<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="java.util.*"%>
<!DOCTYPE html>
<html>
<head>
	<meta charset="UTF-8">
	<title>北祥股份有限公司 智慧電商平台 使用者登入</title>
	
	<link rel="Shortcut Icon" type="image/x-icon" href="./images/Rockettheme-Ecommerce-Shop.ico" />
    <link rel="stylesheet" href="css/1.12.0/jquery-ui.css">
    <link rel="stylesheet" href="css/styles.css">
  <script src="js/jquery-1.12.4.js"></script>
  <script src="js/jquery-ui.min.js"></script>

<script>
function chimg(){
	document.getElementById("validateCodeImg").src="HandleDrawValidateCode.do?t=" + Math.random();
}
function unicheck(){//ian根本就沒做這功能 =_=
	if($("#uninumber").val()<1)return;
	$.ajax({
        url : "login.do",
        type : "POST",
        cache : false,
        delay : 1000,
        data : {
        	action : "check_unicode_exist",
        	unicode : $("#uninumber").val()
        },
        success: function(data) {
        	var json_obj = $.parseJSON(data);
        	if("false"==json_obj.message){
        		$("#uninumber").addClass("error");
        		$("#uninumber").after("<span class='error-msg'>未註冊統編!</span>");
        	}else{
        		$("#uninumber").removeClass("error");
        		$(".error-msg").remove();
        	}
        }
    });
}

$(function() {
	$("#validateCodeImg").click(function() {
		chimg();
	});
	$("#uninumber").blur(function(){
		unicheck();
	});
	$("#uninumber").focus();
	$("#login_btn").click(function(){//<span class='error-msg'>請輸入統編</span>
		$(".error").removeClass("error");
		$(".error-msg").remove();
		var wrong=0;
		if($("#uninumber").val().length<1){$("#uninumber").addClass("error");$("#uninumber").after("<span class='error-msg'>請輸入統編</span>");wrong=1;}
		if($("#username").val().length<1){$("#username").addClass("error");$("#username").after("<span class='error-msg'>請輸入帳號</span>");wrong=1;}
		if($("#password").val().length<1){$("#password").addClass("error");$("#password").after("<span class='error-msg'>請輸入密碼</span>");wrong=1;}
		if($("#verify").val().length<1){$("#verify").addClass("error");$("#verify").after("<span class='error-msg'>請輸入驗證碼</span>");wrong=1;}
		if($("#password").val().length>10){$("#password").addClass("error");$("#password").after("<span class='error-msg'>長度不可超過十個字</span>");wrong=1;}
		if(wrong==0){
			unicheck();
			$.ajax({url : "login.do", type : "POST", cache : false,
	            data : {
	            	action : "login",
	            	userId : $("#username").val(),
	            	pswd : $("#password").val(),
	            	validateCode : $("#verify").val()
	            },
	            success: function(data) {
	            	var json_obj = $.parseJSON(data);
	            	if (json_obj.message=="connect_error"){
	            		$("#my_msg").html("訊號不穩定，<br>&nbsp;請檢查網路連線，或稍後再嘗試。<br>");
	            		$("#my_msg").dialog("open");
	            	}
	            	if (json_obj.message=="success"){window.location.href = "./welcome.jsp";}
	            	if (json_obj.message=="failure"){
	            		$("#verify").val("");
	            		$("#password").val("");
	            		$("#my_msg").html("請確認密碼是否正確");
	            		$("#my_msg").dialog("open");
	            		chimg();
	            	}
	            	if (json_obj.message=="code_failure") {
	            		$("#verify").val("");
						$("#password").val("");
						$("#verify").addClass("error");
						$("#verify").after("<span class='error-msg'>驗證碼錯誤</span>");
	            	}
	            	if (json_obj.message=="user_failure"){
	            		$("#username").addClass("error");
	            		$("#username").after("<span class='error-msg'>查無此帳號</span>");
	            		chimg();
	            		wrong=1;
	            	}
	            }
	        });
		}
 	});
	$("input").keydown(function (event) {
        if (event.which == 13) {
        	$(".error").removeClass("error");$(".error-msg").remove();var wrong=0;if($("#uninumber").val().length<1){$("#uninumber").addClass("error");$("#uninumber").after("<span class='error-msg'>請輸入統編</span>");wrong=1;}if($("#username").val().length<1){$("#username").addClass("error");$("#username").after("<span class='error-msg'>請輸入帳號</span>");wrong=1;}if($("#password").val().length<1){$("#password").addClass("error");$("#password").after("<span class='error-msg'>請輸入密碼</span>");wrong=1;}if($("#verify").val().length<1){$("#verify").addClass("error");$("#verify").after("<span class='error-msg'>請輸入驗證碼</span>");wrong=1;}if($("#password").val().length>10){$("#password").addClass("error");$("#password").after("<span class='error-msg'>長度不可超過十個字</span>");wrong=1;}if(wrong==0){unicheck();$.ajax({url : "login.do", type : "POST", cache : false,data : {action : "login",userId : $("#username").val(),pswd : $("#password").val(),validateCode : $("#verify").val() },success: function(data) {var json_obj = $.parseJSON(data);if (json_obj.message=="connect_error"){$("#my_msg").html("訊號不穩定，<br>&nbsp;請檢查網路連線，或稍後再嘗試。<br>");$("#my_msg").dialog("open");}if (json_obj.message=="success"){window.location.href = "./welcome.jsp";}if (json_obj.message=="failure"){$("#verify").val("");$("#password").val("");$("#my_msg").html("請確認密碼是否正確");$("#my_msg").dialog("open");chimg();}if (json_obj.message=="code_failure") {$("#verify").val("");$("#password").val("");$("#verify").addClass("error");$("#verify").after("<span class='error-msg'>驗證碼錯誤</span>");}if (json_obj.message=="user_failure"){$("#username").addClass("error");$("#username").after("<span class='error-msg'>查無此帳號</span>");wrong=1;chimg();} }});}
        }
    }); 
	$("#reset_btn").click(function(){
		$(".error").removeClass("error");
		$(".error-msg").remove();
		$("#uninumber").val("");
		$("#username").val("");
		$("#password").val("");
		$("#verify").val("");
		chimg();
	});
	
	
// 	$("input").blur(function(){
// 		var str="#"+$(this).attr("id")+"-err";
// 		$(str).html("");
// 	});
// 	$("#username").blur(function(){
// 	});
	$("#my_msg").dialog({
		title : "登入失敗",
		draggable : false,//防止拖曳
		resizable : false,//防止縮放
		autoOpen : false,
		height : "auto",
		modal : true,
		open : function(event, ui) {$(this).parent().children().children('.ui-dialog-titlebar-close').hide();},
		show : {effect : "bounce",duration : 800},
		hide : {effect : "fade",duration : 300},
		buttons : {
			"確認" : function() {$(this).dialog("close");},
		}
	});

	
});
</script>
</head>

<body class="login-body">
	
	<div class="bkg-upper"></div>
	<div class="bkg-lower"></div>
	<div class="login-wrapper">
	
		<h1>智慧電商平台</h1>

		<div class="login-panel-wrap">
		<div class="login-panel">
			<h2>使用者登入</h2>
			<%if(request.getSession().getAttribute("user_name")!=null){%>
				<script>top.location.href="welcome.jsp";</script>
			<%}%>
			<form>
				<label for="uninumber">
					<span class="block-label">統編</span>
					<input type="text" id="uninumber">
<!-- 					<span class="error-msg" id="uninumber-err"> &nbsp; </span> -->
				</label>
				<label for="username">
					<span class="block-label">帳號</span>
					<input type="text" id="username">
<!-- 					<span class="error-msg" id="username-err"> &nbsp; </span> -->
				</label>
				<label for="password">
					<span class="block-label">密碼</span>
					<input type="password" id="password">
<!-- 					<span class="error-msg" id="password-err"> &nbsp; </span> -->
				</label>
				<div class="verify-wrap">
					<label for="verify">
						<span class="block-label">認證碼</span>
						<input type="text" id="verify">
<!-- 						<span class="error-msg" id="verify-err"> &nbsp; </span> -->
					</label>
					<div class="captcha-wrap">
						<img title="看不清楚? 點擊圖片可換一張" src="HandleDrawValidateCode.do" id="validateCodeImg">
					</div>
				</div><!-- /.verify-wrap -->
				<div class="login-btn-wrap">
					<a class="login-button" id="login_btn">登入</a>
					<a class="login-reset-button" id="reset_btn">清除重填</a>					
				</div><!-- /.login-btn-wrap -->
			</form>
		</div><!-- /.login-panel -->
		</div><!-- /.login-panel-wrap -->

		<div class="login-footer">
			北祥股份有限公司 <span>服務電話：+886-2-2658-1910 | 傳真：+886-2-2658-1920</span>
		</div><!-- /.login-footer -->

	</div><!-- /.login-wrapper -->
	
	<div id="my_msg"></div>
</body>
</html>