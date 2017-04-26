package tw.com.aber.realsale.controller;

import java.io.IOException;
import java.sql.Connection;
import java.sql.Date;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import javax.jms.MessageNotWriteableException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.core.appender.rewrite.LoggerNameLevelRewritePolicy;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import tw.com.aber.util.Util;
import tw.com.aber.vo.CustomerVO;
import tw.com.aber.vo.ProductVO;
import tw.com.aber.vo.RealSaleVO;
import tw.com.aber.vo.SaleDetailVO;
import tw.com.aber.vo.SaleVO;

public class realsale extends HttpServlet {
	private static final long serialVersionUID = 1L;

	private static final Logger logger = LogManager.getLogger(realsale.class);

	private Util util = new Util();

	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		request.setCharacterEncoding("UTF-8");
		response.setCharacterEncoding("UTF-8");

		String group_id = request.getSession().getAttribute("group_id").toString();
		String user_id = request.getSession().getAttribute("user_id").toString();

		RealSaleService realsaleService = new RealSaleService();
		List<RealSaleVO> realsaleList = null;
		Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd").create();
		String action = request.getParameter("action");
		try {
			if ("search".equals(action)) {
				String c_order_no_begin = request.getParameter("order_no_begin");
				String c_order_no_end = request.getParameter("order_no_end");
				String c_trans_list_date_begin = request.getParameter("trans_list_date_begin");
				String c_trans_list_date_end = request.getParameter("trans_list_date_end");
				String c_dis_date_begin = request.getParameter("dis_date_begin");
				String c_dis_date_end = request.getParameter("dis_date_end");
				String c_order_source = request.getParameter("order_source");
				String c_deliveryway = request.getParameter("deliveryway");
				String c_customerid = request.getParameter("customerid");
				
//				if ((c_order_no_begin.trim()).length() > 0 && (c_order_no_end.trim()).length() > 0) {
//					saleList = saleService.getSearchDB(group_id, c_order_no_begin, c_order_no_end);
//				} else if ((c_trans_list_date_begin.trim()).length() > 0
//						&& (c_trans_list_date_end.trim()).length() > 0) {
//					saleList = saleService.getSearchTransListDateDB(group_id, c_trans_list_date_begin,
//							c_trans_list_date_end);
//				} else if ((c_dis_date_begin.trim()).length() > 0 && (c_dis_date_end.trim()).length() > 0) {
//					saleList = saleService.getSearchDisDateDB(group_id, c_dis_date_begin, c_dis_date_end);
//				} else if ((c_order_source.trim()).length() > 0) {
//					saleList = saleService.getSearchOrderSourceDB(group_id, c_order_source);
//				} else if ((c_deliveryway.trim()).length() > 0) {
//					saleList = saleService.getSearchDeliverywayDB(group_id, c_deliveryway);
//				} else {
//					saleList = saleService.getSearchAllDB(group_id);					
//				}
				realsaleList = realsaleService.getSearchMuliDB(group_id, c_order_no_begin, c_order_no_end,c_customerid,c_trans_list_date_begin,c_trans_list_date_end,c_dis_date_begin,c_dis_date_end,c_order_source,c_deliveryway);
				String jsonStrList = gson.toJson(realsaleList);
				logger.info(jsonStrList);
				response.getWriter().write(jsonStrList);
			} else if ("getSaleDetail".equals(action)) {
			// String sale_id = request.getParameter("sale_id");
			//
			// logger.debug("sale_id:".concat(sale_id));
			//
			// SaleDetailVO saleDetailVO = new SaleDetailVO();
			// saleDetailVO.setSale_id(sale_id);
			//
			// List<SaleDetailVO> saleDetailList =
			// saleService.getSaleDetail(saleDetailVO);
			//
			// String jsonStr = gson.toJson(saleDetailList);
			// response.getWriter().write(jsonStr);
			//
			// logger.debug("result".concat(jsonStr));
			}else if ("insert".equals(action)) {
				String order_no = request.getParameter("order_no");
				String order_source = request.getParameter("order_source");
				String invoice = request.getParameter("invoice");
				Float total_amt = Float.valueOf(request.getParameter("total_amt"));
				String memo = request.getParameter("memo");
				SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
				Date invoice_date = request.getParameter("invoice_date") == null
						|| request.getParameter("invoice_date").equals("") ? null
								: new Date(sdf.parse(request.getParameter("invoice_date")).getTime());
				Date trans_list_date = request.getParameter("trans_list_date") == null
						|| request.getParameter("trans_list_date").equals("") ? null
								: new Date(sdf.parse(request.getParameter("trans_list_date")).getTime());
				Date dis_date = request.getParameter("dis_date") == null || request.getParameter("dis_date").equals("")
						? null : new Date(sdf.parse(request.getParameter("dis_date")).getTime());
				Date sale_date = request.getParameter("sale_date") == null
						|| request.getParameter("sale_date").equals("") ? null
								: new Date(sdf.parse(request.getParameter("sale_date")).getTime());
				String customer_id = request.getParameter("customer_id");

				RealSaleVO realSaleVO = new RealSaleVO();
				String seq_no;
				List<RealSaleVO> saleSeqNoList = realsaleService.getSaleSeqNo(group_id);
				if (saleSeqNoList.size() == 0) {
					seq_no = getThisYearMonthDate() + "0001";
				} else {
					seq_no = getGenerateSeqNo(saleSeqNoList.get(0).getSeq_no());
				}

				if (order_no.length() < 1) {
					order_no = seq_no;
				}
				realSaleVO.setSeq_no(seq_no);
				realSaleVO.setGroup_id(group_id);
				realSaleVO.setUser_id(user_id);
				realSaleVO.setOrder_no(order_no);
				realSaleVO.setOrder_source(order_source);
				realSaleVO.setCustomer_id(customer_id);// realSaleVO.setName(name);
				realSaleVO.setInvoice(invoice);
				realSaleVO.setTotal_amt(total_amt);
				realSaleVO.setMemo(memo);
				realSaleVO.setInvoice_date(invoice_date);
				realSaleVO.setTrans_list_date(trans_list_date);
				realSaleVO.setDis_date(dis_date);
				realSaleVO.setSale_date(sale_date);

				logger.debug("seq_no:".concat(seq_no));
				logger.debug("group_id:".concat(group_id));
				logger.debug("user_id:".concat(user_id));
				logger.debug("order_no:".concat(order_no));
				logger.debug("order_source:".concat(order_source));
				logger.debug("customer_id:".concat(customer_id));
				logger.debug("invoice:".concat(invoice));
				logger.debug("price:".concat(total_amt.toString()));
				logger.debug("memo:".concat(memo));
				logger.debug("invoice_date:".concat(invoice_date == null ? "" : invoice_date.toString()));
				logger.debug("trans_list_date:".concat(trans_list_date == null ? "" : trans_list_date.toString()));
				logger.debug("dis_date:".concat(dis_date == null ? "" : dis_date.toString()));
				logger.debug("sale_date:".concat(sale_date == null ? "" : sale_date.toString()));

				realsaleService.addRealSale(realSaleVO);
				realsaleList = realsaleService.getSearchAllDB(group_id);
				String jsonStrList = gson.toJson(realsaleList);
				response.getWriter().write(jsonStrList);
			} else if ("search_custom_data".equals(action)) {
				String term = request.getParameter("term");
				String identity = request.getParameter("identity");

				logger.debug("term:".concat(term));
				logger.debug("identity:".concat(identity));

				List<CustomerVO> customerList = null;
				if ("NAME".equals(identity)) {
					customerList = realsaleService.getSearchCustomerByName(group_id, term);
				}

				String jsonStrList = gson.toJson(customerList);			
				response.getWriter().write(jsonStrList);
			} else if ("update".equals(action)) {
				String realsale_id = request.getParameter("realsale_id");				
				String order_no = request.getParameter("order_no");
				String order_source = request.getParameter("order_source");
				String invoice = request.getParameter("invoice");
				Float total_amt = Float.valueOf(request.getParameter("total_amt"));
				String memo = request.getParameter("memo");
				SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
				Date invoice_date = request.getParameter("invoice_date") == null
						|| request.getParameter("invoice_date").equals("") ? null
								: new Date(sdf.parse(request.getParameter("invoice_date")).getTime());
				Date trans_list_date = request.getParameter("trans_list_date") == null
						|| request.getParameter("trans_list_date").equals("") ? null
								: new Date(sdf.parse(request.getParameter("trans_list_date")).getTime());
				Date dis_date = request.getParameter("dis_date") == null || request.getParameter("dis_date").equals("")
						? null : new Date(sdf.parse(request.getParameter("dis_date")).getTime());
				Date sale_date = request.getParameter("sale_date") == null
						|| request.getParameter("sale_date").equals("") ? null
								: new Date(sdf.parse(request.getParameter("sale_date")).getTime());
				String customer_id = request.getParameter("customer_id");

				RealSaleVO realSaleVO = new RealSaleVO();
				//realSaleVO.setSeq_no(seq_no);
				realSaleVO.setRealsale_id(realsale_id);
				realSaleVO.setGroup_id(group_id);
				realSaleVO.setUser_id(user_id);
				realSaleVO.setOrder_no(order_no);
				realSaleVO.setOrder_source(order_source);
				realSaleVO.setCustomer_id(customer_id);// realSaleVO.setName(name);
				realSaleVO.setInvoice(invoice);
				realSaleVO.setTotal_amt(total_amt);
				realSaleVO.setMemo(memo);
				realSaleVO.setInvoice_date(invoice_date);
				realSaleVO.setTrans_list_date(trans_list_date);
				realSaleVO.setDis_date(dis_date);
				realSaleVO.setSale_date(sale_date);
				
				realsaleService.updateRealSale(realSaleVO);
				realsaleList = realsaleService.getSearchAllDB(group_id);
				String jsonStrList = gson.toJson(realsaleList);
				response.getWriter().write(jsonStrList);
			} else if ("delete".equals(action)) {
				String realsale_id = request.getParameter("realsale_id");
				realsaleService.deleteRealSale(realsale_id,user_id);
				realsaleList = realsaleService.getSearchAllDB(group_id);
				String jsonStrList = gson.toJson(realsaleList);
				response.getWriter().write(jsonStrList);
			}
		} catch (Exception e) {
			logger.error("Exception:".concat(e.getMessage()));
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

	interface realsale_interface {

		public void insertDB(RealSaleVO RealSaleVO);

		public void updateDB(RealSaleVO RealSaleVO);

		public void deleteDB(String realsale_id,String user_id);

		public List<RealSaleVO> getNewSaleSeqNo(String group_id);

		public List<CustomerVO> getCustomerByName(String group_id, String name);
		
		public List<RealSaleVO> searchAllDB(String group_id);
		
		public List<SaleDetailVO> getSaleDetail(SaleDetailVO saleDetailVO);
		
		public List<RealSaleVO> searchMuliDB(String group_id,String c_order_no_begin, String c_order_no_end,String c_customerid,String c_trans_list_date_begin,String c_trans_list_date_end,String c_dis_date_begin,String c_dis_date_end,String c_order_source,String c_deliveryway);
	
//		public List<RealSaleVO> searchorder_no(String group_id, String c_order_no_begin, String c_order_no_end);

//		public List<RealSaleVO> searchTransListDateDB(String group_id, String trans_list_date_begin,String trans_list_date_end);

//		public List<RealSaleVO> searchDisDateDB(String group_id, String dis_date_begin, String dis_date_end);

//		public List<RealSaleVO> searchOrderSourceDB(String group_id, String order_source);

//		public List<RealSaleVO> searchDeliverywayDB(String group_id, String deliveryway);		
	}

	class RealSaleService {
		private realsale_interface dao;

		public RealSaleService() {
			dao = new RealSaleDAO();
		}

		public RealSaleVO addRealSale(RealSaleVO paramVO) {
			dao.insertDB(paramVO);
			return paramVO;
		}

		public RealSaleVO updateRealSale(RealSaleVO paramVO) {
			dao.updateDB(paramVO);
			return paramVO;
		}

		public List<RealSaleVO> getSaleSeqNo(String group_id) {
			return dao.getNewSaleSeqNo(group_id);
		}

		public void deleteRealSale(String realsale_id,String user_id) {
			dao.deleteDB(realsale_id,user_id);
		}
		
		public List<RealSaleVO> getSearchAllDB(String group_id) {
			return dao.searchAllDB(group_id);
		}
		
		public List<CustomerVO> getSearchCustomerByName(String group_id, String name) {
			return dao.getCustomerByName(group_id, name);
		}

		public List<SaleDetailVO> getSaleDetail(SaleDetailVO saleDetailVO) {
			return dao.getSaleDetail(saleDetailVO);
		}
		
		public List<RealSaleVO> getSearchMuliDB(String group_id,String c_order_no_begin, String c_order_no_end,String c_customerid,String c_trans_list_date_begin,String c_trans_list_date_end,String c_dis_date_begin,String c_dis_date_end,String c_order_source,String c_deliveryway) {
			return dao.searchMuliDB(group_id,c_order_no_begin,c_order_no_end,c_customerid,c_trans_list_date_begin,c_trans_list_date_end,c_dis_date_begin,c_dis_date_end,c_order_source,c_deliveryway);
		}
		
//		public List<RealSaleVO> getSearchDB(String group_id, String c_order_no_begin, String c_order_no_end) {
//			return dao.searchorder_no(group_id, c_order_no_begin, c_order_no_end);
//		}

//		public List<RealSaleVO> getSearchTransListDateDB(String group_id, String trans_list_date_begin,
//				String trans_list_date_end) {
//			return dao.searchTransListDateDB(group_id, trans_list_date_begin, trans_list_date_end);
//		}

//		public List<RealSaleVO> getSearchDisDateDB(String group_id, String dis_date_begin, String dis_date_end) {
//			return dao.searchDisDateDB(group_id, dis_date_begin, dis_date_end);
//		}

//		public List<RealSaleVO> getSearchOrderSourceDB(String group_id, String order_source) {
//			return dao.searchOrderSourceDB(group_id, order_source);
//		}

//		public List<RealSaleVO> getSearchDeliverywayDB(String group_id, String deliveryway) {
//			return dao.searchDeliverywayDB(group_id, deliveryway);
//		}
	}

	class RealSaleDAO implements realsale_interface {
		private final String jdbcDriver = getServletConfig().getServletContext().getInitParameter("jdbcDriver");
		private final String dbURL = getServletConfig().getServletContext().getInitParameter("dbURL")
				+ "?useUnicode=true&characterEncoding=utf-8&useSSL=false";
		private final String dbUserName = getServletConfig().getServletContext().getInitParameter("dbUserName");
		private final String dbPassword = getServletConfig().getServletContext().getInitParameter("dbPassword");

		// 會使用到的Stored procedure
		// 查詢
		private static final String sp_selectall_realsale = "call sp_selectall_realsale(?)";
		//private static final String sp_select_realsale_byorder_no = "call sp_select_realsale_byorder_no (?,?,?)";
		//private static final String sp_select_realsale_bytranslistdate = "call sp_select_realsale_bytranslistdate(?,?,?)";
		//private static final String sp_select_realsale_bydisdate = "call sp_select_realsale_bydisdate(?,?,?)";
		//private static final String sp_select_realsale_byordersource = "call sp_select_realsale_byordersource(?,?)";
		//private static final String sp_select_realsale_bydeliveryway = "call sp_select_realsale_bydeliveryway(?,?)";
		// 刪除
		private static final String sp_del_realsale = "call sp_del_realsale (?,?)";
		// 新增
		private static final String sp_insert_realsale = "call sp_insert_realsale(?, ?, ?, ?, ?, ?, ?,?, ?, ?, ?, ?, ?)";
		private static final String sp_get_customer_byname = "call sp_get_customer_byname (?,?)";
		private static final String sp_get_realsale_newseqno = "call sp_get_realsale_newseqno(?)";
		//修改
		private static final String sp_update_realsale = "call sp_update_realsale (?,?,?,?,?,?,?,?,?,?,?,?)";
		//明細
		private static final String sp_get_sale_detail = "call sp_get_sale_detail (?)";
		
		@Override
		public void insertDB(RealSaleVO realSaleVO) {
			Connection con = null;
			PreparedStatement pstmt = null;
			try {
				Class.forName(jdbcDriver);
				con = DriverManager.getConnection(dbURL, dbUserName, dbPassword);
				pstmt = con.prepareStatement(sp_insert_realsale);

				pstmt.setString(1, realSaleVO.getSeq_no());
				pstmt.setString(2, realSaleVO.getGroup_id());
				pstmt.setString(3, realSaleVO.getUser_id());
				pstmt.setString(4, realSaleVO.getOrder_no());
				pstmt.setString(5, realSaleVO.getOrder_source());
				pstmt.setString(6, realSaleVO.getCustomer_id());
				pstmt.setString(7, realSaleVO.getInvoice());
				pstmt.setFloat(8, realSaleVO.getTotal_amt());
				pstmt.setString(9, realSaleVO.getMemo());
				pstmt.setDate(10, realSaleVO.getInvoice_date());
				pstmt.setDate(11, realSaleVO.getTrans_list_date());
				pstmt.setDate(12, realSaleVO.getDis_date());
				pstmt.setDate(13, realSaleVO.getSale_date());

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
		public void updateDB(RealSaleVO realSaleVO) {
			Connection con = null;
			PreparedStatement pstmt = null;
			try {
				Class.forName(jdbcDriver);
				con = DriverManager.getConnection(dbURL, dbUserName, dbPassword);
				pstmt = con.prepareStatement(sp_update_realsale);
				
				pstmt.setString(1, realSaleVO.getRealsale_id());
				pstmt.setString(2, realSaleVO.getOrder_no());
				pstmt.setString(3, realSaleVO.getOrder_source());
				pstmt.setString(4, realSaleVO.getCustomer_id());
				pstmt.setFloat(5, realSaleVO.getTotal_amt());
				pstmt.setDate(6, realSaleVO.getTrans_list_date());
				pstmt.setString(7, realSaleVO.getInvoice());
				pstmt.setDate(8, realSaleVO.getSale_date());
				pstmt.setDate(9, realSaleVO.getInvoice_date());
				pstmt.setDate(10, realSaleVO.getDis_date());
				pstmt.setString(11, realSaleVO.getMemo());
				pstmt.setString(12, realSaleVO.getUser_id());

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
		public void deleteDB(String realsale_id,String user_id) {
			Connection con = null;
			PreparedStatement pstmt = null;
			try {
				Class.forName(jdbcDriver);
				con = DriverManager.getConnection(dbURL, dbUserName, dbPassword);
				pstmt = con.prepareStatement(sp_del_realsale);
				pstmt.setString(1, realsale_id);
				pstmt.setString(2, user_id);

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
		public List<RealSaleVO> getNewSaleSeqNo(String group_id) {
			List<RealSaleVO> list = new ArrayList<RealSaleVO>();
			RealSaleVO realSaleVO = null;

			Connection con = null;
			PreparedStatement pstmt = null;
			ResultSet rs = null;

			try {
				Class.forName(jdbcDriver);
				con = DriverManager.getConnection(dbURL, dbUserName, dbPassword);
				pstmt = con.prepareStatement(sp_get_realsale_newseqno);
				pstmt.setString(1, group_id);
				rs = pstmt.executeQuery();
				while (rs.next()) {
					realSaleVO = new RealSaleVO();
					realSaleVO.setSeq_no(rs.getString("result"));
					list.add(realSaleVO);
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
		public List<CustomerVO> getCustomerByName(String group_id, String name) {
			List<CustomerVO> list = new ArrayList<CustomerVO>();
			CustomerVO customerVO = null;

			Connection con = null;
			PreparedStatement pstmt = null;
			ResultSet rs = null;

			try {
				Class.forName(jdbcDriver);
				con = DriverManager.getConnection(dbURL, dbUserName, dbPassword);
				pstmt = con.prepareStatement(sp_get_customer_byname);
				pstmt.setString(1, group_id);
				pstmt.setString(2, name);
				rs = pstmt.executeQuery();
				while (rs.next()) {
					customerVO = new CustomerVO();
					customerVO.setCustomer_id(rs.getString("customer_id"));
					customerVO.setName(rs.getString("name"));
					list.add(customerVO);
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
		public List<SaleDetailVO> getSaleDetail(SaleDetailVO saleDetailVO) {
			List<SaleDetailVO> list = new ArrayList<SaleDetailVO>();
			SaleDetailVO result = null;

			Connection con = null;
			PreparedStatement pstmt = null;
			ResultSet rs = null;

			util = new Util();

			try {
				Class.forName(jdbcDriver);
				con = DriverManager.getConnection(dbURL, dbUserName, dbPassword);
				pstmt = con.prepareStatement(sp_get_sale_detail);

				String sale_id = util.null2str(saleDetailVO.getSale_id());

				pstmt.setString(1, sale_id);
				rs = pstmt.executeQuery();
				while (rs.next()) {
					result = new SaleDetailVO();

					result.setSaleDetail_id(rs.getString("saleDetail_id"));
					result.setSeq_no(rs.getString("seq_no"));
					result.setGroup_id(rs.getString("group_id"));
					result.setOrder_no(rs.getString("order_no"));
					result.setUser_id(rs.getString("user_id"));
					result.setProduct_id(rs.getString("product_id"));
					result.setProduct_name(rs.getString("product_name"));
					result.setC_product_id(rs.getString("c_product_id"));
					result.setCustomer_id(rs.getString("customer_id"));
					result.setName(rs.getString("name"));
					result.setQuantity(rs.getInt("quantity"));
					result.setPrice(rs.getFloat("price"));
					result.setInvoice(rs.getString("invoice"));
					result.setInvoice_date(rs.getDate("invoice_date"));
					result.setTrans_list_date(rs.getDate("trans_list_date"));
					result.setDis_date(rs.getDate("dis_date"));
					result.setMemo(rs.getString("memo"));
					result.setSale_date(rs.getDate("sale_date"));
					result.setOrder_source(rs.getString("order_source"));
					result.setReturn_date(rs.getDate("return_date"));
					result.setIsreturn(rs.getInt("isreturn"));
					result.setDeliveryway(rs.getString("deliveryway"));

					list.add(result);
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
		public List<RealSaleVO> searchMuliDB(String group_id,String c_order_no_begin, String c_order_no_end,String c_customerid,String c_trans_list_date_begin,String c_trans_list_date_end,String c_dis_date_begin,String c_dis_date_end,String c_order_source,String c_deliveryway) {
			List<RealSaleVO> list = new ArrayList<RealSaleVO>();
			RealSaleVO realSaleVO = null;

			Connection con = null;
			PreparedStatement pstmt = null;
			ResultSet rs = null;
			int i =1;
			try {
				Class.forName(jdbcDriver);
				con = DriverManager.getConnection(dbURL, dbUserName, dbPassword);
				String sqlString="SELECT RS.order_no,RS.order_source,RS.customer_id,RS.total_amt,RS.invoice,RS.invoice_date,RS.trans_list_date,RS.sale_date,RS.dis_date,RS.order_source,RS.memo,RS.realsale_id,C.name as name FROM tb_realsale RS "
								+ "LEFT JOIN tmp.tb_customer C ON RS.customer_id=C.customer_id and RS.group_id=C.group_id "
								+ "WHERE isreturn=false";
				  
				if ((c_order_no_begin.trim()).length() > 0 && (c_order_no_end.trim()).length() > 0) {
					sqlString+=" and RS.order_no between '"+ c_order_no_begin +"' and '"+ c_order_no_begin +"'";								
				} 
				if ((c_customerid.trim()).length() > 0) {
					sqlString+=" and RS.customer_id = '"+ c_customerid +"'";		
				} 
				if ((c_trans_list_date_begin.trim()).length() > 0 && (c_trans_list_date_end.trim()).length() > 0) {
					sqlString+=" and RS.trans_list_date between '"+ c_trans_list_date_begin +"' and '"+ c_trans_list_date_end +"'";
				}
				if ((c_dis_date_begin.trim()).length() > 0 && (c_dis_date_end.trim()).length() > 0) {
					sqlString+=" and RS.dis_date between '"+ c_dis_date_begin +"' and '"+ c_dis_date_end +"'";
				}	
				if ((c_order_source.trim()).length() > 0) {
					sqlString+=" and RS.order_source = '"+ c_order_source +"'";
				}
				if ((c_deliveryway.trim()).length() > 0) {
					sqlString+=" and RS.deliveryway = '"+ c_deliveryway +"'";		
				} 
				sqlString+=" and RS.group_id= '"+ group_id +"'";

				pstmt = con.prepareStatement(sqlString);							
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
//		@Override
//		public List<RealSaleVO> searchorder_no(String group_id, String c_order_no_begin, String c_order_no_end) {
//			List<RealSaleVO> list = new ArrayList<RealSaleVO>();
//			RealSaleVO realrealSaleVO = null;
//
//			Connection con = null;
//			PreparedStatement pstmt = null;
//			ResultSet rs = null;
//			try {
//				Class.forName(jdbcDriver);
//				con = DriverManager.getConnection(dbURL, dbUserName, dbPassword);
//				pstmt = con.prepareStatement(sp_select_realsale_byorder_no);
//
//				pstmt.setString(1, group_id);
//				pstmt.setString(2, c_order_no_begin);
//				pstmt.setString(3, c_order_no_end);
//
//				rs = pstmt.executeQuery();
//				while (rs.next()) {
//					realrealSaleVO = new RealSaleVO();
//					realrealSaleVO.setOrder_no(rs.getString("order_no"));
//					realrealSaleVO.setName(rs.getString("name"));
//					realrealSaleVO.setTrans_list_date(rs.getDate("trans_list_date"));
//					realrealSaleVO.setSale_date(rs.getDate("sale_date"));
//					realrealSaleVO.setDis_date(rs.getDate("dis_date"));
//					realrealSaleVO.setOrder_source(rs.getString("order_source"));
//					realrealSaleVO.setMemo(rs.getString("memo"));
//
//					list.add(realrealSaleVO);
//				}
//			} catch (SQLException se) {
//				throw new RuntimeException("A database error occured. " + se.getMessage());
//			} catch (ClassNotFoundException cnfe) {
//				throw new RuntimeException("A database error occured. " + cnfe.getMessage());
//			} finally {
//				try {
//					if (rs != null) {
//						rs.close();
//					}
//					if (pstmt != null) {
//						pstmt.close();
//					}
//					if (con != null) {
//						con.close();
//					}
//				} catch (SQLException se) {
//					logger.error("SQLException:".concat(se.getMessage()));
//				} catch (Exception e) {
//					logger.error("Exception:".concat(e.getMessage()));
//				}
//			}
//			return list;
//		}

//		@Override
//		public List<RealSaleVO> searchTransListDateDB(String group_id, String trans_list_date_begin,
//				String trans_list_date_end) {
//			List<RealSaleVO> list = new ArrayList<RealSaleVO>();
//			RealSaleVO realSaleVO = null;
//
//			Connection con = null;
//			PreparedStatement pstmt = null;
//			ResultSet rs = null;
//
//			try {
//				Class.forName(jdbcDriver);
//				con = DriverManager.getConnection(dbURL, dbUserName, dbPassword);
//				pstmt = con.prepareStatement(sp_select_realsale_bytranslistdate);
//				pstmt.setString(1, group_id);
//				pstmt.setString(2, trans_list_date_begin);
//				pstmt.setString(3, trans_list_date_end);
//				rs = pstmt.executeQuery();
//				while (rs.next()) {
//					realSaleVO = new RealSaleVO();
//					realSaleVO.setOrder_no(rs.getString("order_no"));
//					realSaleVO.setName(rs.getString("name"));
//					realSaleVO.setTrans_list_date(rs.getDate("trans_list_date"));
//					realSaleVO.setSale_date(rs.getDate("sale_date"));
//					realSaleVO.setDis_date(rs.getDate("dis_date"));
//					realSaleVO.setOrder_source(rs.getString("order_source"));
//					realSaleVO.setMemo(rs.getString("memo"));
//					realSaleVO.setRealsale_id(rs.getString("realsale_id"));
//
//					list.add(realSaleVO);
//				}
//			} catch (SQLException se) {
//				throw new RuntimeException("A database error occured. " + se.getMessage());
//			} catch (ClassNotFoundException cnfe) {
//				throw new RuntimeException("A database error occured. " + cnfe.getMessage());
//			} finally {
//				try {
//					if (rs != null) {
//						rs.close();
//					}
//					if (pstmt != null) {
//						pstmt.close();
//					}
//					if (con != null) {
//						con.close();
//					}
//				} catch (SQLException se) {
//					logger.error("SQLException:".concat(se.getMessage()));
//				} catch (Exception e) {
//					logger.error("Exception:".concat(e.getMessage()));
//				}
//			}
//			return list;
//		}	

//		@Override
//		public List<RealSaleVO> searchDisDateDB(String group_id, String dis_date_begin, String dis_date_end) {
//			List<RealSaleVO> list = new ArrayList<RealSaleVO>();
//			RealSaleVO realSaleVO = null;
//
//			Connection con = null;
//			PreparedStatement pstmt = null;
//			ResultSet rs = null;
//
//			try {
//				Class.forName(jdbcDriver);
//				con = DriverManager.getConnection(dbURL, dbUserName, dbPassword);
//				pstmt = con.prepareStatement(sp_select_realsale_bydisdate);
//				pstmt.setString(1, group_id);
//				pstmt.setString(2, dis_date_begin);
//				pstmt.setString(3, dis_date_end);
//				rs = pstmt.executeQuery();
//				while (rs.next()) {
//					realSaleVO = new RealSaleVO();
//					realSaleVO.setOrder_no(rs.getString("order_no"));
//					realSaleVO.setName(rs.getString("name"));
//					realSaleVO.setTrans_list_date(rs.getDate("trans_list_date"));
//					realSaleVO.setSale_date(rs.getDate("sale_date"));
//					realSaleVO.setDis_date(rs.getDate("dis_date"));
//					realSaleVO.setOrder_source(rs.getString("order_source"));
//					realSaleVO.setMemo(rs.getString("memo"));
//					realSaleVO.setRealsale_id(rs.getString("realsale_id"));
//
//					list.add(realSaleVO);
//				}
//			} catch (SQLException se) {
//				throw new RuntimeException("A database error occured. " + se.getMessage());
//			} catch (ClassNotFoundException cnfe) {
//				throw new RuntimeException("A database error occured. " + cnfe.getMessage());
//			} finally {
//				try {
//					if (rs != null) {
//						rs.close();
//					}
//					if (pstmt != null) {
//						pstmt.close();
//					}
//					if (con != null) {
//						con.close();
//					}
//				} catch (SQLException se) {
//					logger.error("SQLException:".concat(se.getMessage()));
//				} catch (Exception e) {
//					logger.error("Exception:".concat(e.getMessage()));
//				}
//			}
//			return list;
//		}

//		@Override
//		public List<RealSaleVO> searchOrderSourceDB(String group_id, String order_source) {
//			List<RealSaleVO> list = new ArrayList<RealSaleVO>();
//			RealSaleVO realSaleVO = null;
//
//			Connection con = null;
//			PreparedStatement pstmt = null;
//			ResultSet rs = null;
//
//			try {
//				Class.forName(jdbcDriver);
//				con = DriverManager.getConnection(dbURL, dbUserName, dbPassword);
//				pstmt = con.prepareStatement(sp_select_realsale_byordersource);
//				pstmt.setString(1, group_id);
//				pstmt.setString(2, order_source);
//
//				rs = pstmt.executeQuery();
//				while (rs.next()) {
//					realSaleVO = new RealSaleVO();
//					realSaleVO.setOrder_no(rs.getString("order_no"));
//					realSaleVO.setName(rs.getString("name"));
//					realSaleVO.setTrans_list_date(rs.getDate("trans_list_date"));
//					realSaleVO.setSale_date(rs.getDate("sale_date"));
//					realSaleVO.setDis_date(rs.getDate("dis_date"));
//					realSaleVO.setOrder_source(rs.getString("order_source"));
//					realSaleVO.setMemo(rs.getString("memo"));
//					realSaleVO.setRealsale_id(rs.getString("realsale_id"));
//
//					list.add(realSaleVO);
//				}
//			} catch (SQLException se) {
//				throw new RuntimeException("A database error occured. " + se.getMessage());
//			} catch (ClassNotFoundException cnfe) {
//				throw new RuntimeException("A database error occured. " + cnfe.getMessage());
//			} finally {
//				try {
//					if (rs != null) {
//						rs.close();
//					}
//					if (pstmt != null) {
//						pstmt.close();
//					}
//					if (con != null) {
//						con.close();
//					}
//				} catch (SQLException se) {
//					logger.error("SQLException:".concat(se.getMessage()));
//				} catch (Exception e) {
//					logger.error("Exception:".concat(e.getMessage()));
//				}
//			}
//			return list;
//		}

//		@Override
//		public List<RealSaleVO> searchDeliverywayDB(String group_id, String deliveryway) {
//			List<RealSaleVO> list = new ArrayList<RealSaleVO>();
//			RealSaleVO realSaleVO = null;
//
//			Connection con = null;
//			PreparedStatement pstmt = null;
//			ResultSet rs = null;
//
//			try {
//				Class.forName(jdbcDriver);
//				con = DriverManager.getConnection(dbURL, dbUserName, dbPassword);
//				pstmt = con.prepareStatement(sp_select_realsale_bydeliveryway);
//				pstmt.setString(1, group_id);
//				pstmt.setString(2, deliveryway);
//
//				rs = pstmt.executeQuery();
//				while (rs.next()) {
//					realSaleVO = new RealSaleVO();
//					realSaleVO.setOrder_no(rs.getString("order_no"));
//					realSaleVO.setName(rs.getString("name"));
//					realSaleVO.setTrans_list_date(rs.getDate("trans_list_date"));
//					realSaleVO.setSale_date(rs.getDate("sale_date"));
//					realSaleVO.setDis_date(rs.getDate("dis_date"));
//					realSaleVO.setOrder_source(rs.getString("order_source"));
//					realSaleVO.setMemo(rs.getString("memo"));
//					realSaleVO.setRealsale_id(rs.getString("realsale_id"));
//
//					list.add(realSaleVO);
//				}
//			} catch (SQLException se) {
//				throw new RuntimeException("A database error occured. " + se.getMessage());
//			} catch (ClassNotFoundException cnfe) {
//				throw new RuntimeException("A database error occured. " + cnfe.getMessage());
//			} finally {
//				try {
//					if (rs != null) {
//						rs.close();
//					}
//					if (pstmt != null) {
//						pstmt.close();
//					}
//					if (con != null) {
//						con.close();
//					}
//				} catch (SQLException se) {
//					logger.error("SQLException:".concat(se.getMessage()));
//				} catch (Exception e) {
//					logger.error("Exception:".concat(e.getMessage()));
//				}
//			}
//			return list;
//		}
	}
}
