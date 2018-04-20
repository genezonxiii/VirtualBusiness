package tw.com.aber.inv.controller;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.StringReader;
import java.io.StringWriter;
import java.math.BigDecimal;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.UnknownHostException;
import java.sql.Time;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.xml.bind.JAXB;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.google.gson.Gson;

import tw.com.aber.inv.vo.B;
import tw.com.aber.inv.vo.Index;
import tw.com.aber.inv.vo.Invoice;
import tw.com.aber.inv.vo.InvoiceData;
import tw.com.aber.vo.GroupVO;
import tw.com.aber.vo.SaleVO;

public class InvoiceApi {
	private static final Logger logger = LogManager.getLogger(InvoiceApi.class);

	/**
	 * <p>A01發票號碼取號 [Request]
	 * 
	 * @return
	 */
	public String genRequestForA01() {
		String result = null;
		Index index = new Index();

		SimpleDateFormat dt = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String setSysTime = dt.format(new Date());

		index.setFunctionCode("A01");
		index.setSellerId("20939790");
		index.setPosId("1");
		index.setPosSn("9dcd1bbf10201d542698");
		index.setSysTime(setSysTime);

		StringWriter sw = new StringWriter();
		JAXB.marshal(index, sw);
		logger.debug(sw.toString());
		result = sw.toString();
		return result;
	}

	/**
	 * <p>A01發票號碼取號 [Response]
	 * 
	 * @return
	 */
	public String genResponseForA01() {
		String result = null;
		Index index = new Index();
		index.setFunctionCode("A01");
		index.setReply("1");
		index.setMessage("成功");
		index.setSellerId("23123123");
		index.setPosId("1");
		index.setPosSn("JXLHDWN");
		index.setSysTime("2013-06-04 16:32:06");
		index.setUserId("CronUpdateInvoiceNum");
		index.setTaxMonth("10206");
		index.setInvoiceHeader("UQ");
		index.setInvoiceStart("$$573250");
		index.setInvoiceEnd("44573499");

		StringWriter sw = new StringWriter();
		JAXB.marshal(index, sw);
		logger.debug(sw.toString());
		result = sw.toString();
		return result;
	}

	/**
	 * <p>A02發票字軌取號訊息規格(查詢當期全部發票號碼) [Request]
	 * 
	 * @return
	 */
	public String genRequestForA02() {
		String result = null;
		Invoice invoice = new Invoice();

		SimpleDateFormat dt = new SimpleDateFormat("yyyy-mm-dd HH:mm:ss");
		String setSysTime = dt.format(new Date());

		invoice.setInvoiceCode("A02");
		invoice.setSellerId("20939790");
		invoice.setPosId("1");
		invoice.setPosSn("9dcd1bbf10201d542698");
		invoice.setSysTime(setSysTime);

		StringWriter sw = new StringWriter();
		JAXB.marshal(invoice, sw);
		logger.debug(sw.toString());
		result = sw.toString();
		return result;
	}

	/**
	 * <p>A02發票字軌取號訊息規格(查詢當期全部發票號碼) [Response]
	 * 
	 * @return
	 */
	public String genResponseForA02() {
		String result = null;
		Invoice invoice = new Invoice();
		invoice.setInvoiceCode("A02");
		invoice.setSellerId("12345678");
		invoice.setPosId("1");
		invoice.setPosSn("HKH1238LS0C3X0WSDG3M1");
		invoice.setSysTime("2014-03-25 13:59 01:45:22");
		invoice.setReply("1");
		invoice.setMessage("成功");

		InvoiceData invoiceData1 = new InvoiceData();
		invoiceData1.setTaxMonth("10304");
		invoiceData1.setType("03");
		invoiceData1.setInvoiceHeader("GG");
		invoiceData1.setInvoiceStart("01234567");
		invoiceData1.setInvoiceEnd("01234567");

		InvoiceData invoiceData2 = new InvoiceData();
		invoiceData2.setTaxMonth("10304");
		invoiceData2.setType("01");
		invoiceData2.setInvoiceHeader("TT");
		invoiceData2.setInvoiceStart("10031000");
		invoiceData2.setInvoiceEnd("10041000");

		List<InvoiceData> invoiceDatas = new ArrayList<InvoiceData>();
		invoiceDatas.add(invoiceData1);
		invoiceDatas.add(invoiceData2);

		invoice.setInvoiceDatas(invoiceDatas);

		StringWriter sw = new StringWriter();
		JAXB.marshal(invoice, sw);
		logger.debug(sw.toString());
		result = sw.toString();
		return result;
	}

	/**
	 * <p>A03上傳當期發票字軌資料 [Request]
	 * 
	 * @return
	 */
	public String genRequestForA03() {
		String result = null;
		Index index = new Index();

		SimpleDateFormat dt = new SimpleDateFormat("yyyy-mm-dd HH:mm:ss");
		String setSysTime = dt.format(new Date());

		index.setInvoiceCode("A03");
		index.setSellerId("20939790");
		index.setPosId("1");
		index.setPosSn("9dcd1bbf10201d542698");
		index.setSysTime(setSysTime);

		InvoiceData invoiceData1 = new InvoiceData();
		invoiceData1.setSellerId("20939790");
		invoiceData1.setType("03");
		invoiceData1.setTypeName("二聯式收銀機");
		invoiceData1.setTaxMonth("10606");
		invoiceData1.setInvoiceHeader("AA");
		invoiceData1.setInvoiceStart("10000001");
		invoiceData1.setInvoiceEnd("10000250");

		InvoiceData invoiceData2 = new InvoiceData();
		invoiceData2.setSellerId("20939790");
		invoiceData2.setType("03");
		invoiceData2.setTypeName("二聯式收銀機");
		invoiceData2.setTaxMonth("10608");
		invoiceData2.setInvoiceHeader("AA");
		invoiceData2.setInvoiceStart("10000001");
		invoiceData2.setInvoiceEnd("10000250");

		List<InvoiceData> invoiceDatas = new ArrayList<InvoiceData>();
		invoiceDatas.add(invoiceData1);
		invoiceDatas.add(invoiceData2);

		index.setInvoiceData(invoiceDatas);

		StringWriter sw = new StringWriter();
		JAXB.marshal(index, sw);
		logger.debug(sw.toString());
		result = sw.toString();
		return result;
	}

	/**
	 * <p>A03上傳當期發票字軌資料 [Response]
	 * 
	 * @return
	 */
	public String genResponseForA03() {
		String result = null;
		Index index = new Index();
		index.setInvoiceCode("A03");
		index.setSellerId("12345678");
		index.setPosId("1");
		index.setPosSn("sdfj2owjrls");
		index.setSysTime("2014-07-24 16:48:22");
		index.setReply("1");
		index.setMessage("成功");

		StringWriter sw = new StringWriter();
		JAXB.marshal(index, sw);
		logger.debug(sw.toString());
		result = sw.toString();
		return result;
	}

	/**
	 * <p>A04發票字軌取號訊息規格(查詢總公司其下門市當期全部字軌資料) [Request]
	 * 
	 * @return
	 */
	public String genRequestForA04() {
		String result = null;
		Invoice invoice = new Invoice();

		SimpleDateFormat dt = new SimpleDateFormat("yyyy-mm-dd HH:mm:ss");
		String setSysTime = dt.format(new Date());

		invoice.setInvoiceCode("A04");
		invoice.setSellerId("20939790");
		invoice.setPosId("1");
		invoice.setPosSn("9dcd1bbf10201d542698");
		invoice.setSysTime(setSysTime);

		StringWriter sw = new StringWriter();
		JAXB.marshal(invoice, sw);
		logger.debug(sw.toString());
		result = sw.toString();
		return result;
	}

	/**
	 * <p>B01發票號碼更新 [Request]
	 * 
	 * @return
	 */
	public String genRequestForB01() {
		String result = null;
		Index index = new Index();

		SimpleDateFormat dt = new SimpleDateFormat("yyyy-mm-dd HH:mm:ss");
		String setSysTime = dt.format(new Date());

		index.setFunctionCode("B01");
		index.setSellerId("20939790");
		index.setPosId("1");
		index.setPosSn("9dcd1bbf10201d542698");
		index.setSysTime(setSysTime);

		index.setTaxMonth("10606");
		index.setInvoiceHeader("AA");
		index.setInvoiceStart("10000001");
		index.setInvoiceEnd("10000250");
		index.setInvoiceNumber("10000001");

		StringWriter sw = new StringWriter();
		JAXB.marshal(index, sw);
		logger.debug(sw.toString());
		result = sw.toString();
		return result;
	}

	/**
	 * <p>C01發票號碼取號(取下一期發票號碼) [Request]
	 * 
	 * @return
	 */
	public String genRequestForC01() {
		String result = null;
		Index index = new Index();

		SimpleDateFormat dt = new SimpleDateFormat("yyyy-mm-dd HH:mm:ss");
		String setSysTime = dt.format(new Date());

		index.setFunctionCode("C01");
		index.setSellerId("20939790");
		index.setPosId("1");
		index.setPosSn("9dcd1bbf10201d542698");
		index.setSysTime(setSysTime);

		index.setTaxMonth("10606");
		index.setInvoiceHeader("AA");
		index.setInvoiceStart("10000001");
		index.setInvoiceEnd("10000250");
		index.setInvoiceNumber("10000001");

		StringWriter sw = new StringWriter();
		JAXB.marshal(index, sw);
		logger.debug(sw.toString());
		result = sw.toString();
		return result;
	}

	/**
	 * <p>C02發票字軌取號訊息規格(查詢下一期全部發票號碼) [Request]
	 * 
	 * @return
	 */
	public String genRequestForC02() {
		String result = null;
		Invoice invoice = new Invoice();

		SimpleDateFormat dt = new SimpleDateFormat("yyyy-mm-dd HH:mm:ss");
		String setSysTime = dt.format(new Date());

		invoice.setInvoiceCode("C02");
		invoice.setSellerId("20939790");
		invoice.setPosId("1");
		invoice.setPosSn("9dcd1bbf10201d542698");
		invoice.setSysTime(setSysTime);

		StringWriter sw = new StringWriter();
		JAXB.marshal(invoice, sw);
		logger.debug(sw.toString());
		result = sw.toString();
		return result;
	}

	/**
	 * <p>C03上傳下一期發票字軌資料 [Request]
	 * 
	 * @return
	 */
	public String genRequestForC03() {
		String result = null;
		Index index = new Index();

		SimpleDateFormat dt = new SimpleDateFormat("yyyy-mm-dd HH:mm:ss");
		String setSysTime = dt.format(new Date());

		index.setInvoiceCode("C03");
		index.setSellerId("20939790");
		index.setPosId("1");
		index.setPosSn("9dcd1bbf10201d542698");
		index.setSysTime(setSysTime);

		InvoiceData invoiceData1 = new InvoiceData();
		invoiceData1.setSellerId("20939790");
		invoiceData1.setType("03");
		invoiceData1.setTypeName("二聯式收銀機");
		invoiceData1.setTaxMonth("10606");
		invoiceData1.setInvoiceHeader("AA");
		invoiceData1.setInvoiceStart("10000001");
		invoiceData1.setInvoiceEnd("10000250");

		InvoiceData invoiceData2 = new InvoiceData();
		invoiceData2.setSellerId("20939790");
		invoiceData2.setType("03");
		invoiceData2.setTypeName("二聯式收銀機");
		invoiceData2.setTaxMonth("10608");
		invoiceData2.setInvoiceHeader("AA");
		invoiceData2.setInvoiceStart("10000001");
		invoiceData2.setInvoiceEnd("10000250");

		List<InvoiceData> invoiceDatas = new ArrayList<InvoiceData>();
		invoiceDatas.add(invoiceData1);
		invoiceDatas.add(invoiceData2);

		index.setInvoiceData(invoiceDatas);

		StringWriter sw = new StringWriter();
		JAXB.marshal(index, sw);
		logger.debug(sw.toString());
		result = sw.toString();
		return result;
	}

	/**
	 * <p>C04發票字軌取號訊息規格(查詢總公司旗下門市下一期全部字軌資料) [Request]
	 * 
	 * @return
	 */
	public String genRequestForC04() {
		String result = null;
		Invoice invoice = new Invoice();

		SimpleDateFormat dt = new SimpleDateFormat("yyyy-mm-dd HH:mm:ss");
		String setSysTime = dt.format(new Date());

		invoice.setInvoiceCode("C04");
		invoice.setSellerId("20939790");
		invoice.setPosId("1");
		invoice.setPosSn("9dcd1bbf10201d542698");
		invoice.setSysTime(setSysTime);

		StringWriter sw = new StringWriter();
		JAXB.marshal(invoice, sw);
		logger.debug(sw.toString());
		result = sw.toString();
		return result;
	}

	/**
	 * <p>Y01取系統時間 [Request]
	 * 
	 * @return
	 */
	public String genRequestForY01() {
		String result = null;
		Index index = new Index();

		SimpleDateFormat dt = new SimpleDateFormat("yyyy-mm-dd HH:mm:ss");
		String setSysTime = dt.format(new Date());

		index.setFunctionCode("Y01");
		index.setSellerId("20939790");
		index.setPosId("1");
		index.setPosSn("9dcd1bbf10201d542698");
		index.setSysTime(setSysTime);

		StringWriter sw = new StringWriter();
		JAXB.marshal(index, sw);
		logger.debug(sw.toString());
		result = sw.toString();
		return result;
	}

	/**
	 * <p>D01總分支機構抓取旗下門市通道金鑰 [Request]
	 * 
	 * @return
	 */
	public String genRequestForD01() {
		String result = null;
		Index index = new Index();

		SimpleDateFormat dt = new SimpleDateFormat("yyyy-mm-dd HH:mm:ss");
		String setSysTime = dt.format(new Date());

		index.setInvoiceCode("D01");
		index.setSellerId("20939790");
		index.setPosId("1");
		index.setPosSn("9dcd1bbf10201d542698");
		index.setSysTime(setSysTime);

		StringWriter sw = new StringWriter();
		JAXB.marshal(index, sw);
		logger.debug(sw.toString());
		result = sw.toString();
		return result;
	}
	
	/**
	 * <p>依order_no來分類SaleVO
	 * 
	 * @param saleVOsAll
	 * @return
	 */
	public List<List<SaleVO>> classification(List<SaleVO> saleVOsAll) {
		List<List<SaleVO>> saleVOsAll_classification =new ArrayList<List<SaleVO>>();
		String order_no_record = "";
		List<SaleVO> saleVOs = null ;
		
		for (int i = 0; i < saleVOsAll.size(); i++) {
			SaleVO saleVO = saleVOsAll.get(i);
			// check data
			if ("".equals(saleVO.getInvoice()) || null == saleVO.getInvoice()) {
				logger.error("getInvoice is null");
				return null;
			}
			if(i==0){
				saleVOs = new ArrayList<SaleVO>();
				order_no_record=saleVO.getOrder_no();
			}

			// classification
			if (!order_no_record.equals(saleVO.getOrder_no())) {
				order_no_record=saleVO.getOrder_no();
				saleVOsAll_classification.add(saleVOs);
				saleVOs = new ArrayList<SaleVO>();
			}

			saleVOs.add(saleVO);

			if (i == saleVOsAll.size()-1) {
				saleVOsAll_classification.add(saleVOs);
			}
		}
		
		return saleVOsAll_classification;
	}
	
	/**
	 * <p>取得列印發票字串
	 * 
	 * @param saleVOsAll
	 * @param groupVO
	 * @return
	 */
	public List<String> getPrintStr(List<SaleVO> saleVOsAll, GroupVO groupVO) {
		List<String> printStrList = new ArrayList<String>();
		List<List<SaleVO>> saleVOsAll_classification = classification(saleVOsAll);

		if (saleVOsAll_classification == null) {
			return null;
		}

		for (int i = 0; i < saleVOsAll_classification.size(); i++) {
			List<SaleVO> saleVOs = saleVOsAll_classification.get(i);
			SaleVO saleVO=saleVOs.get(0);
			String invoiceNum = saleVO.getInvoice();
			Date invoice_date = saleVO.getInvoice_date();
			String invoice_vcode = saleVO.getInvoice_vcode();
			Time invoice_time =saleVO.getInvoice_time();
			
			String xmlStr = genRequestForC0401(invoiceNum, saleVOs, groupVO);

			xmlStr=this.getInvoicePrintXml(xmlStr, invoice_date, invoice_time, invoice_vcode);
			
			logger.debug("printxmlStr: "+xmlStr);
			
			printStrList.add(xmlStr);

		}

		return printStrList;
	}
	
	

	/**
	 * <p>C0401開立發票訊息規格 [Request]
	 * 
	 * @param invoiceNum
	 * @param saleVOs
	 * @param groupVO
	 * @return
	 */
	public String genRequestForC0401(String invoiceNum, List<SaleVO> saleVOs, GroupVO groupVO) {
		String result = null;
		Invoice invoice = new Invoice();

		SimpleDateFormat dt1 = new SimpleDateFormat("yyyy-MM-dd");
		SimpleDateFormat dt2 = new SimpleDateFormat("HH:mm:ss");
		SimpleDateFormat dt3 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		
		Date date = new Date();
		String ymd = dt1.format(date);
		String hms = dt2.format(date);
		String ymdhms = dt3.format(date);

		int randomPIN = (int) (Math.random() * 9000) + 1000;
		String PINString = String.valueOf(randomPIN);

		invoice.setA1("C0401");// 訊息類型
		invoice.setA2(invoiceNum);// 發票號碼
		invoice.setA3(ymd);// 發票開立日期
		invoice.setA4(hms);// 發票開立時間
		invoice.setA5("0000000000");// buyer識別碼(
		invoice.setA6("0000");// buyer名稱
		invoice.setA16(saleVOs.get(0).getSaleExtVO().getCreditCard());
		invoice.setA19("");// 核准日
		invoice.setA20("");// 核准文
		invoice.setA21("");// 核准號
		invoice.setA22("07");// 發票類別
		invoice.setA24("0");// 捐贈註記
		if (saleVOs.get(0).getSaleExtVO() != null && !saleVOs.get(0).getSaleExtVO().getEmail().equals("")) {
			invoice.setA25("EM0015");// 載具類別號碼
			invoice.setA26(saleVOs.get(0).getSaleExtVO().getEmail());// 載具顯碼id
			invoice.setA27(saleVOs.get(0).getSaleExtVO().getEmail());// 載具隱碼id
			invoice.setA28("N");// 紙本電子發票已列印標記
		} else {
			invoice.setA28("Y");// 紙本電子發票已列印標記
		}
		invoice.setA30(PINString);// 發票防偽隨機碼

		List<B> bList = new ArrayList<B>();

		B b = null;
		BigDecimal c1, quantity, price, multiplyNum;
		BigDecimal c1Total = new BigDecimal("0");// 應稅銷售額合計
		BigDecimal sum = new BigDecimal("0");// 應稅總計金額

		for (int i = 0; i < saleVOs.size(); i++) {

			/*
			 * 取得數量及單價，相乘得出總價，再計算得出該筆的應稅銷售額，逐一加入得出合計
			 */
			quantity = new BigDecimal(saleVOs.get(i).getQuantity());
			price = new BigDecimal(saleVOs.get(i).getPrice());

			multiplyNum = quantity.multiply(price);

			c1 = multiplyNum.divide(new BigDecimal("1.05"), 0, BigDecimal.ROUND_HALF_DOWN);

			c1Total = c1Total.add(c1);
			sum = sum.add(multiplyNum);

			b = new B();
			b.setB1(String.valueOf(i + 1));// 商品項目資料
			b.setB2(groupVO.getInv_product_name());// 品名
			b.setB3(String.valueOf(saleVOs.get(i).getQuantity()));// 數量
			b.setB5(String.valueOf(saleVOs.get(i).getPrice().intValue()));// 單價
			b.setB6(String.valueOf(multiplyNum));// 金額
			b.setB7(String.valueOf(i + 1));// 明細排列序號
			bList.add(b);
		}
		invoice.setB(bList);
		
		invoice.setC1(String.valueOf(c1Total));// 應稅銷售額合計(新台幣)
		invoice.setC2("0");// 免稅銷售額合計(新台幣)
		invoice.setC3("0");// 零稅率銷售額合計(新台幣)
		invoice.setC4("1");// 課稅別
		invoice.setC5("0.05");// 稅率
		invoice.setC6(String.valueOf(sum.subtract(c1Total)));// 營業稅額
		invoice.setC7(String.valueOf(sum));// 總計
		invoice.setC12("訂單編號：" + saleVOs.get(0).getOrder_no());
		invoice.setC13("訂單編號：" + saleVOs.get(0).getOrder_no());
		
		//買方統編有填寫
		if(true){
			invoice.setC1(String.valueOf(sum));// 應稅銷售額合計(新台幣)
			invoice.setC6("0");
			invoice.setC7(String.valueOf(sum));// 總計
		}

		invoice.setD1(groupVO.getGroup_unicode());// seller識別碼(統一編號)
		invoice.setD2(groupVO.getInvoice_key());// sellerPOSSN(POS機出廠序號)(通道金鑰)
		invoice.setD3(groupVO.getInvoice_posno());// POSID(POS機編號)
		invoice.setD4(ymdhms);// XML產生時間

		StringWriter sw = new StringWriter();
		JAXB.marshal(invoice, sw);
		logger.debug(sw.toString());
		result = sw.toString();
		return result;
	}
	
	/**
	 * <p>C0501開立發票訊息規格 [Request]
	 * 
	 * @param reason
	 * @param saleVOs
	 * @param groupVO
	 * @return
	 */
	public String genRequestForC0501(String reason, List<SaleVO> saleVOs, GroupVO groupVO) {
		String result = null;
		Invoice invoice = new Invoice();
		String invoiceNum = saleVOs.get(0).getInvoice();
		Date invoice_date = saleVOs.get(0).getInvoice_date();

		SimpleDateFormat dt1 = new SimpleDateFormat("yyyy-MM-dd");
		SimpleDateFormat dt2 = new SimpleDateFormat("HH:mm:ss");
		SimpleDateFormat dt3 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Date date = new Date();
		String ymd = dt1.format(date);
		String hms = dt2.format(date);
		String ymdhms = dt3.format(date);
		String ymd_invoice = dt1.format(invoice_date);

		invoice.setInvoiceCode("C0501");
		invoice.setPosSn(groupVO.getInvoice_key());// POS機出廠序號;
		invoice.setPosId(groupVO.getInvoice_posno());
		invoice.setInvoiceNumber(invoiceNum);
		invoice.setInvoiceDate(ymd_invoice);
		invoice.setBuyerId("0000000000");
		invoice.setSellerId(groupVO.getGroup_unicode());
		invoice.setCancelDate(ymd);
		invoice.setCancelTime(hms);
		invoice.setCancelReason(reason);// 作廢原因
		invoice.setSysTime(ymdhms);// 系統時間

		StringWriter sw = new StringWriter();
		JAXB.marshal(invoice, sw);
		logger.debug(sw.toString());
		result = sw.toString();
		return result;
	}
	

	/**
	 * <p>取得列印發票xml號碼
	 * 
	 * @param invoiceXml
	 * @param invoice_date
	 * @param invoice_time
	 * @param invoice_vcode
	 * @return
	 */
	public String getInvoicePrintXml(String invoiceXml,Date invoice_date,Time invoice_time,String invoice_vcode) {
		
		Invoice invoice=JAXB.unmarshal(new StringReader(invoiceXml), Invoice.class);

		SimpleDateFormat dt1 = new SimpleDateFormat("yyyy-MM-dd");
		SimpleDateFormat dt2 = new SimpleDateFormat("HH:mm:ss");
		
		String ymd = dt1.format(invoice_date);
		String hms = dt2.format(invoice_time);

		invoice.setA3(ymd);// 發票開立日期
		invoice.setA4(hms);// 發票開立時間
		invoice.setA30(invoice_vcode);// 發票防偽隨機碼
		
		StringWriter sw = new StringWriter();
		JAXB.marshal(invoice, sw);
		sw.toString();
		
		return sw.toString();
	}
	
	/**
	 * <p>取得C0401發票驗證碼
	 * 
	 * @param invoiceXml
	 * @return
	 */
	public String getInvoiceInvoiceVcode(String invoiceXml) {
		Invoice invoice=JAXB.unmarshal(new StringReader(invoiceXml), Invoice.class);
		return invoice.getA30();
	}
	
	/**
	 * <p>取得C0401發票開立時間
	 * 
	 * @param invoiceXml
	 * @return
	 */
	public String getInvoiceInvoice_time(String invoiceXml) {
		Invoice invoice=JAXB.unmarshal(new StringReader(invoiceXml), Invoice.class);
		return invoice.getA4();
	}
	
	/**
	 * <p>電文加密前置作業
	 * 
	 * @param reqXml
	 * @return
	 */
	public String sendXML(String reqXml) {
		String targetURL = "http://xmltest.551.com.tw";
		String urlParameters = "";

		urlParameters = reqXml;
		
		String returnValue = InvoiceApi.executePost(targetURL, urlParameters);
		logger.debug("returnValue:" + returnValue);
		return returnValue;
	}

	/**
	 * <p>透過HTTP POST發送電文
	 * 
	 * @param targetURL
	 * @param urlParameters
	 * @return
	 */
	public static String executePost(String targetURL, String urlParameters) {
		HttpURLConnection connection = null;
		StringBuilder response = null;
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
			BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(wr, "UTF-8"));
			writer.write(urlParameters);
			writer.close();
			wr.close();

			// Get Response
			InputStream is = connection.getInputStream();
			BufferedReader rd = new BufferedReader(new InputStreamReader(is, "UTF-8"));

			response = new StringBuilder();
			String line;
			while ((line = rd.readLine()) != null) {
				response.append(line);
				response.append('\r');
			}
			rd.close();
		} catch (UnknownHostException e) {
			logger.error("發送失敗：" + e.getMessage());
		} catch (Exception e) {
			logger.error("發送失敗：" + e.getMessage());
		} finally {
			if (connection != null) {
				connection.disconnect();
			}
		}
		return response.toString();
	}

	/**
	 * <p>回覆電文解析為物件
	 * 
	 * @param resXml
	 * @return
	 */
	public Index getIndexResponse(String resXml) {
		Index index = null;
		StringReader reader = null;
		try {
			resXml = resXml.trim();
			reader = new StringReader(resXml);
			StringWriter sw = new StringWriter();

			// JAXBContext jaxbContext = JAXBContext.newInstance(Index.class);
			// Unmarshaller unmarshaller = jaxbContext.createUnmarshaller();
			// index = (Index) unmarshaller.unmarshal(reader);

			index = JAXB.unmarshal(reader, Index.class);

			String json = new Gson().toJson(index);
			JAXB.marshal(index, sw);
			String xml = sw.toString();
		} catch (Exception e) {
			e.printStackTrace();
			logger.debug(e.getMessage());
		}
		return index;
	}

	/**
	 * <p>回覆電文解析為物件
	 * 
	 * @param resXml
	 * @return
	 */
	public Invoice getInvoiceResponse(String resXml) {
		Invoice invoice = null;
		StringReader reader = null;

		try {
			resXml = resXml.trim();
			reader = new StringReader(resXml);
			invoice = JAXB.unmarshal(reader, Invoice.class);

			String json = new Gson().toJson(invoice);

			StringWriter sw = new StringWriter();
			JAXB.marshal(invoice, sw);
			String xml = sw.toString();

			logger.debug(json);
			logger.debug(xml);
		} catch (Exception e) {
			logger.debug(e.getMessage());
		}
		return invoice;
	}

	public static void main(String[] args) {
		InvoiceApi api = new InvoiceApi();
		String resXml = null;
		String reqXml = null;

		// reqXml = api.genRequestForA01();
		// reqXml = api.genRequestForA02();
		// reqXml = api.genRequestForA03();
		// reqXml = api.genRequestForA04();
		// reqXml = api.genRequestForB01();
		// reqXml = api.genRequestForC01();
		// reqXml = api.genRequestForC02();
		// reqXml = api.genRequestForC03();
		// reqXml = api.genRequestForC04();
		// reqXml = api.genRequestForY01();
		// reqXml = api.genRequestForD01();

		resXml = api.sendXML(reqXml);

		api.getIndexResponse(resXml);
		// api.getInvoiceResponse(resXml);
	}
}
