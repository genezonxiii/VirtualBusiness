<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ page import="tw.com.aber.salereturn.controller.*"%>
<%@ page import="java.util.*"%>
<%@ page import="java.sql.DriverManager"%>
<%@ page import="java.sql.Connection"%>
<%@ page import="java.sql.Statement"%>
<%@ page import="java.sql.ResultSet"%>
<jsp:directive.page import="java.sql.SQLException" />
<!DOCTYPE html>
<html>
<head>
<title>應付帳款報表</title>
<meta charset="utf-8">
<link rel="Shortcut Icon" type="image/x-icon" href="./images/Rockettheme-Ecommerce-Shop.ico" />
<link rel="stylesheet" href="css/jquery.dataTables.min.css" />
<link rel="stylesheet" href="css/buttons.dataTables.min.css" />
<link href="<c:url value="css/css.css" />" rel="stylesheet">
<link href="<c:url value="css/jquery.dataTables.min.css" />" rel="stylesheet">
<link href="<c:url value="css/1.12.0/jquery-ui.css" />" rel="stylesheet">
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
		$("#amount_date_form").validate({
				rules : {
					amount_start_date : {
						dateISO : true
					},
					amount_end_date:{
						dateISO : true
					}
				},
				messages:{
					amount_start_date : {
						dateISO : "日期格式錯誤"
					},
					amount_end_date : {
						dateISO : "日期格式錯誤"
					}
				}
			});
		 $("#pay_date_form").validate({
				rules : {
					pay_start_date : {
						dateISO : true
					},
					pay_end_date:{
						dateISO : true
					}
				},
				messages:{
					pay_start_date : {
						dateISO : "日期格式錯誤"
					},
					pay_end_date : {
						dateISO : "日期格式錯誤"
					}
				}
			});			
	var table;
	//應付帳款日查詢相關設定
	$("#searh_amount_date").click(function(e) {
		e.preventDefault();
		if($("#amount_date_form").valid()){
			$.ajax({
				type : "POST",
				url : "accpay.do",
				data : {
					action : "searh_amount_date",
					start_date: $("#amount_start_date").val(),
					end_date: $("#amount_end_date").val()
				},
				success : function(result) {
						//alert(result);
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
									if(json_obj[i].amount==null){
										json_obj[i].amount="";
									}
									if(json_obj[i].amount_date==null){
										json_obj[i].amount_date="";
									}
									if(json_obj[i].pay_amount==null){
										json_obj[i].pay_amount="";
									}
									if(json_obj[i].pay_date==null){
										json_obj[i].pay_date="";
									}
									if(json_obj[i].memo==null){
										json_obj[i].memo="";
									}
									result_table 
									+= "<tr>"
									+ "<td name='"+ json_obj[i].amount +"'>"+ money(json_obj[i].amount)+ "</td>"
									+ "<td name='"+ json_obj[i].amount_date +"'>"+ json_obj[i].amount_date+ "</td>"
									+ "<td name='"+ json_obj[i].pay_amount +"'>"+ money(json_obj[i].pay_amount)+ "</td>"
									+ "<td name='"+ json_obj[i].pay_date +"'>"+ json_obj[i].pay_date+ "</td>"
									+ "<td name='"+ json_obj[i].supply_name +"'>"+ json_obj[i].supply_name+ "</td>"
									+ "<td name='"+ json_obj[i].memo +"'>"+ json_obj[i].memo+ "</td></tr>";
								}
							});
						}
						if(json_obj[resultRunTime-1].message=="如要以日期查詢，請完整填寫起日欄位"){
							$("#account_amount_date_contain").hide();
							$("#account_pay_date_contain").hide();
                			if(!$("#search_amount_err_mes").length){
                				$("<p id='search_amount_err_mes'>如要以日期查詢，請完整填寫起日欄位</p>").appendTo($("#amount_date_form").parent());
                			}else{
                				$("#search_amount_err_mes").html("如要以日期查詢，請完整填寫起日欄位");
                			}
						}
						if(json_obj[resultRunTime-1].message=="如要以日期查詢，請完整填寫訖日欄位"){
							$("#account_amount_date_contain").hide();
							$("#account_pay_date_contain").hide();
							if(!$("#search_amount_err_mes").length){
                				$("<p id='search_amount_err_mes'>如要以日期查詢，請完整填寫訖日欄位</p>").appendTo($("#amount_date_form").parent());
                			}else{
                				$("#search_amount_err_mes").html("如要以日期查詢，請完整填寫訖日欄位");
                			}
						}						
						if(json_obj[resultRunTime-1].message=="起日不可大於訖日"){
							$("#account_amount_date_contain").hide();
							$("#account_pay_date_contain").hide();
							if(!$("#search_amount_err_mes").length){
                				$("<p id='search_amount_err_mes'>起日不可大於訖日</p>").appendTo($("#amount_date_form").parent());
                			}else{
                				$("#search_amount_err_mes").html("起日不可大於訖日");
                			}
						}							
						if(resultRunTime==0){
							$("#account_amount_date_contain").hide();
							$("#account_pay_date_contain").hide();
							if(!$("#search_amount_err_mes").length){
                				$("<p id='search_amount_err_mes'>查無此結果</p>").appendTo($("#amount_date_form").parent());
                			}else{
                				$("#search_amount_err_mes").html("查無此結果");
                			}
						}
						if ($.fn.DataTable.isDataTable("#account_amount_date_table")) {
							$("#account_amount_date_table").dataTable().fnDestroy();
						}
						if(resultRunTime!=0&&json_obj[resultRunTime-1].message=="驗證通過"){
							$("#account_amount_date_contain").show();
							$("#account_amount_date_table tbody").html(result_table);
							draw_table("account_amount_date_table","應付未付報表");
							$("#account_amount_date_table").find("td").css("text-align","center");
							$("#account_amount_date_table").find("th").css({"word-break":"break-all","min-width":"30px","text-align":"center" });
							$("#account_amount_date_table").find("td").css({"word-break":"break-all","min-width":"30px","text-align":"center" });
							$("td > label").css({"float":"none","display":"inline","margin":"0px 0px 0px 5px"});
							if($("#search_amount_err_mes").length){
                				$("#search_amount_err_mes").remove();
                			}
						}
					}
				});
		}
	});	
	//實付帳款日查詢相關設定
	$("#searh_pay_date").click(function(e) {
		e.preventDefault();
		if($("#pay_date_form").valid()){
			$.ajax({
				type : "POST",
				url : "accpay.do",
				data : {
					action : "searh_pay_date",
					start_date: $("#pay_start_date").val(),
					end_date: $("#pay_end_date").val()
				},
				success : function(result) {
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
									if(json_obj[i].amount==null){
										json_obj[i].amount="";
									}
									if(json_obj[i].amount_date==null){
										json_obj[i].amount_date="";
									}
									if(json_obj[i].pay_amount==null){
										json_obj[i].pay_amount="";
									}
									if(json_obj[i].pay_date==null){
										json_obj[i].pay_date="";
									}
									if(json_obj[i].memo==null){
										json_obj[i].memo="";
									}
									result_table 
									+= "<tr>"
									+ "<td name='"+ json_obj[i].amount +"'>"+ money(json_obj[i].amount)+ "</td>"
									+ "<td name='"+ json_obj[i].amount_date +"'>"+ json_obj[i].amount_date+ "</td>"
									+ "<td name='"+ json_obj[i].pay_amount +"'>"+ money(json_obj[i].pay_amount)+ "</td>"
									+ "<td name='"+ json_obj[i].pay_date +"'>"+ json_obj[i].pay_date+ "</td>"
									+ "<td name='"+ json_obj[i].supply_name +"'>"+ json_obj[i].supply_name+ "</td>"
									+ "<td name='"+ json_obj[i].memo +"'>"+ json_obj[i].memo+ "</td></tr>";
								}
							});
						}
						if(json_obj[resultRunTime-1].message=="如要以日期查詢，請完整填寫起日欄位"){
							$("#account_amount_date_contain").hide();
							$("#account_pay_date_contain").hide();
							if(!$("#pay_err_mes").length){
                				$("<p id='pay_err_mes'>如要以日期查詢，請完整填寫起日欄位</p>").appendTo($("#pay_date_form").parent());
                			}else{
                				$("#pay_err_mes").html("如要以日期查詢，請完整填寫起日欄位");
                			}
						}
						if(json_obj[resultRunTime-1].message=="如要以日期查詢，請完整填寫訖日欄位"){
							$("#account_amount_date_contain").hide();
							$("#account_pay_date_contain").hide();
							if(!$("#pay_err_mes").length){
                				$("<p id='pay_err_mes'>如要以日期查詢，請完整填寫訖日欄位</p>").appendTo($("#pay_date_form").parent());
                			}else{
                				$("#pay_err_mes").html("如要以日期查詢，請完整填寫訖日欄位");
                			}
						}						
						if(json_obj[resultRunTime-1].message=="起日不可大於訖日"){
							$("#account_amount_date_contain").hide();
							$("#account_pay_date_contain").hide();
							if(!$("#pay_err_mes").length){
                				$("<p id='pay_err_mes'>起日不可大於訖日</p>").appendTo($("#pay_date_form").parent());
                			}else{
                				$("#pay_err_mes").html("起日不可大於訖日");
                			}
						}							
						if(resultRunTime==0){
							$("#account_amount_date_contain").hide();
							$("#account_pay_date_contain").hide();
							if(!$("#pay_err_mes").length){
                				$("<p id='pay_err_mes'>查無此結果</p>").appendTo($("#pay_date_form").parent());
                			}else{
                				$("#pay_err_mes").html("查無此結果");
                			}
						}
						if ($.fn.DataTable.isDataTable("#account_pay_date_table")) {
							$("#account_pay_date_table").dataTable().fnDestroy();
						}
						if(resultRunTime!=0&&json_obj[resultRunTime-1].message=="驗證通過"){
							$("#account_pay_date_contain").show();
							$("#account_pay_date_table tbody").html(result_table);
							draw_table("account_pay_date_table","應付已付報表");
							$("#account_pay_date_table").find("td").css("text-align","center");
							$("#account_pay_date_table").find("th").css({"word-break":"break-all","min-width":"30px","text-align":"center" });
							$("#account_pay_date_table").find("td").css({"word-break":"break-all","min-width":"30px","text-align":"center" });
							$("td > label").css({"float":"none","display":"inline","margin":"0px 0px 0px 5px"});
							if($("#pay_err_mes").length){
                				$("#pay_err_mes").remove();
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
})
</script>
	<input id="radio-1" type="radio" name="group" onclick="$('#div1').show();$('#div2').hide();" checked><label for="radio-1"><span class="form-label">應收未收</span></label>
	<input id="radio-2" type="radio" name="group" onclick="$('#div1').hide();$('#div2').show();" ><label for="radio-2"><span class="form-label">應收已收</span></label>
		<div class="datalistWrap" id="div1">
			<!-- 第一列 -->
				<div class="input-field-wrap">
					<div class="form-wrap">
						<form id="amount_date_form" name="trans_list_date_form">
							<div class="form-row">
								<label for="">
									<span class="block-label">應付起日</span>
									<input type="text" class="input-date" id="amount_start_date" name="amount_start_date">
								</label>
								<div class="forward-mark"></div>
								<label for="">
									<span class="block-label">應付迄日</span>
									<input type="text" class="input-date" id="amount_end_date" name="amount_end_date">
								</label>
								<button id="searh_amount_date" class="btn btn-darkblue">查詢</button>
							</div>
						</form>
					</div><!-- /.form-wrap -->
				</div><!-- /.input-field-wrap -->
			<!-- 第二列 -->
			
			<div class="row search-result-wrap" style="height:433px;">
				<div id="account_amount_date_contain" class="result-table-wrap" style="display:none;">
					<table id="account_amount_date_table" class="result-table">
						<thead>
							<tr class="ui-widget-header">
								<th>應付帳款金額</th>
								<th>應付帳款產生日期</th>
								<th>實付帳款金額</th>
								<th>實付帳款產生日期</th>
								<th>供應商名稱</th>
								<th>備註</th>
							</tr>
						</thead>
						<tbody>
						</tbody>
					</table>
				</div>
			</div>
		</div>
		<div class="datalistWrap" id="div2" style="display:none">
			<!-- 第三列 -->
			<!-- 第四列 -->
			<div class="input-field-wrap">
					<div class="form-wrap">
						<form id="pay_date_form" name="trans_list_date_form">
							<div class="form-row">
								<label for="">
									<span class="block-label">實付起日</span>
									<input type="text" class="input-date" id="pay_start_date" name="pay_start_date">
								</label>
								<div class="forward-mark"></div>
								<label for="">
									<span class="block-label">實付迄日</span>
									<input type="text" class="input-date" id="pay_end_date" name="pay_end_date">
								</label>
								<button id="searh_pay_date" class="btn btn-darkblue">查詢</button>				
							</div>
						</form>
					</div><!-- /.form-wrap -->
				</div><!-- /.input-field-wrap -->
			<div class="row search-result-wrap" style="height:433px;">
				<div id="account_pay_date_contain" class="result-table-wrap" style="display:none;">
					<table id="account_pay_date_table" class="result-table">
						<thead>
							<tr class="ui-widget-header">
								<th>應付帳款金額</th>
								<th>應付帳款產生日期</th>
								<th>實付帳款金額</th>
								<th>實付帳款產生日期</th>
								<th>供應商名稱</th>
								<th>備註</th>
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