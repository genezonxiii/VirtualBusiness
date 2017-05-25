package tw.com.aber.sf.vo;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

@XmlRootElement(name = "Item")
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(propOrder = { "sequence", "skuNo", "quantity" })
public class SfBomItem {
	private static final long serialVersionUID = 1L;

	@XmlElement(name = "Sequence")
	private String sequence;
	@XmlElement(name = "SkuNo")
	private String skuNo;
	@XmlElement(name = "Quantity")
	private String quantity;

	public String getSequence() {
		return sequence;
	}

	public void setSequence(String sequence) {
		this.sequence = sequence;
	}

	public String getSkuNo() {
		return skuNo;
	}

	public void setSkuNo(String skuNo) {
		this.skuNo = skuNo;
	}

	public void setQuantity(String quantity) {
		this.quantity = quantity;
	}

	public String getQuantity() {
		return quantity;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

}
