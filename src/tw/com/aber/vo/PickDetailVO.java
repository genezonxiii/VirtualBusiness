package tw.com.aber.vo;

import java.io.Serializable;

public class PickDetailVO implements Serializable{

	private static final long serialVersionUID = 1L;
	private String pickDetail_id;
	private String pick_id;
	private String realsaleDetail_id;
	private String group_id;
	private String order_no;
	private String product_id;
	private String v_product_name;
	private String c_product_id;
	private String location_id;
	private String v_location_code;
	private Integer quantity;
	
	
	public String getPickDetail_id() {
		return pickDetail_id;
	}
	public void setPickDetail_id(String pickDetail_id) {
		this.pickDetail_id = pickDetail_id;
	}
	public String getPick_id() {
		return pick_id;
	}
	public void setPick_id(String pick_id) {
		this.pick_id = pick_id;
	}
	public String getRealsaleDetail_id() {
		return realsaleDetail_id;
	}
	public void setRealsaleDetail_id(String realsaleDetail_id) {
		this.realsaleDetail_id = realsaleDetail_id;
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
	public String getLocation_id() {
		return location_id;
	}
	public void setLocation_id(String location_id) {
		this.location_id = location_id;
	}
	public Integer getQuantity() {
		return quantity;
	}
	public void setQuantity(Integer quantity) {
		this.quantity = quantity;
	}
	public String getV_product_name() {
		return v_product_name;
	}
	public void setV_product_name(String v_product_name) {
		this.v_product_name = v_product_name;
	}
	public String getV_location_code() {
		return v_location_code;
	}
	public void setV_location_code(String v_location_code) {
		this.v_location_code = v_location_code;
	}

	
}
