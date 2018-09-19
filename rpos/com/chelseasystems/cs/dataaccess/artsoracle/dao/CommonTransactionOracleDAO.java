/*
 * put your module comment here
 * formatted with JxBeauty (c) johann.langhofer@nextra.at
 */
//
// Copyright 2002, Retek Inc. All Rights Reserved.
//


package  com.chelseasystems.cs.dataaccess.artsoracle.dao;

import  com.chelseasystems.cr.database.*;
import  com.chelseasystems.cr.sos.*;
import  com.chelseasystems.cr.eos.*;
import  com.chelseasystems.cr.transaction.*;
import  com.chelseasystems.cr.store.Store;
import  com.chelseasystems.cs.dataaccess.*;
import  com.chelseasystems.cs.dataaccess.artsoracle.databean.*;
import  com.chelseasystems.cr.config.*;
import  com.chelseasystems.cs.pos.CMSCompositePOSTransaction;
import  java.sql.*;
import  java.util.*;


/**
 *
 *  CommonTransaction Data Access Object.<br>
 *  This object encapsulates all database access for CommonTransaction.<p>
 *  Below table shows the mapping between the object and the database tables and columns:<p>
 *  <table border=1>
 *    <tr><td><b>Object Attribute</b></td><td><b>Table Name</b></td><td><b>Column Name</b></td></tr>
 *    <tr><td>CreateDate</td><td>TR_TRN</td><td>TS_TRN_CRT</td></tr>
 *    <tr><td>PostDate</td><td>TR_TRN</td><td>TS_TRN_PST</td></tr>
 *    <tr><td>ProcessDate</td><td>TR_TRN</td><td>TS_TM_SRT</td></tr>
 *    <tr><td>RegisterId</td><td>TR_TRN</td><td>ID_RPSTY_TND</td></tr>
 *    <tr><td>Store</td><td>TR_TRN</td><td>ID_STR_RT</td></tr>
 *    <tr><td>SubmitDate</td><td>TR_TRN</td><td>TS_TRN_SBM</td></tr>
 *    <tr><td>TheOperator</td><td>TR_TRN</td><td>ID_OPR</td></tr>
 *    <tr><td>TrainingFlag</td><td>TR_TRN</td><td>FL_TRG_TRN</td></tr>
 *    <tr><td>VoidTransaction</td><td>TR_TRN</td><td>DE_HND_TCK</td></tr>
 *  </table>
 *
 *  @see com.chelseasystems.cr.transaction.CommonTransaction
 *  @see com.chelseasystems.cs.dataaccess.artsoracle.databean.BaseOracleBean
 *  @see com.chelseasystems.cs.dataaccess.artsoracle.databean.TrTrnOracleBean
 *
 */
public class CommonTransactionOracleDAO extends BaseOracleDAO
    implements TransactionDAO {
  private static StoreOracleDAO storeDAO = new StoreOracleDAO();
  private static EmployeeOracleDAO employeeDAO = new EmployeeOracleDAO();
  private static SosOracleDAO sosDAO = new SosOracleDAO();
  static String txnDelim = new ConfigMgr("txnnumber.cfg").getString("DELIM");

  /**
   * put your documentation comment here
   * @param object
   * @return 
   * @exception SQLException
   */
  public ParametricStatement[] getInsertSQL (CommonTransaction object) throws SQLException {
    ArrayList statements = new ArrayList();
    statements.add(new ParametricStatement(TrTrnOracleBean.insertSql, this.toTrTrnBean(object).toList()));
    if (object instanceof TransactionSOS) {
      statements.addAll(Arrays.asList(sosDAO.getInsertSQL((TransactionSOS)object)));
    }           //else if (object instanctof ....
    return  (ParametricStatement[])statements.toArray(new ParametricStatement[0]);
  }

  /**
   * put your documentation comment here
   * @param object
   * @exception SQLException
   */
  public void insert (CommonTransaction object) throws SQLException {
    execute(getInsertSQL(object));
  }

  /*
   public void update(CommonTransaction object) throws SQLException {
   List statements = new ArrayList();
   List params = this.toTrTrnBean(object).toList();
   params.add(object.getId());
   statements.add(new ParametricStatement(TrTrnOracleBean.updateSql + this.where(TrTrnOracleBean.COL_AI_TRN), params));
   if (object instanceof TransactionSOS)
   statements.addAll(Arrays.asList(sosDAO.getUpdateSQL((TransactionSOS)object)));
   this.execute((ParametricStatement[])statements.toArray(new ParametricStatement[0]));
   }
   */
  public CommonTransaction selectById (String id) throws SQLException {
    List params = new ArrayList();
    params.add(id);
    BaseOracleBean[] beans = this.query(new TrTrnOracleBean(), this.where(TrTrnOracleBean.COL_AI_TRN), params);
    if (beans == null || beans.length == 0)
      return  null;
    TrTrnOracleBean bean = (TrTrnOracleBean)beans[0];
    CommonTransaction object = this.getNewCommonTransaction(bean);
    object.doSetTheOperator(employeeDAO.selectById(bean.getIdOpr()));
    object.doSetTrainingFlag(bean.getFlTrgTrn().booleanValue());
    object.doSetStore(storeDAO.selectById(bean.getIdStrRt()));
    object.doSetPostDate(bean.getTsTrnPst());
    object.doSetSubmitDate(bean.getTsTrnSbm());
    object.doSetCreateDate(bean.getTsTrnCrt());
    object.doSetProcessDate(bean.getTsTmSrt());
    object.doSetVoidTransaction(null);          //we don't void common transcation, ie SOS for now
    object.doSetHandWrittenTicketNumber(bean.getDeHndTck());
    object.doSetRegisterId(bean.getIdRpstyTnd());
    return  object;
  }

  /**
   * put your documentation comment here
   * @param trTrnBean
   * @return 
   * @exception SQLException
   */
  private CommonTransaction getNewCommonTransaction (TrTrnOracleBean trTrnBean) throws SQLException {
    Store store = storeDAO.selectById(trTrnBean.getIdStrRt());
    if (trTrnBean.getTyTrn().equals(ArtsConstants.TRANSACTION_TYPE_SOS)) {
      sosDAO.getById(trTrnBean.getAiTrn(), store, trTrnBean.getIdRpstyTnd());
    }           //else ... for other types
    return  null;
  }

  /**
   * put your documentation comment here
   * @param object
   * @return 
   */
  private TrTrnOracleBean toTrTrnBean (CommonTransaction object) {
    TrTrnOracleBean bean = new TrTrnOracleBean();
    bean.setAiTrn(object.getId());
    bean.setIdOpr(object.getTheOperator().getId());
    bean.setTyTrn(this.getCommonTransactionType(object));
    bean.setTsTmSrt(object.getProcessDate());
    bean.setTsTrnBgn(null);
    bean.setTsTrnEnd(null);
    bean.setFlTrgTrn(object.isTrainingFlagOn());
    bean.setFlKyOfl(null);
    bean.setIdStrRt(object.getStore().getId());
    if (object instanceof TransactionSOS)
      bean.setIdRpstyTnd(((TransactionSOS)object).getRegister().getId()); 
    else 
      bean.setIdRpstyTnd(object.getRegisterId());
    bean.setTsTrnPst(object.getPostDate());
    bean.setTsTrnSbm(object.getSubmitDate());
    bean.setTsTrnCrt(object.getCreateDate());
    bean.setTsTrnPrc(null);
    bean.setTyGuiTrn(object.getTransactionType());
    if (object.getVoidTransaction() != null)
      bean.setIdVoid(object.getVoidTransaction().getId());
    bean.setDeHndTck(object.getHandWrittenTicketNumber());
    //ks: get the seq Number from the transactionId
    String trxnId = object.getId();
    int storeAndRegLength = object.getStore().getId().trim().length();
    storeAndRegLength = storeAndRegLength + ((TransactionSOS)object).getRegister().getId().trim().length();
    if (txnDelim != null && trxnId.indexOf(txnDelim) != -1) {
      storeAndRegLength = storeAndRegLength + (2*txnDelim.length());
    }
    System.out.println("TRN ID : <" + object.getId() + ">");
    System.out.println("STOREID : <" + object.getStore().getId() + ">");
    System.out.println("REGISTERID : <" + ((TransactionSOS)object).getRegister().getId() + ">");
    String seqNum = object.getId().trim().substring(storeAndRegLength);
    System.out.println("SEQ : <" + seqNum + ">");
    bean.setTrnSeqNum(new Long(seqNum));
    if (object instanceof CMSCompositePOSTransaction) {
      bean.setFiscalReceiptDate(((CMSCompositePOSTransaction)object).getFiscalReceiptDate());
      bean.setFiscalReceiptNumber(((CMSCompositePOSTransaction)object).getFiscalReceiptNumber());
    }
    return  bean;
  }

  /**
   * put your documentation comment here
   * @param object
   * @return 
   */
  public static String getCommonTransactionType (CommonTransaction object) {
    if (object instanceof TransactionSOS)
      return  ArtsConstants.TRANSACTION_TYPE_SOS;
    //if it is of other type ...
    return  ArtsConstants.TRANSACTION_TYPE_UNKNOWN;
  }

  /**
   * put your documentation comment here
   * @param storeId
   * @param registerId
   * @return 
   * @exception SQLException
   */
  public String[] selectMaxTxnId (String storeId, String registerId) throws SQLException {
    //ks: will get the maxTxnId from TRN_SEQ_NUM
    String sql = "select max(" + TrTrnOracleBean.COL_TRN_SEQ_NUM + ") AS COL_TRN_SEQ_NUM from " + TrTrnOracleBean.TABLE_NAME + " where " + TrTrnOracleBean.COL_ID_RPSTY_TND + " = ?  and " + TrTrnOracleBean.COL_ID_STR_RT + " = ?";
    List params = new ArrayList();
    params.add(0, registerId);
    params.add(1, storeId);
    String[] ids = queryForIds(sql, params);
    if (ids == null || ids.length == 0) {
      return  null;
    } 
    else {
      return  ids;
    }
  }
}



