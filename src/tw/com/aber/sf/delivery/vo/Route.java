package tw.com.aber.sf.delivery.vo;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "Route")
@XmlAccessorType(XmlAccessType.FIELD)
public class Route {
	private static final long serialVersionUID = 1L;
	@XmlAttribute
	private String accept_time;
	@XmlAttribute
	private String accept_address;
	@XmlAttribute
	private String remark;
	@XmlAttribute
	private String opcode;

	public String getAccept_time() {
		return accept_time;
	}

	public void setAccept_time(String accept_time) {
		this.accept_time = accept_time;
	}

	public String getAccept_address() {
		return accept_address;
	}

	public void setAccept_address(String accept_address) {
		this.accept_address = accept_address;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public String getOpcode() {
		return opcode;
	}

	public void setOpcode(String opcode) {
		this.opcode = opcode;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

}