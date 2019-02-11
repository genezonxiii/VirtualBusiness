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
<title>待出庫管理</title>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<link rel="Shortcut Icon" type="image/x-icon"
	href="./images/Rockettheme-Ecommerce-Shop.ico" />
<jsp:include page="template/common_css.jsp" flush="true" />
<link rel="stylesheet" href="css/buttons.dataTables.min.css">
<style>
.hidden_button {
	display: none !important;
}
</style>
</head>
<body>
	<input type="hidden" id="glb_menu" value='<%=menu%>' />
	<input type="hidden" id="glb_privilege" value="<%=privilege%>" />

	<div class="page-wrapper">
		<jsp:include page="template/common_headfoot.jsp" flush="true" />

		<div class="content-wrap">
			<h2 class="page-title">待出庫管理</h2>

			<div class="panel-content">
				<div class="datalistWrap">
					<div class="input-field-wrap">
						<div class="form-wrap">
						<h3>待出庫資料</h3>
						</div>
					</div>
				</div>
			</div>
			<div class="panel-content">
				<div class="datalistWrap">
					<div class="row search-result-wrap">
						<table id="dt_alloc_inv" class="result-table"></table>
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
			<div id="removeAllocInv" align="center" style="display:none">
				<form name="dialog_remove_alloc_inv" id="dialog_remove_alloc_inv">
					<label style="float: left;">訂單編號</label>
					<input type="text" name="remove_order_no" style="display: block">
				</form>
			</div>
		</div>
	</div>
	<div id = "dg_alloc_inv_info">
		<div class="row search-result-wrap">
			<table class="result-table"></table>
		</div>
	</div>
	<jsp:include page="template/common_js.jsp" flush="true" />
	<script type="text/javascript" src="js/dataTables.buttons.min.js"></script>
	<script type="text/javascript" src="js/buttons.jqueryui.min.js"></script>
	<script type="text/javascript">
	$(function(){

		var selectCount = 0;
		
		info_dialog = 
			$('#dg_alloc_inv_info').dialog({
				title : '待出貨摘要',
				autoOpen : false,
				width: '900px',
				position: { my: "center center", at: "center top+20%", of: window }
			});
		$("#dt_alloc_inv").DataTable({
			dom : "frB<t>ip",
			lengthChange: false,
			pageLength: 20,
			scrollY:"290px",
			width : 'auto',
			scrollCollapse : true,
			destroy : true,
			language : {
				"url" : "js/dataTables_zh-tw.txt",
				"emptyTable" : "查無資料",
			},
			initComplete: function(settings, json) {
			    $('div .dt-buttons').css({'float': 'left','margin-left':'10px'});
			    $('div .dt-buttons a').css('margin-left','10px');
			},
			ajax : {
				url : "allocInv.do",
				dataSrc : "",
				type : "POST",
				data : {
					action :"getAll"
				}
			},
			columns : [{
				"title" : "訂單編號",
				"data" : "order_no",
				"defaultContent" : ""
			},{
				"title" : "供應商",
				"data" : "supply_name",
				"defaultContent" : ""
			}, {
				"title" : "商品名稱",
				"data" : "product_name",
				"defaultContent" : ""
			},{
				"title" : "自訂商品編號",
				"data" : "c_product_id",
				"defaultContent" : ""
			}, {
				"title" : "數量",
				"data" : "quantity",
				"defaultContent" : ""
			}, {
				"title" : "分配數量",
				"data" : "alloc_qty",
				"defaultContent" : ""
			}],
			buttons : [ {
				text : '查看摘要',
				action : function(e, dt, node, config) {
					$('#dg_alloc_inv_info').find('table').DataTable({
						dom : "Bfr<t>ip",
						async: false,
						paging:  false,
						scrollY: "200px",
						height : 'auto',
						width : 'auto',
						scrollCollapse : true,
						destroy : true,
						language : {
							"url" : "js/dataTables_zh-tw.txt",
							"emptyTable" : "查無資料",
						},
						ajax : {
							url : "allocInv.do",
							dataSrc : "",
							type : "POST",
							data : {
								action :"getGroup"
							}
						},
						initComplete: function(settings, json) {
						    $('div .dt-buttons').css({'float': 'left','margin-left':'10px'});
						    $('div .dt-buttons a').css('margin-left','10px');
						},
						columns : [{
							"title" : "批次",
							"data" : null,
							"defaultContent" : ""
						},{
							"title" : "供應商",
							"data" : "supply_name",
							"defaultContent" : ""
						}, {
							"title" : "商品名稱",
							"data" : "product_name",
							"defaultContent" : ""
						},{
							"title" : "自訂商品編號",
							"data" : "c_product_id",
							"defaultContent" : ""
						}, {
							"title" : "數量",
							"data" : "quantity",
							"defaultContent" : ""
						}, {
							"title" : "分配數量",
							"data" : "alloc_qty",
							"defaultContent" : ""
						}],
						columnDefs : [ {
							targets : 0,
							searchable : false,
							orderable : false,
							render : function(data, type, row) {
								var product_id = row.product_id;

								var input = document.createElement("INPUT");
								input.type = 'checkbox';
								input.name = 'checkbox-group-select';
								input.id = product_id;

								var span = document.createElement("SPAN");
								span.className = 'form-label';

								var label = document.createElement("LABEL");
								label.htmlFor = product_id;
								label.name = 'checkbox-group-select';
								label.style.marginLeft = '35%';
								label.appendChild(span);

								var options = $("<div/>").append(input, label);

								return options.html();
							}
						} ],
						buttons : [ {
							text : '全選',
							action : function(e, dt, node, config) {
								selectCount++;
								var $table =  $('#dg_alloc_inv_info').find('table');
								var $checkboxs = $table.find('input[name=checkbox-group-select]');
								
								selectCount %2 != 1 ?
										$checkboxs.each(function() {
											$(this).prop("checked", false);
											$(this).removeClass("toggleon");
								        	$(this).closest("tr").removeClass("selected");
										}): 
										$checkboxs.each(function() {
											$(this).prop("checked", true);
											$(this).addClass("toggleon");
											$(this).closest("tr").addClass("selected");
										});						
							}
						},{
							text : '批次轉單(採購單)',
							action : function(e, dt, node, config) {
								var supplys = new Map();
								var $table =  $('#dg_alloc_inv_info').find('table');
								var $checkboxs = $table.find('input[name=checkbox-group-select]');
								var row;
								var data;
								var message = '';
							    
								$checkboxs.each(function(index) {
									if( $(this).prop("checked") ){
										row = $(this).closest("tr");
										data = $table.DataTable().row(row).data();
										supplys.set( data.supply_name, (index+1) );
									}									
								});
								if(supplys.size> 1){
									message = message.concat('以下為您所勾選的供應商↓<br><br>');
									var table = document.createElement('table');
									supplys.forEach(function(value, key, fullArray){
										var tr = document.createElement('tr');
										var text = document.createTextNode(key);
										tr.appendChild(text);
										table.appendChild(tr);
									});
									var $mes = $('#message #text');
									$mes.val('').html(message).append(table);
									$('#message')
										.dialog()
										.dialog('option', 'title', '警告訊息(只允許同一間供應商)')
										.dialog('option', 'width', '322.6px')
										.dialog('option', 'minHeight', 'auto')
										.dialog("open");
								}else{
									var allocInvs = [];
									var jsonList = '';
									$checkboxs.each(function(index) {
										if( $(this).prop("checked") ){
											row = $(this).closest("tr");
											data = $table.DataTable().row(row).data();
											allocInvs.push(data);
										}									
									});
									jsonList = JSON.stringify(allocInvs);
									console.log(jsonList);
									$.ajax({
										url: 'allocInv.do',
									 	type: 'post',
									 	data: {
									 		action: 'doPurchases',
									 		jsonList: jsonList
										},
									 	success: function (response) {
											var $mes = $('#message #text');
											$mes.val('').html('成功發送<br><br>執行結果為: '+response);
											$('#message')
												.dialog()
												.dialog('option', 'title', '提示訊息')
												.dialog('option', 'width', 'auto')
												.dialog('option', 'minHeight', 'auto')
												.dialog("open");									 		
									 	}
									 });
								}
							}
						}]});
					
					info_dialog.dialog('open');
				}
			}, {
				text : '刪除待出庫',
				enabled: false,
				className: 'hidden_button',
				action : function(e, dt, node, config) {
					$('#removeAllocInv').dialog({
						modal: true,
						width: "400px",
						open: function(event, ui) {
					        $(this).parent().children().children('.ui-dialog-titlebar-close').hide();
					    },
					    buttons : [{
					    	text: '確認刪除',
					    	click: function() {
					    		$.ajax({
					    			type : "POST",
					    			url : "allocInv.do",
					    			data : {
					    				action: 'removeAllocInv',
					    				order_no: $('input[type=text][name=remove_order_no]').val().trim()
					    			},
					    			success : function(result) {
					    				document.location.reload();
					    				$('#removeAllocInv').dialog('close');
					    			}
					    		})
					    	}
					    }, {
					    	text: '取消',
					    	click: function() {
					    		$('#removeAllocInv').dialog('close'); 
					    	}
					    }]
					})
					.dialog("open");
				}
			}, {
				text : '還原待出庫',
				enabled: false,
				className: 'hidden_button',
				action : function(e, dt, node, config) {
					$('#removeAllocInv').dialog({
						modal: true,
						width: "400px",
						open: function(event, ui) {
					        $(this).parent().children().children('.ui-dialog-titlebar-close').hide();
					    },
					    buttons : [{
					    	text: '確認還原',
					    	click: function() {
					    		$.ajax({
					    			type : "POST",
					    			url : "allocInv.do",
					    			data : {
					    				action: 'insertAllocInv',
					    				data: $('input[type=text][name=remove_order_no]').val().trim()
					    			},
					    			success : function(result) {
					    				document.location.reload();
					    				$('#removeAllocInv').dialog('close');
					    			}
					    		})
					    	}
					    }, {
					    	text: '取消',
					    	click: function() {
					    		$('#removeAllocInv').dialog('close'); 
					    	}
					    }]
					})
					.dialog("open");
				}
			}]
		});		
	});	
	</script>
</body>
</html>