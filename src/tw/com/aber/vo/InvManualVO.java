package tw.com.aber.vo;

import java.io.Serializable;
import java.sql.Date;

public class InvManualVO implements Serializable {

	private static final long serialVersionUID = 1L;

	private String inv_manual_id;
	private String group_id;
	private String invoice_type;
	private String year_month;
	private String invoice_no;
	private Date invoice_date;
	private String title;
	private String unicode;
	private Float amount;

	public String getInv_manual_id() {
		return inv_manual_id;
	}

	public void setInv_manual_id(String inv_manual_id) {
		this.inv_manual_id = inv_manual_id;
	}

	public String getGroup_id() {
		return group_id;
	}

	public void setGroup_id(String group_id) {
		this.group_id = group_id;
	}

	public String getInvoice_type() {
		return invoice_type;
	}

	public void setInvoice_type(String invoice_type) {
		this.invoice_type = invoice_type;
	}

	public String getYear_month() {
		return year_month;
	}

	public void setYear_month(String year_month) {
		this.year_month = year_month;
	}

	public String getInvoice_no() {
		return invoice_no;
	}

	public void setInvoice_no(String invoice_no) {
		this.invoice_no = invoice_no;
	}

	public Date getInvoice_date() {
		return invoice_date;
	}

	public void setInvoice_date(Date invoice_date) {
		this.invoice_date = invoice_date;
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

	public Float getAmount() {
		return amount;
	}

	public void setAmount(Float amount) {
		this.amount = amount;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

}
