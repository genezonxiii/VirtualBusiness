package tw.com.aber.vo;

public class AccpayVO implements java.io.Serializable {

	private static final long serialVersionUID = 1L;

	private String pay_id;
	private String group_id;
	private String seq_no;
	private Float amount;
	private java.sql.Date amount_date;
	private Float pay_amount;
	private java.sql.Date pay_date;
	private String user_id;
	private String memo;
	private String supply_name;
	private String message;

	public String getPay_id() {
		return pay_id;
	}

	public void setPay_id(String pay_id) {
		this.pay_id = pay_id;
	}

	public String getGroup_id() {
		return group_id;
	}

	public void setGroup_id(String group_id) {
		this.group_id = group_id;
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

	public Float getPay_amount() {
		return pay_amount;
	}

	public void setPay_amount(Float pay_amount) {
		this.pay_amount = pay_amount;
	}

	public java.sql.Date getPay_date() {
		return pay_date;
	}

	public void setPay_date(java.sql.Date pay_date) {
		this.pay_date = pay_date;
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

	public String getSupply_name() {
		return supply_name;
	}

	public void setSupply_name(String supply_name) {
		this.supply_name = supply_name;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}
}
