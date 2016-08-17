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
<style type="text/css">
.dataTables_wrapper .dt-buttons {float:right;}
</style>
<title>銷貨退回</title>
<meta charset="utf-8">
<link rel="Shortcut Icon" type="image/x-icon" href="./images/Rockettheme-Ecommerce-Shop.ico" />
<link rel="stylesheet" href="css/styles.css" />
<link href="<c:url value="css/css.css" />" rel="stylesheet">
<link href="<c:url value="css/jquery.dataTables.min.css" />" rel="stylesheet">
<link href="<c:url value="css/1.11.4/jquery-ui.css" />" rel="stylesheet">
<link href="<c:url value="css/dataTables.jqueryui.min.css" />" rel="stylesheet">
<link href="<c:url value="css/buttons.jqueryui.min.css" />" rel="stylesheet">
</head>
<body>
	<jsp:include page="template.jsp" flush="true"/>
	<div class="content-wrap" style="margin:56px 0px 28px 120px;">
<script type="text/javascript" src="js/jquery-1.10.2.js"></script>
<script type="text/javascript" src="js/jquery.dataTables.min.js"></script>
<script type="text/javascript" src="js/jquery-ui.min.js"></script>
<script type="text/javascript" src="js/jquery-migrate-1.4.1.min.js"></script>
<script type="text/javascript" src="js/jquery.validate.min.js"></script>
<script type="text/javascript" src="js/additional-methods.min.js"></script>
<script type="text/javascript" src="js/messages_zh_TW.min.js"></script>
<!-- data table extensions -->
<script type="text/javascript" src="js/dataTables.buttons.min.js"></script>
<script type="text/javascript" src="js/buttons.jqueryui.min.js"></script>
<script>
	$(function(){
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
		 $("#trans_list_date_form").validate({
				rules : {
					trans_list_staet_date : {
						dateISO : true
					},
					trans_list_end_date:{
						dateISO : true
					}
				},
				messages:{
					trans_list_staet_date : {
						dateISO : "日期格式錯誤"
					},
					trans_list_end_date : {
						dateISO : "日期格式錯誤"
					}
				}
			});
		 $("#trans_dis_date_form").validate({
				rules : {
					trans_dis_staet_date : {
						dateISO : true
					},
					trans_dis_end_date:{
						dateISO : true
					}
				},
				messages:{
					trans_dis_staet_date : {
						dateISO : "日期格式錯誤"
					},
					trans_dis_end_date : {
						dateISO : "日期格式錯誤"
					}
				}
			});
	var table;
	//退貨日查詢相關設定
	$("#searh_salereturn_date").click(function(e) {
		e.preventDefault();
		if($("#return_date_form").valid()){
			$.ajax({
				type : "POST",
				url : "salereturn.do",
				data : {
					action : "search_sale_return_date",
					return_start_date: $("#return_start_date").val(),
					return_end_date: $("#return_end_date").val()
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
									if(json_obj[i].seq_no==null){
										json_obj[i].seq_no="";
									}
									if(json_obj[i].order_no==null){
										json_obj[i].order_no="";
									}
									if(json_obj[i].product_name==null){
										json_obj[i].product_name="";
									}
									if(json_obj[i].c_product_id==null){
										json_obj[i].c_product_id="";
									}
									if(json_obj[i].quantity==null){
										json_obj[i].quantity="";
									}
									if(json_obj[i].price==null){
										json_obj[i].price="";
									}
									if(json_obj[i].invoice==null){
										json_obj[i].invoice="";
									}
									if(json_obj[i].invoice_date==null){
										json_obj[i].invoice_date="";
									}
									if(json_obj[i].trans_list_date==null){
										json_obj[i].trans_list_date="";
									}
									if(json_obj[i].dis_date==null){
										json_obj[i].dis_date="";
									}
									if(json_obj[i].memo==null){
										json_obj[i].memo="";
									}
									if(json_obj[i].sale_date==null){
										json_obj[i].sale_date="";
									}
									if(json_obj[i].order_source==null){
										json_obj[i].order_source="";
									}
									result_table 
									+= "<tr>"
									+ "<td name='"+ json_obj[i].seq_no +"'>"+ json_obj[i].seq_no+ "</td>"
									+ "<td name='"+ json_obj[i].order_no +"'>"+ json_obj[i].order_no+ "</td>"
									+ "<td name='"+ json_obj[i].product_name +"'>"+ json_obj[i].product_name+ "</td>"
									+ "<td name='"+ json_obj[i].c_product_id +"'>"+ json_obj[i].c_product_id+ "</td>"
									+ "<td name='"+ json_obj[i].quantity +"'>"+ json_obj[i].quantity+ "</td>"
									+ "<td name='"+ json_obj[i].price +"'>"+ json_obj[i].price+ "</td>"
									+ "<td name='"+ json_obj[i].invoice +"'>"+ json_obj[i].invoice+ "</td>"
									+ "<td name='"+ json_obj[i].invoice_date +"'>"+ json_obj[i].invoice_date+ "</td>"
									+ "<td name='"+ json_obj[i].trans_list_date +"'>"+ json_obj[i].trans_list_date+ "</td>"
									+ "<td name='"+ json_obj[i].dis_date +"'>"+ json_obj[i].dis_date+ "</td>"
									+ "<td name='"+ json_obj[i].sale_date +"'>"+ json_obj[i].sale_date+ "</td>"
									+ "<td name='"+ json_obj[i].order_source +"'>"+ json_obj[i].order_source+ "</td>"
									+ "<td name='"+ json_obj[i].memo +"'>"+ json_obj[i].memo+ "</td>"
									+ "<td><input id='my-"+json_obj[i].seq_no+"' type='checkbox' style='margin: 0 auto;' value='"+ json_obj[i].sale_id+ "'name='"+ json_obj[i].c_product_id
									+ "'class='checkbox_salereturn_cancel'></input><label for='my-"+json_obj[i].seq_no+"'></label></td></tr>";	
								}
							});
						}
						if(json_obj[resultRunTime-1].message=="如要以日期查詢，請完整填寫起日欄位"){
							$("#salereturns_false_contain").hide();
							$("#salereturns_true_contain").hide();
							if(!$("#return_date_err_mes").length){
                				$("<p id='return_date_err_mes'>如要以日期查詢，請完整填寫起日欄位</p>").appendTo($("#return_date_form").parent());
                			}else{
                				$("#return_date_err_mes").html("如要以日期查詢，請完整填寫起日欄位");
                			}
						}
						if(json_obj[resultRunTime-1].message=="如要以日期查詢，請完整填寫訖日欄位"){
							$("#salereturns_false_contain").hide();
							$("#salereturns_true_contain").hide();
							if(!$("#return_date_err_mes").length){
                				$("<p id='return_date_err_mes'>如要以日期查詢，請完整填寫訖日欄位</p>").appendTo($("#return_date_form").parent());
                			}else{
                				$("#return_date_err_mes").html("如要以日期查詢，請完整填寫訖日欄位");
                			}
						}						
						if(json_obj[resultRunTime-1].message=="起日不可大於訖日"){
							$("#salereturns_false_contain").hide();
							$("#salereturns_true_contain").hide();
							if(!$("#return_date_err_mes").length){
                				$("<p id='return_date_err_mes'>起日不可大於訖日</p>").appendTo($("#return_date_form").parent());
                			}else{
                				$("#return_date_err_mes").html("起日不可大於訖日");
                			}
						}							
						if(resultRunTime==0){
							$("#salereturns_false_contain").hide();
							$("#salereturns_true_contain").hide();
							if(!$("#return_date_err_mes").length){
                				$("<p id='return_date_err_mes'>查無此結果</p>").appendTo($("#return_date_form").parent());
                			}else{
                				$("#return_date_err_mes").html("查無此結果");
                			}
						}
						$("#salereturns_true_table").dataTable().fnDestroy();
						if(resultRunTime!=0&&json_obj[resultRunTime-1].message=="驗證通過"){
							$("#salereturns_true_contain").show();
							$("#salereturns_true_table tbody").html(result_table);
							$("#salereturns_true_table").dataTable({
								  autoWidth: false,
								  scrollX:  true,
						          scrollY:"300px",
								  dom: 'Blfrtip',
						          buttons: [{
						                text: '取消銷貨退回',
						                className: 'btn_sale_return_cancel',
						                action : function(e){
						        			e.preventDefault();
						        			var count = 0;
						        			var message = "";
						        			$(".checkbox_salereturn_cancel").each(function(){
						        				if($(this).prop("checked")){
						        					count+=1;
						        				}
						        			});
						        			message = "確認要取消銷貨退回嗎? 總共" + count + "筆";
						        			$("#dialog-cancel-confirm p").text(message);
						        			confirm_cancel_dialog.dialog("open");
						                }
						            }
						          ],"language": {"url": "js/dataTables_zh-tw.txt","zeroRecords": "沒有符合的結果"}});
									$("#salereturns_true_table").find("th").css({"word-break":"break-all","min-width":"30px","text-align":"center" });
									$("#salereturns_true_table").find("td").css({"word-break":"break-all","min-width":"30px","text-align":"center" });
									$("td > label").css({"float":"none","display":"inline","margin":"0px 0px 0px 5px"});
							if($("#return_date_err_mes").length){
                				$("#return_date_err_mes").remove();
                			}
						}
					}
				});
		}
	});	
		//轉單日查詢相關設定
		$("#searh_trans_list_date").click(function(e) {
			e.preventDefault();
			if($("#trans_list_date_form").valid()){
				$.ajax({
					type : "POST",
					url : "salereturn.do",
					data : {
						action : "search_trans_list_date",
						trans_list_start_date: $("#trans_list_start_date").val(),
						trans_list_end_date: $("#trans_list_end_date").val()
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
										if(json_obj[i].seq_no==null){
											json_obj[i].seq_no="";
										}
										if(json_obj[i].order_no==null){
											json_obj[i].order_no="";
										}
										if(json_obj[i].product_name==null){
											json_obj[i].product_name="";
										}
										if(json_obj[i].c_product_id==null){
											json_obj[i].c_product_id="";
										}
										if(json_obj[i].quantity==null){
											json_obj[i].quantity="";
										}
										if(json_obj[i].price==null){
											json_obj[i].price="";
										}
										if(json_obj[i].invoice==null){
											json_obj[i].invoice="";
										}
										if(json_obj[i].invoice_date==null){
											json_obj[i].invoice_date="";
										}
										if(json_obj[i].trans_list_date==null){
											json_obj[i].trans_list_date="";
										}
										if(json_obj[i].dis_date==null){
											json_obj[i].dis_date="";
										}
										if(json_obj[i].memo==null){
											json_obj[i].memo="";
										}
										if(json_obj[i].sale_date==null){
											json_obj[i].sale_date="";
										}
										if(json_obj[i].order_source==null){
											json_obj[i].order_source="";
										}
										result_table 
										+= "<tr>"
										+ "<td name='"+ json_obj[i].seq_no +"'>"+ json_obj[i].seq_no+ "</td>"
										+ "<td name='"+ json_obj[i].order_no +"'>"+ json_obj[i].order_no+ "</td>"
										+ "<td name='"+ json_obj[i].product_name +"'>"+ json_obj[i].product_name+ "</td>"
										+ "<td name='"+ json_obj[i].c_product_id +"'>"+ json_obj[i].c_product_id+ "</td>"
										+ "<td name='"+ json_obj[i].quantity +"'>"+ json_obj[i].quantity+ "</td>"
										+ "<td name='"+ json_obj[i].price +"'>"+ json_obj[i].price+ "</td>"
										+ "<td name='"+ json_obj[i].invoice +"'>"+ json_obj[i].invoice+ "</td>"
										+ "<td name='"+ json_obj[i].invoice_date +"'>"+ json_obj[i].invoice_date+ "</td>"
										+ "<td name='"+ json_obj[i].trans_list_date +"'>"+ json_obj[i].trans_list_date+ "</td>"
										+ "<td name='"+ json_obj[i].dis_date +"'>"+ json_obj[i].dis_date+ "</td>"
										+ "<td name='"+ json_obj[i].sale_date +"'>"+ json_obj[i].sale_date+ "</td>"
										+ "<td name='"+ json_obj[i].order_source +"'>"+ json_obj[i].order_source+ "</td>"
										+ "<td name='"+ json_obj[i].memo +"'>"+ json_obj[i].memo+ "</td>"
										+ "<td><input id='my-"+json_obj[i].seq_no+"' type='checkbox' style='margin: 0 auto;' value='"+ json_obj[i].sale_id+ "'name='"+ json_obj[i].c_product_id
										+ "'class='checkbox_salereturn'></input><label for='my-"+json_obj[i].seq_no+"'></label></td></tr>";
									}
								});
							}
							if(json_obj[resultRunTime-1].message=="如要以日期查詢，請完整填寫起日欄位"){
								$("#salereturns_false_contain").hide();
								$("#salereturns_true_contain").hide();
								if(!$("#trans_list_date_err_mes").length){
	                				$("<p id='trans_list_date_err_mes'>如要以日期查詢，請完整填寫起日欄位</p>").appendTo($("#trans_list_date_form").parent());
	                			}else{
	                				$("#trans_list_date_err_mes").html("如要以日期查詢，請完整填寫起日欄位");
	                			}
							}
							if(json_obj[resultRunTime-1].message=="如要以日期查詢，請完整填寫訖日欄位"){
								$("#salereturns_false_contain").hide();
								$("#salereturns_true_contain").hide();
								if(!$("#trans_list_date_err_mes").length){
	                				$("<p id='trans_list_date_err_mes'>如要以日期查詢，請完整填寫訖日欄位</p>").appendTo($("#trans_list_date_form").parent());
	                			}else{
	                				$("#trans_list_date_err_mes").html("如要以日期查詢，請完整填寫訖日欄位");
	                			}
							}						
							if(json_obj[resultRunTime-1].message=="起日不可大於訖日"){
								$("#salereturns_false_contain").hide();
								$("#salereturns_true_contain").hide();
								if(!$("#trans_list_date_err_mes").length){
	                				$("<p id='trans_list_date_err_mes'>起日不可大於訖日</p>").appendTo($("#trans_list_date_form").parent());
	                			}else{
	                				$("#trans_list_date_err_mes").html("起日不可大於訖日");
	                			}
							}							
							if(resultRunTime==0){
								$("#salereturns_false_contain").hide();
								$("#salereturns_true_contain").hide();
								if(!$("#trans_list_date_err_mes").length){
	                				$("<p id='trans_list_date_err_mes'>查無此結果</p>").appendTo($("#trans_list_date_form").parent());
	                			}else{
	                				$("#trans_list_date_err_mes").html("查無此結果");
	                			}
							}
							$("#salereturns_false_table").dataTable().fnDestroy();
							if(resultRunTime!=0&&json_obj[resultRunTime-1].message=="驗證通過"){
								$("#salereturns_false_contain").show();
								$("#salereturns_true_contain").hide();
								$("#salereturns_false_table tbody").html(result_table);
								$("#salereturns_false_table").find("th").css({"word-break":"break-all","min-width":"30px","text-align":"center" });
								$("#salereturns_false_table").find("td").css({"word-break":"break-all","min-width":"30px","text-align":"center" });
								$("td > label").css({"float":"none","display":"inline","margin":"0px 0px 0px 5px"});
								$("#salereturns_false_table").dataTable({
									  autoWidth: false,
									  scrollX:  true,
							          scrollY:"300px",
									  dom: 'Blfrtip',
							          buttons: [{
							                text: '銷貨退回',
							                className: 'btn_sale_return',
							                action : function(e){
							                	e.preventDefault();
							        			var count = 0;
							        			var message = "";
							        			$(".checkbox_salereturn").each(function(){
							        				if($(this).prop("checked")){
							        					count+=1;
							        				}
							        			});
							        			message = "確認要銷貨退回嗎? 總共" + count + "筆";
							        			$("#dialog-confirm p").text(message);
							        			confirm_dialog.dialog("open");
							                }
							            }
							          ],"language": {"url": "js/dataTables_zh-tw.txt","zeroRecords": "沒有符合的結果"}});
								if($("#trans_list_date_err_mes").length){
	                				$("#trans_list_date_err_mes").remove();
	                			}
							}
						}
					});
			}
		});
		//配送日查詢相關設定
		$("#searh_dis_date").click(function(e) {
			e.preventDefault();
			if($("#trans_dis_date_form").valid()){
				$.ajax({
					type : "POST",
					url : "salereturn.do",
					data : {
						action : "search_dis_date",
						dis_start_date: $("#dis_start_date").val(),
						dis_end_date: $("#dis_end_date").val()
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
										if(json_obj[i].seq_no==null){
											json_obj[i].seq_no="";
										}
										if(json_obj[i].order_no==null){
											json_obj[i].order_no="";
										}
										if(json_obj[i].product_name==null){
											json_obj[i].product_name="";
										}
										if(json_obj[i].c_product_id==null){
											json_obj[i].c_product_id="";
										}
										if(json_obj[i].quantity==null){
											json_obj[i].quantity="";
										}
										if(json_obj[i].price==null){
											json_obj[i].price="";
										}
										if(json_obj[i].invoice==null){
											json_obj[i].invoice="";
										}
										if(json_obj[i].invoice_date==null){
											json_obj[i].invoice_date="";
										}
										if(json_obj[i].trans_list_date==null){
											json_obj[i].trans_list_date="";
										}
										if(json_obj[i].dis_date==null){
											json_obj[i].dis_date="";
										}
										if(json_obj[i].memo==null){
											json_obj[i].memo="";
										}
										if(json_obj[i].sale_date==null){
											json_obj[i].sale_date="";
										}
										if(json_obj[i].order_source==null){
											json_obj[i].order_source="";
										}
										result_table 
										+= "<tr>"
										+ "<td name='"+ json_obj[i].seq_no +"'>"+ json_obj[i].seq_no+ "</td>"
										+ "<td name='"+ json_obj[i].order_no +"'>"+ json_obj[i].order_no+ "</td>"
										+ "<td name='"+ json_obj[i].product_name +"'>"+ json_obj[i].product_name+ "</td>"
										+ "<td name='"+ json_obj[i].c_product_id +"'>"+ json_obj[i].c_product_id+ "</td>"
										+ "<td name='"+ json_obj[i].quantity +"'>"+ json_obj[i].quantity+ "</td>"
										+ "<td name='"+ json_obj[i].price +"'>"+ json_obj[i].price+ "</td>"
										+ "<td name='"+ json_obj[i].invoice +"'>"+ json_obj[i].invoice+ "</td>"
										+ "<td name='"+ json_obj[i].invoice_date +"'>"+ json_obj[i].invoice_date+ "</td>"
										+ "<td name='"+ json_obj[i].trans_list_date +"'>"+ json_obj[i].trans_list_date+ "</td>"
										+ "<td name='"+ json_obj[i].dis_date +"'>"+ json_obj[i].dis_date+ "</td>"
										+ "<td name='"+ json_obj[i].sale_date +"'>"+ json_obj[i].sale_date+ "</td>"
										+ "<td name='"+ json_obj[i].order_source +"'>"+ json_obj[i].order_source+ "</td>"
										+ "<td name='"+ json_obj[i].memo +"'>"+ json_obj[i].memo+ "</td>"
										+ "<td><input id='my-"+json_obj[i].seq_no+"' type='checkbox' style='margin: 0 auto;' value='"+ json_obj[i].sale_id+ "'name='"+ json_obj[i].c_product_id
										+ "'class='checkbox_salereturn'></input><label for='my-"+json_obj[i].seq_no+"'></label></td></tr>";
									}
								});
							}
							if(json_obj[resultRunTime-1].message=="如要以日期查詢，請完整填寫起日欄位"){
								$("#salereturns_false_contain").hide();
								$("#salereturns_true_contain").hide();
								if(!$("#trans_dis_date_err_mes").length){
	                				$("<p id='trans_dis_date_err_mes'>如要以日期查詢，請完整填寫起日欄位</p>").appendTo($("#trans_dis_date_form").parent());
	                			}else{
	                				$("#trans_dis_date_err_mes").html("如要以日期查詢，請完整填寫起日欄位");
	                			}
							}
							if(json_obj[resultRunTime-1].message=="如要以日期查詢，請完整填寫訖日欄位"){
								$("#salereturns_false_contain").hide();
								$("#salereturns_true_contain").hide();
								if(!$("#trans_dis_date_err_mes").length){
	                				$("<p id='trans_dis_date_err_mes'>如要以日期查詢，請完整填寫訖日欄位</p>").appendTo($("#trans_dis_date_form").parent());
	                			}else{
	                				$("#trans_dis_date_err_mes").html("如要以日期查詢，請完整填寫訖日欄位");
	                			}
							}						
							if(json_obj[resultRunTime-1].message=="起日不可大於訖日"){
								$("#salereturns_false_contain").hide();
								$("#salereturns_true_contain").hide();
								if(!$("#trans_dis_date_err_mes").length){
	                				$("<p id='trans_dis_date_err_mes'>起日不可大於訖日</p>").appendTo($("#trans_dis_date_form").parent());
	                			}else{
	                				$("#trans_dis_date_err_mes").html("起日不可大於訖日");
	                			}
							}							
							if(resultRunTime==0){
								$("#salereturns_false_contain").hide();
								$("#salereturns_true_contain").hide();
								if(!$("#trans_dis_date_err_mes").length){
	                				$("<p id='trans_dis_date_err_mes'>查無此結果</p>").appendTo($("#trans_dis_date_form").parent());
	                			}else{
	                				$("#trans_dis_date_err_mes").html("查無此結果");
	                			}
							}
							$("#salereturns_false_table").dataTable().fnDestroy();
							if(resultRunTime!=0&&json_obj[resultRunTime-1].message=="驗證通過"){
								$("#salereturns_false_contain").show();
								$("#salereturns_true_contain").hide();
								$("#salereturns_false_table tbody").html(result_table);
								$("#salereturns_false_table").find("th").css({"word-break":"break-all","min-width":"30px","text-align":"center" });
								$("#salereturns_false_table").find("td").css({"word-break":"break-all","min-width":"30px","text-align":"center" });
								$("td > label").css({"float":"none","display":"inline","margin":"0px 0px 0px 5px"});
								$("#salereturns_false_table").dataTable({
									  autoWidth: false,
									  scrollX:  true,
							          scrollY:"300px",
									  dom: 'Blfrtip',
							          buttons: [{
							                text: '銷貨退回',
							                className: 'btn_sale_return',
							                action : function(e){
							                	e.preventDefault();
							        			var count = 0;
							        			var message = "";
							        			$(".checkbox_salereturn").each(function(){
							        				if($(this).prop("checked")){
							        					count+=1;
							        				}
							        			});
							        			message = "確認要銷貨退回嗎? 總共" + count + "筆";
							        			$("#dialog-confirm p").text(message);
							        			confirm_dialog.dialog("open");
							                }
							            }
							          ],"language": {"url": "js/dataTables_zh-tw.txt","zeroRecords": "沒有符合的結果"}});
								if($("#trans_dis_date_err_mes").length){
	                				$("#trans_dis_date_err_mes").remove();
	                			}
							}
						}
					});
			}
		});		
		//處理初始的查詢autocomplete
	       $("#searh_c_product_id").autocomplete({
	            minLength: 2,
	            source: function (request, response) {
	                $.ajax({
	                    url : "salereturn.do",
	                    type : "POST",
	                    cache : false,
	                    delay : 1500,
	                    data : {
	                    	action : "search_product_data",
	                    	identity : "ID",
	                        term : request.term
	                    },
	                    success: function(data) {
	                    	var json_obj = $.parseJSON(data);
	                    	response($.map(json_obj, function (item) {
	                            return {
	                              label: item.c_product_id,
	                              value: item.c_product_id
	                            }
	                          }));
	                    }
	                });
	            },
	            change: function(event, ui) {
	    	        var source = $(this).val();
	    	            var temp = $(".ui-autocomplete li").map(function () { return $(this).text()}).get();
	    	        var found = $.inArray(source, temp);
	    	
	    	        if(found < 0) {
	    	            $(this).val('');
	    	            $(this).attr("placeholder","請輸入正確的產品ID名稱!");
	    	        }
	    	    }     
	         });
	       $("#searh_c_product_id").bind('focus', function(){ $(this).attr("placeholder","請輸入產品ID以供查詢"); } ); 
		//自訂產品ID查詢相關設定
		$("#searh_salereturn").click(function(e) {
			e.preventDefault();
			$.ajax({
				type : "POST",
				url : "salereturn.do",
				data : {
					action : "search",
					c_product_id : $("input[name='searh_c_product_id'").val()
				},
				success : function(result) {
						var json_obj = $.parseJSON(result);
						var len=json_obj.length;
						//判斷查詢結果
						var resultRunTime = 0;
						$.each (json_obj, function (i) {
							resultRunTime+=1;
						});
						var result_table = "";
						if(resultRunTime!=0){
							$.each(json_obj,function(i, item) {
								if(json_obj[i].seq_no==null){
									json_obj[i].seq_no="";
								}
								if(json_obj[i].order_no==null){
									json_obj[i].order_no="";
								}
								if(json_obj[i].product_name==null){
									json_obj[i].product_name="";
								}
								if(json_obj[i].c_product_id==null){
									json_obj[i].c_product_id="";
								}
								if(json_obj[i].quantity==null){
									json_obj[i].quantity="";
								}
								if(json_obj[i].price==null){
									json_obj[i].price="";
								}
								if(json_obj[i].invoice==null){
									json_obj[i].invoice="";
								}
								if(json_obj[i].invoice_date==null){
									json_obj[i].invoice_date="";
								}
								if(json_obj[i].trans_list_date==null){
									json_obj[i].trans_list_date="";
								}
								if(json_obj[i].dis_date==null){
									json_obj[i].dis_date="";
								}
								if(json_obj[i].memo==null){
									json_obj[i].memo="";
								}
								if(json_obj[i].sale_date==null){
									json_obj[i].sale_date="";
								}
								if(json_obj[i].order_source==null){
									json_obj[i].order_source="";
								}
								result_table 
								+= "<tr>"
								+ "<td name='"+ json_obj[i].seq_no +"'>"+ json_obj[i].seq_no+ "</td>"
								+ "<td name='"+ json_obj[i].order_no +"'>"+ json_obj[i].order_no+ "</td>"
								+ "<td name='"+ json_obj[i].product_name +"'>"+ json_obj[i].product_name+ "</td>"
								+ "<td name='"+ json_obj[i].c_product_id +"'>"+ json_obj[i].c_product_id+ "</td>"
								+ "<td name='"+ json_obj[i].quantity +"'>"+ json_obj[i].quantity+ "</td>"
								+ "<td name='"+ json_obj[i].price +"'>"+ json_obj[i].price+ "</td>"
								+ "<td name='"+ json_obj[i].invoice +"'>"+ json_obj[i].invoice+ "</td>"
								+ "<td name='"+ json_obj[i].invoice_date +"'>"+ json_obj[i].invoice_date+ "</td>"
								+ "<td name='"+ json_obj[i].trans_list_date +"'>"+ json_obj[i].trans_list_date+ "</td>"
								+ "<td name='"+ json_obj[i].dis_date +"'>"+ json_obj[i].dis_date+ "</td>"
								+ "<td name='"+ json_obj[i].sale_date +"'>"+ json_obj[i].sale_date+ "</td>"
								+ "<td name='"+ json_obj[i].order_source +"'>"+ json_obj[i].order_source+ "</td>"
								+ "<td name='"+ json_obj[i].memo +"'>"+ json_obj[i].memo+ "</td>"
								+ "<td><input id='my-"+json_obj[i].seq_no+"'type='checkbox' style='margin: 0 auto;' value='"+ json_obj[i].sale_id+ "'name='"+ json_obj[i].c_product_id
								+ "'class='checkbox_salereturn'></input><label for='my-"+json_obj[i].seq_no+"'></label></td></tr>";		
							});
						}							
						if(resultRunTime==0){
							$("#salereturns_false_contain").hide();
							if(!$("#search_sale_return_err_mes").length){
                				$("<p id='search_sale_return_err_mes'>查無此結果</p>").appendTo($("#salereturns_serah_create").parent());
                			}else{
                				$("#search_sale_return_err_mes").html("查無此結果");
                			}
						}
						$("#salereturns_false_table").dataTable().fnDestroy();
						if(resultRunTime!=0){
							$("#salereturns_false_contain").show();
							$("#salereturns_false_table tbody").html(result_table);
							var table = $("#salereturns_false_table").dataTable({
								  autoWidth: false,
								  scrollX:  true,
						          scrollY:"300px",
								  dom: 'Blfrtip',
						          buttons: [{
						                text: '銷貨退回',
						                className: 'btn_sale_return',
						                action : function(e){
						                	e.preventDefault();
						        			var count = 0;
						        			var message = "";
						        			$(".checkbox_salereturn").each(function(){
						        				if($(this).prop("checked")){
						        					count+=1;
						        				}
						        			});
						        			message = "確認要銷貨退回嗎? 總共" + count + "筆";
						        			$("#dialog-confirm p").text(message);
						        			confirm_dialog.dialog("open");
						                }
						            }
						          ],"language": {"url": "js/dataTables_zh-tw.txt","zeroRecords": "沒有符合的結果"}});
							$("#salereturns_false_table").find("td").css("text-align","center");
							$("#salereturns_false_table").find("th").css({"word-break":"break-all","min-width":"30px","text-align":"center" });
							$("#salereturns_false_table").find("td").css({"word-break":"break-all","min-width":"30px","text-align":"center" });
							$("td > label").css({"float":"none","display":"inline","margin":"0px 0px 0px 5px"});
							if($("#search_sale_return_err_mes").length){
                				$("#search_sale_return_err_mes").remove();
                			}
						}
					}
				});
		});
		confirm_dialog = $("#dialog-confirm").dialog({
			draggable : false,//防止拖曳
			resizable : false,//防止縮放
			autoOpen : false,
			height : 200,
			modal : true,
			show : {effect : "blind",duration : 300},
			hide : {effect : "fade",duration : 300},
			buttons : {
				"確認退回" : function() {
					$(".checkbox_salereturn").each(function(){
						if($(this).prop("checked")){
							$.ajax({
								type : "POST",
								url : "salereturn.do",
								data : {
									action : "insert_sale_return",
									sale_id : $(this).attr("value"),
									return_date : getCurrentDate()
								},
								success : function(result) {
									$(".checkbox_salereturn").each(function(){
										if($(this).prop("checked")){
											var $row = $(this).parent().parent();
											var table = $("#salereturns_false_table").dataTable();
											table.fnDeleteRow($row);
										}
									});
								    var json_obj = $.parseJSON(result);
									var len=json_obj.length;
									//判斷查詢結果
									var resultRunTime = 0;
									$.each (json_obj, function (i) {
										resultRunTime+=1;
									});
									alert(resultRunTime);
									var result_table = "";
									if(resultRunTime!=0){
										$.each(json_obj,function(i, item) {
											if(json_obj[i].seq_no==null){
												json_obj[i].seq_no="";
											}
											if(json_obj[i].order_no==null){
												json_obj[i].order_no="";
											}
											if(json_obj[i].product_name==null){
												json_obj[i].product_name="";
											}
											if(json_obj[i].c_product_id==null){
												json_obj[i].c_product_id="";
											}
											if(json_obj[i].quantity==null){
												json_obj[i].quantity="";
											}
											if(json_obj[i].price==null){
												json_obj[i].price="";
											}
											if(json_obj[i].invoice==null){
												json_obj[i].invoice="";
											}
											if(json_obj[i].invoice_date==null){
												json_obj[i].invoice_date="";
											}
											if(json_obj[i].trans_list_date==null){
												json_obj[i].trans_list_date="";
											}
											if(json_obj[i].dis_date==null){
												json_obj[i].dis_date="";
											}
											if(json_obj[i].memo==null){
												json_obj[i].memo="";
											}
											if(json_obj[i].sale_date==null){
												json_obj[i].sale_date="";
											}
											if(json_obj[i].order_source==null){
												json_obj[i].order_source="";
											}
											result_table 
											+= "<tr>"
											+ "<td name='"+ json_obj[i].seq_no +"'>"+ json_obj[i].seq_no+ "</td>"
											+ "<td name='"+ json_obj[i].order_no +"'>"+ json_obj[i].order_no+ "</td>"
											+ "<td name='"+ json_obj[i].product_name +"'>"+ json_obj[i].product_name+ "</td>"
											+ "<td name='"+ json_obj[i].c_product_id +"'>"+ json_obj[i].c_product_id+ "</td>"
											+ "<td name='"+ json_obj[i].quantity +"'>"+ json_obj[i].quantity+ "</td>"
											+ "<td name='"+ json_obj[i].price +"'>"+ json_obj[i].price+ "</td>"
											+ "<td name='"+ json_obj[i].invoice +"'>"+ json_obj[i].invoice+ "</td>"
											+ "<td name='"+ json_obj[i].invoice_date +"'>"+ json_obj[i].invoice_date+ "</td>"
											+ "<td name='"+ json_obj[i].trans_list_date +"'>"+ json_obj[i].trans_list_date+ "</td>"
											+ "<td name='"+ json_obj[i].dis_date +"'>"+ json_obj[i].dis_date+ "</td>"
											+ "<td name='"+ json_obj[i].sale_date +"'>"+ json_obj[i].sale_date+ "</td>"
											+ "<td name='"+ json_obj[i].order_source +"'>"+ json_obj[i].order_source+ "</td>"
											+ "<td name='"+ json_obj[i].memo +"'>"+ json_obj[i].memo+ "</td>"
											+ "<td><input id='my-"+json_obj[i].seq_no+"' type='checkbox' style='margin: 0 auto;' value='"+ json_obj[i].sale_id+ "'name='"+ json_obj[i].c_product_id
											+ "'class='checkbox_salereturn_cancel'></input><label for='my-"+json_obj[i].seq_no+"'></label></td></tr>";		
										});
									}							
									if(resultRunTime==0){
										$("#salereturns_true_contain").hide();
									}
									if ($.fn.DataTable.isDataTable("#salereturns_true_table")) {
										$("#salereturns_true_table").dataTable().fnDestroy();
									}
									if(resultRunTime!=0){
										$("#salereturns_true_contain").show();
										$("#salereturns_true_table tbody").html(result_table);
										$("#salereturns_true_table").find("th").css({"word-break":"break-all","min-width":"30px","text-align":"center" });
										$("#salereturns_true_table").find("td").css({"word-break":"break-all","min-width":"30px","text-align":"center" });
										$("td > label").css({"float":"none","display":"inline","margin":"0px 0px 0px 5px"});
										$("#salereturns_true_table").dataTable({
											  autoWidth: false,
											  scrollX:  true,
									          scrollY:"300px",
											  dom: 'Blfrtip',
									          buttons: [{
									                text: '取消銷貨退回',
									                className: 'btn_sale_return_cancel',
									                action : function(e){
									        			e.preventDefault();
									        			var count = 0;
									        			var message = "";
									        			$(".checkbox_salereturn_cancel").each(function(){
									        				if($(this).prop("checked")){
									        					count+=1;
									        				}
									        			});
									        			message = "確認要取消銷貨退回嗎? 總共" + count + "筆";
									        			$("#dialog-cancel-confirm p").text(message);
									        			confirm_cancel_dialog.dialog("open");
									                }
									            }
									          ],"language": {"url": "js/dataTables_zh-tw.txt","zeroRecords": "沒有符合的結果"}});
										$("#salereturns_true_table").find("td").css("text-align","center");
									}
								}
							});
						}
					});
					$(this).dialog("close");
				},
				"取消退回" : function() {
					$(this).dialog("close");
				}
			}
		});
		confirm_cancel_dialog = $("#dialog-cancel-confirm").dialog({
			draggable : false,//防止拖曳
			resizable : false,//防止縮放
			autoOpen : false,
			height : 200,
			modal : true,
			show : {effect : "blind",duration : 300},
			hide : {effect : "fade",duration : 300},
			buttons : {
				"確認取消" : function() {
					$(".checkbox_salereturn_cancel").each(function(){
						if($(this).prop("checked")){
							$.ajax({
								type : "POST",
								url : "salereturn.do",
								data : {
									action : "delete_sale_return",
									sale_id : $(this).attr("value"),
									c_product_id : $("input[name='searh_c_product_id'").val()
								},
								success : function(result) {
									$(".checkbox_salereturn_cancel").each(function(){
										if($(this).prop("checked")){
											var $row = $(this).parent().parent();
											var table = $("#salereturns_true_table").dataTable();
											table.fnDeleteRow($row);
										}
									});
								    var json_obj = $.parseJSON(result);
									//判斷查詢結果
									var resultRunTime = 0;
									$.each (json_obj, function (i) {
										resultRunTime+=1;
									});
									var result_table = "";
									if(resultRunTime!=0){
										$.each(json_obj,function(i, item) {
											if(json_obj[i].seq_no==null){
												json_obj[i].seq_no="";
											}
											if(json_obj[i].order_no==null){
												json_obj[i].order_no="";
											}
											if(json_obj[i].product_name==null){
												json_obj[i].product_name="";
											}
											if(json_obj[i].c_product_id==null){
												json_obj[i].c_product_id="";
											}
											if(json_obj[i].quantity==null){
												json_obj[i].quantity="";
											}
											if(json_obj[i].price==null){
												json_obj[i].price="";
											}
											if(json_obj[i].invoice==null){
												json_obj[i].invoice="";
											}
											if(json_obj[i].invoice_date==null){
												json_obj[i].invoice_date="";
											}
											if(json_obj[i].trans_list_date==null){
												json_obj[i].trans_list_date="";
											}
											if(json_obj[i].dis_date==null){
												json_obj[i].dis_date="";
											}
											if(json_obj[i].memo==null){
												json_obj[i].memo="";
											}
											if(json_obj[i].sale_date==null){
												json_obj[i].sale_date="";
											}
											if(json_obj[i].order_source==null){
												json_obj[i].order_source="";
											}
											result_table 
											+= "<tr>"
											+ "<td name='"+ json_obj[i].seq_no +"'>"+ json_obj[i].seq_no+ "</td>"
											+ "<td name='"+ json_obj[i].order_no +"'>"+ json_obj[i].order_no+ "</td>"
											+ "<td name='"+ json_obj[i].product_name +"'>"+ json_obj[i].product_name+ "</td>"
											+ "<td name='"+ json_obj[i].c_product_id +"'>"+ json_obj[i].c_product_id+ "</td>"
											+ "<td name='"+ json_obj[i].quantity +"'>"+ json_obj[i].quantity+ "</td>"
											+ "<td name='"+ json_obj[i].price +"'>"+ json_obj[i].price+ "</td>"
											+ "<td name='"+ json_obj[i].invoice +"'>"+ json_obj[i].invoice+ "</td>"
											+ "<td name='"+ json_obj[i].invoice_date +"'>"+ json_obj[i].invoice_date+ "</td>"
											+ "<td name='"+ json_obj[i].trans_list_date +"'>"+ json_obj[i].trans_list_date+ "</td>"
											+ "<td name='"+ json_obj[i].dis_date +"'>"+ json_obj[i].dis_date+ "</td>"
											+ "<td name='"+ json_obj[i].sale_date +"'>"+ json_obj[i].sale_date+ "</td>"
											+ "<td name='"+ json_obj[i].order_source +"'>"+ json_obj[i].order_source+ "</td>"
											+ "<td name='"+ json_obj[i].memo +"'>"+ json_obj[i].memo+ "</td>"
											+ "<td><input id='my-"+json_obj[i].seq_no+"' type='checkbox' style='margin: 0 auto;' value='"+ json_obj[i].sale_id+ "'name='"+ json_obj[i].c_product_id
											+ "'class='checkbox_salereturn'></input><label for='my-"+json_obj[i].seq_no+"'></label></td></tr>";	
										});
									}							
									if(resultRunTime==0){
										$("#salereturns_false_contain").hide();
									}
									if ($.fn.DataTable.isDataTable("#salereturns_false_table")) {
										$("#salereturns_false_table").dataTable().fnDestroy();
									}
									if(resultRunTime!=0){
										$("#salereturns_false_contain").show();
										$("#salereturns_false_table tbody").html(result_table);
										$("#salereturns_false_table").find("th").css({"word-break":"break-all","min-width":"30px","text-align":"center" });
										$("#salereturns_false_table").find("td").css({"word-break":"break-all","min-width":"30px","text-align":"center" });
										$("td > label").css({"float":"none","display":"inline","margin":"0px 0px 0px 5px"});
										$("#salereturns_false_table").dataTable({
											  autoWidth: false,
											  scrollX:  true,
									          scrollY:"300px",
											  dom: 'Blfrtip',
									          buttons: [{
									                text: '銷貨退回',
									                className: 'btn_sale_return',
									                action : function(e){
									                	e.preventDefault();
									        			var count = 0;
									        			var message = "";
									        			$(".checkbox_salereturn").each(function(){
									        				if($(this).prop("checked")){
									        					count+=1;
									        				}
									        			});
									        			message = "確認要銷貨退回嗎? 總共" + count + "筆";
									        			$("#dialog-confirm p").text(message);
									        			confirm_dialog.dialog("open");
									                }
									            }
									          ],"language": {"url": "js/dataTables_zh-tw.txt","zeroRecords": "沒有符合的結果"}});
										$("#salereturns_false_table").find("td").css("text-align","center");
									}
								}
							});
						}
					});
					$(this).dialog("close");
				},
				"取消動作" : function() {
					$(this).dialog("close");
				}
			}
		});
		//hide contain
		$("#salereturns_false_contain").hide();
		$("#salereturns_true_contain").hide();
		//get today yyyy-mm-dd
		function getCurrentDate(){
			var fullDate = new Date();
			var twoDigitMonth = fullDate.getMonth()+1;	if(twoDigitMonth.length==1)	twoDigitMonth="0" +twoDigitMonth;
			var twoDigitDate = fullDate.getDate()+"";	if(twoDigitDate.length==1)	twoDigitDate="0" +twoDigitDate;
			var currentDate = fullDate.getFullYear() + "-" + twoDigitMonth + "-" + twoDigitDate;
			return currentDate;
		}
		//日期設定
		$(".date").datepicker({
			dayNamesMin:["日","一","二","三","四","五","六"],
			monthNames:["一月","二月","三月","四月","五月","六月","七月","八月","九月","十月","十一月","十二月"],
			dateFormat:"yy-mm-dd",
			changeYear:true
		});
		//hold header
		$("#salereturns_false_table").find("th").css("min-width","120px");
		$("#salereturns_true_table").find("th").css("min-width","120px");
	})
</script>
		<div class="datalistWrap">
			<!--對話窗樣式-確認 -->
			<div id="dialog-confirm" title="確認銷貨退回嗎?">
				<br><p></p>
			</div>
			<!--對話窗樣式-確認-取消 -->
			<div id="dialog-cancel-confirm" title="確認取消銷貨退回嗎?">
				<br><p></p>
			</div>					
			<!-- 第一列 -->
		<div class="input-field-wrap">
			<div class="form-wrap">
				<div class="form-row">
					<label for="">
						<span class="block-label">產品 ID 查詢</span>
						<input type="text" id="searh_c_product_id" name="searh_c_product_id">
					</label>
					<button class="btn btn-darkblue" id="searh_salereturn">查詢</button>
				</div>
	          	
				<div class="form-row">
					<form id="trans_list_date_form" name="trans_list_date_form">
						<label for="">
							<span class="block-label">轉單起日</span>
							<input type="text" class="input-date" id="trans_list_start_date" name="trans_list_start_date">
						</label>
						<div class="forward-mark"></div>
						<label for="">
							<span class="block-label">轉單迄日</span>
							<input type="text" class="input-date" id="trans_list_end_date" name="trans_list_end_date">
						</label>
						<button id="searh_trans_list_date" class="btn btn-darkblue">查詢</button>
					</form>
				</div>
				<div class="form-row">
					<form id="trans_dis_date_form" name="trans_dis_date_form">
						<label for="">
							<span class="block-label">配送起日</span>
							<input type="text" class="input-date" id="dis_start_date" name="dis_start_date">
						</label>
						<div class="forward-mark"></div>
						<label for="">
							<span class="block-label">配送迄日</span>
							<input type="text" class="input-date" id="dis_end_date" name="dis_end_date">
						</label>
						<button id="searh_dis_date" class="btn btn-darkblue">查詢</button>
					</form>
				</div>
			</div><!-- /.form-wrap -->
		</div><!-- /.input-field-wrap -->
<!-- 			<div class="row" align="center"> -->
<!-- 				<div id="salereturns_serah_create_contain" class="ui-widget"> -->
<!-- 					<table id="salereturns_serah_create"> -->
<!-- 						<thead> -->
<!-- 							<tr> -->
<!-- 								<td> -->
<!-- 									<input type="text" id="searh_c_product_id" name="searh_c_product_id" placeholder="請輸入自訂產品ID查詢"> -->
<!-- 								</td> -->
<!-- 								<th> -->
<!-- 									&nbsp;&nbsp;<button id="searh_salereturn">查詢</button> -->
<!-- 								</th> -->
<!-- 							</tr> -->
<!-- 						</thead> -->
<!-- 					</table> -->
<!-- 				</div> -->
<!-- 			</div> -->
			<!-- 第二列 -->
<!-- 			<div class="row" align="center"> -->
<!-- 				<div class="ui-widget"> -->
<!-- 					<form id="trans_list_date_form" name="trans_list_date_form"> -->
<!-- 						<table> -->
<!-- 							<thead> -->
<!-- 								<tr> -->
<!-- 									<td> -->
<!-- 										<input type="text" id="trans_list_start_date" name="trans_list_start_date" class="date" placeholder="請輸入轉單起日"> -->
<!-- 									</td> -->
<!-- 									<td> -->
<!-- 										<p>&nbsp;&nbsp;~&nbsp;&nbsp;</p> -->
<!-- 									</td> -->
<!-- 									<td> -->
<!-- 										<input type="text" id="trans_list_end_date" name="trans_list_end_date" class="date" placeholder="請輸入轉單迄日"> -->
<!-- 									</td> -->
<!-- 									<td> -->
<!-- 										&nbsp;&nbsp;<button id="searh_trans_list_date">查詢</button> -->
<!-- 									</td> -->
<!-- 								</tr> -->
<!-- 							</thead> -->
<!-- 						</table> -->
<!-- 					</form> -->
<!-- 				</div> -->
<!-- 			</div> -->
			<!-- 第三列 -->
<!-- 			<div class="row" align="center"> -->
<!-- 				<div class="ui-widget"> -->
<!-- 					<form id="trans_dis_date_form" name="trans_dis_date_form"> -->
<!-- 						<table> -->
<!-- 							<thead> -->
<!-- 								<tr> -->
<!-- 									<td> -->
<!-- 										<input type="text" id="dis_start_date" name="dis_start_date" class="date" placeholder="請輸入配送起日"> -->
<!-- 									</td> -->
<!-- 									<td> -->
<!-- 										<p>&nbsp;&nbsp;~&nbsp;&nbsp;</p> -->
<!-- 									</td> -->
<!-- 									<td> -->
<!-- 										<input type="text" id="dis_end_date" name="dis_end_date" class="date" placeholder="請輸入配送迄日"> -->
<!-- 									</td> -->
<!-- 									<td> -->
<!-- 										&nbsp;&nbsp;<button id="searh_dis_date">查詢</button> -->
<!-- 									</td> -->
<!-- 								</tr> -->
<!-- 							</thead> -->
<!-- 						</table> -->
<!-- 					</form> -->
<!-- 				</div> -->
<!-- 			</div> -->
			<!-- 第四列 -->
			<div class="row search-result-wrap" align="center"style="height:433px;">
<!-- 				<div class="form-row"> -->
<!-- 					<button id="button_1" align="center" class="btn btn-wide btn-darkblue">銷貨退回</button> -->
<!-- 				</div> -->
				<div id="salereturns_false_contain" class="result-table-wrap">
					<table id="salereturns_false_table" class="result-table">
						<thead>
							<tr>
								<th>銷貨單號</th>
								<th>訂單號</th>
								<th>產品名稱</th>
								<th>自訂產品ID</th>
								<th>銷貨數量</th>
								<th>銷貨金額</th>
								<th>發票號碼</th>
								<th>發票日期</th>
								<th>轉單日</th>
								<th>配送日</th>
								<th>銷貨/出貨日期</th>
								<th>銷售平台</th>
								<th>備註說明</th>
								<th>勾選</th>
							</tr>
						</thead>
						<tbody>
						</tbody>
					</table>
				</div>
			</div>
		</div>
		<div class="datalistWrap" >
			<!-- 第五列 -->
<!-- 			<div class="row" align="center"> -->
<!-- 				<div class="ui-widget"> -->
<!-- 					<form id="return_date_form" name="trans_dis_date_form"> -->
<!-- 						<table> -->
<!-- 							<thead> -->
<!-- 								<tr> -->
<!-- 									<td> -->
<!-- 										<input type="text" id="return_start_date" name="return_start_date" class="date" placeholder="請輸入退貨起日"> -->
<!-- 									</td> -->
<!-- 									<th> -->
<!-- 										<p>&nbsp;&nbsp;~&nbsp;&nbsp;</p> -->
<!-- 									</th> -->
<!-- 									<td> -->
<!-- 										<input type="text" id="return_end_date" name="return_end_date" class="date" placeholder="請輸入退貨迄日"> -->
<!-- 									</td> -->
<!-- 									<td> -->
<!-- 										&nbsp;&nbsp;<button id="searh_salereturn_date">查詢</button> -->
<!-- 									</td> -->
<!-- 								</tr> -->
<!-- 							</thead> -->
<!-- 						</table> -->
<!-- 					</form> -->
<!-- 				</div> -->
<!-- 			</div> -->
			<!-- 第六列 -->
			<div class="input-field-wrap">
				<div class="form-wrap">
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
							<button id="searh_salereturn_date" class="btn btn-darkblue">查詢</button>
						</form>
					</div>
				</div><!-- /.form-wrap -->
			</div><!-- /.input-field-wrap -->
			<div class="row search-result-wrap" align="center"style="height:433px;">
<!-- 				<div class="form-row"> -->
<!-- 					<button id="button_2" align="center" class="btn btn-wide btn-darkblue">取消銷貨退回</button> -->
<!-- 				</div> -->
				<div id="salereturns_true_contain" class="result-table-wrap">
					<table id="salereturns_true_table" class="result-table">
						<thead>
							<tr>
								<th>銷貨單號</th>
								<th>訂單號</th>
								<th>產品名稱</th>
								<th>自訂產品ID</th>
								<th>銷貨數量</th>
								<th>銷貨金額</th>
								<th>發票號碼</th>
								<th>發票日期</th>
								<th>轉單日</th>
								<th>配送日</th>
								<th>銷貨/出貨日期</th>
								<th>銷售平台</th>
								<th>備註說明</th>
								<th>勾選</th>
							</tr>
						</thead>
						<tbody>
						</tbody>
					</table>
				</div>
			</div>	
		</div>
	</div>
</body>
</html>