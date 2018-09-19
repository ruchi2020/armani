package com.chelseasystems.cs.ajbauthorization;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.apache.log4j.Logger;

import com.chelseasystems.cr.appmgr.IRepositoryManager;
import com.chelseasystems.cr.authorization.PaymentValidationRequests;
import com.chelseasystems.cr.config.IConfig;
import com.chelseasystems.cr.currency.ArmCurrency;
import com.chelseasystems.cr.logging.LoggingServices;
import com.chelseasystems.cr.payment.Amex;
import com.chelseasystems.cr.payment.BankCheck;
import com.chelseasystems.cr.payment.CreditCard;
import com.chelseasystems.cr.payment.DebitCard;
import com.chelseasystems.cr.pos.POSLineItem;
import com.chelseasystems.cs.payment.CMSBankCheck;
import com.chelseasystems.cs.payment.CMSDueBill;
import com.chelseasystems.cs.payment.CMSDueBillIssue;
import com.chelseasystems.cs.payment.CMSPaymentCode;
import com.chelseasystems.cs.payment.CMSPaymentMgr;
import com.chelseasystems.cs.payment.CMSStoreValueCard;
/**
 * This is the AJB validation class which gets called by respective payment
 * object to provide AJB request message by calling the correct formatter.
 * 
 * @author Vivek M
 * 
 */
public class AJBValidation extends PaymentValidationRequests implements
		java.io.Serializable, IConfig {

	/**
	 * 
	 */
	private static final long serialVersionUID = -1941420185428088996L;
	private static Logger logger = Logger.getLogger(AJBValidation.class.getName());

	// This method provide the AJB request message for Credit Card sale.
	public Object getCreditCardValidationRequest(CreditCard cc,
			String storeNumber, String terminalNumber) {
		AJBCreditDebitFormatter creditFormatter = new AJBCreditDebitFormatter();
		String request = (creditFormatter.formatAJBCreditRequest(cc))
				.toString();
		return request;

	}
	

		public Object getCreditCardSAFValidationRequest(CreditCard cc,
				String storeNumber, String terminalNumber,
				boolean isRefundPaymentRequired, boolean isManualOverride) {
			AJBCreditDebitFormatter creditFormatter = new AJBCreditDebitFormatter();
			String request = "";
			
			// mayuri Edhara :: in any event for SAF request we format the prevREsp object
			// hence no checks are required.

			request = (creditFormatter.formatAJBSAFReqWRes(cc))
							.toString();
			 
			return request;

		}
		
	/*	mayuri edhara :  for Timeout scenarios
	 * */
		public Object getCreditCardTimeoutValidationReversalRequest(CreditCard cc) {
			
			AJBCreditDebitFormatter creditFormatter = new AJBCreditDebitFormatter();
			String request = "";
			
			// mayuri Edhara :: in any event for Time out request we format the prevRequest object
			// hence no checks are required.

			request = (creditFormatter.formatAJBTimeoutReversalReq(cc))
							.toString();
			 
			return request;

		}
		
	/* mayuri edhara :: for timeout scenarios
	 * 	public Object getDebitCardTimeoutValidationReversalRequest(CreditCard cc) {
			
			AJBCreditDebitFormatter creditFormatter = new AJBCreditDebitFormatter();
			String request = "";
			
			// mayuri Edhara :: in any event for Time out request we format the prevRequest object
			// hence no checks are required.

			request = (creditFormatter.formatAJBTimeoutReversalReq(cc))
							.toString();
			 
			return request;

		}*/
		

	// Vivek Mishra : Added for generating refund request
	public Object getCreditCardValidationRequest(CreditCard cc,
			String storeNumber, String terminalNumber,
			boolean isRefundPaymentRequired, boolean isManualOverride) {
		AJBCreditDebitFormatter creditFormatter = new AJBCreditDebitFormatter();
		//added the isReturnWithReceipt check to differentiate between COF and return with receipt 
       if(cc.getTokenNo()!=null && !cc.isReturnWithReceipt()){
			//Sonali:this will call cardOnFileFormatter if creditcard contains token number
			AJBCardOnFileFormatter cardOnFileFormatter= new AJBCardOnFileFormatter();
    	   String request;
    	   if( (!isRefundPaymentRequired)&&(!isManualOverride)){
			
    		   request= cardOnFileFormatter.formatAJBCardOnFileRequest(cc);
			return request;
			}else if((isRefundPaymentRequired)&&(!isManualOverride)){
			 request =	cardOnFileFormatter.formatAJBCardOnFileRefundRequest(cc);
				return request;
			}else if(isManualOverride && isRefundPaymentRequired){
    	   
    		   request= cardOnFileFormatter.formatAJBCardOnFileSAFRefundRequest(cc);
    		   return request;
    		   }
		}
		//AJBCreditDebitFormatter creditFormatter = new AJBCreditDebitFormatter();
		String request = "";

		// Condition if this call is for a refund transaction.
		 if (isRefundPaymentRequired && !isManualOverride)
			request = (creditFormatter.formatAJBCreditRefundRequest(cc))
					.toString();

		// Condition if this call is for a force transaction.
		else
			request = (creditFormatter.formatAJBCreditRequest(cc)).toString();
		 
		return request;

	}
	
	//mayuri edhara :: added validationRequest for CMSStoreValueCard
	public Object getStoreValueCardValidationRequest(CMSStoreValueCard gc,
			String storeNumber, String terminalNumber,
			boolean isRefundPaymentRequired, boolean isManualOverride) {
		AJBGiftCardFormatter giftCardFormatter = new AJBGiftCardFormatter();

		//AJBCreditDebitFormatter creditFormatter = new AJBCreditDebitFormatter();
		String request = "";

		// Condition if this call is for a refund transaction.
		 if (isRefundPaymentRequired && !isManualOverride)
			request = (giftCardFormatter.formatAJBGiftCardRefundRequest(gc))
					.toString();

		// Condition if this call is for a force transaction.
		else
			request = (giftCardFormatter.formatAJBGiftCardSaleRequest(gc)).toString();
		 
		return request;

	}
	
	//mayuri edhara :: added validationRequest for CMSStoreValueCard
		public Object getDueBillCardValidationRequest(CMSDueBill db,
				String storeNumber, String terminalNumber,
				boolean isRefundPaymentRequired, boolean isManualOverride) {
			AJBGiftCardFormatter giftCardFormatter = new AJBGiftCardFormatter();

			//AJBCreditDebitFormatter creditFormatter = new AJBCreditDebitFormatter();
			String request = "";

			// Condition if this call is for a refund transaction.
			 if (isRefundPaymentRequired && !isManualOverride)
				request = (giftCardFormatter.formatAJBGiftCardRefundRequest(db))
						.toString();

			// Condition if this call is for a force transaction.
			else
				request = (giftCardFormatter.formatAJBGiftCardSaleRequest(db)).toString();
			 
			return request;

		}
	//vishal yevale 19 oct 2016 cmsduebillissecard
		public Object getDueBillIssueCardValidationRequest(CMSDueBillIssue db,
				String storeNumber, String terminalNumber,
				boolean isRefundPaymentRequired, boolean isManualOverride) {
			AJBGiftCardFormatter giftCardFormatter = new AJBGiftCardFormatter();

			//AJBCreditDebitFormatter creditFormatter = new AJBCreditDebitFormatter();
			String request = "";

			// Condition if this call is for a refund transaction.
			 if (isRefundPaymentRequired && !isManualOverride)
				request = (giftCardFormatter.formatAJBGiftCardRefundRequest(db))
						.toString();

			
			return request;

		}
	//end vishal 19 oct 2016
		
	//Vivek Mishra : Added IRepositoryManager in the parameter list in order to avoid null theMgr for send sale tax calculation
	//public String sendMessageDatatoVerifone(POSLineItem line, POSLineItem[] lineItemArray,boolean Refresh, boolean idleMessage,boolean clearMessage, String discountAmt, boolean fromPayment) throws Exception
	public String sendMessageDatatoVerifone(IRepositoryManager theMgr, POSLineItem line, POSLineItem[] lineItemArray,boolean Refresh, boolean idleMessage,boolean clearMessage, String discountAmt, boolean fromPayment) throws Exception
	{
		AJBCreditDebitFormatter creditFormatter = new AJBCreditDebitFormatter();
		String request="";
		request =  (creditFormatter.sendMessageDatatoVerifone(theMgr, line, lineItemArray, Refresh,idleMessage,clearMessage,discountAmt,fromPayment)).toString();	
		return request;
		  
	  }

	public void processConfigEvent(String[] arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public Object getAmexCreditCardValidationRequest(Amex arg0, String arg1,
			String arg2) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Object getBankCheckValidationRequest(BankCheck arg0, String arg1,
			String arg2) {
		
		// TODO Auto-generated method stub
		AJBBankCheckFormatter bankCheckFormatter = new AJBBankCheckFormatter();
		return bankCheckFormatter.formatAJBCheckRequest(arg0);

	}

	@Override
	public Object getCancelRequest(CreditCard cc, String storeNumber,
			String terminalNumber) {

		return null;
	}

	// Vivek Mishra :Method added for Void Sale for Credit/Debit card and Void
	// refund for Credit card.
	public Object getCancelRequest(CreditCard cc, String storeNumber,
			String terminalNumber, String orgTxnNo, boolean isVoidRefund, boolean isManualOverride) {
		AJBCreditDebitFormatter creditFormatter = new AJBCreditDebitFormatter();
		String request = "";
		// Condition if this call is for a Void Refund transaction.
		logger.info("DEBUG-PROD-ISSUE ::::getCancelRequest()  ::isVoidRefund "+isVoidRefund);

		if (isVoidRefund){
			if(isManualOverride)
			{
				logger.info("DEBUG-PROD-ISSUE ::::getCancelRequest()  ::isManualOverride "+isManualOverride);
				request = (creditFormatter.formatAJBCreditReversalRefundSAFRequest(cc,
						storeNumber, orgTxnNo)).toString();
			}
			else
			{
			request = (creditFormatter.formatAJBCreditReversalRefundRequest(cc,
					storeNumber, orgTxnNo)).toString();
			}
		}
		// Else this call is for a Void Sale transaction.
		else {
			if (cc instanceof DebitCard){
				if(isManualOverride)
				{
					request = (creditFormatter.formatAJBDebitReversalSAFRequest(cc,
							storeNumber, orgTxnNo)).toString();
				}else
				{
				request = (creditFormatter.formatAJBDebitReversalRequest(cc,
						storeNumber, orgTxnNo)).toString();
				}
			}
			else{
				if(isManualOverride){
					request = (creditFormatter.formatAJBCreditReversalSAFRequest(cc,
							storeNumber, orgTxnNo)).toString();
				}
				else{
				request = (creditFormatter.formatAJBCreditReversalRequest(cc,
						storeNumber, orgTxnNo)).toString();
				}
				}
		}
		logger.info("DEBUG-PROD-ISSUE ::::getCancelRequest()  ::request:::: "+request);
		return request;
	}
	
	//mayuri edhara :: added for SAF request
	public Object getSAFCancelRequest(CreditCard cc, String storeNumber,
			String terminalNumber, String orgTxnNo, boolean isVoidRefund, boolean isManualOverride) {
		AJBCreditDebitFormatter creditFormatter = new AJBCreditDebitFormatter();
		String request = "";
		
		request = (creditFormatter.formatAJBSAFReqWRes(cc)).toString();

		return request;
	}
	
	//Ends

	@Override
	public Object getDCCRequest(CreditCard arg0, String arg1, String arg2) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Object getSignatureValidationRequest(CreditCard arg0, String arg1,
			String arg2) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void setAuthCancelled(Object arg0, CreditCard arg1) {
		// TODO Auto-generated method stub

	}
	//Sonali
	//sets values to BankCheck object from response string based on response code
	//IxCmd
	//IxMultiLayeredSTAN(echo)
	//IxActionCode(0=authorized/approved/succesful,1=declined,2-saftor,3-bank down,5-modem down,6-formating problem,8-try later,10-timed out),
	//IxTimeOut(echo)
	//IxDebitCredit (echo as check)
	//IxTermid
	//IxStoreNumber(echo)
	//IxTerminalNumber(echo)
	//IxTranType (Sale, Force , Refund etc)
	//IxAccount(echo) --needed for manual checks 
	//IxAmount(echo : Two decimals places are assumed, so the decimal is not required.)
	//IxInvoice(echo)
	//IxSpdhCode(teleCheck only)
	//IxAuthCode 
	//IxReceiptDisplay 
	//IxCrMerchant(TeleCheck)
	//IxPS2000(required only for settlement)
	//IxSeqNumber(teleCheck only)
	//IxBatchNumber
	//IxPostingDate
	//IxTime
	//IxIsoResp
	//IxBankNodeID
	//IxAuthResponseTime
	//IxAdditionalMesg
	//IxResponseTimeMilliSeconds
	//IxResponseDateTimeMilli
	
	//request 100,1041566661433168806103,,,120,check,,104,1,Sale,,,666777,,,600,104156666,,,,,,,,,,,,,,,,,,,,,,,,,,,,,
	//response 101,1041566661433168806103,,0,115,check,377393,104,1,Sale,,,666777,,,600,104156666,English,,,,,236,,,,,,,,,,,,,AJBDISP0000,168806,%s ,345644000886,,,,,,,000198,,000198,English,06012015,195646,,00,FDNCRD1,0,,10401666,,,,%s ,,352,195646865,,,,,,,,,,,,,,,,,%s ,,
	@Override
	public void setCheckAuthorization(Object resp, BankCheck bc) {
		// TODO Auto-generated method stub
		
		if (resp == null||resp.equals("")) {
			// If null response or empty response then setting the response status code as 10 which
			// means request timeout.
			//resp=03 is recieved when pos server goes offline
			bc.setRespStatusCode("10");
			return;
		}
		String[] response = resp.toString().split(",");
		String responseCode = response[3];

		//Anjana : Added for capturing the unique AJBSequesnce number which goes with every request as Invoice number
		//Changed the manual Auth response code from 3 to 2
		if(responseCode.equals("2") || responseCode.equals("3")){
	
		if (!(response[16].equals("")))
		{
			((CMSBankCheck)bc).setAjbSequence(response[16]);
			//Setting the IxInvoice number in journal key column in ARM_STG_TXN_DTL table
			((CMSBankCheck)bc).setJournalKey(response[16]);
			//Vivek Mishra : Added to capture check entry mode.
			((CMSBankCheck)bc).setLuNmbChkSwpKy(response[20]);
		}}
		//Ends
	/*
		if (responseCode.length() == 1)
			responseCode = "0" + responseCode;*/
		bc.setRespStatusCode(responseCode);
		if(responseCode.equals("0")){
			bc.setRespStatusCode(responseCode);
			bc.setAuthRequired(false);
		bc.setRespAuthorizationCode(response[36]);
		
		bc.setAccountNumber(response[12]);
		String authDate = response[49];
	     String authTime = response[50];
	     
	    SimpleDateFormat fmt = new SimpleDateFormat("yyyyMMddHHmmss");
	    Date approvalDate;
	    authDate = authDate + authTime;
	    try {
	      approvalDate = fmt.parse(authDate);
	    } catch (Exception e) {
	      LoggingServices.getCurrent().logMsg(getClass().getName(), "setCreditCardAuthorization"
	          , "Could not parse Approval date from authorizor.  Setting to current date."
	          , "See Exception", LoggingServices.INFO, e);
	      approvalDate = new Date();
	    }
		bc.setApprovalDate(approvalDate);
		//Vivek Mishra : Added for capturing the unique AJBSequesnce number which goes with every request as Invoice number
		if (!(response[16].equals("")))
		{
			((CMSBankCheck)bc).setAjbSequence(response[16]);
			//Setting the IxInvoice number in journal key column in ARM_STG_TXN_DTL table
			((CMSBankCheck)bc).setJournalKey(response[16]);
		}
		//Ends
		//Vivek Mishra : Added for Check entry mode
		if(!(response[20].equals("")))
			((CMSBankCheck)bc).setLuNmbChkSwpKy(response[20]);
		//Ends
		}
	
	else {
		bc.setRespStatusCode(responseCode);
		bc.setAuthRequired(true);
		return;}
		
	
	}

	// VISHAL YEVALE - 7 SEPT 2016 : ADDED TO PRINT AND SAVE EMV TAG
	public static String getEMVCode(String respField,String startTag, String endTag)
	{
		if(respField.toLowerCase().contains(startTag) && respField.toLowerCase().contains(endTag))
        {
            int index1 = respField.indexOf(startTag);
            int index2 = respField.indexOf(endTag);
            String code = respField.substring(index1 + startTag.length(), index2);
            if(code.equals(""))
            {
                code = null;
            }
            return code;
        } else
        {
            return null;
        }
	}
	// END VISHAL YEVALE - 7 SEPT 2016 : ADDED TO PRINT AND SAVE EMV TAG
	@Override
	// This method sets the required properties in the CreditCard object from
	// response.
	public void setCreditCardAuthorization(Object response, CreditCard cc) {
		//Vivek Mishra : Change the condition to check for blank instead of null which was causing fatal exception in AJB sever down case.  
		//if (arg0 == null)
	// Anjana added below for manual auth : set the approval code correctly 
		String[] responseString  = null;
		String responseCode = "";
		//Need to re-check with AJB as to whether we need to check the response code from 111 SAF to show signature
		if (cc.manualOverride){
			setLuNmbCrdSwpKy(response,cc);
			setMaskCardNum(response,cc);
			setCardType(response,cc);
			setTransactionPaymentNam(response,cc);
			setTndType(response,cc);
			if (response!=null){
				responseString = response.toString().split(",");
			
			if(responseString[5].equalsIgnoreCase("debit")){
				//cc.setCreditOrDebit("debit"); ---uncomment later
			}
			if (!(responseString[11].equals(""))){
				cc.setGUIPaymentName(responseString[11]);
			}
			//to avoid constraint violation as CO_CLS_TND table does not have entry for MASTER_CARD
			if(responseString[11].equalsIgnoreCase("MASTERCARD")){
				cc.setGUIPaymentName("MASTER_CARD");
			}
			//Set masked card number for bank link down scenario as well	
				if (!(responseString[12].equals("")))
					cc.setAccountNumber(responseString[12]);
				
				//Anjana : Added for capturing the unique AJBSequesnce number which goes with every request as Invoice number
				if (!(responseString[16].equals("")))
				{
					cc.setAjbSequence(responseString[16]);
					//Setting the IxInvoice number in journal key column in ARM_STG_TXN_DTL table
					cc.setJournalKey(responseString[16]);
				}
				
				// set the reason auth code in the credit card object - this is done to display the reason to cashier
				
				if ( responseString[37] != null && !responseString[37].equals("") && responseString[37].length() > 0  )
				{
					
					cc.setErrordiscription(responseString[37]);
				}
				
				//Ends
				
				}
			//Added for setting up approved amount considering POS is going to receive Partial Approvals as well
			if (!(responseString[15].equals(""))&&(responseString[9].equalsIgnoreCase(AJBMessageCodes.IX_TRAN_TYPE_CARD_SALE.getValue())))
			{	
				ArmCurrency amt = new ArmCurrency(responseString[15]).divide(100);
				cc.setAmount(amt);
			}
			else if(!(responseString[15].equals(""))&&(responseString[9].equalsIgnoreCase(AJBMessageCodes.IX_TRAN_TYPE_REFUND.getValue())))
			{
				ArmCurrency amt = new ArmCurrency(responseString[15]).divide(100);
				cc.setAmount(amt.multiply(-1));
			}
			//Ends
			return;
		}

		//Anjana commenting the below code to fix the retry popup not showing up when ajb is up
		if (response!=null && response.equals("")) {
			// If no response then setting the response status code as 4 which
			// means request timeout.
			//mayuri edhara:: changes it to 8 to try again.
			cc.setRespStatusCode("8");// Changed from 4 to 10 to 8
			return;
		}
		if (response!=null){
		responseString = response.toString().split(",");
		if(responseString!=null)
		responseCode = responseString[3];
		}
		
		if ( responseString[37] != null && !responseString[37].equals("") && responseString[37].length() > 0  )
		{
			
			cc.setErrordiscription(responseString[37]);
		}
		//IxCmd,
		//IxmultilayeredSTAN(ECHO),
		//IxActioncode(0=authorized/approved/succesful,1=declined,2-saftor,3-bank down,5-modem down,6-formating problem,8-try later,10-timed out),
		//timeout(echo),
		//IxDebitCredit echoed back
		//IxTermID
		//IxStoreNumber
		//IxTerminalNumber
		//IxTranType
		//IxCardType
		//IxAccount
		//IxExpDate
		//IxAmount (Two decimals places are assumed, so the decimal is not required.)
		//IxInvoice(echo)
		//IxTranLanguage
		//IxOptions
		//IxPinPadID
		//IxSpdhCode
		//IxAuthCode
		//IxReceiptDisplay
		//IxCrMerchant
		//IxDbMerchant
		//IxSeqNumber
		//IxPostingDate
		//IxLanguage
		//IxDate
		//IxTime
		//IxIsoResp
		//IxBankNodeID
		//IxAuthResponseTime
		//IxDebitComLink
		//IxAdditionalMesg
		//IxResponseTimeMilliSeconds
		//IxResponseDateTimeMilli
		//IxPrnFile
		//IxIsSAFable
		//request 100,1041930521432934191270,,,120,credit,,104,1,Sale,,,4111111111111111,1901,,37400,104193052,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,
		//response 101,1041930541432934937081,,0,115,credit,377393,104,1,Sale,,VISA,411111******1111,1901,,37400,104193054,English,,,*SIGNATURE  *CNOSIGNATURE,,73,,,,,,,,,,,,,AJBDISP0000,938537,%s ,345644000886,345644000886,,,,,,000062,,000062,English,05292015,142857,,00,FDNCRD1,0,,10401054,,,,%s ,,70,142857252,,,,,,,,,,,,,,,,,%s ,,,,,,,,SAFable,,:::::::::::::::::::::::::::::
		/*if (responseCode.length() == 1)
			responseCode = "0" + responseCode;*/
		cc.setRespStatusCode(responseCode);		
		try {
			if(responseCode != null && responseString != null && responseString[88].equals("SAFable")){
				System.out.println("SAFable ::: " + responseString[88]);
				System.out.println("Set the SAFable flag..");
				cc.setSAFable(true);
				cc.setRespObject(response);
			}
		}catch(ArrayIndexOutOfBoundsException ex){
			cc.setSAFable(false);
		}
		
		////Vivek Mishra : Added fo 111 SAF issue fix : 20-MAY-2016
		if (responseCode.equals("0") || ((responseCode.equals("2") || responseCode.equals("3")) && cc.isSAFable())) {
		//Ends here
			if (!(responseString[12].equals("")))
				cc.setAccountNumber(responseString[12]);
		//Vivek Mishra : Added for capturing the expiration date	
			if (!(responseString[13].equals(""))){
				String testExpDate = responseString[13];
			      if (4 == testExpDate.length()) {
			        StringBuffer buf = new StringBuffer(testExpDate.substring(2));
			        buf.append(testExpDate.substring(0, 2));
			        testExpDate = buf.toString();
			        Calendar cal = getCalendar(testExpDate);
			        if (cal == null) {} else {
			            Date dt = cal.getTime();
			            cc.setExpirationDate(dt);
			}}}
		//Ends		
			// if(!(response[11].equals("INTERAC")))
			//Vivek Mishra : In case of credit card tender fetching the GUIPaymentName(Visa, Master card etc) from AJB response.
			//sonali:modified for not updating guiname if empty
			if(responseString[5].equalsIgnoreCase("debit")){
				//cc.setCreditOrDebit("debit");// ----- uncomment later
				//issue # 29756 added for EOD Report support for all payment types. To support this change DCRD entry in payment.cfg has been replaced by DEBIT_CARD.
				cc.setGUIPaymentName("DEBIT_CARD");
				cc.setTransactionPaymentName("DEBIT_CARD");
				//issue # 29756 changes ends
			}
			if(!(responseString[11].equals(""))){
				cc.setGUIPaymentName(responseString[11]);
					}
			//if (!(cc.getCreditOrDebit()!=null && cc.getCreditOrDebit().equalsIgnoreCase("debit"))){
		/*	if(cc instanceof DebitCard){
				if(!(responseString[11].equals(""))){
			cc.setGUIPaymentName(responseString[11]);
				}}*/
			//to avoid constraint violation as CO_CLS_TND table does not have entry for MASTER_CARD
			//Vishal Yevale : set tender type of MAESTRO card as MASTER_CARD 12 OCT 2016 ADDED JUST ONE CONDITION
			if(responseString[11].equalsIgnoreCase("MASTERCARD") ||responseString[11].equalsIgnoreCase("MAESTRO") ){
				cc.setGUIPaymentName("MASTER_CARD");
			}
			
			//Test
			/*if(responseString[11].equals("VISA"))
			{
				cc.setRespStatusCode("1");
			}*/
			//Test Ends
			//Vivek Mishra : Setting the authRequired flag as false after receiving approved response.
			//It is required as in case of force the flag is getting set to true explicitly for force scenario. 
			//Vivek Mishra : Added fo 111 SAF issue fix : 20-MAY-2016
			if(responseCode.equals("0"))
			    cc.setAuthRequired(false);
			else
				cc.setAuthRequired(true);
				//Ends here
			//When card is swiped, the swipe data is sent in the request and is echoed back in response 
			if(responseString[14].length()!=0){
				//cc.sets
			}
			//If there is no value in field 18 then response code comes in field 36
			if (responseString[18].length() == 0)
				cc.setRespAuthorizationCode(responseString[36]);
			//Vivek Mishra : Added to resolve blank authorization code for local approval 18-OCT-2016
			else
				cc.setRespAuthorizationCode(responseString[18]);
			//Ends here 18-OCT-2016
			String sdate = responseString[49];
			SimpleDateFormat formatter = new SimpleDateFormat("MMddyyyy");
			Date date = null;
			//Anjana added to capture the code entry mode
			setLuNmbCrdSwpKy(response,cc);
			setMaskCardNum(response,cc);
			//to save card type , ty_tnd in tr_ltm_tnd, and tender_type in tr_ltm_crdb_crd_tn
			setCardType(response,cc);
			setTransactionPaymentNam(response,cc);
			setTndType(response,cc);
			//ends
			try {

				date = formatter.parse(sdate);

			} catch (Exception e) {
				e.printStackTrace();
			}
			cc.setApprovalDate(date);
			
			//Vivek Mishra : Added for storing token information
			if (!(responseString[33].equals("")))
			{
				cc.setTokenNo(responseString[33]);
			}
			else
			{
				cc.setTokenNo(""+System.nanoTime());
			}
			//Ends
			//Vivek Mishra : Added for capturing the unique AJBSequesnce number which goes with every request as Invoice number
			if (!(responseString[16].equals("")))
			{
				cc.setAjbSequence(responseString[16]);
				//Setting the IxInvoice number in journal key column in ARM_STG_TXN_DTL table
				cc.setJournalKey(responseString[16]);
			}
			//Ends
			//Added for setting up approved amount considering POS is going to receive Partial Approvals as well
			if (!(responseString[15].equals(""))&&(responseString[9].equalsIgnoreCase(AJBMessageCodes.IX_TRAN_TYPE_CARD_SALE.getValue())))
			{	
				ArmCurrency amt = new ArmCurrency(responseString[15]).divide(100);
				cc.setAmount(amt);
			}
			else if(!(responseString[15].equals(""))&&(responseString[9].equalsIgnoreCase(AJBMessageCodes.IX_TRAN_TYPE_REFUND.getValue())))
			{
				ArmCurrency amt = new ArmCurrency(responseString[15]).divide(100);
				cc.setAmount(amt.multiply(-1));
			}
			//Ends
			// VISHAL YEVALE - 7 SEPT 2016 : ADDED TO PRINT AND SAVE EMV TAG
			String respFields[]=(String[])((String) response).split(",");
			String respField=respFields[55];
			if(respField!=null && respField.length()>=2 && respFields[20].toUpperCase().contains("EMV")){
		cc.setAID(getEMVCode(respField,"<4f>","</4f>"));	
		cc.setTVR(getEMVCode(respField,"<95>","</95>"));		
		cc.setIAD(getEMVCode(respField,"<9f10>","</9f10>"));		
		cc.setTSI(getEMVCode(respField,"<9b>","</9b>"));		
		cc.setARC(getEMVCode(respField,"<8a>","</8a>"));	
		cc.setCVM(getEMVCode(respField,"<9f34>","</9f34>"));		
			}
		// END VISHAL YEVALE - 7 SEPT 2016 : ADDED TO PRINT AND SAVE EMV TAG
		} 
		//Start: Added by Himani for Decline Receipt changes 
		else if(responseCode.equals("1") && responseString[20].contains("EMV"))
			{
				setMaskCardNum(response,cc);
				setCardType(response,cc);
				if (!(responseString[11].equals(""))){
					cc.setGUIPaymentName(responseString[11]);
				}
				// VISHAL YEVALE : to print maskpaymentdetail 13 SEPT 2016
				if (!(responseString[12].equals("")))
					cc.setAccountNumber(responseString[12]);
				//END VISHAL YEVALE
				setLuNmbCrdSwpKy(response,cc);
				if(cc.getLuNmbCrdSwpKy()!=null){
				String entryMode = cc.getLuNmbCrdSwpKy();
				if(entryMode.contains("CEM_Manual")){
			    	entryMode = "Keyed";
			    }
			    // Vishal Yevale : 8 Sept 2016 added for entry mode for Fallback Emv
			    else if(entryMode.contains("CEM_Swipe") && entryMode.toUpperCase().contains("FALLBACK")){
			    	entryMode = "Chip/Swipe";
			    }
			    // Vishal Yevale End: 8 Sept 2016 added for entry mode for Fallback Emv

			    else if(entryMode.contains("CEM_Swipe")){
			    	entryMode = "Swiped";
			    }
				//mayuri edhara : 18 Oct 2016 modified entry mode to check uppercase
			    else if(entryMode.toUpperCase().contains("CONTACTLESS")){
			    	entryMode = "Contactless";
			    }
			    else if(entryMode.contains("CEM_Insert")){
			    	entryMode = "Chip";
				}
				cc.setLuNmbCrdSwpKy(entryMode);
				}
				cc.setAID(getEMVCode(responseString[55],"<4f>","</4f>")); 
				cc.setTVR(getEMVCode(responseString[55],"<95>","</95>"));  
				cc.setIAD(getEMVCode(responseString[55],"<9f10>","</9f10>"));  
				cc.setTSI(getEMVCode(responseString[55],"<9b>","</9b>"));  
				cc.setARC(getEMVCode(responseString[55],"<8a>","</8a>")); 
				cc.setCVM(getEMVCode(responseString[55],"<9f34>","</9f34>"));  
			}
		//End: Added by Himani for Decline Receipt changes
		else
			return;
	}

	@Override
	public void setDCC(Object arg0, CreditCard arg1) {
		// TODO Auto-generated method stub

	}

	@Override
	public void setSignatureValidation(Object response, CreditCard cc) {
		//Get index 16 for signature
		//Vivek Mishra : Added a hard coded 3-byte ascii for testing the Signature Capture scenario. 
		String signAscii = "";
		String[] signResponse = response.toString().split(",");
		if(response.toString().contains("Ajb Servers are down"))
		signAscii = null;
		else
		signAscii = signResponse[16];
		//log.info("Signature response : "+response.toString());
		cc.setSignatureAscii(signAscii);
        //End
	}

	// Vivek Mishra : Added for generating debit card validation request for
	// sale and refund
	public Object getDebitCardValidationRequest(CreditCard cc,
			String storeNumber, String terminalNumber,
			boolean isRefundPaymentRequired) {
		AJBCreditDebitFormatter debitFormatter = new AJBCreditDebitFormatter();
		String request = "";

		if (isRefundPaymentRequired)
			request = (debitFormatter.formatAJBCreditRefundRequest(cc))
					.toString();
		// Vivek Mishra : Commented out as Force is not eligible for Debit Cards
		// as discussed in weekly status meeting on 13-MAY-15
		/*
		 * else if (cc.manualOverride) request =
		 * (debitFormatter.formatAJBDebitForceRequest(cc)) .toString();
		 */
		else
			request = (debitFormatter.formatAJBDebitRequest(cc)).toString();
		return request;

	}

	// End
	
	//Anjana Added for card Entry mode
	
	public void setLuNmbCrdSwpKy(Object response, CreditCard cc) {
		if(response!=null && response.toString().length()>1){
		String[] responseString = response.toString().split(",");

		String setLuNmbCrdSwpKy = responseString[20];
		 cc.setLuNmbCrdSwpKy(setLuNmbCrdSwpKy);
	
	}
	}
	
	//Anjana Added for masking card  mode
	
	public void setMaskCardNum(Object response, CreditCard cc) {
		if(response!=null && response.toString().length()>1){
		String[] responseString = response.toString().split(",");
		
		String setMaskCardNum = responseString[12];
		//sonali:added for not updating masked num if empty
		if(!setMaskCardNum.equals(""))
		 cc.setMaskCardNum(setMaskCardNum);
	
	}
	}
	

	//Anjana added to set card type to TR_LTM_TND based on response as previously it was getting set based on Class type in local bin look up	
	public void setCardType(Object response, CreditCard cc) {
	if (cc.getPaymentCode() == null || cc.getPaymentCode() == "" || cc.getPaymentCode().trim().length() == 0) {
	String payCode="";
		if(response!=null && response.toString().length()>1){
			String[] responseString = response.toString().split(",");
		String paymentDesc=responseString[11];
		//vishal Yevale : for tendercode of Maestro should same as MASTERCARD
		if(paymentDesc.toUpperCase().contains("MAESTRO")){
			paymentDesc="MASTERCARD";
		}
		//END 12 OCT 2016 VISHAL
		payCode = getPaymentCode(cc.getTransactionPaymentName(),paymentDesc);
		cc.setPaymentCode(payCode);
		
		//pay.setPaymentCode(payCode);
	}}
	}
	
	
	//Anjana added to set tender type to TR_LTM_TND based on response as previously it was getting set based on Class type in local bin look up	
		public void setTransactionPaymentNam(Object response, CreditCard cc) {
		String txnPaymentName="";
			if(response!=null && response.toString().length()>1){
				String[] responseString = response.toString().split(",");
				txnPaymentName = responseString[11];
				 if (txnPaymentName.contains("MASTERCARD")) {
				      txnPaymentName = "MASTER_CARD";
				      cc.setTransactionPaymentName(txnPaymentName);
				 }
				   if (txnPaymentName.contains("VISA")) {
					      txnPaymentName = "VISA";
					      cc.setTransactionPaymentName(txnPaymentName);
				   }
			/* if (txnPaymentName.contains("MASTERCARD") || txnPaymentName.contains("VISA")) {
			      txnPaymentName = "BCRD";
			      cc.setTransactionPaymentName(txnPaymentName);
			    }*/
			 if (txnPaymentName.contains("DINERS")) {
			      txnPaymentName = "DINERS";
			      cc.setTransactionPaymentName(txnPaymentName);
			    }
			 if (txnPaymentName.contains("DISC")) {
			      txnPaymentName = "DISC";
			      cc.setTransactionPaymentName(txnPaymentName);
			    }
			 if (txnPaymentName.contains("AMEX")) {
			      txnPaymentName = "AMEX";
			     cc.setTransactionPaymentName(txnPaymentName);
			    }
			
			 if (txnPaymentName.contains("JCB")) {
			      txnPaymentName = "JCB";
			      cc.setTransactionPaymentName(txnPaymentName);
			    }
			 //issue # 29756 added for EOD Report support for all payment types
			 if (txnPaymentName.contains("Debit")) {
				  txnPaymentName = "DEBIT_CARD";
			      cc.setTransactionPaymentName(txnPaymentName);
			    }
		    //chages ends
		}
		}
		
		//Anjana added to set tender type to tr_ltm_crdb_crd_tn as we dont use  local bin look up for Fipay
		public void setTndType(Object response, CreditCard cc) {
	
			if(response!=null && response.toString().length()>1){
				cc.setTenderType("03");
	
		}
		}
		
	//Anjana added to set card type to TR_LTM_TND based on response as previously it was getting set based on Class type in local bin look up	
	private String getPaymentCode(String paymentType, String paymentDesc) {
	
		List paymentCodes = CMSPaymentMgr.getPaymentCode(paymentType);
		if(paymentType.equalsIgnoreCase("BCRD") || paymentType.equalsIgnoreCase("AMEX") || paymentType.equalsIgnoreCase("JCB") || paymentType.equalsIgnoreCase("DINERS")){
	   
			paymentCodes = CMSPaymentMgr.getPaymentCode("CREDIT_CARD");
	  	}
		String cardCode = "";

		int numPaymentCodes = paymentCodes.size();
		if (numPaymentCodes == 0)
			return cardCode;
		if (numPaymentCodes == 1) {
			CMSPaymentCode paymentCode = (CMSPaymentCode) paymentCodes.get(0);
			cardCode = paymentCode.getPaymentCode();
			
		} else {
			CMSPaymentCode[] payCodes = new CMSPaymentCode[numPaymentCodes];
			//GenericChooserRow[] availPaymentCodes = new GenericChooserRow[numPaymentCodes];
			String tempDesc = "";
			for (int i = 0; i < numPaymentCodes; i++) {
				CMSPaymentCode paymentCode = (CMSPaymentCode) paymentCodes.get(i);
				tempDesc = (paymentCode.getPaymentDesc()).replaceAll("_", "");
				//DEBIT cards were not saved in DB as cardType DEBIT in caps
				if((paymentDesc.toUpperCase()).contains(tempDesc.toUpperCase()))
				{
					cardCode=paymentCode.getPaymentCode();
					break;
				}
			}
		}
		return cardCode;
	}
	//Vivek Mishra : Added for capturing the expiration date	
	public static Calendar getCalendar(String sDate) {
	    if (sDate == null || sDate.length() != 4)
	      return null;
	    Calendar cal = Calendar.getInstance();
	    String month = sDate.substring(0, 2);
	    int nMonth = new Integer(month).intValue();
	    if (nMonth < 1 || nMonth > 12)
	      return null;
	    String year = sDate.substring(2);
	    int nYear = new Integer(year).intValue();
	    if (nYear < 50)
	      nYear += 2000; // cal can't deal with year 0
	    else
	      nYear += 1900;
	    cal.set(cal.YEAR, nYear);
	    cal.set(cal.MONTH, nMonth - 1);
	    cal.set(Calendar.DATE, 1);
	    return cal;
	  }
	//Ends
}
