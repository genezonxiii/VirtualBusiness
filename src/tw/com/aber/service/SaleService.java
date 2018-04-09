package tw.com.aber.service;

import java.sql.Date;
import java.util.ArrayList;
import java.util.List;

import tw.com.aber.dao.SaleDao;
import tw.com.aber.vo.GroupVO;
import tw.com.aber.vo.InvoiceTrackVO;
import tw.com.aber.vo.ProductVO;
import tw.com.aber.vo.SaleDetailVO;
import tw.com.aber.vo.SaleVO;

public class SaleService {
	private SaleDao dao;

	public SaleService() {
		dao = new SaleDao();
	}

	/**
	 * <p>新增{@code tb_sale}
	 * 
	 * @param paramVO
	 * @return
	 */
	public SaleVO addSale(SaleVO paramVO) {
		dao.insertDB(paramVO);
		return paramVO;
	}

	/**
	 * <p>修改{@code tb_sale}
	 * 
	 * @param paramVO
	 * @return
	 */
	public SaleVO updateSale(SaleVO paramVO) {
		dao.updateDB(paramVO);
		return paramVO;
	}

	/**
	 * <p>以{@code group_id}，撈取最新流水號{@code tb_sale.seq_no}
	 * 
	 * @param group_id
	 * @return
	 */
	public List<SaleVO> getSaleSeqNo(String group_id) {
		return dao.getNewSaleSeqNo(group_id);
	}

	/**
	 * <p>刪除{@code tb_sale}
	 * 
	 * @param sale_id
	 * @param user_id
	 */
	public void deleteSale(String sale_id, String user_id) {
		dao.deleteDB(sale_id, user_id);
	}

	/**
	 * <p>以{@code c_product_id}撈取{@code tb_sale}
	 * 
	 * @param group_id
	 * @param c_product_id
	 * @return
	 */
	public List<SaleVO> getSearchDB(String group_id, String c_product_id) {
		return dao.searchDB(group_id, c_product_id);
	}

	/**
	 * <p>以{@code group_id}撈取{@code tb_sale}
	 * 
	 * @param group_id
	 * @return
	 */
	public List<SaleVO> getSearchAllDB(String group_id) {
		return dao.searchAllDB(group_id);
	}

	/**
	 * <p>以{@code group_id}、{@code trans_list_date}區間，撈取{@code tb_sale}
	 * 
	 * @param group_id
	 * @param trans_list_start_date
	 * @param trans_list_end_date
	 * @return
	 */
	public List<SaleVO> getSearchTransListDateDB(String group_id, String trans_list_start_date,
			String trans_list_end_date) {
		return dao.searchTransListDateDB(group_id, trans_list_start_date, trans_list_end_date);
	}

	/**
	 * <p>以{@code group_id}、{@code upload_date}區間，撈取{@code tb_sale}
	 * 
	 * @param group_id
	 * @param startDate
	 * @param endDate
	 * @return
	 */
	public List<SaleVO> searchUploadDateDB(String group_id, String startDate, String endDate) {
		return dao.searchUploadDateDB(group_id, startDate, endDate);
	}

	/**
	 * <p>以{@code group_id}、{@code dis_date}區間，撈取{@code tb_sale}
	 * 
	 * @param group_id
	 * @param dis_start_date
	 * @param dis_end_date
	 * @return
	 */
	public List<SaleVO> getSearchDisDateDB(String group_id, String dis_start_date, String dis_end_date) {
		return dao.searchDisDateDB(group_id, dis_start_date, dis_end_date);
	}

	/**
	 * <p>以{@code group_id}、{@code c_product_id}，撈取{@code tb_sale}
	 * 
	 * @param group_id
	 * @param c_product_id
	 * @return
	 */
	public List<ProductVO> getSearchProductById(String group_id, String c_product_id) {
		return dao.getProductById(group_id, c_product_id);
	}

	/**
	 * <p>以{@code group_id}、{@code product_name}，撈取{@code tb_sale}
	 * 
	 * @param group_id
	 * @param product_name
	 * @return
	 */
	public List<ProductVO> getSearchProductByName(String group_id, String product_name) {
		return dao.getProductByName(group_id, product_name);
	}

	/**
	 * <p>以{@code sale_id}，撈取{@code tb_saleDetail}
	 * 
	 * @param saleDetailVO
	 * @return
	 */
	public List<SaleDetailVO> getSaleDetail(SaleDetailVO saleDetailVO) {
		return dao.getSaleDetail(saleDetailVO);
	}

	/**
	 * <p>新增{@code tb_saleDetail}
	 * 
	 * @param paramVO
	 */
	public void addSaleDetail(SaleDetailVO paramVO) {
		dao.insertDetailDB(paramVO);
	}

	/**
	 * <p>修改{@code tb_saleDetail}
	 * 
	 * @param paramVO
	 */
	public void updateSaleDetail(SaleDetailVO paramVO) {
		dao.updateDetailDB(paramVO);
	}

	/**
	 * <p>刪除{@code tb_saleDetail}
	 * 
	 * @param saleDetail_id
	 */
	public void deleteSaleDetail(String saleDetail_id) {
		dao.deleteDetailDB(saleDetail_id);
	}

	/**
	 * <p>以{@code group_id}、{@code sale_id}，撈取{@code tb_sale}
	 * 
	 * @param groupId
	 * @param saleIds
	 * @return
	 */
	public List<SaleVO> getSaleOrdernoInfoByIds(String groupId, String saleIds) {
		return dao.getSaleOrdernoInfoByIds(groupId, saleIds);
	}

	/**
	 * <p>以{@code group_id}，撈取{@code tb_group}
	 * 
	 * @param groupId
	 * @return
	 */
	public GroupVO getGroupInvoiceInfo(String groupId) {
		return dao.getGroupInvoiceInfo(groupId);
	}

	/**
	 * <p>以{@code group_id}、{@code invoice_num_date}，撈取{@code tb_invoice_track}。
	 * 取得目前發票資訊
	 * 
	 * @param group_id
	 * @param invoice_num_date
	 * @param groupBySaleVOsList
	 * @return
	 */
	public List<InvoiceTrackVO> getInvoiceTrack(String group_id, Date invoice_num_date,
			List<List<SaleVO>> groupBySaleVOsList) {
		List<InvoiceTrackVO> invoiceTrackVOList = new ArrayList<InvoiceTrackVO>();
		InvoiceTrackVO invoiceTrackVO = dao.getInvoiceTrack(group_id, invoice_num_date);
		invoiceTrackVOList.add(invoiceTrackVO);
		return invoiceTrackVOList;
	}
	
	/**
	 * <p>累加發票字軌
	 * 
	 * @param invoiceTrackVO
	 * @return
	 */
	public Boolean increaseInvoiceTrack(InvoiceTrackVO invoiceTrackVO) {
		return dao.increaseInvoiceTrack(invoiceTrackVO);
	}

	/**
	 * <p>發票開立成功，將發票驗證碼及開立時間，填入{@code tb_sale}
	 * 
	 * @param SaleVOs
	 * @param InvoiceVcode
	 * @param Invoice_time
	 */
	public void updateSaleInvoiceVcodeAndInvoice_time(List<SaleVO> SaleVOs, String InvoiceVcode, String Invoice_time) {
		dao.updateSaleInvoiceVcodeAndInvoice_time(SaleVOs, InvoiceVcode, Invoice_time);

	}

	/**
	 * <p>發票開立成功，把發票資訊寫入{@code tb_sale}、{@code tb_invoice_use}
	 * 
	 * @param SaleVOs
	 * @param invoiceTrackVO
	 * @param invoice_num_date
	 * @param invoice_time
	 * @param invoice_vcode
	 */
	public void updateSaleInvoice(List<SaleVO> SaleVOs, InvoiceTrackVO invoiceTrackVO, Date invoice_num_date,
			String invoice_time, String invoice_vcode) {
		dao.updateSaleInvoice(SaleVOs, invoiceTrackVO, invoice_num_date, invoice_time, invoice_vcode);
	}

	/**
	 * <p>作廢發票成功，把發票資訊寫入{@code tb_sale}、{@code tb_invoice_use}。
	 * 
	 * @param group_id
	 * @param sale_ids
	 * @param invoice_reason
	 */
	public void invoiceCancel(String group_id, String sale_ids, String invoice_reason) {
		dao.invoiceCancel(group_id, sale_ids, invoice_reason);
	}

	/**
	 * <p>檢查訂單資料
	 * 
	 * @param saleVOs
	 * @return
	 */
	public String checkCancelDate(List<SaleVO> saleVOs) {
		String errorMsg = "";
		if (saleVOs.size() < 1) {
			errorMsg = "很抱歉，查無資料";
		}

		for (int i = 0; i < saleVOs.size(); i++) {
			String invoice = saleVOs.get(i).getInvoice();
			if (null == invoice) {
				errorMsg = "很抱歉，單號" + saleVOs.get(i).getSeq_no() + " 並無開立發票";
			}
		}

		return errorMsg;
	}

	/**
	 * <p>開立發票資訊列表。
	 * 
	 * @param group_id
	 * @param SaleVOs
	 * @return
	 */
	public List<SaleVO> maskOverviewByExt(String group_id, List<SaleVO> SaleVOs) {
		String order_no = null;
		for (SaleVO saleVO : SaleVOs) {
			order_no = saleVO.getOrder_no();
		}

		List<SaleVO> afterMaskVOs = new ArrayList<SaleVO>();
		afterMaskVOs.add(dao.getSaleInvoiceInfoByOrderNo(group_id, order_no));

		return afterMaskVOs;
	}

	/**
	 * <p>以{@code upload_date}區間，撈取{@code tb_sale}
	 * 
	 * @param group_id
	 * @param upload_date_start
	 * @param upload_date_end
	 * @return
	 */
	public List<SaleVO> getSaleByUploadDate(String group_id, String upload_date_start, String upload_date_end) {
		return dao.getSaleByUploadDate(group_id, upload_date_start, upload_date_end);
	}

	/**
	 * <p>以{@code trans_list_date}區間，撈取{@code tb_sale}
	 * 
	 * @param group_id
	 * @param trans_date_start
	 * @param trans_date_end
	 * @return
	 */
	public List<SaleVO> getSaleByTransDate(String group_id, String trans_date_start, String trans_date_end) {
		return dao.getSaleByTransDate(group_id, trans_date_start, trans_date_end);
	}

	/**
	 * <p>以{@code order_no}，撈取{@code tb_sale}
	 * 
	 * @param groupId
	 * @param order_nos
	 * @return
	 */
	public List<SaleVO> getSaleOrdernoInfoByOrdernos(String groupId, String order_nos) {
		return dao.getSaleOrdernoInfoByOrdernos(groupId, order_nos);
	}

	/**
	 * <p>更新訂單是否可轉銷貨註記
	 * @param saleVO
	 * @return
	 */
	public int updateTurnFlag(SaleVO saleVO){
		return dao.updateTurnFlag(saleVO);
	}
	
}
