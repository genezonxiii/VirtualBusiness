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
<title>公司資料</title>
<meta charset="utf-8">
<link rel="stylesheet" href="css/styles.css" />
<link href="<c:url value="css/css.css" />" rel="stylesheet">
<link href="<c:url value="css/jquery.dataTables.min.css" />" rel="stylesheet">
<link rel="stylesheet" href="//code.jquery.com/ui/1.11.4/themes/smoothness/jquery-ui.css">
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
				group_name : {
					required : true,
					maxlength : 10
				}
			},
			messages : {
				group_name : {
					maxlength : "長度不能超過10個字"
				}
			},
			address : {
				required : true,
				maxlength : 80
			},
			phone : {
				required : true,
				maxlength : 12
			},
			fax : {
				required : true,
				maxlength : 12
			},
			mobile : {
				required : true,
				maxlength : 15
			},
			email : {
				required : true,
				maxlength : 30
			},
			master : {
				required : true,
				maxlength : 10
			},
		});
		var group_name = $("#group_name");
		//查詢相關設定
							$.ajax({
									type : "POST",
									url : "group.do",
									data : {
										action : "searh",
										group_name : $("#dialog-form-searh input[name='search_group_name']" ).val()
									},
									success : function(result) {
											var json_obj = $.parseJSON(result);
											var result_table = "";
											$.each(json_obj,function(i, item) {
												result_table += 
													"<tr><td>"+"<p>公司名稱</p>"+"</td><td>"+json_obj[i].group_name +"</td>"
													+"<tr><td>"+"<p>統一編號</p>"+"</td><td>"+ json_obj[i].group_unicode+"</td>"
													+ "<tr><td>"+"<p>公司地址</p>"+"</td><td>"+ json_obj[i].address+"</td>"
													+ "<tr><td>"+"<p>公司電話</p>"+"</td><td>"+ json_obj[i].phone+ "</td>"
													+ "<tr><td>"+"<p>公司傳真</p>"+ "</td><td>"+ json_obj[i].fax+"</td>"
													+ "<tr><td>"+ "<p>負責人</p>"+"</td><td>"+json_obj[i].master+"</td>"
													+ "<tr><td>"+"<p>負責人Email</p>"+"</td><td>"+ json_obj[i].email+ "</td>"
													+ "<tr><td>"+"<p>負責人連絡手機</p>"+"</td><td>"+ json_obj[i].mobile+"</td></tr>"
													+ "<tr><td>"+ "<p>功能</p>"+"</td><td>"
													+"<button value='"+ json_obj[i].group_id+ "'name='"+ json_obj[i].group_name
													+ "'class='btn_update'>修改</button></td></tr>";
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
												$("#products").dataTable({"bFilter": false, "bInfo": false, "paging": false, "bSort": false, "language": {"url": "js/dataTables_zh-tw.txt","zeroRecords": "沒有符合的結果"}});
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
			height : 500,
			width : 500,
			modal : true,
			buttons : [{
				text : "修改",
				click : function() {
					if ($('#update-dialog-form-post').valid()) {
						$.ajax({
							type : "POST",
							url : "group.do",
							data : {
	 							action : "update",
	 							unit_id : uuid,
	 							group_name : $("#dialog-form-update input[name='group_name']" ).val(),
	 							group_unicode : $("#dialog-form-update input[name='group_unicode']" ).val(),
	 							address : $("#dialog-form-update input[name='address']").val(),
	 							phone : $("#dialog-form-update input[name='phone']").val(),
	 							fax : $("#dialog-form-update input[name='fax']").val(),
	 							mobile : $("#dialog-form-update input[name='mobile']").val(),
	 							email : $("#dialog-form-update input[name='email']").val(),
	 							master : $("#dialog-form-update input[name='master']").val(),
							},				
							success : function(result) {
								var json_obj = $.parseJSON(result);
								var result_table = "";
								$.each(json_obj,function(i, item) {
									result_table += 
										"<tr><td>"+"<p>公司名稱</p>"+"</td><td>"+json_obj[i].group_name +"</td>"
										+"<tr><td>"+"<p>統一編號</p>"+"</td><td>"+ json_obj[i].group_unicode+"</td>"
										+ "<tr><td>"+"<p>公司地址</p>"+"</td><td>"+ json_obj[i].address+"</td>"
										+ "<tr><td>"+"<p>公司電話</p>"+"</td><td>"+ json_obj[i].phone+ "</td>"
										+ "<tr><td>"+"<p>公司傳真</p>"+ "</td><td>"+ json_obj[i].fax+"</td>"
										+ "<tr><td>"+ "<p>負責人</p>"+"</td><td>"+json_obj[i].master+"</td>"
										+ "<tr><td>"+"<p>負責人Email</p>"+"</td><td>"+ json_obj[i].email+ "</td>"
										+ "<tr><td>"+"<p>負責人連絡手機</p>"+"</td><td>"+ json_obj[i].mobile+"</td></tr>"
										+ "<tr><td>"+ "<p>功能</p>"+"</td><td>"
										+"<button value='"+ json_obj[i].group_id+ "'name='"+ json_obj[i].group_name
										+ "'class='btn_update'>修改</button></td></tr>";
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
									$("#products").dataTable({"bFilter": false, "bInfo": false, "paging": false, "bSort": false, "language": {"url": "js/dataTables_zh-tw.txt","zeroRecords": "沒有符合的結果"}});
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
			$("input[name='search_group_name'").val("");
			$.ajax({
				type : "POST",
				url : "group.do",
				data : {
					action : "searh",
					group_name : $("input[name='search_group_name'").val()
				
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
										$("#dialog-form-update input[name='group_name']").val(json_obj[i].group_name);
										$("#dialog-form-update input[name='group_unicode']").val(json_obj[i].group_unicode);
										$("#dialog-form-update input[name='address']").val(json_obj[i].address);
										$("#dialog-form-update input[name='phone']").val(json_obj[i].phone);
										$("#dialog-form-update input[name='fax']").val(json_obj[i].fax);
										$("#dialog-form-update input[name='mobile']").val(json_obj[i].mobile);
										$("#dialog-form-update input[name='email']").val(json_obj[i].email);
										$("#dialog-form-update input[name='master']").val(json_obj[i].master);
							});
						} 

				});			
			update_dialog.dialog("open");
		});		
		//預設表格隱藏
		$("#products-contain").hide();
	});	
</script>
</head>
<body>
	<div class="panel-title">
		<h2>公司資料</h2>
	</div>
	<div class="panel-content">
		<div class="datalistWrap">
			<!--對話窗樣式-修改 -->
				<div id="dialog-form-update" title="修改公司資料">
				<form name="update-dialog-form-post" id="update-dialog-form-post">
					<fieldset>
							<table border="0" height="500">
							<tbody>
							<tr><td><h6 >公司名稱:</h6></td><td ><input type="text"  style="background-color:lightgray;" name="group_name"disabled="disabled"  /></td></tr>
							<tr><td><h6>公司統一編號:</h6></td><td><input type="text"  style="background-color:lightgray;" name="group_unicode"disabled="disabled"/></td></tr>
							<tr><td><h6>公司地址:</h6></td><td><input type="text" name="address" placeholder="修改公司地址"/></td></tr>
							<tr><td><h6>公司電話:</h6></td><td><input type="text" name="phone"placeholder="修改公司電話"/></td></tr>
							<tr><td><h6>公司傳真:</h6></td><td><input type="text" name="fax"placeholder="修改公司傳真"/></td></tr>
							<tr><td><h6>負責人:</h6></td><td><input type="text" name="master"placeholder="修改公負責人"/></td></tr>
							<tr><td><h6>負責人Email:</h6></td><td><input type="text" name="email"placeholder="修改負責人Email"/></td></tr>
							<tr><td><h6>負責人連絡手機:&nbsp&nbsp&nbsp&nbsp</h6></td><td><input type="text" name="mobile"placeholder="修改負責人連絡手機"/></td></tr>
							</tbody>
							</table>	
					</fieldset>
				</form>
			</div>			
			<!-- 第二列 -->
			<div class="row" align="center">
				<div id="products-contain" class="ui-widget">
					<table id="products" class="ui-widget ui-widget-content">
						<thead>
							<tr class="ui-widget-header">
								<th>公司稱謂 </th>
								<th>公司資料</th>
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