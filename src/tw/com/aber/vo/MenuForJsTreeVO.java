package tw.com.aber.vo;

import java.util.List;

public class MenuForJsTreeVO {
	private String id;
	private String parent;
	private String text;
	private String icon;
	private StateVO state;
	private List<String> li_attr;
	private List<String> a_attr;

	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getText() {
		return text;
	}
	public void setText(String text) {
		this.text = text;
	}
	public String getIcon() {
		return icon;
	}
	public void setIcon(String icon) {
		this.icon = icon;
	}
	public StateVO getState() {
		return state;
	}
	public void setState(StateVO state) {
		this.state = state;
	}
	public List<String> getLi_attr() {
		return li_attr;
	}
	public void setLi_attr(List<String> li_attr) {
		this.li_attr = li_attr;
	}
	public List<String> getA_attr() {
		return a_attr;
	}
	public void setA_attr(List<String> a_attr) {
		this.a_attr = a_attr;
	}
	public String getParent() {
		return parent;
	}
	public void setParent(String parent) {
		this.parent = parent;
	}


}
