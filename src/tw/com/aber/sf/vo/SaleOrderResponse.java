package tw.com.aber.sf.vo;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

@XmlRootElement(name = "SaleOrderResponse")
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(propOrder = { "saleOrders" })
public class SaleOrderResponse {
	private static final long serialVersionUID = 1L;
	
	@XmlElement(name = "SaleOrders")
	private SaleOrders saleOrders;

	public SaleOrders getSaleOrders() {
		return saleOrders;
	}

	public void setSaleOrders(SaleOrders saleOrders) {
		this.saleOrders = saleOrders;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}
	
}
