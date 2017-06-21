package tw.com.aber.inv.vo;

import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

@XmlRootElement(name = "Invoice")
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(propOrder = { "invoiceCode", "sellerId", "posId", "posSn", "sysTime", "reply", "message", "invoiceDatas",
		"invoiceNumber", "invoiceDate", "buyerId", "cancelDate", "cancelTime", "cancelReason",
		"returnTaxDocumentNumber", "remark", "A1", "A2", "A3", "A4", "A5", "A6", "A7", "A8", "A9", "A10", "A11", "A12",
		"A13", "A14", "A15", "A16", "A17", "A18", "A19", "A20", "A21", "A22", "A23", "A24", "A25", "A26", "A27", "A28",
		"A29", "A30", "B1", "B2", "B3", "B4", "B5", "B6", "B7", "C1", "C2", "C3", "C4", "C5", "C6", "C7", "D1", "D2",
		"D3", "D4" })

public class Invoice {
	private static final long serialVersionUID = 1L;
	/*
	 * A02、C0501、A04
	 */
	@XmlElement(name = "INVOICE_CODE")
	private String invoiceCode;

	/*
	 * A02、C0501、A04
	 */
	@XmlElement(name = "SELLERID")
	private String sellerId;

	/*
	 * A02、C0501、A04
	 */
	@XmlElement(name = "POSID")
	private String posId;

	/*
	 * A02、C0501、A04
	 */
	@XmlElement(name = "POSSN")
	private String posSn;

	/*
	 * A02、C0501、A04
	 */
	@XmlElement(name = "SYSTIME")
	private String sysTime;

	/*
	 * A02、A04
	 */
	@XmlElement(name = "REPLY")
	private String reply;

	/*
	 * A02、A04
	 */
	@XmlElement(name = "MESSAGE")
	private String message;

	/*
	 * A02
	 */
	@XmlElement(name = "INVOICEDATA")
	private List<InvoiceData> invoiceDatas;

	/*
	 * C0501
	 */
	@XmlElement(name = "INVOICE_NUMBER")
	private String invoiceNumber;

	/*
	 * C0501
	 */
	@XmlElement(name = "INVOICE_DATE")
	private String invoiceDate;

	/*
	 * C0501
	 */
	@XmlElement(name = "BUYERID")
	private String buyerId;

	/*
	 * C0501
	 */
	@XmlElement(name = "CANCEL_DATE")
	private String cancelDate;

	/*
	 * C0501
	 */
	@XmlElement(name = "CANCEL_TIME")
	private String cancelTime;

	/*
	 * C0501
	 */
	@XmlElement(name = "CANCEL_REASON")
	private String cancelReason;

	/*
	 * C0501
	 */
	@XmlElement(name = "RETURNTAXDOCUMENT_NUMBER")
	private String returnTaxDocumentNumber;

	/*
	 * C0501
	 */
	@XmlElement(name = "REMARK")
	private String remark;

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

	/*
	 * C0401
	 */
	@XmlElement(name = "C1")
	private String C1;

	/*
	 * C0401
	 */
	@XmlElement(name = "C2")
	private String C2;

	/*
	 * C0401
	 */
	@XmlElement(name = "C3")
	private String C3;

	/*
	 * C0401
	 */
	@XmlElement(name = "C4")
	private String C4;

	/*
	 * C0401
	 */
	@XmlElement(name = "C5")
	private String C5;

	/*
	 * C0401
	 */
	@XmlElement(name = "C6")
	private String C6;

	/*
	 * C0401
	 */
	@XmlElement(name = "C7")
	private String C7;

	/*
	 * C0401
	 */
	@XmlElement(name = "D1")
	private String D1;

	/*
	 * C0401
	 */
	@XmlElement(name = "D2")
	private String D2;

	/*
	 * C0401
	 */
	@XmlElement(name = "D3")
	private String D3;

	/*
	 * C0401
	 */
	@XmlElement(name = "D4")
	private String D4;

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

	public String getBuyerId() {
		return buyerId;
	}

	public void setBuyerId(String buyerId) {
		this.buyerId = buyerId;
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

	public String getA1() {
		return A1;
	}

	public void setA1(String a1) {
		A1 = a1;
	}

	public String getA2() {
		return A2;
	}

	public void setA2(String a2) {
		A2 = a2;
	}

	public String getA3() {
		return A3;
	}

	public void setA3(String a3) {
		A3 = a3;
	}

	public String getA4() {
		return A4;
	}

	public void setA4(String a4) {
		A4 = a4;
	}

	public String getA5() {
		return A5;
	}

	public void setA5(String a5) {
		A5 = a5;
	}

	public String getA6() {
		return A6;
	}

	public void setA6(String a6) {
		A6 = a6;
	}

	public String getA7() {
		return A7;
	}

	public void setA7(String a7) {
		A7 = a7;
	}

	public String getA8() {
		return A8;
	}

	public void setA8(String a8) {
		A8 = a8;
	}

	public String getA9() {
		return A9;
	}

	public void setA9(String a9) {
		A9 = a9;
	}

	public String getA10() {
		return A10;
	}

	public void setA10(String a10) {
		A10 = a10;
	}

	public String getA11() {
		return A11;
	}

	public void setA11(String a11) {
		A11 = a11;
	}

	public String getA12() {
		return A12;
	}

	public void setA12(String a12) {
		A12 = a12;
	}

	public String getA13() {
		return A13;
	}

	public void setA13(String a13) {
		A13 = a13;
	}

	public String getA14() {
		return A14;
	}

	public void setA14(String a14) {
		A14 = a14;
	}

	public String getA15() {
		return A15;
	}

	public void setA15(String a15) {
		A15 = a15;
	}

	public String getA16() {
		return A16;
	}

	public void setA16(String a16) {
		A16 = a16;
	}

	public String getA17() {
		return A17;
	}

	public void setA17(String a17) {
		A17 = a17;
	}

	public String getA18() {
		return A18;
	}

	public void setA18(String a18) {
		A18 = a18;
	}

	public String getA19() {
		return A19;
	}

	public void setA19(String a19) {
		A19 = a19;
	}

	public String getA20() {
		return A20;
	}

	public void setA20(String a20) {
		A20 = a20;
	}

	public String getA21() {
		return A21;
	}

	public void setA21(String a21) {
		A21 = a21;
	}

	public String getA22() {
		return A22;
	}

	public void setA22(String a22) {
		A22 = a22;
	}

	public String getA23() {
		return A23;
	}

	public void setA23(String a23) {
		A23 = a23;
	}

	public String getA24() {
		return A24;
	}

	public void setA24(String a24) {
		A24 = a24;
	}

	public String getA25() {
		return A25;
	}

	public void setA25(String a25) {
		A25 = a25;
	}

	public String getA26() {
		return A26;
	}

	public void setA26(String a26) {
		A26 = a26;
	}

	public String getA27() {
		return A27;
	}

	public void setA27(String a27) {
		A27 = a27;
	}

	public String getA28() {
		return A28;
	}

	public void setA28(String a28) {
		A28 = a28;
	}

	public String getA29() {
		return A29;
	}

	public void setA29(String a29) {
		A29 = a29;
	}

	public String getA30() {
		return A30;
	}

	public void setA30(String a30) {
		A30 = a30;
	}

	public List<B> getB() {
		return B;
	}

	public void setB(List<B> b) {
		B = b;
	}

	public String getC1() {
		return C1;
	}

	public void setC1(String c1) {
		C1 = c1;
	}

	public String getC2() {
		return C2;
	}

	public void setC2(String c2) {
		C2 = c2;
	}

	public String getC3() {
		return C3;
	}

	public void setC3(String c3) {
		C3 = c3;
	}

	public String getC4() {
		return C4;
	}

	public void setC4(String c4) {
		C4 = c4;
	}

	public String getC5() {
		return C5;
	}

	public void setC5(String c5) {
		C5 = c5;
	}

	public String getC6() {
		return C6;
	}

	public void setC6(String c6) {
		C6 = c6;
	}

	public String getC7() {
		return C7;
	}

	public void setC7(String c7) {
		C7 = c7;
	}

	public String getD1() {
		return D1;
	}

	public void setD1(String d1) {
		D1 = d1;
	}

	public String getD2() {
		return D2;
	}

	public void setD2(String d2) {
		D2 = d2;
	}

	public String getD3() {
		return D3;
	}

	public void setD3(String d3) {
		D3 = d3;
	}

	public String getD4() {
		return D4;
	}

	public void setD4(String d4) {
		D4 = d4;
	}

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
