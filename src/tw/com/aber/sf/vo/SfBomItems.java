package tw.com.aber.sf.vo;

import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

@XmlRootElement(name="Items")
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(propOrder = { "itemList" })
public class SfBomItems {
	private static final long serialVersionUID = 1L;
	
	@XmlElement(name = "Item")
    private List<SfBomItem> itemList;

	public List<SfBomItem> getItemList() {
		return itemList;
	}

	public void setItemList(List<SfBomItem> itemList) {
		this.itemList = itemList;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}
}
