package tw.com.aber.sf.vo;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

@XmlRootElement(name = "VendorResponse")
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(propOrder = { "vendors" })
public class VendorResponse {
	private static final long serialVersionUID = 1L;

	@XmlElement(name = "Vendors")
	private Vendors vendors;

	public Vendors getVendors() {
		return vendors;
	}

	public void setVendors(Vendors vendors) {
		this.vendors = vendors;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

}
