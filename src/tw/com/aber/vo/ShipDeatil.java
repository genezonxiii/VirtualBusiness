package tw.com.aber.vo;

import java.io.Serializable;

public class ShipDeatil implements Serializable {

	private static final long serialVersionUID = 1L;

	private String shipDetail_id;
	private String ship_id;
	private String group_id;
	private String user_id;
	private String product_id;
	private String product_name;
	private String c_product_id;
	private String deliveryway;
	private Integer quantity;
	private String price;
	private String memo;

	public String getShipDetail_id() {
		return shipDetail_id;
	}

	public void setShipDetail_id(String shipDetail_id) {
		this.shipDetail_id = shipDetail_id;
	}

	public String getShip_id() {
		return ship_id;
	}

	public void setShip_id(String ship_id) {
		this.ship_id = ship_id;
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

	public String getDeliveryway() {
		return deliveryway;
	}

	public void setDeliveryway(String deliveryway) {
		this.deliveryway = deliveryway;
	}

	public Integer getQuantity() {
		return quantity;
	}

	public void setQuantity(Integer quantity) {
		this.quantity = quantity;
	}

	public String getPrice() {
		return price;
	}

	public void setPrice(String price) {
		this.price = price;
	}

	public String getMemo() {
		return memo;
	}

	public void setMemo(String memo) {
		this.memo = memo;
	}

}
