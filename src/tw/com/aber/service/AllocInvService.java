package tw.com.aber.service;

import static org.junit.Assert.assertTrue;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import tw.com.aber.dao.AllocInvDao;
import tw.com.aber.vo.AllocInvVo;
import tw.com.aber.vo.PurchaseDetailVO;
import tw.com.aber.vo.PurchaseVO;

public class AllocInvService {
	private AllocInvDao dao;
	private ObjectMapper objectMapper;

	public AllocInvService() {
		dao = new AllocInvDao();
		objectMapper = new ObjectMapper();
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

	public List<AllocInvVo> getAllocInvByGroupAndOrderNo(String group_id, String order_no) {
		return dao.getAllocInvByGroupAndOrderNo(group_id, order_no);
	}

	public String deleteAllocInvByGroupAndOrderNo(String group_id, String order_no, String user_id) {
		return dao.deleteAllocInvByGroupAndOrderNo(group_id, order_no, user_id);
	}
	
	public String insertAllocInv(String json) throws JsonParseException, JsonMappingException, IOException {
		List<String> result = new ArrayList<String>();
		List<AllocInvVo> list = objectMapper.readValue(json, new TypeReference<List<AllocInvVo>>(){});
		for(int i = 0; i < list.size(); i++) {
			result.add( dao.insertAllocInv(list.get(i)) );
		}
		return result.toString();
	}
	
	@Test
	public void testMethods() {
		
		AllocInvService allocinvService = new AllocInvService();
		
		List<AllocInvVo> allocinvVOs = allocinvService.getAllData("cbcc3138-5603-11e6-a532-000d3a800878");
		assertTrue(allocinvVOs!=null);
		
	}
}