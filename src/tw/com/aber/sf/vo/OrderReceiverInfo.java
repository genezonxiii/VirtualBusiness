package tw.com.aber.sf.vo;

import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name="OrderReceiverInfo")
@XmlAccessorType(XmlAccessType.FIELD)
public class OrderReceiverInfo {
	private static final long serialVersionUID = 1L;
	
	@XmlElement(name = "ReceiverCompany")
    private String receiverCompany;
	@XmlElement(name = "ReceiverName")
    private String receiverName;
	@XmlElement(name = "ReceiverZipCode")
    private String receiverZipCode;
	@XmlElement(name = "ReceiverMobile")
    private String receiverMobile;
	@XmlElement(name = "ReceiverCountry")
    private String receiverCountry;
	@XmlElement(name = "ReceiverAddress")
    private String receiverAddress;
	@XmlElement(name = "OrderItems")
    private OrderItems orderItems;
	
	public String getReceiverCompany() {
		return receiverCompany;
	}
	public void setReceiverCompany(String receiverCompany) {
		this.receiverCompany = receiverCompany;
	}
	public String getReceiverName() {
		return receiverName;
	}
	public void setReceiverName(String receiverName) {
		this.receiverName = receiverName;
	}
	public String getReceiverZipCode() {
		return receiverZipCode;
	}
	public void setReceiverZipCode(String receiverZipCode) {
		this.receiverZipCode = receiverZipCode;
	}
	public String getReceiverMobile() {
		return receiverMobile;
	}
	public void setReceiverMobile(String receiverMobile) {
		this.receiverMobile = receiverMobile;
	}
	public String getReceiverCountry() {
		return receiverCountry;
	}
	public void setReceiverCountry(String receiverCountry) {
		this.receiverCountry = receiverCountry;
	}
	public String getReceiverAddress() {
		return receiverAddress;
	}
	public void setReceiverAddress(String receiverAddress) {
		this.receiverAddress = receiverAddress;
	}
	public OrderItems getOrderItems() {
		return orderItems;
	}
	public void setOrderItems(OrderItems orderItems) {
		this.orderItems = orderItems;
	}
	public static long getSerialversionuid() {
		return serialVersionUID;
	}

    
}
