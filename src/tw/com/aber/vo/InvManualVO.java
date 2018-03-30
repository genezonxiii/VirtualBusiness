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
	private String invoice_reason;
	private String title;
	private String unicode;
	private String address;
	private String memo;
	private Integer amount;
	private Integer amount_plustax;
	private Integer tax;
	private Integer tax_type;
	private Integer inv_flag;

	public Integer getTax_type() {
		return tax_type;
	}

	public void setTax_type(Integer tax_type) {
		this.tax_type = tax_type;
	}

	public Integer getInv_flag() {
		return inv_flag;
	}

	public void setInv_flag(Integer inv_flag) {
		this.inv_flag = inv_flag;
	}

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

	public Integer getAmount() {
		return amount;
	}

	public void setAmount(Integer amount) {
		this.amount = amount;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
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

	public Integer getTax() {
		return tax;
	}

	public void setTax(Integer tax) {
		this.tax = tax;
	}

	public Integer getAmount_plustax() {
		return amount_plustax;
	}

	public void setAmount_plustax(Integer amount_plustax) {
		this.amount_plustax = amount_plustax;
	}

	public String getInvoice_reason() {
		return invoice_reason;
	}

	public void setInvoice_reason(String invoice_reason) {
		this.invoice_reason = invoice_reason;
	}

}
