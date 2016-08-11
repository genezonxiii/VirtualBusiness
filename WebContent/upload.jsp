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
<link rel="Shortcut Icon" type="image/x-icon" href="./images/Rockettheme-Ecommerce-Shop.ico" />
<link rel="stylesheet" href="css/styles.css" />
    <title>訂單拋轉作業</title>
    <meta content="text/html; charset=UTF8">
</head>
<body>
	<jsp:include page="template.jsp" flush="true"/>
	<div class="content-wrap" style="margin:56px 0px 28px 120px;">
<script>

function setV(){
	//alert("hello".indexOf('he')!=-1);
	//return false;
	if($("#file").val()<1){alert("請選擇檔案");return false;}
	var i=0;
	//alert("跳轉upload.do前");
	while(!document.getElementsByName("vender")[i].checked){i++;}
	document.getElementById("form1").action+="?vender="+document.getElementsByName("vender")[i].value;
	return true;
};
$(function(){
	$("td").css("border","0px solid #aaa");
	$("td img").css("width","30px");	
});
</script>
<%
String str=(String)request.getAttribute("action");
if(str!=null){
	if("success".equals(str)){
		out.println("<script>alert('傳輸成功');window.location.href = './upload.jsp';</script>");
	}else{
		//out.println("<script>alert('傳輸失敗 ');window.location.href = './upload.jsp';</script>");
	}
}
%>
<!--  % session.setAttribute("user_name", "偷渡者"); %-->
<!--% session.setAttribute("group_id", "2cb159a1-472e-11e6-806e-000c29c1d067"); %-->
<!--% session.setAttribute("user_id", "3e82db1c-472f-11e6-806e-000c29c1d067"); %-->
<div class="search-result-wrap">
<table class="result-table" >
	<thead>
		<tr>
			<th colspan=5 style="font-size:30px;">平台選擇</th>
		</tr>
	</thead>
	<tr>
		<td>
			<input id="radio-1" type="radio" name="vender" value="asap" checked>
				<label for="radio-1"><span class="form-label" >
				<img src="./images/store/ASAP.jpg"/>&nbsp;ASAP &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
			</span></label>
		</td>
		<td>
			<input id="radio-2" type="radio" name="vender" value="gohappy" >
				<label for="radio-2"><span class="form-label" >
				<img src="./images/store/GoHappy.jpg"/>&nbsp;GoHappy  &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
			</span></label>
		</td>
		<td>
			<input id="radio-3" type="radio" name="vender" value="ibon" >
				<label for="radio-3"><span class="form-label" >
				<img src="./images/store/ibon.jpg"/>&nbsp;ibon  &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
			</span></label>
		</td>
		<td>
			<input id="radio-4" type="radio" name="vender" value="17life" >
				<label for="radio-4"><span class="form-label" >
				<img src="./images/store/Life.png"/>&nbsp;17Life  &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
			</span></label>
		</td>
		<td>
			<input id="radio-5" type="radio" name="vender" value="linemart" >
				<label for="radio-5"><span class="form-label" >
				<img src="./images/store/Line_Mart.png"/>&nbsp;Line Mart  &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
			</span></label>
		</td>
	</tr>
	<tr>
		<td>
			<input id="radio-6" type="radio" name="vender" value="momo" >
				<label for="radio-6"><span class="form-label" >
				<img src="./images/store/momo.png"/>&nbsp;momo
			</span></label>
		</td>
		<td>
			<input id="radio-7" type="radio" name="vender" value="myfone" >
				<label for="radio-7"><span class="form-label" >
				<img src="./images/store/myfone.jpg"/>&nbsp;myfone
			</span></label>
		</td>
		<td>
			<input id="radio-8" type="radio" name="vender" value="payeasy" >
				<label for="radio-8"><span class="form-label" >
				<img src="./images/store/payeasy.png"/>&nbsp;PayEasy
			</span></label>
		</td>
		<td>
			<input id="radio-9" type="radio" name="vender" value="pchome" >
				<label for="radio-9"><span class="form-label" >
				<img src="./images/store/pchome.jpg"/>&nbsp;PCHome
			</span></label>
		</td>
		<td>
			<input id="radio-10" type="radio" name="vender" value="udn" >
				<label for="radio-10"><span class="form-label" >
				<img src="./images/store/UDN.png"/>&nbsp;UDN
			</span></label>
		</td>
	</tr>
	<tr>
		<td>
			<input id="radio-11" type="radio" name="vender" value="yahoo" >
				<label for="radio-11"><span class="form-label" >
			<img src="./images/store/yahoo.jpg"/>&nbsp;Yahoo購物中心
		</span></label>
		</td>
		<td>
			<input id="radio-12" type="radio" name="vender" value="91mai" >
				<label for="radio-12"><span class="form-label" >
			<img src="./images/store/91.jpg"/>&nbsp;九易
		</span></label>
		</td>
		<td>
			<input id="radio-13" type="radio" name="vender" value="treemall" >
				<label for="radio-13"><span class="form-label" >
			<img src="./images/store/treemall.jpg"/>&nbsp;國泰Tree
		</span></label>
		</td>
		<td>
			<input id="radio-14" type="radio" name="vender" value="gomaji" >
			<label for="radio-14"><span class="form-label" >
			<img src="./images/store/GOMAJI.png"/>&nbsp;夠麻吉
		</span></label>
		</td>
		<td>
			<input id="radio-15" type="radio" name="vender" value="books" >
				<label for="radio-15"><span class="form-label" >
			<img src="./images/store/books.png"/>&nbsp;博客來
		</span></label>
		</td>
	</tr>
	<tr>
		<td>
			<input id="radio-16" type="radio" name="vender" value="umall" >
				<label for="radio-16"><span class="form-label" >
			<img src="./images/store/Umall.jpg"/>&nbsp;森森
		</span></label>
		</td>
		<td>
			<input id="radio-17" type="radio" name="vender" value="yahoomall" >
				<label for="radio-17"><span class="form-label" >
			<img src="./images/store/supermall.jpg"/>&nbsp;Yahoo超級商城
		</span></label>
		</td>
		<td>
			<input id="radio-18" type="radio" name="vender" value="amart" >
				<label for="radio-18"><span class="form-label" >
			<img src="./images/store/AMart.png"/>&nbsp;愛買
		</span></label>
		</td>
		<td>
			<input id="radio-19" type="radio" name="vender" value="rakuten" >
				<label for="radio-19"><span class="form-label" >
			<img src="./images/store/rakuten.jpg"/>&nbsp;樂天
		</span></label>
		</td>
		<td>
			<input id="radio-20" type="radio" name="vender" value="etmall" >
				<label for="radio-20"><span class="form-label" >
			<img src="./images/store/EHS.png"/>&nbsp;東森購物
		</span></label>
		</td>
	</tr>
</table>

<div style="text-align:center; margin:0px auto;font-size:35px;"></div>
<form action="upload.do" id="form1" method="post" enctype="multipart/form-data" style="margin:20px;">
	<input type="file" id="file" name="file" accept=".csv,.pdf,.xls,xlsx" style="opacity:0.7;position:absolute;margin:6px;"/>
	<input type="text" id="upload_name" size="40" style="z-index:-1" />
	<input type="submit" onclick="return setV();" value="檔案上傳" class="btn btn-exec btn-wide" style="color: #fff;margin-left:20px"/>
	<br><br><br>
	<table width=100% >
	<tr><td style="text-align:center">
			<a href='./salereport.jsp' style="font-size:25px;">訂單報表</a>
		</td><td style="text-align:center">
			<a href='./shipreport.jsp' style="font-size:25px;">出貨報表</a>
		</td><td style="text-align:center">
			<a href='./distributereport.jsp' style="font-size:25px;">配送報表</a>
		</td><td style="text-align:center">
			<a href='./salereturnreport.jsp' style="font-size:25px;" >退貨報表</a>
		</td><tr>
	</table>
	<div id="msg" style="color:red;margin:5px;display:none;">請稍候片刻...</div>
</form>
</div>
<%-- <h4>${action}</h4> --%>
</div>
</body>
</html>
