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
</head>
<body>
	<jsp:include page="template.jsp" flush="true"/>
	<div class="content-wrap" style="margin:56px 0px 28px 120px;">
<script type="text/javascript" src="js/jquery-1.10.2.js"></script>
<script type="text/javascript" src="js/jquery-1.11.4.js"></script>
<script type="text/javascript" src="js/jquery.dataTables.min.js"></script>
<script type="text/javascript" src="js/jquery-ui.min.js"></script>
<script type="text/javascript" src="js/jquery-migrate-1.4.1.min.js"></script>
<script type="text/javascript" src="js/jquery.validate.min.js"></script>
<script type="text/javascript" src="js/additional-methods.min.js"></script>
<script type="text/javascript" src="js/messages_zh_TW.min.js"></script>

<script src="js/d3.v3.min.js"></script>


<script>
function getYearWeek(a, b, c) { 
/* date1是当前日期 date2是当年第一天 d是当前日期是今年第多少天 \用d + 当前年的第一天的周差距的和在除以7就是本年第几周 */ 
	var date1 = new Date(a, parseInt(b) - 1, c), date2 = new Date(a, 0, 1), 
	d = Math.round((date1.valueOf() - date2.valueOf()) / 86400000); 
	return Math.ceil((d + ((date2.getDay() + 1) - 1)) / 7 ); 
}; 

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
	max_w = (max_month-min_month+2)*100;
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
	var data2 = [ {"sale": "20020","year": "6"}, {"sale": "82004","year":"7" }];
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
		       .attr('stroke-width',2).attr('fill', 'none');
			
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
	      'x':function(d){return ((padding*5)/2)+(d.x*((max_w-padding*3) /(max_month-min_month+2)));},
	      'y':function(d){return max_h-padding/2;}
	    }).text(function(d){return d.date;})
	    .style({'font-size':'28px'})
	    .style('fill', function(d){return ((d.x%2==0)?'#000000':'#555555')});
     vis.append('line') .attr('x1', padding*2-5    ).attr('y1', max_h-padding)
		 				.attr('x2', max_w-padding-5).attr('y2', max_h-padding)
		 				.style('stroke', 'black').style('stroke-width', 5);
 };
 
 
function date_format(str) {
	var words=str.replace(","," ").split(" ");
	return words[3]+"-"+words[0].replace("一月","1").replace("二月","2").replace("三月","3").replace("四月","4").replace("五月","5").replace("六月","6").replace("七月","7").replace("八月","8").replace("九月","9").replace("十月","10").replace("十一月","11").replace("十二月","12")+"-"+words[1];
}
	$(function() {
		$("#searh-productunit").click(function(e) {
			$("#chart").html("<h2 style='color:red;'>資料查詢中...</h2>");
			e.preventDefault();
			$.ajax({
				type : "POST",
				url : "saleamountchart.do",
				data : {action :"searh",time1 : $('#datepicker1').val(),time2 : $('#datepicker2').val()},
				success : function(result) {
					//alert(result);
					
					var json_obj = $.parseJSON(result);
					var result_table = "";
					var data=[];
					var i=0;
					for(i=0;i<json_obj.entrance.length;i++){
						if(json_obj.entrance[i]!=0){
							data[i]={"sale":json_obj.answer[i],"year":json_obj.entrance[i],"vender":json_obj.vender[i] };
						}
					}
					if(data.length!=0){
						draw_chart(400,200,data);
						}else{
						$("#chart").html("<h2 style='color:red;'>查無資料</h2>");
					}
				}
			});
			//$("#xls").show();
		});
		$("#dialog-confirm").html("<p>是否確認刪....</p>");
		$("#dialog-confirm").dialog({
			title: "你妳你妳你",
			draggable : false,//防止拖曳
			resizable : false,//防止縮放
			autoOpen : false,
			height : "auto",
			modal : true,
			show : {effect : "blind",duration : 300},
 			hide : {effect : "blind",duration : 300},
			buttons : {
				"確認刪除" : function() {alert("嘿嘿嘿~");$(this).dialog("close");},
				"取消刪除" : function() {$(this).dialog("close");}
			}
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
							<span class="block-label" id="datepicker2">轉單迄日</span>
							<input type="text" class="input-date">
						</label>
						<button id="searh-productunit" class="btn btn-darkblue">查詢</button>
					</div>
				</div><!-- /.form-wrap -->
			</div><!-- /.input-field-wrap -->
			<!-- 第一列 -->
<!-- 			<div class="row" > -->
<!-- 				<div id="products-serah-create-contain" style="width: 800px;margin:0px auto;" > -->
<!-- 					<table id="products-serah-create" class="ui-widget ui-widget-content"> -->
<!-- 						<tr class="ui-widget-header"> -->
<!-- 							<th > -->
<!-- 								<p style="width:120px;">轉單日</p> -->
<!-- 							</th><th> -->
<!-- 								<input type="text" id="datepicker1" placeholder="起"> -->
<!-- 							</th><th> -->
<!-- 								~ -->
<!-- 							</th><th> -->
<!-- 								<input type="text" id="datepicker2" placeholder="迄"> -->
<!-- 							</th><th> -->
<!-- 								<button id="searh-productunit" onclick="" style="width:80px;">查詢</button> -->
<!-- 							</th> -->
<!-- 						</tr> -->
<!-- 					</table> -->
<!-- 				</div> -->
<!-- 			</div> -->
			<!-- 第二列 -->
<!-- 			<div class="row" align="left" > -->
<!-- 				<div id="products-contain" class="ui-widget"> -->
<!-- 					<table id="products" class="ui-widget ui-widget-content"> -->
<!-- 						<thead> -->
<!-- 							<tr class="ui-widget-header"> -->
<!-- 								<th>銷貨單號</th> -->
<!-- 								<th>訂單號</th> -->
<!-- 								<th><p style="width:320px;">產品名稱</p></th> -->
<!-- 								<th>客戶自訂產品ID</th> -->
<!-- 								<th><p style="width:80px;">銷貨數量</p></th> -->
<!-- 								<th><p style="width:80px;">銷貨金額</p></th> -->
<!-- 								<th><p style="width:120px;">轉單日</p></th> -->
<!-- 								<th><p style="width:120px;">配送日</p></th> -->
<!-- 								<th><p style="width:120px;">銷貨/出貨日期</p></th> -->
<!-- 								<th><p style="width:100px;">銷售平台</p></th> -->
<!-- 								<th>備註</th>								 -->
<!-- 							</tr> -->
<!-- 						</thead> -->
<!-- 						<tbody> -->
<!-- 						</tbody> -->
<!-- 					</table> -->
<!-- 				</div> -->
<!-- 			</div> -->
		</div>
	</div>
	<div class="validateTips" align="center"> </div>
	<div id="chart" align="center"></div>
	<div id="dialog-confirm"></div>
</div>
</body>
</html>