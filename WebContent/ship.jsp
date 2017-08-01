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
<title>出貨管理</title>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<link rel="Shortcut Icon" type="image/x-icon"
	href="./images/Rockettheme-Ecommerce-Shop.ico" />
<jsp:include page="template/common_css.jsp" flush="true" />
<link rel="stylesheet" href="css/ship.css">
<link rel="stylesheet" href="css/buttons.dataTables.min.css">
</head>
<body>
	<input type="hidden" id="glb_menu" value='<%=menu%>' />
	<input type="hidden" id="glb_privilege" value="<%=privilege%>" />

	<div class="page-wrapper">
		<jsp:include page="template/common_headfoot.jsp" flush="true" />

		<div class="content-wrap">
			<h2 class="page-title">出貨管理</h2>

			<div class="panel-content">
				<div class="datalistWrap">
					<div class="input-field-wrap">
						<div class="form-wrap">
							<div class="form-row">
								<form id = "form_no">
									<label for=""> <span class="block-label">訂單編號</span> <input
										type="text" name="order_no">
									</label>
									<button class="btn btn-darkblue">查詢</button>
								</form>
							</div>
							<div class="form-row">
								<form id = "form_date">
									<label for=""> <span class="block-label">銷售起日</span> <input
										type="text" name="start_date" class='input-date'>
									</label>
									<div class='forward-mark'></div>
									<label for=""> <span class="block-label">銷售迄日</span> <input
										type="text" name="end_date" class='input-date'>
									</label>
									<button class="btn btn-darkblue">查詢</button>
								</form>
							</div>
						</div>
					</div>
				</div>
			</div>
			<div class="panel-content">
				<div class="datalistWrap">
					<div class="row search-result-wrap">
						<table id="dt_master_ship" class="result-table"></table>
					</div>
				</div>
			</div>
			<!-- 對話窗 -->
			<div id="dialog-data-process" class="dialog" align="center">
				<form name="dialog-form-data-process" id="dialog-form-data-process">
					<fieldset>
						<table class="form-table" id="dialog-data-process-table">
						</table>
					</fieldset>
				</form>
			</div>
			<div id="message">
				<div id="text"></div>
			</div>
		</div>
	</div>
	
	<!-- 報表 對話窗 -->
	<div id="dialog_report" class="dialog" align="center" style="display:none">
		<iframe src="" frameborder="0" id="dialog_report_iframe" width="850" height="450"></iframe>
	</div>
	<!-- 銷貨明細對話窗-->
	<div id="dialog-sale-detail" class="dialog" align="center" style="display:none">
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
	<!-- 下訂單對話窗-->
	<div id="dialog-sf-delivery-order" style="display:none">
		<form id ="dialog-sf-delivery-order-form">
			<fieldset>
				<table class='form-table'>
					<tr>
						<td>重量</td><td><input type="text" name="weight" placeholder="單位(千克)"></td>
					</tr>
				</table>
			</fieldset>
		</form>
	</div> 
	<!-- 黑貓對話窗-->
	<div id="dialog-egs" style="display:none">
		<form id ="dialog-egs-form">
			<section>
				<hr><h4>指定配達時段</h4>
				<div class="form-wrap">
					<div class="form-row">
						<input id="delivery-timezone-radio-1" type="radio" name="delivery-timezone-radio-group">
						<label for="delivery-timezone-radio-1">
							<span class="form-label">9~12時</span>
						</label>
	          			<input id="delivery-timezone-radio-2" type="radio" name="delivery-timezone-radio-group">
	          			<label for="delivery-timezone-radio-2">
							<span class="form-label">12~17時</span>
	          			</label>
	          			
	          			<input id="delivery-timezone-radio-3" type="radio" name="delivery-timezone-radio-group">
	          			<label for="delivery-timezone-radio-3">
							<span class="form-label">17~20時</span>
	          			</label>
	          			
	          			<input id="delivery-timezone-radio-4" type="radio" name="delivery-timezone-radio-group">
	          			<label for="delivery-timezone-radio-4">
							<span class="form-label">不限時</span>
	          			</label>
					</div>
				</div>
			</section>
			<hr><h4>指定配達日期</h4>
			<input type="text" name="delivary-date" class="self-date">
			
			<section>
				<hr><h4>尺寸</h4>
				<div class="form-wrap">
					<div class="form-row">
						<input id="package-size-0001" type="radio" name="package-size-radio-group">
						<label for="package-size-0001">
							<span class="form-label">60cm</span>
						</label>
						
	          			<input id="package-size-0002" type="radio" name="package-size-radio-group">
	          			<label for="package-size-0002">
							<span class="form-label">90cm</span>
	          			</label>
	          			
	          			<input id="package-size-0003" type="radio" name="package-size-radio-group">
	          			<label for="package-size-0003">
							<span class="form-label">120cm</span>
	          			</label>
	          			
	          			<input id="package-size-0004" type="radio" name="package-size-radio-group">
	          			<label for="package-size-0004">
							<span class="form-label">150cm</span>
	          			</label>
					</div>
				</div>
			</section>

			<section>
				<hr><h4>溫層</h4>
				<div class="form-wrap">
					<div class="form-row">
						<input id="temperature-0001" type="radio" name="temperature-radio-group">
						<label for="temperature-0001">
							<span class="form-label">常溫</span>
						</label>
						
	          			<input id="temperature-0002" type="radio" name="temperature-radio-group">
	          			<label for="temperature-0002">
							<span class="form-label">冷藏</span>
	          			</label>
	          			
	          			<input id="temperature-0003" type="radio" name="temperature-radio-group">
	          			<label for="temperature-0003">
							<span class="form-label">冷凍</span>
	          			</label>

					</div>
				</div>
			</section>
						
			<section>
				<hr><h4>託運單類別</h4>
				<div class="form-wrap">
					<div class="form-row">
						<input id="waybill-type-A" type="radio" name="waybill-type-radio-group">
						<label for="waybill-type-A">
							<span class="form-label">一般</span>
						</label>
	          			<input id="waybill-type-B" type="radio" name="waybill-type-radio-group">
	          			<label for="waybill-type-B">
							<span class="form-label">代收</span>
	          			</label>
	          			
	          			<input id="waybill-type-G" type="radio" name="waybill-type-radio-group">
	          			<label for="waybill-type-G">
							<span class="form-label">報值</span>
	          			</label>
					</div>
				</div>
			</section>
			
			<hr><h4>備註</h4>
			<input type="text" name="comment">
		</form>
	</div> 
	<!-- 黑貓對話窗-->
	<div id="dialog-egs" style="display:none">
		<form id ="dialog-egs-form">
			<section>
				<hr><h4>指定配達時段</h4>
				<div class="form-wrap">
					<div class="form-row">
						<input id="delivery-timezone-1" type="radio" name="delivery-timezone-radio-group" checked>
						<label for="delivery-timezone-1">
							<span class="form-label">9~12時</span>
						</label>
	          			<input id="delivery-timezone-2" type="radio" name="delivery-timezone-radio-group">
	          			<label for="delivery-timezone-2">
							<span class="form-label">12~17時</span>
	          			</label>
	          			
	          			<input id="delivery-timezone-3" type="radio" name="delivery-timezone-radio-group">
	          			<label for="delivery-timezone-3">
							<span class="form-label">17~20時</span>
	          			</label>
	          			
	          			<input id="delivery-timezone-4" type="radio" name="delivery-timezone-radio-group">
	          			<label for="delivery-timezone-4">
							<span class="form-label">不限時</span>
	          			</label>
					</div>
				</div>
			</section>
			<hr><h4>指定配達日期</h4>
			<input type="text" name="delivery-date" class="self-date">
			
			<section>
				<hr><h4>尺寸</h4>
				<div class="form-wrap">
					<div class="form-row">
						<input id="package-size-0001" type="radio" name="package-size-radio-group" checked>
						<label for="package-size-0001">
							<span class="form-label">60cm</span>
						</label>
						
	          			<input id="package-size-0002" type="radio" name="package-size-radio-group">
	          			<label for="package-size-0002">
							<span class="form-label">90cm</span>
	          			</label>
	          			
	          			<input id="package-size-0003" type="radio" name="package-size-radio-group">
	          			<label for="package-size-0003">
							<span class="form-label">120cm</span>
	          			</label>
	          			
	          			<input id="package-size-0004" type="radio" name="package-size-radio-group">
	          			<label for="package-size-0004">
							<span class="form-label">150cm</span>
	          			</label>
					</div>
				</div>
			</section>

			<section>
				<hr><h4>溫層</h4>
				<div class="form-wrap">
					<div class="form-row">
						<input id="temperature-0001" type="radio" name="temperature-radio-group" checked>
						<label for="temperature-0001">
							<span class="form-label">常溫</span>
						</label>
						
	          			<input id="temperature-0002" type="radio" name="temperature-radio-group">
	          			<label for="temperature-0002">
							<span class="form-label">冷藏</span>
	          			</label>
	          			
	          			<input id="temperature-0003" type="radio" name="temperature-radio-group">
	          			<label for="temperature-0003">
							<span class="form-label">冷凍</span>
	          			</label>

					</div>
				</div>
			</section>
						
			<section>
				<hr><h4>託運單類別</h4>
				<div class="form-wrap">
					<div class="form-row">
						<input id="waybill-type-A" type="radio" name="waybill-type-radio-group" checked>
						<label for="waybill-type-A">
							<span class="form-label">一般</span>
						</label>
	          			<input id="waybill-type-B" type="radio" name="waybill-type-radio-group">
	          			<label for="waybill-type-B">
							<span class="form-label">代收</span>
	          			</label>
	          			<!-- 報值先關起來
	          			<input id="waybill-type-G" type="radio" name="waybill-type-radio-group">
	          			<label for="waybill-type-G">
							<span class="form-label">報值</span>
	          			</label>
	          			 -->
					</div>
				</div>
			</section>
			
			<hr><h4>備註</h4>
			<input type="text" name="comment">
		</form>
	</div>

	<jsp:include page="template/common_js.jsp" flush="true" />
	<script type="text/javascript" src="js/dataTables.buttons.min.js"></script>
	<script type="text/javascript" src="js/buttons.jqueryui.min.js"></script>
	<script type="text/javascript" src="js/buttons.colVis.min.js"></script>
	
	<script>
	var $dtMaster = null; //主要datatable
	var selectCount = 0; //全選按鈕計算用
	</script>
	<script type="text/javascript">
	$(function(){
		$('#form_no').on("click", "button", function(e) {
			e.preventDefault();
			
			var errorMes = '';
			var $mes = $('#message #text');
			var $orderNo = $('#form_no input[name=order_no]').val();
			
			if($.trim($orderNo).length == 0){
				errorMes += "請輸入訂單編號!";
			}

			if(errorMes.length > 0){
				$mes.val('').html(errorMes);
				$('#message')
					.dialog()
					.dialog('option', 'title', '警告')
					.dialog('option', 'width', 'auto')
					.dialog('option', 'minHeight', 'auto')
					.dialog("open");
				return false;
			}
			var parameter = {
				action : "searchByOrderNo",
				orderNo : $orderNo
			};
			
			drawMasterTable(parameter);
		});		
		$('#form_date').on("click", "button", function(e) {
			e.preventDefault();
			var $startDate = $('#form_date input:eq(0)').val();
			var $endDate = $('#form_date input:eq(1)').val();
			var errorMes = '';
			var $mes = $('#message #text');
			if($endDate != null && $endDate.length != 0 && $startDate != null && $startDate.length != 0){
				if($startDate.replace(/-/g,"") > $endDate.replace(/-/g,"")){
					errorMes += "訖日不可小於起日!<br>";
				}
			}
			if($startDate == null || $startDate.length == 0){
				errorMes += "請選擇起日!<br>";
			}
			else if(!dateValidationCheck($startDate)){
				errorMes += "起日格式不符!<br>";
			}
			if($endDate == null || $endDate.length == 0){
				errorMes += "請選擇訖日!<br>";
			}
			else if(!dateValidationCheck($endDate)){
				errorMes += "訖日格式不符!<br>";
			}

			if(errorMes.length > 0){
				$mes.val('').html(errorMes);
				$('#message')
					.dialog()
					.dialog('option', 'title', '警告')
					.dialog('option', 'width', 'auto')
					.dialog('option', 'minHeight', 'auto')
					.dialog("open");
				return false;
			}
			var parameter = {
				action : "searchBySaleDate",
				startDate : $startDate,
				endDate : $endDate
			};
			
			drawMasterTable(parameter);
		});
	    $('#dt_master_ship').on('change', ':checkbox', function() {
	        $(this).is(":checked")?
	        	$(this).closest("tr").addClass("selected"):
	        	$(this).closest("tr").removeClass("selected");
	    });
	});
	</script>
	<script type="text/javascript">
	function drawMasterTable(parameter) {

		$dtMaster = $("#dt_master_ship").DataTable({
		    dom: "frB<t>ip",
		    lengthChange: false,
		    pageLength: 20,
		    scrollY: "290px",
		    width: 'auto',
		    scrollCollapse: true,
		    destroy: true,
		    language: {
		        "url": "js/dataTables_zh-tw.txt",
		        "emptyTable": "查無資料",
		    },
		    initComplete: function(settings, json) {
		        $('div .dt-buttons').css({
		            'float': 'left',
		            'margin-left': '10px'
		        });
		        $('div .dt-buttons a').css('margin-left', '10px');
		    },
		    ajax: {
		        url: "ship.do",
		        dataSrc: "",
		        type: "POST",
		        data: parameter
		    },
		    columns: [{
		        "title": "批次請求",
		        "data": null,
		        "defaultContent": ""
		    }, {
		        "title": "訂單編號",
		        "data": "order_no",
		        "defaultContent": ""
		    }, {
		        "title": "商品編號",
		        "data": "v_c_product_id",
		        "defaultContent": ""
		    }, {
		        "title": "商品名稱",
		        "data": "v_product_name",
		        "defaultContent": ""
		    }, {
		        "title": "客戶姓名",
		        "data": "name",
		        "defaultContent": ""
		    }, {
		        "title": "備註",
		        "data": "memo",
		        "defaultContent": ""
		    }, {
		        "title": "出貨方式",
		        "data": "deliveryway",
		        "defaultContent": ""
		    }, {
		        "title": "訂單總額",
		        "data": "total_amt",
		        "defaultContent": ""
		    }, {
		        "title": "收件人姓名",
		        "data": "deliver_name",
		        "defaultContent": ""
		    }, {
		        "title": "收件地點",
		        "data": "deliver_to",
		        "defaultContent": ""
		    }, {
		        "title": "功能",
		        "data": null,
		        "defaultContent": ""
		    }],
		    columnDefs: [{
		        targets: 0,
		        searchable: false,
		        orderable: false,
		        render: function(data, type, row) {
		            var ship_seq_no = row.ship_seq_no;

		            var input = document.createElement("INPUT");
		            input.type = 'checkbox';
		            input.name = 'checkbox-group-select';
		            input.id = ship_seq_no;

		            var span = document.createElement("SPAN");
		            span.className = 'form-label';

		            var label = document.createElement("LABEL");
		            label.htmlFor = ship_seq_no;
		            label.name = 'checkbox-group-select';
		            label.style.marginLeft = '35%';
		            label.appendChild(span);

		            var options = $("<div/>").append(input, label);

		            return options.html();
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
		                                "class": "btn-in-table btn-green btn_list",
		                                "title": "清單"
		                            })
		                            .append($("<i/>", {
		                                "class": "fa fa-pencil-square-o"
		                            }))
		                        )
		                        .append(
		                            $("<button/>", {
		                                "id": row.seq_no,
		                                "value": row.sale_id,
		                                "name": row.c_product_id,
		                                "class": "btn-in-table  btn-darkblue btn_ship_report_a4_2",
		                                "title": "黑貓A4_2模"
		                            })
		                            .append($("<i/>", {
		                                "class": "fa fa-file-pdf-o"
		                            }))
		                        )
								.append(
		                            $("<button/>", {
		                                "id": row.seq_no,
		                                "value": row.sale_id,
		                                "name": row.c_product_id,
		                                "class": "btn-in-table  btn-alert btn_ship_report_a4_3",
		                                "title": "黑貓A4_3模"
		                            })
		                            .append($("<i/>", {
		                                "class": "fa fa-file-pdf-o"
		                            }))
		                        )
		                        
		                    )
		                );

		            return options.html();
		        }
		    }],
		    buttons: [{
		            text: '全選',
		            action: function(e, dt, node, config) {

		                selectCount++;
		                var $table = $('#dt_master_ship');
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
		        }, {
		            text: '順豐出庫',
		            action: function(e, dt, node, config) {
		                var $table = $('#dt_master_ship');

		                var cells = $dtMaster.cells().nodes();
		                var noArr = '';

		                var $checkboxs = $(cells).find('input[name=checkbox-group-select]:checked');

		                if ($checkboxs.length == 0) {
		                    alert('請至少選擇一筆資料');
		                    return false;
		                }
		                if ($checkboxs.length > 20) {
		                    alert('最多選擇二十筆資料');
		                    return false;
		                }


		                $checkboxs.each(function() {
		                    noArr += this.id + ',';
		                });
		                noArr = noArr.slice(0, -1);
		                $.ajax({
		                    url: 'ship.do',
		                    type: 'post',
		                    data: {
		                        action: 'sendToTelegraph',
		                        ship_seq_nos: noArr
		                    },
		                    error: function(xhr) {},
		                    success: function(response) {
								var msg = "";
								if (response) {
									
									var json_obj = $.parseJSON(response);
									
									if (json_obj.response) {
										if (json_obj.response.head == "OK" || json_obj.response.head == "PART") {
											var sale = json_obj.response.body.saleOrderResponse.saleOrders.saleOrder;
											
											$.each(sale, function(key, value) {
												var suc = value.result == 1? "/" + "出庫單號：" + value.shipmentId:"/" + "失敗";
												var note = value.result == 1? "": "/" + value.note;
												msg += "訂單編號：" + value.erpOrder + suc + note + "<br/>";
											});
										} else if (json_obj.response.head == "ERR") {
											msg += "代碼：" +  json_obj.response.error.code + 
												"/原因：" +  json_obj.response.error.error;
										}
									} else if (json_obj.responseFail) {
										msg = json_obj.responseFail.remark;
									}										
								}

								dialogMsg("順豐出庫", msg);
		                    }
		                });
		            }
		        }, {
		            text: '順豐出庫取消',
		            action: function(e, dt, node, config) {
		                var $table = $('#dt_master_ship');

		                var cells = $dtMaster.cells().nodes();
		                var noArr = '';

		                var $checkboxs = $(cells).find('input[name=checkbox-group-select]:checked');

		                if ($checkboxs.length == 0) {
		                    alert('請至少選擇一筆資料');
		                    return false;
		                }
		                if ($checkboxs.length > 20) {
		                    alert('最多選擇二十筆資料');
		                    return false;
		                }
		                $checkboxs.each(function() {
		                    noArr += this.id + ',';
		                });
		                noArr = noArr.slice(0, -1);
		                $.ajax({
		                    url: 'ship.do',
		                    type: 'post',
		                    data: {
		                        action: 'sendToCancelSaleOrderService',
		                        ship_seq_nos: noArr
		                    },
		                    error: function(xhr) {},
		                    success: function(response) {
								var msg = "";
								if (response) {
									
									var json_obj = $.parseJSON(response);
									
									if (json_obj.response) {
										if (json_obj.response.head == "OK" || json_obj.response.head == "PART") {
											var sale = json_obj.response.body.cancelSaleOrderResponse.saleOrders.saleOrder;
											
											$.each(sale, function(key, value) {
												var suc = "/" + (value.result == 1? "成功":"失敗");
												var note = "/" + value.note;
												msg += "訂單編號：" + value.erpOrder + suc + note + "<br/>";
											});
										} else if (json_obj.response.head == "ERR") {
											msg += "代碼：" +  json_obj.response.error.code + 
												"/原因：" +  json_obj.response.error.error;
										}
									} else if (json_obj.responseFail) {
										msg = json_obj.responseFail.remark;
									}										
								}

								dialogMsg("順豐出庫取消", msg);
		                    }
		                });
		            }
		        },{
		            text: '順豐出庫明細查詢',
		            action: function(e, dt, node, config) {
		                var $table = $('#dt_master_ship');

		                var cells = $dtMaster.cells().nodes();
		                var noArr = '';

		                var $checkboxs = $(cells).find('input[name=checkbox-group-select]:checked');
		                
		                if ($checkboxs.length == 0) {
		                    alert('請至少選擇一筆資料');
		                    return false;
		                }
		                if ($checkboxs.length > 20) {
		                    alert('最多選擇二十筆資料');
		                    return false;
		                }


		                $checkboxs.each(function() {
		                    noArr += this.id + ',';
		                });
		                noArr = noArr.slice(0, -1);
		                $.ajax({
		                    url: 'ship.do',
		                    type: 'post',
		                    data: {
		                        action: 'saleOrderOutboundDetailQueryService',
		                        ship_seq_nos: noArr
		                    },
		                    error: function(xhr) {},
		                    success: function(response) {
								var msg = "";
								if (response) {
									
									var json_obj = $.parseJSON(response);
									
									if (json_obj.response) {
										if (json_obj.response.head == "OK" || json_obj.response.head == "PART") {
											var sale = json_obj.response.body.saleOrderOutboundDetailResponse.saleOrders.saleOrder;
											
											$.each(sale, function(key, value) {
												var suc = value.result == 1? "/" + "出庫單號：" + value.header.shipmentId:"/" + "失敗";
												var note = value.result == 1? "": "/" + value.note;
												
												var tmp_item_list = "";
												if (value.items) {
													$.each(value.items.itemList, function(item_key, item_value){
														var qty = "";
														if (item_value.actualQty) {
															qty += "/實發數量:" + item_value.actualQty;
														}
														tmp_item_list += item_value.skuNo + qty + "<br/>";
													});
												}
												
												if (value.result == 1) {
													msg += "訂單編號：" + value.erpOrder + "/" + value.header.wayBillNo + "/" + 
													value.header.dataStatus + "<br/>" + tmp_item_list + "<br/>";
												} else {
													msg += "訂單編號：" + value.erpOrder + suc + note + "<br/>";
												}
												
											});
										} else if (json_obj.response.head == "ERR") {
											msg += "代碼：" +  json_obj.response.error.code + 
												"/原因：" +  json_obj.response.error.error;
										}
									} else if (json_obj.responseFail) {
										msg = json_obj.responseFail.remark;
									}										
								}

								dialogMsg("順豐出庫明細查詢", msg);
		                    }
		                });
		            }
		        },{
		            text: '順豐快遞',
		            action: function(e, dt, node, config) {
		                var $table = $('#dt_master_ship');

		            	var ships = new Map();
		                var cells = $dtMaster.cells().nodes();
						var row;
						var data;
						var message = '';

		                var $checkboxs = $(cells).find('input[name=checkbox-group-select]:checked');

		                if ($checkboxs.length == 0) {
		                    alert('請至少選擇一筆資料');
		                    return false;
		                }
		                
						$checkboxs.each(function() {
							row = $(this).closest("tr");
							data = $table.DataTable().row(row).data();
							ships.set( data.order_no, data.order_no );
						});
						
						if(ships.size> 1){
							message = message.concat('以下為您所勾選的訂單↓<br><br>');
							var table = document.createElement('table');
							ships.forEach(function(value, key, fullArray){
								var tr = document.createElement('tr');
								var text = document.createTextNode(key);
								tr.appendChild(text);
								table.appendChild(tr);
							});
							var $mes = $('#message #text');
							$mes.val('').html(message).append(table);
							$('#message')
								.dialog()
								.dialog('option', 'title', '警告訊息(只允許同一張訂單)')
								.dialog('option', 'width', '322.6px')
								.dialog('option', 'minHeight', 'auto')
								.dialog("open");
						}else{
							jQuery.validator.addMethod("decimal", function(value, element) {
								var decimal = /^-?\d+(\.\d{1,3})?$/;
								return this.optional(element) || (decimal.test(value));
							},"小數點不能超過三位");
							
							var validator_order = 
								$("#dialog-sf-delivery-order-form").validate({
									rules : {
										weight : {
											number: true,
											decimal : true
										}
									}
								});
							
							$("#dialog-sf-delivery-order").dialog({
								draggable : true,
								resizable : false,
								height : "auto",
								width : "auto",
								modal : true,
								title : '訂單貨物總重量',
								buttons : [{
											text : "發送",
											click : function() {
												if ($('#dialog-sf-delivery-order-form').valid()) {
													var shipsArr = [];
													var jsonList = '';
													$checkboxs.each(function(i,item) {
														row = $(this).closest("tr");
														data = $table.DataTable().row(row).data();
														$.ajax ({
															url : "realsale.do",
															type : "POST",
															async: false,
															data : {
																"action" : "getRealSaleDetail",
																"realsale_id" : data.realsale_id
															},
															success: function (response) {
																var a =$.parseJSON(response);
																data['detail'] = a;
															}
														});
														console.log(data);
														shipsArr.push(data);
													});
													jsonList = JSON.stringify(shipsArr);

													console.log(jsonList);
							 		                $.ajax({
									                    url: 'ship.do',
									                    type: 'post',
									                    data: {
									                        action: 'SFDelivery',
									                        weight: $("#dialog-sf-delivery-order-form").find("input[name=weight]").val(),
									                        jsonList: jsonList
									                    },
										                beforeSend: function(){
									                		 $(':hover').css('cursor','progress');
										                },
										                complete: function(){
									                		 $(':hover').css('cursor','default');
										                },
									                    error: function(xhr) {},
									                    success: function(response) {
															var json_obj = $.parseJSON(response);
															var text = '';
									                        var $mes = $('#message #text');
									                        
															if( json_obj.error != null ){
																var code = json_obj.error.code;
																var value = json_obj.error.value;
																if(code != null | code == 'undefined' ){
																	text += '失敗 / ' + code + ' / ' + value;
																}else{
																	text += '失敗 / ' + value;
																}
															}
															if( json_obj.body != null ){
																var orderid = json_obj.body.orderResponse.orderid;
																var mailno = json_obj.body.orderResponse.mailno;
																var filter_result = json_obj.body.orderResponse.filter_result;
																
																if(filter_result == '1'){
																	filter_result = '人工確認';
																}else if(filter_result == '2'){
																	filter_result = '可收派';
																}else if(filter_result == '2'){
																	filter_result = '不可以收派';
																}
																text += '成功 / 訂單編號: ' + orderid + ' / 託運單號: ' + mailno + ' / 結果: '+ filter_result;
															}
															
									                        $mes.val('').html(text);
									                        $('#message')
									                            .dialog()
									                            .dialog('option', 'title', '提示訊息')
									                            .dialog('option', 'width', 'auto')
									                            .dialog('option', 'minHeight', 'auto')
									                            .dialog("open");
									                    }
													});										
													$(this).dialog("close");
												}
											}
										}, {
											text : "取消",
											click : function() {
												validator_order.resetForm();
												$("#dialog-sf-delivery-order-form").trigger("reset");
												$(this).dialog("close");
											}
										} ],
								close : function() {
									validator_order.resetForm();
									$("#dialog-sf-delivery-order-form").trigger("reset");
								}
							});
													
						}
		            }
		        },{
		            text: '順豐快遞取消',
		            action: function(e, dt, node, config) {
		                var $table = $('#dt_master_ship');

		            	var ships = new Map();
		                var cells = $dtMaster.cells().nodes();
						var row;
						var data;
						var message = '';

		                var $checkboxs = $(cells).find('input[name=checkbox-group-select]:checked');

		                if ($checkboxs.length == 0) {
		                    alert('請至少選擇一筆資料');
		                    return false;
		                }

						$checkboxs.each(function() {
							row = $(this).closest("tr");
							data = $table.DataTable().row(row).data();
							ships.set( data.order_no, data.order_no );
						});
						
						if(ships.size> 1){
							message = message.concat('以下為您所勾選的訂單↓<br><br>');
							var table = document.createElement('table');
							ships.forEach(function(value, key, fullArray){
								var tr = document.createElement('tr');
								var text = document.createTextNode(key);
								tr.appendChild(text);
								table.appendChild(tr);
							});
							var $mes = $('#message #text');
							$mes.val('').html(message).append(table);
							$('#message')
								.dialog()
								.dialog('option', 'title', '警告訊息(只允許同一張訂單)')
								.dialog('option', 'width', '322.6px')
								.dialog('option', 'minHeight', 'auto')
								.dialog("open");
						}else{
							var mapIter = ships.values();
							var orderNo = mapIter.next().value;
							console.log(orderNo);
							
 	 		                $.ajax({
			                    url: 'ship.do',
			                    type: 'post',
			                    data: {
			                        action: 'SFDeliveryOrderConfirmCancel',
			                        orderNo: orderNo
			                    },
				                beforeSend: function(){
			                		 $(':hover').css('cursor','progress');
				                },
				                complete: function(){
			                		 $(':hover').css('cursor','default');
				                },
			                    error: function(xhr) {},
			                    success: function(response) {
									var json_obj = $.parseJSON(response);
									var text = '';
			                        var $mes = $('#message #text');
			                        
									if( json_obj.error != null ){
										var code = json_obj.error.code;
										var value = json_obj.error.value;
										if(code != null | code == 'undefined' ){
											text += '失敗 / ' + code + ' / ' + value;
										}else{
											text += '失敗 / ' + value;
										}
									}
									if( json_obj.body != null ){
										console.log(json_obj);
										var orderid = json_obj.body.orderConfirmResponse.orderid;
										var res_status = json_obj.body.orderConfirmResponse.res_status;
										res_status = res_status == '1' ? '客戶訂單號與順豐運單不匹配':'操作成功'
										text += '成功 / 訂單編號: ' + orderid + ' / 備註: '+ res_status;
									}
									
			                        $mes.val('').html(text);
			                        $('#message')
			                            .dialog()
			                            .dialog('option', 'title', '提示訊息')
			                            .dialog('option', 'width', 'auto')
			                            .dialog('option', 'minHeight', 'auto')
			                            .dialog("open");
			                    }
							});							
						}
		            }
		        },{
		            text: '順豐快遞結果查詢',
		            action: function(e, dt, node, config) {
		                var $table = $('#dt_master_ship');

		            	var ships = new Map();
		                var cells = $dtMaster.cells().nodes();
						var row;
						var data;
						var message = '';

		                var $checkboxs = $(cells).find('input[name=checkbox-group-select]:checked');

		                if ($checkboxs.length == 0) {
		                    alert('請至少選擇一筆資料');
		                    return false;
		                }

						$checkboxs.each(function() {
							row = $(this).closest("tr");
							data = $table.DataTable().row(row).data();
							ships.set( data.order_no, data.order_no );
						});
						
						if(ships.size> 1){
							message = message.concat('以下為您所勾選的訂單↓<br><br>');
							var table = document.createElement('table');
							ships.forEach(function(value, key, fullArray){
								var tr = document.createElement('tr');
								var text = document.createTextNode(key);
								tr.appendChild(text);
								table.appendChild(tr);
							});
							var $mes = $('#message #text');
							$mes.val('').html(message).append(table);
							$('#message')
								.dialog()
								.dialog('option', 'title', '警告訊息(只允許同一張訂單)')
								.dialog('option', 'width', '322.6px')
								.dialog('option', 'minHeight', 'auto')
								.dialog("open");
						}else{
							var mapIter = ships.values();
							var orderNo = mapIter.next().value;
							console.log(orderNo);
							
 	 		                $.ajax({
			                    url: 'ship.do',
			                    type: 'post',
			                    data: {
			                        action: 'SFDeliveryOrderSearchService',
			                        orderNo: orderNo
			                    },
				                beforeSend: function(){
			                		 $(':hover').css('cursor','progress');
				                },
				                complete: function(){
			                		 $(':hover').css('cursor','default');
				                },
			                    error: function(xhr) {},
			                    success: function(response) {
									var json_obj = $.parseJSON(response);
									var text = '';
			                        var $mes = $('#message #text');
			                        
									if( json_obj.error != null ){
										var code = json_obj.error.code;
										var value = json_obj.error.value;
										if(code != null | code == 'undefined' ){
											text += '失敗 / ' + code + ' / ' + value;
										}else{
											text += '失敗 / ' + value;
										}
									}
									if( json_obj.body != null ){
										var orderid = json_obj.body.orderResponse.orderid;
										var mailno = json_obj.body.orderResponse.mailno;
										var origincode = json_obj.body.orderResponse.origincode;
										var destcode = json_obj.body.orderResponse.destcode;
										text += '成功 / 訂單編號: ' 
												+ orderid + ' / 託運單號: ' 
												+ mailno + ' / 原寄地區域代碼: '
												+ origincode + ' / 目的地區域代碼: '
												+ destcode;
									}
			                        $mes.val('').html(text);
			                        $('#message')
			                            .dialog()
			                            .dialog('option', 'title', '提示訊息')
			                            .dialog('option', 'width', 'auto')
			                            .dialog('option', 'minHeight', 'auto')
			                            .dialog("open");
			                    }
							});							
						}
		            }
		        },{
		            text: '順豐快遞路由查詢',
		            action: function(e, dt, node, config) {
		                var $table = $('#dt_master_ship');

		            	var ships = new Map();
		                var cells = $dtMaster.cells().nodes();
						var row;
						var data;
						var message = '';
						var orderNos = '';

		                var $checkboxs = $(cells).find('input[name=checkbox-group-select]:checked');

		                if ($checkboxs.length == 0) {
		                    alert('請至少選擇一筆資料');
		                    return false;
		                }
		                if ($checkboxs.length > 10) {
		                    alert('最多選擇十筆資料');
		                    return false;
		                }

						$checkboxs.each(function() {
							row = $(this).closest("tr");
							data = $table.DataTable().row(row).data();
							ships.set( data.order_no, data.order_no );
						});
						
						ships.forEach(function (item, key, mapObj) {
							orderNos += (item + ',');
						});
						orderNos= orderNos.substring(0,orderNos.length-1);
						
						console.log(orderNos);

 		                $.ajax({
		                    url: 'ship.do',
		                    type: 'post',
		                    data: {
		                        action: 'SFDeliveryRouteService',
		                        type: '1',//前端參數控制後端傳遞 1:託運單號 2:訂單號
		                        orderNos: orderNos
		                    },
			                beforeSend: function(){
		                		 $(':hover').css('cursor','progress');
			                },
			                complete: function(){
		                		 $(':hover').css('cursor','default');
			                },
		                    error: function(xhr) {},
		                    success: function(response) {
								var json_obj = $.parseJSON(response);
								var text = '';
		                        var $mes = $('#message #text');
		                        
								if( json_obj.error != null ){
									var code = json_obj.error.code;
									var value = json_obj.error.value;
									if(code != null | code == 'undefined' ){
										text += '失敗 / ' + code + ' / ' + value;
									}else{
										text += '失敗 / ' + value;
									}
								}
								if( json_obj.body != null ){
									var mailno = json_obj.body.routeResponse.mailno;
									var orderid = json_obj.body.routeResponse.orderid;
									
									var routes = json_obj.body.routeResponse.routes;
									
									if((orderid != null) && (mailno == null)){
										text += ('訂單編號: '+ orderid + ' / 成功<br>') ;
									}
									
									if((orderid != null) && (mailno != null)){
										text += ('訂單編號: '+ orderid + ' / 託運單號: ' + mailno + ' / 成功<br>') ;
									}
									
									if((orderid == null) && (mailno != null)){
										text += ('託運單號: ' + mailno + ' / 成功<br>') ;
									}

									if(routes != null){
										$.each(routes, function(index, item) {
											var accept_time = item.accept_time;
											var remark = item.remark;
												text += '時間: '
												+ accept_time + ' / 狀態: '
												+ remark  + '<br>';
										});
									}
								}
		                        $mes.val('').html(text);
		                        $('#message')
		                            .dialog()
		                            .dialog('option', 'title', '提示訊息')
		                            .dialog('option', 'width', 'auto')
		                            .dialog('option', 'minHeight', 'auto')
		                            .dialog("open");
		                    }
						});	
		            }
		        },{
		            text: '黑貓託運',
		            action: function(e, dt, node, config) {
		                var $table = $('#dt_master_ship');

		            	var orders = new Map(); //儲存訂單
		            	var productNames = '';
		            	var shipIds = '';
		            	var realsaleIds = '';
		            	
		                var cells = $dtMaster.cells().nodes();
						var row;
						var data;
						var message = '';

		                var $checkboxs = $(cells).find('input[name=checkbox-group-select]:checked');

		                if ($checkboxs.length == 0) {
		                    alert('請至少選擇一筆資料');
		                    return false;
		                }

						$checkboxs.each(function() {
							row = $(this).closest("tr");
							data = $table.DataTable().row(row).data();
							console.log(data);
							orders.set( data.order_no, data.order_no );
							productNames+= (','+ data.v_product_name);
							shipIds += (','+ data.ship_id);
							realsaleIds += ( ",'" + data.realsale_id + "'");
						});
						
						if(realsaleIds.length != 0){
							realsaleIds = realsaleIds.substring( 1, realsaleIds.length);
						}
						
						if(productNames.length != 0){
							productNames = productNames.substring( 1, productNames.length);
						}

						if(shipIds.length != 0){
							shipIds = shipIds.substring( 1, shipIds.length);
						}
						
						console.log('orders');
						console.log(orders);
						console.log('productNames');
						console.log(productNames);

						if(orders.size> 1){
							message = message.concat('以下為您所勾選的訂單↓<br><br>');
							var table = document.createElement('table');
							orders.forEach(function(value, key, fullArray){
								var tr = document.createElement('tr');
								var text = document.createTextNode(key);
								tr.appendChild(text);
								table.appendChild(tr);
							});
							var $mes = $('#message #text');
							$mes.val('').html(message).append(table);
							$('#message')
								.dialog()
								.dialog('option', 'title', '警告訊息(只允許同一張訂單)')
								.dialog('option', 'width', '322.6px')
								.dialog('option', 'minHeight', 'auto')
						}else{
							var opt = {
									   dayNamesMin:["日","一","二","三","四","五","六"],
									   monthNames:["1","2","3","4","5","6","7","8","9","10","11","12"],
									   monthNamesShort:["1","2","3","4","5","6","7","8","9","10","11","12"],
									   prevText:"上月",
									   nextText:"次月",
									   weekHeader:"週",
									   showMonthAfterYear:true,
									   dateFormat:"yy-mm-dd",
									   changeYear: true,
									   changeMonth: true,
									   minDate : 0
									   };
							$("#dialog-egs-form").find("input[name=delivery-date]" ).datepicker(opt);
							$("#dialog-egs-form").find("input[name=delivery-date]" ).val(new Date().toJSON().slice(0,10));
							$('#dialog-egs').dialog({
								autoOpen: false,
								draggable : true,
								resizable : false,
								overflow: 'auto',
								width : 'auto',
								modal : true,
								title : '傳送託運單資料',
								buttons : [{
											text : "確認",
											click : function() {
												
												var timezone_type_str = $("input[name='delivery-timezone-radio-group']:checked", '#dialog-egs-form').attr("id");
												var waybill_type_str = $("input[name='waybill-type-radio-group']:checked", '#dialog-egs-form').attr("id");
												var temperature_str = $("input[name='temperature-radio-group']:checked", '#dialog-egs-form').attr("id");
												var package_size_str = $("input[name='package-size-radio-group']:checked", '#dialog-egs-form').attr("id");
												var delivery_timezone_str = $("input[name='delivery-timezone-radio-group']:checked", '#dialog-egs-form').attr("id");
												
												
												console.log('timezone_type_str: ' + timezone_type_str);
												console.log('waybill_type_str: ' + waybill_type_str);
												console.log('temperature_str: ' + temperature_str);

								                $.ajax({
								                    url: 'Egs.do',
								                    type: 'post',
								                    data: {
								                        action: 'transfer_waybill',
					 			                        orderNo: data.order_no,
								                        realsale_id: data.realsale_id,
					 			                        receiver_name: data.deliver_name,
								                        receiver_address: data.deliver_to,
								                        service_type: timezone_type_str.substring( timezone_type_str.length, timezone_type_str.length -1 ),
								                        waybill_type: waybill_type_str.substring( waybill_type_str.length, waybill_type_str.length -1 ),
								                        temperature : temperature_str.substring( temperature_str.length, temperature_str.length -1 ),
								                        package_size : package_size_str.substring( package_size_str.length, package_size_str.length -1 ),
								                        delivery_timezone : delivery_timezone_str.substring( delivery_timezone_str.length, delivery_timezone_str.length -1 ),
								                        delivery_date : $("#dialog-egs-form input[name='delivery-date']").val(),
								                        comment : $("input[name='comment']", '#dialog-egs-form').val(),
								                        product_name : productNames,
								                        shipIds : shipIds,
								                        realsaleIds : realsaleIds
								                    },
									                beforeSend: function(){
								                		 $(':hover').css('cursor','progress');
									                },
									                complete: function(){
								                		 $(':hover').css('cursor','default');
									                },
								                    error: function(xhr) {},
								                    success: function(response) {
								                    	
								                    	console.log('response: '+ response);
								                    	var json_obj = $.parseJSON(response);
								                    	var text = '';
								                    	
														$("#dialog-egs-form").trigger("reset");
														$('#dialog-egs').dialog("close");

														if(json_obj.status == 'OK'){
															text = '傳送託運單: 成功 / 託運單號碼: '+ json_obj.tracking_number;
														}else{
															text = '傳送託運單: 失敗';
															if(json_obj.message.length!=0){
																text += (' / 訊息: ' + json_obj.message );
															}
														}
														var $mes = $('#message #text');
								                        $mes.val('').html(text);
								                        $('#message')
								                            .dialog()
								                            .dialog('option', 'title', '訊息')
								                            .dialog('option', 'width', 'auto')
								                            .dialog('option', 'minHeight', 'auto')
								                            .dialog("open");
								                    }
												});													
											}
										}, {
											text : "取消",
											click : function() {
												$("#dialog-egs-form").trigger("reset");
												$(this).dialog("close");
											}
										} ],
								close : function() {
									$("#dialog-egs-form").trigger("reset");
								}
							});
							$( "#dialog-egs" ).dialog( "option", "height", 530 ).dialog( 'open' );			
						}
		            }
		        }
		    ]
		});
		};

	//驗證日期格式
	function dateValidationCheck(str) {
		  var re = new RegExp("^([0-9]{4})[.-]{1}([0-9]{1,2})[.-]{1}([0-9]{1,2})$");
		  var strDataValue;
		  var infoValidation = true;
		  if ((strDataValue = re.exec(str)) != null) {
		    var i;
		    i = parseFloat(strDataValue[1]);
		    if (i <= 0 || i > 9999) { /*年*/
		      infoValidation = false;
		    }
		    i = parseFloat(strDataValue[2]);
		    if (i <= 0 || i > 12) { /*月*/
		      infoValidation = false;
		    }
		    i = parseFloat(strDataValue[3]);
		    if (i <= 0 || i > 31) { /*日*/
		      infoValidation = false;
		    }
		  } else {
		    infoValidation = false;
		  }
		  return infoValidation;
	}
	
	
	
	function open_report(key,value,key1,value1,modeltype){
		
		var iframUrl="./report.do?"+key+"="+encodeURIComponent(value)+"&"+key1+"="+value1+"&type=ship_report&modeltype="+modeltype;

		console.log(iframUrl);
		$("#dialog_report_iframe").attr("src",iframUrl );
		 $("#dialog_report").dialog({
				draggable : true,
				resizable : false,
				autoOpen : false,
				height : "600",
				width : "900",
				modal : true, 
				closeOnEscape: false,
			    open: function(event, ui) {
			        $(".ui-dialog-titlebar-close", ui.dialog | ui).hide();
			    },
				buttons : [{
						text : "關閉",
						click : function() {
							$("#dialog_report").dialog("close");
						}
						}]
		 })
		 $("#dialog_report").dialog("open"); 	
	}
	
	
	$("#dt_master_ship").on("click", ".btn_ship_report_a4_3", function(e) {
		var row = $(this).closest("tr");
	    var data = $("#dt_master_ship").DataTable().row(row).data();
	    open_report("order_no",data.order_no,"address",data.deliver_to,"a4_3");
	});
	
	$("#dt_master_ship").on("click", ".btn_ship_report_a4_2", function(e) {
		var row = $(this).closest("tr");
	    var data = $("#dt_master_ship").DataTable().row(row).data();
	    open_report("order_no",data.order_no,"address",data.deliver_to,"a4_2");
	});
	
	$("#dt_master_ship").on("click", ".btn_list", function(e) {
		e.preventDefault();

		var row = $(this).closest("tr");
	    var data = $("#dt_master_ship").DataTable().row(row).data();
	    var tblDetail = $("#dialog-sale-detail-table").DataTable({
			dom : "lfr<t>ip",
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
				{"data" : "c_product_id", "defaultContent" : ""},
				{"data" : "product_name", "defaultContent" : ""},
				{"data" : "quantity", "defaultContent" : ""},
				{"data" : "price", "defaultContent" : ""},
				{"data" : "memo", "defaultContent" : ""}
			]})
			
			$("#dialog-sale-detail").dialog({
				title: "資料明細",
				draggable : true,
				resizable : false,
				modal : true,
				autoOpen: true,
					show : {
						effect : "blind",
						duration : 500
					},
				width : 1200,
				close : function() {
					$("#dialog-form-sale-detail").trigger("reset");
				}
			});
			
			$("#dialog-sale-detail")
				.data("sale_id", data.sale_id);
		})
	</script>
	
</body>
</html>
