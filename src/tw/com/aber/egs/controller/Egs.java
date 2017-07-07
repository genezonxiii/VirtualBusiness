package tw.com.aber.egs.controller;

import java.io.IOException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import tw.com.aber.egs.vo.EgsVO;
import tw.com.aber.vo.GroupVO;
import tw.com.aber.vo.ShipVO;

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
			String result = "{\"status\": \"ERR\"}";
			try {
				EgsService service = new EgsService();

				// 訂單編號
				String orderNo = request.getParameter("orderNo");
				logger.debug("[訂單編號] orderNo: " + orderNo);

				// 以訂單編號查詢同訂單所需資訊
				ShipVO shipVO = service.getShipByOrderno(groupId, orderNo);

				// 選取的多筆出貨單id
				String shipIds = shipVO.getShip_id();
				logger.debug("[選取的單筆出貨單id] shipIds: " + shipIds);

				// 選取的多筆出貨單明細id
				String realsaleIds = shipVO.getRealsale_id();
				logger.debug("[選取的單筆出貨單明細id] realsaleIds: " + realsaleIds);

				// 溫層
				String temperature = request.getParameter("temperature");
				logger.debug("[溫層] temperature: " + temperature);

				// 尺寸
				String package_size = request.getParameter("package_size");
				logger.debug("[尺吋] package_size: " + package_size);

				// 指定配達日期
				String delivery_date = request.getParameter("delivery_date");
				logger.debug("[指定配達日期] delivery_date: " + delivery_date);

				// 指定配達時段
				String delivery_timezone = request.getParameter("delivery_timezone");
				logger.debug("[指定配達時段] delivery_timezone: " + delivery_timezone);

				// 品名
				String product_name = request.getParameter("product_name");
				logger.debug("[品名] product_name: " + product_name);

				// 備註
				String comment = request.getParameter("comment");
				logger.debug("[備註] comment: " + comment);

				// 收件人資訊
				EgsVO egsVO = service.getEgsReceiverInfo(realsaleIds);

				// // 收件人姓名
				String receiver_name = egsVO.getReceiver_name();
				logger.debug("[收件人姓名] receiver_name: " + receiver_name);

				// 收件人地址
				String receiver_address = egsVO.getReceiver_address();
				logger.debug("[收件人地址] receiver_address: " + receiver_address);

				// 收件人行動電話
				String receiver_mobile = egsVO.getReceiver_mobile();
				logger.debug("[收件人行動電話] receiver_mobile: " + receiver_mobile);

				// 收件人電話
				String receiver_phone = egsVO.getReceiver_phone();
				logger.debug("[收件人電話] receiver_phone: " + receiver_phone);

				// 收件人地址的速達五碼郵遞區號
				api = new EgsApi();
				String receiver_suda5 = api.querySuda("query_suda5", receiver_address);
				receiver_suda5 = api.toMap(receiver_suda5).get("suda5_1");
				logger.debug("[收件人地址的速達五碼郵遞區號] receiver_suda5: " + receiver_suda5);

				// 收件人地址的速達七碼郵遞區號
				api = new EgsApi();
				String receiver_suda7 = api.querySuda("query_suda7", receiver_address);
				receiver_suda7 = api.toMap(receiver_suda7).get("suda7_1");
				logger.debug("[收件人地址的速達七碼郵遞區號] receiver_suda7: " + receiver_suda7);

				GroupVO groupVO = service.getGroupByGroupId(groupId);

				// 寄件人姓名
				String sender_name = groupVO.getGroup_name();
				logger.debug("[寄件人姓名] sender_name: " + sender_name);

				// 寄件人地址
				String sender_address = groupVO.getAddress();
				logger.debug("[寄件人地址] sender_address: " + sender_address);

				// 寄件人地址的速達五碼郵遞區號
				api = new EgsApi();
				String sender_suda5 = api.querySuda("query_suda5", sender_address);
				sender_suda5 = api.toMap(sender_suda5).get("suda5_1");
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
				distance = api.toMap(distance).get("distance_1");
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
				tracking_number = api.toMap(tracking_number).get("waybill_id");
				logger.debug("[託運單號碼] tracking_number: " + tracking_number);

				// 取時間
				SimpleDateFormat sdFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss.SSS");
				java.util.Date current = new java.util.Date();

				// 建立時間 & 列印時間 ( 建立時間 = 列印時間 )
				java.sql.Date create_time = null;
				java.sql.Date print_time = null;

				String nowDate = sdFormat.format(current);

				try {
					java.util.Date nowDate_util = sdFormat.parse(nowDate);
					create_time = new java.sql.Date(nowDate_util.getTime());
					print_time = create_time;
				} catch (ParseException e) {
					e.printStackTrace();
				}
				logger.debug("[建立時間] create_time: " + create_time);
				logger.debug("[列印時間] print_time: " + print_time);

				// 代收貨款金額
				String product_price = service.getEgsTotalAmt(realsaleIds);
				logger.debug("[代收貨款金額] product_price: " + product_price);

				// 會員編號、關稅、報值金額 default:0
				String member_no = "0", taxin = "0", insurance = "0";
				logger.debug("[會員編號] member_no: " + member_no);
				logger.debug("[關稅] taxin: " + taxin);
				logger.debug("[報值金額] insurance: " + insurance);

				// 組合傳送單筆託運單資料電文
				paramsArr = new String[26];
				paramsArr[0] = "customer_id=".concat(customer_id);
				paramsArr[1] = "tracking_number=".concat(tracking_number);
				paramsArr[2] = "orderNo=".concat(orderNo);
				paramsArr[3] = "receiver_name=".concat(URLEncoder.encode(receiver_name, "utf8"));
				paramsArr[4] = "receiver_address=".concat(URLEncoder.encode(receiver_address, "utf8"));
				paramsArr[5] = "receiver_suda5=".concat(receiver_suda5);
				paramsArr[6] = "receiver_mobile=".concat(receiver_mobile);
				paramsArr[7] = "receiver_phone=".concat(receiver_phone);
				paramsArr[8] = "sender_name=".concat(URLEncoder.encode(sender_name, "utf8"));
				paramsArr[9] = "sender_address=".concat(URLEncoder.encode(sender_address, "utf8"));
				paramsArr[10] = "sender_suda5=".concat(sender_suda5);
				paramsArr[11] = "sender_phone=".concat(sender_phone);
				paramsArr[12] = "product_price=".concat(product_price);
				paramsArr[13] = "product_name=".concat(URLEncoder.encode(product_name, "utf8"));
				paramsArr[14] = "comment=".concat(URLEncoder.encode(comment, "utf8"));
				paramsArr[15] = "package_size=".concat(package_size);
				paramsArr[16] = "temperature=".concat(temperature);
				paramsArr[17] = "distance=".concat(distance);
				paramsArr[18] = "delivery_date=".concat(delivery_date);
				paramsArr[19] = "delivery_timezone=".concat(delivery_timezone);
				paramsArr[20] = "create_time=".concat(nowDate);
				paramsArr[21] = "print_time=".concat(nowDate);
				paramsArr[22] = "account_id=".concat(account_id);
				paramsArr[23] = "member_no=".concat(member_no);
				paramsArr[24] = "taxin=".concat(taxin);
				paramsArr[25] = "insurance=".concat(insurance);

				api = new EgsApi();
				params = api.getParams(paramsArr);
				logger.debug("params: " + params);

				String apiResponseStr = api.send(action, params);
				logger.debug("apiResponseStr: " + apiResponseStr);

				Map<String, String> resultMap = api.toMap(apiResponseStr);
				
				if(resultMap.get("message")!=null){
					String message = URLDecoder.decode(resultMap.get("message"), "utf8");
					resultMap.put("message", message);
				}

				String status = resultMap.get("status");

				if ("OK".equals(status)) {
					// set value
					egsVO = new EgsVO();
					egsVO.setGroup_id(groupId);
					egsVO.setCustomer_id(customer_id);
					egsVO.setTracking_number(tracking_number);
					egsVO.setOrder_no(orderNo);
					egsVO.setReceiver_name(receiver_name);
					egsVO.setReceiver_address(receiver_address);
					egsVO.setReceiver_suda5(receiver_suda5);
					egsVO.setReceiver_suda7(receiver_suda7);
					egsVO.setReceiver_mobile(receiver_mobile);
					egsVO.setReceiver_phone(receiver_phone);
					egsVO.setSender_name(sender_name);
					egsVO.setSender_address(sender_address);
					egsVO.setSender_suda5(sender_suda5);
					egsVO.setSender_phone(sender_phone);
					egsVO.setProduct_price(product_price);
					egsVO.setProduct_name(product_name);
					egsVO.setEgs_comment(comment);
					egsVO.setPackage_size(package_size);
					egsVO.setTemperature(temperature);
					egsVO.setDistance(distance);
					egsVO.setDelivery_date(delivery_date);
					egsVO.setDelivery_timezone(delivery_timezone);
					egsVO.setCreate_time(create_time);
					egsVO.setPrint_time(print_time);
					egsVO.setAccount_id(account_id);
					egsVO.setMember_no(member_no);
					egsVO.setTaxin(taxin);
					egsVO.setInsurance(insurance);

					// insert to database
					service.insertEgs(egsVO);

					resultMap.put("tracking_number", tracking_number);
				}
				result = new Gson().toJson(resultMap);
			} catch (Exception e) {
				logger.error(e.getMessage());
			}
			logger.debug("result: " + result);
			response.getWriter().write(result);
		} else if ("query_consignment_note".equals(action)) {
			// 訂單編號
			String orderNo = request.getParameter("order_no");
			logger.debug("[訂單編號] orderNo: " + orderNo);

			// 託運單號碼
			String trackingNo = request.getParameter("tracking_number");
			logger.debug("[託運單號碼] trackingNo: " + trackingNo);

			EgsService service = new EgsService();
			List<EgsVO> list = service.getEgsByOrderNoOrTrackingNo(groupId, orderNo, trackingNo);
			Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd").create();
			logger.debug(gson.toJson(list));
			response.getWriter().write(gson.toJson(list));
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
		private static final String sp_get_egs_total_amt = "call sp_get_egs_total_amt(?)";
		private static final String sp_get_egs_receiver_info = "call sp_get_egs_receiver_info(?)";
		private static final String sp_get_ship_by_order_no = "call sp_get_ship_by_order_no(?,?)";
		private static final String sp_insert_egs = "call sp_insert_egs(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
		private static final String sp_select_egs_by_orderno_or_trackingno = "call sp_select_egs_by_orderno_or_trackingno(?,?,?)";

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

		@Override
		public String getEgsTotalAmt(String saleIds) {

			String totalAmt = null;

			Connection con = null;
			PreparedStatement pstmt = null;
			ResultSet rs = null;
			try {
				Class.forName(jdbcDriver);
				con = DriverManager.getConnection(dbURL, dbUserName, dbPassword);
				pstmt = con.prepareStatement(sp_get_egs_total_amt);

				pstmt.setString(1, saleIds);

				rs = pstmt.executeQuery();
				while (rs.next()) {
					totalAmt = rs.getString("total_amt");
				}
				if (totalAmt == null) {
					totalAmt = "0";
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
			return totalAmt;
		}

		@Override
		public EgsVO getEgsReceiverInfo(String realsaleIds) {
			EgsVO row = null;

			Connection con = null;
			PreparedStatement pstmt = null;
			ResultSet rs = null;
			try {
				Class.forName(jdbcDriver);
				con = DriverManager.getConnection(dbURL, dbUserName, dbPassword);
				pstmt = con.prepareStatement(sp_get_egs_receiver_info);

				pstmt.setString(1, realsaleIds);

				rs = pstmt.executeQuery();
				while (rs.next()) {
					row = new EgsVO();
					row.setReceiver_name(rs.getString("receiver_name"));
					row.setReceiver_address(rs.getString("receiver_address"));
					row.setReceiver_phone(rs.getString("receiver_phone"));
					row.setReceiver_mobile(rs.getString("receiver_mobile"));
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
		public ShipVO getShipByOrderno(String groupId, String orderNo) {
			ShipVO shipVO = null;
			String productNames = "", shipIds = "", realsaleIds = "";

			Connection con = null;
			PreparedStatement pstmt = null;
			ResultSet rs = null;
			try {
				Class.forName(jdbcDriver);
				con = DriverManager.getConnection(dbURL, dbUserName, dbPassword);
				pstmt = con.prepareStatement(sp_get_ship_by_order_no);

				pstmt.setString(1, groupId);
				pstmt.setString(2, orderNo);

				rs = pstmt.executeQuery();
				while (rs.next()) {
					shipIds += (',' + rs.getString("ship_id"));
					realsaleIds += (",'" + rs.getString("realsale_id") + "'");
					productNames += (',' + rs.getString("product_name"));
				}

				if (realsaleIds.length() != 0) {
					realsaleIds = realsaleIds.substring(1, realsaleIds.length());
				}

				if (productNames.length() != 0) {
					productNames = productNames.substring(1, productNames.length());
				}

				if (shipIds.length() != 0) {
					shipIds = shipIds.substring(1, shipIds.length());
				}

				shipVO = new ShipVO();
				shipVO.setShip_id(shipIds);
				shipVO.setRealsale_id(realsaleIds);
				shipVO.setV_product_name(productNames);

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
			return shipVO;
		}

		@Override
		public void insertEgs(EgsVO egsVO) {
			Connection con = null;
			CallableStatement cs = null;

			try {
				Class.forName(jdbcDriver);
				con = DriverManager.getConnection(dbURL, dbUserName, dbPassword);
				cs = con.prepareCall(sp_insert_egs);

				cs.setString(1, egsVO.getGroup_id());
				cs.setString(2, egsVO.getCustomer_id());
				cs.setString(3, egsVO.getTracking_number());
				cs.setString(4, egsVO.getOrder_no());
				cs.setString(5, egsVO.getReceiver_name());
				cs.setString(6, egsVO.getReceiver_address());
				cs.setString(7, egsVO.getReceiver_suda5());
				cs.setString(8, egsVO.getReceiver_suda7());
				cs.setString(9, egsVO.getReceiver_mobile());
				cs.setString(10, egsVO.getReceiver_phone());
				cs.setString(11, egsVO.getSender_name());
				cs.setString(12, egsVO.getSender_address());
				cs.setString(13, egsVO.getSender_suda5());
				cs.setString(14, egsVO.getSender_phone());
				cs.setString(15, egsVO.getProduct_price());
				cs.setString(16, egsVO.getProduct_name());
				cs.setString(17, egsVO.getEgs_comment());
				cs.setString(18, egsVO.getPackage_size());
				cs.setString(19, egsVO.getTemperature());
				cs.setString(20, egsVO.getDistance());
				cs.setString(21, egsVO.getDelivery_date());
				cs.setString(22, egsVO.getDelivery_timezone());
				cs.setDate(23, egsVO.getCreate_time());
				cs.setDate(24, egsVO.getPrint_time());
				cs.setString(25, egsVO.getAccount_id());
				cs.setString(26, egsVO.getMember_no());
				cs.setString(27, egsVO.getTaxin());
				cs.setString(28, egsVO.getInsurance());

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
		public List<EgsVO> getEgsByOrderNoOrTrackingNo(String groupId, String orderNo, String trackingNo) {
			List<EgsVO> rows = new ArrayList<EgsVO>();
			EgsVO row = null;

			Connection con = null;
			PreparedStatement pstmt = null;
			ResultSet rs = null;
			try {
				Class.forName(jdbcDriver);
				con = DriverManager.getConnection(dbURL, dbUserName, dbPassword);
				pstmt = con.prepareStatement(sp_select_egs_by_orderno_or_trackingno);

				pstmt.setString(1, groupId);
				if (orderNo == null || orderNo.length() == 0) {
					pstmt.setNull(2, Types.VARCHAR);
					logger.error("pstmt.setNull(2, Types.VARCHAR)");
				} else {
					pstmt.setString(2, orderNo);
				}
				if (trackingNo == null || trackingNo.length() == 0) {
					pstmt.setNull(3, Types.VARCHAR);
					logger.error("pstmt.setNull(3, Types.VARCHAR)");
				} else {
					pstmt.setString(3, trackingNo);
				}

				rs = pstmt.executeQuery();
				while (rs.next()) {
					row = new EgsVO();
					row.setGroup_id(rs.getString("group_id"));
					row.setCustomer_id(rs.getString("customer_id"));
					row.setTracking_number(rs.getString("tracking_number"));
					row.setOrder_no(rs.getString("order_no"));
					row.setReceiver_name(rs.getString("receiver_name"));
					row.setReceiver_address(rs.getString("receiver_address"));
					row.setReceiver_suda5(rs.getString("receiver_suda5"));
					row.setReceiver_suda7(rs.getString("receiver_suda7"));
					row.setReceiver_mobile(rs.getString("receiver_mobile"));
					row.setReceiver_phone(rs.getString("receiver_phone"));
					row.setSender_name(rs.getString("sender_name"));
					row.setSender_address(rs.getString("sender_address"));
					row.setSender_suda5(rs.getString("sender_suda5"));
					row.setSender_phone(rs.getString("sender_phone"));
					row.setProduct_price(rs.getString("product_price"));
					row.setProduct_name(rs.getString("product_name"));
					row.setEgs_comment(rs.getString("egs_comment"));
					row.setPackage_size(rs.getString("package_size"));
					row.setTemperature(rs.getString("temperature"));
					row.setDistance(rs.getString("distance"));
					row.setDelivery_date(rs.getString("delivery_date"));
					row.setDelivery_timezone(rs.getString("delivery_timezone"));
					row.setCreate_time(rs.getDate("create_time"));
					row.setPrint_time(rs.getDate("print_time"));
					row.setAccount_id(rs.getString("account_id"));
					row.setMember_no(rs.getString("member_no"));
					row.setTaxin(rs.getString("taxin"));
					row.setInsurance(rs.getString("insurance"));
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

		public String getEgsTotalAmt(String saleIds) {
			return dao.getEgsTotalAmt(saleIds);
		}

		public EgsVO getEgsReceiverInfo(String realsaleIds) {
			return dao.getEgsReceiverInfo(realsaleIds);
		}

		public ShipVO getShipByOrderno(String groupId, String orderNo) {
			return dao.getShipByOrderno(groupId, orderNo);
		}

		public void insertEgs(EgsVO egsVO) {
			dao.insertEgs(egsVO);
		}

		public List<EgsVO> getEgsByOrderNoOrTrackingNo(String groupId, String orderNo, String trackingNo) {
			return dao.getEgsByOrderNoOrTrackingNo(groupId, orderNo, trackingNo);
		}
	}

	interface egs_interface {

		public GroupVO getGroupByGroupId(String groupId);

		public String getGroupEzcatCustomerIdByGroupId(String groupId);

		public String getEgsTotalAmt(String saleIds);

		public EgsVO getEgsReceiverInfo(String realsaleIds);

		public ShipVO getShipByOrderno(String groupId, String orderNo);

		public List<EgsVO> getEgsByOrderNoOrTrackingNo(String groupId, String orderNo, String trackingNo);

		public void insertEgs(EgsVO egsVO);
	}
}
