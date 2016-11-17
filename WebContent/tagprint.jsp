<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ page import="java.util.*"%>
<%@ page import="java.sql.DriverManager"%>
<%@ page import="java.sql.Connection"%>
<%@ page import="java.sql.Statement"%>
<%@ page import="java.sql.ResultSet"%>
<jsp:directive.page import="java.sql.SQLException" />
<!DOCTYPE html>
<html>
<head>
<title>標籤列印</title>
<meta charset="utf-8">
<link rel="Shortcut Icon" type="image/x-icon" href="./images/Rockettheme-Ecommerce-Shop.ico" />
<link rel="stylesheet" href="css/styles.css" />
<link href="<c:url value="css/css.css" />" rel="stylesheet">
<link href="<c:url value="css/jquery.dataTables.min.css" />" rel="stylesheet">
<!-- jquery-ui css要套這一版本，不然Dialog icon會有問題 -->
<link rel="stylesheet" href="css/1.11.4/jquery-ui.css">
</head>
<body>
	<jsp:include page="template.jsp" flush="true"/>
	<div class="content-wrap" >
		<div class='bdyplane' style="opacity:0">
<!-- jquery-ui js要套用這一版，不然Dialog會偏移，且容易當掉 -->
<script type="text/javascript" src="js/jquery-1.10.2.js"></script>
<script type="text/javascript" src="js/jquery.dataTables.min.js"></script>
<script type="text/javascript" src="js/jquery-ui.min.js"></script>
<script type="text/javascript" src="js/jquery-ui.js"></script>
<script type="text/javascript" src="js/jquery-migrate-1.4.1.min.js"></script>
<script type="text/javascript" src="js/jquery.validate.min.js"></script>
<script type="text/javascript" src="js/additional-methods.min.js"></script>
<script type="text/javascript" src="js/messages_zh_TW.min.js"></script>
<script>
	function alert_dialog(str){
		$("#warning").html(str);
		$("#warning").dialog("open");
	}
	var ua = window.navigator.userAgent;
	var msie = ua.indexOf("MSIE ");
	    
	window.onload = function (){
		// 判斷是否為 ie 瀏覽器 (支援 ie 11)
		if (msie > 0 || !!navigator.userAgent.match(/Trident.*rv\:11\./)){   
			// 可在此增加需要的功能	
	    }
	    else { // If another browser, return 0
	    	$("#warning").dialog("open");
	    	//alert("本列印功能只支援IE瀏覽器");
	    }
	}

	$(function() {
		$(".bdyplane").animate({"opacity":"1"});
		// 點選商品名稱時，清除商品ID欄位
		$("#product_name").focus(function() {
			$('#c_product_id').val("");
		});

		// 點選商品ID欄位時，清除商品名稱
		$("#c_product_id").focus(function() {
			$('#product_name').val("");
		});
		
		// 列印數量驗證
		var validator_tagprint = $("#tagprint-form").validate({
			rules : {
				qty : {
					min : 1,
					digits : true,
					maxlength : 2
				}
			}
		});

		//處理 product id 的autocomplete查詢
		$("#c_product_id").autocomplete({
		     minLength: 2,
		     source: function (request, response) {
		         $.ajax({
		             url : "tagprint.do",
		             type : "POST",
		             cache : false,
		             delay : 1500,
		             data : {
		             	action : "autocomplete_id",
		             	term : request.term
		             },
		             success: function(data) {
		             	var json_obj = $.parseJSON(data);
		             	response($.map(json_obj, function (item) {
							return {
								label: item.c_product_id,
			                    value: item.c_product_id
			               	}
		             	}));
		             },
		             error: function(XMLHttpRequest, textStatus, errorThrown) {
		                 alert_dialog(textStatus);
		             }
		         });
		     },
		     change: function(e, ui) {
		     	 if (!ui.item) {
		     		 $(this).val("");
		             $(this).attr("placeholder","請輸入查詢商品ID");
		          }
		     },
		     response: function(e, ui) {
		         if (ui.content.length == 0) {
		             $(this).val("");
		             $(this).attr("placeholder","請輸入查詢商品ID");
		         }
		     }           
		 });
		$("#c_product_id").dblclick(function(event){ $("#c_product_id").autocomplete({minLength: 1}); });
		//處理 product name 的autocomplete查詢
		$("#product_name").autocomplete({
		     minLength: 2,
		     source: function (request, response) {
		         $.ajax({
		             url : "tagprint.do",
		             type : "POST",
		             cache : false,
		             delay : 1500,
		             data : {
		             	action : "autocomplete_name",
		             	term : request.term
		             },
		             success: function(data) {
		             	var json_obj = $.parseJSON(data);
		             	response($.map(json_obj, function (item) {
							return {
								label: item.product_name,
			                    value: item.product_name
			               	}
		             	}));
		             },
		             error: function(XMLHttpRequest, textStatus, errorThrown) {
		                 alert_dialog(textStatus);
		             }
		         });
		     },
		     change: function(e, ui) {
		     	 if (!ui.item) {
		     		 $(this).val("");
		             $(this).attr("placeholder","請輸入查詢商品名稱");
		          }
		     },
		     response: function(e, ui) {
		         if (ui.content.length == 0) {
		             $(this).val("");
		             $(this).attr("placeholder","請輸入查詢商品名稱");
		         }
		     }           
		 });
		$("#product_name").dblclick(function(event){ $("#product_name").autocomplete({minLength: 0}); });
		//自訂產品查詢相關設定
		$(".search-taginfo").click(function(e) {
			e.preventDefault();
			$.ajax({
				type : "POST",
				url : "tagprint.do",
				data : {
					action : "search",
					product_name : $("#product_name").val(),
					c_product_id : $("#c_product_id").val()
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
								$('#output-product-name').val(json_obj[i].product_name);
								$('#output-barcode').val(json_obj[i].barcode);
								$('#output-description').val("");
							}
							return false;
						});
					}							
					if(resultRunTime==0){
						$("#sales-contain").hide();
						$(".validateTips").show();
						$(".validateTips").text("查無此結果");
					}
					$("#sales").dataTable().fnDestroy();
					if(resultRunTime!=0&&json_obj[resultRunTime-1].message=="驗證通過"){
					}
				}
			});
		});
		
		$("#search-productunit").click(function(e) {
			e.preventDefault();
			// 辨識是否為IE瀏覽器 (支援到 ie 11)
			if (msie > 0 || !!navigator.userAgent.match(/Trident.*rv\:11\./)) { 
				
				var product_name = $("#output-product-name").val();
				var description = $("#output-description").val();
				var barcode = $("#output-barcode").val();
				var size = $("#output-font-size").val();
				var qty = $("#output-qty").val();
				var product_name_line1 = "";
				var product_name_line2 = "";
				var description_line1 = "";
				var description_line2 = "";
				
				// 判斷條碼欄位
				if (barcode == ""){
					alert_dialog("條碼不可為空白，請重新查詢商品");
					return;
				}
				
				// 判斷字體大小欄位 (字體：大)
				if(size == 36) {
					// 判斷自訂輸出內容欄位 (不可超過8個字)
					if(description.length > 8){
						alert_dialog("字體為大時，自訂輸出內容最多8字元，請重新輸入");
						return;
					} else {
						// 判斷商品名稱欄位長度
						// 長度不可超過兩行，16個字元 (單行8個字)
						if(product_name.length > 16){
							alert_dialog("字體為大時，商品名稱最多16字元，請重新輸入");
							return;
						}
						// 判斷商品名稱是否超過8字元，做斷行處理
						else if(product_name.length > 8){
							product_name_line1 = product_name.substr(0,8);
							product_name_line2 = product_name.substr(8);
						} else {
							product_name_line1 = product_name;
						}
					}					
				}

				// 判斷字體大小欄位 (字體：小)
				if(size == 24){
					// 判斷自訂輸出內容欄位 (不可超過22個字)
					if(description.length > 22){
						alert_dialog("字體為小時，自訂輸出內容最多22字元，請重新輸入");
						return;
					} else {
						// 判斷商品名稱欄位長度
						// 長度不可超過兩行，22個字元 (單行11個字)
						if(product_name.length > 22) {
							alert_dialog("字體為小時，商品名稱最多22字元，請重新輸入");
							return;
						}
						// 判斷商品名稱是否超過11字元，做斷行處理
						if(product_name.length > 11){
							product_name_line1 = product_name.substr(0,11);
							product_name_line2 = product_name.substr(11);
						} else {
							product_name_line1 = product_name;
						}
						// 判斷自訂輸出是否超過11字元，做斷行處理
						if(description.length > 11){
							description_line1 = description.substr(0,11);
							description_line2 = description.substr(11);
						} else {
							description_line1 = description;
						}
					}
				}
				
				var TSCObj;
				TSCObj = new ActiveXObject("TSCActiveX.TSCLIB");
						
 				//TSCObj.ActiveXabout();																
					
				//printer name
				TSCObj.ActiveXopenport ("TSC TTP-244 Pro");
					
				//標籤機設定
				//Ver.1  
				//TSCObj.ActiveXsetup ("50.0","30.0","4","8","0","2","0");   
				
				//Ver.2
				TSCObj.ActiveXsetup ("40.0","35.0","4","8","0","3","0");
				
				TSCObj.ActiveXsendcommand ("SET TEAR ON");
				
				TSCObj.ActiveXclearbuffer();

				//***** Ver.1 標籤機 列印方式 *****
	 			//TSCObj.ActiveXwindowsfont (400, 120, size, 180, 2, 0, "標楷體", product_name);   		
	 			//TSCObj.ActiveXwindowsfont (400, 20, size, 180, 2, 0, "標楷體", description);     		

				//***** Ver.2 標籤機 列印方式 *****
				
				if (size == 24) {	
					// 字體 = 小，商品名稱與自訂輸出各兩行，共列印四行
					TSCObj.ActiveXwindowsfont (300, 170, size, 180, 2, 0, "標楷體", product_name_line1);
					TSCObj.ActiveXwindowsfont (300, 130, size, 180, 2, 0, "標楷體", product_name_line2);
					TSCObj.ActiveXwindowsfont (300, 90, size, 180, 2, 0, "標楷體", description_line1);
					TSCObj.ActiveXwindowsfont (300, 50, size, 180, 2, 0, "標楷體", description_line2);
				} else if (size == 36) {
					// 字體 = 大，商品名稱兩行，自訂輸出一行，共列印三行
					TSCObj.ActiveXwindowsfont (300, 160, size, 180, 2, 0, "標楷體", product_name_line1);
					TSCObj.ActiveXwindowsfont (300, 110, size, 180, 2, 0, "標楷體", product_name_line2);
					TSCObj.ActiveXwindowsfont (300, 60, size, 180, 2, 0, "標楷體", description);
				} else {
					TSCObj.ActiveXwindowsfont (300, 160, size, 180, 2, 0, "標楷體", product_name);
					TSCObj.ActiveXwindowsfont (300, 50, size, 180, 2, 0, "標楷體", description);
				}
				
				//標籤機設定
				//Ver.1  		
				//TSCObj.ActiveXbarcode ("340", "200", "128", "50", "1", "180", "2", "2", barcode);
				//Ver.2
				TSCObj.ActiveXbarcode ("280", "250", "128", "50", "1", "180", "2", "2", barcode);
				
				TSCObj.ActiveXprintlabel ("1", qty.toString());
				
				TSCObj.ActiveXcloseport();
				
			} else { // If another browser, return 0
			    alert_dialog("本列印功能只支援IE瀏覽器");
			}	
		});
		$("#warning").html("貼心提醒您:<br>&nbsp;&nbsp;本列印功能只支援IE瀏覽器。");
		$("#warning").dialog({
			title: "警告",
			draggable : false,//防止拖曳
			resizable : false,//防止縮放
			autoOpen : false,
			height : "auto",
			modal : true,
			show : {effect : "bounce",duration : 1500},
			hide : {effect : "fade",duration : 300},
			buttons : {
				"確認" : function() {$(this).dialog("close");}
			}
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
	<div class="panel-content">
	<div class="input-field-wrap">
			<div class="form-wrap">
				<div class="form-row">
					<label for="">
						<span class="block-label">查詢商品ID</span>
						<input type="text" id="c_product_id" name="c_product_id" placeholder="請輸入查詢商品ID">
					</label>
					<button class="btn btn-darkblue search-taginfo" id="search-taginfo">查詢</button>
				</div>
				<div class="form-row">
					<label for="">
						<span class="block-label">查詢商品名稱</span>
						<input type="text" id="product_name" name="product_name" placeholder="請輸入查詢商品名稱">
					</label>
					<button class="btn btn-darkblue search-taginfo" id="search-taginfo">查詢</button>
				</div>
			</div><!-- /.form-wrap -->
		</div><!-- /.input-field-wrap -->
		
		
		<div class="datalistWrap">
			<!-- 第一列 -->
<!-- 			<div class="row" align="center"> -->
<!-- 				<div class="ui-widget" style="width: 600px;margin:0px auto;"> -->
<!-- 					<table id="products-serah-create" style="margin: 0px auto;"> -->
<!-- 						<thead> -->
<!-- 							<tr> -->
<!-- 								<th><input type="text" id="c_product_id" name="c_product_id" placeholder="請輸入查詢商品ID"></th> -->
<!-- 								<th><input type="text" id="product_name" name="product_name" placeholder="請輸入查詢商品名稱"></th> -->
<!-- 								<th> -->
<!-- 									<button id="search-taginfo" style="width:80px;">查詢</button> -->
<!-- 								</th> -->
<!-- 							</tr> -->
<!-- 						</thead> -->
<!-- 					</table> -->
<!-- 				</div> -->
<!-- 			</div> -->
			<!-- 第二列 -->
			<div class="row" align="center">
				<div id="products-contain" class="ui-widget result-table-wrap" style="width: 60%;">
					<table id="products" class="ui-widget ui-widget-content result-table" style="margin:20px auto;">
						<thead>
							<tr class="ui-widget-header">
								<th COLSPAN=2>標籤列印內容</th>
							</tr>
						</thead>
						<tbody id="tbdy" name="tbdy">
							<tr>
								<td style="text-align:right">條碼：</td>
								<td style="text-align:left"><input type="text" id="output-barcode" size="40" readonly style="cursor:not-allowed;"></td>
							</tr>
							<tr>
								<td style="text-align:right">商品名稱：</td>
								<td style="text-align:left"><input type="text" id="output-product-name" size="40" readonly style="cursor:not-allowed;"></td>
							</tr>
							<tr>
								<td style="text-align:right">自訂輸出內容：</td>
								<td style="text-align:left"><textarea rows="2" id="output-description" cols="41"></textarea></td>
							</tr>
						</tbody>				
					</table>
					<div>
						<font color="#FF0000">(自訂輸出內容) 字體為大時最多可輸出8字元，字體為小時最多可22字元</font>
					</div>		
				</div>
				<span class="validateTips" style="display:none"> </span>
			</div>
			<!-- 第三列 -->
			<div class="row" align="center">
				<form name="tagprint-form" id="tagprint-form" >
					<div id="products-serah-create-contain" class="ui-widget" style="width: 60%; margin:0px auto;">
						<table id="products-serah-create" class="ui-widget ui-widget-content" style="margin: 0px auto;">
							<thead class="ui-widget-header">
								<tr>
									<th>字體大小：
										<select id="output-font-size">
										  	<option value="36" checked>大</option>
										  	<option value="24">小</option>
										</select>
									</th>
									<th>份數：
										<input id="output-qty" type="text" name="qty" maxlength="2" size="2" value="1" style="display:inline">
										&nbsp&nbsp(輸入範圍 1~99)
									</th>
									<th>
										<button id="search-productunit" class="btn btn-exec btn-wide">列印</button>
									</th>
								</tr>
							</thead>
						</table>
					</div>
				</form>
			</div>
		</div>
	</div>
</div>
</div>
<div id="warning"></div>
<!-- ################################################### -->
</body>
</html>