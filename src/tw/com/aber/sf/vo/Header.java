package tw.com.aber.sf.vo;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

@XmlRootElement(name = "Header")
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(propOrder = { "warehouseCode", "erpOrder", "shipmentId", "receiptId", 
		"erpOrderType", "closeDate", "status", "skuNo", "inventoryStatus", 
		"lot", "expirationDate", 
		"totalQty", "onHandQty", "availableQty", "inTransitQty",
		"carrier", "carrierProduct", "isSplit", "dataStatus" })
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
	@XmlElement(name = "Lot")
	private String lot;
	@XmlElement(name = "ExpirationDate")
	private String expirationDate;
	@XmlElement(name = "TotalQty")
	private String totalQty;
	@XmlElement(name = "OnHandQty")
	private String onHandQty;
	@XmlElement(name = "AvailableQty")
	private String availableQty;
	@XmlElement(name = "InTransitQty")
	private String inTransitQty;
	
	@XmlElement(name = "Carrier")
	private String carrier;
	@XmlElement(name = "CarrierProduct")
	private String carrierProduct;
	@XmlElement(name = "IsSplit")
	private String isSplit;
	@XmlElement(name = "DataStatus")
	private String dataStatus;
	
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

	public String getDataStatus() {
		return dataStatus;
	}

	public void setDataStatus(String dataStatus) {
		String result = "";
		dataStatus = dataStatus == null? "":dataStatus;
		switch (dataStatus) {
			case "1100": 
				result = "生效";
				break;
			case "1400": 
				result = "已取消";
				break;
			case "1600": 
				result = "待審核";
				break;
			case "1700": 
				result = "已審核";
				break;
			case "1800": 
				result = "正在下發";
				break;
			case "1900": 
				result = "下發失敗";
				break;
			case "2000": 
				result = "已下發";
				break;
			case "2300": 
				result = "等待工作";
				break;
			case "2400": 
				result = "揀貨完成";
				break;
			case "2700": 
				result = "包裝完成";
				break;
			case "2900": 
				result = "發貨確認";
				break;
			case "3900": 
				result = "訂單已完成";
				break;
			default:
				result = "查無此狀態:" + dataStatus;
		}
		this.dataStatus = result;
	}

	public String getCarrier() {
		return carrier;
	}

	public void setCarrier(String carrier) {
		this.carrier = carrier;
	}

	public String getCarrierProduct() {
		return carrierProduct;
	}

	public void setCarrierProduct(String carrierProduct) {
		this.carrierProduct = carrierProduct;
	}

	public String getIsSplit() {
		return isSplit;
	}

	public void setIsSplit(String isSplit) {
		this.isSplit = isSplit;
	}

	public String getLot() {
		return lot;
	}

	public void setLot(String lot) {
		this.lot = lot;
	}

	public String getExpirationDate() {
		return expirationDate;
	}

	public void setExpirationDate(String expirationDate) {
		this.expirationDate = expirationDate;
	}

}
