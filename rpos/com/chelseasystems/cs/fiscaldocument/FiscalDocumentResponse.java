/*
 * put your module comment here
 * formatted with JxBeauty (c) johann.langhofer@nextra.at
 */


package com.chelseasystems.cs.fiscaldocument;

import java.util.Date;
import com.chelseasystems.cr.business.BusinessObject;
import com.chelseasystems.cr.currency.ArmCurrency;


/**
 * <p>Title: FiscalDocumentResponse</p>
 * <p>Description:FiscalDocumentResponse </p>
 * <p>Copyright: Copyright (c) 2005</p>
 * <p>Company: SkillNet Inc</p>
 * @author Manpreet S Bawa
 * @version 1.0
 */
/*
 History:
 +------+------------+-----------+-----------+----------------------------------------------------+
 | Ver# | Date       | By        | Defect #  | Description                                        |
 +------+------------+-----------+-----------+----------------------------------------------------+
 | 1    | 05-26-2005 | Manpreet  | N/A       | POS_104665_TS_FiscalDocuments_Rev0                 |
 +------+------------+-----------+-----------+----------------------------------------------------+
 */
public class FiscalDocumentResponse extends BusinessObject {
  /**
   * Document Number
   */
  private String sDocumentNum;
  /**
   * Refund Amount
   */
  private ArmCurrency amtRefund;
  /**
   * Tax Free code
   */
  private String sTaxFreeCode;
  /**
   * Process Date
   */
  private Date dtProcess;
  /**
   * Response Status Code
   */
  private int iResponseStatusCode;

  /**
   * Fiscal Document Date
   */
  public Date fiscalDate;

  /**
   * Pending
   */
  public static final int RESPONSE_PENDING = 0;
  /**
   * Success
   */
  public static final int RESPONSE_SUCCESS = 1;
  /**
   * System Error
   */
  public static final int RESPONSE_SYSTEM_ERROR = 2;
  /**
   * Time Out
   */
  public static final int RESPONSE_TIME_OUT = 3;
  /**
   * Not allowed
   */
  public static final int RESPONSE_NOT_ALLOWED = 4;
  /**
   * No document number
   */
  public static final int RESPONSE_NO_DOC_NUMBER = 5;

  /**
   * No document number
   */
  public static final int RESPONSE_NO_UPDATE = 6;

  //Sergio
  /**
     * No document number
     */
  public static final int RESPONSE_SUCCESS_REPRINT = 7;

  /**
   * Extended error message
   */
  private String error_message = null;

  /**
   * Default Constructor
   */
  public FiscalDocumentResponse() {
    sDocumentNum = new String();
    amtRefund = null;
    iResponseStatusCode = -1;
    dtProcess = null;
    sTaxFreeCode = new String();
  }

  /**
   * TaxFreeCode
   * @return String
   */
  public void setTaxFreeCode(String sValue) {
    if (sValue == null || sValue.length() < 1)
      return;
    doSetTaxFreeCode(sValue);
  }

  /**
   * TaxFreeCode
   * @param sValue String
   */
  public void doSetTaxFreeCode(String sValue) {
    this.sTaxFreeCode = sValue;
  }

  /**
   * TaxFreeCode
   * @return String
   */
  public String getTaxFreeCode() {
    return this.sTaxFreeCode;
  }

  /**
   * RefundAmount
   * @return Currency
   */
  public void setRefundAmount(ArmCurrency sValue) {
    if (sValue == null)
      return;
    doSetRefundAmount(sValue);
  }

  /**
   * RefundAmount
   * @param sValue Currency
   */
  public void doSetRefundAmount(ArmCurrency sValue) {
    this.amtRefund = sValue;
  }

  /**
   * RefundAmount
   * @return Currency
   */
  public ArmCurrency getRefundAmount() {
    return this.amtRefund;
  }

  /**
   * DocumentNumber
   * @return String
   */
  public void setDocumentNumber(String sValue) {
    if (sValue == null || sValue.length() < 1)
      return;
    doSetDocumentNumber(sValue);
  }

  /**
   * DocumentNumber
   * @param sValue String
   */
  public void doSetDocumentNumber(String sValue) {
    this.sDocumentNum = sValue;
  }

  /**
   * DocumentNumber
   * @return String
   */
  public String getDocumentNumber() {
    return sDocumentNum;
  }

  /**
   * ProcessDate
   * @param Date dtFiscal
   */
  public void setProcessDate(Date ProcessDate) {
    if (ProcessDate == null)
      return;
    doSetProcessDate(ProcessDate);
  }

  /**
   * ProcessDate
   * @param Date dtFiscal
   */
  public void doSetProcessDate(Date ProcessDate) {
    this.dtProcess = ProcessDate;
  }

  /**
   * ProcessDate
   * @return Date IssueDate
   */
  public Date getProcessDate() {
    return this.dtProcess;
  }

  /**
   * ResponseStatusCode
   * @param iRespCode int
   */
  public void setResponseStatusCode(int iRespCode) {
    //if (iRespCode < 0 || iRespCode > RESPONSE_TIME_OUT)
      //return;
    doSetResponseStatusCode(iRespCode);
  }

  /**
   * ResponseStatusCode
   * @param iRespCode int
   */
  public void doSetResponseStatusCode(int iRespCode) {
    this.iResponseStatusCode = iRespCode;
  }

  /**
   * ResponseStatusCode
   * @return int
   */
  public int getResponseStatusCode() {
    return iResponseStatusCode;
  }

  /**
   *
   * @param reponseDate Date
   */
  public void setFiscalDate(Date fiscalDate) {
    if (fiscalDate == null)
      return;
    doSetFiscalDate(fiscalDate);
  }

  /**
   * @return
   */
  public String getError_message() {
    if (error_message != null)
      return error_message;
    return this.getErrorMessage(this.iResponseStatusCode);
  }

  /**
   * @param string
   */
  public void setError_message(String string) {
    error_message = string;
  }

  /**
   *
   * @param FiscalDate Date
   */
  public void doSetFiscalDate(Date fiscalDate) {
    this.fiscalDate = fiscalDate;
  }

  /**
   *
   * @return Date
   */
  public Date getFiscalDate() {
    return this.fiscalDate;
  }

  public static String getErrorMessage(int iResponseCode) {
    String errorMessage = new String();
    switch (iResponseCode) {
      case FiscalDocumentResponse.RESPONSE_SUCCESS:
        errorMessage = "Document Printed Successfully";
        break;
      case FiscalDocumentResponse.RESPONSE_NOT_ALLOWED:
        errorMessage = "Cannot Allow Printing of the Fiscal Document";
        break;
      case FiscalDocumentResponse.RESPONSE_NO_DOC_NUMBER:
        errorMessage = "Unable to Retrieve Fiscal Document Number";
        break;
      case FiscalDocumentResponse.RESPONSE_NO_UPDATE:
        errorMessage = "The current value is greater than the new value of the Fiscal Document";
        break;
      case FiscalDocumentResponse.RESPONSE_SYSTEM_ERROR:
        errorMessage = "Network Error or Master Cash Register broken";
        break;
      case FiscalDocumentResponse.RESPONSE_TIME_OUT:
        errorMessage = "Time Out Occured";
        break;
      case FiscalDocumentResponse.RESPONSE_SUCCESS_REPRINT:
        errorMessage = "Document Reprinted Successfully";
      default:
        break;
    }
    return errorMessage;
  }
}


