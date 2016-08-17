package tw.com.aber.salereturn.controller;

import java.io.IOException;
import java.io.Serializable;
import java.sql.Connection;
import java.sql.Date;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Enumeration;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class salereturn extends HttpServlet {
	private static final long serialVersionUID = 1L;

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		request.setCharacterEncoding("UTF-8");
		response.setCharacterEncoding("UTF-8");
		SaleReturnService saleReturnService = null;
		String action = request.getParameter("action");
		String group_id = request.getSession().getAttribute("group_id").toString();
		String user_id = request.getSession().getAttribute("user_id").toString();
		if ("search".equals(action)) {
			try {
				/*************************** 1.接收請求參數-格式檢查 ****************************************/
				String c_product_id = request.getParameter("c_product_id");
				/*************************** 2.開始查詢資料 ****************************************/
				// 假如無查詢條件，則是查詢全部
				if (c_product_id == null || (c_product_id.trim()).length() == 0) {
					saleReturnService = new SaleReturnService();
					List<SaleReturnVO> list = saleReturnService.getSearchAllDB(group_id);
					Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd").create();
					String jsonStrList = gson.toJson(list);
					response.getWriter().write(jsonStrList);
					return;// 程式中斷
				}
				// 查詢指定ID
				if (c_product_id != null || c_product_id.trim().length() != 0) {
					saleReturnService = new SaleReturnService();
					List<SaleReturnVO> list = saleReturnService.getSearchDB(group_id, c_product_id);
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
		if ("search_product_data".equals(action)) {
			String term = request.getParameter("term");
			String identity = request.getParameter("identity");
			if ("ID".equals(identity)) {
				saleReturnService = new SaleReturnService();
				List<ProductVO> list = saleReturnService.getSearchProductById(group_id, term);
				Gson gson = new Gson();
				String jsonStrList = gson.toJson(list);
				response.getWriter().write(jsonStrList);
				return;// 程式中斷
			}
			if ("NAME".equals(identity)) {
				saleReturnService = new SaleReturnService();
				List<ProductVO> list = saleReturnService.getSearchProductByName(group_id, term);
				Gson gson = new Gson();
				String jsonStrList = gson.toJson(list);
				response.getWriter().write(jsonStrList);
				return;// 程式中斷
			}
		}
		if ("insert_sale_return".equals(action)) {
			try {
				/*************************** 1.接收請求參數 ***************************************/
				String sale_id = request.getParameter("sale_id");
				SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
				
				java.sql.Date return_date = null;
				try {
					String return_date_str = request.getParameter("return_date");
					
					java.util.Date invoice_date_util = sdf.parse(return_date_str);
					return_date = new java.sql.Date(invoice_date_util.getTime());
				} catch (ParseException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				/*************************** 2.開始銷貨退回 ***************************************/
				saleReturnService = new SaleReturnService();
				saleReturnService.addSaleReturn(sale_id, user_id, return_date);
				/*************************** 3.刪除完成,準備轉交(Send the Success view) ***********/
				saleReturnService = new SaleReturnService();
				List<SaleReturnVO> list = saleReturnService.getSearchReturnDateDB(group_id, return_date, return_date);
				System.out.println("group_id: "+ group_id );
				System.out.println("return_date_str: "+ return_date );
				Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd").create();
				String jsonStrList = gson.toJson(list);
				response.getWriter().write(jsonStrList);
				return;// 程式中斷
				/*************************** 其他可能的錯誤處理 **********************************/
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		if ("delete_sale_return".equals(action)) {
			try {
				/*************************** 1.接收請求參數 ***************************************/
				String sale_id = request.getParameter("sale_id");
				String c_product_id = request.getParameter("c_product_id");
				/*************************** 2.開始銷貨退回 ***************************************/
				saleReturnService = new SaleReturnService();
				saleReturnService.deleteSaleReturn(sale_id, user_id);
				/*************************** 3.刪除完成,準備轉交(Send the Success view) ***********/
				// 假如無查詢條件，則是查詢全部
				if (c_product_id == null || (c_product_id.trim()).length() == 0) {
					saleReturnService = new SaleReturnService();
					List<SaleReturnVO> list = saleReturnService.getSearchAllDB(group_id);
					Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd").create();
					String jsonStrList = gson.toJson(list);
					response.getWriter().write(jsonStrList);
					return;// 程式中斷
				}
				// 查詢指定ID
				if (c_product_id != null || c_product_id.trim().length() != 0) {
					saleReturnService = new SaleReturnService();
					List<SaleReturnVO> list = saleReturnService.getSearchDB(group_id, c_product_id);
					Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd").create();
					String jsonStrList = gson.toJson(list);
					response.getWriter().write(jsonStrList);
					return;// 程式中斷
				}
				/*************************** 其他可能的錯誤處理 **********************************/
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		if ("search_trans_list_date".equals(action)) {
			try {
				String start_date = request.getParameter("trans_list_start_date");
				String end_date = request.getParameter("trans_list_end_date");
				if (start_date.trim().length() != 0 & end_date.trim().length() == 0) {
					List<SaleReturnVO> list = new ArrayList<SaleReturnVO>();
					SaleReturnVO saleReturnVO = new SaleReturnVO();
					saleReturnVO.setMessage("如要以日期查詢，請完整填寫訖日欄位");
					list.add(saleReturnVO);
					Gson gson = new Gson();
					String jsonStrList = gson.toJson(list);
					response.getWriter().write(jsonStrList);
					return;// 程式中斷
				}
				if (end_date.trim().length() != 0 & start_date.trim().length() == 0) {
					List<SaleReturnVO> list = new ArrayList<SaleReturnVO>();
					SaleReturnVO saleReturnVO = new SaleReturnVO();
					saleReturnVO.setMessage("如要以日期查詢，請完整填寫起日欄位");
					list.add(saleReturnVO);
					Gson gson = new Gson();
					String jsonStrList = gson.toJson(list);
					response.getWriter().write(jsonStrList);
					return;// 程式中斷
				}
				if (start_date.trim().length() != 0 & end_date.trim().length() != 0) {
					if (DateConversionToDigital(start_date) > DateConversionToDigital(end_date)) {
						List<SaleReturnVO> list = new ArrayList<SaleReturnVO>();
						SaleReturnVO saleReturnVO = new SaleReturnVO();
						saleReturnVO.setMessage("起日不可大於訖日");
						list.add(saleReturnVO);
						Gson gson = new Gson();
						String jsonStrList = gson.toJson(list);
						response.getWriter().write(jsonStrList);
						return;// 程式中斷
					} else {
						// 查詢指定期限
						saleReturnService = new SaleReturnService();
						List<SaleReturnVO> list = saleReturnService.getSearchTransListDateDB(group_id, start_date, end_date);
						SaleReturnVO saleReturnVO = new SaleReturnVO();
						saleReturnVO.setMessage("驗證通過");
						list.add(saleReturnVO);
						Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd").create();
						String jsonStrList = gson.toJson(list);
						response.getWriter().write(jsonStrList);
						return;// 程式中斷
					}
				}
				// 假如無查詢條件，則是查詢全部
				if (start_date.trim().length() == 0 & end_date.trim().length() == 0) {
					saleReturnService = new SaleReturnService();
					List<SaleReturnVO> list = saleReturnService.getSearchAllDB(group_id);
					SaleReturnVO saleReturnVO = new SaleReturnVO();
					saleReturnVO.setMessage("驗證通過");
					list.add(saleReturnVO);
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
				/*************************** 1.接收請求參數-格式檢查 ****************************************/
				String start_date = request.getParameter("dis_start_date");
				String end_date = request.getParameter("dis_end_date");
				if (start_date.trim().length() != 0 & end_date.trim().length() == 0) {
					List<SaleReturnVO> list = new ArrayList<SaleReturnVO>();
					SaleReturnVO saleReturnVO = new SaleReturnVO();
					saleReturnVO.setMessage("如要以日期查詢，請完整填寫訖日欄位");
					list.add(saleReturnVO);
					Gson gson = new Gson();
					String jsonStrList = gson.toJson(list);
					response.getWriter().write(jsonStrList);
					return;// 程式中斷
				}
				if (end_date.trim().length() != 0 & start_date.trim().length() == 0) {
					List<SaleReturnVO> list = new ArrayList<SaleReturnVO>();
					SaleReturnVO saleReturnVO = new SaleReturnVO();
					saleReturnVO.setMessage("如要以日期查詢，請完整填寫起日欄位");
					list.add(saleReturnVO);
					Gson gson = new Gson();
					String jsonStrList = gson.toJson(list);
					response.getWriter().write(jsonStrList);
					return;// 程式中斷
				}
				if (start_date.trim().length() != 0 & end_date.trim().length() != 0) {
					if (DateConversionToDigital(start_date) > DateConversionToDigital(end_date)) {
						List<SaleReturnVO> list = new ArrayList<SaleReturnVO>();
						SaleReturnVO saleReturnVO = new SaleReturnVO();
						saleReturnVO.setMessage("起日不可大於訖日");
						list.add(saleReturnVO);
						Gson gson = new Gson();
						String jsonStrList = gson.toJson(list);
						response.getWriter().write(jsonStrList);
						return;// 程式中斷
					} else {
						// 查詢指定期限
						saleReturnService = new SaleReturnService();
						List<SaleReturnVO> list = saleReturnService.getSearchDisDateDB(group_id, start_date, end_date);
						SaleReturnVO saleReturnVO = new SaleReturnVO();
						saleReturnVO.setMessage("驗證通過");
						list.add(saleReturnVO);
						Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd").create();
						String jsonStrList = gson.toJson(list);
						response.getWriter().write(jsonStrList);
						return;// 程式中斷
					}
				}
				/*************************** 2.開始查詢資料 ****************************************/
				// 假如無查詢條件，則是查詢全部
				if (start_date.trim().length() == 0 & end_date.trim().length() == 0) {
					saleReturnService = new SaleReturnService();
					List<SaleReturnVO> list = saleReturnService.getSearchAllDB(group_id);
					SaleReturnVO saleReturnVO = new SaleReturnVO();
					saleReturnVO.setMessage("驗證通過");
					list.add(saleReturnVO);
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
		if ("search_sale_return_date".equals(action)) {
			try {
				/*************************** 1.接收請求參數-格式檢查 ****************************************/
				String start_date = request.getParameter("return_start_date");
				String end_date = request.getParameter("return_end_date");
				if (start_date.trim().length() != 0 & end_date.trim().length() == 0) {
					List<SaleReturnVO> list = new ArrayList<SaleReturnVO>();
					SaleReturnVO saleReturnVO = new SaleReturnVO();
					saleReturnVO.setMessage("如要以日期查詢，請完整填寫訖日欄位");
					list.add(saleReturnVO);
					Gson gson = new Gson();
					String jsonStrList = gson.toJson(list);
					response.getWriter().write(jsonStrList);
					return;// 程式中斷
				}
				if (end_date.trim().length() != 0 & start_date.trim().length() == 0) {
					List<SaleReturnVO> list = new ArrayList<SaleReturnVO>();
					SaleReturnVO saleReturnVO = new SaleReturnVO();
					saleReturnVO.setMessage("如要以日期查詢，請完整填寫起日欄位");
					list.add(saleReturnVO);
					Gson gson = new Gson();
					String jsonStrList = gson.toJson(list);
					response.getWriter().write(jsonStrList);
					return;// 程式中斷
				}
				if (start_date.trim().length() != 0 & end_date.trim().length() != 0) {
					if (DateConversionToDigital(start_date) > DateConversionToDigital(end_date)) {
						List<SaleReturnVO> list = new ArrayList<SaleReturnVO>();
						SaleReturnVO saleReturnVO = new SaleReturnVO();
						saleReturnVO.setMessage("起日不可大於訖日");
						list.add(saleReturnVO);
						Gson gson = new Gson();
						String jsonStrList = gson.toJson(list);
						response.getWriter().write(jsonStrList);
						return;// 程式中斷
					} else {
						SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
						java.sql.Date return_start_date = null;
						try {
							java.util.Date invoice_date_util = sdf.parse(start_date);
							return_start_date = new java.sql.Date(invoice_date_util.getTime());
						} catch (ParseException e) {
							e.printStackTrace();
						}
						java.sql.Date return_end_date = null;
						try {
							java.util.Date invoice_date_util = sdf.parse(end_date);
							return_end_date = new java.sql.Date(invoice_date_util.getTime());
						} catch (ParseException e) {
							e.printStackTrace();
						}
						// 查詢指定期限
						saleReturnService = new SaleReturnService();
						List<SaleReturnVO> list = saleReturnService.getSearchReturnDateDB(group_id, return_start_date, return_end_date);
						SaleReturnVO saleReturnVO = new SaleReturnVO();
						saleReturnVO.setMessage("驗證通過");
						list.add(saleReturnVO);
						Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd").create();
						String jsonStrList = gson.toJson(list);
						response.getWriter().write(jsonStrList);
						return;// 程式中斷
					}
				}
				/*************************** 2.開始查詢資料 ****************************************/
				// 假如無查詢條件，則是查詢全部
				if (start_date.trim().length() == 0 & end_date.trim().length() == 0) {
					saleReturnService = new SaleReturnService();
					List<SaleReturnVO> list = saleReturnService.getSearchAllSaleReturnDB(group_id);
					SaleReturnVO saleReturnVO = new SaleReturnVO();
					saleReturnVO.setMessage("驗證通過");
					list.add(saleReturnVO);
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

	// 格式化銷貨單號，不足四位補位
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

	/************************* 對應資料庫表格格式 **************************************/
	@SuppressWarnings("serial")
	public class SaleReturnVO implements Serializable {
		private String sale_id;
		private String seq_no;
		private String group_id;
		private String order_no;
		private String user_id;
		private String product_id;
		private String product_name;
		private String c_product_id;
		private String customer_id;
		private String name;
		private Integer quantity;
		private Float price;
		private String invoice;
		private Date invoice_date;
		private Date trans_list_date;
		private Date dis_date;
		private String memo;
		private Date sale_date;
		private String order_source;
		@SuppressWarnings("unused")
		private String message;// 此參數用來存放訊息

		public String getSale_id() {
			return sale_id;
		}

		public void setSale_id(String sale_id) {
			this.sale_id = sale_id;
		}

		public String getSeq_no() {
			return seq_no;
		}

		public void setSeq_no(String seq_no) {
			this.seq_no = seq_no;
		}

		public String getGroup_id() {
			return group_id;
		}

		public void setGroup_id(String group_id) {
			this.group_id = group_id;
		}

		public String getOrder_no() {
			return order_no;
		}

		public void setOrder_no(String order_no) {
			this.order_no = order_no;
		}

		public String getUser_id() {
			return user_id;
		}

		public void setUser_id(String user_id) {
			this.user_id = user_id;
		}

		public String getProduct_id() {
			return product_id;
		}

		public void setProduct_id(String product_id) {
			this.product_id = product_id;
		}

		public String getProduct_name() {
			return product_name;
		}

		public void setProduct_name(String product_name) {
			this.product_name = product_name;
		}

		public String getC_product_id() {
			return c_product_id;
		}

		public void setC_product_id(String c_product_id) {
			this.c_product_id = c_product_id;
		}

		public String getCustomer_id() {
			return customer_id;
		}

		public void setCustomer_id(String customer_id) {
			this.customer_id = customer_id;
		}

		public String getName() {
			return name;
		}

		public void setName(String name) {
			this.name = name;
		}

		public Integer getQuantity() {
			return quantity;
		}

		public void setQuantity(Integer quantity) {
			this.quantity = quantity;
		}

		public Float getPrice() {
			return price;
		}

		public void setPrice(Float price) {
			this.price = price;
		}

		public String getInvoice() {
			return invoice;
		}

		public void setInvoice(String invoice) {
			this.invoice = invoice;
		}

		public Date getInvoice_date() {
			return invoice_date;
		}

		public void setInvoice_date(Date invoice_date) {
			this.invoice_date = invoice_date;
		}

		public Date getTrans_list_date() {
			return trans_list_date;
		}

		public void setTrans_list_date(Date trans_list_date) {
			this.trans_list_date = trans_list_date;
		}

		public Date getDis_date() {
			return dis_date;
		}

		public void setDis_date(Date dis_date) {
			this.dis_date = dis_date;
		}

		public String getMemo() {
			return memo;
		}

		public void setMemo(String memo) {
			this.memo = memo;
		}

		public Date getSale_date() {
			return sale_date;
		}

		public void setSale_date(Date sale_date) {
			this.sale_date = sale_date;
		}

		public String getOrder_source() {
			return order_source;
		}

		public void setOrder_source(String order_source) {
			this.order_source = order_source;
		}

		public String getMessage() {
			return message;
		}

		public void setMessage(String message) {
			this.message = message;
		}

	}

	@SuppressWarnings("serial")
	public class ProductVO implements java.io.Serializable {
		private String product_id;
		private String product_name;
		private String c_product_id;

		public String getProduct_id() {
			return product_id;
		}

		public void setProduct_id(String product_id) {
			this.product_id = product_id;
		}

		public String getProduct_name() {
			return product_name;
		}

		public void setProduct_name(String product_name) {
			this.product_name = product_name;
		}

		public String getC_product_id() {
			return c_product_id;
		}

		public void setC_product_id(String c_product_id) {
			this.c_product_id = c_product_id;
		}
	}

	/*************************** 制定規章方法 ****************************************/
	interface salereturn_interface {

		public void insertSaleReturnTotDB(String sale_id, String user_id, Date return_date);

		public void deleteSaleReturnTotDB(String sale_id, String user_id);

		public List<ProductVO> getProductByName(String group_id, String product_name);

		public List<ProductVO> getProductById(String group_id, String c_product_id);

		public List<SaleReturnVO> searchDB(String group_id, String c_product_id);

		public List<SaleReturnVO> searchAllDB(String group_id);

		public List<SaleReturnVO> searchAllSaleReturnDB(String group_id);

		public List<SaleReturnVO> searchDateDB(String group_id, Date trans_list_start_date, Date trans_list_end_date);

		public List<SaleReturnVO> getSearchReturnDateDB(String group_id, Date return_start_date, Date return_end_date);

		public List<SaleReturnVO> searchTransListDateDB(String group_id, String trans_list_start_date, String trans_list_end_date);

		public List<SaleReturnVO> searchDisDateDB(String group_id, String dis_start_date, String dis_end_date);

	}

	/*************************** 處理業務邏輯 ****************************************/
	class SaleReturnService {
		private salereturn_interface dao;

		public SaleReturnService() {
			dao = new SaleReturnDAO();
		}

		public void addSaleReturn(String sale_id, String user_id, Date return_date) {
			dao.insertSaleReturnTotDB(sale_id, user_id, return_date);
		}

		public void deleteSaleReturn(String sale_id, String user_id) {
			dao.deleteSaleReturnTotDB(sale_id, user_id);
		}

		public List<ProductVO> getSearchProductById(String group_id, String c_product_id) {
			return dao.getProductById(group_id, c_product_id);
		}

		public List<ProductVO> getSearchProductByName(String group_id, String product_name) {
			return dao.getProductByName(group_id, product_name);
		}

		public List<SaleReturnVO> getSearchDB(String group_id, String c_product_id) {
			return dao.searchDB(group_id, c_product_id);
		}

		public List<SaleReturnVO> getSearchAllDB(String group_id) {
			return dao.searchAllDB(group_id);
		}

		public List<SaleReturnVO> getSearchAllSaleReturnDB(String group_id) {
			return dao.searchAllSaleReturnDB(group_id);
		}

		public List<SaleReturnVO> getSearchDateDB(String group_id, Date trans_list_start_date, Date trans_list_end_date) {
			return dao.searchDateDB(group_id, trans_list_start_date, trans_list_end_date);
		}

		public List<SaleReturnVO> getSearchReturnDateDB(String group_id, Date return_start_date, Date return_end_date) {
			return dao.getSearchReturnDateDB(group_id, return_start_date, return_end_date);
		}

		public List<SaleReturnVO> getSearchTransListDateDB(String group_id, String trans_list_start_date, String trans_list_end_date) {
			return dao.searchTransListDateDB(group_id, trans_list_start_date, trans_list_end_date);
		}

		public List<SaleReturnVO> getSearchDisDateDB(String group_id, String dis_start_date, String dis_end_date) {
			return dao.searchDisDateDB(group_id, dis_start_date, dis_end_date);
		}
	}

	/*************************** 操作資料庫 ****************************************/
	class SaleReturnDAO implements salereturn_interface {
		// 會使用到的Stored procedure
		private static final String sp_selectall_sale = "call sp_selectall_sale (?)";
		private static final String sp_selectall_sale_return = "call sp_selectall_sale_return (?)";
		private static final String sp_select_sale_bycproductid = "call sp_select_sale_bycproductid (?,?)";
		private static final String sp_select_sale_bytranslistdate = "call sp_select_sale_bytranslistdate(?,?,?)";
		private static final String sp_select_sale_bydisdate = "call sp_select_sale_bydisdate(?,?,?)";
		private static final String sp_insert_sale_return = "call sp_insert_sale_return(?,?,?)";
		private static final String sp_del_sale_return = "call sp_del_sale_return(?,?)";
		private static final String sp_get_product_byid = "call sp_get_product_byid (?,?)";
		private static final String sp_get_product_byname = "call sp_get_product_byname (?,?)";
		private static final String sp_select_sale_return_byreturndate = "call sp_select_sale_return_byreturndate(?,?,?)";
		private final String dbURL = getServletConfig().getServletContext().getInitParameter("dbURL")
				+ "?useUnicode=true&characterEncoding=utf-8&useSSL=false";
		private final String dbUserName = getServletConfig().getServletContext().getInitParameter("dbUserName");
		private final String dbPassword = getServletConfig().getServletContext().getInitParameter("dbPassword");

		@Override
		public List<SaleReturnVO> searchDateDB(String group_id, Date trans_list_start_date, Date trans_list_end_date) {
			// TODO Auto-generated method stub
			List<SaleReturnVO> list = new ArrayList<SaleReturnVO>();
			SaleReturnVO saleReturnVO = null;

			Connection con = null;
			PreparedStatement pstmt = null;
			ResultSet rs = null;

			try {
				Class.forName("com.mysql.jdbc.Driver");
				con = DriverManager.getConnection(dbURL, dbUserName, dbPassword);
				pstmt = con.prepareStatement(sp_select_sale_bytranslistdate);
				pstmt.setString(1, group_id);
				pstmt.setDate(2, trans_list_start_date);
				pstmt.setDate(3, trans_list_end_date);
				rs = pstmt.executeQuery();
				while (rs.next()) {
					saleReturnVO = new SaleReturnVO();
					saleReturnVO.setSale_id(rs.getString("sale_id"));
					saleReturnVO.setSeq_no(rs.getString("seq_no"));
					saleReturnVO.setOrder_no(rs.getString("order_no"));
					saleReturnVO.setProduct_name(rs.getString("product_name"));
					saleReturnVO.setC_product_id(rs.getString("c_product_id"));
					saleReturnVO.setQuantity(rs.getInt("quantity"));
					saleReturnVO.setPrice(rs.getFloat("price"));
					saleReturnVO.setInvoice(rs.getString("invoice"));
					saleReturnVO.setInvoice_date(rs.getDate("invoice_date"));
					saleReturnVO.setTrans_list_date(rs.getDate("trans_list_date"));
					saleReturnVO.setDis_date(rs.getDate("dis_date"));
					saleReturnVO.setMemo(rs.getString("memo"));
					saleReturnVO.setSale_date(rs.getDate("sale_date"));
					saleReturnVO.setOrder_source(rs.getString("order_source"));
					list.add(saleReturnVO); // Store the row in the list
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
			// TODO Auto-generated method stub
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
			// TODO Auto-generated method stub
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
		public List<SaleReturnVO> searchDB(String group_id, String c_product_id) {
			// TODO Auto-generated method stub
			List<SaleReturnVO> list = new ArrayList<SaleReturnVO>();
			SaleReturnVO saleReturnVO = null;

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
					saleReturnVO = new SaleReturnVO();
					saleReturnVO.setSale_id(rs.getString("sale_id"));
					saleReturnVO.setSeq_no(rs.getString("seq_no"));
					saleReturnVO.setOrder_no(rs.getString("order_no"));
					saleReturnVO.setProduct_id(rs.getString("product_id"));
					saleReturnVO.setProduct_name(rs.getString("product_name"));
					saleReturnVO.setC_product_id(rs.getString("c_product_id"));
					saleReturnVO.setQuantity(rs.getInt("quantity"));
					saleReturnVO.setPrice(rs.getFloat("price"));
					saleReturnVO.setInvoice(rs.getString("invoice"));
					saleReturnVO.setInvoice_date(rs.getDate("invoice_date"));
					saleReturnVO.setTrans_list_date(rs.getDate("trans_list_date"));
					saleReturnVO.setDis_date(rs.getDate("dis_date"));
					saleReturnVO.setMemo(rs.getString("memo"));
					saleReturnVO.setSale_date(rs.getDate("sale_date"));
					saleReturnVO.setOrder_source(rs.getString("order_source"));
					list.add(saleReturnVO);
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
		public List<SaleReturnVO> searchAllDB(String group_id) {
			// TODO Auto-generated method stub
			List<SaleReturnVO> list = new ArrayList<SaleReturnVO>();
			SaleReturnVO saleReturnVO = null;

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
					saleReturnVO = new SaleReturnVO();
					saleReturnVO.setSale_id(rs.getString("sale_id"));
					saleReturnVO.setSeq_no(rs.getString("seq_no"));
					saleReturnVO.setOrder_no(rs.getString("order_no"));
					saleReturnVO.setProduct_id(rs.getString("product_id"));
					saleReturnVO.setProduct_name(rs.getString("product_name"));
					saleReturnVO.setC_product_id(rs.getString("c_product_id"));
					saleReturnVO.setQuantity(rs.getInt("quantity"));
					saleReturnVO.setPrice(rs.getFloat("price"));
					saleReturnVO.setInvoice(rs.getString("invoice"));
					saleReturnVO.setInvoice_date(rs.getDate("invoice_date"));
					saleReturnVO.setTrans_list_date(rs.getDate("trans_list_date"));
					saleReturnVO.setDis_date(rs.getDate("dis_date"));
					saleReturnVO.setMemo(rs.getString("memo"));
					saleReturnVO.setSale_date(rs.getDate("sale_date"));
					saleReturnVO.setOrder_source(rs.getString("order_source"));
					list.add(saleReturnVO); // Store the row in the list
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
		public List<SaleReturnVO> searchAllSaleReturnDB(String group_id) {
			// TODO Auto-generated method stub
			List<SaleReturnVO> list = new ArrayList<SaleReturnVO>();
			SaleReturnVO saleReturnVO = null;

			Connection con = null;
			PreparedStatement pstmt = null;
			ResultSet rs = null;

			try {
				Class.forName("com.mysql.jdbc.Driver");
				con = DriverManager.getConnection(dbURL, dbUserName, dbPassword);
				pstmt = con.prepareStatement(sp_selectall_sale_return);
				pstmt.setString(1, group_id);
				rs = pstmt.executeQuery();
				while (rs.next()) {
					saleReturnVO = new SaleReturnVO();
					saleReturnVO.setSale_id(rs.getString("sale_id"));
					saleReturnVO.setSeq_no(rs.getString("seq_no"));
					saleReturnVO.setOrder_no(rs.getString("order_no"));
					saleReturnVO.setProduct_id(rs.getString("product_id"));
					saleReturnVO.setProduct_name(rs.getString("product_name"));
					saleReturnVO.setC_product_id(rs.getString("c_product_id"));
					saleReturnVO.setQuantity(rs.getInt("quantity"));
					saleReturnVO.setPrice(rs.getFloat("price"));
					saleReturnVO.setInvoice(rs.getString("invoice"));
					saleReturnVO.setInvoice_date(rs.getDate("invoice_date"));
					saleReturnVO.setTrans_list_date(rs.getDate("trans_list_date"));
					saleReturnVO.setDis_date(rs.getDate("dis_date"));
					saleReturnVO.setMemo(rs.getString("memo"));
					saleReturnVO.setSale_date(rs.getDate("sale_date"));
					saleReturnVO.setOrder_source(rs.getString("order_source"));
					list.add(saleReturnVO); // Store the row in the list
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
		public void insertSaleReturnTotDB(String sale_id, String user_id, Date return_date) {
			// TODO Auto-generated method stub
			Connection con = null;
			PreparedStatement pstmt = null;
			try {
				Class.forName("com.mysql.jdbc.Driver");
				con = DriverManager.getConnection(dbURL, dbUserName, dbPassword);
				pstmt = con.prepareStatement(sp_insert_sale_return);

				pstmt.setString(1, sale_id);
				pstmt.setString(2, user_id);
				pstmt.setDate(3, return_date);

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
		public List<SaleReturnVO> getSearchReturnDateDB(String group_id, Date return_start_date, Date return_end_date) {
			// TODO Auto-generated method stub
			List<SaleReturnVO> list = new ArrayList<SaleReturnVO>();
			SaleReturnVO saleReturnVO = null;

			Connection con = null;
			PreparedStatement pstmt = null;
			ResultSet rs = null;

			try {
				Class.forName("com.mysql.jdbc.Driver");
				con = DriverManager.getConnection(dbURL, dbUserName, dbPassword);
				pstmt = con.prepareStatement(sp_select_sale_return_byreturndate);
				pstmt.setString(1, group_id);
				pstmt.setDate(2, return_start_date);
				pstmt.setDate(3, return_end_date);
				rs = pstmt.executeQuery();
				while (rs.next()) {
					saleReturnVO = new SaleReturnVO();
					saleReturnVO.setSale_id(rs.getString("sale_id"));
					saleReturnVO.setSeq_no(rs.getString("seq_no"));
					saleReturnVO.setOrder_no(rs.getString("order_no"));
					saleReturnVO.setProduct_name(rs.getString("product_name"));
					saleReturnVO.setC_product_id(rs.getString("c_product_id"));
					saleReturnVO.setQuantity(rs.getInt("quantity"));
					saleReturnVO.setPrice(rs.getFloat("price"));
					saleReturnVO.setInvoice(rs.getString("invoice"));
					saleReturnVO.setInvoice_date(rs.getDate("invoice_date"));
					saleReturnVO.setTrans_list_date(rs.getDate("trans_list_date"));
					saleReturnVO.setDis_date(rs.getDate("dis_date"));
					saleReturnVO.setMemo(rs.getString("memo"));
					saleReturnVO.setSale_date(rs.getDate("sale_date"));
					saleReturnVO.setOrder_source(rs.getString("order_source"));
					list.add(saleReturnVO); // Store the row in the list
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
		public void deleteSaleReturnTotDB(String sale_id, String user_id) {
			// TODO Auto-generated method stub
			Connection con = null;
			PreparedStatement pstmt = null;
			try {
				Class.forName("com.mysql.jdbc.Driver");
				con = DriverManager.getConnection(dbURL, dbUserName, dbPassword);
				pstmt = con.prepareStatement(sp_del_sale_return);

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
		public List<SaleReturnVO> searchDisDateDB(String group_id, String dis_start_date, String dis_end_date) {
			// TODO Auto-generated method stub
			List<SaleReturnVO> list = new ArrayList<SaleReturnVO>();
			SaleReturnVO saleReturnVO = null;

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
					saleReturnVO = new SaleReturnVO();
					saleReturnVO.setSeq_no(rs.getString("seq_no"));
					saleReturnVO.setOrder_no(rs.getString("order_no"));
					saleReturnVO.setProduct_name(rs.getString("product_name"));
					saleReturnVO.setC_product_id(rs.getString("c_product_id"));
					saleReturnVO.setQuantity(rs.getInt("quantity"));
					saleReturnVO.setPrice(rs.getFloat("price"));
					saleReturnVO.setInvoice(rs.getString("invoice"));
					saleReturnVO.setInvoice_date(rs.getDate("invoice_date"));
					saleReturnVO.setTrans_list_date(rs.getDate("trans_list_date"));
					saleReturnVO.setDis_date(rs.getDate("dis_date"));
					saleReturnVO.setMemo(rs.getString("memo"));
					saleReturnVO.setSale_date(rs.getDate("sale_date"));
					saleReturnVO.setOrder_source(rs.getString("order_source"));
					list.add(saleReturnVO); // Store the row in the list
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
		public List<SaleReturnVO> searchTransListDateDB(String group_id, String trans_list_start_date, String trans_list_end_date) {
			// TODO Auto-generated method stub
			List<SaleReturnVO> list = new ArrayList<SaleReturnVO>();
			SaleReturnVO saleReturnVO = null;

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
					saleReturnVO = new SaleReturnVO();
					saleReturnVO.setSeq_no(rs.getString("seq_no"));
					saleReturnVO.setOrder_no(rs.getString("order_no"));
					saleReturnVO.setProduct_name(rs.getString("product_name"));
					saleReturnVO.setC_product_id(rs.getString("c_product_id"));
					saleReturnVO.setQuantity(rs.getInt("quantity"));
					saleReturnVO.setPrice(rs.getFloat("price"));
					saleReturnVO.setInvoice(rs.getString("invoice"));
					saleReturnVO.setInvoice_date(rs.getDate("invoice_date"));
					saleReturnVO.setTrans_list_date(rs.getDate("trans_list_date"));
					saleReturnVO.setDis_date(rs.getDate("dis_date"));
					saleReturnVO.setMemo(rs.getString("memo"));
					saleReturnVO.setSale_date(rs.getDate("sale_date"));
					saleReturnVO.setOrder_source(rs.getString("order_source"));
					list.add(saleReturnVO); // Store the row in the list
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
