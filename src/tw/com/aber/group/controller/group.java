package tw.com.aber.group.controller;

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
public class group extends HttpServlet {

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doPost(request, response);
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		request.setCharacterEncoding("UTF-8");
		response.setCharacterEncoding("UTF-8");
		GroupService groupService = null;
		String action = request.getParameter("action");
		String group_id = request.getSession().getAttribute("group_id").toString();
		String user_id = request.getSession().getAttribute("user_id").toString();
		
		if ("searh".equals(action)) {
			try {
				/*************************** 1.接收請求參數 ****************************************/
				String group_id2 = request.getParameter("group_id");
				/*************************** 2.開始查詢資料 ****************************************/
				// 假如無查詢條件，則是查詢全部
				if (group_id2 == null || (group_id2.trim()).length() == 0) {
					groupService = new GroupService();
					List<GroupBean> list = groupService.getSearchAllDB(group_id);
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
		if ("update".equals(action)) {
			try {
				/*************************** 1.接收請求參數 ***************************************/
				String group_name = request.getParameter("group_name");
				String group_unicode = request.getParameter("group_unicode");
				String address = request.getParameter("address");
				String phone = request.getParameter("phone");
				String fax = request.getParameter("fax");
				String mobile = request.getParameter("mobile");
				String email = request.getParameter("email");
				String master = request.getParameter("master");

				/*************************** 2.開始修改資料 ***************************************/
				groupService = new GroupService();
				groupService.updateGroup(group_id, group_name, group_unicode, address, phone, fax, mobile, email, master, user_id);
				/*************************** 3.修改完成,準備轉交(Send the Success view) ***********/
				groupService = new GroupService();
				List<GroupBean> list = groupService.getSearchAllDB(group_id);
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
	public class GroupBean implements java.io.Serializable {
		private String  group_id;
		private String  group_name;
		private String  group_unicode;
		private String  address;
		private String 	phone;
		private String	fax;
		private String	mobile;	
		private String  email;
		private String  master;	
		private String user_id;
		public String getGroup_id() {
			return group_id;
		}
		public void setGroup_id(String group_id) {
			this.group_id = group_id;
		}
		public String getGroup_name() {
			return group_name;
		}
		public void setGroup_name(String group_name) {
			this.group_name = group_name;
		}
		public String getGroup_unicode() {
			return group_unicode;
		}
		public void setGroup_unicode(String group_unicode) {
			this.group_unicode = group_unicode;
		}
		public String getAddress() {
			return address;
		}
		public void setAddress(String address) {
			this.address = address;
		}
		public String getPhone() {
			return phone;
		}
		public void setPhone(String phone) {
			this.phone = phone;
		}
		public String getFax() {
			return fax;
		}
		public void setFax(String fax) {
			this.fax = fax;
		}
		public String getMobile() {
			return mobile;
		}
		public void setMobile(String mobile) {
			this.mobile = mobile;
		}
		public String getEmail() {
			return email;
		}
		public void setEmail(String email) {
			this.email = email;
		}
		public String getMaster() {
			return master;
		}
		public void setMaster(String master) {
			this.master = master;
		}
		public String getUser_id() {
			return user_id;
		}
		public void setUser_id(String user_id) {
			this.user_id = user_id;
		}
		
		
	}

	/*************************** 制定規章方法 ****************************************/
	interface Group_interface {
		public void updateDB(GroupBean groupBean);
		
		public List<GroupBean> searchAllDB(String group_id);
	}

	/*************************** 處理業務邏輯 ****************************************/
	class GroupService {
		private Group_interface dao;

		public GroupService() {
			dao = new GroupDAO();
		}

		

		public GroupBean updateGroup(String group_id, String group_name, String group_unicode,
				String address,String phone,String fax,String mobile,String email,String master,String user_id) {
			GroupBean groupBean = new GroupBean();
			groupBean.setGroup_id(group_id);
			groupBean.setGroup_name(group_name);
			groupBean.setGroup_unicode(group_unicode);
			groupBean.setAddress(address);
			groupBean.setPhone(phone);
			groupBean.setFax(fax);
			groupBean.setMobile(mobile);
			groupBean.setEmail(email);
			groupBean.setMaster(master);
			groupBean.setUser_id(user_id);
			dao.updateDB(groupBean);
			return groupBean;
		}

	
		public List<GroupBean> getSearchAllDB(String group_id) {
			return dao.searchAllDB(group_id);
		}
	}

	/*************************** 操作資料庫 ****************************************/
	class GroupDAO implements Group_interface {
		// 會使用到的Stored procedure	
		private static final String sp_selectall_group = "call sp_selectall_group(?)";
		private static final String sp_update_group = "call sp_update_group(?,?,?,?,?,?,?,?,?,?)";

		private final String dbURL = getServletConfig().getServletContext().getInitParameter("dbURL");
		private final String dbUserName = getServletConfig().getServletContext().getInitParameter("dbUserName");
		private final String dbPassword = getServletConfig().getServletContext().getInitParameter("dbPassword");

		
		@Override
		public void updateDB(GroupBean groupBean) {
			// TODO Auto-generated method stub
			Connection con = null;
			PreparedStatement pstmt = null;
			try {
				Class.forName("com.mysql.jdbc.Driver");
				con = DriverManager.getConnection(dbURL, dbUserName, dbPassword);
				pstmt = con.prepareStatement(sp_update_group);

				pstmt.setString(1, groupBean.getGroup_id());
				pstmt.setString(2, groupBean.getGroup_name());
				pstmt.setString(3, groupBean.getGroup_unicode());
				pstmt.setString(4, groupBean.getAddress());
				pstmt.setString(5, groupBean.getPhone());
				pstmt.setString(6, groupBean.getFax());
				pstmt.setString(7, groupBean.getMobile());
				pstmt.setString(8, groupBean.getEmail());
				pstmt.setString(9, groupBean.getMaster());
				pstmt.setString(10, groupBean.getUser_id());
				
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
		public List<GroupBean> searchAllDB(String group_id) {
			// TODO Auto-generated method stub
			List<GroupBean> list = new ArrayList<GroupBean>();
			GroupBean groupBean = null;

			Connection con = null;
			PreparedStatement pstmt = null;
			ResultSet rs = null;

			try {
				Class.forName("com.mysql.jdbc.Driver");
				con = DriverManager.getConnection(dbURL, dbUserName, dbPassword);
				pstmt = con.prepareStatement(sp_selectall_group);
				pstmt.setString(1, group_id);
				rs = pstmt.executeQuery();
				while (rs.next()) {
					groupBean = new GroupBean();
					groupBean.setGroup_id(rs.getString("group_id"));
					groupBean.setGroup_name(rs.getString("group_name"));
					groupBean.setGroup_unicode(rs.getString("group_unicode"));
					groupBean.setAddress(rs.getString("address"));
					groupBean.setPhone(rs.getString("phone"));
					groupBean.setFax(rs.getString("fax"));
					groupBean.setMobile(rs.getString("mobile"));
					groupBean.setEmail(rs.getString("email"));
					groupBean.setMaster(rs.getString("master"));
					list.add(groupBean); // Store the row in the list
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

	}

}
