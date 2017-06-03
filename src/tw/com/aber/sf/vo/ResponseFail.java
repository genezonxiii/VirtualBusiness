package tw.com.aber.sf.vo;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

@XmlRootElement(name = "responseFail")
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(propOrder = { "reasoncode", "remark" })
public class ResponseFail {
	private static final long serialVersionUID = 1L;
	@XmlElement(name = "reasoncode")
	private String reasoncode;
	@XmlElement(name = "remark")
	private String remark;

	public String getReasoncode() {
		return reasoncode;
	}

	public void setReasoncode(String reasoncode) {
		this.reasoncode = reasoncode;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}
}
