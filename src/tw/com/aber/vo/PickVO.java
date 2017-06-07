package tw.com.aber.vo;

import java.io.Serializable;
import java.util.Date;

public class PickVO implements Serializable{
	private static final long serialVersionUID = 1L;
	private String pick_id;
	private String pick_no;
	private String group_id;
	private Date pick_time;
	private String pick_user_id;
	
	public String getPick_id() {
		return pick_id;
	}
	public void setPick_id(String pick_id) {
		this.pick_id = pick_id;
	}
	public String getPick_no() {
		return pick_no;
	}
	public void setPick_no(String pick_no) {
		this.pick_no = pick_no;
	}
	public String getGroup_id() {
		return group_id;
	}
	public void setGroup_id(String group_id) {
		this.group_id = group_id;
	}
	public Date getPick_time() {
		return pick_time;
	}
	public void setPick_time(Date pick_time) {
		this.pick_time = pick_time;
	}
	public String getPick_user_id() {
		return pick_user_id;
	}
	public void setPick_user_id(String pick_user_id) {
		this.pick_user_id = pick_user_id;
	}
}
