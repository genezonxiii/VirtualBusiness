<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ page import="tw.com.aber.producttype.controller.*"%>
<%@ page import="java.util.*"%>
<%@ page import="java.sql.DriverManager"%>
<%@ page import="java.sql.Connection"%>
<%@ page import="java.sql.Statement"%>
<%@ page import="java.sql.ResultSet"%>
<jsp:directive.page import="java.sql.SQLException" />
<!DOCTYPE html>
<html>
<head>
<title>撿貨單</title>
<meta charset="utf-8">
<link rel="Shortcut Icon" type="image/x-icon" href="./images/Rockettheme-Ecommerce-Shop.ico" />
<link href="<c:url value="css/jquery.dataTables.min.css" />" rel="stylesheet">
<link href="<c:url value="css/1.11.4/jquery-ui.css" />" rel="stylesheet">
</head>
<body>
	<jsp:include page="template.jsp" flush="true"/>
	<div class="content-wrap" style="overflow-y: hidden;">
		<div class='bdyplane' style="opacity:0">
<script type="text/javascript" src="js/jquery-1.10.2.js"></script>
<script type="text/javascript" src="js/jquery.dataTables.min.js"></script>
<script type="text/javascript" src="js/jquery-ui.min.js"></script>
<script type="text/javascript" src="js/jquery-migrate-1.4.1.min.js"></script>
<script type="text/javascript" src="js/jquery.validate.min.js"></script>
<script type="text/javascript" src="js/additional-methods.min.js"></script>
<script type="text/javascript" src="js/messages_zh_TW.min.js"></script>
<script>

	$(function() {
		$(".bdyplane").animate({"opacity":"1"});
		$("#search").click( function(e) {
			e.preventDefault();
			if($("#pickingday").val().length==0){
				warning_msg("請選擇撿貨日期");
				return;
			}
			var tmp = './report.do?dis_date=' + $("#pickingday").val().replace(/-/g, "");
			$.ajax({
		 		type : "POST",
		 		url : "report.do",
		 		async : false,
		 		data :{
		 			date : $("#pickingday").val().replace(/-/g, ""),
		 		},
		 		success : function(result) {
		 			if(result=="0"){
		 				warning_msg($("#pickingday").val()+" 無訂單");
		 				$("#board").html("");
		 			}else{
		 				warning_msg("輸出撿貨單中請稍候...");
		 				$("#board").html("<embed id='pdf' src='"+tmp+"' style='width:100%;height:calc(100vh - 210px);'>");
		 				
		 				setTimeout(function () { warning_msg(""); }, 1000);
		 			}
		 		}
		 	});
			
// 			$("#pdf").attr("src",tmp);
// 			window.location.href = tmp;
		});
		
		$(".input-field-wrap").append("<div class='div_right_bottom upup'><img src='./images/upup.png'></div>");
		$(".input-field-wrap").after("<div class='div_right_top downdown' style='display:none;'><img src='./images/downdown.png'></div>");
		$(".upup").click(function(){
			$(".input-field-wrap").slideToggle("slow");
			$(".downdown").slideToggle();
// 			$("#pdf").css("height","100vh");
		});
		$(".downdown").click(function(){
			$(".input-field-wrap").slideToggle("slow");
			$(".downdown").slideToggle();
// 			$("#pdf").css("height","calc(100vh - 210px)");
		});
	});
</script>
		<div class="input-field-wrap">
			<div class="form-wrap">
				<form id="amount_date_form" name="trans_list_date_form">
					<div class="form-row">
						<label for="">
							<span class="block-label">撿貨日期</span>
							<input type="text" class="input-date" id="pickingday" >
						</label>
						<button id="search" class="btn btn-darkblue">查詢</button>
					</div>
				</form>
			</div><!-- /.form-wrap -->
		</div><!-- /.input-field-wrap -->
			<div id='board'></div>
		</div>
	</div>
</body>
</html>