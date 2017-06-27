package tw.com.aber.inv.controller;

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
import javax.xml.bind.JAXBContext;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.google.gson.Gson;

import tw.com.aber.inv.vo.B;
import tw.com.aber.inv.vo.Index;
import tw.com.aber.inv.vo.Invoice;
import tw.com.aber.inv.vo.InvoiceData;
import tw.com.aber.sftransfer.controller.Md5Base64;
import tw.com.aber.sftransfer.controller.SfApi;
import tw.com.aber.vo.SaleVO;

public class InvoiceApi {
	private static final Logger logger = LogManager.getLogger(InvoiceApi.class);

	/**********************
	 * A01發票號碼取號 [Request]
	 * 
	 **********************/
	public String genRequestForA01() {
		String result = null;
		Index index = new Index();

		SimpleDateFormat dt = new SimpleDateFormat("yyyy-mm-dd HH:mm:ss");
		String setSysTime = dt.format(new Date());

		index.setFunctionCode("A01");
		index.setSellerId("20939790");
		index.setPosId("1");
		index.setPosSn("8220ceffab2327860856");
		index.setSysTime(setSysTime);

		StringWriter sw = new StringWriter();
		JAXB.marshal(index, sw);
		logger.debug(sw.toString());
		result = sw.toString();
		return result;
	}

	/**********************
	 * A01發票號碼取號 [Response]
	 * 
	 **********************/
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

	/**********************
	 * A02發票字軌取號訊息規格(查詢當期全部發票號碼) [Request]
	 * 
	 **********************/
	public String genRequestForA02() {
		String result = null;
		Invoice invoice = new Invoice();

		SimpleDateFormat dt = new SimpleDateFormat("yyyy-mm-dd HH:mm:ss");
		String setSysTime = dt.format(new Date());

		invoice.setInvoiceCode("A02");
		invoice.setSellerId("20939790");
		invoice.setPosId("1");
		invoice.setPosSn("8220ceffab2327860856");
		invoice.setSysTime(setSysTime);

		StringWriter sw = new StringWriter();
		JAXB.marshal(invoice, sw);
		logger.debug(sw.toString());
		result = sw.toString();
		return result;
	}

	/**********************
	 * A02發票字軌取號訊息規格(查詢當期全部發票號碼) [Response]
	 * 
	 **********************/
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

	/**********************
	 * A03上傳當期發票字軌資料 [Request]
	 * 
	 **********************/
	public String genRequestForA03() {
		String result = null;
		Index index = new Index();

		SimpleDateFormat dt = new SimpleDateFormat("yyyy-mm-dd HH:mm:ss");
		String setSysTime = dt.format(new Date());

		index.setInvoiceCode("A03");
		index.setSellerId("20939790");
		index.setPosId("1");
		index.setPosSn("8220ceffab2327860856");
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

	/**********************
	 * A03上傳當期發票字軌資料 [Response]
	 * 
	 **********************/
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

	/**********************
	 * A04發票字軌取號訊息規格(查詢總公司其廈門市當期全部字軌資料) [Request]
	 * 
	 **********************/
	public String genRequestForA04() {
		String result = null;
		Invoice invoice = new Invoice();

		SimpleDateFormat dt = new SimpleDateFormat("yyyy-mm-dd HH:mm:ss");
		String setSysTime = dt.format(new Date());

		invoice.setInvoiceCode("A03");
		invoice.setSellerId("20939790");
		invoice.setPosId("1");
		invoice.setPosSn("8220ceffab2327860856");
		invoice.setSysTime(setSysTime);

		StringWriter sw = new StringWriter();
		JAXB.marshal(invoice, sw);
		logger.debug(sw.toString());
		result = sw.toString();
		return result;
	}

	/**********************
	 * B01發票號碼更新 [Request]
	 * 
	 **********************/
	public String genRequestForB01() {
		String result = null;
		Index index = new Index();

		SimpleDateFormat dt = new SimpleDateFormat("yyyy-mm-dd HH:mm:ss");
		String setSysTime = dt.format(new Date());

		index.setFunctionCode("B01");
		index.setSellerId("20939790");
		index.setPosId("1");
		index.setPosSn("8220ceffab2327860856");
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

	/**********************
	 * C01發票號碼取號(取下一期發票號碼) [Request]
	 * 
	 **********************/
	public String genRequestForC01() {
		String result = null;
		Index index = new Index();

		SimpleDateFormat dt = new SimpleDateFormat("yyyy-mm-dd HH:mm:ss");
		String setSysTime = dt.format(new Date());

		index.setFunctionCode("C01");
		index.setSellerId("20939790");
		index.setPosId("1");
		index.setPosSn("8220ceffab2327860856");
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

	/**********************
	 * C02發票字軌取號訊息規格(查詢下一期全部發票號碼) [Request]
	 * 
	 **********************/
	public String genRequestForC02() {
		String result = null;
		Invoice invoice = new Invoice();

		SimpleDateFormat dt = new SimpleDateFormat("yyyy-mm-dd HH:mm:ss");
		String setSysTime = dt.format(new Date());

		invoice.setInvoiceCode("C02");
		invoice.setSellerId("20939790");
		invoice.setPosId("1");
		invoice.setPosSn("8220ceffab2327860856");
		invoice.setSysTime(setSysTime);

		StringWriter sw = new StringWriter();
		JAXB.marshal(invoice, sw);
		logger.debug(sw.toString());
		result = sw.toString();
		return result;
	}

	/**********************
	 * C03上傳下一期發票字軌資料 [Request]
	 * 
	 **********************/
	public String genRequestForC03() {
		String result = null;
		Index index = new Index();

		SimpleDateFormat dt = new SimpleDateFormat("yyyy-mm-dd HH:mm:ss");
		String setSysTime = dt.format(new Date());

		index.setInvoiceCode("C03");
		index.setSellerId("20939790");
		index.setPosId("1");
		index.setPosSn("8220ceffab2327860856");
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

	/**********************
	 * C04發票字軌取號訊息規格(查詢總公司旗下門市下一期全部字軌資料) [Request]
	 * 
	 **********************/
	public String genRequestForC04() {
		String result = null;
		Invoice invoice = new Invoice();

		SimpleDateFormat dt = new SimpleDateFormat("yyyy-mm-dd HH:mm:ss");
		String setSysTime = dt.format(new Date());

		invoice.setInvoiceCode("C04");
		invoice.setSellerId("20939790");
		invoice.setPosId("1");
		invoice.setPosSn("8220ceffab2327860856");
		invoice.setSysTime(setSysTime);

		StringWriter sw = new StringWriter();
		JAXB.marshal(invoice, sw);
		logger.debug(sw.toString());
		result = sw.toString();
		return result;
	}

	/**********************
	 * Y01取系統時間 [Request]
	 * 
	 **********************/
	public String genRequestForY01() {
		String result = null;
		Index index = new Index();

		SimpleDateFormat dt = new SimpleDateFormat("yyyy-mm-dd HH:mm:ss");
		String setSysTime = dt.format(new Date());

		index.setFunctionCode("Y01");
		index.setSellerId("20939790");
		index.setPosId("1");
		index.setPosSn("8220ceffab2327860856");
		index.setSysTime(setSysTime);

		StringWriter sw = new StringWriter();
		JAXB.marshal(index, sw);
		logger.debug(sw.toString());
		result = sw.toString();
		return result;
	}

	/**********************
	 * D01總分支機構抓取旗下門市通道金鑰 [Request]
	 * 
	 **********************/
	public String genRequestForD01() {
		String result = null;
		Index index = new Index();

		SimpleDateFormat dt = new SimpleDateFormat("yyyy-mm-dd HH:mm:ss");
		String setSysTime = dt.format(new Date());

		index.setInvoiceCode("D01");
		index.setSellerId("20939790");
		index.setPosId("1");
		index.setPosSn("8220ceffab2327860856");
		index.setSysTime(setSysTime);

		StringWriter sw = new StringWriter();
		JAXB.marshal(index, sw);
		logger.debug(sw.toString());
		result = sw.toString();
		return result;
	}

	/**********************
	 * C0401開立發票訊息規格 [Request]
	 * 
	 **********************/
	public String genRequestForC0401(String invoiceNum, List<SaleVO> saleVOs) {
		String result = null;
		Invoice invoice = new Invoice();

		SimpleDateFormat dt1 = new SimpleDateFormat("yyyy-mm-dd");
		SimpleDateFormat dt2 = new SimpleDateFormat("HH:mm:ss");
		Date date = new Date();
		String ymd = dt1.format(date);
		String hms = dt2.format(date);

		invoice.setA1("C0401");//訊息類型
		invoice.setA2(invoiceNum);//發票號碼
		invoice.setA3(ymd);//發票開立日期
		invoice.setA4(hms);//發票開立時間
		invoice.setA5("0000000000");//buyer識別碼(
		invoice.setA6("0000");//buyer名稱
		invoice.setA19(ymd);
		invoice.setA20("北祥");
		invoice.setA21("1020001054");
		invoice.setA22("03");
		invoice.setA24("0");
		invoice.setA25("Y");
		invoice.setA30("1234");

		StringWriter sw = new StringWriter();
		JAXB.marshal(invoice, sw);
		logger.debug(sw.toString());
		result = sw.toString();
		return result;
	}

	/*
	 * 電文加密前置作業
	 */
	public String sendXML(String reqXml) {
		String targetURL = "http://xmltest.551.com.tw";
		String urlParameters = "";

		urlParameters = reqXml;

		String returnValue = InvoiceApi.executePost(targetURL, urlParameters);
		logger.debug("returnValue:" + returnValue);
		return returnValue;
	}

	/*
	 * 透過HTTP POST發送電文
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
			wr.writeBytes(urlParameters);
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
			logger.debug(json);
			logger.debug(xml);
		} catch (Exception e) {
			e.printStackTrace();
			logger.debug(e.getMessage());
		}
		return index;
	}

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
