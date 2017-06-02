package tw.com.aber.sftransfer.controller;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.io.StringWriter;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Type;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.servlet.ServletContext;
import javax.xml.bind.JAXB;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;
import javax.xml.transform.stream.StreamSource;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpMethod;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.io.IOUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.google.gson.Gson;
import org.mortbay.jetty.servlet.Context;

import tw.com.aber.product.controller.product.ProductBean;
import tw.com.aber.purchase.controller.purchase;
import tw.com.aber.sf.vo.BarCode;
import tw.com.aber.sf.vo.Body;
import tw.com.aber.sf.vo.Bom;
import tw.com.aber.sf.vo.BomRequest;
import tw.com.aber.sf.vo.Boms;
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
import tw.com.aber.sf.vo.RTInventory;
import tw.com.aber.sf.vo.RTInventoryQueryRequest;
import tw.com.aber.sf.vo.RTInventorys;
import tw.com.aber.sf.vo.Request;
import tw.com.aber.sf.vo.Response;
import tw.com.aber.sf.vo.SaleOrder;
import tw.com.aber.sf.vo.SaleOrderOutboundDetailRequest;
import tw.com.aber.sf.vo.SaleOrderOutboundDetailResponse;
import tw.com.aber.sf.vo.SaleOrderRequest;
import tw.com.aber.sf.vo.SaleOrderStatusRequest;
import tw.com.aber.sf.vo.SaleOrders;
import tw.com.aber.sf.vo.SfBomItem;
import tw.com.aber.sf.vo.SfBomItems;
import tw.com.aber.sf.vo.SfContainer;
import tw.com.aber.sf.vo.SfItem;
import tw.com.aber.sf.vo.SkuNoList;
import tw.com.aber.sftransfer.controller.ValueService.ValueService_Service;
import tw.com.aber.util.Util;
import tw.com.aber.vo.GroupSfVO;
import tw.com.aber.vo.PackageVO;
import tw.com.aber.vo.ProductPackageVO;
import tw.com.aber.vo.PurchaseDetailVO;
import tw.com.aber.vo.PurchaseVO;
import tw.com.aber.vo.ShipDetail;
import tw.com.aber.vo.ShipVO;
import tw.com.aber.vo.StockNewVO;
import tw.com.aber.vo.WarehouseVO;

public class SfApi {
	private static final Logger logger = LogManager.getLogger(SfApi.class);

	// 商品接口響應 - 系統正常
	private static final String ITEM_SERVICE_RESPONSE = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>"
			+ "<Response service=\"ITEM_SERVICE\">" + "<Head>OK|PART</Head>" + "<Body><ItemResponse>" + "<Items>"
			+ "<Item><SkuNo>F18M291</SkuNo><Result>1</Result><Note>成功</Note></Item>"
			+ "<Item><SkuNo>FE0577</SkuNo><Result>2</Result><Note>失敗</Note></Item>" + "</Items>"
			+ "</ItemResponse></Body>" + "</Response>";

	// 商品查詢接口響應 - 系統正常
	private static final String ITEM_QUERY_SERVICE_RESPONSE = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>"
			+ "<Response service=\"ITEM_QUERY_SERVICE\">" + "<Head>OK|PART</Head>" + "<Body>" + "<ItemResponse>"
			+ "<CompanyCode>WYDGJ</CompanyCode>" + "<Result>1</Result>" + "<Items>" + "<Item>"
			+ "<SkuNo>F18M291</SkuNo>" + "<ItemName>時尚編織懶人鞋</ItemName>" + "<Containers>" + "<Container>"
			+ "<PackUm>盒</PackUm>" + "</Container>" + "</Containers>" + "</Item>" + "<Item>" + "<SkuNo>FE0577</SkuNo>"
			+ "<ItemName>防水外套(紫色)</ItemName>" + "<Containers>" + "<Container>" + "<PackUm>套</PackUm>" + "</Container>"
			+ "</Containers>" + "</Item>" + "</Items>" + "</ItemResponse>" + "</Body>" + "</Response>";

	// 商品變更推送接口響應 - 系統正常
	private static final String IITEM_CHANGE_PUSH_SERVICE_RESPONSE = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>"
			+ "<Response service=\"ITEM_CHANGE_PUSH_SERVICE\">" + "<Head>OK</Head>" + "<Body><ItemChangePushResponse>"
			+ "<Result>1</Result><Note>測試備註</Note>" + "</ItemChangePushResponse></Body></Response>";

	// BOM(組合商品)接口響應 - 系統正常
	private static final String BOM_SERVICE_RESPONSE = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>"
			+ "<Response service=\"BOM_SERVICE\">" + "<Head>OK|PART</Head>" + "<Body><BomResponse>" + "<Boms>"
			+ "<Bom><Item>F18M291</Item><Result>1</Result><Note>成功</Note></Bom>"
			+ "<Bom><Item>FE0577</Item><Result>2</Result><Note>失敗</Note></Bom>" + "</Boms>" + "</BomResponse></Body>"
			+ "</Response>";

	// 供應商接口響應 - 系統正常
	private static final String VENDOR_SERVICE_RESPONSE = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>"
			+ "<Response service=\"VENDOR_SERVICE\">" + "<Head>OK|PART</Head>" + "<Body><VendorResponse>" + "<Vendors>"
			+ "<Vendor><VendorCode>F18M291</VendorCode><Result>1</Result><Note>成功</Note></Vendor>"
			+ "<Vendor><VendorCode>FE0577</VendorCode><Result>2</Result><Note>失敗</Note></Vendor>" + "</Vendors>" + "</VendorResponse></Body>"
			+ "</Response>";

	// 入庫單接口響應 - 系統正常
	private static final String PURCHASE_ORDER_SERVICE_RESPONSE = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>"
			+ "<Response service=\"PURCHASE_ORDER_SERVICE\" lang=\"zh-CN\">" + "<Head>OK|PART</Head>" + "<Body><PurchaseOrderResponse>" + "<PurchaseOrders>"
			+ "<PurchaseOrder><ErpOrder>F18M291</ErpOrder><ReceiptId>F000001</ReceiptId><Result>1</Result><Note>成功</Note></PurchaseOrder>"
			+ "<PurchaseOrder><ErpOrder>FE0577</ErpOrder><ReceiptId>F000002</ReceiptId><Result>2</Result><Note>失敗</Note></PurchaseOrder>" 
			+ "</PurchaseOrders>" + "</PurchaseOrderResponse></Body>"
			+ "</Response>";

	// 入庫單取消接口響應 - 系統正常
	private static final String CANCEL_PURCHASE_ORDER_SERVICE_RESPONSE = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>"
			+ "<Response service=\"CANCEL_PURCHASE_ORDER_SERVICE\" lang=\"zh-CN\">" 
			+ "<Head>OK</Head>" 
			+ "<Body>"
			+ "<CancelPurchaseOrderResponse>" 
				+ "<PurchaseOrders>"
					+ "<PurchaseOrder><ErpOrder>F18M291</ErpOrder><Result>1</Result><Note>成功</Note></PurchaseOrder>"
					+ "<PurchaseOrder><ErpOrder>FE0577</ErpOrder><Result>2</Result><Note>失敗</Note></PurchaseOrder>" 
				+ "</PurchaseOrders>" 
			+ "</CancelPurchaseOrderResponse>"
			+ "</Body>"
			+ "</Response>";

	// 入庫單明細推送接口響應 - 系統正常
	private static final String PURCHASE_ORDER_INBOUND_PUSH_SERVICE_RESPONSE = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>"
			+ "<Response service=\"PURCHASE_ORDER_INBOUND_PUSH_SERVICE\" lang=\"zh-CN\">" + "<Head>OK</Head>" + "<Body><PurchaseOrderInboundResponse>" + 
			"<Result>1</Result><Note>成功</Note></PurchaseOrderInboundResponse></Body>"
			+ "</Response>";
	
	// 入庫單明細查詢接口響應 - 系統正常
	private static final String PURCHASE_ORDER_INBOUND_QUERY_SERVICE_RESPONSE = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>"
			+ "<Response service=\"PURCHASE_ORDER_INBOUND_QUERY_SERVICE\" lang=\"zh-CN\">" 
				+ "<Head>OK</Head>" 
				+ "<Body><PurchaseOrderInboundResponse>"
				+ "<PurchaseOrders>"
					+ "<PurchaseOrder>"
						+ "<Result>1</Result>"
						+ "<Note>成功</Note>"
						+ "<Header>"
							+ "<WarehouseCode>F0001</WarehouseCode>"
							+ "<ErpOrder>F18M291</ErpOrder>"
							+ "<ReceiptId>F19DD21</ReceiptId>"
							+ "<ErpOrderType>S</ErpOrderType>"
							+ "<CloseDate>2017-05-31</CloseDate>"
						+ "</Header>"
						+ "<Items>"
							+ "<Item>"
								+ "<SkuNo>PD0001</SkuNo>"
							+ "</Item>"
							+ "<Item>"
								+ "<SkuNo>PD0002</SkuNo>"
							+ "</Item>"
						+ "</Items>"
					+ "</PurchaseOrder>"
					+ "<PurchaseOrder>"
						+ "<Result>2</Result>"
						+ "<Note>失敗</Note>"
					+ "</PurchaseOrder>"
				+ "</PurchaseOrders>"
			+ "</PurchaseOrderInboundResponse></Body>"
			+ "</Response>";

	// 出庫單接口響應 - 系統正常
	private static final String SALE_ORDER_SERVICE_RESPONSE = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>"
			+ "<Response service=\"SALE_ORDER_SERVICE\">" + "<Head>OK|PART</Head>" 
			+ "<Body><SaleOrderResponse>" 
			+ "<SaleOrders>"
			+ "<SaleOrder><ErpOrder>F18M291</ErpOrder><ShipmentId>OUT001</ShipmentId><Result>1</Result><Note>成功</Note></SaleOrder>"
			+ "<SaleOrder><ErpOrder>F18M292</ErpOrder><ShipmentId>OUT002</ShipmentId><Result>2</Result><Note>失敗</Note></SaleOrder>"
			+ "</SaleOrders>" 
			+ "</SaleOrderResponse></Body>"
			+ "</Response>";

	// 出庫單取消接口響應 - 系統正常
	private static final String CANCEL_SALE_ORDER_SERVICE_RESPONSE = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>"
			+ "<Response service=\"CANCEL_SALE_ORDER_SERVICE\">" + "<Head>OK|PART</Head>" 
			+ "<Body><CancelSaleOrderResponse>" 
			+ "<SaleOrders>"
			+ "<SaleOrder><ErpOrder>F18M291</ErpOrder><Result>1</Result><Note>成功</Note></SaleOrder>"
			+ "<SaleOrder><ErpOrder>F18M292</ErpOrder><Result>2</Result><Note>失敗</Note></SaleOrder>"
			+ "</SaleOrders>" 
			+ "</CancelSaleOrderResponse></Body>"
			+ "</Response>";

	// 出庫單明細推送接口響應 - 系統正常
	private static final String SALE_ORDER_OUTBOUND_DETAIL_SERVICE_RESPONSE = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>"
			+ "<Response service=\"SALE_ORDER_OUTBOUND_DETAIL_SERVICE\">" + "<Head>OK</Head>" 
			+ "<Body><SaleOrderOutboundDetailResponse>" 
			+ "<Result>1</Result><Note>成功</Note>"
			+ "</SaleOrderOutboundDetailResponse></Body>"
			+ "</Response>";
	
	// 出庫單明細查詢接口響應 - 系統正常
	private static final String SALE_ORDER_OUTBOUND_DETAIL_QUERY_SERVICE_RESPONSE = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>"
			+ "<Response service=\"SALE_ORDER_OUTBOUND_DETAIL_QUERY_SERVICE\">" + "<Head>OK</Head>" 
			+ "<Body><SaleOrderOutboundDetailResponse>" 
			+ "<SaleOrders>"
			+ "<SaleOrder>"
			+ "<Result>1</Result><ErpOrder>F18M291</ErpOrder><Note>成功</Note>"
				+ "<Header>"
				+ "<WarehouseCode>F0001</WarehouseCode>"
				+ "<ErpOrder>F18M291</ErpOrder>"
				+ "<ShipmentId>F19DD21</ShipmentId>"
				+ "</Header>"
			+ "</SaleOrder>"
			+ "<SaleOrder>"
			+ "<Result>2</Result><ErpOrder>F18M292</ErpOrder><Note>失敗</Note>"
				+ "<Header>"
				+ "<WarehouseCode>F0002</WarehouseCode>"
				+ "<ErpOrder>F18M292</ErpOrder>"
				+ "<ShipmentId>F19DD22</ShipmentId>"
				+ "</Header>"
			+ "</SaleOrder>"
			+ "</SaleOrders>" 
			+ "</SaleOrderOutboundDetailResponse></Body>"
			+ "</Response>";

	// 實時庫存查詢接口響應 - 系統正常
	private static final String RT_INVENTORY_QUERY_SERVICE_RESPONSE = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>"
			+ "<Response service=\"RT_INVENTORY_QUERY_SERVICE\">" + "<Head>OK</Head>" 
			+ "<Body><RTInventoryQueryResponse>" 
			+ "<CompanyCode>PSC</CompanyCode>"
			+ "<WarehouseCode>A0001</WarehouseCode>"
			+ "<RTInventorys>"
				+ "<RTInventory>"
				+ "<Result>1</Result><Note>成功</Note>"
					+ "<Header>"
						+ "<SkuNo>S0001</SkuNo>"
						+ "<InventoryStatus>10</InventoryStatus>"
						+ "<TotalQty>184</TotalQty>"
						+ "<OnHandQty>52</OnHandQty>"
						+ "<AvailableQty>40</AvailableQty>"
						+ "<InTransitQty>132</InTransitQty>"
					+ "</Header>"
					+ "<Subs>"
						+ "<Sub>"
							+ "<WarehouseCode>F0001</WarehouseCode>"
							+ "<TotalQty>85</TotalQty>"
							+ "<OnHandQty>26</OnHandQty>"
							+ "<AvailableQty>25</AvailableQty>"
							+ "<InTransitQty>40</InTransitQty>"
						+ "</Sub>"
						+ "<Sub>"
							+ "<WarehouseCode>F0002</WarehouseCode>"
							+ "<TotalQty>99</TotalQty>"
							+ "<OnHandQty>26</OnHandQty>"
							+ "<AvailableQty>15</AvailableQty>"
							+ "<InTransitQty>82</InTransitQty>"
						+ "</Sub>"
					+ "</Subs>"
				+ "</RTInventory>"
				+ "<RTInventory>"
					+ "<Result>2</Result><Note>失敗</Note>"
					+ "<Header>"
						+ "<SkuNo>S0002</SkuNo>"
						+ "<InventoryStatus>10</InventoryStatus>"
						+ "<TotalQty>368</TotalQty>"
						+ "<OnHandQty>104</OnHandQty>"
						+ "<AvailableQty>80</AvailableQty>"
						+ "<InTransitQty>264</InTransitQty>"
					+ "</Header>"
					+ "<Subs>"
						+ "<Sub>"
							+ "<WarehouseCode>K0001</WarehouseCode>"
							+ "<TotalQty>170</TotalQty>"
							+ "<OnHandQty>52</OnHandQty>"
							+ "<AvailableQty>50</AvailableQty>"
							+ "<InTransitQty>80</InTransitQty>"
						+ "</Sub>"
						+ "<Sub>"
							+ "<WarehouseCode>K0002</WarehouseCode>"
							+ "<TotalQty>198</TotalQty>"
							+ "<OnHandQty>52</OnHandQty>"
							+ "<AvailableQty>30</AvailableQty>"
							+ "<InTransitQty>164</InTransitQty>"
						+ "</Sub>"
					+ "</Subs>"
				+ "</RTInventory>"
			+ "</RTInventorys>" 
			+ "</RTInventoryQueryResponse></Body>"
			+ "</Response>";
	
	// 接口響應 - 系統異常
	private static final String ITEM_QUERY_SERVICE_ERR_RESPONSE = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>"
			+ "<Response service=\"ITEM_SERVICE\">" + "<Head>ERR</Head>"
			+ "<Error code=\"01234\">系統異常(測試錯誤訊息)</Error></Response>";
	
	private static final String testOrderType = "采购入库";
	private static final String testOrderType1 = "采购入库 \u91c7\u8d2d\u5165\u5e93 générale 誠哥有無份投佢";
	private static final String xmlDataItemServiceRequest = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>"
			+ "<Request service=\"ITEM_SERVICE\" lang=\"zh-TW\">" + "<Head>"
			+ "<AccessCode>ITCNC1htXV9xuOKrhu24ow==</AccessCode>"
			+ "<Checkword>ANU2VHvV5eqsr2PJHu2znWmWtz2CdIvj</Checkword>" + "</Head>" + "<Body>" + "<ItemRequest>"
			+ "<CompanyCode>WYDGJ</CompanyCode>" + "<Items>" + "<Item>" + "<SkuNo>PY3001ASF</SkuNo>"
			+ "<ItemName>Urban Denim寵物床（城市牛仔-橘黑S）</ItemName>" + "<BarCode>" + "<BarCode1>817152011705</BarCode1>"
			+ "</BarCode>" + "<Containers>" + "<Container>" + "<PackUm>CS</PackUm>" + "</Container>" + "</Containers>"
			+ "</Item>" + "<Item>" + "<SkuNo>PY3001AMF</SkuNo>" + "<ItemName>Urban Denim寵物床（城市牛仔-橘黑M）</ItemName>"
			+ "<BarCode>" + "<BarCode1>817152011712</BarCode1>" + "</BarCode>" + "<Containers>" + "<Container>"
			+ "<PackUm>CS</PackUm>" + "</Container>" + "</Containers>" + "</Item>" + "</Items>" + "</ItemRequest>"
			+ "</Body>" + "</Request>";
	private static final String xmlDataItemQueryServiceRequest = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>"
			+ "<Request service=\"ITEM_QUERY_SERVICE\" lang=\"zh-TW\">" + "<Head>"
			+ "<AccessCode>ITCNC1htXV9xuOKrhu24ow==</AccessCode>"
			+ "<Checkword>ANU2VHvV5eqsr2PJHu2znWmWtz2CdIvj</Checkword>" + "</Head>" + "<Body>" + "<ItemQueryRequest>"
			+ "<CompanyCode>WYDGJ</CompanyCode>" + "<SkuNoList>" + "<SkuNo>PY3001ASF</SkuNo>" + "</SkuNoList>"
			+ "</ItemQueryRequest>" + "</Body>" + "</Request>";
	private static final String xmlDataPurchaseOrderServiceRequest = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>"
			+ "<Request service=\"PURCHASE_ORDER_SERVICE\" lang=\"zh-TW\">" + "<Head>"
			+ "<AccessCode>ITCNC1htXV9xuOKrhu24ow==</AccessCode>"
			+ "<Checkword>ANU2VHvV5eqsr2PJHu2znWmWtz2CdIvj</Checkword>" + "</Head>" + "<Body>"
			+ "<PurchaseOrderRequest>" + "<CompanyCode>WYDGJ</CompanyCode>" + "<PurchaseOrders>" + "<PurchaseOrder>"
			+ "<WarehouseCode>571DCF</WarehouseCode>" + "<ErpOrder>PI170112002</ErpOrder>"
			+ "<ErpOrderType>10</ErpOrderType>" + "<SFOrderType>10</SFOrderType>"
			+ "<ScheduledReceiptDate>2017-03-22 15:00:00</ScheduledReceiptDate>" + "<VendorCode>WYDGJ</VendorCode>"
			+ "<Items>" + "<Item>" + "<SkuNo>PY3001ASF</SkuNo>" + "<Qty>100</Qty>" + "</Item>" + "</Items>"
			+ "</PurchaseOrder>" + "<PurchaseOrder>" + "<WarehouseCode>571DCF</WarehouseCode>"
			+ "<ErpOrder>PI170323002</ErpOrder>" + "<ErpOrderType>10</ErpOrderType>" + "<SFOrderType>10</SFOrderType>"
			+ "<ScheduledReceiptDate>2017-03-22 15:00:00</ScheduledReceiptDate>" + "<VendorCode>WYDGJ</VendorCode>"
			+ "<Items>" + "<Item>" + "<SkuNo>PY3001ASF</SkuNo>" + "<Qty>100</Qty>" + "</Item>" + "</Items>"
			+ "</PurchaseOrder>" + "</PurchaseOrders>" + "</PurchaseOrderRequest>" + "</Body>" + "</Request>";
	private static final String xmlDataPurchaseOrderInboundQueryServiceRequest = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>"
			+ "<Request service=\"PURCHASE_ORDER_INBOUND_QUERY_SERVICE\" lang=\"zh-TW\">" + "<Head>"
			+ "<AccessCode>ITCNC1htXV9xuOKrhu24ow==</AccessCode>"
			+ "<Checkword>ANU2VHvV5eqsr2PJHu2znWmWtz2CdIvj</Checkword>" + "</Head>" + "<Body>"
			+ "<PurchaseOrderInboundRequest>" + "<CompanyCode>WYDGJ</CompanyCode>" + "<PurchaseOrders>"
			+ "<PurchaseOrder>" + "<WarehouseCode>571DCF</WarehouseCode>" + "<ErpOrder>PI170112002</ErpOrder>"
			+ "</PurchaseOrder>" + "</PurchaseOrders>" + "</PurchaseOrderInboundRequest>" + "</Body>" + "</Request>";
	private static final String xmlDataCancelPurchaseOrderServiceRequest = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>"
			+ "<Request service=\"CANCEL_PURCHASE_ORDER_SERVICE\" lang=\"zh-TW\">" + "<Head>"
			+ "<AccessCode>ITCNC1htXV9xuOKrhu24ow==</AccessCode>"
			+ "<Checkword>ANU2VHvV5eqsr2PJHu2znWmWtz2CdIvj</Checkword>" + "</Head>" + "<Body>"
			+ "<CancelPurchaseOrderRequest>" + "<CompanyCode>WYDGJ</CompanyCode>" + "<PurchaseOrders>"
			+ "<PurchaseOrder>" + "<ErpOrder>PI170112002</ErpOrder>" + "</PurchaseOrder>" + "</PurchaseOrders>"
			+ "</CancelPurchaseOrderRequest>" + "</Body>" + "</Request>";
	private static final String xmlDataSaleOrderServiceRequest = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>"
			+ "<Request service=\"SALE_ORDER_SERVICE\" lang=\"zh-TW\">" + "<Head>"
			+ "<AccessCode>ITCNC1htXV9xuOKrhu24ow==</AccessCode>"
			+ "<Checkword>ANU2VHvV5eqsr2PJHu2znWmWtz2CdIvj</Checkword>" + "</Head>" + "<Body>" + "<SaleOrderRequest>"
			+ "<CompanyCode>WYDGJ</CompanyCode>" + "<SaleOrders>" + "<SaleOrder>"
			+ "<WarehouseCode>571DCF</WarehouseCode>" + "<SfOrderType></SfOrderType>"
			+ "<ErpOrder>SI170301007</ErpOrder>" + "<OrderReceiverInfo>" + "<ReceiverCompany>北祥</ReceiverCompany>"
			+ "<ReceiverName>收件人</ReceiverName>" + "<ReceiverZipCode>114</ReceiverZipCode>"
			+ "<ReceiverMobile>0912345678</ReceiverMobile>" + "<ReceiverCountry>台灣</ReceiverCountry>"
			+ "<ReceiverAddress>台北市內湖區文湖街18號</ReceiverAddress>" + "<OrderItems>" + "<OrderItem>"
			+ "<SkuNo>PY3001ASF</SkuNo>" + "<ItemQuantity>1</ItemQuantity>" + "</OrderItem>" + "</OrderItems>"
			+ "</OrderReceiverInfo>" + "</SaleOrder>" + "</SaleOrders>" + "</SaleOrderRequest>" + "</Body>"
			+ "</Request>";
	private static final String xmlDataSaleOrderStatusQueryServiceRequest = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>"
			+ "<Request service=\"SALE_ORDER_STATUS_QUERY_SERVICE\" lang=\"zh-TW\">" + "<Head>"
			+ "<AccessCode>ITCNC1htXV9xuOKrhu24ow==</AccessCode>"
			+ "<Checkword>ANU2VHvV5eqsr2PJHu2znWmWtz2CdIvj</Checkword>" + "</Head>" + "<Body>"
			+ "<SaleOrderStatusRequest>" + "<CompanyCode>WYDGJ</CompanyCode>" + "<SaleOrders>" + "<SaleOrder>"
			+ "<WarehouseCode>571DCF</WarehouseCode>" + "<ErpOrder>SI170301007</ErpOrder>" + "</SaleOrder>"
			+ "</SaleOrders>" + "</SaleOrderStatusRequest>" + "</Body>" + "</Request>";
	private static final String xmlDataSaleOrderOutboundDetailQueryServiceRequest = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>"
			+ "<Request service=\"SALE_ORDER_OUTBOUND_DETAIL_QUERY_SERVICE\" lang=\"zh-TW\">" + "<Head>"
			+ "<AccessCode>ITCNC1htXV9xuOKrhu24ow==</AccessCode>"
			+ "<Checkword>ANU2VHvV5eqsr2PJHu2znWmWtz2CdIvj</Checkword>" + "</Head>" + "<Body>"
			+ "<SaleOrderOutboundDetailRequest>" + "<CompanyCode>WYDGJ</CompanyCode>" + "<SaleOrders>" + "<SaleOrder>"
			+ "<WarehouseCode>571DCF</WarehouseCode>" + "<ErpOrder>SI170301007</ErpOrder>" + "</SaleOrder>"
			+ "</SaleOrders>" + "</SaleOrderOutboundDetailRequest>" + "</Body>" + "</Request>";
	private static final String xmlDataCancelSaleOrderServiceRequest = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>"
			+ "<Request service=\"CANCEL_SALE_ORDER_SERVICE\" lang=\"zh-TW\">" + "<Head>"
			+ "<AccessCode>ITCNC1htXV9xuOKrhu24ow==</AccessCode>"
			+ "<Checkword>ANU2VHvV5eqsr2PJHu2znWmWtz2CdIvj</Checkword>" + "</Head>" + "<Body>"
			+ "<CancelSaleOrderRequest>" + "<CompanyCode>WYDGJ</CompanyCode>" + "<SaleOrders>" + "<SaleOrder>"
			+ "<ErpOrder>SI170301007</ErpOrder>" + "</SaleOrder>" + "</SaleOrders>" + "</CancelSaleOrderRequest>"
			+ "</Body>" + "</Request>";
	private static final String xmlDataAsynSaleOrderServiceRequest = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>"
			+ "<Request service=\"ASYN_SALE_ORDER_SERVICE\" lang=\"zh-TW\">" + "<Head>"
			+ "<AccessCode>ITCNC1htXV9xuOKrhu24ow==</AccessCode>"
			+ "<Checkword>ANU2VHvV5eqsr2PJHu2znWmWtz2CdIvj</Checkword>" + "</Head>" + "<Body>" + "<SaleOrderRequest>"
			+ "<CompanyCode>WYDGJ</CompanyCode>" + "<SaleOrders>" + "<SaleOrder>"
			+ "<WarehouseCode>571DCF</WarehouseCode>" + "<SfOrderType>30</SfOrderType>"
			+ "<ErpOrder>SI170301007</ErpOrder>" + "<TradeOrderDateTime>2017-03-22 15:00:00</TradeOrderDateTime>"
			+ "<OrderReceiverInfo>" + "<ReceiverCompany>北祥</ReceiverCompany>" + "<ReceiverName>收件人</ReceiverName>"
			+ "<ReceiverZipCode>114</ReceiverZipCode>" + "<ReceiverMobile>0912345678</ReceiverMobile>"
			+ "<ReceiverCountry>台灣</ReceiverCountry>" + "<ReceiverAddress>台北市內湖區文湖街18號</ReceiverAddress>"
			+ "<OrderItems>" + "<OrderItem>" + "<SkuNo>PY3001ASF</SkuNo>" + "<ItemQuantity>1</ItemQuantity>"
			+ "</OrderItem>" + "</OrderItems>" + "</OrderReceiverInfo>" + "</SaleOrder>" + "</SaleOrders>"
			+ "</SaleOrderRequest>" + "</Body>" + "</Request>";

	public String genItemService(List<ProductBean> productList, ValueService valueService) {
		List<SfItem> itemList = new ArrayList<SfItem>();

		for (int i = 0; i < productList.size(); i++) {
			ProductBean product = productList.get(i);

			SfItem item = new SfItem();
			item.setSkuNo(product.getC_product_id());
			item.setItemName(product.getProduct_name());

			// Containers
			SfContainer container = new SfContainer();
			// 先暫時放id 等之後再做查詢
			container.setPackUm("CS");
			// product.getUnit_id()

			Containers containers = new Containers();
			containers.setContainer(container);

			// item1
			BarCode barCode = new BarCode();
			barCode.setBarCode1(product.getBarcode());

			item.setBarCode(barCode);
			item.setContainers(containers);

			itemList.add(item);

		}

		String result;

		Items items = new Items();
		items.setItemList(itemList);

		ItemRequest itemRequest = new ItemRequest();

		GroupSfVO groupSfVo = valueService.getGroupSfVO();

		// 不確定是否要改
		itemRequest.setCompanyCode(groupSfVo.getCompany_code());
		itemRequest.setItems(items);

		// head, body
		Head head = new Head();
		head.setAccessCode(groupSfVo.getAccess_code());
		head.setCheckword(groupSfVo.getCheck_word());

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

	public String genItemQueryService(List<ProductBean> productList, ValueService valueService) {
		String result;

		GroupSfVO groupSfVo = valueService.getGroupSfVO();

		List<String> skuNo = new ArrayList<String>();

		for (int i = 0; i < productList.size(); i++) {
			ProductBean product = productList.get(i);
			skuNo.add(product.getC_product_id());
		}

		SkuNoList skuNoList = new SkuNoList();
		skuNoList.setSkuNo(skuNo);

		ItemQueryRequest itemQueryRequest = new ItemQueryRequest();
		itemQueryRequest.setCompanyCode(groupSfVo.getCompany_code());
		itemQueryRequest.setSkuNoList(skuNoList);

		// head, body
		Head head = new Head();
		head.setAccessCode(groupSfVo.getAccess_code());
		head.setCheckword(groupSfVo.getCheck_word());

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

	public String genItemQueryService() {
		String result;

		List<String> skuNo = new ArrayList<String>();

		// item1
		skuNo.add("PY3001ASF");

		SkuNoList skuNoList = new SkuNoList();
		skuNoList.setSkuNo(skuNo);

		ItemQueryRequest itemQueryRequest = new ItemQueryRequest();
		itemQueryRequest.setCompanyCode("WYDGJ");
		itemQueryRequest.setSkuNoList(skuNoList);

		// head, body
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

		// item1
		SfItem item = new SfItem();
		item.setSkuNo("1437368316");
		item.setQty("100");

		itemList.add(item);

		// //item2
		// SfItem item2 = new SfItem();
		// item2.setSkuNo("1437368316");
		// item2.setQty("110");
		//
		// itemList.add(item2);
		//
		// //item3
		// SfItem item3 = new SfItem();
		// item3.setSkuNo("PY3001ALF");
		// item3.setQty("120");
		//
		// itemList.add(item3);

		Items items = new Items();
		items.setItemList(itemList);

		// purchaseOrder1
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

		// purchaseOrder2
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

		// head, body
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

	public String genPurchaseOrderService(List<PurchaseVO> purchaseList, ValueService valueService) {
		String result;

		GroupSfVO groupSfVo = valueService.getGroupSfVO();
		WarehouseVO warehouseVO = valueService.getWarehouseVO();

		List<PurchaseOrder> purchaseOrderList = null;

		PurchaseOrders purchaseOrders = null;

		Items items = new Items();
		for (int i = 0; i < purchaseList.size(); i++) {

			List<PurchaseDetailVO> purchaseDetailList = purchaseList.get(i).getPurchaseDetailList();

			purchaseOrderList = new ArrayList<PurchaseOrder>();

			List<SfItem> itemList = new ArrayList<SfItem>();

			PurchaseOrder purchaseOrder = new PurchaseOrder();

			PurchaseVO purchaseVO = new PurchaseVO();
			if (purchaseDetailList != null) {
				for (int j = 0; j < purchaseDetailList.size(); j++) {

					PurchaseDetailVO purchaseDetailVO = purchaseDetailList.get(j);
					SfItem item = new SfItem();

					item.setSkuNo(purchaseDetailVO.getC_product_id());

					logger.debug("purchaseDetailVO.getQuantity():" + purchaseDetailVO.getQuantity());
					item.setQty(
							purchaseDetailVO.getQuantity() == null ? null : purchaseDetailVO.getQuantity().toString());
					itemList.add(item);
				}
			}
			purchaseOrder.setWarehouseCode(warehouseVO.getSf_warehouse_code());

			Util util = new Util();
			// 待確認
			purchaseOrder.setErpOrder(purchaseVO.getSeq_no());
			purchaseOrder.setErpOrderType("10");
			purchaseOrder.setsFOrderType("采购入库");

			String date = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
			purchaseOrder.setScheduledReceiptDate(date);
			purchaseOrder.setVendorCode(groupSfVo.getVendor_code());
			purchaseOrder.setItems(items);
			
			purchaseOrderList.add(purchaseOrder);
			items.setItemList(itemList);
		}

		purchaseOrders = new PurchaseOrders();
		purchaseOrders.setPurchaseOrder(purchaseOrderList);

		PurchaseOrderRequest purchaseOrderRequest = new PurchaseOrderRequest();
		purchaseOrderRequest.setCompanyCode(groupSfVo.getCompany_code());
		purchaseOrderRequest.setPurchaseOrders(purchaseOrders);

		// head, body
		Head head = new Head();
		head.setAccessCode(groupSfVo.getAccess_code());
		head.setCheckword(groupSfVo.getCheck_word());

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

		// purchaseOrder
		PurchaseOrder purchaseOrder = new PurchaseOrder();
		purchaseOrder.setWarehouseCode("571DCF");
		purchaseOrder.setErpOrder(po);

		purchaseOrderList.add(purchaseOrder);

		// purchaseOrder1
		PurchaseOrder purchaseOrder1 = new PurchaseOrder();
		purchaseOrder1.setWarehouseCode("571DCF");
		purchaseOrder1.setErpOrder(po.concat("-1"));

		purchaseOrderList.add(purchaseOrder1);

		// purchaseOrder2
		PurchaseOrder purchaseOrder2 = new PurchaseOrder();
		purchaseOrder2.setWarehouseCode("571DCF");
		purchaseOrder2.setErpOrder(po.concat("-2"));

		purchaseOrderList.add(purchaseOrder2);

		PurchaseOrders purchaseOrders = new PurchaseOrders();
		purchaseOrders.setPurchaseOrder(purchaseOrderList);

		PurchaseOrderInboundRequest purchaseOrderInboundRequest = new PurchaseOrderInboundRequest();
		purchaseOrderInboundRequest.setCompanyCode("WYDGJ");
		purchaseOrderInboundRequest.setPurchaseOrders(purchaseOrders);

		// head, body
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

		// purchaseOrder1
		PurchaseOrder purchaseOrder = new PurchaseOrder();
		purchaseOrder.setErpOrder("PI170112002");

		purchaseOrderList.add(purchaseOrder);

		// purchaseOrder2
		PurchaseOrder purchaseOrder2 = new PurchaseOrder();
		purchaseOrder2.setErpOrder("PI170323001");

		purchaseOrderList.add(purchaseOrder2);

		PurchaseOrders purchaseOrders = new PurchaseOrders();
		purchaseOrders.setPurchaseOrder(purchaseOrderList);

		CancelPurchaseOrderRequest cancelPurchaseOrderRequest = new CancelPurchaseOrderRequest();
		cancelPurchaseOrderRequest.setCompanyCode("WYDGJ");
		cancelPurchaseOrderRequest.setPurchaseOrders(purchaseOrders);

		// head, body
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

	public String genCancelPurchaseOrderInboundQueryService(List<PurchaseVO> purchaseList, ValueService valueService) {
		String result;

		GroupSfVO groupSfVo = valueService.getGroupSfVO();

		List<PurchaseOrder> purchaseOrderList = new ArrayList<PurchaseOrder>();

		for (int i = 0; i < purchaseList.size(); i++) {
			PurchaseOrder purchaseOrder = new PurchaseOrder();

			PurchaseVO purchaseVO = purchaseList.get(i);

			purchaseOrder.setErpOrder(purchaseVO.getSeq_no());

			purchaseOrderList.add(purchaseOrder);

		}
		
		PurchaseOrders purchaseOrders = new PurchaseOrders();
		purchaseOrders.setPurchaseOrder(purchaseOrderList);

		CancelPurchaseOrderRequest cancelPurchaseOrderRequest = new CancelPurchaseOrderRequest();
		cancelPurchaseOrderRequest.setCompanyCode(groupSfVo.getCompany_code());
		cancelPurchaseOrderRequest.setPurchaseOrders(purchaseOrders);

		// head, body
		Head head = new Head();
		head.setAccessCode(groupSfVo.getAccess_code());
		head.setCheckword(groupSfVo.getCheck_word());

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

		// item1
		OrderItem orderItem = new OrderItem();
		orderItem.setSkuNo("PY3001ASF");
		orderItem.setItemQuantity("1");

		orderItemList.add(orderItem);

		// item2
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

		// head, body
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

	public String genSaleOrderService(List<ShipVO> shipList, ValueService valueService) {
		String result;

		// 使用內部類別的function
		GroupSfVO groupSfVo = valueService.getGroupSfVO();
		WarehouseVO warehouseVo = valueService.getWarehouseVO();

		logger.debug("genSaleOrderService:" + shipList.size());

		List<OrderItem> orderItemList = null;
		List<SaleOrder> saleOrderList = new ArrayList<SaleOrder>();
		OrderItems orderItems = new OrderItems();

		for (int i = 0; i < shipList.size(); i++) {
			logger.debug("i:" + i);
			ShipVO shipVO = shipList.get(i);
			List<ShipDetail> shipDetailList = shipVO.getShipDeatil();

			orderItemList = new ArrayList<OrderItem>();
			for (int j = 0; j < shipDetailList.size(); j++) {
				ShipDetail shipDetail = shipDetailList.get(j);
				logger.debug("shipDetail:" + shipDetail + shipDetail.getQuantity());
				// item1
				OrderItem orderItem = new OrderItem();
				orderItem.setSkuNo(shipDetail.getC_product_id());
				orderItem.setItemQuantity(
						(shipDetail.getQuantity() == null ? null : shipDetail.getQuantity().toString()));
				orderItemList.add(orderItem);
			}
			orderItems.setOrderItem(orderItemList);
			OrderReceiverInfo orderReceiverInfo = new OrderReceiverInfo();
			orderReceiverInfo.setReceiverCompany("個人");
			orderReceiverInfo.setReceiverName(shipVO.getName());
			orderReceiverInfo.setReceiverZipCode("");// 郵遞區號暫不填待資料完整
			orderReceiverInfo.setReceiverMobile("");// 電話號碼暫不填待資料完整
			orderReceiverInfo.setReceiverCountry("台灣");// 國家暫填台灣 之後會改
			orderReceiverInfo.setReceiverAddress(shipVO.getDeliver_to());
			orderReceiverInfo.setOrderItems(orderItems);

			SaleOrder saleOrder = new SaleOrder();

			saleOrder.setWarehouseCode(
					warehouseVo.getWarehouse_code());/* 由順豐提供 資料未定 */
			saleOrder.setSfOrderType("销售订单");
			saleOrder.setErpOrder(shipVO.getOrder_no());
			saleOrder.setOrderReceiverInfo(orderReceiverInfo);
			saleOrderList.add(saleOrder);
		}

		SaleOrders saleOrders = new SaleOrders();
		saleOrders.setSaleOrder(saleOrderList);

		SaleOrderRequest saleOrderRequest = new SaleOrderRequest();
		saleOrderRequest.setCompanyCode(groupSfVo.getCompany_code());
		saleOrderRequest.setSaleOrders(saleOrders);

		// head, body
		Head head = new Head();
		head.setAccessCode(groupSfVo.getAccess_code());
		head.setCheckword(groupSfVo.getCheck_word());

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

	public String genCancelSaleOrderService(List<ShipVO> shipList, ValueService valueService) {
		String result;

		GroupSfVO groupSfVo = valueService.getGroupSfVO();

		List<SaleOrder> saleOrderList = new ArrayList<SaleOrder>();

		for (int i = 0; i < shipList.size(); i++) {
			logger.debug("i:" + i);

			SaleOrder saleOrder = new SaleOrder();

			String erpOrder = shipList.get(i).getOrder_no();
			saleOrder.setErpOrder(erpOrder);
			saleOrderList.add(saleOrder);
		}

		SaleOrders saleOrders = new SaleOrders();
		saleOrders.setSaleOrder(saleOrderList);

		CancelSaleOrderRequest cancelSaleOrderRequest = new CancelSaleOrderRequest();
		cancelSaleOrderRequest.setCompanyCode(groupSfVo.getCompany_code());
		cancelSaleOrderRequest.setSaleOrders(saleOrders);

		// head, body
		Head head = new Head();
		head.setAccessCode(groupSfVo.getAccess_code());
		head.setCheckword(groupSfVo.getCheck_word());

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

	public String genBomService(List<PackageVO> packageVOList, ValueService valueService) {
		String result = "";
		SfBomItem item = null;
		SfBomItems items = null;
		Bom bom = null;
		Boms boms = null;

		List<SfBomItem> itemList = null;
		List<Bom> bomList = new ArrayList<Bom>();

		GroupSfVO groupSfVo = valueService.getGroupSfVO();

		String companyCode = groupSfVo.getCompany_code();
		String accessCode = groupSfVo.getAccess_code();
		String checkword = groupSfVo.getCheck_word();
		for (PackageVO packageVO : packageVOList) {
			bom = new Bom();
			boms = new Boms();
			items = new SfBomItems();
			itemList = new ArrayList<SfBomItem>();
			List<ProductPackageVO> packageVOs = packageVO.getProductPackageList();
			for (ProductPackageVO productPackageVO : packageVOs) {
				item = new SfBomItem();
				int sequenceNum = packageVOs.indexOf(productPackageVO) + 1;
				String sequence = String.valueOf(sequenceNum);
				String itemSkuNo = productPackageVO.getProductVO().getC_product_id();
				String quantity = productPackageVO.getQuantity();
				item.setSequence(sequence);
				item.setSkuNo(itemSkuNo);
				item.setQuantity(quantity);
				itemList.add(item);
			}
			items.setItemList(itemList);
			String bomSkuNo = packageVO.getC_package_id();
			bom.setSkuNo(bomSkuNo);
			bom.setItems(items);
			bomList.add(bom);
			boms.setBomList(bomList);
		}
		BomRequest bomRequest = new BomRequest();

		bomRequest.setCompanyCode(companyCode);
		bomRequest.setBoms(boms);

		// head, body
		Head head = new Head();
		head.setAccessCode(accessCode);
		head.setCheckword(checkword);

		Body body = new Body();
		body.setBomRequest(bomRequest);

		Request mainXML = new Request();
		mainXML.setService("BOM_SERVICE");
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

		// saleOrder1
		SaleOrder saleOrder = new SaleOrder();
		saleOrder.setWarehouseCode("571DCF");
		saleOrder.setErpOrder(so);

		saleOrderList.add(saleOrder);

		// saleOrder2
		SaleOrder saleOrder2 = new SaleOrder();
		saleOrder2.setWarehouseCode("571DCF");
		saleOrder2.setErpOrder("SI170323007");

		saleOrderList.add(saleOrder2);

		SaleOrders saleOrders = new SaleOrders();
		saleOrders.setSaleOrder(saleOrderList);

		SaleOrderStatusRequest saleOrderStatusRequest = new SaleOrderStatusRequest();
		saleOrderStatusRequest.setCompanyCode("WYDGJ");
		saleOrderStatusRequest.setSaleOrders(saleOrders);

		// head, body
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

		// saleOrder1
		SaleOrder saleOrder = new SaleOrder();
		saleOrder.setWarehouseCode("571DCF");
		saleOrder.setErpOrder("SI170301007");

		saleOrderList.add(saleOrder);

		// saleOrder2
		SaleOrder saleOrder2 = new SaleOrder();
		saleOrder2.setWarehouseCode("571DCF");
		saleOrder2.setErpOrder("SI170323007");

		saleOrderList.add(saleOrder2);

		SaleOrders saleOrders = new SaleOrders();
		saleOrders.setSaleOrder(saleOrderList);

		SaleOrderOutboundDetailRequest saleOrderOutboundDetailRequest = new SaleOrderOutboundDetailRequest();
		saleOrderOutboundDetailRequest.setCompanyCode("WYDGJ");
		saleOrderOutboundDetailRequest.setSaleOrders(saleOrders);

		// head, body
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

		// saleOrder1
		SaleOrder saleOrder = new SaleOrder();
		saleOrder.setErpOrder("SI170301007");

		saleOrderList.add(saleOrder);

		// saleOrder2
		SaleOrder saleOrder2 = new SaleOrder();
		saleOrder2.setErpOrder("SI170323007");

		saleOrderList.add(saleOrder2);

		SaleOrders saleOrders = new SaleOrders();
		saleOrders.setSaleOrder(saleOrderList);

		CancelSaleOrderRequest cancelSaleOrderRequest = new CancelSaleOrderRequest();
		cancelSaleOrderRequest.setCompanyCode("WYDGJ");
		cancelSaleOrderRequest.setSaleOrders(saleOrders);

		// head, body
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
	
	public String genRtInventoryQueryService(List<StockNewVO> stockNewVOList, ValueService valueService,
			String InventoryStatus) {
		String result;
		List<RTInventory> rtInventoryList = new ArrayList<RTInventory>();
		GroupSfVO groupSfVO = valueService.getGroupSfVO();
		WarehouseVO warehouseVO = valueService.getWarehouseVO();
		
		// head, body
		Head head = new Head();
		head.setAccessCode(groupSfVO.getAccess_code());
		head.setCheckword(groupSfVO.getCheck_word());
		Body body = new Body();
		RTInventoryQueryRequest rtInventoryQueryRequest = new RTInventoryQueryRequest();
		rtInventoryQueryRequest.setCompanyCode(groupSfVO.getCompany_code());
		rtInventoryQueryRequest.setWarehouseCode(warehouseVO.getWarehouse_code());
		rtInventoryQueryRequest.setInventoryStatus(InventoryStatus);

		RTInventorys rtInventorys = new RTInventorys();

		for (int i = 0; i < stockNewVOList.size(); i++) {
			StockNewVO stockNewVO = stockNewVOList.get(i);
			RTInventory rtInventory = new RTInventory();
			if (stockNewVO != null && stockNewVO.getProductVO() != null) {
				rtInventory.setSkuNo(stockNewVO.getProductVO().getC_product_id());
				rtInventoryList.add(rtInventory);
			}
		}
		rtInventorys.setRtiList(rtInventoryList);
		rtInventoryQueryRequest.setRtInventorys(rtInventorys);

		body.setRtInventoryQueryRequest(rtInventoryQueryRequest);

		Request mainXML = new Request();
		mainXML.setService("RT_INVENTORY_QUERY_SERVICE");
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

				// getServletConfig().getServletContext().getInitParameter("pythonwebservice")
				ws + "/sfexpressapi/data=" + new String(Base64.encodeBase64String(reqXml.getBytes()));

		logger.debug(conString);

		HttpClient client = new HttpClient();
		HttpMethod method = null;
		String ret = "";
		try {
			method = new GetMethod(conString);
			client.executeMethod(method);

			StringWriter writer = new StringWriter();
			IOUtils.copy(method.getResponseBodyAsStream(), writer, "UTF-8");
			ret = writer.toString();
		} catch (Exception e) {
			return "WebService Error for:" + e.toString();
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

	public void codeTest() {
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

	/**
	 * @param xmlString
	 *            The string to be processed
	 * @return returns a response object
	 */
	public Response getItemQueryServiceResponseObj(String xmlString) {
		Response response = null;
		// JAXBContext jaxbContext = JAXBContext.newInstance(Response.class);
		// Unmarshaller unmarshaller = jaxbContext.createUnmarshaller();
		//
		// StringReader reader = new StringReader(xmlString);
		// response = (Response) unmarshaller.unmarshal(reader);
		response = JAXB.unmarshal(new StringReader(xmlString), Response.class);

		logger.debug("\n\nJson格式:\n\n{}\n", new Gson().toJson(response));
		StringWriter sw = new StringWriter();
		JAXB.marshal(response, sw);
		logger.debug("\n\nXML格式:\n\n{}\n", sw.toString());
		return response;
	}

	public static void main(String[] args) {
		SfApi api = new SfApi();
		String genXML = "";

		Response response = api.getItemQueryServiceResponseObj(RT_INVENTORY_QUERY_SERVICE_RESPONSE);

		// genXML = api.getItemQueryServiceResponseObj("");

		/* 不可發送 */
		// api.sendXML(genXML);

		// genXML = api.genItemQueryService();
		// api.sendXML(genXML);

		// genXML = api.genPurchaseOrderService();
		// api.sendXML(genXML);

		// api.codeTest();

		// genXML = api.genItemService();
		// api.sendXMLbyWS(genXML);

		// genXML = api.genPurchaseOrderService();
		// api.sendXMLbyWS("http://192.168.112.164:8090", genXML);
	}
}
