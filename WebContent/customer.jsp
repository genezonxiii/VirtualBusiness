<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ page import="tw.com.aber.customer.controller.*"%>
<%@ page import="java.util.*"%>
<%@ page import="java.sql.DriverManager"%>
<%@ page import="java.sql.Connection"%>
<%@ page import="java.sql.Statement"%>
<%@ page import="java.sql.ResultSet"%>
<jsp:directive.page import="java.sql.SQLException" />

<!DOCTYPE html>
<html>
<head>
<title>客戶管理</title>
<meta charset="utf-8">
<link rel="stylesheet" href="css/styles.css" />
<link href="<c:url value="css/css.css" />" rel="stylesheet">
<link href="<c:url value="css/jquery.dataTables.min.css" />" rel="stylesheet">
<%-- <link href="<c:url value="css/jquery-ui.min.css" />" rel="stylesheet"> --%>
<!-- jquery-ui css要套這一版本，不然Dialog icon會有問題 -->
<link rel="stylesheet" href="//code.jquery.com/ui/1.11.4/themes/smoothness/jquery-ui.css">
<!-- jquery-ui js要套用這一版，不然Dialog會偏移，且容易當掉 -->
<script src="//code.jquery.com/jquery-1.10.2.js"></script>
<script type="text/javascript" src="js/jquery.dataTables.min.js"></script>
<script type="text/javascript" src="js/jquery-ui.min.js"></script>
<script src="//code.jquery.com/ui/1.11.4/jquery-ui.js"></script>
<script type="text/javascript" src="js/jquery-migrate-1.4.1.min.js"></script>
<script type="text/javascript" src="js/jquery.validate.min.js"></script>
<script type="text/javascript" src="js/additional-methods.min.js"></script>
<script type="text/javascript" src="js/messages_zh_TW.min.js"></script>
<script>
window.onload = function (e){ 
	e.preventDefault();
	$.ajax({
		type : "POST",
		url : "customer.do",
		data : {
			action : "search"
		},
		success : function(result) {
			var json_obj = $.parseJSON(result);
			//判斷查詢結果
			var resultRunTime = 0;
			$.each (json_obj, function (i) {
				resultRunTime+=1;
			});
			var result_table = "";
			if(resultRunTime!=0){
				$.each(json_obj,function(i, item) {
					if(json_obj[i].name==null||json_obj[i].name=='NULL'){
				        json_obj[i].name ="";
				    }
					if(json_obj[i].address==null||json_obj[i].address=='NULL'){
				        json_obj[i].address ="";
				    }
					if(json_obj[i].phone==null||json_obj[i].phone=='NULL'){
				        json_obj[i].phone ="";
				    }
					if(json_obj[i].mobile==null||json_obj[i].mobile=='NULL'){
				        json_obj[i].mobile ="";
				    }
					if(json_obj[i].email==null||json_obj[i].email=='NULL'){
				        json_obj[i].email ="";
				    }
					if(json_obj[i].post==null||json_obj[i].post=='NULL'){
				        json_obj[i].post ="";
				    }
					if(json_obj[i].class==null||json_obj[i].class=='NULL'){
				        json_obj[i].class ="";
				    }
					if(json_obj[i].memo==null||json_obj[i].memo=='NULL'){
				        json_obj[i].memo ="";
				    }
					result_table 
					+= "<tr>"
					+ "<td style='width:8%' id='name_"+i+"' name='"+ json_obj[i].name +"'>"+ json_obj[i].name+ "</td>"
					+ "<td style='width:15%' id='address_"+i+"' name='"+ json_obj[i].address +"'>"+ json_obj[i].address+ "</td>"
					+ "<td style='width:14%' id='phone_"+i+"' name='"+ json_obj[i].phone +"'>"+ json_obj[i].phone+ "</td>"
					+ "<td style='width:14%' id='mobile_"+i+"' name='"+ json_obj[i].mobile +"'>"+ json_obj[i].mobile+ "</td>"
					+ "<td style='width:18%' id='email_"+i+"' name='"+ json_obj[i].email +"'>"+ json_obj[i].email+ "</td>"
					+ "<td style='width:8%' id='post_"+i+"' name='"+ json_obj[i].post +"'>"+ json_obj[i].post+ "</td>"
					+ "<td style='width:8%' id='class_"+i+"' name='"+ json_obj[i].class +"'>"+ json_obj[i].class+ "</td>"
					+ "<td style='width:8%' id='memo_"+i+"' name='"+ json_obj[i].memo +"'>"+ json_obj[i].memo+ "</td>"
					+ "<td style='width:7%'><button id="+i+" value='"+ json_obj[i].customer_id + "'class='btn_update'>修改</button>"
					+ "<button id="+i+" value='"+ json_obj[i].customer_id + "'class='btn_delete'>刪除</button></td></tr>";
				});
			}
			if(resultRunTime==0){
				$("#customer-contain").hide();
				$(".validateTips").text("查無此結果");
			}
			$("#customer").dataTable().fnDestroy();
			if(resultRunTime!=0){
				$("#customer-contain").show();
				$("#customer tbody").html(result_table);
				$("#customer").find("td").css("text-align", "center");
				$("#customer").dataTable({"language": {"url": "js/dataTables_zh-tw.txt","zeroRecords": "沒有符合的結果"}});
				$(".validateTips").text("");
			}						
		}
	});
}

	$(function() {
		var uuid = "";
		
		var validator_insert = $("#insert-dialog-form-post").validate({
			rules : {
				post : {
					digits : true,
					maxlength : 5
				},
				email : {
					email: true
				}
			}
		});
		var validator_update = $("#update-dialog-form-post").validate({
			rules : {
				post : {
					digits : true,
					maxlength : 5
				},
				email : {
					email: true
				}
			}
		});	
					
		//新增事件聆聽
		$("#create-customer").button().on("click", function(e) {
			e.preventDefault();		
			insert_dialog.dialog("open");
		});
		
		// "新增" Dialog相關設定
		insert_dialog = $("#dialog-form-insert").dialog({
			draggable : false,//防止拖曳
			resizable : false,//防止縮放
			autoOpen : false,
			show : {
				effect : "blind",
				duration : 1000
			},
			hide : {
				effect : "explode",
				duration : 1000
			},
			width : 'auto',
			modal : true,
			buttons : [{
				id : "insert",
				text : "新增",
				click : function() {
					if ($('#insert-dialog-form-post').valid()) {
						$.ajax({
							type : "POST",
							url : "customer.do",
							data : {
								action : "insert",
								name : $("#dialog-form-insert input[name='name']").val(),
								address : $("#dialog-form-insert input[name='address']").val(),
								phone : $("#dialog-form-insert input[name='phone']").val(),
								mobile : $("#dialog-form-insert input[name='mobile']").val(),
								email : $("#dialog-form-insert input[name='email']").val(),
								post : $("#dialog-form-insert input[name='post']").val(),
								class : $("#dialog-form-insert input[name='class']").val(),
								memo : $("#dialog-form-insert input[name='memo']").val()
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
									if(i<len-1){
										if(json_obj[i].name==null||json_obj[i].name=='NULL'){
									        json_obj[i].name ="";
									    }
										if(json_obj[i].address==null||json_obj[i].address=='NULL'){
									        json_obj[i].address ="";
									    }
										if(json_obj[i].phone==null||json_obj[i].phone=='NULL'){
									        json_obj[i].phone ="";
									    }
										if(json_obj[i].mobile==null||json_obj[i].mobile=='NULL'){
									        json_obj[i].mobile ="";
									    }
										if(json_obj[i].email==null||json_obj[i].email=='NULL'){
									        json_obj[i].email ="";
									    }
										if(json_obj[i].post==null||json_obj[i].post=='NULL'){
									        json_obj[i].post ="";
									    }
										if(json_obj[i].class==null||json_obj[i].class=='NULL'){
									        json_obj[i].class ="";
									    }
										if(json_obj[i].memo==null||json_obj[i].memo=='NULL'){
									        json_obj[i].memo ="";
									    }
										result_table 
										+= "<tr>"
										+ "<td style='width:8%' id='name_"+i+"' name='"+ json_obj[i].name +"'>"+ json_obj[i].name+ "</td>"
										+ "<td style='width:15%' id='address_"+i+"' name='"+ json_obj[i].address +"'>"+ json_obj[i].address+ "</td>"
										+ "<td style='width:14%' id='phone_"+i+"' name='"+ json_obj[i].phone +"'>"+ json_obj[i].phone+ "</td>"
										+ "<td style='width:14%' id='mobile_"+i+"' name='"+ json_obj[i].mobile +"'>"+ json_obj[i].mobile+ "</td>"
										+ "<td style='width:18%' id='email_"+i+"' name='"+ json_obj[i].email +"'>"+ json_obj[i].email+ "</td>"
										+ "<td style='width:8%' id='post_"+i+"' name='"+ json_obj[i].post +"'>"+ json_obj[i].post+ "</td>"
										+ "<td style='width:8%' id='class_"+i+"' name='"+ json_obj[i].class +"'>"+ json_obj[i].class+ "</td>"
										+ "<td style='width:8%' id='memo_"+i+"' name='"+ json_obj[i].memo +"'>"+ json_obj[i].memo+ "</td>"
										+ "<td style='width:7%'><button id="+i+" value='"+ json_obj[i].customer_id + "'class='btn_update'>修改</button>"
										+ "<button id="+i+" value='"+ json_obj[i].customer_id + "'class='btn_delete'>刪除</button></td></tr>";
									}
								});
								$("#customer").dataTable().fnDestroy();
								if(resultRunTime!=0){
									$("#customer-contain").show();
									$("#customer tbody").html(result_table);
									$("#customer").find("td").css("text-align", "center");
									$("#customer").dataTable({"language": {"url": "js/dataTables_zh-tw.txt","zeroRecords": "沒有符合的結果"}});
									$(".validateTips").text("");
								}else{
									$("#customer-contain").hide();
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
					$("#insert-dialog-form-post").trigger("reset");
					insert_dialog.dialog("close");
				}
			} ],
			close : function() {
				validator_insert.resetForm();
				$("#insert-dialog-form-post").trigger("reset");
			}
		}).css("font-size", "order_source25px");
		
		//修改事件聆聽
		$("#customer").delegate(".btn_update", "click", function(e) {
			e.preventDefault();
			customer_id = $(this).val();
			row = $(this).attr("id");
			$("#dialog-form-update input[name='customer_id']").val(customer_id);
			$("#dialog-form-update input[name='name']").val($('#name_'+row).html());
			$("#dialog-form-update input[name='address']").val($('#address_'+row).html());
			$("#dialog-form-update input[name='phone']").val($('#phone_'+row).html());
			$("#dialog-form-update input[name='mobile']").val($('#mobile_'+row).html());
			$("#dialog-form-update input[name='email']").val($('#email_'+row).html());
			$("#dialog-form-update input[name='post']").val($('#post_'+row).html());
			$("#dialog-form-update input[name='class']").val($('#class_'+row).html());
			$("#dialog-form-update input[name='memo']").val($('#memo_'+row).html());
			update_dialog.dialog("open");
		});
		
		// "修改" Dialog相關設定
		update_dialog = $("#dialog-form-update").dialog({
			draggable : false,//防止拖曳
			resizable : false,//防止縮放
			autoOpen : false,
			width : 'auto',
			modal : true,
			buttons : [{
				text : "修改",
				click : function() {
					if ($('#update-dialog-form-post').valid()) {
						$.ajax({
							type : "POST",
							url : "customer.do",
							data : {
	 							action : "update",
								customer_id : $("#dialog-form-update input[name='customer_id']").val(),
	 							name : $("#dialog-form-update input[name='name']").val(),
	 							address : $("#dialog-form-update input[name='address']").val(),
								phone : $("#dialog-form-update input[name='phone']").val(),
								mobile : $("#dialog-form-update input[name='mobile']").val(),
								email : $("#dialog-form-update input[name='email']").val(),
								post : $("#dialog-form-update input[name='post']").val(),
								class : $("#dialog-form-update input[name='class']").val(),
								memo : $("#dialog-form-update input[name='memo']").val()
							},
							success : function(result) {
								var json_obj = $.parseJSON(result);
								//判斷查詢結果
								var resultRunTime = 0;
								$.each (json_obj, function (i) {
									resultRunTime+=1;
								});
								var result_table = "";
								if(resultRunTime!=0){
									$.each(json_obj,function(i, item) {
										if(json_obj[i].name==null||json_obj[i].name=='NULL'){
									        json_obj[i].name ="";
									    }
										if(json_obj[i].address==null||json_obj[i].address=='NULL'){
									        json_obj[i].address ="";
									    }
										if(json_obj[i].phone==null||json_obj[i].phone=='NULL'){
									        json_obj[i].phone ="";
									    }
										if(json_obj[i].mobile==null||json_obj[i].mobile=='NULL'){
									        json_obj[i].mobile ="";
									    }
										if(json_obj[i].email==null||json_obj[i].email=='NULL'){
									        json_obj[i].email ="";
									    }
										if(json_obj[i].post==null||json_obj[i].post=='NULL'){
									        json_obj[i].post ="";
									    }
										if(json_obj[i].class==null||json_obj[i].class=='NULL'){
									        json_obj[i].class ="";
									    }
										if(json_obj[i].memo==null||json_obj[i].memo=='NULL'){
									        json_obj[i].memo ="";
									    }
										result_table 
										+= "<tr>"
										+ "<td style='width:8%' id='name_"+i+"' name='"+ json_obj[i].name +"'>"+ json_obj[i].name+ "</td>"
										+ "<td style='width:15%' id='address_"+i+"' name='"+ json_obj[i].address +"'>"+ json_obj[i].address+ "</td>"
										+ "<td style='width:14%' id='phone_"+i+"' name='"+ json_obj[i].phone +"'>"+ json_obj[i].phone+ "</td>"
										+ "<td style='width:14%' id='mobile_"+i+"' name='"+ json_obj[i].mobile +"'>"+ json_obj[i].mobile+ "</td>"
										+ "<td style='width:18%' id='email_"+i+"' name='"+ json_obj[i].email +"'>"+ json_obj[i].email+ "</td>"
										+ "<td style='width:8%' id='post_"+i+"' name='"+ json_obj[i].post +"'>"+ json_obj[i].post+ "</td>"
										+ "<td style='width:8%' id='class_"+i+"' name='"+ json_obj[i].class +"'>"+ json_obj[i].class+ "</td>"
										+ "<td style='width:8%' id='memo_"+i+"' name='"+ json_obj[i].memo +"'>"+ json_obj[i].memo+ "</td>"
										+ "<td style='width:7%'><button id="+i+" value='"+ json_obj[i].customer_id + "'class='btn_update'>修改</button>"
										+ "<button id="+i+" value='"+ json_obj[i].customer_id + "'class='btn_delete'>刪除</button></td></tr>";
									});
								}
								$("#customer").dataTable().fnDestroy();
								if(resultRunTime!=0){
									$("#customer-contain").show();
									$("#customer tbody").html(result_table);
									$("#customer").find("td").css("text-align", "center");
									$("#customer").dataTable({"language": {"url": "js/dataTables_zh-tw.txt","zeroRecords": "沒有符合的結果"}});
									$(".validateTips").text("");
								}else{
									$("#customer-contain").hide();
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
					$("#update-dialog-form-post").trigger("reset");
					update_dialog.dialog("close");
				}
			} ],
			close : function() {
				$("#update-dialog-form-post").trigger("reset");
				validator_update.resetForm();
			}
		});
		
		//刪除事件聆聽 : 因為聆聽事件動態產生，所以採用delegate來批量處理，節省資源
		$("#customer").delegate(".btn_delete", "click", function() {
			uuid = $(this).val();
			confirm_dialog.dialog("open");
		});
		
		// "刪除" Dialog相關設定
		confirm_dialog = $("#dialog-confirm").dialog({
			draggable : false,//防止拖曳
			resizable : false,//防止縮放
			autoOpen : false,
			height : 'auto',
			modal : true,
			buttons : {
				"確認刪除" : function() {
					$.ajax({
						type : "POST",
						url : "customer.do",
						data : {
							action : "delete",
							customer_id : uuid
						},
						success : function(result) {
							var json_obj = $.parseJSON(result);
							//判斷查詢結果
							var resultRunTime = 0;
							$.each (json_obj, function (i) {
								resultRunTime+=1;
							});
							var result_table = "";
							if(resultRunTime!=0){
								$.each(json_obj,function(i, item) {
									if(json_obj[i].name==null||json_obj[i].name=='NULL'){
								        json_obj[i].name ="";
								    }
									if(json_obj[i].address==null||json_obj[i].address=='NULL'){
								        json_obj[i].address ="";
								    }
									if(json_obj[i].phone==null||json_obj[i].phone=='NULL'){
								        json_obj[i].phone ="";
								    }
									if(json_obj[i].mobile==null||json_obj[i].mobile=='NULL'){
								        json_obj[i].mobile ="";
								    }
									if(json_obj[i].email==null||json_obj[i].email=='NULL'){
								        json_obj[i].email ="";
								    }
									if(json_obj[i].post==null||json_obj[i].post=='NULL'){
								        json_obj[i].post ="";
								    }
									if(json_obj[i].class==null||json_obj[i].class=='NULL'){
								        json_obj[i].class ="";
								    }
									if(json_obj[i].memo==null||json_obj[i].memo=='NULL'){
								        json_obj[i].memo ="";
								    }
									result_table 
									+= "<tr>"
									+ "<td style='width:8%' id='name_"+i+"' name='"+ json_obj[i].name +"'>"+ json_obj[i].name+ "</td>"
									+ "<td style='width:15%' id='address_"+i+"' name='"+ json_obj[i].address +"'>"+ json_obj[i].address+ "</td>"
									+ "<td style='width:14%' id='phone_"+i+"' name='"+ json_obj[i].phone +"'>"+ json_obj[i].phone+ "</td>"
									+ "<td style='width:14%' id='mobile_"+i+"' name='"+ json_obj[i].mobile +"'>"+ json_obj[i].mobile+ "</td>"
									+ "<td style='width:18%' id='email_"+i+"' name='"+ json_obj[i].email +"'>"+ json_obj[i].email+ "</td>"
									+ "<td style='width:8%' id='post_"+i+"' name='"+ json_obj[i].post +"'>"+ json_obj[i].post+ "</td>"
									+ "<td style='width:8%' id='class_"+i+"' name='"+ json_obj[i].class +"'>"+ json_obj[i].class+ "</td>"
									+ "<td style='width:8%' id='memo_"+i+"' name='"+ json_obj[i].memo +"'>"+ json_obj[i].memo+ "</td>"
									+ "<td style='width:7%'><button id="+i+" value='"+ json_obj[i].customer_id + "'class='btn_update'>修改</button>"
									+ "<button id="+i+" value='"+ json_obj[i].customer_id + "'class='btn_delete'>刪除</button></td></tr>";
								});
							}
							$("#customer").dataTable().fnDestroy();
							if(resultRunTime!=0){
								$("#customer-contain").show();
								$("#customer tbody").html(result_table);
								$("#customer").find("td").css("text-align", "center");
								$("#customer").dataTable({"language": {"url": "js/dataTables_zh-tw.txt","zeroRecords": "沒有符合的結果"}});
								$(".validateTips").text("");
							}else{
								$("#customer-contain").hide();
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
		
		//預設表格隱藏
		$("#customer-contain").hide();
	})
</script>
</head>
<body style="font-size: 15px;">
<div style="margin:20px;">
	<div class="panel-title">
		<h2 style="font-size: 25px;">客戶管理</h2>
	</div>
	<div class="panel-content">
		<div class="datalistWrap">
			<!--對話窗樣式-確認 -->
			<div id="dialog-confirm" title="確認刪除資料嗎?">
				<p>是否確認刪除該筆資料</p>
			</div>		
			<!--對話窗樣式-修改 -->
			<div id="dialog-form-update" title="修改客戶資料">
				<form name="update-dialog-form-post" id="update-dialog-form-post">
					<fieldset>
						<table>
							<tbody>
								<tr>
									<td style="text-align:right" >客戶姓名:&nbsp&nbsp</td>
									<td style="text-align:left" ><input type="text" name="name"  placeholder="輸入客戶姓名"></td>
									<td width="30px"></td>
									<td style="text-align:right" >收貨地址:&nbsp&nbsp</td>
									<td style="text-align:left" ><input type="text" name="address" placeholder="輸入收貨地址"></td>
								</tr>
								<tr>
									<td style="text-align:right" >電話:&nbsp&nbsp</td>
									<td style="text-align:left" ><input type="text" name="phone"  placeholder="輸入電話"></td>
									<td width="30px"></td>
									<td style="text-align:right" >手機:&nbsp&nbsp</td>
									<td style="text-align:left" ><input type="text" name="mobile"  placeholder="輸入手機"></td>
								</tr>
								<tr>
									<td style="text-align:right" >Email:&nbsp&nbsp</td>
									<td style="text-align:left" ><input type="text" name="email"  placeholder="輸入Email" ></td>
									<td width="30px"></td>
									<td style="text-align:right" >郵政編號:&nbsp&nbsp</td>
									<td style="text-align:left" ><input type="text" name="post"  placeholder="輸入郵政編號" ></td>
								</tr>
								<tr>
									<!-- 
									<td style="text-align:right" >客戶等級:&nbsp&nbsp</td>
									<td style="text-align:left" ><input type="text" name="class"  placeholder="輸入客戶等級" ></td>
									<td width="30px"></td>
									-->
									<td style="text-align:right" >備註:&nbsp&nbsp</td>
									<td style="text-align:left" ><input type="text" name="memo"  placeholder="輸入備註說明" ></td>
								</tr>
							</tbody>
						</table>
						<input type="hidden" name="customer_id"> 
					</fieldset>
				</form>
			</div>					
			<!--對話窗樣式-新增 -->
			<div id="dialog-form-insert" title="新增客戶資料">
				<form name="insert-dialog-form-post" id="insert-dialog-form-post" style="display:inline">
					<fieldset>
						<table>
							<tbody>
								<tr>
									<td style="text-align:right" >客戶姓名:&nbsp&nbsp</td>
									<td style="text-align:left" ><input type="text" name="name"  placeholder="輸入客戶姓名"></td>
									<td width="30px"></td>
									<td style="text-align:right" >收貨地址:&nbsp&nbsp</td>
									<td style="text-align:left" ><input type="text" name="address" placeholder="輸入收貨地址"></td>
								</tr>
								<tr>
									<td style="text-align:right" >電話:&nbsp&nbsp</td>
									<td style="text-align:left" ><input type="text" name="phone"  placeholder="輸入電話"></td>
									<td width="30px"></td>
									<td style="text-align:right" >手機:&nbsp&nbsp</td>
									<td style="text-align:left" ><input type="text" name="mobile"  placeholder="輸入手機"></td>
								</tr>
								<tr>
									<td style="text-align:right" >Email:&nbsp&nbsp</td>
									<td style="text-align:left" ><input type="text" name="email"  placeholder="輸入Email" ></td>
									<td width="30px"></td>
									<td style="text-align:right" >郵政編號:&nbsp&nbsp</td>
									<td style="text-align:left" ><input type="text" name="post"  placeholder="輸入郵政編號" ></td>
								</tr>
								<tr>
									<!-- 
									<td style="text-align:right" >客戶等級:&nbsp&nbsp</td>
									<td style="text-align:left" ><input type="text" name="class"  placeholder="輸入客戶等級" ></td>
									<td width="30px"></td>
									 -->
									<td style="text-align:right" >備註:&nbsp&nbsp</td>
									<td style="text-align:left" ><input type="text" name="memo"  placeholder="輸入備註說明" ></td>
								</tr>
							</tbody>
						</table>
					</fieldset>
				</form>
			</div>	
			<!-- 第一列 -->
			<div class="row" align="center">
				<button id="create-customer">新增</button>
			</div>
			<!-- 第二列 -->
			<div class="row" align="center">
				<div id="customer-contain" class="ui-widget">
					<table id="customer" class="ui-widget ui-widget-content">
						<thead>
							<tr class="ui-widget-header">
								<th style='width:8%'>客戶姓名</th>
								<th style='width:15%'>收貨地址</th>
								<th style='width:14%'>電話</th>
								<th style='width:14%'>手機</th>
								<th style='width:18%'>Email</th>
								<th style='width:8%'>郵政編號</th>
								<th style='width:8%'>客戶等級</th>
								<th style='width:8%'>備註</th>
								<th style='width:7%'>功能</th>
							</tr>	
					</table>
				</div>
				<span class="validateTips"> </span>
			</div>	
		</div>
	</div>
</div>
</body>
</html>