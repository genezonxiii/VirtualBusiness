package tw.com.aber.dao;

import static org.junit.Assert.assertTrue;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Time;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.Test;

import tw.com.aber.util.Database;
import tw.com.aber.util.Util;
import tw.com.aber.vo.GroupVO;
import tw.com.aber.vo.InvoiceTrackVO;
import tw.com.aber.vo.ProductVO;
import tw.com.aber.vo.SaleDetailVO;
import tw.com.aber.vo.SaleVO;

public class SaleDao {
	private static final Logger logger = LogManager.getLogger(SaleDao.class);
	// 會使用到的Stored procedure
	private static final String sp_get_sale_detail = "call sp_get_sale_detail (?)";
	private static final String sp_selectall_sale = "call sp_selectall_sale (?)";
	private static final String sp_select_sale_bycproductid = "call sp_select_sale_bycproductid (?,?)";
	private static final String sp_select_sale_bytranslistdate = "call sp_select_sale_bytranslistdate(?,?,?)";
	private static final String sp_select_sale_by_upload_date = "call sp_select_sale_by_upload_date(?,?,?)";
	private static final String sp_select_sale_bydisdate = "call sp_select_sale_bydisdate(?,?,?)";
	private static final String sp_get_sale_newseqno = "call sp_get_sale_seqno(?)";
	private static final String sp_insert_sale = "call sp_insert_sale(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
	private static final String sp_del_sale = "call sp_del_sale (?,?)";
	private static final String sp_update_sale = "call sp_update_sale (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
	private static final String sp_get_product_byid = "call sp_get_product_byid (?,?)";
	private static final String sp_get_product_byname = "call sp_get_product_byname (?,?)";
	private static final String sp_insert_saleDetail = "call sp_insert_saleDetail(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
	private static final String sp_update_saleDetail = "call sp_update_saleDetail(?, ?, ?, ?, ?)";
	private static final String sp_del_saleDetail = "call sp_del_saleDetail(?)";
	private static final String sp_get_sale_orderno_info_by_ids = "call sp_get_sale_orderno_info_by_ids(?,?)";
	private static final String sp_get_group_invoice_info = "call sp_get_group_invoice_info(?)";
	private static final String sp_get_invoiceNum = "call sp_get_invoiceNum(?,?)";
	private static final String sp_update_sale_invoice = "call sp_update_sale_invoice(?,?,?,?,?,?)";
	private static final String sp_invoice_cancel = "call sp_invoice_cancel(?,?)";
	private static final String sp_update_sale_invoice_vcode_and_invoice_time = "call sp_update_sale_invoice_vcode_and_invoice_time(?,?,?,?)";
	private static final String sp_get_sale_invoice_info_by_orderno = "call sp_get_sale_invoice_info_by_orderno(?,?)";
	private static final String sp_getSaleByUploadDate_GroupByOrderNo = "call sp_getSaleByUploadDate_GroupByOrderNo(?,?,?)";
	private static final String sp_getSaleByTransDate_GroupByOrderNo = "call sp_getSaleByTransDate_GroupByOrderNo(?,?,?)";
	private static final String sp_get_sale_orderno_info_by_orderno = "call sp_get_sale_orderno_info_by_orderno(?,?)";
	private static final String sp_update_sale_turn_flag = "call sp_update_sale_turn_flag(?,?,?)";
	
	
	private Connection connection;

	public SaleDao() {
		connection = Database.getConnection();
	}

	public void insertDB(SaleVO saleVO) {
		PreparedStatement pstmt = null;
		try {
			pstmt = connection.prepareStatement(sp_insert_sale);

			pstmt.setString(1, saleVO.getSeq_no());
			pstmt.setString(2, saleVO.getGroup_id());
			pstmt.setString(3, saleVO.getOrder_no());
			pstmt.setString(4, saleVO.getUser_id());
			pstmt.setString(5, saleVO.getProduct_id());
			pstmt.setString(6, saleVO.getProduct_name());
			pstmt.setString(7, saleVO.getC_product_id());
			pstmt.setString(8, saleVO.getCustomer_id());
			pstmt.setString(9, null);// saleVO.getName());
			pstmt.setInt(10, saleVO.getQuantity());
			pstmt.setFloat(11, saleVO.getPrice());
			pstmt.setString(12, saleVO.getInvoice());
			pstmt.setDate(13, saleVO.getInvoice_date());
			pstmt.setDate(14, saleVO.getTrans_list_date());
			pstmt.setDate(15, saleVO.getDis_date());
			pstmt.setString(16, saleVO.getMemo());
			pstmt.setDate(17, saleVO.getSale_date());
			pstmt.setString(18, saleVO.getOrder_source());
			pstmt.setString(19, saleVO.getContrast_type());
			pstmt.setString(20, saleVO.getDeliveryway());
			pstmt.setFloat(21, saleVO.getTotal_amt());

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

	public void updateDB(SaleVO saleVO) {
		PreparedStatement pstmt = null;
		try {
			pstmt = connection.prepareStatement(sp_update_sale);

			pstmt.setString(1, saleVO.getSale_id());
			pstmt.setString(2, saleVO.getSeq_no());
			pstmt.setString(3, saleVO.getGroup_id());
			pstmt.setString(4, saleVO.getOrder_no());
			pstmt.setString(5, saleVO.getUser_id());
			pstmt.setString(6, saleVO.getProduct_id());
			pstmt.setString(7, saleVO.getCustomer_id());
			pstmt.setString(8, saleVO.getProduct_name());
			pstmt.setString(9, saleVO.getC_product_id());
			pstmt.setString(10, null); // saleVO.getName());
			pstmt.setInt(11, saleVO.getQuantity());
			pstmt.setFloat(12, saleVO.getPrice());
			pstmt.setString(13, saleVO.getInvoice());
			pstmt.setDate(14, saleVO.getInvoice_date());
			pstmt.setDate(15, saleVO.getTrans_list_date());
			pstmt.setDate(16, saleVO.getDis_date());
			pstmt.setString(17, saleVO.getMemo());
			pstmt.setDate(18, saleVO.getSale_date());
			pstmt.setString(19, saleVO.getOrder_source());
			pstmt.setString(20, saleVO.getContrast_type());
			pstmt.setString(21, saleVO.getDeliveryway());
			pstmt.setFloat(22, saleVO.getTotal_amt());

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

	public void deleteDB(String sale_id, String user_id) {
		PreparedStatement pstmt = null;
		try {
			pstmt = connection.prepareStatement(sp_del_sale);
			pstmt.setString(1, sale_id);
			pstmt.setString(2, user_id);

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

	public List<SaleVO> searchDB(String group_id, String c_product_id) {
		List<SaleVO> list = new ArrayList<SaleVO>();
		SaleVO saleVO = null;

		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try {
			pstmt = connection.prepareStatement(sp_select_sale_bycproductid);

			pstmt.setString(1, group_id);
			pstmt.setString(2, c_product_id);

			rs = pstmt.executeQuery();
			while (rs.next()) {
				saleVO = new SaleVO();
				saleVO.setSale_id(rs.getString("sale_id"));
				saleVO.setSeq_no(rs.getString("seq_no"));
				saleVO.setOrder_no(rs.getString("order_no"));
				saleVO.setProduct_name(rs.getString("product_name"));
				saleVO.setC_product_id(rs.getString("c_product_id"));
				saleVO.setQuantity(rs.getInt("quantity"));
				saleVO.setPrice(rs.getFloat("price"));
				saleVO.setInvoice(rs.getString("invoice"));
				saleVO.setInvoice_date(rs.getDate("invoice_date"));
				saleVO.setTrans_list_date(rs.getDate("trans_list_date"));
				saleVO.setDis_date(rs.getDate("dis_date"));
				saleVO.setMemo(rs.getString("memo"));
				saleVO.setSale_date(rs.getDate("sale_date"));
				saleVO.setOrder_source(rs.getString("order_source"));
				saleVO.setCustomer_id(rs.getString("customer_id"));
				saleVO.setName(rs.getString("name"));
				list.add(saleVO);
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

	public List<SaleVO> searchAllDB(String group_id) {
		List<SaleVO> list = new ArrayList<SaleVO>();
		SaleVO saleVO = null;

		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try {
			pstmt = connection.prepareStatement(sp_selectall_sale);
			pstmt.setString(1, group_id);
			rs = pstmt.executeQuery();
			while (rs.next()) {
				saleVO = new SaleVO();
				saleVO.setSale_id(rs.getString("sale_id"));
				saleVO.setSeq_no(rs.getString("seq_no"));
				saleVO.setOrder_no(rs.getString("order_no"));
				saleVO.setProduct_id(rs.getString("product_id"));
				saleVO.setProduct_name(rs.getString("product_name"));
				saleVO.setC_product_id(rs.getString("c_product_id"));
				saleVO.setQuantity(rs.getInt("quantity"));
				saleVO.setPrice(rs.getFloat("price"));
				saleVO.setInvoice(rs.getString("invoice"));
				saleVO.setInvoice_date(rs.getDate("invoice_date"));
				saleVO.setTrans_list_date(rs.getDate("trans_list_date"));
				saleVO.setDis_date(rs.getDate("dis_date"));
				saleVO.setMemo(rs.getString("memo"));
				saleVO.setSale_date(rs.getDate("sale_date"));
				saleVO.setOrder_source(rs.getString("order_source"));
				saleVO.setCustomer_id(rs.getString("customer_id"));
				saleVO.setName(rs.getString("name"));
				saleVO.setTurnFlag(rs.getBoolean("turn_flag"));
				
				list.add(saleVO); // Store the row in the list
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

	public List<SaleVO> searchTransListDateDB(String group_id, String trans_list_start_date,
			String trans_list_end_date) {
		List<SaleVO> list = new ArrayList<SaleVO>();
		SaleVO saleVO = null;

		PreparedStatement pstmt = null;
		ResultSet rs = null;

		try {
			pstmt = connection.prepareStatement(sp_select_sale_bytranslistdate);
			pstmt.setString(1, group_id);
			pstmt.setString(2, trans_list_start_date);
			pstmt.setString(3, trans_list_end_date);
			rs = pstmt.executeQuery();
			while (rs.next()) {
				saleVO = new SaleVO();
				saleVO.setSale_id(rs.getString("sale_id"));
				saleVO.setSeq_no(rs.getString("seq_no"));
				saleVO.setOrder_no(rs.getString("order_no"));
				saleVO.setProduct_name(rs.getString("product_name"));
				saleVO.setC_product_id(rs.getString("c_product_id"));
				saleVO.setQuantity(rs.getInt("quantity"));
				saleVO.setPrice(rs.getFloat("price"));
				saleVO.setInvoice(rs.getString("invoice"));
				saleVO.setInvoice_date(rs.getDate("invoice_date"));
				saleVO.setTrans_list_date(rs.getDate("trans_list_date"));
				saleVO.setDis_date(rs.getDate("dis_date"));
				saleVO.setMemo(rs.getString("memo"));
				saleVO.setSale_date(rs.getDate("sale_date"));
				saleVO.setOrder_source(rs.getString("order_source"));
				saleVO.setCustomer_id(rs.getString("customer_id"));
				saleVO.setName(rs.getString("name"));
				saleVO.setUpload_date(rs.getDate("upload_date"));
				saleVO.setTurnFlag(rs.getBoolean("turn_flag"));
				
				list.add(saleVO);
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

	public List<SaleVO> searchUploadDateDB(String group_id, String startDate, String endDate) {
		List<SaleVO> list = new ArrayList<SaleVO>();
		SaleVO saleVO = null;

		PreparedStatement pstmt = null;
		ResultSet rs = null;

		try {
			pstmt = connection.prepareStatement(sp_select_sale_by_upload_date);
			pstmt.setString(1, group_id);
			pstmt.setString(2, startDate);
			pstmt.setString(3, endDate);
			rs = pstmt.executeQuery();
			while (rs.next()) {
				saleVO = new SaleVO();
				saleVO.setSale_id(rs.getString("sale_id"));
				saleVO.setSeq_no(rs.getString("seq_no"));
				saleVO.setOrder_no(rs.getString("order_no"));
				saleVO.setProduct_name(rs.getString("product_name"));
				saleVO.setC_product_id(rs.getString("c_product_id"));
				saleVO.setQuantity(rs.getInt("quantity"));
				saleVO.setPrice(rs.getFloat("price"));
				saleVO.setInvoice(rs.getString("invoice"));
				saleVO.setInvoice_date(rs.getDate("invoice_date"));
				saleVO.setTrans_list_date(rs.getDate("trans_list_date"));
				saleVO.setUpload_date(rs.getDate("upload_date"));
				saleVO.setDis_date(rs.getDate("dis_date"));
				saleVO.setMemo(rs.getString("memo"));
				saleVO.setSale_date(rs.getDate("sale_date"));
				saleVO.setOrder_source(rs.getString("order_source"));
				saleVO.setCustomer_id(rs.getString("customer_id"));
				saleVO.setName(rs.getString("name"));
				saleVO.setTurnFlag(rs.getBoolean("turn_flag"));
				
				list.add(saleVO);
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

	public List<SaleVO> getNewSaleSeqNo(String group_id) {
		List<SaleVO> list = new ArrayList<SaleVO>();
		SaleVO saleVO = null;

		PreparedStatement pstmt = null;
		ResultSet rs = null;

		try {
			pstmt = connection.prepareStatement(sp_get_sale_newseqno);
			pstmt.setString(1, group_id);
			rs = pstmt.executeQuery();
			while (rs.next()) {
				saleVO = new SaleVO();
				saleVO.setSeq_no(rs.getString("seq_no"));
				list.add(saleVO);
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

	public List<SaleVO> searchDisDateDB(String group_id, String dis_start_date, String dis_end_date) {
		List<SaleVO> list = new ArrayList<SaleVO>();
		SaleVO saleVO = null;

		PreparedStatement pstmt = null;
		ResultSet rs = null;

		try {
			pstmt = connection.prepareStatement(sp_select_sale_bydisdate);
			pstmt.setString(1, group_id);
			pstmt.setString(2, dis_start_date);
			pstmt.setString(3, dis_end_date);
			rs = pstmt.executeQuery();
			while (rs.next()) {
				saleVO = new SaleVO();
				saleVO.setSale_id(rs.getString("sale_id"));
				saleVO.setSeq_no(rs.getString("seq_no"));
				saleVO.setOrder_no(rs.getString("order_no"));
				saleVO.setProduct_name(rs.getString("product_name"));
				saleVO.setC_product_id(rs.getString("c_product_id"));
				saleVO.setQuantity(rs.getInt("quantity"));
				saleVO.setPrice(rs.getFloat("price"));
				saleVO.setInvoice(rs.getString("invoice"));
				saleVO.setInvoice_date(rs.getDate("invoice_date"));
				saleVO.setTrans_list_date(rs.getDate("trans_list_date"));
				saleVO.setDis_date(rs.getDate("dis_date"));
				saleVO.setMemo(rs.getString("memo"));
				saleVO.setSale_date(rs.getDate("sale_date"));
				saleVO.setOrder_source(rs.getString("order_source"));
				saleVO.setCustomer_id(rs.getString("customer_id"));
				saleVO.setName(rs.getString("name"));

				list.add(saleVO);
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

	public List<ProductVO> getProductByName(String group_id, String product_name) {
		List<ProductVO> list = new ArrayList<ProductVO>();
		ProductVO productVO = null;

		PreparedStatement pstmt = null;
		ResultSet rs = null;

		try {
			pstmt = connection.prepareStatement(sp_get_product_byname);
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

	public List<ProductVO> getProductById(String group_id, String c_product_id) {
		List<ProductVO> list = new ArrayList<ProductVO>();
		ProductVO productVO = null;

		PreparedStatement pstmt = null;
		ResultSet rs = null;

		try {
			pstmt = connection.prepareStatement(sp_get_product_byid);
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

	public List<SaleDetailVO> getSaleDetail(SaleDetailVO saleDetailVO) {
		List<SaleDetailVO> list = new ArrayList<SaleDetailVO>();
		SaleDetailVO result = null;

		PreparedStatement pstmt = null;
		ResultSet rs = null;

		Util util = new Util();

		try {
			pstmt = connection.prepareStatement(sp_get_sale_detail);

			String sale_id = util.null2str(saleDetailVO.getSale_id());

			pstmt.setString(1, sale_id);
			rs = pstmt.executeQuery();
			while (rs.next()) {
				result = new SaleDetailVO();

				result.setSaleDetail_id(rs.getString("saleDetail_id"));
				result.setSeq_no(rs.getString("seq_no"));
				result.setGroup_id(rs.getString("group_id"));
				result.setOrder_no(rs.getString("order_no"));
				result.setUser_id(rs.getString("user_id"));
				result.setProduct_id(rs.getString("product_id"));
				result.setProduct_name(rs.getString("product_name"));
				result.setC_product_id(rs.getString("c_product_id"));
				result.setCustomer_id(rs.getString("customer_id"));
				result.setName(rs.getString("name"));
				result.setQuantity(rs.getInt("quantity"));
				result.setPrice(rs.getFloat("price"));
				result.setInvoice(rs.getString("invoice"));
				result.setInvoice_date(rs.getDate("invoice_date"));
				result.setTrans_list_date(rs.getDate("trans_list_date"));
				result.setDis_date(rs.getDate("dis_date"));
				result.setMemo(rs.getString("memo"));
				result.setSale_date(rs.getDate("sale_date"));
				result.setOrder_source(rs.getString("order_source"));
				result.setReturn_date(rs.getDate("return_date"));
				result.setIsreturn(rs.getInt("isreturn"));
				result.setDeliveryway(rs.getString("deliveryway"));

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
			}
		}
		return list;
	}

	public void insertDetailDB(SaleDetailVO paramVO) {
		PreparedStatement pstmt = null;
		try {
			pstmt = connection.prepareStatement(sp_insert_saleDetail);

			pstmt.setString(1, paramVO.getSale_id());
			pstmt.setString(2, paramVO.getSeq_no());
			pstmt.setString(3, paramVO.getGroup_id());
			pstmt.setString(4, paramVO.getOrder_no());
			pstmt.setString(5, paramVO.getUser_id());
			pstmt.setString(6, paramVO.getProduct_id());
			pstmt.setString(7, paramVO.getProduct_name());
			pstmt.setString(8, paramVO.getC_product_id());
			pstmt.setString(9, null);// customer_id
			pstmt.setString(10, null); // name
			pstmt.setFloat(11, paramVO.getQuantity());
			pstmt.setFloat(12, paramVO.getPrice());
			pstmt.setString(13, null);// invoice
			pstmt.setDate(14, null);// Invoice_date
			pstmt.setDate(15, null);// Trans_list_date
			pstmt.setDate(16, null);// Dis_date
			pstmt.setString(17, null);// Memo
			pstmt.setDate(18, null);// Sale_date
			pstmt.setString(19, null);// Order_source

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

	public void updateDetailDB(SaleDetailVO paramVO) {
		PreparedStatement pstmt = null;
		try {
			pstmt = connection.prepareStatement(sp_update_saleDetail);

			pstmt.setString(1, paramVO.getSaleDetail_id());
			pstmt.setString(2, paramVO.getProduct_name());
			pstmt.setString(3, paramVO.getC_product_id());
			pstmt.setFloat(4, paramVO.getQuantity());
			pstmt.setFloat(5, paramVO.getPrice());

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

	public void deleteDetailDB(String saleDetail_id) {
		PreparedStatement pstmt = null;
		try {
			pstmt = connection.prepareStatement(sp_del_saleDetail);
			pstmt.setString(1, saleDetail_id);

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

	public List<SaleVO> getSaleOrdernoInfoByIds(String groupId, String saleIds) {
		List<SaleVO> list = new ArrayList<SaleVO>();
		SaleVO saleVO = null;

		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try {
			pstmt = connection.prepareStatement(sp_get_sale_orderno_info_by_ids);
			pstmt.setString(1, groupId);
			pstmt.setString(2, saleIds);
			rs = pstmt.executeQuery();
			while (rs.next()) {
				saleVO = new SaleVO();
				saleVO.setGroup_id(rs.getString("group_id"));
				saleVO.setSale_id(rs.getString("sale_id"));
				saleVO.setSeq_no(rs.getString("seq_no"));
				saleVO.setOrder_no(rs.getString("order_no"));
				saleVO.setProduct_id(rs.getString("product_id"));
				saleVO.setProduct_name(rs.getString("product_name"));
				saleVO.setC_product_id(rs.getString("c_product_id"));
				saleVO.setQuantity(rs.getInt("quantity"));
				saleVO.setPrice(rs.getFloat("price"));
				saleVO.setInvoice(rs.getString("invoice"));
				saleVO.setInvoice_date(rs.getDate("invoice_date"));
				saleVO.setTrans_list_date(rs.getDate("trans_list_date"));
				saleVO.setDis_date(rs.getDate("dis_date"));
				saleVO.setMemo(rs.getString("memo"));
				saleVO.setSale_date(rs.getDate("sale_date"));
				saleVO.setOrder_source(rs.getString("order_source"));
				saleVO.setCustomer_id(rs.getString("customer_id"));
				saleVO.setName(rs.getString("name"));
				saleVO.setInvoice_date(rs.getDate("invoice_date"));
				saleVO.setInvoice_vcode(rs.getString("invoice_vcode"));
				saleVO.setInvoice_time(rs.getTime("invoice_time"));
				list.add(saleVO); // Store the row in the list
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

	public GroupVO getGroupInvoiceInfo(String groupId) {
		GroupVO groupVO = null;

		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try {
			pstmt = connection.prepareStatement(sp_get_group_invoice_info);
			pstmt.setString(1, groupId);
			rs = pstmt.executeQuery();
			while (rs.next()) {
				groupVO = new GroupVO();
				groupVO.setGroup_unicode(rs.getString("group_unicode"));
				groupVO.setInvoice_key(rs.getString("invoice_key"));
				groupVO.setInvoice_posno(rs.getString("invoice_posno"));
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
		return groupVO;
	}

	public InvoiceTrackVO getInvoiceTrack(String group_id, Date invoice_num_date) {
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		String InvoiceNum = null;
		InvoiceTrackVO invoiceTrackVO = new InvoiceTrackVO();

		try {
			pstmt = connection.prepareStatement(sp_get_invoiceNum);
			pstmt.setString(1, group_id);
			pstmt.setDate(2, invoice_num_date);
			pstmt.execute();
			rs = pstmt.getResultSet();
			if (rs.next()) {

				InvoiceNum = rs.getString("invoiceNum");
				if (InvoiceNum != null) {
					invoiceTrackVO.setGroup_id(group_id);
					invoiceTrackVO.setInvoice_beginno(rs.getString("invoice_beginno"));
					invoiceTrackVO.setInvoice_endno(rs.getString("invoice_endno"));
					invoiceTrackVO.setInvoice_id(rs.getString("invoice_id"));
					invoiceTrackVO.setInvoice_track(rs.getString("invoice_track"));
					invoiceTrackVO.setInvoice_type(rs.getString("invoice_type"));
					invoiceTrackVO.setInvoiceNum(InvoiceNum);
					invoiceTrackVO.setSeq(rs.getString("seq"));
					invoiceTrackVO.setYear_month(rs.getString("year_month"));

				}
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
		return invoiceTrackVO;
	}

	public void updateSaleInvoice(List<SaleVO> SaleVOs, InvoiceTrackVO invoiceTrackVO, Date invoice_num_date) {
		PreparedStatement pstmt = null;
		try {

			for (int i = 0; i < SaleVOs.size(); i++) {

				pstmt = connection.prepareStatement(sp_update_sale_invoice);
				pstmt.setString(1, SaleVOs.get(i).getGroup_id());
				pstmt.setString(2, SaleVOs.get(i).getSale_id());
				pstmt.setString(3, invoiceTrackVO.getInvoiceNum());
				pstmt.setString(4, invoiceTrackVO.getInvoice_type());
				pstmt.setString(5, invoiceTrackVO.getYear_month());
				pstmt.setDate(6, invoice_num_date);

				pstmt.executeUpdate();
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
	}

	public void invoiceCancel(String group_id, String sale_ids) {
		PreparedStatement pstmt = null;
		try {
			pstmt = connection.prepareStatement(sp_invoice_cancel);
			pstmt.setString(1, "'" + group_id + "'");
			pstmt.setString(2, sale_ids);
			pstmt.execute();

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

	public void updateSaleInvoiceVcodeAndInvoice_time(List<SaleVO> SaleVOs, String InvoiceVcode, String Invoice_time) {
		PreparedStatement pstmt = null;
		Time timeValue = null;
		try {

			DateFormat formatter = new SimpleDateFormat("HH:mm:ss");
			try {
				timeValue = new Time(formatter.parse(Invoice_time).getTime());
			} catch (ParseException e) {
				logger.debug("time_error:" + e.getMessage());
			}

			for (int i = 0; i < SaleVOs.size(); i++) {

				pstmt = connection.prepareStatement(sp_update_sale_invoice_vcode_and_invoice_time);
				pstmt.setString(1, SaleVOs.get(i).getGroup_id());
				pstmt.setString(2, SaleVOs.get(i).getSale_id());
				pstmt.setString(3, InvoiceVcode);
				pstmt.setTime(4, timeValue);
				pstmt.executeUpdate();
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
	}

	public SaleVO getSaleInvoiceInfoByOrderNo(String groupId, String orderNo) {
		SaleVO saleVO = null;

		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try {
			pstmt = connection.prepareStatement(sp_get_sale_invoice_info_by_orderno);
			pstmt.setString(1, groupId);
			pstmt.setString(2, orderNo);
			rs = pstmt.executeQuery();
			while (rs.next()) {
				saleVO = new SaleVO();
				saleVO.setProduct_name("商品");
				saleVO.setQuantity(Integer.valueOf("1"));
				saleVO.setPrice(rs.getFloat("total_amount"));
				saleVO.setOrder_no(rs.getString("order_no"));
				saleVO.setInvoice(rs.getString("invoice"));
				saleVO.setInvoice_date(rs.getDate("invoice_date"));
				saleVO.setInvoice_vcode(rs.getString("invoice_vcode"));
				saleVO.setInvoice_time(rs.getTime("invoice_time"));

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
		return saleVO;
	}

	public List<SaleVO> getSaleByUploadDate(String group_id, String upload_date_start, String upload_date_end) {
		List<SaleVO> list = new ArrayList<SaleVO>();
		SaleVO saleVO = null;

		PreparedStatement pstmt = null;
		ResultSet rs = null;

		try {
			pstmt = connection.prepareStatement(sp_getSaleByUploadDate_GroupByOrderNo);
			pstmt.setString(1, group_id);
			pstmt.setString(2, upload_date_start);
			pstmt.setString(3, upload_date_end);
			rs = pstmt.executeQuery();
			while (rs.next()) {
				saleVO = new SaleVO();
				saleVO.setOrder_no(rs.getString("order_no"));
				saleVO.setTrans_list_date(rs.getDate("trans_list_date"));
				saleVO.setUpload_date(rs.getDate("upload_date"));
				saleVO.setTotal_amt(rs.getFloat("total_amt"));
				saleVO.setInvoice(rs.getString("invoice"));
				saleVO.setInvoice_date(rs.getDate("invoice_date"));
				saleVO.setOrder_source(rs.getString("order_source"));
				list.add(saleVO);
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

	public List<SaleVO> getSaleByTransDate(String group_id, String upload_date_start, String upload_date_end) {
		List<SaleVO> list = new ArrayList<SaleVO>();
		SaleVO saleVO = null;

		PreparedStatement pstmt = null;
		ResultSet rs = null;

		try {
			pstmt = connection.prepareStatement(sp_getSaleByTransDate_GroupByOrderNo);
			pstmt.setString(1, group_id);
			pstmt.setString(2, upload_date_start);
			pstmt.setString(3, upload_date_end);
			rs = pstmt.executeQuery();
			while (rs.next()) {
				saleVO = new SaleVO();
				saleVO.setOrder_no(rs.getString("order_no"));
				saleVO.setTrans_list_date(rs.getDate("trans_list_date"));
				saleVO.setUpload_date(rs.getDate("upload_date"));
				saleVO.setTotal_amt(rs.getFloat("total_amt"));
				saleVO.setInvoice(rs.getString("invoice"));
				saleVO.setInvoice_date(rs.getDate("invoice_date"));
				saleVO.setOrder_source(rs.getString("order_source"));
				list.add(saleVO);
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

	public List<SaleVO> getSaleOrdernoInfoByOrdernos(String groupId, String order_nos) {
		List<SaleVO> list = new ArrayList<SaleVO>();
		SaleVO saleVO = null;

		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try {
			pstmt = connection.prepareStatement(sp_get_sale_orderno_info_by_orderno);
			pstmt.setString(1, groupId);
			pstmt.setString(2, order_nos);
			rs = pstmt.executeQuery();
			while (rs.next()) {
				saleVO = new SaleVO();
				saleVO.setGroup_id(rs.getString("group_id"));
				saleVO.setSale_id(rs.getString("sale_id"));
				saleVO.setSeq_no(rs.getString("seq_no"));
				saleVO.setOrder_no(rs.getString("order_no"));
				saleVO.setProduct_id(rs.getString("product_id"));
				saleVO.setProduct_name(rs.getString("product_name"));
				saleVO.setC_product_id(rs.getString("c_product_id"));
				saleVO.setQuantity(rs.getInt("quantity"));
				saleVO.setPrice(rs.getFloat("price"));
				saleVO.setInvoice(rs.getString("invoice"));
				saleVO.setInvoice_date(rs.getDate("invoice_date"));
				saleVO.setTrans_list_date(rs.getDate("trans_list_date"));
				saleVO.setDis_date(rs.getDate("dis_date"));
				saleVO.setMemo(rs.getString("memo"));
				saleVO.setSale_date(rs.getDate("sale_date"));
				saleVO.setOrder_source(rs.getString("order_source"));
				saleVO.setCustomer_id(rs.getString("customer_id"));
				saleVO.setName(rs.getString("name"));
				saleVO.setInvoice_date(rs.getDate("invoice_date"));
				saleVO.setInvoice_vcode(rs.getString("invoice_vcode"));
				saleVO.setInvoice_time(rs.getTime("invoice_time"));
				list.add(saleVO); // Store the row in the list
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
	
	public void updateTurnFlag(SaleVO saleVO) {
		PreparedStatement pstmt = null;
		try {
			pstmt = connection.prepareStatement(sp_update_sale_turn_flag);

			pstmt.setString(1, saleVO.getGroup_id());
			pstmt.setString(2, saleVO.getSale_id());
			pstmt.setBoolean(3, saleVO.getTurnFlag());

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
	
	@Test
	public void testMethods() {
		
		SaleDao saleDao = new SaleDao();
		
		List<SaleVO> saleVOs = saleDao.searchTransListDateDB("cbcc3138-5603-11e6-a532-000d3a800878","2017-12-25","2017-12-25" );
		assertTrue(saleVOs!=null);
		
	}
	
}