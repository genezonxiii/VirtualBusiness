package tw.com.aber.purchreturn.controller;

import java.io.IOException;
import java.sql.Connection;
import java.sql.Date;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import tw.com.aber.vo.PurchaseReturnVO;
import tw.com.aber.vo.SupplyVO;

public class purchreturn extends HttpServlet {
	private static final long serialVersionUID = 1L;

	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		request.setCharacterEncoding("UTF-8");
		response.setCharacterEncoding("UTF-8");
		PurchaseReturnService purchaseReturnService = null;
		String action = request.getParameter("action");
		String group_id = request.getSession().getAttribute("group_id").toString();
		String user_id = request.getSession().getAttribute("user_id").toString();
		if ("search_supply_name".equals(action)) {
			String supply_name = request.getParameter("term");
			purchaseReturnService = new PurchaseReturnService();
			List<SupplyVO> list = purchaseReturnService.getSupplyname(group_id, supply_name);
			Gson gson = new Gson();
			String jsonStrList = gson.toJson(list);
			response.getWriter().write(jsonStrList);
			return;// 程式中斷
		}
		if ("search".equals(action)) {
			try {
				/***************************
				 * 1.接收請求參數-格式檢查
				 ****************************************/
				String supply_name = request.getParameter("supply_name");
				/***************************
				 * 2.開始查詢資料
				 ****************************************/
				// 假如無查詢條件，則是查詢全部
				if (supply_name == null || (supply_name.trim()).length() == 0) {
					purchaseReturnService = new PurchaseReturnService();
					List<PurchaseReturnVO> list = purchaseReturnService.getSearchAllDB(group_id);
					Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd").create();
					String jsonStrList = gson.toJson(list);
					response.getWriter().write(jsonStrList);
					return;// 程式中斷
				}
				// 查詢指定名稱
				if (supply_name != null || supply_name.trim().length() != 0) {
					purchaseReturnService = new PurchaseReturnService();
					List<PurchaseReturnVO> list = purchaseReturnService.getSearchDB(group_id, supply_name);
					Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd").create();
					String jsonStrList = gson.toJson(list);
					response.getWriter().write(jsonStrList);
					return;// 程式中斷
				}
				/*************************** 其他可能的錯誤處理 **********************************/
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		if ("insert_purchase_return".equals(action)) {
			try {
				/***************************
				 * 1.接收請求參數
				 ***************************************/
				String purchase_id = request.getParameter("purchase_id");
				SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

				java.sql.Date return_date = null;
				try {
					String return_date_str = request.getParameter("return_date");
					java.util.Date invoice_date_util = sdf.parse(return_date_str);
					return_date = new java.sql.Date(invoice_date_util.getTime());
				} catch (ParseException e) {

					e.printStackTrace();
				}
				/***************************
				 * 2.開始銷貨退回
				 ***************************************/
				purchaseReturnService = new PurchaseReturnService();
				purchaseReturnService.addPurchaseReturn(purchase_id, user_id, return_date);
				/***************************
				 * 3.刪除完成,準備轉交(Send the Success view)
				 ***********/
				purchaseReturnService = new PurchaseReturnService();
				List<PurchaseReturnVO> list = purchaseReturnService.getSearchReturnDateDB(group_id, return_date,
						return_date);
				Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd").create();
				String jsonStrList = gson.toJson(list);
				response.getWriter().write(jsonStrList);
				return;// 程式中斷
				/*************************** 其他可能的錯誤處理 **********************************/
			} catch (Exception e) {

				e.printStackTrace();
			}
		}
		if ("delete_purchase_return".equals(action)) {
			try {
				/***************************
				 * 1.接收請求參數
				 ***************************************/
				String supply_name = request.getParameter("sale_id");
				String purchase_id = request.getParameter("purchase_id");
				/***************************
				 * 2.開始銷貨退回
				 ***************************************/
				purchaseReturnService = new PurchaseReturnService();
				purchaseReturnService.deletePurchaseReturn(purchase_id, user_id);
				/***************************
				 * 3.刪除完成,準備轉交(Send the Success view)
				 ***********/
				// 假如無查詢條件，則是查詢全部
				if (supply_name == null || (supply_name.trim()).length() == 0) {
					purchaseReturnService = new PurchaseReturnService();
					List<PurchaseReturnVO> list = purchaseReturnService.getSearchAllDB(group_id);
					Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd").create();
					String jsonStrList = gson.toJson(list);
					response.getWriter().write(jsonStrList);
					return;// 程式中斷
				}
				// 查詢指定名稱
				if (supply_name != null || supply_name.trim().length() != 0) {
					purchaseReturnService = new PurchaseReturnService();
					List<PurchaseReturnVO> list = purchaseReturnService.getSearchDB(group_id, supply_name);
					Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd").create();
					String jsonStrList = gson.toJson(list);
					response.getWriter().write(jsonStrList);
					return;// 程式中斷
				}
				/*************************** 其他可能的錯誤處理 **********************************/
			} catch (Exception e) {

				e.printStackTrace();
			}
		}
		if ("search_purchase_date".equals(action)) {
			try {
				String start_date = request.getParameter("purchase_start_date");
				String end_date = request.getParameter("purchase_end_date");
				if (start_date.trim().length() != 0 & end_date.trim().length() == 0) {
					List<PurchaseReturnVO> list = new ArrayList<PurchaseReturnVO>();
					PurchaseReturnVO purchaseReturnVO = new PurchaseReturnVO();
					purchaseReturnVO.setMessage("如要以日期查詢，請完整填寫訖日欄位");
					list.add(purchaseReturnVO);
					Gson gson = new Gson();
					String jsonStrList = gson.toJson(list);
					response.getWriter().write(jsonStrList);
					return;// 程式中斷
				}
				if (end_date.trim().length() != 0 & start_date.trim().length() == 0) {
					List<PurchaseReturnVO> list = new ArrayList<PurchaseReturnVO>();
					PurchaseReturnVO purchaseReturnVO = new PurchaseReturnVO();
					purchaseReturnVO.setMessage("如要以日期查詢，請完整填寫起日欄位");
					list.add(purchaseReturnVO);
					Gson gson = new Gson();
					String jsonStrList = gson.toJson(list);
					response.getWriter().write(jsonStrList);
					return;// 程式中斷
				}
				if (start_date.trim().length() != 0 & end_date.trim().length() != 0) {
					if (DateConversionToDigital(start_date) > DateConversionToDigital(end_date)) {
						List<PurchaseReturnVO> list = new ArrayList<PurchaseReturnVO>();
						PurchaseReturnVO purchaseReturnVO = new PurchaseReturnVO();
						purchaseReturnVO.setMessage("起日不可大於訖日");
						list.add(purchaseReturnVO);
						Gson gson = new Gson();
						String jsonStrList = gson.toJson(list);
						response.getWriter().write(jsonStrList);
						return;// 程式中斷
					} else {
						// 查詢指定期限
						purchaseReturnService = new PurchaseReturnService();
						List<PurchaseReturnVO> list = purchaseReturnService.getSearchPurchaseDateDB(group_id,
								start_date, end_date);
						PurchaseReturnVO purchaseReturnVO = new PurchaseReturnVO();
						purchaseReturnVO.setMessage("驗證通過");
						list.add(purchaseReturnVO);
						Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd").create();
						String jsonStrList = gson.toJson(list);
						response.getWriter().write(jsonStrList);
						return;// 程式中斷
					}
				}
				// 假如無查詢條件，則是查詢全部
				if (start_date.trim().length() == 0 & end_date.trim().length() == 0) {
					purchaseReturnService = new PurchaseReturnService();
					List<PurchaseReturnVO> list = purchaseReturnService.getSearchAllDB(group_id);
					PurchaseReturnVO purchaseReturnVO = new PurchaseReturnVO();
					purchaseReturnVO.setMessage("驗證通過");
					list.add(purchaseReturnVO);
					Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd").create();
					String jsonStrList = gson.toJson(list);
					response.getWriter().write(jsonStrList);
					return;// 程式中斷
				}
				/*************************** 其他可能的錯誤處理 **********************************/
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		if ("search_return_date".equals(action)) {
			try {
				/***************************
				 * 1.接收請求參數-格式檢查
				 ****************************************/
				String start_date = request.getParameter("return_start_date");
				String end_date = request.getParameter("return_end_date");
				if (start_date.trim().length() != 0 & end_date.trim().length() == 0) {
					List<PurchaseReturnVO> list = new ArrayList<PurchaseReturnVO>();
					PurchaseReturnVO purchaseReturnVO = new PurchaseReturnVO();
					purchaseReturnVO.setMessage("如要以日期查詢，請完整填寫訖日欄位");
					list.add(purchaseReturnVO);
					Gson gson = new Gson();
					String jsonStrList = gson.toJson(list);
					response.getWriter().write(jsonStrList);
					return;// 程式中斷
				}
				if (end_date.trim().length() != 0 & start_date.trim().length() == 0) {
					List<PurchaseReturnVO> list = new ArrayList<PurchaseReturnVO>();
					PurchaseReturnVO purchaseReturnVO = new PurchaseReturnVO();
					purchaseReturnVO.setMessage("如要以日期查詢，請完整填寫起日欄位");
					list.add(purchaseReturnVO);
					Gson gson = new Gson();
					String jsonStrList = gson.toJson(list);
					response.getWriter().write(jsonStrList);
					return;// 程式中斷
				}
				if (start_date.trim().length() != 0 & end_date.trim().length() != 0) {
					if (DateConversionToDigital(start_date) > DateConversionToDigital(end_date)) {
						List<PurchaseReturnVO> list = new ArrayList<PurchaseReturnVO>();
						PurchaseReturnVO purchaseReturnVO = new PurchaseReturnVO();
						purchaseReturnVO.setMessage("起日不可大於訖日");
						list.add(purchaseReturnVO);
						Gson gson = new Gson();
						String jsonStrList = gson.toJson(list);
						response.getWriter().write(jsonStrList);
						return;// 程式中斷
					} else {
						SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
						java.sql.Date return_start_date = null;
						try {
							java.util.Date invoice_date_util = sdf.parse(start_date);
							return_start_date = new java.sql.Date(invoice_date_util.getTime());
						} catch (ParseException e) {
							e.printStackTrace();
						}
						java.sql.Date return_end_date = null;
						try {
							java.util.Date invoice_date_util = sdf.parse(end_date);
							return_end_date = new java.sql.Date(invoice_date_util.getTime());
						} catch (ParseException e) {
							e.printStackTrace();
						}

						// 查詢指定期限
						purchaseReturnService = new PurchaseReturnService();
						List<PurchaseReturnVO> list = purchaseReturnService.getSearchReturnDateDB(group_id,
								return_start_date, return_end_date);
						PurchaseReturnVO purchaseReturnVO = new PurchaseReturnVO();
						purchaseReturnVO.setMessage("驗證通過");
						list.add(purchaseReturnVO);
						Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd").create();
						String jsonStrList = gson.toJson(list);
						response.getWriter().write(jsonStrList);
						return;// 程式中斷
					}
				}
				// 假如無查詢條件，則是查詢全部
				if (start_date.trim().length() == 0 & end_date.trim().length() == 0) {
					purchaseReturnService = new PurchaseReturnService();
					List<PurchaseReturnVO> list = purchaseReturnService.getSearchAllPurchaseReturnDB(group_id);
					PurchaseReturnVO purchaseReturnVO = new PurchaseReturnVO();
					purchaseReturnVO.setMessage("驗證通過");
					list.add(purchaseReturnVO);
					Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd").create();
					String jsonStrList = gson.toJson(list);
					response.getWriter().write(jsonStrList);
					return;// 程式中斷
				}
				/*************************** 其他可能的錯誤處理 **********************************/
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	public int DateConversionToDigital(String Date) {

		StringBuffer str = new StringBuffer();
		String[] dateArray = Date.split("-");
		for (String i : dateArray) {
			str.append(i);
		}
		return Integer.parseInt(str.toString());
	}

	interface Purchreturn_interface {

		public void insertPurchaseReturnTotDB(String purchase_id, String user_id, Date return_date);

		public void deletePurchaseReturnTotDB(String purchase_id, String user_id);

		public List<PurchaseReturnVO> searchDB(String group_id, String supply_name);

		public List<PurchaseReturnVO> searchAllDB(String group_id);

		public List<PurchaseReturnVO> getSearchAllPurchaseReturnDB(String group_id);

		public List<SupplyVO> getSupplyName(String group_id, String supply_name);

		public List<PurchaseReturnVO> getSearchReturnDateDB(String group_id, Date start_date, Date end_date);

		public List<PurchaseReturnVO> getSearchPurchaseDateDB(String group_id, String purchase_start_date,
				String purchase_end_date);
	}

	class PurchaseReturnService {
		public Purchreturn_interface dao;

		public PurchaseReturnService() {
			dao = new PurchreturnDAO();
		}

		public void addPurchaseReturn(String purchase_id, String user_id, Date return_date) {
			dao.insertPurchaseReturnTotDB(purchase_id, user_id, return_date);
		}

		public void deletePurchaseReturn(String purchase_id, String user_id) {
			dao.deletePurchaseReturnTotDB(purchase_id, user_id);
		}

		public List<PurchaseReturnVO> getSearchDB(String group_id, String supply_name) {
			return dao.searchDB(group_id, supply_name);
		}

		public List<PurchaseReturnVO> getSearchAllDB(String group_id) {
			return dao.searchAllDB(group_id);
		}

		public List<PurchaseReturnVO> getSearchAllPurchaseReturnDB(String group_id) {
			return dao.getSearchAllPurchaseReturnDB(group_id);
		}

		public List<SupplyVO> getSupplyname(String group_id, String supply_name) {
			return dao.getSupplyName(group_id, supply_name);
		}

		public List<PurchaseReturnVO> getSearchReturnDateDB(String group_id, Date start_date, Date end_date) {
			return dao.getSearchReturnDateDB(group_id, start_date, end_date);
		}

		public List<PurchaseReturnVO> getSearchPurchaseDateDB(String group_id, String purchase_start_date,
				String purchase_end_date) {
			return dao.getSearchPurchaseDateDB(group_id, purchase_start_date, purchase_end_date);
		}
	}

	class PurchreturnDAO implements Purchreturn_interface {
		private static final String sp_get_supplyname = "call sp_get_supplyname(?,?)";
		private static final String sp_select_purchase_bysupllyname = "call sp_select_purchase_bysupllyname(?,?)";
		private static final String sp_selectall_purchase = "call sp_selectall_purchase (?)";
		private static final String sp_selectall_purch_return = "call sp_selectall_purch_return (?)";
		private static final String sp_insert_purch_return = "call sp_insert_purch_return(?,?,?)";
		private static final String sp_del_purch_return = "call sp_del_purch_return(?,?)";
		private static final String sp_select_purchase_return_byreturndate = "call sp_select_purchase_return_byreturndate(?,?,?)";
		private static final String sp_select_purchase_bypurchase_date = "call sp_select_purchase_bypurchase_date(?,?,?)";
		private final String dbURL = getServletConfig().getServletContext().getInitParameter("dbURL")
				+ "?useUnicode=true&characterEncoding=utf-8&useSSL=false";
		private final String dbUserName = getServletConfig().getServletContext().getInitParameter("dbUserName");
		private final String dbPassword = getServletConfig().getServletContext().getInitParameter("dbPassword");

		@Override
		public List<PurchaseReturnVO> searchDB(String group_id, String supply_name) {

			List<PurchaseReturnVO> list = new ArrayList<PurchaseReturnVO>();
			PurchaseReturnVO purchaseReturnVO = null;

			Connection con = null;
			PreparedStatement pstmt = null;
			ResultSet rs = null;

			try {
				Class.forName("com.mysql.jdbc.Driver");
				con = DriverManager.getConnection(dbURL, dbUserName, dbPassword);
				pstmt = con.prepareStatement(sp_select_purchase_bysupllyname);
				pstmt.setString(1, group_id);
				pstmt.setString(2, supply_name);
				rs = pstmt.executeQuery();
				while (rs.next()) {
					purchaseReturnVO = new PurchaseReturnVO();
					purchaseReturnVO.setPurchase_id(rs.getString("purchase_id"));
					purchaseReturnVO.setSeq_no(rs.getString("seq_no"));
					purchaseReturnVO.setGroup_id(rs.getString("group_id"));
					purchaseReturnVO.setUser_id(rs.getString("user_id"));
					purchaseReturnVO.setSupply_id(rs.getString("supply_id"));
					purchaseReturnVO.setMemo(rs.getString("memo"));
					purchaseReturnVO.setPurchase_date(rs.getDate("purchase_date"));
					purchaseReturnVO.setInvoice(rs.getString("invoice"));
					purchaseReturnVO.setInvoice_type(rs.getString("invoice_type"));
					purchaseReturnVO.setAmount(rs.getFloat("amount"));
					purchaseReturnVO.setReturn_date(rs.getDate("return_date"));
					purchaseReturnVO.setIsreturn(rs.getBoolean("isreturn"));
					list.add(purchaseReturnVO); // Store the row in the list
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
		public List<PurchaseReturnVO> searchAllDB(String group_id) {
			List<PurchaseReturnVO> list = new ArrayList<PurchaseReturnVO>();
			PurchaseReturnVO purchaseReturnVO = null;

			Connection con = null;
			PreparedStatement pstmt = null;
			ResultSet rs = null;

			try {
				Class.forName("com.mysql.jdbc.Driver");
				con = DriverManager.getConnection(dbURL, dbUserName, dbPassword);
				pstmt = con.prepareStatement(sp_selectall_purchase);
				pstmt.setString(1, group_id);
				rs = pstmt.executeQuery();
				while (rs.next()) {
					purchaseReturnVO = new PurchaseReturnVO();
					purchaseReturnVO.setPurchase_id(rs.getString("purchase_id"));
					purchaseReturnVO.setSeq_no(rs.getString("seq_no"));
					purchaseReturnVO.setGroup_id(rs.getString("group_id"));
					purchaseReturnVO.setUser_id(rs.getString("user_id"));
					purchaseReturnVO.setSupply_id(rs.getString("supply_id"));
					purchaseReturnVO.setMemo(rs.getString("memo"));
					purchaseReturnVO.setPurchase_date(rs.getDate("purchase_date"));
					purchaseReturnVO.setInvoice(rs.getString("invoice"));
					purchaseReturnVO.setInvoice_type(rs.getString("invoice_type"));
					purchaseReturnVO.setAmount(rs.getFloat("amount"));
					purchaseReturnVO.setReturn_date(rs.getDate("return_date"));
					purchaseReturnVO.setIsreturn(rs.getBoolean("isreturn"));
					list.add(purchaseReturnVO); // Store the row in the list
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
		public List<SupplyVO> getSupplyName(String group_id, String supply_name) {
			List<SupplyVO> list = new ArrayList<SupplyVO>();
			SupplyVO supplyVO = null;

			Connection con = null;
			PreparedStatement pstmt = null;
			ResultSet rs = null;

			try {
				Class.forName("com.mysql.jdbc.Driver");
				con = DriverManager.getConnection(dbURL, dbUserName, dbPassword);
				pstmt = con.prepareStatement(sp_get_supplyname);
				pstmt.setString(1, group_id);
				pstmt.setString(2, supply_name);
				rs = pstmt.executeQuery();
				while (rs.next()) {
					supplyVO = new SupplyVO();
					supplyVO.setSupply_id(rs.getString("supply_id"));
					supplyVO.setSupply_name(rs.getString("supply_name"));
					list.add(supplyVO); // Store the row in the list
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
		public void insertPurchaseReturnTotDB(String purchase_id, String user_id, Date return_date) {
			Connection con = null;
			PreparedStatement pstmt = null;
			try {
				Class.forName("com.mysql.jdbc.Driver");
				con = DriverManager.getConnection(dbURL, dbUserName, dbPassword);
				pstmt = con.prepareStatement(sp_insert_purch_return);

				pstmt.setString(1, purchase_id);
				pstmt.setString(2, user_id);
				pstmt.setDate(3, return_date);

				pstmt.executeUpdate();

				// Handle any SQL errors
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
		}

		@Override
		public void deletePurchaseReturnTotDB(String purchase_id, String user_id) {
			Connection con = null;
			PreparedStatement pstmt = null;
			try {
				Class.forName("com.mysql.jdbc.Driver");
				con = DriverManager.getConnection(dbURL, dbUserName, dbPassword);
				pstmt = con.prepareStatement(sp_del_purch_return);

				pstmt.setString(1, purchase_id);
				pstmt.setString(2, user_id);

				pstmt.executeUpdate();

				// Handle any SQL errors
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
		}

		@Override
		public List<PurchaseReturnVO> getSearchReturnDateDB(String group_id, Date return_start_date,
				Date return_end_date) {
			List<PurchaseReturnVO> list = new ArrayList<PurchaseReturnVO>();
			PurchaseReturnVO purchaseReturnVO = null;

			Connection con = null;
			PreparedStatement pstmt = null;
			ResultSet rs = null;

			try {
				Class.forName("com.mysql.jdbc.Driver");
				con = DriverManager.getConnection(dbURL, dbUserName, dbPassword);
				pstmt = con.prepareStatement(sp_select_purchase_return_byreturndate);
				pstmt.setString(1, group_id);
				pstmt.setDate(2, return_start_date);
				pstmt.setDate(3, return_end_date);
				rs = pstmt.executeQuery();
				while (rs.next()) {
					purchaseReturnVO = new PurchaseReturnVO();
					purchaseReturnVO.setPurchase_id(rs.getString("purchase_id"));
					purchaseReturnVO.setSeq_no(rs.getString("seq_no"));
					purchaseReturnVO.setGroup_id(rs.getString("group_id"));
					purchaseReturnVO.setUser_id(rs.getString("user_id"));
					purchaseReturnVO.setSupply_id(rs.getString("supply_id"));
					purchaseReturnVO.setMemo(rs.getString("memo"));
					purchaseReturnVO.setPurchase_date(rs.getDate("purchase_date"));
					purchaseReturnVO.setInvoice(rs.getString("invoice"));
					purchaseReturnVO.setInvoice_type(rs.getString("invoice_type"));
					purchaseReturnVO.setAmount(rs.getFloat("amount"));
					purchaseReturnVO.setReturn_date(rs.getDate("return_date"));
					purchaseReturnVO.setIsreturn(rs.getBoolean("isreturn"));
					list.add(purchaseReturnVO); // Store the row in the list
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
		public List<PurchaseReturnVO> getSearchPurchaseDateDB(String group_id, String purchase_start_date,
				String purchase_end_date) {

			List<PurchaseReturnVO> list = new ArrayList<PurchaseReturnVO>();
			PurchaseReturnVO purchaseReturnVO = null;

			Connection con = null;
			PreparedStatement pstmt = null;
			ResultSet rs = null;

			try {
				Class.forName("com.mysql.jdbc.Driver");
				con = DriverManager.getConnection(dbURL, dbUserName, dbPassword);
				pstmt = con.prepareStatement(sp_select_purchase_bypurchase_date);
				pstmt.setString(1, group_id);
				pstmt.setString(2, purchase_start_date);
				pstmt.setString(3, purchase_end_date);
				rs = pstmt.executeQuery();
				while (rs.next()) {
					purchaseReturnVO = new PurchaseReturnVO();
					purchaseReturnVO.setPurchase_id(rs.getString("purchase_id"));
					purchaseReturnVO.setSeq_no(rs.getString("seq_no"));
					purchaseReturnVO.setGroup_id(rs.getString("group_id"));
					purchaseReturnVO.setUser_id(rs.getString("user_id"));
					purchaseReturnVO.setSupply_id(rs.getString("supply_id"));
					purchaseReturnVO.setMemo(rs.getString("memo"));
					purchaseReturnVO.setPurchase_date(rs.getDate("purchase_date"));
					purchaseReturnVO.setInvoice(rs.getString("invoice"));
					purchaseReturnVO.setInvoice_type(rs.getString("invoice_type"));
					purchaseReturnVO.setAmount(rs.getFloat("amount"));
					purchaseReturnVO.setReturn_date(rs.getDate("return_date"));
					purchaseReturnVO.setIsreturn(rs.getBoolean("isreturn"));
					list.add(purchaseReturnVO); // Store the row in the list
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
		public List<PurchaseReturnVO> getSearchAllPurchaseReturnDB(String group_id) {

			List<PurchaseReturnVO> list = new ArrayList<PurchaseReturnVO>();
			PurchaseReturnVO purchaseReturnVO = null;

			Connection con = null;
			PreparedStatement pstmt = null;
			ResultSet rs = null;

			try {
				Class.forName("com.mysql.jdbc.Driver");
				con = DriverManager.getConnection(dbURL, dbUserName, dbPassword);
				pstmt = con.prepareStatement(sp_selectall_purch_return);
				pstmt.setString(1, group_id);
				rs = pstmt.executeQuery();
				while (rs.next()) {
					purchaseReturnVO = new PurchaseReturnVO();
					purchaseReturnVO.setPurchase_id(rs.getString("purchase_id"));
					purchaseReturnVO.setSeq_no(rs.getString("seq_no"));
					purchaseReturnVO.setGroup_id(rs.getString("group_id"));
					purchaseReturnVO.setUser_id(rs.getString("user_id"));
					purchaseReturnVO.setSupply_id(rs.getString("supply_id"));
					purchaseReturnVO.setMemo(rs.getString("memo"));
					purchaseReturnVO.setPurchase_date(rs.getDate("purchase_date"));
					purchaseReturnVO.setInvoice(rs.getString("invoice"));
					purchaseReturnVO.setInvoice_type(rs.getString("invoice_type"));
					purchaseReturnVO.setAmount(rs.getFloat("amount"));
					purchaseReturnVO.setReturn_date(rs.getDate("return_date"));
					purchaseReturnVO.setIsreturn(rs.getBoolean("isreturn"));
					list.add(purchaseReturnVO); // Store the row in the list
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
