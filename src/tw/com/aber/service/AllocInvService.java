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

	/**
	 * <p>依{@code group_id}撈取{@code tb_alloc_inv}
	 * 
	 * @param groupId
	 * @return
	 */
	public List<AllocInvVo> getAllData(String groupId) {
		return dao.getAllData(groupId);
	}

	/**
	 * <p>依{@code group_id}撈取{@code tb_alloc_inv}，並依{@code product_id}彙總數量
	 * 
	 * @param groupId
	 * @return
	 */
	public List<AllocInvVo> getGroupData(String groupId) {
		return dao.getGroupData(groupId);
	}

	/**
	 * <p>取得最新流水號{@code seq_no}
	 * 
	 * @param groupId
	 * @return
	 */
	public String getPurchaseSeqNo(String groupId) {
		return dao.getPurchaseSeqNo(groupId);
	}

	/**
	 * <p>建立進貨單{@code tb_purchase}
	 * 
	 * @param purchaseVO
	 * @return
	 */
	public String addPurchase(PurchaseVO purchaseVO) {
		return dao.doPurchase(purchaseVO);
	}
	
	/**
	 * <p>刪除進貨單{@code tb_purchase}及其明細{@code tb_purchasedetail}
	 * 
	 * @param purchaseId
	 */
	public void delPurchase(String purchaseId) {
		dao.delPurchase(purchaseId);
	}
	
	/**
	 * <p>建立進貨明細{@code tb_purchasedetail}
	 * 
	 * @param purchaseDetailVOs
	 */
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