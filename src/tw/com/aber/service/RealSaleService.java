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

	/**
	 * <p>新增{@code tb_realsale}
	 * 
	 * @param paramVO
	 * @return
	 */
	public RealSaleVO addRealSale(RealSaleVO paramVO) {
		dao.insertDB(paramVO);
		return paramVO;
	}

	/**
	 * <p>修改{@code tb_realsale}
	 * 
	 * @param paramVO
	 * @return
	 */
	public RealSaleVO updateRealSale(RealSaleVO paramVO) {
		dao.updateDB(paramVO);
		return paramVO;
	}

	/**
	 * <p>取得最新流水號
	 * 
	 * @param group_id
	 * @return
	 */
	public List<RealSaleVO> getSaleSeqNo(String group_id) {
		return dao.getNewSaleSeqNo(group_id);
	}

	/**
	 * <p>刪除{@code tb_realsale}
	 * 
	 * @param realsale_id
	 * @param user_id
	 * @param group_id
	 */
	public void deleteRealSale(String realsale_id,String user_id,String group_id) {
		dao.deleteDB(realsale_id,user_id,group_id);
	}
	
	/**
	 * <p>依{@code group_id}撈取{@code tb_realsale}
	 * 
	 * @param group_id
	 * @return
	 */
	public List<RealSaleVO> getSearchAllDB(String group_id) {
		return dao.searchAllDB(group_id);
	}
	
	/**
	 * <p>依{@code name}撈取{@code tb_customer}
	 * @param group_id
	 * @param name
	 * @return
	 */
	public List<CustomerVO> getSearchCustomerByName(String group_id, String name) {
		return dao.getCustomerByName(group_id, name);
	}

	/**
	 * <p>以{@code realsale_id}撈取{@code tb_realsale}
	 * 
	 * @param realsaleDetailVO
	 * @return
	 */
	public List<RealSaleDetailVO> getRealSaleDetail(RealSaleDetailVO realsaleDetailVO) {
		return dao.getRealSaleDetail(realsaleDetailVO);
	}
	
	/**
	 * <p>以多種組合方式撈取{@code tb_realsale}
	 * 
	 * @param group_id
	 * @param c_order_no_begin
	 * @param c_order_no_end
	 * @param c_customerid
	 * @param c_trans_list_date_begin
	 * @param c_trans_list_date_end
	 * @param c_dis_date_begin
	 * @param c_dis_date_end
	 * @param c_order_source
	 * @param c_deliveryway
	 * @param upload_date_begin
	 * @param upload_date_end
	 * @return
	 */
	public List<RealSaleVO> getSearchMuliDB(String group_id,String c_order_no_begin, String c_order_no_end,String c_customerid,String c_trans_list_date_begin,String c_trans_list_date_end,String c_dis_date_begin,String c_dis_date_end,String c_order_source,String c_deliveryway, String upload_date_begin, String upload_date_end) {
		return dao.searchMuliDB(group_id,c_order_no_begin,c_order_no_end,c_customerid,c_trans_list_date_begin,c_trans_list_date_end,c_dis_date_begin,c_dis_date_end,c_order_source,c_deliveryway, upload_date_begin, upload_date_end);
	}
	
	/**
	 * <p>依{@code trans_list_date}區間，將訂單轉入銷單
	 * 
	 * @param group_id
	 * @param user_id
	 * @param trans_list_date_begin
	 * @param trans_list_date_end
	 * @return
	 */
	public JSONObject importRealSale(String group_id,String user_id,String trans_list_date_begin,String trans_list_date_end) {
		return dao.importDB(group_id,user_id,trans_list_date_begin,trans_list_date_end);
	}
	
	/**
	 * <p>銷單轉入配庫，{@code tb_realsale.order_status}等於{@code A2}
	 * 
	 * @param group_id
	 * @param user_id
	 * @return
	 */
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