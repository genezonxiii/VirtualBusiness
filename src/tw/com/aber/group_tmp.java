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
public class group_tmp extends HttpServlet {

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
		//System.out.println(action);
		if ("search".equals(action)) {
			try {
				groupService = new GroupService();
				List<GroupBean> list = groupService.getSearchAllDB();
				Gson gson = new Gson();
				String jsonStrList = gson.toJson(list);
				response.getWriter().write(jsonStrList);
				return;
			} catch (Exception e) {e.printStackTrace();}
		}else if ("insert".equals(action)) {
			try {
				String group_name = request.getParameter("group_name");
				groupService = new GroupService();
				groupService.insertGroup( group_name);
				List<GroupBean> list = groupService.getSearchAllDB();
				Gson gson = new Gson();
				String jsonStrList = gson.toJson(list);
				response.getWriter().write(jsonStrList);
				return;
			} catch (Exception e) {e.printStackTrace();}
		}else if ("update".equals(action)) {
			try {
				String group_id = request.getParameter("group_id");
				String group_name = request.getParameter("group_name");
				groupService = new GroupService();
				groupService.updateGroup(group_id, group_name);
				List<GroupBean> list = groupService.getSearchAllDB();
				Gson gson = new Gson();
				String jsonStrList = gson.toJson(list);
				response.getWriter().write(jsonStrList);
				return;
			} catch (Exception e) {e.printStackTrace();}
		}else if ("delete".equals(action)) {
			try {
				String group_id = request.getParameter("group_id");
				groupService = new GroupService();
				groupService.deleteGroup(group_id);
				List<GroupBean> list = groupService.getSearchAllDB();
				Gson gson = new Gson();
				String jsonStrList = gson.toJson(list);
				response.getWriter().write(jsonStrList);
				return;
			} catch (Exception e) {e.printStackTrace();}
		}
	}

	/************************* 對應資料庫表格格式 **************************************/
	public class GroupBean implements java.io.Serializable {
		private String  group_id;
		private String  group_name;
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
		
	}

	/*************************** 制定規章方法 ****************************************/
	interface Group_interface {
		public void updateDB(String group_id,String group_name);
		public void deleteDB(String group_id);
		public void insertDB(String group_name);
		public List<GroupBean> searchAllDB();
	}

	/*************************** 處理業務邏輯 ****************************************/
	class GroupService {
		private Group_interface dao;

		public GroupService() {
			dao = new GroupDAO();
		}

		public void updateGroup(String group_id, String group_name) {
			dao.updateDB(group_id,group_name);
		}
		public void insertGroup( String group_name) {
			dao.insertDB(group_name);
		}
		public void deleteGroup(String group_id) {
			dao.deleteDB(group_id);
		}
	
		public List<GroupBean> getSearchAllDB() {
			return dao.searchAllDB();
		}
	}

	/*************************** 操作資料庫 ****************************************/
	class GroupDAO implements Group_interface {
		// 會使用到的Stored procedure	
		private static final String selectall = "select * from tb_group";
		private static final String insert = "insert into tb_group (group_id,group_name) VALUES (UUID(),?)";
		private static final String update = "update tb_group set group_name = ? where group_id = ?";
		private static final String delete = "delete from tb_group where group_id = ?";

		private final String dbURL = "jdbc:mysql://192.168.112.164/cdri"
				+ "?useUnicode=true&characterEncoding=utf-8&useSSL=false";
		private final String dbUserName = getServletConfig().getServletContext().getInitParameter("dbUserName");
		private final String dbPassword = getServletConfig().getServletContext().getInitParameter("dbPassword");
		
		@Override
		public void insertDB(String group_name) {
			// TODO Auto-generated method stub
			Connection con = null;
			PreparedStatement pstmt = null;
			try {
				Class.forName("com.mysql.jdbc.Driver");
				con = DriverManager.getConnection(dbURL, dbUserName, dbPassword);
				pstmt = con.prepareStatement(insert);

				pstmt.setString(1, group_name);
				pstmt.executeUpdate();
				// Handle any SQL errors
			} catch (SQLException se) {
				throw new RuntimeException("A database error occured. " + se.getMessage());
				// Clean up JDBC resources
			} catch (ClassNotFoundException cnfe) {
				throw new RuntimeException("A database error occured. " + cnfe.getMessage());
			} finally {
				if (pstmt != null) {try {pstmt.close();} catch (SQLException se) {se.printStackTrace(System.err);}}
				if (con != null) {try {con.close();} catch (Exception e) {e.printStackTrace(System.err);}}
			}
		}
		
		@Override
		public void deleteDB(String group_id) {
			// TODO Auto-generated method stub
			Connection con = null;
			PreparedStatement pstmt = null;
			try {
				Class.forName("com.mysql.jdbc.Driver");
				con = DriverManager.getConnection(dbURL, dbUserName, dbPassword);
				pstmt = con.prepareStatement(delete);

				pstmt.setString(1, group_id);
				pstmt.executeUpdate();
				// Handle any SQL errors
			} catch (SQLException se) {
				throw new RuntimeException("A database error occured. " + se.getMessage());
				// Clean up JDBC resources
			} catch (ClassNotFoundException cnfe) {
				throw new RuntimeException("A database error occured. " + cnfe.getMessage());
			} finally {
				if (pstmt != null) {try {pstmt.close();} catch (SQLException se) {se.printStackTrace(System.err);}}
				if (con != null) {try {con.close();} catch (Exception e) {e.printStackTrace(System.err);}}
			}
		}

		@Override
		public void updateDB(String group_id,String group_name) {
			// TODO Auto-generated method stub
			Connection con = null;
			PreparedStatement pstmt = null;
			try {
				Class.forName("com.mysql.jdbc.Driver");
				con = DriverManager.getConnection(dbURL, dbUserName, dbPassword);
				pstmt = con.prepareStatement(update);
				//System.out.println(group_name+"  "+group_id);
				pstmt.setString(1, group_name);
				pstmt.setString(2, group_id);
				pstmt.executeUpdate();
				// Handle any SQL errors
			} catch (SQLException se) {
				throw new RuntimeException("A database error occured. " + se.getMessage());
				// Clean up JDBC resources
			} catch (ClassNotFoundException cnfe) {
				throw new RuntimeException("A database error occured. " + cnfe.getMessage());
			} finally {
				if (pstmt != null) {try {pstmt.close();} catch (SQLException se) {se.printStackTrace(System.err);}}
				if (con != null) {try {con.close();} catch (Exception e) {e.printStackTrace(System.err);}}
			}
		}
		
		@Override
		public List<GroupBean> searchAllDB() {
			// TODO Auto-generated method stub
			List<GroupBean> list = new ArrayList<GroupBean>();
			GroupBean groupBean = null;

			Connection con = null;
			PreparedStatement pstmt = null;
			ResultSet rs = null;

			try {
				Class.forName("com.mysql.jdbc.Driver");
				con = DriverManager.getConnection(dbURL, dbUserName, dbPassword);
				pstmt = con.prepareStatement(selectall);
				rs = pstmt.executeQuery();
				while (rs.next()) {
					groupBean = new GroupBean();
					groupBean.setGroup_id(rs.getString("group_id"));
					groupBean.setGroup_name(rs.getString("group_name"));
					list.add(groupBean); // Store the row in the list
				}

				// Handle any driver errors
			} catch (SQLException se) {
				throw new RuntimeException("A database error occured. " + se.getMessage());
				// Clean up JDBC resources
			} catch (ClassNotFoundException cnfe) {
				throw new RuntimeException("A database error occured. " + cnfe.getMessage());
			} finally {
				if (pstmt != null) {try {pstmt.close();} catch (SQLException se) {se.printStackTrace(System.err);}}
				if (con != null) {try {con.close();} catch (Exception e) {e.printStackTrace(System.err);}}
			}
			return list;
		}

	}

}
