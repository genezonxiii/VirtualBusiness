<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<script>
$("#logout").click(function(e) {
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

function warningMsg(title, msg) {
	$("<div/>")
		.html(msg)
		.dialog({
			title: title,
			draggable : true,
			resizable : false,
			autoOpen : true,
			height : "auto",
			modal : true,
			buttons : [{
				text: "確認", 
				click: function() { 
					$(this).dialog("close");
				}
			}]
		});
}
</script>