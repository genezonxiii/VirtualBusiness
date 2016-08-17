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
<title>商品管理報表</title>
<meta charset="utf-8">

<link rel="Shortcut Icon" type="image/x-icon" href="./images/Rockettheme-Ecommerce-Shop.ico" />
<!-- 圖片的 -->

<link rel="stylesheet" href="css/jquery.dataTables.min.css" />
<link rel="stylesheet" href="css/buttons.dataTables.min.css" />
<link rel="stylesheet" href="css/styles.css" />
<link href="<c:url value="css/css.css" />" rel="stylesheet">
<link href="<c:url value="css/jquery.dataTables.min.css" />" rel="stylesheet">
<link href="<c:url value="css/1.11.4/jquery-ui.css" />" rel="stylesheet">

</head>
<body>
	<jsp:include page="template.jsp" flush="true"/>
	<div class="content-wrap" style="margin:56px 0px 28px 120px;">

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
	jQuery(document).ready(function($) {
	    $(window).scannerDetection();
	    $(window).bind('scannerDetectionComplete',function(e,data){
	    		if(data.string=="success"){return;}
	    		if(new_or_edit==1){
	    			//$("#new_barcode").focus();
	    			$("#new_barcode").val(data.string);
	    		}
	    		if(new_or_edit==2){
	    			//$("#edit_barcode").focus();
	    			$("#edit_barcode").val(data.string);
	    		}
	    		if(new_or_edit==0){
	    			new_or_edit=3;
	    			$.ajax({url : "product.do", type : "POST", cache : false,
			            data : {
			            	action : "find_barcode",
			            	barcode : data.string,
			            },
			            success: function(result) {
			            	var json_obj = $.parseJSON(result);
			            	var result_table = "";
							$.each(json_obj,function(i, item) {
								if(i<json_obj.length){
									result_table 
									+= "<tr>"
									+ "<td name='"+ json_obj[i].c_product_id +"'>"+ json_obj[i].c_product_id+ "</td>"
									+ "<td name='"+ json_obj[i].product_name +"'>"+ json_obj[i].product_name+ "</td>"
									+ "<td name='"+ json_obj[i].cost +"'>"+ json_obj[i].cost + "</td>"
									+ "<td name='"+ json_obj[i].keep_stock +"'>"+ json_obj[i].keep_stock+ "</td></tr>";
								}
								if(json_obj.length==0){
									$("#sales-contain22").hide();
									$(".validateTips").text("查無此結果");
								}
								if(json_obj.length>0){
									$("#sales-contain22").show();
									$("#sales22 tbody").html(result_table);
									draw_table("sales22","商品管理報表");
									$("#sales22").find("td").css("text-align","center");
									$("#sales22").find("th").css("text-align","center");
									$(".validateTips").text("");
								}
								
							});
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
		var uuid = "";
		var c_product_id="";
		var unit_id="";
		var supply_name = "";
		var type_id= "";
		var supply_id= "";
		//自訂產品ID查詢相關設定
		$("#searh-sale").click(function(e) {
			e.preventDefault();
			$.ajax({
					type : "POST",
					url : "product.do",
					data : {
						action : "search",
						supply_name : $("input[name='searh_product_name'").val(),
					},
					success : function(result) {
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
										result_table += "<tr>"
										+ "<td name='"+ json_obj[i].c_product_id +"'>"+ json_obj[i].c_product_id+ "</td>"
										+ "<td name='"+ json_obj[i].product_name +"'>"+ json_obj[i].product_name+ "</td>"
										+ "<td name='"+ json_obj[i].supply_name +"'>"+ json_obj[i].supply_name + "</td>"
										+ "<td name='"+ json_obj[i].type_id +"'>"+ json_obj[i].type_id+ "</td>"
										+ "<td name='"+ json_obj[i].unit_id +"'>"+ json_obj[i].unit_id+ "</td>"
										+ "<td name='"+ json_obj[i].cost +"'>"+ json_obj[i].cost+ "</td>"
										+ "<td name='"+ json_obj[i].price +"'>"+ json_obj[i].price+ "</td>"
										+ "<td name='"+ json_obj[i].keep_stock +"'>"+ json_obj[i].keep_stock+ "</td>"
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
								$("#sales tbody").html(result_table);
								draw_table("sales","商品管理報表");
								$("#sales").find("td").css("text-align","center");
								$("#sales").find("th").css("text-align","center");
								$(".validateTips").text("");
							}
						}
					});
		})
		//處理初始的查詢autocomplete
	       $("#searh_product_name").autocomplete({
	            minLength: 1,
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
	       $("#searh_product_name").bind('focus', function(){ $(this).attr("placeholder","請輸入廠商名稱以供查詢"); } );
			//處理新增的下拉選單unit_id
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
	                    		}
// 	                    		console.log("'" + item.unit_name + "'");
	                          })
	                    },
	                    error: function(XMLHttpRequest, textStatus, errorThrown) {
	                        alert(textStatus);
	                    }
	                });
			//處理新增的下拉選單type_id
   					 $.ajax({
	                    url : "product.do",
	                    type : "POST",
	                    cache : false,
	                    delay : 1500,
	                    data : {
	                    	action : "search_product_data",
	                    	identity : "ID2",
	                        term : ''
	                    },
	                    success: function(data) {
	                    	var json_obj = $.parseJSON(data);
							$.map(json_obj, function (item) {
	                    		if (item.type_name != '') {
	                    			$("#select_insert_type_id").append("<option value='" + item.type_name + "'>" + item.type_name + "</option>");
	                    		}
	                          })
	                    },
	                    error: function(XMLHttpRequest, textStatus, errorThrown) {
	                        alert(textStatus);
	                    }
	                });
			//處理修改的下拉選單unit_id
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
	                    			$("#select_update_unit_id").append("<option value='" + item.unit_name + "'>" + item.unit_name + "</option>");
	                    		}
	                          })
	                    },
	                    error: function(XMLHttpRequest, textStatus, errorThrown) {
	                        alert(textStatus);
	                    }
	                });
			//處理修改的下拉選單type_id
	     			  $.ajax({
	                    url : "product.do",
	                    type : "POST",
	                    cache : false,
	                    delay : 1500,
	                    data : {
	                    	action : "search_product_data",
	                    	identity : "ID2",
	                        term : ''
	                    },
	                    success: function(data) {
	                    	var json_obj = $.parseJSON(data);
							$.map(json_obj, function (item) {
	                    		if (item.type_name != '') {
	                    			$("#select_update_type_id").append("<option value='" + item.type_name + "'>" + item.type_name + "</option>");
	                    		}
	                          })
	                    },
	                    error: function(XMLHttpRequest, textStatus, errorThrown) {
	                        alert(textStatus);
	                    }
	                });
	});	
</script>

		<div class="datalistWrap">
		<div class="input-field-wrap">
			<div class="form-wrap">
				<div class="form-row">
					<label for="">
						<span class="block-label">廠商名稱查詢</span>
						<input type="text" id="searh_product_name" name="searh_product_name"></input>
					</label>
					<button class="btn btn-darkblue" id="searh-sale">查詢</button>
				</div>
			</div><!-- /.form-wrap -->
		</div>
			
			<!-- 第一列 -->
			<div class="row search-result-wrap">
				<div id="sales-contain" class="ui-widget" style="display:none;">
					<table id="sales" class="result-table">
						<thead>
							<tr class="ui-widget-header">
								<th>自訂產品ID</th>
								<th><p style="width:120px">產品名稱</p></th>
								<th>廠商名稱</th>
								<th>產品類別</th>
								<th>產品單位</th>
								<th>成本</th>
								<th>售價</th>
								<th>安全庫存</th>
								<th>產品說明</th>
								<th>條碼</th>
							</tr>
						</thead>
						<tbody>
						</tbody>
					</table>
				</div>
				<div class="validateTips" align="center"> </div>
			</div>
			<!-- 4th -->
			<div class="row search-result-wrap" align="center">
				<div id="sales-contain22" class="ui-widget" style="display:none">
					<table id="sales22" class="result-table">
						<thead>
							<tr class="ui-widget-header">
								<th>自訂產品ID</th>
								<th>產品名稱</th>
								<th>數量</th>
								<th>售價</th>
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
</body>
</html>