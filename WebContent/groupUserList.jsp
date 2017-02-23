<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html>
<html>
	<head>
		<title>公司人員管理</title>
		
		<style>
			#group-users-table td {
			    text-align: center; /* center checkbox horizontally */
			    vertical-align: middle; /* center checkbox vertically */
			}		
			#group-users-table label{
			    left:65px;
			}
		</style>
		
		<link rel="stylesheet" href="vendor/css/font-awesome.min.css">
		<link rel="stylesheet" href="css/jquery.dataTables.min.css" />
		<link rel="stylesheet" href="css/buttons.dataTables.min.css"/>
		<link rel="stylesheet" href="vendor/css/jquery-ui.min.css">
		<link rel="stylesheet" href="css/styles.css">
	
		<script type="text/javascript" src="js/jquery-1.12.4.js"></script>
		<script type="text/javascript" src="js/jquery-ui.min.js"></script>
		<script type="text/javascript" src="js/jquery.dataTables.min.js"></script>
		<script type="text/javascript" src="js/dataTables.buttons.min.js"></script>
		<!-- validation -->
		<script type="text/javascript" src="js/jquery.validate.min.js"></script>
		<script type="text/javascript" src="js/additional-methods.min.js"></script>
		<script type="text/javascript" src="js/messages_zh_TW.min.js"></script>
				
		<!-- for default parameters -->
		<script>
			
			var dataTableObj; // for set DataTable
			
			var actionMap = new Map();
				actionMap.set("查詢","search");
				actionMap.set("新增","insert");
				actionMap.set("修改","update");
				actionMap.set("刪除","delete");
				
			var tableThs =
				"<th colspan='1'>" +
					"<div style='display:inline'>" +
						"<button class='btn btn-darkblue' name='selectAll'>全選</button>" +
					"</div>" +
					"<div style='display:inline'>" +
						"<button class='btn btn-darkblue' name='batchDel'>批次刪除</button>" +			
					"</div>" +
				"</th>" +						
				"<th>人員名稱</th>" +
				"<th>人員角色</th>" +
				"<th>人員信箱</th>" +
				"<th>功能</th>" ;				
			
			var tableId = "group-users-table";
			
			var dom = "Blfr<t>ip";
			
			var oUrl = "groupUserList.do";		
			
			var columns = 
				[
					"批次刪除",
					"人員名稱","人員角色","人員信箱","功能"
				];
			
			var buttons =
			{
		        buttons: [
		            { 
		            	text: ' 公司管理',
		            	className: 'fa fa-reply',
		            	action: function ( e, dt, node, config ) {
							var url = "groupBackstage.jsp";
							console.log($(parent.iframe).find('iframe'));
							$(parent.mainDiv).show();
							$(parent.iframe).find('iframe').html('');
							var iframes = parent.document.querySelectorAll('iframe');
							for (var i = 0; i < iframes.length; i++) {
							    iframes[i].parentNode.removeChild(iframes[i]);
							}
		                }
		            },
		            { 
		            	text: ' 新增人員',
		            	className: 'fa fa-male',
		            	action: function ( e, dt, node, config ) {
							var dialogId = "dialog-data-process";
							var formId = "dialog-form-data-process";
							var btnTxt_1 = "新增";
							var btnTxt_2 = "取消";
							var oWidth = 950;
							var url = oUrl;
							
							//must be initialized to set dialog
							initGroupDialog();
							drawDialog(dialogId, url, oWidth, formId, btnTxt_1, btnTxt_2)
								.data("group_id",$("#hiddenGrd").val())
								.dialog("option","title","新增人員")
								.dialog("open");
		                }
		            }		            
		        ]
		    }
			
			var oColumnDefs =
		        [
		            {
						targets: 0,
						searchable: false,
					   	orderable: false,
					   	render: function ( data, type, row ) {
				   			var checkboxs =
							 	"<input type='checkbox' name='batchDel-checkbox-group' id = '" + row.user_id + "'>" +
						   		"<label for='" + row.user_id + "'><span class='form-label'>選取</span></label>";
							   
					 		return checkboxs;
						}
		            },
		            {
						targets: 2,
					   	render: function ( data, type, row ) {
							   
					 		return row.role === "1" ? "管理者" : "使用者";
						}
		            },{
						targets: -1,
					   	searchable: false,
					   	orderable: false,
					   	render: function ( data, type, row ) {
					   		var options =
					   			"<div class='table-row-func btn-in-table btn-gray'><i class='fa fa-ellipsis-h'></i>"+
								"	<div class='table-function-list' >"+
								"		<button class='btn-in-table  btn-darkblue btn_update' title='修改' id = '" + row.user_id + "'>" +
								"		<i class='fa fa-pencil'></i></button>"+
								"		<button class='btn-in-table btn-alert btn_delete' title='刪除' id = '" + row.user_id + "'>" +
								"		<i class='fa fa-trash'></i></button>"+
								"	</div>"+
								"</div>";
							   
					 		return options;
					   }
					}				        
		        ];
			var oColumns = 
				[
					{
					 "width": "200",
					 "className": "selectBox",
					 "data": null,
					 "defaultContent": ""
					},
					{"data": "user_name" ,"defaultContent":""},
					{"data": null ,"defaultContent":""},
					{"data": "email" ,"defaultContent":""},
					{"data": null ,"defaultContent":""}
				];				
		</script>
		
		<!-- for common method -->
		<script>
			function drawDataTable(tableId, dom, oUrl, oData, oColumnDefs, oColumns, buttons){
				console.log("userlist drawDataTable start");
				console.log(oUrl);
				console.log(oData);
				var table = document.getElementById(tableId);
				dataTableObj = 
					$(table).DataTable({
						dom: dom,
						destroy: true,
						language: {"url": "js/dataTables_zh-tw.txt"},
						buttons: buttons,
						ajax: {
								url : oUrl,
								dataSrc: "",
								type : "POST",
								data : oData
						},
				        columnDefs: oColumnDefs
						,
						columns: oColumns
					});	
			}
			
			function rebuildTable(tableId, tableThs){
				
				var table = document.getElementById(tableId);
				
				$(table).find("thead").find("tr").remove();
				$(table).find("thead").append($("<tr></tr>").val("").html(tableThs));
				
				$(table).find("tfoot").find('tr').remove();
				$(table).find("tfoot").append($("<tr></tr>").val("").html(tableThs));				
			}
			
			function drawDialog(dialogId, oUrl , oWidth, formId, btnTxt_1, btnTxt_2){
				
				var dialog = document.getElementById(dialogId);
				var form = document.getElementById(formId);
				
				dataDialog = 
					$(dialog).dialog({
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
						width : oWidth,
						buttons : 
								[{
									text : btnTxt_1,
									click : function(e) {
										e.preventDefault();
										
										var action = actionMap.get(btnTxt_1);
										var group_id = $(this).data("group_id");
										var user_id = $(this).data("user_id");
										var jsonStr = "";
										
										if(action == "update"){
											var settings = $(form).validate().settings;
											delete settings.rules.dialog_password;
										}
										
										if($(form).valid() && action != "delete"  ){
											jsonStr = '{"action":"' + action + '",';
											
											$(dialog).find("[name^=dialog_]").each(function(){
												
												var key = $(this).attr("name").replace(/dialog_/, "");
												var val = $(this).val();
												
												jsonStr += '"' + key + '":"' + val + '",'
											});
											
											if(group_id != null){
												jsonStr += '"group_id":"' + group_id + '",';
											}
											
											if(user_id != null){
												jsonStr += '"user_id":"' + user_id + '",';
											}
											
											jsonStr = jsonStr.replace(/,$/,"}");
											
											console.log(jsonStr);
											
											$.ajax({
											    url : oUrl,
											    type : "POST",
											    cache : false,
											    delay : 0,
											    data : $.parseJSON(jsonStr),
											    success: function(data) {
													var oData = {
														"action": "search",
														"group_id": group_id
													};
													
													rebuildTable(tableId, tableThs);
													drawDataTable(tableId, dom, oUrl, oData, oColumnDefs, oColumns, buttons);

													$("#groupUserList").css('height', '742');
													$(form).trigger("reset");
													$(dialog).dialog("close");
											    }
											});												
										}
										
										if(action === "delete"){
											
											var removeKey = "";	
											
											jsonStr = '{"action":"' + action + '",';
											
											if(user_id != null){
												var usds = user_id.split(";");
												
												if(usds.length === 1){
													removeKey += user_id;
													jsonStr += '"user_id":"' + user_id + '",';													
												}else{
													removeKey += user_id + ';';
													jsonStr += '"user_id":"';
													$.each(usds, function(index, value) {
														jsonStr +=  value + ';';
													});
													jsonStr = jsonStr.replace(/;$/,'",');
												}
											}
											
											jsonStr = jsonStr.replace(/,$/,"}");
											
											console.log(jsonStr);
											
											$.ajax({
											    url : oUrl,
											    type : "POST",
											    data : $.parseJSON(jsonStr),
											    success: function(data) {
											    	var removeKeys = removeKey.split(";");
											    	for(var i = 0; i<removeKeys.length; i++){
											    		var element = document.getElementById(removeKeys[i]);
														dataTableObj
												        .row($(element).parents("tr"))
												        .remove();										    		
											    	}
											    	dataTableObj.draw();
													$(dialog).dialog("close");	
											    }
											});											
										}
									}
								},{
									text : btnTxt_2,
									click : function() {
										$(form).trigger("reset");
										$(dialog).dialog("close");
									}
								}],
						close : function() {
							$(form).trigger("reset");
						}
					});
				
				return dataDialog;
			}
			
			function initDeleteDialog(){
				
				var message = "確認刪除資料嗎?";
					
				var dialog = document.getElementById("dialog-data-process-table");
				var form = document.getElementById("dialog-form-data-process");
				
				$(dialog).find('tr').remove();
				$(dialog).append($('<tr></tr>').val('').html(message));

			}
			
			function initGroupDialog(){
				
				var user_name =  
					"<td>人員名稱</td>" + 
					"<td>" + 
						"<input type='text' name='dialog_user_name' placeholder='請填寫人員名稱'>" +
					"</td>";

				var role =  
					"<td>人員角色</td>" + 
					"<td>" + 
						"<select name='dialog_role'>" +
						"<option value= 'default'>請選擇</option>" +
						"<option value='1'>管理者</option>" +
						"<option value='2'>使用者</option>" +
						"</select>"
					"</td>";
					
				var email =  
					"<td>人員信箱</td>" + 
					"<td>" + 
						"<input type='text' name='dialog_email' placeholder='請填寫人員信箱'>" +
					"</td>";
						
				var password =  
					"<td>人員密碼</td>" + 
					"<td>" + 
						"<input type='text' name='dialog_password' placeholder='請填寫人員密碼'>" +
					"</td>";
					
				
				var dialog = document.getElementById("dialog-data-process-table");
				var form = document.getElementById("dialog-form-data-process");
				
				$(dialog).find('tr').remove();
				$(dialog).append($('<tr></tr>').val('').html(email + user_name));
				$(dialog).append($('<tr></tr>').val('').html(password + role));
				
				//validate start
				$.extend(jQuery.validator.messages, {
				    required: "必填欄位"
				});
				
				$.validator.addMethod("valueNotEquals", function(value, element, arg){
					return arg != value;
				}, "請選擇");
				
				$.validator.addMethod("isEmailExist", function(value, element, arg){
					var isExist = false;

			    	console.log(isExist);
					var oData = {
							"action": "isEmailExist",
							"group_id": $("#hiddenGrd").val(),
							"email": value
					};
					
					$.ajax({
					    url : oUrl,
					    type : "POST",
			            async: false, 
					    cache : false,
					    delay : 0,
					    data : oData,
					    success: function(data) {
					    	console.log(data);
					    	isExist = (data == "true");
					    }
					});
			    	console.log(isExist);
			    	
					return arg != isExist;
					
				}, "此電子信箱已註冊");
				
				$(form).validate({
				  	rules:{
				  		dialog_user_name :{
						  	required: true
				  		},
				  		dialog_role:{
						  	required: true,
						  	valueNotEquals : "default"
				  		},
				  		dialog_email:{
						  	required: true,
						  	email: true,
						  	isEmailExist : true
				  		},
				  		dialog_password:{
						  	required: true
				  		}
				  	}
				});
			}
			
		
		</script>
		
		<!-- initialize the query -->
		<script>
		$(function(){
			if($("#hiddenGrd").val().length == 0){
				document.location.href="404.html";
			}
			var oData = {
					"action": "search",
					"group_id": $("#hiddenGrd").val()
			};
			rebuildTable(tableId, tableThs);
			drawDataTable(tableId, dom, oUrl, oData, oColumnDefs, oColumns, buttons);
			console.log(oData);
		});
		</script>
		
		<!-- button listener -->
		<script>
		$(function(){
			
			//delete
			$("#group-users-table").delegate(".btn_delete", "click", function(e) {
				e.preventDefault();
				
				var user_id = $(this).attr("id");
				
				var dialogId = "dialog-data-process";
				var formId = "dialog-form-data-process";
				var btnTxt_1 = "刪除";
				var btnTxt_2 = "取消";
				var oWidth = 250;
				var url = oUrl;
				
				//must be initialized
				initDeleteDialog();
				drawDialog
					(dialogId, url, oWidth, formId, btnTxt_1, btnTxt_2)
					.data("user_id",user_id)
					.dialog("option","title","刪除資料")
					.dialog("open");
			});
			
			//batch delete
			$("#group-users-table").delegate("button[name='batchDel']", "click", function(e) {
				e.preventDefault();
				
				var user_ids = "";
				var name = "batchDel-checkbox-group";
				var user = $("input[name='" + name + "']:checked");
		
				console.log("select count: " + user.length);
				

				if(user.length != 0){
					user.each(function(i){
						user_ids += $(this).context.id + ";";
					});
					
					user_ids = user_ids.substring(0,user_ids.length - 1);
					console.log(user_ids);
					
					var user_id = user_ids;
					
					var dialogId = "dialog-data-process";
					var formId = "dialog-form-data-process";
					var btnTxt_1 = "刪除";
					var btnTxt_2 = "取消";
					var oWidth = 250;
					var url = oUrl;
					
					//must be initialized
					initDeleteDialog();
					drawDialog
						(dialogId, url, oWidth, formId, btnTxt_1, btnTxt_2)
						.data("user_id",user_id)
						.dialog("option","title","刪除資料")
						.dialog("open");
				}
			});
			
			//update
			$("#group-users-table").delegate(".btn_update", "click", function(e) {
				e.preventDefault();
				
				var row = $(this).closest("tr");
			    var data = dataTableObj.row(row).data();
				var dialog = document.getElementById("dialog-data-process-table");
				var user_id = $(this).attr("id");
				var dialogId = "dialog-data-process";
				var formId = "dialog-form-data-process";
				var btnTxt_1 = "修改";
				var btnTxt_2 = "取消";
				var oWidth = 950;
				var url = oUrl;
				
				//must be initialized
				initGroupDialog();
				
				$(dialog).find("input[name='dialog_password']").attr("placeholder","如不需修改，則不用輸入");
				$(dialog).find("input[name='dialog_email']").val(data.email);
				$(dialog).find("input[name='dialog_email']").attr('disabled', true);
				$(dialog).find("input[name='dialog_user_name']").val(data.user_name);
				
				drawDialog
					(dialogId, url, oWidth, formId, btnTxt_1, btnTxt_2)
					.data("user_id",user_id)
					.data("group_id",$("#hiddenGrd").val())	
					.data("password",data.password)
					.dialog("option","title","修改資料")
					.dialog("open");
			});
			
			var buttonCount = 0; // only for selectAll button
			
			//selectAll
			$("#group-users-table").delegate("button[name='selectAll']", "click", function(e) {
				e.preventDefault();

				var name = "batchDel-checkbox-group";
				var group = document.getElementsByName(name);
				
				buttonCount++;
				
				if(buttonCount%2 === 1){
					$(group).each(function() {
					    $(this).prop("checked", true);
					});				
				}else{
					$(group).each(function() {
					    $(this).prop("checked", false);
					});
				}

			});			
		});
		</script>
	</head>
	<body>
		<div >
			<input type="hidden" id="hiddenGrd" value="${param.groupId}"/>
			<input type="hidden" id="hiddenHet" value="${param.iframeHeight}"/>
			
			<div class="row search-result-wrap" align="center" >
				<div class="ui-widget">
					<table id="group-users-table" class="result-table">
						<thead>
							<tr>
							</tr>
						</thead>
						<tfoot>
							<tr>
							</tr>
						</tfoot>
						<tbody>
						</tbody>
					</table>
				</div>
			</div>
		</div>
		<!-- 對話窗 -->
		<div id="dialog-data-process" class="dialog" align="center">
			<form name="dialog-form-data-process" id="dialog-form-data-process">
				<fieldset>
					<table class="form-table" id="dialog-data-process-table">
					</table>
				</fieldset>
			</form>
		</div>			
	</body>
</html>