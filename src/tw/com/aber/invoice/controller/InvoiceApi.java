package tw.com.aber.invoice.controller;

import java.io.StringWriter;

import javax.xml.bind.JAXB;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import tw.com.aber.invoice.vo.Index;

public class InvoiceApi {
	private static final Logger logger = LogManager.getLogger(InvoiceApi.class);
	
	/**********************
	 * A01發票號碼取號
	 * 
	 **********************/
	public String genA01() {
		String result;
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
		logger.debug("--- start: output of marshalling ----");
		logger.debug(sw.toString());
		result = sw.toString();
		logger.debug("--- end: output of marshalling ----");
		return result;
	}

	public static void main(String[] args) {
		InvoiceApi api = new InvoiceApi();
		api.genA01();
	}
}
