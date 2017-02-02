package tw.com.aber.vo;

public class StockVO implements java.io.Serializable {

	private static final long serialVersionUID = 1L;

	private String stock_id;
	private String group_id;
	private String user_id;
	private String product_id;
	private int quantity;
	private String memo;
	private String product_name;
	private String keep_stock;

	public String getStock_id() {
		return stock_id;
	}

	public void setStock_id(String stock_id) {
		this.stock_id = stock_id;
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

	public int getQuantity() {
		return quantity;
	}

	public void setQuantity(int quantity) {
		this.quantity = quantity;
	}

	public String getMemo() {
		return memo;
	}

	public void setMemo(String memo) {
		this.memo = memo;
	}

	public String getProduct_name() {
		return product_name;
	}

	public void setProduct_name(String product_name) {
		this.product_name = product_name;
	}

	public String getKeep_stock() {
		return keep_stock;
	}

	public void setKeep_stock(String keep_stock) {
		this.keep_stock = keep_stock;
	}

}