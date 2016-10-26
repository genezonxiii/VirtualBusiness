<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ page import="tw.com.aber.sale.controller.*"%>
<%@ page import="java.util.*"%>
<%@ page import="java.sql.DriverManager"%>
<%@ page import="java.sql.Connection"%>
<%@ page import="java.sql.Statement"%>
<%@ page import="java.sql.ResultSet"%>
<jsp:directive.page import="java.sql.SQLException" />
<!DOCTYPE html>
<html>
<head>
<title>開立發票</title>
<meta charset="utf-8">
<link rel="Shortcut Icon" type="image/x-icon" href="./images/Rockettheme-Ecommerce-Shop.ico" />
<link href="<c:url value="css/jquery.dataTables.min.css" />" rel="stylesheet">
<link href="<c:url value="css/1.11.4/jquery-ui.css" />" rel="stylesheet">
<link href="<c:url value="css/dataTables.jqueryui.min.css" />" rel="stylesheet">
<link href="<c:url value="css/buttons.jqueryui.min.css" />" rel="stylesheet">
<link rel="stylesheet" href="css/styles.css" />
<style type="text/css">
body { overflow-y: hidden; }
</style>
</head>
<body>
	<jsp:include page="template.jsp" flush="true"/>
	<div class="content-wrap" >
		<div class='bdyplane' style="opacity:0">
<script type="text/javascript" src="js/jquery-1.10.2.js"></script>
<script type="text/javascript" src="js/jquery.dataTables.min.js"></script>
<script type="text/javascript" src="js/jquery-ui.min.js"></script>
<script type="text/javascript" src="js/jquery-migrate-1.4.1.min.js"></script>
<script type="text/javascript" src="js/jquery.validate.min.js"></script>
<script type="text/javascript" src="js/additional-methods.min.js"></script>
<script type="text/javascript" src="js/messages_zh_TW.min.js"></script>
<!-- data table buttons -->
<script type="text/javascript" src="js/dataTables.buttons.min.js"></script>
<script type="text/javascript" src="js/buttons.jqueryui.min.js"></script>
<!-- <script src="js/typed.custom.js"></script> -->
	<script>
function draw_invoice(parameter){
//  	alert(parameter["invoice_date"]==null);
	var table="#invoice_false_table";
	var contain="#invoice_false_table_contain";
	var start="#invoice_false_start";
	var end="#invoice_false_end";
	if(parameter["invoice"]==true){
		table="#invoice_true_table";
		contain="#invoice_true_table_contain";
		start="#invoice_true_start";
		end="#invoice_true_end";
	}
// 	alert(!(parameter["async"]==false));
	$(contain).hide();
	$(contain).css({"opacity":"0"});
	if(parameter["invoice"]==true){warning_msg_last("---讀取中請稍候---");}else{warning_msg("---讀取中請稍候---");}
	if(($(start).val().length<3 &&$(end).val().length>3)||($(start).val().length>3 &&$(end).val().length<3) ){
		$(contain).hide();
		if(parameter["invoice"]==true){warning_msg_last("---如要以日期查詢，請完整填寫起訖日欄位---");}else{warning_msg("---如要以日期查詢，請完整填寫起訖日欄位---");}
	}else if($(start).val()>$(end).val()){
		$(contain).hide();
		if(parameter["invoice"]==true){warning_msg_last("---起日不可大於訖日---");}else{warning_msg("---起日不可大於訖日---");}
	}else{
		$.ajax({
			type : "POST",
			url : "invoice.do",
			data : parameter,
			async : (!(parameter["async"]==false)),
			success : function(result) {
				var json_obj = $.parseJSON(result);
				var len=json_obj.length;
				//判斷查詢結果
				var resultRunTime = 0;
				$.each (json_obj, function (i) {resultRunTime+=1;});
				if(resultRunTime!=0){
					
					var result_table = "";
					$.each(json_obj,function(i, item) {
// 						if(parameter["invoice_date"]!=null){
// 							alert(parameter["invoice_date"]);
// 							alert(json_obj[i].invoice_date);
//  							alert(parameter["invoice_date"]==json_obj[i].invoice_date);
// 						}
						if(parameter["invoice_date"]==null||parameter["invoice_date"]==json_obj[i].invoice_date){
							if(json_obj[i].seq_no==null){json_obj[i].seq_no="";}
							if(json_obj[i].order_no==null){json_obj[i].order_no="";}
							if(json_obj[i].product_name==null){json_obj[i].product_name="";}
							if(json_obj[i].price==null){json_obj[i].price="";}
							if(json_obj[i].invoice==null){json_obj[i].invoice="";}
							if(json_obj[i].invoice_date==null){json_obj[i].invoice_date="";}
							if(json_obj[i].sale_date==null){json_obj[i].sale_date="";}
							if(json_obj[i].order_source==null){json_obj[i].order_source="";}
	
							result_table 
							+= "<tr name='"+json_obj[i].sale_id+"'>"
							+ "<td name='"+ json_obj[i].seq_no +"'>"+ json_obj[i].seq_no+ "</td>"
							+ "<td name='"+ json_obj[i].order_no +"'>"+ json_obj[i].order_no+ "</td>"
							+ "<td name='"+ json_obj[i].product_name +"'>"+ json_obj[i].product_name+ "</td>"
							+ "<td name='"+ json_obj[i].price +"'>"+ money(json_obj[i].price)+ "</td>"
							+ "<td name='invoice'>"+ json_obj[i].invoice+ "</td>"
							+ "<td name='"+ json_obj[i].invoice_date +"'>"+ json_obj[i].invoice_date+ "</td>"
							+ "<td name='"+ json_obj[i].sale_date +"'>"+ json_obj[i].sale_date+ "</td>"
							+ "<td name='"+ json_obj[i].order_source +"'>"+ json_obj[i].order_source+ "</td>"
							+ "<td><input id='"+(parameter["invoice"]==true?"true":"false")+"-"+ i + "' type='checkbox' value='"+ json_obj[i].sale_id + "' val2='"+json_obj[i].order_no+"' val3='"+json_obj[i].invoice+"' class='"+(parameter["invoice"]==true?"true_checkbox":"false_checkbox")+"'></input><label for='"+(parameter["invoice"]==true?"true":"false")+"-"+ i +"'></label>"
							+"</td></tr>";
						}
					});
// 					$(table+" tbody").html(result_table);
				}
				
				$(table).dataTable().fnDestroy();
				if(resultRunTime!=0){
					$(table+" tbody").html(result_table);
					if(parameter["invoice"]==true){
						$(table).dataTable({
							autoWidth: false,
							scrollX:  true,
					        scrollY:"324px",
					        dom: 'Blfrtip',
					        buttons: [{
				                text: '作廢發票',
				                className: 'btn_receive',
				                action : function(e){
				            		e.preventDefault();
				            		var count = 0;
				            		var message = "<div style='padding:0 30px 10px; 30px;'>";
				            		$(".true_checkbox").each(function(j){
				            			if($(this).prop("checked")){
				            				if(count%3==0){message+="<br>";}else{message+="　　";}
				            				count+=1;
				            				message+="<font color=red>'"+$(this).attr("val3")+"'</font>";
				            			}
				            		});
				            		message+="</div>";
				            		$(".ui-dialog-title").html("是否要作廢 此"+ count + "筆 發票嗎?")
				            		$("#make_false_confirm").html(message);
				            		$("#make_false_confirm").dialog("open");
				                }
				         }], "language": {"url": "js/dataTables_zh-tw.txt"}});
					}else{
						$(table).dataTable({
							autoWidth: false,
							scrollX:  true,
					        scrollY:"324px",
					        dom: 'Blfrtip',
					        buttons: [{
				                text: '開立發票',
				                className: 'btn_receive',
				                action : function(e){
				            		e.preventDefault();
				            		var count = 0;
				            		var message = "<div style='padding:0 30px 10px; 30px;'>";
				            		$(".false_checkbox").each(function(j){
				            			if($(this).prop("checked")){
				            				if(count%3==0){message+="<br>";}else{message+="　　";}
				            				count+=1;
				            				message+="<font color=red>'"+$(this).attr("val2")+"'</font>";
			            			}
				            		});
				            		message+="</div>";
				            		$(".ui-dialog-title").html("是否要對此"+ count + "筆 訂單開立發票嗎?")
				            		$("#make_true_confirm").html(message);
				            		$("#make_true_confirm").dialog("open");
			                	}
				         }], "language": {"url": "js/dataTables_zh-tw.txt"}});
					}
					$(table).find("td").css("text-align", "center");
					$(table+" td > label").css({"float":"none","display":"inline","margin":"0px 0px 0px 5px"});
					$(contain).show();
					$(contain).animate({"opacity":"0.01"},2);
					$(contain).animate({"opacity":"1"},300);
					warning_msg("");
				}
				if(resultRunTime<1){
					$(contain).hide();
					if(parameter["invoice"]==true){warning_msg_last("---查無此結果---");}else{warning_msg("---查無此結果---");}
				}
			}
		});
	}
}

	$(function() {
		$(".bdyplane").animate({"opacity":"1"});
		//未開發票之訂單查詢
		$("#searh_invoice_false").click(function(e) {
			e.preventDefault();
// 			$("#invoice_false_table_contain").show();
			var tmp={
				action : "search_invoice_false",
				invoice_false_start: $("#invoice_false_start").val(),
				invoice_false_end: $("#invoice_false_end").val(),
				invoice: false
			};
			draw_invoice(tmp);
		});
		//已開發票之訂單查詢
		$("#searh_invoice_true").click(function(e) {
			e.preventDefault();
// 			alert($("#invoice_true_start").val().length<3);
// 			$("#invoice_true_table_contain").show();
			var tmp={
				action : "search_invoice_true",
				invoice_true_start: $("#invoice_true_start").val(),
				invoice_true_end: $("#invoice_true_end").val(),
				invoice: true
			};
			draw_invoice(tmp);
		});
		$("#test").click(function(e) {
			$(":checkbox").attr("checked","checked");
			var count=0;
			$(":checkbox").each(function(j){
   				if($(this).prop("checked")){
   					count+=1;
   				}
    		});
			alert(count);
		});
		$("#make_true_confirm").dialog({
			draggable : false, resizable : false, autoOpen : false,
			height : "auto", width : "auto", modal : true,
			show : {effect : "blind",duration : 300},
			hide : {effect : "fade",duration : 300},
			buttons : {
				"確認開立" : function() {
					var sale_array="";
					$(".false_checkbox").each(function(j){
            			if($(this).prop("checked")){
            				sale_array+=","+$(this).val();
            			}
					});
					$("#invoice_false_table_contain").hide();
					$("#invoice_true_table_contain").hide();
					warning_msg("發立發票需時較長請稍候...");
					//warning_msg_last("發立發票中請稍候...");
					
					$.ajax({
    					type : "POST",
    					url : "invoice.do",
    					data : {
    						action : "make_invoice_true",
    						sale_array :sale_array
    					},
    					success : function(result) {
    						console.log(result);
    						var json_obj = $.parseJSON(result);
    						var resultRunTime = 0;
    						$.each (json_obj, function (i) {
    							$.ajax({
    								type : "POST",
    								url : "invoice.do",
    								async : false,
    								data : {
    									action : "createInvoiceFile",
    									sale_id : json_obj[i]
    								},
    								success : function(result) {
    									console.log('test');
    								}
    							});
//     							alert(json_obj[i]);
    						});
    						draw_invoice({
								action : "search_invoice_false",
								invoice_false_start: $("#invoice_false_start").val(),
								invoice_false_end: $("#invoice_false_end").val(),
								invoice: false
							});
    						draw_invoice({
								action : "search_invoice_true",
								invoice_true_start: null,
								invoice_true_end: null,
								invoice_date : current_time(),
								invoice: true
							});
    						
    					}
    				});
					//alert('顯示今天下面');//顯示今天下面
					$(this).dialog("close");
				},"不開立發票" : function() {
					$(this).dialog("close");
				}
			}
		});
		$("#make_true_confirm").show();
		$("#make_false_confirm").dialog({
			draggable : false, resizable : false, autoOpen : false,
			height : "auto", width : "auto", modal : true,
			show : {effect : "blind",duration : 300},
			hide : {effect : "fade",duration : 300},
			buttons : {
				"確認作廢發票" : function() {
					$(this).dialog("close");
					$("#invoice_false_table_contain").hide();
					$("#invoice_true_table_contain").hide();
					//warning_msg("發立發票中請稍候...");
					warning_msg_last("作廢發票中請稍候...");
// 					return;
					var sale_array="";
					$(".true_checkbox").each(function(j){
						if($(this).prop("checked")){
							$.ajax({
		    					type : "POST",
		    					url : "invoice.do",
		    					data : {
		    						action : "make_invoice_false",
		    						invoice_id :$(this).attr("val3")
		    					},
		    					success : function(result) {
									console.log(result);	    						
		    					}
							});
						}
    				});
					draw_invoice({
						action : "search_invoice_false",
						invoice_false_start: $("#invoice_false_start").val(),
						invoice_false_end: $("#invoice_false_end").val(),
						invoice : false,
						async : false
					});
// 					sleep2(3000);
// 					sleep(1500).then(() => {
						draw_invoice({
							action : "search_invoice_true",
							invoice_true_start: $("#invoice_true_start").val(),
							invoice_true_end: $("#invoice_true_end").val(),
							invoice: true,
							async : true
						});
// 					});
					//alert('顯示今天下面');//顯示今天下面
					$(this).dialog("close");
				},"不作廢發票" : function() {
					$(this).dialog("close");
				}
			}
		});
		$("#make_false_confirm").show();
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
		$.ajax({
			type : "POST",
			url : "invoice.do",
			data : {
				action : "get_path"
			},
			success : function(result) {
				$("#no_path").html(result);	    						
			}
		});
		
	});
	</script>
<!-- 		<div class="datalistWrap" > -->
			<!-- 第一列 -->
			<div class="input-field-wrap">
				
				<div style=";color:#7f7f7f;height:0px;width:0px;">未開發票</div>
				<div class="form-wrap">
					<form id="invoice_false_form">
						<div class="form-row">
							<label for="">
								<span class="block-label">銷貨起日</span>
								<input type="text" class="input-date" id="invoice_false_start">
							</label>
							<div class="forward-mark"></div>
							<label for="">
								<span class="block-label">銷貨迄日</span>
								<input type="text" class="input-date" id="invoice_false_end">
							</label>
							<button id="searh_invoice_false" class="btn btn-darkblue">查詢</button>
						</div>
					</form>
				</div><!-- /.form-wrap -->
			</div><!-- /.input-field-wrap -->
			<!-- 第二列 -->
			<div class=" search-result-wrap" style="height:457px;">
				
				<div id="invoice_false_table_contain" class="result-table-wrap" style="display:none;">
					<div id='no_path' align=center></div>
					<table id="invoice_false_table" class="result-table">
						<thead>
							<tr>
								<th>銷貨單號</th>
								<th>訂單號</th>
								<th>產品名稱</th>
								<th>銷貨金額</th>
								<th>發票號碼</th>
								<th>發票日期</th>
								<th>銷貨/出貨日期</th>
								<th>銷售平台</th>
								<th style="background-image: none !important;"><div><input type='checkbox' style="position:static;" onclick='$(".false_checkbox").attr("checked",$(this).prop("checked"));'></input></div>勾選</th>
							</tr>
						</thead>
						<tbody>
						</tbody>
					</table>
				</div>
			</div>
<!-- 		</div> -->
		<div class="datalistWrap">
			<!-- 第三列 -->
<!-- 			<button id='test'>AAAAAA</button> -->
			<hr class="hr0">
			<div class="input-field-wrap">
			<div style=";color:#7f7f7f;height:0px;width:0px;">已開發票</div>
				<div class="form-wrap">
					<form id="invoice_true_form">
						<div class="form-row">
							<label for="">
								<span class="block-label">銷貨起日</span>
								<input type="text" class="input-date" id="invoice_true_start">
							</label>
							<div class="forward-mark"></div>
							<label for="">
								<span class="block-label">銷貨迄日</span>
								<input type="text" class="input-date" id="invoice_true_end">
							</label>
							<button id="searh_invoice_true" class="btn btn-darkblue">查詢</button>				
						</div>
					</form>
				</div><!-- /.form-wrap -->
			</div><!-- /.input-field-wrap -->
			<div class="search-result-wrap"style="height:457px;">
				<div id="invoice_true_table_contain" class="result-table-wrap" style="display:none;">
					<table id="invoice_true_table" class="result-table">
						<thead>
							<tr>
								<th>銷貨單號</th>
								<th>訂單號</th>
								<th>產品名稱</th>
								<th>銷貨金額</th>
								<th>發票號碼</th>
								<th>發票日期</th>
								<th>銷貨/出貨日期</th>
								<th>銷售平台</th>
								<th style="background-image: none !important;"><div><input type='checkbox' style="position:static;" onclick='$(".true_checkbox").attr("checked",$(this).prop("checked"));'></input></div>勾選</th>
							</tr>
						</thead>
						<tbody>
						</tbody>
					</table>
				</div>
			</div>
		</div>
	</div>
	<div id="make_true_confirm"  style="display:none;"></div>
	<div id="make_false_confirm"  style="display:none;"></div>
</div>

</body>
</html>