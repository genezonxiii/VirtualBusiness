/**
 * 
 */

		//查詢
		$('.input-field-wrap').on('click', '.btn-darkblue', function(event){
			event.preventDefault();

			divControl(true, false);
		    
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
			
			$( "input[name='amount_plustax'], [name='amount'], [name='tax']" ).closest("tr").hide();
			$( "#invoice-tax-type-radio-1" ).prop("checked", true);
			$( "input[name='invoice_date']" ).datepicker('setDate', new Date());
			
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
							var invoice_type ='2';
							
							var tax_type = $( "input[name='invoice-tax-type-radio-group']:checked", $dialog ).val();
							var title = $( "input[name='title']", $dialog ).val();
							var unicode = $( "input[name='unicode']", $dialog ).val();
							var address = $( "input[name='address']", $dialog ).val();
							var memo = $( "input[name='memo']", $dialog ).val();
							var invoice_date = $( "input[name='invoice_date']", $dialog ).val();
							
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
										modal : true,
										create: function () {
											$(this).dialog("widget").find('.ui-dialog-titlebar-close').remove()
										},
										buttons : [{
											text : "確認",
											click : function() {
												$(this).dialog("close");
											}
										}]
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
				}],
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
									modal : true,
									create: function () {
										$(this).dialog("widget").find('.ui-dialog-titlebar-close').remove()
									},
									buttons : [{
										text : "確認",
										click : function() {
											$(this).dialog("close");
										}
									}]
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
						if($('#dialog-invoice-detail').find('form').valid()){
							if ( checkInvalid( $description.val() ) ) {
			            		dialogMsg('提示', '輸入資料含有特殊字元，請重新輸入');
			            		return;
			            	}
							
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
										modal : true,
										create: function () {
											$(this).dialog("widget").find('.ui-dialog-titlebar-close').remove()
										},
										buttons : [{
											text : "確認",
											click : function() {
												$(this).dialog("close");
											}
										}]
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
									modal : true,
									create: function () {
										$(this).dialog("widget").find('.ui-dialog-titlebar-close').remove()
									},
									buttons : [{
										text : "確認",
										click : function() {
											$(this).dialog("close");
										}
									}]
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

			$('#inv_flag').val(data.inv_flag);
			divControl(false, true);
		    
			var parameter = {
					action: 'query_invoice_detail',
					inv_manual_id: inv_manual_id
			};
			drawInvListTable(parameter);
		});

		//主單功能-修改
		$('#invoice-master-table').delegate(".btn_update", "click", function(e) {
			event.preventDefault();
			
			$( "input[name='amount_plustax'], [name='amount'], [name='tax']" ).closest("tr").show();

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
			$dialog.find('input[name=amount]').val(data.amount);
			$dialog.find('input[name=amount_plustax]').val(data.amount_plustax);
			$dialog.find('input[name=tax]').val(data.tax);
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
// 							var invoice_type = $( "input[name='invoice-type-radio-group']:checked", $dialog ).attr("id");
// 							invoice_type = invoice_type.substring( invoice_type.length, invoice_type.length -1 );
							var invoice_type = '2';
							
							var tax_type = $( "input[name='invoice-tax-type-radio-group']:checked", $dialog ).val();
							var title = $( "input[name='title']", $dialog ).val();
							var unicode = $( "input[name='unicode']", $dialog ).val();
							var address = $( "input[name='address']", $dialog ).val();
							var memo = $( "input[name='memo']", $dialog ).val();
							var invoice_no = $( "input[name='invoice_no']", $dialog ).val();
							var invoice_date = $( "input[name='invoice_date']", $dialog ).val();
							var amount = $( "input[name='amount']", $dialog ).val();
							var amount_plustax = $( "input[name='amount_plustax']", $dialog ).val();
							var tax = $( "input[name='tax']", $dialog ).val();
							
							if (tax_type == 1) {
								var standard_tax = Math.round(amount * 0.05);
								var diff = standard_tax - tax;
								if (!(diff == 0 || diff == 1)) {
									dialogMsg("警告", '營業稅額只能為 ' + standard_tax + ' 或 ' + (standard_tax - 1) + '，請重新修正！！');
									return;
								}
							}
							
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
									tax_type: tax_type,
									amount: amount,
									amount_plustax: amount_plustax,
									tax: tax
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
										modal : true,
										create: function () {
											$(this).dialog("widget").find('.ui-dialog-titlebar-close').remove()
										},
										buttons : [{
											text : "確認",
											click : function() {
												$(this).dialog("close");
											}
										}]
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
