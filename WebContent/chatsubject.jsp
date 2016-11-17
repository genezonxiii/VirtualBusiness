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
<title>討論區主題列表</title>
<meta charset="utf-8">
<link rel="Shortcut Icon" type="image/x-icon" href="./images/Rockettheme-Ecommerce-Shop.ico" />
<link href="<c:url value="css/jquery.dataTables.min.css" />" rel="stylesheet">
<link href="<c:url value="css/1.11.4/jquery-ui.css" />" rel="stylesheet">
<style>	
table.discuss {
	margin:80px auto;
	font-family: "微軟正黑體", "Microsoft JhengHei", 'LiHei Pro', Arial, Helvetica, sans-serif, \5FAE\8EDF\6B63\9ED1\9AD4,\65B0\7D30\660E\9AD4;
    background: white;
    border: 2px solid #666;
    font-size: 13px;
    width: 95%;
}
table.discuss td {
	text-align: center;
/* 	height:80px; */
    margin: 0;
	padding: 8px;
	border: 1px solid #ccc;
	font-size: 10pt;
/* 	vertical-align:text-top; */
}
table.discuss tr:hover {
	background: #f8f8f8;
}
.title-block {
  float:left;
  margin:30px;
  background: #e8e8e8;
  border: 1px solid #aaa;
  width:40%;
  transition: all 0.3s linear;
}
.title-block:hover {
  -webkit-box-shadow: 0px 0px 5px 1px #1096d2;
  -moz-box-shadow: 0px 0px 5px 1px #1096d2;
  box-shadow: 0px 0px 5px 1px #1096d2;
}
.title-block .hi{
  padding:5px;
  background-color:green;
  color:white;
  position:relative;
  top:-5px;
  left:-5px;
}
.title-block .date {
  float:right;
  color: #777;
  font-size:14px;
  padding:5px;
}
.title-block .detail {
  float:right;
  position:relative;
  top:-10px;
  left:-15px;
  border:1px solid #ccc;
  color: #333;
  font-size:14px;
  padding:5px;
}
.title-block .detail:hover {
  background-color:#ccc;
}
</style>
</head>
<body>
	<jsp:include page="template.jsp" flush="true"/>
	<div class="content-wrap" >
		<div class='bdyplane' style="opacity:0">
<script type="text/javascript" src="js/jquery-1.10.2.js"></script>
<script type="text/javascript" src="js/jquery.dataTables.min.js"></script>
<script type="text/javascript" src="js/jquery-ui.min.js"></script>
<script type="text/javascript" src="js/jquery-migrate-1.4.1.min.js"></script>
<script type="text/javascript" src="js/jquery.validate.min.js"></script>
<script type="text/javascript" src="js/additional-methods.min.js"></script>
<script type="text/javascript" src="js/messages_zh_TW.min.js"></script>
<script>
var user_menu=[];
function gethi(str){
	if(str==null)return "";
	if(str.indexOf("網路營銷")>-1){return "<span class='hi' style='background-color:blue;'>網路營銷</span>";}
	if(str.indexOf("經營")>-1||str.indexOf("服務")>-1){return "<span class='hi' style='background-color:orange;'>經營服務</span>";}
	if(str.indexOf("技巧")>-1){return "<span class='hi' style='background-color:green;'>技巧講座</span>";}
	return "<span class='hi' style='background-color:green;'>額外增開</span>";
}
function draw_chattopic(parameter){
	$.ajax({
		type : "POST",
		url : "chatsubject.do",
		data : parameter,
		success : function(result) {
				var json_obj = $.parseJSON(result);
// 				var result_table = "<tr style='border-bottom:2px solid #888;'><td>編號</td><td>主題</td><td>建立時間</td><td>建立者</td></tr>";
				var result_table = "";
				$.each(json_obj,function(i, item) {
					var opening=((new Date(json_obj[i].start_time)-1000 * 60 * 10 < new Date()) && (new Date(json_obj[i].end_time)  > new Date()-1000 * 60 * 10));
					if(isIE()){opening=(new Date(json_obj[i].start_time.replace(" ","T"))-1000 * 60 * 10 < new Date())&&(new Date(json_obj[i].end_time.replace(" ","T"))  > new Date()-1000 * 60 * 10);}
					result_table+= "<tr style='height:50px;"+(opening?"background-color:pink;":"")+"'>"
					+ "<td>"+json_obj[i].topic+"</td>"
					+ "<td>"+json_obj[i].start_time.replace(":00.0"," - ").replace(" ","<br>　")+("<br>"+json_obj[i].end_time.replace(" ","<br>　")).replace(("<br>"+json_obj[i].start_time.replace(" ","<br>　")).substring(0,18),"").replace(":00.0","")+"</td>"
// 					+ "<td>"+json_obj[i].end_time.replace(":00.0","")+"</td>"
					+ "<td>"+json_obj[i].teacher+"</td>"
					+ "<td>"+json_obj[i].memo+"</td>"
					+ "<td>"+(opening?"<a href='./chat.jsp' class='btn btn-primary'>進入</a>":"")+"</td>"
					+ "</tr>";
// 					alert(new Date(Date.parse(   json_obj[i].start_time.replace(":00.0","")   )));
					//alert((new Date(json_obj[i].start_time)-1000 * 60 * 10 < new Date())+" && "+(new Date(json_obj[i].end_time) > new Date()-1000 * 60 * 10) +" "+((new Date(json_obj[i].end_time)+1000 * 60 * 10)> new Date()));
				});
				var resultRunTime = 0;
				$.each (json_obj, function (i) {
					resultRunTime+=1;
				});
				if(resultRunTime!=0){
					$("#chattopic tbody").html(result_table);
					warning_msg("");
				}else{
					warning_msg("---查無此結果---");
				}
			}
		});
}
	$(function() {
		//alert(("<br>"+"123").replace("1","222").substring(0,6));
		$("body").append('<a href="sip:<benchen@pershing.com.tw>" class="btn-explanation" style="position: fixed; top: 85%; right: 60px; background-color: white; border-radius: 200px; display: block;"><img src="./images/skype-icon.png"/></a>');
		//window.addEventListener('DOMMouseScroll', function(){alert("1");});
		//window.addEventListener('MozMousePixelScroll', function(e){alert(evt.deltaY || evt.wheelDelta || evt.detail);});
		//window.addEventListener('mousewheel', function(){alert("3");});
		//window.attachEvent('onmousewheel', function(){alert("4");});
// 		window.addEventListener('wheel', function(evt){
// 			if(evt.deltaY<0){}
// 		});

		
		
		$(".bdyplane").animate({"opacity":"1"});
		draw_chattopic({"action":"topic_list"});
	});
</script>
		
		<div style="margin:80px auto;width:80%;">
	  		<table id='chattopic' class='result-table'>
	  			<thead>
	  				<tr>
		  				<th style="min-width:120px">主題</th>
		  				<th>討論區開放時間</th>
<!-- 		  				<th>結束時間</th> -->
		  				<th style="min-width:60px">講師</th>
		  				<th width='50%'>備註</th>
		  				<th width='80px'></th>
		  			</tr>
	  			</thead>
	  			<tbody>
	  			</tbody>
	  		</table>
		</div>
	</div>
	</div>
</body>
</html>