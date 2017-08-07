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
<title>手開發票</title>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<link rel="Shortcut Icon" type="image/x-icon" href="./images/Rockettheme-Ecommerce-Shop.ico" />
<jsp:include page="template/common_css.jsp" flush="true" />
<link rel="stylesheet" href="css/buttons.dataTables.min.css">
</head>
<body>
	<input type="hidden" id="glb_menu" value='<%=menu%>' />
	<input type="hidden" id="glb_privilege" value="<%=privilege%>" />

	<div class="page-wrapper">
		<jsp:include page="template/common_headfoot.jsp" flush="true" />

		<div class="content-wrap">
			<h2 class="page-title">手開發票</h2>

			<div class="panel-content">
				<div class="datalistWrap">
					<div class="input-field-wrap">
						<div class="form-wrap">
							<div class="form-row">
								<form>
									<label for=""> <span class="block-label">發票起日</span>
										<input type="text" class="input-date">
									</label>
									<div class="forward-mark"></div>
									<label for=""> <span class="block-label">發票迄日</span>
										<input type="text" class="input-date">
									</label>
									<button class="btn btn-darkblue">查詢</button>
									<button class="btn btn-exec">新增發票</button>
								</form>
							</div>
						</div>
					</div>
					<div class="panel-content">
						<div class="datalistWrap">
							<div class="row search-result-wrap">
								<table id="invoice-master-table" class="result-table"></table>
								<table id="invoice-detail-table" class="result-table"></table>
							</div>
						</div>
					</div>					
				</div>
			</div>
		</div>
	</div>

	<div id="dialog-invoice" style="display:none">
		<form>
			<fieldset>
				<table class='form-table'>
					<tr>
						<td>買受人</td><td><input type="text" name="title"></td>
					</tr>
					<tr>
						<td>發票號碼</td><td><input type="text" name="invoice_no"></td>
					</tr>
					<tr>
						<td>發票類別</td>
						<td>
							<input id="invoice-type-radio-1" type="radio" name="invoice-type-radio-group">
							<label for="invoice-type-radio-1">
								<span class="form-label">二聯式</span>
							</label>
		          			<input id="invoice-type-radio-2" type="radio" name="invoice-type-radio-group">
		          			<label for="invoice-type-radio-2">
								<span class="form-label">三聯式</span>
		          			</label>
						</td>
					</tr>
					<tr>
						<td>統一編號</td><td><input type="text" name="unicode" placeholder="選擇三聯式方可輸入" disabled></td>
					</tr>
				</table>
			</fieldset>
		</form>
	</div>
	
	<div id="dialog-invoice-deatil" style="display:none">
		<form>
			<fieldset>
				<table class='form-table'>
					<tr>
						<td>品名</td><td><input type="text" name="description"></td>
					</tr>
					<tr>
						<td>單價</td><td><input type="text" name="price"></td>
					</tr>
					<tr>
						<td>數量</td><td><input type="text" name="quantity"></td>
					</tr>
					<tr>
						<td>小計</td><td><input type="text" name="subtotal" placeholder="系統自動產生" disabled></td>
					</tr>
				</table>
			</fieldset>
		</form>
	</div>
	<jsp:include page="template/common_js.jsp" flush="true" />
	<script type="text/javascript" src="js/dataTables.buttons.min.js"></script>
	<script type="text/javascript" src="js/buttons.jqueryui.min.js"></script>
	<script type="text/javascript" src="js/buttons.colVis.min.js"></script>

	<!-- Global Variables -->
	<script type="text/javascript">
		var $masterTable; //master datatable
		var $detailTable; //detail datatable
		var selectCount = 0; //全選按鈕計算用
		var inv_manual_id;
	</script>
	
	<!-- Listener -->
	<script type="text/javascript">
		//查詢
		$('.page-wrapper').on('click', '.btn-darkblue', function(event){
			event.preventDefault();
			
			var $form = $('form');
			var startDate = $form.find('input[type=text]:eq(0)').val();
			var endDate = $form.find('input[type=text]:eq(1)').val();
	
			var parameter = {
					action: 'query_invoice',
					startDate: startDate,
					endDate: endDate
			};
			drawInvMasTable(parameter);
		});
		
		//新增發票
		$('.page-wrapper').on('click', '.btn-exec', function(event){
			event.preventDefault();
			
			var $dialog = $('#dialog-invoice');

			$('input:radio[name="invoice-type-radio-group"]').change(
				function(){
					console.log(this);
				    if ($(this).is(':checked') && this.id == 'invoice-type-radio-2') {
				    	$dialog.find('input[name="unicode"]').prop("disabled", false);
				    }else{
				    	$dialog.find('input[name="unicode"]').val('');
				    	$dialog.find('input[name="unicode"]').prop("disabled", true);
				    }
				}
			);
			
			$dialog.dialog({
				draggable : true,
				resizable : false,
				height : "auto",
				width : "auto",
				modal : true,
				title : '新增發票',
				buttons : [{
							text : "儲存",
							click : function() {
								
								var invoice_type = $( "input[name='invoice-type-radio-group']:checked", $dialog ).attr("id");
								invoice_type = invoice_type.substring( invoice_type.length, invoice_type.length -1 );
								
								var title = $( "input[name='title']", $dialog ).val();
								var unicode = $( "input[name='unicode']", $dialog ).val();
								var invoice_no = $( "input[name='invoice_no']", $dialog ).val();
								
								console.log('invoice_type: '+ invoice_type);
								console.log('title: '+ title);
								console.log('unicode: '+ unicode);
								console.log('invoice_no: '+ invoice_no);
								
								$.ajax({
									url: 'InvManual.do',
									type: 'post',
									data : {
										action: 'insertMaster',
										invoice_type: invoice_type,
										title: title,
										unicode:  unicode,
										invoice_no: invoice_no
									},
									beforeSend: function(){
									    $(':hover').css('cursor','progress');
									},
									complete: function(){
										$(':hover').css('cursor','default');
									},
									success: function (response) {
										$masterTable.ajax.reload();
										$dialog.find('form').trigger("reset");
										$dialog.dialog("close");
									}
								});
							}
						}, {
							text : "取消",
							click : function() {
								$(this).dialog("close");
							}
						} ],
				close : function() {
					$dialog.find('form').trigger("reset");
				}
			});
		});

		//清單功能-刪除
		$('#invoice-detail-table').delegate(".btn_delete", "click", function(e) {
			e.preventDefault();
			
			var row = $(this).closest("tr");
		    var data = $detailTable.row(row).data();
		    var inv_manual_id = data.inv_manual_id;
		    var inv_manual_detail_id = data.inv_manual_detail_id;
		    
		    console.log(data);
		    
			var parameter = {
					action: 'delete_invoice_detail',
					inv_manual_id: inv_manual_id,
					inv_manual_detail_id: inv_manual_detail_id
			};
			
			$('<div/>').dialog({
				title: '警示訊息',
				draggable : true,
				resizable : false,
				height : "auto",
				width : "auto",
				modal : true,
				buttons : {
					"確認刪除" : function() {
						$.ajax({
							url: 'InvManual.do',
							async: false,
							type: 'post',
							data: parameter,
			                beforeSend: function(){
			                    $(':hover').css('cursor','progress');
			                },
			                complete: function(){
			                	$(':hover').css('cursor','default');
			                },
							success: function (response) {
								
								var result = 'OK' == response ? '刪除成功!': '刪除失敗!';

								$('<div/>').dialog({
									title: '提示訊息',
									draggable : true,
									resizable : false,
									modal : true
								}).append($('<p>', {text: result }));
							}
						});
						
						$detailTable.ajax.reload();
						
						$(this).dialog("close");
					},
					"取消刪除" : function() {
						$(this).dialog("close");
					}
				}
			}).text("確認刪除嗎?");

		});	

		//主單功能-刪除
		$('#invoice-master-table').delegate(".btn_delete", "click", function(e) {
			e.preventDefault();
			
			var row = $(this).closest("tr");
		    var data = $masterTable.row(row).data();
		    var inv_manual_id = data.inv_manual_id;
		    
		    console.log(data);
		    
			var parameter = {
					action: 'delete_invoice',
					inv_manual_id: inv_manual_id
			};
			
			$('<div/>').dialog({
				title: '警示訊息',
				draggable : true,
				resizable : false,
				height : "auto",
				width : "auto",
				modal : true,
				buttons : {
					"確認刪除" : function() {
						$.ajax({
							url: 'InvManual.do',
							async: false,
							type: 'post',
							data: parameter,
			                beforeSend: function(){
			                    $(':hover').css('cursor','progress');
			                },
			                complete: function(){
			                	$(':hover').css('cursor','default');
			                },
							success: function (response) {
								
								var result = 'OK' == response ? '刪除成功!': '刪除失敗!';

								$('<div/>').dialog({
									title: '提示訊息',
									draggable : true,
									resizable : false,
									modal : true
								}).append($('<p>', {text: result }));
							}
						});
						
						$masterTable.ajax.reload();
						
						$(this).dialog("close");
					},
					"取消刪除" : function() {
						$(this).dialog("close");
					}
				}
			}).text("確認刪除嗎?");

		});		
		
		//主單功能-清單
		$('#invoice-master-table').delegate(".btn_list", "click", function(e) {
			e.preventDefault();
			
			var row = $(this).closest("tr");
		    var data = $masterTable.row(row).data();
		    inv_manual_id = data.inv_manual_id;

			if ($masterTable instanceof $.fn.dataTable.Api) {
			    $masterTable.destroy();
			    $('#invoice-master-table').empty();
			}
			var parameter = {
					action: 'query_invoice_detail',
					inv_manual_id: inv_manual_id
			};
			drawInvListTable(parameter);
		});

	    $('#invoice-master-table').on('change', ':checkbox', function() {
	        $(this).is(":checked")?
	        	$(this).closest("tr").addClass("selected"):
	        	$(this).closest("tr").removeClass("selected");
	    });

	    $('#invoice-detail-table').on('change', ':checkbox', function() {
	        $(this).is(":checked")?
	        	$(this).closest("tr").addClass("selected"):
	        	$(this).closest("tr").removeClass("selected");
	    });
	    
	</script>
	
	<!-- Method -->
	<script type="text/javascript">
		function drawInvMasTable(parameter) {
			$masterTable = $("#invoice-master-table").DataTable({
			    dom: "frB<t>ip",
			    lengthChange: false,
			    pageLength: 20,
			    scrollY: "340px",
			    width: 'auto',
			    scrollCollapse: true,
			    destroy: true,
				language : {
					"url" : "js/dataTables_zh-tw.txt",
					"emptyTable" : "查無資料",
				},
			    initComplete: function(settings, json) {
			        $('div .dt-buttons').css({
			            'float': 'left',
			            'margin-left': '10px'
			        });
			        $('div .dt-buttons a').css('margin-left', '10px');
			    },
				ajax : {
					url : "InvManual.do",
					dataSrc : "",
					type : "POST",
					data : parameter,
                    beforeSend: function(){
                        $(':hover').css('cursor','progress');
                    },
                    complete: function(){
                    	$(':hover').css('cursor','default');
                    }
				},
				columns : [{
			        "title": "批次請求",
			        "data": null,
			        "defaultContent": ""
			    },{
					"title" : "發票類別",
					"data" : "invoice_type",
					"defaultContent" : ""
				},{
					"title" : "發票期別",
					"data" : "year_month",
					"defaultContent" : ""
				},{
					"title" : "發票號碼",
					"data" : "invoice_no",
					"defaultContent" : ""
				},{
					"title" : "發票日期",
					"data" : "invoice_date",
					"defaultContent" : ""
				},{
					"title" : "買受人",
					"data" : "title",
					"defaultContent" : ""
				},{
					"title" : "統一編號",
					"data" : "unicode",
					"defaultContent" : ""
				},{
					"title" : "總金額",
					"data" : "amount",
					"defaultContent" : ""
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
			        	
			        	var inv_manual_id = data.inv_manual_id;

			            var input = document.createElement("INPUT");
			            input.type = 'checkbox';
			            input.name = 'checkbox-inv-master-select';
			            input.id = inv_manual_id;

			            var span = document.createElement("SPAN");
			            span.className = 'form-label';

			            var label = document.createElement("LABEL");
			            label.htmlFor = inv_manual_id;
			            label.name = 'checkbox-inv-master-select';
			            label.style.marginLeft = '45%';
			            label.appendChild(span);

			            var options = $("<div/>").append(input, label);

			            return options.html();
			        }
			    },{
			        targets: -1,
			        searchable: false,
			        orderable: false,
			        render: function(data, type, row) {
			            var options = $("<div/>")
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
											"class": "btn-in-table btn-alert btn_delete",
											"title": "刪除"
										})
										.append( $("<i/>", {"class": "fa fa-trash"}) )
									)
									.append( 
										$("<button/>", {
											"class": "btn-in-table btn-green btn_list",
											"title": "清單"
										})
										.append( $("<i/>", {"class": "fa fa-pencil-square-o"}) )
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
		                var $checkboxs = $('#invoice-master-table input[name=checkbox-inv-master-select]');

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
		            text: '批次刪除',
		            action: function(e, dt, node, config) {

		                var $checkboxs = $('#invoice-master-table input[name=checkbox-inv-master-select]:checked');
		                
		                console.log($checkboxs)
		                
		                if ($checkboxs.length == 0) {
		                    alert('請至少選擇一筆資料');
		                    return false;
		                }
		                var inv_manual_ids = '';

		                $checkboxs.each(function() {
		                	inv_manual_ids += this.id + ',';
		                });
		                
		                inv_manual_ids = inv_manual_ids.slice(0, -1);
		    		    
		    			var parameter = {
		    					action: 'delete_invoice',
		    					inv_manual_id: inv_manual_ids
		    			};
		    		    
		    		    console.log(parameter);
		    		    
		    			$('<div/>').dialog({
		    				title: '警示訊息',
		    				draggable : true,
		    				resizable : false,
		    				height : "auto",
		    				width : "auto",
		    				modal : true,
		    				buttons : {
		    					"確認刪除" : function() {
		    						$.ajax({
		    							url: 'InvManual.do',
		    							async: false,
		    							type: 'post',
		    							data: parameter,
		    			                beforeSend: function(){
		    			                    $(':hover').css('cursor','progress');
		    			                },
		    			                complete: function(){
		    			                	$(':hover').css('cursor','default');
		    			                },
		    							success: function (response) {
		    								
		    								var result = 'OK' == response ? '刪除成功!': '刪除失敗!';

		    								$('<div/>').dialog({
		    									title: '提示訊息',
		    									draggable : true,
		    									resizable : false,
		    									modal : true
		    								}).append($('<p>', {text: result }));
		    							}
		    						});
		    						
		    						$masterTable.ajax.reload();
		    						
		    						$(this).dialog("close");
		    					},
		    					"取消刪除" : function() {
		    						$(this).dialog("close");
		    					}
		    				}
		    			}).text("確認刪除嗎?");
		            }
				}]
			});		
		};
		
		function drawInvListTable(parameter) {
			$detailTable = $("#invoice-detail-table").DataTable({
			    dom: "frB<t>ip",
			    lengthChange: false,
			    pageLength: 20,
			    scrollY: "340px",
			    width: 'auto',
			    scrollCollapse: true,
			    destroy: true,
				language : {
					"url" : "js/dataTables_zh-tw.txt",
					"emptyTable" : "查無資料",
				},
			    initComplete: function(settings, json) {
			        $('div .dt-buttons').css({
			            'float': 'left',
			            'margin-left': '10px'
			        });
			        $('div .dt-buttons a').css('margin-left', '10px');
			    },
				ajax : {
					url : "InvManual.do",
					dataSrc : "",
					type : "POST",
					data : parameter,
                    beforeSend: function(){
                        $(':hover').css('cursor','progress');
                    },
                    complete: function(){
                    	$(':hover').css('cursor','default');
                    }
				},
				columns : [{
			        "title": "批次請求",
			        "data": null,
			        "defaultContent": ""
			    },{
					"title" : "品名",
					"data" : "description",
					"defaultContent" : ""
				},{
					"title" : "單價",
					"data" : "price",
					"defaultContent" : ""
				},{
					"title" : "數量",
					"data" : "quantity",
					"defaultContent" : ""
				},{
					"title" : "小計",
					"data" : "subtotal",
					"defaultContent" : ""
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
			        	
			        	var inv_manual_detail_id = data.inv_manual_detail_id;

			            var input = document.createElement("INPUT");
			            input.type = 'checkbox';
			            input.name = 'checkbox-inv-detail-select';
			            input.id = inv_manual_detail_id;

			            var span = document.createElement("SPAN");
			            span.className = 'form-label';

			            var label = document.createElement("LABEL");
			            label.htmlFor = inv_manual_detail_id;
			            label.name = 'checkbox-inv-detail-select';
			            label.style.marginLeft = '45%';
			            label.appendChild(span);

			            var options = $("<div/>").append(input, label);

			            return options.html();
			        }
			    },{
			        targets: -1,
			        searchable: false,
			        orderable: false,
			        render: function(data, type, row) {
			            var options = $("<div/>")
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
											"class": "btn-in-table btn-alert btn_delete",
											"title": "刪除"
										})
										.append( $("<i/>", {"class": "fa fa-trash"}) )
									)
									.append( 
										$("<button/>", {
											"class": "btn-in-table btn-darkblue btn_update",
											"title": "修改"
										})
										.append( $("<i/>", {"class": "fa fa-pencil"}) )
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
		                var $checkboxs = $('#invoice-detail-table input[name=checkbox-inv-detail-select]');

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
		            text: '批次刪除',
		            action: function(e, dt, node, config) {

		                var $checkboxs = $('#invoice-detail-table input[name=checkbox-inv-detail-select]:checked');
		                
		                console.log($checkboxs)
		                
		                if ($checkboxs.length == 0) {
		                    alert('請至少選擇一筆資料');
		                    return false;
		                }
		                var inv_manual_detail_ids = '';

		                $checkboxs.each(function() {
		                	inv_manual_detail_ids += this.id + ',';
		                });
		                
		                inv_manual_detail_ids = inv_manual_detail_ids.slice(0, -1);
		    		    
		    			var parameter = {
		    					action: 'delete_invoice_detail',
		    					inv_manual_id: inv_manual_id,
		    					inv_manual_detail_id: inv_manual_detail_ids
		    			};
		    		    
		    		    console.log(parameter);
		    		    
		    			$('<div/>').dialog({
		    				title: '警示訊息',
		    				draggable : true,
		    				resizable : false,
		    				height : "auto",
		    				width : "auto",
		    				modal : true,
		    				buttons : {
		    					"確認刪除" : function() {
		    						$.ajax({
		    							url: 'InvManual.do',
		    							async: false,
		    							type: 'post',
		    							data: parameter,
		    			                beforeSend: function(){
		    			                    $(':hover').css('cursor','progress');
		    			                },
		    			                complete: function(){
		    			                	$(':hover').css('cursor','default');
		    			                },
		    							success: function (response) {
		    								
		    								var result = 'OK' == response ? '刪除成功!': '刪除失敗!';

		    								$('<div/>').dialog({
		    									title: '提示訊息',
		    									draggable : true,
		    									resizable : false,
		    									modal : true
		    								}).append($('<p>', {text: result }));
		    							}
		    						});
		    						
		    						$detailTable.ajax.reload();
		    						
		    						$(this).dialog("close");
		    					},
		    					"取消刪除" : function() {
		    						$(this).dialog("close");
		    					}
		    				}
		    			}).text("確認刪除嗎?");
		            }
				},{
		            text: '新增明細',
		            action: function(e, dt, node, config) {
		            	var $dialog = $('#dialog-invoice-deatil');
		            	
		            	var $price = $dialog.find('input[name=price]');
		            	var $quantity = $dialog.find('input[name=quantity]');
		            	var $description = $dialog.find('input[name=description]');
		            	var $subtotal = $dialog.find('input[name=subtotal]');


		            	$dialog.find('input[name=price],input[name=quantity]').change(function(){
		            		var subtotalVal = $price.val()* $quantity.val();
		            		
		            		subtotalVal = isNaN(subtotalVal) ? '資料錯誤，請檢查欄位': subtotalVal;

		            		$subtotal.val( subtotalVal );
		        		});
		            	
		    			$dialog.dialog({
		    				draggable : true,
		    				resizable : false,
		    				height : "auto",
		    				width : "auto",
		    				modal : true,
		    				title : '新增明細',
		    				buttons : [{
		    							text : "儲存",
		    							click : function() {

		    				            	if( $subtotal.val() == '資料錯誤，請檢查欄位' ){
		    				            		return false;
		    				            	}
		    				            	
		    								$.ajax({
		    									url: 'InvManual.do',
		    									async : false,
		    									type: 'post',
		    									data : {
		    										action: 'insertDetail',
		    										inv_manual_id: inv_manual_id,
		    										price: $price.val(),
		    										quantity: $quantity.val(),
		    										description: $description.val(),
		    										subtotal: $subtotal.val()
		    									},
		    									beforeSend: function(){
		    									    $(':hover').css('cursor','progress');
		    									},
		    									complete: function(){
		    										$(':hover').css('cursor','default');
		    									},
		    									success: function (response) {
		    										$masterTable.ajax.reload();
		    										$dialog.find('form').trigger("reset");
		    										$dialog.dialog("close");
		    									}
		    								});
		    								
		    								$detailTable.ajax.reload();
		    							}
		    						}, {
		    							text : "取消",
		    							click : function() {
		    								$(this).dialog("close");
		    							}
		    						} ],
		    				close : function() {
		    					$dialog.find('form').trigger("reset");
		    				}
		    			});		            	
		            }
				},{
		            text: '返回主單',
		            action: function(e, dt, node, config) {

		    			if ($detailTable instanceof $.fn.dataTable.Api) {
		    				$detailTable.destroy();
		    			    $('#invoice-detail-table').empty();
		    			}	 
		    		    
		    			var parameter = {
		    					action: 'query_invoice'
		    			};
		    			drawInvMasTable(parameter);
		            }
				}]
			});		
		};		
	</script>	
</body>
</html>