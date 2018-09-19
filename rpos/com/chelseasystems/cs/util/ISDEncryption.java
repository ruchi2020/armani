package com.chelseasystems.cs.util;
/*
History:
+------+------------+----------------+-----------+----------------------------------------------------+
| Ver# | Date       | By             | Defect #  | Description                                        |
+------+------------+----------------+-----------+----------------------------------------------------+
| 1    | 08/05/2010 | Vivek Sawant   |           | ISD Encryption Client.                 			  |
------------------------------------------------------------------------------------------------------+
*
* formatted with JxBeauty (c) johann.langhofer@nextra.at
*/

/*
 * Encryption Key file is stored in C:/Key17.dat
 */

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Properties;

import com.isd.jec.crypto.CryptoException;
import com.isd.jec.crypto.EncryptionManager;
import com.isd.jec.util.Base64Converter;
import com.isd.jec.util.ClassLoaderUtil;
import com.isd.jec.util.HexConvert;
import com.isd.jec.util.StreamUtil;
import com.isd.toolkit.IsdJavaToolkit;
import com.isd.toolkit.IsdJavaToolkitConfig;
import com.isd.toolkit.error.ConfigReadException;
import com.isd.toolkit.format.IsdJavaToolkitFixedFormatProcessor;

public class ISDEncryption {
    
    private EncryptionManager crypto = null;
    
    /** Corresponds to "ISSDIAKP", not a String to hide from decompile */
   /* private static byte[] keyFileBytes =
    { 73,83,83,68,73,65,75,80 };*/
    /** Corresponds to "C:/ISSDIAKP", not a String to hide from decompile */
    private static byte[] keyFileBytes =
    { 67,58,47,73,83,83,68,73,65,75,80 };
    /** Corresponsd to "hexISSDIAKP", not a String to hide from decompile */
   /* private static byte[] hexKeyFileBytes = {104,101,120,73,83,83,68,73,65,75,80};*/
    /** Corresponsd to "C:/hexISSDIAKP", not a String to hide from decompile */
    private static byte[] hexKeyFileBytes = {67,58,47,104,101,120,73,83,83,68,73,65,75,80};
    
    
    public static String keyFilePath = null;
    public static String urlPath = null;
    
    public final static String  TOOLKIT_PROPERTIES_FILE_NAME = "isdjavatoolkit.properties";
    
    /**
     * @param args
     */
    public static void main(String[] args) {
    	ISDEncryption sample = null;

        if(args == null || (!"hex".equals(args[0]) && !"bin".equals(args[0])) ){
            System.err.println("Usage: Sample <hex|bin>");
        } else {
            boolean processHex = false;
            
            // determine which we mode we should used
            if("hex".equals(args[0]))
                processHex = true;
            else
                processHex = false;
                


            try {
                // create this sample
                sample = new ISDEncryption( processHex );
        } catch (CryptoException e) {
                System.out.println("Error obtaining Encryption Manager: " + e);
            } catch (Exception e) {
                System.out.println("Error while reading key from file: " + e);  // thrown in getEncryptedKey()
            }
            //Testing the utlity.
            /*try {
		              sample.encryptString("444433332221111");
		           }
		      catch (CryptoException e) {
                    System.out.println("Error while encrypting text: " + e);
                  }
            */
        }

    }

    // constructor
    public ISDEncryption(boolean hexFlag) throws Exception {  
        byte[] keyBytes = null;
               
        if (hexFlag) {
            keyBytes = getHexEncryptedKey();
		//Vivek Mishra : Merged updated code from source provided by Sergio 19-MAY-16	
         //   crypto = EncryptionManager.obtainInstanceFromHexEncodedBytes(keyBytes);
		//Ends here 
           }
        else {
            keyBytes = getEncryptedKey();
            crypto = EncryptionManager.obtainInstance(keyBytes);
        }        
    }
    
    
    public static String getKeyFilePath() throws ConfigReadException{
    if(keyFilePath==null)
    	{
    		IsdJavaToolkitConfig config = new IsdJavaToolkitConfig();
    		InputStream localInputStream1 = ClassLoader.getSystemResourceAsStream(TOOLKIT_PROPERTIES_FILE_NAME);
    		config.readConfig(localInputStream1);
    		keyFilePath =config.getHkmKeyFile(); 
    	}
        //System.out.println("Keyfilepath   :"+keyFilePath);
        urlPath = "file://localhost/"+keyFilePath;
		return urlPath;
	}
    
    
    public byte[] encryptString(String clearText) throws CryptoException {
    	//System.out.println("No of bytes to be encrypted from HKM toolkit: "+ clearText.getBytes().length);
        //System.out.println("IN HKM toolkit = Text to be encrypted: " + clearText.getBytes());
        
        // Encrypt the clear text
        byte[] bytes = crypto.encrypt(clearText.getBytes());
        //System.out.println("No of Bytes return from HKM toolkit :" + bytes.length);
        return bytes;
        
        
        /* Converts an array of input bytes to a string containing the hexidecimal
         * representation of the byte array. A byte of array of length n generates an
         * output string of length 2n.
         */
        
        /*System.out.println("String value :"+ clearText+" & Encrypted result as hex: " + HexConvert.bytesToHexString(bytes));
        System.out.println("Hex Length "+HexConvert.bytesToHexString(bytes).length());*/
       
       /* Utility to convert to/from Base64 encoding. This is to create "safe" strings
        * from binary data.
        */
        
       /* System.out.println("String value :"+ clearText+" & Encrypted results as Base64: " + Base64Converter.byteArrayToBase64(bytes));
        System.out.println("Base64 Length :"+Base64Converter.byteArrayToBase64(bytes).length());
       */ 
    }
    
    private byte[] getEncryptedKey() throws IOException, Exception {
       
    	
    	String keyFile = new String(keyFileBytes);
       java.net.URL url = ClassLoaderUtil.loadResource("file://localhost/C:/Key17.dat");
        if (url == null) {
            throw new Exception(keyFile + " not found.");
        }
        return StreamUtil.readBytes(url.openStream());
    }
    
    public static byte[] getHexEncryptedKey() throws IOException, Exception {
    	 byte[] keybyte = null;
    	 BufferedReader reader = null;
        byte[] hexKey = null;
        // Explict value added by vivek File protocol
    	//URL must need to mentioned protocol.
        String path = getKeyFilePath();
        
        //System.out.println("path   :"+path);
       // FileInputStream fi = new FileInputStream("file://localhost/"+path);
        java.net.URL url = ClassLoaderUtil.loadResource(path);
        if(path == null){
            throw new Exception("URL not found.");
        } else {
        	try{
        		reader = new BufferedReader(new InputStreamReader(url.openStream()));
        	}
        	catch(Exception e ){
        		IsdJavaToolkit.initialize();
        	}// skip 3 lines before reading the actual key
        	finally{
        		if(reader != null)
        		{
        			reader.readLine(); reader.readLine(); reader.readLine();
                    // now actually create the 96byte array and read in the hex bytes
                    hexKey = new byte[96];
                    for(int i=0; i<96; i++){
                        hexKey[i] = (byte)reader.read();
                     }
                }
        	}
            
        }
        return hexKey; 
    }
    
    /**
     * This method will return unique Key ID of Encryption Key file and being call during CC request.
     * @return String
     * @throws IOException
     * @throws Exception
     */
    
    public static String getKeyID()  throws IOException, Exception {
    	 String keyId = null;
    	 java.net.URL url = ClassLoaderUtil.loadResource("file://localhost/C:/Key17.dat");
         if(url == null){
             throw new Exception("file not found.");
         } else {
             BufferedReader reader = new BufferedReader(new InputStreamReader(url.openStream()));
             // skip 2 lines  key ID
             reader.readLine(); reader.readLine();
             keyId = reader.readLine();
           
        }
   
    	return keyId;
    }
	
    public static String bytes2String(byte[] bytes) {
    	
	    StringBuffer stringBuffer = new StringBuffer();
	    for (int i = 0; i < bytes.length; i++) {
	      stringBuffer.append((char)bytes[i]);
	    }
	    //System.out.println("data set in Customer CC object>>>> "+stringBuffer);
	    return stringBuffer.toString();
	  }
  

}
