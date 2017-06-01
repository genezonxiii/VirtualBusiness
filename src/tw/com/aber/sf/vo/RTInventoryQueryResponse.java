package tw.com.aber.sf.vo;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

@XmlRootElement(name = "RTInventoryQueryResponse")
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(propOrder = { "companyCode", "warehouseCode", "rtInventorys" })
public class RTInventoryQueryResponse {
	private static final long serialVersionUID = 1L;
	@XmlElement(name = "CompanyCode")
	private String companyCode;
	@XmlElement(name = "WarehouseCode")
	private String warehouseCode;
	@XmlElement(name = "RTInventorys")
	private RTInventorys rtInventorys;

	public String getCompanyCode() {
		return companyCode;
	}

	public void setCompanyCode(String companyCode) {
		this.companyCode = companyCode;
	}

	public String getWarehouseCode() {
		return warehouseCode;
	}

	public void setWarehouseCode(String warehouseCode) {
		this.warehouseCode = warehouseCode;
	}

	public RTInventorys getRtInventorys() {
		return rtInventorys;
	}

	public void setRtInventorys(RTInventorys rtInventorys) {
		this.rtInventorys = rtInventorys;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

}
