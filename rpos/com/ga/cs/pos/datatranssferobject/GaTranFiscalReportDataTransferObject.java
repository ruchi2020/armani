/*
 * put your module comment here
 * formatted with JxBeauty (c) johann.langhofer@nextra.at
 */


package com.ga.cs.pos.datatranssferobject;

import com.chelseasystems.cr.currency.ArmCurrency;


/**
 * put your documentation comment here
 */
public class GaTranFiscalReportDataTransferObject implements java.io.Serializable {
  static final long serialVersionUID = 3223694749547779138L;
  private String fiscalHalf;
  private ArmCurrency sales;
  private ArmCurrency netSales;
  private ArmCurrency grossSales;
  private int qty = 0;
  private int fiscalSeq = 0;
  private String fiscalDay = "";

  /**
   * put your documentation comment here
   * @param   String fiscalDay
   */
  public GaTranFiscalReportDataTransferObject(String fiscalDay) {
    this.fiscalDay = fiscalDay;
  }

  /**
   * put your documentation comment here
   * @return
   */
  public String getFiscalDay() {
    return fiscalDay;
  }

  /**
   * put your documentation comment here
   * @return
   */
  public String getFiscalHalf() {
    return fiscalHalf;
  }

  /**
   * put your documentation comment here
   * @param FiscalHalf
   */
  public void setFiscalHalf(String FiscalHalf) {
    this.fiscalHalf = FiscalHalf;
  }

  /**
   * put your documentation comment here
   * @return
   */
  public int getFiscalSeq() {
    return fiscalSeq;
  }

  /**
   * put your documentation comment here
   * @param FiscalSeq
   */
  public void setFiscalSeq(int FiscalSeq) {
    this.fiscalSeq = FiscalSeq;
  }

  /**
   * put your documentation comment here
   * @return
   */
  public ArmCurrency getSales() {
    return sales;
  }

  /**
   * put your documentation comment here
   * @param Sales
   */
  public void setNetSales(ArmCurrency Sales) {
    this.netSales = Sales;
  }

  /**
   * put your documentation comment here
   * @return
   */
  public ArmCurrency getNetSales() {
    return this.netSales;
  }

  /**
   * put your documentation comment here
   * @param Sales
   */
  public void setGrossSales(ArmCurrency Sales) {
    this.grossSales = Sales;
  }

  /**
   * put your documentation comment here
   * @return
   */
  public ArmCurrency getGrossSales() {
    return this.grossSales;
  }

  /**
   * put your documentation comment here
   * @param Sales
   */
  public void setSales(ArmCurrency Sales) {
    this.sales = Sales;
  }

  /**
   * put your documentation comment here
   * @return
   */
  public int getQty() {
    return qty;
  }

  /**
   * put your documentation comment here
   * @param Qty
   */
  public void setQty(int Qty) {
    this.qty = Qty;
  }

  /**
   * put your documentation comment here
   * @return
   */
  public String getSalesString() {
    return sales.formattedStringValue();
  }

  /**
   * put your documentation comment here
   * @return
   */
  public String getNetSalesString() {
    return netSales.formattedStringValue();
  }

  /**
   * put your documentation comment here
   * @return
   */
  public String getGrossSalesString() {
    return grossSales.formattedStringValue();
  }

  /**
   * put your documentation comment here
   * @return
   */
  public String getFiscalSeqString() {
    return "" + fiscalSeq;
  }

  /**
   * put your documentation comment here
   * @return
   */
  public String getQtyString() {
    return "" + qty;
  }
}

