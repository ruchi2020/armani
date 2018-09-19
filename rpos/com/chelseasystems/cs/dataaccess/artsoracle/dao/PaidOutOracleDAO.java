/*
 * put your module comment here
 * formatted with JxBeauty (c) johann.langhofer@nextra.at
 */
//
// Copyright 1999-2001, Chelsea Market Systems
//


package  com.chelseasystems.cs.dataaccess.artsoracle.dao;

import  java.sql.SQLException;
import  java.util.ArrayList;
import  java.util.List;
import  com.chelseasystems.cr.database.ParametricStatement;
import  com.chelseasystems.cr.paidout.PaidOutTransaction;
import  com.chelseasystems.cr.store.Store;
import  com.chelseasystems.cr.transaction.CommonTransaction;
import  com.chelseasystems.cs.dataaccess.PaidOutDAO;
import  com.chelseasystems.cs.dataaccess.artsoracle.databean.BaseOracleBean;
import  com.chelseasystems.cs.dataaccess.artsoracle.databean.RkPaidOutOracleBean;
import  com.chelseasystems.cs.paidout.*;
import  com.chelseasystems.cs.paidout.CMSMiscPaidOut;
import com.chelseasystems.cs.dataaccess.artsoracle.databean.RkPayTrnOracleBean;
import com.chelseasystems.cs.customer.CMSCustomer;
import com.chelseasystems.cr.collection.CollectionTransaction;
import com.chelseasystems.cs.collection.CMSMiscCollection;


/**
 *
 *  PaidOutTransaction Data Access Object.<br>
 *  This object encapsulates all database access for PaidOutTransaction.<p>
 *  Below table shows the mapping between the object and the database tables and columns:<p>
 *  <table border=1>
 *    <tr><td><b>Object Attribute</b></td><td><b>Table Name</b></td><td><b>Column Name</b></td></tr>
 *    <tr><td>Amount</td><td>RK_PAID_OUT</td><td>AMOUNT</td></tr>
 *    <tr><td>Comment</td><td>RK_PAID_OUT</td><td>COMMENT</td></tr>
 *    <tr><td>Id</td><td>RK_PAID_OUT</td><td>AI_TRN</td></tr>
 *  </table>
 *
 *  @see PaidOutTransaction
 *  @see com.chelseasystems.cs.dataaccess.artsoracle.databean.BaseOracleBean
 *  @see com.chelseasystems.cs.dataaccess.artsoracle.databean.RkPaidOutOracleBean
 *
 */
public class PaidOutOracleDAO extends BaseOracleDAO
    implements PaidOutDAO {
  private static TransactionOracleDAO transactionDAO = new TransactionOracleDAO();
  private static CustomerOracleDAO customerDAO = new CustomerOracleDAO();
  private static String selectSql = RkPaidOutOracleBean.selectSql;
  private static String insertSql = RkPaidOutOracleBean.insertSql;
  private static String updateSql = RkPaidOutOracleBean.updateSql + where(RkPaidOutOracleBean.COL_AI_TRN);
  private static String deleteSql = RkPaidOutOracleBean.deleteSql + where(RkPaidOutOracleBean.COL_AI_TRN);

  /**
   * put your documentation comment here
   * @param object
   * @return
   * @exception SQLException
   */
  ParametricStatement getInsertSQL (PaidOutTransaction object) throws SQLException {
    return  (new ParametricStatement(insertSql, fromObjectToBean(object).toList()));
  }

  /**
   * put your documentation comment here
   * @param object
   * @return
   * @exception SQLException
   */
  ParametricStatement getUpdateSQL (PaidOutTransaction object) throws SQLException {
    List params = fromObjectToBean(object).toList();
    params.add(object.getId());
    return  (new ParametricStatement(updateSql, params));
  }

  /**
   * put your documentation comment here
   * @param object
   * @return
   * @exception SQLException
   */
  ParametricStatement getDeleteSQL (PaidOutTransaction object) throws SQLException {
    ArrayList params = new ArrayList();
    params.add(object.getId());
    return  (new ParametricStatement(deleteSql, params));
  }

  /**
   * put your documentation comment here
   * @param transactionId
   * @return
   * @exception SQLException
   */
  public PaidOutTransaction selectById (String transactionId) throws SQLException {
    CommonTransaction transaction = transactionDAO.selectById(transactionId);
    if (transaction == null || !(transaction instanceof PaidOutTransaction))
      return  (null);
    else
      return  (PaidOutTransaction)transaction;
  }

  /**
   * put your documentation comment here
   * @param transactionId
   * @param store
   * @return
   * @exception SQLException
   */
  PaidOutTransaction getById (String transactionId, Store store) throws SQLException {
    String whereSql = where(RkPaidOutOracleBean.COL_AI_TRN);
    PaidOutTransaction[] paidOutTransactions = fromBeansToObjects(query(new RkPaidOutOracleBean(), whereSql, transactionId), store);
    if (paidOutTransactions == null || paidOutTransactions.length == 0)
      return  (null);
    return  (paidOutTransactions[0]);
  }

  /**
   * put your documentation comment here
   * @param beans
   * @param store
   * @return
   * @exception SQLException
   */
  private PaidOutTransaction[] fromBeansToObjects (BaseOracleBean[] beans, Store store) throws SQLException {
    PaidOutTransaction[] array = new PaidOutTransaction[beans.length];
    for (int i = 0; i < array.length; i++)
      array[i] = fromBeanToObject(beans[i], store);
    return  (array);
  }

  /**
   * put your documentation comment here
   * @param baseBean
   * @param store
   * @return
   * @exception SQLException
   */
  private PaidOutTransaction fromBeanToObject (BaseOracleBean baseBean, Store store) throws SQLException {
    RkPaidOutOracleBean bean = (RkPaidOutOracleBean)baseBean;
    PaidOutTransaction object = null;
    if (bean.getType().equalsIgnoreCase("MISC_PAID_OUT")) {
      object = new CMSMiscPaidOut(store);
    }
    else if (bean.getType().equalsIgnoreCase("CASH_TRANSFER")) {
      object = new CMSCashDropPaidOut(store);
    }
    else if (bean.getType().equalsIgnoreCase("CLOSE_DEPOSIT")) {
      object = new CMSMiscPaidOut(store);
    }
    object.doSetId(bean.getAiTrn());
    object.doSetAmount(bean.getAmount());
    object.doSetComment(bean.getComments());
    return  (object);
  }

  /**
   * put your documentation comment here
   * @param object
   * @return
   */
  private RkPaidOutOracleBean fromObjectToBean (PaidOutTransaction object) {
    RkPaidOutOracleBean bean = new RkPaidOutOracleBean();
    bean.setAiTrn(object.getId());
    bean.setType(object.getType());
    bean.setAmount(object.getAmount());
    bean.setComments(object.getComment());
    return  (bean);
  }
  /**
   * put your documentation comment here
   * @param paymentTransactionBean
   * @param store
   * @return
   * @exception SQLException
   */
  PaidOutTransaction getByRkPayTrnBean(RkPayTrnOracleBean paymentTransactionBean, Store store)
      throws SQLException {
    CMSPaidOutTransaction paidOutTxn = (CMSPaidOutTransaction)getById(paymentTransactionBean.
        getAiTrn(), store);
    if (paidOutTxn instanceof CMSMiscPaidOut) {
      if (paymentTransactionBean.getCustId() != null
          && paymentTransactionBean.getCustId().trim().length() > 0)
        ((CMSMiscPaidOut)paidOutTxn).setCustomer((CMSCustomer)customerDAO.selectById(paymentTransactionBean.getCustId()));
    }
    return paidOutTxn;
  }
}



