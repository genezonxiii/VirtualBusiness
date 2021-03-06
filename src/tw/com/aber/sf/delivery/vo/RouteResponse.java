package tw.com.aber.sf.delivery.vo;

import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

@XmlRootElement(name = "RouteResponse")
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(propOrder = { "routes" })
public class RouteResponse {
	private static final long serialVersionUID = 1L;
	@XmlAttribute(name = "mailno")
	private String mailno;

	@XmlAttribute(name = "orderid")
	private String orderid;

	@XmlAttribute(name = "accept_time")
	private String accept_time;

	@XmlAttribute(name = "accept_address")
	private String accept_address;

	@XmlAttribute(name = "remark")
	private String remark;

	@XmlElement(name = "Route")
	private List<Route> routes;

	public String getOrderid() {
		return orderid;
	}

	public void setOrderid(String orderid) {
		this.orderid = orderid;
	}

	public String getMailno() {
		return mailno;
	}

	public void setMailno(String mailno) {
		this.mailno = mailno;
	}

	public String getAccept_time() {
		return accept_time;
	}

	public void setAccept_time(String accept_time) {
		this.accept_time = accept_time;
	}

	public String getAccept_address() {
		return accept_address;
	}

	public void setAccept_address(String accept_address) {
		this.accept_address = accept_address;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public List<Route> getRoutes() {
		return routes;
	}

	public void setRoutes(List<Route> routes) {
		this.routes = routes;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

}
