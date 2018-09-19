/*
 * put your module comment here
 * formatted with JxBeauty (c) johann.langhofer@nextra.at
 */


package com.ga.cs.pos.datatranssferobject;

import com.chelseasystems.cr.currency.ArmCurrency;


/**
 *
 */
public class ItemSalesReportDataTransferObject implements java.io.Serializable {
  static final long serialVersionUID = 1788043623677818486L;
  protected String DeptId;
  protected String DeptDesc;
  protected String ItemId;
  protected String EmpId;
  protected String ClassDesc;
  protected String ItemDesc;
  protected ArmCurrency NetSales;
  protected ArmCurrency Sales;
  protected ArmCurrency SalesNet;
  protected int NetUnits = 0;
  protected ArmCurrency GrossSales;
  protected ArmCurrency GrossMkdown;
  protected ArmCurrency Returns;

  /**
   *
   */
  public ItemSalesReportDataTransferObject() {
  }

  /**
   * put your documentation comment here
   * @param   String ItemId
   */
  public ItemSalesReportDataTransferObject(String ItemId) {
    this.ItemId = ItemId;
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
  public String getClassDesc() {
    return this.ClassDesc;
  }

  /**
   * put your documentation comment here
   * @param classDesc
   */
  public void setClassDesc(String classDesc) {
    this.ClassDesc = classDesc;
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
  public String getItemId() {
    return ItemId;
  }

  /**
   * put your documentation comment here
   * @param ItemId
   */
  public void setItemId(String ItemId) {
    this.ItemId = ItemId;
  }

  /**
   * put your documentation comment here
   * @return
   */
  public String getItemDesc() {
    return ItemDesc;
  }

  /**
   * put your documentation comment here
   * @param ItemDesc
   */
  public void setItemDesc(String ItemDesc) {
    this.ItemDesc = ItemDesc;
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
  public void setSalesNet() {
    try {
      if ((GrossSales != null) && (GrossMkdown != null)) {
        this.SalesNet = new ArmCurrency(GrossSales.subtract(GrossMkdown).subtract(Returns).doubleValue());
        //this.SalesNet = new ArmCurrency(GrossSales.doubleValue());
      }
    } catch (Exception e) {
      System.out.println("setSalesNet: CurrencyException: msg=" + e.getMessage());
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
}

