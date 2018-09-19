/*
 * put your module comment here
 * formatted with JxBeauty (c) johann.langhofer@nextra.at
 */


package com.ga.cs.pos.datatranssferobject;

import java.util.HashMap;
import java.util.Iterator;
import com.chelseasystems.cr.currency.ArmCurrency;
import com.chelseasystems.cr.currency.CurrencyException;
import com.chelseasystems.cs.dataaccess.artsoracle.dao.ArtsConstants;
import com.ga.cs.swing.report.ReportConstants;


/**
 * put your documentation comment here
 */
public class ConsolidatedOverShortReportDataTransferObject implements java.io.Serializable {
  static final long serialVersionUID = -8598491700349115515L;
  protected String registerId;
  protected HashMap tenderTypeValueMap = new HashMap();

  /**
   * put your documentation comment here
   */
  public ConsolidatedOverShortReportDataTransferObject() {
  }

  /**
   * put your documentation comment here
   * @param co
   */
  public void add(ConsolidatedOverShortReportDataTransferObject co) {
    if (this.registerId == null) {
      this.registerId = co.getRegisterId();
    }
    Iterator iter = co.tenderTypeValueMap.keySet().iterator();
    while (iter.hasNext()) {
      String tenderType = (String)iter.next();
      addTenderTypeCurrencyValue(tenderType, (ArmCurrency)co.getTenderTypeValue(tenderType));
    }
  }

  /**
   * @return Returns the registerId.
   */
  public String getRegisterId() {
    return registerId;
  }

  /**
   * @param registerId The registerId to set.
   */
  public void setRegisterId(String registerId) {
    this.registerId = registerId;
  }

  /**
   * @return Returns the totalDollars.
   */
  public ArmCurrency getTotalDollars() {
    return (ArmCurrency)getTenderTypeValue(ReportConstants.PAYMENT_TYPE_DOLLARS);
  }

  /**
   * @param totalDollars The totalDollars to set.
   */
  public void setTotalDollars(ArmCurrency totalDollars) {
    setTendeerTypeValue(ReportConstants.PAYMENT_TYPE_DOLLARS, totalDollars);
  }

  /**
   * put your documentation comment here
   * @param amount
   */
  public void addTotalDollars(ArmCurrency amount) {
    addTenderTypeCurrencyValue(ReportConstants.PAYMENT_TYPE_DOLLARS, amount);
  }

  /**
   * @return Returns the totalTravChecks.
   */
  public ArmCurrency getTotalTravChecks() {
    return (ArmCurrency)getTenderTypeValue(ArtsConstants.PAYMENT_TYPE_TRAVELLERS_CHECK);
  }

  /**
   * @param totalTravChecks The totalTravChecks to set.
   */
  public void setTotalTravChecks(ArmCurrency totalTravChecks) {
    setTendeerTypeValue(ArtsConstants.PAYMENT_TYPE_TRAVELLERS_CHECK, totalTravChecks);
  }

  /**
   * put your documentation comment here
   * @param amount
   */
  public void addTotalTravChecks(ArmCurrency amount) {
    addTenderTypeCurrencyValue(ArtsConstants.PAYMENT_TYPE_TRAVELLERS_CHECK, amount);
  }

  /**
   * @return Returns the totalYen.
   */
  public ArmCurrency getTotalYen() {
    return (ArmCurrency)getTenderTypeValue(ReportConstants.PAYMENT_TYPE_YEN);
  }

  /**
   * @param totalYen The totalYen to set.
   */
  public void setTotalYen(ArmCurrency totalYen) {
    setTendeerTypeValue(ReportConstants.PAYMENT_TYPE_YEN, totalYen);
  }

  /**
   * put your documentation comment here
   * @param amount
   */
  public void addTotalYen(ArmCurrency amount) {
    addTenderTypeCurrencyValue(ReportConstants.PAYMENT_TYPE_YEN, amount);
  }

  /**
   * add value to the map for the given tender type
   * NOTE: does not store nulls
   * @param tenderType
   * @param value
   */
  public void setTendeerTypeValue(String tenderType, ArmCurrency value) {
    if (value != null) {
      tenderTypeValueMap.put(tenderType, value);
    }
  }

  /**
   * put your documentation comment here
   * @param tenderType
   * @return
   */
  public ArmCurrency getTenderTypeValue(String tenderType) {
    return (ArmCurrency)tenderTypeValueMap.get(tenderType);
  }

  /**
   * put your documentation comment here
   * @param tenderType
   * @param dollars
   * @param yen
   */
  public void addAmountByTenderType(String tenderType, ArmCurrency dollars, ArmCurrency yen) {
    ArmCurrency amount = (tenderType.equals(ReportConstants.PAYMENT_TYPE_YEN)
        || tenderType.equals(ReportConstants.PAYMENT_TYPE_JPY_CASH) ? yen : dollars);
    addTenderTypeCurrencyValue(tenderType, amount);
  }

  /**
   * put your documentation comment here
   * @param tenderType
   * @param amount
   */
  public void addTenderTypeCurrencyValue(String tenderType, ArmCurrency amount) {
    ArmCurrency c = (ArmCurrency)tenderTypeValueMap.get(tenderType);
    try {
      if (c != null) {
        tenderTypeValueMap.put(tenderType, c.add(amount));
      } else {
        tenderTypeValueMap.put(tenderType, amount);
      }
    } catch (CurrencyException e) {
      System.out.println(
          "ConsolidatedOverShortReportDataTransferObject.addTenderTypeCurrencyValue: "
          + " ArmCurrency exception: tenderType=" + tenderType + " amount=" + amount + " msg="
          + e.getMessage());
      e.printStackTrace();
    }
  }

  /**
   * put your documentation comment here
   * @return
   */
  public String toString() {
    return "registerID=[" + registerId + "];tenderTypeValueMap=[" + convertTenderTypeMapToString()
        + "]";
  }

  /**
   * put your documentation comment here
   * @return
   */
  public String convertTenderTypeMapToString() {
    String s = "";
    Iterator iter = tenderTypeValueMap.keySet().iterator();
    while (iter.hasNext()) {
      String tenderType = (String)iter.next();
      s += "[" + tenderType + "="
          + ((ArmCurrency)tenderTypeValueMap.get(tenderType)).formattedStringValue() + "]";
    }
    return s;
  }
}

