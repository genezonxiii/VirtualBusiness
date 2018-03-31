<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html>
<html>
<head>
<title>庫存報表</title>
<meta charset="utf-8">
<link rel="Shortcut Icon" type="image/x-icon" href="./images/Rockettheme-Ecommerce-Shop.ico" />
<link rel="stylesheet" href="css/styles.css" />
<link rel="stylesheet" href="css/jquery.dataTables.min.css" />
<link rel="stylesheet" href="css/buttons.dataTables.min.css" />
<link href="<c:url value="css/css.css" />" rel="stylesheet">
<link href="<c:url value="css/jquery.dataTables.min.css" />" rel="stylesheet">
<link rel="stylesheet" href="css/1.11.4/jquery-ui.css">
</head>
<body>
	<jsp:include page="template.jsp" flush="true"/>
	<div class="content-wrap" >
		<div class='bdyplane' style="opacity:0">
<script type="text/javascript" src="js/jquery-1.12.4.js"></script>
<script type="text/javascript" src="js/jquery.dataTables.min.js"></script>
<script type="text/javascript" src="js/dataTables.buttons.min.js"></script>
<script type="text/javascript" src="js/buttons.flash.min.js"></script>
<script type="text/javascript" src="js/jszip.min.js"></script>
<script type="text/javascript" src="js/pdfmake.min.js"></script>
<script type="text/javascript" src="js/vfs_fonts.js"></script>
<script type="text/javascript" src="js/buttons.html5.min.js"></script>
<script type="text/javascript" src="js/buttons.print.min.js"></script>
<script type="text/javascript" src="js/jquery-ui.js"></script>
<script type="text/javascript" src="js/jquery-migrate-1.4.1.min.js"></script>
<script type="text/javascript" src="js/jquery.validate.min.js"></script>
<script type="text/javascript" src="js/additional-methods.min.js"></script>
<script type="text/javascript" src="js/messages_zh_TW.min.js"></script>
<script type="text/javascript" src="js/jquery.table2excel.js"></script>

<script type="text/javascript" src="js/dataTables.buttons.min.js"></script>
<script type="text/javascript" src="js/buttons.jqueryui.min.js"></script>
<script type="text/javascript" src="js/jquery.scannerdetection.js"></script>
<script>
	
	$(function() {
		$(".bdyplane").animate({"opacity":"1"});
		
		$("#search_stock").click(function(){
			var param = {url: "./stock.do?", option:"action=report&kind=pdf"};
			open_report(param);
		});
		
		$("#download_stock").click(function(){
			var param = {url: "./stock.do?", option:"action=report&kind=xls"};
			download_report(param);
		});
		
		function open_report(param){
			$("<div></div>")
			.attr("align", "center")
			.dialog({
			    title: "報表",
			    modal: true,
			    width: "auto",
			    open: function(event, ui) {
			    	var iframeUrl=param.url + param.option;
			        $(this).parent().children().children('.ui-dialog-titlebar-close').hide();
			        $iframe = $("<iframe></iframe>")
			        	.attr("width", 850)
			        	.attr("height", 450)
			        	.attr("src", iframeUrl);
			        $(this).html( $iframe );
			    },
			    buttons: [{
			        text: "確認",
			        click: function() {
			        	$(this).dialog("close");
			        }
			    }]
			});
			
		}

		function download_report(param){
			
			$("<div></div>")
			.dialog({
			    title: "下載",
			    modal: true,
			    open: function(event, ui) {
			    	var iframeUrl=param.url + param.option; 
			    	$(this).parent().children().children('.ui-dialog-titlebar-close').hide();
			        $iframe = $("<iframe></iframe>")
			        	.attr("src", iframeUrl);
			        $(this).html( $iframe );
			        $(this).dialog("close");
			    }
			});
		}

	});	
</script>
		<div class="datalistWrap">
			<div class="input-field-wrap">
				<div class="form-wrap">
					<div class="form-row">
						<button class="btn btn-darkblue" id="search_stock">查詢</button>
						<button class="btn btn-darkblue" id="download_stock">下載</button>
					</div>
				</div><!-- /.form-wrap -->
			</div>
		</div>
	</div>
	</div>
</body>
</html>