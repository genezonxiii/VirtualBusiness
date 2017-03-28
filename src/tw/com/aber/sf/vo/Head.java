package tw.com.aber.sf.vo;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

@XmlRootElement(name="Head")
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(propOrder = { "accessCode", "checkword" })
public class Head {
	private static final long serialVersionUID = 1L;
	
	@XmlElement(name = "AccessCode")
    private String accessCode;
	@XmlElement(name = "Checkword")
    private String checkword;
	
	public String getAccessCode() {
		return accessCode;
	}
	public void setAccessCode(String accessCode) {
		this.accessCode = accessCode;
	}
	public String getCheckword() {
		return checkword;
	}
	public void setCheckword(String checkword) {
		this.checkword = checkword;
	}
	public static long getSerialversionuid() {
		return serialVersionUID;
	}
    
}
