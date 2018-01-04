package tw.com.aber.dao;

import static org.junit.Assert.assertTrue;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.Test;

import tw.com.aber.util.Database;
import tw.com.aber.vo.PickDetailVO;
import tw.com.aber.vo.PickVO;

public class PickDao {
	
	private static final Logger logger = LogManager.getLogger(PickDao.class);
	
	private static final String sp_select_pick_by_pick_Time = "call sp_select_pick_by_pick_Time (?,?,?)";
	private static final String sp_select_pick_by_order_no = "call sp_select_pick_by_order_no (?,?)";
	private static final String sp_select_pick_detail_by_pick_id = "call sp_select_pick_detail_by_pick_id (?,?)";

	private Connection connection;

	public PickDao() {
		connection = Database.getConnection();
	}
	
	public List<PickVO> searchPickByPickTimeDate(String groupId, Date startDate, Date endDate) {
		List<PickVO> pickVOList = new ArrayList<PickVO>();
		PickVO pickVO = null;

		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try {
			pstmt = connection.prepareStatement(sp_select_pick_by_pick_Time);

			pstmt.setString(1, groupId);
			pstmt.setDate(2, startDate);
			pstmt.setDate(3, endDate);

			rs = pstmt.executeQuery();
			while (rs.next()) {
				pickVO = new PickVO();
				pickVO.setPick_id(rs.getString("pick_id"));
				pickVO.setPick_no(rs.getString("pick_no"));
				pickVO.setGroup_id(rs.getString("group_id"));
				pickVO.setPick_time(rs.getDate("pick_time"));
				pickVO.setPick_user_id(rs.getString("pick_user_id"));
				pickVOList.add(pickVO);
			}
		} catch (SQLException se) {
			throw new RuntimeException("A database error occured. " + se.getMessage());
		} finally {
			try {
				if (rs != null) {
					rs.close();
				}
				if (pstmt != null) {
					pstmt.close();
				}
			} catch (SQLException se) {
				logger.error("SQLException:".concat(se.getMessage()));
			}
		}
		return pickVOList;
	}

	public List<PickVO> searchPickByOrderNo(String groupId, String orderNo) {
		List<PickVO> pickVOList = new ArrayList<PickVO>();
		PickVO pickVO = null;

		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try {
			pstmt = connection.prepareStatement(sp_select_pick_by_order_no);

			pstmt.setString(1, groupId);
			pstmt.setString(2, orderNo);

			rs = pstmt.executeQuery();
			while (rs.next()) {
				pickVO = new PickVO();
				pickVO.setPick_id(rs.getString("pick_id"));
				pickVO.setPick_no(rs.getString("pick_no"));
				pickVO.setGroup_id(rs.getString("group_id"));
				pickVO.setPick_time(rs.getDate("pick_time"));
				pickVO.setPick_user_id(rs.getString("pick_user_id"));
				pickVOList.add(pickVO);
			}
		} catch (SQLException se) {
			throw new RuntimeException("A database error occured. " + se.getMessage());
		} finally {
			try {
				if (rs != null) {
					rs.close();
				}
				if (pstmt != null) {
					pstmt.close();
				}
			} catch (SQLException se) {
				logger.error("SQLException:".concat(se.getMessage()));
			}
		}
		return pickVOList;
	}

	public List<PickDetailVO> searchPickDetailByPickId(String groupId, String pickId) {
		List<PickDetailVO> pickDetailVOList = new ArrayList<PickDetailVO>();
		PickDetailVO pickDetailVO = null;

		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try {
			pstmt = connection.prepareStatement(sp_select_pick_detail_by_pick_id);

			pstmt.setString(1, groupId);
			pstmt.setString(2, pickId);
			
			rs = pstmt.executeQuery();
			while (rs.next()) {
				pickDetailVO = new PickDetailVO();
				pickDetailVO.setC_product_id(rs.getString("c_product_id"));
				pickDetailVO.setGroup_id(rs.getString("group_id"));
				pickDetailVO.setLocation_id(rs.getString("location_id"));
				pickDetailVO.setOrder_no(rs.getString("order_no"));
				pickDetailVO.setPick_id(rs.getString("pick_id"));
				pickDetailVO.setPickDetail_id(rs.getString("pickDetail_id"));
				pickDetailVO.setProduct_id(rs.getString("product_id"));
				pickDetailVO.setQuantity(rs.getInt("quantity"));
				pickDetailVO.setRealsaleDetail_id(rs.getString("realsaleDetail_id"));
				pickDetailVO.setV_product_name(rs.getString("product_name"));
				pickDetailVO.setV_location_code(rs.getString("location_code"));
				pickDetailVOList.add(pickDetailVO);
			}
		} catch (SQLException se) {
			throw new RuntimeException("A database error occured. " + se.getMessage());
		} finally {
			try {
				if (rs != null) {
					rs.close();
				}
				if (pstmt != null) {
					pstmt.close();
				}
			} catch (SQLException se) {
				logger.error("SQLException:".concat(se.getMessage()));
			}
		}
		return pickDetailVOList;
	}
	
	@Test
	public void testMethods() {
		
		PickDao pickDao = new PickDao();
		try {
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
			Date fromTime = new Date(sdf.parse("2016-01-01").getTime());
			Date tillTime = new Date(sdf.parse("2017-12-31").getTime());
			List<PickVO> pickVOs = pickDao.searchPickByPickTimeDate("cbcc3138-5603-11e6-a532-000d3a800878",fromTime,tillTime );
			assertTrue(pickVOs!=null);
		} catch (ParseException e) {
			logger.error("jUnitTestErr",e);
		}
		
	}
}
