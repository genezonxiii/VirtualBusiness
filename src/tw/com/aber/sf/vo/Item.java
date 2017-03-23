package tw.com.aber.sf.vo;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name="Item")
@XmlAccessorType(XmlAccessType.FIELD)
public class Item {
	private static final long serialVersionUID = 1L;
	
	@XmlElement(name = "SkuNo")
    private String skuNo;
	@XmlElement(name = "ItemName")
    private String itemName;
	@XmlElement(name = "Qty")
    private String Qty;
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
	public String getQty() {
		return Qty;
	}
	public void setQty(String qty) {
		Qty = qty;
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
