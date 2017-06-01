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

	// Response
	@XmlElement(name = "ItemResponse")
	private ItemResponse ItemResponse;
	@XmlElement(name = "ItemChangePushResponse")
	private ItemChangePushResponse itemChangePushResponse;
	@XmlElement(name = "BomResponse")
	private BomResponse bomResponse;
	@XmlElement(name = "VendorResponse")
	private VendorResponse vendorResponse;
	@XmlElement(name = "PurchaseOrderResponse")
	private PurchaseOrderResponse purchaseOrderResponse;
	@XmlElement(name = "CancelPurchaseOrderResponse")
	private CancelPurchaseOrderResponse cancelPurchaseOrderResponse;
	@XmlElement(name = "PurchaseOrderInboundResponse")
	private PurchaseOrderInboundResponse purchaseOrderInboundResponse;
	@XmlElement(name = "SaleOrderResponse")
	private SaleOrderResponse saleOrderResponse;
	@XmlElement(name = "CancelSaleOrderResponse")
	private CancelSaleOrderResponse cancelSaleOrderResponse;
	@XmlElement(name = "SaleOrderOutboundDetailResponse")
	private SaleOrderOutboundDetailResponse saleOrderOutboundDetailResponse;
	@XmlElement(name = "RTInventoryQueryResponse")
	private RTInventoryQueryResponse rtInventoryQueryResponse;
	@XmlElement(name = "RTInventoryQueryRequest")
	private RTInventoryQueryRequest rtInventoryQueryRequest;
	

	public RTInventoryQueryRequest getRtInventoryQueryRequest() {
		return rtInventoryQueryRequest;
	}

	public void setRtInventoryQueryRequest(RTInventoryQueryRequest rtInventoryQueryRequest) {
		this.rtInventoryQueryRequest = rtInventoryQueryRequest;
	}

	public SaleOrderOutboundDetailResponse getSaleOrderOutboundDetailResponse() {
		return saleOrderOutboundDetailResponse;
	}

	public void setSaleOrderOutboundDetailResponse(SaleOrderOutboundDetailResponse saleOrderOutboundDetailResponse) {
		this.saleOrderOutboundDetailResponse = saleOrderOutboundDetailResponse;
	}

	public CancelPurchaseOrderResponse getCancelPurchaseOrderResponse() {
		return cancelPurchaseOrderResponse;
	}

	public void setCancelPurchaseOrderResponse(CancelPurchaseOrderResponse cancelPurchaseOrderResponse) {
		this.cancelPurchaseOrderResponse = cancelPurchaseOrderResponse;
	}

	public PurchaseOrderInboundResponse getPurchaseOrderInboundResponse() {
		return purchaseOrderInboundResponse;
	}

	public void setPurchaseOrderInboundResponse(PurchaseOrderInboundResponse purchaseOrderInboundResponse) {
		this.purchaseOrderInboundResponse = purchaseOrderInboundResponse;
	}

	public SaleOrderResponse getSaleOrderResponse() {
		return saleOrderResponse;
	}

	public void setSaleOrderResponse(SaleOrderResponse saleOrderResponse) {
		this.saleOrderResponse = saleOrderResponse;
	}

	public CancelSaleOrderResponse getCancelSaleOrderResponse() {
		return cancelSaleOrderResponse;
	}

	public void setCancelSaleOrderResponse(CancelSaleOrderResponse cancelSaleOrderResponse) {
		this.cancelSaleOrderResponse = cancelSaleOrderResponse;
	}

	public PurchaseOrderResponse getPurchaseOrderResponse() {
		return purchaseOrderResponse;
	}

	public void setPurchaseOrderResponse(PurchaseOrderResponse purchaseOrderResponse) {
		this.purchaseOrderResponse = purchaseOrderResponse;
	}

	public VendorResponse getVendorResponse() {
		return vendorResponse;
	}

	public void setVendorResponse(VendorResponse vendorResponse) {
		this.vendorResponse = vendorResponse;
	}

	public BomResponse getBomResponse() {
		return bomResponse;
	}

	public void setBomResponse(BomResponse bomResponse) {
		this.bomResponse = bomResponse;
	}

	public ItemResponse getItemResponse() {
		return ItemResponse;
	}

	public void setItemResponse(ItemResponse itemResponse) {
		ItemResponse = itemResponse;
	}

	public ItemChangePushResponse getItemChangePushResponse() {
		return itemChangePushResponse;
	}

	public void setItemChangePushResponse(ItemChangePushResponse itemChangePushResponse) {
		this.itemChangePushResponse = itemChangePushResponse;
	}

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
