package tw.com.aber.sftransfer.controller;

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
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import tw.com.aber.ship.controller.ship.ShipService;
import tw.com.aber.vo.GroupSfVO;
import tw.com.aber.vo.ShipVO;
import tw.com.aber.vo.WarehouseVO;

public class ValueService extends HttpServlet {

	private static final long serialVersionUID = 1L;
	private static final Logger logger = LogManager.getLogger(ValueService.class);
	
//	public class ValueService{
//		super.init(config);
//	}
	
  interface	ValueService_interFace{
	  public WarehouseVO getWarehouseVoByGroudId(String groudId);
	  public GroupSfVO getGroupSfVoByGroupId(String groudId);
  }
  protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		doPost(request, response);
	}
  
  protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
	
	}
  
  class ValueServiceDAO implements ValueService_interFace{
	  
	  	private final String dbURL = "jdbc:mysql://192.168.112.164/db_virtualbusiness"
				+ "?useUnicode=true&characterEncoding=utf-8&useSSL=false";
		private final String dbUserName = "root";
		private final String dbPassword = "admin123";
		private final String jdbcDriver = "com.mysql.jdbc.Driver";
	  
		private static final String sp_get_warehouse_by_groudId = "call sp_get_warehouse_by_groudId(?)";
		private static final String sp_get_groupsf_by_groupId = "call sp_get_groupsf_by_groupId (?)";
		

	@Override
	public WarehouseVO getWarehouseVoByGroudId(String groudId) {
		
		WarehouseVO warehouseVO =new WarehouseVO();

		Connection con = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try {
			Class.forName(jdbcDriver);
			con = DriverManager.getConnection(dbURL, dbUserName, dbPassword);
			pstmt = con.prepareStatement(sp_get_warehouse_by_groudId);

			pstmt.setString(1, groudId);
		

			rs = pstmt.executeQuery();
			while (rs.next()) {
				warehouseVO = new WarehouseVO();
				warehouseVO.setGroup_id(rs.getString("group_id"));
				warehouseVO.setSf_warehouse_code(rs.getString("sf_warehouse_code"));
				warehouseVO.setWarehouse_id(rs.getString("warehouse_id"));
				warehouseVO.setWarehouse_locate(rs.getString("warehouse_locate"));
				warehouseVO.setWarehouse_name(rs.getString("warehouse_name"));
				warehouseVO.setWarehouse_code(rs.getString("warehouse_code"));
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
		return warehouseVO;
	}

	@Override
	public GroupSfVO getGroupSfVoByGroupId(String groudId) {
		
		GroupSfVO groupSfVO =new GroupSfVO();

		Connection con = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try {
			Class.forName(jdbcDriver);
			con = DriverManager.getConnection(dbURL, dbUserName, dbPassword);
			pstmt = con.prepareStatement(sp_get_groupsf_by_groupId);

			pstmt.setString(1, groudId);
		

			rs = pstmt.executeQuery();
			while (rs.next()) {
				groupSfVO = new GroupSfVO();
				groupSfVO.setAccess_code(rs.getString("access_code"));
				groupSfVO.setCheck_word(rs.getString("check_word"));
				groupSfVO.setCompany_code(rs.getString("company_code"));
				groupSfVO.setGroup_id(rs.getString("group_id"));
				groupSfVO.setMonthly_account(rs.getString("monthly_account"));
				groupSfVO.setVendor_code(rs.getString("vendor_code"));
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
		return groupSfVO;
	}
	  
	  
  }
  
  public class ValueService_Service{
	  public ValueService_interFace dao;

		public ValueService_Service() {
			logger.debug("dao have new:"+getServletConfig());
			
			dao = new ValueServiceDAO();
			logger.debug("dao");
		}
		
		public WarehouseVO getWarehouseVoByGroudId(String groudId){
			return dao.getWarehouseVoByGroudId(groudId);
			
		}
		public GroupSfVO getGroupSfVoByGroupId(String groudId){
			return dao.getGroupSfVoByGroupId(groudId);
		}
  }
  
 
	

}
