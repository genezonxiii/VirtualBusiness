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
<title>直播排程設定作業</title>
<meta charset="utf-8">
<link rel="Shortcut Icon" type="image/x-icon" href="./images/Rockettheme-Ecommerce-Shop.ico" />

<link rel="stylesheet" href="css/jquery.dataTables.min.css" />
<link rel="stylesheet" href="css/buttons.dataTables.min.css" />
<link href="<c:url value="css/css.css" />" rel="stylesheet">
<link href="<c:url value="css/jquery.dataTables.min.css" />" rel="stylesheet">
<link href="<c:url value="css/1.12.0/jquery-ui.css" />" rel="stylesheet">
<link rel="stylesheet" href="css/styles.css" />

<style>
.date2 {
    background-image: url('./images/icon-datepicker.svg') !important;
    background-repeat: no-repeat !important;
    background-position: right center !important;
}
table.form-table{
	border-collapse: separate;
	border-spacing: 10px 20px;
	margin-right: 30px;
	font-family: "微軟正黑體", "Microsoft JhengHei", 'LiHei Pro', Arial, Helvetica, sans-serif, \5FAE\8EDF\6B63\9ED1\9AD4,\65B0\7D30\660E\9AD4;
}
table.form-table tr td:nth-child(2n+1) {
    text-align: right;
    padding-left: 4px;
    padding-bottom: 5px;
}
table.form-table tr td:nth-child(2n) {
    text-align: left;
}
input[type=text].error{
    border: 1px solid #e92500;
    background: rgb(255, 213, 204);
}
</style>

</head>
<body>
	<jsp:include page="template.jsp" flush="true"/>
	
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

<div class="content-wrap">
	<h2 class="page-title">直播排程設定作業</h2>
	<div id="msgAlert"></div>
	
	<div class="search-result-wrap">
		<div id='view' style='background:#f8f8f8;padding:20px;border: 3px solid #666;margin:20px auto;width:860px;	border-radius: 8px;box-shadow: 10px 10px 5px #999;'>
			<div align='center' style='font-size:36px;color:#888;'>直播排程設定作業</div>
			<form id='youtubeBroadcast' style='margin:20px;'>
				<table class='form-table'>
					<tr>
						<td>直播節目標題：</td>
						<td colspan='3'>
							<input type='text' id='broadcast_title' style='width:250px;' name='broadcast_title'  placeholder='請輸入直播節目標題' value="Virtual Business">
						</td>
					</tr>
					<tr>
						<td>開始日期：</td>
						<td>
							<input type='text' id='start_date' class='date2' placeholder='請填入開始日期'>
						</td>
						<td>開始時間：</td>
						<td>
							<select name="select_start_hour"></select> 時
							<select name="select_start_minute"></select> 分
						</td>
					</tr>
					<tr>
						<td>結束日期：</td>
						<td>
							<input type='text' id='end_date' class='date2' placeholder='請填入結束日期'>
						</td>
						<td>結束時間：</td>
						<td>
							<select name="select_end_hour"></select> 時
							<select name="select_end_minute"></select> 分
						</td>
					</tr>
				</table>
				
				<div align='center'>
					<a id='confirm' class='btn btn-primary'>確認</a>
					<a id='reset' class='btn btn-primary'>重置</a>
					<a id='broadcast_page' class='btn btn-primary' target="_blank">開啟直播連結</a>
					<a id='control_room' class='btn btn-primary' href="https://www.youtube.com/my_live_events" target="_blank">開啟中控室</a>
					
				</div>
			</form>
		</div>
	</div>
</div>
<script>
	$(function(){
		

		
		$("#youtubeBroadcast").validate({ 
			rules : { 
			broadcast_title : { required : true }, 
			start_date : { required : true }, 
			select_start_hour : { required : true }, 
			select_start_minute : { required : true },
			end_date : { required : true }, 
			select_end_hour : { required : true }, 
			select_end_minute : { required : true }
// 			service_id_5 : { required : true }, 
// 			product_id : { required : true , maxlength : 40 }, 
// 			cust_name : { required : true , maxlength : 20 }, 
// 			cust_tel:{ required : true , maxlength : 20 },
// 			cust_mobile:{ maxlength : 20 },
// 			cust_address:{ maxlength : 200 },
// 			purchase_date:{ date : true }
			}
		}); 
		
		
		var d = $.datepicker.formatDate('yy-mm-dd', new Date());
		$("#start_date").val(d);
		
		
		for (var i = 0; i < 24; i++) {
			$("select[name='select_start_hour']").append("<option value='" + i + "'>" + i + "</option>");
			$("select[name='select_end_hour']").append("<option value='" + i + "'>" + i + "</option>");
		}
		
		var section = 30;
		for (var i = 0; i < 2; i++) {
			$("select[name='select_start_minute']").append("<option value='" + (i * section) + "'>" + (i * section) + "</option>");
			$("select[name='select_end_minute']").append("<option value='" + (i * section) + "'>" + (i * section) + "</option>");
		}
		
		$("#confirm").click(function(){
			
			if($("#youtubeBroadcast").valid()){
				
				$.ajax({
					type : "POST",
					url : "youtube.do",
					data : {
						action : "create",
						broadcast_title : $("#broadcast_title").val(),
						start_date : $("#start_date").val(),
						start_hour : $("select[name='select_start_hour']").val(),
						start_minute : $("select[name='select_start_minute']").val(),
						end_date : $("#end_date").val(),
						end_hour : $("select[name='select_end_hour']").val(),
						end_minute : $("select[name='select_end_minute']").val() 
					},
					success : function(result) {
						var json_obj = $.parseJSON(result);
						console.log(json_obj);
						if(json_obj.success){
							console.log(json_obj.broadcast_id);
							warningMsg("提醒", json_obj.info);
							
							
							$("#control_room").attr("href", "https://www.youtube.com/my_live_events");
							$("#broadcast_page").attr("href", "https://www.youtube.com/watch?v=" + json_obj.broadcast_id);
						} else {
							warningMsg("警告", json_obj.info);
						};
					}
				});
			}
		});
		
		$("#reset").click(function(){
			$("#broadcast_title").val('');
			$("#start_date").val('');
			$("select[name='select_start_hour']").val('');
			$("select[name='select_start_minute']").val('');
			$("#end_date").val('');
			$("select[name='select_end_hour']").val('');
			$("select[name='select_end_minute']").val('');
		});
		
		function warningMsg(title, msg) {
			$("#msgAlert").html(msg);
			
			$("#msgAlert").dialog({
				title: title,
				draggable : true,
				resizable : false, //防止縮放
				autoOpen : false,
				height : "auto",
				modal : true,
				buttons : {
					"確認" : function() {
						$(this).dialog("close");
					}
				}
			});
				
			$("#msgAlert").dialog("open");
		}

		$( ".date2" ).datepicker({
		   dayNamesMin : ["日","一","二","三","四","五","六"],
		   monthNames : ["1","2","3","4","5","6","7","8","9","10","11","12"],
		   monthNamesShort:["1","2","3","4","5","6","7","8","9","10","11","12"],
		   prevText: "上月", 
		   nextText: "次月", 
		   weekHeader: "週", 
		   dateFormat: "yy-mm-dd",
		   showMonthAfterYear: true, 
		   changeYear: true,  
		   changeMonth: true
		});
	});
</script>

</body>
</html>