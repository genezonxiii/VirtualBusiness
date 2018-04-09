package tw.com.aber.sale.controller;

import java.io.IOException;
import java.sql.Date;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashSet;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import tw.com.aber.inv.controller.InvoiceApi;
import tw.com.aber.inv.vo.Index;
import tw.com.aber.service.SaleService;
import tw.com.aber.util.Util;
import tw.com.aber.vo.GroupVO;
import tw.com.aber.vo.InvoiceTrackVO;
import tw.com.aber.vo.ProductVO;
import tw.com.aber.vo.ResponseVO;
import tw.com.aber.vo.SaleDetailVO;
import tw.com.aber.vo.SaleExtVO;
import tw.com.aber.vo.SaleVO;

public class sale extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private static final Logger logger = LogManager.getLogger(sale.class);

	private Util util = new Util();

	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		request.setCharacterEncoding("UTF-8");
		response.setCharacterEncoding("UTF-8");

		String group_id = request.getSession().getAttribute("group_id").toString();
		String user_id = request.getSession().getAttribute("user_id").toString();

		SaleService saleService = new SaleService();
		List<SaleVO> saleList = null;
		Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd").create();

		String action = request.getParameter("action");
		logger.debug("Action:".concat(action));

		try {
			if ("search".equals(action)) {
				String c_product_id = request.getParameter("c_product_id");

				logger.debug("c_product_id:".concat(c_product_id));

				if (c_product_id == null || (c_product_id.trim()).length() == 0) {
					saleList = saleService.getSearchAllDB(group_id);
				} else {
					saleList = saleService.getSearchDB(group_id, c_product_id);
				}

				String jsonStrList = gson.toJson(saleList);
				response.getWriter().write(jsonStrList);
			} else if ("search_trans_list_date".equals(action)) {
				String type = request.getParameter("type");
				String start_date = request.getParameter("trans_list_start_date");
				String end_date = request.getParameter("trans_list_end_date");
				String upload_start_date = request.getParameter("upload_start_date");
				String upload_end_date = request.getParameter("upload_end_date");

				logger.debug("type:" + type);
				logger.debug("trans_list_date start_date:" + start_date);
				logger.debug("end_date:" + end_date);
				logger.debug("upload_date start_date:" + upload_start_date);
				logger.debug("end_date:" + upload_end_date);

				if (start_date.trim().length() == 0 && end_date.trim().length() == 0
						&& upload_start_date.trim().length() == 0 && upload_end_date.trim().length() == 0) {
					saleList = saleService.getSearchAllDB(group_id);
				} else if ((type.equals("search-upload-date") || type.equals("invoice")) 
						&& upload_start_date.trim().length() > 0 && upload_end_date.trim().length() > 0) {
					saleList = saleService.searchUploadDateDB(group_id, upload_start_date, upload_end_date);
				} else if ((type.equals("searh-trans-list-date") || type.equals("invoice")) 
						&& start_date.trim().length() > 0 && end_date.trim().length() > 0) {
					saleList = saleService.getSearchTransListDateDB(group_id, start_date, end_date);
				}

				String jsonStrList = gson.toJson(saleList);
				response.getWriter().write(jsonStrList);
			} else if ("search_product_data".equals(action)) {
				String term = request.getParameter("term");
				String identity = request.getParameter("identity");

				logger.debug("term:".concat(term));
				logger.debug("identity:".concat(identity));

				List<ProductVO> productList = null;

				if ("ID".equals(identity)) {
					productList = saleService.getSearchProductById(group_id, term);
				} else if ("NAME".equals(identity)) {
					productList = saleService.getSearchProductByName(group_id, term);
				}

				String jsonStrList = gson.toJson(productList);
				response.getWriter().write(jsonStrList);
			} else if ("getSaleDetail".equals(action)) {
				String sale_id = request.getParameter("sale_id");

				logger.debug("sale_id:".concat(sale_id));

				SaleDetailVO saleDetailVO = new SaleDetailVO();
				saleDetailVO.setSale_id(sale_id);

				List<SaleDetailVO> saleDetailList = saleService.getSaleDetail(saleDetailVO);

				String jsonStr = gson.toJson(saleDetailList);
				response.getWriter().write(jsonStr);

				logger.debug("result".concat(jsonStr));
			} else if ("insert".equals(action)) {
				String invoice = request.getParameter("invoice");
				String order_no = request.getParameter("order_no");
				String cus_id = request.getParameter("cus_id");
				String name = request.getParameter("name");
				String product_id = request.getParameter("product_id");
				String product_name = request.getParameter("product_name");
				String c_product_id = request.getParameter("c_product_id");
				Integer quantity = Integer.valueOf(request.getParameter("quantity"));
				String contrast_type = request.getParameter("contrast_type");
				String deliveryway = request.getParameter("deliveryway");
				Float total_amt = Float.valueOf(request.getParameter("total_amt"));
				
				Float price = Float.valueOf(request.getParameter("price"));

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

				String memo = request.getParameter("memo");
				String order_source = request.getParameter("order_source");

				String seq_no;

				List<SaleVO> saleSeqNoList = saleService.getSaleSeqNo(group_id);
				if (saleSeqNoList.size() == 0) {
					seq_no = getThisYearMonthDate() + "0001";
				} else {
					seq_no = getGenerateSeqNo(saleSeqNoList.get(0).getSeq_no());
				}

				if (order_no.length() < 1) {
					order_no = seq_no;
				}

				logger.debug("seq_no:".concat(seq_no));
				
				String deliverName = request.getParameter("deliver_name");
				String deliverTo = request.getParameter("deliver_to");
				String deliverStore = request.getParameter("deliver_store");
				String deliverNote = request.getParameter("deliver_note");
				String deliverPhone = request.getParameter("deliver_phone");
				String deliverMobile = request.getParameter("deliver_mobile");
				String payKind = request.getParameter("pay_kind");
				String payStatus = request.getParameter("pay_status");
				String invName = request.getParameter("inv_name");
				String invTo = request.getParameter("inv_to");
				String email = request.getParameter("email");
				String creditCard = request.getParameter("credit_card");

				SaleVO saleVO = new SaleVO();

				saleVO.setSeq_no(seq_no);
				saleVO.setGroup_id(group_id);
				saleVO.setOrder_no(order_no);
				saleVO.setUser_id(user_id);
				saleVO.setProduct_id(product_id);
				saleVO.setProduct_name(product_name);
				saleVO.setC_product_id(c_product_id);
				saleVO.setCustomer_id(cus_id);
				saleVO.setName(name);
				saleVO.setQuantity(quantity);
				saleVO.setPrice(price);
				saleVO.setInvoice(invoice);
				saleVO.setInvoice_date(invoice_date);
				saleVO.setTrans_list_date(trans_list_date);
				saleVO.setDis_date(dis_date);
				saleVO.setMemo(memo);
				saleVO.setSale_date(sale_date);
				saleVO.setOrder_source(order_source);
				saleVO.setContrast_type(contrast_type);
				saleVO.setDeliveryway(deliveryway);
				
				SaleExtVO saleExtVO = new SaleExtVO();
				
				saleExtVO.setTotalAmt(total_amt);
				saleExtVO.setDeliverName(deliverName);
				saleExtVO.setDeliverTo(deliverTo);
				saleExtVO.setDeliverStore(deliverStore);
				saleExtVO.setDeliverNote(deliverNote);
				saleExtVO.setDeliverPhone(deliverPhone);
				saleExtVO.setDeliverMobile(deliverMobile);
				saleExtVO.setPayKind(payKind);
				saleExtVO.setPayStatus(payStatus);
				saleExtVO.setInvName(invName);
				saleExtVO.setInvTo(invTo);
				saleExtVO.setEmail(email);
				saleExtVO.setCreditCard(creditCard);
				saleVO.setSaleExtVO(saleExtVO);

				logger.debug("order_no:".concat(order_no));
				logger.debug("product_id:".concat(product_id));
				logger.debug("product_name:".concat(product_name));
				logger.debug("c_product_id:".concat(c_product_id));
				logger.debug("customer_id:".concat(cus_id));
				logger.debug("customer_name:".concat(name));
				logger.debug("invoice:".concat(invoice));
				logger.debug("quantity:".concat(quantity.toString()));
				logger.debug("price:".concat(price.toString()));
				logger.debug("invoice_date:".concat(invoice_date == null ? "" : invoice_date.toString()));
				logger.debug("trans_list_date:".concat(trans_list_date == null ? "" : trans_list_date.toString()));
				logger.debug("sale_date:".concat(sale_date == null ? "" : sale_date.toString()));
				logger.debug("memo:".concat(memo));
				logger.debug("order_source:".concat(order_source));
				logger.debug("contrast_type:".concat(contrast_type));
				logger.debug("deliveryway:".concat(deliveryway));
				logger.debug("SaleExtVO:".concat(saleExtVO.toString()));

				saleService.addSale(saleVO);

				saleList = saleService.getSearchAllDB(group_id);

				String jsonStrList = gson.toJson(saleList);
				response.getWriter().write(jsonStrList);
			} else if ("update".equals(action)) {
				String sale_id = request.getParameter("sale_id");
				String seq_no = request.getParameter("seq_no");
				String invoice = request.getParameter("invoice");
				String order_no = request.getParameter("order_no");
				String name = request.getParameter("name");
				String cus_id = request.getParameter("cus_id");
				String product_id = request.getParameter("product_id");
				String product_name = request.getParameter("product_name");
				String c_product_id = request.getParameter("c_product_id");
				String contrast_type = request.getParameter("contrast_type");
				String deliveryway = request.getParameter("deliveryway");
				Float total_amt = Float.valueOf(request.getParameter("total_amt"));

				Integer quantity = Integer.valueOf(request.getParameter("quantity"));

				Float price = Float.valueOf(request.getParameter("price"));

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
				String memo = request.getParameter("memo");
				String order_source = request.getParameter("order_source");

				String deliverName = request.getParameter("deliver_name");
				String deliverTo = request.getParameter("deliver_to");
				String deliverStore = request.getParameter("deliver_store");
				String deliverNote = request.getParameter("deliver_note");
				String deliverPhone = request.getParameter("deliver_phone");
				String deliverMobile = request.getParameter("deliver_mobile");
				String payKind = request.getParameter("pay_kind");
				String payStatus = request.getParameter("pay_status");
				String invName = request.getParameter("inv_name");
				String invTo = request.getParameter("inv_to");
				String email = request.getParameter("email");
				String creditCard = request.getParameter("credit_card");
				
				SaleVO saleVO = new SaleVO();

				saleVO.setSale_id(sale_id);
				saleVO.setSeq_no(seq_no);
				saleVO.setGroup_id(group_id);
				saleVO.setOrder_no(order_no);
				saleVO.setUser_id(user_id);
				saleVO.setProduct_id(product_id);
				saleVO.setProduct_name(product_name);
				saleVO.setC_product_id(c_product_id);
				saleVO.setCustomer_id(cus_id);
				saleVO.setName(name);
				saleVO.setQuantity(quantity);
				saleVO.setPrice(price);
				saleVO.setInvoice(invoice);
				saleVO.setInvoice_date(invoice_date);
				saleVO.setTrans_list_date(trans_list_date);
				saleVO.setDis_date(dis_date);
				saleVO.setMemo(memo);
				saleVO.setSale_date(sale_date);
				saleVO.setOrder_source(order_source);
				saleVO.setContrast_type(contrast_type);
				saleVO.setDeliveryway(deliveryway);
				saleVO.setTotal_amt(total_amt);

				SaleExtVO saleExtVO = new SaleExtVO();
				
				saleExtVO.setTotalAmt(total_amt);
				saleExtVO.setDeliverName(deliverName);
				saleExtVO.setDeliverTo(deliverTo);
				saleExtVO.setDeliverStore(deliverStore);
				saleExtVO.setDeliverNote(deliverNote);
				saleExtVO.setDeliverPhone(deliverPhone);
				saleExtVO.setDeliverMobile(deliverMobile);
				saleExtVO.setPayKind(payKind);
				saleExtVO.setPayStatus(payStatus);
				saleExtVO.setInvName(invName);
				saleExtVO.setInvTo(invTo);
				saleExtVO.setEmail(email);
				saleExtVO.setCreditCard(creditCard);
				saleVO.setSaleExtVO(saleExtVO);
				
				logger.debug("sale_id:".concat(sale_id));
				logger.debug("order_no:".concat(order_no));
				logger.debug("product_id:".concat(product_id));
				logger.debug("product_name:".concat(product_name));
				logger.debug("c_product_id:".concat(c_product_id));
				logger.debug("customer_id:".concat(cus_id));
				logger.debug("customer_name:".concat(name));
				logger.debug("invoice:".concat(invoice));
				logger.debug("quantity:".concat(quantity.toString()));
				logger.debug("price:".concat(price.toString()));
				logger.debug("invoice_date:".concat(invoice_date == null ? "" : invoice_date.toString()));
				logger.debug("trans_list_date:".concat(trans_list_date == null ? "" : trans_list_date.toString()));
				logger.debug("sale_date:".concat(sale_date == null ? "" : sale_date.toString()));
				logger.debug("memo:".concat(memo));
				logger.debug("order_source:".concat(order_source));
				logger.debug("contrast_type:".concat(contrast_type));
				logger.debug("deliveryway:".concat(deliveryway));
				logger.debug("total_amt:".concat(total_amt.toString()));
				logger.debug("SaleExtVO:".concat(saleExtVO.toString()));
				
				saleService.updateSale(saleVO);

				saleList = saleService.getSearchAllDB(group_id);

				String jsonStrList = gson.toJson(saleList);
				response.getWriter().write(jsonStrList);
			} else if ("delete".equals(action)) {
				String sale_id = request.getParameter("sale_id");
				String c_product_id = request.getParameter("c_product_id");

				logger.debug("sale_id:".concat(sale_id));
				logger.debug("c_product_id:".concat(c_product_id));

				saleService.deleteSale(sale_id, user_id);

				saleList = saleService.getSearchAllDB(group_id);

				String jsonStrList = gson.toJson(saleList);
				response.getWriter().write(jsonStrList);
			} else if ("insertDetail".equals(action) || "updateDetail".equals(action)) {

				String sale_id = request.getParameter("sale_id");
				String seq_no = request.getParameter("seq_no");
				String order_no = request.getParameter("order_no");
				String product_id = request.getParameter("product_id");
				String product_name = request.getParameter("product_name");
				String c_product_id = request.getParameter("c_product_id");
				Integer quantity = Integer.valueOf(request.getParameter("quantity"));
				Float price = Float.valueOf(request.getParameter("price"));

				SaleDetailVO paramVO = new SaleDetailVO();

				paramVO.setSale_id(sale_id);
				paramVO.setSeq_no(seq_no);
				paramVO.setGroup_id(group_id);
				paramVO.setOrder_no(order_no);
				paramVO.setUser_id(user_id);
				paramVO.setProduct_id(product_id);
				paramVO.setProduct_name(product_name);
				paramVO.setC_product_id(c_product_id);
				paramVO.setQuantity(quantity);
				paramVO.setPrice(price);

				logger.debug("sale_id:".concat(util.null2str(sale_id)));
				logger.debug("seq_no:".concat(util.null2str(seq_no)));
				logger.debug("group_id:".concat(util.null2str(group_id)));
				logger.debug("order_no:".concat(util.null2str(order_no)));
				logger.debug("user_id:".concat(util.null2str(user_id)));
				logger.debug("product_id:".concat(util.null2str(product_id)));
				logger.debug("product_name:".concat(util.null2str(product_name)));
				logger.debug("c_product_id:".concat(util.null2str(c_product_id)));
				logger.debug("quantity:".concat(util.null2str(quantity.toString())));
				logger.debug("price:".concat(util.null2str(price.toString())));

				if ("updateDetail".equals(action)) {
					String saleDetail_id = request.getParameter("saleDetail_id");
					paramVO.setSaleDetail_id(saleDetail_id);
					logger.debug("saleDetail_id:".concat(util.null2str(saleDetail_id)));

					saleService.updateSaleDetail(paramVO);
				} else {
					saleService.addSaleDetail(paramVO);
				}
			} else if ("deleteDetail".equals(action)) {
				String saleDetail_id = request.getParameter("saleDetail_id");

				for (String id : saleDetail_id.split(",")) {

					saleService.deleteSaleDetail(id);
				}
				logger.debug("saleDetail_id:".concat(saleDetail_id));

			} else if ("invoice".equals(action)) {
				String result = "";
				String errorMsg = "";
				java.sql.Date invoice_date;
				List<InvoiceTrackVO> invoiceTrackVOList = null;
				HashSet<String> order_noSet = new HashSet<String>();
				HashSet<SaleVO> revomeSet = new HashSet<SaleVO>();;
				try {

					String saleIds = (String) request.getParameter("ids");
					String invoice_date_str = (String) request.getParameter("invoice_date");

					logger.debug("saleIds: " + saleIds);
					logger.debug("invoice_date: " + invoice_date_str);

					try {
						SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
						java.util.Date date = sdf.parse(invoice_date_str);

						invoice_date = new java.sql.Date(date.getTime());
					} catch (ParseException e) {
						errorMsg = "日期格式錯誤";
						response.getWriter().write(errorMsg);
						return;
					}

					GroupVO groupVO = saleService.getGroupInvoiceInfo(group_id);
					InvoiceApi api = new InvoiceApi();

					List<SaleVO> saleVOs = saleService.getSaleOrdernoInfoByIds(group_id, saleIds);

					// 確認資料都沒有發送過
					for (int i = 0; i < saleVOs.size(); i++) {
						String invoice = saleVOs.get(i).getInvoice();
						if ((null != invoice) && (!"".equals(invoice))) {
							String order_no = saleVOs.get(i).getOrder_no();
							// 已有發票的order_no
							order_noSet.add(order_no);
							// 紀錄要移除的
							revomeSet.add(saleVOs.get(i));
						}
					}

					// 移除有開發票的
					for (SaleVO saleVO : revomeSet) {
						saleVOs.remove(saleVO);
					}

					List<List<SaleVO>> groupBySaleVOsList = getGroupBySaleVOsList(saleVOs);

					try {
						// TODO 撈取發票號碼
						invoiceTrackVOList = saleService.getInvoiceTrack(group_id, invoice_date, groupBySaleVOsList);
					} catch (Exception e) {
						logger.debug("發票撈取失敗:" + e.getMessage());
						result = "執行失敗";
					}

					// groupBySaleVOsList.size=invoiceTrackVOList.size=order_no數量
					for (int i = 0; i < invoiceTrackVOList.size(); i++) {
						InvoiceTrackVO invoiceTrackVO = invoiceTrackVOList.get(i);
						List<SaleVO> sameOrderNoSaleVOList = groupBySaleVOsList.get(i);
						String invoiceNum = invoiceTrackVO.getInvoiceNum();

						logger.debug("invoiceNum: " + invoiceNum);

						if (invoiceNum == null || "".equals(invoiceNum)) {
							errorMsg = "發票字軌用罄，請洽系統管理員";
							response.getWriter().write(errorMsg);
							return;
						}
						/* 20171219 將列印明細包成單一"商品"的mask*/
						List<SaleVO> sameOrderNoSaleVOListAfterMask = saleService.maskOverviewByExt(group_id, sameOrderNoSaleVOList);
						
						String reqXml = api.genRequestForC0401(invoiceNum, sameOrderNoSaleVOListAfterMask, groupVO);
						String resXml = api.sendXML(reqXml);
						Index index = api.getIndexResponse(resXml);

						if ("1".equals(index.getReply())) {// 0失敗 1 成功
							String invoiceVcode = api.getInvoiceInvoiceVcode(reqXml);
							String invoice_time = api.getInvoiceInvoice_time(reqXml);

							saleService.updateSaleInvoiceVcodeAndInvoice_time(sameOrderNoSaleVOList, invoiceVcode,
									invoice_time);
							saleService.updateSaleInvoice(sameOrderNoSaleVOList, invoiceTrackVO, invoice_date,
									invoice_time, invoiceVcode);
						}

						if (order_noSet.size() > 0) {
							for (String order_no : order_noSet) {
								result = result + "很抱歉，訂單編號:" + order_no + "已有發票，不可重複發送<br/>";
							}
						}
						result = result + "訂單編號:" + sameOrderNoSaleVOList.get(0).getOrder_no() + index.getMessage()
								+ "<br/>";
					}

				} catch (Exception e) {
					logger.debug("error:" + e.getMessage());
					result = "執行失敗";
				}
				response.getWriter().write(result);
			} else if ("invoice_cancel".equals(action)) {
				String result = "";
				String errorMsg = "";
				String saleIds = (String) request.getParameter("ids");
				String reason = (String) request.getParameter("reason");
				InvoiceApi api = new InvoiceApi();

				logger.debug("saleIds: " + saleIds);
				logger.debug("reason: " + reason);
				logger.debug("group_id: " + group_id);
				try {
					GroupVO groupVO = saleService.getGroupInvoiceInfo(group_id);
					
					List<SaleVO> saleVOs = saleService.getSaleOrdernoInfoByIds(group_id, saleIds);

					errorMsg = saleService.checkCancelDate(saleVOs);

					if (errorMsg.length() > 1) {
						response.getWriter().write(errorMsg);
						return;
					}

					String reqXml = api.genRequestForC0501(reason, saleVOs, groupVO);
					String resXml = api.sendXML(reqXml);
					Index index = api.getIndexResponse(resXml);

					if ("1".equals(index.getReply())) {// 0失敗 1 成功
						saleService.invoiceCancel(group_id, saleIds, reason);
					}

					result = index.getMessage();
				} catch (Exception e) {
					logger.debug(e.getMessage());
					result = "false";
				}
				response.getWriter().write(result);
			} else if ("invoice_print".equals(action)) {
				ResponseVO responseVO = new ResponseVO();
				String saleIds = (String) request.getParameter("ids");

				logger.debug("saleIds: " + saleIds);

				GroupVO groupVO = saleService.getGroupInvoiceInfo(group_id);
				InvoiceApi api = new InvoiceApi();
				List<SaleVO> saleVOs = saleService.getSaleOrdernoInfoByIds(group_id, saleIds);

				responseVO = checkData(saleVOs);

				if (responseVO.getError().length() > 0) {
					String jsonResponse = gson.toJson(responseVO);
					logger.debug("jsonStrList: " + jsonResponse);
					response.getWriter().write(jsonResponse);
					return;
				}

				
				/* 20171219 將列印明細包成單一"商品"的mask*/
				List<SaleVO> saleVOsAfterMask =new ArrayList<SaleVO>();
				List<List<SaleVO>> saleVOsAll_classification = api.classification(saleVOs);
				
				for(List<SaleVO> sameOrderSaleVOlist : saleVOsAll_classification){
					
					List<SaleVO> sameOrderNoSaleVOListAfterMask = saleService.maskOverviewByExt(group_id, sameOrderSaleVOlist);
					for(SaleVO saleVO : sameOrderNoSaleVOListAfterMask){
						
						saleVOsAfterMask.add(saleVO);
					}
				}
				
				List<String> reqXmlList = api.getPrintStr(saleVOsAfterMask, groupVO);
				responseVO.setList(reqXmlList);
				responseVO.setSuccess(true);

				String jsonResponse = gson.toJson(responseVO);
				logger.debug("jsonStrList: " + jsonResponse);
				response.getWriter().write(jsonResponse);

			} else if ("getSaleByUploadDate".equals(action)) {
				String upload_date_start = request.getParameter("upload_date_start");
				String upload_date_end = request.getParameter("upload_date_end");
				
				logger.debug("upload_date_start: " + upload_date_start );
				logger.debug("upload_date_end: " + upload_date_end );

				saleList = saleService.getSaleByUploadDate( group_id, upload_date_start, upload_date_end);

				String jsonStrList = gson.toJson(saleList);
				response.getWriter().write(jsonStrList);
				
			} else if ("getSaleByTransDate".equals(action)) {
				String trans_date_start = request.getParameter("trans_date_start");
				String trans_date_end = request.getParameter("trans_date_end");
				
				logger.debug("trans_date_start: " + trans_date_start );
				logger.debug("trans_date_end: " + trans_date_end );

				saleList = saleService.getSaleByTransDate( group_id, trans_date_start, trans_date_end);

				String jsonStrList = gson.toJson(saleList);
				response.getWriter().write(jsonStrList);
				
			} else if ("invoice_new".equals(action)) {
				String result = "";
				String errorMsg = "";
				java.sql.Date invoice_date;
				List<InvoiceTrackVO> invoiceTrackVOList = null;
				HashSet<String> order_noSet = new HashSet<String>();
				HashSet<SaleVO> revomeSet = new HashSet<SaleVO>();;
				try {

					String orderNos = (String) request.getParameter("ids");
					String invoice_date_str = (String) request.getParameter("invoice_date");

					logger.debug("orderNos: " + orderNos);
					logger.debug("invoice_date: " + invoice_date_str);

					try {
						SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
						java.util.Date date = sdf.parse(invoice_date_str);

						invoice_date = new java.sql.Date(date.getTime());
					} catch (ParseException e) {
						errorMsg = "日期格式錯誤";
						response.getWriter().write(errorMsg);
						return;
					}

					GroupVO groupVO = saleService.getGroupInvoiceInfo(group_id);
					InvoiceApi api = new InvoiceApi();

					List<SaleVO> saleVOs = saleService.getSaleOrdernoInfoByOrdernos(group_id, orderNos);

					// 確認資料都沒有發送過
					for (int i = 0; i < saleVOs.size(); i++) {
						String invoice = saleVOs.get(i).getInvoice();
						if ((null != invoice) && (!"".equals(invoice))) {
							String order_no = saleVOs.get(i).getOrder_no();
							// 已有發票的order_no
							order_noSet.add(order_no);
							// 紀錄要移除的
							revomeSet.add(saleVOs.get(i));
						}
					}

					// 移除有開發票的
					for (SaleVO saleVO : revomeSet) {
						saleVOs.remove(saleVO);
					}

					List<List<SaleVO>> groupBySaleVOsList = getGroupBySaleVOsList(saleVOs);

					for (int i = 0; i < groupBySaleVOsList.size(); i++) {
						List<SaleVO> sameOrderNoSaleVOList = groupBySaleVOsList.get(i);
						logger.debug("Order No:" + sameOrderNoSaleVOList.get(0).getOrder_no());
						
						invoiceTrackVOList = saleService.getInvoiceTrack(group_id, invoice_date, groupBySaleVOsList);
						InvoiceTrackVO invoiceTrackVO = invoiceTrackVOList.get(0);
						String invoiceNum = invoiceTrackVO.getInvoiceNum();
						logger.debug("Invoice No:" + invoiceNum);
						if (invoiceNum == null || "".equals(invoiceNum)) {
							result = "發票字軌用罄，請洽系統管理員";
							break;
						}
						
						List<SaleVO> sameOrderNoSaleVOListAfterMask = saleService.maskOverviewByExt(group_id, sameOrderNoSaleVOList);
						
						String reqXml = api.genRequestForC0401(invoiceNum, sameOrderNoSaleVOListAfterMask, groupVO);
						String resXml = api.sendXML(reqXml);
						Index index = api.getIndexResponse(resXml);

						result += "訂單編號：" + sameOrderNoSaleVOList.get(0).getOrder_no() 
								+ "，" + index.getInvoiceNumber() + "，" + index.getMessage()
								+ "<br/>";
						
						if ("1".equals(index.getReply())) {// 0失敗 1 成功
							String invoiceVcode = api.getInvoiceInvoiceVcode(reqXml);
							String invoice_time = api.getInvoiceInvoice_time(reqXml);
							saleService.updateSaleInvoiceVcodeAndInvoice_time(sameOrderNoSaleVOList, 
									invoiceVcode, invoice_time);
							saleService.updateSaleInvoice(sameOrderNoSaleVOList, invoiceTrackVO, invoice_date,
									invoice_time, invoiceVcode);
							saleService.increaseInvoiceTrack(invoiceTrackVO);
						} else {
							break;
						}

						if (order_noSet.size() > 0) {
							for (String order_no : order_noSet) {
								result += "很抱歉，訂單編號:" + order_no + "已有發票，不可重複發送<br/>";
							}
						}
					}
				} catch (Exception e) {
					logger.debug("error:" + e.getMessage());
					result = "執行失敗";
				}
				response.getWriter().write(result);
			} else if ("invoice_cancel_new".equals(action)) {
				String result = "";
				String errorMsg = "";
				String orderNos = (String) request.getParameter("ids");
				String reason = (String) request.getParameter("reason");
				InvoiceApi api = new InvoiceApi();

				logger.debug("orderNos: " + orderNos);
				logger.debug("reason: " + reason);
				logger.debug("group_id: " + group_id);
				try {
					GroupVO groupVO = saleService.getGroupInvoiceInfo(group_id);
					
					List<SaleVO> saleVOs = saleService.getSaleOrdernoInfoByOrdernos(group_id, orderNos);

					errorMsg = saleService.checkCancelDate(saleVOs);

					if (errorMsg.length() > 1) {
						response.getWriter().write(errorMsg);
						return;
					}

					String reqXml = api.genRequestForC0501(reason, saleVOs, groupVO);
					String resXml = api.sendXML(reqXml);
					Index index = api.getIndexResponse(resXml);
					
					String saleIdsStr="";
					for(SaleVO saleVO : saleVOs){
						saleIdsStr += ("".equals(saleIdsStr)?"":",") + "'"+saleVO.getSale_id()+"'" ;
					}
	
					if ("1".equals(index.getReply())) {// 0失敗 1 成功
						saleService.invoiceCancel(group_id, saleIdsStr, reason);
					}

					result = index.getMessage();
				} catch (Exception e) {
					logger.debug(e.getMessage());
					result = "false";
				}
				response.getWriter().write(result);
				
			} else if ("invoice_print_new".equals(action)) {
				ResponseVO responseVO = new ResponseVO();
				String orderNos = (String) request.getParameter("ids");

				logger.debug("orderNos: " + orderNos);

				GroupVO groupVO = saleService.getGroupInvoiceInfo(group_id);
				InvoiceApi api = new InvoiceApi();
				List<SaleVO> saleVOs = saleService.getSaleOrdernoInfoByOrdernos(group_id,orderNos);

				responseVO = checkData(saleVOs);

				if (responseVO.getError().length() > 0) {
					String jsonResponse = gson.toJson(responseVO);
					logger.debug("jsonStrList: " + jsonResponse);
					response.getWriter().write(jsonResponse);
					return;
				}

				List<SaleVO> saleVOsAfterMask =new ArrayList<SaleVO>();
				List<List<SaleVO>> saleVOsAll_classification = api.classification(saleVOs);
				
				for(List<SaleVO> sameOrderSaleVOlist : saleVOsAll_classification){
					
					List<SaleVO> sameOrderNoSaleVOListAfterMask = saleService.maskOverviewByExt(group_id, sameOrderSaleVOlist);
					for(SaleVO saleVO : sameOrderNoSaleVOListAfterMask){
						
						saleVOsAfterMask.add(saleVO);
					}
				}
				
				List<String> reqXmlList = api.getPrintStr(saleVOsAfterMask, groupVO);
				responseVO.setList(reqXmlList);
				responseVO.setSuccess(true);

				String jsonResponse = gson.toJson(responseVO);
				logger.debug("jsonStrList: " + jsonResponse);
				response.getWriter().write(jsonResponse);
				
			} else if ("get_amount_from_ext".equals(action)) {
				String orderNo = (String) request.getParameter("orderNo");
				orderNo = "'" + orderNo + "'";
				
				logger.debug("orderNo: " + orderNo);

				List<SaleVO> saleVOs = saleService.getSaleOrdernoInfoByOrdernos(group_id,orderNo);
				saleVOs = saleService.maskOverviewByExt(group_id, saleVOs);
				
				String jsonResponse = gson.toJson(saleVOs);
				logger.debug("jsonStrList: " + jsonResponse);
				response.getWriter().write(jsonResponse);
			} else if ("update_turn_flag".equals(action)) {
				String saleId = (String) request.getParameter("sale_id");
				String turnFlag = (String) request.getParameter("turn_flag");
				boolean blnTurnFlag = "1".equals(turnFlag);
				
				logger.debug("saleId: " + saleId);
				logger.debug("turnFlag: " + turnFlag);
				
				SaleVO saleVO = new SaleVO();
				saleVO.setGroup_id(group_id);
				saleVO.setSale_id(saleId);
				saleVO.setTurnFlag(blnTurnFlag);

				int matchRows = saleService.updateTurnFlag(saleVO);
				
				response.getWriter().write("[{\"matchRows\":\""+matchRows+"\"}]");
			}
		} catch (Exception e) {
			logger.error("Exception:".concat(e.getMessage()));
		}
	}

	/*************************** 自訂方法 ****************************************/
	/**
	 * <p>處理傳過來的日期格式
	 * 
	 * @param Date
	 * @return
	 */
	public int DateConversionToDigital(String Date) {
		StringBuffer str = new StringBuffer();
		String[] dateArray = Date.split("-");
		for (String i : dateArray) {
			str.append(i);
		}
		return Integer.parseInt(str.toString());
	}

	/**
	 * <p>獲得格式過的今年今月今日
	 * 
	 * @return
	 */
	public String getThisYearMonthDate() {
		Calendar cal = Calendar.getInstance();
		String year = String.valueOf(cal.get(Calendar.YEAR));
		String month = String.valueOf(cal.get(Calendar.MONTH) + 1);
		String date = String.valueOf(cal.get(Calendar.DATE));
		return year + formatTime(month) + formatTime(date);
	}

	/**
	 * <p>處理個位數月份以及日
	 * 
	 * @param str
	 * @return
	 */
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

	/**
	 * <p>格式化單號，不足四位補位
	 * 
	 * @param str
	 * @return
	 */
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
	
	/**
	 * <p>檢查開立發票，沒有開立，則填入錯誤原因
	 * 
	 * @param saleVOsAll
	 * @return
	 */
	public ResponseVO checkData(List<SaleVO> saleVOsAll){
		ResponseVO responseVO = new ResponseVO();
		for (int i = 0; i < saleVOsAll.size(); i++) {
			SaleVO saleVO = saleVOsAll.get(i);
			// check data
			if ("".equals(saleVO.getInvoice()) || null == saleVO.getInvoice()) {
				logger.error("getInvoice is null");
				String order_no=saleVO.getOrder_no();
				responseVO.setErrorReason("錯誤:訂單編號"+order_no+"，未開立發票。");
				return responseVO;
			}
		}
		return responseVO;
	}
	
	/**
	 * <p>取得訂單資料
	 * 
	 * @param saleVOs
	 * @return
	 */
	public List<List<SaleVO>> getGroupBySaleVOsList(List<SaleVO> saleVOs) {
		List<List<SaleVO>> groupBySaleVOsList = new ArrayList<List<SaleVO>>();
		List<SaleVO> sameOrderNoList = new ArrayList<SaleVO>();

		String order_no_record = "";
		String order_no_now = "";

		for (int i = 0; i < saleVOs.size(); i++) {
			SaleVO saleVO = saleVOs.get(i);
			order_no_now = saleVO.getOrder_no();
			if(i==0){
				order_no_record = order_no_now;
			}

			if (!(order_no_now.equals(order_no_record))) {
				order_no_record = order_no_now;
				groupBySaleVOsList.add(sameOrderNoList);
				sameOrderNoList = new ArrayList<SaleVO>();
			}
			sameOrderNoList.add(saleVO);

			if (i == (saleVOs.size() - 1)) {
				groupBySaleVOsList.add(sameOrderNoList);
			}
		}

		return groupBySaleVOsList;
	}
	
}