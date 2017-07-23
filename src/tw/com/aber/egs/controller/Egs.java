package tw.com.aber.egs.controller;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import tw.com.aber.vo.GroupVO;

public class Egs extends HttpServlet {

	private static final long serialVersionUID = 1L;
	private static final Logger logger = LogManager.getLogger(Egs.class);

	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		doPost(request, response);
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		request.setCharacterEncoding("UTF-8");
		response.setCharacterEncoding("UTF-8");

		String groupId = (String) request.getSession().getAttribute("group_id");
		String action = (String) request.getParameter("action");
		String command = null, params = null;
		String[] paramsArr = null;
		EgsApi api = null;

		logger.debug("Action:".concat(action));

		if ("transfer_waybill".equals(action)) {

			// 訂單編號
			String orderNo = request.getParameter("orderNo");
			logger.debug("[訂單編號] orderNo: " + orderNo);

			// 溫層
			String temperature = request.getParameter("temperature");
			logger.debug("[溫層] temperature: " + temperature);

			// 尺寸
			String package_size = request.getParameter("package_size");
			logger.debug("[尺吋] temperature: " + package_size);

			// 指定配達日期
			String delivery_date = request.getParameter("delivery_date");
			logger.debug("[指定配達日期] delivery_date: " + delivery_date);

			// 指定配達時段
			String delivery_timezone = request.getParameter("delivery_timezone");
			logger.debug("[指定配達時段] delivery_timezone: " + delivery_timezone);

			// 品名
			String product_name = request.getParameter("product_name");
			logger.debug("[品名] product_name: " + product_name);
			
			// 選取的單筆出貨單明細id
			String shipIds = request.getParameter("shipIds");
			logger.debug("[選取的單筆出貨單明細id] shipIds: " + shipIds);
			
			// 備註
			String comment = request.getParameter("comment");
			logger.debug("[備註] comment: " + comment);

			// 收件人姓名
			String receiver_name = request.getParameter("receiver_name");
			logger.debug("[收件人姓名] receiver_name: " + receiver_name);

			// 收件人地址
			String receiver_address = request.getParameter("receiver_address");
			logger.debug("[收件人地址] receiver_address: " + receiver_address);

			// 收件人地址的速達五碼郵遞區號
			api = new EgsApi();
			String receiver_suda5 = api.querySuda5("query_suda5", receiver_address);
			receiver_suda5 = receiver_suda5.split("&")[1].split("=")[1];
			logger.debug("[收件人地址的速達五碼郵遞區號] receiver_suda5: " + receiver_suda5);

			EgsService service = new EgsService();
			GroupVO groupVO = service.getGroupByGroupId(groupId);

			// 寄件人姓名
			String sender_name = groupVO.getGroup_name();
			logger.debug("[寄件人姓名] sender_name: " + sender_name);

			// 寄件人地址
			String sender_address = groupVO.getAddress();
			logger.debug("[寄件人地址] sender_address: " + sender_address);

			// 寄件人地址的速達五碼郵遞區號
			api = new EgsApi();
			String sender_suda5 = api.querySuda5("query_suda5", sender_address);
			sender_suda5 = sender_suda5.split("&")[1].split("=")[1];
			logger.debug("[寄件人地址的速達五碼郵遞區號] receiver_suda5: " + sender_suda5);

			// 寄件人電話
			String sender_phone = groupVO.getPhone();
			logger.debug("[寄件人電話] sender_phone: " + sender_phone);

			// 距離 00:同縣市 01:外縣市 02:離島
			api = new EgsApi();
			command = "query_distance";

			paramsArr = new String[2];
			paramsArr[0] = "suda5_senderpostcode_1=".concat(sender_suda5);
			paramsArr[1] = "suda5_customerpostcode_1=".concat(receiver_suda5);

			params = api.getParams(paramsArr);
			logger.debug("params: " + params);

			String distance = api.send(command, params);
			distance = distance.split("&")[1].split("=")[1];
			logger.debug("[距離] distance: " + distance);

			/*
			 * 託運單類別 A:一般 B:代收 G:報值
			 */
			String waybill_type = request.getParameter("waybill_type");
			logger.debug("[託運單類別] waybill_type: " + waybill_type);

			// 連線契客代號
			String customer_id = service.getGroupEzcatCustomerIdByGroupId(groupId);
			logger.debug("[連線契客代號] customer_id: " + customer_id);

			// 託運單帳號(託運單帳號 = 連線契客代號)
			String account_id = customer_id;
			logger.debug("[託運單帳號] account_id: " + account_id);

			// 託運單號碼
			String count = "1";
			command = "query_waybill_id_range";

			paramsArr = new String[3];
			paramsArr[0] = "customer_id=".concat(customer_id);
			paramsArr[1] = "waybill_type=".concat(waybill_type);
			paramsArr[2] = "count=".concat(count);

			api = new EgsApi();
			params = api.getParams(paramsArr);
			logger.debug("params: " + params);

			String tracking_number = api.send(command, params);
			tracking_number = tracking_number.split("&")[2].split("=")[1];
			logger.debug("[託運單號碼] tracking_number: " + tracking_number);

			// 取時間
			SimpleDateFormat sdFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss.SSS");
			java.util.Date current = new java.util.Date();

			String nowDate = sdFormat.format(current);
			String create_time = nowDate;
			String print_time = nowDate;
			logger.debug("[建立時間] create_time: " + create_time);
			logger.debug("[列印時間] print_time: " + print_time);

			// 會員編號、關稅、報值金額 default:0
			String member_no = "0", taxin = "0", insurance = "0";
			logger.debug("[會員編號] member_no: " + member_no);
			logger.debug("[關稅] taxin: " + taxin);
			logger.debug("[報值金額] insurance: " + insurance);

		}
	}

	public class EgsDao implements egs_interface {
		private final String dbURL = getServletConfig().getServletContext().getInitParameter("dbURL")
				+ "?useUnicode=true&characterEncoding=utf-8&useSSL=false";
		private final String dbUserName = getServletConfig().getServletContext().getInitParameter("dbUserName");
		private final String dbPassword = getServletConfig().getServletContext().getInitParameter("dbPassword");
		private final String jdbcDriver = getServletConfig().getServletContext().getInitParameter("jdbcDriver");

		private static final String sp_get_group_by_groupId = "call sp_get_group_by_groupId(?)";
		private static final String sp_get_group_ezcat_customer_id_by_group_id = "call sp_get_group_ezcat_customer_id_by_group_id(?)";

		@Override
		public GroupVO getGroupByGroupId(String groupId) {
			GroupVO row = null;

			Connection con = null;
			PreparedStatement pstmt = null;
			ResultSet rs = null;
			try {
				Class.forName(jdbcDriver);
				con = DriverManager.getConnection(dbURL, dbUserName, dbPassword);
				pstmt = con.prepareStatement(sp_get_group_by_groupId);

				pstmt.setString(1, groupId);

				rs = pstmt.executeQuery();
				while (rs.next()) {
					row = new GroupVO();
					row.setGroup_name(rs.getString("group_name"));
					row.setPhone(rs.getString("phone"));
					row.setAddress(rs.getString("address"));
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
			return row;
		}

		@Override
		public String getGroupEzcatCustomerIdByGroupId(String groupId) {
			String customId = null;

			Connection con = null;
			PreparedStatement pstmt = null;
			ResultSet rs = null;
			try {
				Class.forName(jdbcDriver);
				con = DriverManager.getConnection(dbURL, dbUserName, dbPassword);
				pstmt = con.prepareStatement(sp_get_group_ezcat_customer_id_by_group_id);

				pstmt.setString(1, groupId);

				rs = pstmt.executeQuery();
				while (rs.next()) {
					customId = rs.getString("customer_id");
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
			return customId;
		}

	}

	public class EgsService {
		private egs_interface dao;

		public EgsService() {
			dao = new EgsDao();
		}

		public GroupVO getGroupByGroupId(String groupId) {
			return dao.getGroupByGroupId(groupId);
		}

		public String getGroupEzcatCustomerIdByGroupId(String groupId) {
			return dao.getGroupEzcatCustomerIdByGroupId(groupId);
		}

	}

	interface egs_interface {

		public GroupVO getGroupByGroupId(String groupId);

		public String getGroupEzcatCustomerIdByGroupId(String groupId);

	}
}
