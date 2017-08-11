package tw.com.aber.vo;

import java.io.Serializable;
import java.math.BigDecimal;

public class ProductContrastVO implements Serializable {

	private static final long serialVersionUID = 1L;
	private String message;// for set check message
	private String contrastId;
	private String groupId;
	private String productId;
	private String productName;
	private String platformId;
	private String productNamePlatform;
	private BigDecimal amount;
	private String product_spec_platform;
	private String product_spec;
	private String contrast_type;
	private String platform_name; // tb_platform
	private String c_product_id_platform;

	public String getPlatform_name() {
		return platform_name;
	}

	public void setPlatform_name(String platform_name) {
		this.platform_name = platform_name;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public String getContrastId() {
		return contrastId;
	}

	public void setContrastId(String contrastId) {
		this.contrastId = contrastId;
	}

	public String getGroupId() {
		return groupId;
	}

	public void setGroupId(String groupId) {
		this.groupId = groupId;
	}

	public String getProductId() {
		return productId;
	}

	public void setProductId(String productId) {
		this.productId = productId;
	}

	public String getProductName() {
		return productName;
	}

	public void setProductName(String productName) {
		this.productName = productName;
	}

	public String getPlatformId() {
		return platformId;
	}

	public void setPlatformId(String platformId) {
		this.platformId = platformId;
	}

	public String getProductNamePlatform() {
		return productNamePlatform;
	}

	public void setProductNamePlatform(String productNamePlatform) {
		this.productNamePlatform = productNamePlatform;
	}

	public BigDecimal getAmount() {
		return amount;
	}

	public void setAmount(BigDecimal amount) {
		this.amount = amount;
	}

	public String getProduct_spec_platform() {
		return product_spec_platform;
	}

	public void setProduct_spec_platform(String product_spec_platform) {
		this.product_spec_platform = product_spec_platform;
	}

	public String getContrast_type() {
		return contrast_type;
	}

	public void setContrast_type(String contrast_type) {
		this.contrast_type = contrast_type;
	}

	public String getProduct_spec() {
		return product_spec;
	}

	public void setProduct_spec(String product_spec) {
		this.product_spec = product_spec;
	}

	public String getC_product_id_platform() {
		return c_product_id_platform;
	}

	public void setC_product_id_platform(String c_product_id_platform) {
		this.c_product_id_platform = c_product_id_platform;
	}
	
	

}
