package tw.com.aber.login.controller;

import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
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

import tw.com.aber.vo.MenuVO;
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
		LoginService loginserver=new LoginService();
		if(loginserver.checkconnect()==false){
			response.getWriter().write("{\"message\":\"connect_error\"}");
			return;
		}
		request.setCharacterEncoding("UTF-8");
		response.setCharacterEncoding("UTF-8");
		String action = request.getParameter("action");
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
						// HttpSession session = request.getSession();
						session.setAttribute("sessionID", session.getId());
						session.setAttribute("user_id", list.get(0).getUser_id());
						session.setAttribute("group_id", list.get(0).getGroup_id());
						session.setAttribute("user_name", list.get(0).getUser_name());
						session.setAttribute("privilege", list.get(0).getPrivilege());
						
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

	/*************************** 制定規章方法 ****************************************/

	interface login_interface {

		public List<UserVO> loginDB(String p_email, String p_password, String p_unicode);

		public Boolean checkuser(String p_email);
		
		public Boolean checkconnect();
		
		public Boolean checkunicode(String unicode);
		
		public List<MenuVO> getMainMenuDB();

		public List<MenuVO> getSubMenuDB(String id);
	}

	/*************************** 處理業務邏輯 ****************************************/
	class LoginService {
		private login_interface dao;

		public LoginService() {
			dao = new loginDAO();
		}

		public List<UserVO> selectlogin(String p_email, String p_password,String p_unicode) {
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
		
		public List<MenuVO> getMenuList() {
			List<MenuVO> main = null;
			
			logger.debug("getMainMenu start");
			
			main = dao.getMainMenuDB();
			logger.debug("getMainMenu end");
			
			for (int i = 0; i < main.size(); i++) {
				List<MenuVO> subMenu = null;
				
				logger.debug("get subMenu start:" + main.get(i).getId());
				
				subMenu = setSubMenu( main.get(i).getId() );
				main.get(i).setSubMenu(subMenu);
				
				logger.debug("get subMenu end");
			};
			
			return main;
		}
		
		public List<MenuVO> setSubMenu(String parent_id){
			List<MenuVO> temp = null;
			
			temp = dao.getSubMenuDB(parent_id);
			
			if (temp == null) {
				return null;
			} else {
				for(int i = 0; i < temp.size(); i++) {
					List<MenuVO> tempSub = null;
					tempSub = setSubMenu( temp.get(i).getId() );
					temp.get(i).setSubMenu(tempSub);
				}
			}
			return temp;
		}
		
		public String getMenuListToString() {
			List<MenuVO> list = getMenuList();

			logger.debug("result getMenu list size: " + list.size());

			Gson gson = new Gson();
			String jsonStrList = gson.toJson(list);
			
			return jsonStrList;
		}
		
	}

	/*************************** 操作資料庫 ****************************************/
	class loginDAO implements login_interface {
		// 會使用到的Stored procedure
		private static final String sp_login = "call sp_login(?,?,?)";
		private static final String sp_checkuser = "call sp_checkuser(?,?)";
		private static final String sp_check_unicode  = "call sp_check_unicode (?,?)";
		private static final String sp_get_main_menu = "call sp_get_main_menu()";
		private static final String sp_get_submenu_by_parent_id = "call sp_get_submenu_by_parent_id(?)";
		
		private final String dbURL = getServletConfig().getServletContext().getInitParameter("dbURL")
				+ "?useUnicode=true&characterEncoding=utf-8&useSSL=false";
		private final String dbUserName = getServletConfig().getServletContext().getInitParameter("dbUserName");
		private final String dbPassword = getServletConfig().getServletContext().getInitParameter("dbPassword");

		@Override
		public List<UserVO> loginDB(String p_email, String p_password, String p_unicode) {
			List<UserVO> list = new ArrayList<UserVO>();
			UserVO UserVO = null;

			Connection con = null;
			PreparedStatement pstmt = null;
			ResultSet rs = null;

			try {
				Class.forName("com.mysql.jdbc.Driver");
				con = DriverManager.getConnection(dbURL, dbUserName, dbPassword);
				pstmt = con.prepareStatement(sp_login);
				pstmt.setString(1, p_email);
				pstmt.setString(2, p_password);
				pstmt.setString(3, p_unicode);
				rs = pstmt.executeQuery();
				
				while (rs.next()) {
					UserVO = new UserVO();
					UserVO.setUser_id(rs.getString("uid"));
					UserVO.setGroup_id(rs.getString("gid"));
					UserVO.setUser_name(rs.getString("user"));
					UserVO.setRole(rs.getString("role"));
					UserVO.setPrivilege(rs.getString("privilege"));
					list.add(UserVO);
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
				System.out.println(e.toString());
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
		
		@Override
		public List<MenuVO> getMainMenuDB() {
			List<MenuVO> list = new ArrayList<MenuVO>();

			MenuVO menuVO = null;
			Connection con = null;
			PreparedStatement pstmt = null;
			ResultSet rs = null;

			try {
				Class.forName("com.mysql.jdbc.Driver");
				con = DriverManager.getConnection(dbURL, dbUserName, dbPassword);
				pstmt = con.prepareStatement(sp_get_main_menu);

				rs = pstmt.executeQuery();
				while (rs.next()) {
					menuVO = new MenuVO();
					
					menuVO.setId(null2str(rs.getString("id")));
					menuVO.setMenuName(null2str(rs.getString("menu_name")));
					menuVO.setUrl(null2str(rs.getString("url")));
					menuVO.setSeqNo(null2str(rs.getString("seq_no")));
					menuVO.setParentId(null2str(rs.getString("parent_id")));
					menuVO.setPhotoPath(null2str(rs.getString("photo_path")));
					
					list.add(menuVO); // Store the row in the list
				}
				// Handle any driver errors
			} catch (SQLException se) {
				throw new RuntimeException("A database error occured. " + se.getMessage());
			} catch (ClassNotFoundException cnfe) {
				throw new RuntimeException("A database error occured. " + cnfe.getMessage());
				// Clean up JDBC resources
			} finally {
				if (rs != null) {
					try {
						rs.close();
					} catch (SQLException se) {
						se.printStackTrace(System.err);
					}
				}
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
		public List<MenuVO> getSubMenuDB(String parent_id) {
			List<MenuVO> list = new ArrayList<MenuVO>();

			MenuVO menuVO = null;
			Connection con = null;
			PreparedStatement pstmt = null;
			ResultSet rs = null;

			try {
				Class.forName("com.mysql.jdbc.Driver");
				con = DriverManager.getConnection(dbURL, dbUserName, dbPassword);
				pstmt = con.prepareStatement(sp_get_submenu_by_parent_id);
				pstmt.setString(1, parent_id);
				rs = pstmt.executeQuery();
				while (rs.next()) {
					menuVO = new MenuVO();
					
					menuVO.setId(null2str(rs.getString("id")));
					menuVO.setMenuName(null2str(rs.getString("menu_name")));
					menuVO.setUrl(null2str(rs.getString("url")));
					menuVO.setSeqNo(null2str(rs.getString("seq_no")));
					menuVO.setParentId(null2str(rs.getString("parent_id")));
					menuVO.setPhotoPath(null2str(rs.getString("photo_path")));
					
					list.add(menuVO); // Store the row in the list
				}
				// Handle any driver errors
			} catch (SQLException se) {
				throw new RuntimeException("A database error occured. " + se.getMessage());
			} catch (ClassNotFoundException cnfe) {
				throw new RuntimeException("A database error occured. " + cnfe.getMessage());
				// Clean up JDBC resources
			} finally {
				if (rs != null) {
					try {
						rs.close();
					} catch (SQLException se) {
						se.printStackTrace(System.err);
					}
				}
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

	}
	
	public String null2str(Object object) {
		return object == null ? "" : object.toString();
	}
}