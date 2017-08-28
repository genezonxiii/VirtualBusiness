package tw.com.aber.vo;

import java.io.Serializable;
import java.sql.Date;

public class ShipSFDetailVO implements Serializable {

	private static final long serialVersionUID = 1L;
	private String v_ship_id;// tb_ship
	private String detail_id;
	private String group_id;
	private String order_no;
	private String waybill_no;
	private String shipment_id;
	private String actual_ship_time;
	private String status;
	private String sku_no;
	private String actual_qty;

	public String getV_ship_id() {
		return v_ship_id;
	}

	public void setV_ship_id(String v_ship_id) {
		this.v_ship_id = v_ship_id;
	}

	public String getDetail_id() {
		return detail_id;
	}

	public void setDetail_id(String detail_id) {
		this.detail_id = detail_id;
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

	public String getWaybill_no() {
		return waybill_no;
	}

	public void setWaybill_no(String waybill_no) {
		this.waybill_no = waybill_no;
	}

	public String getShipment_id() {
		return shipment_id;
	}

	public void setShipment_id(String shipment_id) {
		this.shipment_id = shipment_id;
	}

	public String getActual_ship_time() {
		return actual_ship_time;
	}

	public void setActual_ship_time(String actual_ship_time) {
		this.actual_ship_time = actual_ship_time;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getSku_no() {
		return sku_no;
	}

	public void setSku_no(String sku_no) {
		this.sku_no = sku_no;
	}

	public String getActual_qty() {
		return actual_qty;
	}

	public void setActual_qty(String actual_qty) {
		this.actual_qty = actual_qty;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

}
