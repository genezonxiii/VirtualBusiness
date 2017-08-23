package tw.com.aber.inv.vo;

import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

@XmlRootElement(name = "Details")
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(propOrder = { "productItem" })
public class Details {
	private static final long serialVersionUID = 1L;

	/*
	 * A0401
	 */
	@XmlElement(name = "ProductItem")
	private List<ProductItem> productItem;

	public List<ProductItem> getProductItem() {
		return productItem;
	}

	public void setProductItem(List<ProductItem> productItem) {
		this.productItem = productItem;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

}
