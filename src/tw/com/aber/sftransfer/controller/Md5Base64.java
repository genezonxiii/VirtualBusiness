package tw.com.aber.sftransfer.controller;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import org.apache.commons.codec.binary.Base64;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class Md5Base64 {
	
	private static final Logger logger = LogManager.getLogger(Md5Base64.class);
	
	public static String encode(String s) {
		if (s == null) {
			return null;
		}
		String encodeStr = "";
		
		byte[] utfBytes = null;
		try {
			utfBytes = s.getBytes("utf8");
		} catch (UnsupportedEncodingException uee) {
			logger.error("UnsupportedEncodingException:" + uee.getMessage());
		}
		
		MessageDigest mdTemp;
		try {
			mdTemp = MessageDigest.getInstance("MD5");
			mdTemp.update(utfBytes);
			byte[] md5Bytes = mdTemp.digest();
			
			
			encodeStr = Base64.encodeBase64String(md5Bytes);
		} catch (NoSuchAlgorithmException nsae) {
			logger.error("NoSuchAlgorithmException:" + nsae.getMessage());
		}
		return encodeStr;
	}
	
	public static String urlEncode(String s) {
		String encodeStr = "";
		try {
			encodeStr = URLEncoder.encode(s, "UTF-8");
		} catch (UnsupportedEncodingException uee) {
			logger.error("UnsupportedEncodingException:" + uee.getMessage());
		}
		
		return encodeStr;
	}

	public static void main(String[] args) {
		logger.debug(Md5Base64.encode("庄严"));
		logger.debug(Md5Base64.encode("123456"));
		logger.debug(Md5Base64.encode("www.zhuangling.com"));
	}
}
