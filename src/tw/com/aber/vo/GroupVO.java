package tw.com.aber.vo;

import java.sql.Date;

public class GroupVO implements java.io.Serializable {

	private static final long serialVersionUID = 1L;

	private String group_id;
	private String group_name;
	private String group_unicode;
	private String address;
	private String phone;
	private String fax;
	private String mobile;
	private String email;
	private String master;
	private String user_id;
	private String invoice_path;
	private Date expired;
	private String invoice_posno;
	private String invoice_key;
	private String customer_id;
	private GroupSfVO sf;
	private WarehouseVO wh;

	public String getInvoice_posno() {
		return invoice_posno;
	}

	public void setInvoice_posno(String invoice_posno) {
		if (invoice_posno == null) {
			this.invoice_posno = "";
		} else {
			this.invoice_posno = invoice_posno;
		}
	}

	public String getInvoice_key() {
		return invoice_key;
	}

	public void setInvoice_key(String invoice_key) {
		this.invoice_key = invoice_key;
	}

	public String getGroup_id() {
		return group_id;
	}

	public void setGroup_id(String group_id) {
		this.group_id = group_id;
	}

	public String getGroup_name() {
		return group_name;
	}

	public void setGroup_name(String group_name) {
		this.group_name = group_name;
	}

	public String getGroup_unicode() {
		return group_unicode;
	}

	public void setGroup_unicode(String group_unicode) {
		this.group_unicode = group_unicode;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getFax() {
		return fax;
	}

	public void setFax(String fax) {
		this.fax = fax;
	}

	public String getMobile() {
		return mobile;
	}

	public void setMobile(String mobile) {
		this.mobile = mobile;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getMaster() {
		return master;
	}

	public void setMaster(String master) {
		this.master = master;
	}

	public String getUser_id() {
		return user_id;
	}

	public void setUser_id(String user_id) {
		this.user_id = user_id;
	}

	public String getInvoice_path() {
		return invoice_path;
	}

	public void setInvoice_path(String invoice_path) {
		this.invoice_path = invoice_path;
	}

	public Date getExpired() {
		return expired;
	}

	public void setExpired(Date expired) {
		this.expired = expired;
	}

	public GroupSfVO getSf() {
		return sf;
	}

	public void setSf(GroupSfVO sf) {
		this.sf = sf;
	}

	public String getCustomer_id() {
		return customer_id;
	}

	public void setCustomer_id(String customer_id) {
		if (customer_id == null) {
			this.customer_id = "";
		} else {
			this.customer_id = customer_id;
		}
	}

	public WarehouseVO getWh() {
		return wh;
	}

	public void setWh(WarehouseVO wh) {
		this.wh = wh;
	}

}
