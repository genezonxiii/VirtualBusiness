package tw.com.aber.sf.vo;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

@XmlRootElement(name = "CancelPurchaseOrderResponse")
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(propOrder = { "purchaseOrders" })
public class CancelPurchaseOrderResponse {
	private static final long serialVersionUID = 1L;
	
	@XmlElement(name = "PurchaseOrders")
	private PurchaseOrders purchaseOrders;

	public PurchaseOrders getPurchaseOrders() {
		return purchaseOrders;
	}

	public void setPurchaseOrders(PurchaseOrders purchaseOrders) {
		this.purchaseOrders = purchaseOrders;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}
}
