package tw.com.aber.inv.vo;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

@XmlRootElement(name = "Amount")
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(propOrder = { "salesAmount", "taxType", "taxRate", "taxAmount", "totalAmount" })
public class Amount {
	private static final long serialVersionUID = 1L;

	/*
	 * A0401
	 */
	@XmlElement(name = "SalesAmount")
	private String salesAmount;

	/*
	 * A0401
	 */
	@XmlElement(name = "TaxType")
	private String taxType;

	/*
	 * A0401
	 */
	@XmlElement(name = "TaxRate")
	private String taxRate;

	/*
	 * A0401
	 */
	@XmlElement(name = "TaxAmount")
	private String taxAmount;

	/*
	 * A0401
	 */
	@XmlElement(name = "TotalAmount")
	private String totalAmount;

	public String getSalesAmount() {
		return salesAmount;
	}

	public void setSalesAmount(String salesAmount) {
		this.salesAmount = salesAmount;
	}

	public String getTaxType() {
		return taxType;
	}

	public void setTaxType(String taxType) {
		this.taxType = taxType;
	}

	public String getTaxRate() {
		return taxRate;
	}

	public void setTaxRate(String taxRate) {
		this.taxRate = taxRate;
	}

	public String getTaxAmount() {
		return taxAmount;
	}

	public void setTaxAmount(String taxAmount) {
		this.taxAmount = taxAmount;
	}

	public String getTotalAmount() {
		return totalAmount;
	}

	public void setTotalAmount(String totalAmount) {
		this.totalAmount = totalAmount;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

}
