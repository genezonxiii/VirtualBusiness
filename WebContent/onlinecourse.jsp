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
		<div class='bdyplane' style="opacity:0">
<script>
function melvin_movie(location){
	var str="<object classid='CLSID:6BF52A52-394A-11d3-B153-00C04F79FAA6' codebase=http://www.microsoft.com/ntserver/netshow/download/en/nsmp2inf.cab#Version=5,1,51,415  id=msplayer type=application/x-oleobject  standby='Loading Microsoft Media Player components...' name=msplayer><param name='AllowChangeDisplaySize' value='1'><param name='AutoStart' value='1'><param name='AutoSize' value='1'><param name='AnimationAtStart' value='1'><param name='ClickToPlay' value='1'><param name='EnableContextMenu' value='0'><param name='EnablePositionControls' value='1'><param name='EnableFullScreenControls' value='1'><param name='URL' value='"+location+"'><param name='ShowControls' value='1'><param name='ShowAudioControls' value='1'><param name='ShowDisplay' value='0'><param name='ShowGotoBar' value='0'><param name='ShowPositionControls' value='1'><param name='ShowStatusBar' value='1'><param name='ShowTracker' value='1'><embed src='"+location+"' type='video/x-ms-wmv' width='400' height='300' autoStart='1' AutoSize='1' AnimationAtStart='1' ClickToPlay='1' EnableContextMenu='0' EnablePositionControls='1' EnableFullScreenControls='1' ShowControls='1' ShowAudioControls='1' ShowDisplay='0' ShowGotoBar='0' ShowPositionControls='1' ShowStatusBar='1' ShowTracker='1' ></embed></object> ";
	return str;
}

$(function(){
	$(".bdyplane").animate({"opacity":"1"});
	$.ajax({
		type : "POST",
		url : "onlinecourse.do",
		data : {action :"get_video_dir"},
		success : function(result) {
			var json_obj = $.parseJSON(result);
			//alert(json_obj.name.length==0);
			if(null==json_obj.name){$("#movie").html("<h3 align='center'> <font color='red'>影片目錄不存在</font></h3>");return;}
			if(json_obj.name.length==0){$("#movie").html("<h3 align='center'> <font color='red'>無檔案</font></h3>");return;}
			var mov_array="";
			$.each(json_obj.name,function(i, item) {
				if (json_obj.path[i]!="dir"){
					if(!(window.navigator.userAgent.indexOf("MSIE ") > 0 || !!navigator.userAgent.match(/Trident.*rv\:11\./))){
						mov_array+="<div class='row4mpeg'><video width='400' height='300' src='"+json_obj.path[i]+"' controls='controls'>Sorry for your browser doesn`t support the video tag.</video><div>"+json_obj.name[i]+"</div></div>";
					}else{
						mov_array+="<table style='float:left;margin:10px;'><caption align='bottom'>"+json_obj.name[i]+"</caption><tr><td>";
						mov_array+="<div>"+(melvin_movie(json_obj.path[i]))+"<div>"+json_obj.name[i]+"</div></div>";
						mov_array+="</td></tr></table>";
					}
				}
			});
			$("#movie").html(mov_array);
		}
	});
});
</script>
			<div id="movie"></div>
<!-- 			<div class='row4mpeg'><iframe  width='400' height='300' src='./video/experience_share.mp4'></iframe ><div>hello!~</div></div> -->

		</div>
	</div>
</body>
</html>