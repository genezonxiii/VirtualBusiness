package tw.com.aber.basicinfo;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.gson.Gson;

import tw.com.aber.vo.GroupVO;

public class group extends HttpServlet {

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		doPost(request, response);
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
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
					List<GroupVO> list = groupService.getSearchAllDB(group_id);
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
				String invoice_path = request.getParameter("invoice_path");
				//System.out.println(invoice_path.length());
				/*************************** 2.開始修改資料 ***************************************/
				groupService = new GroupService();
				groupService.updateGroup(group_id, group_name, group_unicode, address, phone, fax, mobile, email, master, user_id,invoice_path);
				/*************************** 3.修改完成,準備轉交(Send the Success view) ***********/
				groupService = new GroupService();
				List<GroupVO> list = groupService.getSearchAllDB(group_id);
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
	interface Group_interface {
		public void updateDB(GroupVO groupVO);
		
		public List<GroupVO> searchAllDB(String group_id);
	}

	/*************************** 處理業務邏輯 ****************************************/
	class GroupService {
		private Group_interface dao;

		public GroupService() {
			dao = new GroupDAO();
		}

		

		public GroupVO updateGroup(String group_id, String group_name, String group_unicode,
				String address,String phone,String fax,String mobile,String email,String master,String user_id,String invoice_path) {
			GroupVO groupVO = new GroupVO();
			groupVO.setGroup_id(group_id);
			groupVO.setGroup_name(group_name);
			groupVO.setGroup_unicode(group_unicode);
			groupVO.setAddress(address);
			groupVO.setPhone(phone);
			groupVO.setFax(fax);
			groupVO.setMobile(mobile);
			groupVO.setEmail(email);
			groupVO.setMaster(master);
			groupVO.setUser_id(user_id);
			groupVO.setInvoice_path(invoice_path);
			dao.updateDB(groupVO);
			return groupVO;
		}

	
		public List<GroupVO> getSearchAllDB(String group_id) {
			return dao.searchAllDB(group_id);
		}
	}

	/*************************** 操作資料庫 ****************************************/
	class GroupDAO implements Group_interface {
		// 會使用到的Stored procedure	
		private static final String sp_selectall_group = "call sp_selectall_group(?)";
		private static final String sp_update_group = "call sp_update_group(?,?,?,?,?,?,?,?,?,?,?)";

		private final String dbURL = getServletConfig().getServletContext().getInitParameter("dbURL")
				+ "?useUnicode=true&characterEncoding=utf-8&useSSL=false";
		private final String dbUserName = getServletConfig().getServletContext().getInitParameter("dbUserName");
		private final String dbPassword = getServletConfig().getServletContext().getInitParameter("dbPassword");

		@Override
		public void updateDB(GroupVO groupVO) {
			
			Connection con = null;
			PreparedStatement pstmt = null;
			try {
				Class.forName("com.mysql.jdbc.Driver");
				con = DriverManager.getConnection(dbURL, dbUserName, dbPassword);
				pstmt = con.prepareStatement(sp_update_group);

				pstmt.setString(1, groupVO.getGroup_id());
				pstmt.setString(2, groupVO.getGroup_name());
				pstmt.setString(3, groupVO.getGroup_unicode());
				pstmt.setString(4, groupVO.getAddress());
				pstmt.setString(5, groupVO.getPhone());
				pstmt.setString(6, groupVO.getFax());
				pstmt.setString(7, groupVO.getMobile());
				pstmt.setString(8, groupVO.getEmail());
				pstmt.setString(9, groupVO.getMaster());
				pstmt.setString(10, groupVO.getUser_id());
				pstmt.setString(11, groupVO.getInvoice_path());
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
		public List<GroupVO> searchAllDB(String group_id) {
			
			List<GroupVO> list = new ArrayList<GroupVO>();
			GroupVO groupVO = null;

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
					groupVO = new GroupVO();
					groupVO.setGroup_id(rs.getString("group_id"));
					groupVO.setGroup_name(rs.getString("group_name"));
					groupVO.setGroup_unicode(rs.getString("group_unicode"));
					groupVO.setAddress(rs.getString("address"));
					groupVO.setPhone(rs.getString("phone"));
					groupVO.setFax(rs.getString("fax"));
					groupVO.setMobile(rs.getString("mobile"));
					groupVO.setEmail(rs.getString("email"));
					groupVO.setMaster(rs.getString("master"));
					groupVO.setInvoice_path(rs.getString("invoice_path"));
					
					list.add(groupVO); // Store the row in the list
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
