package tw.com.aber.sf.delivery.vo;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "RouteRequest")
@XmlAccessorType(XmlAccessType.FIELD)
public class RouteRequest {
	private static final long serialVersionUID = 1L;
	@XmlAttribute
	private String tracking_type;
	@XmlAttribute
	private String method_type;
	@XmlAttribute
	private String tracking_number;

	public String getTracking_type() {
		return tracking_type;
	}

	public void setTracking_type(String tracking_type) {
		this.tracking_type = tracking_type;
	}

	public String getMethod_type() {
		return method_type;
	}

	public void setMethod_type(String method_type) {
		this.method_type = method_type;
	}

	public String getTracking_number() {
		return tracking_number;
	}

	public void setTracking_number(String tracking_number) {
		this.tracking_number = tracking_number;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}
}
