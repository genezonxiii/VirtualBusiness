package tw.com.aber.invmanual.controller;

import java.io.IOException;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.Date;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
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

import tw.com.aber.util.Util;
import tw.com.aber.vo.InvManualDetailVO;
import tw.com.aber.vo.InvManualVO;

public class InvManual extends HttpServlet {

	private static final long serialVersionUID = 1L;
	private static final Logger logger = LogManager.getLogger(InvManual.class);

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		doPost(req, resp);
	}

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

		req.setCharacterEncoding("UTF-8");
		resp.setCharacterEncoding("UTF-8");

		String groupId = (String) req.getSession().getAttribute("group_id");
		String userId = (String) req.getSession().getAttribute("user_id");
		String action = req.getParameter("action");

		logger.debug("Action: {} \\ GroupId: {} \\ UserId: {}", action, groupId, userId);

		Util util = new Util();
		InvManualService service = new InvManualService();
		Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd").create();

		if ("query_invoice".equals(action)) {
			String startDate = req.getParameter("startDate");
			String endDate = req.getParameter("endDate");

			String regular = "[0-9]{4}-[0-9]{2}-[0-9]{2}";

			String result = null;

			if (util.checkDateFormat(startDate, regular) && util.checkDateFormat(endDate, regular)) {
				result = gson.toJson(service.searchInvManualByInvoiceDate(groupId, startDate, endDate));
			} else {
				result = gson.toJson(service.searchAllInvManual(groupId));
			}
			logger.debug("result: {}", result);
			resp.getWriter().write(result);
		} else if ("insertMaster".equals(action)) {
			String invoice_type = req.getParameter("invoice_type");
			String title = req.getParameter("title");
			String unicode = req.getParameter("unicode");
			String invoice_no = req.getParameter("invoice_no");
			String year_month = null;

			java.util.Date date = new java.util.Date();
			Date invoice_date = null;
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
			try {
				invoice_date = new java.sql.Date(date.getTime());
				year_month = sdf.format(invoice_date);

				year_month = year_month.substring(5, 7);
				int year_month_int = Integer.valueOf(year_month);

				Integer start = null, end = null;
				if (year_month_int % 2 != 0) {
					start = year_month_int;
					end = year_month_int + 1;
				} else {
					start = year_month_int - 1;
					end = year_month_int;
				}
				year_month = "" + start + "-" + end;
			} catch (Exception e) {
				logger.error(e.getMessage());
			}

			Float amount = 0F;

			InvManualVO invManualVO = new InvManualVO();
			invManualVO.setGroup_id(groupId);
			invManualVO.setInvoice_type(invoice_type);
			invManualVO.setYear_month(year_month);
			invManualVO.setInvoice_no(invoice_no);
			invManualVO.setInvoice_date(invoice_date);
			invManualVO.setTitle(title);
			invManualVO.setUnicode(unicode);
			invManualVO.setAmount(amount);
			service.insertInvManual(invManualVO);

		} else if ("query_invoice_detail".equals(action)) {
			String inv_manual_id = req.getParameter("inv_manual_id");

			String result = null;
			try {
				InvManualDetailVO invManualDetailVO = new InvManualDetailVO();
				invManualDetailVO.setGroup_id(groupId);
				invManualDetailVO.setInv_manual_id(inv_manual_id);
				result = gson.toJson(service.searchInvManualDetailByInvManualId(invManualDetailVO));
				logger.debug("result: {}", result);
			} catch (Exception e) {
				logger.error(e.getMessage());
			}
			resp.getWriter().write(result);
		} else if ("insertDetail".equals(action)) {
			String inv_manual_id = req.getParameter("inv_manual_id");
			String price = req.getParameter("price");
			String quantity = req.getParameter("quantity");
			String description = req.getParameter("description");
			String subtotal = req.getParameter("subtotal");
			logger.debug("inv_manual_id: {} \\ price: {} \\ quantity: {} \\ description: {} \\ subtotal: {}",
					inv_manual_id, price, quantity, description, subtotal);
			InvManualDetailVO invManualDetailVO = new InvManualDetailVO();
			invManualDetailVO.setGroup_id(groupId);
			invManualDetailVO.setInv_manual_id(inv_manual_id);
			invManualDetailVO.setPrice(Integer.valueOf(price));
			invManualDetailVO.setQuantity(Integer.valueOf(quantity));
			invManualDetailVO.setDescription(description);
			invManualDetailVO.setSubtotal(Integer.valueOf(subtotal));
			service.insertInvManualDetail(invManualDetailVO);

		} else if ("delete_invoice_detail".equals(action)) {
			String[] detail_ids = req.getParameter("inv_manual_detail_id").split(",");
			String inv_manual_id = req.getParameter("inv_manual_id");
			String result = "OK";
			try {
				InvManualDetailVO invManualDetailVO = new InvManualDetailVO();
				invManualDetailVO.setInv_manual_id(inv_manual_id);
				invManualDetailVO.setGroup_id(groupId);

				for (int i = 0; i < detail_ids.length; i++) {
					invManualDetailVO.setInv_manual_detail_id(detail_ids[i]);
					service.delInvManualDetail(invManualDetailVO);
				}
			} catch (Exception e) {
				logger.error(e.getMessage());
				result = "ERROR";
			}
			logger.debug("inv_manual_id: {} \\ result: {}", inv_manual_id, result);
			resp.getWriter().write(result);
		} else if ("delete_invoice".equals(action)) {
			String[] master_ids = req.getParameter("inv_manual_id").split(",");
			String result = "OK";
			try {
				InvManualVO invManualVO = new InvManualVO();
				invManualVO.setGroup_id(groupId);

				for (int i = 0; i < master_ids.length; i++) {
					invManualVO.setInv_manual_id(master_ids[i]);
					service.delInvManual(invManualVO);
				}
			} catch (Exception e) {
				logger.error(e.getMessage());
				result = "ERROR";
			}
			resp.getWriter().write(result);
		}
	}

	class InvManualService {
		private InvManual_interface dao;

		public InvManualService() {
			dao = new InvManualDao();
		}

		public List<InvManualDetailVO> searchInvManualDetailByInvManualId(InvManualDetailVO invManualDetailVO) {
			return dao.searchInvManualDetailByInvManualId(invManualDetailVO);
		}

		public List<InvManualVO> searchAllInvManual(String groupId) {
			return dao.searchAllInvManual(groupId);
		}

		public List<InvManualVO> searchInvManualByInvoiceDate(String groupId, String startDate, String endDate) {
			return dao.searchInvManualByInvoiceDate(groupId, startDate, endDate);
		}

		public void insertInvManual(InvManualVO invManualVO) {
			dao.insertInvManual(invManualVO);
		}

		public void insertInvManualDetail(InvManualDetailVO invManualDetailVO) {
			dao.insertInvManualDetail(invManualDetailVO);
		}

		public void delInvManualDetail(InvManualDetailVO invManualDetailVO) {
			dao.delInvManualDetail(invManualDetailVO);
		}

		public void delInvManual(InvManualVO invManualVO) {
			dao.delInvManual(invManualVO);
		}
	}

	class InvManualDao implements InvManual_interface {
		private final String jdbcDriver = getServletConfig().getServletContext().getInitParameter("jdbcDriver");
		private final String dbURL = getServletConfig().getServletContext().getInitParameter("dbURL")
				+ "?useUnicode=true&characterEncoding=utf-8&useSSL=false";
		private final String dbUserName = getServletConfig().getServletContext().getInitParameter("dbUserName");
		private final String dbPassword = getServletConfig().getServletContext().getInitParameter("dbPassword");

		private static final String sp_select_all_inv_manual = "call sp_select_all_inv_manual (?)";
		private static final String sp_select_inv_manual_by_invoice_date = "call sp_select_inv_manual_by_invoice_date (?,?,?)";
		private static final String sp_insert_inv_manual = "call sp_insert_inv_manual(?,?,?,?,?,?,?,?)";
		private static final String sp_select_inv_manual_detail_by_inv_manual_id = "call sp_select_inv_manual_detail_by_inv_manual_id(?,?)";
		private static final String sp_insert_inv_manual_detail = "call sp_insert_inv_manual_detail(?,?,?,?,?,?)";
		private static final String sp_del_inv_manual_detail = "call sp_del_inv_manual_detail(?,?,?)";
		private static final String sp_del_inv_manual = "call sp_del_inv_manual(?,?)";

		@Override
		public List<InvManualVO> searchAllInvManual(String groupId) {
			List<InvManualVO> rows = new ArrayList<InvManualVO>();
			InvManualVO row = null;

			Connection con = null;
			PreparedStatement pstmt = null;
			ResultSet rs = null;
			try {
				Class.forName(jdbcDriver);
				con = DriverManager.getConnection(dbURL, dbUserName, dbPassword);
				pstmt = con.prepareStatement(sp_select_all_inv_manual);

				pstmt.setString(1, groupId);

				rs = pstmt.executeQuery();
				while (rs.next()) {
					row = new InvManualVO();
					row.setInv_manual_id(rs.getString("inv_manual_id"));
					row.setGroup_id(rs.getString("group_id"));
					row.setInvoice_type(rs.getString("invoice_type"));
					row.setYear_month(rs.getString("year_month"));
					row.setInvoice_no(rs.getString("invoice_no"));
					row.setInvoice_date(rs.getDate("invoice_date"));
					row.setTitle(rs.getString("title"));
					row.setUnicode(rs.getString("unicode"));
					row.setAmount(rs.getFloat("amount"));
					rows.add(row);
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
			return rows;
		}

		@Override
		public List<InvManualVO> searchInvManualByInvoiceDate(String groupId, String startDate, String endDate) {
			List<InvManualVO> rows = new ArrayList<InvManualVO>();
			InvManualVO row = null;

			Connection con = null;
			PreparedStatement pstmt = null;
			ResultSet rs = null;
			try {
				Class.forName(jdbcDriver);
				con = DriverManager.getConnection(dbURL, dbUserName, dbPassword);
				pstmt = con.prepareStatement(sp_select_inv_manual_by_invoice_date);

				pstmt.setString(1, groupId);
				pstmt.setString(2, startDate);
				pstmt.setString(3, endDate);

				rs = pstmt.executeQuery();
				while (rs.next()) {
					row = new InvManualVO();
					row.setInv_manual_id(rs.getString("inv_manual_id"));
					row.setGroup_id(rs.getString("group_id"));
					row.setInvoice_type(rs.getString("invoice_type"));
					row.setYear_month(rs.getString("year_month"));
					row.setInvoice_no(rs.getString("invoice_no"));
					row.setInvoice_date(rs.getDate("invoice_date"));
					row.setTitle(rs.getString("title"));
					row.setUnicode(rs.getString("unicode"));
					row.setAmount(rs.getFloat("amount"));
					rows.add(row);
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
			return rows;
		}

		@Override
		public void insertInvManual(InvManualVO invManualVO) {
			Connection con = null;
			CallableStatement cs = null;

			try {
				Class.forName(jdbcDriver);
				con = DriverManager.getConnection(dbURL, dbUserName, dbPassword);
				cs = con.prepareCall(sp_insert_inv_manual);

				cs.setString(1, invManualVO.getGroup_id());
				cs.setString(2, invManualVO.getInvoice_type());
				cs.setString(3, invManualVO.getYear_month());
				cs.setString(4, invManualVO.getInvoice_no());
				cs.setDate(5, invManualVO.getInvoice_date());
				cs.setString(6, invManualVO.getTitle());
				cs.setString(7, invManualVO.getUnicode());
				cs.setFloat(8, invManualVO.getAmount());

				cs.execute();
			} catch (SQLException se) {
				throw new RuntimeException("A database error occured. " + se.getMessage());
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
		public List<InvManualDetailVO> searchInvManualDetailByInvManualId(InvManualDetailVO invManualDetailVO) {
			List<InvManualDetailVO> rows = new ArrayList<InvManualDetailVO>();
			InvManualDetailVO row = null;

			Connection con = null;
			PreparedStatement pstmt = null;
			ResultSet rs = null;
			try {
				Class.forName(jdbcDriver);
				con = DriverManager.getConnection(dbURL, dbUserName, dbPassword);
				pstmt = con.prepareStatement(sp_select_inv_manual_detail_by_inv_manual_id);

				pstmt.setString(1, invManualDetailVO.getGroup_id());
				pstmt.setString(2, invManualDetailVO.getInv_manual_id());

				rs = pstmt.executeQuery();
				while (rs.next()) {
					row = new InvManualDetailVO();
					row.setInv_manual_detail_id(rs.getString("inv_manual_detail_id"));
					row.setInv_manual_id(rs.getString("inv_manual_id"));
					row.setDescription(rs.getString("description"));
					row.setQuantity(rs.getInt("quantity"));
					row.setPrice(rs.getInt("price"));
					row.setSubtotal(rs.getInt("subtotal"));
					rows.add(row);
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
			return rows;
		}

		@Override
		public void insertInvManualDetail(InvManualDetailVO invManualDetailVO) {
			Connection con = null;
			CallableStatement cs = null;

			try {
				Class.forName(jdbcDriver);
				con = DriverManager.getConnection(dbURL, dbUserName, dbPassword);
				cs = con.prepareCall(sp_insert_inv_manual_detail);

				cs.setString(1, invManualDetailVO.getInv_manual_id());
				cs.setString(2, invManualDetailVO.getGroup_id());
				cs.setString(3, invManualDetailVO.getDescription());
				cs.setInt(4, invManualDetailVO.getPrice());
				cs.setInt(5, invManualDetailVO.getQuantity());
				cs.setInt(6, invManualDetailVO.getSubtotal());

				cs.execute();
			} catch (SQLException se) {
				throw new RuntimeException("A database error occured. " + se.getMessage());
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
		public void delInvManualDetail(InvManualDetailVO invManualDetailVO) {
			Connection con = null;
			PreparedStatement pstmt = null;
			try {
				Class.forName(jdbcDriver);
				con = DriverManager.getConnection(dbURL, dbUserName, dbPassword);
				pstmt = con.prepareStatement(sp_del_inv_manual_detail);
				pstmt.setString(1, invManualDetailVO.getInv_manual_detail_id());
				pstmt.setString(2, invManualDetailVO.getInv_manual_id());
				pstmt.setString(3, invManualDetailVO.getGroup_id());

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
		public void delInvManual(InvManualVO invManualVO) {
			Connection con = null;
			PreparedStatement pstmt = null;
			try {
				Class.forName(jdbcDriver);
				con = DriverManager.getConnection(dbURL, dbUserName, dbPassword);
				pstmt = con.prepareStatement(sp_del_inv_manual);
				pstmt.setString(1, invManualVO.getInv_manual_id());
				pstmt.setString(2, invManualVO.getGroup_id());

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

	}
}

interface InvManual_interface {
	public List<InvManualVO> searchAllInvManual(String groupId);

	public List<InvManualDetailVO> searchInvManualDetailByInvManualId(InvManualDetailVO invManualDetailVO);

	public List<InvManualVO> searchInvManualByInvoiceDate(String groupId, String startDate, String endDate);

	public void insertInvManual(InvManualVO invManualVO);

	public void insertInvManualDetail(InvManualDetailVO invManualDetailVO);

	public void delInvManualDetail(InvManualDetailVO invManualDetailVO);

	public void delInvManual(InvManualVO invManualVO);

}
