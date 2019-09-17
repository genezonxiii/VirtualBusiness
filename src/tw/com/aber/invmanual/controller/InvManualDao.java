package tw.com.aber.invmanual.controller;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import tw.com.aber.inv.vo.Invoice;
import tw.com.aber.util.Database;
import tw.com.aber.vo.GroupVO;
import tw.com.aber.vo.InvBuyerVO;
import tw.com.aber.vo.InvManualDetailVO;
import tw.com.aber.vo.InvManualVO;
import tw.com.aber.vo.InvoiceTrackVO;

public 	class InvManualDao implements InvManualInterface {
	private static final Logger logger = LogManager.getLogger(InvManualDao.class);
	
	private Connection connection;

	public InvManualDao() {
		connection = Database.getConnection();
	}
	
	private static final String sp_select_all_inv_manual = "call sp_select_all_inv_manual (?)";
	private static final String sp_select_inv_manual_by_invoice_date = "call sp_select_inv_manual_by_invoice_date (?,?,?)";
	private static final String sp_insert_inv_manual = "call sp_insert_inv_manual(?,?,?,?,?,?,?,?,?)";
	private static final String sp_select_inv_manual_detail_by_inv_manual_id = "call sp_select_inv_manual_detail_by_inv_manual_id(?,?)";
	private static final String sp_insert_inv_manual_detail = "call sp_insert_inv_manual_detail(?,?,?,?,?,?,?)";
	private static final String sp_del_inv_manual_detail = "call sp_del_inv_manual_detail(?,?,?)";
	private static final String sp_del_inv_manual = "call sp_del_inv_manual(?,?)";
	private static final String sp_update_inv_manual = "call sp_update_inv_manual(?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
	private static final String sp_update_inv_manual_detail = "call sp_update_inv_manual_detail(?,?,?,?,?,?,?,?)";
	private static final String sp_get_invoiceNum = "call sp_get_invoiceNum(?,?)";
	private static final String sp_inc_invoice_track = "call sp_inc_invoice_track(?,?,?)";
	private static final String sp_select_inv_manual_by_inv_manual_id = "call sp_select_inv_manual_by_inv_manual_id(?,?)";
	private static final String sp_get_group_invoice_info = "call sp_get_group_invoice_info(?)";
	private static final String sp_update_inv_manual_inv_flag = "call sp_update_inv_manual_inv_flag(?,?,?,?,?,?,?)";
	private static final String sp_update_inv_manual_inv_flag_pershing = "call sp_update_inv_manual_inv_flag_pershing(?,?)";
	private static final String sp_update_inv_manual_canel_invoice = "call sp_update_inv_manual_canel_invoice(?,?,?,?)";
	private static final String sp_select_inv_buyer_by_unicode_or_title = "call sp_select_inv_buyer_by_unicode_or_title (?,?,?)";

	@Override
	public List<InvManualVO> searchAllInvManual(String groupId) {
		List<InvManualVO> rows = new ArrayList<InvManualVO>();
		InvManualVO row = null;

		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try {
			pstmt = connection.prepareStatement(sp_select_all_inv_manual);

			pstmt.setString(1, groupId);

			rs = pstmt.executeQuery();
			while (rs.next()) {
				row = new InvManualVO();
				row.setInv_manual_id(rs.getString("inv_manual_id"));
				row.setGroup_id(rs.getString("group_id"));
				row.setInvoice_type(rs.getString("invoice_type"));
				row.setYear_month(rs.getString("year_month"));
				row.setInvoice_no(rs.getString("invoice_no"));
				row.setInvoice_date(rs.getDate("invoice_date"));
				row.setInvoice_reason(rs.getString("invoice_reason"));
				row.setTitle(rs.getString("title"));
				row.setUnicode(rs.getString("unicode"));
				row.setAddress(rs.getString("address"));
				row.setMemo(rs.getString("memo"));
				row.setAmount(rs.getInt("amount"));
				row.setTax_type(rs.getInt("tax_type"));
				row.setInv_flag(rs.getInt("inv_flag"));
				row.setAmount_plustax(rs.getInt("amount_plustax"));
				row.setTax(rs.getInt("tax"));
				rows.add(row);
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
		return rows;
	}

	@Override
	public List<InvManualVO> searchInvManualByInvoiceDate(String groupId, String startDate, String endDate) {
		List<InvManualVO> rows = new ArrayList<InvManualVO>();
		InvManualVO row = null;

		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try {
			pstmt = connection.prepareStatement(sp_select_inv_manual_by_invoice_date);

			pstmt.setString(1, groupId);
			pstmt.setString(2, startDate);
			pstmt.setString(3, endDate);

			rs = pstmt.executeQuery();
			while (rs.next()) {
				row = new InvManualVO();
				row.setInv_manual_id(rs.getString("inv_manual_id"));
				row.setGroup_id(rs.getString("group_id"));
				row.setInvoice_type(rs.getString("invoice_type"));
				row.setYear_month(rs.getString("year_month"));
				row.setInvoice_no(rs.getString("invoice_no"));
				row.setInvoice_date(rs.getDate("invoice_date"));
				row.setInvoice_reason(rs.getString("invoice_reason"));
				row.setTitle(rs.getString("title"));
				row.setUnicode(rs.getString("unicode"));
				row.setAddress(rs.getString("address"));
				row.setMemo(rs.getString("memo"));
				row.setAmount(rs.getInt("amount"));
				row.setTax_type(rs.getInt("tax_type"));
				row.setInv_flag(rs.getInt("inv_flag"));
				row.setAmount_plustax(rs.getInt("amount_plustax"));
				row.setTax(rs.getInt("tax"));
				rows.add(row);
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
		return rows;
	}

	@Override
	public void insertInvManual(InvManualVO invManualVO) {
		CallableStatement cs = null;

		try {
			cs = connection.prepareCall(sp_insert_inv_manual);

			cs.setString(1, invManualVO.getGroup_id());
			cs.setString(2, invManualVO.getInvoice_type());
			cs.setString(3, invManualVO.getYear_month());
			cs.setDate(4, invManualVO.getInvoice_date());
			cs.setString(5, invManualVO.getTitle());
			cs.setString(6, invManualVO.getUnicode());
			cs.setString(7, invManualVO.getAddress());
			cs.setString(8, invManualVO.getMemo());
			cs.setInt(9, invManualVO.getTax_type());

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

	@Override
	public List<InvManualDetailVO> searchInvManualDetailByInvManualId(InvManualDetailVO invManualDetailVO) {
		List<InvManualDetailVO> rows = new ArrayList<InvManualDetailVO>();
		InvManualDetailVO row = null;

		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try {
			pstmt = connection.prepareStatement(sp_select_inv_manual_detail_by_inv_manual_id);

			pstmt.setString(1, invManualDetailVO.getGroup_id());
			pstmt.setString(2, invManualDetailVO.getInv_manual_id());

			rs = pstmt.executeQuery();
			while (rs.next()) {
				row = new InvManualDetailVO();
				row.setInv_manual_detail_id(rs.getString("inv_manual_detail_id"));
				row.setInv_manual_id(rs.getString("inv_manual_id"));
				row.setDescription(rs.getString("description"));
				row.setQuantity(rs.getInt("quantity"));
				row.setPrice(rs.getInt("price"));
				row.setSubtotal(rs.getInt("subtotal"));
				row.setInv_flag(rs.getInt("inv_flag"));
				row.setMemo(rs.getString("memo"));
				rows.add(row);
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
		return rows;
	}

	@Override
	public void insertInvManualDetail(InvManualDetailVO invManualDetailVO) {
		CallableStatement cs = null;

		try {
			cs = connection.prepareCall(sp_insert_inv_manual_detail);

			cs.setString(1, invManualDetailVO.getInv_manual_id());
			cs.setString(2, invManualDetailVO.getGroup_id());
			cs.setString(3, invManualDetailVO.getDescription());
			cs.setInt(4, invManualDetailVO.getPrice());
			cs.setInt(5, invManualDetailVO.getQuantity());
			cs.setInt(6, invManualDetailVO.getSubtotal());
			cs.setString(7, invManualDetailVO.getMemo());

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

	@Override
	public void delInvManualDetail(InvManualDetailVO invManualDetailVO) {
		PreparedStatement pstmt = null;
		try {
			pstmt = connection.prepareStatement(sp_del_inv_manual_detail);
			pstmt.setString(1, invManualDetailVO.getInv_manual_detail_id());
			pstmt.setString(2, invManualDetailVO.getInv_manual_id());
			pstmt.setString(3, invManualDetailVO.getGroup_id());

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
			} catch (Exception e) {
				logger.error("Exception:".concat(e.getMessage()));
			}
		}
	}

	@Override
	public void delInvManual(InvManualVO invManualVO) {
		PreparedStatement pstmt = null;
		try {
			pstmt = connection.prepareStatement(sp_del_inv_manual);
			pstmt.setString(1, invManualVO.getInv_manual_id());
			pstmt.setString(2, invManualVO.getGroup_id());

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
			} catch (Exception e) {
				logger.error("Exception:".concat(e.getMessage()));
			}
		}
	}

	@Override
	public void updateInvManual(InvManualVO invManualVO) {
		CallableStatement cs = null;

		try {
			cs = connection.prepareCall(sp_update_inv_manual);

			cs.setString(1, invManualVO.getInv_manual_id());
			cs.setString(2, invManualVO.getGroup_id());
			cs.setString(3, invManualVO.getInvoice_type());
			cs.setString(4, invManualVO.getYear_month());
			cs.setString(5, invManualVO.getInvoice_no());
			cs.setDate(6, invManualVO.getInvoice_date());
			cs.setString(7, invManualVO.getTitle());
			cs.setString(8, invManualVO.getUnicode());
			cs.setString(9, invManualVO.getAddress());
			cs.setString(10, invManualVO.getMemo());
			cs.setInt(11, invManualVO.getTax_type());
			cs.setInt(12, invManualVO.getAmount());
			cs.setInt(13, invManualVO.getAmount_plustax());
			cs.setInt(14, invManualVO.getTax());

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

	@Override
	public void updateInvManualDetail(InvManualDetailVO invManualDetailVO) {
		CallableStatement cs = null;

		try {
			cs = connection.prepareCall(sp_update_inv_manual_detail);

			cs.setString(1, invManualDetailVO.getInv_manual_detail_id());
			cs.setString(2, invManualDetailVO.getInv_manual_id());
			cs.setString(3, invManualDetailVO.getGroup_id());
			cs.setString(4, invManualDetailVO.getDescription());
			cs.setInt(5, invManualDetailVO.getPrice());
			cs.setInt(6, invManualDetailVO.getQuantity());
			cs.setInt(7, invManualDetailVO.getSubtotal());
			cs.setString(8, invManualDetailVO.getMemo());

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

	@Override
	public InvoiceTrackVO getInvoiceTrack(String groupId, Date invoice_date) {
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		String InvoiceNum = null;
		InvoiceTrackVO invoiceTrackVO = new InvoiceTrackVO();

		try {
			pstmt = connection.prepareStatement(sp_get_invoiceNum);
			pstmt.setString(1, groupId);
			pstmt.setDate(2, invoice_date);
			pstmt.execute();
			rs = pstmt.getResultSet();

			if (rs.next()) {

				InvoiceNum = rs.getString("invoiceNum");

				if (InvoiceNum != null) {
					invoiceTrackVO.setGroup_id(groupId);
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
			} catch (Exception e) {
				logger.error("Exception:".concat(e.getMessage()));
			}
		}
		return invoiceTrackVO;
	}
	
	@Override
	public Boolean increaseInvoiceTrack(InvoiceTrackVO invoiceTrackVO){
		PreparedStatement pstmt = null;
		try {
			pstmt = connection.prepareStatement(sp_inc_invoice_track);
			pstmt.setString(1, invoiceTrackVO.getGroup_id());
			pstmt.setString(2, invoiceTrackVO.getInvoice_id());
			pstmt.setString(3, invoiceTrackVO.getSeq());
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
		return true;
	}
	
	@Override
	public InvManualVO selectInvManualByInvManualId(String groupId, String inv_manual_id) {
		InvManualVO row = null;

		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try {
			pstmt = connection.prepareStatement(sp_select_inv_manual_by_inv_manual_id);

			pstmt.setString(1, groupId);
			pstmt.setString(2, inv_manual_id);

			rs = pstmt.executeQuery();
			while (rs.next()) {
				row = new InvManualVO();
				row.setInv_manual_id(rs.getString("inv_manual_id"));
				row.setGroup_id(rs.getString("group_id"));
				row.setInvoice_type(rs.getString("invoice_type"));
				row.setYear_month(rs.getString("year_month"));
				row.setInvoice_no(rs.getString("invoice_no"));
				row.setInvoice_date(rs.getDate("invoice_date"));
				row.setTitle(rs.getString("title"));
				row.setUnicode(rs.getString("unicode"));
				row.setAmount(rs.getInt("amount"));
				row.setInv_flag(rs.getInt("inv_flag"));
				row.setTax_type(rs.getInt("tax_type"));
				row.setAddress(rs.getString("address"));
				row.setMemo(rs.getString("memo"));
				row.setTax(rs.getInt("tax"));
				row.setAmount_plustax(rs.getInt("amount_plustax"));
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
		return row;
	}

	@Override
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
				groupVO.setGroup_name(rs.getString("group_name"));
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
		return groupVO;
	}

	@Override
	public void updateInvManualInvFlag(InvManualVO invManualVO, InvoiceTrackVO invoiceTrackVO, Invoice invoice) {
		CallableStatement cs = null;

		try {
			cs = connection.prepareCall(sp_update_inv_manual_inv_flag);

			cs.setString(1, invManualVO.getGroup_id());
			cs.setString(2, invManualVO.getInvoice_no());
			cs.setString(3, invManualVO.getInv_manual_id());
			cs.setString(4, invoiceTrackVO.getInvoice_type());
			cs.setString(5, invoiceTrackVO.getYear_month());
			cs.setString(6, invoice.getMain().getInvoiceDate());
			cs.setString(7, invoice.getMain().getInvoiceTime());
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
	
	@Override
	public void updateInvManualInvFlagPershing(InvManualVO invManualVO) {
		CallableStatement cs = null;

		try {
			cs = connection.prepareCall(sp_update_inv_manual_inv_flag_pershing);

			cs.setString(1, invManualVO.getGroup_id());
			cs.setString(2, invManualVO.getInv_manual_id());
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
	
	@Override
	public void updateInvManualForCancelInvoice(InvManualVO invManualVO){
		CallableStatement cs = null;

		try {
			cs = connection.prepareCall(sp_update_inv_manual_canel_invoice);

			cs.setString(1, invManualVO.getGroup_id());
			cs.setString(2, invManualVO.getInvoice_no());
			cs.setString(3, invManualVO.getInv_manual_id());
			cs.setString(4, invManualVO.getInvoice_reason());
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

	@Override
	public List<InvBuyerVO> getInvBuyerData(InvBuyerVO invBuyerVO) {
		List<InvBuyerVO> rows = new ArrayList<InvBuyerVO>();
		InvBuyerVO row = null;

		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try {
			pstmt = connection.prepareStatement(sp_select_inv_buyer_by_unicode_or_title);

			pstmt.setString(1, invBuyerVO.getGroup_id());
			pstmt.setString(2, invBuyerVO.getUnicode());
			pstmt.setString(3, invBuyerVO.getTitle());

			rs = pstmt.executeQuery();
			while (rs.next()) {
				row = new InvBuyerVO();
				row.setInv_buyer_id(rs.getString("inv_buyer_id"));
				row.setGroup_id(rs.getString("group_id"));
				row.setTitle(rs.getString("title"));
				row.setUnicode(rs.getString("unicode"));
				row.setAddress(rs.getString("address"));
				row.setMemo(rs.getString("memo"));
				row.setCreate_time(rs.getDate("create_time"));
				rows.add(row);
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
		return rows;
	}

}
