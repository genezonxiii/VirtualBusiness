<%@page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@page import="java.util.*" %>
<%@ page import="tw.com.aber.*"%>

<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ page import="java.util.*"%>
<%@ page import="java.sql.DriverManager"%>
<%@ page import="java.sql.Connection"%>
<%@ page import="java.sql.Statement"%>
<%@ page import="java.sql.ResultSet"%>


<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<link rel="stylesheet" href="css/styles.css" />
    <title>訂單拋轉作業</title>
    <meta content="text/html; charset=UTF8">
</head>
<style>
img{
	width: 30px;
}
.my_table{ 
	box-shadow: 10px 5px 30px black; 
	border-radius: 5px;
	spacing: 10px;
	padding: 15px;
}
</style>
<body>
<script>

function setV(){
	var i=0;
	//alert("跳轉upload.do前");
	while(!document.getElementsByName("vender")[i].checked){i++;}
	document.getElementById("form1").action+="?vender="+document.getElementsByName("vender")[i].value;
	document.getElementById("form1").action+="&usr="+"cdri";
	document.getElementById("msg").style.display="inline";
};
</script>
<%

String str=(String)request.getAttribute("action");
if(str!=null){
	if("success".equals(str)){
		out.println("<script>alert('傳輸成功');window.location.href = './upload.jsp';</script>");
		//response.sendRedirect("./upload.jsp");
	}else{
		out.println("<script>alert('傳輸失敗 for: '+'"+str+"');window.location.href = './upload.jsp';</script>");
		//response.sendRedirect("./upload.jsp");
	}
}
%>
<!--  % session.setAttribute("user_name", "偷渡者"); %-->
<!--% session.setAttribute("group_id", "2cb159a1-472e-11e6-806e-000c29c1d067"); %-->
<!--% session.setAttribute("user_id", "3e82db1c-472f-11e6-806e-000c29c1d067"); %-->
<table class="my_table" style=" width:95%;margin: 20px; line-height: 60px;background: #eee;">
<tr>
	<td colspan=6 style="line-height: 30px;"><div class="panel-title"><h2>上傳</h2></div>
	</td>
</tr>
<tr>
	<td>
		<input type="radio" name="vender" value="asap" checked>
			<img src="./images/store/ASAP.jpg" style="padding-top:15px;"/> &nbsp;<span>ASAP</span>
	</td>
	<td>
		<input type="radio" name="vender" value="gohappy" >
			<img src="./images/store/GoHappy.jpg"/> &nbsp;<span>GoHappy</span>
	</td>
	<td>
		<input type="radio" name="vender" value="ibon" >
			<img src="./images/store/ibon.jpg"/> &nbsp;<span>ibon</span>
	</td>
	<td>
		<input type="radio" name="vender" value="17life" >
			<img src="./images/store/Life.png"/> &nbsp;<span>17Life</span>
	</td>
	<td>
		<input type="radio" name="vender" value="linemart" >
			<img src="./images/store/Line_Mart.png"/> &nbsp;<span>Line Mart</span>
	</td>
	<td>
		<input type="radio" name="vender" value="momo" >
			<img src="./images/store/momo.png"/> &nbsp;<span>momo</span>
	</td>
</tr>
<tr>
	<td>
		<input type="radio" name="vender" value="myfone" >
			<img src="./images/store/myfone.jpg"/> &nbsp;<span>myfone</span>
	</td>
	<td>
		<input type="radio" name="vender" value="payeasy" >
			<img src="./images/store/payeasy.png"/> &nbsp;<span>PayEasy</span>
	</td>
	<td>
		<input type="radio" name="vender" value="pchome" >
			<img src="./images/store/pchome.jpg"/> &nbsp;<span>PCHome</span>
	</td>
	<td>
		<input type="radio" name="vender" value="udn" >
			<img src="./images/store/UDN.png"/> &nbsp;<span>UDN</span>
	</td>
	<td style="20%">
			<input type="radio" name="vender" value="yahoo" >
				<img src="./images/store/yahoo.jpg"/> &nbsp;<span>Yahoo購物中心</span>
		</td>
		<td>
			<input type="radio" name="vender" value="91mai" >
				<img src="./images/store/91.jpg"/> &nbsp;<span>九易</span>
		</td>
	</tr>
	<tr>
		<td>
			<input type="radio" name="vender" value="treemall" >
				<img src="./images/store/treemall.jpg"/> &nbsp;<span>國泰Tree</span>
		</td>
		<td>
			<input type="radio" name="vender" value="gomaji" >
			<img src="./images/store/GOMAJI.png"/> &nbsp;<span>夠麻吉</span>
		</td>
		<td>
			<input type="radio" name="vender" value="books" >
				<img src="./images/store/books.png"/> &nbsp;<span>博客來</span>
		</td>
		<td>
			<input type="radio" name="vender" value="umall" >
				<img src="./images/store/Umall.jpg"/> &nbsp;<span>森森</span>
		</td>
		<td>
			<input type="radio" name="vender" value="yahoomall" >
				<img src="./images/store/supermall.jpg"/> &nbsp;<span>Yahoo超級商城</span>
		</td>
		<td>
			<input type="radio" name="vender" value="amart" >
				<img src="./images/store/AMart.png"/> &nbsp;<span>愛買</span>
		</td>
	</tr>
	<tr>
		<td>
			<input type="radio" name="vender" value="rakuten" >
				<img src="./images/store/rakuten.jpg"/> &nbsp;<span>樂天</span>
		</td>
		<td>
			<input type="radio" name="vender" value="etmall" >
				<img src="./images/store/EHS.png"/> &nbsp;<span>東森購物</span>
		</td><td></td><td></td><td></td><td></td>
	</tr>
</table>
<div style="text-align:center; margin:0px auto;font-size:35px;"></div>
<form action="upload.do" name="form1" id="form1" method="post" enctype="multipart/form-data" style="margin-left:60px;margin-top:50px;">
	<!--<input type="text" id="usr" name="usr" value="cdri" type="hidden"/>-->
	<input type="file" name="file" accept=".csv,.pdf,.xls,xlsx" style="opacity:0.7;position:absolute;margin:2px;"/>
	<input type="text" id="upload_name" size="29" style="z-index:-1" />
	<input type="submit" onclick="setV()" value="檔案上傳" /> 
	<a href='./shipreport.jsp' style="float: right;margin-right:150px;">出貨報表</a>
	<a href='./salereturnreport.jsp' style="float: right;margin-right:50px;">退貨報表</a>
	<a href='./distributereport.jsp' style="float: right;margin-right:50px;">配送報表</a>
	<a href='./salereport.jsp' style="float: right;margin-right:50px;">訂單報表</a>
	<div id="msg" style="color:red;margin:5px;display:none;">請稍候片刻...</div>
</form>
<div style="text-align:center; margin:0px auto;font-size:35px;display:none;"><a href='//www.csie.ntu.edu.tw/~b98902023/ASAP4.xls'>範例檔下載</a></div>
<br>
<h4 >${action}</h4>

 <!--  img src="./images/throw.png" alt="throw" height="90%" width="auto"--> 
</body>
</html>
