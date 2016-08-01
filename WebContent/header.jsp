<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>

<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>標頭</title>
<style>
	body{
		background-image: url('images/cloud.png'); 
		background-size: cover;
	}  div {
		font-family:"Microsoft JhengHei";
		font-size: 18px;
		
		width:100%;
		//background-color:#445454;
	} div{
		font-weight: bold;
		font-family:"Yu Gothic", SimHei;
		padding: 5px 5px;
		font-size:60px;
		color: #880000 ; 
		vertical-align:middle
	} span{
	font-size:30px;
	}
</style>
<script type="text/javascript" src="js/jquery-1.10.2.js"></script>
<script>
	function clear_session(){
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
	}
</script>
</head>
<body style="overflow:hidden;">


<%if(request.getSession().getAttribute("user_name")==null){%>
	<script>alert("請重新登入!");top.location.href="login.jsp";</script>
<%}%>
<div style="width:auto;height:59px;">
	<div align="center">
		<img src="./images/title.png" alt="Title" style="height:60px;">
		<span style="float:right;"><span>操作者:<%= (request.getSession().getAttribute("user_name")==null)?"@_@?":request.getSession().getAttribute("user_name").toString() %></span>
		<a href='#' title="logout" target="_top" onclick="clear_session();"><img src="./images/logout.png" alt="logout" height="40" width="40" align="right" ></a>
		</span>
		<!-- <marquee behavior=alternate width="40%">智慧電商平台</marquee> -->
	</div>
</div>
</body>
</html>
