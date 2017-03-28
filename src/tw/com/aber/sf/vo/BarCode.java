package tw.com.aber.sf.vo;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

@XmlRootElement(name="BarCode")
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(propOrder = { "barCode1", "barCode2", "barCode3", "barCode4" })
public class BarCode {
	private static final long serialVersionUID = 1L;
	
	@XmlElement(name = "BarCode1")
    private String barCode1;
	@XmlElement(name = "BarCode2")
    private String barCode2;
    @XmlElement(name = "BarCode3")
    private String barCode3;
    @XmlElement(name = "BarCode4")
    private String barCode4;
    
	public String getBarCode1() {
		return barCode1;
	}
	public void setBarCode1(String barCode1) {
		this.barCode1 = barCode1;
	}
	public String getBarCode2() {
		return barCode2;
	}
	public void setBarCode2(String barCode2) {
		this.barCode2 = barCode2;
	}
	public String getBarCode3() {
		return barCode3;
	}
	public void setBarCode3(String barCode3) {
		this.barCode3 = barCode3;
	}
	public String getBarCode4() {
		return barCode4;
	}
	public void setBarCode4(String barCode4) {
		this.barCode4 = barCode4;
	}
	public static long getSerialversionuid() {
		return serialVersionUID;
	}
    

}
