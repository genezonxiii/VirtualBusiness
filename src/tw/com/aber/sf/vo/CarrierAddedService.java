package tw.com.aber.sf.vo;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

@XmlRootElement(name="CarrierAddedService")
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(propOrder = { "serviceCode", "attr01", "attr02", "attr03", "attr04", "attr05", 
		"attr06", "attr07", "attr08"})
public class CarrierAddedService {
	@XmlElement(name = "ServiceCode")
	private String serviceCode;
	@XmlElement(name = "Attr01")
	private String attr01;
	@XmlElement(name = "Attr02")
	private String attr02;
	@XmlElement(name = "Attr03")
	private String attr03;
	@XmlElement(name = "Attr04")
	private String attr04;
	@XmlElement(name = "Attr05")
	private String attr05;
	@XmlElement(name = "Attr06")
	private String attr06;
	@XmlElement(name = "Attr07")
	private String attr07;
	@XmlElement(name = "Attr08")
	private String attr08;
	
	public String getServiceCode() {
		return serviceCode;
	}
	public void setServiceCode(String serviceCode) {
		this.serviceCode = serviceCode;
	}
	public String getAttr01() {
		return attr01;
	}
	public void setAttr01(String attr01) {
		this.attr01 = attr01;
	}
	public String getAttr02() {
		return attr02;
	}
	public void setAttr02(String attr02) {
		this.attr02 = attr02;
	}
	public String getAttr03() {
		return attr03;
	}
	public void setAttr03(String attr03) {
		this.attr03 = attr03;
	}
	public String getAttr04() {
		return attr04;
	}
	public void setAttr04(String attr04) {
		this.attr04 = attr04;
	}
	public String getAttr05() {
		return attr05;
	}
	public void setAttr05(String attr05) {
		this.attr05 = attr05;
	}
	public String getAttr06() {
		return attr06;
	}
	public void setAttr06(String attr06) {
		this.attr06 = attr06;
	}
	public String getAttr07() {
		return attr07;
	}
	public void setAttr07(String attr07) {
		this.attr07 = attr07;
	}
	public String getAttr08() {
		return attr08;
	}
	public void setAttr08(String attr08) {
		this.attr08 = attr08;
	}
}
