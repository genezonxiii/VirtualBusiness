package tw.com.aber.sf.delivery.vo;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "Cargo")
@XmlAccessorType(XmlAccessType.FIELD)
public class Cargo {
	private static final long serialVersionUID = 1L;
	@XmlAttribute
	private String name;
	@XmlAttribute
	private String count;
	@XmlAttribute
	private String unit;
	@XmlAttribute
	private String weight;
	@XmlAttribute
	private String amount;
	@XmlAttribute
	private String currency;
	@XmlAttribute
	private String source_area;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getCount() {
		return count;
	}

	public void setCount(String count) {
		this.count = count;
	}

	public String getUnit() {
		return unit;
	}

	public void setUnit(String unit) {
		this.unit = unit;
	}

	public String getWeight() {
		return weight;
	}

	public void setWeight(String weight) {
		this.weight = weight;
	}

	public String getAmount() {
		return amount;
	}

	public void setAmount(String amount) {
		this.amount = amount;
	}

	public String getCurrency() {
		return currency;
	}

	public void setCurrency(String currency) {
		this.currency = currency;
	}

	public String getSource_area() {
		return source_area;
	}

	public void setSource_area(String source_area) {
		this.source_area = source_area;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}
}
