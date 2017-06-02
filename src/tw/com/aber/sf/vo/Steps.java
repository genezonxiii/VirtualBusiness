package tw.com.aber.sf.vo;

import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

@XmlRootElement(name = "Steps")
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(propOrder = { "stepList" })
public class Steps {
	private static final long serialVersionUID = 1L;

	@XmlElement(name = "Step")
	private List<Step> stepList;

	public List<Step> getStepList() {
		return stepList;
	}

	public void setStepList(List<Step> stepList) {
		this.stepList = stepList;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

}
