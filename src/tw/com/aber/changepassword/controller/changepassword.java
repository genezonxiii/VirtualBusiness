package tw.com.aber.changepassword.controller;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.google.gson.Gson;

import tw.com.aber.basicinfo.user;
import tw.com.aber.vo.PasswordVO;

public class changepassword extends HttpServlet {
	private static final Logger logger = LogManager.getLogger(changepassword.class);

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		doPost(request, response);
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		request.setCharacterEncoding("UTF-8");
		response.setCharacterEncoding("UTF-8");
		PasswordVOService  passwordVOService = null;
		String action = request.getParameter("action");
		String group_id = request.getSession().getAttribute("group_id").toString();
		String user_id = request.getSession().getAttribute("user_id").toString();
		
		logger.debug("action: "+action);
		logger.debug("group_id: "+group_id);
		logger.debug("user_id: "+user_id);
		
		if ("update".equals(action)) {
			try {
				/*************************** 1.接收請求參數 ***************************************/
				String passwordOld = request.getParameter("password_old");
				String passwordNew = request.getParameter("password_new");
				
				/*************************** 3.修改完成,準備轉交(Send the Success view) ***********/
				passwordVOService = new PasswordVOService();
			    PasswordVO list = passwordVOService.updatePassword(user_id, passwordOld, passwordNew);
				Gson gson = new Gson();
				String jsonStrList = gson.toJson(list);
				response.getWriter().write(jsonStrList);
				/*************************** 其他可能的錯誤處理 **********************************/
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		
	}

	/*************************** 制定規章方法 ****************************************/
	interface PasswordVO_interface {

		public void updateDB(PasswordVO passwordVO);

	}

	/*************************** 處理業務邏輯 ****************************************/
	class PasswordVOService {
		private PasswordVO_interface dao;

		public PasswordVOService() {
			dao = new PasswordVODAO();
		}


		public PasswordVO updatePassword(String user_id, String passwordOld, String passwordNew) {
			PasswordVO passwordVO = new PasswordVO();
			passwordVO.setUser_id(user_id);
			passwordVO.setPasswordOld(passwordOld);
			passwordVO.setPasswordNew(passwordNew);
		
			dao.updateDB(passwordVO);
			return passwordVO;
		}

	}

	/*************************** 操作資料庫 ****************************************/
	class PasswordVODAO implements PasswordVO_interface {
		// 會使用到的Stored procedure
		private static final String sp_update_password = "call sp_update_password (?,?,?)";

		private final String dbURL = getServletConfig().getServletContext().getInitParameter("dbURL")
				+ "?useUnicode=true&characterEncoding=utf-8&useSSL=false";
		private final String dbUserName = getServletConfig().getServletContext().getInitParameter("dbUserName");
		private final String dbPassword = getServletConfig().getServletContext().getInitParameter("dbPassword");


		@Override
		public void updateDB(PasswordVO passwordVO) {
			
			Connection con = null;
			PreparedStatement pstmt = null;
			try {
				Class.forName("com.mysql.jdbc.Driver");	
				con = DriverManager.getConnection(dbURL, dbUserName, dbPassword);
				pstmt = con.prepareStatement(sp_update_password);
				pstmt.setString(1, passwordVO.getUser_id());
				pstmt.setString(2, passwordVO.getPasswordOld());
				pstmt.setString(3, passwordVO.getPasswordNew());
				pstmt.executeUpdate();

				// Handle any SQL errors
			} catch (SQLException se) {
				throw new RuntimeException("A database error occured. " + se.getMessage());
				// Clean up JDBC resources
			} catch (ClassNotFoundException cnfe) {
				throw new RuntimeException("A database error occured. " + cnfe.getMessage());
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
		}
	}

}
