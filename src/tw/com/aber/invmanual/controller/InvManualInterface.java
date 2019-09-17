package tw.com.aber.invmanual.controller;

import java.sql.Date;
import java.util.List;

import tw.com.aber.inv.vo.Invoice;
import tw.com.aber.vo.GroupVO;
import tw.com.aber.vo.InvBuyerVO;
import tw.com.aber.vo.InvManualDetailVO;
import tw.com.aber.vo.InvManualVO;
import tw.com.aber.vo.InvoiceTrackVO;

interface InvManualInterface {
	public List<InvManualVO> searchAllInvManual(String groupId);

	public List<InvManualDetailVO> searchInvManualDetailByInvManualId(InvManualDetailVO invManualDetailVO);

	public List<InvManualVO> searchInvManualByInvoiceDate(String groupId, String startDate, String endDate);

	public void insertInvManual(InvManualVO invManualVO);

	public void insertInvManualDetail(InvManualDetailVO invManualDetailVO);

	public void delInvManualDetail(InvManualDetailVO invManualDetailVO);

	public void delInvManual(InvManualVO invManualVO);

	public void updateInvManual(InvManualVO invManualVO);

	public void updateInvManualDetail(InvManualDetailVO invManualDetailVO);

	public InvoiceTrackVO getInvoiceTrack(String groupId, Date invoice_date);
	
	public Boolean increaseInvoiceTrack(InvoiceTrackVO invoiceTrackVO);

	public InvManualVO selectInvManualByInvManualId(String groupId, String inv_manual_id);

	public GroupVO getGroupInvoiceInfo(String groupId);

	public void updateInvManualInvFlag(InvManualVO invManualVO, InvoiceTrackVO invoiceTrackVO, Invoice invoice);
	public void updateInvManualInvFlagPershing(InvManualVO invManualVO);
	
	public void updateInvManualForCancelInvoice(InvManualVO invManualVO);

	public List<InvBuyerVO> getInvBuyerData(InvBuyerVO invBuyerVO);
}