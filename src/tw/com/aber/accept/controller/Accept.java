package tw.com.aber.accept.controller;

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
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import tw.com.aber.vo.AcceptVO;
import tw.com.aber.vo.AcceptdetailVO;
import tw.com.aber.vo.LocationVO;
import tw.com.aber.vo.WarehouseVO;

public class Accept  extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private static final Logger logger = LogManager.getLogger(Accept.class);
	
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		doPost(request, response);
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		request.setCharacterEncoding("UTF-8");
		response.setCharacterEncoding("UTF-8");
		
		AcceptService acceptService = new AcceptService();
		
		String action = request.getParameter("action");
		String groupId = (String) request.getSession().getAttribute("group_id");
		String userId = (String) request.getSession().getAttribute("user_id");
		String result = null;
		Gson gson = null;

		logger.debug("groupId: "+groupId);
		logger.debug("userId: "+userId);
		logger.debug("Action: "+action);

		try {
			
			if("searchByDate".equals(action)){
				String startStr = request.getParameter("startDate");
				String endStr = request.getParameter("endDate");
				logger.debug("startDate:"+startStr);
				logger.debug("endDate:"+endStr);

				java.util.Date date = null;
				Date startDate = null, endDate = null;
				SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
				try {
					date = sdf.parse(startStr);
					startDate = new java.sql.Date(date.getTime());
					date = sdf.parse(endStr);
					endDate = new java.sql.Date(date.getTime());
				} catch (ParseException e) {
					logger.error("search date convert:".concat(e.getMessage()));
				}
				
				List<AcceptVO> acceptVOList=acceptService.getAcceptVOListByAcceptDate(groupId, startDate, endDate);
				
				gson = new GsonBuilder().setDateFormat("yyyy-MM-dd").create();
				Map<String, Object> map = new HashMap<>();
				//map.put("msg", "有問題");
				map.put("acceptVOList", acceptVOList);

				result = gson.toJson(map);
				
				response.getWriter().write(result);
				
				logger.debug("result: "+result);

			} else if ("getAcceptdetailVOListByAcceptId".equals(action)) {
				String accept_id = request.getParameter("accept_id");
				logger.debug("request groupId: "+groupId+ "  accept_id: "+accept_id);
				
				List<AcceptdetailVO> acceptVOList = acceptService.getAcceptdetailVOListByAcceptId(groupId, accept_id);
				
				gson = new GsonBuilder().setDateFormat("yyyy-MM-dd").create();

				result = gson.toJson(acceptVOList);

				logger.debug("response:"+result);
				
				response.getWriter().write(result);
				
			}else if ("getAcceptDatailByAcceptDetail_id".equals(action)){
				String acceptDetail_id = request.getParameter("acceptDetail_id");
				logger.debug("acceptDetail_id :"+acceptDetail_id);
				
				logger.debug("groupId :"+groupId);
				
				AcceptdetailVO acceptdetailVO = acceptService.getAcceptDatailByAcceptDetail_id(groupId, acceptDetail_id);
				
				gson = new GsonBuilder().setDateFormat("yyyy-MM-dd").create();

				result = gson.toJson(acceptdetailVO);

				logger.debug("response:"+result);
				
				response.getWriter().write(result);
				
			}else if("getDetailDialogDataByWarehouseCode".equals(action)){
				String warehouse_code = request.getParameter("warehouse_code");
				
				logger.debug("warehouse_code :"+warehouse_code);
				logger.debug("groupId :"+groupId);
				
				Map map = acceptService.getDetailDialogDataByWarehouseCode(groupId, warehouse_code);
				
				gson = new GsonBuilder().setDateFormat("yyyy-MM-dd").create();

				result = gson.toJson(map);

				logger.debug("response:"+result);
				
				response.getWriter().write(result);
				
			} else if ("upDateAcceptDetail".equals(action)) {

				// 取值
				String acceptDetail_id = request.getParameter("acceptDetail_id");
				String accept_id = request.getParameter("accept_id");
				String location_id = request.getParameter("location_id");
				String accept_qty_str = request.getParameter("accept_qty");
				Integer accept_qty = null;
				boolean isUpdate = false;

				logger.debug("acceptDetail_id :" + acceptDetail_id);
				logger.debug("location_id :" + location_id);
				logger.debug("accept_qty :" + accept_qty_str);

				// 驗證
				if (acceptDetail_id == "" || accept_qty_str == null) {
					logger.debug("error");
					response.getWriter().write("error");
					return;
				}
				accept_qty = Integer.valueOf(accept_qty_str);
				if (null == location_id || "".equals(location_id)) {
					logger.debug("儲位編號不可為空");
					response.getWriter().write("儲位編號不可為空");
					return;
				}
				if (accept_qty < 0) {
					logger.debug("數量不可為0");
					response.getWriter().write("數量不可為0");
					return;
				}
				if (acceptService.checkAccept(groupId,accept_id) ) {
					logger.debug("已轉入庫存，不可刪除！");
					response.getWriter().write("已轉入庫存，不可刪除！");
					return;
				}

				AcceptdetailVO acceptdetailVO = new AcceptdetailVO();
				acceptdetailVO.setAccept_qty(Integer.valueOf(accept_qty));
				acceptdetailVO.setLocation_id(location_id);
				acceptdetailVO.setAcceptDetail_id(acceptDetail_id);

				isUpdate = acceptService.updateAcceptDetail(acceptdetailVO);

				if (isUpdate) {
					result = "success";
				} else {
					result = "error";
				}

				response.getWriter().write(result);
				
				logger.debug("result: "+result);

			} else if ("masterDeleteByAccept_id".equals(action)) {
				String msg = "";

				// 取值
				String accept_id = request.getParameter("accept_id");
				logger.debug("accept_id:" + accept_id);

				// 驗證
				if (accept_id == "" || accept_id == null) {
					logger.debug("error");
					response.getWriter().write("error");
					return;
				}

				msg = acceptService.deleteAcceptByAccept_id(groupId,accept_id);
				
				response.getWriter().write(msg);
				
				logger.debug("msg: "+msg);

			}else if ("detailDeleteByAcceptDetail_id".equals(action)) {
				boolean isDelete = false;

				// 取值
				String accept_id = request.getParameter("accept_id");
				String acceptDetail_id = request.getParameter("acceptDetail_id");
				
				logger.debug("accept_id:"+accept_id);
				logger.debug("acceptDetail_id:"+acceptDetail_id);
				
				// 驗證
				if (acceptDetail_id == "" || acceptDetail_id == null) {
					logger.debug("error");
					response.getWriter().write("error");
					return;
				}
				if (acceptService.checkAccept(groupId,accept_id) ) {
					logger.debug("已轉入庫存，不可刪除！");
					response.getWriter().write("已轉入庫存，不可刪除！");
					return;
				}

				isDelete = acceptService.deleteAcceptDetailByAcceptDetail_id(groupId,acceptDetail_id);

				if (isDelete) {
					result = "success";
				} else {
					result = "error";
				}

				response.getWriter().write(result);
				
				logger.debug("result: "+result);

			}else if("importDataToStock".equals(action)){
				boolean isImportData = false;
				
				String accept_ids = request.getParameter("accept_ids");
				
				// 取值
				logger.debug("accept_ids: "+accept_ids);
				
				// 驗證
				if (accept_ids == "" || accept_ids == null) {
					logger.debug("error");
					response.getWriter().write("error");
					return;
				}
				
				isImportData = acceptService.importDataToStock(groupId,userId,accept_ids);

				if (isImportData) {
					result = "success";
				} else {
					result = "error";
				}

				response.getWriter().write(result);
	
				logger.debug("result: "+result);
			}

		} catch (Exception e) {
			logger.error("Exception:".concat(e.getMessage()));

		}
		
	}
	
	public class AcceptService {
		private Accept_interface dao;

		public AcceptService() {
			dao = new AcceptDAO();
		}
		public List<AcceptVO> getAcceptVOListByAcceptDate(String groupId, Date startDate, Date endDate) {
			return dao.getAcceptVOListByAcceptDate(groupId,startDate,endDate);
		}
		
		public List<AcceptdetailVO> getAcceptdetailVOListByAcceptId(String groupId, String acceptId) {
			return dao.getAcceptdetailVOListByAcceptId(groupId, acceptId);
		}
		
		public AcceptdetailVO getAcceptDatailByAcceptDetail_id(String groupId, String acceptId) {
			return dao.getAcceptDatailByAcceptDetail_id(groupId, acceptId);
		}
		
		public Map getDetailDialogDataByWarehouseCode(String groupId, String warehouse_code) {
			return dao.getDetailDialogDataByWarehouseCode(groupId, warehouse_code);
		}
		
		public Boolean updateAcceptDetail(AcceptdetailVO  acceptdetailVO) {
			return dao.updateAcceptDetail(acceptdetailVO);
		}
		public String deleteAcceptByAccept_id(String groupId, String acceptId) {
			return dao.deleteAcceptByAccept_id(groupId, acceptId);
		}
		public Boolean deleteAcceptDetailByAcceptDetail_id(String groupId, String acceptDetailId) {
			return dao.deleteAcceptDetailByAcceptDetail_id(groupId, acceptDetailId);
		}
		public Boolean importDataToStock(String groupId,String userId ,String acceptIds) {
			return dao.importDataToStock( groupId, userId , acceptIds);
		}
		public Boolean checkAccept(String group_id, String accept_id) {
			return dao.checkAccept(group_id, accept_id);
		}
		
	}
	
	interface Accept_interface {
		List<AcceptVO> getAcceptVOListByAcceptDate(String groupId, Date startDate, Date endDate);
		List<AcceptdetailVO> getAcceptdetailVOListByAcceptId(String groupId, String acceptId);
		AcceptdetailVO getAcceptDatailByAcceptDetail_id(String groupId, String acceptDetail_id);
		Map getDetailDialogDataByWarehouseCode(String groupId, String warehouse_code);
		Boolean updateAcceptDetail(AcceptdetailVO  acceptdetailVO);
		String deleteAcceptByAccept_id(String groupId, String acceptId);
		Boolean deleteAcceptDetailByAcceptDetail_id(String groupId, String acceptDetail);
		Boolean importDataToStock(String groupId,String userId ,String acceptIds);
		public Boolean checkAccept(String group_id, String accept_id);
	}							 
	
	class AcceptDAO implements Accept_interface {
		
		private final String dbURL = getServletConfig().getServletContext().getInitParameter("dbURL")
				+ "?useUnicode=true&characterEncoding=utf-8&useSSL=false";
		private final String dbUserName = getServletConfig().getServletContext().getInitParameter("dbUserName");
		private final String dbPassword = getServletConfig().getServletContext().getInitParameter("dbPassword");
		private final String jdbcDriver = getServletConfig().getServletContext().getInitParameter("jdbcDriver");
		
		private static final String sp_get_acceptvo_list_by_accept_date = "call sp_get_acceptvo_list_by_accept_date(?,?,?)";
		private static final String sp_get_acceptdetailvo_list_by_acceptId = "call sp_get_acceptdetailvo_list_by_acceptId(?,?)";
		private static final String sp_get_accept_datail_by_acceptDetail_id = "call sp_get_accept_datail_by_acceptDetail_id(?,?)";
		private static final String sp_get_detail_dialog_data_by_warehouse_code = "call sp_get_detail_dialog_data_by_warehouse_code(?,?)";
		private static final String sp_update_acceptdetail = "call sp_update_acceptdetail(?,?,?)";
		private static final String sp_delete_accept_by_accept_id = "call sp_delete_accept_by_accept_id(?,?)";
		private static final String sp_delete_accept_detail_by_acceptDetail_id = "call sp_delete_accept_detail_by_acceptDetail_id(?,?)";
		private static final String sp_importData_to_stock = "call sp_importData_to_stock(?,?,?)";
		private static final String sp_check_accept = "call sp_check_accept(?,?)";

		@Override
		public List<AcceptVO> getAcceptVOListByAcceptDate(String groupId, Date startDate, Date endDate) {
			List<AcceptVO> acceptVOList = new ArrayList<AcceptVO>();
			AcceptVO acceptVO = null;

			Connection con = null;
			PreparedStatement pstmt = null;
			ResultSet rs = null;
			
			try {
				Class.forName(jdbcDriver);
				con = DriverManager.getConnection(dbURL, dbUserName, dbPassword);
				pstmt = con.prepareStatement(sp_get_acceptvo_list_by_accept_date);
				
				pstmt.setString(1, groupId);
				pstmt.setDate(2, startDate);
				pstmt.setDate(3, endDate);
				rs = pstmt.executeQuery();
			
				while(rs.next()){
					acceptVO = new AcceptVO();
					acceptVO.setAccept_id(rs.getString("accept_id"));
					acceptVO.setGroup_id(rs.getString("group_id"));
					acceptVO.setMemo(rs.getString("memo"));
					acceptVO.setPurchase_id(rs.getString("purchase_id"));
					acceptVO.setSeq_no(rs.getString("seq_no"));
					acceptVO.setSupply_id(rs.getString("supply_id"));
					acceptVO.setUser_id(rs.getString("user_id"));
					acceptVO.setV_seq_no(rs.getString("v_seq_no"));
					acceptVO.setAccept_date(rs.getDate("accept_date"));
					acceptVO.setStock_flag(rs.getBoolean("stock_flag"));
					acceptVOList.add(acceptVO);
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
			return acceptVOList;
		}


		@Override
		public List<AcceptdetailVO> getAcceptdetailVOListByAcceptId(String groupId, String acceptId) {
			List<AcceptdetailVO> acceptdetailVOList = new ArrayList<AcceptdetailVO>();
			AcceptdetailVO acceptdetailVO = null;

			Connection con = null;
			PreparedStatement pstmt = null;
			ResultSet rs = null;
			
			try {
				Class.forName(jdbcDriver);
				con = DriverManager.getConnection(dbURL, dbUserName, dbPassword);
				pstmt = con.prepareStatement(sp_get_acceptdetailvo_list_by_acceptId);
				
				pstmt.setString(1, groupId);
				pstmt.setString(2, acceptId);
				rs = pstmt.executeQuery();
				
			
				while(rs.next()){
					acceptdetailVO = new AcceptdetailVO();
					acceptdetailVO.setAccept_id(rs.getString("accept_id"));
					acceptdetailVO.setAccept_qty(rs.getInt("accept_qty"));
					acceptdetailVO.setAcceptDetail_id(rs.getString("acceptDetail_id"));
					acceptdetailVO.setC_product_id(rs.getString("c_product_id"));
					acceptdetailVO.setGroup_id(rs.getString("group_id"));
					acceptdetailVO.setLocation_id(rs.getString("location_id"));
					acceptdetailVO.setMemo(rs.getString("memo"));
					acceptdetailVO.setProduct_id(rs.getString("product_id"));
					acceptdetailVO.setProduct_name(rs.getString("product_name"));
					acceptdetailVO.setQuantity(rs.getInt("quantity"));
					acceptdetailVO.setUser_id(rs.getString("user_id"));
					acceptdetailVO.setV_warehouse_code(rs.getString("v_warehouse_code"));
					acceptdetailVO.setV_warehouse_name(rs.getString("v_warehouse_name"));
					acceptdetailVO.setV_location_code(rs.getString("v_location_code"));
					
					acceptdetailVOList.add(acceptdetailVO);
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
			return acceptdetailVOList;
		}


		@Override
		public AcceptdetailVO getAcceptDatailByAcceptDetail_id(String groupId, String acceptDetail_id) {
			List<WarehouseVO> warehouseVOList = new ArrayList<WarehouseVO>();
			List<LocationVO> locationVOList = new ArrayList<LocationVO>();
			Map<String, Boolean> location_recordMap = new HashMap<String, Boolean>();

			WarehouseVO warehouseVO = null;
			AcceptdetailVO acceptdetailVO = null;
			LocationVO locationVO = null;
			// Location 是否記錄完畢
			//boolean isLocationOver = false;

			Connection con = null;
			PreparedStatement pstmt = null;
			ResultSet rs = null;
			String warehouse_id_record = "";
			String warehouse_id_now = "";


			try {
				Class.forName(jdbcDriver);
				con = DriverManager.getConnection(dbURL, dbUserName, dbPassword);
				pstmt = con.prepareStatement(sp_get_accept_datail_by_acceptDetail_id);

				pstmt.setString(1, groupId);
				pstmt.setString(2, acceptDetail_id);
				rs = pstmt.executeQuery();

				while (rs.next()) {

					warehouse_id_now = rs.getString("warehouse_id");

					String tb_location_location_id = rs.getString("tb_location_location_id");
					

					// 判別Location_id 是否有執行過 且location_id 未走訪完畢
					if (!isHasBeenExecuted(location_recordMap, tb_location_location_id)) {

						record_Location(location_recordMap, tb_location_location_id);

						locationVO = new LocationVO();
						locationVO.setLocation_code(rs.getString("location_code"));
						locationVO.setLocation_id(tb_location_location_id);

						locationVOList.add(locationVO);
					}

					if (warehouse_id_now != null && !warehouse_id_now.equals(warehouse_id_record)) {

						warehouseVO = new WarehouseVO();
						warehouseVO.setWarehouse_name(rs.getString("warehouse_name"));
						warehouseVO.setGroup_id(rs.getString("group_id"));
						warehouseVO.setSf_warehouse_code(rs.getString("sf_warehouse_code"));
						warehouseVO.setWarehouse_code(rs.getString("warehouse_code"));
						warehouseVO.setWarehouse_id(warehouse_id_now);
						warehouseVO.setWarehouse_locate(rs.getString("warehouse_locate"));
						warehouseVO.setWarehouse_name(rs.getString("warehouse_name"));
						warehouseVOList.add(warehouseVO);
						warehouse_id_record = warehouse_id_now;

					}

					if (rs.isLast()) {
						acceptdetailVO = new AcceptdetailVO();
						acceptdetailVO.setAccept_id(rs.getString("accept_id"));
						acceptdetailVO.setAccept_qty(rs.getInt("accept_qty"));
						acceptdetailVO.setAcceptDetail_id(rs.getString("acceptDetail_id"));
						acceptdetailVO.setC_product_id(rs.getString("c_product_id"));
						acceptdetailVO.setGroup_id(rs.getString("group_id"));
						acceptdetailVO.setLocation_id(rs.getString("location_id"));
						acceptdetailVO.setMemo(rs.getString("memo"));
						acceptdetailVO.setProduct_id(rs.getString("product_id"));
						acceptdetailVO.setProduct_name(rs.getString("product_name"));
						acceptdetailVO.setQuantity(rs.getInt("quantity"));
						acceptdetailVO.setUser_id(rs.getString("user_id"));
						acceptdetailVO.setV_warehouse_code(rs.getString("v_warehouse_code"));
						acceptdetailVO.setV_warehouse_name(rs.getString("v_warehouse_name"));
						acceptdetailVO.setV_location_code(rs.getString("v_location_code"));
						acceptdetailVO.setWarehouseVOList(warehouseVOList);
						acceptdetailVO.setLocationVOList(locationVOList);
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
			return acceptdetailVO;
		}


		@Override
		public Map getDetailDialogDataByWarehouseCode(String groupId, String warehouse_code) {
			Map hashMap = new HashMap<>();
			List<Map> locationCodeMapList = new ArrayList<Map>();
			Gson gson = new Gson();
			
			String locationId = null;
			String locationCodeStr = null;
			String warehouseNameStr = null;

			Connection con = null;
			PreparedStatement pstmt = null;
			ResultSet rs = null;
			
			try {
				Class.forName(jdbcDriver);
				con = DriverManager.getConnection(dbURL, dbUserName, dbPassword);
				pstmt = con.prepareStatement(sp_get_detail_dialog_data_by_warehouse_code);
				
				pstmt.setString(1, groupId);
				pstmt.setString(2, warehouse_code);
				rs = pstmt.executeQuery();
				
			
				while(rs.next()){
					locationId = rs.getString("location_id");
					locationCodeStr = rs.getString("location_code");
					
					Map<String, String> map = new HashMap<String, String>();
					map.put("locationCodeStr", locationCodeStr);
					map.put("location_id", locationId);
					
					locationCodeMapList.add(map);
					
					if(rs.isLast()){
						warehouseNameStr  = rs.getString("warehouse_name");
					}
					
				}
	
				hashMap.put("warehouseNameStr", warehouseNameStr);
				hashMap.put("locationCodeStrList", locationCodeMapList);
				
			} catch (SQLException se) {
				throw new RuntimeException("A database error occured. " + se.getMessage());
			} catch (ClassNotFoundException cnfe) {
				throw new RuntimeException("A database error occured. " + cnfe.getMessage());
			} catch (Exception e) {
				throw new RuntimeException("error" + e.getMessage());
			}  finally {
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
			return hashMap;
		}


		@Override
		public Boolean updateAcceptDetail(AcceptdetailVO acceptdetailVO) {

			boolean isUpdate = false;
			Connection con = null;
			PreparedStatement pstmt = null;

			try {
				Class.forName(jdbcDriver);
				con = DriverManager.getConnection(dbURL, dbUserName, dbPassword);
				pstmt = con.prepareStatement(sp_update_acceptdetail);

				
				pstmt.setInt(1, acceptdetailVO.getAccept_qty());
				pstmt.setString(2, acceptdetailVO.getLocation_id());
				pstmt.setString(3, acceptdetailVO.getAcceptDetail_id());
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
		
		@Override
		public String deleteAcceptByAccept_id(String groupId, String acceptId) {
			String msg = "刪除失敗";
			Connection con = null;
			PreparedStatement pstmt = null;
			ResultSet rs = null;
			
			try {
				Class.forName(jdbcDriver);
				con = DriverManager.getConnection(dbURL, dbUserName, dbPassword);
				pstmt = con.prepareStatement(sp_check_accept);
				pstmt.setString(1, groupId);
				pstmt.setString(2, acceptId);
				
				rs = pstmt.executeQuery();
				if (rs.next()) {
					if (!rs.getBoolean("stock_flag")){
						pstmt = con.prepareStatement(sp_delete_accept_by_accept_id);

						pstmt.setString(1, groupId);
						pstmt.setString(2, acceptId);

						int value = pstmt.executeUpdate();

						logger.debug("deleteReturn:" + value);
						msg = "刪除成功";
					} else {
						msg = "已轉入庫存，不可刪除！";
					};
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
			
			return msg;
		}


		@Override
		public Boolean deleteAcceptDetailByAcceptDetail_id(String groupId, String acceptDetail) {

			boolean isDelete = false;
			Connection con = null;
			PreparedStatement pstmt = null;

			try {
				Class.forName(jdbcDriver);
				con = DriverManager.getConnection(dbURL, dbUserName, dbPassword);
				pstmt = con.prepareStatement(sp_delete_accept_detail_by_acceptDetail_id);

				pstmt.setString(1, groupId);
				pstmt.setString(2, acceptDetail);

				int value = pstmt.executeUpdate();

				logger.debug("deleteReturn:" + value);

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
			isDelete = true;
			return isDelete;
		}


		@Override
		public Boolean importDataToStock(String groupId, String userId, String acceptIds) {

			boolean isImportData = false;
			Connection con = null;
			PreparedStatement pstmt = null;
			
			try {
				Class.forName(jdbcDriver);
				con = DriverManager.getConnection(dbURL, dbUserName, dbPassword);
				pstmt = con.prepareStatement(sp_importData_to_stock);

				pstmt.setString(1, "'"+groupId+"'");
				pstmt.setString(2, "'"+userId+"'");
				pstmt.setString(3, acceptIds);
				int value = pstmt.executeUpdate();

				logger.debug("importDataToStockReturn:" + value);

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
		public Boolean checkAccept(String group_id, String accept_id) {
			
			Boolean stock_flag = false;
			Connection con = null;
			PreparedStatement pstmt = null;
			ResultSet rs = null;
			
			try {
				Class.forName("com.mysql.jdbc.Driver");
				con = DriverManager.getConnection(dbURL, dbUserName, dbPassword);
				pstmt = con.prepareStatement(sp_check_accept);
				pstmt.setString(1, group_id);
				pstmt.setString(2, accept_id);
				rs = pstmt.executeQuery();
				if (rs.next()) {
					stock_flag = rs.getBoolean("stock_flag");
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
			
			return stock_flag;
		}
	}

	/*
	 * 用來記錄location_id
	 * map : 存放location_id 的 map
	 * key : location_id
	 * */
	public void record_Location(Map<String, Boolean> map, String key) {
		if (null != map && null != key) {
			map.put(key, true);
		}
	}
	
	/*
	 * 用來判別location_id 是否有執行過
	 * map : 存放location_id 的 map
	 * key : location_id
	 * */
	public boolean isHasBeenExecuted(Map<String, Boolean> map, String key) {
		if (null == map || null == key) {
			return true;
		}

		if (null == map.get(key)) {
			return false;
		} else if(true == map.get(key)){
			return true;
		}else{
			return false;
		}
	}

}
