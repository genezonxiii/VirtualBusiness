package tw.com.aber.sf.delivery.vo;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "OrderConfirmOption")
@XmlAccessorType(XmlAccessType.FIELD)
public class OrderConfirmOption {
	private static final long serialVersionUID = 1L;
	
	@XmlAttribute(name="weight")
	private String weight;
	
	@XmlAttribute(name="volume")
	private String volume;

	public String getWeight() {
		return weight;
	}

	public void setWeight(String weight) {
		this.weight = weight;
	}

	public String getVolume() {
		return volume;
	}

	public void setVolume(String volume) {
		this.volume = volume;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}
}
