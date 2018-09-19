/*
 History:
 +------+------------+-----------+-----------+----------------------------------------------------+
 | Ver# | Date       | By        | Defect #  | Description                                        |
 +------+------------+-----------+-----------+----------------------------------------------------+
 | 2    | 05-10-2005 | Vikram    | N/A       |POS_104665_TS_LoyaltyManagement_Rev0                |
 +------+------------+-----------+-----------+----------------------------------------------------+
 *
 * formatted with JxBeauty (c) johann.langhofer@nextra.at
 */


package com.chelseasystems.cs.loyalty;

import com.chelseasystems.cr.rules.*;
import java.util.Date;
import com.chelseasystems.cs.payment.CMSRedeemable;


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
public class RewardCard extends CMSRedeemable implements IRuleEngine {
  /**
   * Attributes
   */
  public static final String REWARD_CARD_TYPE = "RC";
  //protected String loyaltyNumber = "";
  protected Loyalty loyalty = new Loyalty();
  protected Date expDate = new Date();
  protected boolean status = true;
  protected String issuingStoreId;

  /**
   * put your documentation comment here
   */
  public RewardCard() {
    this(null);
    setType(REWARD_CARD_TYPE);
  }

  /**
   * put your documentation comment here
   * @param   String transactionPaymentName
   */
  public RewardCard(String transactionPaymentName) {
    super(transactionPaymentName);
    setType(REWARD_CARD_TYPE);
  }

  /**
   * Methods
   */
  public Loyalty getLoyalty() {
    return this.loyalty;
  }

  /**
   * put your documentation comment here
   * @param LoyaltyObj
   */
  public void doSetLoyalty(Loyalty LoyaltyObj) {
    this.loyalty = LoyaltyObj;
    this.doSetCustomerId(loyalty.getCustomer().getId());
    this.setFirstName(loyalty.getCustomer().getFirstName());
    this.setLastName(loyalty.getCustomer().getLastName());
    if (loyalty.getCustomer().getTelephone() != null)
      this.setPhoneNumber(loyalty.getCustomer().getTelephone().getTelephoneNumber());
  }

  /**
   * put your documentation comment here
   * @param LoyaltyObj
   */
  public void setLoyalty(Loyalty LoyaltyObj) {
    doSetLoyalty(LoyaltyObj);
  }

  /**
   * put your documentation comment here
   * @return
   */
  public boolean getStatus() {
    return this.status;
  }

  /**
   * put your documentation comment here
   * @param Status
   */
  public void doSetStatus(boolean Status) {
    this.status = Status;
  }

  /**
   * put your documentation comment here
   * @param Status
   */
  public void setStatus(boolean Status) {
    doSetStatus(Status);
  }

  /**
   * put your documentation comment here
   * @return
   */
  public Date getExpDate() {
    return this.expDate;
  }

  /**
   * put your documentation comment here
   * @param ExpDate
   */
  public void doSetExpDate(Date ExpDate) {
    this.expDate = ExpDate;
  }

  /**
   * put your documentation comment here
   * @param ExpDate
   */
  public void setExpDate(Date ExpDate) {
    doSetExpDate(ExpDate);
  }

  /**
   * put your documentation comment here
   * @return
   */
  public String getIssuingStoreId() {
    return issuingStoreId;
  }

  /**
   * put your documentation comment here
   * @param issuingStoreId
   */
  public void doSetIssuingStoreId(String issuingStoreId) {
    this.issuingStoreId = issuingStoreId;
  }

  /**
   * put your documentation comment here
   * @param issuingStoreId
   */
  public void setIssuingStoreId(String issuingStoreId) {
    doSetIssuingStoreId(issuingStoreId);
  }

  /**
   * put your documentation comment here
   * @param id
   */
  public void setId(String id) {
    super.setId(id);
    doSetControlNum(id);
  }
}

