package tw.com.aber.vo;

import java.io.Serializable;
import java.util.List;

public class PackageVO implements Serializable {

	private static final long serialVersionUID = 1L;

	private String package_id;
	private String group_id;
	private String c_package_id;
	private String package_name;
	private String package_spec;
	private String amount;
	private String barcode;
	private String description;
	private List<ProductPackageVO> productPackageList;

	public List<ProductPackageVO> getProductPackageList() {
		return productPackageList;
	}

	public void setProductPackageList(List<ProductPackageVO> productPackageList) {
		this.productPackageList = productPackageList;
	}

	public String getPackage_id() {
		return package_id;
	}

	public void setPackage_id(String package_id) {
		this.package_id = package_id;
	}

	public String getGroup_id() {
		return group_id;
	}

	public void setGroup_id(String group_id) {
		this.group_id = group_id;
	}

	public String getC_package_id() {
		return c_package_id;
	}

	public void setC_package_id(String c_package_id) {
		this.c_package_id = c_package_id;
	}

	public String getPackage_name() {
		return package_name;
	}

	public void setPackage_name(String package_name) {
		this.package_name = package_name;
	}

	public String getPackage_spec() {
		return package_spec;
	}

	public void setPackage_spec(String package_spec) {
		this.package_spec = package_spec;
	}

	public String getAmount() {
		return amount;
	}

	public void setAmount(String amount) {
		this.amount = amount;
	}

	public String getBarcode() {
		return barcode;
	}

	public void setBarcode(String barcode) {
		this.barcode = barcode;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

}
