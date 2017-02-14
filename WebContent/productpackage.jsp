<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ page import="java.util.*"%>
<jsp:directive.page import="java.sql.SQLException" />
<!DOCTYPE html>
<html>
<head>
<title>商品管理(組合包)</title>
<meta charset="utf-8">

<link rel="Shortcut Icon" type="image/x-icon" href="./images/Rockettheme-Ecommerce-Shop.ico" />
<link rel="stylesheet" href="css/photo/style.css">
<link rel="stylesheet" href="css/photo/jquery.fileupload.css">
<link rel="stylesheet" href="css/styles.css" />
<link href="<c:url value="css/jquery.dataTables.min.css" />" rel="stylesheet">
<link href="<c:url value="css/1.11.4/jquery-ui.css" />" rel="stylesheet">
<style type="text/css">
	.warning_msg{
		position:relative;
		height:0px;
 		padding-top:0px ! important; 
		top:20px;
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
	var product_list = [];
	var information;
	
	function draw_product_package(info){
		console.log(info);
		warning_msg("---讀取中請稍候---");
		$.ajax({
			type : "POST",
			url : "productpackage.do",
			data : info,
			success : function(result) {
				var json_obj = $.parseJSON(result);
				
				//判斷查詢結果
				if(json_obj.length>0){
					var result_table = "";
					$.each(json_obj,function(i, item) {
						if(i<json_obj.length){
							result_table 
							+= "<tr>"
							+ "<td name='c_package_id' value='"+ json_obj[i].c_product_id +"'>"+ json_obj[i].c_product_id+ "</td>"
							+ "<td name='package_name' value='"+ json_obj[i].product_name +"'>"+ json_obj[i].product_name+ "</td>"
							+ "<td name='price' value='"+ json_obj[i].price +"'>"+ money(json_obj[i].price)+ "</td>"
							+ "<td name='type' value='"+ json_obj[i].type_id +"'>"+ json_obj[i].type_id+ "</td>"
							+ "<td name='barcode' value='"+ json_obj[i].barcode +"'>"+ json_obj[i].barcode+ "</td>"
							+ "<td name='description' value='"+ json_obj[i].description +"'>"+ json_obj[i].description+ "</td>"
							+ (isIE()?"<td width='150px'><div class='table-row-func btn-in-table btn-gray' style='float:left;'><i class='fa fa-ellipsis-h'></i>":"<td><div class='table-row-func btn-in-table btn-gray'><i class='fa fa-ellipsis-h'></i>")
							+ "	<div class='table-function-list'>"
							+ "		<button class='btn-in-table btn-darkblue btn_update' title='修改組合包' value='"+ json_obj[i].product_id+ "'name='"+ json_obj[i].product_name+"'><i class='fa fa-pencil'></i></button>"
							+ "		<button class='btn-in-table btn-alert btn_delete' title='刪除組合包' value='"+ json_obj[i].product_id+ "' ><i class='fa fa-trash'></i></button>"
							+ "		<button class='btn-in-table btn-primary btn_detail' title='顯示明細項目' value='"+ json_obj[i].product_id + "'><i class='fa fa-list'></i></button>"
							+ "		<button class='btn-in-table btn-green btn_create' title='新增商品' value='"+ json_obj[i].product_id + "'><i class='fa fa-pencil-square-o'></i></button>"
							+ "	</div></div></td></tr>";
						}
					});
				}
				
				$("#package").dataTable().fnDestroy();
				if(json_obj.length>0){
					$("#package-contain").show();
					$("#package tbody").html(result_table);
					$("#package").dataTable({
						autoWidth: false,
						scrollX:  true,
						scrollY:"300px",
						"language": {"url": "js/dataTables_zh-tw.txt"},"order": []});
					tooltip('btn_update');
					tooltip('btn_delete');
					tooltip('btn_detail');
					tooltip('btn_create');
					$("#package").find("td").css("text-align","center");
					$("#package").find("th").css("text-align","center");
					$("#package-contain").animate({"opacity":"0.01"},1);
					$("#package-contain").animate({"opacity":"1"},300);
					warning_msg("");
				}
				
				if(json_obj.length==0){
					$("#package-contain").hide();
					warning_msg("---查無此結果---");
				}
			}
		});
		
		//detail clear
		$("#package-detail tbody").html('');
		$("#package_detail_contain_row").hide();
		
	}
	
	function draw_product_package_detail(info){
		$.ajax({
			type : "POST",
			url : "productpackage.do",
			data : info,
			success : function(result) {
				var json_obj = $.parseJSON(result);
				if(json_obj.length>0){
					var result_table = "";
					
					$.each(json_obj,function(i, item) {
						var tmp=(json_obj[i].photo==null?"":(json_obj[i].photo.length<1)?"無圖片":"<img src=./image.do?picname="+json_obj[i].photo+" style='max-width:100px;max-height:100px'>");
						var tmp1=(json_obj[i].photo==null?"":(json_obj[i].photo1.length<1)?"無圖片":"<img src=./image.do?picname="+json_obj[i].photo1+" style='max-width:100px;max-height:100px'>");
						result_table 
						+= "<tr>"
						+ "<td name='c_product_id' value='"+ json_obj[i].c_product_id +"'>"+ json_obj[i].c_product_id+ "</td>"
						+ "<td name='product_name' value='"+ json_obj[i].product_name +"'>"+ json_obj[i].product_name+ "</td>"
						+ "<td name='quantity' value='"+ json_obj[i].quantity +"'>"+ json_obj[i].quantity + "</td>"
						+ "<td name='price' value='"+ json_obj[i].price +"'><del>"+"原價："+ money(json_obj[i].price)+ "</del></td>"
						+ "<td name='photo' value='"+ json_obj[i].photo+"' align='center'>"+tmp+"</td>"
						+ "<td name='photo1' value='"+ json_obj[i].photo1 +"' align='center'>"+tmp1+"</td>"
						+ "<td name='package_desc' value='"+ json_obj[i].package_desc +"'>"+ json_obj[i].package_desc+ "</td>"
						+ "<td><div class='table-row-func btn-in-table btn-gray'><i class='fa fa-ellipsis-h'></i>"
						+ "	<div class='table-function-list'>"
						+ "		<button class='btn-in-table btn-darkblue btn_update' title='修改' value='"+ json_obj[i].package_id+ "' parent='"+ json_obj[i].parent_id+"' product='"+ json_obj[i].product_id+"'><i class='fa fa-pencil'></i></button>"
						+ "		<button class='btn-in-table btn-alert btn_delete' title='刪除' value='"+ json_obj[i].package_id+ "' parent='"+ json_obj[i].parent_id+"' product='"+ json_obj[i].product_id+"'><i class='fa fa-trash'></i></button>"
						+ "	</div></div></td></tr>";
					});
					
					$("#package-detail").dataTable().fnDestroy();
					$("#package-detail tbody").html(result_table);
					$("#package-detail").dataTable({
						"language": {"url": "js/dataTables_zh-tw.txt"},
						"order": []
					});
					
					tooltip('btn_update');
					tooltip('btn_delete');
					
					$("#package_detail_contain_row").show();
					warning_msg("");
				}
				
				if(json_obj.length==0){
					$("#package-detail tbody").html('');
					$("#package_detail_contain_row").hide();
					warning_msg("該產品包無物品");
				}
			}
		});
	}
	$(function() {
		$(".bdyplane").animate({"opacity":"1"});
		
		//驗證
		$("#dialog-insert-package-form").validate({
			rules : {
				c_product_id : {maxlength : 40 , required : true},
				package_type:{maxlength : 80 , required : true},
				product_name : {maxlength : 80 , required : true},
				price: {digits: true, required : true},
				barcode: {},
				description: {}
			}
		});
		
		$("#dialog-update-package-form").validate({
			rules : {
				c_product_id : {maxlength : 40 , required : true},
				package_type:{maxlength : 80 , required : true},
				product_name : {maxlength : 80 , required : true},
				price: {digits: true, required : true},
				barcode: {},
				description: {}
			}
		});
		
		$("#dialog-insert-package-detail-form").validate({
			rules : {
				c_product_id : {maxlength : 40 , required : true},
				package_name:{maxlength : 80 , required : true},
				quantity : {digits: true, required : true},
				package_desc: {}
			}
		});
		
		$("#dialog-update-package-detail-form").validate({
			rules : {
				c_product_id : {maxlength : 40 , required : true},
				package_name:{maxlength : 80 , required : true},
				quantity : {digits: true, required : true},
				package_desc: {}
			}
		});
		
		//新增Package Dialog
		$("#dialog-insert-package").dialog({
			draggable : true, resizable : false, autoOpen : false,
			height : "auto", width : "auto", modal : true,
			show : {effect : "blind",duration : 300},
			hide : {effect : "fade",duration : 300},
			buttons : [{
				id : "insert",
				text : "新增",
				click : function() {
					if($('#dialog-insert-package-form').valid()){
						$("#dialog-insert-package").dialog("close");
						
						var info={
							action : "insert",
							c_package_id : $("#dialog-insert-package input[name='c_product_id']").val(),
							package_name: $("#dialog-insert-package input[name='product_name']").val(),
							price: $("#dialog-insert-package input[name='price']").val(),
							package_type: $("#dialog-insert-package input[name='package_type']").val(),
							barcode: $("#dialog-insert-package input[name='barcode']").val(),
							description: $("#dialog-insert-package input[name='description']").val()
						};
						
						draw_product_package(info);
					}
				}
			},{
				text : "取消",
				click : function() { 
					$("#dialog-insert-package").dialog("close"); 
				}
			}]
		});
		
		$("#dialog-insert-package").show();
		
		//修改Package Dialog
		$("#dialog-update-package").dialog({
			draggable : true, resizable : false, autoOpen : false,
			height : "auto", width : "auto", modal : true,
			show : {effect : "blind",duration : 300},
			hide : {effect : "fade",duration : 300},
			buttons : [{
				id : "update",
				text : "修改",
				click : function() {
					if($('#dialog-update-package-form').valid()){
						$("#dialog-update-package").dialog("close");
						var info={
							action : "update",
							package_id : $(this).val(),
							c_package_id : $("#dialog-update-package input[name='c_product_id']").val(),
							package_name: $("#dialog-update-package input[name='product_name']").val(),
							price: $("#dialog-update-package input[name='price']").val(),
							package_type: $("#dialog-update-package input[name='package_type']").val(),
							barcode: $("#dialog-update-package input[name='barcode']").val(),
							description: $("#dialog-update-package input[name='description']").val()
						};
						draw_product_package(info);
					}
				}
			},{
				text : "取消",
				click : function() { 
					$("#dialog-update-package").dialog("close"); 
				}
			}]
		});
		
		$("#dialog-update-package").show();
		
		//刪除Package Dialog
		$("#dialog-confirm").dialog({
			draggable : true, resizable : false, autoOpen : false,
			height : "auto", width : "auto", modal : true,
			show : {effect : "blind",duration : 300},
			hide : {effect : "fade",duration : 300},
			buttons : {
				"確認刪除" : function() {
					var information={
						action : "delete",
						package_id : $(this).val()
					};
					draw_product_package(information);
					$(this).dialog("close");
				},
				"取消刪除" : function() {$(this).dialog("close");}
			}
		});
		
		$("#dialog-confirm").show();
		
		//新增Package detail Dialog
		$("#dialog-insert-package-detail").dialog({
			draggable : true, resizable : false, autoOpen : false,
			height : "auto", width : "auto", modal : true,
			show : {effect : "blind",duration : 300},
			hide : {effect : "fade",duration : 300},
			buttons : [{
				id : "insert",
				text : "新增",
				click : function() {
					if($('#dialog-insert-package-detail-form').valid()){
						$("#dialog-insert-package-detail").dialog("close");
						var info={
							action : "insert_detail",
							package_id : $("#dialog-insert-package-detail").val(),
							product_id : $("#dialog-insert-package-detail").attr("value2"),
							c_product_id : '',
							product_name: '',
							quantity : $("#dialog-insert-package-detail input[name='quantity']").val(),
							package_desc: $("#dialog-insert-package-detail input[name='package_desc']").val()
						};
						draw_product_package_detail(info);
						lookdown();
					}
				}
			},{
				text : "取消",
				click : function() { 
					$("#dialog-insert-package-detail").dialog("close"); 
				}
			}]
		});
		
		$("#dialog-insert-package-detail").show();
		
		//修改Package detail Dialog
		$("#dialog-update-package-detail").dialog({
			draggable : true, resizable : false, autoOpen : false,
			height : "auto", width : "auto", modal : true,
			show : {effect : "blind",duration : 300},
			hide : {effect : "fade",duration : 300},
			buttons : [{
				id : "insert",
				text : "修改",
				click : function() {
					if($('#dialog-update-package-detail-form').valid()){
						
						$("#dialog-update-package-detail").dialog("close");
						
						var info={
							action : "update_detail",
							package_id : $("#dialog-update-package-detail").val(),
							package_id2 : $("#dialog-update-package-detail").attr("parent"),
							product_id : $("#dialog-update-package-detail").attr("product"),
							quantity : $("#dialog-update-package-detail input[name='quantity']").val(),
							package_desc: $("#dialog-update-package-detail input[name='package_desc']").val()
						};
						
						draw_product_package_detail(info);
					}
				}
			},{
				text : "取消",
				click : function() { 
					$("#dialog-update-package-detail").dialog("close"); 
				}
			}]
		});
		
		$("#dialog-update-package-detail").show();
		
		//刪除Package detail Dialog
		$("#dialog-delete-package-detail").dialog({
			draggable : true, resizable : false, autoOpen : false,
			height : "auto", width : "auto", modal : true,
			show : {effect : "blind",duration : 300},
			hide : {effect : "fade",duration : 300},
			buttons : {
				"確認移除" : function() {
					var information={
						action : "delete_detail",
						package_id : $(this).val(),
						parent_id : $(this).attr("parent")
					};
					draw_product_package_detail(information);
					$(this).dialog("close");
				},
				"取消移除" : function() {
					$(this).dialog("close");
				}
			}
		});
		
		$("#dialog-delete-package-detail").show();
		
		$("#package").delegate(".btn_update", "click", function(e) {
			e.preventDefault();
			$("#dialog-update-package").val($(this).val());
			$("#dialog-update-package input[name='c_product_id']").val($(this).parents("tr").find("td[name='c_package_id']").attr("value"));
			$("#dialog-update-package input[name='product_name']").val($(this).parents("tr").find("td[name='package_name']").attr("value"));
			$("#dialog-update-package input[name='price']").val($(this).parents("tr").find("td[name='price']").attr("value"));
			$("#dialog-update-package input[name='package_type']").val($(this).parents("tr").find("td[name='type']").attr("value"));
			$("#dialog-update-package input[name='barcode']").val($(this).parents("tr").find("td[name='barcode']").attr("value"));
			$("#dialog-update-package input[name='description']").val($(this).parents("tr").find("td[name='description']").attr("value"));
			$('#same2').prop('checked',false);
			$("#dialog-update-package").dialog("open");
		});
		
		$("#package").delegate(".btn_delete", "click", function(e) {
			e.preventDefault();
			$("#dialog-confirm").val($(this).val());
			$("#dialog-confirm").html("<table class='dialog-table'>"+
				"<tr><td>組合包名稱：</td><td><span class='delete_msg'>'"+$(this).parents("tr").find("td[name='package_name']").attr("value")+"'</span></td></tr>"+
				"<tr><td>組合包類別：</td><td><span class='delete_msg'>'"+$(this).parents("tr").find("td[name='type']").attr("value")+"'</span></td></tr>"+
				"</table>"
			);
			$("#dialog-confirm").dialog("open");
		});
		
		$("#package").delegate(".btn_detail", "click", function(e) {
			e.preventDefault();
			var information={action : "search_detail", package_id : $(this).val()};
			draw_product_package_detail(information);
			lookdown();
		});
		
		$("#package").delegate(".btn_create", "click", function(e) {
			e.preventDefault();
			$("#dialog-insert-package-detail input[name='c_product_id']").val('');
			$("#dialog-insert-package-detail input[name='package_name']").val('');
			$("#dialog-insert-package-detail input[name='quantity']").val('');
			$("#dialog-insert-package-detail input[name='package_desc']").val('');
// 			$("#dialog-insert-package-detail").attr("value2","0a8b87bb-a256-11e6-922d-005056af760c");
			$("#dialog-insert-package-detail").val($(this).val());
			$("#dialog-insert-package-detail").dialog("open");
		});
		
		$("#package-detail").delegate(".btn_update", "click", function(e) {
			e.preventDefault();
			$("#dialog-update-package-detail input[name='c_product_id']").val($(this).parents("tr").find("td[name='c_product_id']").attr("value"));
			$("#dialog-update-package-detail input[name='package_name']").val($(this).parents("tr").find("td[name='product_name']").attr("value"));
			$("#dialog-update-package-detail input[name='quantity']").val($(this).parents("tr").find("td[name='quantity']").attr("value"));
			$("#dialog-update-package-detail input[name='package_desc']").val($(this).parents("tr").find("td[name='package_desc']").attr("value"));
			
			$("#dialog-update-package-detail").val($(this).val());
			$("#dialog-update-package-detail").attr("product",$(this).attr("product"));
			$("#dialog-update-package-detail").attr("parent",$(this).attr("parent"));
			$("#dialog-update-package-detail").dialog("open");
		});
		
		$("#package-detail").delegate(".btn_delete", "click", function(e) {
			e.preventDefault();
			
			$("#dialog-delete-package-detail").html("<table class='dialog-table'>"+
				"<tr><td>商品名稱：</td><td><span class='delete_msg'>'"+$(this).parents("tr").find("td[name='product_name']").attr("value")+"'</span></td></tr>"+
				"<tr><td>商品規格：</td><td><span class='delete_msg'>'"+$(this).parents("tr").find("td[name='package_desc']").attr("value")+"'</span></td></tr>"+
				"</table>"
			);
			
			$("#dialog-delete-package-detail").val($(this).val());
			$("#dialog-delete-package-detail").attr("parent",$(this).attr("parent"));
			$("#dialog-delete-package-detail").dialog("open");
		});
		
		$("#searh-name").click(function(e) {
			e.preventDefault();
			var information={
				action : "search",
				word : $("#searh_name").val(),
			};
			draw_product_package(information);
		});
		
		$("#create-package").click( function() {
			$("#dialog-insert-package input[name='c_product_id']").val('');
			$("#dialog-insert-package input[name='product_name']").val('');
			$("#dialog-insert-package input[name='price']").val('');
			$("#dialog-insert-package input[name='package_type']").val('');
			$("#dialog-insert-package input[name='barcode']").val('');
			$("#dialog-insert-package input[name='description']").val('');
			$('#same1').prop('checked',false);
			$("#dialog-insert-package").dialog("open");
		});
		
		$.ajax({
			type : "POST",
			url : "product.do",
			data :{action : "search"},
			success : function(result) {
				var json_obj = $.parseJSON(result);
				console.log(json_obj);
				$.each (json_obj, function (i,item) {
					if(json_obj[i].product_id!=null){
												
						item = {}
				        item ["c_product_id"] = json_obj[i].c_product_id;
				        item ["product_id"] = json_obj[i].product_id;
				        item ["product_name"] = json_obj[i].product_name;
				        item ["description"] = json_obj[i].description;
				        product_list.push(item);
					}
					
				});
			}
		});		
		
		$("#dialog-insert-package-detail input[name='c_product_id']").autocomplete({
			minLength: 1,
			source: function (request, response) {
				$.ajax({
					url : "product.do",
					data :{action : "search"},
					type: "POST",
					dataType: "JSON",
					success: function (data) {
						response(
							$.map(data, function (el) {
								return {
									label: el.c_product_id,
									value: el.product_id
								};
							})
						);
					}
				});
			},
            select: function( event, ui ){
            	console.log('select');
            	event.preventDefault();
            	
				var idx = product_list.map(function(x) {return x.product_id; }).indexOf(ui.item.value);
				
				$("#dialog-insert-package-detail").attr("value2", product_list[idx].product_id);
				$("#dialog-insert-package-detail input[name='c_product_id']").val( product_list[idx].c_product_id );
				$("#dialog-insert-package-detail input[name='package_name']").val( product_list[idx].product_name );
				$("#dialog-insert-package-detail input[name='package_desc']").val( product_list[idx].description );
            },
			change: function( event, ui ){
            	event.preventDefault();
            	
				var idx = product_list.map(function(x) {return x.product_id; }).indexOf(ui.item.value);
				
				$("#dialog-insert-package-detail").attr("value2", product_list[idx].product_id);
				$("#dialog-insert-package-detail input[name='c_product_id']").val( product_list[idx].c_product_id );
				$("#dialog-insert-package-detail input[name='package_name']").val( product_list[idx].product_name );
				$("#dialog-insert-package-detail input[name='package_desc']").val( product_list[idx].description );
			}
		});
		
		$("#dialog-insert-package-detail input[name='package_name']").autocomplete({
			minLength: 1,
			source: function (request, response) {
				$.ajax({
					url : "product.do",
					data :{action : "search"},
					type: "POST",
					dataType: "JSON",
					success: function (data) {
						response(
							$.map(data, function (el) {
								return {
									label: el.product_name,
									value: el.product_id
								};
							})
						);
					}
				});
			},
			select: function( event, ui ){
            	event.preventDefault();
            	
				var idx = product_list.map(function(x) { return x.product_id; }).indexOf(ui.item.value);
				
				$("#dialog-insert-package-detail").attr("value2", product_list[idx].product_id);
				$("#dialog-insert-package-detail input[name='c_product_id']").val( product_list[idx].c_product_id );
				$("#dialog-insert-package-detail input[name='package_name']").val( product_list[idx].product_name );
				$("#dialog-insert-package-detail input[name='package_desc']").val( product_list[idx].description );
			},
			change: function( event, ui ){
            	event.preventDefault();
            	
				var idx = product_list.map(function(x) {return x.package_name; }).indexOf(ui.item.value);
				
				$("#dialog-insert-package-detail").attr("value2", product_list[idx].product_id);
				$("#dialog-insert-package-detail input[name='c_product_id']").val( product_list[idx].c_product_id );
				$("#dialog-insert-package-detail input[name='package_name']").val( product_list[idx].product_name );
				$("#dialog-insert-package-detail input[name='package_desc']").val( product_list[idx].description );
			}
		});
		
		$("#dialog-update-package-detail input[name='c_product_id']").autocomplete({
			minLength: 1,
			source: function (request, response) {
				$.ajax({
					url : "product.do",
					data :{action : "search"},
					type: "POST",
					dataType: "JSON",
					success: function (data) {
						response(
							$.map(data, function (el) {
								return {
									label: el.c_product_id,
									value: el.product_id
								};
							})
						);
					}
				});
			},
            select: function( event, ui ){
            	event.preventDefault();
            	
				var idx = product_list.map(function(x) { return x.product_id; }).indexOf(ui.item.value);
				
				$("#dialog-update-package-detail").attr("product", product_list[idx].product_id);
				$("#dialog-update-package-detail input[name='c_product_id']").val( product_list[idx].c_product_id );
				$("#dialog-update-package-detail input[name='package_name']").val( product_list[idx].product_name );
				$("#dialog-update-package-detail input[name='package_desc']").val( product_list[idx].description );
			},
			change: function( event, ui ){
            	event.preventDefault();
            	
				var idx = product_list.map(function(x) { return x.product_id; }).indexOf(ui.item.value);
				
				$("#dialog-update-package-detail").attr("product", product_list[idx].product_id);
				$("#dialog-update-package-detail input[name='c_product_id']").val( product_list[idx].c_product_id );
				$("#dialog-update-package-detail input[name='package_name']").val( product_list[idx].product_name );
				$("#dialog-update-package-detail input[name='package_desc']").val( product_list[idx].description );
			}
		});
		
		$("#dialog-update-package-detail input[name='package_name']").autocomplete({
			minLength: 1,
			source: function (request, response) {
				$.ajax({
					url : "product.do",
					data :{action : "search"},
					type: "POST",
					dataType: "JSON",
					success: function (data) {
						response(
							$.map(data, function (el) {
								return {
									label: el.product_name,
									value: el.product_id
								};
							})
						);
					}
				});
			},
            select: function( event, ui ){
            	event.preventDefault();
            	
				var idx = product_list.map(function(x) { return x.product_id; }).indexOf(ui.item.value);
				
				$("#dialog-update-package-detail").attr("product", product_list[idx].product_id);
				$("#dialog-update-package-detail input[name='c_product_id']").val( product_list[idx].c_product_id );
				$("#dialog-update-package-detail input[name='package_name']").val( product_list[idx].product_name );
				$("#dialog-update-package-detail input[name='package_desc']").val( product_list[idx].description );
			},
			change: function(event){
            	event.preventDefault();
            	
				var idx = product_list.map(function(x) { return x.product_id; }).indexOf(ui.item.value);
				
				$("#dialog-update-package-detail").attr("product", product_list[idx].product_id);
				$("#dialog-update-package-detail input[name='c_product_id']").val( product_list[idx].c_product_id );
				$("#dialog-update-package-detail input[name='package_name']").val( product_list[idx].product_name );
				$("#dialog-update-package-detail input[name='package_desc']").val( product_list[idx].description );
			}
		});
		
	    $("#warning").dialog({
			title: "警告",
			draggable : true,//防止拖曳
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
			<!--對話窗樣式-確認 -->
			<div id="dialog-confirm" title="是否刪除此商品?" style="display:none;">
				<p>是否確認刪除該筆資料</p>
			</div>
			
			<!--對話窗樣式-新增 -->
			<div id="dialog-insert-package" title="新增產品包" style="display:none;">
				<form name="insert-dialog-form-post" id="dialog-insert-package-form" style="display:inline">
					<table class="form-table">
					  <tbody>
						<tr>
							<td>自訂組合包ID：</td><td><input type="text" id="package_c_p_id_"name="c_product_id" placeholder="輸入自訂組合包ID"/></td>
							<td>產品類別：</td><td><input type="text" id="package_type"name="package_type" placeholder="輸入組合包類別"/></td>
						</tr><tr>
							<td>組合包名稱：</td><td><input type="text" id="package_name"name="product_name" placeholder="輸入組合包名稱"/></td>
							<td>售價：</td><td><input type="text" id="package_price" name="price" placeholder="輸入組合包售價"/></td>
						</tr><tr>
							<td>條碼(<input id="same1" type="checkbox" style="position:static;transform: scale(0.8);" 
							onclick="if($('#same1').prop('checked')){$('#package_barcode').val($('#package_c_p_id_').val());}else{$('#package_barcode').val('');}">同ID)：</td><td><input type="text" id="package_barcode" name="barcode" placeholder="輸入條碼"/></td>
							<td>說明：</td><td><input type="text" name="description" placeholder="輸入組合包說明"/></td>
						</tr>
	  		         	  </tbody>
	  		        	</table>
				</form>
			</div>
			
			<!--對話窗樣式-修改 -->
			<div id="dialog-update-package" title="修改產品包" style="display:none;">
				<form name="update-dialog-form-post" id="dialog-update-package-form" style="display:inline">
					<table class="form-table">
					  <tbody>
						<tr>
							<td>自訂組合包ID：</td><td><input type="text" id="package_c_p_id_"name="c_product_id" placeholder="修改自訂組合包ID"/></td>
							<td>產品類別：</td><td><input type="text" id="package_type"name="package_type" placeholder="修改組合包類別"/></td>
						</tr><tr>
							<td>組合包名稱：</td><td><input type="text" id="package_name"name="product_name" placeholder="修改組合包名稱"/></td>
							<td>售價：</td><td><input type="text" id="package_price" name="price" placeholder="修改組合包售價"/></td>
						</tr><tr>
							<td>條碼(<input id="same2" type="checkbox" style="position:static;transform: scale(0.8);" 
							onclick="if($('#same2').prop('checked')){$('#package_barcode').val($('#package_c_p_id_').val());}else{$('#package_barcode').val('');}">同ID)：</td><td><input type="text" id="package_barcode" name="barcode" placeholder="修改條碼"/></td>
							<td>說明：</td><td><input type="text" name="description" placeholder="修改組合包說明"/></td>							
						</tr>
	  		         	  </tbody>
	  		        	</table>
				</form>
			</div>
			
			<!--對話窗樣式-刪除Detail -->
			<div id="dialog-delete-package-detail" title="是否移除此商品?" style="display:none;"></div>
			
			<!--對話窗樣式-新增Detail -->
			<div id="dialog-insert-package-detail" title="產品包內含" style="display:none;">
				<form id='dialog-insert-package-detail-form'>
					<table class="form-table">
						<tbody>
							<tr>
								<td>自訂產品ID：</td><td><input type="text" name="c_product_id" placeholder="輸入商品自定ID"/></td>
								<td>產品名稱：</td><td><input type="text" name="package_name" placeholder="輸入商品名稱"/></td>
							</tr><tr>
								<td>數量：</td><td><input type="text" name="quantity" placeholder="輸入產品包內含商品數"/></td>
								<td>產品規格：</td><td><input type="text" name="package_desc" placeholder="輸入產品規格"/></td>
							</tr>
						</tbody>
					</table>
				</form>
			</div>
			
			<!--對話窗樣式-修改Detail -->
			<div id="dialog-update-package-detail" title="修改產品包內含" style="display:none;">
				<form id='dialog-update-package-detail-form'>
					<table class="form-table">
						<tbody>
							<tr>
								<td>自訂產品ID：</td><td><input type="text" name="c_product_id" placeholder="修改商品自定ID"/></td>
								<td>產品名稱：</td><td><input type="text" name="package_name"placeholder="修改商品自定ID"/></td>
							</tr><tr>
								<td>數量：</td><td><input type="text" name="quantity" placeholder="修改產品包內含商品數"/></td>
								<td>產品規格：</td><td><input type="text" name="package_desc" placeholder="修改產品規格"/></td>
							</tr>
						</tbody>
					</table>
				</form>
			</div>
			
			
			<div class="input-field-wrap">
				<div class="form-wrap">
					<div class="form-row">
						<label for="">
							<span class="block-label">組合包ID/名稱查詢</span>
							<input type="text" id="searh_name" name="searh_name"></input>
						</label>
						<button class="btn btn-darkblue" id="searh-name">查詢</button>
					</div>
					<div class="btn-row">
						<button class="btn btn-exec btn-wide" id="create-package">新增組合包資料</button>　
					</div>
				</div><!-- /.form-wrap -->
			</div>
			
			<!-- 第一列 -->
			<div class="row search-result-wrap" align="center">
				<div id="package-contain" class="ui-widget" style="display:none;">
					<table id="package" class="result-table" >
						<thead>
							<tr>
								<th>自訂組合包ID</th>
								<th>組合包名稱</th>
								<th>售價</th>
								<th>組合包類別</th>
								<th>條碼</th>
								<th>備註</th>
								<th>功能</th>
							</tr>
						</thead>
						<tbody style="line-height:16px;">
						</tbody>
					</table>
				</div>
			</div>
			
			<div class="search-result-wrap" align="center" id="package_detail_contain_row" style="display:none;">
				<hr class="hr0" style="padding-top:40px;">
				<div id="package-detail-contain" class="result-table-wrap">
					<table id="package-detail" class="result-table">
						<thead>
							<tr>
								<th>自訂產品ID</th>
								<th style="min-width:100px;">產品名稱</th>
								<th style="min-width:70px;">數量</th>
								<th style="min-width:70px;">單品售價</th>
								<th style="max-width:100px;width:70px;background-image: none !important;">圖片1</th>
								<th style="max-width:100px;width:70px;background-image: none !important;">圖片2</th>
								<th>商品規格</th>
								<th style="background-image: none !important;">功能</th>
							</tr>
						</thead>
						<tbody>
						</tbody>
					</table>
				</div>
			</div>
		</div>
	</div>
	
	<div id="warning" style="display:none;"></div>
</body>
</html>