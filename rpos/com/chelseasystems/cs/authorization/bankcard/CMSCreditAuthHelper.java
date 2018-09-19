/*
 * @copyright (c) 2001 Chelsea Market Systems LLC
 *
 * formatted with JxBeauty (c) johann.langhofer@nextra.at
 */

package com.chelseasystems.cs.authorization.bankcard;

import java.io.IOException;
import java.net.SocketException;
import java.net.SocketTimeoutException;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Enumeration;

import oracle.net.ns.RefusePacket;

import org.apache.log4j.Logger;

import com.chelseasystems.cr.appmgr.IApplicationManager;
import com.chelseasystems.cr.appmgr.IRepositoryManager;
import com.chelseasystems.cr.config.ConfigMgr;
import com.chelseasystems.cr.currency.ArmCurrency;
import com.chelseasystems.cr.logging.LoggingServices;
import com.chelseasystems.cr.payment.CreditCard;
import com.chelseasystems.cr.payment.DueBill;
import com.chelseasystems.cr.payment.IAuthCancellationRequired;
import com.chelseasystems.cr.payment.ISignatureValidatable;
import com.chelseasystems.cr.payment.Payment;
import com.chelseasystems.cr.payment.Redeemable;
import com.chelseasystems.cr.pos.POSLineItem;
import com.chelseasystems.cr.pos.POSLineItemDetail;
import com.chelseasystems.cr.pos.PaymentTransaction;
import com.chelseasystems.cr.pos.RedeemableBuyBackTransaction;
import com.chelseasystems.cr.rules.BusinessRuleException;
import com.chelseasystems.cs.ajbauthorization.AJBCardOnFileFormatter;
import com.chelseasystems.cs.ajbauthorization.AJBCreditDebitFormatter;
import com.chelseasystems.cs.ajbauthorization.AJBGiftCardFormatter;
import com.chelseasystems.cs.ajbauthorization.AJBMessageCodes;
import com.chelseasystems.cs.ajbauthorization.AJBPingThread;
import com.chelseasystems.cs.ajbauthorization.AJBRequestResponseMessage;
import com.chelseasystems.cs.ajbauthorization.AJBServiceManager;
import com.chelseasystems.cs.ajbauthorization.bankcard.CMSCreditAuthAJBServices;
import com.chelseasystems.cs.config.AJBPingThreadBootStrap;
import com.chelseasystems.cs.customer.CustomerCreditCard;
import com.chelseasystems.cs.payment.CMSDebitCard;
import com.chelseasystems.cs.payment.CMSDueBill;
import com.chelseasystems.cs.payment.CMSDueBillIssue;
import com.chelseasystems.cs.payment.CMSRedeemable;
import com.chelseasystems.cs.payment.CMSStoreValueCard;
import com.chelseasystems.cs.pos.CMSRedeemableBuyBackTransaction;
import com.chelseasystems.cs.pos.CMSRedeemableBuyBackTransactionAppModel;

/**
 * @description: Helper Class for CreditAuthClientServices.
 * @author: Christian Greene
 */
public class CMSCreditAuthHelper {

	static boolean verboseMode = false;
	static boolean localTestMode = false;
	private ConfigMgr config;
	private static ConfigMgr storeConfig;
	private static String fipay_flag;
	private static String gcInt_flag;
	static boolean success = false;
	private static String trackData = null; //Added by Himani for fetching TrackData for Reload GC on 20 SEP 2016
	/*Added to log info and errors.*/
	private static Logger log = Logger.getLogger(AJBServiceManager.class.getName());
	
	//Vivek Mishra : Added for Gift Card AJB integration changes 27-SEP-2016 
	static boolean retry = false;
	static String error_message = "All the Ajb Servers are down at the moment";
	static String responseErrorMsg = "";
	static AJBGiftCardFormatter formatter = new AJBGiftCardFormatter();
	static Object request = (formatter.formatAJBGiftCardSwipeRequest()).toString();
	static Object requestManual = (formatter.formatAJBGiftCardManualEntryRequest()).toString();
	static Object response = null;
	static int retryCount = 0;
	static String accountNo = null;
	static String ajbSequenceNo = null;
	static boolean isManualEntry = false;
	private static Logger logger = Logger.getLogger(CMSCreditAuthHelper.class.getName());
	//Ends here 27-SEP-2016
	
		public CMSCreditAuthHelper(){
			config = new ConfigMgr("credit_auth.cfg");
		    String strLocalTestMode = config.getString("LOCAL_TEST_MODE");
		    String strVerboseMode = config.getString("VERBOSE_MODE");
				if (strLocalTestMode.trim().equalsIgnoreCase("TRUE")) {
					localTestMode = true;
				}
				if (strVerboseMode.trim().equalsIgnoreCase("TRUE")) {
					verboseMode = true;
				}
		}
	/**
	 * @param theMgr
	 * @param s
	 * @return
	 * @exception Exception
	 */
	public static  Object[] getAuthorization(IRepositoryManager theMgr,
			Object[] request) throws Exception {
		//commented the below code as we dont need to send the call to server side
		/*CMSCreditAuthClientServices creditauthServices = (CMSCreditAuthClientServices) theMgr
				.getGlobalObject("CREDIT_AUTH_SRVC");
		return creditauthServices.getAuth(s);*/
		
		try {

	  		int request_length = request.length;
	  		String[] reply = new String[request_length];
	  		String[] tempReply = new String[request_length];
	  		String testType = "";
	  		String strDollarAmount = "";
	  		double testInt = 0.0;
	  		if (verboseMode) {
	  			System.out
	  					.println("CMSCreditAuthHelper.getAuth(): No requests = "
	  							+ request_length);
	  		}
	  		for (int i = 0; i < request_length; i++) {
	  			request[i]=request[i].toString();
	  			try {
	  				if (!localTestMode) {
	  					// Initialize reply in case exception is thrown in
	  					// getCreditAuth() method.
	  					reply[i] = "";
	  					/* Vivek Mishra : Added for AJBSever Connection Validation */
	  					//if (AJBServiceManager.getCurrent().error_message != null) {
	  					//Vivek Mishra : Changed the condition for fixing AJB sever down bug.
	  					
	  					
	  					/*if((AJBServiceManager.getCurrent()).getErrorMessage()!=null)
	  					{
	  						reply[i] = (AJBServiceManager.getCurrent()).getErrorMessage();
	  						return reply;
	  					}*/
	  					// End
	  				//Vivek Mishra : Added to avoid sending null request to AJB 13-SEP-2016
                        if(request[i]!=null){//Ends
	  					if (!AJBPingThreadBootStrap.echoping())
	  					{
	  						reply[i] = "All the Ajb Servers are down at the moment";
	  						return reply;
	  					}
	  					tempReply[i] = (AJBServiceManager.getCurrent()).getCreditAuth((String) request[i]);
	  					//log.info(":::::::::::::::::::::::::::::::: temp reply : " +tempReply[i]);
	  					if (tempReply[i] != null && tempReply[i] != "") {
	  						reply[i] = tempReply[i];

	  					}//Vivek Mishra : Added for null response from AJB issue
	  					else
	  					{
	  						reply[i] = "All the Ajb Servers are down at the moment.";
	  						log.info(":::::::::::::::::::::::::::::::::::::::reply[i] : " +reply[i]);
	  					}//Ends
	  				}
	  			
	  				if (localTestMode) {
	  					testType = ((String) request[i]).substring(4, 6);
	  					strDollarAmount = ((String) request[i]).substring(44, 55);
	  					NumberFormat nfmt = NumberFormat.getNumberInstance();
	  					Number dollarAmount = nfmt.parse(strDollarAmount);
	  					if (dollarAmount.doubleValue() > 0) {
	  						testInt = dollarAmount.doubleValue() / 100;
	  					}

	  				} // End of if ( localTestMode )
	  				}
	  			} catch (SocketException e)
	  			{
	  				// TODO Auto-generated catch block
	  				log.error("SocketException     "+ e);
	  				throw new SocketTimeoutException("Card Response Timeout") ;
	  			}
	  			catch (IOException io)
	  			{
	  				log.error( "Error while writing a request to a socket" + io.toString());
	  				throw io;
	  			}catch (Exception e) { // End of Try
	  				LoggingServices.getCurrent().logMsg(CMSCreditAuthHelper.class.toString(),
	  						"getAuth()", "Exception occured: " + e,
	  						"Verify AJBService running", LoggingServices.MAJOR);
	  				System.err
	  						.println("CMSCreditAuthHelper.getAuth(): exception = "
	  								+ e);
	  				log.error(e.toString());
	  				return reply;
	  			}
	  		} // End of for (int i = 0; i< request_length; i++)

	  		return reply;
	  	
	      }finally{
	    	  
	      }

	}
	
	public static  Object getAuthorization(IRepositoryManager theMgr,
			Object request) throws Exception {
		//commented the below code as we dont need to send the call to server side
		/*CMSCreditAuthClientServices creditauthServices = (CMSCreditAuthClientServices) theMgr
				.getGlobalObject("CREDIT_AUTH_SRVC");
		return creditauthServices.getAuth(s);*/
		
		String reply = new String();
		String tempReply = new String();
		String testType = "";
		String strDollarAmount = "";
		double testInt = 0.0;
		if (verboseMode) {
			System.out.println("CMSCreditAuthHelper.getAuth(): No requests = "+ request);
		}
		try 
		{
			if (!localTestMode) {
				reply = "";
				if (!AJBPingThreadBootStrap.echoping())
				{
					reply = "All the Ajb Servers are down at the moment.";
					return reply;
				}
				// End
				tempReply = (AJBServiceManager.getCurrent()).getCreditAuth((String) request);
				log.info(":::::::::::::::::::::::::::::::: temp reply : " +tempReply);
				if (tempReply != null && tempReply != "") 
				{
					reply = tempReply;		
					
				}//Vivek Mishra : Added for null response from AJB issue
				else
				{
					reply = "All the Ajb Servers are down at the moment.";
					log.info(":::::::::::::::::::::::::::::::::::::::reply[i] : " +reply);
				}//Ends
			}
		} catch (SocketException e)
		{
			// TODO Auto-generated catch block
			log.error("SocketException     "+ e);
			throw new SocketTimeoutException("Card Response Timeout") ;
		}
		catch (IOException io)
		{
			log.error( "Error while writing a request to a socket" + io.toString());
			throw io;
		}
		catch (Exception e) { // End of Try
			LoggingServices.getCurrent().logMsg(CMSCreditAuthHelper.class.toString(),
					"getAuth()", "Exception occured: " + e,
					"Verify AJBService running", LoggingServices.MAJOR);
			System.err.println("CMSCreditAuthHelper.getAuth(): exception = "+ e	);
			log.error(e.toString());	
			//throw 
			return reply;
		}
		// End of for (int i = 0; i< request_length; i++	)
		return reply;
	}


	/**
	 * put your documentation comment here
	 * 
	 * @param theMgr
	 * @param s
	 * @return
	 * @exception Exception
	 */
	public static Object getDCC(IRepositoryManager theMgr, Object s)
			throws Exception {
		CMSCreditAuthClientServices creditauthServices = (CMSCreditAuthClientServices) theMgr
				.getGlobalObject("CREDIT_AUTH_SRVC");
		return creditauthServices.getDCC(s);
	}

	/**
	 * put your documentation comment here
	 * 
	 * @param theMgr
	 * @param s
	 * @param payment
	 * @exception Exception
	 */
	public static void setSignatureValidation(IRepositoryManager theMgr,
			Object s, ISignatureValidatable payment) throws Exception {
		CMSCreditAuthClientServices creditauthServices = (CMSCreditAuthClientServices) theMgr
				.getGlobalObject("CREDIT_AUTH_SRVC");
		payment.setSignatureValidation(creditauthServices
				.setSignatureValidation(s));
	}

	/**
	 * put your documentation comment here
	 * 
	 * @param theMgr
	 * @param s
	 * @param payment
	 * @exception Exception
	 */
	public static void setAuthCancelled(IRepositoryManager theMgr, Object s,
			IAuthCancellationRequired payment) throws Exception {
		CMSCreditAuthClientServices creditauthServices = (CMSCreditAuthClientServices) theMgr
				.getGlobalObject("CREDIT_AUTH_SRVC");
		payment.setCancellationRequest(creditauthServices.setAuthCancelled(s));
	}

	/**
	 * This method iterates through the payment list to: 1) obtain the credit
	 * request for each payment which needs to be authorized; 2) obtain the
	 * credit responce via CreditAuthServices, 3) then set the authorization for
	 * the payment. Note: terminalId is defaulted to "1" with this method.
	 * 
	 * @return An array of Strings containing the authorization status code.
	 */
	public static String[] validate(IRepositoryManager theMgr,
			PaymentTransaction theTxn) throws Exception {
		return validate(theMgr, theTxn, "1", false, false, false);
	}

	public static String[] validate(IRepositoryManager theMgr,
			PaymentTransaction theTxn, String terminalId) throws Exception {
		return validate(theMgr, theTxn, "1", false, false, false);
	}
	
	// vishal yevale 13 oct 2016 : for giftcard manual entry Request
		public static Object getGCManualEntryResponse(IRepositoryManager theMgr){
				String error_message = "All the Ajb Servers are down at the moment";
				Object giftCardManualEntryRequest = null;
				Object giftCardManualEntryResponse = null;
				
				AJBGiftCardFormatter giftCardFormatter = new AJBGiftCardFormatter();
				giftCardManualEntryRequest = giftCardFormatter.formatAJBGiftCardManualEntryRequest().toString();	
				
				System.out.println("giftCardManualEntryRequest >>>>>>" + giftCardManualEntryRequest);
				
				try {				
				//	uncomment this code when the swipe is functioning as it should
				  giftCardManualEntryResponse = getAuthorization(theMgr, giftCardManualEntryRequest);	
										
				} catch (Exception e) {
					LoggingServices.getCurrent().logMsg(
							CMSCreditAuthHelper.class.toString(), "formatAJBGiftCardSwipeRequest()",
							"Problem with CreditAuthServices, exception: " + e,
							"Verify CreditAuthServices is running",
							LoggingServices.MAJOR);
					log.error(e.toString());
					
				}

				return giftCardManualEntryResponse;
			}
		// end Vishal Yevale 	
	
	
	// mayuri edhara :: for getting the GiftCardBalance
		public static Object getGCSwipeResponse(IRepositoryManager theMgr){
			String error_message = "All the Ajb Servers are down at the moment";
			Object giftCardSwipeRequest = null;
			Object giftCardSwipeResponse = null;
			
			AJBGiftCardFormatter giftCardFormatter = new AJBGiftCardFormatter();
			giftCardSwipeRequest = giftCardFormatter.formatAJBGiftCardSwipeRequest().toString();	
			
			System.out.println("giftCardSwipeRequest >>>>>>" + giftCardSwipeRequest);
			
			try {				
			//	uncomment this code when the swipe is functioning as it should
			   giftCardSwipeResponse = getAuthorization(theMgr, giftCardSwipeRequest);
								
			} catch (Exception e) {
				LoggingServices.getCurrent().logMsg(
						CMSCreditAuthHelper.class.toString(), "formatAJBGiftCardSwipeRequest()",
						"Problem with CreditAuthServices, exception: " + e,
						"Verify CreditAuthServices is running",
						LoggingServices.MAJOR);
				log.error(e.toString());
				
			}

			return giftCardSwipeResponse;
		}
		
		// mayuri edhara :: for getting the GiftCardBalanceInquiry response.
		public static Object getGCBalanceInquiryResponse (IRepositoryManager theMgr, String trackData, boolean IsManualEntry){
			
			Object giftCardBIRequest = null;
			Object giftCardBIResponse = null;
			
			AJBGiftCardFormatter giftCardFormatter = new AJBGiftCardFormatter();
			giftCardBIRequest = giftCardFormatter.formatAJBGiftCardBalanceInquiryRequest(trackData).toString();							
			
			System.out.println("giftCardBIRequest >>>>>>" + giftCardBIRequest);
			try {	
				
				//uncomment this code when the swipe is functioning as it should
				giftCardBIResponse = getAuthorization(theMgr, giftCardBIRequest);		
				//giftCardBIResponse = "101,,,0,45,GIFTCARD,,000407,01,balanceInquiry,,,,,600649*********1183=1612***********,600.53,01000001,,,0,,,,,,,,,,,,066880,,,,,000000,Approved ,,,,,,,trace\20080630.fip 76407 017761,457936,,457936,,06302008,164339,0,01,SVSGCD1,0,,40701001,,,,,,109,164339199,,,,,,,,,,,,,,,,,,Approved ,,";			
			
			} catch (Exception e) {
				LoggingServices.getCurrent().logMsg(
						CMSCreditAuthHelper.class.toString(), "getGCBalanceInquiryResponse()",
						"Problem with CreditAuthServices, exception: " + e,
						"Verify CreditAuthServices is running",
						LoggingServices.MAJOR);
				log.error(e.toString());
				
			}

			return giftCardBIResponse;
		}
	
		
		// mayuri edhara :: get GC Balance.
		public static String getGCBalance (IRepositoryManager theMgr, Object BIResponse){
			String GCBalance = null;
			if(BIResponse !=null) {
				AJBRequestResponseMessage balanceResponse = new AJBRequestResponseMessage(BIResponse.toString());				
				String respStatusCode = balanceResponse.getValue(3);
				System.out.println("respStatusCode >>> " + respStatusCode);
				if(respStatusCode != null && respStatusCode.equals("0")){						
					    GCBalance = ""+(new ArmCurrency(balanceResponse.getValue(51)).divide(100)).doubleValue();
						System.out.println("GCBalance >>>>>>>>>> " + GCBalance);
				}else{
					((IApplicationManager)theMgr).showErrorDlg("There is an error for the transaction; please do it again." + balanceResponse.getValue(37));
					return null;
				}
			}
			
			return GCBalance;
		}
		
		//Start: Added by Himani for GC Transaction History on 03-NOV-2016
		public static String getGCTxnHistory (IRepositoryManager theMgr, Object BIResponse){
			String GCtxnDetails = null;
			if(BIResponse !=null) {
				AJBRequestResponseMessage txnHistResponse = new AJBRequestResponseMessage(BIResponse.toString());				
				String respStatusCode = txnHistResponse.getValue(3);
				System.out.println("respStatusCode >>> " + respStatusCode);
				if(respStatusCode != null && respStatusCode.equals("0")){						
					GCtxnDetails = txnHistResponse.getValue(55);	
						System.out.println("GCtxnDetails >>>>>>>>>> " + GCtxnDetails);
				}else{
					((IApplicationManager)theMgr).showErrorDlg("There is an error for the transaction; please do it again." + txnHistResponse.getValue(37));
					return null;
				}
			}
			
			return GCtxnDetails;
		}
		//End: Added by Himani for GC Transaction History on 03-NOV-2016
		//Start: Added by Himani for GC Transaction History on 03-NOV-2016
				public static String getTransactionHistory(IRepositoryManager theMgr){
					String txnDetails = null;
					if(!ProcessGiftCardSwipeOrManualEntry(theMgr, false))
						return null;
					
					validateGiftCardSwipeResponse(response.toString());
					if(!success)
					{
						((IApplicationManager)theMgr).showErrorDlg(responseErrorMsg);
						responseErrorMsg="";
						return null;
					}

					request = (formatter.formatAJBGiftCardTransactionHistoryRequest(accountNo, trackData).toString());
					do{
						try {
							response = getAuthorization(theMgr, request);
						} catch (Exception e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					if(response.toString().contains(error_message))
					{
						retry = ((IApplicationManager)theMgr).showOptionDlg("AJB Down"
				    	            , error_message, "Retry", "Cancel");
						if(!retry)
						{
							return null;
						}
					}
					else
					{
						retry = false;
					}
					}while(retry);
					txnDetails = getGCTxnHistory(theMgr, response.toString());
					return txnDetails;
		}
				//End: Added by Himani for GC Transaction History on 03-NOV-2016

		
		//Start: Added by Himani for Reload GC on 20-SEP-2016
		public static Object getGCTrackData(IRepositoryManager theMgr)
		{
			Object gcDetails = null;
			String gcAmt = getBalanceInquiry(theMgr);
			if(gcAmt==null)
				return null;
			gcDetails = accountNo +"," + gcAmt;
			return gcDetails;
		} //End: Added by Himani for Reload GC on 20-SEP-2016
		
		// mayuri edhara :: moved the try/catch from intialSaleApplet so as to use this whenever required.
		public static String getBalanceInquiry(IRepositoryManager theMgr){
			String balance = null;
			if(!ProcessGiftCardSwipeOrManualEntry(theMgr, false))
				return null;
			
			validateGiftCardSwipeResponse(response.toString());
			if(!success)
			{
				((IApplicationManager)theMgr).showErrorDlg(responseErrorMsg);
				responseErrorMsg="";
				return null;
			}

			request = (formatter.formatAJBGiftCardBalanceInquiryRequest(accountNo).toString());
			do{
				try {
					response = getAuthorization(theMgr, request);
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			if(response.toString().contains(error_message))
			{
				retry = ((IApplicationManager)theMgr).showOptionDlg("AJB Down"
		    	            , error_message, "Retry", "Cancel");
				if(!retry)
				{
					return null;
				}
			}
			else
			{
				retry = false;
			}
			}while(retry);
			validateGiftCardActivateResponse(response.toString());
			if(!success)
			{
				//((IApplicationManager)theMgr).showErrorDlg(responseErrorMsg+". Please Post Void the transaction.");
				//responseErrorMsg = null; //Commented by Himani to show error msg while doing redeemable buy back
				return null;
			}
			balance = getGCBalance(theMgr, response.toString());
			return balance;
}

	/**
	 * This method iterates through the payment list to: 1) obtain the credit
	 * request for each payment which needs to be authorized; 2) obtain the
	 * credit responce via CreditAuthServices, 3) then set the authorization for
	 * the payment.
	 * 
	 * @param terminalId
	 *            The id of the terminal for this transaction.
	 * @return An array of Strings containing the authorization status code.
	 */
	public static String[] validate(IRepositoryManager theMgr,
			PaymentTransaction theTxn, String terminalId,
			boolean isRefundPaymentRequired, boolean isVoidSale, boolean isManualOverride)
			throws Exception {
		int noPayments = 0;
		Payment[] payments = theTxn.getPaymentsArray();
	
		int length = payments.length;
		boolean payAuth = false;
		String error_message = "All the Ajb Servers are down at the moment";
	//	list = new ArrayList();
		//Vivek Mishra : Added to send 106 ping just before sending 100 request
		//AJBPingThreadBootStrap.ping();
		for (int i = 0; i < length; i++) {
			// Vivek Mishra : Added to test debit transactions
			/*
			 * if(payments[i] instanceof CMSDebitCard)
			 * ((CMSDebitCard)payments[i]).authRequired = false;
			 */
			// End
			payAuth = payments[i].isAuthRequired();
			if(isManualOverride && payments[i] instanceof CreditCard){
				payAuth = true;
			}
			logger.info("DEBUG-PROD-ISSUE:::::isVoidSale :::"+isVoidSale);
			if (payAuth || isVoidSale) {
				noPayments++;
			}//Vivek Mishra : Added for avoiding fatal exceptions in case of cash tender
			else if(payments[i] instanceof CreditCard && ((CreditCard)payments[i]).isReturnWithReceipt())
			{
				noPayments++;
			}//Ends
		}
		// if not payments need authorization, return
		if (noPayments == 0) {
			return null;
		}
		Object request[] = new Object[noPayments];
		Object reply[] = new Object[noPayments];
		String finalReply[] = new String[length];
		int j = 0;
		// First obtain the validation requests
		for (int i = 0; i < length; i++) {
			payAuth = payments[i].isAuthRequired();
			if(isManualOverride && payments[i] instanceof CreditCard){
				payAuth = true;
			}
			if (payAuth|| isVoidSale) {
			// Condition if the transaction is Void(sale/refund)
				if (isVoidSale) {
					logger.info("DEBUG-PROD-ISSUE:::::isVoidSale :Inside validation loop::"+isVoidSale);

					//Vivek Mishra : Changed the condition for not sending request in AJB Sequence in null(Original transaction was completed in offline mode.)
					//if (payments[i] instanceof CreditCard) {
					if (payments[i] instanceof CreditCard && ((CreditCard)payments[i]).getAjbSequence()!=null) {
						// Vivek Mishra : Added condition for Void Refund
						if (theTxn.getTransactionType().equals("RETN")){
							request[j++] = ((CreditCard) payments[i])
									.getCancellationRequest(theTxn.getStore()
											.getId(), terminalId,
											(CreditCard) payments[i], theTxn
													.getId(), true, false);
													//break;
													}
						else{
							request[j++] = ((CreditCard) payments[i])
									.getCancellationRequest(theTxn.getStore()
											.getId(), terminalId,
											(CreditCard) payments[i], theTxn
													.getId(), false, false);
							//break;
							}
					} else
						continue;
				}
				// Vivek Mishra : Added code for fetching the AJB validation
				// request for debit cards.
				else if (payments[i] instanceof CMSDebitCard) {
					request[j++] = ((CMSDebitCard) payments[i])
							.getValidationRequest(theTxn.getStore().getId(),
									terminalId, isRefundPaymentRequired);
				}// End
				else if (payments[i] instanceof CreditCard) {
				request[j++] = ((CreditCard) payments[i])
								.getValidationRequest(theTxn.getStore().getId(),
										terminalId, isRefundPaymentRequired, false);
					
			
				} else
					//Vivek Mishra : Commented the code to restrict sending 100 request to Fipay for Bank Checks requested by Jason on 02-NOV-2015. This code needs to be uncommented if they need request in future.
					/*request[j++] = payments[i].getValidationRequest(theTxn
							.getStore().getId(), terminalId);*/
					continue;
			}
		}

		// Now get the replies for each credit request
		try {
			
			reply = getAuthorization(theMgr, request);
			if (reply !=null && reply.length > 0 && reply[0].toString().contains(error_message))
			{
				finalReply[0] = error_message;
				return finalReply;
			}
		} catch (Exception e) {
			LoggingServices.getCurrent().logMsg(
					CMSCreditAuthHelper.class.toString(), "validate()",
					"Problem with CreditAuthServices, exception: " + e,
					"Verify CreditAuthServices is running",
					LoggingServices.MAJOR);
			log.error(e.toString());
			throw e;
		}
		// Start Issue # 991
		boolean rawDataRequest = false;
		ConfigMgr config = new ConfigMgr("credit_auth.cfg");
		String strRawDataRequest = config.getString("RAW_DATA_REQUEST");
		if (strRawDataRequest.equalsIgnoreCase("TRUE")) {
			rawDataRequest = true;
		}
		System.out.println("RAW DATA REQUEST=" + rawDataRequest);
		// End Issue # 991
		for (int q = 0; q < reply.length; q++) {
			if (rawDataRequest) {
				System.out.println("the reply is: " + reply[q]);
			}
		}
		// Finally set the authorization from the reply
		j = 0;
		// for ( int i = 0; i < noPayments; i++)
		for (int i = 0; i < length; i++) {
			// reply[j] is set for this payment,
			// if and only if isAuthRequired() is true for this payment
			payAuth = payments[i].isAuthRequired();
			if(isManualOverride && payments[i] instanceof CreditCard){
				payAuth = true;
			}
			if (payAuth || isVoidSale) {
				if (rawDataRequest) {
					log.info("setting reply: " + reply[j]);
				}
				if (reply[j]!=null && reply[j].toString().contains(error_message)) {
					finalReply[0] = error_message;
					return finalReply;
				}
				// if(!isVoidSale && !(payments[i] instanceof CMSDebitCard))
			//	System.out.println("what is the replyt>>>>>>111>>>>>>>>>>>>"+finalReply[i]);
			//	System.out.println("what is the replyt>>>>>>>222>>>>>>>>>>>"+reply[j]);
			//	System.out.println("what is the replyt>>>>>>>>333>>>>>>>>>>"+reply[j++]);
				if (!isVoidSale)
					finalReply[i] = new String(
							payments[i].setCreditAuthorization(reply[j++]));
				//Vivek Mishra : Added for setting response code for Post Void
				else
				{
					if(reply[j]!=null && !(reply[j].equals(""))){
						finalReply[i] = ((reply[j++].toString()).split(","))[3];
						//mayuri edhara :: added for SAF request					
						try{					
							String safable = ((reply[j++].toString()).split(","))[88];
							if(safable != null && safable.equals("SAFable")){
								payments[i].setSAFable(true);
							}
							payments[i].setRespObject(reply);
						}catch(ArrayIndexOutOfBoundsException ex){
							payments[i].setSAFable(false);
						}
					
					}
				}
				//Ends
				//Vivek Mishra : Code added for sending Signature Capture request.
				AJBCreditDebitFormatter signFormatter = new AJBCreditDebitFormatter();
				AJBPingThreadBootStrap.getPingThread().ping();
				//Anjana Changed the response code to 0 from 00
				//if(payments[i].getRespStatusCode().equals("0")&& payments[i] instanceof CreditCard)
				//Vivek Mishra : Changed condition for not sending the signature capture request for Void Sale transactions. 
				if(!isVoidSale && payments[i].getRespStatusCode().equals("0")&& payments[i] instanceof CreditCard)
				{
					//Vivek Mishra : Added condition to capture signature only if receive *SIGNATURE in response at field 21 as per the Integration Record buffer guide.
					CreditCard cc = (CreditCard)payments[i];
					if(cc.getLuNmbCrdSwpKy()!=null && cc.getLuNmbCrdSwpKy().contains("*SIGNATURE") && !isRefundPaymentRequired)
					{	
					//Vivek Mishra : Changed getAuthorization call to the other variant which accepts simple Object instead of Object Array to avoid null request scenario 13-SEP-2016	
					//Object[] signRequest = new Object[10];
					Object signRequest = (signFormatter.sendSignatureCapturetoVerifone()).toString();
					Object signReply = getAuthorization(theMgr, signRequest);
					//Vivek Mishra : Added for setting 3-Byte Ascii into cc object.
					((CreditCard)payments[i]).setSignatureValidation(signReply);
					//End
				}
				}
				//End

				if (rawDataRequest) {
					System.out.println("the return from setting auth: "
							+ finalReply[i]);
				}
			}
		}
		request = null;
		reply = null;
		return finalReply;
	}
	
	
	public static String validate(IRepositoryManager theMgr,PaymentTransaction theTxn,Payment payment, String terminalId,
			boolean isRefundPaymentRequired, boolean isVoidSale, boolean isManualOverride, IApplicationManager theAppMgr)
			throws Exception {
		int noPayments = 0;
		storeConfig = new ConfigMgr("store_custom.cfg");
		fipay_flag = storeConfig.getString("FIPAY_Integration");
		gcInt_flag = storeConfig.getString("FIPAY_GIFTCARD_INTEGRATION");
		
		if(fipay_flag == null){
			fipay_flag = "Y";
		}
		if(gcInt_flag == null){
			gcInt_flag = "N";
		}
		boolean payAuth = false;
		String error_message = "All the Ajb Servers are down at the moment";
		
		payAuth = payment.isAuthRequired();
		if (isManualOverride && payment instanceof CreditCard) {
			payAuth = true;
		}

		if (payAuth || isVoidSale) {
			noPayments++;
		}
		else if (payment instanceof CreditCard
				&& ((CreditCard) payment).isReturnWithReceipt()) {
			noPayments++;
		}// Ends
		// if not payments need authorization, return
		if (noPayments == 0) {
			return null;
		}
		
		// Start : Added By Himani for Credit Note Issue Fipay Integration on 22-SEP-2016
		if(payment instanceof CMSDueBillIssue && fipay_flag.equalsIgnoreCase("Y") && gcInt_flag.equalsIgnoreCase("Y"))
		{
			boolean activationResponse = activateGiftCard(null,payment,theMgr);
			//Vishal Yevale 21 dec 2016 (removed credit note issue's issue)
			if(activationResponse){
				((CMSDueBillIssue)payment).setRespStatusCode("0");
				return "0";
			}else{
				return "UnableToActivate";
			}
			//end vishal yevale 21 dec 2016
		}
		// End : Added By Himani for Credit Note Issue Fipay Integration on 22-SEP-2016
		Object request = new Object();
		Object reply = new Object();
		String finalReply = new String();
		int j = 0;
		// First obtain the validation requests
		//for (int i = 0; i < length; i++) 
		payAuth = payment.isAuthRequired();
		if(isManualOverride && payment instanceof CreditCard){
			payAuth = true;
		}
		if (payAuth || isVoidSale) 
		{
			// Condition if the transaction is Void(sale/refund)
			if (isVoidSale) 
			{	
			logger.info("DEBUG-PROD-ISSUE:::::to check Void parameter isVoidSale in Auth Helper:::"+isVoidSale);
				//Vivek Mishra : Changed the condition for not sending request in AJB Sequence in null(Original transaction was completed in offline mode.)
				//if (payments[i] instanceof CreditCard) {
				if (payment instanceof CreditCard && ((CreditCard)payment).getAjbSequence()!=null )
				{
					// Vivek Mishra : Added condition for Void Refund
					if (theTxn.getTransactionType().equals("RETN"))
					{
						request = ((CreditCard) payment).getCancellationRequest(theTxn.getStore().getId(), terminalId,(CreditCard) payment, theTxn.getId(), true, false);
						//break;
					}
					else
					{
						request = ((CreditCard) payment).getCancellationRequest(theTxn.getStore().getId(), terminalId,(CreditCard) payment, theTxn.getId(), false, false);
					}
				} else
					return null;
			}
			// Vivek Mishra : Added code for fetching the AJB validation
			// request for debit cards.
			else if (payment instanceof CMSDebitCard) 
			{
				//Vishal Yevale : 10/11/2017 : added code to avoid double sale or refund request after receiving 101 response of first request			
				if (payment.getRespStatusCode().equals("0")){
					logger.info("DEBUG-PROD-ISSUE:::DebitCard::TransactionType:: double refund or sale "+theTxn.getTransactionType());
				     return null;
				    }
					//end vishal Yevale 10/11/2017
				request = ((CMSDebitCard) payment).getValidationRequest(theTxn.getStore().getId(),terminalId, isRefundPaymentRequired);
			}// End
			else if (payment instanceof CreditCard )
			{
				//Vishal Yevale : 10/11/2017 : added code to avoid double sale or refund request after receiving 101 response of first request
				if (payment.getRespStatusCode().equals("0")){
					logger.info("DEBUG-PROD-ISSUE:::CreditCard::TransactionType:: double refund or sale "+theTxn.getTransactionType());
				     return null;
				    }
					//end vishal Yevale 10/11/2017
				request = ((CreditCard) payment).getValidationRequest(theTxn.getStore().getId(),terminalId, isRefundPaymentRequired, false);
			}else if (payment instanceof CMSStoreValueCard )
			{
				//vishal yevale to set balance of GIFTCARD 10 nov 2016
				if(isRefundPaymentRequired){
					Object giftCardBalanceInquiryResponse=null;
					ArmCurrency giftcardBalance=null;
					giftCardBalanceInquiryResponse=getGCBalanceInquiryResponse(theMgr, ((CMSStoreValueCard) payment).getControlNum(), false);
					if(giftCardBalanceInquiryResponse!=null){
						double balance=Double.valueOf(new AJBRequestResponseMessage(giftCardBalanceInquiryResponse.toString()).getValue(51));
						balance=balance/100;
						giftcardBalance=new ArmCurrency(balance);
					}
					if(giftcardBalance!=null){
			        	  ((CMSStoreValueCard) payment).setGiftcardBalance(giftcardBalance);
					}
				} // end vishal 10 nov 2016
				
				request = ((CMSStoreValueCard) payment).getValidationRequest(theTxn.getStore().getId(),terminalId, isRefundPaymentRequired, false);
				
				}else if (payment instanceof CMSDueBill )
			{
					//vishal yevale to set balance of GIFTCARD 10 nov 2016
					if(isRefundPaymentRequired){
						Object giftCardBalanceInquiryResponse=null;
						ArmCurrency giftcardBalance=null;
						giftCardBalanceInquiryResponse=getGCBalanceInquiryResponse(theMgr, ((CMSDueBill) payment).getId(), false);
						if(giftCardBalanceInquiryResponse!=null){
							double balance=Double.valueOf(new AJBRequestResponseMessage(giftCardBalanceInquiryResponse.toString()).getValue(51));
							balance=balance/100;
							giftcardBalance=new ArmCurrency(balance);
						}
						if(giftcardBalance!=null){
				        	  ((CMSDueBill) payment).setGiftcardBalance(giftcardBalance);
						}
					} // end vishal 10 nov 2016
					
				request = ((CMSDueBill) payment).getValidationRequest(theTxn.getStore().getId(),terminalId, isRefundPaymentRequired, false);
					
			}
			else
					//Vivek Mishra : Commented the code to restrict sending 100 request to Fipay for Bank Checks requested by Jason on 02-NOV-2015. This code needs to be uncommented if they need request in future.
					/*request[j++] = payments[i].getValidationRequest(theTxn
							.getStore().getId(), terminalId);*/
				return null;
		}
		// Now get the replies for each credit request
		try
		{
			reply = getAuthorization(theMgr, request);
			//checking incase of Null
			if(reply == null)
			{
				finalReply = error_message;
			}
			else if (reply !=null && reply.toString().contains(error_message)) 
			{
				finalReply = error_message;
				return finalReply;
			}
		
		} 
		catch (IOException io)
		{
			log.error( "Error while writing a request to a socket" + io.toString());
			throw io;
		}
		catch (Exception e)
		{
			LoggingServices.getCurrent().logMsg(CMSCreditAuthHelper.class.toString(), "validate()","Problem with CreditAuthServices, exception: " + e,
					"Verify CreditAuthServices is running",
					LoggingServices.MAJOR);
			log.error(e.toString());
			throw e;
		}
		// Start Issue # 991
		boolean rawDataRequest = false;
		ConfigMgr config = new ConfigMgr("credit_auth.cfg");
		String strRawDataRequest = config.getString("RAW_DATA_REQUEST");
		if (strRawDataRequest.equalsIgnoreCase("TRUE"))
		{
			rawDataRequest = true;
		}
		System.out.println("RAW DATA REQUEST=" + rawDataRequest);
		if (rawDataRequest) {
			System.out.println("the reply is: " + reply);
		}
		// Finally set the authorization from the reply
		j = 0;
		// for ( int i = 0; i < noPayments; i++)
		//for (int i = 0; i < length; i++)
		// reply[j] is set for this payment,
		// if and only if isAuthRequired() is true for this payment
		payAuth = payment.isAuthRequired();
		if(isManualOverride && payment instanceof CreditCard)
		{
			payAuth = true;
		}
		if (payAuth || isVoidSale) 
		{
			if (rawDataRequest) {
				log.info("setting reply: " + reply);
			}
			if (reply != null && reply.toString().contains(error_message)) {
				finalReply = error_message;
				return finalReply;
			}
			if (!isVoidSale){
				// set the response data in to payment object
				//Vivek Mishra : Added to set Gift Card related values from AJB response 01-NOV-2016
				if(payment instanceof Redeemable){
					finalReply = new String(setGiftCardAuthorization(reply.toString(), (Redeemable)payment));
				}//Ends here 01-NOV-2016
				else{
				finalReply = new String(payment.setCreditAuthorization(reply));
				}
				
				
			}
			// Vivek Mishra : Added for setting response code for Post Void
			else {
				if (reply != null && !(reply.equals(""))) {
					finalReply = ((reply.toString()).split(","))[3];
					try{					
						String safable = ((reply.toString()).split(","))[88];
						if(safable != null && safable.equals("SAFable")){
							payment.setSAFable(true);
						}
						payment.setRespObject(reply);
					}catch(ArrayIndexOutOfBoundsException ex){
						payment.setSAFable(false);
					}
				}
			}
			
			//Vivek Mishra : Code added for sending Signature Capture request.
			AJBCreditDebitFormatter signFormatter = new AJBCreditDebitFormatter();
			AJBPingThreadBootStrap.getPingThread().ping();
			//Anjana Changed the response code to 0 from 00
			//if(payments[i].getRespStatusCode().equals("0")&& payments[i] instanceof CreditCard)
			//Vivek Mishra : Changed condition for not sending the signature capture request for Void Sale transactions. 
			if(!isVoidSale && payment.getRespStatusCode().equals("0")&& payment instanceof CreditCard)
			{
				//Vivek Mishra : Added condition to capture signature only if receive *SIGNATURE in response at field 21 as per the Integration Record buffer guide.
				CreditCard cc = (CreditCard)payment;
				boolean retry = false;
				if(cc.getLuNmbCrdSwpKy()!=null && cc.getLuNmbCrdSwpKy().contains("*SIGNATURE") && !isRefundPaymentRequired)
				{
				//Vivek Mishra : Changed getAuthorization call to the other variant which accepts simple Object instead of Object Array to avoid null request scenario 13-SEP-2016	
				//Object[] signRequest = new Object[10];
				Object signRequest = (signFormatter.sendSignatureCapturetoVerifone()).toString();
				//Vivek Mishra : Added to handle no response scenario for 150 Signature Capture request 14-SEP-2016
				Object signReply = null;
				do{
				signReply = getAuthorization(theMgr, signRequest);
				if(signReply.toString().contains(error_message) || signReply.toString().contains("timed out"))
				{
					retry = theAppMgr.showOptionDlg("Signature Error"
			    	            , "PINPAD Timeout. Please press Retry to capture signature or press Cancel to go ahead without signature.", "Retry", "Cancel");

				}
				else
				{
					retry = false;
				}
				}while(retry);//Ends here 14-SEP-2016
				//Vivek Mishra : Added for setting 3-Byte Ascii into cc object.
				((CreditCard)payment).setSignatureValidation(signReply);
				//End
				
			}
			}
			//End
			
			if (rawDataRequest) {
				System.out.println("the return from setting auth: "	+ finalReply);
			}
		}
		request = null;
		reply = null;
		return finalReply;
	}
	
	//Anjana added for SAF validation 
	public static String[] validateSAF(IRepositoryManager theMgr,
			PaymentTransaction theTxn, String terminalId,
			boolean isRefundPaymentRequired, boolean isVoidSale, boolean isManualOverride, Payment pay)
			throws Exception {
		int noPayments = 0;
		Payment[] payments = theTxn.getPaymentsArray();
		int length = payments.length;
		boolean payAuth = false;
		String error_message = "All the Ajb Servers are down at the moment";

			// End
			payAuth = pay.isAuthRequired();
			if(isManualOverride && pay instanceof CreditCard){
				payAuth = true;
			}
			
			if (payAuth || isVoidSale) {
				noPayments++;
			}
	//	}
		// if not payments need authorization, return
		if (noPayments == 0) {
			return null;
		}
		Object request[] = new Object[noPayments];
		Object reply[] = new Object[noPayments];
		String finalReply[] = new String[length];
		int j = 0;
		// First obtain the validation requests
		//for (int i = 0; i < length; i++) {
			payAuth = pay.isAuthRequired();
			if(isManualOverride && pay instanceof CreditCard){
				payAuth = true;
			}
			if (payAuth|| isVoidSale) {
				// Condition if the transaction is Void(sale/refund)
				if (isVoidSale) {
					if (pay instanceof CreditCard) {
						// Vivek Mishra : Added condition for Void Refund
						if (theTxn.getTransactionType().equals("RETN")){
							request[j++] = ((CreditCard) pay)
									.getSAFCancellationRequest(theTxn.getStore()
											.getId(), terminalId,
											(CreditCard) pay, theTxn
											.getId(), true, isManualOverride);
													//break;
													}
						else{
							request[j++] = ((CreditCard) pay)
									.getSAFCancellationRequest(theTxn.getStore()
											.getId(), terminalId,
											(CreditCard) pay, theTxn
											.getId(), false, isManualOverride);
						//	System.out.println("request[j++]>>>>>>>>>>>>>in SAF"+request[j++]);
							//break;
							}
					} //else
					//	continue;
				}
				// Vivek Mishra : Added code for fetching the AJB validation
				// request for debit cards.
			/*	mayuri edhara : - commented We do not send SAF request for Debit cards.
			 * else if (pay instanceof CMSDebitCard) {
				
					request[j++] = ((CMSDebitCard) pay)
							.getValidationRequest(theTxn.getStore().getId(),
									terminalId, isRefundPaymentRequired);
					
				}// End
				*/
				else if (pay instanceof CreditCard) {
					if( isManualOverride && pay.getRespStatusCode().equals("0")){
						if (theTxn.getTransactionType().equals("RETN")){
							request[j++] = ((CreditCard) pay)
									.getSAFValidationRequest(theTxn.getStore().getId(),
											terminalId, true, isManualOverride);	
						}
						else{
						request[j++] = ((CreditCard) pay)
								.getSAFValidationRequest(theTxn.getStore().getId(),
										terminalId, isRefundPaymentRequired, isManualOverride);
						}
						//continue;
					}else{
						request[j++] = ((CreditCard) pay)
								.getSAFValidationRequest(theTxn.getStore().getId(),
										terminalId, isRefundPaymentRequired, false);
					}
			
				} 
				/* mayuri edhara :: commented - We only send SAF if pay is an instance of CC.
				else
					request[j++] = pay.getValidationRequest(theTxn
							.getStore().getId(), terminalId);*/
			}
	//	}

		// Now get the replies for each credit request
		try {
			reply = getAuthorization(theMgr, request);
			
			//reply[0] = "111,,,0,110,credit,,248,2,Refund,,AMEX,341111*****2000,1912,,50000,2482120995245,English,5647,,TELEAUTH noexpcheck nomod10   *CEM_Swiped *DEVCAP=F0  *E2EE *ReqTOR *StoreSTAN 020082   *CNOSIGNATURE *FloorLimit_30000 *RTSEMVCapable *SIGNATURE *LocalSAF  *GetToken  *OriginalDate 09292016 *OriginalTime 161300,,121,,,,,,,,,,,,,734,16131L  ,APPROVED ,,,,,,,,24802756,,000121,English,09292016,161300,,734,MPSDPN,0,,24802756,,,,SYSTEM PROBLEM LINK DOWN ,,0,161318873,,,,,,,,,,,,,,,,,SYSTEM PROBLEM LINK DOWN ,,,,,,,,to-be,,,341111132592000=19121019113703595671,,01010089250260150b288-796-678160d02887966780008310136f10d787dc9b2f8410bf1a7f427398b6b78510658d9a86067d1da18610658d9a86067d1da1d809000000083d302093690,,";
			
			if (reply !=null && reply.length > 0 && reply[0].toString().contains(error_message)) 
			{
				finalReply[0] = error_message;
				return finalReply;
			}
			
			
		
		} catch (Exception e) {
			LoggingServices.getCurrent().logMsg(
					CMSCreditAuthHelper.class.toString(), "validate()",
					"Problem with CreditAuthServices, exception: " + e,
					"Verify CreditAuthServices is running",
					LoggingServices.MAJOR);
			log.error(e.toString());
			throw e;
		}
		// Start Issue # 991
		boolean rawDataRequest = false;
		ConfigMgr config = new ConfigMgr("credit_auth.cfg");
		String strRawDataRequest = config.getString("RAW_DATA_REQUEST");
		if (strRawDataRequest.equalsIgnoreCase("TRUE")) {
			rawDataRequest = true;
		}
		System.out.println("RAW DATA REQUEST=" + rawDataRequest);
		// End Issue # 991
		for (int q = 0; q < reply.length; q++) {
			if (rawDataRequest) {
				System.out.println("the reply is: " + reply[q]);
			}
		}
		// Finally set the authorization from the reply
		j = 0;
		// for ( int i = 0; i < noPayments; i++)
	//	for (int i = 0; i < length; i++) {
			// reply[j] is set for this payment,
			// if and only if isAuthRequired() is true for this payment
			payAuth = pay.isAuthRequired();
			if(isManualOverride && pay instanceof CreditCard){
				payAuth = true;
			}
			if (payAuth || isVoidSale) {
				if (rawDataRequest) {
					System.out.println("setting reply: " + reply[j]);
				}
				if (reply[j]!=null && reply[j].toString().contains(error_message)) {
					finalReply[0] = error_message;
					return finalReply;
				}
				// if(!isVoidSale && !(pay instanceof CMSDebitCard))
			//	System.out.println("what is the replyt>>>>>>111>>>>>>>>>>>>"+finalReply[i]);
			//	System.out.println("what is the replyt>>>>>>>222>>>>>>>>>>>"+reply[j]);
			//	System.out.println("what is the replyt>>>>>>>>333>>>>>>>>>>"+reply[j++]);
				if (!isVoidSale){
					//Vivek Mishra : Changed in order to stop using 111 response.
					/*finalReply[0] = new String(
							pay.setCreditAuthorization(reply[j++]));*/
					finalReply[0] = "0";
					//Ends here
				}
				//Vivek Mishra : Added for setting response code for Post Void
				else
				{
					if(reply[j]!=null && !(reply[j].equals(""))){
					finalReply[0] = ((reply[j++].toString()).split(","))[3];
					//break;
					}
				}
				//Ends
				//Vivek Mishra : Code added for sending Signature Capture request.
				AJBCreditDebitFormatter signFormatter = new AJBCreditDebitFormatter();
				//Anjana Changed the response code to 0 from 00
				//if(pay.getRespStatusCode().equals("0")&& pay instanceof CreditCard)
				//Vivek Mishra : Changed condition for not sending the signature capture request for Void Sale transactions. 
				if(!isVoidSale && pay.getRespStatusCode().equals("0")&& pay instanceof CreditCard)
				{   
					//Vivek Mishra : Added condition to capture signature only if receive *SIGNATURE in response at field 21 as per the Integration Record buffer guide.
					CreditCard cc = (CreditCard)pay;
					if(cc.getLuNmbCrdSwpKy()!=null && cc.getLuNmbCrdSwpKy().contains("*SIGNATURE") && !isRefundPaymentRequired)
					{	
				    //Vivek Mishra : Changed getAuthorization call to the other variant which accepts simple Object instead of Object Array to avoid null request scenario 13-SEP-2016	
					//Object[] signRequest = new Object[10];
					Object signRequest = (signFormatter.sendSignatureCapturetoVerifone()).toString();
					Object signReply = getAuthorization(theMgr, signRequest);
					//Vivek Mishra : Added for setting 3-Byte Ascii into cc object.
					((CreditCard)pay).setSignatureValidation(signReply);
					//Ends
					}
				}
				//Ends

				if (rawDataRequest) {
					System.out.println("the return from setting auth: "
							+ finalReply[0]);
				}
			}
	//	}
		request = null;
		reply = null;
		return finalReply;
	}
	// }
	
/*mayuri edhara:: for timeout scenarios.
 * */
  	public static String validateTimeoutReversal(IRepositoryManager theMgr, Payment pay)
			throws Exception {

		String error_message = "All the Ajb Servers are down at the moment";

		Object request = new Object();
		Object reply = new Object();
		String finalReply = new String();
		if (pay instanceof CreditCard) {
			
			if(((CreditCard) pay).getRespStatusCode().equals("10") 
					&& (((CreditCard) pay).getRespObject()!= null || !(((CreditCard) pay).getRespObject().equals("")))){				 
		
						request = ((CreditCard) pay).getTimeoutValidationReversalRequest();
	
			}
			
			if(request != null || !(request.toString().equals(""))){
				try {
					
					reply = getAuthorization(theMgr, request);
					
					if (reply !=null && reply.toString().length() > 0 && reply.toString().contains(error_message)) 
					{
						finalReply = error_message;
						return finalReply;
					}
					
					
				
				} catch (Exception e) {
					LoggingServices.getCurrent().logMsg(
							CMSCreditAuthHelper.class.toString(), "validateTimeoutReversal()",
							"Problem with CreditAuthServices, exception: " + e,
							"Verify CreditAuthServices is running",
							LoggingServices.MAJOR);
					log.error(e.toString());
					throw e;
				}
		
		
				if(reply!=null && !(reply.equals(""))){
					finalReply = ((reply.toString()).split(","))[3];	
				}
			}
		}

		request = null;
		reply = null;
		return finalReply;
	}
	
	public static Object  getCardOnFileTokenResponse(IRepositoryManager theMgr , CustomerCreditCard cc)
	{
	//	System.out.println("ib getCardOnFileTokenResponse>>>>>>>>>>>>>"+cc.getCreditCardNumber());
		String[] request=new String[1];
		AJBCardOnFileFormatter formatter=new AJBCardOnFileFormatter();
		//AJBPingThreadBootStrap.ping();
		request[0]=formatter.addCardOnFileRequest();
		 String[] response = null;
		try {
		//	Object[] response = getAuthorization(theMgr,request);
			 response = (String[])getAuthorization(theMgr,request);
			 
		} catch (Exception e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
			log.error(e1.toString());
		}
		try {
			if(response!=null && request.length > 0 &&   !response[0].toString().contains("All the Ajb Servers are down")){
			String[] status=parse(response[0]);
			//Anjana : Code added for sending Signature Capture request.
			if((status)[3].toString().equals("0"))
			{   
				//Vivek Mishra : Added condition to capture signature only if receive *SIGNATURE in response at field 21 as per the Integration Record buffer guide.
			if(status[20]!=null && status[20].contains("*SIGNATURE"))
				{
				//Vivek Mishra : Changed getAuthorization call to the other variant which accepts simple Object instead of Object Array to avoid null request scenario 13-SEP-2016
				//Object[] signRequest = new Object[10];
				Object signRequest = (formatter.sendSignatureCapturetoVerifone()).toString();
			    Object signReply = getAuthorization(theMgr, signRequest);
				//Vivek Mishra : Added for setting 3-Byte Ascii into cc object.
				if(cc!=null)
				cc.setSignatureValidation(signReply);
				//Ends
			
			}
			}
			}
			//Ends
		return	response;
		} catch (Exception e) {
			log.error(e.toString());
			return	null;
		}
	}
	

	public static String[] parse(String string) {
		String[] data=new String[132];
		if (string != null) {
			data = string.split(",");
		} else {
			throw new NullPointerException("data is null");
		}
		return data;
	}
	//Anjana added for voiding the delete tender button
	public static String[] validateVoid(IRepositoryManager theMgr,
			PaymentTransaction theTxn, String terminalId,
			boolean isRefundPaymentRequired, boolean isVoidSale, boolean isManualOverride, Payment pay, int row)
			throws Exception{
	int noPayments = 0;
	Payment[] payments = theTxn.getPaymentsArray();
	int length = payments.length;
		boolean payAuth = false;
	String error_message = "All the Ajb Servers are down at the moment";
	//AJBPingThreadBootStrap.ping();
//	list = new ArrayList();
//	for (int i = 0; i < length; i++) {
		// Vivek Mishra : Added to test debit transactions
		/*
		 * if(payments[i] instanceof CMSDebitCard)
		 * ((CMSDebitCard)payments[i]).authRequired = false;
		 */
		// End
		payAuth = pay.isAuthRequired();
		if(isManualOverride && pay instanceof CreditCard){
			payAuth = true;
		}
		
		if (payAuth || isVoidSale) {
			noPayments++;
		}
//	}
	// if not payments need authorization, return
	if (noPayments == 0) {
		return null;
	}
	Object request[] = new Object[noPayments];
	Object reply[] = new Object[noPayments];
	String finalReply[] = new String[10];

	int j = 0;
	// First obtain the validation requests
	//for (int i = 0; i < length; i++) {
		payAuth = pay.isAuthRequired();
		if(isManualOverride && pay instanceof CreditCard){
			payAuth = true;
		}
		logger.info("DEBUG-PROD-ISSUE ::::In ValidateVoid():::isVoidSale:: "+ isVoidSale);
		if (payAuth|| isVoidSale) {
			// Condition if the transaction is Void(sale/refund)
			if (isVoidSale) {
				if (pay!=null && pay instanceof CreditCard && (((CreditCard)pay).getAjbSequence()!=null)) {
					// Vivek Mishra : Added condition for Void Refund
					if (theTxn.getTransactionType().equals("RETN")){
						logger.info("DEBUG-PROD-ISSUE ::::In ValidateVoid():::theTxn.getTransactionType():: "+ theTxn.getTransactionType());
						request[j++] = ((CreditCard) pay)
								.getCancellationRequest(theTxn.getStore()
										.getId(), terminalId,
										(CreditCard) pay, theTxn
												.getId(), true, false);
												//break;
						logger.info("DEBUG-PROD-ISSUE ::::In ValidateVoid():::isManualOverride:: "+isManualOverride);
						if(isManualOverride){
							request[j++] = ((CreditCard) pay)
									.getCancellationRequest(theTxn.getStore()
											.getId(), terminalId,
											(CreditCard) pay, theTxn
													.getId(), true, true);
						}
								}
					
					else if (isManualOverride){
						logger.info("DEBUG-PROD-ISSUE ::::In ValidateVoid():::not a Return but Manual Override:: "+isManualOverride);
						request[j++] = ((CreditCard) pay)
								.getCancellationRequest(theTxn.getStore()
										.getId(), terminalId,
										(CreditCard) pay, theTxn
												.getId(), false, true);
						//System.out.println("request[j]>>>>>>>>>>>"+request[j]);
						//break;
						}
					
					else{
						logger.info("DEBUG-PROD-ISSUE ::::In ValidateVoid():::method to Void the payment from the last else loop   ");
						request[j++] = ((CreditCard) pay)
								.getCancellationRequest(theTxn.getStore()
										.getId(), terminalId,
										(CreditCard) pay, theTxn
												.getId(), false, false);
						//System.out.println("request[j]>>>>>>>>>>>"+request[j]);
						//break;
						}
				} //else
				//	continue;
			}
			// Vivek Mishra : Added code for fetching the AJB validation
			// request for debit cards.
			

	// Now get the replies for each credit request
	try {
		
		reply = getAuthorization(theMgr, request);
		logger.info("DEBUG-PROD-ISSUE ::::In ValidateVoid()::Void Response back from AJB ::: reply  ::"+reply[0]);
		if (reply !=null && reply.length > 0 && reply[0].toString().contains(error_message)) 
		{
			finalReply[0] = error_message;
			return finalReply;
		}
	} catch (Exception e) {
		LoggingServices.getCurrent().logMsg(
				CMSCreditAuthHelper.class.toString(), "validate()",
				"Problem with CreditAuthServices, exception: " + e,
				"Verify CreditAuthServices is running",
				LoggingServices.MAJOR);
		log.error(e.toString());
		throw e;
	}
	// Start Issue # 991
	boolean rawDataRequest = false;
	ConfigMgr config = new ConfigMgr("credit_auth.cfg");
	String strRawDataRequest = config.getString("RAW_DATA_REQUEST");
	if (strRawDataRequest.equalsIgnoreCase("TRUE")) {
		rawDataRequest = true;
	}
	System.out.println("RAW DATA REQUEST=" + rawDataRequest);
	// End Issue # 991
	for (int q = 0; q < reply.length; q++) {
		if (rawDataRequest) {
			System.out.println("the reply is: " + reply[q]);
		}
	}
	// Finally set the authorization from the reply
	j = 0;

		payAuth = pay.isAuthRequired();
		if(isManualOverride && pay instanceof CreditCard){
			payAuth = true;
		}
		if (payAuth || isVoidSale) {
			if (rawDataRequest) {
				System.out.println("setting reply: " + reply[j]);
			}
			if (reply[j]!=null && reply[j].toString().contains(error_message)) {
				finalReply[0] = error_message;
				return finalReply;
			}
			
			if (!isVoidSale)
				finalReply[0] = new String(
						pay.setCreditAuthorization(reply[j]));
			//Vivek Mishra : Added for setting response code for Post Void
			else
			{
				if(reply[j]!=null && !(reply[j].equals(""))){
					//mayuri edhara
				finalReply[0] = ((reply[j++].toString()).split(","))[3];
				//mayuri edhara :: Add the Response object and SAFable if the resp code is as below.
				/* commented as we dont require SAF for Voids.
				 * add try catch to resolve the index out of bounds exception.
				if(finalReply[0].equals("2") || finalReply[0].equals("3")|| finalReply[0].equals("10")){
					pay.setRespObject(reply[j++]);
					String safable = ((reply[j++].toString()).split(","))[88];
					if(safable != null && safable.equals("SAFable"))
						pay.setSAFable(true);
				}				
				*/
				//break;
				}
			}
		

			if (rawDataRequest) {
				System.out.println("the return from setting auth: "
						+ finalReply[0]);
			}
		}	}
	request = null;
	reply = null;
	return finalReply;
		}
	

//Vivek Mishra : Added for gift card activation flow 16-SEP-2016	
//Modified by Himani for adding new parameter for credit note issue on 22-SEP-2016
public static boolean activateGiftCard(POSLineItem item, Payment payment, IRepositoryManager theMgr)
{
	String amount;
	if(payment!=null)
		amount = payment.getAmount().absoluteValue().stringValue();
	else	
	    amount = item.getItemRetailPrice().absoluteValue().stringValue();
	    
		if(!ProcessGiftCardSwipeOrManualEntry(theMgr, false))
    {
	   return false;
    }
	validateGiftCardSwipeResponse(response.toString());
	if(!success)
	{
		return false;
	}
	else
	{
		request = (formatter.formatAJBGiftCardActivationRequest(accountNo, amount)).toString();
			try {
				response = getAuthorization(theMgr, request);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		if(response.toString().contains(error_message))
		{
			responseErrorMsg=error_message;
			return false;
		}
		validateGiftCardActivateResponse(response.toString());
		
		if(!success)
		{
			//((IApplicationManager)theMgr).showErrorDlg(responseErrorMsg+". Please Post Void the transaction.");
			return false;
		}
		else {
			//Modified by Himani for adding new parameter for credit note issue on 22-SEP-2016
			if(payment==null)
			{
				POSLineItemDetail[] lineItemDetails = item.getLineItemDetailsArray();
				for(int i=0; i<lineItemDetails.length; i++)
				{
					try {
						if(accountNo!=null && !accountNo.equals(""))
						  lineItemDetails[i].setGiftCertificateId(accountNo);
						else
						  lineItemDetails[i].setGiftCertificateId(trackData);
					} catch (BusinessRuleException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
				//Vivek Mishra : Added to save AJB sequence number in case of gift card activation and reload
				item.setAjbSequence(ajbSequenceNo);
				ajbSequenceNo = null;
			}
			else
			{
				if(payment instanceof CMSRedeemable)
				((CMSRedeemable)payment).doSetControlNum(accountNo);
				else if(payment instanceof CMSDueBillIssue)
					((CMSDueBillIssue)payment).doSetControlNum(accountNo);
				((CMSDueBillIssue)payment).setAjbSequence(ajbSequenceNo); //Vishal Yevale : 17 march 2017
				//((CMSRedeemable)payment).setTrackData(trackData);
			}
			return success;
		}
	}
	//End
}

public static void validateGiftCardSwipeResponse(String response){
	String[] reponseFields = response.split(",");
	if(reponseFields[1].contains("ERROR"))
	{
		success = false;
		responseErrorMsg = reponseFields[1];
	}
	else
	{
		
		success = true;
		if(isManualEntry)
		{
			accountNo = reponseFields[2];
			isManualEntry = false;
		}
		else
		{
			if(reponseFields[2]!=null && reponseFields[2].contains("="))
			{
			String[] trackBkd = reponseFields[2].split("=");
			accountNo = trackBkd[0];
			}
			
			else
				accountNo = reponseFields[2];
				
		}
		//return reponseFields[2];
	}
}

public static void validateGiftCardActivateResponse(String response){
	String[] reponseFields = response.split(",");
	if(!(reponseFields[3].equals("0")))
	{
		success = false;
		responseErrorMsg = reponseFields[37];
	}
	else
	{
		
		success = true;
		accountNo = reponseFields[14];
		ajbSequenceNo = reponseFields[79];
		
        //trackData = reponseFields[14];
	}
}

//Vivek Mishra : Added for gift card reload flow 20-SEP-2016
//Modified by Himani for adding new parameter for credit note issue on 22-SEP-2016
public static boolean reloadGiftCard(String accountNo, String trackData, IRepositoryManager theMgr, POSLineItem item, Payment payment)
{

	boolean retry = false;
	String error_message = "All the Ajb Servers are down at the moment";
	AJBGiftCardFormatter formatter = new AJBGiftCardFormatter();
	Object request = (formatter.formatAJBGiftCardSwipeRequest()).toString();
	Object response = null;
	//Modified by Himani for adding new parameter for credit note issue on 22-SEP-2016
	if(payment==null)
		request = formatter.formatAJBGiftCardReloadRequest(accountNo, item.getItemRetailPrice().absoluteValue().stringValue()).toString();
	else
		request = formatter.formatAJBGiftCardReloadRequest(accountNo, payment.getAmount().absoluteValue().toString()).toString();
	//do{
		try {
			response = getAuthorization(theMgr, request);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	if(response.toString().contains(error_message))
	{
		responseErrorMsg=error_message;
		return false;
	}
	
	validateGiftCardActivateResponse(response.toString());
	if(!success)
	{
		//((IApplicationManager)theMgr).showErrorDlg(responseErrorMsg+". Please Post Void the transaction.");
		return false;
	}
	else
	{
		//Modified by Himani for adding new parameter for credit note issue on 22-SEP-2016
		if(payment==null)
		{
			POSLineItemDetail[] lineItemDetails = item.getLineItemDetailsArray();
			for(int i=0; i<lineItemDetails.length; i++)
			{
				try {
					if(accountNo!=null)
					  lineItemDetails[i].setGiftCertificateId(accountNo);
					else
					  lineItemDetails[i].setGiftCertificateId(trackData);
				} catch (BusinessRuleException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			//Vivek Mishra : Added to save AJB sequence number in case of gift card reload and reload
			item.setAjbSequence(ajbSequenceNo);
			ajbSequenceNo = null;
			return success;
		}
		else
		{
			((CMSRedeemable)payment).doSetControlNum(accountNo);
			((CMSRedeemable)payment).setTrackData(trackData);
			return success;
		}
	}
}
//Ends here 20-SEP-2016

//Vivek Mishra : Seperated the code for 107 gift card swipe/menual entry for reusability 27-SEP-2016
public static boolean ProcessGiftCardSwipeOrManualEntry(IRepositoryManager theMgr, boolean isManual)
{
	AJBGiftCardFormatter formatter = new AJBGiftCardFormatter();
	request = (formatter.formatAJBGiftCardSwipeRequest()).toString();
	Object requestManual = (formatter.formatAJBGiftCardManualEntryRequest()).toString();
	response = null;
	int retryCount = 0;
	do{
		if(isManual)
		{
			isManualEntry = isManual;
			retryCount=1;
		}
		try {
			if(retryCount == 1)
			{
				isManual = true;
				response = getAuthorization(theMgr, requestManual);
				retryCount = 0;
			}
			
			else
			    response = getAuthorization(theMgr, request);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	if(response.toString().contains(error_message))
	{
		responseErrorMsg=error_message;
		return false;
	}
	//else if(response.toString().contains("timed out"))
	else if(response.toString().contains("TimeoutOnResponse"))
	{
		responseErrorMsg="PINPAD Timed out!";	
		return false;
		//}
	}
	else if(response.toString().contains("swipe error"))
	{
		((IApplicationManager)theMgr).showErrorDlg("Card Swipe Error.Please enter Gift Card number and expiration date manually on PINPAD.");
		retryCount = 1;
	}
	else
	{
		break;
	}
	}while(true);
	return true;
}
//Ends here 27-SEP-2016

private static String setGiftCardAuthorization(String response, Redeemable payment)
{

	String[] responseString  = null;
	String responseCode = "";
	if (response!=null && response!=""){
		responseString = response.toString().split(",");
	}
	else
	{
		((CMSStoreValueCard)payment).setErrordiscription("No proper response has been received from Fipay");
		return "1";
	}
	responseCode = responseString[3];
	if(payment instanceof CMSStoreValueCard){
		((CMSStoreValueCard)payment).setRespStatusCode(responseCode);
		if(responseCode.equals("0"))
		{
			// mayuri edhara 02-09-2017:: make the authRequired false if the payment is approved.
			((CMSStoreValueCard)payment).setAuthRequired(false);
			if (responseString[18].length() == 0)
				((CMSStoreValueCard)payment).setRespAuthorizationCode(responseString[36]);
			else
				((CMSStoreValueCard)payment).setRespAuthorizationCode(responseString[18]);
			
			((CMSStoreValueCard)payment).setAjbSequence(responseString[79]);
		}
		else {
			// mayuri edhara :: make the authRequired true if the payment is not approved.
			((CMSStoreValueCard)payment).setAuthRequired(true);
			((CMSStoreValueCard)payment).setErrordiscription(responseString[37]);
		}
	}
	else if(payment instanceof CMSDueBill){
		((CMSDueBill)payment).setRespStatusCode(responseCode);
		if(responseCode.equals("0"))
		{
			// mayuri edhara 02-09-2017:: make the authRequired false if the payment is approved.
			((CMSDueBill)payment).setAuthRequired(false);
			if (responseString[18].length() == 0)
				((CMSDueBill)payment).setRespAuthorizationCode(responseString[36]);
			else
				((CMSDueBill)payment).setRespAuthorizationCode(responseString[18]);
			
			((CMSDueBill)payment).setAjbSequence(responseString[79]);
		}
		else{
			// mayuri edhara 02-09-2017:: make the authRequired true if the payment is not approved.
			((CMSDueBill)payment).setAuthRequired(true);
			((CMSDueBill)payment).setErrordiscription(responseString[37]);
		}
	}
	else if(payment instanceof CMSDueBillIssue){
		((CMSDueBillIssue)payment).setRespStatusCode(responseCode);
		if(responseCode=="0")
		{
			// mayuri edhara 02-09-2017:: make the authRequired false if the payment is approved.
			((CMSDueBillIssue)payment).setAuthRequired(false);
			if (responseString[18].length() == 0)
				((CMSDueBillIssue)payment).setRespAuthorizationCode(responseString[36]);
			else
				((CMSDueBillIssue)payment).setRespAuthorizationCode(responseString[18]);
			
			((CMSDueBillIssue)payment).setAjbSequence(responseString[79]);
		}
		else {
			// mayuri edhara 02-09-2017:: make the authRequired true if the payment is not approved.
			((CMSDueBillIssue)payment).setAuthRequired(true);
			((CMSDueBillIssue)payment).setErrordiscription(responseString[37]);
		}
	}
	return responseCode;
	
}

//Vivek Mishra : Added for voiding Gift Card activation and reload 02-NOV-2016
public static boolean validateGiftCardItemVoid(String ajbSequence, String id, boolean isReload, boolean isRefund)
{
	boolean success = false;
	Object request = null;
	Object response = null;
	String[] responseBrk = null;
	
	if(isRefund)
	{
		request = formatter.formatAJBGiftCardVoidRefundRequest(ajbSequence, id).toString();
	}
	else if(isReload)
	{
		request = formatter.formatAJBGiftCardVoidReloadRequest(ajbSequence, id).toString();
	}
	else
	{
		request = formatter.formatAJBGiftCardVoidActivateRequest(ajbSequence, id).toString();
	}
	
	try {
		response = getAuthorization(null, request);
		if(response == null)
		{
			responseErrorMsg = error_message;
			success = false;
			return success;
		}
		else if (response !=null && response.toString().contains(error_message)) 
		{
			responseErrorMsg = error_message;
			success = false;
			return success;
		}
		else
		{
		  	responseBrk = response.toString().split(",");
		}
		if(responseBrk[3].equals("0"))
			success = true;
		else
		{
			success = false;
			responseErrorMsg = responseBrk[37];
		}
		
	} catch (IOException io)
	{
		log.error( "Error while writing a request to a socket" + io.toString());
	}
	catch (Exception e)
	{
		LoggingServices.getCurrent().logMsg(CMSCreditAuthHelper.class.toString(), "validate()","Problem with CreditAuthServices, exception: " + e,
				"Verify CreditAuthServices is running",
				LoggingServices.MAJOR);
		log.error(e.toString());
	}
	
	return success;
}
//Ends here 02-NOV-2016

//Vivek Mishra : Added for voiding Gift Card sale and refund 02-NOV-2016
public static boolean validateGiftCardPaymentVoid(String ajbSequence, String id, String amount, boolean isRefund)
{

	boolean success = false;
	Object request = null;
	Object response = null;
	String[] responseBrk = null;
	
	if(isRefund)
	{
		request = formatter.formatAJBGiftCardVoidRefundRequest(ajbSequence, id).toString();
	}
	else
	{
		request = formatter.formatAJBGiftCardVoidSaleRequest(ajbSequence, id, amount).toString();
	}
	
	try {
		response = getAuthorization(null, request);
		if(response == null)
		{
			responseErrorMsg = error_message;
			success = false;
			return success;
		}
		else if (response !=null && response.toString().contains(error_message)) 
		{
			responseErrorMsg = error_message;
			success = false;
			return success;
		}
		else
		{
		  	responseBrk = response.toString().split(",");
		}
		if(responseBrk[3].equals("0"))
			success = true;
		else
		{
			success = false;
			responseErrorMsg = responseBrk[37];
		}
		
	} catch (IOException io)
	{
		log.error( "Error while writing a request to a socket" + io.toString());
	}
	catch (Exception e)
	{
		LoggingServices.getCurrent().logMsg(CMSCreditAuthHelper.class.toString(), "validate()","Problem with CreditAuthServices, exception: " + e,
				"Verify CreditAuthServices is running",
				LoggingServices.MAJOR);
		log.error(e.toString());
	}
	
	return success;

}
//Ends here 02-NOV-2016

public static String getResponseErrorMsg()
{
	return responseErrorMsg;
}

public static void setResponseErrorMsg(String errorMsg)
{
	responseErrorMsg = errorMsg;
}

public static boolean validateRedeemableCashout(CMSRedeemableBuyBackTransaction theTxn,String amount, IRepositoryManager theMgr)
{
	Redeemable redeem= theTxn.getRedeemable();
	String[] responseBrk = null;
	
	if(redeem instanceof CMSStoreValueCard || redeem instanceof DueBill)
	{
		String Id=redeem.getId();
		request = (formatter.formatAJBRedeemableCashoutRequest(Id,amount)).toString();
		//Mayuri Edhara :: 03-23-17 - commented the below to get proper response message to payment applet and showing the retry dlg there.
		//do{
			try {
				response = getAuthorization(theMgr, request);
				if(response == null)
				{
					responseErrorMsg = error_message;
					success = false;
					return success;
				}
				else if (response !=null && response.toString().contains(error_message)) 
				{
					responseErrorMsg = error_message;
					success = false;
					return success;
				}
				else
				{
				  	responseBrk = response.toString().split(",");
				}
				if(responseBrk[3].equals("0"))
					success = true;
				else
				{
					success = false;
					responseErrorMsg = responseBrk[37];
				}
				
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
//		if(response == null || 
//				(response !=null && 
//					(response.toString().contains(error_message) || !(responseBrk[3].equals("0")))))
//		{
//			if(response == null || 
//				(response !=null && response.toString().contains(error_message))){
//				
//				retry = ((IApplicationManager)theMgr).showOptionDlg("AJB Down"
//	    	            , error_message, "Retry", "Cancel");
//				
//			}else if(!(responseBrk[3].equals("0"))) {
//				
//				retry = ((IApplicationManager)theMgr).showOptionDlg("AJB Error"
//	    	            , "Cashout Could not Process due to an error" + responseBrk[37], "Retry", "Cancel");
//			}
//			
//			if(!retry)
//			{
//				((IApplicationManager)theMgr).showErrorDlg("Please Post Void the transaction.");
//				return false;
//			}
//		}
//		else
//		{ 
//			retry = false;
//		}
//		}while(retry);
		return success;
	}

	else
		return false;
}


}

