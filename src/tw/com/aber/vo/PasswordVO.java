package tw.com.aber.vo;

public class PasswordVO implements java.io.Serializable {

	private static final long serialVersionUID = 1L;

	private String user_id;
	private String passwordOld;
	private String passwordNew;

	public String getUser_id() {
		return user_id;
	}

	public void setUser_id(String user_id) {
		this.user_id = user_id;
	}

	public String getPasswordOld() {
		return passwordOld;
	}

	public void setPasswordOld(String passwordOld) {
		this.passwordOld = passwordOld;
	}

	public String getPasswordNew() {
		return passwordNew;
	}

	public void setPasswordNew(String passwordNew) {
		this.passwordNew = passwordNew;
	}
}
