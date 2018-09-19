/*
 History:
 +------+------------+-----------+-----------+----------------------------------------------------+
 | Ver# | Date       | By        | Defect #  | Description                                        |
 +------+------------+-----------+-----------+----------------------------------------------------+
 | 1    | 09/13/2005 | Sumit Krishnan    | 989       |                  |
 --------------------------------------------------------------------------------------------------
 *
 * formatted with JxBeauty (c) johann.langhofer@nextra.at
 */


package com.chelseasystems.cs.util;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.KeySpec;
import javax.crypto.Cipher;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESKeySpec;
import javax.crypto.spec.DESedeKeySpec;
import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;


/**
 * put your documentation comment here
 */
public class EncryptionUtils {
  public static final String ENCRYPTION_SCHEME = "DES";
  public static final String DEFAULT_ENCRYPTION_KEY = "Armani Secret Key";
  private KeySpec keySpec;
  private SecretKeyFactory keyFactory;
  private Cipher cipher;
  private static final String UNICODE_FORMAT = "UTF8";

  /**
   * put your documentation comment here
   */
  public EncryptionUtils() {
    this(DEFAULT_ENCRYPTION_KEY);
  }

  /**
   * put your documentation comment here
   * @param   String encryptionKey
   */
  public EncryptionUtils(String encryptionKey) {
    java.security.Security.addProvider(new com.sun.crypto.provider.SunJCE());
    if (encryptionKey == null || encryptionKey.trim().length() == 0) {
      System.out.println("EncryptionUtils:: encryptionKey is null-- Set the Default Value");
      encryptionKey = DEFAULT_ENCRYPTION_KEY;
    }
    try {
      byte[] keyAsBytes = encryptionKey.getBytes(UNICODE_FORMAT);
      if (ENCRYPTION_SCHEME.equals("DES")) {
        keySpec = new DESKeySpec(keyAsBytes);
      }
      keyFactory = SecretKeyFactory.getInstance(ENCRYPTION_SCHEME);
      cipher = Cipher.getInstance(ENCRYPTION_SCHEME);
    } catch (Exception e) {
      System.out.println("ERROR:: EncryptionUtils ()");
      e.printStackTrace();
    }
  }

  /**
   * put your documentation comment here
   * @param unencryptedString
   * @return
   */
  public String encrypt(String unencryptedString) {
    if (unencryptedString == null || unencryptedString.trim().length() == 0) {
      System.out.println("ERROR:  UnencryptedString string is " + unencryptedString);
      return "";
    }
    try {
    	SecretKey key = keyFactory.generateSecret(keySpec);
      cipher.init(Cipher.ENCRYPT_MODE, key);
      byte[] cleartext = unencryptedString.getBytes(UNICODE_FORMAT);
      byte[] ciphertext = cipher.doFinal(cleartext);
      BASE64Encoder base64encoder = new BASE64Encoder();
      return base64encoder.encode(ciphertext);
    } catch (Exception e) {
      System.out.println("ERROR:: EncryptionUtils.encrypt()");
      e.printStackTrace();
      return "";
    }
  }

  /**
   * put your documentation comment here
   * @param encryptedString
   * @return
   */
  public String decrypt(String encryptedString) {
    if (encryptedString == null || encryptedString.trim().length() <= 0) {
      System.out.println("ERROR:  encryptedString string is " + encryptedString);
      return "";
    }
    try {
      SecretKey key = keyFactory.generateSecret(keySpec);
      cipher.init(Cipher.DECRYPT_MODE, key);
      BASE64Decoder base64decoder = new BASE64Decoder();
      byte[] cleartext = base64decoder.decodeBuffer(encryptedString);
      byte[] ciphertext = cipher.doFinal(cleartext);
      return bytes2String(ciphertext);
    } catch (Exception e) {
    	  System.out.println("ERROR:: EncryptionUtils.decrypt()  --> encryptedString = " + encryptedString);
    	  System.out.println("Err msg : " + e.getMessage());
    	  System.err.println("Err msg : " + e.getMessage());
    	  return "";
    }
  }

  /**
   * put your documentation comment here
   * @param bytes
   * @return
   */
  private static String bytes2String(byte[] bytes) {
    StringBuffer stringBuffer = new StringBuffer();
    for (int i = 0; i < bytes.length; i++) {
      stringBuffer.append((char)bytes[i]);
    }
    return stringBuffer.toString();
  }

//Vivek Mishra : Merged updated code from source provided by Sergio 19-MAY-16  
  /**
   * ISD Support 
   * @param encryptedString
   * return String
   */
  
  public static String encode(String encryptedString){
	  if (encryptedString == null || encryptedString.trim().length() == 0) {
	      System.out.println("ERROR:  encryptedString string is " + encryptedString);
	      return "";
	    }
	  String encodedDataString = null;
	 try{
		 BASE64Encoder base64encoder = new BASE64Encoder();
		 encodedDataString = base64encoder.encode(encryptedString.getBytes());
		 return encodedDataString;
	 }catch(Exception e){
	  System.out.println("ERROR:: EncryptionUtils.encode()  --> encryptedString = " + encryptedString);
   	  System.out.println("Err msg : " + e.getMessage());
   	  System.err.println("Err msg : " + e.getMessage());
   	  return "";
	 }
  }
  
  /**
   * ISD Support 
   * @param encodedString
   * return String
   */
  
  public static String decode(String encodedString){
	  if (encodedString == null || encodedString.trim().length() == 0) {
	      System.out.println("ERROR:  encodedString string is " + encodedString);
	      return "";
	    }
	  byte[] decodedEncryptedString = null;
	     BASE64Decoder base64decoder = new BASE64Decoder();
	      try {
	    	  decodedEncryptedString = base64decoder.decodeBuffer(encodedString);
	    	 return bytes2String(decodedEncryptedString);
		} catch (Exception e) {
			  System.out.println("ERROR:: EncryptionUtils.decode()  --> EncodedString = " + encodedString);
		   	  System.out.println("Err msg : " + e.getMessage());
		   	  System.err.println("Err msg : " + e.getMessage());
		   	  return "";
		}
  }
  //Ends here
 /*public static void main(String args[])
  {
  	EncryptionUtils utils = new EncryptionUtils();
  	
  	System.out.println(("Visa==="+utils.encrypt("")));
  	System.out.println(("Visa==="+utils.encrypt("")));
  	System.out.println(("Master==="+utils.encrypt("")));
  	//ITBeeTH4+9u9athzmH+XbruqlbCzQSkD
  String abc =	utils.decrypt("/y8tI37VOVM=");
  System.out.println("Decrypted credit card number ==="+abc.length()+" number = "+abc);
  	
  }*/
  
  
//  private String deCryptCreditCard(String number)
//  {
//  	String deCryptNumber=null;
//  	boolean bDecryptionSuccessful=false;
//  	while(!bDecryptionSuccessful)
//  	{
//	  	try
//	  	{
//	  		decrypt(number);
//	  		bDecryptionSuccessful=true;
//	  	}
//	  	catch(Exception e)
//	  	{
//	  		
//	  	}
//  	}
//  	return deCryptNumber;
//  }
//  
//  
//  private void process(String base64key, int level) {
//  	
//  }
//  
//  private void makeCombinations(String key)
//  {
//  	char keyChars[] = key.toCharArray();
//  	
//  	
//  	
//  	
//  }
//  
//  private void getNextCombination(String key, int level)
//  {
//  	
//  }
//  
  
}

