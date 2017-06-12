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
			<h2 class="page-title">儲位異動管理</h2>

			<div class="panel-content">
				<div class="datalistWrap">
					<div class="input-field-wrap">
						<div class="form-wrap">
							<div class="form-row">
								<form id='form_no'>
									<label for=""> <span class="block-label">儲位異動編號</span>
										<input type="text">
									</label>
									<button class="btn btn-darkblue">查詢</button>
								</form>
							</div>
							<div class="form-row">
								<form id='form_date'>
									<label for=""> <span class="block-label">儲位異動時間起日</span>
										<input type="text" class="input-date">
									</label> <label for=""> <span class="block-label">儲位異動時間迄日</span>
										<input type="text" class="input-date">
									</label>
									<button class="btn btn-darkblue">查詢</button>
								</form>
							</div>
						</div>
					</div>
				</div>
			</div>
			<div class="panel-content">
				<div class="datalistWrap">
					<div class="row search-result-wrap">
						<table id="stockmod-master-table" class="result-table"></table>
						<table id="stockmod-detail-table" class="result-table"></table>
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
			<input type="hidden" id="hidStockModId" value="">
		</div>
	</div>
	<jsp:include page="template/common_js.jsp" flush="true" />
	<script type="text/javascript" src="js/dataTables.buttons.min.js"></script>
	<script type="text/javascript" src="js/buttons.jqueryui.min.js"></script>
	<script>
	var modType;//stock mod 異動型態
	var productInfo;// stock detail 產品 select物件
	var locationInfo;// stock detail 儲位代碼 select物件
	var masterDT = null;
	var detailDT = null;
	var selectCount = 0;
	var actionMap = new Map();
		actionMap.set("新增儲位異動","insertStockMod");
		actionMap.set("修改儲位異動","updateStockMod");
		actionMap.set("刪除儲位主單","deleteStockMod");
		actionMap.set("批次刪除","deleteStockMod");
		actionMap.set("新增儲位異動明細","insertStockModDetail");
		actionMap.set("刪除儲位異動明細","deleteStockModDetail");
		actionMap.set("明細批次刪除","deleteStockModDetail");
		actionMap.set("清單","list");	
		
		$(function() {
			//儲位異動型態
			$.ajax({
				url : 'stockMod.do',
				type : 'post',
				data : {
					action : "getModType"
				},
				error : function(xhrs) {
				},
				success : function(response) {
					var json_obj = $.parseJSON(response);

					var select = document.createElement('select');
					select.name = 'stockmod_type';

					$(select).append($("<option>", {
						value : '請選擇',
						html :'請選擇'
					}));
					
					$(json_obj).each(function(i, item) {
						$(select).append($("<option>", {
							value : item.mod_type,
							html : item.mod_type
						}));
					});
					modType = select;
				}
			});
			//儲位異動明細 產品資訊
			$.ajax({
				url : 'stockMod.do',
				type : 'post',
				data : {
					action : "getDetailProductInfo"
				},
				error : function(xhrs) {
				},
				success : function(response) {
					var json_obj = $.parseJSON(response);
					console.log('pd info');
					console.log(response);
					var select = document.createElement('select');
					select.name = 'product_id';

					$(select).append($("<option>", {
						value : '請選擇',
						html :'請選擇'
					}));
					
					$(json_obj).each(function(i, item) {
						$(select).append($("<option>", {
							value : item.product_id,
							html : item.product_name
						}));
					});
					productInfo = select;
				}
			});
			//儲位異動明細 儲位代碼
			$.ajax({
				url : 'stockMod.do',
				type : 'post',
				data : {
					action : "getDetailLocationInfo"
				},
				error : function(xhrs) {
				},
				success : function(response) {
					var json_obj = $.parseJSON(response);
					console.log('pd info');
					console.log(response);
					var select = document.createElement('select');
					select.name = 'locationInfo_id';

					$(select).append($("<option>", {
						value : '請選擇',
						html :'請選擇'
					}));
					
					$(json_obj).each(function(i, item) {
						$(select).append($("<option>", {
							value : item.location_id,
							html : item.location_code
						}));
					});
					locationInfo = select;
				}
			});			
			$('#form_no').on("click", "button", function(e) {
				e.preventDefault();
				
				initDetailDT();
				
				var parameter = {
					action : "searchByNo",
					stockmodNo : $('#form_no input').val()
				};
				console.log(parameter);
				drawMasterTable(parameter);
			});
			$('#form_date').on("click", "button", function(e) {
				e.preventDefault();

				initDetailDT();
				
				var startDate = $('#form_date input:eq(0)').val();
				var endDate = $('#form_date input:eq(1)').val();
				var errorMes = '';
				var $mes = $('#message #text');
				if(startDate.replace(/-/g,"") > endDate.replace(/-/g,"")){
					errorMes += "訖日不可小於起日!<br>";
				}
				if(startDate == null || startDate.length == 0){
					errorMes += "請選擇起日!<br>";
				}
				else if(!dateValidationCheck(startDate)){
					errorMes += "起日格式不符!<br>";
				}
				if(endDate == null || endDate.length == 0){
					errorMes += "請選擇訖日!<br>";
				}
				else if(!dateValidationCheck(endDate)){
					errorMes += "訖日格式不符!<br>";
				}

				if(errorMes.length > 0){
					$mes.val('').html(errorMes);
					$('#message')
						.dialog()
						.dialog('option', 'title', '警告')
						.dialog("open");
					return false;
				}
				var parameter = {
					action : "searchByDate",
					startDate : $('#form_date input:eq(0)').val(),
					endDate : $('#form_date input:eq(1)').val()
				};
				console.log(parameter);
				drawMasterTable(parameter);
			});
			$("#stockmod-master-table").delegate(".btn_delete", "click", function(e) {
				e.preventDefault();

				var stockmodId = $(this).attr("name");
				
				var dialogId = "dialog-data-process";
				var formId = "dialog-form-data-process";
				var btnTxt_1 = "刪除儲位主單";
				var btnTxt_2 = "取消";
				var oWidth = 'auto';
				var url = 'stockMod.do';
				
				initDeleteDialog();
				drawDialog
					(dialogId, url, oWidth, formId, btnTxt_1, btnTxt_2)
					.data("stockmodId",stockmodId)
					.dialog("option","title",btnTxt_1)
					.dialog('open');
			});
			$("#stockmod-master-table").delegate(".btn_update", "click", function(e) {
				e.preventDefault();

				var dialogId = "dialog-data-process";
				var formId = "dialog-form-data-process";
				var btnTxt_1 = "修改儲位異動";
				var btnTxt_2 = "取消";
				var oWidth = 'auto';
				var oUrl = 'stockMod.do';
			    
				initDialog();
				
				var $form = $('#dialog-data-process #dialog-form-data-process');
				var $stockmod_no = $form.find('input[name=stockmod_no]');
				var $memo = $form.find('input[name=memo]');
				var $stockmod_time = $form.find('input[name=stockmod_time]');
				var $stockmod_type = $form.find('select[name=stockmod_type]');

				var row = $(this).closest("tr");
			    var data = masterDT.row(row).data();
				var stockmod_id = data.stockmod_id;
			    
			    console.log(data);
			    
			    $stockmod_no.val(data.stockmod_no);
			    $memo.val(data.memo);
			    $stockmod_time.val(data.stockmod_time);
			    $stockmod_type.val(data.stockmod_type);
			    
				drawDialog(dialogId, oUrl, oWidth, formId, btnTxt_1, btnTxt_2)
					.data("stockmod_id",stockmod_id)
					.dialog("option","title",btnTxt_1)
					.dialog('open');
			});
			$("#stockmod-master-table").delegate(".btn_list", "click", function(e) {
				e.preventDefault();

				initMasterDT();
				
				var row = $(this).closest("tr");
			    var data = masterDT.row(row).data();
				var stockmod_id = data.stockmod_id;
				
				$('#hidStockModId').val(stockmod_id);
				var parameter = {
					action : "searchDetailById",
					stockmodId : stockmod_id
				};
				console.log(parameter);
				drawDetailTable(parameter);
			});
			$("#stockmod-detail-table").delegate(".btn_delete_detail", "click", function(e) {
				e.preventDefault();
				var stockmodDetail_id = $(this).attr("name");
				var dialogId = "dialog-data-process";
				var formId = "dialog-form-data-process";
				var btnTxt_1 = "刪除儲位異動明細";
				var btnTxt_2 = "取消";
				var oWidth = 'auto';
				var url = 'stockMod.do';
				initDeleteDialog();
				drawDialog
					(dialogId, url, oWidth, formId, btnTxt_1, btnTxt_2)
					.data("stockmodDetail_ids",stockmodDetail_id)
					.dialog("option","title",btnTxt_1)
					.dialog('open');
			});
			
			
			
		});
	</script>
	<script>
		function initDialog() {

			var stockmod_no = "<td>&nbsp;儲位異動編號&nbsp;</td>"
					+ "<td>"
					+ "<input type='text' name='stockmod_no' disabled='disabled' value='系統自動產生'>"
					+ "</td>";

			var stockmod_time = "<td>&nbsp;儲位異動時間&nbsp;</td>"
					+ "<td><label>"
					+ "<input type='text' name='stockmod_time' class='input-date'>"
					+ "</label></td>";

			var stockmod_type = "<td>&nbsp;儲位異動型態&nbsp;</td>";
					
			var memo = "<td>&nbsp;備忘&nbsp;</td>" + "<td>"
					+ "<input type='text' name='memo' placeholder='請填寫備忘'>"
					+ "</td>";
					
			var dialog = document.getElementById("dialog-data-process-table");
			var form = document.getElementById("dialog-form-data-process");

			$(dialog).find('tr').remove();
			$(dialog).append(
					$('<tr></tr>').val('').html(stockmod_no + memo));
			$(dialog).append(
					$('<tr></tr>').val('').html(stockmod_time + stockmod_type).append($('<td/>').append(modType)));

			$(dialog).find(".input-date" ).datepicker(opt);
		}
		function initDetailDialog() {

			var location = "<td>&nbsp;儲位代碼&nbsp;</td>";

			var quantity = "<td>&nbsp;數量&nbsp;</td>"
					+ "<td><label>"
					+ "<input type='text' name='quantity'>"
					+ "</label></td>";

			var product = "<td>&nbsp;產品&nbsp;</td>";
					
			var memo = "<td>&nbsp;備忘&nbsp;</td>" + "<td>"
					+ "<input type='text' name='memo' placeholder='請填寫備忘'>"
					+ "</td>";
					
			var dialog = document.getElementById("dialog-data-process-table");
			var form = document.getElementById("dialog-form-data-process");

			$(dialog).find('tr').remove();
			$(dialog).append(
					$('<tr></tr>').val('').html(product).append($('<td/>').append(productInfo),location,$('<td/>').append(locationInfo)));
			$(dialog).append(
					$('<tr></tr>').val('').html('').append(quantity,memo));

			$(dialog).find(".input-date" ).datepicker(opt);
		}
		function drawDialog(dialogId, oUrl, oWidth, formId, btnTxt_1, btnTxt_2) {

			var dialog = document.getElementById(dialogId);
			var form = document.getElementById(formId);
			var action = actionMap.get(btnTxt_1);
			var $form = $('#dialog-data-process #dialog-form-data-process');
			var $mes = $('#message #text');
			var br= document.createElement("br")
			
			dataDialog = $(dialog).dialog({
				draggable : true,
				resizable : false,
				autoOpen : false,
				modal : true,
				width : oWidth,
				buttons : [ {
					text : btnTxt_1,
					click : function(e) {
						e.preventDefault();
						if(action == 'insertStockMod'){		
							$.ajax({
								url: 'stockMod.do', 
								type: 'post',
								data: {
									action : action,
									stockmodTime : $form.find('input[name="stockmod_time"]').val(),
									stockmodType : $form.find('select[name="stockmod_type"]').val(),
									memo : $form.find('input[name="memo"]').val()
								},
								error: function (xhr) { }, 
								success: function (response) {
									dataDialog.dialog('close');
									$mes.val('').html('').append(
										$('<p></p>').val('').html('新增成功'),
										br,
										$('<p></p>').val('').html('儲位異動編號為: '+response)
									);
									$('#message')
										.dialog()
										.dialog('option', 'title', '儲位異動編號')
										.dialog("open");
									
									masterDT.ajax.reload();
								}
							});
						}
						if(action == 'deleteStockMod'){
							$.ajax({
								url: 'stockMod.do', 
								type: 'post',
								data: {
									action : action,
									stockmodId : dataDialog.data('stockmodId')
								},
								error: function (xhr) { }, 
								success: function (response) {
									dataDialog.dialog('close');
									$mes.val('').html('').append(
										$('<p></p>').val('').html('刪除成功')
									);
									$('#message')
										.dialog()
										.dialog('option', 'title', '通知訊息')
										.dialog("open");

									masterDT.ajax.reload();
								}
							});							
						}
						if(action == 'updateStockMod'){
							$.ajax({
								url: 'stockMod.do', 
								type: 'post',
								data: {
									action : action,
									stockmodId : $(this).data('stockmod_id'),
									stockmodNo : $form.find('input[name="stockmod_no"]').val(),
									stockmodTime : $form.find('input[name="stockmod_time"]').val(),
									stockmodType : $form.find('select[name="stockmod_type"]').val(),
									memo : $form.find('input[name="memo"]').val()
								},
								error: function (xhr) { }, 
								success: function (response) {
									dataDialog.dialog('close');
									$mes.val('').html('').append(
											$('<p></p>').val('').html('修改成功'),
											br,
											$('<p></p>').val('').html('修改儲位異動編號為: '+response)
										);
									$('#message')
										.dialog()
										.dialog('option', 'title', '通知訊息')
										.dialog("open");
									
									masterDT.ajax.reload();
								}
							});							
						}
						if(action == 'deleteStockModDetail'){
							$.ajax({
								url: 'stockMod.do', 
								type: 'post',
								data: {
									action : action,
									stockmodDetail_ids : $(this).data('stockmodDetail_ids')
								},
								error: function (xhr) { }, 
								success: function (response) {
									dataDialog.dialog('close');
									$mes.val('').html('').append(
										$('<p></p>').val('').html(response)
									);
									$('#message')
										.dialog()
										.dialog('option', 'title', '通知訊息')
										.dialog("open");

									detailDT.ajax.reload();
								}
							});							
						}
						
						
						if(action=="insertStockModDetail"){	
							$.ajax({
								url: 'stockMod.do', 
								type: 'post',
								data: {
									action : action,
									stockmodId : $('#hidStockModId').val(),
									locationInfo_id : $form.find('select[name="locationInfo_id"]').val(),
									product_id : $form.find('select[name="product_id"]').val(),
									quantity : $form.find('input[name="quantity"]').val(),
									memo : $form.find('input[name="memo"]').val()
								},
								error: function (xhr) { }, 
								success: function (response) {
									dataDialog.dialog('close');
									$mes.val('').html('').append(
										$('<p></p>').val('').html(response)
									);
									$('#message')
										.dialog()
										.dialog('option', 'title', '執行結果')
										.dialog("open");
									
									detailDT.ajax.reload();
								}
							});
						
							
						}
						
					}
				}, {
					text : btnTxt_2,
					click : function() {
						$(form).trigger("reset");
						$(dialog).dialog("close");
					}
				} ],
				close : function() {
					$(form).trigger("reset");
				}
			});

			return dataDialog;
		}

		function drawMasterTable(parameter) {

			masterDT = $("#stockmod-master-table").DataTable({
				dom : "Blfr<t>ip",
			    scrollY: "290px",
				width : 'auto',
				lengthChange: false,
				scrollCollapse : true,
				destroy : true,
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
					url : "stockMod.do",
					dataSrc : "",
					type : "POST",
					data : parameter
				},
				columns : [ {
					"title" : "勾選",
					"data" : null,
					"defaultContent" : ""
				},{
					"title" : "儲位異動編號",
					"data" : "stockmod_no",
					"defaultContent" : ""
				}, {
					"title" : "儲位異動時間",
					"data" : "stockmod_time",
					"defaultContent" : ""
				}
				, {
					"title" : "儲位異動型態",
					"data" : "stockmod_type",
					"defaultContent" : ""
				}, {
					"title" : "處理狀態",
					"data" : "process_flag",
					"defaultContent" : ""
				}, {
					"title" : "備忘",
					"data" : "memo",
					"defaultContent" : ""
				}
				, {
					"title" : "功能",
					"data" : null,
					"defaultContent" : ""
				} ],
				columnDefs : [ {
					targets : 0,
					searchable : false,
					orderable : false,
					render : function(data, type, row) {
						var stockmod_id = row.stockmod_id;

						var input = document.createElement("INPUT");
						input.type = 'checkbox';
						input.name = 'checkbox-group-select';
						input.id = stockmod_id;
						
						var span = document.createElement("SPAN");
						span.className = 'form-label';

						var label = document.createElement("LABEL");
						label.htmlFor = row.stockmod_id;
						label.name = 'checkbox-group-select';
						label.style.marginLeft = '40%';
						label.appendChild(span);

						var options = $("<div/>").append(input, label);

						return options.html();
					}
				}, {
					//功能
					targets : -1,
					searchable : false,
					orderable : false,
					render : function(data, type, row) {

						var options = $("<div/>").append($("<div/>", {
							"class" : "table-row-func btn-in-table btn-gray"
						}).append($("<i/>", {
							"class" : "fa fa-ellipsis-h"
						})).append($("<div/>", {
							"class" : "table-function-list"
						}).append($("<button/>", {
							"class" : "btn-in-table btn-darkblue btn_update",
							"title" : "修改"
						}).append($("<i/>", {
							"class" : "fa fa-pencil"
						}))).append($("<button/>", {
							"class": "btn-in-table btn-green btn_list",
							"title": "清單"
						}).append( $("<i/>", {
							"class": "fa fa-pencil-square-o"
						}))).append($("<button/>", {
							"class" : "btn-in-table btn-alert btn_delete",
							"title" : "刪除",
							"name" : row.stockmod_id
						}).append($("<i/>", {
							"class" : "fa fa-trash"
						})))));

						return options.html();
					}
				} ],
				buttons : [ {
					text : '全選',
					action : function(e, dt, node, config) {

						selectCount++;
						console.log('selectCount: '+ selectCount);
						var $dtMaster =  $('#stockmod-master-table');
						var $checkboxs = $dtMaster.find('input[name=checkbox-group-select]');

						console.log('selectCount % 2 : ' + selectCount % 2);
						
						
						selectCount %2 != 1 ?
								$checkboxs.each(function() {
									$(this).prop("checked", false);
									$(this).removeClass("toggleon");
								}): 
								$checkboxs.each(function() {
									$(this).prop("checked", true);
									$(this).addClass("toggleon");
								});						
					}
				}, {
					text : '批次刪除',
					action : function(e, dt, node, config) {
						var $dtMaster =  $('#stockmod-master-table');
						var delArr = '';
						
						var $checkboxs = $dtMaster.find('input[name=checkbox-group-select]:checked');
						
						console.log($checkboxs);
						
						if($checkboxs.length == 0){
							alert('請至少選擇一筆資料');
							return false;
						}
						
						var dialogId = "dialog-data-process";
						var formId = "dialog-form-data-process";
						var btnTxt_1 = "批次刪除";
						var btnTxt_2 = "取消";
						var oWidth = 'auto';
						var url = 'stockMod.do';
						
						$checkboxs.each(function() {
							delArr += this.id + ',';
						});
						
						delArr = delArr.slice(0,-1);
						
						console.log("delArr:"+delArr);
						
						initDeleteDialog();
						drawDialog
							(dialogId, url, oWidth, formId, btnTxt_1, btnTxt_2)
							.data("stockmodId",delArr)
							.dialog("option","title","刪除"+ $checkboxs.length +"筆資料")
							.dialog("open");					
					}
				}, {
					text : '新增儲位異動',
					action : function(e, dt, node, config) {
						var dialogId = "dialog-data-process";
						var formId = "dialog-form-data-process";
						var btnTxt_1 = "新增儲位異動";
						var btnTxt_2 = "取消";
						var oWidth = 'auto';
						var oUrl = 'stockMod.do';

						initDialog();
						drawDialog(dialogId, oUrl, oWidth, formId, btnTxt_1, btnTxt_2)
							.dialog("option","title",btnTxt_1)
							.dialog('open');
					}
				} ]
			});
		};
		function drawDetailTable(parameter) {

			detailDT = $("#stockmod-detail-table").DataTable({
				dom : "frB<t>ip",
				scrollY : "290px",
				width : 'auto',
				scrollCollapse : true,
				destroy : true,
				pageLength: 20,
				language : {
					"url" : "js/dataTables_zh-tw.txt",
					"emptyTable" : "查無資料",
				},
				ajax : {
					url : "stockMod.do",
					dataSrc : "",
					type : "POST",
					data : parameter
				},
				initComplete: function(settings, json) {
			        $('div .dt-buttons').css({
			            'float': 'left',
			            'margin-left': '10px'
			        });
			        $('div .dt-buttons a').css('margin-left', '10px');
			    },
				columns : [ {
					"title" : "勾選",
					"data" : null,
					"defaultContent" : ""
				},{
					"title" : "產品名稱",
					"data" : "product_name",
					"defaultContent" : ""
				}, {
					"title" : "數量",
					"data" : "quantity",
					"defaultContent" : ""
				}, {
					"title" : "儲位儲位代碼",
					"data" : "location_code",
					"defaultContent" : ""
				}, {
					"title" : "儲位儲位說明",
					"data" : "location_desc",
					"defaultContent" : ""
				}, {
					"title" : "備忘",
					"data" : "memo",
					"defaultContent" : ""
				}, {
					"title" : "功能",
					"data" : null,
					"defaultContent" : ""
				} ],
				columnDefs : [ {
					targets : 0,
					searchable : false,
					orderable : false,
					render : function(data, type, row) {
						var stockmodDetail_id = row.stockmodDetail_id;

						var input = document.createElement("INPUT");
						input.type = 'checkbox';
						input.name = 'checkbox-group-select';
						input.id = stockmodDetail_id;

						var span = document.createElement("SPAN");
						span.className = 'form-label';

						var label = document.createElement("LABEL");
						label.htmlFor = stockmodDetail_id;
						label.name = 'checkbox-group-select';
						label.style.marginLeft = '40%';
						label.appendChild(span);

						var options = $("<div/>").append(input, label);

						return options.html();
					}
				}, {
					//功能
					targets : -1,
					searchable : false,
					orderable : false,
					render : function(data, type, row) {

						var options = $("<div/>").append($("<div/>", {
							"class" : "table-row-func btn-in-table btn-gray"
						}).append($("<i/>", {
							"class" : "fa fa-ellipsis-h"
						})).append($("<div/>", {
							"class" : "table-function-list"
						}).append($("<button/>", {
							"class" : "btn-in-table btn-alert btn_delete_detail",
							"title" : "刪除",
							"name" : row.stockmodDetail_id
						}).append($("<i/>", {
							"class" : "fa fa-trash"
						})))));

						return options.html();
					}
				} ],
				buttons : [ {
					text : '全選',
					action : function(e, dt, node, config) {

						selectCount++;
						console.log('selectCount: '+ selectCount);
						var $dtMaster =  $('#stockmod-detail-table');
						var $checkboxs = $dtMaster.find('input[name=checkbox-group-select]');

						console.log('selectCount % 2 : ' + selectCount % 2);
						
						
						selectCount %2 != 1 ?
								$checkboxs.each(function() {
									$(this).prop("checked", false);
									$(this).removeClass("toggleon");
								}): 
								$checkboxs.each(function() {
									$(this).prop("checked", true);
									$(this).addClass("toggleon");
								});						
					}
				}, {
					text : '批次刪除',
					action : function(e, dt, node, config) {
						var $dtMaster =  $('#stockmod-detail-table');
						var delArr = '';
						
						var $checkboxs = $dtMaster.find('input[name=checkbox-group-select]:checked');
						
						console.log($checkboxs);
						
						if($checkboxs.length == 0){
							alert('請至少選擇一筆資料');
							return false;
						}
						
						var dialogId = "dialog-data-process";
						var formId = "dialog-form-data-process";
						var btnTxt_1 = "明細批次刪除";
						var btnTxt_2 = "取消";
						var oWidth = 'auto';
						var url = 'stockMod.do';

						$checkboxs.each(function() {
							delArr += this.id + '~';
						});
						delArr.slice(0,-1);
						
						initDeleteDialog();
						drawDialog
							(dialogId, url, oWidth, formId, btnTxt_1, btnTxt_2)
							.data("stockmodDetail_ids",delArr)
							.dialog("option","title","刪除"+ $checkboxs.length +"筆資料")
							.dialog("open");					
					}
				}, {
					text : '新增儲位異動明細',
					action : function(e, dt, node, config) {
						var dialogId = "dialog-data-process";
						var formId = "dialog-form-data-process";
						var btnTxt_1 = "新增儲位異動明細";
						var btnTxt_2 = "取消";
						var oWidth = 'auto';
						var oUrl = 'stockMod.do';

						initDetailDialog();
						drawDialog(dialogId, oUrl, oWidth, formId, btnTxt_1, btnTxt_2)
							.dialog("option","title",btnTxt_1)
							.dialog('open');
					}
				} ]
			});
		};		
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
		function initDeleteDialog(){
			
			var message = "確認刪除資料嗎?";
				
			var dialog = document.getElementById("dialog-data-process-table");
			var form = document.getElementById("dialog-form-data-process");
			
			$(dialog).find('tr').remove();
			$(dialog).append($('<tr></tr>').val('').html(message));

		}
		function initMasterDT(){
			if(masterDT != null){
				masterDT.destroy();
				$('#stockmod-master-table').empty();
			}
		}
		function initDetailDT(){
			if(detailDT != null){
				detailDT.destroy();
				$('#stockmod-detail-table').empty();
			}
		}
	</script>
</body>
</html>