package tw.com.aber.realsale.controller;

import java.io.IOException;
import java.sql.Connection;
import java.sql.Date;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import tw.com.aber.util.Util;
import tw.com.aber.vo.ProductVO;
import tw.com.aber.vo.SaleDetailVO;
import tw.com.aber.vo.SaleVO;

public class realsale extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private static final Logger logger = LogManager.getLogger(realsale.class);
	
	private Util util;

	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		request.setCharacterEncoding("UTF-8");
		response.setCharacterEncoding("UTF-8");
		SaleService saleService = null;
		String action = request.getParameter("action");
		String group_id = request.getSession().getAttribute("group_id").toString();
		String user_id = request.getSession().getAttribute("user_id").toString();
		if ("search".equals(action)) {
			try {
				/***************************
				 * 1.接收請求參數-格式檢查
				 ****************************************/
				String c_product_id = request.getParameter("c_product_id");
				/***************************
				 * 2.開始查詢資料
				 ****************************************/
				// 假如無查詢條件，則是查詢全部
				if (c_product_id == null || (c_product_id.trim()).length() == 0) {
					saleService = new SaleService();
					List<SaleVO> list = saleService.getSearchAllDB(group_id);
					
					SaleVO saleVO = new SaleVO();
					saleVO.setMessage("驗證通過");
					list.add(saleVO);
					Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd").create();
					String jsonStrList = gson.toJson(list);
					response.getWriter().write(jsonStrList);
					return;// 程式中斷
				}
				// 查詢指定ID
				if (c_product_id != null || c_product_id.trim().length() != 0) {
					saleService = new SaleService();
					List<SaleVO> list = saleService.getSearchDB(group_id, c_product_id);
					SaleVO saleVO = new SaleVO();
					saleVO.setMessage("驗證通過");
					list.add(saleVO);
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
		if ("search_trans_list_date".equals(action)) {
			try {
				String start_date = request.getParameter("trans_list_start_date");
				String end_date = request.getParameter("trans_list_end_date");
				if (start_date.trim().length() != 0 & end_date.trim().length() == 0) {
					List<SaleVO> list = new ArrayList<SaleVO>();
					SaleVO saleVO = new SaleVO();
					saleVO.setMessage("如要以日期查詢，請完整填寫訖日欄位");
					list.add(saleVO);
					Gson gson = new Gson();
					String jsonStrList = gson.toJson(list);
					response.getWriter().write(jsonStrList);
					return;// 程式中斷
				}
				if (end_date.trim().length() != 0 & start_date.trim().length() == 0) {
					List<SaleVO> list = new ArrayList<SaleVO>();
					SaleVO saleVO = new SaleVO();
					saleVO.setMessage("如要以日期查詢，請完整填寫起日欄位");
					list.add(saleVO);
					Gson gson = new Gson();
					String jsonStrList = gson.toJson(list);
					response.getWriter().write(jsonStrList);
					return;// 程式中斷
				}
				if (start_date.trim().length() != 0 & end_date.trim().length() != 0) {
					if (DateConversionToDigital(start_date) > DateConversionToDigital(end_date)) {
						List<SaleVO> list = new ArrayList<SaleVO>();
						SaleVO saleVO = new SaleVO();
						saleVO.setMessage("起日不可大於訖日");
						list.add(saleVO);
						Gson gson = new Gson();
						String jsonStrList = gson.toJson(list);
						response.getWriter().write(jsonStrList);
						return;// 程式中斷
					} else {
						// 查詢指定期限
						saleService = new SaleService();
						List<SaleVO> list = saleService.getSearchTransListDateDB(group_id, start_date, end_date);
						SaleVO saleVO = new SaleVO();
						saleVO.setMessage("驗證通過");
						list.add(saleVO);
						Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd").create();
						String jsonStrList = gson.toJson(list);
						response.getWriter().write(jsonStrList);
						return;// 程式中斷
					}
				}
				// 假如無查詢條件，則是查詢全部
				if (start_date.trim().length() == 0 & end_date.trim().length() == 0) {
					saleService = new SaleService();
					List<SaleVO> list = saleService.getSearchAllDB(group_id);
					SaleVO saleVO = new SaleVO();
					saleVO.setMessage("驗證通過");
					list.add(saleVO);
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
		if ("search_dis_date".equals(action)) {
			try {
				/***************************
				 * 1.接收請求參數-格式檢查
				 ****************************************/
				String start_date = request.getParameter("dis_start_date");
				String end_date = request.getParameter("dis_end_date");
				if (start_date.trim().length() != 0 & end_date.trim().length() == 0) {
					List<SaleVO> list = new ArrayList<SaleVO>();
					SaleVO saleVO = new SaleVO();
					saleVO.setMessage("如要以日期查詢，請完整填寫訖日欄位");
					list.add(saleVO);
					Gson gson = new Gson();
					String jsonStrList = gson.toJson(list);
					response.getWriter().write(jsonStrList);
					return;// 程式中斷
				}
				if (end_date.trim().length() != 0 & start_date.trim().length() == 0) {
					List<SaleVO> list = new ArrayList<SaleVO>();
					SaleVO saleVO = new SaleVO();
					saleVO.setMessage("如要以日期查詢，請完整填寫起日欄位");
					list.add(saleVO);
					Gson gson = new Gson();
					String jsonStrList = gson.toJson(list);
					response.getWriter().write(jsonStrList);
					return;// 程式中斷
				}
				if (start_date.trim().length() != 0 & end_date.trim().length() != 0) {
					if (DateConversionToDigital(start_date) > DateConversionToDigital(end_date)) {
						List<SaleVO> list = new ArrayList<SaleVO>();
						SaleVO saleVO = new SaleVO();
						saleVO.setMessage("起日不可大於訖日");
						list.add(saleVO);
						Gson gson = new Gson();
						String jsonStrList = gson.toJson(list);
						response.getWriter().write(jsonStrList);
						return;// 程式中斷
					} else {
						// 查詢指定期限
						saleService = new SaleService();
						List<SaleVO> list = saleService.getSearchDisDateDB(group_id, start_date, end_date);
						SaleVO saleVO = new SaleVO();
						saleVO.setMessage("驗證通過");
						list.add(saleVO);
						Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd").create();
						String jsonStrList = gson.toJson(list);
						response.getWriter().write(jsonStrList);
						return;// 程式中斷
					}
				}
				/***************************
				 * 2.開始查詢資料
				 ****************************************/
				// 假如無查詢條件，則是查詢全部
				if (start_date.trim().length() == 0 & end_date.trim().length() == 0) {
					saleService = new SaleService();
					List<SaleVO> list = saleService.getSearchAllDB(group_id);
					SaleVO saleVO = new SaleVO();
					saleVO.setMessage("驗證通過");
					list.add(saleVO);
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
		// 處理auto complete
		if ("search_product_data".equals(action)) {
			String term = request.getParameter("term");
			String identity = request.getParameter("identity");
			if ("ID".equals(identity)) {
				saleService = new SaleService();
				List<ProductVO> list = saleService.getSearchProductById(group_id, term);
				Gson gson = new Gson();
				String jsonStrList = gson.toJson(list);
				response.getWriter().write(jsonStrList);
				return;// 程式中斷
			}
			if ("NAME".equals(identity)) {
				saleService = new SaleService();
				List<ProductVO> list = saleService.getSearchProductByName(group_id, term);
				Gson gson = new Gson();
				String jsonStrList = gson.toJson(list);
				response.getWriter().write(jsonStrList);
				return;// 程式中斷
			}
		}
		if ("insert".equals(action)) {
			try {
				/***************************
				 * 1.接收請求參數
				 **************************************/
				String invoice = request.getParameter("invoice");
				String order_no = request.getParameter("order_no");
				String cus_id = request.getParameter("cus_id");
				String name = request.getParameter("name");
				// product相關參數
				String product_id = request.getParameter("product_id");
				String product_name = request.getParameter("product_name");
				String c_product_id = request.getParameter("c_product_id");
				// 數值形態處理，這裡會先宣告預設值是根據DB預設值情況而設
				Integer quantity = 0;
				try {
					quantity = 0;
					String quantityStr = request.getParameter("quantity");
					quantity = Integer.valueOf(quantityStr);
				} catch (Exception e1) {

					e1.printStackTrace();
				}

				Float price = null;
				try {
					String priceStr = request.getParameter("price");
					price = Float.valueOf(priceStr);
				} catch (Exception e) {

					e.printStackTrace();
				}
				// 日期參數處理
				SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
				java.sql.Date invoice_date;
				String invoice_dateStr = request.getParameter("invoice_date");
				if (invoice_dateStr.length() != 0) {
					java.util.Date invoice_date_util = sdf.parse(invoice_dateStr);
					invoice_date = new java.sql.Date(invoice_date_util.getTime());
				} else {
					invoice_date = null;
				}

				java.sql.Date trans_list_date;
				String trans_list_dateStr = request.getParameter("trans_list_date");
				if (trans_list_dateStr.length() != 0) {
					java.util.Date trans_list_date_util = sdf.parse(trans_list_dateStr);
					trans_list_date = new java.sql.Date(trans_list_date_util.getTime());
				} else {
					trans_list_date = null;
				}

				java.sql.Date dis_date;
				String dis_dateStr = request.getParameter("dis_date");
				if (dis_dateStr.length() != 0) {
					java.util.Date dis_date_util = sdf.parse(dis_dateStr);
					dis_date = new java.sql.Date(dis_date_util.getTime());
				} else {
					dis_date = null;
				}

				String memo = request.getParameter("memo");

				String sale_dateStr = request.getParameter("sale_date");
				java.util.Date sale_date_util = sdf.parse(sale_dateStr);
				java.sql.Date sale_date = new java.sql.Date(sale_date_util.getTime());

				String order_source = request.getParameter("order_source");
				/***************************
				 * 2.處理銷貨單號
				 ***************************************/
				String seq_no;
				saleService = new SaleService();
				List<SaleVO> list = saleService.getSaleSeqNo(group_id);
				if (list.size() == 0) {
					seq_no = getThisYearMonthDate() + "0001";
				} else {
					seq_no = getGenerateSeqNo(list.get(0).getSeq_no());
				}
				saleService = new SaleService();
				if (order_no.length() < 1) {
					order_no = seq_no;
				}
				saleService.addSale(seq_no, group_id, order_no, user_id, product_id, product_name, c_product_id, cus_id,
						name, quantity, price, invoice, invoice_date, trans_list_date, dis_date, memo, sale_date,
						order_source);
				/***************************
				 * 3.新增完成,準備轉交(Send the Success view)
				 ***********/
				saleService = new SaleService();
				SaleVO saleVO = new SaleVO();
				saleVO.setMessage("驗證通過");
				List<SaleVO> salelist = saleService.getSearchDB(group_id, c_product_id);
				Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd").create();
				salelist.add(saleVO);
				String jsonStrList = gson.toJson(salelist);
				response.getWriter().write(jsonStrList);
				/*************************** 其他可能的錯誤處理 **********************************/
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		if ("delete".equals(action)) {
			try {
				/***************************
				 * 1.接收請求參數
				 ***************************************/
				String sale_id = request.getParameter("sale_id");
				String c_product_id = request.getParameter("c_product_id");
				/***************************
				 * 2.開始刪除資料
				 ***************************************/
				saleService = new SaleService();
				saleService.deleteSale(sale_id, user_id);
				/***************************
				 * 3.刪除完成,準備轉交(Send the Success view)
				 ***********/
				saleService = new SaleService();
				SaleVO saleVO = new SaleVO();
				saleVO.setMessage("驗證通過");
				List<SaleVO> salelist = saleService.getSearchDB(group_id, c_product_id);
				salelist.add(saleVO);
				Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd").create();
				String jsonStrList = gson.toJson(salelist);
				response.getWriter().write(jsonStrList);
				/*************************** 其他可能的錯誤處理 **********************************/
			} catch (Exception e) {

				e.printStackTrace();
			}
		}
		if ("update".equals(action)) {
			try {
				/***************************
				 * 1.接收請求參數
				 ***************************************/
				String sale_id = request.getParameter("sale_id");
				String seq_no = request.getParameter("seq_no");
				String invoice = request.getParameter("invoice");
				String order_no = request.getParameter("order_no");
				String name = request.getParameter("name");
				String cus_id = request.getParameter("cus_id");
				String order_source = request.getParameter("order_source");
				// product相關參數
				String product_id = request.getParameter("product_id");
				String product_name = request.getParameter("product_name");
				String c_product_id = request.getParameter("c_product_id");
				// 數值形態處理，這裡會先宣告預設值是根據DB預設值情況而設
				Integer quantity = 0;
				try {
					quantity = 0;
					String quantityStr = request.getParameter("quantity");
					quantity = Integer.valueOf(quantityStr);
				} catch (Exception e1) {

					e1.printStackTrace();
				}

				Float price = null;
				try {
					String priceStr = request.getParameter("price");
					price = Float.valueOf(priceStr);
				} catch (Exception e) {

					e.printStackTrace();
				}
				// 日期參數處理
				SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
				java.sql.Date invoice_date;
				String invoice_dateStr = request.getParameter("invoice_date");
				if (invoice_dateStr.length() != 0) {
					java.util.Date invoice_date_util = sdf.parse(invoice_dateStr);
					invoice_date = new java.sql.Date(invoice_date_util.getTime());
				} else {
					invoice_date = null;
				}

				java.sql.Date trans_list_date;
				String trans_list_dateStr = request.getParameter("trans_list_date");
				if (trans_list_dateStr.length() != 0) {
					java.util.Date trans_list_date_util = sdf.parse(trans_list_dateStr);
					trans_list_date = new java.sql.Date(trans_list_date_util.getTime());
				} else {
					trans_list_date = null;
				}

				java.sql.Date dis_date;
				String dis_dateStr = request.getParameter("dis_date");
				if (dis_dateStr.length() != 0) {
					java.util.Date dis_date_util = sdf.parse(dis_dateStr);
					dis_date = new java.sql.Date(dis_date_util.getTime());
				} else {
					dis_date = null;
				}

				String memo = request.getParameter("memo");

				String sale_dateStr = request.getParameter("sale_date");
				java.util.Date sale_date_util = sdf.parse(sale_dateStr);
				java.sql.Date sale_date = new java.sql.Date(sale_date_util.getTime());
				/***************************
				 * 2.開始修改資料
				 ***************************************/
				saleService = new SaleService();
				saleService.updatesale(sale_id, seq_no, group_id, order_no, user_id, product_id, product_name,
						c_product_id, cus_id, name, quantity, price, invoice, invoice_date, trans_list_date, dis_date,
						memo, sale_date, order_source);
				/***************************
				 * 3.修改完成,準備轉交(Send the Success view)
				 ***********/
				saleService = new SaleService();
				SaleVO saleVO = new SaleVO();
				saleVO.setMessage("驗證通過");
				List<SaleVO> salelist = saleService.getSearchDB(group_id, c_product_id);
				salelist.add(saleVO);
				Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd").create();
				String jsonStrList = gson.toJson(salelist);
				response.getWriter().write(jsonStrList);
				/*************************** 其他可能的錯誤處理 **********************************/
			} catch (Exception e) {

				e.printStackTrace();
			}
		}
		if ("getSaleDetail".equals(action)) {
			String sale_id = request.getParameter("sale_id");

			SaleDetailVO saleDetailVO = new SaleDetailVO();
			saleDetailVO.setSale_id(sale_id);

			saleService = new SaleService();

			Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd").create();

			List<SaleDetailVO> list = saleService.getSaleDetail(saleDetailVO);

			String jsonStr = gson.toJson(list);
			response.getWriter().write(jsonStr);

			String format = "getSaleDetail Strat\nsale_id: {}\nsearch result json:{}";

			logger.debug(format, sale_id, jsonStr);
			return;
		}
	}

	/*************************** 自訂方法 ****************************************/
	// 處理傳過來的日期格式
	public int DateConversionToDigital(String Date) {

		StringBuffer str = new StringBuffer();
		String[] dateArray = Date.split("-");
		for (String i : dateArray) {
			str.append(i);
		}
		return Integer.parseInt(str.toString());
	}

	// 獲得格式過的今年金月今日
	public String getThisYearMonthDate() {
		Calendar cal = Calendar.getInstance();
		String year = String.valueOf(cal.get(Calendar.YEAR));
		String month = String.valueOf(cal.get(Calendar.MONTH) + 1);
		String date = String.valueOf(cal.get(Calendar.DATE));
		return year + formatTime(month) + formatTime(date);
	}

	// 處理個位數月份以及日
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

	// 格式化單號，不足四位補位
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

	/*************************** 制定規章方法 ****************************************/
	interface sale_interface {

		public void insertDB(SaleVO SaleVO);

		public void updateDB(SaleVO SaleVO);

		public void deleteDB(String sale_id, String user_id);

		public List<SaleVO> getNewSaleSeqNo(String group_id);

		public List<ProductVO> getProductByName(String group_id, String product_name);

		public List<ProductVO> getProductById(String group_id, String c_product_id);

		public List<SaleVO> searchDB(String group_id, String c_product_id);

		public List<SaleVO> searchAllDB(String group_id);

		public List<SaleVO> searchTransListDateDB(String group_id, String trans_list_start_date,
				String trans_list_end_date);

		public List<SaleVO> searchDisDateDB(String group_id, String dis_start_date, String dis_end_date);

		public List<SaleDetailVO> getSaleDetail(SaleDetailVO saleDetailVO);

	}

	/*************************** 處理業務邏輯 ****************************************/
	class SaleService {
		private sale_interface dao;

		public SaleService() {
			dao = new SaleDAO();
		}

		public SaleVO addSale(String seq_no, String group_id, String order_no, String user_id, String product_id,
				String product_name, String c_product_id, String cus_id, String name, Integer quantity, Float price,
				String invoice, Date invoice_date, Date trans_list_date, Date dis_date, String memo, Date sale_date,
				String order_source) {
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
			dao.insertDB(saleVO);
			return saleVO;
		}

		public SaleVO updatesale(String sale_id, String seq_no, String group_id, String order_no, String user_id,
				String product_id, String product_name, String c_product_id, String cus_id, String name,
				Integer quantity, Float price, String invoice, Date invoice_date, Date trans_list_date, Date dis_date,
				String memo, Date sale_date, String order_source) {
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
			dao.updateDB(saleVO);
			return saleVO;
		}

		public List<SaleVO> getSaleSeqNo(String group_id) {
			return dao.getNewSaleSeqNo(group_id);
		}

		public void deleteSale(String sale_id, String user_id) {
			dao.deleteDB(sale_id, user_id);
		}

		public List<SaleVO> getSearchDB(String group_id, String c_product_id) {
			return dao.searchDB(group_id, c_product_id);
		}

		public List<SaleVO> getSearchAllDB(String group_id) {
			return dao.searchAllDB(group_id);
		}

		public List<SaleVO> getSearchTransListDateDB(String group_id, String trans_list_start_date,
				String trans_list_end_date) {
			return dao.searchTransListDateDB(group_id, trans_list_start_date, trans_list_end_date);
		}

		public List<SaleVO> getSearchDisDateDB(String group_id, String dis_start_date, String dis_end_date) {
			return dao.searchDisDateDB(group_id, dis_start_date, dis_end_date);
		}

		public List<ProductVO> getSearchProductById(String group_id, String c_product_id) {
			return dao.getProductById(group_id, c_product_id);
		}

		public List<ProductVO> getSearchProductByName(String group_id, String product_name) {
			return dao.getProductByName(group_id, product_name);
		}

		public List<SaleDetailVO> getSaleDetail(SaleDetailVO saleDetailVO) {
			return dao.getSaleDetail(saleDetailVO);
		}
	}

	/*************************** 操作資料庫 ****************************************/
	class SaleDAO implements sale_interface {
		// 會使用到的Stored procedure
		private static final String sp_get_sale_detail = "call sp_get_sale_detail (?)";
		//private static final String sp_selectall_sale = "call sp_selectall_sale (?)";
		private static final String sp_selectall_sale = "call sp_selectall_realsale (?)";
		private static final String sp_select_sale_bycproductid = "call sp_select_sale_bycproductid (?,?)";
		private static final String sp_select_sale_bytranslistdate = "call sp_select_sale_bytranslistdate(?,?,?)";
		private static final String sp_select_sale_bydisdate = "call sp_select_sale_bydisdate(?,?,?)";
		private static final String sp_get_sale_newseqno = "call sp_get_sale_seqno(?)";
		private static final String sp_insert_sale = "call sp_insert_sale(?, ?, ?, ?, ?, ?, ?,?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
		private static final String sp_del_sale = "call sp_del_sale (?,?)";
		private static final String sp_update_sale = "call sp_update_sale (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";

		private static final String sp_get_product_byid = "call sp_get_product_byid (?,?)";
		private static final String sp_get_product_byname = "call sp_get_product_byname (?,?)";

		private final String dbURL = getServletConfig().getServletContext().getInitParameter("dbURL")
				+ "?useUnicode=true&characterEncoding=utf-8&useSSL=false";
		private final String dbUserName = getServletConfig().getServletContext().getInitParameter("dbUserName");
		private final String dbPassword = getServletConfig().getServletContext().getInitParameter("dbPassword");

		@Override
		public void insertDB(SaleVO saleVO) {

			Connection con = null;
			PreparedStatement pstmt = null;
			try {
				Class.forName("com.mysql.jdbc.Driver");
				con = DriverManager.getConnection(dbURL, dbUserName, dbPassword);
				pstmt = con.prepareStatement(sp_insert_sale);

				pstmt.setString(1, saleVO.getSeq_no());
				pstmt.setString(2, saleVO.getGroup_id());
				pstmt.setString(3, saleVO.getOrder_no());
				pstmt.setString(4, saleVO.getUser_id());
				pstmt.setString(5, saleVO.getProduct_id());
				pstmt.setString(6, saleVO.getProduct_name());
				pstmt.setString(7, saleVO.getC_product_id());
				pstmt.setString(8, saleVO.getCustomer_id());
				pstmt.setString(9, null);// saleVO.getName());
				pstmt.setInt(10, saleVO.getQuantity());
				pstmt.setFloat(11, saleVO.getPrice());
				pstmt.setString(12, saleVO.getInvoice());
				pstmt.setDate(13, saleVO.getInvoice_date());
				pstmt.setDate(14, saleVO.getTrans_list_date());
				pstmt.setDate(15, saleVO.getDis_date());
				pstmt.setString(16, saleVO.getMemo());
				pstmt.setDate(17, saleVO.getSale_date());
				pstmt.setString(18, saleVO.getOrder_source());

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
		public void updateDB(SaleVO saleVO) {

			Connection con = null;
			PreparedStatement pstmt = null;
			try {
				Class.forName("com.mysql.jdbc.Driver");
				con = DriverManager.getConnection(dbURL, dbUserName, dbPassword);
				pstmt = con.prepareStatement(sp_update_sale);

				pstmt.setString(1, saleVO.getSale_id());
				pstmt.setString(2, saleVO.getSeq_no());
				pstmt.setString(3, saleVO.getGroup_id());
				pstmt.setString(4, saleVO.getOrder_no());
				pstmt.setString(5, saleVO.getUser_id());
				pstmt.setString(6, saleVO.getProduct_id());
				pstmt.setString(7, saleVO.getCustomer_id());
				pstmt.setString(8, saleVO.getProduct_name());
				pstmt.setString(9, saleVO.getC_product_id());
				pstmt.setString(10, null); // saleVO.getName());
				pstmt.setInt(11, saleVO.getQuantity());
				pstmt.setFloat(12, saleVO.getPrice());
				pstmt.setString(13, saleVO.getInvoice());
				pstmt.setDate(14, saleVO.getInvoice_date());
				pstmt.setDate(15, saleVO.getTrans_list_date());
				pstmt.setDate(16, saleVO.getDis_date());
				pstmt.setString(17, saleVO.getMemo());
				pstmt.setDate(18, saleVO.getSale_date());
				pstmt.setString(19, saleVO.getOrder_source());

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
		public void deleteDB(String sale_id, String user_id) {

			Connection con = null;
			PreparedStatement pstmt = null;
			try {
				Class.forName("com.mysql.jdbc.Driver");
				con = DriverManager.getConnection(dbURL, dbUserName, dbPassword);
				pstmt = con.prepareStatement(sp_del_sale);
				pstmt.setString(1, sale_id);
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
		public List<SaleVO> searchDB(String group_id, String c_product_id) {
			List<SaleVO> list = new ArrayList<SaleVO>();
			SaleVO saleVO = null;

			Connection con = null;
			PreparedStatement pstmt = null;
			ResultSet rs = null;
			try {
				Class.forName("com.mysql.jdbc.Driver");
				con = DriverManager.getConnection(dbURL, dbUserName, dbPassword);
				pstmt = con.prepareStatement(sp_select_sale_bycproductid);

				pstmt.setString(1, group_id);
				pstmt.setString(2, c_product_id);

				rs = pstmt.executeQuery();
				while (rs.next()) {
					saleVO = new SaleVO();
					saleVO.setSale_id(rs.getString("sale_id"));
					saleVO.setSeq_no(rs.getString("seq_no"));
					saleVO.setOrder_no(rs.getString("order_no"));
					saleVO.setProduct_name(rs.getString("product_name"));
					saleVO.setC_product_id(rs.getString("c_product_id"));
					saleVO.setQuantity(rs.getInt("quantity"));
					saleVO.setPrice(rs.getFloat("price"));
					saleVO.setInvoice(rs.getString("invoice"));
					saleVO.setInvoice_date(rs.getDate("invoice_date"));
					saleVO.setTrans_list_date(rs.getDate("trans_list_date"));
					saleVO.setDis_date(rs.getDate("dis_date"));
					saleVO.setMemo(rs.getString("memo"));
					saleVO.setSale_date(rs.getDate("sale_date"));
					saleVO.setOrder_source(rs.getString("order_source"));
					saleVO.setCustomer_id(rs.getString("customer_id"));
					saleVO.setName(rs.getString("name"));
					list.add(saleVO);
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
		public List<SaleVO> searchAllDB(String group_id) {

			List<SaleVO> list = new ArrayList<SaleVO>();
			SaleVO saleVO = null;

			Connection con = null;
			PreparedStatement pstmt = null;
			ResultSet rs = null;
			try {
				Class.forName("com.mysql.jdbc.Driver");
				con = DriverManager.getConnection(dbURL, dbUserName, dbPassword);
				pstmt = con.prepareStatement(sp_selectall_sale);
				pstmt.setString(1, group_id);
				rs = pstmt.executeQuery();
				while (rs.next()) {
					saleVO = new SaleVO();
					saleVO.setOrder_no(rs.getString("order_no"));
					saleVO.setName(rs.getString("name"));
					saleVO.setTrans_list_date(rs.getDate("trans_list_date"));
					saleVO.setSale_date(rs.getDate("sale_date"));
					saleVO.setDis_date(rs.getDate("dis_date"));
					saleVO.setOrder_source(rs.getString("order_source"));
					saleVO.setMemo(rs.getString("memo"));
					list.add(saleVO); // Store the row in the list
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
		public List<SaleVO> searchTransListDateDB(String group_id, String trans_list_start_date,
				String trans_list_end_date) {

			List<SaleVO> list = new ArrayList<SaleVO>();
			SaleVO saleVO = null;

			Connection con = null;
			PreparedStatement pstmt = null;
			ResultSet rs = null;

			try {
				Class.forName("com.mysql.jdbc.Driver");
				con = DriverManager.getConnection(dbURL, dbUserName, dbPassword);
				pstmt = con.prepareStatement(sp_select_sale_bytranslistdate);
				pstmt.setString(1, group_id);
				pstmt.setString(2, trans_list_start_date);
				pstmt.setString(3, trans_list_end_date);
				rs = pstmt.executeQuery();
				while (rs.next()) {
					saleVO = new SaleVO();
					saleVO.setSale_id(rs.getString("sale_id"));
					saleVO.setSeq_no(rs.getString("seq_no"));
					saleVO.setOrder_no(rs.getString("order_no"));
					saleVO.setProduct_name(rs.getString("product_name"));
					saleVO.setC_product_id(rs.getString("c_product_id"));
					saleVO.setQuantity(rs.getInt("quantity"));
					saleVO.setPrice(rs.getFloat("price"));
					saleVO.setInvoice(rs.getString("invoice"));
					saleVO.setInvoice_date(rs.getDate("invoice_date"));
					saleVO.setTrans_list_date(rs.getDate("trans_list_date"));
					saleVO.setDis_date(rs.getDate("dis_date"));
					saleVO.setMemo(rs.getString("memo"));
					saleVO.setSale_date(rs.getDate("sale_date"));
					saleVO.setOrder_source(rs.getString("order_source"));
					saleVO.setCustomer_id(rs.getString("customer_id"));
					saleVO.setName(rs.getString("name"));
					list.add(saleVO); // Store the row in the list
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
		public List<SaleVO> getNewSaleSeqNo(String group_id) {

			List<SaleVO> list = new ArrayList<SaleVO>();
			SaleVO saleVO = null;

			Connection con = null;
			PreparedStatement pstmt = null;
			ResultSet rs = null;

			try {
				Class.forName("com.mysql.jdbc.Driver");
				con = DriverManager.getConnection(dbURL, dbUserName, dbPassword);
				pstmt = con.prepareStatement(sp_get_sale_newseqno);
				pstmt.setString(1, group_id);
				rs = pstmt.executeQuery();
				while (rs.next()) {
					saleVO = new SaleVO();
					saleVO.setSeq_no(rs.getString("seq_no"));
					list.add(saleVO);
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
		public List<SaleVO> searchDisDateDB(String group_id, String dis_start_date, String dis_end_date) {

			List<SaleVO> list = new ArrayList<SaleVO>();
			SaleVO saleVO = null;

			Connection con = null;
			PreparedStatement pstmt = null;
			ResultSet rs = null;

			try {
				Class.forName("com.mysql.jdbc.Driver");
				con = DriverManager.getConnection(dbURL, dbUserName, dbPassword);
				pstmt = con.prepareStatement(sp_select_sale_bydisdate);
				pstmt.setString(1, group_id);
				pstmt.setString(2, dis_start_date);
				pstmt.setString(3, dis_end_date);
				rs = pstmt.executeQuery();
				while (rs.next()) {
					saleVO = new SaleVO();
					saleVO.setSale_id(rs.getString("sale_id"));
					saleVO.setSeq_no(rs.getString("seq_no"));
					saleVO.setOrder_no(rs.getString("order_no"));
					saleVO.setProduct_name(rs.getString("product_name"));
					saleVO.setC_product_id(rs.getString("c_product_id"));
					saleVO.setQuantity(rs.getInt("quantity"));
					saleVO.setPrice(rs.getFloat("price"));
					saleVO.setInvoice(rs.getString("invoice"));
					saleVO.setInvoice_date(rs.getDate("invoice_date"));
					saleVO.setTrans_list_date(rs.getDate("trans_list_date"));
					saleVO.setDis_date(rs.getDate("dis_date"));
					saleVO.setMemo(rs.getString("memo"));
					saleVO.setSale_date(rs.getDate("sale_date"));
					saleVO.setOrder_source(rs.getString("order_source"));
					saleVO.setCustomer_id(rs.getString("customer_id"));
					saleVO.setName(rs.getString("name"));
					list.add(saleVO); // Store the row in the list
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
		public List<ProductVO> getProductByName(String group_id, String product_name) {

			List<ProductVO> list = new ArrayList<ProductVO>();
			ProductVO productVO = null;

			Connection con = null;
			PreparedStatement pstmt = null;
			ResultSet rs = null;

			try {
				Class.forName("com.mysql.jdbc.Driver");
				con = DriverManager.getConnection(dbURL, dbUserName, dbPassword);
				pstmt = con.prepareStatement(sp_get_product_byname);
				pstmt.setString(1, group_id);
				pstmt.setString(2, product_name);
				rs = pstmt.executeQuery();
				while (rs.next()) {
					productVO = new ProductVO();
					productVO.setProduct_id(rs.getString("product_id"));
					productVO.setC_product_id(rs.getString("c_product_id"));
					productVO.setProduct_name(rs.getString("product_name"));
					productVO.setPrice(rs.getString("price"));
					productVO.setCost(rs.getString("cost"));
					list.add(productVO);
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
		public List<ProductVO> getProductById(String group_id, String c_product_id) {

			List<ProductVO> list = new ArrayList<ProductVO>();
			ProductVO productVO = null;

			Connection con = null;
			PreparedStatement pstmt = null;
			ResultSet rs = null;

			try {
				Class.forName("com.mysql.jdbc.Driver");
				con = DriverManager.getConnection(dbURL, dbUserName, dbPassword);
				pstmt = con.prepareStatement(sp_get_product_byid);
				pstmt.setString(1, group_id);
				pstmt.setString(2, c_product_id);
				rs = pstmt.executeQuery();
				while (rs.next()) {
					productVO = new ProductVO();
					productVO.setProduct_id(rs.getString("product_id"));
					productVO.setC_product_id(rs.getString("c_product_id"));
					productVO.setProduct_name(rs.getString("product_name"));
					productVO.setPrice(rs.getString("price"));
					productVO.setCost(rs.getString("cost"));
					list.add(productVO);
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
		public List<SaleDetailVO> getSaleDetail(SaleDetailVO saleDetailVO) {
			List<SaleDetailVO> list = new ArrayList<SaleDetailVO>();
			SaleDetailVO result = null;

			Connection con = null;
			PreparedStatement pstmt = null;
			ResultSet rs = null;

			util = new Util();
			
			try {
				Class.forName("com.mysql.jdbc.Driver");
				con = DriverManager.getConnection(dbURL, dbUserName, dbPassword);
				pstmt = con.prepareStatement(sp_get_sale_detail);

				String sale_id = util.null2str(saleDetailVO.getSale_id());

				pstmt.setString(1, sale_id);
				rs = pstmt.executeQuery();
				while (rs.next()) {
					result = new SaleDetailVO();

					result.setSaleDetail_id(rs.getString("saleDetail_id"));
					result.setSeq_no(rs.getString("seq_no"));
					result.setGroup_id(rs.getString("group_id"));
					result.setOrder_no(rs.getString("order_no"));
					result.setUser_id(rs.getString("user_id"));
					result.setProduct_id(rs.getString("product_id"));
					result.setProduct_name(rs.getString("product_name"));
					result.setC_product_id(rs.getString("c_product_id"));
					result.setCustomer_id(rs.getString("customer_id"));
					result.setName(rs.getString("name"));
					result.setQuantity(rs.getInt("quantity"));
					result.setPrice(rs.getFloat("price"));
					result.setInvoice(rs.getString("invoice"));
					result.setInvoice_date(rs.getDate("invoice_date"));
					result.setTrans_list_date(rs.getDate("trans_list_date"));
					result.setDis_date(rs.getDate("dis_date"));
					result.setMemo(rs.getString("memo"));
					result.setSale_date(rs.getDate("sale_date"));
					result.setOrder_source(rs.getString("order_source"));
					result.setReturn_date(rs.getDate("return_date"));
					result.setIsreturn(rs.getInt("isreturn"));
					result.setDeliveryway(rs.getString("deliveryway"));

					list.add(result);
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
