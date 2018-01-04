package tw.com.aber.ship.controller;

import java.io.IOException;
import java.lang.reflect.Type;
import java.sql.Date;
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
import com.google.gson.reflect.TypeToken;

import tw.com.aber.service.ShipService;
import tw.com.aber.sf.vo.ResponseUtil;
import tw.com.aber.sf.vo.SaleOrder;
import tw.com.aber.sftransfer.controller.SfApi;
import tw.com.aber.sftransfer.controller.SfDeliveryApi;
import tw.com.aber.sftransfer.controller.ValueService;
import tw.com.aber.util.Util;
import tw.com.aber.vo.DeliveryVO;
import tw.com.aber.vo.ShipSFDeliveryVO;
import tw.com.aber.vo.ShipSFDetailVO;
import tw.com.aber.vo.ShipSFStatusVO;
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
			}  else if ("searchByWaybill_no".equals(action)) {
				String waybill_no = request.getParameter("waybill_no");
				
				logger.debug("waybill_no:" + waybill_no);
				logger.debug("groupId:" + groupId);

				gson = new GsonBuilder().setDateFormat("yyyy-MM-dd").create();
				service = new ShipService();
			
				rows = service.getSearchShipByWaybillNo(groupId, waybill_no);
				result = gson.toJson(rows);

				response.getWriter().write(result);
			} else if ("searchEgsBetween".equals(action)) {
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

				rows = service.getEgsShipGroupByOrderNo(groupId, startDate, endDate);
				result = gson.toJson(rows);
				response.getWriter().write(result);
			}else if ("sendToTelegraph".equals(action)) {

				List<ShipVO> shipVOList = null;

				try {
					String ship_seq_nos = request.getParameter("ship_seq_nos");

					logger.debug("ship_seq_nos:" + ship_seq_nos);

					shipService = new ShipService();
					ship_seq_nos = ship_seq_nos.replace(",", "','");

					ship_seq_nos = "'" + ship_seq_nos + "'";

					shipVOList = shipService.getShipByShipSeqNoGroupByOrderNo(ship_seq_nos, "'" + groupId + "'");

					SfApi sfApi = new SfApi();

					ValueService valueService = util.getValueService(request, response);
					String env = valueService.getGroupSfVO().getEnv();
					String reqXml = sfApi.genSaleOrderService(shipVOList, valueService);
					String resXml = sfApi.sendXML(env, reqXml);
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
					String env = valueService.getGroupSfVO().getEnv();
					
					String reqXml = sfApi.genCancelSaleOrderService(shipVOList, valueService);
					String resXml = sfApi.sendXML(env, reqXml);
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
					String env = valueService.getGroupSfVO().getEnv();
					
					String reqXml = sfApi.genSaleOrderOutboundDetailQueryService(shipVOList, valueService);
					String resXml = sfApi.sendXML(env, reqXml);
					ResponseUtil responseUtil = sfApi.getResponseUtilObj(resXml);

					if (responseUtil.getResponse() != null) {
						if (responseUtil.getResponse().getHead().equals("OK")) {
							List<SaleOrder> saleOrder = responseUtil.getResponse().getBody()
									.getSaleOrderOutboundDetailResponse().getSaleOrders().getSaleOrder();

							if (saleOrder != null) {
								for (SaleOrder tmp : saleOrder) {
									if (tmp.getResult().equals("1")) {
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
			} else if ("searchBySaleDateGroup".equals(action)) {
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

				rows = service.getShipGroupByOrderNo(groupId, startDate, endDate);
				result = gson.toJson(rows);
				response.getWriter().write(result);
			}else if ("sendToTelegraphGroup".equals(action)) {
				List<ShipVO> shipVOList = null;
				try {
					String order_nos = request.getParameter("order_nos");

					logger.debug("order_nos:" + order_nos);

					order_nos = order_nos.replace(",", "','");

					order_nos = "'" + order_nos + "'";
					shipService = new ShipService();
					shipVOList = shipService.getShipByShipSeqNoGroupByOrderNoNew(order_nos, "'" + groupId + "'");

					ValueService valueService = util.getValueService(request, response);
					String env = valueService.getGroupSfVO().getEnv();
					SfApi sfApi = new SfApi();
					String reqXml = sfApi.genSaleOrderService(shipVOList, valueService);
					String resXml = sfApi.sendXML(env, reqXml);
					ResponseUtil responseUtil = sfApi.getResponseUtilObj(resXml);
					gson = new GsonBuilder().setDateFormat("yyyy-MM-dd").create();
					String gresult = gson.toJson(responseUtil);
					response.getWriter().write(gresult);
				} catch (Exception e) {
					e.printStackTrace();
					logger.debug(e.getMessage());
				}
			} else if ("sendToCancelSaleOrderServiceGroup".equals(action)) {
				List<ShipVO> shipVOList = null;
				try {
					/***************************
					 * 1.接收請求參數
					 ***************************************/
					String orderNos = request.getParameter("orderNos");
					logger.debug("orderNos =" + orderNos);
					String[] orderList = orderNos.split(",");
					
					shipVOList = new ArrayList<ShipVO>();
					for(String order : orderList){
						shipVO = new ShipVO();
						shipVO.setOrder_no(order);
						shipVOList.add(shipVO);
					}
					
					ValueService valueService = util.getValueService(request, response);
					String env = valueService.getGroupSfVO().getEnv();
					SfApi sfApi = new SfApi();
					String reqXml = sfApi.genCancelSaleOrderService(shipVOList, valueService);
					String resXml = sfApi.sendXML(env, reqXml);
					ResponseUtil responseUtil = sfApi.getResponseUtilObj(resXml);
					gson = new GsonBuilder().setDateFormat("yyyy-MM-dd").create();
					String gresult = gson.toJson(responseUtil);
					response.getWriter().write(gresult);
				} catch (Exception e) {
					logger.error(e.getMessage());
				}
			} else if ("saleOrderOutboundDetailQueryServiceGroup".equals(action)) {
				List<ShipVO> shipVOList = null;
				try {
					String orderNos = request.getParameter("orderNos");
					logger.debug("orderNos =" + orderNos);
					String[] orderList = orderNos.split(",");
					
					shipVOList = new ArrayList<ShipVO>();
					for(String order : orderList){
						shipVO = new ShipVO();
						shipVO.setOrder_no(order);
						shipVOList.add(shipVO);
					}
					
					ValueService valueService = util.getValueService(request, response);
					String env = valueService.getGroupSfVO().getEnv();
					
					SfApi sfApi = new SfApi();
					String reqXml = sfApi.genSaleOrderOutboundDetailQueryService(shipVOList, valueService);
					String resXml = sfApi.sendXML(env, reqXml);
					ResponseUtil responseUtil = sfApi.getResponseUtilObj(resXml);

					if (responseUtil.getResponse() != null) {
						if (responseUtil.getResponse().getHead().equals("OK")) {
							List<SaleOrder> saleOrder = responseUtil.getResponse().getBody()
									.getSaleOrderOutboundDetailResponse().getSaleOrders().getSaleOrder();

							if (saleOrder != null) {
								for (SaleOrder tmp : saleOrder) {
									if (tmp.getResult().equals("1")) {
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

				DeliveryVO deliveryVO = new DeliveryVO();
				shipService = new ShipService();
				SfDeliveryApi api = new SfDeliveryApi();

				String jsonList = request.getParameter("jsonList");
				String totalWeight = request.getParameter("weight");
				ValueService valueService = util.getValueService(request, response);

				// 得到序號
				String seqNo = shipService.getShipSFDeliveryNewSeqNo(groupId);

				Type type = new TypeToken<List<ShipSFDeliveryVO>>() {
				}.getType();

				List<ShipSFDeliveryVO> sfDeliveryVOs = new Gson().fromJson(jsonList, type);

				// 得到前端訂單號
				String orderNo = sfDeliveryVOs.get(0).getOrder_no();

				String reqXml = shipService.genSFDeliveryOrderService(jsonList, groupId, totalWeight, seqNo);

				try {
					String resXml = api.sendXML(reqXml, valueService);

					tw.com.aber.sf.delivery.vo.Response responseObj = api.getResponseObj(resXml);

					if (responseObj.getError() != null) {
						String err_code = responseObj.getError().getCode();
						String remark = responseObj.getError().getValue();
						deliveryVO.setGroup_id(groupId);
						deliveryVO.setSeq_no(seqNo);
						deliveryVO.setErr_code(err_code);
						deliveryVO.setRemark(remark);

						shipService.insertToShipSFDelivery(deliveryVO);

					} else {
						String mailNo = responseObj.getBody().getOrderResponse().getMailno();
						String origincode = responseObj.getBody().getOrderResponse().getOrigincode();
						String destcode = responseObj.getBody().getOrderResponse().getDestcode();
						String sf_result = responseObj.getBody().getOrderResponse().getFilter_result();

						logger.debug("mailNo: ".concat(mailNo));
						logger.debug("origincode: ".concat(origincode));
						logger.debug("destcode: ".concat(destcode));
						logger.debug("sf_result: ".concat(sf_result));

						deliveryVO.setSeq_no(seqNo);
						deliveryVO.setOrder_no(orderNo);
						deliveryVO.setGroup_id(groupId);
						deliveryVO.setMailno(mailNo);

						deliveryVO.setWeight(totalWeight);
						deliveryVO.setOrigincode(origincode);
						deliveryVO.setDestcode(destcode);
						deliveryVO.setSf_result(sf_result);

						shipService.insertToShipSFDelivery(deliveryVO);
					}

					gson = new Gson();
					result = gson.toJson(responseObj);
					response.getWriter().write(result);
				} catch (Exception e) {
					logger.debug(e.getMessage());
					String remark = "電文傳送失敗";
					deliveryVO.setSeq_no(seqNo);
					deliveryVO.setRemark(remark);
					shipService.insertToShipSFDelivery(deliveryVO);
				}
			} else if ("SFDeliveryOrderConfirmCancel".equals(action)) {
				String orderNo = request.getParameter("orderNo");
				SfDeliveryApi api = new SfDeliveryApi();

				ValueService valueService = util.getValueService(request, response);

				String reqXml = api.genOrderConfirmService(orderNo, valueService);
				String resXml = api.sendXML(reqXml, valueService);
				tw.com.aber.sf.delivery.vo.Response responseObj = api.getResponseObj(resXml);
				gson = new Gson();
				result = gson.toJson(responseObj);
				response.getWriter().write(result);
			} else if ("SFDeliveryOrderSearchService".equals(action)) {
				String orderNo = request.getParameter("orderNo");
				SfDeliveryApi api = new SfDeliveryApi();

				ValueService valueService = util.getValueService(request, response);

				String reqXml = api.genOrderSearchService(orderNo, valueService);
				String resXml = api.sendXML(reqXml, valueService);
				tw.com.aber.sf.delivery.vo.Response responseObj = api.getResponseObj(resXml);
				gson = new Gson();
				result = gson.toJson(responseObj);
				response.getWriter().write(result);
			} else if ("SFDeliveryRouteService".equals(action)) {
				String orderNos = request.getParameter("orderNos");

				// 前端參數控制後端傳遞 1:託運單號 2:訂單號
				String type = request.getParameter("type");

				SfDeliveryApi api = new SfDeliveryApi();
				shipService = new ShipService();
				String[] orderNoArr = orderNos.split(",");
				String formatNos = "";

				for (String s : orderNoArr) {
					formatNos += "\"" + s + "\",";
				}
				formatNos = formatNos.substring(1, formatNos.length() - 2);

				List<DeliveryVO> list = shipService.getShipSFDeliveryInfoByOrderNo(groupId, formatNos);

				orderNos = "";

				if ("1".equals(type)) {
					for (DeliveryVO vo : list) {
						orderNos = orderNos + vo.getMailno() + ",";
					}
				}

				if ("2".equals(type)) {
					for (DeliveryVO vo : list) {
						orderNos = orderNos + vo.getOrder_no() + ",";
					}
				}
				orderNos = orderNos.substring(0, orderNos.length() - 1);

				ValueService valueService = util.getValueService(request, response);

				String reqXml = api.genRouteService(orderNos, type, valueService);
				String resXml = api.sendXML(reqXml, valueService);

				tw.com.aber.sf.delivery.vo.Response responseObj = api.getResponseObj(resXml);
				gson = new Gson();
				result = gson.toJson(responseObj);
				response.getWriter().write(result);
			} else if ("checkReport".equals(action)) {
				/*
				 * p_group_id p_order_no p_suda7 p_egs_num
				 */
			} else if ("selectShipSfStatus".equals(action)) {

				try {
					String ship_id = request.getParameter("ship_id");
					String order_no = request.getParameter("order_no");

					ShipSFStatusVO shipSFStatusVO = new ShipSFStatusVO();
					shipSFStatusVO.setGroup_id(groupId);
					shipSFStatusVO.setV_ship_id(ship_id);
					shipSFStatusVO.setOrder_no(order_no);

					shipService = new ShipService();
					List<ShipSFStatusVO> sfStatusVOs = shipService.selectShipSfStatus(shipSFStatusVO);
					result = new Gson().toJson(sfStatusVOs);
				} catch (Exception e) {
					logger.error(e.getMessage());
					result = "{}";
				}
				response.getWriter().write(result);
			} else if ("selectShipSfDetailStatus".equals(action)) {

				try {
					String ship_id = request.getParameter("ship_id");
					String order_no = request.getParameter("order_no");

					ShipSFDetailVO shipSFDetailVO = new ShipSFDetailVO();
					shipSFDetailVO.setGroup_id(groupId);
					shipSFDetailVO.setV_ship_id(ship_id);
					shipSFDetailVO.setOrder_no(order_no);

					shipService = new ShipService();
					List<ShipSFDetailVO> sfDetailVOs = shipService.selectShipSfDetailStatus(shipSFDetailVO);
					result = new Gson().toJson(sfDetailVOs);
				} catch (Exception e) {
					logger.error(e.getMessage());
					result = "{}";
				}
				logger.debug("result: {}", result);
				response.getWriter().write(result);
			}

		} catch (Exception e) {
			logger.error("Exception:".concat(e.getMessage()));
		}
	}

}

