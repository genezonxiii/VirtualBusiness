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
<style type="text/css">
.dataTables_wrapper .dt-buttons {float:right;}
</style>
<title>進貨退回</title>
<meta charset="utf-8">
<link rel="Shortcut Icon" type="image/x-icon" href="./images/Rockettheme-Ecommerce-Shop.ico" />

<link href="<c:url value="css/css.css" />" rel="stylesheet">
<link href="<c:url value="css/jquery.dataTables.min.css" />" rel="stylesheet">
<link href="<c:url value="css/1.11.4/jquery-ui.css" />" rel="stylesheet">
<link href="<c:url value="css/dataTables.jqueryui.min.css" />" rel="stylesheet">
<link href="<c:url value="css/buttons.jqueryui.min.css" />" rel="stylesheet">
<link rel="stylesheet" href="css/styles.css" />
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
<!-- data table buttons -->

<script type="text/javascript" src="js/dataTables.buttons.min.js"></script>
<script type="text/javascript" src="js/buttons.jqueryui.min.js"></script>
<script>
$(function(){
	
// 	$("#button_1").click(
// 		function(e){
// 			e.preventDefault();
// 			var count = 0;
// 			var message = "";
// 			$(".checkbox_return").each(function(){1234
// 				if($(this).prop("checked")){
// 					count+=1;
// 				}
// 			});
// 			message = "確認要進貨退回嗎? 總共" + count + "筆";
// 			$("#dialog_confirm p").text(message);
// 			confirm_dialog.dialog("open");
// 	    }
// 	);
// 	$("#button_2").click(
// 		function(e){
//        		e.preventDefault();
//        		var count = 0;
//        		var message = "";
//        		$(".checkbox_return_cancel").each(function(){
//        			if($(this).prop("checked")){
//        				count+=1;
//        			}
//        		});
//        		message = "確認要取消進貨退回嗎? 總共" + count + "筆";
//        		$("#dialog_cancel_confirm p").text(message);
//        		confirm_cancel_dialog.dialog("open");
//        }
// 	);
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
									+ "<td name='"+ json_obj[i].amount +"'>"+ json_obj[i].amount+ "</td>"
									+ "<td name='"+ json_obj[i].memo +"'>"+ json_obj[i].memo+ "</td>"
									+ "<td><input id='my-"+ json_obj[i].purchase_id+ "' type='checkbox' style='margin: 0 auto;' value='"+ json_obj[i].purchase_id+ "'name='"+ json_obj[i].supply_id
									+ "'class='checkbox_return_cancel'></input><label for='my-"+ json_obj[i].purchase_id+ "'></label></td></tr>"
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
							$("#purchasereturns_true_table").dataTable({
								  autoWidth: false,
								  scrollX:  true,
						          scrollY:"300px",
								  dom: 'Blfrtip',
						          buttons: [{
						                text: '取消進貨退回',
						                className: 'btn_purchase_return_cancel btn btn-primary',
						                action : function(e){
						            		e.preventDefault();
						            		var count = 0;
						            		var message = "";
						            		$(".checkbox_return_cancel").each(function(){
						            			if($(this).prop("checked")){
						            				count+=1;
						            			}
						            		});
						            		message = "確認要取消進貨退回嗎? 總共" + count + "筆";
						            		$("#dialog_cancel_confirm p").text(message);
						            		confirm_cancel_dialog.dialog("open");
						                }
						            }
						          ],"language": {"url": "js/dataTables_zh-tw.txt","zeroRecords": "沒有符合的結果","zeroRecords": "沒有符合的結果"}});
									$("td > label").css({"float":"none","display":"inline","margin":"0px 0px 0px 5px"});
							if($("#purchase_return_date_err_mes").length){
                				$("#purchase_return_date_err_mes").remove();
                			}
						}
					}
				});
		}
	});		
	//進貨日查詢相關設定
	$("#search_purchase_date").click(function(e) {
		e.preventDefault();
		if($("#purchase_date_form").valid()){
			$.ajax({
				type : "POST",
				url : "purchreturn.do",
				data : {
					action : "search_purchase_date",
					purchase_start_date: $("#purchase_start_date").val(),
					purchase_end_date: $("#purchase_end_date").val()
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
									+ "<td name='"+ json_obj[i].amount +"'>"+ json_obj[i].amount+ "</td>"
									+ "<td name='"+ json_obj[i].memo +"'>"+ json_obj[i].memo+ "</td>"
									+ "<td><input id='my-"+ json_obj[i].purchase_id+ "' type='checkbox' style='margin: 0 auto;' value='"+ json_obj[i].purchase_id+ "'name='"+ json_obj[i].supply_id
									+ "'class='checkbox_return'></input><label for='my-"+ json_obj[i].purchase_id+ "'></label></td></tr>"
								}
							});
						}
						if(json_obj[resultRunTime-1].message=="如要以日期查詢，請完整填寫起日欄位"){
							$("#purchasereturns_true_contain").hide();
							$("#purchasereturns_false_contain").hide();
							if(!$("#purchase_date_err_mes").length){
                				$("<p id='purchase_date_err_mes'>如要以日期查詢，請完整填寫起日欄位</p>").appendTo($("#purchase_date_form").parent());
                			}else{
                				$("#purchase_date_err_mes").html("如要以日期查詢，請完整填寫起日欄位");
                			}
						}
						if(json_obj[resultRunTime-1].message=="如要以日期查詢，請完整填寫訖日欄位"){
							$("#purchasereturns_true_contain").hide();
							$("#purchasereturns_false_contain").hide();
							if(!$("#purchase_date_err_mes").length){
                				$("<p id='purchase_date_err_mes'>如要以日期查詢，請完整填寫訖日欄位</p>").appendTo($("#purchase_date_form").parent());
                			}else{
                				$("#purchase_date_err_mes").html("如要以日期查詢，請完整填寫訖日欄位");
                			}
						}						
						if(json_obj[resultRunTime-1].message=="起日不可大於訖日"){
							$("#purchasereturns_true_contain").hide();
							$("#purchasereturns_false_contain").hide();
							if(!$("#purchase_date_err_mes").length){
                				$("<p id='purchase_date_err_mes'>起日不可大於訖日</p>").appendTo($("#purchase_date_form").parent());
                			}else{
                				$("#purchase_date_err_mes").html("起日不可大於訖日");
                			}
						}							
						if(resultRunTime==0){
							$("#purchasereturns_true_contain").hide();
							$("#purchasereturns_false_contain").hide();
							if(!$("#purchase_date_err_mes").length){
                				$("<p id='purchase_date_err_mes'>查無此結果</p>").appendTo($("#purchase_date_form").parent());
                			}else{
                				$("#purchase_date_err_mes").html("查無此結果");
                			}
						}
						$("#purchasereturns_false_table").dataTable().fnDestroy();
						if(resultRunTime!=0&&json_obj[resultRunTime-1].message=="驗證通過"){
							$("#purchasereturns_true_contain").hide();
							$("#purchasereturns_false_contain").show();
							$("#purchasereturns_false_table tbody").html(result_table);
							//$("#purchasereturns_false_table").find("td").css("text-align", "center");
							$("#purchasereturns_false_table").find("th").css({"word-break":"break-all","min-width":"30px","text-align":"center" });
							$("#purchasereturns_false_table").find("td").css({"word-break":"break-all","min-width":"30px","text-align":"center" });
							$("td > label").css({"float":"none","display":"inline","margin":"0px 0px 0px 5px"});
							$("#purchasereturns_false_table").dataTable({
								  autoWidth: false,
								  scrollX:  true,
						          scrollY:"300px",
								  dom: 'Blfrtip',
						          buttons: [{
						                text: '進貨退回',
						                className: 'btn_purchase_return',
						                action : function(e){
						            		e.preventDefault();
						            		var count = 0;
						            		var message = "";
						            		$(".checkbox_return").each(function(){
						            			if($(this).prop("checked")){
						            				count+=1;
						            			}
						            		});
						            		message = "確認要進貨退回嗎? 總共" + count + "筆";
						            		$("#dialog_confirm p").text(message);
						            		confirm_dialog.dialog("open");
						                }
						            }
						          ],"language": {"url": "js/dataTables_zh-tw.txt","zeroRecords": "沒有符合的結果","zeroRecords": "沒有符合的結果"}});
							if($("#purchase_date_err_mes").length){
                				$("#purchase_date_err_mes").remove();
                			}
						}
					}
				});
		}
	});		
    //這邊是用供應商名稱去自動查詢，然後得到ID
    $("#search_purchase_by_supply_name").autocomplete({
        minLength: 2,
        source: function (request, response) {
            $.ajax({
                url : "purchreturn.do",
                type : "POST",
                cache : false,
                delay : 1500,
                data : {
                	action : "search_supply_name",
                	term : request.term
                },
                success: function(data) {
                	var json_obj = $.parseJSON(data);
                	response($.map(json_obj, function (item) {
                        return {
                          label: item.supply_name,
                          value: item.supply_name
                        }
                      }))
                }
            });
        },
	    change: function(event, ui) {
	        var source = $(this).val();
	            var temp = $(".ui-autocomplete li").map(function () { return $(this).text()}).get();
	        var found = $.inArray(source, temp);
	
	        if(found < 0) {
	            $(this).val('');
	            $(this).attr("placeholder","請輸入正確的供應商名稱!");
	        }
	    }     
     });
    $("#search_purchase_by_supply_name").bind('focus', function(){ $(this).attr("placeholder","請輸入供應商名稱以供查詢"); } );
	//供應商ID查詢相關設定
	$("#search_supply_name").click(function(e) {
		e.preventDefault();
		$.ajax({
			type : "POST",
			url : "purchreturn.do",
			data : {
				action : "search",
				supply_name : $("input[name='search_purchase_by_supply_name'").val()
			},
			success : function(result) {
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
								+ "<td name='"+ json_obj[i].amount +"'>"+ json_obj[i].amount+ "</td>"
								+ "<td name='"+ json_obj[i].memo +"'>"+ json_obj[i].memo+ "</td>"
								+ "<td><input id='my-"+ json_obj[i].purchase_id+ "' type='checkbox' style='margin: 0 auto;' value='"+ json_obj[i].purchase_id+ "'name='"+ json_obj[i].supply_id
								+ "'class='checkbox_return'></input><label for='my-"+ json_obj[i].purchase_id+ "'></label></td></tr>"
						});
					}
					if(resultRunTime==0){
						$("#purchasereturns_false_contain").hide();
						$("#purchasereturns_true_contain").hide();
						if(!$("#supply_name_err_mes").length){
            				$("<p id='supply_name_err_mes'>查無此結果</p>").appendTo($("#purchase_search_contain").parent());
            			}else{
            				$("#supply_name_err_mes").html("查無此結果");
            			}
					}
					if ($.fn.DataTable.isDataTable("#purchasereturns_false_table")) {
						$("#purchasereturns_false_table").dataTable().fnDestroy();
					}
					if(resultRunTime!=0){
						$("#purchasereturns_false_contain").show();
						$("#purchasereturns_false_table tbody").html(result_table);
						//$("#purchasereturns_false_table").find("td").css("text-align", "center");
						$("#purchasereturns_false_table").find("th").css({"word-break":"break-all","min-width":"30px","text-align":"center" });
						$("#purchasereturns_false_table").find("td").css({"word-break":"break-all","min-width":"30px","text-align":"center" });
						$("td > label").css({"float":"none","display":"inline","margin":"0px 0px 0px 5px"});
						$("#purchasereturns_false_table").dataTable({
							  autoWidth: false,
							  scrollX:  true,
					          scrollY:"300px",
							  dom: 'Blfrtip',
					          buttons: [{
					                text: '進貨退回',
					                className: 'btn_purchase_return',
					                action : function(e){
					            		e.preventDefault();
					            		var count = 0;
					            		var message = "";
					            		$(".checkbox_return").each(function(){
					            			if($(this).prop("checked")){
					            				count+=1;
					            			}
					            		});
					            		message = "確認要進貨退回嗎? 總共" + count + "筆";
					            		$("#dialog_confirm p").text(message);
					            		confirm_dialog.dialog("open");
					                }
					            }
					          ],"language": {"url": "js/dataTables_zh-tw.txt","zeroRecords": "沒有符合的結果","zeroRecords": "沒有符合的結果"}});
						$("td > label").css({"float":"none","display":"inline","margin":"0px 0px 0px 5px"});
						if($("#supply_name_err_mes").length){
            				$("#supply_name_err_mes").remove();
            			}
					}						
				}
			});
	});
	confirm_dialog = $("#dialog_confirm").dialog({
		draggable : false,//防止拖曳
		resizable : false,//防止縮放
		autoOpen : false,
		height : 200,
		modal : true,
		show : {effect : "blind",duration : 300},
		hide : {effect : "fade",duration : 300},
		buttons : {
			"確認退回" : function() {
				$(".checkbox_return").each(function(){
					if($(this).prop("checked")){
						$.ajax({
							type : "POST",
							url : "purchreturn.do",
							data : {
								action : "insert_purchase_return",
								purchase_id : $(this).attr("value"),
								return_date : getCurrentDate()
							},
							success : function(result) {
								$(".checkbox_return").each(function(){
									if($(this).prop("checked")){
										var $row = $(this).parent().parent();
										var table = $("#purchasereturns_false_table").dataTable();
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
								var result_table = "";
								if(resultRunTime!=0){
									$.each(json_obj,function(i, item) {
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
										+ "<td name='"+ json_obj[i].amount +"'>"+ json_obj[i].amount+ "</td>"
										+ "<td name='"+ json_obj[i].memo +"'>"+ json_obj[i].memo+ "</td>"
										+ "<td><input id='my-"+ json_obj[i].purchase_id+ "' type='checkbox' style='margin: 0 auto;' value='"+ json_obj[i].purchase_id+ "'name='"+ json_obj[i].supply_id
										+ "'class='checkbox_return_cancel'></input><label for='my-"+ json_obj[i].purchase_id+ "'></label></td></tr>"	
									});
								}							
								if(resultRunTime==0){
									$("#purchasereturns_true_contain").hide();
								}
								if ($.fn.DataTable.isDataTable("#purchasereturns_true_table")) {
									$("#purchasereturns_true_table").dataTable().fnDestroy();
								}
								if(resultRunTime!=0){
									$("#purchasereturns_true_contain").show();
									$("#purchasereturns_true_table tbody").html(result_table);
									$("#purchasereturns_true_table").find("th").css({"word-break":"break-all","min-width":"30px","text-align":"center" });
									$("#purchasereturns_true_table").find("td").css({"word-break":"break-all","min-width":"30px","text-align":"center" });
									$("td > label").css({"float":"none","display":"inline","margin":"0px 0px 0px 5px"});
									$("#purchasereturns_true_table").dataTable({
										  autoWidth: false,
										  scrollX:  true,
								          scrollY:"300px",
										  dom: 'Blfrtip',
								          buttons: [{
								                text: '取消進貨退回',
								                className: 'btn_purchase_return_cancel',
								                action : function(e){
								            		e.preventDefault();
								            		var count = 0;
								            		var message = "";
								            		$(".checkbox_return_cancel").each(function(){
								            			if($(this).prop("checked")){
								            				count+=1;
								            			}
								            		});
								            		message = "確認要取消進貨退回嗎? 總共" + count + "筆";
								            		$("#dialog_cancel_confirm p").text(message);
								            		confirm_cancel_dialog.dialog("open");
								                }
								            }
								          ],"language": {"url": "js/dataTables_zh-tw.txt","zeroRecords": "沒有符合的結果","zeroRecords": "沒有符合的結果"}});
									$("#purchasereturns_true_table").find("td").css("text-align","center");
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
	$("#dialog_confirm").show();
	confirm_cancel_dialog = $("#dialog_cancel_confirm").dialog({
		draggable : false,//防止拖曳
		resizable : false,//防止縮放
		autoOpen : false,
		height : 200,
		modal : true,
		show : {effect : "blind",duration : 300},
		hide : {effect : "fade",duration : 300},
		buttons : {
			"確認取消" : function() {
				$(".checkbox_return_cancel").each(function(){
					if($(this).prop("checked")){
						$.ajax({
							type : "POST",
							url : "purchreturn.do",
							data : {
								action : "delete_purchase_return",
								purchase_id : $(this).attr("value"),
								supply_name : $("input[name='search_purchase_by_supply_name'").val()
							},
							success : function(result) {
								$(".checkbox_return_cancel").each(function(){
									if($(this).prop("checked")){
										var $row = $(this).parent().parent();
										var table = $("#purchasereturns_true_table").dataTable();
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
										+ "<td name='"+ json_obj[i].amount +"'>"+ json_obj[i].amount+ "</td>"
										+ "<td name='"+ json_obj[i].memo +"'>"+ json_obj[i].memo+ "</td>"
										+ "<td><input id='my-"+ json_obj[i].purchase_id+ "' type='checkbox' style='margin: 0 auto;' value='"+ json_obj[i].purchase_id+ "'name='"+ json_obj[i].supply_id
										+ "'class='checkbox_return'></input><label for='my-"+ json_obj[i].purchase_id+ "'></label></td></tr>"	
									});
								}							
								if(resultRunTime==0){
									$("#purchasereturns_false_contain").hide();
								}
								if ($.fn.DataTable.isDataTable("#purchasereturns_false_table")) {
									$("#purchasereturns_false_table").dataTable().fnDestroy();
								}
								if(resultRunTime!=0){
									$("#purchasereturns_false_contain").show();
									$("#purchasereturns_false_table tbody").html(result_table);
									$("#purchasereturns_false_table").dataTable({
										  autoWidth: false,
										  scrollX:  true,
								          scrollY:"300px",
										  dom: 'Blfrtip',
								          buttons: [{
								                text: '進貨退回',
								                className: 'btn_purchase_return',
								                action : function(e){
								            		e.preventDefault();
								            		var count = 0;
								            		var message = "";
								            		$(".checkbox_return").each(function(){
								            			if($(this).prop("checked")){
								            				count+=1;
								            			}
								            		});
								            		message = "確認要進貨退回嗎? 總共" + count + "筆";
								            		$("#dialog_confirm p").text(message);
								            		confirm_dialog.dialog("open");
								                }
								            }
								          ],"language": {"url": "js/dataTables_zh-tw.txt","zeroRecords": "沒有符合的結果","zeroRecords": "沒有符合的結果"}});
									$("#purchasereturns_false_table").find("td").css("text-align","center");
									$("#purchasereturns_false_table").find("th").css({"word-break":"break-all","min-width":"30px","text-align":"center" });
									$("#purchasereturns_false_table").find("td").css({"word-break":"break-all","min-width":"30px","text-align":"center" });
									$("td > label").css({"float":"none","display":"inline","margin":"0px 0px 0px 5px"});
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
	$("#dialog_cancel_confirm").show();
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
	$("#purchasereturns_false_table").find("th").css("min-width","120px");
	$("#purchasereturns_true_table").find("th").css("min-width","120px");	
})
</script>
		<div class="datalistWrap">
			<!--對話窗樣式-確認 -->
			<div id="dialog_confirm" title="確認銷貨退回嗎?" style="display:none;">
				<br><p></p>
			</div>
			<!--對話窗樣式-確認-取消 -->
			<div id="dialog_cancel_confirm" title="確認取消銷貨退回嗎?" style="display:none;">
				<br><p></p>
			</div>			
			<!-- 第一列 -->
			<div class="input-field-wrap">
			<div class="form-wrap" >
				<div class="form-row" id="purchase_search_contain"> 
					<label for="">
						<span class="block-label">供應商名稱查詢</span>
						<input type="text" id="search_purchase_by_supply_name" name="search_purchase_by_supply_name">
					</label>
					<button id="search_supply_name" class="btn btn-darkblue">查詢</button>
				</div>
				<div class="form-row" id="select_dates_contain">
					<form id="purchase_date_form" name="purchase_date_form">
						<label for="">
							<span class="block-label">進貨起日</span>
							<input type="text" class="input-date" id="purchase_start_date" name="purchase_start_date">
						</label>
						<div class="forward-mark"></div>
						<label for="">
							<span class="block-label">進貨迄日</span>
							<input type="text" class="input-date" id="purchase_end_date" name="purchase_end_date">
						</label>
						<button id="search_purchase_date" class="btn btn-darkblue">查詢</button>
					</form>
				</div>
			</div><!-- /.form-wrap -->
		</div><!-- /.input-field-wrap -->
<!-- 			<div class="row" align="center"> -->
<!-- 				<div id="purchase_search_contain" class="ui-widget"> -->
<!-- 					<table id="purchases_search_create"> -->
<!-- 						<thead> -->
<!-- 							<tr> -->
<!-- 								<td><input type="text" id="search_purchase_by_supply_name" name="search_purchase_by_supply_name" placeholder="請輸入供應商名稱以供查詢" ></td> -->
<!-- 								<td> -->
<!-- 									&nbsp;&nbsp;<button id="search_supply_name">查詢</button> -->
<!-- 								</td> -->
<!-- 							</tr> -->
<!-- 						</thead> -->
<!-- 					</table> -->
<!-- 				</div> -->
<!-- 			</div> -->
			<!-- 第二列 -->
<!-- 			<div class="row" align="center"> -->
<!-- 				<div id="select_dates_contain" class="ui-widget"> -->
<!-- 					<form id="purchase_date_form" name="purchase_date_form"> -->
<!-- 						<table> -->
<!-- 							<thead> -->
<!-- 								<tr> -->
<!-- 									<td><input type="text" id="purchase_start_date" name="purchase_start_date" class="date" placeholder="請輸入進貨日期起日"></td> -->
<!-- 									<th><p>&nbsp;&nbsp;~&nbsp;&nbsp;</p></th> -->
<!-- 									<td><input type="text" id="purchase_end_date" name="purchase_end_date"class="date" placeholder="請輸入進貨日期迄日"></td> -->
<!-- 									<td>&nbsp;&nbsp;<button id="search_purchase_date">查詢</button></td> -->
<!-- 								</tr> -->
<!-- 							</thead> -->
<!-- 						</table> -->
<!-- 					</form> -->
<!-- 				</div> -->
<!-- 			</div> -->
			<!-- 第三列 -->
			<div class="row search-result-wrap" align="center"style="height:433px;">
<!-- 				<div class="form-row"> -->
<!-- 					<button id="button_1" align="center" class="btn btn-wide btn-darkblue">進貨退回</button> -->
<!-- 				</div> -->
				<div id="purchasereturns_false_contain" class="result-table-wrap" style="display:none;">
					<table id="purchasereturns_false_table" class="result-table">
						<thead>
							<tr class="ui-widget-header">
								<th>進貨單號</th>
								<th>進貨日期</th>
								<th>進貨發票號碼</th>
								<th>發票樣式</th>
								<th>進貨發票金額</th>
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
			<!-- 第四列 -->
<!-- 			<div class="row" align="center"> -->
<!-- 				<div class="ui-widget"> -->
<!-- 					<form id="return_date_form" name="trans_dis_date_form"> -->
<!-- 						<table> -->
<!-- 							<thead> -->
<!-- 								<tr> -->
<!-- 									<td><input type="text" id="return_start_date" name="return_start_date" class="date" placeholder="請輸入退貨起日"></td> -->
<!-- 									<th><p>&nbsp;&nbsp;~&nbsp;&nbsp;</p></th> -->
<!-- 									<td><input type="text" id="return_end_date" name="return_end_date" class="date" placeholder="請輸入退貨迄日"></td> -->
<!-- 									<td>&nbsp;&nbsp;<button id="search_return_date">查詢</button></td> -->
<!-- 								</tr> -->
<!-- 							</thead> -->
<!-- 						</table> -->
<!-- 					</form> -->
<!-- 				</div> -->
<!-- 			</div>		 -->
			<!-- 第五列 -->
			<div class="row search-result-wrap" align="center"style="height:433px;">
<!-- 				<div class="form-row"> -->
<!-- 					<button id="button_2" align="center" class="btn btn-wide btn-darkblue">取消進貨退回</button> -->
<!-- 				</div> -->
				<div id="purchasereturns_true_contain" class="result-table-wrap" style="display:none;">
					<table id="purchasereturns_true_table" class="result-table">
						<thead>
							<tr class="ui-widget-header">
								<th>進貨單號</th>
								<th>進貨日期</th>
								<th>進貨發票號碼</th>
								<th>發票樣式</th>
								<th>進貨發票金額</th>
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