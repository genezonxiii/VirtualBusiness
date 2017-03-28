package tw.com.aber.sf.vo;

import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

@XmlRootElement(name="PurchaseOrders")
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(propOrder = { "purchaseOrder" })
public class PurchaseOrders {
	private static final long serialVersionUID = 1L;
	
	@XmlElement(name = "PurchaseOrder")
    private List<PurchaseOrder> purchaseOrder;

	public List<PurchaseOrder> getPurchaseOrder() {
		return purchaseOrder;
	}

	public void setPurchaseOrder(List<PurchaseOrder> purchaseOrder) {
		this.purchaseOrder = purchaseOrder;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

}
