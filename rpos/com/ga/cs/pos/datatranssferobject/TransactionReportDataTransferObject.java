/*
 * put your module comment here
 * formatted with JxBeauty (c) johann.langhofer@nextra.at
 */


package com.ga.cs.pos.datatranssferobject;

import java.util.Iterator;
import java.util.List;
import com.chelseasystems.cr.currency.ArmCurrency;
import com.chelseasystems.cr.currency.CurrencyException;
import com.ga.cs.currency.ComparableCurrency;


/**
 * Data transfer object from TransactionReportBean
 * @author Administrator
 *
 */
public class TransactionReportDataTransferObject implements java.io.Serializable {
  static final long serialVersionUID = 5532793516180177563L;
  protected String registerId;
  protected int transactionCount;
  protected int unitsCount;
  protected double unitsPerTransaction;
  protected ArmCurrency totalSales = new ArmCurrency(0);
  protected ArmCurrency dollarsPerTransaction = new ArmCurrency(0);
  public static boolean DEBUG = true;

  /**
   * initalize
   */
  public TransactionReportDataTransferObject() {
  }

  /**
   * put your documentation comment here
   * @param   String registerId
   */
  public TransactionReportDataTransferObject(String registerId) {
    this.registerId = registerId;
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
   * @return Returns the totalSales as a ComparableCurrency.
   */
  public ComparableCurrency getTotalSales() {
    return new ComparableCurrency(totalSales.getCurrencyType(), totalSales.getDoubleValue()
        , totalSales);
  }

  /**
   * @param totalSales The totalSales to set.
   */
  public void setTotalSales(ArmCurrency totalSales) {
    this.totalSales = totalSales;
  }

  /**
   * put your documentation comment here
   * @param amount
   */
  public void addToTotalSales(ArmCurrency amount) {
    try {
      this.totalSales = this.totalSales.add(amount);
    } catch (CurrencyException e) {
      System.out.println("addToTotalSales: CurrencyException: amount="
          + amount.formattedStringValue() + " totalSales=" + totalSales.formattedStringValue()
          + " e=" + e + " msg=" + e.getMessage());
      e.printStackTrace();
    }
  }

  /**
   * @param totalSales
   */
  public void setTotalSales(List currencyAmounts) {
    Iterator iter = currencyAmounts.iterator();
    while (iter.hasNext()) {
      try {
        if (DEBUG) {
          System.out.println("setTotalSales: before: totalSales=" + totalSales.formattedStringValue());
        }
        this.totalSales = this.totalSales.add((ArmCurrency)iter.next());
        if (DEBUG) {
          System.out.println("setTotalSales: after:  totalSales=" + totalSales.formattedStringValue());
        }
      } catch (CurrencyException e) {
        System.out.println(
            "TransactionReportDataTransferObject.setTotalSales: CurrencyException: msg="
            + e.getMessage());
        e.printStackTrace();
      }
    }
  }

  /**
   * @return Returns the transactionCount.
   */
  public int getTransactionCount() {
    return transactionCount;
  }

  /**
   * @param transactionCount The transactionCount to set.
   */
  public void setTransactionCount(int transactionCount) {
    this.transactionCount = transactionCount;
  }

  /**
   * @return Returns the unitsCount.
   */
  public int getUnitsCount() {
    return unitsCount;
  }

  /**
   * @param unitsCount The unitsCount to set.
   */
  public void setUnitsCount(int unitsCount) {
    this.unitsCount = unitsCount;
  }

  /**
   * put your documentation comment here
   */
  public void setUnitsPerTransaction() {
    if (transactionCount != 0) {
      unitsPerTransaction = (double)unitsCount / (double)transactionCount;
    } else {
      unitsPerTransaction = 0.0;
    }
  }

  /**
   * put your documentation comment here
   */
  public void setDollarsPerTransaction() {
    if ((transactionCount != 0) && (totalSales != null)) {
      dollarsPerTransaction = totalSales.divide(transactionCount);
    } else {
      dollarsPerTransaction = new ArmCurrency(0);
    }
  }

  /**
   * @return Returns the dollarsPerTransaction as a ComparableCurrency.
   */
  public ComparableCurrency getDollarsPerTransaction() {
    return new ComparableCurrency(dollarsPerTransaction.getCurrencyType()
        , dollarsPerTransaction.getDoubleValue(), dollarsPerTransaction);
  }

  /**
   * @return Returns the unitsPerTransaction.
   */
  public Double getUnitsPerTransaction() {
    return new Double(unitsPerTransaction);
  }
}

