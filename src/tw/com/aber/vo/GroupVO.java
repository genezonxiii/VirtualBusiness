package tw.com.aber.vo;

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

}
