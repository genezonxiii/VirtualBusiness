<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ page import="tw.com.aber.sale.controller.*"%>
<%@ page import="java.util.*"%>
<%@ page import="java.sql.DriverManager"%>
<%@ page import="java.sql.Connection"%>
<%@ page import="java.sql.Statement"%>
<%@ page import="java.sql.ResultSet"%>
<jsp:directive.page import="java.sql.SQLException" />
<!DOCTYPE html>
<html>
<head>
<title>銷貨管理</title>
<meta charset="utf-8">
<link rel="Shortcut Icon" type="image/x-icon" href="./images/Rockettheme-Ecommerce-Shop.ico" />
<link href="<c:url value="css/css.css" />" rel="stylesheet">
<link href="<c:url value="css/jquery.dataTables.min.css" />" rel="stylesheet">
<link href="<c:url value="css/1.11.4/jquery-ui.css" />" rel="stylesheet">
<link rel="stylesheet" href="css/1.11.4/jquery-ui.css">

<link rel="stylesheet" href="css/styles.css">
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
<script type="text/javascript" src="js/jquery.scannerdetection.js"></script>
<script>
function draw_sale(parameter){
	$("#sales_contain_row").css({"opacity":"0"});
	warning_msg("---讀取中請稍候---");
	$.ajax({
		type : "POST",
		url : "sale.do",
		data : parameter,
		success : function(result) {
				console.log(result);
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
							if(json_obj[i].sale_date==null){
								json_obj[i].sale_date="";
							}
							if(json_obj[i].order_source==null){
								json_obj[i].order_source="";
							}
							if(json_obj[i].memo==null){
								json_obj[i].memo="";
							}
							result_table 
							+= "<tr cus='"+json_obj[i].customer_id+"'>"
							+ "<td name='"+ json_obj[i].seq_no +"'>"+ json_obj[i].seq_no+ "</td>"
							+ "<td name='"+ json_obj[i].order_no +"'>"+ json_obj[i].order_no+ "</td>"
							+ "<td name='"+ json_obj[i].product_name +"'>"+ json_obj[i].product_name+ "</td>"
							+ "<td name='"+ json_obj[i].c_product_id +"'>"+ json_obj[i].c_product_id+ "</td>"
							+ "<td name='"+ json_obj[i].quantity +"'>"+ json_obj[i].quantity+ "</td>"
							+ "<td name='"+ json_obj[i].price +"'>"+ money(json_obj[i].price)+ "</td>"
							+ "<td name='"+ json_obj[i].invoice +"'>"+ json_obj[i].invoice+ "</td>"
							+ "<td name='"+ json_obj[i].invoice_date +"'>"+ json_obj[i].invoice_date+ "</td>"
							+ "<td name='"+ json_obj[i].trans_list_date +"'>"+ json_obj[i].trans_list_date+ "</td>"
//								+ "<td name='"+ json_obj[i].dis_date +"'>"+ json_obj[i].dis_date+ "</td>"
							+ "<td name='"+ json_obj[i].sale_date +"'>"+ json_obj[i].sale_date+ "</td>"
							+ "<td name='"+ json_obj[i].order_source +"'>"+ json_obj[i].order_source+ "</td>"
							+ "<td name='"+ json_obj[i].memo +"'>"+ json_obj[i].memo+ "</td>"
							+ "<td><div class='table-row-func btn-in-table btn-gray'><i class='fa fa-ellipsis-h'></i>"
							+ "	<div class='table-function-list'>"
							+ "		<button class='btn-in-table btn-darkblue btn_update' title='修改' id='"+json_obj[i].seq_no+"'value='"+ json_obj[i].sale_id+"'name='"+ json_obj[i].c_product_id+"' ><i class='fa fa-pencil'></i></button>"
							+ "		<button class='btn-in-table btn-alert btn_delete' title='刪除' id='"+json_obj[i].seq_no+"'value='"+ json_obj[i].sale_id+"'name='"+ json_obj[i].c_product_id+"' val2='"+json_obj[i].order_no+"'><i class='fa fa-trash'></i></button>"
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
				          scrollY:"300px","language": {"url": "js/dataTables_zh-tw.txt"}});
					$("#sales").find("td").css("text-align", "center");
					$("#sales_contain_row").animate({"opacity":"0.01"},1);
					$("#sales_contain_row").animate({"opacity":"1"},300);
					warning_msg("");
					//$("#sales").find("th").css({"word-break":"break-all","min-width":"40px","text-align":"center" });
					//$("#sales").find("td").css({"word-break":"break-all","min-width":"40px","text-align":"center" });
// 					if($("#trans_list_date_err_mes").length){
//         				$("#trans_list_date_err_mes").remove();
//         			}
				}
				
				if(resultRunTime<2){
					$("#sales_contain_row").hide();
					warning_msg("---查無此結果---");
// 					if(!$("#trans_list_date_err_mes").length){
//         				$("<p id='trans_list_date_err_mes'>查無此結果</p>").appendTo($("#trans_list_date_form").parent());
//         			}else{
//         				$("#trans_list_date_err_mes").html("查無此結果");
//         			}
				}
				if(json_obj[resultRunTime-1].message=="如要以日期查詢，請完整填寫起日欄位"){
					$("#sales_contain_row").hide();
					warning_msg("---如要以日期查詢，請完整填寫起日欄位---");
// 					if(!$("#trans_list_date_err_mes").length){
//         				$("<p id='trans_list_date_err_mes'>如要以日期查詢，請完整填寫起日欄位</p>").appendTo($("#trans_list_date_form").parent());
//         			}else{
//         				$("#trans_list_date_err_mes").html("如要以日期查詢，請完整填寫起日欄位");
//         			}
				}
				if(json_obj[resultRunTime-1].message=="如要以日期查詢，請完整填寫訖日欄位"){
					$("#sales_contain_row").hide();
					warning_msg("---如要以日期查詢，請完整填寫訖日欄位---");
// 					if(!$("#trans_list_date_err_mes").length){
//         				$("<p id='trans_list_date_err_mes'>如要以日期查詢，請完整填寫訖日欄位</p>").appendTo($("#trans_list_date_form").parent());
//         			}else{
//         				$("#trans_list_date_err_mes").html("如要以日期查詢，請完整填寫訖日欄位");
//         			}
				}						
				if(json_obj[resultRunTime-1].message=="起日不可大於訖日"){
					$("#sales_contain_row").hide();
					warning_msg("---起日不可大於訖日---");
// 					if(!$("#trans_list_date_err_mes").length){
//         				$("<p id='trans_list_date_err_mes'>起日不可大於訖日</p>").appendTo($("#trans_list_date_form").parent());
//         			}else{
//         				$("#trans_list_date_err_mes").html("起日不可大於訖日");
//         			}
				}							
				
			}
		});
}




	var scan_exist=0,new_or_edit=0;
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
		            	var result_table = "";
						$.each(json_obj,function(i, item) {
							if(new_or_edit==1){
								//new_or_edit=3;
								$("#insert_product_name").val(json_obj[i].product_name);
								$("#insert_c_product_id").val(json_obj[i].c_product_id);
								$("#insert_quantity").val(json_obj[i].keep_stock);
								$("#insert_price").val(json_obj[i].cost);
								$("#insert_product_price").val(json_obj[i].cost);
							}
							if(new_or_edit==2){
								//new_or_edit=3;
								$("#update_product_name").val(json_obj[i].product_name);
								$("#update_c_product_id").val(json_obj[i].c_product_id);
								$("#update_quantity").val(json_obj[i].keep_stock);
								$("#update_price").val(json_obj[i].cost);
								$("#update_product_price").val(json_obj[i].cost);
							}
						});
						if(json_obj.length==0){
							$("#warning").html("<h3>該條碼無產品存在</h3>請至'商品管理'介面&nbsp;定義該條碼。");
							$("#warning").dialog("open");
						}
		            }
	    		});
	        })
	        .bind('scannerDetectionError',function(e,data){console.log('detection error '+data.string);})
	        .bind('scannerDetectionReceive',function(e,data){console.log(data);});
	    $(window).scannerDetection('success');
	});
	$(function() {
		$(".bdyplane").animate({"opacity":"1"});
		var uuid = "";
		var c_product_id="";
		var product_id="";
		var seqNo = "";
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
		 $("#trans_list_date_form").validate({ rules : { trans_list_staet_date : { dateISO : true }, trans_list_end_date:{ dateISO : true } }, messages:{ trans_list_staet_date : { dateISO : "日期格式錯誤" }, trans_list_end_date : { dateISO : "日期格式錯誤" } } }); 
		 $("#trans_dis_date_form").validate({ rules : { trans_dis_staet_date : { dateISO : true }, trans_dis_end_date:{ dateISO : true } }, messages:{ trans_dis_staet_date : { dateISO : "日期格式錯誤" }, trans_dis_end_date : { dateISO : "日期格式錯誤" } } }); 
		 var validator_insert = $("#insert-dialog-form-post").validate({ rules : { order_no : { stringMaxLength : 20 }, product_name:{ maxlength : 80, required : true }, c_product_id : { stringMaxLength : 40 }, name:{ stringMaxLength : 80 }, quantity : { required : true, digits :true }, price : { required : true, number :true, min: 1 }, invoice : { stringMaxLength : 12, alnum :true }, invoice_date : { dateISO : true }, trans_list_date : { dateISO : true }, dis_date : { dateISO : true }, memo : { stringMaxLength : 200 }, sale_date : { required : true, dateISO : true }, order_source : { stringMaxLength : 30 } } }); 
		var validator_update = $("#update-dialog-form-post").validate({ rules : { order_no : { stringMaxLength : 20 }, product_name:{ maxlength : 80, required : true }, c_product_id : { stringMaxLength : 40 }, name:{ stringMaxLength : 80 }, quantity : { required : true, digits :true }, price : { required : true, number :true, min: 1 }, invoice : { stringMaxLength : 12, alnum :true }, invoice_date : { dateISO : true }, trans_list_date : { dateISO : true }, dis_date : { dateISO : true }, memo : { stringMaxLength : 200 }, sale_date : { required : true, dateISO : true }, order_source : { stringMaxLength : 30 } } }); 
		//自訂產品ID查詢相關設定
		$("#searh-sale").click(function(e) {
			e.preventDefault();
			var tmp={
					action : "search",
					c_product_id : $("input[name='searh_c_product_id'").val(),
					trans_list_start_date: $("#trans_list_start_date").val(),
					trans_list_end_date: $("#trans_list_end_date").val()
				};
			draw_sale(tmp);
// 			$.ajax({
// 					type : "POST",
// 					url : "sale.do",
// 					data : {
// 						action : "search",
// 						c_product_id : $("input[name='searh_c_product_id'").val(),
// 						trans_list_start_date: $("#trans_list_start_date").val(),
// 						trans_list_end_date: $("#trans_list_end_date").val()
// 					},
// 					success : function(result) {
// 							var json_obj = $.parseJSON(result);
// 							var len=json_obj.length;
// 							//判斷查詢結果
// 							var resultRunTime = 0;
// 							$.each (json_obj, function (i) {
// 								resultRunTime+=1;
// 							});
// 							if(json_obj[resultRunTime-1].message=="驗證通過"){
// 								var result_table = "";
// 								$.each(json_obj,function(i, item) {
// 									if(i<len-1){
// 										if(json_obj[i].seq_no==null){
// 											json_obj[i].seq_no="";
// 										}
// 										if(json_obj[i].order_no==null){
// 											json_obj[i].order_no="";
// 										}
// 										if(json_obj[i].product_name==null){
// 											json_obj[i].product_name="";
// 										}
// 										if(json_obj[i].c_product_id==null){
// 											json_obj[i].c_product_id="";
// 										}
// 										if(json_obj[i].quantity==null){
// 											json_obj[i].quantity="";
// 										}
// 										if(json_obj[i].price==null){
// 											json_obj[i].price="";
// 										}
// 										if(json_obj[i].invoice==null){
// 											json_obj[i].invoice="";
// 										}
// 										if(json_obj[i].invoice_date==null){
// 											json_obj[i].invoice_date="";
// 										}
// 										if(json_obj[i].trans_list_date==null){
// 											json_obj[i].trans_list_date="";
// 										}
// 										if(json_obj[i].dis_date==null){
// 											json_obj[i].dis_date="";
// 										}
// 										if(json_obj[i].sale_date==null){
// 											json_obj[i].sale_date="";
// 										}
// 										if(json_obj[i].order_source==null){
// 											json_obj[i].order_source="";
// 										}
// 										if(json_obj[i].memo==null){
// 											json_obj[i].memo="";
// 										}
// 										result_table 
// 										+= "<tr>"
// 										+ "<td name='"+ json_obj[i].seq_no +"'>"+ json_obj[i].seq_no+ "</td>"
// 										+ "<td name='"+ json_obj[i].order_no +"'>"+ json_obj[i].order_no+ "</td>"
// 										+ "<td name='"+ json_obj[i].product_name +"'>"+ json_obj[i].product_name+ "</td>"
// 										+ "<td name='"+ json_obj[i].c_product_id +"'>"+ json_obj[i].c_product_id+ "</td>"
// 										+ "<td name='"+ json_obj[i].quantity +"'>"+ json_obj[i].quantity+ "</td>"
// 										+ "<td name='"+ json_obj[i].price +"'>"+ money(json_obj[i].price)+ "</td>"
// 										+ "<td name='"+ json_obj[i].invoice +"'>"+ json_obj[i].invoice+ "</td>"
// 										+ "<td name='"+ json_obj[i].invoice_date +"'>"+ json_obj[i].invoice_date+ "</td>"
// 										+ "<td name='"+ json_obj[i].trans_list_date +"'>"+ json_obj[i].trans_list_date+ "</td>"
// // 										+ "<td name='"+ json_obj[i].dis_date +"'>"+ json_obj[i].dis_date+ "</td>"
// 										+ "<td name='"+ json_obj[i].sale_date +"'>"+ json_obj[i].sale_date+ "</td>"
// 										+ "<td name='"+ json_obj[i].order_source +"'>"+ json_obj[i].order_source+ "</td>"
// 										+ "<td name='"+ json_obj[i].memo +"'>"+ json_obj[i].memo+ "</td>"
// 										+"<td><div class='table-row-func btn-in-table btn-gray'><i class='fa fa-ellipsis-h'></i>"
// 										+"	<div class='table-function-list'>"
// 										+"		<button class='btn-in-table btn-darkblue btn_update' title='修改' id='"+json_obj[i].seq_no+"'value='"+ json_obj[i].sale_id+"'name='"+ json_obj[i].c_product_id+"' ><i class='fa fa-pencil' ></i></button>"
// 										+"		<button class='btn-in-table btn-alert btn_delete' title='刪除' id='"+json_obj[i].seq_no+"'value='"+ json_obj[i].sale_id+"'name='"+ json_obj[i].c_product_id+"'><i class='fa fa-trash'></i></button>"
// 										+"	</div>"
// 										+"</div></td></tr>";
// 									}
// 								});
// 							}					
// 							alert(result);
// 							alert(resultRunTime);
// 							if(resultRunTime==0){
// 								$("#sales_contain_row").hide();
// 								if(!$("#search_sale_err_mes").length){
// 	                				$("<p id='search_sale_err_mes'>查無此結果").appendTo($("#sales-serah-create").parent());
// 	                			}else{
// 	                				$("#search_sale_err_mes").html("查無此結果");
// 	                			}
// 							}
// 							$("#sales").dataTable().fnDestroy();
// 							if(resultRunTime!=0&&json_obj[resultRunTime-1].message=="驗證通過"){
// 								$("#sales_contain_row").show();
// 								$("#sales tbody").html(result_table);
// 								$("#sales").dataTable({
// 									  autoWidth: false,
// 									  scrollX:  true,
// 							         scrollY:"300px","language": {"url": "js/dataTables_zh-tw.txt"}});
// 								$("#sales").addClass("result-table");
// 								$("#sales").find("td").css("text-align", "center");
// 								//$("#sales").find("td").css("max-width", "80px");
// 								//$("#sales").find("th").css({"word-break":"break-all","min-width":"40px","text-align":"center" });
// 								//$("#sales").find("td").css({"word-break":"break-all","min-width":"40px","text-align":"center" });
// 								if($("#search_sale_err_mes").length){
// 	                				$("#search_sale_err_mes").remove();
// 	                			}
// 							}
// 						}
// 					});
		});
		//轉單日查詢相關設定
		$("#searh-trans-list-date").click(function(e) {
			e.preventDefault();
			if($("#trans_list_date_form").valid()){
				var tmp={
						action : "search_trans_list_date",
						trans_list_start_date: $("#trans_list_start_date").val(),
						trans_list_end_date: $("#trans_list_end_date").val()
					};
				draw_sale(tmp);
// 				$.ajax({
// 					type : "POST",
// 					url : "sale.do",
// 					data : {
// 						action : "search_trans_list_date",
// 						trans_list_start_date: $("#trans_list_start_date").val(),
// 						trans_list_end_date: $("#trans_list_end_date").val()
// 					},
// 					success : function(result) {
// 							var json_obj = $.parseJSON(result);
// 							var len=json_obj.length;
// 							//判斷查詢結果
// 							var resultRunTime = 0;
// 							$.each (json_obj, function (i) {
// 								resultRunTime+=1;
// 							});
// 							if(json_obj[resultRunTime-1].message=="驗證通過"){
// 								var result_table = "";
// 								$.each(json_obj,function(i, item) {
// 									if(i<len-1){
// 										if(json_obj[i].seq_no==null){
// 											json_obj[i].seq_no="";
// 										}
// 										if(json_obj[i].order_no==null){
// 											json_obj[i].order_no="";
// 										}
// 										if(json_obj[i].product_name==null){
// 											json_obj[i].product_name="";
// 										}
// 										if(json_obj[i].c_product_id==null){
// 											json_obj[i].c_product_id="";
// 										}
// 										if(json_obj[i].quantity==null){
// 											json_obj[i].quantity="";
// 										}
// 										if(json_obj[i].price==null){
// 											json_obj[i].price="";
// 										}
// 										if(json_obj[i].invoice==null){
// 											json_obj[i].invoice="";
// 										}
// 										if(json_obj[i].invoice_date==null){
// 											json_obj[i].invoice_date="";
// 										}
// 										if(json_obj[i].trans_list_date==null){
// 											json_obj[i].trans_list_date="";
// 										}
// 										if(json_obj[i].dis_date==null){
// 											json_obj[i].dis_date="";
// 										}
// 										if(json_obj[i].sale_date==null){
// 											json_obj[i].sale_date="";
// 										}
// 										if(json_obj[i].order_source==null){
// 											json_obj[i].order_source="";
// 										}
// 										if(json_obj[i].memo==null){
// 											json_obj[i].memo="";
// 										}
// 										result_table 
// 										+= "<tr>"
// 										+ "<td name='"+ json_obj[i].seq_no +"'>"+ json_obj[i].seq_no+ "</td>"
// 										+ "<td name='"+ json_obj[i].order_no +"'>"+ json_obj[i].order_no+ "</td>"
// 										+ "<td name='"+ json_obj[i].product_name +"'>"+ json_obj[i].product_name+ "</td>"
// 										+ "<td name='"+ json_obj[i].c_product_id +"'>"+ json_obj[i].c_product_id+ "</td>"
// 										+ "<td name='"+ json_obj[i].quantity +"'>"+ json_obj[i].quantity+ "</td>"
// 										+ "<td name='"+ json_obj[i].price +"'>"+ money(json_obj[i].price)+ "</td>"
// 										+ "<td name='"+ json_obj[i].invoice +"'>"+ json_obj[i].invoice+ "</td>"
// 										+ "<td name='"+ json_obj[i].invoice_date +"'>"+ json_obj[i].invoice_date+ "</td>"
// 										+ "<td name='"+ json_obj[i].trans_list_date +"'>"+ json_obj[i].trans_list_date+ "</td>"
// // 										+ "<td name='"+ json_obj[i].dis_date +"'>"+ json_obj[i].dis_date+ "</td>"
// 										+ "<td name='"+ json_obj[i].sale_date +"'>"+ json_obj[i].sale_date+ "</td>"
// 										+ "<td name='"+ json_obj[i].order_source +"'>"+ json_obj[i].order_source+ "</td>"
// 										+ "<td name='"+ json_obj[i].memo +"'>"+ json_obj[i].memo+ "</td>"
// 										+ "<td><div class='table-row-func btn-in-table btn-gray'><i class='fa fa-ellipsis-h'></i>"
// 										+ "	<div class='table-function-list'>"
// 										+ "		<button class='btn-in-table btn-darkblue btn_update' title='修改' id='"+json_obj[i].seq_no+"'value='"+ json_obj[i].sale_id+"'name='"+ json_obj[i].c_product_id+"' ><i class='fa fa-pencil'></i></button>"
// 										+ "		<button class='btn-in-table btn-alert btn_delete' title='刪除' id='"+json_obj[i].seq_no+"'value='"+ json_obj[i].sale_id+"'name='"+ json_obj[i].c_product_id+"'><i class='fa fa-trash'></i></button>"
// 										+ "	</div>"
// 										+ "</div></td></tr>";
// 									}
// 								});
// 							}
// 							if(json_obj[resultRunTime-1].message=="如要以日期查詢，請完整填寫起日欄位"){
// 								$("#sales_contain_row").hide();
// 								if(!$("#trans_list_date_err_mes").length){
// 	                				$("<p id='trans_list_date_err_mes'>如要以日期查詢，請完整填寫起日欄位").appendTo($("#trans_list_date_form").parent());
// 	                			}else{
// 	                				$("#trans_list_date_err_mes").html("如要以日期查詢，請完整填寫起日欄位");
// 	                			}
// 							}
// 							if(json_obj[resultRunTime-1].message=="如要以日期查詢，請完整填寫訖日欄位"){
// 								$("#sales_contain_row").hide();
// 								if(!$("#trans_list_date_err_mes").length){
// 	                				$("<p id='trans_list_date_err_mes'>如要以日期查詢，請完整填寫訖日欄位").appendTo($("#trans_list_date_form").parent());
// 	                			}else{
// 	                				$("#trans_list_date_err_mes").html("如要以日期查詢，請完整填寫訖日欄位");
// 	                			}
// 							}						
// 							if(json_obj[resultRunTime-1].message=="起日不可大於訖日"){
// 								$("#sales_contain_row").hide();
// 								if(!$("#trans_list_date_err_mes").length){
// 	                				$("<p id='trans_list_date_err_mes'>起日不可大於訖日").appendTo($("#trans_list_date_form").parent());
// 	                			}else{
// 	                				$("#trans_list_date_err_mes").html("起日不可大於訖日");
// 	                			}
// 							}							
// 							if(resultRunTime==0){
// 								$("#sales_contain_row").hide();
// 								if(!$("#trans_list_date_err_mes").length){
// 	                				$("<p id='trans_list_date_err_mes'>查無此結果").appendTo($("#trans_list_date_form").parent());
// 	                			}else{
// 	                				$("#trans_list_date_err_mes").html("查無此結果");
// 	                			}
// 							}
// 							$("#sales").dataTable().fnDestroy();
// 							if(resultRunTime!=0&&json_obj[resultRunTime-1].message=="驗證通過"){
// 								$("#sales_contain_row").show();
// 								$("#sales tbody").html(result_table);
// 								$("#sales").dataTable({
// 									  autoWidth: false,
// 									  scrollX:  true,
// 							          scrollY:"300px","language": {"url": "js/dataTables_zh-tw.txt"}});
// 								$("#sales").find("td").css("text-align", "center");
// 								//$("#sales").find("th").css({"word-break":"break-all","min-width":"40px","text-align":"center" });
// 								//$("#sales").find("td").css({"word-break":"break-all","min-width":"40px","text-align":"center" });
// 								if($("#trans_list_date_err_mes").length){
// 	                				$("#trans_list_date_err_mes").remove();
// 	                			}
// 							}
// 						}
// 					});
			}
		});
		//配送日查詢相關設定
		$("#searh-dis-date").click(function(e) {
			e.preventDefault();
			if($("#trans_dis_date_form").valid()){
				var tmp={
					action : "search_dis_date",
					dis_start_date: $("#dis_start_date").val(),
					dis_end_date: $("#dis_end_date").val()
				};
				draw_sale(tmp);
// 				$.ajax({
// 					type : "POST",
// 					url : "sale.do",
// 					data : {
// 						action : "search_dis_date",
// 						dis_start_date: $("#dis_start_date").val(),
// 						dis_end_date: $("#dis_end_date").val()
// 					},
// 					success : function(result) {
// 							var json_obj = $.parseJSON(result);
// 							var len=json_obj.length;
// 							//判斷查詢結果
// 							var resultRunTime = 0;
// 							$.each (json_obj, function (i) {
// 								resultRunTime+=1;
// 							});
// 							if(json_obj[resultRunTime-1].message=="驗證通過"){
// 								var result_table = "";
// 								$.each(json_obj,function(i, item) {
// 									if(i<len-1){
// 										if(json_obj[i].seq_no==null){
// 											json_obj[i].seq_no="";
// 										}
// 										if(json_obj[i].order_no==null){
// 											json_obj[i].order_no="";
// 										}
// 										if(json_obj[i].product_name==null){
// 											json_obj[i].product_name="";
// 										}
// 										if(json_obj[i].c_product_id==null){
// 											json_obj[i].c_product_id="";
// 										}
// 										if(json_obj[i].quantity==null){
// 											json_obj[i].quantity="";
// 										}
// 										if(json_obj[i].price==null){
// 											json_obj[i].price="";
// 										}
// 										if(json_obj[i].invoice==null){
// 											json_obj[i].invoice="";
// 										}
// 										if(json_obj[i].invoice_date==null){
// 											json_obj[i].invoice_date="";
// 										}
// 										if(json_obj[i].trans_list_date==null){
// 											json_obj[i].trans_list_date="";
// 										}
// 										if(json_obj[i].dis_date==null){
// 											json_obj[i].dis_date="";
// 										}
// 										if(json_obj[i].sale_date==null){
// 											json_obj[i].sale_date="";
// 										}
// 										if(json_obj[i].order_source==null){
// 											json_obj[i].order_source="";
// 										}
// 										if(json_obj[i].memo==null){
// 											json_obj[i].memo="";
// 										}
// 										result_table 
// 										+= "<tr>"
// 										+ "<td name='"+ json_obj[i].seq_no +"'>"+ json_obj[i].seq_no+ "</td>"
// 										+ "<td name='"+ json_obj[i].order_no +"'>"+ json_obj[i].order_no+ "</td>"
// 										+ "<td name='"+ json_obj[i].product_name +"'>"+ json_obj[i].product_name+ "</td>"
// 										+ "<td name='"+ json_obj[i].c_product_id +"'>"+ json_obj[i].c_product_id+ "</td>"
// 										+ "<td name='"+ json_obj[i].quantity +"'>"+ json_obj[i].quantity+ "</td>"
// 										+ "<td name='"+ json_obj[i].price +"'>"+ money(json_obj[i].price)+ "</td>"
// 										+ "<td name='"+ json_obj[i].invoice +"'>"+ json_obj[i].invoice+ "</td>"
// 										+ "<td name='"+ json_obj[i].invoice_date +"'>"+ json_obj[i].invoice_date+ "</td>"
// 										+ "<td name='"+ json_obj[i].trans_list_date +"'>"+ json_obj[i].trans_list_date+ "</td>"
// // 										+ "<td name='"+ json_obj[i].dis_date +"'>"+ json_obj[i].dis_date+ "</td>"
// 										+ "<td name='"+ json_obj[i].sale_date +"'>"+ json_obj[i].sale_date+ "</td>"
// 										+ "<td name='"+ json_obj[i].order_source +"'>"+ json_obj[i].order_source+ "</td>"
// 										+ "<td name='"+ json_obj[i].memo +"'>"+ json_obj[i].memo+ "</td>"
// 										+ "<td><div class='table-row-func btn-in-table btn-gray'><i class='fa fa-ellipsis-h'></i>"
// 										+ "	<div class='table-function-list'>"
// 										+ "		<button class='btn-in-table btn-darkblue btn_update' title='修改' id='"+json_obj[i].seq_no+"'value='"+ json_obj[i].sale_id+"'name='"+ json_obj[i].c_product_id+"' ><i class='fa fa-pencil'></i></button>"
// 										+ "		<button class='btn-in-table btn-alert btn_delete' title='刪除' id='"+json_obj[i].seq_no+"'value='"+ json_obj[i].sale_id+"'name='"+ json_obj[i].c_product_id+"'><i class='fa fa-trash'></i></button>"
// 										+ "	</div></div></td></tr>";
// 									}
// 								});
// 							}
// 							if(json_obj[resultRunTime-1].message=="如要以日期查詢，請完整填寫起日欄位"){
// 								$("#sales_contain_row").hide();
// 								if(!$("#trans_dis_date_err_mes").length){
// 	                				$("<p id='trans_dis_date_err_mes'>如要以日期查詢，請完整填寫起日欄位").appendTo($("#trans_dis_date_form").parent());
// 	                			}else{
// 	                				$("#trans_dis_date_err_mes").html("如要以日期查詢，請完整填寫起日欄位");
// 	                			}
// 							}
// 							if(json_obj[resultRunTime-1].message=="如要以日期查詢，請完整填寫訖日欄位"){
// 								$("#sales_contain_row").hide();
// 								if(!$("#trans_dis_date_err_mes").length){
// 	                				$("<p id='trans_dis_date_err_mes'>如要以日期查詢，請完整填寫訖日欄位").appendTo($("#trans_dis_date_form").parent());
// 	                			}else{
// 	                				$("#trans_dis_date_err_mes").html("如要以日期查詢，請完整填寫訖日欄位");
// 	                			}
// 							}						
// 							if(json_obj[resultRunTime-1].message=="起日不可大於訖日"){
// 								$("#sales_contain_row").hide();
// 								if(!$("#trans_dis_date_err_mes").length){
// 	                				$("<p id='trans_dis_date_err_mes'>起日不可大於訖日").appendTo($("#trans_dis_date_form").parent());
// 	                			}else{
// 	                				$("#trans_dis_date_err_mes").html("起日不可大於訖日");
// 	                			}
// 							}							
// 							if(resultRunTime==0){
// 								$("#sales_contain_row").hide();
// 								if(!$("#trans_dis_date_err_mes").length){
// 	                				$("<p id='trans_dis_date_err_mes'>查無此結果").appendTo($("#trans_dis_date_form").parent());
// 	                			}else{
// 	                				$("#trans_dis_date_err_mes").html("查無此結果");
// 	                			}
// 							}
// 							$("#sales").dataTable().fnDestroy();
// 							if(resultRunTime!=0&&json_obj[resultRunTime-1].message=="驗證通過"){
// 								$("#sales_contain_row").show();
// 								$("#sales tbody").html(result_table);
// 								$("#sales").dataTable({
// 									  autoWidth: false,
// 									  scrollX:  true,
// 							          scrollY:"300px","language": {"url": "js/dataTables_zh-tw.txt"}});
// 								$("#sales").find("td").css("text-align", "center");
// 								//$("#sales").find("th").css({"word-break":"break-all","min-width":"40px","text-align":"center" });
// 								//$("#sales").find("td").css({"word-break":"break-all","min-width":"40px","text-align":"center" });
// 								if($("#trans_dis_date_err_mes").length){
// 	                				$("#trans_dis_date_err_mes").remove();
// 	                			}
// 							}
// 						}
// 					});
			}
		});		
		//新增Dialog相關設定
		insert_dialog = $("#dialog-form-insert").dialog({
			draggable : false, resizable : false, autoOpen : false,
			height : "auto", width : "auto", modal : true,
			show : {effect : "blind",duration : 300},
			hide : {effect : "fade",duration : 300},
			buttons : [{
						id : "insert",
						text : "新增",
						click : function() {
							if ($('#insert-dialog-form-post').valid()) {
								var tmp={
										action : "insert",
										order_no : $("#dialog-form-insert input[name='order_no']").val(),
										product_name : $("#dialog-form-insert input[name='product_name']").val(),
										product_id : product_id,
										c_product_id : $("#dialog-form-insert input[name='c_product_id']").val(),
										name : $("#dialog-form-insert input[name='name']").val(),
										quantity : $("#dialog-form-insert input[name='quantity']").val(),
										price : $("#dialog-form-insert input[name='price']").val(),
										invoice : $("#dialog-form-insert input[name='invoice']").val(),
										invoice_date : $("#dialog-form-insert input[name='invoice_date']").val(),
										trans_list_date : $("#dialog-form-insert input[name='trans_list_date']").val(),
										dis_date : $("#dialog-form-insert input[name='dis_date']").val(),
										memo : $("#dialog-form-insert input[name='memo']").val(),
										sale_date : $("#dialog-form-insert input[name='sale_date']").val(),
										order_source : $("#dialog-form-insert input[name='order_source']").val()
									};
								draw_sale(tmp);
// 												$.ajax({
// 													type : "POST",
// 													url : "sale.do",
// 													data : {
// 														action : "insert",
// 														order_no : $("#dialog-form-insert input[name='order_no']").val(),
// 														product_name : $("#dialog-form-insert input[name='product_name']").val(),
// 														product_id : product_id,
// 														c_product_id : $("#dialog-form-insert input[name='c_product_id']").val(),
// 														name : $("#dialog-form-insert input[name='name']").val(),
// 														quantity : $("#dialog-form-insert input[name='quantity']").val(),
// 														price : $("#dialog-form-insert input[name='price']").val(),
// 														invoice : $("#dialog-form-insert input[name='invoice']").val(),
// 														invoice_date : $("#dialog-form-insert input[name='invoice_date']").val(),
// 														trans_list_date : $("#dialog-form-insert input[name='trans_list_date']").val(),
// 														dis_date : $("#dialog-form-insert input[name='dis_date']").val(),
// 														memo : $("#dialog-form-insert input[name='memo']").val(),
// 														sale_date : $("#dialog-form-insert input[name='sale_date']").val(),
// 														order_source : $("#dialog-form-insert input[name='order_source']").val()
// 													},
// 													success : function(result) {
// 														var json_obj = $.parseJSON(result);
// 														var len=json_obj.length;
// 														//判斷查詢結果
// 														var resultRunTime = 0;
// 														$.each (json_obj, function (i) {
// 															resultRunTime+=1;
// 														});
// 														var result_table = "";
// 														$.each(json_obj,function(i, item) {
// 															if(i<len-1){
// 																if(json_obj[i].seq_no==null){
// 																	json_obj[i].seq_no="";
// 																}
// 																if(json_obj[i].order_no==null){
// 																	json_obj[i].order_no="";
// 																}
// 																if(json_obj[i].product_name==null){
// 																	json_obj[i].product_name="";
// 																}
// 																if(json_obj[i].c_product_id==null){
// 																	json_obj[i].c_product_id="";
// 																}
// 																if(json_obj[i].quantity==null){
// 																	json_obj[i].quantity="";
// 																}
// 																if(json_obj[i].price==null){
// 																	json_obj[i].price="";
// 																}
// 																if(json_obj[i].invoice==null){
// 																	json_obj[i].invoice="";
// 																}
// 																if(json_obj[i].invoice_date==null){
// 																	json_obj[i].invoice_date="";
// 																}
// 																if(json_obj[i].trans_list_date==null){
// 																	json_obj[i].trans_list_date="";
// 																}
// 																if(json_obj[i].dis_date==null){
// 																	json_obj[i].dis_date="";
// 																}
// 																if(json_obj[i].sale_date==null){
// 																	json_obj[i].sale_date="";
// 																}
// 																if(json_obj[i].order_source==null){
// 																	json_obj[i].order_source="";
// 																}
// 																if(json_obj[i].memo==null){
// 																	json_obj[i].memo="";
// 																}
// 																result_table 
// 																+= "<tr>"
// 																+ "<td name='"+ json_obj[i].seq_no +"'>"+ json_obj[i].seq_no+ "</td>"
// 																+ "<td name='"+ json_obj[i].order_no +"'>"+ json_obj[i].order_no+ "</td>"
// 																+ "<td name='"+ json_obj[i].product_name +"'>"+ json_obj[i].product_name+ "</td>"
// 																+ "<td name='"+ json_obj[i].c_product_id +"'>"+ json_obj[i].c_product_id+ "</td>"
// 																+ "<td name='"+ json_obj[i].quantity +"'>"+ json_obj[i].quantity+ "</td>"
// 																+ "<td name='"+ json_obj[i].price +"'>"+ money(json_obj[i].price)+ "</td>"
// 																+ "<td name='"+ json_obj[i].invoice +"'>"+ json_obj[i].invoice+ "</td>"
// 																+ "<td name='"+ json_obj[i].invoice_date +"'>"+ json_obj[i].invoice_date+ "</td>"
// 																+ "<td name='"+ json_obj[i].trans_list_date +"'>"+ json_obj[i].trans_list_date+ "</td>"
// // 																+ "<td name='"+ json_obj[i].dis_date +"'>"+ json_obj[i].dis_date+ "</td>"
// 																+ "<td name='"+ json_obj[i].sale_date +"'>"+ json_obj[i].sale_date+ "</td>"
// 																+ "<td name='"+ json_obj[i].order_source +"'>"+ json_obj[i].order_source+ "</td>"
// 																+ "<td name='"+ json_obj[i].memo +"'>"+ json_obj[i].memo+ "</td>"
// 																+ "<td><div class='table-row-func btn-in-table btn-gray'><i class='fa fa-ellipsis-h'></i>"
// 																+ "	<div class='table-function-list'>"
// 																+ "		<button class='btn-in-table btn-darkblue btn_update' title='修改' id='"+json_obj[i].seq_no+"'value='"+ json_obj[i].sale_id+"'name='"+ json_obj[i].c_product_id+"' ><i class='fa fa-pencil'></i></button>"
// 																+ "		<button class='btn-in-table btn-alert btn_delete' title='刪除' id='"+json_obj[i].seq_no+"'value='"+ json_obj[i].sale_id+"'name='"+ json_obj[i].c_product_id+"'><i class='fa fa-trash'></i></button>"
// 																+ "	</div></div></td></tr>";																
// 															}
// 														});
// 														$("#sales").dataTable().fnDestroy();
// 														if(resultRunTime!=0){
// 															$("#sales_contain_row").show();
// 															$("#sales tbody").html(result_table);
// 															$("#sales").dataTable({
// 																  autoWidth: false,
// 																  scrollX:  true,
// 														          scrollY:"300px","language": {"url": "js/dataTables_zh-tw.txt"}});
// 															$("#sales").find("td").css("text-align", "center");
// 															//$("#sales").find("th").css({"word-break":"break-all","min-width":"40px","text-align":"center" });
// 															//$("#sales").find("td").css({"word-break":"break-all","min-width":"40px","text-align":"center" });
// 														}else{
// 															$("#sales_contain_row").hide();
// 														}
// 													}
// 												});
								insert_dialog.dialog("close");
								$("#insert-dialog-form-post").trigger("reset");
							}
						}
					}, {
						text : "取消",
						click : function() {
							$("#insert-dialog-form-post").trigger("reset");
							validator_insert.resetForm();
							insert_dialog.dialog("close");
						}
					} ],
			close : function() {
				validator_insert.resetForm();
				$("#insert-dialog-form-post").trigger("reset");
			}
		});
		$("#dialog-form-insert").show();
		//確認Dialog相關設定(刪除功能)
		confirm_dialog = $("#dialog-confirm").dialog({
			draggable : false, resizable : false, autoOpen : false,
			height : "auto", width : "auto", modal : true,
			show : {effect : "blind",duration : 300},
			hide : {effect : "fade",duration : 300},
			buttons : {
				"確認刪除" : function() {
					var tmp={
						action : "delete",
						sale_id : uuid,
						c_product_id : c_product_id//c_product_id是為了刪除後，回傳指定的結果，所需參數
					};
					draw_sale(tmp);
// 					$.ajax({
// 						type : "POST",
// 						url : "sale.do",
// 						data : {
// 							action : "delete",
// 							sale_id : uuid,
// 							c_product_id : c_product_id//c_product_id是為了刪除後，回傳指定的結果，所需參數
// 						},
// 						success : function(result) {
// 							var json_obj = $.parseJSON(result);
// 							var len=json_obj.length;
// 							//判斷查詢結果
// 							var resultRunTime = 0;
// 							$.each (json_obj, function (i) {
// 								resultRunTime+=1;
// 							});
// 							var result_table = "";
// 							$.each(json_obj,function(i, item) {
// 								if(i<len-1){
// 									if(json_obj[i].seq_no==null){
// 										json_obj[i].seq_no="";
// 									}
// 									if(json_obj[i].order_no==null){
// 										json_obj[i].order_no="";
// 									}
// 									if(json_obj[i].product_name==null){
// 										json_obj[i].product_name="";
// 									}
// 									if(json_obj[i].c_product_id==null){
// 										json_obj[i].c_product_id="";
// 									}
// 									if(json_obj[i].quantity==null){
// 										json_obj[i].quantity="";
// 									}
// 									if(json_obj[i].price==null){
// 										json_obj[i].price="";
// 									}
// 									if(json_obj[i].invoice==null){
// 										json_obj[i].invoice="";
// 									}
// 									if(json_obj[i].invoice_date==null){
// 										json_obj[i].invoice_date="";
// 									}
// 									if(json_obj[i].trans_list_date==null){
// 										json_obj[i].trans_list_date="";
// 									}
// 									if(json_obj[i].dis_date==null){
// 										json_obj[i].dis_date="";
// 									}
// 									if(json_obj[i].sale_date==null){
// 										json_obj[i].sale_date="";
// 									}
// 									if(json_obj[i].order_source==null){
// 										json_obj[i].order_source="";
// 									}
// 									if(json_obj[i].memo==null){
// 										json_obj[i].memo="";
// 									}
// 									result_table 
// 									+= "<tr>"
// 									+ "<td name='"+ json_obj[i].seq_no +"'>"+ json_obj[i].seq_no+ "</td>"
// 									+ "<td name='"+ json_obj[i].order_no +"'>"+ json_obj[i].order_no+ "</td>"
// 									+ "<td name='"+ json_obj[i].product_name +"'>"+ json_obj[i].product_name+ "</td>"
// 									+ "<td name='"+ json_obj[i].c_product_id +"'>"+ json_obj[i].c_product_id+ "</td>"
// 									+ "<td name='"+ json_obj[i].quantity +"'>"+ json_obj[i].quantity+ "</td>"
// 									+ "<td name='"+ json_obj[i].price +"'>"+ money(json_obj[i].price)+ "</td>"
// 									+ "<td name='"+ json_obj[i].invoice +"'>"+ json_obj[i].invoice+ "</td>"
// 									+ "<td name='"+ json_obj[i].invoice_date +"'>"+ json_obj[i].invoice_date+ "</td>"
// 									+ "<td name='"+ json_obj[i].trans_list_date +"'>"+ json_obj[i].trans_list_date+ "</td>"
// // 									+ "<td name='"+ json_obj[i].dis_date +"'>"+ json_obj[i].dis_date+ "</td>"
// 									+ "<td name='"+ json_obj[i].sale_date +"'>"+ json_obj[i].sale_date+ "</td>"
// 									+ "<td name='"+ json_obj[i].order_source +"'>"+ json_obj[i].order_source+ "</td>"
// 									+ "<td name='"+ json_obj[i].memo +"'>"+ json_obj[i].memo+ "</td>"
// 									+ "<td><div class='table-row-func btn-in-table btn-gray'><i class='fa fa-ellipsis-h'></i>"
// 									+ "	<div class='table-function-list'>"
// 									+ "		<button class='btn-in-table btn-darkblue btn_update' title='修改' id='"+json_obj[i].seq_no+"'value='"+ json_obj[i].sale_id+"'name='"+ json_obj[i].c_product_id+"' ><i class='fa fa-pencil'></i></button>"
// 									+ "		<button class='btn-in-table btn-alert btn_delete' title='刪除' id='"+json_obj[i].seq_no+"'value='"+ json_obj[i].sale_id+"'name='"+ json_obj[i].c_product_id+"'><i class='fa fa-trash'></i></button>"
// 									+ "	</div></div></td></tr>";											
// 								}
// 							});
// 							$("#sales").dataTable().fnDestroy();
// 							if(resultRunTime!=0){
// 								$("#sales_contain_row").show();
// 								$("#sales tbody").html(result_table);
// 								$("#sales").dataTable({
// 									  autoWidth: false,
// 									  scrollX:  true,
// 							          scrollY:"300px","language": {"url": "js/dataTables_zh-tw.txt"}});
// 								$("#sales").find("td").css("text-align", "center");
// 								//$("#sales").find("th").css({"word-break":"break-all","min-width":"40px","text-align":"center" });
// 								//$("#sales").find("td").css({"word-break":"break-all","min-width":"40px","text-align":"center" });
// 							}else{
// 								$("#sales_contain_row").hide();
// 							}
// 						}
// 					});
					$(this).dialog("close");
				},
				"取消刪除" : function() {
					$(this).dialog("close");
				}
			}
		});
		$("#dialog-confirm").show();
		//修改Dialog相關設定
		update_dialog = $("#dialog-form-update").dialog({
			draggable : false, resizable : false, autoOpen : false,
			height : "auto", width : "auto", modal : true,
			show : {effect : "blind",duration : 300},
			hide : {effect : "fade",duration : 300},
			buttons : [{
				text : "修改",
				click : function() {
					if ($('#update-dialog-form-post').valid()) {
						var tmp={
	 							action : "update",
	 							sale_id : uuid,
	 							seq_no :seqNo,
								order_no : $("#dialog-form-update input[name='order_no']").val(),
								product_name : $("#dialog-form-update input[name='product_name']").val(),
								product_id : product_id,
								c_product_id : $("#dialog-form-update input[name='c_product_id']").val(),
								name : $("#dialog-form-update input[name='name']").val(),
								quantity : $("#dialog-form-update input[name='quantity']").val(),
								price : $("#dialog-form-update input[name='price']").val(),
								invoice : $("#dialog-form-update input[name='invoice']").val(),
								invoice_date : $("#dialog-form-update input[name='invoice_date']").val(),
								trans_list_date : $("#dialog-form-update input[name='trans_list_date']").val(),
								dis_date : $("#dialog-form-update input[name='dis_date']").val(),
								memo : $("#dialog-form-update input[name='memo']").val(),
								sale_date : $("#dialog-form-update input[name='sale_date']").val(),
								order_source : $("#dialog-form-update input[name='order_source']").val()
							};
							draw_sale(tmp);
// 						$.ajax({
// 							type : "POST",
// 							url : "sale.do",
// 							data : {
// 	 							action : "update",
// 	 							sale_id : uuid,
// 	 							seq_no :seqNo,
// 								order_no : $("#dialog-form-update input[name='order_no']").val(),
// 								product_name : $("#dialog-form-update input[name='product_name']").val(),
// 								product_id : product_id,
// 								c_product_id : $("#dialog-form-update input[name='c_product_id']").val(),
// 								name : $("#dialog-form-update input[name='name']").val(),
// 								quantity : $("#dialog-form-update input[name='quantity']").val(),
// 								price : $("#dialog-form-update input[name='price']").val(),
// 								invoice : $("#dialog-form-update input[name='invoice']").val(),
// 								invoice_date : $("#dialog-form-update input[name='invoice_date']").val(),
// 								trans_list_date : $("#dialog-form-update input[name='trans_list_date']").val(),
// 								dis_date : $("#dialog-form-update input[name='dis_date']").val(),
// 								memo : $("#dialog-form-update input[name='memo']").val(),
// 								sale_date : $("#dialog-form-update input[name='sale_date']").val(),
// 								order_source : $("#dialog-form-update input[name='order_source']").val()
// 							},
// 							success : function(result) {
// 								var json_obj = $.parseJSON(result);
// 								var len=json_obj.length;
// 								//判斷查詢結果
// 								var resultRunTime = 0;
// 								$.each (json_obj, function (i) {
// 									resultRunTime+=1;
// 								});
// 								var result_table = "";
// 								if(resultRunTime!=0){
// 									$.each(json_obj,function(i, item) {
// 										if(i<len-1){
// 											if(json_obj[i].seq_no==null){
// 												json_obj[i].seq_no="";
// 											}
// 											if(json_obj[i].order_no==null){
// 												json_obj[i].order_no="";
// 											}
// 											if(json_obj[i].product_name==null){
// 												json_obj[i].product_name="";
// 											}
// 											if(json_obj[i].c_product_id==null){
// 												json_obj[i].c_product_id="";
// 											}
// 											if(json_obj[i].quantity==null){
// 												json_obj[i].quantity="";
// 											}
// 											if(json_obj[i].price==null){
// 												json_obj[i].price="";
// 											}
// 											if(json_obj[i].invoice==null){
// 												json_obj[i].invoice="";
// 											}
// 											if(json_obj[i].invoice_date==null){
// 												json_obj[i].invoice_date="";
// 											}
// 											if(json_obj[i].trans_list_date==null){
// 												json_obj[i].trans_list_date="";
// 											}
// 											if(json_obj[i].dis_date==null){
// 												json_obj[i].dis_date="";
// 											}
// 											if(json_obj[i].sale_date==null){
// 												json_obj[i].sale_date="";
// 											}
// 											if(json_obj[i].order_source==null){
// 												json_obj[i].order_source="";
// 											}
// 											if(json_obj[i].memo==null){
// 												json_obj[i].memo="";
// 											}
// 											result_table 
// 											+= "<tr>"
// 											+ "<td name='"+ json_obj[i].seq_no +"'>"+ json_obj[i].seq_no+ "</td>"
// 											+ "<td name='"+ json_obj[i].order_no +"'>"+ json_obj[i].order_no+ "</td>"
// 											+ "<td name='"+ json_obj[i].product_name +"'>"+ json_obj[i].product_name+ "</td>"
// 											+ "<td name='"+ json_obj[i].c_product_id +"'>"+ json_obj[i].c_product_id+ "</td>"
// 											+ "<td name='"+ json_obj[i].quantity +"'>"+ json_obj[i].quantity+ "</td>"
// 											+ "<td name='"+ json_obj[i].price +"'>"+ money(json_obj[i].price)+ "</td>"
// 											+ "<td name='"+ json_obj[i].invoice +"'>"+ json_obj[i].invoice+ "</td>"
// 											+ "<td name='"+ json_obj[i].invoice_date +"'>"+ json_obj[i].invoice_date+ "</td>"
// 											+ "<td name='"+ json_obj[i].trans_list_date +"'>"+ json_obj[i].trans_list_date+ "</td>"
// // 											+ "<td name='"+ json_obj[i].dis_date +"'>"+ json_obj[i].dis_date+ "</td>"
// 											+ "<td name='"+ json_obj[i].sale_date +"'>"+ json_obj[i].sale_date+ "</td>"
// 											+ "<td name='"+ json_obj[i].order_source +"'>"+ json_obj[i].order_source+ "</td>"
// 											+ "<td name='"+ json_obj[i].memo +"'>"+ json_obj[i].memo+ "</td>"
// 											+ "<td><div class='table-row-func btn-in-table btn-gray'><i class='fa fa-ellipsis-h'></i>"
// 											+ "	<div class='table-function-list'>"
// 											+ "		<button class='btn-in-table btn-darkblue btn_update' title='修改' id='"+json_obj[i].seq_no+"'value='"+ json_obj[i].sale_id+"'name='"+ json_obj[i].c_product_id+"' ><i class='fa fa-pencil'></i></button>"
// 											+ "		<button class='btn-in-table btn-alert btn_delete' title='刪除' id='"+json_obj[i].seq_no+"'value='"+ json_obj[i].sale_id+"'name='"+ json_obj[i].c_product_id+"'><i class='fa fa-trash'></i></button>"
// 											+ "	</div></div></td></tr>";								
// 										}
// 									});
// 								}	
// 								$("#sales").dataTable().fnDestroy();
// 								if(resultRunTime!=0){
// 									$("#sales_contain_row").show();
// 									$("#sales tbody").html(result_table);
// 									$("#sales").dataTable({
// 										  autoWidth: false,
// 										  scrollX:  true,
// 								          scrollY:"300px","language": {"url": "js/dataTables_zh-tw.txt"}});
// 									$("#sales").find("td").css("text-align", "center");
// 									//$("#sales").find("th").css({"word-break":"break-all","min-width":"40px","text-align":"center" });
// 									//$("#sales").find("td").css({"word-break":"break-all","min-width":"40px","text-align":"center" });
// 								}else{
// 									$("#sales_contain_row").hide();
// 								}
// 							}
// 						});
						update_dialog.dialog("close");
						$("#update-dialog-form-post").trigger("reset");
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
		//刪除事件聆聽 : 因為聆聽事件動態產生，所以採用delegate來批量處理，節省資源
		$("#sales").delegate(".btn_delete", "click", function() {
			$("#dialog-confirm").html("<table class='dialog-table'>"+
				"<tr><td>銷貨單號：</td><td><span class='delete_msg'>'"+$(this).parents("tr").find("td:nth-child(2)").attr("name")+"'</span></td></tr>"+
				"<tr><td>交易物品：</td><td><span class='delete_msg'>'"+$(this).parents("tr").find("td:nth-child(3)").attr("name")+"'</span></td></tr>"+
				"<tr><td>銷貨金額：</td><td><span class='delete_msg'>'"+$(this).parents("tr").find("td:nth-child(6)").html()      +"'</span></td></tr>"+
				"<tr><td>銷貨日期：</td><td><span class='delete_msg'>'"+$(this).parents("tr").find("td:nth-child(10)").attr("name")+"'</span></td></tr>"+
				"</table>"
			);
// 			alert($(this).attr("name"));
			uuid = $(this).val();
			c_product_id = $(this).attr("name");
			confirm_dialog.dialog("open");
		});
		//新增事件聆聽
		$("#create-sale").click(function() {
			new_or_edit=1;
			insert_dialog.dialog("open");
			$("#insert_c_product_id").focus();
			scan_exist=1;
			if(!scan_exist){
				$("#warning").html("貼心提醒您:<br>&nbsp;&nbsp;掃描器尚未配置妥善。");
				$("#warning").dialog("open");
			}
		});
		//修改事件聆聽
		$("#sales").delegate(".btn_update", "click", function(e) {
			e.preventDefault();
			new_or_edit=2;
			uuid = $(this).val();
			seqNo = $(this).attr("id");
			$("input[name='searh_c_product_id'").val("");
			$("#trans_list_start_date").val("");
			$("#trans_list_end_date").val("");
			
// 			alert($(this).parents("tr").find("td:nth-child(3)").attr("name"));
			$("#dialog-form-update input[name='order_no']").val($(this).parents("tr").find("td:nth-child(2)").attr("name"));
			$("#dialog-form-update input[name='product_name']").val($(this).parents("tr").find("td:nth-child(3)").attr("name"));
			$("#dialog-form-update input[name='c_product_id']").val($(this).parents("tr").find("td:nth-child(4)").attr("name"));
			$("#dialog-form-update input[name='name']").val("");//"somebody"
			$("#dialog-form-update input[name='quantity']").val($(this).parents("tr").find("td:nth-child(5)").attr("name"));
			$("#dialog-form-update input[name='price']").val($(this).parents("tr").find("td:nth-child(6)").attr("name"));
			$("#dialog-form-update input[name='update_product_price']").val($(this).parents("tr").find("td:nth-child(5)").attr("name")*$(this).parents("tr").find("td:nth-child(6)").attr("name"));
			$("#dialog-form-update input[name='invoice']").val($(this).parents("tr").find("td:nth-child(7)").attr("name"));
			$("#dialog-form-update input[name='invoice_date']").val($(this).parents("tr").find("td:nth-child(8)").attr("name"));
			$("#dialog-form-update input[name='trans_list_date']").val($(this).parents("tr").find("td:nth-child(9)").attr("name"));
			$("#dialog-form-update input[name='dis_date']").val("1991-06-29");
			$("#dialog-form-update input[name='memo']").val($(this).parents("tr").find("td:nth-child(12)").attr("name"));
			$("#dialog-form-update input[name='sale_date']").val($(this).parents("tr").find("td:nth-child(10)").attr("name"));
			$("#dialog-form-update input[name='order_source']").val($(this).parents("tr").find("td:nth-child(11)").attr("name"));
			
			
			
			
			
// 			$.ajax({
// 				type : "POST",
// 				url : "sale.do",
// 				data : {
// 					action : "search",
// 					c_product_id : $("input[name='searh_c_product_id'").val(),
// 					trans_list_start_date: $("#trans_list_start_date").val(),
// 					trans_list_end_date: $("#trans_list_end_date").val()
// 				},
// 				success : function(result) {
// 						console.log(result);
// 						var json_obj = $.parseJSON(result);
// 						var len=json_obj.length;
// 						//判斷查詢結果
// 						var resultRunTime = 0;
// 						$.each (json_obj, function (i) {
// 							resultRunTime+=1;
// 						});
// 						if(json_obj[resultRunTime-1].message=="驗證通過"){
// 							var result_table = "";
// 							$.each(json_obj,function(i, item) {
// 								if(i<len-1){
// 									if(json_obj[i].sale_id==uuid){
										
// 										$("#dialog-form-update input[name='order_no']").val(json_obj[i].order_no);
// 										$("#dialog-form-update input[name='product_name']").val(json_obj[i].product_name);
// 										$("#dialog-form-update input[name='c_product_id']").val(json_obj[i].c_product_id);
// 										$("#dialog-form-update input[name='name']").val(json_obj[i].name);
// 										$("#dialog-form-update input[name='quantity']").val(json_obj[i].quantity);
// 										$("#dialog-form-update input[name='price']").val(json_obj[i].price);
// 										$("#dialog-form-update input[name='update_product_price']").val(json_obj[i].price*json_obj[i].quantity);
// 										$("#dialog-form-update input[name='invoice']").val(json_obj[i].invoice);
// 										$("#dialog-form-update input[name='invoice_date']").val(json_obj[i].invoice_date);
// 										$("#dialog-form-update input[name='trans_list_date']").val(json_obj[i].trans_list_date);
// 										$("#dialog-form-update input[name='dis_date']").val(json_obj[i].dis_date);
// 										$("#dialog-form-update input[name='memo']").val(json_obj[i].memo);
// 										$("#dialog-form-update input[name='sale_date']").val(json_obj[i].sale_date);
// 										$("#dialog-form-update input[name='order_source']").val(json_obj[i].order_source);
										
// 									}
// 								}
// 							});
// 						}
// 					}
// 				});	
			update_dialog.dialog("open");
// 			$("#update_c_product_id").focus();
		});
		//處理初始的查詢autocomplete
       $("#searh_c_product_id").autocomplete({
            minLength: 2,
            source: function (request, response) {
                $.ajax({
                    url : "sale.do",
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
		//處理新增的名稱autocomplete
       $("#insert_product_name").autocomplete({
            minLength: 2,
            source: function (request, response) {
                $.ajax({
                    url : "sale.do",
                    type : "POST",
                    cache : false,
                    delay : 1500,
                    data : {
                    	action : "search_product_data",
                    	identity : "NAME",
                        term : request.term
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
    	            $(this).attr("placeholder","請輸入正確的產品名稱!");
    	        }
    	    }     
         });
       $("#insert_product_name").bind('focus', function(){ $(this).attr("placeholder","請輸入產品名稱以供查詢"); } );
       $('#insert_product_name').bind('autocompleteselect', function (e, ui) {
       		$("#insert_c_product_id").val(ui.item.c_product_id);
       		$("#insert_price").val(ui.item.price);
       		$("#insert_quantity").val('1');
       		$("#insert_product_price").val($("#insert_quantity").val()*$("#insert_price").val());
       		product_id = ui.item.product_id;
       });   
		//處理新增的自訂ID autocomplete
       $("#insert_c_product_id").autocomplete({
            minLength: 2,
            source: function (request, response) {
                $.ajax({
                    url : "sale.do",
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
                              value: item.c_product_id,
                              product_id: item.product_id,
                              product_name: item.product_name,
                              c_product_id: item.c_product_id,
                              price: item.price,
                              cost: item.cost
                            }
                          }));
                    }
                });
            },
            change: function(e, ui) {
           	 if (!ui.item) {
           		 $(this).val("");
                    $(this).attr("placeholder","請輸入正確產品ID名稱!");
                }
           },
            response: function(e, ui) {
                if (ui.content.length == 0) {
                    $(this).val("");
                    $(this).attr("placeholder","請輸入正確ID名稱!");
                }
            }          
        }).blur(function(){
            $.ajax({
                url : "sale.do",
                type : "POST",
                cache : false,
                delay : 1500,
                data : {
                	action : "search_product_data",
                	identity : "ID",
                    term : $("#insert_c_product_id").val()
                },
                success: function(data) {
                	var json_obj = $.parseJSON(data);
                	var resultRunTime = 0;
					$.each (json_obj, function (i) {
						resultRunTime+=1;
					});
					if(resultRunTime==0){
						$("#insert_c_product_id").val("");
						$("#insert_c_product_id").attr("placeholder","請輸入正確ID名稱!");
					}
                }
            });
        });
       $('#insert_c_product_id').bind('autocompleteselect', function (e, ui) {
       		$("#insert_product_name").val(ui.item.product_name);
       		$("#insert_price").val(ui.item.price);
       		$("#insert_quantity").val('1');
       		$("#insert_product_price").val($("#insert_quantity").val()*$("#insert_price").val());
       		product_id = ui.item.product_id;
       });
		//處理修改的名稱autocomplete
       $("#update_product_name").autocomplete({
            minLength: 2,
            source: function (request, response) {
                $.ajax({
                    url : "sale.do",
                    type : "POST",
                    cache : false,
                    delay : 1500,
                    data : {
                    	action : "search_product_data",
                    	identity : "NAME",
                        term : request.term
                    },
                    success: function(data) {
                    	var json_obj = $.parseJSON(data);
                    	response($.map(json_obj, function (item) {
                            return {
                              label: item.product_name,
                              value: item.product_name,
                              product_id: item.product__id,
                              product_name: item.product_name,
                              c_product_id: item.c_product_id,
                              price: item.price,
                              cost: item.cost
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
    	            $(this).attr("placeholder","請輸入正確的產品名稱!");
    	        }
    	    }     
         });
       $("#update_product_name").bind('focus', function(){ $(this).attr("placeholder","請輸入產品名稱以供查詢"); } );
       $('#update_product_name').bind('autocompleteselect', function (e, ui) {
       		$("#update_c_product_id").val(ui.item.c_product_id);
       		$("#update_price").val(ui.item.price);
       		$("#update_quantity").val('1');
       		$("#update_product_price").val($("#update_quantity").val()*$("#update_price").val());
       		product_id = ui.item.product_id;
       });   
		//處理修改的自訂ID autocomplete
       $("#update_c_product_id").autocomplete({
            minLength: 2,
            source: function (request, response) {
                $.ajax({
                    url : "sale.do",
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
                              value: item.c_product_id,
                              product_id: item.product_id,
                              product_name: item.product_name,
                              c_product_id: item.c_product_id,
                              price: item.price,
                              cost: item.cost
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
    	            $(this).attr("placeholder","請輸入正確的ID名稱!");
    	        }
    	    }     
         });
       $("#update_c_product_id").bind('focus', function(){ $(this).attr("placeholder","請輸入ID名稱以供查詢"); } );
       $('#update_c_product_id').bind('autocompleteselect', function (e, ui) {
       		$("#update_product_name").val(ui.item.product_name);
       		$("#update_price").val(ui.item.price);
       		$("#update_quantity").val('1');
       		$("#update_product_price").val($("#update_quantity").val()*$("#update_price").val());
       		product_id = ui.item.product_id;
       });       
		$("#update_quantity").change(function(){
			$("#update_product_price").val($("#update_quantity").val()*$("#update_price").val());
		});
		$("#update_price").change(function(){
			$("#update_product_price").val($("#update_quantity").val()*$("#update_price").val());
		});
		$("#insert_quantity").change(function(){
			$("#insert_product_price").val($("#insert_quantity").val()*$("#insert_price").val());
		});
		$("#insert_price").change(function(){
			$("#insert_product_price").val($("#insert_quantity").val()*$("#insert_price").val());
		});
		order_source_auto("insert-dialog-form-post input[name='order_source']");
		order_source_auto("update-dialog-form-post input[name='order_source']");
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
		$("#warning").show();
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
	});
</script>

<!-- 	<div class="panel-title"> -->
<!-- 		<h2>銷貨管理</h2> -->
<!-- 	</div> -->
	<div class="panel-content">
		<div class="datalistWrap">
			<!--對話窗樣式-確認 -->
			<div id="dialog-confirm" title="是否刪除此銷貨資料?" style="display:none;"></div>
			<!--對話窗樣式-修改 -->
			<div id="dialog-form-update" title="修改銷貨資料" style="display:none;">
				<form name="update-dialog-form-post" id="update-dialog-form-post">
				<font color=red style="padding-left:26px">掃條碼亦可取得商品資料</font>
					<fieldset>
						<table class='form-table'>
							<tr>
								<td>訂單號：</td>
								<td><input type="text" name="order_no"  placeholder="輸入訂單號"></td>
								<td>客戶名字：</td>
								<td><input type="text" name="name"  placeholder="輸入客戶名字"></td>
							</tr>
							<tr>
								<td>自訂產品ID：</td>
								<td><input type="text" id="update_c_product_id" name="c_product_id"  placeholder="輸入自訂產品ID"></td>
								<td>產品名稱：</td>
								<td><input type="text" id="update_product_name" name="product_name"  placeholder="輸入產品名稱"></td>
							</tr>
							<tr>
								<td>銷貨數量：</td>
								<td><input type="text" id="update_quantity" name="quantity"  placeholder="輸入銷貨數量"></td>
								<td>單價：</td>
								<td><input type="text" id="update_price" name="price" placeholder="輸入單價"></td>
							</tr>
							<tr>
								<td>總金額：</td>
								<td><input type="text" id="update_product_price" name="update_product_price" disabled></td>
							</tr>
							<tr>
								<td>發票號碼：</td>
								<td><input type="text" name="invoice"  placeholder="輸入發票號碼"></td>
								<td>發票日期：</td>
								<td><input type="text" name="invoice_date"  placeholder="輸入發票日期" class="input-date"></td>
							</tr>
							<tr>
								<td>轉單日：</td>
								<td><input type="text" name="trans_list_date"  placeholder="輸入轉單日" class="input-date"></td>
								<td>銷貨/出貨日期：</td>
								<td><input type="text" name="sale_date"  placeholder="輸入銷貨/出貨日期" class="input-date"></td>
							</tr>
							<tr>
								<td>備註說明：</td>
								<td><input type="text" name="memo"  placeholder="輸入備註說明"></td>
								<td>銷售平台：</td>
								<td><input type="text" name="order_source"  placeholder="輸入銷售平台"></td>
							</tr>
							<tr>
								<td style="display:none">配送日：</td>
								<td style="display:none"><input type="text" name="dis_date"  placeholder="輸入配送日" class="input-date" value="2000-01-01"></td>
							</tr>
						</table>
					</fieldset>
				</form>
			</div>
			<!--對話窗樣式-新增 -->
			<div id="dialog-form-insert" title="新增銷貨資料" style="display:none;">
				<form name="insert-dialog-form-post" id="insert-dialog-form-post"style="display:inline">
					<font color=red style="padding-left:26px">掃條碼亦可取得商品資料</font>
					<fieldset>
						<table class='form-table' style="border-spacing: 10px 18px;">
							<tr>
								<td>銷貨單號：</td>
								<td><input type="text" name="original_seq_no" disabled="disabled" value="系統自動產生"></td>
								<td>訂單號：</td>
								<td><input type="text" name="order_no"  placeholder="輸入訂單號"></td>
							</tr>
							<tr>
								<td>客戶名字：</td>
								<td><input type="text" name="name"  placeholder="輸入客戶名字"></td>
							</tr>
							<tr>
								<td>自訂產品ID：</td>
								<td><input type="text" id="insert_c_product_id" name="c_product_id"  placeholder="輸入自訂產品ID"></td>
								<td>產品名稱：</td>
								<td><input type="text" id="insert_product_name" name="product_name"  placeholder="輸入產品名稱"></td>
							</tr>
							<tr>
								<td>銷貨數量：</td>
								<td><input type="text" id="insert_quantity" name="quantity"  placeholder="輸入銷貨數量"></td>
								<td>單價：</td>
								<td><input type="text" id="insert_price" name="price" placeholder="輸入單價"></td>
							</tr>
							<tr>
								<td>總金額：</td>
								<td><input type="text" id="insert_product_price" name="insert_product_price" disabled></td>
								
							</tr>
							<tr>
								<td>發票號碼：</td>
								<td><input type="text" name="invoice"  placeholder="輸入發票號碼"></td>
								<td>發票日期：</td>
								<td><input type="text" name="invoice_date"  placeholder="輸入發票日期" class="input-date"></td>
							</tr>
							<tr>
								<td>轉單日：</td>
								<td><input type="text" name="trans_list_date"  placeholder="輸入轉單日" class="input-date"></td>
								<td>銷貨/出貨日期：</td>
								<td><input type="text" name="sale_date"  placeholder="輸入銷貨/出貨日期" class="input-date"></td>
							</tr>
							<tr>
								<td>銷售平台：</td>
								<td><input type="text" name="order_source"  placeholder="輸入銷售平台"></td>
								<td>備註說明：</td>
								<td><input type="text" name="memo"  placeholder="輸入備註說明"></td>
								<td style="display:none">配送日：</td>
								<td style="display:none"><input type="text" name="dis_date"  placeholder="輸入配送日" class="input-date" value="2000-01-01"></td>
							</tr>
						</table>
					</fieldset>
				</form>
			</div>
			
		<div class="input-field-wrap">
			<div class="form-wrap">
				<div class="form-row">
					<label for="">
						<span class="block-label">自訂產品 ID 查詢</span>
						<input type="text" id="searh_c_product_id" name="searh_c_product_id">
					</label>
					<button class="btn btn-darkblue" id="searh-sale">查詢</button>
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
					<button class="btn btn-darkblue" id="searh-trans-list-date">查詢</button>
				</form>
				</div>
				<div class="btn-row">
					<button class="btn btn-exec btn-wide" id="create-sale">新增銷售資料</button>
				</div>
			</div><!-- /.form-wrap -->
		</div>
			<div class="row search-result-wrap" align="center" id ="sales_contain_row"  style="display:none;">	
				<div id="sales-contain" class="ui-widget">
					<table id="sales" class="result-table" style="width:99.9%;">
						<thead>
							<tr class="">
								<th>銷貨單號</th>
								<th>訂單號</th>
								<th style="min-width:120px">產品名稱</th>
								<th>自訂產品ID</th>
								<th>銷貨數量</th>
								<th style="min-width:80px">銷貨金額</th>
								<th>發票號碼</th>
								<th>發票日期</th>
								<th>轉單日</th>
<!-- 								<th>配送日</th> -->
								<th>銷貨/出貨日期</th>
								<th>銷售平台</th>
								<th>備註說明</th>
								<th>功能</th>
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
	</div>
	</div>
<div id="warning"  style="display:none;"></div>
</body>
</html>