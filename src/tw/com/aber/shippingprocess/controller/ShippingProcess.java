package tw.com.aber.shippingprocess.controller;

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
		Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd").create();
		ShippingProcessService service = new ShippingProcessService();
		try {
			if ("statisticsAllocinvData".equals(action)) {
				JSONObject responseStr = service.statisticsAlloc(group_id, user_id);
				response.getWriter().write(responseStr.toString());
			} else if ("importData".equals(action)) {
				List<RealSaleVO> realsaleList = null;
				String c_import_trans_list_date_begin = request.getParameter("import_trans_list_date_begin");
				String c_import_trans_list_date_end = request.getParameter("import_trans_list_date_end");
				service.importRealSale(group_id, user_id, c_import_trans_list_date_begin, c_import_trans_list_date_end);
				realsaleList = service.getSearchAllDB(group_id);
				String jsonStrList = gson.toJson(realsaleList);
				response.getWriter().write(jsonStrList);
				logger.info(jsonStrList);
			}else if("importPicking".equals(action)){
				String order_no_count = request.getParameter("order_no_count");
				JSONObject responseStr = service.importPicking(group_id, user_id,order_no_count);
				response.getWriter().write(responseStr.toString());
			}
		} catch (Exception e) {
			logger.error("Exception:".concat(e.getMessage()));
		}
	}

	protected class ShippingProcessService {
		private ShippingProcess_interface dao = new ShippingProcessDAO();

		public void importRealSale(String group_id, String user_id, String trans_list_date_begin,
				String trans_list_date_end) {
			dao.importDB(group_id, user_id, trans_list_date_begin, trans_list_date_end);
		}

		public List<RealSaleVO> getSearchAllDB(String group_id) {
			return dao.searchAllDB(group_id);
		}

		public JSONObject statisticsAlloc(String group_id, String user_id) {
			return dao.statisticsAlloc(group_id, user_id);
		}
		public JSONObject importPicking(String group_id, String user_id, String order_count) {
			return dao.importPicking(group_id, user_id,order_count);
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
		private static final String sp_statistics_alloc_inv = "call sp_statistics_alloc_inv(?,?,?)";		
		// 撿貨
		private static final String sp_importData_picking = "call sp_importData_picking(?,?,?)";

		@Override
		public JSONObject statisticsAlloc(String group_id, String user_id) {
		
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
				cs.registerOutParameter(3, Types.BOOLEAN);
				isSuccess = cs.execute();
				updateCount = cs.getString(3);
				jsonObject.put("update_count", updateCount);
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
		public void importDB(String group_id, String user_id, String trans_list_date_begin,
				String trans_list_date_end) {
			Connection con = null;
			PreparedStatement pstmt = null;
			try {
				Class.forName(jdbcDriver);
				con = DriverManager.getConnection(dbURL, dbUserName, dbPassword);
				pstmt = con.prepareStatement(sp_importData_realsale);
				pstmt.setString(1, group_id);
				pstmt.setString(2, user_id);
				pstmt.setString(3, trans_list_date_begin);
				pstmt.setString(4, trans_list_date_end);
				pstmt.executeUpdate();
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
		}

		@Override
		public JSONObject importPicking(String group_id, String user_id, String order_count) {
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
				isSuccess = cs.execute();
				jsonObject.put("update_count", updateCount);
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
	}

	interface ShippingProcess_interface {
		public void importDB(String group_id, String user_id, String trans_list_date_begin, String trans_list_date_end);

		public List<RealSaleVO> searchAllDB(String group_id);

		public JSONObject statisticsAlloc(String group_id, String user_id);
		
		public JSONObject importPicking(String group_id, String user_id,String order_count);
	}

}
