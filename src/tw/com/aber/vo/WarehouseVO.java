package tw.com.aber.vo;

import java.io.Serializable;

public class WarehouseVO implements Serializable{
	
	private static final long serialVersionUID = 1L;
	
	private String warehouse_id;
	private String group_id;
	private String warehouse_code;
	private String warehouse_name;
	private String warehouse_locate;
	private String sf_warehouse_code;
	
	
	public String getWarehouse_id() {
		return warehouse_id;
	}
	public void setWarehouse_id(String warehouse_id) {
		this.warehouse_id = warehouse_id;
	}
	public String getGroup_id() {
		return group_id;
	}
	public void setGroup_id(String group_id) {
		this.group_id = group_id;
	}
	public String getWarehouse_code() {
		return warehouse_code;
	}
	public void setWarehouse_code(String warehouse_code) {
		this.warehouse_code = warehouse_code;
	}
	public String getWarehouse_name() {
		return warehouse_name;
	}
	public void setWarehouse_name(String warehouse_name) {
		this.warehouse_name = warehouse_name;
	}
	public String getWarehouse_locate() {
		return warehouse_locate;
	}
	public void setWarehouse_locate(String warehouse_locate) {
		this.warehouse_locate = warehouse_locate;
	}
	public String getSf_warehouse_code() {
		return sf_warehouse_code;
	}
	public void setSf_warehouse_code(String sf_warehouse_code) {
		if (sf_warehouse_code == null){
			this.sf_warehouse_code = "";
		} else {
			this.sf_warehouse_code = sf_warehouse_code;
		}
	}

	
}
