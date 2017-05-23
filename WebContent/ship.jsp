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
			<h2 class="page-title">出貨管理</h2>

			<div class="panel-content">
				<div class="datalistWrap">
					<div class="input-field-wrap">
						<div class="form-wrap">
							<div class="form-row">
								<form>
									<label for=""> <span class="block-label">銷售起日</span> <input
										type="text" name="start_date" class='input-date'>
									</label>
									<div class='forward-mark'></div>
									<label for=""> <span class="block-label">銷售迄日</span> <input
										type="text" name="end_date" class='input-date'>
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
						<table id="dt_master_ship" class="result-table"></table>
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
	function drawMasterTable(parameter) {

		masterDT = $("#stockmod-master-table").DataTable({
			dom : "Blfr<t>ip",
			//scrollY : "200px",
			width : 'auto',
			scrollCollapse : true,
			destroy : true,
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
			columns : [ {
				"title" : "出貨流水編號",
				"data" : "ship_seq_no",
				"defaultContent" : ""
			}, {
				"title" : "訂單編號",
				"data" : "order_no",
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
			}, {
				"title" : "功能",
				"data" : null,
				"defaultContent" : ""
			}, {
				"title" : "批次刪除",
				"data" : null,
				"defaultContent" : ""
			} ],
			columnDefs : [ {
				targets : -1,
				searchable : false,
				orderable : false,
				render : function(data, type, row) {
					var stockmod_id = row.stockmod_id;

					var input = document.createElement("INPUT");
					input.type = 'checkbox';
					input.name = 'checkbox-group-select';
					input.id = stockmod_id;
					//input.setAttribute('data-on','false');

					var span = document.createElement("SPAN");
					span.className = 'form-label';

					var text = document.createTextNode('選取');
					span.appendChild(text);

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
				targets : 8,
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
					delArr.slice(0,-1);
					
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
	</script>
</body>
</html>
