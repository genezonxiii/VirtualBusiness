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
<title>商品庫存報表</title>
<meta charset="utf-8">
<link rel="Shortcut Icon" type="image/x-icon" href="./images/Rockettheme-Ecommerce-Shop.ico" />
<link rel="stylesheet" href="css/jquery.dataTables.min.css" />
<link rel="stylesheet" href="css/buttons.dataTables.min.css" />
<link rel="stylesheet" href="css/styles.css" />
<link href="<c:url value="css/css.css" />" rel="stylesheet">
<link href="<c:url value="css/jquery.dataTables.min.css" />" rel="stylesheet">
<link rel="stylesheet" href="css/1.11.4/jquery-ui.css">
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
<script>
	$(function() {
		$.ajax({
			type : "POST",
			url : "stock.do",
			data : {
				action : "searh",
				product_id : $("#dialog-form-searh input[name='search_product_id']" ).val(),
			},
			success : function(result) {
					var json_obj = $.parseJSON(result);
					var result_table = "";
					$.each(json_obj,function(i, item) {
						result_table += 
								"<tr><td>"+json_obj[i].product_name+"</td><td>" 
								+json_obj[i].quantity+"</td></tr>";
					});
					
					$("#group_button").show();
					//判斷查詢結果
					var resultRunTime = 0;
					$.each (json_obj, function (i) {
						resultRunTime+=1;
					});
					$("#products").dataTable().fnDestroy();
					if(resultRunTime!=0){
						$("#products-contain").show();
						$("#products tbody").html(result_table);
						draw_table("products","商品庫存報表");
						$(".validateTips").text("");
					}else{
						$("#products-contain").hide();
						$(".validateTips").text("查無此結果");
					}
				}
		});

		//預設表格隱藏
		$("#products-contain").hide();
	});	
</script>

		<div class="datalistWrap">
			<!--對話窗樣式-修改 -->
			<!-- 第二列 -->
			<div class="row search-result-wrap" align="center" style="width:600px;margin:0px auto;">
				<div id="products-contain" class=" result-table-wrap" style="display:none" >
					<table id="products" class="ui-widget ui-widget-content result-table">
						<thead>
							<tr class="ui-widget-header">
								<th style="min-width:150px;">產品名稱 </th>
								<th style="min-width:150px;">庫存數量</th>
							</tr>
						</thead>
						<tbody id="tbdy">
						</tbody>
					</table>
				</div>
				<span class="validateTips"> </span>
			</div>
		</div>
	</div>
</body>
</html>