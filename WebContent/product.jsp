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

<!-- 圖片的 -->
<link rel="stylesheet" href="//netdna.bootstrapcdn.com/bootstrap/3.2.0/css/bootstrap.min.css">
<!-- Generic page styles -->
<link rel="stylesheet" href="css/photo/style.css">	
<!-- CSS to style the file input field as button and adjust the Bootstrap progress bars -->
<link rel="stylesheet" href="css/photo/jquery.fileupload.css">


<script src="//ajax.googleapis.com/ajax/libs/jquery/1.11.3/jquery.min.js"></script>
<!-- The jQuery UI widget factory, can be omitted if jQuery UI is already included -->
<script src="js/photo/vendor/jquery.ui.widget.js"></script>
<!-- The Load Image plugin is included for the preview images and image resizing functionality -->
<script src="//blueimp.github.io/JavaScript-Load-Image/js/load-image.all.min.js"></script>
<!-- The Canvas to Blob plugin is included for image resizing functionality -->
<script src="//blueimp.github.io/JavaScript-Canvas-to-Blob/js/canvas-to-blob.min.js"></script>
<!-- Bootstrap JS is not required, but included for the responsive demo navigation -->
<script src="//netdna.bootstrapcdn.com/bootstrap/3.2.0/js/bootstrap.min.js"></script>
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
<link rel="stylesheet" href="css/styles.css" />
<link href="<c:url value="css/css.css" />" rel="stylesheet">
<link href="<c:url value="css/jquery.dataTables.min.css" />" rel="stylesheet">
<link href="<c:url value="css/1.11.4/jquery-ui.css" />" rel="stylesheet">
<!-- <script type="text/javascript" src="js/jquery-1.10.2.js"></script> -->
<script type="text/javascript" src="js/jquery-1.11.4.js"></script>
<script type="text/javascript" src="js/jquery.dataTables.min.js"></script>
<script type="text/javascript" src="js/jquery-ui.min.js"></script>
<script src="//code.jquery.com/ui/1.11.4/jquery-ui.js"></script>
<script type="text/javascript" src="js/jquery.validate.min.js"></script>
<script type="text/javascript" src="js/additional-methods.min.js"></script>
<script type="text/javascript" src="js/messages_zh_TW.min.js"></script>

<script>
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
		$("#searh-sale").button().on("click",function(e) {
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
										+ "<td><button id='"+json_obj[i].product_id+"'value='"+ json_obj[i].product_id+ "'name='"+ json_obj[i].product_name
										+ "'class='btn_update'>修改</button>"
										+ "<button id='"+json_obj[i].product_id+"'value='"+ json_obj[i].product_id+ "'name='"+ json_obj[i].product_name
										+ "'class='btn_delete'>刪除</button></td></tr>";	
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
								$("#sales").dataTable({"language": {"url": "js/dataTables_zh-tw.txt"}});
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
							show : {
								effect : "blind",
								duration : 1000
							},
							hide : {
								effect : "explode",
								duration : 1000
							},
							width : 600,
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
																	+ "<td><button id='"+json_obj[i].product_id+"'value='"+ json_obj[i].product_id+ "'name='"+ json_obj[i].product_name
																	+ "'class='btn_update'>修改</button>"
																	+ "<button id='"+json_obj[i].product_id+"'value='"+ json_obj[i].product_id+ "'name='"+ json_obj[i].product_name
																	+ "'class='btn_delete'>刪除</button></td></tr>";	
															}
														});
														$("#sales").dataTable().fnDestroy();
														if(resultRunTime!=0){
															$("#sales-contain").show();
															$("#sales tbody").html(result_table);
															$("#sales").dataTable({"language": {"url": "js/dataTables_zh-tw.txt"}});
															$(".validateTips").text("");
														}else{
															$("#sales-contain").hide();
														}
													}
												});
												insert_dialog.dialog("close");
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
		})
		.css("width", "10%");
		//確認Dialog相關設定(刪除功能)
		confirm_dialog = $("#dialog-confirm").dialog({
			draggable : false,//防止拖曳
			resizable : false,//防止縮放
			autoOpen : false,
			height : 140,
			modal : true,
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
										+ "<td><button id='"+json_obj[i].c_product_id+"'value='"+ json_obj[i].product_id+ "'name='"+ json_obj[i].product_name
										+ "'class='btn_update'>修改</button>"
										+ "<button id='"+json_obj[i].c_product_id+"'value='"+ json_obj[i].product_id+ "'name='"+ json_obj[i].product_name
										+ "'class='btn_delete'>刪除</button></td></tr>";											
								}
							});
							$("#sales").dataTable().fnDestroy();
							if(resultRunTime!=0){
								$("#sales-contain").show();
								$("#sales tbody").html(result_table);
								$("#sales").dataTable({"language": {"url": "js/dataTables_zh-tw.txt"}});
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
		//修改Dialog相關設定
		update_dialog = $("#dialog-form-update").dialog({
			draggable : false,//防止拖曳
			resizable : false,//防止縮放
			autoOpen : false,
			width : 600,
			modal : true,
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
								photo : $("#dialog-form-update input[name='photo-update']").val(),
								photo1 : $("#dialog-form-update input[name='photo1-update']").val(),
								description : $("#dialog-form-update input[name='description']").val(),
								barcode : $("#dialog-form-update input[name='barcode']").val()
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
												+ "<td><button id='"+json_obj[i].product_id+"'value='"+ json_obj[i].product_id+ "'name='"+ json_obj[i].product_name
												+ "'class='btn_update'>修改</button>"
												+ "<button id='"+json_obj[i].product_id+"'value='"+ json_obj[i].product_id+ "'name='"+ json_obj[i].product_name
												+ "'class='btn_delete'>刪除</button></td></tr>";		
												
										}
									});
								}	
								$("#sales").dataTable().fnDestroy();
								if(resultRunTime!=0){
									$("#sales-contain").show();
									$("#sales tbody").html(result_table);
									$("#sales").dataTable({"language": {"url": "js/dataTables_zh-tw.txt"}});
									$(".validateTips").text("");
								}else{
									$("#sales-contain").hide();
								}
							}
						});
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
		})
// 		.css("font-size", "25px");;			
		//刪除事件聆聽 : 因為聆聽事件動態產生，所以採用delegate來批量處理，節省資源
		$("#sales").delegate(".btn_delete", "click", function() {
			uuid = $(this).val();
			product_id = $(this).attr("id");
			confirm_dialog.dialog("open");
		});
		//新增事件聆聽
		$("#create-sale").button().on("click", function() {
			insert_dialog.dialog("open");
		});
		//修改事件聆聽
		$("#sales").delegate(".btn_update", "click", function(e) {
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
										$("#dialog-form-update input[name='type_id']").val(json_obj[i].type_id);
										$("#dialog-form-update input[name='unit_id']").val(json_obj[i].unit_id);
										$("#dialog-form-update input[name='cost']").val(json_obj[i].cost);
										$("#dialog-form-update input[name='price']").val(json_obj[i].price);
										$("#dialog-form-update input[name='keep_stock']").val(json_obj[i].keep_stock);
										$("#dialog-form-update input[name='photo']").val(json_obj[i].photo);
										$("#dialog-form-update input[name='photo1']").val(json_obj[i].photo1);
										$("#dialog-form-update input[name='description']").val(json_obj[i].description);
										$("#dialog-form-update input[name='barcode']").val(json_obj[i].barcode);
										
										if (json_obj[i].photo != '') {
											$("#product-photo").attr("src", "/VirtualBusiness/image.do?picname=" + json_obj[i].photo);
										}
										if (json_obj[i].photo1 != '') {
											$("#product-photo1").attr("src", "/VirtualBusiness/image.do?picname=" + json_obj[i].photo1);
										}
// 										alert($("#product-photo").attr("src"));
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
		//預設表格隱藏
		$("#sales-contain").hide();
		
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
	        maxFileSize: 999000,
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
	        maxFileSize: 999000,
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
	        maxFileSize: 999000,
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
	    	console.log(data.result.files);
	    	console.log("fileuploaddone");
	    	
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
	        maxFileSize: 999000,
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
	
	    
	    
	    
	});	
</script>
</head>
<body>
	<div class="panel-title">
		<h2>商品管理</h2>
	</div>
	<div class="panel-content">
		<div class="datalistWrap">
			<!--對話窗樣式-確認 -->
			<div id="dialog-confirm" title="確認刪除資料嗎?">
				<p>是否確認刪除該筆資料</p>
			</div>
<!-- 			<img alt="" src="123.jpg"> -->
			<!--對話窗樣式-修改 -->
			<div id="dialog-form-update" title="修改產品資料">
				<form name="update-dialog-form-post" id="update-dialog-form-post" style="display:inline"	>
					<fieldset>
						<table border="0" height="600">
					<tbody>
						<tr><td><h6>自訂產品ID:&nbsp;&nbsp;</h6></td><td><input type="text" name="c_product_id"  placeholder="輸入自訂產品ID"/></td></tr>
<!-- 						<td> &nbsp;&nbsp;&nbsp;&nbsp; </td> -->
<!-- 						<td rowspan=10 > -->
<!-- 						</td> -->
<!-- 						</tr> -->
						<tr><td><h6>產品名稱:</h6></td><td><input type="text" name="product_name"  placeholder="輸入產品名稱"/></td><td><input type="hidden" id="photo-update" name="photo-update"  placeholder="輸入產品圖片名稱"/></td></tr>
						<tr><td><h6>廠商名稱:</h6></td><td><input type="text" name="supply_name"  placeholder="輸入廠商名稱"/></td><td><input type="hidden"  id="photo1-update" name="photo1-update"  placeholder="輸入產品圖片名稱2"/></td></tr>
						<tr><td><h6>產品類別:</h6></td>
						<td>
							<select id="select_update_type_id" name="select_update_type_id">
							</select>
						</td>
<!-- 						<td> -->
<!-- 						<input type="text" id="insert_type_id" name="type_id_unit_id"  placeholder="輸入產品類別"/> -->
<!-- 						</td> -->
						</tr>
						<tr><td><h6>產品單位:</h6></td>
						<td>
							<select id="select_update_unit_id" name="select_update_unit_id">
							</select>
						</td>
						</tr>	
<!-- 						<td> -->
<!-- 						<input type="text" id="insert_unit_id" name="unit_id"  placeholder="輸入產品單位"/></td></tr> -->
						<tr><td><h6>成本:</h6></td><td><input type="text" name="cost"  placeholder="輸入成本"/></td></tr>
						<tr><td><h6>售價:</h6></td><td><input type="text" name="price"  placeholder="輸入售價"/></td></tr>
						<tr><td><h6>安全庫存:</h6></td><td><input type="text" name="keep_stock"  placeholder="輸入安全庫存"/></td></tr>
						<tr><td><h6>產品說明:</h6></td><td><input type="text" name="description"  placeholder="輸入產品說明"/></td></tr>
						<tr><td><h6>條碼:</h6></td><td><input type="text" name="barcode"  placeholder="輸入條碼"/></td></tr>
<!-- 						<tr><td><h6>產品圖片名稱:</h6></td><td><input type="text" id="photo" name="photo"  placeholder="輸入產品圖片名稱"/></td></tr> -->
<!-- 						<tr><td><h6>產品圖片名稱2:</h6></td><td><input type="text"  id="photo1" name="photo1"  placeholder="輸入產品圖片名稱2"/></td></tr> -->
   		         	  </tbody>
   		         	  </table>		
					</fieldset>
				</form>
				<!-- photo section begin by Melvin -->
					<table border="0">
					<tbody>
					<tr>
						<td>
							<h6>產品圖片名稱:&nbsp;&nbsp;</h6>
						</td>
						<td>
							<span class="btn btn-success fileinput-button">
							<i class="glyphicon glyphicon-plus"></i>
							<span>增加圖片1</span>
							<input id="fileupload-update" type="file" name="files-update[]">
						<br>
							</span>
							<div id="files-update" class="files" ></div>
               			</td>
               		 	</tr>	
               		<tr>
              			 <td>	
               				<h6>產品圖片名稱2:&nbsp;&nbsp;</h6>
               			 </td>
              			 <td>	
                             <span class="btn btn-success fileinput-button">
					         <i class="glyphicon glyphicon-plus"></i>
					         <span>增加圖片2</span>
					         <input id="fileupload2-update" type="file" name="files2-update[]">
					     <br>
					         </span>
					         <div id="files2-update" class="files"></div>        
               			</td>
               			</tr>	
               	  		</tbody>
               	  </table>		
               	<!-- photo section end by Melvin -->
               	<img id="product-photo" name="product-photo" src="" width="200" height="200">
               	<img id="product-photo1" src="" width="200" height="200">
 
			</div>
			<!--對話窗樣式-新增 -->
			<div id="dialog-form-insert" title="新增產品資料">
				<form name="insert-dialog-form-post" id="insert-dialog-form-post" style="display:inline">
					<fieldset>
					<table border="0" height="600">
					<tbody>
						<tr><td><h6>自訂產品ID:&nbsp;&nbsp;</h6></td><td><input type="text" name="c_product_id"  placeholder="輸入自訂產品ID"/></td></tr>
<!-- 						<td> &nbsp;&nbsp;&nbsp;&nbsp; </td> -->
<!-- 						<td rowspan=10 > -->
<!-- 						</td> -->
<!-- 						</tr> -->
						<tr><td><h6>產品名稱:</h6></td><td><input type="text" name="product_name"  placeholder="輸入產品名稱"/></td><td><input type="hidden" id="photo" name="photo"  placeholder="輸入產品圖片名稱"/></td></tr>
						<tr><td><h6>廠商名稱:</h6></td><td><input type="text" name="supply_name"  placeholder="輸入廠商名稱"/></td><td><input type="hidden"  id="photo2" name="photo1"  placeholder="輸入產品圖片名稱2"/></td></tr>
						<tr><td><h6>產品類別:</h6></td>
						<td>
							<select id="select_insert_type_id" name="select_insert_type_id">
							</select>
						</td>
<!-- 						<td> -->
<!-- 						<input type="text" id="insert_type_id" name="type_id_unit_id"  placeholder="輸入產品類別"/> -->
<!-- 						</td> -->
						</tr>
						<tr><td><h6>產品單位:</h6></td>
						<td>
							<select id="select_insert_unit_id" name="select_insert_unit_id">
							</select>
						</td>
						</tr>	
<!-- 						<td> -->
<!-- 						<input type="text" id="insert_unit_id" name="unit_id"  placeholder="輸入產品單位"/></td></tr> -->
						<tr><td><h6>成本:</h6></td><td><input type="text" name="cost"  placeholder="輸入成本"/></td></tr>
						<tr><td><h6>售價:</h6></td><td><input type="text" name="price"  placeholder="輸入售價"/></td></tr>
						<tr><td><h6>安全庫存:</h6></td><td><input type="text" name="keep_stock"  placeholder="輸入安全庫存"/></td></tr>
						<tr><td><h6>產品說明:</h6></td><td><input type="text" name="description"  placeholder="輸入產品說明"/></td></tr>
						<tr><td><h6>條碼:</h6></td><td><input type="text" name="barcode"  placeholder="輸入條碼"/></td></tr>
   		         	  </tbody>
   		         	  </table>		
					</fieldset>
				</form>
				<!-- photo section begin by Melvin -->
					<table border="0">
					<tbody>
					<tr>
						<td>
							<h6>產品圖片名稱:&nbsp;&nbsp;</h6>
						</td>
						<td>
							<span class="btn btn-success fileinput-button">
							<i class="glyphicon glyphicon-plus"></i>
							<span>增加圖片1</span>
							<input id="fileupload" type="file" name="files[]">
						<br>
							</span>
							<div id="files" class="files" ></div>
               			</td>
               		 	</tr>	
               		<tr>
              			 <td>	
               				<h6>產品圖片名稱2:&nbsp;&nbsp;</h6>
               			 </td>
              			 <td>	
                             <span class="btn btn-success fileinput-button">
					         <i class="glyphicon glyphicon-plus"></i>
					         <span>增加圖片2</span>
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
			<!-- 第一列 -->
			<div class="row" align="center">
				<div id="sales-serah-create-contain" class="ui-widget">
					<table id="sales-serah-create">
						<thead>
							<tr>
								<td><input type="text" id="searh_product_name" name="searh_product_name" placeholder="請輸入廠商名稱查詢"></td>
								<th>
									<button id="searh-sale">查詢</button>
								</th>
							</tr>
						</thead>
					</table>
				</div>
			</div>	
			<!-- 第二列 -->
			<div class="row" align="center">
				<div class="ui-widget">
					<button id="create-sale">新增產品資料</button>
				</div>
			</div>						
			<!-- 第三列 -->
			<div class="row" align="center">
				<div id="sales-contain" class="ui-widget">
					<table id="sales" class="ui-widget ui-widget-content">
						<thead>
							<tr class="ui-widget-header">
								<th><p style="width:80px;">自訂產品ID</p></th>
								<th><p style="width:80px;">產品名稱</p></th>
								<th><p style="width:80px;">廠商名稱</p></th>
								<th><p style="width:80px;">產品類別</p></th>
								<th><p style="width:80px;">產品單位</p></th>
								<th><p style="width:80px;">成本</p></th>
								<th><p style="width:80px;">售價</p></th>
								<th><p style="width:80px;">安全庫存</p></th>
<!-- 								<th><p style="width:100px;">產品圖片名稱</p></th> -->
<!-- 								<th><p style="width:120px;">產品圖片名稱2</p></th> -->
								<th><p style="width:80px;">產品說明</p></th>
								<th><p style="width:80px;">條碼</p></th>
								<th><p style="width:80px;">功能</p></th>
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

</body>
</html>