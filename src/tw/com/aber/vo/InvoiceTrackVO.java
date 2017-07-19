package tw.com.aber.vo;

public class InvoiceTrackVO implements java.io.Serializable  {
	private static final long serialVersionUID = 1L;
	
	private String invoice_id;	
	private String group_id;
	private String invoice_type;	
	private String year_month;	
	private String invoice_track;	
	private String invoice_beginno;	
	private String invoice_endno;	
	private String seq;	
	private String invoiceNum;
	
	
	public String getInvoice_id() {
		return invoice_id;
	}
	public void setInvoice_id(String invoice_id) {
		this.invoice_id = invoice_id;
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
	public String getInvoice_track() {
		return invoice_track;
	}
	public void setInvoice_track(String invoice_track) {
		this.invoice_track = invoice_track;
	}
	public String getInvoice_beginno() {
		return invoice_beginno;
	}
	public void setInvoice_beginno(String invoice_beginno) {
		this.invoice_beginno = invoice_beginno;
	}
	public String getInvoice_endno() {
		return invoice_endno;
	}
	public void setInvoice_endno(String invoice_endno) {
		this.invoice_endno = invoice_endno;
	}
	public String getSeq() {
		return seq;
	}
	public void setSeq(String seq) {
		this.seq = seq;
	}
	public String getInvoiceNum() {
		return invoiceNum;
	}
	public void setInvoiceNum(String invoiceNum) {
		this.invoiceNum = invoiceNum;
	}

}
