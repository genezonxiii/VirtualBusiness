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
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.xml.bind.JAXB;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.google.gson.Gson;

import tw.com.aber.product.controller.product.ProductBean;
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
import tw.com.aber.sf.vo.OrderCarrier;
import tw.com.aber.sf.vo.OrderItem;
import tw.com.aber.sf.vo.OrderItems;
import tw.com.aber.sf.vo.OrderReceiverInfo;
import tw.com.aber.sf.vo.OrderSenderInfo;
import tw.com.aber.sf.vo.PurchaseOrder;
import tw.com.aber.sf.vo.PurchaseOrderInboundRequest;
import tw.com.aber.sf.vo.PurchaseOrderRequest;
import tw.com.aber.sf.vo.PurchaseOrders;
import tw.com.aber.sf.vo.RTInventory;
import tw.com.aber.sf.vo.RTInventoryQueryRequest;
import tw.com.aber.sf.vo.RTInventorys;
import tw.com.aber.sf.vo.Request;
import tw.com.aber.sf.vo.Response;
import tw.com.aber.sf.vo.ResponseFail;
import tw.com.aber.sf.vo.ResponseUtil;
import tw.com.aber.sf.vo.SaleOrder;
import tw.com.aber.sf.vo.SaleOrderOutboundDetailRequest;
import tw.com.aber.sf.vo.SaleOrderRequest;
import tw.com.aber.sf.vo.SaleOrders;
import tw.com.aber.sf.vo.SfBomItem;
import tw.com.aber.sf.vo.SfBomItems;
import tw.com.aber.sf.vo.SfContainer;
import tw.com.aber.sf.vo.SfItem;
import tw.com.aber.sf.vo.SkuNoList;
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
	
	/******************
	 * 商品接口
	 * 
	 * ***************/
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
			item.setQtymin(String.valueOf( product.getKeep_stock() )); 
			item.setContainers(containers);
			//要求掃描序列號
			item.setSerialNumTrackInbound("Y");
			item.setSerialNumTrackInventory("Y");
			item.setSerialNumTrackOutbound("Y");

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
		mainXML.setLang("zh-CN");
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
	
	/******************
	 * 商品接口(組合包)
	 * 
	 ******************/
	public String genItemServiceForPackage(List<tw.com.aber.vo.PackageVO> packageVOList, ValueService valueService) {
		List<SfItem> itemList = new ArrayList<SfItem>();

		for (int i = 0; i < packageVOList.size(); i++) {
			tw.com.aber.vo.PackageVO packageVO = packageVOList.get(i);

			SfItem item = new SfItem();
			item.setSkuNo(packageVO.getC_package_id());
			item.setItemName(packageVO.getPackage_name());

			// Containers
			SfContainer container = new SfContainer();
			// 先暫時放id 等之後再做查詢
			container.setPackUm("CS");
			// product.getUnit_id()

			Containers containers = new Containers();
			containers.setContainer(container);

			// item1
			BarCode barCode = new BarCode();
			barCode.setBarCode1(packageVO.getBarcode());

			// xxx
			item.setSerialNumTrackInbound("N");
			item.setSerialNumTrackInventory("N");
			item.setSerialNumTrackOutbound("N");
			item.setBarCode(barCode);
			item.setContainers(containers);

			item.setBomAction("Y");
			itemList.add(item);

		}

		String result;

		Items items = new Items();
		items.setItemList(itemList);

		ItemRequest itemRequest = new ItemRequest();

		GroupSfVO groupSfVo = valueService.getGroupSfVO();

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
		mainXML.setLang("zh-CN");
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

	/**********************
	 * 商品查詢接口
	 * 
	 **********************/
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
		mainXML.setLang("zh-CN");
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
	
	/**********************
	 * 商品查詢接口(組合包)
	 * 
	 **********************/
	public String genItemQueryService(String [] arr_C_product_id, ValueService valueService) {
		String result;

		GroupSfVO groupSfVo = valueService.getGroupSfVO();

		List<String> skuNo = new ArrayList<String>();

		for (int i = 0; i < arr_C_product_id.length; i++) {
			
			skuNo.add(arr_C_product_id[i]);
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
		mainXML.setLang("zh-CN");
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

	/**********************
	 * 入庫單接口
	 * 
	 **********************/	
	public String genPurchaseOrderService(List<PurchaseVO> purchaseList, ValueService valueService) {
		String result;

		GroupSfVO groupSfVo = valueService.getGroupSfVO();
		WarehouseVO warehouseVO = valueService.getWarehouseVO();

		List<PurchaseOrder> purchaseOrderList = new ArrayList<PurchaseOrder>();

		for (int i = 0; i < purchaseList.size(); i++) {
			PurchaseVO purchaseVO = purchaseList.get(i);
			List<PurchaseDetailVO> purchaseDetailList = purchaseVO.getPurchaseDetailList();

			PurchaseOrder purchaseOrder = new PurchaseOrder();
			List<SfItem> itemList = new ArrayList<SfItem>();
			Items items = new Items();
			
			if (purchaseDetailList != null) {
				for (int j = 0; j < purchaseDetailList.size(); j++) {
					PurchaseDetailVO purchaseDetailVO = purchaseDetailList.get(j);
					SfItem item = new SfItem();

					item.setSkuNo(purchaseDetailVO.getC_product_id());
					item.setQty(purchaseDetailVO.getQuantity() == null ?
							null : purchaseDetailVO.getQuantity().toString());
					//入庫暫定為正品
					item.setInventoryStatus("10");
					item.setLot(purchaseVO.getSeq_no());
					
					logger.debug("purchaseDetailVO");
					logger.debug("c_product_id:" + purchaseDetailVO.getC_product_id());
					logger.debug("quantity:" + purchaseDetailVO.getQuantity());
					
					itemList.add(item);
				}
				items.setItemList(itemList);
			}
			
			purchaseOrder.setErpOrder(purchaseVO.getSeq_no());
			purchaseOrder.setErpOrderType("10");
			purchaseOrder.setsFOrderType("采购入库");
			String date = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
			purchaseOrder.setScheduledReceiptDate(date);
			purchaseOrder.setVendorCode(groupSfVo.getVendor_code());
			purchaseOrder.setWarehouseCode(warehouseVO.getSf_warehouse_code());
			purchaseOrder.setItems(items);

			purchaseOrderList.add(purchaseOrder);
		}

		PurchaseOrders purchaseOrders = new PurchaseOrders();
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
		mainXML.setLang("zh-CN");
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
	
	/**********************
	 * 入庫單明細查詢接口
	 * 
	 **********************/	
	public String genPurchaseOrderInboundQueryService(List<PurchaseVO> purchaseList, ValueService valueService) {
		String result;

		List<PurchaseOrder> purchaseOrderList = new ArrayList<PurchaseOrder>();

		GroupSfVO groupSfVo = valueService.getGroupSfVO();
		WarehouseVO warehouseVO = valueService.getWarehouseVO();

		for (int i = 0; i < purchaseList.size(); i++) {
			PurchaseVO purchaseVO = purchaseList.get(i);

			PurchaseOrder purchaseOrder = new PurchaseOrder();
			purchaseOrder.setWarehouseCode(warehouseVO.getSf_warehouse_code());
			purchaseOrder.setErpOrder(purchaseVO.getSeq_no());
			purchaseOrderList.add(purchaseOrder);
		}

		PurchaseOrders purchaseOrders = new PurchaseOrders();
		purchaseOrders.setPurchaseOrder(purchaseOrderList);

		PurchaseOrderInboundRequest purchaseOrderInboundRequest = new PurchaseOrderInboundRequest();
		purchaseOrderInboundRequest.setCompanyCode(groupSfVo.getCompany_code());
		purchaseOrderInboundRequest.setPurchaseOrders(purchaseOrders);

		// head, body
		Head head = new Head();
		head.setAccessCode(groupSfVo.getAccess_code());
		head.setCheckword(groupSfVo.getCheck_word());

		Body body = new Body();
		body.setPurchaseOrderInboundRequest(purchaseOrderInboundRequest);

		Request mainXML = new Request();
		mainXML.setService("PURCHASE_ORDER_INBOUND_QUERY_SERVICE");
		mainXML.setLang("zh-CN");
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

	/**********************
	 * 入庫取消接口
	 * 
	 **********************/	
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
		mainXML.setLang("zh-CN");
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

	/**********************
	 * 出庫單接口
	 * 
	 **********************/	
	public String genSaleOrderService(List<ShipVO> shipList, ValueService valueService) {
		String result;

		// 使用內部類別的function
		GroupSfVO groupSfVo = valueService.getGroupSfVO();
		WarehouseVO warehouseVo = valueService.getWarehouseVO();

		logger.debug("genSaleOrderService:" + shipList.size());

		List<OrderItem> orderItemList = null;
		List<SaleOrder> saleOrderList = new ArrayList<SaleOrder>();

		for (int i = 0; i < shipList.size(); i++) {
			OrderItems orderItems = new OrderItems();
			logger.debug("i:" + i);
			ShipVO shipVO = shipList.get(i);
			List<ShipDetail> shipDetailList = shipVO.getShipDetail();

			orderItemList = new ArrayList<OrderItem>();
			for (int j = 0; j < shipDetailList.size(); j++) {
				ShipDetail shipDetail = shipDetailList.get(j);
				logger.debug("shipDetail:" + shipDetail + shipDetail.getQuantity());
				// item1
				OrderItem orderItem = new OrderItem();
				orderItem.setSkuNo(shipDetail.getC_product_id());
				orderItem.setItemQuantity(shipDetail.getQuantity().toString());
				orderItemList.add(orderItem);
			}
			orderItems.setOrderItem(orderItemList);
			
			OrderReceiverInfo orderReceiverInfo = new OrderReceiverInfo();
			orderReceiverInfo.setReceiverCompany("個人");
			orderReceiverInfo.setReceiverName(shipVO.getV_deliver_name());
			orderReceiverInfo.setReceiverZipCode("");// 郵遞區號暫不填待資料完整
			if (shipVO.getV_deliver_mobile() != null) {
				orderReceiverInfo.setReceiverMobile(shipVO.getV_deliver_mobile());// 手機號碼
			}
			if (shipVO.getV_deliver_phone() != null) {
				orderReceiverInfo.setReceiverPhone(shipVO.getV_deliver_phone());// 電話號碼
			}
			orderReceiverInfo.setReceiverCountry("中国");
			orderReceiverInfo.setReceiverProvince("台灣");
			orderReceiverInfo.setReceiverAddress(shipVO.getDeliver_to());
			
			//配合FromFlag為10時。20時，由順豐系統帶入。
			OrderSenderInfo orderSenderInfo = new OrderSenderInfo();
			orderSenderInfo.setSenderCompany("水魔素");
			orderSenderInfo.setSenderName("王小明");
			orderSenderInfo.setSenderMobile("0912345678");
			orderSenderInfo.setSenderAddress("台北市文湖街18號");

			SaleOrder saleOrder = new SaleOrder();
			OrderCarrier orderCarrier = new OrderCarrier();
			
			/* 
			 * CP:顺丰速运
			 */
			orderCarrier.setCarrier("CP");
			/*
			 * 43:島內件-批(80CM)
			 */
			orderCarrier.setCarrierProduct("43");
			orderCarrier.setMonthlyAccount(groupSfVo.getMonthly_account());
			orderCarrier.setPaymentOfcharge("寄付");

			saleOrder.setOrderCarrier(orderCarrier);
			saleOrder.setWarehouseCode(warehouseVo.getSf_warehouse_code());
			saleOrder.setSfOrderType("销售订单");
			saleOrder.setErpOrder(shipVO.getOrder_no());
			if ( shipVO.getV_dis_date() != null ){
				String deliveryDate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(shipVO.getV_dis_date());
				saleOrder.setDeliveryDate(deliveryDate);
			}
			saleOrder.setErpOrder(shipVO.getOrder_no());
			saleOrder.setOrderReceiverInfo(orderReceiverInfo);
			saleOrder.setOrderItems(orderItems);
			String tradeOrderDateTime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
			saleOrder.setTradeOrderDateTime(tradeOrderDateTime);
			
//			saleOrder.setShopName("水魔素");
//			saleOrder.setFromFlag("10"); 
//			saleOrder.setOrderSenderInfo(orderSenderInfo);
	
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
		mainXML.setLang("zh-CN");
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

	/**********************
	 * 出庫單取消接口
	 * 
	 **********************/
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
		mainXML.setLang("zh-CN");
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

	/**********************
	 * 出庫單明細查詢接口
	 * 
	 **********************/	
	public String genSaleOrderOutboundDetailQueryService(List<ShipVO> shipList, ValueService valueService) {
		String result;
		WarehouseVO warehouseVO = valueService.getWarehouseVO();
		GroupSfVO groupSfVO = valueService.getGroupSfVO();
		List<SaleOrder> saleOrderList = new ArrayList<SaleOrder>();
		for (int i = 0; i < shipList.size(); i++) {
			SaleOrder saleOrder = new SaleOrder();

			String erpOrder = shipList.get(i).getOrder_no();
			saleOrder.setErpOrder(erpOrder);
			saleOrder.setWarehouseCode(warehouseVO.getSf_warehouse_code());
			saleOrderList.add(saleOrder);
		}

		SaleOrders saleOrders = new SaleOrders();
		saleOrders.setSaleOrder(saleOrderList);

		SaleOrderOutboundDetailRequest saleOrderOutboundDetailRequest = new SaleOrderOutboundDetailRequest();
		saleOrderOutboundDetailRequest.setCompanyCode(groupSfVO.getCompany_code());
		saleOrderOutboundDetailRequest.setSaleOrders(saleOrders);

		// head, body
		Head head = new Head();
		head.setAccessCode(groupSfVO.getAccess_code());
		head.setCheckword(groupSfVO.getCheck_word());

		Body body = new Body();
		body.setSaleOrderOutboundDetailRequest(saleOrderOutboundDetailRequest);

		Request mainXML = new Request();
		mainXML.setService("SALE_ORDER_OUTBOUND_DETAIL_QUERY_SERVICE");
		mainXML.setLang("zh-CN");
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

	/**********************
	 * 組合商品接口
	 * 
	 **********************/
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
		mainXML.setLang("zh-CN");
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

	/**********************
	 * 實時庫存查詢接口
	 * 
	 **********************/
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
		rtInventoryQueryRequest.setWarehouseCode(warehouseVO.getSf_warehouse_code());
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
		mainXML.setLang("zh-CN");
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

	/* 
	 * 電文加密前置作業
	 */
	public String sendXML(String reqXml) {
		String targetURL = 
				/*
				 * Testing
				 */
				"http://bsp.sit.sf-express.com:8080/bsp-wms/OmsCommons";
				/*
				 * Production
				 */
//				"http://bsp.sf-express.com/bsp-wms/OmsCommons";
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

	/*
	 * 透過HTTP POST發送電文
	 */
	public static String executePost(String targetURL, String urlParameters) {
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
			BufferedReader rd = new BufferedReader(new InputStreamReader(is, "UTF-8"));
			StringBuilder response = new StringBuilder(); // or StringBuffer if Java version 5+
			String line;
			while ((line = rd.readLine()) != null) {
				response.append(line);
				response.append('\r');
			}
			rd.close();
			return response.toString();
		} catch (UnknownHostException e) {
			logger.error("發送失敗：" + e.getMessage());
			return "<ResponseFail><reasoncode>pershing</reasoncode><remark>電文傳送失敗:" + e.getMessage() + "</remark></ResponseFail>";
		} catch (Exception e) {
			logger.error("發送失敗：" + e.getMessage());
			return "<ResponseFail><reasoncode>pershing</reasoncode><remark>電文傳送失敗" + e.getMessage() + "</remark></ResponseFail>";
		} finally {
			if (connection != null) {
				connection.disconnect();
			}
		}
	}

	/**
	 * @param responseUtil
	 * 
	 * @return whether the return is successful
	 */
	public boolean isTelegraph(ResponseUtil responseUtil) {
		
		boolean result = false;

		if (responseUtil != null && responseUtil.getResponse() != null) {
			result = "OK".equals(responseUtil.getResponse().getHead()) ? true : false;
		}
		return result;
	}

	/**
	 * @param xmlString
	 *            The string to be processed
	 * @return returns a responseUtil object
	 */
	public ResponseUtil getResponseUtilObj(String resXml) {
		ResponseUtil responseUtil = null;
		Response response = null;
		ResponseFail responseFail = null;
		StringWriter sw = new StringWriter();
		try {
			response = JAXB.unmarshal(new StringReader(resXml), Response.class);
			responseUtil = new ResponseUtil();
			String jsonStr = new Gson().toJson(response);
			if (jsonStr.length() == 2) {
				responseFail = JAXB.unmarshal(new StringReader(resXml), ResponseFail.class);
				responseFail = getErrResponseObj(resXml);
				JAXB.marshal(responseFail, sw);
				responseUtil.setResponseFail(responseFail);
			} else {
				JAXB.marshal(response, sw);
				responseUtil.setResponse(response);
			}
		} catch (Exception e) {
			logger.debug("\n\ngetResponseObj err:{}\n", e.getMessage());
		}
		return responseUtil;
	}

	/**
	 * @param xmlString
	 *            The string to be processed
	 * @return returns a response object
	 */
	public Response getResponseObj(String resXml) {
		Response response = null;
		try {
			response = JAXB.unmarshal(new StringReader(resXml), Response.class);
			String jsonStr = new Gson().toJson(response);
			logger.debug("\n\n[Response]\n\nJson格式:\n\n{}\n", jsonStr);
			StringWriter sw = new StringWriter();
			JAXB.marshal(response, sw);
			logger.debug("\n\nXML格式:\n\n{}\n", sw.toString());
		} catch (Exception e) {
			logger.debug("\n\ngetResponseObj err:{}\n", e.getMessage());
		}
		return response;
	}

	/**
	 * @param xmlString
	 *            The string to be processed
	 * @return returns a responseFail object
	 */
	public ResponseFail getErrResponseObj(String resXml) {
		ResponseFail responseFail = null;
		try {
			responseFail = JAXB.unmarshal(new StringReader(resXml), ResponseFail.class);

			logger.debug("\n\nJson格式:\n\n{}\n", new Gson().toJson(responseFail));
			StringWriter sw = new StringWriter();
			JAXB.marshal(responseFail, sw);
			logger.debug("\n\nXML格式:\n\n{}\n", sw.toString());
		} catch (Exception e) {
			logger.debug("\n\ngetErrResponseObj err:{}\n", e.getMessage());
		}
		return responseFail;
	}

	public static void main(String[] args) {
		SfApi api = new SfApi();
		String genXML = "";

		ResponseUtil response = api.getResponseUtilObj(
				"<?xml version=\"1.0\" encoding=\"UTF-8\"?><responseFail><reasoncode>1101</reasoncode><remark>蝟餌??????秤????撘虜</remark></responseFail>");

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
