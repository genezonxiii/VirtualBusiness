package tw.com.aber.login.controller;

import java.io.IOException;
import java.io.OutputStream;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.Random;

import javax.imageio.ImageIO;
import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.google.gson.Gson;

public class login extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private static final int WIDTH = 120; // 圖片寬度
	private static final int HEIGHT = 30; // 圖片高度

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doPost(request, response);
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		LoginService loginserver=new LoginService();
		if(loginserver.checkconnect()==false){
			response.getWriter().write("{\"message\":\"connect_error\"}");
			return;
		}
		
		request.setCharacterEncoding("UTF-8");
		response.setCharacterEncoding("UTF-8");
		String action = request.getParameter("action");
		HttpSession session = request.getSession(true);
		LoginVO message = null;
		LoginService loginService = null;
		Gson gson = null;
		//System.out.println("action= "+action);
		if ("check_unicode_exist".equals(action)){
			String unicode = request.getParameter("unicode");
			loginService = new LoginService();
			//System.out.println(unicode +" return: "+loginService.checkunicode(unicode));
			String uni_check="{\"message\":\""+loginService.checkunicode(unicode)+"\"}";
			response.getWriter().write(uni_check);
			return;
		}
		if ("login".equals(action)) {
			String username = request.getParameter("userId");
			String password = request.getParameter("pswd");
			String unicode = request.getParameter("unicode");
			// 获取验证码
			String validateCode = request.getParameter("validateCode").trim();
			Object checkcode = session.getAttribute("checkcode");
			
			loginService = new LoginService();
			if(!loginService.checkuser(username)){
				message = new LoginVO();
				message.setMessage("user_failure");
				gson = new Gson();
				String jsonStrList = gson.toJson(message);
				response.getWriter().write(jsonStrList);
				return;
			}
			if (!checkcode.equals(convertToCapitalString(validateCode))) {
				message = new LoginVO();
				message.setMessage("code_failure");
				gson = new Gson();
				String jsonStrList = gson.toJson(message);
				response.getWriter().write(jsonStrList);
				return;
			}
			if (checkcode.equals(convertToCapitalString(validateCode))) {
				loginService = new LoginService();
				try{
					List<LoginVO> list = loginService.selectlogin(username, password,unicode);
					if (list.size() != 0) {
						// HttpSession session = request.getSession();
						session.setAttribute("sessionID", session.getId());
						session.setAttribute("user_id", list.get(0).getUser_id());
						session.setAttribute("group_id", list.get(0).getGroup_id());
						session.setAttribute("user_name", list.get(0).getUser_name());
						message = new LoginVO();
						message.setMessage("success");
					} else {
						message = new LoginVO();
						message.setMessage("failure");
					}
				}catch(Exception e){
					message = new LoginVO();
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
			//System.out.println("aaa= "+loginService.checkuser(username));
			if(!loginService.checkuser(username)){
				message = new LoginVO();
				message.setMessage("user_failure");
				gson = new Gson();
				String jsonStrList = gson.toJson(message);
				response.getWriter().write(jsonStrList);
			}
			if(loginService.checkuser(username)){
				message = new LoginVO();
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
			if (temp <= 122 && temp >= 97) { // array[i]为小写字母
				array[i] = (char) (temp - 32);
			}
		}
		return String.valueOf(array);
	}

	/************************* 對應資料庫表格格式 **************************************/
	@SuppressWarnings("serial")
	public class LoginVO implements java.io.Serializable {

		private String email;
		private String password;
		private String user_id;
		private String group_id;
		private String user_name;
		private String role;
		private String message;// for set check message

		public String getEmail() {
			return email;
		}

		public void setEmail(String email) {
			this.email = email;
		}

		public String getPassword() {
			return password;
		}

		public void setPassword(String password) {
			this.password = password;
		}

		public String getUser_id() {
			return user_id;
		}

		public void setUser_id(String user_id) {
			this.user_id = user_id;
		}

		public String getGroup_id() {
			return group_id;
		}

		public void setGroup_id(String group_id) {
			this.group_id = group_id;
		}

		public String getUser_name() {
			return user_name;
		}

		public void setUser_name(String user_name) {
			this.user_name = user_name;
		}

		public String getRole() {
			return role;
		}

		public void setRole(String role) {
			this.role = role;
		}

		public String getMessage() {
			return message;
		}

		public void setMessage(String message) {
			this.message = message;
		}

	}

	/*************************** 制定規章方法 ****************************************/

	interface login_interface {

		public List<LoginVO> loginDB(String p_email, String p_password, String p_unicode);

		public Boolean checkuser(String p_email);
		
		public Boolean checkconnect();
		
		public Boolean checkunicode(String unicode);
	}

	/*************************** 處理業務邏輯 ****************************************/
	class LoginService {
		private login_interface dao;

		public LoginService() {
			dao = new loginDAO();
		}

		public List<LoginVO> selectlogin(String p_email, String p_password,String p_unicode) {
			return dao.loginDB(p_email, p_password,p_unicode);
		}

		public Boolean checkuser(String p_email) {
			return dao.checkuser(p_email);
		}
		public Boolean checkconnect() {
			return dao.checkconnect();
		}
		public Boolean checkunicode(String unicode) {
			return dao.checkunicode(unicode);
		}
		
	}

	/*************************** 操作資料庫 ****************************************/
	class loginDAO implements login_interface {
		// 會使用到的Stored procedure
		private static final String sp_login = "call sp_login(?,?,?)";
		private static final String sp_checkuser = "call sp_checkuser(?,?)";
		private static final String sp_check_unicode  = "call sp_check_unicode (?,?)";
		private final String dbURL = getServletConfig().getServletContext().getInitParameter("dbURL")
				+ "?useUnicode=true&characterEncoding=utf-8&useSSL=false";
		private final String dbUserName = getServletConfig().getServletContext().getInitParameter("dbUserName");
		private final String dbPassword = getServletConfig().getServletContext().getInitParameter("dbPassword");

		@Override
		public List<LoginVO> loginDB(String p_email, String p_password, String p_unicode) {
			List<LoginVO> list = new ArrayList<LoginVO>();
			LoginVO LoginVO = null;

			Connection con = null;
			PreparedStatement pstmt = null;
			ResultSet rs = null;

			try {
				Class.forName("com.mysql.jdbc.Driver");
				con = DriverManager.getConnection(dbURL, dbUserName, dbPassword);
				System.out.println("hello"+p_email+" "+p_password+" "+p_unicode);
				pstmt = con.prepareStatement(sp_login);
				pstmt.setString(1, p_email);
				pstmt.setString(2, p_password);
				pstmt.setString(3, p_unicode);
				System.out.println("hihi");
				
				rs = pstmt.executeQuery();
				
				System.out.println("hihi2");
				
				while (rs.next()) {
					LoginVO = new LoginVO();
					LoginVO.setUser_id(rs.getString("uid"));
					LoginVO.setGroup_id(rs.getString("gid"));
					LoginVO.setUser_name(rs.getString("user"));
					LoginVO.setRole(rs.getString("role"));
					list.add(LoginVO);
				}
			} catch (SQLException se) {
				throw new RuntimeException("A database error occured. " + se.getMessage());
				
			} catch (ClassNotFoundException cnfe) {
				throw new RuntimeException("A database error occured. " + cnfe.getMessage());
				// Clean up JDBC resources
			} finally {
				if (pstmt != null) {
					try {
						pstmt.close();
					} catch (SQLException se) {
						se.printStackTrace(System.err);
					}
				}
				if (con != null) {
					try {
						con.close();
					} catch (Exception e) {
						e.printStackTrace(System.err);
					}
				}
			}
			return list;

		}

		@Override
		public Boolean checkuser(String p_email) {
			Connection con = null;
			CallableStatement cs = null;
			Boolean rs = null;
			try {
				Class.forName("com.mysql.jdbc.Driver");
				con = DriverManager.getConnection(dbURL, dbUserName, dbPassword);
				cs = con.prepareCall(sp_checkuser);
				cs.registerOutParameter(2, Types.BOOLEAN);
				cs.setString(1, p_email);
				cs.execute();
				rs = cs.getBoolean(2);
			} catch (SQLException se) {
				throw new RuntimeException("A database error occured. " + se.getMessage());
			} catch (ClassNotFoundException cnfe) {
				throw new RuntimeException("A database error occured. " + cnfe.getMessage());
				// Clean up JDBC resources
			} finally {
				if (cs != null) {
					try {
						cs.close();
					} catch (SQLException se) {
						se.printStackTrace(System.err);
					}
				}
				if (con != null) {
					try {
						con.close();
					} catch (Exception e) {
						e.printStackTrace(System.err);
					}
				}
			}
			return rs;
		}
		public Boolean checkconnect() {
			Connection con = null;
			PreparedStatement pstmt = null;
			ResultSet rs = null;
			try {
				Class.forName("com.mysql.jdbc.Driver");
				con = DriverManager.getConnection(dbURL, dbUserName, dbPassword);
				pstmt = con.prepareStatement("SELECT DISTINCT(fax) FROM tb_group");
				rs = pstmt.executeQuery();
				return true;
			} catch (Exception e) {
				return false;
			}
		}
		public Boolean checkunicode(String unicode) {
			Connection con = null;
			CallableStatement cs = null;
			Boolean rs = null;
			try {
				Class.forName("com.mysql.jdbc.Driver");
				con = DriverManager.getConnection(dbURL, dbUserName, dbPassword);
				cs = con.prepareCall(sp_check_unicode);
				cs.registerOutParameter(2, Types.BOOLEAN);
				cs.setString(1, unicode);
				cs.execute();
				rs = cs.getBoolean(2);
			} catch (SQLException se) {
				throw new RuntimeException("A database error occured. " + se.getMessage());
			} catch (ClassNotFoundException cnfe) {
				throw new RuntimeException("A database error occured. " + cnfe.getMessage());
				// Clean up JDBC resources
			} finally {
				if (cs != null) {
					try {
						cs.close();
					} catch (SQLException se) {
						se.printStackTrace(System.err);
					}
				}
				if (con != null) {
					try {
						con.close();
					} catch (Exception e) {
						e.printStackTrace(System.err);
					}
				}
			}
			return rs;
		}
	}
}