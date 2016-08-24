<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ page import="tw.com.aber.customer.controller.*"%>
<%@ page import="java.util.*"%>
<%@ page import="java.sql.DriverManager"%>
<%@ page import="java.sql.Connection"%>
<%@ page import="java.sql.Statement"%>
<%@ page import="java.sql.ResultSet"%>
<jsp:directive.page import="java.sql.SQLException" />

<!DOCTYPE html>
<html>
<head>
<title>客戶管理</title>
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
<script type="text/javascript" src="js/jquery.scannerdetection.js"></script>

<script>
window.onload = function (e){ 
	e.preventDefault();
	$.ajax({
		type : "POST",
		url : "customer.do",
		data : {
			action : "search"
		},
		success : function(result) {
			var json_obj = $.parseJSON(result);
			//判斷查詢結果
			var resultRunTime = 0;
			$.each (json_obj, function (i) {
				resultRunTime+=1;
			});
			var result_table = "";
			if(resultRunTime!=0){
				$.each(json_obj,function(i, item) {
					if(json_obj[i].name==null||json_obj[i].name=='NULL'){
				        json_obj[i].name ="";
				    }
					if(json_obj[i].address==null||json_obj[i].address=='NULL'){
				        json_obj[i].address ="";
				    }
					if(json_obj[i].phone==null||json_obj[i].phone=='NULL'){
				        json_obj[i].phone ="";
				    }
					if(json_obj[i].mobile==null||json_obj[i].mobile=='NULL'){
				        json_obj[i].mobile ="";
				    }
					if(json_obj[i].email==null||json_obj[i].email=='NULL'){
				        json_obj[i].email ="";
				    }
					if(json_obj[i].post==null||json_obj[i].post=='NULL'){
				        json_obj[i].post ="";
				    }
					if(json_obj[i].class==null||json_obj[i].class=='NULL'){
				        json_obj[i].class ="";
				    }
					if(json_obj[i].memo==null||json_obj[i].memo=='NULL'){
				        json_obj[i].memo ="";
				    }
					result_table 
					+= "<tr>"
					+ "<td id='name_"+i+"' name='"+ json_obj[i].name +"'>"+ json_obj[i].name+ "</td>"
					+ "<td id='address_"+i+"' name='"+ json_obj[i].address +"'>"+ json_obj[i].address+ "</td>"
					+ "<td id='phone_"+i+"' name='"+ json_obj[i].phone +"'>"+ json_obj[i].phone+ "</td>"
					+ "<td id='mobile_"+i+"' name='"+ json_obj[i].mobile +"'>"+ json_obj[i].mobile+ "</td>"
					+ "<td id='email_"+i+"' name='"+ json_obj[i].email +"'>"+ json_obj[i].email+ "</td>"
					+ "<td id='post_"+i+"' name='"+ json_obj[i].post +"'>"+ json_obj[i].post+ "</td>"
					+ "<td id='class_"+i+"' name='"+ json_obj[i].class +"'>"+ json_obj[i].class+ "</td>"
					+ "<td id='memo_"+i+"' name='"+ json_obj[i].memo +"'>"+ json_obj[i].memo+ "</td></tr>";
				});
			}
			if(resultRunTime==0){
				$("#customer-contain").hide();
				$(".validateTips").text("查無此結果");
			}
			$("#customer").dataTable().fnDestroy();
			if(resultRunTime!=0){
				$("#customer-contain").show();
				$("#customer tbody").html(result_table);
				$("#customer").find("td").css("text-align", "center");
				draw_table("customer","客戶資料報表");
				$("#customer").find("th").css({"word-break":"break-all","min-width":"70px","text-align":"center" });
				$("#customer").find("td").css({"word-break":"break-all","min-width":"70px","text-align":"center" });
				$(".validateTips").text("");
			}						
		}
	});
}

</script>
	<div class="panel-content">
		<div class="datalistWrap">
			<div class="row search-result-wrap" id ="sales_contain_row">
				<div id="customer-contain" class="ui-widget" style="display:none">
					<table id="customer" class="result-table">
						<thead>
							<tr class="ui-widget-header">
								<th>客戶姓名</th>
								<th>收貨地址</th>
								<th>電話</th>
								<th>手機</th>
								<th>Email</th>
								<th>郵政編號</th>
								<th>客戶等級</th>
								<th>備註</th>
							</tr>	
					</table>
				</div>
				<div class="validateTips" align="center"> </div>
			</div>	
		</div>
	</div>
</div>
</body>
</html>