package tw.com.aber.inv.vo;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

@XmlRootElement(name = "Buyer")
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(propOrder = { "identifier", "name", "address", "customerNumber" })
public class Buyer {
	private static final long serialVersionUID = 1L;
	/*
	 * A0401
	 */
	@XmlElement(name = "Identifier")
	private String identifier;

	/*
	 * A0401
	 */
	@XmlElement(name = "Name")
	private String name;

	/*
	 * A0401
	 */
	@XmlElement(name = "Address")
	private String address;

	/*
	 * A0401
	 */
	@XmlElement(name = "CustomerNumber")
	private String customerNumber;

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getIdentifier() {
		return identifier;
	}

	public void setIdentifier(String identifier) {
		this.identifier = identifier;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getCustomerNumber() {
		return customerNumber;
	}

	public void setCustomerNumber(String customerNumber) {
		this.customerNumber = customerNumber;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}
}
