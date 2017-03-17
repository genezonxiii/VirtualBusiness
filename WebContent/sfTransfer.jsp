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
	
		<!-- Styles -->
		<link rel="stylesheet" href="css/jqueryFiler/jquery.filer.css">
		<link rel="stylesheet" href="css/jqueryFiler/themes/jquery.filer-dragdropbox-theme.css">
		<link rel="stylesheet" href="css/sfTransfer.css">
	
		<!-- Jvascript -->
		<script src="js/jqueryFiler/jquery.filer.min.js" type="text/javascript"></script>
		<script src="js/sfTransfer.js" type="text/javascript"></script>
	</head>
	<body >
		<div class="content-wrap" >
			<div>
				<div class="form-wrap">
					<div align="center">	
						<div class="form-row">
							<select id='select-type'>
								<option value="0">請選擇</option>
								<option value="inbound">入庫</option>
								<option value="outbound">出庫</option>
							</select>
						</div>
					</div>
					<div id ='fileDiv' align="center">	
						<div class='input-field-wrap jFiler-input-choose-btn black'>	
							<div class="form-row">
								<form action="" id="form" method="post" enctype="multipart/form-data" style="margin:0px;">
									<input type="file" id="file" name="file" accept=".csv,.xls,.xlsx" style="width: 300px" multiple/>
									<button class='btn btn-darkblue' id = 'uploadBtn' onclick="return upload();">上傳</button>
									<span id='download'></span>
									<div id="data"></div>
								</form>
							</div>
						</div>		
						<div class="form-row" id ="filesEdit">
						</div>
						
					</div>								
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