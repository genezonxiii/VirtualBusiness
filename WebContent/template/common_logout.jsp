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
</script>