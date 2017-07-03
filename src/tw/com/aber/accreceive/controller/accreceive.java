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

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import tw.com.aber.vo.AccreceiveVO;

public class accreceive extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private static final Logger logger = LogManager.getLogger(accreceive.class);

	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		request.setCharacterEncoding("UTF-8");
		response.setCharacterEncoding("UTF-8");
		AccreceiveService accreceiveService = null;
		String action = request.getParameter("action");

		logger.debug("action:" + action);

		String group_id = request.getSession().getAttribute("group_id").toString();
		String user_id = request.getSession().getAttribute("user_id").toString();
		if ("searh_amount_date".equals(action)) {
			try {
				String start_date = request.getParameter("start_date");
				String end_date = request.getParameter("end_date");

				logger.debug("start_date:" + start_date);
				logger.debug("end_date:" + end_date);

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
						accreceiveService = new AccreceiveService();
						List<AccreceiveVO> list = accreceiveService.searchAccountNotReceiveByDayDB(group_id,
								amount_start_date, amount_end_date);

						Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd").create();
						String jsonStrList = gson.toJson(list);

						logger.debug(jsonStrList);
						response.getWriter().write(jsonStrList);
						return;// 程式中斷
					}
				}
				// 假如無查詢條件，則是查詢全部
				if (start_date.trim().length() == 0 & end_date.trim().length() == 0) {
					accreceiveService = new AccreceiveService();
					List<AccreceiveVO> list = accreceiveService.searchAccountNotReceiveDB(group_id);
					Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd").create();
					String jsonStrList = gson.toJson(list);
					response.getWriter().write(jsonStrList);
					return;// 程式中斷
				}
				/*************************** 其他可能的錯誤處理 **********************************/
			} catch (Exception e) {
				e.printStackTrace();
			}
		} else if ("searh_receive_date".equals(action)) {
			try {
				String start_date = request.getParameter("start_date");
				String end_date = request.getParameter("end_date");

				logger.debug("start_date:" + start_date);
				logger.debug("end_date:" + end_date);

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

						List<AccreceiveVO> list = accreceiveService.searchAccountReceiveByDayDB(group_id,
								receive_start_date, receive_end_date);

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
					Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd").create();
					String jsonStrList = gson.toJson(list);
					response.getWriter().write(jsonStrList);
					return;// 程式中斷
				}
				/*************************** 其他可能的錯誤處理 **********************************/
			} catch (Exception e) {
				e.printStackTrace();
			}
		} else if ("receive_account".equals(action)) {
			try {
				String order_nos = request.getParameter("order_nos");
				String[] order_no_arr = order_nos.split("~");

				logger.debug("order_nos:" + order_nos);

				accreceiveService = new AccreceiveService();
				accreceiveService.receiveAccountTotDB(group_id, order_no_arr, user_id);

			} catch (Exception e) {
				e.printStackTrace();
			}
		} else if ("delete_receive_account".equals(action)) {
			try {
				/***************************
				 * 1.接收請求參數
				 ***************************************/

				String order_nos = request.getParameter("order_nos");
				String[] order_no_arr = order_nos.split("~");

				/***************************
				 * 2.開始收帳退回
				 ***************************************/
				accreceiveService = new AccreceiveService();

				accreceiveService.delReceiveAccountTotDB(group_id, order_no_arr, user_id);

				/*************************** 其他可能的錯誤處理 **********************************/
			} catch (Exception e) {
				e.printStackTrace();
			}
		} else if ("update_receivable_noreceive".equals(action)) {
			try {
				String order_no = request.getParameter("order_no");
				String amount_str = request.getParameter("amount");
				Float amount = null;
				String amount_date_str = request.getParameter("amount_date");
				String memo = request.getParameter("memo");
				String receivable_id = request.getParameter("receivable_id");
				Boolean isSuccess = false;

				if ("".equals(amount_str)) {
					amount = null;
				} else {
					amount = Float.valueOf(amount_str);

					SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
					java.util.Date parsed = sdf.parse(amount_date_str);
					java.sql.Date amount_date = new java.sql.Date(parsed.getTime());

					AccreceiveVO accreceiveVO = new AccreceiveVO();
					accreceiveVO.setGroup_id(group_id);
					accreceiveVO.setOrder_no(order_no);
					accreceiveVO.setMemo(memo);
					accreceiveVO.setAmount(amount);
					accreceiveVO.setAmount_date(amount_date);
					accreceiveVO.setReceivable_id(receivable_id);

					accreceiveService = new AccreceiveService();

					isSuccess = accreceiveService.updateReceiveAccountNoReceive(accreceiveVO);
				}

				if (isSuccess) {
					response.getWriter().write("success");
				} else {
					response.getWriter().write("error");
				}

			} catch (Exception e) {
				e.printStackTrace();
			}

		} else if ("update_receivable_receive".equals(action)) {
			try {
				String order_no = request.getParameter("order_no");
				String receive_amount_str = request.getParameter("receive_amount");

				Float receive_amount = null;
				String receive_date_str = request.getParameter("receive_date");
				String memo = request.getParameter("memo");
				String receivable_id = request.getParameter("receivable_id");
				Boolean isSuccess = false;

				if ("".equals(receive_amount_str)) {
					receive_amount = null;
				} else {
					receive_amount = Float.valueOf(receive_amount_str);

					SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
					java.util.Date parsed = sdf.parse(receive_date_str);
					java.sql.Date receive_date = new java.sql.Date(parsed.getTime());

					AccreceiveVO accreceiveVO = new AccreceiveVO();
					accreceiveVO.setGroup_id(group_id);
					accreceiveVO.setOrder_no(order_no);
					accreceiveVO.setMemo(memo);
					accreceiveVO.setReceive_amount(receive_amount);
					accreceiveVO.setReceive_date(receive_date);
					accreceiveVO.setReceivable_id(receivable_id);

					accreceiveService = new AccreceiveService();

					isSuccess = accreceiveService.updateReceiveAccountReceive(accreceiveVO);
				}

				if (isSuccess) {
					response.getWriter().write("success");
				} else {
					response.getWriter().write("error");
				}

			} catch (Exception e) {
				e.printStackTrace();
			}

		} else if ("delete_receivable".equals(action)) {

			try {

				String order_no = request.getParameter("order_no");
				AccreceiveVO accreceiveVO = new AccreceiveVO();
				accreceiveVO.setGroup_id(group_id);

				accreceiveVO.setOrder_no(order_no);

				accreceiveService = new AccreceiveService();

				Boolean isSuccess = accreceiveService.deleteReceiveAccount(accreceiveVO);

				if (isSuccess) {
					response.getWriter().write("success");
				} else {
					response.getWriter().write("error");
				}

			} catch (Exception e) {
				e.printStackTrace();
			}

		} else if ("getAccreceiveVOByOrderNO".equals(action)) {
			try {
				String order_no = request.getParameter("order_no");

				logger.debug("order_no :" + order_no);

				AccreceiveVO accreceiveVO = new AccreceiveVO();
				accreceiveVO.setGroup_id(group_id);
				accreceiveVO.setOrder_no(order_no);

				accreceiveService = new AccreceiveService();
				List<AccreceiveVO> accreceiveVOList = accreceiveService.getAccreceiveVOListByOrderNO(accreceiveVO);

				Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd").create();
				String jsonStrList = gson.toJson(accreceiveVOList);
				response.getWriter().write(jsonStrList);

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

		public void receiveAccountTotDB(String group_id, String[] order_no_arr, String user_id);

		public void delReceiveAccountTotDB(String group_id, String[] order_no_arr, String user_id);

		public List<AccreceiveVO> searchAccountNotReceiveDB(String group_id);

		public List<AccreceiveVO> searchAccountReceiveDB(String group_id);

		public List<AccreceiveVO> searchAllAccountDB(String group_id);

		public List<AccreceiveVO> searchAccountNotReceiveByDayDB(String group_id, Date start_date, Date end_date);

		public List<AccreceiveVO> searchAccountReceiveByDayDB(String group_id, Date start_date, Date end_date);

		public List<AccreceiveVO> getAccreceiveVOListByOrderNO(AccreceiveVO accreceiveVO);

		public Boolean updateReceiveAccountNoReceive(AccreceiveVO accreceiveVO);

		public Boolean updateReceiveAccountReceive(AccreceiveVO accreceiveVO);

		public Boolean deleteReceiveAccount(AccreceiveVO accreceiveVO);
	}

	class AccreceiveService {
		private accreceive_interface dao;

		public AccreceiveService() {
			dao = new AccreceiveDAO();
		}

		public Boolean updateReceiveAccountNoReceive(AccreceiveVO accreceiveVO) {
			return dao.updateReceiveAccountNoReceive(accreceiveVO);
		}

		public Boolean updateReceiveAccountReceive(AccreceiveVO accreceiveVO) {
			return dao.updateReceiveAccountReceive(accreceiveVO);
		}

		public Boolean deleteReceiveAccount(AccreceiveVO accreceiveVO) {
			return dao.deleteReceiveAccount(accreceiveVO);
		}

		public void receiveAccountTotDB(String group_id, String[] order_no_arr, String user_id) {
			dao.receiveAccountTotDB(group_id, order_no_arr, user_id);
		}

		public void delReceiveAccountTotDB(String group_id, String[] order_no_arr, String user_id) {
			dao.delReceiveAccountTotDB(group_id, order_no_arr, user_id);
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

		public List<AccreceiveVO> getAccreceiveVOListByOrderNO(AccreceiveVO accreceiveVO) {
			return dao.getAccreceiveVOListByOrderNO(accreceiveVO);
		}

	}

	class AccreceiveDAO implements accreceive_interface {
		private static final String sp_selectall_account_receivable = "call sp_selectall_account_receivable (?)";
		private static final String sp_select_account_receive = "call sp_select_account_receive (?)";
		private static final String sp_select_account_not_receive = "call sp_select_account_not_receive (?)";
		private static final String sp_select_account_notreceive_bydate = "call sp_select_account_notreceive_bydate(?,?,?)";
		private static final String sp_select_account_receive_bydate = "call sp_select_account_receive_bydate(?,?,?)";
		private static final String sp_receive_account = "call sp_receive_account(?,?,?)";
		private static final String sp_del_receive_account = "call sp_del_receive_account(?,?,?)";
		private static final String sp_update_receive_account_amount = "call sp_update_receive_account_amount(?,?,?,?,?)";
		private static final String sp_update_receive_account_receive_amount = "call sp_update_receive_account_receive_amount(?,?,?,?,?)";
		private static final String sp_delete_receive_account = "call sp_delete_receive_account(?,?)";
		private static final String sp_get_accreceivevo_list_by_order_no = "call sp_get_accreceivevo_list_by_order_no(?,?)";

		private final String dbURL = getServletConfig().getServletContext().getInitParameter("dbURL")
				+ "?useUnicode=true&characterEncoding=utf-8&useSSL=false";
		private final String dbUserName = getServletConfig().getServletContext().getInitParameter("dbUserName");
		private final String dbPassword = getServletConfig().getServletContext().getInitParameter("dbPassword");

		@Override
		public void receiveAccountTotDB(String group_id, String[] order_no_arr, String user_id) {
			Connection con = null;
			PreparedStatement pstmt = null;
			try {
				Class.forName("com.mysql.jdbc.Driver");
				con = DriverManager.getConnection(dbURL, dbUserName, dbPassword);

				for (int i = 0; i < order_no_arr.length; i++) {
					pstmt = con.prepareStatement(sp_receive_account);
					pstmt.setString(1, group_id);
					pstmt.setString(2, order_no_arr[i]);
					pstmt.setString(3, user_id);
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
		public void delReceiveAccountTotDB(String group_id, String[] order_no_arr, String user_id) {
			Connection con = null;
			PreparedStatement pstmt = null;
			try {
				Class.forName("com.mysql.jdbc.Driver");
				con = DriverManager.getConnection(dbURL, dbUserName, dbPassword);

				for (int i = 0; i < order_no_arr.length; i++) {
					pstmt = con.prepareStatement(sp_del_receive_account);
					pstmt.setString(1, group_id);
					pstmt.setString(2, order_no_arr[i]);
					pstmt.setString(3, user_id);
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
					accreceiveVO.setAmount(rs.getFloat("amount"));
					accreceiveVO.setAmount_date(rs.getDate("amount_date"));
					accreceiveVO.setReceive_amount(rs.getFloat("receive_amount"));
					accreceiveVO.setReceive_date(rs.getDate("receive_date"));
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
					accreceiveVO.setAmount(rs.getFloat("amount"));
					accreceiveVO.setAmount_date(rs.getDate("amount_date"));
					accreceiveVO.setReceive_amount(rs.getFloat("receive_amount"));
					accreceiveVO.setReceive_date(rs.getDate("receive_date"));
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
		public Boolean deleteReceiveAccount(AccreceiveVO accreceiveVO) {
			Connection con = null;
			PreparedStatement pstmt = null;
			ResultSet rs = null;
			Boolean isSuccess = false;

			try {
				Class.forName("com.mysql.jdbc.Driver");
				con = DriverManager.getConnection(dbURL, dbUserName, dbPassword);

				pstmt = con.prepareStatement(sp_delete_receive_account);

				pstmt.setString(1, accreceiveVO.getGroup_id());
				pstmt.setString(2, accreceiveVO.getOrder_no());

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
		public List<AccreceiveVO> getAccreceiveVOListByOrderNO(AccreceiveVO accreceiveVO) {
			List<AccreceiveVO> list = new ArrayList<AccreceiveVO>();

			Connection con = null;
			PreparedStatement pstmt = null;
			ResultSet rs = null;

			try {
				Class.forName("com.mysql.jdbc.Driver");
				con = DriverManager.getConnection(dbURL, dbUserName, dbPassword);
				pstmt = con.prepareStatement(sp_get_accreceivevo_list_by_order_no);
				pstmt.setString(1, accreceiveVO.getGroup_id());
				pstmt.setString(2, accreceiveVO.getOrder_no());
				rs = pstmt.executeQuery();
				while (rs.next()) {
					accreceiveVO = new AccreceiveVO();
					accreceiveVO.setProduct_name(rs.getString("product_name"));
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
		public Boolean updateReceiveAccountNoReceive(AccreceiveVO accreceiveVO) {
			Connection con = null;
			PreparedStatement pstmt = null;
			ResultSet rs = null;
			Boolean isSuccess = false;

			try {
				Class.forName("com.mysql.jdbc.Driver");
				con = DriverManager.getConnection(dbURL, dbUserName, dbPassword);
				pstmt = con.prepareStatement(sp_update_receive_account_amount);

				pstmt.setString(1, accreceiveVO.getGroup_id());
				pstmt.setFloat(2, accreceiveVO.getAmount());
				pstmt.setString(3, accreceiveVO.getAmount_date().toString());
				pstmt.setString(4, accreceiveVO.getMemo());
				pstmt.setString(5, accreceiveVO.getReceivable_id());

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
		public Boolean updateReceiveAccountReceive(AccreceiveVO accreceiveVO) {
			Connection con = null;
			PreparedStatement pstmt = null;
			ResultSet rs = null;
			Boolean isSuccess = false;

			try {
				Class.forName("com.mysql.jdbc.Driver");
				con = DriverManager.getConnection(dbURL, dbUserName, dbPassword);

				pstmt = con.prepareStatement(sp_update_receive_account_receive_amount);

				pstmt.setString(1, accreceiveVO.getGroup_id());
				pstmt.setFloat(2, accreceiveVO.getReceive_amount());
				pstmt.setString(3, accreceiveVO.getReceive_date().toString());
				pstmt.setString(4, accreceiveVO.getMemo());
				pstmt.setString(5, accreceiveVO.getReceivable_id());

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
