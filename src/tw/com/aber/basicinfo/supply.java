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

import tw.com.aber.vo.SupplyVO;


public class supply extends HttpServlet {

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		doPost(request, response);
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		request.setCharacterEncoding("UTF-8");
		response.setCharacterEncoding("UTF-8");
		SupplyVOService supplyVOService = null;
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
					supplyVOService = new SupplyVOService();
					List<SupplyVO> list = supplyVOService.getSearchAllDB(group_id);
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
				String email = request.getParameter("email");
				String email1 = request.getParameter("email1");
				String mobile1 = request.getParameter("mobile1");
				String memo = request.getParameter("memo");
				
				/*************************** 2.開始新增資料 ***************************************/
				supplyVOService = new SupplyVOService();
				supplyVOService.addSupply(group_id, supply_name, supply_unicode, address, contact,phone, ext, mobile,
						contact1, phone1, ext1,  mobile1,email,email1, memo, user_id);
				/*************************** 3.新增完成,準備轉交(Send the Success view) ***********/
				supplyVOService = new SupplyVOService();
				List<SupplyVO> list = supplyVOService.getSearchAllDB(group_id);
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
				String email = request.getParameter("email");
				String email1 = request.getParameter("email1");
				String memo = request.getParameter("memo");
				/*************************** 2.開始修改資料 ***************************************/
				supplyVOService = new SupplyVOService();
				supplyVOService.updateSupply(supply_id, group_id, supply_name, supply_unicode, address, contact, phone,
						ext, mobile, contact1, phone1, ext1, mobile1,email,email1, memo, user_id);
				/*************************** 3.修改完成,準備轉交(Send the Success view) ***********/
				supplyVOService = new SupplyVOService();
				List<SupplyVO> list = supplyVOService.getSearchAllDB(group_id);
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
				supplyVOService = new SupplyVOService();
				supplyVOService.deleteSupply(supply_id,user_id);
				/*************************** 3.刪除完成,準備轉交(Send the Success view) ***********/
				supplyVOService = new SupplyVOService();
				List<SupplyVO> list = supplyVOService.getSearchAllDB(group_id);
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
	interface SupplyVO_interface {

		public void insertDB(SupplyVO supply);

		public void updateDB(SupplyVO supply);

		public void deleteDB(String supply_id,String user_id);

		public List<SupplyVO> searchAllDB(String group_id);
		
	}

	/*************************** 處理業務邏輯 ****************************************/
	class SupplyVOService {
		private SupplyVO_interface dao;

		public SupplyVOService() {
			dao = new SupplyDAO();
		}

		public SupplyVO addSupply(String group_id, String supply_name,String supply_unicode,String address,String contact,String phone,
				String ext,String mobile,String contact1,String phone1,String ext1,String mobile1,String email,String email1,String memo,String user_id) {
			SupplyVO supplybean = new SupplyVO();
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
			supplybean.setEmail(email);
			supplybean.setEmail1(email1);
			supplybean.setMemo(memo);
			supplybean.setUser_id(user_id);
			
			dao.insertDB(supplybean);
			return supplybean;
		}

		public SupplyVO updateSupply(String supply_id,String group_id, String supply_name,String supply_unicode,String address,String contact,String phone,
				String ext,String mobile,String contact1,String phone1,String ext1,String mobile1,String email,String email1,String memo,String user_id) {
			SupplyVO supplybean = new SupplyVO();
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
			supplybean.setEmail(email);
			supplybean.setEmail1(email1);
			
			supplybean.setMemo(memo);
			supplybean.setUser_id(user_id);
			
			dao.updateDB(supplybean);
			return supplybean;
		}

		public void deleteSupply(String supply_id,String user_id) {
			dao.deleteDB(supply_id,user_id);
		}
		public List<SupplyVO> getSearchAllDB(String group_id) {
			return dao.searchAllDB(group_id);
		}
		
	}

	/*************************** 操作資料庫 ****************************************/
	class SupplyDAO implements SupplyVO_interface {
		// 會使用到的Stored procedure
		private static final String sp_insert_supply = "call sp_insert_supply(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
		private static final String sp_selectall_supply = "call sp_selectall_supply (?)";
		private static final String sp_del_supply = "call sp_del_supply (?,?)";
		private static final String sp_update_supply = "call sp_update_supply (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
		
		private final String dbURL = getServletConfig().getServletContext().getInitParameter("dbURL")
				+ "?useUnicode=true&characterEncoding=utf-8&useSSL=false";
		private final String dbUserName = getServletConfig().getServletContext().getInitParameter("dbUserName");
		private final String dbPassword = getServletConfig().getServletContext().getInitParameter("dbPassword");

		@Override
		public void insertDB(SupplyVO supplyVO) {
			
			Connection con = null;
			PreparedStatement pstmt = null;
			try {
				Class.forName("com.mysql.jdbc.Driver");
				con = DriverManager.getConnection(dbURL, dbUserName, dbPassword);
				pstmt = con.prepareStatement(sp_insert_supply);

				pstmt.setString(1, supplyVO.getGroup_id());
				pstmt.setString(2, supplyVO.getSupply_name());
				pstmt.setString(3, supplyVO.getSupply_unicode());
				pstmt.setString(4, supplyVO.getAddress());
				pstmt.setString(5, supplyVO.getContact());
				pstmt.setString(6, supplyVO.getPhone());
				pstmt.setString(7, supplyVO.getExt());
				pstmt.setString(8, supplyVO.getMobile());
				pstmt.setString(9, supplyVO.getContact1());
				pstmt.setString(10, supplyVO.getPhone1());
				pstmt.setString(11, supplyVO.getExt1());
				pstmt.setString(12, supplyVO.getMobile1());
				pstmt.setString(13, supplyVO.getEmail());
				pstmt.setString(14, supplyVO.getEmail1());
				pstmt.setString(15, supplyVO.getMemo());
				pstmt.setString(16, supplyVO.getUser_id());
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
		public void updateDB(SupplyVO supplyVO) {
			
			Connection con = null;
			PreparedStatement pstmt = null;
			try {
				Class.forName("com.mysql.jdbc.Driver");
				con = DriverManager.getConnection(dbURL, dbUserName, dbPassword);
				pstmt = con.prepareStatement(sp_update_supply);
				
				pstmt.setString(1, supplyVO.getSupply_id());
				pstmt.setString(2, supplyVO.getGroup_id());
				pstmt.setString(3, supplyVO.getSupply_name());
				pstmt.setString(4, supplyVO.getSupply_unicode());
				pstmt.setString(5, supplyVO.getAddress());
				pstmt.setString(6, supplyVO.getContact());
				pstmt.setString(7, supplyVO.getPhone());
				pstmt.setString(8, supplyVO.getExt());
				pstmt.setString(9, supplyVO.getMobile());
				pstmt.setString(10, supplyVO.getContact1());
				pstmt.setString(11, supplyVO.getPhone1());
				pstmt.setString(12, supplyVO.getExt1());
				pstmt.setString(13, supplyVO.getMobile1());
				pstmt.setString(14, supplyVO.getEmail());
				pstmt.setString(15, supplyVO.getEmail1());
				pstmt.setString(16, supplyVO.getMemo());
				pstmt.setString(17, supplyVO.getUser_id());

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
		public List<SupplyVO> searchAllDB(String group_id) {
			
			List<SupplyVO> list = new ArrayList<SupplyVO>();
			SupplyVO supplyVO = null;

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
					supplyVO = new SupplyVO();
					supplyVO.setSupply_id(rs.getString("supply_id"));
					supplyVO.setGroup_id(rs.getString("group_id"));
					supplyVO.setSupply_name(rs.getString("supply_name"));
					supplyVO.setSupply_unicode(rs.getString("supply_unicode"));
					supplyVO.setAddress(rs.getString("address"));
					supplyVO.setContact(rs.getString("contact"));
					supplyVO.setPhone(rs.getString("phone"));
					supplyVO.setExt(rs.getString("ext"));
					supplyVO.setMobile(rs.getString("mobile"));
					supplyVO.setEmail(rs.getString("email"));
					supplyVO.setContact1(rs.getString("contact1"));
					supplyVO.setPhone1(rs.getString("phone1"));
					supplyVO.setExt1(rs.getString("ext1"));
					supplyVO.setMobile1(rs.getString("mobile1"));
					supplyVO.setEmail1(rs.getString("email1"));
					supplyVO.setMemo(rs.getString("memo"));
					
					list.add(supplyVO); // Store the row in the list
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
