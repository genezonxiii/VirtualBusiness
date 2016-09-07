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
<style>
.visualize-pie .visualize-info { top: 10px; border: 0; right: auto; left: 10px; padding: 0; background: none; }
.visualize-pie ul.visualize-title { font-weight: bold; border: 0; }
.visualize-pie ul.visualize-key li { float: none; }
.visualize-pie { margin:5px 10px; }
.visualize { border: 0px;float: left; }
</style>
</head>
<body>
	<jsp:include page="template.jsp" flush="true"/>
	<div class="content-wrap" >
		<div class='bdyplane' style="opacity:0">
<script type="text/javascript" src="js/jquery-1.10.2.js"></script>
<script type="text/javascript" src="js/jquery-1.11.4.js"></script>
<script type="text/javascript" src="js/jquery.dataTables.min.js"></script>
<script type="text/javascript" src="js/jquery-ui.min.js"></script>
<script type="text/javascript" src="js/jquery-migrate-1.4.1.min.js"></script>
<script type="text/javascript" src="js/jquery.validate.min.js"></script>
<script type="text/javascript" src="js/additional-methods.min.js"></script>
<script type="text/javascript" src="js/messages_zh_TW.min.js"></script>
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
			charttable+="<th>"+item+"</th>";
		});
		charttable += "</tr></thead><tbody>";
		//i=0;
	// 	for(i=0;i<data.length;i++){
			charttable+="<tr><th>"+data[m]["month"]+"</th>";
			for(j=0;j<list.length;j++){
				charttable+="<td>"+(data[m][list[j]]==null?"":data[m][list[j]])+"</td>";
			}
			charttable+="</tr>";
	// 	}
		charttable+="</tbody></table>";
		
		$('#chart-'+m).html(charttable);
		$('#chart-'+m).visualize({
			'type':'pie',
			'pieMargin':'50','pieLabelPos':'outside',
			'width':'380','height':'300',
			'appendTitle':'true','title':data[m]["month"]+'月銷售金額比例統計圖',
			'appendKey':'true',
			'colors':['#e9e744','#666699','#92d5ea','#ee8310','#8d10ee','#5a3b16','#26a4ed','#f45a90','#be1e2d'],
			'textColors':'','parseDirection':'y'
		});
		$(".visualize").css("opacity","0");
	}

}
function draw_chart(m_h,m_w,data){
	var i,j;
	var vender_exist=[],tmp_vender="ya!~";
	
	for(i=0,j=0;i<data.length;i++){
		for(j=0;j<vender_exist.length;j++){
			if(vender_exist[j]==data[i].vender||data[i].vender.length==0){break;}
		}
		if(j==vender_exist.length){
			vender_exist[j]=data[i].vender;
		}
	}
	var w = m_w,                        //width
    h = m_h,                            //height
    r = 150,                            //radius
    color = d3.scale.category20c();     //builtin range of colors
    
    var vis = d3.select("#chart")
        .append("svg:svg")              //create the SVG element inside the <body>
        .data([data])                   //associate our data with the document
            .attr("width", w)           //set the width and height of our visualization (these will be attributes of the <svg> tag
            .attr("height", h)
        .append("svg:g")                //make a group to hold our pie chart
            .attr("transform", "translate(" + (r * 4 / 3) + "," + (r * 4 / 3) + ")")    //move the center of the pie chart from 0, 0 to radius, radius

    var arc = d3.svg.arc()              //this will create <path> elements for us using arc data
        .outerRadius(r);
    var pie = d3.layout.pie()           //this will create arc data for us given a list of values
        .value(function(d) {return d.value; });    //we must tell it out to access the value of each element in our data array
    var arcs = vis.selectAll("g.slice")     //this selects all <g> elements with class slice (there aren't any yet)
        .data(pie)                          //associate the generated pie data (an array of arcs, each having startAngle, endAngle and value properties) 
        .enter()                            //this will create <g> elements for every "extra" data element that should be associated with a selection. The result is creating a <g> for every object in the data array
            .append("svg:g")                //create a group to hold each slice (we will have a <path> and a <text> element associated with each slice)    
  //      		.attr('stroke',"#111111")
        		.attr("class", "slice");    //allow us to style things in the slices (like text)
        arcs.append("svg:path")
                .attr("fill", function(d, i) { return vender_color(data[i].vender); } ) //set the color for each slice to be chosen from the color function defined above
                .attr("d", arc);                                    //this creates the actual SVG path using the associated data (pie) with the arc drawing function
        arcs.append("svg:text")                                     //add a label to each slice
                .attr("transform", function(d) {                    //set the label's origin to the center of the arc
                //we have to make sure to set these before calling arc.centroid
                d.innerRadius = r*4/3;
                d.outerRadius = 2*r;
                return "translate(" + arc.centroid(d) + ")";        //this gives us a pair of coordinates like [50, 50]
            })
            .attr("text-anchor", "middle")                          //center the text on it's origin
            .text(function(d, i) { return data[i].label; }).style({'font-size':'14px'});        //get the label from our original data array   
 		//########vender#################################
        for(i=0;i<vender_exist.length;i++){
    		vis.append('line').attr('x1', m_w/2-40).attr('y1', -100+30*i)
    				 			  .attr('x2',m_w/2-10).attr('y2',-100+30*i)
    				 			  .style('stroke',vender_color(vender_exist[i])).style('stroke-width',15);
    		vis.append('g').selectAll('text')
    		  .data(vender_exist).enter().append('text')
    		  .attr({'x':m_w/2 ,'y':-100+30*i+5 ,}).text(vender_exist[i])
    		  .style('fill',vender_color(vender_exist[i]))
    		  .style({'font-size':'18px'});
      	}
 };
	$(function() {
		$(".bdyplane").animate({"opacity":"1"});
		$("#searh-productunit").click(function(e) {
			e.preventDefault();
			$.ajax({
				type : "POST",
				url : "saleamountstaticchart.do",
				data : {action :"searh", time1 : $('#datepicker1').val(), time2 : $('#datepicker2').val()},
				success : function(result) {
					//console.log(result);
					//alert($('#datepicker2').val());
					var json_obj = $.parseJSON(result);
					var chart_data=[],chart_obj={},i=0,j=0,list=[];
					
					if(json_obj.entrance.length==0){
						$("#chart").html("<h2 style='color:red;'>查無資料</h2>");
						return;
					}
					var tmp_month=json_obj.entrance[0];
					$.each(json_obj.vender,function(i, item) {
						var k=0;
						while(list[k]!=item && list[k]!=null){k++;}
						if(k==list.length)list[k]=item;
					});
					
					for(i=0;i<json_obj.entrance.length;i++){
						if(json_obj.entrance[i]!=tmp_month){ //in&
							tmp_month=json_obj.entrance[i];
							chart_data[j]=chart_obj;
							chart_obj={};
							j++;
						}
						chart_obj["month"]=json_obj.entrance[i];
						chart_obj[json_obj.vender[i]]=json_obj.answer[i];
					}
					chart_data[j]=chart_obj;
					j++;
					
					draw_piechart(chart_data,list);
					$(".visualize").animate({"opacity":"1"});
					
// 					var result_table = "";
// 					var data=[];
// 					var i=0,j=0,last_month=0;
// 					$('#chart').html("");
// 					if(json_obj.entrance.length>0){last_month=json_obj.entrance[0];}
// 					for(i=0,j=0;i<=json_obj.entrance.length;i++,j++){
// 						if(json_obj.entrance[i]!=last_month){
// 							draw_chart(450,550,data);
// 							last_month=json_obj.entrance[i];
// 							data=[];
// 							j=0;
// 						}
// 						data[j]={"label":json_obj.entrance[i]+"月:"+json_obj.answer[i]+"%", "value":json_obj.answer[i],"vender":json_obj.vender[i]};
// 					}
// 					$("#chart").animate({"opacity":"0.5"});
// 					$("#chart").animate({"opacity":"1"});
				}
			});
		});
	});
</script>
	<div class="panel-content">
		<div class="datalistWrap" >
			<div class="input-field-wrap">
				<div class="form-wrap">
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
						<button id="searh-productunit" class="btn btn-darkblue">查詢</button>
					</div>
				</div><!-- /.form-wrap -->
			</div><!-- /.input-field-wrap -->
			<!-- 第一列 -->
		</div>
	</div>
<div class="validateTips" align="center"> </div>
<div id="chart-0" align="center" style="opacity:0"></div>
<div id="chart-1" align="center" style="opacity:0"></div>
<div id="chart-2" align="center" style="opacity:0"></div>
<div id="chart-3" align="center" style="opacity:0"></div>
<div id="chart-4" align="center" style="opacity:0"></div>
<div id="chart-5" align="center" style="opacity:0"></div>
<div id="chart-6" align="center" style="opacity:0"></div>
<div id="chart-7" align="center" style="opacity:0"></div>
<div id="chart-8" align="center" style="opacity:0"></div>
<div id="chart-9" align="center" style="opacity:0"></div>
<div id="chart-10" align="center" style="opacity:0"></div>
<div id="chart-11" align="center" style="opacity:0"></div>
<div id="chart-12" align="center" style="opacity:0"></div>
</div>
</div>
</body>
</html>