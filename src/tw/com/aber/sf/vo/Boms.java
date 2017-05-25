package tw.com.aber.sf.vo;

import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

@XmlRootElement(name="Boms")
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(propOrder = { "bomList" })
public class Boms {
	private static final long serialVersionUID = 1L;

	@XmlElement(name = "Bom")
	private List<Bom> bomList;

	public List<Bom> getBomList() {
		return bomList;
	}

	public void setBomList(List<Bom> bomList) {
		this.bomList = bomList;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

}
