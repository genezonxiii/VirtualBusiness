<!--//############################################################-->
<!--//#####################簡化版 Jsp code#######################-->
<!--//############################################################-->
<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ page import="tw.com.aber.*"%>
<%@ page import="java.util.*"%>
<%@ page import="java.sql.DriverManager"%>
<%@ page import="java.sql.Connection"%>
<%@ page import="java.sql.Statement"%>
<%@ page import="java.sql.ResultSet"%>
<jsp:directive.page import="java.sql.SQLException" />
<!DOCTYPE html>
<html>
<head>
<title>出貨量統計圖</title>
<meta charset="utf-8">
<link rel="Shortcut Icon" type="image/x-icon" href="./images/Rockettheme-Ecommerce-Shop.ico" />
<link rel="stylesheet" href="css/styles.css" />
<link href="<c:url value="css/css.css" />" rel="stylesheet">
<link href="<c:url value="css/jquery.dataTables.min.css" />" rel="stylesheet">
<link href="<c:url value="css/1.11.4/jquery-ui.css" />" rel="stylesheet">

<link type="text/css" rel="stylesheet" href="css/visualize.jQuery.css"/>

</head>
<body>
	<jsp:include page="template.jsp" flush="true"/>
	<div class="content-wrap" style="overflow-y:auto;">
		<div class='bdyplane' style="opacity:0">
<script type="text/javascript" src="js/jquery-1.10.2.js"></script>
<script type="text/javascript" src="js/jquery-1.11.4.js"></script>
<script type="text/javascript" src="js/jquery.dataTables.min.js"></script>
<script type="text/javascript" src="js/jquery-ui.min.js"></script>
<script type="text/javascript" src="js/jquery-migrate-1.4.1.min.js"></script>
<script type="text/javascript" src="js/jquery.validate.min.js"></script>
<script type="text/javascript" src="js/additional-methods.min.js"></script>
<script type="text/javascript" src="js/messages_zh_TW.min.js"></script>

<script src="js/d3.v3.min.js"></script>

<script type="text/javascript" src="js/visualize.jQuery.js"></script>

<script>
function draw_linechart_plotly(data,list,choose){
	
}
function draw_linechart(data,list,choose){
// 	for(i=0;i<data.length;i++){
// 		$.each(data[i],function(k, item) {
// 			alert(i+"&&"+k+"&&"+item);
// 		});
// 	}
	//alert(data[0]["vender"]);
	var i,j;
// 	for(i=0;i<data.length;i++){
// 		$.each(data[i],function(k, item) {
// 			alert(k+"&&"+item);
// 		});
// 	}
	var charttable="<table id='line' style='display:none'><caption>銷售金額統計圖 </caption><thead><tr><td></td><th></th>";
	$.each(list,function(i, item) {
		charttable+="<th>"+(choose=='week'?get_week_day(item):('<span style="width:36px;">'+item+'月</span>'))+"</th>";
	});
	charttable += "<th></th></tr></thead><tbody>";
	for(i=0;i<data.length;i++){
		charttable+="<tr><th>"+data[i]["vender"]+"</th><td></td>";
		for(j=0;j<list.length;j++){
			charttable+="<td>"+(data[i][list[j]]==null?"0":data[i][list[j]])+"</td>";
		}
		charttable+="</tr>";
	}
	charttable+="</tbody></table>";
	var choose_chart=(choose=='week'?'#chart1':'#chart2');
	$(choose_chart).html(charttable);
	$(choose_chart).visualize({
		'type':'line',
		'lineWeight':'3',
		'width':'1000','height':'300',
		'appendTitle':'true','title':'銷售金額統計圖('+(choose=='week'?'週':'月')+')',
		'appendKey':'true',
		'colors':['#e9e744','#666699','#92d5ea','#ee8310','#8d10ee','#5a3b16','#26a4ed','#f45a90','#be1e2d'],
		'textColors':'','parseDirection':'x',
		'yLabelInterval': '40'
	});
	$(".visualize").css("opacity","0");
	//alert(charttable);
}


function getYearWeek(a, b, c) { 
/* date1是当前日期 date2是当年第一天 d是当前日期是今年第多少天 \用d + 当前年的第一天的周差距的和在除以7就是本年第几周 */ 
	var date1 = new Date(a, parseInt(b) - 1, c), date2 = new Date(a, 0, 1), 
	d = Math.round((date1.valueOf() - date2.valueOf()) / 86400000); 
	return Math.ceil((d + ((date2.getDay() + 1) - 1)) / 7 ); 
}; 

function draw_chart(m_h,m_w,data){
	//alert(getYearWeek(2016,1,4));
	var max_h = m_h, max_w = data.length*200,padding=70;
	var xtag=[],ytag=[];
	var i=0,j=0,max_data=0,min_data=100000000,max_month=0,min_month=200;
	for(i=0;i<data.length;i++){if(data[i].sale>max_data)max_data=data[i].sale;}
	for(i=0;i<data.length;i++){if(data[i].sale<min_data)min_data=data[i].sale;}
	for(i=0;i<data.length;i++){if(data[i].year>max_month)max_month=data[i].year;}
	for(i=0;i<data.length;i++){if(data[i].year<min_month)min_month=data[i].year;}
	j=0;
	for(i=min_month;i<max_month+1;i++){
		xtag[j]=i;j++;
	}
	max_w = (max_month-min_month+2)*100>1000?1000:(max_month-min_month+2)*100;
	var real_date=[],k=min_month,get_day=0;//k紀錄目前周指標
	for(i=1;i<13&&k<max_month+1;i++){
		for(j=1;j<32&&k<max_month+1;j++){
			if(j==31){
				if(i==2||i==4||i==6||i==9||i==11){continue;}
			}
			if(getYearWeek(2016,i,j)==k+1){
				real_date[k-min_month]={x:k-min_month,date:i+"/"+((j<10)?"0"+j:j)};
				k++;
			}
		}
	}
	//var data2 = [ {"sale": "20020","year": "6"}, {"sale": "82004","year":"7" }];
	//alert(max_data+","+min_data+" "+max_month+","+min_month);
 	//var data=[];
	$('#chart').html("");
     var vis = d3.select("#chart").append('svg').attr({'width':max_w+padding, 'height':max_h+padding});
     var WIDTH = max_w,HEIGHT = max_h,
         MARGINS = {
             top: padding,bottom: padding,
             right: padding,left: padding*2
         };
     var xScale = d3.scale.linear().range([MARGINS.left, WIDTH - MARGINS.right]).domain([min_month-1, max_month+1]);
     var yScale = d3.scale.linear().range([HEIGHT - MARGINS.top, MARGINS.bottom]).domain([min_data * 4 / 5 ,max_data * 10 / 9 ]);
     var xAxis = d3.svg.axis().scale(xScale).ticks(max_month-min_month+2);
     var yAxis = d3.svg.axis().scale(yScale).orient("left").ticks(6);

     vis.append("svg:g")
         .attr("class", "y axis")
         .attr("transform", "translate(" + (MARGINS.left) + ",0)")
         .call(yAxis).style({'font-size':'24px'});
     var lineGen = d3.svg.line()
     	 .x(function(data) {return xScale(data.year);})
         .y(function(data) {return yScale(data.sale);})
         .interpolate("basis");
//      vis.append('svg:path')
//          .attr('d', lineGen(data))
//          .attr('stroke', 'green')
//          .attr('stroke-width', 2)
//          .attr('fill', 'none');
     var tmp=[];
     var tmp_vender=data[0].vender;
     var vender_n=0;
     for(i=0,j=0;i<data.length;i++){
//     	 alert(i+" "+j+"+1= "+data[i].vender);
    	 tmp[j]=data[i];
    	 j++;
		 if(i+1==data.length||tmp_vender!=data[i+1].vender){
			 //alert(data[i+1]);
			 
			
			//alert("進來"+tmp_vender+" ");
			//alert(tmp[j-1].sale+tmp[j-1].vender+tmp[j-1].year+"   "+i);
			//var color="#"+(Math.round(Math.random()*888888)+111111);
			var color=vender_color(tmp_vender);
				//color="#"+('00000'+(0|Math.random()*(1<<24)).toString(16)).slice(-6);
			if(i+1!=data.length)tmp_vender=data[i+1].vender;
			vis.append('svg:path').attr('d', lineGen(tmp)).attr('stroke', color)
		       .attr('stroke-width',4).attr('fill', 'none');
			
			//alert(color);
			//#######################以下畫哪家vender的說明#############################
			vis.append('line').attr('x1', max_w-60).attr('y1', 100+30*vender_n)
	 			 			  .attr('x2', max_w-40).attr('y2',100+30*vender_n)
	 			 			  .style('stroke',color).style('stroke-width', 15);
			vis.append('g').selectAll('text')
			  .data(data).enter().append('text')
			  .attr({'x':max_w-30 ,'y':105+30*vender_n ,}).text(data[i].vender)
			  .style('fill',color).style({'font-size':'18px'});
			vender_n++;
			//#######################以上畫哪家vender的說明#############################
			j=0;
			tmp=[];
		 }
		 
     }
//   //##############單位######################  
	vis.append('g').selectAll('text')
	  .data(data).enter().append('text')
	  .attr({'x':padding ,'y':padding/2 ,}).text("銷售金額")
	  .style({'font-size':'28px'});
	vis.append('g').selectAll('text')
	  .data(data).enter().append('text')
	  .attr({'x':max_w-padding/2 ,'y':max_h-padding/2 }).text("月份")
	  .style({'font-size':'28px'});
     //#############X軸下面文字和x軸########################
	 vis.append('g').attr({'id':'num'}).selectAll('text')
	    .data(real_date).enter().append('text')
	    .attr({
	      'fill':'#000',
	      'x':function(d){return ((padding*9)/5)+((d.x+1)*((max_w-padding*3) /(max_month-min_month+2)));},
	      'y':function(d){return max_h-padding/2;}
	    }).text(function(d){return d.date;})
	    //.attr("transform","rotate(10)")
	    .style({'font-size':'12px'})
	    .style('fill', function(d){return ((d.x%2==0)?'#000000':'#555555')});
     
     vis.append('line') .attr('x1', padding*2-5    ).attr('y1', max_h-padding)
		 				.attr('x2', max_w-padding-5).attr('y2', max_h-padding)
		 				.style('stroke', 'black').style('stroke-width', 5);
 };
	$(function() {
		//alert(get_week_day(22));
		$(".bdyplane").animate({"opacity":"1"});
		$("#searh-productunit").click(function(e) {
			$(".visualize").animate({"opacity":"0"});
			//$(".validateTips").html("<h4 style='color:red;'>資料查詢中...</h4>");
			$("#chart1").html('');
			$("#chart2").html('');
			e.preventDefault();
			$('.visualize').remove();
			$.ajax({
				type : "POST",
				url : "saleamountchart.do",
				data : {action :"search_week",time1 : $('#datepicker1').val(),time2 : $('#datepicker2').val()},
				success : function(result) {
					alert(result);
					$(".validateTips").html('');
					var json_obj = $.parseJSON(result);
					var chart_data=[],chart_obj={},i=0,j=0,list=[];
					var tmp_vender=json_obj.vender[0];
					if(json_obj.entrance.length==0){
						$(".validateTips").html("<h2 style='color:red;'>查無資料</h2>");
						return;
					}
					var max=0,min=10000;
					$.each(json_obj.entrance,function(i, item) {
						var k=0;
						if(item>max){max=item;}
						if(item<min){min=item;}
						while(list[k]!=item && list[k]!=null){k++;}
						if(k==list.length)list[k]=item;
					});
					var k=0;
					for(k=0;min+k<max+1;k++){
						list[k]=min+k;
					}
					
					for(i=0,j=0;i<json_obj.entrance.length;i++){
						if(json_obj.vender[i]!=tmp_vender){
							chart_data[j]=chart_obj;
							j++;
							tmp_vender=json_obj.vender[i];
							chart_obj={};
						}
						chart_obj["vender"]=json_obj.vender[i];
						chart_obj[json_obj.entrance[i]]=json_obj.answer[i];
					}
					chart_data[j]=chart_obj;
					j++;
// 					draw_linechart_plotly(chart_data,list,'week');
					draw_linechart(chart_data,list,'week');
					$(".visualize").animate({"opacity":"1"});
				}
			});
			var show_or_not=0;
			if(!show_or_not){
				$("#hello").remove();
			}else{
				$.ajax({
					type : "POST",
					url : "saleamountchart.do",
					data : {action :"search_month",time1 : $('#datepicker1').val(),time2 : $('#datepicker2').val()},
					success : function(result) {
						$(".validateTips").html('');
						var json_obj = $.parseJSON(result);
						var chart_data=[],chart_obj={},i=0,j=0,list=[];
						var tmp_vender=json_obj.vender[0];
						if(json_obj.entrance.length==0){
							$(".validateTips").html("<h2 style='color:red;'>查無資料</h2>");
							return;
						}
						var max=0,min=10000;
						$.each(json_obj.entrance,function(i, item) {
							var k=0;
							if(item>max){max=item;}
							if(item<min){min=item;}
							while(list[k]!=item && list[k]!=null){k++;}
							if(k==list.length)list[k]=item;
						});
						var k=0;
						for(k=0;min+k<max+1;k++){
							list[k]=min+k;
						}
						
						for(i=0,j=0;i<json_obj.entrance.length;i++){
							if(json_obj.vender[i]!=tmp_vender){
								chart_data[j]=chart_obj;
								j++;
								tmp_vender=json_obj.vender[i];
								chart_obj={};
							}
							chart_obj["vender"]=json_obj.vender[i];
							chart_obj[json_obj.entrance[i]]=json_obj.answer[i];
						}
						chart_data[j]=chart_obj;
						j++;
						
						draw_linechart(chart_data,list,'month');
						$(".visualize").animate({"opacity":"1"});
					}
				});
			}
		});
		$(".input-field-wrap").append("<div class='div_right_bottom upup'><img src='./images/upup.png'></div>");
		$(".input-field-wrap").after("<div class='div_right_top downdown' style='display:none;'><img src='./images/downdown.png'></div>");
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
			<div class="input-field-wrap">
				<div class="form-wrap">
					<div class="form-row">
						<label for="">
							<span class="block-label">轉單起日</span>
							<input type="text" class="input-date" id="datepicker1">
						</label>
						<div class="forward-mark"></div>
						<label for="">
							<span class="block-label">轉單迄日</span>
							<input type="text" class="input-date" id="datepicker2">
						</label>
						<button id="searh-productunit" class="btn btn-darkblue">查詢</button>
					</div>
				</div><!-- /.form-wrap -->
			</div><!-- /.input-field-wrap -->
			<!-- 第一列 -->
	<div style="margin:30px auto;width:1000px;"><div id="chart1" style="opacity:0;"></div></div>
<div class="validateTips" align="center" style="position:relative;height:0px;top:-25px;"> </div>	
	<div id='hello' style="margin:80px auto;width:1000px;"><div id="chart2" style="opacity:0;"></div></div>
</div>
</div>
</body>
</html>