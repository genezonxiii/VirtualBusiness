package tw.com.aber.sf.vo;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

@XmlRootElement(name = "PurchaseOrderInboundResponse")
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(propOrder = { "result","note","purchaseOrders" })
public class PurchaseOrderInboundResponse {
	private static final long serialVersionUID = 1L;

	@XmlElement(name = "Result")
	private String result;
	@XmlElement(name = "Note")
	private String note;
	@XmlElement(name = "PurchaseOrders")
	private PurchaseOrders purchaseOrders;

	public PurchaseOrders getPurchaseOrders() {
		return purchaseOrders;
	}

	public void setPurchaseOrders(PurchaseOrders purchaseOrders) {
		this.purchaseOrders = purchaseOrders;
	}

	public String getResult() {
		return result;
	}

	public void setResult(String result) {
		this.result = result;
	}

	public String getNote() {
		return note;
	}

	public void setNote(String note) {
		this.note = note;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

}
