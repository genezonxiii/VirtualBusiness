package tw.com.aber.vo;

public class LocationVO {
	
	private String location_id;
	private String group_id;
	private String warehouse_id;
	private String location_code;
	private String location_desc;
	private String location_memo;
	
	private String v_warehouse_code;
	private String v_warehouse_name;
	private String v_warehouse_locate;
	private String v_sf_warehouse_code;
	private WarehouseVO warehouseVO;
	
	public String getLocation_id() {
		return location_id;
	}
	public void setLocation_id(String location_id) {
		this.location_id = location_id;
	}
	public String getGroup_id() {
		return group_id;
	}
	public void setGroup_id(String group_id) {
		this.group_id = group_id;
	}
	public String getWarehouse_id() {
		return warehouse_id;
	}
	public void setWarehouse_id(String warehouse_id) {
		this.warehouse_id = warehouse_id;
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
	public String getLocation_memo() {
		return location_memo;
	}
	public void setLocation_memo(String location_memo) {
		this.location_memo = location_memo;
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
	public String getV_warehouse_locate() {
		return v_warehouse_locate;
	}
	public void setV_warehouse_locate(String v_warehouse_locate) {
		this.v_warehouse_locate = v_warehouse_locate;
	}
	public String getV_sf_warehouse_code() {
		return v_sf_warehouse_code;
	}
	public void setV_sf_warehouse_code(String v_sf_warehouse_code) {
		this.v_sf_warehouse_code = v_sf_warehouse_code;
	}
	public WarehouseVO getWarehouseVO() {
		return warehouseVO;
	}
	public void setWarehouseVO(WarehouseVO warehouseVO) {
		this.warehouseVO = warehouseVO;
	}
	


}
