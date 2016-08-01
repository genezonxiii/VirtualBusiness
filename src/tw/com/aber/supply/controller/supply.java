package tw.com.aber.supply.controller;

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
public class supply extends HttpServlet {

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doPost(request, response);
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		request.setCharacterEncoding("UTF-8");
		response.setCharacterEncoding("UTF-8");
		SupplyBeanService supplyBeanService = null;
		String action = request.getParameter("action");
		String group_id = request.getSession().getAttribute("group_id").toString();
		String user_id = request.getSession().getAttribute("user_id").toString();
		if ("search".equals(action)) {
			try {
				/*************************** 1.接收請求參數 ****************************************/
				String supply_name = request.getParameter("supply_name");
				/*************************** 2.開始查詢資料 ****************************************/
				// 假如無查詢條件，則是查詢全部
				if (supply_name == null || (supply_name.trim()).length() == 0) {
					supplyBeanService = new SupplyBeanService();
					List<SupplyBean> list = supplyBeanService.getSearchAllDB(group_id);
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
				String supply_name = request.getParameter("supply_name");
				String supply_unicode = request.getParameter("supply_unicode");
				String address = request.getParameter("address");
				String contact = request.getParameter("contact");
				String phone = request.getParameter("phone");
				String ext = request.getParameter("ext");
				String mobile = request.getParameter("mobile");
				String contact1 = request.getParameter("contact1");
				String phone1 = request.getParameter("phone1");
				String ext1 = request.getParameter("ext1");
				String mobile1 = request.getParameter("mobile1");
				String memo = request.getParameter("memo");
				
				/*************************** 2.開始新增資料 ***************************************/
				supplyBeanService = new SupplyBeanService();
				supplyBeanService.addSupply(group_id, supply_name, supply_unicode, address, contact,phone, ext, mobile,
						contact1, phone1, ext1,  mobile1, memo, user_id);
				/*************************** 3.新增完成,準備轉交(Send the Success view) ***********/
				supplyBeanService = new SupplyBeanService();
				List<SupplyBean> list = supplyBeanService.getSearchAllDB(group_id);
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
				String supply_id = request.getParameter("supply_id");
				String supply_name = request.getParameter("supply_name");
				String supply_unicode = request.getParameter("supply_unicode");
				String address = request.getParameter("address");
				String contact = request.getParameter("contact");
				String phone = request.getParameter("phone");
				String ext = request.getParameter("ext");
				String mobile = request.getParameter("mobile");
				String contact1 = request.getParameter("contact1");
				String phone1 = request.getParameter("phone1");
				String ext1 = request.getParameter("ext1");
				String mobile1 = request.getParameter("mobile1");
				String memo = request.getParameter("memo");
				/*************************** 2.開始修改資料 ***************************************/
				supplyBeanService = new SupplyBeanService();
				supplyBeanService.updateSupply(supply_id, group_id, supply_name, supply_unicode, address, contact, phone,
						ext, mobile, contact1, phone1, ext1, mobile1, memo, user_id);
				/*************************** 3.修改完成,準備轉交(Send the Success view) ***********/
				supplyBeanService = new SupplyBeanService();
				List<SupplyBean> list = supplyBeanService.getSearchAllDB(group_id);
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
				String supply_id = request.getParameter("supply_id");
				/*************************** 2.開始刪除資料 ***************************************/
				supplyBeanService = new SupplyBeanService();
				supplyBeanService.deleteSupply(supply_id,user_id);
				/*************************** 3.刪除完成,準備轉交(Send the Success view) ***********/
				supplyBeanService = new SupplyBeanService();
				List<SupplyBean> list = supplyBeanService.getSearchAllDB(group_id);
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
	public class SupplyBean implements java.io.Serializable {
		private String  supply_id;
		private String  group_id;
		private String  supply_name;
		private String  supply_unicode;
		private String  address;
		private String  contact;
		private String  phone;
		private String  ext;
		private String  mobile;
		private String  contact1;
		private String  phone1;
		private String  ext1;
		private String  mobile1;
		private String  memo;
		private String  user_id;
		public String getSupply_id() {
			return supply_id;
		}
		public void setSupply_id(String supply_id) {
			this.supply_id = supply_id;
		}
		public String getGroup_id() {
			return group_id;
		}
		public void setGroup_id(String group_id) {
			this.group_id = group_id;
		}
		public String getSupply_name() {
			return supply_name;
		}
		public void setSupply_name(String supply_name) {
			this.supply_name = supply_name;
		}
		public String getSupply_unicode() {
			return supply_unicode;
		}
		public void setSupply_unicode(String supply_unicode) {
			this.supply_unicode = supply_unicode;
		}
		public String getAddress() {
			return address;
		}
		public void setAddress(String address) {
			this.address = address;
		}
		public String getContact() {
			return contact;
		}
		public void setContact(String contact) {
			this.contact = contact;
		}
		public String getPhone() {
			return phone;
		}
		public void setPhone(String phone) {
			this.phone = phone;
		}
		public String getExt() {
			return ext;
		}
		public void setExt(String ext) {
			this.ext = ext;
		}
		public String getMobile() {
			return mobile;
		}
		public void setMobile(String mobile) {
			this.mobile = mobile;
		}
		public String getContact1() {
			return contact1;
		}
		public void setContact1(String contact1) {
			this.contact1 = contact1;
		}
		public String getPhone1() {
			return phone1;
		}
		public void setPhone1(String phone1) {
			this.phone1 = phone1;
		}
		public String getExt1() {
			return ext1;
		}
		public void setExt1(String ext1) {
			this.ext1 = ext1;
		}
		public String getMobile1() {
			return mobile1;
		}
		public void setMobile1(String mobile1) {
			this.mobile1 = mobile1;
		}
		public String getMemo() {
			return memo;
		}
		public void setMemo(String memo) {
			this.memo = memo;
		}
		public String getUser_id() {
			return user_id;
		}
		public void setUser_id(String user_id) {
			this.user_id = user_id;
		}
		
		
		
	}

	/*************************** 制定規章方法 ****************************************/
	interface SupplyBean_interface {

		public void insertDB(SupplyBean supply);

		public void updateDB(SupplyBean supply);

		public void deleteDB(String supply_id,String user_id);

		public List<SupplyBean> searchAllDB(String group_id);
	}

	/*************************** 處理業務邏輯 ****************************************/
	class SupplyBeanService {
		private SupplyBean_interface dao;

		public SupplyBeanService() {
			dao = new SupplyDAO();
		}

		public SupplyBean addSupply(String group_id, String supply_name,String supply_unicode,String address,String contact,String phone,
				String ext,String mobile,String contact1,String phone1,String ext1,String mobile1,String memo,String user_id) {
			SupplyBean supplybean = new SupplyBean();
			supplybean.setGroup_id(group_id);
			supplybean.setSupply_name(supply_name);
			supplybean.setSupply_unicode(supply_unicode);
			supplybean.setAddress(address);
			supplybean.setContact(contact);
			supplybean.setPhone(phone);
			supplybean.setExt(ext);
			supplybean.setMobile(mobile);
			supplybean.setContact1(contact1);
			supplybean.setPhone1(phone1);
			supplybean.setExt1(ext1);
			supplybean.setMobile1(mobile1);
			supplybean.setMemo(memo);
			supplybean.setUser_id(user_id);
			
			dao.insertDB(supplybean);
			return supplybean;
		}

		public SupplyBean updateSupply(String supply_id,String group_id, String supply_name,String supply_unicode,String address,String contact,String phone,
				String ext,String mobile,String contact1,String phone1,String ext1,String mobile1,String memo,String user_id) {
			SupplyBean supplybean = new SupplyBean();
			supplybean.setSupply_id(supply_id);
			supplybean.setGroup_id(group_id);
			supplybean.setSupply_name(supply_name);
			supplybean.setSupply_unicode(supply_unicode);
			supplybean.setAddress(address);
			supplybean.setContact(contact);
			supplybean.setPhone(phone);
			supplybean.setExt(ext);
			supplybean.setMobile(mobile);
			supplybean.setContact1(contact1);
			supplybean.setPhone1(phone1);
			supplybean.setExt1(ext1);
			supplybean.setMobile1(mobile1);
			supplybean.setMemo(memo);
			supplybean.setUser_id(user_id);
			
			dao.updateDB(supplybean);
			return supplybean;
		}

		public void deleteSupply(String supply_id,String user_id) {
			dao.deleteDB(supply_id,user_id);
		}
		public List<SupplyBean> getSearchAllDB(String group_id) {
			return dao.searchAllDB(group_id);
		}
	}

	/*************************** 操作資料庫 ****************************************/
	class SupplyDAO implements SupplyBean_interface {
		// 會使用到的Stored procedure
		private static final String sp_insert_supply = "call sp_insert_supply(?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
		private static final String sp_selectall_supply = "call sp_selectall_supply (?)";
		private static final String sp_del_supply = "call sp_del_supply (?,?)";
		private static final String sp_update_supply = "call sp_update_supply (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";

		private final String dbURL = getServletConfig().getServletContext().getInitParameter("dbURL");
		private final String dbUserName = getServletConfig().getServletContext().getInitParameter("dbUserName");
		private final String dbPassword = getServletConfig().getServletContext().getInitParameter("dbPassword");

		@Override
		public void insertDB(SupplyBean supplyBean) {
			// TODO Auto-generated method stub
			Connection con = null;
			PreparedStatement pstmt = null;
			try {
				Class.forName("com.mysql.jdbc.Driver");
				con = DriverManager.getConnection(dbURL, dbUserName, dbPassword);
				pstmt = con.prepareStatement(sp_insert_supply);

				pstmt.setString(1, supplyBean.getGroup_id());
				pstmt.setString(2, supplyBean.getSupply_name());
				pstmt.setString(3, supplyBean.getSupply_unicode());
				pstmt.setString(4, supplyBean.getAddress());
				pstmt.setString(5, supplyBean.getContact());
				pstmt.setString(6, supplyBean.getPhone());
				pstmt.setString(7, supplyBean.getExt());
				pstmt.setString(8, supplyBean.getMobile());
				pstmt.setString(9, supplyBean.getContact1());
				pstmt.setString(10, supplyBean.getPhone1());
				pstmt.setString(11, supplyBean.getExt1());
				pstmt.setString(12, supplyBean.getMobile1());
				pstmt.setString(13, supplyBean.getMemo());
				pstmt.setString(14, supplyBean.getUser_id());
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
		public void updateDB(SupplyBean supplyBean) {
			// TODO Auto-generated method stub
			Connection con = null;
			PreparedStatement pstmt = null;
			try {
				Class.forName("com.mysql.jdbc.Driver");
				con = DriverManager.getConnection(dbURL, dbUserName, dbPassword);
				pstmt = con.prepareStatement(sp_update_supply);
				
				pstmt.setString(1, supplyBean.getSupply_id());
				pstmt.setString(2, supplyBean.getGroup_id());
				pstmt.setString(3, supplyBean.getSupply_name());
				pstmt.setString(4, supplyBean.getSupply_unicode());
				pstmt.setString(5, supplyBean.getAddress());
				pstmt.setString(6, supplyBean.getContact());
				pstmt.setString(7, supplyBean.getPhone());
				pstmt.setString(8, supplyBean.getExt());
				pstmt.setString(9, supplyBean.getMobile());
				pstmt.setString(10, supplyBean.getContact1());
				pstmt.setString(11, supplyBean.getPhone1());
				pstmt.setString(12, supplyBean.getExt1());
				pstmt.setString(13, supplyBean.getMobile1());
				pstmt.setString(14, supplyBean.getMemo());
				pstmt.setString(15, supplyBean.getUser_id());

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
		public void deleteDB(String supply_id,String user_id) {
			// TODO Auto-generated method stub
			Connection con = null;
			PreparedStatement pstmt = null;
			try {
				Class.forName("com.mysql.jdbc.Driver");
				con = DriverManager.getConnection(dbURL, dbUserName, dbPassword);
				pstmt = con.prepareStatement(sp_del_supply);
				pstmt.setString(1, supply_id);
				pstmt.setString(2, user_id);

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
		public List<SupplyBean> searchAllDB(String group_id) {
			// TODO Auto-generated method stub
			List<SupplyBean> list = new ArrayList<SupplyBean>();
			SupplyBean supplyBean = null;

			Connection con = null;
			PreparedStatement pstmt = null;
			ResultSet rs = null;

			try {
				Class.forName("com.mysql.jdbc.Driver");
				con = DriverManager.getConnection(dbURL, dbUserName, dbPassword);
				pstmt = con.prepareStatement(sp_selectall_supply);
				pstmt.setString(1, group_id);
				rs = pstmt.executeQuery();
				while (rs.next()) {
					supplyBean = new SupplyBean();
					supplyBean.setSupply_id(rs.getString("supply_id"));
					supplyBean.setGroup_id(rs.getString("group_id"));
					supplyBean.setSupply_name(rs.getString("supply_name"));
					supplyBean.setSupply_unicode(rs.getString("supply_unicode"));
					supplyBean.setAddress(rs.getString("address"));
					supplyBean.setContact(rs.getString("contact"));
					supplyBean.setPhone(rs.getString("phone"));
					supplyBean.setExt(rs.getString("ext"));
					supplyBean.setMobile(rs.getString("mobile"));
					supplyBean.setContact1(rs.getString("contact1"));
					supplyBean.setPhone1(rs.getString("phone1"));
					supplyBean.setExt1(rs.getString("ext1"));
					supplyBean.setMobile1(rs.getString("mobile1"));
					supplyBean.setMemo(rs.getString("memo"));
					
					list.add(supplyBean); // Store the row in the list
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
