package tw.com.aber.sf.delivery.vo;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "OrderResponse")
@XmlAccessorType(XmlAccessType.FIELD)
public class OrderResponse {
	private static final long serialVersionUID = 1L;
	@XmlAttribute(name="orderid")
	private String orderId;
	
	@XmlAttribute(name="mailno")
	private String mailno;
	
	@XmlAttribute(name="origincode")
	private String origincode;
	
	@XmlAttribute(name="destcode")
	private String destcode;
	
	@XmlAttribute(name="filter_result")
	private String filter_result;

	public String getOrderId() {
		return orderId;
	}

	public void setOrderId(String orderId) {
		this.orderId = orderId;
	}

	public String getMailno() {
		return mailno;
	}

	public void setMailno(String mailno) {
		this.mailno = mailno;
	}

	public String getOrigincode() {
		return origincode;
	}

	public void setOrigincode(String origincode) {
		this.origincode = origincode;
	}

	public String getDestcode() {
		return destcode;
	}

	public void setDestcode(String destcode) {
		this.destcode = destcode;
	}

	public String getFilter_result() {
		return filter_result;
	}

	public void setFilter_result(String filter_result) {
		this.filter_result = filter_result;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}
}
