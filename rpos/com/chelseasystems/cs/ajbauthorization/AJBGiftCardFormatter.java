package com.chelseasystems.cs.ajbauthorization;

import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;

import sun.util.logging.resources.logging;

import com.chelseasystems.cr.appmgr.AppManager;
import com.chelseasystems.cr.appmgr.BrowserManager;
import com.chelseasystems.cr.payment.CreditCard;
import com.chelseasystems.cr.payment.Redeemable;
import com.chelseasystems.cr.pos.PaymentTransaction;
import com.chelseasystems.cr.txnnumber.TransactionNumberStore;
import com.chelseasystems.cs.payment.CMSDueBill;
import com.chelseasystems.cs.payment.CMSDueBillIssue;
import com.chelseasystems.cs.payment.CMSRedeemable;
import com.chelseasystems.cs.payment.CMSStoreValueCard;
import com.chelseasystems.cs.register.CMSRegister;
import com.chelseasystems.cs.store.CMSStore;
import com.chelseasystems.cr.pos.POSLineItem;

public class AJBGiftCardFormatter {

	private PaymentTransaction txn;
	private CMSRegister currRegister;
	private String sequence;
	private TransactionNumberStore transactionNumber;
	private int TIME_OUT_SECONDS;
	public static final String TXN_NUMBER = "TXN_NUMBER";
	private CMSStore store;

	public static int seqPostfix = 10;
	AJBRequestResponseMessage request;
	int reqMesgSize;

	public AJBGiftCardFormatter() {
		txn = (PaymentTransaction) AppManager.getCurrent().getStateObject(
				"TXN_POS");
		currRegister = (CMSRegister) AppManager.getCurrent().getGlobalObject(
				"REGISTER");
		transactionNumber = (TransactionNumberStore) BrowserManager
				.getInstance().getGlobalObject(TXN_NUMBER);
		store = (CMSStore) AppManager.getCurrent().getGlobalObject("STORE"); //Added by Himani for redeemable buy back
	}

	// Vivek Mishra : Added to format 107 swipe request for gift cards
	// IxCmd,,IxTimeout,IxSwipe,,,,,IxTerminalNumber,,,,,,,,,,,,IxOptions,
	public AJBRequestResponseMessage formatAJBGiftCardSwipeRequest() {
		reqMesgSize = 21;
		request = new AJBRequestResponseMessage(reqMesgSize);
		// IxCmd
		request.setValue(0, AJBMessageCodes.IX_CMD_GIFTCARD_SWIPE.getValue());
		// IxTimeout
		request.setValue(2, String.valueOf(getTimeOutInSeconds()));
		// IxSwipe
		request.setValue(3, AJBMessageCodes.IX_SWIPE_TRACK1AND2.getValue());
		 //fix for bug#28619 
		//IxStoreNumber 
		//request.setValue(7, txn.getStore().getId());
		 request.setValue(7,store.getId());
		// fix ends
		// IxTerminalNumber
		request.setValue(8, currRegister.getId());
		// IxOptions
		request.setValue(20, AJBMessageCodes.IX_OPTIONS_VERIFY_CARD.getValue());
		return request;
	}

	// Vivek Mishra : Added to format 107 manual entry request for gift cards
	// IxCmd,,IxTimeout,IxSwipe,,,,,IxTerminalNumber,,,,,,,,,,,,IxOptions,
	public AJBRequestResponseMessage formatAJBGiftCardManualEntryRequest() {
		reqMesgSize = 21;
		request = new AJBRequestResponseMessage(reqMesgSize);
		// IxCmd
		request.setValue(0, AJBMessageCodes.IX_CMD_GIFTCARD_SWIPE.getValue());
		// IxTimeout
		request.setValue(2, String.valueOf(getTimeOutInSeconds()));
		// IxSwipe
		request.setValue(3, AJBMessageCodes.IX_SWIPE_MANUAL_ENTRY.getValue());
		//IxStoreNumber
		//request.setValue(7, txn.getStore().getId());
		//bug #28213
		 request.setValue(7,store.getId());		
		// IxTerminalNumber
		request.setValue(8, currRegister.getId());
		// IxOptions
		//request.setValue(20, AJBMessageCodes.IX_SWIPE_MANUAL_ENTRY.getValue());
		return request;
	}

	// Vivek Mishra : Added to format Gift Card Sale 100 request
	// IxCmd,,,,IxTimeout,IxDebitCredit,,IxStoreNumber,IxTerminalNumber,IxTranType,,,,,IxSwipe,IxAmount,IxInvoice,,
	public AJBRequestResponseMessage formatAJBGiftCardSaleRequest(
			CMSStoreValueCard gc) {
		reqMesgSize = 119;
		request = new AJBRequestResponseMessage(reqMesgSize);
		sequence = formatSequence(transactionNumber.getSequenceNumber()
				.intValue()) + Calendar.getInstance().get(Calendar.MILLISECOND);
		// IxCmd
		request.setValue(0, AJBMessageCodes.IX_CMD_REQUEST.getValue());
		// IxTimeout
		request.setValue(4, String.valueOf(getTimeOutInSeconds()));
		// IxDebitCredit
		request.setValue(5, AJBMessageCodes.IX_DEBIT_CREDIT_GIFTCARD.getValue());
		// IxStoreNumber
		request.setValue(7, txn.getStore().getId());
		// IxTerminalNumber
		request.setValue(8, currRegister.getId());
		// IxTranType
		request.setValue(9, AJBMessageCodes.IX_TRAN_TYPE_CARD_SALE.getValue());
		// IxAccount
		if (gc.getId() != null) {
			request.setValue(12, gc.getControlNum());
			// request.setValue(13, "5896290000000001");
		}
		// IxSwipe
		if (gc.getTrackData() != null) {
			request.setValue(14, gc.getTrackData());
		}
		// IxAmount
		request.setValue(15, formatCurrency(gc.getAmount().getAbsoluteValue().stringValue()));
		// IvInvoice
		request.setValue(16, sequence);
		return request;

	}
	
	public AJBRequestResponseMessage formatAJBGiftCardSaleRequest(
			CMSDueBill db) {
		reqMesgSize = 119;
		request = new AJBRequestResponseMessage(reqMesgSize);
		sequence = formatSequence(transactionNumber.getSequenceNumber()
				.intValue()) + Calendar.getInstance().get(Calendar.MILLISECOND);
		// IxCmd
		request.setValue(0, AJBMessageCodes.IX_CMD_REQUEST.getValue());
		// IxTimeout
		request.setValue(4, String.valueOf(getTimeOutInSeconds()));
		// IxDebitCredit
		request.setValue(5, AJBMessageCodes.IX_DEBIT_CREDIT_GIFTCARD.getValue());
		// IxStoreNumber
		request.setValue(7, txn.getStore().getId());
		// IxTerminalNumber
		request.setValue(8, currRegister.getId());
		// IxTranType
		request.setValue(9, AJBMessageCodes.IX_TRAN_TYPE_CARD_SALE.getValue());
		// IxAccount
		if (db.getId() != null) {
			request.setValue(12, db.getId());
			// request.setValue(13, "5896290000000001");
		}
		// IxSwipe
		if (db.getTrackData() != null) {
			request.setValue(14, db.getTrackData());
		}
		// IxAmount
		request.setValue(15, formatCurrency(db.getAmount().getAbsoluteValue().stringValue()));
		// IvInvoice
		request.setValue(16, sequence);
		return request;
	}

	// Vivek Mishra : Added to format Gift Card Refund 100 request
	// IxCmd,,,,IxTimeout,IxDebitCredit,,IxStoreNumber,IxTerminalNumber,IxTranType,,,,,IxSwipe,IxAmount,IxInvoice,,
	public AJBRequestResponseMessage formatAJBGiftCardRefundRequest(
			CMSStoreValueCard gc) {
		reqMesgSize = 119;
		request = new AJBRequestResponseMessage(reqMesgSize);
		sequence = formatSequence(transactionNumber.getSequenceNumber()
				.intValue()) + Calendar.getInstance().get(Calendar.MILLISECOND);
		// IxCmd
		request.setValue(0, AJBMessageCodes.IX_CMD_REQUEST.getValue());
		// IxTimeout
		request.setValue(4, String.valueOf(getTimeOutInSeconds()));
		// IxDebitCredit
		request.setValue(5, AJBMessageCodes.IX_DEBIT_CREDIT_GIFTCARD.getValue());
		// IxStoreNumber
		request.setValue(7, txn.getStore().getId());
		// IxTerminalNumber
		request.setValue(8, currRegister.getId());
		// IxTranType
		request.setValue(9, AJBMessageCodes.IX_TRAN_TYPE_REFUND.getValue());
		// IxAccount
		if (gc.getId() != null) {
			request.setValue(12, gc.getControlNum());
			// request.setValue(13, "5896290000000001");
		}
		// IxSwipe
		if (gc.getTrackData() != null) {
			request.setValue(14, gc.getTrackData());
		}
		// IxAmount
		request.setValue(15, formatCurrency(gc.getAmount().getAbsoluteValue().stringValue()));
		// IvInvoice
		request.setValue(16, sequence);
		return request;

	}
	
	public AJBRequestResponseMessage formatAJBGiftCardRefundRequest(
			CMSDueBill db) {
		reqMesgSize = 119;
		request = new AJBRequestResponseMessage(reqMesgSize);
		sequence = formatSequence(transactionNumber.getSequenceNumber()
				.intValue()) + Calendar.getInstance().get(Calendar.MILLISECOND);
		// IxCmd
		request.setValue(0, AJBMessageCodes.IX_CMD_REQUEST.getValue());
		// IxTimeout
		request.setValue(4, String.valueOf(getTimeOutInSeconds()));
		// IxDebitCredit
		request.setValue(5, AJBMessageCodes.IX_DEBIT_CREDIT_GIFTCARD.getValue());
		// IxStoreNumber
		request.setValue(7, txn.getStore().getId());
		// IxTerminalNumber
		request.setValue(8, currRegister.getId());
		// IxTranType
		request.setValue(9, AJBMessageCodes.IX_TRAN_TYPE_REFUND.getValue());
		// IxAccount
		if (db.getId() != null) {
			request.setValue(12, db.getId());
			// request.setValue(13, "5896290000000001");
		}
		// IxSwipe
		if (db.getTrackData() != null) {
			request.setValue(14, db.getTrackData());
		}
		// IxAmount
		request.setValue(15, formatCurrency(db.getAmount().getAbsoluteValue().stringValue()));
		// IvInvoice
		request.setValue(16, sequence);
		return request;

	}

	// Vivek Mishra : Added to format Gift Card Void Sale 100 request
	// IxCmd,,,,IxTimeout,IxDebitCredit,,IxStoreNumber,IxTerminalNumber,IxTranType,,,,,AjbSequence,IxAmount,IxInvoice,,
	public AJBRequestResponseMessage formatAJBGiftCardVoidSaleRequest(
			String ajbSequence, String id, String amount) {
		reqMesgSize = 119;
		request = new AJBRequestResponseMessage(reqMesgSize);
		sequence = formatSequence(transactionNumber.getSequenceNumber()
				.intValue()) + Calendar.getInstance().get(Calendar.MILLISECOND);
		// IxCmd
		request.setValue(0, AJBMessageCodes.IX_CMD_REQUEST.getValue());
		// IxTimeout
		request.setValue(4, String.valueOf(getTimeOutInSeconds()));
		// IxDebitCredit
		request.setValue(5, AJBMessageCodes.IX_DEBIT_CREDIT_GIFTCARD.getValue());
		// IxStoreNumber
		request.setValue(7, store.getId());
		// IxTerminalNumber
		request.setValue(8, currRegister.getId());
		// IxTranType
		request.setValue(9, AJBMessageCodes.IX_GIFT_CARD_VOIDSALE.getValue());
		// IxAccount
		if(id!=null)
			request.setValue(12, id);
		if (ajbSequence != null) {
			request.setValue(79, ajbSequence);
		}
		// IxAmount
		//request.setValue(15, formatCurrency(amount));
		// IvInvoice
		request.setValue(16, sequence);
		return request;

	}
	
	// Vivek Mishra : Added to format Gift Card Void Cashout 100 request
		// IxCmd,,,,IxTimeout,IxDebitCredit,,IxStoreNumber,IxTerminalNumber,IxTranType,,,,,AjbSequence,IxAmount,IxInvoice,,
		public AJBRequestResponseMessage formatAJBGiftCardVoidCashoutRequest(
				String ajbSequence, String id) {
			reqMesgSize = 119;
			request = new AJBRequestResponseMessage("100,,,0,115,giftcard,,485,4,voidCashout,,,,,5896290340000473,,4854150374155,English,,,noexpcheck nomod10 *DEVCAP=F0 *E2EE *ReqTOR *StoreSTAN 040891 *FloorLimit_0,,,,,,,,,,,,,5896295844000473,,1000,001155,,,,,,,,00400054,002703,,002703,English,12072016,175654,0,00,MPSDPN1,1,,48504155,,,,,,839,175655052,,,,,,,,,,,,,,,,200400054,,");
			/*request = new AJBRequestResponseMessage(reqMesgSize);
			sequence = formatSequence(transactionNumber.getSequenceNumber()
					.intValue()) + Calendar.getInstance().get(Calendar.MILLISECOND);
			// IxCmd
			request.setValue(0, AJBMessageCodes.IX_CMD_REQUEST.getValue());
			// IxTimeout
			request.setValue(4, String.valueOf(getTimeOutInSeconds()));
			// IxDebitCredit
			request.setValue(5, AJBMessageCodes.IX_DEBIT_CREDIT_GIFTCARD.getValue());
			// IxStoreNumber
			request.setValue(7, store.getId());
			// IxTerminalNumber
			request.setValue(8, currRegister.getId());
			// IxTranType
			request.setValue(9, AJBMessageCodes.IX_GIFT_CARD_VOIDCASHOUT.getValue());
			// IxAccount
			if(id!=null)
				request.setValue(12, id);
			if (ajbSequence != null) {
				//String[] ajbS = ajbSequence.split(",");
				//request.setValue(44, ajbSequence);
				//request.setValue(44, ajbS[0]);
				request.setValue(79, ajbSequence);
				// request.setValue(13, "5896290000000001");
			}
			// IxAmount
			//request.setValue(15, formatCurrency(amount));
			// IvInvoice
			request.setValue(16, sequence);*/
			return request;
			
			/*String voidRequest = ajbSequence.replaceFirst("101", "100");
			voidRequest = voidRequest.replaceFirst("Sale", AJBMessageCodes.IX_GIFT_CARD_VOIDSALE.getValue());
			request = new AJBRequestResponseMessage(voidRequest);
			return request;*/
			}
		
	// Vivek Mishra : Added to format Gift Card Void Sale 100 request
	// IxCmd,,,,IxTimeout,IxDebitCredit,,IxStoreNumber,IxTerminalNumber,IxTranType,,,,,AjbSequence,IxAmount,IxInvoice,,
	public AJBRequestResponseMessage formatAJBGiftCardVoidActivateRequest(
			String ajbSequence, String id) {
		reqMesgSize = 119;
		request = new AJBRequestResponseMessage(reqMesgSize);
		sequence = formatSequence(transactionNumber.getSequenceNumber()
				.intValue()) + Calendar.getInstance().get(Calendar.MILLISECOND);
		// IxCmd
		request.setValue(0, AJBMessageCodes.IX_CMD_REQUEST.getValue());
		// IxTimeout
		request.setValue(4, String.valueOf(getTimeOutInSeconds()));
		// IxDebitCredit
		request.setValue(5, AJBMessageCodes.IX_DEBIT_CREDIT_GIFTCARD.getValue());
		// IxStoreNumber
		request.setValue(7, store.getId());
		// IxTerminalNumber
		request.setValue(8, currRegister.getId());
		// IxTranType
		request.setValue(9, AJBMessageCodes.IX_GIFT_CARD_VOIDACTIVATE.getValue());
		// IxAccount
				if(id!=null)
					request.setValue(12, id);
		if (ajbSequence != null) {
			request.setValue(79, ajbSequence);
			// request.setValue(13, "5896290000000001");
		}
		// IxAmount
		//request.setValue(14, formatCurrency(amount));
		// IvInvoice
		request.setValue(16, sequence);
		return request;

	}
	
	// Vivek Mishra : Added to format Gift Card Void Sale 100 request
		// IxCmd,,,,IxTimeout,IxDebitCredit,,IxStoreNumber,IxTerminalNumber,IxTranType,,,,,AjbSequence,IxAmount,IxInvoice,,
		public AJBRequestResponseMessage formatAJBGiftCardVoidReloadRequest(
				String ajbSequence, String id) {
			reqMesgSize = 119;
			request = new AJBRequestResponseMessage(reqMesgSize);
			sequence = formatSequence(transactionNumber.getSequenceNumber()
					.intValue()) + Calendar.getInstance().get(Calendar.MILLISECOND);
			// IxCmd
			request.setValue(0, AJBMessageCodes.IX_CMD_REQUEST.getValue());
			// IxTimeout
			request.setValue(4, String.valueOf(getTimeOutInSeconds()));
			// IxDebitCredit
			request.setValue(5, AJBMessageCodes.IX_DEBIT_CREDIT_GIFTCARD.getValue());
			// IxStoreNumber
			request.setValue(7, store.getId());
			// IxTerminalNumber
			request.setValue(8, currRegister.getId());
			// IxTranType
			request.setValue(9, AJBMessageCodes.IX_GIFT_CARD_VOIDRELOAD.getValue());
			// IxAccount
			if(id!=null)
				request.setValue(12, id);
			if (ajbSequence != null) {
				request.setValue(79, ajbSequence);
				// request.setValue(13, "5896290000000001");
			}
			// IxAmount
			//request.setValue(14, formatCurrency(amount));
			// IvInvoice
			request.setValue(16, sequence);
			return request;

		}

	//vishal yevale 19 oct 2016 cmsduebillissue
	public AJBRequestResponseMessage formatAJBGiftCardRefundRequest(
			CMSDueBillIssue db) {
		reqMesgSize = 119;
		request = new AJBRequestResponseMessage(reqMesgSize);
		sequence = formatSequence(transactionNumber.getSequenceNumber()
				.intValue()) + Calendar.getInstance().get(Calendar.MILLISECOND);
		// IxCmd
		request.setValue(0, AJBMessageCodes.IX_CMD_REQUEST.getValue());
		// IxTimeout
		request.setValue(4, String.valueOf(getTimeOutInSeconds()));
		// IxDebitCredit
		request.setValue(5, AJBMessageCodes.IX_DEBIT_CREDIT_GIFTCARD.getValue());
		// IxStoreNumber
		request.setValue(7, txn.getStore().getId());
		// IxTerminalNumber
		request.setValue(8, currRegister.getId());
		// IxTranType
		request.setValue(9, AJBMessageCodes.IX_TRAN_TYPE_REFUND.getValue());
		// IxAccount
		if (db.getId() != null) {
			request.setValue(12, db.getId());
		}
		// IxSwipe
		if (db.getTrackData() != null) {
			request.setValue(14, db.getTrackData());
		}
		// IxAmount
		request.setValue(14, db.getAmount().getAbsoluteValue().stringValue());
		// IvInvoice
		request.setValue(16, sequence);
		return request;

	}
	// Vivek Mishra : Added to format Gift Card Void Refund 100 request
	// IxCmd,,,,IxTimeout,IxDebitCredit,,IxStoreNumber,IxTerminalNumber,IxTranType,,,,,AjbSequence,IxAmount,IxInvoice,,
	public AJBRequestResponseMessage formatAJBGiftCardVoidRefundRequest(
			CMSRedeemable gc) {
		reqMesgSize = 119;
		request = new AJBRequestResponseMessage(reqMesgSize);
		sequence = formatSequence(transactionNumber.getSequenceNumber()
				.intValue()) + Calendar.getInstance().get(Calendar.MILLISECOND);
		// IxCmd
		request.setValue(0, AJBMessageCodes.IX_CMD_REQUEST.getValue());
		// IxTimeout
		request.setValue(4, String.valueOf(getTimeOutInSeconds()));
		// IxDebitCredit
		request.setValue(5, AJBMessageCodes.IX_DEBIT_CREDIT_VOID.getValue());
		// IxStoreNumber
		request.setValue(7, store.getId());
		// IxTerminalNumber
		request.setValue(8, currRegister.getId());
		// IxTranType
		request.setValue(9, AJBMessageCodes.IX_TRAN_TYPE_REFUND.getValue());
		// IxAccount
		if (gc.getAjbSequence() != null) {
			request.setValue(13, gc.getAjbSequence());
			// request.setValue(13, "5896290000000001");
		}
		// IxAmount
		request.setValue(14, gc.getAmount().getAbsoluteValue().stringValue());
		// IvInvoice
		request.setValue(16, sequence);
		return request;

	}

	// Vivek Mishra : Added to format Gift Card Sale SAF 111 request
	// IxCmd,,,,IxTimeout,IxDebitCredit,,IxStoreNumber,IxTerminalNumber,IxTranType,,,,,IxSwipe,IxAmount,IxInvoice,,
	public AJBRequestResponseMessage formatAJBGiftCardSaleSAFRequest(
			CMSRedeemable gc) {
		reqMesgSize = 119;
		request = new AJBRequestResponseMessage(reqMesgSize);
		sequence = formatSequence(transactionNumber.getSequenceNumber()
				.intValue()) + Calendar.getInstance().get(Calendar.MILLISECOND);
		// IxCmd
		request.setValue(0, AJBMessageCodes.IX_CMD_SAF_REQUEST.getValue());
		// IxTimeout
		request.setValue(4, String.valueOf(getTimeOutInSeconds()));
		// IxDebitCredit
		request.setValue(5, AJBMessageCodes.IX_DEBIT_CREDIT_GIFTCARD.getValue());
		// IxStoreNumber
		request.setValue(7, txn.getStore().getId());
		// IxTerminalNumber
		request.setValue(8, currRegister.getId());
		// IxTranType
		request.setValue(9, AJBMessageCodes.IX_TRAN_TYPE_CARD_SALE.getValue());
		// IxAccount
		if (gc.getId() != null) {
			request.setValue(13, gc.getControlNum());
			// request.setValue(13, "5896290000000001");
		}
		// IxSwipe
		if (gc.getTrackData() != null) {
			request.setValue(14, gc.getTrackData());
		}
		// IxAmount
		request.setValue(15, gc.getAmount().getAbsoluteValue().stringValue());
		// IvInvoice
		request.setValue(16, sequence);
		return request;
	}

	// Vivek Mishra : Added to format Gift Card Balance Inquiry 100 request
	// IxCmd,,,,IxTimeout,IxDebitCredit,,IxStoreNumber,IxTerminalNumber,IxTranType,,,,,IxSwipe,,IvInvoice,,
	// 100,,,,120,GiftCard,,1,1,BalanceInquiry,,,601056******8275,2501,601056******8275=2501****************,,054950,,,,*StoreSTAN
	// 054950 *AptosRefNum 112-549 *MsgEP
	// 127.0.0.1:49284,,,4444,,,,,,,,,,,,,,,,,,,,,,,,,,09122016,122149,,
	public AJBRequestResponseMessage formatAJBGiftCardBalanceInquiryRequest(String accountNo) {

		reqMesgSize = 119;
		request = new AJBRequestResponseMessage(reqMesgSize);
		sequence = formatSequence(transactionNumber.getSequenceNumber()
				.intValue()) + Calendar.getInstance().get(Calendar.MILLISECOND);
		// IxCmd
		request.setValue(0, AJBMessageCodes.IX_CMD_REQUEST.getValue());
		// IxTimeout
		request.setValue(4, String.valueOf(getTimeOutInSeconds()));
		// IxDebitCredit
		request.setValue(5, AJBMessageCodes.IX_DEBIT_CREDIT_GIFTCARD.getValue());
		// IxStoreNumber
		if(txn!=null)
			request.setValue(7, txn.getStore().getId());
		else
			request.setValue(7, store.getId());
		// IxTerminalNumber
		request.setValue(8, currRegister.getId());
		// IxTranType
		request.setValue(9,
				AJBMessageCodes.IX_TRAN_TYPE_BALANCE_INQUIRY.getValue());

		if (accountNo!= null) {
			// IxAccount
			request.setValue(14, accountNo);
		}
		// IxInvoice
		request.setValue(16, sequence);
		return request;

	}

	// Vivek Mishra : Added to format Gift Card Activate 100 request
	// IxCmd,,,,IxTimeout,IxDebitCredit,,IxStoreNumber,IxTerminalNumber,IxTranType,,,,,IxSwipe,IxAmount,IxInvoice,,
	public AJBRequestResponseMessage formatAJBGiftCardActivationRequest(String accountNo, String amount) {
		reqMesgSize = 119;
		request = new AJBRequestResponseMessage(reqMesgSize);
		sequence = formatSequence(transactionNumber.getSequenceNumber()
				.intValue()) + Calendar.getInstance().get(Calendar.MILLISECOND);
		// IxCmd
		request.setValue(0, AJBMessageCodes.IX_CMD_REQUEST.getValue());
		// IxTimeout
		request.setValue(4, String.valueOf(getTimeOutInSeconds()));
		// IxDebitCredit
		request.setValue(5, AJBMessageCodes.IX_DEBIT_CREDIT_GIFTCARD.getValue());
		// IxStoreNumber
		request.setValue(7, txn.getStore().getId());
		// IxTerminalNumber
		request.setValue(8, currRegister.getId());
		// IxTranType
		request.setValue(9, AJBMessageCodes.IX_TRAN_TYPE_ACTIVATE.getValue());
		// IxSwipe
		if (accountNo != null) {
			request.setValue(14, accountNo);
		}
		
		// IxAmount
				if(amount != null)
				request.setValue(15, formatCurrency(amount));
				
		// IvInvoice
		request.setValue(16, sequence);
		return request;

	}

	// Vivek Mishra : Added to format Gift Card Reload 100 request
	// IxCmd,,,,IxTimeout,IxDebitCredit,,IxStoreNumber,IxTerminalNumber,IxTranType,,,,,IxSwipe,IxAmount,IxInvoice,,
	public AJBRequestResponseMessage formatAJBGiftCardReloadRequest(String accountNo, String amount) {
		reqMesgSize = 119;
		request = new AJBRequestResponseMessage(reqMesgSize);
		sequence = formatSequence(transactionNumber.getSequenceNumber()
				.intValue()) + Calendar.getInstance().get(Calendar.MILLISECOND);
		// IxCmd
		request.setValue(0, AJBMessageCodes.IX_CMD_REQUEST.getValue());
		// IxTimeout
		request.setValue(4, String.valueOf(getTimeOutInSeconds()));
		// IxDebitCredit
		request.setValue(5, AJBMessageCodes.IX_DEBIT_CREDIT_GIFTCARD.getValue());
		// IxStoreNumber
		request.setValue(7, txn.getStore().getId());
		// IxTerminalNumber
		request.setValue(8, currRegister.getId());
		// IxTranType
		request.setValue(9, AJBMessageCodes.IX_TRAN_TYPE_RELOAD.getValue());
		// IxAccount
		if (accountNo != null) {
			request.setValue(14, accountNo);
		}
		// IxAmount
		if(amount != null)
		request.setValue(15, formatCurrency(amount));
		// IvInvoice
		request.setValue(16, sequence);
		return request;

	}
	
	// Formatter for Credit Card Reversal/VoidRefund AJB request
		// Currently using following message format:
		// IxCmd,,,,IxTimeOut,IxDebitCredit,,IxStoreNumber,IxTerminalNumber,IxTranType,,,IxInvoice,,,IxAmount,IxMultiLayeredSTAN,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,
		public AJBRequestResponseMessage formatAJBGiftCardVoidRefundRequest(
				String ajbSequence, String id) {
			AJBRequestResponseMessage request = new AJBRequestResponseMessage();
			sequence = formatSequence(transactionNumber.getSequenceNumber()
					.intValue()) + Calendar.getInstance().get(Calendar.MILLISECOND);;
			request.setValue(0, AJBMessageCodes.IX_CMD_REQUEST.getValue());
			request.setValue(4, getTimeOutInSeconds());
			request.setValue(5, AJBMessageCodes.IX_DEBIT_CREDIT_GIFTCARD.getValue());
			request.setValue(7, store.getId());
			request.setValue(8, currRegister.getId());
			request.setValue(9, AJBMessageCodes.IX_GIFT_CARD_VOIDREFUND.getValue());
			//request.setValue(12, orgTxnNo.replace("*", ""));
			//Vivek Mishra : Setting unique AJB Sequence instead of invoice number in order to make every request unique
			//request.setValue(12, ajbSequence);
			// IxAccount
			if(id!=null)
				request.setValue(12, id);
			if (ajbSequence != null) {
				request.setValue(79, ajbSequence);
			}
			//Ends
			//request.setValue(15, formatCurrency(amount));
			request.setValue(16, sequence);

			return request;
		}
		
		//Start: Added by Himani for redeemable buyback fipay integration
		public AJBRequestResponseMessage formatAJBRedeemableCashoutRequest(
				String Id, String amount) {
			reqMesgSize = 119;
			request = new AJBRequestResponseMessage(reqMesgSize);
			sequence = formatSequence(transactionNumber.getSequenceNumber()
					.intValue()) + Calendar.getInstance().get(Calendar.MILLISECOND);
			// IxCmd
			request.setValue(0, AJBMessageCodes.IX_CMD_REQUEST.getValue());
			// IxTimeout
			request.setValue(4, String.valueOf(getTimeOutInSeconds()));
			// IxDebitCredit
			request.setValue(5, AJBMessageCodes.IX_DEBIT_CREDIT_GIFTCARD.getValue());
			// IxStoreNumber
			if(txn!=null)
				request.setValue(7, txn.getStore().getId());
			else
				request.setValue(7, store.getId());
			// IxTerminalNumber
			request.setValue(8, currRegister.getId());
			// IxTranType
			request.setValue(9, AJBMessageCodes.IX_TRAN_TYPE_CASHOUT.getValue());
			// IxAccount
			if (Id != null) {
				request.setValue(14, Id);
				
			}
	
			request.setValue(16, sequence);
			return request;

		}
		//End: Added by Himani for redeemable buyback fipay integration
	
		//Start: Added by Himani for GC Transaction History on 03-NOV-2016
		public AJBRequestResponseMessage formatAJBGiftCardTransactionHistoryRequest(String accountNo, String trackData) {

			reqMesgSize = 119;
			request = new AJBRequestResponseMessage(reqMesgSize);
			sequence = formatSequence(transactionNumber.getSequenceNumber()
					.intValue()) + Calendar.getInstance().get(Calendar.MILLISECOND);
			// IxCmd
			request.setValue(0, AJBMessageCodes.IX_CMD_REQUEST.getValue());
			// IxTimeout
			request.setValue(4, String.valueOf(getTimeOutInSeconds()));
			// IxDebitCredit
			request.setValue(5, AJBMessageCodes.IX_DEBIT_CREDIT_GIFTCARD.getValue());
			// IxStoreNumber
			if(txn!=null)
				request.setValue(7, txn.getStore().getId());
			else
				request.setValue(7, store.getId());
			// IxTerminalNumber
			request.setValue(8, currRegister.getId());
			// IxTranType
			request.setValue(9,
					AJBMessageCodes.IX_TRAN_TYPE_TRANSACTION_HISTORY.getValue());

			if (accountNo!= null) {
				// IxAccount
				request.setValue(14, accountNo);
			}

			/*if (trackData != null) {
				// IxSwipe
				request.setValue(14, trackData);
			}*/
			// IxInvoice
			request.setValue(16, sequence);
			
			/*if(isManualEntry){
			//IxOptions
			request.setValue(20, AJBMessageCodes.IX_ENTRY_METHOD_MANUAL.getValue());
			}*/
			return request;

		}
		//End: Added by Himani for GC Transaction History on 03-NOV-2016


	// Provides timeout's value
	public int getTimeOutInSeconds() {

		try {
			AJBServiceManager current = AJBServiceManager.getCurrent();
			TIME_OUT_SECONDS = current.getAjbResTimeOutSec();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			// if exception set to default
			TIME_OUT_SECONDS = 120;
		}

		return TIME_OUT_SECONDS;
	}

	// Formats amount by removing the decimal point as per AJB's standard.
	public String formatCurrency(String price) {
		String priceSplit = price.replace(".", "");
		// Vivek Mishra : Added for removing "," from tender amount
		priceSplit = priceSplit.replace(",", "");
		// Ends
		return priceSplit;

	}

	// Generates sequence number by adding store id and register id and removing
	// '*' if any.
	public String formatSequence(int seqNum) {
		// Vivek Mishra : Added store id and register id with the sequence as
		// asked by Jason on 31-JUL-15.
		// String stringSeqNum = String.valueOf(seqNum);
		// String stringSeqNum = txn.getStore().getId() + currRegister.getId()
		String stringSeqNum = currRegister.getStoreId() + currRegister.getId()
				+ String.valueOf(seqNum);
		return stringSeqNum.replace("*", "");
	}

	private String createYYMMDate(Date calenderDate) {
		SimpleDateFormat fmt = new SimpleDateFormat("yyMM");
		String strDate = fmt.format(calenderDate);
		return strDate;
	}

	// Vivek Mishra : Added to fix the duplicate sequence issue.
	public static int getSeqPostFix() {
		int temp = 0;
		if (seqPostfix == 99) {
			seqPostfix = 10;
		}
		temp = seqPostfix;
		seqPostfix++;
		return temp;
	}
	// Ends

}
