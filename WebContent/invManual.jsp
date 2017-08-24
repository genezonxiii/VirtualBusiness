<!-- 二聯式功能先註解起來，未來需要再開啟 -->	
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
								<div id = "masterTable">
									<table id="invoice-master-table" class="result-table"></table>
								</div>
								<div id = "detailTable">
									<table id="invoice-detail-table" class="result-table"></table>
								</div>
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
<!-- 					<tr> -->
<!-- 						<td>發票類別</td> -->
<!-- 						<td> -->
<!-- 							<input id="invoice-type-radio-1" type="radio" name="invoice-type-radio-group"> -->
<!-- 							<label for="invoice-type-radio-1"> -->
<!-- 								<span class="form-label">二聯式</span> -->
<!-- 							</label> -->
<!-- 		          			<input id="invoice-type-radio-2" type="radio" name="invoice-type-radio-group"> -->
<!-- 		          			<label for="invoice-type-radio-2"> -->
<!-- 								<span class="form-label">三聯式</span> -->
<!-- 		          			</label> -->
<!-- 		          			<div id = 'validate-invoice-type-radio'></div> -->
<!-- 						</td> -->
<!-- 					</tr> -->
					<tr>
<!-- 						<td>買受人</td><td><input type="text" name="title" placeholder="選擇三聯式方可輸入" disabled></td> -->
						<td>買受人</td><td><input type="text" name="title"></td>
					</tr>
					<tr>
<!-- 						<td>統一編號</td><td><input type="text" name="unicode" placeholder="選擇三聯式方可輸入" disabled></td> -->
						<td>統一編號</td><td><input type="text" name="unicode"></td>
					</tr>
					<tr>
						<td>地址</td><td><input type="text" name="address"></td>
					</tr>
					<tr>
						<td>備註</td><td><input type="text" name="memo"></td>
					</tr>
					<tr>
						<td>課稅別</td>
						<td>
							<input id="invoice-tax-type-radio-1" type="radio" name="invoice-tax-type-radio-group">
							<label for="invoice-tax-type-radio-1">
								<span class="form-label">應稅</span>
							</label>
		          			<input id="invoice-tax-type-radio-2" type="radio" name="invoice-tax-type-radio-group">
		          			<label for="invoice-tax-type-radio-2">
								<span class="form-label">零稅率</span>
		          			</label>
		          			<input id="invoice-tax-type-radio-3" type="radio" name="invoice-tax-type-radio-group">
		          			<label for="invoice-tax-type-radio-3">
								<span class="form-label">免稅</span>
		          			</label>
<!-- 		          			<input id="invoice-tax-type-radio-4" type="radio" name="invoice-tax-type-radio-group"> -->
<!-- 		          			<label for="invoice-tax-type-radio-4"> -->
<!-- 								<span class="form-label">應稅(特種稅率)</span> -->
<!-- 		          			</label> -->
<!-- 		          			<input id="invoice-tax-type-radio-9" type="radio" name="invoice-tax-type-radio-group"> -->
<!-- 		          			<label for="invoice-tax-type-radio-9"> -->
<!-- 								<span class="form-label">混合應稅與免稅或零稅率(限收銀機發票無法分辨時使用)</span> -->
<!-- 		          			</label> -->
		          			<div id = 'validate-invoice-tax-type-radio'></div>
						</td>
					</tr>
					<tr>
						<td>發票日期</td>
						<td>
							<input type="text" name="invoice_date" class="input-date">
						</td>
					</tr>
				</table>
			</fieldset>
		</form>
	</div>
	
	<div id="dialog-invoice-detail" style="display:none">
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
					<tr>
						<td>備註</td><td><input type="text" name="memo"></td>
					</tr>
				</table>
			</fieldset>
		</form>
	</div>
	<jsp:include page="template/common_js.jsp" flush="true" />
	<script type="text/javascript" src="js/dataTables.buttons.min.js"></script>
	<script type="text/javascript" src="js/buttons.jqueryui.min.js"></script>
	<script type="text/javascript" src="js/buttons.colVis.min.js"></script>

	<!-- Initialization -->
	<script type="text/javascript">
		$(function(){
			$("#dialog-invoice .input-date").datepicker("option", "minDate", new Date());
		});
	</script>
	
	<!-- Global Variables -->
	<script type="text/javascript">
		var $masterTable; //master datatable
		var $detailTable; //detail datatable
		var selectCount = 0; //全選按鈕計算用
		var inv_manual_id;
	</script>
	
	<!-- Jquery Validate -->
	<script type="text/javascript">

		$.extend(jQuery.validator.messages, {
		    required: "必填欄位"
		});
		
		var validator_insert_invoice = $('#dialog-invoice').find('form').validate({
	        rules: {
	            'unicode': {
	                required: true
	            },
	            'title': {
	                required: true
	            },
	            'invoice_no': {
	                required: true
	            },
// 	            'invoice-type-radio-group': {
// 	                required: true
// 	            },
	            'invoice_date': {
	                required: true
	            },
	            'invoice-tax-type-radio-group': {
	                required: true
	            }
	        },
	        errorPlacement: function(error, element) {
	        	error.insertAfter(element.closest("td"));
	    	}
	    });
		
		var validator_insert_invoice_detail = $('#dialog-invoice-detail').find('form').validate({
	        rules: {
	        	'description': {
	                required: true
	            },
	            'price': {
	                required: true,
	                number:true
	            },
	            'quantity': {
	                required: true,
	                digits:true
	            }
	        }
	    });		
	</script>
	<!-- Listener -->
	<script type="text/javascript">

		//查詢
		$('.input-field-wrap').on('click', '.btn-darkblue', function(event){
			event.preventDefault();

			if ($detailTable instanceof $.fn.dataTable.Api) {
// 				$detailTable.destroy();
// 			    $('#invoice-detail-table').empty();
			    $('#detailTable').hide();
			}
			
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
		$('.input-field-wrap').on('click', '.btn-exec', function(event){
			event.preventDefault();
			
			var $dialog = $('#dialog-invoice');
		
// 			$('input:radio[name="invoice-type-radio-group"]').change(
// 				function(){
// 				    if ($(this).is(':checked') && this.id == 'invoice-type-radio-2') {
// 				    	$dialog.find('input[name="unicode"]').prop("disabled", false);
// 				    	$dialog.find('input[name="title"]').prop("disabled", false);
				    	
// 				    	$dialog.find('input[name="title"]').rules("add", "required");
// 						$dialog.find('input[name="unicode"]').rules("add", "required");
// 				    }else{
// 				    	$dialog.find('input[name="unicode"]').val('');
// 				    	$dialog.find('input[name="title"]').val('');
// 				    	$dialog.find('input[name="unicode"]').prop("disabled", true);
// 				    	$dialog.find('input[name="title"]').prop("disabled", true);
				    	
// 				    	$dialog.find('input[name="title"]').rules('remove', 'required');
// 				    	$dialog.find('input[name="unicode"]').rules('remove', 'required');
// 				    }
// 				}
// 			);
			
			$dialog.dialog({
				draggable : true,
				resizable : false,
				height : "auto",
				width : "500px",
				modal : true,
				title : '新增發票',
				buttons : [{
							text : "儲存",
							click : function() {
								
								if($('#dialog-invoice').find('form').valid()){
// 									var invoice_type = $( "input[name='invoice-type-radio-group']:checked", $dialog ).attr("id");
// 									invoice_type = invoice_type.substring( invoice_type.length, invoice_type.length -1 );
									invoice_type ='2';
									
									var tax_type = $( "input[name='invoice-tax-type-radio-group']:checked", $dialog ).attr("id");
									tax_type = tax_type.substring( tax_type.length, tax_type.length -1 );
									
									var title = $( "input[name='title']", $dialog ).val();
									var unicode = $( "input[name='unicode']", $dialog ).val();
									var address = $( "input[name='address']", $dialog ).val();
									var memo = $( "input[name='memo']", $dialog ).val();
									var invoice_date = $( "input[name='invoice_date']", $dialog ).val();
									
									console.log('invoice_type: '+ invoice_type);
									console.log('tax_type: '+ tax_type);
									console.log('title: '+ title);
									console.log('unicode: '+ unicode);
									console.log('invoice_date: '+ invoice_date);
									
									$.ajax({
										url: 'InvManual.do',
										type: 'post',
										data : {
											action: 'insertMaster',
											invoice_type: invoice_type,
											title: title,
											unicode:  unicode,
											address: address,
											memo: memo,
											invoice_date: invoice_date,
											tax_type: tax_type
										},
										beforeSend: function(){
										    $(':hover').css('cursor','progress');
										},
										complete: function(){
											$(':hover').css('cursor','default');
										},
										success: function (response) {
											var text = '新增失敗';
											if(response == 'OK'){
												if ($masterTable instanceof $.fn.dataTable.Api) {
													$masterTable.ajax.reload();
												}
												text = '新增成功';
											}
											$dialog.find('form').trigger("reset");
											$dialog.dialog("close");
											$('<div/>').dialog({
												title: '提示訊息',
												draggable : true,
												resizable : false,
												width : "140px",
												modal : true
											}).text(text);
										}
									});
								}
							}
						}, {
							text : "取消",
							click : function() {
								validator_insert_invoice.resetForm();
								$(this).dialog("close");
							}
						} ],
			    beforeClose: function() {
					validator_insert_invoice.resetForm();
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
									width : "140px",
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

		//清單功能-修改
		$('#invoice-detail-table').delegate(".btn_update", "click", function(e) {
			e.preventDefault();

			var row = $(this).closest("tr");
		    var data = $detailTable.row(row).data();
		    var inv_manual_detail_id = data.inv_manual_detail_id;
		    var inv_manual_id = data.inv_manual_id;
		    
        	var $dialog = $('#dialog-invoice-detail');
        	
        	var $price = $dialog.find('input[name=price]');
        	var $quantity = $dialog.find('input[name=quantity]');
        	var $description = $dialog.find('input[name=description]');
        	var $subtotal = $dialog.find('input[name=subtotal]');
        	var $memo = $dialog.find('input[name=memo]');

        	$price.val(data.price);
			$quantity.val(data.quantity);
			$description.val(data.description);
			$subtotal.val(data.subtotal);
			$memo.val(data.memo);
			
        	$dialog.find('input[name=price],input[name=quantity]').change(function(){
        		var subtotalVal = $price.val()* $quantity.val();
        		
        		subtotalVal = isNaN(subtotalVal) ? '資料錯誤，請檢查欄位': subtotalVal;

        		$subtotal.val( subtotalVal );
    		});
        	console.log('data');
        	console.log(data);
			$dialog.dialog({
				draggable : true,
				resizable : false,
				height : "auto",
				width : "auto",
				modal : true,
				title : '修改明細',
				buttons : [{
							text : "修改",
							click : function() {

				            	if( $subtotal.val() == '資料錯誤，請檢查欄位' ){
				            		return false;
				            	}

								if($('#dialog-invoice-detail').find('form').valid()){
									$.ajax({
										url: 'InvManual.do',
										async : false,
										type: 'post',
										data : {
											action: 'updateDetail',
											inv_manual_detail_id: inv_manual_detail_id,
											inv_manual_id: inv_manual_id,
											price: $price.val(),
											quantity: $quantity.val(),
											description: $description.val(),
											subtotal: $subtotal.val(),
											memo: $memo.val()
										},
										beforeSend: function(){
										    $(':hover').css('cursor','progress');
										},
										complete: function(){
											$(':hover').css('cursor','default');
										},
										success: function (response) {
											var text = '修改失敗';
											if(response == 'OK'){
												if ($detailTable instanceof $.fn.dataTable.Api) {
													$detailTable.ajax.reload();
												}
												text = '修改成功';
											}
											$dialog.find('form').trigger("reset");
											$dialog.dialog("close");
											$('<div/>').dialog({
												title: '提示訊息',
												draggable : true,
												resizable : false,
												width : "140px",
												modal : true
											}).text(text);
										}
									});
								}
								}
						}, {
							text : "取消",
							click : function() {
								$(this).dialog("close");
							}
						} ],
			    beforeClose: function() {
			    	validator_insert_invoice_detail.resetForm();
					$dialog.find('form').trigger("reset");
			    }
			});	
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
									width : "140px",
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
// 			    $masterTable.destroy();
// 			    $('#invoice-master-table').empty();
			    $('#masterTable').hide();
			    $('#detailTable').show();
			}
			var parameter = {
					action: 'query_invoice_detail',
					inv_manual_id: inv_manual_id
			};
			drawInvListTable(parameter);
		});

		//主單功能-修改
		$('#invoice-master-table').delegate(".btn_update", "click", function(e) {
			event.preventDefault();

			var row = $(this).closest("tr");
		    var data = $masterTable.row(row).data();
		    inv_manual_id = data.inv_manual_id;
		    
			var $dialog = $('#dialog-invoice');
			$dialog.find('input[name=invoice_date]').val(data.invoice_date);
			$dialog.find('input[name=title]').val(data.title);
			$dialog.find('input[name=invoice_no]').val(data.invoice_no);
			$dialog.find('input[name=unicode]').val(data.unicode);
			$dialog.find('input[name=address]').val(data.address);
			$dialog.find('input[name=memo]').val(data.memo);
// 			$( '#invoice-type-radio-'+ data.invoice_type ).prop("checked", true);
			$( '#invoice-tax-type-radio-'+ data.tax_type ).prop("checked", true);
			
// 			$('input:radio[name="invoice-type-radio-group"]').change(
// 				function(){
// 				    if ($(this).is(':checked') && this.id == 'invoice-type-radio-2') {
// 				    	$dialog.find('input[name="unicode"]').prop("disabled", false);
// 				    	$dialog.find('input[name="title"]').prop("disabled", false);
				    	
// 				    	$dialog.find('input[name="title"]').rules("add", "required");
// 						$dialog.find('input[name="unicode"]').rules("add", "required");
// 				    }else{
// 				    	$dialog.find('input[name="unicode"]').val('');
// 				    	$dialog.find('input[name="title"]').val('');
// 				    	$dialog.find('input[name="unicode"]').prop("disabled", true);
// 				    	$dialog.find('input[name="title"]').prop("disabled", true);
				    	
// 				    	$dialog.find('input[name="title"]').rules('remove', 'required');
// 				    	$dialog.find('input[name="unicode"]').rules('remove', 'required');
// 				    }
// 				}
// 			);
			
			$dialog.dialog({
				draggable : true,
				resizable : false,
				height : "auto",
				width : "auto",
				modal : true,
				title : '修改發票',
				buttons : [{
							text : "儲存",
							click : function() {

								if($('#dialog-invoice').find('form').valid()){
// 									var invoice_type = $( "input[name='invoice-type-radio-group']:checked", $dialog ).attr("id");
// 									invoice_type = invoice_type.substring( invoice_type.length, invoice_type.length -1 );
									invoice_type = '2';
									
									var tax_type = $( "input[name='invoice-tax-type-radio-group']:checked", $dialog ).attr("id");
									tax_type = tax_type.substring( tax_type.length, tax_type.length -1 );
									
									var title = $( "input[name='title']", $dialog ).val();
									var unicode = $( "input[name='unicode']", $dialog ).val();
									var address = $( "input[name='address']", $dialog ).val();
									var memo = $( "input[name='memo']", $dialog ).val();
									var invoice_no = $( "input[name='invoice_no']", $dialog ).val();
									var invoice_date = $( "input[name='invoice_date']", $dialog ).val();
									
									console.log('invoice_type: '+ invoice_type);
									console.log('title: '+ title);
									console.log('unicode: '+ unicode);
									console.log('invoice_no: '+ invoice_no);
									
									$.ajax({
										url: 'InvManual.do',
										type: 'post',
										data : {
											action: 'updateMaster',
											inv_manual_id: inv_manual_id,
											invoice_type: invoice_type,
											title: title,
											unicode:  unicode,
											address: address,
											memo: memo,
											invoice_no: invoice_no,
											invoice_date: invoice_date,
											tax_type: tax_type
										},
										beforeSend: function(){
										    $(':hover').css('cursor','progress');
										},
										complete: function(){
											$(':hover').css('cursor','default');
										},
										success: function (response) {
											var text = '修改失敗';
											if(response == 'OK'){
												if ($masterTable instanceof $.fn.dataTable.Api) {
													$masterTable.ajax.reload();
												}
												text = '修改成功';
											}
											$dialog.find('form').trigger("reset");
											$dialog.dialog("close");
											$('<div/>').dialog({
												title: '提示訊息',
												draggable : true,
												resizable : false,
												width : "140px",
												modal : true
											}).text(text);
										}
									});
								}
							}
						}, {
							text : "取消",
							click : function() {
								validator_insert_invoice.resetForm();
								$(this).dialog("close");
							}
						} ],
			    beforeClose: function() {
					validator_insert_invoice.resetForm();
					$dialog.find('form').trigger("reset");
			    }
			});
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
					"title" : "課稅別",
					"data" : null,
					"defaultContent" : ""
				},{
					"title" : "發票類別",
					"data" : null,
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
					"data" : null,
					"defaultContent" : ""
				},{
					"title" : "總金額",
					"data" : "amount",
					"defaultContent" : ""
				},{
					"title" : "已開立",
					"data" : null,
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
						input.disabled = data.inv_flag == 0 ? false : true;
						
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
			        targets: 1,
			        searchable: false,
			        orderable: false,
			        render: function(data, type, row) {
			        	var result = '';
			        	if(data.tax_type == '1') {
			            	return '應稅';
			        	}else if (data.tax_type == '2'){
			            	return '零稅率';
			        	}else if (data.tax_type == '3'){
			            	return '免稅';
			        	}
			        }
			    },{
			        targets: 2,
			        searchable: false,
			        orderable: false,
			        render: function(data, type, row) {

			        	return data.invoice_type == 1 ? '二聯式' : '三聯式';
			        }
			    },{
			        targets: 7,
			        searchable: false,
			        orderable: false,
			        render: function(data, type, row) {

			        	return data.unicode == '' ? '無' : data.unicode;
			        }
			    },{
			        targets: 9,
			        searchable: false,
			        orderable: false,
			        render: function(data, type, row) {
			        	return data.inv_flag == 0 ? '否' : '是';
			        }
			    },{
			        targets: -1,
			        searchable: false,
			        orderable: false,
			        render: function(data, type, row) {
			        	
			        	var $div =
			        		$("<div/>", {
			        			"class": "table-function-list"
                            });

			        	if(data.inv_flag == 0){
		        			$div.append(
		        					$("<button/>", {
										"class": "btn-in-table btn-alert btn_delete",
										"title": "刪除"
									}).append($("<i/>", {"class": "fa fa-trash"}))
								).append( 
									$("<button/>", {
										"class": "btn-in-table btn-darkblue btn_update",
										"title": "修改"
									}).append( $("<i/>", {"class": "fa fa-pencil"}) )
								);			        		
			        	}
			        	
	        			$div.append(
	        					$("<button/>", {
									"class": "btn-in-table btn-green btn_list",
									"title": "清單"
								}).append( $("<i/>", {"class": "fa fa-pencil-square-o"}) )
								);
	        			
			            var options = $("<div/>")
			                .append($("<div/>", {
			                        "class": "table-row-func btn-in-table btn-gray"
			                    })
			                    .append($("<i/>", {
			                        "class": "fa fa-ellipsis-h"
			                    }))
			                    .append( $div )
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
		                    	if(!$(this).is(':disabled')){
			                        $(this).prop("checked", false);
			                        $(this).removeClass("toggleon");
			                        $(this).closest("tr").removeClass("selected");
		                    	}
		                    }) :
		                    $checkboxs.each(function() {
		                    	if(!$(this).is(':disabled')){
			                        $(this).prop("checked", true);
			                        $(this).addClass("toggleon");
			                        $(this).closest("tr").addClass("selected");
		                    	}
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
												width : "140px",
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
				},{
		            text: '開立發票',
		            action: function(e, dt, node, config) {
		            	var arr = [];
		            	var data;
		            	
		                var cells = $masterTable.cells().nodes();

		                var $checkboxs = $(cells).find('input[name=checkbox-inv-master-select]:checked');

		                if ($checkboxs.length == 0) {
		                    alert('請至少選擇一筆資料');
		                    return false;
		                }
						
		    			$('<div/>').dialog({
		    				title: '提示訊息',
		    				draggable : true,
		    				resizable : false,
		    				height : "auto",
		    				width : "auto",
		    				modal : true,
		    				buttons : {
		    					"開立" : function() {
		    						var ids = '';

		    						$checkboxs.each(function(index) {
		    							data = $masterTable.row( $(this).closest("tr") ).data();
					                    ids += ',' + data.inv_manual_id ;
		    						});

		    						if (ids.length != 0) {
		    							ids = ids.substring(1, ids.length);
		    						}
		    						
		    						console.log('ids: '+ids);
		    						  
		    						$.ajax({
		    							url: 'InvManual.do',
		    							type: 'post',
		    							data: {
		    								action : 'issueTheInvoice',
		    								ids : ids
		    							},
		    			                beforeSend: function(){
		    			                    $(':hover').css('cursor','progress');
		    			                },
		    			                complete: function(){
		    			                	$(':hover').css('cursor','default');
		    			                },
		    							success: function (response) {
		    								
		    								var $div =
		    									$('<div/>').dialog({
			    									title: '提示訊息',
			    									draggable : true,
			    									resizable : false,
													width : "auto",
			    									modal : true
			    								});
		    								try {
			    								var json_obj = $.parseJSON(response);
			    								var result = '';

			    				                $.each(json_obj, function(i, item) {
			    				                	result += '<br>' + item.invoice_no + ' / ' + item.message + '<hr>'
			    				                });

			    				                $div.append( result );
			    								$masterTable.ajax.reload();
			    								
		    								}catch(e) {
		    									
		    									$div.append($('<p>', {text: '開立失敗' }));

		    								}
				    						
		    							}
		    						});
		    						
		    						$(this).dialog("close");
		    					},
		    					"取消" : function() {
		    						$(this).dialog("close");
		    					}
		    				}
		    			}).text("是否確認要開立發票?");
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
				},{
					"title" : "備註",
					"data" : "memo",
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
						input.disabled = data.inv_flag == 0 ? false : true;

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
			        	var $drawer =
			        		$("<div/>", {
		                        "class": "table-row-func btn-in-table btn-gray"
		                    });
			        	
			        	var $div =
			        		$("<div/>", {
			        			"class": "table-function-list"
                            });

			        	if(data.inv_flag == 0){
		        			$div.append(
		        					$("<button/>", {
										"class": "btn-in-table btn-alert btn_delete",
										"title": "刪除"
									}).append($("<i/>", {"class": "fa fa-trash"}))
								).append( 
									$("<button/>", {
										"class": "btn-in-table btn-darkblue btn_update",
										"title": "修改"
									}).append( $("<i/>", {"class": "fa fa-pencil"}) )
								);
			        	}else{
		        			$drawer.attr('title', '已開立發票，功能禁用!')
			        	}
			        	
			            var options = $("<div/>")
			                .append( $drawer
			                    .append($("<i/>", {
			                        "class": "fa fa-ellipsis-h"
			                    }))
			                    .append( $div )
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
		                    	if(!$(this).is(':disabled')){
			                        $(this).prop("checked", false);
			                        $(this).removeClass("toggleon");
			                        $(this).closest("tr").removeClass("selected");
		                    	}
		                    }) :
		                    $checkboxs.each(function() {
		                    	if(!$(this).is(':disabled')){
			                        $(this).prop("checked", true);
			                        $(this).addClass("toggleon");
			                        $(this).closest("tr").addClass("selected");
		                    	}
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
		            	var $dialog = $('#dialog-invoice-detail');
		            	
		            	var $price = $dialog.find('input[name=price]');
		            	var $quantity = $dialog.find('input[name=quantity]');
		            	var $description = $dialog.find('input[name=description]');
		            	var $subtotal = $dialog.find('input[name=subtotal]');
		            	var $memo = $dialog.find('input[name=memo]');


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

		    								if($('#dialog-invoice-detail').find('form').valid()){

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
			    										subtotal: $subtotal.val(),
			    										memo: $memo.val()
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
		    							}
		    						}, {
		    							text : "取消",
		    							click : function() {
		    								validator_insert_invoice_detail.resetForm();
		    								$(this).dialog("close");
		    							}
		    						} ],
    					    beforeClose: function() {
    					    	validator_insert_invoice_detail.resetForm();
    							$dialog.find('form').trigger("reset");
    					    }
		    			});		            	
		            }
				},{
		            text: '返回主單',
		            action: function(e, dt, node, config) {

		    			if ($detailTable instanceof $.fn.dataTable.Api) {
// 		    				$detailTable.destroy();
// 		    			    $('#invoice-detail-table').empty();
		    			    $('#detailable').hide();
		    			    $('#masterTable').show();
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