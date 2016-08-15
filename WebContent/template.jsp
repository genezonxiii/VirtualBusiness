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
<script>
function table_before(str){
	var i;
	var table_name=str;
	var selector="#"+table_name+" th";
	var leng =  $(selector).length;
	var tmp="";
	for(i=1;i<leng+1;i++){
		selector="#"+table_name+" th:nth-child("+i+")";
		tmp+="<input class='tog_col' checked id='col-"+i+"' type='checkbox' value='"+i+"'onclick='$(\"#"+table_name+"\").DataTable().column("+(i-1)+").visible(!$(\"#"+table_name+"\").DataTable().column("+(i-1)+").visible());'><label class='tog_col' for='col-"+i+"'><span class='form-label'>"+$(selector).text()+"</span></label>"
	}
	selector="#"+table_name;
	$(selector).before(tmp);
}

function draw_table(table_name,title){
	var selector="#"+table_name;
	$(".tog_col").remove();
	$(selector).dataTable().fnDestroy();
	table_before(table_name);
	$(selector).dataTable({
		dom: 'lfrB<t>ip',
		buttons: [{
		    extend: 'excel',
		    text: '輸出為execl報表',
		    title: title,
		    exportOptions: {modifier: { page: 'current'}}
		  }],
		"language": {"url": "js/dataTables_zh-tw.txt"}
	});
}

function who(){
	switch(location.pathname.split("/")[2]){
//####交易處理############################
	case "upload.jsp":
		$(".sidenav > ul > li:nth-child(1)").addClass("active");
		return "訂單拋轉作業";
		break;
//####後臺支援系統############################
	case "purchase.jsp":
		$(".sidenav > ul > li:nth-child(2)").addClass("active");
		return "進貨管理";
		break;
	case "purchreturn.jsp":
		$(".sidenav > ul > li:nth-child(2)").addClass("active");
		return "進貨退回管理";
		break;
	case "sale.jsp":
		$(".sidenav > ul > li:nth-child(2)").addClass("active");
		return "銷貨管理";
		break;
	case "salereturn.jsp":
		$(".sidenav > ul > li:nth-child(2)").addClass("active");
		return "銷貨退回管理";
		break;
	case "stock.jsp":
		$(".sidenav > ul > li:nth-child(2)").addClass("active");
		return "庫存管理";
		break;
	case "producttype.jsp":
		$(".sidenav > ul > li:nth-child(2)").addClass("active");
		return "商品類型管理";
		break;
	case "productunit.jsp":
		$(".sidenav > ul > li:nth-child(2)").addClass("active");
		return "商品單位管理";
		break;
	case "product.jsp":
		$(".sidenav > ul > li:nth-child(2)").addClass("active");
		return "商品管理";
		break;
	case "supply.jsp":
		$(".sidenav > ul > li:nth-child(2)").addClass("active");
		return "供應商管理";
		break;
	case "user.jsp":
		$(".sidenav > ul > li:nth-child(2)").addClass("active");
		return "用戶管理";
		break;
	case "group.jsp":
		$(".sidenav > ul > li:nth-child(2)").addClass("active");
		return "公司管理";
		break;
	case "customer.jsp":
		return "客戶管理";
		break;
	case "accreceive.jsp":
		$(".sidenav > ul > li:nth-child(2)").addClass("active");
		return "應收帳款管理";
		break;
	case "accpay.jsp":
		$(".sidenav > ul > li:nth-child(2)").addClass("active");
		return "應付帳款管理";
		break;
	case "changepassword.jsp":
		$(".sidenav > ul > li:nth-child(2)").addClass("active");
		return "密碼修改";
		break;
	case "tagprint.jsp":
		$(".sidenav > ul > li:nth-child(2)").addClass("active");
		return "標籤列印";
		break;
//####報表管理############################
	case "salereport.jsp":
		$(".sidenav > ul > li:nth-child(3)").addClass("active");
		return "訂單報表";
		break;
	case "distributereport.jsp":
		$(".sidenav > ul > li:nth-child(3)").addClass("active");
		return "配送報表";
		break;
	case "salereturnreport.jsp":
		$(".sidenav > ul > li:nth-child(3)").addClass("active");
		return "退貨報表";
		break;
	case "shipreport.jsp":
		$(".sidenav > ul > li:nth-child(3)").addClass("active");
		return "出貨報表";
		break;
	case "purchreport.jsp":
		$(".sidenav > ul > li:nth-child(3)").addClass("active");
		return "進貨報表";
		break;
	case "purchreturnreport.jsp":
		$(".sidenav > ul > li:nth-child(3)").addClass("active");
		return "進貨退回報表";
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
//######線上學院##########################
	case "onlinecourse.jsp":
		$(".sidenav > ul > li:nth-child(5)").addClass("active");
		return "線上學院";
		break;
	case "template.jsp":
		return "暫時的";
		break;
	case "welcome.jsp":
		return "";
		break;
	default:
		if(location.pathname.split("/")[2].indexOf("upload")!=-1){return "訂單拋轉作業";}
		//alert("default_page; "+location.pathname.split("/")[2]);
		//return "something_wrong?";
		return "";
		break;
	}
};
</script>
</head>
<body>
<script>
$(function() {
	$("#title").append(who());
	$("#logout").click(function(e) {
		$.ajax({
			type : "POST",
			url : "login.do",
			data : {
				action : "logout"
			},
			success : function(result) {
				top.location.href = "login.jsp";
			}
		});
	});
});
</script>
<div class="page-wrapper" >
	<div class="header" style="z-index:1;">
		<h1>智慧電商平台</h1>
		<div class="userinfo">
			<p>使用者<span><%= (request.getSession().getAttribute("user_name")==null)?"@_@?":request.getSession().getAttribute("user_name").toString() %></span></p>
			<a id="logout" class="btn-logout" >登出</a>
		</div>
	</div><!-- /.header -->
	<div id="sidenav-panel" style="position: fixed;top: 56px;width: 120px;height:100%;background: #3F7FAA;"></div>
	<div class="sidenav" style="z-index:2;">
		<ul>
			<li><img src="images/sidenav-transaction.svg" alt="">交易處理
				<ul>
					<li><a href="upload.jsp">訂單拋轉作業</a></li>
				</ul>
			</li>
			<li><img src="images/sidenav-support.svg" alt="">後臺支援系統
				<ul style="top: -156px;">
					<li><a href="purchase.jsp">進貨管理</a></li>
					<li><a href="purchreturn.jsp">進貨退回管理</a></li>
					<li><a href="sale.jsp">銷貨管理</a></li>
					<li><a href="salereturn.jsp">銷貨退回管理</a></li>
					<li><a href="stock.jsp">庫存管理</a></li>
					<li><a href="producttype.jsp">商品類型管理</a></li>
					<li><a href="productunit.jsp">商品單位管理</a></li>
					<li><a href="product.jsp">商品管理</a></li>
					<li><a href="supply.jsp">供應商管理</a></li>
					<li><a href="user.jsp">用戶管理</a></li>
					<li><a href="group.jsp">公司管理</a></li>
					<li><a href="customer.jsp">客戶管理</a></li>
					<li><a href="accreceive.jsp">應收帳款管理</a></li>
					<li><a href="accpay.jsp">應付帳款管理</a></li>
					<li><a href="changepassword.jsp">用戶帳密管理</a></li>
					<li><a href="tagprint.jsp">標籤列印</a></li>
				</ul>
			</li>
			<li><img src="images/sidenav-report.svg" alt="">報表管理
				<ul>
					<li><a href="salereport.jsp">訂單報表</a></li>
					<li><a href="shipreport.jsp">出貨報表</a></li>
					<li><a href="distributereport.jsp">配送報表</a></li>
					<li><a href="salereturnreport.jsp">退貨報表</a></li>
					<li><a href="purchreport.jsp">進貨報表</a></li>
					<li><a href="purchreturnreport.jsp">進貨退回報表</a></li>
				</ul>
			</li>
			<li><img src="images/sidenav-chart.svg" alt="">分析圖表
				<ul>
					<li><a href="salechart.jsp">出貨量統計圖</a></li>
					<li><a href="saleamountchart.jsp">銷售金額統計圖</a></li>
					<li><a href="saleamountstaticchart.jsp">銷售金額比例統計圖</a></li>
				</ul>
			</li>
			<li><img src="images/sidenav-school.svg" alt="">線上學院
				<ul>
					<li><a href="onlinecourse.jsp">線上學院</a></li>
				</ul>
			</li>
		</ul>
	</div><!-- /.sidenav -->
 	<h2 id="title" class="page-title" style="z-index:1">
 		<%= (("welcome.jsp".equals(request.getRequestURI().split("/")[2]))?("歡迎"+request.getSession().getAttribute("user_name")+"使用本系統"):("")) %>
 	</h2> 
<!-- 	<div class="content-wrap" style="display:none"> -->
 <!--################正文開始###############--> 
 <!--################正文結束###############-->
<!-- 	</div>/.content-wrap -->

	<footer class="footer" style="z-index:1;">
		北祥股份有限公司 <span>服務電話：+886-2-2658-1910  |  傳真：+886-2-2658-1920</span>
	</footer><!-- / .footer -->
</div><!-- /.page-wrapper -->
<script src="vendor/js/jquery-1.12.4.min.js"></script>
<script src="vendor/js/jquery-ui.min.js"></script>
<script src="js/jquery.dataTables.min.js"></script>
<script src="js/scripts.js"></script>

</body>
</html>