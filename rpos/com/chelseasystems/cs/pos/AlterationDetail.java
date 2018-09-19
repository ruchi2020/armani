/*
 * put your module comment here
 * formatted with JxBeauty (c) johann.langhofer@nextra.at
 */


package com.chelseasystems.cs.pos;

import com.chelseasystems.cr.business.BusinessObject;
import com.chelseasystems.cr.currency.ArmCurrency;
import java.util.Date;


/**
 * <p>Title: Alteration Detail </p>
 * <p>Description: This class store the details of alteration</p>
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
 | 1    | 04-19-2005 | Manpreet  | N/A       | POS_104665_TS_Alterations_Rev2                     |
 +------+------------+-----------+-----------+----------------------------------------------------+
 | 2    | 04-21-2005 | Manpreet  | N/A       | Added Description Attribute                        |
 +------+------------+-----------+-----------+----------------------------------------------------+
 | 3    | 04-27-2005 | Manpreet  | N/A       | Modified EstimatedTime, ActualTime to Date type    |
 +------+------------+-----------+-----------+----------------------------------------------------+
 */
public class AlterationDetail extends BusinessObject {
  /**
   * Description
   */
  private String sDescription;
  /**
   * Transaction ID
   */
  private String sTransactionID;
  /**
   * Retail Transaction Line Item ID
   */
  private Long lLineItemID;
  /**
   * Detail Sequence Num
   */
  private Long lDetailSeqNum;
  /**
   * Alteration Sequence Num
   */
  private Long lAlteratonSeqNum;
  /**
   * Alteration Code
   */
  private String sAlterationCode;
  /**
   * Estimated Price
   */
  private ArmCurrency curEstimatedPrice;
  /**
   * Estimated Time
   */
  private Date dtEstimatedTime;
  /**
   * Actual Price
   */
  private ArmCurrency curActualPrice;
  /**
   * Actual Time
   */
  private Date dtActualTime;

  /**
   * Default Constructor
   */
  public AlterationDetail() {
    sDescription = new String("");
    sTransactionID = new String("");
    lLineItemID = new Long(0);
    lDetailSeqNum = new Long(0);
    lAlteratonSeqNum = new Long(0);
    sAlterationCode = "";
    dtEstimatedTime = null;
    curActualPrice = new ArmCurrency(0);
    dtActualTime = null;
    curEstimatedPrice = new ArmCurrency(0);
  }

  /**
   * This method used to get the description alteration
   * @return Description
   */
  public String getDescription() {
    return sDescription;
  }

  /**
   * This method set the description of alteration
   * @param sDescription Description
   */
  public void setDescription(String sDescription) {
    if (sDescription == null || sDescription.trim().length() < 1)
      return;
    doSetDescription(sDescription);
  }

  /**
   * This method set the description of alteration
   * @param sDescription Description
   */
  public void doSetDescription(String sDescription) {
    this.sDescription = sDescription;
  }

  /**
   * This method is used to get the alteration sequence number
   * @return lAlteratonSeqNum AlterationSequenceNumber
   */
  public Long getAlterationSeqNum() {
    return lAlteratonSeqNum;
  }

  /**
   * This method is used to get the detail sequence number
   * @return lDetailSeqNum DetailSequenceNumber
   */
  public Long getDetailSeqNum() {
    return lDetailSeqNum;
  }

  /**
   * This method is used to get the transaction Id of alteration
   * @return TransactionId
   */
  public String getTransactionID() {
    return sTransactionID;
  }

  /**
   * This method is used to get the line item id
   * @return LineITemID
   */
  public Long getLineItemID() {
    return lLineItemID;
  }

  /**
   * This method is used to get the actual price of an item
   * @return curActualPrice ActualPrice
   */
  public ArmCurrency getActualPrice() {
    return curActualPrice;
  }

  /**
   * This method is used to set the transaction Id of alteration
   * @param sTransactionID TransactionId
   */
  public void setTransactionID(String sTransactionID) {
    if (sTransactionID == null || sTransactionID.length() < 1)
      return;
    doSetTransactionID(sTransactionID);
  }

  /**
   * This method is used to set the transaction Id of alteration
   * @param sTransactionID TransactionId
   */
  public void doSetTransactionID(String sTransactionID) {
    this.sTransactionID = sTransactionID;
  }

  /**
   * This method is used to set the line item id
   * @param lLineItemID LineItemID
   */
  public void setLineItemID(Long lLineItemID) {
    if (lLineItemID == null || lLineItemID.longValue() < 0)
      return;
    doSetLineItemID(lLineItemID);
  }

  /**
   * This method is used to set the line item id
   * @param lLineItemID LineItemID
   */
  public void doSetLineItemID(Long lLineItemID) {
    this.lLineItemID = lLineItemID;
  }

  /**
   * This method is used to set the detail sequence number
   * @param lDetailSeqNum DetailSeqNum
   */
  public void setDetailSeqNum(Long lDetailSeqNum) {
    if (lDetailSeqNum == null || lDetailSeqNum.longValue() < 0)
      return;
    doSetDetailSeqNum(lDetailSeqNum);
  }

  /**
   * This method is used to set the detail sequence number
   * @param lDetailSeqNum DetailSeqNum
   */
  public void doSetDetailSeqNum(Long lDetailSeqNum) {
    this.lDetailSeqNum = lDetailSeqNum;
  }

  /**
   * This method is used to set the alteration sequence number
   * @param lAlteratonSeqNum AlteratonSeqNum
   */
  public void setAlteratonSeqNum(Long lAlteratonSeqNum) {
    if (lAlteratonSeqNum == null || lAlteratonSeqNum.longValue() < 0)
      return;
    doSetAlterationSeqNum(lAlteratonSeqNum);
  }

  /**
   * This method is used to set the alteration sequence number
   * @param sAlteratonSeqNum AlteratonSeqNum
   */
  public void doSetAlterationSeqNum(Long lAlteratonSeqNum) {
    this.lAlteratonSeqNum = lAlteratonSeqNum;
  }

  /**
   * This method is used to set the actual price of alteration
   * @param curActualPrice ActualPrice
   */
  public void setActualPrice(ArmCurrency curActualPrice) {
    if (curActualPrice == null)
      return;
    doSetActualPrice(curActualPrice);
  }

  /**
   * This method is used to set the actual price of alteration
   * @param curActualPrice ActualPrice
   */
  public void doSetActualPrice(ArmCurrency curActualPrice) {
    this.curActualPrice = curActualPrice;
  }

  /**
   * This method is used to get the estimated price of alteration
   * @return EstimatedPrice
   */
  public ArmCurrency getEstimatedPrice() {
    return curEstimatedPrice;
  }

  /**
   * This method is used to set the estimated price of alteration
   * @param curEstimatedPrice EstimatedPrice
   */
  public void setEstimatedPrice(ArmCurrency curEstimatedPrice) {
    if (curEstimatedPrice == null)
      return;
    doSetEstimatedPrice(curEstimatedPrice);
  }

  /**
   * This method is used to set the estimated price of alteration
   * @param curEstimatedPrice EstimatedPrice
   */
  public void doSetEstimatedPrice(ArmCurrency curEstimatedPrice) {
    this.curEstimatedPrice = curEstimatedPrice;
  }

  /**
   * This method is used to get the actual time require for alteration
   * @return ActualNumDays
   */
  public Date getActualTime() {
    return dtActualTime;
  }

  /**
   * This method is used to set the actual time require for alteration
   * @param iActualNumDays ActualNumDays
   */
  public void setActualTime(Date dtActualTime) {
    if (dtActualTime == null)
      return;
    doSetActualTime(dtActualTime);
  }

  /**
   * This method is used to set the actual time require for alteration
   * @param iActualNumDays ActualNumDays
   */
  public void doSetActualTime(Date dtActualTime) {
    this.dtActualTime = dtActualTime;
  }

  /**
   * This method is used to get the estimated time for alteration
   * @return EstimatedNumDays
   */
  public Date getEstimatedTime() {
    return dtEstimatedTime;
  }

  /**
   * This method is used to set the estimated time for alteration
   * @param iEstimatedNumDays EstimatedNumDays
   */
  public void setEstimatedTime(Date dtEstimatedTime) {
    if (dtEstimatedTime == null)
      return;
    doSetEstimatedTime(dtEstimatedTime);
  }

  /**
   * This method is used to set the estimated time for alteration
   * @param iEstimatedNumDays EstimatedNumDays
   */
  public void doSetEstimatedTime(Date dtEstimatedTime) {
    this.dtEstimatedTime = dtEstimatedTime;
  }

  /**
   * This method is used to get all alteration codes
   * @return AlterationCode
   */
  public String getAlterationCode() {
    return sAlterationCode;
  }

  /**
   * This method is used to set all alteration codes
   * @param sAlterationCode AlterationCode
   */
  public void setAlterationCode(String sAlterationCode) {
    if (sAlterationCode == null || sAlterationCode.length() < 1)
      return;
    doSetAlterationCode(sAlterationCode);
  }

  /**
   * This method is used to set all alteration codes
   * @param sAlterationCode AlterationCode
   */
  public void doSetAlterationCode(String sAlterationCode) {
    this.sAlterationCode = sAlterationCode;
  }
}

