package tw.com.aber.inv.vo;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

@XmlRootElement(name = "INVOICEDATA")
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(propOrder = { "sellerId", "taxMonth", "type", "typeName",
		"invoiceHeader", "invoiceStart", "invoiceEnd" })

public class InvoiceData {
	private static final long serialVersionUID = 1L;
	
	/*
	 * A02、A03、A04、C02、C03、C04
	 */
	@XmlElement(name = "TAXMONTH")
	private String taxMonth;

	/*
	 * A02、A03、A04、C02、C03、C04
	 */
	@XmlElement(name = "TYPE")
	private String type;

	/*
	 * A03、C03
	 */
	@XmlElement(name = "TYPENAME")
	private String typeName;

	/*
	 * A02、A03、A04、C02、C03、C04
	 */
	@XmlElement(name = "INVOICEHEADER")
	private String invoiceHeader;

	/*
	 * A02、A03、A04、C02、C03、C04
	 */
	@XmlElement(name = "INVOICESTART")
	private String invoiceStart;

	/*
	 * A02、A03、A04、C02、C03、C04
	 */
	@XmlElement(name = "INVOICEEND")
	private String invoiceEnd;

	/*
	 * A03、A04、C03、C04
	 */
	@XmlElement(name = "SELLERID")
	private String sellerId;

	public String getTypeName() {
		return typeName;
	}

	public void setTypeName(String typeName) {
		this.typeName = typeName;
	}

	public String getSellerId() {
		return sellerId;
	}

	public void setSellerId(String sellerId) {
		this.sellerId = sellerId;
	}

	public String getTaxMonth() {
		return taxMonth;
	}

	public void setTaxMonth(String taxMonth) {
		this.taxMonth = taxMonth;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getInvoiceHeader() {
		return invoiceHeader;
	}

	public void setInvoiceHeader(String invoiceHeader) {
		this.invoiceHeader = invoiceHeader;
	}

	public String getInvoiceStart() {
		return invoiceStart;
	}

	public void setInvoiceStart(String invoiceStart) {
		this.invoiceStart = invoiceStart;
	}

	public String getInvoiceEnd() {
		return invoiceEnd;
	}

	public void setInvoiceEnd(String invoiceEnd) {
		this.invoiceEnd = invoiceEnd;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

}
