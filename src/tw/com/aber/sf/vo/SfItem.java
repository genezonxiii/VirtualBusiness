package tw.com.aber.sf.vo;

import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

@XmlRootElement(name = "Item")
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(propOrder = { "skuNo", "itemName", "description", "qty", 
		"barCode", "containers", "qtymin", "serialNumTrackInbound", 
		"serialNumTrackOutbound","serialNumTrackInventory", "bomAction", "result", "note",
		"planQty", "actualQty", "inventoryStatus", "lot", "expirationDate", "serialNumbers"})
public class SfItem {
	private static final long serialVersionUID = 1L;

	@XmlElement(name = "SkuNo")
	private String skuNo;
	@XmlElement(name = "ItemName")
	private String itemName;
	@XmlElement(name = "Description")
	private String description;
	@XmlElement(name = "Qty")
	private String qty;
	@XmlElement(name = "BarCode")
	private BarCode barCode;
	@XmlElement(name = "Containers")
	private Containers containers;
	@XmlElement(name = "Qtymin")
	private String qtymin;
	@XmlElement(name = "SerialNumTrackInbound")
	private String serialNumTrackInbound;
	@XmlElement(name = "SerialNumTrackOutbound")
	private String serialNumTrackOutbound;
	@XmlElement(name = "SerialNumTrackInventory")
	private String serialNumTrackInventory;
	@XmlElement(name = "BomAction")
	private String bomAction;
	@XmlElement(name = "InventoryStatus")
	private String inventoryStatus;
	
	//response use (商品查詢)
	@XmlElement(name = "Result")
	private String result;
	@XmlElement(name = "Note")
	private String note;
	
	//response use (入庫明細查詢)
	@XmlElement(name = "PlanQty")
	private String planQty;
	@XmlElement(name = "ActualQty")
	private String actualQty;
	@XmlElement(name = "Lot")
	private String lot;
	@XmlElement(name = "ExpirationDate")
	private String expirationDate;
	@XmlElement(name = "SerialNumbers")
    private SerialNumbers serialNumbers;

	
	public String getSkuNo() {
		return skuNo;
	}
	public void setSkuNo(String skuNo) {
		this.skuNo = skuNo;
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
	public String getItemName() {
		return itemName;
	}
	public void setItemName(String itemName) {
		this.itemName = itemName;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public String getQty() {
		return qty;
	}
	public void setQty(String qty) {
		this.qty = qty;
	}
	public BarCode getBarCode() {
		return barCode;
	}
	public void setBarCode(BarCode barCode) {
		this.barCode = barCode;
	}
	public Containers getContainers() {
		return containers;
	}
	public void setContainers(Containers containers) {
		this.containers = containers;
	}

	public String getSerialNumTrackInbound() {
		return serialNumTrackInbound;
	}
	public void setSerialNumTrackInbound(String serialNumTrackInbound) {
		this.serialNumTrackInbound = serialNumTrackInbound;
	}
	public String getSerialNumTrackOutbound() {
		return serialNumTrackOutbound;
	}
	public void setSerialNumTrackOutbound(String serialNumTrackOutbound) {
		this.serialNumTrackOutbound = serialNumTrackOutbound;
	}
	public String getSerialNumTrackInventory() {
		return serialNumTrackInventory;
	}
	public void setSerialNumTrackInventory(String serialNumTrackInventory) {
		this.serialNumTrackInventory = serialNumTrackInventory;
	}
	public String getBomAction() {
		return bomAction;
	}
	public void setBomAction(String bomAction) {
		this.bomAction = bomAction;
	}
	public String getQtymin() {
		return qtymin;
	}
	public void setQtymin(String qtymin) {
		this.qtymin = qtymin;
	}
	public String getInventoryStatus() {
		return inventoryStatus;
	}
	public void setInventoryStatus(String inventoryStatus) {
		this.inventoryStatus = inventoryStatus;
	}
	public String getLot() {
		return lot;
	}
	public void setLot(String lot) {
		this.lot = lot;
	}
	public String getPlanQty() {
		return planQty;
	}
	public void setPlanQty(String planQty) {
		this.planQty = planQty;
	}
	public String getActualQty() {
		return actualQty;
	}
	public void setActualQty(String actualQty) {
		this.actualQty = actualQty;
	}
	public String getExpirationDate() {
		return expirationDate;
	}
	public void setExpirationDate(String expirationDate) {
		this.expirationDate = expirationDate;
	}

	

}
