<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>

<%
	request.setCharacterEncoding("UTF-8");
	String groupId = (String) request.getSession().getAttribute("group_id");
	String userId = (String) request.getSession().getAttribute("user_id");
	String userName = (String) request.getSession().getAttribute("user_name");
	String menu = (String) request.getSession().getAttribute("menu");
	String privilege = (String) request.getSession().getAttribute("privilege");
%>

<html>
<head>
<title>統一速達-託運單管理</title>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<link rel="Shortcut Icon" type="image/x-icon"
	href="./images/Rockettheme-Ecommerce-Shop.ico" />
<jsp:include page="template/common_css.jsp" flush="true" />
<link rel="stylesheet" href="css/buttons.dataTables.min.css">
</head>
<body>
	<input type="hidden" id="glb_menu" value='<%=menu%>' />
	<input type="hidden" id="glb_privilege" value="<%=privilege%>" />

	<div class="page-wrapper">
		<jsp:include page="template/common_headfoot.jsp" flush="true" />

		<div class="content-wrap">
			<h2 class="page-title">託運單管理</h2>

			<div class="panel-content">
				<div class="datalistWrap">
					<div class="input-field-wrap">
						<div class="form-wrap">
							<div class="form-row">
								<form>
									<label for=""> <span class="block-label">訂單編號</span>
										<input type="text">
									</label>
									<label for=""> <span class="block-label">託運單號碼</span>
										<input type="text">
									</label>
									<button class="btn btn-darkblue">查詢</button>
								</form>
							</div>
						</div>
					</div>
					<div class="panel-content">
						<div class="datalistWrap">
							<div class="row search-result-wrap">
								<table id="consignment-note-table" class="result-table"></table>
							</div>
						</div>
					</div>					
				</div>
			</div>
		</div>
	</div>
	<jsp:include page="template/common_js.jsp" flush="true" />
	<script type="text/javascript" src="js/dataTables.buttons.min.js"></script>
	<script type="text/javascript" src="js/buttons.jqueryui.min.js"></script>
	<script type="text/javascript" src="js/buttons.colVis.min.js"></script>

	<script type="text/javascript">
		$('form').on('click', 'button', function(event){
			event.preventDefault();
			
			var $form = $('form');
			var order_no = $form.find('input[type=text]:eq(0)').val();
			var tracking_number = $form.find('input[type=text]:eq(1)').val();
	
			var parameter = {
					action: 'query_consignment_note',
					order_no: order_no,
					tracking_number: tracking_number
			};
			drawCNTable(parameter);
		});
	</script>
	<script type="text/javascript">
		function drawCNTable(parameter) {
	
			$("#consignment-note-table").DataTable({
			    dom: "frB<t>ip",
			    lengthChange: false,
			    pageLength: 20,
			    scrollY: "290px",
			    width: 'auto',
			    scrollCollapse: true,
			    destroy: true,
				language : {
					"url" : "js/dataTables_zh-tw.txt",
					"emptyTable" : "查無資料",
				},
				ajax : {
					url : "Egs.do",
					dataSrc : "",
					type : "POST",
					data : parameter
				},
		        buttons: [{
		        	text: '欄位控制',
					extend: 'colvis',
					collectionLayout: 'fixed two-column'
		        }],
				columns : [ {
					"title" : "連線契客代號",
					"data" : "customer_id",
					"defaultContent" : ""
				}, {
					"title" : "託運單號碼",
					"data" : "tracking_number",
					"defaultContent" : ""
				}, {
					"title" : "訂單編號",
					"data" : "order_no",
					"defaultContent" : ""
				}, {
					"title" : "收件人姓名",
					"data" : "receiver_name",
					"defaultContent" : ""
				}, {
					"title" : "收件人地址",
					"data" : "receiver_address",
					"defaultContent" : ""
				}, {
					"title" : "收件人地址的速達五碼郵遞區號",
					"data" : "receiver_suda5",
					"defaultContent" : ""
				}, {
					"title" : "速達七碼條碼",
					"data" : "receiver_suda7",
					"defaultContent" : ""
				}, {
					"title" : "收件人行動電話",
					"data" : "receiver_mobile",
					"defaultContent" : ""
				}, {
					"title" : "收件人電話",
					"data" : "receiver_phone",
					"defaultContent" : ""
				}, {
					"title" : "寄件人姓名",
					"data" : "sender_name",
					"defaultContent" : ""
				}, {
					"title" : "寄件人地址",
					"data" : "sender_address",
					"defaultContent" : ""
				}, {
					"title" : "寄件人地址的速達五碼郵遞區號",
					"data" : "sender_suda5",
					"defaultContent" : ""
				}, {
					"title" : "寄件人電話",
					"data" : "sender_phone",
					"defaultContent" : ""
				}, {
					"title" : "代收貨款金額",
					"data" : "product_price",
					"defaultContent" : ""
				}, {
					"title" : "品名",
					"data" : "product_name",
					"defaultContent" : ""
				}, {
					"title" : "備註",
					"data" : "egs_comment",
					"defaultContent" : ""
				}, {
					"title" : "尺寸",
					"data" : "package_size",
					"defaultContent" : ""
				}, {
					"title" : "溫層",
					"data" : "temperature",
					"defaultContent" : ""
				}, {
					"title" : "距離",
					"data" : "distance",
					"defaultContent" : ""
				}, {
					"title" : "指定配達日期",
					"data" : "delivery_date",
					"defaultContent" : ""
				}, {
					"title" : "指定配達時段",
					"data" : "delivery_timezone",
					"defaultContent" : ""
				}, {
					"title" : "建立時間",
					"data" : "create_time",
					"defaultContent" : ""
				}, {
					"title" : "列印時間",
					"data" : "print_time",
					"defaultContent" : ""
				}, {
					"title" : "託運單帳號",
					"data" : "account_id",
					"defaultContent" : ""
				} ]
			});
		}; 	
	</script>	
</body>
</html>