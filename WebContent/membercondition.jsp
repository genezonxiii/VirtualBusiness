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
<title>會員分級</title>
<meta charset="utf-8">
<link rel="Shortcut Icon" type="image/x-icon" href="./images/Rockettheme-Ecommerce-Shop.ico" />
<link rel="stylesheet" href="css/styles.css" />
<link rel="stylesheet" href="css/jquery.dataTables.min.css" />
<link rel="stylesheet" href="css/1.11.4/jquery-ui.css">

</head>
<body>
	<jsp:include page="template.jsp" flush="true"/>
	<div class="content-wrap" >
		<div class='bdyplane' style="opacity:0">
		
<script type="text/javascript" src="js/jquery-1.10.2.js"></script>
<script type="text/javascript" src="js/jquery.dataTables.min.js"></script>
<script type="text/javascript" src="js/jquery-ui.min.js"></script>
<script type="text/javascript" src="js/jquery-ui.js"></script>
<script type="text/javascript" src="js/jquery.validate.min.js"></script>
<script type="text/javascript" src="js/messages_zh_TW.min.js"></script>

<script>
function draw_membercondition(parameter){
	$.ajax({
		type : "POST",
		url : "membercondition.do",
		data : parameter,
		success : function(result) {
			var normal=0;
			var json_obj = $.parseJSON(result);
			var result_table = "";
			$.each(json_obj,function(i, item) {
				if(json_obj[i].classname=='普通會員'){normal=1;}
				result_table += "<tr name='"+json_obj[i].condition_id+"'><td>"+ json_obj[i].classname 
				+ "</td><td>&nbsp;"+ json_obj[i].total_period + " 天"
				+ "</td><td>" + json_obj[i].total_consumption
				+ "</td><td>&nbsp;"+ json_obj[i].continue_period + " 天"
				+ "</td><td>"+ json_obj[i].continue_consumption
				+ "</td><td>"+ " "
				+ "</td><td>"+ "<div class='table-row-func btn-in-table btn-gray'>"
				+ "  <i class='fa fa-ellipsis-h'></i><div class='table-function-list'>"
				+ "    <button class='btn-in-table btn-darkblue btn_update' title='修改' value='"+json_obj[i].condition_id+"'><i class='fa fa-pencil'></i></button>"
				+ (json_obj[i].classname=='普通會員'?"":"    <button class='btn-in-table btn-alert btn_delete' title='刪除' value='"+json_obj[i].condition_id+"' val2='"+json_obj[i].classname+"'><i class='fa fa-trash'></i></button>")
				+ "</div></div></td></tr>";
			});
			if(normal==0){
				$.ajax({
					type : "POST",
					url : "membercondition.do",
					data : {
						action : "insert",
						classname : "普通會員",
						total_period : 0,
						total_consumption : 0,
						expire_day: 30 ,
						continue_period : 0,
						continue_consumption : 0
					},success : function(result) {}
				});
			}
			if(json_obj.length!=0){
				$("#membercondition-table").dataTable().fnDestroy();
				$("#membercondition-table tbody").html(result_table);
				
				$("#membercondition-table").show();
				$("#membercondition-table").dataTable({
			         "language": {"url": "js/dataTables_zh-tw.txt"},
			         "order": [2]
				});
				tooltip("btn_update");
				tooltip("btn_delete");
				$("#membercondition-table tr td").each(function(index){
					$( this ).html(money($( this ).html())) ;
				});
				$("#err_msg").html("");
			}else{
				$("#err_msg").html("<div style='color:red;'>查無資料</div>");
			}
		}
	});
}


$(function() {
	$(".bdyplane").animate({"opacity":"1"});
	draw_membercondition({action:"select"});
	$("#membercondition-insert-form").validate({
		rules : {
			classname : {required : true},
			total_period:{required : true},
			total_consumption : {required : true, number :true, min: 1},
			continue_period:{required : true},
			continue_consumption : {required : true, number :true, min: 1},
			memo : {
				stringMaxLength : 200
			}
		}
	});
	$("#new").click(function(){
		$("#membercondition-insert").dialog('open');
	});
	$("#membercondition-table").delegate(".btn_delete", "click", function() {
		$("#membercondition-delete").val($(this).val());
		$("#membercondition-delete").html("<div style='width:180px' align='center'>' <font color='red'><b>"+$(this).attr("val2")+"</b></font> '</div>");
		$("#membercondition-delete").dialog('open');
	});
	$("#membercondition-table").delegate(".btn_update", "click", function() {
		$("#membercondition-update").val($(this).val());
		//五個數字的帶入 
		$.ajax({
			type : "POST",
			url : "membercondition.do",
			data : { action : "select"},
			success : function(result) {
				var json_obj = $.parseJSON(result);
				$.each(json_obj,function(i, item) {
					if(json_obj[i].condition_id==$("#membercondition-update").val()){
						if(json_obj[i].classname=="普通會員"){
							//$("input").attr("disabled","disabled");
							$("#membercondition-update input[name='classname']").attr("disabled","disabled");
						}else{
							$("#membercondition-update input[name='classname']").attr("disabled",false);
						}
						$("#membercondition-update input[name='classname']").val(json_obj[i].classname);
						if(json_obj[i].total_period%365==0){
							$("#membercondition-update input[name='total_period']").val(json_obj[i].total_period/365);
							$("#membercondition-update select[name='total_unit']").val('365');
						}else if(json_obj[i].total_period%90==0){
							$("#membercondition-update input[name='total_period']").val(json_obj[i].total_period/90);
							$("#membercondition-update select[name='total_unit']").val('90');
						}else if(json_obj[i].total_period%30==0){
							$("#membercondition-update input[name='total_period']").val(json_obj[i].total_period/30);
							$("#membercondition-update select[name='total_unit']").val('30');
						}else {
							$("#membercondition-update input[name='total_period']").val(json_obj[i].total_period);
							$("#membercondition-update select[name='total_unit']").val('1');
						}
						$("#membercondition-update input[name='total_consumption']").val(json_obj[i].total_consumption);
						if(json_obj[i].total_period%365==0){
							$("#membercondition-update input[name='continue_period']").val(json_obj[i].continue_period/365);
							$("#membercondition-update select[name='continue_unit']").val('365');
						}else if(json_obj[i].total_period%90==0){
							$("#membercondition-update input[name='continue_period']").val(json_obj[i].continue_period/90);
							$("#membercondition-update select[name='continue_unit']").val('90');
						}else if(json_obj[i].total_period%30==0){
							$("#membercondition-update input[name='continue_period']").val(json_obj[i].continue_period/30);
							$("#membercondition-update select[name='continue_unit']").val('30');
						}else {
							$("#membercondition-update input[name='continue_period']").val(json_obj[i].continue_period);
							$("#membercondition-update select[name='continue_unit']").val('1');
						}
						
						
						$("#membercondition-update input[name='continue_consumption']").val(json_obj[i].continue_consumption);
					}
				});
			}
		});
		$("#membercondition-update").dialog('open');
	});
	$("#membercondition-insert").dialog({
			draggable : true, resizable : false, autoOpen : false,
			width : 760 ,height : "auto", modal : true,
			show : {effect : "blind", duration : 300 },
			hide : { effect : "fade", duration : 300 },
			buttons : [{
						id : "insert",
						text : "新增",
						click : function() {
							if ($('#membercondition-insert-form').valid()) {
								draw_membercondition({
									action : "insert",
									classname : $("#membercondition-insert input[name='classname']").val(),
									total_period : $("#membercondition-insert input[name='total_period']").val()*$("#membercondition-insert select[name='total_unit']").val(),
									total_consumption : $("#membercondition-insert input[name='total_consumption']").val(),
									expire_day: 30 ,
									continue_period : $("#membercondition-insert input[name='continue_period']").val()*$("#membercondition-insert select[name='continue_unit']").val(),
									continue_consumption : $("#membercondition-insert input[name='continue_consumption']").val(),
								});
								$("#membercondition-insert").dialog("close");
								$("#membercondition-insert-form").trigger("reset");
							}
						}
					}, {
						text : "取消",
						click : function() {
							$("#membercondition-insert-form").trigger("reset");
							$("#membercondition-insert").dialog("close");
						}
					} ],
			close : function() {
				$("#membercondition-insert-form").trigger("reset");
			}
	});
	$("#membercondition-insert").show();
	$("#membercondition-update").dialog({
			draggable : true, resizable : false, autoOpen : false,
			width : 760,height : "auto", modal : true,
			show : {effect : "blind", duration : 300 },
			hide : { effect : "fade", duration : 300 },
			buttons : [{
						id : "update",
						text : "修改",
						click : function() {
							if ($('#membercondition-update-form').valid()) {
								draw_membercondition({
									action : "update",
									condition_id : $("#membercondition-update").val(),
									classname : $("#membercondition-update input[name='classname']").val(),
									total_period : $("#membercondition-update input[name='total_period']").val()*$("#membercondition-update select[name='total_unit']").val(),
									total_consumption : $("#membercondition-update input[name='total_consumption']").val(),
									expire_day: 30 ,
									continue_period : $("#membercondition-update input[name='continue_period']").val()*$("#membercondition-update select[name='continue_unit']").val(),
									continue_consumption : $("#membercondition-update input[name='continue_consumption']").val(),
								});
								$("#membercondition-update").dialog("close");
								$("#membercondition-update-form").trigger("reset");
							}
						}
					}, {
						text : "取消",
						click : function() {
							$("#membercondition-update-form").trigger("reset");
							$("#membercondition-update").dialog("close");
						}
					} ],
			close : function() {
				$("#membercondition-update-form").trigger("reset");
			}
	});
	$("#membercondition-update").show();
	$("#membercondition-delete").dialog({
			draggable : true, resizable : false, autoOpen : false,
			width : "auto" ,height : "auto", modal : true,
			show : {effect : "blind", duration : 300 },
			hide : { effect : "fade", duration : 300 },
			buttons : [{
						id : "delete",
						text : "刪除",
						click : function() {
							draw_membercondition({
								action : "delete",
								condition_id : $("#membercondition-delete").val()
							});
							$("#membercondition-delete").dialog("close");
						}
					}, {
						text : "取消",
						click : function() {
							$("#membercondition-delete").dialog("close");
						}
					} ],
			close : function() {
			}
	});
	$("#membercondition-delete").show();
	$("#explane").dialog({
		draggable : true, resizable : false, autoOpen : false,
		width : 720 ,height : "auto", modal : false,
		show : {effect : "blind", duration : 300 },
		hide : { effect : "fade", duration : 300 },
		buttons : {
			"確定" : function() {$(this).dialog("close");}
		}
	});
	$("#explane").show();
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
		<div class="input-field-wrap">
			<button class='btn-explanation' onclick="$('#explane').dialog('open');" style="">?</button>
			<!-- 第一列 -->
			<div class="form-wrap">
				<div class="btn-row">
					<button id="new" class="btn btn-exec btn-wide">新增</button>
				</div>
				<div id="errormesg"></div>
			</div><!-- /.form-wrap -->
		</div><!-- /.input-field-wrap -->
		<div class="search-result-wrap">
			<div class="result-table-wrap">
				<table id="membercondition-table" class="result-table" style="display:none">
					<thead>
						<tr>
							<th style="background-image: none !important;">級別</th>
							<th>入會累計期間</th>
							<th>入會累計金額</th>
							<th>續會累計期間</th>
							<th>續會累計金額</th>
							<th style="background-image: none !important;">備註</th>
							<th style="background-image: none !important;">功能</th>
						</tr>
					</thead>
					<tbody style="text-align:right;">
					</tbody>
				</table>
				<div class="error-msg" id="err_msg" align="center"> </div>
			</div>
		</div>
		</div>
	</div>
<div id="membercondition-insert" title="新增會員分級" style="display:none">
	<form id="membercondition-insert-form">
		<table class="form-table">
			<tr>
				<td>級別名稱：</td>
				<td><input type="text" name="classname"  placeholder="普通會員"></td>
			</tr>
			<tr>
				<td>入會累計期間：</td>
				<td ><input type="text" name="total_period" style="width:84px" placeholder="數字">
					<select name="total_unit">
						<option value="1">日</option>
						<option value="30">月</option>
						<option value="90">季</option>
						<option value="365">年</option>
					</select>
				</td>
				<td>入會累計金額：</td>
				<td><input type="text" name="total_consumption"  placeholder="$"></td>
			</tr>
			<tr>
				<td>續會累計期間：</td>
				<td>
					<input type="text" name="continue_period" style="width:84px" placeholder="數字">
					<select name="continue_unit">
						<option value="1">日</option>
						<option value="30">月</option>
						<option value="90">季</option>
						<option value="365">年</option>
					</select> 
				</td>
				<td>續會累計金額：</td>
				<td><input type="text" name="continue_consumption" placeholder="$"></td>
			</tr>
		</table>
	</form>
</div>
<div id="membercondition-update" title="修改會員分級" style="display:none">
	<form id="membercondition-update-form">
		<table class="form-table">
			<tr>
				<td>級別名稱：</td>
				<td><input type="text" name="classname"  placeholder="普通會員"></td>
			</tr>
			<tr>
				<td>入會累計期間：</td>
				<td><input type="text" name="total_period" style="width:84px" placeholder="數字">
					<select name="total_unit">
						<option value="1">日</option>
						<option value="30">月</option>
						<option value="90">季</option>
						<option value="365">年</option>
					</select>
				</td>
				<td>入會累計金額：</td>
				<td><input type="text" name="total_consumption" placeholder="$"></td>
			</tr>
			<tr>
				<td>續會累計期間：</td>
				<td>
					<input type="text" name="continue_period" style="width:84px" placeholder="數字">
					<select name="continue_unit">
						<option value="1">日</option>
						<option value="30">月</option>
						<option value="90">季</option>
						<option value="365">年</option>
					</select> 
				</td>
				<td>續會累計金額：</td>
				<td><input type="text" name="continue_consumption" placeholder="$"></td>
			</tr>
		</table>
	</form>
</div>
<div id="explane" title="分級辦法詳述 (重要)" style="display:none;">
	<div style="padding:0 40px; font-family: Helvetica, Arial, '微軟正黑體', '新細明體', sans-serif;">
		<br><li><font color=red size=4>每月核算日</font>(註1)0點，我們將根據會員在截至上月月底之累計期間內的<font color=red size=4>有效交易次數</font>(註2)與<font color=red size=4>實際消費金額</font>(註3)與<font color=red size=4>上月會員等級身分別</font>(註4)，按進行會員<font color=red size=4>等級調整</font>(註5)並取會員層級較高階者，以企求符合管理會員等級、並可據此對不同會員做出不同管理辦法之需求。</li>
		<font size=2>
		<br><br>*註1：每月核算日為　每月8號。
		<br><br>*註2：有效交易次數係指　統計之累計期間內，上月結算日前至少完成一件以上商品取件之銷貨訂單核算。<!-- 就是出貨日在上月30,31日之前直到現在還沒被退貨的東西算有效。 -->
		<br><br>*註3：實際消費金額係指　統計之累計期間內，上月結算日前實際完程應收流程有效交易次數中的價款總額。(不含運費、刷卡手續費、購物折價金優惠券)
		<br><br>*註4：上月會員等級身分別係指　若上次核算日後，該會員被判斷之會員層級，則此次計算核用該會員等級之'續會'與其餘各項'入會'之統計標準(含累計期間及累計金額)
		<br><br>*註5：調整方式係為　若上述條件符合者將滿足續會標準，不符合者將降為普通會員
		</font><br><br>
	</div>
</div>
<div id="membercondition-delete" title="是否刪除此級別?" style="display:none;">
</div>
</body>
</html>