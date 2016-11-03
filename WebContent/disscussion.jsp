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
<title>留言版</title>
<meta charset="utf-8">
<link rel="Shortcut Icon" type="image/x-icon" href="./images/Rockettheme-Ecommerce-Shop.ico" />
<link href="<c:url value="css/jquery.dataTables.min.css" />" rel="stylesheet">
<link href="<c:url value="css/1.11.4/jquery-ui.css" />" rel="stylesheet">
<style>	
table.discuss {
	font-family: "微軟正黑體", "Microsoft JhengHei", 'LiHei Pro', Arial, Helvetica, sans-serif, \5FAE\8EDF\6B63\9ED1\9AD4,\65B0\7D30\660E\9AD4;
    background: white;
    border: 3px solid #666;
    font-size: 13px;
    width: 95%;
}
table.discuss td {
    margin: 0;
	padding: 8px;
	border: 1px solid #ccc;
	font-size: 10pt;
/* 	vertical-align:text-top; */
}

</style>
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
var user_menu=[];
var subject_id="";//"110a2eed-91f1-11e6-922d-005056af760c";
function draw_disscussion(parameter){
	$.ajax({
		type : "POST",
		url : "disscussion.do",
		data : parameter,
		success : function(result) {
				var json_obj = $.parseJSON(result);
				var result_table = "";
				$.each(json_obj,function(i, item) {
					$("#topic").html(json_obj[i].subject_Name);
					$("#topic").val(json_obj[i].subject_id);
					result_table +="<tr id='floor"+(i+1)+"' style='border-top: 2px solid #777;'><td style='text-align:center;'>"
					+ (i+1)
					+ "樓</td><td>"
		// 			+ 
					+ json_obj[i].create_time.replace(".0","")
					+ "</td></tr><tr style='height:120px;'><td style='text-align:center;'>"
					+ "<div><img src='"+(json_obj[i].user_id.charCodeAt(0)%4==0?"images/anonymous.png":"images/anonymous_g.png")+"' style='max-height:100px;'></div>"+(user_menu[json_obj[i].user_id]==null?"匿名使用者":user_menu[json_obj[i].user_id])
					+ "</td><td  valign='top' style='font-size:18px'>"
					+ json_obj[i].content
					+ "</td></tr>";
				});
				//判斷查詢結果
				var resultRunTime = 0;
				$.each (json_obj, function (i) {
					resultRunTime+=1;
				});
// 				$("#products2").dataTable().fnDestroy();
				if(resultRunTime!=0){
					$("#products2 tbody").html(result_table);
					$("#products2_contain_row").show();
					$("#products2_contain_row").css({"opacity":"0"});
					
// 					$("#products2").dataTable({
// 						"language": {"url": "js/dataTables_zh-tw.txt","zeroRecords": "沒有符合的結果"}});
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
		$("body").append('<a href="sip:<benchen@pershing.com.tw>" class="btn-explanation" style="position: fixed; top: 85%; right: 60px; background-color: white; border-radius: 200px; display: block;"><img src="./images/skype-icon.png"/></a>');
		var page_info=location.href.split("?disscussion_id=")[1];
// 		alert(page_info);
		if(page_info==null){window.location.href = './disscussionsubject.jsp';}
		subject_id=page_info;
		$("#topic").val(page_info);
		$(".bdyplane").animate({"opacity":"1"});
		$.ajax({
			type : "POST",
			url : "disscussion.do",
			async : false,
			data :{action : "user_list"},
			success : function(result) {
				var json_obj = $.parseJSON(result);
				$.each (json_obj, function (i,item) {
					if(json_obj[i].user_id!=null){
						user_menu[json_obj[i].user_id]=json_obj[i].user_name;
					}
				});
				
			}
		});
		draw_disscussion({action : "search",subject_id : subject_id});
		//使用jquery.validate來做驗證  注意事項: 1.不能用選擇器方式批次設定，它只取最後一個參數 2.要調用resetForm()而不是reset()
		var validator_insert = $("#insert-dialog-form-post").validate({
			rules : {
				name : {required : true,maxlength : 10},
				rate : {required : true,maxlength : 10,number :true }
			}
		});
		var validator_update = $("#update-dialog-form-post").validate({
			rules : {
				name : {required : true,maxlength : 10},
				rate : {required : true,maxlength : 10,number :true }
			}
		});
		//新增Dialog相關設定
		insert_dialog = $("#dialog-form-insert").dialog({
			draggable : false, resizable : false, autoOpen : false,
			height : "auto", width : 600, modal : true,
			show : {effect : "blind",duration : 300},
			hide : {effect : "fade",duration : 300},
			buttons : [{
						id : "insert",
						text : "新增",
						click : function() {
							if ($('#msg').val().length>180) {alert("留言最多180字!");return;}
							var tmp={
								action : "insert",
// 								disscussion_id:$("#topic").val(),
								subject_id:$("#topic").val(),
								subject_Name :$("#topic").html(),
								content : $("#msg").val()
							};
							draw_disscussion(tmp);
							$("#msg").val('');
							$("#dialog-form-insert").dialog("close");
						}
					}, {
						text : "取消",
						click : function() {
							$("#dialog-form-insert").dialog("close");
						}
					} ]
		});
		$("#dialog-form-insert").show();
		//刪除事件聆聽 : 因為聆聽事件動態產生，所以採用delegate來批量處理，節省資源
		$("#products2").delegate(".btn_delete", "click", function() {
			$("#dialog-confirm").val($(this).val());
			$("#dialog-confirm").html("<div class='delete_msg'>'"+$(this).attr("val2")+"'</div>");
			confirm_dialog.dialog("open");
		});
		//修改事件聆聽
		$("#products2").delegate(".btn_update", "click", function() {
			$("#dialog-form-update").val($(this).val());
// 			$("#update-dialog-form-post input[name='name']").val($(this).attr("name"));
// 			$("#update-dialog-form-post input[name='rate']").val($(this).attr("val2"));
			update_dialog.dialog("open");
		});
		//新增事件聆聽
		$("#post").click( function() {
			if ($('#msg').val().length<1) {return;}
			$("#dialog-form-insert").html($("#msg").val());
			$("#dialog-form-insert").dialog("open");
		});
		window.addEventListener('wheel', function(evt){
			if(evt.deltaY<0){$('.btn-explanation').fadeIn();}
		});	
// 		$(".input-field-wrap").append("<div class='div_right_bottom upup'><img src='./images/upup.png'></div>");
// 		$(".input-field-wrap").after("<div class='div_right_top downdown' style='display:none;'><img src='./images/downdown.png'></div>");
// 		$(".upup").click(function(){
// 			$(".input-field-wrap").slideToggle("slow");
// 			$(".downdown").slideToggle();
// 		});
// 		$(".downdown").click(function(){
// 			$(".input-field-wrap").slideToggle("slow");
// 			$(".downdown").slideToggle();
// 		});
	});
</script>
		<div class="datalistWrap">
			<!--對話窗樣式-確認 -->
<!-- 			<div id="dialog-confirm" title="是否刪除此主題?" style="display:none;"> -->
<!-- 			</div> -->
			<!--對話窗樣式-修改 -->
<!-- 			<div id="dialog-form-update" title="修改主題" style="display:none;"> -->
<!-- 				<form name="update-dialog-form-post" id="update-dialog-form-post"> -->
<!-- 					<fieldset> -->
<!-- 						<table class="form-table"> -->
<!-- 							<tr> -->
<!-- 								<td>幣值名稱：</td> -->
<!-- 								<td><input type="text" name="name" placeholder="修改幣值名稱"></td> -->
<!-- 							</tr><tr> -->
<!-- 								<td>匯率：</td> -->
<!-- 								<td><input type="text" name="rate" placeholder="修改匯率"></td> -->
<!-- 							</tr> -->
<!-- 						</table> -->
<!-- 					</fieldset> -->
<!-- 				</form> -->
<!-- 			</div>			 -->
			<!--對話窗樣式-新增 -->
			<div id="dialog-form-insert" title="新增留言?" style="display:none;"></div>
<!-- 				<form name="insert-dialog-form-post" id="insert-dialog-form-post"> -->
<!-- 					<fieldset> -->
<!-- 						<table class="form-table"> -->
<!-- 							<tr> -->
<!-- 								<td>幣值名稱：</td> -->
<!-- 								<td><input type="text" name="name" placeholder="修改幣值名稱"></td> -->
<!-- 							</tr><tr> -->
<!-- 								<td>匯率：</td> -->
<!-- 								<td><input type="text" name="rate" placeholder="修改匯率"></td> -->
<!-- 							</tr> -->
<!-- 						</table> -->
<!-- 					</fieldset> -->
<!-- 				</form> -->
<!-- 			</div> -->
			<!-- 第一列 -->
<!-- 		<div class="input-field-wrap"> -->
<!-- 			<div class="form-wrap"> -->
<!--  			<a href='#floor3'>333</a>  -->
<!-- 				<div class="btn-row"> -->
<!-- 					<button class="btn btn-exec btn-wide" id="create-exchange">新增主題</button> -->
<!-- 				</div> -->
<!-- 			</div>/.form-wrap -->
<!-- 		</div> -->
			<div class="row search-result-wrap" align="center" id="products2_contain_row" style="display:none;">
			<a href='disscussionsubject.jsp' style='float:right'>返回主題列表</a>
			<h3 id='topic'>主題</h3>
				<div id="products2-contain" class="ui-widget">
					<table id="products2" class="discuss">
					<colgroup> 
					  <col width="150px">
					  <col>
					</colgroup>
						<tbody>
						</tbody>
					</table>
				</div>
				
			</div>
			<div style='width:90%;margin:0 auto;'>
				<hr class='hr-gray'>
				<div style="border:2px solid #ccc;height:200px;padding:8px;text-align:left;">
					以<%=request.getSession().getAttribute("user_name") %>身分發表回覆：<br>
					<textarea type="text" id='msg' name='msg' style="background:#FFF;border:1px solid #55c;margin:10px;height:80%;width:97%;"></textarea>
					<button class="btn btn-darkblue" id="post" style="margin:15px ;">發表</button>
				</div>
			</div>
			<div class='btn-explanation' style='position:fixed;top:75%;right:60px;background-color:blue;border-radius: 200px;'><a href='#msg' onclick="$('.btn-explanation').fadeOut();"><img src='images/post.png'style="max-height:40px;"></a></div>
		</div>
	</div>
	</div>
	
</body>
</html>