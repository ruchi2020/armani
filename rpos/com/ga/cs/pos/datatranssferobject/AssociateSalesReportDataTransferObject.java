/*
 * put your module comment here
 * formatted with JxBeauty (c) johann.langhofer@nextra.at
 */


package com.ga.cs.pos.datatranssferobject;

import com.chelseasystems.cr.currency.ArmCurrency;


/**
 * put your documentation comment here
 */
public class AssociateSalesReportDataTransferObject implements java.io.Serializable {
  static final long serialVersionUID = 354415349650437468L;
  protected int TrnCount = 0;
  protected int AvgCount = 0;
  protected int NetUnits = 0;
  protected int GrandTrnCount = 0;
  protected int GrandAvgCount = 0;
  protected int GrandNetUnits = 0;
  protected int AddNetUnits = 0;
  protected int AvgUnits;
  protected int GrandAvgUnits;
  protected String EmpLast;
  protected String EmpFirst;
  protected String EmpName;
  protected String EmpId;
  protected ArmCurrency NetSales;
  protected ArmCurrency AvgSal;
  protected ArmCurrency GrandNetSales;
  protected ArmCurrency GrandAvgSal;

  /**
   *
   */
  public AssociateSalesReportDataTransferObject() {
    AvgUnits = 0;
    GrandAvgUnits = 0;
    EmpLast = new String();
    EmpFirst = new String();
    EmpId = new String();
    NetSales = new ArmCurrency(0.0d);
    AvgSal = new ArmCurrency(0.0d);
    GrandNetSales = new ArmCurrency(0.0d);
    GrandAvgSal = new ArmCurrency(0.0d);
  }

  /**
   * put your documentation comment here
   * @return
   */
  public int getTrnCount() {
    return TrnCount;
  }

  /**
   * put your documentation comment here
   * @param TrnCount
   */
  public void setTrnCount(int TrnCount) {
    this.TrnCount = TrnCount;
  }

  /**
   * put your documentation comment here
   * @return
   */
  public int getAvgCount() {
    return AvgCount;
  }

  /**
   * put your documentation comment here
   * @param AvgCount
   */
  public void setAvgCount(int AvgCount) {
    this.AvgCount = AvgCount;
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
   * @param AddNetUnits
   */
  public void setAddNetUnits(int AddNetUnits) {
    this.AddNetUnits = +AddNetUnits;
  }

  /**
   * put your documentation comment here
   * @return
   */
  public int getAddNetUnits() {
    return AddNetUnits;
  }

  /**
   * put your documentation comment here
   * @param AddNetUnits
   */
  public void setGrandNetUnits(int AddNetUnits) {
    this.AddNetUnits = +AddNetUnits;
  }

  /**
   * put your documentation comment here
   * @return
   */
  public double getAvgUnits() {
    return (double)this.NetUnits / this.TrnCount;
  }

  /**
   * put your documentation comment here
   * @param AvgUnits
   */
  public void setAvgUnits(int AvgUnits) {
    this.AvgUnits = AvgUnits;
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
  public ArmCurrency getAvgSal() {
    return this.NetSales.divide(this.TrnCount);
  }

  /**
   * put your documentation comment here
   * @param AvgSal
   */
  public void setAvgSal(ArmCurrency AvgSal) {
    this.AvgSal = new ArmCurrency(AvgSal.doubleValue());
  }

  /**
   * put your documentation comment here
   * @return
   */
  public String getEmpFirst() {
    return EmpFirst;
  }

  /**
   * put your documentation comment here
   * @param EmpFirst
   */
  public void setEmpFirst(String EmpFirst) {
    this.EmpFirst = EmpFirst;
  }

  /**
   * put your documentation comment here
   * @return
   */
  public String getEmpId() {
    return EmpId;
  }

  /**
   * put your documentation comment here
   * @param EmpId
   */
  public void setEmpId(String EmpId) {
    this.EmpId = EmpId;
  }

  /**
   * put your documentation comment here
   * @return
   */
  public String getEmpLast() {
    return EmpLast;
  }

  /**
   * put your documentation comment here
   * @param EmpLast
   */
  public void setEmpLast(String EmpLast) {
    this.EmpLast = EmpLast;
  }

  /**
   * put your documentation comment here
   * @return
   */
  public String getAvgSalString() {
    return AvgSal.formattedStringValue();
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
  public String getAvgUnitsString() {
    return "" + AvgUnits;
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
  public String getAvgCountString() {
    return "" + AvgCount;
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
   * @param GrandTrnCount
   */
  public void setGrandTrnCount(int GrandTrnCount) {
    this.GrandTrnCount = GrandTrnCount;
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
   * @param GrandAvgCount
   */
  public void setGrandAvgCount(int GrandAvgCount) {
    this.GrandAvgCount = GrandAvgCount;
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
  public int getGrandAvgUnits() {
    return GrandAvgUnits;
  }

  /**
   * put your documentation comment here
   * @param GrandAvgUnits
   */
  public void setGrandAvgUnits(int GrandAvgUnits) {
    this.GrandAvgUnits = GrandAvgUnits;
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
   */
  public void setGrandNetSales() {
    this.GrandNetSales = new ArmCurrency(NetSales.doubleValue());
  }

  /**
   * put your documentation comment here
   * @return
   */
  public ArmCurrency getGrandAvgSal() {
    return GrandAvgSal;
  }

  /**
   * put your documentation comment here
   * @param GrandAvgSal
   */
  public void setGrandAvgSal(ArmCurrency GrandAvgSal) {
    this.GrandAvgSal = new ArmCurrency(GrandAvgSal.doubleValue());
  }

  /**
   * put your documentation comment here
   * @return
   */
  public String getGrandAvgSalString() {
    return GrandAvgSal.formattedStringValue();
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
  public String getGrandAvgUnitsString() {
    return "" + GrandAvgUnits;
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
   */
  public void setEmpName() {
    try {
      if ((EmpLast != null) && (EmpLast != null)) {
        this.EmpName = new String(EmpLast.concat(EmpFirst));
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
  public String getEmpName() {
    return EmpName;
  }
}

