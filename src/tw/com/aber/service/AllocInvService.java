package tw.com.aber.service;

import static org.junit.Assert.assertTrue;

import java.util.List;

import org.junit.Test;

import tw.com.aber.dao.AllocInvDao;
import tw.com.aber.vo.AllocInvVo;
import tw.com.aber.vo.PurchaseDetailVO;
import tw.com.aber.vo.PurchaseVO;

public class AllocInvService {
	private AllocInvDao dao;

	public AllocInvService() {
		dao = new AllocInvDao();
	}

	public List<AllocInvVo> getAllData(String groupId) {
		return dao.getAllData(groupId);
	}

	public List<AllocInvVo> getGroupData(String groupId) {
		return dao.getGroupData(groupId);
	}

	public String getPurchaseSeqNo(String groupId) {
		return dao.getPurchaseSeqNo(groupId);
	}

	public String addPurchase(PurchaseVO purchaseVO) {
		return dao.doPurchase(purchaseVO);
	}
	public void delPurchase(String purchaseId) {
		dao.delPurchase(purchaseId);
	}
	public void addPurchaseDetail(List<PurchaseDetailVO> purchaseDetailVOs) {
		dao.doPurchaseDetail(purchaseDetailVOs);
	}
	
	@Test
	public void testMethods() {
		
		AllocInvService allocinvService = new AllocInvService();
		
		List<AllocInvVo> allocinvVOs = allocinvService.getAllData("cbcc3138-5603-11e6-a532-000d3a800878");
		assertTrue(allocinvVOs!=null);
		
	}
}