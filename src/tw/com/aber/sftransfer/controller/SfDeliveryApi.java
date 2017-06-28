package tw.com.aber.sftransfer.controller;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.io.StringWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.UnknownHostException;
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
	public static final String ORDER_SERVICE_RESPONSE = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>"
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

	public String genOrderService(Order order) {
		String result = "";
		Body body = new Body();
		body.setOrder(order);

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

	public String genOrderConfirmService(String orderNo) {
		String result = "";

		OrderConfirm orderConfirm = new OrderConfirm();
		orderConfirm.setOrderId(orderNo);

		Body body = new Body();
		body.setOrderConfirm(orderConfirm);

		Request request = new Request();
		request.setHead("BSPdevelop");
		request.setService("OrderConfirmService");
		request.setBody(body);

		StringWriter sw = new StringWriter();
		JAXB.marshal(request, sw);
		logger.debug("--- start: output of marshalling ----");
		logger.debug(sw.toString());
		result = sw.toString();
		logger.debug("--- end: output of marshalling ----");

		return result;
	}

	public String genOrderSearchService(String orderNo) {
		String result = "";

		OrderConfirm orderConfirm = new OrderConfirm();
		orderConfirm.setOrderId(orderNo);

		Body body = new Body();
		body.setOrderConfirm(orderConfirm);

		Request request = new Request();
		request.setHead("BSPdevelop");
		request.setService("OrderSearchService");
		request.setBody(body);

		StringWriter sw = new StringWriter();
		JAXB.marshal(request, sw);
		logger.debug("--- start: output of marshalling ----");
		logger.debug(sw.toString());
		result = sw.toString();
		logger.debug("--- end: output of marshalling ----");

		return result;
	}

	// 測試用
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

	// 測試用
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

	// 測試用
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

	// 測試用
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
			logger.debug(jsonStr);
		} catch (Exception e) {
			logger.debug(e.getMessage());
		}
		return response;
	}

	public boolean isTelegraph(Response response) {
		boolean result = false;

		try {
			if (response != null && response.getHead() != null) {
				result = "OK".equals(response.getHead()) ? true : false;
			}
		} catch (Exception e) {
			logger.debug("isTelegraph: {}", e.getMessage());
			result = false;
		}
		return result;
	}

	public String sendXML(String reqXml) {
		String targetURL = "http://bspoisp.sit.sf-express.com:11080/bsp-oisp/sfexpressService";
		String urlParameters = "";

		SfDeliveryApi api = new SfDeliveryApi();

		String logisticsInterface = reqXml;
		String dataDigest = reqXml + "123456";

		// Md5Base64 enMd5Base64 = new Md5Base64();
		dataDigest = Md5Base64.encode(dataDigest);
		logger.debug("md5 + Base64:" + dataDigest);
		dataDigest = Md5Base64.urlEncode(dataDigest);
		logger.debug("md5 + Base64 > urlEncode:" + dataDigest);

		logisticsInterface = Md5Base64.urlEncode(logisticsInterface);
		logger.debug("logisticsInterface:" + logisticsInterface);

		urlParameters = "logistics_interface=" + logisticsInterface + "&data_digest=" + dataDigest;

		String returnValue = api.executePost(targetURL, urlParameters);
		logger.debug("returnValue:" + returnValue);
		return returnValue;
	}

	public String executePost(String targetURL, String urlParameters) {
		HttpURLConnection connection = null;

		try {
			// Create connection
			URL url = new URL(targetURL);
			connection = (HttpURLConnection) url.openConnection();
			connection.setRequestMethod("POST");
			connection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded;charset=UTF-8");

			connection.setRequestProperty("Content-Length", Integer.toString(urlParameters.getBytes().length));
			connection.setRequestProperty("Content-Language", "zh-TW");

			connection.setUseCaches(false);
			connection.setDoOutput(true);

			// Send request
			DataOutputStream wr = new DataOutputStream(connection.getOutputStream());
			wr.writeBytes(urlParameters);
			wr.close();

			// Get Response
			InputStream is = connection.getInputStream();
			BufferedReader rd = new BufferedReader(new InputStreamReader(is, "utf-8"));
			StringBuilder response = new StringBuilder(); // or StringBuffer if
															// Java version 5+
			String line;
			while ((line = rd.readLine()) != null) {
				response.append(line);
				response.append('\r');
			}
			rd.close();
			return response.toString();
		} catch (UnknownHostException e) {
			logger.error("發送失敗：" + e.getMessage());
			return "<Response><Head>ERR</Head><ERROR>電文傳送失敗:" + e.getMessage() + "</ERROR></Response>";
		} catch (Exception e) {
			logger.error("發送失敗：" + e.getMessage());
			return "<Response><Head>ERR</Head><ERROR>電文傳送失敗:" + e.getMessage() + "</ERROR></Response>";
		}finally {
			if (connection != null) {
				connection.disconnect();
			}
		}
	}

	public static void main(String[] args) {
		SfDeliveryApi api = new SfDeliveryApi();

		String reqXml = api.genOrderService();
		String resXml = api.sendXML(reqXml);
		api.getResponseObj(resXml);
		// api.getResponseObj(ORDER_SERVICE_RESPONSE);
		// api.getResponseObj(ORDER_SERVICE_ERR_RESPONSE);

		// api.genOrderConfirmService();
		// api.getResponseObj(ORDER_CONFIRM_SERVICE_RESPONSE);
		// api.getResponseObj(ORDER_CONFIRM_SERVICE_ERR_RESPONSE);

		// api.genOrderSearchService();
		// api.getResponseObj(ORDER_SEARCH_SERVICE_RESPONSE);
		// api.getResponseObj(ORDER_SEARCH_SERVICE_ERR_RESPONSE);

		// api.genRouteService();
		// api.getResponseObj(ROUTE_SERVICE_RESPONSE);
		// api.getResponseObj(ROUTE_SERVICE_ERR_RESPONSE);

	}
}
