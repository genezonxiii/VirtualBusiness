package tw.com.aber.user.controller;

import java.io.BufferedReader;
import java.io.IOException;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
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
public class user extends HttpServlet {

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doPost(request, response);
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		request.setCharacterEncoding("UTF-8");
		response.setCharacterEncoding("UTF-8");
		UserService userService = null;
		String action = request.getParameter("action");
		String group_id = request.getSession().getAttribute("group_id").toString();
		String user_id = request.getSession().getAttribute("user_id").toString();
		
		
		if ("check_email".equals(action)) {
			try {
				/*************************** 1.接收請求參數 ****************************************/
				//String user_name = request.getParameter("user_name");
				String email=request.getParameter("email");
				/*************************** 2.開始查詢資料 ****************************************/
				// 假如無查詢條件，則是查詢全部
				userService = new UserService();
//				System.out.println(userService.checkemail(group_id,email));
				String which=userService.checkemail(group_id,email)+"";
				response.getWriter().write(which);
				//Gson gson = new Gson();
				//String jsonStrList = gson.toJson(list);
				//response.getWriter().write(jsonStrList);
				return;// 程式中斷
				/*************************** 其他可能的錯誤處理 **********************************/
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		if ("searh".equals(action)) {
			try {
				/*************************** 1.接收請求參數 ****************************************/
				String user_name = request.getParameter("user_name");
				/*************************** 2.開始查詢資料 ****************************************/
				// 假如無查詢條件，則是查詢全部
				if (user_name == null || (user_name.trim()).length() == 0) {
					userService = new UserService();
					List<UserBean> list = userService.getSearchAllDB(group_id);
					request.setAttribute("action", "searchResults");
					request.setAttribute("list", list);
					Gson gson = new Gson();
					String jsonStrList = gson.toJson(list);
					response.getWriter().write(jsonStrList);
					return;// 程式中斷
				}
				/*************************** 其他可能的錯誤處理 **********************************/
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		if ("insert".equals(action)) {
			try {
				/*************************** 1.接收請求參數 **************************************/
				String user_name = request.getParameter("user_name");
				String role = request.getParameter("role");
				String email = request.getParameter("email");
				String password = request.getParameter("password");
				/*************************** 2.開始新增資料 ***************************************/
				userService = new UserService();
				userService.addUser(group_id, user_name,role,email,password);

				/*************************** 3.新增完成,準備轉交(Send the Success view) ***********/
				userService = new UserService();
				List<UserBean> list = userService.getSearchAllDB(group_id);
				Gson gson = new Gson();
				String jsonStrList = gson.toJson(list);
				response.getWriter().write(jsonStrList);
				/*************************** 其他可能的錯誤處理 **********************************/
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		if ("update".equals(action)) {
			try {
				/*************************** 1.接收請求參數 ***************************************/
				String user_id1 = request.getParameter("user_id");
				String user_name = request.getParameter("user_name");
				String role = request.getParameter("role");
				String email = request.getParameter("email");
				
				/*************************** 2.開始修改資料 ***************************************/
				userService = new UserService();
				userService.updateUser(user_id1, group_id, user_name, role, email);
				/*************************** 3.修改完成,準備轉交(Send the Success view) ***********/
				userService = new UserService();
				List<UserBean> list = userService.getSearchAllDB(group_id);
				Gson gson = new Gson();
				String jsonStrList = gson.toJson(list);
				response.getWriter().write(jsonStrList);
				/*************************** 其他可能的錯誤處理 **********************************/
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		if ("delete".equals(action)) {
			try {
				/*************************** 1.接收請求參數 ***************************************/
				String operation = request.getParameter("operation");
				String user_id2 = request.getParameter("user_id");
				/*************************** 2.開始刪除資料 ***************************************/
				userService = new UserService();
				userService.deleteUser(user_id2,operation);
				/*************************** 3.刪除完成,準備轉交(Send the Success view) ***********/
				userService = new UserService();
				List<UserBean> list = userService.getSearchAllDB(group_id);
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
	public class UserBean implements java.io.Serializable {
		private String  user_id;
		private String 	group_id;
		private String  user_name;
		private String  role;
		private String  email;
		private String  password;
		private String  operation;
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
		public String getOperation() {
			return operation;
		}
		public void setOperation(String operation) {
			this.operation = operation;
		}
		
	
	}

	/*************************** 制定規章方法 ****************************************/
	interface User_interface {

		public void insertDB(UserBean userBean);

		public void updateDB(UserBean userBean);

		public void deleteDB(String user_id,String operation);

		public List<UserBean> searchAllDB(String group_id);
		
		public Boolean checkemail(String group_id, String email);
	}

	/*************************** 處理業務邏輯 ****************************************/
	class UserService {
		private User_interface dao;

		public UserService() {
			dao = new UserDAO();
		}

		public UserBean addUser(String group_id, String user_name,String role,String email,String password	) {
			UserBean userBean = new UserBean();
			userBean.setGroup_id(group_id);
			userBean.setUser_name(user_name);
			userBean.setRole(role);
			userBean.setEmail(email);
			userBean.setPassword(password);
			dao.insertDB(userBean);
			return userBean;
		}

		public UserBean updateUser(String user_id, String group_id, String user_name,String role,String email) {
			UserBean userBean = new UserBean();
			userBean.setUser_id(user_id);
			userBean.setGroup_id(group_id);
			userBean.setUser_name(user_name);
			userBean.setRole(role);
			userBean.setEmail(email);
			dao.updateDB(userBean);
			return userBean;
		}

		public void deleteUser(String user_id,String operation) {
			dao.deleteDB(user_id,operation);
		}

		public List<UserBean> getSearchAllDB(String group_id) {
			return dao.searchAllDB(group_id);
		}
		public Boolean checkemail(String group_id,String email){
			return dao.checkemail(group_id,email);
		}
	}

	/*************************** 操作資料庫 ****************************************/
	class UserDAO implements User_interface {
		// 會使用到的Stored procedure	
		private static final String sp_insert_user = "call sp_insert_user(?,?,?,?,?)";
		private static final String sp_selectall_user = "call sp_selectall_user(?)";
		private static final String sp_del_user = "call sp_del_user(?,?)";
		private static final String sp_update_user = "call sp_update_user(?,?,?,?,?)";
		private static final String sp_check_email = "call sp_check_email(?,?,?)";
		 

		private final String dbURL = getServletConfig().getServletContext().getInitParameter("dbURL")
				+ "?useUnicode=true&characterEncoding=utf-8&useSSL=false";
		private final String dbUserName = getServletConfig().getServletContext().getInitParameter("dbUserName");
		private final String dbPassword = getServletConfig().getServletContext().getInitParameter("dbPassword");

		@Override
		public void insertDB(UserBean userBean) {
			// TODO Auto-generated method stub
			Connection con = null;
			PreparedStatement pstmt = null;
			try {
				Class.forName("com.mysql.jdbc.Driver");
				con = DriverManager.getConnection(dbURL, dbUserName, dbPassword);
				pstmt = con.prepareStatement(sp_insert_user);

				pstmt.setString(1, userBean.getGroup_id());
				pstmt.setString(2, userBean.getUser_name());
				pstmt.setString(3, userBean.getRole());
				pstmt.setString(4, userBean.getEmail());
				pstmt.setString(5, userBean.getPassword());
				
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

		@Override
		public void updateDB(UserBean userBean) {
			// TODO Auto-generated method stub
			Connection con = null;
			PreparedStatement pstmt = null;
			try {
				Class.forName("com.mysql.jdbc.Driver");
				con = DriverManager.getConnection(dbURL, dbUserName, dbPassword);
				pstmt = con.prepareStatement(sp_update_user);
				pstmt.setString(1, userBean.getUser_id());
				pstmt.setString(2, userBean.getGroup_id());
				pstmt.setString(3, userBean.getUser_name());
				pstmt.setString(4, userBean.getRole());
				pstmt.setString(5, userBean.getEmail());
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

		@Override
		public void deleteDB(String user_id,String operation) {
			// TODO Auto-generated method stub
			Connection con = null;
			PreparedStatement pstmt = null;
			try {
				Class.forName("com.mysql.jdbc.Driver");
				con = DriverManager.getConnection(dbURL, dbUserName, dbPassword);
				pstmt = con.prepareStatement(sp_del_user);
				pstmt.setString(1, user_id);
				pstmt.setString(2, operation);

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
		@Override
		public List<UserBean> searchAllDB(String group_id) {
			// TODO Auto-generated method stub
			List<UserBean> list = new ArrayList<UserBean>();
			UserBean userBean = null;

			Connection con = null;
			PreparedStatement pstmt = null;
			ResultSet rs = null;

			try {
				Class.forName("com.mysql.jdbc.Driver");
				con = DriverManager.getConnection(dbURL, dbUserName, dbPassword);
				pstmt = con.prepareStatement(sp_selectall_user);
				pstmt.setString(1, group_id);
				rs = pstmt.executeQuery();
				while (rs.next()) {
					userBean = new UserBean();
					userBean.setUser_id(rs.getString("user_id"));
					userBean.setGroup_id(rs.getString("group_id"));
					userBean.setUser_name(rs.getString("user_name"));
					userBean.setRole(rs.getString("role"));
					userBean.setEmail(rs.getString("email"));
					userBean.setPassword(rs.getString("password"));
					list.add(userBean);
				}

				// Handle any driver errors
			} catch (SQLException se) {
				throw new RuntimeException("A database error occured. " + se.getMessage());
				// Clean up JDBC resources
			} catch (ClassNotFoundException cnfe) {
				throw new RuntimeException("A database error occured. " + cnfe.getMessage());
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
		
		public Boolean checkemail(String group_id,String email) {
			Connection con = null;
			CallableStatement cs = null;
			Boolean rs = null;
			try {
				Class.forName("com.mysql.jdbc.Driver");
				con = DriverManager.getConnection(dbURL, dbUserName, dbPassword);
				cs = con.prepareCall(sp_check_email);
				cs.registerOutParameter(3, Types.BOOLEAN);
				cs.setString(1, group_id);
				cs.setString(2, email);
				cs.execute();
				rs = cs.getBoolean(3);
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
