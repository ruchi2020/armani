/*
 * put your module comment here
 * formatted with JxBeauty (c) johann.langhofer@nextra.at
 */


package com.chelseasystems.cs.payment;


/**
 * put your documentation comment here
 */
public class CMSPaymentCode {
  public String paymentCode;
  public String paymentDesc;
  public String paymentType;

  /**
   * <p>Title: </p>
   * <p>Description: </p>
   * <p>Copyright: Copyright (c) 2005</p>
   * <p>Company: SkillNet Inc</p>
   * @author Sumit Krishnan
   * @version 1.0
   */
  public CMSPaymentCode(String paymentType, String paymentCode, String paymentDesc) {
    this.paymentCode = paymentCode;
    this.paymentDesc = paymentDesc;
    this.paymentType = paymentType;
  }

  /**
   * This method is used to get item's paymentCode
   * @return String
   */
  public String getPaymentCode() {
    return this.paymentCode;
  }

  /**
   * This method is used to get item's paymentCode
   * @return String
   */
  public String getPaymentDesc() {
    return this.paymentDesc;
  }

  /**
   * This method is used to get item's paymentCode
   * @return String
   */
  public String getPaymentType() {
    return this.paymentType;
  }
}
