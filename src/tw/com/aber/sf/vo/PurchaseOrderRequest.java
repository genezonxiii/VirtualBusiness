package tw.com.aber.sf.vo;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name="ItemRequest")
@XmlAccessorType(XmlAccessType.FIELD)
public class PurchaseOrderRequest {
	private static final long serialVersionUID = 1L;
	
	@XmlElement(name = "CompanyCode")
    private String companyCode;
	@XmlElement(name = "PurchaseOrders")
    private PurchaseOrders purchaseOrders;
	
	public String getCompanyCode() {
		return companyCode;
	}
	public void setCompanyCode(String companyCode) {
		this.companyCode = companyCode;
	}
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
