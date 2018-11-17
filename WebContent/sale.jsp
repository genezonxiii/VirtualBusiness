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
<title>訂單管理</title>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<link rel="Shortcut Icon" type="image/x-icon" href="./images/Rockettheme-Ecommerce-Shop.ico" />
<link rel="stylesheet" href="css/buttons.dataTables.min.css">
<style>
#dialog-select-table td {
    text-align: center; /* center checkbox horizontally */
    vertical-align: middle; /* center checkbox vertically */
}
input.error[type=radio] + label {
    color: red;
	font-size: 15px;
	margin-top: 5px;
	border: 1px solid #e92500;
	background: rgb(255, 213, 204);
}
</style>
<jsp:include page="template/common_css.jsp" flush="true"/>
</head>
<body>
	<input type="hidden" id="glb_menu" value='<%= menu %>' />
	<input type="hidden" id="glb_privilege" value="<%= privilege %>" />
	
	<div class="page-wrapper">
	 	<jsp:include page="template/common_headfoot.jsp" flush="true"/>
		
		<div class="content-wrap">
			<h2 class="page-title">訂單管理</h2> 
		
			<div class='bdyplane' style="opacity: 0">
				<div class="panel-content">
					<div class="datalistWrap">
	
						<!--對話窗樣式-確認 -->
						<div id="dialog-confirm" title="是否刪除此銷貨資料?"></div>
	
						<!--對話窗樣式-修改 -->
						<div id="dialog-form-update" title="修改銷貨資料">
							<form name="update-dialog-form-post" id="update-dialog-form-post">
								<font color=red style="padding-left: 26px">掃條碼亦可取得商品資料</font>
								<fieldset>
									<table class='form-table'>
										<tr>
											<td>平台訂單號：</td>
											<td><input type="text" name="order_no"
												placeholder="輸入訂單號" disabled></td>
											<td>客戶名字：</td>
											<td><input type="text" name="name" 
												placeholder="輸入客戶名字"></td>
										</tr>
										<tr>
		        <td>對照類別</td>
	            <td>
		            <section>
			            <input id='update-radio-1' type='radio' name='radio-group-type' value='PRD'>
			            <label for='update-radio-1'><span class='form-label'>商品</span></label>
		
			            <input id='update-radio-2' type='radio' name='radio-group-type' value='PKG'>
			            <label for='update-radio-2'><span class='form-label'>組合包</span></label>
		            </section>
	            </td>
										</tr>
										<tr>
											<td>自訂商品ID：</td>
											<td><input type="text" id="update_c_product_id"
												name="c_product_id" placeholder="輸入自訂商品ID"></td>
											<td>商品名稱：</td>
											<td><input type="text" id="update_product_name"
												name="product_name" placeholder="輸入商品名稱"></td>
										</tr>
										<tr>
											<td>銷貨數量：</td>
											<td><input type="text" id="update_quantity"
												name="quantity" placeholder="輸入銷貨數量"></td>
											<td>單價：</td>
											<td><input type="text" id="update_price" name="price"
												placeholder="輸入單價"></td>
										</tr>
										<tr>
											<td>小計：</td>
											<td><input type="text" id="update_product_price"
												name="update_product_price" disabled></td>
										</tr>
										<tr  style="display:none;">
											<td>發票號碼：</td>
											<td><input type="text" name="invoice"
												placeholder="輸入發票號碼"></td>
											<td>發票日期：</td>
											<td><input type="text" name="invoice_date"
												placeholder="輸入發票日期" class="input-date"></td>
										</tr>
										<tr>
											<td>轉單日：</td>
											<td><input type="text" name="trans_list_date"
												placeholder="輸入轉單日" class="input-date"></td>
											<td>銷貨日：</td>
											<td><input type="text" name="sale_date"
												placeholder="輸入銷貨日" class="input-date"></td>
										</tr>
										<tr>
											<td>實際配送日：</td>
											<td>
												<input type="text" name="dis_date" placeholder="輸入配送日" class="input-date">
											</td>
											<td>銷售平台：</td>
											<td>
												<input type="text" name="order_source" placeholder="輸入銷售平台">
											</td>
										</tr>
										<tr>
											<td>備註說明：</td>
											<td>
												<input type="text" name="memo" placeholder="輸入備註說明">
											</td>
										</tr>
										<tr>
											<td colspan="4">
												<font color=red style="padding-left: 26px">※若訂單有多筆明細，請於訂單的最後一筆明細，完整填入下列資料。</font>
											</td>
										</tr>
										<tr>
											<td>訂單總金額：</td>
											<td><input type="text" id="update_total_amt"
												name="total_amt" value="0"></td>
										</tr>
										<tr>
											<td>收件人姓名：</td>
											<td><input type="text" name="deliver_name"></td>
											<td>收件人地址：</td>
											<td><input type="text" name="deliver_to"></td>
										</tr>
										<tr>
											<td>超商門市名稱：</td>
											<td><input type="text" name="deliver_store"></td>
											<td>物流備註：</td>
											<td><input type="text" name="deliver_note"></td>
										</tr>
										<tr>
											<td>收件人電話：</td>
											<td><input type="text" name="deliver_phone"></td>
											<td>收件人手機：</td>
											<td><input type="text" name="deliver_mobile"></td>
										</tr>
										<tr>
											<td>付款方式：</td>
											<td><input type="text" name="pay_kind"></td>
											<td>付款狀態：</td>
											<td><input type="text" name="pay_status"></td>
										</tr>
										<tr>
											<td>發票收件人姓名：</td>
											<td><input type="text" name="inv_name"></td>
											<td>發票收件人地址：</td>
											<td><input type="text" name="inv_to"></td>
										</tr>
										<tr>
											<td>電子郵件信箱：</td>
											<td><input type="text" name="email"></td>
											<td>信用卡末四碼：</td>
											<td><input type="text" name="credit_card"></td>
										</tr>
									</table>
								</fieldset>
							</form>
						</div>
						
						<!--對話窗樣式-發票日期 -->
						<div id="dialog-invoice" title="請選擇發票日期" style="display:none;">
							<form name="dialog-invoice-form-post" id="idialog-invoice-form-post" >
								<fieldset>
									<table class='form-table'>
										<tr>
											<td>發票日期：</td>
											<td><input type="text" id="invoice_num_date"
												class="input-date"></td>
										</tr>
									</table>
								</fieldset>
							</form>
						</div>
						
						<!--對話窗樣式-取消開立發票 -->
						<div id="dialog-invoice-cancel" title="取消開立發票原因" style="display:none;">
							<form name="dialog-invoice-cancel-form-post" id="dialog-invoice-cancel-form-post" >
								<fieldset>
									<table class='form-table'>
										<tr>
											<td>取消原因：</td>
											<td><input type="text" id="invoice_cancel_reason"></td>
										</tr>
									</table>
								</fieldset>
							</form>
						</div>
	
						<!--對話窗樣式-新增 -->
						<div id="dialog-form-insert" title="新增銷貨資料">
							<form name="insert-dialog-form-post" id="insert-dialog-form-post">
								<font color=red style="padding-left: 26px">掃條碼亦可取得商品資料</font>
								<fieldset>
									<table class='form-table'>
										<tr>
											<td>銷貨單號：</td>
											<td><input type="text" name="original_seq_no"
												disabled="disabled" value="系統自動產生"></td>
											<td>平台訂單號：</td>
											<td><input type="text" name="order_no"
												placeholder="未輸入將代入銷貨單號"></td>
										</tr>
										<tr>
											<td>客戶名字：</td>
											<td><input type="text" name="name" placeholder="輸入客戶名字"></td>
										</tr>
										<tr>
		        <td>對照類別</td>
	            <td>
		            <section>
			            <input id='insert-radio-1' type='radio' name='radio-group-type' value='PRD'>
			            <label for='insert-radio-1'><span class='form-label'>商品</span></label>
		
			            <input id='insert-radio-2' type='radio' name='radio-group-type' value='PKG'>
			            <label for='insert-radio-2'><span class='form-label'>組合包</span></label>
		            </section>
	            </td>
										</tr>
										<tr>
											<td>自訂商品ID：</td>
											<td><input type="text" id="insert_c_product_id"
												name="c_product_id" placeholder="輸入自訂商品ID"></td>
											<td>商品名稱：</td>
											<td><input type="text" id="insert_product_name"
												name="product_name" placeholder="輸入商品名稱"></td>
										</tr>
										<tr>
											<td>銷貨數量：</td>
											<td><input type="text" id="insert_quantity"
												name="quantity" placeholder="輸入銷貨數量"></td>
											<td>單價：</td>
											<td><input type="text" id="insert_price" name="price"
												placeholder="輸入單價"></td>
										</tr>
										<tr style="display: none;">
											<td>小計：</td>
											<td><input type="hidden" id="insert_product_price"
												name="insert_product_price" placeholder="系統自動產生金額" disabled></td>
										</tr>
										<tr style="display:none;">
											<td>發票號碼：</td>
											<td><input type="text" name="invoice"
												placeholder="輸入發票號碼"></td>
											<td>發票日期：</td>
											<td><input type="text" name="invoice_date"
												placeholder="輸入發票日期" class="input-date"></td>
										</tr>
										<tr>
											<td>轉單日：</td>
											<td><input type="text" name="trans_list_date"
												placeholder="輸入轉單日" class="input-date"></td>
											<td>銷貨/出貨日期：</td>
											<td><input type="text" name="sale_date"
												placeholder="輸入銷貨/出貨日期" class="input-date"></td>
										</tr>
										<tr>
											<td>銷售平台：</td>
											<td><input type="text" name="order_source"
												placeholder="輸入銷售平台"></td>
											<td>備註說明：</td>
											<td><input type="text" name="memo" placeholder="輸入備註說明"></td>
											<td style="display: none">配送日：</td>
											<td style="display: none"><input type="text"
												name="dis_date" placeholder="輸入配送日" class="input-date"
												value=""></td>
										</tr>
										<tr>
											<td colspan="4">
												<font color=red style="padding-left: 26px">※若訂單有多筆明細，請於訂單的最後一筆明細，完整填入下列資料。</font>
											</td>
										</tr>
										<tr>
											<td>訂單總金額：</td>
											<td><input type="text" id="insert_total_amt"
												name="total_amt" value="0"></td>
										</tr>
										<tr>
											<td>收件人姓名：</td>
											<td><input type="text" name="deliver_name"></td>
											<td>收件人地址：</td>
											<td><input type="text" name="deliver_to"></td>
										</tr>
										<tr>
											<td>超商門市名稱：</td>
											<td><input type="text" name="deliver_store"></td>
											<td>物流備註：</td>
											<td><input type="text" name="deliver_note"></td>
										</tr>
										<tr>
											<td>收件人電話：</td>
											<td><input type="text" name="deliver_phone"></td>
											<td>收件人手機：</td>
											<td><input type="text" name="deliver_mobile"></td>
										</tr>
										<tr>
											<td>付款方式：</td>
											<td><input type="text" name="pay_kind"></td>
											<td>付款狀態：</td>
											<td><input type="text" name="pay_status"></td>
										</tr>
										<tr>
											<td>發票收件人姓名：</td>
											<td><input type="text" name="inv_name"></td>
											<td>發票收件人地址：</td>
											<td><input type="text" name="inv_to"></td>
										</tr>
										<tr>
											<td>電子郵件信箱：</td>
											<td><input type="text" name="email"></td>
											<td>信用卡末四碼：</td>
											<td><input type="text" name="credit_card"></td>
										</tr>
									</table>
								</fieldset>
							</form>
						</div>
						
						<div id="dialog-form-select" style="display:none;">
							<form name="dialog-form-post-select" id="dialog-form-post-select" >
								<fieldset>
									<table class='result-table' id="dialog-select-table">
										<thead>
											<tr>
												<th></th>
												<th>自訂商品ID</th>
												<th>商品名稱</th>
												<th>規格名稱</th>
											</tr>
										</thead>									
									</table>
								</fieldset>
							</form>
						</div>
	
						<div class="input-field-wrap">
							<div class="form-wrap">
								<div class="form-row">
									<form id="trans_list_date_form" name="trans_list_date_form">
										<label for=""> <span class="block-label">轉單起日</span> <input
											type="text" class="input-date" id="trans_list_start_date"
											name="trans_list_start_date">
										</label>
										<div class="forward-mark"></div>
										<label for=""> <span class="block-label">轉單迄日</span> <input
											type="text" class="input-date" id="trans_list_end_date"
											name="trans_list_end_date">
										</label>
										<button class="btn btn-darkblue" id="searh-trans-list-date">查詢</button>
									</form>
								</div>

								<div class="form-row">
									<form id="upload_date_form" name="upload_date_form">
										<label for=""> <span class="block-label">上傳起日</span> <input
											type="text" class="input-date" id="upload_start_date"
											name="upload_start_date">
										</label>
										<div class="forward-mark"></div>
										<label for=""> <span class="block-label">上傳迄日</span> <input
											type="text" class="input-date" id="upload_end_date"
											name="upload_end_date">
										</label>
										<button class="btn btn-darkblue" id="search-upload-date">查詢</button>
									</form>
								</div>
	
								<div class="btn-row">
									<button class="btn btn-exec btn-wide" id="create-sale">新增銷售資料</button>
								</div>
							</div>
						</div>
						<div class="row search-result-wrap" align="center"
							id="sales_contain_row" style="display: none;">
							<div id="sales-contain" class="ui-widget">
								<table id="sales" class="result-table">
									<thead>
										<tr class="">
											<th>批次請求</th>
											<th>可轉銷單</th>
											<th>銷貨單號</th>
											<th>訂單號</th>
											<th>商品 ID/名稱</th>
											<th>數量</th>
											<th>銷貨金額</th>
											<th>銷貨對象</th>
											<th>發票</th>
											<th>轉單日</th>
											<th>銷/出貨日期</th>
											<th>上傳日期</th>
											<th>銷售平台</th>
											<th>註</th>
											<th>功能</th>
										</tr>
									</thead>
									<tbody>
									</tbody>
								</table>
							</div>
							<div class="validateTips" id="err_msg" align="center"></div>
						</div>
					</div>
				</div>
			</div>
		</div>
	</div>

	<div id="warning" style="display: none; color: #f00; font-size: 28px;"></div>
	<div id="message" align="center">
		<div id="text"></div>
	</div>

	<!-- 銷貨明細對話窗 -->
	<div id="dialog-sale-detail" class="dialog" align="center">
		<form name="dialog-form-sale-detail" id="dialog-form-sale-detail">
			<fieldset>
				<table id="dialog-sale-detail-table" class="result-table">
					<thead></thead>
					<tbody></tbody>
				</table>
			</fieldset>
		</form>
	</div>
	<!-- 出貨報表 對話窗 -->
	<div id="dialog_report" class="dialog" align="center">
		<iframe src="" frameborder="0" id="dialog_report_iframe" width="850" height="450"></iframe>
	</div>
	
	<jsp:include page="template/common_js.jsp" flush="true"/>
	<script type="text/javascript" src="js/dataTables.buttons.min.js"></script>
	<script type="text/javascript" src="js/buttons.jqueryui.min.js"></script>
	<script type="text/javascript" src="js/sale.js"></script>
</body>
</html>