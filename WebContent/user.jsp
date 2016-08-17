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
<title>用戶資料</title>
<meta charset="utf-8">
<link rel="Shortcut Icon" type="image/x-icon" href="./images/Rockettheme-Ecommerce-Shop.ico" />
<link rel="stylesheet" href="css/styles.css" />
<link href="<c:url value="css/css.css" />" rel="stylesheet">
<link href="<c:url value="css/jquery.dataTables.min.css" />" rel="stylesheet">
</head>
<body>
	<jsp:include page="template.jsp" flush="true"/>
	<div class="content-wrap" style="margin:56px 0px 28px 120px;">
<script type="text/javascript" src="js/jquery-1.10.2.js"></script>
<script type="text/javascript" src="js/jquery.dataTables.min.js"></script>
<script type="text/javascript" src="js/jquery-ui.min.js"></script>
<script type="text/javascript" src="js/jquery-ui.js"></script>
<script type="text/javascript" src="js/jquery-migrate-1.4.1.min.js"></script>
<script type="text/javascript" src="js/jquery.validate.min.js"></script>
<script type="text/javascript" src="js/additional-methods.min.js"></script>
<script type="text/javascript" src="js/messages_zh_TW.min.js"></script>
<script>
$(function() {
	$('.btn_update').prop('title', '刪除');
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
	//使用jquery.validate來做驗證  注意事項: 1.不能用選擇器方式批次設定，它只取最後一個參數 2.要調用resetForm()而不是reset()
var validator_insert = $("#insert-dialog-form-post").validate({
			rules : {
				user_name:{
					maxlength : 10,
					required : true
				},
				role:{
					required : true,
					maxlength : 10
				},
				email: {
					required : true,
					maxlength : 30
				},
				password: {
					required : true,
					maxlength : 20
				}
			}
	});
	var validator_update = $("#update-dialog-form-post").validate({
		rules : {
			user_name:{
				maxlength : 10,
				required : true
			},
			role:{
				required : true,
				maxlength : 10
			},
			email: {
				required : true,
				maxlength : 30
			},
			password: {
				required : true,
				maxlength : 20
			}
		}
	});
	var user_name = $("#user_name");
		//查詢相關設定
							$.ajax({
									type : "POST",
									url : "user.do",
									data : {
										action : "searh",
										user_name : $("#dialog-form-searh input[name='search_user_name']" ).val()
									},
									success : function(result) {
											var json_obj = $.parseJSON(result);
											var result_table = "";
					
											$.each(json_obj,function(i, item) { 											
													result_table+=
													"<tr><td>"+ json_obj[i].user_name+
													"</td><td>"+json_obj[i].role+ 
													"</td><td>"+json_obj[i].email+
													"</td><td>"
													+ "<div class='table-row-func btn-in-table btn-gray'><i class='fa fa-ellipsis-h'></i>"
													+ "	<div class='table-function-list'>"
													+ "		<button class='btn-in-table btn-darkblue btn_update' title='新增' value='"+ json_obj[i].user_id+   "'name='"+ json_obj[i].user_name+"' ><i class='fa fa-pencil'></i></button>"
													+ "		<button class='btn-in-table btn-alert btn_delete' title='刪除' value='"+ json_obj[i].user_id+   "'name='"+ json_obj[i].user_name+"'><i class='fa fa-trash'></i></button>"
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
												$("#products").dataTable({"language": {"url": "js/dataTables_zh-tw.txt","zeroRecords": "沒有符合的結果"}});
												$(".validateTips").text("");
											}else{
												$("#products-contain").hide();
												$(".validateTips").text("查無此結果");
											}
										}
									});
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
							height : 480,
							width : 450,
							modal : true,
							buttons : [{
										id : "insert",
										text : "新增",
										click : function() {
// 											alert($("#dialog-form-insert select[name='selectinsert']").val());
											if ($('#insert-dialog-form-post').valid()) {
												$.ajax({
													type : "POST",
													url : "user.do",
													data : {
														action : "insert",
														user_name : $("#dialog-form-insert input[name='user_name']").val(),
// 														role : $("#dialog-form-insert input[name='selectinsert']").val(),
														role :$("#dialog-form-insert select[name='selectinsert']").val(),
														email : $("#dialog-form-insert input[name='email']").val(),
														password : $("#dialog-form-insert input[name='password']").val(),
													},
													success : function(result) {
														var json_obj = $.parseJSON(result);
														var result_table = "";
								
														$.each(json_obj,function(i, item) { 											
																result_table+=
																"<tr><td>"+ json_obj[i].user_name+
																"</td><td>"+json_obj[i].role+ 
																"</td><td>"+json_obj[i].email+
																"</td><td>"
																+ "<div class='table-row-func btn-in-table btn-gray'><i class='fa fa-ellipsis-h'></i>"
																+ "	<div class='table-function-list'>"
																+ "		<button class='btn-in-table btn-darkblue btn_update' title='新增' value='"+ json_obj[i].user_id+   "'name='"+ json_obj[i].user_name+"' ><i class='fa fa-pencil'></i></button>"
																+ "		<button class='btn-in-table btn-alert btn_delete' title='刪除' value='"+ json_obj[i].user_id+   "'name='"+ json_obj[i].user_name+"'><i class='fa fa-trash'></i></button>"
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
															$("#products").dataTable({"language": {"url": "js/dataTables_zh-tw.txt","zeroRecords": "沒有符合的結果"}});
															$(".validateTips").text("");
														}else{
															$("#products-contain").hide();
														}
													}
												});
												insert_dialog.dialog("close");
											}
										}
									}, {
										text : "取消",
										click : function() {
											validator_insert.resetForm();
											insert_dialog.dialog("close");
										}
									} ],
							close : function() {
								validator_insert.resetForm();
							}
						});
		var uuid = "";
		var uuid2= ""
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
						url : "user.do",
						data : {
							action : "delete",
							user_id : uuid ,
							operation : uuid2
						},
						success : function(result) {
							var json_obj = $.parseJSON(result);
							var result_table = "";
	
							$.each(json_obj,function(i, item) { 											
									result_table+=
									"<tr><td>"+ json_obj[i].user_name+
									"</td><td>"+json_obj[i].role+ 
									"</td><td>"+json_obj[i].email+
									"</td><td>"
									+ "<div class='table-row-func btn-in-table btn-gray'><i class='fa fa-ellipsis-h'></i>"
									+ "	<div class='table-function-list'>"
									+ "		<button class='btn-in-table btn-darkblue btn_update' title='新增' value='"+ json_obj[i].user_id+   "'name='"+ json_obj[i].user_name+"' ><i class='fa fa-pencil'></i></button>"
									+ "		<button class='btn-in-table btn-alert btn_delete' title='刪除' value='"+ json_obj[i].user_id+   "'name='"+ json_obj[i].user_name+"'><i class='fa fa-trash'></i></button>"
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
								$("#products").dataTable({"language": {"url": "js/dataTables_zh-tw.txt","zeroRecords": "沒有符合的結果"}});
								$(".validateTips").text("");
							}else{
								$("#products-contain").hide();
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
		var user_name = "";
		//修改Dialog相關設定
			update_dialog = $("#dialog-form-update").dialog({
			draggable : false,//防止拖曳
			resizable : false,//防止縮放
			autoOpen : false,
			height : 400,
			width : 450,
			modal : true,
			show : {effect : "blind",duration : 300},
			hide : {effect : "fade",duration : 300},
			buttons : [{
				text : "修改",
				click : function() {
					if ($('#update-dialog-form-post').valid()) {
						$.ajax({
							type : "POST",
							url : "user.do",
							data : {
	 							action : "update",
	 							unit_id : uuid,
	 							user_id : $("#dialog-form-update input[name='user_id']").val(),
	 							user_name : $("#dialog-form-update input[name='user_name']").val(),
	 							role :$("#dialog-form-update select[name='selectupdate']").val(),
	 							email : $("#dialog-form-update input[name='email']").val()
							},
							success : function(result) {
								var json_obj = $.parseJSON(result);
								var result_table = "";
		
								$.each(json_obj,function(i, item) { 											
										result_table+=
										"<tr><td>"+ json_obj[i].user_name+
										"</td><td>"+json_obj[i].role+ 
										"</td><td>"+json_obj[i].email+
										"</td><td>"
										+ "<div class='table-row-func btn-in-table btn-gray'><i class='fa fa-ellipsis-h'></i>"
										+ "	<div class='table-function-list'>"
										+ "		<button class='btn-in-table btn-darkblue btn_update' title='新增' value='"+ json_obj[i].user_id+   "'name='"+ json_obj[i].user_name+"' ><i class='fa fa-pencil'></i></button>"
										+ "		<button class='btn-in-table btn-alert btn_delete' title='刪除' value='"+ json_obj[i].user_id+   "'name='"+ json_obj[i].user_name+"'><i class='fa fa-trash'></i></button>"
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
									$("#products").dataTable({"language": {"url": "js/dataTables_zh-tw.txt","zeroRecords": "沒有符合的結果"}});
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
		//刪除事件聆聽 : 因為聆聽事件動態產生，所以採用delegate來批量處理，節省資源
		$("#products").delegate(".btn_delete", "click", function() {
			uuid = $(this).val();
			user_name = $(this).attr("id");
			confirm_dialog.dialog("open");
		});
		//修改事件聆聽
		$("#products").delegate(".btn_update", "click", function(e) {
			e.preventDefault();
			uuid = $(this).val();
			$("input[name='search_user_name'").val("");
			$.ajax({
				type : "POST",
				url : "user.do",
				data : {
					action : "searh",
					user_name : $("input[name='search_user_name'").val()
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
							if(json_obj[i].user_id==uuid){
								$("#dialog-form-update input[name='user_id']").val(json_obj[i].user_id);
								$("#dialog-form-update input[name='user_name']").val(json_obj[i].user_name);
								$("#dialog-form-update input[name='role']").val(json_obj[i].role);
								$("#dialog-form-update input[name='email']").val(json_obj[i].email);
							}
						});
						} 
				});			
			update_dialog.dialog("open");
		});		
		//新增事件聆聽
		$("#create-productunit").click( function() {
			insert_dialog.dialog("open");
		});
		//預設表格隱藏
		$("#products-contain").hide();
		$("select").change(function(){	
		});
	});
</script>
		<div class="datalistWrap">
			<!--對話窗樣式-確認 -->
			<div id="dialog-confirm" title="確認刪除資料嗎?">
				<p>是否確認刪除該筆資料</p>
			</div>
			<!--對話窗樣式-修改 -->
			<div id="dialog-form-update" title="修改用戶資料">
				<form name="update-dialog-form-post" id="update-dialog-form-post">
					<fieldset>
							<table border="0" height="200">
							<tbody>
							<tr><td><h6>用戶名稱:</h6></td><td><input type="text" name="user_name"  placeholder="輸入用戶名稱"/></td></tr>
							<tr><td><h6>用戶角色:</h6></td><td>	
							<select id="selectok" name="selectupdate"  >
							<option value="管理者">管理者</option>
　							<option value="使用者">使用者</option>
							</select></td></tr>
							<tr><td><h6>Email:&nbsp;&nbsp;</h6></td><td><input type="text" name="email"  placeholder="輸入Email"/></td></tr>
							<tr><td><input type="hidden" name="user_id"  disabled="disabled"/></td></tr>
							</tbody>
							</table>	
					</fieldset>
				</form>
			</div>			
			<!--對話窗樣式-新增 -->
			<div id="dialog-form-insert" title="新增使用者">
				
				<form name="insert-dialog-form-post" id="insert-dialog-form-post">
					<fieldset>
							<table border="0" height="300">
	 							<tbody>
							<tr><td><h6>用戶名稱:</h6></td><td><input type="text" name="user_name" placeholder="輸入用戶名稱"/></td></tr>
							<tr><td><h6>用戶角色:</h6></td><td>	
							<select id="selectok" name="selectinsert"  >
							<option value="管理者">管理者</option>
　							<option value="使用者">使用者</option>
							</select></td></tr>
							<tr><td><h6>Email:&nbsp;&nbsp;</h6></td><td><input type="text" name="email"  placeholder="輸入Email"/></td></tr>
							<tr><td><h6>密碼:</h6></td><td><input type="text" name="password" placeholder="輸入密碼"/></td></tr>
							</tbody>
							</table>	
					</fieldset>
				</form>
			</div>
			<!-- 第一列 -->
			<div class="input-field-wrap">
				<div class="form-wrap">
					<div class="btn-row" >
						<button class="btn btn-exec btn-wide" id="create-productunit">新增用戶</button>
					</div>
				</div><!-- /.form-wrap -->
			</div>
<!-- 			<div class="row" align="center"> -->
<!-- 				<div id="products-serah-create-contain" class="ui-widget"> -->
<!-- 					<button id="create-productunit">新增</button> -->
<!-- 				</div> -->
<!-- 			</div> -->
			<!-- 第二列 -->
			<div class="row search-result-wrap" align="center" style="width:600px;margin:0px auto;">
				<div id="products-contain" class="ui-widget">
					<table id="products" class="result-table">
						<thead>
							<tr class="ui-widget-header">
								<th><p style="width:80px;">用戶名稱</p></th>
								<th><p style="width:80px;">用戶角色</p></th>
								<th><p style="width:80px;">Email</p></th>
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