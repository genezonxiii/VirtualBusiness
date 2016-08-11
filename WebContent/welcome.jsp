<%@page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@page import="java.util.*" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
    <title>歡迎畫面</title>
	<meta charset="utf-8">
<link rel="Shortcut Icon" type="image/x-icon" href="./images/Rockettheme-Ecommerce-Shop.ico" />
<link rel="stylesheet" href="css/styles.css" />
</head>
<body>
	<jsp:include page="template.jsp" flush="true"/>
	<div class="content-wrap" style="background-image: url('./images/welcome.png');background-size: cover;">
	<div style="margin:56px 0px 28px 120px;">
<script type="text/javascript" src="js/jquery-1.10.2.js"></script>
<script type="text/javascript" src="js/jquery-1.11.4.js"></script>
<script type="text/javascript" src="js/jquery.dataTables.min.js"></script>
<script type="text/javascript" src="js/jquery-ui.min.js"></script>
<script type="text/javascript" src="js/jquery-migrate-1.4.1.min.js"></script>
<script type="text/javascript" src="js/jquery.validate.min.js"></script>
<script type="text/javascript" src="js/additional-methods.min.js"></script>
<script type="text/javascript" src="js/messages_zh_TW.min.js"></script>
<script type="text/javascript" src="js/jquery.table2excel.js"></script>

<script>
$(function() {
	$.ajax({
		type : "POST",
		url : "welcome.do",
		data : {action :"searh",time1 : "2016/06/01",time2 : "2016/07/26" },
		success : function(result) {
			var json_obj = $.parseJSON(result);
			//$.each(json_obj,function(i, item) {json_obj.count;});
			//alert("any one else?");
			//alert(result);
			$("#sale").html("<h1 style='color: brown;'>本日訂單計<a href='./salereport.jsp?action=today'>"+json_obj.sale_data+"</a>筆。</h1>");
			$("#ship").html("<h1 style='color: brown;'>本日出貨計<a href='./shipreport.jsp?action=today'>"+json_obj.ship_data+"</a>筆。</h1>");
		}
	});
});
</script>

<div style="margin:10px;">
<!--  <img src="./images/welcome.png" alt="welcome" style="width:70%;" > -->
<div style="text-align: center;margin:100px;font-size:35px;">
	<div id="ship"></div>
	<br>
	<div id="sale"></div>
</div>
</div>  
</div> 
</div>  
</body>
</html>
