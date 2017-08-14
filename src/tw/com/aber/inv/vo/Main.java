package tw.com.aber.inv.vo;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

@XmlRootElement(name = "Main")
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(propOrder = { "invoiceNumber", "invoiceDate", "invoiceTime", "seller", "buyer", "invoiceType", "donateMark",
		"printMark" })
public class Main {
	private static final long serialVersionUID = 1L;
	
	/*
	 * A0401
	 */
	@XmlElement(name = "InvoiceNumber")
	private String invoiceNumber;
	
	/*
	 * A0401
	 */
	@XmlElement(name = "InvoiceDate")
	public String invoiceDate;
	
	/*
	 * A0401
	 */
	@XmlElement(name = "InvoiceTime")
	public String invoiceTime;
	
	/*
	 * A0401
	 */
	@XmlElement(name = "Seller")
	private Seller seller;
	
	/*
	 * A0401
	 */
	@XmlElement(name = "Buyer")
	private Buyer buyer;
	
	/*
	 * A0401
	 */
	@XmlElement(name = "InvoiceType")
	private String invoiceType;
	
	/*
	 * A0401
	 */
	@XmlElement(name = "DonateMark")
	public String donateMark;
	
	/*
	 * A0401
	 */
	@XmlElement(name = "PrintMark")
	public String printMark;

	public String getInvoiceNumber() {
		return invoiceNumber;
	}

	public void setInvoiceNumber(String invoiceNumber) {
		this.invoiceNumber = invoiceNumber;
	}

	public String getInvoiceDate() {
		return invoiceDate;
	}

	public void setInvoiceDate(String invoiceDate) {
		this.invoiceDate = invoiceDate;
	}

	public String getInvoiceTime() {
		return invoiceTime;
	}

	public void setInvoiceTime(String invoiceTime) {
		this.invoiceTime = invoiceTime;
	}

	public Seller getSeller() {
		return seller;
	}

	public void setSeller(Seller seller) {
		this.seller = seller;
	}

	public Buyer getBuyer() {
		return buyer;
	}

	public void setBuyer(Buyer buyer) {
		this.buyer = buyer;
	}

	public String getInvoiceType() {
		return invoiceType;
	}

	public void setInvoiceType(String invoiceType) {
		this.invoiceType = invoiceType;
	}

	public String getDonateMark() {
		return donateMark;
	}

	public void setDonateMark(String donateMark) {
		this.donateMark = donateMark;
	}

	public String getPrintMark() {
		return printMark;
	}

	public void setPrintMark(String printMark) {
		this.printMark = printMark;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}
	
}
