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
<title>儲位管理</title>
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
.ui-autocomplete
    {
        position:absolute;
        cursor:default;
        z-index:4000 !important
    }
   
</style>
</head>
<body>
	<input type="hidden" id="glb_menu" value='<%=menu%>' />
	<input type="hidden" id="glb_privilege" value="<%=privilege%>" />

	<div class="page-wrapper">
		<jsp:include page="template/common_headfoot.jsp" flush="true" />

		<div class="content-wrap">
			<h2 class="page-title">儲位管理</h2>

			<div class="panel-content">
				<div class="datalistWrap">
					<div class="input-field-wrap">
						<div class="form-wrap">
							<div class="form-row">
								<form id="form_select">					
									<label for="">
										<span class="block-label">倉庫代碼</span>
											<input type="text" id="warehouse_code">
									</label>
									<button class="btn btn-darkblue">查詢</button>
								</form>
							</div>
							<div class="form-row">
								<form id="form_insert">
									<button class="btn btn-exec">新增儲位資料</button>
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
					<input type="hidden" id=hidwarehouse_code value=''>
					<input type="hidden" id=hidlocation_id value=''>
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
							<td><input type="text" name="dialog_v_warehouse_code" /></td>
							<td>倉庫名稱：</td>
							<td><input type="text" name="dialog_v_warehouse_name" disabled readonly /></td>
						</tr>
						<tr>
							<td>儲位編號：</td>
							<td><input type="text" name="dialog_location_code" /></td>
							<td>倉庫地點：</td>
							<td><input type="text" name="dialog_v_warehouse_locate" disabled readonly /></td>
						</tr>
						<tr>
							<td>儲位名稱：</td>
							<td><input type="text" name="dialog_location_desc" /></td>
						</tr>
						<tr>
							<td>備註：</td>
							<td><input type="text" name="dialog_location_memo" /></td>
						</tr>
					</tbody>
				</table>
			</fieldset>
		</form>
	</div>


	<!--對話窗樣式-新增- -->
	<div id="dialog-form-insert" title="新增資料"  style="display: none;">
		<form name="update-dialog-form-post" id="update-dialog-form-post"  style="display: inline">
			<fieldset>
				<table class="form-table">
					<tbody>
						<tr>
							<td>倉庫編號：</td>
							<td><input type="text" name="dialog_v_warehouse_code" id="test"/></td>
							<td>倉庫名稱：</td>
							<td><input type="text" name="dialog_v_warehouse_name" disabled readonly /></td>
						</tr>
						<tr>
							<td>儲位編號：</td>
							<td><input type="text" name="dialog_location_code" /></td>
							<td>倉庫地點：</td>
							<td><input type="text" name="dialog_v_warehouse_locate" disabled readonly /></td>
						</tr>
						<tr>
							<td>儲位名稱：</td>
							<td><input type="text" name="dialog_location_desc" /></td>
						</tr>
						<tr>
							<td>備註：</td>
							<td><input type="text" name="dialog_location_memo" /></td>
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
		
		
		$("#warehouse_code").autocomplete({
		    minLength: 0,
		    source: function(request, response) {
		        $.ajax({
		            url: "Location.do",
		            cache: false,
		            delay: 1500,
		            data: {
		                action: "getLocationByWarehouseCode",
		                warehouse_code: request.term,
		            },
		            success: function(data) {
		                var json_obj = $.parseJSON(data);
		                response($.map(json_obj, function(item) {
		                    return {
		                        label: item.warehouse_code+"-"+item.warehouse_name,
		                        value: item.warehouse_code,
		                        warehouse_id: item.warehouse_id,
		                    }
		                }))

		            }

		        });
		    },
		    change: function(event, ui) {
		        var source = $(this).val();
		        var temp = $(".ui-autocomplete li").map(function() {
		            return $(this).text()
		        }).get();

		        console.log("source: " + source);
		        console.log("temp: " + temp);
		        var found = $.inArray(source, temp);
		        console.log(found);

		    }
		});
	
		
		
		
		
		$("#dialog-form-insert  input[name='dialog_v_warehouse_code']").autocomplete({
		    minLength: 0,
		    source: function(request, response) {
		        $.ajax({
		            url: "Location.do",
		            cache: false,
		            delay: 1500,
		            data: {
		                action: "getLocationByWarehouseCode",
		                warehouse_code: request.term,
		            },
		            success: function(data) {
		                var json_obj = $.parseJSON(data);
		                response($.map(json_obj, function(item) {
		                    return {
		                        label: item.warehouse_code,
		                        value: item.warehouse_code,
		                        warehouse_id: item.warehouse_id,
		                        group_id: item.group_id,
		                        warehouse_code: item.warehouse_code,
		                        warehouse_name: item.warehouse_name,
		                        warehouse_locate: item.warehouse_locate
		                    }
		                }))

		            }

		        });
		    },
		    change: function(event, ui) {
		        var source = $(this).val();
		        var temp = $(".ui-autocomplete li").map(function() {
		            return $(this).text()
		        }).get();

		        console.log("source: " + source);
		        console.log("temp: " + temp);
		        var found = $.inArray(source, temp);
		        console.log(found);
		        if (found < 0) {
		            $(this).val('');
		            $(this).attr("placeholder", "輸入正確的倉庫編號!");
		        }
		    }
		});
		$("#dialog-form-insert  input[name='dialog_v_warehouse_code']").bind('focus', function() {
		    $(this).attr("placeholder", "輸入倉庫編號以供查詢");
		});
		$("#dialog-form-insert  input[name='dialog_v_warehouse_code']").bind('autocompleteselect', function(e, ui) {
		    $("#dialog-form-insert  input[name='dialog_v_warehouse_code']").val(ui.item.warehouse_code);
		    $("#dialog-form-insert  input[name='dialog_v_warehouse_name']").val(ui.item.warehouse_name);
		    $("#dialog-form-insert  input[name='dialog_v_warehouse_locate']").val(ui.item.warehouse_locate);
		});

		$("#dialog-form-update  input[name='dialog_v_warehouse_code']").autocomplete({
		    minLength: 0,
		    source: function(request, response) {
		        $.ajax({
		            url: "Location.do",
		            type: "POST",
		            cache: false,
		            delay: 1500,
		            data: {
		                action: "getLocationByWarehouseCode",
		                warehouse_code: request.term,
		            },
		            success: function(data) {
		                var json_obj = $.parseJSON(data);
		                response($.map(json_obj, function(item) {
		                    return {
		                        label: item.warehouse_code,
		                        value: item.warehouse_code,
		                        warehouse_id: item.warehouse_id,
		                        group_id: item.group_id,
		                        warehouse_code: item.warehouse_code,
		                        warehouse_name: item.warehouse_name,
		                        warehouse_locate: item.warehouse_locate
		                    }
		                }))

		            }

		        });
		    },
		    change: function(event, ui) {
		        var source = $(this).val();
		        console.log($(this));

		        var temp = $(".ui-autocomplete li").map(function() {
		            return $(this).text()
		        }).get();

		        console.log("source: " + source);
		        console.log("temp: " + temp);
		        var found = $.inArray(source, temp);
		        console.log(found);
		        if (found < 0) {
		            $(this).val('');
		            $(this).attr("placeholder", "輸入正確的倉庫編號!");
		        }
		    }
		});
		$("#dialog-form-update  input[name='dialog_v_warehouse_code']").bind('focus', function() {
		    $(this).attr("placeholder", "輸入倉庫編號以供查詢");
		});
		$("#dialog-form-update  input[name='dialog_v_warehouse_code']").bind('autocompleteselect', function(e, ui) {
		    $("#dialog-form-update  input[name='dialog_v_warehouse_code']").val(ui.item.warehouse_code);
		    $("#dialog-form-update  input[name='dialog_v_warehouse_name']").val(ui.item.warehouse_name);
		    $("#dialog-form-update  input[name='dialog_v_warehouse_locate']").val(ui.item.warehouse_locate);
		});
		
		$('#form_select').on("click", "button", function(e) {
			e.preventDefault();
			$("#hidwarehouse_code").val($("#warehouse_code").val());
			var parameter = {
				action : "getLocationVOListByWarehousecode",
				warehouse_code:$("#warehouse_code").val()
			};
			drawMasterTable(parameter);
		});
		
		$('#form_insert').on("click", "button", function(e) {
			e.preventDefault();
			initDialog();
			
			 var dialog_form_insert = $("#dialog-form-insert").dialog({
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
	                    id: "insert_enter",
	                    text: "新增",
	                    click: function() {
	                        $.ajax({
	                            url: 'Location.do',
	                            type: 'post',
	                            data: {
	                                action: 'insert_location',
	                                location_desc: $("#dialog-form-insert  input[name='dialog_location_desc']").val(),
	                                location_memo:  $("#dialog-form-insert  input[name='dialog_location_memo']").val(),
	                                location_code: $("#dialog-form-insert  input[name='dialog_location_code']").val(),
	                                v_warehouse_code: $("#dialog-form-insert  input[name='dialog_v_warehouse_code']").val(),
	                                location_id: $("#hidlocation_id").val()
	                            },
	                            error: function(xhr) {
	                                alere("error");
	                            },
	                            success: function(response) {
	                                if ("success" == response) {
	                        			var parameter = {
	                        				action : "getLocationVOListByWarehousecode",
	                        				warehouse_code:$("#hidwarehouse_code").val()
	                        			};
	                        			drawMasterTable(parameter);

	                                    massageDialog('執行成功', '新增');
	                                } else {
	                                    massageDialog('執行失敗  ' + response, '新增');
	                                }

	                            }
	                        });

	                        $(this).dialog("close");
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

            $("#dialog-form-update  input[name='dialog_location_code']").val(data.location_code);
            $("#dialog-form-update  input[name='dialog_location_desc']").val(data.location_desc);
            $("#dialog-form-update  input[name='dialog_v_warehouse_code']").val(data.v_warehouse_code);
            $("#dialog-form-update  input[name='dialog_v_warehouse_name']").val(data.v_warehouse_name);
            $("#dialog-form-update  input[name='dialog_v_warehouse_locate']").val(data.v_warehouse_locate);
            $("#dialog-form-update  input[name='dialog_location_memo']").val(data.location_memo);
            $("#hidlocation_id").val(data.location_id);


            var dialog_form_update = $("#dialog-form-update").dialog({
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
                    id: "update_enter",
                    text: "修改",
                    click: function() {
                        $.ajax({
                            url: 'Location.do',
                            type: 'post',
                            data: {
                                action: 'update_location',
                                location_code: $("#dialog-form-update  input[name='dialog_location_code']").val(),
                            	location_desc:$("#dialog-form-update  input[name='dialog_location_desc']").val(),
                            	v_warehouse_code:$("#dialog-form-update  input[name='dialog_v_warehouse_code']").val(),
                            	location_memo :$("#dialog-form-update  input[name='dialog_location_memo']").val(),
                            	location_id   :$("#hidlocation_id").val()
                            },
                            error: function(xhr) {
                                alere("error");
                            },
                            success: function(response) {
                                if ("success" == response) {

                        			var parameter = {
                        					action : "getLocationVOListByWarehousecode",
                        					warehouse_code:$("#hidwarehouse_code").val()
                        				};
                        				drawMasterTable(parameter);

                                    massageDialog('執行成功', '修改');
                                } else {
                                    massageDialog('執行失敗  ' + response, '修改');
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

	        },
	        ajax: {
	            url: "Location.do",
	            dataSrc: "locationVOList",
	            type: "POST",
	            delay: 1500,
	            data: parameter
	        },
	        columns: [
			{
			    "title": "儲位編號",
			    "data": "location_code",
			    "defaultContent": ""
			},{
			    "title": "儲位名稱",
			    "data": "location_desc",
			    "defaultContent": ""
			},{
	            "title": "倉庫編號",
	            "data": "v_warehouse_code",
	            "defaultContent": ""
	        },{
	            "title": "倉庫名稱",
	            "data": "v_warehouse_name",
	            "defaultContent": ""
	        },{
	            "title": "倉庫地點",
	            "data": "v_warehouse_locate",
	            "defaultContent": ""
	        },{
			    "title": "備註",
			    "data": "location_memo",
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

        $("#dialog-form-update  input[name='dialog_location_code']").val("");
        $("#dialog-form-update  input[name='dialog_location_desc']").val("");
        $("#dialog-form-update  input[name='dialog_v_warehouse_code']").val("");
        $("#dialog-form-update  input[name='dialog_v_warehouse_name']").val("");
        $("#dialog-form-update  input[name='dialog_v_warehouse_locate']").val("");
        $("#dialog-form-update  input[name='dialog_location_memo']").val("");
        
        $("#dialog-form-insert  input[name='dialog_location_code']").val("");
        $("#dialog-form-insert  input[name='dialog_location_desc']").val("");
        $("#dialog-form-insert  input[name='dialog_v_warehouse_code']").val("");
        $("#dialog-form-insert  input[name='dialog_v_warehouse_name']").val("");
        $("#dialog-form-insert  input[name='dialog_v_warehouse_locate']").val("");
        $("#dialog-form-insert  input[name='dialog_location_memo']").val("");
        $("#hidlocation").val("");
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




