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
		<style type="text/css">
		.dataTables_wrapper .dt-buttons {float:right;}
		input[type="text"]:disabled, input[type="text"].readonly {
		    border: 0;
		    width:450px;
		}
		</style>
		<title>應付帳款</title>
		<meta charset="utf-8">
		<link rel="Shortcut Icon" type="image/x-icon" href="./images/Rockettheme-Ecommerce-Shop.ico" />
		<link rel="stylesheet" href="css/styles.css" />
		<link href="<c:url value="css/css.css" />" rel="stylesheet">
		<link href="<c:url value="css/jquery.dataTables.min.css" />" rel="stylesheet">
		<link href="<c:url value="css/1.11.4/jquery-ui.css" />" rel="stylesheet">
		<link href="<c:url value="css/dataTables.jqueryui.min.css" />" rel="stylesheet">
		<link href="<c:url value="css/buttons.jqueryui.min.css" />" rel="stylesheet">
		<link rel="stylesheet" href="css/buttons.dataTables.min.css">
	</head>
	<body>
		<jsp:include page="template.jsp" flush="true" />
		<div class="content-wrap">
			<div class='bdyplane' style="opacity: 0">
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
				    function drawnoaccpay(parameter) {
				        $("#account_amount_date_table").DataTable({
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
				                url: "accpay.do",
				                dataSrc: "",
				                type: "POST",
				                data: parameter
				            },
				            columns: [{
				                "title": "批次請求",
				                "data": null,
				                "defaultContent": ""
				            }, {
				                "title": "採購編號",
				                "data": "seq_no",
				                "defaultContent": ""
				            }, {
				                "title": "應付帳款金額",
				                "data": "amount",
				                "defaultContent": ""
				            }, {
				                "title": "實付帳款金額",
				                "data": "pay_amount",
				                "defaultContent": ""
				            }, {
				                "title": "應付帳款產生日期",
				                "data": "amount_date",
				                "defaultContent": ""
				            }, {
				                "title": "實付帳款產生日期",
				                "data": "pay_date",
				                "defaultContent": ""
				            }, {
				                "title": "供應商名稱",
				                "data": "supply_name",
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
				                targets: 0,
				                searchable: false,
				                orderable: false,
				                render: function(data, type, row) {
				
				                    var pay_id = row.pay_id;
				
				                    var input = document.createElement("INPUT");
				                    input.type = 'checkbox';
				                    input.name = 'checkbox-group-select';
				                    input.id = 'my-' + pay_id;
				                    input.className = 'checkbox_pay';
				                    input.value = pay_id
				
				                    var span = document.createElement("SPAN");
				                    span.className = 'form-label';
				
				                    var label = document.createElement("LABEL");
				                    label.htmlFor = 'my-' + pay_id;
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
				                        "class": "btn-in-table btn-darkblue btn_update",
				                        "title": "修改",
				                    }).append($("<i/>", {
				                        "class": "fa fa-pencil"
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
				                    $(".checkbox_pay").each(function() {
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
				
				    function drawaccpay(parameter) {
				        $("#account_pay_date_table").DataTable({
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
				                url: "accpay.do",
				                dataSrc: "",
				                type: "POST",
				                data: parameter
				            },
				            columns: [{
				                "title": "批次請求",
				                "data": null,
				                "defaultContent": ""
				            }, {
				                "title": "採購編號",
				                "data": "seq_no",
				                "defaultContent": ""
				            }, {
				                "title": "應付帳款金額",
				                "data": "amount",
				                "defaultContent": ""
				            }, {
				                "title": "實付帳款金額",
				                "data": "pay_amount",
				                "defaultContent": ""
				            }, {
				                "title": "應付帳款產生日期",
				                "data": "amount_date",
				                "defaultContent": ""
				            }, {
				                "title": "實付帳款產生日期",
				                "data": "pay_date",
				                "defaultContent": ""
				            }, {
				                "title": "供應商名稱",
				                "data": "supply_name",
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
				                targets: 0,
				                searchable: false,
				                orderable: false,
				                render: function(data, type, row) {
				
				                    var pay_id = row.pay_id;
				
				                    var input = document.createElement("INPUT");
				                    input.type = 'checkbox';
				                    input.name = 'checkbox-group-select';
				                    input.id = 'my-' + pay_id;
				                    input.className = 'checkbox_pay_cancel';
				                    input.value = pay_id
				
				                    var span = document.createElement("SPAN");
				                    span.className = 'form-label';
				
				                    var label = document.createElement("LABEL");
				                    label.htmlFor = 'my-' + pay_id;
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
				                        "class": "btn-in-table btn-darkblue btn_update",
				                        "title": "修改",
				                    }).append($("<i/>", {
				                        "class": "fa fa-pencil"
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
				                text: '取消批次收帳',
				                className: 'btn_receive',
				                action: function(e) {
				                    e.preventDefault();
				                    var count = 0;
				                    var message = "";
				                    $(".checkbox_pay_cancel").each(function() {
				                        if ($(this).prop("checked")) {
				                            count += 1;
				                        }
				                    });
				
				                    if (count == 0) {
				                        massageDialog('至少一筆', '警告');
				                        return false;
				                    }
				
				                    message = "確認要收帳嗎? 總共" + count + "筆";
				                    $("#dialog-cancel-confirm p").text(message);
				                    confirm_cancel_dialog.dialog("open");
				                }
				            }]
				        });
				    }
				
				    function massageDialog(massage, tital) {
				        $('#message #text').val('').html(massage);
				        $('#message')
				            .dialog()
				            .dialog('option', 'title', tital)
				            .dialog('option', 'width', 'auto')
				            .dialog('option', 'minHeight', 'auto')
				            .dialog("open");
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
				        $("#pay_date_form").validate({
				            rules: {
				                pay_start_date: {
				                    dateISO: true
				                },
				                pay_end_date: {
				                    dateISO: true
				                }
				            },
				            messages: {
				                pay_start_date: {
				                    dateISO: "日期格式錯誤"
				                },
				                pay_end_date: {
				                    dateISO: "日期格式錯誤"
				                }
				            }
				        });
				        var table;
				        //應付帳款日查詢相關設定
				        $("#searh_amount_date").click(function(e) {
				            e.preventDefault();
				            if ($("#amount_date_form").valid()) {

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
				            	
				                $("#hidNOAccpayBeginDate").val($("#amount_start_date").val());
				                $("#hidNOAccpayEndDate").val($("#amount_end_date").val());
				                var parameter = {
				                    action: "searh_amount_date",
				                    start_date: $("#amount_start_date").val(),
				                    end_date: $("#amount_end_date").val()
				                };
				                drawnoaccpay(parameter);
				            }
				        });
				        //實付帳款日查詢相關設定
				        $("#searh_pay_date").click(function(e) {
				            e.preventDefault();
				            if ($("#pay_date_form").valid()) {
				            	
				                var $startDate = $("#pay_start_date").val();
					            var $endDate = $("#pay_end_date").val();
					
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
					            
				                $("#hidAccpayBeginDate").val($("#pay_start_date").val());
				                $("#hidAccpayEndDate").val($("#pay_end_date").val());
				
				                var parameter = {
				                    action: "searh_pay_date",
				                    start_date: $("#pay_start_date").val(),
				                    end_date: $("#pay_end_date").val()
				                };
				                drawaccpay(parameter);
				            }
				        });
				        confirm_dialog = $("#dialog-confirm").dialog({
				            draggable: true, //防止拖曳
				            resizable: false, //防止縮放
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
				            buttons: {
				                "確認批次收帳": function() {
				                    var pay_ids = '';
				
				                    $(".checkbox_pay").each(function() {
				                        if ($(this).prop("checked")) {
				                            pay_ids += $(this).attr("value") + '~';
				                        }
				                    });
				
				                    pay_ids = pay_ids.slice(0, -1);
				                    console.log("pay_ids" + pay_ids);
				                    $.ajax({
				                        type: "POST",
				                        url: "accpay.do",
				                        async: false,
				                        data: {
				                            action: "pay_account",
				                            pay_ids: pay_ids,
				                            receive_date: getCurrentDate()
				                        },
				                        success: function(result) {
				                            if ($("#hidNOAccpayBeginDate").val() != "") {
				                                var parameter = {
				                                    action: "searh_amount_date",
				                                    start_date: $("#hidNOAccpayBeginDate").val(),
				                                    end_date: $("#hidNOAccpayEndDate").val()
				                                };
				                                drawnoaccpay(parameter);
				                            }
				                            if ($("#hidAccpayBeginDate").val() != "") {
				                                var parameter = {
				                                    action: "searh_pay_date",
				                                    start_date: $("#hidAccpayBeginDate").val(),
				                                    end_date: $("#hidAccpayEndDate").val()
				                                };
				                                drawaccpay(parameter);
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
				            buttons: {
				                "確認取消批次收帳": function() {
				                    var pay_ids = '';
				
				                    $(".checkbox_pay_cancel").each(function() {
				                        if ($(this).prop("checked")) {
				                            pay_ids += $(this).attr("value") + '~';
				                        }
				                    });
				
				                    pay_ids = pay_ids.slice(0, -1);
				                    console.log("checkbox_pay_cancel:" + pay_ids);
				                    $.ajax({
				                        type: "POST",
				                        url: "accpay.do",
				                        async: false,
				                        data: {
				                            action: "delete_pay_account",
				                            pay_ids: pay_ids,
				                        },
				                        success: function(result) {
				                            if ($("#hidNOAccpayBeginDate").val() != "") {
				                                var parameter = {
				                                    action: "searh_amount_date",
				                                    start_date: $("#hidNOAccpayBeginDate").val(),
				                                    end_date: $("#hidNOAccpayEndDate").val()
				                                };
				                                drawnoaccpay(parameter);
				                            }
				                            if ($("#hidAccpayBeginDate").val() != "") {
				                                var parameter = {
				                                    action: "searh_pay_date",
				                                    start_date: $("#hidAccpayBeginDate").val(),
				                                    end_date: $("#hidAccpayEndDate").val()
				                                };
				                                drawaccpay(parameter);
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
				
				
				        $("#account_amount_date_table").delegate(".btn_update", "click", function() {
				
				            var row = $(this).closest("tr");
				            var data = $("#account_amount_date_table").DataTable().row(row).data();
				
				            $("#dialog-form-update-noaccpay  input[name='dialog_amount']").val(data.amount);
				            $("#dialog-form-update-noaccpay  input[name='dialog_amount_date']").val(data.amount_date);
				            $("#dialog-form-update-noaccpay  input[name='dialog_memo']").val(data.memo);
				            $("#dialog-form-update-noaccpay  input[name='dialog_seq_no']").val(data.seq_no);
				            $("#dialog-form-update-noaccpay  input[name='dialog_supply_name']").val(data.supply_name);
				            $("#hidpay_id").val(data.pay_id);
				
				
				            var dialog_form_update = $("#dialog-form-update-noaccpay").dialog({
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
				                            url: 'accpay.do',
				                            type: 'post',
				                            data: {
				                                action: 'update_noaccpay',
				                                amount: $("#dialog-form-update-noaccpay  input[name='dialog_amount']").val(),
				                                amount_date: $("#dialog-form-update-noaccpay  input[name='dialog_amount_date']").val(),
				                                memo: $("#dialog-form-update-noaccpay  input[name='dialog_memo']").val(),
				                                pay_id: $("#hidpay_id").val()
				                            },
				                            error: function(xhr) {
				                                alere("error");
				                            },
				                            success: function(response) {
				                                if ("success" == response) {
				                                    var parameter = {
				                                        action: "searh_amount_date",
				                                        start_date: $("#hidNOAccpayBeginDate").val(),
				                                        end_date: $("#hidNOAccpayEndDate").val()
				                                    };
				                                    drawnoaccpay(parameter);
				
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
				
				
				        $("#account_pay_date_table").delegate(".btn_update", "click", function() {
				
				            var row = $(this).closest("tr");
				            var data = $("#account_pay_date_table").DataTable().row(row).data();
				
				            $("#dialog-form-update-accpay  input[name='dialog_pay_amount']").val(data.pay_amount);
				            $("#dialog-form-update-accpay  input[name='dialog_amount_date']").val(data.amount_date);
				            $("#dialog-form-update-accpay  input[name='dialog_memo']").val(data.memo);
				            $("#dialog-form-update-accpay  input[name='dialog_seq_no']").val(data.seq_no);
				            $("#dialog-form-update-accpay  input[name='dialog_supply_name']").val(data.supply_name);
				            $("#hidpay_id").val(data.pay_id);
				
				
				            var dialog_form_update = $("#dialog-form-update-accpay").dialog({
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
				                            url: 'accpay.do',
				                            type: 'post',
				                            data: {
				                                action: 'update_accpay',
				                                pay_amount: $("#dialog-form-update-accpay  input[name='dialog_pay_amount']").val(),
				                                amount_date: $("#dialog-form-update-accpay  input[name='dialog_amount_date']").val(),
				                                memo: $("#dialog-form-update-accpay  input[name='dialog_memo']").val(),
				                                pay_id: $("#hidpay_id").val()
				                            },
				                            error: function(xhr) {
				                                alere("error");
				                            },
				                            success: function(response) {
				                                if ("success" == response) {
				                                    var parameter = {
				                                        action: "searh_pay_date",
				                                        start_date: $("#hidAccpayBeginDate").val(),
				                                        end_date: $("#hidAccpayEndDate").val()
				                                    };
				                                    drawaccpay(parameter);
				
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
				        
				        $("#account_pay_date_table").delegate(".btn_delete", "click", function() {
				            var row = $(this).closest("tr");
				            var data = $("#account_pay_date_table").DataTable().row(row).data();
				            $("#hidpay_id").val(data.pay_id);
				
				            var confirm_delete_dialog = $("#confirm_delete_dialog").dialog({
				                draggable: true, //防止拖曳
				                resizable: false, //防止縮放
				                autoOpen: false,
				                height: "auto",
				                width: "auto",
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
				                    id: "delete_enter",
				                    text: "確認刪除",
				                    click: function() {
				                        $.ajax({
				                            url: 'accpay.do',
				                            type: 'post',
				                            data: {
				                                action: 'delete_accpay_by_pay_id',
				                                pay_id: $("#hidpay_id").val()
				                            },
				                            error: function(xhr) {
				                                alere("error");
				                            },
				                            success: function(response) {
				                                if ("success" == response) {
				                                    var parameter = {
				                                        action: "searh_pay_date",
				                                        start_date: $("#hidAccpayBeginDate").val(),
				                                        end_date: $("#hidAccpayEndDate").val()
				                                    };
				                                    drawaccpay(parameter);
				
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
				                    	confirm_delete_dialog.dialog("close");
				                    }
				                }]
				            });
				
				            confirm_delete_dialog.dialog("open");
				        });
				        
				        
				        $("#account_amount_date_table").delegate(".btn_delete", "click", function() {
				            var row = $(this).closest("tr");
				            var data = $("#account_amount_date_table").DataTable().row(row).data();
				            $("#hidpay_id").val(data.pay_id);
				
				            var confirm_delete_dialog = $("#confirm_delete_dialog").dialog({
				                draggable: true, //防止拖曳
				                resizable: false, //防止縮放
				                autoOpen: false,
				                height: "auto",
				                width: "auto",
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
				                    id: "delete_enter",
				                    text: "確認刪除",
				                    click: function() {
				                        $.ajax({
				                            url: 'accpay.do',
				                            type: 'post',
				                            data: {
				                                action: 'delete_accpay_by_pay_id',
				                                pay_id: $("#hidpay_id").val()
				                            },
				                            error: function(xhr) {
				                                alere("error");
				                            },
				                            success: function(response) {
				                                if ("success" == response) {
				                                    var parameter = {
						                                    action: "searh_amount_date",
						                                    start_date: $("#hidNOAccpayBeginDate").val(),
						                                    end_date: $("#hidNOAccpayEndDate").val()
						                                };
						                            drawnoaccpay(parameter);
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
				                    	confirm_delete_dialog.dialog("close");
				                    }
				                }]
				            });
				
				            confirm_delete_dialog.dialog("open");
				        });
				    });
				</script>		
				<div class="datalistWrap">
					<!--對話窗樣式-確認 -->
					<div id="dialog-confirm" title="確認批次付帳嗎?" style="display: none;">
						<br>
						<p></p>
					</div>
					<!--對話窗樣式-確認-取消 -->
					<div id="dialog-cancel-confirm" title="確認取消批次付帳嗎?" style="display: none;">
						<br>
						<p></p>
					</div>
					<!--對話窗樣式-確認-刪除 -->
					<div id="confirm_delete_dialog" title="警告" style="display:none;">
						<br><p>確認刪除付帳嗎?</p>
					</div>
					
					<!--對話窗樣式-提示 -->
					<div id="message" align="center">
						<div id="text"></div>
					</div>
	
					<!--對話窗樣式-應付修改- -->
					<div id="dialog-form-update-noaccpay" title="修改資料" style="display: none;">
						<form name="update-dialog-form-post" id="update-dialog-form-post" style="display: inline">
							<fieldset>
								<table class="form-table">
									<tbody>
										<tr>
											<td>採購編號：</td>
											<td><input type="text" name="dialog_seq_no" style="background: white;" disabled readonly /></td>
										</tr>
										<tr>
											<td>供應商名稱：</td>
											<td><input type="text" name="dialog_supply_name" style="background: white;" disabled readonly /></td>
										</tr>
										<tr>
											<td>應付帳款金額：</td>
											<td><input type="text" name="dialog_amount" /></td>
										</tr>
										<tr>
											<td>備註：</td>
											<td><input type="text" name="dialog_memo" /></td>
										</tr>
										<tr>
											<td>應付帳款產生日期：</td>
											<td><input type="text" name="dialog_amount_date" style="background: white;" disabled readonly /></td>
										</tr>
									</tbody>
								</table>
							</fieldset>
						</form>
					</div>
					<!--對話窗樣式-實付修改- -->
					<div id="dialog-form-update-accpay" title="修改資料" style="display: none;">
						<form name="update-dialog-form-post" id="update-dialog-form-post" style="display: inline">
							<fieldset>
								<table class="form-table">
									<tbody>
										<tr>
											<td>採購編號：</td>
											<td><input type="text" name="dialog_seq_no" style="background: white;" disabled readonly /></td>
										</tr>
										<tr>
											<td>供應商名稱：</td>
											<td><input type="text" name="dialog_supply_name" style="background: white;" disabled readonly /></td>
										</tr>
										<tr>
											<td>實付帳款金額：</td>
											<td><input type="text" name="dialog_pay_amount" /></td>
										</tr>
										<tr>
											<td>備註：</td>
											<td><input type="text" name="dialog_memo" /></td>
										</tr>
										<tr>
											<td>應付帳款產生日期：</td>
											<td><input type="text" name="dialog_amount_date" style="background: white;" disabled readonly /></td>
										</tr>
									</tbody>
								</table>
							</fieldset>
						</form>
					</div>
					<input type="hidden" id=hidNOAccpayBeginDate value=''> <input type="hidden" id=hidNOAccpayEndDate value=''> <input type="hidden" id=hidAccpayBeginDate value=''> <input type="hidden" id=hidAccpayEndDate value=''> <input type="hidden" id=hidpay_id value=''>
					<!-- 第一列 -->
					<div class="input-field-wrap">
						<div class="form-wrap">
							<form id="amount_date_form" name="trans_list_date_form">
								<div class="form-row">
									<label for=""> <span class="block-label">應付起日</span> <input type="text" class="input-date" id="amount_start_date" name="amount_start_date">
									</label>
									<div class="forward-mark"></div>
									<label for=""> <span class="block-label">應付迄日</span> <input type="text" class="input-date" id="amount_end_date" name="amount_end_date">
									</label>
									<button id="searh_amount_date" class="btn btn-darkblue">查詢</button>
								</div>
							</form>
						</div>
						<!-- /.form-wrap -->
					</div>
					<!-- /.input-field-wrap -->
					<!-- 第二列 -->
	
					<div class="row search-result-wrap" align="center" style="height: 433px;">
						<div id="account_amount_date_contain" class="result-table-wrap">
							<table id="account_amount_date_table" class="result-table">
							</table>
						</div>
					</div>
				</div>
				<div class="panel-content">
					<div class="datalistWrap">
	
						<!-- 第四列 -->
						<hr class="hr0">
						<div class="input-field-wrap">
							<div class="form-wrap">
								<form id="pay_date_form" name="trans_list_date_form">
									<div class="form-row">
										<label for=""> <span class="block-label">實付起日</span> <input type="text" class="input-date" id="pay_start_date" name="pay_start_date">
										</label>
										<div class="forward-mark"></div>
										<label for=""> <span class="block-label">實付迄日</span> <input type="text" class="input-date" id="pay_end_date" name="pay_end_date">
										</label>
										<button id="searh_pay_date" class="btn btn-darkblue">查詢</button>
									</div>
								</form>
							</div>
							<!-- /.form-wrap -->
						</div>
						<!-- /.input-field-wrap -->
						<div class="row search-result-wrap" align="center" style="height: 433px;">
	
							<div id="account_pay_date_contain" class="result-table-wrap">
								<table id="account_pay_date_table" class="result-table">
								</table>
							</div>
						</div>
					</div>
				</div>
			</div>
		</div>
	</body>
</html>