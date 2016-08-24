<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<link rel="Shortcut Icon" type="image/x-icon" href="./images/Rockettheme-Ecommerce-Shop.ico" />
<link rel="stylesheet" href="css/styles.css" />
<meta http-equiv="Content-Type" content="text/html; charset=BIG5">
<title>線上學院</title>
<style>
.row4mpeg{
	width:400px;
	padding:20px;　
	border:2px solid;
	float:left;
	vertical-align: middle; 
	text-align: center;
}
</style>
</head>
<body>
	<jsp:include page="template.jsp" flush="true"/>
	<div class="content-wrap" >
<script>
$(function(){
	$.ajax({
		type : "POST",
		url : "onlinecourse.do",
		data : {action :"get_video_dir"},
		success : function(result) {
			var json_obj = $.parseJSON(result);
			//alert(json_obj.name.length==0);
			if(null==json_obj.name){$("#movie").html("<h1 align='center'> <font color='red'>影片目錄不存在</font></h1>");return;}
			if(json_obj.name.length==0){$("#movie").html("<h1 align='center'> <font color='red'>無檔案</font></h1>");return;}
			var mov_array="";
			$.each(json_obj.name,function(i, item) {
				if(json_obj.path[i]!="dir"){
					mov_array+="<div class='row4mpeg'><video width='400' height='300' src='"+json_obj.path[i]+"' controls='controls'>Sorry for your browser doesn`t support the video tag.</video><div>"+json_obj.name[i]+"</div></div>";
				}
			});
			$("#movie").html(mov_array);
		}
	});
});
</script>
		<div id="movie"></div>
	</div>
</body>
</html>