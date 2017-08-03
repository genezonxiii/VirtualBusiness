package tw.com.aber.invmanual.controller;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import tw.com.aber.util.Util;
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

		if ("query_invoice".equals(action)) {
			String startDate = req.getParameter("startDate");
			String endDate = req.getParameter("endDate");

			String regular = "[0-9]{4}-[0-9]{2}-[0-9]{2}";

			Util util = new Util();
			InvManualService service = new InvManualService();
			Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd").create();
			String result = null;

			if (util.checkDateFormat(startDate, regular) && util.checkDateFormat(endDate, regular)) {
				result = gson.toJson(service.searchInvManualByInvoiceDate(groupId, startDate, endDate));
			} else {
				result = gson.toJson(service.searchAllInvManual(groupId));
			}
			logger.debug("result: {}", result);
			resp.getWriter().write(result);
		}
	}

	class InvManualService {
		private InvManual_interface dao;

		public InvManualService() {
			dao = new InvManualDao();
		}

		public List<InvManualVO> searchAllInvManual(String groupId) {
			return dao.searchAllInvManual(groupId);
		}

		public List<InvManualVO> searchInvManualByInvoiceDate(String groupId, String startDate, String endDate) {
			return dao.searchInvManualByInvoiceDate(groupId, startDate, endDate);
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
					row.setInvoice_date(rs.getString("invoice_date"));
					row.setTitle(rs.getString("title"));
					row.setUnicode(rs.getString("unicode"));
					row.setAmount(rs.getString("amount"));
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
					row.setInvoice_date(rs.getString("invoice_date"));
					row.setTitle(rs.getString("title"));
					row.setUnicode(rs.getString("unicode"));
					row.setAmount(rs.getString("amount"));
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

	}
}

interface InvManual_interface {
	public List<InvManualVO> searchAllInvManual(String groupId);

	public List<InvManualVO> searchInvManualByInvoiceDate(String groupId, String startDate, String endDate);
	
	public void insertInvManual(InvManualVO invManualVO);
}
