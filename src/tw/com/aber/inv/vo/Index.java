package tw.com.aber.inv.vo;

import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

@XmlRootElement(name = "INDEX")
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(propOrder = { "invoiceCode", "functionCode", "reply", "errorCode", "message","posDatas",
		"verionUpdate", "sellerId","posId", "posSn", "appVserion", "sysTime", "invoiceData",
		"userId", "taxMonth", "invoiceHeader", "invoiceStart","invoiceEnd", "invoiceNumber",
		"invoiceDate", "security", "sellDetail", "ckeckSum" })

public class Index {
	private static final long serialVersionUID = 1L;

	/*
	 * A01、C0401、C0501、B01、C01、Y01
	 */
	@XmlElement(name = "FUNCTIONCODE")
	private String functionCode;

	/*
	 * A03、C03、D01
	 */
	@XmlElement(name = "INVOICE_CODE")
	private String invoiceCode;

	/*
	 * A01、C0401、C0501、A03、B01、C01、C03、Y01、D01
	 */
	@XmlElement(name = "SELLERID")
	private String sellerId;

	/*
	 * A01、C0401、C0501、A03、B01、C01、C03、Y01、D01
	 */
	@XmlElement(name = "POSID")
	private String posId;

	/*
	 * A01、C0401、C0501、A03、B01、C01、C03、Y01、D01
	 */
	@XmlElement(name = "POSSN")
	private String posSn;

	/*
	 * A01、B01、C01、Y01
	 */
	@XmlElement(name = "APPVSERION")
	private String appVserion;

	/*
	 * A01、C0401、C0501、A03、B01、C01、C03、Y01、D01
	 */
	@XmlElement(name = "SYSTIME")
	private String sysTime;

	/*
	 * C03
	 */
	@XmlElement(name = "invoiceData")
	private List<InvoiceData> invoiceData;

	/*
	 * A01、B01、C01
	 */
	@XmlElement(name = "USERID")
	private String userId;

	/*
	 * A01、B01、C01
	 */
	@XmlElement(name = "TAXMONTH")
	private String taxMonth;

	/*
	 * A01、B01、C01
	 */
	@XmlElement(name = "INVOICEHEADER")
	private String invoiceHeader;

	/*
	 * A01、B01、C01
	 */
	@XmlElement(name = "INVOICESTART")
	private String invoiceStart;

	/*
	 * A01、B01、C01
	 */
	@XmlElement(name = "INVOICEEND")
	private String invoiceEnd;

	/*
	 * A01、C0401、C0501、B01、C01
	 */
	@XmlElement(name = "INVOICENUMBER")
	private String invoiceNumber;

	/*
	 * C0401、C0501
	 */
	@XmlElement(name = "INVOICEDATE")
	private String invoiceDate;

	/*
	 * A01、B01、C01
	 */
	@XmlElement(name = "SECURITY")
	private String security;

	/*
	 * A01、B01、C01
	 */
	@XmlElement(name = "SELLDETAIL")
	private String sellDetail;

	/*
	 * A01、B01、C01
	 */
	@XmlElement(name = "CKECKSUM")
	private String ckeckSum;

	/*
	 * A01、C0401、C0501、A03、B01、C01、C03、D01
	 */
	@XmlElement(name = "REPLY")
	private String reply;

	/*
	 * A01、C0401、C0501、A03、B01、C01、C03、D01
	 */
	@XmlElement(name = "MESSAGE")
	private String message;

	/*
	 * D01
	 */
	@XmlElement(name = "POSDATA")
	private List<PosData> posDatas;
	
	/*
	 * A01、B01、C01
	 */
	@XmlElement(name = "VERIONUPDATE")
	private String verionUpdate;

	/*
	 * C0401、C0501
	 */
	@XmlElement(name = "ERROR_CODE")
	private String errorCode;

	public List<InvoiceData> getInvoiceData() {
		return invoiceData;
	}

	public void setInvoiceData(List<InvoiceData> invoiceData) {
		this.invoiceData = invoiceData;
	}

	public String getInvoiceCode() {
		return invoiceCode;
	}

	public void setInvoiceCode(String invoiceCode) {
		this.invoiceCode = invoiceCode;
	}

	public String getInvoiceDate() {
		return invoiceDate;
	}

	public void setInvoiceDate(String invoiceDate) {
		this.invoiceDate = invoiceDate;
	}

	public String getErrorCode() {
		return errorCode;
	}

	public void setErrorCode(String errorCode) {
		this.errorCode = errorCode;
	}

	public String getFunctionCode() {
		return functionCode;
	}

	public void setFunctionCode(String functionCode) {
		this.functionCode = functionCode;
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

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getTaxMonth() {
		return taxMonth;
	}

	public void setTaxMonth(String taxMonth) {
		this.taxMonth = taxMonth;
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

	public String getInvoiceNumber() {
		return invoiceNumber;
	}

	public void setInvoiceNumber(String invoiceNumber) {
		this.invoiceNumber = invoiceNumber;
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

	public String getVerionUpdate() {
		return verionUpdate;
	}

	public void setVerionUpdate(String verionUpdate) {
		this.verionUpdate = verionUpdate;
	}

	public String getSecurity() {
		return security;
	}

	public void setSecurity(String security) {
		this.security = security;
	}

	public String getSellDetail() {
		return sellDetail;
	}

	public void setSellDetail(String sellDetail) {
		this.sellDetail = sellDetail;
	}

	public String getCkeckSum() {
		return ckeckSum;
	}

	public String getAppVserion() {
		return appVserion;
	}

	public void setAppVserion(String appVserion) {
		this.appVserion = appVserion;
	}

	public void setCkeckSum(String ckeckSum) {
		this.ckeckSum = ckeckSum;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

}
