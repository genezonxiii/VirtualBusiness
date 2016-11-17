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
<style type="text/css">
	table.form-table {
	 	border-spacing: 10px 8px !important; 
	}
/* 	.warning_msg{ */
/* 		position:absolute; */
/* 		left:calc(47%); */
/* 		z-index:9999; */
/* 	} */

	ul, li {
		margin: 0;
		padding: 0;
		list-style: none;
	}
	.abgne_tab {
		position:relative;
		top: -43px;
		left:5px;
		clear: left;
		width: 99.3%;
		margin: 10px 0;

	}
	ul.tabs {
		width: 100%;
		height: 32px;
/* 		border-bottom: 1px solid #999; */
/* 		border-left: 1px solid #999; */
	}
	ul.tabs li {
/* 	 	border-radius: 15px 15px 0 0; */
		float: left;
		height: 31px;
		line-height: 31px;
		overflow: hidden;
		position: relative;
		margin-bottom: -1px;	/* 讓 li 往下移來遮住 ul 的部份 border-bottom */
/* 		border: 1px solid #999; */
		border:0px solid #fff;
		border-left: none;
/* 		background: #e1e1e1; */
		background:#85b9fF;
	}
	ul.tabs li a {
		display: block;
		padding: 0 20px;
		color: #000;
		border: 1px solid #fff;
		text-decoration: none;
	}
	ul.tabs li a:hover {
		background: #5599FF;
	}
	ul.tabs li.active  {
		background: #EEF3F9;
/* 		#fff; */
/* 		border-bottom: 1px solid #fff; */
	}
	ul.tabs li.active a:hover {
/* 		background: #cEd3d9; */
		background: #EEF3F9;
	}
	div.tab_container {
		clear: left;
		width: 100%;
/* 		border: 1px solid #999; */
		border:0px solid #fff;
		border-top: none;
		background: #EEF3F9;
	}
	div.tab_container .tab_content {
/* 		padding: 20px; */
	}
</style>
</head>
<body>
	<jsp:include page="template.jsp" flush="true"/>
	<div class="content-wrap" >
		<div class='bdyplane' style="opacity:0">

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
<script type="text/javascript" src="js/photo/jquery-ui.js"></script>
<script type="text/javascript" src="js/jquery.validate.min.js"></script>
<script type="text/javascript" src="js/additional-methods.min.js"></script>
<script type="text/javascript" src="js/messages_zh_TW.min.js"></script>
<script type="text/javascript" src="js/jquery.scannerdetection.js"></script>

<script>
var c_product_id_tags=[];
var product_name_tags=[];
	var new_or_edit=0;
	var scan_exist=0;
	var information;
	//##############Draw_datatable##############
	//##############Draw_datatable##############
	//##############Draw_datatable##############
	function draw_product(info){
		$("#sales-contain").css({"opacity":"0"});
		$("#packages-contain").css({"opacity":"0"});
		warning_msg("---讀取中請稍候---");
// 		$(".warning_msg").remove();
// 		$("#sales").append('<div class="warning_msg">---讀取中請稍候---</div>');
// 		$("#packages").append('<div class="warning_msg">---讀取中請稍候---</div>');
		$.ajax({
			type : "POST",
			url : "product.do",
			data : info,
			success : function(result) {
				//console.log(result);
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
								//alert(json_obj[i].photo);
								var tmp=(json_obj[i].photo.length<1)?"無圖片":"<img src=./image.do?picname="+json_obj[i].photo+" style='max-width:100px;max-height:100px'>";
								var tmp1=(json_obj[i].photo1.length<1)?"無圖片":"<img src=./image.do?picname="+json_obj[i].photo1+" style='max-width:100px;max-height:100px'>";
								result_table 
								+= "<tr>"
								+ "<td name='"+ json_obj[i].c_product_id +"'>"+ json_obj[i].c_product_id+ "</td>"
								+ "<td name='"+ json_obj[i].product_name +"'>"+ json_obj[i].product_name+ "</td>"
//									+ "<td name='"+ json_obj[i].supply_id  +"'>"+ json_obj[i].supply_id + "</td>"
								+ "<td name='"+ json_obj[i].supply_name +"'>"+ json_obj[i].supply_name + "</td>"
								+ "<td name='"+ json_obj[i].type_id +"'>"+ json_obj[i].type_id+ "</td>"
								+ "<td name='"+ json_obj[i].unit_id +"'>"+ json_obj[i].unit_id+ "</td>"
								+ "<td name='"+ json_obj[i].cost +"'>"+ money(json_obj[i].cost)+ "</td>"
								+ "<td name='"+ json_obj[i].price +"'>"+ money(json_obj[i].price)+ "</td>"
								+ "<td name='"+ json_obj[i].keep_stock +"'>"+ json_obj[i].keep_stock+ "</td>"
								+ "<td name='"+ json_obj[i].photo+"'>"+tmp+"</td>"
								+ "<td name='"+ json_obj[i].photo1 +"'>"+tmp1+"</td>"
								+ "<td name='"+ json_obj[i].description +"'>"+ json_obj[i].description+ "</td>"
								+ "<td name='"+ json_obj[i].barcode +"'>"+ json_obj[i].barcode+ "</td>"
								+ "<td><div class='table-row-func btn-in-table btn-gray'><i class='fa fa-ellipsis-h'></i>"
								+ "	<div class='table-function-list'>"
								+ "		<button class='btn-in-table btn-darkblue btn_update' title='修改' id='"+json_obj[i].product_id+"'value='"+ json_obj[i].product_id+ "'name='"+ json_obj[i].product_name+"'><i class='fa fa-pencil'></i></button>"
								+ "		<button class='btn-in-table btn-alert btn_delete' title='刪除' id='"+json_obj[i].product_id+"'value='"+ json_obj[i].product_id+ "'name='"+ json_obj[i].product_name+"' ><i class='fa fa-trash'></i></button>"
								+ "	</div></div></td></tr>";
							}//"src", "./image.do?picname=" + json_obj[i].photo
						});
					}
					$("#sales").dataTable().fnDestroy();
					if(resultRunTime!=0&&json_obj[resultRunTime-1].message=="驗證通過"){
						$("#sales-contain").show();
						$("#sales tbody").html(result_table);
						$("#sales").dataTable({"language": {"url": "js/dataTables_zh-tw.txt"},"order": []});
						tooltip('btn_update');
						tooltip('btn_delete');
						$("#sales").find("td").css("text-align","center");
						$("#sales").find("th").css("text-align","center");
						$("#sales tbody td:nth-child(2)").css("text-align", "left");
						$("#sales tbody td:nth-child(3)").css("text-align", "left");
						$("#sales tbody td:nth-child(4)").css("text-align", "left");
						$("#sales tbody td:nth-child(5)").css("text-align", "left");
						$("#sales tbody td:nth-child(11)").css("text-align", "left");
						$("#sale tbody tr").css("line-height", "20px");
						$("#sales-contain").animate({"opacity":"0.01"},1);
						$("#sales-contain").animate({"opacity":"1"},300);
						warning_msg("");
// 						$("#sales .warning_msg").remove();
					}
					if(resultRunTime<2){
						$("#sales-contain").hide();
						warning_msg("---查無此結果---");
// 						$("#sales").append('<div class="warning_msg">---查無此結果---</div>');
					}
				}
			});
// 		skypeforbusiness
// 		$.ajax({
// 			type : "POST",
// 			url : "productpackage.do",
// 			data :{ action : "search"},
// 			success : function(result) {
// 				alert(result);
// 				var result_table = "";
// 				var json_obj = $.parseJSON(result);
// 				$.each(json_obj,function(i, item) {
// 					var json_obj2 = $.parseJSON(json_obj[i].content);
					
// 					result_table+="<tr>"+
// 					"<td>"+json_obj[i].c_product_id+"</td>"+
// 					"<td>"+json_obj[i].product_name+"</td>"+
// 					"<td>"+json_obj[i].supply_name+"</td>"+
// 					"<td>"+json_obj[i].price+"</td>"+
// 					"<td>"+json_obj[i].type_id+"</td>"+
// 					"<td>"+json_obj[i].barcode+"</td>"+
// 					"<td>"+json_obj[i].description+"</td>"+
// 					"<td>"+json_obj[i].content+
// 					"</td></tr>";
// 				});
// 				$("#packages").dataTable().fnDestroy();
// 				if(json_obj.length>0){
// 					$("#packages tbody").html(result_table);
// 					$("#packages").dataTable({"language": {"url": "js/dataTables_zh-tw.txt"},"order": []});
// 					$("#packages-contain").show();
// 					$("#packages-contain").animate({"opacity":"0.01"},1);
// 					$("#packages-contain").animate({"opacity":"1"},300);
// 					$("#packages .warning_msg").remove();
// 				}else{
// 					$("#packages-contain").hide();
// 					$("#packages").append('<div class="warning_msg">---查無組合包---</div>');
// 				}
// 			}
// 		});
	}
	//##############Scanner##############
	//##############Scanner##############
	//##############Scanner##############
	jQuery(document).ready(function($) {
	    $(window).scannerDetection();
	    $(window).bind('scannerDetectionComplete',function(e,data){
	    	e.preventDefault();
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
	    			$.ajax({url : "product.do", type : "POST", cache : false,
			            data : {
			            	action : "find_barcode",
			            	barcode : data.string,
			            },
			            success: function(result) {
			            	var json_obj2 = $.parseJSON(result);
			            	if(json_obj2.length>0){
			            		information={
										action : "search_name",
										product_name : json_obj2[0].product_name,
									};
			            		
			            		draw_product(information);
			            	}else{
			            		$("#sales-contain").hide();
								$(".validateTips").text("查無此結果");
			            	}
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
		
		$(".bdyplane").animate({"opacity":"1"});
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
				c_product_id : {maxlength : 40,required : true},
				product_name:{maxlength : 80,required : true},
				supply_name : {maxlength : 40,required : true},
				type_id: {required : true},
				unit_id: {required : true},
				cost: {required : true},
				price: {required : true},
				keep_stock: {required : true},
				description : {stringMaxLength : 200},
				barcode : {stringMaxLength : 40}
			}
		});
		var validator_update = $("#update-dialog-form-post").validate({
			rules : {
				c_product_id : {maxlength : 40,required : true},
				product_name:{maxlength : 80,required : true},
				supply_name : {maxlength : 40,required : true},
				type_id: {required : true},
				unit_id: {required : true},
				cost: {required : true},
				price: {required : true},
				keep_stock: {required : true},
				description : {stringMaxLength : 200},
				barcode : {stringMaxLength : 40}
			}
		});
		$("#searh-name").click(function(e) {
			e.preventDefault();
			information={
				action : "search_name",
				product_name : $("#searh_name").val(),
			};
			draw_product(information);
		});
		
		//自訂產品ID查詢相關設定
		$("#searh-sale").click(function(e) {
			e.preventDefault();
			information={
				action : "search",
				supply_name : $("input[name='searh_product_name'").val(),
			};
			draw_product(information);
		});
		//新增Dialog相關設定
		insert_dialog = $("#dialog-form-insert").dialog({
			draggable : false, resizable : false, autoOpen : false,
			height : "auto", width : "auto", modal : true,
			show : {effect : "blind",duration : 300},
			hide : {effect : "fade",duration : 300},
			open : function(event, ui) {$(this).parent().children().children('.ui-dialog-titlebar-close').hide();},
			buttons : [{
						id : "insert",
						text : "新增",
						click : function() {
							if ($('#insert-dialog-form-post').valid()) {
								information={
										action : "insert",
										c_product_id : $("#dialog-form-insert input[name='c_product_id']").val(),
										product_name : $("#dialog-form-insert input[name='product_name']").val(),
			// 							supply_id : $("#dialog-form-insert input[name='supply_id']").val(),
										supply_id : supply_id,
										supply_name : $("#dialog-form-insert input[name='supply_name']").val(),
										type_id : $("#dialog-form-insert select[name='select_insert_type_id']").val(),
										unit_id : $("#dialog-form-insert select[name='select_insert_unit_id']").val(),
										cost : $("#dialog-form-insert input[name='cost']").val(),
										price : $("#dialog-form-insert input[name='price']").val(),
										current_stock : $("#dialog-form-insert input[name='current_stock']").val(),
										keep_stock : $("#dialog-form-insert input[name='keep_stock']").val(),
										photo : $("#photo0").val(),
										photo1 : $("#photo1").val(),
			// 							photo : $("#dialog-form-insert input[name='photo']").val(),
			// 							photo1 : $("#dialog-form-insert input[name='photo1']").val(),
										description : $("#dialog-form-insert input[name='description']").val(),
										barcode : $("#dialog-form-insert input[name='barcode']").val(),
										ispackage : "0"
									};
								draw_product(information);
								
								insert_dialog.dialog("close");
								$("#insert-dialog-form-post").trigger("reset");
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
		}).css("width", "10%");
		$("#dialog-form-insert").show();
		
// 		skypeforbusiness
// 		$("#dialog-form-package").dialog({
// 			draggable : false, resizable : false, autoOpen : false,
// 			height : "auto", width : "auto", modal : true,
// 			show : {effect : "blind",duration : 300},
// 			hide : {effect : "fade",duration : 300},
// 			buttons : [{
// 						id : "insert",
// 						text : "新增",
// 						click : function() {
// 							//$("#sales tbody").html("<tr style='text-align:center;'><td rowspan='2'>123</td><td rowspan='2'>ABC組合包</td><td rowspan='2'>pershing</td><td rowspan='2' colspan='2'>組合包</td><td rowspan='2'>販售價格:<br>$123元</td><td rowspan='2' style=''>內含<img src='./images/hifi.png' style='height:50px;vertical-align:middle;' /></td><td>123</td><td>123</td><td>123</td><td>123</td><td>123</td><td>123</td></tr><tr><td>　</td></tr>"+$("#sales tbody").html());
// 							$("#dialog-form-package").dialog("close");
// 							var tmp='[{"package_id":"708d91ca-a71d-11e6-922d-005056af760c","parent_id":"b1eebd9b-a4bc-11e6-922d-005056af760c","product_id":"e3205e2a-809a-11e6-ac1c-005056af760c","quantity":"3","package_desc":"紅色三層派"},{"package_id":"708d91ca-a71d-11e6-922d-005056af760d","parent_id":"b1eebd9b-a4bc-11e6-922d-005056af760c","product_id":"e3205e2a-809a-11e6-ac1c-005056af760c","quantity":"2","package_desc":"黃色三層派"},{"package_id":"c3ef439b-a623-11e6-922d-005056af760c","parent_id":"b1eebd9b-a4bc-11e6-922d-005056af760c","product_id":"0a8b87bb-a256-11e6-922d-005056af760c","quantity":"1","package_desc":"#紅黃藍組合包#"}]';
// 							$.ajax({
// 								type : "POST",
// 								url : "productpackage.do",
// 								data :{
// 									action : "search",
// 									c_package_id : $("#dialog-form-package input[name='c_product_id']").val(),
// 									supply_name : $("#dialog-form-package input[name='supply_name']").val(),
// 									package_name: $("#dialog-form-package input[name='product_name']").val(),
// 									price: $("#dialog-form-package input[name='price']").val(),
// 									package_type: $("#dialog-form-package input[name='package_type']").val(),
// 									barcode: $("#dialog-form-package input[name='barcode']").val(),
// 									description: $("#dialog-form-package input[name='description']").val(),
// 									packagecontain: tmp  //$().val(),
// 								},
// 								success : function(result) {
// 									//alert($("#dialog-form-package input[name='c_product_id']").val());
// 									alert(result);
// 								}
// 						 	});
							
							
// 						}
// 				},{
// 						text : "取消",
// 						click : function() {
// 							$("#dialog-form-package").dialog("close");
// 						}
// 				}]
// 		});
// 		$("#dialog-form-package").show();
		
		//確認Dialog相關設定(刪除功能)
		confirm_dialog = $("#dialog-confirm").dialog({
			draggable : false, resizable : false, autoOpen : false,
			height : "auto", width : "auto", modal : true,
			show : {effect : "blind",duration : 300},
			hide : {effect : "fade",duration : 300},
			open : function(event, ui) {$(this).parent().children().children('.ui-dialog-titlebar-close').hide();},
			buttons : {
				"確認刪除" : function() {
					information={
						action : "delete",
						product_id : uuid
					};
					draw_product(information);
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
			open : function(event, ui) {$(this).parent().children().children('.ui-dialog-titlebar-close').hide();},
			buttons : [{
				text : "修改",
				click : function() {
					if ($('#update-dialog-form-post').valid()) {
						information={
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
							photo : $("#photo0-update").val(),//$("#dialog-form-update input[name='photo-update']").val(),
							photo1 : $("#photo1-update").val(),//$("#dialog-form-update input[name='photo1-update']").val(),
							description : $("#dialog-form-update input[name='description']").val(),
							barcode : $("#dialog-form-update input[name='barcode']").val(),
							ispackage : "0"
						};
						draw_product(information);
						$("#update-dialog-form-post").trigger("reset");
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
			
			$("#dialog-confirm").html("");
			$("#dialog-confirm").html("<table class='dialog-table'>"+
				"<tr><td>商品名稱：</td><td><span class='delete_msg'>'"+$(this).parents("tr").find("td:nth-child(2)").attr("name")+"'</span></td></tr>"+
				"<tr><td>進貨來源：</td><td><span class='delete_msg'>'"+$(this).parents("tr").find("td:nth-child(3)").attr("name")+"'</span></td></tr>"+
				"</table>"
			);
			confirm_dialog.dialog("open");
		});
		//新增事件聆聽
		
// 		skypeforbusiness
// 		$("#create-package").click( function() {
// 			$("#dialog-form-package").dialog("open");
// 		});
		
		$("#create-sale").click( function() {
			new_or_edit=1;
			insert_dialog.dialog("open");
			$("#files").html('');
			$("#files2").html('');
			$("#photo0").val('');
			$("#photo1").val('');
			$("#new_barcode").focus();
			scan_exist=1;
			if(!scan_exist){$("#warning").dialog("open");};
		});
		
		//修改事件聆聽
		$("#sales").delegate(".btn_update", "click", function(e) {
			new_or_edit=2;
			e.preventDefault();
			$("#files-update").html('');
			$("#files2-update").html('');
			$("#photo0-update").val('');
			$("#photo1-update").val('');
// 			$("").val();
// 			$("#photo").val();
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
										$("#dialog-form-update input[name='tmp_cost']").val(json_obj[i].cost);
										$("#dialog-form-update input[name='cost']").val(json_obj[i].cost);
										$("#update_exchange_cost").html(currency_unit($("#update_currency").find("option:selected").text())+$("#dialog-form-update input[name='tmp_cost']").val()+" x "+$("#update_currency").val()+" = NT$"+$("#dialog-form-update input[name='cost']").val());
										$("#dialog-form-update input[name='tmp_price']").val(json_obj[i].price);
										$("#dialog-form-update input[name='price']").val(json_obj[i].price);
										$("#update_exchange_price").html(currency_unit($("#update_currency").find("option:selected").text())+$("#dialog-form-update input[name='tmp_price']").val()+" x "+$("#update_currency").val()+" = NT$"+$("#dialog-form-update input[name='price']").val());
										$("#dialog-form-update input[name='keep_stock']").val(json_obj[i].keep_stock);
										$("#dialog-form-update input[name='photo']").val(json_obj[i].photo);
										$("#dialog-form-update input[name='photo1']").val(json_obj[i].photo1);
										$("#dialog-form-update input[name='description']").val(json_obj[i].description);
										$("#dialog-form-update input[name='barcode']").val(json_obj[i].barcode);
										
										if (json_obj[i].photo != '') {
											$("#product-photo").attr("src", "./image.do?picname=" + json_obj[i].photo);
											$("#product-photo").attr("max-width","150");
											$("#product-photo").attr("alt",json_obj[i].photo);
										}else{
											$("#product-photo").attr("src","");
											$("#product-photo").attr("max-width","100");
											$("#product-photo").attr("alt","無");
										}
										if (json_obj[i].photo1 != '') {
											$("#product-photo1").attr("src", "./image.do?picname=" + json_obj[i].photo1);
											$("#product-photo1").attr("max-width","150");
											$("#product-photo1").attr("alt",json_obj[i].photo1);
										}else{
											$("#product-photo1").attr("src","");
											$("#product-photo1").attr("max-width","100");
											$("#product-photo1").attr("alt","無");
										}
									}
								}
							});
						}
					}
				});			
			update_dialog.dialog("open");
			$("#edit_barcode").focus();
		});
		//X_X
		$("#insert_supply_name").autocomplete({
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
  		$("#insert_supply_name").bind('focus', function(){  
  	    	$(this).attr("placeholder","輸入供應商名稱");
  	    	var eve=jQuery.Event("keydown");
  	    	eve.which=40;
  	      	$(this).trigger(eve);
  	    });
	    $('#insert_supply_name').bind('autocompleteselect', function (e, ui) {
	    	supply_id = ui.item.supply_id;
	    });
	    $("#update_supply_name").autocomplete({
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
  		$("#update_supply_name").bind('focus', function(){  
  	    	$(this).attr("placeholder","輸入供應商名稱");
  	    	var eve=jQuery.Event("keydown");
  	    	eve.which=40;
  	      	$(this).trigger(eve);
  	    } );
	    $('#update_supply_name').bind('autocompleteselect', function (e, ui) {
	    	supply_id = ui.item.supply_id;
	    });
	  //X_X
		//處理初始的查詢autocomplete//其實是supply@@@@
	       $("#searh_product_name").autocomplete({
	            minLength: 0,
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
	       $("#searh_name").autocomplete({
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
	       $("#searh_name").dblclick(function(event){ $("#searh_name").autocomplete({minLength: 0}); });
	       $("#searh_product_name").bind('focus', function(){   
	       	$(this).attr("placeholder","輸入供應商名稱");
	    	var eve=jQuery.Event("keydown");
	    	eve.which=40;
	      	$(this).trigger(eve);
	      	
	     } );
	    
			//處理新增修改的下拉選單unit_id
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
                   			$("#select_update_unit_id").append("<option value='" + item.unit_name + "'>" + item.unit_name + "</option>");
                   		}
                         })
                   },
                   error: function(XMLHttpRequest, textStatus, errorThrown) {
                       alert(textStatus);
                   }
               });
			//處理新增修改的下拉選單type_id
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
                    			$("#select_update_type_id").append("<option value='" + item.type_name + "'>" + item.type_name + "</option>");
                    		}
                          })
                    },
                    error: function(XMLHttpRequest, textStatus, errorThrown) {
                        alert(textStatus);
                    }
                });
		//<!-- photo section jquery begin by Melvin -->
		'use strict';
	    var url = window.location.hostname === 'blueimp.github.io' ?
	                '//jquery-file-upload.appspot.com/' : '/VirtualBusiness/photo.do',
	        uploadButton = $('<button/>')
	            .addClass('btn btn-primary')
	            .prop('disabled', true)
	            .text('處理中...')
	            .on('click', function (e) {
	            	e.preventDefault();
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
	            //$("#photo0").val(file.name);
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
	        $.each(data.result.files, function (index, file) {
	        	$("#photo0").val(file.name);/////////////////////////////////////
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
	        $.each(data.result.files, function (index, file) {
	        	$("#photo1").val(file.name);///////////////////////////////////////
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
	    
	    //////////////////////////////////////////////////////////////////
        
	    $('#fileupload-update').fileupload({
	        url: url,
	        dataType: 'json',
	        autoUpload: false,
	        acceptFileTypes: /(\.|\/)(gif|jpe?g|png)$/i,
	        maxFileSize: 600000,
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
	        	$("#photo0-update").val(file.name);///////////////////////////////////////
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
	        	
	        	$("#photo1-update").val(file.name);  /////////////////////////////////////
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
	    
	    $("#insert_currency").change(function(e){
	    	$(".currency1").html("("+$("#insert_currency").find("option:selected").text()+")");
	    	$("#dialog-form-insert input[name='cost']").val(Math.round($("#dialog-form-insert input[name='tmp_cost']").val()*$("#insert_currency").val()*10000) / 10000);
	    	$("#insert_exchange_cost").html(currency_unit($("#insert_currency").find("option:selected").text())+$("#dialog-form-insert input[name='tmp_cost']").val()+" x "+$("#insert_currency").val()+" = NT$"+$("#dialog-form-insert input[name='cost']").val());
	    	$("#dialog-form-insert input[name='price']").val(Math.round($("#dialog-form-insert input[name='tmp_price']").val()*$("#insert_currency").val()*10000) / 10000);
	    	$("#insert_exchange_price").html(currency_unit($("#insert_currency").find("option:selected").text())+$("#dialog-form-insert input[name='tmp_price']").val()+" x "+$("#insert_currency").val()+" = NT$"+$("#dialog-form-insert input[name='price']").val());
	    });
	    $("#dialog-form-insert input[name='tmp_cost']").change(function(e){
	    	$("#dialog-form-insert input[name='cost']").val(Math.round($("#dialog-form-insert input[name='tmp_cost']").val()*$("#insert_currency").val()*10000) / 10000);
	    	$("#insert_exchange_cost").html(currency_unit($("#insert_currency").find("option:selected").text())+$("#dialog-form-insert input[name='tmp_cost']").val()+" x "+$("#insert_currency").val()+" = NT$"+$("#dialog-form-insert input[name='cost']").val());
	    });
	    $("#dialog-form-insert input[name='tmp_price']").change(function(e){
	    	$("#dialog-form-insert input[name='price']").val(Math.round($("#dialog-form-insert input[name='tmp_price']").val()*$("#insert_currency").val()*10000) / 10000);
	    	$("#insert_exchange_price").html(currency_unit($("#insert_currency").find("option:selected").text())+$("#dialog-form-insert input[name='tmp_price']").val()+" x "+$("#insert_currency").val()+" = NT$"+$("#dialog-form-insert input[name='price']").val());
	    });
	    
	    $("#update_currency").change(function(e){
	    	$(".currency2").html("("+$("#update_currency").find("option:selected").text()+")");
	    	$("#dialog-form-update input[name='cost']").val(Math.round($("#dialog-form-update input[name='tmp_cost']").val()*$("#update_currency").val()*10000) / 10000);
	    	$("#update_exchange_cost").html(currency_unit($("#update_currency").find("option:selected").text())+$("#dialog-form-update input[name='tmp_cost']").val()+" x "+$("#update_currency").val()+" = NT$"+$("#dialog-form-update input[name='cost']").val());
	    	$("#dialog-form-update input[name='price']").val(Math.round($("#dialog-form-update input[name='tmp_price']").val()*$("#update_currency").val()*10000) / 10000);
	    	$("#update_exchange_price").html(currency_unit($("#update_currency").find("option:selected").text())+$("#dialog-form-update input[name='tmp_price']").val()+" x "+$("#update_currency").val()+" = NT$"+$("#dialog-form-update input[name='price']").val());
	    });
	    $("#dialog-form-update input[name='tmp_cost']").change(function(e){
	    	$("#dialog-form-update input[name='cost']").val(Math.round($("#dialog-form-update input[name='tmp_cost']").val()*$("#update_currency").val()*10000) / 10000);
	    	$("#update_exchange_cost").html(currency_unit($("#update_currency").find("option:selected").text())+$("#dialog-form-update input[name='tmp_cost']").val()+" x "+$("#update_currency").val()+" = NT$"+$("#dialog-form-update input[name='cost']").val());
	    });
	    $("#dialog-form-update input[name='tmp_price']").change(function(e){
	    	$("#dialog-form-update input[name='price']").val(Math.round($("#dialog-form-update input[name='tmp_price']").val()*$("#update_currency").val()*10000) / 10000);
	    	$("#update_exchange_price").html(currency_unit($("#update_currency").find("option:selected").text())+$("#dialog-form-update input[name='tmp_price']").val()+" x "+$("#update_currency").val()+" = NT$"+$("#dialog-form-update input[name='price']").val());
	    });
	    
	    $.ajax({
			type : "POST",
			url : "exchange.do",
			data : {action : "search"},
			success : function(result) {
				var json_obj = $.parseJSON(result);
				var result_select="<option value='1'>台幣</option>";// ="<select  name='' id=''><option value='1'>台幣</option>";
				$.each(json_obj,function(i, item) {
					result_select += "<option value='"+json_obj[i].exchange_rate+"'>"+json_obj[i].currency+"</option>";
				});
				$("#update_currency").html(result_select);
				$("#insert_currency").html(result_select);
			}
		});
		 
	    $.ajax({
			type : "POST",
			url : "exchange.do",
			data :{action : "search"},
			success : function(result) {
				var json_obj = $.parseJSON(result);
				$.each (json_obj, function (i,item) {
					if(json_obj[i].c_product_id!=null){
						c_product_id_tags[i]=json_obj[i].c_product_id;
					}
					if(json_obj[i].product_name!=null){
						product_name_tags[i]=json_obj[i].product_name;
					}
				});
			}
		});

	    auto_complete("package_product_name",product_name_tags);
	    
// 	    skypeforbusiness
// 	    var _showTab = 0;
// 		var $defaultLi = $('ul.tabs li').eq(_showTab).addClass('active');
// 		$($defaultLi.find('a').attr('href')).siblings().hide();
		
// 		// 當 li 頁籤被點擊時...
// 		// 若要改成滑鼠移到 li 頁籤就切換時, 把 click 改成 mouseover
// 		$('ul.tabs li').click(function() {
// 			// 找出 li 中的超連結 href(#id)
// 			var $this = $(this),
// 				_clickTab = $this.find('a').attr('href');
// 			// 把目前點擊到的 li 頁籤加上 .active
// 			// 並把兄弟元素中有 .active 的都移除 class
// 			$this.addClass('active').siblings('.active').removeClass('active');
// 			// 淡入相對應的內容並隱藏兄弟元素
// 			$(_clickTab).stop(false, true).fadeIn().siblings().hide();

// 			return false;
// 		}).find('a').focus(function(){
// 			this.blur();
// 		});
		
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
			<!--對話窗樣式-確認 -->
			<div id="dialog-confirm" title="是否刪除此商品?" style="display:none;">
				<p>是否確認刪除該筆資料</p>
			</div>
			<!--對話窗樣式-修改 -->
			<div id="dialog-form-update" title="修改產品資料" style="display:none;">
				<form name="update-dialog-form-post" id="update-dialog-form-post" style="display:inline">
					<fieldset>
				<table class="form-table">
					<tbody>
						<tr>
							<td>自訂產品ID：</td><td><input type="text" id="c_p_id2" name="c_product_id"/></td>
							<td>供應商名稱：</td><td><input type="text" id="update_supply_name" name="supply_name"/></td>
						</tr><tr>
							<td>產品類別：</td><td><select id="select_update_type_id" name="select_update_type_id"></select></td>
							<td>產品單位：</td><td><select id="select_update_unit_id" name="select_update_unit_id"></select></td>
						</tr><tr>
							<td>產品名稱：</td><td><input type="text" name="product_name"  ></td>
							<td>幣別：</td><td><select id='update_currency'></select></td>
						</tr><tr>
							<td>成本：<a class='currency2'></a></td><td><input type="text" name="tmp_cost" /></td>
							<td>售價：<a class='currency2'></a></td><td><input type="text" name="tmp_price" /></td>
						</tr><tr>
							<td>折合台幣成本：</td><td><a id='update_exchange_cost'>NT＄0 x 1 = NT$0</a><input type="hidden" name="cost" /></td>
							<td>折合台幣售價：</td><td><a id='update_exchange_price'>NT＄0 x 1 = NT$0</a><input type="hidden" name="price" /></td>
						</tr><tr>
							<td>安全庫存：</td><td><input type="text" name="keep_stock" /></td>
							<td>條碼：</td><td><input type="text" id="edit_barcode" name="barcode"/></td><td><input id="same2" type="checkbox" style="position:static;" 
							onclick="if($('#same2').prop('checked')){$('#edit_barcode').val($('#c_p_id2').val());}else{$('#edit_barcode').val('');}">同自定ID</td>
						</tr><tr>
							<td>產品說明：</td><td><input type="text" name="description"/></td>
						</tr>
   		         	  </tbody>
   		         	  </table>
					</fieldset>
				</form>
				<!-- photo section begin by Melvin -->
				<table class="form-table">
					<tbody>
					<tr>
						<td>產品圖片：</td>
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
              			 <td>產品圖片2：</td>
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
			
<!-- 			skypeforbusiness -->
<!-- 			<div id="dialog-form-package" title="新增產品包" style="display:none;"> -->
<!-- 				<form name="insert-dialog-form-post" id="insert-dialog-form-post_" style="display:inline"> -->
<!-- 					<fieldset> -->
<!-- 					<table class="form-table"> -->
<!-- 					<tbody> -->
<!-- 						<tr> -->
<!-- 							<td>自訂組合包ID：</td><td><input type="text" id="package_c_p_id_"name="c_product_id"/></td> -->
<!-- 							<td>供應商名稱：</td><td><input type="text" id="package_supply_name_" name="supply_name"/></td> -->
<!-- 						</tr><tr> -->
<!-- 							<td>組合包名稱：</td><td><input type="text" id="package_name"name="product_name"/></td> -->
<!-- 							<td>售價：</td><td><input type="text" id="package_price" name="price"/></td> -->
<!-- 						</tr><tr> -->
<!-- 							<td>產品類別：</td><td><input type="text" id="package_type"name="package_type"/></td> -->
<!-- 							<td>條碼(<input id="same3" type="checkbox" style="position:static;transform: scale(0.8);"  -->
<!-- 							onclick="if($('#same3').prop('checked')){$('#package_barcode').val($('#package_c_p_id_').val());}else{$('#package_barcode').val('');}">同ID)：</td><td><input type="text" id="package_barcode" name="barcode"/></td> -->
<!-- 						</tr><tr> -->
<!-- 							<td>說明：</td><td><input type="text" name="description"/></td> -->
<!-- 						</tr><tr> -->
<!-- 							<td colspan='4' style='width:100%;text-align:center;'> -->
<!-- 							<hr style='margin-left: 30px;margin-right: 10px;border-top:1px solid #ccc;'> -->
<!-- 							產品包內含 -->
<!-- 							</td> -->
<!-- 						</tr><tr> -->
<!-- 							<td colspan='4' style='width:100%;text-align:center;'> -->
<!-- 							商品自訂ID：<input type="text" style="width:100px;" id="package_product_name"name="product_name"/> -->
<!-- 							商品名稱：　<input type="text" id="package_product_name"name="product_name"/> -->
<!-- 							　數量：　<input type="text" id="package_amount" style="width:70px;"/> -->
<!-- 							　<a class='btn btn-primary' onclick='if($("#package_product_name").val().length>1){$("#package-contain tbody").append("<tr><td>某ID</td><td>"+$("#package_product_name").val()+"</td><td>"+$("#package_amount").val()+"</td><td>"+" <div class=\"table-row-func btn-in-table btn-gray\"><i class=\"fa fa-ellipsis-h\"></i><div class=\"table-function-list\"><button class=\"btn-in-table btn-alert btn_delete\" title=\"刪除\" onclick=\"$(this).parent().parent().parent().parent().remove();\" ><i class=\"fa fa-trash\"></i></button></div></div> "+"</td></tr>")}'>>></a> -->
<!-- 							</td> -->
<!-- 						</tr> -->
<!--    		         	  </tbody> -->
<!--    		         	  </table> -->
<!--    		        <div style='max-height:180px;overflow-y:auto;'> -->
<!--    		         	 	<table id="package-contain" style="margin:0 auto;width:75%;"class='result-table'> -->
<!-- 							<thead> -->
<!-- 								<tr> -->
<!-- 									<th>產品ID</th><th>產品名稱</th><th>數量</th><th>功能</th> -->
<!-- 								</tr> -->
<!-- 							</thead> -->
<!-- 							<tbody> -->
<!-- 								<tr> -->
<!-- 									<td>3345678</td><td>咖啡陶杯</td><td>1</td> -->
<!-- 									<td><div class='table-row-func btn-in-table btn-gray'><i class='fa fa-ellipsis-h'></i> -->
<!-- 									<div class='table-function-list'> -->
<!-- 										<button class='btn-in-table btn-alert btn_delete' title='刪除' id=''value=''onclick='$(this).parent().parent().parent().parent().remove();' ><i class='fa fa-trash'></i></button> -->
<!-- 									</div></div></td> -->
<!-- 								</tr> -->
<!-- 							</tbody> -->
<!-- 						</table> -->
<!-- 				</div> -->
<!-- 					</fieldset> -->
<!-- 				</form> -->
<!-- 			</div> -->
			<div id="dialog-form-insert" title="新增產品資料" style="display:none;">
				<form name="insert-dialog-form-post" id="insert-dialog-form-post" style="display:inline">
					<fieldset>
					<table class="form-table">
					<tbody>
						<tr>
							<td>自訂產品ID：</td><td><input type="text" id="c_p_id"name="c_product_id"/></td>
							<td>廠商名稱：</td><td><input type="text" id="insert_supply_name" name="supply_name"/></td>
						</tr><tr>
							<td>產品類別：</td><td><select id="select_insert_type_id" name="select_insert_type_id"></select></td>
							<td>產品單位：</td><td><select id="select_insert_unit_id" name="select_insert_unit_id"></select></td>
						</tr><tr>
							<td>產品名稱：</td><td><input type="text" name="product_name"  ></td>
							<td>幣別：</td><td><select id='insert_currency'></select></td>
						</tr><tr>
							<td>成本：<a class='currency1'></a></td><td><input type="text" name="tmp_cost" /></td>
							<td>售價：<a class='currency1'></a></td><td><input type="text" name="tmp_price" /></td>
						</tr><tr>
							<td>折合台幣成本：</td><td><a id='insert_exchange_cost'>NT＄0 x 1 = NT$0</a><input type="hidden" name="cost" /></td>
							<td>折合台幣售價：</td><td><a id='insert_exchange_price'>NT＄0 x 1 = NT$0</a><input type="hidden" name="price" /></td>
						</tr><tr>
							<td>庫存量：</td><td><input type="text" name="current_stock" /></td>
							<td>條碼：</td><td><input type="text" id="new_barcode" name="barcode"/></td><td><input id="same" type="checkbox" style="position:static;" onclick="if($('#same').prop('checked')){$('#new_barcode').val($('#c_p_id').val());}else{$('#new_barcode').val('');}">同自定ID</td>
						</tr><tr>
							<td>安全庫存：</td><td><input type="text" name="keep_stock" /></td>
							<td>產品說明：</td><td><input type="text" name="description"/></td>
						</tr>
   		         	  </tbody>
   		         	  </table>		
					</fieldset>
				</form>
				<!-- photo section begin by Melvin -->
				<table class='form-table'>
					<tbody>
						<tr>
							<td>產品圖片：</td>
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
	              			 <td>產品圖片2：</td>
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
						<span class="block-label">供應商名稱查詢</span>
						<input type="text" id="searh_product_name" name="searh_product_name"></input>
					</label>
					<button class="btn btn-darkblue" id="searh-sale">查詢</button>
				</div>
				<div class="form-row">
					<label for="">
						<span class="block-label">產品名稱查詢</span>
						<input type="text" id="searh_name" name="searh_name"></input>
					</label>
					<button class="btn btn-darkblue" id="searh-name">查詢</button>
				</div>
				<font color='#6A5ACD' >掃條碼亦可取得商品資料</font>
				<div class="btn-row">
					<button class="btn btn-exec btn-wide" id="create-sale">新增商品資料</button>　
<!-- 					<button class="btn btn-exec btn-wide" id="create-package">新增組合包</button> -->
				</div>
			</div><!-- /.form-wrap -->
		</div>
			<!-- 第一列 -->
<!-- 	<div class="abgne_tab"> -->
<!-- 		<ul class="tabs"> -->
<!-- 			<li><a href="#tab1">普通商品</a></li> -->
<!-- 			<li><a href="#tab2">組合包</a></li> -->
<!-- 		</ul> -->
<!-- 		<div class="tab_container"> -->
<!-- 			<div id="tab1" class="tab_content"> -->
				<div class="row search-result-wrap" align="center">
					<div id="sales-contain" class="ui-widget" style="display:none;">
						<table id="sales" class="result-table" >
							<thead>
								<tr>
									<th>自訂產品ID</th>
									<th style="min-width:100px;">產品名稱</th>
									<th>供應商名稱</th>
									<th style="min-width:40px;">類別</th>
									<th style="min-width:40px;">單位</th>
									<th style="min-width:70px;">成本</th>
									<th style="min-width:70px;">售價</th>
									<th>安全庫存</th>
									<th style="max-width:100px;background-image: none !important;">圖片1</th>
									<th style="max-width:100px;background-image: none !important;">圖片2</th>
									<th style="background-image: none !important;">產品說明</th>
									<th>條碼</th>
									<th style="background-image: none !important;">功能</th>
								</tr>
							</thead>
							<tbody style="line-height:16px;">
							</tbody>
						</table>
					</div>
					<span class="validateTips"> </span>
				</div>
<!-- 			</div> -->
<!-- 			<div id="tab2" class="tab_content"> -->
<!-- 				<div class="row search-result-wrap" align="center"> -->
<!-- 					<div id="packages-contain" class="ui-widget" style="display:none;"> -->
<!-- 						<table id="packages" class="result-table" > -->
<!-- 							<thead> -->
<!-- 								<tr> -->
<!-- 									<th>自訂組合包ID</th> -->
<!-- 									<th>組合包名稱</th> -->
<!-- 									<th>供應商名稱</th> -->
<!-- 									<th>售價</th> -->
<!-- 									<th>產品類別</th> -->
<!-- 									<th>條碼</th> -->
<!-- 									<th>備註</th> -->
<!-- 									<th>功能</th> -->
<!-- 								</tr> -->
<!-- 							</thead> -->
<!-- 							<tbody > -->
<!-- 							</tbody> -->
<!-- 						</table> -->
<!-- 					</div> -->
<!-- 				</div> -->
<!-- 			</div> -->
			</div>
		</div>
<input type="text" id="photo0" style="display:none"/>
<input type="text" id="photo1" style="display:none"/>
<input type="text" id="photo0-update" style="display:none"/>
<input type="text" id="photo1-update" style="display:none"/>
<input type="text" id="bar_code_focus" style="display:none"/>
<div id="warning" style="display:none;"></div>
</div>
</body>
</html>