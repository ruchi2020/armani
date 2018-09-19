/*
 * put your module comment here
 * formatted with JxBeauty (c) johann.langhofer@nextra.at
 */


package com.chelseasystems.cs.eod;

import com.chelseasystems.cr.currency.ArmCurrency;


/**
 *
 * <p>Title: AccountableMedia</p>
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
public class AccountableMedia implements java.io.Serializable {
  static final long serialVersionUID = -123456789725951L;
  private String description;
  private ArmCurrency tendered;
  private ArmCurrency reported;

  /**
   * Constructor
   * @param description String
   * @param reported Currency
   * @param tendered Currency
   */
  public AccountableMedia(String description, ArmCurrency reported, ArmCurrency tendered) {
    this.description = description;
    this.tendered = tendered;
    this.reported = reported;
  }

  /**
   * This method is used to get description
   * @return String
   */
  public String getDecription() {
    return description;
  }

  /**
   * This method is used to set reported
   * @return Currency
   */
  public ArmCurrency getTendered() {
    return tendered;
  }

  /**
   * This method is used to get reported
   * @return Currency
   */
  public ArmCurrency getReported() {
    return reported;
  }

  /**
   *
   * @return String
   */
  public String toString() {
    StringBuffer buff = new StringBuffer();
    buff.append("AccountableMedia[desc=");
    buff.append(description);
    buff.append(",rptd=");
    buff.append(reported.formattedStringValue());
    buff.append(",tend=");
    buff.append(tendered.formattedStringValue());
    buff.append("]");
    return buff.toString();
  }
}

