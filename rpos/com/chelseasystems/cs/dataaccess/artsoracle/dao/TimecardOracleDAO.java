/*
 * put your module comment here
 * formatted with JxBeauty (c) johann.langhofer@nextra.at
 */
//
// Copyright 1999-2001, Chelsea Market Systems
//


package  com.chelseasystems.cs.dataaccess.artsoracle.dao;

import  com.chelseasystems.cr.database.*;
import  com.chelseasystems.cr.store.Store;
import  com.chelseasystems.cr.timecard.*;
import  com.chelseasystems.cs.timecard.*;
import  com.chelseasystems.cs.dataaccess.*;
import  com.chelseasystems.cs.dataaccess.artsoracle.databean.*;
import  com.chelseasystems.cs.employee.*;
import  com.chelseasystems.cs.store.*;
import  java.util.Date;
import  java.sql.*;
import  java.util.*;
import  com.chelseasystems.cs.util.*;


/**
 *
 *  TransactionTimecard Data Access Object.<br>
 *  This object encapsulates all database access for TransactionTimecard.<p>
 *  Below table shows the mapping between the object and the database tables and columns:<p>
 *  <table border=1>
 *    <tr><td><b>Object Attribute</b></td><td><b>Table Name</b></td><td><b>Column Name</b></td></tr>
 *    <tr><td>Employee</td><td>RK_TIMECARD</td><td>ID_EM</td></tr>
 *    <tr><td>Id</td><td>RK_TIMECARD</td><td>ID</td></tr>
 *    <tr><td>ModificationReason</td><td>RK_TIMECARD</td><td>MANAGER_REASON</td></tr>
 *    <tr><td>TheOperator</td><td>RK_TIMECARD</td><td>ID_OP</td></tr>
 *    <tr><td>TimeStamp</td><td>RK_TIMECARD</td><td>TIME_STAMP</td></tr>
 *    <tr><td>WeekEndingDate</td><td>RK_TIMECARD</td><td>WEEK_ENDING</td></tr>
 *  </table>
 *
 *  @see TransactionTimecard
 *  @see com.chelseasystems.cs.dataaccess.artsoracle.databean.BaseOracleBean
 *  @see com.chelseasystems.cs.dataaccess.artsoracle.databean.RkTimecardOracleBean
 *
 */
public class TimecardOracleDAO extends BaseOracleDAO
    implements TimecardDAO {
  private static EmployeeTimecardOracleDAO employeeTimecardDAO = new EmployeeTimecardOracleDAO();
  private static EmployeeOracleDAO employeeDAO = new EmployeeOracleDAO();
  private static TimecardBenefitOracleDAO timecardBenefitDAO = new TimecardBenefitOracleDAO();
  private static TimecardModOracleDAO timecardModDAO = new TimecardModOracleDAO();
  private static String TABLE_NAME = RkTimecardOracleBean.TABLE_NAME;
  private static String COL_TIMECARD_ID = RkTimecardOracleBean.COL_ID;
  private static String COL_TIMECARD_DELETED = RkTimecardOracleBean.COL_DELETED;
  private static String COL_TIMECARD_EMPLOYEE_ID = RkTimecardOracleBean.COL_ID_EM;
  private static String COL_TIMECARD_WEEK_ENDING = RkTimecardOracleBean.COL_WEEK_ENDING;
  private static String COL_TIMECARD_TIME_STAMP = RkTimecardOracleBean.COL_TIME_STAMP;
  private static String selectSql = RkTimecardOracleBean.selectSql;
  private static String insertSql = RkTimecardOracleBean.insertSql;
  private static String updateSql = RkTimecardOracleBean.updateSql + where(COL_TIMECARD_ID);
  private static String deleteSql = RkTimecardOracleBean.deleteSql + where(COL_TIMECARD_ID);
  private static String updateForDeleteSql = "update " + TABLE_NAME + " set " + COL_TIMECARD_DELETED + " = 1 where " + COL_TIMECARD_ID + " = ?";

  /**
   * put your documentation comment here
   * @param object
   * @return 
   * @exception SQLException
   */
  public ParametricStatement[] getInsertSQL (TransactionTimecard object) throws SQLException {
    ArrayList statements = new ArrayList();
    statements.add(new ParametricStatement(insertSql, fromObjectToBean(object).toList()));
    // Insert into the ARM_OUT_STG_TIMECARD table
    if (object instanceof TransactionTimecardIn || object instanceof TransactionTimecardOut) {
      statements.add(new ParametricStatement(ArmOutStgTimecardOracleBean.insertSql, getStagingTableBean(object).toList()));
    }
    if (object instanceof BenefitTimecardTransaction)
      statements.add(timecardBenefitDAO.getInsertSQL((BenefitTimecardTransaction)object)); 
    else if (object instanceof TransactionTimecardMod) {
      statements.add(timecardModDAO.getInsertSQL((TransactionTimecardMod)object));
      // Insert into the ARM_OUT_STG_TIMECARD_MODS table
      statements.add(new ParametricStatement(ArmOutStgTimecardModsOracleBean.insertSql, getStagingModsTableBean(object).toList()));
    }
    return  (ParametricStatement[])statements.toArray(new ParametricStatement[0]);
  }

  /**
   * put your documentation comment here
   * @param object
   * @return 
   */
  ArmOutStgTimecardOracleBean getStagingTableBean (TransactionTimecard object) {
    ArmOutStgTimecardOracleBean stgBean = new ArmOutStgTimecardOracleBean();
    stgBean.setCompanyCode(((CMSStore)object.getStore()).getCompanyCode());
    stgBean.setDataPopulationDt(new Date());
    stgBean.setEmpHrId(object.getEmployee().getExternalID());
    stgBean.setExtractedDt(null);
    stgBean.setShopCode(object.getStore().getId());
    stgBean.setStatus("0");
    stgBean.setTimestamp(object.getExactTimeStampCalendar().getTime());
    if (object instanceof TransactionTimecardIn)
      stgBean.setTrnCode("CLOCK_IN");
    if (object instanceof TransactionTimecardOut)
      stgBean.setTrnCode("CLOCK_OUT");
    stgBean.setWeekEnding(object.getWeekEndingString());
    return  stgBean;
  }

  /**
   * put your documentation comment here
   * @param object
   * @return 
   */
  ArmOutStgTimecardModsOracleBean getStagingModsTableBean (TransactionTimecard object) {
    ArmOutStgTimecardModsOracleBean stgBean = new ArmOutStgTimecardModsOracleBean();
    stgBean.setCompanyCode(((CMSStore)object.getStore()).getCompanyCode());
    stgBean.setDataPopulationDt(new Date());
    stgBean.setEmpHrId(object.getEmployee().getExternalID());
    stgBean.setExtractedDt(null);
    stgBean.setShopCode(object.getStore().getId());
    stgBean.setStatus("0");
    stgBean.setModEmpHrId(((TransactionTimecardMod)object).getTheOperator().getExternalID());
    stgBean.setModReason(((TransactionTimecardMod)object).getReason());
    stgBean.setModTimestamp(object.getExactTimeStampCalendar().getTime());
    stgBean.setWeekEnding(object.getWeekEndingString());
    TransactionTimecard oldTxn = ((TransactionTimecardMod)object).getOldTransactionTimecard();
    TransactionTimecard newTxn = ((TransactionTimecardMod)object).getNewTransactionTimecard();
    if (oldTxn != null && newTxn != null) {
      // Mod of a time
      if (oldTxn instanceof TransactionTimecardIn) {
        // Clock In mod
        stgBean.setTrnCode("CLOCK_IN_MOD");
      } 
      else {
        // Clock Out mod
        stgBean.setTrnCode("CLOCK_OUT_MOD");
      }
      stgBean.setBfTimestamp(oldTxn.getExactTimeStampCalendar().getTime());
      stgBean.setAfTimestamp(newTxn.getExactTimeStampCalendar().getTime());
    } 
    else if (oldTxn == null && newTxn != null) {
      // Add of a time
      if (newTxn instanceof TransactionTimecardIn) {
        // Clock In add
        stgBean.setTrnCode("ADD_CLOCK_IN");
      } 
      else {
        // Clock Out add
        stgBean.setTrnCode("ADD_CLOCK_OUT");
      }
      stgBean.setBfTimestamp(null);
      stgBean.setAfTimestamp(newTxn.getExactTimeStampCalendar().getTime());
    } 
    else if (oldTxn != null && newTxn == null) {
      // Delete of a time
      if (oldTxn instanceof TransactionTimecardIn) {
        // Clock In delete
        stgBean.setTrnCode("DEL_CLOCK_IN");
      } 
      else {
        // Clock Out delete
        stgBean.setTrnCode("DEL_CLOCK_OUT");
      }
      stgBean.setBfTimestamp(oldTxn.getExactTimeStampCalendar().getTime());
      stgBean.setAfTimestamp(null);
    }
    return  stgBean;
  }

  /**
   * put your documentation comment here
   * @param object
   * @return 
   * @exception SQLException
   */
  public ParametricStatement[] getUpdateForDeleteSql (TransactionTimecard object) throws SQLException {
    List list = new ArrayList();
    list.add(object.getId());
    ParametricStatement[] array = new ParametricStatement[1];
    array[0] = new ParametricStatement(updateForDeleteSql, list);
    return  array;
  }

  /**
   * put your documentation comment here
   * @param id
   * @return 
   * @exception SQLException
   */
  public TransactionTimecard selectById (String id) throws SQLException {
    String whereSql = where(COL_TIMECARD_ID);
    TransactionTimecard[] transactionTimecards = fromBeansToObjects(query(new RkTimecardOracleBean(), whereSql, id));
    if (transactionTimecards == null || transactionTimecards.length == 0)
      return  null;
    return  transactionTimecards[0];
  }

  //this method is called by EmployeeTimecardOracleDAO
  TransactionTimecard[] getByEmployeeIdAndWeekEndingDate (String employeeId, String weekEndingDate) throws SQLException {
    Map storeCache = new Hashtable();
    Map employeeCache = new Hashtable();
    Map transactionCache = new Hashtable();
    String whereSql = "where " + COL_TIMECARD_EMPLOYEE_ID + " = ? and " + COL_TIMECARD_WEEK_ENDING + " = ? and " + COL_TIMECARD_DELETED + " = 0 order by " + COL_TIMECARD_TIME_STAMP;
    List param = new ArrayList();
    param.add(employeeId);
    param.add(weekEndingDate);
    BaseOracleBean[] beans = query(new RkTimecardOracleBean(), whereSql, param);
    TransactionTimecard[] ttt = fromBeansToObjects(beans);
    return  ttt;
  }

  /**
   * put your documentation comment here
   * @return 
   */
  protected BaseOracleBean getDatabeanInstance () {
    return  new RkTimecardOracleBean();
  }

  /**
   * put your documentation comment here
   * @param beans
   * @return 
   * @exception SQLException
   */
  private TransactionTimecard[] fromBeansToObjects (BaseOracleBean[] beans) throws SQLException {
    Map storeCache = new Hashtable();
    Map employeeCache = new Hashtable();
    List list = new ArrayList();
    for (int i = 0; i < beans.length; i++) {
      if (!((RkTimecardOracleBean)beans[i]).getTyTrn().equals(TRANSACTION_TYPE_TIMECARD_MOD))
        list.add(fromBeanToObject(beans[i], storeCache, employeeCache, list));
    }
    for (int i = 0; i < beans.length; i++) {
      if (((RkTimecardOracleBean)beans[i]).getTyTrn().equals(TRANSACTION_TYPE_TIMECARD_MOD))
        list.add(fromBeanToObject(beans[i], storeCache, employeeCache, list));
    }
    return  (TransactionTimecard[])list.toArray(new TransactionTimecard[0]);
  }

  /**
   * put your documentation comment here
   * @param baseBean
   * @param storeCache
   * @param employeeCache
   * @param txnCache
   * @return 
   * @exception SQLException
   */
  private TransactionTimecard fromBeanToObject (BaseOracleBean baseBean, Map storeCache, Map employeeCache, List txnCache) throws SQLException {
    RkTimecardOracleBean bean = (RkTimecardOracleBean)baseBean;
    Store store = TimecardDAOCache.getStoreById(bean.getIdStrRt(), storeCache);
    TransactionTimecard object = getNewTransactionTimecard(bean, store, txnCache);
    if (object == null)
      return  null;
    object.doSetTheOperator(TimecardDAOCache.getEmployeeById(bean.getIdOp(), employeeCache));
    object.doSetEmployee(TimecardDAOCache.getEmployeeById(bean.getIdEm(), employeeCache));
    object.doSetId(bean.getId());
    object.doSetTimeStamp(bean.getTimeStamp());
    object.doSetWeekEndingDate(bean.getWeekEnding());
    object.doSetModificationReason(bean.getManagerReason());
    return  object;
  }

  /**
   * put your documentation comment here
   * @param object
   * @return 
   */
  private RkTimecardOracleBean fromObjectToBean (TransactionTimecard object) {
    RkTimecardOracleBean bean = new RkTimecardOracleBean();
    bean.setId(object.getId());
    bean.setIdStrRt(object.getStore().getId());
    bean.setIdOp(object.getTheOperator().getId());
    bean.setIdEm(object.getEmployee().getId());
    bean.setTyTrn(getTransactionTimecardType(object));
    bean.setTimeStamp(object.getExactTimeStampCalendar());
    bean.setWeekEnding(object.getWeekEndingString());
    bean.setDeleted(new Boolean(false));
    bean.setManagerReason(object.getModificationReason());
    return  bean;
  }

  /**
   * put your documentation comment here
   * @param object
   * @return 
   */
  private String getTransactionTimecardType (TransactionTimecard object) {
    if (object instanceof TransactionTimecardIn)
      return  TRANSACTION_TYPE_TIMECARD_IN; 
    else if (object instanceof TransactionTimecardOut)
      return  TRANSACTION_TYPE_TIMECARD_OUT; 
    else if (object instanceof BenefitTimecardTransaction)
      return  TRANSACTION_TYPE_TIMECARD_BENEFIT; 
    else if (object instanceof TransactionTimecardMod)
      return  TRANSACTION_TYPE_TIMECARD_MOD;
    return  null;
  }

  /**
   * put your documentation comment here
   * @param bean
   * @param store
   * @param txnCache
   * @return 
   * @exception SQLException
   */
  private TransactionTimecard getNewTransactionTimecard (RkTimecardOracleBean bean, Store store, List txnCache) throws SQLException {
    String timecardTypeId = bean.getTyTrn();
    TransactionTimecard transactionTimecard = null;
    if (timecardTypeId.equals(TRANSACTION_TYPE_TIMECARD_IN))
      transactionTimecard = new TransactionTimecardIn(store); 
    else if (timecardTypeId.equals(TRANSACTION_TYPE_TIMECARD_OUT))
      transactionTimecard = new TransactionTimecardOut(store); 
    else if (timecardTypeId.equals(TRANSACTION_TYPE_TIMECARD_BENEFIT))
      transactionTimecard = timecardBenefitDAO.getById(bean.getId(), store); 
    else if (timecardTypeId.equals(TRANSACTION_TYPE_TIMECARD_MOD))
      transactionTimecard = timecardModDAO.getById(bean.getId(), store, txnCache);
    return  transactionTimecard;
  }
}



