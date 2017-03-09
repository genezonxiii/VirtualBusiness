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
	
		<!-- Jvascript -->
		<script src="js/jqueryFiler/jquery.filer.min.js" type="text/javascript"></script>
		<script src="js/jqueryFiler/custom.js" type="text/javascript"></script>

	<script>
		var fileName;
		
		$(function(){
			$('#fileDiv').hide();
			
			message_dialog =
				$('#message').dialog({
					draggable : true,
					resizable : false,
					autoOpen : false,
					modal : true,
					show : {
						effect : "blind",
						duration : 300
					},
					hide : {
						effect : "fade",
						duration : 300
					},
					width : 200,
					buttons : 
							[{
								text : '確認',
								click : function() {
									$(this).dialog("close");
								}
							}]
					});
			
			$('select').on('change', function() {
				$('#file').val('');
				$("#download").html("");
				if(this.value != 0){
					$('#fileDiv').show();
				}else{
					$('#fileDiv').hide();
				}
			});
			
			$('#file').click(function(){
				$("#download").html("");
			});
		});
		
		function upload(){
			if($('#file').get(0).files.length === 0){
				$('#message').find("#text").val('').text("請選擇檔案");
				message_dialog.dialog("open");
				return false;
			}
			var arrays = ($('#file').val().replace(/C:\\fakepath\\/i, '')).split('.');
			if(arrays[1]!='csv' && arrays[1]!='xls' && arrays[1]!='xlsx'){
				$('#message').find("#text").val('').html("轉檔失敗!<br/>請確認格式是否正確!");
				message_dialog.dialog("open");
				$('#file').val('');
				return false;
			}
			
			fileName = $('#file').val().replace(/C:\\fakepath\\/i, '');
			var form = document.getElementById("form");
			var action = "sfTransfer.do"
				+"?action=upload"
				+"&type=" + $('#select-type').val()
				+"&filename=" + fileName;
			$(form).attr("action",action);
			return true;
		}
		
		$('#form').ajaxForm(function(result) {
			if(result=="false"){
				$('#message').find("#text").val('').html("轉檔失敗!<br/>請確認格式是否正確!");
				message_dialog.dialog("open");
				$("#download").html("");
			}else{
				$("#download").html("");
				$("#download").append("&nbsp;&nbsp;&nbsp;<a href='./sfTransfer.do?action=download&fileName="+fileName+"&downloadName="+$('#select-type').val()+"_"+result+"'class='btn btn-primary'>檔案下載</a>");
			}

	    });
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
								<option value="inbound">入庫</option>
								<option value="outbound">出庫</option>
							</select>
						</div>
					</div>
					<div id ='fileDiv' align="center">	
						<div class='input-field-wrap jFiler-input-choose-btn black'>	
							<div class="form-row">
								<form action="" id="form" method="post" enctype="multipart/form-data" style="margin:0px;">
									<input type="file" id="file" name="file" accept=".csv,.xls,.xlsx" style="width: 300px"/>
									<button class='btn btn-darkblue' id = 'uploadBtn' onclick="return upload();">上傳</button>
									<span id='download'></span>
								</form>
							</div>
						</div>
					</div>								
				</div>
			</div>
		</div>
		<div id="message">
			<div id="text" align="center"></div>
		</div>
	</body>	
</html>