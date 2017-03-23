package tw.com.aber.sf.vo;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name="Container")
@XmlAccessorType(XmlAccessType.FIELD)
public class SfContainer {
	private static final long serialVersionUID = 1L;
	
	@XmlElement(name = "PackUm")
    private String packUm;

	public String getPackUm() {
		return packUm;
	}

	public void setPackUm(String packUm) {
		this.packUm = packUm;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

}
