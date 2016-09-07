<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ page import="tw.com.aber.purchase.controller.*"%>
<%@ page import="java.util.*"%>
<%@ page import="java.sql.DriverManager"%>
<%@ page import="java.sql.Connection"%>
<%@ page import="java.sql.Statement"%>
<%@ page import="java.sql.ResultSet"%>
<jsp:directive.page import="java.sql.SQLException" />
<!DOCTYPE html>
<html>
<head>
<title>進貨退回</title>
<meta charset="utf-8">
<link rel="Shortcut Icon" type="image/x-icon" href="./images/Rockettheme-Ecommerce-Shop.ico" />

<link rel="stylesheet" href="css/jquery.dataTables.min.css" />
<link rel="stylesheet" href="css/buttons.dataTables.min.css" />
<link href="<c:url value="css/css.css" />" rel="stylesheet">
<link href="<c:url value="css/jquery.dataTables.min.css" />" rel="stylesheet">
<link href="<c:url value="css/1.11.4/jquery-ui.css" />" rel="stylesheet">
<link rel="stylesheet" href="css/styles.css" />
</head>
<body>
	<jsp:include page="template.jsp" flush="true"/>
	<div class="content-wrap" >
		<div class='bdyplane' style="opacity:0">
		
<script type="text/javascript" src="js/jquery-1.12.4.js"></script>
<script type="text/javascript" src="js/jquery.dataTables.min.js"></script>
<script type="text/javascript" src="js/dataTables.buttons.min.js"></script>
<script type="text/javascript" src="js/buttons.flash.min.js"></script>
<script type="text/javascript" src="js/jszip.min.js"></script>
<script type="text/javascript" src="js/pdfmake.min.js"></script>
<script type="text/javascript" src="js/vfs_fonts.js"></script>
<script type="text/javascript" src="js/buttons.html5.min.js"></script>
<script type="text/javascript" src="js/buttons.print.min.js"></script>
<script type="text/javascript" src="js/jquery-ui.js"></script>
<script type="text/javascript" src="js/jquery-migrate-1.4.1.min.js"></script>
<script type="text/javascript" src="js/jquery.validate.min.js"></script>
<script type="text/javascript" src="js/additional-methods.min.js"></script>
<script type="text/javascript" src="js/messages_zh_TW.min.js"></script>
<script type="text/javascript" src="js/jquery.table2excel.js"></script>

<script type="text/javascript" src="js/dataTables.buttons.min.js"></script>
<script type="text/javascript" src="js/buttons.jqueryui.min.js"></script>
<script>
$(function(){
	$(".bdyplane").animate({"opacity":"1"});
	 $("#return_date_form").validate({
			rules : {
				return_staet_date : {
					dateISO : true
				},
				return_end_date:{
					dateISO : true
				}
			},
			messages:{
				return_staet_date : {
					dateISO : "日期格式錯誤"
				},
				return_end_date : {
					dateISO : "日期格式錯誤"
				}
			}
		});		
	//退貨日查詢相關設定
	$("#search_return_date").click(function(e) {
		e.preventDefault();
		if($("#return_date_form").valid()){
			$.ajax({
				type : "POST",
				url : "purchreturn.do",
				data : {
					action : "search_return_date",
					return_start_date: $("#return_start_date").val(),
					return_end_date: $("#return_end_date").val()
				},
				success : function(result) {
						var json_obj = $.parseJSON(result);
						//判斷查詢結果
						var resultRunTime = 0;
						$.each (json_obj, function (i) {
							resultRunTime+=1;
						});
						if(json_obj[resultRunTime-1].message=="驗證通過"){
							var len=json_obj.length;
							var result_table = "";
							$.each(json_obj,function(i, item) {
								if(i<len-1){
									if(json_obj[i].seq_no==null){
										json_obj[i].seq_no="";
									}
									if(json_obj[i].memo==null){
										json_obj[i].memo="";
									}
									if(json_obj[i].purchase_date==null){
										json_obj[i].purchase_date="";
									}
									if(json_obj[i].invoice==null){
										json_obj[i].invoice="";
									}
									if(json_obj[i].invoice_type==null||json_obj[i].invoice_type=="0"){
										json_obj[i].invoice_type="";
									}
									if(json_obj[i].invoice_type=="1"){
										json_obj[i].invoice_type="二聯式發票";
									}
									if(json_obj[i].invoice_type=="2"){
										json_obj[i].invoice_type="三聯式發票";
									}
									if(json_obj[i].amount==null){
										json_obj[i].amount="";
									}
									result_table 
									+= "<tr>"
									+ "<td name='"+ json_obj[i].seq_no +"'>"+ json_obj[i].seq_no+ "</td>"
									+ "<td name='"+ json_obj[i].purchase_date +"'>"+ json_obj[i].purchase_date+ "</td>"
									+ "<td name='"+ json_obj[i].invoice +"'>"+ json_obj[i].invoice+ "</td>"
									+ "<td name='"+ json_obj[i].invoice_type +"'>"+ json_obj[i].invoice_type+ "</td>"
									+ "<td name='"+ json_obj[i].amount +"'>"+ money(json_obj[i].amount)+ "</td>"
									+ "<td name='"+ json_obj[i].memo +"'>"+ json_obj[i].memo+ "</td></tr>"
								}
							});
						}
						if(json_obj[resultRunTime-1].message=="如要以日期查詢，請完整填寫起日欄位"){
							$("#purchasereturns_false_contain").hide();
							$("#purchasereturns_true_contain").hide();
							if(!$("#purchase_return_date_err_mes").length){
                				$("<p id='purchase_return_date_err_mes'>如要以日期查詢，完整填寫起日欄位</p>").appendTo($("#return_date_form").parent());
                			}else{
                				$("#purchase_return_date_err_mes").html("如要以日期查詢，完整填寫起日欄位");
                			}
						}
						if(json_obj[resultRunTime-1].message=="如要以日期查詢，請完整填寫訖日欄位"){
							$("#purchasereturns_false_contain").hide();
							$("#purchasereturns_true_contain").hide();
							if(!$("#purchase_return_date_err_mes").length){
                				$("<p id='purchase_return_date_err_mes'>如要以日期查詢，請完整填寫訖日欄位</p>").appendTo($("#return_date_form").parent());
                			}else{
                				$("#purchase_return_date_err_mes").html("如要以日期查詢，請完整填寫訖日欄位");
                			}
						}						
						if(json_obj[resultRunTime-1].message=="起日不可大於訖日"){
							$("#purchasereturns_false_contain").hide();
							$("#purchasereturns_true_contain").hide();
							if(!$("#purchase_return_date_err_mes").length){
                				$("<p id='purchase_return_date_err_mes'>起日不可大於訖日</p>").appendTo($("#return_date_form").parent());
                			}else{
                				$("#purchase_return_date_err_mes").html("起日不可大於訖日");
                			}
						}							
						if(resultRunTime>2){
							$("#purchasereturns_false_contain").hide();
							$("#purchasereturns_true_contain").hide();
							if(!$("#purchase_return_date_err_mes").length){
                				$("<p id='purchase_return_date_err_mes'>查無此結果</p>").appendTo($("#return_date_form").parent());
                			}else{
                				$("#purchase_return_date_err_mes").html("查無此結果");
                			}
						}
						$("#purchasereturns_true_table").dataTable().fnDestroy();
						if(resultRunTime!=0&&json_obj[resultRunTime-1].message=="驗證通過"){
							$("#purchasereturns_true_contain").show();
							$("#purchasereturns_true_table tbody").html(result_table);
							$("#purchasereturns_true_table").find("th").css({"word-break":"break-all","min-width":"30px","text-align":"center" });
							$("#purchasereturns_true_table").find("td").css({"word-break":"break-all","min-width":"30px","text-align":"center" });
							draw_table("purchasereturns_true_table","進貨退回報表");
									$("td > label").css({"float":"none","display":"inline","margin":"0px 0px 0px 5px"});
							if($("#purchase_return_date_err_mes").length){
                				$("#purchase_return_date_err_mes").remove();
                			}
						}
					}
				});
		}
	});		
	//get today yyyy-mm-dd
	function getCurrentDate(){
		var fullDate = new Date();
		var twoDigitMonth = fullDate.getMonth()+1;	if(twoDigitMonth.length==1)	twoDigitMonth="0" +twoDigitMonth;
		var twoDigitDate = fullDate.getDate()+"";	if(twoDigitDate.length==1)	twoDigitDate="0" +twoDigitDate;
		var currentDate = fullDate.getFullYear() + "-" + twoDigitMonth + "-" + twoDigitDate;
		return currentDate;
	}
	//日期設定
	//hold header
	$("#purchasereturns_true_table").find("th").css("min-width","120px");	
})
</script>
		<div class="datalistWrap">
		<div class="input-field-wrap">
			<div class="form-wrap" >
				<div class="form-row">
					<form id="return_date_form" name="trans_dis_date_form">
						<label for="">
							<span class="block-label">退貨起日</span>
							<input type="text" class="input-date" id="return_start_date" name="return_start_date">
						</label>
						<div class="forward-mark"></div>
						<label for="">
							<span class="block-label">退貨迄日</span>
							<input type="text" class="input-date" id="return_end_date" name="return_end_date">
						</label>
						<button id="search_return_date" class="btn btn-darkblue">查詢</button>		
					</form>
				</div>
			</div><!-- /.form-wrap -->
		</div><!-- /.input-field-wrap -->
			<div class="row search-result-wrap" style="height:433px;">
				<div id="purchasereturns_true_contain" class="result-table-wrap" style="display:none;">
					<table id="purchasereturns_true_table" class="result-table">
						<thead>
							<tr class="ui-widget-header">
								<th>進貨單號</th>
								<th>進貨日期</th>
								<th>進貨發票號碼</th>
								<th>發票樣式</th>
								<th style="min-width:70px;">進貨發票金額</th>
								<th>備註說明</th>
							</tr>
						</thead>
						<tbody>
						</tbody>
					</table>
				</div>
			</div>		
		</div>
	</div>
	</div>
</body>
</html>