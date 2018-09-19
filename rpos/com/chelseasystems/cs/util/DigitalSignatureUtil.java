package com.chelseasystems.cs.util;

/**
 * Created by Satin. 
 * This class creates a message used for creating digital signature. 
 * Then it executes two commands to create digital signature.
 * 
 */


import com.chelseasystems.cr.config.*;
import com.chelseasystems.cr.currency.ArmCurrency;
import com.chelseasystems.cr.logging.*;
import com.chelseasystems.cr.pos.*;
import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.util.*;
import com.chelseasystems.cs.collection.CMSMiscCollection;
import com.chelseasystems.cs.pos.CMSCompositePOSTransaction;
import java.text.DateFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.io.*;
import java.nio.channels.*;

//import at.atrust.cashregister.LibRegistrierkasseJNI;
 

public class DigitalSignatureUtil {
	
	static {
		System.loadLibrary("LibRegistrierkasseJNI");
	}
	
	
	public DigitalSignatureUtil() {
	  }
	
	public static String createMessageAndDigitalSignature(PaymentTransaction txn, String previousSignature) throws Exception{
		String previousDigitalSignature = previousSignature;
		ConfigMgr cfgMgr = new ConfigMgr("payment.cfg");
		String activateDigitalSignature = cfgMgr.getString("ACTIVATE_DIGITAL_SIGNATURE");
		String austrianEnv =cfgMgr.getString("AUSTRIAN_ENVOIREMENT");
		/*   if (txn instanceof CmsCollectionCredit) {
							    txnId = ((CMSMiscCollection)txn).getId();
						   }
		 * */
		
		
		
		if (((CMSCompositePOSTransaction)txn) instanceof CMSCompositePOSTransaction){
			CMSCompositePOSTransaction theTxnPOS = ((CMSCompositePOSTransaction) txn);
			String totalAmount = theTxnPOS.getCompositeTotalAmountDue().toString();
			 String[] splittedString = totalAmount.split("->");
			 String grossTotal = splittedString[1];
			 			 
			 DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
			 String d = df.format(theTxnPOS.getCreateDate());
			 
			 //RICCARDINO MODIFICA METTO IL CREATE DATE 
			 //Date date = new Date();
			 DateFormat df1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			 String e= df1.format(theTxnPOS.getCreateDate());
			 e= e.replace(" ", "T");
			 DateFormat df2 = new SimpleDateFormat("yyyy");
			 String f = df2.format(theTxnPOS.getCreateDate());
			 
			 
			 //String fsclReceiptNumber = ((CMSCompositePOSTransaction)txn).getFiscalReceiptNumber();
			 //String fsclReceiptNumber = fiscalInterface.getDocumentResponse().getDocumentNumber();
			 String fsclReceiptNumber = theTxnPOS.getId();
			 
		 
			 String finalMessageString;
			 if (previousDigitalSignature != null){
				 finalMessageString = d.concat(";").concat(e).concat(";").concat(fsclReceiptNumber).concat(";").concat(grossTotal).concat(";").concat(previousDigitalSignature);
			 }
			 else 
			 finalMessageString = d.concat(";").concat(e).concat(";").concat(fsclReceiptNumber).concat(";").concat(grossTotal);


	 
			 Writer output = null;
			 String messageFile=cfgMgr.getString("MESSAGE_FILE");
			 File file = new File(messageFile);
			  
			  try {
				output = new BufferedWriter(new FileWriter(file));
				output.write(finalMessageString);
				output.close();
			} catch (IOException e1) {
				e1.printStackTrace();
				LoggingServices.getCurrent().logMsg("Message from DigitalSignatureUtil.java :IOException in creating the Message.txt file");
				LoggingServices.getCurrent().logMsg(DigitalSignatureUtil.class.getName(), "start", "Exception"
				          , "See Exception", LoggingServices.MAJOR, e1);
				}
		}
		// End of message creation
		
		
		
		String digitalSignature;
			
            /* These are the commands that we need to execute
             *  from the directory "C:\OpenSSL-Win32\bin" directory 
             */
			String folderPath = cfgMgr.getString("FOLDER_PATH");
			File directory1 = new File(folderPath);
			String cmd1 = cfgMgr.getString("COMMAND1");
        	String cmd2 = cfgMgr.getString("COMMAND2");

        	Process p = Runtime.getRuntime().exec(cmd1, null, directory1);
        	try {
				p.waitFor();
			} catch (InterruptedException e1) {
				e1.printStackTrace();
				// Added by Satin to update the log
				LoggingServices.getCurrent().logMsg("ERROR in DigitalSignatureUtil.java : Interrupted Exception. Command not executed sucessfully.");
				LoggingServices.getCurrent().logMsg(DigitalSignatureUtil.class.getName(), "start", "Exception"
				          , "See Exception", LoggingServices.MAJOR, e1);
				}
        	int exitcode = p.exitValue();
        	if (exitcode == 0){System.out.println("****----Record1.sha1 sucessfully created----****");}
        	else{
        		LoggingServices.getCurrent().logMsg("Message from DigitalSignatureUtil.java : failure in generating Record1.sha1");
        	}
        		
        	
        	if (exitcode==0){
        		Process p1 = Runtime.getRuntime().exec(cmd2, null, directory1);
        		p1.waitFor();
        	}
     	
        	// This is to read the Record1.b64 file which stores the digital signature.
        	// This digital signature will be stored in TR_RTL table.
        	String b64FilePath=cfgMgr.getString("B64_FILE_PATH");
        	File f = new File(b64FilePath);
        	FileInputStream fis1 = new FileInputStream(f);
            DataInputStream dis = new DataInputStream(fis1);
            byte[] keyBytes = new byte[(int) f.length()];
            dis.readFully(keyBytes);
            dis.close();

            digitalSignature = new String(keyBytes);
          // End of block for creation of digital signature
            
            return(digitalSignature);
            
	}

	
	public static void openFile() 
	{
	 Writer output = null;
	 ConfigMgr cfgMgr = new ConfigMgr("payment.cfg");
	 String messageFile=cfgMgr.getString("KEY_FILE");
	 
	 File file = new File(messageFile);
	
	  try {
		output = new BufferedWriter(new FileWriter(file));
		output.write("dddd");
		output.close();
	  
	} catch (IOException e1) {
		e1.printStackTrace();
	 }
	
	}
	public static String createNewMessageAndDigitalSignature(PaymentTransaction txn, String previousSignature, String fsclReceiptNumber ) throws Exception{
	
		String previousDigitalSignature = previousSignature;
		ConfigMgr cfgMgr = new ConfigMgr("payment.cfg");
		String activateDigitalSignature = cfgMgr.getString("ACTIVATE_DIGITAL_SIGNATURE");
		
		/*   if (txn instanceof CmsCollectionCredit) {
							    txnId = ((CMSMiscCollection)txn).getId();
						   }
		 * */
		
		
		if (((CMSCompositePOSTransaction)txn) instanceof CMSCompositePOSTransaction){
			CMSCompositePOSTransaction theTxnPOS = ((CMSCompositePOSTransaction) txn);
			String totalAmount = theTxnPOS.getCompositeTotalAmountDue().toString();
			 String[] splittedString = totalAmount.split("->");
			 
			 String totalAmt = theTxnPOS.getSaleRetailAmount().formattedStringValue().substring(1,theTxnPOS.getSaleRetailAmount().formattedStringValue().length());
			 String grossTotal = splittedString[1];
			 grossTotal = grossTotal.replace('-', ' ');			 
			 grossTotal = grossTotal.trim();			 
			 float numero = Float.parseFloat(grossTotal);
			 numero = Math.abs(numero);
			 
			 String tot = String.format("%.2f", numero);

			 tot = tot.replace("," , ".");
			 
			// int lastindex = grossTotal.lastIndexOf(".");
			 
			 
			
				 
				 
			 DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
			 String d = df.format(theTxnPOS.getCreateDate());
			 
			//RICCARDINO MODIFICA METTO IL CREATE DATE 
			 //Date date = new Date();
			 Date date = new Date();
			 DateFormat df1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			 String e= df1.format(theTxnPOS.getCreateDate());
			 e= e.replace(" ", "T");
			 DateFormat df2 = new SimpleDateFormat("yyyy");
			 String f = df2.format(theTxnPOS.getCreateDate());
			 
			 
			 //String fsclReceiptNumber = ((CMSCompositePOSTransaction)txn).getFiscalReceiptNumber();
			 //String fsclReceiptNumber = fiscalInterface.getDocumentResponse().getDocumentNumber();
			 
						 
			 //grossTotal
			 
			 String finalMessageString;
			 if (previousDigitalSignature != null){
				 finalMessageString = d.concat(";").concat(e).concat(";").concat(fsclReceiptNumber).concat(";").concat(tot).concat(";").concat(previousDigitalSignature);
			 }
			 else 
			 finalMessageString = d.concat(";").concat(e).concat(";").concat(fsclReceiptNumber).concat(";").concat(tot).concat(";");


	 
			 Writer output = null;
			 String messageFile=cfgMgr.getString("MESSAGE_FILE");
			 File file = new File(messageFile);
			  
			  try {
				output = new BufferedWriter(new FileWriter(file));
				output.write(finalMessageString);
				output.close();
			} catch (IOException e1) {
				e1.printStackTrace();
				LoggingServices.getCurrent().logMsg("Message from DigitalSignatureUtil.java :IOException in creating the Message.txt file");
				LoggingServices.getCurrent().logMsg(DigitalSignatureUtil.class.getName(), "start", "Exception"
				          ,"See Exception", LoggingServices.MAJOR, e1);
				}
		}
		// End of message creation
		
		
		
		String digitalSignature;
			
            /* These are the commands that we need to execute
             *  from the directory "C:\OpenSSL-Win32\bin" directory 
             */
			String folderPath = cfgMgr.getString("FOLDER_PATH");
			File directory1 = new File(folderPath);
			String cmd1 = cfgMgr.getString("COMMAND1");
        	String cmd2 = cfgMgr.getString("COMMAND2");

        	Process p = Runtime.getRuntime().exec(cmd1, null, directory1);
        	try {
				p.waitFor();
			} catch (InterruptedException e1) {
				e1.printStackTrace();
				// Added by Satin to update the log
				LoggingServices.getCurrent().logMsg("ERROR in DigitalSignatureUtil.java : Interrupted Exception. Command not executed sucessfully.");
				LoggingServices.getCurrent().logMsg(DigitalSignatureUtil.class.getName(), "start", "Exception"
				          , "See Exception", LoggingServices.MAJOR, e1);
				}
        	int exitcode = p.exitValue();
        	if (exitcode == 0){System.out.println("****----Record1.sha1 sucessfully created----****");}
        	else{
        		LoggingServices.getCurrent().logMsg("Message from DigitalSignatureUtil.java : failure in generating Record1.sha1");
        	}
        		
        	
        	if (exitcode==0){
        		Process p1 = Runtime.getRuntime().exec(cmd2, null, directory1);
        		p1.waitFor();
        	}
     	
        	// This is to read the Record1.b64 file which stores the digital signature.
        	// This digital signature will be stored in TR_RTL table.
        	String b64FilePath=cfgMgr.getString("B64_FILE_PATH");
        	File f = new File(b64FilePath);
        	FileInputStream fis1 = new FileInputStream(f);
            DataInputStream dis = new DataInputStream(fis1);
            byte[] keyBytes = new byte[(int) f.length()];
            dis.readFully(keyBytes);
            dis.close();

            digitalSignature = new String(keyBytes);
          // End of block for creation of digital signature
            
            return(digitalSignature);
            
	}	
	
	// Restituisce il totale TurnoverTotale.
	public static String sumTurnover(PaymentTransaction txn , String sTurnover) 
	{
		String sumTurnover ="";	
		 if (((CMSCompositePOSTransaction)txn) instanceof CMSCompositePOSTransaction)
		     {
				CMSCompositePOSTransaction theTxnPOS = ((CMSCompositePOSTransaction) txn);
				 String totalAmount = theTxnPOS.getCompositeTotalAmountDue().toString();
				 String[] splittedString = totalAmount.split("->");
				 String grossTotal = splittedString[1];
				 //grossTotal = grossTotal.replace('-', ' ');			 
				 //grossTotal = grossTotal.trim();			 
				 float numero = Math.round((Float.parseFloat(grossTotal)*100));
				 Integer IntTrxNum = (int)numero;
				 				 
				 /*String totalAmt = theTxnPOS.getSaleRetailAmount().formattedStringValue().substring(1,theTxnPOS.getSaleRetailAmount().formattedStringValue().length());
				 totalAmt = totalAmt.replace(',', '.');
				 //grossTotal = grossTotal.trim(); 89,44 ->8944
				 float totalTRx = Float.parseFloat(totalAmt)*100;
				 totalTRx = Math.abs(totalTRx);
				 Integer IntTrx = (int)totalTRx;*/

				 // TOTALE REGISTATORE 
				 Integer turnOver = Integer.valueOf(sTurnover);
				 turnOver = Math.abs(turnOver);
				 //String myturnOver = String.format("%.2f", turnOver);
				 // Calcolo totale TurnOver 
				 int totalTurnover = turnOver + IntTrxNum;
				 totalTurnover = Math.abs(totalTurnover);
				 //sumTurnover = String.format("%.0f", totalTurnover);
				sumTurnover =String.valueOf(totalTurnover);
		     }
	
		    return (sumTurnover);
		}		
			
	public static String getTurnoverEncripted(String aesKey,String TransactionID ,String shopid, String cashregister, String turnover ) throws Exception
	{
		int returnValue;
		String[] encrypted= new String[1];
		//LibRegistrierkasseJNI jni = new LibRegistrierkasseJNI();
		String register = shopid.concat(cashregister);
		
		//returnValue = jni.encrypt(aesKey, turnover, register, TransactionID, encrypted);
		//System.out.println("encrypt() returns " + returnValue + " value " + encrypted[0]);  
				
		 
	   	 return(encrypted[0]);
		
	 }	
	
	
	
	public static String getFirstEncripted(String aesKey, String TransactionID ,String shopid, String cashregister, String turnover ) throws Exception
	{
		int returnValue;
		String[] encrypted= new String[1];
		//LibRegistrierkasseJNI jni = new LibRegistrierkasseJNI();
		String register = shopid.concat(cashregister);
		
		//returnValue = jni.encrypt(aesKey, turnover, register, TransactionID, encrypted);
		//System.out.println("encrypt() returns " + returnValue + " value " + encrypted[0]);  
				
		 
	   	 return(encrypted[0]);
		
	 }	
	
	public static String createFIRSTSignature(String shopid,String cashregister, String fsclReceiptNumber,String encripted,String certificate ) throws Exception
	{
		String  digitalSignature=null;
		//LibRegistrierkasseJNI jni = new LibRegistrierkasseJNI();
		ConfigMgr cfgMgr = new ConfigMgr("payment.cfg");
		String finalMessageString="_R1-AT1_";
		int returnSignedKey=0;
		String[] jwsSignature = new String[1];
		String QrtoSave=null;
	
		
		
		 // *****   DATA DELLA ZERO TRANSACTION 
		 DateFormat df1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		 Date date = new Date();
		 String dateTransaction= df1.format(date);
		 dateTransaction= dateTransaction.replace(" ", "T");
		
		 // *****   RIPASSO LA PRECEDENTE DIGITALSIGNATURE CHE E' NEGOZIO PIU' CASSA 
		    String[] hashPreviousResult = new String[1];
			//int returnCode = jni.hashSigVorigerBeleg(shopid.concat(cashregister),8, hashPreviousResult);
			//System.out.println("hashSigVorigerBeleg() REGISTERID FIRST TIME returnCode " + returnCode  +  " value :" + hashPreviousResult[0]);
			//String previosTranHash = hashPreviousResult[0];
		 
			//finalMessageString = finalMessageString.concat(shopid).concat(cashregister).concat("_").concat(fsclReceiptNumber).concat("_").concat(dateTransaction).concat("_").concat("0,00").concat("_0,00_0,00_0,00_0,00_").concat(encripted).concat("_").concat(certificate).concat("_").concat(previosTranHash);
		 
		
			 System.out.println("Message to Cript :"+finalMessageString);
			 //returnSignedKey = jni.signJWS(finalMessageString, jwsSignature);
			// System.out.println("Stringa  " + returnCode + " value " + jwsSignature[0]);
	     //_R1-AT1_76101_ARQ401011028273_2017-03-22T18:12:52_0,00_0,00_0,00_0,00_0,00_5Eqkwp/pqaJ00tiim12I+g==_6d9f4d1c_pkD98l0Ik/4=

	     
	 	if(returnSignedKey==0)  // se torna ZERO VA TUTTO BENE 
	 	{ 
	 			digitalSignature=jwsSignature[0];
	 			QrtoSave = finalMessageString.concat("_").concat(createQRbarcodeString(digitalSignature));
	 			System.out.println("QR INIZIALE DA STAMPARE :"+QrtoSave);
	 	} 
	 	
	 	
	 	 Writer output = null;
		 String messageFile=cfgMgr.getString("MESSAGE_FILE");
		 File file = new File(messageFile);
		  
		 //SALVO LA STRINGA DA ESEGUIRE IN UN FILE DI LOG 
		 try {
			output = new BufferedWriter(new FileWriter(file));
			output.write(QrtoSave);
			output.close();
		} catch (IOException e1) {
			e1.printStackTrace();
			LoggingServices.getCurrent().logMsg("Message from DigitalSignatureUtil.java :IOException in creating the Message.txt file");
			LoggingServices.getCurrent().logMsg(DigitalSignatureUtil.class.getName(), "start", "Exception"
			          ,"See Exception", LoggingServices.MAJOR, e1);
			}
	     
	 	 return digitalSignature;
	
	 
	}	
	
	
	
	
	public static String createZeroTransactionSignature(String shopid,String cashregister, String previousSignature, String fsclReceiptNumber,String encripted,String certificate ) throws Exception
	{
		//LibRegistrierkasseJNI jni = new LibRegistrierkasseJNI();
		String previousDigitalSignature = previousSignature;
		ConfigMgr cfgMgr = new ConfigMgr("payment.cfg");
		String finalMessageString="_R1-AT1_";
		int returnSignedKey=0;
		String[] jwsSignature = new String[1];
		String[] returnKey = new String[2];
		
		 // *****   DATA DELLA ZERO TRANSACTION 
		 DateFormat df1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		 Date date = new Date();
		 String dateTransaction= df1.format(date);
		 dateTransaction= dateTransaction.replace(" ", "T");
		
		 // *****   RIPASSO LA PRECEDENTE DIGITALSIGNATURE 
		    String[] hashPreviousResult = new String[1];
			//int returnCode = jni.hashSigVorigerBeleg(previousDigitalSignature, 8, hashPreviousResult);
			//System.out.println("Stringa precedente_returnCode:" + returnCode  +  "and value:" + hashPreviousResult[0]);
			String previosTranHash = hashPreviousResult[0];
			
			
			finalMessageString = finalMessageString.concat(shopid).concat(cashregister).concat("_").concat(fsclReceiptNumber).concat("_").concat(dateTransaction).concat("_").concat("0,00").concat("_0,00_0,00_0,00_0,00_").concat(encripted).concat("_").concat(certificate).concat("_").concat(previosTranHash);
	
			
			 System.out.println("Message to Cript :"+finalMessageString);
			// returnSignedKey = jni.signJWS(finalMessageString, jwsSignature);
			// System.out.println("Zero transaction Signature value:" +jwsSignature[0]);
		 
		 
		return jwsSignature[0];
	 
	}	
	
	

	public static String createQRbarcodeString (String digitalSignature)
	{
 
		String[] splittedString = digitalSignature.split("[.]");
		//LibRegistrierkasseJNI jni = new LibRegistrierkasseJNI();
	
		String[] result = new String[1];
	
	 	// jni.base64reencodeUrlToNormal(splittedString[2], result);
	 	//System.out.println("the base64reencodeUrlToNormal value:" + result[0]);
	 	return result[0];
	}
	
	
	public static String[] createAustriaDigitalSignature(PaymentTransaction txn,String shopid,String cashregister, String previousSignature, String fsclReceiptNumber,String encripted,String certificate,boolean isReturn ) throws Exception
	{
		String  digitalSignature=null;
		String previousDigitalSignature = previousSignature;
		//LibRegistrierkasseJNI jni = new LibRegistrierkasseJNI();
		ConfigMgr cfgMgr = new ConfigMgr("payment.cfg");
		String finalMessageString="_R1-AT1_";
		int returnSignedKey=0;
		String[] jwsSignature = new String[1];
		String[] returnKey = new String[3];
			
	     if (((CMSCompositePOSTransaction)txn) instanceof CMSCompositePOSTransaction)
	     {
			 CMSCompositePOSTransaction theTxnPOS = ((CMSCompositePOSTransaction) txn);
			 String totalAmount = theTxnPOS.getCompositeTotalAmountDue().toString();
			 String[] splittedString = totalAmount.split("->");
			 String grossTotal = splittedString[1];
			 String totalAmt = theTxnPOS.getSaleRetailAmount().formattedStringValue().substring(1,theTxnPOS.getSaleRetailAmount().formattedStringValue().length());
			 //grossTotal = grossTotal.replace('-', ' ');
			 //grossTotal = grossTotal.trim();			 
			 float numero = Float.parseFloat(grossTotal);
			 //numero = Math.abs(numero);
			 String tot = String.format("%.2f", numero);
     		 DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
			 String d = df.format(theTxnPOS.getCreateDate());
			 
			 // RICCARDINO MODIFICA METTO IL CREATE DATE 
			 // Date date = new Date();
			 Date date = new Date();
			 DateFormat df1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			 String dateTransaction= df1.format(theTxnPOS.getCreateDate());
			 dateTransaction= dateTransaction.replace(" ", "T");
			 
			 // *****   RIPASSO LA PRECEDENTE DIGITALSIGNATURE 
			
			    String[] hashPreviousResult = new String[1];
				//int returnCode = jni.hashSigVorigerBeleg(previousDigitalSignature, 8, hashPreviousResult);
				//System.out.println("hashSigVorigerBeleg() returnCode " + returnCode  +  " value :" + hashPreviousResult[0]);
				String previosTranHash = hashPreviousResult[0];
			 
			 // Compongo la stringa da FIRMARE CON QUESTI CAMPI 
				/*
				 * 
				-	R1-AT1: Until the crypto algorithm changes, it stays the same.
				-	demokasse42: name of the cash register. Choose as you like (No underscores as these are used as separators).
				-	0: receipt ID
				-	2016-06-13T14:22:23: date
				-	0,00: amount prima iva
				-	0,00: amount seconda iva 
				-	0,00: Beleg-Satz-Ermaessigt-2
				-	0,00: Beleg-Satz-Null
				-	0,00: Beleg-Satz-Besonders
				-	Ncdqp924IxGaJgi8UDVmiA==: encrypted turnover counter
				-	6edceb2b: certificate serial  =  encripted
				-	pKrk1IDbcxM=: chaining value  = previosTranHash
				* */
			 
			if (isReturn)	
			{ 
				finalMessageString = finalMessageString.concat(shopid).concat(cashregister).concat("_").concat(fsclReceiptNumber).concat("_").concat(dateTransaction).concat("_").concat(tot).concat("_0,00_0,00_0,00_0,00_").concat("U1RP").concat("_").concat(certificate).concat("_").concat(previosTranHash);
			} else
			    finalMessageString = finalMessageString.concat(shopid).concat(cashregister).concat("_").concat(fsclReceiptNumber).concat("_").concat(dateTransaction).concat("_").concat(tot).concat("_0,00_0,00_0,00_0,00_").concat(encripted).concat("_").concat(certificate).concat("_").concat(previosTranHash);
			 
			
				System.out.println("Message to Cript :"+finalMessageString);
				//returnSignedKey = jni.signJWS(finalMessageString, jwsSignature);
				//System.out.println("Stringa  " + returnCode + " value " + jwsSignature[0]);
	     }
	     
	 	if(returnSignedKey==0)  // se torna ZERO VA TUTTO BENE 
	 	{ 
	 			digitalSignature=jwsSignature[0];
	 			returnKey[0]=jwsSignature[0];
	 			returnKey[1]="0";
	 	} 
	 	else
	 	{   // FACCIO LA PROCEDURA SPECIALE 
	 			//returnSignedKey = jni.signJWSAusgefallen(finalMessageString, jwsSignature);
	 			//digitalSignature=jwsSignature[0];
	 			returnKey[0]=jwsSignature[0];
	 			returnKey[1]="1";
	 	}
	     
	 	// Creo la Stringa per il QR BARCODE
	 	returnKey[2]= finalMessageString.concat("_").concat(createQRbarcodeString(returnKey[0]));
	 	
	 	
	 	 return returnKey;

	}
}