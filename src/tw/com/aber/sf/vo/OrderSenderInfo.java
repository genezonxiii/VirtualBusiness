package tw.com.aber.sf.vo;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

@XmlRootElement(name="OrderSenderInfo")
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(propOrder = { "senderCompany", "senderName", "senderEmail", "senderZipCode", 
		"senderMobile","senderPhone", "senderCountry", "senderProvince", "senderAddress"})

public class OrderSenderInfo {
	private static final long serialVersionUID = 1L;
	
	@XmlElement(name = "SenderCompany")
    private String senderCompany;
	@XmlElement(name = "SenderName")
    private String senderName;
	@XmlElement(name = "SenderEmail")
    private String senderEmail;
	@XmlElement(name = "SenderZipCode")
    private String senderZipCode;
	@XmlElement(name = "SenderMobile")
    private String senderMobile;
	@XmlElement(name = "SenderPhone")
    private String senderPhone;
	@XmlElement(name = "SenderCountry")
    private String senderCountry;
	@XmlElement(name = "SenderProvince")
    private String senderProvince;
	@XmlElement(name = "SenderAddress")
    private String senderAddress;
	
	public String getSenderCompany() {
		return senderCompany;
	}
	public void setSenderCompany(String senderCompany) {
		this.senderCompany = senderCompany;
	}
	public String getSenderName() {
		return senderName;
	}
	public void setSenderName(String senderName) {
		this.senderName = senderName;
	}
	public String getSenderEmail() {
		return senderEmail;
	}
	public void setSenderEmail(String senderEmail) {
		this.senderEmail = senderEmail;
	}
	public String getSenderZipCode() {
		return senderZipCode;
	}
	public void setSenderZipCode(String senderZipCode) {
		this.senderZipCode = senderZipCode;
	}
	public String getSenderMobile() {
		return senderMobile;
	}
	public void setSenderMobile(String senderMobile) {
		this.senderMobile = senderMobile;
	}
	public String getSenderPhone() {
		return senderPhone;
	}
	public void setSenderPhone(String senderPhone) {
		this.senderPhone = senderPhone;
	}
	public String getSenderCountry() {
		return senderCountry;
	}
	public void setSenderCountry(String senderCountry) {
		this.senderCountry = senderCountry;
	}
	public String getSenderProvince() {
		return senderProvince;
	}
	public void setSenderProvince(String senderProvince) {
		this.senderProvince = senderProvince;
	}
	public String getSenderAddress() {
		return senderAddress;
	}
	public void setSenderAddress(String senderAddress) {
		this.senderAddress = senderAddress;
	}
}
