package tw.com.aber.stocktake.controller;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
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

import tw.com.aber.vo.LocationVO;
import tw.com.aber.vo.StockTakeDetailVO;
import tw.com.aber.vo.StockTakeVO;
import tw.com.aber.vo.WarehouseVO;

public class StockTake  extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private static final Logger logger = LogManager.getLogger(StockTake.class);
	
	
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		doPost(request, response);
	}
	

	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		request.setCharacterEncoding("UTF-8");
		response.setCharacterEncoding("UTF-8");

		String action = request.getParameter("action");
		String group_id = (String) request.getSession().getAttribute("group_id");
		String user_id = (String) request.getSession().getAttribute("user_id");
		
		String result = null;
		Gson gson = null;
		
		StocktakeSerivce serivce = new StocktakeSerivce();

		logger.debug("action: "+action);
		try {

			if ("createMaster".equals(action)) {
				boolean isSuccess = false;
				String memo = request.getParameter("memo");
				
				logger.debug("memo: "+memo);
				
				StockTakeVO stockTakeVO = new StockTakeVO();
				stockTakeVO.setGroup_id(group_id);
				stockTakeVO.setUser_id(user_id);
				stockTakeVO.setMemo(memo);
				
				isSuccess = serivce.insertStockTake(stockTakeVO);

				if (isSuccess) {
					result = "success";
				} else {
					result = "error";
				}

				response.getWriter().write(result);
				
			} else if ("getAllMaster".equals(action)) {

				logger.debug("group_id: "+group_id);
				
				List<StockTakeVO> stackTakeVOList = serivce.getAllMaster(group_id);

				gson = new GsonBuilder().setDateFormat("yyyy-MM-dd").create();

				result = gson.toJson(stackTakeVOList);
				
				logger.debug("result: "+result);
				
				response.getWriter().write(result);
				
			}else if ("getAllWarehouse".equals(action)){

				logger.debug("group_id: "+group_id);
				
				List<WarehouseVO> warehouseVOList = serivce.getAllWarehouse(group_id);

				gson = new GsonBuilder().setDateFormat("yyyy-MM-dd").create();

				result = gson.toJson(warehouseVOList);
				
				logger.debug("result: "+result);
				
				response.getWriter().write(result);
				
			}else if("getLocationDataByWarehouseCode".equals(action)){
				
				String warehouse_code = (String) request.getParameter("warehouse_code");

				logger.debug("group_id: "+group_id);
				
				logger.debug("warehouse_code: "+warehouse_code);
				
				List<LocationVO> locationVOList = serivce.getLocationDataByWarehouseCode(group_id,warehouse_code);

				gson = new GsonBuilder().setDateFormat("yyyy-MM-dd").create();

				result = gson.toJson(locationVOList);
				
				logger.debug("result: "+result);
				
				response.getWriter().write(result);
			}else if("InsterDetail".equals(action)){
				boolean isSuccess = false;
				//boolean stocktake_Flag = true;
				
				String location_ids = request.getParameter("location_ids");
				String stocktake_id = request.getParameter("stocktake_id");
				
				logger.debug("location_ids: "+location_ids);
				logger.debug("stocktake_id: "+stocktake_id);
				
				
				//庫存狀態需要未鎖定才可新增盤點明細
				//stocktake_Flag = serivce.getStockTakeVOFlag(group_id, stocktake_id);

				// 未鎖定才可新增盤點明細
				//if (!stocktake_Flag) {
					isSuccess = serivce.insertStockTakeDetail(group_id, location_ids, stocktake_id);
				//}

				if (isSuccess) {
					result = "success";
				} else {
					result = "error";
				}

				response.getWriter().write(result);

			} else if ("getStockDetailVOListByStockTake_id".equals(action)) {

				String stocktake_id = request.getParameter("stocktake_id");

				logger.debug("stocktake_id: " + stocktake_id);

				List<StockTakeDetailVO> stockTakeDetialList = 
						serivce.getStockDetailVOListByStockTake_id(group_id,stocktake_id);

				gson = new GsonBuilder().setDateFormat("yyyy-MM-dd").create();

				result = gson.toJson(stockTakeDetialList);

				logger.debug("result: " + result);
				
				response.getWriter().write(result);
			} else if ("inventory".equals(action)) {
				boolean isSuccess = false;
				boolean stocktake_Flag = false;

				String stocktake_id = request.getParameter("stocktake_id");

				String stocktakeDetail_id = request.getParameter("stocktakeDetail_id");

				String stocktake_qty_str = request.getParameter("stocktake_qty");

				Integer stocktake_qty = null;

				logger.debug("stocktakeDetail_id: " + stocktakeDetail_id);
				logger.debug("stocktake_id: " + stocktake_id);
				logger.debug("stocktake_qty_str: " + stocktake_qty_str);

				// 驗證
				if ("".equals(stocktake_id) || null == stocktake_id) {
					response.getWriter().write("error");
					return;
				}
				if ("".equals(stocktakeDetail_id) || null == stocktakeDetail_id) {
					response.getWriter().write("error");
					return;
				}
				if ("".equals(stocktake_qty_str) || null == stocktake_qty_str) {
					response.getWriter().write("請填入數字");
					return;
				}
				stocktake_qty = Integer.valueOf(stocktake_qty_str);
				if (stocktake_qty <= 0) {
					response.getWriter().write("請填入數字");
					return;
				}

				stocktake_Flag = serivce.getStockTakeVOFlag(group_id, stocktake_id);

				// 有鎖住代表可盤點
				if (stocktake_Flag) {
					StockTakeDetailVO stockTakeDetail = new StockTakeDetailVO();
					stockTakeDetail.setStocktakeDetail_id(stocktakeDetail_id);
					stockTakeDetail.setGroup_id(group_id);
					stockTakeDetail.setStocktake_qty(Integer.valueOf(stocktake_qty_str));
					isSuccess = serivce.updateStockTakeDetail(stockTakeDetail);
				}else{
					response.getWriter().write("請先點選盤點按鈕");
					return;
				}

				if (isSuccess) {
					result = "success";
				} else {
					result = "error";
				}

				response.getWriter().write(result);
			}else if("lockStocktakeByStocktake_id".equals(action)){

				boolean isSuccess = false;
				String stocktake_id = request.getParameter("stocktake_id");
				
				logger.debug("stocktake_id: "+stocktake_id);
	
				
				StockTakeVO stockTakeVO = new StockTakeVO();
				stockTakeVO.setGroup_id(group_id);
				stockTakeVO.setStocktake_id(stocktake_id);


				isSuccess = serivce.lockStocktakeByStocktake_id(stockTakeVO);

				if (isSuccess) {
					result = "success";
				} else {
					result = "error";
				}

				response.getWriter().write(result);

			}else if("unLockStocktakeByStocktake_id".equals(action)){

				boolean isSuccess = false;
				String stocktake_id = request.getParameter("stocktake_id");
				
				logger.debug("stocktake_id: "+stocktake_id);
	
				
				StockTakeVO stockTakeVO = new StockTakeVO();
				stockTakeVO.setGroup_id(group_id);
				stockTakeVO.setStocktake_id(stocktake_id);


				isSuccess = serivce.unLockStocktakeByStocktake_id(stockTakeVO);

				if (isSuccess) {
					result = "success";
				} else {
					result = "error";
				}

				response.getWriter().write(result);

			}else if("updateEndDate".equals(action)){
				boolean isSuccess = false;
				//boolean stocktake_Flag = true;
				
				String stocktake_id = request.getParameter("stocktake_id");
				
				logger.debug("stocktake_id: "+stocktake_id);
				
				
				//庫存狀態需要未鎖定才可完成盤點
				//stocktake_Flag = serivce.getStockTakeVOFlag(group_id, stocktake_id);

				// 未鎖定才可新增盤點明細
				//if (!stocktake_Flag) {
					isSuccess = serivce.updateEndDate(group_id, stocktake_id);
				//}

				if (isSuccess) {
					result = "success";
				} else {
					result = "error";
				}

				response.getWriter().write(result);

			}    
			
		} catch (Exception e) {
			logger.error("Exception:".concat(e.getMessage()));
		}
	}

	public class StocktakeSerivce {
		Stocktake_interface dao;
		StocktakeSerivce(){
			dao = new StocktakeDAO();
		}
		
		public Boolean insertStockTake(StockTakeVO stockTakeVO){
			return dao.insertStockTake(stockTakeVO);
		}
		
		public Boolean lockStocktakeByStocktake_id(StockTakeVO stockTakeVO){
			return dao.lockStocktakeByStocktake_id(stockTakeVO);
		}
		public Boolean unLockStocktakeByStocktake_id(StockTakeVO stockTakeVO){
			return dao.unLockStocktakeByStocktake_id(stockTakeVO);
		}
		

		Boolean insertStockTakeDetail(String group_id,String location_ids,String  stocktake_id){
			return dao.insertStockTakeDetail(group_id,location_ids,stocktake_id);
		}
		
		public List<StockTakeVO> getAllMaster(String group_id){
			return dao.getAllMaster(group_id);
		}
		
		public List<WarehouseVO> getAllWarehouse(String group_id){
			return dao.getAllWarehouse(group_id);
		}
		
		public List<LocationVO> getLocationDataByWarehouseCode(String group_id , String warehouse_code){
			return dao.getLocationDataByWarehouseCode(group_id,warehouse_code);
		}
		
		public List<StockTakeDetailVO> getStockDetailVOListByStockTake_id(String group_id , String stockTake_id){
			return dao.getStockDetailVOListByStockTake_id(group_id,stockTake_id);
		}
		
		public StockTakeVO getStockTakeVO(String group_id,String stocktake_id){
			return dao. getStockTakeVO( group_id, stocktake_id);
		}
		
		Boolean updateStockTakeDetail(StockTakeDetailVO stockTakeDetail) {
			return dao.updateStockTakeDetail(stockTakeDetail);
		}
		Boolean updateEndDate(String group_id,String stocktake_id) {
			return dao.updateEndDate( group_id, stocktake_id);
		}
		
		
		
		public boolean getStockTakeVOFlag(String group_id,String stocktake_id){
			boolean stockTakeVOFlag = false;

			StockTakeVO stockTakeVO = this.getStockTakeVO(group_id, stocktake_id);
			
			if(stockTakeVO!=null){
				
				stockTakeVOFlag = stockTakeVO.getStocktake_flag();
			}
			
			return stockTakeVOFlag;
		}
	}

	interface Stocktake_interface {
		Boolean insertStockTake(StockTakeVO  stockTakeVO);
		
		Boolean insertStockTakeDetail(String group_id,String location_ids,String  stocktake_id);
		
		Boolean updateStockTakeDetail(StockTakeDetailVO stockTakeDetail);
		
		Boolean lockStocktakeByStocktake_id(StockTakeVO stockTakeVO);
		
		Boolean unLockStocktakeByStocktake_id(StockTakeVO stockTakeVO);
		
		Boolean updateEndDate(String group_id,String stocktake_id);
		
		StockTakeVO getStockTakeVO(String group_id,String stocktake_id);
		
		List<StockTakeVO> getAllMaster(String group_id);
		
		List<WarehouseVO> getAllWarehouse(String group_id);
		
		List<LocationVO> getLocationDataByWarehouseCode(String group_id , String warehouse_code);
		
		List<StockTakeDetailVO> getStockDetailVOListByStockTake_id(String group_id , String stockTake_id);
		
		
	}

	class StocktakeDAO implements Stocktake_interface {
		private final String dbURL = getServletConfig().getServletContext().getInitParameter("dbURL")
				+ "?useUnicode=true&characterEncoding=utf-8&useSSL=false";
		private final String dbUserName = getServletConfig().getServletContext().getInitParameter("dbUserName");
		private final String dbPassword = getServletConfig().getServletContext().getInitParameter("dbPassword");
		private final String jdbcDriver = getServletConfig().getServletContext().getInitParameter("jdbcDriver");
		
		private static final String sp_insert_stock_take = "call sp_insert_stock_take(?,?,?)";
		private static final String sp_insert_stock_take_detail = "call sp_insert_stock_take_detail(?,?,?)";
		private static final String sp_get_all_master = "call sp_get_all_master(?)";
		private static final String sp_get_all_warehouse = "call sp_get_all_warehouse(?)";
		private static final String sp_get_location_by_warehousecode = "call sp_get_location_by_warehousecode(?,?)";
		private static final String sp_get_stockdetail_by_stocktake_id = "call sp_get_stockdetail_by_stocktake_id(?,?)";
		private static final String sp_get_stocktake_by_stocktake_id = "call sp_get_stocktake_by_stocktake_id(?,?)";
		private static final String sp_update_stock_take_detail_by_stocktake_id = "call sp_update_stock_take_detail_by_stocktake_id(?,?,?)";
		private static final String sp_update_enddate_by_stocktake_id = "call sp_update_enddate_by_stocktake_id(?,?)";
		private static final String sp_lock_stocktake_by_stocktake_id = "call sp_lock_stocktake_by_stocktake_id(?,?)";
		private static final String sp_unlock_stocktake_by_stocktake_id = "call sp_unlock_stocktake_by_stocktake_id(?,?)";
		
		
		@Override
		public Boolean insertStockTake(StockTakeVO stockTakeVO) {

			boolean isSuccess = false;
			Connection con = null;
			PreparedStatement pstmt = null;

			try {
				Class.forName(jdbcDriver);
				con = DriverManager.getConnection(dbURL, dbUserName, dbPassword);
				pstmt = con.prepareStatement(sp_insert_stock_take);

				pstmt.setString(1, stockTakeVO.getGroup_id());
				pstmt.setString(2, stockTakeVO.getUser_id());
				pstmt.setString(3, stockTakeVO.getMemo());
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
			isSuccess = true;
			return isSuccess;

		}
		
		

		@Override
		public Boolean insertStockTakeDetail(String group_id, String location_ids, String stocktake_id) {
			boolean isSuccess = false;
			Connection con = null;
			PreparedStatement pstmt = null;
			String[] location_idArr = location_ids.split("~");

			try {
				Class.forName(jdbcDriver);
				con = DriverManager.getConnection(dbURL, dbUserName, dbPassword);
				pstmt = con.prepareStatement(sp_insert_stock_take_detail);

				for (int i = 0; i < location_idArr.length; i++) {
					pstmt.setString(1, group_id);
					pstmt.setString(2, location_idArr[i]);
					pstmt.setString(3, stocktake_id);

					int value = pstmt.executeUpdate();
					logger.debug("updateReturn:" + value);
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
			isSuccess = true;
			return isSuccess;
		}


		@Override
		public List<StockTakeVO> getAllMaster(String group_id) {
			List<StockTakeVO> stockTakeVOList = new ArrayList<StockTakeVO>();
			StockTakeVO stockTakeVO = null;

			Connection con = null;
			PreparedStatement pstmt = null;
			ResultSet rs = null;
			
			try {
				Class.forName(jdbcDriver);
				con = DriverManager.getConnection(dbURL, dbUserName, dbPassword);
				pstmt = con.prepareStatement(sp_get_all_master);
				
				pstmt.setString(1, group_id);
				rs = pstmt.executeQuery();
				
			
				while(rs.next()){
					stockTakeVO = new StockTakeVO();
					stockTakeVO.setCreate_date(rs.getDate("create_date"));
					stockTakeVO.setEnd_date(rs.getDate("end_date"));
					stockTakeVO.setGroup_id(rs.getString("group_id"));
					stockTakeVO.setMemo(rs.getString("memo"));
					stockTakeVO.setSeq_no(rs.getString("seq_no"));
					stockTakeVO.setStocktake_flag(rs.getBoolean("stocktake_flag"));
					stockTakeVO.setStocktake_id(rs.getString("stocktake_id"));
					stockTakeVO.setUser_id(rs.getString("user_id"));
					
					stockTakeVOList.add(stockTakeVO);
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
			return stockTakeVOList;
		}

		@Override
		public List<WarehouseVO> getAllWarehouse(String group_id) {
			List<WarehouseVO> warehouseVOList = new ArrayList<WarehouseVO>();
			WarehouseVO warehouseVO = null;

			Connection con = null;
			PreparedStatement pstmt = null;
			ResultSet rs = null;
			
			try {
				Class.forName(jdbcDriver);
				con = DriverManager.getConnection(dbURL, dbUserName, dbPassword);
				pstmt = con.prepareStatement(sp_get_all_warehouse);
				
				pstmt.setString(1, group_id);
				rs = pstmt.executeQuery();
				
			
				while(rs.next()){
					warehouseVO = new WarehouseVO();
					warehouseVO.setGroup_id(group_id);
					warehouseVO.setSf_warehouse_code(rs.getString("sf_warehouse_code"));
					warehouseVO.setWarehouse_code(rs.getString("warehouse_code"));
					warehouseVO.setWarehouse_id(rs.getString("warehouse_id"));
					warehouseVO.setWarehouse_locate(rs.getString("warehouse_locate"));
					warehouseVO.setWarehouse_name(rs.getString("warehouse_name"));
			
					warehouseVOList.add(warehouseVO);
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
			return warehouseVOList;
		}
		
		@Override
		public List<LocationVO> getLocationDataByWarehouseCode(String group_id,String warehouse_code) {
			List<LocationVO> locationVOList = new ArrayList<LocationVO>();
			LocationVO locationVO = null;

			Connection con = null;
			PreparedStatement pstmt = null;
			ResultSet rs = null;
			
			try {
				Class.forName(jdbcDriver);
				con = DriverManager.getConnection(dbURL, dbUserName, dbPassword);
				pstmt = con.prepareStatement(sp_get_location_by_warehousecode);
				
				pstmt.setString(1, group_id);
				pstmt.setString(2, warehouse_code);
				rs = pstmt.executeQuery();
				
			
				while(rs.next()){
					locationVO = new LocationVO();
					locationVO.setGroup_id(group_id);
					locationVO.setLocation_code(rs.getString("location_code"));
					locationVO.setLocation_desc(rs.getString("location_desc"));
					locationVO.setLocation_id(rs.getString("location_id"));
					locationVO.setLocation_memo(rs.getString("location_memo"));
					locationVO.setWarehouse_id(rs.getString("warehouse_id"));
			
					locationVOList.add(locationVO);
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
			return locationVOList;
		}

		@Override
		public List<StockTakeDetailVO> getStockDetailVOListByStockTake_id(String group_id, String stockTake_id) {
			List<StockTakeDetailVO> stockTakeDetailVOList = new ArrayList<StockTakeDetailVO>();
			StockTakeDetailVO stockTakeDetailVO = null;

			Connection con = null;
			PreparedStatement pstmt = null;
			ResultSet rs = null;
			
			try {
				Class.forName(jdbcDriver);
				con = DriverManager.getConnection(dbURL, dbUserName, dbPassword);
				pstmt = con.prepareStatement(sp_get_stockdetail_by_stocktake_id);
				
				pstmt.setString(1, group_id);
				pstmt.setString(2, stockTake_id);
				rs = pstmt.executeQuery();
				
			
				while(rs.next()){
					stockTakeDetailVO = new StockTakeDetailVO();
					stockTakeDetailVO.setC_product_id(rs.getString("c_product_id"));
					stockTakeDetailVO.setGroup_id(rs.getString("group_id"));
					stockTakeDetailVO.setLocation_id(rs.getString("location_id"));
					stockTakeDetailVO.setMemo(rs.getString("memo"));
					stockTakeDetailVO.setProduct_id(rs.getString("product_id"));
					stockTakeDetailVO.setProduct_name(rs.getString("product_name"));
					stockTakeDetailVO.setQuantity(rs.getInt("quantity"));
					stockTakeDetailVO.setStocktake_id(rs.getString("stocktake_id"));
					stockTakeDetailVO.setStocktake_qty(rs.getInt("stocktake_qty"));
					stockTakeDetailVO.setStocktakeDetail_id(rs.getString("stocktakeDetail_id"));
					stockTakeDetailVO.setV_location_code(rs.getString("v_location_code"));
					stockTakeDetailVO.setV_location_desc(rs.getString("v_location_desc"));
					stockTakeDetailVO.setV_warehouse_code(rs.getString("v_warehouse_code"));
					stockTakeDetailVO.setV_warehouse_name(rs.getString("v_warehouse_name"));
					stockTakeDetailVOList.add(stockTakeDetailVO);
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
			return stockTakeDetailVOList;
		}

		@Override
		public StockTakeVO getStockTakeVO(String group_id, String stocktake_id) {
			StockTakeVO stockTakeVO = null;

			Connection con = null;
			PreparedStatement pstmt = null;
			ResultSet rs = null;
			
			try {
				Class.forName(jdbcDriver);
				con = DriverManager.getConnection(dbURL, dbUserName, dbPassword);
				pstmt = con.prepareStatement(sp_get_stocktake_by_stocktake_id);
				
				pstmt.setString(1, group_id);
				pstmt.setString(2, stocktake_id);
				rs = pstmt.executeQuery();
				
			
				if(rs.next()){
					stockTakeVO = new StockTakeVO();
					stockTakeVO.setCreate_date(rs.getDate("create_date"));
					stockTakeVO.setEnd_date(rs.getDate("end_date"));
					stockTakeVO.setGroup_id(rs.getString("group_id"));
					stockTakeVO.setMemo(rs.getString("memo"));
					stockTakeVO.setSeq_no(rs.getString("seq_no"));
					stockTakeVO.setStocktake_flag(rs.getBoolean("stocktake_flag"));
					stockTakeVO.setStocktake_id(rs.getString("stocktake_id"));
					stockTakeVO.setUser_id(rs.getString("user_id"));
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
			return stockTakeVO;
		}

		@Override
		public Boolean updateStockTakeDetail(StockTakeDetailVO stockTakeDetailVO) {

			boolean isSuccess = false;
			Connection con = null;
			PreparedStatement pstmt = null;

			try {
				Class.forName(jdbcDriver);
				con = DriverManager.getConnection(dbURL, dbUserName, dbPassword);
				pstmt = con.prepareStatement(sp_update_stock_take_detail_by_stocktake_id);

				pstmt.setString(1, stockTakeDetailVO.getGroup_id());
				pstmt.setString(2, stockTakeDetailVO.getStocktakeDetail_id());
				pstmt.setInt(3, stockTakeDetailVO.getStocktake_qty());
				
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
			isSuccess = true;
			return isSuccess;

		}
		
		
		@Override
		public Boolean lockStocktakeByStocktake_id(StockTakeVO stockTakeVO) {

			boolean isSuccess = false;
			Connection con = null;
			PreparedStatement pstmt = null;

			try {
				Class.forName(jdbcDriver);
				con = DriverManager.getConnection(dbURL, dbUserName, dbPassword);
				pstmt = con.prepareStatement(sp_lock_stocktake_by_stocktake_id);

				pstmt.setString(1, stockTakeVO.getGroup_id());
				pstmt.setString(2, stockTakeVO.getStocktake_id());
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
			isSuccess = true;
			return isSuccess;

		}

		@Override
		public Boolean unLockStocktakeByStocktake_id(StockTakeVO stockTakeVO) {

			boolean isSuccess = false;
			Connection con = null;
			PreparedStatement pstmt = null;

			try {
				Class.forName(jdbcDriver);
				con = DriverManager.getConnection(dbURL, dbUserName, dbPassword);
				pstmt = con.prepareStatement(sp_unlock_stocktake_by_stocktake_id);

				pstmt.setString(1, stockTakeVO.getGroup_id());
				pstmt.setString(2, stockTakeVO.getStocktake_id());
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
			isSuccess = true;
			return isSuccess;

		}



		@Override
		public Boolean updateEndDate(String group_id, String stocktake_id) {

			boolean isSuccess = false;
			Connection con = null;
			PreparedStatement pstmt = null;

			try {
				Class.forName(jdbcDriver);
				con = DriverManager.getConnection(dbURL, dbUserName, dbPassword);
				pstmt = con.prepareStatement(sp_update_enddate_by_stocktake_id);

				pstmt.setString(1, group_id);
				pstmt.setString(2, stocktake_id);
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
			isSuccess = true;
			return isSuccess;

		}





		
	}
}
