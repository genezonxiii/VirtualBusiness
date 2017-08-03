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
<title>手開發票</title>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<link rel="Shortcut Icon" type="image/x-icon" href="./images/Rockettheme-Ecommerce-Shop.ico" />
<jsp:include page="template/common_css.jsp" flush="true" />
<link rel="stylesheet" href="css/buttons.dataTables.min.css">
</head>
<body>
	<input type="hidden" id="glb_menu" value='<%=menu%>' />
	<input type="hidden" id="glb_privilege" value="<%=privilege%>" />

	<div class="page-wrapper">
		<jsp:include page="template/common_headfoot.jsp" flush="true" />

		<div class="content-wrap">
			<h2 class="page-title">手開發票</h2>

			<div class="panel-content">
				<div class="datalistWrap">
					<div class="input-field-wrap">
						<div class="form-wrap">
							<div class="form-row">
								<form>
									<label for=""> <span class="block-label">發票起日</span>
										<input type="text" class="input-date">
									</label>
									<div class="forward-mark"></div>
									<label for=""> <span class="block-label">發票迄日</span>
										<input type="text" class="input-date">
									</label>
									<button class="btn btn-darkblue">查詢</button>
									<button class="btn btn-exec">新增發票</button>
								</form>
							</div>
						</div>
					</div>
					<div class="panel-content">
						<div class="datalistWrap">
							<div class="row search-result-wrap">
								<table id="invoice-master-table" class="result-table"></table>
								<table id="invoice-detail-table" class="result-table"></table>
							</div>
						</div>
					</div>					
				</div>
			</div>
		</div>
	</div>

	<div id="dialog-invoice" style="display:none">
		<form>
			<fieldset>
				<table class='form-table'>
					<tr>
						<td>買受人</td><td><input type="text" name="title"></td>
					</tr>
					<tr>
						<td>發票類別</td>
						<td>
							<input id="invoice-type-radio-1" type="radio" name="invoice-type-radio-group">
							<label for="invoice-type-radio-1">
								<span class="form-label">二聯式</span>
							</label>
		          			<input id="invoice-type-radio-2" type="radio" name="invoice-type-radio-group">
		          			<label for="invoice-type-radio-2">
								<span class="form-label">三聯式</span>
		          			</label>
						</td>
					</tr>
					<tr>
						<td>統一編號</td><td><input type="text" name="unicode" placeholder="選擇三聯式方可輸入" disabled></td>
					</tr>
				</table>
			</fieldset>
		</form>
	</div> 
	<jsp:include page="template/common_js.jsp" flush="true" />
	<script type="text/javascript" src="js/dataTables.buttons.min.js"></script>
	<script type="text/javascript" src="js/buttons.jqueryui.min.js"></script>
	<script type="text/javascript" src="js/buttons.colVis.min.js"></script>

	<!-- Global Variables -->
	<script type="text/javascript">
		var $masterTable; //master datatable
		var $detailTable; //detail datatable
	</script>
	
	<!-- Listener -->
	<script type="text/javascript">
		$('.page-wrapper').on('click', '.btn-darkblue', function(event){
			event.preventDefault();
			
			var $form = $('form');
			var startDate = $form.find('input[type=text]:eq(0)').val();
			var endDate = $form.find('input[type=text]:eq(1)').val();
	
			var parameter = {
					action: 'query_invoice',
					startDate: startDate,
					endDate: endDate
			};
			drawInvMasTable(parameter);
		});
		$('.page-wrapper').on('click', '.btn-exec', function(event){
			event.preventDefault();
			
			var $dialog = $('#dialog-invoice');

			$('input:radio[name="invoice-type-radio-group"]').change(
				function(){
					console.log(this);
				    if ($(this).is(':checked') && this.id == 'invoice-type-radio-2') {
				    	$dialog.find('input[name="unicode"]').prop("disabled", false);
				    }else{
				    	$dialog.find('input[name="unicode"]').val('');
				    	$dialog.find('input[name="unicode"]').prop("disabled", true);
				    }
				}
			);			
			$dialog.dialog({
				draggable : true,
				resizable : false,
				height : "auto",
				width : "auto",
				modal : true,
				title : '新增發票',
				buttons : [{
							text : "儲存",
							click : function() {

							}
						}, {
							text : "取消",
							click : function() {

								$(this).dialog("close");
							}
						} ],
				close : function() {

				}
			});
		});		
	</script>
	
	<!-- Method -->
	<script type="text/javascript">
		function drawInvMasTable(parameter) {
			
			$masterTable = $("#invoice-master-table").DataTable({
			    dom: "fr<t>ip",
			    lengthChange: false,
			    pageLength: 20,
			    scrollY: "340px",
			    width: 'auto',
			    scrollCollapse: true,
			    destroy: true,
				language : {
					"url" : "js/dataTables_zh-tw.txt",
					"emptyTable" : "查無資料",
				},
				ajax : {
					url : "InvManual.do",
					dataSrc : "",
					type : "POST",
					data : parameter
				},
				columns : [{
					"title" : "發票類別",
					"data" : "invoice_type",
					"defaultContent" : ""
				},{
					"title" : "發票期別",
					"data" : "year_month",
					"defaultContent" : ""
				},{
					"title" : "發票號碼",
					"data" : "invoice_no",
					"defaultContent" : ""
				},{
					"title" : "發票日期",
					"data" : "invoice_date",
					"defaultContent" : ""
				},{
					"title" : "買受人",
					"data" : "title",
					"defaultContent" : ""
				},{
					"title" : "統一編號",
					"data" : "unicode",
					"defaultContent" : ""
				},{
					"title" : "總金額",
					"data" : "amount",
					"defaultContent" : ""
				}]
			});		
		};
	</script>	
</body>
</html>