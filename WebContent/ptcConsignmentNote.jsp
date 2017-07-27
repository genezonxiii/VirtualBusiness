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
				</div>
			</div>
		</div>
	</div>
	<jsp:include page="template/common_js.jsp" flush="true" />
	<script type="text/javascript" src="js/dataTables.buttons.min.js"></script>
	<script type="text/javascript" src="js/buttons.jqueryui.min.js"></script>
	<script type="text/javascript">
	$('form').on('click', 'button', function(event){
		var $form = $('form');
		var order_no = $form.find('input[type=text]:eq(0)').val();
		var tracking_number = $form.find('input[type=text]:eq(1)').val();
		
        $.ajax({
            url: 'Egs.do',
            type: 'post',
            data: {
                action: 'query_consignment_note',
                order_no: order_no,
                tracking_number: tracking_number
            },
            beforeSend: function(){
        		 $(':hover').css('cursor','progress');
            },
            complete: function(){
        		 $(':hover').css('cursor','default');
            },
            error: function(xhr) {},
            success: function(response) {
            	console.log('response: '+ response);
            }
		});
	});
	</script>
</body>
</html>