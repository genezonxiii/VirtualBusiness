package tw.com.aber.sf.vo;

import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

@XmlRootElement(name="OrderReceiverInfo")
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(propOrder = { "orderItem" })
public class OrderItems {
	private static final long serialVersionUID = 1L;
	
	@XmlElement(name = "OrderItem")
    private List<OrderItem> orderItem;

	public List<OrderItem> getOrderItem() {
		return orderItem;
	}

	public void setOrderItem(List<OrderItem> orderItem) {
		this.orderItem = orderItem;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}
	

    
}
