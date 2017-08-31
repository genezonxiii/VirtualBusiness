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
<title>買受人管理</title>
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
			<h2 class="page-title">買受人管理</h2>

			<div class="panel-content">
				<div class="datalistWrap">
					<div class="input-field-wrap">
						<div class="form-wrap">
							<div class="form-row">
								<form>
									<label for=""> <span class="block-label">統一編號</span>
										<input type="text">
									</label>
									<label for=""> <span class="block-label">買受人名稱</span>
										<input type="text">
									</label>
									<button class="btn btn-darkblue">查詢</button>
									<button class="btn btn-exec">新增買受人</button>
								</form>
							</div>
						</div>
					</div>
					<div class="panel-content">
						<div class="datalistWrap">
							<div class="row search-result-wrap">
								<table id="inv-buyer-table" class="result-table"></table>
							</div>
						</div>
					</div>					
				</div>
			</div>
		</div>
	</div>
	<div id="dialog-inv-buyer" style="display:none">
		<form>
			<fieldset>
				<table class='form-table'>
					<tr>
						<td>買受人</td><td><input type="text" name="title"></td>
					</tr>
					<tr>
						<td>統一編號</td><td><input type="text" name="unicode"></td>
					</tr>
					<tr>
						<td>地址</td><td><input type="text" name="address"></td>
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
	
	<!-- Global Variables -->
	<script type="text/javascript">
		var $buyerTable;
		var selectCount = 0;
	</script>

	<!-- Jquery Validate -->
	<script type="text/javascript">

		$.extend(jQuery.validator.messages, {
		    required: "必填欄位"
		});
		
		var validator_insert_inv_buyer = $('#dialog-inv-buyer').find('form').validate({
	        rules: {
	            'title': {
	                required: true
	            }
	        },
	        errorPlacement: function(error, element) {
	        	error.insertAfter(element.closest("td"));
	    	}
	    });
	</script>	
	
	<!-- Listener -->
	<script type="text/javascript">
		//新增
		$('.input-field-wrap').on('click', '.btn-exec', function(event){
			event.preventDefault();
			
			var $dialog = $('#dialog-inv-buyer');
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
								if( $('#dialog-inv-buyer').find('form').valid() ){
									var title = $( "input[name='title']", $dialog ).val();
									var unicode = $( "input[name='unicode']", $dialog ).val();
									var address = $( "input[name='address']", $dialog ).val();
									var memo = $( "input[name='memo']", $dialog ).val();
									
									console.log('title: '+ title);
									console.log('unicode: '+ unicode);
									console.log('address: '+ title);
									console.log('memo: '+ unicode);
									
									$.ajax({
										url: 'InvBuyer.do',
										type: 'post',
										data : {
											action: 'insertInvBuyer',
											title: title,
											unicode:  unicode,
											address: address,
											memo: memo
										},
										beforeSend: function(){
										    $(':hover').css('cursor','progress');
										},
										complete: function(){
											$(':hover').css('cursor','default');
										},
										success: function (response) {
											
											var result = 'fail' != response ? 
		    										function(){
														if ($buyerTable instanceof $.fn.dataTable.Api) {
		    												$buyerTable.ajax.reload();
														}
		    											return '新增成功!'
		    										}: '新增失敗!';
		    											
											$dialog.find('form').trigger("reset");
											
											$dialog.dialog("close");
											
											$('<div/>').dialog({
												title: '提示訊息',
												draggable : true,
												resizable : false,
												width : "140px",
												modal : true
											}).text( result );
										}
									});
								}
							}
						}, {
							text : "取消",
							click : function() {
								validator_insert_inv_buyer.resetForm();
								$(this).dialog("close");
							}
						} ],
			    beforeClose: function() {
					validator_insert_inv_buyer.resetForm();
					$dialog.find('form').trigger("reset");
			    }
			});
		});
		
		//查詢
		$('.input-field-wrap').on('click', '.btn-darkblue', function(event){
			event.preventDefault();
		    
			var $form = $('form');
			var unicode = $form.find('input[type=text]:eq(0)').val();
			var title = $form.find('input[type=text]:eq(1)').val();
	
			var parameter = {
					action: 'selectInvBuyerByUnicodeOrTitle',
					unicode: unicode,
					title: title
			};
			drawBuyerTable(parameter);
		});

		//刪除
		$('#inv-buyer-table').delegate(".btn_delete", "click", function(e) {
			e.preventDefault();
			
			var row = $(this).closest("tr");
		    var data = $buyerTable.row(row).data();
		    var inv_buyer_id = data.inv_buyer_id;
		    
			var parameter = {
					action: 'delInvBuyer',
					inv_buyer_ids: "'" + inv_buyer_id + "'"
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
							url: 'InvBuyer.do',
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

								var result = 'fail' != response ? 
										function(){
											$buyerTable.ajax.reload();
											return '刪除成功!'
										}: '刪除失敗!';

								$('<div/>').dialog({
									title: '提示訊息',
									draggable : true,
									resizable : false,
									width : "140px",
									modal : true
								}).append($('<p>', {text: result }));
							}
						});
						
						$(this).dialog("close");
					},
					"取消刪除" : function() {
						$(this).dialog("close");
					}
				}
			}).text("確認刪除嗎?");

		});
		
		//修改
		$('#inv-buyer-table').delegate(".btn_update", "click", function(e) {
			event.preventDefault();

			var row = $(this).closest("tr");
		    var data = $buyerTable.row(row).data();
		    inv_buyer_id = data.inv_buyer_id;
		    
			var $dialog = $('#dialog-inv-buyer');
			
			$dialog.find('input[name=title]').val(data.title);
			$dialog.find('input[name=unicode]').val(data.unicode);
			$dialog.find('input[name=address]').val(data.address);
			$dialog.find('input[name=memo]').val(data.memo);
			
			$dialog.dialog({
				draggable : true,
				resizable : false,
				height : "auto",
				width : "auto",
				modal : true,
				title : '修改買受人',
				buttons : [{
							text : "修改",
							click : function() {

								if($('#dialog-inv-buyer').find('form').valid()){
									
									var title = $( "input[name='title']", $dialog ).val();
									var unicode = $( "input[name='unicode']", $dialog ).val();
									var address = $( "input[name='address']", $dialog ).val();
									var memo = $( "input[name='memo']", $dialog ).val();
									
									console.log('title: '+ title);
									console.log('unicode: '+ unicode);
									console.log('address: '+ address);
									console.log('memo: '+ memo);

									$.ajax({
										url: 'InvBuyer.do',
										type: 'post',
										data : {
											action: 'updateInvBuyer',
											inv_buyer_id: inv_buyer_id,
											title: title,
											unicode:  unicode,
											address: address,
											memo: memo,
										},
										beforeSend: function(){
										    $(':hover').css('cursor','progress');
										},
										complete: function(){
											$(':hover').css('cursor','default');
										},
										success: function (response) {
											
											var result = 'fail' != response ? 
		    										function(){
														if ($buyerTable instanceof $.fn.dataTable.Api) {
		    												$buyerTable.ajax.reload();
														}
		    											return '修改成功!'
		    										}: '修改失敗!';
		    											
											$dialog.find('form').trigger("reset");
											$dialog.dialog("close");
											$('<div/>').dialog({
												title: '提示訊息',
												draggable : true,
												resizable : false,
												width : "140px",
												modal : true
											}).text( result );
										}
									});									
								}
							}
						}, {
							text : "取消",
							click : function() {
								validator_insert_inv_buyer.resetForm();
								$(this).dialog("close");
							}
						} ],
			    beforeClose: function() {
					validator_insert_inv_buyer.resetForm();
					$dialog.find('form').trigger("reset");
			    }
			});
		});
	    $('#inv-buyer-table').on('change', ':checkbox', function() {
	        $(this).is(":checked")?
	        	$(this).closest("tr").addClass("selected"):
	        	$(this).closest("tr").removeClass("selected");
	    });
	</script>
	
	<!-- Method -->
	<script type="text/javascript">
		function drawBuyerTable(parameter) {
			$buyerTable = $("#inv-buyer-table").DataTable({
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
					url : "InvBuyer.do",
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
					"title" : "買受人",
					"data" : "title",
					"defaultContent" : ""
				},{
					"title" : "統一編號",
					"data" : "unicode",
					"defaultContent" : ""
				},{
					"title" : "地址",
					"data" : "address",
					"defaultContent" : ""
				},{
					"title" : "備註",
					"data" : "memo",
					"defaultContent" : ""
				},{
					"title" : "建立時間",
					"data" : "create_time",
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

						var inv_buyer_id = data.inv_buyer_id;
						
						var input = document.createElement("INPUT");
						input.type = 'checkbox';
						input.name = 'checkbox-inv-buyer-select';
						input.id = inv_buyer_id;
						
						var span = document.createElement("SPAN");
						span.className = 'form-label';
						
						var label = document.createElement("LABEL");
						label.htmlFor = inv_buyer_id;
						label.name = 'checkbox-inv-buyer-select';
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
			        	
			        	var $div =
			        		$("<div/>", {
			        			"class": "table-function-list"
                            });
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
		                var $checkboxs = $('#inv-buyer-table input[name=checkbox-inv-buyer-select]');

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

		                var $checkboxs = $('#inv-buyer-table input[name=checkbox-inv-buyer-select]:checked');
		                
		                console.log($checkboxs)
		                
		                if ($checkboxs.length == 0) {
		                	dialogMsg("提示", '請至少選擇一筆資料');
		                    return false;
		                }
		                
		                var inv_buyer_ids = '';

		                $checkboxs.each(function() {
		                	inv_buyer_ids += "'"+this.id + "',";
		                });
		                
		                inv_buyer_ids = inv_buyer_ids.slice(0, -1);
		    		    
		    			var parameter = {
		    					action: 'delInvBuyer',
		    					inv_buyer_ids: inv_buyer_ids
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
		    							url: 'InvBuyer.do',
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
		    								
		    								var result = 'fail' != response ? 
		    										function(){
		    											$buyerTable.ajax.reload();
		    											return '刪除成功!'
		    										}: '刪除失敗!';

		    								$('<div/>').dialog({
		    									title: '提示訊息',
		    									draggable : true,
		    									resizable : false,
												width : "140px",
		    									modal : true
		    								}).append($('<p>', {text: result }));
		    							}
		    						});
		    						
		    						$buyerTable.ajax.reload();
		    						
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
	</script>	
</body>
</html>