<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<jsp:include page="template.jsp" flush="true"/>
<!DOCTYPE html>
<html>
	<head>
		<title>轉檔作業</title>
		
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
<!-- 		<script type="text/javascript" src="js/jqueryFiler/custom.js"></script> -->
		<script type="text/javascript" src="js/upload-dialog-common.js"></script>
		<script type="text/javascript" src="js/page/invoiceImport.js"></script>
	
		<!-- Styles -->
		<link rel="stylesheet" href="css/jqueryFiler/jquery.filer.css">
		<link rel="stylesheet" href="css/jqueryFiler/themes/jquery.filer-dragdropbox-theme.css">
		<link rel="stylesheet" href="css/basicDataImport.css">
		<link rel="stylesheet" href="css/invoiceImport.css">
	</head>
	<body >
		<div class="content-wrap" >
			<div>
				<div class="form-wrap">
					<div align="center">	
						<div class="form-row">
							<select id='select-type' style="display: none">
								<option value="0">請選擇</option>
								<option value="invoiceTemplate" selected>發票</option> 	
							</select>
						</div>
					</div>
					<div id="btnArea" align="center"></div>
					<div class="dragandrophandler" id="fileDiv">
						<p align="center">請將您的發票檔案拖拉至此或<br/>直接點擊後選擇您要的發票檔案</p>
						<div class="form-row" id ="filesEdit"></div>
					</div>
					<div class="statusbarDiv" id ="statusbarDiv"></div>
				</div>
			</div>
		</div>		
		<div id="message">
			<div id="text" align="center"></div>
		</div>		
		<div id="status">
			<div id="text" align="center"></div>
		</div>
	</body>	
</html>