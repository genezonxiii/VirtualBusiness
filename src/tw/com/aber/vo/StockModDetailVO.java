package tw.com.aber.vo;

import java.io.Serializable;

public class StockModDetailVO implements Serializable {

	private static final long serialVersionUID = 1L;

	private String stockmodDetail_id;
	private String stockmod_id;
	private String product_id;
	private String quantity;
	private String location_id;
	private String memo;

	private String group_id;
	private String location_code;
	private String location_desc;
	private String product_name;

	
	public String getProduct_name() {
		return product_name;
	}

	public void setProduct_name(String product_name) {
		this.product_name = product_name;
	}

	public String getGroup_id() {
		return group_id;
	}

	public void setGroup_id(String group_id) {
		this.group_id = group_id;
	}

	public String getStockmodDetail_id() {
		return stockmodDetail_id;
	}

	public void setStockmodDetail_id(String stockmodDetail_id) {
		this.stockmodDetail_id = stockmodDetail_id;
	}

	public String getStockmod_id() {
		return stockmod_id;
	}

	public void setStockmod_id(String stockmod_id) {
		this.stockmod_id = stockmod_id;
	}

	public String getProduct_id() {
		return product_id;
	}

	public void setProduct_id(String product_id) {
		this.product_id = product_id;
	}

	public String getQuantity() {
		return quantity;
	}

	public void setQuantity(String quantity) {
		this.quantity = quantity;
	}

	public String getLocation_id() {
		return location_id;
	}

	public void setLocation_id(String location_id) {
		this.location_id = location_id;
	}

	public String getMemo() {
		return memo;
	}

	public void setMemo(String memo) {
		this.memo = memo;
	}

	public String getLocation_code() {
		return location_code;
	}

	public void setLocation_code(String location_code) {
		this.location_code = location_code;
	}

	public String getLocation_desc() {
		return location_desc;
	}

	public void setLocation_desc(String location_desc) {
		this.location_desc = location_desc;
	}

}
