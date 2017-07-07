package tw.com.aber.sf.delivery.vo;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "OrderSearch")
@XmlAccessorType(XmlAccessType.FIELD)
public class OrderSearch {
	private static final long serialVersionUID = 1L;

	@XmlAttribute(name = "orderid")
	private String orderid;

	public String getOrderid() {
		return orderid;
	}

	public void setOrderid(String orderid) {
		this.orderid = orderid;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

}
