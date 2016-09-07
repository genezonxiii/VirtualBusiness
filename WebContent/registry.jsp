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
  <script type="text/javascript" src="js/jquery.validate.min.js"></script>
  <script type="text/javascript" src="js/messages_zh_TW.min.js"></script>
<style>
a {
    color: #0011c3;
    font-weight: bold;
}
</style>
<script>
function checkunicode(uniString){       
	var res = uniString.split("");
	var sum = 0 ;
	var para=[1,2,1,2,1,2,4,1] ;
	
	for (i = 0; i <= 7; i++) {
		var inttemp = parseInt(res[i]) * para[i];   
		
		if(inttemp >= 9){   
		  var s = inttemp + "";
		  
		  n1 = s.substring(0,1) * 1;
		  n2 = s.substring(1,2) * 1;
		  inttemp = n1 + n2;           
		}
		sum += inttemp; 
	}
 
	if( sum % 10 == 0 ){ 
		return true;
	} else {
		if(res[6] == 7){
			sum += 1 ;
			if( sum % 10 == 0 ){
				return true;
			} else {
				return false;
			}
		} else {
			return false;
		} 
	}
}
function unicheck(){
	
// 	$("#register").fadeOut();
// 	$("#sys").fadeIn();
	
	if($("#uninumber").val()<1)return false;
	if(!checkunicode($("#uninumber").val())){
		$(".error-msg").remove();
		$("#uninumber").addClass("error");
		$("#uninumber").after("<span class='error-msg'>非正式統編!</span>");
		return false;
	}
	var check=0;
	$.ajax({
        url : "login.do",
        type : "POST",
        cache : false,
        async : false,
        delay : 1000,
        data : {
        	action : "check_unicode_exist",
        	unicode : $("#uninumber").val()
        },
        success: function(data) {
        	var json_obj = $.parseJSON(data);
        	if("true"==json_obj.message){
        		$("#uninumber").removeClass("error");
        		$(".error-msg").remove();
        		$("#uninumber").addClass("error");
        		$("#uninumber").after("<span class='error-msg'>此統編已被註冊!</span>");
        		check=1;
        	}else{
        		$("#uninumber").removeClass("error");
        		$(".error-msg").remove();
        		check=2;
        	}
        }
    });
	if(check==2){return true;}else{return false;}
}
function regis(){
	//$("#regis-form").valid();
	//alert($("#email").val().indexOf("@")==-1);
	var wrong=0,regexp1=/[a-zA-Z]+/,regexp2=/[0-9]+/,regexp3=/[a-zA-Z0-9]+@[a-zA-Z0-9]+\.com.*/;
	$(".error").removeClass("error");
 	$(".error-msg").remove();
	if($("#uninumber").val().length<1){$("#uninumber").addClass("error");$("#uninumber").after("<span class='error-msg'>請輸入統編</span>");wrong=1;}
	if($("#corporation").val().length<1){$("#corporation").addClass("error");$("#corporation").after("<span class='error-msg'>請輸入公司名稱</span>");wrong=1;}
	if($("#name").val().length<1){$("#name").addClass("error");$("#name").after("<span class='error-msg'>請輸入姓名</span>");wrong=1;}
	if($("#email").val().length<1){$("#email").addClass("error");$("#email").after("<span class='error-msg'>請輸入帳號</span>");wrong=1;}
	if($("#pwd").val().length<1){$("#pwd").addClass("error");$("#pwd").after("<span class='error-msg'>請輸入密碼</span>");wrong=1;}
	if($("#pwd2").val().length<1){$("#pwd2").addClass("error");$("#pwd2").after("<span class='error-msg'>請再次確認密碼</span>");wrong=1;}
	if($("#verify").val().length<1){$("#verify").addClass("error");$("#verify").after("<span class='error-msg'>請輸入驗證碼</span>");wrong=1;}
	if(!$("#agree").prop("checked")){$("#privacy").after("<font class='error-msg' style='top:-17px;' color=red>←請閱讀後勾選同意</font>");wrong=1;}
	if(wrong){return;}
	if(!regexp3.test($("#email").val())){
		$("#email").addClass("error");$("#email").after("<span class='error-msg'>非正式email</span>");wrong=1;
	}
	if($("#pwd").val().length<6||$("#pwd").val().length>12||!regexp1.test($("#pwd").val())||!regexp2.test($("#pwd").val())){
		$("#pwd").addClass("error");$("#pwd").after("<span class='error-msg'>密碼格式不符</span>");wrong=1;
	}
	if($("#pwd").val()!=$("#pwd2").val()){
		$("#pwd2").addClass("error");$("#pwd2").after("<span class='error-msg'>請輸入相同密碼</span>");wrong=1;
	}
	if(!unicheck()){wrong=1;}
	if(wrong){return;}
	$.ajax({url : "registry.do", type : "POST", cache : false,
		data : {
			action : "registry",
			uninumber : $("#uninumber").val(),
			corporation : $("#corporation").val(),
			name : $("#name").val(),
			email : $("#email").val(),
			pwd : $("#pwd").val(),
			verify : $("#verify").val()
		}, success: function(reg_id) {
			$("input").attr("disabled","disabled");
			$("input").animate({backgroundColor:"#DDD"});
			$("#register").animate({opacity: '0'},function() {
				$("#register").attr("onclick","");
				$("#register").html("系統處理中，請稍候...");
				$("#register").animate({backgroundColor: 'gray'});
				$("#register").animate({opacity: '1'});
			});
			send_mail(reg_id);//regid
		}
	});
}
function send_mail(reg_id){
	$.ajax({url : "registry.do", type : "POST", cache : false,
        data : {
        	action : "send_mail",
        	name: $("#name").val(),
        	to : $("#email").val(),
        	user_id : reg_id
        },
        success: function(data) {
        	if("success"==data){
        		$("#bdy").animate({opacity: '0'},function() {
        			$("#bdy").css("margin-top","-100px");
        			$("#bdy").html("<div align='center'><h1>註冊完成</h1><h2><font style='font-size:40px'>已發送驗證郵件!</font></h2><img src='images/sendmail.png' width='400px'><br><br><font style='font-size:24px'>已發送驗證郵件至<a href='#'>"
        				+$("#email").val()+"</a>,<br>請點擊您郵件中的連結來驗證您的帳戶並完成註冊。</font><div>");
        		});
        		$("#bdy").animate({opacity: '1'});
        	}else{
        		$("#bdy").animate({opacity: '0'},function() {
	        		$("#bdy").css("margin-top","0px");
	        		$("#bdy").html("<div align='center'><font style='font-size:40px'>不明原因註冊失敗。</font><br><br><br><font style='font-size:20px'>返回<a href='./registry.jsp'>上一頁</a>重新申請註冊。</font><div>");
        		});
        		$("#bdy").animate({opacity: '1'});
        	}
        }
	});
}
$(function() {
	$("#bdy").animate({
		top : '20%',
		'marginTop': '-150px'
	});
	$("#bdy2").css("height","462px");
	$("#bdy2").animate({
		height : '510px'
	});
	var value='<%=request.getParameter("regid")%>';
	if(value.length>10){
		$.ajax({url : "registry.do", type : "POST", cache : false,
	        data : {
	        	action : "registry_confirm",
	        	regid: value
	        },
	        success: function(result) {
				if("success"==result){
					$("#bdy").animate({opacity: '0'},function() {
						$("#bdy").css("margin-top","-100px");
						$("#bdy").html("<div align='center'><h1>驗證成功</h1><img src='images/laugh.png' width='400px'><br><br>"
							+"<font style='font-size:24px'>恭喜您已完成驗證程序，現在可以盡情使用智慧電商平台</font><br><br><br>"
							+"<a href='./login.jsp' class='btn btn-darkblue'>登入智慧電商系統</a><div>");
						});
					$("#bdy").animate({opacity: '1'});
	        	}else{
	        		$("#bdy").animate({opacity: '0'},function() {
						$("#bdy").css("margin-top","-100px");
						$("#bdy").html("<div align='center'><h1>驗證失敗</h1><br><br><br><font style='font-size:24px'>不明原因驗證失敗，請洽系統管理員</font>"
							+"<br><br><br><br><a href='./login.jsp' class='btn btn-darkblue'>登入智慧電商系統</a><div>");
						});
					$("#bdy").animate({opacity: '1'});
	        	}
	        }
		});
	}
	$('#pwd').keypress(function(e){//密碼不給貼
	    if(e.ctrlKey && e.which == 118){$('#pwd').blur();}
	});
	$('#pwd2').keypress(function(e){//密碼不給貼
	    if(e.ctrlKey && e.which == 118){$('#pwd').blur();}
	});
	$("#uninumber").focus();
	$("#uninumber").blur(function(){
		unicheck();
	});
// 	$("input").keydown(function (event) {
//         if (event.which == 13) {
//         	to_login();
//         }
//     }); 
// 	$("#my_msg").dialog({
// 		title : "註冊失敗",
// 		draggable : false,//防止拖曳
// 		resizable : false,//防止縮放
// 		autoOpen : false,
// 		height : "auto",
// 		modal : true,
// 		open : function(event, ui) {$(this).parent().children().children('.ui-dialog-titlebar-close').hide();},
// 		show : {effect : "bounce",duration : 1200},
// 		hide : {effect : "fade",duration : 300},
// 		buttons : {
// 			"確認" : function() {$(this).dialog("close");},
// 		}
// 	});
});
</script>
</head>

<body class="login-body">
	
<!-- 	<br><a id="logout" href="./login.jsp" class="btn btn-primary" style="float:right;margin-right:20px;">登入</a> -->
	<div class="bkg-upper"></div>
	<div class="bkg-lower"></div>
	<div class="login-wrapper" id="bdy">
		<h1>註冊</h1>
<!-- 		<button onclick="send_mail();">##@_@##</button> -->
		<div class="login-panel-wrap">
		<div class="registry-panel" id="bdy2">
			<form id="regis-form">
				<table class="normal-table">
					<tr><td>
						<span class="block-label">公司統編:</span>
						<input type="text" id="uninumber" name="uninumber">
					</td><td>
						<span class="block-label">公司名稱:</span>
						<input type="text" id="corporation" name="corporation">
					</td></tr>
					<tr><td colspan=2 style="text-align:center">
						<span class="block-label">姓名:</span>
						<input type="text" id="name" name="name" style="width:96%;">
					</td></tr>
					<tr><td colspan=2>
						<span class="block-label">帳號:(電子郵件)</span>
						<input type="text" id="email" name="email" style="width:96%;">
					</td></tr>
					<tr><td colspan=2 style="text-align:center">
						<font color=red>密碼長度須為6~12位，須包含英文及數字，英文有區分大小寫</font>
					</td></tr>
					<tr><td>
						<span class="block-label">密碼:</span>
						<input type="password" id="pwd" name="pwd" >
					</td><td>
						<span class="block-label">密碼確認:</span>
						<input type="password" id="pwd2" name="pwd2" >
					</td></tr>
					<tr><td>
						<span class="block-label">驗證碼:</span>
						<input type="text" id="verify" name="verify">
					</td><td>
					<span class="block-label">點擊可換圖片</span>
						<img title="看不清楚? 點擊圖片可換一張" src="HandleDrawValidateCode.do" id="validateCodeImg" onclick='document.getElementById("validateCodeImg").src="HandleDrawValidateCode.do?t=" + Math.random();' style="height:38px;">
					</td></tr>
					<tr><td colspan=2>
<!-- 						<input type='checkbox' style="position:static;">我同意接受智慧電商平台<a href="#">服務條款</a>與<a href="#">隱私權聲明</a> -->
						 <input type='checkbox' id="agree" style="position:static;">我同意接受智慧電商平台<a href="./servicepolicy.html">服務條款</a>與<a href="./privacy.html" id="privacy">隱私權聲明</a>
					</td></tr>
					<tr><td colspan=2>
						<a class="login-button" id="register" onclick="regis();" style="width:100%;">註冊</a>
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