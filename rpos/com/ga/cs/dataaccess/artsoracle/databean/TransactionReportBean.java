/*
 * Created on Apr 27, 2005
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 *
 * formatted with JxBeauty (c) johann.langhofer@nextra.at
 */


package com.ga.cs.dataaccess.artsoracle.databean;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import com.chelseasystems.cr.currency.ArmCurrency;
import com.chelseasystems.cs.dataaccess.artsoracle.databean.BaseOracleBean;
import com.ga.cs.dataaccess.artsoracle.dao.TransactionReportOracleDAO;
import com.ga.cs.utils.ReportUtils;


/**
 * bean to hold results of Transaction Report query
 * @author fbulah
 *
 */
public class TransactionReportBean extends BaseOracleBean {
  public static String REGISTER_ID_ATTR_NAME = TransactionReportOracleDAO.REGISTER_ID;
  public static String TRANSACTION_COUNT_ATTR_NAME = TransactionReportOracleDAO.TRANSACTION_COUNT;
  public static String UNITS_COUNT_ATTR_NAME = TransactionReportOracleDAO.UNITS_COUNT;
  public static String TOTAL_SALES_ATTR_NAME = TransactionReportOracleDAO.TOTAL_SALES;
  private String registerId;
  private int transactionCount;
  private int unitsCount;
  private ArmCurrency totalSales;
  private String registerIdAttrName;
  private String transactionCountAttrName;
  private String unitsCountAttrName;
  private String totalSalesAttrName;

  /**
   * put your documentation comment here
   */
  public TransactionReportBean() {
    registerIdAttrName = REGISTER_ID_ATTR_NAME;
    transactionCountAttrName = TRANSACTION_COUNT_ATTR_NAME;
    unitsCountAttrName = UNITS_COUNT_ATTR_NAME;
    totalSalesAttrName = TOTAL_SALES_ATTR_NAME;
  }

  /**
   * put your documentation comment here
   * @param   String registerIdAttrName
   * @param   String transactionCountAttrName
   * @param   String unitsCountAttrName
   * @param   String totalSalesAttrName
   */
  public TransactionReportBean(String registerIdAttrName, String transactionCountAttrName
      , String unitsCountAttrName, String totalSalesAttrName) {
    this.registerIdAttrName = registerIdAttrName;
    this.transactionCountAttrName = transactionCountAttrName;
    this.unitsCountAttrName = unitsCountAttrName;
    this.totalSalesAttrName = totalSalesAttrName;
  }

  /**
   * loads list of beans with result set data and returns list
   * @see com.chelseasystems.cs.dataaccess.artsoracle.databean.BaseOracleBean#getDatabeans(java.sql.ResultSet)
   * NOTE: getInt() not supported in BaseOracleBean
   */
  public BaseOracleBean[] getDatabeans(ResultSet rs)
      throws SQLException {
    //		ReportUtils.printResultSetMetadata(rs);
    ArrayList list = new ArrayList();
    while (rs.next()) {
      TransactionReportBean bean = new TransactionReportBean();
      bean.registerId = getStringFromResultSet(rs, registerIdAttrName);
      bean.transactionCount = rs.getInt(transactionCountAttrName);
      bean.unitsCount = rs.getInt(unitsCountAttrName);
      bean.totalSales = new ArmCurrency(rs.getBigDecimal(totalSalesAttrName).doubleValue());
      list.add(bean);
    }
    return (TransactionReportBean[])list.toArray(new TransactionReportBean[0]);
  }

  /**
   * @return Returns the registerId.
   */
  public String getRegisterId() {
    return registerId;
  }

  /**
   * @param registerId The registerId to set.
   */
  public void setRegisterId(String registerId) {
    this.registerId = registerId;
  }

  /**
   * @return Returns the registerIdAttrName.
   */
  public String getRegisterIdAttrName() {
    return registerIdAttrName;
  }

  /**
   * @param registerIdAttrName The registerIdAttrName to set.
   */
  public void setRegisterIdAttrName(String registerIdAttrName) {
    this.registerIdAttrName = registerIdAttrName;
  }

  /**
   * @return Returns the totalSales.
   */
  public ArmCurrency getTotalSales() {
    return totalSales;
  }

  /**
   * @param totalSales The totalSales to set.
   */
  public void setTotalSales(ArmCurrency totalSales) {
    this.totalSales = totalSales;
  }

  /**
   * @return Returns the totalSalesAttrName.
   */
  public String getTotalSalesAttrName() {
    return totalSalesAttrName;
  }

  /**
   * @param totalSalesAttrName The totalSalesAttrName to set.
   */
  public void setTotalSalesAttrName(String totalSalesAttrName) {
    this.totalSalesAttrName = totalSalesAttrName;
  }

  /**
   * @return Returns the transactionCount.
   */
  public int getTransactionCount() {
    return transactionCount;
  }

  /**
   * @param transactionCount The transactionCount to set.
   */
  public void setTransactionCount(int transactionCount) {
    this.transactionCount = transactionCount;
  }

  /**
   * @return Returns the transactionCountAttrName.
   */
  public String getTransactionCountAttrName() {
    return transactionCountAttrName;
  }

  /**
   * @param transactionCountAttrName The transactionCountAttrName to set.
   */
  public void setTransactionCountAttrName(String transactionCountAttrName) {
    this.transactionCountAttrName = transactionCountAttrName;
  }

  /**
   * @return Returns the unitsCount.
   */
  public int getUnitsCount() {
    return unitsCount;
  }

  /**
   * @param unitsCount The unitsCount to set.
   */
  public void setUnitsCount(int unitsCount) {
    this.unitsCount = unitsCount;
  }

  /**
   * @return Returns the unitsCountAttrName.
   */
  public String getUnitsCountAttrName() {
    return unitsCountAttrName;
  }

  /**
   * @param unitsCountAttrName The unitsCountAttrName to set.
   */
  public void setUnitsCountAttrName(String unitsCountAttrName) {
    this.unitsCountAttrName = unitsCountAttrName;
  }

  /* (non-Javadoc)
   * @see com.chelseasystems.cs.dataaccess.artsoracle.databean.BaseOracleBean#getDeleteSql()
   */
  public String getDeleteSql() {
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
   * @see com.chelseasystems.cs.dataaccess.artsoracle.databean.BaseOracleBean#getSelectSql()
   */
  public String getSelectSql() {
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
   * @see com.chelseasystems.cs.dataaccess.artsoracle.databean.BaseOracleBean#toList()
   */
  public List toList() {
    // TODO Auto-generated method stub
    return null;
  }
}

