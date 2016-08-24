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

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;


public class accpay extends HttpServlet {
	private static final long serialVersionUID = 1L;

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		request.setCharacterEncoding("UTF-8");
		response.setCharacterEncoding("UTF-8");
		AccpayService accpayService = null;
		String action = request.getParameter("action");
		String group_id = request.getSession().getAttribute("group_id").toString();
		String user_id = request.getSession().getAttribute("user_id").toString();
		if("searh_amount_date".equals(action)){
			try {
				String start_date = request.getParameter("start_date");
				String end_date = request.getParameter("end_date");
				if (start_date.trim().length() != 0 & end_date.trim().length() == 0) {
					List<AccpayVO> list = new ArrayList<AccpayVO>();
					AccpayVO accpayVO = new AccpayVO();
					accpayVO.setMessage("如要以日期查詢，請完整填寫訖日欄位");
					list.add(accpayVO);
					Gson gson = new Gson();
					String jsonStrList = gson.toJson(list);
					response.getWriter().write(jsonStrList);
					return;// 程式中斷
				}
				if (end_date.trim().length() != 0 & start_date.trim().length() == 0) {
					List<AccpayVO> list = new ArrayList<AccpayVO>();
					AccpayVO accpayVO = new AccpayVO();
					accpayVO.setMessage("如要以日期查詢，請完整填寫起日欄位");
					list.add(accpayVO);
					Gson gson = new Gson();
					String jsonStrList = gson.toJson(list);
					response.getWriter().write(jsonStrList);
					return;// 程式中斷
				}
				if (start_date.trim().length() != 0 & end_date.trim().length() != 0) {
					if (DateConversionToDigital(start_date) > DateConversionToDigital(end_date)) {
						List<AccpayVO> list = new ArrayList<AccpayVO>();
						AccpayVO accpayVO = new AccpayVO();
						accpayVO.setMessage("起日不可大於訖日");
						list.add(accpayVO);
						Gson gson = new Gson();
						String jsonStrList = gson.toJson(list);
						response.getWriter().write(jsonStrList);
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
						AccpayVO accpayVO = new AccpayVO();
						accpayVO.setMessage("驗證通過");
						list.add(accpayVO);
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
					AccpayVO accpayVO = new AccpayVO();
					accpayVO.setMessage("驗證通過");
					list.add(accpayVO);
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
				if (start_date.trim().length() != 0 & end_date.trim().length() == 0) {
					List<AccpayVO> list = new ArrayList<AccpayVO>();
					AccpayVO accpayVO = new AccpayVO();
					accpayVO.setMessage("如要以日期查詢，請完整填寫訖日欄位");
					list.add(accpayVO);
					Gson gson = new Gson();
					String jsonStrList = gson.toJson(list);
					response.getWriter().write(jsonStrList);
					return;// 程式中斷
				}
				if (end_date.trim().length() != 0 & start_date.trim().length() == 0) {
					List<AccpayVO> list = new ArrayList<AccpayVO>();
					AccpayVO accpayVO = new AccpayVO();
					accpayVO.setMessage("如要以日期查詢，請完整填寫起日欄位");
					list.add(accpayVO);
					Gson gson = new Gson();
					String jsonStrList = gson.toJson(list);
					response.getWriter().write(jsonStrList);
					return;// 程式中斷
				}
				if (start_date.trim().length() != 0 & end_date.trim().length() != 0) {
					if (DateConversionToDigital(start_date) > DateConversionToDigital(end_date)) {
						List<AccpayVO> list = new ArrayList<AccpayVO>();
						AccpayVO accpayVO = new AccpayVO();
						accpayVO.setMessage("起日不可大於訖日");
						list.add(accpayVO);
						Gson gson = new Gson();
						String jsonStrList = gson.toJson(list);
						response.getWriter().write(jsonStrList);
						return;// 程式中斷
					} else {
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
						AccpayVO accpayVO = new AccpayVO();
						accpayVO.setMessage("驗證通過");
						list.add(accpayVO);
						Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd").create();
						String jsonStrList = gson.toJson(list);
						response.getWriter().write(jsonStrList);
						return;// 程式中斷
					}
				}
				// 假如無查詢條件，則是查詢全部(沒有實付)
				if (start_date.trim().length() == 0 & end_date.trim().length() == 0) {
					accpayService = new AccpayService();
					List<AccpayVO> list = accpayService.searchAccountPayDB(group_id);
					AccpayVO accpayVO = new AccpayVO();
					accpayVO.setMessage("驗證通過");
					list.add(accpayVO);
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
				/*************************** 1.接收請求參數 ***************************************/
				String pay_id = request.getParameter("pay_id");
				SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

				java.sql.Date pay_date = null;
				try {
					String pay_date_str = request.getParameter("pay_date");
					java.util.Date invoice_date_util = sdf.parse(pay_date_str);
					pay_date = new java.sql.Date(invoice_date_util.getTime());
				} catch (ParseException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				/*************************** 2.開始付帳退回 ***************************************/
				accpayService = new AccpayService();
				accpayService.payAccountTotDB(pay_id, user_id);
				/*************************** 3.刪除完成,準備轉交(Send the Success view) ***********/
				accpayService = new AccpayService();
				List<AccpayVO> list = accpayService.searchAccountPayByDayDB(group_id, pay_date, pay_date);
				Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd").create();
				String jsonStrList = gson.toJson(list);
				response.getWriter().write(jsonStrList);
				return;// 程式中斷
				/*************************** 其他可能的錯誤處理 **********************************/
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		if ("delete_pay_account".equals(action)) {
			try {
				/*************************** 1.接收請求參數 ***************************************/
				String pay_id = request.getParameter("pay_id");
				String start_date = request.getParameter("start_date");
				String end_date = request.getParameter("end_date");
				/*************************** 2.開始付帳退回 ***************************************/
				accpayService = new AccpayService();
				accpayService.delPayAccountTotDB(pay_id, user_id);
				/*************************** 3.刪除完成,準備轉交(Send the Success view) ***********/
				// 假如無查詢條件，則是查詢全部
				if ((start_date == null || (start_date.trim()).length() == 0)&(end_date == null || (end_date.trim()).length() == 0)) {
					accpayService = new AccpayService();
					List<AccpayVO> list = accpayService.searchAccountNotPayDB(group_id);
					Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd").create();
					String jsonStrList = gson.toJson(list);
					response.getWriter().write(jsonStrList);
					return;// 程式中斷
				}
				// 假如有一開始查詢日期的話，查詢指定日期
				if ((start_date != null || (start_date.trim()).length() != 0)&(end_date != null || (end_date.trim()).length() != 0)) {
					accpayService = new AccpayService();
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
					List<AccpayVO> list = accpayService.searchAccountNotPayByDayDB(group_id, amount_start_date, amount_end_date);
					Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd").create();
					String jsonStrList = gson.toJson(list);
					response.getWriter().write(jsonStrList);
					return;// 程式中斷
				}
				/*************************** 其他可能的錯誤處理 **********************************/
			} catch (Exception e) {
				// TODO Auto-generated catch block
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
	@SuppressWarnings("serial")
	public class AccpayVO implements java.io.Serializable {
		private String pay_id;
		private String group_id;
		private String seq_no;
		private Float amount;
		private java.sql.Date amount_date;
		private Float pay_amount;
		private java.sql.Date pay_date;
		private String user_id;
		private String memo;
		private String supply_name;
		private String message;
		

		public String getPay_id() {
			return pay_id;
		}

		public void setPay_id(String pay_id) {
			this.pay_id = pay_id;
		}

		public String getGroup_id() {
			return group_id;
		}

		public void setGroup_id(String group_id) {
			this.group_id = group_id;
		}

		public String getSeq_no() {
			return seq_no;
		}

		public void setSeq_no(String seq_no) {
			this.seq_no = seq_no;
		}

		public Float getAmount() {
			return amount;
		}

		public void setAmount(Float amount) {
			this.amount = amount;
		}

		public java.sql.Date getAmount_date() {
			return amount_date;
		}

		public void setAmount_date(java.sql.Date amount_date) {
			this.amount_date = amount_date;
		}

		public Float getPay_amount() {
			return pay_amount;
		}

		public void setPay_amount(Float pay_amount) {
			this.pay_amount = pay_amount;
		}

		public java.sql.Date getPay_date() {
			return pay_date;
		}

		public void setPay_date(java.sql.Date pay_date) {
			this.pay_date = pay_date;
		}

		public String getUser_id() {
			return user_id;
		}

		public void setUser_id(String user_id) {
			this.user_id = user_id;
		}

		public String getMemo() {
			return memo;
		}

		public void setMemo(String memo) {
			this.memo = memo;
		}
		
		public String getSupply_name() {
			return supply_name;
		}

		public void setSupply_name(String supply_name) {
			this.supply_name = supply_name;
		}

		public String getMessage() {
			return message;
		}

		public void setMessage(String message) {
			this.message = message;
		}
	}

	interface accpay_interface {

		public void payAccountTotDB(String pay_id, String user_id);

		public void delPayAccountTotDB(String pay_id, String user_id);

		public List<AccpayVO> searchAccountNotPayDB(String group_id);

		public List<AccpayVO> searchAccountPayDB(String group_id);

		public List<AccpayVO> searchAllAccountDB(String group_id);
		
		public List<AccpayVO> searchAccountNotPayByDayDB(String group_id, Date start_date, Date end_date);
		
		public List<AccpayVO> searchAccountPayByDayDB(String group_id, Date start_date, Date end_date);
	}
	class AccpayService{
		private accpay_interface dao;
		public AccpayService(){
			dao = new AccpayDAO();
		}
		public void payAccountTotDB(String pay_id, String user_id){
			dao.payAccountTotDB(pay_id, user_id);
		}

		public void delPayAccountTotDB(String pay_id, String user_id){
			dao.delPayAccountTotDB(pay_id, user_id);
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
	}
	class AccpayDAO implements accpay_interface {
		private static final String sp_selectall_account_payable = "call sp_selectall_account_payable (?)";
		private static final String sp_select_account_pay= "call sp_select_account_pay (?)";
		private static final String sp_select_account_not_pay = "call sp_select_account_not_pay (?)";
		private static final String sp_select_account_notpay_byday = "call sp_select_account_notpay_byday(?,?,?)";
		private static final String sp_select_account_pay_byday = "call sp_select_account_pay_byday(?,?,?)";
		private static final String sp_pay_account= "call sp_pay_account(?,?)";
		private static final String sp_del_pay_account = "call sp_del_pay_account(?,?)";
		
		private final String dbURL = getServletConfig().getServletContext().getInitParameter("dbURL")
				+ "?useUnicode=true&characterEncoding=utf-8&useSSL=false";
		private final String dbUserName = getServletConfig().getServletContext().getInitParameter("dbUserName");
		private final String dbPassword = getServletConfig().getServletContext().getInitParameter("dbPassword");
		
		@Override
		public void payAccountTotDB(String pay_id, String user_id) {
			Connection con = null;
			PreparedStatement pstmt = null;
			try {
				Class.forName("com.mysql.jdbc.Driver");
				con = DriverManager.getConnection(dbURL, dbUserName, dbPassword);
				pstmt = con.prepareStatement(sp_pay_account);
				//System.out.println("pay_id: "+pay_id+" user_id: "+user_id);
				pstmt.setString(1, pay_id);
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
		public void delPayAccountTotDB(String pay_id, String user_id) {
			Connection con = null;
			PreparedStatement pstmt = null;
			try {
				Class.forName("com.mysql.jdbc.Driver");
				con = DriverManager.getConnection(dbURL, dbUserName, dbPassword);
				pstmt = con.prepareStatement(sp_del_pay_account);

				pstmt.setString(1, pay_id);
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

	}
}
