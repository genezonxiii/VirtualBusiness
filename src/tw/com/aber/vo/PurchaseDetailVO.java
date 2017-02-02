package tw.com.aber.vo;

public class PurchaseDetailVO implements java.io.Serializable {

	private static final long serialVersionUID = 1L;
	private String purchaseDetail_id;
	private String purchase_id;
	private String group_id;
	private String user_id;
	private String product_id;
	private String c_product_id;
	private String product_name;
	private Integer quantity;
	private Float cost;
	private String memo;
	private java.sql.Date return_date;
	private Boolean isreturn;

	public String getPurchaseDetail_id() {
		return purchaseDetail_id;
	}

	public void setPurchaseDetail_id(String purchaseDetail_id) {
		this.purchaseDetail_id = purchaseDetail_id;
	}

	public String getPurchase_id() {
		return purchase_id;
	}

	public void setPurchase_id(String purchase_id) {
		this.purchase_id = purchase_id;
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

	public String getProduct_id() {
		return product_id;
	}

	public void setProduct_id(String product_id) {
		this.product_id = product_id;
	}

	public String getC_product_id() {
		return c_product_id;
	}

	public void setC_product_id(String c_product_id) {
		this.c_product_id = c_product_id;
	}

	public String getProduct_name() {
		return product_name;
	}

	public void setProduct_name(String product_name) {
		this.product_name = product_name;
	}

	public Integer getQuantity() {
		return quantity;
	}

	public void setQuantity(Integer quantity) {
		this.quantity = quantity;
	}

	public Float getCost() {
		return cost;
	}

	public void setCost(Float cost) {
		this.cost = cost;
	}

	public String getMemo() {
		return memo;
	}

	public void setMemo(String memo) {
		this.memo = memo;
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
