package tw.com.aber.inv.vo;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

@XmlRootElement(name = "Invoice")
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(propOrder = {"cancelInvoiceNumber", "sellerId", "buyerId", "invoiceDate", 
		"cancelDate", "cancelTime", "cancelReason",
		"returnTaxDocumentNumber", "remark"})
public class InvoiceC0501 extends Invoice{
	private static final long serialVersionUID = 1L;
	
	@XmlElement(name = "SellerId")
	private String sellerId;
	@XmlElement(name = "BuyerId")
	private String buyerId;
	@XmlElement(name = "CancelInvoiceNumber")
	private String cancelInvoiceNumber;
	@XmlElement(name = "InvoiceDate")
	private String invoiceDate;
	@XmlElement(name = "CancelDate")
	private String cancelDate;
	@XmlElement(name = "CancelTime")
	private String cancelTime;
	@XmlElement(name = "CancelReason")
	private String cancelReason;
	@XmlElement(name = "ReturnTaxDocumentNumber")
	private String returnTaxDocumentNumber;
	@XmlElement(name = "Remark")
	private String remark;

	public String getBuyerId() {
		return buyerId;
	}

	public void setBuyerId(String buyerId) {
		this.buyerId = buyerId;
	}

	public String getCancelInvoiceNumber() {
		return cancelInvoiceNumber;
	}

	public void setCancelInvoiceNumber(String cancelInvoiceNumber) {
		this.cancelInvoiceNumber = cancelInvoiceNumber;
	}

	public String getSellerId() {
		return sellerId;
	}

	public void setSellerId(String sellerId) {
		this.sellerId = sellerId;
	}

	public String getCancelDate() {
		return cancelDate;
	}

	public void setCancelDate(String cancelDate) {
		this.cancelDate = cancelDate;
	}

	public String getCancelTime() {
		return cancelTime;
	}

	public void setCancelTime(String cancelTime) {
		this.cancelTime = cancelTime;
	}

	public String getCancelReason() {
		return cancelReason;
	}

	public void setCancelReason(String cancelReason) {
		this.cancelReason = cancelReason;
	}

	public String getReturnTaxDocumentNumber() {
		return returnTaxDocumentNumber;
	}

	public void setReturnTaxDocumentNumber(String returnTaxDocumentNumber) {
		this.returnTaxDocumentNumber = returnTaxDocumentNumber;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public String getInvoiceDate() {
		return invoiceDate;
	}

	public void setInvoiceDate(String invoiceDate) {
		this.invoiceDate = invoiceDate;
	}
}
