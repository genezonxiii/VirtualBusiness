package tw.com.aber.sf.vo;

import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

@XmlRootElement(name="OrderReceiverInfo")
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(propOrder = { "skuNo", "itemName", "itemQuantity", "bomAction", "isPresent", "inventoryStatus" })
public class OrderItem {
	private static final long serialVersionUID = 1L;
	
	@XmlElement(name = "SkuNo")
    private String skuNo;
	@XmlElement(name = "ItemName")
    private String itemName;
	@XmlElement(name = "ItemQuantity")
    private String itemQuantity;
	@XmlElement(name = "BomAction")
    private String bomAction;
	@XmlElement(name = "IsPresent")
    private String isPresent;
	@XmlElement(name = "InventoryStatus")
    private String inventoryStatus;
	
	public String getSkuNo() {
		return skuNo;
	}
	public void setSkuNo(String skuNo) {
		this.skuNo = skuNo;
	}
	public String getItemQuantity() {
		return itemQuantity;
	}
	public void setItemQuantity(String itemQuantity) {
		this.itemQuantity = itemQuantity;
	}
	public static long getSerialversionuid() {
		return serialVersionUID;
	}
	public String getItemName() {
		return itemName;
	}
	public void setItemName(String itemName) {
		this.itemName = itemName;
	}
	public String getBomAction() {
		return bomAction;
	}
	public void setBomAction(String bomAction) {
		this.bomAction = bomAction;
	}
	public String getInventoryStatus() {
		return inventoryStatus;
	}
	public void setInventoryStatus(String inventoryStatus) {
		this.inventoryStatus = inventoryStatus;
	}
}
