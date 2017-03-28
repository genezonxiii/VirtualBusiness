<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%
request.setCharacterEncoding("UTF-8");
String groupId = (String) request.getSession().getAttribute("group_id"); 
String userId = (String) request.getSession().getAttribute("user_id"); 
String userName = (String) request.getSession().getAttribute("user_name"); 
String menu = (String) request.getSession().getAttribute("menu"); 
String privilege = (String) request.getSession().getAttribute("privilege");
%>
<!DOCTYPE html>
<html>
<head>
<title>順豐api測試介面</title>
<meta charset="utf-8">

<link rel="Shortcut Icon" type="image/x-icon" href="./images/Rockettheme-Ecommerce-Shop.ico" />
<link rel="stylesheet" href="css/photo/style.css">
<link rel="stylesheet" href="css/styles.css" />
<link href="css/jquery.dataTables.min.css" rel="stylesheet">
<link href="css/1.11.4/jquery-ui.css" rel="stylesheet">
</head>
<body>
<div class="page-wrap" >
	<div class="header" style="z-index:1;">
		<h1 ondblclick="location.href = './welcome.jsp';">智慧電商平台</h1>
		<div class="userinfo">
			<p>使用者<span><%= (request.getSession().getAttribute("user_name")==null)?"尚未登入?":request.getSession().getAttribute("user_name").toString() %></span></p>
			<a href='#' id="logout" class="btn-logout" >登出</a>
		</div>
	</div>
	
	<footer class="footer" style="z-index:1;">
		北祥股份有限公司 <span>服務電話：+886-2-2658-1910  |  傳真：+886-2-2658-1920</span>
	</footer>
	
	<h2 id="title" class="page-title" style="z-index:1"></h2>
	
	<div class="content-wrap" >
		<div class="input-field-wrap">
			<div class="form-wrap">
				<div class="form-row">
					<!-- <select id="api_option"></select> -->
					<input type="text" id="so_no">
					<input type="text" id="po_no">
				</div>
				<div class="btn-row">
					<!-- <button class="btn btn-exec btn-wide" id="send_api">查詢</button> -->
				</div>
			</div>
		</div>
			
		<div class="search-result-wrap">
			<div id="tabs" >
				<ul>
					<li><a href="#tabs-1"><span>本次電文</span></a></li>
					<li><a href="#tabs-2"><span>歷史電文</span></a></li>
				</ul>
			  	
			  	<div id="tabs-1">
					<textarea id="view_result" cols="125" rows="16"></textarea>
				</div>
				<div id="tabs-2">
					<textarea id="view_result_all" cols="125" rows="16"></textarea>
				</div>
			</div>
		</div>
	</div>
</div>
	
<div id="warning"></div>
<div class="sidenav"></div>

<input type="hidden" id="glb_menu" value='<%= menu %>' />
<input type="hidden" id="glb_privilege" value="<%= privilege %>" />	

<script type="text/javascript" src="js/jquery-1.12.4.js"></script>
<script type="text/javascript" src="js/jquery-ui.min.js"></script>
<script type="text/javascript" src="js/jquery.validate.min.js"></script>
<script type="text/javascript" src="js/additional-methods.min.js"></script>
<script type="text/javascript" src="js/messages_zh_TW.min.js"></script>
<script type="text/javascript" src="js/virtual_business/menu.js"></script>
<script>
$(function(){
		
	var api_option = {
		'genItemService': '建立/修改商品', 
		'genItemQueryService' : '查詢商品',
		'genPurchaseOrderService' : '建立入庫單',
		'genPurchaseOrderInboundQueryService' : '查詢入庫單明細',
		'genCancelPurchaseOrderInboundQueryService' : '取消入庫單',
		'genSaleOrderService' : '建立出庫單',
		'genSaleOrderOutboundDetailQueryService' : '查詢出庫單明細',
		'genSaleOrderStatusQueryService' : '查詢出庫單狀態',
		'genCancelSaleOrderService' : '取消出庫單'		
	};

	$( ".page-title").html('順豐api測試介面');
	$( "#tabs" ).tabs();
	$("#po_no, #so_no").hide();
	
	$("<button>")
		.addClass("btn btn-exec btn-wide btn-query")
		.text('查詢')
		.appendTo(".btn-row");
	
	$("<select>")
		.addClass("select_api_option")
		.prepend("<option value=''>請選擇</option>")
		.prependTo(".form-row");
	
	$.each(api_option, function(index, value){
		$(".select_api_option")
		.append("<option value='" + index + "'>" + value + "</option>");
	});
		
	$(".form-row").delegate(".select_api_option", 'change', function(e) {
		var chgValue = $( this ).val();
		if ( chgValue == "genPurchaseOrderInboundQueryService" ) {
			$("#po_no").show();
			$("#so_no").hide();
		} else if ( chgValue == "genSaleOrderStatusQueryService" ) {
			$("#po_no").hide();
			$("#so_no").show();
		} else {
			$("#po_no, #so_no").hide();
		}
	});
	
	$(".btn-row").delegate(".btn-query", 'click', function(e) {
		e.preventDefault();

		var selValue = $(".select_api_option").val();
		var so_no = $("#so_no").val();
		var po_no = $("#po_no").val();
		
		status_dialog
			.dialog('option', 'title', '提醒')
			.html("<p>電文傳送中，請稍候!!</p>")
			.dialog("open");
		
		$.ajax({
			url : "sfTransfer.do", 
			type : "POST", 
			data : {
            	action : selValue,
            	so : so_no,
            	po : po_no
            },
            success: function(result) {
            	$('#view_result').val(result);
            	$('#view_result_all').val($('#view_result_all').val() + '<br/><br/>' + result);
            	
            	$("#tabs").tabs("option", "active", 0);
            	
            	status_dialog.dialog("close");
            }
		});
	});
	
	var status_dialog =
		$('<div></div>').dialog({
			modal : true,
		    resizable : false,
			autoOpen : false,
			closeOnEscape: false,
		    open: function(event, ui) {
		        $(".ui-dialog-titlebar-close", ui.dialog | ui).hide();
		    }
		});
})
</script>
</body>
</html>