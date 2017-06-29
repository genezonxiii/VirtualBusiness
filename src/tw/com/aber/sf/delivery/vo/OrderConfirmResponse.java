package tw.com.aber.sf.delivery.vo;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "OrderConfirmResponse")
@XmlAccessorType(XmlAccessType.FIELD)
public class OrderConfirmResponse {
	private static final long serialVersionUID = 1L;

	@XmlAttribute(name="orderid")
	private String orderid;
	
	@XmlAttribute(name="mailno")
	private String mailno;
	
	@XmlAttribute(name="res_status")
	private String res_status;

	public String getOrderid() {
		return orderid;
	}

	public void setOrderId(String orderid) {
		this.orderid = orderid;
	}

	public String getMailno() {
		return mailno;
	}

	public void setMailno(String mailno) {
		this.mailno = mailno;
	}

	public String getRes_status() {
		return res_status;
	}

	public void setRes_status(String res_status) {
		this.res_status = res_status;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}
}
