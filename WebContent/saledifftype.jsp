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

function draw_barchart(data,title){
	var i,j,maxdata=0;
	for(i=0;i<data.length;i++){
		if(data[i].quantity>maxdata){maxdata=data[i].quantity;}
		//alert(i+"th: "+data[i].name+" && "+data[i].quantity);
	}
	
	scale= 500 / (( maxdata * 21) / 20 );
	var leftpad=260,toppad=80;
// 	if(title.indexOf("!!!")<0){
// 		scale= 600 / (( maxdata * 21) / 20 );
// 		leftpad=160
// 	}
    var s = d3.select('#board').append('svg')
              .attr({
                'width':800,
                'height':data.length*25+100
              }).style({
                'border':'1px solid #000',
                'background-color' : '#E0E8F0'
              });
    var rect = s.append('g')
                .attr({
                  'id':'rect'
                });
    var num = s.append('g')
                .attr({
                  'id':'num'
                });
    var date = s.append('g')
                .attr({
                  'id':'date'
                });
    var date2 = s.append('g')
			    .attr({
			      'id':'date2'
			    });
//長方形
    rect.selectAll('rect')
      .data(data)
      .enter()
      .append('rect')
      .attr({
        'width':0,
        'height':10,
        'fill': '#9d0',
        'x':leftpad,
        'y':function(d){
          return d.x*25+toppad;
        }
      })
      .transition()
      .duration(2000)
      .attr({
        'width':function(d){
          return d.quantity * scale;
        },
        'fill':function(d){
          if(d.quantity>(maxdata * 4 / 7)){
            return '#c00';
          }else if(d.quantity>(maxdata * 3 / 7)&& d.quantity <=(maxdata * 4 / 7)){
            return '#f90';
          }else if(d.quantity>(maxdata * 2 / 7)&& d.quantity <=(maxdata * 3 / 7)){
            return '#ec0';
          }else if(d.quantity>(maxdata * 1 / 7)&& d.quantity <=(maxdata * 2 / 7)){
            return '#9d0';
          }else{
            return '#ae0';
          }
        }
      });
//title
	s.append('g').selectAll('text')
	  .data("t").enter().append('text')
	  .attr({'fill':'#222','x':180 ,'y':50 }).text(title)
	  .style({'font-size':'32px','font-family':'Microsoft JhengHei'});
//數量
    num.selectAll('text')
      .data(data)
      .enter()
      .append('text')
      .style({
        'font-size':'12px'
      })
      .attr({
        'fill':'#000',
        'x':leftpad+10,
        'y':function(d){
          return d.x * 25 + toppad+10;
        }
      })
      .transition()
      .duration(2000)
      .attr({
        'x':function(d){
          return d.quantity*scale+leftpad+10;
        }
      }).tween('number',function(d){
          var i = d3.interpolateRound(0, d.quantity);
          return function(t) {
          this.textContent = i(t);
        };
     });
  //商品前面的數字加星號
    date.selectAll('text')
      .data(data)
      .enter()
      .append('text')
      .attr({
   	  	'fill':function(d){
         	return (d.x<3?"#FF0000":"#000")
         },
        'text-anchor': 'end',
        'x':leftpad-210,
        'y':function(d){
          return d.x * 25 + toppad+10;
        }
      }).text(function(d){
        return (d.x<3?"★ ":"")+(d.x+1)+". ";
      }).style({
        'font-size':'16px',
      });
  //商品名字
    date2.selectAll('text')
    .data(data)
    .enter()
    .append('text')
    .attr({
 	  	'fill':function(d){
       	return (d.x<3?"#FF0000":"#000")
       },
      'text-anchor': 'start',
      'textLength':"190",
     // 'lengthAdjust':'spacing',
      'x':leftpad-200,
      'y':function(d){
        return d.x * 25 + toppad+10;
      }
    }).text(function(d){
      return (d.name.length>13?(grows_up(d.name.substring(0,12))+"..."):grows_up(d.name));
    }).style({
      'font-size':'16px',
    });
        
}
	$(function() {
		$(".bdyplane").animate({"opacity":"1"});
		order_source_auto("order_source");
		$(".searhbestsale").click(function(e) {
			
// 			$("#order_source").removeClass("error");
// 			if($("#order_source").val().length<1){
// 				$("#order_source").attr("placeholder","此為必填欄位");
// 				$("#order_source").addClass("error");
// 				return;
// 			}
			
			$("#board").css({"opacity":"0"});
			e.preventDefault();
			$.ajax({
				type : "POST",
				url : "saledifftype.do",
				data : {action :"search_best_sale",ordersource: $("#order_source").val() ,time1 : $('#datepicker1').val(), time2 : $('#datepicker2').val()},
				success : function(result) {
					//alert(result);
					$("#board").html('');
					var json_obj = $.parseJSON(result);
					var chart_data=[],chart_obj={},j=0;
					if(json_obj.length==0){
						if($("#order_source").val().length>0){
							warning_msg('---此平台查無類別---');
						}else{
							warning_msg('---全平台查無類別---');
						}
						return;
					}else{warning_msg("");}
					$.each (json_obj, function (i) {
						chart_obj={}
						chart_obj["x"]=j;
						chart_obj["quantity"]=json_obj[i].quantity;//(10-i)*30;
						chart_obj["name"]=json_obj[i].product_name+(json_obj[i].order_source!=null?"("+json_obj[i].order_source+")":"");
						chart_data[j]=chart_obj;
						j++;
					});
					if($("#order_source").val().length>0){
						draw_barchart(chart_data,$("#order_source").val()+"平台 暢銷類別");
					}else{
						draw_barchart(chart_data,"全平台暢銷類別統計排行榜!!!");
					}
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
							<span class="block-label">銷售平台<font color=brown>(選填)</font></span>
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
</div>
</div>
</body>
</html>