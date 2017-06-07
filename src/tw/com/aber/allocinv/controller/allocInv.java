package tw.com.aber.allocinv.controller;

import java.io.IOException;
import java.lang.reflect.Type;
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
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import tw.com.aber.basicdataimport.controller.BasicDataImport;
import tw.com.aber.vo.AllocInvVo;
import tw.com.aber.vo.PurchaseDetailVO;
import tw.com.aber.vo.PurchaseVO;
import tw.com.aber.vo.SaleVO;

public class allocInv extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private static final Logger logger = LogManager.getLogger(allocInv.class);

	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		doPost(request, response);
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		request.setCharacterEncoding("UTF-8");
		response.setCharacterEncoding("UTF-8");

		String groupId = (String) request.getSession().getAttribute("group_id");
		String userId = (String) request.getSession().getAttribute("user_id");

		String action = request.getParameter("action");
		logger.debug("Action:".concat(action));

		AllocInvService service = null;
		Gson gson = null;
		String jsonStr = null;
		List<AllocInvVo> list = null;

		if ("getAll".equals(action)) {
			service = new AllocInvService();
			list = service.getAllData(groupId);

			gson = new GsonBuilder().setDateFormat("yyyy-MM-dd").create();
			jsonStr = gson.toJson(list);
			logger.debug(jsonStr);

			response.getWriter().write(jsonStr);
		} else if ("getGroup".equals(action)) {
			service = new AllocInvService();
			list = service.getGroupData(groupId);

			gson = new GsonBuilder().setDateFormat("yyyy-MM-dd").create();
			jsonStr = gson.toJson(list);
			logger.debug(jsonStr);

			response.getWriter().write(jsonStr);
		} else if ("doPurchases".equals(action)) {
			service = new AllocInvService();
			String seqNo = service.getPurchaseSeqNo(groupId);
			String jsonList = request.getParameter("jsonList");

			gson = new GsonBuilder().setDateFormat("yyyy-MM-dd").create();
			Type type = new TypeToken<List<AllocInvVo>>() {
			}.getType();
			List<AllocInvVo> allocInvVos = gson.fromJson(jsonList, type);

			String supplyId = allocInvVos.get(0).getSupply_id();

			PurchaseVO purchaseVO = new PurchaseVO();
			purchaseVO.setSeq_no(seqNo);
			purchaseVO.setGroup_id(groupId);
			purchaseVO.setUser_id(userId);
			purchaseVO.setSupply_id(supplyId);

			String purchaseId = service.addPurchase(purchaseVO);
			logger.debug("purchaseId: " + purchaseId);
			List<PurchaseDetailVO> purchaseDetailVOs = new ArrayList<PurchaseDetailVO>();
			for (AllocInvVo allocInvVo : allocInvVos) {
				PurchaseDetailVO purchaseDetailVO = new PurchaseDetailVO();
				purchaseDetailVO.setPurchase_id(purchaseId);
				purchaseDetailVO.setGroup_id(groupId);
				purchaseDetailVO.setUser_id(userId);
				purchaseDetailVO.setProduct_id(allocInvVo.getProduct_id());
				purchaseDetailVO.setC_product_id(allocInvVo.getC_product_id());
				purchaseDetailVO.setProduct_name(allocInvVo.getProduct_name());
				purchaseDetailVO.setQuantity((int) (allocInvVo.getQuantity() - allocInvVo.getAlloc_qty()));

				purchaseDetailVOs.add(purchaseDetailVO);
			}
			service.addPurchaseDetail(purchaseDetailVOs);
		}
	}

	class AllocInvService {
		private allocInv_interface dao;

		public AllocInvService() {
			dao = new AllocInvDao();
		}

		public List<AllocInvVo> getAllData(String groupId) {
			return dao.getAllData(groupId);
		}

		public List<AllocInvVo> getGroupData(String groupId) {
			return dao.getGroupData(groupId);
		}

		public String getPurchaseSeqNo(String groupId) {
			return dao.getPurchaseSeqNo(groupId);
		}

		public String addPurchase(PurchaseVO purchaseVO) {
			return dao.doPurchase(purchaseVO);
		}

		public void addPurchaseDetail(List<PurchaseDetailVO> purchaseDetailVOs) {
			dao.doPurchaseDetail(purchaseDetailVOs);
		}
	}

	class AllocInvDao implements allocInv_interface {
		private final String jdbcDriver = getServletConfig().getServletContext().getInitParameter("jdbcDriver");
		private final String dbUserName = getServletConfig().getServletContext().getInitParameter("dbUserName");
		private final String dbPassword = getServletConfig().getServletContext().getInitParameter("dbPassword");
		private final String dbURL = getServletConfig().getServletContext().getInitParameter("dbURL")
				+ "?useUnicode=true&characterEncoding=utf-8&useSSL=false";

		private static final String sp_select_all_alloc_inv = "call sp_select_all_alloc_inv (?)";
		private static final String sp_select_group_alloc_inv = "call sp_select_group_alloc_inv (?)";
		private static final String sp_insert_allocinv_to_purchase = "call sp_insert_allocinv_to_purchase (?,?,?,?,?)";
		private static final String sp_insert_allocinv_to_purchase_detail = "call sp_insert_allocinv_to_purchase_detail(?)";
		private static final String sp_get_purchase_newseqno = "call sp_get_purchase_newseqno (?,?)";

		@Override
		public List<AllocInvVo> getAllData(String group_id) {
			List<AllocInvVo> list = new ArrayList<AllocInvVo>();
			AllocInvVo allocInvVo = null;

			Connection con = null;
			PreparedStatement pstmt = null;
			ResultSet rs = null;
			try {
				Class.forName(jdbcDriver);
				con = DriverManager.getConnection(dbURL, dbUserName, dbPassword);
				pstmt = con.prepareStatement(sp_select_all_alloc_inv);
				pstmt.setString(1, group_id);
				rs = pstmt.executeQuery();
				while (rs.next()) {
					allocInvVo = new AllocInvVo();
					allocInvVo.setAlloc_id(rs.getString("alloc_id"));
					allocInvVo.setRealsaleDetail_id(rs.getString("realsaleDetail_id"));
					allocInvVo.setGroup_id(rs.getString("group_id"));
					allocInvVo.setOrder_no(rs.getString("order_no"));
					allocInvVo.setProduct_id(rs.getString("product_id"));
					allocInvVo.setC_product_id(rs.getString("c_product_id"));
					allocInvVo.setQuantity(rs.getInt("quantity"));
					allocInvVo.setPrice(rs.getFloat("price"));
					allocInvVo.setLocation_id(rs.getString("location_id"));
					allocInvVo.setAlloc_qty(rs.getInt("alloc_qty"));
					allocInvVo.setAlloc_time(rs.getDate("alloc_time"));
					allocInvVo.setCheckin_time(rs.getDate("checkin_time"));
					allocInvVo.setProduct_name(rs.getString("product_name"));
					allocInvVo.setSupply_id(rs.getString("supply_id"));
					allocInvVo.setSupply_name(rs.getString("supply_name"));
					list.add(allocInvVo); // Store the row in the list
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
		public List<AllocInvVo> getGroupData(String group_id) {
			List<AllocInvVo> list = new ArrayList<AllocInvVo>();
			AllocInvVo allocInvVo = null;

			Connection con = null;
			PreparedStatement pstmt = null;
			ResultSet rs = null;
			try {
				Class.forName(jdbcDriver);
				con = DriverManager.getConnection(dbURL, dbUserName, dbPassword);
				pstmt = con.prepareStatement(sp_select_group_alloc_inv);
				pstmt.setString(1, group_id);
				rs = pstmt.executeQuery();
				while (rs.next()) {
					allocInvVo = new AllocInvVo();
					allocInvVo.setProduct_id(rs.getString("product_id"));
					allocInvVo.setC_product_id(rs.getString("c_product_id"));
					allocInvVo.setQuantity(rs.getInt("quantity"));
					allocInvVo.setAlloc_qty(rs.getInt("alloc_qty"));
					allocInvVo.setProduct_name(rs.getString("product_name"));
					allocInvVo.setSupply_id(rs.getString("supply_id"));
					allocInvVo.setSupply_name(rs.getString("supply_name"));
					list.add(allocInvVo); // Store the row in the list
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
		public String doPurchase(PurchaseVO purchaseVO) {
			Connection con = null;
			CallableStatement cs = null;
			String result = null;
			try {
				Class.forName(jdbcDriver);
				con = DriverManager.getConnection(dbURL, dbUserName, dbPassword);
				cs = con.prepareCall(sp_insert_allocinv_to_purchase);

				cs.setString(1, purchaseVO.getSeq_no());
				cs.setString(2, purchaseVO.getGroup_id());
				cs.setString(3, purchaseVO.getUser_id());
				cs.setString(4, purchaseVO.getSupply_id());
				cs.registerOutParameter(5, Types.CHAR);
				cs.execute();

				result = cs.getString(5);

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
			return result;
		}

		@Override
		public String getPurchaseSeqNo(String groupId) {
			Connection con = null;
			CallableStatement cs = null;
			String result = null;
			try {
				Class.forName(jdbcDriver);
				con = DriverManager.getConnection(dbURL, dbUserName, dbPassword);
				cs = con.prepareCall(sp_get_purchase_newseqno);

				cs.setString(1, groupId);
				cs.registerOutParameter(2, Types.VARCHAR);
				cs.execute();

				result = cs.getString(2);

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
			return result;
		}

		@Override
		public void doPurchaseDetail(List<PurchaseDetailVO> purchaseDetailVOs) {

			String sql = "VALUES";

			for (PurchaseDetailVO purchaseDetailVO : purchaseDetailVOs) {
				sql = sql.concat("(UUID(),'").concat(purchaseDetailVO.getPurchase_id() + "','")
						.concat(purchaseDetailVO.getGroup_id() + "','").concat(purchaseDetailVO.getUser_id() + "','")
						.concat(purchaseDetailVO.getProduct_id() + "','")
						.concat(purchaseDetailVO.getC_product_id() + "','")
						.concat(purchaseDetailVO.getProduct_name() + "','")
						.concat(purchaseDetailVO.getQuantity() + "',").concat("0,NULL,NULL,false),");
			}
			sql = sql.substring(0, sql.length() - 1);
			Connection con = null;
			CallableStatement cs = null;
			try {
				Class.forName(jdbcDriver);
				logger.debug("\nsql: {}",sql);
				con = DriverManager.getConnection(dbURL, dbUserName, dbPassword);
				 cs = con.prepareCall(sp_insert_allocinv_to_purchase_detail);
				
				 cs.setString(1, sql);
				 cs.execute();

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

	}
}

interface allocInv_interface {
	public String getPurchaseSeqNo(String group_id);

	public String doPurchase(PurchaseVO purchaseVO);

	public void doPurchaseDetail(List<PurchaseDetailVO> purchaseDetailVOs);

	public List<AllocInvVo> getAllData(String group_id);

	public List<AllocInvVo> getGroupData(String group_id);
}