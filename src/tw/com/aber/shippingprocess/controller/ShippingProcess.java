package tw.com.aber.shippingprocess.controller;

import java.io.IOException;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.JSONObject;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;

import tw.com.aber.vo.CustomerVO;
import tw.com.aber.vo.RealSaleVO;

public class ShippingProcess extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private static final Logger logger = LogManager.getLogger(ShippingProcess.class);

	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		request.setCharacterEncoding("UTF-8");
		response.setCharacterEncoding("UTF-8");

		String group_id = request.getSession().getAttribute("group_id").toString();
		String user_id = request.getSession().getAttribute("user_id").toString();
		String action = request.getParameter("action");
		
		logger.debug("group_id: "+group_id);
		logger.debug("user_id: "+group_id);
		logger.debug("action: "+action);
		
		Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd").create();
		ShippingProcessService service = new ShippingProcessService();
		try {
			if ("statisticsAllocinvData".equals(action)) {
				// 配庫
				String warehouse_id = request.getParameter("import_warehouse_id");
				
				logger.debug("warehouse_id: " + warehouse_id);
				
				JSONObject jsonObject = service.statisticsAlloc(group_id, user_id,warehouse_id);

				response.getWriter().write(jsonObject.toString());
				
				logger.debug("jsonObject: " + jsonObject.toString());

			} else if ("importData".equals(action)) { // 銷貨
				String c_import_trans_list_date_begin = request.getParameter("import_trans_list_date_begin");
				String c_import_trans_list_date_end = request.getParameter("import_trans_list_date_end");

				logger.debug("c_import_trans_list_date_begin: " + c_import_trans_list_date_begin);
				logger.debug("c_import_trans_list_date_end: " + c_import_trans_list_date_end);

				JSONObject jsonObject = service.importRealSale(group_id, user_id, c_import_trans_list_date_begin,
						c_import_trans_list_date_end);

				response.getWriter().write(jsonObject.toString());
				
				logger.debug("jsonObject: " + jsonObject.toString());

			} else if ("importallocinvData".equals(action)) {
				// 配庫
				JSONObject jsonObject = service.importAllocInv(group_id, user_id);

				response.getWriter().write(jsonObject.toString());
				
				logger.debug("responseStr: " + jsonObject.toString());

			} else if ("importPicking".equals(action)) {
				// 揀貨
				String order_no_count = request.getParameter("order_no_count");
				String warehouse_id = request.getParameter("import_warehouse_id");
				
				logger.debug("order_no_count: " + order_no_count);
				logger.debug("warehouse_id: " + warehouse_id);

				JSONObject jsonObject = service.importPicking(group_id, user_id, order_no_count,warehouse_id);
				
				logger.debug("jsonObject: " + jsonObject.toString());
				
				response.getWriter().write(jsonObject.toString());

			} else if ("importShip".equals(action)) {
				// 出貨
				JSONObject jsonObject = service.importShip(group_id, user_id);
				
				logger.debug("jsonObject: " + jsonObject.toString());

				response.getWriter().write(jsonObject.toString());
				
			} else if ("fastExecution".equals(action)) {

				String fast_trans_list_date_begin = request.getParameter("fast_trans_list_date_begin");
				String fast_trans_list_date_end = request.getParameter("fast_trans_list_date_end");
				String order_no_count = request.getParameter("fast_order_count");
				String warehouse_id = request.getParameter("fast_warehouse_id");
				

				logger.debug("fast_trans_list_date_begin:" + fast_trans_list_date_begin);
				logger.debug("fast_trans_list_date_end:" + fast_trans_list_date_end);
				logger.debug("order_no_count:" + order_no_count);
				logger.debug("warehouse_id:" + warehouse_id);

				// 匯入銷貨
				JSONObject jsonObject = service.importRealSale(group_id, user_id, fast_trans_list_date_begin,
						fast_trans_list_date_end);
				
				boolean isSucess = checkData(jsonObject, 1);
				
				if (!isSucess) {
					jsonObject = new JSONObject();
					jsonObject.put("isSuccess", false);
					logger.debug(jsonObject.toString());
					response.getWriter().write(jsonObject.toString());
				}

				// 匯入待出庫
				jsonObject = service.importAllocInv(group_id, user_id);

				isSucess = checkData(jsonObject, 1);
				if (!isSucess) {
					jsonObject.put("isSuccess", false);
					logger.debug(jsonObject.toString());
					response.getWriter().write(jsonObject.toString());
				}

				// 配庫
				jsonObject = service.statisticsAlloc(group_id, user_id,warehouse_id);

				isSucess = checkData(jsonObject, 1);
				if (!isSucess) {
					jsonObject = new JSONObject();
					jsonObject.put("isSuccess", false);
					logger.debug(jsonObject.toString());
					response.getWriter().write(jsonObject.toString());
				}

				// 檢貨
				jsonObject = service.importPicking(group_id, user_id, order_no_count,warehouse_id);

				isSucess = checkData(jsonObject, 2);
				if (!isSucess) {
					jsonObject = new JSONObject();
					jsonObject.put("isSuccess", false);
					logger.debug(jsonObject.toString());
					response.getWriter().write(jsonObject.toString());
				}
				// 出貨
				jsonObject = service.importShip(group_id, user_id);

				isSucess = checkData(jsonObject, 2);
				if (!isSucess) {
					jsonObject = new JSONObject();
					jsonObject.put("isSuccess", false);
					logger.debug(jsonObject.toString());
					response.getWriter().write(jsonObject.toString());
				} else {
					jsonObject = new JSONObject();
					jsonObject.put("isSuccess", true);
					logger.debug(jsonObject.toString());
					response.getWriter().write(jsonObject.toString());
				}

			}

		} catch (Exception e) {
			logger.error("Exception:".concat(e.getMessage()));
		}
	}
		
	/**
	 * <p>資料檢查
	 * 
	 * @param jsonObject
	 * @param type
	 * @return
	 */
	public boolean checkData(JSONObject jsonObject, int type) {
		try {

			if (type == 1) {

				String order_no_cnt = (String) jsonObject.get("order_no_cnt");
				String total_cnt = (String) jsonObject.get("total_cnt");

				if ("".equals(order_no_cnt) || null == order_no_cnt) {
					return false;
				}
				if ("".equals(total_cnt) || null == total_cnt) {
					return false;
				}
				return true;
			} else if (type == 2) {
				Boolean isSuccess =  jsonObject.getBoolean("isSuccess");
				if (isSuccess==false || null == isSuccess) {
					return false;
				}
				return true;
			} else {
				return false;
			}
		} catch (Exception e) {
			return false;
		}
	}
	
	protected class ShippingProcessService {
		private ShippingProcess_interface dao = new ShippingProcessDAO();


		/**
		 * <p>轉入銷貨作業。依轉單日期{@code trans_list_date}區間
		 * 
		 * @param group_id
		 * @param user_id
		 * @param trans_list_date_begin
		 * @param trans_list_date_end
		 * @return
		 */
		public JSONObject importRealSale(String group_id,String user_id,String trans_list_date_begin,String trans_list_date_end) {
			return dao.importDB(group_id,user_id,trans_list_date_begin,trans_list_date_end);
		}
		
		public List<RealSaleVO> getSearchAllDB(String group_id) {
			return dao.searchAllDB(group_id);
		}

		/**
		 * <p>配庫作業。
		 * 
		 * @param group_id
		 * @param user_id
		 * @param warehouse_id
		 * @return
		 */
		public JSONObject statisticsAlloc(String group_id, String user_id, String warehouse_id) {
			return dao.statisticsAlloc(group_id, user_id, warehouse_id);
		}
		
		/**
		 * <p>轉入揀貨作業
		 * 
		 * @param group_id
		 * @param user_id
		 * @param order_count
		 * @param warehouse_id
		 * @return
		 */
		public JSONObject importPicking(String group_id, String user_id, String order_count, String warehouse_id) {
			return dao.importPicking(group_id, user_id, order_count, warehouse_id);
		}
		
		/**
		 * <p>轉入出貨作業
		 * 
		 * @param group_id
		 * @param user_id
		 * @return
		 */
		public JSONObject importShip(String group_id, String user_id) {
			return dao.importShip(group_id, user_id);
		}
		
		/**
		 * <p>轉入待出庫作業
		 * 
		 * @param group_id
		 * @param user_id
		 * @return
		 */
		public JSONObject importAllocInv(String group_id,String user_id) {
			return dao.importAllocInvDB(group_id,user_id);
		}
	}

	class ShippingProcessDAO implements ShippingProcess_interface {
		private final String jdbcDriver = getServletConfig().getServletContext().getInitParameter("jdbcDriver");
		private final String dbURL = getServletConfig().getServletContext().getInitParameter("dbURL")
				+ "?useUnicode=true&characterEncoding=utf-8&useSSL=false";
		private final String dbUserName = getServletConfig().getServletContext().getInitParameter("dbUserName");
		private final String dbPassword = getServletConfig().getServletContext().getInitParameter("dbPassword");

		// 匯入
		private static final String sp_importData_realsale = "call sp_importData_realsale (?,?,?,?)";
		// 查詢
		private static final String sp_selectall_realsale = "call sp_selectall_realsale(?)";
		// 配庫
		private static final String sp_statistics_alloc_inv = "call sp_statistics_alloc_inv(?,?,?,?)";		
		// 撿貨
		private static final String sp_importData_picking = "call sp_importData_picking(?,?,?,?)";
		// 出貨
		private static final String sp_importData_ship = "call sp_importData_ship(?,?)";
		
		private static final String sp_importData_alloc_inv = "call sp_importData_alloc_inv (?,?)";


		@Override
		public JSONObject statisticsAlloc(String group_id, String user_id,String warehouse_id) {
			Connection con = null;
			CallableStatement cs = null;
			ResultSet rs = null;
			String updateCount = null;
			JSONObject jsonObject = new JSONObject();
			boolean isSuccess = false;

			try {
				Class.forName(jdbcDriver);
				con = DriverManager.getConnection(dbURL, dbUserName, dbPassword);
				cs = con.prepareCall(sp_statistics_alloc_inv);
				cs.setString(1, group_id);
				cs.setString(2, user_id);
				cs.setString(3, warehouse_id);
				cs.registerOutParameter(4, Types.BOOLEAN);
				
				isSuccess = cs.execute();
				updateCount = cs.getString(4);
				
				jsonObject.put("update_count", updateCount);
				
				rs = cs.getResultSet();
				if (rs.next()) {
					jsonObject.put("order_no_cnt", rs.getString("order_no_cnt"));
					jsonObject.put("total_cnt", rs.getString("total_cnt"));
				}
			} catch (SQLException se) {
				throw new RuntimeException("A database error occured. " + se.getMessage());
			} catch (ClassNotFoundException cnfe) {
				throw new RuntimeException("A database error occured. " + cnfe.getMessage());
			} catch (Exception e) {
				throw new RuntimeException("Exception. " + e.getMessage());
			} finally {
				try {
					if (rs != null) {
						rs.close();
					}
					if (cs != null) {
						cs.close();
					}
					if (con != null) {
						con.close();
					}
				} catch (SQLException se) {
					logger.error("SQLException:".concat(se.getMessage()));
				} catch (Exception e) {
					logger.error("Exception:".concat(e.getMessage()));
				}
			}
			return jsonObject;
		}

		@Override
		public List<RealSaleVO> searchAllDB(String group_id) {
			List<RealSaleVO> list = new ArrayList<RealSaleVO>();
			RealSaleVO realSaleVO = null;

			Connection con = null;
			PreparedStatement pstmt = null;
			ResultSet rs = null;
			try {
				Class.forName(jdbcDriver);
				con = DriverManager.getConnection(dbURL, dbUserName, dbPassword);
				pstmt = con.prepareStatement(sp_selectall_realsale);
				pstmt.setString(1, group_id);
				rs = pstmt.executeQuery();
				while (rs.next()) {
					realSaleVO = new RealSaleVO();
					realSaleVO.setOrder_no(rs.getString("order_no"));
					realSaleVO.setName(rs.getString("name"));
					realSaleVO.setTrans_list_date(rs.getDate("trans_list_date"));
					realSaleVO.setSale_date(rs.getDate("sale_date"));
					realSaleVO.setDis_date(rs.getDate("dis_date"));
					realSaleVO.setOrder_source(rs.getString("order_source"));
					realSaleVO.setMemo(rs.getString("memo"));
					realSaleVO.setRealsale_id(rs.getString("realsale_id"));
					realSaleVO.setOrder_source(rs.getString("order_source"));
					realSaleVO.setCustomer_id(rs.getString("customer_id"));
					realSaleVO.setInvoice(rs.getString("invoice"));
					realSaleVO.setTotal_amt(rs.getFloat("total_amt"));
					realSaleVO.setInvoice_date(rs.getDate("invoice_date"));
					list.add(realSaleVO); // Store the row in the list
				}
			} catch (SQLException se) {
				throw new RuntimeException("A database error occured. " + se.getMessage());
			} catch (ClassNotFoundException cnfe) {
				throw new RuntimeException("A database error occured. " + cnfe.getMessage());
			} finally {
				try {
					if (rs != null) {
						rs.close();
					}
					if (pstmt != null) {
						pstmt.close();
					}
					if (con != null) {
						con.close();
					}
				} catch (SQLException se) {
					logger.error("SQLException:".concat(se.getMessage()));
				} catch (Exception e) {
					logger.error("Exception:".concat(e.getMessage()));
				}
			}
			return list;
		}

		@Override
		public JSONObject importDB(String group_id,String user_id,String trans_list_date_begin,String trans_list_date_end) {
			Connection con = null;
			PreparedStatement pstmt = null;
			ResultSet rs = null;
			JSONObject jsonObject = new JSONObject();
			
			try {
				
				Class.forName(jdbcDriver);
				con = DriverManager.getConnection(dbURL, dbUserName, dbPassword);
				pstmt = con.prepareStatement(sp_importData_realsale);
				pstmt.setString(1, group_id);
				pstmt.setString(2, user_id);
				pstmt.setString(3, trans_list_date_begin);		
				pstmt.setString(4, trans_list_date_end);			

				rs = pstmt.executeQuery();
				if (rs.next()) {
					jsonObject.put("order_no_cnt", rs.getString("order_no_cnt"));
					jsonObject.put("total_cnt", rs.getString("total_cnt"));
				}
				
			} catch (SQLException se) {
				throw new RuntimeException("A database error occured. " + se.getMessage());
			} catch (ClassNotFoundException cnfe) {
				throw new RuntimeException("A database error occured. " + cnfe.getMessage());
			} finally {
				try {
					if (pstmt != null) {
						pstmt.close();
					}
					if (con != null) {
						con.close();
					}
				} catch (SQLException se) {
					logger.error("SQLException:".concat(se.getMessage()));
				} catch (Exception e) {
					logger.error("Exception:".concat(e.getMessage()));
				}
			}
			
			return jsonObject;
		}

		@Override
		public JSONObject importPicking(String group_id, String user_id, String order_count, String warehouse_id) {
			Connection con = null;
			CallableStatement cs = null;
			ResultSet rs = null;
			String updateCount = null;
			JSONObject jsonObject = new JSONObject();
			boolean isSuccess = false;

			try {
				Class.forName(jdbcDriver);
				con = DriverManager.getConnection(dbURL, dbUserName, dbPassword);
				cs = con.prepareCall(sp_importData_picking);
				cs.setString(1, group_id);
				cs.setString(2, user_id);
				cs.setString(3, order_count);
				cs.setString(4, warehouse_id);
				isSuccess = cs.execute();
				jsonObject.put("update_count", updateCount);
				
				rs = cs.getResultSet();
				if (rs.next()) {
					jsonObject.put("order_no_cnt", rs.getString("order_no_cnt"));
					jsonObject.put("total_cnt", rs.getString("total_cnt"));
				}
			} catch (SQLException se) {
				throw new RuntimeException("A database error occured. " + se.getMessage());
			} catch (ClassNotFoundException cnfe) {
				throw new RuntimeException("A database error occured. " + cnfe.getMessage());
			} catch (Exception e) {
				throw new RuntimeException("Exception. " + e.getMessage());
			} finally {
				try {
					if (rs != null) {
						rs.close();
					}
					if (cs != null) {
						cs.close();
					}
					if (con != null) {
						con.close();
					}
				} catch (SQLException se) {
					logger.error("SQLException:".concat(se.getMessage()));
				} catch (Exception e) {
					logger.error("Exception:".concat(e.getMessage()));
				}
			}
			isSuccess = true;
			jsonObject.put("isSuccess", isSuccess);
			return jsonObject;

		}
		
		
		@Override
		public JSONObject importShip(String group_id, String user_id) {
			Connection con = null;
			CallableStatement cs = null;
			ResultSet rs = null;
			String updateCount = null;
			JSONObject jsonObject = new JSONObject();
			boolean isSuccess = false;

			try {
				Class.forName(jdbcDriver);
				con = DriverManager.getConnection(dbURL, dbUserName, dbPassword);
				cs = con.prepareCall(sp_importData_ship);
				cs.setString(1, group_id);
				cs.setString(2, user_id);
				isSuccess = cs.execute();
				
				rs = cs.getResultSet();
				if (rs.next()) {
					jsonObject.put("order_no_cnt", rs.getString("order_no_cnt"));
					jsonObject.put("item_cnt", rs.getString("item_cnt"));
					jsonObject.put("total_cnt", rs.getString("total_cnt"));
				}
			} catch (SQLException se) {
				throw new RuntimeException("A database error occured. " + se.getMessage());
			} catch (ClassNotFoundException cnfe) {
				throw new RuntimeException("A database error occured. " + cnfe.getMessage());
			} catch (Exception e) {
				throw new RuntimeException("Exception. " + e.getMessage());
			} finally {
				try {
					if (rs != null) {
						rs.close();
					}
					if (cs != null) {
						cs.close();
					}
					if (con != null) {
						con.close();
					}
				} catch (SQLException se) {
					logger.error("SQLException:".concat(se.getMessage()));
				} catch (Exception e) {
					logger.error("Exception:".concat(e.getMessage()));
				}
			}
			isSuccess = true;
			jsonObject.put("isSuccess", isSuccess);
			return jsonObject;

		}
		
		
		@Override
		public JSONObject importAllocInvDB(String group_id,String user_id) {
			Connection con = null;
			PreparedStatement pstmt = null;
			ResultSet rs = null;
			JSONObject jsonObject = new JSONObject();
			
			try {
				Class.forName(jdbcDriver);
				con = DriverManager.getConnection(dbURL, dbUserName, dbPassword);
				pstmt = con.prepareStatement(sp_importData_alloc_inv);
				pstmt.setString(1, group_id);
				pstmt.setString(2, user_id);
				
				rs = pstmt.executeQuery();
				if (rs.next()) {
					jsonObject.put("order_no_cnt", rs.getString("order_no_cnt"));
					jsonObject.put("total_cnt", rs.getString("total_cnt"));
				}
			} catch (SQLException se) {
				throw new RuntimeException("A database error occured. " + se.getMessage());
			} catch (ClassNotFoundException cnfe) {
				throw new RuntimeException("A database error occured. " + cnfe.getMessage());
			} finally {
				try {
					if (pstmt != null) {
						pstmt.close();
					}
					if (con != null) {
						con.close();
					}
				} catch (SQLException se) {
					logger.error("SQLException:".concat(se.getMessage()));
				} catch (Exception e) {
					logger.error("Exception:".concat(e.getMessage()));
				}
			}
			
			return jsonObject;
		}
	}

	interface ShippingProcess_interface {
		
		/**
		 * <p>執行訂單轉銷貨
		 * 
		 * @param group_id
		 * @param user_id
		 * @param trans_list_date_begin
		 * @param trans_list_date_end
		 * @return
		 */
		public JSONObject importDB(String group_id,String user_id,String trans_list_date_begin,String trans_list_date_end);

		public List<RealSaleVO> searchAllDB(String group_id);

		/**
		 * <p>執行配庫作業
		 * 
		 * @param group_id
		 * @param user_id
		 * @param warehouse_id
		 * @return
		 */
		public JSONObject statisticsAlloc(String group_id, String user_id, String warehouse_id);
		
		/**
		 * <p>執行待出庫轉揀貨
		 * 
		 * @param group_id
		 * @param user_id
		 * @param order_count
		 * @param warehouse_id
		 * @return
		 */
		public JSONObject importPicking(String group_id, String user_id, String order_count, String warehouse_id);
		
		/**
		 * <p>執行揀貨轉出貨
		 * 
		 * @param group_id
		 * @param user_id
		 * @return
		 */
		public JSONObject importShip(String group_id, String user_id);
		
		/**
		 * <p>執行銷貨轉待出庫
		 * 
		 * @param group_id
		 * @param user_id
		 * @return
		 */
		public JSONObject importAllocInvDB(String group_id,String user_id);
	}

}
