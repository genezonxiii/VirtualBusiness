package tw.com.aber.sf.vo;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name="ItemQueryRequest")
@XmlAccessorType(XmlAccessType.FIELD)
public class ItemQueryRequest {
	private static final long serialVersionUID = 1L;
	
	@XmlElement(name = "CompanyCode")
    private String companyCode;
	@XmlElement(name = "SkuNoList")
    private SkuNoList skuNoList;
	
	public String getCompanyCode() {
		return companyCode;
	}
	public void setCompanyCode(String companyCode) {
		this.companyCode = companyCode;
	}
	public SkuNoList getSkuNoList() {
		return skuNoList;
	}
	public void setSkuNoList(SkuNoList skuNoList) {
		this.skuNoList = skuNoList;
	}
	public static long getSerialversionuid() {
		return serialVersionUID;
	}
	
	
}
