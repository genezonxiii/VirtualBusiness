package tw.com.aber.invmanual.controller;

import java.sql.Date;
import java.util.List;

import tw.com.aber.inv.vo.Invoice;
import tw.com.aber.vo.GroupVO;
import tw.com.aber.vo.InvBuyerVO;
import tw.com.aber.vo.InvManualDetailVO;
import tw.com.aber.vo.InvManualVO;
import tw.com.aber.vo.InvoiceTrackVO;

public class InvManualService {
	private InvManualInterface dao;

	public InvManualService() {
		dao = new InvManualDao();
	}

	public List<InvManualDetailVO> searchInvManualDetailByInvManualId(InvManualDetailVO invManualDetailVO) {
		return dao.searchInvManualDetailByInvManualId(invManualDetailVO);
	}

	public List<InvManualVO> searchAllInvManual(String groupId) {
		return dao.searchAllInvManual(groupId);
	}

	public List<InvManualVO> searchInvManualByInvoiceDate(String groupId, String startDate, String endDate) {
		return dao.searchInvManualByInvoiceDate(groupId, startDate, endDate);
	}

	public void insertInvManual(InvManualVO invManualVO) {
		dao.insertInvManual(invManualVO);
	}

	public void insertInvManualDetail(InvManualDetailVO invManualDetailVO) {
		dao.insertInvManualDetail(invManualDetailVO);
	}

	public void delInvManualDetail(InvManualDetailVO invManualDetailVO) {
		dao.delInvManualDetail(invManualDetailVO);
	}

	public void delInvManual(InvManualVO invManualVO) {
		dao.delInvManual(invManualVO);
	}

	public void updateInvManual(InvManualVO invManualVO) {
		dao.updateInvManual(invManualVO);
	}

	public void updateInvManualDetail(InvManualDetailVO invManualDetailVO) {
		dao.updateInvManualDetail(invManualDetailVO);
	}

	public InvManualVO selectInvManualByInvManualId(String groupId, String inv_manual_id) {
		return dao.selectInvManualByInvManualId(groupId, inv_manual_id);
	}

	public GroupVO getGroupInvoiceInfo(String groupId) {
		return dao.getGroupInvoiceInfo(groupId);
	}

	public InvoiceTrackVO getInvoiceTrack(String groupId, Date invoice_date) {
		return dao.getInvoiceTrack(groupId, invoice_date);
	}
	
	public Boolean increaseInvoiceTrack(InvoiceTrackVO invoiceTrackVO) {
		return dao.increaseInvoiceTrack(invoiceTrackVO);
	}

	public void updateInvManualInvFlag(InvManualVO invManualVO, InvoiceTrackVO invoiceTrackVO, Invoice invoice) {
		dao.updateInvManualInvFlag(invManualVO, invoiceTrackVO, invoice);
	}
	public void updateInvManualInvFlagPershing(InvManualVO invManualVO) {
		dao.updateInvManualInvFlagPershing(invManualVO);
	}
	
	public void updateInvManualForCancelInvoice(InvManualVO invManualVO) {
		dao.updateInvManualForCancelInvoice(invManualVO);
	}

	public List<InvBuyerVO> getInvBuyerData(InvBuyerVO invBuyerVO) {
		return dao.getInvBuyerData(invBuyerVO);
	}
}