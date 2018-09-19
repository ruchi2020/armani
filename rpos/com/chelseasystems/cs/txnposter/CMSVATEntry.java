/*
 * put your module comment here
 * formatted with JxBeauty (c) johann.langhofer@nextra.at
 */
//
// Copyright 1999-2001, Chelsea Market Systems
//
/*
 History:
 +------+------------+-----------+-----------+----------------------------------------------------+
 | Ver# | Date       | By        | Defect #  | Description                                        |
 +------+------------+-----------+-----------+----------------------------------------------------+
 | 1    | 04-29-2005 | Pankaja   | N/A       |To compute the media count                          |
 +------+------------+-----------+-----------+----------------------------------------------------+
 */


package com.chelseasystems.cs.txnposter;

import com.chelseasystems.cr.business.BusinessObject;
import com.chelseasystems.cr.currency.*;
import java.io.Serializable;


/**
 * <p>Title: </p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2004</p>
 * <p>Company: </p>
 * @author unascribed
 * @version 1.0
 */
public class CMSVATEntry implements Serializable{
  private double vatRate;
  private ArmCurrency vatAmt;
  private ArmCurrency totalAmt;

  /**
   * put your documentation comment here
   * @param   Payment payment
   */
  public CMSVATEntry() {
    vatRate = 0.0d;
    vatAmt = new ArmCurrency(0.0d);
    totalAmt = new ArmCurrency(0.0d);
  }

  public ArmCurrency getTotalAmount() {
      return totalAmt;
  }

  /**
   * put your documentation comment here
   * @param amt
   */
  public void addTotalAmount(ArmCurrency amt) {
    try {
      ArmCurrency total = this.getTotalAmount().add(amt);
      totalAmt = total;
    } catch (Exception ex) {
      System.out.println("Exception addTotalAmount()->" + ex);
    }
  }

  public void setVATRate(double rate) {
    this.vatRate = rate;
  }

  public double getVATRate() {
    return this.vatRate;
  }

  public void addVATAmount(ArmCurrency amt) {
    try {
      ArmCurrency total = this.getVATAmount().add(amt);
      this.vatAmt = total;
    } catch (Exception ex) {
      System.out.println("Exception addTotalAmount()->" + ex);
    }
  }

  public ArmCurrency getVATAmount() {
    return this.vatAmt;
  }

}

