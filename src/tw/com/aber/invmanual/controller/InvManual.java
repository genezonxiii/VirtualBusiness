package tw.com.aber.invmanual.controller;

import java.io.IOException;
import java.io.StringWriter;
import java.math.BigDecimal;
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
					InvoiceTrackVO invoiceTrackVO = service.getInvoiceNum(groupId, invManualVO.getInvoice_date());
					String invoiceNum = invoiceTrackVO.getInvoiceNum();

					logger.debug("invoiceNum: " + invoiceNum);

					if (invoiceNum == null || "".equals(invoiceNum)) {
						Map<String, String> map = new HashMap<String, String>();
						map.put("invoice_no", "");
						map.put("message", "發票字軌用罄，請洽系統管理員");
						list.add(map);
						resp.getWriter().write(new Gson().toJson(list));
						return;
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
						invManualVO = new InvManualVO();
						invManualVO.setGroup_id(groupId);
						invManualVO.setInvoice_no(invoiceNum);
						invManualVO.setInv_manual_id(inv_manual_id);
						invManualVO.setInv_flag(1);
						service.updateInvManualInvFlag(invManualVO);
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
					invManualVO = new InvManualVO();
					invManualVO.setGroup_id(groupId);
					invManualVO.setInvoice_no("");
					invManualVO.setInv_manual_id(inv_manual_id);
					invManualVO.setInv_flag(0);
					service.updateInvManualInvFlag(invManualVO);
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
		}
	}

	class InvManualService {
		private InvManual_interface dao;

		public InvManualService() {
			dao = new InvManualDao();
		}

		public List<InvManualDetailVO> searchInvManualDetailByInvManualId(InvManualDetailVO invManualDetailVO) {
			return dao.searchInvManualDetailByInvManualId(invManualDetailVO);
		}

		public List<InvManualVO> searchAllInvManual(String groupId) {
			return dao.searchAllInvManual(groupId);
		}

		public List<InvManualVO> searchInvManualByInvoiceDate(String groupId, String startDate, String endDate) {
			return dao.searchInvManualByInvoiceDate(groupId, startDate, endDate);
		}

		public void insertInvManual(InvManualVO invManualVO) {
			dao.insertInvManual(invManualVO);
		}

		public void insertInvManualDetail(InvManualDetailVO invManualDetailVO) {
			dao.insertInvManualDetail(invManualDetailVO);
		}

		public void delInvManualDetail(InvManualDetailVO invManualDetailVO) {
			dao.delInvManualDetail(invManualDetailVO);
		}

		public void delInvManual(InvManualVO invManualVO) {
			dao.delInvManual(invManualVO);
		}

		public void updateInvManual(InvManualVO invManualVO) {
			dao.updateInvManual(invManualVO);
		}

		public void updateInvManualDetail(InvManualDetailVO invManualDetailVO) {
			dao.updateInvManualDetail(invManualDetailVO);
		}

		public InvManualVO selectInvManualByInvManualId(String groupId, String inv_manual_id) {
			return dao.selectInvManualByInvManualId(groupId, inv_manual_id);
		}

		public GroupVO getGroupInvoiceInfo(String groupId) {
			return dao.getGroupInvoiceInfo(groupId);
		}

		public InvoiceTrackVO getInvoiceNum(String groupId, Date invoice_date) {
			return dao.getInvoiceNum(groupId, invoice_date);
		}

		public void updateInvManualInvFlag(InvManualVO invManualVO) {
			dao.updateInvManualInvFlag(invManualVO);
		}

		public List<InvBuyerVO> getInvBuyerData(InvBuyerVO invBuyerVO) {
			return dao.getInvBuyerData(invBuyerVO);
		}
	}

	class InvManualDao implements InvManual_interface {
		private final String jdbcDriver = getServletConfig().getServletContext().getInitParameter("jdbcDriver");
		private final String dbURL = getServletConfig().getServletContext().getInitParameter("dbURL")
				+ "?useUnicode=true&characterEncoding=utf-8&useSSL=false";
		private final String dbUserName = getServletConfig().getServletContext().getInitParameter("dbUserName");
		private final String dbPassword = getServletConfig().getServletContext().getInitParameter("dbPassword");

		private static final String sp_select_all_inv_manual = "call sp_select_all_inv_manual (?)";
		private static final String sp_select_inv_manual_by_invoice_date = "call sp_select_inv_manual_by_invoice_date (?,?,?)";
		private static final String sp_insert_inv_manual = "call sp_insert_inv_manual(?,?,?,?,?,?,?,?,?)";
		private static final String sp_select_inv_manual_detail_by_inv_manual_id = "call sp_select_inv_manual_detail_by_inv_manual_id(?,?)";
		private static final String sp_insert_inv_manual_detail = "call sp_insert_inv_manual_detail(?,?,?,?,?,?,?)";
		private static final String sp_del_inv_manual_detail = "call sp_del_inv_manual_detail(?,?,?)";
		private static final String sp_del_inv_manual = "call sp_del_inv_manual(?,?)";
		private static final String sp_update_inv_manual = "call sp_update_inv_manual(?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
		private static final String sp_update_inv_manual_detail = "call sp_update_inv_manual_detail(?,?,?,?,?,?,?,?)";
		private static final String sp_get_invoiceNum = "call sp_get_invoiceNum(?,?)";
		private static final String sp_select_inv_manual_by_inv_manual_id = "call sp_select_inv_manual_by_inv_manual_id(?,?)";
		private static final String sp_get_group_invoice_info = "call sp_get_group_invoice_info(?)";
		private static final String sp_update_inv_manual_inv_flag = "call sp_update_inv_manual_inv_flag(?,?,?,?)";
		private static final String sp_select_inv_buyer_by_unicode_or_title = "call sp_select_inv_buyer_by_unicode_or_title (?,?,?)";

		@Override
		public List<InvManualVO> searchAllInvManual(String groupId) {
			List<InvManualVO> rows = new ArrayList<InvManualVO>();
			InvManualVO row = null;

			Connection con = null;
			PreparedStatement pstmt = null;
			ResultSet rs = null;
			try {
				Class.forName(jdbcDriver);
				con = DriverManager.getConnection(dbURL, dbUserName, dbPassword);
				pstmt = con.prepareStatement(sp_select_all_inv_manual);

				pstmt.setString(1, groupId);

				rs = pstmt.executeQuery();
				while (rs.next()) {
					row = new InvManualVO();
					row.setInv_manual_id(rs.getString("inv_manual_id"));
					row.setGroup_id(rs.getString("group_id"));
					row.setInvoice_type(rs.getString("invoice_type"));
					row.setYear_month(rs.getString("year_month"));
					row.setInvoice_no(rs.getString("invoice_no"));
					row.setInvoice_date(rs.getDate("invoice_date"));
					row.setTitle(rs.getString("title"));
					row.setUnicode(rs.getString("unicode"));
					row.setAddress(rs.getString("address"));
					row.setMemo(rs.getString("memo"));
					row.setAmount(rs.getInt("amount"));
					row.setTax_type(rs.getInt("tax_type"));
					row.setInv_flag(rs.getInt("inv_flag"));
					row.setAmount_plustax(rs.getInt("amount_plustax"));
					row.setTax(rs.getInt("tax"));
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
		public List<InvManualVO> searchInvManualByInvoiceDate(String groupId, String startDate, String endDate) {
			List<InvManualVO> rows = new ArrayList<InvManualVO>();
			InvManualVO row = null;

			Connection con = null;
			PreparedStatement pstmt = null;
			ResultSet rs = null;
			try {
				Class.forName(jdbcDriver);
				con = DriverManager.getConnection(dbURL, dbUserName, dbPassword);
				pstmt = con.prepareStatement(sp_select_inv_manual_by_invoice_date);

				pstmt.setString(1, groupId);
				pstmt.setString(2, startDate);
				pstmt.setString(3, endDate);

				rs = pstmt.executeQuery();
				while (rs.next()) {
					row = new InvManualVO();
					row.setInv_manual_id(rs.getString("inv_manual_id"));
					row.setGroup_id(rs.getString("group_id"));
					row.setInvoice_type(rs.getString("invoice_type"));
					row.setYear_month(rs.getString("year_month"));
					row.setInvoice_no(rs.getString("invoice_no"));
					row.setInvoice_date(rs.getDate("invoice_date"));
					row.setTitle(rs.getString("title"));
					row.setUnicode(rs.getString("unicode"));
					row.setAddress(rs.getString("address"));
					row.setMemo(rs.getString("memo"));
					row.setAmount(rs.getInt("amount"));
					row.setTax_type(rs.getInt("tax_type"));
					row.setInv_flag(rs.getInt("inv_flag"));
					row.setAmount_plustax(rs.getInt("amount_plustax"));
					row.setTax(rs.getInt("tax"));
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
		public void insertInvManual(InvManualVO invManualVO) {
			Connection con = null;
			CallableStatement cs = null;

			try {
				Class.forName(jdbcDriver);
				con = DriverManager.getConnection(dbURL, dbUserName, dbPassword);
				cs = con.prepareCall(sp_insert_inv_manual);

				cs.setString(1, invManualVO.getGroup_id());
				cs.setString(2, invManualVO.getInvoice_type());
				cs.setString(3, invManualVO.getYear_month());
				cs.setDate(4, invManualVO.getInvoice_date());
				cs.setString(5, invManualVO.getTitle());
				cs.setString(6, invManualVO.getUnicode());
				cs.setString(7, invManualVO.getAddress());
				cs.setString(8, invManualVO.getMemo());
				cs.setInt(9, invManualVO.getTax_type());

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
		public List<InvManualDetailVO> searchInvManualDetailByInvManualId(InvManualDetailVO invManualDetailVO) {
			List<InvManualDetailVO> rows = new ArrayList<InvManualDetailVO>();
			InvManualDetailVO row = null;

			Connection con = null;
			PreparedStatement pstmt = null;
			ResultSet rs = null;
			try {
				Class.forName(jdbcDriver);
				con = DriverManager.getConnection(dbURL, dbUserName, dbPassword);
				pstmt = con.prepareStatement(sp_select_inv_manual_detail_by_inv_manual_id);

				pstmt.setString(1, invManualDetailVO.getGroup_id());
				pstmt.setString(2, invManualDetailVO.getInv_manual_id());

				rs = pstmt.executeQuery();
				while (rs.next()) {
					row = new InvManualDetailVO();
					row.setInv_manual_detail_id(rs.getString("inv_manual_detail_id"));
					row.setInv_manual_id(rs.getString("inv_manual_id"));
					row.setDescription(rs.getString("description"));
					row.setQuantity(rs.getInt("quantity"));
					row.setPrice(rs.getInt("price"));
					row.setSubtotal(rs.getInt("subtotal"));
					row.setInv_flag(rs.getInt("inv_flag"));
					row.setMemo(rs.getString("memo"));
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
		public void insertInvManualDetail(InvManualDetailVO invManualDetailVO) {
			Connection con = null;
			CallableStatement cs = null;

			try {
				Class.forName(jdbcDriver);
				con = DriverManager.getConnection(dbURL, dbUserName, dbPassword);
				cs = con.prepareCall(sp_insert_inv_manual_detail);

				cs.setString(1, invManualDetailVO.getInv_manual_id());
				cs.setString(2, invManualDetailVO.getGroup_id());
				cs.setString(3, invManualDetailVO.getDescription());
				cs.setInt(4, invManualDetailVO.getPrice());
				cs.setInt(5, invManualDetailVO.getQuantity());
				cs.setInt(6, invManualDetailVO.getSubtotal());
				cs.setString(7, invManualDetailVO.getMemo());

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
		public void delInvManualDetail(InvManualDetailVO invManualDetailVO) {
			Connection con = null;
			PreparedStatement pstmt = null;
			try {
				Class.forName(jdbcDriver);
				con = DriverManager.getConnection(dbURL, dbUserName, dbPassword);
				pstmt = con.prepareStatement(sp_del_inv_manual_detail);
				pstmt.setString(1, invManualDetailVO.getInv_manual_detail_id());
				pstmt.setString(2, invManualDetailVO.getInv_manual_id());
				pstmt.setString(3, invManualDetailVO.getGroup_id());

				pstmt.executeUpdate();
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
		}

		@Override
		public void delInvManual(InvManualVO invManualVO) {
			Connection con = null;
			PreparedStatement pstmt = null;
			try {
				Class.forName(jdbcDriver);
				con = DriverManager.getConnection(dbURL, dbUserName, dbPassword);
				pstmt = con.prepareStatement(sp_del_inv_manual);
				pstmt.setString(1, invManualVO.getInv_manual_id());
				pstmt.setString(2, invManualVO.getGroup_id());

				pstmt.executeUpdate();
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
		}

		@Override
		public void updateInvManual(InvManualVO invManualVO) {
			Connection con = null;
			CallableStatement cs = null;

			try {
				Class.forName(jdbcDriver);
				con = DriverManager.getConnection(dbURL, dbUserName, dbPassword);
				cs = con.prepareCall(sp_update_inv_manual);

				cs.setString(1, invManualVO.getInv_manual_id());
				cs.setString(2, invManualVO.getGroup_id());
				cs.setString(3, invManualVO.getInvoice_type());
				cs.setString(4, invManualVO.getYear_month());
				cs.setString(5, invManualVO.getInvoice_no());
				cs.setDate(6, invManualVO.getInvoice_date());
				cs.setString(7, invManualVO.getTitle());
				cs.setString(8, invManualVO.getUnicode());
				cs.setString(9, invManualVO.getAddress());
				cs.setString(10, invManualVO.getMemo());
				cs.setInt(11, invManualVO.getTax_type());
				cs.setInt(12, invManualVO.getAmount());
				cs.setInt(13, invManualVO.getAmount_plustax());
				cs.setInt(14, invManualVO.getTax());

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
		public void updateInvManualDetail(InvManualDetailVO invManualDetailVO) {
			Connection con = null;
			CallableStatement cs = null;

			try {
				Class.forName(jdbcDriver);
				con = DriverManager.getConnection(dbURL, dbUserName, dbPassword);
				cs = con.prepareCall(sp_update_inv_manual_detail);

				cs.setString(1, invManualDetailVO.getInv_manual_detail_id());
				cs.setString(2, invManualDetailVO.getInv_manual_id());
				cs.setString(3, invManualDetailVO.getGroup_id());
				cs.setString(4, invManualDetailVO.getDescription());
				cs.setInt(5, invManualDetailVO.getPrice());
				cs.setInt(6, invManualDetailVO.getQuantity());
				cs.setInt(7, invManualDetailVO.getSubtotal());
				cs.setString(8, invManualDetailVO.getMemo());

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
		public InvoiceTrackVO getInvoiceNum(String groupId, Date invoice_date) {
			Connection con = null;
			PreparedStatement pstmt = null;
			ResultSet rs = null;
			String InvoiceNum = null;
			InvoiceTrackVO invoiceTrackVO = new InvoiceTrackVO();

			try {
				Class.forName(jdbcDriver);
				con = DriverManager.getConnection(dbURL, dbUserName, dbPassword);
				pstmt = con.prepareStatement(sp_get_invoiceNum);
				pstmt.setString(1, groupId);
				pstmt.setDate(2, invoice_date);
				pstmt.execute();
				rs = pstmt.getResultSet();

				if (rs.next()) {

					InvoiceNum = rs.getString("invoiceNum");

					if (InvoiceNum != null) {
						invoiceTrackVO.setGroup_id(groupId);
						invoiceTrackVO.setInvoice_beginno(rs.getString("invoice_beginno"));
						invoiceTrackVO.setInvoice_endno(rs.getString("invoice_endno"));
						invoiceTrackVO.setInvoice_id(rs.getString("invoice_id"));
						invoiceTrackVO.setInvoice_track(rs.getString("invoice_track"));
						invoiceTrackVO.setInvoice_type(rs.getString("invoice_type"));
						invoiceTrackVO.setInvoiceNum(InvoiceNum);
						invoiceTrackVO.setSeq(rs.getString("seq"));
						invoiceTrackVO.setYear_month(rs.getString("year_month"));

					}
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
			return invoiceTrackVO;
		}

		@Override
		public InvManualVO selectInvManualByInvManualId(String groupId, String inv_manual_id) {
			InvManualVO row = null;

			Connection con = null;
			PreparedStatement pstmt = null;
			ResultSet rs = null;
			try {
				Class.forName(jdbcDriver);
				con = DriverManager.getConnection(dbURL, dbUserName, dbPassword);
				pstmt = con.prepareStatement(sp_select_inv_manual_by_inv_manual_id);

				pstmt.setString(1, groupId);
				pstmt.setString(2, inv_manual_id);

				rs = pstmt.executeQuery();
				while (rs.next()) {
					row = new InvManualVO();
					row.setInv_manual_id(rs.getString("inv_manual_id"));
					row.setGroup_id(rs.getString("group_id"));
					row.setInvoice_type(rs.getString("invoice_type"));
					row.setYear_month(rs.getString("year_month"));
					row.setInvoice_no(rs.getString("invoice_no"));
					row.setInvoice_date(rs.getDate("invoice_date"));
					row.setTitle(rs.getString("title"));
					row.setUnicode(rs.getString("unicode"));
					row.setAmount(rs.getInt("amount"));
					row.setInv_flag(rs.getInt("inv_flag"));
					row.setTax_type(rs.getInt("tax_type"));
					row.setAddress(rs.getString("address"));
					row.setMemo(rs.getString("memo"));
					row.setTax(rs.getInt("tax"));
					row.setAmount_plustax(rs.getInt("amount_plustax"));
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
		public GroupVO getGroupInvoiceInfo(String groupId) {
			GroupVO groupVO = null;

			Connection con = null;
			PreparedStatement pstmt = null;
			ResultSet rs = null;
			try {
				Class.forName(jdbcDriver);
				con = DriverManager.getConnection(dbURL, dbUserName, dbPassword);
				pstmt = con.prepareStatement(sp_get_group_invoice_info);
				pstmt.setString(1, groupId);
				rs = pstmt.executeQuery();
				while (rs.next()) {
					groupVO = new GroupVO();
					groupVO.setGroup_unicode(rs.getString("group_unicode"));
					groupVO.setInvoice_key(rs.getString("invoice_key"));
					groupVO.setInvoice_posno(rs.getString("invoice_posno"));
					groupVO.setGroup_name(rs.getString("group_name"));
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
			return groupVO;
		}

		@Override
		public void updateInvManualInvFlag(InvManualVO invManualVO) {
			Connection con = null;
			CallableStatement cs = null;

			try {
				Class.forName(jdbcDriver);
				con = DriverManager.getConnection(dbURL, dbUserName, dbPassword);
				cs = con.prepareCall(sp_update_inv_manual_inv_flag);

				cs.setString(1, invManualVO.getGroup_id());
				cs.setString(2, invManualVO.getInvoice_no());
				cs.setInt(3, invManualVO.getInv_flag());
				cs.setString(4, invManualVO.getInv_manual_id());
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
		public List<InvBuyerVO> getInvBuyerData(InvBuyerVO invBuyerVO) {
			List<InvBuyerVO> rows = new ArrayList<InvBuyerVO>();
			InvBuyerVO row = null;

			Connection con = null;
			PreparedStatement pstmt = null;
			ResultSet rs = null;
			try {
				Class.forName(jdbcDriver);
				con = DriverManager.getConnection(dbURL, dbUserName, dbPassword);
				pstmt = con.prepareStatement(sp_select_inv_buyer_by_unicode_or_title);

				pstmt.setString(1, invBuyerVO.getGroup_id());
				pstmt.setString(2, invBuyerVO.getUnicode());
				pstmt.setString(3, invBuyerVO.getTitle());

				rs = pstmt.executeQuery();
				while (rs.next()) {
					row = new InvBuyerVO();
					row.setInv_buyer_id(rs.getString("inv_buyer_id"));
					row.setGroup_id(rs.getString("group_id"));
					row.setTitle(rs.getString("title"));
					row.setUnicode(rs.getString("unicode"));
					row.setAddress(rs.getString("address"));
					row.setMemo(rs.getString("memo"));
					row.setCreate_time(rs.getDate("create_time"));
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
}

interface InvManual_interface {
	public List<InvManualVO> searchAllInvManual(String groupId);

	public List<InvManualDetailVO> searchInvManualDetailByInvManualId(InvManualDetailVO invManualDetailVO);

	public List<InvManualVO> searchInvManualByInvoiceDate(String groupId, String startDate, String endDate);

	public void insertInvManual(InvManualVO invManualVO);

	public void insertInvManualDetail(InvManualDetailVO invManualDetailVO);

	public void delInvManualDetail(InvManualDetailVO invManualDetailVO);

	public void delInvManual(InvManualVO invManualVO);

	public void updateInvManual(InvManualVO invManualVO);

	public void updateInvManualDetail(InvManualDetailVO invManualDetailVO);

	public InvoiceTrackVO getInvoiceNum(String groupId, Date invoice_date);

	public InvManualVO selectInvManualByInvManualId(String groupId, String inv_manual_id);

	public GroupVO getGroupInvoiceInfo(String groupId);

	public void updateInvManualInvFlag(InvManualVO invManualVO);

	public List<InvBuyerVO> getInvBuyerData(InvBuyerVO invBuyerVO);
}
