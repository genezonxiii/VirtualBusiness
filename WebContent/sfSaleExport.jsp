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
		<title>順豐出庫報表</title>
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
				<h2 class="page-title">順豐出庫報表</h2>
	
				<div class="panel-content">
					<div class="datalistWrap">
						<div class="input-field-wrap">
							<div class="form-wrap">
								<div class="form-row">
									<form id = "sf_export">
										<label for=""> <span class="block-label">上傳起日</span> <input
											type="text" name="start_date" class='input-date'>
										</label>
										<div class='forward-mark'></div>
										<label for=""> <span class="block-label">上傳迄日</span> <input
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
		<!-- 報表 對話窗 -->
		<div id="dialog_report" class="dialog" align="center" style="display:none">
			<iframe src="" frameborder="0" id="dialog_report_iframe" width="850" height="450"></iframe>
		</div>
		<jsp:include page="template/common_js.jsp" flush="true" />
		<script type="text/javascript" src="js/dataTables.buttons.min.js"></script>
		<script type="text/javascript" src="js/buttons.jqueryui.min.js"></script>
		<script type="text/javascript" src="js/buttons.colVis.min.js"></script>
		<script type="text/javascript">
		$(function() {
			$("#sf_export").validate({
		        rules: {
		        	start_date: {
		        		required: true, dateISO: true
		            },
		            end_date: {
		            	required: true, dateISO: true
		            }
		        },
		        messages: {
		        	start_date: {
		                dateISO: "日期格式錯誤"
		            },
		            end_date: {
		                dateISO: "日期格式錯誤"
		            }
		        }
		    });
		    
		    $('#sf_export').on("click", "button", function(e) {
		    	e.preventDefault();
		        var start_date = $('#sf_export input[name=start_date]').val();
		        var end_date = $('#sf_export input[name=end_date]').val();
		        
		        if ($("#sf_export").valid()) {
		        	$("#dialog_report_iframe").attr("src", "./report.do?action=rptSfSale" 
						+ "&start_date=" + start_date + "&end_date=" + end_date);
		        }
		    });
		});
		</script>
	</body>
</html>
