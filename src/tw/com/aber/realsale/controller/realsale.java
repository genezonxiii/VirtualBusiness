package tw.com.aber.realsale.controller;

import java.io.IOException;
import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.JSONObject;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import tw.com.aber.vo.CustomerVO;
import tw.com.aber.vo.RealSaleDetailVO;
import tw.com.aber.vo.RealSaleVO;
import tw.com.aber.service.RealSaleService;

public class realsale extends HttpServlet {
	private static final long serialVersionUID = 1L;

	private static final Logger logger = LogManager.getLogger(realsale.class);

	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		request.setCharacterEncoding("UTF-8");
		response.setCharacterEncoding("UTF-8");

		String group_id = request.getSession().getAttribute("group_id").toString();
		String user_id = request.getSession().getAttribute("user_id").toString();

		RealSaleService realsaleService = new RealSaleService();
		List<RealSaleVO> realsaleList = null;
		Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd").create();
		String action = request.getParameter("action");
		try {
			if ("search".equals(action)) {
				String c_order_no_begin = request.getParameter("order_no_begin");
				String c_order_no_end = request.getParameter("order_no_end");
				String c_trans_list_date_begin = request.getParameter("trans_list_date_begin");
				String c_trans_list_date_end = request.getParameter("trans_list_date_end");
				String c_dis_date_begin = request.getParameter("dis_date_begin");
				String c_dis_date_end = request.getParameter("dis_date_end");
				String c_order_source = request.getParameter("order_source");
				String c_deliveryway = request.getParameter("deliveryway");
				String c_customerid = request.getParameter("customerid");
				String c_upload_date_begin = request.getParameter("upload_date_begin");
				String c_upload_date_end = request.getParameter("upload_date_end");
				
				realsaleList = realsaleService.getSearchMuliDB(group_id, c_order_no_begin, c_order_no_end,c_customerid,c_trans_list_date_begin,c_trans_list_date_end,c_dis_date_begin,c_dis_date_end,c_order_source,c_deliveryway, c_upload_date_begin, c_upload_date_end);
				String jsonStrList = gson.toJson(realsaleList);
				logger.info(jsonStrList);
				response.getWriter().write(jsonStrList);
			} else if ("getRealSaleDetail".equals(action)) {
			 String realsale_id = request.getParameter("realsale_id");
			
			 logger.debug("realsale_id:".concat(realsale_id));
			
			 RealSaleDetailVO realsaleDetailVO= new RealSaleDetailVO();
			 realsaleDetailVO.setRealsale_id(realsale_id);			
			 realsaleDetailVO.setGroup_id(group_id);
			 
			 List<RealSaleDetailVO> realsaleDetailList = realsaleService.getRealSaleDetail(realsaleDetailVO);			
			 String jsonStr = gson.toJson(realsaleDetailList);		
			 response.getWriter().write(jsonStr);
			
			 logger.debug("result".concat(jsonStr));
			 
			}else if ("insert".equals(action)) {
				String order_no = request.getParameter("order_no");
				String order_source = request.getParameter("order_source");
				String invoice = request.getParameter("invoice");
				Float total_amt = Float.valueOf(request.getParameter("total_amt"));
				String memo = request.getParameter("memo");
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
				String customer_id = request.getParameter("customer_id");

				RealSaleVO realSaleVO = new RealSaleVO();
				String seq_no;
				List<RealSaleVO> saleSeqNoList = realsaleService.getSaleSeqNo(group_id);
				if (saleSeqNoList.size() == 0) {
					seq_no = getThisYearMonthDate() + "0001";
				} else {
					seq_no = getGenerateSeqNo(saleSeqNoList.get(0).getSeq_no());
				}

				if (order_no.length() < 1) {
					order_no = seq_no;
				}
				realSaleVO.setSeq_no(seq_no);
				realSaleVO.setGroup_id(group_id);
				realSaleVO.setUser_id(user_id);
				realSaleVO.setOrder_no(order_no);
				realSaleVO.setOrder_source(order_source);
				realSaleVO.setCustomer_id(customer_id);
				realSaleVO.setInvoice(invoice);
				realSaleVO.setTotal_amt(total_amt);
				realSaleVO.setMemo(memo);
				realSaleVO.setInvoice_date(invoice_date);
				realSaleVO.setTrans_list_date(trans_list_date);
				realSaleVO.setDis_date(dis_date);
				realSaleVO.setSale_date(sale_date);

				logger.debug("seq_no:".concat(seq_no));
				logger.debug("group_id:".concat(group_id));
				logger.debug("user_id:".concat(user_id));
				logger.debug("order_no:".concat(order_no));
				logger.debug("order_source:".concat(order_source));
				logger.debug("customer_id:".concat(customer_id));
				logger.debug("invoice:".concat(invoice));
				logger.debug("price:".concat(total_amt.toString()));
				logger.debug("memo:".concat(memo));
				logger.debug("invoice_date:".concat(invoice_date == null ? "" : invoice_date.toString()));
				logger.debug("trans_list_date:".concat(trans_list_date == null ? "" : trans_list_date.toString()));
				logger.debug("dis_date:".concat(dis_date == null ? "" : dis_date.toString()));
				logger.debug("sale_date:".concat(sale_date == null ? "" : sale_date.toString()));

				realsaleService.addRealSale(realSaleVO);
				realsaleList = realsaleService.getSearchAllDB(group_id);
				String jsonStrList = gson.toJson(realsaleList);
				response.getWriter().write(jsonStrList);
			} else if ("search_custom_data".equals(action)) {
				String term = request.getParameter("term");
				String identity = request.getParameter("identity");

				logger.debug("term:".concat(term));
				logger.debug("identity:".concat(identity));

				List<CustomerVO> customerList = null;
				if ("NAME".equals(identity)) {
					customerList = realsaleService.getSearchCustomerByName(group_id, term);
				}

				String jsonStrList = gson.toJson(customerList);			
				response.getWriter().write(jsonStrList);
			} else if ("update".equals(action)) {
				String realsale_id = request.getParameter("realsale_id");				
				String order_no = request.getParameter("order_no");
				String order_source = request.getParameter("order_source");
				String invoice = request.getParameter("invoice");
				Float total_amt = Float.valueOf(request.getParameter("total_amt"));
				String memo = request.getParameter("memo");
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
				String customer_id = request.getParameter("customer_id");

				RealSaleVO realSaleVO = new RealSaleVO();
				realSaleVO.setRealsale_id(realsale_id);
				realSaleVO.setGroup_id(group_id);
				realSaleVO.setUser_id(user_id);
				realSaleVO.setOrder_no(order_no);
				realSaleVO.setOrder_source(order_source);
				realSaleVO.setCustomer_id(customer_id);
				realSaleVO.setInvoice(invoice);
				realSaleVO.setTotal_amt(total_amt);
				realSaleVO.setMemo(memo);
				realSaleVO.setInvoice_date(invoice_date);
				realSaleVO.setTrans_list_date(trans_list_date);
				realSaleVO.setDis_date(dis_date);
				realSaleVO.setSale_date(sale_date);
				
				realsaleService.updateRealSale(realSaleVO);
				realsaleList = realsaleService.getSearchAllDB(group_id);
				String jsonStrList = gson.toJson(realsaleList);
				response.getWriter().write(jsonStrList);
			} else if ("delete".equals(action)) {
				String realsale_id = request.getParameter("realsale_id");
				realsaleService.deleteRealSale(realsale_id,user_id,group_id);
				realsaleList = realsaleService.getSearchAllDB(group_id);
				String jsonStrList = gson.toJson(realsaleList);
				response.getWriter().write(jsonStrList);
			} else if ("importData".equals(action)) {
				//銷貨
				String c_import_trans_list_date_begin = request.getParameter("import_trans_list_date_begin");
				String c_import_trans_list_date_end = request.getParameter("import_trans_list_date_end");				
				JSONObject jsonObject = realsaleService.importRealSale(group_id, user_id, c_import_trans_list_date_begin,c_import_trans_list_date_end);
				logger.info(jsonObject.toString());
				response.getWriter().write(jsonObject.toString());
			} else if ("importallocinvData".equals(action)) {
				//配庫
				JSONObject jsonObject = realsaleService.importAllocInv(group_id, user_id);
				logger.info(jsonObject.toString());
				response.getWriter().write(jsonObject.toString());
			}			
			
		} catch (Exception e) {
			logger.error("Exception:".concat(e.getMessage()));
		}
	}

	/*************************** 自訂方法 ****************************************/
	/**
	 * <p>獲得格式過的今年今月今日
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

	/**
	 * <p> 格式化流水號
	 * 
	 * @param str
	 * @return
	 */
	public String getGenerateSeqNo(String str) {
		str = str.substring(str.length() - 4);
		return getThisYearMonthDate() + formatSeqNo((Integer.valueOf(str) + 1));
	}
}
