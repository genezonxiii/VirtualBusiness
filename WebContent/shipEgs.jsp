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
<title>黑貓託運單列印作業</title>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<link rel="Shortcut Icon" type="image/x-icon"
	href="./images/Rockettheme-Ecommerce-Shop.ico" />
<jsp:include page="template/common_css.jsp" flush="true" />
<link rel="stylesheet" href="css/ship.css">
<link rel="stylesheet" href="css/buttons.dataTables.min.css">
</head>
<body>
	<input type="hidden" id="glb_menu" value='<%=menu%>' />
	<input type="hidden" id="glb_privilege" value="<%=privilege%>" />

	<div class="page-wrapper">
		<jsp:include page="template/common_headfoot.jsp" flush="true" />

		<div class="content-wrap">
			<h2 class="page-title">黑貓託運單列印作業</h2>

			<div class="panel-content">
				<div class="datalistWrap">
					<div class="input-field-wrap">
						<div class="form-wrap">
<!-- 							<div class="form-row"> -->
<!-- 								<form id = "form_no"> -->
<!-- 									<label for=""> <span class="block-label">訂單編號</span> <input -->
<!-- 										type="text" name="order_no"> -->
<!-- 									</label> -->
<!-- 									<button class="btn btn-darkblue">查詢</button> -->
<!-- 								</form> -->
<!-- 							</div> -->
							<div class="form-row">
								<form id = "form_date">
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
<!-- 							<div class="form-row"> -->
<!-- 								<form id ="form_waybill"> -->
<!-- 									<label for="">  -->
<!-- 										<span class="block-label">順豐託運單號</span>  -->
<!-- 										<input type="text" name="waybill_no"/> -->
<!-- 									</label> -->
<!-- 										<button class="btn btn-darkblue">查詢</button> -->
<!-- 								</form> -->
<!-- 							</div> -->
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
			<div id="message">
				<div id="text"></div>
			</div>
		</div>
	</div>
	
	<!-- 報表 對話窗 -->
	<div id="dialog_report" class="dialog" align="center" style="display:none">
		<iframe src="" frameborder="0" id="dialog_report_iframe" width="850" height="450"></iframe>
	</div>
	<!-- 銷貨明細對話窗-->
	<div id="dialog-sale-detail" class="dialog" align="center" style="display:none">
		<form name="dialog-form-sale-detail" id="dialog-form-sale-detail">
			<fieldset>
				<table id="dialog-sale-detail-table" class="result-table">
					<thead>
						<tr class="">
							<th>訂單編號</th>
							<th>自訂商品編號</th>
							<th>商品名稱</th>
							<th>數量</th>
							<th>單價</th>
							<th>備註</th>														
						</tr>
					</thead>
					<tfoot></tfoot>
					<tbody></tbody>
				</table>
			</fieldset>
		</form>
	</div>
	<!-- 順豐出貨狀態對話窗-->
	<div id="dialog-sf-status" style="display:none">
		<table id="dialog-sf-status-table" class="result-table"></table>
	</div>
	<!-- 順豐出貨明細對話窗-->
	<div id="dialog-sf-detail-status" style="display:none">
		<table id="dialog-sf-detail-status-table" class="result-table"></table>
	</div>		
	<!-- 下訂單對話窗-->
	<div id="dialog-sf-delivery-order" style="display:none">
		<form id ="dialog-sf-delivery-order-form">
			<fieldset>
				<table class='form-table'>
					<tr>
						<td>重量</td><td><input type="text" name="weight" placeholder="單位(千克)"></td>
					</tr>
				</table>
			</fieldset>
		</form>
	</div> 
	<!-- 黑貓對話窗-->
	<div id="dialog-egs" style="display:none">
		<form id ="dialog-egs-form">
			<h4>份數</h4>
			<input type="text" name="copy" value="1">
						
			<section>
				<hr><h4>託運單類別</h4>
				<div class="form-wrap">
					<div class="form-row">
						<input id="waybill-type-A" type="radio" name="waybill-type-radio-group" checked>
						<label for="waybill-type-A">
							<span class="form-label">一般</span>
						</label>
	          			<input id="waybill-type-B" type="radio" name="waybill-type-radio-group">
	          			<label for="waybill-type-B">
							<span class="form-label">代收</span>
	          			</label>
	          			<!-- 報值先關起來
	          			<input id="waybill-type-G" type="radio" name="waybill-type-radio-group">
	          			<label for="waybill-type-G">
							<span class="form-label">報值</span>
	          			</label>
	          			 -->
					</div>
				</div>
			</section>
			
			<section>
				<hr><h4>尺寸</h4>
				<div class="form-wrap">
					<div class="form-row">
						<input id="package-size-0001" type="radio" name="package-size-radio-group" checked>
						<label for="package-size-0001">
							<span class="form-label">60cm</span>
						</label>
						
	          			<input id="package-size-0002" type="radio" name="package-size-radio-group">
	          			<label for="package-size-0002">
							<span class="form-label">90cm</span>
	          			</label>
	          			
	          			<input id="package-size-0003" type="radio" name="package-size-radio-group">
	          			<label for="package-size-0003">
							<span class="form-label">120cm</span>
	          			</label>
	          			
	          			<input id="package-size-0004" type="radio" name="package-size-radio-group">
	          			<label for="package-size-0004">
							<span class="form-label">150cm</span>
	          			</label>
					</div>
				</div>
			</section>

			<section>
				<hr><h4>溫層</h4>
				<div class="form-wrap">
					<div class="form-row">
						<input id="temperature-0001" type="radio" name="temperature-radio-group" checked>
						<label for="temperature-0001">
							<span class="form-label">常溫</span>
						</label>
						
	          			<input id="temperature-0002" type="radio" name="temperature-radio-group">
	          			<label for="temperature-0002">
							<span class="form-label">冷藏</span>
	          			</label>
	          			
	          			<input id="temperature-0003" type="radio" name="temperature-radio-group">
	          			<label for="temperature-0003">
							<span class="form-label">冷凍</span>
	          			</label>

					</div>
				</div>
			</section>
			
			<section>
				<hr><h4>指定配達時段</h4>
				<div class="form-wrap">
					<div class="form-row">
						<input id="delivery-timezone-1" type="radio" name="delivery-timezone-radio-group" checked>
						<label for="delivery-timezone-1">
							<span class="form-label">13時前</span>
						</label>
	          			<input id="delivery-timezone-2" type="radio" name="delivery-timezone-radio-group">
	          			<label for="delivery-timezone-2">
							<span class="form-label">14~18時</span>
	          			</label>
	          			
	          			<input id="delivery-timezone-4" type="radio" name="delivery-timezone-radio-group">
	          			<label for="delivery-timezone-4">
							<span class="form-label">不指定</span>
	          			</label>
					</div>
				</div>
			</section>
			
			<hr><h4>指定配達日期</h4>
			<input type="text" name="delivery-date" class="self-date">
			
			<hr><h4>備註</h4>
			<input type="text" name="comment">
		</form>
	</div> 
	
	
	<!--對話窗樣式-新增 -->
	<div id="dialog-form-select" title="請選擇託運單" style="display:none;">
		<table class="form-table">
			<tbody>
				<tr>
					<td>託運單類別：</td>
					<td>
						<select id="select_report">
						  <option value="ezcat_a4_2_pick">A4 二模 揀貨單</option>
						  <option value="ezcat_a4_2">A4 二模</option>
						  <option value="ezcat">EZCAT</option>
						</select>
					</td>
				</tr>
			</tbody>
		</table>		
	</div>

	<jsp:include page="template/common_js.jsp" flush="true" />
	<script type="text/javascript" src="js/dataTables.buttons.min.js"></script>
	<script type="text/javascript" src="js/buttons.jqueryui.min.js"></script>
	<script type="text/javascript" src="js/buttons.colVis.min.js"></script>
	
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
			var $orderNo = $('#form_no input[name=order_no]').val();
			
			if($.trim($orderNo).length == 0){
				errorMes += "請輸入訂單編號!";
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
				action : "searchByOrderNo",
				orderNo : $orderNo
			};
			
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
				action : "searchEgsBetween",
				startDate : $startDate,
				endDate : $endDate
			};
			
			drawMasterTable(parameter);
		});
		
		$('#form_waybill').on("click", "button", function(e) {
			e.preventDefault();
		
			var errorMes = '';
			var $mes = $('#message #text');
			var $waybill_no = $('#form_waybill input[name=waybill_no]').val();
			
			if($waybill_no.length<=0){
				errorMes='請輸入託運單號';
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
				action : "searchByWaybill_no",
				waybill_no : $waybill_no,
			};
			
			drawMasterTable(parameter);
		});
		
	    $('#dt_master_ship').on('change', ':checkbox', function() {
	        $(this).is(":checked")?
	        	$(this).closest("tr").addClass("selected"):
	        	$(this).closest("tr").removeClass("selected");
	    });
	    
	    
	});
	</script>
	<script type="text/javascript">
	function drawMasterTable(parameter) {

		$dtMaster = $("#dt_master_ship").DataTable({
		    dom: "frB<t>ip",
		    lengthChange: false,
		    pageLength: 50,
		    scrollY: "290px",
		    width: 'auto',
		    scrollCollapse: true,
		    destroy: true,
		    language: {
		        "url": "js/dataTables_zh-tw.txt",
		        "emptyTable": "查無資料",
		    },
		    initComplete: function(settings, json) {
		        $('div .dt-buttons').css({
		            'float': 'left',
		            'margin-left': '10px'
		        });
		        $('div .dt-buttons a').css('margin-left', '10px');
		    },
		    ajax: {
		        url: "ship.do",
		        dataSrc: "",
		        type: "POST",
		        data: parameter
		    },
		    columns: [{
		        "title": "批次請求",
		        "data": null,
		        "defaultContent": ""
		    }, {
		        "title": "訂單編號",
		        "data": "order_no",
		        "defaultContent": ""
		    }, {
		        "title": "收件人姓名",
		        "data": "deliver_name",
		        "defaultContent": ""
		    }, {
		        "title": "收件地址",
		        "data": "deliver_to",
		        "defaultContent": ""
		    }, {
		        "title": "託運類別",
		        "data": "v_waybill_type",
		        "defaultContent": ""
		    }, {
		        "title": "託運單號",
		        "data": "v_tracking_number",
		        "defaultContent": ""
		    }, {
		        "title": "溫層",
		        "data": "v_temperature",
		        "defaultContent": ""
		    }, {
		        "title": "尺寸",
		        "data": "v_package_size",
		        "defaultContent": ""
		    }, {
		        "title": "付款方式",
		        "data": "v_pay_kind",
		        "defaultContent": ""
		    }, {
		        "title": "付款狀態",
		        "data": "v_pay_status",
		        "defaultContent": ""
// 		    }, {
// 		        "title": "功能",
// 		        "data": null,
// 		        "defaultContent": ""
		    }],
		    columnDefs: [{
		        targets: 0,
		        searchable: false,
		        orderable: false,
		        render: function(data, type, row) {
		            var ship_seq_no = row.v_tracking_number;

		            var input = document.createElement("INPUT");
		            input.type = 'checkbox';
		            input.name = 'checkbox-group-select';
		            input.id = ship_seq_no;
		            input.className=row.v_tracking_number;

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
		    }, {
		        targets: 4,
		        render: function(data, type, row) {
		        	var result = "";
		            if (data == 'A'){
		            	result = "一般";
		            } else if (data == 'B'){
		            	result = "代收";
		            }

		            return result;
		        }
		    }, {
		        targets: 6,
		        render: function(data, type, row) {
		        	var result = "";
		            if (data == '0001'){
		            	result = "常溫";
		            } else if (data == '0002'){
		            	result = "冷藏";
		            } else if (data == '0003'){
		            	result = "冷凍";
		            }

		            return result;
		        }
		    }, {
		        targets: 7,
		        render: function(data, type, row) {
		        	var result = "";
		            if (data == '0001'){
		            	result = "60cm";
		            } else if (data == '0002'){
		            	result = "90cm";
		            } else if (data == '0003'){
		            	result = "120cm";
		            } else if (data == '0004'){
		            	result = "150cm";
		            }

		            return result;
		        }
// 		    }, {
// 		        //功能
// 		        targets: -1,
// 		        searchable: false,
// 		        orderable: false,
// 		        render: function(data, type, row) {
// 		            var options = $("<div/>") //fake tag
// 		                .append($("<div/>", {
// 		                        "class": "table-row-func btn-in-table btn-gray"
// 		                    })
// 		                    .append($("<i/>", {
// 		                        "class": "fa fa-ellipsis-h"
// 		                    }))
// 		                    .append(
// 		                        $("<div/>", {
// 		                            "class": "table-function-list"
// 		                        })
// 		                        .append(
// 		                            $("<button/>", {
// 		                                "id": row.seq_no,
// 		                                "value": row.sale_id,
// 		                                "name": row.c_product_id,
// 		                                "class": "btn-in-table btn-green btn_list",
// 		                                "title": "清單"
// 		                            })
// 		                            .append($("<i/>", {
// 		                                "class": "fa fa-pencil-square-o"
// 		                            }))
// 		                        )
// 								.append(
// 		                            $("<button/>", {
// 		                                "id": row.seq_no,
// 		                                "value": row.sale_id,
// 		                                "name": row.c_product_id,
// 		                                "class": "btn-in-table  btn-alert btn_ship_report",
// 		                                "title": "託運單"
// 		                            })
// 		                            .append($("<i/>", {
// 		                                "class": "fa fa-file-pdf-o"
// 		                            }))
// 		                        )
// 								.append(
// 			                            $("<button/>", {
// 				                            "class": "btn-in-table btn-primary btn_sf_list",
// 			                                "title": "順豐出貨狀態"
// 			                            })
// 			                            .append($("<i/>", {
// 				                            "class": "fa fa-list"
// 			                            }))
// 			                    )
// 								.append(
// 			                            $("<button/>", {
// 				                            "class": "btn-in-table btn-exec btn_sf_detail_list",
// 			                                "title": "順豐出貨明細"
// 			                            })
// 			                            .append($("<i/>", {
// 				                            "class": "fa fa-list"
// 			                            }))
// 			                    )
		                        
// 		                    )
// 		                );

// 		            return options.html();
// 		        }
		    }],
		    buttons: [{
		            text: '全選',
		            action: function(e, dt, node, config) {

		                selectCount++;
		                var $table = $('#dt_master_ship');
		                var $checkboxs = $table.find('input[name=checkbox-group-select]');

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
		            text: '託運單列印',
		            action: function(e, dt, node, config) {
		                var $table = $('#dt_master_ship');

		            	var track = new Map(); //儲存託運單
		            	var track_list = '';
		            	
		                var cells = $dtMaster.cells().nodes();
						var row;
						var data;
						var message = '';

		                var $checkboxs = $(cells).find('input[name=checkbox-group-select]:checked');

		                if ($checkboxs.length == 0) {
		                    alert('請至少選擇一筆資料');
		                    return false;
		                }

						$checkboxs.each(function() {
							row = $(this).closest("tr");
							data = $table.DataTable().row(row).data();
							track.set( data.v_tracking_number, data.v_tracking_number );
						});

						track.forEach(function(value, index, fullArray){
							track_list+= (','+ value);
						});
						if(track_list.length != 0){
							track_list = track_list.substring( 1, track_list.length);
						}
						console.log('track_list');
						console.log(track_list);
						console.log('track');
						console.log(track);

					    dialog_select = $("#dialog-form-select").dialog({
							draggable : true, resizable : false, autoOpen : false,
							height : "auto", width : "auto", modal : true,
							show : {effect : "blind",duration : 300},
							hide : {effect : "fade",duration : 300},
							open : function(event, ui) {
								$(this).parent().children().children('.ui-dialog-titlebar-close').hide();
								$("#select_report")[0].selectedIndex = 0;
							},
							buttons : [{
										id : "choose",
										text : "確定",
										click : function() {
											var report_type=$("#select_report").val();
											 open_report2(track_list,report_type);
												dialog_select.dialog("close");
										}
									}, {
										text : "取消",
										click : function() {
											dialog_select.dialog("close");
										}
									} ]
						}).css("width", "10%");
					    dialog_select.dialog("open");
							
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
	
	function open_report(key,value,key1,value1,modeltype){
		
		var iframUrl="./report.do?"+key+"="+encodeURIComponent(value)+"&"+key1+"="+value1+"&type=ship_report&modeltype="+modeltype;

		console.log(iframUrl);
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
	

	function open_report2(track_list, modeltype){
		
		var iframUrl="./report.do?"
				+ "track_list="+encodeURIComponent(track_list)
				+ "&type=reportEzcat"
				+ "&modeltype="+modeltype;

		$("#dialog_report_iframe").attr("src", iframUrl);
		
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
		 .dialog("open"); 	
	}
	
	$("#dt_master_ship").on("click", ".btn_ship_report", function(e) {
		var row = $(this).closest("tr");
	    var data = $("#dt_master_ship").DataTable().row(row).data();
	    
	    
	    dialog_select = $("#dialog-form-select").dialog({
			draggable : true, resizable : false, autoOpen : false,
			height : "auto", width : "auto", modal : true,
			show : {effect : "blind",duration : 300},
			hide : {effect : "fade",duration : 300},
			open : function(event, ui) {
				$(this).parent().children().children('.ui-dialog-titlebar-close').hide();
				$("#select_report")[0].selectedIndex = 0;
			},
			buttons : [{
						id : "choose",
						text : "確定",
						click : function() {
							var report_type=$("#select_report").val();
							 open_report("order_no",data.order_no,"address",data.deliver_to,report_type);
								dialog_select.dialog("close");
						}
					}, {
						text : "取消",
						click : function() {
							dialog_select.dialog("close");
						}
					} ]
		}).css("width", "10%");
	    dialog_select.dialog("open");
	});

	$("#dt_master_ship").on("click", ".btn_list", function(e) {
		e.preventDefault();

		var row = $(this).closest("tr");
	    var data = $("#dt_master_ship").DataTable().row(row).data();
	    var tblDetail = $("#dialog-sale-detail-table").DataTable({
			dom : "fr<t>ip",
			destroy : true,
			language : {
				"url" : "js/dataTables_zh-tw.txt"
			},
			ajax : {
				url : "realsale.do",
				dataSrc : "",
				type : "POST",
				data : {
					"action" : "getRealSaleDetail",
					"realsale_id" : data.realsale_id
				}
			},
			columns : [ 				
				{"data" : "order_no", "defaultContent" : ""},
				{"data" : "c_product_id", "defaultContent" : ""},
				{"data" : "product_name", "defaultContent" : ""},
				{"data" : "quantity", "defaultContent" : ""},
				{"data" : "price", "defaultContent" : ""},
				{"data" : "memo", "defaultContent" : ""}
			]})
			
			$("#dialog-sale-detail").dialog({
				title: "資料明細",
				draggable : true,
				resizable : false,
				modal : true,
				autoOpen: true,
					show : {
						effect : "blind",
						duration : 500
					},
				width : 1200,
				close : function() {
					$("#dialog-form-sale-detail").trigger("reset");
				},
				buttons: [{
			        text: "關閉",
			        click: function() {
			            $(this).dialog("close");
			        }
			    }]
			});
			
			$("#dialog-sale-detail")
				.data("sale_id", data.sale_id);
		});
	</script>
</body>
</html>
