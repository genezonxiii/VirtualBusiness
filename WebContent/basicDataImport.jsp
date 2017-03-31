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
		<script type="text/javascript" src="js/basicDataImport.js"></script>
	
		<!-- Styles -->
		<link rel="stylesheet" href="css/jqueryFiler/jquery.filer.css">
		<link rel="stylesheet" href="css/jqueryFiler/themes/jquery.filer-dragdropbox-theme.css">
		<link rel="stylesheet" href="css/basicDataImport.css">
		
	<script>
// 		var fileName;
		
// 		$(function(){
// 			$('#fileDiv').hide();
			
// 			message_dialog =
// 				$('#message').dialog({
// 					draggable : true,
// 					resizable : false,
// 					autoOpen : false,
// 					modal : true,
// 					show : {
// 						effect : "blind",
// 						duration : 300
// 					},
// 					hide : {
// 						effect : "fade",
// 						duration : 300
// 					},
// 					width : 200,
// 					buttons : 
// 							[{
// 								text : '確認',
// 								click : function() {
// 									$(this).dialog("close");
// 								}
// 							}]
// 					});
			
// 			$('select').on('change', function() {
// 				$('#file').val('');
// 				if(this.value != 0){
// 					$('#fileDiv').show();
// 					var type = $('#select-type').val();
// 					$("#download").html('').append("<a href='./basicDataImport.do?action=download&type=" + type + "'class='btn btn-exec'>範本下載</a>");
// 				}else{
// 					$('#fileDiv').hide();
// 				}
// 			});
// 		});
		
// 		function importFile(){
// 			if($('#file').get(0).files.length === 0){
// 				$('#message').find("#text").val('').text("請選擇檔案匯入");
// 				message_dialog.dialog("open");
// 				return false;
// 			}
// 			var arrays = ($('#file').val().replace(/C:\\fakepath\\/i, '')).split('.');
// 			if(arrays[1]!='csv' && arrays[1]!='xls' && arrays[1]!='xlsx'){
// 				$('#message').find("#text").val('').html("匯入失敗!<br/>請確認格式是否正確!");
// 				message_dialog.dialog("open");
// 				$('#file').val('');
// 				return false;
// 			}
			
// 			fileName = $('#file').val().replace(/C:\\fakepath\\/i, '');
// 			var form = document.getElementById("form");
// 			var action = "basicDataImport.do"
// 				+"?action=transfer"
// 				+"&type=" + $('#select-type').val().replace('Template','')
// 				+"&filename=" + fileName;
// 			$(form).attr("action",action);
// 			return true;
// 		}
		
// 		$('#form').ajaxForm(function(result) {
// 			if(result=="false"){
// 				$('#message').find("#text").val('').html("匯入失敗!<br/>請確認格式是否正確!");
// 				message_dialog.dialog("open");
// 			}else{
// 				$('#file').val('');
// 				$('#message').find("#text").val('').html("匯入成功!");
// 				message_dialog.dialog("open");
// 			}

// 	    });
	</script>
	</head>
	<body >
		<div class="content-wrap" >
			<div>
				<div class="form-wrap">
					<div align="center">	
						<div class="form-row">
							<select id='select-type'>
								<option value="0">請選擇</option>
								<option value="supplyTemplate">供應</option>
								<option value="productTemplate">產品</option>
								<option value="packageTemplate">產品組合</option>
								<option value="contrastTemplate">產品對照</option>
							</select>
						</div>
					</div>
					<div id="btnArea" align="center"></div>
					<div class="dragandrophandler" id="fileDiv">
						<p align="center">Drag & Click Files Here</p>
						<div class="form-row" id ="filesEdit"></div>
					</div>
					<div class="statusbarDiv" id ="statusbarDiv"></div>					
<!-- 					<div id ='fileDiv' align="center"> -->
<!-- 						<div class='search-result-wrap'> -->
<!-- 						<div id='download'></div> -->
<!-- 						</div> -->
<!-- 						<div class='input-field-wrap jFiler-input-choose-btn black'>	 -->
<!-- 							<div class="form-row"> -->
<!-- 								<form action="" id="form" method="post" enctype="multipart/form-data" style="margin:0px;"> -->
<!-- 									<input type="file" id="file" name="file" accept=".csv,.xls,.xlsx" style="width: 300px"/> -->
<!-- 									<button class='btn btn-darkblue' id ='importBtn' onclick="return importFile()">匯入</button> -->
<!-- 								</form> -->
<!-- 							</div> -->
<!-- 						</div> -->
<!-- 					</div>								 -->
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