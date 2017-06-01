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
									<label for=""> <span class="block-label">供應商名稱查詢</span> <input
										type="text" name="supply_name">
									</label>
									<button class="btn btn-darkblue">查詢</button>
								</form>
							</div>
							<div class="form-row">
								<form id = "form_date">
									<label for=""> <span class="block-label">進貨起日</span> <input
										type="text" name="start_date" class='input-date'>
									</label>
									<div class='forward-mark'></div>
									<label for=""> <span class="block-label">進貨迄日</span> <input
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
			dom : "lfrB<t>ip",
			width : 'auto',
			scrollCollapse : true,
			destroy : true,
			language : {
				"url" : "js/dataTables_zh-tw.txt",
				"emptyTable" : "查無資料",
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
				"title" : "產品流水編號",
				"data" : "productVO.c_product_id",
				"defaultContent" : ""
			}, {
				"title" : "產品名稱",
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

					var text = document.createTextNode('選取');
					span.appendChild(text);

					var label = document.createElement("LABEL");
					label.htmlFor = stock_id;
					label.name = 'checkbox-group-select';
					label.style.marginLeft = '20%';
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
				text : '發送電文',
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
							var $mes = $('#message #text');
							$mes.val('').html('成功發送');
							$('#message')
								.dialog()
								.dialog('option', 'title', '提示訊息')
								.dialog('option', 'width', 'auto')
								.dialog('option', 'minHeight', 'auto')
								.dialog("open");
						}
					});		
					console.log(noArr);				
				}
			},{
				text : '發送取消電文',
				action : function(e, dt, node, config) {
					var $table =  $('#dt_master_stock_new');

				    var cells = $dtMaster.cells( ).nodes();
					var noArr = '';
					
					var $checkboxs = $(cells).find('input[name=checkbox-group-select]:checked');
					
					console.log($checkboxs);
					
					if($checkboxs.length == 0){
						alert('請至少選擇一筆資料');
						return false;
					}
					if($checkboxs.length > 20){
						alert('最多選擇二十筆資料');
						return false;
					}
					$checkboxs.each(function() {
						noArr += this.id + ',';
					});
					noArr = noArr.slice(0,-1);
					$.ajax({
						url: 'StockNew.do', 
						type: 'post',
						data: {
							action: '',
							stock_ids: noArr
							
						},
						error: function (xhr) { },
						success: function (response) {
							var $mes = $('#message #text');
							$mes.val('').html('成功發送');
							$('#message')
								.dialog()
								.dialog('option', 'title', '提示訊息')
								.dialog('option', 'width', 'auto')
								.dialog('option', 'minHeight', 'auto')
								.dialog("open");
						}
					});		
					console.log(noArr);				
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
