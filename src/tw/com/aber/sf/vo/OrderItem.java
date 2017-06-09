package tw.com.aber.sf.vo;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

@XmlRootElement(name="OrderReceiverInfo")
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(propOrder = { "skuNo", "itemQuantity" })
public class OrderItem {
	private static final long serialVersionUID = 1L;
	
	@XmlElement(name = "SkuNo")
    private String skuNo;
	@XmlElement(name = "ItemQuantity")
    private String itemQuantity;
	
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
}
