/*
 * put your module comment here
 * formatted with JxBeauty (c) johann.langhofer@nextra.at
 */


package com.ga.cs.pos.datatranssferobject;

import java.io.Serializable;
import com.chelseasystems.cr.currency.ArmCurrency;


/**
 * put your documentation comment here
 */
public class AssociateSalesGrandTotalsDataTransferObject implements Serializable {
  static final long serialVersionUID = 5622066091566658638L;
  protected int AddTrnCount = 0;
  protected int AddAvgCount = 0;
  protected int AddNetUnits = 0;
  protected ArmCurrency AddNetSales;
  protected ArmCurrency AddAvgUnits;
  protected int GrandTrnCount = 0;
  protected int GrandAvgCount = 0;
  protected int GrandNetUnits = 0;
  protected ArmCurrency GrandAvgUnits;
  protected ArmCurrency GrandNetSales;

  /**
   * put your documentation comment here
   */
  public AssociateSalesGrandTotalsDataTransferObject() {
  }

  /**
   * put your documentation comment here
   * @param AddNetUnits
   */
  public void setGrandNetUnits(int AddNetUnits) {
    this.GrandNetUnits = AddNetUnits;
  }

  /**
   * put your documentation comment here
   * @return
   */
  public int getGrandNetUnits() {
    return GrandNetUnits;
  }

  /**
   * put your documentation comment here
   * @return
   */
  public ArmCurrency getGrandAvgUnits() {
    return GrandAvgUnits;
  }

  /**
   * put your documentation comment here
   * @param AddAvgUnits
   */
  public void setGrandAvgUnits(ArmCurrency AddAvgUnits) {
    this.GrandAvgUnits = new ArmCurrency(AddAvgUnits.doubleValue());
  }

  /**
   * put your documentation comment here
   * @param AddTrnCount
   */
  public void setGrandTrnCount(int AddTrnCount) {
    this.GrandTrnCount = AddTrnCount;
  }

  /**
   * put your documentation comment here
   * @return
   */
  public int getGrandTrnCount() {
    return GrandTrnCount;
  }

  /**
   * put your documentation comment here
   * @param AddAvgCount
   */
  public void setGrandAvgCount(int AddAvgCount) {
    this.GrandAvgCount = AddAvgCount;
  }

  /**
   * put your documentation comment here
   * @return
   */
  public int getGrandAvgCount() {
    return GrandAvgCount;
  }

  /**
   * put your documentation comment here
   * @return
   */
  public ArmCurrency getGrandNetSales() {
    return GrandNetSales;
  }

  /**
   * put your documentation comment here
   * @param AddNetSales
   */
  public void setGrandNetSales(ArmCurrency AddNetSales) {
    this.GrandNetSales = new ArmCurrency(AddNetSales.doubleValue());
  }

  /**
   * put your documentation comment here
   * @return
   */
  public String getGrandNetSalesString() {
    return GrandNetSales.formattedStringValue();
  }

  /**
   * put your documentation comment here
   * @return
   */
  public String getGrandNetUnitsString() {
    return "" + GrandNetUnits;
  }

  /**
   * put your documentation comment here
   * @return
   */
  public String getGrandAvgCountString() {
    return "" + GrandAvgCount;
  }

  /**
   * put your documentation comment here
   * @return
   */
  public String getGrandTrnCountString() {
    return "" + GrandTrnCount;
  }

  /**
   * put your documentation comment here
   * @return
   */
  public String getGrandAvgUnitsString() {
    return GrandAvgUnits.formattedStringValue();
  }
}

