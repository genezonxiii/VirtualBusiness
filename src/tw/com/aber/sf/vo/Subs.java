package tw.com.aber.sf.vo;

import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

@XmlRootElement(name = "Subs")
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(propOrder = { "subList" })
public class Subs {
	private static final long serialVersionUID = 1L;

	@XmlElement(name = "Sub")
	private List<Sub> subList;

	public List<Sub> getSubList() {
		return subList;
	}

	public void setSubList(List<Sub> subList) {
		this.subList = subList;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

}
