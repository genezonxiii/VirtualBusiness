package tw.com.aber.vo;

import java.io.Serializable;
import java.util.Date;

public class StockTakeVO  implements Serializable {
	private static final long serialVersionUID = 1L;
	
	private String stocktake_id;
	private String seq_no;
	private String group_id;
	private String user_id;
	private String memo;
	private Date create_date;
	private Date end_date;
	private Boolean stocktake_flag;
	
	
	public String getStocktake_id() {
		return stocktake_id;
	}
	public void setStocktake_id(String stocktake_id) {
		this.stocktake_id = stocktake_id;
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
	public String getUser_id() {
		return user_id;
	}
	public void setUser_id(String user_id) {
		this.user_id = user_id;
	}
	public String getMemo() {
		return memo;
	}
	public void setMemo(String memo) {
		this.memo = memo;
	}
	public Date getCreate_date() {
		return create_date;
	}
	public void setCreate_date(Date create_date) {
		this.create_date = create_date;
	}
	public Date getEnd_date() {
		return end_date;
	}
	public void setEnd_date(Date end_date) {
		this.end_date = end_date;
	}
	public Boolean getStocktake_flag() {
		return stocktake_flag;
	}
	public void setStocktake_flag(Boolean stocktake_flag) {
		this.stocktake_flag = stocktake_flag;
	}

	
}
