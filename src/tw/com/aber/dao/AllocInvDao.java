package tw.com.aber.dao;

import static org.junit.Assert.assertTrue;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.ArrayList;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.Test;

import tw.com.aber.util.Database;
import tw.com.aber.vo.AllocInvVo;
import tw.com.aber.vo.PurchaseDetailVO;
import tw.com.aber.vo.PurchaseVO;

public class AllocInvDao {
	private static final Logger logger = LogManager.getLogger(AllocInvDao.class);

	private static final String sp_select_all_alloc_inv = "call sp_select_all_alloc_inv (?)";
	private static final String sp_select_group_alloc_inv = "call sp_select_group_alloc_inv (?)";
	private static final String sp_insert_allocinv_to_purchase = "call sp_insert_allocinv_to_purchase (?,?,?,?,?)";
	private static final String sp_insert_allocinv_to_purchase_detail = "call sp_insert_allocinv_to_purchase_detail(?)";
	private static final String sp_get_purchase_newseqno = "call sp_get_purchase_newseqno (?,?)";
	private static final String sp_del_purchase= "call sp_del_purchase (?)";
	private static final String sp_get_alloc_inv = "call sp_get_alloc_inv (?,?)";
	private static final String sp_del_alloc_inv = "call sp_del_alloc_inv (?,?,?,?)";
	private static final String sp_insert_alloc_inv = "call sp_insert_alloc_inv (?,?,?,?,?,?,?,?,?,?,?,?,?)";
		
	
	private Connection connection;

	public AllocInvDao() {
		connection = Database.getConnection();
	}
	
	public List<AllocInvVo> getAllData(String group_id) {
		List<AllocInvVo> list = new ArrayList<AllocInvVo>();
		AllocInvVo allocInvVo = null;

		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try {
			pstmt = connection.prepareStatement(sp_select_all_alloc_inv);
			pstmt.setString(1, group_id);
			rs = pstmt.executeQuery();
			while (rs.next()) {
				allocInvVo = new AllocInvVo();
				allocInvVo.setAlloc_id(rs.getString("alloc_id"));
				allocInvVo.setRealsaleDetail_id(rs.getString("realsaleDetail_id"));
				allocInvVo.setGroup_id(rs.getString("group_id"));
				allocInvVo.setOrder_no(rs.getString("order_no"));
				allocInvVo.setProduct_id(rs.getString("product_id"));
				allocInvVo.setC_product_id(rs.getString("c_product_id"));
				allocInvVo.setQuantity(rs.getInt("quantity"));
				allocInvVo.setPrice(rs.getFloat("price"));
				allocInvVo.setLocation_id(rs.getString("location_id"));
				allocInvVo.setAlloc_qty(rs.getInt("alloc_qty"));
				allocInvVo.setAlloc_time(rs.getTimestamp("alloc_time"));
				allocInvVo.setCheckin_time(rs.getTimestamp("checkin_time"));
				allocInvVo.setProduct_name(rs.getString("product_name"));
				allocInvVo.setSupply_id(rs.getString("supply_id"));
				allocInvVo.setSupply_name(rs.getString("supply_name"));
				list.add(allocInvVo); 
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
		return list;
	}

	public List<AllocInvVo> getGroupData(String group_id) {
		List<AllocInvVo> list = new ArrayList<AllocInvVo>();
		AllocInvVo allocInvVo = null;

		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try {
			pstmt = connection.prepareStatement(sp_select_group_alloc_inv);
			pstmt.setString(1, group_id);
			rs = pstmt.executeQuery();
			while (rs.next()) {
				allocInvVo = new AllocInvVo();
				allocInvVo.setProduct_id(rs.getString("product_id"));
				allocInvVo.setC_product_id(rs.getString("c_product_id"));
				allocInvVo.setQuantity(rs.getInt("quantity"));
				allocInvVo.setAlloc_qty(rs.getInt("alloc_qty"));
				allocInvVo.setProduct_name(rs.getString("product_name"));
				allocInvVo.setSupply_id(rs.getString("supply_id"));
				allocInvVo.setSupply_name(rs.getString("supply_name"));
				list.add(allocInvVo);
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
		return list;
	}

	public String doPurchase(PurchaseVO purchaseVO) {
		CallableStatement cs = null;
		String result = null;
		try {
			cs = connection.prepareCall(sp_insert_allocinv_to_purchase);

			cs.setString(1, purchaseVO.getSeq_no());
			cs.setString(2, purchaseVO.getGroup_id());
			cs.setString(3, purchaseVO.getUser_id());
			cs.setString(4, purchaseVO.getSupply_id());
			cs.registerOutParameter(5, Types.CHAR);
			cs.execute();

			result = cs.getString(5);

		} catch (SQLException se) {
			logger.error("SQLException:" + se.getMessage());
		} finally {
			if (cs != null) {
				try {
					cs.close();
				} catch (SQLException se) {
					se.printStackTrace(System.err);
				}
			}
		}
		return result;
	}

	public String getPurchaseSeqNo(String groupId) {
		CallableStatement cs = null;
		String result = null;
		try {
			cs = connection.prepareCall(sp_get_purchase_newseqno);

			cs.setString(1, groupId);
			cs.registerOutParameter(2, Types.VARCHAR);
			cs.execute();

			result = cs.getString(2);

		} catch (SQLException se) {
			throw new RuntimeException("A database error occured. " + se.getMessage());
		} finally {
			if (cs != null) {
				try {
					cs.close();
				} catch (SQLException se) {
					se.printStackTrace(System.err);
				}
			}
		}
		return result;
	}

	public void doPurchaseDetail(List<PurchaseDetailVO> purchaseDetailVOs) {

		String sql = "VALUES";

		for (PurchaseDetailVO purchaseDetailVO : purchaseDetailVOs) {
			sql = sql.concat("(UUID(),'").concat(purchaseDetailVO.getPurchase_id() + "','")
					.concat(purchaseDetailVO.getGroup_id() + "','").concat(purchaseDetailVO.getUser_id() + "','")
					.concat(purchaseDetailVO.getProduct_id() + "','")
					.concat(purchaseDetailVO.getC_product_id() + "','")
					.concat(purchaseDetailVO.getProduct_name() + "','")
					.concat(purchaseDetailVO.getQuantity() + "',").concat("0,NULL,NULL,false),");
		}
		sql = sql.substring(0, sql.length() - 1);
		CallableStatement cs = null;
		try {
			logger.debug("\nsql: {}",sql);
			 cs = connection.prepareCall(sp_insert_allocinv_to_purchase_detail);
			
			 cs.setString(1, sql);
			 cs.execute();

		} catch (SQLException se) {
			throw new RuntimeException("A database error occured. " + se.getMessage());
		} finally {
			if (cs != null) {
				try {
					cs.close();
				} catch (SQLException se) {
					se.printStackTrace(System.err);
				}
			}
		}
	}

	public void delPurchase(String purchaseId) {
		CallableStatement cs = null;
		try {
			 cs = connection.prepareCall(sp_del_purchase);
			 cs.setString(1, purchaseId);
			 cs.execute();

		} catch (SQLException se) {
			throw new RuntimeException("A database error occured. " + se.getMessage());
		} finally {
			if (cs != null) {
				try {
					cs.close();
				} catch (SQLException se) {
					se.printStackTrace(System.err);
				}
			}
		}
	}
	
	public List<AllocInvVo> getAllocInvByGroupAndOrderNo(String group_id, String order_no) {
		List<AllocInvVo> list = new ArrayList<AllocInvVo>();
		AllocInvVo allocInvVo = null;

		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try {
			pstmt = connection.prepareStatement(sp_get_alloc_inv);
			pstmt.setString(1, group_id);
			pstmt.setString(2, order_no);
			rs = pstmt.executeQuery();
			
			while (rs.next()) {
				allocInvVo = new AllocInvVo();
				allocInvVo.setAlloc_id(rs.getString("alloc_id"));
				allocInvVo.setRealsaleDetail_id(rs.getString("realsaleDetail_id"));
				allocInvVo.setGroup_id(rs.getString("group_id"));
				allocInvVo.setOrder_no(rs.getString("order_no"));
				allocInvVo.setProduct_id(rs.getString("product_id"));
				allocInvVo.setC_product_id(rs.getString("c_product_id"));
				allocInvVo.setQuantity(rs.getInt("quantity"));
				allocInvVo.setPrice(rs.getFloat("price"));
				allocInvVo.setLocation_id(rs.getString("location_id"));
				allocInvVo.setAlloc_qty(rs.getInt("alloc_qty"));
				allocInvVo.setAlloc_time(rs.getTimestamp("alloc_time"));
				allocInvVo.setCheckin_time(rs.getTimestamp("checkin_time"));

				list.add(allocInvVo); 
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
		return list;
	}

	public String deleteAllocInvByGroupAndOrderNo(String group_id, String order_no, String user_id) {
		CallableStatement cs = null;
		String result = null;
		try {
			cs = connection.prepareCall(sp_del_alloc_inv);

			cs.setString(1, group_id);
			cs.setString(2, order_no);
			cs.setString(3, user_id);
			cs.registerOutParameter(4, Types.CHAR);
			cs.execute();

			result = cs.getString(4);
		} catch (SQLException se) {
			throw new RuntimeException("A database error occured. " + se.getMessage());
		} finally {
			try {
				if (cs != null) {
					cs.close();
				}
			} catch (SQLException se) {
				logger.error("SQLException:".concat(se.getMessage()));
			}
		}
		return result;
	}
	
	public String insertAllocInv(AllocInvVo allocInvVo) {
		CallableStatement cs = null;
		String result = null;
		try {
			cs = connection.prepareCall(sp_insert_alloc_inv);

			cs.setString(1, allocInvVo.getAlloc_id());
			cs.setString(2, allocInvVo.getRealsaleDetail_id());
			cs.setString(3, allocInvVo.getGroup_id());
			cs.setString(4, allocInvVo.getOrder_no());
			cs.setString(5, allocInvVo.getProduct_id());
			cs.setString(6, allocInvVo.getC_product_id());
			cs.setInt(7, allocInvVo.getQuantity());
			cs.setFloat(8, allocInvVo.getPrice());
			cs.setString(9, allocInvVo.getLocation_id());
			cs.setInt(10, allocInvVo.getAlloc_qty());
			cs.setTimestamp(11, convertUtilToSql(allocInvVo.getAlloc_time()));
			cs.setTimestamp(12, convertUtilToSql(allocInvVo.getCheckin_time()));
			cs.registerOutParameter(13, Types.CHAR);
			cs.execute();

			result = cs.getString(13);
		} catch (SQLException se) {
			logger.error("SQLException:" + se.getMessage());
		} finally {
			if (cs != null) {
				try {
					cs.close();
				} catch (SQLException se) {
					se.printStackTrace(System.err);
				}
			}
		}
		return result;
	}
	
	private static java.sql.Timestamp convertUtilToSql(java.util.Date uDate) {
        java.sql.Timestamp sDate = new java.sql.Timestamp(uDate.getTime());
        return sDate;
    }
	
	@Test
	public void testMethods() {
		
		AllocInvDao allocinvDao = new AllocInvDao();
		
		List<AllocInvVo> allocinvVOs = allocinvDao.getAllData("cbcc3138-5603-11e6-a532-000d3a800878");
		assertTrue(allocinvVOs!=null);
		
	}
}
