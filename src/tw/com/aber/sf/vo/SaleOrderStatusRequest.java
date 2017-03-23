package tw.com.aber.sf.vo;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name="SaleOrderStatusRequest")
@XmlAccessorType(XmlAccessType.FIELD)
public class SaleOrderStatusRequest {
	private static final long serialVersionUID = 1L;
	
	@XmlElement(name = "CompanyCode")
    private String companyCode;
	@XmlElement(name = "SaleOrders")
    private SaleOrders saleOrders;
	
	public String getCompanyCode() {
		return companyCode;
	}
	public void setCompanyCode(String companyCode) {
		this.companyCode = companyCode;
	}
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
