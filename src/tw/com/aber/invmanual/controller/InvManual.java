package tw.com.aber.invmanual.controller;

import java.io.IOException;
import java.io.StringWriter;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.Date;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.xml.bind.JAXB;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import tw.com.aber.inv.controller.InvoiceApi;
import tw.com.aber.inv.vo.Amount;
import tw.com.aber.inv.vo.Buyer;
import tw.com.aber.inv.vo.Details;
import tw.com.aber.inv.vo.Index;
import tw.com.aber.inv.vo.Invoice;
import tw.com.aber.inv.vo.InvoiceC0501;
import tw.com.aber.inv.vo.Main;
import tw.com.aber.inv.vo.ProductItem;
import tw.com.aber.inv.vo.Seller;
import tw.com.aber.util.Util;
import tw.com.aber.vo.GroupVO;
import tw.com.aber.vo.InvBuyerVO;
import tw.com.aber.vo.InvManualDetailVO;
import tw.com.aber.vo.InvManualVO;
import tw.com.aber.vo.InvoiceTrackVO;

public class InvManual extends HttpServlet {

	private static final long serialVersionUID = 1L;
	private static final Logger logger = LogManager.getLogger(InvManual.class);

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		doPost(req, resp);
	}

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

		req.setCharacterEncoding("UTF-8");
		resp.setCharacterEncoding("UTF-8");

		String groupId = (String) req.getSession().getAttribute("group_id");
		String userId = (String) req.getSession().getAttribute("user_id");
		String action = req.getParameter("action");

		logger.debug("Action: {} \\ GroupId: {} \\ UserId: {}", action, groupId, userId);

		Util util = new Util();
		InvManualService service = new InvManualService();
		Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd").create();

		if ("query_invoice".equals(action)) {
			String startDate = req.getParameter("startDate");
			String endDate = req.getParameter("endDate");

			String regular = "[0-9]{4}-[0-9]{2}-[0-9]{2}";

			String result = null;

			if (util.checkDateFormat(startDate, regular) && util.checkDateFormat(endDate, regular)) {
				result = gson.toJson(service.searchInvManualByInvoiceDate(groupId, startDate, endDate));
			} else {
				result = gson.toJson(service.searchAllInvManual(groupId));
			}
			logger.debug("result: {}", result);
			resp.getWriter().write(result);
		} else if ("insertMaster".equals(action)) {
			String invoice_type = req.getParameter("invoice_type");
			String title = req.getParameter("title");
			String unicode = req.getParameter("unicode");
			String address = req.getParameter("address");
			String memo = req.getParameter("memo");
			String invoice_date_str = req.getParameter("invoice_date");
			String tax_type = req.getParameter("tax_type");
			String result = "OK";
			String year_month = null;
			Date invoice_date = null;

			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

			try {
				invoice_date = new java.sql.Date(sdf.parse(invoice_date_str).getTime());
				year_month = sdf.format(invoice_date);

				String monthStr = year_month.substring(5, 7);
				String yearStr = year_month.substring(0, 4);

				int monthInt = Integer.valueOf(monthStr);

				int yearInt = Integer.valueOf(yearStr);
				yearInt = yearInt - 1911;

				Integer end = null;
				if (monthInt % 2 != 0) {
					end = monthInt + 1;
				} else {
					end = monthInt;
				}
				year_month = "";
				if (yearInt < 100) {
					year_month += "0" + yearInt;
				} else {
					year_month += yearInt;
				}
				if (end < 10) {
					year_month += "0" + end;
				} else {
					year_month += end;
				}
				logger.debug("year_month: " + year_month);

				InvManualVO invManualVO = new InvManualVO();
				invManualVO.setGroup_id(groupId);
				invManualVO.setInvoice_type(invoice_type);
				invManualVO.setYear_month(year_month);
				invManualVO.setInvoice_date(invoice_date);
				invManualVO.setTitle(title);
				invManualVO.setUnicode(unicode);
				invManualVO.setAddress(address);
				invManualVO.setMemo(memo);
				invManualVO.setTax_type(Integer.valueOf(tax_type));

				service.insertInvManual(invManualVO);
			} catch (Exception e) {
				logger.error(e.getMessage());
				result = "ERROR";
			}
			resp.getWriter().write(result);

		} else if ("query_invoice_detail".equals(action)) {
			String inv_manual_id = req.getParameter("inv_manual_id");

			String result = null;
			try {
				InvManualDetailVO invManualDetailVO = new InvManualDetailVO();
				invManualDetailVO.setGroup_id(groupId);
				invManualDetailVO.setInv_manual_id(inv_manual_id);
				result = gson.toJson(service.searchInvManualDetailByInvManualId(invManualDetailVO));
				logger.debug("result: {}", result);
			} catch (Exception e) {
				logger.error(e.getMessage());
			}
			resp.getWriter().write(result);
		} else if ("insertDetail".equals(action)) {
			String inv_manual_id = req.getParameter("inv_manual_id");
			String price = req.getParameter("price");
			String quantity = req.getParameter("quantity");
			String description = req.getParameter("description");
			String subtotal = req.getParameter("subtotal");
			String memo = req.getParameter("memo");
			String result = "OK";
			logger.debug("inv_manual_id: {} \\ price: {} \\ quantity: {} \\ description: {} \\ subtotal: {}",
					inv_manual_id, price, quantity, description, subtotal);
			try {
				InvManualDetailVO invManualDetailVO = new InvManualDetailVO();
				invManualDetailVO.setGroup_id(groupId);
				invManualDetailVO.setInv_manual_id(inv_manual_id);
				invManualDetailVO.setPrice(Integer.valueOf(price));
				invManualDetailVO.setQuantity(Integer.valueOf(quantity));
				invManualDetailVO.setDescription(description);
				invManualDetailVO.setSubtotal(Integer.valueOf(subtotal));
				invManualDetailVO.setMemo(memo);
				service.insertInvManualDetail(invManualDetailVO);
			} catch (Exception e) {
				logger.error(e.getMessage());
				result = "ERROR";
			}
			resp.getWriter().write(result);

		} else if ("delete_invoice_detail".equals(action)) {
			String[] detail_ids = req.getParameter("inv_manual_detail_id").split(",");
			String inv_manual_id = req.getParameter("inv_manual_id");
			String result = "OK";
			try {
				InvManualDetailVO invManualDetailVO = new InvManualDetailVO();
				invManualDetailVO.setInv_manual_id(inv_manual_id);
				invManualDetailVO.setGroup_id(groupId);

				for (int i = 0; i < detail_ids.length; i++) {
					invManualDetailVO.setInv_manual_detail_id(detail_ids[i]);
					service.delInvManualDetail(invManualDetailVO);
				}
			} catch (Exception e) {
				logger.error(e.getMessage());
				result = "ERROR";
			}
			logger.debug("inv_manual_id: {} \\ result: {}", inv_manual_id, result);
			resp.getWriter().write(result);
		} else if ("delete_invoice".equals(action)) {
			String[] master_ids = req.getParameter("inv_manual_id").split(",");
			String result = "OK";
			try {
				InvManualVO invManualVO = new InvManualVO();
				invManualVO.setGroup_id(groupId);

				for (int i = 0; i < master_ids.length; i++) {
					invManualVO.setInv_manual_id(master_ids[i]);
					service.delInvManual(invManualVO);
				}
			} catch (Exception e) {
				logger.error(e.getMessage());
				result = "ERROR";
			}
			resp.getWriter().write(result);
		} else if ("updateMaster".equals(action)) {
			String inv_manual_id = req.getParameter("inv_manual_id");
			String invoice_type = req.getParameter("invoice_type");
			String title = req.getParameter("title");
			String unicode = req.getParameter("unicode");
			String address = req.getParameter("address");
			String memo = req.getParameter("memo");
			String invoice_no = req.getParameter("invoice_no");
			String invoice_date_str = req.getParameter("invoice_date");
			String tax_type = req.getParameter("tax_type");
			String amount = req.getParameter("amount");
			String amount_plustax = req.getParameter("amount_plustax");
			String tax = req.getParameter("tax");
			String year_month = null, result = "OK";

			Date invoice_date = null;
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
			try {
				invoice_date = new java.sql.Date(sdf.parse(invoice_date_str).getTime());
				year_month = sdf.format(invoice_date);

				String monthStr = year_month.substring(5, 7);
				String yearStr = year_month.substring(0, 4);

				int monthInt = Integer.valueOf(monthStr);

				int yearInt = Integer.valueOf(yearStr);
				yearInt = yearInt - 1911;

				Integer end = null;
				if (monthInt % 2 != 0) {
					end = monthInt + 1;
				} else {
					end = monthInt;
				}
				year_month = "";
				if (yearInt < 100) {
					year_month += "0" + yearInt;
				} else {
					year_month += yearInt;
				}
				if (end < 10) {
					year_month += "0" + end;
				} else {
					year_month += end;
				}
				logger.debug("year_month: " + year_month);

				InvManualVO invManualVO = new InvManualVO();
				invManualVO.setInv_manual_id(inv_manual_id);
				invManualVO.setGroup_id(groupId);
				invManualVO.setInvoice_type(invoice_type);
				invManualVO.setYear_month(year_month);
				invManualVO.setInvoice_no(invoice_no);
				invManualVO.setInvoice_date(invoice_date);
				invManualVO.setTitle(title);
				invManualVO.setUnicode(unicode);
				invManualVO.setAddress(address);
				invManualVO.setMemo(memo);
				invManualVO.setTax_type(Integer.valueOf(tax_type));
				invManualVO.setAmount(Integer.valueOf(amount));
				invManualVO.setAmount_plustax(Integer.valueOf(amount_plustax));
				invManualVO.setTax(Integer.valueOf(tax));

				service.updateInvManual(invManualVO);
			} catch (Exception e) {
				logger.error(e.getMessage());
				result = "ERROR";
			}
			resp.getWriter().write(result);

		} else if ("updateDetail".equals(action)) {
			String inv_manual_detail_id = req.getParameter("inv_manual_detail_id");
			String inv_manual_id = req.getParameter("inv_manual_id");
			String price = req.getParameter("price");
			String quantity = req.getParameter("quantity");
			String description = req.getParameter("description");
			String subtotal = req.getParameter("subtotal");
			String memo = req.getParameter("memo");
			String result = "OK";
			logger.debug(
					"inv_manual_detail_id:{} \\inv_manual_id: {} \\ price: {} \\ quantity: {} \\ description: {} \\ subtotal: {}",
					inv_manual_detail_id, inv_manual_id, price, quantity, description, subtotal);
			try {
				InvManualDetailVO invManualDetailVO = new InvManualDetailVO();
				invManualDetailVO.setGroup_id(groupId);
				invManualDetailVO.setInv_manual_detail_id(inv_manual_detail_id);
				invManualDetailVO.setInv_manual_id(inv_manual_id);
				invManualDetailVO.setPrice(Integer.valueOf(price));
				invManualDetailVO.setQuantity(Integer.valueOf(quantity));
				invManualDetailVO.setDescription(description);
				invManualDetailVO.setSubtotal(Integer.valueOf(subtotal));
				invManualDetailVO.setMemo(memo);
				service.updateInvManualDetail(invManualDetailVO);
			} catch (Exception e) {
				logger.error(e.getMessage());
				result = "ERROR";
			}
			resp.getWriter().write(result);
		} else if ("issueTheInvoice".equals(action)) {
			String ids = req.getParameter("ids");
			String[] idsArr = ids.split(",");

			InvManualVO invManualVO = null;

			List<Map<String, String>> list = new ArrayList<Map<String, String>>();

			try {
				GroupVO groupVO = service.getGroupInvoiceInfo(groupId);

				for (String inv_manual_id : idsArr) {
					invManualVO = service.selectInvManualByInvManualId(groupId, inv_manual_id);

					InvManualDetailVO invManualDetailVO = new InvManualDetailVO();
					invManualDetailVO.setGroup_id(groupId);
					invManualDetailVO.setInv_manual_id(inv_manual_id);
					List<InvManualDetailVO> detailVOs = service.searchInvManualDetailByInvManualId(invManualDetailVO);

					List<ProductItem> productItems = new ArrayList<ProductItem>();
					
					if (detailVOs == null || detailVOs.size() == 0) {
						Map<String, String> map = new HashMap<String, String>();
						map.put("invoice_no", "");
						map.put("message", "請新增明細。");
						list.add(map);
						resp.getWriter().write(new Gson().toJson(list));
						return;
					}

					for (int i = 0; i < detailVOs.size(); i++) {

						ProductItem productItem = new ProductItem();
						productItem.setDescription(detailVOs.get(i).getDescription());
						productItem.setQuantity(util.null2str(detailVOs.get(i).getQuantity()));
						productItem.setUnitPrice(util.null2str(detailVOs.get(i).getPrice()));
						productItem.setAmount(util.null2str(detailVOs.get(i).getSubtotal()));
						productItem.setSequenceNumber(util.null2str(i + 1));
						productItem.setRemark(util.null2str(detailVOs.get(i).getMemo()));

						productItems.add(productItem);
					}
					Details details = new Details();
					details.setProductItem(productItems);

					Amount amount = new Amount();
					Integer amountVal = invManualVO.getAmount();
					amount.setSalesAmount(util.null2str(amountVal));

					Integer taxType = invManualVO.getTax_type();
					Float taxRate = 0F;
					if (taxType == 1) {
						taxRate = 0.05F;
					}
					Integer taxAmount = 0;
					taxAmount = invManualVO.getTax();

					amount.setTaxType(util.null2str(taxType));
					amount.setTaxRate(util.null2str(taxRate));
					amount.setTaxAmount(util.null2str(taxAmount));
					amount.setTotalAmount(String.valueOf(invManualVO.getAmount_plustax()));

					String sellerId = null, sellerName = null, buyerId = null, buyName = null;
					// 二聯式
					if ("1".equals(invManualVO.getInvoice_type())) {
						sellerId = "";
						sellerName = "";
						buyerId = "";
						buyName = "";
					}
					// 三聯式
					if ("2".equals(invManualVO.getInvoice_type())) {
						sellerId = groupVO.getGroup_unicode();
						sellerName = groupVO.getGroup_name();
						buyerId = invManualVO.getUnicode();
						buyName = invManualVO.getTitle();
					}
					Seller seller = new Seller();
					seller.setIdentifier(sellerId);
					seller.setName(sellerName);

					Buyer buyer = new Buyer();
					buyer.setIdentifier(buyerId);
					buyer.setName(buyName);
					buyer.setAddress(invManualVO.getAddress());

					// 發票號碼
					InvoiceTrackVO invoiceTrackVO = service.getInvoiceTrack(groupId, invManualVO.getInvoice_date());
					String invoiceNum = invoiceTrackVO.getInvoiceNum();
					logger.debug("invoiceNum: " + invoiceNum);

					if (invoiceNum == null || "".equals(invoiceNum)) {
						Map<String, String> map = new HashMap<String, String>();
						map.put("invoice_no", "");
						map.put("message", "發票字軌用罄，請洽系統管理員");
						list.add(map);
						break;
					}

					Main main = new Main();
					main.setInvoiceNumber(invoiceNum);
					main.setInvoiceDate(util.null2str(invManualVO.getInvoice_date()));
					main.setInvoiceTime("00:00:00");

					if (taxType == 2) {
						main.setCustomsClearanceMark("1");
					}

					String voiceType = "07";

					main.setInvoiceType(voiceType);
					main.setDonateMark("0");
					main.setPrintMark("Y");

					main.setSeller(seller);
					main.setBuyer(buyer);
					main.setMainRemark(invManualVO.getMemo());

					SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
					java.util.Date date = new java.util.Date();
					String ymdhms = sdf.format(date);

					Invoice invoice = new Invoice();
					invoice.setInvoiceCode("A0401");
					invoice.setPosSn(groupVO.getInvoice_key());
					invoice.setPosId(groupVO.getInvoice_posno());
					invoice.setSysTime(ymdhms);

					invoice.setMain(main);
					invoice.setDetails(details);
					invoice.setAmount(amount);

					StringWriter sw = new StringWriter();
					JAXB.marshal(invoice, sw);
					logger.debug(sw.toString());
					String reqXml = sw.toString();
					InvoiceApi api = new InvoiceApi();

					String resXml = api.sendXML(reqXml);
					Index index = api.getIndexResponse(resXml);
					
					Map<String, String> map = new HashMap<String, String>();
					map.put("invoice_no", invoiceNum);
					map.put("message", index.getMessage());
					map.put("reply", index.getReply());
					list.add(map);
					
					if (index != null && index.getReply().equals("1")) {
						InvManualVO tempVO = new InvManualVO();
						tempVO.setGroup_id(groupId);
						tempVO.setInvoice_no(invoiceNum);
						tempVO.setInv_manual_id(inv_manual_id);
						service.updateInvManualInvFlag(tempVO, invoiceTrackVO, invoice);
						service.increaseInvoiceTrack(invoiceTrackVO);
					} else {
						break;
					}
				}
			} catch (Exception e) {
				logger.error(e.getMessage());
				Map<String, String> map = new HashMap<String, String>();
				map.put("invoice_no", "");
				map.put("message", "非預期錯誤");
				list.add(map);
			}
			resp.getWriter().write(new Gson().toJson(list));
		} else if ("cancelInvoice".equals(action)) {
			String ids = req.getParameter("ids");
			String reason = (String) req.getParameter("reason");
			logger.debug("ids:" + ids);
			logger.debug("reason:" + reason);
			
			String[] idsArr = ids.split(",");
			
			GroupVO groupVO = service.getGroupInvoiceInfo(groupId);
			
			InvManualVO invManualVO = null;
			List<Map<String, String>> list = new ArrayList<Map<String, String>>();
			
			for (String inv_manual_id : idsArr) {
				invManualVO = service.selectInvManualByInvManualId(groupId, inv_manual_id);
				
				SimpleDateFormat dt1 = new SimpleDateFormat("yyyy-MM-dd");
				SimpleDateFormat dt2 = new SimpleDateFormat("HH:mm:ss");
				SimpleDateFormat dt3 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
				java.util.Date date = new java.util.Date();
				String ymd = dt1.format(date);
				String hms = dt2.format(date);
				String ymdhms = dt3.format(date);
				
				String cancelInvoiceNumber = invManualVO.getInvoice_no();
				String invoiceDate = invManualVO.getInvoice_date().toString();
				String buyerId = invManualVO.getUnicode();
				String sellerId = groupVO.getGroup_unicode();
				String cancelDate = ymd;
				String cancelTime = hms;
				String cancelReason = reason;
				String returnTaxDocumentNumber = "";
				String remark = "";
				
				InvoiceC0501 invoice = new InvoiceC0501();
				invoice.setInvoiceCode("A0501");
				invoice.setPosSn(groupVO.getInvoice_key());
				invoice.setPosId(groupVO.getInvoice_posno());
				invoice.setSysTime(ymdhms);

				invoice.setCancelInvoiceNumber(cancelInvoiceNumber);
				invoice.setInvoiceDate(invoiceDate);
				invoice.setBuyerId(buyerId);
				invoice.setSellerId(sellerId);
				invoice.setCancelDate(cancelDate);
				invoice.setCancelTime(cancelTime);
				invoice.setCancelReason(cancelReason);
				invoice.setReturnTaxDocumentNumber(returnTaxDocumentNumber);
				invoice.setRemark(remark);
				
				StringWriter sw = new StringWriter();
				JAXB.marshal(invoice, sw);
				logger.debug(sw.toString());
				
				String reqXml = sw.toString();
				InvoiceApi api = new InvoiceApi();
						
				String resXml = api.sendXML(reqXml);
				Index index = api.getIndexResponse(resXml);
				
				Map<String, String> map = new HashMap<String, String>();
				map.put("invoice_no", cancelInvoiceNumber);
				map.put("message", index.getMessage());
				map.put("reply", index.getReply());
				list.add(map);
				
				if (index != null && index.getReply().equals("1")) {
					InvManualVO tempVO = new InvManualVO();
					tempVO.setGroup_id(groupId);
					tempVO.setInvoice_no(cancelInvoiceNumber);
					tempVO.setInv_manual_id(inv_manual_id);
					tempVO.setInvoice_reason(reason);
					service.updateInvManualForCancelInvoice(tempVO);
				} else {
					break;
				}
			}
			resp.getWriter().write(new Gson().toJson(list));
		} else if ("getInvBuyerData".equals(action)) {
			String term = req.getParameter("term");
			String type = req.getParameter("type");
			String result = "";

			logger.error("term: {} \\ type: {} ", term, type);

			try {
				InvBuyerVO invBuyerVO = new InvBuyerVO();
				invBuyerVO.setGroup_id(groupId);

				if ("title".equals(type)) {
					invBuyerVO.setTitle(term);
					invBuyerVO.setUnicode("");
				} else if ("unicode".equals(type)) {
					invBuyerVO.setUnicode(term);
					invBuyerVO.setTitle("");
				}
				List<InvBuyerVO> list = service.getInvBuyerData(invBuyerVO);

				result = new GsonBuilder().setDateFormat("yyyy-MM-dd").create().toJson(list);
				logger.error("result: {}", result);
			} catch (Exception e) {
				logger.error(e.getMessage());
			}
			resp.getWriter().write(result);
		} else if ("transferInvoice".equals(action)) {
			String ids = req.getParameter("ids");
			String[] idsArr = ids.split(",");

			InvManualVO invManualVO = null;

			List<Map<String, String>> list = new ArrayList<Map<String, String>>();

			try {
				GroupVO groupVO = service.getGroupInvoiceInfo(groupId);

				for (String inv_manual_id : idsArr) {
					invManualVO = service.selectInvManualByInvManualId(groupId, inv_manual_id);

					InvManualDetailVO invManualDetailVO = new InvManualDetailVO();
					invManualDetailVO.setGroup_id(groupId);
					invManualDetailVO.setInv_manual_id(inv_manual_id);
					List<InvManualDetailVO> detailVOs = service.searchInvManualDetailByInvManualId(invManualDetailVO);

					List<ProductItem> productItems = new ArrayList<ProductItem>();
					
					if (detailVOs == null || detailVOs.size() == 0) {
						Map<String, String> map = new HashMap<String, String>();
						map.put("invoice_no", "");
						map.put("message", "請新增明細。");
						list.add(map);
						resp.getWriter().write(new Gson().toJson(list));
						return;
					}

					for (int i = 0; i < detailVOs.size(); i++) {
						ProductItem productItem = new ProductItem();
						
						productItem.setDescription(detailVOs.get(i).getDescription());
						productItem.setQuantity(util.null2str(detailVOs.get(i).getQuantity()));
						productItem.setUnitPrice(util.null2str(detailVOs.get(i).getPrice()));
						productItem.setAmount(util.null2str(detailVOs.get(i).getSubtotal()));
						productItem.setSequenceNumber(util.null2str(i + 1));
						productItem.setRemark(util.null2str(detailVOs.get(i).getMemo()));

						productItems.add(productItem);
					}
					Details details = new Details();
					details.setProductItem(productItems);

					Amount amount = new Amount();
					Integer amountVal = invManualVO.getAmount();
					amount.setSalesAmount(util.null2str(amountVal));

					Integer taxType = invManualVO.getTax_type();
					Float taxRate = 0F;
					if (taxType == 1) {
						taxRate = 0.05F;
					}
					Integer taxAmount = 0;
					taxAmount = invManualVO.getTax();

					amount.setTaxType(util.null2str(taxType));
					amount.setTaxRate(util.null2str(taxRate));
					amount.setTaxAmount(util.null2str(taxAmount));
					amount.setTotalAmount(String.valueOf(invManualVO.getAmount_plustax()));

					String sellerId = null, sellerName = null, buyerId = null, buyName = null;
					// 二聯式
					if ("1".equals(invManualVO.getInvoice_type())) {
						sellerId = "";
						sellerName = "";
						buyerId = "";
						buyName = "";
					}
					// 三聯式
					if ("2".equals(invManualVO.getInvoice_type())) {
						sellerId = groupVO.getGroup_unicode();
						sellerName = groupVO.getGroup_name();
						buyerId = invManualVO.getUnicode();
						buyName = invManualVO.getTitle();
					}
					Seller seller = new Seller();
					seller.setIdentifier(sellerId);
					seller.setName(sellerName);

					Buyer buyer = new Buyer();
					buyer.setIdentifier(buyerId);
					buyer.setName(buyName);
					buyer.setAddress(invManualVO.getAddress());

					// 發票號碼
//					InvoiceTrackVO invoiceTrackVO = service.getInvoiceTrack(groupId, invManualVO.getInvoice_date());
//					String invoiceNum = invoiceTrackVO.getInvoiceNum();
//					logger.debug("invoiceNum: " + invoiceNum);
//
//					if (invoiceNum == null || "".equals(invoiceNum)) {
//						Map<String, String> map = new HashMap<String, String>();
//						map.put("invoice_no", "");
//						map.put("message", "發票字軌用罄，請洽系統管理員");
//						list.add(map);
//						break;
//					}

					Main main = new Main();
					main.setInvoiceNumber(invManualVO.getInvoice_no());
					main.setInvoiceDate(util.null2str(invManualVO.getInvoice_date()));
					main.setInvoiceTime("00:00:00");

					if (taxType == 2) {
						main.setCustomsClearanceMark("1");
					}

					String voiceType = "07";

					main.setInvoiceType(voiceType);
					main.setDonateMark("0");
					main.setPrintMark("Y");

					main.setSeller(seller);
					main.setBuyer(buyer);
					main.setMainRemark(invManualVO.getMemo());

					SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
					java.util.Date date = new java.util.Date();
					String ymdhms = sdf.format(date);

					Invoice invoice = new Invoice();
					invoice.setInvoiceCode("A0401");
					invoice.setPosSn(groupVO.getInvoice_key());
					invoice.setPosId(groupVO.getInvoice_posno());
					invoice.setSysTime(ymdhms);

					invoice.setMain(main);
					invoice.setDetails(details);
					invoice.setAmount(amount);

					StringWriter sw = new StringWriter();
					JAXB.marshal(invoice, sw);
					logger.debug(sw.toString());
					String reqXml = sw.toString();
					InvoiceApi api = new InvoiceApi();

					String resXml = api.sendXML(reqXml);
					Index index = api.getIndexResponse(resXml);
					
					Map<String, String> map = new HashMap<String, String>();
//					map.put("invoice_no", invoiceNum);
					map.put("invoice_no", invManualVO.getInvoice_no());
					map.put("message", index.getMessage());
					map.put("reply", index.getReply());
					list.add(map);
					
					if (index != null && index.getReply().equals("1")) {
						InvManualVO tempVO = new InvManualVO();
						tempVO.setGroup_id(groupId);
//						tempVO.setInvoice_no(invoiceNum);
						tempVO.setInv_manual_id(inv_manual_id);
						service.updateInvManualInvFlagPershing(tempVO);
//						service.increaseInvoiceTrack(invoiceTrackVO);
					} else {
						break;
					}
				}
			} catch (Exception e) {
				logger.error(e.getMessage());
				Map<String, String> map = new HashMap<String, String>();
				map.put("invoice_no", "");
				map.put("message", "非預期錯誤");
				list.add(map);
			}
			resp.getWriter().write(new Gson().toJson(list));
		}
	}
}
