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
<title>倉庫管理</title>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<link rel="Shortcut Icon" type="image/x-icon" href="./images/Rockettheme-Ecommerce-Shop.ico" />
<jsp:include page="template/common_css.jsp" flush="true" />
<link rel="stylesheet" href="css/buttons.dataTables.min.css">
<style>
input[type="number"] {
	background: #efefef;
	border: 1px solid #999;
	font-size: 16px;
	padding: 5px;
	font-family: Arial, Helvetica, sans-serif;
}
.form-wrap .btn-exec{
	width:140px;
}

   
</style>
</head>
<body>
	<input type="hidden" id="glb_menu" value='<%=menu%>' />
	<input type="hidden" id="glb_privilege" value="<%=privilege%>" />

	<div class="page-wrapper">
		<jsp:include page="template/common_headfoot.jsp" flush="true" />

		<div class="content-wrap">
			<h2 class="page-title">倉庫管理</h2>

			<div class="panel-content">
				<div class="datalistWrap">
					<div class="input-field-wrap">
						<div class="form-wrap">
							<div class="form-row">
								<form id="form_select">
									<button class="btn btn-darkblue">查詢</button>
								</form>
							</div>
							<div class="form-row">
								<form id="form_insert">
									<button class="btn btn-exec">新增倉庫資料</button>
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
			<div id="message">
				<div id="text"></div>
			</div>
					<input type="hidden" id=hidwarehouse_id value=''>
			
		</div>
	</div>
	
	<!--對話窗樣式-修改- -->
	<div id="dialog-form-update" title="修改資料" style="display: none;">
		<form name="update-dialog-form-post" id="update-dialog-form-post" style="display: inline">
			<fieldset>
				<table class="form-table">
					<tbody>
						<tr>
							<td>倉庫編號：</td>
							<td><input type="text" name="dialog_warehouse_code" /></td>
						</tr>
						<tr>
							<td>倉庫名稱：</td>
							<td><input type="text" name="dialog_warehouse_name" /></td>
						</tr>
						<tr>
							<td>倉庫地點：</td>
							<td><input type="text" name="dialog_warehouse_locate" /></td>
						</tr>
						<tr>
							<td>倉庫序號：</td>
							<td><input type="text" name="dialog_warehouse_seqNo" /></td>
						</tr>
					</tbody>
				</table>
			</fieldset>
		</form>
	</div>


	<!--對話窗樣式-新增- -->
	<div id="dialog-form-insert" title="新增資料" style="display: none;">
		<form name="insert-dialog-form-post" id="insert-dialog-form-post" style="display: inline">
			<fieldset>
				<table class="form-table">
					<tbody>
						<tr>
							<td>倉庫編號：</td>
							<td><input type="text" name="dialog_warehouse_code" /></td>
						</tr>
						<tr>
							<td>倉庫名稱：</td>
							<td><input type="text" name="dialog_warehouse_name" /></td>
						</tr>
						<tr>
							<td>倉庫地點：</td>
							<td><input type="text" name="dialog_warehouse_locate" /></td>
						</tr>
						<tr>
							<td>倉庫序號：</td>
							<td><input type="text" name="dialog_warehouse_seqNo" /></td>
						</tr>
					</tbody>
				</table>
			</fieldset>
		</form>
	</div>



	<jsp:include page="template/common_js.jsp" flush="true" />
	<script type="text/javascript" src="js/dataTables.buttons.min.js"></script>
	<script type="text/javascript" src="js/buttons.jqueryui.min.js"></script>
	<script>
	$(function(){
		$('#form_select').on("click", "button", function(e) {
			e.preventDefault();
			var parameter = {
				action : "getAllWarehouseVOList",
			};
			drawMasterTable(parameter);
		});

		var oRules = {
			dialog_warehouse_seqNo : {
				digits : true,
				required : true,
				min : 1
			}
		};

		var validator_insert = $("#update-dialog-form-post").validate({
			rules : oRules
		});
		
		var validator_update = $("#insert-dialog-form-post").validate({
			rules : oRules
		});
		
		
		
		$('#form_insert').on("click", "button", function(e) {
			e.preventDefault();
			initDialog();
			
			 var dialog_form_insert = $("#dialog-form-insert").dialog({
	                draggable: true, //防止拖曳
	                resizable: false, //防止縮放
	                autoOpen: false,
	                height: "auto",
	                width: "500",
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
	                    id: "insert_enter",
	                    text: "新增",
	                    click: function() {
	                    	if($("#insert-dialog-form-post").valid()){
		                        $.ajax({
		                            url: 'Warehouse.do',
		                            type: 'post',
		                            data: {
		                                action: 'insert_warehouse',
		                                warehouse_code: $("#dialog-form-insert  input[name='dialog_warehouse_code']").val(),
		                                warehouse_name:  $("#dialog-form-insert  input[name='dialog_warehouse_name']").val(),
		                                warehouse_locate: $("#dialog-form-insert  input[name='dialog_warehouse_locate']").val(),
		                                warehouse_seqNo: $("#dialog-form-insert  input[name='dialog_warehouse_seqNo']").val()
		                            },
		                            error: function(xhr) {
		                                alere("error");
		                            },
		                            success: function(response) {
		                                if ("success" == response) {
	
		                                	var parameter = {
		                            				action : "getAllWarehouseVOList",
		                            			};
		                            			
		                            	drawMasterTable(parameter);
	
		                                    warningMsg('新增','執行成功');
		                                } else {
		                                	warningMsg('新增','執行失敗  ' + response);
		                                }
	
		                            }
		                        });
	
		                        $(this).dialog("close");
	                    	}
	                    }
	                }, {
	                    text: "取消",
	                    click: function() {
	                    	dialog_form_insert.dialog("close");
	                    }
	                }]
	            });

			 dialog_form_insert.dialog("open");
	        });
			
			
		});
		
		$("#dt_master").delegate(".btn_update", "click", function() {
			 initDialog();
            var row = $(this).closest("tr");
            var data = $("#dt_master").DataTable().row(row).data();
            $("#dialog-form-update  input[name='dialog_warehouse_code']").prop("disabled", true);
            $("#dialog-form-update  input[name='dialog_warehouse_code']").val(data.warehouse_code);
            $("#dialog-form-update  input[name='dialog_warehouse_name']").val(data.warehouse_name);
            $("#dialog-form-update  input[name='dialog_warehouse_locate']").val(data.warehouse_locate);
            $("#dialog-form-update  input[name='dialog_warehouse_seqNo']").val(data.seqNo);
            $("#hidwarehouse_id").val(data.warehouse_id);

            var dialog_form_update = $("#dialog-form-update").dialog({
                draggable: true, //防止拖曳
                resizable: false, //防止縮放
                autoOpen: false,
                height: "auto",
                width: "500",
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
                    	if($("#update-dialog-form-post").valid()){
	                        $.ajax({
	                            url: 'Warehouse.do',
	                            type: 'post',
	                            data: {
	                                action: 'update_warehouse',
	                                warehouse_id: $("#hidwarehouse_id").val(),
	                                warehouse_code: $("#dialog-form-update  input[name='dialog_warehouse_code']").val(),
	                                warehouse_name:  $("#dialog-form-update  input[name='dialog_warehouse_name']").val(),
	                                warehouse_locate: $("#dialog-form-update  input[name='dialog_warehouse_locate']").val(),
	                                warehouse_seqNo: $("#dialog-form-update  input[name='dialog_warehouse_seqNo']").val()
	                            },
	                            error: function(xhr) {
	                                alere("error");
	                            },
	                            success: function(response) {
	                                if ("success" == response) {
	
	                                	var parameter = {
	                            				action : "getAllWarehouseVOList",
	                            			};
	                            			
	                            	drawMasterTable(parameter);
	
	                                    warningMsg('修改','執行成功');
	                                } else {
	                                	warningMsg('修改','執行失敗  ' + response);
	                                }
	
	                            }
	                        });
	
	                        $(this).dialog("close");
                    	}
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

	
	function drawMasterTable(parameter) {

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
	            
	            /*if(json.msg!=null){
	            	alert(json.msg);
	            }*/
	          
	        },
	        ajax: {
	            url: "Warehouse.do",
	            dataSrc: "warehouseVOList",
	            type: "POST",
	            delay: 1500,
	            data: parameter
	        },
	        columns: [
	        {
	            "title": "倉庫編號",
	            "data": "warehouse_code",
	            "defaultContent": ""
	        }, {
	            "title": "倉庫名稱",
	            "data": "warehouse_name",
	            "defaultContent": ""
	        }, {
	            "title": "倉庫地點",
	            "data": "warehouse_locate",
	            "defaultContent": ""
	        },{
	            "title": "倉庫序號",
	            "data": "seqNo",
	            "defaultContent": ""
	        },{
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
	                                    "value": row.accept_id,
	                                    "name": row.purchase_id,
	                                    "class": "btn-in-table btn-darkblue btn_update",
	                                    "title": "修改"
	                                })
	                                .append($("<i/>", {
	                                    "class": "fa fa-pencil"
	                                }))
	                              
	                            )

	                        )
	                    );

	                return options.html();
	            }
	        },{
                "className": "dt-center",
                "targets": "_all"
            }]
	    });
	};
	
	
	
	
	
    function initDialog() {
        $("#dialog-form-update  input[name='dialog_warehouse_code']").val("");
        $("#dialog-form-update  input[name='dialog_warehouse_name']").val("");
        $("#dialog-form-update  input[name='dialog_warehouse_locate']").val("");
        $("#dialog-form-update  input[name='dialog_warehouse_seqNo']").val("");
        
        $("#dialog-form-insert  input[name='dialog_warehouse_code']").val("");
        $("#dialog-form-insert  input[name='dialog_warehouse_name']").val("");
        $("#dialog-form-insert  input[name='dialog_warehouse_locate']").val("");
        $("#dialog-form-insert  input[name='dialog_warehouse_seqNo']").val("");
        $("#hidwarehouse_id").val("");
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
	
	</script>
</body>


</html>




