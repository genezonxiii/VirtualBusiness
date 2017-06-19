package tw.com.aber.vo;

import java.io.Serializable;
import java.util.Date;

public class AcceptVO  implements Serializable {

	private static final long serialVersionUID = 1L;
	
	private String accept_id;
	private String seq_no;
	private String group_id;
	private String purchase_id;
	private String user_id;
	private String supply_id;
	private String memo;
	private Date accept_date;
	private Boolean stock_flag;
	//from purchase
	private String v_seq_no;
	
	
	public String getAccept_id() {
		return accept_id;
	}
	public void setAccept_id(String accept_id) {
		this.accept_id = accept_id;
	}
	public String getSeq_no() {
		return seq_no;
	}
	public void setSeq_no(String seq_no) {
		this.seq_no = seq_no;
	}
	public String getGroup_id() {
		return group_id;
	}
	public void setGroup_id(String group_id) {
		this.group_id = group_id;
	}
	public String getPurchase_id() {
		return purchase_id;
	}
	public void setPurchase_id(String purchase_id) {
		this.purchase_id = purchase_id;
	}
	public String getUser_id() {
		return user_id;
	}
	public void setUser_id(String user_id) {
		this.user_id = user_id;
	}
	public String getSupply_id() {
		return supply_id;
	}
	public void setSupply_id(String supply_id) {
		this.supply_id = supply_id;
	}
	public String getMemo() {
		return memo;
	}
	public void setMemo(String memo) {
		this.memo = memo;
	}
	public Date getAccept_date() {
		return accept_date;
	}
	public void setAccept_date(Date accept_date) {
		this.accept_date = accept_date;
	}
	public String getV_seq_no() {
		return v_seq_no;
	}
	public void setV_seq_no(String v_seq_no) {
		this.v_seq_no = v_seq_no;
	}
	public Boolean getStock_flag() {
		return stock_flag;
	}
	public void setStock_flag(Boolean stock_flag) {
		this.stock_flag = stock_flag;
	}
	

}
