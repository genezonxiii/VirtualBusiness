package tw.com.aber.service;

import static org.junit.Assert.assertTrue;

import java.sql.Date;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.Test;

import tw.com.aber.dao.ShipDao;
import tw.com.aber.vo.DeliveryVO;
import tw.com.aber.vo.ShipSFDetailVO;
import tw.com.aber.vo.ShipSFStatusVO;
import tw.com.aber.vo.ShipVO;

public class ShipService {
	private static final Logger logger = LogManager.getLogger(PickService.class);
	private ShipDao dao;

	public ShipService() {
		dao = new ShipDao();
	}

	/**
	 * <p>依{@code group_id}和{@code sale_date}區間，撈取{@code tb_ship}
	 * 
	 * @param groupId
	 * @param startDate
	 * @param endDate
	 * @return
	 */
	public List<ShipVO> getSearchShipBySaleDate(String groupId, Date startDate, Date endDate) {
		return dao.searchShipBySaleDate(groupId, startDate, endDate);
	}

	/**
	 * <p>依{@code group_id}及{@code order_no}撈取{@code tb_ship}
	 * 
	 * @param shipVO
	 * @return
	 */
	public List<ShipVO> getSearchShipByOrderNo(ShipVO shipVO) {
		return dao.searchShipByOrderNo(shipVO);
	}
	
	/**
	 * <p>依{@code group_id}及{@code waybill_no}撈取出貨資訊
	 * 
	 * @param groupId
	 * @param waybill
	 * @return
	 */
	public List<ShipVO> getSearchShipByWaybillNo(String groupId,String waybill) {
		return dao.getSearchShipByWaybillNo(groupId,waybill);
	}
	
	/**
	 * <p>依{@code group_id}及{@code ship_seq_no}撈取{@code tb_ship}
	 * 
	 * @param shipSeqNo
	 * @param groupId
	 * @return
	 */
	public List<ShipVO> getShipByShipSeqNo(String shipSeqNo, String groupId) {
		return dao.getShipByShipSeqNo(shipSeqNo, groupId);
	}

	/**
	 * <p>依{@code group_id}及{@code ship_seq_no}撈取出貨相關資訊
	 * 
	 * @param shipSeqNos
	 * @param groupId
	 * @return
	 */
	public List<ShipVO> getShipByShipSeqNoGroupByOrderNo(String shipSeqNos, String groupId) {
		return dao.getShipByShipSeqNoGroupByOrderNo(shipSeqNos, groupId);
	}

	/**
	 * <p>依{@code group_id}及{@code order_no}撈取出貨及順豐快遞資訊，產生電文格式
	 * 
	 * @param info
	 * @param groupId
	 * @param totalWeight
	 * @param seqNo
	 * @return
	 */
	public String genSFDeliveryOrderService(String info, String groupId, String totalWeight, String seqNo) {
		return dao.genSFDeliveryOrderService(info, groupId, totalWeight, seqNo);
	}

	/**
	 * <p>依{@code group_id}撈取順豐快遞的最新流水號
	 * 
	 * @param groupId
	 * @return
	 */
	public String getShipSFDeliveryNewSeqNo(String groupId) {
		return dao.getShipSFDeliveryNewSeqNo(groupId);
	}

	/**
	 * <p>新增順豐快遞
	 * 
	 * @param deliveryVO
	 */
	public void insertToShipSFDelivery(DeliveryVO deliveryVO) {
		dao.insertToShipSFDelivery(deliveryVO);
	}

	/**
	 * <p>依{@code group_id}及{@code order_no}撈取順豐快遞資訊
	 * 
	 * @param groupId
	 * @param orderNos
	 * @return
	 */
	public List<DeliveryVO> getShipSFDeliveryInfoByOrderNo(String groupId, String orderNos) {
		return dao.getShipSFDeliveryInfoByOrderNo(groupId, orderNos);
	}

	/**
	 * <p>依{@code group_id}、{@code ship_id}及{@code order_no}撈取順豐回推出庫狀態
	 * 
	 * @param shipSFStatusVO
	 * @return
	 */
	public List<ShipSFStatusVO> selectShipSfStatus(ShipSFStatusVO shipSFStatusVO) {
		return dao.selectShipSfStatus(shipSFStatusVO);
	}

	/**
	 * <p>依{@code group_id}、{@code ship_id}及{@code order_no}撈取順豐回推出庫明細
	 * 
	 * @param shipSFDetailVO
	 * @return
	 */
	public List<ShipSFDetailVO> selectShipSfDetailStatus(ShipSFDetailVO shipSFDetailVO) {
		return dao.selectShipSfDetailStatus(shipSFDetailVO);
	}
	
	/**
	 * <p>依{@code group_id}及{@code sale_date}區間，撈取出貨相關資訊
	 * 
	 * @param groupId
	 * @param startDate
	 * @param endDate
	 * @return
	 */
	public List<ShipVO> getShipGroupByOrderNo(String groupId, Date startDate, Date endDate) {
		return dao.getShipGroupByOrderNo(groupId, startDate, endDate);
	}

	/**
	 * <p>依{@code group_id}及{@code ship_seq_no}撈取出貨相關資訊
	 * 
	 * @param orderNos
	 * @param groupId
	 * @return
	 */
	public List<ShipVO> getShipByShipSeqNoGroupByOrderNoNew(String orderNos, String groupId) {
		return dao.getShipByShipSeqNoGroupByOrderNoNew(orderNos, groupId);
	}

	/**
	 * <p>依{@code group_id}及{@code sale_date}區間，撈取黑貓出貨相關資訊
	 * 
	 * @param groupId
	 * @param startDate
	 * @param endDate
	 * @return
	 */
	public List<ShipVO> getEgsShipGroupByOrderNo(String groupId, Date startDate, Date endDate){
		return dao.getEgsShipGroupByOrderNo(groupId, startDate, endDate);
	}
	
	@Test
	public void testMethods() {
		
		ShipService shipService = new ShipService();
		try {
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
			Date fromTime = new Date(sdf.parse("2016-01-01").getTime());
			Date tillTime = new Date(sdf.parse("2017-12-31").getTime());
			List<ShipVO> pickVOs = shipService.getSearchShipBySaleDate("cbcc3138-5603-11e6-a532-000d3a800878",fromTime,tillTime );
			assertTrue(pickVOs!=null);
		} catch (ParseException e) {
			logger.error("jUnitTestErr",e);
		}
		
	}

}