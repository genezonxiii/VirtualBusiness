package tw.com.aber.vo;

import java.io.Serializable;

public class ProductPackageVO implements Serializable {

	private static final long serialVersionUID = 1L;

	private String package_id;
	private String parent_id;
	private String product_id;
	private String quantity;
	private String package_desc;

	public String getPackage_id() {
		return package_id;
	}

	public void setPackage_id(String package_id) {
		this.package_id = package_id;
	}

	public String getParent_id() {
		return parent_id;
	}

	public void setParent_id(String parent_id) {
		this.parent_id = parent_id;
	}

	public String getProduct_id() {
		return product_id;
	}

	public void setProduct_id(String product_id) {
		this.product_id = product_id;
	}

	public String getQuantity() {
		return quantity;
	}

	public void setQuantity(String quantity) {
		this.quantity = quantity;
	}

	public String getPackage_desc() {
		return package_desc;
	}

	public void setPackage_desc(String package_desc) {
		this.package_desc = package_desc;
	}

}
