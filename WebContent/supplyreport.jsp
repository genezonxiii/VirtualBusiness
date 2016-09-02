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
<title>供應商報表</title>
<meta charset="utf-8">
<link rel="Shortcut Icon" type="image/x-icon" href="./images/Rockettheme-Ecommerce-Shop.ico" />
<link rel="stylesheet" href="css/jquery.dataTables.min.css" />
<link rel="stylesheet" href="css/buttons.dataTables.min.css" />
<link rel="stylesheet" href="css/styles.css" />
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
	$(function() {
		$(".bdyplane").animate({"opacity":"1"});
		//=============自定義validator=============
		//字符最大長度驗證（一個中文字符長度為2）
		//查詢相關設定
		$.ajax({
				type : "POST",
				url : "supply.do",
				data : {
					action : "search",
					supply_name : $("input[name='searh_supply_name'").val(),
					
				},
				success : function(result) {
						var json_obj = $.parseJSON(result);
						var result_table = "";
						$.each(json_obj,function(i, item) {
							result_table += 
								"<tr><td>"+ json_obj[i].supply_name+
								"</td><td>"+json_obj[i].supply_unicode+ 
								"</td><td>"+json_obj[i].address+
								"</td><td>"+json_obj[i].contact+
								"</td><td>"+json_obj[i].phone+
								"</td><td>"+json_obj[i].ext+
								"</td><td>"+json_obj[i].mobile+
								"</td><td>"+json_obj[i].email+
								"</td><td>"+json_obj[i].contact1+
								"</td><td>"+json_obj[i].phone1+
								"</td><td>"+json_obj[i].ext1+
								"</td><td>"+json_obj[i].mobile1+
								"</td><td>"+json_obj[i].email1+
								"</td><td>"+json_obj[i].memo+"</td></tr>";
						});
						//判斷查詢結果
						var resultRunTime = 0;
						$.each (json_obj, function (i) {
							resultRunTime+=1;
						});
						$("#products2").dataTable().fnDestroy();
						if(resultRunTime!=0){
							
							$("#products2-contain").show();
							$("#products2 tbody").html(result_table);
							draw_table("products2","供應商報表");
							$("#products2").find("td").css({"word-break":"break-all","min-width":"30px","text-align":"center" });
							$(".validateTips").text("");
						}else{
							$("#products2-contain").hide();
							$(".validateTips").text("查無此結果");
						}
					}
				});
	});
</script>
		<div class="datalistWrap">
		
			<div class="row search-result-wrap" >
				<div id="products2-contain" class="ui-widget" style="display:none">
					<table id="products2" class="result-table">
						<thead>
							<tr class="ui-widget-header">
								<th>廠商名稱</th>
								<th>廠商統編</th>
								<th>廠商地址</th>
								<th>連絡人</th>
								<th>連絡人電話</th>
								<th>連絡人分機</th>
								<th>連絡人手機</th>
								<th>連絡人email</th>
								<th>第二連絡人</th>
								<th>第二連絡人電話</th>
								<th>第二連絡人分機</th>
								<th>第二連絡人手機</th>
								<th>第二連絡人email</th>
								<th>備註說明 </th>
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
	</div>
</body>
</html>