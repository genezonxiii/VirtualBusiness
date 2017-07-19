package tw.com.aber.sale.controller;

import java.io.IOException;
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
import tw.com.aber.util.Util;
import tw.com.aber.vo.GroupVO;
import tw.com.aber.vo.InvoiceTrackVO;
import tw.com.aber.vo.ProductVO;
import tw.com.aber.vo.SaleDetailVO;
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
				String start_date = request.getParameter("trans_list_start_date");
				String end_date = request.getParameter("trans_list_end_date");

				logger.debug("start_date:".concat(start_date));
				logger.debug("end_date:".concat(end_date));

				if (start_date.trim().length() == 0 && end_date.trim().length() == 0) {
					saleList = saleService.getSearchAllDB(group_id);
				} else if (start_date.trim().length() > 0 && end_date.trim().length() > 0) {
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
				String result="";
				String errorMsg="";
				java.sql.Date invoice_date;
				try {

					String saleIds = (String) request.getParameter("ids");
					String invoice_date_str = (String) request.getParameter("invoice_date");
					
					try {
						//設定日期格式
						SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
						//進行轉換
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
					
					//確認資料都沒有發送過
					for(int i =0;i<saleVOs.size();i++){
						String invoice=saleVOs.get(i).getInvoice();
						if(null!=invoice){
							errorMsg = "很抱歉，該訂單已有發票，不可重複發送";
							response.getWriter().write(errorMsg);
							return;
						}
					}

					// TODO 撈取發票號碼
					InvoiceTrackVO invoiceTrackVO = saleService.getInvoiceTrack(group_id, invoice_date);
					String invoiceNum = invoiceTrackVO.getInvoiceNum();
					
					logger.debug("invoiceNum: "+invoiceNum);
					
					if(invoiceNum==null ||"".equals(invoiceNum)){
						errorMsg = "發票字軌用罄，請洽系統管理員";
						response.getWriter().write(errorMsg);
						return;
					}
					
					saleService.updateSaleInvoice(saleVOs,invoiceTrackVO,invoice_date);

					String reqXml = api.genRequestForC0401(invoiceNum, saleVOs, groupVO);
					String resXml = api.sendXML(reqXml);
					Index index = api.getIndexResponse(resXml);
					result = index.getMessage();
				} catch (Exception e) {
					result = "false";
				}
				response.getWriter().write(result);
			}
		} catch (Exception e) {
			logger.error("Exception:".concat(e.getMessage()));
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

	interface sale_interface {

		public void insertDB(SaleVO SaleVO);

		public void updateDB(SaleVO SaleVO);

		public void deleteDB(String sale_id, String user_id);

		public List<SaleVO> getNewSaleSeqNo(String group_id);

		public List<ProductVO> getProductByName(String group_id, String product_name);

		public List<ProductVO> getProductById(String group_id, String c_product_id);

		public List<SaleVO> searchDB(String group_id, String c_product_id);

		public List<SaleVO> getSaleOrdernoInfoByIds(String groupId, String saleIds);

		public List<SaleVO> searchAllDB(String group_id);

		public List<SaleVO> searchTransListDateDB(String group_id, String trans_list_start_date,
				String trans_list_end_date);

		public List<SaleVO> searchDisDateDB(String group_id, String dis_start_date, String dis_end_date);

		public List<SaleDetailVO> getSaleDetail(SaleDetailVO saleDetailVO);

		public void insertDetailDB(SaleDetailVO paramVO);

		public void updateDetailDB(SaleDetailVO paramVO);

		public void deleteDetailDB(String saleDetail_id);

		public GroupVO getGroupInvoiceInfo(String groupId);
		
		public InvoiceTrackVO getInvoiceTrack(String group_id,Date invoice_num_date);
		
		public void updateSaleInvoice(List<SaleVO> SaleVOs,InvoiceTrackVO invoiceTrackVO,Date invoice_num_date);

	}

	class SaleService {
		private sale_interface dao;

		public SaleService() {
			dao = new SaleDAO();
		}

		public SaleVO addSale(SaleVO paramVO) {
			dao.insertDB(paramVO);
			return paramVO;
		}

		public SaleVO updateSale(SaleVO paramVO) {
			dao.updateDB(paramVO);
			return paramVO;
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

		public void addSaleDetail(SaleDetailVO paramVO) {
			dao.insertDetailDB(paramVO);
		}

		public void updateSaleDetail(SaleDetailVO paramVO) {
			dao.updateDetailDB(paramVO);
		}

		public void deleteSaleDetail(String saleDetail_id) {
			dao.deleteDetailDB(saleDetail_id);
		}

		public List<SaleVO> getSaleOrdernoInfoByIds(String groupId, String saleIds) {
			return dao.getSaleOrdernoInfoByIds(groupId, saleIds);
		}

		public GroupVO getGroupInvoiceInfo(String groupId) {
			return dao.getGroupInvoiceInfo(groupId);
		}
		
		public InvoiceTrackVO getInvoiceTrack(String group_id,Date invoice_num_date){
			return dao.getInvoiceTrack(group_id, invoice_num_date);
		}
		
		public void updateSaleInvoice(List<SaleVO> SaleVOs,InvoiceTrackVO invoiceTrackVO,Date invoice_num_date){
			dao.updateSaleInvoice(SaleVOs , invoiceTrackVO, invoice_num_date);

		}
	}

	class SaleDAO implements sale_interface {
		private final String jdbcDriver = getServletConfig().getServletContext().getInitParameter("jdbcDriver");
		private final String dbURL = getServletConfig().getServletContext().getInitParameter("dbURL")
				+ "?useUnicode=true&characterEncoding=utf-8&useSSL=false";
		private final String dbUserName = getServletConfig().getServletContext().getInitParameter("dbUserName");
		private final String dbPassword = getServletConfig().getServletContext().getInitParameter("dbPassword");

		// 會使用到的Stored procedure
		private static final String sp_get_sale_detail = "call sp_get_sale_detail (?)";
		private static final String sp_selectall_sale = "call sp_selectall_sale (?)";
		private static final String sp_select_sale_bycproductid = "call sp_select_sale_bycproductid (?,?)";
		private static final String sp_select_sale_bytranslistdate = "call sp_select_sale_bytranslistdate(?,?,?)";
		private static final String sp_select_sale_bydisdate = "call sp_select_sale_bydisdate(?,?,?)";
		private static final String sp_get_sale_newseqno = "call sp_get_sale_seqno(?)";
		private static final String sp_insert_sale = "call sp_insert_sale(?, ?, ?, ?, ?, ?, ?,?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
		private static final String sp_del_sale = "call sp_del_sale (?,?)";
		private static final String sp_update_sale = "call sp_update_sale (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
		private static final String sp_get_product_byid = "call sp_get_product_byid (?,?)";
		private static final String sp_get_product_byname = "call sp_get_product_byname (?,?)";
		private static final String sp_insert_saleDetail = "call sp_insert_saleDetail(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
		private static final String sp_update_saleDetail = "call sp_update_saleDetail(?, ?, ?, ?, ?)";
		private static final String sp_del_saleDetail = "call sp_del_saleDetail(?)";
		private static final String sp_get_sale_orderno_info_by_ids = "call sp_get_sale_orderno_info_by_ids(?,?)";
		private static final String sp_get_group_invoice_info = "call sp_get_group_invoice_info(?)";
		private static final String sp_get_invoiceNum = "call sp_get_invoiceNum(?,?)";
		private static final String sp_update_sale_invoice = "call sp_update_sale_invoice(?,?,?,?,?,?)";

		@Override
		public void insertDB(SaleVO saleVO) {
			Connection con = null;
			PreparedStatement pstmt = null;
			try {
				Class.forName(jdbcDriver);
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
		public void updateDB(SaleVO saleVO) {
			Connection con = null;
			PreparedStatement pstmt = null;
			try {
				Class.forName(jdbcDriver);
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
		public void deleteDB(String sale_id, String user_id) {
			Connection con = null;
			PreparedStatement pstmt = null;
			try {
				Class.forName(jdbcDriver);
				con = DriverManager.getConnection(dbURL, dbUserName, dbPassword);
				pstmt = con.prepareStatement(sp_del_sale);
				pstmt.setString(1, sale_id);
				pstmt.setString(2, user_id);

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
		public List<SaleVO> searchDB(String group_id, String c_product_id) {
			List<SaleVO> list = new ArrayList<SaleVO>();
			SaleVO saleVO = null;

			Connection con = null;
			PreparedStatement pstmt = null;
			ResultSet rs = null;
			try {
				Class.forName(jdbcDriver);
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
				Class.forName(jdbcDriver);
				con = DriverManager.getConnection(dbURL, dbUserName, dbPassword);
				pstmt = con.prepareStatement(sp_selectall_sale);
				pstmt.setString(1, group_id);
				rs = pstmt.executeQuery();
				while (rs.next()) {
					saleVO = new SaleVO();
					saleVO.setSale_id(rs.getString("sale_id"));
					saleVO.setSeq_no(rs.getString("seq_no"));
					saleVO.setOrder_no(rs.getString("order_no"));
					saleVO.setProduct_id(rs.getString("product_id"));
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
				Class.forName(jdbcDriver);
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

					list.add(saleVO);
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
				Class.forName(jdbcDriver);
				con = DriverManager.getConnection(dbURL, dbUserName, dbPassword);
				pstmt = con.prepareStatement(sp_get_sale_newseqno);
				pstmt.setString(1, group_id);
				rs = pstmt.executeQuery();
				while (rs.next()) {
					saleVO = new SaleVO();
					saleVO.setSeq_no(rs.getString("seq_no"));
					list.add(saleVO);
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
				Class.forName(jdbcDriver);
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

					list.add(saleVO);
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
				Class.forName(jdbcDriver);
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
				Class.forName(jdbcDriver);
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
				Class.forName(jdbcDriver);
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
			return list;
		}

		@Override
		public void insertDetailDB(SaleDetailVO paramVO) {
			Connection con = null;
			PreparedStatement pstmt = null;
			try {
				Class.forName(jdbcDriver);
				con = DriverManager.getConnection(dbURL, dbUserName, dbPassword);
				pstmt = con.prepareStatement(sp_insert_saleDetail);

				pstmt.setString(1, paramVO.getSale_id());
				pstmt.setString(2, paramVO.getSeq_no());
				pstmt.setString(3, paramVO.getGroup_id());
				pstmt.setString(4, paramVO.getOrder_no());
				pstmt.setString(5, paramVO.getUser_id());
				pstmt.setString(6, paramVO.getProduct_id());
				pstmt.setString(7, paramVO.getProduct_name());
				pstmt.setString(8, paramVO.getC_product_id());
				pstmt.setString(9, null);// customer_id
				pstmt.setString(10, null); // name
				pstmt.setFloat(11, paramVO.getQuantity());
				pstmt.setFloat(12, paramVO.getPrice());
				pstmt.setString(13, null);// invoice
				pstmt.setDate(14, null);// Invoice_date
				pstmt.setDate(15, null);// Trans_list_date
				pstmt.setDate(16, null);// Dis_date
				pstmt.setString(17, null);// Memo
				pstmt.setDate(18, null);// Sale_date
				pstmt.setString(19, null);// Order_source

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
		public void updateDetailDB(SaleDetailVO paramVO) {
			Connection con = null;
			PreparedStatement pstmt = null;
			try {
				Class.forName(jdbcDriver);
				con = DriverManager.getConnection(dbURL, dbUserName, dbPassword);
				pstmt = con.prepareStatement(sp_update_saleDetail);

				pstmt.setString(1, paramVO.getSaleDetail_id());
				pstmt.setString(2, paramVO.getProduct_name());
				pstmt.setString(3, paramVO.getC_product_id());
				pstmt.setFloat(4, paramVO.getQuantity());
				pstmt.setFloat(5, paramVO.getPrice());

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
		public void deleteDetailDB(String saleDetail_id) {
			Connection con = null;
			PreparedStatement pstmt = null;
			try {
				Class.forName(jdbcDriver);
				con = DriverManager.getConnection(dbURL, dbUserName, dbPassword);
				pstmt = con.prepareStatement(sp_del_saleDetail);
				pstmt.setString(1, saleDetail_id);

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
		public List<SaleVO> getSaleOrdernoInfoByIds(String groupId, String saleIds) {
			List<SaleVO> list = new ArrayList<SaleVO>();
			SaleVO saleVO = null;

			Connection con = null;
			PreparedStatement pstmt = null;
			ResultSet rs = null;
			try {
				Class.forName(jdbcDriver);
				con = DriverManager.getConnection(dbURL, dbUserName, dbPassword);
				pstmt = con.prepareStatement(sp_get_sale_orderno_info_by_ids);
				pstmt.setString(1, groupId);
				pstmt.setString(2, saleIds);
				rs = pstmt.executeQuery();
				while (rs.next()) {
					saleVO = new SaleVO();
					saleVO.setGroup_id(rs.getString("group_id"));
					saleVO.setSale_id(rs.getString("sale_id"));
					saleVO.setSeq_no(rs.getString("seq_no"));
					saleVO.setOrder_no(rs.getString("order_no"));
					saleVO.setProduct_id(rs.getString("product_id"));
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
			return list;
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
		public InvoiceTrackVO getInvoiceTrack(String group_id,Date invoice_num_date)  {
			Connection con = null;
			PreparedStatement pstmt = null;
			ResultSet rs = null;
			String InvoiceNum = null;
			InvoiceTrackVO invoiceTrackVO = new InvoiceTrackVO();

			try {
				Class.forName(jdbcDriver);
				con = DriverManager.getConnection(dbURL, dbUserName, dbPassword);
				pstmt = con.prepareStatement(sp_get_invoiceNum);
				pstmt.setString(1, group_id);
				pstmt.setDate(2, invoice_num_date);
				 pstmt.execute();
				 rs = pstmt.getResultSet();
				if (rs.next()) {

					InvoiceNum = rs.getString("invoiceNum");
					if(InvoiceNum!=null){
						invoiceTrackVO.setGroup_id(group_id);
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
		public void updateSaleInvoice(List<SaleVO> SaleVOs,InvoiceTrackVO invoiceTrackVO,Date invoice_num_date) {
			Connection con = null;
			PreparedStatement pstmt = null;
			try {
				Class.forName(jdbcDriver);
				con = DriverManager.getConnection(dbURL, dbUserName, dbPassword);
				
				for(int i =0;i<SaleVOs.size();i++){
					
					pstmt = con.prepareStatement(sp_update_sale_invoice);
					pstmt.setString(1, SaleVOs.get(i).getGroup_id());
					pstmt.setString(2, SaleVOs.get(i).getSale_id());
					pstmt.setString(3, invoiceTrackVO.getInvoiceNum());
					pstmt.setString(4, invoiceTrackVO.getInvoice_type());
					pstmt.setString(5, invoiceTrackVO.getYear_month());
					pstmt.setDate(6, invoice_num_date);

					pstmt.executeUpdate();
				}
			
			

			
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

	}
}
