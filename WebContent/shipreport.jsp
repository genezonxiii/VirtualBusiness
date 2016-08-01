<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ page import="tw.com.aber.productunit.controller.*"%>
<%@ page import="java.util.*"%>
<%@ page import="java.sql.DriverManager"%>
<%@ page import="java.sql.Connection"%>
<%@ page import="java.sql.Statement"%>
<%@ page import="java.sql.ResultSet"%>
<jsp:directive.page import="java.sql.SQLException" />
<!DOCTYPE html>
<html>
<head>
<title>出貨報表</title>
<meta charset="utf-8">
<link rel="stylesheet" href="css/styles.css" />
<link href="<c:url value="css/css.css" />" rel="stylesheet">
<link href="<c:url value="css/jquery.dataTables.min.css" />" rel="stylesheet">
<link href="<c:url value="css/1.11.4/jquery-ui.css" />" rel="stylesheet">
<script type="text/javascript" src="js/jquery-1.10.2.js"></script>
<script type="text/javascript" src="js/jquery-1.11.4.js"></script>
<script type="text/javascript" src="js/jquery.dataTables.min.js"></script>
<script type="text/javascript" src="js/jquery-ui.min.js"></script>
<script type="text/javascript" src="js/jquery-migrate-1.4.1.min.js"></script>
<script type="text/javascript" src="js/jquery.validate.min.js"></script>
<script type="text/javascript" src="js/additional-methods.min.js"></script>
<script type="text/javascript" src="js/messages_zh_TW.min.js"></script>
<script type="text/javascript" src="js/jquery.table2excel.js"></script>

<script>
function date_format(str) {
	var words=str.replace(","," ").split(" ");
	return words[3]+"-"+words[0].replace("一月","1").replace("二月","2").replace("三月","3").replace("四月","4").replace("五月","5").replace("六月","6").replace("七月","7").replace("八月","8").replace("九月","9").replace("十月","10").replace("十一月","11").replace("十二月","12")+"-"+words[1];
}
	$(function() {
		$( "#datepicker1" ).datepicker({dateFormat: 'yy/mm/dd'});
		$( "#datepicker2" ).datepicker({dateFormat: 'yy/mm/dd'});
		//查詢相關設定
		$("#searh-productunit").button().on("click",function(e) {
			e.preventDefault();
			$(".validateTips").text("請稍候片刻");
			$.ajax({
				type : "POST",
				url : "shipreport.do",
				data : {action :"searh",time1 : $('#datepicker1').val(),time2 : $('#datepicker2').val()},
				success : function(result) {
					if(result.indexOf("WebService")!=-1){
						$(".validateTips").text("WebService Error: "+result);
						return
					};
					var json_obj = $.parseJSON(result);
					var result_table = "";
					$.each(json_obj,function(i, item) {
						result_table += "<tr><td style='min-width:80px;word-break: break-all;'>"+ json_obj[i].order_no 
						+ "</td><td>" + json_obj[i].c_product_id.replace("NULL","")
						+ "</td><td>" + json_obj[i].sale_date.replace("T00:00:00Z","")
						+ "</td><td>" + json_obj[i].dis_date.replace("T00:00:00Z","")

						+ "</td><td>" + json_obj[i].price
						+ "</td><td>" + ((json_obj[i].phone==null)?"":json_obj[i].phone)
						+ "</td><td style='min-width:200px;word-break: break-all;'>" + json_obj[i].address
						+ "</td><td>" + json_obj[i].post
						+ "</td><td style='min-width:80px;word-break:keep-all;'>" + json_obj[i].name
						+ "</td><td>" + json_obj[i].mobile
						+ "</td><td style='min-width:100px;word-break: break-all;'>" + json_obj[i].product_name
						+ "</td><td  style='min-width:80px;word-break: break-all;'>" + json_obj[i].seq_no
						+ "</td><td>" + json_obj[i].quantity
						+ "</td><td>" + json_obj[i].order_source
						+ "</td><td>" + ((json_obj[i].memo==null)?"":json_obj[i].memo)
						+"</td></tr>";
					});
					$(".validateTips").text("");
					$("#my").html("<tr class='noExl'><td></td></tr><tr><td>出貨單號</td><td>自訂產品ID</td><td>出貨日期</td><td>配送日期</td><td>價格</td><td>電話</td><td>地址</td><td>郵編</td><td>顧客姓名</td><td>手機</td><td>產品名稱</td><td>銷貨單號</td><td>數量</td><td>銷貨平台</td><td>訂單備註</td></tr>"+result_table);
					$("#products").dataTable().fnDestroy();
					if(json_obj.length!=0){
						$("#products-contain").show();
						$("#products tbody").html(result_table);
						$("#products").dataTable({

							"language": {"url": "js/dataTables_zh-tw.txt"}});
						$(".validateTips").text("");
						$("#xls").show();
					}else{
						$("#products-contain").hide();
						$(".validateTips").text("查無此結果");
						$("#xls").hide();
					}
				}
			});
		});
		$("#dialog-confirm").html("<p>是否確認刪....</p>");
		$("#dialog-confirm").dialog({
			title: "你妳你妳你",
			draggable : false,//防止拖曳
			resizable : false,//防止縮放
			autoOpen : false,
			height : "auto",
			modal : true,
			show : {effect : "blind",duration : 1000},
 			hide : {effect : "blind",duration : 1000},
			buttons : {
				"確認刪除" : function() {
					alert("嘿嘿嘿~");
					$(this).dialog("close");
				},
				"取消刪除" : function() {
					$(this).dialog("close");
				}
			}
		});
		//刪除事件聆聽 : 因為聆聽事件動態產生，所以採用delegate來批量處理，節省資源
		$("#products").delegate(".btn_delete", "click", function() {
			uuid = $(this).val();
			confirm_dialog.dialog("open");
		});
		//修改事件聆聽
		$("#products").delegate(".btn_update", "click", function() {
			uuid = $(this).val();
			update_dialog.dialog("open");
			var text = $(this).attr("name");
			$("input[name='original_unit_name']").val(text);
		});
		//新增事件聆聽
		$("#create-productunit").button().on("click", function() {
			insert_dialog.dialog("open");
		});
		//預設表格隱藏
		$("#products-contain").hide();
	});
	function export_xls(){
		$(".result").table2excel({
			exclude: ".noExl",
			name: "Excel Document Name",
			filename: "出貨報表",
			fileext: ".xls",
			exclude_img: true,
			exclude_links: true,
			exclude_inputs: true
		});
	}
</script>
<style>
.row {
    border-bottom: 0px;
    margin-bottom: 0px;
    padding-bottom: 0px;
}
::-webkit-input-placeholder {
   text-align: center;
}

:-moz-placeholder { /* Firefox 18- */
   text-align: center;  
}

::-moz-placeholder {  /* Firefox 19+ */
   text-align: center;  
}

:-ms-input-placeholder {  
   text-align: center; 
}
</style>
</head>
<body style="font-size: 15px;">
<script>
$(function() {
	var value='<%=request.getParameter("action")%>';
	//alert($.url.param('action'));
	if(value=="today"){
		$.ajax({
			type : "POST",
			url : "shipreport.do",
			data : {action :"today",time1 : $('#datepicker1').val(),time2 : $('#datepicker2').val()},
			success : function(result) {
				var json_obj = $.parseJSON(result);
				var result_table = "";
				$.each(json_obj,function(i, item) {
					result_table += "<tr><td style='min-width:80px;word-break: break-all;'>"+ json_obj[i].order_no 
					+ "</td><td>" + json_obj[i].c_product_id.replace("NULL","")
					+ "</td><td>" + json_obj[i].sale_date.replace("T00:00:00Z","")
					+ "</td><td>" + json_obj[i].dis_date.replace("T00:00:00Z","")

					+ "</td><td>" + json_obj[i].price
					+ "</td><td>" + ((json_obj[i].phone==null)?"":json_obj[i].phone)
					+ "</td><td style='min-width:200px;word-break: break-all;'>" + json_obj[i].address
					+ "</td><td>" + json_obj[i].post
					+ "</td><td style='min-width:200px;word-break:keep-all;'>" + json_obj[i].name
					+ "</td><td>" + json_obj[i].mobile
					+ "</td><td style='min-width:100px;word-break: break-all;'>" + json_obj[i].product_name
					+ "</td><td  style='min-width:80px;word-break: break-all;'>" + json_obj[i].seq_no
					+ "</td><td>" + json_obj[i].quantity
					+ "</td><td>" + json_obj[i].order_source
					+ "</td><td>" + ((json_obj[i].memo==null)?"":json_obj[i].memo)
					+"</td></tr>";
				});
				$(".validateTips").text("");
				$("#my").html("<tr class='noExl'><td></td></tr><tr><td>出貨單號</td><td>自訂產品ID</td><td>出貨日期</td><td>配送日期</td><td>價格</td><td>電話</td><td>地址</td><td>郵編</td><td>顧客姓名</td><td>手機</td><td>產品名稱</td><td>銷貨單號</td><td>數量</td><td>銷貨平台</td><td>訂單備註</td></tr>"+result_table);
				$("#products").dataTable().fnDestroy();
				if(json_obj.length!=0){
					$("#products-contain").show();
					$("#products tbody").html(result_table);
					$("#products").dataTable({"language": {"url": "js/dataTables_zh-tw.txt"}});
					$(".validateTips").text("");
					$("#xls").show();
				}else{
					$("#products-contain").hide();
					$(".validateTips").text("查無此結果");
					$("#xls").hide();
				}
			}
		});
		//alert('叫ajax去顯示今天的資料');
	}
});
</script>
<!--% 
	String str=(String)request.getParameter("action");
	if(str!=null){
		if("today".equals(str)){
			out.println("<script>alert('顯示今天的資料')</script>");
		}
	}
%-->
<div style="margin:20px;">
	<div class="panel-title">
		<h2 style="font-size: 25px;">出貨報表</h2>
	</div>
	<div class="panel-content">
		<div class="datalistWrap" >
			<!-- 第一列 -->
			<div class="row" >
				<div id="products-serah-create-contain" style="width: 800px;margin:20px auto;" >
					<table id="products-serah-create" class="ui-widget ui-widget-content">
						<tr class="ui-widget-header">
							<th >
								<p style="width:120px;">出貨日</p>
							</th><th>
								<input type="text" id="datepicker1" placeholder="起">
							</th><th>
								~
							</th><th>
								<input type="text" id="datepicker2" placeholder="迄">
							</th><th>
								<button id="searh-productunit" onclick="" style="width:80px;">查詢</button>
							</th><th id="xls" style="display:none">
								<button onclick="export_xls()" style="width:100px;" >產生報表</button>
							</th>
						</tr>
					</table>
				</div>
			</div>
			<!-- 第二列 -->
			<div class="row" align="left" >
				<div id="products-contain" class="ui-widget" style="width:auto;">
					<table id="products" class="ui-widget ui-widget-content">
						<thead>
							<tr class="ui-widget-header">
								<th>出貨單號</th>
								<th>自訂產品ID</th>
								<th>出貨日期</th>
								<th>配送日期</th>
								<th>價格</th>
								<th>電話</th>
								<th>地址</th>
								<th>郵編</th>
								<th>顧客姓名</th>
								<th>手機</th>
								<th>產品名稱</th>
								<th>銷貨單號</th>
								<th>數量</th>	
								<th>銷貨平台</th>
								<th>訂單備註</th>		
							</tr>
						</thead>
						<tbody>
						</tbody>
					</table>
				</div>
				<div class="validateTips" align="center"> </div>
			</div>
		</div>
	</div>
<div id="dialog-confirm"></div>
<div style="display:none">
	<table id="my" class="result">
	<tr><td></td></tr>
	</table>
</div>
</div>
</body>
</html>