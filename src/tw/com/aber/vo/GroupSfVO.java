package tw.com.aber.vo;

import java.io.Serializable;

public class GroupSfVO implements Serializable{
	
	private static final long serialVersionUID = 1L;
	private String group_id;
	private String access_code;
	private String check_word;
	private String company_code;
	private String monthly_account;
	private String vendor_code;
	private String env;
	
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
	public String getCompany_code() {
		return company_code;
	}
	public void setCompany_code(String company_code) {
		this.company_code = company_code;
	}
	public String getMonthly_account() {
		return monthly_account;
	}
	public void setMonthly_account(String monthly_account) {
		this.monthly_account = monthly_account;
	}
	public String getVendor_code() {
		return vendor_code;
	}
	public void setVendor_code(String vendor_code) {
		this.vendor_code = vendor_code;
	}
	public String getEnv() {
		return env;
	}
	public void setEnv(String env) {
		if (env == null) {
			this.env = "";
		} else {
			this.env = env;
		}
	}

}
