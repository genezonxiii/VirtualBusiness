package tw.com.aber.invoice.vo;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

@XmlRootElement(name = "INVOICEDATA")
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(propOrder = { "taxMonth", "type", "invoiceHeader", "invoiceStart", "invoiceEnd" })

public class InvoiceData {
	private static final long serialVersionUID = 1L;
	/*
	 * A02
	 */
	@XmlElement(name = "TAXMONTH")
	private String taxMonth;
	/*
	 * A02
	 */
	@XmlElement(name = "TYPE")
	private String type;
	/*
	 * A02
	 */
	@XmlElement(name = "INVOICEHEADER")
	private String invoiceHeader;
	/*
	 * A02
	 */
	@XmlElement(name = "INVOICESTART")
	private String invoiceStart;
	/*
	 * A02
	 */
	@XmlElement(name = "INVOICEEND")
	private String invoiceEnd;

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
