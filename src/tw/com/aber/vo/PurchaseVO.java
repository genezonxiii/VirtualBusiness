package tw.com.aber.vo;

public class PurchaseVO implements java.io.Serializable {

	private static final long serialVersionUID = 1L;
	private String purchase_id;
	private String seq_no;
	private String group_id;
	private String user_id;
	private String memo;
	private java.sql.Date purchase_date;
	private String invoice;
	private String invoice_type;
	private java.sql.Date return_date;
	private Boolean isreturn;
	private Float amount;
	private String supply_id;
	private String message;// 此參數用來存放錯誤訊息

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public String getSupply_id() {
		return supply_id;
	}

	public void setSupply_id(String supply_id) {
		this.supply_id = supply_id;
	}

	public Float getAmount() {
		return amount;
	}

	public void setAmount(Float amount) {
		this.amount = amount;
	}

	public String getPurchase_id() {
		return purchase_id;
	}

	public void setPurchase_id(String purchase_id) {
		this.purchase_id = purchase_id;
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

	public String getUser_id() {
		return user_id;
	}

	public void setUser_id(String user_id) {
		this.user_id = user_id;
	}

	public String getMemo() {
		return memo;
	}

	public void setMemo(String memo) {
		this.memo = memo;
	}

	public java.sql.Date getPurchase_date() {
		return purchase_date;
	}

	public void setPurchase_date(java.sql.Date purchase_date) {
		this.purchase_date = purchase_date;
	}

	public String getInvoice() {
		return invoice;
	}

	public void setInvoice(String invoice) {
		this.invoice = invoice;
	}

	public String getInvoice_type() {
		return invoice_type;
	}

	public void setInvoice_type(String invoice_type) {
		this.invoice_type = invoice_type;
	}

	public java.sql.Date getReturn_date() {
		return return_date;
	}

	public void setReturn_date(java.sql.Date return_date) {
		this.return_date = return_date;
	}

	public Boolean getIsreturn() {
		return isreturn;
	}

	public void setIsreturn(Boolean isreturn) {
		this.isreturn = isreturn;
	}
}
