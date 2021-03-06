<!-- 沒有處理完的70% -->
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
<title>庫存資料</title>
<meta charset="utf-8">
<link rel="Shortcut Icon" type="image/x-icon" href="./images/Rockettheme-Ecommerce-Shop.ico" />

<link href="<c:url value="css/css.css" />" rel="stylesheet">
<link href="<c:url value="css/jquery.dataTables.min.css" />" rel="stylesheet">
<link rel="stylesheet" href="css/1.11.4/jquery-ui.css">

<link rel="stylesheet" href="css/styles.css" />
<!-- <link rel="stylesheet" href="css/beagle/beagle.css"> -->


<style>
.spbg{
/* 	border:1px dotted #a00; */
	font-size:16px;
	font-weight:bold;
	color:red;
	background-image:url('./images/sp_bg.png');
}
tr:hover .spbg{
	background-image:none;
	border: 1px solid #ddd;
}
</style>
</head>
<body>
	<jsp:include page="template.jsp" flush="true"/>
	<div class="content-wrap" >
		<div class='bdyplane' style="opacity:0">
		
<script type="text/javascript" src="js/jquery-1.10.2.js"></script>
<script type="text/javascript" src="js/jquery.dataTables.min.js"></script>
<script type="text/javascript" src="js/jquery-ui.min.js"></script>
<script type="text/javascript" src="js/jquery-ui.js"></script>
<script type="text/javascript" src="js/jquery-migrate-1.4.1.min.js"></script>
<script type="text/javascript" src="js/jquery.validate.min.js"></script>
<script type="text/javascript" src="js/additional-methods.min.js"></script>
<script type="text/javascript" src="js/messages_zh_TW.min.js"></script>
<script type="text/javascript" src="js/jquery.scannerdetection.js"></script>

<script>
var bar_search=null,uuid;
function draw_stock(parameter){
// 	if(parameter.action=="bar_code_search")alert(bar_search+" & "+uuid);
	$("#products-contain").css({"opacity":"0"});
	warning_msg("---讀取中請稍候---");
	$.ajax({
		type : "POST",
		url : "stock.do",
		data : parameter,
		success : function(result) {
			//alert(result);
			//console.log(result);
			var json_obj = $.parseJSON(result);
			var result_table = "";
			$.each(json_obj,function(i, item) {
				if(bar_search==null||uuid==json_obj[i].product_id){
					result_table += 
						  "<tr><td>"+json_obj[i].product_name
						+ "</td><td "+((json_obj[i].quantity<json_obj[i].keep_stock) ? "class='spbg'" : "")+">" + json_obj[i].quantity +"</td>"
						+ "<td>"+json_obj[i].keep_stock+"</td>"
						+ "<td><div class='table-row-func btn-in-table btn-gray'><i class='fa fa-ellipsis-h'></i>"
						+ "	<div class='table-function-list'>"
						+ "		<button class='btn-in-table btn-darkblue btn_update' title='修改' value='"+ json_obj[i].product_id+"'name='"+ json_obj[i].stock_id+"' "+(parameter.action=="bar_code_search"||bar_search!=null?"id='bar_search'":"")+"><i class='fa fa-pencil'></i></button>"
						+ "	</div></div></td></tr>";
				}
			});
			
			//判斷查詢結果
			var resultRunTime = 0;
			$.each (json_obj, function (i) {
				resultRunTime+=1;
			});
			$("#products").dataTable().fnDestroy();
			if(resultRunTime!=0){
				$("#products-contain").show();
				$("#products tbody").html(result_table);
				$("#products").dataTable({ "language": {"url": "js/dataTables_zh-tw.txt","zeroRecords": "沒有符合的結果"}});
				tooltip('btn_update');
				$("#products-contain").animate({"opacity":"0.01"},1);
				$("#products-contain").animate({"opacity":"1"},300);
				warning_msg("");
			}else{
				$("#products-contain").hide();
				if(parameter.action=="bar_code_search"){
					warning_msg("---查無此條碼庫存---");
				}else{
					warning_msg("---查無此商品庫存---");
				}
			}
		}
	});
}
	jQuery(document).ready(function($) {
	    $(window).scannerDetection();
	    $(window).bind('scannerDetectionComplete',function(e,data){
	    	var tmp={
				action : "bar_code_search",
				barcode : data.string
			};
	    	draw_stock(tmp);
        })
        .bind('scannerDetectionError',function(e,data){console.log('detection error '+data.string);})
        .bind('scannerDetectionReceive',function(e,data){console.log(data);});
	});

	$(function() {
		$(".bdyplane").animate({"opacity":"1"});
		var validator_update = $("#update-dialog-form-post").validate({
			rules : {
				memo : {
					required : true,
					maxlength : 200
				}
			},
			messages : {
				group_name : {
					maxlength : "長度不能超過200個字"
				}
			}
		});
		var group_name = $("#group_name");
		$("#searh_stock").click(function(){
			var tmp={
					action : "searh",
					product_name : $("#searh_stock_name" ).val(),
				};
			draw_stock(tmp);
		});
		//修改Dialog相關設定
		update_dialog = $("#dialog-form-update").dialog({
			draggable : true, resizable : false, autoOpen : false,
			height : "auto", width :480, modal : true,
			show : {effect : "blind",duration : 300},
			hide : {effect : "fade",duration : 300},
			buttons : [{
				text : "修改",
				click : function() {
 					if ($('#update-dialog-form-post').valid()) {
 						var tmp={
	 							action : "update",
	 							unit_id : uuid,
	 							product_id : $("#dialog-form-update input[name='product_id']").val(),
	 							stock_id : $("#dialog-form-update input[name='stock_id']").val(),
	 							quantity : $("#dialog-form-update input[name='quantity']").val(),
	 							memo : $("#dialog-form-update input[name='memo']").val()
							};
 						draw_stock(tmp);
						update_dialog.dialog("close");
 					}
				}
			}, {
				text : "取消",
				click : function() {
					validator_update.resetForm();
					update_dialog.dialog("close");
				}
			} ],
			close : function() {
				validator_update.resetForm();
			}
		});
		$("#dialog-form-update").show();
		//修改事件聆聽		
		$("#products").delegate(".btn_update", "click", function(e) {
			e.preventDefault();
			if($(this).attr("id")!=null){
				bar_search=$(this).attr("id");
			}else{
				bar_search=null;
			}
			uuid = $(this).val();
			$("input[name='search_product_id'").val("");
			$.ajax({
				type : "POST",
				url : "stock.do",
				data : {
					action : "searh",
					product_id : $("input[name='search_product_id'").val()
				
				},
				success : function(result) {
						var json_obj = $.parseJSON(result);
						var len=json_obj.length;
						
						//判斷查詢結果
						var resultRunTime = 0;
						$.each (json_obj, function (i) {
							resultRunTime+=1;
						});
							var result_table = "";
							$.each(json_obj,function(i, item) {
								if(json_obj[i].product_id==uuid){
										$("#dialog-form-update input[name='product_id']").val(json_obj[i].product_id);
										$("#dialog-form-update input[name='stock_id']").val(json_obj[i].stock_id);
										$("#dialog-form-update input[name='quantity']").val(json_obj[i].quantity);
										$("#dialog-form-update input[name='memo']").val(json_obj[i].memo);
								}
							});
						} 

				});			
			update_dialog.dialog("open");
		});	
		var product_name_tags=[];
		$.ajax({
			type : "POST",
			url : "product.do",
			data :{action : "search"},
			success : function(result) {
				var json_obj = $.parseJSON(result);
				$.each (json_obj, function (i,item) {
					if(json_obj[i].product_name!=null){
						product_name_tags[i]=json_obj[i].product_name;
					}
				});
			}
		});
		auto_complete("searh_stock_name",product_name_tags);
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
		<div class="datalistWrap">
			<div class="input-field-wrap">
				<div class="form-wrap">
					<div class="form-row">
						<label for="">
							<span class="block-label">商品名稱查詢</span>
							<input type="text" id="searh_stock_name"></input>
						</label>
						<button class="btn btn-darkblue" id="searh_stock">查詢</button>
						<br><br>
						<font color='#6A5ACD' >掃條碼亦可取得商品資料</font>
					</div>
				</div><!-- /.form-wrap -->
			</div>
			<div id="dialog-form-update" title="修改庫存資料" style="display:none;">
				<form name="update-dialog-form-post" id="update-dialog-form-post">
					<fieldset>
							<table class="form-table" style="margin-right:0px;">
							<tbody>
							<tr><td>庫存數量：</td><td><input type="text" id="quant" name="quantity"placeholder="修改庫存數量"/></td>
							<td>&nbsp;
							<a onclick="$('#quant').val(parseInt($('#quant').val())+1);" class="btn btn-gray">+</a>
							<a onclick="$('#quant').val(parseInt($('#quant').val())-1);" class="btn btn-gray">-</a>
<!-- 								&nbsp;<a class='btn-gray' onclick="$('#quant').val(parseInt($('#quant').val())+1);">&nbsp;+&nbsp;</a>&nbsp;<a class='btn-gray' onclick="$('#quant').val(parseInt($('#quant').val())-1);">&nbsp;-&nbsp;</a> -->
							</td></tr>
							<tr><td>備註說明：</td><td><input type="text" name="memo"placeholder="修改備註說明"/></td></tr>
							<tr><td><input type="hidden" name="stock_id" disabled="disabled"> 
							<input type="hidden" name="product_id" disabled="disabled"></td> </tr>
							</tbody>
							</table>	
					</fieldset>
				</form>
			</div>			
			<!-- 第二列 -->
			<div class="row search-result-wrap" align="center" style="margin:0px auto;">
				<div id="products-contain" class=" result-table-wrap" style="display:none;">
					<table id="products" class="ui-widget ui-widget-content result-table">
						<thead>
							<tr class="ui-widget-header">
								<th>商品名稱 </th>
								<th style="max-width:150px;">庫存數量</th>
								<th style="max-width:150px;">安全庫存</th>
								<th style="background-image: none !important;">功能</th>
							</tr>
						</thead>
						<tbody id="tbdy" style="text-align:center">
						</tbody>
					</table>
				</div>
				<span class="validateTips"> </span>
			</div>
		</div>
	</div>
	</div>
</body>
</html>