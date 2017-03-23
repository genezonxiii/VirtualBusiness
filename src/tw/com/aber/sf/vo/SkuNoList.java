package tw.com.aber.sf.vo;

import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name="SkuNoList")
@XmlAccessorType(XmlAccessType.FIELD)
public class SkuNoList {
	private static final long serialVersionUID = 1L;
	
	@XmlElement(name = "SkuNo")
    private List<String> SkuNo;

	public List<String> getSkuNo() {
		return SkuNo;
	}

	public void setSkuNo(List<String> skuNo) {
		SkuNo = skuNo;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

}
