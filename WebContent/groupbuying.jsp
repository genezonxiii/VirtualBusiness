<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<jsp:include page="template.jsp" flush="true"/>
<!DOCTYPE html>
<html>
	<head>
		<title>團購轉檔作業</title>
		
		<link rel="stylesheet" href="css/jquery.dataTables.min.css" />
		<link rel="stylesheet" href="css/buttons.dataTables.min.css"/>
		
		<script type="text/javascript" src="js/jquery-1.11.4.js"></script>
		<script type="text/javascript" src="js/jquery-ui.min.js"></script>
		<script type="text/javascript" src="js/jquery.dataTables.min.js"></script>
		<script type="text/javascript" src="js/dataTables.buttons.min.js"></script>
		
		<script src="./js/jquery.form.js"></script> 
		<!-- validation -->
		<script type="text/javascript" src="js/jquery.validate.min.js"></script>
		<script type="text/javascript" src="js/additional-methods.min.js"></script>
		<script type="text/javascript" src="js/messages_zh_TW.min.js"></script>
		
		<!-- Google Fonts -->
		<link href="https://fonts.googleapis.com/css?family=Roboto+Condensed" rel="stylesheet">
	
		<!-- Jvascript -->
		<script type="text/javascript" src="js/jqueryFiler/jquery.filer.min.js"></script>
		<script type="text/javascript" src="js/upload-dialog-common.js"></script>
		<script type="text/javascript" src="js/groupbuying.js"></script>
	
		<!-- Styles -->
		<link rel="stylesheet" href="css/jqueryFiler/jquery.filer.css">
		<link rel="stylesheet" href="css/jqueryFiler/themes/jquery.filer-dragdropbox-theme.css">
		<link rel="stylesheet" href="css/groupbuying.css">
	</head>
	<body >
		<div class="content-wrap" >
			<h2 align="center">請選擇轉檔平台</h2>
			<div class="dragandrophandler" id="fileDiv">
				<p align="center">請拖拉檔案至此或直接點擊後選擇檔案</p>
				<div class="form-row" id ="filesEdit"></div>
			</div>
			<div id ="selectDiv" align="center"></div>
			<div class="statusbarDiv" id ="statusbarDiv"></div>
			<div id="btnArea" align="center"></div>
			<div id='iconBtns'></div>
		</div>
		<div id ="choose-order-type"></div>		
		<div id="message">
			<div id="text"></div>
		</div>		
		<div id="status">
			<div id="text" align="center"></div>
		</div>
	</body>	
</html>