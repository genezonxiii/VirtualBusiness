package tw.com.aber.sf.vo;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

@XmlRootElement(name = "Sub")
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(propOrder = { "warehouseCode", "totalQty", "onHandQty", "availableQty", "inTransitQty" })
public class Sub {
	private static final long serialVersionUID = 1L;

	@XmlElement(name = "WarehouseCode")
	private String warehouseCode;
	@XmlElement(name = "TotalQty")
	private String totalQty;
	@XmlElement(name = "OnHandQty")
	private String onHandQty;
	@XmlElement(name = "AvailableQty")
	private String availableQty;
	@XmlElement(name = "InTransitQty")
	private String inTransitQty;

	public String getWarehouseCode() {
		return warehouseCode;
	}

	public void setWarehouseCode(String warehouseCode) {
		this.warehouseCode = warehouseCode;
	}

	public String getTotalQty() {
		return totalQty;
	}

	public void setTotalQty(String totalQty) {
		this.totalQty = totalQty;
	}

	public String getOnHandQty() {
		return onHandQty;
	}

	public void setOnHandQty(String onHandQty) {
		this.onHandQty = onHandQty;
	}

	public String getAvailableQty() {
		return availableQty;
	}

	public void setAvailableQty(String availableQty) {
		this.availableQty = availableQty;
	}

	public String getInTransitQty() {
		return inTransitQty;
	}

	public void setInTransitQty(String inTransitQty) {
		this.inTransitQty = inTransitQty;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}
}
