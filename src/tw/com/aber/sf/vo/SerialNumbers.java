package tw.com.aber.sf.vo;

import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

@XmlRootElement(name = "SerialNumbers")
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(propOrder = { "serialNumber" })

public class SerialNumbers {
	private static final long serialVersionUID = 1L;

	@XmlElement(name = "SerialNumber")
	private List<String> serialNumber;

	public List<String> getSerialNumber() {
		return serialNumber;
	}

	public void setSerialNumber(List<String> serialNumber) {
		this.serialNumber = serialNumber;
	}

}
