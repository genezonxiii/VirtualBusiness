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
<title>庫存管理</title>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<link rel="Shortcut Icon" type="image/x-icon"
	href="./images/Rockettheme-Ecommerce-Shop.ico" />
<jsp:include page="template/common_css.jsp" flush="true" />
<link rel="stylesheet" href="css/buttons.dataTables.min.css">
</head>
<body>
	<input type="hidden" id="glb_menu" value='<%=menu%>' />
	<input type="hidden" id="glb_privilege" value="<%=privilege%>" />

	<div class="page-wrapper">
		<jsp:include page="template/common_headfoot.jsp" flush="true" />

		<div class="content-wrap">
			<h2 class="page-title">庫存管理</h2>

			<div class="panel-content">
				<div class="datalistWrap">
					<div class="input-field-wrap">
						<div class="form-wrap">
							<div class="form-row">
								<form id = "form_no">
									<label for=""> 
										<span class="block-label">供應商名稱查詢</span> 
										<input type="text" name="supply_name">
									</label>
									<button class="btn btn-darkblue">查詢</button>
								</form>
							</div>
							<div class="form-row">
								<form id = "form_date">
									<label for=""> <span class="block-label">入庫起日</span> <input
										type="text" name="start_date" class='input-date'>
									</label>
									<div class='forward-mark'></div>
									<label for=""> <span class="block-label">入庫迄日</span> <input
										type="text" name="end_date" class='input-date'>
									</label>
									<button class="btn btn-darkblue">查詢</button>
								</form>
							</div>
							<div class="form-row">
								<form id = "form_status">
									<select id='inventory_status'>
										<option value="0">請選擇庫存狀態</option>
										<option value="10">正品</option>
										<option value="20">殘品</option>
									</select>
								</form>
							</div>
							<input type="hidden" id="hid_type" value="">
							<input type="hidden" id="hid_supply_name" value="">
							<input type="hidden" id="hid_start_date" value="">
							<input type="hidden" id="hid_end_date" value="">
						</div>
					</div>
				</div>
			</div>
			<div class="panel-content">
				<div class="datalistWrap">
					<div class="row search-result-wrap">
						<table id="dt_master_stock_new" class="result-table"></table>
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
	$(function(){
		
		$("input[name=supply_name]").autocomplete({
            minLength: 0,
            source: function (request, response) {
            	console.log(request);
                $.ajax({
                    url : "purchase.do",
                    type : "POST",
                    cache : false,
                    delay : 1500,
                    data : {
                    	action : "search_supply_name",
                        term : request.term
                    },
                    success: function(data) {
                    	var json_obj = $.parseJSON(data);
                    	response($.map(json_obj, function (item) {
                            return {
                              label: item.supply_name,
                              value: item.supply_name,
                              supply_id: item.supply_id
                            }
                        }))
                    }
                });
            },
            change: function(event, ui) {
    	        var source = $(this).val();
   	            var temp = $(".ui-autocomplete li")
   	            	.map(function () { 
   	            		return $(this).text()
					})
   	            	.get();
    	        var found = $.inArray(source, temp);
    	
    	        if(found < 0) {
    	        	$("input[name=supply_name]").attr("supply_error", $(this).val());
    	            $(this).val('');
    	            $(this).attr("placeholder","輸入正確的供應商名稱!");
    	            setTimeout(function(){
    	            	$("input[name=supply_name]").attr("supply_error","");
   	            	}, 200);
    	        }
    	    }     
        });
		
		$('#form_no').on("click", "button", function(e) {
			e.preventDefault();
			
			var errorMes = '';
			var $mes = $('#message #text');
			var $supply_name = $('#form_no input[name=supply_name]').val();
			console.log($.trim($supply_name).length);
			if($.trim($supply_name).length == 0){
				errorMes += "請輸入供應商名稱!";
			}

			if(errorMes.length > 0){
				$mes.val('').html(errorMes);
				$('#message')
					.dialog()
					.dialog('option', 'title', '警告')
					.dialog('option', 'width', 'auto')
					.dialog('option', 'minHeight', 'auto')
					.dialog("open");
				return false;
			}
			
			$("#hid_type").val("supply");
			$("#hid_supply_name").val($supply_name);
			
			var parameter = {
				action : "getStockNewListBySupplyName",
				supply_name : $supply_name
			};
			console.log(parameter);
			drawMasterTable(parameter);
		});
		
		$('#form_date').on("click", "button", function(e) {
			e.preventDefault();
			var $startDate = $('#form_date input:eq(0)').val();
			var $endDate = $('#form_date input:eq(1)').val();
			var errorMes = '';
			var $mes = $('#message #text');
			if($endDate != null && $endDate.length != 0 && $startDate != null && $startDate.length != 0){
				if($startDate.replace(/-/g,"") > $endDate.replace(/-/g,"")){
					errorMes += "訖日不可小於起日!<br>";
				}
			}
			if($startDate == null || $startDate.length == 0){
				errorMes += "請選擇起日!<br>";
			}
			else if(!dateValidationCheck($startDate)){
				errorMes += "起日格式不符!<br>";
			}
			if($endDate == null || $endDate.length == 0){
				errorMes += "請選擇訖日!<br>";
			}
			else if(!dateValidationCheck($endDate)){
				errorMes += "訖日格式不符!<br>";
			}

			if(errorMes.length > 0){
				$mes.val('').html(errorMes);
				$('#message')
					.dialog()
					.dialog('option', 'title', '警告')
					.dialog('option', 'width', 'auto')
					.dialog('option', 'minHeight', 'auto')
					.dialog("open");
				return false;
			}
			$("#hid_type").val("date");
			$("#hid_start_date").val($startDate);
			$("#hid_end_date").val($endDate);

			var parameter = {
				action : "getStockNewListByStockTime",
				startDate : $startDate,
				endDate : $endDate
			};
			console.log(parameter);
			drawMasterTable(parameter);
		});
	    $('#dt_master_stock_new').on('change', ':checkbox', function() {
	        $(this).is(":checked")?
	        	$(this).closest("tr").addClass("selected"):
	        	$(this).closest("tr").removeClass("selected");
	    });
	});
	</script>
	<script type="text/javascript">
	function drawMasterTable(parameter) {

		console.log("parameter: "+parameter);
		$dtMaster = $("#dt_master_stock_new").DataTable({
			dom : "frB<t>ip",
			lengthChange: false,
			pageLength: 20,
			width : 'auto',
			scrollY:"250px",
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
				url : "StockNew.do",
				dataSrc : "",
				type : "POST",
				data : parameter
			},
			columns : [{
				"title" : "批次請求",
				"data" : null,
				"defaultContent" : ""
			},{
				"title" : "商品流水編號",
				"data" : "productVO.c_product_id",
				"defaultContent" : ""
			}, {
				"title" : "商品名稱",
				"data" : "productVO.product_name",
				"defaultContent" : ""
			}, {
				"title" : "供應商名稱",
				"data" : "productVO.supply_name",
				"defaultContent" : ""
			}, {
				"title" : "備註",
				"data" : "memo",
				"defaultContent" : ""
			},{
				"title" : "庫存量",
				"data" : "quantity",
				"defaultContent" : ""
			},{
				"title" : "倉庫代號",
				"data" : "locationVO.warehouseVO.warehouse_code",
				"defaultContent" : ""
			},{
				"title" : "儲位代碼",
				"data" : "locationVO.location_code",
				"defaultContent" : ""
			},{
				"title" : "有效日期",
				"data" : "valid_date",
				"defaultContent" : ""
			} ],
			columnDefs : [ {
				targets : 0,
				searchable : false,
				orderable : false,
				render : function(data, type, row) {
					var stock_id = row.stock_id;

					var input = document.createElement("INPUT");
					input.type = 'checkbox';
					input.name = 'checkbox-group-select';
					input.id = stock_id;

					var span = document.createElement("SPAN");
					span.className = 'form-label';

					var label = document.createElement("LABEL");
					label.htmlFor = stock_id;
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
					var $table =  $('#dt_master_stock_new');
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
			}, {
				text : '順豐庫存查詢',
				action : function(e, dt, node, config) {
					var $table =  $('#dt_master_stock_new');

				    var cells = $dtMaster.cells( ).nodes();
					var noArr = '';
					
					var $checkboxs = $(cells).find('input[name=checkbox-group-select]:checked');
					var inventory_status_val = $("#inventory_status").val();
					console.log($checkboxs);
					
					if($checkboxs.length == 0){
						alert('請至少選擇一筆資料');
						return false;
					}
					if($checkboxs.length > 20){
						alert('最多選擇二十筆資料');
						return false;
					}
		
					if(inventory_status_val == '0'){
						alert('請選擇庫存狀態');
						return false;
					}
					
					$checkboxs.each(function() {
						noArr += this.id + ',';
					});
					noArr = noArr.slice(0,-1);
					noArr = noArr.replace(/,/g,"','");
					noArr = "'" + noArr + "'";
					
					$.ajax({
						url: 'StockNew.do', 
						type: 'post',
						data: {
							action: 'rtInventoryQueryService',
							stock_ids: noArr,
							inventory_status : inventory_status_val
						},
						error: function (xhr) { },
						success: function (response) {
							var msg = "";
							if (response) {
								console.log(response);
								var json_obj = $.parseJSON(response);
								console.log(json_obj);
								if (json_obj.response) {
									if (json_obj.response.head == "OK" || json_obj.response.head == "PART") {
										var rtInvList = json_obj.response.body.rtInventoryQueryResponse.rtInventorys.rtiList;
										
										$.each(rtInvList, function(key, value) {
											var skuNo = "", lot = "", expire = "";
											var availableQty = "", totalQty = "";
											var onHandQty = "", inTransitQty = "";
											if (value.header) {
												skuNo = "商品料號：" + value.header.skuNo;
												availableQty = "/總庫存量：" + value.header.availableQty;
												totalQty = "/可用量：" + value.header.totalQty;
												onHandQty = "/在庫量：" + value.header.onHandQty;
												inTransitQty = "/在途量：" + value.header.inTransitQty;
												if (value.header.expirationDate) {
													expire = "/效期：" + value.header.availableQty;
												}
												if (value.header.lot) {
													lot = "/批號：" + value.header.lot;
												}
											}
											
											if (value.result == 1) {
												msg += skuNo + 
													availableQty + totalQty + 
													onHandQty + inTransitQty + 
													lot + expire + "<br/>";
											} else {
												msg += "商品料號：" + value.header.skuNo + "<br/>";
											}
										});
									} else if (json_obj.response.head == "ERR") {
										msg += "代碼：" +  json_obj.response.error.code + 
											"/原因：" +  json_obj.response.error.error;
									}
								} else if (json_obj.responseFail) {
									msg = json_obj.responseFail.remark;
								}										
							}

							dialogMsg("順豐庫存查詢", msg);
							
							if($("#hid_type").val()=="date"){
								var parameter = {
										action : "getStockNewListByStockTime",
										startDate : $("#hid_start_date").val(),
										endDate : $("#hid_end_date").val()
									};
									console.log(parameter);
									drawMasterTable(parameter);
							}else if($("#hid_type").val()=="supply"){
								var parameter = {
										action : "getStockNewListBySupplyName",
										supply_name : $("#hid_supply_name").val()
									};
							}

						}
					});
				}
			}
			]
		});
	};

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
	</script>
</body>
</html>
