package tw.com.aber.vo;

import java.io.Serializable;
import java.sql.Date;

public class StockModVO implements Serializable {

	private static final long serialVersionUID = 1L;

	private String stockmod_id;
	private String group_id;
	private String stockmod_no;
	private Date stockmod_time;
	private String ref_id;
	private String stockmod_type;
	private String process_flag;
	private String memo;
	private String create_user;
	private Date create_time;
	private String process_user;
	private Date process_time;
	private String startDate;
	private String endDate;

	public String getStartDate() {
		return startDate;
	}

	public void setStartDate(String startDate) {
		this.startDate = startDate;
	}

	public String getEndDate() {
		return endDate;
	}

	public void setEndDate(String endDate) {
		this.endDate = endDate;
	}

	public String getStockmod_id() {
		return stockmod_id;
	}

	public void setStockmod_id(String stockmod_id) {
		this.stockmod_id = stockmod_id;
	}

	public String getGroup_id() {
		return group_id;
	}

	public void setGroup_id(String group_id) {
		this.group_id = group_id;
	}

	public String getStockmod_no() {
		return stockmod_no;
	}

	public void setStockmod_no(String stockmod_no) {
		this.stockmod_no = stockmod_no;
	}

	public Date getStockmod_time() {
		return stockmod_time;
	}

	public void setStockmod_time(Date stockmod_time) {
		this.stockmod_time = stockmod_time;
	}

	public String getRef_id() {
		return ref_id;
	}

	public void setRef_id(String ref_id) {
		this.ref_id = ref_id;
	}

	public String getStockmod_type() {
		return stockmod_type;
	}

	public void setStockmod_type(String stockmod_type) {
		this.stockmod_type = stockmod_type;
	}

	public String getProcess_flag() {
		return process_flag;
	}

	public void setProcess_flag(String process_flag) {
		this.process_flag = process_flag;
	}

	public String getMemo() {
		return memo;
	}

	public void setMemo(String memo) {
		this.memo = memo;
	}

	public String getCreate_user() {
		return create_user;
	}

	public void setCreate_user(String create_user) {
		this.create_user = create_user;
	}

	public Date getCreate_time() {
		return create_time;
	}

	public void setCreate_time(Date create_time) {
		this.create_time = create_time;
	}

	public String getProcess_user() {
		return process_user;
	}

	public void setProcess_user(String process_user) {
		this.process_user = process_user;
	}

	public Date getProcess_time() {
		return process_time;
	}

	public void setProcess_time(Date process_time) {
		this.process_time = process_time;
	}

}
