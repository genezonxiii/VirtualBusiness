package tw.com.aber.vo;

public class ProductVO implements java.io.Serializable {

	private static final long serialVersionUID = 1L;
	
	private String product_id;
	private String product_name;
	private String c_product_id;
	private String price;
	private String cost;
	private String supply_name;

	public String getSupply_name() {
		return supply_name;
	}

	public void setSupply_name(String supply_name) {
		this.supply_name = supply_name;
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

	public String getPrice() {
		return price;
	}

	public void setPrice(String price) {
		this.price = price;
	}

	public String getCost() {
		return cost;
	}

	public void setCost(String cost) {
		this.cost = cost;
	}

}