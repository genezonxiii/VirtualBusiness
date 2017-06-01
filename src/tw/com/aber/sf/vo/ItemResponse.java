package tw.com.aber.sf.vo;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

@XmlRootElement(name="ItemResponse")
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(propOrder = { "companyCode","result", "items" })
public class ItemResponse {
	private static final long serialVersionUID = 1L;

	@XmlElement(name = "companyCode")
	private String companyCode;
	@XmlElement(name = "Result")
	private String result;
	@XmlElement(name = "Items")
	private Items items;

	public String getCompanyCode() {
		return companyCode;
	}

	public void setCompanyCode(String companyCode) {
		this.companyCode = companyCode;
	}

	public String getResult() {
		return result;
	}

	public void setResult(String result) {
		this.result = result;
	}

	public Items getItems() {
		return items;
	}

	public void setItems(Items items) {
		this.items = items;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

}
