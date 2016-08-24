<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ page import="tw.com.aber.purchase.controller.*"%>
<%@ page import="java.util.*"%>
<%@ page import="java.sql.DriverManager"%>
<%@ page import="java.sql.Connection"%>
<%@ page import="java.sql.Statement"%>
<%@ page import="java.sql.ResultSet"%>
<jsp:directive.page import="java.sql.SQLException" />
<!DOCTYPE html>
<html>
<head>
<title>進貨報表</title>
<meta charset="utf-8">
<link rel="Shortcut Icon" type="image/x-icon" href="./images/Rockettheme-Ecommerce-Shop.ico" />
<link rel="stylesheet" href="css/styles.css" />
<link href="<c:url value="css/css.css" />" rel="stylesheet">
<link href="<c:url value="css/jquery.dataTables.min.css" />" rel="stylesheet">
<link href="<c:url value="css/1.11.4/jquery-ui.css" />" rel="stylesheet">
<link rel="stylesheet" href="css/buttons.dataTables.min.css" />
</head>
<body>
	<jsp:include page="template.jsp" flush="true"/>
	<div class="content-wrap" >
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
		//進貨日查詢相關設定
		$("#search_purchase_date").click(function(e) {
			e.preventDefault();
				$.ajax({
					type : "POST",
					url : "purchase.do",
					data : {
						action : "search_purchase_date",
						purchase_start_date: $("#purchase_start_date").val(),
						purchase_end_date: $("#purchase_end_date").val()
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
										if(json_obj[i].seq_no==null){
											json_obj[i].seq_no="";
										}
										if(json_obj[i].memo==null){
											json_obj[i].memo="";
										}
										if(json_obj[i].purchase_date==null){
											json_obj[i].purchase_date="";
										}
										if(json_obj[i].invoice==null){
											json_obj[i].invoice="";
										}
										if(json_obj[i].invoice_type==null||json_obj[i].invoice_type=="0"){
											json_obj[i].invoice_type="";
										}
										if(json_obj[i].invoice_type=="1"){
											json_obj[i].invoice_type="二聯式發票";
										}
										if(json_obj[i].invoice_type=="2"){
											json_obj[i].invoice_type="三聯式發票";
										}
										if(json_obj[i].amount==null){
											json_obj[i].amount="";
										}
										result_table 
										+= "<tr>"
										+ "<td name='"+ json_obj[i].seq_no +"'>"+ json_obj[i].seq_no+ "</td>"
										+ "<td name='"+ json_obj[i].purchase_date +"'>"+ json_obj[i].purchase_date+ "</td>"
										+ "<td name='"+ json_obj[i].invoice +"'>"+ json_obj[i].invoice+ "</td>"
										+ "<td name='"+ json_obj[i].invoice_type +"'>"+ json_obj[i].invoice_type+ "</td>"
										+ "<td name='"+ json_obj[i].amount +"'>"+ json_obj[i].amount+ "</td>"
										+ "<td name='"+ json_obj[i].memo +"'>"+ json_obj[i].memo+ "</td></tr>";
									}
								});
							}
							
							$("#purchases").dataTable().fnDestroy();
							if(resultRunTime!=0&&json_obj[resultRunTime-1].message=="驗證通過"){
								if($("#purchase_date_err_mes").length){
									$("#purchase_date_err_mes").remove();
	                			}
								$("#purchases_contain_row").show();
								$("#purchases tbody").html(result_table);
								draw_table("purchases",'進貨報表');
								$("#purchases").find("td").css("text-align", "center");
							}
						}
				});		
		});
	})
</script>
		<div class="input-field-wrap">
			<div class="form-wrap">
				<div class="form-row">
					<label for="">
						<span class="block-label">進貨起日</span>
						<input type="text" class="input-date" id="purchase_start_date" name="purchase_start_date">
					</label>
					<div class="forward-mark"></div>
					<label for="">
						<span class="block-label">進貨迄日</span>
						<input type="text" class="input-date" id="purchase_end_date" name="purchase_end_date">
					</label>
					<a class="btn btn-darkblue" id="search_purchase_date">查詢</a>
				</div>
			</div><!-- /.form-wrap -->
		</div>
			<div id ="purchases_contain_row" class="search-result-wrap" style="display:none;">
				<div id="purchases-contain" class="result-table-wrap">
					<table id="purchases" class="result-table ">
						<thead>
							<tr class="ui-widget-header">
								<th>進貨單號</th>
								<th>進貨日期</th>
								<th>進貨發票號碼</th>
								<th>發票樣式</th>
								<th>進貨發票金額</th>
								<th>備註說明</th>
							</tr>
						</thead>
						<tbody>
						</tbody>
					</table>
				</div>
			</div>			
		</div>
</body>
</html>