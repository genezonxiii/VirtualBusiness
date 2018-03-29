<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%
request.setCharacterEncoding("UTF-8");
String groupId = (String) request.getSession().getAttribute("group_id"); 
String userId = (String) request.getSession().getAttribute("user_id"); 
String userName = (String) request.getSession().getAttribute("user_name"); 
String menu = (String) request.getSession().getAttribute("menu"); 
String privilege = (String) request.getSession().getAttribute("privilege");
%>
<html>
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
	<title>電商平台</title>
	<link rel="Shortcut Icon" type="image/x-icon" href="./images/Rockettheme-Ecommerce-Shop.ico" />
	<link rel="stylesheet" href="css/1.11.4/jquery-ui.css">
	<link rel="stylesheet" href="vendor/css/jquery-ui.min.css">
<!-- 	<link rel="stylesheet" href="vendor/css/jquery-ui.min.css"> -->
	
	<link rel="stylesheet" href="css/jquery.dataTables.min.css" />
	<link rel="stylesheet" href="vendor/css/font-awesome.min.css">
	<link rel="stylesheet" href="css/styles.css">
<style>
.func{ }
.func:hover{
	background: #DDD;
}
.func2:hover{
	background: #aaa;
}

.livehelp{
	position:fixed;
	bottom:0px;
	right:5px;
	z-index:1;
}
.livehelp > div:nth-child(2n+1){
	height:21px;
  	position: relative;
  	margin-right:30px;
  	bottom: 3px;
  	float:right;
}


.livehelp > div:nth-child(2n+1) div:nth-child(1){
	width:200px;height:14px;font-size:12px;
	background-color:rgb(120, 180, 255);
	-webkit-box-shadow: 1px 1px 5px 1px #1096d2;
  	-moz-box-shadow: 1px 1px 5px 1px #1096d2;
  	box-shadow: 1px 1px 5px 1px #1096d2;
  	border-radius:2px;
  	padding:3px 5px;
  	position: relative;
  	top: 0px;
}

.livehelp > div:nth-child(2n+1) div:nth-child(1):hover {
  background-color:rgb(140, 200, 255);
}

.livehelp > div:nth-child(2n+2){
	width:360px;height:300px;float:right;margin-right:30px;font-size:20px;display:none;
	-webkit-box-shadow: 1px 1px 5px 1px #1096d2;
  	-moz-box-shadow: 1px 1px 5px 1px #1096d2;
  	box-shadow: 1px 1px 5px 1px #1096d2;
}

.livehelp > div:nth-child(2n+2) > div:nth-child(1){
	width:360px;height:34px;position:absolute;top:0px;
	background-color:rgb(44, 108, 235);
	border-radius:3px 3px 0 0;
}
.livehelp > div:nth-child(2n+2) > div:nth-child(1):hover{
	background-color:rgb(64, 128, 255);
}
.livehelp > div:nth-child(2n+2) > div:nth-child(2){
	width:340px;height:calc(100% - 93px);background-color:#ccc;position:absolute;top:34px;opacity:0.9;overflow-y: scroll;border-top:1px solid #999;padding:10px;
	font-size:16px;
}
.livehelp > div:nth-child(2n+2) > div:nth-child(2) p{
	line-height:16px;
	margin: 0 0 5px;
}
.livehelp > div:nth-child(2n+2) > div:nth-child(2) p .tag{
	font-size:12px;
	color:#555;
	float:right;
}
.livehelp > div:nth-child(2n+2) > div:nth-child(3){
	width:350px;height:30px;background-color:#ddd;position:absolute;bottom:0px;padding: 4px 5px;border-top:1px solid #999;
	border-radius:0 0 3px 3px;
}
</style>

	<script type="text/javascript" src="js/jquery-1.10.2.js"></script>
	<script type="text/javascript" src="js/virtual_business/menu.js"></script>
<!-- 	<script type="text/javascript" src="http://demonstration.abgne.tw/jquery/jquery.easing.1.3.js"></script> -->
<script type="text/javascript">
function trans(str){
	$('.sidenav > ul > li:hover ul').css('opacity','0');
// 	settimeout();
	if($('.bdyplane').length==0){location.replace(str);}else{
		$('.bdyplane').animate({opacity: '0'},300,function() {
			location.replace(str);
			$('.bdyplane').animate({opacity: '1'},10000);
	// 		$('.sidenav > ul > li:hover ul').animate({opacity: '1'});
		});
	}
	
}
function who(){
	var page=location.pathname.split("/")[2];
// 	var page="upload.jsp";
	var side2=$(".sidenav > ul > li:nth-child(2)");
	var side3=$(".sidenav > ul > li:nth-child(3)");
	switch(page){
//####交易處理############################
	case "upload.jsp":
		$(".sidenav > ul > li:nth-child(1)").addClass("active");
		return "網購拋轉作業";
		break;
	case "groupbuying.jsp":
		$(".sidenav > ul > li:nth-child(1)").addClass("active");
		return "團購轉檔作業";
		break;
	case "sfTransfer.jsp":
		$(".sidenav > ul > li:nth-child(1)").addClass("active");
		return "順豐轉檔作業";
		break;
//####後臺支援系統############################
	case "purchase.jsp":
		side2.addClass("active");
		return "採購管理";
		break;
	case "purchreturn.jsp":
		side2.addClass("active");
		return "採購退回管理";
		break;
	case "sale.jsp":
		side2.addClass("active");
		return "訂單管理";
		break;
	case "salereturn.jsp":
		side2.addClass("active");
		return "訂單退回管理";
		break;
	case "stock.jsp":
		side2.addClass("active");
		return "庫存管理";
		break;
	case "producttype.jsp":
		side2.addClass("active");
		return "商品類型管理";
		break;
	case "productunit.jsp":
		side2.addClass("active");
		return "商品單位管理";
		break;
	case "product.jsp":
		side2.addClass("active");
		return "商品管理";
		break;
	case "productpackage.jsp":
		side2.addClass("active");
		return "商品管理（組合包）";
		break;
	case "supply.jsp":
		side2.addClass("active");
		return "供應商管理";
		break;
	case "user.jsp":
		side2.addClass("active");
		return "使用者管理";
		break;
	case "group.jsp":
		side2.addClass("active");
		return "公司管理";
		break;
	case "groupBackstage.jsp":
		side2.addClass("active");
		return "公司後台管理";
		break;
	case "customer.jsp":
		side2.addClass("active");
		return "客戶管理";
		break;
	case "accreceive.jsp":
		side2.addClass("active");
		return "應收帳款管理";
		break;
	case "accpay.jsp":
		side2.addClass("active");
		return "應付帳款管理";
		break;
	case "changepassword.jsp":
		side2.addClass("active");
		return "使用者密碼管理";
		break;
	case "tagprint.jsp":
		side2.addClass("active");
		return "標籤列印";
		break;
	case "membercondition.jsp":
		side2.addClass("active");
		return "會員分級設定";
		break;
	case "exchange.jsp":
		side2.addClass("active");
		return "匯率轉換管理";
		break;
	case "invoice.jsp":
		side2.addClass("active");
		return "開立發票";
		break;
	case "invoicetrack.jsp":
		side2.addClass("active");
		return "發票資訊管理";
		break;
	case "productContrast.jsp":
		side2.addClass("active");
		return "商品對照資料管理";
		break;
	case "basicDataImport.jsp":
		$(".sidenav > ul > li:nth-child(1)").addClass("active");
		return "基本資料匯入作業";
		break;
//####報表管理############################
	case "salereport.jsp":
		side3.addClass("active");
		return "訂單報表";
		break;
	case "distributereport.jsp":
		side3.addClass("active");
		return "配送報表";
		break;
	case "salereturnreport.jsp":
		side3.addClass("active");
		return "退貨報表";
		break;
	case "shipreport.jsp":
		side3.addClass("active");
		return "出貨報表";
		break;
	case "purchreport.jsp":
		side3.addClass("active");
		return "進貨報表";
		break;
	case "purchreturnreport.jsp":
		side3.addClass("active");
		return "進貨退回報表";
		break;
	case "stockreport.jsp":
		side3.addClass("active");
		return "庫存報表";
		break;
	case "supplyreport.jsp":
		side3.addClass("active");
		return "供應商報表";
		break;
	case "productreport.jsp":
		side3.addClass("active");
		return "商品報表";
		break;
	case "customerreport.jsp":
		side3.addClass("active");
		return "客戶報表";
		break;
	case "accreceivereport.jsp":
		side3.addClass("active");
		return "應收帳款報表";
		break;
	case "accpayreport.jsp":
		side3.addClass("active");
		return "應付帳款報表";
		break;
	case "pickingreport.jsp":
		side3.addClass("active");
		return "揀貨單";
		break;
	case "deliveryorder.jsp":
		side3.addClass("active");
		return "出貨單";
		break;
	
//######分析圖表##########################
	case "salechart.jsp":
		$(".sidenav > ul > li:nth-child(4)").addClass("active");
		return "出貨量統計圖";
		break;
	case "saleamountchart.jsp":
		$(".sidenav > ul > li:nth-child(4)").addClass("active");
		return "銷售金額統計圖";
		break;
	case "saleamountstaticchart.jsp":
		$(".sidenav > ul > li:nth-child(4)").addClass("active");
		return "銷售金額比例統計圖";
		break;
	case "bestsale.jsp":
		$(".sidenav > ul > li:nth-child(4)").addClass("active");
		return "暢銷商品統計圖";
		break;
	case "heavybuyer.jsp":
		$(".sidenav > ul > li:nth-child(4)").addClass("active");
		return "訂購人消費排名統計圖";
		break;	
	case "saledifftype.jsp":
		$(".sidenav > ul > li:nth-child(4)").addClass("active");
		return "各類別銷售數量統計圖 ";
		break;	
	case "saledifftypestatic.jsp":
		$(".sidenav > ul > li:nth-child(4)").addClass("active");
		return "各類別銷售比例統計圖";
		break;	
// 	case "saledifftypetotal.jsp":
// 		$(".sidenav > ul > li:nth-child(4)").addClass("active");
// 		return "暢銷商品類別統計圖";
// 		break;	
//######線上學院##########################
	case "onlinecourse.jsp":
		$(".sidenav > ul > li:nth-child(5)").addClass("active");
		return "線上學院";
		break;
	case "disscussion.jsp":
		$(".sidenav > ul > li:nth-child(5)").addClass("active");
		return "留言版";
		break;
	case "disscussionsubject.jsp":
		$(".sidenav > ul > li:nth-child(5)").addClass("active");
		return "留言版主題列表";
		break;
	case "chat.jsp":
		$(".sidenav > ul > li:nth-child(5)").addClass("active");
		return "討論區";
		break;
	case "livehelp.jsp":
		$(".sidenav > ul > li:nth-child(5)").addClass("active");
		return "LiveHelp";
		break;
	case "chatsubject.jsp":
		$(".sidenav > ul > li:nth-child(5)").addClass("active");
		return "討論區主題列表";
		break;
	case "function.jsp":
		$(".sidenav > ul > li:nth-child(5)").addClass("active");
		return "真●後臺系統";
		break;
	case "youtubeSchedule.jsp":
		return "直播排程設定作業";
		break;
	case "template.jsp":
		window.location.href = './welcome.jsp';
		return "　　";
		break;
	case "group_tmp.jsp":
		return "　　";
		break;
	case "welcome.jsp":
		return "首頁";
		break;
	case "groupUserList.jsp":
		return "";
		break;		
	default:
		window.location.href = './404.html';
		return "";
		break;
	}
};
</script>
</head>
<body>
<input type="hidden" id="glb_menu" value='<%= menu %>' />
<input type="hidden" id="glb_privilege" value="<%= privilege %>" />

<div class="page-wrapper" style="" >
	<div class="header" style="z-index:1;">
		<h1 ondblclick="location.href = './welcome.jsp';">智慧電商平台</h1>
		<div class="userinfo">
			<p>使用者<span><%= (request.getSession().getAttribute("user_name")==null)?"尚未登入?":request.getSession().getAttribute("user_name").toString() %></span></p>
			<a href='#' id="logout" class="btn-logout" >登出</a>
		</div>
	</div><!-- /.header -->
	<div class="sidenavpanel"></div>
	<div class="sidenav" style="z-index:2;">
	</div><!-- /.sidenav -->
 	<h2 id="title" class="page-title" style="z-index:1">
 	<script>document.write(who())</script>
<%--  		<%= (("welcome.jsp".equals(request.getRequestURI().split("/")[2]))?("歡迎"+request.getSession().getAttribute("user_name")+"使用本系統"):("")) %> --%>
 	</h2> 
<!-- 	<div class="content-wrap"> -->
 <!--################正文開始###############-->
 <!--################正文結束###############-->
<!-- 	</div>   /.content-wrap -->

	<footer class="footer" style="z-index:1;">
		　　　　　　　　　　　北祥股份有限公司 <span>服務電話：+886-2-2658-1910  |  傳真：+886-2-2658-1920</span>
	</footer><!-- / .footer -->
	<div class='livehelp'>
	
		<div id="livehelp_m" ondblclick="$('#livehelp_m').hide();$('#livehelp').fadeIn();">
			<div>
				<font class="online_or_not" style="color:#aaa;" size='1'> ● </font>線上客服
				<a><img src='./images/putback.png' class='func' style='margin:0px;float:right;' onclick="$('#livehelp_m').hide();$('#livehelp').fadeIn();"></a>
			</div>
		</div>
		<div id="livehelp">
			<div ondblclick="$('#livehelp').hide();$('#livehelp_m').fadeIn();">
				<div style="padding:6px 5px"><font class="online_or_not" style="color:#4f4;" size="3"> ● </font>線上客服</div>
				<div style="position:absolute;top:0px;right:0px;">
					<a><img src="./images/minimize.png" class="func" style="margin:7px 4px 0px 0px;padding:3px;" onclick="$('#livehelp').hide();$('#livehelp_m').fadeIn();"></a>
<!-- 					<a><img src="./images/close.png" class="func" style="margin:7px 4px 0px 0px;padding:3px;" onclick="$('.livehelp').fadeOut(function(){$('.livehelp').remove();});"></a> -->
				</div>
			</div>
			<div id="consoleL">
			</div>
			<div>
				<input id="chatL" type="text" style="width:300px;height:19px;margin:0;"/>
				<div id="send_msgL" style="position:absolute;top:0px;right:4px;margin:8px 4px;padding:0px 3px 4px 4px;" class="func2">→</div>
			</div>
		</div>
<!-- 	repeat..... -->
	</div>
</div><!-- /.page-wrapper -->
<script src="vendor/js/jquery-1.12.4.min.js"></script>
<script src="vendor/js/jquery-ui.min.js"></script>
<script src="js/jquery.dataTables.min.js"></script>
<script src="js/scripts.js"></script>
<script src="js/virtual_business/common.js"></script>

<script type="text/javascript">

$(function() {

	function checksession () {
		setTimeout(function () {
			$.ajax({
				type : "POST",
				url : "welcome.do",
				data : { action : "current_login" },
				success : function(result) {
					if(result=="false"){ window.location.href = './login.jsp'; }
				}
			});
			checksession();
		}, 1*1000);
	}
	checksession(); 
	$('body').keypress(function(e){//ctrl + alt + b
	    if(e.shiftKey && (location.href.indexOf("bers1.eastasia")>-1 ||(location.href.indexOf("112.164")>-1||location.href.indexOf("local")>-1))){
	    	if(e.which == 41){
	    		$(".hide_everywhere").removeClass("hide_everywhere");
	    		$(".sidenav > ul > li:nth-child(5) ul").css("top", "-100px");
	    	}
	    }
	});
	$(".sidenav > ul > li:nth-child(3)").find("a").css("padding","8px 20px");
	$("#logout").click(function(e) {
		$.ajax({
			type : "POST",
			url : "login.do",
			data : {
				action : "logout"
			},
			success : function(result) {
				location.replace('login.jsp');
			}
		});
	});
	
	var me='<%=request.getSession().getAttribute("user_name")%>';
// 	if(me=='DemoUser'){
// 	}else{
// 	}
	if(me=='DemoUser'){
		//LiveHelper  alert('1');
		$(".livehelp").html(
			'<div id="hello">'+
				'<div style="text-align:center;">'+
					'<font size="2"  style="color:#f44;"><b> ● 請友善對待客戶 ● <span id="total"></span></b></font>'+
					//'<a><img src='./images/putback.png' class='func' style='margin:0px;float:right;' onclick="$('#livehelp_m').hide();$('#livehelp').fadeIn();"></a>'+
				'</div>'+
			'</div>'
		);
		var Livehelp = {};
		Livehelp.socket = null;
		Livehelp.connect = (function(host) {
            if ('WebSocket' in window) {
            	Livehelp.socket = new WebSocket(host);
            } else if ('MozWebSocket' in window) {
            	Livehelp.socket = new MozWebSocket(host);
            } else {
                ConsoleL.log('Error: WebSocket is not supported by this browser.');
                return;
            }
            Livehelp.socket.onopen = function () {
                $(".livehelp").delegate(".send", "click", function(event) {
                	var name = $(this).parent().attr("name");
                	event.preventDefault();
                   	//Livehelp.sendMessage();
                   	
                   	Livehelp.socket.send(name+":"+name+"@﹀︿﹀"+$("#chatL_"+name).val());
                   	$("#chatL_"+name).val('');
                });
                $(".livehelp").delegate(".chatL", "keydown", function(event) {
                	var name = $(this).parent().attr("name");
                	if (event.keyCode == 13) {
                    	event.preventDefault();
                    	Livehelp.socket.send(name+":"+name+"@﹀︿﹀"+$("#chatL_"+name).val());
                       	$("#chatL_"+name).val('');
                    }
                });
            };

            Livehelp.socket.onclose = function () {
                //document.getElementById('chatL').onkeydown = null;
                ConsoleL.log('~<font style="color:#f44;">連線異常，中斷連線</font>~');
            };

            Livehelp.socket.onmessage = function (message) {
            	if((message.data).indexOf("System(>_<):Total:")>-1){
            		$("#total").html("("+((message.data).split(":Total:")[1]-1)+")");
            	}else if((message.data).indexOf("System(>_<):Leave:")>-1){
                	var name= (message.data).split(":Leave:")[1];
                	$('#consoleL_'+name).append("<p style='word-wrap: break-word;color:red;'>"+name+"關閉了視窗。</p>");
                	
                	$('#livehelp_m_'+name+' .online_or_not').css("color","#aaa");
                	$('#livehelp_'+name+' .online_or_not').css("color","#aaa");
                	$('#chatL_'+name).prop("disabled",true);
//                 	if((message.data).split(":Leave:")[1]=="online"){
//                 		if($("#chatL").prop("disabled")){
//                 			ConsoleL.log("<font color=#080 style='font-size:12px;line-height: 10px;'>~客服人員在線~</font>");
//                 		}
//                 		$(".online_or_not").attr("color","#4f4");
//                 		$("#chatL").prop("disabled",false);
//                 	}else{
//                 		if(!$("#chatL").prop("disabled")){
// 	                		ConsoleL.log("<font color=red style='font-size:12px;line-height: 10px;'>~客服人員離線~</font>");
// 	                		ConsoleL.log("<font color=red style='font-size:12px;line-height: 10px;'>請於上班時間使用。</font>");
//                 		}
//                 		$(".online_or_not").attr("color","#aaa");
//                 		$("#chatL").prop("disabled",true);
//                 	}
                }else{
                	//alert(message.data);
                	if(message.data.split("name='name'>").length<2) return ;
                	var name = message.data.split("name='name'>")[1].split("</font>")[0];
                	if(message.data.indexOf("@﹀︿﹀") < 0 && $('#livehelp_m_'+name).length < 1 ){
                		$('#hello').remove();
                		$(".content-wrap").append('<audio src="./audio/message.mp3" autoplay="autoplay" style="display:none;"></audio>');
	                	$(".livehelp").append(
	               			'<div id="livehelp_m_'+name+'" style="display:none;" >'+
		               			'<div ondblclick="$(\'#livehelp_m_'+name+'\').hide();$(\'#livehelp_'+name+'\').show();">'+
		            				'<font class="online_or_not" style="color:#4f4;" size="1"> ● </font>'+name+"<font size='2' style='color:#f44;'><b> ● 請友善對待客戶 ● </b></font>"+
		            				'<a><img src="./images/putback.png" class="func" style="margin:0px;float:right;" onclick="$(\'#livehelp_m_'+name+'\').hide();$(\'#livehelp_'+name+'\').show();"></a>'+
		            			'</div>'+
	                		'</div>'+
	                		'<div id="livehelp_'+name+'" style="display:none;">'+
	                			'<div ondblclick="$(\'#livehelp_'+name+'\').hide();$(\'#livehelp_m_'+name+'\').show();">'+
	            					'<div style="padding:6px 5px"><font class="online_or_not" style="color:#4f4;" size="3"> ● </font>'+name+"<font size='4'  style='color:#fb3;'><b> ( 請友善對待客戶 ) </b></font>"+'</div>'+
		            				'<div style="position:absolute;top:0px;right:0px;">'+
		            					'<a><img src="./images/minimize.png" class="func" style="margin:7px 4px 0px 0px;padding:3px;" onclick="$(\'#livehelp_'+name+'\').hide();$(\'#livehelp_m_'+name+'\').show();"></a>'+
// 		            					'<a><img src="./images/close.png" class="func" style="margin:7px 4px 0px 0px;padding:3px;" onclick="$(\'#livehelp_'+name+'\').fadeOut(function(){$(\'#livehelp_'+name+'\').remove();$(\'#livehelp_m_'+name+'\').remove();});"></a>'+
	            					'</div>'+
	            				'</div>'+
	            			'<div id="consoleL_'+name+'"></div>'+
	            			'<div name='+name+'>'+
	            				'<input id="chatL_'+name+'" class="chatL" type="text" style="width:300px;height:19px;margin:0;"/>'+
	            				'<div id="send_msgL_'+name+'" class="send" style="position:absolute;top:0px;right:4px;margin:8px 4px;padding:0px 3px 4px 4px;" class="func2">→</div>'+
	            			'</div>'+
	            		'</div>');
	                	$("#livehelp_"+name).fadeIn(function(){alert('有人需要您的幫助呦~');});
	                	$('#consoleL_'+name).append('<p style="word-wrap: break-word;">'+message.data+'</p>');
                	}else{
                		var msg =  message.data ;
                		if(message.data.indexOf("@﹀︿﹀")>-1){
                			name = message.data.split("</font></b> : ")[1].split("@﹀︿﹀")[0];
                			msg = message.data.replace(name+"@﹀︿﹀","");
                			//alert(message.data+"\nXXXXXX"+name+"\n#####"+msg);
                		}
                		if($('#chatL_'+name).prop("disabled")){$('#consoleL_'+name).append("<p style='word-wrap: break-word;color:red;'>"+name+"又上線了喔。</p>");}
                    	$('#livehelp_m_'+name+' .online_or_not').css("color","#4f4");
                    	$('#livehelp_'+name+' .online_or_not').css("color","#4f4");
                    	$('#chatL_'+name).prop("disabled",false);
                		$('#consoleL_'+name).append('<p style="word-wrap: break-word;">'+msg+'</p>');
                	}
                }
                //所有東西都從這邊經過 如果要加特殊處理人數 名單 都在這邊做
            };
        });

		Livehelp.initialize = function() {
            if (window.location.protocol == 'http:') {
            	if(window.location.host.indexOf("112.164")>-1){
                	//164
                	console.log("ws_ver_livehelp_164");
                	Livehelp.connect('ws://'+get_sensitive("164")+'/VirtualBusiness/websocket/livehelp/'+'<%=request.getSession().getAttribute("user_name")%>');
                }else if(window.location.host.indexOf("ber.com.tw")>-1||window.location.host.indexOf("ers1.eastasia")>-1){
                	//aber
                	console.log("ws_ver_livehelp_aber");
                	Livehelp.connect('ws://'+get_sensitive("aber")+'/VirtualBusiness/websocket/livehelp/'+'<%=request.getSession().getAttribute("user_name")%>');
                }else{
                	//localhost
                	console.log("ws_ver_livehelp_local");
<%--                 	Livehelp.connect('ws://'+get_sensitive("164")+'/VirtualBusiness/websocket/livehelp/'+'<%=request.getSession().getAttribute("user_name")%>'); --%>
                	Livehelp.connect('ws://' + window.location.host + '/VirtualBusiness/websocket/livehelp/'+'<%=request.getSession().getAttribute("user_name")%>');
                }
            } else {
            	Livehelp.connect('wss://' + window.location.host + '/VirtualBusiness/websocket/livehelp/'+'<%=request.getSession().getAttribute("user_name")%>');
            }
        };

        Livehelp.sendMessage = (function() {
        	//alert($(this).html());
            var message = document.getElementById('chatL').value;
            if (message != '') {
            	Livehelp.socket.send(message);
                document.getElementById('chatL').value = '';
            }
        });

        var ConsoleL = {};

        ConsoleL.log = (function(message) {
            var console = document.getElementById('consoleL');
            var p = document.createElement('p');
            p.style.wordWrap = 'break-word';
            p.innerHTML = message;
            console.appendChild(p);
            while (console.childNodes.length > 40) {
                console.removeChild(console.firstChild);
            }
            console.scrollTop = console.scrollHeight;
        });
        
        Livehelp.initialize();
      //####################################################
      //#################先把Server端和Client端分開寫############
      //####################################################
        
	}else{
		//asker  alert('2');
		var Livehelp = {};
		Livehelp.socket = null;
		Livehelp.connect = (function(host) {
            if ('WebSocket' in window) {
            	Livehelp.socket = new WebSocket(host);
            } else if ('MozWebSocket' in window) {
            	Livehelp.socket = new MozWebSocket(host);
            } else {
                ConsoleL.log('Error: WebSocket is not supported by this browser.');
                return;
            }
            Livehelp.socket.onopen = function () {
//                 ConsoleL.log('~<font color=gray>您進入了聊天室</font>~');
                document.getElementById('send_msgL').onclick =function(event) {
                   	event.preventDefault();
                   	Livehelp.sendMessage();
                };
                document.getElementById('chatL').onkeydown = function(event) {
                    if (event.keyCode == 13) {
                    	event.preventDefault();
                    	Livehelp.sendMessage();
                    }
                };
            };

            Livehelp.socket.onclose = function () {
                document.getElementById('chatL').onkeydown = null;
                ConsoleL.log('~<font style="color:#F33;font-size:18px;"><b>連線不穩定</b></font>~');
                $("#chatL").prop("disabled",true);
            };

            Livehelp.socket.onmessage = function (message) {
                if((message.data).indexOf("System(>_<):")>-1){
                	if((message.data).split(":")[1]=="online"){
                		if($("#chatL").prop("disabled")){
                			ConsoleL.log("<font style='color:#080;' style='font-size:12px;line-height: 10px;'>~客服人員在線~</font>");
                		}
                		$(".online_or_not").css("color","#4f4");
                		$("#chatL").prop("disabled",false);
                	}else if((message.data).split(":")[1]=="offline"){
                		if(!$("#chatL").prop("disabled")){
	                		ConsoleL.log("<font style='color:#f44;' style='font-size:12px;line-height: 10px;'>~客服人員離線~</font>");
	                		ConsoleL.log("<font style='color:#f44;' style='font-size:12px;line-height: 10px;'>請於上班時間使用。</font>");
                		}
                		$(".online_or_not").css("color","#aaa");
                		$("#chatL").prop("disabled",true);
                	}
                }else{
                	ConsoleL.log(message.data.replace('<%=request.getSession().getAttribute("user_name")%>'+"@﹀︿﹀",""));
                }
                //所有東西都從這邊經過 如果要加特殊處理人數 名單 都在這邊做
            };
        });

		Livehelp.initialize = function() {
            if (window.location.protocol == 'http:') {
            	if(window.location.host.indexOf("112.164")>-1){
                	//164
                	console.log("ws_ver_livehelp_164");
                	Livehelp.connect('ws://'+get_sensitive("164")+'/VirtualBusiness/websocket/livehelp/'+'<%=request.getSession().getAttribute("user_name")%>');
                }else if(window.location.host.indexOf("ber.com.tw")>-1||window.location.host.indexOf("ers1.eastasia.cloudapp.azure.com")>-1){
                	//abe
                	console.log("ws_ver_livehelp_aber");
                	Livehelp.connect('ws://'+get_sensitive("aber")+'/VirtualBusiness/websocket/livehelp/'+'<%=request.getSession().getAttribute("user_name")%>');
                }else{
                	//localhost
                	console.log("ws_ver_livehelp_local");
<%--                 	Livehelp.connect('ws://'+get_sensitive("164")+'/VirtualBusiness/websocket/livehelp/'+'<%=request.getSession().getAttribute("user_name")%>'); --%>
                	Livehelp.connect('ws://' + window.location.host + '/VirtualBusiness/websocket/livehelp/'+'<%=request.getSession().getAttribute("user_name")%>');
                }
            } else {
            	Livehelp.connect('wss://' + window.location.host + '/VirtualBusiness/websocket/livehelp/'+'<%=request.getSession().getAttribute("user_name")%>');
            }
        };

        Livehelp.sendMessage = (function() {
        	
            var message = document.getElementById('chatL').value;
            if (message != '') {
            	Livehelp.socket.send(message);
                document.getElementById('chatL').value = '';
            }
        });

        var ConsoleL = {};

        ConsoleL.log = (function(message) {
            var console = document.getElementById('consoleL');
            var p = document.createElement('p');
            p.style.wordWrap = 'break-word';
            p.innerHTML = message;
            console.appendChild(p);
            while (console.childNodes.length > 40) {
                console.removeChild(console.firstChild);
            }
            console.scrollTop = console.scrollHeight;
        });
        
        Livehelp.initialize();
	}
	
});
</script>
</body>
</html>