<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ page import="tw.com.aber.producttype.controller.*"%>
<%@ page import="java.util.*"%>
<%@ page import="java.sql.DriverManager"%>
<%@ page import="java.sql.Connection"%>
<%@ page import="java.sql.Statement"%>
<%@ page import="java.sql.ResultSet"%>
<jsp:directive.page import="java.sql.SQLException" />
<!DOCTYPE html>
<html>
<head>
<title>發票字軌管理</title>
<meta charset="utf-8">
<link rel="Shortcut Icon" type="image/x-icon" href="./images/Rockettheme-Ecommerce-Shop.ico" />
<link href="<c:url value="css/jquery.dataTables.min.css" />" rel="stylesheet">
<link href="<c:url value="css/1.11.4/jquery-ui.css" />" rel="stylesheet">
</head>
<body>
	<jsp:include page="template.jsp" flush="true"/>
	<div class="content-wrap" >
		<div class='bdyplane' style="opacity:0">

<script>

function draw_exchange(parameter){
	$.ajax({
		type : "POST",
		url : "invoicetrack.do",
		data : parameter,
		success : function(result) {
				var json_obj = $.parseJSON(result);
				var result_table = "";
				$.each(json_obj,function(i, item) {
					result_table 
					+="<tr><td name='invoice_type'>" + json_obj[i].invoice_type
					+ "</td><td name='year_month'>"+ json_obj[i].year_month
					+ "</td><td name='invoice_track'>"+ json_obj[i].invoice_track
					+ "</td><td name='invoice_beginno'>"+ json_obj[i].invoice_beginno
					+ "</td><td name='invoice_endno'>"+ json_obj[i].invoice_endno
					+ "</td><td name='seq'>"+ json_obj[i].seq
					+ "</td><td>"
					+ "<div class='table-row-func btn-in-table btn-gray'><i class='fa fa-ellipsis-h'></i>"
					+ "	<div class='table-function-list'>"
					+ "		<button class='btn-in-table btn-darkblue btn_update' title='修改' value='"+ json_obj[i].invoice_id+ "'><i class='fa fa-pencil'></i></button>"
					+ "		<button class='btn-in-table btn-alert btn_delete' title='刪除' value='"+ json_obj[i].invoice_id+"' val2='"+json_obj[i].year_month+"'><i class='fa fa-trash'></i></button>"
					+ "	</div></div>"
					+ "</td></tr>";
				});
				//判斷查詢結果
				var resultRunTime = 0;
				$.each (json_obj, function (i) {
					resultRunTime+=1;
				});
				$("#products2").dataTable().fnDestroy();
				if(resultRunTime!=0){
					$("#products2_contain_row").show();
					$("#products2_contain_row").css({"opacity":"0"});
					$("#products2 tbody").html(result_table);
					$("#products2").dataTable({
						"language": {"url": "js/dataTables_zh-tw.txt","zeroRecords": "沒有符合的結果"}});
					$("#products2").find("td").css({"text-align":"center","height":"32px"});
					$("#products2_contain_row").animate({"opacity":"0.01"},1);
					$("#products2_contain_row").animate({"opacity":"1"},300);
					warning_msg("");
				}else{
					warning_msg("---查無此結果---");
					$("#products2_contain_row").hide();
				}
			}
		});
}
	$(function() {
		$(".bdyplane").animate({"opacity":"1"});
		draw_exchange({action : "search"});
		
		//使用jquery.validate來做驗證  注意事項: 1.不能用選擇器方式批次設定，它只取最後一個參數 2.要調用resetForm()而不是reset()
		var validator_insert = $("#insert-dialog-form-post").validate({
			rules : {
				invoice_type : {required : true,maxlength : 10,number :true },
				year_month : {required : true,maxlength : 5,number :true },
				invoice_track : {required : true,maxlength : 2, minlength : 2},
				invoice_beginno : {required : true,maxlength : 8, minlength : 8,number :true },
				invoice_endno : {required : true,maxlength : 8, minlength : 8,number :true },
				seq : {required : true,maxlength : 10,number :true }
			}
		});
		var validator_update = $("#update-dialog-form-post").validate({
			rules : {
				invoice_type : {required : true,maxlength : 10,number :true },
				year_month : {required : true,maxlength : 5,number :true },
				invoice_track : {required : true,maxlength : 2},
				invoice_beginno : {required : true,maxlength : 10,number :true },
				invoice_endno : {required : true,maxlength : 10,number :true },
				seq : {required : true,maxlength : 10,number :true }
			}
		});
	
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
								var tmp={
									action : "insert",
									invoice_type : $("#dialog-form-insert input[name='invoice_type']").val(),
									year_month : $("#dialog-form-insert input[name='year_month']").val(),
									invoice_track : $("#dialog-form-insert input[name='invoice_track']").val(),
									invoice_beginno : $("#dialog-form-insert input[name='invoice_beginno']").val(),
									invoice_endno : $("#dialog-form-insert input[name='invoice_endno']").val(),
									seq : $("#dialog-form-insert input[name='seq']").val()
								};
								draw_exchange(tmp);
								insert_dialog.dialog("close");
							}
						}
					}, {
						text : "取消",
						click : function() {
							validator_insert.resetForm();
							insert_dialog.dialog("close");
						}
					} ],
			close : function() {
				validator_insert.resetForm();
			}
		});
		$("#dialog-form-insert").show();
		var uuid = "";
		//確認Dialog相關設定(刪除功能)
		confirm_dialog = $("#dialog-confirm").dialog({
			draggable : false, resizable : false, autoOpen : false,
			height : "auto", width : "auto", modal : true,
			show : {effect : "blind",duration : 300},
			hide : {effect : "fade",duration : 300},
			buttons : {
				"確認刪除" : function() {
					var tmp={
							action : "delete",
							invoice_id : $(this).val()
						};
					draw_exchange(tmp);
					$(this).dialog("close");
				},
				"取消刪除" : function() {
					$(this).dialog("close");
				}
			}
		});
		$("#dialog-confirm").show();
		//修改Dialog相關設定
		update_dialog = $("#dialog-form-update").dialog({
			draggable : false, resizable : false, autoOpen : false,
			height : "auto", width : "auto", modal : true,
			show : {effect : "blind",duration : 300},
			hide : {effect : "fade",duration : 300},
			buttons : [{
				text : "修改",
				click : function() {
					if ($('#update-dialog-form-post').valid()) {
						var tmp={
	 							action : "update",
	 							invoice_id : $(this).val(),
	 							invoice_type : $("#dialog-form-update input[name='invoice_type']").val(),
	 							year_month : $("#dialog-form-update input[name='year_month']").val(),
	 							invoice_track : $("#dialog-form-update input[name='invoice_track']").val(),
	 							invoice_beginno : $("#dialog-form-update input[name='invoice_beginno']").val(),
	 							invoice_endno : $("#dialog-form-update input[name='invoice_endno']").val(),
	 							seq : $("#dialog-form-update input[name='seq']").val()
							};
						draw_exchange(tmp);
						update_dialog.dialog("close");
					}
				}
			}, {
				text : "取消",
				click : function() {
					validator_update.resetForm();
					update_dialog.dialog("close");
				}
			} ],
			close : function() {
				validator_update.resetForm();
			}
		});
		$("#dialog-form-update").show();
		//刪除事件聆聽 : 因為聆聽事件動態產生，所以採用delegate來批量處理，節省資源
		$("#products2").delegate(".btn_delete", "click", function() {
			$("#dialog-confirm").val($(this).val());
			$("#dialog-confirm").html("<div class='delete_msg'>'"+$(this).attr("val2")+"'</div>");
			confirm_dialog.dialog("open");
		});
		//修改事件聆聽
		$("#products2").delegate(".btn_update", "click", function() {
			$("#dialog-form-update").val($(this).val());
			$("#update-dialog-form-post input[name='invoice_type']").val($(this).parents("tr").find("td[name='invoice_type']").html());
			$("#update-dialog-form-post input[name='year_month']").val($(this).parents("tr").find("td[name='year_month']").html());
			$("#update-dialog-form-post input[name='invoice_track']").val($(this).parents("tr").find("td[name='invoice_track']").html());
			$("#update-dialog-form-post input[name='invoice_beginno']").val($(this).parents("tr").find("td[name='invoice_beginno']").html());
			$("#update-dialog-form-post input[name='invoice_endno']").val($(this).parents("tr").find("td[name='invoice_endno']").html());
			$("#update-dialog-form-post input[name='seq']").val($(this).parents("tr").find("td[name='seq']").html());
			update_dialog.dialog("open");
		});
		//新增事件聆聽
		$("#create-exchange").click( function() {
			$("#dialog-form-insert input[name='invoice_type']").val('');
			$("#dialog-form-insert input[name='year_month']").val('');
			$("#dialog-form-insert input[name='invoice_track']").val('');
			$("#dialog-form-insert input[name='invoice_beginno']").val('');
			$("#dialog-form-insert input[name='invoice_endno']").val('');
			$("#dialog-form-insert input[name='seq']").val('');
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
			<div id="dialog-confirm" title="是否刪除此時期之發票字軌?" style="display:none;">
			</div>
			<!--對話窗樣式-修改 -->
			<div id="dialog-form-update" title="修改幣值匯率" style="display:none;">
				<form name="update-dialog-form-post" id="update-dialog-form-post">
					<fieldset>
						<table class="form-table">
							<tr>
								<td>發票類型：</td>
								<td><input type="text" name="invoice_type" placeholder="修改類型"></td>
							</tr><tr>
								<td>發票年月：</td>
								<td><input type="text" name="year_month" placeholder="修改年月"></td>
								<td>發票字頭：</td>
								<td><input type="text" name="invoice_track" placeholder="修改字頭"></td>
							</tr><tr>
								<td>字軌開頭號：</td>
								<td><input type="text" name="invoice_beginno" placeholder="修改字軌開頭號"></td>
								<td>字軌結尾號：</td>
								<td><input type="text" name="invoice_endno" placeholder="修改字軌結尾號"></td>
							</tr><tr>
								<td>字軌捲數：</td>
								<td><input type="text" name="seq" placeholder="修改字軌捲數"></td>
							</tr>
						</table>
					</fieldset>
				</form>
			</div>			
			<!--對話窗樣式-新增 -->
			<div id="dialog-form-insert" title="新增產品類別" style="display:none;">
				<form name="insert-dialog-form-post" id="insert-dialog-form-post">
					<fieldset>
						<table class="form-table">
							<tr>
								<td>發票類型：</td>
								<td><input type="text" name="invoice_type" placeholder="修改類型"></td>
							</tr><tr>
								<td>發票年月：</td>
								<td><input type="text" name="year_month" placeholder="修改年月"></td>
								<td>發票字頭：</td>
								<td><input type="text" name="invoice_track" placeholder="修改字頭"></td>
							</tr><tr>
								<td>字軌開頭號：</td>
								<td><input type="text" name="invoice_beginno" placeholder="修改字軌開頭號"></td>
								<td>字軌結尾號：</td>
								<td><input type="text" name="invoice_endno" placeholder="修改字軌結尾號"></td>
							</tr><tr>
								<td>字軌捲數：</td>
								<td><input type="text" name="seq" placeholder="修改字軌捲數"></td>
							</tr>
						</table>
					</fieldset>
				</form>
			</div>
			<!-- 第一列 -->
		<div class="input-field-wrap">
			<div class="form-wrap">
				<div class="btn-row">
					<button class="btn btn-exec btn-wide" id="create-exchange">新增發票字軌</button>
				</div>
			</div><!-- /.form-wrap -->
		</div>
			<div class="row search-result-wrap" align="center" id="products2_contain_row" style="display:none;">
				<div id="products2-contain" class="ui-widget">
					<table id="products2" class="result-table">
						<thead>
							<tr>
								<th>類型</th>
								<th>年月</th>
								<th>字頭</th>
								<th>字軌開頭</th>
								<th>字軌結尾</th>
								<th>捲數</th>
								<th>功能</th>
							</tr>
						</thead>
						<tbody>
						</tbody>
					</table>
				</div>
			</div>
		</div>
	</div>
	</div>
<script type="text/javascript" src="js/jquery-1.10.2.js"></script>
<script type="text/javascript" src="js/jquery.dataTables.min.js"></script>
<script type="text/javascript" src="js/jquery-ui.min.js"></script>
<script type="text/javascript" src="js/jquery-migrate-1.4.1.min.js"></script>
<script type="text/javascript" src="js/jquery.validate.min.js"></script>
<script type="text/javascript" src="js/additional-methods.min.js"></script>
<script type="text/javascript" src="js/messages_zh_TW.min.js"></script>
</body>
</html>