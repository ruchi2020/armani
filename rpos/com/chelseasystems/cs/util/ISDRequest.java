package com.chelseasystems.cs.util;
/*History:
	+------+------------+----------------+-----------+----------------------------------------------------+
	| Ver# | Date       | By             | Defect #  | Description                                        |
	+------+------------+----------------+-----------+----------------------------------------------------+
	| 1    | 08/30/2010 | Vivek Sawant   |           |ISD REquest and response from ISD toolkit.		  |
	------------------------------------------------------------------------------------------------------+
	*
	* formatted with JxBeauty (c) johann.langhofer@nextra.at
	*/

	/*
	 * Encryption Key file is stored in C:/Key17.dat
	 */

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;
import java.util.ResourceBundle;

import imsformat.credit.CCRequest;
import imsformat.credit.CCResponse;

import com.chelseasystems.cr.appmgr.AppManager;
import com.chelseasystems.cr.payment.CreditCard;
import com.chelseasystems.cr.payment.Payment;
import com.chelseasystems.cr.pos.PaymentTransaction;
import com.isd.jec.IsdEncryptionClient;
import com.isd.jec.crypto.CryptoException;
import com.isd.toolkit.IsdJavaToolkit;
import com.isd.toolkit.IsdJavaToolkitConfig;
import com.isd.toolkit.IsdMessageContainer;
import com.isd.toolkit.error.ConfigNotFoundException;
import com.isd.toolkit.error.ConfigReadException;
import com.isd.toolkit.error.InvalidInputException;
import com.isd.toolkit.error.InvalidTimeoutException;
import com.isd.toolkit.error.NoUicResponseException;
import com.isd.toolkit.error.NotInitializedException;
import com.isd.toolkit.error.SocketConnectException;
import com.isd.toolkit.error.SocketReceiveFailedException;
import com.isd.toolkit.error.SocketSendFailedException;
import com.isd.toolkit.error.TimeoutException;

public class ISDRequest {

	 public ISDRequest(){
		 System.out.println("**** BEGINING OF NEW REQUEST AND INITIALIZATION OF ISDTOOLKIT ****");
		 if (!IsdJavaToolkit.isInitialized())
				try {
					IsdJavaToolkit.initialize();
					System.out.println("initialize successfully");
					
				} catch (ConfigNotFoundException e) {
					e.printStackTrace();
				} catch (ConfigReadException e) {
					e.printStackTrace();
				}
		 
	 }
	 
	
	
	//REQUEST WITH ONLY MANDOTORY FIELDS AND ENCRYPTION HKM TOOLKIT USED INSTEAD OF PLAIN DATA
	public void mandotaryEncryptionFieldRequest(){
		IsdMessageContainer container = new IsdMessageContainer();
		//container.setValue(CCRequest.MESSAGE_LENGTH, "0099");
		container.setValue(CCRequest.MESSAGE_ID, "CC");
		container.setValue(CCRequest.JOURNAL_KEY, "010400112831484");
		container.setValue(CCRequest.MESSAGE_TYPE, "0");
		container.setValue(CCRequest.TENDER_TYPE, "03");
		container.setValue(CCRequest.ACCOUNT_NUMBER,"4444333322221111"); //encryption then keep blank
		//container.setValue(CCRequest.ACCOUNT_NUMBER, "0000000000000000"); //encryption then keep blank
		container.setValue(CCRequest.TENDER_AMOUNT, "1700");
		container.setValue(CCRequest.LOCATION, "0104");
		container.setValue(CCRequest.TERMINAL_NUMBER, "0001");
		container.setValue(CCRequest.MESSAGE_SEQUENCE, "0000");
		container.setValue(CCRequest.RETRY_INDICATOR, "0"); //check
		container.setValue(CCRequest.STAND_IN, "0"); 
		container.setValue(CCRequest.HOST_CAPTURE, "N");
		container.setValue(CCRequest.SYSTEM_TRACE_AUDIT_NUMBER, "000000"); 
		container.setValue(CCRequest.LOCAL_TIME, "095715"); //hhmmss
		container.setValue(CCRequest.LOCAL_DATE, "100901"); 
		container.setValue(CCRequest.EXPIRATION_DATE, "1212"); //encryption then keep blank
//		container.setValue(CCRequest.EXPIRATION_DATE, "0000"); //encryption then keep blank
		container.setValue(CCRequest.TRACE_NUMB, "3"); 
		container.setValue(CCRequest.POS_CONDITION_CODE, "00"); 
		container.setValue(CCRequest.CURRENCY_CODE, "840"); 
		container.setValue(CCRequest.POS_TERMINAL_TYPE, "4"); 
		container.setValue(CCRequest.POS_TERMINAL_ENTRY, "2"); 
		container.setValue(CCRequest.POS_PIN_ENTRY, "2"); 
		container.setValue(CCRequest.CVV2_PRESENCE_INDICATOR, "0"); 
		//container.setValue(CCRequest.CVV2_VALUE , "     "); //if this is encrypted place 5 blank char 
		container.setValue(CCRequest.RETRIEVAL_REFERENCE_NUMBER, "          "); 
		container.setValue(CCRequest.SECURITY_CONTROL_INFO , "                "); //if this is encrypted place 16 blank char
	//	container.setValue(CCRequest.TRACK_DATA , "                                                                               "); // if this is encrypted place 79 blank char 
		try {
			System.out.println("setting key id in request "+ISDEncryption.getKeyID());
		//	container.setValue(CCRequest.ENCR_KEY_ID , ISDEncryption.getKeyID());
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (Exception e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}//set encryption id from key file 3 rd line
		container.setValue(CCRequest.ENCR_CODE_SET , "A"); // WHENEVER ENCRYPT PLACE A FOR ASCII
		// ISD Encrytion will call to create binary data of A/c bnumber, CVV2, TrackData, security String, expiration date
		//this binary data will be append in CCRequest during request.
		
		String fmtAccountNum ="4444333322221111";
		String expirationDate ="1212";
		String plainText  = createPlainDataString("4444333322221111","1212");
		
	
		byte[] encryptedValue = null;
		try {
			encryptedValue = IsdEncryptionClient.isdencrypt(ISDEncryption.getHexEncryptedKey(), plainText.getBytes());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		container.setBinaryValue(CCRequest.ENCR_STRING,encryptedValue );
		
		try {
			IsdJavaToolkit.processTransaction(container, 300);
		} catch (NotInitializedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InvalidInputException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SocketConnectException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SocketReceiveFailedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SocketSendFailedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InvalidTimeoutException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (TimeoutException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (NoUicResponseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		String authCode = container.getValue(CCResponse.AUTHORIZATION_CODE);
		String statusCode = container.getValue(CCResponse.STATUS_CODE);
		String resCode = container.getValue(CCResponse.AUTHORIZATION_RESPONSE_CODE);
		String responseMsg  = container.getValue(CCResponse.RESPONSE_MESSAGE);
		
		System.out.println("AUTHORIZATION_CODE : " + authCode);
		System.out.println("STATUS_CODE :" + statusCode);
		System.out.println("AUTHORIZATION_RESPONSE_CODE :" + resCode);
		System.out.println("RESPONSE_MESSAGE :" + responseMsg);
		
		requestall(container);
		responseAll(container);
		
	}
	
/*	public void plainCCNoRequest(){
		IsdMessageContainer container = new IsdMessageContainer();
		//container.setValue(CCRequest.MESSAGE_LENGTH, "0099");
		container.setValue(CCRequest.MESSAGE_ID, "CC");
		container.setValue(CCRequest.JOURNAL_KEY, "01040011283130");
		container.setValue(CCRequest.MESSAGE_TYPE, "0");
		container.setValue(CCRequest.TENDER_TYPE, "03");
		container.setValue(CCRequest.ACCOUNT_NUMBER, "0004444111122223333");
		container.setValue(CCRequest.TENDER_AMOUNT, "00000001700");
		container.setValue(CCRequest.LOCATION, "0104");
		container.setValue(CCRequest.TERMINAL_NUMBER, "0001");
		container.setValue(CCRequest.MESSAGE_SEQUENCE, "0000");
		container.setValue(CCRequest.RETRY_INDICATOR, "0"); //check
		container.setValue(CCRequest.STAND_IN, "0"); 
		container.setValue(CCRequest.SYSTEM_TRACE_AUDIT_NUMBER, "000000"); 
		container.setValue(CCRequest.LOCAL_TIME, "095715"); //hhmmss
		container.setValue(CCRequest.LOCAL_DATE, "100901"); 
		container.setValue(CCRequest.EXPIRATION_DATE, "1012"); 
		container.setValue(CCRequest.TRACE_NUMB, "3"); 
		container.setValue(CCRequest.POS_CONDITION_CODE, "00"); 
		container.setValue(CCRequest.CURRENCY_CODE, "840"); 
		container.setValue(CCRequest.POS_TERMINAL_TYPE, "4"); 
		container.setValue(CCRequest.POS_TERMINAL_ENTRY, "2"); 
		container.setValue(CCRequest.POS_PIN_ENTRY, "2"); 
		container.setValue(CCRequest.CVV2_PRESENCE_INDICATOR, "0"); 
		container.setValue(CCRequest.RETRIEVAL_REFERENCE_NUMBER, "          "); 
		try {
			IsdJavaToolkit.processTransaction(container, 300);
		} catch (NotInitializedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InvalidInputException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SocketConnectException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SocketReceiveFailedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SocketSendFailedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InvalidTimeoutException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (TimeoutException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (NoUicResponseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		String authCode = container.getValue(CCResponse.AUTHORIZATION_CODE);
		String statusCode = container.getValue(CCResponse.STATUS_CODE);
		String resCode = container.getValue(CCResponse.AUTHORIZATION_RESPONSE_CODE);
		String responseMsg  = container.getValue(CCResponse.RESPONSE_MESSAGE);
		
		
		requestall(container);
		responseAll(container);
		
		
		System.out.println("AUTHORIZATION_CODE : " + authCode);
		System.out.println("STATUS_CODE :" + statusCode);
		System.out.println("AUTHORIZATION_RESPONSE_CODE :" + resCode);
		System.out.println("RESPONSE_MESSAGE :" + responseMsg);
		
	}*/
	public static void responseAll(IsdMessageContainer container) {
		StringBuffer response = new StringBuffer();
		response.append(container.getValue(CCResponse.MESSAGE_LENGTH));
		response.append(container.getValue(CCResponse.MESSAGE_ID));
		response.append(container.getValue(CCResponse.JOURNAL_KEY));
		response.append(container.getValue(CCResponse.MESSAGE_TYPE));
		response.append(container.getValue(CCResponse.TENDER_TYPE));
		response.append(container.getValue(CCResponse.ACCOUNT_NUMBER));
		response.append(container.getValue(CCResponse.TENDER_AMOUNT));
		response.append(container.getValue(CCResponse.LOCATION));
		response.append(container.getValue(CCResponse.TERMINAL_NUMBER));
		response.append(container.getValue(CCResponse.MESSAGE_SEQUENCE));
		response.append(container.getValue(CCResponse.USER_DATA));
		response.append(container.getValue(CCResponse.AUTH_DATE));
		response.append(container.getValue(CCResponse.AUTHORIZATION_TIME));
		response.append(container.getValue(CCResponse.HOST_ACTION_CODE));
		response.append(container.getValue(CCResponse.STATUS_CODE));
		response.append(container.getValue(CCResponse.RESPONSE_MESSAGE));
		response.append(container.getValue(CCResponse.MESSAGE_NUMBER));
		response.append(container.getValue(CCResponse.RESPONSE_LENGTH));
		response.append(container.getValue(CCResponse.PRINT_FLAG));
		response.append(container.getValue(CCResponse.MORE_FLAG));
		response.append(container.getValue(CCResponse.AUTHORIZATION_CODE));
		response.append(container.getValue(CCResponse.AUTHORIZATION_RESPONSE_CODE));
		response.append(container.getValue(CCResponse.AUTHORIZATION_RESPONSE_REASON));
		response.append(container.getValue(CCResponse.MERCHANT_ID));
		response.append(container.getValue(CCResponse.ACCOUNT_NUMBER_LENGTH));
		response.append(container.getValue(CCResponse.ACTIVE_KEY_UPDATE));
		response.append(container.getValue(CCResponse.ISD_CARD_TYPE));
		response.append(container.getValue(CCResponse.TRAN_CONF_REQUIRED));
		response.append(container.getValue(CCResponse.FILLER_11));
		response.append(container.getValue(CCResponse.ISD_STATUS_CODE));
		response.append(container.getValue(CCResponse.MERCHANT_TYPE));
		response.append(container.getValue(CCResponse.POS_ENTRY_MODE));
		response.append(container.getValue(CCResponse.RETRIEVAL_REFERENCE_NUMBER));
		response.append(container.getValue(CCResponse.AUTHORIZATION_SOURCE));
		response.append(container.getValue(CCResponse.ADDRESS_VERIFICATION));
		response.append(container.getValue(CCResponse.CARD_TYPE));
		response.append(container.getValue(CCResponse.CURRENCY_CODE));
		response.append(container.getValue(CCResponse.PAYMENT_SERVICE_INDICATOR));
		response.append(container.getValue(CCResponse.TRANSACTION_ID));
		response.append(container.getValue(CCResponse.VALIDATION_CODE));
		response.append(container.getValue(CCResponse.LOCAL_DATE));
		response.append(container.getValue(CCResponse.ASSOCIATION_RETURN_CODE));
		response.append(container.getValue(CCResponse.LOCAL_TIME));
		response.append(container.getValue(CCResponse.SYSTEM_TRACE_AUDIT_NUMBER));
		response.append(container.getValue(CCResponse.EBT_RESPONSE_TEXT));
		response.append(container.getValue(CCResponse.EBT_FOOD_BEGIN_ACCT_BALANCE));
		response.append(container.getValue(CCResponse.EBT_FOOD_BEGIN_ACCT_BALANCE_SIGN));
		response.append(container.getValue(CCResponse.EBT_FOOD_END_ACCT_BALANCE));
		response.append(container.getValue(CCResponse.EBT_FOOD_END_ACCT_BALANCE_SIGN));
		response.append(container.getValue(CCResponse.AVAILABLE_BALANCE));
		response.append(container.getValue(CCResponse.AVAILABLE_BALANCE_SIGN));
		response.append(container.getValue(CCResponse.EBT_CASH_BEGIN_ACCT_BALANCE));
		response.append(container.getValue(CCResponse.EBT_CASH_BEGIN_ACCT_BALANCE_SIGN));
		response.append(container.getValue(CCResponse.EBT_CASH_END_ACCT_BALANCE));
		response.append(container.getValue(CCResponse.EBT_CASH_END_ACCT_BALANCE_SIGN));
		response.append(container.getValue(CCResponse.EBT_CASH_AVAILABLE_ACCT_BALANCE));
		response.append(container.getValue(CCResponse.EBT_CASH_AVAILABLE_ACCT_BALANCE_SIGN));
		response.append(container.getValue(CCResponse.NETWORK_ID));
		response.append(container.getValue(CCResponse.CVV2_RESPONSE_CODE));
		response.append(container.getValue(CCResponse.DATE_N_TIME));
		response.append(container.getValue(CCResponse.DATE_4));
		response.append(container.getValue(CCResponse.ACCOUNT_TYPE));
		response.append(container.getValue(CCResponse.CARD_ID));
		response.append(container.getValue(CCResponse.AUTHORIZATION_AMOUNT));
		response.append(container.getValue(CCResponse.BATCH_NUMBER));
		response.append(container.getValue(CCResponse.NEW_BALANCE));
		response.append(container.getValue(CCResponse.BALANCE_SIGN1));
		response.append(container.getValue(CCResponse.EXPIRATION_DATE));
		response.append(container.getValue(CCResponse.CUSTOMER_NAME));
		response.append(container.getValue(CCResponse.ADDITIONAL_AMOUNTS));
		response.append(container.getValue(CCResponse.THREEDSC_RESP));
		response.append(container.getValue(CCResponse.INVOICE_NUMBER));
		response.append(container.getValue(CCResponse.POS_DATA_CODE));
		response.append(container.getValue(CCResponse.DCC_AMOU));
		response.append(container.getValue(CCResponse.DCC_AMOU_DECM));
		response.append(container.getValue(CCResponse.DCC_RATE));
		response.append(container.getValue(CCResponse.DCC_RATE_DECM));
		response.append(container.getValue(CCResponse.DCC_CURR));
		response.append(container.getValue(CCResponse.DCC_OPT_IND));
		response.append(container.getValue(CCResponse.PROD_DELI_METH));
		response.append(container.getValue(CCResponse.SER_NBR));
		response.append(container.getValue(CCResponse.LOAD_FEE));
		response.append(container.getValue(CCResponse.AUTHORIZATION_ID));
		response.append(container.getValue(CCResponse.EBT_CASE_NUMBER));
		response.append(container.getValue(CCResponse.HOST_REFERENCE));
		response.append(container.getValue(CCResponse.CORPORATION));
		response.append(container.getValue(CCResponse.COMPANY));
		response.append(container.getValue(CCResponse.SUBSIDIARY));
		response.append(container.getValue(CCResponse.LOCATION_10));
		response.append(container.getValue(CCResponse.CARD_LEVEL));
		response.append(container.getValue(CCResponse.TRACK_NUMBER));
		response.append(container.getValue(CCResponse.AUTH_BANK_NUMB));
		response.append(container.getValue(CCResponse.POS_PIN_ENTRY));
		response.append(container.getValue(CCResponse.EXT_POS_DATA_CODE));
		response.append(container.getValue(CCResponse.PROCS_PROC_CODE));
		response.append(container.getValue(CCResponse.PROCS_ENTR_MODE));
		response.append(container.getValue(CCResponse.PROCS_LOCA_TIME));
		response.append(container.getValue(CCResponse.PROCS_LOCA_DATE));
		response.append(container.getValue(CCResponse.PROCS_RESP_CODE));
		response.append(container.getValue(CCResponse.PROCS_POS_DATA));
		response.append(container.getValue(CCResponse.PROCS_TRAC_COND));
		response.append(container.getValue(CCResponse.PROC_ADDR_VERI));
		response.append(container.getValue(CCResponse.MARKET_SPECIFIC_INDICATOR));
		response.append(container.getValue(CCResponse.TOKEN));
		response.append(container.getValue(CCResponse.AVAILABLE_BALANCE_CURRENCY_CODE));
		response.append(container.getValue(CCResponse.NEW_BALANCE_CURRENCY_CODE));
		response.append(container.getValue(CCResponse.CAVV_UCAF_DATA));
		response.append(container.getValue(CCResponse.AUTH_NETWORK_ID));
		response.append(container.getValue(CCResponse.APPR_ADDITIONAL_AMOUNTS));
		response.append(container.getValue(CCResponse.PARTIAL_APPROVE_INDI));
		response.append(container.getValue(CCResponse.TRACE_NUMB));
		response.append(container.getValue(CCResponse.TERMS_CONDITIONS_VERSION_NUMB));
		response.append(container.getValue(CCResponse.RESPONSE_MATCH_POSTAL_CODE));
		response.append(container.getValue(CCResponse.RESPONSE_MATCH_STREET_ADDR));
		response.append(container.getValue(CCResponse.RESPONSE_MATCH_CUST_NAME));
		response.append(container.getValue(CCResponse.RESPONSE_MATCH_CUST_PHONE));
		response.append(container.getValue(CCResponse.RESPONSE_MATCH_EMAIL_ADDR));
		response.append(container.getValue(CCResponse.APR));
		response.append(container.getValue(CCResponse.FEE_PROGRAM_INDICAATOR));
		response.append(container.getValue(CCResponse.APR_TYPE));
		response.append(container.getValue(CCResponse.PROMO_APR));
		response.append(container.getValue(CCResponse.PROMO_APR_TYPE));
		response.append(container.getValue(CCResponse.PROMO_DESCRIPTION));
		response.append(container.getValue(CCResponse.PROMO_DURATION));
		
		System.out.println("RESPONSE  : "+ response.toString());
		
	}
	public static void requestall(IsdMessageContainer container){
	StringBuffer request = new StringBuffer();
	request.append(container.getValue(CCRequest.MESSAGE_LENGTH));
	System.out.println("MESSAGE_LENGTH   :"+container.getValue(CCRequest.MESSAGE_LENGTH));
	request.append(container.getValue(CCRequest.MESSAGE_ID));
	System.out.println("MESSAGE_ID  :"+container.getValue(CCRequest.MESSAGE_ID));
	request.append(container.getValue(CCRequest.JOURNAL_KEY));
	System.out.println("JOURNAL_KEY  :"+container.getValue(CCRequest.JOURNAL_KEY));
	request.append(container.getValue(CCRequest.MESSAGE_TYPE));
	System.out.println("MESSAGE_TYPE  :"+container.getValue(CCRequest.MESSAGE_TYPE));
	request.append(container.getValue(CCRequest.TENDER_TYPE));
	System.out.println("TENDER_TYPE  :"+container.getValue(CCRequest.TENDER_TYPE));
	request.append(container.getValue(CCRequest.ACCOUNT_NUMBER));
	System.out.println("ACCOUNT_NUMBER  :"+container.getValue(CCRequest.ACCOUNT_NUMBER));
	request.append(container.getValue(CCRequest.TENDER_AMOUNT ));
	System.out.println("TENDER_AMOUNT  :"+container.getValue(CCRequest.TENDER_AMOUNT));
	request.append(container.getValue(CCRequest.LOCATION ));
	System.out.println("LOCATION  :"+container.getValue(CCRequest.LOCATION));
	request.append(container.getValue(CCRequest.TERMINAL_NUMBER ));
	System.out.println("TERMINAL_NUMBER  :"+container.getValue(CCRequest.TERMINAL_NUMBER));
	request.append(container.getValue(CCRequest.MESSAGE_SEQUENCE ));
	System.out.println("MESSAGE_SEQUENCE  :"+container.getValue(CCRequest.MESSAGE_SEQUENCE));
	request.append(container.getValue(CCRequest.USER_DATA ));
	System.out.println("USER_DATA  :"+container.getValue(CCRequest.USER_DATA));
	request.append(container.getValue(CCRequest.OUTSTANDING_COUNT ));
	System.out.println("JOURNAL_KEY  :"+container.getValue(CCRequest.JOURNAL_KEY));
	request.append(container.getValue(CCRequest.RETRY_INDICATOR));
	System.out.println("OUTSTANDING_COUNT  :"+container.getValue(CCRequest.OUTSTANDING_COUNT));
	request.append(container.getValue(CCRequest.STAND_IN ));
	System.out.println("JOURNAL_KEY  :"+container.getValue(CCRequest.JOURNAL_KEY));
	request.append(container.getValue(CCRequest.TRANSPORT_INDICATOR));
	System.out.println("JOURNAL_KEY  :"+container.getValue(CCRequest.JOURNAL_KEY));
	request.append(container.getValue(CCRequest.HOST_CAPTURE));
	System.out.println("JOURNAL_KEY  :"+container.getValue(CCRequest.JOURNAL_KEY));
	request.append(container.getValue(CCRequest.ACCOUNT_NUMBER_LENGTH));
	System.out.println("JOURNAL_KEY  :"+container.getValue(CCRequest.JOURNAL_KEY));
	request.append(container.getValue(CCRequest.FILLER_17));
	System.out.println("JOURNAL_KEY  :"+container.getValue(CCRequest.JOURNAL_KEY));
	request.append(container.getValue(CCRequest.REVERSAL_REPLACEMENT_AMOUNT ));
	System.out.println("JOURNAL_KEY  :"+container.getValue(CCRequest.JOURNAL_KEY));
	request.append(container.getValue(CCRequest.SYSTEM_TRACE_AUDIT_NUMBER));
	System.out.println("JOURNAL_KEY  :"+container.getValue(CCRequest.JOURNAL_KEY));
	request.append(container.getValue(CCRequest.LOCAL_TIME ));
	System.out.println("JOURNAL_KEY  :"+container.getValue(CCRequest.JOURNAL_KEY));
	request.append(container.getValue(CCRequest.LOCAL_DATE ));
	System.out.println("JOURNAL_KEY  :"+container.getValue(CCRequest.JOURNAL_KEY));
	request.append(container.getValue(CCRequest.EXPIRATION_DATE));
	System.out.println("JOURNAL_KEY  :"+container.getValue(CCRequest.JOURNAL_KEY));
	request.append(container.getValue(CCRequest.TRACK_NUMBER));
	System.out.println("JOURNAL_KEY  :"+container.getValue(CCRequest.JOURNAL_KEY));
	request.append(container.getValue(CCRequest.POS_CONDITION_CODE));
	System.out.println("JOURNAL_KEY  :"+container.getValue(CCRequest.JOURNAL_KEY));
	request.append(container.getValue(CCRequest.TRACK_DATA));
	System.out.println("JOURNAL_KEY  :"+container.getValue(CCRequest.JOURNAL_KEY));
	request.append(container.getValue(CCRequest.RETRIEVAL_REFERENCE_NUMBER));
	System.out.println("JOURNAL_KEY  :"+container.getValue(CCRequest.JOURNAL_KEY));
	request.append(container.getValue(CCRequest.AUTHORIZATION_CODE));
	System.out.println("JOURNAL_KEY  :"+container.getValue(CCRequest.JOURNAL_KEY));
	request.append(container.getValue(CCRequest.ACCOUNT_DATA_SOURCE));
	System.out.println("JOURNAL_KEY  :"+container.getValue(CCRequest.JOURNAL_KEY));
	request.append(container.getValue(CCRequest.CURRENCY_CODE));
	System.out.println("JOURNAL_KEY  :"+container.getValue(CCRequest.JOURNAL_KEY));
	request.append(container.getValue(CCRequest.PIN_DATA));
	System.out.println("JOURNAL_KEY  :"+container.getValue(CCRequest.JOURNAL_KEY));
	request.append(container.getValue(CCRequest.SECURITY_CONTROL_INFO));
	System.out.println("JOURNAL_KEY  :"+container.getValue(CCRequest.JOURNAL_KEY));
	request.append(container.getValue(CCRequest.ADDITIONAL_AMOUNTS));
	System.out.println("JOURNAL_KEY  :"+container.getValue(CCRequest.JOURNAL_KEY));
	request.append(container.getValue(CCRequest.POS_TERMINAL_TYPE));
	System.out.println("JOURNAL_KEY  :"+container.getValue(CCRequest.JOURNAL_KEY));
	request.append(container.getValue(CCRequest.POS_TERMINAL_ENTRY));
	System.out.println("JOURNAL_KEY  :"+container.getValue(CCRequest.JOURNAL_KEY));
	request.append(container.getValue(CCRequest.PAYMENT_SERVICE_INDICATOR));
	System.out.println("JOURNAL_KEY  :"+container.getValue(CCRequest.JOURNAL_KEY));
	request.append(container.getValue(CCRequest.TRANSACTION_ID));
	System.out.println("JOURNAL_KEY  :"+container.getValue(CCRequest.JOURNAL_KEY));
	request.append(container.getValue(CCRequest.ZIP_CODE1));
	System.out.println("JOURNAL_KEY  :"+container.getValue(CCRequest.JOURNAL_KEY));
	request.append(container.getValue(CCRequest.ADDRESS_20));
	System.out.println("JOURNAL_KEY  :"+container.getValue(CCRequest.JOURNAL_KEY));
	request.append(container.getValue(CCRequest.POS_PIN_ENTRY));
	System.out.println("JOURNAL_KEY  :"+container.getValue(CCRequest.JOURNAL_KEY));
	request.append(container.getValue(CCRequest.CREDIT_PLAN));
	System.out.println("JOURNAL_KEY  :"+container.getValue(CCRequest.JOURNAL_KEY));
	request.append(container.getValue(CCRequest.NAME_30));
	System.out.println("JOURNAL_KEY  :"+container.getValue(CCRequest.JOURNAL_KEY));
	request.append(container.getValue(CCRequest.ADDRESS1_24));
	System.out.println("JOURNAL_KEY  :"+container.getValue(CCRequest.JOURNAL_KEY));
	request.append(container.getValue(CCRequest.ADDRESS2_24));
	System.out.println("JOURNAL_KEY  :"+container.getValue(CCRequest.JOURNAL_KEY));
	request.append(container.getValue(CCRequest.CITY_20));
	System.out.println("JOURNAL_KEY  :"+container.getValue(CCRequest.JOURNAL_KEY));
	request.append(container.getValue(CCRequest.STATE));
	System.out.println("JOURNAL_KEY  :"+container.getValue(CCRequest.JOURNAL_KEY));
	request.append(container.getValue(CCRequest.ZIP_CODE2));
	System.out.println("JOURNAL_KEY  :"+container.getValue(CCRequest.JOURNAL_KEY));
	request.append(container.getValue(CCRequest.EBT_VOUCHER_NUMBER));
	System.out.println("JOURNAL_KEY  :"+container.getValue(CCRequest.JOURNAL_KEY));
	request.append(container.getValue(CCRequest.KSN_FORMAT));
	System.out.println("JOURNAL_KEY  :"+container.getValue(CCRequest.JOURNAL_KEY));
	request.append(container.getValue(CCRequest.CVV2_PRESENCE_INDICATOR));
	System.out.println("JOURNAL_KEY  :"+container.getValue(CCRequest.JOURNAL_KEY));
	request.append(container.getValue(CCRequest.CVV2_VALUE));
	System.out.println("JOURNAL_KEY  :"+container.getValue(CCRequest.JOURNAL_KEY));
	request.append(container.getValue(CCRequest.KSN_20));
	System.out.println("JOURNAL_KEY  :"+container.getValue(CCRequest.JOURNAL_KEY));
	request.append(container.getValue(CCRequest.TERMINAL_ID_11));
	System.out.println("JOURNAL_KEY  :"+container.getValue(CCRequest.JOURNAL_KEY));
	request.append(container.getValue(CCRequest.CARD_START_DATE));
	System.out.println("JOURNAL_KEY  :"+container.getValue(CCRequest.JOURNAL_KEY));
	request.append(container.getValue(CCRequest.CARD_ISSUE_NUMBER));
	System.out.println("JOURNAL_KEY  :"+container.getValue(CCRequest.JOURNAL_KEY));
	request.append(container.getValue(CCRequest.CARD_HOLDER_IP_ADDRESS));
	System.out.println("JOURNAL_KEY  :"+container.getValue(CCRequest.JOURNAL_KEY));
	request.append(container.getValue(CCRequest.DELIVERY_METHOD));
	System.out.println("JOURNAL_KEY  :"+container.getValue(CCRequest.JOURNAL_KEY));
	request.append(container.getValue(CCRequest.BILLING_METHOD));
	System.out.println("JOURNAL_KEY  :"+container.getValue(CCRequest.JOURNAL_KEY));
	request.append(container.getValue(CCRequest.AUTHORIZATION_AMOUNT));
	System.out.println("JOURNAL_KEY  :"+container.getValue(CCRequest.JOURNAL_KEY));
	request.append(container.getValue(CCRequest.AUTHORIZATION_RESPONSE_CODE));
	System.out.println("JOURNAL_KEY  :"+container.getValue(CCRequest.JOURNAL_KEY));
	request.append(container.getValue(CCRequest.TRANSACTION_TYPE));
	System.out.println("JOURNAL_KEY  :"+container.getValue(CCRequest.JOURNAL_KEY));
	request.append(container.getValue(CCRequest.MERCHANT_TYPE));
	System.out.println("JOURNAL_KEY  :"+container.getValue(CCRequest.JOURNAL_KEY));
	request.append(container.getValue(CCRequest.BATCH_NUMBER));
	System.out.println("JOURNAL_KEY  :"+container.getValue(CCRequest.JOURNAL_KEY));
	request.append(container.getValue(CCRequest.OPERATOR_ID));
	System.out.println("JOURNAL_KEY  :"+container.getValue(CCRequest.JOURNAL_KEY));
	request.append(container.getValue(CCRequest.VALIDATION_CODE));
	System.out.println("JOURNAL_KEY  :"+container.getValue(CCRequest.JOURNAL_KEY));
	request.append(container.getValue(CCRequest.AUTHORIZATION_SOURCE));
	System.out.println("JOURNAL_KEY  :"+container.getValue(CCRequest.JOURNAL_KEY));
	request.append(container.getValue(CCRequest.COUNTRY_CODE));
	System.out.println("JOURNAL_KEY  :"+container.getValue(CCRequest.JOURNAL_KEY));
	request.append(container.getValue(CCRequest.AUTH_DTAQ_NAME));
	System.out.println("JOURNAL_KEY  :"+container.getValue(CCRequest.JOURNAL_KEY));
	request.append(container.getValue(CCRequest.PIN_DATA_2));
	System.out.println("JOURNAL_KEY  :"+container.getValue(CCRequest.JOURNAL_KEY));
	request.append(container.getValue(CCRequest.INVOICE_NUMBER));
	System.out.println("JOURNAL_KEY  :"+container.getValue(CCRequest.JOURNAL_KEY));
	request.append(container.getValue(CCRequest.FILLER));
	System.out.println("JOURNAL_KEY  :"+container.getValue(CCRequest.JOURNAL_KEY));
	request.append(container.getValue(CCRequest.ENCR_CODE_SET));
	System.out.println("JOURNAL_KEY  :"+container.getValue(CCRequest.JOURNAL_KEY));
	request.append(container.getValue(CCRequest.ENCR_STRING));
	System.out.println("JOURNAL_KEY  :"+container.getValue(CCRequest.JOURNAL_KEY));
	request.append(container.getValue(CCRequest.THREEDSC_ENROLL_RESPONSE));
	System.out.println("JOURNAL_KEY  :"+container.getValue(CCRequest.JOURNAL_KEY));
	request.append(container.getValue(CCRequest.THREEDSC_TRANSACTION_STATUS));
	System.out.println("JOURNAL_KEY  :"+container.getValue(CCRequest.JOURNAL_KEY));
	request.append(container.getValue(CCRequest.CAVV_UCAF_DATA));
	System.out.println("JOURNAL_KEY  :"+container.getValue(CCRequest.JOURNAL_KEY));
	request.append(container.getValue(CCRequest.XID_DATA));
	System.out.println("JOURNAL_KEY  :"+container.getValue(CCRequest.JOURNAL_KEY));
	request.append(container.getValue(CCRequest.CUSTOMER_CODE));
	System.out.println("JOURNAL_KEY  :"+container.getValue(CCRequest.JOURNAL_KEY));
	request.append(container.getValue(CCRequest.SALES_TAX));
	System.out.println("JOURNAL_KEY  :"+container.getValue(CCRequest.JOURNAL_KEY));
	request.append(container.getValue(CCRequest.SALES_TAX_INDI));
	System.out.println("JOURNAL_KEY  :"+container.getValue(CCRequest.JOURNAL_KEY));
	request.append(container.getValue(CCRequest.EMAIL_60));
	System.out.println("JOURNAL_KEY  :"+container.getValue(CCRequest.JOURNAL_KEY));
	request.append(container.getValue(CCRequest.HTTP_BROWSER_TYPE));
	System.out.println("JOURNAL_KEY  :"+container.getValue(CCRequest.JOURNAL_KEY));
	request.append(container.getValue(CCRequest.SHIP_COUNRTY));
	System.out.println("JOURNAL_KEY  :"+container.getValue(CCRequest.JOURNAL_KEY));
	request.append(container.getValue(CCRequest.PROD_UPC_CODE));
	System.out.println("JOURNAL_KEY  :"+container.getValue(CCRequest.JOURNAL_KEY));
	request.append(container.getValue(CCRequest.CUSTOMER_ANI));
	System.out.println("JOURNAL_KEY  :"+container.getValue(CCRequest.JOURNAL_KEY));
	request.append(container.getValue(CCRequest.CUSTOMER_II_DIGITS));
	System.out.println("JOURNAL_KEY  :"+container.getValue(CCRequest.JOURNAL_KEY));
	request.append(container.getValue(CCRequest.CARD_FIRST_NAME));
	System.out.println("JOURNAL_KEY  :"+container.getValue(CCRequest.JOURNAL_KEY));
	request.append(container.getValue(CCRequest.CARD_LAST_NAME));
	System.out.println("JOURNAL_KEY  :"+container.getValue(CCRequest.JOURNAL_KEY));
	request.append(container.getValue(CCRequest.CARD_PHONE));
	System.out.println("JOURNAL_KEY  :"+container.getValue(CCRequest.JOURNAL_KEY));
	request.append(container.getValue(CCRequest.SHIP_FIRST_NAME));
	System.out.println("JOURNAL_KEY  :"+container.getValue(CCRequest.JOURNAL_KEY));
	request.append(container.getValue(CCRequest.SHIP_PHONE));
	System.out.println("JOURNAL_KEY  :"+container.getValue(CCRequest.JOURNAL_KEY));
	request.append(container.getValue(CCRequest.PARTIAL_APPROVE_INDI));
	System.out.println("JOURNAL_KEY  :"+container.getValue(CCRequest.JOURNAL_KEY));
	request.append(container.getValue(CCRequest.DCC_AMOU));
	System.out.println("JOURNAL_KEY  :"+container.getValue(CCRequest.JOURNAL_KEY));
	request.append(container.getValue(CCRequest.DCC_AMOU_DECM));
	System.out.println("JOURNAL_KEY  :"+container.getValue(CCRequest.JOURNAL_KEY));
	request.append(container.getValue(CCRequest.DCC_RATE));
	System.out.println("JOURNAL_KEY  :"+container.getValue(CCRequest.JOURNAL_KEY));
	request.append(container.getValue(CCRequest.DCC_RATE_DECM));
	System.out.println("JOURNAL_KEY  :"+container.getValue(CCRequest.JOURNAL_KEY));
	request.append(container.getValue(CCRequest.DCC_CURR));
	System.out.println("JOURNAL_KEY  :"+container.getValue(CCRequest.JOURNAL_KEY));
	request.append(container.getValue(CCRequest.DCC_OPT_IND));
	System.out.println("JOURNAL_KEY  :"+container.getValue(CCRequest.JOURNAL_KEY));
	request.append(container.getValue(CCRequest.SER_NBR));
	System.out.println("JOURNAL_KEY  :"+container.getValue(CCRequest.JOURNAL_KEY));
	request.append(container.getValue(CCRequest.EBT_CASE_NUMBER));
	System.out.println("JOURNAL_KEY  :"+container.getValue(CCRequest.JOURNAL_KEY));
	request.append(container.getValue(CCRequest.CARD_SEQUENCE));
	System.out.println("JOURNAL_KEY  :"+container.getValue(CCRequest.JOURNAL_KEY));
	request.append(container.getValue(CCRequest.HOST_REFERENCE));
	System.out.println("JOURNAL_KEY  :"+container.getValue(CCRequest.JOURNAL_KEY));
	request.append(container.getValue(CCRequest.ENCR_KEY_ID));
	System.out.println("JOURNAL_KEY  :"+container.getValue(CCRequest.JOURNAL_KEY));
	request.append(container.getValue(CCRequest.COMPANY));
	System.out.println("JOURNAL_KEY  :"+container.getValue(CCRequest.JOURNAL_KEY));
	request.append(container.getValue(CCRequest.SUBSIDIARY));
	System.out.println("JOURNAL_KEY  :"+container.getValue(CCRequest.JOURNAL_KEY));
	request.append(container.getValue(CCRequest.LOCATION_10));
	System.out.println("JOURNAL_KEY  :"+container.getValue(CCRequest.JOURNAL_KEY));
	request.append(container.getValue(CCRequest.MARKET_SPECIFIC_INDICATOR));
	System.out.println("JOURNAL_KEY  :"+container.getValue(CCRequest.JOURNAL_KEY));
	request.append(container.getValue(CCRequest.HEALTHCARE_TOTAL_AMOUNT));
	System.out.println("JOURNAL_KEY  :"+container.getValue(CCRequest.JOURNAL_KEY));
	request.append(container.getValue(CCRequest.PRESCRIPTION_TOTAL_AMOUNT));
	System.out.println("JOURNAL_KEY  :"+container.getValue(CCRequest.JOURNAL_KEY));
	request.append(container.getValue(CCRequest.OPTICAL_TOTAL_AMOUNT));
	System.out.println("JOURNAL_KEY  :"+container.getValue(CCRequest.JOURNAL_KEY));
	request.append(container.getValue(CCRequest.MEDICAL_TOTAL_AMOUNT));
	System.out.println("JOURNAL_KEY  :"+container.getValue(CCRequest.JOURNAL_KEY));
	request.append(container.getValue(CCRequest.DENTAL_TOTAL_AMOUNT));
	System.out.println("JOURNAL_KEY  :"+container.getValue(CCRequest.JOURNAL_KEY));
	request.append(container.getValue(CCRequest.TOKEN));
	System.out.println("JOURNAL_KEY  :"+container.getValue(CCRequest.JOURNAL_KEY));
	request.append(container.getValue(CCRequest.ENCR_STRING2));
	System.out.println("JOURNAL_KEY  :"+container.getValue(CCRequest.JOURNAL_KEY));
	request.append(container.getValue(CCRequest.PRODUCT_CODE28));
	System.out.println("JOURNAL_KEY  :"+container.getValue(CCRequest.JOURNAL_KEY));
	request.append(container.getValue(CCRequest.TRACE_NUMB));
	System.out.println("JOURNAL_KEY  :"+container.getValue(CCRequest.JOURNAL_KEY));
	request.append(container.getValue(CCRequest.BIRTH_DATE1_8));
	System.out.println("BIRTH_DATE1_8  :"+container.getValue(CCRequest.BIRTH_DATE1_8));
	request.append(container.getValue(CCRequest.ACCOUNT_ID_CODE));
	System.out.println("ACCOUNT_ID_CODE  :"+container.getValue(CCRequest.ACCOUNT_ID_CODE));
	request.append(container.getValue(CCRequest.IDENTIFICATION_DATA));
	System.out.println("IDENTIFICATION_DATA  :"+container.getValue(CCRequest.IDENTIFICATION_DATA));
	request.append(container.getValue(CCRequest.REQUEST_ABSTRACT_TOKEN));
	System.out.println("REQUEST_ABSTRACT_TOKEN  :"+container.getValue(CCRequest.REQUEST_ABSTRACT_TOKEN));

	System.out.println("***************************************************");
	System.out.println("REQUEST : "+ request.toString());
	System.out.println("***************************************************");
	
	}
	
	

	public static void main(String[] args) {
		ISDRequest request = new ISDRequest();
		request.mandotaryEncryptionFieldRequest();

	}
	public String createPlainDataString(String accountNo, String expDate){
		 String fmtAccountNo = getRightJustifiedAccountNumber(accountNo);
		 String cvv2 = createBlankString(5);
		 String trackdata = createBlankString(79);
		 String srci = createBlankString(16);
	     String plainText  = fmtAccountNo+cvv2+trackdata+srci+expDate;
	     return plainText;
	}
	private static final int MAX_ACCT_NUM_LENGTH = 19;
	boolean verboseMode = false;
	private String getRightJustifiedAccountNumber(String accountNum) {
	    String inAcctNum = accountNum.trim();
	    int diff = MAX_ACCT_NUM_LENGTH - inAcctNum.length();
	    if ((diff < 0) && (verboseMode)) {
	      System.out.println("ISD.getRightJustifiedAccountNumber: ERROR account number too large");
	    }
	    return getRightJustifiedNumber(inAcctNum, MAX_ACCT_NUM_LENGTH);
	  }
	private String createBlankString(int size) {
	    char array[] = new char[size];
	    for (int i = 0; i < size; i++) {
	      array[i] = ' ';
	    }
	    String str = new String(array);
	    array = null;
	    return str;
	  }
	 private String getRightJustifiedNumber(String orgString, int leng) {
		    String inStr = orgString.trim();
		    int diff = leng - inStr.length();
		    String result = null;
		    if (diff > 0) {
		      String temp = createZeroString(diff);
		      result = temp.concat(inStr);
		    } else if (diff < 0) {
		      if (verboseMode) {
		        System.err.println(
		            "INFO -- ISDValidation.getRightJustifiedNumber() input too large.  Input: >" + inStr
		            + "<   Max. length: " + leng);
		      }
		      result = inStr.substring(0, leng);
		    } else {
		      result = new String(inStr);
		    }
		    return result;
		  }

	 private String createZeroString(int size) {
		    char array[] = new char[size];
		    for (int i = 0; i < size; i++) {
		      array[i] = '0';
		    }
		    String str = new String(array);
		    array = null;
		    return str;
		  }
}
