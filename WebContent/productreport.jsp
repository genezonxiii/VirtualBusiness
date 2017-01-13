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
<title>商品報表</title>
<meta charset="utf-8">

<link rel="Shortcut Icon" type="image/x-icon" href="./images/Rockettheme-Ecommerce-Shop.ico" />
<link rel="stylesheet" href="css/photo/jquery.fileupload.css">
<link rel="stylesheet" href="css/styles.css" />
<link href="<c:url value="css/css.css" />" rel="stylesheet">
<link href="<c:url value="css/jquery.dataTables.min.css" />" rel="stylesheet">
<link href="<c:url value="css/1.11.4/jquery-ui.css" />" rel="stylesheet">
<link rel="stylesheet" href="css/jquery.dataTables.min.css" />
<link rel="stylesheet" href="css/buttons.dataTables.min.css" />

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
<script type="text/javascript" src="js/jquery.scannerdetection.js"></script>

<script>
	var new_or_edit=0;
	var scan_exist=0;
	var information;
	function draw_product(info){
		$.ajax({
			type : "POST",
			url : "product.do",
			data : info,
			success : function(result) {
				console.log(result);
					var json_obj = $.parseJSON(result);
					var len=json_obj.length;
					//判斷查詢結果
					var resultRunTime = 0;
					$.each (json_obj, function (i) {
						resultRunTime+=1;
					});
					if(json_obj[resultRunTime-1].message=="驗證通過"){
						var result_table = "";
						$.each(json_obj,function(i, item) {
							if(i<len-1){
// 								var tmp=(json_obj[i].photo.length<1)?"無圖片":"<img src=./image.do?picname="+json_obj[i].photo+" style='max-width:100px;max-height:100px'>";
// 								var tmp1=(json_obj[i].photo1.length<1)?"無圖片":"<img src=./image.do?picname="+json_obj[i].photo1+" style='max-width:100px;max-height:100px'>";
								result_table 
								+= "<tr>"
								+ "<td name='"+ json_obj[i].c_product_id +"'>"+ json_obj[i].c_product_id+ "</td>"
								+ "<td name='"+ json_obj[i].product_name +"'>"+ json_obj[i].product_name+ "</td>"
								+ "<td name='"+ json_obj[i].supply_name +"'>"+ json_obj[i].supply_name + "</td>"
								+ "<td name='"+ json_obj[i].type_id +"'>"+ json_obj[i].type_id+ "</td>"
								+ "<td name='"+ json_obj[i].unit_id +"'>"+ json_obj[i].unit_id+ "</td>"
								+ "<td name='"+ json_obj[i].cost +"'>"+ money(json_obj[i].cost)+ "</td>"
								+ "<td name='"+ json_obj[i].price +"'>"+ money(json_obj[i].price)+ "</td>"
								+ "<td name='"+ json_obj[i].keep_stock +"'>"+ json_obj[i].keep_stock+ "</td>"
// 								+ "<td name='"+ json_obj[i].photo+"'>"+tmp+"</td>"
// 								+ "<td name='"+ json_obj[i].photo1 +"'>"+tmp1+"</td>"
								+ "<td name='"+ json_obj[i].description +"'>"+ json_obj[i].description+ "</td>"
								+ "<td name='"+ json_obj[i].barcode +"'>"+ json_obj[i].barcode+ "</td></tr>";
							}
						});
					}
					if(resultRunTime==0){
						$("#sales-contain").hide();
						$(".validateTips").text("查無此結果");
					}
					$("#sales").dataTable().fnDestroy();
					if(resultRunTime!=0&&json_obj[resultRunTime-1].message=="驗證通過"){
						$("#sales-contain").show();
						$("#sales").dataTable().fnDestroy();
						$("#sales tbody").html(result_table);
						draw_table("sales","產品報表");
						//$("#sales").dataTable({"language": {"url": "js/dataTables_zh-tw.txt"},"order": []});
						$("#sales").find("td").css("text-align","center");
						$("#sales").find("th").css("text-align","center");
						$(".validateTips").text("");
					}
				}
			});
		
	}
	jQuery(document).ready(function($) {
	    $(window).scannerDetection();
	    $(window).bind('scannerDetectionComplete',function(e,data){
	    		if(data.string=="success"){return;}
	    		if(new_or_edit==1){
	    			$("#new_barcode").val(data.string);
	    		}
	    		if(new_or_edit==2){
	    			$("#edit_barcode").val(data.string);
	    		}
	    		if(new_or_edit==0){
	    			$.ajax({url : "product.do", type : "POST", cache : false,
			            data : {
			            	action : "find_barcode",
			            	barcode : data.string,
			            },
			            success: function(result) {
			            	var json_obj2 = $.parseJSON(result);
			            	if(json_obj2.length>0){
			            		information={
										action : "search_name",
										product_name : json_obj2[0].product_name,
									};
			            		
			            		draw_product(information);
			            	}else{
			            		$("#sales-contain").hide();
								$(".validateTips").text("查無此結果");
			            	}
			            }
		    		});
	    		}
	        })
	        .bind('scannerDetectionError',function(e,data){
	            console.log('detection error '+data.string);
	        })
	        .bind('scannerDetectionReceive',function(e,data){
	            console.log(data);
	        });
	    $(window).scannerDetection('success');
	});
	$(function() {
		$(".bdyplane").animate({"opacity":"1"});
		var uuid = "";
		var c_product_id="";
		var unit_id="";
		var supply_name = "";
		var type_id= "";
		var supply_id= "";
		//=============自定義validator=============
		//字符最大長度驗證（一個中文字符長度為2）
		jQuery.validator.addMethod("stringMaxLength", function(value, element, param) { 
			var length = value.length; 
			for ( var i = 0; i < value .length; i++) { 
				if (value.charCodeAt(i) > 127) { 
				length++; 
				} 
			} 
			return this.optional(element) || (length <= param); 
		}, $.validator.format("長度不能大於{0}!"));
		//字母數字
		jQuery.validator.addMethod("alnum", function(value, element) {
		return this.optional(element) || /^[a-zA-Z0-9]+$/.test(value);
		}, "只能包括英文字母和數字");
		//=========================================
		//驗證
		$("#searh-name").click(function(e) {
			e.preventDefault();
			information={
					action : "search_name",
					product_name : $("#searh_name").val(),
				};
			draw_product(information);
		});
		
		//自訂產品ID查詢相關設定
		$("#searh-sale").click(function(e) {
			e.preventDefault();
			information={
					action : "search",
					supply_name : $("input[name='searh_product_name'").val(),
				};
			draw_product(information);
		});
		//X_X
		//處理初始的查詢autocomplete//其實是supply@@@@
	       $("#searh_product_name").autocomplete({
	            minLength: 0,
	            source: function (request, response) {
	                $.ajax({
	                    url : "product.do",
	                    type : "POST",
	                    cache : false,
	                    delay : 1500,
	                    data : {
	                    	action : "search_product_data",
	                    	identity : "NAME",
	                        term : request.term
	                    },
	                    success: function(data) {
	                    	var json_obj = $.parseJSON(data);
	                    	response($.map(json_obj, function (item) {
	                            return {
	                              label: item.supply_name,
	                              value: item.supply_name,
	                              supply_id : item.supply_id,
	                              supply_name : item.supply_name,
	                            }
	                          }));
	                    },
	                    error: function(XMLHttpRequest, textStatus, errorThrown) {
	                        alert(textStatus);
	                    }
	                });
	            },
	            change: function(event, ui) {
	    	        var source = $(this).val();
	    	            var temp = $(".ui-autocomplete li").map(function () { return $(this).text()}).get();
	    	        var found = $.inArray(source, temp);
	    	
	    	        if(found < 0) {
	    	            $(this).val('');
	    	            $(this).attr("placeholder","請輸入正確廠商名稱以供查詢!");
	    	        }
	    	    }     
	         });
	       $("#searh_name").autocomplete({
	            minLength: 1,
	            source: function (request, response) {
	                $.ajax({
	                    url : "purchase.do",
	                    type : "POST",
	                    cache : false,
	                    delay : 1500,
	                    data : {
	                    	action : "search_product_data",
	                        term : request.term,
	                        identity : "NAME"
	                    },
	                    success: function(data) {
	                    	var json_obj = $.parseJSON(data);
	                    	response($.map(json_obj, function (item) {
	                            return {
		                        	 label: item.product_name,
		                             value: item.product_name,
		                             product_id: item.product_id,
		                             product_name: item.product_name,
		                             c_product_id: item.c_product_id,
		                             price: item.price,
		                             cost: item.cost
	                            }
	                          }))
	                    }
	                });
	            },
	            change: function(event, ui) {
	    	        var source = $(this).val();
	    	            var temp = $(".ui-autocomplete li").map(function () { return $(this).text()}).get();
	    	        var found = $.inArray(source, temp);
	    	        if(found < 0) {
	    	            $(this).val('');
	    	            $(this).attr("placeholder","輸入正確的產品名稱!");
	    	        }
	    	    }     
	         });
	       $("#searh_product_name").bind('focus', function(){  
	       	$(this).attr("placeholder","輸入供應商名稱");
	    	var eve=jQuery.Event("keydown");
	    	eve.which=40;
	      	$(this).trigger(eve);
	     } );
			//處理新增修改的下拉選單unit_id
               $.ajax({
                   url : "product.do",
                   type : "POST",
                   cache : false,
                   delay : 1500,
                   data : {
                   	action : "search_product_data",
                   	identity : "ID",
                       term : ''
                   },
                   success: function(data) {
                   	var json_obj = $.parseJSON(data);
					$.map(json_obj, function (item) {
                   		if (item.unit_name != '') {
                   			$("#select_insert_unit_id").append("<option value='" + item.unit_name + "'>" + item.unit_name + "</option>");
                   			$("#select_update_unit_id").append("<option value='" + item.unit_name + "'>" + item.unit_name + "</option>");
                   		}
                         })
                   },
                   error: function(XMLHttpRequest, textStatus, errorThrown) {
                       alert(textStatus);
                   }
               });
	    $("#warning").dialog({
			title: "警告",
			draggable : true,//防止拖曳
			resizable : false,//防止縮放
			autoOpen : false,
			height : "auto",
			modal : true,
			show : {effect : "bounce",duration : 1000},
			hide : {effect : "fade",duration : 300},
			buttons : {
				"確認" : function() {$(this).dialog("close");}
			}
		});
	    $("#warning").show();
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
						<span class="block-label">供應商名稱查詢</span>
						<input type="text" id="searh_product_name" name="searh_product_name"></input>
					</label>
					<button class="btn btn-darkblue" id="searh-sale">查詢</button>
				</div>
				<div class="form-row">
					<label for="">
						<span class="block-label">產品名稱查詢</span>
						<input type="text" id="searh_name" name="searh_name"></input>
					</label>
					<button class="btn btn-darkblue" id="searh-name">查詢</button>
				</div>
				<font color='#6A5ACD' >掃條碼亦可取得商品資料</font>
			</div><!-- /.form-wrap -->
		</div>
			
			<!-- 第一列 -->
			<div class="row search-result-wrap" align="center">
				<div id="sales-contain" class="ui-widget" style="display:none;">
					<table id="sales" class="result-table">
						<thead>
							<tr class="ui-widget-header">
								<th>自訂產品ID</th>
								<th style="min-width:80px;">產品名稱</th>
								<th>供應商名稱</th>
								<th>產品類別</th>
								<th>產品單位</th>
								<th style="min-width:70px;">成本</th>
								<th style="min-width:70px;">售價</th>
								<th>安全庫存</th>
<!-- 								<th style="max-width:100px;">產品圖片1</th> -->
<!-- 								<th style="max-width:100px;">產品圖片2</th> -->
								<th>產品說明</th>
								<th>條碼</th>
							</tr>
						</thead>
						<tbody>
						</tbody>
					</table>
				</div>
				<span class="validateTips"> </span>
			</div>
		</div>
	</div>
<div id="warning" style="display:none;"></div>
</div>
</body>
</html>