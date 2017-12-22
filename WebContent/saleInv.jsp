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
<title>電子發票管理</title>
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
			<h2 class="page-title">電子發票管理</h2>

			<div class="panel-content">
				<div class="datalistWrap">
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
						</div>
					</div>
				</div>
			</div>
			<div class="panel-content">
				<div class="datalistWrap">
					<div class="row search-result-wrap">
						<table id="dt_master" class="result-table"></table>
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
	
	<!-- 報表 對話窗 -->
	<div id="dialog_report" class="dialog" align="center">
		<iframe src="" frameborder="0" id="dialog_report_iframe" width="850" height="450"></iframe>
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
		$('#searh-trans-list-date').click(function(e) {
			e.preventDefault();
			continue_search_type = $('#searh-trans-list-date');
			var $startDate = $('#trans_list_start_date').val();
			var $endDate = $('#trans_list_end_date').val();
			var errorMes = '';
			if($endDate != null && $endDate.length != 0 && $startDate != null && $startDate.length != 0){
				if($startDate.replace(/-/g,"") > $endDate.replace(/-/g,"")){
					errorMes += "訖日不可小於起日!<br>";
				}
			}
			if($startDate == null || $startDate.length == 0){
				errorMes += "請選擇起日!<br>";
			}else if(!dateValidationCheck($startDate)){
				errorMes += "起日格式不符!<br>";
			}
			if($endDate == null || $endDate.length == 0){
				errorMes += "請選擇訖日!<br>";
			}else if(!dateValidationCheck($endDate)){
				errorMes += "訖日格式不符!<br>";
			}

			if(errorMes.length > 0){
				dialogMsg("警告",errorMes);
				return false;
			}
			var parameter = {
				action : "getSaleByTransDate",
				trans_date_start : $("#trans_list_start_date").val(),
				trans_date_end : $("#trans_list_end_date").val()
			};
			search_parameter = parameter;
			console.log(parameter);
			drawMasterTable(parameter);
		});
		
		$('#search-upload-date').click(function(e) {
			e.preventDefault();
			
			var $startDate = $('#upload_start_date').val();
			var $endDate = $('#upload_end_date').val();
			var errorMes = '';
			if($endDate != null && $endDate.length != 0 && $startDate != null && $startDate.length != 0){
				if($startDate.replace(/-/g,"") > $endDate.replace(/-/g,"")){
					errorMes += "訖日不可小於起日!<br>";
				}
			}
			if($startDate == null || $startDate.length == 0){
				errorMes += "請選擇起日!<br>";
			}else if(!dateValidationCheck($startDate)){
				errorMes += "起日格式不符!<br>";
			}
			if($endDate == null || $endDate.length == 0){
				errorMes += "請選擇訖日!<br>";
			}else if(!dateValidationCheck($endDate)){
				errorMes += "訖日格式不符!<br>";
			}

			if(errorMes.length > 0){
				dialogMsg("警告",errorMes);
				return false;
			}
			var parameter = {
				action : "getSaleByUploadDate",
				upload_date_start : $("#upload_start_date").val(),
				upload_date_end : $("#upload_end_date").val()
			};
			search_parameter = parameter;
			console.log(parameter);
			drawMasterTable(parameter);
		});
	});
	</script>
	<script type="text/javascript">
	function drawMasterTable(parameter) {

		$dtMaster = $("#dt_master").DataTable({
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
		        url: "sale.do",
		        dataSrc: "",
		        type: "POST",
		        data: parameter
		    },
		    columns: [{
		        "title": "批次請求",
		        "data": null,
		        "defaultContent": ""
		    },{
		        "title": "訂單編號",
		        "data": "order_no",
		        "defaultContent": ""
		    }, {
		        "title": "轉單日期",
		        "data": "trans_list_date",
		        "defaultContent": ""
		    }, {
		        "title": "上傳日期",
		        "data": "upload_date",
		        "defaultContent": ""
		    }, {
		        "title": "訂單總額",
		        "data": "total_amt",
		        "defaultContent": ""
		    }, {
		        "title": "發票號碼",
		        "data": "invoice",
		        "defaultContent": ""
		    }, {
		        "title": "發票日期",
		        "data": "invoice_date",
		        "defaultContent": ""
		    }, {
		        "title": "銷售平台",
		        "data": "order_source",
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
		        	
		            var order_no = row.order_no;
		            var input = document.createElement("INPUT");
		            input.type = 'checkbox';
		            input.name = 'checkbox-group-select';
		            input.id = order_no;

		            var span = document.createElement("SPAN");
		            span.className = 'form-label';

		            var label = document.createElement("LABEL");
		            label.htmlFor = order_no;
		            label.name = 'checkbox-group-select';
		            label.style.marginLeft = '48%';
		            label.appendChild(span);

		            var options = $("<div/>").append(input, label);

		            return options.html();
		        }
		    },{
		        //功能
		        targets: -1,
		        searchable: false,
		        orderable: false,
		        visible: false,
		        render: function(data, type, row) {
		            return null;
		        }
		    }],
		    buttons: [{
	            text: '全選',
	            action: function(e, dt, node, config) {

	                selectCount++;
	                var $table = $('#dt_master');
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
	            },
	        }, {
	            text: '開立發票',
	            action: function(e, dt, node, config) {
	                var $table = $('#dt_master');

	                var cells = $dtMaster.cells().nodes();
	                var saleMap = new Map();
	                var ids = '';
	                var row;
	                var data;
	                var message = '';

	                var $checkboxs = $(cells).find('input[name=checkbox-group-select]:checked');


	                if ($checkboxs.length == 0) {
						dialogMsg('提示','請選擇一筆資料');
	                    return false;
	                }

	                $checkboxs.each(function(index) {
	                    ids += "'" + this.id + "',";
	                    row = $(this).closest("tr");
	                    data = $table.DataTable().row(row).data();
	                });

	                    ids = ids.slice(0, -1);
	                    var Today = new Date();
	                    $("#invoice_num_date").val(formatDate())
	                    console.log(ids);

	                    $.ui.dialog.prototype._focusTabbable = function(){};
	                    var dialog_invoice = $("#dialog-invoice").dialog({
	                        draggable: true,
	                        resizable: false,
	                        autoOpen: false,
	                        height: "auto",
	                        width: "auto",
	                        modal: true,
	                        open: function(event, ui) {
	                            $(this).parent().children().children('.ui-dialog-titlebar-close').hide();
	                        },
	                        buttons: [{
	                            id: "dialog_invoice_enter",
	                            text: "確定",
	                            click: function() {

	                                $.ajax({
	                                    url: 'sale.do',
	                                    type: 'post',
	                                    beforeSend: function() {
	                                        $(':hover').css('cursor', 'progress');
	                                    },
	                                    complete: function() {
	                                        $(':hover').css('cursor', 'default');
	                                    },
	                                    data: {
	                                        action: 'invoice_new',
	                                        ids: ids,
	                                        invoice_date: $("#invoice_num_date").val()
	                                    },
	                                    success: function(response) {
	                                        var $mes = $('#message #text');
	                                        var text = '成功發送<br><br>執行結果為: <br>' + response;

	                                        $mes.val('').html(text);

	                                        $('#message')
	                                            .dialog()
	                                            .dialog('option', 'title', '提示訊息')
	                                            .dialog('option', 'width', 'auto')
	                                            .dialog('option', 'minHeight', 'auto')
	                                            .dialog("open");

	                                        if(window.search_parameter){
	                                        	drawMasterTable(search_parameter);
	                                        }

	                                    }
	                                });

	                                $("#dialog-invoice").trigger("reset");
	                                dialog_invoice.dialog("close");
	                            }
	                        }, {
	                            text: "取消",
	                            click: function() {
	                                $("#dialog-invoice").trigger("reset");
	                                dialog_invoice.dialog("close");
	                            }
	                        }],
	                        close: function() {
	                            $("#dialog-invoice").trigger("reset");
	                        }
	                    });
	                    dialog_invoice.dialog("open");

	            },
	        }, {
	            text: '作廢發票',
	            action: function(e, dt, node, config) {
	                var $table = $('#dt_master');

	                var cells = $dtMaster.cells().nodes();
	                var saleMap = new Map();
	                var ids = '';
	                var row;
	                var data;
	                var message = '';

	                var $checkboxs = $(cells).find('input[name=checkbox-group-select]:checked');


	                if ($checkboxs.length == 0) {
						dialogMsg('提示','請選擇一筆資料');
	                    return false;
	                }

	                $checkboxs.each(function(index) {
	                    ids += "'" + this.id + "',";
	                    row = $(this).closest("tr");
	                    data = $table.DataTable().row(row).data();
	                    saleMap.set(data.order_no, (index + 1));
	                });

	                if (saleMap.size > 1) {
	                    message = message.concat('以下為您所勾選的訂單號↓<br><br>');
	                    var table = document.createElement('table');
	                    saleMap.forEach(function(value, key, fullArray) {
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
	                } else {
	                    ids = ids.slice(0, -1);
	                    console.log(ids);

	                 
	                    var dialog_invoice_cancel = $("#dialog-invoice-cancel").dialog({
	                        draggable: true,
	                        resizable: false,
	                        autoOpen: false,
	                        height: "auto",
	                        width: "auto",
	                        modal: true,
	                        open: function(event, ui) {
	                            $(this).parent().children().children('.ui-dialog-titlebar-close').hide();
	                            $("#invoice_cancel_reason").val("");
	                        },
	                        buttons: [{
	                            id: "dialog_invoice_cancel_enter",
	                            text: "確定",
	                            click: function() {
	                            	
	                            	if($("#invoice_cancel_reason").val()==""){
	                            		$("#invoice_cancel_reason").val("無");
	                            	}

	                                $.ajax({
	                                    url: 'sale.do',
	                                    type: 'post',
	                                    beforeSend: function() {
	                                        $(':hover').css('cursor', 'progress');
	                                    },
	                                    complete: function() {
	                                        $(':hover').css('cursor', 'default');
	                                    },
	                                    data: {
	                                        action: 'invoice_cancel_new',
	                                        ids: ids,
	                                        reason: $("#invoice_cancel_reason").val()
	                                    },
	                                    success: function(response) {
	                                        var $mes = $('#message #text');
	                                        var text = '成功發送<br><br>執行結果為: ' + response;

	                                        $mes.val('').html(text);

	                                        $('#message')
	                                            .dialog()
	                                            .dialog('option', 'title', '提示訊息')
	                                            .dialog('option', 'width', 'auto')
	                                            .dialog('option', 'minHeight', 'auto')
	                                            .dialog("open");

	                                        if(window.search_parameter){
	                                        	drawMasterTable(search_parameter);
	                                        }
	                                    }
	                                });

	                                $("#dialog-invoice-cancel").trigger("reset");
	                                dialog_invoice_cancel.dialog("close");
	                            }


	                        }, {
	                            text: "取消",
	                            click: function() {
	                                $("#dialog-invoice-cancel").trigger("reset");
	                                dialog_invoice_cancel.dialog("close");
	                            }
	                        }],
	                        close: function() {
	                            $("#dialog-invoice-cancel").trigger("reset");
	                        }
	                    });
	                    dialog_invoice_cancel.dialog("open");
	                }
	            }
	        }, {
	            text: '列印發票',
	            action: function(e, dt, node, config) {
	                var $table = $('#dt_master');

	                var cells = $dtMaster.cells().nodes();
	                var saleMap = new Map();
	                var ids = '';
	                var row;
	                var data;
	                var message = '';

	                var $checkboxs = $(cells).find('input[name=checkbox-group-select]:checked');


	                if ($checkboxs.length == 0) {
						dialogMsg('提示','請選擇一筆資料');
	                    return false;
	                }

	                $checkboxs.each(function(index) {
	                    ids += "'" + this.id + "',";
	                    row = $(this).closest("tr");
	                    data = $table.DataTable().row(row).data();
	                    saleMap.set(data.order_no, (index + 1));
	                });

	                //修改Dialog相關設定
	                ids = ids.slice(0, -1);

	                $.ajax({
	                    url: 'sale.do',
	                    type: 'post',
	                    beforeSend: function() {
	                        $(':hover').css('cursor', 'progress');
	                    },
	                    complete: function() {
	                        $(':hover').css('cursor', 'default');
	                    },
	                    data: {
	                        action: 'invoice_print_new',
	                        ids: ids,
	                    },
	                    success: function(response) {
	                        var json_obj = $.parseJSON(response);

	                        if (json_obj.error.length > 0) {
								dialogMsg('提示',json_obj.error);
	                            return;
	                        }
	                        if (json_obj.isSuccess == false) {
	                            return;
	                        }
	                        console.log('json_obj: ' + json_obj);

	                        console.log(json_obj);
	                        $.each(json_obj.list, function(i, item) {
	                        	console.log(item);
	                            $.ajax({
	                                url: "http://127.0.0.1:55180/receiver.php",
	                                data: item,
	                                type: 'POST',
	                                crossDomain: true,
	                                success: function(a, b, c) {
	                                    console.log("Success:");
	                                    console.log(a);
	                                    console.log(b);
	                                    console.log(c);
	                                },
	                                error: function(xhr, ajaxOptions, thrownError) {
	                                    console.log("Error:");
	                                    console.log(xhr.status);
	                                    console.log(thrownError);
	                                }
	                            });
	                        });
	                    }
	                });
	            }
	        }]
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
	
</body>
</html>
