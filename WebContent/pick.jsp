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
<title>揀貨管理</title>
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
			<h2 class="page-title">揀貨管理</h2>

			<div class="panel-content">
				<div class="datalistWrap">
					<div class="input-field-wrap">
						<div class="form-wrap">
							<div class="form-row">
								<form id = "form_no">
									<label for=""> <span class="block-label">訂單編號</span> <input
										type="text" name="order_no">
									</label>
									<button class="btn btn-darkblue">查詢</button>
								</form>
							</div>
							<div class="form-row">
								<form id = "form_date">
									<label for=""> <span class="block-label">揀貨起日</span> <input
										type="text" name="start_date" class='input-date'>
									</label>
									<div class='forward-mark'></div>
									<label for=""> <span class="block-label">揀貨迄日</span> <input
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
						<table id="dt_master" class="result-table"></table>
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
	<!-- 揀貨明細對話窗-->
	<div id="dialog-sale-detail" class="dialog" align="center">
		<form name="dialog-form-sale-detail" id="dialog-form-sale-detail">
			<fieldset>
				<table id="dialog-sale-detail-table" class="result-table">
					<thead>
						<tr class="">
							<th>訂單編號</th>
							<th>自訂商品編號</th>
							<th>商品名稱</th>
							<th>數量</th>
							<th>儲位代碼</th>				
						</tr>
					</thead>
					<tfoot></tfoot>
					<tbody></tbody>
				</table>
			</fieldset>
		</form>
	</div> 
	
	<!-- 報表 對話窗 -->
	<div id="dialog_report" class="dialog" align="center">
		<iframe src="" frameborder="0" id="dialog_report_iframe" width="850" height="450"></iframe>
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
			var $orderNo = $('#form_no input[name=order_no]').val();
			console.log($.trim($orderNo).length);
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
				action : "searchPickByOrderNo",
				orderNo : $orderNo
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
				action : "searchPickByPickTimeDate",
				startDate : $startDate,
				endDate : $endDate
			};
			console.log(parameter);
			drawMasterTable(parameter);
		});
	    $('#dt_master').on('change', ':checkbox', function() {
	        $(this).is(":checked")?
	        	$(this).closest("tr").addClass("selected"):
	        	$(this).closest("tr").removeClass("selected");
	    });
	});
	</script>
	<script type="text/javascript">
	function drawMasterTable(parameter) {

		$dtMaster = $("#dt_master").DataTable({
		    dom: "frB<t>ip",
		    lengthChange: false,
		    pageLength: 20,
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
		        url: "Pick.do",
		        dataSrc: "",
		        type: "POST",
		        data: parameter
		    },
		    columns: [{
		        "title": "批次請求",
		        "data": null,
		        "defaultContent": ""
		    },{
		        "title": "揀貨編號",
		        "data": "pick_no",
		        "defaultContent": ""
		    }, {
		        "title": "產生揀貨時間",
		        "data": "pick_time",
		        "defaultContent": ""
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
		        	
		            var pick_no = row.pick_no;
		            var input = document.createElement("INPUT");
		            input.type = 'checkbox';
		            input.name = 'checkbox-group-select';
		            input.id = pick_no;

		            var span = document.createElement("SPAN");
		            span.className = 'form-label';

		            var label = document.createElement("LABEL");
		            label.htmlFor = pick_no;
		            label.name = 'checkbox-group-select';
		            label.style.marginLeft = '35%';
		            label.appendChild(span);

		            var options = $("<div/>").append(input, label);

		            return options.html();
		        }
		    },{
		        //功能
		        targets: -1,
		        searchable: false,
		        orderable: false,
		        render: function(data, type, row) {
		            var options = $("<div/>") //fake tag
		                .append($("<div/>", {
		                        "class": "table-row-func btn-in-table btn-gray"
		                    })
		                    .append($("<i/>", {
		                        "class": "fa fa-ellipsis-h"
		                    }))
		                    .append(
		                        $("<div/>", {
		                            "class": "table-function-list"
		                        })
		                        .append(
		                            $("<button/>", {
		                                "class": "btn-in-table btn-green btn_list",
		                                "title": "清單"
		                            })
		                            .append($("<i/>", {
		                                "class": "fa fa-pencil-square-o"
		                            }))
		                        ).append( 
										$("<button/>", {
											"class": "btn-in-table btn-darkblue btn_pick_report_list",
											"title": "產生揀貨單"
									}).append( $("<i/>", {"class": "fa fa fa-file-pdf-o"}) )
								).append( 
										$("<button/>", {
											"class": "btn-in-table btn-alert btn_ship_report_list",
											"title": "產生出貨單"
									}).append( $("<i/>", {"class": "fa fa fa-file-pdf-o"}) )
								)
									

		                    )
		                );

		            return options.html();
		        }
		    }],
		    buttons: [{
		            text: '全選',
		            action: function(e, dt, node, config) {

		                selectCount++;
		                var $table = $('#dt_master');
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
	$("#dt_master").on("click", ".btn_pick_report_list", function(e) {
		e.preventDefault();
		var row = $(this).closest("tr");
	    var data = $("#dt_master").DataTable().row(row).data();
		open_pick_report(data.pick_id);
	})
	
	$("#dt_master").on("click", ".btn_ship_report_list", function(e) {
		e.preventDefault();
		var row = $(this).closest("tr");
	    var data = $("#dt_master").DataTable().row(row).data();
	    open_ship_report(data.pick_no);
	})
	
	
	
	$("#dt_master").on("click", ".btn_list", function(e) {
		e.preventDefault();

		var row = $(this).closest("tr");
	    var data = $("#dt_master").DataTable().row(row).data();
	    var tblDetail = $("#dialog-sale-detail-table").DataTable({
			dom : "lfr<t>ip",
			destroy : true,
			language : {
				"url" : "js/dataTables_zh-tw.txt"
			},
			ajax : {
				url : "Pick.do",
				dataSrc : "",
				type : "POST",
				data : {
					"action" : "getDetail",
					"pick_id" : data.pick_id
				}
			},
			columns : [ 				
				{"data" : "order_no", "defaultContent" : ""},
				{"data" : "c_product_id", "defaultContent" : ""},
				{"data" : "v_product_name", "defaultContent" : ""},
				{"data" : "quantity", "defaultContent" : ""},
				{"data" : "v_location_code", "defaultContent" : ""},
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
					open: function(event, ui) {
				        $(".ui-dialog-titlebar-close", ui.dialog | ui).hide();
				    },
					width : 1200,
					buttons : [{
					text : "關閉",
					click : function() {
						$("#dialog-sale-detail").dialog("close");
					}
					}],
				close : function() {
					$("#dialog-form-sale-detail").trigger("reset");
				}
			});
			
			$("#dialog-sale-detail")
				.data("sale_id", data.sale_id);
		})
		
		function open_pick_report(id){
			open_report("pick_id",id)
		}
		function open_ship_report(id){
			open_report("pick_no",id)
		}
	
	function open_report(key,value){
		var iframUrl="./report.do?"+key+"="+value;
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
	
		
	</script>
	
</body>
</html>
