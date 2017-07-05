<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ page import="tw.com.aber.salereturn.controller.*"%>
<%@ page import="java.util.*"%>
<%@ page import="java.sql.DriverManager"%>
<%@ page import="java.sql.Connection"%>
<%@ page import="java.sql.Statement"%>
<%@ page import="java.sql.ResultSet"%>
<jsp:directive.page import="java.sql.SQLException" />
<!DOCTYPE html>
<html>
	<head>
		<title>應收帳款</title>
		<meta charset="utf-8">
		<link rel="Shortcut Icon" type="image/x-icon" href="./images/Rockettheme-Ecommerce-Shop.ico" />
		<link href="<c:url value="css/jquery.dataTables.min.css" />" rel="stylesheet">
		<link href="<c:url value="css/1.11.4/jquery-ui.css" />" rel="stylesheet">
		<link href="<c:url value="css/dataTables.jqueryui.min.css" />" rel="stylesheet">
		<link href="<c:url value="css/buttons.jqueryui.min.css" />" rel="stylesheet">
		<link rel="stylesheet" href="css/buttons.dataTables.min.css">
		<link rel="stylesheet" href="css/styles.css" />
		
		<style type="text/css">
			.dataTables_wrapper .dt-buttons {
				float:right;margin-bottom:10px;
			}
			
			input[type="text"]:disabled, input[type="text"].readonly {
			    border: 0;
			    width:450px;
			}
		</style>
	</head>
	<body>
		<jsp:include page="template.jsp" flush="true"/>
		<div class="content-wrap" >
		<div class='bdyplane' style="opacity:0">
		<script type="text/javascript" src="js/jquery-1.10.2.js"></script>
		<script type="text/javascript" src="js/jquery.dataTables.min.js"></script>
		<script type="text/javascript" src="js/jquery-ui.min.js"></script>
		<script type="text/javascript" src="js/jquery-migrate-1.4.1.min.js"></script>
		<script type="text/javascript" src="js/jquery.validate.min.js"></script>
		<script type="text/javascript" src="js/additional-methods.min.js"></script>
		<script type="text/javascript" src="js/messages_zh_TW.min.js"></script>
		<!-- data table buttons -->
		<script type="text/javascript" src="js/dataTables.buttons.min.js"></script>
		<script type="text/javascript" src="js/buttons.jqueryui.min.js"></script>
	
		<script>
		    function draw_accreceive(parameter) {
		        var masterDT = $("#account_amount_date_table").DataTable({
		            dom: "Blfr<t>ip",
		            scrollY: "290px",
		            width: 'auto',
		            lengthChange: false,
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
		                url: "accreceive.do",
		                dataSrc: "",
		                type: "POST",
		                data: parameter
		            },
		            columns: [{
		                "title": "批次請求",
		                "data": null,
		                "defaultContent": ""
		            }, {
		                "title": "訂單號",
		                "data": "order_no",
		                "defaultContent": ""
		            }, {
		                "title": "應收帳款金額",
		                "data": "amount",
		                "defaultContent": ""
		            }, {
		                "title": "實收帳款金額",
		                "data": "receive_amount",
		                "defaultContent": ""
		            }, {
		                "title": "應收帳款產生日期",
		                "data": "amount_date",
		                "defaultContent": ""
		            }, {
		                "title": "實收帳款產生日期",
		                "data": "receive_date",
		                "defaultContent": ""
		            }, {
		                "title": "交易平台",
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
		                    input.id = 'my-' + order_no;
		                    input.className = 'checkbox_receive';
		                    input.value = order_no
		
		                    var span = document.createElement("SPAN");
		                    span.className = 'form-label';
		
		                    var label = document.createElement("LABEL");
		                    label.htmlFor = 'my-' + order_no;
		                    label.name = 'checkbox-group-select';
		                    label.style.marginLeft = '40%';
		                    label.appendChild(span);
		
		                    var options = $("<div/>").append(input, label);
		
		                    return options.html();
		                }
		            }, {
		                "className": "dt-center",
		                "targets": "_all"
		            }, {
		                //功能
		                targets: -1,
		                searchable: false,
		                orderable: false,
		                render: function(data, type, row) {
		
		                    var options = $("<div/>").append($("<div/>", {
		                        "class": "table-row-func btn-in-table btn-gray"
		                    }).append($("<i/>", {
		                        "class": "fa fa-ellipsis-h"
		                    })).append($("<div/>", {
		                        "class": "table-function-list"
		                    }).append($("<button/>", {
		                        "class": "btn-in-table btn-green btn_list",
		                        "value": row.order_no,
		                        "title": "清單"
		                    }).append($("<i/>", {
		                        "class": "fa fa-pencil-square-o"
		                    }))).append($("<button/>", {
		                        "class": "btn-in-table btn-alert btn_delete",
		                        "title": "刪除",
		                    }).append($("<i/>", {
		                        "class": "fa fa-trash"
		                    })))));
		
		                    return options.html();
		                }
		            }],
		            buttons: [{
		                text: '批次收帳',
		                className: 'btn_receive',
		                action: function(e) {
		                    e.preventDefault();
		                    var count = 0;
		                    var message = "";
		                    $(".checkbox_receive").each(function() {
		                        if ($(this).prop("checked")) {
		                            count += 1;
		                        }
		                    });
		
		                    if (count == 0) {
		                        massageDialog('至少一筆', '警告');
		                        return false;
		                    }
		
		                    message = "確認要收帳嗎? 總共" + count + "筆";
		                    $("#dialog-confirm p").text(message);
		                    confirm_dialog.dialog("open");
		                }
		            }]
		        });
		    }
		
		
		    function draw_receive(parameter) {
		        var masterDT = $("#account_receive_date_table").DataTable({
		            dom: "Blfr<t>ip",
		            scrollY: "290px",
		            width: 'auto',
		            lengthChange: false,
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
		                url: "accreceive.do",
		                dataSrc: "",
		                type: "POST",
		                data: parameter
		            },
		            columns: [{
		                "title": "批次請求",
		                "data": null,
		                "defaultContent": ""
		            }, {
		                "title": "訂單號",
		                "data": "order_no",
		                "defaultContent": ""
		            }, {
		                "title": "應收帳款金額",
		                "data": "amount",
		                "defaultContent": ""
		            }, {
		                "title": "實收帳款金額",
		                "data": "receive_amount",
		                "defaultContent": ""
		            }, {
		                "title": "應收帳款產生日期",
		                "data": "amount_date",
		                "defaultContent": ""
		            }, {
		                "title": "實收帳款產生日期",
		                "data": "receive_date",
		                "defaultContent": ""
		            }, {
		                "title": "交易平台",
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
		                    input.id = 'my-' + order_no;
		                    input.className = 'checkbox_receive_cancel';
		                    input.value = order_no
		
		                    var span = document.createElement("SPAN");
		                    span.className = 'form-label';
		
		                    var label = document.createElement("LABEL");
		                    label.htmlFor = 'my-' + order_no;
		                    label.name = 'checkbox-group-select';
		                    label.style.marginLeft = '40%';
		                    label.appendChild(span);
		
		                    var options = $("<div/>").append(input, label);
		
		                    return options.html();
		                }
		            }, {
		                "className": "dt-center",
		                "targets": "_all"
		            }, {
		                //功能
		                targets: -1,
		                searchable: false,
		                orderable: false,
		                render: function(data, type, row) {
		
		                    var options = $("<div/>").append($("<div/>", {
		                        "class": "table-row-func btn-in-table btn-gray"
		                    }).append($("<i/>", {
		                        "class": "fa fa-ellipsis-h"
		                    })).append($("<div/>", {
		                        "class": "table-function-list"
		                    }).append($("<button/>", {
		                        "class": "btn-in-table btn-green btn_list",
		                        "value": row.order_no,
		                        "title": "清單"
		                    }).append($("<i/>", {
		                        "class": "fa fa-pencil-square-o"
		                    }))).append($("<button/>", {
		                        "class": "btn-in-table btn-alert btn_delete",
		                        "title": "刪除",
		                    }).append($("<i/>", {
		                        "class": "fa fa-trash"
		                    })))));
		
		                    return options.html();
		                }
		            }],
		            buttons: [{
		                text: '批次取消收帳',
		                className: 'btn_receive_cancel',
		                action: function(e) {
		                    e.preventDefault();
		                    var count = 0;
		                    var message = "";
		                    $(".checkbox_receive_cancel").each(function() {
		                        if ($(this).prop("checked")) {
		                            count += 1;
		                        }
		                    });
		
		                    if (count == 0) {
		                        massageDialog('至少一筆', '警告');
		                        return false;
		                    }
		
		                    message = "確認要取消收帳嗎? 總共" + count + "筆";
		                    $("#dialog-cancel-confirm p").text(message);
		                    confirm_cancel_dialog.dialog("open");
		                }
		            }]
		        });
		    }
		
		
		    function draw_detail(parameter, type) {
		        console.log("draw_detail=> parameter:");
		        console.log(parameter);
		        console.log("draw_detail=> type:" + type);
		
		        var typeName = '';
		        var typeData = '';
		        var detailTableID = ''
		        if (type == '1') {
		            typeName = '應收帳款金額';
		            typeData = 'amount';
		            detailTableID = '#noreceive_detail_table';
		        }
		        if (type == '2') {
		            typeName = '實收帳款金額';
		            typeData = 'receive_amount';
		            detailTableID = '#receive_detail_table';
		        }
		
		        $(detailTableID).DataTable({
		            dom: "lfr<t>ip",
		            scrollY: "290px",
		            width: 'auto',
		            lengthChange: false,
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
		                url: "accreceive.do",
		                dataSrc: "",
		                type: "POST",
		                data: parameter
		            },
		            columns: [{
		                "title": "訂單號",
		                "data": "order_no",
		                "defaultContent": ""
		            }, {
		                "title": "產品名稱",
		                "data": "product_name",
		                "defaultContent": ""
		            }, {
		                "title": typeName,
		                "data": typeData,
		                "defaultContent": ""
		            }, {
		                "title": "交易平台",
		                "data": "order_source",
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
		                "className": "dt-center",
		                "targets": "_all"
		            }, {
		                //功能
		                targets: -1,
		                searchable: false,
		                orderable: false,
		                render: function(data, type, row) {
		
		                    var options = $("<div/>").append($("<div/>", {
		                        "class": "table-row-func btn-in-table btn-gray"
		                    }).append($("<i/>", {
		                        "class": "fa fa-ellipsis-h"
		                    })).append($("<div/>", {
		                        "class": "table-function-list"
		                    }).append($("<button/>", {
		                        "class": "btn-in-table btn-darkblue btn_update",
		                        "title": "修改",
		                    }).append($("<i/>", {
		                        "class": "fa fa-pencil"
		                    })))));
		
		                    return options.html();
		                }
		            }]
		        });
		    }
		</script>
	
		<script>

		
		    function massageDialog(massage, tital) {
		        $('#message #text').val('').html(massage);
		        $('#message')
		            .dialog()
		            .dialog('option', 'title', tital)
		            .dialog('option', 'width', 'auto')
		            .dialog('option', 'minHeight', 'auto')
		            .dialog("open");
		    }
		
		    function initUpdateDialog() {
		        $("#dialog-form-update-noreceive  input[name='dialog_order_no']").val("");
		        $("#dialog-form-update-noreceive  input[name='dialog_product_name']").val("");
		        $("#dialog-form-update-noreceive  input[name='dialog_receive_amount']").val("");
		        $("#dialog-form-update-noreceive  input[name='dialog_amount']").val("");
		        $("#dialog-form-update-noreceive  input[name='dialog_memo']").val("");
		
		        $("#dialog-form-update-receive  input[name='dialog_order_no']").val("");
		        $("#dialog-form-update-receive  input[name='dialog_product_name']").val("");
		        $("#dialog-form-update-receive  input[name='dialog_receive_amount']").val("");
		        $("#dialog-form-update-receive  input[name='dialog_amount']").val("");
		        $("#dialog-form-update-receive  input[name='dialog_memo']").val("");
		    }
		
		
		    //驗證日期格式
		    function dateValidationCheck(str) {
		        var re = new RegExp("^([0-9]{4})[.-]{1}([0-9]{1,2})[.-]{1}([0-9]{2})$");
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
		
		    $(function() {
		
		        $("#noreceive_detail_table").delegate(".btn_update", "click", function() {
		
		            var row = $(this).closest("tr");
		            var data = $("#noreceive_detail_table").DataTable().row(row).data();
		
		            $("#dialog-form-update-noreceive  input[name='dialog_amount']").val(data.amount);
		            $("#dialog-form-update-noreceive  input[name='dialog_amount_date']").val(data.amount_date);
		            $("#dialog-form-update-noreceive  input[name='dialog_memo']").val(data.memo);
		            $("#dialog-form-update-noreceive  input[name='dialog_order_no']").val(data.order_no);
		            $("#dialog-form-update-noreceive  input[name='dialog_product_name']").val(data.product_name);
		
		            $("#hidreceive_id").val(data.receivable_id);
		
		
		            var dialog_form_update = $("#dialog-form-update-noreceive").dialog({
		                draggable: true, //防止拖曳
		                resizable: false, //防止縮放
		                autoOpen: false,
		                height: "auto",
		                width: "700",
		                modal: true,
		                show: {
		                    effect: "blind",
		                    duration: 300
		                },
		                open: function(event, ui) {
	                        $(this).parent().children().children('.ui-dialog-titlebar-close').hide();
	                    },
		                hide: {
		                    effect: "fade",
		                    duration: 300
		                },
		                buttons: [{
		                    id: "update_enter",
		                    text: "修改",
		                    click: function() {
		                        $.ajax({
		                            url: 'accreceive.do',
		                            type: 'post',
		                            data: {
		                                action: 'update_receivable_noreceive',
		                                order_no: $("#dialog-form-update-noreceive  input[name='dialog_order_no']").val(),
		                                amount: $("#dialog-form-update-noreceive  input[name='dialog_amount']").val(),
		                                amount_date: $("#dialog-form-update-noreceive  input[name='dialog_amount_date']").val(),
		                                memo: $("#dialog-form-update-noreceive  input[name='dialog_memo']").val(),
		                                receivable_id: $("#hidreceive_id").val()
		                            },
		                            error: function(xhr) {
		                                alere("error");
		                            },
		                            success: function(response) {
		                                if ("success" == response) {
		
		                                    var parameter = {
		                                        action: "getAccreceiveVOByOrderNO",
		                                        order_no: $("#dialog-form-update-noreceive  input[name='dialog_order_no']").val()
		                                    };
		                                    draw_detail(parameter, '1');
		                                    parameter = {
		                                        action: "searh_amount_date",
		                                        start_date: $("#hidAccreceiveBeginDate").val(),
		                                        end_date: $("#hidAccreceiveEndDate").val()
		                                    }
		                                    draw_accreceive(parameter);
		
		                                    massageDialog('執行成功', '修改');
		                                } else {
		                                    massageDialog('執行失敗', '修改');
		                                }
		
		                            }
		                        });
		
		                        $(this).dialog("close");
		                    }
		                }, {
		                    text: "取消",
		                    click: function() {
		                        dialog_form_update.dialog("close");
		                    }
		                }]
		            });
		
		            dialog_form_update.dialog("open");
		        });
		
		
		
		        $("#receive_detail_table").delegate(".btn_update", "click", function() {
		
		            var row = $(this).closest("tr");
		            var data = $("#receive_detail_table").DataTable().row(row).data();
		
		            $("#dialog-form-update-receive  input[name='dialog_receive_amount']").val(data.receive_amount);
		            $("#dialog-form-update-receive  input[name='dialog_receive_date']").val(data.receive_date);
		            $("#dialog-form-update-receive  input[name='dialog_memo']").val(data.memo);
		            $("#dialog-form-update-receive  input[name='dialog_order_no']").val(data.order_no);
		            $("#dialog-form-update-receive  input[name='dialog_product_name']").val(data.product_name);
		
		            $("#hidreceive_id").val(data.receivable_id);
		
		
		            var dialog_form_update = $("#dialog-form-update-receive").dialog({
		                draggable: true, //防止拖曳
		                resizable: false, //防止縮放
		                autoOpen: false,
		                height: "auto",
		                width: "700",
		                modal: true,
		                show: {
		                    effect: "blind",
		                    duration: 300
		                },
		                open: function(event, ui) {
	                        $(this).parent().children().children('.ui-dialog-titlebar-close').hide();
	                    },
		                hide: {
		                    effect: "fade",
		                    duration: 300
		                },
		                buttons: [{
		                    id: "update_enter",
		                    text: "修改",
		                    click: function() {
		                        $.ajax({
		                            url: 'accreceive.do',
		                            type: 'post',
		                            data: {
		                                action: 'update_receivable_receive',
		                                order_no: $("#dialog-form-update-receive  input[name='dialog_order_no']").val(),
		                                receive_amount: $("#dialog-form-update-receive  input[name='dialog_receive_amount']").val(),
		                                receive_date: $("#dialog-form-update-receive  input[name='dialog_receive_date']").val(),
		                                memo: $("#dialog-form-update-receive  input[name='dialog_memo']").val(),
		                                receivable_id: $("#hidreceive_id").val()
		                            },
		                            error: function(xhr) {
		                                alere("error");
		                            },
		                            success: function(response) {
		                                if ("success" == response) {
		
		                                    var parameter = {
		                                        action: "getAccreceiveVOByOrderNO",
		                                        order_no: $("#dialog-form-update-receive  input[name='dialog_order_no']").val()
		                                    };
		                                    draw_detail(parameter, '2');
		                                    parameter = {
		                                        action: "searh_receive_date",
		                                        start_date: $("#hidAccreceiveBeginDate").val(),
		                                        end_date: $("#hidAccreceiveEndDate").val()
		                                    }
		                                    draw_receive(parameter);
		                                    massageDialog('執行成功', '修改');
		                                } else {
		                                    massageDialog('執行失敗', '修改');
		                                }
		
		                            }
		                        });
		
		                        $(this).dialog("close");
		                    }
		                }, {
		                    text: "取消",
		                    click: function() {
		                        dialog_form_update.dialog("close");
		                    }
		                }]
		            });
		
		            dialog_form_update.dialog("open");
		        });
		
		
		        $("#account_amount_date_table").delegate(".btn_list", "click", function() {
		
		
		            var row = $(this).closest("tr");
		            var data = $("#account_amount_date_table").DataTable().row(row).data();
		
		            var parameter = {
		                action: "getAccreceiveVOByOrderNO",
		                order_no: data.order_no
		            };
		
		            draw_detail(parameter, '1');
		
		            initUpdateDialog();
		
		            var dialog_form_detail = $("#dialog-form-noreceive-detail").dialog({
		                draggable: true, //防止拖曳
		                resizable: false, //防止縮放
		                autoOpen: false,
		                height: "auto",
		                width: "900",
		                modal: true,
		                show: {
		                    effect: "blind",
		                    duration: 300
		                },open: function(event, ui) {
	                        $(this).parent().children().children('.ui-dialog-titlebar-close').hide();
	                    },
		                hide: {
		                    effect: "fade",
		                    duration: 300
		                },
		                buttons: {
		                    "關閉": function() {
		                        $(this).dialog("close");
		                    }
		                }
		            });
		
		            dialog_form_detail.dialog("open");
		        });
		
		
		        $("#account_amount_date_table").delegate(".btn_delete", "click", function() {
		            var row = $(this).closest("tr");
		            var data = $("#account_amount_date_table").DataTable().row(row).data();
		
		            $("#hidOrder_no").val(data.order_no);
		
		            var confirm_delete_dialog = $("#dialog-delete-confirm").dialog({
		                draggable: true, //防止拖曳
		                resizable: false, //防止縮放
		                autoOpen: false,
		                height: 200,
		                modal: true,
		                show: {
		                    effect: "blind",
		                    duration: 300
		                },
		                open: function(event, ui) {
	                        $(this).parent().children().children('.ui-dialog-titlebar-close').hide();
	                    },
		                hide: {
		                    effect: "fade",
		                    duration: 300
		                },
		                buttons: {
		                    "確認刪除": function() {
		                        $.ajax({
		                            url: 'accreceive.do',
		                            type: 'post',
		                            data: {
		                                action: 'delete_receivable',
		                                order_no: $("#hidOrder_no").val()
		                            },
		                            error: function(xhr) {
		                                alere("error");
		                            },
		                            success: function(response) {
		
		                                if ("success" == response) {
		                                    massageDialog('執行成功', '刪除');
		                                    parameter = {
		                                        action: "searh_amount_date",
		                                        start_date: $("#hidAccreceiveBeginDate").val(),
		                                        end_date: $("#hidAccreceiveEndDate").val()
		                                    }
		                                    draw_accreceive(parameter);
		
		
		
		                                } else {
		                                    massageDialog('執行失敗', '刪除');
		                                }
		
		                            }
		                        });
		
		                        $(this).dialog("close");
		                    },
		                    "取消": function() {
		                        $(this).dialog("close");
		                    }
		                }
		            });
		            confirm_delete_dialog.dialog("open");
		        });
		
		
		
		        $("#account_receive_date_table").delegate(".btn_list", "click", function() {
		
		
		            var row = $(this).closest("tr");
		            var data = $("#account_receive_date_table").DataTable().row(row).data();
		
		            var parameter = {
		                action: "getAccreceiveVOByOrderNO",
		                order_no: data.order_no
		            };
		
		            draw_detail(parameter, '2');
		
		            initUpdateDialog();
		
		            var dialog_form_detail = $("#dialog-form-receive-detail").dialog({
		                draggable: true, //防止拖曳
		                resizable: false, //防止縮放
		                autoOpen: false,
		                height: "auto",
		                width: "900",
		                modal: true,
		                show: {
		                    effect: "blind",
		                    duration: 300
		                },open: function(event, ui) {
	                        $(this).parent().children().children('.ui-dialog-titlebar-close').hide();
	                    },
		                hide: {
		                    effect: "fade",
		                    duration: 300
		                },
		                buttons: {
		                    "關閉": function() {
		                        $(this).dialog("close");
		                    }
		                }
		            });
		
		            dialog_form_detail.dialog("open");
		        });
		
		
		        $("#account_receive_date_table").delegate(".btn_delete", "click", function() {
		            var row = $(this).closest("tr");
		            var data = $("#account_receive_date_table").DataTable().row(row).data();
		
		            $("#hidOrder_no").val(data.order_no);
		
		            var confirm_delete_dialog = $("#dialog-delete-confirm").dialog({
		                draggable: true, //防止拖曳
		                resizable: false, //防止縮放
		                autoOpen: false,
		                height: 200,
		                modal: true,
		                show: {
		                    effect: "blind",
		                    duration: 300
		                },
		                open: function(event, ui) {
	                        $(this).parent().children().children('.ui-dialog-titlebar-close').hide();
	                    },
		                hide: {
		                    effect: "fade",
		                    duration: 300
		                },
		                buttons: {
		                    "確認刪除": function() {
		                        $.ajax({
		                            url: 'accreceive.do',
		                            type: 'post',
		                            data: {
		                                action: 'delete_receivable',
		                                order_no: $("#hidOrder_no").val()
		                            },
		                            error: function(xhr) {
		                                alere("error");
		                            },
		                            success: function(response) {
		
		                                if ("success" == response) {
		                                    massageDialog('執行成功', '刪除');
		                                    var parameter = {
		                                        action: "searh_receive_date",
		                                        start_date: $("#hidReceiveBeginDate").val(),
		                                        end_date: $("#hidReceiveEndDate").val()
		                                    }
		                                    draw_receive(parameter);
		                                } else {
		                                    massageDialog('執行失敗', '刪除');
		                                }
		
		                            }
		                        });
		
		                        $(this).dialog("close");
		                    },
		                    "取消": function() {
		                        $(this).dialog("close");
		                    }
		                }
		            });
		            confirm_delete_dialog.dialog("open");
		        });
		
		
		
		
		        $(".bdyplane").animate({
		            "opacity": "1"
		        });
		        $("#amount_date_form").validate({
		            rules: {
		                amount_start_date: {
		                    dateISO: true
		                },
		                amount_end_date: {
		                    dateISO: true
		                }
		            },
		            messages: {
		                amount_start_date: {
		                    dateISO: "日期格式錯誤"
		                },
		                amount_end_date: {
		                    dateISO: "日期格式錯誤"
		                }
		            }
		        });
		        $("#receive_date_form").validate({
		            rules: {
		                receive_start_date: {
		                    dateISO: true
		                },
		                receive_end_date: {
		                    dateISO: true
		                }
		            },
		            messages: {
		                receive_start_date: {
		                    dateISO: "日期格式錯誤"
		                },
		                receive_end_date: {
		                    dateISO: "日期格式錯誤"
		                }
		            }
		        });
		        //應收帳款日查詢相關設定
		        $("#searh_amount_date").click(function(e) {
		            e.preventDefault();
		
		            var $startDate = $("#amount_start_date").val();
		            var $endDate = $("#amount_end_date").val();
		
		            var errorMes = '';
		            var $mes = $('#message #text');
		            if ($endDate != null && $endDate.length != 0 && $startDate != null && $startDate.length != 0) {
		                if ($startDate.replace(/-/g, "") > $endDate.replace(/-/g, "")) {
		                    errorMes += "訖日不可小於起日!<br>";
		                }
		            }
		            if ($startDate == null || $startDate.length == 0) {
		                errorMes += "請選擇起日!<br>";
		            } else if (!dateValidationCheck($startDate)) {
		                errorMes += "起日格式不符!<br>";
		            }
		            if ($endDate == null || $endDate.length == 0) {
		                errorMes += "請選擇訖日!<br>";
		            } else if (!dateValidationCheck($endDate)) {
		                errorMes += "訖日格式不符!<br>";
		            }
		
		            if (errorMes.length > 0) {
		                massageDialog(errorMes, '警告');
		                return false;
		            }
		
		            $("#hidAccreceiveBeginDate").val($startDate);
		            $("#hidAccreceiveEndDate").val($endDate);
		
		            if ($("#amount_date_form").valid()) {
		                var parameter = {
		                    action: "searh_amount_date",
		                    start_date: $("#amount_start_date").val(),
		                    end_date: $("#amount_end_date").val()
		                }
		                draw_accreceive(parameter);
		            }
		        });
		
		        //實收帳款日查詢相關設定
		        $("#searh_receive_date").click(function(e) {
		            e.preventDefault();
		            var $startDate = $("#receive_start_date").val();
		            var $endDate = $("#receive_end_date").val();
		
		
		            var errorMes = '';
		            var $mes = $('#message #text');
		            if ($endDate != null && $endDate.length != 0 && $startDate != null && $startDate.length != 0) {
		                if ($startDate.replace(/-/g, "") > $endDate.replace(/-/g, "")) {
		                    errorMes += "訖日不可小於起日!<br>";
		                }
		            }
		            if ($startDate == null || $startDate.length == 0) {
		                errorMes += "請選擇起日!<br>";
		            } else if (!dateValidationCheck($startDate)) {
		                errorMes += "起日格式不符!<br>";
		            }
		            if ($endDate == null || $endDate.length == 0) {
		                errorMes += "請選擇訖日!<br>";
		            } else if (!dateValidationCheck($endDate)) {
		                errorMes += "訖日格式不符!<br>";
		            }
		
		            if (errorMes.length > 0) {
		                massageDialog(errorMes, '警告');
		                return false;
		            }
		
		            $("#hidReceiveBeginDate").val($startDate);
		            $("#hidReceiveEndDate").val($endDate);
		
		
		            if ($("#receive_date_form").valid()) {
		
		                var parameter = {
		                    action: "searh_receive_date",
		                    start_date: $("#receive_start_date").val(),
		                    end_date: $("#receive_end_date").val()
		                }
		                draw_receive(parameter);
		            }
		        });
		        confirm_dialog = $("#dialog-confirm").dialog({
		            draggable: true, //防止拖曳
		            resizable: false, //防止縮放
		            autoOpen: false,
		            height: 200,
		            modal: true,
		            show: {
		                effect: "blind",
		                duration: 300
		            },
		            hide: {
		                effect: "fade",
		                duration: 300
		            },
		            buttons: {
		                "確認批次收帳": function() {
		                    var order_nos = '';
		
		                    $(".checkbox_receive").each(function() {
		                        if ($(this).prop("checked")) {
		                            order_nos += $(this).attr("value") + '~';
		                        }
		                    });
		
		                    order_nos = order_nos.slice(0, -1);
		
		                    $.ajax({
		                        type: "POST",
		                        url: "accreceive.do",
		                        async: false,
		                        data: {
		                            action: "receive_account",
		                            order_nos: order_nos,
		                            receive_date: getCurrentDate()
		                        },
		                        success: function(result) {
		                            if ($("#hidReceiveBeginDate").val() != "") {
		                                var parameter = {
		                                    action: "searh_receive_date",
		                                    start_date: $("#hidReceiveBeginDate").val(),
		                                    end_date: $("#hidReceiveEndDate").val()
		                                }
		                                draw_receive(parameter);
		                            }
		                            if ($("#hidAccreceiveBeginDate").val() != "") {
			                            parameter = {
			                                action: "searh_amount_date",
			                                start_date: $("#hidAccreceiveBeginDate").val(),
			                                end_date: $("#hidAccreceiveEndDate").val()
			                            }
			                            draw_accreceive(parameter);
		                            }
		                        }
		                    });
		
		                    $(this).dialog("close");
		                },
		                "取消": function() {
		                    $(this).dialog("close");
		                }
		            }
		        });
		        $("#dialog-confirm").show();
		
		        confirm_cancel_dialog = $("#dialog-cancel-confirm").dialog({
		            draggable: true, //防止拖曳
		            resizable: false, //防止縮放
		            autoOpen: false,
		            height: 200,
		            modal: true,
		            show: {
		                effect: "blind",
		                duration: 300
		            },
		            hide: {
		                effect: "fade",
		                duration: 300
		            },
		            buttons: {
		                "確認取消收帳": function() {
		
		                    var order_no = '';
		
		                    $(".checkbox_receive_cancel").each(function() {
		                        if ($(this).prop("checked")) {
		                            order_no += $(this).attr("value") + '~';
		                        }
		                    });
		                    order_no = order_no.slice(0, -1);
		
		                    $.ajax({
		                        type: "POST",
		                        url: "accreceive.do",
		                        async: false,
		                        data: {
		                            action: "delete_receive_account",
		                            order_nos: order_no,
		                            receive_date: getCurrentDate()
		                        },
		                        success: function(result) {
		                        	if ($("#hidReceiveBeginDate").val() != "") {
			                            var parameter = {
			                                action: "searh_receive_date",
			                                start_date: $("#hidReceiveBeginDate").val(),
			                                end_date: $("#hidReceiveEndDate").val()
			                            }
		                            }
		                            draw_receive(parameter);
		                            if ($("#hidAccreceiveBeginDate").val() != "") {
		                                parameter = {
		                                    action: "searh_amount_date",
		                                    start_date: $("#hidAccreceiveBeginDate").val(),
		                                    end_date: $("#hidAccreceiveEndDate").val()
		                                }
		                                draw_accreceive(parameter);
		                            }
		
		                        }
		                    });
		                    $(this).dialog("close");
		                },
		                "取消": function() {
		                    $(this).dialog("close");
		                }
		            }
		        });
		        $("#dialog-cancel-confirm").show();
		        $(".input-field-wrap").append("<div class='div_right_bottom upup'><img src='./images/upup.png'></div>");
		        $(".input-field-wrap").after("<div class='div_right_top downdown' style='display:none;'><img src='./images/downdown.png'></div>");
		        $(".upup").click(function() {
		            $(".input-field-wrap").slideToggle("slow");
		            $(".downdown").slideToggle();
		        });
		        $(".downdown").click(function() {
		            $(".input-field-wrap").slideToggle("slow");
		            $(".downdown").slideToggle();
		        });
		        
		        //get today yyyy-mm-dd
		        function getCurrentDate() {
		            var fullDate = new Date();
		            var twoDigitMonth = fullDate.getMonth() + 1;
		            if (twoDigitMonth.length == 1) twoDigitMonth = "0" + twoDigitMonth;
		            var twoDigitDate = fullDate.getDate() + "";
		            if (twoDigitDate.length == 1) twoDigitDate = "0" + twoDigitDate;
		            var currentDate = fullDate.getFullYear() + "-" + twoDigitMonth + "-" + twoDigitDate;
		            return currentDate;
		        }
		        //日期設定
		        $(".date").datepicker({
		            dayNamesMin: ["日", "一", "二", "三", "四", "五", "六"],
		            monthNames: ["一月", "二月", "三月", "四月", "五月", "六月", "七月", "八月", "九月", "十月", "十一月", "十二月"],
		            dateFormat: "yy-mm-dd",
		            changeYear: true
		        });
		    })
		</script>
			<div class="datalistWrap" >
				<!--對話窗樣式-確認 -->
				<div id="dialog-confirm" title="確認批次收帳嗎?" style="display:none;">
					<br><p></p>
				</div>
				<!--對話窗樣式-確認-取消 -->
				<div id="dialog-cancel-confirm" title="確認取消批次收帳嗎?" style="display:none;">
					<br><p></p>
				</div>
				<!--對話窗樣式-確認-取消 -->
				<div id="dialog-delete-confirm" title="警告" style="display:none;">
					<br><p>確認刪除收帳嗎?</p>
				</div>
				<!--對話窗樣式-應收修改- -->
				<div id="dialog-form-update-noreceive" title="修改資料" style="display: none;">
					<form name="update-dialog-form-post" id="update-dialog-form-post" style="display: inline">
						<fieldset>
							<table class="form-table">
								<tbody>
									<tr>
										<td>訂單編號：</td>
										<td><input type="text"  name="dialog_order_no" style=" background:white;" disabled readonly /></td>
									</tr>
									<tr>
										<td>產品名稱：</td>
										<td><input type="text"  name="dialog_product_name" style=" background:white;" disabled readonly /></td>
									</tr>
									<tr>
										<td>應收帳款金額：</td>
										<td><input type="text"  name="dialog_amount"  /></td>
									</tr>
									<tr>
										<td>備註：</td>
										<td><input type="text"  name="dialog_memo" /></td>
									</tr>
									<tr>
										<td>應收帳款產生日期：</td>
										<td><input type="text"  name="dialog_amount_date" style=" background:white;" disabled readonly /></td>
									</tr>
										
								</tbody>
							</table>
						</fieldset>
					</form>
				</div>
				
				<!--對話窗樣式-實收修改- -->
				<div id="dialog-form-update-receive" title="修改資料" style="display: none;">
					<form name="update-dialog-form-post" id="update-dialog-form-post" style="display: inline">
						<fieldset>
							<table class="form-table">
								<tbody>
									<tr>
										<td>訂單編號：</td>
										<td><input type="text"  name="dialog_order_no" style=" background:white;" disabled readonly /></td>
									</tr>
						
									<tr>
										<td>產品名稱：</td>
										<td><input type="text"  name="dialog_product_name" class="dialog_disable" style=" background:white;" disabled readonly /></td>
									</tr>
									<tr >
										<td>實收帳款金額：</td>
										<td><input type="text"  name="dialog_receive_amount"  /></td>
									</tr>
									<tr>
										<td>備註：</td>
										<td><input type="text" name="dialog_memo" /></td>
									</tr>
									<tr>
										<td>實收帳款產生日期：</td>
										<td><input type="text"  name="dialog_receive_date" style=" background:white;" disabled readonly /></td>
									</tr>	
								</tbody>
							</table>
						</fieldset>
					</form>
				</div>
	
				<!--對話窗 應收 明細-->
				<div id="dialog-form-noreceive-detail" title="修改資料" style="display: none;">
					<table id="noreceive_detail_table" class="result-table"></table>
				</div>
				
				<!--對話窗 實收  明細-->
				<div id="dialog-form-receive-detail" title="修改資料" style="display: none;">
					<table id="receive_detail_table" class="result-table"></table>
				</div>
	
				<!-- 第一列 -->
				<div class="input-field-wrap">
					<div class="form-wrap">
						<form id="amount_date_form" name="trans_list_date_form">
							<div class="form-row">
								<label for="">
									<span class="block-label">應收起日</span>
									<input type="text" class="input-date" id="amount_start_date" name="amount_start_date">
								</label>
								<div class="forward-mark"></div>
								<label for="">
									<span class="block-label">應收迄日</span>
									<input type="text" class="input-date" id="amount_end_date" name="amount_end_date">
								</label>
								<button id="searh_amount_date" class="btn btn-darkblue">查詢</button>
							</div>
						</form>
		
					</div><!-- /.form-wrap -->
				</div><!-- /.input-field-wrap -->
				<!-- 第二列 -->
				<div class=" search-result-wrap" align="center" style="height:433px;">
					<div id="account_amount_date_contain" class="result-table-wrap" >
						<table id="account_amount_date_table" class="result-table">
						</table>
					</div>
				</div>
			</div>
	
			<div class="datalistWrap">
				<!-- 第三列 -->
				<hr class="hr0">
				<div class="input-field-wrap">
					<div class="form-wrap">
						<form id="receive_date_form" name="trans_list_date_form">
							<div class="form-row">
								<label for="">
									<span class="block-label">實收起日</span>
									<input type="text" class="input-date" id="receive_start_date" name="receive_start_date">
								</label>
								<div class="forward-mark"></div>
								<label for="">
									<span class="block-label">實收迄日</span>
									<input type="text" class="input-date" id="receive_end_date" name="receive_end_date">
								</label>
								<button id="searh_receive_date" class="btn btn-darkblue">查詢</button>				
							</div>
				
						</form>
					</div><!-- /.form-wrap -->
				</div><!-- /.input-field-wrap -->
				<div class="row search-result-wrap" align="center" style="height:433px;">
					<div id="account_receive_date_contain" class="result-table-wrap" ">
						<table id="account_receive_date_table" class="result-table">
					
						</table>
					</div>
				</div>
			</div>
		</div>
		</div>
		<div id="my"></div>
		<input type="hidden" id=hidAccreceiveBeginDate value=''>
		<input type="hidden" id=hidAccreceiveEndDate value=''>
		<input type="hidden" id=hidReceiveBeginDate value=''>
		<input type="hidden" id=hidReceiveEndDate value=''>
		<input type="hidden" id=hidreceive_id value=''>
		<input type="hidden" id=hidOrder_no value=''>
		<div id="message" align="center">
			<div id="text"></div>
		</div>
	</body>
</html>