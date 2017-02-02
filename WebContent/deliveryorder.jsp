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
<title>出貨單</title>
<meta charset="utf-8">
<link rel="Shortcut Icon" type="image/x-icon" href="./images/Rockettheme-Ecommerce-Shop.ico" />

<link rel="stylesheet" href="css/jquery.dataTables.min.css" />
<link rel="stylesheet" href="css/buttons.dataTables.min.css" />
<link href="<c:url value="css/css.css" />" rel="stylesheet">
<link href="<c:url value="css/jquery.dataTables.min.css" />" rel="stylesheet">
<link href="<c:url value="css/1.12.0/jquery-ui.css" />" rel="stylesheet">
<link rel="stylesheet" href="css/styles.css" />
</head>
<body>
<jsp:include page="template.jsp" flush="true"/>
<div class="content-wrap" >
<div class='bdyplane' style="opacity:1">
	
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
	function dayPlusOne(dayStr){
		var year= "", month= "", date= "";
		var arr = dayStr.split("-");  
		var newdt = new Date(Number(arr[0]),Number(arr[1])-1,Number(arr[2])+1);
		
		year = ""+newdt.getFullYear();
		
		if((newdt.getMonth()+1).toString().length === 1){
			month = "0"+(newdt.getMonth()+1);
		}else{
			month = ""+(newdt.getMonth()+1);
		}
		
		if(newdt.getDate().toString().length === 1){
			date = "0"+newdt.getDate();
		}else{
			date = ""+newdt.getDate();
		}
		repnewdt = year + "-" + month + "-" + date;
		
		return repnewdt;
	}  
	$('#searh-productunit').click(function(e){
		e.preventDefault();
		
		if(DateValidator()){
			return;
		}
		
		$.ajax({
			type : "POST",
			url : "deliveryOrder.do",
			data : {
				action :"search",
				startTime : $('#datepicker1').val(),
				endTime : $('#datepicker2').val()
				},
			success : function(result) {
				console.log(result);
				var json_obj = $.parseJSON(result);
				var result_table = "";
				$.each(json_obj,function(i, item) {
					result_table += 
					"<tr><td>" + (json_obj[i].trans_list_date == null? "":json_obj[i].trans_list_date.replace("T00:00:00Z","")) + 
					"</td><td>"+ (json_obj[i].trans_list_date == null? "":dayPlusOne(json_obj[i].trans_list_date.replace("T00:00:00Z",""))) +
					"</td><td>"+ "" +
					"</td><td>"+ (json_obj[i].order_no == null ? "" : json_obj[i].order_no)  + 
					"</td><td>"+ "" +
					"</td><td>"+ (json_obj[i].name == null ? "" : json_obj[i].name)  + 
					"</td><td>"+ (json_obj[i].name == null ? "" : json_obj[i].name)  +
					"</td><td>"+ (json_obj[i].address == null ? "" : json_obj[i].address)  + 
					"</td><td>"+ (json_obj[i].mobile == null ? "" : json_obj[i].mobile) + 
					"</td><td>"+ (json_obj[i].phone == null ? "" : json_obj[i].phone)+ 
					"</td><td>"+ (json_obj[i].memo == null ? "" : json_obj[i].memo) + 
					"</td><td>"+ 1 + 
					"</td><td>"+ (json_obj[i].product_name == null ? "" : json_obj[i].product_name) + 
					"</td><td>"+ (json_obj[i].quantity == null ? "" : json_obj[i].quantity) + 
					"</td><td>"+ (json_obj[i].quantity == null ? "" : json_obj[i].quantity) + 
					"</td><td>"+ (json_obj[i].quantity == null ? "" : (json_obj[i].quantity * json_obj[i].quantity)) + 
					"</td><td>"+ "" +
					"</td><td>"+ "" +
					"</td></tr>";
				});
				
				if(json_obj.length!=0){
					$("#products-contain").show();
					$("#products").dataTable().fnDestroy();
					$("#products tbody").html(result_table);
					$("#products").dataTable({
						lengthMenu	: [ [10, 25, 50, -1], [10, 25, 50, "全"] ],
						dom			: 'Bfrtip',
					    buttons		: [{extend: 'excel',text: '輸出為execl報表'}],
						language	: {"url": "js/dataTables_zh-tw.txt"}
					});
					
					
					$(".validateTips").text("");
				}else{
					$("#products-contain").hide();
					$(".validateTips").text("查無此結果");
				}
			}
		});
	});
	
	function DateValidator(){
		var result = false ;
		var mes = "";
		var start = $('#datepicker1').val();
		var end = $('#datepicker2').val();
		var regEx = /^\d{4}-\d{2}-\d{2}$/;
		
		$(".validateTips").text("");
		
		if(start.length === 0){
			mes+="『起日』";
			result = true ;
		}
		if(end.length === 0){
			mes+="『訖日』";
			result = true ;
		}
		
		if(result){
			mes+="需輸入，方能進行查詢動作!";
			$(".validateTips").text(mes);
		}	
		if(!start.match(regEx) && start.length != 0){
			mes+="『起日』";
			result = true ;
		}
		
		if(!end.match(regEx) && end.length != 0){
			mes+="『訖日』";
			result = true ;
		}
		
		if(result && start.length != 0 && end.length !=0){
			mes+="格式不符!";
			$(".validateTips").text(mes);
		}
		if(!result){
			
			start = start.replace(/\-/g,"");
			end = end.replace(/\-/g,"");
			
			if(parseInt(end) < parseInt(start)){
				mes+="起日不可大於訖日!";
				$(".validateTips").text(mes);
				result = true ;
			}
		}
		return result;
	}
});
</script>
		<div class="input-field-wrap">
			<div class="form-wrap">
				<div class="form-row">
					<label for="">
						<span class="block-label">出貨單起日</span>
						<input type="text" class="input-date" id="datepicker1">
					</label>
					<div class="forward-mark"></div>
					<label for="">
						<span class="block-label">出貨單迄日</span>
						<input type="text" class="input-date" id="datepicker2">
					</label>
					<a class="btn btn-darkblue" id="searh-productunit">查詢</a>
				</div>
			</div>
		</div>
		<div class="search-result-wrap" >
			<div id="products-contain" class="result-table-wrap" style="width:100%;display:none;">
				<table id="products" class="result-table">
					<thead>
						<tr>
							<th>收件日</th>
							<th>配達日</th>
							<th>托運單號</th>
							<th>訂單編號</th>
							<th></th>
							<th>訂購人</th>
							<th>收件人</th>
							<th>收件地址</th>
							<th>收件人手機1</th>
							<th>收件人電話2</th>
							<th>交易備註</th>
							<th>送貨時段</th>
							<th>訂購品項</th>
							<th>訂購份數</th>
							<th>盒數</th>
							<th>總數量</th>
							<th>DM</th>
							<th>J</th>								
						</tr>
					</thead>
					<tbody>
					</tbody>
				</table>
			</div>
			<div class="validateTips" align="center"> </div>
		</div>
	</div>
	</div>
</body>
</html>