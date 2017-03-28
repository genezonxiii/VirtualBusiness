package tw.com.aber.sf.vo;

import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

@XmlRootElement(name="SkuNoList")
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(propOrder = { "skuNo" })
public class SkuNoList {
	private static final long serialVersionUID = 1L;
	
	@XmlElement(name = "SkuNo")
    private List<String> skuNo;

	public List<String> getSkuNo() {
		return skuNo;
	}

	public void setSkuNo(List<String> skuNo) {
		this.skuNo = skuNo;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}


}
