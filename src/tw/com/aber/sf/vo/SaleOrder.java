package tw.com.aber.sf.vo;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

@XmlRootElement(name = "SaleOrders")
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(propOrder = { "warehouseCode", "sfOrderType", "erpOrderType", "erpOrder", "orderReceiverInfo", "shipmentId",
		"result", "note" })
public class SaleOrder {
	private static final long serialVersionUID = 1L;

	@XmlElement(name = "WarehouseCode")
	private String warehouseCode;
	@XmlElement(name = "SfOrderType")
	private String sfOrderType;
	@XmlElement(name = "ErpOrderType")
	private String erpOrderType;
	@XmlElement(name = "ErpOrder")
	private String erpOrder;
	@XmlElement(name = "OrderReceiverInfo")
	private OrderReceiverInfo orderReceiverInfo;

	@XmlElement(name = "ShipmentId")
	private String shipmentId;
	@XmlElement(name = "Result")
	private String result;
	@XmlElement(name = "Note")
	private String note;

	public String getShipmentId() {
		return shipmentId;
	}

	public void setShipmentId(String shipmentId) {
		this.shipmentId = shipmentId;
	}

	public String getResult() {
		return result;
	}

	public void setResult(String result) {
		this.result = result;
	}

	public String getNote() {
		return note;
	}

	public void setNote(String note) {
		this.note = note;
	}

	public String getWarehouseCode() {
		return warehouseCode;
	}

	public void setWarehouseCode(String warehouseCode) {
		this.warehouseCode = warehouseCode;
	}

	public String getSfOrderType() {
		return sfOrderType;
	}

	public void setSfOrderType(String sfOrderType) {
		this.sfOrderType = sfOrderType;
	}

	public String getErpOrderType() {
		return erpOrderType;
	}

	public void setErpOrderType(String erpOrderType) {
		this.erpOrderType = erpOrderType;
	}

	public String getErpOrder() {
		return erpOrder;
	}

	public void setErpOrder(String erpOrder) {
		this.erpOrder = erpOrder;
	}

	public OrderReceiverInfo getOrderReceiverInfo() {
		return orderReceiverInfo;
	}

	public void setOrderReceiverInfo(OrderReceiverInfo orderReceiverInfo) {
		this.orderReceiverInfo = orderReceiverInfo;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

}
