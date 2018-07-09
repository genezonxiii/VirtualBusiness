<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<title>密碼修改</title>
<meta charset="utf-8">
<link rel="Shortcut Icon" type="image/x-icon" href="./images/Rockettheme-Ecommerce-Shop.ico" />
<link rel="stylesheet" href="css/1.12.0/jquery-ui.css">
</head>
<body>

<script type="text/javascript" src="js/jquery-1.12.4.js"></script>
<script type="text/javascript" src="js/jquery-ui.min.js"></script>
<script type="text/javascript" src="js/jquery.validate.min.js"></script>
<script>

	$(function() {
		$(".bdyplane").animate({
			"opacity" : "1"
		});
		
		$("input[name='password']").bind('focus', function() {
			warning_msg("");
			$("#ok").text("");
		});
		
		$("#dialog-confirm").dialog({
			draggable : true,
			resizable : false,
			autoOpen : false,
			height : "auto",
			width : "auto",
			modal : true,
			show : {
				effect : "blind",
				duration : 300
			},
			hide : {
				effect : "fade",
				duration : 300
			},
			buttons : {
				"確認" : function() {
					$.ajax({
						type : "POST",
						url : "changepassword.do",
						data : {
							action : "update",
							password_old : $("input[name='password_old']").val(),
							password_new : $("input[name='password_new']").val(),
						},
						success : function(result) {
							warning_msg("");
							var json_obj = $.parseJSON(result);
							//判斷查詢結果
							var resultRunTime = 0;
							$.each(json_obj, function(i) {
								resultRunTime += 1;
							});
							if (resultRunTime != 0) {
								$("input").attr("value", "");
								$("#ok").text("密碼修改完成");
								$("#pwd_old,#pwd_new,#pwd_new_confirm").val('');
							}
						}
					});
					$(this).dialog("close");
				},
				"取消" : function() {
					$(this).dialog("close");
				}
			}
		});
		
		$("#dialog-confirm").show();
		
		//密碼修改
		$("#password_btn").click(function(e) {
					e.preventDefault();

					if (! /^(?=.*\d)(?=.*[a-z]).{6,20}$/.test($("#pwd_new").val()) ) {
						warning_msg("新密碼與規則不符，請重新輸入，謝謝！");
						return;
					}
					
					if ($("#pwd_new").val() != $("#pwd_new_confirm").val()) {
						$("#pwd_new_confirm").addClass("error");
						warning_msg("請輸入相同密碼");
						return;
					}
					
					$("#dialog-confirm")
						.html("<div class='delete_msg'>確認是否修改密碼</div>")
						.dialog("open");
				});
		
		$(".input-field-wrap")
			.append("<div class='div_right_bottom upup'><img src='./images/upup.png'></div>");
		
		$(".input-field-wrap")
			.after("<div class='div_right_top downdown' style='display:none;'><img src='./images/downdown.png'></div>");
		
		$(".upup").click(function() {
			$(".input-field-wrap").slideToggle("slow");
			$(".downdown").slideToggle();
		});
		
		$(".downdown").click(function() {
			$(".input-field-wrap").slideToggle("slow");
			$(".downdown").slideToggle();
		});
	});
</script>

	<jsp:include page="template.jsp" flush="true" />
	
	<div class="content-wrap">
		<div class='bdyplane' style="opacity: 0">
			<!-- 第一列 -->
			<div class="input-field-wrap">
				<div class="form-wrap">
					<div class="form-row">
						<label for=""> <span class="block-label">舊密碼</span> <input
							type="password" id="pwd_old" style="width: 200px;" name="password_old">
						</label>
					</div>
					<div class="form-row">
						<label for=""> <span class="block-label">新密碼</span> <input
							type="password" id="pwd_new" style="width: 200px;" name="password_new">
						</label>
					</div>
					<div class="form-row">
						<label for=""> <span class="block-label">確認新密碼</span> <input
							type="password" id="pwd_new_confirm" style="width: 200px;" name="password_new_confirm">
						</label>
					</div>
					<font color=brown>密碼規則：長度為6~20個字，且必須包含數字及小寫英文字母。</font><br>
					<br>
					<div class="btn-row">
						<button id="password_btn" class="btn btn-exec btn-wide">更改密碼</button>
					</div>
				</div>
				<!-- /.form-wrap -->
			</div>
			<!-- /.input-field-wrap -->
			<div id="ok" align="center" style="color: #00CC00; font-size: 32px;"></div>
		</div>
	</div>

	<div id="dialog-confirm" title="提示" style="display: none;"></div>
</body>
</html>