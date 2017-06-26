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
<title>盤點管理</title>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<link rel="Shortcut Icon" type="image/x-icon" href="./images/Rockettheme-Ecommerce-Shop.ico" />
<jsp:include page="template/common_css.jsp" flush="true" />
<link rel="stylesheet" href="css/buttons.dataTables.min.css">
<style>
#msg_div {
	text-align: center;
	color: red;
}

input[type="number"] {
	background: #efefef;
	border: 1px solid #999;
	font-size: 16px;
	padding: 5px;
	font-family: Arial, Helvetica, sans-serif;
}
.red_span {
	color: red;
}
.blue_span{
	color:blue;
}
.dialog_check_box_div{
	overflow:auto;height: 200px; width: 500px;
}


</style>
</head>
<body>
	<input type="hidden" id="glb_menu" value='<%=menu%>' />
	<input type="hidden" id="glb_privilege" value="<%=privilege%>" />

	<div class="page-wrapper">
		<jsp:include page="template/common_headfoot.jsp" flush="true" />

		<div class="content-wrap">
			<h2 class="page-title">盤點管理</h2>

			<div class="panel-content">
				<div class="datalistWrap">
					<div class="input-field-wrap">
						<div class="form-wrap" id="create_master">
							<button class="btn btn-darkblue" >新增盤點</button>
						</div>
					</div>
				</div>
			</div>
			<div class="panel-content">
				<div class="datalistWrap">
					<div class="row search-result-wrap">
						<table id="dt_master" class="result-table"></table>
						<hr class="hr0" style="padding-top: 50px;" id="line">
						<div id="msg_div"></div>

						<table id="detail-table" class="ui-widget ui-widget-content result-table"></table>
					</div>
				</div>
				<input type='hidden' id ='hidStocktake_id' value=''/>
				<input type='hidden' id ='hidStocktakeDetail_id' value=''/>
			</div>



			<!-- 對話窗 -->
			<div id="dialog-data-process" class="dialog" align="center">
				<form name="dialog-data-process" id="dialog-form-data-process">
					<fieldset>
						<table class="form-table" id="dialog-data-process-table">
						</table>
					</fieldset>
				</form>
			</div>

			<!--對話窗樣式-新增盤點 -->
			<div id="dialog-form-inster-master" title="新增盤點" style="display: none;">
				<form name="dialog-form-inster-master-post" id="dialog-form-inster-master-post" style="display: inline">
					<fieldset>
						<table class="form-table">
							<tbody>
								<tr>
									<td>備註：</td>
									<td><input type="text" id="dialog_memo" name="dialog_memo"/></td>
								</tr>
							</tbody>
						</table>
					</fieldset>
				</form>
			</div>
			
			<!--對話窗樣式-新增盤點明細 -->
			<div id="dialog-form-inster-deail" title="新增盤點明細" style="display: none;">
				<form name="dialog-form-inster-deail" id="dialog-form-inster-deail-post" style="display: inline">
					<fieldset>
						<select id="dialog_warehouse"/></select>
						<div id="dialog_check_box_div" class="dialog_check_box_div"></div>
					</fieldset>
				</form>
			</div>
			
			<!--對話窗樣式-盤點 -->
			<div id="dialog-form-update-detail" title="盤點" style="display: none;">
				<form name="dialog-form-update-detail-post" id="dialog-form-update-detail-post" style="display: inline">
					<fieldset>
						<table class="form-table">
							<tbody>
								<tr>
									<td>盤點數量：</td>
									<td><input type="number" id="dialog_stocktake_qty" name="dialog_memo"/></td>
								</tr>
							</tbody>
						</table>
					</fieldset>
				</form>
			</div>

			<div id="message" align="center">
				<div id="text"></div>
			</div>
		</div>
	</div>


	<jsp:include page="template/common_js.jsp" flush="true" />
	<script type="text/javascript" src="js/dataTables.buttons.min.js"></script>
	<script type="text/javascript" src="js/buttons.jqueryui.min.js"></script>
	<script>
		var $dtMaster = null; //主要datatable
	
	</script>
	<script type="text/javascript">
	$(function() {
		
		parameter = {
	            action: "getAllMaster",
	     };
		drawMasterTable(parameter)
		
		
	    $('#create_master').on("click", "button", function(e) {
	        e.preventDefault();
	        
			confirm_dialog = $("#dialog-form-inster-master").dialog({
				draggable : true, resizable : false, autoOpen : false,
				height : "auto", width : "auto", modal : true,
				show : {effect : "blind",duration : 300},
				hide : {effect : "fade",duration : 300},
				open : function(event, ui) {$(this).parent().children().children('.ui-dialog-titlebar-close').hide();},
				buttons : {

					"確認新增" : function() {
						parameter={
							action : "createMaster",
							memo : $("#dialog_memo").val()
						};
						SendAjax(parameter);
						
						parameter = {
					      	action: "getAllMaster",
					    };
						
						drawMasterTable(parameter);
						$(this).dialog("close");
					},
					"取消" : function() {
						$(this).dialog("close");
					}
				}
			});
			confirm_dialog.dialog("open");
	    });
	});
	</script>
	<script type="text/javascript">
	function drawMasterTable(parameter) {
		
		hidDetail();

	    $dtMaster = $("#dt_master").DataTable({
	        dom: "fr<t>ip",
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
	            url: "StockTake.do",
	            dataSrc: "",
	            type: "POST",
	            data: parameter
	        },
	        columns: [{
	            "title": "盤點編號",
	            "data": "seq_no",
	            "defaultContent": ""
	        }, {
	            "title": "盤點產生日期",
	            "data": "create_date",
	            "defaultContent": ""
	        }, {
	            "title": "盤點完成日期",
	            "data": "end_date",
	            "defaultContent": ""
	        }, {
	            "title": "庫存鎖定狀態",
	            "data": "stocktake_flag",
	            "defaultContent": ""
	        }, {
	            "title": "備註",
	            "data": "memo",
	            "defaultContent": ""
	        }, {
	            "title": "功能",
	            "data": null,
	            "defaultContent": ""
	        }],
	        columnDefs: [{
	            targets: 3,
	            searchable: false,
	            orderable: false,
	            render: function(data, type, row) {

	                var span = document.createElement("SPAN");
	                span.className = 'form-label';

	                if (row.stocktake_flag == false) {
	                    span.textContent = "未鎖定";
	                    span.setAttribute("class", "blue_span");
	                } else if (row.stocktake_flag == true) {
	                    span.textContent = "盤點中";
	                    span.setAttribute("class", "red_span");
	                } else {
	                    span.textContent = "";
	                }

	                var options = $("<div/>").append(span);

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
	                                    "value": row.stocktake_id,
	                                    "name": row.stocktake_id,
	                                    "class": "btn-in-table btn-exec",
	                                    "title": "新增盤點明細"
	                                })

	                                .append($("<i/>", {
	                                    "class": "fa fa-hand-o-up"
	                                }))
	                                .append(
	                                    $("<button/>", {
	                                        "id": row.seq_no,
	                                        "value": row.accept_id,
	                                        "name": row.purchase_id,
	                                        "class": "btn-in-table btn-green btn_detail",
	                                        "title": "清單"
	                                    })
	                                    .append($("<i/>", {
	                                        "class": "fa fa-pencil-square-o"
	                                    }))

	                                    .append(
	                                        $("<button/>", {
	                                            "id": row.seq_no,
	                                            "value": row.stocktake_id,
	                                            "name": row.stocktake_id,
	                                            "class": "btn-in-table btn-alert btn-lock",
	                                            "title": "盤點"
	                                        })
	                                        .append($("<i/>", {
	                                            "class": "fa fa-lock"
	                                        })))
	                                    .append(
	                                        $("<button/>", {
	                                            "id": row.seq_no,
	                                            "value": row.stocktake_id,
	                                            "name": row.stocktake_id,
	                                            "class": "btn-in-table  btn-darkblue btn-unlock",
	                                            "title": "解除盤點"
	                                        })
	                                        .append($("<i/>", {
	                                            "class": "fa fa-unlock"
	                                        })))
         									.append(
	                                        $("<button/>", {
	                                            "id": row.seq_no,
	                                            "value": row.stocktake_id,
	                                            "name": row.stocktake_id,
	                                            "class": "btn-in-table btn-ok",
	                                            "title": "盤點完成"
	                                        })
	                                        .append($("<i/>", {
	                                            "class": "fa  fa-check-circle-o"
	                                        })))

	                                )
	                            )

	                        )
	                    );

	                return options.html();
	            }
	        }]
	    });
	};

	function drawDetailTable(parameter) {
	    var tblDetail = $("#detail-table").DataTable({
	        dom: "fr<t>ip",
	        lengthChange: false,
	        pageLength: 20,
	        scrollY: "290px",
	        width: 'auto',
	        scrollCollapse: true,
	        destroy: true,
	        language: {
	            "url": "js/dataTables_zh-tw.txt"
	        },
	        ajax: {
	            url: "StockTake.do",
	            dataSrc: "",
	            type: "POST",
	            data: parameter
	        },

	        columns: [{
	            "title": "產品編號",
	            "data": "c_product_id",
	            "defaultContent": ""
	        }, {
	            "title": "產品名稱",
	            "data": "product_name",
	            "defaultContent": ""
	        }, {
	            "title": "庫存數量",
	            "data": "quantity",
	            "defaultContent": ""
	        }, {
	            "title": "盤點數量",
	            "data": "stocktake_qty",
	            "defaultContent": ""
	        }, {
	            "title": "儲位編號",
	            "data": "v_location_code",
	            "defaultContent": ""
	        }, {
	            "title": "儲位名稱",
	            "data": "v_location_desc",
	            "defaultContent": ""
	        }, {
	            "title": "倉庫編號",
	            "data": "v_warehouse_code",
	            "defaultContent": ""
	        }, {
	            "title": "倉庫名稱",
	            "data": "v_warehouse_name",
	            "defaultContent": ""
	        }, {
	            "title": "備註",
	            "data": "memo",
	            "defaultContent": ""
	        }, {
	            "title": "功能",
	            "data": null,
	            "defaultContent": ""
	        }],
	        columnDefs: [{
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
	                                    "value": row.seq_no,
	                                    "name": row.seq_no,
	                                    "class": "btn-in-table btn-darkblue btn_update",
	                                    "title": "盤點"
	                                })
	                                .append($("<i/>", {
	                                    "class": "fa fa-pencil"
	                                }))
	                            )
	                        )
	                    );

	                return options.html();
	            }

	        }]
	    })
	}

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


	//initDialog
	function initDetailDialog() {

	    $("#dialog_warehouse").empty();
	    $("#dialog_check_box_div").empty();
	    $("#dialog_stocktake_qty").val("0");

	}

	function showDetail() {

	      $("#detail-table_wrapper").show();
	      $("#detail-table").show();
	      $("#line").show();
	}
	function hidDetail() {

	      $("#detail-table_wrapper").hide();
	      $("#detail-table").hide();
	      $("#line").hide();
	}
	

	
	//共用的ajax 需處理同步的不使用此function
	function SendAjax(parameter) {
	    console.log("SendAjax:" + parameter);
	    $.ajax({
	        url: "StockTake.do",
	        type: "POST",
	        cache: false,
	        delay: 1500,
	        data: parameter,
	        success: function(data) {
	            var $mes = $('#message #text');
	            if (data == 'success') {
	                $mes.val('').html('執行成功');
	                $('#message')
	                    .dialog()
	                    .dialog('option', 'title', '提示訊息')
	                    .dialog('option', 'width', 'auto')
	                    .dialog('option', 'minHeight', 'auto')
	                    .dialog("open");
	            } else {
	                $mes.val('').html('執行失敗');
	                $('#message')
	                    .dialog()
	                    .dialog('option', 'title', '提示訊息')
	                    .dialog('option', 'width', 'auto')
	                    .dialog('option', 'minHeight', 'auto')
	                    .dialog("open");
	            }



	        },
	        error: function(XMLHttpRequest, textStatus, errorThrown) {
	            var $mes = $('#message #text');
	            $mes.val('').html('執行失敗');
	            $('#message')
	                .dialog()
	                .dialog('option', 'title', '提示訊息')
	                .dialog('option', 'width', 'auto')
	                .dialog('option', 'minHeight', 'auto')
	                .dialog("open");
	        }
	    })
	}
	
	
	
	
	
	//盤點完成
	$("#dt_master").on("click", ".btn-ok", function(e) {
	    e.preventDefault();
	    hidDetail();

	    var row = $(this).closest("tr");
	    var data = $("#dt_master").DataTable().row(row).data();
	    console.log("stocktake_id:" + data.stocktake_id);

	    var parameter = {
	        "action": "updateEndDate",
	        "stocktake_id": data.stocktake_id
	    };

	    $.ajax({
            url: "StockTake.do",
            type: "POST",
            cache: false,
            delay: 1500,
            data: parameter,
            async: false,
            success: function(data) {
                var $mes = $('#message #text');
                if (data == 'success') {
                    $mes.val('').html('執行成功');
                    $('#message')
                        .dialog()
                        .dialog('option', 'title', '提示訊息')
                        .dialog('option', 'width', 'auto')
                        .dialog('option', 'minHeight', 'auto')
                        .dialog("open");

                } else {
                    $mes.val('').html('執行失敗');
                    $('#message')
                        .dialog()
                        .dialog('option', 'title', '提示訊息')
                        .dialog('option', 'width', 'auto')
                        .dialog('option', 'minHeight', 'auto')
                        .dialog("open");
                }
            },
            error: function(XMLHttpRequest, textStatus, errorThrown) {
                var $mes = $('#message #text');
                $mes.val('').html('執行失敗');
                $('#message')
                    .dialog()
                    .dialog('option', 'title', '提示訊息')
                    .dialog('option', 'width', 'auto')
                    .dialog('option', 'minHeight', 'auto')
                    .dialog("open");
            }
        })
	    
	    parameter = {
	        action: "getAllMaster",
	    };
	    drawMasterTable(parameter)
	});

	
	//主表功能事件 明細
	$("#dt_master").on("click", ".btn_detail", function(e) {
	    e.preventDefault();
	    showDetail();
	    lookdown();

	    var row = $(this).closest("tr");
	    var data = $("#dt_master").DataTable().row(row).data();
	    console.log("stocktake_id:" + data.stocktake_id);


	    $("#hidStocktake_id").val(data.stocktake_id);

	    var parameter = {
	        "action": "getStockDetailVOListByStockTake_id",
	        "stocktake_id": data.stocktake_id
	    };

	    drawDetailTable(parameter)
	});

	//master 功能 盤點
	$("#dt_master").on("click", ".btn-lock", function(e) {

	    //先清空
	    initDetailDialog();
	    hidDetail();
	    var row = $(this).closest("tr");
	    var data = $("#dt_master").DataTable().row(row).data();

	    console.log("stocktake_id" + data.stocktake_id);

	    $("#hidStocktake_id").val(data.stocktake_id);

	    parameter = {
	        action: "lockStocktakeByStocktake_id",
	        stocktake_id: $("#hidStocktake_id").val()
	    };

	    SendAjax(parameter);

	    parameter = {
	        action: "getAllMaster",
	    };
	    drawMasterTable(parameter)
	});

	//master 功能 解鎖
	$("#dt_master").on("click", ".btn-unlock", function(e) {

	    //先清空
	    initDetailDialog();
	    hidDetail();
	    var row = $(this).closest("tr");
	    var data = $("#dt_master").DataTable().row(row).data();

	    console.log("stocktake_id" + data.stocktake_id);

	    $("#hidStocktake_id").val(data.stocktake_id);

	    parameter = {
	        action: "unLockStocktakeByStocktake_id",
	        stocktake_id: $("#hidStocktake_id").val()
	    };

	    SendAjax(parameter);

	    parameter = {
	        action: "getAllMaster",
	    };
	    drawMasterTable(parameter)
	});
	
	//master 功能 新增明細功能
	$("#dt_master").on("click", ".btn-exec", function(e) {

	    //先清空
	    initDetailDialog();
	    hidDetail();
	    var row = $(this).closest("tr");
	    var data = $("#dt_master").DataTable().row(row).data();

	    console.log("stocktake_id" + data.stocktake_id);

	    $("#hidStocktake_id").val(data.stocktake_id);

	    $.ajax({
	        url: "StockTake.do",
	        type: "POST",
	        cache: false,
	        delay: 1500,
	        data: {
	            action: "getAllWarehouse",
	        },
	        success: function(data) {

	            var json_obj = $.parseJSON(data);


	            console.log("json_obj.warehouseVOList:" + json_obj);
                $("#dialog_warehouse").append("<option value=''>請選擇倉庫</option>");

	            $.map(json_obj, function(item) {
	                if (item.warehouse_code != '' && ('undefined' != typeof(item.warehouse_code))) {
	                    $("#dialog_warehouse").append("<option value='" + item.warehouse_code + "'>" + item.warehouse_code + '-' + item.warehouse_name + "</option>");
	                }

	            });
	        }
	    });




	    //Dialog相關設定(新增明細)
		$("#dialog-form-inster-deail").dialog({
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
	        buttons: {
	            "確認新增": function() {

	                var idArr = '';
	                var checkedLength = 0;
	                $('input[name=checkbox-group-select]:checked').each(function() {
	                    checkedLength = checkedLength + 1;
	                    idArr += this.id + '~';
	                });


	                if (checkedLength == 0) {
	                    alert('請至少選擇一筆資料');
	                    return false;
	                }

	                idArr = idArr.slice(0, -1);
	   

	                parameter = {
	                    action: "InsterDetail",
	                    location_ids: idArr,
	                    stocktake_id: $("#hidStocktake_id").val()
	                };
	                SendAjax(parameter);
	                $(this).dialog("close");
	            },
	            "取消": function() {
	                $(this).dialog("close");
	            }
	        }
	    });

	    $("#dialog-form-inster-deail").dialog("open");

	});

	//dialog事件 :新增明細 選擇倉庫後觸發
	$("#dialog_warehouse").change(function() {
	    console.log("dialog_warehouse_change");

	    //驗證
	    var warehouseCode = $("#dialog_warehouse").val();
	    if (('' == warehouseCode) || ('undefined' == typeof(warehouseCode))) {
	        console.log("dialog_warehouse_code_return");
	        return;
	    }
	    console.log("warehouse_code:" + warehouseCode);

	    $.ajax({
	        url: "StockTake.do",
	        type: "POST",
	        cache: false,
	        delay: 1500,
	        data: {
	            action: "getLocationDataByWarehouseCode",
	            warehouse_code: warehouseCode,
	            asdasd: "adsad"
	        },
	        success: function(data) {

	            $("#dialog_check_box_div").empty()

	            var json_obj = $.parseJSON(data);

	            $.map(json_obj, function(item) {

	                var input = document.createElement("INPUT");
	                input.type = 'checkbox';
	                input.name = 'checkbox-group-select';
	                input.id = item.location_id;

	                var span = document.createElement("SPAN");
	                span.className = 'form-label';

	                var label = document.createElement("LABEL");
	                label.htmlFor = item.location_id;
	                label.name = 'checkbox-group-select';
	                label.style.marginLeft = '5%';
	                
	                label.innerHTML = item.location_code + ' - ' + item.location_desc;

	                label.appendChild(span);

	                var options = $("<div/>").append(input, label);

	                $("#dialog_check_box_div").append(options.html());

	            });

	        },
	        error: function(XMLHttpRequest, textStatus, errorThrown) {
	            alert(textStatus);
	        }
	    });
	});



	//明細盤點事件 
	$("#detail-table").on("click", ".btn_update", function(e) {
	    e.preventDefault();

	

	    var row = $(this).closest("tr");
	    var data = $("#detail-table").DataTable().row(row).data();
	    
	    //驗證是否鎖定前端先驗    待修改的瞬間後端在驗證


	    $("#hidStocktakeDetail_id").val(data.stocktakeDetail_id);
	    console.log("hidStocktakeDetail_id_value: " + $("#hidStocktakeDetail_id").val());
	    //修改Dialog相關設定
	    update_dialog = $("#dialog-form-update-detail").dialog({
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
	                parameter = {
	                    action: "inventory",
	                    stocktake_id: $("#hidStocktake_id").val(),
	                    stocktakeDetail_id: $("#hidStocktakeDetail_id").val(),
	                    stocktake_qty: $("#dialog_stocktake_qty").val()

	                };

	                $.ajax({
	                    url: "StockTake.do",
	                    type: "POST",
	                    cache: false,
	                    delay: 1500,
	                    data: parameter,
	                    async: false,
	                    success: function(data) {
	                        var $mes = $('#message #text');
	                        if (data == 'success') {
	                            $mes.val('').html('執行成功');
	                            $('#message')
	                                .dialog()
	                                .dialog('option', 'title', '提示訊息')
	                                .dialog('option', 'width', 'auto')
	                                .dialog('option', 'minHeight', 'auto')
	                                .dialog("open");

	                        } else {
	                            $mes.val('').html('執行失敗:'+data);
	                            $('#message')
	                                .dialog()
	                                .dialog('option', 'title', '提示訊息')
	                                .dialog('option', 'width', 'auto')
	                                .dialog('option', 'minHeight', 'auto')
	                                .dialog("open");
	                        }
	                    },
	                    error: function(XMLHttpRequest, textStatus, errorThrown) {
	                        var $mes = $('#message #text');
	                        $mes.val('').html('執行失敗');
	                        $('#message')
	                            .dialog()
	                            .dialog('option', 'title', '提示訊息')
	                            .dialog('option', 'width', 'auto')
	                            .dialog('option', 'minHeight', 'auto')
	                            .dialog("open");
	                    }
	                })

	                $("#dialog-form-update-detail").trigger("reset");
	                update_dialog.dialog("close");
	                var parameter = {
	                    "action": "getStockDetailVOListByStockTake_id",
	                    "stocktake_id": data.stocktake_id
	                };
	                drawDetailTable(parameter)
	            }


	        }, {
	            text: "取消",
	            click: function() {
	                $("#update-dialog-form-post").trigger("reset");
	                update_dialog.dialog("close");
	            }
	        }],
	        close: function() {
	            $("#update-dialog-form-post").trigger("reset");
	        }
	    });
	    update_dialog.dialog("open");
	});
	
	
	</script>

</body>


</html>




