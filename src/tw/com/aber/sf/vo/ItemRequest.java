package tw.com.aber.sf.vo;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name="ItemRequest")
@XmlAccessorType(XmlAccessType.FIELD)
public class ItemRequest {
	private static final long serialVersionUID = 1L;
	
	@XmlElement(name = "CompanyCode")
    private String companyCode;
	@XmlElement(name = "Items")
    private Items items;
	
	public String getCompanyCode() {
		return companyCode;
	}
	public void setCompanyCode(String companyCode) {
		this.companyCode = companyCode;
	}
	public Items getItems() {
		return items;
	}
	public void setItems(Items items) {
		this.items = items;
	}
	public static long getSerialversionuid() {
		return serialVersionUID;
	}
    


}
