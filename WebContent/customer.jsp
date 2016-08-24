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
<link rel="Shortcut Icon" type="image/x-icon" href="./images/Rockettheme-Ecommerce-Shop.ico" />
<link rel="stylesheet" href="css/styles.css" />
<link href="<c:url value="css/css.css" />" rel="stylesheet">
<link href="<c:url value="css/jquery.dataTables.min.css" />" rel="stylesheet">
<%-- <link href="<c:url value="css/jquery-ui.min.css" />" rel="stylesheet"> --%>
<!-- jquery-ui css要套這一版本，不然Dialog icon會有問題 -->
<link rel="stylesheet" href="css/1.11.4/jquery-ui.css">
</head>
<body>
	<jsp:include page="template.jsp" flush="true"/>
	<div class="content-wrap" >

<!-- jquery-ui js要套用這一版，不然Dialog會偏移，且容易當掉 -->
<script type="text/javascript" src="js/jquery-1.10.2.js"></script>
<script type="text/javascript" src="js/jquery.dataTables.min.js"></script>
<script type="text/javascript" src="js/jquery-ui.min.js"></script>
<script type="text/javascript" src="js/jquery-ui.js"></script>
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
					+ "<td id='name_"+i+"' name='"+ json_obj[i].name +"'>"+ json_obj[i].name+ "</td>"
					+ "<td id='address_"+i+"' name='"+ json_obj[i].address +"'>"+ json_obj[i].address+ "</td>"
					+ "<td id='phone_"+i+"' name='"+ json_obj[i].phone +"'>"+ json_obj[i].phone+ "</td>"
					+ "<td id='mobile_"+i+"' name='"+ json_obj[i].mobile +"'>"+ json_obj[i].mobile+ "</td>"
					+ "<td id='email_"+i+"' name='"+ json_obj[i].email +"'>"+ json_obj[i].email+ "</td>"
					+ "<td id='post_"+i+"' name='"+ json_obj[i].post +"'>"+ json_obj[i].post+ "</td>"
					+ "<td id='class_"+i+"' name='"+ json_obj[i].class +"'>"+ json_obj[i].class+ "</td>"
					+ "<td id='memo_"+i+"' name='"+ json_obj[i].memo +"'>"+ json_obj[i].memo+ "</td>"
					+ "<td><div class='table-row-func btn-in-table btn-gray'><i class='fa fa-ellipsis-h'></i>"
					+ "	<div class='table-function-list'>"
					+ "		<button class='btn-in-table btn-darkblue btn_update' title='修改' id="+i+" value='"+ json_obj[i].customer_id + "' ><i class='fa fa-pencil'></i></button>"
					+ "		<button class='btn-in-table btn-alert btn_delete' title='刪除' id="+i+" value='"+ json_obj[i].customer_id + "' ><i class='fa fa-trash'></i></button>"
					+ "	</div></div></td></tr>";
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
				$("#customer").find("th").css({"word-break":"break-all","min-width":"70px","text-align":"center" });
				$("#customer").find("td").css({"word-break":"break-all","min-width":"70px","text-align":"center" });
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
		$("#create-customer").click(function(e) {
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
				duration : 300
			},
			hide : {
				effect : "fade",
				duration : 300
			},
			width : 'auto',
			modal : true,
			buttons : [{
				id : "insert",
				text : "新增",
				click : function() {
					if ($('#insert-dialog-form-post').valid()) {
						//alert("進來喔");
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
								//alert(result);
								var json_obj = $.parseJSON(result);
								var len=json_obj.length;
								//判斷查詢結果
								var resultRunTime = 0;
								$.each (json_obj, function (i) {
									resultRunTime+=1;
								});
								var result_table = "";
								$.each(json_obj,function(i, item) {
									if(i<len){
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
										+ "<td id='name_"+i+"' name='"+ json_obj[i].name +"'>"+ json_obj[i].name+ "</td>"
										+ "<td id='address_"+i+"' name='"+ json_obj[i].address +"'>"+ json_obj[i].address+ "</td>"
										+ "<td id='phone_"+i+"' name='"+ json_obj[i].phone +"'>"+ json_obj[i].phone+ "</td>"
										+ "<td id='mobile_"+i+"' name='"+ json_obj[i].mobile +"'>"+ json_obj[i].mobile+ "</td>"
										+ "<td id='email_"+i+"' name='"+ json_obj[i].email +"'>"+ json_obj[i].email+ "</td>"
										+ "<td id='post_"+i+"' name='"+ json_obj[i].post +"'>"+ json_obj[i].post+ "</td>"
										+ "<td id='class_"+i+"' name='"+ json_obj[i].class +"'>"+ json_obj[i].class+ "</td>"
										+ "<td id='memo_"+i+"' name='"+ json_obj[i].memo +"'>"+ json_obj[i].memo+ "</td>"
										+ "<td><div class='table-row-func btn-in-table btn-gray'><i class='fa fa-ellipsis-h'></i>"
										+ "	<div class='table-function-list'>"
										+ "		<button class='btn-in-table btn-darkblue btn_update' title='修改' id="+i+" value='"+ json_obj[i].customer_id + "' ><i class='fa fa-pencil'></i></button>"
										+ "		<button class='btn-in-table btn-alert btn_delete' title='刪除' id="+i+" value='"+ json_obj[i].customer_id + "' ><i class='fa fa-trash'></i></button>"
										+ "	</div></div></td></tr>";
									}
								});
								$("#customer").dataTable().fnDestroy();
								if(resultRunTime!=0){
									$("#customer-contain").show();
									$("#customer tbody").html(result_table);
									$("#customer").find("td").css("text-align", "center");
									$("#customer").dataTable({"language": {"url": "js/dataTables_zh-tw.txt","zeroRecords": "沒有符合的結果"}});
									$("#customer").find("th").css({"word-break":"break-all","min-width":"70px","text-align":"center" });
								    $("#customer").find("td").css({"word-break":"break-all","min-width":"70px","text-align":"center" });
									$(".validateTips").text("");
								}else{
									$("#customer-contain").hide();
								}
							}
						});
						$("#insert-dialog-form-post").trigger("reset");
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
		$("#dialog-form-insert").show();
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
			show : {effect : "blind",duration : 300},
			hide : {effect : "fade",duration : 300},
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
								//alert(result);
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
										+ "<td id='name_"+i+"' name='"+ json_obj[i].name +"'>"+ json_obj[i].name+ "</td>"
										+ "<td id='address_"+i+"' name='"+ json_obj[i].address +"'>"+ json_obj[i].address+ "</td>"
										+ "<td id='phone_"+i+"' name='"+ json_obj[i].phone +"'>"+ json_obj[i].phone+ "</td>"
										+ "<td id='mobile_"+i+"' name='"+ json_obj[i].mobile +"'>"+ json_obj[i].mobile+ "</td>"
										+ "<td id='email_"+i+"' name='"+ json_obj[i].email +"'>"+ json_obj[i].email+ "</td>"
										+ "<td id='post_"+i+"' name='"+ json_obj[i].post +"'>"+ json_obj[i].post+ "</td>"
										+ "<td id='class_"+i+"' name='"+ json_obj[i].class +"'>"+ json_obj[i].class+ "</td>"
										+ "<td id='memo_"+i+"' name='"+ json_obj[i].memo +"'>"+ json_obj[i].memo+ "</td>"
										+ "<td><div class='table-row-func btn-in-table btn-gray'><i class='fa fa-ellipsis-h'></i>"
										+ "	<div class='table-function-list'>"
										+ "		<button class='btn-in-table btn-darkblue btn_update' title='修改' id="+i+" value='"+ json_obj[i].customer_id + "' ><i class='fa fa-pencil'></i></button>"
										+ "		<button class='btn-in-table btn-alert btn_delete' title='刪除' id="+i+" value='"+ json_obj[i].customer_id + "' ><i class='fa fa-trash'></i></button>"
										+ "	</div></div></td></tr>";
									});
								}
								$("#customer").dataTable().fnDestroy();
								if(resultRunTime!=0){
									$("#customer-contain").show();
									$("#customer tbody").html(result_table);
									$("#customer").find("td").css("text-align", "center");
									$("#customer").dataTable({"language": {"url": "js/dataTables_zh-tw.txt","zeroRecords": "沒有符合的結果"}});
									$("#customer").find("th").css({"word-break":"break-all","min-width":"70px","text-align":"center" });
									$("#customer").find("td").css({"word-break":"break-all","min-width":"70px","text-align":"center" });
									$(".validateTips").text("");
								}else{
									$("#customer-contain").hide();
								}
							}
						});
						update_dialog.dialog("close");
						$("#update-dialog-form-post").trigger("reset");
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
		$("#dialog-form-update").show();
		
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
			show : {effect : "blind",duration : 300},
			hide : {effect : "fade",duration : 300},
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
									+ "<td id='name_"+i+"' name='"+ json_obj[i].name +"'>"+ json_obj[i].name+ "</td>"
									+ "<td id='address_"+i+"' name='"+ json_obj[i].address +"'>"+ json_obj[i].address+ "</td>"
									+ "<td id='phone_"+i+"' name='"+ json_obj[i].phone +"'>"+ json_obj[i].phone+ "</td>"
									+ "<td id='mobile_"+i+"' name='"+ json_obj[i].mobile +"'>"+ json_obj[i].mobile+ "</td>"
									+ "<td id='email_"+i+"' name='"+ json_obj[i].email +"'>"+ json_obj[i].email+ "</td>"
									+ "<td id='post_"+i+"' name='"+ json_obj[i].post +"'>"+ json_obj[i].post+ "</td>"
									+ "<td id='class_"+i+"' name='"+ json_obj[i].class +"'>"+ json_obj[i].class+ "</td>"
									+ "<td id='memo_"+i+"' name='"+ json_obj[i].memo +"'>"+ json_obj[i].memo+ "</td>"
									+ "<td><div class='table-row-func btn-in-table btn-gray'><i class='fa fa-ellipsis-h'></i>"
									+ "	<div class='table-function-list'>"
									+ "		<button class='btn-in-table btn-darkblue btn_update' title='修改' id="+i+" value='"+ json_obj[i].customer_id + "' ><i class='fa fa-pencil'></i></button>"
									+ "		<button class='btn-in-table btn-alert btn_delete' title='刪除' id="+i+" value='"+ json_obj[i].customer_id + "' ><i class='fa fa-trash'></i></button>"
									+ "	</div></div></td></tr>";
								});
							}
							$("#customer").dataTable().fnDestroy();
							if(resultRunTime!=0){
								$("#customer-contain").show();
								$("#customer tbody").html(result_table);
								$("#customer").find("td").css("text-align", "center");
								$("#customer").dataTable({"language": {"url": "js/dataTables_zh-tw.txt","zeroRecords": "沒有符合的結果"}});
								$("#customer").find("th").css({"word-break":"break-all","min-width":"70px","text-align":"center" });
								$("#customer").find("td").css({"word-break":"break-all","min-width":"70px","text-align":"center" });
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
		 $("#dialog-confirm").show();
		
	});
</script>

	<div class="panel-content">
		<div class="datalistWrap">
			<!--對話窗樣式-確認 -->
			<div id="dialog-confirm" title="確認刪除資料嗎?" style="display:none;">
				<p>是否確認刪除該筆資料</p>
			</div>		
			<!--對話窗樣式-修改 -->
			<div id="dialog-form-update" title="修改客戶資料" style="display:none;">
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
			<div id="dialog-form-insert" title="新增客戶資料" style="display:none;">
				<form name="insert-dialog-form-post" id="insert-dialog-form-post" style="display:inline">
					<fieldset>
						<table class="result-table">
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
			<div class="input-field-wrap">
				<div class="form-wrap">
					<div class="btn-row">
						<button class="btn btn-exec btn-wide" id="create-customer">新增客戶資料</button>
					</div>
				</div>
			</div>
<!-- 			<div class="row" align="center"> -->
<!-- 				<button id="create-customer">新增</button> -->
<!-- 			</div> -->
			<!-- 第二列 -->

			<div class="row search-result-wrap" align="center" id ="sales_contain_row">
				<div id="customer-contain" class="ui-widget" style="display:none">
					<table id="customer" class="result-table">
						<thead>
							<tr class="ui-widget-header">
								<th>客戶姓名</th>
								<th>收貨地址</th>
								<th>電話</th>
								<th>手機</th>
								<th>Email</th>
								<th>郵政編號</th>
								<th>客戶等級</th>
								<th>備註</th>
								<th>功能</th>
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