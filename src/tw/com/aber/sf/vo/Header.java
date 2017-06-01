package tw.com.aber.sf.vo;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

@XmlRootElement(name = "Header")
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(propOrder = { "warehouseCode", "erpOrder", "shipmentId", "receiptId", "erpOrderType", "closeDate", "status",
		"skuNo", "inventoryStatus", "totalQty", "onHandQty", "availableQty", "inTransitQty" })
public class Header {
	private static final long serialVersionUID = 1L;
	@XmlElement(name = "WarehouseCode")
	private String warehouseCode;
	@XmlElement(name = "ErpOrder")
	private String erpOrder;
	@XmlElement(name = "ReceiptId")
	private String receiptId;
	@XmlElement(name = "ErpOrderType")
	private String erpOrderType;
	@XmlElement(name = "CloseDate")
	private String closeDate;
	@XmlElement(name = "Status")
	private String status;
	@XmlElement(name = "ShipmentId")
	private String shipmentId;

	@XmlElement(name = "SkuNo")
	private String skuNo;
	@XmlElement(name = "InventoryStatus")
	private String inventoryStatus;
	@XmlElement(name = "TotalQty")
	private String totalQty;
	@XmlElement(name = "OnHandQty")
	private String onHandQty;
	@XmlElement(name = "AvailableQty")
	private String availableQty;
	@XmlElement(name = "InTransitQty")
	private String inTransitQty;

	public String getSkuNo() {
		return skuNo;
	}

	public void setSkuNo(String skuNo) {
		this.skuNo = skuNo;
	}

	public String getInventoryStatus() {
		return inventoryStatus;
	}

	public void setInventoryStatus(String inventoryStatus) {
		this.inventoryStatus = inventoryStatus;
	}

	public String getTotalQty() {
		return totalQty;
	}

	public void setTotalQty(String totalQty) {
		this.totalQty = totalQty;
	}

	public String getOnHandQty() {
		return onHandQty;
	}

	public void setOnHandQty(String onHandQty) {
		this.onHandQty = onHandQty;
	}

	public String getAvailableQty() {
		return availableQty;
	}

	public void setAvailableQty(String availableQty) {
		this.availableQty = availableQty;
	}

	public String getInTransitQty() {
		return inTransitQty;
	}

	public void setInTransitQty(String inTransitQty) {
		this.inTransitQty = inTransitQty;
	}

	public String getShipmentId() {
		return shipmentId;
	}

	public void setShipmentId(String shipmentId) {
		this.shipmentId = shipmentId;
	}

	public String getWarehouseCode() {
		return warehouseCode;
	}

	public void setWarehouseCode(String warehouseCode) {
		this.warehouseCode = warehouseCode;
	}

	public String getErpOrder() {
		return erpOrder;
	}

	public void setErpOrder(String erpOrder) {
		this.erpOrder = erpOrder;
	}

	public String getReceiptId() {
		return receiptId;
	}

	public void setReceiptId(String receiptId) {
		this.receiptId = receiptId;
	}

	public String getErpOrderType() {
		return erpOrderType;
	}

	public void setErpOrderType(String erpOrderType) {
		this.erpOrderType = erpOrderType;
	}

	public String getCloseDate() {
		return closeDate;
	}

	public void setCloseDate(String closeDate) {
		this.closeDate = closeDate;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

}
