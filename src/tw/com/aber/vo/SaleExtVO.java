package tw.com.aber.vo;

import java.sql.Date;
import java.sql.Time;

public class SaleExtVO implements java.io.Serializable {

	private static final long serialVersionUID = 1L;
	private String sale_id;

	private Float totalAmt; 
	private String orderStatus;
	private String deliverName;
	private String deliverTo;
	private String deliverStore;
	private String deliverNote;
	private String deliverPhone;
	private String deliverMobile;
	private String payKind;
	private String payStatus;
	private String invName;
	private String invTo;
	private String email;
	
	public String toString(){
		return "Total Amt:" + totalAmt + 
				", Deliver Name:" + deliverName +
				", Deliver To:" + deliverTo +
				", Deliver Store:" + deliverStore +
				", Deliver Note:" + deliverNote +
				", Deliver Phone:" + deliverPhone +
				", Deliver Mobile:" + deliverMobile +
				", Pay Kind:" + payKind +
				", Pay Status:" + payStatus +
				", Invoice Name:" + invName +
				", Invoice To:" + invTo +
				", E-mail:" + email;
	}
	public String getSale_id() {
		return sale_id;
	}
	public void setSale_id(String sale_id) {
		this.sale_id = sale_id == null?"":sale_id;
	}
	public Float getTotalAmt() {
		return totalAmt;
	}
	public void setTotalAmt(Float totalAmt) {
		this.totalAmt = totalAmt == null?0:totalAmt;
	}
	public String getOrderStatus() {
		return orderStatus;
	}
	public void setOrderStatus(String orderStatus) {
		this.orderStatus = orderStatus;
	}
	public String getDeliverName() {
		return deliverName;
	}
	public void setDeliverName(String deliverName) {
		this.deliverName = deliverName == null?"":deliverName;
	}
	public String getDeliverTo() {
		return deliverTo;
	}
	public void setDeliverTo(String deliverTo) {
		this.deliverTo = deliverTo == null?"":deliverTo;
	}
	public String getDeliverStore() {
		return deliverStore;
	}
	public void setDeliverStore(String deliverStore) {
		this.deliverStore = deliverStore == null?"":deliverStore;
	}
	public String getDeliverNote() {
		return deliverNote;
	}
	public void setDeliverNote(String deliverNote) {
		this.deliverNote = deliverNote == null?"":deliverNote;
	}
	public String getDeliverPhone() {
		return deliverPhone;
	}
	public void setDeliverPhone(String deliverPhone) {
		this.deliverPhone = deliverPhone == null?"":deliverPhone;
	}
	public String getDeliverMobile() {
		return deliverMobile;
	}
	public void setDeliverMobile(String deliverMobile) {
		this.deliverMobile = deliverMobile == null?"":deliverMobile;
	}
	public String getPayKind() {
		return payKind;
	}
	public void setPayKind(String payKind) {
		this.payKind = payKind == null?"":payKind;
	}
	public String getPayStatus() {
		return payStatus;
	}
	public void setPayStatus(String payStatus) {
		this.payStatus = payStatus == null?"":payStatus;
	}
	public String getInvName() {
		return invName;
	}
	public void setInvName(String invName) {
		this.invName = invName == null?"":invName;
	}
	public String getInvTo() {
		return invTo;
	}
	public void setInvTo(String invTo) {
		this.invTo = invTo == null?"":invTo;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email == null?"":email;
	}
}
