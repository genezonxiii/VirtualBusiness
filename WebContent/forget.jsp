<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="java.util.*"%>
<!DOCTYPE html>
<html>
<head>
	<meta charset="UTF-8">
	<title>更改密碼?</title>
	<link rel="Shortcut Icon" type="image/x-icon" href="./images/Rockettheme-Ecommerce-Shop.ico" />
    <link rel="stylesheet" href="css/1.12.0/jquery-ui.css">
    <link rel="stylesheet" href="css/styles.css">
  <script src="js/jquery-1.12.4.js"></script>
  <script src="js/jquery-ui.min.js"></script>
  <script src="js/scripts.js"></script>
<style>
input[type="text"], input[type="password"], select, textarea {
	font-family: sans-serif;
}
</style>
<script>
$(function() {
	var num=location.href.indexOf("?");
	if(num==-1){location.href="./login.jsp";}
	
	$.ajax({
		type : "POST",
		url : "forget.do",
		data : {
			action : "send_mail",
			email : $("input[name='password'").val(),
		},
		success : function(result) {
			alert(result);
		}
	});
	//alert((location.href).substr(num+1));
	$("#dialog-confirm").dialog({
		draggable : false, resizable : false, autoOpen : false,
		height : "auto", width : "auto", modal : true,
		show : {effect : "blind",duration : 300},
		hide : {effect : "fade",duration : 300},
		buttons : {
			"確認更改" : function() {
				$.ajax({
					type : "POST",
					url : "changepassword.do",
					data : {
						action : "update",
						password : $("input[name='password'").val(),
					},
					success : function(result) {
						warning_msg("");
						var json_obj = $.parseJSON(result);
						//判斷查詢結果
						var resultRunTime = 0;
						$.each (json_obj, function (i) {
							resultRunTime+=1;
						});
						if(resultRunTime!=0){
							$("input").attr("value","");
							$("#ok").text("密碼修改完成"); 										
						}
					}		
				});
				$(this).dialog("close");
			},
			"取消更改" : function() {
				$(this).dialog("close");
			}
		}
	});
	$("#dialog-confirm").show();
	//密碼修改
	$("#password_btn").click(function(e) {
		e.preventDefault();
		var regexp1=/[a-zA-Z]+/,regexp2=/[0-9]+/;
		if($("#pwd").val().length<6||$("#pwd").val().length>12||!regexp1.test($("#pwd").val())||!regexp2.test($("#pwd").val())){
			$("#pwd").addClass("error");warning_msg("密碼格式不符");return;
		}
		if($("#pwd").val()!=$("#pwd2").val()){
			$("#pwd2").addClass("error");warning_msg("請輸入相同密碼");return;
		}
		warning_msg("");
		$("#pwd").removeClass("error");
		$("#pwd2").removeClass("error");
		$("#dialog-confirm").dialog("open");
		$("#dialog-confirm").html("<div class='delete_msg'>'"+$("#pwd").val().substring(0,$("#pwd").val().length-4)+"****"+"'</div>")
	});
});
</script>
</head>

<body class="login-body">
	
<!-- 	<br><a id="logout" href="./registry.jsp" class="btn btn-primary" style="float:right;margin-right:20px;">註冊</a> -->
	<div class="bkg-upper"></div>
	<div class="bkg-lower"></div>
	<div class="login-wrapper">
		<h1>智慧電商平台</h1>
		<div class="login-panel-wrap">
		<div class="login-panel">
			<h2>修改密碼</h2>
<%
String str=(String)request.getAttribute("action");
if(str!=null){
	//out.println("<script>alert('匯入成功');</script>");
}else{
	//out.println("<script>alert('"+str+"');</script>");
	//out.println("<script>window.location.href = './login.jsp';</script>");
}
%>
			<form>
				<label for="uninumber">
					<span class="block-label">新密碼</span>
					<input type="password" id="pwd">
				</label>
				<label for="username">
					<span class="block-label">新密碼確認 </span>
					<input type="password" id="pwd2">
				</label>
				<br>
				<div class="login-btn-wrap" align="center">
					<a class="login-button btn-exec" style="float:inherit;" id="password_btn">更改密碼</a>
				</div><!-- /.login-btn-wrap -->
				<br>
				<div style="color:brown;font-size:16px;text-align: center;">長度須為6~12位且包含英文及數字</div>
				<div class="input-field-wrap" style="padding: 8px 30px;"></div>
			</form>
		</div><!-- /.login-panel -->
		</div><!-- /.login-panel-wrap -->
		<div class="login-footer">
			北祥股份有限公司<script>if(location.href.indexOf("abers2")>-1){document.write('-1');}</script> <span>服務電話：+886-2-2658-1910 | 傳真：+886-2-2658-1920</span>
		</div><!-- /.login-footer -->
	</div><!-- /.login-wrapper -->
	<div id="dialog-confirm" title="是否更改密碼為" style="display:none;"></div>
</body>
</html>