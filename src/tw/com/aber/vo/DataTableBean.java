package tw.com.aber.vo;

import java.io.Serializable;

public class DataTableBean implements Serializable {

	private static final long serialVersionUID = 1L;

	private String id;
	private String name;
	private String desc;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDesc() {
		return desc;
	}

	public void setDesc(String desc) {
		this.desc = desc;
	}

}