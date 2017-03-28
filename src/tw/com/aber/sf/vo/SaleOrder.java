package tw.com.aber.sf.vo;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name="SaleOrders")
@XmlAccessorType(XmlAccessType.FIELD)
public class SaleOrder {
	private static final long serialVersionUID = 1L;
	
	@XmlElement(name = "WarehouseCode")
    private String warehouseCode;
	@XmlElement(name = "SfOrderType")
    private String sfOrderType;
	@XmlElement(name = "ErpOrderType")
    private String erpOrderType;
	@XmlElement(name = "ErpOrder")
    private String erpOrder;
	@XmlElement(name = "OrderReceiverInfo")
    private OrderReceiverInfo orderReceiverInfo;
	
	public String getWarehouseCode() {
		return warehouseCode;
	}
	public void setWarehouseCode(String warehouseCode) {
		this.warehouseCode = warehouseCode;
	}
	public String getSfOrderType() {
		return sfOrderType;
	}
	public void setSfOrderType(String sfOrderType) {
		this.sfOrderType = sfOrderType;
	}
	public String getErpOrderType() {
		return erpOrderType;
	}
	public void setErpOrderType(String erpOrderType) {
		this.erpOrderType = erpOrderType;
	}
	public String getErpOrder() {
		return erpOrder;
	}
	public void setErpOrder(String erpOrder) {
		this.erpOrder = erpOrder;
	}
	public OrderReceiverInfo getOrderReceiverInfo() {
		return orderReceiverInfo;
	}
	public void setOrderReceiverInfo(OrderReceiverInfo orderReceiverInfo) {
		this.orderReceiverInfo = orderReceiverInfo;
	}
	public static long getSerialversionuid() {
		return serialVersionUID;
	}
	    
}
