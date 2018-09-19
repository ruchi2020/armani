/*
 * put your module comment here
 * formatted with JxBeauty (c) johann.langhofer@nextra.at
 */


package com.chelseasystems.cs.pos;

import com.chelseasystems.cr.business.BusinessObject;
import com.chelseasystems.cr.currency.ArmCurrency;
import java.util.Date;
import java.util.Vector;
import java.util.Enumeration;


/**
 * <p>Title: AlterationLineItemDetail </p>
 * <p>Description: This class store the line Item details for Alterations</p>
 * <p>Copyright: Copyright (c) 2005</p>
 * <p>Company: SkillNet Inc</p>
 * @author Manpreet S Bawa
 * @version 1.0
 */
/*
 History:
 +------+------------+-----------+-----------+------------------------------------------------------+
 | Ver# | Date       | By        | Defect #  | Description                                          |
 +------+------------+-----------+-----------+------------------------------------------------------+
 | 1    | 04-19-2005 | Manpreet  | N/A       | POS_104665_TS_Alterations_Rev2                       |
 +------+------------+-----------+-----------+------------------------------------------------------+
 | 2    | 04-27-2005 | Manpreet  | N/A       | Modified to add Comments,PriceOverride,Clear details |
 +------+------------+-----------+-----------+------------------------------------------------------+
 */
public class AlterationLineItemDetail extends BusinessObject {
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
   * Alteration ID
   */
  private String sAlterationTicketID;
  /**
   * Try Date
   */
  private Date dtTry;
  /**
   * Promise Date
   */
  private Date dtPromise;
  /**
   * Fitter ID
   */
  private String sFitterID;
  /**
   * Tailor ID
   */
  private String sTailorID;
  /**
   * Total Price
   */
  private ArmCurrency curTotalPrice;
  /**
   * Customer
   */
  private String sComments;
  /**
   * ALterationDetail Vector
   */
  private Vector vecAlterationDetail;
  /**
   * PriceOverride Flag
   */
  private boolean bPriceOverriden;

  /**
   * Default Constructor
   */
  public AlterationLineItemDetail() {
    sTransactionID = new String("");
    lLineItemID = new Long(0);
    lDetailSeqNum = new Long(0);
    lAlteratonSeqNum = new Long(0);
    sAlterationTicketID = new String("");
    dtTry = null;
    dtPromise = null;
    sFitterID = new String("");
    sTailorID = new String("");
    curTotalPrice = new ArmCurrency(0.0d);
    sComments = new String("");
    vecAlterationDetail = new Vector();
    bPriceOverriden = false;
  }

  /**
   * This method is used to check whether price over ridden done or not
   * @return boolean
   */
  public boolean isPriceOverriden() {
    return bPriceOverriden;
  }

  /**
   * This method is used to set boolean flag of price over ridden
   * @param bPriceOverride boolean
   */
  public void setPriceOverride(boolean bPriceOverride) {
    bPriceOverriden = bPriceOverride;
  }

  /**
   * This method is used to clear the line item details.
   */
  public void clearLineItemDetails() {
    sTransactionID = new String("");
    lLineItemID = new Long(0);
    lDetailSeqNum = new Long(0);
    lAlteratonSeqNum = new Long(0);
    sAlterationTicketID = new String("");
    dtTry = null;
    dtPromise = null;
    sFitterID = new String("");
    sTailorID = new String("");
    curTotalPrice = new ArmCurrency(0.0d);
    sComments = new String("");
    vecAlterationDetail = new Vector();
  }

  /**
   * This method is used to set the comments of alteration line item
   * @param sValue String
   */
  public void setComments(String sValue) {
    if (sValue == null || sValue.trim().length() < 1)
      return;
    doSetComments(sValue);
  }

  /**
   * This method is used to set the comments of alteration line item
   * @param sValue String
   */
  public void doSetComments(String sValue) {
    this.sComments = sValue;
  }

  /**
   * This method is used to get the comments of alteration line item
   * @return String
   */
  public String getComments() {
    return sComments;
  }

  /**
   * This method is used to add an alteration detail into list.
   * @param sAlterationCode AlterationCode
   * @param sEstimatedPrice EstimatedPrice
   * @param iEstimatedTime EstimatedNumberOfDays
   */
  public void addAlterationDetail(AlterationDetail alterationDetail) {
    if (alterationDetail == null)
      return;
    doAddAlterationDetail(alterationDetail);
  }

  /**
   * This method is used to add an alteration detail into list
   * @param sAlterationCode AlterationCode
   * @param sEstimatedPrice EstimatedPrice
   * @param iEstimatedTime EstimatedTime
   */
  public void doAddAlterationDetail(AlterationDetail alterationDetail) {
    //    if(vecAlterationDetail==null) vecAlterationDetail = new Vector();
    vecAlterationDetail.addElement(alterationDetail);
  }

  /**
   * This method is used to get alteration details array list
   * @return AlterationDetail[]
   */
  public AlterationDetail[] getAlterationDetailsArray() {
    return (AlterationDetail[])vecAlterationDetail.toArray(new AlterationDetail[vecAlterationDetail.
        size()]);
  }

  /**
   * This method is used to get the collection of alteration details
   * @return AlterationDetailList
   */
  public Enumeration getAlterationDetails() {
    return vecAlterationDetail.elements();
  }

  /**
   * This method is used to get the alteration sequence number
   * @return AlterationSequenceNumber
   */
  public Long getAlterationSeqNum() {
    return lAlteratonSeqNum;
  }

  /**
   * This method is used to get the detail sequence number
   * @return DetailSequenceNumber
   */
  public Long getDetailSeqNum() {
    return lDetailSeqNum;
  }

  /**
   * This method is used to get the transaction id
   * @return TransactionId
   */
  public String getTransactionID() {
    return sTransactionID;
  }

  /**
   * This method is used to get alteration line item id
   * @return LineITemID
   */
  public Long getLineItemID() {
    return lLineItemID;
  }

  /**
   * This method is used to set transaction id
   * @param sTransactionID TransactionId
   */
  public void setTransactionID(String sTransactionID) {
    if (sTransactionID == null || sTransactionID.length() < 1)
      return;
    doSetTransactionID(sTransactionID);
  }

  /**
   * This method is used to set transaction id
   * @param sTransactionID TransactionId
   */
  public void doSetTransactionID(String sTransactionID) {
    this.sTransactionID = sTransactionID;
  }

  /**
   * This method is used to set alteration line item id
   * @param sLineItemID LineItemID
   */
  public void setLineItemID(Long lLineItemID) {
    if (lLineItemID == null || lLineItemID.longValue() < 0)
      return;
    doSetLineItemID(lLineItemID);
  }

  /**
   * This method is used to set alteration line item id
   * @param sLineItemID LineItemID
   */
  public void doSetLineItemID(Long lLineItemID) {
    this.lLineItemID = lLineItemID;
  }

  /**
   * This method is used to set detail sequence number
   * @param sDetailSeqNum DetailSeqNum
   */
  public void setDetailSeqNum(Long lDetailSeqNum) {
    if (lDetailSeqNum == null || lDetailSeqNum.longValue() < 0)
      return;
    doSetDetailSeqNum(lDetailSeqNum);
  }

  /**
   * This method is used to set detail sequence number
   * @param sDetailSeqNum DetailSeqNum
   */
  public void doSetDetailSeqNum(Long lDetailSeqNum) {
    this.lDetailSeqNum = lDetailSeqNum;
  }

  /**
   * This method is used to set alteration sequence number
   * @param sAlteratonSeqNum AlteratonSeqNum
   */
  public void setAlteratonSeqNum(Long lAlteratonSeqNum) {
    if (lAlteratonSeqNum == null || lAlteratonSeqNum.longValue() < 0)
      return;
    doSetAlterationSeqNum(lAlteratonSeqNum);
  }

  /**
   * This method is used to set alteration sequence number
   * @param sAlteratonSeqNum AlteratonSeqNum
   */
  public void doSetAlterationSeqNum(Long lAlteratonSeqNum) {
    this.lAlteratonSeqNum = lAlteratonSeqNum;
  }

  /**
   * This method is used to get the total price of the transaction
   * @return TotalPrice
   */
  public ArmCurrency getTotalPrice() {
    return curTotalPrice;
  }

  /**
   * This method is used to set the total price of the transaction
   * @param curTotalPrice TotalPrice
   */
  public void setTotalPrice(ArmCurrency curTotalPrice) {
    if (curTotalPrice == null)
      return;
    doSetTotalPrice(curTotalPrice);
  }

  /**
   * This method is used to set the total price of the transaction
   * @param curTotalPrice TotalPrice
   */
  public void doSetTotalPrice(ArmCurrency curTotalPrice) {
    this.curTotalPrice = curTotalPrice;
  }

  /**
   * This method is used to get the promise date for alteration items
   * @return DatePromised
   */
  public Date getPromiseDate() {
    return dtPromise;
  }

  /**
   * This method is used to get the alteration ticket id
   * @return AlterationTicketID
   */
  public String getAlterationTicketID() {
    return sAlterationTicketID;
  }

  /**
   * This method is used to get the try date for alteration items
   * @return TryDate
   */
  public Date getTryDate() {
    return dtTry;
  }

  /**
   * This method is used to get the fitter id for the alteration
   * @return FitterID
   */
  public String getFitterID() {
    return sFitterID;
  }

  /**
   * This method is used to get the tailor id for the alteration
   * @return TailorID
   */
  public String getTailorID() {
    return sTailorID;
  }

  /**
   * This method is used to set the tailor id for the alteration
   * @param sTailorID TailorID
   */
  public void setTailorID(String sTailorID) {
    if (sTailorID == null || sTailorID.length() < 1)
      return;
    doSetTailorID(sTailorID);
  }

  /**
   * This method is used to set the tailor id for the alteration
   * @param sTailorID TailorID
   */
  public void doSetTailorID(String sTailorID) {
    this.sTailorID = sTailorID;
  }

  /**
   * This method is used to set the fitter id for the alteration
   * @param sFitterID FitterID
   */
  public void setFitterID(String sFitterID) {
    if (sFitterID == null || sFitterID.length() < 1)
      return;
    doSetFitterID(sFitterID);
  }

  /**
   * This method is used to set the fitter id for the alteration
   * @param sFitterID FitterID
   */
  public void doSetFitterID(String sFitterID) {
    this.sFitterID = sFitterID;
  }

  /**
   * This method is used to set the alteration ticket id
   * @param sAlterationTicketID AlterationTicketID
   */
  public void setAlterationTicketID(String sAlterationTicketID) {
    if (sAlterationTicketID == null || sAlterationTicketID.length() < 1) {
      return;
    }
    doSetAlterationTicketID(sAlterationTicketID);
  }

  /**
   * This method is used to set the alteration ticket id
   * @param sAlterationTicketID AlterationTicketID
   */
  public void doSetAlterationTicketID(String sAlterationTicketID) {
    this.sAlterationTicketID = sAlterationTicketID;
  }

  /**
   * This method is used to set the try date for alteration item
   * @param dtTry TryDate
   */
  public void setTryDate(Date dtTry) {
    if (dtTry == null)
      return;
    doSetTryDate(dtTry);
  }

  /**
   * This method is used to set the try date for alteration item
   * @param dtTry TryDate
   */
  public void doSetTryDate(Date dtTry) {
    this.dtTry = dtTry;
  }

  /**
   * This method is used to set the promise date for alteration item
   * @param dtPromise PromiseDate
   */
  public void setPromiseDate(Date dtPromise) {
    if (dtPromise == null)
      return;
    doSetPromiseDate(dtPromise);
  }

  /**
   * This method is used to set the promise date for alteration item
   * @param dtPromise PromiseDate
   */
  public void doSetPromiseDate(Date dtPromise) {
    this.dtPromise = dtPromise;
  }
}

