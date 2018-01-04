package tw.com.aber.dao;

import static org.junit.Assert.assertTrue;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.JSONObject;
import org.junit.Test;

import tw.com.aber.util.Database;
import tw.com.aber.util.Util;
import tw.com.aber.vo.CustomerVO;
import tw.com.aber.vo.RealSaleDetailVO;
import tw.com.aber.vo.RealSaleVO;

public class RealSaleDao {
	private static final Logger logger = LogManager.getLogger(RealSaleDao.class);
	// 會使用到的Stored procedure
	// 查詢
	private static final String sp_selectall_realsale = "call sp_selectall_realsale(?)";
	// 刪除
	private static final String sp_del_realsale = "call sp_del_realsale (?,?,?)";
	// 新增
	private static final String sp_insert_realsale = "call sp_insert_realsale(?, ?, ?, ?, ?, ?, ?,?, ?, ?, ?, ?, ?)";
	private static final String sp_get_customer_byname = "call sp_get_customer_byname (?,?)";
	private static final String sp_get_realsale_newseqno = "call sp_get_realsale_newseqno(?)";
	//修改
	private static final String sp_update_realsale = "call sp_update_realsale (?,?,?,?,?,?,?,?,?,?,?,?)";
	//明細
	private static final String sp_get_realsale_detail = "call sp_get_realsale_detail (?,?)";
	//匯入
	private static final String sp_importData_realsale = "call sp_importData_realsale (?,?,?,?)";
	private static final String sp_importData_alloc_inv = "call sp_importData_alloc_inv (?,?)";
	
	private Connection connection;

	public RealSaleDao() {
		connection = Database.getConnection();
	}
	
	public void insertDB(RealSaleVO realSaleVO) {
		PreparedStatement pstmt = null;
		try {
			pstmt = connection.prepareStatement(sp_insert_realsale);

			pstmt.setString(1, realSaleVO.getSeq_no());
			pstmt.setString(2, realSaleVO.getGroup_id());
			pstmt.setString(3, realSaleVO.getUser_id());
			pstmt.setString(4, realSaleVO.getOrder_no());
			pstmt.setString(5, realSaleVO.getOrder_source());
			pstmt.setString(6, realSaleVO.getCustomer_id());
			pstmt.setString(7, realSaleVO.getInvoice());
			pstmt.setFloat(8, realSaleVO.getTotal_amt());
			pstmt.setString(9, realSaleVO.getMemo());
			pstmt.setDate(10, realSaleVO.getInvoice_date());
			pstmt.setDate(11, realSaleVO.getTrans_list_date());
			pstmt.setDate(12, realSaleVO.getDis_date());
			pstmt.setDate(13, realSaleVO.getSale_date());

			pstmt.executeUpdate();
		} catch (SQLException se) {
			throw new RuntimeException("A database error occured. " + se.getMessage());
		} finally {
			try {
				if (pstmt != null) {
					pstmt.close();
				}
			} catch (SQLException se) {
				logger.error("SQLException:".concat(se.getMessage()));
			}
		}
	}

	public void updateDB(RealSaleVO realSaleVO) {
		PreparedStatement pstmt = null;
		try {
			pstmt = connection.prepareStatement(sp_update_realsale);
			
			pstmt.setString(1, realSaleVO.getRealsale_id());
			pstmt.setString(2, realSaleVO.getOrder_no());
			pstmt.setString(3, realSaleVO.getOrder_source());
			pstmt.setString(4, realSaleVO.getCustomer_id());
			pstmt.setFloat(5, realSaleVO.getTotal_amt());
			pstmt.setDate(6, realSaleVO.getTrans_list_date());
			pstmt.setString(7, realSaleVO.getInvoice());
			pstmt.setDate(8, realSaleVO.getSale_date());
			pstmt.setDate(9, realSaleVO.getInvoice_date());
			pstmt.setDate(10, realSaleVO.getDis_date());
			pstmt.setString(11, realSaleVO.getMemo());
			pstmt.setString(12, realSaleVO.getUser_id());

			pstmt.executeUpdate();
		} catch (SQLException se) {
			throw new RuntimeException("A database error occured. " + se.getMessage());
		} finally {
			try {
				if (pstmt != null) {
					pstmt.close();
				}
			} catch (SQLException se) {
				logger.error("SQLException:".concat(se.getMessage()));
			}
		}
	}

	public void deleteDB(String realsale_id,String user_id,String group_id) {
		PreparedStatement pstmt = null;
		try {
			pstmt = connection.prepareStatement(sp_del_realsale);
			pstmt.setString(1, realsale_id);
			pstmt.setString(2, user_id);
			pstmt.setString(3, group_id);
			
			pstmt.executeUpdate();
		} catch (SQLException se) {
			throw new RuntimeException("A database error occured. " + se.getMessage());
		} finally {
			try {
				if (pstmt != null) {
					pstmt.close();
				}
			} catch (SQLException se) {
				logger.error("SQLException:".concat(se.getMessage()));
			}
		}
	}

	public List<RealSaleVO> searchAllDB(String group_id) {
		List<RealSaleVO> list = new ArrayList<RealSaleVO>();
		RealSaleVO realSaleVO = null;

		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try {
			pstmt = connection.prepareStatement(sp_selectall_realsale);
			pstmt.setString(1, group_id);
			rs = pstmt.executeQuery();
			while (rs.next()) {
				realSaleVO = new RealSaleVO();

				realSaleVO.setOrder_no(rs.getString("order_no"));
				realSaleVO.setName(rs.getString("name"));
				realSaleVO.setTrans_list_date(rs.getDate("trans_list_date"));
				realSaleVO.setSale_date(rs.getDate("sale_date"));
				realSaleVO.setDis_date(rs.getDate("dis_date"));
				realSaleVO.setOrder_source(rs.getString("order_source"));
				realSaleVO.setMemo(rs.getString("memo"));
				realSaleVO.setRealsale_id(rs.getString("realsale_id"));
				realSaleVO.setOrder_source(rs.getString("order_source"));
				realSaleVO.setCustomer_id(rs.getString("customer_id"));
				realSaleVO.setInvoice(rs.getString("invoice"));
				realSaleVO.setTotal_amt(rs.getFloat("total_amt"));
				realSaleVO.setInvoice_date(rs.getDate("invoice_date"));
				list.add(realSaleVO); 
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
	
	public List<RealSaleVO> getNewSaleSeqNo(String group_id) {
		List<RealSaleVO> list = new ArrayList<RealSaleVO>();
		RealSaleVO realSaleVO = null;

		PreparedStatement pstmt = null;
		ResultSet rs = null;

		try {
			pstmt = connection.prepareStatement(sp_get_realsale_newseqno);
			pstmt.setString(1, group_id);
			rs = pstmt.executeQuery();
			while (rs.next()) {
				realSaleVO = new RealSaleVO();
				realSaleVO.setSeq_no(rs.getString("result"));
				list.add(realSaleVO);
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
	
	public List<CustomerVO> getCustomerByName(String group_id, String name) {
		List<CustomerVO> list = new ArrayList<CustomerVO>();
		CustomerVO customerVO = null;

		PreparedStatement pstmt = null;
		ResultSet rs = null;

		try {
			pstmt = connection.prepareStatement(sp_get_customer_byname);
			pstmt.setString(1, group_id);
			pstmt.setString(2, name);
			rs = pstmt.executeQuery();
			while (rs.next()) {
				customerVO = new CustomerVO();
				customerVO.setCustomer_id(rs.getString("customer_id"));
				customerVO.setName(rs.getString("name"));
				list.add(customerVO);
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

	public List<RealSaleDetailVO> getRealSaleDetail(RealSaleDetailVO realsaleDetailVO) {
		List<RealSaleDetailVO> list = new ArrayList<RealSaleDetailVO>();
		RealSaleDetailVO result = null;

		PreparedStatement pstmt = null;
		ResultSet rs = null;

		Util util = new Util();

		try {
			pstmt = connection.prepareStatement(sp_get_realsale_detail);

			String group_id = util.null2str(realsaleDetailVO.getGroup_id());
			String realsale_id = util.null2str(realsaleDetailVO.getRealsale_id());
			
			pstmt.setString(1, group_id);
			pstmt.setString(2, realsale_id);
			rs = pstmt.executeQuery();
			while (rs.next()) {
				result = new RealSaleDetailVO();
				
				result.setOrder_no(rs.getString("order_no"));
				result.setC_product_id(rs.getString("c_product_id"));
				result.setProduct_name(rs.getString("product_name"));
				result.setQuantity(rs.getInt("quantity"));
				result.setPrice(rs.getFloat("price"));			
				result.setMemo(rs.getString("memo"));

				list.add(result);
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
			} catch (Exception e) {
				logger.error("Exception:".concat(e.getMessage()));
			}
		}
		return list;
	}

	public List<RealSaleVO> searchMuliDB(String group_id,String c_order_no_begin, String c_order_no_end,String c_customerid,String c_trans_list_date_begin,String c_trans_list_date_end,String c_dis_date_begin,String c_dis_date_end,String c_order_source,String c_deliveryway, String upload_begin, String upload_end) {
		List<RealSaleVO> list = new ArrayList<RealSaleVO>();
		RealSaleVO realSaleVO = null;

		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try {
			String sqlString="SELECT RS.order_no,RS.order_source,RS.customer_id,RS.total_amt,RS.invoice,RS.invoice_date,RS.trans_list_date,RS.sale_date,RS.dis_date,RS.order_source,RS.memo,RS.realsale_id,C.name as name, RS.upload_date FROM tb_realsale RS "
							+ "LEFT JOIN tmp.tb_customer C ON RS.customer_id=C.customer_id and RS.group_id=C.group_id "
							+ "WHERE isreturn=false";
			  
			if ((c_order_no_begin.trim()).length() > 0 && (c_order_no_end.trim()).length() > 0) {
				sqlString+=" and RS.order_no between '"+ c_order_no_begin +"' and '"+ c_order_no_begin +"'";								
			} 
			if ((c_customerid.trim()).length() > 0) {
				sqlString+=" and RS.customer_id = '"+ c_customerid +"'";		
			} 
			if ((c_trans_list_date_begin.trim()).length() > 0 && (c_trans_list_date_end.trim()).length() > 0) {
				sqlString+=" and RS.trans_list_date between '"+ c_trans_list_date_begin +"' and '"+ c_trans_list_date_end +"'";
			}
			if ((c_dis_date_begin.trim()).length() > 0 && (c_dis_date_end.trim()).length() > 0) {
				sqlString+=" and RS.dis_date between '"+ c_dis_date_begin +"' and '"+ c_dis_date_end +"'";
			}	
			if ((upload_begin.trim()).length() > 0 && (upload_end.trim()).length() > 0) {
				sqlString+=" and RS.upload_date between '"+ upload_begin +"' and '"+ upload_begin +"'";
			}
			if ((c_order_source.trim()).length() > 0) {
				sqlString+=" and RS.order_source = '"+ c_order_source +"'";
			}
			if ((c_deliveryway.trim()).length() > 0) {
				sqlString+=" and RS.deliveryway = '"+ c_deliveryway +"'";		
			} 
			sqlString+=" and RS.group_id= '"+ group_id +"'";

			pstmt = connection.prepareStatement(sqlString);							
			rs = pstmt.executeQuery();
			while (rs.next()) {
				realSaleVO = new RealSaleVO();

				realSaleVO.setOrder_no(rs.getString("order_no"));
				realSaleVO.setName(rs.getString("name"));
				realSaleVO.setTrans_list_date(rs.getDate("trans_list_date"));
				realSaleVO.setSale_date(rs.getDate("sale_date"));
				realSaleVO.setDis_date(rs.getDate("dis_date"));
				realSaleVO.setOrder_source(rs.getString("order_source"));
				realSaleVO.setMemo(rs.getString("memo"));
				realSaleVO.setRealsale_id(rs.getString("realsale_id"));
				realSaleVO.setOrder_source(rs.getString("order_source"));
				realSaleVO.setCustomer_id(rs.getString("customer_id"));
				realSaleVO.setInvoice(rs.getString("invoice"));
				realSaleVO.setTotal_amt(rs.getFloat("total_amt"));
				realSaleVO.setInvoice_date(rs.getDate("invoice_date"));
				realSaleVO.setUpload_date(rs.getDate("upload_date"));
				
				list.add(realSaleVO); 
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
	
	public JSONObject importDB(String group_id,String user_id,String trans_list_date_begin,String trans_list_date_end) {
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		JSONObject jsonObject = new JSONObject();
		
		try {
			
			pstmt = connection.prepareStatement(sp_importData_realsale);
			pstmt.setString(1, group_id);
			pstmt.setString(2, user_id);
			pstmt.setString(3, trans_list_date_begin);		
			pstmt.setString(4, trans_list_date_end);			

			rs = pstmt.executeQuery();
			if (rs.next()) {
				jsonObject.put("order_no_cnt", rs.getString("order_no_cnt"));
				jsonObject.put("total_cnt", rs.getString("total_cnt"));
			}
			
		} catch (SQLException se) {
			throw new RuntimeException("A database error occured. " + se.getMessage());
		} finally {
			try {
				if (pstmt != null) {
					pstmt.close();
				}
			} catch (SQLException se) {
				logger.error("SQLException:".concat(se.getMessage()));
			}
		}
		
		return jsonObject;
	}
	
	public JSONObject importAllocInvDB(String group_id,String user_id) {
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		JSONObject jsonObject = new JSONObject();
		
		try {
			pstmt = connection.prepareStatement(sp_importData_alloc_inv);
			pstmt.setString(1, group_id);
			pstmt.setString(2, user_id);
			
			rs = pstmt.executeQuery();
			if (rs.next()) {
				jsonObject.put("order_no_cnt", rs.getString("order_no_cnt"));
				jsonObject.put("total_cnt", rs.getString("total_cnt"));
			}
		} catch (SQLException se) {
			throw new RuntimeException("A database error occured. " + se.getMessage());
		} finally {
			try {
				if (pstmt != null) {
					pstmt.close();
				}
			} catch (SQLException se) {
				logger.error("SQLException:".concat(se.getMessage()));
			}
		}
		
		return jsonObject;
	}
	
	@Test
	public void testMethods() {
		
		RealSaleDao realsaleDao = new RealSaleDao();
		
		List<RealSaleVO> realsaleVos = realsaleDao.searchAllDB("cbcc3138-5603-11e6-a532-000d3a800878");
		assertTrue(realsaleVos!=null);
		
	}
}
