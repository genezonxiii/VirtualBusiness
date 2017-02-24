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

	