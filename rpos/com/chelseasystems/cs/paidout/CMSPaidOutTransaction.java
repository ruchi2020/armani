/*
 History:
 +------+------------+-----------+-----------+----------------------------------------------------+
 | Ver# | Date       | By        | Defect #  | Description                                        |
 +------+------------+-----------+-----------+----------------------------------------------------+
 |1     |            | Base      |           |  From US                                           |
 --------------------------------------------------------------------------------------------------
 |2     | 08/11/2005 | KS        | N/A       |  Added FIscal Receipt Number ()'s                  |
 --------------------------------------------------------------------------------------------------
 *
 * formatted with JxBeauty (c) johann.langhofer@nextra.at
 */


package com.chelseasystems.cs.paidout;

import com.chelseasystems.cr.paidout.PaidOutTransaction;
import com.chelseasystems.cr.store.Store;
import com.chelseasystems.cr.pos.TransactionPOSServices;
import com.chelseasystems.cs.pos.ASISTxnData;
import java.util.Date;
import com.chelseasystems.cr.rules.BusinessRuleException;


/**
 * <p>Title: </p>
 *
 * <p>Description: </p>
 *
 * <p>Copyright: Copyright (c) 2005</p>
 *
 * <p>Company: </p>
 *
 * @author not attributable
 * @version 1.0
 */
public class CMSPaidOutTransaction extends PaidOutTransaction {
  private ASISTxnData asisTxnData = null;
  private String sFiscalReceiptNumber = null;
  private Date dtFiscalReceiptDate = null;

  /**
   * put your documentation comment here
   * @param   String aType
   * @param   Store aStore
   */
  public CMSPaidOutTransaction(String aType, Store aStore) {
    super(aType, aStore);
  }

  /**
   * put your documentation comment here
   * @return
   * @exception java.lang.Exception
   */
  public boolean post()
      throws java.lang.Exception {
    return TransactionPOSServices.getCurrent().submit(this);
  }

  /**
   *
   * @param ASIS
   */
  public void doSetASISTxnData(ASISTxnData asisData) {
    this.asisTxnData = asisData;
  }

  /**
   *
   * @param ASIS
   */
  public void setASISTxnData(ASISTxnData asisData)
      throws BusinessRuleException {
    executeRule("setASISTxnData", asisData);
    doSetASISTxnData(asisData);
  }

  /**
   *
   * @return ASIS
   */
  public ASISTxnData getASISTxnData() {
    return this.asisTxnData;
  }

  /**
   *
   * @param sValue String
   */
  public void setFiscalReceiptNumber(String sValue) {
    doSetFiscalReceiptNumber(sValue);
  }

  /**
   *
   * @param sValue String
   */
  public void doSetFiscalReceiptNumber(String sValue) {
    if (sValue == null || sValue.length() < 1)
      return;
    sFiscalReceiptNumber = sValue;
  }

  /**
   *
   * @return String
   */
  public String getFiscalReceiptNumber() {
    return sFiscalReceiptNumber;
  }

  /**
   *
   * @param dtReciept Date
   */
  public void setFiscalReceiptDate(Date dtReciept) {
    doSetFiscalReceiptDate(dtReciept);
  }

  /**
   *
   * @param dtReciept Date
   */
  public void doSetFiscalReceiptDate(Date dtReciept) {
    if (dtReciept == null)
      return;
    dtFiscalReceiptDate = dtReciept;
  }

  /**
   *
   * @return Date
   */
  public Date getFiscalReceiptDate() {
    return dtFiscalReceiptDate;
  }
}

