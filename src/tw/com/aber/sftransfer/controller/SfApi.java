package tw.com.aber.sftransfer.controller;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.File;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class SfApi {
	private static final Logger logger = LogManager.getLogger(SfApi.class);
	
	private static final String xmlData1 = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>" +
			"<Request service=\"ITEM_QUERY_SERVICE\" lang=\"zh-CN\">" +
			"<Head>" +
			"<AccessCode>ITCNC1htXV9xuOKrhu24ow==</AccessCode>" +
			"<Checkword>ANU2VHvV5eqsr2PJHu2znWmWtz2CdIvj</Checkword>" +
			"</Head>" +
			"<Body>" +
			"<ItemQueryRequest>" +
			"<CompanyCode>WYDGJ</CompanyCode>" +
			"<SkuNoList>" +
			"<SkuNo>0609788854142</SkuNo>" +
			"</SkuNoList>" +
			"</ItemQueryRequest>" +
			"</Body>" +
			"</Request>";
	
	public static void main(String[] args){
		SfApi api = new SfApi();
		
		String targetURL = "http://bsp.sit.sf-express.com:8080/bsp-wms/OmsCommons";
		String urlParameters = "";
		
//		private final String targetURL = getServletConfig().getServletContext().getInitParameter("dbUserName");
		
		byte[] bytesOfMessage;
		MessageDigest md;
		
		String dataDigest = "<order></order>" + "123456";
		Md5Base64 enMd5Base64 = new Md5Base64();
		String encodeValue = enMd5Base64.encode(dataDigest);
		
		logger.debug("md5 + Base64:" + encodeValue);
		
		
//		String returnValue = api.executePost(targetURL, urlParameters);
		
//		logger.debug("returnValue:" + returnValue);
//		String returnValue = api.getXML("type");
		
		
		
	}
	
	public String getXML(String type) {
		try {
			
			URL url = getClass().getResource("RequestProduct.xml");
			logger.debug(url);
			
			File fXmlFile = new File(url.getPath());
			DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
			Document doc = dBuilder.parse(fXmlFile);
			
			doc.getDocumentElement().normalize();
			logger.debug("Root element :" + doc.getDocumentElement().getNodeName());
			NodeList nList = doc.getElementsByTagName("Head");
			
			Node nNode = nList.item(0);
			
			if (nNode.getNodeType() == Node.ELEMENT_NODE) {
				Element eElement = (Element) nNode;
				logger.debug("AccessCode : " + eElement.getAttribute("AccessCode"));
			}
			
			
			
			return doc.toString();
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		} finally {
			
		}
	}
	
	
	
	public static String executePost(String targetURL, String urlParameters) {
		HttpURLConnection connection = null;

		try {
			// Create connection
			URL url = new URL(targetURL);
			connection = (HttpURLConnection) url.openConnection();
			connection.setRequestMethod("POST");
			connection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");

			connection.setRequestProperty("Content-Length", Integer.toString(urlParameters.getBytes().length));
			connection.setRequestProperty("Content-Language", "zh-CN");

			connection.setUseCaches(false);
			connection.setDoOutput(true);

			// Send request
			DataOutputStream wr = new DataOutputStream(connection.getOutputStream());
			wr.writeBytes(urlParameters);
			wr.close();

			// Get Response
			InputStream is = connection.getInputStream();
			BufferedReader rd = new BufferedReader(new InputStreamReader(is));
			StringBuilder response = new StringBuilder(); // or StringBuffer if
															// Java version 5+
			String line;
			while ((line = rd.readLine()) != null) {
				response.append(line);
				response.append('\r');
			}
			rd.close();
			return response.toString();
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		} finally {
			if (connection != null) {
				connection.disconnect();
			}
		}
	}
	
//	public String postXMLData(String destinationUrl, String requestXml) {
//	    HttpWebRequest request = (HttpWebRequest)WebRequest.Create(destinationUrl);
//	    byte[] bytes;
//	    bytes = System.Text.Encoding.ASCII.GetBytes(requestXml);
//	    request.ContentType = "text/xml; encoding='utf-8'";
//	    request.ContentLength = bytes.Length;
//	    request.Method = "POST";
//	    Stream requestStream = request.GetRequestStream();
//	    requestStream.Write(bytes, 0, bytes.Length);
//	    requestStream.Close();
//	    HttpWebResponse response;
//	    response = (HttpWebResponse)request.GetResponse();
//	    if (response.StatusCode == HttpStatusCode.OK)
//	    {
//	        Stream responseStream = response.GetResponseStream();
//	        String responseStr = new StreamReader(responseStream).ReadToEnd();
//	        return responseStr;
//	    }
//	    return null;
//	}
}
