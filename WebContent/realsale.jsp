<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ page import="tw.com.aber.realsale.controller.*"%>
<%@ page import="java.util.*"%>
<%@ page import="java.sql.DriverManager"%>
<%@ page import="java.sql.Connection"%>
<%@ page import="java.sql.Statement"%>
<%@ page import="java.sql.ResultSet"%>
<jsp:directive.page import="java.sql.SQLException" />
<!DOCTYPE html>
<html>
<head>
<title>管理</title>
<meta charset="utf-8">
<link rel="Shortcut Icon" type="image/x-icon"
	href="./images/Rockettheme-Ecommerce-Shop.ico" />
<link href="<c:url value="css/css.css" />" rel="stylesheet">
<link href="<c:url value="css/jquery.dataTables.min.css" />"
	rel="stylesheet">
<link href="<c:url value="css/1.11.4/jquery-ui.css" />" rel="stylesheet">
<link rel="stylesheet" href="css/1.11.4/jquery-ui.css">
<link rel="stylesheet" href="css/styles.css">

</head>
<body>
<script type="text/javascript" src="js/jquery-1.10.2.js"></script>
<script type="text/javascript" src="js/jquery.dataTables.min.js"></script>
<script type="text/javascript" src="js/jquery-ui.min.js"></script>
<script type="text/javascript" src="js/jquery-migrate-1.4.1.min.js"></script>
<script type="text/javascript" src="js/jquery.validate.min.js"></script>
<script type="text/javascript" src="js/additional-methods.min.js"></script>
<script type="text/javascript" src="js/messages_zh_TW.min.js"></script>
<script type="text/javascript" src="js/jquery.scannerdetection.js"></script>
<script>
function draw_sale(parameter){
	$("#sales_contain_row").css({"opacity":"0"});
// 	warning_msg("---讀取中請稍候---");
	$.ajax({
		type : "POST",
		url : "realsale.do",
		data : parameter,
		success : function(result) {
				//console.log(result);
				var json_obj = $.parseJSON(result);
				var len=json_obj.length;
				//判斷查詢結果
				var resultRunTime = 0;
				$.each (json_obj, function (i) {
					resultRunTime+=1;
				});
				if(json_obj[resultRunTime-1].message=="驗證通過"){					
					var result_table = "";
					$.each(json_obj,function(i, item) {
						if(i<len-1){				
							result_table 
							+= "<tr>"							
							+ "<td name='"+ json_obj[i].order_no +"'>"+ json_obj[i].order_no+ "</td>"
							+ "<td name='"+ json_obj[i].name +"'>"+ json_obj[i].name+ "</td>"
							+ "<td name='"+ json_obj[i].trans_list_date+"'>"+json_obj[i].trans_list_date+"</td>"						
							+ "<td name='"+ json_obj[i].sale_date +"'>"+ json_obj[i].sale_date+ "</td>"
							+ "<td name='"+ json_obj[i].dis_date +"'>"+ json_obj[i].dis_date+ "</td>"
							+ "<td name='"+ json_obj[i].order_source +"'>"+ json_obj[i].order_source+ "</td>"
							+ "<td name='"+ json_obj[i].memo +"'>"+ json_obj[i].memo+ "</td>"
							+ "<td><div class='table-row-func btn-in-table btn-gray'><i class='fa fa-ellipsis-h'></i>"
							+ "	<div class='table-function-list'>"
							+ "		<button class='btn-in-table btn-darkblue btn_update' title='修改' id='"+json_obj[i].seq_no+"'value='"+ json_obj[i].sale_id+"'name='"+ json_obj[i].c_product_id+"' ><i class='fa fa-pencil'></i></button>"
							+ "		<button class='btn-in-table btn-alert btn_delete' title='刪除' id='"+json_obj[i].seq_no+"'value='"+ json_obj[i].sale_id+"'name='"+ json_obj[i].c_product_id+"' val2='"+json_obj[i].order_no+"'><i class='fa fa-trash'></i></button>"
							+ "		<button class='btn-in-table btn-green btn_list' title='清單' id = '" + json_obj[i].sale_id + "'>" 
							+ "		<i class='fa fa-pencil-square-o'></i></button>"
							+ "	</div>"
							+ "</div></td></tr>";
						}
					});
				}
				$("#sales").dataTable().fnDestroy();
				if(resultRunTime!=0&&json_obj[resultRunTime-1].message=="驗證通過"){
					$("#sales_contain_row").show();
					$("#sales tbody").html(result_table);
					$("#sales").dataTable({
						  autoWidth: false,
						  scrollX:  true,
				          scrollY:"300px",
				          "language": {"url": "js/dataTables_zh-tw.txt"}});
					$("#sales").find("td").css("text-align", "center");
					$("#sales tbody").find("td:nth-child(3)").css("text-align", "left");
					$("#sales_contain_row").animate({"opacity":"0.01"},1);
					$("#sales_contain_row").animate({"opacity":"1"},300);
					tooltip("btn_update");
					tooltip("btn_delete");
					tooltip("report");
					warning_msg("");
				}
				
				if(resultRunTime<2){
					$("#sales_contain_row").hide();
					warning_msg("---查無此結果---");
				}
				if(json_obj[resultRunTime-1].message=="如要以日期查詢，請完整填寫起日欄位"){
					$("#sales_contain_row").hide();
					warning_msg("---如要以日期查詢，請完整填寫起日欄位---");
				}
				if(json_obj[resultRunTime-1].message=="如要以日期查詢，請完整填寫訖日欄位"){
					$("#sales_contain_row").hide();
					warning_msg("---如要以日期查詢，請完整填寫訖日欄位---");
				}						
				if(json_obj[resultRunTime-1].message=="起日不可大於訖日"){
					$("#sales_contain_row").hide();
					warning_msg("---起日不可大於訖日---");
				}							
				
			}
		});
}	

$(function() {
	$(".bdyplane").animate({"opacity":"1"});
	var uuid = "";
	var order_no_begin="";
	var order_no_end="";
	var name = "";
	var trans_list_date_begin = "";
	var trans_list_date_end = "";
	var dis_date_begin = "";
	var dis_date_end = "";
	var order_source = "";
	var deliveryway = "";
	
	//訂單編號查詢相關設定
	$("#searh-sale").click(function(e) {		
		e.preventDefault();	
		if($("#searh-sale").attr("c_product_id_error").length>0){
			var tmp="查無商品ID: "+$("#searh-sale").attr("c_product_id_error")+"\n將為您查詢所有訂單";
			if(!confirm(tmp,"繼續","取消") ){
				return;
			}
		}	
		var tmp={
			action : "search",
			order_no_begin : $("input[name='order_no_begin'").val(),
			order_no_end : $("input[name='order_no_end'").val(),
			name : $("input[name='name'").val(),
			trans_list_date_begin : $("input[name='trans_list_date_begin'").val(),
			trans_list_date_end : $("input[name='trans_list_date_end'").val(),
			dis_date_begin : $("input[name='dis_date_begin'").val(),
			dis_date_end : $("input[name='dis_date_end'").val(),
			order_source : $("input[name='order_source'").val(),
			deliveryway : $("input[name='deliveryway'").val(),
		};
		draw_sale(tmp);
	});
});


</script>
	<div class="panel-content">
		<div class="datalistWrap">
			<div class="input-field-wrap">
				<table class='form-table'>
					<tr>
						<td>訂單編號：</td>
						<td><input type="text" id="order_no_begin"
							name="order_no_begin"></td>
						<td><input type="text" id="order_no_end" name="order_no_end"></td>
					</tr>
					<tr>
						<td>客戶姓名：</td>
						<td><input type="text" id="name" name="name"></td>
					</tr>
					<tr>
						<td>轉單日期區間：</td>
						<td><input type="text" class="input-date"
							id="trans_list_date_begin" name="trans_list_date_begin"></td>
						<td><input type="text" class="input-date"
							id="trans_list_date_end" name="trans_list_date_end"></td>
					</tr>
					<tr>
						<td>出貨日期區間：</td>
						<td><input type="text" class="input-date" id="dis_date_begin"
							name="dis_date_begin"></td>
						<td><input type="text" class="input-date" id="dis_date_end"
							name="dis_date_end"></td>
					</tr>
					<tr>
						<td>訂單來源：</td>
						<td><input type="text" id="order_source" name="order_source"></td>
					</tr>
					<tr>
						<td>出貨方式：</td>
						<td><input type="text" id="deliveryway" name="deliveryway"></td>
					</tr>
					<tr>
						<td><button class="btn btn-darkblue" id="searh-sale" c_product_id_error="">查詢</button></td>
						<td><button class="btn btn-exec btn-wide" id="create-sale">新增</button></td>
					</tr>
				</table>
			</div>
			<div class="row search-result-wrap" align="center" id ="sales_contain_row"  style="display:none;">	
				<div id="sales-contain" class="ui-widget">
					<table id="sales" class="result-table" style="width:99.9%;">
						<thead>
							<tr class="">
								<th>訂單編號</th>
								<th>客戶姓名</th>
								<th>轉單日</th>
								<th>銷貨日</th>
								<th>出貨日</th>
								<th>銷貨平台</th>								
								<th style="background-image: none !important;">備註</th>
								<th style="background-image: none !important;">功能</th>
							</tr>
						</thead>
						<tbody>
						</tbody>
					</table>
				</div>
				<div class="validateTips" id="err_msg" align="center"> </div>
			</div>		
		</div>
	</div>
</body>
</html>