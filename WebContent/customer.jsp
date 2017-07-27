<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ page import="tw.com.aber.basicinfo.*"%>
<%@ page import="java.util.*"%>
<%@ page import="java.sql.DriverManager"%>
<%@ page import="java.sql.Connection"%>
<%@ page import="java.sql.Statement"%>
<%@ page import="java.sql.ResultSet"%>
<jsp:directive.page import="java.sql.SQLException" />

<!DOCTYPE html>
<html>
<head>
<title>客戶管理</title>
<meta charset="utf-8">
<link rel="Shortcut Icon" type="image/x-icon" href="./images/Rockettheme-Ecommerce-Shop.ico" />
<link rel="stylesheet" href="css/styles.css" />
<link href="<c:url value="css/css.css" />" rel="stylesheet">
<link href="<c:url value="css/jquery.dataTables.min.css" />" rel="stylesheet">
<%-- <link href="<c:url value="css/jquery-ui.min.css" />" rel="stylesheet"> --%>
<!-- jquery-ui css要套這一版本，不然Dialog icon會有問題 -->
<link rel="stylesheet" href="css/1.11.4/jquery-ui.css">
</head>
<body>
	<jsp:include page="template.jsp" flush="true"/>
	<div class="content-wrap" >
		<div class='bdyplane' style="opacity:0">

<!-- jquery-ui js要套用這一版，不然Dialog會偏移，且容易當掉 -->
<script type="text/javascript" src="js/jquery-1.10.2.js"></script>
<script type="text/javascript" src="js/jquery.dataTables.min.js"></script>
<script type="text/javascript" src="js/jquery-ui.min.js"></script>
<script type="text/javascript" src="js/jquery-ui.js"></script>
<script type="text/javascript" src="js/jquery-migrate-1.4.1.min.js"></script>
<script type="text/javascript" src="js/jquery.validate.min.js"></script>
<script type="text/javascript" src="js/additional-methods.min.js"></script>
<script type="text/javascript" src="js/messages_zh_TW.min.js"></script>
<script>var first = 1;

function draw_customer(parameter) {
    $("#customer").DataTable({
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
            url: "customer.do",
            dataSrc: "",
            type: "POST",
            data: parameter
        },
        columns: [{
            "title": "客戶姓名",
            "data": "name",
            "defaultContent": ""
        }, {
            "title": "收貨地址",
            "data": "address",
            "defaultContent": ""
        }, {
            "title": "電話",
            "data": "phone",
            "defaultContent": ""
        }, {
            "title": "手機",
            "data": "mobile",
            "defaultContent": ""
        }, {
            "title": "Email",
            "data": "email",
            "defaultContent": ""
        }, {
            "title": "郵政編號",
            "data": "post",
            "defaultContent": ""
        }, {
            "title": "客戶等級",
            "data": "class",
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
                        }))
                    )

                    .append($("<button/>", {
                        "class": "btn-in-table btn-alert btn_delete",
                        "title": "刪除",
                    }).append($("<i/>", {
                        "class": "fa fa-trash"
                    })))));

                return options.html();
            }
        }]
    });
}

function draw_transaction_record(parameter) {
    $("#customerTransactionRecordTable").DataTable({
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
            url: "customer.do",
            dataSrc: "",
            type: "POST",
            data: parameter
        },
        columns: [{
            "title": "訂單編號",
            "data": "order_no",
            "defaultContent": ""
        }, {
            "title": "產品編號",
            "data": "c_product_id",
            "defaultContent": ""
        }, {
            "title": "產品名稱",
            "data": "product_name",
            "defaultContent": ""
        }, {
            "title": "交易日期",
            "data": "trans_list_date",
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
    var tmp = {
        action: "search"
    };
    draw_customer(tmp);
    var validator_insert = $("#insert-dialog-form-post").validate({
        rules: {
            name: {
                required: true
            },
            address: {
                required: true
            },
            phone: {
                required: true
            },
            mobile: {
                required: true
            },
            email: {
                email: true
            },
            post: {
                digits: true,
                maxlength: 5
            }
        }
    });
    var validator_update = $("#update-dialog-form-post").validate({
        rules: {
            name: {
                required: true
            },
            address: {
                required: true
            },
            phone: {
                required: true
            },
            mobile: {
                required: true
            },
            email: {
                email: true
            },
            post: {
                digits: true,
                maxlength: 5
            }
        }
    });
    //新增事件聆聽
    $("#create-customer").click(function(e) {
        e.preventDefault();
        insert_dialog.dialog("open");
    });

    //查詢
    $('#select_date').click(function(e) {
        e.preventDefault();
        console.log("select_date");
        var $customer_name = $('input[name=customer_name]').val();
        var parameter = {
            action: "getCustomerVOByName",
            customer_name: $customer_name
        };
        $("#hidCustomeName").val($customer_name);
        console.log(parameter);
        draw_customer(parameter);
    });

    // "新增" Dialog相關設定
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
                    var tmp = {
                        action: "insert",
                        name: $("#dialog-form-insert input[name='name']").val(),
                        address: $("#dialog-form-insert input[name='address']").val(),
                        phone: $("#dialog-form-insert input[name='phone']").val(),
                        mobile: $("#dialog-form-insert input[name='mobile']").val(),
                        email: $("#dialog-form-insert input[name='email']").val(),
                        post: $("#dialog-form-insert input[name='post']").val(),
                        customerClass: $("#dialog-form-insert input[name='class']").val(),
                        memo: $("#dialog-form-insert input[name='memo']").val()
                    };
                    draw_customer(tmp);
                    $("#insert-dialog-form-post").trigger("reset");
                    insert_dialog.dialog("close");
                }
            }
        }, {
            text: "取消",
            click: function() {
                validator_insert.resetForm();
                $("#insert-dialog-form-post").trigger("reset");
                insert_dialog.dialog("close");
            }
        }],
        close: function() {
            validator_insert.resetForm();
            $("#insert-dialog-form-post").trigger("reset");
        }
    });
    $("#dialog-form-insert").show();

    //交易紀錄清單
    $("#customer").delegate(".btn_list", "click", function(e) {
        e.preventDefault();
        var row = $(this).closest("tr");
        var data = $("#customer").DataTable().row(row).data();

        var parameter = {
            action: "transactionRecord",
            customer_id: data.customer_id
        };
        draw_transaction_record(parameter);
        customer_transaction_record_dialog.dialog("open");
    });

    customer_transaction_record_dialog = $("#dialog_form_customer_transaction_record").dialog({
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
                validator_update.resetForm();
                customer_transaction_record_dialog.dialog("close");
            }
        }],
        close: function() {
            $("#customer_transaction_form_post").trigger("reset");
        }
    });



    //修改事件聆聽
    $("#customer").delegate(".btn_update", "click", function(e) {
        e.preventDefault();
        var row = $(this).closest("tr");
        var data = $("#customer").DataTable().row(row).data();

        $("#dialog-form-update input[name='customer_id']").val(data.customer_id);
        $("#dialog-form-update input[name='name']").val(data.name);
        $("#dialog-form-update input[name='address']").val(data.address);
        $("#dialog-form-update input[name='phone']").val(data.phone);
        $("#dialog-form-update input[name='mobile']").val(data.mobile);
        $("#dialog-form-update input[name='email']").val(data.email);
        $("#dialog-form-update input[name='post']").val(data.post);
        $("#dialog-form-update input[name='class']").val(data.class);
        $("#dialog-form-update input[name='memo']").val(data.memo);
        update_dialog.dialog("open");
    });

    // "修改" Dialog相關設定
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
                    var tmp = {
                        action: "update",
                        customer_id: $("#dialog-form-update input[name='customer_id']").val(),
                        name: $("#dialog-form-update input[name='name']").val(),
                        address: $("#dialog-form-update input[name='address']").val(),
                        phone: $("#dialog-form-update input[name='phone']").val(),
                        mobile: $("#dialog-form-update input[name='mobile']").val(),
                        email: $("#dialog-form-update input[name='email']").val(),
                        post: $("#dialog-form-update input[name='post']").val(),
                        customerClass: $("#dialog-form-update input[name='class']").val(),
                        memo: $("#dialog-form-update input[name='memo']").val()
                    };
                    draw_customer(tmp);
                    update_dialog.dialog("close");
                    $("#update-dialog-form-post").trigger("reset");
                }
            }
        }, {
            text: "取消",
            click: function() {
                validator_update.resetForm();
                $("#update-dialog-form-post").trigger("reset");
                update_dialog.dialog("close");
            }
        }],
        close: function() {
            $("#update-dialog-form-post").trigger("reset");
            validator_update.resetForm();
        }
    });
    $("#dialog-form-update").show();

    //刪除事件聆聽 : 因為聆聽事件動態產生，所以採用delegate來批量處理，節省資源
    $("#customer").delegate(".btn_delete", "click", function() {
        var row = $(this).closest("tr");
        var data = $("#customer").DataTable().row(row).data();
    	
        $("#dialog-confirm").html("<div class='delete_msg'>" +data.name  + "</div>");
        $("#dialog-confirm").val(data.customer_id);
        $("#hidCustomerId").val(data.customer_id);

        confirm_dialog.dialog("open");
    });

    // "刪除" Dialog相關設定
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
                var tmp = {
                    action: "delete",
                    customer_id: $("#hidCustomerId").val()
                };
                draw_customer(tmp);
                $(this).dialog("close");
            },
            "取消刪除": function() {
                $(this).dialog("close");
            }
        }
    });
    $("#dialog-confirm").show();
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
	<div class="panel-content">
		<div class="datalistWrap">
			<!--對話窗樣式-確認 -->
			<div id="dialog-confirm" title="是否刪除此客戶資料?" style="display:none;">
				<p>是否確認刪除該筆資料</p>
			</div>		
			<!--對話窗樣式-修改 -->
			<div id="dialog-form-update" title="修改客戶資料" style="display:none;">
				<form name="update-dialog-form-post" id="update-dialog-form-post">
					<fieldset>
						<table class="form-table">
							<tbody>
								<tr>
									<td>客戶姓名：</td>
									<td><input type="text" name="name"  placeholder="輸入客戶姓名"></td>
									<td></td>
									<td>收貨地址：</td>
									<td><input type="text" name="address" placeholder="輸入收貨地址"></td>
								</tr>
								<tr>
									<td>電話：</td>
									<td><input type="text" name="phone"  placeholder="輸入電話"></td>
									<td></td>
									<td>手機：</td>
									<td><input type="text" name="mobile"  placeholder="輸入手機"></td>
								</tr>
								<tr>
									<td>Email：</td>
									<td><input type="text" name="email"  placeholder="輸入Email" ></td>
									<td></td>
									<td>郵政編號：</td>
									<td><input type="text" name="post"  placeholder="輸入郵政編號" ></td>
								</tr>
								<tr>
									<!-- 
									<td>客戶等級:&nbsp&nbsp</td>
									<td><input type="text" name="class"  placeholder="輸入客戶等級" ></td>
									<td></td>
									-->
									<td>備註：</td>
									<td><input type="text" name="memo"  placeholder="輸入備註說明" ></td>
								</tr>
							</tbody>
						</table>
						<input type="hidden" name="customer_id"> 
					</fieldset>
				</form>
			</div>					
			<!--對話窗樣式-新增 -->
			<div id="dialog-form-insert" title="新增客戶資料" style="display:none;">
				<form name="insert-dialog-form-post" id="insert-dialog-form-post" style="display:inline">
					<fieldset>
						<table class="form-table">
							<tbody>
								<tr>
									<td>客戶姓名：</td>
									<td><input type="text" name="name"  placeholder="輸入客戶姓名"></td>
									<td></td>
									<td>收貨地址：</td>
									<td><input type="text" name="address" placeholder="輸入收貨地址"></td>
								</tr>
								<tr>
									<td>電話：</td>
									<td><input type="text" name="phone"  placeholder="輸入電話"></td>
									<td></td>
									<td>手機：</td>
									<td><input type="text" name="mobile"  placeholder="輸入手機"></td>
								</tr>
								<tr>
									<td>Email：</td>
									<td><input type="text" name="email"  placeholder="輸入Email" ></td>
									<td></td>
									<td>郵政編號：</td>
									<td><input type="text" name="post"  placeholder="輸入郵政編號" ></td>
								</tr>
								<tr>
									<!-- 
									<td>客戶等級:&nbsp&nbsp</td>
									<td><input type="text" name="class"  placeholder="輸入客戶等級" ></td>
									<td></td>
									 -->
									<td>備註：</td>
									<td><input type="text" name="memo"  placeholder="輸入備註說明" ></td>
								</tr>
							</tbody>
						</table>
					</fieldset>
				</form>
			</div>	
			
			<!--對話窗樣式-交易紀錄 -->
			<div id="dialog_form_customer_transaction_record" title="客戶交易紀錄" style="display:none;">
				<form name="customer_transaction_form_post" id="customer_transaction_form_post" style="display:inline">
					<table id="customerTransactionRecordTable">
					</table>
				</form>
			</div>	
			<!-- 第一列 -->
			<input type="hidden" id="hidCustomeName" value=''>
			<input type="hidden" id="hidCustomerId" value=''>
			<div class="input-field-wrap">
				<div class="form-wrap">
					<div class="form-row">
						<label for=""> <span class="block-label">客戶姓名</span> <input
							type="text" name="customer_name">
						</label>
						<button class="btn btn-darkblue" id="select_date">查詢</button>
					</div>
				</div>
				<div class="form-wrap">
					<div class="btn-row">
						<button class="btn btn-exec btn-wide" id="create-customer">新增客戶資料</button>
					</div>
				</div>
			</div>

			<!-- 第二列 -->

			<div class="row search-result-wrap" align="center" id ="sales_contain_row">
				<div id="customer-contain" class="ui-widget" >
					<table id="customer" class="result-table">
					</table>
				</div>
				<span class="validateTips"> </span>
			</div>	
		</div>
	</div>
</div>
</div>
</body>
</html>