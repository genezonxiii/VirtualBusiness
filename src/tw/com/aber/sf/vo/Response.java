package tw.com.aber.sf.vo;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

@XmlRootElement(name = "Response")
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(propOrder = { "head", "error", "body" })
public class Response {
	private static final long serialVersionUID = 1L;

	@XmlAttribute
	private String service;
	@XmlAttribute
	private String lang;
	@XmlElement(name = "Head")
	private String head;
	@XmlElement(name = "Error")
	private Error error;
	@XmlElement(name = "Body")
	private Body body;

	public String getLang() {
		return lang;
	}

	public void setLang(String lang) {
		this.lang = lang;
	}

	public Error getError() {
		return error;
	}

	public void setError(Error error) {
		this.error = error;
	}

	public String getService() {
		return service;
	}

	public void setService(String service) {
		this.service = service;
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
