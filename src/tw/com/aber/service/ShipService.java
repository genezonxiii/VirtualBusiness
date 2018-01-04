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

	public List<ShipVO> getSearchShipBySaleDate(String groupId, Date startDate, Date endDate) {
		return dao.searchShipBySaleDate(groupId, startDate, endDate);
	}

	public List<ShipVO> getSearchShipByOrderNo(ShipVO shipVO) {
		return dao.searchShipByOrderNo(shipVO);
	}
	
	public List<ShipVO> getSearchShipByWaybillNo(String groupId,String waybill) {
		return dao.getSearchShipByWaybillNo(groupId,waybill);
	}
	
	public List<ShipVO> getShipByShipSeqNo(String shipSeqNo, String groupId) {
		return dao.getShipByShipSeqNo(shipSeqNo, groupId);
	}

	public List<ShipVO> getShipByShipSeqNoGroupByOrderNo(String shipSeqNos, String groupId) {
		return dao.getShipByShipSeqNoGroupByOrderNo(shipSeqNos, groupId);
	}

	public String genSFDeliveryOrderService(String info, String groupId, String totalWeight, String seqNo) {
		return dao.genSFDeliveryOrderService(info, groupId, totalWeight, seqNo);
	}

	public String getShipSFDeliveryNewSeqNo(String groupId) {
		return dao.getShipSFDeliveryNewSeqNo(groupId);
	}

	public void insertToShipSFDelivery(DeliveryVO deliveryVO) {
		dao.insertToShipSFDelivery(deliveryVO);
	}

	public List<DeliveryVO> getShipSFDeliveryInfoByOrderNo(String groupId, String orderNos) {
		return dao.getShipSFDeliveryInfoByOrderNo(groupId, orderNos);
	}

	public List<ShipSFStatusVO> selectShipSfStatus(ShipSFStatusVO shipSFStatusVO) {
		return dao.selectShipSfStatus(shipSFStatusVO);
	}

	public List<ShipSFDetailVO> selectShipSfDetailStatus(ShipSFDetailVO shipSFDetailVO) {
		return dao.selectShipSfDetailStatus(shipSFDetailVO);
	}
	
	public List<ShipVO> getShipGroupByOrderNo(String groupId, Date startDate, Date endDate) {
		return dao.getShipGroupByOrderNo(groupId, startDate, endDate);
	}

	public List<ShipVO> getShipByShipSeqNoGroupByOrderNoNew(String orderNos, String groupId) {
		return dao.getShipByShipSeqNoGroupByOrderNoNew(orderNos, groupId);
	}

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