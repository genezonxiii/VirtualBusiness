<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
	<meta charset="UTF-8">
	<meta name="viewport" content="width=device-width, initial-scale=1.0, maximum-scale=1.0, user-scalable=no">
	<meta name="format-detection" content="telephone=no">
	<title>北祥股份有限公司 雲端電商管理平台 使用者登入</title>
	<link rel="Shortcut Icon" type="image/x-icon" href="./images/Rockettheme-Ecommerce-Shop.ico" />
	<link rel="stylesheet" href="css/RWD/styles.css">
	<link rel="stylesheet" href="css/1.12.0/jquery-ui.css">

	<script src="js/jquery-1.12.4.js"></script>
	<script src="js/jquery-ui.min.js"></script>
	<script src="js/scripts.js"></script>
</head>

<script>

	function chimg() {
		document.getElementById("validateCodeImg").src = "HandleDrawValidateCode.do?t="
				+ Math.random();
	}
	
	function unicheck() {
		if ($("#uninumber").val() < 1)
			return false;
		var check = 0;
		$.ajax({
			url : "login.do",
			type : "POST",
			cache : false,
			//         async : false,
			delay : 1000,
			data : {
				action : "check_unicode_exist",
				unicode : $("#uninumber").val()
			},
			success : function(data) {
				var json_obj = $.parseJSON(data);
				if ("false" == json_obj.message) {
					$("#uninumber").addClass("error");
					$("#uninumber").after(
							"<span class='error-msg'>未註冊統編!</span>");
					check = 1;
				} else {
					$("#uninumber").removeClass("error");

					check = 2;
				}
			}
		});
	}
	
	function to_login() {
		$(".error").removeClass("error");
		$(".error-msg").remove();
		var wrong = 0;
		if ($("#uninumber").val().length < 1) {
			$("#uninumber").addClass("error");
			$("#uninumber").after("<span class='error-msg'>請輸入統編</span>");
			wrong = 1;
		}
		if ($("#username").val().length < 1) {
			$("#username").addClass("error");
			$("#username").after("<span class='error-msg'>請輸入帳號</span>");
			wrong = 1;
		}
		if ($("#password").val().length < 1) {
			$("#password").addClass("error");
			$("#password").after("<span class='error-msg'>請輸入密碼</span>");
			wrong = 1;
			if ($("#forgetpwd").css("display") == "none") {
				$("#forgetpwd").slideToggle();
			}
		}
		if ($("#verify").val().length < 1) {
			$("#verify").addClass("error");
			$("#verify").after("<span class='error-msg'>請輸入驗證碼</span>");
			wrong = 1;
		}
		if ($("#password").val().length > 10) {
			$("#password").addClass("error");
			$("#password").after("<span class='error-msg'>長度不可超過十個字</span>");
			wrong = 1;
		}

		unicheck();

		if (wrong == 0) {
			$.ajax({
				url : "login.do",
				type : "POST",
				cache : false,
				data : {
					action : "login",
					unicode : $("#uninumber").val(),
					userId : $("#username").val(),
					pswd : $("#password").val(),
					validateCode : $("#verify").val()
				},
				success : function(data) {
					var json_obj = $.parseJSON(data);
					if (json_obj.message == "connect_error") {
						$("#uninumber").after(
								"<span class='error-msg'>連線異常!</span>");
						//$("#my_msg").html("訊號不穩定，<br>&nbsp;請檢查網路連線，或稍後再嘗試。<br>");
						//$("#my_msg").dialog("open");
					}
					if (json_obj.message == "success") {
						location.replace("./welcome.jsp");
					}
					if (json_obj.message == "failure") {
						$("#verify").val("");
						$("#password").val("");
						$("#password").after(
								"<span class='error-msg'>請確認密碼是否正確!</span>");
						chimg();
						if ($("#forgetpwd").css("display") == "none") {
							$("#forgetpwd").slideToggle();
						}
					}
					if (json_obj.message == "uni_failure") {
						$("#verify").val("");
						$("#password").val("");
						$("#uninumber").after(
								"<span class='error-msg'>請確認統編是否正確!</span>");
						chimg();
					}
					if (json_obj.message == "code_failure") {
						$("#verify").val("");
						$("#password").val("");
						$("#verify").addClass("error");
						$("#verify").after(
								"<span class='error-msg'>認證碼錯誤</span>");
					}
					if (json_obj.message == "user_failure") {
						$("#username").addClass("error");
						$("#username").after(
								"<span class='error-msg'>查無此帳號</span>");
						chimg();
						wrong = 1;
					}
				}
			});
		}
	}
	
	$(function() {
		$('body')
			.keypress(function(e) {
				e.preventDefault();
// 				if (e.shiftKey) {
// 					if (e.which == 41) {
// 						e.preventDefault();

// 						if (location.href.indexOf("bers1.eastasia") > -1) {
// 							$("#uninumber").val(
// 									get_sensitive("Demouser_uni"));
// 							$("#username").val(
// 									get_sensitive("Demouser_usr"));
// 							$("#password").val(
// 									get_sensitive("Demouser_pwd"));
// 							$("input").blur();
// 							$("#verify").focus();
// 						} else if (location.href.indexOf("164") > -1
// 								|| location.href.indexOf("local") > -1) {
// 							$("#uninumber").val(
// 									get_sensitive("Myuser_uni"));
// 							$("#username").val(
// 									get_sensitive("Myuser_usr"));
// 							$("#password").val(
// 									get_sensitive("Myuser_pwd"));
// 							$("input").blur();
// 							$("#verify").focus();
// 						}
// 					}
// 				}
			});
		
		$("#validateCodeImg").click(function() {
			chimg();
		});
		
		$("#uninumber").blur(function() {
			$(".error-msg").remove();
			unicheck();
		});
		
		$("#uninumber").focus();
		
		$("#login_btn").click(function() {
			to_login();
		});
		
		$("input").keydown(function(event) {

			if (event.which == 13 && $(this).attr("id") != "username") {
				to_login();
			}
		});
		
		$("#reset_btn").click(function() {
			$(".error").removeClass("error");
			$(".error-msg").remove();
			$("#uninumber").val("");
			$("#username").val("");
			$("#password").val("");
			$("#verify").val("");
			chimg();
		});

		$("#my_msg").dialog({
			title : "登入失敗",
			draggable : true,//防止拖曳
			resizable : false,//防止縮放
			autoOpen : false,
			height : "auto",
			modal : true,
			open : function(event, ui) {
				$(this).parent().children().children(
						'.ui-dialog-titlebar-close').hide();
			},
			show : {
				effect : "bounce",
				duration : 1200
			},
			hide : {
				effect : "fade",
				duration : 300
			},
			buttons : {
				"確認" : function() {
					$(this).dialog("close");
				},
			}
		});
	});
</script>
</head>

<body class="login-body">
	<div class="bkg-cloud"></div>
	
	<div class="aber-logo-wrap">
		<div class="aber-logo"></div>
		<div class="aber-txt">
			<h1>雲端電商管理平台</h1>
		</div>
	</div>
	<!-- /.aber-logo-wrap -->

	<div class="login-wrapper">
		<div class="login-panel-wrap">
			<h2 class="login-txt">使用者登入</h2>
			<%
				if (request.getSession().getAttribute("user_name") != null) {
			%>
			<script>
				top.location.href = "welcome.jsp";
			</script>
			<%
				}
			%>
			<div class="login-panel">
				<form>
					<label for="uninumber"> <span class="block-label">統編</span>
						<input type="text" id="uninumber" value="11913502"> <!-- 				<span class="error-msg">查無貴公司統編</span> -->
					</label> 
					<label for="username"> <span class="block-label">帳號</span>
						<input type="text" id="username" value="melvinchu@melvin.com">
						<!-- <span class="error-msg">長度不能超過10個字</span> -->
					</label> 
					
					<label for="password"> <span class="block-label">密碼</span>
						<input type="password" id="password" value="1234"> <!-- <span class="error-msg">長度不能超過10個字</span> -->
					</label>
					<div class="verify-wrap">
						<label for="verify"> <span class="block-label">認證碼</span>
							<input type="text" id="verify"> <!-- <span class="error-msg">認證錯誤</span> -->
						</label>
						<div class="captcha-wrap">
							<img src="HandleDrawValidateCode.do" id="validateCodeImg" alt="">
						</div>
					</div>
					<!-- /.verify-wrap -->
					
					<div class="login-btn-wrap">
						<a href="#" id="login_btn" class="login-button">登入</a> <a href="#"
							class="login-reset-button" id="reset_btn">清除重填</a>
					</div>
					<!-- /.login-btn-wrap -->
					
					<div class="login-btn-wrap" id='forgetpwd' style='display: none;'>
						<div align='center' style="padding-top: 15px;">
							<a href="./forget.jsp">忘記密碼</a>?
						</div>
					</div>
					<!-- /.login-btn-wrap -->
				</form>
			</div>
			<!-- /.login-panel -->
		</div>
		<!-- /.login-panel-wrap -->

		<div class="login-footer">
			<p>--北祥股份有限公司--</p>
			<span>電話：02-2658-1910 | 傳真：02-2658-1920</span>
		</div>
		<!-- /.login-footer -->

	</div>
	<!-- /.login-wrapper -->
</body>
</html>