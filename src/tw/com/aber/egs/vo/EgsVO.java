package tw.com.aber.egs.vo;

import java.io.Serializable;
import java.sql.Date;

public class EgsVO implements Serializable {

	private static final long serialVersionUID = 1L;

	private String egs_id;
	private String group_id;
	private String customer_id;
	private String waybill_type;
	private String tracking_number;
	private String order_no;
	private String receiver_name;
	private String receiver_address;
	private String receiver_suda5;
	private String receiver_suda7;
	private String receiver_mobile;
	private String receiver_phone;
	private String sender_name;
	private String sender_address;
	private String sender_suda5;
	private String sender_phone;
	private String product_price;
	private String product_name;
	private String egs_comment;
	private String package_size;
	private String temperature;
	private String distance;
	private String delivery_date;
	private String delivery_timezone;
	private Date create_time;
	private Date print_time;
	private String account_id;
	private String member_no;
	private String taxin;
	private String insurance;
	private String ship_ids;

	public String getEgs_id() {
		return egs_id;
	}

	public void setEgs_id(String egs_id) {
		this.egs_id = egs_id;
	}

	public String getGroup_id() {
		return group_id;
	}

	public void setGroup_id(String group_id) {
		this.group_id = group_id;
	}

	public String getCustomer_id() {
		return customer_id;
	}

	public void setCustomer_id(String customer_id) {
		this.customer_id = customer_id;
	}

	public String getTracking_number() {
		return tracking_number;
	}

	public void setTracking_number(String tracking_number) {
		this.tracking_number = tracking_number;
	}

	public String getOrder_no() {
		return order_no;
	}

	public void setOrder_no(String order_no) {
		this.order_no = order_no;
	}

	public String getReceiver_name() {
		return receiver_name;
	}

	public void setReceiver_name(String receiver_name) {
		this.receiver_name = receiver_name;
	}

	public String getReceiver_address() {
		return receiver_address;
	}

	public void setReceiver_address(String receiver_address) {
		this.receiver_address = receiver_address;
	}

	public String getReceiver_suda5() {
		return receiver_suda5;
	}

	public void setReceiver_suda5(String receiver_suda5) {
		this.receiver_suda5 = receiver_suda5;
	}

	public String getReceiver_suda7() {
		return receiver_suda7;
	}

	public void setReceiver_suda7(String receiver_suda7) {
		this.receiver_suda7 = receiver_suda7;
	}

	public String getReceiver_mobile() {
		return receiver_mobile;
	}

	public void setReceiver_mobile(String receiver_mobile) {
		this.receiver_mobile = receiver_mobile;
	}

	public String getReceiver_phone() {
		return receiver_phone;
	}

	public void setReceiver_phone(String receiver_phone) {
		this.receiver_phone = receiver_phone;
	}

	public String getSender_name() {
		return sender_name;
	}

	public void setSender_name(String sender_name) {
		this.sender_name = sender_name;
	}

	public String getSender_address() {
		return sender_address;
	}

	public void setSender_address(String sender_address) {
		this.sender_address = sender_address;
	}

	public String getSender_suda5() {
		return sender_suda5;
	}

	public void setSender_suda5(String sender_suda5) {
		this.sender_suda5 = sender_suda5;
	}

	public String getSender_phone() {
		return sender_phone;
	}

	public void setSender_phone(String sender_phone) {
		this.sender_phone = sender_phone;
	}

	public String getProduct_price() {
		return product_price;
	}

	public void setProduct_price(String product_price) {
		this.product_price = product_price;
	}

	public String getProduct_name() {
		return product_name;
	}

	public void setProduct_name(String product_name) {
		this.product_name = product_name;
	}

	public String getEgs_comment() {
		return egs_comment;
	}

	public void setEgs_comment(String egs_comment) {
		this.egs_comment = egs_comment;
	}

	public String getPackage_size() {
		return package_size;
	}

	public void setPackage_size(String package_size) {
		this.package_size = package_size;
	}

	public String getTemperature() {
		return temperature;
	}

	public void setTemperature(String temperature) {
		this.temperature = temperature;
	}

	public String getDistance() {
		return distance;
	}

	public void setDistance(String distance) {
		this.distance = distance;
	}

	public String getDelivery_date() {
		return delivery_date;
	}

	public void setDelivery_date(String delivery_date) {
		this.delivery_date = delivery_date;
	}

	public String getDelivery_timezone() {
		return delivery_timezone;
	}

	public void setDelivery_timezone(String delivery_timezone) {
		this.delivery_timezone = delivery_timezone;
	}

	public Date getCreate_time() {
		return create_time;
	}

	public void setCreate_time(Date create_time) {
		this.create_time = create_time;
	}

	public Date getPrint_time() {
		return print_time;
	}

	public void setPrint_time(Date print_time) {
		this.print_time = print_time;
	}

	public String getAccount_id() {
		return account_id;
	}

	public void setAccount_id(String account_id) {
		this.account_id = account_id;
	}

	public String getMember_no() {
		return member_no;
	}

	public void setMember_no(String member_no) {
		this.member_no = member_no;
	}

	public String getTaxin() {
		return taxin;
	}

	public void setTaxin(String taxin) {
		this.taxin = taxin;
	}

	public String getInsurance() {
		return insurance;
	}

	public void setInsurance(String insurance) {
		this.insurance = insurance;
	}

	public String getShip_ids() {
		return ship_ids;
	}

	public void setShip_ids(String ship_ids) {
		this.ship_ids = ship_ids;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	public String getWaybill_type() {
		return waybill_type;
	}

	public void setWaybill_type(String waybill_type) {
		this.waybill_type = waybill_type;
	}

}
