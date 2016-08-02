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
<script src="js/d3.v3.min.js"></script>

<script>
function color(vender){
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
	var s = d3.select('#chart').append('svg').attr({'width':max_w+200, 'height':max_h+100});
    s.selectAll('rect').data(data).enter().append('rect')
     .attr({
      'fill':function(d){
    	  return color(d.vender);
      },
      'width':(max_w/(data.length+1))-0,
      'height':function(d){
    	  //alert("d.y= "+d.y+" max_h= "+max_h+" max_data= "+max_data);
      	  return (d.y * max_h * 7 / 11 ) / max_data;
      },
      'x':function(d){return ((d.x) *(max_w / (data.length+1)))+padding;},
      'y':function(d){return max_h - ((d.y*max_h *7 / 11 ) / max_data)- padding;}
     });
  //#############數量文字和vender######################
    s.append('g').attr({'id':'num'}).selectAll('text')
    .data(data).enter().append('text')
    .attr({
      'fill':'#000',
      'x':function(d){return ((d.x) *(max_w/(data.length+1)))+padding;},
      'y':function(d){return max_h - ((d.y*max_h *7 / 11) / max_data)- padding/2 - 25;}
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
      'x':function(d){return ((d.x) * (max_w / (data.length+1))+(padding))+10;},
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
	  .attr({'x':max_w+padding ,'y':max_h-padding*4/5 }).text("月份")
	  .style({'font-size':'28px'});
	

  //##############Y軸######################
    var tmp=[];
    for(var i=0;i*50<max_data;i++){tmp[i]=i*50;}
    var xScale = d3.scale.linear().range([padding,max_h-padding]).domain([max_data, 0]);;  
    var xAxis = d3.svg.axis().scale(xScale).orient("left").tickValues(tmp);
    s.append('g').call(xAxis).attr({
        'fill':'none',
        'stroke':'#000',
        'transform':'translate('+ padding*2/3 +',0)' 
       }).selectAll('text')
       .attr({
        'fill':'#000',
        'stroke':'none',
       }).style({
        'font-size':'22px'
       });
  //#######################以下畫哪家vender的說明#############################
  	for(i=0;i<vender_exist.length;i++){
		s.append('line').attr('x1', max_w+60).attr('y1', 100+30*i)
				 			  .attr('x2', max_w+40).attr('y2',100+30*i)
				 			  .style('stroke',color(vender_exist[i])).style('stroke-width',15);
		s.append('g').selectAll('text')
		  .data(vender_exist).enter().append('text')
		  .attr({'x':max_w+70 ,'y':105+30*i ,}).text(vender_exist[i])
		  .style('fill',color(vender_exist[i]))
		  .style({'font-size':'18px'});
  	}
    //########### 直接畫x,y軸 ########################
     s.append('line').attr('x1', padding*2/3).attr('y1', max_h-padding)
     	 			 .attr('x2', max_w+50).attr('y2', max_h-padding)
     	 			 .style('stroke', 'black').style('stroke-width', 2);
     
     s.append('line').attr('x1', padding*2/3).attr('y1',padding)
		 			 .attr('x2', padding*2/3).attr('y2', max_h-padding)
					 .style('stroke', 'black').style('stroke-width', 3);
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
				url : "salechart.do",
				data : {action :"searh",time1 : $('#datepicker1').val(),time2 : $('#datepicker2').val()},
				success : function(result) {
					var json_obj = $.parseJSON(result);
					var result_table = "";
					var data=[];
					var i=0,tmp_month=json_obj.month[0];
					//alert(json_obj.length);
					//$.each(json_obj,function(i, item) {}
					//alert(result);
					//alert("夢幻第七人: "+json_obj.month[7]);
					//draw_chart(400,200,json_obj);
					//$(".validateTips").text("長條圖維護中，請洽管理員");
					for(i=0,j=0;i<json_obj.entrance.length;i++,j++){
						if(json_obj.entrance[i]!=0){
							if(i==0||json_obj.month[i]!=tmp_month){
								tmp_month=json_obj.month[i];
								//alert("進來的i: "+i);
								if(i!=0){data[j]={x:j,month:"",y:"",vender:""};j++;}
								data[j]={x:j,month:"  "+json_obj.month[i]+"月",y:json_obj.answer[i],vender:json_obj.entrance[i]};
							}else{
								data[j]={x:j,month:"",y:json_obj.answer[i],vender:json_obj.entrance[i]};
							}
						}
					}
					if(data.length!=0){
						draw_chart(400,200,data);
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
	function export_xls(){
		$(".result").table2excel({
			exclude: ".noExl",
			name: "Excel Document Name",
			filename: "訂單資料",
			fileext: ".xls",
			exclude_img: true,
			exclude_links: true,
			exclude_inputs: true
		});
	}
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
		<h2 style="font-size: 25px;">出貨量統計圖</h2>
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
				<div class="validateTips" align="center"> </div>
				<div id="chart" align="center"></div>
			</div>
		</div>
	</div>
<div id="dialog-confirm"></div>

</div>
</body>
</html>