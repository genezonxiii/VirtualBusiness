package tw.com.aber.sf.vo;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

@XmlRootElement(name = "BomResponse")
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(propOrder = { "boms" })
public class BomResponse {
	private static final long serialVersionUID = 1L;

	@XmlElement(name = "Boms")
	private Boms boms;

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
