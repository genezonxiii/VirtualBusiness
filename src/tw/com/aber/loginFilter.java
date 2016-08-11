package tw.com.aber;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

public class loginFilter implements Filter {

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        // TODO Auto-generated method stub
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response,
            FilterChain chain) throws IOException, ServletException {
        // 取得在下面要用的request,response,session對象
        HttpServletRequest servletRequest = (HttpServletRequest) request;
        HttpServletResponse servletResponse = (HttpServletResponse) response;
        HttpSession session = servletRequest.getSession();
        
        // 取得用戶請求的URI
        String path = servletRequest.getRequestURI();
                
        // 登入頁面不須過濾
        if(path.indexOf("/login.jsp") > -1) {
            chain.doFilter(servletRequest, servletResponse);
            return;
        }

		// 从session裡取員工id信息
		if(session.getAttribute("group_id")==null || session.getAttribute("user_id")==null){
            // 導到登入畫面
            servletResponse.sendRedirect("./login.jsp");
        } else {
            // 已登入 繼續動作
            chain.doFilter(request, response);
        }
    }

    @Override
    public void destroy() {
        // TODO Auto-generated method stub
    }

}