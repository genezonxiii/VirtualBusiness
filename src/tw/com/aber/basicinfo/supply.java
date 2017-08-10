package tw.com.aber.basicinfo;

import java.io.IOException;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import tw.com.aber.vo.PurchaseDetailVO;
import tw.com.aber.vo.PurchaseVO;
import tw.com.aber.vo.SupplyVO;


public class supply extends HttpServlet {
	private static final long serialVersionUID = 1L;
    
	private static final Logger logger = LogManager.getLogger(supply.class);

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
		
		logger.debug("action: "+action);
		logger.debug("group_id: "+group_id);
		logger.debug("user_id: "+user_id);
		
		if ("search".equals(action)) {
			try {
				/*************************** 1.接收請求參數 ****************************************/
				String supply_name = request.getParameter("supply_name");
				logger.debug("supply_name: "+supply_name);
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
					logger.debug(jsonStrList);
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
				
				logger.debug("supply_name: "+supply_name);
				logger.debug("supply_unicode: "+supply_unicode);
				logger.debug("address: "+address);
				logger.debug("contact: "+contact);
				logger.debug("phone: "+phone);
				logger.debug("ext: "+ext);
				logger.debug("mobile: "+mobile);
				logger.debug("contact1: "+contact1);
				logger.debug("phone1: "+phone1);
				logger.debug("ext1: "+ext1);
				logger.debug("email: "+email);
				logger.debug("email1: "+email1);
				logger.debug("mobile1: "+mobile1);
				logger.debug("memo: "+memo);
				
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
				
				logger.debug("supply_id: "+supply_id);
				logger.debug("supply_name: "+supply_name);
				logger.debug("supply_unicode: "+supply_unicode);
				logger.debug("address: "+address);
				logger.debug("contact: "+contact);
				logger.debug("phone: "+phone);
				logger.debug("ext: "+ext);
				logger.debug("mobile: "+mobile);
				logger.debug("contact1: "+contact1);
				logger.debug("phone1: "+phone1);
				logger.debug("ext1: "+ext1);
				logger.debug("email: "+email);
				logger.debug("email1: "+email1);
				logger.debug("mobile1: "+mobile1);
				logger.debug("memo: "+memo);
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
				logger.debug("supply_id: "+supply_id);
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
		
		if("getPurchaseRecordBySupplyId".equals(action)){
			try {
				String supply_id = request.getParameter("supply_id");
				logger.debug("supply_id: "+supply_id);
				
				supplyVOService = new SupplyVOService();
				List<PurchaseVO> list=supplyVOService.getPurchaseRecordBySupplyId(group_id,supply_id);

				Gson gson =  new GsonBuilder().setDateFormat("yyyy-MM-dd").create();
				String jsonStrList = gson.toJson(list);
				response.getWriter().write(jsonStrList);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		
		// 採購明細
		if ("select_all_purchasedetail".equals(action)) {
			String purchase_id = request.getParameter("purchase_id");
			logger.debug("purchase_id: "+purchase_id);
			
			supplyVOService = new SupplyVOService();
			List<PurchaseDetailVO> list = supplyVOService.getSearchAllPurchaseDetail(purchase_id);

			Gson gson =  new GsonBuilder().setDateFormat("yyyy-MM-dd").create();
			String jsonStrList = gson.toJson(list);
			response.getWriter().write(jsonStrList);
			return;// 程式中斷
		}
	}

	/*************************** 制定規章方法 ****************************************/
	interface SupplyVO_interface {

		public void insertDB(SupplyVO supply);

		List<PurchaseDetailVO> searchAllPurchaseDetail(String purchase_id);

		public void updateDB(SupplyVO supply);

		public void deleteDB(String supply_id,String user_id);

		public List<SupplyVO> searchAllDB(String group_id);
		
		public List<PurchaseVO> getPurchaseRecordBySupplyId(String group_id,String supply_id);
		
	}

	/*************************** 處理業務邏輯 ****************************************/
	class SupplyVOService {
		private SupplyVO_interface dao;

		public SupplyVOService() {
			dao = new SupplyDAO();
		}
		public List<PurchaseVO> getPurchaseRecordBySupplyId(String group_id,String supply_id){
			return dao.getPurchaseRecordBySupplyId(group_id, supply_id);
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
		public List<PurchaseDetailVO> getSearchAllPurchaseDetail(String purchase_id) {
			return dao.searchAllPurchaseDetail(purchase_id);
		}
		
	}

	/*************************** 操作資料庫 ****************************************/
	class SupplyDAO implements SupplyVO_interface {
		// 會使用到的Stored procedure
		private static final String sp_insert_supply = "call sp_insert_supply(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
		private static final String sp_selectall_supply = "call sp_selectall_supply (?)";
		private static final String sp_del_supply = "call sp_del_supply (?,?)";
		private static final String sp_update_supply = "call sp_update_supply (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
		private static final String sp_get_purchase_record_by_supply_id = "call sp_get_purchase_record_by_supply_id (?,?)";
		private static final String sp_selectall_purchasedetail = "call sp_selectall_purchasedetail(?)";


		private final String dbURL = getServletConfig().getServletContext().getInitParameter("dbURL")
				+ "?useUnicode=true&characterEncoding=utf-8&useSSL=false";
		private final String dbUserName = getServletConfig().getServletContext().getInitParameter("dbUserName");
		private final String dbPassword = getServletConfig().getServletContext().getInitParameter("dbPassword");

		@Override
		public void insertDB(SupplyVO supplyVO) {
			
			Connection con = null;
			CallableStatement cs = null;
			String result = null;
			try {
				Class.forName("com.mysql.jdbc.Driver");
				con = DriverManager.getConnection(dbURL, dbUserName, dbPassword);
				cs = con.prepareCall(sp_insert_supply);

				cs.setString(1, supplyVO.getGroup_id());
				cs.setString(2, supplyVO.getSupply_name());
				cs.setString(3, supplyVO.getSupply_unicode());
				cs.setString(4, supplyVO.getAddress());
				cs.setString(5, supplyVO.getContact());
				cs.setString(6, supplyVO.getPhone());
				cs.setString(7, supplyVO.getExt());
				cs.setString(8, supplyVO.getMobile());
				cs.setString(9, supplyVO.getContact1());
				cs.setString(10, supplyVO.getPhone1());
				cs.setString(11, supplyVO.getExt1());
				cs.setString(12, supplyVO.getMobile1());
				cs.setString(13, supplyVO.getEmail());
				cs.setString(14, supplyVO.getEmail1());
				cs.setString(15, supplyVO.getMemo());
				cs.setString(16, supplyVO.getUser_id());
				cs.registerOutParameter(17, Types.BOOLEAN);
				cs.execute();
				
				result = cs.getString(17);

				// Handle any SQL errors
			} catch (SQLException se) {
				throw new RuntimeException("A database error occured. " + se.getMessage());
				// Clean up JDBC resources
			} catch (ClassNotFoundException cnfe) {
				throw new RuntimeException("A database error occured. " + cnfe.getMessage());
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

		@Override
		public List<PurchaseVO> getPurchaseRecordBySupplyId(String group_id, String supply_id) {

			List<PurchaseVO> list = new ArrayList<PurchaseVO>();
			PurchaseVO purchaseVO = null;

			Connection con = null;
			PreparedStatement pstmt = null;
			ResultSet rs = null;

			try {
				Class.forName("com.mysql.jdbc.Driver");
				con = DriverManager.getConnection(dbURL, dbUserName, dbPassword);
				pstmt = con.prepareStatement(sp_get_purchase_record_by_supply_id);
				pstmt.setString(1, group_id);
				pstmt.setString(2, supply_id);
				rs = pstmt.executeQuery();
				while (rs.next()) {
					purchaseVO = new PurchaseVO();
					purchaseVO.setPurchase_id(rs.getString("purchase_id"));
					purchaseVO.setSeq_no(rs.getString("seq_no"));
					purchaseVO.setGroup_id(rs.getString("group_id"));
					purchaseVO.setUser_id(rs.getString("user_id"));
					purchaseVO.setSupply_id(rs.getString("supply_id"));
					purchaseVO.setMemo(rs.getString("memo"));
					purchaseVO.setPurchase_date(rs.getDate("purchase_date"));
					purchaseVO.setInvoice(rs.getString("invoice"));
					purchaseVO.setInvoice_type(rs.getString("invoice_type"));
					purchaseVO.setAmount(rs.getFloat("amount"));
					purchaseVO.setReturn_date(rs.getDate("return_date"));
					purchaseVO.setIsreturn(rs.getBoolean("isreturn"));
					purchaseVO.setAccept_flag(rs.getBoolean("accept_flag"));
					purchaseVO.setV_supply_name(rs.getString("supply_name"));
					
					list.add(purchaseVO); // Store the row in the list
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
		public List<PurchaseDetailVO> searchAllPurchaseDetail(String purchase_id) {

			List<PurchaseDetailVO> list = new ArrayList<PurchaseDetailVO>();
			PurchaseDetailVO purchaseDetailVO = null;

			Connection con = null;
			PreparedStatement pstmt = null;
			ResultSet rs = null;

			try {
				Class.forName("com.mysql.jdbc.Driver");
				con = DriverManager.getConnection(dbURL, dbUserName, dbPassword);
				pstmt = con.prepareStatement(sp_selectall_purchasedetail);
				pstmt.setString(1, purchase_id);
				rs = pstmt.executeQuery();
				while (rs.next()) {
					purchaseDetailVO = new PurchaseDetailVO();
					purchaseDetailVO.setPurchaseDetail_id(rs.getString("purchaseDetail_id"));
					purchaseDetailVO.setPurchase_id(rs.getString("purchase_id"));
					purchaseDetailVO.setGroup_id(rs.getString("group_id"));
					purchaseDetailVO.setUser_id(rs.getString("user_id"));
					purchaseDetailVO.setProduct_id(rs.getString("product_id"));
					purchaseDetailVO.setC_product_id(rs.getString("c_product_id"));
					purchaseDetailVO.setProduct_name(rs.getString("product_name"));
					purchaseDetailVO.setQuantity(rs.getInt("quantity"));
					purchaseDetailVO.setCost(rs.getFloat("cost"));
					purchaseDetailVO.setMemo(rs.getString("memo"));
					purchaseDetailVO.setReturn_date(rs.getDate("return_date"));
					purchaseDetailVO.setIsreturn(rs.getBoolean("isreturn"));
					list.add(purchaseDetailVO);
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

}
