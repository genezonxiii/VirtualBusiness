package tw.com.aber.sf.vo;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

@XmlRootElement(name="Item")
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(propOrder = { "skuNo", "itemName", "description", "qty", "barCode",
		"containers" })
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
    
	public String getSkuNo() {
		return skuNo;
	}
	public void setSkuNo(String skuNo) {
		this.skuNo = skuNo;
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
	public static long getSerialversionuid() {
		return serialVersionUID;
	}
    
    
}
