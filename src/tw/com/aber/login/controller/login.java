package tw.com.aber.login.controller;

import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.google.gson.Gson;

import tw.com.aber.service.LoginService;
import tw.com.aber.sftransfer.controller.ValueService;
import tw.com.aber.vo.UserVO;

public class login extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private static final Logger logger = LogManager.getLogger(login.class);

	private static final int WIDTH = 120; // 圖片寬度
	private static final int HEIGHT = 30; // 圖片高度

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doPost(request, response);
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		request.setCharacterEncoding("UTF-8");
		response.setCharacterEncoding("UTF-8");
		String action = request.getParameter("action");
		
		logger.debug("Action:" + action);
		
		HttpSession session = request.getSession(true);
		UserVO message = null;
		LoginService loginService = null;
		Gson gson = null;
		if ("check_unicode_exist".equals(action)){
			String unicode = request.getParameter("unicode");
			loginService = new LoginService();
			String uni_check="{\"message\":\""+loginService.checkunicode(unicode)+"\"}";
			response.getWriter().write(uni_check);
			return;
		}
		if ("login".equals(action)) {
			String username = request.getParameter("userId");
			String password = request.getParameter("pswd");
			String unicode = request.getParameter("unicode");
			// 獲取驗證碼
			String validateCode = request.getParameter("validateCode").trim();
			Object checkcode = session.getAttribute("checkcode");
			
			loginService = new LoginService();
			if(!loginService.checkuser(username)){
				message = new UserVO();
				message.setMessage("user_failure");
				gson = new Gson();
				String jsonStrList = gson.toJson(message);
				response.getWriter().write(jsonStrList);
				return;
			}
			if (!checkcode.equals(convertToCapitalString(validateCode))) {
				message = new UserVO();
				message.setMessage("code_failure");
				gson = new Gson();
				String jsonStrList = gson.toJson(message);
				response.getWriter().write(jsonStrList);
				return;
			}
			if (checkcode.equals(convertToCapitalString(validateCode))) {
				loginService = new LoginService();
				try{
					List<UserVO> list = loginService.selectlogin(username, password,unicode);
					if (list.size() != 0) {
						logger.info("Unicode:" + unicode);
						logger.info("Client IP:" + getClientIp(request));
						
						// HttpSession session = request.getSession();
						session.setAttribute("sessionID", session.getId());
						session.setAttribute("user_id", list.get(0).getUser_id());
						session.setAttribute("group_id", list.get(0).getGroup_id());
						session.setAttribute("user_name", list.get(0).getUser_name());
						session.setAttribute("privilege", list.get(0).getPrivilege());
						
						UserVO userVO = list.get(0);
						ValueService valueService = new ValueService(this.getServletConfig().getServletContext(),userVO);
						session.setAttribute("valueService", valueService);

						String menuListStr = loginService.getMenuListToString();
						session.setAttribute("menu", menuListStr);
						
						//log.txt
						try{
							String record_log = getServletConfig().getServletContext().getInitParameter("uploadpath")+"/log.txt";
							String my_msg =(new SimpleDateFormat("yyyy-MM-dd(u) HH:mm:ss").format(new Date()))+":\r\n  "+list.get(0).getUser_name()+" login.\r\n";
							FileWriter fw;
							try{
								fw = new FileWriter(record_log,true);
							}catch(FileNotFoundException e){
								fw = new FileWriter(record_log,false);
							}
							fw.write(my_msg);
							fw.close();
						}catch(Exception e){System.out.println("Error: "+e.toString());}
						//log.txt
						message = new UserVO();
						message.setMessage("success");
					} else {
						message = new UserVO();
						message.setMessage("failure");
					}
				}catch(Exception e){
					message = new UserVO();
					message.setMessage("uni_failure");
				}
				gson = new Gson();
				String jsonStrList = gson.toJson(message);
				response.getWriter().write(jsonStrList);
			}
		}
		if ("check_user_exist".equals(action)) {
			String username = request.getParameter("userId");
			loginService = new LoginService();
			if(!loginService.checkuser(username)){
				message = new UserVO();
				message.setMessage("user_failure");
				gson = new Gson();
				String jsonStrList = gson.toJson(message);
				response.getWriter().write(jsonStrList);
			}
			if(loginService.checkuser(username)){
				message = new UserVO();
				message.setMessage("success");
				gson = new Gson();
				String jsonStrList = gson.toJson(message);
				response.getWriter().write(jsonStrList);
			}
		}
		if ("logout".equals(action)) {		
			session.setAttribute("sessionID", null);
			session.setAttribute("user_id", null);
			session.setAttribute("group_id", null);
			session.setAttribute("user_name", null);
			session.setAttribute("valueService", null);
		}
	}

	/**
	 * 將一個字符串中的小寫字母轉換為大寫字母
	 * 
	 * */
	public static String convertToCapitalString(String src) {
		char[] array = src.toCharArray();
		int temp = 0;
		for (int i = 0; i < array.length; i++) {
			temp = (int) array[i];
			if (temp <= 122 && temp >= 97) { // array[i]為小寫字母
				array[i] = (char) (temp - 32);
			}
		}
		return String.valueOf(array);
	}
	
	public String null2str(Object object) {
		return object == null ? "" : object.toString();
	}
	
	private static String getClientIp(HttpServletRequest request) {

        String remoteAddr = "";

        if (request != null) {
            remoteAddr = request.getHeader("X-FORWARDED-FOR");
            if (remoteAddr == null || "".equals(remoteAddr)) {
                remoteAddr = request.getRemoteAddr();
            }
        }

        return remoteAddr;
    }
}