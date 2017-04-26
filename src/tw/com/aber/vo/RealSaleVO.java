package tw.com.aber.vo;

import java.sql.Date;

public class RealSaleVO implements java.io.Serializable{
	
	private static final long serialVersionUID = 1L;
	private String realsale_id;
	private String seq_no;
	private String group_id;
	private String order_no;
	private String user_id;
	private String customer_id;
	private String name;
	private String invoice;
	private Date invoice_date;
	private Date trans_list_date;
	private Date dis_date;
	private String memo;
	private Date sale_date;
	private String order_source;
	private Date return_date;
	private Boolean isreturn;
	private String deliveryway;
	private Float total_amt;
	private String order_status;
	private String deliver_name;
	private String deliver_to;
	public String getRealsale_id() {
		return realsale_id;
	}
	public void setRealsale_id(String realsale_id) {
		this.realsale_id = realsale_id;
	}
	public String getSeq_no() {
		return seq_no;
	}
	public void setSeq_no(String seq_no) {
		this.seq_no = seq_no;
	}
	public String getGroup_id() {
		return group_id;
	}
	public void setGroup_id(String group_id) {
		this.group_id = group_id;
	}
	public String getOrder_no() {
		return order_no;
	}
	public void setOrder_no(String order_no) {
		this.order_no = order_no;
	}
	public String getUser_id() {
		return user_id;
	}
	public void setUser_id(String user_id) {
		this.user_id = user_id;
	}
	public String getCustomer_id() {
		return customer_id;
	}
	public void setCustomer_id(String customer_id) {
		this.customer_id = customer_id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getInvoice() {
		return invoice;
	}
	public void setInvoice(String invoice) {
		this.invoice = invoice;
	}
	public Date getInvoice_date() {
		return invoice_date;
	}
	public void setInvoice_date(Date invoice_date) {
		this.invoice_date = invoice_date;
	}
	public Date getTrans_list_date() {
		return trans_list_date;
	}
	public void setTrans_list_date(Date trans_list_date) {
		this.trans_list_date = trans_list_date;
	}
	public Date getDis_date() {
		return dis_date;
	}
	public void setDis_date(Date dis_date) {
		this.dis_date = dis_date;
	}
	public String getMemo() {
		return memo;
	}
	public void setMemo(String memo) {
		this.memo = memo;
	}
	public Date getSale_date() {
		return sale_date;
	}
	public void setSale_date(Date sale_date) {
		this.sale_date = sale_date;
	}
	public String getOrder_source() {
		return order_source;
	}
	public void setOrder_source(String order_source) {
		this.order_source = order_source;
	}
	public Date getReturn_date() {
		return return_date;
	}
	public void setReturn_date(Date return_date) {
		this.return_date = return_date;
	}
	public Boolean getIsreturn() {
		return isreturn;
	}
	public void setIsreturn(Boolean isreturn) {
		this.isreturn = isreturn;
	}
	public String getDeliveryway() {
		return deliveryway;
	}
	public void setDeliveryway(String deliveryway) {
		this.deliveryway = deliveryway;
	}
	public Float getTotal_amt() {
		return total_amt;
	}
	public void setTotal_amt(Float total_amt) {
		this.total_amt = total_amt;
	}
	public String getOrder_status() {
		return order_status;
	}
	public void setOrder_status(String order_status) {
		this.order_status = order_status;
	}
	public String getDeliver_name() {
		return deliver_name;
	}
	public void setDeliver_name(String deliver_name) {
		this.deliver_name = deliver_name;
	}
	public String getDeliver_to() {
		return deliver_to;
	}
	public void setDeliver_to(String deliver_to) {
		this.deliver_to = deliver_to;
	}

}
