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
<title>出貨流程</title>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<link rel="Shortcut Icon" type="image/x-icon" href="./images/Rockettheme-Ecommerce-Shop.ico" />
<jsp:include page="template/common_css.jsp" flush="true" />
<style>
.realsale_table {
	margin: auto;
	border-collapse: separate;
	border-spacing: 10px 20px;
	font-family: "微軟正黑體", "Microsoft JhengHei", 'LiHei Pro', Arial,
		Helvetica, sans-serif, \5FAE\8EDF\6B63\9ED1\9AD4, \65B0\7D30\660E\9AD4;
	*/
}

input[type="number"] {
	background: #efefef;
	border: 1px solid #999;
	font-size: 16px;
	padding: 5px;
	font-family: "微軟正黑體", "Microsoft JhengHei", 'LiHei Pro', Arial,
		Helvetica, sans-serif, \5FAE\8EDF\6B63\9ED1\9AD4, \65B0\7D30\660E\9AD4;
}

input[type="number"]:disabled {
	background-color: #cccccc;
}

button:disabled, button[disabled] {
	border: 1px solid #999999;
	background-color: #cccccc;
	color: #666666;
	pointer-events: none;
}

.side_by_side_div {
	width: 230px;
	height: 200px;
	line-height: 20px;
	padding: 5px;
	float: left;
}

.on_div {
	width: 200px;
	height: 170px;
}

.under_div {
	display: inline;
	width: 200px;
	height: 30px;
}

.under_div button {
	margin-left: 20px;
}

.under_div .forward-mark {
	margin-left: 45px;
}

.down_div {
	padding: 10px
}

.fast_div{
    padding: 50px;
}

#dialog-form-fast{
line-height: 25px;
}


</style>
</head>
<body>
	<input type="hidden" id="glb_menu" value='<%=menu%>' />
	<input type="hidden" id="glb_privilege" value="<%=privilege%>" />

	<div class="page-wrapper">
		<jsp:include page="template/common_headfoot.jsp" flush="true" />

		<div class="content-wrap">
			<h2 class="page-title">出貨流程</h2>

			<div class='bdyplane' style="opacity: 0">
				<div class="panel-content">
					<div class="datalistWrap">
						<div class="input-field-wrap">
							<div class="form-wrap">
								<div class="form-row fast_div">
									<label for=""> <span class="block-label">合併訂單數量：</span> <input type="number" id="fast_order_count" name="import_order_count" class="ui-autocomplete-input" value="5">
									</label><br/> <label for=""> <span class="block-label">轉單日期區間：</span> <input type="text" class="input-date" id="fast_trans_list_date_begin" name="fast_trans_list_date_begin">
										<div class="forward-mark"></div> <input type="text" class="input-date" id="fast_trans_list_date_end" name="import_trans_list_date_end">
									</label> <br /> <br />
									<button class="btn btn-exec btn-wide" id="fast_button">快速出貨</button>
								</div>
			
								
								<div class="form-row" id="L2R">

									<div class="side_by_side_div">
										<div class="on_div">
											<label for=""> <span class="block-label">轉單日期區間：</span> <input type="text" class="input-date" id="import_trans_list_date_begin" name="import_trans_list_date_begin">
												<div class="down_div">
													<img src="./images/downdown.png">
												</div> <input type="text" class="input-date" id="import_trans_list_date_end" name="import_trans_list_date_end">
											</label>
										</div>
										<div class="under_div">
											<span>
												<button class="btn btn-exec btn-wide" id="import_resale">轉入銷貨</button>

											</span> <span class="forward-mark"></span>
										</div>

									</div>
									<div class="side_by_side_div">
										<div class="on_div"></div>
										<div class="under_div">
											<span>
												<button class="btn btn-exec btn-wide" id="import_alloc_inv">轉入待出庫</button>
											</span> <span class="forward-mark"></span>
										</div>
									</div>
									<div class="side_by_side_div">
										<div class="on_div"></div>
										<div class="under_div">
											<button class="btn btn-exec btn-wide" id="statistics_alloc_inv">執行配庫</button>
											<span class="forward-mark"></span>
										</div>
									</div>
									<div class="side_by_side_div">
										<div class="on_div">
											<label for=""> <span class="block-label">合併訂單數量：</span> <input type="number" id="import_order_count" name="import_order_count" class="ui-autocomplete-input" value="5">
											</label>
										</div>
										<div class="under_div">
											<span>
												<button class="btn btn-exec btn-wide" id="import_picking">轉入揀貨</button>
											</span> <span class="forward-mark"></span>
										</div>

									</div>

									<div class="side_by_side_div">
										<div class="on_div"></div>

										<div class="under_div">
											<button class="btn btn-exec btn-wide" id="import_ship">轉入出貨</button>
										</div>
									</div>

								</div>

							</div>
						</div>
						<div class="row search-result-wrap" align="center" id="sales_contain_row" style="display: none;">
							<div id="sales-contain" class="ui-widget">
								<table id="sales" class="result-table">
									<thead>
										<tr class="">
											<th>訂單編號</th>
											<th>客戶姓名</th>
											<th>轉單日</th>
											<th>銷貨日</th>
											<th>出貨日</th>
											<th>銷貨平台</th>
											<th style="background-image: none !important;">備註</th>
											<th style="background-image: none !important;">功能</th>
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
	
	
	
		<!--對話窗樣式-快速出貨 -->
			<div id="dialog-form-fast" title="快速出貨" style="display: none;">
				您將快速出貨，轉單日期區間為:<br/>
				<sapn id="dialog_fast_trans_list_date_begin"></sapn>到<sapn id=dialog_fast_trans_list_date_end></sapn>訂單。<br/>
				合併訂單數量:	<sapn id="dialog_fast_order_count"></sapn><br/>
				
			</div>

	<!-- 銷貨明細對話窗 -->
	<div id="dialog-sale-detail" class="dialog" align="center">
		<form name="dialog-form-sale-detail" id="dialog-form-sale-detail">
			<fieldset>
				<table id="dialog-sale-detail-table" class="result-table">
					<thead>
						<tr class="">
							<th>訂單編號</th>
							<th>自訂商品編號</th>
							<th>商品名稱</th>
							<th>數量</th>
							<th>單價</th>
							<th>備註</th>
						</tr>
					</thead>
					<tfoot></tfoot>
					<tbody></tbody>
				</table>
			</fieldset>
		</form>
	</div>

	<jsp:include page="template/common_js.jsp" flush="true" />
	<script type="text/javascript" src="js/jquery.blockUI.js"></script>

	<script>
		$(function() {
			$("#import_alloc_inv").attr('disabled', true);
			$("#statistics_alloc_inv").attr('disabled', true);
			$("#import_order_count").attr('disabled', true);
			$("#import_picking").attr('disabled', true);
			$("#import_ship").attr('disabled', true);
		});
			
         

		var customer_menu = [];
		var customer_tags = [];

		function draw_sale(parameter, process_name, msg) {
			$("#sales_contain_row").css({
				"opacity" : "0"
			});
			warning_msg("---讀取中請稍候---");

			console.log('ajax start');

			var oColumnDefs = [ {
				//功能
				targets : -1,
				searchable : false,
				orderable : false,
				render : function(data, type, row) {

					var options = $("<div/>") //fake tag
					.append($("<div/>", {
						"class" : "table-row-func btn-in-table btn-gray"
					}).append($("<i/>", {
						"class" : "fa fa-ellipsis-h"
					})).append($("<div/>", {
						"class" : "table-function-list"
					}).append($("<button/>", {
						"id" : row.order_no,
						"value" : row.order_no,
						"name" : row.order_no,
						"class" : "btn-in-table btn-darkblue btn_update",
						"title" : "修改"
					}).append($("<i/>", {
						"class" : "fa fa-pencil"
					}))).append($("<button/>", {
						"id" : row.order_no,
						"value" : row.order_no,
						"name" : row.order_no,
						"class" : "btn-in-table btn-alert btn_delete",
						"title" : "刪除"
					}).append($("<i/>", {
						"class" : "fa fa-trash"
					}))).append($("<button/>", {
						"class" : "btn-in-table btn-green btn_list",
						"title" : "清單"
					}).append($("<i/>", {
						"class" : "fa fa-pencil-square-o"
					})))));

					return options.html();
				}
			} ];

			var oColumns = [ {
				"data" : "order_no",
				"width" : "10%",
				"defaultContent" : ""
			}, {
				"data" : "name",
				"width" : "5%",
				"defaultContent" : ""
			}, {
				"data" : "trans_list_date",
				"width" : "10%",
				"defaultContent" : ""
			}, {
				"data" : "sale_date",
				"width" : "10%",
				"defaultContent" : ""
			}, {
				"data" : "dis_date",
				"width" : "10%",
				"defaultContent" : ""
			}, {
				"data" : "order_source",
				"width" : "5%",
				"defaultContent" : ""
			}, {
				"data" : "memo",
				"width" : "10%",
				"defaultContent" : ""
			}, {
				"data" : null,
				"width" : "10%",
				"defaultContent" : ""
			} ];

			var dataTableObj = $("#sales").DataTable({
				dom : "lfr<t>ip",
				destroy : true,
				language : {
					"url" : "js/dataTables_zh-tw.txt"
				},
				ajax : {
					url : "realsale.do",
					dataSrc : "",
					type : "POST",
					data : parameter
				},
				columnDefs : oColumnDefs,
				columns : oColumns
			});

			$("#sales_contain_row").show().animate({
				"opacity" : "0.01"
			}, 1).animate({
				"opacity" : "1"
			}, 300);

			dialogMsg(process_name, msg);
		}

		function showBlockUI() {
		    $.blockUI({
		        message: '<table><tr><td valign="middle" style="height:150px;width:200px" >'+
		        	'<img src="images/ajax-loader.gif" />處理中,請稍候...</td></tr></table>',
		        css: {
		            width: '200px',
		            height: '150px',
		            top: '40%',
		            left: '43%'
		        },
		        timeout: 60000
		    });
		}

		var scan_exist = 0, new_or_edit = 0; //1: insert, 2: update
		$(function() {
			$(window).scannerDetection();
			$(window)
					.bind(
							'scannerDetectionComplete',
							function(e, data) {
								if (data.string == "success") {
									return;
								}

								$
										.ajax({
											url : "product.do",
											type : "POST",
											cache : false,
											data : {
												action : "find_barcode",
												barcode : data.string,
											},
											success : function(result) {
												var json_obj = $
														.parseJSON(result);
												var result_table = "";
												$
														.each(
																json_obj,
																function(i,
																		item) {
																	if (new_or_edit == 1) {
																		//new_or_edit=3;
																		$(
																				"#insert_product_name")
																				.val(
																						json_obj[i].product_name);
																		$(
																				"#insert_c_product_id")
																				.val(
																						json_obj[i].c_product_id);
																		$(
																				"#insert_quantity")
																				.val(
																						json_obj[i].keep_stock);
																		$(
																				"#insert_price")
																				.val(
																						json_obj[i].cost);
																		$(
																				"#insert_product_price")
																				.val(
																						json_obj[i].cost);
																	}
																	if (new_or_edit == 2) {
																		//new_or_edit=3;
																		$(
																				"#update_product_name")
																				.val(
																						json_obj[i].product_name);
																		$(
																				"#update_c_product_id")
																				.val(
																						json_obj[i].c_product_id);
																		$(
																				"#update_quantity")
																				.val(
																						json_obj[i].keep_stock);
																		$(
																				"#update_price")
																				.val(
																						json_obj[i].cost);
																		$(
																				"#update_product_price")
																				.val(
																						json_obj[i].cost);
																	}
																});
												if (json_obj.length == 0) {
													$("#warning")
															.html(
																	"<h3>該條碼無商品存在</h3>請至'商品管理'介面&nbsp;定義該條碼。");
													$("#warning")
															.dialog("open");
												}
											}
										});
							}).bind('scannerDetectionError', function(e, data) {

					}).bind('scannerDetectionReceive', function(e, data) {

					});
			$(window).scannerDetection('success');
		});

		$(function() {

			$(".bdyplane").animate({
				"opacity" : "1"
			});

			var uuid = "";
			var order_no_begin = "";
			var order_no_end = "";
			var seqNo = "";

			//--新增--
			var order_no = "";
			var order_source = "";
			var name = "";
			var total_amt = "";
			var trans_list_date = "";
			var invoice = "";
			var sale_date = "";
			var invoice_date = "";
			var dis_date = "";
			var memo = "";

			//=============自定義validator=============
			//字符最大長度驗證（一個中文字符長度為2）
			jQuery.validator.addMethod("stringMaxLength", function(value,
					element, param) {
				var length = value.length;
				for (var i = 0; i < value.length; i++) {
					if (value.charCodeAt(i) > 127) {
						length++;
					}
				}
				return this.optional(element) || (length <= param);
			}, $.validator.format("長度不能大於{0}!"));

			//字母數字
			jQuery.validator.addMethod("alnum", function(value, element) {
				return this.optional(element) || /^[a-zA-Z0-9]+$/.test(value);
			}, "只能包括英文字母和數字");
			//=========================================

			//驗證
			$("#trans_list_date_form").validate({
				rules : {
					trans_list_start_date : {
						dateISO : true
					},
					trans_list_end_date : {
						dateISO : true
					}
				},
				messages : {
					trans_list_start_date : {
						dateISO : "日期格式錯誤"
					},
					trans_list_end_date : {
						dateISO : "日期格式錯誤"
					}
				}
			});
			$("#trans_dis_date_form").validate({
				rules : {
					trans_dis_start_date : {
						dateISO : true
					},
					trans_dis_end_date : {
						dateISO : true
					}
				},
				messages : {
					trans_dis_start_date : {
						dateISO : "日期格式錯誤"
					},
					trans_dis_end_date : {
						dateISO : "日期格式錯誤"
					}
				}
			});

			var oRules = {
				order_no : {
					stringMaxLength : 20
				},
				product_name : {
					maxlength : 80,
					required : true
				},
				c_product_id : {
					stringMaxLength : 40
				},
				name : {
					stringMaxLength : 80
				},
				quantity : {
					required : true,
					digits : true
				},
				price : {
					required : true,
					number : true,
					min : 1
				},
				invoice : {
					stringMaxLength : 12,
					alnum : true
				},
				invoice_date : {
					dateISO : true
				},
				trans_list_date : {
					dateISO : true
				},
				dis_date : {
					dateISO : true
				},
				memo : {
					stringMaxLength : 200
				},
				sale_date : {
					required : true,
					dateISO : true
				},
				order_source : {
					stringMaxLength : 30
				}
			};

			var validator_insert = $("#insert-dialog-form-post").validate({
				rules : oRules
			});

			var validator_update = $("#update-dialog-form-post").validate({
				rules : oRules
			});

			//20170426從sale匯入到realsale---------------------------------
			$("#import_resale")
					.click(
							function(e) {
								e.preventDefault();
								$("#import_resale").attr('disabled', true);
								if ($("#import_trans_list_date_begin").val() == ''
										|| $("#import_trans_list_date_end")
												.val() == '') {
									dialogMsg('提醒', '請輸入日期');
									$("#import_resale").attr('disabled', false);
									return;
								}
							
								$
										.ajax({
											type : 'POST',
											url : 'shippingProcess.do',
											data : {
												action : "importData",
												import_trans_list_date_begin : $(
														"#import_trans_list_date_begin")
														.val(),
												import_trans_list_date_end : $(
														"#import_trans_list_date_end")
														.val()
											},beforeSend : function(){
												showBlockUI();
												$("#import_resale").attr('disabled', false);
											},complete:function(){
												$.unblockUI();
											},
											success : function(result) {
										
												var obj = jQuery
														.parseJSON(result);
												var order_no_cnt = "轉入訂單數："
														+ obj.order_no_cnt;
												var total_cnt = "<br/> 轉入訂單商品明細數："
														+ obj.total_cnt;

												dialogMsg('轉入銷貨', order_no_cnt
														+ total_cnt);

												$(
														"#import_trans_list_date_begin")
														.attr('disabled', true);
												$("#import_trans_list_date_end")
														.attr('disabled', true);
												$("#import_resale").attr(
														'disabled', true);
												$("#import_alloc_inv").attr(
														'disabled', false);
												$("#statistics_alloc_inv")
														.attr('disabled', true);
												$("#import_order_count").attr(
														'disabled', true);
												$("#import_picking").attr(
														'disabled', true);
												$("#import_ship").attr(
														'disabled', true);

											}
										});
							});

			//20170427從realsale匯入到alloc_inv---------------------------------
			$("#import_alloc_inv").click(
					function(e) {
						e.preventDefault();
						$("#import_alloc_inv").attr('disabled', true);

						$
								.ajax({
									type : 'POST',
									url : 'shippingProcess.do',
									data : {
										action : "importallocinvData"
									},beforeSend : function(){
										showBlockUI();
										$("#import_alloc_inv").attr('disabled', true);
									},complete:function(){
										$.unblockUI();
									},
									success : function(result) {
										var obj = jQuery.parseJSON(result);
										var order_no_cnt = "待出庫訂單數："
												+ obj.order_no_cnt;
										var total_cnt = "<br/> 待出庫商品明細數："
												+ obj.total_cnt;

										dialogMsg('轉入待出庫', order_no_cnt
												+ total_cnt);

										$("#import_trans_list_date_begin")
												.attr('disabled', true);
										$("#import_trans_list_date_end").attr(
												'disabled', true);
										$("#import_resale").attr('disabled',
												true);
										$("#import_alloc_inv").attr('disabled',
												true);
										$("#statistics_alloc_inv").attr(
												'disabled', false);
										$("#import_order_count").attr(
												'disabled', true);
										$("#import_picking").attr('disabled',
												true);
										$("#import_ship")
												.attr('disabled', true);

									}
								});
					});

			//20170504 做配庫alloc_inv---------------------------------
			$("#statistics_alloc_inv").click(
					function(e) {
						$("#statistics_alloc_inv").attr('disabled', false);
						$.ajax({
							type : 'POST',
							url : 'shippingProcess.do',
							data : {
								action : "statisticsAllocinvData"
							},beforeSend : function(){
								showBlockUI();
								$("#statistics_alloc_inv").attr('disabled', true);
							},complete:function(){
								$.unblockUI();
							},
							success : function(result) {
								var obj = jQuery.parseJSON(result);
								var order_no_cnt = "出庫訂單數：" + obj.order_no_cnt;
								var total_cnt = "<br/>  出庫商品明細數："
										+ obj.total_cnt;

								dialogMsg('執行配庫', order_no_cnt + total_cnt);
								$("#import_trans_list_date_begin").attr(
										'disabled', true);
								$("#import_trans_list_date_end").attr(
										'disabled', true);
								$("#import_resale").attr('disabled', true);
								$("#import_alloc_inv").attr('disabled', true);
								$("#statistics_alloc_inv").attr('disabled',
										true);
								$("#import_order_count")
										.attr('disabled', false);
								$("#import_picking").attr('disabled', false);
								$("#import_ship").attr('disabled', true);
							},
						});

					});

			//20170504 揀貨---------------------------------
			$("#import_picking")
					.click(
							function(e) {
								$("#import_picking").attr('disabled', true);
								var import_order_count = $(
										"#import_order_count").val();
								if (!(import_order_count == "")) {
									$
											.ajax({
												type : 'POST',
												url : 'shippingProcess.do',
												data : {
													order_no_count : import_order_count,
													action : "importPicking"
												},beforeSend : function(){
													showBlockUI();
													$("#import_picking").attr('disabled', false);
												},complete:function(){
													$.unblockUI();
												},
												success : function(result) {
													var obj = jQuery
															.parseJSON(result);
													var isSuccess = obj.isSuccess ? "成功"
															: "失敗";
													dialogMsg('轉入揀貨', "轉入揀貨作業："
															+ isSuccess);

													$(
															"#import_trans_list_date_begin")
															.attr('disabled',
																	true);
													$(
															"#import_trans_list_date_end")
															.attr('disabled',
																	true);
													$("#import_resale").attr(
															'disabled', true);
													$("#import_alloc_inv")
															.attr('disabled',
																	true);
													$("#statistics_alloc_inv")
															.attr('disabled',
																	true);
													$("#import_order_count")
															.attr('disabled',
																	true);
													$("#import_picking").attr(
															'disabled', true);
													$("#import_ship").attr(
															'disabled', false);
												}
											});
								} else {
									dialogMsg("轉入揀貨", "請輸入揀貨單訂單數量");
								}

							});

			//20170517  出貨---------------------------------
			$("#import_ship")
					.click(
							function(e) {
								$("#import_ship").attr('disabled', true);
								$
										.ajax({
											type : 'POST',
											url : 'shippingProcess.do',
											data : {
												action : "importShip"
											},beforeSend : function(){
												showBlockUI();
												$("#import_ship").attr('disabled', false);
											},complete:function(){
												$.unblockUI();
											},
											success : function(result) {
												var obj = jQuery
														.parseJSON(result);
												var isSuccess = obj.isSuccess ? "成功"
														: "失敗";
												dialogMsg('轉入出貨', "轉入出貨作業："
														+ isSuccess);

												$(
														"#import_trans_list_date_begin")
														.attr('disabled', false);
												$("#import_trans_list_date_end")
														.attr('disabled', false);
												$("#import_resale").attr(
														'disabled', false);
												$("#import_alloc_inv").attr(
														'disabled', true);
												$("#statistics_alloc_inv")
														.attr('disabled', true);
												$("#import_order_count").attr(
														'disabled', true);
												$("#import_picking").attr(
														'disabled', true);
												$("#import_ship").attr(
														'disabled', true);
											},
										});
							});

			//20170627  出貨---------------------------------
			$("#fast_button").click(function(e) {

				if(""==$("#fast_trans_list_date_begin").val()){
					dialogMsg("警告","請填入開始日期");
					return;
				}
				
				if(""==$("#fast_trans_list_date_end").val()){
					dialogMsg("警告","請填入結束日期");
					return;
				}
				
				
				if(""==$("#fast_order_count").val()){
					dialogMsg("警告","請填入核定訂單數量");
					return;
				}
				
				
				$("#dialog_fast_trans_list_date_begin").text($("#fast_trans_list_date_begin").val());
				$("#dialog_fast_trans_list_date_end").text($("#fast_trans_list_date_end").val());
				$("#dialog_fast_order_count").text($("#fast_order_count").val());

				
				var fast_dialog=$("#dialog-form-fast").dialog({
		                draggable: true,
		                resizable: false,
		                autoOpen: false,
		                height: "auto",
		                width: "auto",
		                modal: true,
		                show: {
		                    effect: "blind",
		                    duration: 300
		                },
		                hide: {
		                    effect: "fade",
		                    duration: 300
		                },
		                open: function(event, ui) {
		                    $(this).parent().children().children('.ui-dialog-titlebar-close').hide();
		                },
		                buttons: [{
		                    id: "update_enter",
		                    text: "確定",
		                    click: function() {

		                    	$.ajax({
		        					type : 'POST',
		        					url : 'shippingProcess.do',
		        					data : {
		        						action : "fastExecution",
		        						fast_trans_list_date_begin:$("#fast_trans_list_date_begin").val(),
		        						fast_trans_list_date_end:$("#fast_trans_list_date_end").val(),
		        						fast_order_count:$("#fast_order_count").val(),
		        					},
		        					success : function(result) {
		        						var obj = jQuery.parseJSON(result);
		        						var isSuccess = obj.isSuccess ? "成功" : "失敗";
		        						dialogMsg('轉入出貨', "轉入出貨作業：" + isSuccess);

		        					},
		        				});

		                    	fast_dialog.dialog("close");
		                    }


		                }, {
		                    text: "取消",
		                    click: function() {
		                    	fast_dialog.dialog("close");
		                    }
		                }],
		                close: function() {
		                }
		            });
				 fast_dialog.dialog("open");
			
			});

			//新增Dialog相關設定
			insert_dialog = $("#dialog-form-insert")
					.dialog(
							{
								draggable : true,
								resizable : false,
								autoOpen : false,
								height : "auto",
								width : "auto",
								modal : true,
								buttons : [
										{
											id : "insert",
											text : "新增",
											click : function() {

												if ($(
														'#insert-dialog-form-post')
														.valid()) {
													var $insert = $("#dialog-form-insert");
													var tmp = {
														action : "insert",
														order_no : $insert
																.find(
																		"input[name='insert_order_no']")
																.val(),
														order_source : $insert
																.find(
																		"input[name='insert_order_source']")
																.val(),
														name : $insert
																.find(
																		"input[name='insert_name']")
																.val(),
														total_amt : $insert
																.find(
																		"input[name='insert_total_amt']")
																.val(),
														trans_list_date : $insert
																.find(
																		"input[name='insert_trans_list_date']")
																.val(),
														invoice : $insert
																.find(
																		"input[name='insert_invoice']")
																.val(),
														sale_date : $insert
																.find(
																		"input[name='insert_sale_date']")
																.val(),
														invoice_date : $insert
																.find(
																		"input[name='insert_invoice_date']")
																.val(),
														dis_date : $insert
																.find(
																		"input[name='insert_dis_date']")
																.val(),
														memo : $insert
																.find(
																		"input[name='insert_memo']")
																.val(),
														customer_id : $insert
																.find(
																		"input[name='insert_customerid']")
																.val()
													};

													draw_sale(tmp, "", "");
													insert_dialog
															.dialog("close");
													$(
															"#insert-dialog-form-post")
															.trigger("reset");
												}
											}
										},
										{
											text : "取消",
											click : function() {
												$("#insert-dialog-form-post")
														.trigger("reset");
												validator_insert.resetForm();
												insert_dialog.dialog("close");
											}
										} ],
								close : function() {
									validator_insert.resetForm();
									$("#insert-dialog-form-post").trigger(
											"reset");
								}
							});

			//確認Dialog相關設定(刪除功能)
			confirm_dialog = $("#dialog-confirm").dialog({
				draggable : true,
				resizable : false,
				autoOpen : false,
				height : "auto",
				width : "auto",
				modal : true,

				buttons : {
					"確認刪除" : function() {
						var tmp = {
							action : "delete",
							realsale_id : uuid,
							order_no : order_no
						//c_product_id是為了刪除後，回傳指定的結果，所需參數
						};
						draw_sale(tmp, "", "");
						$(this).dialog("close");
					},
					"取消刪除" : function() {
						$(this).dialog("close");
					}
				}
			});
			// 			$("#dialog-confirm").show();

			//修改Dialog相關設定
			update_dialog = $("#dialog-form-update")
					.dialog(
							{
								draggable : true,
								resizable : false,
								autoOpen : false,
								height : "auto",
								width : "auto",
								modal : true,

								buttons : [
										{
											text : "修改",
											click : function() {
												if ($(
														'#update-dialog-form-post')
														.valid()) {
													var cus_id = "";
													var $update = $("#dialog-form-update");
													var tmp = {
														action : "update",
														realsale_id : uuid,
														seq_no : seqNo,
														order_no : $update
																.find(
																		"input[name='update_order_no']")
																.val(),
														order_source : $update
																.find(
																		"input[name='update_order_source']")
																.val(),
														name : $update
																.find(
																		"input[name='update_name']")
																.val(),
														total_amt : $update
																.find(
																		"input[name='update_total_amt']")
																.val(),
														trans_list_date : $update
																.find(
																		"input[name='update_trans_list_date']")
																.val(),
														invoice : $update
																.find(
																		"input[name='update_invoice']")
																.val(),
														sale_date : $update
																.find(
																		"input[name='update_sale_date']")
																.val(),
														invoice_date : $update
																.find(
																		"input[name='update_invoice_date']")
																.val(),
														dis_date : $update
																.find(
																		"input[name='update_dis_date']")
																.val(),
														memo : $update
																.find(
																		"input[name='update_memo']")
																.val(),
														customer_id : $update
																.find(
																		"input[name='update_customerid']")
																.val()

													};

													draw_sale(tmp, "", "");
													update_dialog
															.dialog("close");
													$(
															"#update-dialog-form-post")
															.trigger("reset");
												}
											}
										},
										{
											text : "取消",
											click : function() {
												validator_update.resetForm();
												$("#update-dialog-form-post")
														.trigger("reset");
												update_dialog.dialog("close");
											}
										} ],
								close : function() {
									$("#update-dialog-form-post").trigger(
											"reset");
									validator_update.resetForm();
								}
							});

			//刪除事件聆聽 : 因為聆聽事件動態產生，所以採用delegate來批量處理，節省資源
			$("#sales")
					.delegate(
							".btn_delete",
							"click",
							function() {

								var row = $(this).closest("tr");
								var data = $("#sales").DataTable().row(row)
										.data();
								console.log(data);

								$("#dialog-confirm")
										.html(
												"<table class='dialog-table'>"
														+ "<tr><td>銷貨單號：</td><td><span class='delete_msg'>'"
														+ data.order_no
														+ "'</span></td></tr>"
														+ "<tr><td>銷貨日期：</td><td><span class='delete_msg'>'"
														+ data.sale_date
														+ "'</span></td></tr>"
														+ "</table>");

								uuid = data.realsale_id;
								order_no = $(this).attr("name");
								confirm_dialog.dialog("open");
							});

			//新增事件聆聽
			$("#create-sale").click(function() {
				new_or_edit = 1;
				insert_dialog.dialog("open");
				$("#insert_c_product_id").focus();
				scan_exist = 1;
				if (!scan_exist) {
					$("#warning").html("貼心提醒您:<br>&nbsp;&nbsp;掃描器尚未配置妥善。");
					$("#warning").dialog("open");
				}
			});

			//修改事件聆聽
			$("#sales")
					.delegate(
							".btn_update",
							"click",
							function(e) {
								e.preventDefault();

								new_or_edit = 2;
								var row = $(this).closest("tr");
								var data = $("#sales").DataTable().row(row)
										.data();
								uuid = data.realsale_id;

								var dialogA = document
										.getElementById("dialog-form-update");
								var dialogB = $("#dialog-form-update");

								console.log(data);
								$(
										"#dialog-form-update input[name='update_order_no']")
										.val(data.order_no);
								$(
										"#dialog-form-update input[name='update_order_source']")
										.val(data.order_source);
								$(
										"#dialog-form-update input[name='update_customerid']")
										.val(data.customer_id);
								$(
										"#dialog-form-update input[name='update_name']")
										.val(data.name);
								$(
										"#dialog-form-update input[name='update_total_amt']")
										.val(data.total_amt);
								$(
										"#dialog-form-update input[name='update_trans_list_date']")
										.val(data.trans_list_date);
								$(
										"#dialog-form-update input[name='update_invoice']")
										.val(data.invoice);
								$(
										"#dialog-form-update input[name='update_sale_date']")
										.val(data.sale_date);
								$(
										"#dialog-form-update input[name='update_invoice_date']")
										.val(data.invoice_date);
								$(
										"#dialog-form-update input[name='update_dis_date']")
										.val(data.dis_date);
								$(
										"#dialog-form-update input[name='update_memo']")
										.val(data.memo);

								update_dialog.dialog("open");
							});

			//處理初始的查詢autocomplete
			$("#name").autocomplete({
				minLength : 1,
				source : function(request, response) {
					getProductData(request, response, "NAME");
				},
				change : function(event, ui) {
					var source = $(this).val();
					var temp = $(".ui-autocomplete li").map(function() {
						return $(this).text()
					});
					var found = $.inArray(source, temp);

					if (found < 0) {
						$(this).val('');
						$(this).attr("placeholder", "請輸入正確的客戶名稱!");
					}
				}
			});

			//處理新增的名稱autocomplete
			$("#insert_name").autocomplete({
				minLength : 1,
				source : function(request, response) {
					getProductData(request, response, "NAME");
				},
				change : function(event, ui) {
					var source = $(this).val();
					var temp = $(".ui-autocomplete li").map(function() {
						return $(this).text()
					});
					var found = $.inArray(source, temp);

					if (found < 0) {
						$(this).val('');
						$(this).attr("placeholder", "請輸入正確的客戶名稱!");
					}
				}
			});
			$("#update_name").autocomplete({
				minLength : 1,
				source : function(request, response) {
					getProductData(request, response, "NAME");
				},
				change : function(event, ui) {
					var source = $(this).val();
					var temp = $(".ui-autocomplete li").map(function() {
						return $(this).text()
					});
					var found = $.inArray(source, temp);

					if (found < 0) {
						$(this).val('');
						$(this).attr("placeholder", "請輸入正確的客戶名稱!");
					}
				}
			});

			$(
					[ "#search_c_product_id", "#update_c_product_id",
							"#update_product_name", "#insert_c_product_id",
							"#update_product_name" ].join(",")).dblclick(
					function() {
						$(this).autocomplete({
							minLength : 0
						});
					});

			$("#update_product_name").bind('focus', function() {
				$(this).attr("placeholder", "請輸入商品名稱以供查詢");
			});

			$('#dialog-form-insert, #dialog-form-update').delegate(
					'input[name=quantity], input[name=price]',
					'change',
					function() {
						$this = $(this).closest("div");
						$this.find("input[name$=product_price]")
								.val(
										$this.find("input[name=quantity]")
												.val()
												* $this.find(
														"input[name=price]")
														.val());
					});

			//create list for autocomplete use
			$.ajax({
				type : "POST",
				url : "customer.do",
				data : {
					action : "search"
				},
				success : function(result) {
					var json_obj = $.parseJSON(result);
					console.log("customer list");
					console.log(result);
					$.each(json_obj, function(i, item) {
						if (item.name != null) {
							customer_tags[i] = json_obj[i].name;
							customer_menu[item.customer_id] = item.name;
						}
					});
				}
			});

			$(
					[ "#search_c_product_id", "#update_c_product_id",
							"#update_product_name", "#insert_c_product_id",
							"#insert_product_name" ].join(",")).dblclick(
					function() {
						$(this).autocomplete({
							minLength : 0
						});
					});

			auto_complete("insert-dialog-form-post input[name='name']",
					customer_tags);
			auto_complete("update-dialog-form-post input[name='name']",
					customer_tags);
			order_source_auto("insert-dialog-form-post input[name='insert_order_source']");
			order_source_auto("update-dialog-form-post input[name='update_order_source']");
			auto_complete("order_source", customer_tags);
			order_source_auto("order_source");

			$("#warning").dialog({
				title : "警告",
				draggable : true, //防止拖曳
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
			$("#warning").show();

			//查詢條件版面折疊
			$(".input-field-wrap")
					.append(
							"<div class='div_right_bottom upup'><img src='./images/upup.png'></div>")
					.after(
							"<div class='div_right_top downdown' style='display:none;'><img src='./images/downdown.png'></div>");

			$(".upup, .downdown").click(function() {
				$(".input-field-wrap").slideToggle("slow");
				$(".downdown").slideToggle();
			});

			tooltip("tool");

			//點擊
			$('.sidenav').delegate(
					'a',
					'click',
					function() {
						console.log($(this).html());

						$(".content-wrap > .page-title").html(
								$(this).html() + "test");
						$(".content-wrap").prepend(
								$('<h2 class="test page-title">'
										+ $(this).html() + '</h2>'));

					});

			function getProductData(request, response, kind) {

				$.ajax({
					url : "realsale.do",
					type : "POST",
					cache : false,
					delay : 1500,
					data : {
						action : "search_custom_data",
						identity : kind,
						term : request.term
					},
					success : function(data) {
						console.log("getCustomData By " + kind);
						var json_obj = $.parseJSON(data);
						var result = [];

						if (!json_obj.length) {
							result = [ {
								label : '找不到符合資料',
								value : request.term
							} ];
						} else {
							result = $.map(json_obj, function(item) {
								var label = "", value = "";

								if (kind == "NAME") {
									label = item.name;
									value = item.name;
									$("#insert_customerid").val(
											item.customer_id);
									$("#update_customerid").val(
											item.customer_id);
									$("#customerid").val(item.customer_id);
								}

								return {
									label : label,
									value : value
								}
							});
						}

						return response(result);
					}
				});
			}

		});
	</script>

	<!-- for default parameters -->
	<script>
		var dataTableObj; // for set DataTable
	</script>

	<!-- for common method -->
	<script>
		function drawDataTable(tableId, dom, oUrl, oData, oColumnDefs, oColumns) {
			console.log("drawDataTable start");

			var table = document.getElementById(tableId);

			dataTableObj = $(table).DataTable({
				dom : dom,
				destroy : true,
				language : {
					"url" : "js/dataTables_zh-tw.txt"
				},
				ajax : {
					url : oUrl,
					dataSrc : "",
					type : "POST",
					data : oData
				},
				columnDefs : oColumnDefs,
				columns : oColumns
			});
		}

		function rebuildTable(tableId, tableThs) {

			var table = document.getElementById(tableId);

			$(table).find("thead").find("tr").remove();
			$(table).find("thead")
					.append($("<tr></tr>").val("").html(tableThs));

			$(table).find("tfoot").find('tr').remove();
			$(table).find("tfoot")
					.append($("<tr></tr>").val("").html(tableThs));
		}

		function drawDialog(dialogId, oUrl, oWidth, formId) {

			var dialog = document.getElementById(dialogId);
			var form = document.getElementById(formId);

			dataDialog = $(dialog).dialog({
				draggable : true,
				resizable : false,
				autoOpen : false,
				modal : true,
				width : oWidth,
				close : function() {
					$(form).trigger("reset");
				}
			});

			return dataDialog;
		}
	</script>

	<!-- button listener -->
	<script>
		$("#sales").on("click", ".btn_list", function(e) {
			e.preventDefault();

			var row = $(this).closest("tr");
			var data = $("#sales").DataTable().row(row).data();

			var tblDetail = $("#dialog-sale-detail-table").DataTable({
				dom : "Blfr<t>ip",
				destroy : true,
				language : {
					"url" : "js/dataTables_zh-tw.txt"
				},
				ajax : {
					url : "realsale.do",
					dataSrc : "",
					type : "POST",
					data : {
						"action" : "getRealSaleDetail",
						"realsale_id" : data.realsale_id
					}
				},
				columns : [ {
					"data" : "order_no",
					"defaultContent" : ""
				}, {
					"data" : "product_name",
					"defaultContent" : ""
				}, {
					"data" : "c_product_id",
					"defaultContent" : ""
				}, {
					"data" : "quantity",
					"defaultContent" : ""
				}, {
					"data" : "price",
					"defaultContent" : ""
				}, {
					"data" : "memo",
					"defaultContent" : ""
				} ],
				buttons : [ {
					text : '新增明細',
					className : 'fa fa-plus',
					action : function(e, dt, node, config) {
						OpenDgDetailInsert();
					}
				} ]
			});

			var dgDetail = $("#dialog-sale-detail").dialog({
				title : "銷售資料明細",
				draggable : true,
				resizable : false,
				modal : true,
				autoOpen : true,
				show : {
					effect : "blind",
					duration : 500
				},

				width : 1200,
				close : function() {
					$("#dialog-form-sale-detail").trigger("reset");
				}
			});

			$("#dialog-sale-detail").data("sale_id", data.sale_id);
		});
	</script>
</body>
</html>