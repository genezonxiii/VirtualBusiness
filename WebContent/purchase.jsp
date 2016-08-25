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
<title>進貨管理</title>
<meta charset="utf-8">
<link rel="Shortcut Icon" type="image/x-icon" href="./images/Rockettheme-Ecommerce-Shop.ico" />
<link rel="stylesheet" href="css/styles.css" />
<link href="<c:url value="css/css.css" />" rel="stylesheet">
<link href="<c:url value="css/jquery.dataTables.min.css" />" rel="stylesheet">
<link href="<c:url value="css/1.11.4/jquery-ui.css" />" rel="stylesheet">
</head>
<body>
	<jsp:include page="template.jsp" flush="true"/>
	<div class="content-wrap" >
<script type="text/javascript" src="js/jquery-1.10.2.js"></script>
<script type="text/javascript" src="js/jquery.dataTables.min.js"></script>
<script type="text/javascript" src="js/jquery-ui.min.js"></script>
<script type="text/javascript" src="js/jquery-migrate-1.4.1.min.js"></script>
<script type="text/javascript" src="js/jquery.validate.min.js"></script>
<script type="text/javascript" src="js/additional-methods.min.js"></script>
<script type="text/javascript" src="js/messages_zh_TW.min.js"></script>
<script type="text/javascript" src="js/jquery.scannerdetection.js"></script>
<script>
	var new_or_edit=0;
	jQuery(document).ready(function($) {
	    $(window).scannerDetection();
	    $(window).bind('scannerDetectionComplete',function(e,data){
	    		if(data.string=="success"){return;}
	    		$.ajax({url : "product.do", type : "POST", cache : false,
		            data : {
		            	action : "find_barcode",
		            	barcode : data.string,
		            },
		            success: function(result) {
		            	var json_obj = $.parseJSON(result);
						if(json_obj.length!=0){
							if(new_or_edit==1){
								//new_or_edit=3;
								$("#insert_detail_c_product_id").val(json_obj[0].c_product_id);
								$("#insert_detail_product_name").val(json_obj[0].product_name);
								$("#insert_detail_product_n").val(json_obj[0].keep_stock);
								$("#insert_detail_product_cost").val(json_obj[0].cost);
								$("#insert_detail_product_price").val(json_obj[0].cost);
				    		}
				    		if(new_or_edit==2){
				    			//new_or_edit=3;
				    			$("#update_detail_c_product_id").val(json_obj[0].c_product_id);
				    			$("#update_detail_product_name").val(json_obj[0].product_name);
				    			$("#update_detail_product_n").val(json_obj[0].keep_stock);
								$("#update_detail_product_cost").val(json_obj[0].cost);
								$("#update_detail_product_price").val(json_obj[0].cost);
				    		}
						}
						if(json_obj.length==0){
							$("#warning").html("<h3>該條碼無產品存在</h3>請至'商品管理'介面&nbsp;定義該條碼。");
							$("#warning").dialog("open");
						}
		            }
	    		});
	        })
	        .bind('scannerDetectionError',function(e,data){console.log('detection error '+data.string);})
	        .bind('scannerDetectionReceive',function(e,data){console.log(data);});
	
	});
	$(function() {
		
// 		$("#purchase_detail_contain_row").dialog({
// 			title: "明細",
// 			draggable : true,//防止拖曳
// 			resizable : false,//防止縮放
// 			autoOpen : false,
// 			height : "auto",
// 			width : "auto",
// 			modal : true,
// 			show : {effect : "blind",duration : 300},
// 			hide : {effect : "fade",duration : 300},
// 			buttons : {
// 				"確認" : function() {$(this).dialog("close");}
// 			}
// 		});
		var uuid = "";
		var seqNo = "";
		var supply_id ="";
		var purchase_id ="";
		var product_id = "";
		var c_product_id = "";
		var product_name = "";
		//=============自定義validator=============
		//字符最大長度驗證（一個中文字符長度為2）
		jQuery.validator.addMethod("stringMaxLength", function(value, element, param) { 
			var length = value.length; 
			for ( var i = 0; i < value .length; i++) { 
				if (value.charCodeAt(i) > 127) { 
				length++; 
				} 
			} 
			return this.optional(element) || (length <= param); 
		}, $.validator.format("長度不能大於{0}!"));
		//字母數字
		jQuery.validator.addMethod("alnum", function(value, element) {
		return this.optional(element) || /^[a-zA-Z0-9]+$/.test(value);
		}, "只能包括英文字母和數字");
		//=========================================
		//驗證
		 $("#purchase_date_form").validate({
				rules : {
					purchase_start_date : {
						dateISO : true
					},
					purchase_end_date:{
						dateISO : true
					}
				},
				messages:{
					purchase_start_date : {
						dateISO : "日期格式錯誤"
					},
					purchase_end_date : {
						dateISO : "日期格式錯誤"
					}
				}
			});			
		var validator_insert = $("#insert-dialog-form-post").validate({
			rules : {
				supply_id : {
					stringMaxLength : 40,
					required : true
				},
				memo:{
					stringMaxLength : 200
				},
				purchase_date : {
					dateISO : true,
					required : true
				},
				invoice : {
					stringMaxLength : 12,
					alnum :true
				},
				amount : {
					required : true,
					digits :true,
					min : 1
				},
				return_date : {
					dateISO : true
				}
			}
		});
		var validator_update = $("#update-dialog-form-post").validate({
			rules : {
				supply_id : {
					stringMaxLength : 40,
					required : true
				},
				memo:{
					stringMaxLength : 200
				},
				purchase_date : {
					dateISO : true
				},
				invoice : {
					stringMaxLength : 12,
					alnum :true
				},
				amount : {
					required : true,
					digits :true,
					min : 1
				},
				return_date : {
					dateISO : true
				}
			}
		});
		var validator_detail_insert = $("#detail-insert-dialog-form-post").validate({
			rules : {
				insert_detail_product_name : {
					stringMaxLength : 80,
					required : true
				},
				memo:{
					stringMaxLength : 200
				},
				quantity : {
					required : true,
					digits :true
				},
				cost : {
					required : true,
					number :true
				}
			}
		});
		var validator_detail_update = $("#detail-update-dialog-form-post").validate({
			rules : {
				update_detail_product_name : {
					stringMaxLength : 80,
					required : true
				},
				memo:{
					stringMaxLength : 200
				},
				quantity : {
					required : true,
					digits :true
				},
				cost : {
					required : true,
					number :true
				}
			}
		});		
		//進貨日查詢相關設定
		$("#search_purchase_date").click(function(e) {
			e.preventDefault();
			$("#purchase_detail_contain_row").hide();
			if($("#purchase_date_form").valid()){
				$.ajax({
					type : "POST",
					url : "purchase.do",
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
										+ "<td ><div class='table-row-func btn-in-table btn-gray' style='float:left;'><i class='fa fa-ellipsis-h'></i>"
										+ "	<div class='table-function-list'>"
										+ "		<button class='btn-in-table btn-darkblue btn_update' title='修改' id='"+json_obj[i].seq_no+"'value='"+ json_obj[i].purchase_id + "' ><i class='fa fa-pencil'></i></button>"
										+ "		<button class='btn-in-table btn-alert btn_delete' title='刪除' id='"+json_obj[i].seq_no+"'value='"+ json_obj[i].purchase_id + "'><i class='fa fa-trash'></i></button>"
										+ "		<button class='btn-in-table btn-primary btn_detail' title='顯示明細' value='"+ json_obj[i].purchase_id + "'><i class='fa fa-list'></i></button>"
										+ "		<button class='btn-in-table btn-green btn_create' title='新增明細' value='"+ json_obj[i].purchase_id + "'><i class='fa fa-pencil-square-o'></i></button>"
										+ "	</div></div></td></tr>";	
// 										$('#searh_supply_name').prop('title', '刪除');
										

									}
								});
							}
							if(json_obj[resultRunTime-1].message=="如要以日期查詢，完整填寫起日欄位"){
								$("#purchases_contain_row").hide();
								if(!$("#purchase_date_err_mes").length){
	                				$("<p id='purchase_date_err_mes'>如要以日期查詢，完整填寫起日欄位</p>").appendTo($("#purchase_date_form").parent());
	                			}else{
	                				$("#purchase_date_err_mes").html("如要以日期查詢，完整填寫起日欄位");
	                			}
							}
							if(json_obj[resultRunTime-1].message=="如要以日期查詢，完整填寫訖日欄位"){
								$("#purchases_contain_row").hide();
								if(!$("#purchase_date_err_mes").length){
	                				$("<p id='purchase_date_err_mes'>如要以日期查詢，完整填寫訖日欄位</p>").appendTo($("#purchase_date_form").parent());
	                			}else{
	                				$("#purchase_date_err_mes").html("如要以日期查詢，完整填寫訖日欄位");
	                			}
							}						
							if(json_obj[resultRunTime-1].message=="起日不可大於訖日"){
								$("#purchases_contain_row").hide();
								if(!$("#purchase_date_err_mes").length){
	                				$("<p id='purchase_date_err_mes'>起日不可大於訖日</p>").appendTo($("#purchase_date_form").parent());
	                			}else{
	                				$("#purchase_date_err_mes").html("起日不可大於訖日");
	                			}
							}	
							if(resultRunTime==1){$("#supply_name_err_mes").html("查無此結果");}else{$("#supply_name_err_mes").html("");}
							if(resultRunTime==0){
								$("#purchases_contain_row").hide();
								if(!$("#purchase_date_err_mes").length){
	                				$("<p id='purchase_date_err_mes'>查無此結果</p>").appendTo($("#purchase_date_form").parent());
	                			}else{
	                				$("#purchase_date_err_mes").html("查無此結果");
	                			}
							}
							$("#purchases").dataTable().fnDestroy();
							if(resultRunTime!=0&&json_obj[resultRunTime-1].message=="驗證通過"){
								if($("#purchase_date_err_mes").length){
									$("#purchase_date_err_mes").html("");
	                			}
								$("#purchases_contain_row").show();
								$("#purchases tbody").html(result_table);
								$("#purchases").dataTable({
									  autoWidth: false,
									  scrollX:  true,
							          scrollY:"300px","language": {"url": "js/dataTables_zh-tw.txt","zeroRecords": "沒有符合的結果"}});
								$("#purchases").find("td").css("text-align", "center");
							}
						}
					});
			}
		});		
		//供應商ID查詢相關設定
		$("#searh_supply_name").click(function(e) {
			e.preventDefault();
			$.ajax({
				type : "POST",
				url : "purchase.do",
				data : {
					action : "search",
					supply_name : $("input[name='searh_purchase_by_supply_name'").val()
				},
				success : function(result) {
					console.log(result);
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
								+ "<td><div class='table-row-func btn-in-table btn-gray' style='float:left;'><i class='fa fa-ellipsis-h'></i>"
								+ "	<div class='table-function-list'>"
								+ "		<button class='btn-in-table btn-darkblue btn_update' title='修改' id='"+json_obj[i].seq_no+"'value='"+ json_obj[i].purchase_id + "' ><i class='fa fa-pencil'></i></button>"
								+ "		<button class='btn-in-table btn-alert btn_delete' title='刪除' id='"+json_obj[i].seq_no+"'value='"+ json_obj[i].purchase_id + "'><i class='fa fa-trash'></i></button>"
								+ "		<button class='btn-in-table btn-primary btn_detail' title='顯示明細' value='"+ json_obj[i].purchase_id + "'><i class='fa fa-list'></i></button>"
								+ "		<button class='btn-in-table btn-green btn_create' title='新增明細' value='"+ json_obj[i].purchase_id + "'><i class='fa fa-pencil-square-o'></i></button>"
								+ "	</div></div></td></tr>";
							});
						}
						if(resultRunTime==1){$("#supply_name_err_mes").html("查無此結果");}else{$("#supply_name_err_mes").html("");}
						if(resultRunTime==0){
							$("#purchases_contain_row").hide();
							$("#supply_name_err_mes").html("查無此結果");
						}else{$("#supply_name_err_mes").html("");}
						$("#purchases").dataTable().fnDestroy();
						if(resultRunTime!=0){
							if($("#supply_name_err_mes").length){
								$("#supply_name_err_mes").html("");
                			}
							$("#purchases_contain_row").show();
							$("#purchases tbody").html(result_table);
							$("#purchases").dataTable({
								  autoWidth: false,
								  scrollX:  true,
						          scrollY:"300px","language": {"url": "js/dataTables_zh-tw.txt","zeroRecords": "沒有符合的結果"}});
							$("#purchases").find("td").css("text-align", "center");
						}						
					}
				});
		});
		//確認Dialog相關設定(刪除功能)
		confirm_dialog = $("#dialog-confirm").dialog({
			draggable : false,//防止拖曳
			resizable : false,//防止縮放
			autoOpen : false,
			height : 140,
			modal : true,
			show : {effect : "blind",duration : 300},
			hide : {effect : "fade",duration : 300},
			buttons : {
				"確認刪除" : function() {
					$.ajax({
						type : "POST",
						url : "purchase.do",
						data : {
							action : "delete",
							purchase_id : uuid
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
									+ "<td><div class='table-row-func btn-in-table btn-gray' style='float:left;'><i class='fa fa-ellipsis-h'></i>"
									+ "	<div class='table-function-list'>"
									+ "		<button class='btn-in-table btn-darkblue btn_update' title='修改' id='"+json_obj[i].seq_no+"'value='"+ json_obj[i].purchase_id + "' ><i class='fa fa-pencil'></i></button>"
									+ "		<button class='btn-in-table btn-alert btn_delete' title='刪除' id='"+json_obj[i].seq_no+"'value='"+ json_obj[i].purchase_id + "'><i class='fa fa-trash'></i></button>"
									+ "		<button class='btn-in-table btn-primary btn_detail' title='顯示明細' value='"+ json_obj[i].purchase_id + "'><i class='fa fa-list'></i></button>"
									+ "		<button class='btn-in-table btn-green btn_create' title='新增明細' value='"+ json_obj[i].purchase_id + "'><i class='fa fa-pencil-square-o'></i></button>"
									+ "	</div></div></td></tr>";	
								});
							}
							$("#purchases").dataTable().fnDestroy();
							if(resultRunTime!=0){
								$("#purchases_contain_row").show();
								$("#purchases tbody").html(result_table);
								$("#supply_name_err_mes").html("");
								$("#purchases").dataTable({
									  autoWidth: false,
									  scrollX:  true,
							          scrollY:"300px","language": {"url": "js/dataTables_zh-tw.txt","zeroRecords": "沒有符合的結果"}});
								$("#purchases").find("td").css("text-align", "center");
							}else{
								$("#purchases_contain_row").hide();
							}
						}
					});
					$(this).dialog("close");
				},
				"取消刪除" : function() {
					$(this).dialog("close");
				}
			}
		});
		$("#dialog-confirm").show();
		//新增Dialog相關設定
			insert_dialog = $("#dialog-form-insert").dialog(
			{
				draggable : false,//防止拖曳
				resizable : false,//防止縮放
				autoOpen : false,
				show : {
					effect : "blind",
					duration : 300
				},
				hide : {
					effect : "fade",
					duration : 300
				},
				width : 800,
				modal : true,
				buttons : [{
							id : "insert",
							text : "新增",
							click : function() {
								if ($('#insert-dialog-form-post').valid()) {
									$.ajax({
										type : "POST",
										url : "purchase.do",
										data : {
											action : "insert",
											memo : $("#dialog-form-insert input[name='memo']").val(),
											purchase_date : $("#dialog-form-insert input[name='purchase_date']").val(),
											invoice : $("#dialog-form-insert input[name='invoice']").val(),
											invoice_type : $("#insert_select_invoice_type option:selected").val(),
											supply_id : supply_id,
											supply_name : $("#dialog-form-insert input[name='supply_id']").val(),
											amount : $("#dialog-form-insert input[name='amount']").val()
										},
										success : function(result) {
											var json_obj = $.parseJSON(result);
											//判斷查詢結果
											var resultRunTime = 0;
											$.each (json_obj, function (i) {
												resultRunTime+=1;
											});
											var result_table = "";
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
													+ "<td><div class='table-row-func btn-in-table btn-gray' style='float:left;'><i class='fa fa-ellipsis-h'></i>"
													+ "	<div class='table-function-list'>"
													+ "		<button class='btn-in-table btn-darkblue btn_update' title='修改' id='"+json_obj[i].seq_no+"'value='"+ json_obj[i].purchase_id + "' ><i class='fa fa-pencil'></i></button>"
													+ "		<button class='btn-in-table btn-alert btn_delete' title='刪除' id='"+json_obj[i].seq_no+"'value='"+ json_obj[i].purchase_id + "'><i class='fa fa-trash'></i></button>"
													+ "		<button class='btn-in-table btn-primary btn_detail' title='顯示明細' value='"+ json_obj[i].purchase_id + "'><i class='fa fa-list'></i></button>"
													+ "		<button class='btn-in-table btn-green btn_create' title='新增明細' value='"+ json_obj[i].purchase_id + "'><i class='fa fa-pencil-square-o'></i></button>"
													+ "	</div></div></td></tr>";															
											});
											
											$("#purchases").dataTable().fnDestroy();
											if(resultRunTime!=0){
												$("#supply_name_err_mes").html("");
												$("#purchases_contain_row").show();
												$("#purchases tbody").html(result_table);
												$("#purchases").dataTable({
													  autoWidth: false,
													  scrollX:  true,
											          scrollY:"300px","language": {"url": "js/dataTables_zh-tw.txt","zeroRecords": "沒有符合的結果"}});
												$("#purchases").find("td").css("text-align", "center");
											}else{
												$("#purchases_contain_row").hide();
											}
										}
									});
									insert_dialog.dialog("close");
									$("#insert-dialog-form-post").trigger("reset");
								}
							}
						}, {
							text : "取消",
							click : function() {
								validator_insert.resetForm();
								$("#insert-dialog-form-post").trigger("reset");
								insert_dialog.dialog("close");
							}
						} ],
				close : function() {
					validator_insert.resetForm();
					$("#insert-dialog-form-post").trigger("reset");
				}
			});
			$("#dialog-form-insert").show();
		    //這邊是用供應商名稱去自動查詢，然後得到ID
	        $("#searh_purchase_by_supply_name").autocomplete({
	            minLength: 0,
	            source: function (request, response) {
	                $.ajax({
	                    url : "purchase.do",
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
	                              value: item.supply_name,
	                              supply_id: item.supply_id
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
	    	            $(this).attr("placeholder","輸入正確的供應商名稱!");
	    	        }
	    	    }     
	         });
      	$("#searh_purchase_by_supply_name").bind('focus', function(){ $(this).attr("placeholder","輸入供應商名稱以供查詢"); } );
      	$('#searh_purchase_by_supply_name').bind('autocompleteselect', function (e, ui) {
        	supply_id = ui.item.supply_id;
        });
		//修改Dialog相關設定
		update_dialog = $("#dialog-form-update").dialog({
			draggable : false,//防止拖曳
			resizable : false,//防止縮放
			autoOpen : false,
			width : 700,
			modal : true,
			show : {effect : "blind",duration : 300},
			hide : {effect : "fade",duration : 300},
			buttons : [{
				text : "修改",
				click : function() {
					if ($('#update-dialog-form-post').valid()) {
						$.ajax({
							type : "POST",
							url : "purchase.do",
							data : {
	 							action : "update",
	 							purchase_id : uuid,
	 							seq_no :seqNo,
								supply_id : supply_id,
								supply_name : $("#dialog-form-update input[name='supply_id']").val(),
								memo : $("#dialog-form-update input[name='memo']").val(),
								purchase_date : $("#dialog-form-update input[name='purchase_date']").val(),
								invoice : $("#dialog-form-update input[name='invoice']").val(),
								invoice_type : $("#update_select_invoice_type option:selected").val(),
								amount : $("#dialog-form-update input[name='amount']").val()
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
										+ "<td><div class='table-row-func btn-in-table btn-gray' style='float:left;'><i class='fa fa-ellipsis-h'></i>"
										+ "	<div class='table-function-list'>"
										+ "		<button class='btn-in-table btn-darkblue btn_update' title='修改' id='"+json_obj[i].seq_no+"'value='"+ json_obj[i].purchase_id + "' ><i class='fa fa-pencil'></i></button>"
										+ "		<button class='btn-in-table btn-alert btn_delete' title='刪除' id='"+json_obj[i].seq_no+"'value='"+ json_obj[i].purchase_id + "'><i class='fa fa-trash'></i></button>"
										+ "		<button class='btn-in-table btn-primary btn_detail' title='顯示明細' value='"+ json_obj[i].purchase_id + "'><i class='fa fa-list'></i></button>"
										+ "		<button class='btn-in-table btn-green btn_create' title='新增明細' value='"+ json_obj[i].purchase_id + "'><i class='fa fa-pencil-square-o'></i></button>"
										+ "	</div></div></td></tr>";									
									});
								}
								$("#purchases").dataTable().fnDestroy();
								if(resultRunTime!=0){
									$("#supply_name_err_mes").html("");
									$("#purchases_contain_row").show();
									$("#purchases tbody").html(result_table);
									$("#purchases").dataTable({
										  autoWidth: false,
										  scrollX:  true,
								          scrollY:"300px","language": {"url": "js/dataTables_zh-tw.txt","zeroRecords": "沒有符合的結果"}});
									$("#purchases").find("td").css("text-align", "center");
								}else{
									$("#purchases_contain_row").hide();
								}
							}
						});
						$("#update-dialog-form-post").trigger("reset");
						update_dialog.dialog("close");
					}
				}
			}, {
				text : "取消",
				click : function() {
					validator_update.resetForm();
					$("#update-dialog-form-post").trigger("reset");
					update_dialog.dialog("close");
				}
			} ],
			close : function() {
				$("#update-dialog-form-post").trigger("reset");
				validator_update.resetForm();
			}
		});
		$("#dialog-form-update").show();
		//修改事件聆聽
		$("#purchases").delegate(".btn_update", "click", function(e) {
			e.preventDefault();
			uuid = $(this).val();
			seqNo = $(this).attr("id");
			$.ajax({
				type : "POST",
				url : "purchase.do",
				data : {
					action : "search",
					supply_name : ""
				},
				success : function(result) {
					console.log(result);
						//TODOTODOTODO
						var json_obj = $.parseJSON(result);
						//判斷查詢結果
						var resultRunTime = 0;
						$.each (json_obj, function (i) {
							resultRunTime+=1;
						});
						var result_table = "";
						if(resultRunTime!=0){
							$.each(json_obj,function(i, item) {
								if(json_obj[i].purchase_id==uuid){
									$("#update_select_invoice_type").val(json_obj[i].invoice_type);
									$("#dialog-form-update input[name='memo']").val(json_obj[i].memo);
									$("#dialog-form-update input[name='invoice']").val(json_obj[i].invoice);
									$("#dialog-form-update input[name='amount']").val(json_obj[i].amount);
									$("#dialog-form-update input[name='purchase_date']").val(json_obj[i].purchase_date);							
								}
							});
						}
					}
				});			
			update_dialog.dialog("open");
		});
		//新增  廠商欄位autocomplete
        $("#insert_supply_id").autocomplete({
            minLength: 1,
            source: function (request, response) {
                $.ajax({
                    url : "purchase.do",
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
                    		supply_id = item.supply_id;
                            return {
                              label: item.supply_name,
                              value: item.supply_name,
                              supply_id : item.supply_id
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
    	            $(this).attr("placeholder","輸入正確的供應商名稱!");
    	        }
    	    }     
         });
  		$("#insert_supply_id").bind('focus', function(){ $(this).attr("placeholder","輸入供應商名稱以供查詢"); } );
	    $('#insert_supply_id').bind('autocompleteselect', function (e, ui) {
	    	supply_id = ui.item.supply_id;
	    }); 		
		//修改  autocomplete
        $("#update_supply_id").autocomplete({
            minLength: 1,
            source: function (request, response) {
                $.ajax({
                    url : "purchase.do",
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
                    		supply_id = item.supply_id;
                            return {
                              label: item.supply_name,
                              value: item.supply_name,
                              supply_id : item.supply_id
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
    	            $(this).attr("placeholder","輸入正確的供應商名稱!");
    	        }
    	    }     
         });
  		$("#update_supply_id").bind('focus', function(){ $(this).attr("placeholder","輸入供應商名稱以供查詢"); } );
	    $('#update_supply_id').bind('autocompleteselect', function (e, ui) {
	    	supply_id = ui.item.supply_id;
	    }); 
		//刪除事件聆聽 : 因為聆聽事件動態產生，所以採用delegate來批量處理，節省資源
		$("#purchases").delegate(".btn_delete", "click", function() {
			uuid = $(this).val();
			confirm_dialog.dialog("open");
		});
		//明細事件聆聽
		$("#purchases").delegate(".btn_detail", "click", function() {
			$.ajax({
				type : "POST",
				url : "purchase.do",
				data : {
						action : "select_all_purchasedetail",
						purchase_id : $(this).val()
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
							if(json_obj[i].c_product_id==null){
								json_obj[i].c_product_id="";
							}
							if(json_obj[i].product_name==null){
								json_obj[i].product_name="";
							}
							if(json_obj[i].quantity==null){
								json_obj[i].quantity="";
							}
							if(json_obj[i].cost==null){
								json_obj[i].cost="";
							}
							if(json_obj[i].memo==null){
								json_obj[i].memo="";
							}
								result_table 
								+= "<tr>"
								+ "<td name='"+ json_obj[i].c_product_id +"'>"+ json_obj[i].c_product_id+ "</td>"
								+ "<td name='"+ json_obj[i].product_name +"'>"+ json_obj[i].product_name+ "</td>"
								+ "<td name='"+ json_obj[i].quantity +"'>"+ json_obj[i].quantity+ "</td>"
								+ "<td name='"+ json_obj[i].cost +"'>"+ json_obj[i].cost+ "</td>"
								+ "<td name='"+ json_obj[i].memo +"'>"+ json_obj[i].memo+ "</td>"
								+ "<td><div class='table-row-func btn-in-table btn-gray'><i class='fa fa-ellipsis-h'></i>"
								+ "	<div class='table-function-list'>"
								+ "		<button class='btn-in-table btn-darkblue btn_update' title='修改' name='"+json_obj[i].product_id+"'id='"+json_obj[i].purchase_id+"'value='"+ json_obj[i].purchaseDetail_id + "' ><i class='fa fa-pencil'></i></button>"
								+ "		<button class='btn-in-table btn-alert btn_delete' title='刪除' id='"+json_obj[i].purchase_id+"'value='"+ json_obj[i].purchaseDetail_id + "'><i class='fa fa-trash'></i></button>"
								+ "	</div></div></td></tr>";									
						});
					}
					$("#purchase-detail-table").dataTable().fnDestroy();
					if(resultRunTime!=0){
						if($("#purchase_detail_err_mes").length){
            				$("#purchase_detail_err_mes").html("");
            			}
						
						$("#supply_name_err_mes").html("");
						$("#purchase-detail-table tbody").html(result_table);
						$("#purchase-detail-table").dataTable({
							  autoWidth: false,
							  scrollX:  true,
					          scrollY:"300px","language": {"url": "js/dataTables_zh-tw.txt","zeroRecords": "沒有符合的結果"}});
						$("#purchase-detail-table").find("td").css("text-align", "center");
						$("#purchase_detail_contain_row").show();
						
// 						$("#fake").show();
// 						$("#fake").animate({
// 							top: '+=600px',
// 							left: '-=1200px',
// 							width: '100%',
// 						    opacity: '1',
// 						},"slow");
// 						$("#fake").fadeOut(2000);
// 						$("#fake").css({"top":"10%","left":"80%","width":"1px","opacity":"0"});
					}else{
						if(!$("#purchase_detail_err_mes").length){
            				$("<p id='purchase_detail_err_mes'>查無明細</p>").appendTo($("#purchases-contain").parent());
            			}else{
            				$("#purchase_detail_err_mes").html("查無明細");
            			}
						$("#purchase_detail_contain_row").hide();
					}
				}
			});			
		});
		//明細新增Dialog相關設定
		detail_insert_dialog = $("#detail-dialog-form-insert").dialog(
		{
			draggable : false,//防止拖曳
			resizable : false,//防止縮放
			autoOpen : false,
			show : {
				effect : "blind",
				duration : 300
			},
			hide : {
				effect : "fade",
				duration : 300
			},
			width : 800,
			modal : true,
			buttons : [{
						text : "新增",
						click : function() {
							if ($('#detail-insert-dialog-form-post').valid()) {
								$.ajax({
									type : "POST",
									url : "purchase.do",
									data : {
										action : "insert_detail",
			 							purchase_id :purchase_id,
			 							product_id : product_id,
			 							c_product_id : c_product_id,
			 							product_name : product_name,
										memo : $("#detail-dialog-form-insert input[name='memo']").val(),
										quantity : $("#detail-dialog-form-insert input[name='quantity']").val(),
										cost : $("#detail-dialog-form-insert input[name='cost']").val()
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
												if(json_obj[i].c_product_id==null){
													json_obj[i].c_product_id="";
												}
												if(json_obj[i].product_name==null){
													json_obj[i].product_name="";
												}
												if(json_obj[i].quantity==null){
													json_obj[i].quantity="";
												}
												if(json_obj[i].cost==null){
													json_obj[i].cost="";
												}
												if(json_obj[i].memo==null){
													json_obj[i].memo="";
												}
												result_table 
												+= "<tr>"
												+ "<td name='"+ json_obj[i].c_product_id +"'>"+ json_obj[i].c_product_id+ "</td>"
												+ "<td name='"+ json_obj[i].product_name +"'>"+ json_obj[i].product_name+ "</td>"
												+ "<td name='"+ json_obj[i].quantity +"'>"+ json_obj[i].quantity+ "</td>"
												+ "<td name='"+ json_obj[i].cost +"'>"+ json_obj[i].cost+ "</td>"
												+ "<td name='"+ json_obj[i].memo +"'>"+ json_obj[i].memo+ "</td>"
												+ "<td><div class='table-row-func btn-in-table btn-gray'><i class='fa fa-ellipsis-h'></i>"
												+ "	<div class='table-function-list'>"
												+ "		<button class='btn-in-table btn-darkblue btn_update' title='修改' name='"+json_obj[i].product_id+"'id='"+json_obj[i].purchase_id+"'value='"+ json_obj[i].purchaseDetail_id + "' ><i class='fa fa-pencil'></i></button>"
												+ "		<button class='btn-in-table btn-alert btn_delete' title='刪除' id='"+json_obj[i].purchase_id+"'value='"+ json_obj[i].purchaseDetail_id + "'><i class='fa fa-trash'></i></button>"
												+ "	</div></div></td></tr>";										
											});
										}
										$("#purchase-detail-table").dataTable().fnDestroy();
										if(resultRunTime!=0){
											//$("#purchase_detail_contain_row").dialog("open");
											$("#purchase-detail-table tbody").html(result_table);
											$("#supply_name_err_mes").html("");
											$("#purchase-detail-table").dataTable({
												  autoWidth: false,
												  scrollX:  true,
										          scrollY:"300px","language": {"url": "js/dataTables_zh-tw.txt","zeroRecords": "沒有符合的結果"}});
											$("#purchase-detail-table").find("td").css("text-align", "center");
										}else{
											$("#purchase_detail_contain_row").hide();
										}								
									}
								});
								detail_insert_dialog.dialog("close");
								$("#detail-insert-dialog-form-post").trigger("reset");
							}
						}
					}, {
						text : "取消",
						click : function() {
							validator_detail_insert.resetForm();
							$("#detail-insert-dialog-form-post").trigger("reset");
							detail_insert_dialog.dialog("close");
						}
					} ],
			close : function() {
				validator_detail_insert.resetForm();
				$("#detail-insert-dialog-form-post").trigger("reset");
			}
		});
		$("#detail-dialog-form-insert").show();
		//明細新增事件聆聽 
		$("#purchases").delegate(".btn_create", "click", function() {
			purchase_id = $(this).val();
			detail_insert_dialog.dialog("open");
			new_or_edit=1;
		});		
		///明細確認Dialog相關設定(刪除功能)
		confirm_detail_dialog = $("#dialog-detail-confirm").dialog({
			draggable : false,//防止拖曳
			resizable : false,//防止縮放
			autoOpen : false,
			height : 140,
			modal : true,
			show : {effect : "blind",duration : 300},
			hide : {effect : "fade",duration : 300},
			buttons : {
				"確認刪除" : function() {
					$.ajax({
						type : "POST",
						url : "purchase.do",
						data : {
							action : "delete_detail",
							purchaseDetail_id : uuid,
							purchase_id : purchase_id
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
									if(json_obj[i].c_product_id==null){
										json_obj[i].c_product_id="";
									}
									if(json_obj[i].product_name==null){
										json_obj[i].product_name="";
									}
									if(json_obj[i].quantity==null){
										json_obj[i].quantity="";
									}
									if(json_obj[i].cost==null){
										json_obj[i].cost="";
									}
									if(json_obj[i].memo==null){
										json_obj[i].memo="";
									}
									result_table 
									+= "<tr>"
									+ "<td name='"+ json_obj[i].c_product_id +"'>"+ json_obj[i].c_product_id+ "</td>"
									+ "<td name='"+ json_obj[i].product_name +"'>"+ json_obj[i].product_name+ "</td>"
									+ "<td name='"+ json_obj[i].quantity +"'>"+ json_obj[i].quantity+ "</td>"
									+ "<td name='"+ json_obj[i].cost +"'>"+ json_obj[i].cost+ "</td>"
									+ "<td name='"+ json_obj[i].memo +"'>"+ json_obj[i].memo+ "</td>"
									+ "<td><div class='table-row-func btn-in-table btn-gray'><i class='fa fa-ellipsis-h'></i>"
									+ "	<div class='table-function-list'>"
									+ "		<button class='btn-in-table btn-darkblue btn_update' title='修改' name='"+json_obj[i].product_id+"'id='"+json_obj[i].purchase_id+"'value='"+ json_obj[i].purchaseDetail_id + "' ><i class='fa fa-pencil'></i></button>"
									+ "		<button class='btn-in-table btn-alert btn_delete' title='刪除' id='"+json_obj[i].purchase_id+"'value='"+ json_obj[i].purchaseDetail_id + "'><i class='fa fa-trash'></i></button>"
									+ "	</div></div></td></tr>";												
								});
							}
							$("#purchase-detail-table").dataTable().fnDestroy();
							if(resultRunTime!=0){
								$("#purchase_detail_contain_row").show();
								$("#supply_name_err_mes").html("");
								$("#purchase-detail-table tbody").html(result_table);
								$("#purchase-detail-table").dataTable({
									  autoWidth: false,
									  scrollX:  true,
							          scrollY:"300px","language": {"url": "js/dataTables_zh-tw.txt","zeroRecords": "沒有符合的結果"}});
								$("#purchase-detail-table").find("td").css("text-align", "center");
							}else{
								$("#purchase_detail_contain_row").hide();
							}
						}
					});
					$(this).dialog("close");
				},
				"取消刪除" : function() {
					$(this).dialog("close");
				}
			}
		});
		$("#dialog-detail-confirm").show();
		//明細刪除事件聆聽 
		
		$("#purchase-detail-table").delegate(".btn_delete", "click", function() {
			uuid = $(this).val();
			purchase_id= $(this).attr("id");
			confirm_detail_dialog.dialog("open");
		});
		//明細修改事件聆聽
		$("#purchase-detail-table").delegate(".btn_update", "click", function(e) {
			e.preventDefault();
			new_or_edit=2;
			uuid = $(this).val();
			purchase_id = $(this).attr("id");
			product_id = $(this).attr("name");
			$.ajax({
				type : "POST",
				url : "purchase.do",
				data : {
					action : "select_all_purchasedetail",
					purchase_id : purchase_id
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
								if(json_obj[i].purchaseDetail_id==uuid){
									$("#detail_dialog_form_update input[name='update_detail_c_product_id']").val(json_obj[i].c_product_id);
									$("#detail_dialog_form_update input[name='update_detail_product_name']").val(json_obj[i].product_name);
									$("#detail_dialog_form_update input[name='quantity']").val(json_obj[i].quantity);
									$("#detail_dialog_form_update input[name='cost']").val(json_obj[i].cost);
									$("#detail_dialog_form_update input[name='total']").val($("#detail_dialog_form_update input[name='cost']").val()*$("#detail_dialog_form_update input[name='quantity']").val());
									//???
									$("#detail_dialog_form_update input[name='memo']").val(json_obj[i].memo);
									$("#update_detail_c_product_id").autocomplete('search');
					                if(ui.items.length==1) {
					                    $(this).val(ui.items[0].value);
					            	}
								}
							}
						);
					}
				}
			});			
			detail_update_dialog.dialog("open");
		});	
		//修改detail update Dialog相關設定
		detail_update_dialog = $("#detail_dialog_form_update").dialog({
			draggable : false,//防止拖曳
			resizable : false,//防止縮放
			autoOpen : false,
			width : 700,
			modal : true,
			show : {effect : "blind",duration : 300},
			hide : {effect : "fade",duration : 300},
			buttons : [{
				text : "修改",
				click : function() {
					if ($('#detail-update-dialog-form-post').valid()) {
						$.ajax({
							type : "POST",
							url : "purchase.do",
							data : {
	 							action : "update_detail",
	 							purchaseDetail_id : uuid,
	 							purchase_id :purchase_id,
	 							product_id : product_id,
	 							c_product_id : $("#detail_dialog_form_update input[name='update_detail_c_product_id']").val(),
	 							product_name : $("#detail_dialog_form_update input[name='update_detail_product_name']").val(),
								memo : $("#detail_dialog_form_update input[name='memo']").val(),
								quantity : $("#detail_dialog_form_update input[name='quantity']").val(),
								cost : $("#detail_dialog_form_update input[name='cost']").val()
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
										if(json_obj[i].c_product_id==null){
											json_obj[i].c_product_id="";
										}
										if(json_obj[i].product_name==null){
											json_obj[i].product_name="";
										}
										if(json_obj[i].quantity==null){
											json_obj[i].quantity="";
										}
										if(json_obj[i].cost==null){
											json_obj[i].cost="";
										}
										if(json_obj[i].memo==null){
											json_obj[i].memo="";
										}
										result_table 
										+= "<tr>"
										+ "<td name='"+ json_obj[i].c_product_id +"'>"+ json_obj[i].c_product_id+ "</td>"
										+ "<td name='"+ json_obj[i].product_name +"'>"+ json_obj[i].product_name+ "</td>"
										+ "<td name='"+ json_obj[i].quantity +"'>"+ json_obj[i].quantity+ "</td>"
										+ "<td name='"+ json_obj[i].cost +"'>"+ json_obj[i].cost+ "</td>"
										+ "<td name='"+ json_obj[i].memo +"'>"+ json_obj[i].memo+ "</td>"
										+ "<td><div class='table-row-func btn-in-table btn-gray'><i class='fa fa-ellipsis-h'></i>"
										+ "	<div class='table-function-list'>"
										+ "		<button class='btn-in-table btn-darkblue btn_update' title='修改' name='"+json_obj[i].product_id+"'id='"+json_obj[i].purchase_id+"'value='"+ json_obj[i].purchaseDetail_id + "' ><i class='fa fa-pencil'></i></button>"
										+ "		<button class='btn-in-table btn-alert btn_delete' title='刪除' id='"+json_obj[i].purchase_id+"'value='"+ json_obj[i].purchaseDetail_id + "'><i class='fa fa-trash'></i></button>"
										+ "	</div></div></td></tr>";										
									});
								}
								$("#purchase-detail-table").dataTable().fnDestroy();
								if(resultRunTime!=0){
									$("#purchase_detail_contain_row").show();
									$("#purchase-detail-table tbody").html(result_table);
									$("#supply_name_err_mes").html("");
									$("#purchase-detail-table").dataTable({
										  autoWidth: false,
										  scrollX:  true,
								          scrollY:"300px","language": {"url": "js/dataTables_zh-tw.txt","zeroRecords": "沒有符合的結果"}});
									$("#purchase-detail-table").find("td").css("text-align", "center");
								}else{
									$("#purchase_detail_contain_row").hide();
								}								
							}
						});
						detail_update_dialog.dialog("close");
						$("#detail-update-dialog-form-post").trigger("reset");
					}
				}
			}, {
				text : "取消",
				click : function() {
					validator_detail_update.resetForm();
					$("#detail-update-dialog-form-post").trigger("reset");
					detail_update_dialog.dialog("close");
				}
			} ],
			close : function() {
				$("#detail-update-dialog-form-post").trigger("reset");
				validator_detail_update.resetForm();
			}
		});
		$("#detail_dialog_form_update").show();
		//detail update autocomplete
        $("#update_detail_c_product_id").autocomplete({
            minLength: 1,
            source: function (request, response) {
                $.ajax({
                    url : "purchase.do",
                    type : "POST",
                    cache : false,
                    delay : 1500,
                    data : {
                    	action : "search_product_data",
                        term : request.term,
                        identity : "ID"
                    },
                    success: function(data) {
                    	var json_obj = $.parseJSON(data);
                    	response($.map(json_obj, function (item) {
                            return {
	                        	 label: item.c_product_id,
	                             value: item.c_product_id,
	                             product_id: item.product_id,
	                             product_name: item.product_name,
	                             c_product_id: item.c_product_id,
	                             price: item.price,
	                             cost: item.cost
                            }
                          }))
                    }
                });
            },
            change: function(event, ui) {
            	//alert($(this).val());
    	        var source = $(this).val();
    	            var temp = $(".ui-autocomplete li").map(function () { return $(this).text()}).get();
    	        var found = $.inArray(source, temp);
    	        if(found < 0) {
    	            $(this).val('');
    	            $(this).attr("placeholder","輸入正確的自訂產品ID名稱!");
    	        }
    	    }     
         });
  		$("#update_detail_c_product_id").bind('focus', function(){ $(this).attr("placeholder","輸入自訂產品ID名稱以供查詢"); } );
	    $('#update_detail_c_product_id').bind('autocompleteselect', function (e, ui) {
	    	product_id = ui.item.product_id;
	    	product_name = ui.item.product_name;
	    	c_product_id = ui.item.c_product_id;
	    	$("#update_detail_product_name").val(ui.item.product_name);
	    	$("#update_detail_product_n").val("1");
	    	$("#update_detail_product_price").val(ui.item.cost);
	    	$("#update_detail_product_cost").val(ui.item.cost);
	    });
        $("#update_detail_product_name").autocomplete({
            minLength: 1,
            source: function (request, response) {
                $.ajax({
                    url : "purchase.do",
                    type : "POST",
                    cache : false,
                    delay : 1500,
                    data : {
                    	action : "search_product_data",
                        term : request.term,
                        identity : "NAME"
                    },
                    success: function(data) {
                    	var json_obj = $.parseJSON(data);
                    	response($.map(json_obj, function (item) {
                            return {
	                        	 label: item.product_name,
	                             value: item.product_name,
	                             product_id: item.product_id,
	                             product_name: item.product_name,
	                             c_product_id: item.c_product_id,
	                             price: item.price,
	                             cost: item.cost
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
    	            $(this).attr("placeholder","輸入正確的產品名稱!");
    	        }
    	    }     
         });
  		$("#update_detail_product_name").bind('focus', function(){ $(this).attr("placeholder","輸入產品名稱以供查詢"); } );
	    $('#update_detail_product_name').bind('autocompleteselect', function (e, ui) {
	    	product_id = ui.item.product_id;
	    	product_name = ui.item.product_name;
	    	c_product_id = ui.item.c_product_id;
	    	$("#update_detail_c_product_id").val(ui.item.c_product_id);
	    	$("#update_detail_product_n").val("1");
	    	$("#update_detail_product_price").val(ui.item.cost);
	    	$("#update_detail_product_cost").val(ui.item.cost);
	    });
		//detail insert autocomplete
        $("#insert_detail_c_product_id").autocomplete({
            minLength: 1,
            source: function (request, response) {
                $.ajax({
                    url : "purchase.do",
                    type : "POST",
                    cache : false,
                    delay : 1500,
                    data : {
                    	action : "search_product_data",
                        term : request.term,
                        identity : "ID"
                    },
                    success: function(data) {
                    	console.log(data);
                    	var json_obj = $.parseJSON(data);
                    	response($.map(json_obj, function (item) {
                            return {
	                        	 label: item.c_product_id,
	                             value: item.c_product_id,
	                             product_id: item.product_id,
	                             product_name: item.product_name,
	                             c_product_id: item.c_product_id,
	                             price: item.price,
	                             cost: item.cost
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
    	            $(this).attr("placeholder","輸入正確的自訂產品ID名稱!");
    	        }
    	    }     
         });
  		$("#insert_detail_c_product_id").bind('focus', function(){ $(this).attr("placeholder","輸入自訂產品ID名稱以供查詢"); } );
	    $('#insert_detail_c_product_id').bind('autocompleteselect', function (e, ui) {
	    	product_id = ui.item.product_id;
	    	product_name = ui.item.product_name;
	    	c_product_id = ui.item.c_product_id;
	    	$("#insert_detail_product_name").val(ui.item.product_name);
	    	$("#insert_detail_product_n").val("1");
	    	$("#insert_detail_product_price").val(ui.item.cost);
	    	$("#insert_detail_product_cost").val(ui.item.cost);
	    });
        $("#insert_detail_product_name").autocomplete({
            minLength: 1,
            source: function (request, response) {
                $.ajax({
                    url : "purchase.do",
                    type : "POST",
                    cache : false,
                    delay : 1500,
                    data : {
                    	action : "search_product_data",
                        term : request.term,
                        identity : "NAME"
                    },
                    success: function(data) {
                    	var json_obj = $.parseJSON(data);
                    	response($.map(json_obj, function (item) {
                            return {
	                        	 label: item.product_name,
	                             value: item.product_name,
	                             product_id: item.product_id,
	                             product_name: item.product_name,
	                             c_product_id: item.c_product_id,
	                             price: item.price,
	                             cost: item.cost
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
    	            $(this).attr("placeholder","輸入正確的產品名稱!");
    	        }
    	    }
         });
  		$("#insert_detail_product_name").bind('focus', function(){ $(this).attr("placeholder","輸入產品名稱以供查詢"); } );
	    $('#insert_detail_product_name').bind('autocompleteselect', function (e, ui) {
	    	product_id = ui.item.product_id;
	    	product_name = ui.item.product_name;
	    	c_product_id = ui.item.c_product_id;
	    	$("#insert_detail_c_product_id").val(ui.item.c_product_id);
	    	$("#insert_detail_product_n").val("1");
	    	$("#insert_detail_product_price").val(ui.item.cost);
	    	$("#insert_detail_product_cost").val(ui.item.cost);
	    });
		//新增事件聆聽
		$("#create-supply").click( function(e) {
			e.preventDefault();		
			insert_dialog.dialog("open");
			//@@@
		});
		
		$("#insert_detail_product_n").change(function(e){
			$("#insert_detail_product_cost").val($("#insert_detail_product_n").val()*$("#insert_detail_product_price").val());
		});
		
		$("#update_detail_product_n").change(function(e){
			$("#update_detail_product_cost").val($("#update_detail_product_n").val()*$("#update_detail_product_price").val());
		});
		
		$("#insert_detail_product_price").change(function(e){
			$("#insert_detail_product_cost").val($("#insert_detail_product_n").val()*$("#insert_detail_product_price").val());
		});
		
		$("#update_detail_product_price").change(function(e){
			$("#update_detail_product_cost").val($("#update_detail_product_n").val()*$("#update_detail_product_price").val());
		});

		$("#warning").dialog({
			title: "警告",
			draggable : false,//防止拖曳
			resizable : false,//防止縮放
			autoOpen : false,
			height : "auto",
			modal : true,
			show : {effect : "bounce",duration : 1000},
			hide : {effect : "fade",duration : 300},
			buttons : {
				"確認" : function() {$(this).dialog("close");}
			}
		});
		//日期設定
// 		$(".date").datepicker({
// 			dayNamesMin:["日","一","二","三","四","五","六"],
// 			monthNames:["一月","二月","三月","四月","五月","六月","七月","八月","九月","十月","十一月","十二月"],
// 			dateFormat:"yy-mm-dd",
// 			changeYear:true
// 		});
		//hold header
// 		$("#purchases").find("th").css("min-width","120px");
// 		$("#purchase-detail-table").find("th").css("min-width","120px");
	});
</script>
	<div class="input-field-wrap">
			<div class="form-wrap">
				<div class="form-row">
					<label for="">
						<span class="block-label">供應商名稱查詢</span>
						<input type="text" id="searh_purchase_by_supply_name" name="searh_purchase_by_supply_name">
					</label>
					<button class="btn btn-darkblue" id="searh_supply_name">查詢</button>
				</div>
				<div class="form-row">
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
					<button class="btn btn-darkblue" id="search_purchase_date">查詢</button>
				</form>
				</div>
				<div class="btn-row">
					<button class="btn btn-exec " id="create-supply">新增進貨資料</button>
				</div>
				<div id="supply_name_err_mes"></div>
			</div><!-- /.form-wrap -->
		</div><!-- /.input-field-wrap -->
		
		
		<div class="datalistWrap">
			<!--對話窗樣式-確認 -->
			<div id="dialog-confirm" title="確認刪除資料嗎?" style="display:none;">
			</div>
			<!--對話窗樣式-確認 -->
			<div id="dialog-detail-confirm" title="確認刪除資料嗎?" style="display:none;">
			</div>		
			<!--對話窗樣式-修改 -->
			<div id="dialog-form-update" title="修改進貨資料" style="display:none;">
				<form name="update-dialog-form-post" id="update-dialog-form-post">
					<fieldset>
						<table style="border-collapse: separate;border-spacing: 10px 20px;">
							<tr>
<!-- 								<td><p>名稱</p></td> -->
<!-- 								<td><input type="text" name="supply_id" id="update_supply_id" placeholder="輸入供應商名稱已供查詢"></td> -->
								<td><p>進貨發票號碼</p></td>
								<td><input type="text" name="invoice"  placeholder="輸入進貨發票號碼"></td>
								<td><p>發票金額</p></td>
								<td><input type="text" name="amount"  placeholder="輸入發票金額"></td>
							</tr>
							<tr>
								<td><p>進貨日期</p></td>
								<td><input type="text" name="purchase_date"  placeholder="輸入進貨日期" class="input-date"></td>
								<td><p>發票樣式</p></td>
								<td><select name="invoice_type" id="update_select_invoice_type"><option value="0">選擇</option><option value="1">二聯式發票</option><option value="2">三聯式發票</option></select></td>
							</tr>
							<tr>
								<td><p>備註說明</p></td>
								<td><input type="text" name="memo" placeholder="輸入備註說明"></td>
								<td></td>
								<td></td>
							</tr>
						</table>
						
					</fieldset>
				</form>
			</div>
			<!--對話窗樣式-detail-修改 -->
			<div id="detail_dialog_form_update" title="修改明細資料" style="display:none;">
				<form name="detail-update-dialog-form-post" id="detail-update-dialog-form-post">
					<font color=red style="padding-left:26px">掃條碼亦可取得商品資料</font>
					<fieldset>
						<table style="border-collapse: separate;border-spacing: 10px 20px;">
							<tr>
								<td><p>自訂產品名稱</p></td>
								<td><input type="text" id="update_detail_c_product_id"name="update_detail_c_product_id"  placeholder="輸入自訂產品ID"></td>
								<td><p>產品名稱</p></td>
								<td><input type="text" id="update_detail_product_name"name="update_detail_product_name" placeholder="輸入產品名稱"></td>
							</tr>
							<tr>
								<td><p>進貨數量</p></td>
								<td><input type="text" id="update_detail_product_n" name="quantity"  placeholder="輸入進貨數量"></td>
								<td><p>單價</p></td>
								<td><input type="text" id="update_detail_product_price" name="cost" placeholder="輸入單價"></td>
							</tr>
							<tr>
								<td><p>總價格</p></td>
								<td><input type="text" id="update_detail_product_cost" name="total" disabled></td>
								<td><p>備註說明</p></td>
								<td><input type="text" name="memo"  placeholder="輸入備註說明"></td>
							</tr>
						</table>
					</fieldset>
				</form>
			</div>						
			<!--對話窗樣式-新增 -->
			<div id="dialog-form-insert" title="新增進貨資料" style="display:none;">
				<form name="insert-dialog-form-post" id="insert-dialog-form-post">
					<fieldset>
						<table style="border-collapse: separate;border-spacing: 10px 20px;">
							<tr>
								<td><p>進貨單號</p></td>
								<td><input id="title-dialog-post"type="text" name="original_seq_no" disabled="disabled" value="系統自動產生"></td>
								<td><p>供應商名稱</p></td>
								<td><input type="text" name="supply_id" id="insert_supply_id" placeholder="輸入供應商名稱已供查詢"></td>
							</tr>
							<tr>
								<td><p>進貨發票號碼</p></td>
								<td><input type="text" name="invoice" placeholder="輸入進貨發票號碼"></td>
								<td><p>發票金額</p></td>
								<td><input type="text" name="amount" placeholder="輸入發票金額"></td>
							</tr>
							<tr>
								<td><p>進貨日期</p></td>
								<td><input type="text" name="purchase_date" placeholder="輸入進貨日期" class="input-date"></td>
								<td><p>發票樣式</p></td>
								<td><select name="invoice_type" id="insert_select_invoice_type"><option value="0">選擇</option><option value="1">二聯式發票</option><option value="2">三聯式發票</option></select></td>
							</tr>
							<tr>
								<td><p>備註說明</p></td>
								<td><input type="text" name="memo" placeholder="輸入備註說明"></td>
							</tr>
						</table>
					</fieldset>
				</form>
			</div>
			<!--對話窗樣式-明細新增 -->
			<div id="detail-dialog-form-insert" title="新增明細資料" style="display:none;">
				<form name="detail-insert-dialog-form-post" id="detail-insert-dialog-form-post"style="display:inline">
					<fieldset>
						<font color=red style="padding-left:26px">掃條碼亦可取得商品資料</font>
						<table style="border-collapse: separate;border-spacing: 10px 20px;">
							<tr>
								<td><p>自訂產品ID名稱</p></td>
								<td><input type="text" id="insert_detail_c_product_id"name="insert_detail_c_product_id"  placeholder="輸入自訂產品ID名稱以供查詢"></td>
								<td><p>產品名稱</p></td>
								<td><input type="text" id="insert_detail_product_name"name="insert_detail_product_name" placeholder="輸入產品名稱以供查詢"></td>
							</tr>
							<tr>
								<td><p>進貨數量</p></td>
								<td><input type="text" id="insert_detail_product_n" name="quantity" placeholder="輸入進貨數量"></td>
								<td><p>單價</p></td>
								<td><input type="text" id="insert_detail_product_price" name="cost"placeholder="輸入單價"></td>
							</tr>
							<tr>
								<td><p>總價格</p></td>
								<td><input type="text" id="insert_detail_product_cost" name="total" disabled></td>
								<td><p>備註說明</p></td>
								<td><input type="text" name="memo"  placeholder="輸入備註說明"></td>
							</tr>
						</table>
					</fieldset>
				</form>
			</div>			
			<!-- 第一列 -->
<!-- 			<div class="row" align="center"> -->
<!-- 				<div id="purchase-serah-create-contain" class="ui-widget"> -->
<!-- 					<table id="purchases-serah-create"> -->
<!-- 						<thead> -->
<!-- 							<tr> -->
<!-- 								<td> -->
<!-- 									<input type="text" id="searh_purchase_by_supply_name" name="searh_purchase_by_supply_name" placeholder="輸入供應商名稱已供查詢"> -->
<!-- 								</td> -->
<!-- 								<td> -->
<!-- 									&nbsp;&nbsp;<button id="searh_supply_name">查詢</button> -->
<!-- 								</td> -->
<!-- 							</tr> -->
<!-- 						</thead> -->
<!-- 					</table> -->
<!-- 				</div> -->
<!-- 			</div> -->
			<!-- 第二列 -->
<!-- 			<div class="row" align="center"> -->
<!-- 				<div id="select-dates-contain" class="ui-widget"> -->
<!-- 					<form id="purchase_date_form" name="purchase_date_form"> -->
<!-- 						<table> -->
<!-- 							<thead> -->
<!-- 								<tr> -->
<!-- 									<td><input type="text" id="purchase_start_date" name="purchase_start_date" class="input-date" placeholder="輸入進貨日期起日"></td> -->
<!-- 									<td>&nbsp;&nbsp;~&nbsp;&nbsp;</td> -->
<!-- 									<td><input type="text" id="purchase_end_date" name="purchase_end_date"class="input-date" placeholder="輸入進貨日期迄日"></td> -->
<!-- 									<td>&nbsp;&nbsp;<button id="search_purchase_date">查詢</button></td> -->
<!-- 								</tr> -->
<!-- 							</thead> -->
<!-- 						</table> -->
<!-- 					</form> -->
<!-- 				</div> -->
<!-- 			</div> -->
			<!-- 第三列 -->
<!-- 			<div class="row" align="center"> -->
<!-- 				<button id="create-supply">新增進貨資料</button> -->
<!-- 			</div> -->
			<!-- 第四列 -->
			<div class="row search-result-wrap" align="center" id ="purchases_contain_row" style="margin-bottom:0px;display:none;">
				<div id="purchases-contain" class="result-table-wrap">
					<table id="purchases" class="result-table">
						<thead>
							<tr class="ui-widget-header">
								<th>進貨單號</th>
								<th>進貨日期</th>
								<th>進貨發票號碼</th>
								<th>發票樣式</th>
								<th>進貨發票金額</th>
								<th>備註說明</th>
								<th style="min-width:75px;">功能</th>
							</tr>
						</thead>
						<tbody>
						</tbody>
					</table>
				</div>
			</div>
			<!-- 第五列 -->
			<div class="row search-result-wrap" align="center" id="purchase_detail_contain_row" style="display:none;">
				<div id="purchase-detail-contain" class="ui-widget result-table-wrap">
					<table id="purchase-detail-table" class="ui-widget ui-widget-content result-table">
						<thead>
							<tr class="ui-widget-header">
								<th>自訂產品ID</th>
								<th>產品名稱</th>
								<th>進貨數量</th>
								<th>進貨價格</th>
								<th>備註說明 </th>
								<th>功能</th>
							</tr>
						</thead>
						<tbody>
						</tbody>
					</table>
				</div>
			</div>
<!-- 			<table id="fake" class="result-table" style="position:absolute;top:10%;left:80%;width:1px;opacity:0;display:none;"> -->
<!-- 				<thead> -->
<!-- 					<tr> -->
<!-- 						<th>自訂產品ID</th><th>產品名稱</th><th>進貨數量</th><th>進貨價格</th><th>備註說明 </th><th>功能</th> -->
<!-- 					</tr> -->
<!-- 				</thead> -->
<!-- 				<tbody> -->
<!-- 					<tr><td></td><td></td><td></td><td></td><td></td><td></td></tr> -->
<!-- 					<tr><td></td><td></td><td></td><td></td><td></td><td></td></tr> -->
<!-- 					<tr><td></td><td></td><td></td><td></td><td></td><td></td></tr> -->
<!-- 					<tr><td></td><td></td><td></td><td></td><td></td><td></td></tr> -->
<!-- 				</tbody> -->
<!-- 			</table>			 -->
		</div>
	</div>
<div id="warning"></div>
</body>
</html>