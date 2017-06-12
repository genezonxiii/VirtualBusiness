package tw.com.aber.sf.vo;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

@XmlRootElement(name = "Item")
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(propOrder = { "skuNo", "result", "note", "itemName", "description", "qty", 
		"barCode", "containers", "qtymin", "serialNumTrackInbound", 
		"serialNumTrackOutbound","serialNumTrackInventory", "bomAction" })
public class SfItem {
	private static final long serialVersionUID = 1L;

	@XmlElement(name = "SkuNo")
	private String skuNo;
	@XmlElement(name = "Result")
	private String result;
	@XmlElement(name = "Note")
	private String note;
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

	

}
