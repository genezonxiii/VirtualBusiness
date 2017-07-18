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
<title>驗收管理</title>
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
#dt_master_wrapper{
		height:500px;
	
}
</style>
</head>
<body>
	<input type="hidden" id="glb_menu" value='<%=menu%>' />
	<input type="hidden" id="glb_privilege" value="<%=privilege%>" />

	<div class="page-wrapper">
		<jsp:include page="template/common_headfoot.jsp" flush="true" />

		<div class="content-wrap">
			<h2 class="page-title">驗收管理</h2>

			<div class="panel-content">
				<div class="datalistWrap">
					<div class="input-field-wrap">
						<div class="form-wrap">
							<div class="form-row">
								<form id="form_date">
									<label for=""> <span class="block-label">驗收產生日期起日</span> <input type="text" name="start_date" class='input-date'>
									</label>
									<div class='forward-mark'></div>
									<label for=""> <span class="block-label"> 驗收產生日期迄日 </span> <input type="text" name="end_date" class='input-date'>
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
						<table id="dt_master" class="result-table"></table>
						<hr class="hr0" style="padding-top: 50px;" id="line">
						<div id="msg_div"></div>

						<table id="detail-table" class="ui-widget ui-widget-content result-table"></table>
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

			<!--對話窗樣式-修改 -->
			<div id="dialog-form-update" title="驗收明細資料" style="display: none;">
				<form name="update-dialog-form-post" id="update-dialog-form-post" style="display: inline">
					<fieldset>
						<table class="form-table">
							<tbody>
								<tr>
									<td>產品編號：</td>
									<td><input type="text" id="dialog_c_product_id" name="dialog_c_product_id" disabled readonly /></td>
									<td>產品名稱：</td>
									<td><input type="text" id="dialog_product_name" name="dialog_product_name" disabled readonly /></td>
								</tr>
								<tr>
									<td>採購數量：</td>
									<td><input type="text" id="dialog_quantity" name="dialog_quantity" disabled readonly /></td>
									<td>驗收數量：</td>
									<td><input type="number" id="dialog_accept_qty" name="dialog_accept_qty" /></td>
								</tr>
								<tr>
									<td>倉庫編號：</td>
									<td><select id="dialog_warehouse_code" name="dialog_warehouse_code"></select></td>
									<td>倉庫名稱：</td>
									<td><select id="dialog_v_warehouse_name" name="dialog_v_warehouse_name" disabled readonly></select></td>
								</tr>
								<tr>
									<td>儲位編號：</td>
									<td><select id="dialog_v_location_code" name="dialog_v_location_code" /></select></td>
								</tr>
							</tbody>
						</table>
					</fieldset>
				</form>

				<input type="hidden" id=hidAcceptDetail_id value=''>
				<input type="hidden" id=hidAccept_id value=''>
				<input type="hidden" id=hidStartDate value=''>
				<input type="hidden" id=hidEndDate value=''>
			</div>
			
			<div id="dialog-confirm" title="刪除資料" style="display:none;">
				<p>是否確認刪除該筆資料</p>
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
		var selectCount = 0; //全選按鈕計算用
	</script>
	<script type="text/javascript">
	$(function() {
	    $('#form_date').on("click", "button", function(e) {
	        e.preventDefault();
	        
	        $("#line").hide();
		    $("#detail-table").hide();
		    $("#detail-table_wrapper").hide();
	        
	        
	        var $startDate = $('#form_date input:eq(0)').val();
	        var $endDate = $('#form_date input:eq(1)').val();
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
	        	dialogMsg("提示", errorMes);
	            return false;
	        }
	        
	        //紀錄執行的日期
	        $("#hidStartDate").val($startDate);
	        $("#hidEndDate").val($endDate);
	        var parameter = {
	            action: "searchByDate",
	            startDate: $startDate,
	            endDate: $endDate
	        };
	        console.log(parameter);
	        drawMasterTable(parameter);
	    });

	    $('#dt_master').on('change', ':checkbox', function() {
	        $(this).is(":checked") ?
	            $(this).closest("tr").addClass("selected") :
	            $(this).closest("tr").removeClass("selected");
	    });

	    $("#line").hide();
	    $("#detail-table_wrapper").hide();
	    $("#detail-table").hide();
	    
	    //修改事件
	    $("#detail-table").on("click", ".btn_update", function(e) {
	        e.preventDefault();
	        var row = $(this).closest("tr");
	        var data = $("#detail-table").DataTable().row(row).data();
	        
	        console.log("acceptDetail_id"+data.acceptDetail_id);	  
	        
	        $("#hidAcceptDetail_id").val(data.acceptDetail_id);
	        console.log("hidAcceptDetail_id_value"+$("#hidAcceptDetail_id").val());	  
	             			
	        $.ajax({
	            url: "Accept.do",
	            type: "POST",
	            cache: false,
	            delay: 1500,
	            data: {
	                action: "getAcceptDatailByAcceptDetail_id",
	                acceptDetail_id: data.acceptDetail_id,
	            },
	            success: function(data) {
	                var json_obj = $.parseJSON(data);
	                //先清空
	                $("#dialog_warehouse_code").empty();
	                $("#dialog_v_warehouse_name").empty();
	                $("#dialog_v_location_code").empty();
	                $.map(json_obj.warehouseVOList, function(item) {
	                    if (item.warehouse_code != '' && ('undefined' != typeof(item.warehouse_code))) {
	                        $("#dialog_warehouse_code").append("<option value='" + item.warehouse_code + "'>" + item.warehouse_code + "</option>");
	                    }

	                    if (item.warehouse_name != '' && ('undefined' != typeof(item.warehouse_name))) {
	                        $("#dialog_v_warehouse_name").append("<option value='" + item.warehouse_name + "'>" + item.warehouse_name + "</option>");
	                    }
	                });
					if(json_obj.v_location_code!='' && ('undefined' != typeof(json_obj.v_location_code))){
		                $.map(json_obj.locationVOList, function(item) {
		                    if (item.location_code != '' && ('undefined' != typeof(item.location_code))) {
		                        $("#dialog_v_location_code").append("<option value='" + item.location_id + "'>" + item.location_code + "</option>");
		                    }
		                });
					}
	                console.log("data:" + data);
	                //放置dialog value
	                $("#dialog_c_product_id").val(json_obj.c_product_id);
	                $("#dialog_product_name").val(json_obj.product_name);
	                $("#dialog_quantity").val(json_obj.quantity);
	                $("#dialog_accept_qty").val(json_obj.accept_qty);
	                $("#dialog_warehouse_code").val(json_obj.v_warehouse_code);
	                $("#dialog_v_warehouse_name").val(json_obj.v_warehouse_name);

	                console.log("v_location_code" + json_obj.v_location_code);
	                $("#dialog_v_location_code").val(json_obj.location_id);


	                //如果
	                if (('undefined' == typeof(json_obj.v_location_code)) || '' == json_obj.v_location_code) {
	                    $("#dialog_v_location_code")
	                        .append("<option value=''> 請先選擇倉庫編號 </option>");
	                }


	                //修改Dialog相關設定
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
	                    open: function(event, ui) {
	                        $(this).parent().children().children('.ui-dialog-titlebar-close').hide();
	                    },
	                    buttons: [{
	                        id: "update_enter",
	                        text: "修改",
	                        click: function() {

	                            $.ajax({
	                                url: "Accept.do",
	                                type: "POST",
	                                cache: false,
	                                delay: 1500,
	                                data: {
	                                    action: "upDateAcceptDetail",
	                                    acceptDetail_id: $("#hidAcceptDetail_id").val(),
	                                    accept_id: $("#hidAccept_id").val(),   
	                                    location_id: $("#dialog-form-update select[name='dialog_v_location_code']").val(),
	                                    accept_qty: $("#dialog_accept_qty").val()
	                                },
	                                success: function(data) {
	                                	
	                                    if (data == "success") {
	                                    	dialogMsg("驗收明細修改", "儲存成功");
		                                	
	                                        var parameter = {
	                                            "action": "getAcceptdetailVOListByAcceptId",
	                                            "accept_id": $("#hidAccept_id").val()
	                                        };
	                                        drawDetailTable(parameter)

	                                    } else {
	                                    	dialogMsg("驗收明細修改", "儲存失敗:" + data);
	                                    }
	                                },
	                                error: function(XMLHttpRequest, textStatus, errorThrown) {
	                                    dialogMsg("驗收明細修改", "發生錯誤, 儲存失敗");
	                                }
	                            })

	                            $("#update-dialog-form-post").trigger("reset");
	                            update_dialog.dialog("close");
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
	            },
	            error: function(XMLHttpRequest, textStatus, errorThrown) {
	                dialogMsg("錯誤", textStatus);
	            }
	        });

	        $("#dialog_warehouse_code").change(function() {
	            console.log("dialog_warehouse_code_change");

	            //驗證
	            var warehouseCode = $("#dialog_warehouse_code").val();
	            if (('' == warehouseCode) || ('undefined' == typeof(warehouseCode))) {
	                console.log("dialog_warehouse_code_return");
	                return;
	            }
	            console.log("warehouse_code:"+warehouseCode);

	            $.ajax({
	                url: "Accept.do",
	                type: "POST",
	                cache: false,
	                delay: 1500,
	                data: {
	                    action: "getDetailDialogDataByWarehouseCode",
	                    warehouse_code: warehouseCode,
	                },
	                success: function(data) {
	                    var json_obj = $.parseJSON(data);
	                    //先清空
	                    $("#dialog_v_warehouse_name").empty();
	                    $("#dialog_v_location_code").empty();

	                    $.map(json_obj.locationCodeStrList, function(item) {
	                        if (item.locationCodeStr != '' && ('undefined' != typeof(item.locationCodeStr))) {
	                            $("#dialog_v_location_code").append("<option value='" + item.location_id + "'>" + item.locationCodeStr + "</option>");
	                        }
	                    });
	                    if (json_obj.warehouseNameStr != '' && ('undefined' != typeof(json_obj.warehouseNameStr))) {
	                        $("#dialog_v_warehouse_name").append("<option value='" + json_obj.warehouseNameStr + "'>" + json_obj.warehouseNameStr + "</option>");
	                    }
	                },
	                error: function(XMLHttpRequest, textStatus, errorThrown) {
	                	dialogMsg("錯誤", textStatus);
	                }
	            });
	        });

	    })

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
	            
	            if(json.msg!=null){
	            	alert(json.msg);
	            }
	          
	           // alert(json);
	        },
	        ajax: {
	            url: "Accept.do",
	            dataSrc: "acceptVOList",
	            type: "POST",
	            delay: 1500,
	            data: parameter
	        },
	        columns: [{
	            "title": "勾選",
	            "data": null,
	            "defaultContent": ""
	        }, {
	            "title": "驗收編號",
	            "data": "seq_no",
	            "defaultContent": ""
	        }, {
	            "title": "採購編號",
	            "data": "v_seq_no",
	            "defaultContent": ""
	        }, {
	            "title": "驗收產生日期",
	            "data": "accept_date",
	            "defaultContent": ""
	        }, {
	            "title": "備註",
	            "data": "memo",
	            "defaultContent": ""
	        }, {
	            "title": "已轉庫存",
	            "data": "stock_flag",
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
	                var accept_id = row.accept_id;

	                var input = document.createElement("INPUT");
	                input.type = 'checkbox';
	                input.name = 'checkbox-group-select';
	                input.id = accept_id;

	                var span = document.createElement("SPAN");
	                span.className = 'form-label';

	                var label = document.createElement("LABEL");
	                label.htmlFor = accept_id;
	                label.name = 'checkbox-group-select';
	                label.style.marginLeft = '35%';
	                label.appendChild(span);

	                var options = $("<div/>").append(input, label);

	                return options.html();
	            }
	        }, {
	            //已轉庫存
	            targets: -2,
	            searchable: false,
	            orderable: false,
	            render: function(data, type, row) {
	            	var msg = row.stock_flag?"是":"否";
	            	return msg;
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
	                                    "value": row.accept_id,
	                                    "name": row.purchase_id,
	                                    "class": "btn-in-table btn-alert",
	                                    "title": "刪除"
	                                })
	                                .append($("<i/>", {
	                                    "class": "fa fa-trash"
	                                }))
	                            )
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
	            }
	        },{
	            text: '轉入庫存',
	            action: function(e, dt, node, config) {
	            	
	            	$("<div></div>").dialog({
	            		title: "轉入庫存",
		                modal: true,
		                open: function(event, ui) {
	                        $(this)
	                        	.html("轉入庫存前，請確認是否驗收完成。")
	                        	.parent().children().children('.ui-dialog-titlebar-close').hide();
	                    },
		                buttons: [{
		                    text: "確認",
		                    click: function() {
		                    	var $table =  $('#dt_master');

		    					var cells = $dtMaster.cells().nodes();

		    					var idArr = '';
		    					
		    					var $checkboxs = $(cells).find('input[name=checkbox-group-select]:checked');
		    					
		    					if($checkboxs.length == 0){
		    						dialogMsg("提示", "請至少選擇一筆資料");
		    						return false;
		    					}
		    				
		    					
		    					$checkboxs.each(function() {
		    						idArr += this.id + ',';
		    					});
		    					idArr = idArr.slice(0,-1);
		    					idArr = idArr.replace(/,/g,"','");
		    					idArr = "'" + idArr + "'";
		    					
		    					$.ajax({
		    						url: 'Accept.do',
		    						type: 'post',
		    						data: {
		    							action: 'importDataToStock',
		    							accept_ids: idArr
		    						},
		    						error: function (xhr) { },
		    						success: function (response) {
		    							var msg = "";
		    							
		    							if(response=='success'){
		    								msg = '轉入成功';
		    							}else{
		    								msg = '轉入失敗';
		    							}
		    							
		    							dialogMsg("轉入庫存", msg);
		    							
		    							parameter = {
		    					            action: "searchByDate",
		    					            startDate: $("#hidStartDate").val(),
		    					            endDate: $("#hidEndDate").val()
		    					        };
		    							drawMasterTable(parameter);
		    						}
		    					});
		    					console.log('idArr: '+ idArr);
		    					$(this).dialog("close");
		                    }
		                }, {
		                	text: "取消",
		                    click: function() {
		                    	$(this).dialog("close");
		                    }
		                }]
	            	});
				}
	        }]
	    });
	};
	
	function drawDetailTable(parameter){
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
	            url: "Accept.do",
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
	            "title": "驗收數量",
	            "data": "accept_qty",
	            "defaultContent": ""
	        }, {
	            "title": "儲位編號",
	            "data": "v_location_code",
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
	                                    "value": row.accept_id,
	                                	"name": row.acceptDetail_id,
	                                    "class": "btn-in-table btn-darkblue btn_update",
	                                    "title": "修改"
	                                })
	                                .append($("<i/>", {
	                                    "class": "fa fa-pencil"
	                                }))
	                            )
	                            .append(
	                                $("<button/>", {
	                              		"name": row.acceptDetail_id,
	                                    "class": "btn-in-table btn-alert",
	                                    "title": "刪除"
	                                })
	                                .append($("<i/>", {
	                                    "class": "fa fa-trash"
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

	//明細事件聆聽
	$("#dt_master").on("click", ".btn_detail", function(e) {
	    e.preventDefault();
	    $("#line").show();
	    $("#detail-table_wrapper").show();
	    $("#detail-table").show();

	    lookdown();
	    $("#detail_contain_row").css({
	        "opacity": "0"
	    });

	    var row = $(this).closest("tr");
	    var data = $("#dt_master").DataTable().row(row).data();
	    console.log("accept_id:" + data.accept_id);
	   

	    //需紀錄 accept_id 使後續dialog可以重新整理
	    $("#hidAccept_id").val(data.accept_id);
	    
	   var parameter ={
	                "action": "getAcceptdetailVOListByAcceptId",
	                "accept_id": data.accept_id
	             };
	   
	   drawDetailTable(parameter)
	});
	
	//刪除ajax
	function deleteAjax(parameter) {
		console.log("del_parameter:"+parameter);
		$.ajax({
            url: "Accept.do",
            type: "POST",
            cache: false,
            data: parameter,
            success: function(data) {
            	if(data=='success'){
            	  	dialogMsg("提示", '執行成功');
            	}else{
                    	dialogMsg("提示", data);
            	}
            },
            error: function(XMLHttpRequest, textStatus, errorThrown) {
            	dialogMsg("提示", '刪除失敗');
            }
        })
	}

	
	//刪除事件聆聽
	$("#dt_master").on("click", ".btn-alert", function(e) {
		 var row = $(this).closest("tr");
		 var data = $("#dt_master").DataTable().row(row).data();
		 console.log("accept_id:" + data.accept_id);
		 
		//確認Dialog相關設定(刪除功能)
		confirm_dialog = $("#dialog-confirm").dialog({
			draggable : true, resizable : false, autoOpen : false,
			height : "auto", width : "auto", modal : true,
			show : {effect : "blind",duration : 300},
			hide : {effect : "fade",duration : 300},
			open : function(event, ui) {$(this).parent().children().children('.ui-dialog-titlebar-close').hide();},
			buttons : {
				"確認刪除" : function() {
					parameter={
						action : "masterDeleteByAccept_id",
						accept_id : data.accept_id
					};
					deleteAjax(parameter);
					parameter = {
				            action: "searchByDate",
				            startDate: $("#hidStartDate").val(),
				            endDate: $("#hidEndDate").val()
				        };
					//關閉明細
					$("#line").hide();
					$("#detail-table_wrapper").hide();
					$("#detail-table").hide();
					
					drawMasterTable(parameter);
					$(this).dialog("close");
				},
				"取消刪除" : function() {
					$(this).dialog("close");
				}
			}
		});
		
		$("#dialog-confirm").dialog("open");
	});
	
	
	//刪除事件聆聽
	$("#detail-table").on("click", ".btn-alert", function(e) {
		var delete_value = $(this).attr("name");
		console.log("acceptDetail_id: "+delete_value);
		
		//確認Dialog相關設定(刪除功能)
		confirm_dialog = $("#dialog-confirm").dialog({
			draggable : true, resizable : false, autoOpen : false,
			height : "auto", width : "auto", modal : true,
			show : {effect : "blind",duration : 300},
			hide : {effect : "fade",duration : 300},
			open : function(event, ui) {$(this).parent().children().children('.ui-dialog-titlebar-close').hide();},
			buttons : {
				"確認刪除" : function() {
					parameter={
						action : "detailDeleteByAcceptDetail_id",
						accept_id: $("#hidAccept_id").val(),
						acceptDetail_id :  delete_value
					};
					deleteAjax(parameter);
					
					parameter = {
                      action: "getAcceptdetailVOListByAcceptId",
                      accept_id: $("#hidAccept_id").val()        
				    };
		
					setTimeout(function(){ drawDetailTable(parameter) 
						}, 500);
					$(this).dialog("close");
			
					
				},
				"取消刪除" : function() {
					$(this).dialog("close");
				}
			}
		});
		
		$("#dialog-confirm").dialog("open");
	});
	</script>

</body>


</html>




