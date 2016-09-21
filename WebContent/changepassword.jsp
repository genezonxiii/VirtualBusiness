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
<title>密碼修改</title>
<meta charset="utf-8">
<link rel="Shortcut Icon" type="image/x-icon" href="./images/Rockettheme-Ecommerce-Shop.ico" />
<link rel="stylesheet" href="css/styles.css" />
<link href="<c:url value="css/css.css" />" rel="stylesheet">
<link href="<c:url value="css/jquery.dataTables.min.css" />" rel="stylesheet">
<link rel="stylesheet" href="css/1.11.4/jquery-ui.css">
</head>
<body>
	<jsp:include page="template.jsp" flush="true"/>
	<div class="content-wrap" >
		<div class='bdyplane' style="opacity:0">
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
		$(".bdyplane").animate({"opacity":"1"});
		$("input[name='password']").bind('focus', function(){ 
			warning_msg("");
			$("#ok").text(""); 
		});
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
			$("#dialog-confirm").dialog("open");
			$("#dialog-confirm").html("<div class='delete_msg'>'"+$("#pwd").val().substring(0,$("#pwd").val().length-4)+"****"+"'</div>")
			
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
			<!-- 第一列 -->
		<div class="input-field-wrap">
			<div class="form-wrap">
				<div class="form-row">
					<label for="">
						<span class="block-label">新密碼</span>
						<input type="password" id="pwd" style="width:200px;" name="password">
					</label>
				</div>
				<div class="form-row">
					<label for="">
						<span class="block-label">新密碼確認</span>
						<input type="password" id="pwd2" style="width:200px;" name="password2">
					</label>
				</div>
				<font color=brown>長度須為6~12位且包含英文及數字。</font><br><br>
				<div class="btn-row">
					<button id="password_btn" class="btn btn-exec btn-wide">更改密碼</button>
				</div>
			</div><!-- /.form-wrap -->
		</div><!-- /.input-field-wrap -->
		<div id="ok" align="center" style="color:#00CC00;font-size:32px;"></div>
	</div>
	</div>
	<div id="dialog-confirm" title="是否更改密碼為" style="display:none;"></div>
</body>
</html>