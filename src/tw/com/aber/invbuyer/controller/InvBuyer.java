package tw.com.aber.invbuyer.controller;

import java.io.IOException;
import java.sql.CallableStatement;
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

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.google.gson.GsonBuilder;

import tw.com.aber.vo.InvBuyerVO;

interface InvBuyer_interface {
	public void insertInvBuyer(InvBuyerVO invBuyerVO);

	public List<InvBuyerVO> selectInvBuyerByUnicodeOrTitle(InvBuyerVO invBuyerVO);

	public void delInvBuyer(InvBuyerVO invBuyerVO);

	public void updateInvBuyer(InvBuyerVO invBuyerVO);
}

public class InvBuyer extends HttpServlet {

	private static final long serialVersionUID = 1L;

	private static final Logger logger = LogManager.getLogger(InvBuyer.class);

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

		InvBuyerService invBuyerService = null;
		InvBuyerVO invBuyerVO = null;
		String result = "";

		if ("insertInvBuyer".equals(action)) {

			String title = req.getParameter("title");
			String unicode = req.getParameter("unicode");
			String address = req.getParameter("address");
			String memo = req.getParameter("memo");

			try {
				invBuyerVO = new InvBuyerVO();
				invBuyerVO.setGroup_id(groupId);
				invBuyerVO.setTitle(title);
				invBuyerVO.setUnicode(unicode);
				invBuyerVO.setAddress(address);
				invBuyerVO.setMemo(memo);

				invBuyerService = new InvBuyerService();
				invBuyerService.insertInvBuyer(invBuyerVO);
			} catch (Exception e) {
				logger.error(e.getMessage());
				result = "fail";
			}
			resp.getWriter().write(result);

		} else if ("selectInvBuyerByUnicodeOrTitle".equals(action)) {

			String title = req.getParameter("title");
			String unicode = req.getParameter("unicode");

			try {
				invBuyerVO = new InvBuyerVO();
				invBuyerVO.setGroup_id(groupId);
				invBuyerVO.setTitle(title);
				invBuyerVO.setUnicode(unicode);

				invBuyerService = new InvBuyerService();
				List<InvBuyerVO> list = invBuyerService.selectInvBuyerByUnicodeOrTitle(invBuyerVO);

				result = new GsonBuilder().setDateFormat("yyyy-MM-dd").create().toJson(list);
			} catch (Exception e) {
				logger.error(e.getMessage());
				result = "{}";
			}
			resp.getWriter().write(result);
		} else if ("delInvBuyer".equals(action)) {
			String inv_buyer_ids = req.getParameter("inv_buyer_ids");

			logger.debug("inv_buyer_ids: {} ", inv_buyer_ids);

			try {
				invBuyerVO = new InvBuyerVO();
				invBuyerVO.setGroup_id(groupId);
				invBuyerVO.setInv_buyer_id(inv_buyer_ids);

				invBuyerService = new InvBuyerService();
				invBuyerService.delInvBuyer(invBuyerVO);
			} catch (Exception e) {
				logger.error(e.getMessage());
				result = "fail";
			}
			resp.getWriter().write(result);
		} else if ("updateInvBuyer".equals(action)) {
			String inv_buyer_id = req.getParameter("inv_buyer_id");
			String address = req.getParameter("address");
			String memo = req.getParameter("memo");
			String title = req.getParameter("title");
			String unicode = req.getParameter("unicode");

			logger.debug("inv_buyer_id: {} ", inv_buyer_id);

			try {
				invBuyerVO = new InvBuyerVO();
				invBuyerVO.setGroup_id(groupId);
				invBuyerVO.setInv_buyer_id(inv_buyer_id);
				invBuyerVO.setAddress(address);
				invBuyerVO.setMemo(memo);
				invBuyerVO.setTitle(title);
				invBuyerVO.setUnicode(unicode);

				invBuyerService = new InvBuyerService();
				invBuyerService.updateInvBuyer(invBuyerVO);
			} catch (Exception e) {
				logger.error(e.getMessage());
				result = "fail";
			}
			resp.getWriter().write(result);
		}
	}

	class InvBuyerService {
		private InvBuyer_interface dao;

		public InvBuyerService() {
			dao = new InvBuyerDao();
		}

		public void insertInvBuyer(InvBuyerVO invBuyerVO) {
			dao.insertInvBuyer(invBuyerVO);
		}

		public List<InvBuyerVO> selectInvBuyerByUnicodeOrTitle(InvBuyerVO invBuyerVO) {
			return dao.selectInvBuyerByUnicodeOrTitle(invBuyerVO);
		}

		public void delInvBuyer(InvBuyerVO invBuyerVO) {
			dao.delInvBuyer(invBuyerVO);
		}

		public void updateInvBuyer(InvBuyerVO invBuyerVO) {
			dao.updateInvBuyer(invBuyerVO);
		}
	}

	class InvBuyerDao implements InvBuyer_interface {
		private final String jdbcDriver = getServletConfig().getServletContext().getInitParameter("jdbcDriver");
		private final String dbURL = getServletConfig().getServletContext().getInitParameter("dbURL")
				+ "?useUnicode=true&characterEncoding=utf-8&useSSL=false";
		private final String dbUserName = getServletConfig().getServletContext().getInitParameter("dbUserName");
		private final String dbPassword = getServletConfig().getServletContext().getInitParameter("dbPassword");

		private static final String sp_insert_inv_buyer = "call sp_insert_inv_buyer (?,?,?,?,?)";
		private static final String sp_select_inv_buyer_by_unicode_or_title = "call sp_select_inv_buyer_by_unicode_or_title (?,?,?)";
		private static final String sp_del_inv_buyer = "call sp_del_inv_buyer (?,?)";
		private static final String sp_update_inv_buyer = "call sp_update_inv_buyer (?,?,?,?,?,?)";

		@Override
		public void insertInvBuyer(InvBuyerVO invBuyerVO) {
			Connection con = null;
			CallableStatement cs = null;

			try {
				Class.forName(jdbcDriver);
				con = DriverManager.getConnection(dbURL, dbUserName, dbPassword);
				cs = con.prepareCall(sp_insert_inv_buyer);

				cs.setString(1, invBuyerVO.getGroup_id());
				cs.setString(2, invBuyerVO.getTitle());
				cs.setString(3, invBuyerVO.getUnicode());
				cs.setString(4, invBuyerVO.getAddress());
				cs.setString(5, invBuyerVO.getMemo());

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
		public List<InvBuyerVO> selectInvBuyerByUnicodeOrTitle(InvBuyerVO invBuyerVO) {
			List<InvBuyerVO> rows = new ArrayList<InvBuyerVO>();
			InvBuyerVO row = null;

			Connection con = null;
			PreparedStatement pstmt = null;
			ResultSet rs = null;
			try {
				Class.forName(jdbcDriver);
				con = DriverManager.getConnection(dbURL, dbUserName, dbPassword);
				pstmt = con.prepareStatement(sp_select_inv_buyer_by_unicode_or_title);

				pstmt.setString(1, invBuyerVO.getGroup_id());
				pstmt.setString(2, invBuyerVO.getUnicode());
				pstmt.setString(3, invBuyerVO.getTitle());

				rs = pstmt.executeQuery();
				while (rs.next()) {
					row = new InvBuyerVO();
					row.setInv_buyer_id(rs.getString("inv_buyer_id"));
					row.setGroup_id(rs.getString("group_id"));
					row.setTitle(rs.getString("title"));
					row.setUnicode(rs.getString("unicode"));
					row.setAddress(rs.getString("address"));
					row.setMemo(rs.getString("memo"));
					row.setCreate_time(rs.getDate("create_time"));
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
		public void delInvBuyer(InvBuyerVO invBuyerVO) {
			Connection con = null;
			CallableStatement cs = null;

			try {
				Class.forName(jdbcDriver);
				con = DriverManager.getConnection(dbURL, dbUserName, dbPassword);
				cs = con.prepareCall(sp_del_inv_buyer);

				cs.setString(1, invBuyerVO.getInv_buyer_id());
				cs.setString(2, invBuyerVO.getGroup_id());

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
		public void updateInvBuyer(InvBuyerVO invBuyerVO) {
			Connection con = null;
			CallableStatement cs = null;

			try {
				Class.forName(jdbcDriver);
				con = DriverManager.getConnection(dbURL, dbUserName, dbPassword);
				cs = con.prepareCall(sp_update_inv_buyer);

				cs.setString(1, invBuyerVO.getInv_buyer_id());
				cs.setString(2, invBuyerVO.getGroup_id());
				cs.setString(3, invBuyerVO.getTitle());
				cs.setString(4, invBuyerVO.getUnicode());
				cs.setString(5, invBuyerVO.getAddress());
				cs.setString(6, invBuyerVO.getMemo());

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
	}
}
