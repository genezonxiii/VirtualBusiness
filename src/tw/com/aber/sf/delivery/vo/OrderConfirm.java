package tw.com.aber.sf.delivery.vo;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

@XmlRootElement(name = "OrderConfirm")
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(propOrder = { "orderConfirmOption" })
public class OrderConfirm {
	private static final long serialVersionUID = 1L;

	@XmlAttribute(name = "orderid")
	private String orderid;

	@XmlAttribute(name = "mailno")
	private String mailno;

	@XmlAttribute(name = "dealtype")
	private String dealtype;
	
	@XmlElement(name = "OrderConfirmOption")
	private OrderConfirmOption orderConfirmOption;

	public OrderConfirmOption getOrderConfirmOption() {
		return orderConfirmOption;
	}

	public void setOrderConfirmOption(OrderConfirmOption orderConfirmOption) {
		this.orderConfirmOption = orderConfirmOption;
	}

	public String getOrderid() {
		return orderid;
	}

	public void setOrderid(String orderid) {
		this.orderid = orderid;
	}

	public String getMailno() {
		return mailno;
	}

	public void setMailno(String mailno) {
		this.mailno = mailno;
	}

	public String getDealtype() {
		return dealtype;
	}

	public void setDealtype(String dealtype) {
		this.dealtype = dealtype;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}
}
