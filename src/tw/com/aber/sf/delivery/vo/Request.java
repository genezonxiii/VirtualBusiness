package tw.com.aber.sf.delivery.vo;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

@XmlRootElement(name="Request")
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(propOrder = { "head", "body" })
public class Request {
	private static final long serialVersionUID = 1L;
	
	@XmlAttribute(name = "service")
	private String service;
	@XmlAttribute(name = "lang")
	private String lang;
	
	@XmlElement(name = "Head")
    private String head;
	@XmlElement(name = "Body")
    private Body body;
	
	public String getService() {
		return service;
	}
	public void setService(String service) {
		this.service = service;
	}
	public String getLang() {
		return lang;
	}
	public void setLang(String lang) {
		this.lang = lang;
	}
	public String getHead() {
		return head;
	}
	public void setHead(String head) {
		this.head = head;
	}
	public Body getBody() {
		return body;
	}
	public void setBody(Body body) {
		this.body = body;
	}
	public static long getSerialversionuid() {
		return serialVersionUID;
	}
	
}
