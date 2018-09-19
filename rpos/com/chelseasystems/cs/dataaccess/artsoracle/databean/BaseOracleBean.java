//
// Copyright 1999-2001, Chelsea Market Systems
//
package com.chelseasystems.cs.dataaccess.artsoracle.databean;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Calendar;
import java.util.List;

import com.chelseasystems.cr.currency.ArmCurrency;
import com.chelseasystems.cr.database.DatabaseNull;
import com.chelseasystems.cs.dataaccess.artsoracle.dao.BaseOracleDAO;

public abstract class BaseOracleBean{

  public abstract String getSelectSql();
  public abstract String getInsertSql();
  public abstract String getUpdateSql();
  public abstract String getDeleteSql();
  public abstract BaseOracleBean[] getDatabeans(ResultSet resultSet) throws SQLException;
  public abstract List toList();

  protected void addToList(List list, Object obj, int sqlType) {
    if (obj == null)
      list.add(new DatabaseNull(sqlType));
    else if (obj instanceof Boolean)
      list.add(new Long(((Boolean)obj).booleanValue()?1:0));
    else if (obj instanceof ArmCurrency)
      list.add(((ArmCurrency)obj).toDelimitedString());
    else if (obj instanceof Calendar)
      list.add(BaseOracleDAO.fromCalendarToString((Calendar)obj));
    else
      list.add(obj);
  }

  protected String getStringFromResultSet(ResultSet resultSet, String columnName) throws SQLException {
    String object = resultSet.getString(columnName);
    if (resultSet.wasNull()) return null;
    return object;
  }

  protected java.util.Date getDateFromResultSet(ResultSet resultSet, String columnName) throws SQLException {
    java.util.Date object = resultSet.getTimestamp(columnName);
    if (resultSet.wasNull()) return null;
    return object;
  }

  protected Boolean getBooleanFromResultSet(ResultSet resultSet, String columnName) throws SQLException {
    Boolean object = new Boolean(resultSet.getBoolean(columnName));
    if (resultSet.wasNull()) return null;
    return object;
  }

  protected Long getLongFromResultSet(ResultSet resultSet, String columnName) throws SQLException {
    Long object = new Long(resultSet.getLong(columnName));
    if (resultSet.wasNull()) return null;
    return object;
  }

  protected Double getDoubleFromResultSet(ResultSet resultSet, String columnName) throws SQLException {
    double value = ignorePoint000001(resultSet.getDouble(columnName));
    Double object = new Double(value);
    if (resultSet.wasNull()) return null;
    return object;
  }

  protected ArmCurrency getCurrencyFromResultSet(ResultSet resultSet, String columnName) throws SQLException {
    String string = resultSet.getString(columnName);
    if (string != null)
    {
       try {
         return  ArmCurrency.valueOf(string);
       } catch (Exception ex) {
         throw  new SQLException(ex.getMessage());
       }
    }
    return  null;
  }

  protected Calendar getCalendarFromResultSet(ResultSet resultSet, String columnName) throws SQLException {
    String string = resultSet.getString(columnName);
    if (string == null) return null;
    Calendar object = null;
    object = BaseOracleDAO.fromStringToCalendar(string);
    if (resultSet.wasNull()) return null;
    return object;
  }

  static double ignorePoint000001(double x) {
      int n = 100000;
      double y = (double)Math.round(x * n) / n;
      return y;
  }

  static double ignorePoint0001(double x) {
    int n = 1000;
    double y = (double)Math.round(x * n) / n;
    return y;
  }

  static double ignorePoint00001(double x) {
    int n = 10000;
    double y = (double)Math.round(x * n) / n;
    return y;
  }


  static double ignorePoint001(double x) {
    int n = 100;
    double y = (double)Math.round(x * n) / n;
    return y;
  }

}