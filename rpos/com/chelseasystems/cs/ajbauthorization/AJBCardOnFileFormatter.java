package com.chelseasystems.cs.ajbauthorization;

import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;

import com.chelseasystems.cr.appmgr.AppManager;
import com.chelseasystems.cr.appmgr.BrowserManager;
import com.chelseasystems.cr.payment.CreditCard;
import com.chelseasystems.cr.pos.PaymentTransaction;
import com.chelseasystems.cr.txnnumber.TransactionNumberStore;
import com.chelseasystems.cs.register.CMSRegister;
import com.chelseasystems.cs.swing.builder.CreditCardBldrUtil;
import com.chelseasystems.cs.util.CreditAuthUtil;

public class AJBCardOnFileFormatter {

	
	private PaymentTransaction txn;
	private CMSRegister currRegister;
	private String sequence;
	private TransactionNumberStore transactionNumber;
	public static final int TIME_OUT_SECONDS = 120;
	public static final String TXN_NUMBER = "TXN_NUMBER";
	//Vivek Mishra : Added to fix the duplicate sequence issue.
	public static int seqPostfix = 10;
	
	public AJBCardOnFileFormatter(){
		txn = (PaymentTransaction) AppManager.getCurrent().getStateObject(
		"TXN_POS");
	currRegister = (CMSRegister) AppManager.getCurrent().getGlobalObject(
		"REGISTER");
	transactionNumber = (TransactionNumberStore) BrowserManager
		.getInstance().getGlobalObject(TXN_NUMBER);
		}
	
	public String addCardOnFileRequest() {
		int size=132;
		
		

		sequence = formatSequence(transactionNumber.getSequenceNumber()
				.intValue()) + Calendar.getInstance().get(Calendar.MILLISECOND);
		String[] request =new String[size];
	    Arrays.fill(request, 0, size, "");
		request[0]= AJBMessageCodes.IX_CMD_REQUEST.getValue();
		request[4]=String.valueOf(getTimeOutInSeconds());
	    request[5]=AJBMessageCodes.IX_DEBIT_CREDIT_CREDIT.getValue();
		request[7]=txn.getStore().getId();
		request[8]=currRegister.getId();
		//change it once device is up and token is supported
		//request[9]=AJBMessageCodes.IX_TRAN_TYPE_CARD_GET_TOKEN.getValue();
		request[9]=AJBMessageCodes.IX_TRAN_TYPE_CARD_GET_TOKEN.getValue();

        //Vivek Mishra : Setting unique AJB Sequence instead of invoice number in order to make every request unique
        request[16]=sequence;
        //comment this for now as response was incorrect ...Uncomment once get token works
   		//request[20]=AJBMessageCodes.IX_OPTIONS_TOKENIZATION.getValue();
		//*tokenization for requesting token
		String result="";
		boolean first = true;
		for(String string : request) {
			   if(first) {
			        result+=string;
			        first=false;
			    } else {
			        result+=","+string;
			    }
			}
		
			return result;

	}
	

	public String formatAJBCardOnFileRequest(CreditCard cc) {
		int size=132;
		
		

		sequence = formatSequence(transactionNumber.getSequenceNumber()
				.intValue()) + Calendar.getInstance().get(Calendar.MILLISECOND);
		String[] request =new String[size];
	    Arrays.fill(request, 0, size, "");
		request[0]= AJBMessageCodes.IX_CMD_REQUEST.getValue();
		//request[1]= sequence;
		request[4]=String.valueOf(getTimeOutInSeconds());
	    request[5]=AJBMessageCodes.IX_DEBIT_CREDIT_CREDIT.getValue();
		request[7]=txn.getStore().getId();
		request[8]=currRegister.getId();
		request[9]= AJBMessageCodes.IX_TRAN_TYPE_CARD_SALE.getValue();
		request[12]= cc.getAccountNumber();
		if(cc.getExpirationDate()!=null){
			request[13]= createYYMMDate(cc.getExpirationDate());
		}
			request[15]= formatCurrency(cc.getAmount().absoluteValue()
				.stringValue());
			if(cc.getTokenNo()!=null)
				request[33]= cc.getTokenNo();
        //Vivek Mishra : Setting unique AJB Sequence instead of invoice number in order to make every request unique
        request[16]=sequence;
        //comment it as we are not able to store account number because of get token
		request[33]=cc.getTokenNo();//card token number
		 

		String result="";
		boolean first = true;
		for(String string : request) {
			   if(first) {
			        result+=string;
			        first=false;
			    } else {
			        result+=","+string;
			    }
			}
		
			return result;

	}
	
	public String formatAJBCardOnFileRefundRequest(CreditCard cc) 
	{
int size=132;
		
		sequence = formatSequence(transactionNumber.getSequenceNumber()
				.intValue()) + Calendar.getInstance().get(Calendar.MILLISECOND);
		String[] request =new String[size];
	    Arrays.fill(request, 0, size, "");
		request[0]= AJBMessageCodes.IX_CMD_REQUEST.getValue();
		//request[1]= sequence;
		request[4]=String.valueOf(getTimeOutInSeconds());
	    request[5]=AJBMessageCodes.IX_DEBIT_CREDIT_CREDIT.getValue();
		request[7]=txn.getStore().getId();
		request[8]=currRegister.getId();
		request[9]= AJBMessageCodes.IX_TRAN_TYPE_REFUND.getValue();

		request[15]= formatCurrency(cc.getAmount().absoluteValue()
				.stringValue());
	
         //Vivek Mishra : Setting unique AJB Sequence instead of invoice number in order to make every request unique
         request[16]=sequence;
         //comment as we are not able to send token now
		 request[33]=cc.getTokenNo();//card token number
		 

		String result="";
		boolean first = true;
		for(String string : request) {
			   if(first) {
			        result+=string;
			        first=false;
			    } else {
			        result+=","+string;
			    }
			}
		
			return result;
	}
	public String formatAJBCardOnFileSAFRequest(CreditCard cc) 
	{
	
int size=132;
		
		sequence = formatSequence(transactionNumber.getSequenceNumber()
				.intValue()) + Calendar.getInstance().get(Calendar.MILLISECOND);
		String[] request =new String[size];
	    Arrays.fill(request, 0, size, "");
		request[0]= AJBMessageCodes.IX_CMD_SAF_REQUEST.getValue();
		//request[1]= sequence;
		request[4]=String.valueOf(getTimeOutInSeconds());
	    request[5]=AJBMessageCodes.IX_DEBIT_CREDIT_CREDIT.getValue();
		request[7]=txn.getStore().getId();
		request[8]=currRegister.getId();
		request[9]= AJBMessageCodes.IX_TRAN_TYPE_CARD_SALE.getValue();

		request[15]= formatCurrency(cc.getAmount().absoluteValue()
				.stringValue());
	
         //Vivek Mishra : Setting unique AJB Sequence instead of invoice number in order to make every request unique
         request[16]=sequence;
         //commented as token is not accepted right now 
		request[33]=cc.getTokenNo();//card token number
		 request[18]=cc.getRespAuthorizationCode();
		 request[20]= AJBMessageCodes.IX_OPTIONS_TELEAUTH.getValue();		
			
			cc.setAjbSequence(request[16]);

		String result="";
		boolean first = true;
		for(String string : request) {
			   if(first) {
			        result+=string;
			        first=false;
			    } else {
			        result+=","+string;
			    }
			}
		
			return result;
	}
	public String formatAJBCardOnFileSAFRefundRequest(CreditCard cc) 
	{
int size=132;
		
		sequence = formatSequence(transactionNumber.getSequenceNumber()
				.intValue()) + Calendar.getInstance().get(Calendar.MILLISECOND);
		String[] request =new String[size];
	    Arrays.fill(request, 0, size, "");
		request[0]= AJBMessageCodes.IX_CMD_SAF_REQUEST.getValue();
		//request[1]= sequence;
		request[4]=String.valueOf(getTimeOutInSeconds());
	    request[5]=AJBMessageCodes.IX_DEBIT_CREDIT_CREDIT.getValue();
		request[7]=txn.getStore().getId();
		request[8]=currRegister.getId();
		request[9]= AJBMessageCodes.IX_TRAN_TYPE_REFUND.getValue();

		request[15]= formatCurrency(cc.getAmount().absoluteValue()
				.stringValue());
	
         //Vivek Mishra : Setting unique AJB Sequence instead of invoice number in order to make every request unique
         request[16]=sequence;
         //commented as we dont have support token with simulator
		request[33]=cc.getTokenNo();//card token number
		 
		 request[18]=cc.getRespAuthorizationCode();
		 request[20]= AJBMessageCodes.IX_OPTIONS_TELEAUTH.getValue();		
			
			cc.setAjbSequence(request[16]);

		String result="";
		boolean first = true;
		for(String string : request) {
			   if(first) {
			        result+=string;
			        first=false;
			    } else {
			        result+=","+string;
			    }
			}
		
			return result;
	}
	
	// Formatter for SIGNATURE CAPTURE 150 request
		// Currently following the message format is :
		//IxCmd,IxMultiLayeredSTAN,,,IxTimeOut,,,IxStoreNumber,IxTerminalNumber,IxTranType,,,,,,,,,,,IxOptions,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,
		public AJBRequestResponseMessage sendSignatureCapturetoVerifone(){

			AJBRequestResponseMessage request = new AJBRequestResponseMessage();
			// Generating a unique key which is getting set under field 2
			// IxMultiLayeredSTAN in the request for uniquely identify the response
			// for a particular request as this field is echos back with the
			// response.
			sequence = formatSequence(transactionNumber.getSequenceNumber()
					.intValue()) + Calendar.getInstance().get(Calendar.MILLISECOND);
			request.setValue(0, AJBMessageCodes.IX_CMD_PROMPT_REQUEST.getValue());
			//As per Enrique's email everywhere commenting request[1] and adding the sequence directly to feild17 
			//request.setValue(1, sequence);
			request.setValue(4, getTimeOutInSeconds());
			request.setValue(7, txn.getStore().getId());
			request.setValue(8, currRegister.getId());
			request.setValue(9, AJBMessageCodes.IX_VERIFONE_PROMPT.getValue());

			// Vivek Mishra : Commented as Danny asked to send the complete account
			// number instead of masked one.
			// request.setValue(12, maskCreditCardNo(cc.getAccountNumber()));
			request.setValue(12, "Please Enter Signature");
			// Vivek Mishra : Setting the complete invoice number instead of just
			// the transaction sequence number.
			/*request.setValue(16, formatSequence(transactionNumber
					.getSequenceNumber().intValue()));*/
			//Vivek Mishra : Setting unique AJB Sequence instead of invoice number in order to make every request unique
			request.setValue(16, sequence);
			//Ends
			
			request.setValue(20, AJBMessageCodes.IX_VERIFONE_SIGNATURE.getValue());

			return request;
		}
		

	public int getTimeOutInSeconds() {
		return TIME_OUT_SECONDS;
	}

	// Formats amount by removing the decimal point as per AJB's standard.
	public String formatCurrency(String price) {
		String priceSplit = price.replace(".", "");
		//Vivek Mishra : Added for removing "," from tender amount
		priceSplit = priceSplit.replace(",", "");
		//Ends
		return priceSplit;

	}

	// Generates sequence number by adding store id and register id and removing
	// '*' if any.
	public String formatSequence(int seqNum) {
		//Vivek Mishra : Added store id and register id with the sequence as asked by Jason on 31-JUL-15.
		// String stringSeqNum = String.valueOf(seqNum);
		//String stringSeqNum = txn.getStore().getId() + currRegister.getId()
		String stringSeqNum = currRegister.getStoreId() + currRegister.getId()
		+ String.valueOf(seqNum);
		return stringSeqNum.replace("*", "");
	}
	
	
	private String createYYMMDate(Date calenderDate) {
		SimpleDateFormat fmt = new SimpleDateFormat("yyMM");
		String strDate = fmt.format(calenderDate);
		return strDate;
	}
	
	//Vivek Mishra : Added to fix the duplicate sequence issue.
	public static int getSeqPostFix()
	{
		int temp = 0;
		if(seqPostfix==99){
			seqPostfix=10;}
        temp = seqPostfix;
        seqPostfix++;
        return temp;
	}
	//Ends
}

