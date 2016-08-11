<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ page import="tw.com.aber.productunit.controller.*"%>
<%@ page import="java.util.*"%>
<%@ page import="java.sql.DriverManager"%>
<%@ page import="java.sql.Connection"%>
<%@ page import="java.sql.Statement"%>
<%@ page import="java.sql.ResultSet"%>
<jsp:directive.page import="java.sql.SQLException" />
<%
// 	session.setAttribute("group_id", "493cdecf-472e-11e6-806e-000c29c1d067"); //還沒拿到session，先自己假設
// 	session.setAttribute("user_id", ""); //還沒拿到session，先自己假設
%>
<!DOCTYPE html>
<html>
<head>
<title>庫存資料</title>
<meta charset="utf-8">
<link rel="Shortcut Icon" type="image/x-icon" href="./images/Rockettheme-Ecommerce-Shop.ico" />
<link rel="stylesheet" href="css/styles.css" />
<link href="<c:url value="css/css.css" />" rel="stylesheet">
<link href="<c:url value="css/jquery.dataTables.min.css" />" rel="stylesheet">
<link rel="stylesheet" href="//code.jquery.com/ui/1.11.4/themes/smoothness/jquery-ui.css">
</head>
<body>
	<jsp:include page="template.jsp" flush="true"/>
	<div class="content-wrap" style="margin:56px 0px 28px 120px;">
<script src="//code.jquery.com/jquery-1.10.2.js"></script>
<script type="text/javascript" src="js/jquery.dataTables.min.js"></script>
<script type="text/javascript" src="js/jquery-ui.min.js"></script>
<script src="//code.jquery.com/ui/1.11.4/jquery-ui.js"></script>
<script type="text/javascript" src="js/jquery-migrate-1.4.1.min.js"></script>
<script type="text/javascript" src="js/jquery.validate.min.js"></script>
<script type="text/javascript" src="js/additional-methods.min.js"></script>
<script type="text/javascript" src="js/messages_zh_TW.min.js"></script>
<script>
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
		//查詢相關設定

							//e.preventDefault();
							$.ajax({
									type : "POST",
									url : "stock.do",
									data : {
										action : "searh",
										product_id : $("#dialog-form-searh input[name='search_product_id']" ).val(),
									},
									success : function(result) {
											var json_obj = $.parseJSON(result);
											var result_table = "";
											$.each(json_obj,function(i, item) {
												result_table += 
 													"<tr><td>"+json_obj[i].product_name+"</td><td>" 
 													+json_obj[i].quantity+"</td>"
 													+ "<td><div class='table-row-func btn-in-table btn-gray'><i class='fa fa-ellipsis-h'></i>"
 													+ "	<div class='table-function-list'>"
 													+ "		<button class='btn-in-table btn-darkblue btn_update' value='"+ json_obj[i].product_id+"'name='"+ json_obj[i].stock_id+"' ><i class='fa fa-pencil'></i></button>"
 													+ "	</div></div></td></tr>";	
											});
											
											$("#group_button").show();
											//判斷查詢結果
											var resultRunTime = 0;
											$.each (json_obj, function (i) {
												resultRunTime+=1;
											});
											$("#products").dataTable().fnDestroy();
											if(resultRunTime!=0){
												$("#products-contain").show();
												$("#products tbody").html(result_table);
												//$("#products").dataTable({"bFilter": false, "bInfo": false, "paging": false, "language": {"url": "js/dataTables_zh-tw.txt","zeroRecords": "沒有符合的結果"}});
												$(".validateTips").text("");
											}else{
												$("#products-contain").hide();
												$(".validateTips").text("查無此結果");
											}
										}
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
									result_table += 
										"<tr><td>"+json_obj[i].product_name+"</td><td>" 
											+json_obj[i].quantity+"</td>"
											+ "<td><div class='table-row-func btn-in-table btn-gray'><i class='fa fa-ellipsis-h'></i>"
											+ "	<div class='table-function-list'>"
											+ "		<button class='btn-in-table btn-darkblue btn_update' value='"+ json_obj[i].product_id+"'name='"+ json_obj[i].stock_id+"' ><i class='fa fa-pencil'></i></button>"
											+ "	</div></div></td></tr>";	
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
// 									$("#products").dataTable({"language": {"url": "js/dataTables_zh-tw.txt"}});
									$("#products").dataTable({"bFilter": false, "bInfo": false, "paging": false, "language": {"url": "js/dataTables_zh-tw.txt","zeroRecords": "沒有符合的結果"}});
									$(".validateTips").text("");
								}else{
									$("#products-contain").hide();
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
					update_dialog.dialog("close");
				}
			} ],
			close : function() {
				validator_update.resetForm();
			}
		});		
		//修改事件聆聽		
		$("#products").delegate(".btn_update", "click", function(e) {
			e.preventDefault();
			uuid = $(this).val();
			$("input[name='search_product_id'").val("");
			$.ajax({
				type : "POST",
				url : "stock.do",
				data : {
					action : "searh",
					product_id : $("input[name='search_product_id'").val()
				
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
								if(json_obj[i].product_id==uuid){
										$("#dialog-form-update input[name='product_id']").val(json_obj[i].product_id);
										$("#dialog-form-update input[name='stock_id']").val(json_obj[i].stock_id);
										$("#dialog-form-update input[name='quantity']").val(json_obj[i].quantity);
										$("#dialog-form-update input[name='memo']").val(json_obj[i].memo);
								}
							});
						} 

				});			
			update_dialog.dialog("open");
		});		

		//預設表格隱藏
		$("#products-contain").hide();
	});	
</script>

		<div class="datalistWrap">
			<!--對話窗樣式-修改 -->
				<div id="dialog-form-update" title="修改庫存資料">
				<form name="update-dialog-form-post" id="update-dialog-form-post">
					<fieldset>
							<table border="0" height="120">
							<tbody>
							<tr><td><h6>庫存數量:</h6></td><td><input type="text" name="quantity"placeholder="修改庫存數量"/></td></tr>
							<tr><td><h6>備註說明:</h6></td><td><input type="text" name="memo"placeholder="修改備註說明"/></td></tr>
							<tr><td><input type="hidden" name="stock_id" disabled="disabled"> 
							<input type="hidden" name="product_id" disabled="disabled"></td> </tr>
							</tbody>
							</table>	
					</fieldset>
				</form>
			</div>			
			<!-- 第二列 -->
			<div class="row search-result-wrap" align="center" style="width:600px;margin:0px auto;">
				<div id="products-contain" class=" result-table-wrap" >
					<table id="products" class="ui-widget ui-widget-content result-table">
						<thead>
							<tr class="ui-widget-header">
								<th style="min-width:150px;">產品名稱 </th>
								<th style="min-width:150px;">庫存數量</th>
								<th>功能</th>
							</tr>
						</thead>
						<tbody id="tbdy">
						</tbody>
					</table>
				</div>
				<span class="validateTips"> </span>
			</div>
		</div>
	</div>
</body>
</html>