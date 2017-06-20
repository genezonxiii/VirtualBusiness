package tw.com.aber.invoice.vo;

import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

@XmlRootElement(name = "Invoice")
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(propOrder = { "invoiceCode", "sellerId", "posId", "posSn",
		"sysTime", "reply", "message", "invoiceDatas" })

public class Invoice {
	private static final long serialVersionUID = 1L;
	/*
	 * A02
	 */
	@XmlElement(name = "INVOICE_CODE")
	private String invoiceCode;
	/*
	 * A02
	 */
	@XmlElement(name = "SELLERID")
	private String sellerId;
	/*
	 * A02
	 */
	@XmlElement(name = "POSID")
	private String posId;
	/*
	 * A02
	 */
	@XmlElement(name = "POSSN")
	private String posSn;
	/*
	 * A02
	 */
	@XmlElement(name = "SYSTIME")
	private String sysTime;
	/*
	 * A02
	 */
	@XmlElement(name = "REPLY")
	private String reply;
	/*
	 * A02
	 */
	@XmlElement(name = "MESSAGE")
	private String message;
	/*
	 * A02
	 */
	@XmlElement(name = "INVOICEDATA")
	private List<InvoiceData> invoiceDatas;

	public String getInvoiceCode() {
		return invoiceCode;
	}

	public void setInvoiceCode(String invoiceCode) {
		this.invoiceCode = invoiceCode;
	}

	public String getSellerId() {
		return sellerId;
	}

	public void setSellerId(String sellerId) {
		this.sellerId = sellerId;
	}

	public String getPosId() {
		return posId;
	}

	public void setPosId(String posId) {
		this.posId = posId;
	}

	public String getPosSn() {
		return posSn;
	}

	public void setPosSn(String posSn) {
		this.posSn = posSn;
	}

	public String getSysTime() {
		return sysTime;
	}

	public void setSysTime(String sysTime) {
		this.sysTime = sysTime;
	}

	public String getReply() {
		return reply;
	}

	public void setReply(String reply) {
		this.reply = reply;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public List<InvoiceData> getInvoiceDatas() {
		return invoiceDatas;
	}

	public void setInvoiceDatas(List<InvoiceData> invoiceDatas) {
		this.invoiceDatas = invoiceDatas;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}
}
