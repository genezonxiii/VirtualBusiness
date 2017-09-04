package tw.com.aber.vo;

import java.io.Serializable;
import java.sql.Date;

public class InvBuyerVO implements Serializable {

	private static final long serialVersionUID = 1L;
	private String inv_buyer_id;
	private String group_id;
	private String title;
	private String unicode;
	private String address;
	private String memo;
	private Date create_time;

	public String getInv_buyer_id() {
		return inv_buyer_id;
	}

	public void setInv_buyer_id(String inv_buyer_id) {
		this.inv_buyer_id = inv_buyer_id;
	}

	public String getGroup_id() {
		return group_id;
	}

	public void setGroup_id(String group_id) {
		this.group_id = group_id;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getUnicode() {
		return unicode;
	}

	public void setUnicode(String unicode) {
		this.unicode = unicode;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getMemo() {
		return memo;
	}

	public void setMemo(String memo) {
		this.memo = memo;
	}

	public Date getCreate_time() {
		return create_time;
	}

	public void setCreate_time(Date create_time) {
		this.create_time = create_time;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

}
