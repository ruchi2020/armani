/*
 * put your module comment here
 * formatted with JxBeauty (c) johann.langhofer@nextra.at
 */


package com.ga.cs.pos.datatranssferobject;

import com.chelseasystems.cr.currency.ArmCurrency;


/**
 *
 */
public class DeptClassSalesReportDataTransferObject implements java.io.Serializable {
  static final long serialVersionUID = -6484214698249326034L;
  protected String DeptId;
  protected String ClassId;
  protected String DeptDesc;
  protected String ClassDesc;
  protected ArmCurrency NetSales;
  protected ArmCurrency SalesNet;
  protected ArmCurrency TotSales;
  protected int NetUnits = 0;
  protected ArmCurrency GrossSales;
  protected ArmCurrency GrossMkdown;
  protected ArmCurrency ReturnMkdown;
  protected ArmCurrency Returns;
  protected int ReturnsUnits = 0;
  protected ArmCurrency Sales;
  protected ArmCurrency NetReturns;

  /**
   *
   */
  public DeptClassSalesReportDataTransferObject() {
  }

  /**
   * put your documentation comment here
   * @return
   */
  public String getClassId() {
    return ClassId;
  }

  /**
   * put your documentation comment here
   * @param ClassId
   */
  public void setClassId(String ClassId) {
    this.ClassId = ClassId;
  }

  /**
   * put your documentation comment here
   * @return
   */
  public String getClassDesc() {
    return ClassDesc;
  }

  /**
   * put your documentation comment here
   * @param ClassDesc
   */
  public void setClassDesc(String ClassDesc) {
    this.ClassDesc = ClassDesc;
  }

  /**
   * put your documentation comment here
   * @return
   */
  public String getDeptId() {
    return DeptId;
  }

  /**
   * put your documentation comment here
   * @param DeptId
   */
  public void setDeptId(String DeptId) {
    this.DeptId = DeptId;
  }

  /**
   * put your documentation comment here
   * @return
   */
  public String getDeptDesc() {
    return DeptDesc;
  }

  /**
   * put your documentation comment here
   * @param DeptDesc
   */
  public void setDeptDesc(String DeptDesc) {
    this.DeptDesc = DeptDesc;
  }

  /**
   * put your documentation comment here
   * @return
   */
  public ArmCurrency getGrossSales() {
    return GrossSales;
  }

  /**
   * put your documentation comment here
   * @param GrossSales
   */
  public void setGrossSales(ArmCurrency GrossSales) {
    this.GrossSales = new ArmCurrency(GrossSales.doubleValue());
  }

  /**
   * put your documentation comment here
   * @return
   */
  public ArmCurrency getGrossMkdown() {
    return GrossMkdown;
  }

  /**
   * put your documentation comment here
   * @param GrossMkdown
   */
  public void setGrossMkdown(ArmCurrency GrossMkdown) {
    this.GrossMkdown = new ArmCurrency(GrossMkdown.doubleValue());
  }

  /**
   * put your documentation comment here
   * @return
   */
  public ArmCurrency getReturnMkdown() {
    return ReturnMkdown;
  }

  /**
   * put your documentation comment here
   * @param ReturnMkdown
   */
  public void setReturnMkdown(ArmCurrency ReturnMkdown) {
    this.ReturnMkdown = new ArmCurrency(ReturnMkdown.doubleValue());
  }

  /**
   * put your documentation comment here
   * @return
   */
  public ArmCurrency getNetSales() {
    return NetSales;
  }

  /**
   * put your documentation comment here
   * @param NetSales
   */
  public void setNetSales(ArmCurrency NetSales) {
    this.NetSales = new ArmCurrency(NetSales.doubleValue());
  }

  /**
   * put your documentation comment here
   * @return
   */
  public int getNetUnits() {
    return NetUnits;
  }

  /**
   * put your documentation comment here
   * @param NetUnits
   */
  public void setNetUnits(int NetUnits) {
    this.NetUnits = NetUnits;
  }

  /**
   * put your documentation comment here
   * @return
   */
  public ArmCurrency getReturns() {
    return Returns;
  }

  /**
   * put your documentation comment here
   * @param Returns
   */
  public void setReturns(ArmCurrency Returns) {
    this.Returns = new ArmCurrency(Returns.doubleValue());
  }

  /**
   * put your documentation comment here
   * @return
   */
  public int getReturnsUnits() {
    return ReturnsUnits;
  }

  /**
   * put your documentation comment here
   * @param ReturnsUnits
   */
  public void setReturnsUnits(int ReturnsUnits) {
    this.ReturnsUnits = ReturnsUnits;
  }

  /**
   * put your documentation comment here
   */
  public void setSales() {
    try {
      if ((GrossSales != null) && (GrossMkdown != null)) {
        this.Sales = new ArmCurrency(GrossSales.subtract(GrossMkdown).doubleValue());
      }
    } catch (Exception e) {
      System.out.println("setSales: CurrencyException: msg=" + e.getMessage());
      e.printStackTrace();
    }
  }

  /**
   * put your documentation comment here
   * @return
   */
  public ArmCurrency getSales() {
    return Sales;
  }

  /**
   * put your documentation comment here
   */
  public void setNetReturns() {
    try {
      if ((Returns != null) && (ReturnMkdown != null)) {
        this.NetReturns = new ArmCurrency(Returns.subtract(ReturnMkdown).doubleValue());
      }
    } catch (Exception e) {
      System.out.println("setSales: CurrencyException: msg=" + e.getMessage());
      e.printStackTrace();
    }
  }

  /**
   * put your documentation comment here
   * @return
   */
  public ArmCurrency getNetReturns() {
    return NetReturns;
  }

  /**
   * put your documentation comment here
   */
  public void setSalesNet() {
    try {
      if ((GrossSales != null) && (GrossMkdown != null)) {
        this.SalesNet = new ArmCurrency(GrossSales.subtract(GrossMkdown).subtract(Returns).add(
            ReturnMkdown).doubleValue());
      }
    } catch (Exception e) {
      System.out.println("setSales: CurrencyException: msg=" + e.getMessage());
      e.printStackTrace();
    }
  }

  /**
   * put your documentation comment here
   * @return
   */
  public ArmCurrency getSalesNet() {
    return SalesNet;
  }

  /**
   * put your documentation comment here
   * @return
   */
  public String getNetSalesString() {
    return NetSales.formattedStringValue();
  }

  /**
   * put your documentation comment here
   * @return
   */
  public String getSalesString() {
    return Sales.formattedStringValue();
  }

  /**
   * put your documentation comment here
   * @return
   */
  public String getSalesNetString() {
    return SalesNet.formattedStringValue();
  }

  /**
   * put your documentation comment here
   * @return
   */
  public String getGrossSalesString() {
    return GrossSales.formattedStringValue();
  }

  /**
   * put your documentation comment here
   * @return
   */
  public String getGrossMkdownString() {
    return GrossMkdown.formattedStringValue();
  }

  /**
   * put your documentation comment here
   * @return
   */
  public String getReturnMkdownString() {
    return ReturnMkdown.formattedStringValue();
  }

  /**
   * put your documentation comment here
   * @return
   */
  public String getNetReturnsString() {
    return NetReturns.formattedStringValue();
  }

  /**
   * put your documentation comment here
   * @return
   */
  public String getReturnsString() {
    return Returns.formattedStringValue();
  }

  /**
   * put your documentation comment here
   * @return
   */
  public String getNetUnitsString() {
    return "" + NetUnits;
  }

  /**
   * put your documentation comment here
   * @return
   */
  public String getReturnsUnitsString() {
    return "" + ReturnsUnits;
  }
}

