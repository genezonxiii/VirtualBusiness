package tw.com.aber.sftransfer.controller;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import org.apache.commons.codec.binary.Base64;

public class Md5Base64 {
	public static String encode(String s) {
		if (s == null)
			return null;
		String encodeStr = "";
		byte[] utfBytes = s.getBytes();
		MessageDigest mdTemp;
		try {
			mdTemp = MessageDigest.getInstance("MD5");
			mdTemp.update(utfBytes);
			byte[] md5Bytes = mdTemp.digest();
			
			
			encodeStr = Base64.encodeBase64String(md5Bytes);
		} catch (NoSuchAlgorithmException e) {
			return null;
		}
		return encodeStr;
	}

	public static void main(String[] args) {
		System.out.println(Md5Base64.encode("庄严"));
		System.out.println(Md5Base64.encode("123456"));
		System.out.println(Md5Base64.encode("www.zhuangling.com"));
	}
}
