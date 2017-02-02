package tw.com.aber.vo;

import java.io.Serializable;

public class ProductContrastVO implements Serializable {
	
	private static final long serialVersionUID = 1L;
	private String message;// for set check message
	private String contrastId;
	private String groupId;
	private String productId;
	private String productName;
	private String platformId;
	private String productNamePlatform;
	private String amount;
	
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
	public String getAmount() {
		return amount;
	}
	public void setAmount(String amount) {
		this.amount = amount;
	}
	public static long getSerialversionuid() {
		return serialVersionUID;
	}
	
}
