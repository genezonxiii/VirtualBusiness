package tw.com.aber.sf.vo;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

@XmlRootElement(name="PurchaseOrder")
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(propOrder = { "warehouseCode", "erpOrder", "erpOrderType", "sFOrderType", "scheduledReceiptDate",
		"vendorCode", "items"})
public class PurchaseOrder {
	private static final long serialVersionUID = 1L;
	
	@XmlElement(name = "WarehouseCode")
    private String warehouseCode;
	@XmlElement(name = "ErpOrder")
    private String erpOrder;
	@XmlElement(name = "ErpOrderType")
    private String erpOrderType;
	@XmlElement(name = "SFOrderType")
    private String sFOrderType;
	@XmlElement(name = "ScheduledReceiptDate")
    private String scheduledReceiptDate;
	@XmlElement(name = "VendorCode")
    private String vendorCode;
	@XmlElement(name = "Items")
    private Items items;
	
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
	public String getErpOrderType() {
		return erpOrderType;
	}
	public void setErpOrderType(String erpOrderType) {
		this.erpOrderType = erpOrderType;
	}
	public String getsFOrderType() {
		return sFOrderType;
	}
	public void setsFOrderType(String sFOrderType) {
		this.sFOrderType = sFOrderType;
	}
	public String getScheduledReceiptDate() {
		return scheduledReceiptDate;
	}
	public void setScheduledReceiptDate(String scheduledReceiptDate) {
		this.scheduledReceiptDate = scheduledReceiptDate;
	}
	public String getVendorCode() {
		return vendorCode;
	}
	public void setVendorCode(String vendorCode) {
		this.vendorCode = vendorCode;
	}
	public Items getItems() {
		return items;
	}
	public void setItems(Items items) {
		this.items = items;
	}
	public static long getSerialversionuid() {
		return serialVersionUID;
	}
	
	

}
