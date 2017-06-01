package tw.com.aber.sf.vo;

import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

@XmlRootElement(name = "RTInventorys")
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(propOrder = { "rtiList" })
public class RTInventorys {
	private static final long serialVersionUID = 1L;
	
	@XmlElement(name = "RTInventory")
	private List<RTInventory> rtiList;

	public List<RTInventory> getRtiList() {
		return rtiList;
	}

	public void setRtiList(List<RTInventory> rtiList) {
		this.rtiList = rtiList;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

}
