/*
 * put your module comment here
 * formatted with JxBeauty (c) johann.langhofer@nextra.at
 */


package com.chelseasystems.cs.loyalty;

import com.chelseasystems.cr.business.BusinessObject;
import com.chelseasystems.cr.config.ConfigMgr;
import java.util.Date;
import com.chelseasystems.cs.customer.CMSCustomer;
import com.chelseasystems.cs.pos.CMSCompositePOSTransaction;
import com.chelseasystems.cr.rules.RuleEngine;
import com.chelseasystems.cr.rules.BusinessRuleException;


/**
 * <p>Title: Loyalty Card</p>
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
public class Loyalty extends BusinessObject {
  /**
   * Attributes
   */
  protected CMSCustomer cmsCustomer = new CMSCustomer();
  protected String loyaltyNumber = "";
  protected String storeType = "";
  protected Date issueDate = new Date();
  protected String issuedBy = "";
  protected double currBalance;
  protected double lifeTimeBalance;
  // true = Active, false = notActive
  protected boolean status = true;
  
  protected double currYearBalance;
  protected double lastYearBalance;

  protected Loyalty originalLoyalty;

  protected double premioRewardRatioMultiplier;


  /**
   * Constructor
   */
  public Loyalty() {
	  setPremioRewardRatioMultiplier(LoyaltyEngine.getPremioRewardRatioMultiplier());
  }

  /**
   * Methods
   */
  public CMSCustomer getCustomer() {
    return this.cmsCustomer;
  }

  /**
   *
   * @param customer CMSCustomer
   */
  public void doSetCustomer(CMSCustomer customer) {
    this.cmsCustomer = customer;
  }

  /**
   *
   * @param customer CMSCustomer
   */
  public void setCustomer(CMSCustomer customer) {
    doSetCustomer(customer);
  }

  /**
   *
   * @return String
   */
  public String getLoyaltyNumber() {
    return this.loyaltyNumber;
  }

  /**
   *
   * @param LoyaltyNumber String
   */
  public void doSetLoyaltyNumber(String LoyaltyNumber) {
    this.loyaltyNumber = LoyaltyNumber;
  }

  /**
   *
   * @param LoyaltyNumber String
   */
  public void setLoyaltyNumber(String LoyaltyNumber) {
    doSetLoyaltyNumber(LoyaltyNumber);
  }

  /**
   *
   * @return String
   */
  public String getStoreType() {
    return this.storeType;
  }

  /**
   *
   * @param StoreType String
   */
  public void doSetStoreType(String StoreType) {
    this.storeType = StoreType;
  }

  /**
   *
   * @param StoreType String
   */
  public void setStoreType(String StoreType) {
    doSetStoreType(StoreType);
  }

  /**
   * put your documentation comment here
   * @return
   */
  public String getIssuedBy() {
    return this.issuedBy;
  }

  /**
   *
   * @param IssuedBy String
   */
  public void doSetIssuedBy(String IssuedBy) {
    this.issuedBy = IssuedBy;
  }

  /**
   *
   * @param IssuedBy String
   */
  public void setIssuedBy(String IssuedBy) {
    doSetIssuedBy(IssuedBy);
  }

  /**
   *
   * @return boolean
   */
  public boolean getStatus() {
    return this.status;
  }

  /**
   *
   * @param Status boolean
   */
  public void doSetStatus(boolean Status) {
    this.status = Status;
  }

  /**
   *
   * @param Status boolean
   */
  public void setStatus(boolean Status) {
    doSetStatus(Status);
  }

  /**
   *
   * @return Date
   */
  public Date getIssueDate() {
    return this.issueDate;
  }

  /**
   *
   * @param IssueDate Date
   */
  public void doSetIssueDate(Date IssueDate) {
    this.issueDate = IssueDate;
  }

  /**
   *
   * @param IssueDate Date
   */
  public void setIssueDate(Date IssueDate) {
    doSetIssueDate(IssueDate);
  }

  /**
   *
   * @return double
   */
  public double getCurrBalance() {
	  if (isYearlyComputed()) {
	      return getLastYearBalance() + getCurrYearBalance();
	    }
    return this.currBalance;
  }

  /**
   *
   * @param CurrBalance double
   */
  public void doSetCurrBalance(double CurrBalance) {
    this.currBalance = CurrBalance;
  }

  /**
   *
   * @param CurrBalance double
   */
  public void setCurrBalance(double CurrBalance) {
    doSetCurrBalance(CurrBalance);
  }

  /**
   *
   * @return double
   */
  public double getLifeTimeBalance() {
    return this.lifeTimeBalance;
  }

  /**
   *
   * @param LifeTimeBalance double
   */
  public void doSetLifeTimeBalance(double LifeTimeBalance) {
    this.lifeTimeBalance = LifeTimeBalance;
  }

  /**
   *
   * @param LifeTimeBalance double
   */
  public void setLifeTimeBalance(double LifeTimeBalance) {
    doSetLifeTimeBalance(LifeTimeBalance);
  }

  /*
   * newCurrBlance should be passed as a negative value, if balance is to be deducted.
   */
  public void updateCurrBalance(double newCurrBalance) {
    if (isYearlyComputed()) {
      if (newCurrBalance < 0) {
        double lastYearBalance = getLastYearBalance();
        double currYearBalance = getCurrYearBalance();
        System.out.println("New Curr Balance==="+newCurrBalance+"last year=="+lastYearBalance+"currYear ===)"+currYearBalance);
        /*if (lastYearBalance + newCurrBalance >= 0) {
          this.setCurrYearBalance(getCurrYearBalance()
              - (getLastYearBalance() + newCurrBalance));
          this.setLastYearBalance(getLastYearBalance() + newCurrBalance);
        } else {
          this.setCurrYearBalance(getCurrYearBalance() + newCurrBalance);
//                   this.setCurrBalance(getCurrYearBalance() + newCurrBalance);
        }*/
        
        if(Math.abs(newCurrBalance)>lastYearBalance){
        	this.setLastYearBalance(0);
        	this.setCurrYearBalance(currYearBalance-(Math.abs(newCurrBalance)-lastYearBalance));
        }else{
        	this.setLastYearBalance(lastYearBalance+newCurrBalance);
        }
      } else
        setCurrYearBalance(getCurrYearBalance() + newCurrBalance);
    } else {
      setCurrBalance(getCurrBalance() + newCurrBalance);
    }
    setLifeTimeBalance(getLifeTimeBalance() + newCurrBalance);
  }

  
  /**
   * put your documentation comment here
   * @param reIssue
   * @param existingLoyalties
   * @exception BusinessRuleException
   */
  public void checkIfValidForEnroll(boolean reIssue, Loyalty[] existingLoyalties)
      throws BusinessRuleException {
    RuleEngine.execute(this.getClass().getName(), "isValidForEnroll", this
        , new Object[] {new Boolean(reIssue), existingLoyalties
    });
  }
  /**
  *
  * @return double
  */
 public double getCurrYearBalance() {
   return this.currYearBalance;
 }

 /**
  *
  * @param CurrBalance double
  */
 public void doSetCurrYearBalance(double CurrYearBalance) {
   this.currYearBalance = CurrYearBalance;
 }

 /**
  *
  * @param CurrBalance double
  */
 public void setCurrYearBalance(double CurrYearBalance) {
   doSetCurrYearBalance(CurrYearBalance);
 }

 /**
  *
  * @return double
  */
 public double getLastYearBalance() {
   return this.lastYearBalance;
 }

 /**
  *
  * @param CurrBalance double
  */
 public void doSetLastYearBalance(double LastYearBalance) {
   this.lastYearBalance = LastYearBalance;
 }

 /**
  *
  * @param CurrBalance double
  */
 public void setLastYearBalance(double LastYearBalance) {
   doSetLastYearBalance(LastYearBalance);
 }

 public boolean isYearlyComputed() {
   ConfigMgr loyaltyconfig = new ConfigMgr("loyalty.cfg");
   String isYearlyComputedStr = loyaltyconfig.getString("IS_YEARLY_COMPUTED");
   boolean yearlyComputed = false;
   if (isYearlyComputedStr != null) {
     yearlyComputed = Boolean.valueOf(isYearlyComputedStr).booleanValue();
   }
   return yearlyComputed;
 }


 public String toString() {
   StringBuffer loyaltySB = new StringBuffer();
   loyaltySB.append("LoyaltyNumber: ").append(" ").append(getLoyaltyNumber())
       .append(" | ").append(" StoreType: ").append(" ").append(getStoreType())
       .append(" | ").append(" Status: ").append(" ").append(getStatus())
       .append(" | ").append(" CurrentBalance: ").append(" ").append(getCurrBalance());

   return loyaltySB.toString();
 }

 public Loyalty getOriginalLoyalty() {
   return originalLoyalty;
 }

 public void setOriginalLoyalty(Loyalty originalLoyalty) {
   this.originalLoyalty = originalLoyalty;
 }

 public double getPremioRewardRatioMultiplier() {
   return premioRewardRatioMultiplier;
 }

 public void setPremioRewardRatioMultiplier(double premioRewardRatioMultiplier) {
   this.premioRewardRatioMultiplier = premioRewardRatioMultiplier;
 }
}

