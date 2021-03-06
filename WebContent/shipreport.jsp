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
<title>出貨報表</title>
<meta charset="utf-8">
<link rel="Shortcut Icon" type="image/x-icon" href="./images/Rockettheme-Ecommerce-Shop.ico" />
<link rel="stylesheet" href="css/styles.css" />
<link rel="stylesheet" href="css/jquery.dataTables.min.css" />
<link rel="stylesheet" href="css/buttons.dataTables.min.css" />
<link href="<c:url value="css/css.css" />" rel="stylesheet">
<link href="<c:url value="css/jquery.dataTables.min.css" />" rel="stylesheet">
<link href="<c:url value="css/1.11.4/jquery-ui.css" />" rel="stylesheet">
</head>
<body>
	<jsp:include page="template.jsp" flush="true"/>
	<div class="content-wrap" >
		<div class='bdyplane' style="opacity:0">
		
<script type="text/javascript" src="js/jquery-1.12.4.js"></script>
<script type="text/javascript" src="js/jquery.dataTables.min.js"></script>
<script type="text/javascript" src="js/dataTables.buttons.min.js"></script>
<script type="text/javascript" src="js/buttons.flash.min.js"></script>
<script type="text/javascript" src="js/jszip.min.js"></script>
<script type="text/javascript" src="js/pdfmake.min.js"></script>
<script type="text/javascript" src="js/vfs_fonts.js"></script>
<script type="text/javascript" src="js/buttons.html5.min.js"></script>
<script type="text/javascript" src="js/buttons.print.min.js"></script>
<script type="text/javascript" src="js/jquery-ui.js"></script>
<script type="text/javascript" src="js/jquery-migrate-1.4.1.min.js"></script>
<script type="text/javascript" src="js/jquery.validate.min.js"></script>
<script type="text/javascript" src="js/additional-methods.min.js"></script>
<script type="text/javascript" src="js/messages_zh_TW.min.js"></script>
<script type="text/javascript" src="js/jquery.table2excel.js"></script>

<script type="text/javascript" src="js/dataTables.buttons.min.js"></script>
<script type="text/javascript" src="js/buttons.jqueryui.min.js"></script>
<script>
var do_what="searh";
function ship_data(){
	$(".validateTips").text("請稍候片刻");
	
	$.ajax({
		type : "POST",
		url : "shipreport.do",
		data : {action :do_what,time1 : $('#datepicker1').val(),time2 : $('#datepicker2').val()},
		success : function(result) {
			//alert(result);
			if(result.indexOf("WebService")!=-1){
				$(".validateTips").text("WebService Error: "+result);
				return;
			};
			if(result.indexOf("not found")!=-1){
				$(".validateTips").text("網路不穩定，請稍候再試。");
				//$(".validateTips").text("查無資料:請確認客戶資料及銷貨資料");
				return;
			}
			var tmp = setTimeout( function() {alert("發生未知異常錯誤，請聯絡管理員。")}, 200);
			var json_obj = $.parseJSON(result);
			clearTimeout(tmp);
			var result_table = "";
			$.each(json_obj,function(i, item) {
				result_table += "<tr><td>"+ json_obj[i].order_no 
				+ "</td><td>" + (json_obj[i].c_product_id==null?"":json_obj[i].c_product_id.replace("NULL",""))
				+ "</td><td>" + (json_obj[i].sale_date==null?"":json_obj[i].sale_date.replace("T00:00:00Z",""))
// 				+ "</td><td>" + (json_obj[i].dis_date==null?"":json_obj[i].dis_date.replace("T00:00:00Z",""))
				+ "</td><td>" + money(json_obj[i].price)
				+ "</td><td>" + ((json_obj[i].phone==null)?"":json_obj[i].phone)
				+ "</td><td>" + ((json_obj[i].phone==json_obj[i].address)?"":json_obj[i].address)
				+ "</td><td>" + ((json_obj[i].post==null)?"":json_obj[i].post.replace("NULL","").replace("null",""))
				+ "</td><td>" + json_obj[i].name
				+ "</td><td>" + json_obj[i].mobile
				+ "</td><td>" + json_obj[i].product_name
				+ "</td><td>" + json_obj[i].seq_no
				+ "</td><td>" + json_obj[i].quantity
				+ "</td><td>" + json_obj[i].order_source
				+ "</td><td>" + json_obj[i].memo//((json_obj[i].memo==null)?"":json_obj[i].memo.replace("NULL",""))
				+"</td></tr>";
			});
			$("#products").dataTable().fnDestroy();
			if(json_obj.length!=0){
				$("#products-contain").show();
				$("#products").dataTable().fnDestroy();
				$("#products tbody").html(result_table);
				draw_table("products","出貨報表");
				$("#products").find("td").css({"word-break":"break-all","min-width":"50px","text-align":"center" });
				$(".validateTips").text("");
			}else{
				$("#products-contain").hide();
				$(".validateTips").text("查無此結果");
			}
		}
	});
}

function open_report(param){
	
	$("<div></div>")
	.attr("align", "center")
	.dialog({
	    title: "報表",
	    modal: true,
	    width: "auto",
	    open: function(event, ui) {
	    	var iframeUrl="./shipreport.do?" + param;
	        $(this).parent().children().children('.ui-dialog-titlebar-close').hide();
	        $iframe = $("<iframe></iframe>")
	        	.attr("width", 850)
	        	.attr("height", 450)
	        	.attr("src", iframeUrl);
	        $(this).html( $iframe );
	    },
	    buttons: [{
	        text: "確認",
	        click: function() {
	        	$(this).dialog("close");
	        }
	    }]
	});
	
}

function download_report(param){
	
	$("<div></div>")
	.dialog({
	    title: "下載",
	    modal: true,
	    open: function(event, ui) {
	    	var iframeUrl="./shipreport.do?" + param; 
	    	$(this).parent().children().children('.ui-dialog-titlebar-close').hide();
	        $iframe = $("<iframe></iframe>")
	        	.attr("src", iframeUrl);
	        $(this).html( $iframe );
	        $(this).dialog("close");
	    }
	});
	
}

	$(function() {
		$(".bdyplane").animate({"opacity":"1"});
		var value='<%=request.getParameter("action")%>';
		if(value=="today"){
			do_what="today";
			$('#datepicker1').datepicker('setDate',new Date());
			$('#datepicker2').datepicker('setDate',new Date());
			ship_data();
		}
		if(value=="upload"){
			$('#datepicker1').datepicker('setDate',new Date());
			$('#datepicker2').datepicker('setDate',new Date());
		}
		
		//查詢相關設定
		$("#searh-productunit").click(function(e) {
			e.preventDefault();
			var param = "action=ship&kind=pdf&time1=" + $('#datepicker1').val() + "&time2=" + $('#datepicker2').val();
			open_report(param);
		});
		
		//下載
		$("#download-report").click(function(e) {
			e.preventDefault();
			var param = "action=ship&kind=xls&time1=" + $('#datepicker1').val() + "&time2=" + $('#datepicker2').val();
			download_report(param);
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
						<span class="block-label">出貨起日</span>
						<input type="text" class="input-date" id="datepicker1">
					</label>
					<div class="forward-mark"></div>
					<label for="">
						<span class="block-label">出貨迄日</span>
						<input type="text" class="input-date" id="datepicker2">
					</label>
					<a class="btn btn-darkblue" id="searh-productunit">查詢</a>
					<a class="btn btn-darkblue" id="download-report">下載</a>
				</div>
			</div><!-- /.form-wrap -->
		</div>
			<!-- 第二列 -->
			<div class="search-result-wrap" >
				<div id="products-contain" class="result-table-wrap" style="display:none;" >
					<table id="products" class="result-table">
						<thead>
							<tr class="ui-widget-header">
								<th>出貨單號</th>
								<th>自訂商品ID</th>
								<th>出貨日期</th>
<!-- 								<th>配送日期</th> -->
								<th style="min-width:70px;">價格</th>
								<th>電話</th>
								<th style="min-width:100px">地址</th>
								<th>郵編</th>
								<th>顧客姓名</th>
								<th>手機</th>
								<th style="min-width:140px">商品名稱</th>
								<th>銷貨單號</th>
								<th>數量</th>	
								<th>銷貨平台</th>
								<th>訂單備註</th>		
							</tr>
						</thead>
						<tbody>
						</tbody>
					</table>
				</div>
				<div class="validateTips" align="center"> </div>
			</div>
		</div>
	</div>
</body>
</html>