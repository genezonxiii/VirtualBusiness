package tw.com.aber.vo;

import java.io.Serializable;
import java.util.List;

public class AcceptdetailVO implements Serializable {
	
	private static final long serialVersionUID = 1L;

	private String acceptDetail_id;
	private String accept_id;
	private String group_id;
	private String user_id;
	private String product_id;
	private String c_product_id;
	private String product_name;
	private Integer quantity;
	private Integer accept_qty;
	private String location_id;
	private String memo;	
	//from tb_warehouse
	private String v_warehouse_code;
	private String v_warehouse_name;
	//from tb_location
	private String v_location_code;
	//為了放同公司的 WarehouseVO 跟v_warehouse_code,v_warehouse_name有差異
	private List<WarehouseVO> warehouseVOList;
	//為了放同公司的 LocationVO 跟v_location_code,location_id有差異
	private List<LocationVO> locationVOList;
	
	
	public String getAcceptDetail_id() {
		return acceptDetail_id;
	}
	public void setAcceptDetail_id(String acceptDetail_id) {
		this.acceptDetail_id = acceptDetail_id;
	}
	public String getAccept_id() {
		return accept_id;
	}
	public void setAccept_id(String accept_id) {
		this.accept_id = accept_id;
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
	public Integer getAccept_qty() {
		return accept_qty;
	}
	public void setAccept_qty(Integer accept_qty) {
		this.accept_qty = accept_qty;
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
	public String getV_location_code() {
		return v_location_code;
	}
	public void setV_location_code(String v_location_code) {
		this.v_location_code = v_location_code;
	}
	public List<WarehouseVO> getWarehouseVOList() {
		return warehouseVOList;
	}
	public void setWarehouseVOList(List<WarehouseVO> warehouseVOList) {
		this.warehouseVOList = warehouseVOList;
	}
	public List<LocationVO> getLocationVOList() {
		return locationVOList;
	}
	public void setLocationVOList(List<LocationVO> locationVOList) {
		this.locationVOList = locationVOList;
	}

}
