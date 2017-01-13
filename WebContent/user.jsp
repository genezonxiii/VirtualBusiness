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
<title>使用者資料</title>
<meta charset="utf-8">
<link rel="Shortcut Icon" type="image/x-icon" href="./images/Rockettheme-Ecommerce-Shop.ico" />
<link rel="stylesheet" href="css/styles.css" />
<link href="<c:url value="css/css.css" />" rel="stylesheet">
<link href="<c:url value="css/jquery.dataTables.min.css" />" rel="stylesheet">
</head>
<body>
	<jsp:include page="template.jsp" flush="true"/>
	<div class="content-wrap" >
		<div class='bdyplane' style="opacity:0">
		<div class="datalistWrap">
			<!--對話窗樣式-確認 -->
			<div id="dialog-confirm" title="是否刪除使用者?" style="display:none;">
				<p>是否確認刪除該筆資料</p>
			</div>
			<!--對話窗樣式-修改 -->
			<div id="dialog-form-update" title="修改使用者資料" style="display:none;">
				<form name="update-dialog-form-post" id="update-dialog-form-post">
					<fieldset>
							<table class="form-table">
								<tbody>
									<tr><td>使用者名稱：</td><td><input type="text" name="user_name" placeholder="輸入使用者名稱"/></td></tr>
									<tr><td>使用者角色：</td><td>	
										<select id="selectok" name="selectupdate" >
										<option value="1">管理者</option>
			　							<option value="2">使用者</option>
										</select></td></tr>
									<tr><td>Email：</td><td><input type="text" name="email" id="update_email" placeholder="輸入Email"/><a id="update_regmsg"></a></td></tr>
									<tr><td><input type="hidden" name="user_id"  disabled="disabled"/></td></tr>
								</tbody>
							</table>	
					</fieldset>
				</form>
			</div>			
			<!--對話窗樣式-新增 -->
			<div id="dialog-form-insert" title="新增使用者" style="display:none;">
				
				<form name="insert-dialog-form-post" id="insert-dialog-form-post">
					<fieldset>
							<table class="form-table">
	 							<tbody>
									<tr><td>使用者名稱：</td><td><input type="text" name="user_name" placeholder="輸入使用者名稱"/></td></tr>
									<tr><td>使用者角色：</td><td>	
										<select id="selectok" name="selectinsert">
										<option value="1">管理者</option>
			　							<option value="2">使用者</option>
										</select></td></tr>
									<tr><td>Email：</td><td><input type="text" name="email" id="insert_email" placeholder="輸入Email"/><a id="insert_regmsg"></a></td></tr>
									<tr><td>密碼：</td><td><input type="password" name="password" placeholder="輸入密碼"/></td></tr>
								</tbody>
							</table>	
					</fieldset>
				</form>
			</div>
			<!-- 第一列 -->
			<div class="input-field-wrap">
				<div class="form-wrap">
					<div class="btn-row" >
						<button class="btn btn-exec btn-wide" id="create-productunit">新增使用者</button>
					</div>
				</div><!-- /.form-wrap -->
			</div>
			<!-- 第二列 -->
			<div class="row search-result-wrap" align="center" style="width:800px;margin:0px auto;">
				<div id="products-contain" class="ui-widget" style="display:none">
					<table id="products" class="result-table">
						<thead>
							<tr>
								<th style="min-width:80px;">使用者名稱</th>
								<th style="min-width:80px; background-image: none !important;">使用者角色</th>
								<th style="min-width:80px; background-image: none !important;">Email</th>
								<th style="min-width:80px; background-image: none !important;">功能</th>
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
	</div>
<script type="text/javascript" src="js/jquery-1.10.2.js"></script>
<script type="text/javascript" src="js/jquery.dataTables.min.js"></script>
<script type="text/javascript" src="js/jquery-ui.min.js"></script>
<script type="text/javascript" src="js/jquery-ui.js"></script>
<script type="text/javascript" src="js/jquery-migrate-1.4.1.min.js"></script>
<script type="text/javascript" src="js/jquery.validate.min.js"></script>
<script type="text/javascript" src="js/additional-methods.min.js"></script>
<script type="text/javascript" src="js/messages_zh_TW.min.js"></script>
<script>
function character(value){
	if(value==1){return "管理者";}
	if(value==2){return "使用者";}
	return "*資料格式錯誤*";
}

function draw_user(parameter){
	$.ajax({
		type : "POST",
		url : "user.do",
		data : parameter,
		success : function(result) {
				var json_obj = $.parseJSON(result);
				var result_table = "";
				
				$.each(json_obj,function(i, item) { 											
						result_table+=
						"<tr value='"+json_obj[i].user_id+"'><td name='name' value='"+json_obj[i].user_name+"'>"+ json_obj[i].user_name+
						"</td><td name='character' value='"+json_obj[i].role+"'>"+character(json_obj[i].role)+ 
						"</td><td name='email' value='"+json_obj[i].email+"'>"+json_obj[i].email+
						"</td><td>"
						+ "<div class='table-row-func btn-in-table btn-gray'><i class='fa fa-ellipsis-h'></i>"
						+ "	<div class='table-function-list'>"
						+ "		<button class='btn-in-table btn-darkblue btn_update' title='修改' value='"+ json_obj[i].user_id+   "'name='"+ json_obj[i].user_name+"' ><i class='fa fa-pencil'></i></button>"
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
					$("#products-contain").css({"opacity":"0"});
					warning_msg("---讀取中請稍候---");
					$("#products tbody").html(result_table);
					$("#products").dataTable({
// 						"bRetrieve": true,
// 						"order": [],
						"language": {"url": "js/dataTables_zh-tw.txt","zeroRecords": "沒有符合的結果"}});
					tooltip('btn_update');
					tooltip('btn_delete');
					$("#products-contain").animate({"opacity":"0.01"},1);
					$("#products-contain").animate({"opacity":"1"},300);
					warning_msg("");
				}else{
					warning_msg("---查無資料---");
					$("#products-contain").hide();
				}
			}
		});
}

	$(function() {
		$(".bdyplane").animate({"opacity":"1"});
		var this_email="";
		var reg=0;
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
						email : true,
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
					email : true,
					maxlength : 30
				},
				password: {
					required : true,
					maxlength : 20
				}
			}
		});
			//查詢相關設定
		var tmp={
				action : "searh",
				user_name : $("#dialog-form-searh input[name='search_user_name']" ).val()
			};
		draw_user(tmp);
	
		//新增Dialog相關設定
		insert_dialog = $("#dialog-form-insert").dialog({
			draggable : true, resizable : false, autoOpen : false,
			height : "auto", width : "auto", modal : true,
			show : {effect : "blind",duration : 300},
			hide : {effect : "fade",duration : 300},
			buttons : [{
						id : "insert",
						text : "新增",
						click : function() {
							if ($('#insert-dialog-form-post').valid()&&reg==0) {
								var tmp={
										action : "insert",
										user_name : $("#dialog-form-insert input[name='user_name']").val(),
										role :$("#dialog-form-insert select[name='selectinsert']").val(),
										email : $("#dialog-form-insert input[name='email']").val(),
										password : $("#dialog-form-insert input[name='password']").val(),
									};
								draw_user(tmp);
								insert_dialog.dialog("close");
								$("#insert-dialog-form-post").trigger("reset");
							}
						}
					}, {
						text : "取消",
						click : function() {
							validator_insert.resetForm();
							insert_dialog.dialog("close");
							$("#insert-dialog-form-post").trigger("reset");
						}
					} ],
			close : function() {
				validator_insert.resetForm();
				$("#insert-dialog-form-post").trigger("reset");
			}
		});
		$("#dialog-form-insert").show();
// 		var uuid = "";
// 		var uuid2= ""
		//確認Dialog相關設定(刪除功能)
		confirm_dialog = $("#dialog-confirm").dialog({
			draggable : true, resizable : false, autoOpen : false,
			height : "auto", width : "auto", modal : true,
			show : {effect : "blind",duration : 300},
			hide : {effect : "fade",duration : 300},
			buttons : {
				"確認刪除" : function() {
					var tmp={
							action : "delete",
							user_id : $(this).val() ,
							operation : ""
						};
					draw_user(tmp);
					$(this).dialog("close");
				},
				"取消刪除" : function() {
// 					alert(uuid2);
// 					alert($(this).val().find("td[name='name']").html());
					$(this).dialog("close");
				}
			}
		});
		$("#dialog-confirm").show();
		//修改Dialog相關設定
		update_dialog = $("#dialog-form-update").dialog({
			draggable : true, resizable : false, autoOpen : false,
			height : "auto", width : "auto", modal : true,
			show : {effect : "blind",duration : 300},
			hide : {effect : "fade",duration : 300},
			buttons : [{
				text : "修改",
				click : function() {
					if ($('#update-dialog-form-post').valid()&&reg==0) {
						var tmp={
	 							action : "update",
	 							unit_id : $(this).val(),
	 							user_id : $("#dialog-form-update input[name='user_id']").val(),
	 							user_name : $("#dialog-form-update input[name='user_name']").val(),
	 							role :$("#dialog-form-update select[name='selectupdate']").val(),
	 							email : $("#dialog-form-update input[name='email']").val()
							};
						draw_user(tmp);
						update_dialog.dialog("close");
						$("#update-dialog-form-post").trigger("reset");
					}
				}
			}, {
				text : "取消",
				click : function() {
					validator_update.resetForm();
					update_dialog.dialog("close");
					$("#update-dialog-form-post").trigger("reset");
				}
			} ],
			close : function() {
				validator_update.resetForm();
				$("#update-dialog-form-post").trigger("reset");
			}
		});	
		$("#dialog-form-update").show();
		//刪除事件聆聽 : 因為聆聽事件動態產生，所以採用delegate來批量處理，節省資源
		$("#products").delegate(".btn_delete", "click", function() {
			$("#dialog-confirm").val($(this).val());
			$("#dialog-confirm").html("<div class='delete_msg'>'"+$(this).attr("name")+"'</div>");
// 			$("#dialog-confirm").val($(this).parents("tr"));
// 			user_name = $(this).attr("id");
			confirm_dialog.dialog("open");
		});
		//修改事件聆聽
		$("#products").delegate(".btn_update", "click", function(e) {
			e.preventDefault();
// 			$("#dialog-confirm").val($(this).parents("tr"))
			$("#dialog-form-update").val($(this).val());
// 			alert($(this).parents("tr").find("td[name='name']").attr("value"));
			$("#dialog-form-update input[name='user_id']").val($(this).val());
			$("#dialog-form-update input[name='user_name']").val($(this).parents("tr").find("td[name='name']").attr("value"));
			$("#dialog-form-update select[name='selectupdate']").val($(this).parents("tr").find("td[name='character']").attr("value"));
			$("#dialog-form-update input[name='email']").val($(this).parents("tr").find("td[name='email']").attr("value"));
			this_email=$(this).parents("tr").find("td[name='email']").attr("value");
			update_dialog.dialog("open");
		});		
		//新增事件聆聽
		$("#create-productunit").click( function() {
			insert_dialog.dialog("open");
			$("#insert-dialog-form-post").trigger("reset");
		});
		$("#insert_email").blur(function(){
			$.ajax({
				type : "POST",
				url : "user.do",
				data : {
					action : "check_email",
					email : $(this).val()
				},
				success : function(result) {
					if("true"==result){
						$("#insert_regmsg").html("<font color=red>&nbsp;此信箱已有人註冊!</font>");
						reg=1;
					}else{
						$("#insert_regmsg").html("");
						reg=0;
					}
				}
			});
		});
		$("#update_email").blur(function(){
			//alert(this_email);
			if(this_email!=$(this).val()){
				$.ajax({
					type : "POST",
					url : "user.do",
					data : {
						action : "check_email",
						email : $(this).val()
					},
					success : function(result) {
						if("true"==result){
							$("#update_regmsg").html("<font color=red>&nbsp;此信箱已有人註冊!</font>");
							reg=1;
						}else{
							$("#update_regmsg").html("");
							reg=0;
						}
					}
				});
			}
		});
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
</body>
</html>