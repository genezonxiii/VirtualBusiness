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
			<div id="message" align="center">
				<div id="text"></div>
			</div>
		</div>
	</div>
	<!-- 銷貨明細對話窗-->
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
	<script type="text/javascript" src="js/dataTables.buttons.min.js"></script>
	<script type="text/javascript" src="js/buttons.jqueryui.min.js"></script>
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
			console.log($.trim($orderNo).length);
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
			console.log(parameter);
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
			console.log(parameter);
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

		                console.log($checkboxs);

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
									console.log(response);
									var json_obj = $.parseJSON(response);
									console.log(json_obj);
									if (json_obj.response) {
										console.log("response");
										console.log(json_obj.response);
										var sale = json_obj.response.body.saleOrderResponse.saleOrders.saleOrder;
										console.log(sale);
										$.each(sale, function(key, value) {
											var suc = value.result == 1? "/" + "出庫單號：" + value.shipmentId:"/" + "失敗";
											var note = value.result == 1? "": "/" + value.note;
											msg += "訂單編號：" + value.erpOrder + suc + note + "<br/>";
										});
									} else if (json_obj.responseFail) {
										console.log("fail");
										console.log(json_obj.responseFail);
										msg = json_obj.responseFail.remark;
									}										
								}

								var $mes = $('#message #text');
		                        $mes.val('').html(msg);
		                        $('#message')
		                            .dialog()
		                            .dialog('option', 'title', '提示訊息')
		                            .dialog('option', 'width', 'auto')
		                            .dialog('option', 'minHeight', 'auto')
		                            .dialog("open");
		                    }
		                });
		                console.log(noArr);
		            }
		        }, {
		            text: '順豐出庫取消',
		            action: function(e, dt, node, config) {
		                var $table = $('#dt_master_ship');

		                var cells = $dtMaster.cells().nodes();
		                var noArr = '';

		                var $checkboxs = $(cells).find('input[name=checkbox-group-select]:checked');

		                console.log($checkboxs);

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
									console.log(response);
									var json_obj = $.parseJSON(response);
									console.log(json_obj);
									if (json_obj.response) {
										console.log("response");
										console.log(json_obj.response);
										var sale = json_obj.response.body.cancelSaleOrderResponse.saleOrders.saleOrder;
										console.log(sale);
										$.each(sale, function(key, value) {
											var suc = "/" + (value.result == 1? "成功":"失敗");
											var note = "/" + value.note;
											msg += "訂單編號：" + value.erpOrder + suc + note + "<br/>";
										});
									} else if (json_obj.responseFail) {
										console.log("fail");
										console.log(json_obj.responseFail);
										msg = json_obj.responseFail.remark;
									}										
								}

								var $mes = $('#message #text');
		                        $mes.val('').html(msg);
		                        $('#message')
		                            .dialog()
		                            .dialog('option', 'title', '提示訊息')
		                            .dialog('option', 'width', 'auto')
		                            .dialog('option', 'minHeight', 'auto')
		                            .dialog("open");
		                    }
		                });
		                console.log(noArr);
		            }
		        },{
		            text: '順豐出庫明細查詢',
		            action: function(e, dt, node, config) {
		                var $table = $('#dt_master_ship');

		                var cells = $dtMaster.cells().nodes();
		                var noArr = '';

		                var $checkboxs = $(cells).find('input[name=checkbox-group-select]:checked');

		                console.log($checkboxs);

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
									console.log(response);
									var json_obj = $.parseJSON(response);
									console.log(json_obj);
									if (json_obj.response) {
										console.log("response");
										console.log(json_obj.response);
										var sale = json_obj.response.body.saleOrderOutboundDetailResponse.saleOrders.saleOrder;
										console.log(sale);
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
												msg += "訂單編號：" + value.erpOrder + "/" + value.header.shipmentId + "/" + 
												value.header.dataStatus + "<br/>" + tmp_item_list + "<br/>";
											} else {
												msg += "訂單編號：" + value.erpOrder + suc + note + "<br/>";
											}
											
										});
									} else if (json_obj.responseFail) {
										console.log("fail");
										console.log(json_obj.responseFail);
										msg = json_obj.responseFail.remark;
									}										
								}

		                        var $mes = $('#message #text');
		                        $mes.val('').html(msg);
		                        $('#message')
		                            .dialog()
		                            .dialog('option', 'title', '提示訊息')
		                            .dialog('option', 'width', 'auto')
		                            .dialog('option', 'minHeight', 'auto')
		                            .dialog("open");
		                    }
		                });
		                console.log(noArr);
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
