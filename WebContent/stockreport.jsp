<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ page import="tw.com.aber.productunit.controller.*"%>
<%@ page import="java.util.*"%>
<%@ page import="java.sql.DriverManager"%>
<%@ page import="java.sql.Connection"%>
<%@ page import="java.sql.Statement"%>
<%@ page import="java.sql.ResultSet"%>
<jsp:directive.page import="java.sql.SQLException" />
<!DOCTYPE html>
<html>
<head>
<title>庫存報表</title>
<meta charset="utf-8">
<link rel="Shortcut Icon" type="image/x-icon" href="./images/Rockettheme-Ecommerce-Shop.ico" />
<link rel="stylesheet" href="css/styles.css" />
<link rel="stylesheet" href="css/jquery.dataTables.min.css" />
<link rel="stylesheet" href="css/buttons.dataTables.min.css" />
<link href="<c:url value="css/css.css" />" rel="stylesheet">
<link href="<c:url value="css/jquery.dataTables.min.css" />" rel="stylesheet">
<link rel="stylesheet" href="css/1.11.4/jquery-ui.css">
</head>
<body>
	<jsp:include page="template.jsp" flush="true"/>
	<div class="content-wrap" style="margin:56px 0px 28px 120px;">
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
<script type="text/javascript" src="js/jquery.scannerdetection.js"></script>
<script>
	jQuery(document).ready(function($) {
	    $(window).scannerDetection();
	    $(window).bind('scannerDetectionComplete',function(e,data){
		    	$.ajax({
					type : "POST",
					url : "stock.do",
					data : {
						action : "bar_code_search",
						barcode : data.string
					},
					success : function(result) {
		 				var json_obj = $.parseJSON(result);
						var result_table = "";
						$.each(json_obj,function(i, item) {
							result_table += 
									"<tr><td>"+json_obj[i].product_name+"</td><td>" 
									+json_obj[i].quantity+"</td><td>"+json_obj[i].keep_stock+"</td></tr>";	
						});
						
						//判斷查詢結果
						var resultRunTime = 0;
						$.each (json_obj, function (i) {
							resultRunTime+=1;
						});
						$("#products").dataTable().fnDestroy();
						if(resultRunTime!=0){
							$("#products-contain").show();
							$("#products tbody").html(result_table);
 							draw_table("products","庫存報表");
							//$("#products").dataTable({"language": {"url": "js/dataTables_zh-tw.txt","zeroRecords": "沒有符合的結果"}});
// 							$("#products").dataTable({"bFilter": false, "bInfo": false, "paging": false, "language": {"url": "js/dataTables_zh-tw.txt","zeroRecords": "沒有符合的結果"}});
							$(".validateTips").text("");
						}else{
							$("#products-contain").hide();
							$(".validateTips").text("查無此條碼庫存");
						}
					}
				});
        })
        .bind('scannerDetectionError',function(e,data){console.log('detection error '+data.string);})
        .bind('scannerDetectionReceive',function(e,data){console.log(data);});
	});
var bar_search=0;
	$(function() {
		var validator_update = $("#update-dialog-form-post").validate({
			rules : {
				memo : {
					required : true,
					maxlength : 200
				}
			},
			messages : {
				group_name : {
					maxlength : "長度不能超過200個字"
				}
			}
		});
		var group_name = $("#group_name");
		$("#searh_stock").click(function(){
			$.ajax({
				type : "POST",
				url : "stock.do",
				data : {
					action : "searh",
					product_name : $("#searh_stock_name" ).val(),
				},
				success : function(result) {
					//alert(result);
					//console.log(result);
					var json_obj = $.parseJSON(result);
					var result_table = "";
					$.each(json_obj,function(i, item) {
						result_table += 
								  "<tr><td>"+json_obj[i].product_name
								+ "</td><td>" + json_obj[i].quantity
								+ "</td><td>"+json_obj[i].keep_stock+"</td></tr>";	
					});
					
					//判斷查詢結果
					var resultRunTime = 0;
					$.each (json_obj, function (i) {
						resultRunTime+=1;
					});
					$("#products").dataTable().fnDestroy();
					if(resultRunTime!=0){
						$("#products-contain").show();
						$("#products tbody").html(result_table);
						draw_table("products","庫存報表");
						//$("#products").dataTable({ "language": {"url": "js/dataTables_zh-tw.txt","zeroRecords": "沒有符合的結果"}});
						//$("#products").dataTable({"bFilter": false, "bInfo": false, "paging": false, "language": {"url": "js/dataTables_zh-tw.txt","zeroRecords": "沒有符合的結果"}});
						$(".validateTips").text("");
					}else{
						$("#products-contain").hide();
						$(".validateTips").text("查無此結果");
					}
				}
			});
		});
		//修改Dialog相關設定
		update_dialog = $("#dialog-form-update").dialog({
			draggable : false,//防止拖曳
			resizable : false,//防止縮放
			autoOpen : false,
			height : 300,
			width : 350,
			modal : true,
			show : {effect : "blind",duration : 300},
			hide : {effect : "fade",duration : 300},
			buttons : [{
				text : "修改",
				click : function() {
 					if ($('#update-dialog-form-post').valid()) {
						$.ajax({
							type : "POST",
							url : "stock.do",
							data : {
	 							action : "update",
	 							unit_id : uuid,
	 							product_id : $("#dialog-form-update input[name='product_id']").val(),
	 							stock_id : $("#dialog-form-update input[name='stock_id']").val(),
	 							quantity : $("#dialog-form-update input[name='quantity']").val(),
	 							memo : $("#dialog-form-update input[name='memo']").val()
							},				
							success : function(result) {
								var json_obj = $.parseJSON(result);
								var result_table = "";
								$.each(json_obj,function(i, item) {
									if(bar_search==null||uuid==json_obj[i].product_id)
									result_table += 
										"<tr><td>"+json_obj[i].product_name
											+"</td><td>"+json_obj[i].quantity
											+"</td><td>"+json_obj[i].keep_stock+"</td></tr>";	
								});
								//判斷查詢結果
								var resultRunTime = 0;
								$.each (json_obj, function (i) {
									resultRunTime+=1;
								});
								$("#products").dataTable().fnDestroy();
								if(resultRunTime!=0){
									$("#products-contain").show();
									$("#products tbody").html(result_table);
									draw_table("products","庫存報表");
									//$("#products").dataTable({"language": {"url": "js/dataTables_zh-tw.txt"}});
// 									$("#products").dataTable({"bFilter": false, "bInfo": false, "paging": false, "language": {"url": "js/dataTables_zh-tw.txt","zeroRecords": "沒有符合的結果"}});
									$(".validateTips").text("");
								}else{
									$("#products-contain").hide();
								}
							}
						});
						update_dialog.dialog("close");
// 	 					if(bar_search!=null){
// 	 						$.ajax({
// 	 							type : "POST",
// 	 							url : "stock.do",
// 	 							data : {
// 	 								action : "bar_code_search",
// 	 								barcode : bar_search
// 	 							},
// 	 							success : function(result) {
// 	 				 				var json_obj = $.parseJSON(result);
// 	 								var result_table = "";
// 	 								$.each(json_obj,function(i, item) {
// 	 									result_table += 
// 	 											"<tr><td>"+json_obj[i].product_name+"</td><td>" 
// 	 											+json_obj[i].quantity+"</td>"
// 	 											+ "<td><div class='table-row-func btn-in-table btn-gray'><i class='fa fa-ellipsis-h'></i>"
// 	 											+ "	<div class='table-function-list'>"
// 	 											+ "		<button class='btn-in-table btn-darkblue btn_update' title='修改' id='"+bar_search+"' value='"+ json_obj[i].product_id+"'name='"+ json_obj[i].stock_id+"' ><i class='fa fa-pencil'></i></button>"
// 	 											+ "	</div></div></td></tr>";	
// 	 								});
// 	 								//判斷查詢結果
// 	 								var resultRunTime = 0;
// 	 								$.each (json_obj, function (i) {
// 	 									resultRunTime+=1;
// 	 								});
// 	 								$("#products").dataTable().fnDestroy();
// 	 								if(resultRunTime!=0){
// 	 									$("#products-contain").show();
// 	 									$("#products tbody").html(result_table);
// 	 									$("#products").dataTable({"bFilter": false, "bInfo": false, "paging": false, "language": {"url": "js/dataTables_zh-tw.txt","zeroRecords": "沒有符合的結果"}});
// 	 									$(".validateTips").text("");
// 	 								}else{
// 	 									$("#products-contain").hide();
// 	 									$(".validateTips").text("查無此條碼庫存");
// 	 								}
// 	 							}
// 	 						});
// 	 					}
 					}
				}
			}, {
				text : "取消",
				click : function() {
					validator_update.resetForm();
					update_dialog.dialog("close");
				}
			} ],
			close : function() {
				validator_update.resetForm();
			}
		});
		$("#dialog-form-update").show();
		//修改事件聆聽		

	});	
</script>
		<div class="datalistWrap">
			<div class="input-field-wrap">
				<div class="form-wrap">
					<div class="form-row">
						<label for="">
							<span class="block-label">商品名稱查詢<font color=red>&nbsp;或讀取條碼</font></span>
							<input type="text" id="searh_stock_name"></input>
						</label>
						<button class="btn btn-darkblue" id="searh_stock">查詢</button>
					</div>
				</div><!-- /.form-wrap -->
			</div>
			<div id="dialog-form-update" title="修改庫存資料" style="display:none;">
				<form name="update-dialog-form-post" id="update-dialog-form-post">
					<fieldset>
							<table border="0" height="120">
							<tbody>
							<tr><td><h6>庫存數量:</h6></td><td><input type="text" id="quant" name="quantity"placeholder="修改庫存數量"/></td>
							<td>
								&nbsp;<a class='btn-gray' onclick="$('#quant').val(parseInt($('#quant').val())+1);">&nbsp;+&nbsp;</a>&nbsp;<a class='btn-gray' onclick="$('#quant').val(parseInt($('#quant').val())-1);">&nbsp;-&nbsp;</a>
							</td></tr>
							<tr><td><h6>備註說明:</h6></td><td><input type="text" name="memo"placeholder="修改備註說明"/></td></tr>
							<tr><td><input type="hidden" name="stock_id" disabled="disabled"> 
							<input type="hidden" name="product_id" disabled="disabled"></td> </tr>
							</tbody>
							</table>	
					</fieldset>
				</form>
			</div>			
			<!-- 第二列 -->
			<div class="row search-result-wrap" style="width:600px;margin:0px auto;">
				<div id="products-contain" class=" result-table-wrap" style="display:none;">
					<table id="products" class="ui-widget ui-widget-content result-table">
						<thead>
							<tr class="ui-widget-header">
								<th style="min-width:150px;">產品名稱 </th>
								<th style="min-width:150px;">庫存數量</th>
								<th style="min-width:150px;">安全庫存</th>
							</tr>
						</thead>
						<tbody id="tbdy" style="text-align:center">
						</tbody>
					</table>
				</div>
				<span class="validateTips"> </span>
			</div>
		</div>
	</div>
</body>
</html>