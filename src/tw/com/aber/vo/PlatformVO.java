package tw.com.aber.vo;

import java.io.Serializable;

public class PlatformVO implements Serializable {

	private static final long serialVersionUID = 1L;

	private String platform_name;
	private String platform_id;

	public String getPlatform_id() {
		return platform_id;
	}

	public void setPlatform_id(String platform_id) {
		this.platform_id = platform_id;
	}

	public String getPlatform_name() {
		return platform_name;
	}

	public void setPlatform_name(String platform_name) {
		this.platform_name = platform_name;
	}

}
