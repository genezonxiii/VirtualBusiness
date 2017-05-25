package tw.com.aber.sf.vo;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

@XmlRootElement(name = "BomRequest")
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(propOrder = { "companyCode", "boms" })
public class BomRequest {
	private static final long serialVersionUID = 1L;

	@XmlElement(name = "CompanyCode")
	private String companyCode;
	@XmlElement(name = "Boms")
	private Boms boms;

	public String getCompanyCode() {
		return companyCode;
	}

	public void setCompanyCode(String companyCode) {
		this.companyCode = companyCode;
	}

	public Boms getBoms() {
		return boms;
	}

	public void setBoms(Boms boms) {
		this.boms = boms;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

}
