package tw.com.aber.stockmod.controller;

import java.io.IOException;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.Date;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
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
import tw.com.aber.vo.LocationVO;
import tw.com.aber.vo.StockModDetailVO;
import tw.com.aber.vo.StockModTypeVO;
import tw.com.aber.vo.StockModVO;

public class stockMod extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private static final Logger logger = LogManager.getLogger(stockMod.class);

	private Util util;

	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		request.setCharacterEncoding("UTF-8");
		response.setCharacterEncoding("UTF-8");

		StockModService stockModService = null;
		StockModVO stockModVO = null;
		StockModDetailVO stockModDetailVO = null;
		List<StockModVO> master_rows = null;
		List<StockModDetailVO> detail_rows = null;
		Gson gson = null;
		String jsonStr = null;

		String action = request.getParameter("action");
		String groupId = request.getSession().getAttribute("group_id").toString();
		String userId = request.getSession().getAttribute("user_id").toString();

		if ("searchByNo".equals(action)) {
			try {
				stockModService = new StockModService();
				stockModVO = new StockModVO();

				String stockmodNo = request.getParameter("stockmodNo");

				gson = new GsonBuilder().setDateFormat("yyyy-MM-dd").create();

				stockModVO.setGroup_id(groupId);

				if (stockmodNo == null || "".equals(stockmodNo)) {
					stockModVO.setStockmod_no(stockmodNo);
					master_rows = stockModService.getSearchAllDB(stockModVO);
				} else {
					master_rows = stockModService.getSearchDB(stockModVO);
				}

				jsonStr = gson.toJson(master_rows);
				response.getWriter().write(jsonStr);
			} catch (Exception e) {
				logger.error("search exception: ".concat(e.getMessage()));
			}
		} else if ("searchByDate".equals(action)) {
			try {
				stockModService = new StockModService();
				stockModVO = new StockModVO();

				String startDate = request.getParameter("startDate");
				String endDate = request.getParameter("endDate");

				gson = new GsonBuilder().setDateFormat("yyyy-MM-dd").create();

				stockModVO.setGroup_id(groupId);
				stockModVO.setStartDate(startDate);
				stockModVO.setEndDate(endDate);

				master_rows = stockModService.getSearchDBByDate(stockModVO);

				jsonStr = gson.toJson(master_rows);
				response.getWriter().write(jsonStr);
			} catch (Exception e) {
				logger.error("search exception: ".concat(e.getMessage()));
			}
		} else if ("getModType".equals(action)) {
			stockModService = new StockModService();
			jsonStr = stockModService.getModType();
			response.getWriter().write(jsonStr);
		} else if ("insertStockMod".equals(action)) {
			stockModService = new StockModService();
			stockModVO = new StockModVO();

			String stockmodTime = request.getParameter("stockmodTime");
			String stockmodType = request.getParameter("stockmodType");
			String processFlag = "N";
			String memo = request.getParameter("memo");

			logger.debug("\nTime:{}\nType:{}\nmemo:{}", stockmodTime, stockmodType, memo);

			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
			java.sql.Date stockmod_time = null;
			try {
				java.util.Date date = sdf.parse(stockmodTime);
				stockmod_time = new java.sql.Date(date.getTime());
			} catch (ParseException e) {
				logger.error("stockmod_time convert :".concat(e.getMessage()));
			}
			String ref_id = "";

			stockModVO.setGroup_id(groupId);
			stockModVO.setStockmod_time(stockmod_time);
			stockModVO.setRef_id(ref_id);
			stockModVO.setStockmod_type(stockmodType);
			stockModVO.setProcess_flag(processFlag);
			stockModVO.setMemo(memo);
			stockModVO.setCreate_user(userId);
			stockModVO.setProcess_user(null);
			stockModVO.setProcess_time(null);

			String stockmodNo = stockModService.insertToDB(stockModVO);
			logger.debug("stockmodNo: ".concat(stockmodNo));
			response.getWriter().write(stockmodNo);
		} else if ("deleteStockMod".equals(action)) {
			stockModService = new StockModService();
			stockModVO = new StockModVO();

			String[] delArr = request.getParameter("stockmodId").split(",");
			if (delArr.length > 0) {
				for (String id : delArr) {
					stockModVO.setStockmod_id(id);
					stockModVO.setCreate_user(userId);
					stockModService.deleteDB(stockModVO);
				}
			} else {
				String stockmodId = request.getParameter("stockmodId");
				stockModVO.setStockmod_id(stockmodId);
				stockModVO.setCreate_user(userId);
				stockModService.deleteDB(stockModVO);
			}

		} else if ("updateStockMod".equals(action)) {
			stockModService = new StockModService();
			stockModVO = new StockModVO();

			String stockmodNo = request.getParameter("stockmodNo");
			String stockmodTime = request.getParameter("stockmodTime");
			String stockmodType = request.getParameter("stockmodType");
			String memo = request.getParameter("memo");
			String stockmodId = request.getParameter("stockmodId");

			logger.debug("\nId:{}\nTime:{}\nType:{}\nmemo:{}", stockmodId, stockmodTime, stockmodType, memo);

			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
			java.sql.Date stockmod_time = null;
			try {
				java.util.Date date = sdf.parse(stockmodTime);
				stockmod_time = new java.sql.Date(date.getTime());
			} catch (ParseException e) {
				logger.error("stockmod_time convert :".concat(e.getMessage()));
			}
			String ref_id = "";

			stockModVO.setStockmod_no(stockmodNo);
			stockModVO.setStockmod_id(stockmodId);
			stockModVO.setGroup_id(groupId);
			stockModVO.setStockmod_time(stockmod_time);
			stockModVO.setRef_id(ref_id);
			stockModVO.setStockmod_type(stockmodType);
			stockModVO.setMemo(memo);
			stockModVO.setCreate_user(userId);
			stockModVO.setProcess_user(null);
			stockModVO.setProcess_time(null);

			stockModService.updateToDB(stockModVO);

			response.getWriter().write(stockmodNo);
		} else if ("searchDetailById".equals(action)) {
			stockModService = new StockModService();

			String stockmodId = request.getParameter("stockmodId");

			stockModDetailVO = new StockModDetailVO();
			stockModDetailVO.setGroup_id(groupId);
			stockModDetailVO.setStockmod_id(stockmodId);

			gson = new GsonBuilder().setDateFormat("yyyy-MM-dd").create();

			detail_rows = stockModService.getSearchAllDetailDB(stockModDetailVO);

			jsonStr = gson.toJson(detail_rows);
			response.getWriter().write(jsonStr);
		} else if ("getDetailProductInfo".equals(action)) {
			stockModService = new StockModService();

			stockModDetailVO = new StockModDetailVO();
			stockModDetailVO.setGroup_id(groupId);

			detail_rows = stockModService.getDetailProductInfoDB(stockModDetailVO);

			gson = new GsonBuilder().setDateFormat("yyyy-MM-dd").create();

			jsonStr = gson.toJson(detail_rows);
			response.getWriter().write(jsonStr);
		} else if ("getDetailLocationInfo".equals(action)) {
			stockModService = new StockModService();

			LocationVO locationVO = new LocationVO();
			locationVO.setGroup_id(groupId);

			List<LocationVO> rows = stockModService.getDetailLocationInfoDB(locationVO);

			gson = new GsonBuilder().setDateFormat("yyyy-MM-dd").create();

			jsonStr = gson.toJson(rows);
			response.getWriter().write(jsonStr);
		}

	}

	/*************************** 自訂方法 ****************************************/
	// 處理傳過來的日期格式
	public int DateConversionToDigital(String Date) {

		StringBuffer str = new StringBuffer();
		String[] dateArray = Date.split("-");
		for (String i : dateArray) {
			str.append(i);
		}
		return Integer.parseInt(str.toString());
	}

	// 獲得格式過的今年金月今日
	public String getThisYearMonthDate() {
		Calendar cal = Calendar.getInstance();
		String year = String.valueOf(cal.get(Calendar.YEAR));
		String month = String.valueOf(cal.get(Calendar.MONTH) + 1);
		String date = String.valueOf(cal.get(Calendar.DATE));
		return year + formatTime(month) + formatTime(date);
	}

	// 處理個位數月份以及日
	private String formatTime(String str) {
		int counter = 0;
		for (int i = str.length() - 1; i >= 0; i--) {
			counter++;
		}
		if (counter == 1) {
			str = "0" + str;
		}
		return str;
	}

	// 格式化單號，不足四位補位
	private String formatSeqNo(int str) {
		String seqNo = String.valueOf(str);
		StringBuffer buf = new StringBuffer();
		buf.append(seqNo);
		int counter = 0;
		for (int i = 0; i < seqNo.length(); i++) {
			counter++;
		}
		if (counter < 5) {
			for (int j = 0; j < (4 - counter); j++) {
				buf.insert(0, "0");
			}
		}
		return buf.toString();
	}

	public String getGenerateSeqNo(String str) {
		str = str.substring(str.length() - 4);
		return getThisYearMonthDate() + formatSeqNo((Integer.valueOf(str) + 1));
	}

	/*************************** 制定規章方法 ****************************************/
	interface stockMod_interface {

		public List<StockModVO> searchDB(StockModVO stockModVO);

		public List<StockModVO> searchAllDB(StockModVO stockModVO);

		public List<StockModDetailVO> searchAllDetailDB(StockModDetailVO stockModDetailVO);

		public List<StockModDetailVO> getDetailProductInfoDB(StockModDetailVO stockModDetailVO);

		public List<LocationVO> getDetailLocationInfoDB(LocationVO locationVO);

		public List<StockModVO> searchDBByDate(StockModVO stockModVO);

		public String searchModType();

		public String getNewNo(StockModVO stockModVO);

		public String insertDB(StockModVO stockModVO);

		public void updateDB(StockModVO stockModVO);

		public void deleteDB(StockModVO stockModVO);

	}

	/*************************** 處理業務邏輯 ****************************************/
	class StockModService {
		private stockMod_interface dao;

		public StockModService() {
			dao = new StockModDAO();
		}

		public List<StockModVO> getSearchDB(StockModVO stockModVO) {
			return dao.searchDB(stockModVO);
		}

		public List<StockModVO> getSearchAllDB(StockModVO stockModVO) {
			return dao.searchAllDB(stockModVO);
		}

		public List<LocationVO> getDetailLocationInfoDB(LocationVO locationVO) {
			return dao.getDetailLocationInfoDB(locationVO);
		}

		public List<StockModDetailVO> getSearchAllDetailDB(StockModDetailVO stockModDetailVO) {
			return dao.searchAllDetailDB(stockModDetailVO);
		}

		public List<StockModDetailVO> getDetailProductInfoDB(StockModDetailVO stockModDetailVO) {
			return dao.getDetailProductInfoDB(stockModDetailVO);
		}

		public List<StockModVO> getSearchDBByDate(StockModVO stockModVO) {
			return dao.searchDBByDate(stockModVO);
		}

		public String getModType() {
			return dao.searchModType();
		}

		public String insertToDB(StockModVO stockModVO) {
			return dao.insertDB(stockModVO);
		}

		public void updateToDB(StockModVO stockModVO) {
			dao.updateDB(stockModVO);
		}

		public void deleteDB(StockModVO stockModVO) {
			dao.deleteDB(stockModVO);
		}
	}

	class StockModDAO implements stockMod_interface {
		private final String jdbcDriver = getServletConfig().getServletContext().getInitParameter("jdbcDriver");
		private final String dbURL = getServletConfig().getServletContext().getInitParameter("dbURL")
				+ "?useUnicode=true&characterEncoding=utf-8&useSSL=false";
		private final String dbUserName = getServletConfig().getServletContext().getInitParameter("dbUserName");
		private final String dbPassword = getServletConfig().getServletContext().getInitParameter("dbPassword");

		private static final String sp_get_stock_mod_new_no = "call sp_get_stock_mod_new_no (?,?)";
		private static final String sp_insert_stock_mod = "call sp_insert_stock_mod (?,?,?,?,?,?,?,?,?,?,?)";
		private static final String sp_select_all_stock_mod = "call sp_select_all_stock_mod (?)";
		private static final String sp_select_stock_mod_by_no = "call sp_select_stock_mod_by_no (?,?)";
		private static final String sp_get_stock_mod_type = "call sp_get_stock_mod_type ()";
		private static final String sp_select_stock_mod_by_stockmod_time = "call sp_select_stock_mod_by_stockmod_time (?,?,?)";
		private static final String sp_del_stock_mod = "call sp_del_stock_mod (?,?)";
		private static final String sp_update_stock_mod = "call sp_update_stock_mod (?,?,?,?,?,?,?,?,?)";
		private static final String sp_select_all_stock_mod_detail_by_stockmod_id = "call sp_select_all_stock_mod_detail_by_stockmod_id (?,?)";
		private static final String sp_get_stock_new_pdid_with_pdname = "call sp_get_stock_new_pdid_with_pdname (?)";
		private static final String sp_get_stock_new_location_info= "call sp_get_stock_new_location_info (?)";
		
		@Override
		public List<StockModVO> searchDB(StockModVO stockModVO) {
			Connection con = null;
			PreparedStatement pstmt = null;
			ResultSet rs = null;
			StockModVO row = null;
			List<StockModVO> rows = null;

			String groupId = null;
			String stockmodNo = null;

			try {
				Class.forName(jdbcDriver);
				con = DriverManager.getConnection(dbURL, dbUserName, dbPassword);

				stockmodNo = stockModVO.getStockmod_no();
				groupId = stockModVO.getGroup_id();
				pstmt = con.prepareStatement(sp_select_stock_mod_by_no);
				pstmt.setString(1, groupId);
				pstmt.setString(2, stockmodNo);

				rows = new ArrayList<StockModVO>();

				rs = pstmt.executeQuery();
				while (rs.next()) {
					row = new StockModVO();
					row.setStockmod_id(rs.getString("stockmod_id"));
					row.setGroup_id(rs.getString("group_id"));
					row.setStockmod_no(rs.getString("stockmod_no"));
					row.setStockmod_time(rs.getDate("stockmod_time"));
					row.setRef_id(rs.getString("ref_id"));
					row.setStockmod_type(rs.getString("stockmod_type"));
					row.setProcess_flag(rs.getString("process_flag"));
					row.setMemo(rs.getString("memo"));
					row.setCreate_user(rs.getString("create_user"));
					row.setCreate_time(rs.getDate("create_time"));
					row.setProcess_user(rs.getString("process_user"));
					row.setProcess_time(rs.getDate("process_time"));
					rows.add(row);
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
			return rows;
		}

		@Override
		public List<StockModVO> searchAllDB(StockModVO stockModVO) {
			Connection con = null;
			PreparedStatement pstmt = null;
			ResultSet rs = null;
			String groupId = null;

			List<StockModVO> rows;
			StockModVO row = null;

			try {
				Class.forName(jdbcDriver);
				con = DriverManager.getConnection(dbURL, dbUserName, dbPassword);

				groupId = stockModVO.getGroup_id();
				pstmt = con.prepareStatement(sp_select_all_stock_mod);
				pstmt.setString(1, groupId);

				rows = new ArrayList<StockModVO>();

				rs = pstmt.executeQuery();
				while (rs.next()) {
					row = new StockModVO();
					row.setStockmod_id(rs.getString("stockmod_id"));
					row.setGroup_id(rs.getString("group_id"));
					row.setStockmod_no(rs.getString("stockmod_no"));
					row.setStockmod_time(rs.getDate("stockmod_time"));
					row.setRef_id(rs.getString("ref_id"));
					row.setStockmod_type(rs.getString("stockmod_type"));
					row.setProcess_flag(rs.getString("process_flag"));
					row.setMemo(rs.getString("memo"));
					row.setCreate_user(rs.getString("create_user"));
					row.setCreate_time(rs.getDate("create_time"));
					row.setProcess_user(rs.getString("process_user"));
					row.setProcess_time(rs.getDate("process_time"));

					rows.add(row);
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
			return rows;
		}

		@Override
		public String searchModType() {
			Connection con = null;
			PreparedStatement pstmt = null;
			ResultSet rs = null;
			List<StockModTypeVO> rows;
			StockModTypeVO row = null;
			String jsonStr = "";
			try {
				Class.forName(jdbcDriver);
				con = DriverManager.getConnection(dbURL, dbUserName, dbPassword);

				pstmt = con.prepareStatement(sp_get_stock_mod_type);

				rows = new ArrayList<StockModTypeVO>();

				rs = pstmt.executeQuery();
				while (rs.next()) {
					row = new StockModTypeVO();
					row.setMod_type(rs.getString("mod_type"));
					rows.add(row);
				}

				jsonStr = new Gson().toJson(rows);

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
			return jsonStr;
		}

		@Override
		public List<StockModVO> searchDBByDate(StockModVO stockModVO) {
			Connection con = null;
			PreparedStatement pstmt = null;
			ResultSet rs = null;
			StockModVO row = null;
			List<StockModVO> rows = null;

			String groupId = null, startDate = null, endDate = null;

			try {
				Class.forName(jdbcDriver);
				con = DriverManager.getConnection(dbURL, dbUserName, dbPassword);

				groupId = stockModVO.getGroup_id();
				startDate = stockModVO.getStartDate();
				endDate = stockModVO.getEndDate();

				pstmt = con.prepareStatement(sp_select_stock_mod_by_stockmod_time);
				pstmt.setString(1, groupId);
				pstmt.setString(2, startDate);
				pstmt.setString(3, endDate);

				rows = new ArrayList<StockModVO>();

				rs = pstmt.executeQuery();
				while (rs.next()) {
					row = new StockModVO();
					row.setStockmod_id(rs.getString("stockmod_id"));
					row.setGroup_id(rs.getString("group_id"));
					row.setStockmod_no(rs.getString("stockmod_no"));
					row.setStockmod_time(rs.getDate("stockmod_time"));
					row.setRef_id(rs.getString("ref_id"));
					row.setStockmod_type(rs.getString("stockmod_type"));
					row.setProcess_flag(rs.getString("process_flag"));
					row.setMemo(rs.getString("memo"));
					row.setCreate_user(rs.getString("create_user"));
					row.setCreate_time(rs.getDate("create_time"));
					row.setProcess_user(rs.getString("process_user"));
					row.setProcess_time(rs.getDate("process_time"));
					rows.add(row);
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
			return rows;
		}

		@Override
		public String getNewNo(StockModVO stockModVO) {
			Connection con = null;
			CallableStatement cs = null;
			String stockmodNo = null;

			try {
				Class.forName("com.mysql.jdbc.Driver");
				con = DriverManager.getConnection(dbURL, dbUserName, dbPassword);
				cs = con.prepareCall(sp_get_stock_mod_new_no);

				cs.setString(1, stockModVO.getGroup_id());
				cs.registerOutParameter(2, Types.CHAR);
				cs.execute();

				stockmodNo = cs.getString(2);
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
			return stockmodNo;
		}

		@Override
		public String insertDB(StockModVO stockModVO) {
			Connection con = null;
			CallableStatement cs = null;
			String stockmodNo = null;

			try {
				Class.forName(jdbcDriver);
				con = DriverManager.getConnection(dbURL, dbUserName, dbPassword);
				cs = con.prepareCall(sp_insert_stock_mod);

				String groupId = stockModVO.getGroup_id();
				Date getStockmodTime = stockModVO.getStockmod_time();
				String refId = stockModVO.getRef_id();
				String stockmodType = stockModVO.getStockmod_type();
				String processFlag = stockModVO.getProcess_flag();
				String memo = stockModVO.getMemo();
				String CreateUser = stockModVO.getCreate_user();
				String ProcessUser = stockModVO.getProcess_user();
				Date ProcessTime = stockModVO.getProcess_time();

				cs.setString(1, groupId);
				cs.setString(2, getNewNo(stockModVO));
				cs.setDate(3, getStockmodTime);
				cs.setString(4, refId);
				cs.setString(5, stockmodType);
				cs.setString(6, processFlag);
				cs.setString(7, memo);
				cs.setString(8, CreateUser);
				cs.setString(9, ProcessUser);
				cs.setDate(10, ProcessTime);
				cs.registerOutParameter(11, Types.CHAR);
				cs.execute();

				stockmodNo = cs.getString(11);
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
			return stockmodNo;
		}

		@Override
		public void deleteDB(StockModVO stockModVO) {
			Connection con = null;
			PreparedStatement pstmt = null;
			try {
				Class.forName(jdbcDriver);
				con = DriverManager.getConnection(dbURL, dbUserName, dbPassword);
				pstmt = con.prepareStatement(sp_del_stock_mod);
				String stockmodId = stockModVO.getStockmod_id();
				String userId = stockModVO.getCreate_user();

				pstmt.setString(1, stockmodId);
				pstmt.setString(2, userId);

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
		public void updateDB(StockModVO stockModVO) {
			Connection con = null;
			CallableStatement cs = null;

			try {
				Class.forName(jdbcDriver);
				con = DriverManager.getConnection(dbURL, dbUserName, dbPassword);
				cs = con.prepareCall(sp_update_stock_mod);

				String stockmodId = stockModVO.getStockmod_id();
				String groupId = stockModVO.getGroup_id();
				Date getStockmodTime = stockModVO.getStockmod_time();
				String refId = stockModVO.getRef_id();
				String stockmodType = stockModVO.getStockmod_type();
				String memo = stockModVO.getMemo();
				String CreateUser = stockModVO.getCreate_user();
				String ProcessUser = stockModVO.getProcess_user();
				Date ProcessTime = stockModVO.getProcess_time();

				cs.setString(1, stockmodId);
				cs.setString(2, groupId);
				cs.setDate(3, getStockmodTime);
				cs.setString(4, refId);
				cs.setString(5, stockmodType);
				cs.setString(6, memo);
				cs.setString(7, CreateUser);
				cs.setString(8, ProcessUser);
				cs.setDate(9, ProcessTime);
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
		public List<StockModDetailVO> searchAllDetailDB(StockModDetailVO stockModDetailVO) {
			Connection con = null;
			PreparedStatement pstmt = null;
			ResultSet rs = null;
			String groupId = null;
			String stockmodId = null;

			List<StockModDetailVO> rows;
			StockModDetailVO row = null;

			try {
				Class.forName(jdbcDriver);
				con = DriverManager.getConnection(dbURL, dbUserName, dbPassword);

				groupId = stockModDetailVO.getGroup_id();
				stockmodId = stockModDetailVO.getStockmod_id();

				pstmt = con.prepareStatement(sp_select_all_stock_mod_detail_by_stockmod_id);
				pstmt.setString(1, groupId);
				pstmt.setString(2, stockmodId);

				rows = new ArrayList<StockModDetailVO>();

				rs = pstmt.executeQuery();
				while (rs.next()) {
					row = new StockModDetailVO();
					row.setStockmodDetail_id(rs.getString("stockmodDetail_id"));
					row.setStockmod_id(rs.getString("stockmod_id"));
					row.setProduct_name(rs.getString("product_name"));
					row.setQuantity(rs.getString("quantity"));
					row.setLocation_code(rs.getString("location_code"));
					row.setLocation_desc(rs.getString("location_desc"));
					row.setMemo(rs.getString("memo"));

					rows.add(row);
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
			return rows;
		}

		@Override
		public List<StockModDetailVO> getDetailProductInfoDB(StockModDetailVO stockModDetailVO) {
			Connection con = null;
			PreparedStatement pstmt = null;
			ResultSet rs = null;
			String groupId = null;

			List<StockModDetailVO> rows;
			StockModDetailVO row = null;

			try {
				Class.forName(jdbcDriver);
				con = DriverManager.getConnection(dbURL, dbUserName, dbPassword);

				groupId = stockModDetailVO.getGroup_id();

				pstmt = con.prepareStatement(sp_get_stock_new_pdid_with_pdname);
				pstmt.setString(1, groupId);

				rows = new ArrayList<StockModDetailVO>();

				rs = pstmt.executeQuery();
				while (rs.next()) {
					row = new StockModDetailVO();
					row.setProduct_id(rs.getString("product_id"));
					row.setProduct_name(rs.getString("product_name"));
					rows.add(row);
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
			return rows;
		}

		@Override
		public List<LocationVO> getDetailLocationInfoDB(LocationVO locationVO) {
			Connection con = null;
			PreparedStatement pstmt = null;
			ResultSet rs = null;
			String groupId = null;

			List<LocationVO> rows;
			LocationVO row = null;

			try {
				Class.forName(jdbcDriver);
				con = DriverManager.getConnection(dbURL, dbUserName, dbPassword);

				groupId = locationVO.getGroup_id();

				pstmt = con.prepareStatement(sp_get_stock_new_location_info);
				pstmt.setString(1, groupId);

				rows = new ArrayList<LocationVO>();

				rs = pstmt.executeQuery();
				while (rs.next()) {
					row = new LocationVO();
					row.setLocation_id(rs.getString("location_id"));
					row.setLocation_desc(rs.getString("location_desc"));
					row.setLocation_code(rs.getString("location_code"));
					rows.add(row);
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
			return rows;
		}

	}
}
