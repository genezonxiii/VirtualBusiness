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
table.result {
	font-family: "微軟正黑體", "Microsoft JhengHei", 'LiHei Pro', Arial, Helvetica, sans-serif, \5FAE\8EDF\6B63\9ED1\9AD4,\65B0\7D30\660E\9AD4;
    border: 1px solid #ccc;
    font-size: 13px;
    width: 80%;
}
table.result tr { background: #eee;}
table.result tr:nth-child(2n+1) { background: #fff;}
table.result tbody tr:hover {background: #d8d8d8;}
table.result tbody tr:hover:nth-child(2n+1) {background: #d8d8d8;}
table.result th {
    background: #194A6B;
    border: 1px solid #62E0E9;
    color: #fff;
    word-break: keep-all;
}
table.result th, table.result td {
	border: 1px solid #777;
    padding: 10px;
    align: center;
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
<script type="text/javascript" src="js/jquery-migrate-1.4.1.min.js"></script>
<script type="text/javascript" src="js/jquery.validate.min.js"></script>
<script type="text/javascript" src="js/additional-methods.min.js"></script>
<script type="text/javascript" src="js/messages_zh_TW.min.js"></script>
<script>
function draw_group(parameter){
	$.ajax({
		type : "POST",
		url : "group_tmp.do",
		data : parameter,
		success : function(result) {
			var json_obj = $.parseJSON(result);
			var result_table = "";
			$.each(json_obj,function(i, item) {
				result_table  
				+="<tr><td><div align='center'>"
				+"   <button value='"+json_obj[i].group_id+"' class='btn_update'>修改</button>"
				+"   <button value='"+json_obj[i].group_id+"' class='btn_delete'>刪除</button>"
				+"</div></td><td name='name'>"+json_obj[i].group_name+"</td>"
				+ "</tr>";
			});
			//判斷查詢結果
			var resultRunTime = 0;
			$.each (json_obj, function (i) {
				resultRunTime+=1;
			});
			if(resultRunTime!=0){
				$("#products-contain").show();
				$("#products tbody").html(result_table);
				$(".validate").html("");
			}else{
				$("#products-contain").hide();
				$(".validate").html("---查無此結果---");
			}
		}
	});
}
	$(function() {
		$(".bdyplane").animate({"opacity":"1"});
		draw_group({action : "search"});
		//修改Dialog相關設定
		$("#dialog-form-update").dialog({
			draggable : false, resizable : false, autoOpen : false,
			height : "auto", width : "auto", modal : true,
			show : {effect : "blind",duration : 300},
			hide : {effect : "fade",duration : 300},
			buttons : [{
				text : "修改",
				click : function() {
					draw_group({
						action : "update",
						group_id : $(this).val(),
						group_name : $("#dialog-form-update input[name='name']").val()
					});
					$("#dialog-form-update").dialog("close");
				}
			},{
				text : "取消",
				click : function() {
					$("#dialog-form-update").dialog("close");
				}
			}],
			close : function() {
				$("#dialog-form-update").dialog("close");
			}
		});
		$("#dialog-form-delete").dialog({
			draggable : false, resizable : false, autoOpen : false,
			height : "auto", width : "auto", modal : true,
			show : {effect : "blind",duration : 300},
			hide : {effect : "fade",duration : 300},
			buttons : [{
				text : "刪除",
				click : function() {
					draw_group({
						action : "delete",
						group_id : $(this).val(),
					});
					$("#dialog-form-delete").dialog("close");
				}
			},{
				text : "取消",
				click : function() {
					$("#dialog-form-delete").dialog("close");
				}
			}]
		});
		$("#dialog-form-insert").dialog({
			draggable : false, resizable : false, autoOpen : false,
			height : "auto", width : "auto", modal : true,
			show : {effect : "blind",duration : 300},
			hide : {effect : "fade",duration : 300},
			buttons : [{
				text : "新增",
				click : function() {
					draw_group({
						action : "insert",
						group_name : $("#dialog-form-insert input[name='name']").val(),
					});
					$("#dialog-form-insert").dialog("close");
				}
			},{
				text : "取消",
				click : function() {
					$("#dialog-form-insert").dialog("close");
				}
			}]
		});
		//修改事件聆聽
		$("#products").delegate(".btn_update", "click", function(e) {
			e.preventDefault();
			$("#dialog-form-update").val($(this).val());
			$("#dialog-form-update input[name='name']").val($(this).parents("tr").find("td[name='name']").html());
			$("#dialog-form-update").dialog("open");
		});
		$("#products").delegate(".btn_delete", "click", function(e) {
			e.preventDefault();
			$("#dialog-form-delete").val($(this).val());
			$("#dialog-form-delete").html("<div style='padding:5px 40px;' color=red>"+$(this).parents("tr").find("td[name='name']").html()+"</div>");
			$("#dialog-form-delete").dialog("open");
		});
		$("#products").delegate(".btn_insert", "click",  function(e) {
			e.preventDefault();
			$("#dialog-form-insert input[name='name']").val('');
			$("#dialog-form-insert").dialog("open");
		});
	});	
</script>
			<div id="dialog-form-update" title="修改公司資料" style="padding:20px;display:none;">
				公司名稱：<input type="text" name="name"  />
			</div>
			<div id="dialog-form-delete" title="是否刪除此公司" style="padding:20px;display:none;"></div>
			<div id="dialog-form-insert" title="新增公司資料" style="padding:20px;display:none;">
				公司名稱：<input type="text" name="name"  />
			</div>
			<!-- 第二列 -->
			
			<div align="center" style="width:600px;margin:30px auto;">
				<div id="products-contain" style="display:none">
					<table id="products" class="result">
					<caption><button class='btn_insert'>新增</button><br>　</caption>
						<thead>
							<tr>
								<th>功能 </th>
								<th>公司名稱</th>
							</tr>
						</thead>
						<tbody></tbody>
					</table>
				</div>
				<span class="validateTips" style='color:red;font:22px;'> </span>
			</div>
		</div>
	</div>
</body>
</html>