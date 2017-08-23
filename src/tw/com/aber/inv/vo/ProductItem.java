package tw.com.aber.inv.vo;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

@XmlRootElement(name = "ProductItem")
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(propOrder = { "description", "quantity", "unitPrice", "amount", "sequenceNumber", "remark" })
public class ProductItem {
	private static final long serialVersionUID = 1L;

	/*
	 * A0401
	 */
	@XmlElement(name = "Description")
	private String description;

	/*
	 * A0401
	 */
	@XmlElement(name = "Quantity")
	private String quantity;

	/*
	 * A0401
	 */
	@XmlElement(name = "UnitPrice")
	private String unitPrice;

	/*
	 * A0401
	 */
	@XmlElement(name = "Amount")
	private String amount;

	/*
	 * A0401
	 */
	@XmlElement(name = "SequenceNumber")
	private String sequenceNumber;

	/*
	 * A0401
	 */
	@XmlElement(name = "Remark")
	private String remark;

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getQuantity() {
		return quantity;
	}

	public void setQuantity(String quantity) {
		this.quantity = quantity;
	}

	public String getUnitPrice() {
		return unitPrice;
	}

	public void setUnitPrice(String unitPrice) {
		this.unitPrice = unitPrice;
	}

	public String getAmount() {
		return amount;
	}

	public void setAmount(String amount) {
		this.amount = amount;
	}

	public String getSequenceNumber() {
		return sequenceNumber;
	}

	public void setSequenceNumber(String sequenceNumber) {
		this.sequenceNumber = sequenceNumber;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

}
