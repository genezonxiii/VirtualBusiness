package tw.com.aber.vo;

import java.io.Serializable;

public class StockTakeDetailVO implements Serializable {
	private static final long serialVersionUID = 1L;
	
	private String stocktakeDetail_id;
	private String stocktake_id;
	private String group_id;
	private String product_id;
	private String c_product_id;
	private String product_name;
	private Integer quantity;
	private Integer stocktake_qty;
	private String location_id;
	private String memo;
	
	private String v_location_code;
	private String v_location_desc;
	private String v_warehouse_code;
	private String v_warehouse_name;
	
	public String getStocktakeDetail_id() {
		return stocktakeDetail_id;
	}
	public void setStocktakeDetail_id(String stocktakeDetail_id) {
		this.stocktakeDetail_id = stocktakeDetail_id;
	}
	public String getStocktake_id() {
		return stocktake_id;
	}
	public void setStocktake_id(String stocktake_id) {
		this.stocktake_id = stocktake_id;
	}
	public String getGroup_id() {
		return group_id;
	}
	public void setGroup_id(String group_id) {
		this.group_id = group_id;
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
	public Integer getStocktake_qty() {
		return stocktake_qty;
	}
	public void setStocktake_qty(Integer stocktake_qty) {
		this.stocktake_qty = stocktake_qty;
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
	public String getV_location_code() {
		return v_location_code;
	}
	public void setV_location_code(String v_location_code) {
		this.v_location_code = v_location_code;
	}
	public String getV_location_desc() {
		return v_location_desc;
	}
	public void setV_location_desc(String v_location_desc) {
		this.v_location_desc = v_location_desc;
	}
	public String getV_warehouse_code() {
		return v_warehouse_code;
	}
	public void setV_warehouse_code(String v_warehouse_code) {
		this.v_warehouse_code = v_warehouse_code;
	}
	public String getV_warehouse_name() {
		return v_warehouse_name;
	}
	public void setV_warehouse_name(String v_warehouse_name) {
		this.v_warehouse_name = v_warehouse_name;
	}

	
	

}
