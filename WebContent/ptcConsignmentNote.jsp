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
<title>統一速達-託運單管理</title>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<link rel="Shortcut Icon" type="image/x-icon"
	href="./images/Rockettheme-Ecommerce-Shop.ico" />
<jsp:include page="template/common_css.jsp" flush="true" />
<link rel="stylesheet" href="css/ptcConsignmentNote.css">
<link rel="stylesheet" href="css/buttons.dataTables.min.css">
</head>
<body>
	<input type="hidden" id="glb_menu" value='<%=menu%>' />
	<input type="hidden" id="glb_privilege" value="<%=privilege%>" />

	<div class="page-wrapper">
		<jsp:include page="template/common_headfoot.jsp" flush="true" />

		<div class="content-wrap">
			<h2 class="page-title">託運單管理</h2>

			<div class="panel-content">
				<div class="datalistWrap">
					<div class="input-field-wrap">
						<div class="form-wrap">
							<div class="form-row">
								<form>
									<label for=""> <span class="block-label">訂單編號</span>
										<input type="text">
									</label>
									<label for=""> <span class="block-label">託運單號碼</span>
										<input type="text">
									</label>
									<button class="btn btn-darkblue">查詢</button>
								</form>
							</div>
						</div>
					</div>
					<div class="panel-content">
						<div class="datalistWrap">
							<div class="row search-result-wrap">
								<table id="consignment-note-table" class="result-table"></table>
							</div>
						</div>
					</div>					
				</div>
			</div>
		</div>
	</div>
	<jsp:include page="template/common_js.jsp" flush="true" />
	<script type="text/javascript" src="js/dataTables.buttons.min.js"></script>
	<script type="text/javascript" src="js/buttons.jqueryui.min.js"></script>
	<script type="text/javascript" src="js/buttons.colVis.min.js"></script>

	<script type="text/javascript">
		$('form').on('click', 'button', function(event){
			event.preventDefault();
			
			var $form = $('form');
			var order_no = $form.find('input[type=text]:eq(0)').val();
			var tracking_number = $form.find('input[type=text]:eq(1)').val();
	
			var parameter = {
					action: 'query_consignment_note',
					order_no: order_no,
					tracking_number: tracking_number
			};
			drawCNTable(parameter);
		});
	</script>
	<script type="text/javascript">
		function drawCNTable(parameter) {
			
			var table =
			$("#consignment-note-table").DataTable({
			    dom: "fr<t>ip",
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
				ajax : {
					url : "Egs.do",
					dataSrc : "",
					type : "POST",
					data : parameter
				},
			    columnDefs: [{
			        targets: 8,
			        searchable: false,
			        orderable: false,
			        render: function(data, type, row) {
			        	data =  parseInt(data);
			        	
			        	switch(data) {
			            case 1:
			                return "9~12時";
			                break;
			            case 2:
			                return "12~17時";
			                break;
			            case 3:
			                return "17~20時";
			                break;
			            default:
			               return "不限時";
			        	}
			        }
			    }],
				columns : [ {
	                "className":'details-control',
	                "orderable":false,
	                "data":null,
	                "defaultContent": ''
	            },{
					"title" : "連線契客代號",
					"data" : "customer_id",
					"defaultContent" : ""
				},{
					"title" : "託運單號碼",
					"data" : "tracking_number",
					"defaultContent" : ""
				},{
					"title" : "訂單編號",
					"data" : "order_no",
					"defaultContent" : ""
				},{
					"title" : "收件人姓名",
					"data" : "receiver_name",
					"defaultContent" : ""
				},{
					"title" : "寄件人姓名",
					"data" : "sender_name",
					"defaultContent" : ""
				},{
					"title" : "代收貨款金額",
					"data" : "product_price",
					"defaultContent" : ""
				},{
					"title" : "指定配達日期",
					"data" : "delivery_date",
					"defaultContent" : ""
				},{
					"title" : "指定配達時段",
					"data" : "delivery_timezone",
					"defaultContent" : ""
				},{
					"title" : "建立時間",
					"data" : "create_time",
					"defaultContent" : ""
				},{
					"title" : "列印時間",
					"data" : "print_time",
					"defaultContent" : ""
				},{
					"title" : "備註",
					"data" : "egs_comment",
					"defaultContent" : ""
				}]
			});
		    $('#consignment-note-table tbody').on('click', 'td.details-control', function () {
		        var tr = $(this).closest('tr');
		        var row = table.row( tr );
		 
		        if ( row.child.isShown() ) {
		            // This row is already open - close it
		            row.child.hide();
		            tr.removeClass('shown');
		        }
		        else {
		            // Open this row
		            row.child( format(row.data()) ).show();
		            tr.addClass('shown');
		        }
		    } );			
		};
		function format ( d ) {
		    // `d` is the original data object for the row
		    return '<table cellpadding="12" cellspacing="0" border="0" style="padding-left:50px;">'+
				'<tr>'+
            		'<td>託運單帳號:</td>'+
            		'<td>'+d.account_id+'</td>'+
        		'</tr>'+		    
		        '<tr>'+
		            '<td>收件人地址:</td>'+
		            '<td>'+d.receiver_address+'</td>'+
		        '</tr>'+
		        '<tr>'+
		            '<td>收件人地址的速達五碼郵遞區號:</td>'+
		            '<td>'+d.receiver_suda5+'</td>'+
		        '</tr>'+
		        '<tr>'+
		            '<td>速達七碼條碼:</td>'+
		            '<td>'+d.receiver_suda7+'</td>'+
		        '</tr>'+
		        '<tr>'+
		           	'<td>收件人行動電話:</td>'+
		           	'<td>'+d.receiver_mobile+'</td>'+
		       	'</tr>'+
		       	'<tr>'+
		           	'<td>收件人電話:</td>'+
		           	'<td>'+d.receiver_phone+'</td>'+
		       	'</tr>'+
		        '<tr>'+
		       		'<td>寄件人地址:</td>'+
		       		'<td>'+d.sender_address+'</td>'+
		   		'</tr>'+
		   		'<tr>'+
		       		'<td>寄件人地址的速達五碼郵遞區號:</td>'+
		       		'<td>'+d.sender_suda5+'</td>'+
		   		'</tr>'+
		       	'<tr>'+
		   			'<td>寄件人電話:</td>'+
		   			'<td>'+d.sender_phone+'</td>'+
				'</tr>'+
				'<tr>'+
		   			'<td>品名:</td>'+
		   			'<td>'+d.product_name+'</td>'+
				'</tr>'+
				'<tr>'+
					'<td>尺寸:</td>'+
					'<td>'+d.package_size+'</td>'+
				'</tr>'+
					'<tr>'+
					'<td>溫層:</td>'+
				'<td>'+d.temperature+'</td>'+
				'</tr>'+
				'<tr>'+
					'<td>距離:</td>'+
					'<td>'+d.distance+'</td>'+
				'</tr>'+
		    '</table>';
		}
	</script>	
</body>
</html>