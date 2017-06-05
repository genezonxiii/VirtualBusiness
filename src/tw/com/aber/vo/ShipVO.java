package tw.com.aber.vo;

import java.io.Serializable;
import java.sql.Date;
import java.util.List;

public class ShipVO implements Serializable {

	private static final long serialVersionUID = 1L;

	private String ship_id;
	private String ship_seq_no;
	private String group_id;
	private String order_no;
	private String user_id;
	private String customer_id;
	private String name;
	private String memo;
	private String deliveryway;
	private Float total_amt;
	private String deliver_name;
	private String deliver_to;
	private String realsale_id;
	private Date v_sale_date; // tb_realsale
	private String v_c_product_id;// tb_realsale
	private String v_product_name;// tb_realsale
	private List<ShipDetail> shipDetail;

	public String getShip_id() {
		return ship_id;
	}

	public void setShip_id(String ship_id) {
		this.ship_id = ship_id;
	}

	public String getShip_seq_no() {
		return ship_seq_no;
	}

	public void setShip_seq_no(String ship_seq_no) {
		this.ship_seq_no = ship_seq_no;
	}

	public String getGroup_id() {
		return group_id;
	}

	public void setGroup_id(String group_id) {
		this.group_id = group_id;
	}

	public String getOrder_no() {
		return order_no;
	}

	public void setOrder_no(String order_no) {
		this.order_no = order_no;
	}

	public String getUser_id() {
		return user_id;
	}

	public void setUser_id(String user_id) {
		this.user_id = user_id;
	}

	public String getCustomer_id() {
		return customer_id;
	}

	public void setCustomer_id(String customer_id) {
		this.customer_id = customer_id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getMemo() {
		return memo;
	}

	public void setMemo(String memo) {
		this.memo = memo;
	}

	public String getDeliveryway() {
		return deliveryway;
	}

	public void setDeliveryway(String deliveryway) {
		this.deliveryway = deliveryway;
	}

	public Float getTotal_amt() {
		return total_amt;
	}

	public void setTotal_amt(Float total_amt) {
		this.total_amt = total_amt;
	}

	public String getDeliver_name() {
		return deliver_name;
	}

	public void setDeliver_name(String deliver_name) {
		this.deliver_name = deliver_name;
	}

	public String getDeliver_to() {
		return deliver_to;
	}

	public void setDeliver_to(String deliver_to) {
		this.deliver_to = deliver_to;
	}

	public Date getV_sale_date() {
		return v_sale_date;
	}

	public void setV_sale_date(Date v_sale_date) {
		this.v_sale_date = v_sale_date;
	}

	public List<ShipDetail> getShipDeatil() {
		return shipDetail;
	}

	public void setShipDeatil(List<ShipDetail> shipDeatil) {
		this.shipDetail = shipDeatil;
	}

	public String getV_c_product_id() {
		return v_c_product_id;
	}

	public void setV_c_product_id(String v_c_product_id) {
		this.v_c_product_id = v_c_product_id;
	}

	public String getV_product_name() {
		return v_product_name;
	}

	public void setV_product_name(String v_product_name) {
		this.v_product_name = v_product_name;
	}

	public List<ShipDetail> getShipDetail() {
		return shipDetail;
	}

	public void setShipDetail(List<ShipDetail> shipDetail) {
		this.shipDetail = shipDetail;
	}

	public String getRealsale_id() {
		return realsale_id;
	}

	public void setRealsale_id(String realsale_id) {
		this.realsale_id = realsale_id;
	}
	
	
}
