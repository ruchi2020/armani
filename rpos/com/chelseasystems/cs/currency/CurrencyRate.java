/**
 * History (tab separated):-
 * Vers	Date		By	Spec		                Description
 * 1	1/24/05	        KS	POS_IS_CurrencyRate_Rev1ArmCurrency Rate Object
 *
 *
 * formatted with JxBeauty (c) johann.langhofer@nextra.at
 */


package com.chelseasystems.cs.currency;

import com.chelseasystems.cr.business.BusinessObject;
import com.chelseasystems.cr.rules.BusinessRuleException;
import java.util.Date;


/**
 * <p>Title: CurrencyRate</p>
 *
 * <p>Description: This store the information of currency conversion</p>
 *
 * <p>Copyright: Copyright (c) 2005</p>
 *
 * <p>Company: Skillnet Inc</p>
 * @author not attributable
 * @version 1.0
 */
public class CurrencyRate extends BusinessObject {
  static final long serialVersionUID = -6298795358167955804L;
  private String fromCurrency = "";
  private String toCurrency = "";
  private Double conversionRate = new Double(0.00);
  private Date lastUpdateDate = new Date();
  private String tenderCode = "";

  /**
   * This method is used to get the currency conversion rate
   * @return Double
   */
  public Double getConversionRate() {
    return conversionRate;
  }

  /**
   * This method is used to get From To ArmCurrency for exchange rate
   * @return String
   */
  public String getFromCurrency() {
    return fromCurrency;
  }

  /**
   * This method is used to get the To ArmCurrency for exchange rate
   * @return String
   */
  public String getToCurrency() {
    return toCurrency;
  }

  /**
   * This method is used to get the update date
   * @return Date
   */
  public Date getUpdateDate() {
    return lastUpdateDate;
  }

  /**
   * This method is used to set the conversion rate
   * @param conversionRate Double
   * @throws BusinessRuleException
   */
  public void setConversionRate(Double conversionRate)
      throws BusinessRuleException {
    this.checkForNullParameter("setConversionRate", conversionRate);
    if (!this.conversionRate.equals(conversionRate)) {
      this.executeRule("setConversionRate", conversionRate);
      this.doSetConversionRate(conversionRate);
    }
  }

  /**
   * This method is used to set the From ArmCurrency for exchange rate
   * @param fromCurrency String
   * @throws BusinessRuleException
   */
  public void setFromCurrency(String fromCurrency)
      throws BusinessRuleException {
    this.checkForNullParameter("setFromCurrency", fromCurrency);
    if (!this.fromCurrency.equals(fromCurrency)) {
      this.executeRule("setFromCurrency", fromCurrency);
      this.doSetFromCurrency(fromCurrency);
    }
  }

  /**
   * This method is used to set the To ArmCurrency for exchange rate
   * @param toCurrency String
   * @throws BusinessRuleException
   */
  public void setToCurrency(String toCurrency)
      throws BusinessRuleException {
    this.checkForNullParameter("setToCurrency", toCurrency);
    if (!this.toCurrency.equals(toCurrency)) {
      this.executeRule("setToCurrency", toCurrency);
      this.doSetToCurrency(toCurrency);
    }
  }

  /**
   * This method is used to set updated data
   * @param lastUpdateDate Date
   * @throws BusinessRuleException
   */
  public void setUpdateDate(Date lastUpdateDate)
      throws BusinessRuleException {
    this.doSetUpdateDate(lastUpdateDate);
  }

  /**
   * This method is used to set the conversion rate
   * @param conversionRate Double
   */
  public void doSetConversionRate(Double conversionRate) {
    this.conversionRate = conversionRate;
  }

  /**
   * This method is used to set the From ArmCurrency for exchange rate
   * @param fromCurrency String
   */
  public void doSetFromCurrency(String fromCurrency) {
    this.fromCurrency = fromCurrency;
  }

  /**
   * This method is used to set the To ArmCurrency for exchange rate
   * @param toCurrency String
   */
  public void doSetToCurrency(String toCurrency) {
    this.toCurrency = toCurrency;
  }

  /**
   * This method is used to set updated data
   * @param lastUpdateDate Date
   */
  public void doSetUpdateDate(Date lastUpdateDate) {
    this.lastUpdateDate = lastUpdateDate;
  }

  public void setTenderCode (String tenderCode) {
    this.tenderCode = tenderCode;
  }

  public String getTenderCode() {
    return tenderCode;
  }
}
