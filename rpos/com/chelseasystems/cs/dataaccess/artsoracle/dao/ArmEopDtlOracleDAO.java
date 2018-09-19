package com.chelseasystems.cs.dataaccess.artsoracle.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import com.chelseasystems.cr.config.ConfigMgr;
import com.chelseasystems.cr.database.ParametricStatement;
import com.chelseasystems.cr.database.connection.ConnectionPool;
import com.chelseasystems.cr.sos.TransactionSOS;
import com.chelseasystems.cs.dataaccess.ArmEopDtlDAO;
import com.chelseasystems.cs.dataaccess.DAOConnectionPool;
import com.chelseasystems.cs.dataaccess.artsoracle.databean.ArmEopDtlOracleBean;
import com.chelseasystems.cs.eod.CMSTransactionEOD;
import com.chelseasystems.cs.register.CMSRegister;

/**
 * AUTHOR Ruchi Gupta : Dec 2017 : POS_TS_NF25 Implementation PCR (FRANCE)
 */
public class ArmEopDtlOracleDAO extends BaseOracleDAO implements ArmEopDtlDAO{

	HashMap hashMap = null;
	private static String insertSql = ArmEopDtlOracleBean.insertSql;
	private  String ID_CMY;  
	private  String ID_STR_RT;
	private  String REGISTER_ID;
	private  String YEAR;
	private  String MONTH;
	private  String DAY;
	private  String CD_TYPE;
	private  String TOTAL;
	private  String CURRENCY;
	private  String CREATE_DATE;
	private  CMSRegister cmsRegister = null;
	
	  static ConfigMgr jdbcConfig = new ConfigMgr("jdbc.cfg");
	  static String databaseType = jdbcConfig.getString("DATABASE");
	  static String verboseOrNot = jdbcConfig.getString("JDBC_VERBOSE");
	  static boolean dedug_mode = verboseOrNot.equals("true");
	
	/**
	 * This method returns hashmap which contains only two key value pairs.
	 * first key : "MONTH" and value : calculated net amount of month and if value is persisted then value will be "PERSISTED".
	 * second key : "YEAR" and value : calculated net amount of year and if value is persisted then value will be "PERSISTED".
	 */
	public HashMap submitAndReturnNetAmount(Object txnObject) throws Exception {
		
		if(txnObject instanceof CMSTransactionEOD){
			CMSTransactionEOD eodTxn = (CMSTransactionEOD) txnObject;
			cmsRegister = (CMSRegister) eodTxn.getRegister();
			ID_CMY=eodTxn.getStore().getCompanyId();
			ID_STR_RT=eodTxn.getStore().getId();
			REGISTER_ID=eodTxn.getRegister().getId();
			Date date = eodTxn.getProcessDate();
			Calendar calendar=Calendar.getInstance();
			calendar.setTime(date);
			MONTH = String.valueOf(calendar.get(calendar.MONTH)+1);
			YEAR  = String.valueOf(calendar.get(calendar.YEAR));
			DAY   = String.valueOf(calendar.get(calendar.DAY_OF_MONTH));
			CURRENCY = eodTxn.getStore().getCurrencyType().getCode();
			
			List params = null;
			ArmEopDtlOracleBean armEopDtlOracleBean = null;	
			params = new ArrayList();
			params.add(ID_CMY);
			params.add(ID_STR_RT);
			params.add(REGISTER_ID);

			if(cmsRegister.isYearEnd()){
				System.out.println("EOD transaction : Last day of Year");
				hashMap = new HashMap();
				String whereSql= " and "+ArmEopDtlOracleBean.COL_YEAR+" = ?"+" and "+ArmEopDtlOracleBean.COL_CD_TYPE+" = ?";	
				params.add(YEAR);
				params.add("MONTH_SALES_TOTAL");
				Double netAmountofCurrentYear = calculateNetAmount(whereSql,params);

				params.remove("MONTH_SALES_TOTAL");
			    whereSql= " and "+ArmEopDtlOracleBean.COL_YEAR+" = ?"+" and "+ArmEopDtlOracleBean.COL_MONTH+" = ?"+" and "+ArmEopDtlOracleBean.COL_CD_TYPE+" = ?";
				params.add(MONTH);
				params.add("DAY_SALES_TOTAL");
				Double netAmountOfCurrentMonth = calculateNetAmount(whereSql,params);

				netAmountofCurrentYear+=netAmountOfCurrentMonth;
				hashMap.put("MONTH", netAmountOfCurrentMonth);
				hashMap.put("YEAR", netAmountofCurrentYear);
			}else{
				System.out.println("EOD transaction : Last day of Month");
				hashMap = new HashMap();
				String whereSql= " and "+ArmEopDtlOracleBean.COL_YEAR+" = ?"+" and "+ArmEopDtlOracleBean.COL_MONTH+" = ?"+" and "+ArmEopDtlOracleBean.COL_CD_TYPE+" = ?";
				params.add(YEAR);
				params.add(MONTH);
				params.add("DAY_SALES_TOTAL");
				
				Double netAmountOfCurrentMonth = calculateNetAmount(whereSql,params);
				if(netAmountOfCurrentMonth==null || netAmountOfCurrentMonth==0.0){
					return null;
				}
				hashMap.put("MONTH", netAmountOfCurrentMonth);
			}

			
		}
		if(txnObject instanceof TransactionSOS){
			
			TransactionSOS sosTxn = (TransactionSOS) txnObject;
			cmsRegister = (CMSRegister) sosTxn.getRegister();
			ID_CMY=sosTxn.getStore().getCompanyId();
			ID_STR_RT=sosTxn.getStore().getId();
			REGISTER_ID=sosTxn.getRegister().getId();
			CURRENCY = sosTxn.getStore().getCurrencyType().getCode();
			Date date = sosTxn.getProcessDate();
			Calendar calendar=Calendar.getInstance();
			calendar.setTime(date);
		    calendar.add(Calendar.MONTH, -1);
		    calendar.set(Calendar.DAY_OF_MONTH, calendar.getActualMaximum(Calendar.DAY_OF_MONTH));
			MONTH = String.valueOf(calendar.get(calendar.MONTH)+1);
			YEAR  = String.valueOf(calendar.get(calendar.YEAR));
			hashMap = new HashMap();
			List params = null;
			params = new ArrayList();
			params.add(ID_CMY);
			params.add(ID_STR_RT);
			params.add(REGISTER_ID);
			
			if(cmsRegister.isYearEnd()){
			System.out.println("SOS transaction : Last day of year");
			boolean isMonthAmountPersisted =false;
			boolean isYearAmountPersisted = false;
			String whereSql= " and "+ArmEopDtlOracleBean.COL_YEAR+" = ?"+" and "+ArmEopDtlOracleBean.COL_CD_TYPE+" = ?";	
			params.add(YEAR);
			params.add("YEAR_SALES_TOTAL");
			double netAmountofCurrentYear = calculateNetAmount(whereSql,params);
			if(netAmountofCurrentYear>0.0){
				hashMap.put("YEAR", "PERSISTED");
				isYearAmountPersisted = true;
				}
			params.remove("YEAR_SALES_TOTAL");
		    whereSql= " and "+ArmEopDtlOracleBean.COL_YEAR+" = ?"+" and "+ArmEopDtlOracleBean.COL_MONTH+" = ?"+" and "+ArmEopDtlOracleBean.COL_CD_TYPE+" = ?";
			params.add(MONTH);
			params.add("MONTH_SALES_TOTAL");
			double netAmountOfCurrentMonth = calculateNetAmount(whereSql,params);
			if(netAmountOfCurrentMonth>0.0d){
				hashMap.put("MONTH", "PERSISTED");
				isMonthAmountPersisted = true;
			}
			if(!isMonthAmountPersisted){
				params = null;
				params = new ArrayList();
				params.add(ID_CMY);
				params.add(ID_STR_RT);
				params.add(REGISTER_ID);
				YEAR  = String.valueOf(calendar.get(calendar.YEAR));
			    whereSql= " and "+ArmEopDtlOracleBean.COL_YEAR+" = ?"+" and "+ArmEopDtlOracleBean.COL_MONTH+" = ?"+" and "+ArmEopDtlOracleBean.COL_CD_TYPE+" = ?";
				params.add(YEAR);
			    params.add(MONTH);
				params.add("DAY_SALES_TOTAL");
				netAmountOfCurrentMonth = calculateNetAmount(whereSql,params);
				if(netAmountOfCurrentMonth>0.0d){
					hashMap.put("MONTH",netAmountOfCurrentMonth);
					}
			}
			    if(!isYearAmountPersisted){
			    	params = null;
					params = new ArrayList();
					params.add(ID_CMY);
					params.add(ID_STR_RT);
					params.add(REGISTER_ID);
					params.add(YEAR);
					params.add("MONTH_SALES_TOTAL");
					 whereSql= " and "+ArmEopDtlOracleBean.COL_YEAR+" = ?"+" and "+ArmEopDtlOracleBean.COL_CD_TYPE+" = ?";	
					 netAmountofCurrentYear = calculateNetAmount(whereSql,params);
				 if(netAmountofCurrentYear>0.0d){
						netAmountofCurrentYear+=netAmountOfCurrentMonth;
						hashMap.put("YEAR",netAmountofCurrentYear);
						}else if(netAmountOfCurrentMonth>0.0d){
						netAmountofCurrentYear+=netAmountOfCurrentMonth;
						hashMap.put("YEAR",netAmountofCurrentYear);	
						}

			    }					
			}else{
				System.out.println("SOS transaction : Last day of Month");
				boolean isMonthAmountPersisted = false;
				params = null;
				params = new ArrayList();
				String whereSql= " and "+ArmEopDtlOracleBean.COL_YEAR+" = ?"+" and "+ArmEopDtlOracleBean.COL_MONTH+" = ?"+" and "+ArmEopDtlOracleBean.COL_CD_TYPE+" = ?";
				params.add(ID_CMY);
				params.add(ID_STR_RT);
				params.add(REGISTER_ID);
					params.add(YEAR);
					params.add(MONTH);
					params.add("MONTH_SALES_TOTAL");
					double netAmountOfCurrentMonth = calculateNetAmount(whereSql,params);
					if(netAmountOfCurrentMonth>0.0){
						hashMap.put("MONTH", "PERSISTED");
						isMonthAmountPersisted = true;
					}
					if(!isMonthAmountPersisted){
						params = null;
						params = new ArrayList();
						params.add(ID_CMY);
						params.add(ID_STR_RT);
						params.add(REGISTER_ID);
					    whereSql= " and "+ArmEopDtlOracleBean.COL_YEAR+" = ?"+" and "+ArmEopDtlOracleBean.COL_MONTH+" = ?"+" and "+ArmEopDtlOracleBean.COL_CD_TYPE+" = ?";
						params.add(YEAR);
					    params.add(MONTH);
						params.add("DAY_SALES_TOTAL");
						netAmountOfCurrentMonth = calculateNetAmount(whereSql,params);
						if(netAmountOfCurrentMonth>0.0){
							hashMap.put("MONTH",netAmountOfCurrentMonth);
							}
					}
					
			}
		}
		
	return hashMap;
}

	/**
	 * This method returns total calculated net amount of month or year.
	 * @param wheresql
	 * @param params
	 * @return double
	 * @throws SQLException
	 */
public  Double calculateNetAmount (String whereSql,List params) throws SQLException {
    String sql = "select " + ArmEopDtlOracleBean.COL_TOTAL + " from " + ArmEopDtlOracleBean.TABLE_NAME + " where " + ArmEopDtlOracleBean.COL_ID_CMY + " = ?"
    		+" and "+ArmEopDtlOracleBean.COL_ID_STR_RT+" = ?"+" and "+ArmEopDtlOracleBean.COL_REGISTER_ID+" = ?"
    		+whereSql;
    return  this.queryForNetAmount(sql, params);
  }

/**
 * Method maps all property of CMSTransactionEOD to ArmEopDtlOracleBean
 * @param eodTxn
 * @return
 */
private ArmEopDtlOracleBean fromObjectToBean (CMSTransactionEOD eodTxn) {
    ArmEopDtlOracleBean bean = new ArmEopDtlOracleBean();
    bean.setIdCmy(eodTxn.getStore().getCompanyId());
    bean.setIdStrRt(eodTxn.getStore().getId());
    bean.setRegisterId(eodTxn.getRegister().getId());
    Date date = eodTxn.getProcessDate();
    Calendar calendar=Calendar.getInstance();
    calendar.setTime(date);
    bean.setDay(String.valueOf(calendar.get(calendar.DAY_OF_MONTH)));
    bean.setMonth(String.valueOf(calendar.get(calendar.MONTH)+1));
    bean.setYear(String.valueOf(calendar.get(calendar.YEAR)));
    bean.setCurrency(eodTxn.getStore().getCurrencyType().getCode());
    bean.setCreateDate(new Date());
    
    return  bean;
  }

/**
 * Method maps all property of TransactionSOS to ArmEopDtlOracleBean
 * @param sosTxn
 * @return
 */
private ArmEopDtlOracleBean fromObjectToBean (TransactionSOS sosTxn) {
    ArmEopDtlOracleBean bean = new ArmEopDtlOracleBean();
    bean.setIdCmy(sosTxn.getStore().getCompanyId());
    bean.setIdStrRt(sosTxn.getStore().getId());
    bean.setRegisterId(sosTxn.getRegister().getId());
    Date date = sosTxn.getProcessDate();
    Calendar calendar=Calendar.getInstance();
    calendar.setTime(date);
    calendar.add(Calendar.MONTH, -1);
    calendar.set(Calendar.DAY_OF_MONTH, calendar.getActualMaximum(Calendar.DAY_OF_MONTH));
    bean.setDay(String.valueOf(calendar.get(calendar.DAY_OF_MONTH)));
    bean.setMonth(String.valueOf(calendar.get(calendar.MONTH)+1));
    bean.setYear(String.valueOf(calendar.get(calendar.YEAR)));
    bean.setCurrency(sosTxn.getStore().getCurrencyType().getCode());
    bean.setCreateDate(new Date());
    
    return  bean;
  }

/**
 *  Method returns insert SQL to persist  * in ARMEOPDTL table for Month or Year
 *  sales data.
 * @param object
 * @return
 * @throws SQLException
 */
ParametricStatement[] getInsertSQL(CMSTransactionEOD object)
		throws SQLException {
	ArrayList statements = new ArrayList();
	String typeCode;
	cmsRegister = (CMSRegister) object.getRegister();
	double todaysNetAmount = cmsRegister.getNetAmountOfDay();
	
	ArmEopDtlOracleBean armEopDtlOracleBean = fromObjectToBean(object);
	armEopDtlOracleBean.setCdType("DAY_SALES_TOTAL");
	armEopDtlOracleBean.setTotal(String.valueOf(todaysNetAmount));
	statements.add(new ParametricStatement(insertSql,armEopDtlOracleBean.toList()));
	
	if(((cmsRegister.isYearEnd()) && (cmsRegister.getNetAmountOfYear()==0.0d))  || 
			((cmsRegister.isYearEnd()) && (cmsRegister.getNetAmountOfMonth()==0.0d))){
		try {
			HashMap netAmountMap = submitAndReturnNetAmount(object);
			if(cmsRegister.isYearEnd()){
				Double netAmountOfMonth = (Double) netAmountMap.get("MONTH");
				((CMSRegister)object.getRegister()).setNetAmountOfMonth(netAmountOfMonth);
				Double netAmountOfYear  = (Double) netAmountMap.get("YEAR");
				((CMSRegister)object.getRegister()).setNetAmountOfYear(netAmountOfYear);
			}else{
				Double netAmountOfMonth = (Double) netAmountMap.get("MONTH");
				((CMSRegister)object.getRegister()).setNetAmountOfMonth(netAmountOfMonth);
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	if(cmsRegister.isMonthEnd()){
		ArmEopDtlOracleBean bean = fromObjectToBean(object);
		bean.setCdType("MONTH_SALES_TOTAL");
		bean.setTotal(String.valueOf(((CMSRegister)object.getRegister()).getNetAmountOfMonth()+todaysNetAmount));
		statements.add(new ParametricStatement(insertSql,bean.toList()));
	}
	if(cmsRegister.isYearEnd()){
		ArmEopDtlOracleBean bean = fromObjectToBean(object);
		bean.setCdType("YEAR_SALES_TOTAL");
		bean.setTotal(String.valueOf(((CMSRegister)object.getRegister()).getNetAmountOfYear()+todaysNetAmount));
		statements.add(new ParametricStatement(insertSql,bean.toList()));
	}
	return (ParametricStatement[]) statements.toArray(new ParametricStatement[0]);
}

/**
 * Method returns insert SQL to persist in ARMEOPDTL table for Month or Year sales data.
 * @param object
 * @return
 * @throws SQLException
 */
ParametricStatement[] getInsertSQL(TransactionSOS object)
		throws SQLException {
	ArrayList statements = new ArrayList();
	cmsRegister = (CMSRegister) object.getRegister();
	
	if(((cmsRegister.isYearEnd()) && (cmsRegister.getNetAmountOfYear()==0.0d)) || (cmsRegister.getNetAmountOfMonth()==0.0d)){
		try {
			HashMap netAmountMap = submitAndReturnNetAmount(object);
			if(!(netAmountMap.get("MONTH") instanceof String )){
			if(cmsRegister.isYearEnd()){
				Double netAmountOfMonth = (Double) netAmountMap.get("MONTH");
				((CMSRegister)object.getRegister()).setNetAmountOfMonth(netAmountOfMonth);
				Double netAmountOfYear  = (Double) netAmountMap.get("YEAR");
				((CMSRegister)object.getRegister()).setNetAmountOfYear(netAmountOfYear);
			}else{
				Double netAmountOfMonth = (Double) netAmountMap.get("MONTH");
				((CMSRegister)object.getRegister()).setNetAmountOfMonth(netAmountOfMonth);
			}
			}else{
				return null;
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	if(cmsRegister.isMonthEnd()){
		ArmEopDtlOracleBean bean = fromObjectToBean(object);
		bean.setCdType("MONTH_SALES_TOTAL");
		bean.setTotal(String.valueOf(((CMSRegister)object.getRegister()).getNetAmountOfMonth()));
		statements.add(new ParametricStatement(insertSql,bean.toList()));
	}
	if(cmsRegister.isYearEnd()){
		ArmEopDtlOracleBean bean = fromObjectToBean(object);
		bean.setCdType("YEAR_SALES_TOTAL");
		bean.setTotal(String.valueOf(((CMSRegister)object.getRegister()).getNetAmountOfYear()));
		statements.add(new ParametricStatement(insertSql,bean.toList()));
	}
	return (ParametricStatement[]) statements.toArray(new ParametricStatement[0]);
	}

/**
 * This method returns total calculated net amount of month or year.
 * @param sql
 * @param params
 * @return
 * @throws SQLException
 */
public Double queryForNetAmount (String sql, List params) throws SQLException {
    ParametricStatement parametricStatement = new ParametricStatement(sql, params);
    this.debug("QUERY FOR NET AMOUNT: ", parametricStatement);
    ConnectionPool pool = null;
    Connection connection = null;
    PreparedStatement pStatement = null;
    ArrayList list = new ArrayList();
    Double netAmount = 0.0;
    try {
      pool = DAOConnectionPool.getPool();
      connection = pool.getConnection();
      pStatement = parametricStatement.getQueryPreparedStatement(connection);
      ResultSet resultSet = pStatement.executeQuery();
      while (resultSet.next()){
    	  if(resultSet.getString(1)!=null)
				netAmount +=(Double.valueOf(resultSet.getString(1)));
      }
      pStatement.close();
    } catch (SQLException sqlExp) {
      try {
        connection = pool.renewConnection(connection);
        pStatement = parametricStatement.getQueryPreparedStatement(connection);
        ResultSet resultSet = pStatement.executeQuery();
        netAmount = 0.0;
        while (resultSet.next()){
	    	  if(resultSet.getString(1)!=null)
					netAmount +=(Double.valueOf(resultSet.getString(1)));
	      }
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
    return netAmount;
}

/**
 * Method to print error log file.
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
 * Method to convert ParametricStatement to String
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
/**
 * Method to print SQL QUERY on log file.
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
}
