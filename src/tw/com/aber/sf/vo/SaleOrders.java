package tw.com.aber.sf.vo;

import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

@XmlRootElement(name="SaleOrders")
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(propOrder = { "saleOrder" })
public class SaleOrders {
	private static final long serialVersionUID = 1L;
	
	@XmlElement(name = "SaleOrder")
    private List<SaleOrder> saleOrder;

	public List<SaleOrder> getSaleOrder() {
		return saleOrder;
	}

	public void setSaleOrder(List<SaleOrder> saleOrder) {
		this.saleOrder = saleOrder;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}
    
}
