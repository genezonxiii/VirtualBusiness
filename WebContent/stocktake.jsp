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
			<div id="dialog-form-update" title="新增盤點" style="display: none;">
				<form name="update-dialog-form-post" id="update-dialog-form-post" style="display: inline">
					<fieldset>
						<table class="form-table">
							<tbody>
								<tr>
									<td>備註：</td>
									<td><input type="text" id="dialog_c_product_id" name="dialog_c_product_id"/></td>
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
	    $('#create_master').on("click", "button", function(e) {
	        e.preventDefault();
	        
	        
			confirm_dialog = $("#dialog-form-update").dialog({
				draggable : true, resizable : false, autoOpen : false,
				height : "auto", width : "auto", modal : true,
				show : {effect : "blind",duration : 300},
				hide : {effect : "fade",duration : 300},
				open : function(event, ui) {$(this).parent().children().children('.ui-dialog-titlebar-close').hide();},
				buttons : {
					"確認新增" : function() {
						parameter={
							action : "masterDeleteByAccept_id",
							accept_id : data.accept_id
						};
					
						parameter = {
					            action: "searchByDate",
					            startDate: $("#hidStartDate").val(),
					            endDate: $("#hidEndDate").val()
					        };
						//關閉明細
					/*	$("#line").hide();
						$("#detail-table_wrapper").hide();
						$("#detail-table").hide();*/
						
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

	    
	    
	    
	    $('#dt_master').on('change', ':checkbox', function() {
	        $(this).is(":checked") ?
	            $(this).closest("tr").addClass("selected") :
	            $(this).closest("tr").removeClass("selected");
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
	            url: "Accept.do",
	            dataSrc: "",
	            type: "POST",
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
	            text: '轉庫存',
	            action: function(e, dt, node, config) {
					var $table =  $('#dt_master');

					var cells = $dtMaster.cells().nodes();

					var idArr = '';
					
					var $checkboxs = $(cells).find('input[name=checkbox-group-select]:checked');
					
					if($checkboxs.length == 0){
						alert('請至少選擇一筆資料');
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
							var $mes = $('#message #text');
							
							if(response=='success'){
								$mes.val('').html('轉入成功');
							}else{
								$mes.val('').html('轉入失敗');
							}
							
							$('#message')
							.dialog()
							.dialog('option', 'title', '提示訊息')
							.dialog('option', 'width', 'auto')
							.dialog('option', 'minHeight', 'auto')
							.dialog("open");
						}
					});		
					console.log('idArr: '+ idArr);		
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
            delay: 1500,
            data: parameter,
            success: function(data) {
            	var $mes = $('#message #text');
            	if(data=='success'){
					$mes.val('').html('刪除成功');

				}else{
					$mes.val('').html('刪除失敗');
				}
				
				$('#message')
				.dialog()
				.dialog('option', 'title', '提示訊息')
				.dialog('option', 'width', 'auto')
				.dialog('option', 'minHeight', 'auto')
				.dialog("open");
            	
            },
            error: function(XMLHttpRequest, textStatus, errorThrown) {
            		var $mes = $('#message #text');
					$mes.val('').html('刪除失敗');
					$('#message')
					.dialog()
					.dialog('option', 'title', '提示訊息')
					.dialog('option', 'width', 'auto')
					.dialog('option', 'minHeight', 'auto')
					.dialog("open");
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
						acceptDetail_id :  delete_value
					};
					deleteAjax(parameter);
					
					parameter = {
                      action: "getAcceptdetailVOListByAcceptId",
                      accept_id: $("#hidAccept_id").val()        
				    };
					drawDetailTable(parameter);
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




