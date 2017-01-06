<%@page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@page import="java.util.*" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
    <title>智慧電商平台 主頁</title>
	<meta charset="UTF-8">
<link rel="Shortcut Icon" type="image/x-icon" href="./images/Rockettheme-Ecommerce-Shop.ico" />
<link rel="stylesheet" href="css/styles.css" />
</head>
<body>
	<jsp:include page="template.jsp" flush="true"/>
	<div class="content-wrap" style="background-image: url('./images/welcometo.png');background-size: 100% 100%;overflow-y:auto;">
	<div class='bdyplane' style="opacity:0;"></div>
	<div class='board' style="opacity:0.1;">
		<div style="position:absolute;top:52%;left:27.0%;text-align:center;font-size:65px;color:#00BCD5;">
			<a href='./salereport.jsp?action=today' style='color:#00BCD5;'>
				<div id="sale" style="position:relative;top:-18px;left:78px;">
					0
				</div>
			</a>
		</div>
		<div style="position:absolute;top:52%;left:70.5%;text-align:center;font-size:65px;color:#00BCD5;">
			<a href='./shipreport.jsp?action=today' style='color:#00BCD5;'>
				<div id="ship" style="position:relative;top:-18px;left:24px;">
					0
				</div>
			</a>
		</div>
	</div> 
	</div>
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
	$(".bdyplane").animate({"opacity":"1"});
	$.ajax({
		type : "POST",
		url : "welcome.do",
		data : {action :"searh"},
		success : function(result) {
			var json_obj = $.parseJSON(result);
			//$.each(json_obj,function(i, item) {json_obj.count;});
// 			alert("any one else?");
			//alert(result);
			$("#sale").html(json_obj.sale_data);
			$("#ship").html(json_obj.ship_data);
			if(json_obj.sale_data>9){
				$("#sale").css("left","28%");
				$("#sale").parent().css("left","50px");
			}
			if(json_obj.ship_data>9){
				$("#ship").css("left","71.5%");
				$("#ship").parent().css("left","-4px");
			}
			$(".board").animate({"opacity":"1"});
		}
	});
});
</script>
</body>
</html>
