package tw.com.aber.inv.controller;

import java.io.StringWriter;

import javax.xml.bind.JAXB;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import tw.com.aber.inv.vo.Index;

public class InvoiceApi {
	private static final Logger logger = LogManager.getLogger(InvoiceApi.class);

	/**********************
	 * A01發票號碼取號
	 * 
	 **********************/
	public String genRequestForA01() {
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
		logger.debug("\n\n[A01][XML格式][genRequestForA01]\n\n{}", sw.toString());
		result = sw.toString();
		return result;
	}

	public static void main(String[] args) {
		InvoiceApi api = new InvoiceApi();
		api.genRequestForA01();
	}
}
