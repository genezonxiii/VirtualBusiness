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
<title>銷單管理</title>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<link rel="Shortcut Icon" type="image/x-icon" href="./images/Rockettheme-Ecommerce-Shop.ico" />
<jsp:include page="template/common_css.jsp" flush="true"/>
<style>
.realsale_table {
    margin:auto;
   border-collapse: separate; 
    border-spacing: 10px 20px; 

    font-family: "微軟正黑體", "Microsoft JhengHei", 'LiHei Pro', Arial, Helvetica, sans-serif, \5FAE\8EDF\6B63\9ED1\9AD4,\65B0\7D30\660E\9AD4; */
  }
  </style>
</head>
<body>
	<input type="hidden" id="glb_menu" value='<%= menu %>' />
	<input type="hidden" id="glb_privilege" value="<%= privilege %>" />
	
	<div class="page-wrapper">
	 	<jsp:include page="template/common_headfoot.jsp" flush="true"/>
		
		<div class="content-wrap">
			<h2 class="page-title">銷單管理</h2> 
		
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
											<td>訂單編號：</td>
											<td><input type="text" name="update_order_no" placeholder="輸入訂單編號"></td>
											<td>銷售平台：</td>
											<td><input type="text" name="update_order_source" placeholder="輸入銷售平台"></td>
										</tr>
										<tr>
											<td>客戶姓名：</td>
											<td>
											<input type="text" id="update_name" name="update_name" placeholder="輸入客戶姓名">
											<input type="hidden" id="update_customerid" name="update_customerid" />
											</td>
											<td>訂單總額：</td>
											<td><input type="text" id="update_total_amt" name="update_total_amt" placeholder="輸入訂單總額"></td>
										</tr>
										<tr>
											<td>轉單日：</td>
											<td><input type="text" id="update_trans_list_date" name="update_trans_list_date" placeholder="輸入轉單日" class="input-date"></td>
											<td>發票號碼：</td>
											<td><input type="text" id="update_invoice" name="update_invoice" placeholder="輸入發票號碼"></td>
										</tr>
										<tr>
											<td>銷貨日：</td>
											<td><input type="text" id="update_sale_date" name="update_sale_date" placeholder="輸入銷貨日" class="input-date"></td>
											<td>發票日：</td>
											<td><input type="text" name="update_invoice_date" placeholder="輸入發票日期" class="input-date"></td>
										</tr>
										<tr>
											<td>出貨日：</td>
											<td><input type="text" name="update_dis_date" placeholder="輸入出貨日期" class="input-date"></td>
										</tr>
										<tr>
											<td>備註說明：</td>
											<td><input type="text" name="update_memo" placeholder="輸入備註說明"></td>
										</tr>		
									</table>
								</fieldset>
							</form>
						</div>
	
						<!--對話窗樣式-新增 -->
						<div id="dialog-form-insert" title="新增銷售資料">
							<form name="insert-dialog-form-post" id="insert-dialog-form-post">
								<font color=red style="padding-left: 26px">掃條碼亦可取得商品資料</font>
								<fieldset>
									<table class='form-table'>									
										<tr>
											<td>訂單編號：</td>
											<td><input type="text" name="insert_order_no" placeholder="輸入訂單編號"></td>
											<td>銷售平台：</td>
											<td><input type="text" name="insert_order_source" placeholder="輸入銷售平台"></td>
										</tr>
										<tr>
											<td>客戶姓名：</td>
											<td>
											<input type="text" id="insert_name" name="insert_name" placeholder="輸入客戶姓名">
											<input type="hidden" id="insert_customerid" name="insert_customerid" />
											</td>
											<td>訂單總額：</td>
											<td><input type="text" id="insert_total_amt" name="insert_total_amt" placeholder="輸入訂單總額"></td>
										</tr>
										<tr>
											<td>轉單日：</td>
											<td><input type="text" id="insert_trans_list_date" name="insert_trans_list_date" placeholder="輸入轉單日" class="input-date"></td>
											<td>發票號碼：</td>
											<td><input type="text" id="insert_invoice" name="insert_invoice" placeholder="輸入發票號碼"></td>
										</tr>
										<tr>
											<td>銷貨日：</td>
											<td><input type="text" id="insert_sale_date" name="insert_sale_date" placeholder="輸入銷貨日" class="input-date"></td>
											<td>發票日：</td>
											<td><input type="text" name="insert_invoice_date" placeholder="輸入發票日期" class="input-date"></td>
										</tr>
										<tr>
											<td>出貨日：</td>
											<td><input type="text" name="insert_dis_date" placeholder="輸入出貨日期" class="input-date"></td>
										</tr>
										<tr>
											<td>備註說明：</td>
											<td><input type="text" name="insert_memo" placeholder="輸入備註說明"></td>
										</tr>										
									</table>
								</fieldset>
							</form>
						</div>
	
						<div class="input-field-wrap">
							<div class="form-wrap">
								<div class="form-row">
									<table class='realsale_table'>
					<tr>
						<td>訂單編號：</td>
						<td><input type="text" id="order_no_begin"
							name="order_no_begin"></td>
						<td><input type="text" id="order_no_end" name="order_no_end"></td>
					</tr>
					<tr>
						<td>客戶姓名：</td>
						<td>
						<input type="text" id="name" name="name">
						<input type="hidden" id="customerid" name="customerid" />
						</td>
					</tr>
					<tr>
						<td>轉單日期區間：</td>
						<td><input type="text" class="input-date"
							id="trans_list_date_begin" name="trans_list_date_begin"></td>
						<td><input type="text" class="input-date"
							id="trans_list_date_end" name="trans_list_date_end"></td>
					</tr>
					<tr>
						<td>出貨日期區間：</td>
						<td><input type="text" class="input-date" id="dis_date_begin"
							name="dis_date_begin"></td>
						<td><input type="text" class="input-date" id="dis_date_end"
							name="dis_date_end"></td>
					</tr>
					<tr>
						<td>訂單來源：</td>
						<td><input type="text" id="order_source" name="order_source"></td>
					</tr>
					<tr>
						<td>出貨方式：</td>
						<td>
						<!-- <input type="text" id="deliveryway" name="deliveryway"> -->
							<select name="deliveryway" id="deliveryway">
						    <option value="1">宅配</option>
						    <option value="2">超取711</option>
						    <option value="3">超取全家</option>
						  </select>						  
						</td>
					</tr>
					<tr>
						<td><button class="btn btn-darkblue" id="search-sale" c_product_id_error="">查詢</button></td>
						<td><button class="btn btn-exec btn-wide" id="create-sale">新增</button></td>					
					</tr>
<!-- 					<tr> -->
<!-- 						<td>轉單日期區間：</td> -->
<!-- 						<td><input type="text" class="input-date" id="import_trans_list_date_begin" name="import_trans_list_date_begin"></td> -->
<!-- 						<td><input type="text" class="input-date" id="import_trans_list_date_end" name="import_trans_list_date_end"></td> -->
<!-- 						<td><button class="btn btn-exec btn-wide" id="import_resale">匯入</button></td> -->
<!-- 						<td><button class="btn btn-exec btn-wide" id="import_alloc_inv">匯入配庫</button></td>	 -->
<!-- 						<td><button class="btn btn-exec btn-wide" id="action_alloc_inv">執行配庫</button></td>	 -->
<!-- 					</tr> -->
					
				</table>
								</div>
							</div>
						</div>
						<div class="row search-result-wrap" align="center"
							id="sales_contain_row" style="display: none;">
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
	
	<jsp:include page="template/common_js.jsp" flush="true"/>

	<script>
		var customer_menu = [];
		var customer_tags = [];
		
		function draw_sale(parameter) {
			$("#sales_contain_row").css({
				"opacity" : "0"
			});
			warning_msg("---讀取中請稍候---");

			console.log('ajax start');
			
			var oColumnDefs =
 		        [{
// 					//訂單編號 及 出貨單連結
// 		        	targets: 1,
// 					render: function ( data, type, row ) {
// 						var result = $("<div/>") //fake tag
// 							.append( 
// 								$("<a/>", {
// 									"title": "出貨單",
// 									"class": "report",
// 									"href": "./report.do?sale_id=" + row.sale_id,
// 									"target": "_blank"
// 								})
// 								.text(row.order_no)
// 							);
// 				 		return result.html();
// 					}
// 	            },{
// 					//自訂商品編號 + 商品名稱
// 	            	targets: 2,
// 					render: function ( data, type, row ) {
// 				   		var result = row.c_product_id + '<br>' + row.product_name;
// 				 		return result;
// 					}
// 	            },{
// 					//銷貨對象
// 	            	targets: 5,
// 					render: function ( data, type, row ) {
// 						console.log(row.customer_id);
// 						console.log(customer_menu);
// 				   		var result = row.customer_id == null || row.customer_id == '' ? "":customer_menu[row.customer_id];
// 				 		return result;
// 					}
// 	            },{
// 					//發票 + 發票日期
// 	            	targets: 6,
// 					render: function ( data, type, row ) {
// 				   		var result = row.invoice == null || row.invoice == '' ? "":"號碼：" + row.invoice + "<br>日期：" + row.invoice_date;
// 				 		return result;
// 					}
// 	            },{
	            	//功能
					targets: -1,
				   	searchable: false,
				   	orderable: false,
				   	render: function ( data, type, row ) {
				   		
						var options = $("<div/>") //fake tag
							.append( $("<div/>", {"class": "table-row-func btn-in-table btn-gray"}) 
								.append( $("<i/>", {"class": "fa fa-ellipsis-h"}) )
								.append( 
									$("<div/>", {"class": "table-function-list"})
										.append( 
											$("<button/>", {
												"id": row.order_no,
												"value": row.order_no,
												"name": row.order_no,
												"class": "btn-in-table btn-darkblue btn_update",
												"title": "修改"
											})
											.append( $("<i/>", {"class": "fa fa-pencil"}) )
										)
										.append( 
											$("<button/>", {
												"id": row.order_no,
												"value": row.order_no,
												"name": row.order_no,
												"class": "btn-in-table btn-alert btn_delete",
												"title": "刪除"
											})
											.append( $("<i/>", {"class": "fa fa-trash"}) )
										)
										.append( 
											$("<button/>", {
												"class": "btn-in-table btn-green btn_list",
												"title": "清單"
											})
											.append( $("<i/>", {"class": "fa fa-pencil-square-o"}) )
										)
								)
							);
						
				 		return options.html();
				   	}
				}];
			
			var oColumns = 
				[				
					{"data": "order_no", "width": "10%", "defaultContent":""},					
					{"data": "name", "width": "5%", "defaultContent":""},
					{"data": "trans_list_date", "width": "10%", "defaultContent":""},
					{"data": "sale_date", "width": "10%", "defaultContent":""},
					{"data": "dis_date", "width": "10%", "defaultContent":""},
					{"data": "order_source", "width": "5%", "defaultContent":""},
					{"data": "memo", "width": "10%", "defaultContent":""},
					{"data": null, "width": "10%", "defaultContent":""}
				];
			
			var dataTableObj = $("#sales").DataTable({
				dom: "lfr<t>ip",
				destroy: true,
				language: {"url": "js/dataTables_zh-tw.txt"},
				ajax: {
					url : "realsale.do",
					dataSrc: "",
					type : "POST",
					data : parameter
				},
		        columnDefs: oColumnDefs,
				columns: oColumns
			});	
				
			
			$("#sales_contain_row")
				.show()
				.animate({
					"opacity" : "0.01"
				}, 1)
				.animate({
					"opacity" : "1"
				}, 300);
			
			warning_msg("");
		}

		var scan_exist = 0, 
			new_or_edit = 0; //1: insert, 2: update
		$(function() {
			$(window).scannerDetection();
			$(window).bind('scannerDetectionComplete', function(e, data) {
				if (data.string == "success") {
					return;
				}
				
				$.ajax({
					url : "product.do",
					type : "POST",
					cache : false,
					data : {
						action : "find_barcode",
						barcode : data.string,
					},
					success : function(result) {
						var json_obj = $.parseJSON(result);
						var result_table = "";
						$.each(json_obj, function(i, item) {
							if (new_or_edit == 1) {
								//new_or_edit=3;
								$("#insert_product_name").val(json_obj[i].product_name);
								$("#insert_c_product_id").val(json_obj[i].c_product_id);
								$("#insert_quantity").val(json_obj[i].keep_stock);
								$("#insert_price").val(json_obj[i].cost);
								$("#insert_product_price").val(json_obj[i].cost);
							}
							if (new_or_edit == 2) {
								//new_or_edit=3;
								$("#update_product_name").val(json_obj[i].product_name);
								$("#update_c_product_id").val(json_obj[i].c_product_id);
								$("#update_quantity").val(json_obj[i].keep_stock);
								$("#update_price").val(json_obj[i].cost);
								$("#update_product_price").val(json_obj[i].cost);
							}
						});
						if (json_obj.length == 0) {
							$("#warning").html("<h3>該條碼無商品存在</h3>請至'商品管理'介面&nbsp;定義該條碼。");
							$("#warning").dialog("open");
						}
					}
				});
			}).bind( 'scannerDetectionError', function(e, data) {
						
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
			jQuery.validator.addMethod("stringMaxLength", function(value, element, param) {
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
			
			//自訂商品ID查詢相關設定
			$("#search-sale").click(function(e) {						
				e.preventDefault();				
// 				if ($("#search-sale").attr("c_product_id_error").length > 0) {
// 					var tmp = "查無商品ID: "
// 							+ $("#search-sale").attr("c_product_id_error");
					
// 					if (!confirm(tmp)) {
// 						return;
// 					}
// 				}
				var tmp = {
					action : "search",				
 					order_no_begin : $("input[name='order_no_begin']").val(),
 					order_no_end : $("input[name='order_no_end']").val(),
 					trans_list_date_begin : $("#trans_list_date_begin").val(),
 					trans_list_date_end : $("#trans_list_date_end").val(),
 					dis_date_begin : $("#dis_date_begin").val(),
 					dis_date_end : $("#dis_date_end").val(),
 					order_source : $("#order_source").val(),
 					deliveryway : $("#deliveryway").val(),
 					customerid : $("#customerid").val()
				};
				draw_sale(tmp);
			});
		
			//20170426從sale匯入到realsale---------------------------------
			$("#import_resale").click(function(e) {						
				e.preventDefault();				
				var tmp = {
					action : "importData",				
					import_trans_list_date_begin : $("#import_trans_list_date_begin").val(),	
 					import_trans_list_date_end : $("#import_trans_list_date_end").val()	
				};
				draw_sale(tmp);
			});
			//------------------------------------------------------------
			//20170427從realsale匯入到alloc_inv---------------------------------
			$("#import_alloc_inv").click(function(e) {						
				e.preventDefault();				
				var tmp = {
					action : "importallocinvData"
				};
				draw_sale(tmp);
			});
			//------------------------------------------------------------
			
			//新增Dialog相關設定
			insert_dialog = $("#dialog-form-insert").dialog({
				draggable : true,
				resizable : false,
				autoOpen : false,
				height : "auto",
				width : "auto",
				modal : true,
// 				show : {
// 					effect : "blind",
// 					duration : 300
// 				},
// 				hide : {
// 					effect : "fade",
// 					duration : 300
// 				},
				buttons : [
						{
							id : "insert",
							text : "新增",
							click : function() {
								
								if ($('#insert-dialog-form-post').valid()) {
// 									var cus_id = "";
// 									for (x in customer_menu) {
// 										if (customer_menu[x] == $("#dialog-form-insert input[name='name']").val()) {
// 											cus_id = x;
// 										}
// 									}
// 									if (cus_id.length < 1
// 											&& $("#dialog-form-insert input[name='name']").val().length > 0) {
// 										alert("查無客戶: '"
// 												+ $("#dialog-form-insert input[name='name']").val()
// 												+ "'\n 請先至客戶管理介面新增");
// 										return;
// 									}
									
									var $insert = $("#dialog-form-insert");
									var tmp = {
										action : "insert",
										order_no : $insert.find("input[name='insert_order_no']").val(),
										order_source : $insert.find("input[name='insert_order_source']").val(),
										name : $insert.find("input[name='insert_name']").val(),
										total_amt : $insert.find("input[name='insert_total_amt']").val(),
										trans_list_date : $insert.find("input[name='insert_trans_list_date']").val(),
										invoice : $insert.find("input[name='insert_invoice']").val(),
										sale_date : $insert.find("input[name='insert_sale_date']").val(),
										invoice_date : $insert.find("input[name='insert_invoice_date']").val(),
										dis_date : $insert.find("input[name='insert_dis_date']").val(),
										memo : $insert.find("input[name='insert_memo']").val(),
										customer_id : $insert.find("input[name='insert_customerid']").val()
									};
									
									draw_sale(tmp);
									insert_dialog.dialog("close");
									$("#insert-dialog-form-post").trigger("reset");
								}
							}
						},
						{
							text : "取消",
							click : function() {
								$("#insert-dialog-form-post").trigger("reset");
								validator_insert.resetForm();
								insert_dialog.dialog("close");
							}
						} ],
				close : function() {
					validator_insert.resetForm();
					$("#insert-dialog-form-post").trigger("reset");
				}
			});
// 			$("#dialog-form-insert").show();
			
			//確認Dialog相關設定(刪除功能)
			confirm_dialog = $("#dialog-confirm").dialog({
				draggable : true,
				resizable : false,
				autoOpen : false,
				height : "auto",
				width : "auto",
				modal : true,
// 				show : {
// 					effect : "blind",
// 					duration : 300
// 				},
// 				hide : {
// 					effect : "fade",
// 					duration : 300
// 				},
				buttons : {
					"確認刪除" : function() {		
						var tmp = {
							action : "delete",
							realsale_id : uuid,
							order_no : order_no
						//c_product_id是為了刪除後，回傳指定的結果，所需參數
						};
						draw_sale(tmp);
						$(this).dialog("close");
					},
					"取消刪除" : function() {
						$(this).dialog("close");
					}
				}
			});
// 			$("#dialog-confirm").show();
			
			//修改Dialog相關設定
			update_dialog = $("#dialog-form-update").dialog({
				draggable : true,
				resizable : false,
				autoOpen : false,
				height : "auto",
				width : "auto",
				modal : true,
// 				show : {
// 					effect : "blind",
// 					duration : 300
// 				},
// 				hide : {
// 					effect : "fade",
// 					duration : 300
// 				},
				buttons : [
						{
							text : "修改",
							click : function() {
								if ($('#update-dialog-form-post').valid()) {
									var cus_id = "";
// 									for (x in customer_menu) {
// 										if (customer_menu[x] == $("#dialog-form-update input[name='name']").val()) {
// 											cus_id = x;
// 										}
// 									}

// 									if (cus_id.length < 1
// 											&& $("#dialog-form-update input[name='name']").val().length > 0) {
// 										alert("查無客戶: '"
// 												+ $("#dialog-form-update input[name='name']").val()
// 												+ "'\n 請先至客戶管理介面新增");
// 										return;
// 									}								
									var $update = $("#dialog-form-update");
									var tmp = {
										action : "update",
										realsale_id : uuid,
										seq_no : seqNo,									
										order_no : $update.find("input[name='update_order_no']").val(),
										order_source : $update.find("input[name='update_order_source']").val(),
										name : $update.find("input[name='update_name']").val(),
										total_amt : $update.find("input[name='update_total_amt']").val(),
										trans_list_date : $update.find("input[name='update_trans_list_date']").val(),
										invoice : $update.find("input[name='update_invoice']").val(),
										sale_date : $update.find("input[name='update_sale_date']").val(),
										invoice_date : $update.find("input[name='update_invoice_date']").val(),
										dis_date : $update.find("input[name='update_dis_date']").val(),
										memo : $update.find("input[name='update_memo']").val(),
										customer_id : $update.find("input[name='update_customerid']").val()
										
									};
									
									draw_sale(tmp);
									update_dialog.dialog("close");
									$("#update-dialog-form-post").trigger("reset");
								}
							}
						},
						{
							text : "取消",
							click : function() {
								validator_update.resetForm();
								$("#update-dialog-form-post").trigger("reset");
								update_dialog.dialog("close");
							}
						} ],
				close : function() {
					$("#update-dialog-form-post").trigger("reset");
					validator_update.resetForm();
				}
			});
			
			//刪除事件聆聽 : 因為聆聽事件動態產生，所以採用delegate來批量處理，節省資源
			$("#sales").delegate(".btn_delete", "click", function() {
				
				var row = $(this).closest("tr");
			    var data = $("#sales").DataTable().row(row).data();
			    console.log(data);
			    
				$("#dialog-confirm").html("<table class='dialog-table'>"
					+ "<tr><td>銷貨單號：</td><td><span class='delete_msg'>'"
					+ data.order_no
					+ "'</span></td></tr>"					
					+ "<tr><td>銷貨日期：</td><td><span class='delete_msg'>'"
					+ data.sale_date
					+ "'</span></td></tr>"
					+ "</table>");
				
				uuid=data.realsale_id;				
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
			$("#sales").delegate(".btn_update", "click", function(e) {
				e.preventDefault();
				
				new_or_edit = 2;			
// 				seqNo = $(this).attr("id");			
				var row = $(this).closest("tr");
			    var data = $("#sales").DataTable().row(row).data();
			    uuid = data.realsale_id;
			    //清空查詢條件
// 				$("input[name='search_c_product_id']").val("");
// 				$("#trans_list_start_date").val("");
// 				$("#trans_list_end_date").val("");
				
				var dialogA = document.getElementById("dialog-form-update");
				var dialogB = $("#dialog-form-update");
				
				console.log(data);
				$("#dialog-form-update input[name='update_order_no']").val(data.order_no);
				$("#dialog-form-update input[name='update_order_source']").val(data.order_source);
				$("#dialog-form-update input[name='update_customerid']").val(data.customer_id);	
				$("#dialog-form-update input[name='update_name']").val(data.name);
				$("#dialog-form-update input[name='update_total_amt']").val(data.total_amt);
				$("#dialog-form-update input[name='update_trans_list_date']").val(data.trans_list_date);
				$("#dialog-form-update input[name='update_invoice']").val(data.invoice);
				$("#dialog-form-update input[name='update_sale_date']").val(data.sale_date);
				$("#dialog-form-update input[name='update_invoice_date']").val(data.invoice_date);
				$("#dialog-form-update input[name='update_dis_date']").val(data.dis_date);
				$("#dialog-form-update input[name='update_memo']").val(data.memo);
				
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
// 			.bind('focus', function() {
// 				$(this).attr("placeholder", "請輸入商品ID以供查詢");
// 			});
			
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
			
// 			$("#insert_name").bind('focus', function() {
// 				$(this).attr("placeholder", "請輸入客戶名稱以供查詢");
// 			});
			
			//處理新增的自訂ID autocomplete
// 			$("#insert_c_product_id").autocomplete({
// 				minLength : 1,
// 				source : function(request, response) {
// 					console.log("source");
// 					getProductData(request, response, "ID");
// 				}
// 				change : function(e, ui) {
// 					console.log("change");
// 					if (!ui.item) {
// 						$(this)
// 							.val("")
// 							.attr("placeholder", "請輸入正確商品ID名稱!");
// 					}
// 				},
// 				response : function(e, ui) {
// 					console.log("response");
// 					if (ui.content.length == 0) {
// 						$(this)
// 							.val("")
// 							.attr("placeholder", "請輸入正確ID名稱!");
// 					}
// 				}
//			});
// 			.blur(function() {
// 				console.log("blur");
// 				$.ajax({
// 					url : "sale.do",
// 					type : "POST",
// 					cache : false,
// 					delay : 1500,
// 					data : {
// 						action : "search_product_data",
// 						identity : "ID",
// 						term : $("#insert_c_product_id").val()
// 					},
// 					success : function(data) {
// 						var json_obj = $.parseJSON(data);
// 						var resultRunTime = 0;
// 						$.each(json_obj, function(i) {
// 							resultRunTime += 1;
// 						});
// 						if (resultRunTime == 0) {
// // 							$("#insert_c_product_id")
// 							$("#dialog-form-insert")
// 								.find("input[name=c_product_id]")
// 								.val("")
// 								.attr("placeholder", "請輸入正確ID名稱!");
// 						}
// 					}
// 				});
// 			});
			
			//處理修改的名稱autocomplete
// 			$("#update_product_name").autocomplete({
// 				minLength : 1,
// 				source : function(request, response) {
// 					getProductData(request, response, "NAME");
// 				},
// 				change : function(event, ui) {
// 					var source = $(this).val();
// 					var temp = $(".ui-autocomplete li").map(function() {
// 						return $(this).text()
// 					});
// 					var found = $.inArray(source, temp);

// 					if (found < 0) {
// 						$(this).val('');
// 						$(this).attr("placeholder", "請輸入正確的商品名稱!");
// 					}
// 				}
// 			});
			
			$(["#search_c_product_id", 
				"#update_c_product_id", 
				"#update_product_name",  
				"#insert_c_product_id", 
				"#update_product_name"].join(","))
			.dblclick(function() {
				$(this).autocomplete({
					minLength : 0
				});
			});
			
			$("#update_product_name").bind('focus', function() {
				$(this).attr("placeholder", "請輸入商品名稱以供查詢");
			});
			
			//處理修改的自訂ID autocomplete
// 			$("#update_c_product_id").autocomplete({
// 				minLength : 1,
// 				source : function(request, response) {
// 					getProductData(request, response, "ID");
// 				},
// 				change : function(event, ui) {
// 					var source = $(this).val();
// 					var temp = $(".ui-autocomplete li").map(function() {
// 						return $(this).text()
// 					});
// 					var found = $.inArray(source, temp);

// 					if (found < 0) {
// 						$(this).val('');
// 						$(this).attr("placeholder", "請輸入正確的ID名稱!");
// 					}
// 				}
// 			});
			
// 			$("#update_c_product_id").bind('focus', function() {
// 				$(this).attr("placeholder", "請輸入ID名稱以供查詢");
// 			});
			
// 			$("#update_quantity").change(function() {
// 				$("#update_product_price").val(
// 						$("#update_quantity").val() * $("#update_price").val() );
// 			});
			
// 			$("#update_price").change(function() {
// 				$("#update_product_price").val(
// 						$("#update_quantity").val() * $("#update_price").val() );
// 			});
			
// 			$("#insert_quantity").change(function() {
// 				$("#insert_product_price").val(
// 						$("#insert_quantity").val()
// 								* $("#insert_price").val());
// 			});
			
// 			$("#insert_price").change(function() {
// 				$("#insert_product_price").val(
// 						$("#insert_quantity").val() * $("#insert_price").val() );
// 			});
			
			$('#dialog-form-insert, #dialog-form-update').delegate('input[name=quantity], input[name=price]', 'change', function(){
				$this = $(this).closest("div");
				$this.find("input[name$=product_price]").val(
					$this.find("input[name=quantity]").val() * $this.find("input[name=price]").val() );
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
			
			$(["#search_c_product_id", 
				"#update_c_product_id", 
				"#update_product_name",  
				"#insert_c_product_id", 
				"#insert_product_name"].join(","))
			.dblclick(function() {
				$(this).autocomplete({
					minLength : 0
				});
			});
			
			auto_complete("insert-dialog-form-post input[name='name']", customer_tags);
			auto_complete("update-dialog-form-post input[name='name']", customer_tags);
			order_source_auto("insert-dialog-form-post input[name='insert_order_source']");
			order_source_auto("update-dialog-form-post input[name='update_order_source']");
			auto_complete("order_source", customer_tags);
			order_source_auto("order_source");
			
			
			$("#warning").dialog({
				title : "警告",
				draggable : true,//防止拖曳
				resizable : false,//防止縮放
				autoOpen : false,
				height : "auto",
				modal : true,
// 				show : {
// 					effect : "bounce",
// 					duration : 1000
// 				},
// 				hide : {
// 					effect : "fade",
// 					duration : 300
// 				},
				buttons : {
					"確認" : function() {
						$(this).dialog("close");
					}
				}
			});
			$("#warning").show();
			
			//查詢條件版面折疊
			$(".input-field-wrap")
				.append("<div class='div_right_bottom upup'><img src='./images/upup.png'></div>")
				.after("<div class='div_right_top downdown' style='display:none;'><img src='./images/downdown.png'></div>");
			
			$(".upup, .downdown").click(function() {
				$(".input-field-wrap").slideToggle("slow");
				$(".downdown").slideToggle();
			});
			
			tooltip("tool");
			
			//點擊
			$('.sidenav').delegate('a', 'click', function(){
				console.log( $(this).html() );

				$(".content-wrap > .page-title").html( $(this).html() );
				$(".content-wrap").prepend($('<h2 class="test page-title">' + $(this).html() + '</h2>'));
				
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
						
						if(!json_obj.length){
				      		result = [ {
						       	label: '找不到符合資料', 
						       	value: request.term
					       	} ];
				     	} else {
							result = $.map(json_obj, function(item) {
								var label = "", value = "";
								
								 if (kind == "NAME") {
										label = item.name;
										value = item.name;
										$("#insert_customerid").val(item.customer_id);
										$("#update_customerid").val(item.customer_id);
										$("#customerid").val(item.customer_id);
									}

								return {
									label : label,
									value : value
// 									,product_id : item.product_id,
// 									product_name : item.product_name,
// 									c_product_id : item.c_product_id,
// 									price : item.price,
// 									cost : item.cost
								}
							});
				     	}
						
						return response( result );
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
// 				show : {
// 					effect : "blind",
// 					duration : 300
// 				},
// 				hide : {
// 					effect : "fade",
// 					duration : 300
// 				},
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
			columns : [ 				
				{"data" : "order_no", "defaultContent" : ""},
				{"data" : "product_name", "defaultContent" : ""},
				{"data" : "c_product_id", "defaultContent" : ""},
				{"data" : "quantity", "defaultContent" : ""},
				{"data" : "price", "defaultContent" : ""},
				{"data" : "memo", "defaultContent" : ""}
			],
			buttons: [{
				text: '新增明細', 
				className: 'fa fa-plus',
            	action: function ( e, dt, node, config ) {
            		OpenDgDetailInsert();
                }
			}]
		});
		
		var dgDetail = $("#dialog-sale-detail").dialog({
			title: "銷售資料明細",
			draggable : true,
			resizable : false,
			modal : true,
			autoOpen: true,
				show : {
					effect : "blind",
					duration : 500
				},
//				hide : {
//					effect : "fade",
//					duration : 300
//				},
			width : 1200,
			close : function() {
				$("#dialog-form-sale-detail").trigger("reset");
			}
		});
		
		$("#dialog-sale-detail")
			.data("sale_id", data.sale_id);
	});
	</script>
	
</body>
</html>