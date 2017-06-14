package tw.com.aber.vo;

import java.io.Serializable;

public class SFDeliveryVO implements Serializable {
	private static final long serialVersionUID = 1L;
	private String group_id;
	private String access_code;
	private String check_word;
	private String contact;
	private String tel;
	private String mobile;

	public String getGroup_id() {
		return group_id;
	}

	public void setGroup_id(String group_id) {
		this.group_id = group_id;
	}

	public String getAccess_code() {
		return access_code;
	}

	public void setAccess_code(String access_code) {
		this.access_code = access_code;
	}

	public String getCheck_word() {
		return check_word;
	}

	public void setCheck_word(String check_word) {
		this.check_word = check_word;
	}

	public String getContact() {
		return contact;
	}

	public void setContact(String contact) {
		this.contact = contact;
	}

	public String getTel() {
		return tel;
	}

	public void setTel(String tel) {
		this.tel = tel;
	}

	public String getMobile() {
		return mobile;
	}

	public void setMobile(String mobile) {
		this.mobile = mobile;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

}
