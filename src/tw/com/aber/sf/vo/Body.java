package tw.com.aber.sf.vo;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "Body")
@XmlAccessorType(XmlAccessType.FIELD)
public class Body {
	private static final long serialVersionUID = 1L;

	@XmlElement(name = "ItemRequest")
	private ItemRequest itemRequest;
	@XmlElement(name = "ItemQueryRequest")
	private ItemQueryRequest itemQueryRequest;
	@XmlElement(name = "PurchaseOrderRequest")
	private PurchaseOrderRequest purchaseOrderRequest;
	@XmlElement(name = "PurchaseOrderInboundRequest")
	private PurchaseOrderInboundRequest purchaseOrderInboundRequest;
	@XmlElement(name = "CancelPurchaseOrderRequest")
	private CancelPurchaseOrderRequest cancelPurchaseOrderRequest;
	@XmlElement(name = "SaleOrderRequest")
	private SaleOrderRequest saleOrderRequest;
	@XmlElement(name = "SaleOrderStatusRequest")
	private SaleOrderStatusRequest saleOrderStatusRequest;
	@XmlElement(name = "SaleOrderOutboundDetailRequest")
	private SaleOrderOutboundDetailRequest saleOrderOutboundDetailRequest;
	@XmlElement(name = "CancelSaleOrderRequest")
	private CancelSaleOrderRequest cancelSaleOrderRequest;
	@XmlElement(name = "BomRequest")
	private BomRequest bomRequest;

	public BomRequest getBomRequest() {
		return bomRequest;
	}

	public void setBomRequest(BomRequest bomRequest) {
		this.bomRequest = bomRequest;
	}

	public ItemRequest getItemRequest() {
		return itemRequest;
	}

	public void setItemRequest(ItemRequest itemRequest) {
		this.itemRequest = itemRequest;
	}

	public ItemQueryRequest getItemQueryRequest() {
		return itemQueryRequest;
	}

	public void setItemQueryRequest(ItemQueryRequest itemQueryRequest) {
		this.itemQueryRequest = itemQueryRequest;
	}

	public PurchaseOrderRequest getPurchaseOrderRequest() {
		return purchaseOrderRequest;
	}

	public void setPurchaseOrderRequest(PurchaseOrderRequest purchaseOrderRequest) {
		this.purchaseOrderRequest = purchaseOrderRequest;
	}

	public PurchaseOrderInboundRequest getPurchaseOrderInboundRequest() {
		return purchaseOrderInboundRequest;
	}

	public void setPurchaseOrderInboundRequest(PurchaseOrderInboundRequest purchaseOrderInboundRequest) {
		this.purchaseOrderInboundRequest = purchaseOrderInboundRequest;
	}

	public CancelPurchaseOrderRequest getCancelPurchaseOrderRequest() {
		return cancelPurchaseOrderRequest;
	}

	public void setCancelPurchaseOrderRequest(CancelPurchaseOrderRequest cancelPurchaseOrderRequest) {
		this.cancelPurchaseOrderRequest = cancelPurchaseOrderRequest;
	}

	public SaleOrderRequest getSaleOrderRequest() {
		return saleOrderRequest;
	}

	public void setSaleOrderRequest(SaleOrderRequest saleOrderRequest) {
		this.saleOrderRequest = saleOrderRequest;
	}

	public SaleOrderStatusRequest getSaleOrderStatusRequest() {
		return saleOrderStatusRequest;
	}

	public void setSaleOrderStatusRequest(SaleOrderStatusRequest saleOrderStatusRequest) {
		this.saleOrderStatusRequest = saleOrderStatusRequest;
	}

	public SaleOrderOutboundDetailRequest getSaleOrderOutboundDetailRequest() {
		return saleOrderOutboundDetailRequest;
	}

	public void setSaleOrderOutboundDetailRequest(SaleOrderOutboundDetailRequest saleOrderOutboundDetailRequest) {
		this.saleOrderOutboundDetailRequest = saleOrderOutboundDetailRequest;
	}

	public CancelSaleOrderRequest getCancelSaleOrderRequest() {
		return cancelSaleOrderRequest;
	}

	public void setCancelSaleOrderRequest(CancelSaleOrderRequest cancelSaleOrderRequest) {
		this.cancelSaleOrderRequest = cancelSaleOrderRequest;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

}
