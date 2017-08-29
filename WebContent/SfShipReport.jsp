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
		<title>順豐出貨報表</title>
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
				<h2 class="page-title">順豐出貨報表</h2>
	
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
			</div>
		</div>
		<jsp:include page="template/common_js.jsp" flush="true" />
		<script type="text/javascript" src="js/dataTables.buttons.min.js"></script>
		<script type="text/javascript" src="js/buttons.jqueryui.min.js"></script>
		<script type="text/javascript" src="js/buttons.colVis.min.js"></script>
		<script type="text/javascript">
		$(function() {
		    $('#form_no').on("click", "button", function(e) {
		        var order_no = $('#form_no input[name=order_no]').val();
		        var start_date = '';
		        var end_date = '';

		        open_sf_report(order_no, start_date, end_date);
		    });

		    $('#form_date').on("click", "button", function(e) {
		        var order_no = '';
		        var start_date = $('#form_no input[name=start_date]').val();
		        var end_date = $('#form_no input[name=end_date]').val();
		        open_sf_report(order_no, start_date, end_date);
		    });

		    function open_sf_report(order_no, start_date, end_date) {

		        var iframUrl = "./report.do?action=rptSfShip&start_date=" + start_date + "&start_date=" + end_date + "&ids=" + encodeURIComponent(order_no);

		        console.log(iframUrl);
		        $("#dialog_report_iframe").attr("src", iframUrl);
		        $("#dialog_report").dialog({
		            draggable: true,
		            resizable: false,
		            autoOpen: false,
		            height: "600",
		            width: "900",
		            modal: true,
		            closeOnEscape: false,
		            open: function(event, ui) {
		                $(".ui-dialog-titlebar-close", ui.dialog | ui).hide();
		            },
		            buttons: [{
		                text: "關閉",
		                click: function() {
		                    $("#dialog_report").dialog("close");
		                }
		            }]
		        })
		        $("#dialog_report").dialog("open");
		    }

		});
		
		</script>
	</body>
</html>
