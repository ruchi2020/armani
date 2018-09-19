//This class creates AJB request in form of comma separated string for Bank Check authorization 
package com.chelseasystems.cs.ajbauthorization;

import java.util.Arrays;
import java.util.Calendar;

import com.chelseasystems.cr.appmgr.AppManager;
import com.chelseasystems.cr.appmgr.BrowserManager;
import com.chelseasystems.cr.payment.BankCheck;
import com.chelseasystems.cr.pos.PaymentTransaction;
import com.chelseasystems.cr.txnnumber.TransactionNumberStore;
import com.chelseasystems.cs.register.CMSRegister;



public class AJBBankCheckFormatter {
	
	private PaymentTransaction txn;
	private CMSRegister currRegister;
	private String sequence;
	private TransactionNumberStore transactionNumber;
	public static final int TIME_OUT_SECONDS = 120;
	public static final String TXN_NUMBER = "TXN_NUMBER";
	
	//Vivek Mishra : Added to fix the duplicate sequence issue.
	public static int seqPostfix = 10;
	
	public AJBBankCheckFormatter(){
	txn = (PaymentTransaction) AppManager.getCurrent().getStateObject(
	"TXN_POS");
currRegister = (CMSRegister) AppManager.getCurrent().getGlobalObject(
	"REGISTER");
transactionNumber = (TransactionNumberStore) BrowserManager
	.getInstance().getGlobalObject(TXN_NUMBER);
	}

	public String formatAJBCheckRequest(BankCheck bc) {
	int size=46;
	sequence = formatSequence(transactionNumber.getSequenceNumber()
			.intValue()) + Calendar.getInstance().get(Calendar.MILLISECOND);
	String[] request =new String[size];
    Arrays.fill(request, 0, size, "");
	request[0]= AJBMessageCodes.IX_CMD_REQUEST.getValue();
	//request[1]= sequence;
	request[4]=String.valueOf(getTimeOutInSeconds());
    request[5]=AJBMessageCodes.IX_DEBIT_CREDIT_CHECK.getValue();
	request[7]=txn.getStore().getId();
	request[8]=currRegister.getId();
	request[9]=AJBMessageCodes.IX_TRAN_TYPE_CHECK_SALE.getValue();
	//Vivek Mishra : Added check for making sure if the check is scanned in with MICR or manually keyed.  
	if(bc.getIsCheckScannedIn())
		request[14]=bc.getTransitNumber()+bc.getAccountNumber()+bc.getCheckNumber();
	else
	request[12]=bc.getTransitNumber()+bc.getAccountNumber()+bc.getCheckNumber();
	//Ends
	//field 13 contains ABA/transit number,DDA number,Check number
	//The full manual MICR if the POS prompts for entry of all MICR numbers including
//	the check number in one field from left to right (no spaces or symbols)
	request[15]= formatCurrency(bc.getAmount().absoluteValue()
			.stringValue());
	/*request[16]= formatSequence(transactionNumber
			.getSequenceNumber().intValue());*/
	//Vivek Mishra : Setting unique AJB Sequence instead of invoice number in order to make every request unique
	request[16]=sequence;
	//Ends
	//request[45]= sequence;
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
