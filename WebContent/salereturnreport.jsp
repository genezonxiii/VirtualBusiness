
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
<title>退貨報表</title>
<meta charset="utf-8">
<link rel="Shortcut Icon" type="image/x-icon" href="./images/Rockettheme-Ecommerce-Shop.ico" />
<link rel="stylesheet" href="css/styles.css" />
<link href="<c:url value="css/css.css" />" rel="stylesheet">
<link href="<c:url value="css/jquery.dataTables.min.css" />" rel="stylesheet">
<link href="<c:url value="css/1.11.4/jquery-ui.css" />" rel="stylesheet">
<link rel="stylesheet" href="css/buttons.dataTables.min.css" />
</head>
<body>
	<jsp:include page="template.jsp" flush="true"/>
	<div class="content-wrap" style="margin:56px 0px 28px 120px;">
<script type="text/javascript" src="js/jquery-1.12.4.js"></script>
<script type="text/javascript" src="js/jquery.dataTables.min.js"></script>
<script type="text/javascript" src="js/dataTables.buttons.min.js"></script>
<script type="text/javascript" src="js/buttons.flash.min.js"></script>
<script type="text/javascript" src="js/jszip.min.js"></script>
<script type="text/javascript" src="js/pdfmake.min.js"></script>
<script type="text/javascript" src="js/vfs_fonts.js"></script>
<script type="text/javascript" src="js/buttons.html5.min.js"></script>
<script type="text/javascript" src="js/buttons.print.min.js"></script>
<script type="text/javascript" src="js/jquery-ui.js"></script>
<script type="text/javascript" src="js/jquery-migrate-1.4.1.min.js"></script>
<script type="text/javascript" src="js/jquery.validate.min.js"></script>
<script type="text/javascript" src="js/additional-methods.min.js"></script>
<script type="text/javascript" src="js/messages_zh_TW.min.js"></script>
<script type="text/javascript" src="js/jquery.table2excel.js"></script>

<script type="text/javascript" src="js/dataTables.buttons.min.js"></script>
<script type="text/javascript" src="js/buttons.jqueryui.min.js"></script>
<script>
function date_format(str) {
	if(str==null){
		return "";
	}
	var words=str.replace(","," ").split(" ");
	return words[3]+"-"+words[0].replace("一月","1").replace("二月","2").replace("三月","3").replace("四月","4").replace("五月","5").replace("六月","6").replace("七月","7").replace("八月","8").replace("九月","9").replace("十月","10").replace("十一月","11").replace("十二月","12").replace("Jan","1").replace("Feb","2").replace("Mar","3").replace("Apr","4").replace("May","5").replace("Jun","6").replace("Jul","7").replace("Aug","8").replace("Sep","9").replace("Oct","10").replace("Nov","11").replace("Dec","12")+"-"+words[1];
}
	$(function() {
		table_before("products");
		$( "#datepicker1" ).datepicker({
			 dateFormat: 'yy/mm/dd'
		});
		$( "#datepicker2" ).datepicker({dateFormat: 'yy/mm/dd'});
		//使用jquery.validate來做驗證  注意事項: 1.不能用選擇器方式批次設定，它只取最後一個參數 2.要調用resetForm()而不是reset()
		var validator_insert = $("#insert-dialog-form-post").validate({
			rules : {unit_name : {required : true,maxlength : 10}},
			messages : {unit_name : {maxlength : "長度不能超過10個字"}}});
		var validator_update = $("#update-dialog-form-post").validate({
			rules : {unit_name : {required : true,maxlength : 10}},
			messages : {unit_name : {maxlength : "長度不能超過10個字"}}});
		var unit_name = $("#unit_name");
		//查詢相關設定
		$("#searh-productunit").click(function(e) {
							
							e.preventDefault();
							$.ajax({
									type : "POST",
									url : "salereturnreport.do",
									data : {
										action :"searh",
										time1 : $('#datepicker1').val(),
										time2 : $('#datepicker2').val()
									},
									success : function(result) {
											var json_obj = $.parseJSON(result);
											var result_table = "";
											$.each(json_obj,function(i, item) {
												result_table += "<tr><td>"
													+ date_format(json_obj[i].return_date)
													+ "</td><td style='min-width:80px;word-break: break-all;'>"
													+ json_obj[i].seq_no
													+ "</td><td style='min-width:80px;word-break: break-all;'>"
													+ json_obj[i].order_no
													+ "</td><td>"
													+ json_obj[i].product_name
													+ "</td><td>"
													+ json_obj[i].c_product_id
													+ "</td><td>"
													+ json_obj[i].quantity
													+ "</td><td>"
													+ json_obj[i].price
													+ "</td><td>"
													+ date_format(json_obj[i].trans_list_date)
													+ "</td><td>"
													+ date_format(json_obj[i].dis_date)
													+ "</td><td>"
													+ date_format(json_obj[i].sale_date)
													+ "</td><td>"
													+ json_obj[i].order_source
													+ "</td><td>"
													+ (json_obj[i].memo==null?"":json_obj[i].memo)
													+ "</td></tr>";
											});
											//判斷查詢結果
// 											$("#my123").html("<tr class='noExl'><td></td></tr><tr><td>退貨日期</td><td>銷貨單號</td><td>訂單號</td><td>產品名稱</td><td>客戶自訂產品ID</td><td>銷貨數量</td><td>貨金額</td><td>轉單日</td><td>配送日</td><td>銷貨/出貨日期</td><td>銷售平台</td><td>備註</td></tr>"+result_table);
 											var resultRunTime = 0;
 											$.each (json_obj, function (i) {
 												resultRunTime+=1;
 											});
 											$("#products").dataTable().fnDestroy();
 											if(resultRunTime!=0){
 												$("#products-contain").show();
 												$("#products tbody").html(result_table);
 												$("#products").dataTable({
 													dom: 'lfrB<t>ip',
 													buttons: [{
 														extend: 'excel',
 														text: '輸出為execl報表',
 														title: '退貨報表',
 														exportOptions: {modifier: {search: 'none'}}
 													  }],
 													"language": {"url": "js/dataTables_zh-tw.txt"}
 												});
//  												$("#products").find("td").css({"word-break":"break-all","min-width":"90px","text-align":"center" });
 												$(".validateTips").text("");
 											}else{
 												$("#products-contain").hide();
 												$(".validateTips").text("查無此結果");
 											}
										}
									});
							
						});
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
							height : 300,
							width : 350,
							modal : true,
							buttons : [{
										id : "insert",
										text : "新增",
										click : function() {
											if ($('#insert-dialog-form-post').valid()) {
												$.ajax({
													type : "POST",
													url : "salereport.do",
													data : {
														action : "insert",
														unit_name : $("#dialog-form-insert input[name='unit_name']").val()
													},
													success : function(result) {
														var json_obj = $.parseJSON(result);
														var result_table = "";
														$.each(json_obj,function(i,item) {
																result_table += "<tr><td>"
																	+ json_obj[i].unit_name
																	+ "</td><td>"
																	+ "<button value='"
																	+ json_obj[i].unit_id
																	+ "'name='"
																	+ json_obj[i].unit_name
																	+ "'class='btn_update'>修改</button><button value='"
																	+ json_obj[i].unit_id
																	+ "'class='btn_delete'>刪除</button></td></tr>";
														});
														//判斷查詢結果
														var resultRunTime = 0;
														$.each (json_obj, function (i) {
															resultRunTime+=1;
														});
														$("#products").dataTable().fnDestroy();
														if(resultRunTime!=0){
															$("#products-contain").show();
															$("#products tbody").html(result_table);
															$("#products").dataTable({
																dom: 'lfrB<t>ip',
																buttons: [{
																	extend: 'excel',
																	text: '輸出為execl報表',
																	title: '退貨報表',
																	exportOptions: {modifier: {search: 'none'}}
																  }],
																"language": {"url": "js/dataTables_zh-tw.txt"}
															});
															$(".validateTips").text("");
														}else{
															$("#products-contain").hide();
														}
													}
												});
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

		insert_dialog_form = insert_dialog.find("form").on("submit",
				function(event) {
					event.preventDefault();
				});
		var uuid = "";
		//確認Dialog相關設定(刪除功能)
		confirm_dialog = $("#dialog-confirm").dialog({
			draggable : false,//防止拖曳
			resizable : false,//防止縮放
			autoOpen : false,
			height : 140,
			modal : true,
			buttons : {
				"確認刪除" : function() {
					$.ajax({
						type : "POST",
						url : "salereport.do",
						data : {
							action : "delete",
							unit_id : uuid
						},
						success : function(result) {
							var json_obj = $.parseJSON(result);
							var result_table = "";
							$.each(json_obj,function(i,item) {
										result_table += "<tr><td>"
											+ json_obj[i].unit_name
											+ "</td><td>"
											+ "<button value='"
											+ json_obj[i].unit_id
											+ "'name='"
											+ json_obj[i].unit_name
											+ "'class='btn_update'>修改</button><button value='"
											+ json_obj[i].unit_id
											+ "'class='btn_delete'>刪除</button></td></tr>";
							});
							//判斷查詢結果
							var resultRunTime = 0;
							$.each (json_obj, function (i) {
								resultRunTime+=1;
							});
							$("#products").dataTable().fnDestroy();
							if(resultRunTime!=0){
								$("#products-contain").show();
								$("#products tbody").html(result_table);
								$("#products").dataTable({
									dom: 'lfrB<t>ip',
									buttons: [{
										extend: 'excel',
										text: '輸出為execl報表',
										title: '退貨報表',
										exportOptions: {modifier: {search: 'none'}}
									  }],
									"language": {"url": "js/dataTables_zh-tw.txt"}
								});
							}else{
								$("#products-contain").hide();
							}
						}
					});
					$(this).dialog("close");
				},
				"取消刪除" : function() {
					$(this).dialog("close");
				}
			}
		});
		//修改Dialog相關設定
		update_dialog = $("#dialog-form-update").dialog({
			draggable : false,//防止拖曳
			resizable : false,//防止縮放
			autoOpen : false,
			height : 300,
			width : 350,
			modal : true,
			buttons : [{
				text : "修改",
				click : function() {
					if ($('#update-dialog-form-post').valid()) {
						$.ajax({
							type : "POST",
							url : "salereport.do",
							data : {
	 							action : "update",
	 							unit_id : uuid,
	 							unit_name : $("#dialog-form-update input[name='unit_name']").val()
							},
							success : function(result) {
								var json_obj = $.parseJSON(result);
								var result_table = "";
								$.each(json_obj,function(i,item) {
										result_table += "<tr><td>"
											+ json_obj[i].unit_name
											+ "</td><td>"
											+ "<button value='"
											+ json_obj[i].unit_id
											+ "'name='"
											+ json_obj[i].unit_name
											+ "'class='btn_update'>修改</button><button value='"
											+ json_obj[i].unit_id
											+ "'class='btn_delete'>刪除</button></td></tr>";
								});
								//判斷查詢結果
								var resultRunTime = 0;
								$.each (json_obj, function (i) {
									resultRunTime+=1;
								});
								$("#products").dataTable().fnDestroy();
								if(resultRunTime!=0){
									$("#products-contain").show();
									$("#products tbody").html(result_table);
									$("#products").dataTable({
										dom: 'lfrB<t>ip',
										buttons: [{
											extend: 'excel',
											text: '輸出為execl報表',
											title: '退貨報表',
											exportOptions: {modifier: {search: 'none'}}
										  }],
										"language": {"url": "js/dataTables_zh-tw.txt"}
									});
								}else{
									$("#products-contain").hide();
								}
							}
						});
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
		//刪除事件聆聽 : 因為聆聽事件動態產生，所以採用delegate來批量處理，節省資源
		$("#products").delegate(".btn_delete", "click", function() {
			uuid = $(this).val();
			confirm_dialog.dialog("open");
		});
		//修改事件聆聽
		$("#products").delegate(".btn_update", "click", function() {
			uuid = $(this).val();
			update_dialog.dialog("open");
			var text = $(this).attr("name");
			$("input[name='original_unit_name']").val(text);
		});
		//新增事件聆聽
		$("#create-productunit").button().on("click", function() {
			insert_dialog.dialog("open");
		});
		//預設表格隱藏
		$("#products-contain").hide();
// 		$("#xls").click(function(){
// 			$(".result").table2excel({
// 				exclude: ".noExl",
// 				name: "Excel Document Name",
// 				filename: "退貨資料",
// 				fileext: ".xls",
// 				exclude_img: true,
// 				exclude_links: true,
// 				exclude_inputs: true
// 			});
// 		});
	});
	
</script>
		<div class="input-field-wrap">
			<div class="form-wrap">
				<div class="form-row">
					<label for="">
						<span class="block-label">退貨起日</span>
						<input type="text" class="input-date" id="datepicker1">
					</label>
					<div class="forward-mark"></div>
					<label for="">
						<span class="block-label">退貨迄日</span>
						<input type="text" class="input-date" id="datepicker2">
					</label>
					<a class="btn btn-darkblue" id="searh-productunit">查詢</a>
<!-- 					<a class="btn btn btn-exec" id="xls" style="display:none" >產生報表</a> -->
				</div>
			</div><!-- /.form-wrap -->
		</div>
			<!-- 第二列 -->
			<div class="search-result-wrap" >
				<div id="products-contain" class="result-table-wrap">
					<table id="products" class="result-table">
						<thead>
							<tr class="ui-widget-header">
								<th>退貨日期</th>
								<th>銷貨單號</th>
								<th>訂單號</th>
								<th><p style="width:90px;">產品名稱</p></th>
								<th>客戶自訂產品ID</th>
								<th>銷貨數量</th>
								<th>銷貨金額</th>
								<th>轉單日</th>
								<th>配送日</th>
								<th>銷貨/出貨日期</th>
								<th>銷售平台</th>
								<th>備註</th>								
							</tr>
						</thead>
						<tbody>
						</tbody>
					</table>
				</div>
				<div class="validateTips" align="center"> </div>
			</div>
		</div>
<table id="my123" class="result" style="display:none"><tr><td></td></tr></table>
</body>
</html>