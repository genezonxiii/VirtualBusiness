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
<title>供應商資料</title>
<meta charset="utf-8">
<link rel="Shortcut Icon" type="image/x-icon" href="./images/Rockettheme-Ecommerce-Shop.ico" />
<link rel="stylesheet" href="css/styles.css" />
<link href="<c:url value="css/css.css" />" rel="stylesheet">
<link href="<c:url value="css/jquery.dataTables.min.css" />" rel="stylesheet">
<link href="<c:url value="css/1.11.4/jquery-ui.css" />" rel="stylesheet">
<style>
table.form-table {
 	border-spacing: 10px 8px !important; 
}
</style>
</head>
<body>
	<jsp:include page="template.jsp" flush="true"/>
	<div class="content-wrap" >
		<div class='bdyplane' style="opacity:0">
		
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
	$("#products2-contain").css({"opacity":"0"});
// 	warning_msg("---讀取中請稍候---");
	$.ajax({
		type : "POST",
		url : "supply.do",
		data : info,
		success : function(result) {
			var json_obj = $.parseJSON(result);
			var result_table = "";
			$.each(json_obj,function(i, item) {
				result_table += 
					"<tr><td name='name'>"+ json_obj[i].supply_name+
					"</td><td name='uni'>"+json_obj[i].supply_unicode+ 
					"</td><td name='addr'>"+json_obj[i].address+
					"</td><td name='contact0'>"+
						((json_obj[i].contact.length<1)?"":("&nbsp;&nbsp;姓名："+json_obj[i].contact+"<br>"))+
						((json_obj[i].mobile.length<1)?"":("&nbsp;&nbsp;手機："+json_obj[i].mobile+"<br>"))+
						((json_obj[i].phone.length<1)?"":("&nbsp;&nbsp;電話："+json_obj[i].phone+"<br>"))+
						((json_obj[i].ext.length<1)?"":("&nbsp;&nbsp;分機："+json_obj[i].ext+"<br>"))+
						((json_obj[i].email.length<1)?"":("email："+json_obj[i].email+"<br>"))+
					"</td><td name='contact1'>"+
						((json_obj[i].contact1.length<1)?"":("&nbsp;&nbsp;姓名："+json_obj[i].contact1+"<br>"))+
						((json_obj[i].mobile1.length<1)?"":("&nbsp;&nbsp;手機："+json_obj[i].mobile1+"<br>"))+
						((json_obj[i].phone1.length<1)?"":("&nbsp;&nbsp;電話："+json_obj[i].phone1+"<br>"))+
						((json_obj[i].ext1.length<1)?"":("&nbsp;&nbsp;分機："+json_obj[i].ext1+"<br>"))+
						((json_obj[i].email1.length<1)?"":("email："+json_obj[i].email1+"<br>"))+
					"</td><td name='memo'>"+json_obj[i].memo+
					 "<td><div class='table-row-func btn-in-table btn-gray'><i class='fa fa-ellipsis-h'></i>"
					+ "	<div class='table-function-list'>"
					+ "		<button class='btn-in-table btn-darkblue btn_update' title='修改' value='"+ json_obj[i].supply_id+ "'name='"+ json_obj[i].supply_name+"' ><i class='fa fa-pencil'></i></button>"
					+ "		<button class='btn-in-table btn-alert btn_delete' title='刪除' id='"+json_obj[i].supply_name+"' value='"+ json_obj[i].supply_id+"' val2='"+json_obj[i].supply_name+"' ><i class='fa fa-trash'></i></button>"
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
// 				$("#products2").find("td").css("text-align", "center");
				$("#products2-contain").animate({"opacity":"0.01"},1);
				$("#products2-contain").animate({"opacity":"1"},300);
				warning_msg("");
			}else{
				$("#products2-contain").hide();
				warning_msg("---查無供應商---");
			}
		}
	});
}
	$(function() {
		$(".bdyplane").animate({"opacity":"1"});
		var information={
			action : "search",
		};
		draw_supply(information);
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
		
		//新增Dialog相關設定
		insert_dialog = $("#dialog-form-insert").dialog({
			draggable : false, resizable : false, autoOpen : false,
			height : "auto", width : "auto", modal : true,
			show : {effect : "blind",duration : 300},
			hide : {effect : "fade",duration : 300},
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
		//確認Dialog相關設定(刪除功能)
		confirm_dialog = $("#dialog-confirm").dialog({
			draggable : false, resizable : false, autoOpen : false,
			height : "auto", width : "auto", modal : true,
			show : {effect : "blind",duration : 300},
			hide : {effect : "fade",duration : 300},
			buttons : {
				"確認刪除" : function() {
					information={
							action : "delete",
							supply_id : $(this).val()
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
			update_dialog = $("#dialog-form-update").dialog({
			draggable : false, resizable : false, autoOpen : false,
			height : "auto", width : "auto", modal : true,
			show : {effect : "blind",duration : 300},
			hide : {effect : "fade",duration : 300},
			buttons : [{
				text : "修改",
				click : function() {
					if ($('#update-dialog-form-post').valid()) {
						information={
	 							action : "update",
	 							supply_id : $(this).val(),
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
			$("#dialog-confirm").html("<div class='delete_msg'>'"+$(this).attr("val2")+"'</div>");
			$("#dialog-confirm").val($(this).val());
			confirm_dialog.dialog("open");
		});
		//修改事件聆聽
		$("#products2").delegate(".btn_update", "click", function(e) {
			e.preventDefault();
			$("#dialog-form-update").val($(this).val());
			$.ajax({
				type : "POST",
				url : "supply.do",
				data : { action: "search"},
				success : function(result) {
					var json_obj = $.parseJSON(result);
					var result_table = "";
					$.each(json_obj,function(i, item) {
						if(json_obj[i].supply_id==$("#dialog-form-update").val()){
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
// 			$("#dialog-form-update").val($(this).val());
// 			$("#dialog-form-update input[name='supply_name']").val($(this).parents("tr").find("td[name='name']").html());
// 			$("#dialog-form-update input[name='supply_unicode']").val($(this).parents("tr").find("td[name='uni']").html());
// 			$("#dialog-form-update input[name='address']").val($(this).parents("tr").find("td[name='addr']").html());
// 			$("#dialog-form-update input[name='contact']").val($(this).parents("tr").find("td[name='contact0']").html());
// 			$("#dialog-form-update input[name='phone']").val($(this).parents("tr").find("td[name='phone0']").html());
// 			$("#dialog-form-update input[name='ext']").val($(this).parents("tr").find("td[name='ext0']").html());
// 			$("#dialog-form-update input[name='mobile']").val($(this).parents("tr").find("td[name='mobile0']").html());
// 			$("#dialog-form-update input[name='contact1']").val($(this).parents("tr").find("td[name='contact1']").html());
// 			$("#dialog-form-update input[name='phone1']").val($(this).parents("tr").find("td[name='phone1']").html());
// 			$("#dialog-form-update input[name='ext1']").val($(this).parents("tr").find("td[name='ext1']").html());
// 			$("#dialog-form-update input[name='mobile1']").val($(this).parents("tr").find("td[name='mobile1']").html());
// 			$("#dialog-form-update input[name='email']").val($(this).parents("tr").find("td[name='email0']").html());
// 			$("#dialog-form-update input[name='email1']").val($(this).parents("tr").find("td[name='email1']").html());
// 			$("#dialog-form-update textarea[name='memo']").val($(this).parents("tr").find("td[name='memo']").html());
			update_dialog.dialog("open");
		});		
		//新增事件聆聽
		$("#create-productunit").click( function() {
			insert_dialog.dialog("open");
		});
		$(".input-field-wrap").append("<div class='div_right_bottom upup'><img src='./images/upup.png'></div>");
		$(".input-field-wrap").after("<div class='div_right_top downdown' style='display:none;'><img src='./images/downdown.png'></div>");
		$(".upup").click(function(){
			$(".input-field-wrap").slideToggle("slow");
			$(".downdown").slideToggle();
		});
		$(".downdown").click(function(){
			$(".input-field-wrap").slideToggle("slow");
			$(".downdown").slideToggle();
		});
	});
</script>
		<div class="datalistWrap">
			<!--對話窗樣式-確認 -->
			<div id="dialog-confirm" title="是否刪除此供應商?" style="display:none;"></div>
			<!--對話窗樣式-修改 -->
			<div id="dialog-form-update" title="修改資料" style="display:none;">
				<form name="update-dialog-form-post" id="update-dialog-form-post">
					<fieldset>
					<table class="form-table">
						<tbody>
							<tr>
							    <td>供應商名稱：</td><td><input type="text" name="supply_name"  placeholder="輸入供應商名稱"/>*</td>	
							</tr>
							<tr>
							    <td>供應商統一編號：</td><td><input type="text" name="supply_unicode" placeholder="輸入供應商統編"/></td>
							    <td>供應商地址：</td><td><input type="text" name="address" placeholder="輸入供應商地址"/></td>
							</tr>
							<tr>
								<td>連絡人：</td><td><input type="text" name="contact" placeholder="輸入連絡人"/></td>
							    <td>連絡人手機：</td><td><input type="text" name="mobile"placeholder="輸入連絡人手機"/></td>
							</tr>
							<tr>
								<td>連絡人電話：</td><td><input type="text" name="phone" placeholder="輸入連絡人電話"/></td>
							    <td>連絡人分機：</td><td><input type="text" name="ext" placeholder="輸入連絡人分機"/></td>
							</tr>
							<tr><td>連絡人email：</td><td><input type="text" name="email" placeholder="輸入連絡人email"/></td></tr>
							<tr>
							    <td>第二連絡人：</td><td><input type="text" name="contact1" placeholder="輸入第二連絡人"/></td>
								<td>第二連絡人手機：</td><td><input type="text" name="mobile1"placeholder="輸入第二連絡人手機"/></td>		  
							</tr>
							<tr>
							  <td>第二連絡人電話：</td><td><input type="text" name="phone1"placeholder="輸入第二連絡人電話"/></td>
							  <td>第二連絡人分機：</td><td><input type="text" name="ext1"placeholder="輸入第二連絡人分機"/></td>
							</tr>
							<tr><td>第二連絡人email：</td><td><input type="text" name="email1" placeholder="輸入第二連絡人email"/></td></tr>
							<tr>
								<td valign="top">備註說明：</td>
								<td colspan="3"><textarea rows="2" style="width:98%" name="memo" placeholder="輸入備註說明 "/></textarea></td>
							</tr>
<!-- 							<tr> -->
<!-- 								<td><input type="hidden" name="supply_id" disabled="disabled"/></td> -->
<!-- 							</tr> -->
							</tbody>
							</table>	
					</fieldset>
				</form>
			</div>			
			<!--對話窗樣式-新增 -->
			<div id="dialog-form-insert" title="新增供應商資料" style="display:none;">
				<form name="insert-dialog-form-post" id="insert-dialog-form-post"style="display:inline">
					<fieldset>
							<table class="form-table">
							<tbody>
							<tr>
							    <td>供應商名稱：</td><td><input type="text" name="supply_name"  placeholder="輸入供應商名稱"/>*</td>	
							</tr>
							<tr>
							    <td>供應商統一編號：</td><td><input type="text" name="supply_unicode" placeholder="輸入供應商統編"/></td>
							    <td>供應商地址：</td><td><input type="text" name="address" placeholder="輸入供應商地址"/></td>
							</tr>
							<tr>
								<td>連絡人：</td><td><input type="text" name="contact" placeholder="輸入連絡人"/></td>
							    <td>連絡人手機：</td><td><input type="text" name="mobile"placeholder="輸入連絡人手機"/></td>
							</tr>
							<tr>
								<td>連絡人電話：</td><td><input type="text" name="phone" placeholder="輸入連絡人電話"/></td>
							    <td>連絡人分機：</td><td><input type="text" name="ext" placeholder="輸入連絡人分機"/></td>
							</tr>
							<tr><td>連絡人email：</td><td><input type="text" name="email" placeholder="輸入連絡人email"/></td></tr>
							<tr>
							    <td>第二連絡人：</td><td><input type="text" name="contact1" placeholder="輸入第二連絡人"/></td>
								 <td>第二連絡人手機：</td><td><input type="text" name="mobile1"placeholder="輸入第二連絡人手機"/></td>		  
							</tr>
							<tr>
							  <td>第二連絡人電話：</td><td><input type="text" name="phone1"placeholder="輸入第二連絡人電話"/></td>
							  <td>第二連絡人分機：</td><td><input type="text" name="ext1"placeholder="輸入第二連絡人分機"/></td>
							</tr>
							<tr><td>第二連絡人email：</td><td><input type="text" name="email1" placeholder="輸入第二連絡人email"/></td></tr>
							<tr>
								<td valign="top">備註說明：</td>
								<td colspan="3"><textarea rows="2" style="width:98%" name="memo" placeholder="輸入備註說明 "/></textarea></td>
							</tr>
<!-- 							<tr> -->
<!-- 								<td><input type="hidden" name="supply_id" disabled="disabled"/></td> -->
<!-- 							</tr> -->
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
								<th style="min-width:60px">供應商名稱</th>
								<th style="min-width:60px">供應商統編</th>
								<th style="min-width:100px">供應商地址</th>
								<th style="min-width:40px">第一連絡人</th>
<!-- 								<th>連絡人手機</th> -->
<!-- 								<th>連絡人電話</th> -->
<!-- 								<th>連絡人分機</th> -->
<!-- 								<th>連絡人email</th> -->
								<th>第二連絡人</th>
<!-- 								<th>第二連絡人手機</th> -->
<!-- 								<th>第二連絡人電話</th> -->
<!-- 								<th>第二連絡人分機</th> -->
<!-- 								<th>第二連絡人email</th> -->
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
	</div>
</body>
</html>