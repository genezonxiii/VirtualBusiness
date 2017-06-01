package tw.com.aber.sf.vo;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

@XmlRootElement(name = "SaleOrderOutboundDetailResponse")
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(propOrder = { "result", "note","saleOrders" })
public class SaleOrderOutboundDetailResponse {
	private static final long serialVersionUID = 1L;

	@XmlElement(name = "Result")
	private String result;
	@XmlElement(name = "Note")
	private String note;
	@XmlElement(name = "SaleOrders")
    private SaleOrders saleOrders;

	public SaleOrders getSaleOrders() {
		return saleOrders;
	}

	public void setSaleOrders(SaleOrders saleOrders) {
		this.saleOrders = saleOrders;
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

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

}
