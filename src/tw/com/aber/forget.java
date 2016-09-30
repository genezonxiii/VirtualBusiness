package tw.com.aber;

import java.io.BufferedReader;
import java.io.IOException;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.sql.DataSource;

import com.google.gson.Gson;

//@SuppressWarnings("serial")
public class forget extends HttpServlet {

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doPost(request, response);
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		request.setCharacterEncoding("UTF-8");
		response.setCharacterEncoding("UTF-8");
		PasswordBeanService  passwordBeanService = null;
		String action = request.getParameter("action");
		String group_id = request.getParameter("group_id");
		String user_id = request.getSession().getAttribute("user_id").toString();
		
		if ("update".equals(action)) {
			try {
				/*************************** 1.接收請求參數 ***************************************/
				String password = request.getParameter("password");
				
				/*************************** 2.開始修改資料 ***************************************/
				passwordBeanService = new PasswordBeanService();
				passwordBeanService.updatePassword(user_id,password);
				/*************************** 3.修改完成,準備轉交(Send the Success view) ***********/
				passwordBeanService = new PasswordBeanService();
			    PasswordBean list = passwordBeanService.updatePassword(user_id,password);
				Gson gson = new Gson();
				String jsonStrList = gson.toJson(list);
				response.getWriter().write(jsonStrList);
				/*************************** 其他可能的錯誤處理 **********************************/
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		
	}

	/************************* 對應資料庫表格格式 **************************************/
	public class PasswordBean implements java.io.Serializable {
		private String user_id;
		private String password;
		public String getUser_id() {
			return user_id;
		}
		public void setUser_id(String user_id) {
			this.user_id = user_id;
		}
		public String getPassword() {
			return password;
		}
		public void setPassword(String password) {
			this.password = password;
		}
	}

	/*************************** 制定規章方法 ****************************************/
	interface PasswordBean_interface {

		public void updateDB(PasswordBean passwordBean);

	}

	/*************************** 處理業務邏輯 ****************************************/
	class PasswordBeanService {
		private PasswordBean_interface dao;

		public PasswordBeanService() {
			dao = new PasswordBeanDAO();
		}


		public PasswordBean updatePassword(String user_id, String password) {
			PasswordBean passwordBean = new PasswordBean();
			passwordBean.setUser_id(user_id);;
			passwordBean.setPassword(password);
		
			dao.updateDB(passwordBean);
			return passwordBean;
		}

	}

	/*************************** 操作資料庫 ****************************************/
	class PasswordBeanDAO implements PasswordBean_interface {
		// 會使用到的Stored procedure
		private static final String sp_update_password = "call sp_update_password (?,?)";

		private final String dbURL = getServletConfig().getServletContext().getInitParameter("dbURL")
				+ "?useUnicode=true&characterEncoding=utf-8&useSSL=false";
		private final String dbUserName = getServletConfig().getServletContext().getInitParameter("dbUserName");
		private final String dbPassword = getServletConfig().getServletContext().getInitParameter("dbPassword");


		@Override
		public void updateDB(PasswordBean passwordBean) {
			// TODO Auto-generated method stub
			Connection con = null;
			PreparedStatement pstmt = null;
			try {
				Class.forName("com.mysql.jdbc.Driver");	
				con = DriverManager.getConnection(dbURL, dbUserName, dbPassword);
				pstmt = con.prepareStatement(sp_update_password);
				pstmt.setString(1, passwordBean.getUser_id());
				pstmt.setString(2, passwordBean.getPassword());
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
