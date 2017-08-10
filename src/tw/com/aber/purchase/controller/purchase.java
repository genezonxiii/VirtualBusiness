package tw.com.aber.purchase.controller;

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
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.JSONObject;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import tw.com.aber.sf.vo.ResponseUtil;
import tw.com.aber.sftransfer.controller.SfApi;
import tw.com.aber.sftransfer.controller.ValueService;
import tw.com.aber.util.Util;
import tw.com.aber.vo.ProductVO;
import tw.com.aber.vo.PurchaseDetailVO;
import tw.com.aber.vo.PurchaseVO;
import tw.com.aber.vo.SupplyVO;

public class purchase extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private static final Logger logger = LogManager.getLogger(purchase.class);

	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		request.setCharacterEncoding("UTF-8");
		response.setCharacterEncoding("UTF-8");
		PurchaseService purchaseService = null;
		String action = request.getParameter("action");
		
		Util util =new Util();
		
		util.ConfirmLoginAgain(request, response);
		
		String group_id = (String)request.getSession().getAttribute("group_id");
		String user_id = (String)request.getSession().getAttribute("user_id");
		
		logger.debug("Action:" + action);
		logger.debug("group_id:" + group_id);
		logger.debug("user_id:" + user_id);
	
		if ("get_supply_name".equals(action)) {

			String supply_id = request.getParameter("supply_id");
			String dbURL = getServletConfig().getServletContext().getInitParameter("dbURL")
					+ "?useUnicode=true&characterEncoding=utf-8&useSSL=false";
			String dbUserName = getServletConfig().getServletContext().getInitParameter("dbUserName");
			String dbPassword = getServletConfig().getServletContext().getInitParameter("dbPassword");
			Connection con = null;
			PreparedStatement pstmt = null;
			ResultSet rs = null;
			try {
				Class.forName("com.mysql.jdbc.Driver");
				con = DriverManager.getConnection(dbURL, dbUserName, dbPassword);
				pstmt = con.prepareStatement("SELECT supply_name FROM tb_supply where group_id= ? and supply_id = ?");
				pstmt.setString(1, group_id);
				pstmt.setString(2, supply_id);
				rs = pstmt.executeQuery();
				while (rs.next()) {
					response.getWriter().write(rs.getString("supply_name"));
				}

				// Handle any driver errors
			} catch (Exception e) {
				System.out.println(e.toString());
			}
		}

		if ("insert_detail".equals(action)) {
			try {
				String purchase_id = request.getParameter("purchase_id");
				String product_id = request.getParameter("product_id");
				String c_product_id = request.getParameter("c_product_id");
				String product_name = request.getParameter("product_name");
				String memo = request.getParameter("memo");
				
				logger.debug("purchase_id:" + purchase_id);
				logger.debug("product_id:" + product_id);
				logger.debug("c_product_id:" + c_product_id);
				logger.debug("product_name:" + product_name);
				logger.debug("memo:" + memo);

				Integer quantity = 0;
				try {
					String quantityStr = request.getParameter("quantity");
					logger.debug("quantityStr:" + quantityStr);
					if (quantityStr.trim().length() != 0) {
						quantity = Integer.valueOf(quantityStr);
					}
				} catch (Exception e) {

					e.printStackTrace();
				}
				Float cost = null;
				try {
					String costStr = request.getParameter("cost");
					
					logger.debug("costStr:" + costStr);
					
					if (costStr.trim().length() != 0) {
						cost = Float.valueOf(costStr);
					}
				} catch (Exception e) {

					e.printStackTrace();
				}

				purchaseService = new PurchaseService();
				
				Map<String, Object> map = new HashMap<>();
				
				
				if (!purchaseService.checkAccountPayable(group_id, purchase_id)) {
					//未付款
					if (!purchaseService.checkPurchase(purchase_id)) {
						//未轉入驗收
						purchaseService.addPurchaseDetail(purchase_id, group_id, user_id, product_id, c_product_id,
								product_name, quantity, cost, memo);
					} else {
						map.put("note", "已轉入驗收，明細資料不可新增！");
					}
				} else {
					map.put("note", "已付款，明細資料不可新增！");
				}

				List<PurchaseDetailVO> list = purchaseService.getSearchAllPurchaseDetail(purchase_id);
				map.put("detail", list);

				Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd").create();
				String jsonList = gson.toJson(map);
				response.getWriter().write(jsonList);
			} catch (Exception e) {

				e.printStackTrace();
			}
		}
		if ("delete_detail".equals(action)) {
			try {
				String purchaseDetail_id = request.getParameter("purchaseDetail_id");
				String purchase_id = request.getParameter("purchase_id");

				logger.debug("purchaseDetail_id:" + purchaseDetail_id);

				purchaseService = new PurchaseService();
				Map<String, Object> map = new HashMap<>();

				if (!purchaseService.checkAccountPayable(group_id, purchase_id)) {
					//未付款
					if (!purchaseService.checkPurchase(purchase_id)) {
						// 未轉入驗收
						purchaseService.deletePurchaseDetail(purchaseDetail_id);
					} else {
						map.put("note", "已轉入驗收，明細資料不可刪除！");
					}
				} else {
					map.put("note", "已付款，明細資料不可刪除！");
				}

				List<PurchaseDetailVO> list = purchaseService.getSearchAllPurchaseDetail(purchase_id);
				map.put("detail", list);

				Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd").create();
				String jsonList = gson.toJson(map);
				response.getWriter().write(jsonList);
			} catch (Exception e) {

				e.printStackTrace();
			}
		}
		if ("update_detail".equals(action)) {
			try {
				String purchaseDetail_id = request.getParameter("purchaseDetail_id");
				String purchase_id = request.getParameter("purchase_id");
				String product_id = request.getParameter("product_id");
				String c_product_id = request.getParameter("c_product_id");
				String product_name = request.getParameter("product_name");
				
				logger.debug("purchase_id:" + purchase_id);
				logger.debug("product_id:" + product_id);
				logger.debug("c_product_id:" + c_product_id);
				logger.debug("product_name:" + product_name);
				logger.debug("purchaseDetail_id:" + purchaseDetail_id);
				
				
				Integer quantity = 0;
				try {
					String quantityStr = request.getParameter("quantity");
					logger.debug("quantity:" + quantity);

					if (quantityStr.trim().length() != 0) {
						quantity = Integer.valueOf(quantityStr);
					}
				} catch (Exception e) {

					e.printStackTrace();
				}
				String memo = request.getParameter("memo");
				logger.debug("memo:" + memo);
				Float cost = null;
				try {
					String costStr = request.getParameter("cost");
					if (costStr.trim().length() != 0) {
						cost = Float.valueOf(costStr);
						logger.debug("costStr:" + costStr);
					}
				} catch (Exception e) {

					e.printStackTrace();
				}
				
				purchaseService = new PurchaseService();
				Map<String, Object> map = new HashMap<>();
				
				if (!purchaseService.checkAccountPayable(group_id, purchase_id)) {
					//未付款
					if (!purchaseService.checkPurchase(purchase_id)) {
						// 未轉入驗收
						purchaseService.updatePurchaseDetail(purchaseDetail_id, purchase_id, group_id, user_id,
								product_id, c_product_id, product_name, quantity, cost, memo);
					} else {
						map.put("note", "已轉入驗收，明細資料不可修改！");
					}
				} else {
					map.put("note", "已付款，明細資料不可修改！");
				}
				
				List<PurchaseDetailVO> list = purchaseService.getSearchAllPurchaseDetail(purchase_id);
				map.put("detail", list);
				Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd").create();
				String jsonList = gson.toJson(map);
				response.getWriter().write(jsonList);
			} catch (Exception e) {

				e.printStackTrace();
			}
		}
		if ("search_product_data".equals(action)) {
			String term = request.getParameter("term");
			String identity = request.getParameter("identity");
			
			logger.debug("term:" + term);
			logger.debug("identity:" + identity);

			if ("ID".equals(identity)) {
				purchaseService = new PurchaseService();
				List<ProductVO> list = purchaseService.getSearchProductById(group_id, term);
				Gson gson = new Gson();
				String jsonStrList = gson.toJson(list);
				response.getWriter().write(jsonStrList);
				return;// 程式中斷
			}
			if ("NAME".equals(identity)) {
				purchaseService = new PurchaseService();
				List<ProductVO> list = purchaseService.getSearchProductByName(group_id, term);
				Gson gson = new Gson();
				String jsonStrList = gson.toJson(list);
				response.getWriter().write(jsonStrList);
				return;// 程式中斷
			}
		}
		// 明細
		if ("select_all_purchasedetail".equals(action)) {
			String purchase_id = request.getParameter("purchase_id");
			purchaseService = new PurchaseService();
			List<PurchaseDetailVO> list = purchaseService.getSearchAllPurchaseDetail(purchase_id);
			
			Map<String, Object> map = new HashMap<>();
			map.put("detail", list);
			
			Gson gson = new Gson();
			String jsonStrList = gson.toJson(map);
			response.getWriter().write(jsonStrList);
			return;// 程式中斷
		}
		// 新增時的auto complete
		if ("search_supply_name".equals(action)) {
			String term = request.getParameter("term");
			
			logger.debug("term:" + term);
			purchaseService = new PurchaseService();
			List<SupplyVO> list = purchaseService.getSupplyname(group_id, term);
			Gson gson = new Gson();
			String jsonStrList = gson.toJson(list);
			response.getWriter().write(jsonStrList);
			return;// 程式中斷
		}
		if ("delete".equals(action)) {
			try {
				/***************************
				 * 1.接收請求參數
				 ***************************************/
				String purchase_id = request.getParameter("purchase_id");
				List<PurchaseVO> salelist = null;
				logger.debug("purchase_id:" + purchase_id);
				purchaseService = new PurchaseService();

				if (!purchaseService.checkAccountPayable(group_id, purchase_id)) {
					// 未付款

					/***************************
					 * 2.開始刪除資料
					 ***************************************/
					String result = purchaseService.deletePurchase(purchase_id, user_id);
					/***************************
					 * 3.刪除完成,準備轉交(Send the Success view)
					 ***********/
					purchaseService = new PurchaseService();
					salelist = purchaseService.getSearchAllDB(group_id);
					PurchaseVO purchaseVO = new PurchaseVO();
					purchaseVO.setMessage("驗證通過");
					purchaseVO.setNote(result);
					salelist.add(purchaseVO);
				} else {
					PurchaseVO purchaseVO = new PurchaseVO();
					purchaseVO.setMessage("already_paid");
					salelist = purchaseService.getSearchAllDB(group_id);
					salelist.add(purchaseVO);
				}
				Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd").create();
				String jsonStrList = gson.toJson(salelist);
				response.getWriter().write(jsonStrList);
				/*************************** 其他可能的錯誤處理 **********************************/
			} catch (Exception e) {

				e.printStackTrace();
			}
		}
		if ("search_purchase_date".equals(action)) {
			try {
				String start_date = request.getParameter("purchase_start_date");
				String end_date = request.getParameter("purchase_end_date");
				logger.debug("start_date:" + start_date);
				logger.debug("end_date:" + end_date);
				
				if (start_date.trim().length() != 0 & end_date.trim().length() == 0) {
					logger.debug("缺少end_date");
					List<PurchaseVO> list = new ArrayList<PurchaseVO>();
					PurchaseVO purchaseVO = new PurchaseVO();
					purchaseVO.setMessage("如要以日期查詢，請完整填寫訖日欄位");
					list.add(purchaseVO);
					Gson gson = new Gson();
					String jsonStrList = gson.toJson(list);
					response.getWriter().write(jsonStrList);
					return;// 程式中斷
				}
				if (end_date.trim().length() != 0 & start_date.trim().length() == 0) {
					logger.debug("缺少start_date");
					List<PurchaseVO> list = new ArrayList<PurchaseVO>();
					PurchaseVO purchaseVO = new PurchaseVO();
					purchaseVO.setMessage("如要以日期查詢，請完整填寫起日欄位");
					list.add(purchaseVO);
					Gson gson = new Gson();
					String jsonStrList = gson.toJson(list);
					response.getWriter().write(jsonStrList);
					return;// 程式中斷
				}
				if (start_date.trim().length() != 0 & end_date.trim().length() != 0) {
					if (DateConversionToDigital(start_date) > DateConversionToDigital(end_date)) {
						logger.debug("起日不可大於訖日");
						List<PurchaseVO> list = new ArrayList<PurchaseVO>();
						PurchaseVO purchaseVO = new PurchaseVO();
						purchaseVO.setMessage("起日不可大於訖日");
						list.add(purchaseVO);
						Gson gson = new Gson();
						String jsonStrList = gson.toJson(list);
						response.getWriter().write(jsonStrList);
						return;// 程式中斷
					} else {
						logger.debug("符合起訖日區間");
						// 查詢指定期限
						purchaseService = new PurchaseService();
						List<PurchaseVO> list = purchaseService.getSearchPurchaseDateDB(group_id, start_date, end_date);
						PurchaseVO purchaseVO = new PurchaseVO();
						purchaseVO.setMessage("驗證通過");
						list.add(purchaseVO);
						Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd").create();
						String jsonStrList = gson.toJson(list);
						response.getWriter().write(jsonStrList);
						return;// 程式中斷
					}
				}
				// 假如無查詢條件，則是查詢全部
				if (start_date.trim().length() == 0 & end_date.trim().length() == 0) {
					logger.debug("區間未填寫 - getSearchAllDB");
					purchaseService = new PurchaseService();
					List<PurchaseVO> list = purchaseService.getSearchAllDB(group_id);
					PurchaseVO purchaseVO = new PurchaseVO();
					purchaseVO.setMessage("驗證通過");
					list.add(purchaseVO);
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
		if ("insert".equals(action)) {
			try {
				String supply_id = request.getParameter("supply_id");
				String supply_name = request.getParameter("supply_name");
				String memo = request.getParameter("memo");
				String invoice = request.getParameter("invoice");
				String invoice_type = request.getParameter("invoice_type");
				SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

				java.sql.Date purchase_date = null;
				String purchase_dateStr = request.getParameter("purchase_date");
				if (purchase_dateStr.length() != 0) {
					java.util.Date purchase_date_util = sdf.parse(purchase_dateStr);
					purchase_date = new java.sql.Date(purchase_date_util.getTime());
				}

				Float amount = null;
				String amountStr = request.getParameter("amount");
				if (amountStr.length() != 0) {
					amount = Float.valueOf(amountStr);
				}

				String seq_no;
				purchaseService = new PurchaseService();
				List<PurchaseVO> list = purchaseService.getSaleSeqNo(group_id);
				if (list.size() == 0) {
					seq_no = getThisYearMonthDate() + "0001";
				} else {
					seq_no = getGenerateSeqNo(list.get(0).getSeq_no());
				}
				purchaseService = new PurchaseService();
				purchaseService.addPurchase(seq_no, group_id, user_id, supply_id, memo, purchase_date, invoice,
						invoice_type, amount);
				purchaseService = new PurchaseService();
				List<PurchaseVO> resultNameList = purchaseService.getSearchDB(group_id, supply_name);
				PurchaseVO purchaseVO = new PurchaseVO();
				purchaseVO.setMessage("驗證通過");
				resultNameList.add(purchaseVO);
				Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd").create();
				String jsonList = gson.toJson(resultNameList);
				response.getWriter().write(jsonList);
			} catch (NumberFormatException e) {

				e.printStackTrace();
			} catch (ParseException e) {

				e.printStackTrace();
			}
		}
		if ("update".equals(action)) {
			try {
				String purchase_id = request.getParameter("purchase_id");
				String seq_no = request.getParameter("seq_no");
				String supply_id = request.getParameter("supply_id");
				String supply_name = request.getParameter("supply_name");
				String memo = request.getParameter("memo");
				String invoice = request.getParameter("invoice");
				String invoice_type = request.getParameter("invoice_type");
				SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

				java.sql.Date purchase_date = null;
				String purchase_dateStr = request.getParameter("purchase_date");
				if (purchase_dateStr.length() != 0) {
					java.util.Date purchase_date_util = sdf.parse(purchase_dateStr);
					purchase_date = new java.sql.Date(purchase_date_util.getTime());
				}

				Float amount = null;
				String amountStr = request.getParameter("amount");
				if (amountStr.length() != 0) {
					amount = Float.valueOf(amountStr);
				}
				
				purchaseService = new PurchaseService();
				PurchaseVO purchaseVO = new PurchaseVO();
				purchaseVO.setMessage("驗證通過");
				
				
				
				if (!purchaseService.checkAccountPayable(group_id, purchase_id)) {
					//未付款
					if (!purchaseService.checkPurchase(purchase_id)) {
						//未轉入驗收
						purchaseService.updatePurchase(purchase_id, seq_no, group_id, user_id, supply_id, memo, purchase_date,
								invoice, invoice_type, amount);
					} else {
						purchaseVO.setNote("已轉入驗收，資料不可修改！");
					}
				} else {
					purchaseVO.setNote("已付款，資料不可修改！");
				}

				List<PurchaseVO> resultNameList = purchaseService.getSearchDB(group_id, supply_name);
				resultNameList.add(purchaseVO);

				Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd").create();
				String jsonList = gson.toJson(resultNameList);
				response.getWriter().write(jsonList);
			} catch (NumberFormatException e) {

				e.printStackTrace();
			} catch (ParseException e) {

				e.printStackTrace();
			}
		}
		if ("search".equals(action)) {
			try {
				/***************************
				 * 1.接收請求參數-格式檢查
				 ****************************************/
				String supply_name = request.getParameter("supply_name");
				logger.debug("supply_name:" + supply_name);
				
				/***************************
				 * 2.開始查詢資料
				 ****************************************/
				// 假如無查詢條件，則是查詢全部
				if (supply_name == null || (supply_name.trim()).length() == 0) {
					purchaseService = new PurchaseService();
					logger.debug("getSearchAllDB");
					List<PurchaseVO> list = purchaseService.getSearchAllDB(group_id);
					PurchaseVO purchaseVO = new PurchaseVO();
					purchaseVO.setMessage("驗證通過");
					list.add(purchaseVO);
					Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd").create();
					String jsonStrList = gson.toJson(list);
					response.getWriter().write(jsonStrList);
					return;// 程式中斷
				}
				// 查詢指定名稱
				if (supply_name != null || supply_name.trim().length() != 0) {
					purchaseService = new PurchaseService();
					logger.debug("getSearchDB - By Supply Name");
					List<PurchaseVO> list = purchaseService.getSearchDB(group_id, supply_name);
					PurchaseVO purchaseVO = new PurchaseVO();
					purchaseVO.setMessage("驗證通過");
					list.add(purchaseVO);
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

		if ("purchaseOrderService".equals(action)) {
			try {
				/***************************
				 * 1.接收請求參數
				 ***************************************/
				SfApi sfApi = new SfApi();

				String purchase_ids = request.getParameter("purchase_ids");

				purchaseService = new PurchaseService();

				logger.debug("ship_seq_nos =" + purchase_ids + ",   group_id =" + group_id);

				List<PurchaseVO> purchaseList = purchaseService.getPurchasesByPurchaseIDs("'" + group_id + "'",
						purchase_ids);

				ValueService valueService = util.getValueService(request, response);

				String reqXml = sfApi.genPurchaseOrderService(purchaseList, valueService);
				String resXml = sfApi.sendXML(reqXml);
				ResponseUtil responseUtil = sfApi.getResponseUtilObj(resXml);
				Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd").create();
				String gresult = gson.toJson(responseUtil);
				response.getWriter().write(gresult);
			} catch (Exception e) {
				e.printStackTrace();
			}

		}
		if ("cancelPurchaseOrderService".equals(action)) {
			try {
				/***************************
				 * 1.接收請求參數
				 ***************************************/
				SfApi sfApi = new SfApi();

				String purchase_ids = request.getParameter("purchase_ids");

				purchaseService = new PurchaseService();

				logger.debug("ship_seq_nos =" + purchase_ids + ",   group_id =" + group_id);

				List<PurchaseVO> purchaseList = purchaseService.getPurchasesByPurchaseIDs("'" + group_id + "'",
						purchase_ids);
				ValueService valueService = util.getValueService(request, response);
				String reqXml =sfApi.genCancelPurchaseOrderInboundQueryService(purchaseList, valueService);
				String resXml = sfApi.sendXML(reqXml);
				ResponseUtil responseUtil = sfApi.getResponseUtilObj(resXml);
				Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd").create();
				String gresult = gson.toJson(responseUtil);
				response.getWriter().write(gresult);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		if ("PurchaseOrderQueryService".equals(action)) {
			try {
				/***************************
				 * 1.接收請求參數
				 ***************************************/
				SfApi sfApi = new SfApi();

				String purchase_ids = request.getParameter("purchase_ids");

				purchaseService = new PurchaseService();

				logger.debug("ship_seq_nos =" + purchase_ids + ", group_id =" + group_id);

				List<PurchaseVO> purchaseList = purchaseService.getPurchasesByPurchaseIDs("'" + group_id + "'",
						purchase_ids);
				ValueService valueService = util.getValueService(request, response);
				String reqXml =sfApi.genPurchaseOrderInboundQueryService(purchaseList, valueService);
				String resXml = sfApi.sendXML(reqXml);
				ResponseUtil responseUtil = sfApi.getResponseUtilObj(resXml);
				Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd").create();
				String gresult = gson.toJson(responseUtil);
				response.getWriter().write(gresult);
			} catch (Exception e) {
				e.printStackTrace();
			}

		}
		
		if("importDataToAcceptByPurchaseId".equals(action)){
			try {
				Boolean isImporData = false;
				
				String result = null;
				
				String purchase_ids = request.getParameter("purchase_ids");

				purchaseService = new PurchaseService();

				logger.debug("purchase_ids =" + purchase_ids);
				logger.debug(" group_id =" + group_id);
				
				String[] purchaseIdArr = purchase_ids.split(",");
				if(purchaseIdArr.length>0){
					isImporData = purchaseService.importDataToAcceptByPurchaseId(purchaseIdArr, group_id, user_id);
				}
				
				if (isImporData) {
					result = "success";
				} else {
					result = "error";
				}

				response.getWriter().write(result);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

	}

	// 處理傳過來的日期格式，變為數字
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
		return (year + formatTime(month) + formatTime(date));
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

	// 單號製造器
	public String getGenerateSeqNo(String str) {
		str = str.substring(str.length() - 4);
		return (getThisYearMonthDate() + formatSeqNo((Integer.valueOf(str) + 1)));
	}

	interface Purchase_interface {
		public void insertDB(PurchaseVO purchaseVO);

		public void insertDetail(PurchaseDetailVO purchaseDetailVO);

		public void updateDB(PurchaseVO purchaseVO);

		public void updateDetailToDB(PurchaseDetailVO purchaseDetailVO);

		public String deleteDB(String purchase_id, String user_id);

		public void deleteDetail(String purchaseDetail_id);
		
		public boolean importDataToAcceptByPurchaseId(String[] PurchaseIdArr ,String group_id ,String user_id);

		public List<PurchaseVO> getSaleSeqNo(String group_id);

		public List<PurchaseVO> searchDB(String group_id, String supply_name);

		public List<PurchaseVO> searchAllDB(String group_id);

		public List<PurchaseDetailVO> searchAllPurchaseDetail(String purchase_id);

		public List<PurchaseVO> searchPurchaseDateDB(String group_id, String purchase_start_date,
				String purchase_end_date);

		public List<SupplyVO> getSupplyName(String group_id, String supply_name);

		public List<ProductVO> getProductByName(String group_id, String product_name);

		public List<ProductVO> getProductById(String group_id, String c_product_id);
		
		public List<PurchaseVO> getPurchasesByPurchaseIDs(String group_id,String purchase_ids);
		
		public Boolean checkPurchase(String purchase_id);
		
		public Boolean checkAccountPayable(String group_id,String purchase_id);
	}

	class PurchaseService {
		private Purchase_interface dao;

		public PurchaseService() {
			dao = new PurchaseDAO();
		}

		public String deletePurchase(String purchase_id, String user_id) {
			return dao.deleteDB(purchase_id, user_id);
		}

		public void deletePurchaseDetail(String purchaseDetail_id) {
			dao.deleteDetail(purchaseDetail_id);
		}

		public List<SupplyVO> getSupplyname(String group_id, String supply_name) {
			return dao.getSupplyName(group_id, supply_name);
		}

		public List<PurchaseVO> getSaleSeqNo(String group_id) {
			return dao.getSaleSeqNo(group_id);
		}

		public List<PurchaseVO> getSearchDB(String group_id, String supply_name) {
			return dao.searchDB(group_id, supply_name);
		}

		public List<PurchaseVO> getSearchAllDB(String group_id) {
			return dao.searchAllDB(group_id);
		}

		public List<PurchaseDetailVO> getSearchAllPurchaseDetail(String purchase_id) {
			return dao.searchAllPurchaseDetail(purchase_id);
		}

		public List<PurchaseVO> getSearchPurchaseDateDB(String group_id, String start_date, String end_date) {
			return dao.searchPurchaseDateDB(group_id, start_date, end_date);
		}

		public List<ProductVO> getSearchProductById(String group_id, String c_product_id) {
			return dao.getProductById(group_id, c_product_id);
		}

		public List<ProductVO> getSearchProductByName(String group_id, String product_name) {
			return dao.getProductByName(group_id, product_name);
		}
		
		public List<PurchaseVO> getPurchasesByPurchaseIDs(String group_id,String pruchase_id){
			return dao.getPurchasesByPurchaseIDs(group_id, pruchase_id);
		}
		
		public Boolean importDataToAcceptByPurchaseId(String [] PurchaseIdArr ,String group_id ,String user_id){
			return dao.importDataToAcceptByPurchaseId(PurchaseIdArr , group_id ,user_id);
		}
		
		public Boolean checkPurchase(String purchase_id) {
			return dao.checkPurchase(purchase_id);
		}
		
		public Boolean checkAccountPayable(String group_id,String purchase_id){
			return dao.checkAccountPayable(group_id,purchase_id);
		}

		public PurchaseVO addPurchase(String seq_no, String group_id, String user_id, String supply_id, String memo,
				Date purchase_date, String invoice, String invoice_type, Float amount) {
			PurchaseVO purchaseVO = new PurchaseVO();
			purchaseVO.setSeq_no(seq_no);
			purchaseVO.setGroup_id(group_id);
			purchaseVO.setUser_id(user_id);
			purchaseVO.setSupply_id(supply_id);
			purchaseVO.setMemo(memo);
			purchaseVO.setPurchase_date(purchase_date);
			purchaseVO.setInvoice(invoice);
			purchaseVO.setInvoice_type(invoice_type);
			purchaseVO.setAmount(amount);
			dao.insertDB(purchaseVO);
			return purchaseVO;
		}

		public PurchaseDetailVO addPurchaseDetail(String purchase_id, String group_id, String user_id,
				String product_id, String c_product_id, String product_name, Integer quantity, Float cost,
				String memo) {
			PurchaseDetailVO purchaseDetailVO = new PurchaseDetailVO();
			purchaseDetailVO.setPurchase_id(purchase_id);
			purchaseDetailVO.setGroup_id(group_id);
			purchaseDetailVO.setUser_id(user_id);
			purchaseDetailVO.setProduct_id(product_id);
			purchaseDetailVO.setC_product_id(c_product_id);
			purchaseDetailVO.setProduct_name(product_name);
			purchaseDetailVO.setQuantity(quantity);
			purchaseDetailVO.setCost(cost);
			purchaseDetailVO.setMemo(memo);
			dao.insertDetail(purchaseDetailVO);
			return purchaseDetailVO;
		}

		public PurchaseVO updatePurchase(String purchase_id, String seq_no, String group_id, String user_id,
				String supply_id, String memo, Date purchase_date, String invoice, String invoice_type, Float amount) {
			PurchaseVO purchaseVO = new PurchaseVO();
			purchaseVO.setPurchase_id(purchase_id);
			purchaseVO.setSeq_no(seq_no);
			purchaseVO.setGroup_id(group_id);
			purchaseVO.setUser_id(user_id);
			purchaseVO.setSupply_id(supply_id);
			purchaseVO.setMemo(memo);
			purchaseVO.setPurchase_date(purchase_date);
			purchaseVO.setInvoice(invoice);
			purchaseVO.setInvoice_type(invoice_type);
			purchaseVO.setAmount(amount);
			dao.updateDB(purchaseVO);
			return purchaseVO;
		}

		public PurchaseDetailVO updatePurchaseDetail(String purchaseDetail_id, String purchase_id, String group_id,
				String user_id, String product_id, String c_product_id, String product_name, Integer quantity,
				Float cost, String memo) {
			PurchaseDetailVO purchaseDetailVO = new PurchaseDetailVO();
			purchaseDetailVO.setPurchaseDetail_id(purchaseDetail_id);
			purchaseDetailVO.setPurchase_id(purchase_id);
			purchaseDetailVO.setGroup_id(group_id);
			purchaseDetailVO.setUser_id(user_id);
			purchaseDetailVO.setProduct_id(product_id);
			purchaseDetailVO.setC_product_id(c_product_id);
			purchaseDetailVO.setProduct_name(product_name);
			purchaseDetailVO.setQuantity(quantity);
			purchaseDetailVO.setCost(cost);
			purchaseDetailVO.setMemo(memo);
			dao.updateDetailToDB(purchaseDetailVO);
			return purchaseDetailVO;
		}
	}

	class PurchaseDAO implements Purchase_interface {
		private static final String sp_update_purchase = "call sp_update_purchase(?,?,?,?,?,?,?,?,?,?)";
		private static final String sp_select_purchase_bysupllyname = "call sp_select_purchase_bysupllyname(?,?)";
		private static final String sp_insert_purchase = "call sp_insert_purchase(?,?,?,?,?,?,?,?,?)";
		private static final String sp_get_purchase_seqno = "call sp_get_purchase_seqno (?)";
		private static final String sp_selectall_purchase = "call sp_selectall_purchase (?)";
		private static final String sp_get_supplyname = "call sp_get_supplyname(?,?)";
		private static final String sp_select_purchase_bypurchase_date = "call sp_select_purchase_bypurchase_date(?,?,?)";
		private static final String sp_del_purchase = "call sp_del_purchase(?)";
		private static final String sp_selectall_purchasedetail = "call sp_selectall_purchasedetail(?)";
		private static final String sp_update_purchaseDetail = "call sp_update_purchaseDetail(?,?,?,?,?,?,?,?,?,?)";
		private static final String sp_del_purchaseDetail = "call sp_del_purchaseDetail(?)";
		private static final String sp_insert_purchaseDetail = "call sp_insert_purchaseDetail(?,?,?,?,?,?,?,?,?)";
		private static final String sp_get_product_byid = "call sp_get_product_byid (?,?)";
		private static final String sp_get_product_byname = "call sp_get_product_byname (?,?)";
		private static final String sp_get_purchases_by_purchase_ids = "call sp_get_purchases_by_purchase_ids(?,?)";
		private static final String sp_import_Data_tb_accept = "call sp_import_Data_tb_accept(?,?,?)";
		private static final String sp_check_purchase = "call sp_check_purchase(?)";
		private static final String sp_check_account_payable = "call sp_check_account_payable(?,?)";

		private final String dbURL = getServletConfig().getServletContext().getInitParameter("dbURL")
				+ "?useUnicode=true&characterEncoding=utf-8&useSSL=false";
		private final String dbUserName = getServletConfig().getServletContext().getInitParameter("dbUserName");
		private final String dbPassword = getServletConfig().getServletContext().getInitParameter("dbPassword");

		@Override
		public void insertDB(PurchaseVO purchaseVO) {

			Connection con = null;
			PreparedStatement pstmt = null;
			try {
				Class.forName("com.mysql.jdbc.Driver");
				con = DriverManager.getConnection(dbURL, dbUserName, dbPassword);
				pstmt = con.prepareStatement(sp_insert_purchase);

				pstmt.setString(1, purchaseVO.getSeq_no());
				pstmt.setString(2, purchaseVO.getGroup_id());
				pstmt.setString(3, purchaseVO.getUser_id());
				pstmt.setString(4, purchaseVO.getSupply_id());
				pstmt.setString(5, purchaseVO.getMemo());
				pstmt.setDate(6, purchaseVO.getPurchase_date());
				pstmt.setString(7, purchaseVO.getInvoice());
				pstmt.setString(8, purchaseVO.getInvoice_type());
				pstmt.setFloat(9, purchaseVO.getAmount());

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
		public void updateDB(PurchaseVO purchaseVO) {

			Connection con = null;
			PreparedStatement pstmt = null;
			try {
				Class.forName("com.mysql.jdbc.Driver");
				con = DriverManager.getConnection(dbURL, dbUserName, dbPassword);
				pstmt = con.prepareStatement(sp_update_purchase);

				pstmt.setString(1, purchaseVO.getPurchase_id());
				pstmt.setString(2, purchaseVO.getSeq_no());
				pstmt.setString(3, purchaseVO.getGroup_id());
				pstmt.setString(4, purchaseVO.getUser_id());
				pstmt.setString(5, purchaseVO.getSupply_id());
				pstmt.setString(6, purchaseVO.getMemo());
				pstmt.setDate(7, purchaseVO.getPurchase_date());
				pstmt.setString(8, purchaseVO.getInvoice());
				pstmt.setString(9, purchaseVO.getInvoice_type());
				pstmt.setFloat(10, purchaseVO.getAmount());

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
		public String deleteDB(String purchase_id, String user_id) {
			String msg = "";
			Connection con = null;
			PreparedStatement pstmt = null;
			ResultSet rs = null;
			
			try {
				Class.forName("com.mysql.jdbc.Driver");
				con = DriverManager.getConnection(dbURL, dbUserName, dbPassword);
				pstmt = con.prepareStatement(sp_check_purchase);
				pstmt.setString(1, purchase_id);
				rs = pstmt.executeQuery();
				if (rs.next()) {
					if (!rs.getBoolean("accept_flag")){
						pstmt = con.prepareStatement(sp_del_purchase);
						pstmt.setString(1, purchase_id);
//						pstmt.setString(2, user_id);

						pstmt.executeUpdate();
						msg = "刪除成功";
					} else {
						msg = "已轉入驗收，不可刪除！";
					};
				}
				
//				pstmt = con.prepareStatement(sp_del_purchase);
//				pstmt.setString(1, purchase_id);
////				pstmt.setString(2, user_id);
//
//				pstmt.executeUpdate();

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
			
			return msg;
		}

		@Override
		public List<PurchaseVO> getSaleSeqNo(String group_id) {
			List<PurchaseVO> list = new ArrayList<PurchaseVO>();
			PurchaseVO purchaseVO = null;

			Connection con = null;
			PreparedStatement pstmt = null;
			ResultSet rs = null;

			try {
				Class.forName("com.mysql.jdbc.Driver");
				con = DriverManager.getConnection(dbURL, dbUserName, dbPassword);
				pstmt = con.prepareStatement(sp_get_purchase_seqno);
				pstmt.setString(1, group_id);
				rs = pstmt.executeQuery();
				while (rs.next()) {
					purchaseVO = new PurchaseVO();
					purchaseVO.setSeq_no(rs.getString("seq_no"));
					list.add(purchaseVO);
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
		public List<PurchaseVO> searchDB(String group_id, String supply_name) {

			List<PurchaseVO> list = new ArrayList<PurchaseVO>();
			PurchaseVO purchaseVO = null;

			Connection con = null;
			PreparedStatement pstmt = null;
			ResultSet rs = null;

			try {
				Class.forName("com.mysql.jdbc.Driver");
				con = DriverManager.getConnection(dbURL, dbUserName, dbPassword);
				pstmt = con.prepareStatement(sp_select_purchase_bysupllyname);
				pstmt.setString(1, group_id);
				pstmt.setString(2, supply_name);
				rs = pstmt.executeQuery();
				while (rs.next()) {
					purchaseVO = new PurchaseVO();
					purchaseVO.setPurchase_id(rs.getString("purchase_id"));
					purchaseVO.setSeq_no(rs.getString("seq_no"));
					purchaseVO.setGroup_id(rs.getString("group_id"));
					purchaseVO.setUser_id(rs.getString("user_id"));
					purchaseVO.setSupply_id(rs.getString("supply_id"));
					purchaseVO.setMemo(rs.getString("memo"));
					purchaseVO.setPurchase_date(rs.getDate("purchase_date"));
					purchaseVO.setInvoice(rs.getString("invoice"));
					purchaseVO.setInvoice_type(rs.getString("invoice_type"));
					purchaseVO.setAmount(rs.getFloat("amount"));
					purchaseVO.setReturn_date(rs.getDate("return_date"));
					purchaseVO.setIsreturn(rs.getBoolean("isreturn"));
					purchaseVO.setAccept_flag(rs.getBoolean("accept_flag"));
					purchaseVO.setV_supply_name(rs.getString("supply_name"));
					
					list.add(purchaseVO); // Store the row in the list
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
		public List<PurchaseVO> searchAllDB(String group_id) {
			List<PurchaseVO> list = new ArrayList<PurchaseVO>();
			PurchaseVO purchaseVO = null;

			Connection con = null;
			PreparedStatement pstmt = null;
			ResultSet rs = null;

			try {
				Class.forName("com.mysql.jdbc.Driver");
				con = DriverManager.getConnection(dbURL, dbUserName, dbPassword);
				pstmt = con.prepareStatement(sp_selectall_purchase);
				pstmt.setString(1, group_id);
				rs = pstmt.executeQuery();
				while (rs.next()) {
					purchaseVO = new PurchaseVO();
					purchaseVO.setPurchase_id(rs.getString("purchase_id"));
					purchaseVO.setSeq_no(rs.getString("seq_no"));
					purchaseVO.setGroup_id(rs.getString("group_id"));
					purchaseVO.setUser_id(rs.getString("user_id"));
					purchaseVO.setSupply_id(rs.getString("supply_id"));
					purchaseVO.setMemo(rs.getString("memo"));
					purchaseVO.setPurchase_date(rs.getDate("purchase_date"));
					purchaseVO.setInvoice(rs.getString("invoice"));
					purchaseVO.setInvoice_type(rs.getString("invoice_type"));
					purchaseVO.setAmount(rs.getFloat("amount"));
					purchaseVO.setReturn_date(rs.getDate("return_date"));
					purchaseVO.setIsreturn(rs.getBoolean("isreturn"));
					purchaseVO.setAccept_flag(rs.getBoolean("accept_flag"));
					purchaseVO.setV_supply_name(rs.getString("supply_name"));
					
					list.add(purchaseVO); // Store the row in the list
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
		public List<PurchaseVO> searchPurchaseDateDB(String group_id, String purchase_start_date,
				String purchase_end_date) {

			List<PurchaseVO> list = new ArrayList<PurchaseVO>();
			PurchaseVO purchaseVO = null;

			Connection con = null;
			PreparedStatement pstmt = null;
			ResultSet rs = null;

			try {
				Class.forName("com.mysql.jdbc.Driver");
				con = DriverManager.getConnection(dbURL, dbUserName, dbPassword);
				pstmt = con.prepareStatement(sp_select_purchase_bypurchase_date);
				pstmt.setString(1, group_id);
				pstmt.setString(2, purchase_start_date);
				pstmt.setString(3, purchase_end_date);
				rs = pstmt.executeQuery();
				while (rs.next()) {
					purchaseVO = new PurchaseVO();
					purchaseVO.setPurchase_id(rs.getString("purchase_id"));
					purchaseVO.setSeq_no(rs.getString("seq_no"));
					purchaseVO.setGroup_id(rs.getString("group_id"));
					purchaseVO.setUser_id(rs.getString("user_id"));
					purchaseVO.setSupply_id(rs.getString("supply_id"));
					purchaseVO.setMemo(rs.getString("memo"));
					purchaseVO.setPurchase_date(rs.getDate("purchase_date"));
					purchaseVO.setInvoice(rs.getString("invoice"));
					purchaseVO.setInvoice_type(rs.getString("invoice_type"));
					purchaseVO.setAmount(rs.getFloat("amount"));
					purchaseVO.setReturn_date(rs.getDate("return_date"));
					purchaseVO.setIsreturn(rs.getBoolean("isreturn"));
					purchaseVO.setAccept_flag(rs.getBoolean("accept_flag"));
					purchaseVO.setV_supply_name(rs.getString("supply_name"));
					
					list.add(purchaseVO); // Store the row in the list
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
		public List<SupplyVO> getSupplyName(String group_id, String supply_name) {
			List<SupplyVO> list = new ArrayList<SupplyVO>();
			SupplyVO supplyVO = null;

			Connection con = null;
			PreparedStatement pstmt = null;
			ResultSet rs = null;

			try {
				Class.forName("com.mysql.jdbc.Driver");
				con = DriverManager.getConnection(dbURL, dbUserName, dbPassword);
				pstmt = con.prepareStatement(sp_get_supplyname);
				pstmt.setString(1, group_id);
				pstmt.setString(2, supply_name);
				rs = pstmt.executeQuery();
				while (rs.next()) {
					supplyVO = new SupplyVO();
					supplyVO.setSupply_id(rs.getString("supply_id"));
					supplyVO.setSupply_name(rs.getString("supply_name"));
					list.add(supplyVO); // Store the row in the list
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
		public List<PurchaseDetailVO> searchAllPurchaseDetail(String purchase_id) {

			List<PurchaseDetailVO> list = new ArrayList<PurchaseDetailVO>();
			PurchaseDetailVO purchaseDetailVO = null;

			Connection con = null;
			PreparedStatement pstmt = null;
			ResultSet rs = null;

			try {
				Class.forName("com.mysql.jdbc.Driver");
				con = DriverManager.getConnection(dbURL, dbUserName, dbPassword);
				pstmt = con.prepareStatement(sp_selectall_purchasedetail);
				pstmt.setString(1, purchase_id);
				rs = pstmt.executeQuery();
				while (rs.next()) {
					purchaseDetailVO = new PurchaseDetailVO();
					purchaseDetailVO.setPurchaseDetail_id(rs.getString("purchaseDetail_id"));
					purchaseDetailVO.setPurchase_id(rs.getString("purchase_id"));
					purchaseDetailVO.setGroup_id(rs.getString("group_id"));
					purchaseDetailVO.setUser_id(rs.getString("user_id"));
					purchaseDetailVO.setProduct_id(rs.getString("product_id"));
					purchaseDetailVO.setC_product_id(rs.getString("c_product_id"));
					purchaseDetailVO.setProduct_name(rs.getString("product_name"));
					purchaseDetailVO.setQuantity(rs.getInt("quantity"));
					purchaseDetailVO.setCost(rs.getFloat("cost"));
					purchaseDetailVO.setMemo(rs.getString("memo"));
					purchaseDetailVO.setReturn_date(rs.getDate("return_date"));
					purchaseDetailVO.setIsreturn(rs.getBoolean("isreturn"));
					list.add(purchaseDetailVO);
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
		public void updateDetailToDB(PurchaseDetailVO purchaseDetailVO) {

			Connection con = null;
			PreparedStatement pstmt = null;
			try {
				Class.forName("com.mysql.jdbc.Driver");
				con = DriverManager.getConnection(dbURL, dbUserName, dbPassword);
				pstmt = con.prepareStatement(sp_update_purchaseDetail);

				pstmt.setString(1, purchaseDetailVO.getPurchaseDetail_id());
				pstmt.setString(2, purchaseDetailVO.getPurchase_id());
				pstmt.setString(3, purchaseDetailVO.getGroup_id());
				pstmt.setString(4, purchaseDetailVO.getUser_id());
				pstmt.setString(5, purchaseDetailVO.getProduct_id());
				pstmt.setString(6, purchaseDetailVO.getC_product_id());
				pstmt.setString(7, purchaseDetailVO.getProduct_name());
				pstmt.setInt(8, purchaseDetailVO.getQuantity());
				pstmt.setFloat(9, purchaseDetailVO.getCost());
				pstmt.setString(10, purchaseDetailVO.getMemo());

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
		public void deleteDetail(String purchaseDetail_id) {

			Connection con = null;
			PreparedStatement pstmt = null;
			try {
				Class.forName("com.mysql.jdbc.Driver");
				con = DriverManager.getConnection(dbURL, dbUserName, dbPassword);
				pstmt = con.prepareStatement(sp_del_purchaseDetail);
				pstmt.setString(1, purchaseDetail_id);

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
		public void insertDetail(PurchaseDetailVO purchaseDetailVO) {

			Connection con = null;
			PreparedStatement pstmt = null;
			try {
				Class.forName("com.mysql.jdbc.Driver");
				con = DriverManager.getConnection(dbURL, dbUserName, dbPassword);
				pstmt = con.prepareStatement(sp_insert_purchaseDetail);

				pstmt.setString(1, purchaseDetailVO.getPurchase_id());
				pstmt.setString(2, purchaseDetailVO.getGroup_id());
				pstmt.setString(3, purchaseDetailVO.getUser_id());
				pstmt.setString(4, purchaseDetailVO.getProduct_id());
				pstmt.setString(5, purchaseDetailVO.getC_product_id());
				pstmt.setString(6, purchaseDetailVO.getProduct_name());
				pstmt.setInt(7, purchaseDetailVO.getQuantity());
				pstmt.setFloat(8, purchaseDetailVO.getCost());
				pstmt.setString(9, purchaseDetailVO.getMemo());

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
		public List<PurchaseVO> getPurchasesByPurchaseIDs(String group_id, String purchase_ids) {

			List<PurchaseVO> purchaseVOList = new ArrayList<PurchaseVO>();
			List<PurchaseDetailVO> purchaseDetailList = null;
			
			PurchaseVO purchaseVO = null;
			PurchaseDetailVO purchaseDetailVO = null;
			String purchase_id_now = null;
			String purchase_id_Record = null;
			
			
			Connection con = null;
			PreparedStatement pstmt = null;
			ResultSet rs = null;

			try {
				Class.forName("com.mysql.jdbc.Driver");
				con = DriverManager.getConnection(dbURL, dbUserName, dbPassword);
				pstmt = con.prepareStatement(sp_get_purchases_by_purchase_ids);
				logger.debug("group_id:"+group_id+"purchase_ids:"+purchase_ids);
				pstmt.setString(1, group_id);
			
				pstmt.setString(2, purchase_ids);
				rs = pstmt.executeQuery();
			
				while (rs.next()) {
					
					purchaseDetailVO = new PurchaseDetailVO();
					
					purchaseDetailVO.setC_product_id(rs.getString("pd_c_product_id"));
					purchaseDetailVO.setGroup_id(rs.getString("pd_group_id"));
					purchaseDetailVO.setIsreturn(rs.getBoolean("pd_isreturn"));
					purchaseDetailVO.setMemo(rs.getString("pd_memo"));
					purchaseDetailVO.setProduct_id(rs.getString("pd_product_id"));
					purchaseDetailVO.setProduct_name(rs.getString("pd_product_name"));
					purchaseDetailVO.setPurchase_id(rs.getString("pd_purchase_id"));
					purchaseDetailVO.setPurchaseDetail_id(rs.getString("pd_purchaseDetail_id"));
					
					String pd_quantity = rs.getString("pd_quantity");
				
					if (!(pd_quantity == null || "".equals(pd_quantity))){
						purchaseDetailVO.setQuantity(Integer.parseInt(pd_quantity));
					}else{
						purchaseDetailVO.setQuantity(0);
					}
					
					purchaseDetailVO.setReturn_date(rs.getDate("pd_return_date"));
					purchaseDetailVO.setUser_id(rs.getString("pd_user_id"));
					purchaseDetailVO.setCost(rs.getFloat("pd_cost"));
					
					purchase_id_now = rs.getString("p_purchase_id");
					if((!purchase_id_now.equals(purchase_id_Record))||rs.isFirst()){
						purchaseDetailList = new ArrayList<PurchaseDetailVO>();

						purchaseVO = new PurchaseVO();
						
						purchaseVO.setPurchase_id(rs.getString("p_purchase_id"));
						purchaseVO.setSeq_no(rs.getString("p_seq_no"));
						purchaseVO.setGroup_id(rs.getString("p_group_id"));
						purchaseVO.setUser_id(rs.getString("p_user_id"));
						purchaseVO.setSupply_id(rs.getString("p_supply_id"));
						purchaseVO.setMemo(rs.getString("p_memo"));
						purchaseVO.setPurchase_date(rs.getDate("p_purchase_date"));
						purchaseVO.setInvoice(rs.getString("p_invoice"));
						purchaseVO.setInvoice_type(rs.getString("p_invoice_type"));
						purchaseVO.setAmount(rs.getFloat("p_amount"));
						purchaseVO.setReturn_date(rs.getDate("p_return_date"));
						purchaseVO.setIsreturn(rs.getBoolean("p_isreturn"));
						purchaseVO.setPurchaseDetailList(purchaseDetailList);
						
						purchaseVOList.add(purchaseVO); // Store the row in the list
						purchase_id_Record = purchase_id_now;
					}
					
					purchaseDetailList.add(purchaseDetailVO);

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
			return purchaseVOList;
		}

		@Override
		public boolean importDataToAcceptByPurchaseId(String[] PurchaseIdArr, String group_id ,String user_id) {

				boolean isImportData = false;
				Connection con = null;
				PreparedStatement pstmt = null;

			try {
				Class.forName("com.mysql.jdbc.Driver");
				con = DriverManager.getConnection(dbURL, dbUserName, dbPassword);
				pstmt = con.prepareStatement(sp_import_Data_tb_accept);

				for (int i = 0; i < PurchaseIdArr.length; i++) {
					pstmt.setString(1, group_id);
					pstmt.setString(2, user_id);
					pstmt.setString(3, PurchaseIdArr[i]);
					int value= pstmt.executeUpdate();
					
					logger.debug("changeValue = "+value);
				}

			} catch (SQLException se) {
					throw new RuntimeException("A database error occured. " + se.getMessage());
				} catch (ClassNotFoundException cnfe) {
					throw new RuntimeException("A database error occured. " + cnfe.getMessage());
				} catch (Exception e) {
					throw new RuntimeException("error" + e.getMessage());
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
				isImportData = true;
				return isImportData;
			
		}
		
		@Override
		public Boolean checkPurchase(String purchase_id) {
			
			Boolean accept_flag = false;
			Connection con = null;
			PreparedStatement pstmt = null;
			ResultSet rs = null;
			
			try {
				Class.forName("com.mysql.jdbc.Driver");
				con = DriverManager.getConnection(dbURL, dbUserName, dbPassword);
				pstmt = con.prepareStatement(sp_check_purchase);
				pstmt.setString(1, purchase_id);
				rs = pstmt.executeQuery();
				if (rs.next()) {
					accept_flag = rs.getBoolean("accept_flag");
				}
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
			
			return accept_flag;
		}

		@Override
		public Boolean checkAccountPayable(String group_id, String purchase_id) {
			
			Boolean flag = false;
			Connection con = null;
			PreparedStatement pstmt = null;
			ResultSet rs = null;
			
			try {
				Class.forName("com.mysql.jdbc.Driver");
				con = DriverManager.getConnection(dbURL, dbUserName, dbPassword);
				pstmt = con.prepareStatement(sp_check_account_payable);
				pstmt.setString(1, group_id);
				pstmt.setString(2, purchase_id);
				rs = pstmt.executeQuery();
				if (rs.next()) {
					flag = rs.getBoolean("flag");
				}
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
			
			return flag;
		}



	}
}
