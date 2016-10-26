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
function draw_disscussion(parameter){
	$.ajax({
		type : "POST",
		url : "disscussionsubject.do",
		data : parameter,
		success : function(result) {
// 				alert(result);
// 				return;
				var json_obj = $.parseJSON(result);
// 				var result_table = "<tr style='border-bottom:2px solid #888;'><td>編號</td><td>主題</td><td>建立時間</td><td>建立者</td></tr>";
				var result_table2 = "";
				$.each(json_obj,function(i, item) {
					
					result_table2+=
					"<div class='title-block'>"
					+ gethi(json_obj[i].subject_Name)
					+ "<span class='date'>"+json_obj[i].create_time.replace(":00.0","")+"</span>"
					+ "<h3>　　　<a href='./disscussion.jsp?disscussion_id="+json_obj[i].subject_id+"'>"+json_obj[i].subject_Name+"</a></h3>"
					+ "<span class='detail'><a href='./disscussion.jsp?disscussion_id="+json_obj[i].subject_id+"'>詳細內容</a></span>"
					+ "</div>"
					
// 					  "<tr><td>"+ (i+1)+"</td>"
// 					  + "<td><a href='./disscussion.jsp?disscussion_id="+json_obj[i].subject_id+"'>"+ json_obj[i].subject_Name+"</a></td>"
// 					  + "<td>"+ json_obj[i].create_time.replace(".0","")+"</td>"
// 					  + "<td>"+ (user_menu[json_obj[i].user_id]==null?"":user_menu[json_obj[i].user_id])+"</td>"
// 					  + "</tr>";
// 					$("#topic").html(json_obj[i].subject_Name);
// 					$("#topic").val(json_obj[i].subject_id);
// 					result_table +="<tr id='floor"+(i+1)+"' style='border-top: 2px solid #777;'><td style='text-align:center;'>"
// 					+ (i+1)
// 					+ "樓</td><td>"
// 					+ json_obj[i].create_time.replace(".0","")
// 					+ "</td></tr><tr style='height:120px;'><td style='text-align:center;'>"
// 					+ "<div><img src='images/anonymous.png' style='max-height:100px;'></div>"+(user_menu[json_obj[i].user_id]==null?"":user_menu[json_obj[i].user_id])
// 					+ "</td><td  valign='top'>"
// 					+ json_obj[i].content
// 					+ "</td></tr>";
					
				});
				var resultRunTime = 0;
				$.each (json_obj, function (i) {
					resultRunTime+=1;
				});
				if(resultRunTime!=0){
					
					$("#ty_subject").html(result_table2);
// 					$("#dis_subject").html(result_table);
					warning_msg("");
				}else{
					warning_msg("---查無此結果---");
				}
			}
		});
}
	$(function() {
		$(".bdyplane").animate({"opacity":"1"});
		$.ajax({
			type : "POST",
			url : "disscussion.do",
			async : false,
			data :{action : "user_list"},
			success : function(result) {
				var json_obj = $.parseJSON(result);
				$.each (json_obj, function (i,item) {
					if(json_obj[i].user_id!=null){
						user_menu[json_obj[i].user_id]=json_obj[i].user_name;
					}
				});
				
			}
		});
		draw_disscussion({"action":"user_list"});
	});
</script>
		<div class="datalistWrap">
<!-- 		<div class="title-block"> -->
<!-- 			<span class="hi">解決方案</span> -->
<!-- 			<span class="date">2016-10-17</span> -->
<!-- 			<h3>　　　<a href="#">網路營銷-blog經營</a></h3> -->
<!-- 			<span class="detail"><a href="#">詳細內容</a></span> -->
<!-- 	  	</div> -->
	  	<div id='ty_subject' style="margin:0 auto;"></div>
			<table id="dis_subject" class='discuss' style='display:none'>
				<caption>討論區主題列表<br>　</caption>
				<tr><td>
					<a href="./disscussion.jsp?disscussion_id=4df45a38-9473-11e6-922d-005056af760c">促銷技巧</a>
				</td><td>
					<a href="./disscussion.jsp?disscussion_id=4df45a38-9273-11e6-922d-005056af760c">為什麼顧客服務很重要</a>
				</td><td>
					<a href="./disscussion.jsp?disscussion_id=4df45a38-9273-11e6-922d-005056a2360c">跨境電商策略與經營</a>
				</td></tr><tr><td>
					<a href="./disscussion.jsp?disscussion_id=44f45a38-9273-11e6-922d-005056a2370c">網路營銷-blog經營</a>
				</td><td>
					<a href="./disscussion.jsp?disscussion_id=110a2eed-91f1-11e6-922d-005056af760c">網路營銷-網路開店</a>
				</td><td>
				</td></tr>
			</table>
		</div>
	</div>
	</div>
</body>
</html>