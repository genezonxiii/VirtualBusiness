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
<link rel="stylesheet" href="css/styles.css" />
<link href="<c:url value="css/css.css" />" rel="stylesheet">
<link href="<c:url value="css/jquery.dataTables.min.css" />" rel="stylesheet">
<link href="<c:url value="css/1.11.4/jquery-ui.css" />" rel="stylesheet">
<script type="text/javascript" src="js/jquery-1.10.2.js"></script>
<script type="text/javascript" src="js/jquery-1.11.4.js"></script>
<script type="text/javascript" src="js/jquery.dataTables.min.js"></script>
<script type="text/javascript" src="js/jquery-ui.min.js"></script>
<script type="text/javascript" src="js/jquery-migrate-1.4.1.min.js"></script>
<script type="text/javascript" src="js/jquery.validate.min.js"></script>
<script type="text/javascript" src="js/additional-methods.min.js"></script>
<script type="text/javascript" src="js/messages_zh_TW.min.js"></script>
<script type="text/javascript" src="js/d3.v3.min.js"></script>

<script>
function vender_color(vender){
	if(vender=="ibon"){return '#FF0000';}
	if(vender=="九易"){return '#EEEE00';}
	if(vender=="Pchome"){return '#0000FF';}
	if(vender=="ASAP"){return '#FF6666';}
	if(vender=="GoHappy"){return '#FF00FF';}
	if(vender=="國泰Tree"){return '#00FFFF';}
	if(vender=="17Life"){return '#BBBBBB';}
	if(vender=="yahoo"){return '#BBBB00';}
	if(vender=="UDN"){return '#BB00BB';}
	if(vender=="樂天"){return '#00BBBB';}
	if(vender=="愛買"){return '#BBBBFF';}
	if(vender=="夠麻吉"){return '#BBFFBB';}
	if(vender=="通用"){return '#FFBBBB';}
	if(vender=="超級商城"){return '#6666FF';}
	if(vender=="博客來"){return '#66FF66';}
	if(vender=="momo"){return '#00FF00';}
	if(vender=="payeasy"){return '#006666';}
	if(vender=="myfone"){return '#660066';}
	if(vender=="森森購物"){return '#666600';}
	if(vender=="Line Mart"){return '#333333';}
	return '#000000';
}

function draw_chart(m_h,m_w,data){
	var i,j;
	var vender_exist=[],tmp_vender="ya!~";
	
	for(i=0,j=0;i<data.length;i++){
		for(j=0;j<vender_exist.length;j++){
			//alert("第二層: i="+i+" j="+j);
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
    $('#chart').html("");
    var vis = d3.select("#chart")
        .append("svg:svg")              //create the SVG element inside the <body>
        .data([data])                   //associate our data with the document
            .attr("width", w)           //set the width and height of our visualization (these will be attributes of the <svg> tag
            .attr("height", h)
        .append("svg:g")                //make a group to hold our pie chart
            .attr("transform", "translate(" + r + "," + r + ")")    //move the center of the pie chart from 0, 0 to radius, radius

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
                d.innerRadius = 0;
                d.outerRadius = r;
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
 		
 		
//  		var vender_n=0;
//         vis.append('line').attr('x1', m_w-100).attr('y1', -100)
// 		  .attr('x2', m_w).attr('y2',-100)
// 		  .style('stroke',color).style('stroke-width', 3);
// 		vis.append('g').selectAll('text')
// 			.data(data).enter().append('text')
// 			.attr({'x':m_w ,'y':105+30*vender_n ,}).text(data[i].vender)
// 			.style('stroke',color).style({'font-size':'18px'});
 };
 
 
function date_format(str) {
	var words=str.replace(","," ").split(" ");
	return words[3]+"-"+words[0].replace("一月","1").replace("二月","2").replace("三月","3").replace("四月","4").replace("五月","5").replace("六月","6").replace("七月","7").replace("八月","8").replace("九月","9").replace("十月","10").replace("十一月","11").replace("十二月","12")+"-"+words[1];
}
	$(function() {
		$( "#datepicker1" ).datepicker({dateFormat: 'yy/mm/dd'});
		$( "#datepicker2" ).datepicker({dateFormat: 'yy/mm/dd'});
		$("#searh-productunit").button().on("click",function(e) {
			
			e.preventDefault();
			$.ajax({
				type : "POST",
				url : "saleamountstaticchart.do",
				data : {action :"searh",time1 : $('#datepicker1').val(),time2 : $('#datepicker2').val()},
				success : function(result) {
					//alert(result);
					var json_obj = $.parseJSON(result);
					var result_table = "";
					var data=[];
					var i=0;
					for(i=0;i<json_obj.entrance.length;i++){
						if(json_obj.entrance[i]!=0){
							data[i]={"label":json_obj.entrance[i]+"月:"+json_obj.answer[i]+"%", "value":json_obj.answer[i],"vender":json_obj.vender[i]};
						}
					}
					if(data.length!=0){
						draw_chart(300,450,data);
					}else{
						$("#chart").html("<h2 style='color:red;'>查無資料</h2>");
					}
				}
			});
		});
		$("#dialog-confirm").html("<p>是否確認刪....</p>");
		$("#dialog-confirm").dialog({
			title: "你妳你妳你",
			draggable : false,//防止拖曳
			resizable : false,//防止縮放
			autoOpen : false,
			height : "auto",
			modal : true,
			show : {effect : "blind",duration : 1000},
 			hide : {effect : "blind",duration : 1000},
			buttons : {
				"確認刪除" : function() {alert("嘿嘿嘿~");$(this).dialog("close");},
				"取消刪除" : function() {$(this).dialog("close");}
			}
		});
		//刪除事件聆聽 : 因為聆聽事件動態產生，所以採用delegate來批量處理，節省資源
		$("#products").delegate(".btn_delete", "click", function() {
			uuid = $(this).val();
			confirm_dialog.dialog("open");
		});
		//新增事件聆聽
		$("#create-productunit").button().on("click", function() {
			insert_dialog.dialog("open");
		});
		//預設表格隱藏
		$("#products-contain").hide();
	});
</script>
<style>
.row {
    border-bottom: 0px;
    margin-bottom: 0px;
    padding-bottom: 0px;
}
::-webkit-input-placeholder {text-align: center;}
:-moz-placeholder {text-align: center; } /* Firefox 18- */
::-moz-placeholder { text-align: center; }/* Firefox 19+ */
:-ms-input-placeholder { text-align: center; }
</style>
</head>
<body style="font-size: 15px;">

<script>
</script>
<div style="margin:20px;">
	<div class="panel-title">
		<h2 style="font-size: 25px;">銷售金額統計圖</h2>
	</div>
	<div class="panel-content">
		<div class="datalistWrap" >

			<!-- 第一列 -->
			<div class="row" >
				<div id="products-serah-create-contain" style="width: 800px;margin:0px auto;" >
					<table id="products-serah-create" class="ui-widget ui-widget-content">
						<tr class="ui-widget-header">
							<th >
								<p style="width:120px;">轉單日</p>
							</th><th>
								<input type="text" id="datepicker1" placeholder="起">
							</th><th>
								~
							</th><th>
								<input type="text" id="datepicker2" placeholder="迄">
							</th><th>
								<button id="searh-productunit" onclick="" style="width:80px;">查詢</button>
							</th>
						</tr>
					</table>
				</div>
			</div>
			<!-- 第二列 -->
			<div class="row" align="left" >
				<div id="products-contain" class="ui-widget">
					<table id="products" class="ui-widget ui-widget-content">
						<thead>
							<tr class="ui-widget-header">
								<th>銷貨單號</th>
								<th>訂單號</th>
								<th><p style="width:320px;">產品名稱</p></th>
								<th>客戶自訂產品ID</th>
								<th><p style="width:80px;">銷貨數量</p></th>
								<th><p style="width:80px;">銷貨金額</p></th>
								<th><p style="width:120px;">轉單日</p></th>
								<th><p style="width:120px;">配送日</p></th>
								<th><p style="width:120px;">銷貨/出貨日期</p></th>
								<th><p style="width:100px;">銷售平台</p></th>
								<th>備註</th>			
							</tr>
						</thead>
						<tbody>
						</tbody>
					</table>
				</div>
				<span class="validateTips"> </span>
				<div id="chart" align="center"></div>
			</div>
		</div>
	</div>
<script>	

//for(i=0;i<10;i++){data[i]={x:i, y:Math.floor(Math.random()*300)};}
</script>

<div id="dialog-confirm"></div>
</div>
</body>
</html>