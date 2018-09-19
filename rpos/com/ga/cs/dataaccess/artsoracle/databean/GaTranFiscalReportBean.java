/*
 * put your module comment here
 * formatted with JxBeauty (c) johann.langhofer@nextra.at
 */


package com.ga.cs.dataaccess.artsoracle.databean;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import com.ga.cs.dataaccess.artsoracle.dao.GaTranFiscalOracleDAO;
import com.chelseasystems.cr.currency.ArmCurrency;
import com.chelseasystems.cs.dataaccess.artsoracle.databean.BaseOracleBean;


/**
 * Bean used to contain Associate sales data by date
 *
 */
public class GaTranFiscalReportBean extends BaseOracleBean {
  public static String FISCAL_HALF_ATTR_NAME = GaTranFiscalOracleDAO.FISCAL_HALF;
  public static String FISCAL_SEQ_ATTR_NAME = GaTranFiscalOracleDAO.FISCAL_SEQ;
  public static String QTY_ATTR_NAME = GaTranFiscalOracleDAO.QTY;
  public static String SALES_ATTR_NAME = GaTranFiscalOracleDAO.SALES;
  public static String NET_SALES_ATTR_NAME = GaTranFiscalOracleDAO.NET_SALES;
  public static String GROSS_SALES_ATTR_NAME = GaTranFiscalOracleDAO.GROSS_SALES;
  public static String FISCAL_DAY_ATTR_NAME = GaTranFiscalOracleDAO.FISCAL_DAY;
  private String FiscalDay;
  private String FiscalHalf;
  private int FiscalSeq;
  private ArmCurrency Sales;
  private ArmCurrency NetSales;
  private ArmCurrency GrossSales;
  private int Qty;

  /**
   * put your documentation comment here
   */
  public GaTranFiscalReportBean() {
  }

  /**
   * put your documentation comment here
   * @return
   */
  public String getFiscalHalf() {
    return FiscalHalf;
  }

  /**
   * put your documentation comment here
   * @param FiscalHalf
   */
  public void setFiscalHalf(String FiscalHalf) {
    this.FiscalHalf = FiscalHalf;
  }

  /**
   * put your documentation comment here
   * @return
   */
  public String getFiscalDay() {
    return FiscalDay;
  }

  /**
   * put your documentation comment here
   * @param FiscalDay
   */
  public void setFiscalDay(String FiscalDay) {
    this.FiscalDay = FiscalDay;
  }

  /**
   * put your documentation comment here
   * @return
   */
  public int getFiscalSeq() {
    return FiscalSeq;
  }

  /**
   * put your documentation comment here
   * @param FiscalSeq
   */
  public void setFiscalSeq(int FiscalSeq) {
    this.FiscalSeq = FiscalSeq;
  }

  /**
   * put your documentation comment here
   * @param Sales
   */
  public void setNetSales(ArmCurrency Sales) {
    this.NetSales = Sales;
  }

  /**
   * put your documentation comment here
   * @return
   */
  public ArmCurrency getNetSales() {
    return this.NetSales;
  }

  /**
   * put your documentation comment here
   * @param Sales
   */
  public void setGrossSales(ArmCurrency Sales) {
    this.GrossSales = Sales;
  }

  /**
   * put your documentation comment here
   * @return
   */
  public ArmCurrency getGrossSales() {
    return this.GrossSales;
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
   * @param Sales
   */
  public void setSales(ArmCurrency Sales) {
    this.Sales = Sales;
  }

  /**
   * put your documentation comment here
   * @return
   */
  public int getQty() {
    return Qty;
  }

  /**
   * put your documentation comment here
   * @param Qty
   */
  public void setQty(int Qty) {
    this.Qty = Qty;
  }

  /* (non-Javadoc)
   * @see com.chelseasystems.cs.dataaccess.artsoracle.databean.BaseOracleBean#getDatabeans(java.sql.ResultSet)
   */
  public BaseOracleBean[] getDatabeans(ResultSet rs)
      throws SQLException {
    ArrayList list = new ArrayList();
    //printResultSetMetadata(rs);
    while (rs.next()) {
      GaTranFiscalReportBean bean = new GaTranFiscalReportBean();
      bean.FiscalHalf = getStringFromResultSet(rs, this.FISCAL_HALF_ATTR_NAME);
      bean.FiscalSeq = rs.getInt(FISCAL_SEQ_ATTR_NAME);
      bean.Qty = rs.getInt(QTY_ATTR_NAME);
      bean.GrossSales = new ArmCurrency(getDoubleFromResultSet(rs, this.GROSS_SALES_ATTR_NAME).
          doubleValue());
      bean.NetSales = new ArmCurrency(getDoubleFromResultSet(rs, this.NET_SALES_ATTR_NAME).doubleValue());
      bean.FiscalDay = getStringFromResultSet(rs, this.FISCAL_DAY_ATTR_NAME);
      list.add(bean);
    }
    return (GaTranFiscalReportBean[])list.toArray(new GaTranFiscalReportBean[0]);
  }

  /* (non-Javadoc)
   * @see com.chelseasystems.cs.dataaccess.artsoracle.databean.BaseOracleBean#toList()
   */
  public List toList() {
    // TODO Auto-generated method stub
    return null;
  }

  /* (non-Javadoc)
   * @see com.chelseasystems.cs.dataaccess.artsoracle.databean.BaseOracleBean#getSelectSql()
   */
  public String getSelectSql() {
    // TODO Auto-generated method stub
    return null;
  }

  /* (non-Javadoc)
   * @see com.chelseasystems.cs.dataaccess.artsoracle.databean.BaseOracleBean#getInsertSql()
   */
  public String getInsertSql() {
    // TODO Auto-generated method stub
    return null;
  }

  /* (non-Javadoc)
   * @see com.chelseasystems.cs.dataaccess.artsoracle.databean.BaseOracleBean#getUpdateSql()
   */
  public String getUpdateSql() {
    // TODO Auto-generated method stub
    return null;
  }

  /* (non-Javadoc)
   * @see com.chelseasystems.cs.dataaccess.artsoracle.databean.BaseOracleBean#getDeleteSql()
   */
  public String getDeleteSql() {
    // TODO Auto-generated method stub
    return null;
  }

  /**
   * put your documentation comment here
   * @param args
   */
  public static void main(String[] args) {}
}

