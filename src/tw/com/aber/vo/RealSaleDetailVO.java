package tw.com.aber.vo;

import java.io.Serializable;

public class RealSaleDetailVO implements Serializable {

	private static final long serialVersionUID = 1L;
	
	private String realsaleDetail_id;
	private String realsale_id;
	private String group_id;
	private String order_no;
	private String user_id;
	private String product_id;
	private String product_name;
	private String c_product_id;
	private Integer quantity;
	private Float price;
	private String memo;
	public String getRealsaleDetail_id() {
		return realsaleDetail_id;
	}
	public void setRealsaleDetail_id(String realsaleDetail_id) {
		this.realsaleDetail_id = realsaleDetail_id;
	}
	public String getRealsale_id() {
		return realsale_id;
	}
	public void setRealsale_id(String realsale_id) {
		this.realsale_id = realsale_id;
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
	public String getProduct_id() {
		return product_id;
	}
	public void setProduct_id(String product_id) {
		this.product_id = product_id;
	}
	public String getProduct_name() {
		return product_name;
	}
	public void setProduct_name(String product_name) {
		this.product_name = product_name;
	}
	public String getC_product_id() {
		return c_product_id;
	}
	public void setC_product_id(String c_product_id) {
		this.c_product_id = c_product_id;
	}
	public Integer getQuantity() {
		return quantity;
	}
	public void setQuantity(Integer quantity) {
		this.quantity = quantity;
	}
	public Float getPrice() {
		return price;
	}
	public void setPrice(Float price) {
		this.price = price;
	}
	public String getMemo() {
		return memo;
	}
	public void setMemo(String memo) {
		this.memo = memo;
	}
	public static long getSerialversionuid() {
		return serialVersionUID;
	}
}
