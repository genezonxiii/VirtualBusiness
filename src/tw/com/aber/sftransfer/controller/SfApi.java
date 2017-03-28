package tw.com.aber.sftransfer.controller;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.StringWriter;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.xml.bind.JAXB;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpMethod;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.io.IOUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import tw.com.aber.sf.vo.BarCode;
import tw.com.aber.sf.vo.Body;
import tw.com.aber.sf.vo.CancelPurchaseOrderRequest;
import tw.com.aber.sf.vo.CancelSaleOrderRequest;
import tw.com.aber.sf.vo.Containers;
import tw.com.aber.sf.vo.Head;
import tw.com.aber.sf.vo.ItemQueryRequest;
import tw.com.aber.sf.vo.ItemRequest;
import tw.com.aber.sf.vo.Items;
import tw.com.aber.sf.vo.OrderItem;
import tw.com.aber.sf.vo.OrderItems;
import tw.com.aber.sf.vo.OrderReceiverInfo;
import tw.com.aber.sf.vo.PurchaseOrder;
import tw.com.aber.sf.vo.PurchaseOrderInboundRequest;
import tw.com.aber.sf.vo.PurchaseOrderRequest;
import tw.com.aber.sf.vo.PurchaseOrders;
import tw.com.aber.sf.vo.Request;
import tw.com.aber.sf.vo.SaleOrder;
import tw.com.aber.sf.vo.SaleOrderOutboundDetailRequest;
import tw.com.aber.sf.vo.SaleOrderRequest;
import tw.com.aber.sf.vo.SaleOrderStatusRequest;
import tw.com.aber.sf.vo.SaleOrders;
import tw.com.aber.sf.vo.SfContainer;
import tw.com.aber.sf.vo.SfItem;
import tw.com.aber.sf.vo.SkuNoList;

public class SfApi {
	private static final Logger logger = LogManager.getLogger(SfApi.class);
	
	private static final String testOrderType = "采购入库";
	private static final String testOrderType1 = "采购入库 \u91c7\u8d2d\u5165\u5e93 générale 誠哥有無份投佢";
	private static final String xmlDataItemServiceRequest = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>" +
		"<Request service=\"ITEM_SERVICE\" lang=\"zh-TW\">" +
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
		"<Request service=\"ITEM_QUERY_SERVICE\" lang=\"zh-TW\">" +
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
		"<Request service=\"PURCHASE_ORDER_SERVICE\" lang=\"zh-TW\">" +
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
		"<SFOrderType>10</SFOrderType>" +
		"<ScheduledReceiptDate>2017-03-22 15:00:00</ScheduledReceiptDate>" +
		"<VendorCode>WYDGJ</VendorCode>" +
		"<Items>" +
		"<Item>" +
		"<SkuNo>PY3001ASF</SkuNo>" +
		"<Qty>100</Qty>" +
		"</Item>" +
		"</Items>" +
		"</PurchaseOrder>" +
		"<PurchaseOrder>" +
		"<WarehouseCode>571DCF</WarehouseCode>" +
		"<ErpOrder>PI170323002</ErpOrder>" +
		"<ErpOrderType>10</ErpOrderType>" +
		"<SFOrderType>10</SFOrderType>" +
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
		"<Request service=\"PURCHASE_ORDER_INBOUND_QUERY_SERVICE\" lang=\"zh-TW\">" +
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
		"<Request service=\"CANCEL_PURCHASE_ORDER_SERVICE\" lang=\"zh-TW\">" +
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
		"<Request service=\"SALE_ORDER_SERVICE\" lang=\"zh-TW\">" +
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
		"<Request service=\"SALE_ORDER_STATUS_QUERY_SERVICE\" lang=\"zh-TW\">" +
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
		"<Request service=\"SALE_ORDER_OUTBOUND_DETAIL_QUERY_SERVICE\" lang=\"zh-TW\">" +
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
		"<Request service=\"CANCEL_SALE_ORDER_SERVICE\" lang=\"zh-TW\">" +
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
		"<Request service=\"ASYN_SALE_ORDER_SERVICE\" lang=\"zh-TW\">" +
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

	public String genItemService() {
		String result;
		
		List<SfItem> itemList = new ArrayList<SfItem>();
		
		//Containers
		SfContainer container = new SfContainer();
		container.setPackUm("CS");
		
		Containers containers = new Containers();
		containers.setContainer(container);
		
		//item1
		BarCode barCode = new BarCode();
		barCode.setBarCode1("12345");
		
		SfItem item = new SfItem();
		item.setSkuNo("PY3001ASF");
		item.setItemName("採購入庫");
		
		String itemDescNo = this.genNo();
		try {
			String test = new String( "采购入库".getBytes("UTF-8") );
			item.setDescription( "采购入库".concat(itemDescNo));
		} catch (UnsupportedEncodingException uee) {
			logger.error("UnsupportedEncodingException:" + uee.getMessage());
		}
		
		itemList.add(item);
		
		Items items = new Items();
		items.setItemList(itemList);
		
		ItemRequest itemRequest = new ItemRequest();
		itemRequest.setCompanyCode("WYDGJ");
		itemRequest.setItems(items);
		
		//head, body
		Head head = new Head();
		head.setAccessCode("ITCNC1htXV9xuOKrhu24ow==");
		head.setCheckword("ANU2VHvV5eqsr2PJHu2znWmWtz2CdIvj");

		Body body = new Body();
		body.setItemRequest(itemRequest);
		
		Request mainXML = new Request();
		mainXML.setService("ITEM_SERVICE");
		mainXML.setLang("zh-TW");
		mainXML.setHead(head);
        mainXML.setBody(body);
		
        StringWriter sw = new StringWriter();
        JAXB.marshal(mainXML, sw);
        logger.debug("--- start: output of marshalling ----");
        logger.debug(sw.toString());
        result = sw.toString();
        logger.debug("--- end: output of marshalling ----");
      
        return result;
	}
	
	public String genItemQueryService() {
		String result;
		
		List<String> skuNo = new ArrayList<String>();
		
		//item1
		skuNo.add("PY3001ASF");
		
		SkuNoList skuNoList = new SkuNoList();
		skuNoList.setSkuNo(skuNo);
		
		ItemQueryRequest itemQueryRequest = new ItemQueryRequest();
		itemQueryRequest.setCompanyCode("WYDGJ");
		itemQueryRequest.setSkuNoList(skuNoList);
		
		//head, body
		Head head = new Head();
		head.setAccessCode("ITCNC1htXV9xuOKrhu24ow==");
		head.setCheckword("ANU2VHvV5eqsr2PJHu2znWmWtz2CdIvj");

		Body body = new Body();
		body.setItemQueryRequest(itemQueryRequest);
		
		Request mainXML = new Request();
		mainXML.setService("ITEM_QUERY_SERVICE");
		mainXML.setLang("zh-TW");
		mainXML.setHead(head);
        mainXML.setBody(body);
		
        StringWriter sw = new StringWriter();
        JAXB.marshal(mainXML, sw);
        logger.debug("--- start: output of marshalling ----");
        logger.debug(sw.toString());
        result = sw.toString();
        logger.debug("--- end: output of marshalling ----");
      
        return result;
	}

	public String genPurchaseOrderService() {
		String result;
		
		List<SfItem> itemList = new ArrayList<SfItem>();
		List<PurchaseOrder> purchaseOrderList = new ArrayList<PurchaseOrder>();
		
		//item1
		SfItem item = new SfItem();
		item.setSkuNo("1437368316");
		item.setQty("100");
		
		itemList.add(item);
		
//		//item2
//		SfItem item2 = new SfItem();
//		item2.setSkuNo("1437368316");
//		item2.setQty("110");
//		
//		itemList.add(item2);
//		
//		//item3
//		SfItem item3 = new SfItem();
//		item3.setSkuNo("PY3001ALF");
//		item3.setQty("120");
//		
//		itemList.add(item3);
		
		Items items = new Items();
		items.setItemList(itemList);
		
		//purchaseOrder1
		String purInstructNo = this.genNo();
		
		PurchaseOrder purchaseOrder = new PurchaseOrder();
		purchaseOrder.setWarehouseCode("571DCF");
		purchaseOrder.setErpOrder("PI".concat(purInstructNo).concat("-1"));
		purchaseOrder.setErpOrderType("10");
		purchaseOrder.setsFOrderType("采购入库");
		purchaseOrder.setScheduledReceiptDate("2017-03-25 15:00:00");
		purchaseOrder.setVendorCode("WYDGJ");
		purchaseOrder.setItems(items);
		
		purchaseOrderList.add(purchaseOrder);
		
		//purchaseOrder2
		PurchaseOrder purchaseOrder2 = new PurchaseOrder();
		purchaseOrder2.setWarehouseCode("571DCF");
		purchaseOrder2.setErpOrder("PI".concat(purInstructNo).concat("-2"));
		purchaseOrder2.setErpOrderType("10");
		purchaseOrder2.setsFOrderType("采购入库");
		purchaseOrder2.setScheduledReceiptDate("2017-03-25 15:00:00");
		purchaseOrder2.setVendorCode("WYDGJ");
		purchaseOrder2.setItems(items);
		
		purchaseOrderList.add(purchaseOrder2);
		
		PurchaseOrders purchaseOrders = new PurchaseOrders();
		purchaseOrders.setPurchaseOrder(purchaseOrderList);
		
		PurchaseOrderRequest purchaseOrderRequest = new PurchaseOrderRequest();
		purchaseOrderRequest.setCompanyCode("WYDGJ");
		purchaseOrderRequest.setPurchaseOrders(purchaseOrders);
		
		//head, body
		Head head = new Head();
		head.setAccessCode("ITCNC1htXV9xuOKrhu24ow==");
		head.setCheckword("ANU2VHvV5eqsr2PJHu2znWmWtz2CdIvj");

		Body body = new Body();
		body.setPurchaseOrderRequest(purchaseOrderRequest);
		
		Request mainXML = new Request();
		mainXML.setService("PURCHASE_ORDER_SERVICE");
		mainXML.setLang("zh-TW");
		mainXML.setHead(head);
        mainXML.setBody(body);
		
        StringWriter sw = new StringWriter();
        JAXB.marshal(mainXML, sw);
        logger.debug("--- start: output of marshalling ----");
        logger.debug(sw.toString());
        result = sw.toString();
        logger.debug("--- end: output of marshalling ----");
        
        return result;
	}
	
	public String genPurchaseOrderInboundQueryService(String po) {
		String result;
		
		List<PurchaseOrder> purchaseOrderList = new ArrayList<PurchaseOrder>();
		
		//purchaseOrder
		PurchaseOrder purchaseOrder = new PurchaseOrder();
		purchaseOrder.setWarehouseCode("571DCF");
		purchaseOrder.setErpOrder(po);
		
		purchaseOrderList.add(purchaseOrder);
		
		//purchaseOrder1
		PurchaseOrder purchaseOrder1 = new PurchaseOrder();
		purchaseOrder1.setWarehouseCode("571DCF");
		purchaseOrder1.setErpOrder(po.concat("-1"));
		
		purchaseOrderList.add(purchaseOrder1);
		
		//purchaseOrder2
		PurchaseOrder purchaseOrder2 = new PurchaseOrder();
		purchaseOrder2.setWarehouseCode("571DCF");
		purchaseOrder2.setErpOrder(po.concat("-2"));
		
		purchaseOrderList.add(purchaseOrder2);
		
		PurchaseOrders purchaseOrders = new PurchaseOrders();
		purchaseOrders.setPurchaseOrder(purchaseOrderList);
		
		PurchaseOrderInboundRequest purchaseOrderInboundRequest = new PurchaseOrderInboundRequest();
		purchaseOrderInboundRequest.setCompanyCode("WYDGJ");
		purchaseOrderInboundRequest.setPurchaseOrders(purchaseOrders);
		
		//head, body
		Head head = new Head();
		head.setAccessCode("ITCNC1htXV9xuOKrhu24ow==");
		head.setCheckword("ANU2VHvV5eqsr2PJHu2znWmWtz2CdIvj");
	
		Body body = new Body();
		body.setPurchaseOrderInboundRequest(purchaseOrderInboundRequest);
		
		Request mainXML = new Request();
		mainXML.setService("PURCHASE_ORDER_INBOUND_QUERY_SERVICE");
		mainXML.setLang("zh-TW");
		mainXML.setHead(head);
	    mainXML.setBody(body);
		
	    StringWriter sw = new StringWriter();
	    JAXB.marshal(mainXML, sw);
	    logger.debug("--- start: output of marshalling ----");
	    logger.debug(sw.toString());
	    result = sw.toString();
	    logger.debug("--- end: output of marshalling ----");
	    
	    return result;
	}

	public String genCancelPurchaseOrderInboundQueryService() {
		String result;
		
		List<PurchaseOrder> purchaseOrderList = new ArrayList<PurchaseOrder>();
		
		//purchaseOrder1
		PurchaseOrder purchaseOrder = new PurchaseOrder();
		purchaseOrder.setErpOrder("PI170112002");
		
		purchaseOrderList.add(purchaseOrder);
		
		//purchaseOrder2
		PurchaseOrder purchaseOrder2 = new PurchaseOrder();
		purchaseOrder2.setErpOrder("PI170323001");
		
		purchaseOrderList.add(purchaseOrder2);
		
		PurchaseOrders purchaseOrders = new PurchaseOrders();
		purchaseOrders.setPurchaseOrder(purchaseOrderList);
		
		CancelPurchaseOrderRequest cancelPurchaseOrderRequest = new CancelPurchaseOrderRequest();
		cancelPurchaseOrderRequest.setCompanyCode("WYDGJ");
		cancelPurchaseOrderRequest.setPurchaseOrders(purchaseOrders);
		
		//head, body
		Head head = new Head();
		head.setAccessCode("ITCNC1htXV9xuOKrhu24ow==");
		head.setCheckword("ANU2VHvV5eqsr2PJHu2znWmWtz2CdIvj");
	
		Body body = new Body();
		body.setCancelPurchaseOrderRequest(cancelPurchaseOrderRequest);
		
		Request mainXML = new Request();
		mainXML.setService("CANCEL_PURCHASE_ORDER_SERVICE");
		mainXML.setLang("zh-TW");
		mainXML.setHead(head);
	    mainXML.setBody(body);
		
	    StringWriter sw = new StringWriter();
	    JAXB.marshal(mainXML, sw);
	    logger.debug("--- start: output of marshalling ----");
	    logger.debug(sw.toString());
	    result = sw.toString();
	    logger.debug("--- end: output of marshalling ----");
	    
	    return result;
	}

	public String genSaleOrderService() {
		String result;
		
		List<OrderItem> orderItemList = new ArrayList<OrderItem>();
		List<SaleOrder> saleOrderList = new ArrayList<SaleOrder>();
		
		//item1
		OrderItem orderItem = new OrderItem();
		orderItem.setSkuNo("PY3001ASF");
		orderItem.setItemQuantity("1");
		
		orderItemList.add(orderItem);
		
		//item2
		OrderItem orderItem2 = new OrderItem();
		orderItem2.setSkuNo("PY3001AMF");
		orderItem2.setItemQuantity("1");
		
		orderItemList.add(orderItem2);
		
		OrderItems orderItems = new OrderItems();
		orderItems.setOrderItem(orderItemList);
		
		OrderReceiverInfo orderReceiverInfo = new OrderReceiverInfo();
		orderReceiverInfo.setReceiverCompany("北祥");
		orderReceiverInfo.setReceiverName("收件人");
		orderReceiverInfo.setReceiverZipCode("114");
		orderReceiverInfo.setReceiverMobile("0912345678");
		orderReceiverInfo.setReceiverCountry("台灣");
		orderReceiverInfo.setReceiverAddress("台北市內湖區文湖街18號");
		orderReceiverInfo.setOrderItems(orderItems);
		
		String saleNo = this.genNo();
		
		SaleOrder saleOrder = new SaleOrder();
		saleOrder.setWarehouseCode("571DCF");
		saleOrder.setSfOrderType("销售订单");
		saleOrder.setErpOrder("SI".concat(saleNo));
		saleOrder.setOrderReceiverInfo(orderReceiverInfo);
		
		saleOrderList.add(saleOrder);
		
		SaleOrders saleOrders = new SaleOrders();
		saleOrders.setSaleOrder(saleOrderList);
		
		SaleOrderRequest saleOrderRequest = new SaleOrderRequest();
		saleOrderRequest.setCompanyCode("WYDGJ");
		saleOrderRequest.setSaleOrders(saleOrders);
		
		//head, body
		Head head = new Head();
		head.setAccessCode("ITCNC1htXV9xuOKrhu24ow==");
		head.setCheckword("ANU2VHvV5eqsr2PJHu2znWmWtz2CdIvj");
	
		Body body = new Body();
		body.setSaleOrderRequest(saleOrderRequest);
		
		Request mainXML = new Request();
		mainXML.setService("SALE_ORDER_SERVICE");
		mainXML.setLang("zh-TW");
		mainXML.setHead(head);
	    mainXML.setBody(body);
		
	    StringWriter sw = new StringWriter();
	    JAXB.marshal(mainXML, sw);
	    logger.debug("--- start: output of marshalling ----");
	    logger.debug(sw.toString());
	    result = sw.toString();
	    logger.debug("--- end: output of marshalling ----");
	    
	    return result;
	}

	public String genSaleOrderStatusQueryService(String so) {
		String result;
		
		List<SaleOrder> saleOrderList = new ArrayList<SaleOrder>();
		
		//saleOrder1
		SaleOrder saleOrder = new SaleOrder();
		saleOrder.setWarehouseCode("571DCF");
		saleOrder.setErpOrder(so);

		saleOrderList.add(saleOrder);
		
		//saleOrder2
		SaleOrder saleOrder2 = new SaleOrder();
		saleOrder2.setWarehouseCode("571DCF");
		saleOrder2.setErpOrder("SI170323007");
				
		saleOrderList.add(saleOrder2);
		
		SaleOrders saleOrders = new SaleOrders();
		saleOrders.setSaleOrder(saleOrderList);
		
		SaleOrderStatusRequest saleOrderStatusRequest = new SaleOrderStatusRequest();
		saleOrderStatusRequest.setCompanyCode("WYDGJ");
		saleOrderStatusRequest.setSaleOrders(saleOrders);
		
		//head, body
		Head head = new Head();
		head.setAccessCode("ITCNC1htXV9xuOKrhu24ow==");
		head.setCheckword("ANU2VHvV5eqsr2PJHu2znWmWtz2CdIvj");
	
		Body body = new Body();
		body.setSaleOrderStatusRequest(saleOrderStatusRequest);
		
		Request mainXML = new Request();
		mainXML.setService("SALE_ORDER_STATUS_QUERY_SERVICE");
		mainXML.setLang("zh-TW");
		mainXML.setHead(head);
	    mainXML.setBody(body);
		
	    StringWriter sw = new StringWriter();
	    JAXB.marshal(mainXML, sw);
	    logger.debug("--- start: output of marshalling ----");
	    logger.debug(sw.toString());
	    result = sw.toString();
	    logger.debug("--- end: output of marshalling ----");
	    
	    return result;
	}

	public String genSaleOrderOutboundDetailQueryService() {
		String result;
		
		List<SaleOrder> saleOrderList = new ArrayList<SaleOrder>();
		
		//saleOrder1
		SaleOrder saleOrder = new SaleOrder();
		saleOrder.setWarehouseCode("571DCF");
		saleOrder.setErpOrder("SI170301007");

		saleOrderList.add(saleOrder);
		
		//saleOrder2
		SaleOrder saleOrder2 = new SaleOrder();
		saleOrder2.setWarehouseCode("571DCF");
		saleOrder2.setErpOrder("SI170323007");
				
		saleOrderList.add(saleOrder2);
		
		SaleOrders saleOrders = new SaleOrders();
		saleOrders.setSaleOrder(saleOrderList);
		
		SaleOrderOutboundDetailRequest saleOrderOutboundDetailRequest = new SaleOrderOutboundDetailRequest();
		saleOrderOutboundDetailRequest.setCompanyCode("WYDGJ");
		saleOrderOutboundDetailRequest.setSaleOrders(saleOrders);
		
		//head, body
		Head head = new Head();
		head.setAccessCode("ITCNC1htXV9xuOKrhu24ow==");
		head.setCheckword("ANU2VHvV5eqsr2PJHu2znWmWtz2CdIvj");
	
		Body body = new Body();
		body.setSaleOrderOutboundDetailRequest(saleOrderOutboundDetailRequest);
		
		Request mainXML = new Request();
		mainXML.setService("SALE_ORDER_OUTBOUND_DETAIL_QUERY_SERVICE");
		mainXML.setLang("zh-TW");
		mainXML.setHead(head);
	    mainXML.setBody(body);
		
	    StringWriter sw = new StringWriter();
	    JAXB.marshal(mainXML, sw);
	    logger.debug("--- start: output of marshalling ----");
	    logger.debug(sw.toString());
	    result = sw.toString();
	    logger.debug("--- end: output of marshalling ----");
	    
	    return result;
	}

	public String genCancelSaleOrderService() {
		String result;
		
		List<SaleOrder> saleOrderList = new ArrayList<SaleOrder>();
		
		//saleOrder1
		SaleOrder saleOrder = new SaleOrder();
		saleOrder.setErpOrder("SI170301007");

		saleOrderList.add(saleOrder);
		
		//saleOrder2
		SaleOrder saleOrder2 = new SaleOrder();
		saleOrder2.setErpOrder("SI170323007");
				
		saleOrderList.add(saleOrder2);
		
		SaleOrders saleOrders = new SaleOrders();
		saleOrders.setSaleOrder(saleOrderList);
		
		CancelSaleOrderRequest cancelSaleOrderRequest = new CancelSaleOrderRequest();
		cancelSaleOrderRequest.setCompanyCode("WYDGJ");
		cancelSaleOrderRequest.setSaleOrders(saleOrders);
		
		//head, body
		Head head = new Head();
		head.setAccessCode("ITCNC1htXV9xuOKrhu24ow==");
		head.setCheckword("ANU2VHvV5eqsr2PJHu2znWmWtz2CdIvj");
	
		Body body = new Body();
		body.setCancelSaleOrderRequest(cancelSaleOrderRequest);
		
		Request mainXML = new Request();
		mainXML.setService("CANCEL_SALE_ORDER_SERVICE");
		mainXML.setLang("zh-TW");
		mainXML.setHead(head);
	    mainXML.setBody(body);
		
	    StringWriter sw = new StringWriter();
	    JAXB.marshal(mainXML, sw);
	    logger.debug("--- start: output of marshalling ----");
	    logger.debug(sw.toString());
	    result = sw.toString();
	    logger.debug("--- end: output of marshalling ----");
	    
	    return result;
	}

	public String genNo() {
		Date date = new Date();
		DateFormat dateFormat = new SimpleDateFormat("yyyyMMddHHmmssSSS");
		return dateFormat.format(date).toString();
	}

	public String sendXML(String reqXml) {
		String targetURL = "http://bsp.sit.sf-express.com:8080/bsp-wms/OmsCommons";
		String urlParameters = "";
		
		SfApi api = new SfApi();
	
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
		return returnValue;
	}

	public String sendXMLbyWS(String ws, String reqXml) {
		
		String conString = 
				//getServletConfig().getServletContext().getInitParameter("pythonwebservice")
				ws
				+ "/sfexpressapi/data="
				+ new String(Base64.encodeBase64String(reqXml.getBytes()));
		
		logger.debug(conString);
		
		HttpClient client = new HttpClient();
		HttpMethod method = null;
		String ret="";
		try{
			method=new GetMethod(conString);
			client.executeMethod(method);
			
			StringWriter writer = new StringWriter();
			IOUtils.copy(method.getResponseBodyAsStream(), writer, "UTF-8");
			ret = writer.toString();
		}catch(Exception e){
			return "WebService Error for:"+e.toString();
		} finally {
			method.releaseConnection();
			
		}
		return ret;	
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
			connection.setRequestProperty("Content-Language", "zh-TW");

			connection.setUseCaches(false);
			connection.setDoOutput(true);

			// Send request
			DataOutputStream wr = new DataOutputStream(connection.getOutputStream());
			wr.writeBytes(urlParameters);
			wr.close();

			// Get Response
			InputStream is = connection.getInputStream();
			BufferedReader rd = new BufferedReader(new InputStreamReader(is));
			StringBuilder response = new StringBuilder(); // or StringBuffer if Java version 5+
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

	public void codeTest(){
		 try {
	            
			// 測試中文轉碼
			String x = "采购入库";

			System.out.printf("%s:\t%s\n", "String", x);

			System.out.print("BIG5:\t");
			byte y[] = x.getBytes("big5");
			for (int i = 0; i < y.length; i++) {
				System.out.printf("%x ", y[i]);
			}
			System.out.println(y);

			System.out.print("UTF-8:\t");
			byte z[] = x.getBytes("utf-8");
			for (int i = 0; i < z.length; i++) {
				System.out.printf("%x ", z[i]);
			}
			System.out.println(z);

			String v = new String(y);
			System.out.println("BIG5:\t".concat(v));

			String w = new String(z);
			System.out.println("UTF-8:\t".concat(w));

		} catch (java.io.UnsupportedEncodingException e) {
			System.out.println("Error: UnsupportedEncodingException - ".concat(e.getMessage()));
			System.exit(1);
		}
	}
	
	public static void main(String[] args){
		SfApi api = new SfApi();		
		String genXML = "";
		
		genXML = api.genItemService();
		api.sendXML(genXML);
	
//		genXML = api.genItemQueryService();
//		api.sendXML(genXML);

//		genXML = api.genPurchaseOrderService();
//		api.sendXML(genXML);
		
//		api.codeTest();
		
//		genXML = api.genItemService();
//		api.sendXMLbyWS(genXML);		
		
//		genXML = api.genPurchaseOrderService();
//		api.sendXMLbyWS("http://192.168.112.164:8090", genXML);
	}
	
}
