<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="java.util.*"%>
<!DOCTYPE html>
<html>
<head>
	<meta charset="UTF-8">
	<title>忘記密碼</title>
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
var uid64;
function check_user(){
	warning_msg("");
	$("input").attr("disabled","disabled");
	$("input").animate({backgroundColor:"#DDD"});
	$("#send-mail").animate({opacity: '0'},function() {
		$("#send-mail").attr("onclick","");
		$("#send-mail").html("處理中請稍候...");
		$("#send-mail").css({"background": 'gray'});//#D34200
		$("#send-mail").animate({opacity: '1'});//check_user()
	
		$.ajax({
			type : "POST",
			url : "forget.do",
			data : {
				action : "user_exist",
				email : $("#email").val()
			},
			success : function(result) {
				if(result=="exist"){
					$("#bdy").animate({opacity: '0'},function() {
	        			$("#bdy").css("margin-top","-300px");
	        			$("#bdy").html("<div align='center'><h1>收到您更改密碼的請求</h1><h2><font style='font-size:40px'>已發送確認信!</font></h2><img src='images/sendmail.png' width='400px'><br><br><font style='font-size:24px'>已發送確認信件至&nbsp;<a href='#'>"
	        				+$("#email").val()+"</a>&nbsp;,<br>請點擊您郵件中的連結以完成修改密碼之流程。</font><div>");
	        		});
	        		$("#bdy").animate({opacity: '1'});
					//alert("已發送確認信");
				}else if(result=="Send Message Error?"){
					warning_msg("寄送郵件出現問題<br>請重新整理頁面");
				}else if(result=="Connect error."){
					warning_msg("連線異常<br>請重新整理頁面");
				}else{
					warning_msg("無此使用者");
					$("input").attr("disabled",false);
					$("input").animate({backgroundColor:"#FFF"});
					$("#send-mail").css("background","#D34200");
					$("#send-mail").attr("onclick","check_user();");
					$("#send-mail").html("送出確認信");
					//$("#send-mail").css({"background": '#D34200'});
				}
			}
		});
	});
}
$(function() {
	
	var num=location.href.indexOf("?");
	if(num==-1){
		//location.href="./login.jsp";send-email-interface
		$("#send-email-interface").fadeIn();
	}else{
		uid64 = (location.href).substr(num+5);
		$.ajax({
			type : "POST",
			url : "forget.do",
			data : {
				action : "check_user",
				uid :  uid64
			},
			success : function(result) {
				if(result=="yes"){
					$("#change-pwd-interface").fadeIn();
					$("#pwd").val('');
				}
				if(result=="no"){
					location.href="./login.jsp";
				}
			}
		});
	}
	
// 	$.ajax({
// 		type : "POST",
// 		url : "forget.do",
// 		data : {
// 			action : "send_mail"
// 		},
// 		success : function(result) {
// 			//alert(result);
// 		}
// 	});
	//alert((location.href).substr(num+1));
	$("#dialog-confirm").dialog({
		draggable : true, resizable : false, autoOpen : false,
		height : "auto", width : "auto", modal : true,
		show : {effect : "blind",duration : 300},
		hide : {effect : "fade",duration : 300},
		buttons : {
			"確認更改" : function() {
				$.ajax({
					type : "POST",
					url : "forget.do",
					data : {
						action : "update_pwd",
						uid : uid64,
						pwd : $("#pwd").val()
					},
					success : function(result) {
						warning_msg("");
// 						var json_obj = $.parseJSON(result);
// 						//判斷查詢結果
// 						var resultRunTime = 0;
// 						$.each (json_obj, function (i) {
// 							resultRunTime+=1;
// 						});
// 						if(resultRunTime!=0){
							$("#change-pwd-interface").fadeOut("slow",function(){
								$("#pwd").val("");
								$("#pwd2").val("");
								$("#ok").html("<br><br><br>Dear:　　　　　　　　　　　　<br>密碼修改已完成，前往<a href='./login.jsp'>首頁</a>。");
								$("#ok").fadeIn();
							});
														
// 						}
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
	$("#explane").dialog({
		draggable : true, resizable : false, autoOpen : false,
		width : "auto" ,height : "auto", modal : false,
		show : {effect : "blind", duration : 300 },
		hide : { effect : "fade", duration : 300 },
		open : function(event, ui) {$(this).parent().children().children('.ui-dialog-titlebar-close').hide();},
		buttons : {
			"確定" : function() {$(this).dialog("close");}
		}
	});
	$("#explane").show();
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
		$("#dialog-confirm").html("<div class='delete_msg'>'"+$("#pwd").val().substring(0,$("#pwd").val().length-4)+"****"+"'</div>");
		
	});
	$("#btn-explane").click(function(e) {
		e.preventDefault();
		$("#explane").dialog("open");
		
	});
	$("#send-mail").click(function(e) {
		e.preventDefault();
	});
});
</script>
</head>

<body class="login-body">
	
<!-- 	<br><a id="logout" href="./registry.jsp" class="btn btn-primary" style="float:right;margin-right:20px;">註冊</a> -->
	<div class="bkg-upper"></div>
	<div class="bkg-lower"></div>
	<div class="login-wrapper" id="bdy">
		<h1>智慧電商平台</h1>
		<div class="login-panel-wrap" style="height:464px;">
		<div class="login-panel">
			<div id='change-pwd-interface' style="display:none;">
				<h2>修改密碼</h2>
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
					<br>
				</form>
			</div>
<!-- 			################################################### -->
			<div id='send-email-interface' style="display:none;">
				<h2>忘記密碼?</h2>
				<form>
					<br><br><br><br>
					<label >
						<span class="block-label">輸入email</span>
						<input type="text" id="email"><button class='btn-explanation' id="btn-explane" style="right:-40px;top:-34px;">?</button>
					</label>
					<div style="color:brown;font-size:16px;text-align: center;"></div>
					<br>
					<div class="login-btn-wrap" align="center">
						<a class="login-button btn-exec" style="float:inherit;" id="send-mail" onclick="check_user()">送出確認信</a>
					</div><!-- /.login-btn-wrap -->
					<div class="input-field-wrap" style="padding: 8px 30px;border-bottom:0px;"></div>
				</form>
			</div>
			
		</div><!-- /.login-panel -->
		<div id="ok" align="center" style="color:#005500;font-size:48px;" style="display:none;"></div>
		</div><!-- /.login-panel-wrap -->
		<div class="login-footer">
			北祥股份有限公司<script>if(location.href.indexOf("abers2")>-1){document.write('-1');}</script> <span>服務電話：+886-2-2658-1910 | 傳真：+886-2-2658-1920</span>
		</div><!-- /.login-footer -->
	</div><!-- /.login-wrapper -->
<div id="dialog-confirm" title="是否更改密碼為" style="display:none;"></div>
<div id="explane" title="說明" style="display:none;">
	<div style="padding:0 40px; font-family: Helvetica, Arial, '微軟正黑體', '新細明體', sans-serif;">
		<br>請輸入您註冊時所使用的email,<br>我們將會發郵件至該信箱，<br><br>請點選郵件內連結以修改密碼
	</div>
</div>
</body>
</html>