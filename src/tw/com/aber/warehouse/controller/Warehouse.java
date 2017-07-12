package tw.com.aber.warehouse.controller;

import java.io.IOException;
import java.sql.Connection;
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

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import tw.com.aber.vo.AccreceiveVO;
import tw.com.aber.vo.WarehouseVO;

public class Warehouse extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private static final Logger logger = LogManager.getLogger(Warehouse.class);

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

		WarehouseSerivce warehouseSerivce = new WarehouseSerivce();
		String result = null;
		Gson gson = null;

		logger.debug("action: " + action);

		try {
			if ("getAllWarehouseVOList".equals(action)) {

				List<WarehouseVO> warehouseVOList = warehouseSerivce.getAllWarehoseVOList(group_id);

				Map<String, Object> map = new HashMap<>();

				map.put("warehouseVOList", warehouseVOList);

				gson = new GsonBuilder().setDateFormat("yyyy-MM-dd").create();

				result = gson.toJson(map);

				logger.debug("result: " + result);

				response.getWriter().write(result);
			} else if ("update_warehouse".equals(action)) {

				String warehouse_id = request.getParameter("warehouse_id");
				String warehouse_code = request.getParameter("warehouse_code");
				String warehouse_name = request.getParameter("warehouse_name");
				String warehouse_locate = request.getParameter("warehouse_locate");

				Boolean isSuccess = false;

				String errorMsg = checkData(warehouse_id, warehouse_code, warehouse_name, warehouse_locate);

				if (errorMsg.length() == 0) {
					boolean isEXISTSByWarehouseCode = warehouseSerivce.checkWarehouseCode(group_id, warehouse_code);
					if (!isEXISTSByWarehouseCode) {
						WarehouseVO warehouseVO = new WarehouseVO();
						warehouseVO.setGroup_id(group_id);
						warehouseVO.setWarehouse_code(warehouse_code);
						warehouseVO.setWarehouse_id(warehouse_id);
						warehouseVO.setWarehouse_locate(warehouse_locate);
						warehouseVO.setWarehouse_name(warehouse_name);

						isSuccess = warehouseSerivce.updateWarehouse(warehouseVO);
					} else {
						errorMsg = "此倉庫代碼已使用，請填入其他代碼。";
					}
				}

				if (isSuccess) {
					response.getWriter().write("success");
				} else {
					response.getWriter().write(errorMsg);
				}

			} else if ("insert_warehouse".equals(action)) {

				String warehouse_code = request.getParameter("warehouse_code");
				String warehouse_name = request.getParameter("warehouse_name");
				String warehouse_locate = request.getParameter("warehouse_locate");

				Boolean isSuccess = false;

				String errorMsg = checkData(warehouse_code, warehouse_name, warehouse_locate);

				if (errorMsg.length() == 0) {
					boolean isEXISTSByWarehouseCode = warehouseSerivce.checkWarehouseCode(group_id, warehouse_code);
					if (!isEXISTSByWarehouseCode) {
						WarehouseVO warehouseVO = new WarehouseVO();
						warehouseVO.setGroup_id(group_id);
						warehouseVO.setWarehouse_code(warehouse_code);
						warehouseVO.setWarehouse_locate(warehouse_locate);
						warehouseVO.setWarehouse_name(warehouse_name);

						isSuccess = warehouseSerivce.insertWarehouse(warehouseVO);
					} else {
						errorMsg = "此倉庫代碼已使用，請填入其他代碼。";
					}
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

	public class WarehouseSerivce {
		Warehouse_interface dao;

		WarehouseSerivce() {
			dao = new WarehouseDAO();
		}

		List<WarehouseVO> getAllWarehoseVOList(String group_id) {
			return dao.getAllWarehoseVOList(group_id);
		}

		Boolean updateWarehouse(WarehouseVO warehouseVO) {
			return dao.updateWarehouse(warehouseVO);
		}

		Boolean insertWarehouse(WarehouseVO warehouseVO) {
			return dao.insertWarehouse(warehouseVO);
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
	}

	interface Warehouse_interface {
		List<WarehouseVO> getAllWarehoseVOList(String group_id);

		List<WarehouseVO> getWarehouseByWarehouseCode(String group_id, String warehouse_code);

		Boolean updateWarehouse(WarehouseVO warehouseVO);

		Boolean insertWarehouse(WarehouseVO warehouseVO);
	}

	class WarehouseDAO implements Warehouse_interface {
		private final String dbURL = getServletConfig().getServletContext().getInitParameter("dbURL")
				+ "?useUnicode=true&characterEncoding=utf-8&useSSL=false";
		private final String dbUserName = getServletConfig().getServletContext().getInitParameter("dbUserName");
		private final String dbPassword = getServletConfig().getServletContext().getInitParameter("dbPassword");
		private final String jdbcDriver = getServletConfig().getServletContext().getInitParameter("jdbcDriver");

		private static final String sp_get_all_warehosevo_list = "call sp_get_all_warehosevo_list(?)";
		private static final String sp_update_warehouse = "call sp_update_warehouse(?,?,?,?,?)";
		private static final String sp_insert_warehouse = "call sp_insert_warehouse(?,?,?,?)";
		private static final String sp_get_warehouse_by_warehouse_code = "call sp_get_warehouse_by_warehouse_code(?,?)";

		@Override
		public List<WarehouseVO> getAllWarehoseVOList(String group_id) {
			List<WarehouseVO> warehouseVOList = new ArrayList<WarehouseVO>();
			WarehouseVO warehouseVO = null;

			Connection con = null;
			PreparedStatement pstmt = null;
			ResultSet rs = null;

			try {
				Class.forName(jdbcDriver);
				con = DriverManager.getConnection(dbURL, dbUserName, dbPassword);
				pstmt = con.prepareStatement(sp_get_all_warehosevo_list);

				pstmt.setString(1, group_id);
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
		public Boolean updateWarehouse(WarehouseVO warehouseVO) {
			Connection con = null;
			PreparedStatement pstmt = null;
			ResultSet rs = null;
			Boolean isSuccess = false;

			try {
				Class.forName("com.mysql.jdbc.Driver");
				con = DriverManager.getConnection(dbURL, dbUserName, dbPassword);
				pstmt = con.prepareStatement(sp_update_warehouse);

				pstmt.setString(1, warehouseVO.getGroup_id());
				pstmt.setString(2, warehouseVO.getWarehouse_code());
				pstmt.setString(3, warehouseVO.getWarehouse_name());
				pstmt.setString(4, warehouseVO.getWarehouse_locate());
				pstmt.setString(5, warehouseVO.getWarehouse_id());

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
		public Boolean insertWarehouse(WarehouseVO warehouseVO) {
			Connection con = null;
			PreparedStatement pstmt = null;
			ResultSet rs = null;
			Boolean isSuccess = false;

			try {
				Class.forName("com.mysql.jdbc.Driver");
				con = DriverManager.getConnection(dbURL, dbUserName, dbPassword);
				pstmt = con.prepareStatement(sp_insert_warehouse);

				pstmt.setString(1, warehouseVO.getGroup_id());
				pstmt.setString(2, warehouseVO.getWarehouse_code());
				pstmt.setString(3, warehouseVO.getWarehouse_name());
				pstmt.setString(4, warehouseVO.getWarehouse_locate());

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

	}

	public String checkData(String warehouse_id, String warehouse_code, String warehouse_name,
			String warehouse_locate) {
		String errorMsg = "";

		if (warehouse_id == null || warehouse_code == null || warehouse_name == null || warehouse_locate == null) {
			errorMsg = "資料錯誤";
			return errorMsg;
		}

		if ("".equals(warehouse_id.trim())) {
			errorMsg = "資料錯誤";
			return errorMsg;
		}
		if ("".equals(warehouse_code.trim())) {
			errorMsg = "倉庫編號不可為空";
			return errorMsg;
		}
		if ("".equals(warehouse_name.trim())) {
			errorMsg = "倉庫名稱不可為空";
			return errorMsg;
		}

		if ("".equals(warehouse_locate.trim())) {
			errorMsg = "倉庫地點不可為空";
			return errorMsg;
		}

		return errorMsg;
	}

	public String checkData(String warehouse_code, String warehouse_name, String warehouse_locate) {
		String errorMsg = "";

		if (warehouse_code == null || warehouse_name == null || warehouse_locate == null) {
			errorMsg = "資料錯誤";
			return errorMsg;
		}

		if ("".equals(warehouse_code.trim())) {
			errorMsg = "倉庫編號不可為空";
			return errorMsg;
		}
		if ("".equals(warehouse_name.trim())) {
			errorMsg = "倉庫名稱不可為空";
			return errorMsg;
		}

		if ("".equals(warehouse_locate.trim())) {
			errorMsg = "倉庫地點不可為空";
			return errorMsg;
		}

		return errorMsg;
	}
}
