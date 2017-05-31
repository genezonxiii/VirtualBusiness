package tw.com.aber.sf.vo;

import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

@XmlRootElement(name = "Vendors")
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(propOrder = { "vendorList" })
public class Vendors {
	private static final long serialVersionUID = 1L;
	@XmlElement(name = "Vendor")
	private List<Vendor> vendorList;

	public List<Vendor> getVendorList() {
		return vendorList;
	}

	public void setVendorList(List<Vendor> vendorList) {
		this.vendorList = vendorList;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

}
