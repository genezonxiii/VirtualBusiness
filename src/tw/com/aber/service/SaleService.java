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

	public SaleVO addSale(SaleVO paramVO) {
		dao.insertDB(paramVO);
		return paramVO;
	}

	public SaleVO updateSale(SaleVO paramVO) {
		dao.updateDB(paramVO);
		return paramVO;
	}

	public List<SaleVO> getSaleSeqNo(String group_id) {
		return dao.getNewSaleSeqNo(group_id);
	}

	public void deleteSale(String sale_id, String user_id) {
		dao.deleteDB(sale_id, user_id);
	}

	public List<SaleVO> getSearchDB(String group_id, String c_product_id) {
		return dao.searchDB(group_id, c_product_id);
	}

	public List<SaleVO> getSearchAllDB(String group_id) {
		return dao.searchAllDB(group_id);
	}

	public List<SaleVO> getSearchTransListDateDB(String group_id, String trans_list_start_date,
			String trans_list_end_date) {
		return dao.searchTransListDateDB(group_id, trans_list_start_date, trans_list_end_date);
	}

	public List<SaleVO> searchUploadDateDB(String group_id, String startDate, String endDate) {
		return dao.searchUploadDateDB(group_id, startDate, endDate);
	}

	public List<SaleVO> getSearchDisDateDB(String group_id, String dis_start_date, String dis_end_date) {
		return dao.searchDisDateDB(group_id, dis_start_date, dis_end_date);
	}

	public List<ProductVO> getSearchProductById(String group_id, String c_product_id) {
		return dao.getProductById(group_id, c_product_id);
	}

	public List<ProductVO> getSearchProductByName(String group_id, String product_name) {
		return dao.getProductByName(group_id, product_name);
	}

	public List<SaleDetailVO> getSaleDetail(SaleDetailVO saleDetailVO) {
		return dao.getSaleDetail(saleDetailVO);
	}

	public void addSaleDetail(SaleDetailVO paramVO) {
		dao.insertDetailDB(paramVO);
	}

	public void updateSaleDetail(SaleDetailVO paramVO) {
		dao.updateDetailDB(paramVO);
	}

	public void deleteSaleDetail(String saleDetail_id) {
		dao.deleteDetailDB(saleDetail_id);
	}

	public List<SaleVO> getSaleOrdernoInfoByIds(String groupId, String saleIds) {
		return dao.getSaleOrdernoInfoByIds(groupId, saleIds);
	}

	public GroupVO getGroupInvoiceInfo(String groupId) {
		return dao.getGroupInvoiceInfo(groupId);
	}

	public List<InvoiceTrackVO> getInvoiceTrack(String group_id, Date invoice_num_date,
			List<List<SaleVO>> groupBySaleVOsList) {
		List<InvoiceTrackVO> invoiceTrackVOList = new ArrayList<InvoiceTrackVO>();

		for (int i = 0; i < groupBySaleVOsList.size(); i++) {
			InvoiceTrackVO invoiceTrackVO = dao.getInvoiceTrack(group_id, invoice_num_date);

			invoiceTrackVOList.add(invoiceTrackVO);
		}

		return invoiceTrackVOList;
	}

	public void updateSaleInvoiceVcodeAndInvoice_time(List<SaleVO> SaleVOs, String InvoiceVcode, String Invoice_time) {
		dao.updateSaleInvoiceVcodeAndInvoice_time(SaleVOs, InvoiceVcode, Invoice_time);

	}

	public void updateSaleInvoice(List<SaleVO> SaleVOs, InvoiceTrackVO invoiceTrackVO, Date invoice_num_date) {
		dao.updateSaleInvoice(SaleVOs, invoiceTrackVO, invoice_num_date);

	}

	public void invoiceCancel(String group_id, String sale_ids) {
		dao.invoiceCancel(group_id, sale_ids);
	}

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

	public List<SaleVO> maskOverviewByExt(String group_id, List<SaleVO> SaleVOs) {
		String order_no = null;
		for (SaleVO saleVO : SaleVOs) {
			order_no = saleVO.getOrder_no();
		}

		List<SaleVO> afterMaskVOs = new ArrayList<SaleVO>();
		afterMaskVOs.add(dao.getSaleInvoiceInfoByOrderNo(group_id, order_no));

		return afterMaskVOs;
	}

	public List<SaleVO> getSaleByUploadDate(String group_id, String upload_date_start, String upload_date_end) {
		return dao.getSaleByUploadDate(group_id, upload_date_start, upload_date_end);
	}

	public List<SaleVO> getSaleByTransDate(String group_id, String trans_date_start, String trans_date_end) {
		return dao.getSaleByTransDate(group_id, trans_date_start, trans_date_end);
	}

	public List<SaleVO> getSaleOrdernoInfoByOrdernos(String groupId, String order_nos) {
		return dao.getSaleOrdernoInfoByOrdernos(groupId, order_nos);
	}

	public void updateTurnFlag(SaleVO saleVO){
		dao.updateTurnFlag(saleVO);
	}
	
}
