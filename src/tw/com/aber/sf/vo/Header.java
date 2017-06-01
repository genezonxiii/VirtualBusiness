package tw.com.aber.sf.vo;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

@XmlRootElement(name = "Header")
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(propOrder = { "warehouseCode", "erpOrder", "receiptId", "erpOrderType", "closeDate", "status" })
public class Header {
	private static final long serialVersionUID = 1L;
	@XmlElement(name = "WarehouseCode")
	private String warehouseCode;
	@XmlElement(name = "ErpOrder")
	private String erpOrder;
	@XmlElement(name = "ReceiptId")
	private String receiptId;
	@XmlElement(name = "ErpOrderType")
	private String erpOrderType;
	@XmlElement(name = "CloseDate")
	private String closeDate;
	@XmlElement(name = "Status")
	private String status;

	public String getWarehouseCode() {
		return warehouseCode;
	}

	public void setWarehouseCode(String warehouseCode) {
		this.warehouseCode = warehouseCode;
	}

	public String getErpOrder() {
		return erpOrder;
	}

	public void setErpOrder(String erpOrder) {
		this.erpOrder = erpOrder;
	}

	public String getReceiptId() {
		return receiptId;
	}

	public void setReceiptId(String receiptId) {
		this.receiptId = receiptId;
	}

	public String getErpOrderType() {
		return erpOrderType;
	}

	public void setErpOrderType(String erpOrderType) {
		this.erpOrderType = erpOrderType;
	}

	public String getCloseDate() {
		return closeDate;
	}

	public void setCloseDate(String closeDate) {
		this.closeDate = closeDate;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

}
