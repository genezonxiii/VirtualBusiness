package tw.com.aber.sf.vo;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

@XmlRootElement(name = "Bom")
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(propOrder = { "item", "result", "note", "skuNo", "items" })
public class Bom {
	private static final long serialVersionUID = 1L;

	@XmlElement(name = "Item")
	private String item;
	@XmlElement(name = "Result")
	private String result;
	@XmlElement(name = "Note")
	private String note;

	@XmlElement(name = "SkuNo")
	private String skuNo;
	@XmlElement(name = "Items")
	private SfBomItems items;

	public String getItem() {
		return item;
	}

	public void setItem(String item) {
		this.item = item;
	}

	public String getResult() {
		return result;
	}

	public void setResult(String result) {
		this.result = result;
	}

	public String getNote() {
		return note;
	}

	public void setNote(String note) {
		this.note = note;
	}

	public String getSkuNo() {
		return skuNo;
	}

	public void setSkuNo(String skuNo) {
		this.skuNo = skuNo;
	}

	public SfBomItems getItems() {
		return items;
	}

	public void setItems(SfBomItems items) {
		this.items = items;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

}
