/*
 * put your module comment here
 * formatted with JxBeauty (c) johann.langhofer@nextra.at
 */


package com.ga.cs.pos.datatranssferobject;

import com.chelseasystems.cr.currency.ArmCurrency;


/**
 *
 */
public class DepartmentSalesGrandTotalsDataTransferObject implements java.io.Serializable {
  static final long serialVersionUID = 3925891175342254518L;
  protected ArmCurrency AddSales;
  protected ArmCurrency AddSalesNet;
  protected ArmCurrency AddNetReturns;
  protected ArmCurrency AddGrossSales;
  protected ArmCurrency AddReturnMkdown;
  protected ArmCurrency AddReturns;
  protected int AddReturnsUnits = 0;
  protected int AddNetUnits = 0;
  protected ArmCurrency TotalSales;
  protected ArmCurrency TotalSalesNet;
  protected ArmCurrency TotalNetReturns;
  protected int TotalNetUnits = 0;
  protected ArmCurrency TotalGrossSales;
  protected ArmCurrency TotalReturnMkdown;
  protected ArmCurrency TotalGrossMkdown;
  protected ArmCurrency TotalReturns;
  protected int TotalReturnsUnits = 0;

  /**
   *
   */
  public DepartmentSalesGrandTotalsDataTransferObject() {
  }

  /**
   * put your documentation comment here
   * @param AddNetUnits
   */
  public void setTotalNetUnits(int AddNetUnits) {
    this.TotalNetUnits = AddNetUnits;
  }

  /**
   * put your documentation comment here
   * @return
   */
  public int getTotalNetUnits() {
    return TotalNetUnits;
  }

  /**
   * put your documentation comment here
   * @param AddReturnsUnits
   */
  public void setTotalReturnsUnits(int AddReturnsUnits) {
    this.TotalReturnsUnits = AddReturnsUnits;
  }

  /**
   * put your documentation comment here
   * @return
   */
  public int getTotalReturnsUnits() {
    return TotalReturnsUnits;
  }

  /**
   * put your documentation comment here
   * @return
   */
  public ArmCurrency getTotalReturns() {
    return TotalReturns;
  }

  /**
   * put your documentation comment here
   * @param AddReturns
   */
  public void setTotalReturns(ArmCurrency AddReturns) {
    this.TotalReturns = new ArmCurrency(AddReturns.doubleValue());
  }

  /**
   * put your documentation comment here
   * @return
   */
  public ArmCurrency getTotalNetReturns() {
    return TotalNetReturns;
  }

  /**
   * put your documentation comment here
   * @param AddNetReturns
   */
  public void setTotalNetReturns(ArmCurrency AddNetReturns) {
    this.TotalNetReturns = new ArmCurrency(AddNetReturns.doubleValue());
  }

  /**
   * put your documentation comment here
   * @return
   */
  public ArmCurrency getTotalReturnMkdown() {
    return TotalReturnMkdown;
  }

  /**
   * put your documentation comment here
   * @param AddReturnMkdown
   */
  public void setTotalReturnMkdown(ArmCurrency AddReturnMkdown) {
    this.TotalReturnMkdown = new ArmCurrency(AddReturnMkdown.doubleValue());
  }

  /**
   * put your documentation comment here
   * @return
   */
  public ArmCurrency getTotalSales() {
    return TotalSales;
  }

  /**
   * put your documentation comment here
   * @param AddSales
   */
  public void setTotalSales(ArmCurrency AddSales) {
    this.TotalSales = new ArmCurrency(AddSales.doubleValue());
  }

  /**
   * put your documentation comment here
   * @return
   */
  public ArmCurrency getTotalSalesNet() {
    return TotalSalesNet;
  }

  /**
   * put your documentation comment here
   * @param AddSalesNet
   */
  public void setTotalSalesNet(ArmCurrency AddSalesNet) {
    this.TotalSalesNet = new ArmCurrency(AddSalesNet.doubleValue());
  }

  /**
   * put your documentation comment here
   * @return
   */
  public ArmCurrency getTotalGrossSales() {
    return TotalGrossSales;
  }

  /**
   * put your documentation comment here
   * @param AddGrossSales
   */
  public void setTotalGrossSales(ArmCurrency AddGrossSales) {
    this.TotalGrossSales = new ArmCurrency(AddGrossSales.doubleValue());
  }

  /**
   * put your documentation comment here
   * @return
   */
  public String getTotalSalesString() {
    return TotalSales.formattedStringValue();
  }

  /**
   * put your documentation comment here
   * @return
   */
  public String getTotalSalesNetString() {
    return TotalSalesNet.formattedStringValue();
  }

  /**
   * put your documentation comment here
   * @return
   */
  public String getTotalGrossSalesString() {
    return TotalGrossSales.formattedStringValue();
  }

  /**
   * put your documentation comment here
   * @return
   */
  public String getTotalReturnMkdownString() {
    return TotalReturnMkdown.formattedStringValue();
  }

  /**
   * put your documentation comment here
   * @return
   */
  public String getTotalNetReturnsString() {
    return TotalNetReturns.formattedStringValue();
  }

  /**
   * put your documentation comment here
   * @return
   */
  public String getTotalReturnsString() {
    return TotalReturns.formattedStringValue();
  }

  /**
   * put your documentation comment here
   * @return
   */
  public String getTotalNetUnitsString() {
    return "" + TotalNetUnits;
  }

  /**
   * put your documentation comment here
   * @return
   */
  public String getTotalReturnsUnitsString() {
    return "" + TotalReturnsUnits;
  }

  /**
   * put your documentation comment here
   * @return
   */
  public String getTotalGrossMkdownString() {
    return TotalGrossMkdown.formattedStringValue();
  }
}

