package tw.com.aber.service;

import tw.com.aber.dao.LogisticDao;
import tw.com.aber.vo.LogisticVO;

public class LogisticService {
	private LogisticDao dao;

	public LogisticService() {
		dao = new LogisticDao();
	}
	
	public void logisticRecordSf(String groupId, String userId, String orderNo, String shipmentId) {
		LogisticVO logisticVO = new LogisticVO();
		logisticVO.setGroupId(groupId);
		logisticVO.setUserId(userId);
		logisticVO.setOrderNo(orderNo);
		logisticVO.setShippingCompany("sf");
		logisticVO.setReturnLabel(shipmentId);
		
		dao.logisticRecord(logisticVO);
	}
	
	public void logisticRecordEzcat(String groupId, String userId, String orderNo, String trackingNumber) {
		LogisticVO logisticVO = new LogisticVO();
		logisticVO.setGroupId(groupId);
		logisticVO.setUserId(userId);
		logisticVO.setOrderNo(orderNo);
		logisticVO.setShippingCompany("ezcat");
		logisticVO.setReturnLabel(trackingNumber);
		
		dao.logisticRecord(logisticVO);
	}
	
}
