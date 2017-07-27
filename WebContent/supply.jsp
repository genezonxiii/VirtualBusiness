<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ page import="tw.com.aber.productunit.controller.*"%>
<%@ page import="java.util.*"%>
<%@ page import="java.sql.DriverManager"%>
<%@ page import="java.sql.Connection"%>
<%@ page import="java.sql.Statement"%>
<%@ page import="java.sql.ResultSet"%>
<jsp:directive.page import="java.sql.SQLException" />
<!DOCTYPE html>
<html>
<head>
<title>供應商資料</title>
<meta charset="utf-8">
<link rel="Shortcut Icon" type="image/x-icon" href="./images/Rockettheme-Ecommerce-Shop.ico" />
<link rel="stylesheet" href="css/styles.css" />
<link href="<c:url value="css/css.css" />" rel="stylesheet">
<link href="<c:url value="css/jquery.dataTables.min.css" />" rel="stylesheet">
<link href="<c:url value="css/1.11.4/jquery-ui.css" />" rel="stylesheet">
<style>
table.form-table {
 	border-spacing: 10px 8px !important; 
}
#tooltip{
    position:absolute;
    border:1px solid #333;
    background:#f7f5d1;
    padding:1px;
    color:#333;
    display:none;
}
</style>
</head>
<body>
	<jsp:include page="template.jsp" flush="true"/>
	<div class="content-wrap" >
		<div class='bdyplane' style="opacity:0">
		
<script type="text/javascript" src="js/jquery-1.10.2.js"></script>
<script type="text/javascript" src="js/jquery-1.11.4.js"></script>

<script type="text/javascript" src="js/jquery.dataTables.min.js"></script>
<script type="text/javascript" src="js/jquery-ui.min.js"></script>
<script type="text/javascript" src="js/jquery-ui.js"></script>
<script type="text/javascript" src="js/jquery.validate.min.js"></script>
<script type="text/javascript" src="js/additional-methods.min.js"></script>
<script type="text/javascript" src="js/messages_zh_TW.min.js"></script>

<script>
function draw_supply(parameter) {
    $("#supply_table").DataTable({
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
            url: "supply.do",
            dataSrc: "",
            type: "POST",
            data: parameter
        },
        columns: [{
            "title": "供應商名稱",
            "data": "supply_name",
            "defaultContent": ""
        }, {
            "title": "供應商統編",
            "data": "supply_unicode",
            "defaultContent": ""
        }, {
            "title": "供應商地址",
            "data": "address",
            "defaultContent": ""
        }, {
            "title": "第一聯絡人",
            "data": null,
            "defaultContent": ""
        }, {
            "title": "第二聯絡人",
            "data": null,
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
            "targets": [0, 1, 2, 5, 6]
        }, {
            //聯絡人1
            targets: 3,
            searchable: false,
            orderable: false,
            render: function(data, type, row) {

                var options = $("<div/>").append(
                    ((data.contact.length < 1) ? "" : ("&nbsp;&nbsp;姓名：" + data.contact + "<br>")) +
                    ((data.mobile.length < 1) ? "" : ("&nbsp;&nbsp;手機：" + data.mobile + "<br/>")) +
                    ((data.phone.length < 1) ? "" : ("&nbsp;&nbsp;電話：" + data.phone + "<br/>")) +
                    ((data.ext.length < 1) ? "" : ("&nbsp;&nbsp;分機：" + data.ext + "<br/>")) +
                    ((data.email.length < 1) ? "" : "&nbsp;&nbsp;email：" + data.email + "<br/>"));
                return options.html()

            }
        }, {
            //聯絡人1
            targets: 4,
            searchable: false,
            orderable: false,
            render: function(data, type, row) {

                var options = $("<div/>").append(
                    ((data.contact1.length < 1) ? "" : ("&nbsp;&nbsp;姓名：" + data.contact1 + "<br>")) +
                    ((data.mobile1.length < 1) ? "" : ("&nbsp;&nbsp;手機：" + data.mobile1 + "<br/>")) +
                    ((data.phone1.length < 1) ? "" : ("&nbsp;&nbsp;電話：" + data.phone1 + "<br/>")) +
                    ((data.ext1.length < 1) ? "" : ("&nbsp;&nbsp;分機：" + data.ext1 + "<br/>")) +
                    ((data.email1.length < 1) ? "" : "&nbsp;&nbsp;email：" + data.email1 + "<br/>"));
                return options.html()

            }
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
                    })
                    .append($("<button/>", {
                        "class": "btn-in-table btn-darkblue btn_update",
                        "title": "修改",
                    }).append($("<i/>", {
                        "class": "fa fa-pencil"
                    }))).append(
                        $("<button/>", {
                            "id": row.seq_no,
                            "value": row.sale_id,
                            "name": row.c_product_id,
                            "class": "btn-in-table btn-green btn_list",
                            "title": "清單"
                        })
                        .append($("<i/>", {
                            "class": "fa fa-pencil-square-o"
                        })).append($("<button/>", {
                            "class": "btn-in-table btn-alert btn_delete",
                            "title": "刪除",
                        }).append($("<i/>", {
                            "class": "fa fa-trash"
                        }))))));

                return options.html();
            }
        }]
    });
}

function draw_purchase(parameter) {
    $("#purchase_table").DataTable({
        dom: "lfr<t>ip",
        scrollY: "290px",
        width: '1200',
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
            url: "supply.do",
            dataSrc: "",
            type: "POST",
            data: parameter
        },
        columns: [{
            "title": "採購單號",
            "data": "seq_no",
            "defaultContent": ""
        }, {
            "title": "採購日期",
            "data": "purchase_date",
            "defaultContent": ""
        }, {
            "title": "供應商名稱",
            "data": "v_supply_name",
            "defaultContent": ""
        }, {
            "title": "採購發票號碼",
            "data": "invoice",
            "defaultContent": ""
        }, {
            "title": "發票樣式",
            "data": "invoice_type",
            "defaultContent": ""
        }, {
            "title": "採購發票金額",
            "data": "amount",
            "defaultContent": ""
        }, {
            "title": "備註說明",
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
                }).append(
                    $("<button/>", {
                        "id": row.seq_no,
                        "value": row.sale_id,
                        "name": row.c_product_id,
                        "class": "btn-in-table btn-green btn_list",
                        "title": "清單"
                    })
                    .append($("<i/>", {
                        "class": "fa fa-pencil-square-o"
                    })))));

                return options.html();
            }
        }]

    });
}

function draw_purchaseDetail(parameter) {
    $("#purchaseDetail_table").DataTable({
        dom: "lfr<t>ip",
        scrollY: "290px",
        width: '1200',
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
            url: "supply.do",
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
            "title": "採購數量",
            "data": "quantity",
            "defaultContent": ""
        }, {
            "title": "採購單價",
            "data": "cost",
            "defaultContent": ""
        }, {
            "title": "備註說明",
            "data": "memo",
            "defaultContent": ""
        }],
        columnDefs: [{
            "className": "dt-center",
            "targets": "_all"
        }]

    });
}
$(function() {
    $(".bdyplane").animate({
        "opacity": "1"
    });
    var information = {
        action: "search",

    };
    draw_supply(information);
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
    //使用jquery.validate來做驗證  注意事項: 1.不能用選擇器方式批次設定，它只取最後一個參數 2.要調用resetForm()而不是reset()
    var validator_insert = $("#insert-dialog-form-post").validate({
        rules: {
            supply_name: {
                maxlength: 40,
                required: true
            },
            supply_unicode: {
                maxlength: 10,
            },
            address: {
                stringMaxLength: 100
            },
            contact: {
                stringMaxLength: 10
            },
            phone: {
                stringMaxLength: 12
            },
            ext: {
                stringMaxLength: 6
            },
            mobile: {
                stringMaxLength: 15
            },
            contact1: {
                stringMaxLength: 10
            },
            phone1: {
                stringMaxLength: 12
            },
            ext1: {
                stringMaxLength: 6
            },
            mobile1: {
                stringMaxLength: 15
            },
            memo: {
                stringMaxLength: 200
            }
        }
    });
    var validator_update = $("#update-dialog-form-post").validate({
        rules: {
            supply_name: {
                maxlength: 40,
                required: true
            },
            supply_unicode: {
                maxlength: 10,
            },
            address: {
                stringMaxLength: 100
            },
            contact: {
                stringMaxLength: 10
            },
            phone: {
                stringMaxLength: 12
            },
            ext: {
                stringMaxLength: 6
            },
            mobile: {
                stringMaxLength: 15
            },
            contact1: {
                stringMaxLength: 10
            },
            phone1: {
                stringMaxLength: 12
            },
            ext1: {
                stringMaxLength: 6
            },
            mobile1: {
                stringMaxLength: 15
            },
            memo: {
                stringMaxLength: 200
            }
        }
    });
    //查詢相關設定
    //採購紀錄Dialog相關設定
    purchase_dialog = $("#dialog-form-purchase").dialog({
        draggable: true,
        resizable: false,
        autoOpen: false,
        height: "auto",
        width: "1200",
        modal: true,
        show: {
            effect: "blind",
            duration: 300
        },
        hide: {
            effect: "fade",
            duration: 300
        },
        buttons: [{
            text: "取消",
            click: function() {
                purchase_dialog.dialog("close");
                $("#purchase-dialog-form-post").trigger("reset");
            }
        }],
        close: function() {
            $("#purchase-dialog-form-post").trigger("reset");
        }
    });
    $("#dialog-form-purchase").show();

    //採購明細紀錄Dialog相關設定
    purchaseDetail_dialog = $("#dialog-form-purchaseDetail").dialog({
        draggable: true,
        resizable: false,
        autoOpen: false,
        height: "auto",
        width: "1200",
        modal: true,
        show: {
            effect: "blind",
            duration: 300
        },
        hide: {
            effect: "fade",
            duration: 300
        },
        buttons: [{
            text: "取消",
            click: function() {
                purchaseDetail_dialog.dialog("close");
                $("#purchaseDetail-dialog-form-post").trigger("reset");
            }
        }],
        close: function() {
            $("#purchaseDetail-dialog-form-post").trigger("reset");
        }
    });
    $("#dialog-form-purchaseDetail").show();

    //新增Dialog相關設定
    insert_dialog = $("#dialog-form-insert").dialog({
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
        buttons: [{
            id: "insert",
            text: "新增",
            click: function() {
                if ($('#insert-dialog-form-post').valid()) {
                    information = {
                        action: "insert",
                        supply_name: $("#dialog-form-insert input[name='supply_name']").val(),
                        supply_unicode: $("#dialog-form-insert input[name='supply_unicode']").val(),
                        address: $("#dialog-form-insert input[name='address']").val(),
                        contact: $("#dialog-form-insert input[name='contact']").val(),
                        phone: $("#dialog-form-insert input[name='phone']").val(),
                        ext: $("#dialog-form-insert input[name='ext']").val(),
                        mobile: $("#dialog-form-insert input[name='mobile']").val(),
                        contact1: $("#dialog-form-insert input[name='contact1']").val(),
                        phone1: $("#dialog-form-insert input[name='phone1']").val(),
                        ext1: $("#dialog-form-insert input[name='ext1']").val(),
                        email: $("#dialog-form-insert input[name='email']").val(),
                        email1: $("#dialog-form-insert input[name='email1']").val(),
                        mobile1: $("#dialog-form-insert input[name='mobile1']").val(),
                        memo: $("#dialog-form-insert textarea[name='memo']").val(),
                    };
                    draw_supply(information);
                    insert_dialog.dialog("close");
                    $("#insert-dialog-form-post").trigger("reset");
                }
            }
        }, {
            text: "取消",
            click: function() {
                validator_insert.resetForm();
                insert_dialog.dialog("close");
                $("#insert-dialog-form-post").trigger("reset");
            }
        }],
        close: function() {
            validator_insert.resetForm();
            $("#insert-dialog-form-post").trigger("reset");
        }
    });
    $("#dialog-form-insert").show();

    //確認Dialog相關設定(刪除功能)
    confirm_dialog = $("#dialog-confirm").dialog({
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
        buttons: {
            "確認刪除": function() {
                information = {
                    action: "delete",
                    supply_id: $(this).val()
                };
                draw_supply(information);
                $(this).dialog("close");
            },
            "取消刪除": function() {
                $(this).dialog("close");
            }
        }
    });
    $("#dialog-confirm").show();
    update_dialog = $("#dialog-form-update").dialog({
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
        buttons: [{
            text: "修改",
            click: function() {
                if ($('#update-dialog-form-post').valid()) {
                    information = {
                        action: "update",
                        supply_id: $(this).val(),
                        supply_name: $("#dialog-form-update input[name='supply_name']").val(),
                        supply_unicode: $("#dialog-form-update input[name='supply_unicode']").val(),
                        address: $("#dialog-form-update input[name='address']").val(),
                        contact: $("#dialog-form-update input[name='contact']").val(),
                        phone: $("#dialog-form-update input[name='phone']").val(),
                        ext: $("#dialog-form-update input[name='ext']").val(),
                        mobile: $("#dialog-form-update input[name='mobile']").val(),
                        contact1: $("#dialog-form-update input[name='contact1']").val(),
                        phone1: $("#dialog-form-update input[name='phone1']").val(),
                        ext1: $("#dialog-form-update input[name='ext1']").val(),
                        mobile1: $("#dialog-form-update input[name='mobile1']").val(),
                        email: $("#dialog-form-update input[name='email']").val(),
                        email1: $("#dialog-form-update input[name='email1']").val(),
                        memo: $("#dialog-form-update textarea[name='memo']").val(),
                    };
                    draw_supply(information);
                    update_dialog.dialog("close");
                    $("#update-dialog-form-post").trigger("reset");
                }
            }
        }, {
            text: "取消",
            click: function() {
                validator_update.resetForm();
                update_dialog.dialog("close");
                $("#update-dialog-form-post").trigger("reset");
            }
        }],
        close: function() {
            validator_update.resetForm();
            $("#update-dialog-form-post").trigger("reset");
        }
    });
    $("#dialog-form-update").show();
    //刪除事件聆聽 : 因為聆聽事件動態產生，所以採用delegate來批量處理，節省資源
    $("#supply_table").delegate(".btn_delete", "click", function() {
        var row = $(this).closest("tr");
        var data = $("#supply_table").DataTable().row(row).data();

        $("#dialog-confirm").html("<div class='delete_msg'>'" + data.supply_name + "'</div>");
        $("#dialog-confirm").val(data.supply_id);
        confirm_dialog.dialog("open");
    });

    //供應商採購紀錄
    $("#supply_table").delegate(".btn_list", "click", function() {
        var row = $(this).closest("tr");
        var data = $("#supply_table").DataTable().row(row).data();


        var parameter = {
            action: "getPurchaseRecordBySupplyId",
            supply_id: data.supply_id
        };
        draw_purchase(parameter);
        purchase_dialog.dialog("open");
    });

    //修改事件聆聽supply_table
    $("#purchase_table").delegate(".btn_list", "click", function(e) {
        e.preventDefault();
        var row = $(this).closest("tr");
        var data = $("#purchase_table").DataTable().row(row).data();
        var parameter = {
            action: "select_all_purchasedetail",
            purchase_id: data.purchase_id
        }
        draw_purchaseDetail(parameter)
        purchaseDetail_dialog.dialog("open");
    });



    //修改事件聆聽supply_table
    $("#supply_table").delegate(".btn_update", "click", function(e) {
        e.preventDefault();
        var row = $(this).closest("tr");
        var data = $("#supply_table").DataTable().row(row).data();
        $("#dialog-form-update").val(data.supply_id);
        $.ajax({
            type: "POST",
            url: "supply.do",
            data: {
                action: "search"
            },
            success: function(result) {
                var json_obj = $.parseJSON(result);
                var result_table = "";
                $.each(json_obj, function(i, item) {
                    if (json_obj[i].supply_id == $("#dialog-form-update").val()) {
                        $("#dialog-form-update input[name='supply_name']").val(json_obj[i].supply_name);
                        $("#dialog-form-update input[name='supply_unicode']").val(json_obj[i].supply_unicode);
                        $("#dialog-form-update input[name='address']").val(json_obj[i].address);
                        $("#dialog-form-update input[name='contact']").val(json_obj[i].contact);
                        $("#dialog-form-update input[name='phone']").val(json_obj[i].phone);
                        $("#dialog-form-update input[name='ext']").val(json_obj[i].ext);
                        $("#dialog-form-update input[name='mobile']").val(json_obj[i].mobile);
                        $("#dialog-form-update input[name='contact1']").val(json_obj[i].contact1);
                        $("#dialog-form-update input[name='phone1']").val(json_obj[i].phone1);
                        $("#dialog-form-update input[name='ext1']").val(json_obj[i].ext1);
                        $("#dialog-form-update input[name='mobile1']").val(json_obj[i].mobile1);
                        $("#dialog-form-update input[name='email']").val(json_obj[i].email);
                        $("#dialog-form-update input[name='email1']").val(json_obj[i].email1);
                        $("#dialog-form-update textarea[name='memo']").val(json_obj[i].memo);
                    }
                });
            }
        });

        update_dialog.dialog("open");
    });
    //新增事件聆聽
    $("#create-productunit").click(function() {
        insert_dialog.dialog("open");
    });
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
});
</script>
		<div class="datalistWrap">
			<!--對話窗樣式-確認 -->
			<div id="dialog-confirm" title="是否刪除此供應商?" style="display:none;"></div>
			<!--對話窗樣式-修改 -->
			<div id="dialog-form-update" title="修改資料" style="display:none;">
				<form name="update-dialog-form-post" id="update-dialog-form-post">
					<fieldset>
					<table class="form-table">
						<tbody>
							<tr>
							    <td>供應商名稱：</td><td><input type="text" name="supply_name"  placeholder="輸入供應商名稱"/>*</td>	
							</tr>
							<tr>
							    <td>供應商統一編號：</td><td><input type="text" name="supply_unicode" placeholder="輸入供應商統編"/></td>
							    <td>供應商地址：</td><td><input type="text" name="address" placeholder="輸入供應商地址"/></td>
							</tr>
							<tr>
								<td>連絡人：</td><td><input type="text" name="contact" placeholder="輸入連絡人"/></td>
							    <td>連絡人手機：</td><td><input type="text" name="mobile"placeholder="輸入連絡人手機"/></td>
							</tr>
							<tr>
								<td>連絡人電話：</td><td><input type="text" name="phone" placeholder="輸入連絡人電話"/></td>
							    <td>連絡人分機：</td><td><input type="text" name="ext" placeholder="輸入連絡人分機"/></td>
							</tr>
							<tr><td>連絡人email：</td><td><input type="text" name="email" placeholder="輸入連絡人email"/></td></tr>
							<tr>
							    <td>第二連絡人：</td><td><input type="text" name="contact1" placeholder="輸入第二連絡人"/></td>
								<td>第二連絡人手機：</td><td><input type="text" name="mobile1"placeholder="輸入第二連絡人手機"/></td>		  
							</tr>
							<tr>
							  <td>第二連絡人電話：</td><td><input type="text" name="phone1"placeholder="輸入第二連絡人電話"/></td>
							  <td>第二連絡人分機：</td><td><input type="text" name="ext1"placeholder="輸入第二連絡人分機"/></td>
							</tr>
							<tr><td>第二連絡人email：</td><td><input type="text" name="email1" placeholder="輸入第二連絡人email"/></td></tr>
							<tr>
								<td valign="top">備註說明：</td>
								<td colspan="3"><textarea rows="2" style="width:98%" name="memo" placeholder="輸入備註說明 "/></textarea></td>
							</tr>
<!-- 							<tr> -->
<!-- 								<td><input type="hidden" name="supply_id" disabled="disabled"/></td> -->
<!-- 							</tr> -->
							</tbody>
							</table>	
					</fieldset>
				</form>
			</div>			
			<!--對話窗樣式-新增 -->
			<div id="dialog-form-insert" title="新增供應商資料" style="display:none;">
				<form name="insert-dialog-form-post" id="insert-dialog-form-post"style="display:inline">
					<fieldset>
							<table class="form-table">
							<tbody>
							<tr>
							    <td>供應商名稱：</td><td><input type="text" name="supply_name"  placeholder="輸入供應商名稱"/>*</td>	
							</tr>
							<tr>
							    <td>供應商統一編號：</td><td><input type="text" name="supply_unicode" placeholder="輸入供應商統編"/></td>
							    <td>供應商地址：</td><td><input type="text" name="address" placeholder="輸入供應商地址"/></td>
							</tr>
							<tr>
								<td>連絡人：</td><td><input type="text" name="contact" placeholder="輸入連絡人"/></td>
							    <td>連絡人手機：</td><td><input type="text" name="mobile"placeholder="輸入連絡人手機"/></td>
							</tr>
							<tr>
								<td>連絡人電話：</td><td><input type="text" name="phone" placeholder="輸入連絡人電話"/></td>
							    <td>連絡人分機：</td><td><input type="text" name="ext" placeholder="輸入連絡人分機"/></td>
							</tr>
							<tr><td>連絡人email：</td><td><input type="text" name="email" placeholder="輸入連絡人email"/></td></tr>
							<tr>
							    <td>第二連絡人：</td><td><input type="text" name="contact1" placeholder="輸入第二連絡人"/></td>
								 <td>第二連絡人手機：</td><td><input type="text" name="mobile1"placeholder="輸入第二連絡人手機"/></td>		  
							</tr>
							<tr>
							  <td>第二連絡人電話：</td><td><input type="text" name="phone1"placeholder="輸入第二連絡人電話"/></td>
							  <td>第二連絡人分機：</td><td><input type="text" name="ext1"placeholder="輸入第二連絡人分機"/></td>
							</tr>
							<tr><td>第二連絡人email：</td><td><input type="text" name="email1" placeholder="輸入第二連絡人email"/></td></tr>
							<tr>
								<td valign="top">備註說明：</td>
								<td colspan="3"><textarea rows="2" style="width:98%" name="memo" placeholder="輸入備註說明 "/></textarea></td>
							</tr>
<!-- 							<tr> -->
<!-- 								<td><input type="hidden" name="supply_id" disabled="disabled"/></td> -->
<!-- 							</tr> -->
							</tbody>
						</table>	
					</fieldset>
				</form>
			</div>
			<!--對話窗樣式-採購紀錄-->
			<div id="dialog-form-purchase" title="供應商採購紀錄">
				<form name="purchase-dialog-form-post" id="purchase-dialog-form-post" style="display:inline">
					<table class="purchase_table" id="purchase_table"></table>	
				</form>
			</div>
			<!--對話窗樣式-採購紀錄明細-->
			<div id="dialog-form-purchaseDetail" title="供應商採購明細紀錄">
				<form name="purchaseDetail-dialog-form-post" id="purchaseDetail-dialog-form-post" style="display:inline">
					<table class="purchaseDetail_table" id="purchaseDetail_table"></table>	
				</form>
			</div>
			<!-- 第一列 -->
			<div class="input-field-wrap">
				<div class="form-wrap">
					<div class="btn-row">
						<button class="btn btn-exec btn-wide" id="create-productunit">新增供應商</button>
					</div>
				</div><!-- /.form-wrap -->
			</div>
			<!-- 第二列 -->
		
			<div class="row search-result-wrap" >
				<div id="products2-contain" class="ui-widget" >
					<table id="supply_table" class="result-table">
					</table>
					<input type="hidden" id=hidSupplyId value=''> 
				</div>
				<span class="validateTips"> </span>
			</div>
		</div>
	</div>
	</div>
</body>
</html>