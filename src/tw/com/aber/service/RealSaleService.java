package tw.com.aber.service;

import java.util.List;

import org.json.JSONObject;

import tw.com.aber.dao.RealSaleDao;
import tw.com.aber.vo.CustomerVO;
import tw.com.aber.vo.RealSaleDetailVO;
import tw.com.aber.vo.RealSaleVO;
import static org.junit.Assert.*;
import org.junit.Test;

public class RealSaleService {
	private RealSaleDao dao;

	public RealSaleService() {
		dao = new RealSaleDao();
	}

	public RealSaleVO addRealSale(RealSaleVO paramVO) {
		dao.insertDB(paramVO);
		return paramVO;
	}

	public RealSaleVO updateRealSale(RealSaleVO paramVO) {
		dao.updateDB(paramVO);
		return paramVO;
	}

	public List<RealSaleVO> getSaleSeqNo(String group_id) {
		return dao.getNewSaleSeqNo(group_id);
	}

	public void deleteRealSale(String realsale_id,String user_id,String group_id) {
		dao.deleteDB(realsale_id,user_id,group_id);
	}
	
	public List<RealSaleVO> getSearchAllDB(String group_id) {
		return dao.searchAllDB(group_id);
	}
	
	public List<CustomerVO> getSearchCustomerByName(String group_id, String name) {
		return dao.getCustomerByName(group_id, name);
	}

	public List<RealSaleDetailVO> getRealSaleDetail(RealSaleDetailVO realsaleDetailVO) {
		return dao.getRealSaleDetail(realsaleDetailVO);
	}
	
	public List<RealSaleVO> getSearchMuliDB(String group_id,String c_order_no_begin, String c_order_no_end,String c_customerid,String c_trans_list_date_begin,String c_trans_list_date_end,String c_dis_date_begin,String c_dis_date_end,String c_order_source,String c_deliveryway, String upload_date_begin, String upload_date_end) {
		return dao.searchMuliDB(group_id,c_order_no_begin,c_order_no_end,c_customerid,c_trans_list_date_begin,c_trans_list_date_end,c_dis_date_begin,c_dis_date_end,c_order_source,c_deliveryway, upload_date_begin, upload_date_end);
	}
	
	public JSONObject importRealSale(String group_id,String user_id,String trans_list_date_begin,String trans_list_date_end) {
		return dao.importDB(group_id,user_id,trans_list_date_begin,trans_list_date_end);
	}
	
	public JSONObject importAllocInv(String group_id,String user_id) {
		return dao.importAllocInvDB(group_id,user_id);
	}
	
	@Test
	public void testMethods() {
		
		RealSaleService realsaleService = new RealSaleService();
		
		List<RealSaleVO> realsaleVOs = realsaleService.getSearchAllDB("cbcc3138-5603-11e6-a532-000d3a800878");
		assertTrue(realsaleVOs!=null);
		
	}
}