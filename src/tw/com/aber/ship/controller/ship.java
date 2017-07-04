package tw.com.aber.ship.controller;

import java.io.IOException;
import java.io.StringWriter;
import java.lang.reflect.Type;
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
import javax.xml.bind.JAXB;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import tw.com.aber.sf.delivery.vo.Body;
import tw.com.aber.sf.delivery.vo.Cargo;
import tw.com.aber.sf.delivery.vo.Order;
import tw.com.aber.sf.delivery.vo.Request;
import tw.com.aber.sf.vo.ResponseUtil;
import tw.com.aber.sf.vo.SaleOrder;
import tw.com.aber.sftransfer.controller.SfApi;
import tw.com.aber.sftransfer.controller.SfDeliveryApi;
import tw.com.aber.sftransfer.controller.ValueService;
import tw.com.aber.util.Util;
import tw.com.aber.vo.RealSaleDetailVO;
import tw.com.aber.vo.ShipDetail;
import tw.com.aber.vo.ShipSFDeliveryVO;
import tw.com.aber.vo.ShipVO;

public class ship extends HttpServlet {

	private static final long serialVersionUID = 1L;
	private static final Logger logger = LogManager.getLogger(ship.class);

	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		doPost(request, response);
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		request.setCharacterEncoding("UTF-8");
		response.setCharacterEncoding("UTF-8");

		Util util = new Util();

		util.ConfirmLoginAgain(request, response);

		String groupId = (String) request.getSession().getAttribute("group_id");
		String userId = (String) request.getSession().getAttribute("user_id");

		ShipService shipService = null;

		String action = request.getParameter("action");
		logger.debug("Action:".concat(action));
		List<ShipVO> rows = null;
		String result = null;
		ShipService service = null;
		ShipVO shipVO = null;
		Gson gson = null;

		try {
			if ("searchBySaleDate".equals(action)) {
				String startStr = request.getParameter("startDate");
				String endStr = request.getParameter("endDate");

				java.util.Date date = null;
				Date startDate = null, endDate = null;
				SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
				try {
					date = sdf.parse(startStr);
					startDate = new java.sql.Date(date.getTime());
					date = sdf.parse(endStr);
					endDate = new java.sql.Date(date.getTime());
				} catch (ParseException e) {
					logger.error("search date convert :".concat(e.getMessage()));
				}
				gson = new GsonBuilder().setDateFormat("yyyy-MM-dd").create();
				service = new ShipService();

				rows = service.getSearchShipBySaleDate(groupId, startDate, endDate);
				result = gson.toJson(rows);
				response.getWriter().write(result);

			} else if ("searchByOrderNo".equals(action)) {
				String orderNo = request.getParameter("orderNo");

				gson = new GsonBuilder().setDateFormat("yyyy-MM-dd").create();
				service = new ShipService();
				shipVO = new ShipVO();

				shipVO.setGroup_id(groupId);
				shipVO.setOrder_no(orderNo);

				rows = service.getSearchShipByOrderNo(shipVO);
				result = gson.toJson(rows);

				response.getWriter().write(result);
			} else if ("sendToTelegraph".equals(action)) {

				List<ShipVO> shipVOList = null;

				try {
					String ship_seq_nos = request.getParameter("ship_seq_nos");

					shipService = new ShipService();
					ship_seq_nos = ship_seq_nos.replace(",", "','");

					ship_seq_nos = "'" + ship_seq_nos + "'";

					shipVOList = shipService.getShipByShipSeqNo(ship_seq_nos, "'" + groupId + "'");

					SfApi sfApi = new SfApi();

					ValueService valueService = util.getValueService(request, response);
					String reqXml = sfApi.genSaleOrderService(shipVOList, valueService);
					String resXml = sfApi.sendXML(reqXml);
					ResponseUtil responseUtil = sfApi.getResponseUtilObj(resXml);
					gson = new GsonBuilder().setDateFormat("yyyy-MM-dd").create();
					String gresult = gson.toJson(responseUtil);
					response.getWriter().write(gresult);
				} catch (Exception e) {
					e.printStackTrace();
					logger.debug(e.getMessage());
				}

			} else if ("sendToCancelSaleOrderService".equals(action)) {

				List<ShipVO> shipVOList = null;
				try {
					/***************************
					 * 1.接收請求參數
					 ***************************************/
					String ship_seq_nos = request.getParameter("ship_seq_nos");

					shipService = new ShipService();
					ship_seq_nos = ship_seq_nos.replace(",", "','");

					ship_seq_nos = "'" + ship_seq_nos + "'";

					shipVOList = shipService.getShipByShipSeqNo(ship_seq_nos, "'" + groupId + "'");

					SfApi sfApi = new SfApi();
					logger.debug("ship_seq_nos =" + ship_seq_nos);

					ValueService valueService = util.getValueService(request, response);

					String reqXml = sfApi.genCancelSaleOrderService(shipVOList, valueService);
					String resXml = sfApi.sendXML(reqXml);
					ResponseUtil responseUtil = sfApi.getResponseUtilObj(resXml);
					gson = new GsonBuilder().setDateFormat("yyyy-MM-dd").create();
					String gresult = gson.toJson(responseUtil);
					response.getWriter().write(gresult);
				} catch (Exception e) {
					logger.error(e.getMessage());
				}

			} else if ("saleOrderOutboundDetailQueryService".equals(action)) {
				List<ShipVO> shipVOList = null;
				try {
					String ship_seq_nos = request.getParameter("ship_seq_nos");

					shipService = new ShipService();
					ship_seq_nos = ship_seq_nos.replace(",", "','");

					ship_seq_nos = "'" + ship_seq_nos + "'";

					shipVOList = shipService.getShipByShipSeqNo(ship_seq_nos, "'" + groupId + "'");

					SfApi sfApi = new SfApi();
					logger.debug("ship_seq_nos =" + ship_seq_nos);

					ValueService valueService = util.getValueService(request, response);

					String reqXml = sfApi.genSaleOrderOutboundDetailQueryService(shipVOList, valueService);
					String resXml = sfApi.sendXML(reqXml);
					ResponseUtil responseUtil = sfApi.getResponseUtilObj(resXml);
					
					if (responseUtil.getResponse() != null) {
						if (responseUtil.getResponse().getHead().equals("OK")) {
							List<SaleOrder> saleOrder = responseUtil.getResponse().getBody().getSaleOrderOutboundDetailResponse().getSaleOrders().getSaleOrder();
							
							if (saleOrder != null) {
								for (SaleOrder tmp : saleOrder){
									if ( tmp.getResult().equals("1") ) {
										tmp.getHeader().setDataStatus(tmp.getHeader().getDataStatus());
									}
								}
							}
						}
					}
					
					gson = new GsonBuilder().setDateFormat("yyyy-MM-dd").create();
					String gresult = gson.toJson(responseUtil);
					response.getWriter().write(gresult);
				} catch (Exception e) {
					response.getWriter().write("失敗");
					e.printStackTrace();
					logger.error(e.getMessage());
				}
			} else if ("SFDelivery".equals(action)) {
				String jsonList = request.getParameter("jsonList");
				shipService = new ShipService();
				SfDeliveryApi api = new SfDeliveryApi();
				
				String reqXml = shipService.genSFDeliveryOrderService(jsonList, groupId);
				String resXml = api.sendXML(reqXml);
				tw.com.aber.sf.delivery.vo.Response responseObj = api.getResponseObj(resXml);
				result = api.isTelegraph(responseObj) ? "成功" : "失敗";
				logger.debug("執行結果: " + result);
				response.getWriter().write(result);
			}else if("SFDeliveryOrderConfirmCancel".equals(action)){
				String orderNo = request.getParameter("orderNo");
				SfDeliveryApi api = new SfDeliveryApi();
				String reqXml = api.genOrderConfirmService(orderNo);
				String resXml = api.sendXML(reqXml);
				tw.com.aber.sf.delivery.vo.Response responseObj = api.getResponseObj(resXml);
				result = api.isTelegraph(responseObj) ? "成功" : "失敗";
				logger.debug("執行結果: " + result);
				response.getWriter().write(result);
			}else if("SFDeliveryOrderSearchService".equals(action)){
				String orderNo = request.getParameter("orderNo");
				SfDeliveryApi api = new SfDeliveryApi();
				String reqXml = api.genOrderSearchService(orderNo);
				String resXml = api.sendXML(reqXml);
				tw.com.aber.sf.delivery.vo.Response responseObj = api.getResponseObj(resXml);
				result = api.isTelegraph(responseObj) ? "成功" : "失敗";
				logger.debug("執行結果: " + result);
				response.getWriter().write(result);
			}

		} catch (Exception e) {
			logger.error("Exception:".concat(e.getMessage()));
		}
	}

	public class ShipService {
		private ship_interface dao;

		public ShipService() {
			dao = new ShipDAO();
		}

		public List<ShipVO> getSearchShipBySaleDate(String groupId, Date startDate, Date endDate) {
			return dao.searchShipBySaleDate(groupId, startDate, endDate);
		}

		public List<ShipVO> getSearchShipByOrderNo(ShipVO shipVO) {
			return dao.searchShipByOrderNo(shipVO);
		}

		public List<ShipVO> getShipByShipSeqNo(String shipSeqNo, String groupId) {
			return dao.getShipByShipSeqNo(shipSeqNo, groupId);
		}

		public String genSFDeliveryOrderService(String info, String groupId) {
			return dao.genSFDeliveryOrderService(info, groupId);
		}
	}

	class ShipDAO implements ship_interface {
		private final String dbURL = getServletConfig().getServletContext().getInitParameter("dbURL")
				+ "?useUnicode=true&characterEncoding=utf-8&useSSL=false";
		private final String dbUserName = getServletConfig().getServletContext().getInitParameter("dbUserName");
		private final String dbPassword = getServletConfig().getServletContext().getInitParameter("dbPassword");
		private final String jdbcDriver = getServletConfig().getServletContext().getInitParameter("jdbcDriver");

		private static final String sp_select_ship_by_sale_date = "call sp_select_ship_by_sale_date (?,?,?)";
		private static final String sp_select_ship_by_order_no = "call sp_select_ship_by_order_no (?,?)";
		private static final String sp_get_ship_by_shipseqno = "call sp_get_ship_by_shipseqno(?,?)";
		private static final String sp_select_ship_delivery = "call sp_select_ship_delivery(?,?)";

		@Override
		public List<ShipVO> searchShipBySaleDate(String groupId, Date startDate, Date endDate) {
			List<ShipVO> rows = new ArrayList<ShipVO>();
			ShipVO row = null;

			Connection con = null;
			PreparedStatement pstmt = null;
			ResultSet rs = null;
			try {
				Class.forName(jdbcDriver);
				con = DriverManager.getConnection(dbURL, dbUserName, dbPassword);
				pstmt = con.prepareStatement(sp_select_ship_by_sale_date);

				pstmt.setString(1, groupId);
				pstmt.setDate(2, startDate);
				pstmt.setDate(3, endDate);

				rs = pstmt.executeQuery();
				while (rs.next()) {
					row = new ShipVO();
					row.setShip_id(rs.getString("ship_id"));
					row.setShip_seq_no(rs.getString("ship_seq_no"));
					row.setGroup_id(rs.getString("group_id"));
					row.setOrder_no(rs.getString("order_no"));
					row.setUser_id(rs.getString("user_id"));
					row.setCustomer_id(rs.getString("customer_id"));
					row.setMemo(rs.getString("memo"));
					row.setDeliveryway(rs.getString("deliveryway"));
					row.setTotal_amt(rs.getFloat("total_amt"));
					row.setDeliver_name(rs.getString("deliver_name"));
					row.setDeliver_to(rs.getString("deliver_to"));
					row.setRealsale_id(rs.getString("realsale_id"));
					row.setV_sale_date(rs.getDate("sale_date"));
					row.setV_c_product_id(rs.getString("c_product_id"));
					row.setV_product_name(rs.getString("product_name"));

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
		public List<ShipVO> getShipByShipSeqNo(String shipSeqNos, String groupId) {

			List<ShipVO> shipVOList = new ArrayList<ShipVO>();
			ShipVO shipVO = null;

			Connection con = null;
			PreparedStatement pstmt = null;
			ResultSet rs = null;

			List<ShipDetail> shipDetailList = null;
			ShipDetail shipDetail = null;

			String ship_id_Record = null;
			String ship_id_now = "";
			String order_no_Record = null;
			String order_no_now = "";

			try {
				Class.forName(jdbcDriver);
				con = DriverManager.getConnection(dbURL, dbUserName, dbPassword);
				pstmt = con.prepareStatement(sp_get_ship_by_shipseqno);

				logger.debug("getShipByShipSeqNo groupId:" + groupId + "shipSeqNos:" + shipSeqNos);
				pstmt.setString(1, groupId);
				pstmt.setString(2, shipSeqNos);

				rs = pstmt.executeQuery();
				while (rs.next()) {

					// sp 為ship sd 為shipDetail
					shipDetail = new ShipDetail();
					shipDetail.setC_product_id(rs.getString("sd_c_product_id"));
					shipDetail.setDeliveryway(rs.getString("sd_deliveryway"));
					shipDetail.setGroup_id(rs.getString("sd_group_id"));
					shipDetail.setMemo(rs.getString("sd_memo"));
					shipDetail.setPrice(rs.getString("sd_price"));
					shipDetail.setProduct_id(rs.getString("sd_product_id"));
					shipDetail.setProduct_name(rs.getString("sd_product_name"));

					String sd_quantity = rs.getString("sd_quantity");

					if (!(sd_quantity == null || "".equals(sd_quantity))) {
						shipDetail.setQuantity(Integer.parseInt(sd_quantity));
					}
					shipDetail.setShip_id(rs.getString("sd_ship_id"));
					shipDetail.setShipDetail_id(rs.getString("sd_shipDetail_id"));
					shipDetail.setUser_id(rs.getString("sd_user_id"));

					// 如果現在跑的ship_id跟紀錄的ship_id不相等 那代表已經換出貨單
					// 所以要新增出貨明細
					logger.debug("order_no_now:" + order_no_now);
					logger.debug("order_no_Record:" + order_no_Record);
					logger.debug("order_no_Record:" + shipDetail.getC_product_id());
					logger.debug("order_no_Record:" + shipDetail.getProduct_name());

					ship_id_now = rs.getString("sp_ship_id");
					order_no_now = rs.getString("sp_order_no");
					if ((!order_no_now.equals(order_no_Record)) || rs.isFirst()) {
						shipDetailList = new ArrayList<ShipDetail>();

						// 並且紀錄出貨明細
						shipVO = new ShipVO();
						shipVO.setShip_id(rs.getString("sp_ship_id"));

						shipVO.setShip_seq_no(rs.getString("sp_ship_seq_no"));
						shipVO.setGroup_id(rs.getString("sp_group_id"));
						shipVO.setOrder_no(rs.getString("sp_order_no"));
						shipVO.setUser_id(rs.getString("sp_user_id"));
						shipVO.setCustomer_id(rs.getString("sp_customer_id"));
						shipVO.setMemo(rs.getString("sp_memo"));
						shipVO.setDeliveryway(rs.getString("sp_deliveryway"));
						shipVO.setTotal_amt(rs.getFloat("sp_total_amt"));
						shipVO.setDeliver_name(rs.getString("sp_deliver_name"));
						shipVO.setDeliver_to(rs.getString("sp_deliver_to"));
						shipVO.setV_deliver_mobile(rs.getString("se_deliver_mobile"));
						shipVO.setV_deliver_name(rs.getString("se_deliver_name"));
						shipVO.setV_deliver_phone(rs.getString("se_deliver_phone"));
						shipVO.setV_dis_date(rs.getDate("sm_dis_date"));
						shipVO.setShipDetail(shipDetailList);
						shipVOList.add(shipVO);

						ship_id_Record = ship_id_now;
						order_no_Record = order_no_now;
					}

					shipDetailList.add(shipDetail);
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
			return shipVOList;
		}

		@Override
		public List<ShipVO> searchShipByOrderNo(ShipVO shipVO) {
			List<ShipVO> rows = new ArrayList<ShipVO>();
			ShipVO row = null;

			Connection con = null;
			PreparedStatement pstmt = null;
			ResultSet rs = null;
			try {
				Class.forName(jdbcDriver);
				con = DriverManager.getConnection(dbURL, dbUserName, dbPassword);
				pstmt = con.prepareStatement(sp_select_ship_by_order_no);

				String groupId = shipVO.getGroup_id();
				String orderNo = shipVO.getOrder_no();

				pstmt.setString(1, groupId);
				pstmt.setString(2, orderNo);

				rs = pstmt.executeQuery();
				while (rs.next()) {
					row = new ShipVO();
					row.setShip_id(rs.getString("ship_id"));
					row.setShip_seq_no(rs.getString("ship_seq_no"));
					row.setGroup_id(rs.getString("group_id"));
					row.setOrder_no(rs.getString("order_no"));
					row.setUser_id(rs.getString("user_id"));
					row.setCustomer_id(rs.getString("customer_id"));
					row.setMemo(rs.getString("memo"));
					row.setDeliveryway(rs.getString("deliveryway"));
					row.setTotal_amt(rs.getFloat("total_amt"));
					row.setDeliver_name(rs.getString("deliver_name"));
					row.setDeliver_to(rs.getString("deliver_to"));
					row.setRealsale_id(rs.getString("realsale_id"));
					row.setV_sale_date(rs.getDate("sale_date"));
					row.setV_c_product_id(rs.getString("c_product_id"));
					row.setV_product_name(rs.getString("product_name"));

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
		public String genSFDeliveryOrderService(String info, String groupId) {

			Type type = new TypeToken<List<ShipSFDeliveryVO>>() {
			}.getType();
			List<ShipSFDeliveryVO> sfDeliveryVOs = new Gson().fromJson(info, type);

			List<RealSaleDetailVO> details = null;
			List<Cargo> cargos = new ArrayList<Cargo>();

			Order order = null;

			for (ShipSFDeliveryVO master : sfDeliveryVOs) {
				details = master.getRealSaleDetailVOs();

				for (RealSaleDetailVO detail : details) {
					Cargo cargo = new Cargo();
					cargo.setName(detail.getProduct_name());
					cargo.setCount(String.valueOf(detail.getQuantity()));
					cargos.add(cargo);
				}
			}
			// 因為只有同筆訂單的一或多筆出貨單，才能使用該功能，所以order資料統一取第一筆(至少一筆)
			ShipSFDeliveryVO vo = sfDeliveryVOs.get(0);
			String orderNo = vo.getOrder_no();
			Connection con = null;
			PreparedStatement pstmt = null;
			ResultSet rs = null;

			String result = "";
			Body body = new Body();
			Request request = new Request();

			try {
				Class.forName(jdbcDriver);
				con = DriverManager.getConnection(dbURL, dbUserName, dbPassword);
				pstmt = con.prepareStatement(sp_select_ship_delivery);

				pstmt.setString(1, groupId);
				pstmt.setString(2, orderNo);

				rs = pstmt.executeQuery();
				while (rs.next()) {
					order = new Order();
					order.setOrderId(orderNo);
					order.setJ_company(rs.getString("j_company"));
					order.setJ_contact(rs.getString("j_contact"));
					order.setJ_tel(rs.getString("j_tel"));
					order.setJ_mobile(rs.getString("j_mobile"));
					order.setJ_address(rs.getString("j_address"));
					// TODO 到件方公司名稱先填寫為[個人]
					order.setD_company("個人");
					order.setD_contact(rs.getString("d_contact"));
					order.setD_tel(rs.getString("d_tel"));
					order.setD_mobile(rs.getString("d_mobile"));
					order.setD_address(rs.getString("d_address"));
					// TODO 付款方式先填寫為[1]
					order.setPay_method("1");
					// TODO 快件產品類別先填寫為[1]
					order.setExpress_type("1");
					// TODO 包裹數先填寫為[1]
					order.setParcel_quantity("1");
					order.setCargos(cargos);
					request.setHead(rs.getString("head"));
				}
				body.setOrder(order);

				request.setService("OrderService");
				request.setLang("zh-CN");
				request.setBody(body);

				String rqJsonStr = new Gson().toJson(request);
				logger.debug("\n\n[Request]\n\nJson格式:\n\n{}\n", rqJsonStr);
				
				StringWriter sw = new StringWriter();
				JAXB.marshal(request, sw);
				logger.debug("\n\nXML格式:\n\n{}\n", sw.toString());
				result = sw.toString();
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

			return result;
		}
	}

}

interface ship_interface {
	public List<ShipVO> searchShipBySaleDate(String groupId, Date startDate, Date endDate);

	public List<ShipVO> searchShipByOrderNo(ShipVO shipVO);

	public List<ShipVO> getShipByShipSeqNo(String shipSeqNos, String groupID);

	public String genSFDeliveryOrderService(String info, String groupId);

}