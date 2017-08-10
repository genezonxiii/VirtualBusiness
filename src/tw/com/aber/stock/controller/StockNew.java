package tw.com.aber.stock.controller;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import tw.com.aber.sf.vo.Header;
import tw.com.aber.sf.vo.RTInventory;
import tw.com.aber.sf.vo.ResponseUtil;
import tw.com.aber.sftransfer.controller.SfApi;
import tw.com.aber.sftransfer.controller.ValueService;
import tw.com.aber.util.Util;
import tw.com.aber.vo.LocationVO;
import tw.com.aber.vo.ProductVO;
import tw.com.aber.vo.StockNewVO;
import tw.com.aber.vo.WarehouseVO;

public class StockNew extends HttpServlet {

	private static final long serialVersionUID = 1L;
	private static final Logger logger = LogManager.getLogger(StockNew.class);

	// protected void doGet(HttpServletRequest request, HttpServletResponse
	// response)
	// throws ServletException, IOException {
	// response.sendRedirect("./login.jsp");
	// }

	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		request.setCharacterEncoding("UTF-8");
		response.setCharacterEncoding("UTF-8");

		String responseStr = null;
		Gson gson = null;
		Util util = new Util();

		util.ConfirmLoginAgain(request, response);

		String action = request.getParameter("action");
		String group_id = (String) request.getSession().getAttribute("group_id");
		String user_id = (String) request.getSession().getAttribute("user_id");
		
		logger.debug("action:" + action);
		logger.debug("group_id:" + group_id);
		logger.debug("user_id:" + user_id);

		if ("getStockNewListBySupplyName".equals(action)) {
			String supply_name = request.getParameter("supply_name");
			StockNewService stockNewService = new StockNewService();
			List<StockNewVO> stockNewList = stockNewService.getStockNewListBySupplyName(group_id, supply_name);

			gson = new GsonBuilder().setDateFormat("yyyy-MM-dd").create();
			responseStr = gson.toJson(stockNewList);
			response.getWriter().write(responseStr);

			logger.debug(responseStr);
		}
		if ("getStockNewListByStockTime".equals(action)) {
			String startDate = request.getParameter("startDate");
			String endDate = request.getParameter("endDate");
			StockNewService stockNewService = new StockNewService();
			List<StockNewVO> stockNewList = stockNewService.getStockNewListByStockTime(group_id, startDate, endDate);

			gson = new GsonBuilder().setDateFormat("yyyy-MM-dd").create();
			responseStr = gson.toJson(stockNewList);
			response.getWriter().write(responseStr);
			logger.debug(responseStr);
		}

		if ("rtInventoryQueryService".equals(action)) {

			String stock_ids = request.getParameter("stock_ids");
			String inventory_status = request.getParameter("inventory_status");
			StockNewService stockNewService = new StockNewService();

			List<StockNewVO> stockNewList = stockNewService.getStockNewListByStockIDs("'" + group_id + "'", stock_ids);
			SfApi sfApi = new SfApi();

			ValueService valueService = util.getValueService(request, response);

			logger.debug("stockNewList.size():" + stockNewList.size());
			logger.debug("valueService:" + valueService);
			logger.debug("inventory_status:" + inventory_status);

			String reqXml = sfApi.genRtInventoryQueryService(stockNewList, valueService, inventory_status);
			String resXml = sfApi.sendXML(reqXml);

			ResponseUtil responseUtil = sfApi.getResponseUtilObj(resXml);
			
			stockNewService.upInventoryQuery(responseUtil, group_id);
			
			gson = new GsonBuilder().setDateFormat("yyyy-MM-dd").create();
			String gresult = gson.toJson(responseUtil);
			response.getWriter().write(gresult);
		}

	}

	interface StockNew_interface {
		public List<StockNewVO> getStockNewListBySupplyName(String group_id, String supplyName);

		public List<StockNewVO> getStockNewListByStockTime(String group_id, String StockTimeStart, String StockTimeEnd);

		public List<StockNewVO> getStockNewListByStockIDs(String group_id, String stock_ids);
		
		public Boolean upInventoryQuery(String group_id,String c_product_id,Integer totalQtySum);


	}

	class StockNewDAO implements StockNew_interface {

		private static final String sp_get_stock_new_list_by_supplyname = "call sp_get_stock_new_list_by_supplyname (?,?)";
		private static final String sp_get_stock_new_List_by_stock_time = "call sp_get_stock_new_List_by_stock_time (?,?,?)";
		private static final String sp_get_stock_new_List_by_stock_ids = "call sp_get_stock_new_List_by_stock_ids (?,?)";
		private static final String sp_update_stock_new_by_response = "call sp_update_stock_new_by_response (?,?,?)";


		private final String dbURL = getServletConfig().getServletContext().getInitParameter("dbURL")
				+ "?useUnicode=true&characterEncoding=utf-8&useSSL=false";
		private final String dbUserName = getServletConfig().getServletContext().getInitParameter("dbUserName");
		private final String dbPassword = getServletConfig().getServletContext().getInitParameter("dbPassword");

		@Override
		public List<StockNewVO> getStockNewListBySupplyName(String group_id, String supplyName) {
			Connection con = null;
			PreparedStatement pstmt = null;
			ResultSet rs = null;
			List<StockNewVO> stockNewVOList = new ArrayList<StockNewVO>();
			try {
				Class.forName("com.mysql.jdbc.Driver");
				con = DriverManager.getConnection(dbURL, dbUserName, dbPassword);
				pstmt = con.prepareStatement(sp_get_stock_new_list_by_supplyname);
				pstmt.setString(1, group_id);
				pstmt.setString(2, supplyName);

				logger.debug("Begin execution getStockNewListBySupplyName");
				logger.debug("group_id:" + group_id);
				logger.debug("supplyName:" + supplyName);

				rs = pstmt.executeQuery();
				while (rs.next()) {
					StockNewVO stockNewVO = new StockNewVO();
					ProductVO productVO = new ProductVO();
					LocationVO locationVO = new LocationVO();
					WarehouseVO warehouseVO = new WarehouseVO();

					stockNewVO.setGroup_id(rs.getString("group_id"));
					stockNewVO.setLocation_id(rs.getString("location_id"));
					stockNewVO.setMemo(rs.getString("memo"));
					stockNewVO.setProduct_id(rs.getString("product_id"));
					stockNewVO.setQuantity(rs.getString("quantity"));
					stockNewVO.setStock_id(rs.getString("stock_id"));
					stockNewVO.setStock_time(rs.getDate("stock_time"));
					stockNewVO.setUser_id(rs.getString("user_id"));
					stockNewVO.setValid_date(rs.getDate("valid_date"));
					stockNewVO.setProductVO(productVO);
					stockNewVO.setLocationVO(locationVO);

					locationVO.setWarehouseVO(warehouseVO);
					locationVO.setLocation_code(rs.getString("location_code"));

					warehouseVO.setWarehouse_code(rs.getString("warehouse_code"));

					productVO.setC_product_id(rs.getString("c_product_id"));
					productVO.setProduct_name(rs.getString("product_name"));
					productVO.setSupply_name(rs.getString("supply_name"));
					stockNewVOList.add(stockNewVO);
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

			return stockNewVOList;
		}

		@Override
		public List<StockNewVO> getStockNewListByStockTime(String group_id, String stockTimeStart,
				String stockTimeEnd) {
			List<StockNewVO> stockNewVOList = new ArrayList<StockNewVO>();
			Connection con = null;
			PreparedStatement pstmt = null;
			ResultSet rs = null;
			try {
				Class.forName("com.mysql.jdbc.Driver");
				con = DriverManager.getConnection(dbURL, dbUserName, dbPassword);
				pstmt = con.prepareStatement(sp_get_stock_new_List_by_stock_time);

				pstmt.setString(1, group_id);
				pstmt.setString(2, stockTimeStart);
				pstmt.setString(3, stockTimeEnd);

				logger.debug("Begin execution getStockNewListByStockTime");
				logger.debug("group_id:" + group_id);
				logger.debug("StockTimeStart:" + stockTimeStart);
				logger.debug("StockTimeEnd:" + stockTimeEnd);

				rs = pstmt.executeQuery();
				while (rs.next()) {
					StockNewVO stockNewVO = new StockNewVO();
					ProductVO productVO = new ProductVO();
					LocationVO locationVO = new LocationVO();
					WarehouseVO warehouseVO = new WarehouseVO();

					stockNewVO.setGroup_id(rs.getString("group_id"));
					stockNewVO.setLocation_id(rs.getString("location_id"));
					stockNewVO.setMemo(rs.getString("memo"));
					stockNewVO.setProduct_id(rs.getString("product_id"));
					stockNewVO.setQuantity(rs.getString("quantity"));
					stockNewVO.setStock_id(rs.getString("stock_id"));
					stockNewVO.setStock_time(rs.getDate("stock_time"));
					stockNewVO.setUser_id(rs.getString("user_id"));
					stockNewVO.setValid_date(rs.getDate("valid_date"));
					stockNewVO.setProductVO(productVO);
					stockNewVO.setLocationVO(locationVO);

					locationVO.setWarehouseVO(warehouseVO);
					locationVO.setLocation_code(rs.getString("location_code"));

					warehouseVO.setWarehouse_code(rs.getString("warehouse_code"));

					productVO.setC_product_id(rs.getString("c_product_id"));
					productVO.setProduct_name(rs.getString("product_name"));
					productVO.setSupply_name(rs.getString("supply_name"));
					stockNewVOList.add(stockNewVO);
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

			return stockNewVOList;
		}

		@Override
		public List<StockNewVO> getStockNewListByStockIDs(String group_id, String stock_ids) {

			Connection con = null;
			PreparedStatement pstmt = null;
			ResultSet rs = null;
			List<StockNewVO> stockNewVOList = new ArrayList<StockNewVO>();

			try {
				Class.forName("com.mysql.jdbc.Driver");
				con = DriverManager.getConnection(dbURL, dbUserName, dbPassword);
				pstmt = con.prepareStatement(sp_get_stock_new_List_by_stock_ids);
				pstmt.setString(1, group_id);
				pstmt.setString(2, stock_ids);

				logger.debug("Begin execution getStockNewListByStockIDs");
				logger.debug("group_id: " + group_id);
				logger.debug("Stock_ids: " + stock_ids);

				rs = pstmt.executeQuery();
				while (rs.next()) {
					StockNewVO stockNewVO = new StockNewVO();
					ProductVO productVO = new ProductVO();
					LocationVO locationVO = new LocationVO();
					WarehouseVO warehouseVO = new WarehouseVO();

					stockNewVO.setGroup_id(rs.getString("group_id"));
					stockNewVO.setLocation_id(rs.getString("location_id"));
					stockNewVO.setMemo(rs.getString("memo"));
					stockNewVO.setProduct_id(rs.getString("product_id"));
					stockNewVO.setQuantity(rs.getString("quantity"));
					stockNewVO.setStock_id(rs.getString("stock_id"));
					stockNewVO.setStock_time(rs.getDate("stock_time"));
					stockNewVO.setUser_id(rs.getString("user_id"));
					stockNewVO.setValid_date(rs.getDate("valid_date"));
					stockNewVO.setProductVO(productVO);
					stockNewVO.setLocationVO(locationVO);

					locationVO.setWarehouseVO(warehouseVO);
					locationVO.setLocation_code(rs.getString("location_code"));

					warehouseVO.setWarehouse_code(rs.getString("warehouse_code"));

					productVO.setC_product_id(rs.getString("c_product_id"));
					productVO.setProduct_name(rs.getString("product_name"));
					productVO.setSupply_name(rs.getString("supply_name"));
					stockNewVOList.add(stockNewVO);
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

			return stockNewVOList;
		}

		@Override
		public Boolean upInventoryQuery(String group_id,String c_product_id,Integer totalQtySum) {

			boolean isUpdate = false;
			Connection con = null;
			PreparedStatement pstmt = null;

			try {
				Class.forName("com.mysql.jdbc.Driver");
				con = DriverManager.getConnection(dbURL, dbUserName, dbPassword);
				pstmt = con.prepareStatement(sp_update_stock_new_by_response);

				pstmt.setString(1, group_id);
				pstmt.setString(2, c_product_id);
				pstmt.setInt(3, totalQtySum);
				int value = pstmt.executeUpdate();

				logger.debug("updateReturn:" + value);

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
			isUpdate = true;
			return isUpdate;

		}

	}

	class StockNewService {
		private StockNew_interface dao;

		public StockNewService() {
			dao = new StockNewDAO();
		}

		public List<StockNewVO> getStockNewListBySupplyName(String group_id, String supplyName) {
			return dao.getStockNewListBySupplyName(group_id, supplyName);
		}

		public List<StockNewVO> getStockNewListByStockTime(String group_id, String StockTimeStart,
				String StockTimeEnd) {
			return dao.getStockNewListByStockTime(group_id, StockTimeStart, StockTimeEnd);
		}

		public List<StockNewVO> getStockNewListByStockIDs(String group_id, String stock_ids) {
			return dao.getStockNewListByStockIDs(group_id, stock_ids);
		}

		public void upInventoryQuery(ResponseUtil responseUtil, String group_id) {
			// 需代 group_id
			String skuNoTemp = "";
			String skuNoNow = "";

			int totalQtySum = 0;
			int totalQty = 0;

			// 驗證
			if (responseUtil == null || responseUtil.getResponse() == null
					|| responseUtil.getResponse().getHead() == null) {
				return;
			}
			String head = responseUtil.getResponse().getHead();
			if (!("OK".equals(head) || "PART".equals(head))) {
				return;
			}

			List<RTInventory> rtInvList = responseUtil.getResponse().getBody().getRtInventoryQueryResponse()
					.getRtInventorys().getRtiList();

			// 因為直接對rtInvList做排序 刪除等 會影響到原本的資料 所以另外copy 一個一模一樣的list 來做動作
			List<RTInventory> rtInvListCopy = new ArrayList<RTInventory>(
					Arrays.asList(new RTInventory[rtInvList.size()]));

			Collections.copy(rtInvListCopy, rtInvList);

			List<RTInventory> delrtInvListCopy = new ArrayList<RTInventory>();

			// 紀錄失敗的的
			for (int i = 0; i < rtInvListCopy.size(); i++) {
				RTInventory rTInventory = rtInvListCopy.get(i);
				Header header = rTInventory.getHeader();
				if ((!"1".equals(rTInventory.getResult())) || header == null) {
					delrtInvListCopy.add(rTInventory);
				}
			}

			// 移除失敗的
			for (int i = 0; i < delrtInvListCopy.size(); i++) {
				rtInvListCopy.remove(delrtInvListCopy.get(i));
			}

			// 排序 c_product_id
			if (rtInvListCopy.size() > 1) {
				Collections.sort(rtInvListCopy, new Comparator<RTInventory>() {
					@Override
					public int compare(RTInventory arg0, RTInventory arg1) {
						return arg0.getHeader().getSkuNo().compareTo(arg1.getHeader().getSkuNo());
					}
				});
			}

			for (int i = 0; i < rtInvListCopy.size(); i++) {
				RTInventory rTInventory = rtInvListCopy.get(i);
				Header header = rTInventory.getHeader();

				skuNoNow = header.getSkuNo();
				totalQty = Integer.valueOf(header.getTotalQty());

				if (i == 0) {
					skuNoTemp = skuNoNow;
				}
				if (!skuNoTemp.equals(skuNoNow)) {
					logger.debug("c_product_id:" + skuNoTemp);
					logger.debug("totalQtySum:" + totalQtySum);

					dao.upInventoryQuery(group_id, skuNoTemp, totalQtySum);

					skuNoTemp = skuNoNow;
					totalQtySum = 0;
				}

				totalQtySum = totalQtySum + totalQty;

				if (i == rtInvListCopy.size() - 1) {
					logger.debug("c_product_id:" + skuNoTemp);
					logger.debug("totalQtySum:" + totalQtySum);
					dao.upInventoryQuery(group_id, skuNoTemp, totalQtySum);
				}

			}

		}

	}
	
}
