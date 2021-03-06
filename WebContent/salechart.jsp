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
<style>
        .axis path, .axis line{
            fill: none;
            stroke: black;
            shape-rendering: auto;
        }
        .axis text{
            font-size: 12px;
        }
        #tooltip{
            position: absolute;
            background: #eee;
            width: 150px;
            height: auto;
            padding: 0px 10px;
            border-radius: 10px;
            box-shadow: 5px 5px 10px rgba(0,0,0,0.3);
        }
        #tooltip.hidden{
            display: none;
        }
    </style>
</head>
<body>
	<jsp:include page="template.jsp" flush="true"/>
	<div class="content-wrap">
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
function dataformat(dataSet){
	var linearr=[];
	var aline=[];
	var current_order_source;
	if(dataSet[0]!=null){ current_order_source=dataSet[0].ordersource; }
	var aline_i=0;
	var linearr_i=0;
	$.each(dataSet,function(i, item) {
		//alert(item.month);
		if(dataSet[i].ordersource!=current_order_source){
			linearr[linearr_i] = aline;
			aline=[];
			linearr_i++;
			aline_i=0;
			//###########################
			current_order_source=dataSet[i].ordersource;
			aline[aline_i]=item;
			aline_i++;
		}else{
			aline[aline_i]=item;
			aline_i++;
		}
// 		if(dataSet.length==i+1){
// 		}
		
		//alert(i+" "+aline_i+" "+linearr_i);
		//linearr
		//aline
		//dataSet[i]
	});
	//alert(linearr[0][0]);
	return linearr;
}
function svg(){
    d3.select("#board").append("svg").attr({
        width: 900,
        height: 360
    });
    d3.select("svg").append("g").append("rect").attr({
        width: "100%",
        height: "100%",
        fill: "white"
    });
    d3.select("svg").append("g").attr("id","axisX");
    d3.select("svg").append("g").attr("id","axisY");
}


function return_to_d3js(data,ori_dataSet){
	var w=900;
	var h=360;
	var padding = 80;
	var xScale = d3.time.scale()
	    .domain([new Date(d3.min(ori_dataSet, function(d){
	        return new Date(d.month);
	    })),new Date(d3.max(ori_dataSet, function(d){
	        return new Date(d.month);
	    }))])
	    .range([padding,w-padding]);
	
	var yScale = d3.scale.linear()
	    .domain([0,d3.max(ori_dataSet, function(d){
	        return +d.amount;
	    })])
	    .range([h-padding,padding]);
	var xAxis = d3.svg.axis().scale(xScale).orient("bottom");
	var yAxis = d3.svg.axis().scale(yScale).orient("left");
	
	d3.select("svg")
	    .select("g#axisY")
	    .attr("class","axis")
	    .attr("transform", "translate("+(padding)+",0)")
	    .call(yAxis);
	d3.select("svg")
	    .select("g#axisX")
	    .attr("class","axis")
	    .attr("transform", "translate(0,"+(h-padding)+")")
	    .call(xAxis);

    
    
    
    
    
    
    
	
	
	
	console.log(JSON.stringify(data));
}
function bind(dataSet){
    var selection = d3.select("svg")
                        .selectAll("circle")
                        .data(dataSet);
    selection.enter().append("circle");
    selection.exit().remove();
}
function render(dataSet,ori_dataSet){
    //比例尺們 xScale, yScale, rScale, fScale
     var xScale = d3.time.scale()
              .domain([new Date(d3.min(dataSet, function(d){
                  return new Date(d.date);
              })),new Date(d3.max(dataSet, function(d){
                  return new Date(d.date);
              }))])
              .range([padding,w-padding]);
     var yScale = d3.scale.linear()
              .domain([0,d3.max(dataSet, function(d){
                  return +d.number;
              })])
              .range([h-padding,padding]);
     var rScale = d3.scale.linear()
              .domain([d3.min(dataSet, function(d){
                  return +d.amount;
              }),d3.max(dataSet, function(d){
                  return +d.amount;
              })])
              .range([5,20]);
     var fScale = d3.scale.category20();
      
     //開始畫圈圈
     d3.selectAll("circle")
      .transition()
      .attr({
          cx: function(d){
            return xScale(new Date(d.date)); 
          },
          cy:function(d){
            return yScale(+d.number); 
          },
          r:function(d){
            return rScale(+d.amount); 
          },
          fill: function(d){
            return fScale(letterList.indexOf(d.cid));
          }
      });
//         .append("title").text(function(d){
//         return d.city+"\r\n"+d.industry+"\r\n發票金額:"+d.amount;
//          })
     d3.selectAll("circle")
      .on("mouseover",function(d){
         cX=d3.select(this).attr("cx");
         cY=d3.select(this).attr("cy");
         var tooltip = d3.select("#tooltip")
          .style({
              left: (+cX+20)+"px",
              top: (+cY+20)+"px"
          });
          //替換tooltip內容(選擇其id後,修改內容)
          d3.select("#tooltip").select("#city").text(d.city);
          d3.select("#tooltip").select("#industry").text(d.industry);
          d3.select("#tooltip").classed("hidden",false);
     }).on("mouseout",function(d){
          d3.select("#tooltip").classed("hidden",true);
     });
      
     //開始畫x,y軸線 
      var xAxis = d3.svg.axis().scale(xScale).orient("bottom");
      var yAxis = d3.svg.axis().scale(yScale).orient("left");
      d3.select("svg")
          .select("g#axisY")
          .attr("class","axis")
          .attr("transform", "translate("+(padding-10)+",0)")
          .call(yAxis);
      d3.select("svg")
          .select("g#axisX")
          .attr("class","axis")
          .attr("transform", "translate(0,"+(h-padding+10)+")")
          .call(xAxis);
  }











function draw_barchart__plotly(data,list){
// 	data = [ {
// 		  x: ['giraffes', 'orangutans', 'monkeys'], 
// 		  y: [20, 14, 23], 
// 		  name: 'SF Zoo', 
// 		  type: 'bar'
// 		}, {
// 		  x: ['giraffes', 'orangutans', 'monkeys'], 
// 		  y: [12, 18, 29], 
// 		  name: 'LA Zoo', 
// 		  type: 'bar'
// 		}];
// 	alert(data[0].y[1]);
	
	var layout = {
	  title: '出貨量統計圖',
	  titlefont: {
	        family: "Microsoft JhengHei",
	        size: 32,
	        color: '#000'
	  },
	  xaxis: {
	    title: '月份',
	    titlefont: {
	        family: "Microsoft JhengHei",
	        size: 16,
	        color: '#7f7f7f'
	      },
	    showgrid: false,
	    zeroline: false
	  },
	  yaxis: {
	    title: '數量',
	    titlefont: {
	        family: "Microsoft JhengHei",
	        size: 16,
	        color: '#7f7f7f'
	    },
	    showline: true
	  },
	  paper_bgcolor: '#E0E8F0',
	  plot_bgcolor: '#E0E8F0',
	  bargap: 0.15,
	  bargroupgap: 0.1
	};
	
	Plotly.newPlot('chart', data,layout);
	$(".ytitle").attr("transform","rotate(0,42,235) translate(-10, 0)");
	$("#chart").animate({opacity: '1'});
}


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
		'barMargin':'2','barGroupMargin':'15',
		'width':((data.length*150)>1000?"1000":data.length*150),'height':'300',
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
 
	$(function() {
		svg();
		$(".bdyplane").animate({"opacity":"1"});
		$("#searh-productunit").click(function(e) {
			$(".visualize").animate({"opacity":"0"});
			$("#chart").animate({opacity: '0'});
			e.preventDefault();
// 			warning_msg("---讀取中請稍候---");
			$.ajax({
				type : "POST",
				url : "salechart.do",
				data : {action :"searh", time1 : $('#datepicker1').val(), time2 : $('#datepicker2').val()},
				success : function(result) {
					
					//console.log(result);
					var json_obj = $.parseJSON(result);
					var linearr = dataformat(json_obj);
					
					return_to_d3js(linearr,json_obj);
					$(".visualize").animate({"opacity":"1"});
					return ;
					var json_obj = $.parseJSON(result);
					var result_table = "";
					var chart_data=[],chart_obj={},i=0,j=0,list=[];
					var tmp_layer=json_obj.entrance[0];
					
					if(json_obj.entrance.length==0){
						warning_msg("---查無資料---");
// 						$(".validateTips").html("<h2 style='color:red;'>查無資料</h2>");
						return;
					}
					warning_msg(" ");
					var max_m=0,min_m=13;
					$.each(json_obj.month,function(i, item) {
						if(item>max_m){max_m=item;}
						if(item<min_m){min_m=item;}
					});
					for(i=min_m;i<max_m+1;i++){
						list[i-min_m]=i+"月";
					}
					
					
					chart_obj["x"]=[];
					chart_obj["y"]=[];
					$.each(list,function(i, item) {
						var k=0;
						chart_obj["x"][i]=list[i];
						chart_obj["y"][i]=0;
					});
					
					//chart_obj["month"]=json_obj.month[0];//init
					var k=0;
					for(i=0;i<json_obj.entrance.length;i++){
						if(json_obj.entrance[i]!=tmp_layer){ //in&
							tmp_layer=json_obj.entrance[i];
							chart_data[j]=chart_obj;
							chart_obj={};
							chart_obj["x"]=[];
							chart_obj["y"]=[];
							$.each(list,function(i, item) {
								var k=0;
								chart_obj["x"][i]=list[i];
								chart_obj["y"][i]=0;
							});
							j++;
							k=0;
						}
						//chart_obj["month"]=json_obj.month[i];
						//chart_obj[json_obj.entrance[i]]=json_obj.answer[i];
						//alert(json_obj.month[i]+json_obj.entrance[i]+json_obj.answer[i]+" "+k);
						//alert(chart_obj["x"][0]);
						while(chart_obj["x"][k]!=(json_obj.month[i]+"月")&&k<13){k++;}
						//alert(k+"  "+chart_obj["x"][k]+"  "+json_obj.month[i]+"月");
						chart_obj["y"][k]=json_obj.answer[i];
						
						chart_obj["marker"]={};
						chart_obj["marker"]["color"]=vender_color(json_obj.entrance[i]);
						//marker: {color: 'rgb(55, 83, 109)'}, 
						chart_obj["name"]=json_obj.entrance[i];
						chart_obj["type"]='bar';
						k++;
						
					}
					chart_data[j]=chart_obj;
					j++;
					$("#board").css({"width":(list.length*150>1000?"1100":list.length*150+100)});
					draw_barchart__plotly(chart_data,list);
// 					draw_barchart(chart_data,list);
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
	<div style="color:red;margin:10px auto;text-align:center;font-size:32px;">為了提供您更好的使用品質，該功能維護中。</div>
	
<div id="board" style="margin:10px auto;width:1000px;"><div id="chart" style="opacity:0;"></div></div>

</div>
</div>
<script type="text/javascript" src="js/plotly-latest.min.js"></script>
</body>
</html>