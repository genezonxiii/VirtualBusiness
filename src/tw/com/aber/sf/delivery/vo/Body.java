package tw.com.aber.sf.delivery.vo;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

@XmlRootElement(name = "Body")
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(propOrder = { "order", "orderConfirm", "orderResponse", "orderConfirmResponse", "orderSearch", "routeRequest",
		"routeResponse", "extra" })
public class Body {
	private static final long serialVersionUID = 1L;

	@XmlElement(name = "Order")
	private Order order;
	@XmlElement(name = "Extra")
	private Extra extra;
	@XmlElement(name = "OrderConfirm")
	private OrderConfirm orderConfirm;
	@XmlElement(name = "OrderResponse")
	private OrderResponse orderResponse;
	@XmlElement(name = "OrderConfirmResponse")
	private OrderConfirmResponse orderConfirmResponse;
	@XmlElement(name = "OrderSearch")
	private OrderSearch orderSearch;
	@XmlElement(name = "RouteRequest")
	private RouteRequest routeRequest;
	@XmlElement(name = "RouteResponse")
	public RouteResponse routeResponse;

	public RouteResponse getRouteResponse() {
		return routeResponse;
	}

	public void setRouteResponse(RouteResponse routeResponse) {
		this.routeResponse = routeResponse;
	}

	public RouteRequest getRouteRequest() {
		return routeRequest;
	}

	public void setRouteRequest(RouteRequest routeRequest) {
		this.routeRequest = routeRequest;
	}

	public OrderSearch getOrderSearch() {
		return orderSearch;
	}

	public void setOrderSearch(OrderSearch orderSearch) {
		this.orderSearch = orderSearch;
	}

	public OrderConfirm getOrderConfirm() {
		return orderConfirm;
	}

	public void setOrderConfirm(OrderConfirm orderConfirm) {
		this.orderConfirm = orderConfirm;
	}

	public OrderConfirmResponse getOrderConfirmResponse() {
		return orderConfirmResponse;
	}

	public void setOrderConfirmResponse(OrderConfirmResponse orderConfirmResponse) {
		this.orderConfirmResponse = orderConfirmResponse;
	}

	public OrderResponse getOrderResponse() {
		return orderResponse;
	}

	public void setOrderResponse(OrderResponse orderResponse) {
		this.orderResponse = orderResponse;
	}

	public Order getOrder() {
		return order;
	}

	public void setOrder(Order order) {
		this.order = order;
	}

	public Extra getExtra() {
		return extra;
	}

	public void setExtra(Extra extra) {
		this.extra = extra;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

}
