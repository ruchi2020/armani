package com.chelseasystems.cs.ajbauthorization;

/**
 * @author Vivek M
 *
 */

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import org.apache.log4j.Logger;

import com.chelseasystems.cr.appmgr.AppManager;
import com.chelseasystems.cr.appmgr.BrowserManager;
import com.chelseasystems.cr.appmgr.IApplicationManager;
import com.chelseasystems.cr.appmgr.IRepositoryManager;
import com.chelseasystems.cr.config.FileMgr;
import com.chelseasystems.cr.currency.ArmCurrency;
import com.chelseasystems.cr.currency.CurrencyException;
import com.chelseasystems.cr.item.Item;
import com.chelseasystems.cr.payment.CreditCard;
import com.chelseasystems.cr.pos.POSLineItem;
import com.chelseasystems.cr.pos.PaymentTransaction;
import com.chelseasystems.cr.store.Store;
import com.chelseasystems.cr.txnnumber.TransactionNumberStore;
import com.chelseasystems.cr.util.ObjectStore;
import com.chelseasystems.cr.util.ObjectStoreException;
import com.chelseasystems.cs.item.CMSItem;
import com.chelseasystems.cs.pos.CMSCompositePOSTransaction;
import com.chelseasystems.cs.register.CMSRegister;
import com.chelseasystems.cs.store.CMSStore;
import com.chelseasystems.cs.tax.TaxUtilities;

/**
 * This is the message formatter class for Credit Card and Debit Card. Its
 * various formatter methods creates a particular AJB request message in the
 * required format and returns back to AJBValidation class for send it further.
 */

public class AJBCreditDebitFormatter implements AJBRequestResponseConstants {

	private PaymentTransaction txn;
	private String sequence;
	private CMSRegister currRegister;
	private TransactionNumberStore transactionNumber;
	private CMSCompositePOSTransaction theTxn;
	//public static final int TIME_OUT_SECONDS = 120; // -moved to config file.
	//public int TIME_OUT_SECONDS = AJBServiceManager.ajbResTimeOutSec;
	private int TIME_OUT_SECONDS;
	public static final String TXN_NUMBER = "TXN_NUMBER";
	public static final int LANGUAGE = 0;
	public static IApplicationManager theAppMgr;
	//Added the index to show multiple item details for 150 request
	//Vivek Mishra : changed the index to 21 as 20 is already assigned to show IxOptions. 
	//int index= 20;
	int index= 21;
	Item itemLookup;
	//Vivek Mishra : Added to fix the duplicate sequence issue.
	public static int seqPostfix = 10;
	// Constructor for AJBCreditDebitFormatter which fetches the current pos
	// transaction, current register and current transaction sequence number
	// from the repository.
	/*Added to log info and errors.*/
	private static Logger log = Logger.getLogger(AJBCreditDebitFormatter.class.getName());
	public AJBCreditDebitFormatter() {
		txn = (PaymentTransaction) AppManager.getCurrent().getStateObject(
				"TXN_POS");
		if((txn instanceof CMSCompositePOSTransaction))
		theTxn = (CMSCompositePOSTransaction) AppManager.getCurrent().getStateObject("TXN_POS");
		currRegister = (CMSRegister) AppManager.getCurrent().getGlobalObject(
				"REGISTER");
		transactionNumber = (TransactionNumberStore) BrowserManager
				.getInstance().getGlobalObject(TXN_NUMBER);
		itemLookup  = (Item) AppManager.getCurrent().getStateObject("ITEM_SELECT");
	}

	// Formatter for Credit Card Sale AJB request. It fetches the constant
	// values from AJBMessageCodes.java.
	// Currently using following message format:
	// IxCmd,IxMultiLayeredSTAN,,,IxTimeOut,IxDebitCredit,,IxStoreNumber,IxTerminalNumber,IxTranType,,,IxAccount,IxExpDate,,IxAmount,IxInvoice,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,
	public AJBRequestResponseMessage formatAJBCreditRequest(CreditCard cc) {
		AJBRequestResponseMessage request = new AJBRequestResponseMessage();
		if(cc.isManuallyKeyed()){
			sequence = formatSequence(transactionNumber.getSequenceNumber()
					.intValue()) + Calendar.getInstance().get(Calendar.MILLISECOND);
				
			request.setValue(0, AJBMessageCodes.IX_CMD_REQUEST.getValue());
			//As per Enrique's email everywhere commenting request[1] and adding the sequence directly to feild17 
			//request.setValue(1, sequence);
			request.setValue(4, getTimeOutInSeconds());
			request.setValue(5, AJBMessageCodes.IX_DEBIT_CREDIT_CREDIT.getValue());
			//request.setValue(5, AJBMessageCodes.IX_DEBIT_CREDIT_DEBIT.getValue());
			request.setValue(7, txn.getStore().getId());
			request.setValue(8, currRegister.getId());
			request.setValue(9, AJBMessageCodes.IX_TRAN_TYPE_CARD_SALE.getValue());
			//request.setValue(9, AJBMessageCodes.IX_TRAN_TYPE_DEBIT_BIN_LOOKUP.getValue());
			request.setValue(15, formatCurrency(cc.getAmount().absoluteValue()
					.stringValue()));

			// Vivek Mishra : Setting the complete invoice number instead of just
			// the transaction sequence number.
			/*request.setValue(16, formatSequence(transactionNumber
					.getSequenceNumber().intValue()));*/
			//Vivek Mishra : Setting unique AJB Sequence instead of invoice number in order to make every request unique
			request.setValue(16, sequence);
			request.setValue(20, AJBMessageCodes.IX_ENTRY_METHOD_MANUAL.getValue());
		}
		else{
		
		
		// Generating a unique key which is getting set under field 2
		// IxMultiLayeredSTAN in the request for uniquely identify the response
		// for a particular request as this field is echos back with the
		// response.
		sequence = formatSequence(transactionNumber.getSequenceNumber()
				.intValue()) + Calendar.getInstance().get(Calendar.MILLISECOND);
			
		request.setValue(0, AJBMessageCodes.IX_CMD_REQUEST.getValue());
		//As per Enrique's email everywhere commenting request[1] and adding the sequence directly to feild17 
		//request.setValue(1, sequence);
		request.setValue(4, getTimeOutInSeconds());
		request.setValue(5, AJBMessageCodes.IX_DEBIT_CREDIT_DEBIT.getValue());
		request.setValue(7, txn.getStore().getId());
		request.setValue(8, currRegister.getId());
		request.setValue(9, AJBMessageCodes.IX_TRAN_TYPE_DEBIT_BIN_LOOKUP.getValue());

		request.setValue(15, formatCurrency(cc.getAmount().absoluteValue()
				.stringValue()));

		// Vivek Mishra : Setting the complete invoice number instead of just
		// the transaction sequence number.
		/*request.setValue(16, formatSequence(transactionNumber
				.getSequenceNumber().intValue()));*/
		//Vivek Mishra : Setting unique AJB Sequence instead of invoice number in order to make every request unique
		request.setValue(16, sequence);
		}
		//Ends
		log.info("DEBUG-PROD-ISSUE:::: sale request created "+request);
		return request;
		
	}

	// Formatter for Credit Card Refund AJB request
	// Currently using following message format:
	// IxCmd,IxMultiLayeredSTAN,,,IxTimeOut,IxDebitCredit,,IxStoreNumber,IxTerminalNumber,IxTranType,,,IxAccount,IxExpDate,,IxAmount,IxInvoice,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,
	public AJBRequestResponseMessage formatAJBCreditRefundRequest(CreditCard cc) {
		AJBRequestResponseMessage request = new AJBRequestResponseMessage();
if(cc.isManuallyKeyed()){
		sequence = formatSequence(transactionNumber.getSequenceNumber()
					.intValue()) + Calendar.getInstance().get(Calendar.MILLISECOND);
				
			request.setValue(0, AJBMessageCodes.IX_CMD_REQUEST.getValue());
			//As per Enrique's email everywhere commenting request[1] and adding the sequence directly to feild17 
			//request.setValue(1, sequence);
			request.setValue(4, getTimeOutInSeconds());
			request.setValue(5, AJBMessageCodes.IX_DEBIT_CREDIT_CREDIT.getValue());
			//request.setValue(5, AJBMessageCodes.IX_DEBIT_CREDIT_CREDIT.getValue());
			request.setValue(7, txn.getStore().getId());
			request.setValue(8, currRegister.getId());
			request.setValue(9, AJBMessageCodes.IX_TRAN_TYPE_REFUND.getValue());
			//request.setValue(9, AJBMessageCodes.IX_TRAN_TYPE_DEBIT_BIN_LOOKUP.getValue());
			request.setValue(15, formatCurrency(cc.getAmount().absoluteValue()
					.stringValue()));

			// Vivek Mishra : Setting the complete invoice number instead of just
			// the transaction sequence number.
			/*request.setValue(16, formatSequence(transactionNumber
					.getSequenceNumber().intValue()));*/
			//Vivek Mishra : Setting unique AJB Sequence instead of invoice number in order to make every request unique
			request.setValue(16, sequence);
			request.setValue(20, AJBMessageCodes.IX_ENTRY_METHOD_MANUAL.getValue());
		}
		else{
		sequence = formatSequence(transactionNumber.getSequenceNumber()
				.intValue()) + Calendar.getInstance().get(Calendar.MILLISECOND);
		//As per Enrique's email everywhere commenting request[1] and adding the sequence directly to feild17 
		//request.setValue(1, sequence);
		request.setValue(0, AJBMessageCodes.IX_CMD_REQUEST.getValue());
		request.setValue(4, getTimeOutInSeconds());
		request.setValue(5, AJBMessageCodes.IX_DEBIT_CREDIT_CREDIT.getValue());
		request.setValue(7, txn.getStore().getId());
		request.setValue(8, currRegister.getId());
		request.setValue(9, AJBMessageCodes.IX_TRAN_TYPE_REFUND.getValue());

		// Vivek Mishra : Commented as Danny asked to send the complete account
		// number instead of masked one.
		// request.setValue(12, maskCreditCardNo(cc.getAccountNumber()));
		if(cc.isReturnWithReceipt()){
		//request.setValue(12, cc.getTokenNo()); 	
		//Vivek Mishra : Added as asked by Enrique on 01-DEC-15
		request.setValue(12, cc.getAccountNumber());
		if(cc.getTokenNo()!=null)
		request.setValue(33, cc.getTokenNo());
		//Vivek Mishra : Added as asked by Justin on 07-DEC-15
		if(cc.getExpirationDate()!=null)
		request.setValue(13, createYYMMDate(cc.getExpirationDate()));
		}
		request.setValue(15, formatCurrency(cc.getAmount().absoluteValue()
				.stringValue()));
		// Vivek Mishra : Setting the complete invoice number instead of just
		// the transaction sequence number.
		/*request.setValue(16, formatSequence(transactionNumber
				.getSequenceNumber().intValue()));*/
		//Vivek Mishra : Setting unique AJB Sequence instead of invoice number in order to make every request unique
			//As per Enrique's email everywhere commenting request[1] and adding the sequence directly to feild17 
		request.setValue(16, sequence);
		//Ends
		}
     log.info("DEBUG-PROD-ISSUE::::refund request created");
		return request;
	}
	
	// Formatter for Credit Card Refund AJB SAF request
		// Currently using following message format:
		// IxCmd,IxMultiLayeredSTAN,,,IxTimeOut,IxDebitCredit,,IxStoreNumber,IxTerminalNumber,IxTranType,,,IxAccount,IxExpDate,,IxAmount,IxInvoice,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,
		public AJBRequestResponseMessage formatAJBCreditRefundSAFRequest(CreditCard cc) {
			AJBRequestResponseMessage request = new AJBRequestResponseMessage();
			sequence = formatSequence(transactionNumber.getSequenceNumber()
					.intValue()) + Calendar.getInstance().get(Calendar.MILLISECOND);
			//As per Enrique's email everywhere commenting request[1] and adding the sequence directly to feild17 
			//request.setValue(1, sequence);
			request.setValue(0, AJBMessageCodes.IX_CMD_SAF_REQUEST.getValue());
			request.setValue(4, getTimeOutInSeconds());
			request.setValue(5, AJBMessageCodes.IX_DEBIT_CREDIT_CREDIT.getValue());
			request.setValue(7, txn.getStore().getId());
			request.setValue(8, currRegister.getId());
			request.setValue(9, AJBMessageCodes.IX_TRAN_TYPE_REFUND.getValue());

			// Vivek Mishra : Commented as Danny asked to send the complete account
			// number instead of masked one.
			// request.setValue(12, maskCreditCardNo(cc.getAccountNumber()));
			request.setValue(12, cc.getAccountNumber());
			if(cc.getExpirationDate()!=null)
			request.setValue(13, createYYMMDate(cc.getExpirationDate()));
			request.setValue(15, formatCurrency(cc.getAmount().absoluteValue()
					.stringValue()));

			// Vivek Mishra : Setting the complete invoice number instead of just
			// the transaction sequence number.
			/*request.setValue(16, formatSequence(transactionNumber
					.getSequenceNumber().intValue()));*/
			//Vivek Mishra : Setting unique AJB Sequence instead of invoice number in order to make every request unique
			//As per Enrique's email everywhere commenting request[1] and adding the sequence directly to feild17 
			request.setValue(16, sequence);
			//Ends
			log.info("DEBUG-PROD-ISSUE:::: refund request created "+request);
			return request;
		}
	

	// Formatter for Credit Card Reversal/VoidSale AJB request
	// Currently using following message format:
	// IxCmd,,,,IxTimeOut,IxDebitCredit,,IxStoreNumber,IxTerminalNumber,IxTranType,,,IxInvoice,,,IxAmount,IxMultiLayeredSTAN,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,
	public AJBRequestResponseMessage formatAJBCreditReversalRequest(
			CreditCard cc, String storeNumber, String orgTxnNo) {
		AJBRequestResponseMessage request = new AJBRequestResponseMessage();
		//Vivek Mishra : Added to fix the duplicate sequence issue.
		sequence = formatSequence(transactionNumber.getSequenceNumber()
				.intValue()) + Calendar.getInstance().get(Calendar.MILLISECOND);
		//Ends
		request.setValue(0, AJBMessageCodes.IX_CMD_REQUEST.getValue());
		// request.setValue(1, sequence);
		request.setValue(4, getTimeOutInSeconds());
		request.setValue(5, AJBMessageCodes.IX_DEBIT_CREDIT_VOID.getValue());
		request.setValue(7, storeNumber);
		request.setValue(8, currRegister.getId());
		request.setValue(9, AJBMessageCodes.IX_TRAN_TYPE_CARD_SALE.getValue());

		// Vivek Mishra : Setting here the original sale transaction's invoice
		// number instead on account number as asked by Danny.
		// request.setValue(12, cc.getAccountNumber());
		//request.setValue(12, orgTxnNo.replace("*", ""));
		//Vivek Mishra : Setting unique AJB Sequence instead of invoice number in order to make every request unique
		request.setValue(12, cc.getAjbSequence());
		//Ends
		request.setValue(15, formatCurrency(cc.getAmount().absoluteValue()
				.stringValue()));

		// Vivek Mishra : Setting the unique sequence number here as asked by
		// Danny.
		request.setValue(16, sequence);
		log.info("DEBUG-PROD-ISSUE:::: CreditReversal created "+request);
		return request;
	}

	// Formatter for Credit Card Reversal/VoidRefund AJB request
	// Currently using following message format:
	// IxCmd,,,,IxTimeOut,IxDebitCredit,,IxStoreNumber,IxTerminalNumber,IxTranType,,,IxInvoice,,,IxAmount,IxMultiLayeredSTAN,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,
	public AJBRequestResponseMessage formatAJBCreditReversalRefundRequest(
			CreditCard cc, String storeNumber, String orgTxnNo) {
		AJBRequestResponseMessage request = new AJBRequestResponseMessage();
		sequence = formatSequence(transactionNumber.getSequenceNumber()
				.intValue()) + Calendar.getInstance().get(Calendar.MILLISECOND);;
		request.setValue(0, AJBMessageCodes.IX_CMD_REQUEST.getValue());
		request.setValue(4, getTimeOutInSeconds());
		request.setValue(5, AJBMessageCodes.IX_DEBIT_CREDIT_VOID.getValue());
		request.setValue(7, storeNumber);
		request.setValue(8, currRegister.getId());
		request.setValue(9, AJBMessageCodes.IX_TRAN_TYPE_REFUND.getValue());
		//request.setValue(12, orgTxnNo.replace("*", ""));
		//Vivek Mishra : Setting unique AJB Sequence instead of invoice number in order to make every request unique
		request.setValue(12, cc.getAjbSequence());
		//Ends
		request.setValue(15, formatCurrency(cc.getAmount().absoluteValue()
				.stringValue()));
		request.setValue(16, sequence);
		log.info("DEBUG-PROD-ISSUE:::: CreditReversal created "+request);
		return request;
	}

	// Formatter for Credit Card Force request
	// Currently using following message format:
	// IxCmd,IxMultiLayeredSTAN,,,IxTimeOut,IxDebitCredit,,IxStoreNumber,IxTerminalNumber,IxTranType,,,IxAccount,IxExpDate,,IxAmount,IxInvoice,,IxForceAuthCode,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,
	public AJBRequestResponseMessage formatAJBCreditForceRequest(CreditCard cc) {
		AJBRequestResponseMessage request = new AJBRequestResponseMessage();
		sequence = formatSequence(transactionNumber.getSequenceNumber()
				.intValue()) + Calendar.getInstance().get(Calendar.MILLISECOND);
		request.setValue(0, AJBMessageCodes.IX_CMD_REQUEST.getValue());
		//As per Enrique's email everywhere commenting request[1] and adding the sequence directly to feild17 
		//request.setValue(1, sequence);
		request.setValue(4, getTimeOutInSeconds());
		request.setValue(5, AJBMessageCodes.IX_DEBIT_CREDIT_CREDIT.getValue());
		request.setValue(7, txn.getStore().getId());
		request.setValue(8, currRegister.getId());
		request.setValue(9, AJBMessageCodes.IX_DEBIT_CREDIT_FORCE.getValue());
		request.setValue(12, cc.getAccountNumber());
		request.setValue(13, createYYMMDate(cc.getExpirationDate()));
		request.setValue(15, formatCurrency(cc.getAmount().absoluteValue()
				.stringValue()));
		// Vivek Mishra : Setting the complete invoice number instead of just
		// the transaction sequence number.
		/*request.setValue(16, formatSequence(transactionNumber
				.getSequenceNumber().intValue()));*/
		//Vivek Mishra : Setting unique AJB Sequence instead of invoice number in order to make every request unique
		request.setValue(16, sequence);
		//Ends
		request.setValue(18, cc.getRespAuthorizationCode());

		return request;
	}

	// Formatter for Debit Card Sale AJB request
	// Currently using following message format:
	// IxCmd,IxMultiLayeredSTAN,,,IxTimeOut,IxDebitCredit,,IxStoreNumber,IxTerminalNumber,IxTranType,,,,,,IxAmount,IxInvoice,,,,,,,,,,,,,,,,,,Reserved,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,
	public AJBRequestResponseMessage formatAJBDebitRequest(CreditCard cc) {
		AJBRequestResponseMessage request = new AJBRequestResponseMessage();
		sequence = formatSequence(transactionNumber.getSequenceNumber()
				.intValue()) + Calendar.getInstance().get(Calendar.MILLISECOND);
		request.setValue(0, AJBMessageCodes.IX_CMD_REQUEST.getValue());
		//As per Enrique's email everywhere commenting request[1] and adding the sequence directly to feild17 
		//request.setValue(1, sequence);
		request.setValue(4, getTimeOutInSeconds());
		request.setValue(5, AJBMessageCodes.IX_DEBIT_CREDIT_DEBIT.getValue());
		request.setValue(7, txn.getStore().getId());
		request.setValue(8, currRegister.getId());
		/*
		 * request.setValue(9,
		 * AJBMessageCodes.IX_TRAN_TYPE_DEBIT_INIT.getValue());
		 */
		// Vivek Mishra : Commented the Binlookup as currently Fipay Simulator
		// is not compatible of handling this transaction.
		// So currenlt using sale instead. Need to change accordingly when
		// Binlookup gets implemented on AJB side.
		/*
		 * request.setValue(9,
		 * AJBMessageCodes.IX_TRAN_TYPE_DEBIT_BIN_LOOKUP.getValue());
		 */
		request.setValue(9, AJBMessageCodes.IX_TRAN_TYPE_CARD_SALE.getValue());
		request.setValue(12, cc.getAccountNumber());
		// request.setValue(13, createYYMMDate(cc.getExpirationDate()));
		request.setValue(15, formatCurrency(cc.getAmount().absoluteValue()
				.stringValue()));
		// Vivek Mishra : Setting the complete invoice number instead of just
		// the transaction sequence number.
		/*request.setValue(16, formatSequence(transactionNumber
				.getSequenceNumber().intValue()));*/
		//Vivek Mishra : Setting unique AJB Sequence instead of invoice number in order to make every request unique
		request.setValue(16, sequence);
		//Ends
		/* request.setValue(23, txn.getTheOperator().getId()); */
		/* request.setValue(34,txn.getStore().getAddress()); */

		request.setValue(45, formatSequence(transactionNumber
				.getSequenceNumber().intValue()));

		log.info("DEBUG-PROD-ISSUE:::: debit request created "+request);
		return request;
	}

	
	// Formatter for Debit Void sale AJB request. As asked by Armani/AJB in
	// weekly status meeting on 13-MAY-15 currently creating credit refund
	// message for debit void sale.
	// Currently using following message format:
	// IxCmd,IxMultiLayeredSTAN,,,IxTimeOut,IxDebitCredit,,IxStoreNumber,IxTerminalNumber,IxTranType,,,IxAccount,IxExpDate,,IxAmount,IxInvoice,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,
	public AJBRequestResponseMessage formatAJBDebitReversalRequest(
			CreditCard cc, String storeNumber, String orgTxnNo) {

		AJBRequestResponseMessage request = new AJBRequestResponseMessage();
		sequence = formatSequence(transactionNumber.getSequenceNumber()
				.intValue()) + Calendar.getInstance().get(Calendar.MILLISECOND);
			//As per Enrique's email everywhere commenting request[1] and adding the sequence directly to feild17 
		//request.setValue(1, sequence);
		request.setValue(0, AJBMessageCodes.IX_CMD_REQUEST.getValue());
		request.setValue(4, getTimeOutInSeconds());
		request.setValue(5, AJBMessageCodes.IX_DEBIT_CREDIT_CREDIT.getValue());
		request.setValue(7, storeNumber);
		request.setValue(8, currRegister.getId());
		request.setValue(9, AJBMessageCodes.IX_TRAN_TYPE_REFUND.getValue());

		// Vivek Mishra : Commented as Danny asked to send the complete account
		// number instead of masked one.
		// request.setValue(12, maskCreditCardNo(cc.getAccountNumber()));
		//request.setValue(12, cc.getAccountNumber());

		//request.setValue(13, createYYMMDate(cc.getExpirationDate()));
		request.setValue(15, formatCurrency(cc.getAmount().absoluteValue()
				.stringValue()));

		// Vivek Mishra : Setting the complete invoice number instead of just
		// the transaction sequence number.
		// Vivek Mishra : Setting the complete invoice number instead of just
		// the transaction sequence number.
		/*request.setValue(16, formatSequence(transactionNumber
				.getSequenceNumber().intValue()));*/
		//Vivek Mishra : Setting unique AJB Sequence instead of invoice number in order to make every request unique
		request.setValue(16, sequence);
		//Ends
		log.info("DEBUG-PROD-ISSUE:::: debit reversal  created "+request);
		return request;

	}
	
	// Formatter for 150 request
	// Currently following the message format is :
	//IxCmd,IxMultiLayeredSTAN,,,IxTimeOut,,,IxStoreNumber,IxTerminalNumber,IxTranType,IxTranLanguage,,,,,,,,,,IxOptions,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,
	//Vivek Mishra : Added IRepositoryManager in the parameter list in order to avoid null theMgr for send sale tax calculation
	//public AJBRequestResponseMessage sendMessageDatatoVerifone(POSLineItem line, POSLineItem[] lineItemArray, boolean Refresh , boolean idleMessage,boolean clearMessage,String discountAmt,boolean fromPayment){
	public AJBRequestResponseMessage sendMessageDatatoVerifone(IRepositoryManager theMgr, POSLineItem line, POSLineItem[] lineItemArray, boolean Refresh , boolean idleMessage,boolean clearMessage,String discountAmt,boolean fromPayment){
		AJBRequestResponseMessage request = new AJBRequestResponseMessage();
		//Anjana Asok added  null check as we are passing empty lines for REFRESH request after posting transaction
		//1 cmd 150
		//2 IxMultiLayeredSTAN keeping it blank
		//3-4 reserved 
		//5 Timeout in seconds this defines number of seconds device will display the message
		//6-7 reserved
		//8 Store
		//9 Terminal Id
		//10 transaction type Prompt
		//11 Lannguage 0- english 1-frech
		//12 promptid keepinng it empty
		//13 Message display on pad Qty DeptCode Style Description Extended Price
		//14 enable  keeys on fipay keeping it blank
		//15 length of secure data input minimum lenth from the pad keeping it blank
		//16 length of secure data input max lenth from the pad keeping it blank
		//17,18,19 blank
		//20 Scrolling receipt form
		//21 IxOptions -*POSITEMS , *POSITEMS_CLEAR, *POSITEM-CHANGE, *POSITEMS-REFRESH, *POSITEMS_RESET
		//22 
		Store store = null;
		CMSItem itm = null;
		CMSItem itmforDelete = null;
		if(line!=null){
		itmforDelete = (CMSItem) line.getItem();
		itm = (CMSItem) line.getItem();
		}
		POSLineItem lineItem=null;
		//Vivek Mishra : Commented the extra for loop 
		/*for(int i=0; i<lineItemArray.length ; i++){
				lineItem=lineItemArray[i];
				itm = (CMSItem) lineItem.getItem();
			
		}*/
	
		
		//double rate = txn.getStore().getDefaultTaxRate().doubleValue();
		String discount = null;
		ArmCurrency tax = null;
		ArmCurrency preAmtDue = null;
		if(theTxn!=null){
		discount = theTxn.getDiscount()!=null?theTxn.getDiscount().toString():"$0.00";
		 try {
			 //Comment this to check if the latest taxes are calculated.
			// TaxUtilities.applyTax(theMgr, theTxn, (CMSStore) theTxn.getStore(), (CMSStore) theTxn.getStore(), theTxn.getProcessDate());
			 //Vivek Mishra : Added to show proper tax and due amount in case of presale transactions. 
			 if((theTxn.getTransactionType()).equals("PRSO"))
				{
				    preAmtDue = theTxn.getPresaleTransaction().getTotalAmountDue();
					tax =  preAmtDue.subtract(theTxn.getCompositeNetAmount());
				}
			 else
			 tax = theTxn.getCompositeTotalAmountDue().subtract(theTxn.getCompositeNetAmount());
			 
	} catch (CurrencyException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	 }
		sequence = formatSequence(transactionNumber.getSequenceNumber()
				.intValue()) + Calendar.getInstance().get(Calendar.MILLISECOND);
		request.setValue(0, AJBMessageCodes.IX_CMD_PROMPT_REQUEST.getValue());
		request.setValue(4, getItemTimeOutInSeconds());
	    if(txn!=null){
		request.setValue(7, txn.getStore().getId());
		request.setValue(8, currRegister.getId());
		}else{
			  File storeFile = new File(FileMgr.getLocalFile("repository", "STORE"));
		      try {
				store = (Store)new ObjectStore(storeFile).read();
				request.setValue(7, store.getId());
				request.setValue(8, currRegister.getId());
			} catch (ObjectStoreException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		/*
		 * sequence =
		 * txn.getStore().getId()+txn.getRegisterId()+txn.getId()+txn.
		 * getCreateDate().getTime(); request.setValue(1, sequence);
		 */
		
		if(!clearMessage){
		request.setValue(9, AJBMessageCodes.IX_VERIFONE_PROMPT.getValue());
		
		request.setValue(10, getLanguage());
		request.setValue(11, "");

	//	request.setValue(12, "Qty  DeptCode Style Description Extended Price");
		request.setValue(12, "Qty |Model |Description |Price");
		if(line!=null && theTxn!=null){
		log.info("Transaction Type : "+theTxn.getTransactionType());
		//Vivek Mishra : Added condition in order to send tax only for sale and return transaction.
		if((theTxn.getTransactionType()).equals("SALE") || (theTxn.getTransactionType()).equals("RETN") || (theTxn.getTransactionType()).equals("SALE/RETN") || (theTxn.getTransactionType()).equals("PRSO")){
		try {
			if(fromPayment){
				//Vivek Mishra : Added to show proper Sub Total and Due Amount in case of presale transactions.
				 if((theTxn.getTransactionType()).equals("PRSO"))
				 {
				request.setValue(
						19,
					"TenderAmt|"
							+ format150Currency(txn.getTotalPaymentAmount().formattedStringValue())
						+ "|Tax|" + format150Currency(tax.formattedStringValue()) + "|Amount Due|"
								+ format150Currency((preAmtDue.subtract(theTxn.getTotalPaymentAmount())).formattedStringValue()) + "|Total|"
									+ format150Currency(preAmtDue.formattedStringValue()));
				 }//Ends
			else {
				request.setValue(
						19,
					"TenderAmt|"
							+ format150Currency(txn.getTotalPaymentAmount().formattedStringValue())
						+ "|Tax|" + format150Currency(tax.formattedStringValue()) + "|Amount Due|"
						+ format150Currency((theTxn.getCompositeTotalAmountDue().subtract(theTxn.getTotalPaymentAmount())).formattedStringValue()) + "|Total|"
							+ format150Currency(theTxn.getCompositeTotalAmountDue().formattedStringValue()));
				 }
			}
			else {
				//Vivek Mishra : Added to show proper Sub Total and Due Amount in case of presale transactions.
				if((theTxn.getTransactionType()).equals("PRSO"))
				 {
					request.setValue(
							19,
							"Subtotal|"
										+ format150Currency(theTxn.getCompositeNetAmount().formattedStringValue())
									+ "|Tax|" + format150Currency(tax.formattedStringValue()) + "|Amount Due|"
									+ format150Currency((preAmtDue.subtract(theTxn.getTotalPaymentAmount())).formattedStringValue()) + "|Total|"
										+ format150Currency(preAmtDue.formattedStringValue()));
				 }//Ends
				else{
				request.setValue(
				19,
				"Subtotal|"
							+ format150Currency(theTxn.getCompositeNetAmount().formattedStringValue())
						+ "|Tax|" + format150Currency(tax.formattedStringValue()) + "|Amount Due|"
						+ format150Currency((theTxn.getCompositeTotalAmountDue().subtract(theTxn.getTotalPaymentAmount())).formattedStringValue()) + "|Total|"
							+ format150Currency(theTxn.getCompositeTotalAmountDue().formattedStringValue()));
				}
			}
		} catch (CurrencyException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		}
		else{
			try {
				request.setValue(
						19,
						"Subtotal|"
								+ format150Currency(theTxn.getCompositeNetAmount().formattedStringValue())
							+ "|Amount Due|"+  format150Currency((theTxn.getCompositeTotalAmountDue().subtract(theTxn.getTotalPaymentAmount())).formattedStringValue()) + "|Total|"+format150Currency(theTxn.getCompositeTotalAmountDue().formattedStringValue()));
			} catch (CurrencyException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		//Ends
		//Vivek Mishra : Added Condition for Sending *POSITEMS for PreSale, Consignment and Reservation when price override is true 
		}
		//request.setValue(12,itemLookup.getDefaultQuantity().toString()+".."+itemLookup.getDescription()+"………."+itemLookup.getSellingPrice().formattedStringValue());
		}
		
	/*if(clearMessage){
			request.setValue(12, "*");
		}*/
	
/*		if(Refresh){
			System.out.println("AJBMessageCodes.IX_VERIFONE_POS_ITEMS.getValue()>>>>>"+AJBMessageCodes.IX_VERIFONE_POS_ITEMS.getValue());
			request.setValue(20, AJBMessageCodes.IX_VERIFONE_POS_ITEMS.getValue());
		}//End
*/		//Welcome message 
		 if(idleMessage){
			request.setValue(20, AJBMessageCodes.IX_VERIFONE_RESET.getValue());
			return request;
		}
		//Clear screen 
		else if(clearMessage){
			request.setValue(20, AJBMessageCodes.IX_VERIFONE_POS_ITEMS_CLEAR.getValue());
			return request;
		}
		
		else if(Refresh){
			request.setValue(20, AJBMessageCodes.IX_VERIFONE_POS_ITEMS_REFRESH.getValue());	
			if(lineItemArray!=null){
				int qty = 0;
				for(int i=0; i<lineItemArray.length ; i++){
					lineItem=lineItemArray[i];
					itm = (CMSItem) lineItem.getItem();
				
			
				/*request.setValue(index, line.getQuantity());
				request.setValue(index+1, itm.getDepartment());
				//Vivek Mishra : Added check for null for Deposit during reservation.
				if(itm.getStyleNum()!=null)
				request.setValue(index+2, itm.getStyleNum());
				request.setValue(index+3, line.getItemDescription());
				request.setValue(index+4, line.getExtendedNetAmount().formattedStringValue());*/
				//Vivek Mishra  :Changed the condition in order to restrict sending already deleted item with refresh request.
					//if(line!=null)
					if(line!=null && !(lineItem.isDeletedFlag())){
			//Vivek Mishra : Added to send barcode in place of style type for misc items			
			if(lineItemArray[i].getMiscItemId()!=null)	{		
			   // request.setValue(index, lineItem.getQuantity()+" "+itm.getDepartment()+"  "+itm.getBarCode()+ "  "+"Service Item"+ "   "+lineItem.getExtendedNetAmount().formattedStringValue() );
				 request.setValue(index, lineItem.getQuantity()+ " "+itm.getBarCode()+ " "+"Service Item"+ " "+format150Currency(lineItem.getExtendedNetAmount().formattedStringValue()) );
			}else{
				//request.setValue(index, lineItem.getQuantity()+" "+itm.getDepartment()+"  "+itm.getStyleNum()+ "  "+itm.getDescription()+ "   "+lineItem.getExtendedNetAmount().formattedStringValue() );
				request.setValue(index, lineItem.getQuantity()+" "+itm.getModel()+ " "+itm.getDescription()+ " "+format150Currency(lineItem.getExtendedNetAmount().formattedStringValue()) );
			}//Ends
			index++;
			}
		}
			}
			return request;
		}
		//Vivek Mishra : Changed the condition from if to else if
		//if(deleteFlag){
	/*	else if(deleteFlag){
		request.setValue(20, AJBMessageCodes.IX_VERIFONE_POS_ITEMS_REMOVE.getValue());
		if(line!=null){
		request.setValue(21, line.getQuantity()+" "+itm.getDepartment()+"  "+itm.getStyleNum()+ "  "+itm.getDescription()+ "   "+line.getExtendedNetAmount().formattedStringValue());
		}
		return request;
		}*/else{
			//Vivek Mishra : Added condition for price override
			//Added null check by Anjana for final REFRESH request when lines are empty
			//Vivek Mishra : Changed the condition to avoid sending *POSITEM-CHANGE request for misc items.
			//if(line !=null && (line.getQuantity()>1 || line.isManualUnitPrice()))
			if(line !=null && (line.getQuantity()>1 || line.isManualUnitPrice()) && itm.getStyleNum()!=null){
				//change here for multiple quantity 
				request.setValue(20, AJBMessageCodes.IX_VERIFONE_POS_ITEMS_CHANGE.getValue());
				
			}else{
				
		request.setValue(20, AJBMessageCodes.IX_VERIFONE_POS_ITEMS.getValue());
		}
		}
		//Added null check for line as for the Final REFRESH request lines are null
		if(line!=null && !clearMessage){
			request.setValue(index, line.getQuantity()+" "+itm.getDepartment()+"  "+itm.getModel()+ "  "+itm.getDescription()+ "   "+line.getExtendedNetAmount().formattedStringValue() );
			
		}
		
		//	request.setValue(index, line.getQuantity()+" "+itm.getDepartment()+"  "+itm.getStyleNum()+ "  "+itm.getDescription()+ "   "+line.getExtendedNetAmount().formattedStringValue() );
	
		// posRequest.setRawRequest(request);


		return request;
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
		//request.setValue(16, sequence);
		//Ends
		
		request.setValue(20, AJBMessageCodes.IX_VERIFONE_SIGNATURE.getValue());
		SimpleDateFormat sdfTime = new SimpleDateFormat("HHmmss");
		Date now = new Date();
		String strTime = sdfTime.format(now);
		request.setValue(50,strTime);
		

		return request;
	}
	
	
	// Formatter for Manual Credit Card for SAF AJB request. It fetches the constant
		// values from AJBMessageCodes.java.
		// Currently using following message format:
		// IxCmd,IxMultiLayeredSTAN,,,IxTimeOut,IxDebitCredit,,IxStoreNumber,IxTerminalNumber,IxTranType,,,IxAccount,IxExpDate,,IxAmount,IxInvoice,,IxForceAuthCode,,IxOptions,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,
		public AJBRequestResponseMessage formatAJBSAFRequest(CreditCard cc, boolean isManualOverride) {
			AJBRequestResponseMessage request = new AJBRequestResponseMessage();
			// Generating a unique key which is getting set under field 2
			// IxMultiLayeredSTAN in the request for uniquely identify the response
			// for a particular request as this field is echos back with the
			// response.
			sequence = formatSequence(transactionNumber.getSequenceNumber()
					.intValue()) + Calendar.getInstance().get(Calendar.MILLISECOND);
			request.setValue(0, AJBMessageCodes.IX_CMD_SAF_RESPONSE.getValue());
				//As per Enrique's email everywhere commenting request[1] and adding the sequence directly to feild17 
			//request.setValue(1, sequence);
			request.setValue(4, getTimeOutInSeconds());
			request.setValue(5, AJBMessageCodes.IX_DEBIT_CREDIT_CREDIT.getValue());
			request.setValue(7, txn.getStore().getId());
			request.setValue(8, currRegister.getId());
			//sending Force for SAF
			
			//As per Apar_21665 doc we have to send SALE
			request.setValue(9, AJBMessageCodes.IX_TRAN_TYPE_CHECK_SALE.getValue());

			request.setValue(12, cc.getAccountNumber());

			request.setValue(13, createYYMMDate(cc.getExpirationDate()));
			request.setValue(15, formatCurrency(cc.getAmount().absoluteValue()
					.stringValue()));

			// Vivek Mishra : Setting the complete invoice number instead of just
			// the transaction sequence number.
			/*request.setValue(16, formatSequence(transactionNumber
					.getSequenceNumber().intValue()));*/
			//Vivek Mishra : Setting unique AJB Sequence instead of invoice number in order to make every request unique
			request.setValue(16, sequence);
			//Ends
			/*The POS must ignore the floor limit and prompt the operator to call for
			authorization. The POS must then send the approval code obtained by phone to
			FiPay in a 111 SAF request. The parameter TELAUTH must exist in Field 21
			IxOptions and Field 19 IxForceAuthCode must be populated with the approval
			code.*/
			
			/*For 111 SAF requests, this field contains “FORCE” for transactions below the
			floor limit. -->what to send here FORCE or respcode*/
			//request.setValue(18, IX_DEBIT_CREDIT_FORCE);
			request.setValue(18, cc.getRespAuthorizationCode());
			//how to check if transaction is greater than floor limit 
			request.setValue(20, AJBMessageCodes.IX_OPTIONS_TELEAUTH.getValue());

			return request;
		}
		
		// Formatter for Manual Credit Card for SAF AJB request. It fetches the constant
		// values from AJBMessageCodes.java.
		// Currently using following message format:
		// IxCmd,IxMultiLayeredSTAN,,,IxTimeOut,IxDebitCredit,,IxStoreNumber,IxTerminalNumber,IxTranType,,,IxAccount,IxExpDate,,IxAmount,IxInvoice,,IxForceAuthCode,,IxOptions,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,
		public AJBRequestResponseMessage formatAJBSAFReqWRes(CreditCard cc) {
			//Vivek Mishra : Added to remove *AcquirerBackup from SAF request as suggested by Eric Taskiran on 12-OCT-2016
			AJBRequestResponseMessage request = new AJBRequestResponseMessage(cc.getRespObject().toString().replace("*AcquirerBackup", ""));
			//Ends here 12-OCT-2016
			request.setValue(0, AJBMessageCodes.IX_CMD_SAF_REQUEST.getValue());
			request.setValue(3, "");
			request.setValue(12, cc.getAccountNumber());

			request.setValue(18, cc.getRespAuthorizationCode());
			//how to check if transaction is greater than floor limit 
			String IxOptions = request.getValue(20);
			request.setValue(20, AJBMessageCodes.IX_OPTIONS_TELEAUTH.getValue()+" "+ IxOptions);
			
			log.info("request >>> for SAF >>>>> " + request + "<<<<<<<<<<<<<");

			return request;
		}
		
		
	/*
	 * */
		public AJBRequestResponseMessage formatAJBTimeoutReversalReq(CreditCard cc) {

            //Vivek Mishra : Added to remove *AcquirerBackup from timeout reversal request as suggested by Eric Taskiran on 12-OCT-2016
			AJBRequestResponseMessage request = new AJBRequestResponseMessage(cc.getRespObject().toString().replace("*AcquirerBackup", ""));
			//Ends here 12-OCT-2016
			request.setValue(0, AJBMessageCodes.IX_CMD_SAF_REQUEST.getValue());
			request.setValue(3, "");			
			//Vivek Mishra : Added to send reversal request as void 27-DEC-2016
			if(request.getValue(9)!=null)
			request.setValue(9, "void"+request.getValue(9));
			//Ends here 27-DEC-2016
			log.info("request >>> for Timeout Reversal is >>>>> " + request + "<<<<<<<<<<<<<");

			return request;
		}
	
		// Formatter for Credit Card Reversal/VoidSale AJB SAF request
		// Currently using following message format:
		// IxCmd,,,,IxTimeOut,IxDebitCredit,,IxStoreNumber,IxTerminalNumber,IxTranType,,,IxInvoice,,,IxAmount,IxMultiLayeredSTAN,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,
		public AJBRequestResponseMessage formatAJBCreditReversalSAFRequest(
				CreditCard cc, String storeNumber, String orgTxnNo) {
			AJBRequestResponseMessage request = new AJBRequestResponseMessage();
			//Vivek Mishra : Added to fix the duplicate sequence issue.
			sequence = formatSequence(transactionNumber.getSequenceNumber()
					.intValue()) + Calendar.getInstance().get(Calendar.MILLISECOND);
			//Ends
			request.setValue(0, AJBMessageCodes.IX_CMD_SAF_RESPONSE.getValue());
			// request.setValue(1, sequence);
			request.setValue(4, getTimeOutInSeconds());
			request.setValue(5, AJBMessageCodes.IX_DEBIT_CREDIT_VOID.getValue());
			request.setValue(7, storeNumber);
			request.setValue(8, currRegister.getId());
			request.setValue(9, AJBMessageCodes.IX_TRAN_TYPE_CARD_SALE.getValue());

			// Vivek Mishra : Setting here the original sale transaction's invoice
			// number instead on account number as asked by Danny.
			// request.setValue(12, cc.getAccountNumber());
			//request.setValue(12, orgTxnNo.replace("*", ""));
			//Vivek Mishra : Setting unique AJB Sequence instead of invoice number in order to make every request unique
			request.setValue(12, cc.getAjbSequence());
			//Ends
			request.setValue(15, formatCurrency(cc.getAmount().absoluteValue()
					.stringValue()));

			// Vivek Mishra : Setting the unique sequence number here as asked by
			// Danny.
			request.setValue(16, sequence);

			/*For 111 SAF requests, this field contains “FORCE” for transactions below the
				floor limit. -->what to send here FORCE or respcode*/
				//request.setValue(18, IX_DEBIT_CREDIT_FORCE);
				/*request.setValue(18, cc.getRespAuthorizationCode());*/
				//how to check if transaction is greater than floor limit 
				/*request.setValue(20, AJBMessageCodes.IX_OPTIONS_TELEAUTH.getValue());*/
				//Vivek Mishra : Setting AJB sequence in credit card object as SAF response in not necessarily be sent from AJB
				
				cc.setAjbSequence(request.getValue(16));

			return request;
		}

		// Formatter for Credit Card Reversal/VoidRefund AJB SAF request
		// Currently using following message format:
		// IxCmd,,,,IxTimeOut,IxDebitCredit,,IxStoreNumber,IxTerminalNumber,IxTranType,,,IxInvoice,,,IxAmount,IxMultiLayeredSTAN,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,
		public AJBRequestResponseMessage formatAJBCreditReversalRefundSAFRequest(
				CreditCard cc, String storeNumber, String orgTxnNo) {
			AJBRequestResponseMessage request = new AJBRequestResponseMessage();
			sequence = formatSequence(transactionNumber.getSequenceNumber()
					.intValue()) + Calendar.getInstance().get(Calendar.MILLISECOND);;
			request.setValue(0, AJBMessageCodes.IX_CMD_SAF_RESPONSE.getValue());
			request.setValue(4, getTimeOutInSeconds());
			request.setValue(5, AJBMessageCodes.IX_DEBIT_CREDIT_VOID.getValue());
			request.setValue(7, storeNumber);
			request.setValue(8, currRegister.getId());
			request.setValue(9, AJBMessageCodes.IX_TRAN_TYPE_REFUND.getValue());
			//request.setValue(12, orgTxnNo.replace("*", ""));
			//Vivek Mishra : Setting unique AJB Sequence instead of invoice number in order to make every request unique
			request.setValue(12, cc.getAjbSequence());
			//Ends
			request.setValue(15, formatCurrency(cc.getAmount().absoluteValue()
					.stringValue()));
			request.setValue(16, sequence);

			/*For 111 SAF requests, this field contains “FORCE” for transactions below the
				floor limit. -->what to send here FORCE or respcode*/
				//request.setValue(18, IX_DEBIT_CREDIT_FORCE);
				/*request.setValue(18, cc.getRespAuthorizationCode());*/
				//how to check if transaction is greater than floor limit 
				/*request.setValue(20, AJBMessageCodes.IX_OPTIONS_TELEAUTH.getValue());*/
				//Vivek Mishra : Setting AJB sequence in credit card object as SAF response in not necessarily be sent from AJB
				
				cc.setAjbSequence(request.getValue(16));

			return request;
		}

		// Formatter for Debit Void sale AJB SAF request. As asked by Armani/AJB in
		// weekly status meeting on 13-MAY-15 currently creating credit refund
		// message for debit void sale.
		// Currently using following message format:
		// IxCmd,IxMultiLayeredSTAN,,,IxTimeOut,IxDebitCredit,,IxStoreNumber,IxTerminalNumber,IxTranType,,,IxAccount,IxExpDate,,IxAmount,IxInvoice,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,
		public AJBRequestResponseMessage formatAJBDebitReversalSAFRequest(
				CreditCard cc, String storeNumber, String orgTxnNo) {

			AJBRequestResponseMessage request = new AJBRequestResponseMessage();
			sequence = formatSequence(transactionNumber.getSequenceNumber()
					.intValue()) + Calendar.getInstance().get(Calendar.MILLISECOND);
		//As per Enrique's email everywhere commenting request[1] and adding the sequence directly to feild17 
			//request.setValue(1, sequence);
			request.setValue(0, AJBMessageCodes.IX_CMD_SAF_RESPONSE.getValue());
			request.setValue(4, getTimeOutInSeconds());
			request.setValue(5, AJBMessageCodes.IX_DEBIT_CREDIT_CREDIT.getValue());
			request.setValue(7, storeNumber);
			request.setValue(8, currRegister.getId());
			request.setValue(9, AJBMessageCodes.IX_TRAN_TYPE_REFUND.getValue());

			// Vivek Mishra : Commented as Danny asked to send the complete account
			// number instead of masked one.
			// request.setValue(12, maskCreditCardNo(cc.getAccountNumber()));
			request.setValue(12, cc.getAccountNumber());

			request.setValue(13, createYYMMDate(cc.getExpirationDate()));
			request.setValue(15, formatCurrency(cc.getAmount().absoluteValue()
					.stringValue()));

			// Vivek Mishra : Setting the complete invoice number instead of just
			// the transaction sequence number.
			// Vivek Mishra : Setting the complete invoice number instead of just
			// the transaction sequence number.
			/*request.setValue(16, formatSequence(transactionNumber
					.getSequenceNumber().intValue()));*/
			//Vivek Mishra : Setting unique AJB Sequence instead of invoice number in order to make every request unique
			request.setValue(16, sequence);
			//Ends

			/*For 111 SAF requests, this field contains “FORCE” for transactions below the
				floor limit. -->what to send here FORCE or respcode*/
				//request.setValue(18, IX_DEBIT_CREDIT_FORCE);
				/*request.setValue(18, cc.getRespAuthorizationCode());*/
				//how to check if transaction is greater than floor limit 
				/*request.setValue(20, AJBMessageCodes.IX_OPTIONS_TELEAUTH.getValue());*/
				//Vivek Mishra : Setting AJB sequence in credit card object as SAF response in not necessarily be sent from AJB
				
				cc.setAjbSequence(request.getValue(16));

			return request;

		}
	
	public int getLanguage() {
		return LANGUAGE;
	}

	// Provides timeout's value
	public int getTimeOutInSeconds()
	{
		
		try {
			AJBServiceManager current = AJBServiceManager.getCurrent();
			// Yashwanth : Changed ResTimeOut to getter and setter and referencing the variable
			TIME_OUT_SECONDS = current.getAjbResTimeOutSec();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			// if exception set to default 
			TIME_OUT_SECONDS = 120;
		}
		
		return TIME_OUT_SECONDS;
	}
	
	
	// Provides timeout's value
		public int getItemTimeOutInSeconds()
		{
			int item_response_time_out = 10;
			try {
				AJBServiceManager current = AJBServiceManager.getCurrent();
				item_response_time_out = current.getItemAjbResTimeOutSec();
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				// if exception set to default 
			}
			
			return item_response_time_out;
		}
	// Formats amount by removing the decimal point as per AJB's standard.
	public String formatCurrency(String price) {
		String priceSplit = price.replace(".", "");
		//Vivek Mishra : Added for removing "," from tender amount
		priceSplit = priceSplit.replace(",", "");
		//Ends
		return priceSplit;

	}

	// Formats amount by removing the comma  as per AJB's standard.
	public String format150Currency(String price) {
		String priceSplit = price.replace(",", "");
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

	// Format date in requested format.
	private String createYYMMDate(Date calenderDate) {
		SimpleDateFormat fmt = new SimpleDateFormat("yyMM");
		String strDate = fmt.format(calenderDate);
		return strDate;
	}

	// Masked the credit card number.
	public static String maskCreditCardNo(String fmtAccountNum) {
		if (fmtAccountNum == null) {
			
			return "";
		}
		fmtAccountNum = fmtAccountNum.trim();
		if (!fmtAccountNum.equals("")) {
			try {
				// System.out.println ("Masking the Account Num");
				int strLength = fmtAccountNum.length();
				String appo_temp = "*************************";
				if (strLength < 10) {
					int l = (10 - strLength) + 1;
					fmtAccountNum = appo_temp.substring(0, l) + fmtAccountNum;
				}
				strLength = fmtAccountNum.length();
				String firstStr = fmtAccountNum.substring(0, (strLength - 10));
				String lastStr = fmtAccountNum.substring(strLength - 4);
				return firstStr + "******" + lastStr;
			} catch (Exception e) {
				System.out.println("Error in Masking the Credit Card Num :: "
						+ e.getMessage());
				// e.printStackTrace();
				return fmtAccountNum;
			}
		} else
			return fmtAccountNum;
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
