<!--//############################################################-->
<!--//#####################簡化版 Jsp code#######################-->
<!--//############################################################-->
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
<title>出貨量統計圖</title>
<meta charset="utf-8">
<link rel="Shortcut Icon" type="image/x-icon" href="./images/Rockettheme-Ecommerce-Shop.ico" />
<link rel="stylesheet" href="css/styles.css" />
<link href="<c:url value="css/css.css" />" rel="stylesheet">
<link href="<c:url value="css/jquery.dataTables.min.css" />" rel="stylesheet">
<link href="<c:url value="css/1.11.4/jquery-ui.css" />" rel="stylesheet">
		
<link type="text/css" rel="stylesheet" href="css/visualize.jQuery.css"/>
<!-- 		<link type="text/css" rel="stylesheet" href="https://www.filamentgroup.com/examples/charting_v2/demopage.css"/> -->

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
<script src="js/d3.v3.min.js"></script>

<!-- <script type="text/javascript" src="http://ajax.googleapis.com/ajax/libs/jquery/1.3.2/jquery.min.js"></script> -->
<script type="text/javascript" src="js/visualize.jQuery.js"></script>

<script>
function draw_barchart(data,list){
	//alert(data[0]["yahoo"]==null);
	var i,j;
	var charttable="<table id='bar' style='display:none'><caption>出貨量統計圖</caption><thead><tr><td></td>";
	$.each(list,function(i, item) {
		charttable+="<th>"+item+"</th>";
	});
	charttable+="</tr></thead><tbody>";
	
	
	for(i=0;i<data.length;i++){
		charttable+="<tr><th>"+data[i]["month"]+"月</th>";
		for(j=0;j<list.length;j++){
			charttable+="<td>"+(data[i][list[j]]==null?"":data[i][list[j]])+"</td>";
			//alert(list[j]+"的資料:"+data[i][list[j]]);
		}
		charttable+="</tr>";
// 		$.each(data[i],function(j, item) {alert(j+" & "+item);});
	}
	charttable+="</tbody></table>";
	$('.visualize').remove();
	$('#chart').html(charttable);
	$('#chart').visualize({
		'type':'bar',
		'barMargin':'3','barGroupMargin':'10',
		'width':'800','height':'300',
		'appendTitle':'true','title':'出貨量統計圖',
		'appendKey':'true',
		'colors':['#e9e744','#666699','#92d5ea','#ee8310','#8d10ee','#5a3b16','#26a4ed','#f45a90','#be1e2d'],
		'textColors':'','parseDirection':'y',
		'yLabelInterval': '40'
	});
	$(".visualize").css("opacity","0");
	//alert(charttable);
}
function draw_chart(m_h,m_w,data){
	//alert(data.length);
	$('#chart').html("");
	var max_h = m_h, max_w = m_w, max_data=0,padding=70;
	max_w=data.length*30+100;
	var i=0,j=0;
	for(i=0;i<data.length;i++){if(data[i].y>max_data)max_data=data[i].y;}
	max_data=max_data*5/4;
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
	//alert("共有廠商: "+vender_exist.length);
	
	//alert(max_data);
	//##############表格大小， 輸入資料， 位置######################
	var s = d3.select('#chart').append('svg').attr({'width':max_w+4*padding, 'height':max_h+padding*2});
    s.selectAll('rect').data(data).enter().append('rect')
     .attr({
      'fill':function(d){
    	  return vender_color(d.vender);
      },
      'width':(max_w/(data.length+1))-0,
      'height':function(d){
    	  //alert("d.y= "+d.y+" max_h= "+max_h+" max_data= "+max_data);
      	  return (d.y * max_h * 7 / 11 ) / max_data;
      },
      'x':function(d){return ((d.x) *(max_w / (data.length+1)))+padding*3 / 2;},
      'y':function(d){return max_h - ((d.y*max_h *7 / 11 ) / max_data)- padding;}
     });
  //#############數量文字和vender######################
    s.append('g').attr({'id':'num'}).selectAll('text')
    .data(data).enter().append('text')
    .attr({
      'fill':'#000',
      'x':function(d){return ((d.x) *(max_w/(data.length+1)))+padding*3 / 2+5;},
      'y':function(d){return max_h - ((d.y*max_h *7 / 11) / max_data)- padding/2 - 40;}
    }).text(function(d){return d.y;})
    .style({'font-size':'20px'});
  //@@
//     s.append('g').attr({'id':'num'}).selectAll('text')
//     .data(data).enter().append('text')
//     .attr({
//       'fill':'#000',
//       'x':function(d){return ((d.x) *(max_w/(data.length+1)))+padding;},
//       'y':function(d){return max_h - ((d.y*max_h *7 / 11) / max_data)- padding/2 - 50;}
//     }).text(function(d){return d.vender;})
//     .style({'font-size':'18px'});
  //##############X軸下面文字######################
    s.append('g').attr({'id':'num'}).selectAll('text')
    .data(data).enter().append('text')
    .attr({
      'fill':'#000',
      'x':function(d){return ((d.x) * (max_w / (data.length+1))+(padding*3/2))+10;},
      'y':function(d){return max_h-padding/2;}
    }).text(function(d){return d.month;})
    .style({'font-size':'28px'});
  //##############單位######################  
	s.append('g').selectAll('text')
	  .data(data).enter().append('text')
	  .attr({'x':0 ,'y':padding*2/3 ,}).text("訂單數")
	  .style({'font-size':'28px'});
	s.append('g').selectAll('text')
	  .data(data).enter().append('text')
	  .attr({'x':max_w+padding* 3 / 2 ,'y':max_h-padding*4/5 }).text("月份")
	  .style({'font-size':'28px'});
	

  //##############Y軸######################
    var tmp=[];
    for(var i=0;i*50<max_data;i++){tmp[i]=i*50;}
    var xScale = d3.scale.linear().range([padding,max_h-padding]).domain([max_data, 0]);;  
    //var xAxis = d3.svg.axis().scale(xScale).orient("left").tickValues(tmp);

    var xAxis = d3.svg.axis().scale(xScale).orient("left").ticks(8);
    s.append('g').call(xAxis).attr({
        'fill':'none',
        'stroke':'#000',
        'transform':'translate('+ padding +',0)' 
       }).selectAll('text')
       .attr({
        'fill':'#000',
        'stroke':'none',
       }).style({
        'font-size':'18px'
       });
  //#######################以下畫哪家vender的說明#############################
  	for(i=0;i<vender_exist.length;i++){
		s.append('line').attr('x1', max_w+padding+60).attr('y1', 100+30*i)
				 		.attr('x2', max_w+padding+40).attr('y2',100+30*i)
				 		.style('stroke',vender_color(vender_exist[i])).style('stroke-width',15);
		s.append('g').selectAll('text')
		  .data(vender_exist).enter().append('text')
		  .attr({'x':max_w+padding+70 ,'y':105+30*i ,}).text(vender_exist[i])
		  .style('fill',vender_color(vender_exist[i]))
		  .style({'font-size':'18px'});
  	}
    //########### 直接畫x,y軸 ########################
     s.append('line').attr('x1', padding).attr('y1', max_h-padding)
     	 			 .attr('x2', max_w+padding*4 / 3).attr('y2', max_h-padding)
     	 			 .style('stroke', 'black').style('stroke-width', 2);
     
     s.append('line').attr('x1', padding).attr('y1',padding)
		 			 .attr('x2', padding).attr('y2', max_h-padding)
					 .style('stroke', 'black').style('stroke-width', 3);
 };
 
 
function date_format(str) {
	var words=str.replace(","," ").split(" ");
	return words[3]+"-"+words[0].replace("一月","1").replace("二月","2").replace("三月","3").replace("四月","4").replace("五月","5").replace("六月","6").replace("七月","7").replace("八月","8").replace("九月","9").replace("十月","10").replace("十一月","11").replace("十二月","12")+"-"+words[1];
}
	$(function() {
		$(".bdyplane").animate({"opacity":"1"});
		$("#searh-productunit").click(function(e) {
			$(".visualize").animate({"opacity":"0"});
			e.preventDefault();
			$.ajax({
				type : "POST",
				url : "salechart.do",
				data : {action :"searh", time1 : $('#datepicker1').val(), time2 : $('#datepicker2').val()},
				success : function(result) {
					//console.log(result);
					var json_obj = $.parseJSON(result);
					var result_table = "";
					var chart_data=[],chart_obj={},i=0,j=0,list=[];
					var tmp_month=json_obj.month[0];
					
					if(json_obj.entrance.length==0){
						$("#chart").html("<h2 style='color:red;'>查無資料</h2>");
						return;
					}
				
					$.each(json_obj.entrance,function(i, item) {
						var k=0;
						while(list[k]!=item && list[k]!=null){k++;}
						if(k==list.length)list[k]=item;
					});
					
					
					//chart_obj["month"]=json_obj.month[0];//init
					
					for(i=0;i<json_obj.entrance.length;i++){
						if(json_obj.month[i]!=tmp_month){ //in&
							tmp_month=json_obj.month[i];
							chart_data[j]=chart_obj;
							chart_obj={};
							j++;
						}
						chart_obj["month"]=json_obj.month[i];
						chart_obj[json_obj.entrance[i]]=json_obj.answer[i];
					}
					chart_data[j]=chart_obj;
					j++;
					
					draw_barchart(chart_data,list);
					$(".visualize").animate({"opacity":"1"});
// 					var data=[];
// 					var i=0,tmp_month=json_obj.month[0];
// 					for(i=0,j=0;i<json_obj.entrance.length;i++,j++){
// 						if(json_obj.entrance[i]!=0){
// 							if(i==0||json_obj.month[i]!=tmp_month){
// 								tmp_month=json_obj.month[i];
// 								if(i!=0){data[j]={x:j,month:"",y:"",vender:""};j++;}
// 								data[j]={x:j,month:"  "+json_obj.month[i]+"月",y:json_obj.answer[i],vender:json_obj.entrance[i]};
// 							}else{
// 								data[j]={x:j,month:"",y:json_obj.answer[i],vender:json_obj.entrance[i]};
// 							}
// 						}
// 					}
// 					if(data.length!=0){
// 						draw_chart(400,200,data);
// 						$("#chart").animate({"opacity":"0.5"});
// 						$("#chart").animate({"opacity":"1"});
// 					}else{
// 						$("#chart").html("<h2 style='color:red;'>查無資料</h2>");
// 					}
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
<div style="margin:30px auto;width:800px;"><div id="chart" style="opacity:0;"></div></div>
<div class="validateTips" align="center"> </div>
</div>
</div>
</body>
</html>