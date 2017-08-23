package tw.com.aber.inv.vo;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

@XmlRootElement(name = "Seller")
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(propOrder = { "identifier", "name" })
public class Seller {
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

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

}
