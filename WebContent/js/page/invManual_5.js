/**
 * 
 */
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
					"visible": false,
					"defaultContent" : ""
				},{
					"title" : "發票類別",
					"data" : null,
					"visible": false,
					"defaultContent" : ""
				},{
					"title" : "發票期別",
					"data" : "year_month",
					"visible": false,
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
					"title" : "作廢原因",
					"data" : "invoice_reason",
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
					"title" : "總計",
					"data" : "amount_plustax",
					"defaultContent" : ""
				},{
					"title" : "銷售額合計",
					"data" : "amount",
					"defaultContent" : ""
				},{
					"title" : "營業稅額",
					"data" : "tax",
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
			        targets: 8,
			        searchable: false,
			        orderable: false,
			        render: function(data, type, row) {
			        	return data.unicode == '' ? '無' : data.unicode;
			        }
			    },{
			        targets: 12,
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
//								).append( 
//									$("<button/>", {
//										"class": "btn-in-table btn-darkblue btn_update",
//										"title": "修改"
//									}).append( $("<i/>", {"class": "fa fa-pencil"}) )
								);			        		
			        	}
			        	
	        			$div.append(
	        					$("<button/>", {
									"class": "btn-in-table btn-green btn_list",
									"title": "清單"
								}).append( $("<i/>", {"class": "fa fa-list"}) )
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
		                
		                if ($checkboxs.length == 0) {
		                	dialogMsg("提示", '請至少選擇一筆資料');
		                    return false;
		                }
		                
		                var error_msg='';
		                $checkboxs.each(function(index) {
							data = $masterTable.row( $(this).closest("tr") ).data();

		                    if(data.inv_flag!=0){
		                    	error_msg=error_msg+'<br/> 發票號碼: '+data.invoice_no+'已開立，無法刪除已開立發票。<br/>';
		                    }
						});

		                if(error_msg.length>0){
		                	dialogMsg("提示", error_msg);
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
		            }
//				},{
//		            text: '開立發票',
//		            action: function(e, dt, node, config) {
//		            	var arr = [];
//		            	var data;
//		            	
//		                var cells = $masterTable.cells().nodes();
//
//		                var $checkboxs = $(cells).find('input[name=checkbox-inv-master-select]:checked');
//
//		                if ($checkboxs.length == 0) {
//		                	dialogMsg("提示", '請至少選擇一筆資料');
//		                    return false;
//		                }
//		                
//		                var error_msg='';
//		                $checkboxs.each(function(index) {
//							data = $masterTable.row( $(this).closest("tr") ).data();
//
//		                    if(data.inv_flag!=0){
//		                    	error_msg=error_msg+'<br/> 發票號碼: '+data.invoice_no+'已開立，請勿重複開立發票。<br/>';
//		                    }
//						});
//		                
//		                if(error_msg.length>0){
//		                	dialogMsg("提示", error_msg);
//		                	return false;
//		                }
//						
//		    			$('<div/>').dialog({
//		    				title: '提示訊息',
//		    				draggable : true,
//		    				resizable : false,
//		    				height : "auto",
//		    				width : "auto",
//		    				modal : true,
//		    				buttons : {
//		    					"開立" : function() {
//		    						var ids = '';
//
//		    						$checkboxs.each(function(index) {
//		    							data = $masterTable.row( $(this).closest("tr") ).data();
//					                    ids += ',' + data.inv_manual_id ;
//		    						});
//
//		    						if (ids.length != 0) {
//		    							ids = ids.substring(1, ids.length);
//		    						}
//		    						  
//		    						$.ajax({
//		    							url: 'InvManual.do',
//		    							type: 'post',
//		    							data: {
//		    								action : 'issueTheInvoice',
//		    								ids : ids
//		    							},
//		    			                beforeSend: function(){
//		    			                    $(':hover').css('cursor','progress');
//		    			                },
//		    			                complete: function(){
//		    			                	$(':hover').css('cursor','default');
//		    			                },
//		    							success: function (response) {
//		    								
//		    								var $div =
//		    									$('<div/>').dialog({
//			    									title: '提示訊息',
//			    									draggable : true,
//			    									resizable : false,
//													width : "auto",
//			    									modal : true,
//													create: function () {
//														$(this).dialog("widget").find('.ui-dialog-titlebar-close').remove()
//													},
//													buttons : [{
//														text : "確認",
//														click : function() {
//															$(this).dialog("close");
//														}
//													}]
//			    								});
//		    								try {
//			    								var json_obj = $.parseJSON(response);
//			    								var result = '';
//
//			    				                $.each(json_obj, function(i, item) {
//			    				                	result += item.invoice_no == ""? 
//			    				                			item.message:item.invoice_no + " / " + item.message + "<br/>";
//			    				                });
//
//			    				                $div.append( result );
//			    								$masterTable.ajax.reload();
//		    								}catch(e) {
//		    									$div.append($('<p>', {text: '開立失敗' }));
//		    								}
//		    							}
//		    						});
//		    						
//		    						$(this).dialog("close");
//		    					},
//		    					"取消" : function() {
//		    						$(this).dialog("close");
//		    					}
//		    				}
//		    			}).text("是否確認要開立發票?");
//		            }
				},{
		            text: '上傳盟立',
		            action: function(e, dt, node, config) {
		            	var arr = [];
		            	var data;
		            	
		                var cells = $masterTable.cells().nodes();

		                var $checkboxs = $(cells).find('input[name=checkbox-inv-master-select]:checked');

		                if ($checkboxs.length == 0) {
		                	dialogMsg("提示", '請至少選擇一筆資料');
		                    return false;
		                }
		                
		                var error_msg='';
		                $checkboxs.each(function(index) {
							data = $masterTable.row( $(this).closest("tr") ).data();

		                    if(data.inv_flag!=0){
		                    	error_msg=error_msg+'<br/> 發票號碼: '+data.invoice_no+'已開立，請勿重複開立發票。<br/>';
		                    }
						});
		                
		                if(error_msg.length>0){
		                	dialogMsg("提示", error_msg);
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
		    					"上傳" : function() {
		    						var ids = '';

		    						$checkboxs.each(function(index) {
		    							data = $masterTable.row( $(this).closest("tr") ).data();
					                    ids += ',' + data.inv_manual_id ;
		    						});

		    						if (ids.length != 0) {
		    							ids = ids.substring(1, ids.length);
		    						}
		    						  
		    						$.ajax({
		    							url: 'InvManual.do',
		    							type: 'post',
		    							data: {
		    								action : 'transferInvoice',
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
			    								});
		    								try {
			    								var json_obj = $.parseJSON(response);
			    								var result = '';

			    				                $.each(json_obj, function(i, item) {
			    				                	result += item.invoice_no == ""? 
			    				                			item.message:item.invoice_no + " / " + item.message + "<br/>";
			    				                });

			    				                $div.append( result );
			    								$masterTable.ajax.reload();
		    								}catch(e) {
		    									$div.append($('<p>', {text: '上傳失敗' }));
		    								}
		    							}
		    						});
		    						
		    						$(this).dialog("close");
		    					},
		    					"取消" : function() {
		    						$(this).dialog("close");
		    					}
		    				}
		    			}).text("確認是否要上傳加值中心?");
		            }
				},{

		            text: '列印發票',
		            action: function(e, dt, node, config) {
		            	var arr = [];
		            	var data;
		            	
		                var cells = $masterTable.cells().nodes();

		                var $checkboxs = $(cells).find('input[name=checkbox-inv-master-select]:checked');

		                if ($checkboxs.length == 0) {
		                    dialogMsg("提示", '請至少選擇一筆資料');
		                    return false;
		                }

		                var error_msg='';
		                $checkboxs.each(function(index) {
							data = $masterTable.row( $(this).closest("tr") ).data();

		                    if(data.inv_flag==0){
		                    	error_msg=error_msg+'<br/> 發票日期: '+data.invoice_date+'，買受人:'+data.title+'未開立，請先開立發票。<br/>';
		                    }
						});
		                
						if(error_msg.length>0){
							dialogMsg("提示", error_msg);
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
		    					"列印" : function() {
		    						var ids = '';

		    						$checkboxs.each(function(index) {
		    							data = $masterTable.row( $(this).closest("tr") ).data();
					                    ids += ',' + data.inv_manual_id ;
		    						});

		    						if (ids.length != 0) {
		    							ids = ids.substring(1, ids.length);
		    						}

		    						open_report(ids);  

		    						$(this).dialog("close");
		    					},
		    					"取消" : function() {
		    						$(this).dialog("close");
		    					}
		    				}
		    			}).text("是否確認要列印發票?");
		            }
				},{
		            text: '作廢發票',
		            action: cancelInvoice
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
			      
			       if($('#inv_flag').val()=='1'){
			        	$('.hiddenClass').hide();
			       }
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
			        "visible": false,
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
			        "visible": false,
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
//		            text: '全選',
//		            action: function(e, dt, node, config) {
//
//		                selectCount++;
//		                var $checkboxs = $('#invoice-detail-table input[name=checkbox-inv-detail-select]');
//
//		                selectCount % 2 != 1 ?
//		                    $checkboxs.each(function() {
//		                    	if(!$(this).is(':disabled')){
//			                        $(this).prop("checked", false);
//			                        $(this).removeClass("toggleon");
//			                        $(this).closest("tr").removeClass("selected");
//		                    	}
//		                    }) :
//		                    $checkboxs.each(function() {
//		                    	if(!$(this).is(':disabled')){
//			                        $(this).prop("checked", true);
//			                        $(this).addClass("toggleon");
//			                        $(this).closest("tr").addClass("selected");
//		                    	}
//		                    });
//		            }
//		        },{
//		            text: '批次刪除',
//		            action: function(e, dt, node, config) {
//
//		                var $checkboxs = $('#invoice-detail-table input[name=checkbox-inv-detail-select]:checked');
//		                
//		                if ($checkboxs.length == 0) {
//		                    alert('請至少選擇一筆資料');
//		                    return false;
//		                }
//		                var inv_manual_detail_ids = '';
//
//		                $checkboxs.each(function() {
//		                	inv_manual_detail_ids += this.id + ',';
//		                });
//		                
//		                inv_manual_detail_ids = inv_manual_detail_ids.slice(0, -1);
//		    		    
//		    			var parameter = {
//		    					action: 'delete_invoice_detail',
//		    					inv_manual_id: inv_manual_id,
//		    					inv_manual_detail_id: inv_manual_detail_ids
//		    			};
//		    		    
//		    			$('<div/>').dialog({
//		    				title: '警示訊息',
//		    				draggable : true,
//		    				resizable : false,
//		    				height : "auto",
//		    				width : "auto",
//		    				modal : true,
//		    				buttons : {
//		    					"確認刪除" : function() {
//		    						$.ajax({
//		    							url: 'InvManual.do',
//		    							async: false,
//		    							type: 'post',
//		    							data: parameter,
//		    			                beforeSend: function(){
//		    			                    $(':hover').css('cursor','progress');
//		    			                },
//		    			                complete: function(){
//		    			                	$(':hover').css('cursor','default');
//		    			                },
//		    							success: function (response) {
//		    								
//		    								var result = 'OK' == response ? '刪除成功!': '刪除失敗!';
//
//		    								$('<div/>').dialog({
//		    									title: '提示訊息',
//		    									draggable : true,
//		    									resizable : false,
//		    									modal : true,
//												create: function () {
//													$(this).dialog("widget").find('.ui-dialog-titlebar-close').remove()
//												},
//												buttons : [{
//													text : "確認",
//													click : function() {
//														$(this).dialog("close");
//													}
//												}]
//		    								}).append($('<p>', {text: result }));
//		    							}
//		    						});
//		    						
//		    						$detailTable.ajax.reload();
//		    						
//		    						$(this).dialog("close");
//		    					},
//		    					"取消刪除" : function() {
//		    						$(this).dialog("close");
//		    					}
//		    				}
//		    			}).text("確認刪除嗎?");
//		            }
//				},{
//		            text: '新增明細',
//		            className: 'hiddenClass',
//		            action: function(e, dt, node, config) {
//		            	var $dialog = $('#dialog-invoice-detail');
//		            	
//		            	var $price = $dialog.find('input[name=price]');
//		            	var $quantity = $dialog.find('input[name=quantity]');
//		            	var $description = $dialog.find('input[name=description]');
//		            	var $subtotal = $dialog.find('input[name=subtotal]');
//		            	var $memo = $dialog.find('input[name=memo]');
//		            	
//		            	$quantity.val(1);
//		            	
//		            	$dialog.find('input[name=price],input[name=quantity]').change(function(){
//		            		var subtotalVal = $price.val()* $quantity.val();
//		            		
//		            		subtotalVal = isNaN(subtotalVal) ? '資料錯誤，請檢查欄位': subtotalVal;
//
//		            		$subtotal.val( subtotalVal );
//		        		});
//		            	
//		    			$dialog.dialog({
//		    				draggable : true,
//		    				resizable : false,
//		    				height : "auto",
//		    				width : "auto",
//		    				modal : true,
//		    				title : '新增明細',
//		    				buttons : [{
//		    							text : "儲存",
//		    							click : function() {
//		    								if($('#dialog-invoice-detail').find('form').valid()){
//												if ( checkInvalid( $description.val() ) ) {
//			    				            		dialogMsg('提示', '輸入資料含有特殊字元，請重新輸入');
//			    				            		return;
//			    				            	}
//			    				            	
//			    								$.ajax({
//			    									url: 'InvManual.do',
//			    									async : false,
//			    									type: 'post',
//			    									data : {
//			    										action: 'insertDetail',
//			    										inv_manual_id: inv_manual_id,
//			    										price: $price.val(),
//			    										quantity: $quantity.val(),
//			    										description: $description.val(),
//			    										subtotal: $subtotal.val(),
//			    										memo: $memo.val()
//			    									},
//			    									beforeSend: function(){
//			    									    $(':hover').css('cursor','progress');
//			    									},
//			    									complete: function(){
//			    										$(':hover').css('cursor','default');
//			    									},
//			    									success: function (response) {
//			    										$masterTable.ajax.reload();
//			    										$dialog.find('form').trigger("reset");
//			    										$dialog.dialog("close");
//			    									}
//			    								});
//			    								
//			    								$detailTable.ajax.reload();
//			    							}
//		    							}
//		    						}, {
//		    							text : "取消",
//		    							click : function() {
//		    								validator_insert_invoice_detail.resetForm();
//		    								$(this).dialog("close");
//		    							}
//		    						} ],
//    					    beforeClose: function() {
//    					    	validator_insert_invoice_detail.resetForm();
//    							$dialog.find('form').trigger("reset");
//    					    }
//		    			});		            	
//		            }
//				},{
		            text: '返回',
		            action: function(e, dt, node, config) {
						divControl(true, false);
		    		    
		    			var parameter = {
    						action: 'query_invoice'
		    			};
		    			
		    			var startDate = $('form').find('input[type=text]:eq(0)').val();
		    			var endDate = $('form').find('input[type=text]:eq(1)').val();
		    			
		    			if (startDate && endDate) {
		    				parameter = {
	    						action: 'query_invoice',
								startDate: startDate,
								endDate: endDate
			    			};
		    			}
		    			drawInvMasTable(parameter);
		            }
				}]
			});		
		};
		
		function divControl(master, detail){
			if(master){
				$('#masterTable').show()
			}else{
				$('#masterTable').hide()
			};
			if(detail){
				$('#detailTable').show()
			}else{
				$('#detailTable').hide()
			};
		}
		
		function open_report(ids){
			
			var iframUrl="./report.do?action=rptInvManual&ids="+encodeURIComponent(ids);

			$("#dialog_report_iframe").attr("src",iframUrl );
			 $("#dialog_report").dialog({
					draggable : true,
					resizable : false,
					autoOpen : false,
					height : "600",
					width : "900",
					modal : true, 
					closeOnEscape: false,
				    open: function(event, ui) {
				        $(".ui-dialog-titlebar-close", ui.dialog | ui).hide();
				    },
					buttons : [{
						text : "關閉",
						click : function() {
							$("#dialog_report").dialog("close");
						}
					}]
			 })
			 $("#dialog_report").dialog("open"); 	
		}
		
	    function getInvBuyerData(request, response, type) {
	        $.ajax({
	            url: "InvManual.do",
	            type: "POST",
				async: false,
	            data: {
	                action: "getInvBuyerData",
	                type: type,
	                term: request.term
	            },
	            success: function(data) {
	                var json_obj = $.parseJSON(data);
	                var result = [];
	                
	                if (!json_obj.length) {
	                    result = [{
	                        label: '找不到符合資料',
	                        value: request.term
	                    }];
	                } else {
	                    result = $.map(json_obj, function(item) {
	                        var label = "",
	                            value = "";
	                        if (type == "title") {
	                            label = item.title;
	                            value = item.title;
	                        } else if (type == "unicode") {
	                            label = item.unicode;
	                            value = item.unicode;
	                        }

	                        return {
	                            label: label,
	                            value: value,
	                            title: item.title,
	                            unicode: item.unicode,
	                            address: item.address
	                        }
	                    });
	                }
	                return response(result);
	            }
	        });
	    }
	    
	    function cancelInvoice(e, dt, node, config) {
        	var cells = dt.cells().nodes();
            var checklist = $(cells).find('input[name=checkbox-inv-master-select]:checked');
            var invMap = new Map();
            
            if (checklist.length == 0) {
            	dialogMsg("提示", '請至少選擇一筆資料');
                return false;
            }
            
            var error_msg = '';
            checklist.each(function(index) {
				temp = dt.row( $(this).closest("tr") ).data();
                if(temp.inv_flag == 0){
                	error_msg = error_msg + '<br/> 未開立，請勿勾選！<br/>';
                }
                if(temp.invoice_no!=""){
					invMap.set(temp.invoice_no, (index + 1));
                }
			});
            
            if(error_msg.length>0){
            	dialogMsg("提示", error_msg);
            	return false;
            }
            
            if (invMap.size > 1) {
            	dialogMsg("警告", '僅能單筆作廢');
                return;
            }
        	
            $("#dialog-invoice-cancel").dialog({
				title: '作廢發票',
				draggable : true,
				resizable : false,
				height : "auto",
				width : "auto",
				modal : true,
			    beforeClose: function() {
			    	valid_cancel_form.resetForm();
					$(this).find('form').trigger("reset");
			    },
				buttons : {
					"作廢" : function() {
						var ids = '';

						checklist.each(function(index) {
							data = dt.row( $(this).closest("tr") ).data();
		                    ids += ',' + data.inv_manual_id ;
						});

						if (ids.length != 0) {
							ids = ids.substring(1, ids.length);
						}
						
						if ( !$("#dialog-invoice-cancel").find('form').valid() ) {
							return;
						}
						  
						$.ajax({
							url: 'InvManual.do',
							type: 'post',
							data: {
								action : 'cancelInvoice',
								ids : ids,
                                reason: $("#invoice_cancel_reason").val()
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
    									title: '作廢發票',
    									draggable : true,
    									resizable : false,
										width : "auto",
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
    								});
								
								try {
    								var json_obj = $.parseJSON(response);
    								var result = '';

    				                $.each(json_obj, function(i, item) {
    				                	result += item.invoice_no == ""? 
    				                			item.message:item.invoice_no + " / " + item.message + "<br/>";
    				                });

    				                $div.append( result );
    								dt.ajax.reload();
								} catch(e) {
									$div.append($('<p>', {text: '作廢失敗' }));
								}
							}
						});
						
						$(this).dialog("close");
					},
					"取消" : function() {
						$(this).dialog("close");
					}
				}
			});
            
            $("#dialog-invoice-cancel").dialog("open");
        }
	    
	    $("#dialog-invoice").find('input[name=amount],input[name=tax]').change(function(){
    		if ($("#dialog-invoice").find('form').valid()) {
    			var amount = $("#dialog-invoice input[name=amount]").val();
        		var tax = $("#dialog-invoice input[name=tax]").val();
       			$("#dialog-invoice input[name=amount_plustax]").val(parseInt(amount) + parseInt(tax));
    		}
		});
	    
	    function checkInvalid( value ) {
	    	return /\x08/.test( value );
	    }
