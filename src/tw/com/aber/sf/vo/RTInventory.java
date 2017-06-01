package tw.com.aber.sf.vo;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

@XmlRootElement(name = "RTInventory")
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(propOrder = { "result", "note","skuNo", "header", "subs" })
public class RTInventory {
	private static final long serialVersionUID = 1L;

	@XmlElement(name = "Result")
	private String result;
	@XmlElement(name = "Note")
	private String note;
	@XmlElement(name = "SkuNo")
	private String skuNo;
	@XmlElement(name = "Header")
	private Header header;
	@XmlElement(name = "Subs")
	private Subs subs;

	public String getSkuNo() {
		return skuNo;
	}

	public void setSkuNo(String skuNo) {
		this.skuNo = skuNo;
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

	public Header getHeader() {
		return header;
	}

	public void setHeader(Header header) {
		this.header = header;
	}

	public Subs getSubs() {
		return subs;
	}

	public void setSubs(Subs subs) {
		this.subs = subs;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}
}
