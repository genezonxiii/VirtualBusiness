package tw.com.aber.sftransfer.controller;

import java.io.StringReader;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.JAXB;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.google.gson.Gson;

import tw.com.aber.sf.delivery.vo.Body;
import tw.com.aber.sf.delivery.vo.Cargo;
import tw.com.aber.sf.delivery.vo.Extra;
import tw.com.aber.sf.delivery.vo.Order;
import tw.com.aber.sf.delivery.vo.OrderConfirm;
import tw.com.aber.sf.delivery.vo.OrderConfirmOption;
import tw.com.aber.sf.delivery.vo.OrderSearch;
import tw.com.aber.sf.delivery.vo.Request;
import tw.com.aber.sf.delivery.vo.Response;
import tw.com.aber.sf.delivery.vo.RouteRequest;

public class SfDeliveryApi {
	private static final Logger logger = LogManager.getLogger(SfDeliveryApi.class);

	// 下訂單(含篩選)接口響應 - 訂單處理成功
	private static final String ORDER_SERVICE_RESPONSE = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>"
			+ "<Response service=\"OrderService\">" + "<Head>OK</Head>" + "<Body>"
			+ "<OrderResponse orderId=\"TEST201706090001\" mailno=\"444003409873\" orgincode=\"SIN01D\" destcode=\"852\" filter_result=\"2\"/>"
			+ "</Body>" + "</Response>";
	// 下訂單(含篩選)接口響應 - 訂單處理失敗
	private static final String ORDER_SERVICE_ERR_RESPONSE = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>"
			+ "<Response service=\"OrderService\">" + "<Head>ERR</Head>"
			+ "<Error code=\"8016\">重複下單</Error></Response>";
	// 訂單確認/取消接口響應 - 訂單確認成功
	private static final String ORDER_CONFIRM_SERVICE_RESPONSE = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>"
			+ "<Response service=\"OrderConfirmService\">" + "<Head>OK</Head>" + "<Body>"
			+ "<OrderConfirmResponse orderId=\"TEST201706090003\" mailno=\"444003078326\" res_status=\"2\"/>"
			+ "</Body>" + "</Response>";
	// 訂單確認/取消接口響應 - 訂單確認失敗
	private static final String ORDER_CONFIRM_SERVICE_ERR_RESPONSE = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>"
			+ "<Response service=\"OrderConfirmService\">" + "<Head>ERR</Head>"
			+ "<Error code=\"4001\">系統發生數據錯誤或運行時異常</Error></Response>";
	// 訂單結果查詢接口響應 - 訂單處理成功
	private static final String ORDER_SEARCH_SERVICE_RESPONSE = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>"
			+ "<Response service=\"OrderSearchService\">" + "<Head>OK</Head>" + "<Body>"
			+ "<OrderResponse orderId=\"TEST201706090006\" mailno=\"444003078089\" orgincode=\"755\" destcode=\"010\" filter_result=\"2\"/>"
			+ "</Body>" + "</Response>";
	// 訂單結果查詢接口響應 - 訂單處理失敗
	private static final String ORDER_SEARCH_SERVICE_ERR_RESPONSE = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>"
			+ "<Response service=\"OrderSearchService\">" + "<Head>ERR</Head>"
			+ "<Error code=\"4001\">系統發生數據錯誤或運行時異常</Error></Response>";
	// 路由查詢接口響應 - 路由查詢成功
	private static final String ROUTE_SERVICE_RESPONSE = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>"
			+ "<Response service=\"RouteService\">" + "<Head>OK</Head>" + "<Body>"
			+ "<RouteResponse mailno=\"444003077898\">"
			+ "<Route accept_time =\"2017-06-09 18:09:26\" accept_address =\"深圳\" remark =\"已收件\" opcode =\"50\"/>"
			+ "<Route accept_time =\"2017-06-10 18:09:26\" remark =\"此件签单返还的单号为123638813180\" opcode =\"922\"/>"
			+ "</RouteResponse>" + "</Body>" + "</Response>";
	// 路由查詢接口響應 - 路由查詢失敗
	private static final String ROUTE_SERVICE_ERR_RESPONSE = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>"
			+ "<Response service=\"RouteService\">" + "<Head>ERR</Head>"
			+ "<Error code=\"4001\">系統發生數據錯誤或運行時異常</Error></Response>";

	public String genOrderService() {
		String result = "";
		Cargo cargo1 = new Cargo();
		cargo1.setName("LV1");
		cargo1.setCount("1");
		cargo1.setUnit("a");
		cargo1.setWeight("");
		cargo1.setAmount("");
		cargo1.setCurrency("");
		cargo1.setSource_area("");

		Cargo cargo2 = new Cargo();
		cargo2.setName("LV2");
		cargo2.setCount("2");
		cargo2.setUnit("b");
		cargo2.setWeight("");
		cargo2.setAmount("");
		cargo2.setCurrency("");
		cargo2.setSource_area("");

		List<Cargo> cargos = new ArrayList<Cargo>();

		cargos.add(cargo1);
		cargos.add(cargo2);

		Order order = new Order();
		order.setOrderId("TE20170609");
		order.setJ_company("罗湖火车站");
		order.setJ_contact("小雷");
		order.setJ_tel("13810744");
		order.setJ_mobile("1311744");
		order.setJ_province("广东省");
		order.setJ_city("深圳");
		order.setJ_county("福田区");
		order.setJ_address("罗湖火车站东区调度室");
		order.setD_company("顺丰速运");
		order.setD_contact("小邱");
		order.setD_tel("15819050");
		order.setD_mobile("15539050");
		order.setD_address("北京市海滨区中关村");
		order.setExpress_type("1");
		order.setPay_method("1");
		order.setParcel_quantity("1");
		order.setCargo_height("33");
		order.setCargo_width("33");
		order.setCargo_height("33");
		order.setRemark("");
		order.setCargos(cargos);

		Extra extra = new Extra();
		extra.setE1("e1");
		extra.setE2("e2");

		Body body = new Body();
		body.setOrder(order);
		body.setExtra(extra);

		Request request = new Request();
		request.setService("OrderService");
		request.setLang("zh-CN");
		request.setHead("BSPdevelop");
		request.setBody(body);

		StringWriter sw = new StringWriter();
		JAXB.marshal(request, sw);
		logger.debug("--- start: output of marshalling ----");
		logger.debug(sw.toString());
		result = sw.toString();
		logger.debug("--- end: output of marshalling ----");

		return result;
	}

	public String genOrderConfirmService() {
		String result = "";
		OrderConfirmOption option = new OrderConfirmOption();
		option.setWeight("3.56");
		option.setVolume("33,33,33");

		OrderConfirm confirm = new OrderConfirm();
		confirm.setOrderId("TS201706090002");
		confirm.setMailno("444003078326");
		confirm.setDealtype("1");
		confirm.setOrderConfirmOption(option);

		Extra extra = new Extra();
		extra.setE1("e1");
		extra.setE2("e2");

		Body body = new Body();
		body.setOrderConfirm(confirm);
		body.setExtra(extra);

		Request request = new Request();
		request.setService("OrderConfirmService");
		request.setLang("zh-CN");
		request.setHead("BSPdevelop");
		request.setBody(body);

		StringWriter sw = new StringWriter();
		JAXB.marshal(request, sw);
		logger.debug("--- start: output of marshalling ----");
		logger.debug(sw.toString());
		result = sw.toString();
		logger.debug("--- end: output of marshalling ----");

		return result;
	}

	public String genOrderSearchService() {
		String result = "";
		OrderSearch orderSearch = new OrderSearch();
		orderSearch.setOrderId("TS201706090009");
		Body body = new Body();
		body.setOrderSearch(orderSearch);

		Request request = new Request();
		request.setService("OrderSearchService");
		request.setLang("zh-CN");
		request.setHead("BSPdevelop");
		request.setBody(body);

		StringWriter sw = new StringWriter();
		JAXB.marshal(request, sw);
		logger.debug("--- start: output of marshalling ----");
		logger.debug(sw.toString());
		result = sw.toString();
		logger.debug("--- end: output of marshalling ----");

		return result;
	}

	public String genRouteService() {
		String result = "";
		RouteRequest routeRequest = new RouteRequest();
		routeRequest.setTracking_type("1");
		routeRequest.setMethod_type("1");
		routeRequest.setTracking_number("444003077898");
		Body body = new Body();
		body.setRouteRequest(routeRequest);

		Request request = new Request();
		request.setService("RouteService");
		request.setLang("zh-CN");
		request.setHead("BSPdevelop");
		request.setBody(body);

		StringWriter sw = new StringWriter();
		JAXB.marshal(request, sw);
		logger.debug("--- start: output of marshalling ----");
		logger.debug(sw.toString());
		result = sw.toString();
		logger.debug("--- end: output of marshalling ----");

		return result;
	}

	public Response getResponseObj(String resXml) {
		Response response = null;
		try {
			response = JAXB.unmarshal(new StringReader(resXml), Response.class);
			String jsonStr = new Gson().toJson(response);
			logger.debug("\n\nJson格式:\n\n{}\n", jsonStr);
			StringWriter sw = new StringWriter();
			JAXB.marshal(response, sw);
			logger.debug("\n\nXML格式:\n\n{}\n", sw.toString());
		} catch (Exception e) {
			logger.debug("\n\ngetResponseObj err:{}\n", e.getMessage());
		}
		return response;
	}

	public static void main(String[] args) {
		SfDeliveryApi api = new SfDeliveryApi();

		// api.genOrderService();
		// api.getResponseObj(ORDER_SERVICE_RESPONSE);
		// api.getResponseObj(ORDER_SERVICE_ERR_RESPONSE);

		// api.genOrderConfirmService();
		// api.getResponseObj(ORDER_CONFIRM_SERVICE_RESPONSE);
		// api.getResponseObj(ORDER_CONFIRM_SERVICE_ERR_RESPONSE);

		// api.genOrderSearchService();
		// api.getResponseObj(ORDER_SEARCH_SERVICE_RESPONSE);
		// api.getResponseObj(ORDER_SEARCH_SERVICE_ERR_RESPONSE);

		api.genRouteService();
		api.getResponseObj(ROUTE_SERVICE_RESPONSE);
		api.getResponseObj(ROUTE_SERVICE_ERR_RESPONSE);

	}
}
