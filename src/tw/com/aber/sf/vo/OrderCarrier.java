package tw.com.aber.sf.vo;

import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

@XmlRootElement(name="OrderCarrier")
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(propOrder = { "carrier","carrierProduct","paymentOfcharge","monthlyAccount", "carrierAddedServices"})
public class OrderCarrier {
	@XmlElement(name = "Carrier")
	private String carrier;
	@XmlElement(name = "CarrierProduct")
	private String carrierProduct;
	@XmlElement(name = "PaymentOfcharge")
	private String paymentOfcharge;
	@XmlElement(name = "MonthlyAccount")
	private String monthlyAccount;
	@XmlElement(name = "CarrierAddedServices")
	private CarrierAddedServices carrierAddedServices;
	
	public String getCarrier() {
		return carrier;
	}
	public void setCarrier(String carrier) {
		this.carrier = carrier;
	}
	public String getCarrierProduct() {
		return carrierProduct;
	}
	public void setCarrierProduct(String carrierProduct) {
		this.carrierProduct = carrierProduct;
	}
	public String getPaymentOfcharge() {
		return paymentOfcharge;
	}
	public void setPaymentOfcharge(String paymentOfcharge) {
		this.paymentOfcharge = paymentOfcharge;
	}
	public String getMonthlyAccount() {
		return monthlyAccount;
	}
	public void setMonthlyAccount(String monthlyAccount) {
		this.monthlyAccount = monthlyAccount;
	}
	public CarrierAddedServices getCarrierAddedServices() {
		return carrierAddedServices;
	}
	public void setCarrierAddedServices(CarrierAddedServices carrierAddedServices) {
		this.carrierAddedServices = carrierAddedServices;
	}

}
