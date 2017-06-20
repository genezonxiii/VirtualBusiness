/**
 * 
 */
$(function(){
	
	/*
	 * logout
	 */
	$(".btn-logout").click(function(e) {
		$.ajax({
			type : "POST",
			url : "login.do",
			data : {
				action : "logout"
			},
			success : function(result) {
				location.replace('login.jsp');
			}
		});
	});
})

function dialogMsg(title, msg) {
	$("<div></div>")
	.html(msg)
	.dialog({
		title: title,
		width: 'auto',
		minHeight: 'auto',
		modal : true,
		create: function () {
			$(this).dialog("widget").find('.ui-dialog-titlebar-close').remove()
		},
		buttons : [{
			text: "確認", 
			click: function() { 
				$(this).dialog("close");
			}
		}]
	});
}

	