/*
 * put your module comment here
 * formatted with JxBeauty (c) johann.langhofer@nextra.at
 */


package com.ga.cs.dataaccess.artsoracle.databean;

import java.math.BigDecimal;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import com.chelseasystems.cs.dataaccess.artsoracle.databean.BaseOracleBean;
import com.chelseasystems.cs.dataaccess.rms.databean.PosModOracleBean;
import com.ga.cs.utils.ReportUtils;


/**
 * A bean containing n double numeric fields used to return a list of double SQL query result values
 * @author fbulah
 *
 */
public class NBigDecimalValueBean extends BaseOracleBean {
  private HashMap attrValueMap;

  /**
   * put your documentation comment here
   */
  public NBigDecimalValueBean() {
    this.attrValueMap = new HashMap();
  }

  /**
   * put your documentation comment here
   * @param   HashMap attrValueMap
   */
  public NBigDecimalValueBean(HashMap attrValueMap) {
    this.attrValueMap = new HashMap(attrValueMap);
  }

  /**
   * put your documentation comment here
   * @param attr
   * @param value
   */
  public void setAttrValue(String attr, BigDecimal value) {
    this.attrValueMap.put(attr, value);
  }

  /**
   * put your documentation comment here
   * @param attr
   * @return
   */
  public BigDecimal getAttrValue(String attr) {
    return (BigDecimal)attrValueMap.get(attr);
  }

  /**
   * put your documentation comment here
   * @return
   */
  public HashMap getAttrValueMap() {
    return attrValueMap;
  }

  /* (non-Javadoc)
   * @see com.chelseasystems.cs.dataaccess.artsoracle.databean.BaseOracleBean#getSelectSql()
   */
  public String getSelectSql() {
    return null;
  }

  /* (non-Javadoc)
   * @see com.chelseasystems.cs.dataaccess.artsoracle.databean.BaseOracleBean#getInsertSql()
   */
  public String getInsertSql() {
    return null;
  }

  /* (non-Javadoc)
   * @see com.chelseasystems.cs.dataaccess.artsoracle.databean.BaseOracleBean#getUpdateSql()
   */
  public String getUpdateSql() {
    return null;
  }

  /* (non-Javadoc)
   * @see com.chelseasystems.cs.dataaccess.artsoracle.databean.BaseOracleBean#getDeleteSql()
   */
  public String getDeleteSql() {
    return null;
  }

  /** (non-Javadoc)
   * @see com.chelseasystems.cs.dataaccess.artsoracle.databean.BaseOracleBean#getDatabeans(java.sql.ResultSet)
   */
  public BaseOracleBean[] getDatabeans(ResultSet rs)
      throws SQLException {
    ReportUtils.printResultSetMetadata(rs); //TODO: DEBUG
    ArrayList list = new ArrayList();
    while (rs.next()) {
      NBigDecimalValueBean bean = new NBigDecimalValueBean();
      Set keys = attrValueMap.keySet();
      Iterator iter = keys.iterator();
      while (iter.hasNext()) {
        String attr = (String)iter.next();
        System.out.println("getDataBeans: attr=" + attr + " value=" + rs.getBigDecimal(attr)); //TODO: DEBUG
        //
        // getBigDecimal() not implemented in BaseOracleBean, so native ResultSet method must be used
        //
        bean.attrValueMap.put(attr, rs.getBigDecimal(attr));
      }
      list.add(bean);
    }
    return (NBigDecimalValueBean[])list.toArray(new NBigDecimalValueBean[0]);
  }

  /* (non-Javadoc)
   * @see com.chelseasystems.cs.dataaccess.artsoracle.databean.BaseOracleBean#toList()
   */
  public List toList() {
    return null;
  }

  /**
   * put your documentation comment here
   * @param args
   */
  public static void main(String[] args) {}
}

