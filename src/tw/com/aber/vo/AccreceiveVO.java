package tw.com.aber.vo;

public class AccreceiveVO implements java.io.Serializable {

	private static final long serialVersionUID = 1L;

	private String receivable_id;
	private String product_id;
	private String seq_no;
	private Float amount;
	private java.sql.Date amount_date;
	private Float receive_amount;
	private java.sql.Date receive_date;
	private String user_id;
	private String memo;
	private String order_source;
	private String order_no;
	private String message;

	public String getReceivable_id() {
		return receivable_id;
	}

	public void setReceivable_id(String receivable_id) {
		this.receivable_id = receivable_id;
	}

	public String getProduct_id() {
		return product_id;
	}

	public void setProduct_id(String product_id) {
		this.product_id = product_id;
	}

	public String getSeq_no() {
		return seq_no;
	}

	public void setSeq_no(String seq_no) {
		this.seq_no = seq_no;
	}

	public Float getAmount() {
		return amount;
	}

	public void setAmount(Float amount) {
		this.amount = amount;
	}

	public java.sql.Date getAmount_date() {
		return amount_date;
	}

	public void setAmount_date(java.sql.Date amount_date) {
		this.amount_date = amount_date;
	}

	public Float getReceive_amount() {
		return receive_amount;
	}

	public void setReceive_amount(Float receive_amount) {
		this.receive_amount = receive_amount;
	}

	public java.sql.Date getReceive_date() {
		return receive_date;
	}

	public void setReceive_date(java.sql.Date receive_date) {
		this.receive_date = receive_date;
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

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public String getOrder_source() {
		return order_source;
	}

	public void setOrder_source(String order_source) {
		this.order_source = order_source;
	}

	public String getOrder_no() {
		return order_no;
	}

	public void setOrder_no(String order_no) {
		this.order_no = order_no;
	}

}