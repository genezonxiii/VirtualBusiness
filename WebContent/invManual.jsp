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
<title>B2B發票</title>
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
			<h2 class="page-title">B2B發票</h2>
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
								</form>
							</div>
<!-- 							<div class="form-row"> -->
<!-- 								<button class="btn btn-exec">新增發票</button> -->
<!-- 							</div> -->
						</div>
					</div>
					<div class="panel-content">
						<div class="datalistWrap">
							<div class="row search-result-wrap">
								<div id = "masterTable">
									<table id="invoice-master-table" class="result-table"></table>
								</div>
								<div id = "detailTable">
									<table id="invoice-detail-table" class="result-table"></table>
								</div>
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
<!-- 					<tr> -->
<!-- 						<td>發票類別</td> -->
<!-- 						<td> -->
<!-- 							<input id="invoice-type-radio-1" type="radio" name="invoice-type-radio-group"> -->
<!-- 							<label for="invoice-type-radio-1"> -->
<!-- 								<span class="form-label">二聯式</span> -->
<!-- 							</label> -->
<!-- 		          			<input id="invoice-type-radio-2" type="radio" name="invoice-type-radio-group"> -->
<!-- 		          			<label for="invoice-type-radio-2"> -->
<!-- 								<span class="form-label">三聯式</span> -->
<!-- 		          			</label> -->
<!-- 		          			<div id = 'validate-invoice-type-radio'></div> -->
<!-- 						</td> -->
<!-- 					</tr> -->
					<tr>
<!-- 						<td>買受人</td><td><input type="text" name="title" placeholder="選擇三聯式方可輸入" disabled></td> -->
						<td>買受人</td><td><input type="text" name="title"></td>
					</tr>
					<tr>
<!-- 						<td>統一編號</td><td><input type="text" name="unicode" placeholder="選擇三聯式方可輸入" disabled></td> -->
						<td>統一編號</td><td><input type="text" name="unicode"></td>
					</tr>
					<tr>
						<td>地址</td><td><input type="text" name="address"></td>
					</tr>
					<tr>
						<td>備註</td><td><input type="text" name="memo"></td>
					</tr>
					<tr>
						<td>課稅別</td>
						<td>
							<input id="invoice-tax-type-radio-1" type="radio" name="invoice-tax-type-radio-group" value="1">
							<label for="invoice-tax-type-radio-1">
								<span class="form-label">應稅</span>
							</label>
		          			<input id="invoice-tax-type-radio-2" type="radio" name="invoice-tax-type-radio-group" value="2">
		          			<label for="invoice-tax-type-radio-2">
								<span class="form-label">零稅率</span>
		          			</label>
		          			<input id="invoice-tax-type-radio-3" type="radio" name="invoice-tax-type-radio-group" value="3">
		          			<label for="invoice-tax-type-radio-3">
								<span class="form-label">免稅</span>
		          			</label>
<!-- 		          			<input id="invoice-tax-type-radio-4" type="radio" name="invoice-tax-type-radio-group"> -->
<!-- 		          			<label for="invoice-tax-type-radio-4"> -->
<!-- 								<span class="form-label">應稅(特種稅率)</span> -->
<!-- 		          			</label> -->
<!-- 		          			<input id="invoice-tax-type-radio-9" type="radio" name="invoice-tax-type-radio-group"> -->
<!-- 		          			<label for="invoice-tax-type-radio-9"> -->
<!-- 								<span class="form-label">混合應稅與免稅或零稅率(限收銀機發票無法分辨時使用)</span> -->
<!-- 		          			</label> -->
		          			<div id = 'validate-invoice-tax-type-radio'></div>
						</td>
					</tr>
					<tr>
						<td>發票日期</td>
						<td>
							<input type="text" name="invoice_date" class="input-date">
						</td>
					</tr>
					<tr>
						<td>總計</td>
						<td>
							<input type="text" name="amount_plustax" value="0" disabled>
						</td>
					</tr>
					<tr>
						<td>銷售額合計(未稅)</td>
						<td>
							<input type="text" name="amount" value="0" disabled>
						</td>
					</tr>
					<tr>
						<td>營業稅額</td>
						<td>
							<input type="text" name="tax" value="0">
						</td>
					</tr>
				</table>
			</fieldset>
		</form>
	</div>
	
	<div id="dialog-invoice-detail" style="display:none">
		<form>
			<fieldset>
				<table class='form-table'>
					<tr>
						<td>品名</td><td><input type="text" name="description"></td>
					</tr>
					<tr>
						<td>單價</td><td><input type="text" name="price"></td>
					</tr>
					<tr>
						<td>數量</td><td><input type="text" name="quantity"></td>
					</tr>
					<tr>
						<td>小計</td><td><input type="text" name="subtotal" placeholder="系統自動產生" disabled></td>
					</tr>
					<tr>
						<td>備註</td><td><input type="text" name="memo"></td>
					</tr>
				</table>
			</fieldset>
		</form>
	</div>
	<!--對話窗樣式-作廢發票 -->
	<div id="dialog-invoice-cancel" title="作廢發票" style="display:none;">
		<form name="dialog-invoice-cancel-form-post" id="dialog-invoice-cancel-form-post" >
			<fieldset>
				<table class='form-table'>
					<tr>
						<td>作廢原因：</td>
						<td><input type="text" id="invoice_cancel_reason" name="invoice_cancel_reason"></td>
					</tr>
				</table>
			</fieldset>
		</form>
	</div>

	<!-- 辦別是否開立 flag-->
	<input type="hidden" id="inv_flag">
	<!-- 報表 對話窗 -->
	<div id="dialog_report" class="dialog" align="center" style="display:none">
		<iframe src="" frameborder="0" id="dialog_report_iframe" width="850" height="450"></iframe>
	</div>
	
	<jsp:include page="template/common_js.jsp" flush="true" />
	<script type="text/javascript" src="js/dataTables.buttons.min.js"></script>
	<script type="text/javascript" src="js/buttons.jqueryui.min.js"></script>
	<script type="text/javascript" src="js/buttons.colVis.min.js"></script>
	<!-- Initialization -->
	<script type="text/javascript" src="js/page/invManual_1.js"></script>
	<!-- Global Variables -->
	<script type="text/javascript" src="js/page/invManual_2.js"></script>
	<!-- Jquery Validate -->
	<script type="text/javascript" src="js/page/invManual_3.js"></script>
	<!-- Listener -->
	<script type="text/javascript" src="js/page/invManual_4.js"></script>
	<!-- Method -->
	<script type="text/javascript" src="js/page/invManual_5.js"></script>
</body>
</html>