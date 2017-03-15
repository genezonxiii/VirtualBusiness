<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html>
<html>
<head>
<link rel="stylesheet" href="css/jquery.dataTables.min.css" />
<title>供應商報表</title>
<meta charset="utf-8">
<link rel="Shortcut Icon" type="image/x-icon" href="./images/Rockettheme-Ecommerce-Shop.ico" />
</head>
<body>
	<jsp:include page="template.jsp" flush="true" />
	<div class="content-wrap">
		<div class='bdyplane' style="opacity: 0">
			<div class="datalistWrap">
				<div class="row search-result-wrap">
					<div id="products2-contain" class="ui-widget" style="display: none">
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
									<th>備註說明</th>
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
<script>
	$(function() {
		$(".bdyplane").animate({"opacity":"1"});
		$("#products2-contain").show()
		
		$("#products2").dataTable().fnDestroy();
		$("#products2").show();
		
		$("#products2").DataTable({
			dom: 'lfrB<t>ip',
			paging: true,
			ordering: true,
			scrollX: true,
			scrollY: '50vh',
	        scrollCollapse: true,
			info: false,
			language: {"url": "js/dataTables_zh-tw.txt"},
			ajax: {
				url : "supply.do",
				dataSrc: "",
				type : "POST",
				data : {
					action : "search",
					supply_name : $("input[name='searh_supply_name'").val(),
				}
			},
			columns: [
				{"data": "supply_name" ,"defaultContent":""},
				{"data": "supply_unicode" ,"defaultContent":""},
				{"data": "address" ,"defaultContent":""},
				{"data": "contact" ,"defaultContent":""},
				{"data": "phone" ,"defaultContent":""},
				{"data": "ext" ,"defaultContent":""},
				{"data": "mobile" ,"defaultContent":""},
				{"data": "email" ,"defaultContent":""},
				{"data": "contact1" ,"defaultContent":""},
				{"data": "phone1" ,"defaultContent":""},
				{"data": "ext1" ,"defaultContent":""},
				{"data": "mobile1" ,"defaultContent":""},
				{"data": "email1" ,"defaultContent":""},
				{"data": "memo" ,"defaultContent":""}
			]				      	      
		});
	});
</script>


<script type="text/javascript" src="js/jquery-migrate-1.4.1.min.js"></script>
<script type="text/javascript" src="js/jquery.validate.min.js"></script>
<script type="text/javascript" src="js/jquery.dataTables.min.js"></script>
<script type="text/javascript" src="js/additional-methods.min.js"></script>
<script type="text/javascript" src="js/messages_zh_TW.min.js"></script>

</body>
</html>