package tw.com.aber.vo;

import java.util.List;

public class MenuVO implements java.io.Serializable {

	private static final long serialVersionUID = 1L;

	private String id;
	private String menuName;
	private String url;
	private String seqNo;
	private String parentId;
	private String photoPath;
	private List<MenuVO> subMenu;
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getMenuName() {
		return menuName;
	}
	public void setMenuName(String menuName) {
		this.menuName = menuName;
	}
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	public String getSeqNo() {
		return seqNo;
	}
	public void setSeqNo(String seqNo) {
		this.seqNo = seqNo;
	}
	public String getParentId() {
		return parentId;
	}
	public void setParentId(String parentId) {
		this.parentId = parentId;
	}
	public String getPhotoPath() {
		return photoPath;
	}
	public void setPhotoPath(String photoPath) {
		this.photoPath = photoPath;
	}
	public List<MenuVO> getSubMenu() {
		return subMenu;
	}
	public void setSubMenu(List<MenuVO> subMenu) {
		this.subMenu = subMenu;
	}
	public static long getSerialversionuid() {
		return serialVersionUID;
	}

}
