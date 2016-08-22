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
<title>商品管理</title>
<meta charset="utf-8">

<link rel="Shortcut Icon" type="image/x-icon" href="./images/Rockettheme-Ecommerce-Shop.ico" />
<!-- 圖片的 -->
<!-- <link rel="stylesheet" href="//netdna.bootstrapcdn.com/bootstrap/3.2.0/css/bootstrap.min.css"> -->
<!-- Generic page styles -->
<link rel="stylesheet" href="css/photo/style.css">	
<!-- CSS to style the file input field as button and adjust the Bootstrap progress bars -->
<link rel="stylesheet" href="css/photo/jquery.fileupload.css">
<link rel="stylesheet" href="css/styles.css" />
<link href="<c:url value="css/css.css" />" rel="stylesheet">
<link href="<c:url value="css/jquery.dataTables.min.css" />" rel="stylesheet">
<link href="<c:url value="css/1.11.4/jquery-ui.css" />" rel="stylesheet">
<!-- <script type="text/javascript" src="js/jquery-1.10.2.js"></script> -->

</head>
<body>
	<jsp:include page="template.jsp" flush="true"/>
	<div class="content-wrap" style="margin:56px 0px 28px 120px;">

<script src="js/photo/jquery.min.js"></script>
<!-- The jQuery UI widget factory, can be omitted if jQuery UI is already included -->
<script src="js/photo/vendor/jquery.ui.widget.js"></script>
<!-- The Load Image plugin is included for the preview images and image resizing functionality -->
<script src="js/photo/load-image.all.min.js"></script>
<!-- The Canvas to Blob plugin is included for image resizing functionality -->
<script src="js/photo/canvas-to-blob.min.js"></script>
<!-- Bootstrap JS is not required, but included for the responsive demo navigation -->
<script src="js/photo/bootstrap.min.js"></script>
<!-- The Iframe Transport is required for browsers without support for XHR file uploads -->
<script src="js/photo/jquery.iframe-transport.js"></script>
<!-- The basic File Upload plugin -->
<script src="js/photo/jquery.fileupload.js"></script>
<!-- The File Upload processing plugin -->
<script src="js/photo/jquery.fileupload-process.js"></script>
<!-- The File Upload image preview & resize plugin -->
<script src="js/photo/jquery.fileupload-image.js"></script>
<!-- The File Upload audio preview plugin -->
<script src="js/photo/jquery.fileupload-audio.js"></script>
<!-- The File Upload video preview plugin -->
<script src="js/photo/jquery.fileupload-video.js"></script>
<!-- The File Upload validation plugin -->
<script src="js/photo/jquery.fileupload-validate.js"></script>

<!-- 新3修 -->

<script type="text/javascript" src="js/jquery-1.11.4.js"></script>
<script type="text/javascript" src="js/jquery.dataTables.min.js"></script>
<script type="text/javascript" src="js/jquery-ui.min.js"></script>
<script src="js/photo/jquery-ui.js"></script>
<script type="text/javascript" src="js/jquery.validate.min.js"></script>
<script type="text/javascript" src="js/additional-methods.min.js"></script>
<script type="text/javascript" src="js/messages_zh_TW.min.js"></script>
<script type="text/javascript" src="js/jquery.scannerdetection.js"></script>

<script>
	var new_or_edit=0;
	var scan_exist=0;
// 	$(document).scannerDetection({
//     	timeBeforeScanTest: 200, // wait for the next character for upto 200ms
//     	startChar: [120], // Prefix character for the cabled scanner (OPL6845R)
//     	endChar: [13], // be sure the scan is complete if key 13 (enter) is detected
//     	avgTimeByChar: 40, // it's not a barcode if a character takes longer than 40ms
//     	onComplete: function(barcode, qty){}, // main callback function	
//     	onKeyDetect:function(barcode,qty){$("input").blur();}
//     });
	jQuery(document).ready(function($) {
	    $(window).scannerDetection();
	    $(window).bind('scannerDetectionComplete',function(e,data){
	    		if(data.string=="success"){return;}
	    		if(new_or_edit==1){
	    			//$("#new_barcode").focus();
	    			$("#new_barcode").val(data.string);
	    		}
	    		if(new_or_edit==2){
	    			//$("#edit_barcode").focus();
	    			$("#edit_barcode").val(data.string);
	    		}
	    		if(new_or_edit==0){
	    			new_or_edit=3;
	    			$.ajax({url : "product.do", type : "POST", cache : false,
			            data : {
			            	action : "find_barcode",
			            	barcode : data.string,
			            },
			            success: function(result) {
			            	var json_obj = $.parseJSON(result);
			            	var result_table = "";
							$.each(json_obj,function(i, item) {
								if(i<json_obj.length){
									result_table 
									+= "<tr>"
									+ "<td name='"+ json_obj[i].c_product_id +"'>"+ json_obj[i].c_product_id+ "</td>"
									+ "<td name='"+ json_obj[i].product_name +"'>"+ json_obj[i].product_name+ "</td>"
									+ "<td name='"+ json_obj[i].cost +"'>"+ json_obj[i].cost + "</td>"
									+ "<td name='"+ json_obj[i].keep_stock +"'>"+ json_obj[i].keep_stock+ "</td>"
									+ "<td><div class='table-row-func btn-in-table btn-gray'><i class='fa fa-ellipsis-h'></i>"
									+ "	<div class='table-function-list'>"
									+ "		<button class='btn-in-table btn-darkblue btn_update' title='修改' id='"+json_obj[i].product_id+"'value='"+ json_obj[i].product_id+ "'><i class='fa fa-pencil'></i></button>"
									+ "		<button class='btn-in-table btn-alert btn_delete' title='刪除' id='"+json_obj[i].product_id+"'value='"+ json_obj[i].product_id+ "' ><i class='fa fa-trash'></i></button>"
									+ "	</div></div></td></tr>";
								}
								if(json_obj.length==0){
									$("#sales-contain22").hide();
									$(".validateTips").text("查無此結果");
								}
								if(json_obj.length>0){
									$("#sales-contain22").show();
									$("#sales22 tbody").html(result_table);
									$("#sales22").find("td").css("text-align","center");
									$("#sales22").find("th").css("text-align","center");
									$(".validateTips").text("");
								}
								
							});
			            }
		    		});
	    		}
	        })
	        .bind('scannerDetectionError',function(e,data){
	            console.log('detection error '+data.string);
	        })
	        .bind('scannerDetectionReceive',function(e,data){
	            console.log(data);
	        });
	
	    $(window).scannerDetection('success');
	});
	$(function() {
		var uuid = "";
		var c_product_id="";
		var unit_id="";
		var supply_name = "";
		var type_id= "";
		var supply_id= "";
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
		var validator_insert = $("#insert-dialog-form-post").validate({
			rules : {
				c_product_id : {
					maxlength : 40,
					required : true
				},
				product_name:{
					maxlength : 80,
					required : true
				},
				supply_name : {
					maxlength : 40,
					required : true
				},
				type_id: {
					required : true
				},
				unit_id: {
					required : true
				},
				cost: {
					required : true
				},
				price: {
					required : true
				},
				keep_stock: {
					required : true
				},
				description : {
					stringMaxLength : 200
				},
				barcode : {
					stringMaxLength : 40
				}
			}
		});
		var validator_update = $("#update-dialog-form-post").validate({
			rules : {
				c_product_id : {
					maxlength : 40,
					required : true
				},
				product_name:{
					maxlength : 80,
					required : true
				},
				supply_name : {
					maxlength : 40,
					required : true
				},
				type_id: {
					required : true
				},
				unit_id: {
					required : true
				},
				cost: {
					required : true
				},
				price: {
					required : true
				},
				keep_stock: {
					required : true
				},
				description : {
					stringMaxLength : 200
				},
				barcode : {
					stringMaxLength : 40
				}
			}
		});		
		
		
		//自訂產品ID查詢相關設定
		$("#searh-sale").click(function(e) {
			e.preventDefault();
			$.ajax({
					type : "POST",
					url : "product.do",
					data : {
						action : "search",
						supply_name : $("input[name='searh_product_name'").val(),
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
									
									//herehere
									if(i<len-1){
										result_table 
										+= "<tr>"
										+ "<td name='"+ json_obj[i].c_product_id +"'>"+ json_obj[i].c_product_id+ "</td>"
										+ "<td name='"+ json_obj[i].product_name +"'>"+ json_obj[i].product_name+ "</td>"
// 										+ "<td name='"+ json_obj[i].supply_id  +"'>"+ json_obj[i].supply_id + "</td>"
										+ "<td name='"+ json_obj[i].supply_name +"'>"+ json_obj[i].supply_name + "</td>"
										+ "<td name='"+ json_obj[i].type_id +"'>"+ json_obj[i].type_id+ "</td>"
										+ "<td name='"+ json_obj[i].unit_id +"'>"+ json_obj[i].unit_id+ "</td>"
										+ "<td name='"+ json_obj[i].cost +"'>"+ json_obj[i].cost+ "</td>"
										+ "<td name='"+ json_obj[i].price +"'>"+ json_obj[i].price+ "</td>"
										+ "<td name='"+ json_obj[i].keep_stock +"'>"+ json_obj[i].keep_stock+ "</td>"
// 										+ "<td name='"+ json_obj[i].photo+"'>"+ json_obj[i].photo+ "</td>"
// 										+ "<td name='"+ json_obj[i].photo1 +"'>"+ json_obj[i].photo1+ "</td>"
										+ "<td name='"+ json_obj[i].description +"'>"+ json_obj[i].description+ "</td>"
										+ "<td name='"+ json_obj[i].barcode +"'>"+ json_obj[i].barcode+ "</td>"
										+ "<td><div class='table-row-func btn-in-table btn-gray'><i class='fa fa-ellipsis-h'></i>"
										+ "	<div class='table-function-list'>"
										+ "		<button class='btn-in-table btn-darkblue btn_update' title='修改' id='"+json_obj[i].product_id+"'value='"+ json_obj[i].product_id+ "'name='"+ json_obj[i].product_name+"'><i class='fa fa-pencil'></i></button>"
										+ "		<button class='btn-in-table btn-alert btn_delete' title='刪除' id='"+json_obj[i].product_id+"'value='"+ json_obj[i].product_id+ "'name='"+ json_obj[i].product_name+"' ><i class='fa fa-trash'></i></button>"
										+ "	</div></div></td></tr>";
									}
								});
							}							
							if(resultRunTime==0){
								$("#sales-contain").hide();
								$(".validateTips").text("查無此結果");
							}
							$("#sales").dataTable().fnDestroy();
							if(resultRunTime!=0&&json_obj[resultRunTime-1].message=="驗證通過"){
								$("#sales-contain").show();
								$("#sales tbody").html(result_table);
								$("#sales").dataTable({"language": {"url": "js/dataTables_zh-tw.txt"},"order": []});
								$("#sales").find("td").css("text-align","center");
								$("#sales").find("th").css("text-align","center");
								$(".validateTips").text("");
							}
						}
					});
		})
		//新增Dialog相關設定
		insert_dialog = $("#dialog-form-insert").dialog(
						{
							draggable : false,//防止拖曳
							resizable : false,//防止縮放
							autoOpen : false,
							open : function(event, ui) {$(this).parent().children().children('.ui-dialog-titlebar-close').hide();},
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
													url : "product.do",
													data : {
														action : "insert",
														c_product_id : $("#dialog-form-insert input[name='c_product_id']").val(),
														product_name : $("#dialog-form-insert input[name='product_name']").val(),
// 														supply_id : $("#dialog-form-insert input[name='supply_id']").val(),
														supply_id : supply_id,
														supply_name : $("#dialog-form-insert input[name='supply_name']").val(),
														type_id : $("#dialog-form-insert select[name='select_insert_type_id']").val(),
														unit_id : $("#dialog-form-insert select[name='select_insert_unit_id']").val(),
														cost : $("#dialog-form-insert input[name='cost']").val(),
														price : $("#dialog-form-insert input[name='price']").val(),
														keep_stock : $("#dialog-form-insert input[name='keep_stock']").val(),
														photo : $("#photo").val(),
														photo1 : $("#photo1").val(),
// 														photo : $("#dialog-form-insert input[name='photo']").val(),
// 														photo1 : $("#dialog-form-insert input[name='photo1']").val(),
														description : $("#dialog-form-insert input[name='description']").val(),
														barcode : $("#dialog-form-insert input[name='barcode']").val()
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
														$.each(json_obj,function(i, item) {
															if(i<len-1){
																result_table 
																+= "<tr>"
																	+ "<td name='"+ json_obj[i].c_product_id +"'>"+ json_obj[i].c_product_id+ "</td>"
																	+ "<td name='"+ json_obj[i].product_name +"'>"+ json_obj[i].product_name+ "</td>"
// 																	+ "<td name='"+ json_obj[i].supply_id  +"'>"+ json_obj[i].supply_id + "</td>"
																	+ "<td name='"+ json_obj[i].supply_name +"'>"+ json_obj[i].supply_name + "</td>"
																	+ "<td name='"+ json_obj[i].type_id +"'>"+ json_obj[i].type_id+ "</td>"
																	+ "<td name='"+ json_obj[i].unit_id +"'>"+ json_obj[i].unit_id+ "</td>"
																	+ "<td name='"+ json_obj[i].cost +"'>"+ json_obj[i].cost+ "</td>"
																	+ "<td name='"+ json_obj[i].price +"'>"+ json_obj[i].price+ "</td>"
																	+ "<td name='"+ json_obj[i].keep_stock +"'>"+ json_obj[i].keep_stock+ "</td>"
// 																	+ "<td name='"+ json_obj[i].photo+"'>"+ json_obj[i].photo+ "</td>"
// 																	+ "<td name='"+ json_obj[i].photo1 +"'>"+ json_obj[i].photo1+ "</td>"
																	+ "<td name='"+ json_obj[i].description +"'>"+ json_obj[i].description+ "</td>"
																	+ "<td name='"+ json_obj[i].barcode +"'>"+ json_obj[i].barcode+ "</td>"
																	+ "<td><div class='table-row-func btn-in-table btn-gray'><i class='fa fa-ellipsis-h'></i>"
																	+ "	<div class='table-function-list'>"
																	+ "		<button class='btn-in-table btn-darkblue btn_update' title='修改' id='"+json_obj[i].product_id+"'value='"+ json_obj[i].product_id+ "'name='"+ json_obj[i].product_name+"'><i class='fa fa-pencil'></i></button>"
																	+ "		<button class='btn-in-table btn-alert btn_delete' title='刪除' id='"+json_obj[i].product_id+"'value='"+ json_obj[i].product_id+ "'name='"+ json_obj[i].product_name+"' ><i class='fa fa-trash'></i></button>"
																	+ "	</div></div></td></tr>";
															}
														});
														$("#sales").dataTable().fnDestroy();
														if(resultRunTime!=0){
															$("#sales-contain").show();
															$("#sales tbody").html(result_table);
															$("#sales").dataTable({"language": {"url": "js/dataTables_zh-tw.txt"},"order": []});
															$("#sales").find("td").css("text-align","center");
															$("#sales").find("th").css("text-align","center");
															$(".validateTips").text("");
														}else{
															$("#sales-contain").hide();
														}
													}
												});
												insert_dialog.dialog("close");
											}
											new_or_edit=0;
										}
									}, {
										text : "取消",
										click : function() {
											$("#insert-dialog-form-post").trigger("reset");
											validator_insert.resetForm();
											insert_dialog.dialog("close");
											new_or_edit=0;
										}
									} ],
							close : function() {
								validator_insert.resetForm();
								$("#insert-dialog-form-post").trigger("reset");
							}
		})
		.css("width", "10%");
		$("#dialog-form-insert").show();
		//確認Dialog相關設定(刪除功能)
		confirm_dialog = $("#dialog-confirm").dialog({
			draggable : false,//防止拖曳
			resizable : false,//防止縮放
			autoOpen : false,
			open : function(event, ui) {$(this).parent().children().children('.ui-dialog-titlebar-close').hide();},
			height : "auto",
			modal : true,
			show : {effect : "blind",duration : 300},
			hide : {effect : "fade",duration : 300},
			buttons : {
				"確認刪除" : function() {
					$.ajax({
						type : "POST",
						url : "product.do",
						data : {
							action : "delete",
							product_id : uuid//c_product_id是為了刪除後，回傳指定的結果，所需參數
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
							$.each(json_obj,function(i, item) {
								if(i<len-1){
									result_table 
									+= "<tr>"
										+ "<td name='"+ json_obj[i].c_product_id +"'>"+ json_obj[i].c_product_id+ "</td>"
										+ "<td name='"+ json_obj[i].product_name +"'>"+ json_obj[i].product_name+ "</td>"
// 										+ "<td name='"+ json_obj[i].supply_id  +"'>"+ json_obj[i].supply_id + "</td>"
										+ "<td name='"+ json_obj[i].supply_name +"'>"+ json_obj[i].supply_name + "</td>"
										+ "<td name='"+ json_obj[i].type_id +"'>"+ json_obj[i].type_id+ "</td>"
										+ "<td name='"+ json_obj[i].unit_id +"'>"+ json_obj[i].unit_id+ "</td>"
										+ "<td name='"+ json_obj[i].cost +"'>"+ json_obj[i].cost+ "</td>"
										+ "<td name='"+ json_obj[i].price +"'>"+ json_obj[i].price+ "</td>"
										+ "<td name='"+ json_obj[i].keep_stock +"'>"+ json_obj[i].keep_stock+ "</td>"
// 										+ "<td name='"+ json_obj[i].photo+"'>"+ json_obj[i].photo+ "</td>"
// 										+ "<td name='"+ json_obj[i].photo1 +"'>"+ json_obj[i].photo1+ "</td>"
										+ "<td name='"+ json_obj[i].description +"'>"+ json_obj[i].description+ "</td>"
										+ "<td name='"+ json_obj[i].barcode +"'>"+ json_obj[i].barcode+ "</td>"
										+ "<td><div class='table-row-func btn-in-table btn-gray'><i class='fa fa-ellipsis-h'></i>"
										+ "	<div class='table-function-list'>"
										+ "		<button class='btn-in-table btn-darkblue btn_update' title='修改' id='"+json_obj[i].product_id+"'value='"+ json_obj[i].product_id+ "'name='"+ json_obj[i].product_name+"'><i class='fa fa-pencil'></i></button>"
										+ "		<button class='btn-in-table btn-alert btn_delete' title='刪除' id='"+json_obj[i].product_id+"'value='"+ json_obj[i].product_id+ "'name='"+ json_obj[i].product_name+"' ><i class='fa fa-trash'></i></button>"
										+ "	</div></div></td></tr>";										
								}
							});
							$("#sales").dataTable().fnDestroy();
							if(resultRunTime!=0){
								$("#sales-contain").show();
								$("#sales tbody").html(result_table);
								$("#sales").dataTable({"language": {"url": "js/dataTables_zh-tw.txt"},"order": []});
								$("#sales").find("td").css("text-align","center");
								$("#sales").find("th").css("text-align","center");
								$(".validateTips").text("");
							}else{
								$("#sales-contain").hide();
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
		//修改Dialog相關設定
		update_dialog = $("#dialog-form-update").dialog({
			draggable : false,//防止拖曳
			resizable : false,//防止縮放
			autoOpen : false,
			width : 800,
			height: "auto",
			modal : true,
			open : function(event, ui) {$(this).parent().children().children('.ui-dialog-titlebar-close').hide();},
			show : {effect : "blind",duration : 300},
			hide : {effect : "fade",duration : 300},
			buttons : [{
				text : "修改",
				click : function() {
					if ($('#update-dialog-form-post').valid()) {
						$.ajax({
							type : "POST",
							url : "product.do",
							data : {
	 							action : "update",
								product_id : product_id,
	 							c_product_id : $("#dialog-form-update input[name='c_product_id']").val(),
								product_name : $("#dialog-form-update input[name='product_name']").val(),
								supply_id : $("#dialog-form-update input[name='supply_id']").val(),
								supply_name : $("#dialog-form-update input[name='supply_name']").val(),
								type_id : $("#dialog-form-update select[name='select_update_type_id']").val(),
								unit_id : $("#dialog-form-update select[name='select_update_unit_id']").val(),
								cost : $("#dialog-form-update input[name='cost']").val(),
								price : $("#dialog-form-update input[name='price']").val(),
								keep_stock : $("#dialog-form-update input[name='keep_stock']").val(),
								photo : $("#photo-update").val(),//$("#dialog-form-update input[name='photo-update']").val(),
								photo1 : $("#photo1-update").val(),//$("#dialog-form-update input[name='photo1-update']").val(),
								description : $("#dialog-form-update input[name='description']").val(),
								barcode : $("#dialog-form-update input[name='barcode']").val()
							},
							success : function(result) {
								//alert($("#photo-update").val());
								//alert($("#dialog-form-update input[name='fileupload-update']").val());
								//alert($("#fileupload-update").attr());
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
										if(i<len-1){
											result_table 
											+= "<tr>"
												+ "<td name='"+ json_obj[i].c_product_id +"'>"+ json_obj[i].c_product_id+ "</td>"
												+ "<td name='"+ json_obj[i].product_name +"'>"+ json_obj[i].product_name+ "</td>"
// 												+ "<td name='"+ json_obj[i].supply_id  +"'>"+ json_obj[i].supply_id + "</td>"
												+ "<td name='"+ json_obj[i].supply_name +"'>"+ json_obj[i].supply_name + "</td>"
												+ "<td name='"+ json_obj[i].type_id +"'>"+ json_obj[i].type_id+ "</td>"
												+ "<td name='"+ json_obj[i].unit_id +"'>"+ json_obj[i].unit_id+ "</td>"
												+ "<td name='"+ json_obj[i].cost +"'>"+ json_obj[i].cost+ "</td>"
												+ "<td name='"+ json_obj[i].price +"'>"+ json_obj[i].price+ "</td>"
												+ "<td name='"+ json_obj[i].keep_stock +"'>"+ json_obj[i].keep_stock+ "</td>"
// 												+ "<td name='"+ json_obj[i].photo+"'>"+ json_obj[i].photo+ "</td>"
// 												+ "<td name='"+ json_obj[i].photo1 +"'>"+ json_obj[i].photo1+ "</td>"
												+ "<td name='"+ json_obj[i].description +"'>"+ json_obj[i].description+ "</td>"
												+ "<td name='"+ json_obj[i].barcode +"'>"+ json_obj[i].barcode+ "</td>"
												+ "<td><div class='table-row-func btn-in-table btn-gray'><i class='fa fa-ellipsis-h'></i>"
												+ "	<div class='table-function-list'>"
												+ "		<button class='btn-in-table btn-darkblue btn_update' title='修改' id='"+json_obj[i].product_id+"'value='"+ json_obj[i].product_id+ "'name='"+ json_obj[i].product_name+"'><i class='fa fa-pencil'></i></button>"
												+ "		<button class='btn-in-table btn-alert btn_delete' title='刪除' id='"+json_obj[i].product_id+"'value='"+ json_obj[i].product_id+ "'name='"+ json_obj[i].product_name+"' ><i class='fa fa-trash'></i></button>"
												+ "	</div></div></td></tr>";	
												
										}
									});
								}	
								$("#sales").dataTable().fnDestroy();
								if(resultRunTime!=0){
									$("#sales-contain").show();
									$("#sales tbody").html(result_table);
									$("#sales").dataTable({"language": {"url": "js/dataTables_zh-tw.txt"},"order": []});
									$("#sales").find("td").css("text-align","center");
									$("#sales").find("th").css("text-align","center");
									$(".validateTips").text("");
								}else{
									$("#sales-contain").hide();
								}
							}
						});
						update_dialog.dialog("close");
					}
					new_or_edit=0;
				}
			}, {
				text : "取消",
				click : function() {
					validator_update.resetForm();
					$("#update-dialog-form-post").trigger("reset");
					update_dialog.dialog("close");
					new_or_edit=0;
				}
			} ],
			close : function() {
				$("#update-dialog-form-post").trigger("reset");
				validator_update.resetForm();
			}
		});
		$("#dialog-form-update").show();
// 		.css("font-size", "25px");;			
		//刪除事件聆聽 : 因為聆聽事件動態產生，所以採用delegate來批量處理，節省資源
		$("#sales").delegate(".btn_delete", "click", function() {
			uuid = $(this).val();
			product_id = $(this).attr("id");
			confirm_dialog.dialog("open");
		});
		//新增事件聆聽
		$("#create-sale").click( function() {
			new_or_edit=1;
			insert_dialog.dialog("open");
			$("#new_barcode").focus();
			scan_exist=1;
			if(!scan_exist){$("#warning").dialog("open");};
		});
		//修改事件聆聽
		$("#sales").delegate(".btn_update", "click", function(e) {
			new_or_edit=2;
			e.preventDefault();
			uuid = $(this).val();
			product_id = $(this).attr("id");
			$("input[name='search_product_name'").val("");
			$.ajax({
				type : "POST",
				url : "product.do",
				data : {
					action : "search",
					product_name : $("input[name='search_product_name'").val(),
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
									if(json_obj[i].product_id==uuid){
										$("#dialog-form-update input[name='product_id']").val(json_obj[i].product_id);
										$("#dialog-form-update input[name='c_product_id']").val(json_obj[i].c_product_id);
										$("#dialog-form-update input[name='product_name']").val(json_obj[i].product_name);
										$("#dialog-form-update input[name='supply_id']").val(json_obj[i].supply_id);
										$("#dialog-form-update input[name='supply_name']").val(json_obj[i].supply_name);
										$("#dialog-form-update select[name='select_update_type_id']").val(json_obj[i].type_id);
										$("#dialog-form-update select[name='select_update_unit_id']").val(json_obj[i].unit_id);
										$("#dialog-form-update input[name='cost']").val(json_obj[i].cost);
										$("#dialog-form-update input[name='price']").val(json_obj[i].price);
										$("#dialog-form-update input[name='keep_stock']").val(json_obj[i].keep_stock);
										$("#dialog-form-update input[name='photo']").val(json_obj[i].photo);
										$("#dialog-form-update input[name='photo1']").val(json_obj[i].photo1);
										$("#dialog-form-update input[name='description']").val(json_obj[i].description);
										$("#dialog-form-update input[name='barcode']").val(json_obj[i].barcode);
										
										if (json_obj[i].photo != '') {
											$("#product-photo").attr("src", "./image.do?picname=" + json_obj[i].photo);
											$("#product-photo").attr("width","150");
											$("#product-photo").attr("alt",json_obj[i].photo);
										}else{
											$("#product-photo").attr("src","");
											$("#product-photo").attr("width","100");
											$("#product-photo").attr("alt","無圖片");
										}
										if (json_obj[i].photo1 != '') {
											$("#product-photo1").attr("src", "./image.do?picname=" + json_obj[i].photo1);
											$("#product-photo1").attr("width","150");
											$("#product-photo1").attr("alt",json_obj[i].photo1);
										}else{
											$("#product-photo1").attr("src","");
											$("#product-photo1").attr("width","100");
											$("#product-photo1").attr("alt"," 囗無圖片");
										}
									}
								}
							});
						}
					}
				});			
			update_dialog.dialog("open");
		});
		//處理初始的查詢autocomplete
	       $("#searh_product_name").autocomplete({
	            minLength: 1,
	            source: function (request, response) {
	                $.ajax({
	                    url : "product.do",
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
	                              label: item.supply_name,
	                              value: item.supply_name,
	                              supply_id : item.supply_id,
	                              supply_name : item.supply_name,
	                            }
	                          }));
	                    },
	                    error: function(XMLHttpRequest, textStatus, errorThrown) {
	                        alert(textStatus);
	                    }
	                });
	            },
	            change: function(event, ui) {
	    	        var source = $(this).val();
	    	            var temp = $(".ui-autocomplete li").map(function () { return $(this).text()}).get();
	    	        var found = $.inArray(source, temp);
	    	
	    	        if(found < 0) {
	    	            $(this).val('');
	    	            $(this).attr("placeholder","請輸入正確廠商名稱以供查詢!");
	    	        }
	    	    }     
	         });
	       $("#searh_product_name").bind('focus', function(){ $(this).attr("placeholder","請輸入廠商名稱以供查詢"); } );
			//處理新增的下拉選單unit_id
	                $.ajax({
	                    url : "product.do",
	                    type : "POST",
	                    cache : false,
	                    delay : 1500,
	                    data : {
	                    	action : "search_product_data",
	                    	identity : "ID",
	                        term : ''
	                    },
	                    success: function(data) {
	                    	var json_obj = $.parseJSON(data);
							$.map(json_obj, function (item) {
	                    		if (item.unit_name != '') {
	                    			$("#select_insert_unit_id").append("<option value='" + item.unit_name + "'>" + item.unit_name + "</option>");
	                    		}
// 	                    		console.log("'" + item.unit_name + "'");
	                          })
	                    },
	                    error: function(XMLHttpRequest, textStatus, errorThrown) {
	                        alert(textStatus);
	                    }
	                });
			//處理新增的下拉選單type_id
   					 $.ajax({
	                    url : "product.do",
	                    type : "POST",
	                    cache : false,
	                    delay : 1500,
	                    data : {
	                    	action : "search_product_data",
	                    	identity : "ID2",
	                        term : ''
	                    },
	                    success: function(data) {
	                    	var json_obj = $.parseJSON(data);
							$.map(json_obj, function (item) {
	                    		if (item.type_name != '') {
	                    			$("#select_insert_type_id").append("<option value='" + item.type_name + "'>" + item.type_name + "</option>");
	                    		}
	                          })
	                    },
	                    error: function(XMLHttpRequest, textStatus, errorThrown) {
	                        alert(textStatus);
	                    }
	                });
			//處理修改的下拉選單unit_id
	      			  $.ajax({
	                    url : "product.do",
	                    type : "POST",
	                    cache : false,
	                    delay : 1500,
	                    data : {
	                    	action : "search_product_data",
	                    	identity : "ID",
	                        term : ''
	                    },
	                    success: function(data) {
	                    	var json_obj = $.parseJSON(data);
							$.map(json_obj, function (item) {
	                    		if (item.unit_name != '') {
	                    			$("#select_update_unit_id").append("<option value='" + item.unit_name + "'>" + item.unit_name + "</option>");
	                    		}
	                          })
	                    },
	                    error: function(XMLHttpRequest, textStatus, errorThrown) {
	                        alert(textStatus);
	                    }
	                });
			//處理修改的下拉選單type_id
	     			  $.ajax({
	                    url : "product.do",
	                    type : "POST",
	                    cache : false,
	                    delay : 1500,
	                    data : {
	                    	action : "search_product_data",
	                    	identity : "ID2",
	                        term : ''
	                    },
	                    success: function(data) {
	                    	var json_obj = $.parseJSON(data);
							$.map(json_obj, function (item) {
	                    		if (item.type_name != '') {
	                    			$("#select_update_type_id").append("<option value='" + item.type_name + "'>" + item.type_name + "</option>");
	                    		}
	                          })
	                    },
	                    error: function(XMLHttpRequest, textStatus, errorThrown) {
	                        alert(textStatus);
	                    }
	                });
		
		//<!-- photo section jquery begin by Melvin -->
		////////////圖片的/////////////////////////
		'use strict';
	    // Change this to the location of your server-side upload handler:
	    var url = window.location.hostname === 'blueimp.github.io' ?
	                '//jquery-file-upload.appspot.com/' : '/VirtualBusiness/photo.do',
	        uploadButton = $('<button/>')
	            .addClass('btn btn-primary')
	            .prop('disabled', true)
	            .text('處理中...')
	            .on('click', function () {
	            	var $this = $(this),
	                    data = $this.data();
	                $this
	                    .off('click')
	                    .text('Abort')
	                    .on('click', function () {
	                        $this.remove();
	                        data.abort();
	                    });
	                data.submit().always(function () {
	                    $this.remove();
	                });
	            });
	                
	    $('#fileupload').fileupload({
	        url: url,
	        dataType: 'json',
	        autoUpload: false,
	        acceptFileTypes: /(\.|\/)(gif|jpe?g|png)$/i,
	        maxFileSize: 600000,
	        // Enable image resizing, except for Android and Opera,
	        // which actually support image resizing, but fail to
	        // send Blob objects via XHR requests:
	        disableImageResize: /Android(?!.*Chrome)|Opera/
	            .test(window.navigator.userAgent),
	        previewMaxWidth: 200,
	        previewMaxHeight: 200,
	        previewCrop: true
	    }).on('fileuploadadd', function (e, data) {
	        data.context = $('<div/>').appendTo('#files');
	        $.each(data.files, function (index, file) {
	            var node = $('<p/>')
	                    .append($('<span/>').text(file.name));
	            if (!index) {
	                node
	                    .append('<br>')
	                    .append(uploadButton.clone(true).data(data));
	            }
	            node.appendTo(data.context);
	            $("#photo").val(file.name);
	        });
	    }).on('fileuploadprocessalways', function (e, data) {
	        var index = data.index,
	            file = data.files[index],
	            node = $(data.context.children()[index]);
	        if (file.preview) {
	            node
	                .prepend('<br>')
	                .prepend(file.preview);
	        }
	        if (file.error) {
	            node
	                .append('<br>')
	                .append($('<span class="text-danger"/>').text(file.error));
	        }
	        if (index + 1 === data.files.length) {
	            data.context.find('button')
	                .text('上傳')
	                .prop('disabled', !!data.files.error);
	        }
	    }).on('fileuploadprogressall', function (e, data) {
	        var progress = parseInt(data.loaded / data.total * 100, 10);
	        $('#progress .progress-bar').css(
	            'width',
	            progress + '%'
	        );
	    }).on('fileuploaddone', function (e, data) {
	    	console.log(data.result.files);
	    	console.log("fileuploaddone");
	        $.each(data.result.files, function (index, file) {
	        	$("#photo").val(file.name);
	            if (file.url) {
	                var link = $('<a>')
	                    .attr('target', '_blank')
	                    .prop('href', file.url);
	                $(data.context.children()[index])
	                    .wrap(link);
	            } else if (file.error) {
	                var error = $('<span class="text-danger"/>').text(file.error);
	                $(data.context.children()[index])
	                    .append('<br>')
	                    .append(error);
	            }
	        });
	    }).on('fileuploadfail', function (e, data) {
	    	$.each(data.files, function (index) {
	            var error = $('<span class="text-danger"/>').text('File upload failed.');
	            $(data.context.children()[index])
	                .append('<br>')
	                .append(error);
	        });
	    }).prop('disabled', !$.support.fileInput)
	        .parent().addClass($.support.fileInput ? undefined : 'disabled');
	    
	    $('#fileupload2').fileupload({
	        url: url,
	        dataType: 'json',
	        autoUpload: false,
	        acceptFileTypes: /(\.|\/)(gif|jpe?g|png)$/i,
	        maxFileSize: 600000,
	        // Enable image resizing, except for Android and Opera,
	        // which actually support image resizing, but fail to
	        // send Blob objects via XHR requests:
	        disableImageResize: /Android(?!.*Chrome)|Opera/
	            .test(window.navigator.userAgent),
	        previewMaxWidth: 200,
	        previewMaxHeight: 200,
	        previewCrop: true
	    }).on('fileuploadadd', function (e, data) {
	        data.context = $('<div/>').appendTo('#files2');
	        $.each(data.files, function (index, file) {
	            var node = $('<p/>')
	                    .append($('<span/>').text(file.name));
	            if (!index) {
	                node
	                    .append('<br>')
	                    .append(uploadButton.clone(true).data(data));
	            }
	            node.appendTo(data.context);
// 	            alert('test');
	            $("#photo2").val(file.name);
  	        });
	    }).on('fileuploadprocessalways', function (e, data) {
	        var index = data.index,
	            file = data.files[index],
	            node = $(data.context.children()[index]);
	        if (file.preview) {
	            node
	                .prepend('<br>')
	                .prepend(file.preview);
	        }
	        if (file.error) {
	            node
	                .append('<br>')
	                .append($('<span class="text-danger"/>').text(file.error));
	        }
	        if (index + 1 === data.files.length) {
	            data.context.find('button')
	                .text('上傳')
	                .prop('disabled', !!data.files.error);
	        }
	    }).on('fileuploadprogressall', function (e, data) {
	        var progress = parseInt(data.loaded / data.total * 100, 10);
	        $('#progress .progress-bar').css(
	            'width',
	            progress + '%'
	        );
	    }).on('fileuploaddone', function (e, data) {
	    	console.log(data.result.files);
	    	
	        $.each(data.result.files, function (index, file) {
	        	$("#photo2").val(file.name);
	            if (file.url) {
	                var link = $('<a>')
	                    .attr('target', '_blank')
	                    .prop('href', file.url);
	                $(data.context.children()[index])
	                    .wrap(link);
	            } else if (file.error) {
	                var error = $('<span class="text-danger"/>').text(file.error);
	                $(data.context.children()[index])
	                    .append('<br>')
	                    .append(error);
	            }
	        });
	    }).on('fileuploadfail', function (e, data) {
	    	$.each(data.files, function (index) {
	            var error = $('<span class="text-danger"/>').text('File upload failed.');
	            $(data.context.children()[index])
	                .append('<br>')
	                .append(error);
	        });
	    }).prop('disabled', !$.support.fileInput)
	        .parent().addClass($.support.fileInput ? undefined : 'disabled');
	    
        
	    $('#fileupload-update').fileupload({
	        url: url,
	        dataType: 'json',
	        autoUpload: false,
	        acceptFileTypes: /(\.|\/)(gif|jpe?g|png)$/i,
	        maxFileSize: 600000,
	        // Enable image resizing, except for Android and Opera,
	        // which actually support image resizing, but fail to
	        // send Blob objects via XHR requests:
	        disableImageResize: /Android(?!.*Chrome)|Opera/
	            .test(window.navigator.userAgent),
	        previewMaxWidth: 200,
	        previewMaxHeight: 200,
	        previewCrop: true
	    }).on('fileuploadadd', function (e, data) {
	        data.context = $('<div/>').appendTo('#files-update');
	        $.each(data.files, function (index, file) {
	            var node = $('<p/>')
	                    .append($('<span/>').text(file.name));
	            if (!index) {
	                node
	                    .append('<br>')
	                    .append(uploadButton.clone(true).data(data));
	            }
	            node.appendTo(data.context);
	            $("#photo-update").val(file.name);
	        });
	    }).on('fileuploadprocessalways', function (e, data) {
	        var index = data.index,
	            file = data.files[index],
	            node = $(data.context.children()[index]);
	        if (file.preview) {
	            node
	                .prepend('<br>')
	                .prepend(file.preview);
	        }
	        if (file.error) {
	            node
	                .append('<br>')
	                .append($('<span class="text-danger"/>').text(file.error));
	        }
	        if (index + 1 === data.files.length) {
	            data.context.find('button')
	                .text('上傳')
	                .prop('disabled', !!data.files.error);
	        }
	    }).on('fileuploadprogressall', function (e, data) {
	        var progress = parseInt(data.loaded / data.total * 100, 10);
	        $('#progress .progress-bar').css(
	            'width',
	            progress + '%'
	        );
	    }).on('fileuploaddone', function (e, data) {
	    	console.log("fileuploaddone");
	        $.each(data.result.files, function (index, file) {
	        	$("#photo-update").val(file.name);
	            if (file.url) {
	                var link = $('<a>')
	                    .attr('target', '_blank')
	                    .prop('href', file.url);
	                $(data.context.children()[index])
	                    .wrap(link);
	            } else if (file.error) {
	                var error = $('<span class="text-danger"/>').text(file.error);
	                $(data.context.children()[index])
	                    .append('<br>')
	                    .append(error);
	            }
	        });
	    }).on('fileuploadfail', function (e, data) {
	    	$.each(data.files, function (index) {
	            var error = $('<span class="text-danger"/>').text('File upload failed.');
	            $(data.context.children()[index])
	                .append('<br>')
	                .append(error);
	        });
	    }).prop('disabled', !$.support.fileInput)
	        .parent().addClass($.support.fileInput ? undefined : 'disabled');             
	    $('#fileupload2-update').fileupload({
	        url: url,
	        dataType: 'json',
	        autoUpload: false,
	        acceptFileTypes: /(\.|\/)(gif|jpe?g|png)$/i,
	        maxFileSize: 600000,
	        // Enable image resizing, except for Android and Opera,
	        // which actually support image resizing, but fail to
	        // send Blob objects via XHR requests:
	        disableImageResize: /Android(?!.*Chrome)|Opera/
	            .test(window.navigator.userAgent),
	        previewMaxWidth: 200,
	        previewMaxHeight: 200,
	        previewCrop: true
	    }).on('fileuploadadd', function (e, data) {
	        data.context = $('<div/>').appendTo('#files2-update');
	        $.each(data.files, function (index, file) {
	            var node = $('<p/>')
	                    .append($('<span/>').text(file.name));
	            if (!index) {
	                node
	                    .append('<br>')
	                    .append(uploadButton.clone(true).data(data));
	            }
	            node.appendTo(data.context);
// 	            alert('test');
	            $("#photo1-update").val(file.name);
	        });
	    }).on('fileuploadprocessalways', function (e, data) {
	        var index = data.index,
	            file = data.files[index],
	            node = $(data.context.children()[index]);
	        if (file.preview) {
	            node
	                .prepend('<br>')
	                .prepend(file.preview);
	        }
	        if (file.error) {
	            node
	                .append('<br>')
	                .append($('<span class="text-danger"/>').text(file.error));
	        }
	        if (index + 1 === data.files.length) {
	            data.context.find('button')
	                .text('上傳')
	                .prop('disabled', !!data.files.error);
	        }
	    }).on('fileuploadprogressall', function (e, data) {
	        var progress = parseInt(data.loaded / data.total * 100, 10);
	        $('#progress .progress-bar').css(
	            'width',
	            progress + '%'
	        );
	    }).on('fileuploaddone', function (e, data) {
	    	console.log(data.result.files);
	    	
	        $.each(data.result.files, function (index, file) {
	        	
	        	$("#photo1-update").val(file.name);  
	            if (file.url) {
	                var link = $('<a>')
	                    .attr('target', '_blank')
	                    .prop('href', file.url);
	                $(data.context.children()[index])
	                    .wrap(link);
	            } else if (file.error) {
	                var error = $('<span class="text-danger"/>').text(file.error);
	                $(data.context.children()[index])
	                    .append('<br>')
	                    .append(error);
	            }
	        });
	    }).on('fileuploadfail', function (e, data) {
	    	$.each(data.files, function (index) {
	            var error = $('<span class="text-danger"/>').text('File upload failed.');
	            $(data.context.children()[index])
	                .append('<br>')
	                .append(error);
	        });
	    }).prop('disabled', !$.support.fileInput)
	        .parent().addClass($.support.fileInput ? undefined : 'disabled');
	    //<!-- photo section jquery end by Melvin -->
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
	    
	    
	});	
</script>

		<div class="datalistWrap">
			<!--對話窗樣式-確認 -->
			<div id="dialog-confirm" title="確認刪除資料嗎?" style="display:none;">
				<p>是否確認刪除該筆資料</p>
			</div>
			<!--對話窗樣式-修改 -->
			<div id="dialog-form-update" title="修改產品資料" style="display:none;">
				<form name="update-dialog-form-post" id="update-dialog-form-post" style="display:inline"	>
					<fieldset>
				<table class="result-table">
					<tbody>
						<tr>
							<td>自訂產品ID:</td><td><input type="text" id="c_p_id2" name="c_product_id"/></td>
							<td>廠商名稱:</td><td><input type="text" name="supply_name"/></td>
						</tr><tr>
							<td>產品類別:</td><td><select id="select_update_type_id" name="select_update_type_id"></select></td>
							<td>產品單位:</td><td><select id="select_update_unit_id" name="select_update_unit_id"></select></td>
						</tr><tr>
							<td>&nbsp;產品名稱:</td><td><input type="text" name="product_name"  ></td>
							<td>產品說明:</td><td><input type="text" name="description"/></td>
						</tr><tr>
							<td>成本:</td><td><input type="text" name="cost" /></td>
							<td>售價:</td><td><input type="text" name="price" /></td>
						</tr><tr>
							<td>安全庫存:</td><td><input type="text" name="keep_stock" /></td>
							<td>條碼:<br><br><input id="same2" type="checkbox" style="position:static;" 
							onclick="if($('#same2').prop('checked')){$('#edit_barcode').val($('#c_p_id2').val());}else{$('#new_barcode').val('');}">同自定ID</td><td><input type="text" id="edit_barcode" name="barcode"/></td>
						</tr>
   		         	  </tbody>
   		         	  </table>		
					</fieldset>
				</form>
				<!-- photo section begin by Melvin -->
					<table border="0">
					<tbody>
					<tr>
						<td>
							<h6>產品圖片:</h6>
						</td>
						<td>
							<span class="btn btn-success fileinput-button btn-primary" style="padding: 6px 12px;border-radius: 5px;">
							<span><font color="white">+&nbsp;</font>瀏覽<font color="red">(最大500K)</font></span>
							<input id="fileupload-update" type="file" name="files-update[]">
						<br>
							</span>
							<div id="files-update" class="files" ></div>
               			</td>
               		 	</tr>	
               		<tr>
              			 <td>	
               				<h6>產品圖片2:&nbsp;&nbsp;</h6>
               			 </td>
              			 <td>	
                             <span class="btn btn-success fileinput-button btn-primary" style="padding: 6px 12px;border-radius: 5px;">
					         <span><font color="white">+&nbsp;</font>瀏覽<font color="red">(最大500K)</font></span>
					         <input id="fileupload2-update" type="file" name="files2-update[]">
					     <br>
					         </span>
					         <div id="files2-update" class="files"></div>        
               			</td>
               			</tr>	
               	  		</tbody>
               	  </table>		
               	<!-- photo section end by Melvin -->
               	<img id="product-photo" src="">
               	<img id="product-photo1" src="">
 
			</div>
			<!--對話窗樣式-新增 -->
			<div id="dialog-form-insert" title="新增產品資料" style="display:none;">
				<form name="insert-dialog-form-post" id="insert-dialog-form-post" style="display:inline">
					<fieldset>
					<table class="result-table">
					<tbody>
						<tr>
							<td>自訂產品ID:</td><td><input type="text" id="c_p_id"name="c_product_id"/></td>
							<td>廠商名稱:</td><td><input type="text" name="supply_name"/></td>
						</tr><tr>
							<td>產品類別:</td><td><select id="select_insert_type_id" name="select_insert_type_id"></select></td>
							<td>產品單位:</td><td><select id="select_insert_unit_id" name="select_insert_unit_id"></select></td>
						</tr><tr>
							<td>&nbsp;產品名稱:</td><td><input type="text" name="product_name"  ></td>
							<td>產品說明:</td><td><input type="text" name="description"/></td>
						</tr><tr>
							<td>成本:</td><td><input type="text" name="cost" /></td>
							<td>售價:</td><td><input type="text" name="price" /></td>
						</tr><tr>
							<td>安全庫存:</td><td><input type="text" name="keep_stock" /></td>
							<td>條碼:<br><br><input id="same" type="checkbox" style="position:static;" 
							onclick="if($('#same').prop('checked')){$('#new_barcode').val($('#c_p_id').val());}else{$('#new_barcode').val('');}">同自定ID</td><td><input type="text" id="new_barcode" name="barcode"/></td>
						</tr>
   		         	  </tbody>
   		         	  </table>		
					</fieldset>
				</form>
				<!-- photo section begin by Melvin -->
					<table border="0">
					<tbody>
					<tr>
						<td>
							<h6>產品圖片:</h6>
						</td>
						<td>
							<span class="btn btn-success fileinput-button btn-primary" style="padding: 6px 12px;border-radius: 5px;">
							<span><font color="white">+&nbsp;</font>瀏覽<font color="red">(最大500K)</font></span>
							<input id="fileupload" type="file" name="files[]">
						<br>
							</span>
							<div id="files" class="files" ></div>
               			</td>
               		 	</tr>	
               		<tr>
              			 <td>	
               				<h6>產品圖片2:&nbsp;&nbsp;</h6>
               			 </td>
              			 <td>	
                             <span class="btn btn-success fileinput-button btn-primary" style="padding: 6px 12px;border-radius: 5px;">
					         <span><font color="white">+&nbsp;</font>瀏覽<font color="red">(最大500K)</font></span>
					         <input id="fileupload2" type="file" name="files2[]">
					     <br>
					         </span>
					         <div id="files2" class="files"></div>        
               			</td>
               			</tr>	
               	  		</tbody>
               	  </table>		
               	<!-- photo section end by Melvin -->
<!--                	<img src="/VirtualBusiness/image.do?picname=a0001.jpg" width="200" height="200"> -->
<!--                	<img src="/VirtualBusiness/image.do?picname=a0002.jpg" width="200" height="200"> -->
			</div>
		<div class="input-field-wrap">
			<div class="form-wrap">
				<div class="form-row">
					<label for="">
						<span class="block-label">廠商名稱查詢</span>
						<input type="text" id="searh_product_name" name="searh_product_name"></input>
					</label>
					<button class="btn btn-darkblue" id="searh-sale">查詢</button>
				</div>
				<div class="btn-row">
					<button class="btn btn-exec btn-wide" id="create-sale">新增商品資料</button>
				</div>
			</div><!-- /.form-wrap -->
		</div>
			
			<!-- 第一列 -->
<!-- 			<div class="row" align="center"> -->
<!-- 				<div id="sales-serah-create-contain" class="ui-widget"> -->
<!-- 					<table id="sales-serah-create"> -->
<!-- 						<thead> -->
<!-- 							<tr> -->
<!-- 								<td><input type="text" id="searh_product_name" name="searh_product_name" placeholder="請輸入廠商名稱查詢"></td> -->
<!-- 								<th> -->
<!-- 									<button id="searh-sale">查詢</button> -->
<!-- 								</th> -->
<!-- 							</tr> -->
<!-- 						</thead> -->
<!-- 					</table> -->
<!-- 				</div> -->
<!-- 			</div>	 -->
			<!-- 第二列 -->
<!-- 			<div class="row" align="center"> -->
<!-- 				<div class="ui-widget"> -->
<!-- 					<button id="create-sale">新增產品資料</button> -->
<!-- 				</div> -->
<!-- 			</div>						 -->
			<!-- 第三列 -->
			<div class="row search-result-wrap" align="center">
				<div id="sales-contain" class="ui-widget" style="display:none">
					<table id="sales" class="result-table">
						<thead>
							<tr class="ui-widget-header">
								<th>自訂產品ID</th>
								<th>產品名稱</th>
								<th>廠商名稱</th>
								<th>產品類別</th>
								<th>產品單位</th>
								<th>成本</th>
								<th>售價</th>
								<th>安全庫存</th>
<!-- 								<th><p style="width:100px;">產品圖片名稱</p></th> -->
<!-- 								<th><p style="width:120px;">產品圖片名稱2</p></th> -->
								<th>產品說明</th>
								<th>條碼</th>
								<th>功能</th>
							</tr>
						</thead>
						<tbody>
						</tbody>
					</table>
				</div>
				<span class="validateTips"> </span>
			</div>
			<!-- 4th -->
			<div class="row search-result-wrap" align="center">
				<div id="sales-contain22" class="ui-widget" style="display:none">
					<table id="sales22" class="result-table">
						<thead>
							<tr class="ui-widget-header">
								<th>自訂產品ID</th>
								<th>產品名稱</th>
								<th>數量</th>
								<th>售價</th>
								<th>功能</th>
							</tr>
						</thead>
						<tbody>
						</tbody>
					</table>
				</div>
				<span class="validateTips"> </span>
			</div>
		</div>
	</div>
<input type="text" id="photo" style="display:none"/>
<input type="text" id="photo1" style="display:none"/>
<input type="text" id="photo-update" style="display:none"/>
<input type="text" id="photo1-update" style="display:none"/>
<input type="text" id="bar_code_focus" style="display:none"/>
<div id="warning" style="display:none;"></div>
</body>
</html>