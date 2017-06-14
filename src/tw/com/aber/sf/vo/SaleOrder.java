package tw.com.aber.sf.vo;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

@XmlRootElement(name = "SaleOrders")
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(propOrder = { "warehouseCode", "sfOrderType", "erpOrderType", "erpOrder", 
		"tradeOrderDateTime", "shopName", "tradeOrder", "fromFlag", "orgErpOrder", 
		"orgTradeOrder", "orderCarrier", "orderReceiverInfo", "orderSenderInfo", 
		"orderItems", "shipmentId","result", "note", "header", "items", 
		"containers", "steps" })

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
	@XmlElement(name = "TradeOrderDateTime")
	private String tradeOrderDateTime;
	@XmlElement(name = "ShopName")
	private String shopName;
	@XmlElement(name = "TradeOrder")
	private String tradeOrder;
	@XmlElement(name = "FromFlag")
	private String fromFlag;
	@XmlElement(name = "OrgErpOrder")
	private String orgErpOrder;
	@XmlElement(name = "OrgTradeOrder")
	private String orgTradeOrder;
	@XmlElement(name = "OrderCarrier")
	private OrderCarrier orderCarrier;
	@XmlElement(name = "OrderReceiverInfo")
	private OrderReceiverInfo orderReceiverInfo;
	@XmlElement(name = "OrderSenderInfo")
	private OrderSenderInfo orderSenderInfo;
	@XmlElement(name = "OrderItems")
    private OrderItems orderItems;

	@XmlElement(name = "ShipmentId")
	private String shipmentId;
	@XmlElement(name = "Result")
	private String result;
	@XmlElement(name = "Note")
	private String note;

	@XmlElement(name = "Header")
	private Header header;
	@XmlElement(name = "Items")
	private Items items;
	@XmlElement(name = "Containers")
	private Containers containers;

	@XmlElement(name = "Steps")
	private Steps steps;

	public Steps getSteps() {
		return steps;
	}

	public void setSteps(Steps steps) {
		this.steps = steps;
	}

	public Header getHeader() {
		return header;
	}

	public void setHeader(Header header) {
		this.header = header;
	}

	public Items getItems() {
		return items;
	}

	public void setItems(Items items) {
		this.items = items;
	}

	public Containers getContainers() {
		return containers;
	}

	public void setContainers(Containers containers) {
		this.containers = containers;
	}

	public String getShipmentId() {
		return shipmentId;
	}

	public void setShipmentId(String shipmentId) {
		this.shipmentId = shipmentId;
	}

	public String getResult() {
		return result;
	}

	public void setResult(String result) {
		this.result = result;
	}

	public String getNote() {
		return note;
	}

	public void setNote(String note) {
		this.note = note;
	}

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
	public OrderItems getOrderItems() {
		return orderItems;
	}
	
	public void setOrderItems(OrderItems orderItems) {
		this.orderItems = orderItems;
	}

	public OrderCarrier getOrderCarrier() {
		return orderCarrier;
	}

	public void setOrderCarrier(OrderCarrier orderCarrier) {
		this.orderCarrier = orderCarrier;
	}

	public String getTradeOrder() {
		return tradeOrder;
	}

	public void setTradeOrder(String tradeOrder) {
		this.tradeOrder = tradeOrder;
	}

	public String getFromFlag() {
		return fromFlag;
	}

	public void setFromFlag(String fromFlag) {
		this.fromFlag = fromFlag;
	}

	public String getOrgErpOrder() {
		return orgErpOrder;
	}

	public void setOrgErpOrder(String orgErpOrder) {
		this.orgErpOrder = orgErpOrder;
	}

	public String getOrgTradeOrder() {
		return orgTradeOrder;
	}

	public void setOrgTradeOrder(String orgTradeOrder) {
		this.orgTradeOrder = orgTradeOrder;
	}

	public OrderSenderInfo getOrderSenderInfo() {
		return orderSenderInfo;
	}

	public void setOrderSenderInfo(OrderSenderInfo orderSenderInfo) {
		this.orderSenderInfo = orderSenderInfo;
	}

	public String getTradeOrderDateTime() {
		return tradeOrderDateTime;
	}

	public void setTradeOrderDateTime(String tradeOrderDateTime) {
		this.tradeOrderDateTime = tradeOrderDateTime;
	}

	public String getShopName() {
		return shopName;
	}

	public void setShopName(String shopName) {
		this.shopName = shopName;
	}

}
