package tw.com.aber.inv.controller;

import java.io.StringReader;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.JAXB;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.google.gson.Gson;

import tw.com.aber.inv.vo.Index;
import tw.com.aber.inv.vo.Invoice;
import tw.com.aber.inv.vo.InvoiceData;

public class InvoiceApi {
	private static final Logger logger = LogManager.getLogger(InvoiceApi.class);

	/**********************
	 * A01發票號碼取號 [Request]
	 * 
	 **********************/
	public String genRequestForA01() {
		String result = null;
		Index index = new Index();
		index.setFunctionCode("A01");

		index.setSellerId("12345678");
		index.setPosId("7");
		index.setPosSn("SSSSSS");
		index.setAppVserion("00101");
		index.setSysTime("2017-06-20 13:28:42");
		index.setUserId("");

		StringWriter sw = new StringWriter();
		JAXB.marshal(index, sw);
		logger.debug("\n\n[A01][XML格式][genRequestForA01]\n\n{}", sw.toString());
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
		logger.debug("\n\n[A01][XML格式][genResponseForA01]\n\n{}", sw.toString());
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
		invoice.setInvoiceCode("A02");
		invoice.setSellerId("12345678");
		invoice.setPosId("1");
		invoice.setPosSn("HKH1238LS0C3X0WSDG3M1");
		invoice.setSysTime("2014-03-25 13:59 01:45:22");
		
		StringWriter sw = new StringWriter();
		JAXB.marshal(invoice, sw);
		logger.debug("\n\n[A02][XML格式][genRequestForA02]\n\n{}", sw.toString());
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
		logger.debug("\n\n[A02][XML格式][genResponseForA02]\n\n{}", sw.toString());
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
		index.setInvoiceCode("A03");
		index.setSellerId("12345678");
		index.setPosId("1");
		index.setPosSn("sdfj2owjrls");
		index.setSysTime("2014-07-24 16:47:50");

		InvoiceData invoiceData1 = new InvoiceData();
		invoiceData1.setSellerId("12345678");
		invoiceData1.setType("03");
		invoiceData1.setTypeName("二聯式收銀機");
		invoiceData1.setTaxMonth("10308");
		invoiceData1.setInvoiceHeader("TV");
		invoiceData1.setInvoiceStart("10000001");
		invoiceData1.setInvoiceEnd("10000250");

		InvoiceData invoiceData2 = new InvoiceData();
		invoiceData2.setSellerId("12345678");
		invoiceData2.setType("03");
		invoiceData2.setTypeName("二聯式收銀機");
		invoiceData2.setTaxMonth("10308");
		invoiceData2.setInvoiceHeader("TV");
		invoiceData2.setInvoiceStart("10000251");
		invoiceData2.setInvoiceEnd("10000500");
		
		List<InvoiceData> invoiceDatas = new ArrayList<InvoiceData>();
		invoiceDatas.add(invoiceData1);
		invoiceDatas.add(invoiceData2);
		
		index.setInvoiceData(invoiceDatas);
		
		StringWriter sw = new StringWriter();
		JAXB.marshal(index, sw);
		logger.debug("\n\n[A03][XML格式][genRequestForA03]\n\n{}", sw.toString());
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
		logger.debug("\n\n[A03][XML格式][genResponseForA03]\n\n{}", sw.toString());
		result = sw.toString();
		return result;
	}

	/**********************
	 * A04發票字軌取號訊息規格(查詢總公司其廈門市當期全部字軌資料) [Request]
	 * 
	 **********************/
	public String genRequestForA04() {
		String result = null;
		Index index = new Index();
		index.setInvoiceCode("A03");
		index.setSellerId("12345678");
		index.setPosId("1");
		index.setPosSn("sdfj2owjrls");
		index.setSysTime("2014-07-24 16:47:50");

		InvoiceData invoiceData1 = new InvoiceData();
		invoiceData1.setSellerId("12345678");
		invoiceData1.setType("03");
		invoiceData1.setTypeName("二聯式收銀機");
		invoiceData1.setTaxMonth("10308");
		invoiceData1.setInvoiceHeader("TV");
		invoiceData1.setInvoiceStart("10000001");
		invoiceData1.setInvoiceEnd("10000250");

		InvoiceData invoiceData2 = new InvoiceData();
		invoiceData2.setSellerId("12345678");
		invoiceData2.setType("03");
		invoiceData2.setTypeName("二聯式收銀機");
		invoiceData2.setTaxMonth("10308");
		invoiceData2.setInvoiceHeader("TV");
		invoiceData2.setInvoiceStart("10000251");
		invoiceData2.setInvoiceEnd("10000500");
		
		List<InvoiceData> invoiceDatas = new ArrayList<InvoiceData>();
		invoiceDatas.add(invoiceData1);
		invoiceDatas.add(invoiceData2);
		
		index.setInvoiceData(invoiceDatas);
		
		StringWriter sw = new StringWriter();
		JAXB.marshal(index, sw);
		logger.debug("\n\n[A03][XML格式][genRequestForA03]\n\n{}", sw.toString());
		result = sw.toString();
		return result;
	}
	
	public Index getIndexResponse(String resXml) {
		Index index = null;
		StringReader reader = null;

		try {
			reader = new StringReader(resXml);
			index = JAXB.unmarshal(reader, Index.class);

			String json = new Gson().toJson(index);

			StringWriter sw = new StringWriter();
			JAXB.marshal(index, sw);
			String xml = sw.toString();

			String regulation = "\n\n[Response][INDEX][Json格式]\n\n{}\n\n\n[Response][INDEX][XML格式]\n\n{}\n";
			logger.debug(regulation, json, xml);
		} catch (Exception e) {
			logger.debug("\n\ngetErrResponseObj err:{}\n", e.getMessage());
		}
		return index;
	}

	public Invoice getInvoiceResponse(String resXml) {
		Invoice invoice = null;
		StringReader reader = null;

		try {
			reader = new StringReader(resXml);
			invoice = JAXB.unmarshal(reader, Invoice.class);

			String json = new Gson().toJson(invoice);

			StringWriter sw = new StringWriter();
			JAXB.marshal(invoice, sw);
			String xml = sw.toString();

			String regulation = "\n\n[Response][Invoice][Json格式]\n\n{}\n\n\n[Response][Invoice][XML格式]\n\n{}\n";
			logger.debug(regulation, json, xml);
		} catch (Exception e) {
			logger.debug("\n\ngetErrResponseObj err:{}\n", e.getMessage());
		}
		return invoice;
	}

	public static void main(String[] args) {
		InvoiceApi api = new InvoiceApi();
		String resXml = null;
		
		//A01
		api.genRequestForA01();
		resXml = api.genResponseForA01();
		api.getIndexResponse(resXml);

		//A02
		api.genRequestForA02();
		resXml = api.genResponseForA02();
		api.getInvoiceResponse(resXml);

		//A03
		api.genRequestForA03();
		resXml = api.genResponseForA03();
		api.getIndexResponse(resXml);
	}
}
