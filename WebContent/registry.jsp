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
<style>
a {
    color: #00c1c3;
    font-weight: bold;
}
</style>
<script>

function unicheck(){
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
        		$("#uninumber").removeClass("error");
        		$(".error-msg").remove();
        		$("#uninumber").addClass("error");
        		$("#uninumber").after("<span class='error-msg'>未註冊統編!</span>");
        	}else{
        		$("#uninumber").removeClass("error");
        		$(".error-msg").remove();
        	}
        }
    });
}
function to_login(){//<span class='error-msg'>請輸入統編</span>
// 	$(".error").removeClass("error");
// 	$(".error-msg").remove();
// 	var wrong=0;
// 	if($("#uninumber").val().length<1){$("#uninumber").addClass("error");$("#uninumber").after("<span class='error-msg'>請輸入統編</span>");wrong=1;}
// 	if($("#username").val().length<1){$("#username").addClass("error");$("#username").after("<span class='error-msg'>請輸入帳號</span>");wrong=1;}
// 	if($("#password").val().length<1){$("#password").addClass("error");$("#password").after("<span class='error-msg'>請輸入密碼</span>");wrong=1;}
// 	if($("#verify").val().length<1){$("#verify").addClass("error");$("#verify").after("<span class='error-msg'>請輸入驗證碼</span>");wrong=1;}
// 	if($("#password").val().length>10){$("#password").addClass("error");$("#password").after("<span class='error-msg'>長度不可超過十個字</span>");wrong=1;}
// 	if(wrong==0){
// 		unicheck();
// 		$.ajax({url : "login.do", type : "POST", cache : false,
//             data : {
//             	action : "login",
//             	unicode : $("#uninumber").val(),
//             	userId : $("#username").val(),
//             	pswd : $("#password").val(),
//             	validateCode : $("#verify").val()
//             },
//             success: function(data) {
//             	var json_obj = $.parseJSON(data);
//             	if (json_obj.message=="connect_error"){
//             		$("#uninumber").after("<span class='error-msg'>訊號不穩定，請稍後再試!</span>");
//             		//$("#my_msg").html("訊號不穩定，<br>&nbsp;請檢查網路連線，或稍後再嘗試。<br>");
//             		//$("#my_msg").dialog("open");
//             	}
//             	if (json_obj.message=="success"){location.replace("./welcome.jsp");}
//             	if (json_obj.message=="failure"){
//             		$("#verify").val("");
//             		$("#password").val("");
//             		$("#password").after("<span class='error-msg'>請確認密碼與統編是否正確!</span>");
//             		//$("#my_msg").html("請確認密碼與統編是否正確");
//             		//$("#my_msg").dialog("open");
//             		chimg();
//             	}
//             	if (json_obj.message=="code_failure") {
//             		$("#verify").val("");
// 					$("#password").val("");
// 					$("#verify").addClass("error");
// 					$("#verify").after("<span class='error-msg'>驗證碼錯誤</span>");
//             	}
//             	if (json_obj.message=="user_failure"){
//             		$("#username").addClass("error");
//             		$("#username").after("<span class='error-msg'>查無此帳號</span>");
//             		chimg();
//             		wrong=1;
//             	}
//             }
//         });
// 	}
}
function send_mail(){
	$.ajax({url : "registry.do", type : "POST", cache : false,
        data : {
        	action : "send_mail",
        	name: "張啟靈",
        	to : "g4ru04@gmail.com",
        	user_id : "111111111111111"
        },
        success: function(data) {
        	alert(data);
        	if("success"==data){
        		alert('跳轉感謝畫面');
        	}else{
        		alert('寫信失敗'');
        	}
        }
	});
}
$(function() {
	$("#uninumber").focus();
	$("#uninumber").blur(function(){
		unicheck();
	});
// 	$("#login_btn").click(function(){
// 		to_login();
//  	});
// 	$("input").keydown(function (event) {
//         if (event.which == 13) {
//         	to_login();
//         }
//     }); 
// 	$("#reset_btn").click(function(){
// 		$(".error").removeClass("error");
// 		$(".error-msg").remove();
// 		$("#uninumber").val("");
// 		$("#username").val("");
// 		$("#password").val("");
// 		$("#verify").val("");
// 		chimg();
// 	});
	
	
	$("#my_msg").dialog({
		title : "註冊失敗",
		draggable : false,//防止拖曳
		resizable : false,//防止縮放
		autoOpen : false,
		height : "auto",
		modal : true,
		open : function(event, ui) {$(this).parent().children().children('.ui-dialog-titlebar-close').hide();},
		show : {effect : "bounce",duration : 1200},
		hide : {effect : "fade",duration : 300},
		buttons : {
			"確認" : function() {$(this).dialog("close");},
		}
	});
});
</script>
</head>

<body class="login-body">
	
<!-- 	<br><a id="logout" href="./login.jsp" class="btn btn-primary" style="float:right;margin-right:20px;">登入</a> -->
	<div class="bkg-upper"></div>
	<div class="bkg-lower"></div>
	<div class="login-wrapper" style="top:20%;margin-top:-150px;">
		<h1>註冊</h1>
		<button onclick="send_mail();">##@_@##</button>
		<div class="login-panel-wrap">
		<div class="registry-panel">
<!-- 			<h3>註冊</h3> -->
			<form>
				<table class="normal-table">
					<tr><td>
						<span class="block-label">公司統編:</span>
						<input type="text" id="uninumber">
					</td><td>
						<span class="block-label">公司名稱:</span>
						<input type="text" id="">
					</td></tr>
					<tr><td colspan=2 style="text-align:center">
						<span class="block-label">姓名:</span>
						<input type="text" id="" style="width:95%;">
					</td></tr>
					<tr><td colspan=2>
						<span class="block-label">帳號:(電子郵件)</span>
						<input type="text" id=""style="width:95%;">
					</td></tr>
					<tr><td colspan=2 style="text-align:center">
						<font color=red>密碼長度須為6~12位，須包含英文及數字，英文有區分大小寫</font>
					</td></tr>
					<tr><td>
						<span class="block-label">密碼:</span>
						<input type="password" id="">
					</td><td>
						<span class="block-label">密碼確認:</span>
						<input type="password" id="" >
					</td></tr>
					<tr><td>
						<span class="block-label">驗證碼:</span>
						<input type="text" id="verify">
					</td><td>
					<span class="block-label">點擊可換圖片</span>
						<img title="看不清楚? 點擊圖片可換一張" src="HandleDrawValidateCode.do" id="validateCodeImg" onclick='document.getElementById("validateCodeImg").src="HandleDrawValidateCode.do?t=" + Math.random();' style="height:38px;">
					</td></tr>
					<tr><td colspan=2>
<!-- 						<input type='checkbox' style="position:static;">我同意接受智慧電商平台<a href="#">服務條款</a>與<a href="#">隱私權聲明</a> -->
						 <input type='checkbox' style="position:static;">我同意接受智慧電商平台<a href="./servicepolicy.html">服務條款</a>與<a href="./privacy.html">隱私權聲明</a>
					</td></tr>
					<tr><td colspan=2>
						<a class="login-button" id="" style="width:100%;">註冊</a>
					</td></tr>
					<tr><td colspan=2>
						已經有智慧電商平台的帳號嗎? <a href="./login.jsp">登入</a>
					</td></tr>
				</table>
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