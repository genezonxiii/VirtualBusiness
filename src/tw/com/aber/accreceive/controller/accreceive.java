package tw.com.aber.accreceive.controller;

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

import tw.com.aber.vo.AccreceiveVO;

public class accreceive extends HttpServlet {
	private static final long serialVersionUID = 1L;

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		request.setCharacterEncoding("UTF-8");
		response.setCharacterEncoding("UTF-8");
		AccreceiveService accreceiveService = null;
		String action = request.getParameter("action");
		if("melvin".equals(action)){
			String g_id=request.getSession().getAttribute("group_id").toString();
			String seq=request.getParameter("seq_no");
			String detail="";
			Connection con = null;
			PreparedStatement pstmt = null;
			ResultSet rs = null;
			String dbURL = getServletConfig().getServletContext().getInitParameter("dbURL")
						+"?useUnicode=true&characterEncoding=utf-8&useSSL=false";
			String dbUserName = getServletConfig().getServletContext().getInitParameter("dbUserName");
			String dbPassword = getServletConfig().getServletContext().getInitParameter("dbPassword");
			try {
				Class.forName("com.mysql.jdbc.Driver");
				con = DriverManager.getConnection(dbURL, dbUserName, dbPassword);
				pstmt = con.prepareStatement("call sp_get_orderno(?,?)");
				pstmt.setString(1,g_id);
				pstmt.setString(2,seq);
				rs = pstmt.executeQuery();
				while (rs.next()) {
					detail+="<tr><td>訂單號：</td><td><span class='delete_msg'>"+rs.getString("order_no")+"</span></td></tr>";
					detail+="<tr><td>產品名稱：</td><td><span class='delete_msg'>"+rs.getString("product_name")+"</span></td></tr>";
					detail+="<tr><td>價格：</td><td><span class='delete_msg'>"+rs.getString("price")+"</span></td></tr>";
					detail+="<tr><td>平台：</td><td><span class='delete_msg'>"+rs.getString("order_source")+"</span></td></tr>";
				}
			} catch (Exception se) {System.out.println("ERROR WITH: "+se);}
			//System.out.println(detail);
			response.getWriter().write(detail);
			return;
		}
		String group_id = request.getSession().getAttribute("group_id").toString();
		String user_id = request.getSession().getAttribute("user_id").toString();
		if("searh_amount_date".equals(action)){
			try {
				String start_date = request.getParameter("start_date");
				String end_date = request.getParameter("end_date");
				if (start_date.trim().length() != 0 & end_date.trim().length() == 0) {
					List<AccreceiveVO> list = new ArrayList<AccreceiveVO>();
					AccreceiveVO accreceiveVO = new AccreceiveVO();
					accreceiveVO.setMessage("如要以日期查詢，請完整填寫訖日欄位");
					list.add(accreceiveVO);
					Gson gson = new Gson();
					String jsonStrList = gson.toJson(list);
					response.getWriter().write(jsonStrList);
					return;// 程式中斷
				}
				if (end_date.trim().length() != 0 & start_date.trim().length() == 0) {
					List<AccreceiveVO> list = new ArrayList<AccreceiveVO>();
					AccreceiveVO accreceiveVO = new AccreceiveVO();
					accreceiveVO.setMessage("如要以日期查詢，請完整填寫起日欄位");
					list.add(accreceiveVO);
					Gson gson = new Gson();
					String jsonStrList = gson.toJson(list);
					response.getWriter().write(jsonStrList);
					return;// 程式中斷
				}
				if (start_date.trim().length() != 0 & end_date.trim().length() != 0) {
					if (DateConversionToDigital(start_date) > DateConversionToDigital(end_date)) {
						List<AccreceiveVO> list = new ArrayList<AccreceiveVO>();
						AccreceiveVO accreceiveVO = new AccreceiveVO();
						accreceiveVO.setMessage("起日不可大於訖日");
						list.add(accreceiveVO);
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
						accreceiveService = new AccreceiveService();
						List<AccreceiveVO> list = accreceiveService.searchAccountNotReceiveByDayDB(group_id, amount_start_date, amount_end_date);
						AccreceiveVO accreceiveVO = new AccreceiveVO();
						accreceiveVO.setMessage("驗證通過");
						list.add(accreceiveVO);
						Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd").create();
						String jsonStrList = gson.toJson(list);
						response.getWriter().write(jsonStrList);
						return;// 程式中斷
					}
				}
				// 假如無查詢條件，則是查詢全部
				if (start_date.trim().length() == 0 & end_date.trim().length() == 0) {
					accreceiveService = new AccreceiveService();
					List<AccreceiveVO> list = accreceiveService.searchAccountNotReceiveDB(group_id);
					AccreceiveVO accreceiveVO = new AccreceiveVO();
					accreceiveVO.setMessage("驗證通過");
					list.add(accreceiveVO);
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
		if("searh_receive_date".equals(action)){
			try {
				String start_date = request.getParameter("start_date");
				String end_date = request.getParameter("end_date");
				if (start_date.trim().length() != 0 & end_date.trim().length() == 0) {
					List<AccreceiveVO> list = new ArrayList<AccreceiveVO>();
					AccreceiveVO accreceiveVO = new AccreceiveVO();
					accreceiveVO.setMessage("如要以日期查詢，請完整填寫訖日欄位");
					list.add(accreceiveVO);
					Gson gson = new Gson();
					String jsonStrList = gson.toJson(list);
					response.getWriter().write(jsonStrList);
					return;// 程式中斷
				}
				if (end_date.trim().length() != 0 & start_date.trim().length() == 0) {
					List<AccreceiveVO> list = new ArrayList<AccreceiveVO>();
					AccreceiveVO accreceiveVO = new AccreceiveVO();
					accreceiveVO.setMessage("如要以日期查詢，請完整填寫起日欄位");
					list.add(accreceiveVO);
					Gson gson = new Gson();
					String jsonStrList = gson.toJson(list);
					response.getWriter().write(jsonStrList);
					return;// 程式中斷
				}
				if (start_date.trim().length() != 0 & end_date.trim().length() != 0) {
					if (DateConversionToDigital(start_date) > DateConversionToDigital(end_date)) {
						List<AccreceiveVO> list = new ArrayList<AccreceiveVO>();
						AccreceiveVO accreceiveVO = new AccreceiveVO();
						accreceiveVO.setMessage("起日不可大於訖日");
						list.add(accreceiveVO);
						Gson gson = new Gson();
						String jsonStrList = gson.toJson(list);
						response.getWriter().write(jsonStrList);
						return;// 程式中斷
					} else {
						SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
						java.sql.Date receive_start_date = null;
						try {
							java.util.Date start_date_util = sdf.parse(start_date);
							receive_start_date = new java.sql.Date(start_date_util.getTime());
						} catch (ParseException e) {
							e.printStackTrace();
						}
						java.sql.Date receive_end_date = null;
						try {
							java.util.Date end_date_util = sdf.parse(end_date);
							receive_end_date = new java.sql.Date(end_date_util.getTime());
						} catch (ParseException e) {
							e.printStackTrace();
						}
						// 查詢指定期限
						accreceiveService = new AccreceiveService();
						List<AccreceiveVO> list = accreceiveService.searchAccountReceiveByDayDB(group_id, receive_start_date, receive_end_date);
						AccreceiveVO accreceiveVO = new AccreceiveVO();
						accreceiveVO.setMessage("驗證通過");
						list.add(accreceiveVO);
						Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd").create();
						String jsonStrList = gson.toJson(list);
						response.getWriter().write(jsonStrList);
						return;// 程式中斷
					}
				}
				// 假如無查詢條件，則是查詢全部(沒有實付)
				if (start_date.trim().length() == 0 & end_date.trim().length() == 0) {
					accreceiveService = new AccreceiveService();
					List<AccreceiveVO> list = accreceiveService.searchAccountReceiveDB(group_id);
					AccreceiveVO accreceiveVO = new AccreceiveVO();
					accreceiveVO.setMessage("驗證通過");
					list.add(accreceiveVO);
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
		if ("receive_account".equals(action)) {
			try {
				/*************************** 1.接收請求參數 ***************************************/
				String receivable_id = request.getParameter("receivable_id");
				SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

				java.sql.Date receive_date = null;
				try {
					String receive_date_str = request.getParameter("receive_date");
					java.util.Date invoice_date_util = sdf.parse(receive_date_str);
					receive_date = new java.sql.Date(invoice_date_util.getTime());
				} catch (ParseException e) {
					e.printStackTrace();
				}
				/*************************** 2.開始收帳退回 ***************************************/
				accreceiveService = new AccreceiveService();
				accreceiveService.receiveAccountTotDB(receivable_id, user_id);
				/*************************** 3.刪除完成,準備轉交(Send the Success view) ***********/
				accreceiveService = new AccreceiveService();
				List<AccreceiveVO> list = accreceiveService.searchAccountReceiveByDayDB(group_id, receive_date, receive_date);
				Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd").create();
				String jsonStrList = gson.toJson(list);
				response.getWriter().write(jsonStrList);
				return;// 程式中斷
				/*************************** 其他可能的錯誤處理 **********************************/
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		if ("delete_receive_account".equals(action)) {
			try {
				/*************************** 1.接收請求參數 ***************************************/
				String receivable_id = request.getParameter("receivable_id");
				String start_date = request.getParameter("start_date");
				String end_date = request.getParameter("end_date");
				/*************************** 2.開始收帳退回 ***************************************/
				accreceiveService = new AccreceiveService();
				accreceiveService.delReceiveAccountTotDB(receivable_id, user_id);
				/*************************** 3.刪除完成,準備轉交(Send the Success view) ***********/
				// 假如無查詢條件，則是查詢全部
				if ((start_date == null || (start_date.trim()).length() == 0)&(end_date == null || (end_date.trim()).length() == 0)) {
					accreceiveService = new AccreceiveService();
					List<AccreceiveVO> list = accreceiveService.searchAccountNotReceiveDB(group_id);
					Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd").create();
					String jsonStrList = gson.toJson(list);
					response.getWriter().write(jsonStrList);
					return;// 程式中斷
				}
				// 假如有一開始查詢日期的話，查詢指定日期
				if ((start_date != null || (start_date.trim()).length() != 0)&(end_date != null || (end_date.trim()).length() != 0)) {
					accreceiveService = new AccreceiveService();
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
					List<AccreceiveVO> list = accreceiveService.searchAccountNotReceiveByDayDB(group_id, amount_start_date, amount_end_date);
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
	}

	protected int DateConversionToDigital(String Date) {

		StringBuffer str = new StringBuffer();
		String[] dateArray = Date.split("-");
		for (String i : dateArray) {
			str.append(i);
		}
		return Integer.parseInt(str.toString());
	}

	interface accreceive_interface {

		public void receiveAccountTotDB(String receivable_id, String user_id);

		public void delReceiveAccountTotDB(String receivable_id, String user_id);

		public List<AccreceiveVO> searchAccountNotReceiveDB(String group_id);

		public List<AccreceiveVO> searchAccountReceiveDB(String group_id);

		public List<AccreceiveVO> searchAllAccountDB(String group_id);

		public List<AccreceiveVO> searchAccountNotReceiveByDayDB(String group_id, Date start_date, Date end_date);

		public List<AccreceiveVO> searchAccountReceiveByDayDB(String group_id, Date start_date, Date end_date);
	}

	class AccreceiveService {
		private accreceive_interface dao;

		public AccreceiveService() {
			dao = new AccreceiveDAO();
		}

		public void receiveAccountTotDB(String receivable_id, String user_id) {
			dao.receiveAccountTotDB(receivable_id, user_id);
		}

		public void delReceiveAccountTotDB(String receivable_id, String user_id) {
			dao.delReceiveAccountTotDB(receivable_id, user_id);
		}

		public List<AccreceiveVO> searchAccountNotReceiveDB(String group_id) {
			return dao.searchAccountNotReceiveDB(group_id);
		}

		public List<AccreceiveVO> searchAccountReceiveDB(String group_id) {
			return dao.searchAccountReceiveDB(group_id);
		}

		public List<AccreceiveVO> searchAllAccountDB(String group_id) {
			return dao.searchAllAccountDB(group_id);
		}

		public List<AccreceiveVO> searchAccountNotReceiveByDayDB(String group_id, Date start_date, Date end_date) {
			return dao.searchAccountNotReceiveByDayDB(group_id, start_date, end_date);
		}

		public List<AccreceiveVO> searchAccountReceiveByDayDB(String group_id, Date start_date, Date end_date) {
			return dao.searchAccountReceiveByDayDB(group_id, start_date, end_date);
		}
	}

	class AccreceiveDAO implements accreceive_interface {
		private static final String sp_selectall_account_receivable = "call sp_selectall_account_receivable (?)";
		private static final String sp_select_account_receive = "call sp_select_account_receive (?)";
		private static final String sp_select_account_not_receive = "call sp_select_account_not_receive (?)";
		private static final String sp_select_account_notreceive_bydate = "call sp_select_account_notreceive_bydate(?,?,?)";
		private static final String sp_select_account_receive_bydate = "call sp_select_account_receive_bydate(?,?,?)";
		private static final String sp_receive_account = "call sp_receive_account(?,?)";
		private static final String sp_del_receive_account = "call sp_del_receive_account(?,?)";

		private final String dbURL = getServletConfig().getServletContext().getInitParameter("dbURL")
				+ "?useUnicode=true&characterEncoding=utf-8&useSSL=false";
		private final String dbUserName = getServletConfig().getServletContext().getInitParameter("dbUserName");
		private final String dbPassword = getServletConfig().getServletContext().getInitParameter("dbPassword");

		@Override
		public void receiveAccountTotDB(String receivable_id, String user_id) {
			Connection con = null;
			PreparedStatement pstmt = null;
			try {
				Class.forName("com.mysql.jdbc.Driver");
				con = DriverManager.getConnection(dbURL, dbUserName, dbPassword);
				pstmt = con.prepareStatement(sp_receive_account);

				pstmt.setString(1, receivable_id);
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
		public void delReceiveAccountTotDB(String receivable_id, String user_id) {
			Connection con = null;
			PreparedStatement pstmt = null;
			try {
				Class.forName("com.mysql.jdbc.Driver");
				con = DriverManager.getConnection(dbURL, dbUserName, dbPassword);
				pstmt = con.prepareStatement(sp_del_receive_account);

				pstmt.setString(1, receivable_id);
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
		public List<AccreceiveVO> searchAccountNotReceiveDB(String group_id) {
			List<AccreceiveVO> list = new ArrayList<AccreceiveVO>();
			AccreceiveVO accreceiveVO = null;

			Connection con = null;
			PreparedStatement pstmt = null;
			ResultSet rs = null;

			try {
				Class.forName("com.mysql.jdbc.Driver");
				con = DriverManager.getConnection(dbURL, dbUserName, dbPassword);
				pstmt = con.prepareStatement(sp_select_account_not_receive);
				pstmt.setString(1, group_id);
				rs = pstmt.executeQuery();
				while (rs.next()) {
					accreceiveVO = new AccreceiveVO();
					accreceiveVO.setReceivable_id(rs.getString("receivable_id"));
					accreceiveVO.setSeq_no(rs.getString("seq_no"));
					accreceiveVO.setProduct_id(rs.getString("product_id"));
					accreceiveVO.setAmount(rs.getFloat("amount"));
					accreceiveVO.setAmount_date(rs.getDate("amount_date"));
					accreceiveVO.setReceive_amount(rs.getFloat("receive_amount"));
					accreceiveVO.setReceive_date(rs.getDate("receive_date"));
					accreceiveVO.setUser_id(rs.getString("user_id"));
					accreceiveVO.setMemo(rs.getString("memo"));
					accreceiveVO.setOrder_source(rs.getString("order_source"));
					accreceiveVO.setOrder_no(rs.getString("order_no"));
					list.add(accreceiveVO); // Store the row in the list
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
		public List<AccreceiveVO> searchAccountReceiveDB(String group_id) {
			List<AccreceiveVO> list = new ArrayList<AccreceiveVO>();
			AccreceiveVO accreceiveVO = null;

			Connection con = null;
			PreparedStatement pstmt = null;
			ResultSet rs = null;

			try {
				Class.forName("com.mysql.jdbc.Driver");
				con = DriverManager.getConnection(dbURL, dbUserName, dbPassword);
				pstmt = con.prepareStatement(sp_select_account_receive);
				pstmt.setString(1, group_id);
				rs = pstmt.executeQuery();
				while (rs.next()) {
					accreceiveVO = new AccreceiveVO();
					accreceiveVO.setReceivable_id(rs.getString("receivable_id"));
					accreceiveVO.setSeq_no(rs.getString("seq_no"));
					accreceiveVO.setProduct_id(rs.getString("product_id"));
					accreceiveVO.setAmount(rs.getFloat("amount"));
					accreceiveVO.setAmount_date(rs.getDate("amount_date"));
					accreceiveVO.setReceive_amount(rs.getFloat("receive_amount"));
					accreceiveVO.setReceive_date(rs.getDate("receive_date"));
					accreceiveVO.setUser_id(rs.getString("user_id"));
					accreceiveVO.setMemo(rs.getString("memo"));
					accreceiveVO.setOrder_source(rs.getString("order_source"));
					accreceiveVO.setOrder_no(rs.getString("order_no"));
					list.add(accreceiveVO); // Store the row in the list
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
		public List<AccreceiveVO> searchAllAccountDB(String group_id) {
			List<AccreceiveVO> list = new ArrayList<AccreceiveVO>();
			AccreceiveVO accreceiveVO = null;

			Connection con = null;
			PreparedStatement pstmt = null;
			ResultSet rs = null;

			try {
				Class.forName("com.mysql.jdbc.Driver");
				con = DriverManager.getConnection(dbURL, dbUserName, dbPassword);
				pstmt = con.prepareStatement(sp_selectall_account_receivable);
				pstmt.setString(1, group_id);
				rs = pstmt.executeQuery();
				while (rs.next()) {
					accreceiveVO = new AccreceiveVO();
					accreceiveVO.setReceivable_id(rs.getString("receivable_id"));
					accreceiveVO.setSeq_no(rs.getString("seq_no"));
					accreceiveVO.setProduct_id(rs.getString("product_id"));
					accreceiveVO.setAmount(rs.getFloat("amount"));
					accreceiveVO.setAmount_date(rs.getDate("amount_date"));
					accreceiveVO.setReceive_amount(rs.getFloat("receive_amount"));
					accreceiveVO.setReceive_date(rs.getDate("receive_date"));
					accreceiveVO.setUser_id(rs.getString("user_id"));
					accreceiveVO.setMemo(rs.getString("memo"));
					accreceiveVO.setOrder_source(rs.getString("order_source"));
					accreceiveVO.setOrder_no(rs.getString("order_no"));
					list.add(accreceiveVO); // Store the row in the list
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
		public List<AccreceiveVO> searchAccountNotReceiveByDayDB(String group_id, Date start_date, Date end_date) {
			List<AccreceiveVO> list = new ArrayList<AccreceiveVO>();
			AccreceiveVO accreceiveVO = null;

			Connection con = null;
			PreparedStatement pstmt = null;
			ResultSet rs = null;

			try {
				Class.forName("com.mysql.jdbc.Driver");
				con = DriverManager.getConnection(dbURL, dbUserName, dbPassword);
				pstmt = con.prepareStatement(sp_select_account_notreceive_bydate);
				pstmt.setString(1, group_id);
				pstmt.setDate(2, start_date);
				pstmt.setDate(3, end_date);
				rs = pstmt.executeQuery();
				while (rs.next()) {
					accreceiveVO = new AccreceiveVO();
					accreceiveVO.setReceivable_id(rs.getString("receivable_id"));
					accreceiveVO.setSeq_no(rs.getString("seq_no"));
					accreceiveVO.setProduct_id(rs.getString("product_id"));
					accreceiveVO.setAmount(rs.getFloat("amount"));
					accreceiveVO.setAmount_date(rs.getDate("amount_date"));
					accreceiveVO.setReceive_amount(rs.getFloat("receive_amount"));
					accreceiveVO.setReceive_date(rs.getDate("receive_date"));
					accreceiveVO.setUser_id(rs.getString("user_id"));
					accreceiveVO.setMemo(rs.getString("memo"));
					accreceiveVO.setOrder_source(rs.getString("order_source"));
					accreceiveVO.setOrder_no(rs.getString("order_no"));
					list.add(accreceiveVO); // Store the row in the list
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
		public List<AccreceiveVO> searchAccountReceiveByDayDB(String group_id, Date start_date, Date end_date) {
			List<AccreceiveVO> list = new ArrayList<AccreceiveVO>();
			AccreceiveVO accreceiveVO = null;

			Connection con = null;
			PreparedStatement pstmt = null;
			ResultSet rs = null;

			try {
				Class.forName("com.mysql.jdbc.Driver");
				con = DriverManager.getConnection(dbURL, dbUserName, dbPassword);
				pstmt = con.prepareStatement(sp_select_account_receive_bydate);
				pstmt.setString(1, group_id);
				pstmt.setDate(2, start_date);
				pstmt.setDate(3, end_date);
				rs = pstmt.executeQuery();
				while (rs.next()) {
					accreceiveVO = new AccreceiveVO();
					accreceiveVO.setReceivable_id(rs.getString("receivable_id"));
					accreceiveVO.setSeq_no(rs.getString("seq_no"));
					accreceiveVO.setProduct_id(rs.getString("product_id"));
					accreceiveVO.setAmount(rs.getFloat("amount"));
					accreceiveVO.setAmount_date(rs.getDate("amount_date"));
					accreceiveVO.setReceive_amount(rs.getFloat("receive_amount"));
					accreceiveVO.setReceive_date(rs.getDate("receive_date"));
					accreceiveVO.setUser_id(rs.getString("user_id"));
					accreceiveVO.setMemo(rs.getString("memo"));
					accreceiveVO.setOrder_source(rs.getString("order_source"));
					accreceiveVO.setOrder_no(rs.getString("order_no"));
					list.add(accreceiveVO); // Store the row in the list
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
