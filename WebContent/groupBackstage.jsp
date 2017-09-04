<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<jsp:include page="template.jsp" flush="true"/>

<!DOCTYPE html>
<html>
	<head>
		<title>公司後台管理</title>

		<style>
			#group-backstage-table td {
			    text-align: center; /* center checkbox horizontally */
			    vertical-align: middle; /* center checkbox vertically */
			}		
			#group-backstage-table label{
			    left:65px;
			}
		</style>
		
		<link rel="stylesheet" href="css/jquery.dataTables.min.css" />
		<link rel="stylesheet" href="css/buttons.dataTables.min.css"/>
		
		<script type="text/javascript" src="js/jquery-1.11.4.js"></script>
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
				actionMap.set("清單","list");
				
			var tableThs =
				"<th colspan='1'>" +
					"<div style='display:inline'>" +
						"<button class='btn btn-darkblue' name='selectAll'>全選</button>" +
					"</div>" +
					"<div style='display:inline'>" +
						"<button class='btn btn-darkblue' name='batchDel'>批次刪除</button>" +			
					"</div>" +
				"</th>" +						
				"<th>公司名稱</th>" +
				"<th>統一編號</th>" +
				"<th>公司地址</th>" +
				"<th>公司電話</th>" +
				"<th>公司傳真</th>" +
				"<th>負責人</th>" +
				"<th>負責人電子信箱</th>" +
				"<th>負責人手機</th>" +
				"<th>電子發票路徑</th>" +
				"<th>使用期限</th>" +
				"<th>功能</th>" ;				
			
			var tableId = "group-backstage-table";
			
			var dom = "lfr<t>ip";
			
			var oUrl = "groupBackstage.do";		
			
			var columns = 
				[
					"批次刪除",
					"公司名稱","統一編號","公司地址",
					"公司電話","公司傳真","負責人",
					"負責人電子信箱","負責人手機",
					"電子發票路徑","使用期限","功能"
				];
			
			var hiddenColumns = 
				[
				 columns.indexOf("負責人電子信箱")	,
				 columns.indexOf("電子發票路徑")
				];
			
			var oColumnDefs =
		        [
		            {
		                targets: hiddenColumns,
		                visible: false,
		                searchable: false
		            },
		            {
						targets: 0,
						searchable: false,
					   	orderable: false,
					   	render: function ( data, type, row ) {
				   			var checkboxs =
							 	"<input type='checkbox' name='batchDel-checkbox-group' id = '" + row.group_id + "'>" +
						   		"<label for='" + row.group_id + "'><span class='form-label'>選取</span></label>";
							   
					 		return checkboxs;
						}
		            },{
						targets: -1,
					   	searchable: false,
					   	orderable: false,
					   	render: function ( data, type, row ) {
					   		var options =
					   			"<div class='table-row-func btn-in-table btn-gray'><i class='fa fa-ellipsis-h'></i>"+
								"	<div class='table-function-list' >"+
								"		<button class='btn-in-table  btn-darkblue btn_update' title='修改' id = '" + row.group_id + "'>" +
								"		<i class='fa fa-pencil'></i></button>"+
								"		<button class='btn-in-table btn-alert btn_delete' title='刪除' id = '" + row.group_id + "'>" +
								"		<i class='fa fa-trash'></i></button>"+
								"		<button class='btn-in-table btn-green btn_list' title='清單' id = '" + row.group_id + "'>" +
								"		<i class='fa fa-list'></i></button>"+
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
					{"data": "group_name" ,"defaultContent":""},
					{"data": "group_unicode" ,"defaultContent":""},
					{"data": "address" ,"defaultContent":""},
					{"data": "phone" ,"defaultContent":""},
					{"data": "fax" ,"defaultContent":""},
					{"data": "master" ,"defaultContent":""},
					{"data": "email" ,"defaultContent":""},
					{"data": "mobile" ,"defaultContent":""},
					{"data": "invoice_path" ,"defaultContent":""},
					{"data": "expired" ,"defaultContent":""},
					{"data": null ,"defaultContent":""}
				];				
		</script>
		
		<!-- for common method -->
		<script>
			function drawDataTable(tableId, dom, oUrl, oData, oColumnDefs, oColumns){
				console.log("drawDataTable start");
				
				var table = document.getElementById(tableId);
				
				dataTableObj = 
					$(table).DataTable({
						dom: dom,
						destroy: true,
						language: {"url": "js/dataTables_zh-tw.txt"},
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
										var jsonStr = "";
										
										if($(form).valid() && action != "delete" ){
											
											jsonStr = '{"action":"' + action + '",';
											
											$("input[name^=dialog_group_]").each(function(){
												
												var key = $(this).attr("name").replace(/dialog_group_/, "");
												var val = $(this).val();
												
												jsonStr += '"' + key + '":"' + val + '",'
											});
											
											if(group_id != null){
												jsonStr += '"group_id":"' + group_id + '",';
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
														"group_name": $(dialog).find("input[name='dialog_group_name']").val(),
														"group_unicode": $(dialog).find("input[name='dialog_group_unicode']").val()
													};
													
													rebuildTable(tableId, tableThs);
													drawDataTable(tableId, dom, oUrl, oData, oColumnDefs, oColumns);
													
													$(form).trigger("reset");
													$(dialog).dialog("close");
											    }
											});												
										}
										
										if(action === "delete"){
											
											var removeKey = "";	
											
											jsonStr = '{"action":"' + action + '",';
											
											if(group_id != null){
												var grds = group_id.split(";");
												
												if(grds.length === 1){
													removeKey += group_id;
													jsonStr += '"group_id":"' + group_id + '",';													
												}else{
													removeKey += group_id + ';';
													jsonStr += '"group_id":"';
													$.each(grds, function(index, value) {
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
				
				var grpName =  
					"<td>公司名稱</td>" + 
					"<td>" + 
						"<input type='text' name='dialog_group_name' placeholder='請填寫公司名稱'>" +
					"</td>";

				var grpUnicode =  
					"<td>統一編號</td>" + 
					"<td>" + 
						"<input type='text' name='dialog_group_unicode' placeholder='請填寫統一編號'>" +
					"</td>";
				
				var grpAddress =  
					"<td>公司地址</td>" + 
					"<td>" + 
						"<input type='text' name='dialog_group_address' placeholder='請填寫公司地址'>" +
					"</td>";		
					
				var grpPhone =  
					"<td>公司電話</td>" + 
					"<td>" + 
						"<input type='text' name='dialog_group_phone' placeholder='請填寫公司電話'>" +
					"</td>";

				var grpFax =  
					"<td>公司傳真</td>" + 
					"<td>" + 
						"<input type='text' name='dialog_group_fax' placeholder='請填寫公司傳真'>" +
					"</td>";

						
				var grpMaster =  
					"<td>負責人</td>" + 
					"<td>" + 
						"<input type='text' name='dialog_group_master' placeholder='請填寫負責人'>" +
					"</td>";
								
				var grpMobile =  
					"<td>負責人手機</td>" + 
					"<td>" + 
						"<input type='text' name='dialog_group_mobile' placeholder='請填寫負責人手機'>" +
					"</td>";
					
				var grpExpired =  
					"<td>使用期限</td>" + 
					"<td>" + 
						"<input type='text' class='input-date' name='dialog_group_expired' placeholder='請填寫使用期限'>" +
					"</td>";
					
				var grpEmail =  
					"<td>電子信箱</td>" + 
					"<td>" + 
						"<input type='text' name='dialog_group_email' placeholder='請填寫電子信箱'>" +
					"</td>";
						
				var grpInvoice_path =  
					"<td>電子發票路徑</td>" + 
					"<td>" + 
						"<input type='text' name='dialog_group_invoice_path' placeholder='請填寫電子發票路徑'>" +
					"</td>";
					
				
				var dialog = document.getElementById("dialog-data-process-table");
				var form = document.getElementById("dialog-form-data-process");
				
				$(dialog).find('tr').remove();
				$(dialog).append($('<tr></tr>').val('').html(grpName +  grpUnicode));
				$(dialog).append($('<tr></tr>').val('').html(grpAddress + grpMaster));
				$(dialog).append($('<tr></tr>').val('').html(grpPhone + grpMobile));
				$(dialog).append($('<tr></tr>').val('').html(grpFax + grpExpired));
				$(dialog).append($('<tr></tr>').val('').html(grpEmail + grpInvoice_path));
				
				$.extend(jQuery.validator.messages, {
				    required: "必填欄位"
				});
				
				$(form).validate({
				  	rules:{
				  		dialog_group_expired :{
				  			dateISO: true
				  		},
				  		dialog_group_name:{
						  	required: true
				  		},
				  		dialog_group_unicode:{
						  	required: true
				  		}
				  	}
				});
				
				var opt = {
						   dayNamesMin:["日","一","二","三","四","五","六"],
						   monthNames:["1","2","3","4","5","6","7","8","9","10","11","12"],
						   monthNamesShort:["1","2","3","4","5","6","7","8","9","10","11","12"],
						   prevText:"上月",
						   nextText:"次月",
						   weekHeader:"週",
						   showMonthAfterYear:true,
						   dateFormat:"yy-mm-dd",
						   changeYear: true,
						   changeMonth: true
						   };

				$(".input-date").datepicker(opt);
			}
			
			function formUtil(url){
			    var object = this;
			    object.form = $('<form action="'+url+'" target="output_frame" method="post" style="display:none;"></form>');

			    object.addParameter = function(parameter,value){
			        $("<input type='hidden' />")
			         .attr("name", parameter)
			         .attr("value", value)
			         .appendTo(object.form);
			    }
			    
			    object.send = function(){
					$( "body" ).append(object.form);
			        object.form.submit();
			    }
			}			
		</script>
		
		<!-- button listener -->
		<script>
			$(function(){
				
				mainDiv = $("#mainDiv");
				iframe = $("#iframeDiv");
				
				$("#search-group-backstage").click(function(e) {
					e.preventDefault();
					
					var oData = {
							"action": "search",
							"group_name": $("#search-group-name").val(),
							"group_unicode": $("#search-group-unicode").val()
					};
					
					rebuildTable(tableId, tableThs);
					drawDataTable(tableId, dom, oUrl, oData, oColumnDefs, oColumns);
				});
				
				//insert
				$("#create-group-backstage").click(function(e) {
					e.preventDefault();
					
					var dialogId = "dialog-data-process";
					var formId = "dialog-form-data-process";
					var btnTxt_1 = "新增";
					var btnTxt_2 = "取消";
					var oWidth = 950;
					var url = oUrl;
					
					//must be initialized to set dialog
					initGroupDialog();
					drawDialog
						(dialogId, url, oWidth, formId, btnTxt_1, btnTxt_2)
						.dialog("option","title","新增資料")
						.dialog("open");
				});

				//delete
				$("#group-backstage-table").delegate(".btn_delete", "click", function(e) {
					e.preventDefault();
					
					var group_id = $(this).attr("id");
					
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
						.data("group_id",group_id)
						.dialog("option","title","刪除資料")
						.dialog("open");
				});
				
				//batch delete
				$("#group-backstage-table").delegate("button[name='batchDel']", "click", function(e) {
					e.preventDefault();
					
					var group_ids = "";
					var name = "batchDel-checkbox-group";
					var group = $("input[name='" + name + "']:checked");
			
					console.log("select count: " + group.length);
					
	
					if(group.length != 0){
						group.each(function(i){
							group_ids += $(this).context.id + ";";
						});
						
						group_ids = group_ids.substring(0,group_ids.length - 1);
						console.log(group_ids);
						
						var group_id = group_ids;
						
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
							.data("group_id",group_id)
							.dialog("option","title","刪除資料")
							.dialog("open");
					}
				});
				
				//users list
				$("#group-backstage-table").delegate(".btn_list", "click", function(e) {
					e.preventDefault();

					mainDiv.hide();//hide parent page
					
					//Initialized and dynamically set iframe
					var iframes = document.querySelectorAll('iframe');
					for (var i = 0; i < iframes.length; i++) {
					    iframes[i].parentNode.removeChild(iframes[i]);
					}
					$('<iframe id= "groupUserList" scrolling="no" name = "output_frame" frameborder="0" style="width:100%;" ></iframe>').appendTo(iframe);
					
					$("#groupUserList").css('height','742');
					
					var id = $(this).attr("id");
					var url = "groupUserList.jsp";
					var frame = new formUtil(url);
					frame.addParameter('groupId',id);
					frame.send();
				});
				
				//update
				$("#group-backstage-table").delegate(".btn_update", "click", function(e) {
					e.preventDefault();
					
					var row = $(this).closest("tr");
					
					console.log(dataTableObj);
					
				    var data = dataTableObj.row(row).data();
				    
				    console.log(data);
		
					var group_id = $(this).attr("id");

					var dialogId = "dialog-data-process";
					var formId = "dialog-form-data-process";
					var btnTxt_1 = "修改";
					var btnTxt_2 = "取消";
					var oWidth = 950;
					var url = oUrl;
					
					var dialog = document.getElementById(dialogId);
					
					//must be initialized to set values
					initGroupDialog();
					
					//set val to update fields
					$(dialog).find("input[name='dialog_group_name']").val(data.group_name);
					$(dialog).find("input[name='dialog_group_unicode']").val(data.group_unicode);
					$(dialog).find("input[name='dialog_group_address']").val(data.address);
					$(dialog).find("input[name='dialog_group_phone']").val(data.phone);
					$(dialog).find("input[name='dialog_group_fax']").val(data.fax);
					$(dialog).find("input[name='dialog_group_mobile']").val(data.mobile);
					$(dialog).find("input[name='dialog_group_email']").val(data.email);
					$(dialog).find("input[name='dialog_group_master']").val(data.master);
					$(dialog).find("input[name='dialog_group_invoice_path']").val(data.invoice_path);
					$(dialog).find("input[name='dialog_group_expired']").val(data.expired);
					
					drawDialog
						(dialogId, url, oWidth, formId, btnTxt_1, btnTxt_2)
						.data("group_id",group_id)
						.dialog("option","title","修改資料")
						.dialog("open");	
				});
				
				var buttonCount = 0; // only for selectAll button
				
				//selectAll
				$("#group-backstage-table").delegate("button[name='selectAll']", "click", function(e) {
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
	<body >
		<div class="content-wrap" >
			<div id="mainDiv">
				<div class="input-field-wrap">
					<div class="form-wrap">			
						<div class="form-row">
							<label for="">
								<span class="block-label">公司名稱</span>
								<input type="text" id="search-group-name">
							</label>
						</div>
						<div class="form-row">
							<label for="">
								<span class="block-label">統一編號</span>
								<input type="text" id="search-group-unicode">
							</label>
						</div>
						<div class="form-row">
							<button class="btn btn-darkblue" id="search-group-backstage">查詢</button>
							<button class="btn btn-exec btn-wide" id="create-group-backstage">新增</button>
						</div>										
					</div>
				</div>
		
				<div class="row search-result-wrap" align="center">
					<div class="ui-widget">
						<table id="group-backstage-table" class="result-table">
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
			<div id="iframeDiv">
 				<iframe id= "groupUserList" scrolling="no" name = "output_frame" frameborder="0" style="width:100%;" ></iframe> 
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