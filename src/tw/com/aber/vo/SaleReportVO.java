package tw.com.aber.vo;

public class SaleReportVO implements java.io.Serializable {

	private static final long serialVersionUID = 1L;
	private String sale_id;
	private String seq_no;
	private String group_id;
	private String order_no;
	private String user_id;
	private String product_id;
	private String product_name;
	private String c_product_id;
	private String customer_id;
	private String name;
	private int quantity;
	private float price;
	private String invoice;
	private java.sql.Date invoice_date;
	private java.sql.Date trans_list_date;
	private java.sql.Date dis_date;
	private String memo;
	private java.sql.Date sale_date;
	private String order_source;
	private java.sql.Date return_date;
	private boolean isreturn;

	public java.sql.Date getReturn_date() {
		return return_date;
	}

	public void setReturn_date(java.sql.Date return_date) {
		this.return_date = return_date;
	}

	public boolean isIsreturn() {
		return isreturn;
	}

	public void setIsreturn(boolean isreturn) {
		this.isreturn = isreturn;
	}

	public String getSale_id() {
		return sale_id;
	}

	public void setSale_id(String sale_id) {
		this.sale_id = sale_id;
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

	public String getOrder_no() {
		return order_no;
	}

	public void setOrder_no(String order_no) {
		this.order_no = order_no;
	}

	public String getUser_id() {
		return user_id;
	}

	public void setUser_id(String user_id) {
		this.user_id = user_id;
	}

	public String getProduct_id() {
		return product_id;
	}

	public void setProduct_id(String product_id) {
		this.product_id = product_id;
	}

	public String getProduct_name() {
		return product_name;
	}

	public void setProduct_name(String product_name) {
		this.product_name = product_name;
	}

	public String getC_product_id() {
		return c_product_id;
	}

	public void setC_product_id(String c_product_id) {
		this.c_product_id = c_product_id;
	}

	public String getCustomer_id() {
		return customer_id;
	}

	public void setCustomer_id(String customer_id) {
		this.customer_id = customer_id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getQuantity() {
		return quantity;
	}

	public void setQuantity(int quantity) {
		this.quantity = quantity;
	}

	public float getPrice() {
		return price;
	}

	public void setPrice(float price) {
		this.price = price;
	}

	public String getInvoice() {
		return invoice;
	}

	public void setInvoice(String invoice) {
		this.invoice = invoice;
	}

	public java.sql.Date getInvoice_date() {
		return invoice_date;
	}

	public void setInvoice_date(java.sql.Date invoice_date) {
		this.invoice_date = invoice_date;
	}

	public java.sql.Date getTrans_list_date() {
		return trans_list_date;
	}

	public void setTrans_list_date(java.sql.Date trans_list_date) {
		this.trans_list_date = trans_list_date;
	}

	public java.sql.Date getDis_date() {
		return dis_date;
	}

	public void setDis_date(java.sql.Date dis_date) {
		this.dis_date = dis_date;
	}

	public String getMemo() {
		return memo;
	}

	public void setMemo(String memo) {
		this.memo = memo;
	}

	public java.sql.Date getSale_date() {
		return sale_date;
	}

	public void setSale_date(java.sql.Date sale_date) {
		this.sale_date = sale_date;
	}

	public String getOrder_source() {
		return order_source;
	}

	public void setOrder_source(String order_source) {
		this.order_source = order_source;
	}
}
