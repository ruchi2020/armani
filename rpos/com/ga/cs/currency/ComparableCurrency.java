/*
 * put your module comment here
 * formatted with JxBeauty (c) johann.langhofer@nextra.at
 */


package com.ga.cs.currency;

import com.chelseasystems.cr.currency.ArmCurrency;
import com.chelseasystems.cr.currency.CurrencyException;
import com.chelseasystems.cr.currency.CurrencyType;


/**
 * A thin wrapper around com.chelseasystems.cr.currency.ArmCurrency that implements Comparable and thius
 * enables sorting on report table columns containing currencies
 * @author fbulah
 *
 */
public class ComparableCurrency extends ArmCurrency implements Comparable {

  /**
   * @param arg0
   * @param arg1
   */
  public ComparableCurrency(CurrencyType arg0, double arg1) {
    super(arg0, arg1);
  }

  /**
   * @param arg0
   * @param arg1
   * @param arg2
   */
  public ComparableCurrency(CurrencyType arg0, double arg1, ArmCurrency arg2) {
    super(arg0, arg1, arg2);
  }

  /**
   * @param arg0
   * @param arg1
   */
  public ComparableCurrency(CurrencyType arg0, long arg1) {
    super(arg0, arg1);
  }

  /**
   * @param arg0
   * @param arg1
   * @param arg2
   */
  public ComparableCurrency(CurrencyType arg0, long arg1, ArmCurrency arg2) {
    super(arg0, arg1, arg2);
  }

  /**
   * @param arg0
   */
  public ComparableCurrency(ArmCurrency arg0) {
    super(arg0.doubleValue());
  }

  /**
   * @param arg0
   */
  public ComparableCurrency(double arg0) {
    super(arg0);
  }

  /**
   * @param arg0
   */
  public ComparableCurrency(String arg0) {
    super(arg0);
  }

  /* (non-Javadoc)
   * @see java.lang.Comparable#compareTo(java.lang.Object)
   */
  public int compareTo(Object o)
      throws ClassCastException {
    if (!(o instanceof ArmCurrency)) {
      throw new ClassCastException("Object o=" + o + " is not a Currency");
    }
    ArmCurrency c = (ArmCurrency)o;
    try {
      if (this.lessThan(c)) {
        return -1;
      }
    } catch (CurrencyException e) {
      System.out.println("compareTo: CurrencyException: lessThan: msg=" + e.getMessage());
      e.printStackTrace();
    }
    try {
      if (this.greaterThan(c)) {
        return 1;
      }
    } catch (CurrencyException e1) {
      System.out.println("compareTo: CurrencyException: greaterThan: msg=" + e1.getMessage());
      e1.printStackTrace();
    }
    return 0;
  }

  /**
   * put your documentation comment here
   * @param c
   * @return
   */
  public static ComparableCurrency toComparableCurrency(ArmCurrency c) {
    if (c != null) {
      return new ComparableCurrency(c.doubleValue());
    } else {
      return new ComparableCurrency(0);
    }
  }

  /**
   * default implementation mapped to ArmCurrency.formattedStringValue()
   */
  public String toString() {
    return formattedStringValue();
  }
}

