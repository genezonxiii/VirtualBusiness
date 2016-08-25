<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ page import="tw.com.aber.productunit.controller.*"%>
<%@ page import="java.util.*"%>
<%@ page import="java.sql.DriverManager"%>
<%@ page import="java.sql.Connection"%>
<%@ page import="java.sql.Statement"%>
<%@ page import="java.sql.ResultSet"%>
<jsp:directive.page import="java.sql.SQLException" />
<!DOCTYPE html>
<html>
<head>
<title>廠商資料</title>
<meta charset="utf-8">
<link rel="Shortcut Icon" type="image/x-icon" href="./images/Rockettheme-Ecommerce-Shop.ico" />
<link rel="stylesheet" href="css/styles.css" />
<link href="<c:url value="css/css.css" />" rel="stylesheet">
<link href="<c:url value="css/jquery.dataTables.min.css" />" rel="stylesheet">
<link href="<c:url value="css/1.11.4/jquery-ui.css" />" rel="stylesheet">
</head>
<body>
	<jsp:include page="template.jsp" flush="true"/>
	<div class="content-wrap" class="content-wrap" >
<script type="text/javascript" src="js/jquery-1.10.2.js"></script>
<script type="text/javascript" src="js/jquery-1.11.4.js"></script>
<script type="text/javascript" src="js/jquery.dataTables.min.js"></script>
<script type="text/javascript" src="js/jquery-ui.min.js"></script>
<script type="text/javascript" src="js/jquery-ui.js"></script>
<script type="text/javascript" src="js/jquery.validate.min.js"></script>
<script type="text/javascript" src="js/additional-methods.min.js"></script>
<script type="text/javascript" src="js/messages_zh_TW.min.js"></script>
<script>
function draw_supply(info){
	$.ajax({
		type : "POST",
		url : "supply.do",
		data : info,
		success : function(result) {
				var json_obj = $.parseJSON(result);
				var result_table = "";
				$.each(json_obj,function(i, item) {
					result_table += 
						"<tr><td>"+ json_obj[i].supply_name+
						"</td><td>"+json_obj[i].supply_unicode+ 
						"</td><td>"+json_obj[i].address+
						"</td><td>"+json_obj[i].contact+
						"</td><td>"+json_obj[i].phone+
						"</td><td>"+json_obj[i].ext+
						"</td><td>"+json_obj[i].mobile+
						"</td><td>"+json_obj[i].email+
						"</td><td>"+json_obj[i].contact1+
						"</td><td>"+json_obj[i].phone1+
						"</td><td>"+json_obj[i].ext1+
						"</td><td>"+json_obj[i].mobile1+
						"</td><td>"+json_obj[i].email1+
						"</td><td>"+json_obj[i].memo+
						 "<td><div class='table-row-func btn-in-table btn-gray'><i class='fa fa-ellipsis-h'></i>"
						+ "	<div class='table-function-list'>"
						+ "		<button class='btn-in-table btn-darkblue btn_update' title='修改' value='"+ json_obj[i].supply_id+ "'name='"+ json_obj[i].supply_name+"' ><i class='fa fa-pencil'></i></button>"
						+ "		<button class='btn-in-table btn-alert btn_delete' title='刪除' id='"+json_obj[i].supply_name+"' value='"+ json_obj[i].supply_id+"' ><i class='fa fa-trash'></i></button>"
						+ "	</div></div></td></tr>";
				});
				//判斷查詢結果
				var resultRunTime = 0;
				$.each (json_obj, function (i) {
					resultRunTime+=1;
				});
				$("#products2").dataTable().fnDestroy();
				if(resultRunTime!=0){
					$("#products2-contain").show();
					$("#products2 tbody").html(result_table);
					$("#products2").dataTable({"language": {"url": "js/dataTables_zh-tw.txt"}});
					$(".validateTips").text("");
				}else{
					$("#products2-contain").hide();
					$(".validateTips").text("查無此結果");
				}
			}
		});
}
	$(function() {
		var information={
				action : "search",
				supply_name : $("input[name='searh_supply_name'").val(),
		};
		//=============自定義validator=============
		//字符最大長度驗證（一個中文字符長度為2）
		jQuery.validator.addMethod("stringMaxLength", function(value, element, param) { 
			var length = value.length; 
			for ( var i = 0; i < value .length; i++) { 
				if (value.charCodeAt(i) > 127) { 
				length++; 
				} 
			} 
			return this.optional(element) || (length <= param); 
		}, $.validator.format("長度不能大於{0}!"));
		//字母數字
		jQuery.validator.addMethod("alnum", function(value, element) {
		return this.optional(element) || /^[a-zA-Z0-9]+$/.test(value);
		}, "只能包括英文字母和數字");
		//=========================================
		//驗證
		//使用jquery.validate來做驗證  注意事項: 1.不能用選擇器方式批次設定，它只取最後一個參數 2.要調用resetForm()而不是reset()
		var validator_insert = $("#insert-dialog-form-post").validate({
				rules : {
					supply_name:{
						maxlength : 40,
						required : true
					},
					supply_unicode:{
						maxlength : 10,
					},
					address: {
						stringMaxLength : 100
					},
					contact: {
						stringMaxLength : 10
					},
					phone: {
						stringMaxLength : 12
					},
					ext: {
						stringMaxLength : 6
					},
					mobile: {
						stringMaxLength : 15
					},
					contact1: {
						stringMaxLength : 10
					},
					phone1: {
						stringMaxLength : 12
					},
					ext1: {
						stringMaxLength : 6
					},
					mobile1: {
						stringMaxLength : 15
					},
					memo: {
						stringMaxLength : 200
					}
				}
		});
		var validator_update = $("#update-dialog-form-post").validate({
			rules : {
				supply_name:{maxlength : 40,required : true},
				supply_unicode:{maxlength : 10,},
				address: {stringMaxLength : 100},
				contact: {stringMaxLength : 10},
				phone: {stringMaxLength : 12},
				ext: {stringMaxLength : 6},
				mobile: {stringMaxLength : 15},
				contact1: {stringMaxLength : 10},
				phone1: {stringMaxLength : 12},
				ext1: {stringMaxLength : 6},
				mobile1: {stringMaxLength : 15},
				memo: {stringMaxLength : 200}
			}
		});
		//查詢相關設定
		draw_supply(information);
		//新增Dialog相關設定
		insert_dialog = $("#dialog-form-insert").dialog(
						{
							draggable : false,//防止拖曳
							resizable : false,//防止縮放
							autoOpen : false,
							show : {
								effect : "blind",
								duration : 300
							},
							hide : {
								effect : "fade",
								duration : 300
							},
							width : 950,
							modal : true,
							buttons : [{
										id : "insert",
										text : "新增",
										click : function() {
											if ($('#insert-dialog-form-post').valid()) {
												information= {
													action : "insert",
													supply_name : $("#dialog-form-insert input[name='supply_name']").val(),
													supply_unicode : $("#dialog-form-insert input[name='supply_unicode']").val(),
													address : $("#dialog-form-insert input[name='address']").val(),
													contact : $("#dialog-form-insert input[name='contact']").val(),
													phone : $("#dialog-form-insert input[name='phone']").val(),
													ext : $("#dialog-form-insert input[name='ext']").val(),
													mobile : $("#dialog-form-insert input[name='mobile']").val(),
													contact1 : $("#dialog-form-insert input[name='contact1']").val(),
													phone1 : $("#dialog-form-insert input[name='phone1']").val(),
													ext1 : $("#dialog-form-insert input[name='ext1']").val(),
													email : $("#dialog-form-insert input[name='email']").val(),
													email1 : $("#dialog-form-insert input[name='email1']").val(),
													mobile1 : $("#dialog-form-insert input[name='mobile1']").val(),
													memo : $("#dialog-form-insert textarea[name='memo']").val(),
												};
												draw_supply(information);
												insert_dialog.dialog("close");
												$("#insert-dialog-form-post").trigger("reset");
											}
										}	
									}, {
										text : "取消",
										click : function() {
											validator_insert.resetForm();
											insert_dialog.dialog("close");
											$("#insert-dialog-form-post").trigger("reset");
										}
									}],
							close : function() {
								validator_insert.resetForm();
								$("#insert-dialog-form-post").trigger("reset");
							}
						});
		$("#dialog-form-insert").show();
		var uuid = "";
		//確認Dialog相關設定(刪除功能)
		confirm_dialog = $("#dialog-confirm").dialog({
			draggable : false,//防止拖曳
			resizable : false,//防止縮放
			autoOpen : false,
			height : 140,
			modal : true,
			show : {effect : "blind",duration : 300},
			hide : {effect : "fade",duration : 300},
			buttons : {
				"確認刪除" : function() {
					information={
							action : "delete",
							supply_id : uuid
						};
					draw_supply(information);
					$(this).dialog("close");
				},
				"取消刪除" : function() {
					$(this).dialog("close");
				}
			}
		});
		$("#dialog-confirm").show();
		var supply_name = "";
		//修改Dialog相關設定
			update_dialog = $("#dialog-form-update").dialog({
			draggable : false,//防止拖曳
			resizable : false,//防止縮放
			autoOpen : false,
			width : 950,
			modal : true,
			show : {effect : "blind",duration : 300},
			hide : {effect : "fade",duration : 300},
			buttons : [{
				text : "修改",
				click : function() {
					if ($('#update-dialog-form-post').valid()) {
						information={
	 							action : "update",
// 	 							unit_id : uuid,
	 							supply_id : $("#dialog-form-update input[name='supply_id']").val(),
	 							supply_name : $("#dialog-form-update input[name='supply_name']").val(),
								supply_unicode : $("#dialog-form-update input[name='supply_unicode']").val(),
								address : $("#dialog-form-update input[name='address']").val(),
								contact : $("#dialog-form-update input[name='contact']").val(),
								phone : $("#dialog-form-update input[name='phone']").val(),
								ext : $("#dialog-form-update input[name='ext']").val(),
								mobile : $("#dialog-form-update input[name='mobile']").val(),
								contact1 : $("#dialog-form-update input[name='contact1']").val(),
								phone1 : $("#dialog-form-update input[name='phone1']").val(),
								ext1 : $("#dialog-form-update input[name='ext1']").val(),
								mobile1 : $("#dialog-form-update input[name='mobile1']").val(),
								email : $("#dialog-form-update input[name='email']").val(),
								email1 : $("#dialog-form-update input[name='email1']").val(),
								memo : $("#dialog-form-update textarea[name='memo']").val(),
							};
						draw_supply(information);
						update_dialog.dialog("close");
						$("#update-dialog-form-post").trigger("reset");
					}
				}
			}, {
				text : "取消",
				click : function() {
					validator_update.resetForm();
					update_dialog.dialog("close");
					$("#update-dialog-form-post").trigger("reset");
				}
			} ],
			close : function() {
				validator_update.resetForm();
				$("#update-dialog-form-post").trigger("reset");
			}
		});
		$("#dialog-form-update").show();
		//刪除事件聆聽 : 因為聆聽事件動態產生，所以採用delegate來批量處理，節省資源
		$("#products2").delegate(".btn_delete", "click", function() {
			uuid = $(this).val();
			supply_name = $(this).attr("id");
			confirm_dialog.dialog("open");
		});
		//修改事件聆聽
		$("#products2").delegate(".btn_update", "click", function(e) {
			e.preventDefault();
			uuid = $(this).val();
			$("input[name='search_supply_name'").val("");
			$.ajax({
				type : "POST",
				url : "supply.do",
				data : {
					action : "search",
					supply_name : $("input[name='search_supply_name'").val()
				},
				success : function(result) {
						var json_obj = $.parseJSON(result);
						var len=json_obj.length;
						
						//判斷查詢結果
						var resultRunTime = 0;
						$.each (json_obj, function (i) {
							resultRunTime+=1;
						});
							var result_table = "";
							$.each(json_obj,function(i, item) {
								if(json_obj[i].supply_id==uuid){
										$("#dialog-form-update input[name='supply_id']").val(json_obj[i].supply_id);
										$("#dialog-form-update input[name='supply_name']").val(json_obj[i].supply_name);
										$("#dialog-form-update input[name='supply_unicode']").val(json_obj[i].supply_unicode);
										$("#dialog-form-update input[name='address']").val(json_obj[i].address);
										$("#dialog-form-update input[name='contact']").val(json_obj[i].contact);
										$("#dialog-form-update input[name='phone']").val(json_obj[i].phone);
										$("#dialog-form-update input[name='ext']").val(json_obj[i].ext);
										$("#dialog-form-update input[name='mobile']").val(json_obj[i].mobile);
										$("#dialog-form-update input[name='contact1']").val(json_obj[i].contact1);
										$("#dialog-form-update input[name='phone1']").val(json_obj[i].phone1);
										$("#dialog-form-update input[name='ext1']").val(json_obj[i].ext1);
										$("#dialog-form-update input[name='mobile1']").val(json_obj[i].mobile1);
										$("#dialog-form-update input[name='email']").val(json_obj[i].email);
										$("#dialog-form-update input[name='email1']").val(json_obj[i].email1);
										$("#dialog-form-update textarea[name='memo']").val(json_obj[i].memo);
									}
								
							});
						} 

				});			
			update_dialog.dialog("open");
		});		
		//新增事件聆聽
		$("#create-productunit").click( function() {
			insert_dialog.dialog("open");
		});
	});
</script>
		<div class="datalistWrap">
			<!--對話窗樣式-確認 -->
			<div id="dialog-confirm" title="確認刪除資料嗎?" style="display:none;">
				<p>是否確認刪除該筆資料</p>
			</div>
			<!--對話窗樣式-修改 -->
			<div id="dialog-form-update" title="修改資料" style="display:none;">
				<form name="update-dialog-form-post" id="update-dialog-form-post">
					<fieldset>
					<table border="0" height="500">
							<tbody>
							<tr>
							    <td>廠商名稱:</td><td><input type="text" name="supply_name"  placeholder="輸入廠商名稱"/>*</td>	
							</tr>
							<tr>
							    <td>廠商統一編號:</td><td><input type="text" name="supply_unicode" placeholder="輸入廠商統編"/></td>
							    <td>廠商地址:</td><td><input type="text" name="address" placeholder="輸入廠商地址"/></td>
							</tr>
							<tr>
								<td>連絡人:</td><td><input type="text" name="contact" placeholder="輸入連絡人"/></td>
							    <td>連絡人手機:</td><td><input type="text" name="mobile"placeholder="輸入連絡人手機"/></td>
							</tr>
							<tr>
								<td>連絡人電話:</td><td><input type="text" name="phone" placeholder="輸入連絡人電話"/></td>
							    <td>連絡人分機:</td><td><input type="text" name="ext" placeholder="輸入連絡人分機"/></td>
							</tr>
							<tr><td>連絡人email:</td><td><input type="text" name="email" placeholder="輸入連絡人email"/></td></tr>
							<tr>
							    <td>第二連絡人:</td><td><input type="text" name="contact1" placeholder="輸入第二連絡人"/></td>
								 <td>第二連絡人手機:</td><td><input type="text" name="mobile1"placeholder="輸入第二連絡人手機"/></td>		  
							</tr>
							<tr>
							  <td>第二連絡人電話:</td><td><input type="text" name="phone1"placeholder="輸入第二連絡人電話"/></td>
							  <td>第二連絡人分機:</td><td><input type="text" name="ext1"placeholder="輸入第二連絡人電話分機"/></td>
							</tr>
							<tr><td>第二連絡人email:</td><td><input type="text" name="email1" placeholder="輸入第二連絡人email"/></td></tr>
							<tr>
								<td valign="top">備註說明:</td>
							</tr>
							<tr>
								<td colspan="4"><textarea rows="3" cols="74" name="memo" placeholder="輸入備註說明 "/></textarea></td>
							</tr>
							<tr>
								<td><input type="hidden" name="supply_id" disabled="disabled"/></td>
							</tr>
							</tbody>
							</table>	
					</fieldset>
				</form>
			</div>			
			<!--對話窗樣式-新增 -->
			<div id="dialog-form-insert" title="新增廠商資料" style="display:none;">
				<form name="insert-dialog-form-post" id="insert-dialog-form-post"style="display:inline">
					<fieldset>
							<table border="0" height="500">
							<tbody>
							<tr>
							    <td>廠商名稱:</td><td><input type="text" name="supply_name"  placeholder="輸入廠商名稱"/>*</td>	
							</tr>
							<tr>
							    <td>廠商統一編號:</td><td><input type="text" name="supply_unicode" placeholder="輸入廠商統編"/></td>
							    <td>廠商地址:</td><td><input type="text" name="address" placeholder="輸入廠商地址"/></td>
							</tr>
							<tr>
								<td>連絡人:</td><td><input type="text" name="contact" placeholder="輸入連絡人"/></td>
							    <td>連絡人手機:</td><td><input type="text" name="mobile"placeholder="輸入連絡人手機"/></td>
							</tr>
							<tr>
								<td>連絡人電話:</td><td><input type="text" name="phone" placeholder="輸入連絡人電話"/></td>
							    <td>連絡人分機:</td><td><input type="text" name="ext" placeholder="輸入連絡人分機"/></td>
							</tr>
							<tr><td>連絡人email:</td><td><input type="text" name="email" placeholder="輸入連絡人email"/></td></tr>
							<tr>
							    <td>第二連絡人:</td><td><input type="text" name="contact1" placeholder="輸入第二連絡人"/></td>
								 <td>第二連絡人手機:</td><td><input type="text" name="mobile1"placeholder="輸入第二連絡人手機"/></td>		  
							</tr>
							<tr>
							  <td>第二連絡人電話:</td><td><input type="text" name="phone1"placeholder="輸入第二連絡人電話"/></td>
							  <td>第二連絡人分機:</td><td><input type="text" name="ext1"placeholder="輸入第二連絡人電話分機"/></td>
							</tr>
							<tr><td>第二連絡人email:</td><td><input type="text" name="email1" placeholder="輸入第二連絡人email"/></td></tr>
							<tr>
								<td valign="top">備註說明:</td>
							</tr>
							<tr>
								<td colspan="4"><textarea rows="3" cols="74" name="memo" placeholder="輸入備註說明 "/></textarea></td>
							</tr>
							<tr>
								<td><input type="hidden" name="supply_id" disabled="disabled"/></td>
							</tr>
							</tbody>
							</table>	
					</fieldset>
				</form>
			</div>
			<!-- 第一列 -->
			<div class="input-field-wrap">
				<div class="form-wrap">
					<div class="btn-row">
						<button class="btn btn-exec btn-wide" id="create-productunit">新增供應商</button>
					</div>
				</div><!-- /.form-wrap -->
			</div>
			<!-- 第二列 -->
		
			<div class="row search-result-wrap" >
				<div id="products2-contain" class="ui-widget" style="display:none;">
					<table id="products2" class="result-table">
						<thead>
							<tr class="ui-widget-header">
								<th>廠商名稱</th>
								<th>廠商統編</th>
								<th style="min-width:120px">廠商地址</th>
								<th>連絡人</th>
								<th>連絡人電話</th>
								<th>連絡人分機</th>
								<th>連絡人手機</th>
								<th>連絡人email</th>
								<th>第二連絡人</th>
								<th>第二連絡人電話</th>
								<th>第二連絡人分機</th>
								<th>第二連絡人手機</th>
								<th>第二連絡人email</th>
								<th>備註說明 </th>
								<th>功能</th>
							</tr>
						</thead>
						<tbody>
						</tbody>
					</table>
				</div>
				<span class="validateTips"> </span>
			</div>
		</div>
	</div>
</body>
</html>