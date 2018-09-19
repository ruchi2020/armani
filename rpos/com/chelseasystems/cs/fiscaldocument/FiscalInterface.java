/*
 * put your module comment here
 * formatted with JxBeauty (c) johann.langhofer@nextra.at
 */


package com.chelseasystems.cs.fiscaldocument;

import com.chelseasystems.cr.pos.PaymentTransactionAppModel;
import java.util.ArrayList;
import com.chelseasystems.cs.eod.CMSEODReport;
import com.chelseasystems.cs.eod.CMSTransactionEOD;
import java.util.Date;
import com.armani.reports.FiscalDocumentEvent;
import com.chelseasystems.cs.readings.CMSTrialBalance;
import com.chelseasystems.cs.customer.CreditHistory;
import com.chelseasystems.cs.customer.DepositHistory;

public interface FiscalInterface {
  public static final int RESPONSE_PENDING = 0;
  public static final int RESPONSE_SUCCESS = 1;
  public static final int RESPONSE_TIMEOUT = 2;
  public static final int RESPONSE_SYSTEM_ERROR = 3;


  /**
   * put your documentation comment here
   * @param fiscalDocument
   * @param taxFreeList
   * @return
   */
  public int printFiscalDocument(FiscalDocumentEvent ev, FiscalDocument fiscalDocument
      , ArrayList taxFreeList);

  /**
   * put your documentation comment here
   * @param payTxnAppModel
   * @return
   */
  public int printFiscalReceipt(PaymentTransactionAppModel payTxnAppModel);

  /**
   * put your documentation comment here
   * @param payTxnAppModel
   * @return
   */
  public int printCustomerReceipt(PaymentTransactionAppModel payTxnAppModel);

  public int printCustomerStatement(DepositHistory[] depositHistory);

  public int printCustomerStatement(CreditHistory[] creditHistory);

  /**
   * put your documentation comment here
   * @param payTxnAppModel
   * @return
   */
  public int printGiftReceipt(PaymentTransactionAppModel payTxnAppModel);

  /**
   * put your documentation comment here
   * @return
   */
  public int printZReport(CMSTransactionEOD eodReport, CMSTrialBalance trialBalance);

  /**
   * @param eodTxn
   * @param report
   */
  public int printZReport(CMSTransactionEOD eodTxn, CMSEODReport report);


  /**
   * put your documentation comment here
   * @param eodReport
   * @return
   */
  public int printXReport(CMSEODReport eodReport);


  /**
   * put your documentation comment here
   * @return
   */
  public String getNextFiscalReceiptNo();
  public String getNextFiscalReceiptNo(boolean askToprinter);

  /**
   * put your documentation comment here
   * @return
   */
  public Date getFiscalReceiptDate();

  public boolean openDrawer();
  //Sergio
  public boolean setSystemAndFiscalDate(Date d);

  public FiscalDocumentResponse getDocumentResponse();
  public void setModeType(int i);


}


