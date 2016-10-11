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
<title>公司管理</title>
<meta charset="utf-8">
<link rel="Shortcut Icon" type="image/x-icon" href="./images/Rockettheme-Ecommerce-Shop.ico" />
<link rel="stylesheet" href="css/styles.css" />
<link href="<c:url value="css/css.css" />" rel="stylesheet">
<link href="<c:url value="css/jquery.dataTables.min.css" />" rel="stylesheet">
<!-- <link rel="stylesheet" href="//code.jquery.com/ui/1.11.4/themes/smoothness/jquery-ui.css"> -->
<style>
form input{
	width:240px;
}
table.result-table td:nth-child(2n){
	font-size:22px;
	font-family: "Times New Roman", Times, serif;
	font-family: DFKai-sb;
	font-family:"微軟正黑體", "Microsoft JhengHei", 'LiHei Pro', Arial, Helvetica, sans-serif ;
 	font-weight: lighter; 
 	padding-left: 60px;
}
</style>
</head>
<body>
	<jsp:include page="template.jsp" flush="true"/>
	<div class="content-wrap" >
		<div class='bdyplane' style="opacity:0">
<!-- <script src="//code.jquery.com/jquery-1.10.2.js"></script> -->
<script type="text/javascript" src="js/jquery.dataTables.min.js"></script>
<script type="text/javascript" src="js/jquery-ui.min.js"></script>
<!-- <script src="//code.jquery.com/ui/1.11.4/jquery-ui.js"></script> -->
<script type="text/javascript" src="js/jquery-migrate-1.4.1.min.js"></script>
<script type="text/javascript" src="js/jquery.validate.min.js"></script>
<script type="text/javascript" src="js/additional-methods.min.js"></script>
<script type="text/javascript" src="js/messages_zh_TW.min.js"></script>
<script>
	$(function() {
		$(".bdyplane").animate({"opacity":"1"});
		var validator_update = $("#update-dialog-form-post").validate({
			rules : {
				group_name : {
					required : true,
					maxlength : 10
				},
				address : {
					required : true,
					maxlength : 80
				},
				phone : {
					required : true,
					maxlength : 12
				},
				fax : {
					required : true,
					maxlength : 12
				},
				mobile : {
					required : true,
					maxlength : 15
				},
				email : {
					required : true,
					maxlength : 30
				},
				master : {
					required : true,
					maxlength : 10
				},
				invoice_path : {
					maxlength : 50
				}
			},
			messages : {
				group_name : {
					maxlength : "長度不能超過10個字"
				}
			}
		});
		//melvin begin
		$("[name^=group_unicode]").rules("add", {
			required: true,
			number: true,
			customUnicode: true
		});
		
		$.validator.addMethod('customUnicode',function(value, element, param) {
			if(checkunicode(value)){
				return true;
			} else {
				return false;
			}

           	return isValid; // return bool here if valid or not.
       	}, '統編輸入錯誤');
		//melvin end
		var group_name = $("#group_name");
		//查詢相關設定
		$.ajax({
				type : "POST",
				url : "group.do",
				data : {
					action : "searh",
					group_name : $("#dialog-form-searh input[name='search_group_name']" ).val()
				},
				success : function(result) {
						var json_obj = $.parseJSON(result);
						var result_table = "";
						$.each(json_obj,function(i, item) {
							result_table += 
								"<tr><td>"+"公司名稱"+"</td><td>"+json_obj[i].group_name +"</td>"
								+"<tr><td>"+"統一編號"+"</td><td>"+ json_obj[i].group_unicode+"</td>"
								+ "<tr><td>"+"公司地址"+"</td><td>"+ json_obj[i].address+"</td>"
								+ "<tr><td>"+"公司電話"+"</td><td>"+ json_obj[i].phone+ "</td>"
								+ "<tr><td>"+"公司傳真"+ "</td><td>"+ json_obj[i].fax+"</td>"
								+ "<tr><td>"+ "負責人"+"</td><td>"+json_obj[i].master+"</td>"
								+ "<tr><td>"+"負責人Email"+"</td><td>"+ json_obj[i].email+ "</td>"
								+ "<tr><td>"+"負責人手機"+"</td><td>"+ json_obj[i].mobile+"</td></tr>"
								+ "<tr><td>"+"電子發票路徑"+"</td><td>"+ json_obj[i].invoice_path+"</td></tr>"
								+ "<tr><td>"+ "功能"+"</td><td>"
								+ "<div class='table-row-func btn-in-table btn-gray'><i class='fa fa-ellipsis-h'></i>"
								+ "	<div class='table-function-list'>"
								+ "		<button class='btn-in-table btn-darkblue btn_update' title='修改' value='"+ json_obj[i].group_id+ "'name='"+ json_obj[i].group_name+"' ><i class='fa fa-pencil'></i></button>"
								+ "	</div></div>"
								+ "</td></tr>";
						});
						//判斷查詢結果
						var resultRunTime = 0;
						$.each (json_obj, function (i) {
							resultRunTime+=1;
						});
			// 			$("#products").dataTable().fnDestroy();
						if(resultRunTime!=0){
							$("#products-contain").show();
							$("#products tbody").html(result_table);
			// 				$("#products").dataTable({"bFilter": false, "bInfo": false, "paging": false, "bSort": false, "language": {"url": "js/dataTables_zh-tw.txt","zeroRecords": "沒有符合的結果"}});
							warning_msg("");
						}else{
							$("#products-contain").hide();
							warning_msg("---查無此結果---");
						}
					}
				});
		//修改Dialog相關設定
		update_dialog = $("#dialog-form-update").dialog({
			draggable : true,//防止拖曳
			resizable : false,//防止縮放
			autoOpen : false,
			height : "auto",
			width : "auto",
			modal : true,
			show : {effect : "blind",duration : 300},
			hide : {effect : "fade",duration : 300},
			buttons : [{
				text : "修改",
				click : function() {
					$("#dialog-form-update input[name='invoice_path']").val($("#dialog-form-update input[name='invoice_path']").val().replace(new RegExp("\\\\","g"),"/"));
					if ($('#update-dialog-form-post').valid()) {
						$.ajax({
							type : "POST",
							url : "group.do",
							data : {
	 							action : "update",
	 							unit_id : uuid,
	 							group_name : $("#dialog-form-update input[name='group_name']" ).val(),
	 							group_unicode : $("#dialog-form-update input[name='group_unicode']" ).val(),
	 							address : $("#dialog-form-update input[name='address']").val(),
	 							phone : $("#dialog-form-update input[name='phone']").val().substring(0,12),
	 							fax : $("#dialog-form-update input[name='fax']").val().substring(0,12),
	 							mobile : $("#dialog-form-update input[name='mobile']").val(),
	 							email : $("#dialog-form-update input[name='email']").val(),
	 							master : $("#dialog-form-update input[name='master']").val(),
	 							invoice_path : $("#dialog-form-update input[name='invoice_path']").val(),
							},				
							success : function(result) {
								var json_obj = $.parseJSON(result);
								var result_table = "";
								$.each(json_obj,function(i, item) {
									result_table += 
										"<tr><td>"+"公司名稱"+"</td><td>"+json_obj[i].group_name +"</td>"
										+"<tr><td>"+"統一編號"+"</td><td>"+ json_obj[i].group_unicode+"</td>"
										+ "<tr><td>"+"公司地址"+"</td><td>"+ json_obj[i].address+"</td>"
										+ "<tr><td>"+"公司電話"+"</td><td>"+ json_obj[i].phone+ "</td>"
										+ "<tr><td>"+"公司傳真"+ "</td><td>"+ json_obj[i].fax+"</td>"
										+ "<tr><td>"+ "負責人"+"</td><td>"+json_obj[i].master+"</td>"
										+ "<tr><td>"+"負責人Email"+"</td><td>"+ json_obj[i].email+ "</td>"
										+ "<tr><td>"+"負責人手機"+"</td><td>"+ json_obj[i].mobile+"</td></tr>"
										+ "<tr><td>"+"電子發票路徑"+"</td><td>"+ json_obj[i].invoice_path+"</td></tr>"
										+ "<tr><td>"+ "功能"+"</td><td>"
										+ "<div class='table-row-func btn-in-table btn-gray'><i class='fa fa-ellipsis-h'></i>"
										+ "	<div class='table-function-list'>"
										+ "		<button class='btn-in-table btn-darkblue btn_update' title='修改' value='"+ json_obj[i].group_id+ "'name='"+ json_obj[i].group_name+"' ><i class='fa fa-pencil'></i></button>"
										+ "	</div></div>"
										+"</td></tr>";
								});
								//判斷查詢結果
								var resultRunTime = 0;
								$.each (json_obj, function (i) {
									resultRunTime+=1;
								});
// 								$("#products").dataTable().fnDestroy();
								if(resultRunTime!=0){
									$("#products-contain").show();
									$("#products tbody").html(result_table);
// 									$("#products").dataTable({"bFilter": false, "bInfo": false, "paging": false, "bSort": false, "language": {"url": "js/dataTables_zh-tw.txt","zeroRecords": "沒有符合的結果"}});
									$(".validateTips").text("");
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
		//修改事件聆聽
		$("#products").delegate(".btn_update", "click", function(e) {
			e.preventDefault();
			uuid = $(this).val();
			$("input[name='search_group_name'").val("");
			$.ajax({
				type : "POST",
				url : "group.do",
				data : {
					action : "searh",
					group_name : $("input[name='search_group_name'").val()
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
								$("#dialog-form-update input[name='group_name']").val(json_obj[i].group_name);
								$("#dialog-form-update input[name='group_unicode']").val(json_obj[i].group_unicode);
								$("#dialog-form-update input[name='address']").val(json_obj[i].address);
								$("#dialog-form-update input[name='phone']").val(json_obj[i].phone);
								$("#dialog-form-update input[name='fax']").val(json_obj[i].fax);
								$("#dialog-form-update input[name='mobile']").val(json_obj[i].mobile);
								$("#dialog-form-update input[name='email']").val(json_obj[i].email);
								$("#dialog-form-update input[name='master']").val(json_obj[i].master);
								$("#dialog-form-update input[name='invoice_path']").val(json_obj[i].invoice_path);
							});
						} 

				});			
			update_dialog.dialog("open");
		});	
		$("#file").change(function(){
			$("#dialog-form-update input[name='invoice_path']").val($("#file").val());
		});
		//melvin begin
		function checkunicode(uniString){       
			var res = uniString.split("");
			var sum = 0 ;
			var para=[1,2,1,2,1,2,4,1] ;
			
			for (i = 0; i <= 7; i++) {
				var inttemp = parseInt(res[i]) * para[i];   
				
				if(inttemp >= 9){   
				  var s = inttemp + "";
				  
				  n1 = s.substring(0,1) * 1;
				  n2 = s.substring(1,2) * 1;
				  inttemp = n1 + n2;           
				}
				sum += inttemp; 
			}
		 
			if( sum % 10 == 0 ){ 
				return true;
			} else {
				if(res[6] == 7){
					sum += 1 ;
					if( sum % 10 == 0 ){
						return true;
					} else {
						return false;
					}
				} else {
					return false;
				} 
			}
		}
		//melvin end
	});	
</script>
			<!--對話窗樣式-修改 -->
				<div id="dialog-form-update" title="修改公司資料" style="display:none;">
				<form name="update-dialog-form-post" id="update-dialog-form-post">
					<fieldset>
							<table class="form-table">
							<tbody>
							<tr>
								<td>公司名稱：</td><td ><input type="text"  style="background-color:lightgray;" name="group_name"disabled="disabled"  /></td>
								<td>公司統一編號：</td><td><input type="text"  style="background-color:lightgray;" name="group_unicode"disabled="disabled"/></td>
							</tr><tr>
								<td>公司地址：</td><td><input type="text" name="address" placeholder="修改公司地址"/></td>
								<td>公司電話：</td><td><input type="text" name="phone"placeholder="修改公司電話"/></td>
							</tr><tr>
								<td>公司傳真：</td><td><input type="text" name="fax"placeholder="修改公司傳真"/></td>
								<td>負責人：</td><td><input type="text" name="master"placeholder="修改公負責人"/></td>
							</tr><tr>
								<td>負責人Email：</td><td><input type="text" name="email"placeholder="修改負責人Email"/></td>
								<td>負責人連絡手機：</td><td><input type="text" name="mobile"placeholder="修改負責人連絡手機"/></td>
							</tr><tr>
								<td>電子發票路徑：</td><td>
<!-- 								<input type="file" id="file" name="file" style="opacity:0;position:absolute;margin:6px;width:140px;"/> -->
<!-- 								<div style="opacity:0.9;position:absolute;"><button id="choose_path" onclick="return false;">選擇路徑</button></div> -->
								<input type="text" name="invoice_path"placeholder="修改電子發票路徑"/>
								</td>
							</tr>
							</tbody>
							</table>	
					</fieldset>
				</form>
			</div>			
			<!-- 第二列 -->
			<div class="row search-result-wrap" align="center" style="width:600px;margin:30px auto;">
				<div id="products-contain" class="ui-widget" style="display:none">
					<table id="products" class="result-table" style="height:460px">
						<thead>
							<tr>
								<th>項目 </th>
								<th>公司資料</th>
							</tr>
						</thead>
						<tbody id="tbdy">
						</tbody>
					</table>
				</div>
				<span class="validateTips"> </span>
			</div>
		</div>
	</div>
</body>
</html>