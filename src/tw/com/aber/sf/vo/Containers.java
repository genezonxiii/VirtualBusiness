package tw.com.aber.sf.vo;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

@XmlRootElement(name="Containers")
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(propOrder = { "container" })
public class Containers {
	private static final long serialVersionUID = 1L;
	
	@XmlElement(name = "Container")
    private SfContainer container;

	public SfContainer getContainer() {
		return container;
	}

	public void setContainer(SfContainer container) {
		this.container = container;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

}
