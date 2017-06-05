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
<title>儲位異動管理</title>
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
			<h2 class="page-title">預出庫管理</h2>

			<div class="panel-content">
				<div class="datalistWrap">
					<div class="input-field-wrap">
						<div class="form-wrap">
						<h3>預計出庫資料</h3>
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
		</div>
	</div>

	<jsp:include page="template/common_js.jsp" flush="true" />
	<script type="text/javascript" src="js/dataTables.buttons.min.js"></script>
	<script type="text/javascript" src="js/buttons.jqueryui.min.js"></script>
	<script type="text/javascript">
	$(function(){
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
				data : parameter
			},
			columns : [{
				"title" : "批次請求",
				"data" : null,
				"defaultContent" : ""
			}, {
				"title" : "訂單編號",
				"data" : "order_no",
				"defaultContent" : ""
			},{
				"title" : "產品編號",
				"data" : "v_c_product_id",
				"defaultContent" : ""
			}, {
				"title" : "產品名稱",
				"data" : "v_product_name",
				"defaultContent" : ""
			}, {
				"title" : "客戶姓名",
				"data" : "name",
				"defaultContent" : ""
			}, {
				"title" : "備註",
				"data" : "memo",
				"defaultContent" : ""
			}, {
				"title" : "出貨方式",
				"data" : "deliveryway",
				"defaultContent" : ""
			}, {
				"title" : "訂單總額",
				"data" : "total_amt",
				"defaultContent" : ""
			}, {
				"title" : "收件人姓名",
				"data" : "deliver_name",
				"defaultContent" : ""
			}, {
				"title" : "收件地點",
				"data" : "deliver_to",
				"defaultContent" : ""
			} ],
			columnDefs : [ {
				targets : 0,
				searchable : false,
				orderable : false,
				render : function(data, type, row) {
					var ship_seq_no = row.ship_seq_no;

					var input = document.createElement("INPUT");
					input.type = 'checkbox';
					input.name = 'checkbox-group-select';
					input.id = ship_seq_no;

					var span = document.createElement("SPAN");
					span.className = 'form-label';

					var label = document.createElement("LABEL");
					label.htmlFor = ship_seq_no;
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
					var $table =  $('#dt_master_ship');
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
					var $table =  $('#dt_master_ship');

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
						url: 'ship.do', 
						type: 'post',
						data: {
							action: 'sendToTelegraph',
							ship_seq_nos: noArr
						},
						error: function (xhr) { },
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
					console.log(noArr);				
				}
			},{
				text : '發送取消電文',
				action : function(e, dt, node, config) {
					var $table =  $('#dt_master_ship');

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
						url: 'ship.do', 
						type: 'post',
						data: {
							action: 'sendToCancelSaleOrderService',
							ship_seq_nos: noArr
						},
						error: function (xhr) { },
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
					console.log(noArr);				
				}
			}]
		});		
	});	
	</script>
</body>
</html>