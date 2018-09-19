package com.chelseasystems.cs.pos;

import com.chelseasystems.cr.business.BusinessObject;
import com.chelseasystems.cr.currency.ArmCurrency;
import java.util.Date;


public class DocumentResponse extends BusinessObject {
  int respStatusCode = 0;
  String taxFreeCode = null;
  ArmCurrency refundAmount = null;
  String docNum = null;
  Date processDate = null;
  public static final int RESPONSE_SUCCESS = 1;
  public static final int RESPONSE_NOT_APPROVED = 2;
  public static final int RESPONSE_REFERRAL = 3;
  public static final int RESPONSE_TIMEOUT = 4;
  public static final int RESPONSE_SYSTEM_ERROR = 5;
  public DocumentResponse() {
    respStatusCode = 0;
    taxFreeCode = null;
    refundAmount = null;
    docNum = null;
    processDate = null;
    respStatusCode = 2;
    taxFreeCode = null;
    refundAmount = new ArmCurrency(0.0D);
    docNum = null;
    processDate = new Date();
  }

  public String getDocNum() {
    return docNum;
  }

  public void setDocNum(String docNum) {
    this.docNum = docNum;
  }

  public void setDocNum(int docNum) {
    this.docNum = String.valueOf(docNum);
  }

  public Date getProcessDate() {
    return processDate;
  }

  public void setProcessDate(Date processDate) {
    this.processDate = processDate;
  }

  public ArmCurrency getRefundAmount() {
    return refundAmount;
  }

  public void setRefundAmount(ArmCurrency refundAmount) {
    this.refundAmount = refundAmount;
  }

  public int getRespStatusCode() {
    return respStatusCode;
  }

  public void setRespStatusCode(int respStatusCode) {
    this.respStatusCode = respStatusCode;
  }

  public String getTaxFreeCode() {
    return taxFreeCode;
  }

  public void setTaxFreeCode(String taxFreeCode) {
    this.taxFreeCode = taxFreeCode;
  }


}
