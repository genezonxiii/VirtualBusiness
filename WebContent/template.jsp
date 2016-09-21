<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
	<title>電商平台</title>
	<link rel="Shortcut Icon" type="image/x-icon" href="./images/Rockettheme-Ecommerce-Shop.ico" />
	<link rel="stylesheet" href="css/1.11.4/jquery-ui.css">
	<link rel="stylesheet" href="vendor/css/jquery-ui.min.css">
	<link rel="stylesheet" href="css/jquery.dataTables.min.css" />
	<link rel="stylesheet" href="vendor/css/font-awesome.min.css">
	<link rel="stylesheet" href="css/styles.css">

	<script type="text/javascript" src="js/jquery-1.10.2.js"></script>
<script type="text/javascript">
function who(){
	var page=location.pathname.split("/")[2];
// 	var page="upload.jsp";
	var side2=$(".sidenav > ul > li:nth-child(2)");
	var side3=$(".sidenav > ul > li:nth-child(3)");
	switch(page){
//####交易處理############################
	case "upload.jsp":
		$(".sidenav > ul > li:nth-child(1)").addClass("active");
		return "訂單拋轉作業";
		break;
	case "upload.do":
		$(".sidenav > ul > li:nth-child(1)").addClass("active");
		return "訂單拋轉作業";
		break;
//####後臺支援系統############################
	case "purchase.jsp":
		side2.addClass("active");
		return "進貨管理";
		break;
	case "purchreturn.jsp":
		side2.addClass("active");
		return "進貨退回管理";
		break;
	case "sale.jsp":
		side2.addClass("active");
		return "銷貨管理";
		break;
	case "salereturn.jsp":
		side2.addClass("active");
		return "銷貨退回管理";
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
//######線上學院##########################
	case "onlinecourse.jsp":
		$(".sidenav > ul > li:nth-child(5)").addClass("active");
		return "線上學院";
		break;
	case "template.jsp":
		window.location.href = './welcome.jsp';
		return "　　";
		break;
	case "welcome.jsp":
		return "首頁";
		break;
		
	default:
		window.location.href = './404.jsp';
		return "";
		break;
	}
};
</script>
</head>
<body>

<div class="page-wrapper" style="" >
	<div class="header" style="z-index:1;">
		<h1>智慧電商平台</h1>
		<div class="userinfo">
			<p>使用者<span><%= (request.getSession().getAttribute("user_name")==null)?"尚未登入?":request.getSession().getAttribute("user_name").toString() %></span></p>
			<a id="logout" class="btn-logout" >登出</a>
		</div>
	</div><!-- /.header -->
	<div class="sidenavpanel"></div>
	<div class="sidenav" style="z-index:2;">
		<ul>
			<li><img src="images/sidenav-transaction.svg" alt="">交易處理
				<ul>
					<li><a href="#" onclick="$('.sidenav > ul > li:hover ul').css('opacity','0');$('.bdyplane').animate({opacity: '0'},300,function() {location.replace('upload.jsp');});">訂單拋轉作業</a></li>
				</ul>
			</li>
			<li><img src="images/sidenav-support.svg" alt="">後臺支援系統
				<ul style="top: -146px;">
					<li><a href="#" onclick="$('.sidenav > ul > li:hover ul').css('opacity','0');$('.bdyplane').animate({opacity: '0'},100,function() {location.replace('purchase.jsp');});">進貨管理</a></li>
					<li><a href="#" onclick="$('.sidenav > ul > li:hover ul').css('opacity','0');$('.bdyplane').animate({opacity: '0'},300,function() {location.replace('purchreturn.jsp');});">進貨退回管理</a></li>
					<li><a href="#" onclick="$('.sidenav > ul > li:hover ul').css('opacity','0');$('.bdyplane').animate({opacity: '0'},300,function() {location.replace('sale.jsp');});">銷貨管理</a></li>
					<li><a href="#" onclick="$('.sidenav > ul > li:hover ul').css('opacity','0');$('.bdyplane').animate({opacity: '0'},300,function() {location.replace('salereturn.jsp');});">銷貨退回管理</a></li>
					<li><a href="#" onclick="$('.sidenav > ul > li:hover ul').css('opacity','0');$('.bdyplane').animate({opacity: '0'},300,function() {location.replace('stock.jsp');});">庫存管理</a></li>
					<li><a href="#" onclick="$('.sidenav > ul > li:hover ul').css('opacity','0');$('.bdyplane').animate({opacity: '0'},300,function() {location.replace('supply.jsp');});">供應商管理</a></li>
					<li><a href="#" onclick="$('.sidenav > ul > li:hover ul').css('opacity','0');$('.bdyplane').animate({opacity: '0'},300,function() {location.replace('product.jsp');});">商品管理</a></li>
					<li><a href="#" onclick="$('.sidenav > ul > li:hover ul').css('opacity','0');$('.bdyplane').animate({opacity: '0'},300,function() {location.replace('tagprint.jsp');});">標籤列印</a></li>
					<li><a href="#" onclick="$('.sidenav > ul > li:hover ul').css('opacity','0');$('.bdyplane').animate({opacity: '0'},300,function() {location.replace('customer.jsp');});">客戶管理</a></li>
					<li><a href="#" onclick="$('.sidenav > ul > li:hover ul').css('opacity','0');$('.bdyplane').animate({opacity: '0'},300,function() {location.replace('producttype.jsp');});">商品類型管理</a></li>
					<li><a href="#" onclick="$('.sidenav > ul > li:hover ul').css('opacity','0');$('.bdyplane').animate({opacity: '0'},300,function() {location.replace('productunit.jsp');});">商品單位管理</a></li>
					<li><a href="#" onclick="$('.sidenav > ul > li:hover ul').css('opacity','0');$('.bdyplane').animate({opacity: '0'},300,function() {location.replace('group.jsp');});">公司管理</a></li>
					<li><a href="#" onclick="$('.sidenav > ul > li:hover ul').css('opacity','0');$('.bdyplane').animate({opacity: '0'},300,function() {location.replace('user.jsp');});">使用者管理</a></li>
					<li><a href="#" onclick="$('.sidenav > ul > li:hover ul').css('opacity','0');$('.bdyplane').animate({opacity: '0'},300,function() {location.replace('accreceive.jsp');});">應收帳款管理</a></li>
					<li><a href="#" onclick="$('.sidenav > ul > li:hover ul').css('opacity','0');$('.bdyplane').animate({opacity: '0'},300,function() {location.replace('accpay.jsp');});">應付帳款管理</a></li>
					<li><a href="#" onclick="$('.sidenav > ul > li:hover ul').css('opacity','0');$('.bdyplane').animate({opacity: '0'},300,function() {location.replace('changepassword.jsp');});">使用者密碼管理</a></li>
<!-- 					<li><a href="#" onclick="$('.sidenav > ul > li:hover ul').css('opacity','0');$('.bdyplane').animate({opacity: '0'},300,function() {location.replace('membercondition.jsp');});">會員分級設定</a></li> -->
				</ul>
			</li>
			<li><img src="images/sidenav-report.svg" alt="">報表管理
				<ul style="top: -100px;">
					<li><a href="#" onclick="$('.sidenav > ul > li:hover ul').css('opacity','0');$('.bdyplane').animate({opacity: '0'},300,function() {location.replace('salereport.jsp');});">訂單報表</a></li>
					<li><a href="#" onclick="$('.sidenav > ul > li:hover ul').css('opacity','0');$('.bdyplane').animate({opacity: '0'},300,function() {location.replace('shipreport.jsp');});">出貨報表</a></li>
<!-- 					<li><a href="#" onclick="$('.sidenav > ul > li:hover ul').css('opacity','0');$('.bdyplane').animate({opacity: '0'},300,function() {location.replace('distributereport.jsp');});">配送報表</a></li> -->
					<li><a href="#" onclick="$('.sidenav > ul > li:hover ul').css('opacity','0');$('.bdyplane').animate({opacity: '0'},300,function() {location.replace('salereturnreport.jsp');});">退貨報表</a></li>
					<li><a href="#" onclick="$('.sidenav > ul > li:hover ul').css('opacity','0');$('.bdyplane').animate({opacity: '0'},300,function() {location.replace('purchreport.jsp');});">進貨報表</a></li>
					<li><a href="#" onclick="$('.sidenav > ul > li:hover ul').css('opacity','0');$('.bdyplane').animate({opacity: '0'},300,function() {location.replace('purchreturnreport.jsp');});">進貨退回報表</a></li>
			    	<li><a href="#" onclick="$('.sidenav > ul > li:hover ul').css('opacity','0');$('.bdyplane').animate({opacity: '0'},300,function() {location.replace('stockreport.jsp');});">庫存報表</a></li>
			    	<li><a href="#" onclick="$('.sidenav > ul > li:hover ul').css('opacity','0');$('.bdyplane').animate({opacity: '0'},300,function() {location.replace('supplyreport.jsp');});">供應商報表</a></li>
			    	<li><a href="#" onclick="$('.sidenav > ul > li:hover ul').css('opacity','0');$('.bdyplane').animate({opacity: '0'},300,function() {location.replace('productreport.jsp');});">商品報表</a></li>
			    	<li><a href="#" onclick="$('.sidenav > ul > li:hover ul').css('opacity','0');$('.bdyplane').animate({opacity: '0'},300,function() {location.replace('customerreport.jsp');});">客戶報表</a></li>
			    	<li><a href="#" onclick="$('.sidenav > ul > li:hover ul').css('opacity','0');$('.bdyplane').animate({opacity: '0'},300,function() {location.replace('accreceivereport.jsp');});">應收帳款報表</a></li>
			    	<li><a href="#" onclick="$('.sidenav > ul > li:hover ul').css('opacity','0');$('.bdyplane').animate({opacity: '0'},300,function() {location.replace('accpayreport.jsp');});">應付帳款報表</a></li>
				</ul>
			</li>
			<li><img src="images/sidenav-chart.svg" alt="">分析圖表
				<ul>
					<li><a href="#" onclick="$('.sidenav > ul > li:hover ul').css('opacity','0');$('.bdyplane').animate({opacity: '0'},300,function() {location.replace('salechart.jsp');});">出貨量統計圖</a></li>
					<li><a href="#" onclick="$('.sidenav > ul > li:hover ul').css('opacity','0');$('.bdyplane').animate({opacity: '0'},300,function() {location.replace('saleamountchart.jsp');});">銷售金額統計圖</a></li>
					<li><a href="#" onclick="$('.sidenav > ul > li:hover ul').css('opacity','0');$('.bdyplane').animate({opacity: '0'},300,function() {location.replace('saleamountstaticchart.jsp');});">銷售金額比例統計圖</a></li>
					<li><a href="#" onclick="$('.sidenav > ul > li:hover ul').css('opacity','0');$('.bdyplane').animate({opacity: '0'},300,function() {location.replace('heavybuyer.jsp');});">訂購人消費排名統計圖</a></li>
					<li><a href="#" onclick="$('.sidenav > ul > li:hover ul').css('opacity','0');$('.bdyplane').animate({opacity: '0'},300,function() {location.replace('bestsale.jsp');});">暢銷商品統計圖</a></li>
				</ul>
			</li>
			<li><img src="images/sidenav-school.svg" alt="">線上學院
				<ul>
					<li><a href="#">Coming Soon</a></li>
					<li class="hide_everywhere"><a href="msgboard.html">留言版</a></li>
					<li class="hide_everywhere"><a href="membercondition.jsp">會員分級設定</a></li>
					<li class="hide_everywhere"><a href="distributereport.jsp">配送報表</a></li>
					<li class="hide_everywhere"><a href="onlinecourse.jsp">線上學院</a></li>
				</ul>
			</li>
		</ul>
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
</div><!-- /.page-wrapper -->
<script src="vendor/js/jquery-1.12.4.min.js"></script>
<script src="vendor/js/jquery-ui.min.js"></script>
<script src="js/jquery.dataTables.min.js"></script>
<script src="js/scripts.js"></script>
<script type="text/javascript">
$(function() {
	$('body').keypress(function(e){//ctrl + alt + b
	    if(e.shiftKey){
	    	if(e.which == 41){
	    		$(".hide_everywhere").removeClass("hide_everywhere");
	    		$(".sidenav > ul > li:nth-child(5) ul").css("top", "-100px");
	    	}
	    }
	});
	$(".sidenav > ul > li:nth-child(2)").find("a").css("padding","7px 20px");
	$(".sidenav > ul > li:nth-child(3)").find("a").css("padding","8px 20px");
	$("#logout").click(function(e) {
		$.ajax({
			type : "POST",
			url : "login.do",
			data : {
				action : "logout"
			},
			success : function(result) {
				//top.location.href = "login.jsp";
				location.replace('login.jsp');
			}
		});
	});
});
</script>
</body>
</html>