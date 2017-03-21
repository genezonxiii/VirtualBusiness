package tw.com.aber.sftransfer.controller;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.File;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.w3c.dom.Attr;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

public class SfApi {
	private static final Logger logger = LogManager.getLogger(SfApi.class);
	
	private static final String xmlDataItemServiceRequest = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>" +
		"<Request service=\"ITEM_SERVICE\" lang=\"zh-CN\">" +
		"<Head>" +
		"<AccessCode>ITCNC1htXV9xuOKrhu24ow==</AccessCode>" +
		"<Checkword>ANU2VHvV5eqsr2PJHu2znWmWtz2CdIvj</Checkword>" +
		"</Head>" +
		"<Body>" +
		"<ItemRequest>" +
		"<CompanyCode>WYDGJ</CompanyCode>" +
		"<Items>" +
		"<Item>" +
		"<SkuNo>PY3001ASF</SkuNo>" +
		"<ItemName>Urban Denim寵物床（城市牛仔-橘黑S）</ItemName>" +
		"<BarCode>" +
		"<BarCode1>817152011705</BarCode1>" +
		"</BarCode>" +
		"<Containers>" +
		"<Container>" +
		"<PackUm>CS</PackUm>" +
		"</Container>" +
		"</Containers>" +
		"</Item>" +
		"<Item>" +
		"<SkuNo>PY3001AMF</SkuNo>" +
		"<ItemName>Urban Denim寵物床（城市牛仔-橘黑M）</ItemName>" +
		"<BarCode>" +
		"<BarCode1>817152011712</BarCode1>" +
		"</BarCode>" +
		"<Containers>" +
		"<Container>" +
		"<PackUm>CS</PackUm>" +
		"</Container>" +
		"</Containers>" +
		"</Item>" +
		"</Items>" +
		"</ItemRequest>" +
		"</Body>" +
		"</Request>";
	private static final String xmlDataItemQueryServiceRequest = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>" +
		"<Request service=\"ITEM_QUERY_SERVICE\" lang=\"zh-CN\">" +
		"<Head>" +
		"<AccessCode>ITCNC1htXV9xuOKrhu24ow==</AccessCode>" +
		"<Checkword>ANU2VHvV5eqsr2PJHu2znWmWtz2CdIvj</Checkword>" +
		"</Head>" +
		"<Body>" +
		"<ItemQueryRequest>" +
		"<CompanyCode>WYDGJ</CompanyCode>" +
		"<SkuNoList>" +
		"<SkuNo>PY3001ASF</SkuNo>" +
		"</SkuNoList>" +
		"</ItemQueryRequest>" +
		"</Body>" +
		"</Request>";
	private static final String xmlDataPurchaseOrderServiceRequest = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>" +
		"<Request service=\"PURCHASE_ORDER_SERVICE\" lang=\"zh-CN\">" +
		"<Head>" +
		"<AccessCode>ITCNC1htXV9xuOKrhu24ow==</AccessCode>" +
		"<Checkword>ANU2VHvV5eqsr2PJHu2znWmWtz2CdIvj</Checkword>" +
		"</Head>" +
		"<Body>" +
		"<PurchaseOrderRequest>" +
		"<CompanyCode>WYDGJ</CompanyCode>" +
		"<PurchaseOrders>" +
		"<PurchaseOrder>" +
		"<WarehouseCode>571DCF</WarehouseCode>" +
		"<ErpOrder>PI170112002</ErpOrder>" +
		"<ErpOrderType>10</ErpOrderType>" +
		"<ScheduledReceiptDate>2017-03-22 15:00:00</ScheduledReceiptDate>" +
		"<VendorCode>WYDGJ</VendorCode>" +
		"<Items>" +
		"<Item>" +
		"<SkuNo>PY3001ASF</SkuNo>" +
		"<Qty>100</Qty>" +
		"</Item>" +
		"</Items>" +
		"</PurchaseOrder>" +
		"</PurchaseOrders>" +
		"</PurchaseOrderRequest>" +
		"</Body>" +
		"</Request>";
	private static final String xmlDataPurchaseOrderInboundQueryServiceRequest = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>" +
		"<Request service=\"PURCHASE_ORDER_INBOUND_QUERY_SERVICE\" lang=\"zh-CN\">" +
		"<Head>" +
		"<AccessCode>ITCNC1htXV9xuOKrhu24ow==</AccessCode>" +
		"<Checkword>ANU2VHvV5eqsr2PJHu2znWmWtz2CdIvj</Checkword>" +
		"</Head>" +
		"<Body>" +
		"<PurchaseOrderInboundRequest>" +
		"<CompanyCode>WYDGJ</CompanyCode>" +
		"<PurchaseOrders>" +
		"<PurchaseOrder>" +
		"<WarehouseCode>571DCF</WarehouseCode>" +
		"<ErpOrder>PI170112002</ErpOrder>" +
		"</PurchaseOrder>" +
		"</PurchaseOrders>" +
		"</PurchaseOrderInboundRequest>" +
		"</Body>" +
		"</Request>";
	private static final String xmlDataCancelPurchaseOrderServiceRequest = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>" +
		"<Request service=\"CANCEL_PURCHASE_ORDER_SERVICE\" lang=\"zh-CN\">" +
		"<Head>" +
		"<AccessCode>ITCNC1htXV9xuOKrhu24ow==</AccessCode>" +
		"<Checkword>ANU2VHvV5eqsr2PJHu2znWmWtz2CdIvj</Checkword>" +
		"</Head>" +
		"<Body>" +
		"<CancelPurchaseOrderRequest>" +
		"<CompanyCode>WYDGJ</CompanyCode>" +
		"<PurchaseOrders>" +
		"<PurchaseOrder>" +
		"<ErpOrder>PI170112002</ErpOrder>" +
		"</PurchaseOrder>" +
		"</PurchaseOrders>" +
		"</CancelPurchaseOrderRequest>" +
		"</Body>" +
		"</Request>";
	private static final String xmlDataSaleOrderServiceRequest = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>" +
		"<Request service=\"SALE_ORDER_SERVICE\" lang=\"zh-CN\">" +
		"<Head>" +
		"<AccessCode>ITCNC1htXV9xuOKrhu24ow==</AccessCode>" +
		"<Checkword>ANU2VHvV5eqsr2PJHu2znWmWtz2CdIvj</Checkword>" +
		"</Head>" +
		"<Body>" +
		"<SaleOrderRequest>" +
		"<CompanyCode>WYDGJ</CompanyCode>" +
		"<SaleOrders>" +
		"<SaleOrder>" +
		"<WarehouseCode>571DCF</WarehouseCode>" +
		"<SfOrderType></SfOrderType>" +
		"<ErpOrder>SI170301007</ErpOrder>" +
		"<OrderReceiverInfo>" +
		"<ReceiverCompany>北祥</ReceiverCompany>" +
		"<ReceiverName>收件人</ReceiverName>" +
		"<ReceiverZipCode>114</ReceiverZipCode>" +
		"<ReceiverMobile>0912345678</ReceiverMobile>" +
		"<ReceiverCountry>台灣</ReceiverCountry>" +
		"<ReceiverAddress>台北市內湖區文湖街18號</ReceiverAddress>" +
		"<OrderItems>" +
		"<OrderItem>" +
		"<SkuNo>PY3001ASF</SkuNo>" +
		"<ItemQuantity>1</ItemQuantity>" +
		"</OrderItem>" +
		"</OrderItems>" +
		"</OrderReceiverInfo>" +
		"</SaleOrder>" +
		"</SaleOrders>" +
		"</SaleOrderRequest>" +
		"</Body>" +
		"</Request>";
	private static final String xmlDataSaleOrderStatusQueryServiceRequest = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>" +
		"<Request service=\"SALE_ORDER_STATUS_QUERY_SERVICE\" lang=\"zh-CN\">" +
		"<Head>" +
		"<AccessCode>ITCNC1htXV9xuOKrhu24ow==</AccessCode>" +
		"<Checkword>ANU2VHvV5eqsr2PJHu2znWmWtz2CdIvj</Checkword>" +
		"</Head>" +
		"<Body>" +
		"<SaleOrderStatusRequest>" +
		"<CompanyCode>WYDGJ</CompanyCode>" +
		"<SaleOrders>" +
		"<SaleOrder>" +
		"<WarehouseCode>571DCF</WarehouseCode>" +
		"<ErpOrder>SI170301007</ErpOrder>" +
		"</SaleOrder>" +
		"</SaleOrders>" +
		"</SaleOrderStatusRequest>" +
		"</Body>" +
		"</Request>";
	private static final String xmlDataSaleOrderOutboundDetailQueryServiceRequest = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>" +
		"<Request service=\"SALE_ORDER_OUTBOUND_DETAIL_QUERY_SERVICE\" lang=\"zh-CN\">" +
		"<Head>" +
		"<AccessCode>ITCNC1htXV9xuOKrhu24ow==</AccessCode>" +
		"<Checkword>ANU2VHvV5eqsr2PJHu2znWmWtz2CdIvj</Checkword>" +
		"</Head>" +
		"<Body>" +
		"<SaleOrderOutboundDetailRequest>" +
		"<CompanyCode>WYDGJ</CompanyCode>" +
		"<SaleOrders>" +
		"<SaleOrder>" +
		"<WarehouseCode>571DCF</WarehouseCode>" +
		"<ErpOrder>SI170301007</ErpOrder>" +
		"</SaleOrder>" +
		"</SaleOrders>" +
		"</SaleOrderOutboundDetailRequest>" +
		"</Body>" +
		"</Request>";
	private static final String xmlDataCancelSaleOrderServiceRequest = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>" +
		"<Request service=\"CANCEL_SALE_ORDER_SERVICE\" lang=\"zh-CN\">" +
		"<Head>" +
		"<AccessCode>ITCNC1htXV9xuOKrhu24ow==</AccessCode>" +
		"<Checkword>ANU2VHvV5eqsr2PJHu2znWmWtz2CdIvj</Checkword>" +
		"</Head>" +
		"<Body>" +
		"<CancelSaleOrderRequest>" +
		"<CompanyCode>WYDGJ</CompanyCode>" +
		"<SaleOrders>" +
		"<SaleOrder>" +
		"<ErpOrder>SI170301007</ErpOrder>" +
		"</SaleOrder>" +
		"</SaleOrders>" +
		"</CancelSaleOrderRequest>" +
		"</Body>" +
		"</Request>";
	private static final String xmlDataAsynSaleOrderServiceRequest = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>" +
		"<Request service=\"ASYN_SALE_ORDER_SERVICE\" lang=\"zh-CN\">" +
		"<Head>" +
		"<AccessCode>ITCNC1htXV9xuOKrhu24ow==</AccessCode>" +
		"<Checkword>ANU2VHvV5eqsr2PJHu2znWmWtz2CdIvj</Checkword>" +
		"</Head>" +
		"<Body>" +
		"<SaleOrderRequest>" +
		"<CompanyCode>WYDGJ</CompanyCode>" +
		"<SaleOrders>" +
		"<SaleOrder>" +
		"<WarehouseCode>571DCF</WarehouseCode>" +
		"<SfOrderType>30</SfOrderType>" +
		"<ErpOrder>SI170301007</ErpOrder>" +
		"<TradeOrderDateTime>2017-03-22 15:00:00</TradeOrderDateTime>" +
		"<OrderReceiverInfo>" +
		"<ReceiverCompany>北祥</ReceiverCompany>" +
		"<ReceiverName>收件人</ReceiverName>" +
		"<ReceiverZipCode>114</ReceiverZipCode>" +
		"<ReceiverMobile>0912345678</ReceiverMobile>" +
		"<ReceiverCountry>台灣</ReceiverCountry>" +
		"<ReceiverAddress>台北市內湖區文湖街18號</ReceiverAddress>" +
		"<OrderItems>" +
		"<OrderItem>" +
		"<SkuNo>PY3001ASF</SkuNo>" +
		"<ItemQuantity>1</ItemQuantity>" +
		"</OrderItem>" +
		"</OrderItems>" +
		"</OrderReceiverInfo>" +
		"</SaleOrder>" +
		"</SaleOrders>" +
		"</SaleOrderRequest>" +
		"</Body>" +
		"</Request>";

	public static void main(String[] args){
		SfApi api = new SfApi();
		
//		private final String targetURL = getServletConfig().getServletContext().getInitParameter("dbUserName");

//		String returnValue = api.generateXMLw3c("");

		//XML 報文測通
		String targetURL = "http://bsp.sit.sf-express.com:8080/bsp-wms/OmsCommons";
		String urlParameters = "";
		
		String reqXml = xmlDataAsynSaleOrderServiceRequest;
		String logisticsInterface = reqXml;
		String dataDigest = reqXml + "123456";
		
		Md5Base64 enMd5Base64 = new Md5Base64();
		dataDigest = enMd5Base64.encode(dataDigest);
		logger.debug("md5 + Base64:" + dataDigest);
		dataDigest = enMd5Base64.urlEncode(dataDigest);
		logger.debug("md5 + Base64 > urlEncode:" + dataDigest);
		logisticsInterface = enMd5Base64.urlEncode(logisticsInterface);
		logger.debug("logisticsInterface:" + logisticsInterface);
		
		urlParameters = "logistics_interface=" + logisticsInterface + "&data_digest=" + dataDigest;
		
		String returnValue = api.executePost(targetURL, urlParameters);
		
		logger.debug("returnValue:" + returnValue);
	}
	
	public String getXML(String type) {
		try {
			logger.debug("getXML");
//			URL url = getClass().getResource("RequestProduct.xml");
//			logger.debug(url);
//			
//			File fXmlFile = new File(url.getPath());
//			DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
//			DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
//			Document doc = dBuilder.parse(fXmlFile);
//			
//			doc.getDocumentElement().normalize();
//			logger.debug("Root element :" + doc.getDocumentElement().getNodeName());
//			NodeList nList = doc.getElementsByTagName("Head");
//			
//			Node nNode = nList.item(0);
//			
//			if (nNode.getNodeType() == Node.ELEMENT_NODE) {
//				Element eElement = (Element) nNode;
//				logger.debug("AccessCode : " + eElement.getAttribute("AccessCode"));
//			}

			return "";
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		} finally {
			
		}
	}
	
	public String generateXMLw3c(String type) {

		try {
			/*
			 * 
			 *<?xml version="1.0" encoding="UTF-8" standalone="no" ?>
			 *<company>
			 *	<staff id="1">
			 *		<firstname>yong</firstname>
			 *		<lastname>mook kim</lastname>
			 *		<nickname>mkyong</nickname>
			 *		<salary>100000</salary>
			 *	</staff>
			 *</company>
			 */
			
			/*
			 * import javax.xml.parsers.DocumentBuilder;
			 * import javax.xml.parsers.DocumentBuilderFactory;
			 * import javax.xml.parsers.ParserConfigurationException;
			 * import javax.xml.transform.Transformer;
			 * import javax.xml.transform.TransformerException;
			 * import javax.xml.transform.TransformerFactory;
			 * import javax.xml.transform.dom.DOMSource;
			 * import javax.xml.transform.stream.StreamResult;
			
			 * import org.apache.logging.log4j.LogManager;
			 * import org.apache.logging.log4j.Logger;
			 * import org.w3c.dom.Attr;
			 * import org.w3c.dom.Document;
			 * import org.w3c.dom.Element;
			 */
			DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder docBuilder = docFactory.newDocumentBuilder();

			// root elements
			Document doc = docBuilder.newDocument();
			Element rootElement = doc.createElement("company");
			doc.appendChild(rootElement);

			// staff elements
			Element staff = doc.createElement("Staff");
			rootElement.appendChild(staff);

			// set attribute to staff element
			Attr attr = doc.createAttribute("id");
			attr.setValue("1");
			staff.setAttributeNode(attr);

			// shorten way
			// staff.setAttribute("id", "1");

			// firstname elements
			Element firstname = doc.createElement("firstname");
			firstname.appendChild(doc.createTextNode("yong"));
			staff.appendChild(firstname);

			// lastname elements
			Element lastname = doc.createElement("lastname");
			lastname.appendChild(doc.createTextNode("mook kim"));
			staff.appendChild(lastname);

			// nickname elements
			Element nickname = doc.createElement("nickname");
			nickname.appendChild(doc.createTextNode("mkyong"));
			staff.appendChild(nickname);

			// salary elements
			Element salary = doc.createElement("salary");
			salary.appendChild(doc.createTextNode("100000"));
			staff.appendChild(salary);

			// write the content into xml file
			TransformerFactory transformerFactory = TransformerFactory.newInstance();
			Transformer transformer = transformerFactory.newTransformer();
			DOMSource source = new DOMSource(doc);
			logger.debug(new File("file.xml").getAbsolutePath());
			StreamResult result = new StreamResult(new File("file.xml").getAbsolutePath());

			// Output to console for testing
			// StreamResult result = new StreamResult(System.out);

			transformer.transform(source, result);

			logger.debug("File saved!");
			
			

		} catch (ParserConfigurationException pce) {
			pce.printStackTrace();
		} catch (TransformerException tfe) {
			tfe.printStackTrace();
		}
		
		return "test";

	}
	
	public String generateXMLItemQueryServicew3c(String type) {
	
		try {
			
			DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder docBuilder = docFactory.newDocumentBuilder();
	
			// root elements
			Document doc = docBuilder.newDocument();
			Element rootElement = doc.createElement("Request");
			doc.appendChild(rootElement);
	
			// staff elements
			Element staff = doc.createElement("Staff");
			rootElement.appendChild(staff);
	
			// set attribute to staff element
			Attr attr = doc.createAttribute("id");
			attr.setValue("1");
			staff.setAttributeNode(attr);
	
			// shorten way
			// staff.setAttribute("id", "1");
	
			// firstname elements
			Element firstname = doc.createElement("firstname");
			firstname.appendChild(doc.createTextNode("yong"));
			staff.appendChild(firstname);
	
			// lastname elements
			Element lastname = doc.createElement("lastname");
			lastname.appendChild(doc.createTextNode("mook kim"));
			staff.appendChild(lastname);
	
			// nickname elements
			Element nickname = doc.createElement("nickname");
			nickname.appendChild(doc.createTextNode("mkyong"));
			staff.appendChild(nickname);
	
			// salary elements
			Element salary = doc.createElement("salary");
			salary.appendChild(doc.createTextNode("100000"));
			staff.appendChild(salary);
	
			// write the content into xml file
			TransformerFactory transformerFactory = TransformerFactory.newInstance();
			Transformer transformer = transformerFactory.newTransformer();
			DOMSource source = new DOMSource(doc);
			logger.debug(new File("file.xml").getAbsolutePath());
			StreamResult result = new StreamResult(new File("file.xml").getAbsolutePath());
	
			// Output to console for testing
			// StreamResult result = new StreamResult(System.out);
	
			transformer.transform(source, result);
	
			logger.debug("File saved!");
			
			
	
		} catch (ParserConfigurationException pce) {
			pce.printStackTrace();
		} catch (TransformerException tfe) {
			tfe.printStackTrace();
		}
		
		return "test";
	
	}

	public String generateXMLjdom2(String type) {
		try {
			/*
			 * import org.jdom2.Document;
			 * import org.jdom2.Element;
			 * import org.jdom2.output.Format;
			 * import org.jdom2.output.XMLOutputter;
			 * 
			 */
//			Element root=new Element("CONFIGURATION");
//			Document doc=new Document();
//
//			Element child1=new Element("BROWSER");
//			child1.addContent("chrome");
//			Element child2=new Element("BASE");
//			child1.addContent("http:fut");
//			Element child3=new Element("EMPLOYEE");
//			child3.addContent(new Element("EMP_NAME").addContent("Anhorn, Irene"));
//
//			root.addContent(child1);
//			root.addContent(child2);
//			root.addContent(child3);
//
//			doc.setRootElement(root);
//
//			XMLOutputter outter=new XMLOutputter();
//			outter.setFormat(Format.getPrettyFormat());
//			logger.debug(new File("myxml.xml").getAbsolutePath());
//			outter.output(doc, new FileWriter(new File("myxml.xml")));
			
			
			
			return "";
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
	
}
