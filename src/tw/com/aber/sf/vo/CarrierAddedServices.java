package tw.com.aber.sf.vo;

import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

@XmlRootElement(name="CarrierAddedService")
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(propOrder = { "carrierAddedServicesList"})
public class CarrierAddedServices {
	@XmlElement(name = "CarrierAddedService")
	private List<CarrierAddedService> carrierAddedServicesList;

	public List<CarrierAddedService> getCarrierAddedServicesList() {
		return carrierAddedServicesList;
	}

	public void setCarrierAddedServicesList(List<CarrierAddedService> carrierAddedServicesList) {
		this.carrierAddedServicesList = carrierAddedServicesList;
	}
	
}
