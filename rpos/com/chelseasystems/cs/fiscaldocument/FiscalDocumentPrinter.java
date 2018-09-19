package com.chelseasystems.cs.fiscaldocument;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OptionalDataException;
import java.util.ArrayList;
import java.util.Date;

import com.armani.business.rules.ARMDocBusinessRule;
import com.armani.reports.ArmReceiptDocManager;
import com.armani.reports.FiscalDocumentEvent;
import com.armani.utils.ArmConfig;
import com.chelseasystems.cr.pos.PaymentTransactionAppModel;
import com.chelseasystems.cr.register.LightPoleDisplay;
import com.chelseasystems.cs.customer.CreditHistory;
import com.chelseasystems.cs.customer.DepositHistory;
import com.chelseasystems.cs.eod.CMSEODReport;
import com.chelseasystems.cs.eod.CMSTransactionEOD;
import com.chelseasystems.cs.pos.CMSPaymentTransactionAppModel;
import com.chelseasystems.cs.readings.CMSTrialBalance;
import com.chelseasystems.cs.swing.model.GiftReceiptEntry;
import com.chelseasystems.cs.util.FiscalDocumentUtil;

/**
 */
public class FiscalDocumentPrinter implements FiscalInterface, FiscalDocumentEvent {
	private ArmReceiptDocManager doc = null;
	private FiscalDocumentEvent fiscalDocumentEv = null;
	private FiscalDocumentUtil fiscalUtil;
	int modeType = -1;
	private FiscalDocumentResponse respFisc = null;

	/**
	 */
	public FiscalDocumentPrinter() {
		fiscalUtil = new FiscalDocumentUtil();
	}

	/**
	 * @param c
	 */
	private void createDocumenResponseObj(int c) {
		respFisc = new FiscalDocumentResponse();
		respFisc.setResponseStatusCode(c);
		//    respFisc.setError_message("Network Error or Master Cash Register broken");
		fireResult(false, respFisc);
	}

	/**
	 * @param i
	 * @param string
	 */
	private void createDocumenResponseObj(int i, String string) {
		respFisc = new FiscalDocumentResponse();
		respFisc.setResponseStatusCode(i);
		respFisc.setError_message(string);
		//   respFisc.setError_message("Network Error or Master Cash Register broken");
		fireResult(false, respFisc);
	}

	/* (non Javadoc)
	 * @see com.armani.reports.FiscalDocumentEvent#Fireresult(boolean, com.chelseasystems.cs.fiscalDocument.FiscalDocumentResponse)
	 */
	public void fireResult(boolean result, FiscalDocumentResponse doc) {
		fiscalDocumentEv.fireResult(result, doc);
	}

	/* (non Javadoc)
	 * @see com.chelseasystems.cs.fiscalDocument.FiscalInterface#getDocumentResponse()
	 */
	public FiscalDocumentResponse getDocumentResponse() {
		return respFisc;
	}

	/* (non Javadoc)
	 * @see com.chelseasystems.cs.fiscaldocument.FiscalInterface#getFiscalReceiptDate()
	 */
	public Date getFiscalReceiptDate() {
		doc = new ArmReceiptDocManager();
		Date retDate = doc.getFiscDate();
		return retDate;
	}

	/**
	 * @return
	 */
	public int getModeType() {
		return modeType;
	}

	/**
	 * @return
	 */
	public String getNextFiscalReceiptNo(boolean askToprinter) {
		doc = new ArmReceiptDocManager();
		int i=0;
		try {
		     i = doc.getFiscNum(askToprinter);
		 
			}
		catch (Exception e)
		{
		    return "0";
		}
		
		return new Integer(i).toString();
	}
	
	
	public String getNextFiscalReceiptNo() {
		doc = new ArmReceiptDocManager();
		int i=0;
		try {
		     i = doc.getFiscNum();
			}
		catch (Exception e)
		{
		    return "0";
		}
		
		return new Integer(i).toString();
	}

	/* (non Javadoc)
	 * @see com.chelseasystems.cs.fiscalDocument.FiscalInterface#getNextNOFiscalReceiptNo()
	 */
	public String getNextNOFiscalReceiptNo() {
		doc = new ArmReceiptDocManager();
		int i = doc.getNotFiscNum();
		if (i == 0){
			i = 1;
		}
		return new Integer(i).toString();
	}

	/**
	 * @param file
	 * @return
	 */
	public Object getObjectTransaction(String file) {
		try {
			FileInputStream in = new FileInputStream(file);
			ObjectInputStream s = new ObjectInputStream(in);
			return (Object) s.readObject();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (OptionalDataException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}

	/* (non Javadoc)
	 * @see com.chelseasystems.cs.fiscaldocument.FiscalInterface#openDrawer()
	 */
	public boolean openDrawer() {
		doc = new ArmReceiptDocManager();
		doc.openDrawer();
		return true;
	}

	/* (non Javadoc)
	 * @see com.chelseasystems.cs.fiscaldocument.FiscalInterface#printCustomerReceipt(com.chelseasystems.cr.pos.PaymentTransactionAppModel)
	 */
	public int printCustomerReceipt(PaymentTransactionAppModel payTxnAppModel) {
		int ret;
		doc = new ArmReceiptDocManager();
		doc.setObjectTransaction(payTxnAppModel.getPaymentTransaction());
		ret = doc.printCustomerReceipt();
		respFisc = doc.getDocumentResponse();
		return ret;
	}

	/* (non Javadoc)
	 * @see com.chelseasystems.cs.fiscaldocument.FiscalInterface#printCustomerStatement(com.chelseasystems.cs.customer.CreditHistory[])
	 */
	public int printCustomerStatement(CreditHistory[] creditHistory) {
		int ret = 0;
		doc = new ArmReceiptDocManager();
		doc.setObjectTransaction(creditHistory);
		ret = doc.printCustomerStatement();
		//ret = doc.doDocumentPrintout();
		respFisc = doc.getDocumentResponse();
		System.out.println("printCustomerStatement");
		return ret;
	}

	/* (non Javadoc)
	 * @see com.chelseasystems.cs.fiscaldocument.FiscalInterface#printCustomerStatement(com.chelseasystems.cr.pos.PaymentTransactionAppModel)
	 */
	public int printCustomerStatement(DepositHistory[] depositHistory) {
		int ret = 0;
		doc = new ArmReceiptDocManager();
		doc.setObjectTransaction(depositHistory);
		ret = doc.printCustomerStatement();
		respFisc = doc.getDocumentResponse();
		System.out.println("printCustomerStatement");
		return ret;
	}

	/**
	 * @param ev
	 * @param fiscalDocument
	 * @param taxFreeList
	 * @return
	 */
	public int printFiscalDocument(FiscalDocumentEvent ev, FiscalDocument fiscalDocument, ArrayList taxFreeList) {
		int ret = 0;
		String documentNumber = null;
		
		fiscalDocumentEv = ev;
		
		//Implements Armani Business rule
		ARMDocBusinessRule rule = new ARMDocBusinessRule();
		if (!rule.isPossible(fiscalDocument, taxFreeList, getModeType())) {			
			createDocumenResponseObj(FiscalDocumentResponse.RESPONSE_NOT_ALLOWED, "Not possible. " + rule.getExplicitText());
			ret = -1;
			return ret;
		}
		//Test if already assigned a document same kind (useless?)
		documentNumber = rule.getDocNumber(fiscalDocument);

		doc = new ArmReceiptDocManager();
		if (documentNumber == null) {
			doc.setNew_document(true);
			fiscalDocument.setFiscalDate(new Date());
		}

		FiscalDocumentUtil util = new FiscalDocumentUtil();
		if (fiscalDocument.isDocNumAssigned()) {
			documentNumber = fiscalDocument.getDocumentNumber();
			util.setResponseCode(RESPONSE_SUCCESS);
		} else {
			fiscalDocument.setFiscalDate(new Date());
			if (fiscalDocument.isDDTDocument()) {
				documentNumber = util.getAvailableDDTNumber();
			} else if (fiscalDocument.isTaxFreeDocument() || fiscalDocument.isVatInvoiceDocument()) {
				documentNumber = util.getAvailableVATNumber();
			} else if (fiscalDocument.isCreditNoteDocument()) {
				documentNumber = util.getAvailableCreditNoteNumber();
			}
			fiscalDocument.doSetDocumentNumber(documentNumber);
		}
		//Now prints
		if (util.getResponseCode() == RESPONSE_SUCCESS) {
			if (doc.isNew_document()) {
				fiscalDocument.doSetDocumentNumber(documentNumber);
			}
			doc.setFiscEvent(this);
			doc.setObjectTransaction(fiscalDocument);
			doc.setObjParms(taxFreeList);
			ret = doc.doDocumentPrintout();
		} else {
			createDocumenResponseObj(util.getResponseCode());
		}
		return ret;
	}

	/**
	 * @param payTxnAppModel
	 * @return
	 */
	public int printFiscalReceipt(PaymentTransactionAppModel payTxnAppModel) {
		int ret;
		 
		doc = new ArmReceiptDocManager();
		doc.setObjectTransaction(payTxnAppModel.getPaymentTransaction());
		ret = doc.doDocumentPrintout();
		respFisc = doc.getDocumentResponse();
		Boolean isFiscal =  Boolean.valueOf(ArmConfig.getValue("FISCAL_TYPE"));
		if ( (! ArmConfig.hasValue("PRINTONLASER")&&(isFiscal.booleanValue() ))) {
		     LightPoleDisplay.getInstance().reOpen();
		 }
		return ret;
	}

	/* (non Javadoc)
	 * @see com.chelseasystems.cs.fiscaldocument.FiscalInterface#printGiftReceipt(com.chelseasystems.cr.pos.PaymentTransactionAppModel)
	 */
	public int printGiftReceipt(PaymentTransactionAppModel payTxnAppModel) {
		int ret;
		doc = new ArmReceiptDocManager();
		respFisc = doc.getDocumentResponse();
		//Alex: Strada 1 - extra params
		doc.setObjectTransaction(payTxnAppModel.getPaymentTransaction());
		GiftReceiptEntry[] gifts = ((CMSPaymentTransactionAppModel) payTxnAppModel).getSelectedGiftItems();
		if (gifts != null) {
			ArrayList gifted = new ArrayList(gifts.length);
			for (int i = 0; i < gifts.length; i++) {
				gifted.add(gifts[i]);
			}
			doc.setObjParms(gifted);
			ret = doc.printGiftReceipt();
			respFisc = doc.getDocumentResponse();
		} else {
			respFisc.setError_message("No items");
			respFisc.setResponseStatusCode(FiscalDocumentResponse.RESPONSE_NOT_ALLOWED);
			ret = -1;
		}
		return ret;
	}

	/* (non Javadoc)
	 * @see com.chelseasystems.cs.fiscaldocument.FiscalInterface#printXReport(com.chelseasystems.cs.eod.CMSEODReport)
	 */
	public int printXReport(CMSEODReport eodReport) {
		int ret;
		doc = new ArmReceiptDocManager();
		doc.setObjectTransaction(eodReport);
		ret = doc.printXReport();
		respFisc = doc.getDocumentResponse();
		return ret;
	}

	/**
	 * @deprecated
	 * @param eodReport
	 * @return
	 */
	public int printXReport(CMSTransactionEOD eodReport) {
		int ret;
		doc = new ArmReceiptDocManager();
		doc.setObjectTransaction(eodReport);
		ret = doc.printXReport();
		respFisc = doc.getDocumentResponse();
		return ret;
	}

	/**
	 * @deprecated
	 * @param eodReport
	 * @param trialBalance
	 * @return
	 */
	public int printZReport(CMSTransactionEOD eodReport, CMSTrialBalance trialBalance) {
		int ret;
		doc = new ArmReceiptDocManager();
		doc.setObjectTransaction(eodReport);
		ret = doc.printZReport(trialBalance);
		respFisc = doc.getDocumentResponse();
		return ret;
	}

	/**
	 * @param eodReport
	 * @param trialBalance
	 * @return
	 */
	public int printZReport(CMSTransactionEOD eodReport, CMSEODReport report) {
		int ret;
		doc = new ArmReceiptDocManager();
		doc.setObjectTransaction(eodReport);
		ret = doc.printZReport(report);
		respFisc = doc.getDocumentResponse();
		return ret;
	}

	/**
	 * @param obj
	 * @param file
	 */
	public void saveTransaction(Object obj, String file) {
		try {
			FileOutputStream in = new FileOutputStream(file);
			ObjectOutputStream s = new ObjectOutputStream(in);
			//obj = (Object) s.readObject();
			s.writeObject(obj);
			s.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (OptionalDataException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * @param response
	 */
	public void setDocumentResponse(FiscalDocumentResponse response) {
		respFisc = response;
	}

	/* (non Javadoc)
	 * @see com.chelseasystems.cs.fiscaldocument.FiscalInterface#setModeType(int)
	 */
	public void setModeType(int i) {
		modeType = i;
	}

	/**
	 * @param date
	 * @return
	 */
	public boolean setSystemAndFiscalDate(Date date) {
		doc = new ArmReceiptDocManager();
		doc.setSystemAndFiscalDate();
		return true;
	}
}
