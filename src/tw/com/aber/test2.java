package tw.com.aber;
import org.apache.commons.codec.binary.Base64;
import java.io.*;
import sun.misc.*;
import javax.crypto.*;
import java.security.*;
import javax.crypto.spec.*;

public class test2 {
	  public static void main(String[] args) throws Exception
	  {
		System.out.println("141212525215252521");
	    String k="1234";
	    String plain="JSPtw好棒阿^^!!";
	    //加密
	    String en=AES.getencrypt(k,plain);
	    String encryptd=new String (en);
	 
	    System.out.println("\nget密文:"+encryptd);
	 
	    //解密
	    String de=AES.getdecrypt(k,encryptd);
//	    String de=AES.getdecrypt(k,encryptd);
	    String decryptd=new String (de);
	    System.out.println("\nget明文:"+decryptd);
	  }
}


class AES
{
  public static String getencrypt(String k,String p) throws Exception
  {
    String encrypted="";
    try{
      KeyGenerator kgen = KeyGenerator.getInstance("AES");
      kgen.init(128,new SecureRandom(k.getBytes() ) );
      SecretKey skey = kgen.generateKey();
      byte[] raw = skey.getEncoded();
      SecretKeySpec skeySpec = new SecretKeySpec(raw, "AES");
      Cipher cipher = Cipher.getInstance("AES");
      cipher.init(Cipher.ENCRYPT_MODE, skeySpec);
      byte[] encrypt =
      cipher.doFinal( p.getBytes());
      encrypted=new String(Base64.encodeBase64String(encrypt));
      //System.out.println("encrypted="+encrypted);
      }//try
      catch(Exception e){System.out.println(e);
      }
      return encrypted;  //return 密文
  }
  public static String getdecrypt(String k2,String base64) throws Exception
  {
    String decrypted="";
    try{
    byte[] b =(new String(Base64.decodeBase64(base64))).getBytes(); 
    KeyGenerator kgen2 = KeyGenerator.getInstance("AES");
    kgen2.init(128,new SecureRandom(k2.getBytes() ) );
    SecretKey skey2 = kgen2.generateKey();
    byte[] raw2 = skey2.getEncoded();
    SecretKeySpec skeySpec2 = new SecretKeySpec(raw2, "AES");
    Cipher cipher2 = Cipher.getInstance("AES");
    cipher2.init(Cipher.DECRYPT_MODE, skeySpec2);
    System.out.println("521");
    byte[] decrypt =
    cipher2.doFinal( b);
    decrypted=new String(decrypt);
    //System.out.println("decrypted="+decrypted);
 
    }catch(Exception e)
    {System.out.println(e);}
      return decrypted;
  }
}
