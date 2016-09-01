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
<title>密碼修改</title>
<meta charset="utf-8">
<link rel="Shortcut Icon" type="image/x-icon" href="./images/Rockettheme-Ecommerce-Shop.ico" />
<link rel="stylesheet" href="css/styles.css" />
<link href="<c:url value="css/css.css" />" rel="stylesheet">
<link href="<c:url value="css/jquery.dataTables.min.css" />" rel="stylesheet">
<link rel="stylesheet" href="css/1.11.4/jquery-ui.css">
</head>
<body>
	<jsp:include page="template.jsp" flush="true"/>
	<div class="content-wrap" >
		<div id='bdy' style="opacity:0">
<script type="text/javascript" src="js/jquery-1.10.2.js"></script>
<script type="text/javascript" src="js/jquery.dataTables.min.js"></script>
<script type="text/javascript" src="js/jquery-ui.min.js"></script>
<script type="text/javascript" src="js/jquery-ui.js"></script>
<script type="text/javascript" src="js/jquery-migrate-1.4.1.min.js"></script>
<script type="text/javascript" src="js/jquery.validate.min.js"></script>
<script type="text/javascript" src="js/additional-methods.min.js"></script>
<script type="text/javascript" src="js/messages_zh_TW.min.js"></script>
<script>
	$(function() {
		$("#bdy").animate({"opacity":"1"});
		var validator_update = $("#password-form-post").validate({
			rules : {
				password : {
					required : true,
					maxlength : 10
				},
		password2 : {
			required : true,
			maxlength : 10
		}
			},
			messages : {
				unit_name : {
					maxlength : "長度不能超過10個字"
				}
			}
			
		});
		$("input[name='password']").bind('focus', function(){ 
			$("#errormesg").text(""); 
			$("#ok").text(""); 
		});
		
		//密碼修改
		$("#password_btn").click(function(e) {
			var pass1 = $("input[name='password']").val();
			var pass2 = $("input[name='password2']").val();
			if ( pass1 === pass2) {
			} else {
				$("#errormesg").text("密碼不相同請重新輸入"); 
				$("input").attr("value","");
				return false;
			}
							e.preventDefault();
							$.ajax({
									type : "POST",
									url : "changepassword.do",
									data : {
										action : "update",
										password : $("input[name='password'").val(),
									},
									success : function(result) {
										if ($('#password-form-post').valid()) {
											var json_obj = $.parseJSON(result);
											//判斷查詢結果
											var resultRunTime = 0;
											$.each (json_obj, function (i) {
												resultRunTime+=1;
											});
											if(resultRunTime!=0){
												$("input").attr("value","");
												$("#ok").text("密碼修改完成"); 										
											}
										}
									}		
								});
							});
						});
</script>
		<div class="datalistWrap">
		
			<!-- 第一列 -->
		<div class="input-field-wrap">
			<div class="form-wrap">
			<form name="password-form-post" id="password-form-post" class="result-table">
				<div class="form-row">
					<label for="">
						<span class="block-label">新密碼</span>
						<input type="password" name="password">
					</label>
				</div>
				<div class="form-row">
					<label for="">
						<span class="block-label">新密碼確認</span>
						<input type="password" name="password2">
					</label>
				</div>
				<div class="btn-row">
					<button id="password_btn" class="btn btn-exec btn-wide">更改密碼</button>
				</div>
				<div id="errormesg"></div>
			</form>
			</div><!-- /.form-wrap -->
		</div><!-- /.input-field-wrap -->
			
			
			
<!-- 			<div class="row search-result-wrap" align="center"> -->
<!-- 				<div id="products-serah-create-contain" class="ui-widget result-table-wrap"> -->
<!-- 				<form name="password-form-post" id="password-form-post" class="result-table"> -->
<!-- 						<table  id="password"> -->
<!-- 							<tbody> -->
<!-- 								<tr> -->
<!-- 									<td><h2>新密碼:</h2></td> -->
<!-- 									<td><input type="password" name="password"  placeholder="輸入新密碼"/></td> -->
									
<!-- 								</tr> -->
<!-- 								<tr> -->
<!-- 									<td><h2>新密碼確認:</h2></td> -->
<!-- 									<td><input type="password" name="password2"  placeholder="再輸入新密碼"/></td> -->
<!-- 								</tr> -->
<!-- 								<tr> -->
<!-- 									<td colspan="2"> -->
<!-- 										<button id="password_btn" class="btn btn-darkblue">更改密碼</button> -->
<!-- 									</td> -->
<!-- 								</tr> -->
<!-- 								<tr> -->
<!-- 									<td colspan="2"><div id="errormesg"></div></td> -->
								
<!-- 								</tr> -->
<!-- 							</tbody> -->
<!-- 						</table> -->
<!-- 						</form> -->
<!-- 				</div> -->
<!-- 			</div> -->
		</div>
	</div>
	</div>
</body>
</html>