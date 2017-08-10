package tw.com.aber.accpay.controller;

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

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import tw.com.aber.accreceive.controller.accreceive;
import tw.com.aber.vo.AccpayVO;


public class accpay extends HttpServlet {
	private static final Logger logger = LogManager.getLogger(accpay.class);

	private static final long serialVersionUID = 1L;

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		request.setCharacterEncoding("UTF-8");
		response.setCharacterEncoding("UTF-8");
		AccpayService accpayService = null;
		String action = request.getParameter("action");
		String group_id = request.getSession().getAttribute("group_id").toString();
		String user_id = request.getSession().getAttribute("user_id").toString();
		
		logger.debug("action: "+action);
		logger.debug("group_id: "+group_id);
		logger.debug("user_id: "+user_id);
		
		if("searh_amount_date".equals(action)){
			try {
				String start_date = request.getParameter("start_date");
				String end_date = request.getParameter("end_date");
				
				logger.debug("start_date: "+start_date);
				logger.debug("end_date: "+end_date);

				
				if (start_date.trim().length() != 0 & end_date.trim().length() == 0) {
					return;// 程式中斷
				}
				if (end_date.trim().length() != 0 & start_date.trim().length() == 0) {
					return;// 程式中斷
				}
				if (start_date.trim().length() != 0 & end_date.trim().length() != 0) {
					if (DateConversionToDigital(start_date) > DateConversionToDigital(end_date)) {
						return;// 程式中斷
					} else {
						SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
						java.sql.Date amount_start_date = null;
						try {
							java.util.Date start_date_util = sdf.parse(start_date);
							amount_start_date = new java.sql.Date(start_date_util.getTime());
						} catch (ParseException e) {
							e.printStackTrace();
						}
						java.sql.Date amount_end_date = null;
						try {
							java.util.Date end_date_util = sdf.parse(end_date);
							amount_end_date = new java.sql.Date(end_date_util.getTime());
						} catch (ParseException e) {
							e.printStackTrace();
						}
						// 查詢指定期限
						accpayService = new AccpayService();
						List<AccpayVO> list = accpayService.searchAccountNotPayByDayDB(group_id, amount_start_date, amount_end_date);
						Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd").create();
						String jsonStrList = gson.toJson(list);
						response.getWriter().write(jsonStrList);
						return;// 程式中斷
					}
				}
				// 假如無查詢條件，則是查詢全部
				if (start_date.trim().length() == 0 & end_date.trim().length() == 0) {
					accpayService = new AccpayService();
					List<AccpayVO> list = accpayService.searchAccountNotPayDB(group_id);
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
		if("searh_pay_date".equals(action)){
			try {
				String start_date = request.getParameter("start_date");
				String end_date = request.getParameter("end_date");
				
				logger.debug("start_date: "+start_date);
				logger.debug("end_date: "+end_date);
				
				if (start_date.trim().length() != 0 & end_date.trim().length() == 0) {
					return;// 程式中斷
				}
				if (end_date.trim().length() != 0 & start_date.trim().length() == 0) {
					return;// 程式中斷
				}
				if (start_date.trim().length() != 0 & end_date.trim().length() != 0) {

						SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
						java.sql.Date pay_start_date = null;
						try {
							java.util.Date start_date_util = sdf.parse(start_date);
							pay_start_date = new java.sql.Date(start_date_util.getTime());
						} catch (ParseException e) {
							e.printStackTrace();
						}
						java.sql.Date pay_end_date = null;
						try {
							java.util.Date end_date_util = sdf.parse(end_date);
							pay_end_date = new java.sql.Date(end_date_util.getTime());
						} catch (ParseException e) {
							e.printStackTrace();
						}
						// 查詢指定期限
						accpayService = new AccpayService();
						List<AccpayVO> list = accpayService.searchAccountPayByDayDB(group_id, pay_start_date, pay_end_date);
						Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd").create();
						String jsonStrList = gson.toJson(list);
						response.getWriter().write(jsonStrList);
						return;// 程式中斷
				}
				// 假如無查詢條件，則是查詢全部(沒有實付)
				if (start_date.trim().length() == 0 & end_date.trim().length() == 0) {
					accpayService = new AccpayService();
					List<AccpayVO> list = accpayService.searchAccountPayDB(group_id);
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
		if ("pay_account".equals(action)) {
			try {
				String pay_ids = request.getParameter("pay_ids");
				String[] pay_ids_arr = pay_ids.split("~");
				
				logger.debug("pay_ids: "+pay_ids);

				accpayService = new AccpayService();
				accpayService.payAccountTotDB(pay_ids_arr, user_id);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		if ("delete_pay_account".equals(action)) {
			try {
				String pay_ids = request.getParameter("pay_ids");
				String[] pay_ids_arr = pay_ids.split("~");
				
				logger.debug("pay_ids: "+pay_ids);
				
				accpayService = new AccpayService();
				accpayService.delPayAccountTotDB(pay_ids_arr, user_id);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		
		if ("update_noaccpay".equals(action)) {
			try {
				String pay_id = request.getParameter("pay_id");
				String amount_str = request.getParameter("amount");
				String amount_date = request.getParameter("amount_date");
				String memo = request.getParameter("memo");

				logger.debug("pay_id: "+pay_id);
				logger.debug("amount_str: "+amount_str);
				logger.debug("amount_date: "+amount_date);
				logger.debug("memo: "+memo);
				
				Float amount = null;
				Boolean isSuccess = false;
				if ("".equals(amount_str)) {
					amount = null;
				} else {
					amount = Float.valueOf(amount_str);

					AccpayVO accpayVO = new AccpayVO();
					accpayVO.setPay_id(pay_id);
					accpayVO.setAmount(amount);
					accpayVO.setMemo(memo);
					accpayVO.setGroup_id(group_id);

					accpayService = new AccpayService();
					isSuccess = accpayService.update_noaccpay(accpayVO);

					if (isSuccess) {
						response.getWriter().write("success");
					} else {
						response.getWriter().write("error");
					}

				}
			} catch (Exception e) {
				e.printStackTrace();
			}

		}
		
		
		if ("update_accpay".equals(action)) {
			try {
				String pay_id = request.getParameter("pay_id");
				String pay_amount_str = request.getParameter("pay_amount");
				String amount_date = request.getParameter("amount_date");
				String memo = request.getParameter("memo");
				
				logger.debug("pay_id: "+pay_id);
				logger.debug("pay_amount_str: "+pay_amount_str);
				logger.debug("amount_date: "+amount_date);
				logger.debug("memo: "+memo);
				
				Float pay_amount = null;
				Boolean isSuccess = false;
				if ("".equals(pay_amount_str)) {
					pay_amount = null;
				} else {
					pay_amount = Float.valueOf(pay_amount_str);

					AccpayVO accpayVO = new AccpayVO();
					accpayVO.setPay_id(pay_id);
					accpayVO.setPay_amount(pay_amount);
					accpayVO.setMemo(memo);
					accpayVO.setGroup_id(group_id);

					accpayService = new AccpayService();
					isSuccess = accpayService.update_accpay(accpayVO);

					if (isSuccess) {
						response.getWriter().write("success");
					} else {
						response.getWriter().write("error");
					}

				}
			} catch (Exception e) {
				e.printStackTrace();
			}

		}
		
		if("delete_accpay_by_pay_id".equals(action)){

			try {
				String pay_id = request.getParameter("pay_id");
				
				logger.debug("pay_id: "+pay_id);
				
				Boolean isSuccess = false;
				if (!"".equals(pay_id)) {
					AccpayVO accpayVO = new AccpayVO();
					accpayVO.setPay_id(pay_id);
					accpayVO.setGroup_id(group_id);

					accpayService = new AccpayService();
					isSuccess = accpayService.deleteAccpayByPayId(accpayVO);

					if (isSuccess) {
						response.getWriter().write("success");
					} else {
						response.getWriter().write("error");
					}

				}
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

	interface accpay_interface {

		public void payAccountTotDB(String[] pay_id_arr, String user_id);

		public void delPayAccountTotDB(String[] pay_id, String user_id);

		public List<AccpayVO> searchAccountNotPayDB(String group_id);

		public List<AccpayVO> searchAccountPayDB(String group_id);

		public List<AccpayVO> searchAllAccountDB(String group_id);
		
		public List<AccpayVO> searchAccountNotPayByDayDB(String group_id, Date start_date, Date end_date);
		
		public List<AccpayVO> searchAccountPayByDayDB(String group_id, Date start_date, Date end_date);
		
		public Boolean update_noaccpay(AccpayVO accpayVO);
		
		public Boolean update_accpay(AccpayVO accpayVO);
		
		public Boolean deleteAccpayByPayId(AccpayVO accpayVO);

		
	}
	class AccpayService{
		private accpay_interface dao;
		public AccpayService(){
			dao = new AccpayDAO();
		}
		public void payAccountTotDB(String[] pay_id_arr, String user_id){
			dao.payAccountTotDB(pay_id_arr, user_id);
		}

		public void delPayAccountTotDB(String[] pay_id_arr, String user_id){
			dao.delPayAccountTotDB(pay_id_arr, user_id);
		}

		public List<AccpayVO> searchAccountNotPayDB(String group_id){
			return dao.searchAccountNotPayDB(group_id);
		}

		public List<AccpayVO> searchAccountPayDB(String group_id){
			return dao.searchAccountPayDB(group_id);
		}

		public List<AccpayVO> searchAllAccountDB(String group_id){
			return dao.searchAllAccountDB(group_id);
		}
		
		public List<AccpayVO> searchAccountNotPayByDayDB(String group_id, Date start_date, Date end_date){
			return dao.searchAccountNotPayByDayDB(group_id, start_date, end_date);
		}
		
		public List<AccpayVO> searchAccountPayByDayDB(String group_id, Date start_date, Date end_date){
			return dao.searchAccountPayByDayDB(group_id, start_date, end_date);
		}
		public Boolean update_noaccpay(AccpayVO accpayVO){
			return dao.update_noaccpay(accpayVO);
		}
		
		public Boolean update_accpay(AccpayVO accpayVO){
			return dao.update_accpay(accpayVO);
		}

		public Boolean deleteAccpayByPayId(AccpayVO accpayVO){
			return dao.deleteAccpayByPayId(accpayVO);
		}

	}
	class AccpayDAO implements accpay_interface {
		private static final String sp_selectall_account_payable = "call sp_selectall_account_payable (?)";
		private static final String sp_select_account_pay= "call sp_select_account_pay (?)";
		private static final String sp_select_account_not_pay = "call sp_select_account_not_pay (?)";
		private static final String sp_select_account_notpay_byday = "call sp_select_account_notpay_byday(?,?,?)";
		private static final String sp_select_account_pay_byday = "call sp_select_account_pay_byday(?,?,?)";
		private static final String sp_pay_account= "call sp_pay_account(?,?)";
		private static final String sp_del_pay_account = "call sp_del_pay_account(?,?)";
		private static final String sp_update_accpay_nopay = "call sp_update_accpay_nopay(?,?,?,?)";
		private static final String sp_update_accpay_pay = "call sp_update_accpay_pay(?,?,?,?)";
		private static final String sp_delete_accpay_by_pay_id = "call sp_delete_accpay_by_pay_id(?,?)";

		
		private final String dbURL = getServletConfig().getServletContext().getInitParameter("dbURL")
				+ "?useUnicode=true&characterEncoding=utf-8&useSSL=false";
		private final String dbUserName = getServletConfig().getServletContext().getInitParameter("dbUserName");
		private final String dbPassword = getServletConfig().getServletContext().getInitParameter("dbPassword");
		
		@Override
		public void payAccountTotDB(String[] pay_id_arr, String user_id) {
			Connection con = null;
			PreparedStatement pstmt = null;
			try {
				Class.forName("com.mysql.jdbc.Driver");
				con = DriverManager.getConnection(dbURL, dbUserName, dbPassword);
				for (int i = 0; i < pay_id_arr.length; i++) {
					pstmt = con.prepareStatement(sp_pay_account);
					pstmt.setString(1, pay_id_arr[i]);
					pstmt.setString(2, user_id);
					pstmt.executeUpdate();
				}
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
		public void delPayAccountTotDB(String[] pay_id_arr, String user_id) {
			Connection con = null;
			PreparedStatement pstmt = null;
			try {
				Class.forName("com.mysql.jdbc.Driver");
				con = DriverManager.getConnection(dbURL, dbUserName, dbPassword);
				for(int i=0;i<pay_id_arr.length;i++){
				pstmt = con.prepareStatement(sp_del_pay_account);
				pstmt.setString(1, pay_id_arr[i]);
				pstmt.setString(2, user_id);
				pstmt.executeUpdate();
				}
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
		public List<AccpayVO> searchAccountNotPayDB(String group_id) {
			List<AccpayVO> list = new ArrayList<AccpayVO>();
			AccpayVO accpayVO = null;

			Connection con = null;
			PreparedStatement pstmt = null;
			ResultSet rs = null;

			try {
				Class.forName("com.mysql.jdbc.Driver");
				con = DriverManager.getConnection(dbURL, dbUserName, dbPassword);
				pstmt = con.prepareStatement(sp_select_account_not_pay);
				pstmt.setString(1, group_id);
				rs = pstmt.executeQuery();
				while (rs.next()) {
					accpayVO = new AccpayVO();
					accpayVO.setPay_id(rs.getString("pay_id"));
					accpayVO.setSeq_no(rs.getString("seq_no"));
					accpayVO.setGroup_id(rs.getString("group_id"));
					accpayVO.setAmount(rs.getFloat("amount"));
					accpayVO.setAmount_date(rs.getDate("amount_date"));
					accpayVO.setPay_amount(rs.getFloat("pay_amount"));
					accpayVO.setPay_date(rs.getDate("pay_date"));
					accpayVO.setUser_id(rs.getString("user_id"));
					accpayVO.setMemo(rs.getString("memo"));
					accpayVO.setSupply_name(rs.getString("supply_name"));
					list.add(accpayVO); // Store the row in the list
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
		public List<AccpayVO> searchAccountPayDB(String group_id) {
			List<AccpayVO> list = new ArrayList<AccpayVO>();
			AccpayVO accpayVO = null;

			Connection con = null;
			PreparedStatement pstmt = null;
			ResultSet rs = null;

			try {
				Class.forName("com.mysql.jdbc.Driver");
				con = DriverManager.getConnection(dbURL, dbUserName, dbPassword);
				pstmt = con.prepareStatement(sp_select_account_pay);
				pstmt.setString(1, group_id);
				rs = pstmt.executeQuery();
				while (rs.next()) {
					accpayVO = new AccpayVO();
					accpayVO.setPay_id(rs.getString("pay_id"));
					accpayVO.setSeq_no(rs.getString("seq_no"));
					accpayVO.setGroup_id(rs.getString("group_id"));
					accpayVO.setAmount(rs.getFloat("amount"));
					accpayVO.setAmount_date(rs.getDate("amount_date"));
					accpayVO.setPay_amount(rs.getFloat("pay_amount"));
					accpayVO.setPay_date(rs.getDate("pay_date"));
					accpayVO.setUser_id(rs.getString("user_id"));
					accpayVO.setMemo(rs.getString("memo"));
					accpayVO.setSupply_name(rs.getString("supply_name"));
					list.add(accpayVO); // Store the row in the list
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
		public List<AccpayVO> searchAllAccountDB(String group_id) {
			List<AccpayVO> list = new ArrayList<AccpayVO>();
			AccpayVO accpayVO = null;

			Connection con = null;
			PreparedStatement pstmt = null;
			ResultSet rs = null;

			try {
				Class.forName("com.mysql.jdbc.Driver");
				con = DriverManager.getConnection(dbURL, dbUserName, dbPassword);
				pstmt = con.prepareStatement(sp_selectall_account_payable);
				pstmt.setString(1, group_id);
				rs = pstmt.executeQuery();
				while (rs.next()) {
					accpayVO = new AccpayVO();
					accpayVO.setPay_id(rs.getString("pay_id"));
					accpayVO.setSeq_no(rs.getString("seq_no"));
					accpayVO.setGroup_id(rs.getString("group_id"));
					accpayVO.setAmount(rs.getFloat("amount"));
					accpayVO.setAmount_date(rs.getDate("amount_date"));
					accpayVO.setPay_amount(rs.getFloat("pay_amount"));
					accpayVO.setPay_date(rs.getDate("pay_date"));
					accpayVO.setUser_id(rs.getString("user_id"));
					accpayVO.setMemo(rs.getString("memo"));
					accpayVO.setSupply_name(rs.getString("supply_name"));
					list.add(accpayVO); // Store the row in the list
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
		public List<AccpayVO> searchAccountNotPayByDayDB(String group_id, Date start_date, Date end_date) {
			List<AccpayVO> list = new ArrayList<AccpayVO>();
			AccpayVO accpayVO = null;

			Connection con = null;
			PreparedStatement pstmt = null;
			ResultSet rs = null;

			try {
				Class.forName("com.mysql.jdbc.Driver");
				con = DriverManager.getConnection(dbURL, dbUserName, dbPassword);
				pstmt = con.prepareStatement(sp_select_account_notpay_byday);
				pstmt.setString(1, group_id);
				pstmt.setDate(2, start_date);
				pstmt.setDate(3, end_date);
				rs = pstmt.executeQuery();
				while (rs.next()) {
					accpayVO = new AccpayVO();
					accpayVO.setPay_id(rs.getString("pay_id"));
					accpayVO.setSeq_no(rs.getString("seq_no"));
					accpayVO.setGroup_id(rs.getString("group_id"));
					accpayVO.setAmount(rs.getFloat("amount"));
					accpayVO.setAmount_date(rs.getDate("amount_date"));
					accpayVO.setPay_amount(rs.getFloat("pay_amount"));
					accpayVO.setPay_date(rs.getDate("pay_date"));
					accpayVO.setUser_id(rs.getString("user_id"));
					accpayVO.setMemo(rs.getString("memo"));
					accpayVO.setSupply_name(rs.getString("supply_name"));
					list.add(accpayVO); // Store the row in the list
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
		public List<AccpayVO> searchAccountPayByDayDB(String group_id, Date start_date, Date end_date) {
			List<AccpayVO> list = new ArrayList<AccpayVO>();
			AccpayVO accpayVO = null;

			Connection con = null;
			PreparedStatement pstmt = null;
			ResultSet rs = null;

			try {
				Class.forName("com.mysql.jdbc.Driver");
				con = DriverManager.getConnection(dbURL, dbUserName, dbPassword);
				pstmt = con.prepareStatement(sp_select_account_pay_byday);
				pstmt.setString(1, group_id);
				pstmt.setDate(2, start_date);
				pstmt.setDate(3, end_date);
				rs = pstmt.executeQuery();
				while (rs.next()) {
					accpayVO = new AccpayVO();
					accpayVO.setPay_id(rs.getString("pay_id"));
					accpayVO.setSeq_no(rs.getString("seq_no"));
					accpayVO.setGroup_id(rs.getString("group_id"));
					accpayVO.setAmount(rs.getFloat("amount"));
					accpayVO.setAmount_date(rs.getDate("amount_date"));
					accpayVO.setPay_amount(rs.getFloat("pay_amount"));
					accpayVO.setPay_date(rs.getDate("pay_date"));
					accpayVO.setUser_id(rs.getString("user_id"));
					accpayVO.setMemo(rs.getString("memo"));
					accpayVO.setSupply_name(rs.getString("supply_name"));
					list.add(accpayVO); // Store the row in the list
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
		public Boolean update_noaccpay(AccpayVO accpayVO) {
			Connection con = null;
			PreparedStatement pstmt = null;
			ResultSet rs = null;
			Boolean isSuccess = false;

			try {
				Class.forName("com.mysql.jdbc.Driver");
				con = DriverManager.getConnection(dbURL, dbUserName, dbPassword);
				pstmt = con.prepareStatement(sp_update_accpay_nopay);

				pstmt.setString(1, accpayVO.getGroup_id());
				pstmt.setFloat(2, accpayVO.getAmount());
				pstmt.setString(3, accpayVO.getMemo());
				pstmt.setString(4, accpayVO.getPay_id());

				rs = pstmt.executeQuery();
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
			isSuccess = true;
			return isSuccess;
		}

		
		@Override
		public Boolean update_accpay(AccpayVO accpayVO) {
			Connection con = null;
			PreparedStatement pstmt = null;
			ResultSet rs = null;
			Boolean isSuccess = false;

			try {
				Class.forName("com.mysql.jdbc.Driver");
				con = DriverManager.getConnection(dbURL, dbUserName, dbPassword);
				pstmt = con.prepareStatement(sp_update_accpay_pay);

				pstmt.setString(1, accpayVO.getGroup_id());
				pstmt.setFloat(2, accpayVO.getPay_amount());
				pstmt.setString(3, accpayVO.getMemo());
				pstmt.setString(4, accpayVO.getPay_id());

				rs = pstmt.executeQuery();
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
			isSuccess = true;
			return isSuccess;
		}

		@Override
		public Boolean deleteAccpayByPayId(AccpayVO accpayVO) {
			Connection con = null;
			PreparedStatement pstmt = null;
			ResultSet rs = null;
			Boolean isSuccess = false;

			try {
				Class.forName("com.mysql.jdbc.Driver");
				con = DriverManager.getConnection(dbURL, dbUserName, dbPassword);
				pstmt = con.prepareStatement(sp_delete_accpay_by_pay_id);

				pstmt.setString(1, accpayVO.getGroup_id());
				pstmt.setString(2, accpayVO.getPay_id());

				rs = pstmt.executeQuery();
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
			isSuccess = true;
			return isSuccess;
		}

	}
}
