package tw.com.aber.sf.delivery.vo;

import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

@XmlRootElement(name = "Order")
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(propOrder = { "cargos" })
public class Order {
	private static final long serialVersionUID = 1L;

	@XmlAttribute
	private String orderId;
	@XmlAttribute
	private String j_company;
	@XmlAttribute
	private String j_contact;
	@XmlAttribute
	private String j_tel;
	@XmlAttribute
	private String j_mobile;
	@XmlAttribute
	private String j_province;
	@XmlAttribute
	private String j_city;
	@XmlAttribute
	private String j_county;
	@XmlAttribute
	private String j_address;
	@XmlAttribute
	private String d_company;
	@XmlAttribute
	private String d_contact;
	@XmlAttribute
	private String d_tel;
	@XmlAttribute
	private String d_mobile;
	@XmlAttribute
	private String d_address;
	@XmlAttribute
	private String express_type;
	@XmlAttribute
	private String pay_method;
	@XmlAttribute
	private String parcel_quantity;
	@XmlAttribute
	private String cargo_length;
	@XmlAttribute
	private String cargo_width;
	@XmlAttribute
	private String cargo_height;
	@XmlAttribute
	private String remark;
	@XmlElement(name = "Cargo")
	private List<Cargo> cargos;

	public String getOrderId() {
		return orderId;
	}

	public void setOrderId(String orderId) {
		this.orderId = orderId;
	}

	public String getJ_company() {
		return j_company;
	}

	public void setJ_company(String j_company) {
		this.j_company = j_company;
	}

	public String getJ_contact() {
		return j_contact;
	}

	public void setJ_contact(String j_contact) {
		this.j_contact = j_contact;
	}

	public String getJ_tel() {
		return j_tel;
	}

	public void setJ_tel(String j_tel) {
		this.j_tel = j_tel;
	}

	public String getJ_mobile() {
		return j_mobile;
	}

	public void setJ_mobile(String j_mobile) {
		this.j_mobile = j_mobile;
	}

	public String getJ_province() {
		return j_province;
	}

	public void setJ_province(String j_province) {
		this.j_province = j_province;
	}

	public String getJ_city() {
		return j_city;
	}

	public void setJ_city(String j_city) {
		this.j_city = j_city;
	}

	public String getJ_county() {
		return j_county;
	}

	public void setJ_county(String j_county) {
		this.j_county = j_county;
	}

	public String getJ_address() {
		return j_address;
	}

	public void setJ_address(String j_address) {
		this.j_address = j_address;
	}

	public String getD_company() {
		return d_company;
	}

	public void setD_company(String d_company) {
		this.d_company = d_company;
	}

	public String getD_contact() {
		return d_contact;
	}

	public void setD_contact(String d_contact) {
		this.d_contact = d_contact;
	}

	public String getD_tel() {
		return d_tel;
	}

	public void setD_tel(String d_tel) {
		this.d_tel = d_tel;
	}

	public String getD_mobile() {
		return d_mobile;
	}

	public void setD_mobile(String d_mobile) {
		this.d_mobile = d_mobile;
	}

	public String getD_address() {
		return d_address;
	}

	public void setD_address(String d_address) {
		this.d_address = d_address;
	}

	public String getExpress_type() {
		return express_type;
	}

	public void setExpress_type(String express_type) {
		this.express_type = express_type;
	}

	public String getPay_method() {
		return pay_method;
	}

	public void setPay_method(String pay_method) {
		this.pay_method = pay_method;
	}

	public String getParcel_quantity() {
		return parcel_quantity;
	}

	public void setParcel_quantity(String parcel_quantity) {
		this.parcel_quantity = parcel_quantity;
	}

	public String getCargo_length() {
		return cargo_length;
	}

	public void setCargo_length(String cargo_length) {
		this.cargo_length = cargo_length;
	}

	public String getCargo_width() {
		return cargo_width;
	}

	public void setCargo_width(String cargo_width) {
		this.cargo_width = cargo_width;
	}

	public String getCargo_height() {
		return cargo_height;
	}

	public void setCargo_height(String cargo_height) {
		this.cargo_height = cargo_height;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public List<Cargo> getCargos() {
		return cargos;
	}

	public void setCargos(List<Cargo> cargos) {
		this.cargos = cargos;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}
}
