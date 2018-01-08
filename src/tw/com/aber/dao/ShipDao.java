package tw.com.aber.dao;

import static org.junit.Assert.assertTrue;

import java.io.StringWriter;
import java.lang.reflect.Type;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.JAXB;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.Test;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import tw.com.aber.sf.delivery.vo.Body;
import tw.com.aber.sf.delivery.vo.Cargo;
import tw.com.aber.sf.delivery.vo.Order;
import tw.com.aber.sf.delivery.vo.Request;
import tw.com.aber.util.Database;
import tw.com.aber.vo.DeliveryVO;
import tw.com.aber.vo.RealSaleDetailVO;
import tw.com.aber.vo.ShipDetail;
import tw.com.aber.vo.ShipSFDeliveryVO;
import tw.com.aber.vo.ShipSFDetailVO;
import tw.com.aber.vo.ShipSFStatusVO;
import tw.com.aber.vo.ShipVO;

public class ShipDao {
	private static final Logger logger = LogManager.getLogger(ShipDao.class);

	private static final String sp_get_ship_sf_delivery_info_by_order_no = "call sp_get_ship_sf_delivery_info_by_order_no(?, ?)";
	private static final String sp_select_ship_by_sale_date = "call sp_select_ship_by_sale_date (?,?,?)";
	private static final String sp_select_ship_by_waybill_no = "call sp_select_ship_by_waybill_no (?,?)";
	private static final String sp_select_ship_by_order_no = "call sp_select_ship_by_order_no (?,?)";
	private static final String sp_get_ship_by_shipseqno = "call sp_get_ship_by_shipseqno(?,?)";
	private static final String sp_select_ship_delivery = "call sp_select_ship_delivery(?,?)";
	private static final String sp_get_ship_sf_delivery_new_no = "call sp_get_ship_sf_delivery_new_no(?,?)";
	private static final String sp_insert_ship_sf_delivery = "call sp_insert_ship_sf_delivery(?,?,?,?,?,?,?,?,?,?)";
	private static final String sp_get_ship_by_shipseqno_group_by_order_no = "call sp_get_ship_by_shipseqno_group_by_order_no (?,?)";
	private static final String sp_select_ship_sf_status = "call sp_select_ship_sf_status (?,?,?)";
	private static final String sp_select_ship_sf_detail_status = "call sp_select_ship_sf_detail_status (?,?,?)";
	private static final String sp_getShipGroupByOrderNo = "call sp_getShipGroupByOrderNo (?,?,?)";
	private static final String sp_getEgsShipGroupByOrderNo = "call sp_getEgsShipGroupByOrderNo (?,?,?)";
	private static final String sp_get_ship_by_orderno_group_by_order_no = "call sp_get_ship_by_orderno_group_by_order_no (?,?)";
	
	private Connection connection;

	public ShipDao() {
		connection = Database.getConnection();
	}

	public List<ShipVO> searchShipBySaleDate(String groupId, Date startDate, Date endDate) {
		List<ShipVO> rows = new ArrayList<ShipVO>();
		ShipVO row = null;

		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try {
			pstmt = connection.prepareStatement(sp_select_ship_by_sale_date);

			pstmt.setString(1, groupId);
			pstmt.setDate(2, startDate);
			pstmt.setDate(3, endDate);

			rs = pstmt.executeQuery();
			while (rs.next()) {
				row = new ShipVO();
				row.setShip_id(rs.getString("ship_id"));
				row.setShip_seq_no(rs.getString("ship_seq_no"));
				row.setGroup_id(rs.getString("group_id"));
				row.setOrder_no(rs.getString("order_no"));
				row.setUser_id(rs.getString("user_id"));
				row.setCustomer_id(rs.getString("customer_id"));
				row.setMemo(rs.getString("memo"));
				row.setDeliveryway(rs.getString("deliveryway"));
				row.setTotal_amt(rs.getFloat("total_amt"));
				row.setDeliver_name(rs.getString("deliver_name"));
				row.setDeliver_to(rs.getString("deliver_to"));
				row.setRealsale_id(rs.getString("realsale_id"));
				row.setV_sale_date(rs.getDate("sale_date"));
				row.setV_c_product_id(rs.getString("c_product_id"));
				row.setV_product_name(rs.getString("product_name"));

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
			}
		}
		return rows;
	}

	public List<ShipVO> getShipByShipSeqNo(String shipSeqNos, String groupId) {
		List<ShipVO> shipVOList = new ArrayList<ShipVO>();
		ShipVO shipVO = null;

		PreparedStatement pstmt = null;
		ResultSet rs = null;

		List<ShipDetail> shipDetailList = null;
		ShipDetail shipDetail = null;

		String ship_id_Record = null;
		String ship_id_now = "";
		String order_no_Record = null;
		String order_no_now = "";

		try {
			pstmt = connection.prepareStatement(sp_get_ship_by_shipseqno);

			logger.debug("getShipByShipSeqNo groupId:" + groupId + "shipSeqNos:" + shipSeqNos);
			pstmt.setString(1, groupId);
			pstmt.setString(2, shipSeqNos);

			rs = pstmt.executeQuery();
			while (rs.next()) {

				// sp 為ship sd 為shipDetail
				shipDetail = new ShipDetail();
				shipDetail.setC_product_id(rs.getString("sd_c_product_id"));
				shipDetail.setDeliveryway(rs.getString("sd_deliveryway"));
				shipDetail.setGroup_id(rs.getString("sd_group_id"));
				shipDetail.setMemo(rs.getString("sd_memo"));
				shipDetail.setPrice(rs.getString("sd_price"));
				shipDetail.setProduct_id(rs.getString("sd_product_id"));
				shipDetail.setProduct_name(rs.getString("sd_product_name"));

				String sd_quantity = rs.getString("sd_quantity");

				if (!(sd_quantity == null || "".equals(sd_quantity))) {
					shipDetail.setQuantity(Integer.parseInt(sd_quantity));
				}
				shipDetail.setShip_id(rs.getString("sd_ship_id"));
				shipDetail.setShipDetail_id(rs.getString("sd_shipDetail_id"));
				shipDetail.setUser_id(rs.getString("sd_user_id"));

				// 如果現在跑的ship_id跟紀錄的ship_id不相等 那代表已經換出貨單
				// 所以要新增出貨明細
				logger.debug("order_no_now:" + order_no_now);
				logger.debug("order_no_Record:" + order_no_Record);
				logger.debug("order_no_Record:" + shipDetail.getC_product_id());
				logger.debug("order_no_Record:" + shipDetail.getProduct_name());

				ship_id_now = rs.getString("sp_ship_id");
				order_no_now = rs.getString("sp_order_no");
				if ((!order_no_now.equals(order_no_Record)) || rs.isFirst()) {
					shipDetailList = new ArrayList<ShipDetail>();

					// 並且紀錄出貨明細
					shipVO = new ShipVO();
					shipVO.setShip_id(rs.getString("sp_ship_id"));

					shipVO.setShip_seq_no(rs.getString("sp_ship_seq_no"));
					shipVO.setGroup_id(rs.getString("sp_group_id"));
					shipVO.setOrder_no(rs.getString("sp_order_no"));
					shipVO.setUser_id(rs.getString("sp_user_id"));
					shipVO.setCustomer_id(rs.getString("sp_customer_id"));
					shipVO.setMemo(rs.getString("sp_memo"));
					shipVO.setDeliveryway(rs.getString("sp_deliveryway"));
					shipVO.setTotal_amt(rs.getFloat("sp_total_amt"));
					shipVO.setDeliver_name(rs.getString("sp_deliver_name"));
					shipVO.setDeliver_to(rs.getString("sp_deliver_to"));
					shipVO.setV_deliver_mobile(rs.getString("se_deliver_mobile"));
					shipVO.setV_deliver_name(rs.getString("se_deliver_name"));
					shipVO.setV_ext_deliver_note(rs.getString("se_deliver_note"));
					shipVO.setV_deliver_phone(rs.getString("se_deliver_phone"));
					shipVO.setV_pay_kind(rs.getString("se_pay_kind"));
					shipVO.setV_pay_status(rs.getString("se_pay_status"));
					shipVO.setV_total_amt(rs.getString("se_total_amt"));
					shipVO.setV_dis_date(rs.getDate("sm_dis_date"));
					shipVO.setShipDetail(shipDetailList);
					shipVOList.add(shipVO);

					ship_id_Record = ship_id_now;
					order_no_Record = order_no_now;
				}

				shipDetailList.add(shipDetail);
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
		return shipVOList;
	}

	public List<ShipVO> searchShipByOrderNo(ShipVO shipVO) {
		List<ShipVO> rows = new ArrayList<ShipVO>();
		ShipVO row = null;

		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try {
			pstmt = connection.prepareStatement(sp_select_ship_by_order_no);

			String groupId = shipVO.getGroup_id();
			String orderNo = shipVO.getOrder_no();

			pstmt.setString(1, groupId);
			pstmt.setString(2, orderNo);

			rs = pstmt.executeQuery();
			while (rs.next()) {
				row = new ShipVO();
				row.setShip_id(rs.getString("ship_id"));
				row.setShip_seq_no(rs.getString("ship_seq_no"));
				row.setGroup_id(rs.getString("group_id"));
				row.setOrder_no(rs.getString("order_no"));
				row.setUser_id(rs.getString("user_id"));
				row.setCustomer_id(rs.getString("customer_id"));
				row.setMemo(rs.getString("memo"));
				row.setDeliveryway(rs.getString("deliveryway"));
				row.setTotal_amt(rs.getFloat("total_amt"));
				row.setDeliver_name(rs.getString("deliver_name"));
				row.setDeliver_to(rs.getString("deliver_to"));
				row.setRealsale_id(rs.getString("realsale_id"));
				row.setV_sale_date(rs.getDate("sale_date"));
				row.setV_c_product_id(rs.getString("c_product_id"));
				row.setV_product_name(rs.getString("product_name"));

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
			}
		}
		return rows;
	}

	public String genSFDeliveryOrderService(String info, String groupId, String totalWeight, String seqNo) {

		Type type = new TypeToken<List<ShipSFDeliveryVO>>() {}.getType();
		List<ShipSFDeliveryVO> sfDeliveryVOs = new Gson().fromJson(info, type);

		List<RealSaleDetailVO> details = null;
		List<Cargo> cargos = new ArrayList<Cargo>();

		Order order = null;

		for (ShipSFDeliveryVO master : sfDeliveryVOs) {
			details = master.getRealSaleDetailVOs();

			for (RealSaleDetailVO detail : details) {
				Cargo cargo = new Cargo();
				cargo.setName(detail.getProduct_name());
				cargo.setCount(String.valueOf(detail.getQuantity()));
				cargos.add(cargo);
			}
		}
		// 因為只有同筆訂單的一或多筆出貨單，才能使用該功能，所以order資料統一取第一筆(至少一筆)
		ShipSFDeliveryVO vo = sfDeliveryVOs.get(0);
		String orderNo = vo.getOrder_no();
		PreparedStatement pstmt = null;
		ResultSet rs = null;

		String result = "";
		Body body = new Body();
		Request request = new Request();

		try {
			pstmt = connection.prepareStatement(sp_select_ship_delivery);
			logger.debug("groupId: ".concat(groupId));
			logger.debug("orderNo: ".concat(orderNo));
			pstmt.setString(1, groupId);
			pstmt.setString(2, orderNo);

			rs = pstmt.executeQuery();
			while (rs.next()) {
				order = new Order();
				order.setOrderid(orderNo.concat("-").concat(seqNo));
				order.setJ_company(rs.getString("j_company"));
				order.setJ_contact(rs.getString("j_contact"));
				order.setJ_tel(rs.getString("j_tel"));
				order.setJ_mobile(rs.getString("j_mobile"));
				order.setJ_address(rs.getString("j_address"));
				// TODO 到件方公司名稱先填寫為[個人]
				order.setD_company("個人");
				order.setD_contact(rs.getString("d_contact"));
				order.setD_tel(rs.getString("d_tel"));
				order.setD_mobile(rs.getString("d_mobile"));
				order.setD_address(rs.getString("d_address"));
				// TODO 付款方式先填寫為[1]
				order.setPay_method("1");
				// TODO 快件產品類別先填寫為[1]
				order.setExpress_type("1");
				// TODO 包裹數先填寫為[1]
				order.setParcel_quantity("1");
				order.setCargos(cargos);
				order.setCargo_total_weight(totalWeight);
				request.setHead(rs.getString("head"));
			}
			body.setOrder(order);

			request.setService("OrderService");
			request.setLang("zh-CN");
			request.setBody(body);

			String rqJsonStr = new Gson().toJson(request);
			logger.debug(rqJsonStr);

			StringWriter sw = new StringWriter();
			JAXB.marshal(request, sw);
			logger.debug("\n{}", sw.toString());
			result = sw.toString();
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

		return result;
	}

	public String getShipSFDeliveryNewSeqNo(String groupId) {
		CallableStatement cs = null;
		String seqNo = null;

		try {
			cs = connection.prepareCall(sp_get_ship_sf_delivery_new_no);

			cs.setString(1, groupId);
			cs.registerOutParameter(2, Types.CHAR);
			cs.execute();

			seqNo = cs.getString(2);
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
		return seqNo;
	}

	public void insertToShipSFDelivery(DeliveryVO deliveryVO) {
		CallableStatement cs = null;

		try {
			cs = connection.prepareCall(sp_insert_ship_sf_delivery);

			cs.setString(1, deliveryVO.getSeq_no());
			cs.setString(2, deliveryVO.getOrder_no());
			cs.setString(3, deliveryVO.getGroup_id());
			cs.setString(4, deliveryVO.getMailno());
			cs.setString(5, deliveryVO.getWeight());
			cs.setString(6, deliveryVO.getOrigincode());
			cs.setString(7, deliveryVO.getDestcode());
			cs.setString(8, deliveryVO.getSf_result());
			cs.setString(9, deliveryVO.getErr_code());
			cs.setString(10, deliveryVO.getRemark());

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

	public List<DeliveryVO> getShipSFDeliveryInfoByOrderNo(String groupId, String orderNos) {
		List<DeliveryVO> rows = new ArrayList<DeliveryVO>();
		DeliveryVO row = null;

		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try {
			pstmt = connection.prepareStatement(sp_get_ship_sf_delivery_info_by_order_no);

			pstmt.setString(1, groupId);
			pstmt.setString(2, orderNos);

			rs = pstmt.executeQuery();
			while (rs.next()) {
				row = new DeliveryVO();
				row.setMailno(rs.getString("mailno"));
				row.setSeq_no(rs.getString("seq_no"));
				row.setOrder_no(rs.getString("order_no"));

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
			}
		}
		return rows;
	}

	public List<ShipVO> getShipByShipSeqNoGroupByOrderNo(String shipSeqNos, String groupId) {

		List<ShipVO> shipVOList = new ArrayList<ShipVO>();
		ShipVO shipVO = null;

		PreparedStatement pstmt = null;
		ResultSet rs = null;

		List<ShipDetail> shipDetailList = null;
		ShipDetail shipDetail = null;

		String ship_id_Record = null;
		String ship_id_now = "";
		String order_no_Record = null;
		String order_no_now = "";

		try {
			pstmt = connection.prepareStatement(sp_get_ship_by_shipseqno_group_by_order_no);

			logger.debug("getShipByShipSeqNo groupId:" + groupId + "shipSeqNos:" + shipSeqNos);
			pstmt.setString(1, groupId);
			pstmt.setString(2, shipSeqNos);

			rs = pstmt.executeQuery();
			while (rs.next()) {

				// sp 為ship sd 為shipDetail
				shipDetail = new ShipDetail();
				shipDetail.setC_product_id(rs.getString("sd_c_product_id"));
				shipDetail.setDeliveryway(rs.getString("sd_deliveryway"));
				shipDetail.setGroup_id(rs.getString("sd_group_id"));
				shipDetail.setMemo(rs.getString("sd_memo"));
				shipDetail.setPrice(rs.getString("sd_price"));
				shipDetail.setProduct_id(rs.getString("sd_product_id"));
				shipDetail.setProduct_name(rs.getString("sd_product_name"));

				String sd_quantity = rs.getString("sd_quantity");

				if (!(sd_quantity == null || "".equals(sd_quantity))) {
					shipDetail.setQuantity(Integer.parseInt(sd_quantity));
				}
				shipDetail.setShip_id(rs.getString("sd_ship_id"));
				shipDetail.setShipDetail_id(rs.getString("sd_shipDetail_id"));
				shipDetail.setUser_id(rs.getString("sd_user_id"));

				// 如果現在跑的ship_id跟紀錄的ship_id不相等 那代表已經換出貨單
				// 所以要新增出貨明細
				logger.debug("order_no_now:" + order_no_now);
				logger.debug("order_no_Record:" + order_no_Record);
				logger.debug("order_no_Record:" + shipDetail.getC_product_id());
				logger.debug("order_no_Record:" + shipDetail.getProduct_name());

				ship_id_now = rs.getString("sp_ship_id");
				order_no_now = rs.getString("sp_order_no");
				if ((!order_no_now.equals(order_no_Record)) || rs.isFirst()) {
					shipDetailList = new ArrayList<ShipDetail>();

					// 並且紀錄出貨明細
					shipVO = new ShipVO();
					shipVO.setShip_id(rs.getString("sp_ship_id"));

					shipVO.setShip_seq_no(rs.getString("sp_ship_seq_no"));
					shipVO.setGroup_id(rs.getString("sp_group_id"));
					shipVO.setOrder_no(rs.getString("sp_order_no"));
					shipVO.setUser_id(rs.getString("sp_user_id"));
					shipVO.setCustomer_id(rs.getString("sp_customer_id"));
					shipVO.setMemo(rs.getString("sp_memo"));
					shipVO.setDeliveryway(rs.getString("sp_deliveryway"));
					shipVO.setTotal_amt(rs.getFloat("sp_total_amt"));
					shipVO.setDeliver_name(rs.getString("sp_deliver_name"));
					shipVO.setDeliver_to(rs.getString("sp_deliver_to"));
					shipVO.setV_deliver_mobile(rs.getString("se_deliver_mobile"));
					shipVO.setV_deliver_name(rs.getString("se_deliver_name"));
					shipVO.setV_ext_deliver_note(rs.getString("se_deliver_note"));
					shipVO.setV_deliver_phone(rs.getString("se_deliver_phone"));
					shipVO.setV_pay_kind(rs.getString("se_pay_kind"));
					shipVO.setV_pay_status(rs.getString("se_pay_status"));
					shipVO.setV_total_amt(rs.getString("se_total_amt"));
					shipVO.setV_dis_date(rs.getDate("sm_dis_date"));
					shipVO.setShipDetail(shipDetailList);
					shipVOList.add(shipVO);

					ship_id_Record = ship_id_now;
					order_no_Record = order_no_now;
				}

				shipDetailList.add(shipDetail);
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
		return shipVOList;
	}

	public List<ShipVO> getShipByShipSeqNoGroupByOrderNoNew(String shipSeqNos, String groupId) {

		List<ShipVO> shipVOList = new ArrayList<ShipVO>();
		ShipVO shipVO = null;

		PreparedStatement pstmt = null;
		ResultSet rs = null;

		List<ShipDetail> shipDetailList = null;
		ShipDetail shipDetail = null;

		String ship_id_Record = null;
		String ship_id_now = "";
		String order_no_Record = null;
		String order_no_now = "";

		try {
			pstmt = connection.prepareStatement(sp_get_ship_by_orderno_group_by_order_no);

			logger.debug("getShipByShipSeqNo groupId:" + groupId + "shipSeqNos:" + shipSeqNos);
			pstmt.setString(1, groupId);
			pstmt.setString(2, shipSeqNos);

			rs = pstmt.executeQuery();
			while (rs.next()) {

				// sp 為ship sd 為shipDetail
				shipDetail = new ShipDetail();
				shipDetail.setC_product_id(rs.getString("sd_c_product_id"));
				shipDetail.setDeliveryway(rs.getString("sd_deliveryway"));
				shipDetail.setGroup_id(rs.getString("sd_group_id"));
				shipDetail.setMemo(rs.getString("sd_memo"));
				shipDetail.setPrice(rs.getString("sd_price"));
				shipDetail.setProduct_id(rs.getString("sd_product_id"));
				shipDetail.setProduct_name(rs.getString("sd_product_name"));

				String sd_quantity = rs.getString("sd_quantity");

				if (!(sd_quantity == null || "".equals(sd_quantity))) {
					shipDetail.setQuantity(Integer.parseInt(sd_quantity));
				}
				shipDetail.setShip_id(rs.getString("sd_ship_id"));
				shipDetail.setShipDetail_id(rs.getString("sd_shipDetail_id"));
				shipDetail.setUser_id(rs.getString("sd_user_id"));

				// 如果現在跑的ship_id跟紀錄的ship_id不相等 那代表已經換出貨單
				// 所以要新增出貨明細
				logger.debug("order_no_now:" + order_no_now);
				logger.debug("order_no_Record:" + order_no_Record);
				logger.debug("order_no_Record:" + shipDetail.getC_product_id());
				logger.debug("order_no_Record:" + shipDetail.getProduct_name());

				ship_id_now = rs.getString("sp_ship_id");
				order_no_now = rs.getString("sp_order_no");
				if ((!order_no_now.equals(order_no_Record)) || rs.isFirst()) {
					shipDetailList = new ArrayList<ShipDetail>();

					// 並且紀錄出貨明細
					shipVO = new ShipVO();
					shipVO.setShip_id(rs.getString("sp_ship_id"));

					shipVO.setShip_seq_no(rs.getString("sp_ship_seq_no"));
					shipVO.setGroup_id(rs.getString("sp_group_id"));
					shipVO.setOrder_no(rs.getString("sp_order_no"));
					shipVO.setUser_id(rs.getString("sp_user_id"));
					shipVO.setCustomer_id(rs.getString("sp_customer_id"));
					shipVO.setMemo(rs.getString("sp_memo"));
					shipVO.setDeliveryway(rs.getString("sp_deliveryway"));
					shipVO.setTotal_amt(rs.getFloat("sp_total_amt"));
					shipVO.setDeliver_name(rs.getString("sp_deliver_name"));
					shipVO.setDeliver_to(rs.getString("sp_deliver_to"));
					shipVO.setV_deliver_mobile(rs.getString("se_deliver_mobile"));
					shipVO.setV_deliver_name(rs.getString("se_deliver_name"));
					shipVO.setV_ext_deliver_note(rs.getString("se_deliver_note"));
					shipVO.setV_deliver_phone(rs.getString("se_deliver_phone"));
					shipVO.setV_pay_kind(rs.getString("se_pay_kind"));
					shipVO.setV_pay_status(rs.getString("se_pay_status"));
					shipVO.setV_total_amt(rs.getString("se_total_amt"));
					shipVO.setV_dis_date(rs.getDate("sm_dis_date"));
					shipVO.setShipDetail(shipDetailList);
					shipVOList.add(shipVO);

					ship_id_Record = ship_id_now;
					order_no_Record = order_no_now;
				}

				shipDetailList.add(shipDetail);
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
		return shipVOList;
	}

	public List<ShipSFStatusVO> selectShipSfStatus(ShipSFStatusVO shipSFStatusVO) {

		List<ShipSFStatusVO> rows = new ArrayList<ShipSFStatusVO>();
		ShipSFStatusVO row = null;

		PreparedStatement pstmt = null;
		ResultSet rs = null;

		try {
			pstmt = connection.prepareStatement(sp_select_ship_sf_status);

			pstmt.setString(1, shipSFStatusVO.getGroup_id());
			pstmt.setString(2, shipSFStatusVO.getV_ship_id());
			pstmt.setString(3, shipSFStatusVO.getOrder_no());

			rs = pstmt.executeQuery();
			while (rs.next()) {
				row = new ShipSFStatusVO();
				row.setOrder_no(rs.getString("order_no"));
				row.setWaybill_no(rs.getString("waybill_no"));
				row.setShipment_id(rs.getString("shipment_id"));
				row.setEvent_time(rs.getString("event_time"));
				row.setStatus(rs.getString("status"));
				row.setNote(rs.getString("note"));

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
			}
		}
		return rows;
	}

	public List<ShipSFDetailVO> selectShipSfDetailStatus(ShipSFDetailVO shipSFDetailVO) {
		List<ShipSFDetailVO> rows = new ArrayList<ShipSFDetailVO>();
		ShipSFDetailVO row = null;

		PreparedStatement pstmt = null;
		ResultSet rs = null;

		try {
			pstmt = connection.prepareStatement(sp_select_ship_sf_detail_status);

			pstmt.setString(1, shipSFDetailVO.getGroup_id());
			pstmt.setString(2, shipSFDetailVO.getV_ship_id());
			pstmt.setString(3, shipSFDetailVO.getOrder_no());

			rs = pstmt.executeQuery();
			while (rs.next()) {
				row = new ShipSFDetailVO();
				row.setDetail_id(rs.getString("detail_id"));
				row.setGroup_id(rs.getString("group_id"));
				row.setOrder_no(rs.getString("order_no"));
				row.setWaybill_no(rs.getString("waybill_no"));
				row.setShipment_id(rs.getString("shipment_id"));
				row.setActual_ship_time(rs.getString("actual_ship_time"));
				row.setStatus(rs.getString("status"));
				row.setSku_no(rs.getString("sku_no"));
				row.setActual_qty(rs.getString("actual_qty"));

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
			}
		}
		return rows;
	}

	public List<ShipVO> getSearchShipByWaybillNo(String groupId, String waybill) {
		List<ShipVO> rows = new ArrayList<ShipVO>();
		ShipVO row = null;

		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try {
			pstmt = connection.prepareStatement(sp_select_ship_by_waybill_no);

			pstmt.setString(1, groupId);
			pstmt.setString(2, waybill);

			rs = pstmt.executeQuery();
			while (rs.next()) {
				row = new ShipVO();
				row.setShip_id(rs.getString("ship_id"));
				row.setShip_seq_no(rs.getString("ship_seq_no"));
				row.setGroup_id(rs.getString("group_id"));
				row.setOrder_no(rs.getString("order_no"));
				row.setUser_id(rs.getString("user_id"));
				row.setCustomer_id(rs.getString("customer_id"));
				row.setMemo(rs.getString("memo"));
				row.setDeliveryway(rs.getString("deliveryway"));
				row.setTotal_amt(rs.getFloat("total_amt"));
				row.setDeliver_name(rs.getString("deliver_name"));
				row.setDeliver_to(rs.getString("deliver_to"));
				row.setRealsale_id(rs.getString("realsale_id"));
				row.setV_sale_date(rs.getDate("sale_date"));
				row.setV_c_product_id(rs.getString("c_product_id"));
				row.setV_product_name(rs.getString("product_name"));

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
			}
		}
		return rows;
	}

	public List<ShipVO> getShipGroupByOrderNo(String groupId, Date startDate, Date endDate) {
		List<ShipVO> rows = new ArrayList<ShipVO>();
		
		ShipVO row = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try {
			pstmt = connection.prepareStatement(sp_getShipGroupByOrderNo);

			pstmt.setString(1, groupId);
			pstmt.setDate(2, startDate);
			pstmt.setDate(3, endDate);

			rs = pstmt.executeQuery();
			while (rs.next()) {
				row = new ShipVO();
				row.setGroup_id(rs.getString("group_id"));
				row.setOrder_no(rs.getString("order_no"));
				row.setTotal_amt(rs.getFloat("total_amt"));
				row.setDeliver_name(rs.getString("deliver_name"));
				row.setDeliver_to(rs.getString("deliver_to"));
				row.setV_pay_kind(rs.getString("pay_kind"));
				row.setV_pay_status(rs.getString("pay_status"));
				row.setV_sale_date(rs.getDate("sale_date"));
				row.setV_trans_list_date(rs.getDate("trans_list_date"));
				row.setV_sf_whflag(rs.getString("sf_whflag"));
				row.setV_shipment_id(rs.getString("shipment_id"));
				row.setV_ezcat_flag(rs.getString("ezcat_flag"));
				row.setV_tracking_number(rs.getString("tracking_number"));

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
			}
		}
		return rows;
	}

	public List<ShipVO> getEgsShipGroupByOrderNo(String groupId, Date startDate, Date endDate) {
		List<ShipVO> rows = new ArrayList<ShipVO>();
		
		ShipVO row = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try {
			pstmt = connection.prepareStatement(sp_getEgsShipGroupByOrderNo);

			pstmt.setString(1, groupId);
			pstmt.setDate(2, startDate);
			pstmt.setDate(3, endDate);

			rs = pstmt.executeQuery();
			while (rs.next()) {
				row = new ShipVO();
				row.setGroup_id(rs.getString("group_id"));
				row.setOrder_no(rs.getString("order_no"));
				row.setDeliver_name(rs.getString("deliver_name"));
				row.setDeliver_to(rs.getString("deliver_to"));
				row.setV_waybill_type(rs.getString("waybill_type"));
				row.setV_tracking_number(rs.getString("tracking_number"));
				row.setV_package_size(rs.getString("package_size"));
				row.setV_temperature(rs.getString("temperature"));
				row.setV_pay_kind(rs.getString("pay_kind"));
				row.setV_pay_status(rs.getString("pay_status"));
				row.setV_sale_date(rs.getDate("sale_date"));
				
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
			}
		}
		return rows;
	}

	@Test
	public void testMethods() {
		
		ShipDao shipDao = new ShipDao();
		try {
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
			Date fromTime = new Date(sdf.parse("2016-01-01").getTime());
			Date tillTime = new Date(sdf.parse("2017-12-31").getTime());
			List<ShipVO> shipVOs = shipDao.searchShipBySaleDate("cbcc3138-5603-11e6-a532-000d3a800878",fromTime,tillTime );
			assertTrue(shipVOs!=null);
		} catch (ParseException e) {
			logger.error("jUnitTestErr",e);
		}
		
	}
}