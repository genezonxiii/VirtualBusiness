package tw.com.aber.vo;

import java.io.Serializable;

public class InvManualDetailVO implements Serializable {

	private static final long serialVersionUID = 1L;

	private String inv_manual_detail_id;
	private String inv_manual_id, group_id;
	private String description;
	private Integer price;
	private Integer quantity;
	private Integer subtotal;

	public String getInv_manual_detail_id() {
		return inv_manual_detail_id;
	}

	public void setInv_manual_detail_id(String inv_manual_detail_id) {
		this.inv_manual_detail_id = inv_manual_detail_id;
	}

	public String getInv_manual_id() {
		return inv_manual_id;
	}

	public void setInv_manual_id(String inv_manual_id) {
		this.inv_manual_id = inv_manual_id;
	}

	public String getGroup_id() {
		return group_id;
	}

	public void setGroup_id(String group_id) {
		this.group_id = group_id;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Integer getPrice() {
		return price;
	}

	public void setPrice(Integer price) {
		this.price = price;
	}

	public Integer getQuantity() {
		return quantity;
	}

	public void setQuantity(Integer quantity) {
		this.quantity = quantity;
	}

	public Integer getSubtotal() {
		return subtotal;
	}

	public void setSubtotal(Integer subtotal) {
		this.subtotal = subtotal;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

}
