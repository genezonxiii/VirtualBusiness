package tw.com.aber.vo;

import java.io.Serializable;
import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;

public class AllocInvVo implements Serializable {

	private static final long serialVersionUID = 1L;
	private String alloc_id;
	private String realsaleDetail_id;
	private String group_id;
	private String order_no;
	private String product_id;
	private String c_product_id;
	private Integer quantity;
	private Float price;
	private String location_id;
	private Integer alloc_qty;
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone="GMT+8")
	private Date alloc_time;
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone="GMT+8")
	private Date checkin_time;
	// tb_product
	private String product_name;
	private String supply_id;
	private String supply_name;

	public String getAlloc_id() {
		return alloc_id;
	}

	public void setAlloc_id(String alloc_id) {
		this.alloc_id = alloc_id;
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

	public String getLocation_id() {
		return location_id;
	}

	public void setLocation_id(String location_id) {
		this.location_id = location_id;
	}

	public Integer getAlloc_qty() {
		return alloc_qty;
	}

	public void setAlloc_qty(Integer alloc_qty) {
		this.alloc_qty = alloc_qty;
	}

	public Date getAlloc_time() {
		return alloc_time;
	}

	public void setAlloc_time(Date alloc_time) {
		this.alloc_time = alloc_time;
	}

	public Date getCheckin_time() {
		return checkin_time;
	}

	public void setCheckin_time(Date checkin_time) {
		this.checkin_time = checkin_time;
	}

	public String getProduct_name() {
		return product_name;
	}

	public void setProduct_name(String product_name) {
		this.product_name = product_name;
	}

	public String getSupply_id() {
		return supply_id;
	}

	public void setSupply_id(String supply_id) {
		this.supply_id = supply_id;
	}

	public String getSupply_name() {
		return supply_name;
	}

	public void setSupply_name(String supply_name) {
		this.supply_name = supply_name;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

}
