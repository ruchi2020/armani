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
public class GaFiscalReportBean extends BaseOracleBean {
  public static String FISCAL_DAY_ATTR_NAME = GaTranFiscalOracleDAO.FISCAL_DAY;
  private String FiscalDay;
  private String FiscalDayAttrName;
  private String DeptDescAttrName;

  /**
   * put your documentation comment here
   */
  public GaFiscalReportBean() {
    FiscalDayAttrName = FISCAL_DAY_ATTR_NAME;
  }

  /**
   * put your documentation comment here
   * @param   String FiscalDayAttrName
   */
  public GaFiscalReportBean(String FiscalDayAttrName) {
    this.FiscalDayAttrName = FiscalDayAttrName;
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

  /* (non-Javadoc)
   * @see com.chelseasystems.cs.dataaccess.artsoracle.databean.BaseOracleBean#getDatabeans(java.sql.ResultSet)
   */
  public BaseOracleBean[] getDatabeans(ResultSet rs)
      throws SQLException {
    ArrayList list = new ArrayList();
    //printResultSetMetadata(rs);
    while (rs.next()) {
      GaFiscalReportBean bean = new GaFiscalReportBean();
      String string = bean.FiscalDay = getStringFromResultSet(rs, FiscalDayAttrName);
      list.add(bean);
    }
    return (GaFiscalReportBean[])list.toArray(new GaFiscalReportBean[0]);
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

