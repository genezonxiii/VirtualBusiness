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
.dataTables_wrapper .dt-buttons {float:right;margin-bottom:10px;}
</style>
<title>應收帳款</title>
<meta charset="utf-8">
<link rel="Shortcut Icon" type="image/x-icon" href="./images/Rockettheme-Ecommerce-Shop.ico" />

<link href="<c:url value="css/jquery.dataTables.min.css" />" rel="stylesheet">
<link href="<c:url value="css/1.11.4/jquery-ui.css" />" rel="stylesheet">
<link href="<c:url value="css/dataTables.jqueryui.min.css" />" rel="stylesheet">
<link href="<c:url value="css/buttons.jqueryui.min.css" />" rel="stylesheet">
<link rel="stylesheet" href="css/styles.css" />
</head>
<body>
	<jsp:include page="template.jsp" flush="true"/>
	<div class="content-wrap" >
		<div class='bdyplane' style="opacity:0">
<script type="text/javascript" src="js/jquery-1.10.2.js"></script>
<script type="text/javascript" src="js/jquery.dataTables.min.js"></script>
<script type="text/javascript" src="js/jquery-ui.min.js"></script>
<script type="text/javascript" src="js/jquery-migrate-1.4.1.min.js"></script>
<script type="text/javascript" src="js/jquery.validate.min.js"></script>
<script type="text/javascript" src="js/additional-methods.min.js"></script>
<script type="text/javascript" src="js/messages_zh_TW.min.js"></script>
<!-- data table buttons -->
<script type="text/javascript" src="js/dataTables.buttons.min.js"></script>
<script type="text/javascript" src="js/buttons.jqueryui.min.js"></script>
<script>
function draw_accreceive(parameter){
	
}




function detail(str){
	$.ajax({
		type : "POST",
		url : "accreceive.do",
		data : {
			action : "melvin",
			seq_no : str
		},
		success : function(result) {
			$("#my").html("<table class='dialog-table'>"+result+"</table>");
			$("#my").dialog("open");
		}
	});
}
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
		 $("#receive_date_form").validate({
				rules : {
					receive_start_date : {
						dateISO : true
					},
					receive_end_date:{
						dateISO : true
					}
				},
				messages:{
					receive_start_date : {
						dateISO : "日期格式錯誤"
					},
					receive_end_date : {
						dateISO : "日期格式錯誤"
					}
				}
			});
	//應收帳款日查詢相關設定
	$("#searh_amount_date").click(function(e) {
		e.preventDefault();
		if($("#amount_date_form").valid()){			
			$.ajax({
				type : "POST",
				url : "accreceive.do",
				data : {
					action : "searh_amount_date",
					start_date: $("#amount_start_date").val(),
					end_date: $("#amount_end_date").val()
				},
				success : function(result) {
						var json_obj = $.parseJSON(result);
						var len=json_obj.length;
						//判斷查詢結果
						var resultRunTime = 0;
						$.each (json_obj, function (i) {
							resultRunTime+=1;
						});
						//console.log(result);
						//alert(json_obj.length);
						if(json_obj[resultRunTime-1].message=="驗證通過"){
							var result_table = "";
							
							$.each(json_obj,function(i, item) {
								if(i<len-1){
									if(json_obj[i].amount==null){json_obj[i].amount="";}
									if(json_obj[i].amount_date==null){json_obj[i].amount_date="";}
									if(json_obj[i].receive_amount==null){json_obj[i].receive_amount="";}
									if(json_obj[i].receive_date==null){json_obj[i].receive_date="";}
									if(json_obj[i].memo==null){json_obj[i].memo="";}
									result_table 
									+= "<tr ondblclick='detail("+json_obj[i].seq_no+")'>"
									+ "<td name='"+ json_obj[i].amount +"'>"+ money(json_obj[i].amount)+ "</td>"
									+ "<td name='"+ json_obj[i].amount_date +"'>"+ json_obj[i].amount_date+ "</td>"
									+ "<td name='"+ json_obj[i].receive_amount +"'>"+ money(json_obj[i].receive_amount)+ "</td>"
									+ "<td name='"+ json_obj[i].receive_date +"'>"+ json_obj[i].receive_date+ "</td>"
									+ "<td name='"+ json_obj[i].order_source +"'>"+ json_obj[i].order_source+ "</td>"
									+ "<td name='"+ json_obj[i].order_no +"'>"+ json_obj[i].order_no+ "</td>"
									+ "<td name='"+ json_obj[i].memo +"'>"+ json_obj[i].memo+ "</td>"
									+ "<td><input id='my-"+ json_obj[i].receivable_id+ "' type='checkbox' style='margin: 0 auto;' value='"+ json_obj[i].receivable_id
									+ "'class='checkbox_receive'></input><label for='my-"+ json_obj[i].receivable_id+ "'></label></td></tr>";
								}
							});
						}
						if(json_obj[resultRunTime-1].message=="如要以日期查詢，請完整填寫起日欄位"){
							$("#account_amount_date_contain").hide();
							$("#account_receive_date_contain").hide();
							if(!$("#search_amount_date_err_mes").length){
                				$("<p id='search_amount_date_err_mes'>如要以日期查詢，請完整填寫起日欄位</p>").appendTo($("#amount_date_form").parent());
                			}else{
                				$("#search_amount_date_err_mes").html("如要以日期查詢，請完整填寫起日欄位");
                			}
						}
						if(json_obj[resultRunTime-1].message=="如要以日期查詢，請完整填寫訖日欄位"){
							$("#account_amount_date_contain").hide();
							$("#account_receive_date_contain").hide();
							if(!$("#search_amount_date_err_mes").length){
                				$("<p id='search_amount_date_err_mes'>如要以日期查詢，請完整填寫訖日欄位</p>").appendTo($("#amount_date_form").parent());
                			}else{
                				$("#search_amount_date_err_mes").html("如要以日期查詢，請完整填寫訖日欄位");
                			}
						}						
						if(json_obj[resultRunTime-1].message=="起日不可大於訖日"){
							$("#account_amount_date_contain").hide();
							$("#account_receive_date_contain").hide();
							if(!$("#search_amount_date_err_mes").length){
                				$("<p id='search_amount_date_err_mes'>起日不可大於訖日</p>").appendTo($("#amount_date_form").parent());
                			}else{
                				$("#search_amount_date_err_mes").html("起日不可大於訖日");
                			}
						}							
						if(resultRunTime==0){
							$("#account_amount_date_contain").hide();
							$("#account_receive_date_contain").hide();
							if(!$("#search_amount_date_err_mes").length){
                				$("<p id='search_amount_date_err_mes'>查無此結果</p>").appendTo($("#amount_date_form").parent());
                			}else{
                				$("#search_amount_date_err_mes").html("查無此結果");
                			}
						}
						if ($.fn.DataTable.isDataTable("#account_amount_date_table")) {
							$("#account_amount_date_table").dataTable().fnDestroy();
						}
						if(resultRunTime!=0&&json_obj[resultRunTime-1].message=="驗證通過"){
							$("#account_amount_date_contain").hide();
							$("#account_amount_date_table tbody").html(result_table);
							$("#account_amount_date_table").dataTable({
								  autoWidth: false,
								  scrollX:  true,
						          scrollY:"300px",
								  dom: 'Blfrtip',
						          buttons: [{
						                text: '批次收帳',
						                className: 'btn_receive',
						                action : function(e){
						            		e.preventDefault();
						            		var count = 0;
						            		var message = "";
						            		$(".checkbox_receive").each(function(){
						            			if($(this).prop("checked")){
						            				count+=1;
						            			}
						            		});
						            		message = "確認要收帳嗎? 總共" + count + "筆";
						            		$("#dialog-confirm p").text(message);
						            		confirm_dialog.dialog("open");
						                }
						            }
						          ],"language": {"url": "js/dataTables_zh-tw.txt","zeroRecords": "沒有符合的結果"}});
// 						          ],"language": {"url": "js/dataTables_zh-tw.txt","zeroRecords": "沒有符合的結果"}});
							$("#account_amount_date_table").find("td").css("text-align","center");
							$("#account_amount_date_contain").fadeIn();
							//$("#account_amount_date_table").find("th").css({"word-break":"break-all","min-width":"70px","text-align":"center" });
							//$("#account_amount_date_table").find("td").css({"word-break":"break-all","min-width":"70px","text-align":"center" });
							$("td > label").css({"float":"none","display":"inline","margin":"0px 0px 0px 5px"});
							if($("#search_amount_date_err_mes").length){
                				$("#search_amount_date_err_mes").remove();
                			}
						}
					}
				});
		}
	});	
	//實收帳款日查詢相關設定
	$("#searh_receive_date").click(function(e) {
		e.preventDefault();
		if($("#receive_date_form").valid()){
			$.ajax({
				type : "POST",
				url : "accreceive.do",
				data : {
					action : "searh_receive_date",
					start_date: $("#receive_start_date").val(),
					end_date: $("#receive_end_date").val()
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
									if(json_obj[i].receive_amount==null){
										json_obj[i].receive_amount="";
									}
									if(json_obj[i].receive_date==null){
										json_obj[i].receive_date="";
									}
									if(json_obj[i].memo==null){
										json_obj[i].memo="";
									}
									result_table 
									+= "<tr>"
									+ "<td name='"+ json_obj[i].amount +"'>"+ money(json_obj[i].amount)+ "</td>"
									+ "<td name='"+ json_obj[i].amount_date +"'>"+ json_obj[i].amount_date+ "</td>"
									+ "<td name='"+ json_obj[i].receive_amount +"'>"+ money(json_obj[i].receive_amount)+ "</td>"
									+ "<td name='"+ json_obj[i].receive_date +"'>"+ json_obj[i].receive_date+ "</td>"
									+ "<td name='"+ json_obj[i].order_source +"'>"+ json_obj[i].order_source+ "</td>"
									+ "<td name='"+ json_obj[i].order_no +"'>"+ json_obj[i].order_no+ "</td>"
									+ "<td name='"+ json_obj[i].memo +"'>"+ json_obj[i].memo+ "</td>"
									+ "<td><input id='my-"+ json_obj[i].receivable_id+ "' type='checkbox' style='margin: 0 auto;' value='"+ json_obj[i].receivable_id
									+ "'class='checkbox_receive_cancel'></input><label for='my-"+ json_obj[i].receivable_id+ "'></label></td></tr>";
								}
							});
						}
						if(json_obj[resultRunTime-1].message=="如要以日期查詢，請完整填寫起日欄位"){
							$("#account_amount_date_contain").hide();
							$("#account_receive_date_contain").hide();
							if(!$("#search_receive_date_err_mes").length){
                				$("<p id='search_receive_date_err_mes'>如要以日期查詢，請完整填寫起日欄位</p>").appendTo($("#receive_date_form").parent());
                			}else{
                				$("#search_receive_date_err_mes").html("如要以日期查詢，請完整填寫起日欄位");
                			}
						}
						if(json_obj[resultRunTime-1].message=="如要以日期查詢，請完整填寫訖日欄位"){
							$("#account_amount_date_contain").hide();
							$("#account_receive_date_contain").hide();
							if(!$("#search_receive_date_err_mes").length){
                				$("<p id='search_receive_date_err_mes'>如要以日期查詢，請完整填寫訖日欄位</p>").appendTo($("#receive_date_form").parent());
                			}else{
                				$("#search_receive_date_err_mes").html("如要以日期查詢，請完整填寫訖日欄位");
                			}
						}						
						if(json_obj[resultRunTime-1].message=="起日不可大於訖日"){
							$("#account_amount_date_contain").hide();
							$("#account_receive_date_contain").hide();
							if(!$("#search_receive_date_err_mes").length){
                				$("<p id='search_receive_date_err_mes'>起日不可大於訖日</p>").appendTo($("#receive_date_form").parent());
                			}else{
                				$("#search_receive_date_err_mes").html("起日不可大於訖日");
                			}
						}							
						if(resultRunTime==0){
							$("#account_amount_date_contain").hide();
							$("#account_receive_date_contain").hide();
							if(!$("#search_receive_date_err_mes").length){
                				$("<p id='search_receive_date_err_mes'>查無此結果</p>").appendTo($("#receive_date_form").parent());
                			}else{
                				$("#search_receive_date_err_mes").html("查無此結果");
                			}
						}
						if ($.fn.DataTable.isDataTable("#account_receive_date_table")) {
							$("#account_receive_date_table").dataTable().fnDestroy();
						}
						if(resultRunTime!=0&&json_obj[resultRunTime-1].message=="驗證通過"){
							$("#account_receive_date_contain").hide();
							$("#account_receive_date_table tbody").html(result_table);
							$("#account_receive_date_table").dataTable({
								  autoWidth: false,
								  scrollX:  true,
						          scrollY:"300px",
								  dom: 'Blfrtip',
						          buttons: [{
						                text: '批次取消收帳',
						                className: 'btn_receive_cancel',
						                action : function(e){
						            		e.preventDefault();
						            		var count = 0;
						            		var message = "";
						            		$(".checkbox_receive_cancel").each(function(){
						            			if($(this).prop("checked")){
						            				count+=1;
						            			}
						            		});
						            		message = "確認要取消收帳嗎? 總共" + count + "筆";
						            		$("#dialog-cancel-confirm p").text(message);
						            		confirm_cancel_dialog.dialog("open");
						                }
						            }
						          ],"language": {"url": "js/dataTables_zh-tw.txt","zeroRecords": "沒有符合的結果"}});
							$("#account_receive_date_table").find("td").css("text-align","center");
							$("#account_receive_date_table").find("th").css({"word-break":"break-all","min-width":"30px","text-align":"center" });
							$("#account_receive_date_table").find("td").css({"word-break":"break-all","min-width":"30px","text-align":"center" });
							$("td > label").css({"float":"none","display":"inline","margin":"0px 0px 0px 5px"});
							$("#account_receive_date_contain").fadeIn();
							if($("#search_receive_date_err_mes").length){
                				$("#search_receive_date_err_mes").remove();
                			}
						}
					}
				});
		}
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
			"確認批次收帳" : function() {
				$(".checkbox_receive").each(function(){
					if($(this).prop("checked")){
						$.ajax({
							type : "POST",
							url : "accreceive.do",
							async : false,
							data : {
								action : "receive_account",
								receivable_id : $(this).attr("value"),
								receive_date : getCurrentDate()
							},
							success : function(result) {
								$(".checkbox_receive").each(function(){
									if($(this).prop("checked")){
										var table = $("#account_amount_date_table").DataTable();
										table.row($(this).parents('tr')).remove().draw();
									}
								});
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
										if(json_obj[i].amount==null){
											json_obj[i].amount="";
										}
										if(json_obj[i].amount_date==null){
											json_obj[i].amount_date="";
										}
										if(json_obj[i].receive_amount==null){
											json_obj[i].receive_amount="";
										}
										if(json_obj[i].receive_date==null){
											json_obj[i].receive_date="";
										}
										if(json_obj[i].memo==null){
											json_obj[i].memo="";
										}
										result_table 
										+= "<tr>"
										+ "<td name='"+ json_obj[i].amount +"'>"+ money(json_obj[i].amount)+ "</td>"
										+ "<td name='"+ json_obj[i].amount_date +"'>"+ json_obj[i].amount_date+ "</td>"
										+ "<td name='"+ json_obj[i].receive_amount +"'>"+ money(json_obj[i].receive_amount)+ "</td>"
										+ "<td name='"+ json_obj[i].receive_date +"'>"+ json_obj[i].receive_date+ "</td>"
										+ "<td name='"+ json_obj[i].memo +"'>"+ json_obj[i].memo+ "</td>"
										+ "<td name='"+ json_obj[i].order_source +"'>"+ json_obj[i].order_source+ "</td>"
										+ "<td name='"+ json_obj[i].order_no +"'>"+ json_obj[i].order_no+ "</td>"
										+ "<td><input id='my-"+ json_obj[i].receivable_id+ "' type='checkbox' style='margin: 0 auto;' value='"+ json_obj[i].receivable_id
										+ "'class='checkbox_receive_cancel'></input><label for='my-"+ json_obj[i].receivable_id+ "'></label></td></tr>";	
									});
								}							
								if(resultRunTime==0){
									$("#account_amount_date_contain").hide();
									$("#account_receive_date_contain").hide();
									if(!$("#receive_account_err_mes").length){
		                				$("<p id='receive_account_err_mes'>查無此結果</p>").appendTo($("#amount_date_form").parent());
		                			}else{
		                				$("#receive_account_err_mes").html("查無此結果");
		                			}
								}
								if ($.fn.DataTable.isDataTable("#account_receive_date_table")) {
									$("#account_receive_date_table").dataTable().fnDestroy();
								}
								if(resultRunTime!=0){
									$("#account_receive_date_contain").hide();
									$("#account_receive_date_table tbody").html(result_table);
									$("#account_receive_date_table").dataTable({
										  autoWidth: false,
										  scrollX:  true,
								          scrollY:"300px",
										  dom: 'Blfrtip',
								          buttons: [{
								                text: '批次取消收帳',
								                className: 'btn_receive_cancel',
								                action : function(e){
								            		e.preventDefault();
								            		var count = 0;
								            		var message = "";
								            		$(".checkbox_receive_cancel").each(function(){
								            			if($(this).prop("checked")){
								            				count+=1;
								            			}
								            		});
								            		message = "確認要取消收帳嗎? 總共" + count + "筆";
								            		$("#dialog-cancel-confirm p").text(message);
								            		confirm_cancel_dialog.dialog("open");
								                }
								            }
								          ],"language": {"url": "js/dataTables_zh-tw.txt","zeroRecords": "沒有符合的結果"}});
									$("#account_receive_date_table").find("td").css("text-align","center");
									$("#account_receive_date_table").find("th").css({"word-break":"break-all","min-width":"30px","text-align":"center" });
									$("#account_receive_date_table").find("td").css({"word-break":"break-all","min-width":"30px","text-align":"center" });
									$("#account_receive_date_contain").fadeIn();
									$("td > label").css({"float":"none","display":"inline","margin":"0px 0px 0px 5px"});
									if($("#receive_account_err_mes").length){
		                				$("#receive_account_err_mes").remove();
		                			}
								}
							}
						});
					}
				});
				$(this).dialog("close");
			},
			"取消" : function() {
				$(this).dialog("close");
			}
		}
	});
	 $("#dialog-confirm").show();
	
	confirm_cancel_dialog = $("#dialog-cancel-confirm").dialog({
		draggable : false,//防止拖曳
		resizable : false,//防止縮放
		autoOpen : false,
		height : 200,
		modal : true,
		show : {effect : "blind",duration : 300},
		hide : {effect : "fade",duration : 300},
		buttons : {
			"確認取消收帳" : function() {
				$(".checkbox_receive_cancel").each(function(){
					if($(this).prop("checked")){
						$.ajax({
							type : "POST",
							url : "accreceive.do",
							async : false,
							data : {
								action : "delete_receive_account",
								receivable_id : $(this).attr("value"),
								start_date : $("input[name='amount_start_date'").val(),
								end_date : $("input[name='amount_end_date'").val()
							},
							success : function(result) {
								$(".checkbox_receive_cancel").each(function(){
									if($(this).prop("checked")){
										var table = $("#account_receive_date_table").DataTable();
										table.row($(this).parents('tr')).remove().draw();
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
										if(json_obj[i].amount==null){
											json_obj[i].amount="";
										}
										if(json_obj[i].amount_date==null){
											json_obj[i].amount_date="";
										}
										if(json_obj[i].receive_amount==null){
											json_obj[i].receive_amount="";
										}
										if(json_obj[i].receive_date==null){
											json_obj[i].receive_date="";
										}
										if(json_obj[i].memo==null){
											json_obj[i].memo="";
										}
										result_table 
										+= "<tr>"
										+ "<td name='"+ json_obj[i].amount +"'>"+ money(json_obj[i].amount)+ "</td>"
										+ "<td name='"+ json_obj[i].amount_date +"'>"+ json_obj[i].amount_date+ "</td>"
										+ "<td name='"+ json_obj[i].receive_amount +"'>"+ money(json_obj[i].receive_amount)+ "</td>"
										+ "<td name='"+ json_obj[i].receive_date +"'>"+ json_obj[i].receive_date+ "</td>"
										+ "<td name='"+ json_obj[i].order_source +"'>"+ json_obj[i].order_source+ "</td>"
										+ "<td name='"+ json_obj[i].order_no +"'>"+ json_obj[i].order_no+ "</td>"
										+ "<td name='"+ json_obj[i].memo +"'>"+ json_obj[i].memo+ "</td>"
										+ "<td><input id='my-"+ json_obj[i].receivable_id+ "' type='checkbox' style='margin: 0 auto;' value='"+ json_obj[i].receivable_id
										+ "'class='checkbox_receive'></input><label for='my-"+ json_obj[i].receivable_id+ "'></label></td></tr>";	
									});
								}
								if(resultRunTime==0){
									$("#account_amount_date_contain").hide();
									$("#account_receive_date_contain").hide();
									if(!$("#receive_cancel_account_err_mes").length){
		                				$("<p id='receive_cancel_account_err_mes'>查無此結果</p>").appendTo($("#receive_date_form").parent());
		                			}else{
		                				$("#receive_cancel_account_err_mes").html("查無此結果");
		                			}
								}
								if ($.fn.DataTable.isDataTable("#account_amount_date_table")) {
									$("#account_amount_date_table").dataTable().fnDestroy();
								}
								if(resultRunTime!=0){
									$("#account_amount_date_contain").hide();
									$("#account_amount_date_table tbody").html(result_table);
									$("#account_amount_date_table").dataTable({
										  autoWidth: false,
										  scrollX:  true,
								          scrollY:"300px",
										  dom: 'Blfrtip',
								          buttons: [{
								                text: '批次收帳',
								                className: 'btn_receive',
								                action : function(e){
								            		e.preventDefault();
								            		var count = 0;
								            		var message = "";
								            		$(".checkbox_receive").each(function(){
								            			if($(this).prop("checked")){
								            				count+=1;
								            			}
								            		});
								            		message = "確認要收帳嗎? 總共" + count + "筆";
								            		$("#dialog-confirm p").text(message);
								            		confirm_dialog.dialog("open");
								                }
								            }
								          ],"language": {"url": "js/dataTables_zh-tw.txt","zeroRecords": "沒有符合的結果"}});
									$("#account_amount_date_table").find("td").css("text-align","center");
									$("#account_amount_date_table").find("th").css({"word-break":"break-all","min-width":"30px","text-align":"center" });
									$("#account_amount_date_table").find("td").css({"word-break":"break-all","min-width":"30px","text-align":"center" });
									$("td > label").css({"float":"none","display":"inline","margin":"0px 0px 0px 5px"});
									$("#account_amount_date_contain").fadeIn();
									if($("#receive_cancel_account_err_mes").length){
		                				$("#receive_cancel_account_err_mes").remove();
		                			}
								}
							}
						});
					}
				});
				$(this).dialog("close");
			},
			"取消" : function() {
				$(this).dialog("close");
			}
		}
	});	
	$("#dialog-cancel-confirm").show();
	$(".input-field-wrap").append("<div class='div_right_bottom upup'><img src='./images/upup.png'></div>");
	$(".input-field-wrap").after("<div class='div_right_top downdown' style='display:none;'><img src='./images/downdown.png'></div>");
	$(".upup").click(function(){
		$(".input-field-wrap").slideToggle("slow");
		$(".downdown").slideToggle();
	});
	$(".downdown").click(function(){
		$(".input-field-wrap").slideToggle("slow");
		$(".downdown").slideToggle();
	});
	$("#my").dialog({
		title: "明細",
		draggable : false, resizable : false, autoOpen : false,
		height : "auto", width : "auto", modal : true,
		show : {effect : "blind",duration : 300},
		hide : {effect : "fade",duration : 300},
		buttons : {
			"確認" : function() {$(this).dialog("close");},
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
	$(".date").datepicker({
		dayNamesMin:["日","一","二","三","四","五","六"],
		monthNames:["一月","二月","三月","四月","五月","六月","七月","八月","九月","十月","十一月","十二月"],
		dateFormat:"yy-mm-dd",
		changeYear:true
	});	
	//hold header
// 	$("#account_amount_date_table").find("th").css("min-width","120px");
// 	$("#account_receive_date_table").find("th").css("min-width","120px");
})
</script>
		<div class="datalistWrap" >
			<!--對話窗樣式-確認 -->
			<div id="dialog-confirm" title="確認批次收帳嗎?" style="display:none;">
				<br><p></p>
			</div>
			<!--對話窗樣式-確認-取消 -->
			<div id="dialog-cancel-confirm" title="確認取消批次收帳嗎?" style="display:none;">
				<br><p></p>
			</div>			
			<!-- 第一列 -->
			<div class="input-field-wrap">
			<div class="form-wrap">
				<form id="amount_date_form" name="trans_list_date_form">
					<div class="form-row">
						<label for="">
							<span class="block-label">應收起日</span>
							<input type="text" class="input-date" id="amount_start_date" name="amount_start_date">
						</label>
						<div class="forward-mark"></div>
						<label for="">
							<span class="block-label">應收迄日</span>
							<input type="text" class="input-date" id="amount_end_date" name="amount_end_date">
						</label>
						<button id="searh_amount_date" class="btn btn-darkblue">查詢</button>
					</div>
				</form>
			</div><!-- /.form-wrap -->
		</div><!-- /.input-field-wrap -->
			<!-- 第二列 -->
			<div class=" search-result-wrap" align="center" style="height:433px;">
				<div id="account_amount_date_contain" class="result-table-wrap" style="display:none;">
					<table id="account_amount_date_table" class="result-table">
						<thead>
							<tr class="">
								<th>應收帳款金額</th>
								<th>應收帳款產生日期</th>
								<th>實收帳款金額</th>
								<th>實收帳款產生日期</th>
								<th>交易平台</th>
								<th>訂單號</th>
								<th>備註</th>
								<th style="background-image: none !important;">勾選</th>
							</tr>
						</thead>
						<tbody>
						</tbody>
					</table>
				</div>
			</div>
		</div>

		<div class="datalistWrap">
			<!-- 第三列 -->
			<hr class="hr0">
			<div class="input-field-wrap">
				<div class="form-wrap">
					<form id="receive_date_form" name="trans_list_date_form">
						<div class="form-row">
							<label for="">
								<span class="block-label">實收起日</span>
								<input type="text" class="input-date" id="receive_start_date" name="receive_start_date">
							</label>
							<div class="forward-mark"></div>
							<label for="">
								<span class="block-label">實收迄日</span>
								<input type="text" class="input-date" id="receive_end_date" name="receive_end_date">
							</label>
							<button id="searh_receive_date" class="btn btn-darkblue">查詢</button>				
						</div>
					</form>
				</div><!-- /.form-wrap -->
			</div><!-- /.input-field-wrap -->
			<div class="row search-result-wrap" align="center" style="height:433px;">
				<div id="account_receive_date_contain" class="result-table-wrap" style="display:none;">
					<table id="account_receive_date_table" class="result-table">
						<thead>
							<tr>
								<th>應收帳款金額</th>
								<th>應收帳款產生日期</th>
								<th>實收帳款金額</th>
								<th>實收帳款產生日期</th>
								<th>交易平台</th>
								<th>訂單號</th>
								<th>備註</th>
								<th style="background-image: none !important;">勾選</th>
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
	<div id="my"></div>
</body>
</html>