package tw.com.aber.sf.vo;

import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name="Items")
@XmlAccessorType(XmlAccessType.FIELD)
public class Items {
	private static final long serialVersionUID = 1L;
	
	@XmlElement(name = "Item")
    private List<SfItem> itemList;

	public List<SfItem> getItemList() {
		return itemList;
	}

	public void setItemList(List<SfItem> itemList) {
		this.itemList = itemList;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}


}
