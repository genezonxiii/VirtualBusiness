package tw.com.aber.egs.controller;

import java.io.StringWriter;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpMethod;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.io.IOUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class EgsApi {
	private static final Logger logger = LogManager.getLogger(EgsApi.class);

	private EgsApi egsApi;
	private String conString = "http://192.168.112.156:8800/egs?";
	private String cmd = "cmd=";
	
	public Map toMap(String string) {
		Map<String, String> map = new HashMap<String, String>();
		String[] pairs = string.split("&");
		if (pairs.length > 0) {
			for (int i = 0; i < pairs.length; i++) {
				String pair = pairs[i];
				String[] keyValue = pair.split("=");
				map.put(keyValue[0], keyValue[1]);
			}
		}
		return map;
	}
	
	public String getQuerySuda7(String address) {
		String suda7 = null;
		try {
			address = URLEncoder.encode(address, "utf8");

			String sendString = "query_suda7&address_1=" + address;
			logger.debug("sendString:" + sendString);

			String responseStr = send(sendString);

			logger.debug("responseStr:" + responseStr);
			Map<String, String> map = toMap(responseStr);
			suda7 = map.get("suda7_1");

		} catch (Exception e) {
			logger.debug("error:" + e.getMessage());
			return "";
		}
		if (suda7 != null) {
			suda7 = suda7.replace("+", "");
			return suda7;
		} else {
			return "";
		}
	}

	public String getEGSNum() {
		String responseStr = send("query_egs_info");
		logger.debug("responseStr:" + responseStr);
		Map<String, String> map = toMap(responseStr);
		String eGSNum = map.get("egs_version");
		if (eGSNum != null) {
			return eGSNum;
		} else {
			return "";
		}
	}
	
	public String send(String command) {
		egsApi = new EgsApi();
		cmd = cmd.concat(command);
		conString += cmd;
		return egsApi.executeGET(conString);
	}

	public String send(String command, String params) {
		egsApi = new EgsApi();
		cmd = cmd.concat(command).concat(params);
		conString += cmd;
		return egsApi.executeGET(conString);
	}
	 
//	public String queryWaybillIdRange(String command, String params) {
//		egsApi = new EgsApi();
//		cmd = cmd.concat(command).concat(params);
//		conString += cmd;
//		return egsApi.executeGET(conString);
//	}
//
//	
//	public String queryEgsInfo(String command) {
//		egsApi = new EgsApi();
//		cmd = cmd.concat(command);
//		conString += cmd;
//		return egsApi.executeGET(conString);
//	}
//
//	public String queryCustomers(String command) {
//		egsApi = new EgsApi();
//		cmd = cmd.concat(command);
//		conString += cmd;
//		return egsApi.executeGET(conString);
//	}
//
//	public String testDeliveryTimezone(String command, String params) {
//		egsApi = new EgsApi();
//		cmd = cmd.concat(command).concat(params);
//		conString += cmd;
//		return egsApi.executeGET(conString);
//	}

	public String querySuda5(String command, String addresses) {
		egsApi = new EgsApi();
		cmd = cmd.concat(command);

		String params = "";
		/*
		 * 假如無地址，則以noaddress替代，若有多筆地址，因中文緣故，需編碼處理
		 */
		if (!"noaddress".equals(addresses)) {
			String[] addresArr = addresses.split(",");

			try {
				for (int i = 0; i < addresArr.length; i++) {
					addresArr[i] = URLEncoder.encode(addresArr[i], "utf8");
				}
				for (int i = 0; i < addresArr.length; i++) {
					params += ("&address_" + (i + 1) + "=" + addresArr[i]);
				}
			} catch (Exception e) {
				logger.debug("querySuda5:" + e.getMessage());
			}
		} else {
			params += ("&address_1=" + addresses);
		}
		conString += (cmd + params);
		return egsApi.executeGET(conString);
	}

	public String getParams(String[] paramsArr) {
		String result = "";
		try {
			for (int i = 0; i < paramsArr.length; i++) {
				result += ("&" + paramsArr[i]);
			}
		} catch (Exception e) {
			logger.debug(e.getMessage());
		}
		return result;
	}

	private String executeGET(String conString) {
		logger.debug("conString: {}", conString);
		String responseStr = "";
		HttpClient client = new HttpClient();
		HttpMethod method = null;
		try {
			method = new GetMethod(conString);
		} catch (Exception e) {
			logger.debug("executeGET conString:" + e.getMessage());
		}

		try {
			client.executeMethod(method);
		} catch (Exception e) {
			logger.debug("executeGET executeMethod:" + e.getMessage());
		}

		try {
			StringWriter writer = new StringWriter();
			IOUtils.copy(method.getResponseBodyAsStream(), writer, "UTF-8");
			responseStr = writer.toString();

			logger.debug("responseStr: {}", responseStr);

		} catch (Exception e) {
			logger.debug("Error of call webservice content:" + e.toString());
		} finally {
			method.releaseConnection();
		}
		return responseStr;
	}
}
