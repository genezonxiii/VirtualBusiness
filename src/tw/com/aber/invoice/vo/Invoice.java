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
	/*
	 * C0401
	 */
	@XmlElement(name = "A1")
	private String A1;
	/*
	 * C0401
	 */
	@XmlElement(name = "A2")
	private String A2;
	/*
	 * C0401
	 */
	@XmlElement(name = "A3")
	private String A3;
	/*
	 * C0401
	 */
	@XmlElement(name = "A4")
	private String A4;
	/*
	 * C0401
	 */
	@XmlElement(name = "A5")
	private String A5;
	/*
	 * C0401
	 */
	@XmlElement(name = "A6")
	private String A6;
	/*
	 * C0401
	 */
	@XmlElement(name = "A7")
	private String A7;
	/*
	 * C0401
	 */
	@XmlElement(name = "A8")
	private String A8;
	/*
	 * C0401
	 */
	@XmlElement(name = "A9")
	private String A9;
	/*
	 * C0401
	 */
	@XmlElement(name = "A10")
	private String A10;
	/*
	 * C0401
	 */
	@XmlElement(name = "A11")
	private String A11;
	/*
	 * C0401
	 */
	@XmlElement(name = "A12")
	private String A12;
	/*
	 * C0401
	 */
	@XmlElement(name = "A13")
	private String A13;
	/*
	 * C0401
	 */
	@XmlElement(name = "A14")
	private String A14;
	/*
	 * C0401
	 */
	@XmlElement(name = "A15")
	private String A15;
	/*
	 * C0401
	 */
	@XmlElement(name = "A16")
	private String A16;
	/*
	 * C0401
	 */
	@XmlElement(name = "A17")
	private String A17;
	/*
	 * C0401
	 */
	@XmlElement(name = "A18")
	private String A18;
	/*
	 * C0401
	 */
	@XmlElement(name = "A19")
	private String A19;
	/*
	 * C0401
	 */
	@XmlElement(name = "A20")
	private String A20;
	/*
	 * C0401
	 */
	@XmlElement(name = "A21")
	private String A21;
	/*
	 * C0401
	 */
	@XmlElement(name = "A22")
	private String A22;
	/*
	 * C0401
	 */
	@XmlElement(name = "A23")
	private String A23;
	/*
	 * C0401
	 */
	@XmlElement(name = "A24")
	private String A24;
	/*
	 * C0401
	 */
	@XmlElement(name = "A25")
	private String A25;
	/*
	 * C0401
	 */
	@XmlElement(name = "A26")
	private String A26;
	/*
	 * C0401
	 */
	@XmlElement(name = "A27")
	private String A27;
	/*
	 * C0401
	 */
	@XmlElement(name = "A28")
	private String A28;
	/*
	 * C0401
	 */
	@XmlElement(name = "A29")
	private String A29;
	/*
	 * C0401
	 */
	@XmlElement(name = "A30")
	private String A30;
	/*
	 * C0401
	 */
	@XmlElement(name = "B")
	private List<B> B;
	
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
