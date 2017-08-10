package tw.com.aber.location.controller;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
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

import tw.com.aber.vo.LocationVO;
import tw.com.aber.vo.WarehouseVO;

public class Location  extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private static final Logger logger = LogManager.getLogger(Location.class);

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
		
		logger.debug("action :"+action);
		logger.debug("group_id :"+group_id);
		logger.debug("user_id :"+user_id);
			
		LocationSerivce locationSerivce = new LocationSerivce();
		String result = null;
		Gson gson = null;
		try {
			if("getLocationVOListByWarehousecode".equals(action)){
				String warehouse_code=request.getParameter("warehouse_code");
				
				logger.debug("warehouse_code :"+warehouse_code);
				
				if("".equals(warehouse_code.trim())){
					
					List<LocationVO> locationVOList = locationSerivce.getAllLocationVOList(group_id);

					Map<String, Object> map = new HashMap<>();

					map.put("locationVOList", locationVOList);

					gson = new GsonBuilder().setDateFormat("yyyy-MM-dd").create();

					result = gson.toJson(map);

					logger.debug("result: " + result);

					response.getWriter().write(result);
				}else{
					List<LocationVO> locationVOList = locationSerivce.getLocationByWarehouseCode(group_id,warehouse_code);

					Map<String, Object> map = new HashMap<>();

					map.put("locationVOList", locationVOList);

					gson = new GsonBuilder().setDateFormat("yyyy-MM-dd").create();

					result = gson.toJson(map);

					logger.debug("result: " + result);

					response.getWriter().write(result);
					
				}
				
			
			}else if("getLocationByWarehouseCode".equals(action)){
				String warehouse_code = request.getParameter("warehouse_code");
				
				logger.debug("warehouse_code :"+warehouse_code);
				
				List<WarehouseVO> warehouseVOList = locationSerivce.getWarehouseByParallelismWarehouseCode(group_id, warehouse_code);

				gson = new GsonBuilder().setDateFormat("yyyy-MM-dd").create();

				result = gson.toJson(warehouseVOList);

				logger.debug("result: " + result);

				response.getWriter().write(result);
			}else if("insert_location".equals(action)){
				String location_desc = request.getParameter("location_desc");
				String location_memo = request.getParameter("location_memo");
				String location_code = request.getParameter("location_code");
				String v_warehouse_code = request.getParameter("v_warehouse_code");

				logger.debug("location_desc :"+location_desc);
				logger.debug("location_memo :"+location_memo);
				logger.debug("location_code :"+location_code);
				logger.debug("v_warehouse_code :"+v_warehouse_code);
				
				Boolean isSuccess = false;

				String errorMsg = checkData(v_warehouse_code, location_code,location_desc,group_id);
				
				if (errorMsg.length() == 0) {
		
						LocationVO locationVO = new LocationVO();
						locationVO.setGroup_id(group_id);
						locationVO.setLocation_code(location_code);
						locationVO.setLocation_desc(location_desc);
						locationVO.setLocation_memo(location_memo);
						locationVO.setV_warehouse_code(v_warehouse_code);
						
						isSuccess = locationSerivce.insertLocation(locationVO);
				
				}

				if (isSuccess) {
					response.getWriter().write("success");
				} else {
					response.getWriter().write(errorMsg);
				}
			}else if("update_location".equals(action)){
				String location_desc = request.getParameter("location_desc");
				String location_memo = request.getParameter("location_memo");
				String location_code = request.getParameter("location_code");
				String v_warehouse_code = request.getParameter("v_warehouse_code");
				String location_id = request.getParameter("location_id");

				logger.debug("location_desc :"+location_desc);
				logger.debug("location_memo :"+location_memo);
				logger.debug("location_code :"+location_code);
				logger.debug("v_warehouse_code :"+v_warehouse_code);
				
				Boolean isSuccess = false;

				String errorMsg = checkData(location_id,v_warehouse_code, location_code,location_desc,group_id);
				
				if (errorMsg.length() == 0) {
		
						LocationVO locationVO = new LocationVO();
						locationVO.setGroup_id(group_id);
						locationVO.setLocation_code(location_code);
						locationVO.setLocation_desc(location_desc);
						locationVO.setLocation_memo(location_memo);
						locationVO.setV_warehouse_code(v_warehouse_code);
						locationVO.setLocation_id(location_id);
						
						isSuccess = locationSerivce.updateLocation(locationVO);
				
				}

				if (isSuccess) {
					response.getWriter().write("success");
				} else {
					response.getWriter().write(errorMsg);
				}
			}
			
		} catch (Exception e) {
			System.out.println(e.toString());
		}
		
		
	}
	
	public class LocationSerivce {
		Location_interface dao;

		LocationSerivce() {
			dao = new LocationDAO();
		}

		List<LocationVO> getAllLocationVOList(String group_id) {
			return dao.getAllLocationVOList(group_id);
		}
		List<LocationVO> getLocationByWarehouseCode(String group_id,String warehouse_code) {
			return dao.getLocationByWarehouseCode(group_id,warehouse_code);
		}
		Boolean updateLocation(LocationVO LocationVO) {
			return dao.updateLocation(LocationVO);
		}

		Boolean insertLocation(LocationVO LocationVO) {
			return dao.insertLocation(LocationVO);
		}

		Boolean checkLocationCode(String group_id, String location_code,String location_id) {
			Boolean isEXISTSByLocationCode = true;
			List<LocationVO> locationVOList = dao.getLocationByLocationCode(group_id, location_code,location_id);
			if (locationVOList.size() > 0) {
				isEXISTSByLocationCode = true;
				return isEXISTSByLocationCode;
			} else {
				isEXISTSByLocationCode = false;
				return isEXISTSByLocationCode;
			}

		}
		
		Boolean checkWarehouseCode(String group_id, String warehouse_code) {
			Boolean isEXISTSByWarehouseCode = true;
			List<WarehouseVO> warehouseVOList = dao.getWarehouseByWarehouseCode(group_id, warehouse_code);
			if (warehouseVOList.size() > 0) {
				isEXISTSByWarehouseCode = true;
				return isEXISTSByWarehouseCode;
			} else {
				isEXISTSByWarehouseCode = false;
				return isEXISTSByWarehouseCode;
			}

		}
		List<WarehouseVO> getWarehouseByParallelismWarehouseCode(String group_id,String warehouse_code) {
			return dao.getWarehouseByParallelismWarehouseCode(group_id, warehouse_code);
		}
		
	}
	interface Location_interface {
		List<LocationVO> getAllLocationVOList(String group_id);

		List<LocationVO> getLocationByWarehouseCode(String group_id, String warehouse_code);
		
		List<WarehouseVO> getWarehouseByWarehouseCode(String group_id, String warehouse_code);

		Boolean updateLocation(LocationVO locationVO);

		Boolean insertLocation(LocationVO locationVO);
		
		List<WarehouseVO> getWarehouseByParallelismWarehouseCode(String group_id, String warehouse_code);
		
		List<LocationVO> getLocationByLocationCode(String group_id, String location_code,String location_id);

	}
	
	class LocationDAO implements Location_interface{
		private final String dbURL = getServletConfig().getServletContext().getInitParameter("dbURL")
				+ "?useUnicode=true&characterEncoding=utf-8&useSSL=false";
		private final String dbUserName = getServletConfig().getServletContext().getInitParameter("dbUserName");
		private final String dbPassword = getServletConfig().getServletContext().getInitParameter("dbPassword");
		private final String jdbcDriver = getServletConfig().getServletContext().getInitParameter("jdbcDriver");

		private static final String sp_get_all_location_list = "call sp_get_all_location_list(?)";
		private static final String sp_get_location_by_warehouse_code = "call sp_get_location_by_warehouse_code(?,?)";
		private static final String sp_get_warehouse_by_parallelism_warehouse_code = "call sp_get_warehouse_by_parallelism_warehouse_code(?,?)";
		private static final String sp_insert_location = "call sp_insert_location(?,?,?,?,?)";
		private static final String sp_update_location = "call sp_update_location(?,?,?,?,?,?)";
		private static final String sp_get_warehouse_by_warehouse_code = "call sp_get_warehouse_by_warehouse_code(?,?)";
		private static final String sp_get_location_by_location_code = "call sp_get_location_by_location_code(?,?,?)";


		
		@Override
		public List<LocationVO> getLocationByWarehouseCode(String group_id, String warehouse_code) {
			List<LocationVO> locationVOList = new ArrayList<LocationVO>();
			LocationVO locationVO = null;

			Connection con = null;
			PreparedStatement pstmt = null;
			ResultSet rs = null;

			try {
				Class.forName(jdbcDriver);
				con = DriverManager.getConnection(dbURL, dbUserName, dbPassword);
				pstmt = con.prepareStatement(sp_get_location_by_warehouse_code);

				pstmt.setString(1, group_id);
				pstmt.setString(2, warehouse_code);
				rs = pstmt.executeQuery();

				while (rs.next()) {
					locationVO = new LocationVO();
					locationVO.setGroup_id(group_id);
					locationVO.setLocation_code(rs.getString("location_code"));
					locationVO.setLocation_desc(rs.getString("location_desc"));
					locationVO.setLocation_id(rs.getString("location_id"));
					locationVO.setLocation_memo(rs.getString("location_memo"));
					locationVO.setV_warehouse_code(rs.getString("warehouse_code"));
					locationVO.setV_warehouse_locate(rs.getString("warehouse_locate"));
					locationVO.setV_warehouse_name(rs.getString("warehouse_name"));
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
		public Boolean updateLocation(LocationVO LocationVO) {
			Connection con = null;
			PreparedStatement pstmt = null;
			ResultSet rs = null;
			Boolean isSuccess = false;

			try {
				Class.forName("com.mysql.jdbc.Driver");
				con = DriverManager.getConnection(dbURL, dbUserName, dbPassword);
				pstmt = con.prepareStatement(sp_update_location);

				pstmt.setString(1, LocationVO.getGroup_id());
				pstmt.setString(2, LocationVO.getLocation_code());
				pstmt.setString(3, LocationVO.getLocation_desc());
				pstmt.setString(4, LocationVO.getLocation_memo());
				pstmt.setString(5, LocationVO.getV_warehouse_code());
				pstmt.setString(6, LocationVO.getLocation_id());
				
				rs = pstmt.executeQuery();
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
			isSuccess = true;
			return isSuccess;
		}

		@Override
		public Boolean insertLocation(LocationVO LocationVO) {
			Connection con = null;
			PreparedStatement pstmt = null;
			ResultSet rs = null;
			Boolean isSuccess = false;

			try {
				Class.forName("com.mysql.jdbc.Driver");
				con = DriverManager.getConnection(dbURL, dbUserName, dbPassword);
				pstmt = con.prepareStatement(sp_insert_location);

				pstmt.setString(1, LocationVO.getGroup_id());
				pstmt.setString(2, LocationVO.getLocation_code());
				pstmt.setString(3, LocationVO.getLocation_desc());
				pstmt.setString(4, LocationVO.getLocation_memo());
				pstmt.setString(5, LocationVO.getV_warehouse_code());
				
				rs = pstmt.executeQuery();
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
			isSuccess = true;
			return isSuccess;
		}

		@Override
		public List<LocationVO> getAllLocationVOList(String group_id) {
			List<LocationVO> locationVOList = new ArrayList<LocationVO>();
			LocationVO locationVO = null;

			Connection con = null;
			PreparedStatement pstmt = null;
			ResultSet rs = null;

			try {
				Class.forName(jdbcDriver);
				con = DriverManager.getConnection(dbURL, dbUserName, dbPassword);
				pstmt = con.prepareStatement(sp_get_all_location_list);

				pstmt.setString(1, group_id);
				rs = pstmt.executeQuery();

				while (rs.next()) {
					locationVO = new LocationVO();
					locationVO.setGroup_id(group_id);
					locationVO.setLocation_code(rs.getString("location_code"));
					locationVO.setLocation_desc(rs.getString("location_desc"));
					locationVO.setLocation_id(rs.getString("location_id"));
					locationVO.setLocation_memo(rs.getString("location_memo"));
					locationVO.setV_warehouse_code(rs.getString("warehouse_code"));
					locationVO.setV_warehouse_locate(rs.getString("warehouse_locate"));
					locationVO.setV_warehouse_name(rs.getString("warehouse_name"));
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
		public List<WarehouseVO> getWarehouseByParallelismWarehouseCode(String group_id, String warehouse_code) {
			List<WarehouseVO> warehouseVOList = new ArrayList<WarehouseVO>();
			WarehouseVO warehouseVO = null;

			Connection con = null;
			PreparedStatement pstmt = null;
			ResultSet rs = null;

			try {
				Class.forName(jdbcDriver);
				con = DriverManager.getConnection(dbURL, dbUserName, dbPassword);
				pstmt = con.prepareStatement(sp_get_warehouse_by_parallelism_warehouse_code);

				pstmt.setString(1, group_id);
				pstmt.setString(2, warehouse_code);
				rs = pstmt.executeQuery();

				while (rs.next()) {
					warehouseVO = new WarehouseVO();
					warehouseVO.setSf_warehouse_code(rs.getString("sf_warehouse_code"));
					warehouseVO.setGroup_id(rs.getString("group_id"));
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
		public List<WarehouseVO> getWarehouseByWarehouseCode(String group_id, String warehouse_code) {
			List<WarehouseVO> warehouseVOList = new ArrayList<WarehouseVO>();
			WarehouseVO warehouseVO = null;

			Connection con = null;
			PreparedStatement pstmt = null;
			ResultSet rs = null;

			try {
				Class.forName(jdbcDriver);
				con = DriverManager.getConnection(dbURL, dbUserName, dbPassword);
				pstmt = con.prepareStatement(sp_get_warehouse_by_warehouse_code);

				pstmt.setString(1, group_id);
				pstmt.setString(2, warehouse_code);
				rs = pstmt.executeQuery();

				while (rs.next()) {
					warehouseVO = new WarehouseVO();
					warehouseVO.setSf_warehouse_code(rs.getString("sf_warehouse_code"));
					warehouseVO.setGroup_id(rs.getString("group_id"));
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
		public List<LocationVO> getLocationByLocationCode(String group_id, String location_code,String location_id) {
			List<LocationVO> locationVOList = new ArrayList<LocationVO>();
			LocationVO locationVO = null;

			Connection con = null;
			PreparedStatement pstmt = null;
			ResultSet rs = null;

			try {
				Class.forName(jdbcDriver);
				con = DriverManager.getConnection(dbURL, dbUserName, dbPassword);
				pstmt = con.prepareStatement(sp_get_location_by_location_code);

				pstmt.setString(1, group_id);
				pstmt.setString(2, location_code);
				pstmt.setString(3, location_id);
				rs = pstmt.executeQuery();

				while (rs.next()) {
					locationVO = new LocationVO();
					locationVO.setGroup_id(group_id);
					locationVO.setLocation_code(rs.getString("location_code"));
					locationVO.setLocation_desc(rs.getString("location_desc"));
					locationVO.setLocation_id(rs.getString("location_id"));
					locationVO.setLocation_memo(rs.getString("location_memo"));
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
		
	}
	public String checkData(String location_id,String v_warehouse_code,String location_code, String location_desc,String group_id) {
		String errorMsg = "";

		if (location_id == null||v_warehouse_code == null || location_code == null || location_desc == null || group_id == null) {
			errorMsg = "資料錯誤";
			return errorMsg;
		}

		if ("".equals(group_id.trim())) {
			errorMsg = "資料錯誤";
			return errorMsg;
		}
		if ("".equals(location_id.trim())) {
			errorMsg = "資料錯誤";
			return errorMsg;
		}
		if ("".equals(v_warehouse_code.trim())) {
			errorMsg = "倉庫編號不可為空";
			return errorMsg;
		}
		if ("".equals(location_code.trim())) {
			errorMsg = "儲位編號不可為空";
			return errorMsg;
		}

		if ("".equals(location_desc.trim())) {
			errorMsg = "儲位名稱不可為空";
			return errorMsg;
		}
		
		LocationSerivce locationSerivce = new LocationSerivce();
		boolean isEXISTSByWarehouseCode = locationSerivce.checkWarehouseCode(group_id, v_warehouse_code);
		if (!isEXISTSByWarehouseCode) {
			errorMsg = "查無此倉庫代碼，請填入其他代碼。";
		}
		
		boolean isEXISTSByLocationCode = locationSerivce.checkLocationCode(group_id, location_code,location_id);
		if (isEXISTSByLocationCode) {
			errorMsg = "此儲位代碼已使用，請填入其他代碼。";
		}
		return errorMsg;
	}
	
	
	public String checkData(String v_warehouse_code,String location_code, String location_desc,String group_id) {
		String errorMsg = "";

		if (v_warehouse_code == null || location_code == null || location_desc == null || group_id == null) {
			errorMsg = "資料錯誤";
			return errorMsg;
		}

		if ("".equals(group_id.trim())) {
			errorMsg = "資料錯誤";
			return errorMsg;
		}
		if ("".equals(v_warehouse_code.trim())) {
			errorMsg = "倉庫編號不可為空";
			return errorMsg;
		}
		if ("".equals(location_code.trim())) {
			errorMsg = "儲位編號不可為空";
			return errorMsg;
		}

		if ("".equals(location_desc.trim())) {
			errorMsg = "儲位名稱不可為空";
			return errorMsg;
		}
		LocationSerivce locationSerivce = new LocationSerivce();
		boolean isEXISTSByWarehouseCode = locationSerivce.checkWarehouseCode(group_id, v_warehouse_code);
		if (!isEXISTSByWarehouseCode) {
			errorMsg = "查無此倉庫代碼，請填入其他代碼。";
		}
		
		boolean isEXISTSByLocationCode = locationSerivce.checkLocationCode(group_id, location_code,"");
		if (isEXISTSByLocationCode) {
			errorMsg = "此儲位代碼已使用，請填入其他代碼。";
		}

		return errorMsg;
	}
}
