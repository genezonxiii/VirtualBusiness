<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ page import="tw.com.aber.productunit.controller.*"%>
<%@ page import="java.util.*"%>
<%@ page import="java.sql.DriverManager"%>
<%@ page import="java.sql.Connection"%>
<%@ page import="java.sql.Statement"%>
<%@ page import="java.sql.ResultSet"%>
<jsp:directive.page import="java.sql.SQLException" />
<!DOCTYPE html>
<html>
<head>
<title>各類別商品銷售數量統計圖</title>
<meta charset="utf-8">
<link rel="Shortcut Icon" type="image/x-icon" href="./images/Rockettheme-Ecommerce-Shop.ico" />
<link rel="stylesheet" href="css/styles.css" />
<link type="text/css" rel="stylesheet" href="css/1.11.4/jquery-ui.css"/>
<link type="text/css" rel="stylesheet" href="css/visualize.jQuery.css"/>
<style>
.visualize { margin:0 auto; }
canvas { background-color:#E0E8F0; } 
</style>
</head>
<body>
	<jsp:include page="template.jsp" flush="true"/>
	<div class="content-wrap">
		<div class='bdyplane' style="opacity:0">
<script type="text/javascript" src="js/jquery-1.11.4.js"></script>
<script type="text/javascript" src="js/jquery-ui.min.js"></script>
<script type="text/javascript" src="js/d3.v3.min.js"></script>

<script type="text/javascript" src="js/visualize.jQuery.js"></script>
<script>
function draw_piechart(data,list){
// 	for(i=0;i<data.length;i++){
// 		$.each(data[i],function(k, item) {
// 			alert(i+"&&"+k+"&&"+item);
// 		});
// 	}

	var i,j;
	$('.visualize').remove();
	for(m=0;m<data.length;m++){
		var charttable="<table id='pie' style='display:none'><caption>銷售金額比例統計圖 </caption><thead><tr><td></td>";
		$.each(list,function(i, item) {
			if(data[m][list[i]]!=null){
				charttable+="<th>"+item+"</th>";
			}
		});
		charttable += "</tr></thead><tbody>";
		//i=0;
	// 	for(i=0;i<data.length;i++){
			charttable+="<tr><th>"+data[m]["month"]+"</th>";
			for(j=0;j<list.length;j++){
				if(data[m][list[j]]!=null){//裡面是多餘的判斷
					charttable+="<td>"+(data[m][list[j]]==null?"":data[m][list[j]])+"</td>";
				}
			}
			charttable+="</tr>";
	// 	}
		charttable+="</tbody></table>";
		
		$('#chart').html(charttable);
		$('#chart').visualize({
			'type':'pie',
			'pieMargin':'85','pieLabelPos':'outside',
			'width':'600','height':'400',
			'appendTitle':'true','title':data[0]["layer"]+'平台　'+((data[0]["ten"])?'前十大暢銷':'各')+'類別銷售比例統計圖',
			'appendKey':'true',
			'colors':['#e9e744','#666699','#92d5ea','#ee8310','#8d10ee','#5a3b16','#26a4ed','#f45a90','#be1e2d','#FFFF2d'],
			'textColors':'','parseDirection':'y'
		});
		$(".visualize").css("opacity","0");
	}
	
}
// function draw_barchart(data,title){
// 	var i,j,maxdata=0;
// 	for(i=0;i<data.length;i++){
// 		if(data[i].quantity>maxdata){maxdata=data[i].quantity;}
// 		//alert(i+"th: "+data[i].name+" && "+data[i].quantity);
// 	}
// 	scale= 480 / ( maxdata * 21 / 20 );
// 	var leftpad=160,toppad=80;
//     var s = d3.select('#board').append('svg')
//               .attr({
//                 'width':800,
//                 'height':data.length*25+100
//               }).style({
//                 'border':'1px solid #000',
//                 'background-color' : '#E0E8F0'
//               });
//     var rect = s.append('g')
//                 .attr({
//                   'id':'rect'
//                 });
//     var num = s.append('g')
//                 .attr({
//                   'id':'num'
//                 });
//     var date = s.append('g')
//                 .attr({
//                   'id':'date'
//                 });
// //長方形
//     rect.selectAll('rect')
//       .data(data)
//       .enter()
//       .append('rect')
//       .attr({
//         'width':function(d){
//           return d.quantity * scale;
//         },
//         'height':10,
//         'fill':function(d){
//           if(d.quantity>(maxdata * 4 / 5)){
//             return '#c00';
//           }else if(d.quantity>(maxdata * 3 / 5)&& d.quantity <=(maxdata * 4 / 5)){
//             return '#f90';
//           }else if(d.quantity>(maxdata * 2 / 5)&& d.quantity <=(maxdata * 3 / 5)){
//             return '#aa0';
//           }else if(d.quantity>(maxdata * 1 / 5)&& d.quantity <=(maxdata * 2 / 5)){
//             return '#ac0';
//           }else{
//             return '#6c0';
//           }
//         },
//         'x':leftpad,
//         'y':function(d){
//           return d.x*25+toppad;
//         }
//       });
// //title
// 	s.append('g').selectAll('text')
// 	  .data("t").enter().append('text')
// 	  .attr({'fill':'#222','x':180 ,'y':50 }).text(title)
// 	  .style({'font-size':'32px','font-family':'DFKai-sb'});
// //數量
//     num.selectAll('text')
//       .data(data)
//       .enter()
//       .append('text')
//       .attr({
//         'fill':'#000',
//         'x':function(d){
//           return d.quantity*scale+leftpad+10;
//         },
//         'y':function(d){
//           return d.x * 25 + toppad+10;
//         }
//       }).text(function(d){
//         return d.quantity;
//       }).style({
//         'font-size':'12px'
//       });
// //商品名字
//     date.selectAll('text')
//       .data(data)
//       .enter()
//       .append('text')
//       .attr({
//         'fill':'#000',
//         'text-anchor': 'end',
//         'x':leftpad-10,
//         'y':function(d){
//           return d.x * 25 + toppad+10;
//         }
//       }).text(function(d){
//         return (d.name.length>13?(grows_up(d.name.substring(0,12))+"..."):grows_up(d.name));
//       }).style({
//         'font-size':'16px'
//       });
// }
	$(function() {
		$(".bdyplane").animate({"opacity":"1"});
		order_source_auto("order_source");
		$(".searhbestsale").click(function(e) {
			
			$("#order_source").removeClass("error");
			if($("#order_source").val().length<1){
				$("#order_source").attr("placeholder","此為必填欄位");
				$("#order_source").addClass("error");
				return;
			}
			$('.visualize').remove();
			$("#board").css({"opacity":"0"});
			e.preventDefault();
			$.ajax({
				type : "POST",
				url : "saledifftypestatic.do",
				data : {action :"search_best_sale",ordersource: $("#order_source").val() ,time1 : $('#datepicker1').val(), time2 : $('#datepicker2').val()},
				success : function(result) {
					//alert(result);
					var json_obj = $.parseJSON(result);
					var chart_data=[],chart_obj={},i=0,j=0,k=0,list=[];
					//alert(json_obj.length);
					if(json_obj.length==0){
						warning_msg("查無資料");
						return;
					}
// 					var tmp_month=json_obj.entrance[0];
					$.each(json_obj,function(i, item) {
						if(i<10){
							list[i]=item.product_name;
							chart_obj[json_obj[i].product_name]=json_obj[i].quantity;
						}else{
							chart_obj["ten"]=true;
						}
					});
// 					json_obj[i].product_name;
// 					json_obj[i].quantity;
// 					for(i=0;i<json_obj.entrance.length;i++){
// 						if(json_obj.entrance[i]!=tmp_month){ //in&
// 							tmp_month=json_obj.entrance[i];
// 							chart_data[j]=chart_obj;
// 							chart_obj={};
// 							j++;
// 						}
					
					chart_obj["layer"]=$("#order_source").val();
// 						chart_obj[json_obj[i].product_name]=json_obj[i].quantity;
// 					}
					chart_data[0]=chart_obj;
					j++;
					
					draw_piechart(chart_data,list);
					$(".visualize").animate({"opacity":"1"});
					warning_msg('');
					
					
					
					
					
// 					$("#board").html('');
// 					var json_obj = $.parseJSON(result);
// 					var chart_data=[],chart_obj={},j=0;
// 					if(json_obj.length==0){warning_msg('---此平台查無商品---');return;}else{warning_msg("");}
// 					$.each (json_obj, function (i) {
// 						chart_obj={}
// 						chart_obj["x"]=j;
// 						chart_obj["quantity"]=json_obj[i].quantity;//(10-i)*30;
// 						chart_obj["name"]=json_obj[i].product_name;
// 						chart_data[j]=chart_obj;
// 						j++;
// 					});
// 					if($("#order_source").val().length>0){
// 						draw_piechart(chart_data,title);
// 					}
// 					else{
// 						draw_barchart(chart_data,"暢銷商品統計 TOP10!!");
// 					}
					$("#board").animate({"opacity":"1"});
				}
			});
		});
		$(".input-field-wrap").append("<div class='div_right_bottom upup'><img src='./images/upup.png'></div>");
		$(".input-field-wrap").after("<div class='div_right_top downdown' style='display:none;top:-15px;'><img src='./images/downdown.png'></div>");
		$(".upup").click(function(){
			$(".input-field-wrap").slideToggle("slow");
			$(".downdown").slideToggle();
		});
		$(".downdown").click(function(){
			$(".input-field-wrap").slideToggle("slow");
			$(".downdown").slideToggle();
		});
	});
</script>
	<div class="panel-content">
		<div class="datalistWrap" >
			<div class="input-field-wrap">
				<div class="form-wrap">
					<div class="form-row">
						<label for="">
							<span class="block-label">銷售平台<font color=red>(必填)</font></span>
							<input type="text" id="order_source">
						</label>
						<button class="btn btn-darkblue searhbestsale">查詢</button>
					</div>
					<div class="form-row">
						<label for="">
							<span class="block-label">轉單起日</span>
							<input type="text" class="input-date" id="datepicker1">
						</label>
						<div class="forward-mark"></div>
						<label for="">
							<span class="block-label" >轉單迄日</span>
							<input type="text" class="input-date" id="datepicker2">
						</label>
						<button class="btn btn-darkblue searhbestsale">查詢</button>
					</div>
				</div><!-- /.form-wrap -->
			</div><!-- /.input-field-wrap -->
			<!-- 第一列 -->
		</div>
	</div>
<div id="board" style="margin:30px auto;width:800px;opacity:0;"></div>
<div class="validateTips" align="center"> </div>
<div id="chart" align="center" style="opacity:0"></div>
</div>
</div>
</body>
</html>