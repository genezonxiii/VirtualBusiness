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
<title>商品類別</title>
<meta charset="utf-8">
<link rel="Shortcut Icon" type="image/x-icon" href="./images/Rockettheme-Ecommerce-Shop.ico" />
<link rel="stylesheet" href="css/styles.css" />
<link href="<c:url value="css/css.css" />" rel="stylesheet">
<link href="<c:url value="css/jquery.dataTables.min.css" />" rel="stylesheet">
<link href="<c:url value="css/1.11.4/jquery-ui.css" />" rel="stylesheet">
</head>
<body>
	<jsp:include page="template.jsp" flush="true"/>
	<div class="content-wrap" >
		<div class='bdyplane' style="opacity:0">
<script type="text/javascript" src="js/jquery-1.10.2.js"></script>
<script type="text/javascript" src="js/jquery.dataTables.min.js"></script>
<script type="text/javascript" src="js/jquery-ui.min.js"></script>
<script type="text/javascript" src="js/jquery-migrate-1.4.1.min.js"></script>
<script type="text/javascript" src="js/jquery.validate.min.js"></script>
<script type="text/javascript" src="js/additional-methods.min.js"></script>
<script type="text/javascript" src="js/messages_zh_TW.min.js"></script>
<script>

function draw_producttype(parameter){
	$.ajax({
		type : "POST",
		url : "producttype.do",
		data : parameter,
		success : function(result) {
				var json_obj = $.parseJSON(result);
// 				json_obj=[];
				var result_table = "";
				$.each(json_obj,function(i, item) {
					var text = "";
					if(json_obj[i].group_id!="common"){
						text+="<div class='table-row-func btn-in-table btn-gray'><i class='fa fa-ellipsis-h'></i>"
							+ "	<div class='table-function-list'>"
							+ "		<button class='btn-in-table btn-darkblue btn_update' title='修改' value='"+ json_obj[i].type_id+ "'name='"+ json_obj[i].type_name+"' ><i class='fa fa-pencil'></i></button>"
							+ "		<button class='btn-in-table btn-alert btn_delete' title='刪除' value='"+ json_obj[i].type_id+"' val2='"+json_obj[i].type_name+"'><i class='fa fa-trash'></i></button>"
							+ "	</div></div>";
					}
					if(json_obj[i].group_id=="common"){
						text="";
					}
					result_table += "<tr><td>"
						+ json_obj[i].type_name
						+ "</td><td>"
						+ text
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
// 						dom: 'lfr<t>ip',
// 						"bRetrieve": true,
						autoWidth: false,
// 						"order": [],
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
		//使用jquery.validate來做驗證  注意事項: 1.不能用選擇器方式批次設定，它只取最後一個參數 2.要調用resetForm()而不是reset()
		var validator_insert = $("#insert-dialog-form-post").validate({
			rules : {
				type_name : {required : true,maxlength : 10}
			},
			messages : {
				type_name : {maxlength : "長度不能超過10個字"}
			}
		});
		var validator_update = $("#update-dialog-form-post").validate({
			rules : {
				type_name : {required : true,maxlength : 10}
			},
			messages : {
				type_name : {maxlength : "長度不能超過10個字"}
			}
		});
	
		var type_name = $("#type_name");
		//查詢相關設定
		$("#searh-producttype").click(function(e) {
			e.preventDefault();
			var tmp={
					action : "searh",
					type_name : $("input[name='searh_type_name'").val()
				};
			draw_producttype(tmp);
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
									type_name : $("#dialog-form-insert input[name='type_name']").val()
								};
								draw_producttype(tmp);
								insert_dialog.dialog("close");
								$("#dialog-form-insert input[name='type_name']").val('');
							}
						}
					}, {
						text : "取消",
						click : function() {
							validator_insert.resetForm();
							insert_dialog.dialog("close");
							$("#dialog-form-insert input[name='type_name']").val('');
						}
					} ],
			close : function() {
				validator_insert.resetForm();
				$("#dialog-form-insert input[name='type_name']").val('');
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
							type_id : $(this).val()
						};
					draw_producttype(tmp);
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
	 							type_id : $(this).val(),
	 							type_name : $("#dialog-form-update input[name='type_name']").val()
							};
						draw_producttype(tmp);
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
// 			$("#dialog-confirm").html("<div style='width:180px' align='center'><br>'<font color='red'><b>"+$(this).attr("val2")+"</b></font>'<br><br></div>");
			confirm_dialog.dialog("open");
		});
		//修改事件聆聽
		$("#products2").delegate(".btn_update", "click", function() {
			$("#dialog-form-update").val($(this).val());
			$("#update-dialog-form-post input[name='type_name']").val($(this).attr("name"));
			update_dialog.dialog("open");
		});
		//新增事件聆聽
		$("#create-producttype").click( function() {
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
			<div id="dialog-confirm" title="是否刪除此類型?" style="display:none;">
			</div>
			<!--對話窗樣式-修改 -->
			<div id="dialog-form-update" title="修改產品類別" style="display:none;">
				<form name="update-dialog-form-post" id="update-dialog-form-post">
					<fieldset>
						<table class="form-table">
							<tr>
								<td>修改產品類別：</td>
								<td><input type="text" name="type_name" placeholder="修改商品類別名稱"></td>
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
								<td>產品類別：</td>
								<td><input type="text" name="type_name"  placeholder="輸入商品類別名稱"></td>
							</tr>
						</table>
					</fieldset>
				</form>
			</div>
			<!-- 第一列 -->
		<div class="input-field-wrap">
			<div class="form-wrap">
				<div class="form-row">
					<label for="">
						<span class="block-label">商品類別名稱查詢</span>
						<input type="text" name="searh_type_name">
					</label>
					<button class="btn btn-darkblue" id="searh-producttype">查詢</button>
				</div>
				<div class="btn-row">
					<button class="btn btn-exec btn-wide" id="create-producttype">新增商品類別</button>
				</div>
			</div><!-- /.form-wrap -->
		</div>
			<div class="row search-result-wrap" align="center" id="products2_contain_row" style="width:600px;margin:0px auto;display:none;">
				<div id="products2-contain" class="ui-widget">
					<table id="products2" class="result-table">
						<thead>
							<tr class="ui-widget-header">
								<th>產品類別</th>
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
</body>
</html>