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
											<td>總金額：</td>
											<td><input type="text" id="update_product_price"
												name="update_product_price" disabled></td>
										</tr>
										<tr>
											<td>訂單總金額：</td>
											<td><input type="text" id="update_total_amt"
												name="total_amt" value="0"></td>
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
										<tr>
											<td>訂單總金額：</td>
											<td><input type="text" id="insert_total_amt"
												name="total_amt" value="0"></td>
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
											<th style="min-width: 120px">商品 ID/名稱</th>
											<th>數量</th>
											<th style="min-width: 60px">銷貨金額</th>
											<th>銷貨對象</th>
											<th style="min-width: 110px">發票</th>
											<th>轉單日</th>
											<th>銷/出貨日期</th>
											<th>上傳日期</th>
											<th>銷售平台</th>
											<th style="background-image: none !important;">註</th>
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
	<script>
	var customer_menu = [];
	var customer_tags = [];
	var selectCount = 0; //全選按鈕計算用
	function open_report(id) {
	    var iframUrl = "./report.do?sale_id=" + id;
	    $("#dialog_report_iframe").attr("src", iframUrl);
	    $("#dialog_report").dialog({
	        draggable: true,
	        resizable: false,
	        autoOpen: false,
	        height: "600",
	        width: "900",
	        modal: true,
	        closeOnEscape: false,
	        open: function(event, ui) {
	            $(".ui-dialog-titlebar-close", ui.dialog | ui).hide();
	        },
	        buttons: [{
	            text: "關閉",
	            click: function() {
	                $("#dialog_report").dialog("close");
	            }
	        }]
	    })
	    $("#dialog_report").dialog("open");
	}

	function draw_sale(parameter) {
		last_parameter = parameter;
	    $("#sales_contain_row").css({
	        "opacity": "0"
	    });
	    warning_msg("---讀取中請稍候---");

	    console.log('ajax start');
	    var oColumnDefs = [{
	        targets: 0,
	        searchable: false,
	        orderable: false,
	        render: function(data, type, row) {
	            var sale_id = row.sale_id;

	            var input = document.createElement("INPUT");
	            input.type = 'checkbox';
	            input.name = 'checkbox-group-select';
	            input.id = sale_id;

	            var span = document.createElement("SPAN");
	            span.className = 'form-label';

	            var label = document.createElement("LABEL");
	            label.htmlFor = sale_id;
	            label.name = 'checkbox-group-select';
	            label.style.marginLeft = '35%';
	            label.appendChild(span);

	            var options = $("<div/>").append(input, label);

	            return options.html();
	        }
	    }, {
	        //可轉銷貨
	        targets: 1,
	        render: function(data, type, row) {
	            var result = row.turnFlag == true ? "是" : "否";
	            return result;
	        }
	    }, {
	        //訂單編號 及 出貨單連結
	        targets: 3,
	        render: function(data, type, row) {
	            var result = $("<div/>") //fake tag
	                .append(
	                    $("<a/>", {
	                        "title": "出貨單",
	                        "class": "report",
	                        "href": "javascript:open_report('" + row.sale_id + "')",
	                        "target": "_blank"
	                    })
	                    .text(row.order_no)
	                );
	            return result.html();
	        }
	    }, {
	        //自訂商品編號 + 商品名稱
	        targets: 4,
	        render: function(data, type, row) {
	            var result = row.c_product_id + '<br>' + row.product_name;
	            return result;
	        }
	    }, {
	        //銷貨對象
	        targets: 7,
	        render: function(data, type, row) {

	            var result = row.customer_id == null || row.customer_id == '' ? "" : customer_menu[row.customer_id];
	            return result;
	        }
	    }, {
	        //發票 + 發票日期
	        targets: 8,
	        render: function(data, type, row) {
	            var result = row.invoice == null || row.invoice == '' ? "" : "號碼：" + row.invoice + "<br>日期：" + row.invoice_date;
	            return result;
	        }
	    }, {
	        //功能
	        targets: -1,
	        searchable: false,
	        orderable: false,
	        render: function(data, type, row) {
	            var options = $("<div/>") //fake tag
	                .append($("<div/>", {
	                        "class": "table-row-func btn-in-table btn-gray"
	                    })
	                    .append($("<i/>", {
	                        "class": "fa fa-ellipsis-h"
	                    }))
	                    .append(
	                        $("<div/>", {
	                            "class": "table-function-list"
	                        })
	                        .append(
	                            $("<button/>", {
	                                "id": row.seq_no,
	                                "value": row.sale_id,
	                                "name": row.c_product_id,
	                                "class": "btn-in-table btn-darkblue btn_update",
	                                "title": "修改"
	                            })
	                            .append($("<i/>", {
	                                "class": "fa fa-pencil"
	                            }))
	                        )
	                        .append(
	                            $("<button/>", {
	                                "id": row.seq_no,
	                                "value": row.sale_id,
	                                "name": row.c_product_id,
	                                "class": "btn-in-table btn-alert btn_delete",
	                                "title": "刪除"
	                            })
	                            .append($("<i/>", {
	                                "class": "fa fa-trash"
	                            }))
	                        )
	                        .append(
	                            $("<button/>", {
	                                "class": "btn-in-table btn-green btn_list",
	                                "title": "清單"
	                            })
	                            .append($("<i/>", {
	                                "class": "fa fa-list"
	                            }))
	                        )
	                    )
	                );

	            return options.html();
	        }
	    }];

	    var oColumns = [{
            "data": null,
            "defaultContent": ""
        },{
            "data": "seq_no",
            "width": "10%",
            "defaultContent": ""
        },{
            "data": "order_no",
            "width": "10%",
            "defaultContent": ""
        },{
            "data": null,
            "width": "20%",
            "defaultContent": ""
        },{
            "data": "quantity",
            "width": "5%",
            "defaultContent": ""
        },{
            "data": "price",
            "width": "5%",
            "defaultContent": ""
        },{
            "data": null,
            "width": "10%",
            "defaultContent": ""
        },{
            "data": "turn_flag",
            "width": "10%",
            "defaultContent": ""
        },{
            "data": "invoice",
            "width": "10%",
            "defaultContent": ""
        },{
            "data": "trans_list_date",
            "width": "10%",
            "defaultContent": ""
        },{
            "data": "sale_date",
            "width": "10%",
            "defaultContent": ""
        },{
            "data": "upload_date",
            "width": "10%",
            "defaultContent": ""
        },{
            "data": "order_source",
            "width": "5%",
            "defaultContent": ""
        },{
            "data": "memo",
            "width": "10%",
            "defaultContent": ""
        },{
            "data": null,
            "width": "10%",
            "defaultContent": ""
        }];

	    var dataTableObj = $("#sales").DataTable({
	        dom: "frB<t>ip",
	        lengthChange: false,
	        pageLength: 20,
	        autoWidth: false,
	        scrollY: "250px",
	        destroy: true,
	        language: {
	            "url": "js/dataTables_zh-tw.txt",
	            "emptyTable": "查無資料"
	        },
	        initComplete: function(settings, json) {
	            $('div .dt-buttons').css({
	                'float': 'left',
	                'margin-left': '10px'
	            });
	            $('div .dt-buttons a').css('margin-left', '10px');
	        },
	        ajax: {
	            url: "sale.do",
	            dataSrc: "",
	            type: "POST",
	            data: parameter
	        },
	        columnDefs: oColumnDefs,
	        columns: oColumns,
	        buttons: [{
	            text: '全選',
	            action: function(e, dt, node, config) {

	                selectCount++;
	                var $table = $('#sales');
	                var $checkboxs = $table.find('input[name=checkbox-group-select]');

	                selectCount % 2 != 1 ?
	                    $checkboxs.each(function() {
	                        $(this).prop("checked", false);
	                        $(this).removeClass("toggleon");
	                        $(this).closest("tr").removeClass("selected");
	                    }) :
	                    $checkboxs.each(function() {
	                        $(this).prop("checked", true);
	                        $(this).addClass("toggleon");
	                        $(this).closest("tr").addClass("selected");
	                    });
	            }
	        },{
	            text: '不轉銷貨',
	            action: function(e, dt, node, config) {

	                var cells = dataTableObj.cells().nodes();
	                var $checkboxs = $(cells).find('input[name=checkbox-group-select]:checked');
	                
	                if ($checkboxs.length == 0) {
	                	warningMsg('提示','請選擇至少一筆資料');
	                    return false;
	                }
	                
	                $checkboxs.each(function() {
	                	var sale_id = this.id;
						$.ajax({
							url: 'sale.do',
							type: 'post',
							data: {
							    action: 'update_turn_flag',
							    sale_id: sale_id,
							    turn_flag: "0"
							}
	                	});
					});
	                draw_sale(last_parameter);
	                warningMsg("訊息","此"+$checkboxs.length+"筆已設定不轉銷貨");
	            }
	        },{
	            text: '可轉銷貨',
	            action: function(e, dt, node, config) {

	            	var cells = dataTableObj.cells().nodes();
	                var $checkboxs = $(cells).find('input[name=checkbox-group-select]:checked');
	                
	                if ($checkboxs.length == 0) {
	                	warningMsg('提示','請選擇至少一筆資料');
	                    return false;
	                }
	                
	                $checkboxs.each(function() {
	                	var sale_id = this.id;
						$.ajax({
							url: 'sale.do',
							type: 'post',
							data: {
							    action: 'update_turn_flag',
							    sale_id: sale_id,
							    turn_flag: "1"
							}
	                	});
					});
	                draw_sale(last_parameter);
	                warningMsg("訊息","此"+$checkboxs.length+"筆已設定可轉銷貨");
	            }
	        }]
	    });

	    $('#sales').on('change', ':checkbox', function() {
	        $(this).is(":checked") ?
	            $(this).closest("tr").addClass("selected") :
	            $(this).closest("tr").removeClass("selected");
	    });

	    $("#sales_contain_row")
	        .show()
	        .animate({
	            "opacity": "0.01"
	        }, 1)
	        .animate({
	            "opacity": "1"
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
	            url: "product.do",
	            type: "POST",
	            cache: false,
	            data: {
	                action: "find_barcode",
	                barcode: data.string,
	            },
	            success: function(result) {
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
	    }).bind('scannerDetectionError', function(e, data) {

	    }).bind('scannerDetectionReceive', function(e, data) {

	    });
	    $(window).scannerDetection('success');
	});

	$(function() {

	    $(".bdyplane").animate({
	        "opacity": "1"
	    });

	    var uuid = "";
	    var c_product_id = "";
	    var product_id = "";
	    var seqNo = "";

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
	    jQuery.validator.addMethod("radiobutton", function(value, element, param) {
			if (value == null) {
				return false;
			} else {
				return true;
			}
	    }, $.validator.format("*"));
	    
	    //字母數字
	    jQuery.validator.addMethod("alnum", function(value, element) {
	        return this.optional(element) || /^[a-zA-Z0-9]+$/.test(value);
	    }, "只能包括英文字母和數字");
	    //=========================================

	    //驗證
	    $("#trans_list_date_form").validate({
	        rules: {
	            trans_list_start_date: {
	                dateISO: true
	            },
	            trans_list_end_date: {
	                dateISO: true
	            }
	        },
	        messages: {
	            trans_list_start_date: {
	                dateISO: "日期格式錯誤"
	            },
	            trans_list_end_date: {
	                dateISO: "日期格式錯誤"
	            }
	        }
	    });
	    $("#trans_dis_date_form").validate({
	        rules: {
	            trans_dis_start_date: {
	                dateISO: true
	            },
	            trans_dis_end_date: {
	                dateISO: true
	            }
	        },
	        messages: {
	            trans_dis_start_date: {
	                dateISO: "日期格式錯誤"
	            },
	            trans_dis_end_date: {
	                dateISO: "日期格式錯誤"
	            }
	        }
	    });

	    var oRules = {
	        order_no: {
	            stringMaxLength: 20
	        },
	        product_name: {
	            maxlength: 80,
	            required: true
	        },
	        c_product_id: {
	            stringMaxLength: 40,
	            required: true
	        },
	        total_amt: {
	            required: true,
	            number: true
	        },
	        name: {
	            stringMaxLength: 80
	        },
	        quantity: {
	            required: true,
	            digits: true
	        },
	        price: {
	            required: true,
	            number: true,
	            min: 1
	        },
	        invoice: {
	            stringMaxLength: 12,
	            alnum: true
	        },
	        invoice_date: {
	            dateISO: true
	        },
	        trans_list_date: {
	            dateISO: true
	        },
	        dis_date: {
	            dateISO: true
	        },
	        memo: {
	            stringMaxLength: 200
	        },
	        sale_date: {
	            required: true,
	            dateISO: true
	        },
	        order_source: {
	            stringMaxLength: 30
	        },
	        "radio-group-type" : {
	        	radiobutton : true
			}
	    };

	    var validator_insert = $("#insert-dialog-form-post").validate({
	        rules: oRules,
			errorPlacement : function(error, element) {
				if (element.is(":radio")) {
					error.appendTo(element.parents("td"));
				} else { 
					error.insertAfter(element);
				}
			}
	    });

	    var validator_update = $("#update-dialog-form-post").validate({
	        rules: oRules,
	        errorPlacement : function(error, element) {
				if (element.is(":radio")) {
					error.appendTo(element.parents("td"));
				} else { 
					error.insertAfter(element);
				}
			}
	    });

	    //自訂商品ID查詢相關設定
	    $("#search-sale").click(function(e) {
	        e.preventDefault();

	        if ($("#search-sale").attr("c_product_id_error").length > 0) {
	            var tmp = "查無商品ID: " +
	                $("#search-sale").attr("c_product_id_error");

	            if (!confirm(tmp)) {
	                return;
	            }
	        }

	        var tmp = {
	            action: "search",
	            c_product_id: $("input[name='search_c_product_id']").val(),
	            trans_list_start_date: $("#trans_list_start_date").val(),
	            trans_list_end_date: $("#trans_list_end_date").val()
	        };
	        draw_sale(tmp);
	    });

	    //轉單日查詢相關設定
	    $("#searh-trans-list-date, #search-upload-date").click(function(e) {
	        e.preventDefault();
	        if ($("#trans_list_date_form").valid()) {
	            var tmp = {
	                action: "search_trans_list_date",
	                type: this.id,
	                trans_list_start_date: $("#trans_list_start_date").val(),
	                trans_list_end_date: $("#trans_list_end_date").val(),
	                upload_start_date: $("#upload_start_date").val(),
	                upload_end_date: $("#upload_end_date").val()
	            };
	            draw_sale(tmp);
	        }
	    });

	    //新增Dialog相關設定
	    insert_dialog = $("#dialog-form-insert").dialog({
	        draggable: true,
	        resizable: false,
	        autoOpen: false,
	        height: "auto",
	        width: "auto",
	        modal: true,
	        // 				show : {
	        // 					effect : "blind",
	        // 					duration : 300
	        // 				},
	        // 				hide : {
	        // 					effect : "fade",
	        // 					duration : 300
	        // 				},
	        buttons: [{
	                id: "insert",
	                text: "新增",
	                click: function() {
	                    if ($('#insert-dialog-form-post').valid()) {
	                        var cus_id = "";
	                        for (x in customer_menu) {
	                            if (customer_menu[x] == $("#dialog-form-insert input[name='name']").val()) {
	                                cus_id = x;
	                            }
	                        }
	                        if (cus_id.length < 1 &&
	                            $("#dialog-form-insert input[name='name']").val().length > 0) {
	                        	dialogMsg('提示',"查無客戶: '" +
		                                $("#dialog-form-insert input[name='name']").val() +
		                                "'\n 請先至客戶管理介面新增");     
	                            return;
	                        }

	                        var $insert = $("#dialog-form-insert");
	                        var tmp = {
	                            action: "insert",
	                            order_no: $insert.find("input[name='order_no']").val(),
	                            product_name: $insert.find("input[name='product_name']").val(),
	                            product_id: product_id,
	                            c_product_id: $insert.find("input[name='c_product_id']").val(),
	                            cus_id: cus_id,
	                            name: $insert.find("input[name='name']").val(),
	                            quantity: $insert.find("input[name='quantity']").val(),
	                            price: $insert.find("input[name='price']").val(),
	                            invoice: $insert.find("input[name='invoice']").val(),
	                            invoice_date: $insert.find("input[name='invoice_date']").val(),
	                            trans_list_date: $insert.find("input[name='trans_list_date']").val(),
	                            dis_date: $insert.find("input[name='dis_date']").val(),
	                            memo: $insert.find("input[name='memo']").val(),
	                            sale_date: $insert.find("input[name='sale_date']").val(),
	                            order_source: $insert.find("input[name='order_source']").val(),
	                            contrast_type: $("input[name='radio-group-type']:checked").val(),
	                            deliveryway: '1',
	                            total_amt: $insert.find("input[name='total_amt']").val()
	                        };

	                        draw_sale(tmp);
	                        insert_dialog.dialog("close");
	                        $("#insert-dialog-form-post").trigger("reset");
	                    }
	                }
	            },
	            {
	                text: "取消",
	                click: function() {
	                    $("#insert-dialog-form-post").trigger("reset");
	                    validator_insert.resetForm();
	                    insert_dialog.dialog("close");
	                }
	            }
	        ],
	        close: function() {
	            validator_insert.resetForm();
	            $("#insert-dialog-form-post").trigger("reset");
	        }
	    });
	    // 			$("#dialog-form-insert").show();

	    //確認Dialog相關設定(刪除功能)
	    confirm_dialog = $("#dialog-confirm").dialog({
	        draggable: true,
	        resizable: false,
	        autoOpen: false,
	        height: "auto",
	        width: "auto",
	        modal: true,
	        // 				show : {
	        // 					effect : "blind",
	        // 					duration : 300
	        // 				},
	        // 				hide : {
	        // 					effect : "fade",
	        // 					duration : 300
	        // 				},
	        buttons: {
	            "確認刪除": function() {
	                var tmp = {
	                    action: "delete",
	                    sale_id: uuid,
	                    c_product_id: c_product_id
	                    //c_product_id是為了刪除後，回傳指定的結果，所需參數
	                };
	                draw_sale(tmp);
	                $(this).dialog("close");
	            },
	            "取消刪除": function() {
	                $(this).dialog("close");
	            }
	        }
	    });
	    // 			$("#dialog-confirm").show();

	    //修改Dialog相關設定
	    update_dialog = $("#dialog-form-update").dialog({
	        draggable: true,
	        resizable: false,
	        autoOpen: false,
	        height: "auto",
	        width: "auto",
	        modal: true,
	        // 				show : {
	        // 					effect : "blind",
	        // 					duration : 300
	        // 				},
	        // 				hide : {
	        // 					effect : "fade",
	        // 					duration : 300
	        // 				},
	        buttons: [{
	                text: "修改",
	                click: function() {
	                    if ($('#update-dialog-form-post').valid()) {
	                        var cus_id = "";
	                        for (x in customer_menu) {
	                            if (customer_menu[x] == $("#dialog-form-update input[name='name']").val()) {
	                                cus_id = x;
	                            }
	                        }

	                        if (cus_id.length < 1 &&
	                            $("#dialog-form-update input[name='name']").val().length > 0) {
	                        	dialogMsg('提示',"查無客戶: '" +
		                                $("#dialog-form-update input[name='name']").val() +
		                                "'\n 請先至客戶管理介面新增");
	                            return;
	                        }

	                        var $update = $("#dialog-form-update");
	                        var tmp = {
	                            action: "update",
	                            sale_id: uuid,
	                            seq_no: seqNo,
	                            order_no: $update.find("input[name='order_no']").val(),
	                            product_name: $update.find("input[name='product_name']").val(),
	                            product_id: product_id,
	                            c_product_id: $update.find("input[name='c_product_id']").val(),
	                            cus_id: cus_id,
	                            name: $update.find("input[name='name']").val(),
	                            quantity: $update.find("input[name='quantity']").val(),
	                            price: $update.find("input[name='price']").val(),
	                            invoice: $update.find("input[name='invoice']").val(),
	                            invoice_date: $update.find("input[name='invoice_date']").val(),
	                            trans_list_date: $update.find("input[name='trans_list_date']").val(),
	                            dis_date: $update.find("input[name='dis_date']").val(),
	                            memo: $update.find("input[name='memo']").val(),
	                            sale_date: $update.find("input[name='sale_date']").val(),
	                            order_source: $update.find("input[name='order_source']").val(),
	                            contrast_type: $("input[name='radio-group-type']:checked").val(),
	                            deliveryway: '1',
	                            total_amt: $update.find("input[name='total_amt']").val()
	                        };

	                        draw_sale(tmp);
	                        update_dialog.dialog("close");
	                        $("#update-dialog-form-post").trigger("reset");
	                    }
	                }
	            },
	            {
	                text: "取消",
	                click: function() {
	                    validator_update.resetForm();
	                    $("#update-dialog-form-post").trigger("reset");
	                    update_dialog.dialog("close");
	                }
	            }
	        ],
	        close: function() {
	            $("#update-dialog-form-post").trigger("reset");
	            validator_update.resetForm();
	        }
	    });

	    //刪除事件聆聽 : 因為聆聽事件動態產生，所以採用delegate來批量處理，節省資源
	    $("#sales").delegate(".btn_delete", "click", function() {

	        var row = $(this).closest("tr");
	        var data = $("#sales").DataTable().row(row).data();

	        $("#dialog-confirm").html("<table class='dialog-table'>" +
	            "<tr><td>銷貨單號：</td><td><span class='delete_msg'>'" +
	            data.order_no +
	            "'</span></td></tr>" +
	            "<tr><td>交易物品：</td><td><span class='delete_msg'>'" +
	            data.product_name +
	            "'</span></td></tr>" +
	            "<tr><td>銷貨金額：</td><td><span class='delete_msg'>'" +
	            data.price +
	            "'</span></td></tr>" +
	            "<tr><td>銷貨日期：</td><td><span class='delete_msg'>'" +
	            data.sale_date +
	            "'</span></td></tr>" +
	            "</table>");

	        uuid = $(this).val();
	        c_product_id = $(this).attr("name");
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
	        uuid = $(this).val();
	        seqNo = $(this).attr("id");

	        var row = $(this).closest("tr");
	        var data = $("#sales").DataTable().row(row).data();

	        //清空查詢條件
	        $("input[name='search_c_product_id']").val("");
	        $("#trans_list_start_date, #trans_list_end_date, #upload_start_date, #upload_end_date").val("");

	        var dialogA = document.getElementById("dialog-form-update");
	        var dialogB = $("#dialog-form-update");

	        console.log(data);
	        $("#dialog-form-update input[name='order_no']").val(data.order_no);
	        $("#dialog-form-update input[name='product_name']").val(data.product_name);
	        $("#dialog-form-update input[name='c_product_id']").val(data.c_product_id);
	        $("#dialog-form-update input[name='name']").val(customer_menu[data.customer_id]);
	        $("#dialog-form-update input[name='quantity']").val(data.quantity);
	        $("#dialog-form-update input[name='price']").val(data.price);
	        $("#dialog-form-update input[name='update_product_price']").val(data.price);
	        $("#dialog-form-update input[name='invoice']").val(data.invoice);
	        $("#dialog-form-update input[name='invoice_date']").val(data.invoice_date);
	        $("#dialog-form-update input[name='trans_list_date']").val(data.trans_list_date);
	        $("#dialog-form-update input[name='dis_date']").val(data.dis_date);
	        $("#dialog-form-update input[name='memo']").val(data.memo);
	        $("#dialog-form-update input[name='sale_date']").val(data.sale_date);
	        $("#dialog-form-update input[name='order_source']").val(data.order_source);
	        $("#dialog-form-update input:radio[name='radio-group-type'][value='"+data.contrast_type+"']").prop("checked", true);

	        $.ajax({
				url : "sale.do",
				type : "POST",
				async : false,
				delay : 1500,
				data : {
					action : "get_amount_from_ext",
					orderNo : data.order_no,
				},
				success : function(data) {
					var json_obj = $.parseJSON(data);
					$.each(json_obj, function(i, item) {
		                if (item.price != null) {
		                	$("#dialog-form-update input[name='total_amt']").val(
		                			parseInt(item.price)
		                		);
		                }
		            });
				}
	        });
	        
	        
	        console.log(dialogA);
	        console.log(dialogB);

	        console.log($(dialogA).find("input[name='order_no']"));
	        console.log($(dialogB).find("input[name='order_no']"));

	        update_dialog.dialog("open");
	    });

	    //處理初始的查詢autocomplete
	    $("#search_c_product_id").autocomplete({
	        minLength: 1,
	        source: function(request, response) {
	            getProductData(request, response, "ID");
	        }
	        // 				change : function(event, ui) {
	        // 					var source = $(this).val();
	        // 					var arTemp = $(".ui-autocomplete li").map(function() {
	        // 						return $(this).text();
	        // 					});

	        // 					var found = $.inArray(source, arTemp);

	        // 					if (found < 0) {
	        // 						$("#search-sale").attr("c_product_id_error", $(this).val());
	        // 						$(this).val('');
	        // 						$(this).attr("placeholder", "請輸入正確的商品ID名稱!");
	        // 						setTimeout(function() {
	        // 							$("#search-sale").attr("c_product_id_error", "");
	        // 						}, 200);
	        // 					}
	        // 				}
	    });
	    // 			.bind('focus', function() {
	    // 				$(this).attr("placeholder", "請輸入商品ID以供查詢");
	    // 			});

	    //處理新增的名稱autocomplete
	    $("#insert_product_name").autocomplete({
	        minLength: 1,
	        source: function(request, response) {
	            getProductData(request, response, "NAME");
	        },
	        change: function(event, ui) {
	            var source = $(this).val();
	            var temp = $(".ui-autocomplete li").map(function() {
	                return $(this).text()
	            });
	            var found = $.inArray(source, temp);

	            if (found < 0) {
	                $(this).val('');
	                $(this).attr("placeholder", "請輸入正確的商品名稱!");
	            }
	        }
	    });

	    $("#insert_product_name").bind('focus', function() {
	        $(this).attr("placeholder", "請輸入商品名稱以供查詢");
	    });

	    //處理新增的自訂ID autocomplete
	    $("#insert_c_product_id").autocomplete({
	        minLength: 1,
	        source: function(request, response) {
	            console.log("source");
	            getProductData(request, response, "ID");
	        }
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
	    });
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

	    $(["#insert_c_product_id",
	        "#update_c_product_id",
	        "#insert_product_name",
	        "#update_product_name"
	    ].join(",")).bind('autocompleteselect', function(e, ui) {
	        console.log("autocompleteselect");

	        $this = $(this).closest("div");

	        $this.find("input[name=c_product_id]").val(ui.item.c_product_id);
	        $this.find("input[name=product_name]").val(ui.item.product_name);
	        $this.find("input[name=price]").val(ui.item.price);
	        $this.find("input[name=quantity]").val("1");
	        $this.find("input[name$=product_price]").val(ui.item.price);

	        product_id = ui.item.product_id;
	    });

	    //處理修改的名稱autocomplete
	    $("#update_product_name").autocomplete({
	        minLength: 1,
	        source: function(request, response) {
	            getProductData(request, response, "NAME");
	        },
	        change: function(event, ui) {
	            var source = $(this).val();
	            var temp = $(".ui-autocomplete li").map(function() {
	                return $(this).text()
	            });
	            var found = $.inArray(source, temp);

	            if (found < 0) {
	                $(this).val('');
	                $(this).attr("placeholder", "請輸入正確的商品名稱!");
	            }
	        }
	    });

	    $("#update_product_name").bind('focus', function() {
	        $(this).attr("placeholder", "請輸入商品名稱以供查詢");
	    });

	    //處理修改的自訂ID autocomplete
	    $("#update_c_product_id").autocomplete({
	        minLength: 1,
	        source: function(request, response) {
	            getProductData(request, response, "ID");
	        },
	        change: function(event, ui) {
	            var source = $(this).val();
	            var temp = $(".ui-autocomplete li").map(function() {
	                return $(this).text()
	            });
	            var found = $.inArray(source, temp);

	            if (found < 0) {
	                $(this).val('');
	                $(this).attr("placeholder", "請輸入正確的ID名稱!");
	            }
	        }
	    });

	    $("#update_c_product_id").bind('focus', function() {
	        $(this).attr("placeholder", "請輸入ID名稱以供查詢");
	    });

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

	    $('#dialog-form-insert, #dialog-form-update').delegate('input[name=quantity], input[name=price]', 'change', function() {
	        $this = $(this).closest("div");
	        $this.find("input[name$=product_price]").val(
	            $this.find("input[name=quantity]").val() * $this.find("input[name=price]").val());
	    });

	    //create list for autocomplete use
	    $.ajax({
	        type: "POST",
	        url: "customer.do",
	        data: {
	            action: "search"
	        },
	        success: function(result) {
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
	            "#insert_product_name"
	        ].join(","))
	        .dblclick(function() {
	            $(this).autocomplete({
	                minLength: 0
	            });
	        });

	    auto_complete("insert-dialog-form-post input[name='name']", customer_tags);
	    auto_complete("update-dialog-form-post input[name='name']", customer_tags);
	    order_source_auto("insert-dialog-form-post input[name='order_source']");
	    order_source_auto("update-dialog-form-post input[name='order_source']");

	    $("#warning").dialog({
	        title: "警告",
	        draggable: true, //防止拖曳
	        resizable: false, //防止縮放
	        autoOpen: false,
	        height: "auto",
	        modal: true,
	        // 				show : {
	        // 					effect : "bounce",
	        // 					duration : 1000
	        // 				},
	        // 				hide : {
	        // 					effect : "fade",
	        // 					duration : 300
	        // 				},
	        buttons: {
	            "確認": function() {
	                $(this).dialog("close");
	            }
	        }
	    });
	    $("#warning").show();
	    
	    $("input[name='radio-group-type']").on("click", function(e) {
	    	$("#dialog-select-table").dataTable().fnDestroy();
            
	    	var table = $("#dialog-select-table").DataTable({
                dom: 'fr<t>ip',
                paging: true,
                ordering: false,
                info: false,
                bLengthChange: false,
                pageLength: 10,
                scrollY:"250px",
                width: 800,
		        show: {
                    effect: "blind",
                    duration: 300
                },
                hide: {
                    effect: "fade",
                    duration: 300
                },
                language: {
                    "url": "js/dataTables_zh-tw.txt"
                },
                ajax: {
                    url: "productContrast.do",
                    dataSrc: "",
                    type: "POST",
                    data: {
                        "action": "search-product-type-list",
                        "type": $("input[name='radio-group-type']:checked").val()
                    }
                },
                columnDefs: [{
                    targets: 0,
                    searchable: false,
                    orderable: false,
                    render: function(data, type, row) {
                        var rad =
                            "<input type='radio' name='radio-group-select' value='" + 
                            row.name + ";" + row.desc + "' id = '" + row.id + "'>" +
                            "<label for='" + row.id + "'><span class='form-label'>選取</span></label>";

                        return rad;
                    }
                }],
                columns: [{
				    "data": null,
				    "defaultContent": ""
				}, {
				    "data": "c_product_id",
				    "defaultContent": ""
				}, {
				    "data": "name",
				    "defaultContent": ""
				}, {
				    "data": "desc",
				    "defaultContent": ""
				}]
            });
            
	    	select_dialog.dialog("open");
	    });

	    var select_dialog = $("#dialog-form-select").dialog({
	    	title: "商品/組合包選擇",
            draggable: true,
	        resizable: false,
	        autoOpen: false,
	        height: "auto",
	        width: "auto",
	        modal: true,
	        buttons: [{
                text: "確認",
                click: function(e) {
                	e.preventDefault();
                	if ($("input[name='radio-group-select']:checked").length === 0) {
                        $("<div></div>")
                        .text("請選擇一筆資料 !")
                       	.dialog({
                            title: "訊息",
                            resizable: false,
                            modal: true
                        });
                    } else {
                        var row = $("input[name='radio-group-select']:checked").closest('tr');
                        var row_data = $("#dialog-select-table").dataTable().fnGetData(row);
                        
                        $this = $(["#insert_c_product_id",
                	        "#update_c_product_id",
                	        "#insert_product_name",
                	        "#update_product_name"
                	    ].join(",")).closest("div");
                        
            	        $this.find("input[name=c_product_id]").val(row_data.c_product_id);
            	        $this.find("input[name=product_name]").val(row_data.name);
            	        $this.find("input[name=price]").val(row_data.price);
            	        $this.find("input[name$=product_price]").val(row_data.price);
            	        $this.find("input[name=quantity]").val("1");
            	        
            	        $("#dialog-form-select").data("product_id", row_data.id);
            	        product_id = row_data.id;
            	        
                    	select_dialog.dialog("close");
                    }
                }
            }, {
                text: "取消",
                click: function() {
                	var $radios = $("input[name='radio-group-type']");
                    if ($radios.is(':checked') === true) {
                        $radios.prop('checked', false);
                    }
                    $("#dialog-form-post-select").trigger("reset");
                    select_dialog.dialog("close");
                }
            }],
	        close: function() {
	        	select_dialog.dialog("close");
                $("#dialog-form-post-select").trigger("reset");
	        }
	    });
	    
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
	    $('.sidenav').delegate('a', 'click', function() {
	        console.log($(this).html());

	        $(".content-wrap > .page-title").html($(this).html());
	        $(".content-wrap").prepend($('<h2 class="test page-title">' + $(this).html() + '</h2>'));

	    });

	    function getProductData(request, response, kind) {
	        $.ajax({
	            url: "sale.do",
	            type: "POST",
	            cache: false,
	            delay: 1500,
	            data: {
	                action: "search_product_data",
	                identity: kind,
	                term: request.term
	            },
	            success: function(data) {
	                console.log("getProductData By " + kind);
	                var json_obj = $.parseJSON(data);
	                var result = [];

	                if (!json_obj.length) {
	                    result = [{
	                        label: '找不到符合資料',
	                        value: request.term
	                    }];
	                } else {
	                    result = $.map(json_obj, function(item) {
	                        var label = "",
	                            value = "";

	                        if (kind == "ID") {
	                            label = item.c_product_id;
	                            value = item.c_product_id;
	                        } else if (kind == "NAME") {
	                            label = item.product_name;
	                            value = item.product_name;
	                        }

	                        return {
	                            label: label,
	                            value: value,
	                            product_id: item.product_id,
	                            product_name: item.product_name,
	                            c_product_id: item.c_product_id,
	                            price: item.price,
	                            cost: item.cost
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
		}

		function drawDialog(dialogId, oUrl, oWidth, formId) {

			var dialog = document.getElementById(dialogId);
			var form = document.getElementById(formId);

			dataDialog = $(dialog).dialog({
				draggable : true,
				resizable : false,
				autoOpen : false,
				modal : true,
				open: function(event, ui) {
			        $(this).parent().children().children('.ui-dialog-titlebar-close').hide();
			    },
				buttons: [{
			        text: "關閉",
			        click: function() {
			            $(this).dialog("close");
			        }
			    }],
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
		function formatDate() {
		    var d = new Date(),
		        month = '' + (d.getMonth() + 1),
		        day = '' + d.getDate(),
		        year = d.getFullYear();

		    if (month.length < 2) month = '0' + month;
		    if (day.length < 2) day = '0' + day;

		    return [year, month, day].join('-');
		}
	</script>

	<!-- button listener -->
	<script>
		$(function() {
			var table = document.getElementById("sales");

			$(table).delegate(".btn_list", "click", function(e) {
				e.preventDefault();
				
				var row = $(this).closest("tr");
			    var data = $("#sales").DataTable().row(row).data();
			    
				//declare object and options
				var sale_id = data.sale_id;
				var dataDialog;
				var dialogId = "dialog-sale-detail";
				var dom = "lfr<t>ip";
				var oUrl = "sale.do"
				var oWidth = 1200;
				var formId = "dialog-form-sale-detail";
				var tableId = "dialog-sale-detail-table";
				var tableThs = "<th>銷貨單號</th><th>平台訂單號</th><th>商品名稱</th>"
						+ "<th>自訂商品ID</th><th>銷貨數量</th><th>單價</th>"
						+ "<th>發票號碼</th><th>發票日期</th><th>轉單日</th>"
						+ "<th>銷貨/出貨日期</th><th>銷售平台</th><th>備註說明</th>";
				var oColumnDefs = [];
				var oColumns = [ {
					"data" : "seq_no",
					"defaultContent" : ""
				}, {
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
					"data" : "invoice",
					"defaultContent" : ""
				}, {
					"data" : "invoice_date",
					"defaultContent" : ""
				}, {
					"data" : "trans_list_date",
					"defaultContent" : ""
				}, {
					"data" : "sale_date",
					"defaultContent" : ""
				}, {
					"data" : "order_source",
					"defaultContent" : ""
				}, {
					"data" : "memo",
					"defaultContent" : ""
				} ];
				
				var oData = {
					"action" : "getSaleDetail",
					"sale_id" : sale_id
				};

				//call method return dialog object to operate
				dataDialog = drawDialog(dialogId, oUrl, oWidth, formId);

				dataDialog
					.dialog("option", "title", "銷售資料明細")
					.dialog("open");

				drawDialog(dialogId, oUrl, oWidth, formId);

				//must be initialized to set table
				rebuildTable(tableId, tableThs);
				drawDataTable(tableId, dom, oUrl, oData, oColumnDefs, oColumns)
			});
		});
	</script>
	
</body>
</html>