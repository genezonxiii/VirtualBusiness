package tw.com.aber.vo;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

@XmlRootElement(name = "State")
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(propOrder = { "opened", "disabled","selected" })
public class StateVO {
	private static final long serialVersionUID = 1L;
	@XmlElement(name = "opened")
	private boolean opened;
	@XmlElement(name = "disabled")
	private boolean disabled ;
	@XmlElement(name = "selected")
	private boolean selected ;
	
	public boolean isOpened() {
		return opened;
	}
	public void setOpened(boolean opened) {
		this.opened = opened;
	}
	public boolean isDisabled() {
		return disabled;
	}
	public void setDisabled(boolean disabled) {
		this.disabled = disabled;
	}
	public boolean isSelected() {
		return selected;
	}
	public void setSelected(boolean selected) {
		this.selected = selected;
	}
	public static long getSerialversionuid() {
		return serialVersionUID;
	} 
}
