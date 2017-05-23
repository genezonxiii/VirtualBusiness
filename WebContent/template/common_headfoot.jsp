<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<div class="header">
	<h1 ondblclick="location.href = './welcome.jsp';">智慧電商平台</h1>
	<div class="userinfo">
		<p>使用者<span><%= (request.getSession().getAttribute("user_name")==null)?"尚未登入?":request.getSession().getAttribute("user_name").toString() %></span></p>
		<a href='#' id="logout" class="btn-logout" >登出</a>
	</div>
</div>

<div class="sidenavpanel"></div>

<div class="sidenav"></div>

<footer class="footer">
北祥股份有限公司 <span>服務電話：+886-2-2658-1910  |  傳真：+886-2-2658-1920</span>
</footer>