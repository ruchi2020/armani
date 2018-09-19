/*
 * put your module comment here
 * formatted with JxBeauty (c) johann.langhofer@nextra.at
 */
//
// Copyright 1999-2002, Retek Inc.
//


package  com.chelseasystems.cs.dataaccess.artsoracle.dao;

import  java.awt.Color;
import  java.sql.Connection;
import  java.sql.PreparedStatement;
import  java.sql.ResultSet;
import  java.sql.SQLException;
import  java.sql.Timestamp;
import  java.util.ArrayList;
import  java.util.Calendar;
import  java.util.Date;
import  java.util.Iterator;
import  java.util.List;
import  java.util.TimeZone;
import  com.chelseasystems.cr.config.ConfigMgr;
import  com.chelseasystems.cr.currency.ArmCurrency;
import com.chelseasystems.cr.currency.CurrencyException;
import  com.chelseasystems.cr.currency.UnsupportedCurrencyTypeException;
import  com.chelseasystems.cr.database.DatabaseNull;
import  com.chelseasystems.cr.database.ParametricStatement;
import  com.chelseasystems.cr.database.connection.ConnectionPool;
import  com.chelseasystems.cs.dataaccess.DAOConnectionPool;
import  com.chelseasystems.cs.dataaccess.artsoracle.databean.BaseOracleBean;
//New package added for presale issue US
import com.chelseasystems.cs.database.CMSParametricStatement;
import  com.chelseasystems.cs.util.CaseSensitiveString;


/**
 * put your documentation comment here
 */
public abstract class BaseOracleDAO
    implements ArtsConstants {
  static ConfigMgr jdbcConfig = new ConfigMgr("jdbc.cfg");
  static String databaseType = jdbcConfig.getString("DATABASE");
  static String verboseOrNot = jdbcConfig.getString("JDBC_VERBOSE");
  static boolean dedug_mode = verboseOrNot.equals("true");

  /**
   * put your documentation comment here
   * @return 
   * @exception SQLException
   */
  public String getNextChelseaId () throws SQLException {
    if (databaseType.equals("ORACLE"))
      return  getNextOracleChelseaId(); 
    else if (databaseType.equals("CLOUDSCAPE"))
      return  getNextCloudscapeChelseaId(); 
    else 
      throw  new SQLException("Undefined database type in config file jdbc.cfg");
  }

  /**
   * put your documentation comment here
   * @return 
   * @exception SQLException
   */
  private String getNextOracleChelseaId () throws SQLException {
    String sql = "select ChelseaID.nextVal from dual";
    String[] ids = this.queryForIds(sql, null);
    if (ids == null || ids.length != 1)
      return  null;
    return  ids[0];
  }

  /**
   * put your documentation comment here
   * @return 
   * @exception SQLException
   */
  private String getNextCloudscapeChelseaId () throws SQLException {
    String createIdSql = "insert into chelsea_id (DUMMY) values ('X')";
    String selectIdSql = "select id from chelsea_id";
    String deleteIdSql = "delete from chelsea_id";
    new ParametricStatement(createIdSql, null).execute(DAOConnectionPool.getPool());
    String[] ids = this.queryForIds(selectIdSql, null);
    new ParametricStatement(deleteIdSql, null).execute(DAOConnectionPool.getPool());
    if (ids == null || ids.length != 1)
      return  null;
    return  ids[0];
  }

  /**
   * put your documentation comment here
   * @param sql
   * @return 
   */
  public ParametricStatement toUpperCase (ParametricStatement sql) {
    if (sql == null)
      return  null;
    //Added for presale issue US START
    //Now it is inserting all presale credit card number in mixed case(encrypted form) before it was saving that in Upper case
    if (sql instanceof CMSParametricStatement && ((CMSParametricStatement)sql).isCaseSensitive())
    	return sql;
    //---end---

    if (sql.getParameters() != null) {
      ArrayList list = new ArrayList(sql.getParameters());
      for (int i = 0; i < list.size(); i++) {
        if (list.get(i) != null && list.get(i) instanceof CaseSensitiveString) {
          list.set(i, ((CaseSensitiveString)list.get(i)).toString());
        } 
        else {
          if (list.get(i) != null && list.get(i) instanceof String) {
            list.set(i, ((String)list.get(i)).toUpperCase());
          }
        }
      }
      return  new ParametricStatement(sql.getSqlStatement(), list);
    } 
    else 
      return  sql;
  }

  /**
   * put your documentation comment here
   * @param statements
   * @return 
   */
  public ParametricStatement[] toUpperCase (ParametricStatement[] statements) {
    if (statements == null)
      return  null;
    for (int i = 0; i < statements.length; i++) {
      statements[i] = toUpperCase(statements[i]);
    }
    return  statements;
  }

  /**
   * put your documentation comment here
   * @param statements
   * @return 
   */
  public List toUpperCase (List statements) {
    if (statements == null)
      return  null;
    for (int i = 0; i < statements.size(); i++) {
      if (statements.get(i) instanceof ParametricStatement)
        statements.set(i, toUpperCase((ParametricStatement)statements.get(i)));
    }
    return  statements;
  }

  /**
   * put your documentation comment here
   * @param sql
   * @exception SQLException
   */
  public void execute (ParametricStatement sql) throws SQLException {
    executeCaseSensitive(toUpperCase(sql));
  }

  /**
   * put your documentation comment here
   * @param statements
   * @exception SQLException
   */
  public void execute (ParametricStatement[] statements) throws SQLException {
    executeCaseSensitive(toUpperCase(statements));
  }

  /**
   * put your documentation comment here
   * @param sql
   * @exception SQLException
   */
  public void executeCaseSensitive (ParametricStatement sql) throws SQLException {
    try {
      sql.execute(DAOConnectionPool.getPool());
    } catch (SQLException se) {
      this.printError(se, sql);
      System.out.println("SQLException -> " + se);
      se.printStackTrace();
      throw  se;
    }
  }

  /**
   * put your documentation comment here
   * @param statements
   * @exception SQLException
   */
  public void executeCaseSensitive (ParametricStatement[] statements) throws SQLException {
    Connection connection = null;
    PreparedStatement pStatement = null;
    ConnectionPool connectionPool = DAOConnectionPool.getPool();
    try {
      connection = connectionPool.getConnection();
	  //For US
      //this.currentDateTime("Printing time before inserts   :");
      for (int i = 0; i < statements.length; i++) {
    	this.debug("Executing[" + i + "] : ", statements[i]);
        pStatement = this.setupPreparedStatement(connection, statements[i]);
        pStatement.execute();
        pStatement.close();
      }
      connection.commit();
      //this.currentDateTime("Printing time after inserts  commit :");
    } catch (SQLException exp) {
      try {
        connection.rollback();
      } catch (SQLException e) {}
      try {
        connection = connectionPool.renewConnection(connection);
        for (int i = 0; i < statements.length; i++) {
          try {
            this.debug("Executing2[" + i + "] : ", statements[i]);
            pStatement = this.setupPreparedStatement(connection, statements[i]);
            pStatement.execute();
            pStatement.close();
          } catch (SQLException sqlExp) {
            this.printError(sqlExp, statements[i]);
            throw  sqlExp;
          }
        }
        connection.commit();
      } catch (SQLException se) {
        pStatement.close();
        connection.rollback();
        connectionPool.releaseConnection(connection);
        throw  se;
      }
    } finally {
      connectionPool.releaseConnection(connection);
    }
  }


  /**
   * put your documentation comment here
   * @param bean
   * @param sql
   * @param oneParam
   * @return 
   * @exception SQLException
   */
  public BaseOracleBean[] query (BaseOracleBean bean, String sql, Object oneParam) throws SQLException {
    List params = new ArrayList(1);
    params.add(oneParam);
    return  query(bean, sql, params);
  }

  /**
   * put your documentation comment here
   * @param bean
   * @param sql
   * @param params
   * @return 
   * @exception SQLException
   */
  public BaseOracleBean[] query (BaseOracleBean bean, String sql, List params) throws SQLException {
    if (sql.trim().toUpperCase().indexOf("SELECT ") != 0)
      sql = bean.getSelectSql() + " " + sql;
    ParametricStatement parametricStatement = new ParametricStatement(sql, params);
    this.debug("QUERY: ", parametricStatement);
    ConnectionPool pool = null;
    Connection connection = null;
    PreparedStatement pStatement = null;
    BaseOracleBean[] resultBeans = null;
    try {
		//For US
      //this.currentDateTime("Printing time before select:");
      pool = DAOConnectionPool.getPool();
      connection = pool.getConnection();
      pStatement = parametricStatement.getQueryPreparedStatement(connection);
      ResultSet resultSet = pStatement.executeQuery();
      resultBeans = bean.getDatabeans(resultSet);
     // this.currentDateTime("Printing time after select:");
      pStatement.close();
    } catch (SQLException sqle) {
      try {
        if (pool != null) {
          connection = pool.renewConnection(connection);
          pStatement = parametricStatement.getQueryPreparedStatement(connection);
          ResultSet resultSet = pStatement.executeQuery();
          resultBeans = bean.getDatabeans(resultSet);
          pStatement.close();
        } 
        else {
          throw  sqle;
        }
      } catch (SQLException sqlex) {
        this.printError(sqlex, parametricStatement);
        System.out.println("SQLException -> " + sqlex);
        sqlex.printStackTrace();
        throw  sqlex;
      }
    } finally {
      if (pool != null)
        pool.releaseConnection(connection);
    }
    return  resultBeans;
  }

  /**
   * put your documentation comment here
   * @param sql
   * @param params
   * @return 
   * @exception SQLException
   */
  public String[] queryForIds (String sql, List params) throws SQLException {
    ParametricStatement parametricStatement = new ParametricStatement(sql, params);
    this.debug("QUERY FOR IDS: ", parametricStatement);
    ConnectionPool pool = null;
    Connection connection = null;
    PreparedStatement pStatement = null;
    ArrayList list = new ArrayList();
    try {
      pool = DAOConnectionPool.getPool();
      connection = pool.getConnection();
      pStatement = parametricStatement.getQueryPreparedStatement(connection);
	  //For US only
     // this.currentDateTime("Printing time before QUERY FOR IDS::");
      ResultSet resultSet = pStatement.executeQuery();
      while (resultSet.next())
        list.add(resultSet.getString(1));
      //this.currentDateTime("Printing time after QUERY FOR IDS::");
      pStatement.close();
    } catch (SQLException sqlExp) {
      try {
        connection = pool.renewConnection(connection);
        pStatement = parametricStatement.getQueryPreparedStatement(connection);
        ResultSet resultSet = pStatement.executeQuery();
        list.clear();
        while (resultSet.next())
          list.add(resultSet.getString(1));
        pStatement.close();
      } catch (SQLException exp) {
        this.printError(exp, parametricStatement);
        System.out.println("SQLException -> " + exp);
        exp.printStackTrace();
        pool.releaseConnection(connection);
        throw  exp;
      }
    } finally {
      pool.releaseConnection(connection);
    }
    return  (String[])list.toArray(new String[0]);
  }

  /**
   * This method will generate a "where" clause fragment given a column name.
   * Ex. "where COLUMN_NAME = ?"
   *
   * @param columnName
   *            the specified column name
   * @return a "where" clause fragment given a column name
   */
  public static String where (String columnName) {
    return  "where " + columnName + " = ?";
  }

  /**
   * This method will generate a "where" clause fragment given a column name.
   * Ex. "where COLUMN_NAME = ?"
   *
   * @param columnName1
   *            a specified column name
   * @param columnName2
   *            a specified column name
   * @return a "where" clause fragment given a column name
   */
  public static String where (String columnName1, String columnName2) {
    return  BaseOracleDAO.where(new String[] {
      columnName1, columnName2
    });
  }

  /**
   * This method will generate a "where" clause fragment given a column name.
   * Ex. "where COLUMN_NAME = ?"
   *
   * @param columnName1
   *            a specified column name
   * @param columnName2
   *            a specified column name
   * @param columnName3
   *            a specified column name
   * @return a "where" clause fragment given a column name
   */
  public static String where (String columnName1, String columnName2, String columnName3) {
    return  BaseOracleDAO.where(new String[] {
      columnName1, columnName2, columnName3
    });
  }

  /**
   * This method will generate a "where" clause fragment given some column
   * names. Ex. "where COLUMN_NAME1 = ? and COLUMN_NAME2 = ?"
   *
   * @param columnNames
   *            the specified column names
   * @return a "where" clause fragment given some column names
   */
  public static String where (String[] columnNames) {
    StringBuffer buff = new StringBuffer();
    buff.append("where ");
    for (int i = 0; i < columnNames.length; i++) {
      buff.append(columnNames[i]);
      buff.append(" = ?");
      if (i < columnNames.length - 1)
        buff.append(" and ");
    }
    return  buff.toString();
  }

  /**
   * put your documentation comment here
   * @param string
   * @return 
   * @exception UnsupportedCurrencyTypeException
   */
  static public ArmCurrency fromStringToCurrency (String string) throws UnsupportedCurrencyTypeException {
    return  ArmCurrency.valueOf(string);
  }

  /**
   * put your documentation comment here
   * @param currency
   * @return 
   */
  static public String fromCurrencyToString (ArmCurrency currency) {
    if (currency == null)
      return  null;
    return  currency.toDelimitedString();
  }

  /**
   * put your documentation comment here
   * @param string
   * @return 
   */
  static public Color fromStringToColor (String string) {
    if (string == null)
      return  null;
    int rgb = new Integer(string).intValue();
    return  new Color(rgb);
  }

  /**
   * put your documentation comment here
   * @param color
   * @return 
   */
  static public String fromColorToString (Color color) {
    if (color == null)
      return  null;
    return  "" + color.getRGB();
  }

  /**
   * put your documentation comment here
   * @param string
   * @return 
   */
  static public Calendar fromStringToCalendar (String string) {
    int indexOfAt = string.indexOf("@");
    long time = new Long(string.substring(0, indexOfAt)).longValue();
    TimeZone timeZone = TimeZone.getTimeZone(string.substring(indexOfAt + 1));
    Calendar calendar = Calendar.getInstance(timeZone);
    calendar.setTime(new Date(time));
    return  calendar;
  }

  /**
   * put your documentation comment here
   * @param calendar
   * @return 
   */
  static public String fromCalendarToString (Calendar calendar) {
    return  "" + calendar.getTime().getTime() + "@" + calendar.getTimeZone().getID();
  }

  /**
   * put your documentation comment here
   * @param connection
   * @param statement
   * @return 
   * @exception SQLException
   */
  private PreparedStatement setupPreparedStatement (Connection connection, ParametricStatement statement) throws SQLException {
    PreparedStatement pStatement = connection.prepareStatement(statement.getSqlStatement());
    List param = statement.getParameters();
    if (param != null && param.size() > 0) {
      for (int i = 0; i < param.size(); i++) {
        Object obj = param.get(i);
        if (obj instanceof DatabaseNull) {
          pStatement.setNull(i + 1, ((DatabaseNull)obj).getType());
        } 
        else {
          if (obj instanceof java.util.Date) {
            obj = new Timestamp(((java.util.Date)obj).getTime());
          }
          pStatement.setObject(i + 1, obj);
        }
      }
    }
    return  pStatement;
  }

  /**
   * put your documentation comment here
   * @param prefix
   * @param statment
   */
  private void debug (String prefix, ParametricStatement statment) {
    try {
      if (dedug_mode) {
        System.out.println(prefix + this.parametricStatementToString(statment));
      }
    } catch (Throwable throwable) {
      System.out.println("" + new Date() + " BaseOracleDAO.debug --> " + throwable);
      System.err.println("" + new Date() + " BaseOracleDAO.debug --> " + throwable);
      throwable.printStackTrace();
    }
  }

  /**
   * put your documentation comment here
   * @param exp
   * @param statment
   */
  private void printError (Exception exp, ParametricStatement statment) {
    try {
      String prefix = "SQL Fail@" + new Date() + ": ";
      System.out.println(prefix + " Exception --> " + exp);
      System.out.println(prefix + this.parametricStatementToString(statment));
    } catch (Throwable throwable) {
      System.err.println("" + new Date() + " BaseOracleDAO.printError --> " + throwable);
      throwable.printStackTrace();
    }
  }

  /**
   * put your documentation comment here
   * @param statement
   * @return 
   */
  private String parametricStatementToString (ParametricStatement statement) {
    try {
      if (statement == null) {
        return  "NULL";
      }
      String sql = statement.getSqlStatement();
      List params = statement.getParameters();
      if (sql == null) {
        return  "[NULL SQL statement String]";
      }
      StringBuffer sb = new StringBuffer(sql);
      if (params != null) {
        for (Iterator it = params.iterator(); it.hasNext();) {
          Object next = it.next();
          if (next != null) {
            sb.append("[" + next.toString() + "]");
          } 
          else {
            sb.append("[NULL]");
          }
        }
      }
      sb.append("\n");
      return  sb.toString();
    } catch (Throwable throwable) {
      throwable.printStackTrace();
      return  "SQL not printable.";
    }
  }
  //Ruchi
  private void currentDateTime(String prefix){
	  //System.out.println("Inside currentDateTime  "+new Timestamp(System.currentTimeMillis()));
	  try {
		  if (dedug_mode) {
	        System.out.println(prefix + new Timestamp(System.currentTimeMillis()));
	      }
	    } catch (Throwable throwable) {
	      System.out.println("" + new Timestamp(System.currentTimeMillis()) + " BaseOracleDAO.debug --> " + throwable);
	      System.err.println("" + new Timestamp(System.currentTimeMillis()) + " BaseOracleDAO.debug --> " + throwable);
	      throwable.printStackTrace();
	    }
	  
	  //return new Timestamp(System.currentTimeMillis());
  }
}





