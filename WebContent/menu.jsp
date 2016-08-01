<%@page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@page import="java.util.*" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
    <title>菜單</title>
<link rel="stylesheet" href="css/1.11.4/jquery-ui.css"/>
<script type="text/javascript" src="js/jquery-3.0.0.min.js"></script>
<script type="text/javascript" src="js/jquery-ui.min.js"></script>
<script type="text/javascript" src="js/jquery.ui.touch-punch.min.js"></script>

    <style>
    .ui-widget-content{background:#7FDDED};
        body{
        	font-size:14px;
            font-family:"Microsoft JhengHei";
        }ul {
            list-style-type: none;                  
            padding: 0; margin: 0;
        }li{ 
            padding-top:2px;
        }li a {
            display: block;                     
            width: auto;  
            text-decoration:none;
            color:#555;
            padding: 10px 30px 10px 10px;
            border-left-style:solid;                    
            border-left-width:5px;
            border-left-color:#009DD8;
            background-color: #f5f4f2;
        }li a.active {  
            color: #000000;                
            font-weight: bold;
            border-left-style:solid;  
            border-left-width:5px;          
            border-left-color:#000000;
        }li a:hover:not(.active) {
            color: #1ab1ff;            
            border-left-style:solid;   
            border-left-width:5px;
            border-left-color:#1ab1ff;                  
        }
    </style>
    <script>
    $(function() {
    	$( "#accordion" ).accordion({
    		//collapsible: true;
    		active:false,
    		heightStyle: "content",
    		
    	});
    });
    $(document).ready(function(){
    	$("a").click(function() {
    		$("a").removeClass("active");
    		$(this).addClass("active");
    	});
    });
    </script>
</head>
<body style="background-color:#7FDDED;">
<div id="accordion" >
  <h3>交易處理</h3>
  <div style="padding: 0px 0px 0px 10px;">
    <ul>
    	<li><a href="upload.jsp" target="main_page">訂單拋轉作業</a></li>
    </ul>
  </div>
  <h3>後臺支援系統</h3>
  <div style="padding: 0px 0px 0px 10px;">
    <ul>
    	<li><a href="purchase.jsp" target="main_page">進貨管理</a></li>
     	<li><a href="purchreturn.jsp" target="main_page">進貨退回管理</a></li> 
    	<li><a href="sale.jsp" target="main_page">銷貨管理</a></li>
    	<li><a href="salereturn.jsp" target="main_page" >銷貨退回管理</a></li>
    	<li><a href="stock.jsp" target="main_page">庫存管理</a></li>
    	<li><a href="producttype.jsp" target="main_page">商品類型管理</a></li>
    	<li><a href="productunit.jsp" target="main_page">商品單位管理</a></li>
    	<li><a href="product.jsp" target="main_page">商品管理</a></li>
    	<li><a href="supply.jsp" target="main_page">供應商管理</a></li>
    	<li><a href="user.jsp" target="main_page">用戶管理</a></li>
    	<li><a href="group.jsp" target="main_page">公司管理</a></li>
    	<li><a href="customer.jsp" target="main_page">客戶管理</a></li>
    	<li><a href="accreceive.jsp" target="main_page">應收帳款管理</a></li>
    	<li><a href="accpay.jsp" target="main_page">應付帳款管理</a></li>
    	<li><a href="changepassword.jsp" target="main_page">用戶帳密管理</a></li>
     	<li><a href="tagprint.jsp" target="main_page">標籤列印</a></li>
    </ul>
  </div>
  <h3>報表管理</h3>
  <div style="padding: 0px 0px 0px 10px;">
    <ul>
    	<li><a href="salereport.jsp" target="main_page">訂單報表</a></li>
    	<li><a href="distributereport.jsp" target="main_page">配送報表</a></li>
    	<li><a href="salereturnreport.jsp" target="main_page">退貨報表</a></li>
    	<li><a href="shipreport.jsp" target="main_page">出貨報表</a></li>
    	<li><a href="purchreport.jsp" target="main_page">進貨報表</a></li>
    	<li><a href="purchreturnreport.jsp" target="main_page">進貨退回報表</a></li>
<!--     	<li><a href="stockreport.jsp" target="main_page">庫存管理報表</a></li> -->
<!--     	<li><a href="supplyreport.jsp" target="main_page">供應商報表</a></li> -->
<!--     	<li><a href="productreport.jsp" target="main_page">商品報表</a></li> -->
<!--     	<li><a href="customerreport.jsp" target="main_page">客戶報表</a></li> -->
<!--     	<li><a href="404.jsp" target="main_page">應收帳款報表</a></li> -->
<!--     	<li><a href="404.jsp" target="main_page">應負帳款報表</a></li> -->
    </ul>
  </div>
  <h3>分析圖表</h3>
  <div style="padding: 0px 0px 0px 10px;">
    <ul>
    	<li><a href="salechart.jsp" target="main_page">出貨量統計圖</a></li>
    	<li><a href="saleamountchart.jsp" target="main_page">銷售金額統計圖</a></li>
    	<li><a href="saleamountstaticchart.jsp" target="main_page">銷售金額比例統計圖</a></li>
    </ul>
  </div>
  <h3>線上學院</h3>
  <div  style="padding: 0px 0px 0px 20px;">
    <ul>
    	<li><a href="onlinecourse.jsp" target="main_page">線上學院</a></li>
    </ul>
  </div>
</div>

</body>
</html>
