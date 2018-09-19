/*
 * put your module comment here
 * formatted with JxBeauty (c) johann.langhofer@nextra.at
 */
//
// Copyright 1999-2001, Chelsea Market Systems
//
//
// Copyright 1999-2001, Chelsea Market Systems
//
/*
 History:
 +------+------------+-----------+-----------+----------------------------------------------------+
 | Ver# | Date       | By        | Defect #  | Description                                        |
 +------+------------+-----------+-----------+----------------------------------------------------+
 | 2    | 04-30-2005 | Pankaja   | N/A       |To persist media count                              |
 +------+------------+-----------+-----------+----------------------------------------------------+
 */


package com.chelseasystems.cs.dataaccess.artsoracle.dao;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import com.chelseasystems.cr.currency.ArmCurrency;
import com.chelseasystems.cr.database.ParametricStatement;
import com.chelseasystems.cr.payment.Payment;
import com.chelseasystems.cr.pos.PaymentTransaction;
import com.chelseasystems.cr.pos.VoidTransaction;
import com.chelseasystems.cr.transaction.ITransaction;
import com.chelseasystems.cr.txnposter.PaymentSummary;
import com.chelseasystems.cs.txnposter.CMSPaymentSummary;
import com.chelseasystems.cs.dataaccess.PaymentSummaryDAO;
import com.chelseasystems.cs.dataaccess.artsoracle.databean.BaseOracleBean;
import com.chelseasystems.cs.dataaccess.artsoracle.databean.RkPaySumOracleBean;
import com.chelseasystems.cs.payment.CMSForeignCash;


/**
 *
 *  PaymentSummary Data Access Object.<br>
 *  This object encapsulates all database access for PaymentSummary.<p>
 *  Below table shows the mapping between the object and the database tables and columns:<p>
 *  <table border=1>
 *    <tr><td><b>Object Attribute</b></td><td><b>Table Name</b></td><td><b>Column Name</b></td></tr>
 *    <tr><td>Date</td><td>RK_PAYMENT_SUMMARY</td><td>DATE</td></tr>
 *    <tr><td>EmployeeId</td><td>RK_PAYMENT_SUMMARY</td><td>ID_EM</td></tr>
 *    <tr><td>Id</td><td>RK_PAYMENT_SUMMARY</td><td>ID</td></tr>
 *    <tr><td>PaymentTotal</td><td>RK_PAYMENT_SUMMARY</td><td>PAYMENT_TOTAL</td></tr>
 *    <tr><td>PaymentType</td><td>RK_PAYMENT_SUMMARY</td><td>PAYMENT_TYPE</td></tr>
 *    <tr><td>RegisterId</td><td>RK_PAYMENT_SUMMARY</td><td>REGISTER_ID</td></tr>
 *    <tr><td>StoreId</td><td>RK_PAYMENT_SUMMARY</td><td>ID_STR_RT</td></tr>
 *  </table>
 *
 *  @see PaymentSummary
 *  @see com.chelseasystems.cs.dataaccess.artsoracle.databean.BaseOracleBean
 *  @see com.chelseasystems.cs.dataaccess.artsoracle.databean.RkPaySumOracleBean
 *
 */
public class PaymentSummaryOracleDAO extends BaseOracleDAO implements PaymentSummaryDAO {
  private static String selectSql = RkPaySumOracleBean.selectSql;
  private static String insertSql = RkPaySumOracleBean.insertSql;
  private static String updateSql = RkPaySumOracleBean.updateSql + where(RkPaySumOracleBean.COL_ID);
  private static String deleteSql = RkPaySumOracleBean.deleteSql + where(RkPaySumOracleBean.COL_ID);

  /**
   * put your documentation comment here
   * @param object
   * @exception SQLException
   */
  public void insert(PaymentSummary object)
      throws SQLException {
    object.doSetId(this.getNextChelseaId());
    this.execute(new ParametricStatement[] {
        new ParametricStatement(insertSql, fromObjectToBean(object).toList())
    });
  }

  /**
   * put your documentation comment here
   * @param object
   * @exception SQLException
   */
  public void update(PaymentSummary object)
      throws SQLException {
    List params = fromObjectToBean(object).toList();
    params.add(object.getId());
    this.execute(new ParametricStatement[] {
        new ParametricStatement(updateSql, params)
    });
  }

  /**
   * put your documentation comment here
   * @param date
   * @param storeId
   * @return
   * @exception SQLException
   */
  public PaymentSummary[] selectByDateStoreId(Date date, String storeId)
      throws SQLException {
    String whereSql = where(RkPaySumOracleBean.COL_PAYMENT_DATE, RkPaySumOracleBean.COL_ID_STR_RT);
    ArrayList params = new ArrayList();
    params.add(date);
    params.add(storeId);
    return fromBeansToObjects(query(new RkPaySumOracleBean(), whereSql, params));
  }

  /**
   * put your documentation comment here
   * @param date
   * @param storeId
   * @param paymentType
   * @return
   * @exception SQLException
   */
  public PaymentSummary[] selectByDateStoreIdPaymentType(Date date, String storeId
      , String paymentType)
      throws SQLException {
    String whereSql = where(RkPaySumOracleBean.COL_PAYMENT_DATE, RkPaySumOracleBean.COL_ID_STR_RT
        , RkPaySumOracleBean.COL_PAYMENT_TYPE);
    ArrayList params = new ArrayList();
    params.add(date);
    params.add(storeId);
    params.add(paymentType);
    return fromBeansToObjects(query(new RkPaySumOracleBean(), whereSql, params));
  }

  /**
   * put your documentation comment here
   * @param paymentTransaction
   * @return
   * @exception SQLException
   */
  public ParametricStatement[] createForPaymentTransaction(PaymentTransaction paymentTransaction)
      throws SQLException {
    int multiplier = 1;
    Date date = paymentTransaction.getProcessDate();
    if (paymentTransaction instanceof VoidTransaction) {
      VoidTransaction trans = (VoidTransaction)paymentTransaction;
      ITransaction originalTrans = trans.getOriginalTransaction();
      if (originalTrans instanceof PaymentTransaction) {
        multiplier = -1;
        paymentTransaction = (PaymentTransaction)originalTrans;
      } else {
        // Do nothing if original was not a payment transaction.
        return new ParametricStatement[0];
      }
    }
    ArrayList parametricStatements = new ArrayList();
    String storeId = paymentTransaction.getStore().getId();
    String operatorId = paymentTransaction.getTheOperator().getId();
    //    String registerId = paymentTransaction.getId();
    //    int registerIdIndex = registerId.indexOf('*') + 1;
    //    registerId = registerId.substring(registerIdIndex, registerId.indexOf('*', registerIdIndex));
    String registerId = paymentTransaction.getRegisterId();
    PaymentSummary[] currentPaymentSummaries = this.selectByDateStoreId(date, storeId);
    List newPaymentSummaries = new ArrayList();
    Payment[] payments = paymentTransaction.getPaymentsArray();
    for (int paymentIndex = 0; paymentIndex < payments.length; paymentIndex++) {
      ArmCurrency paymentAmount = payments[paymentIndex].getAmount().multiply(multiplier);
      String paymentType = payments[paymentIndex].getTransactionPaymentName();
      if (payments[paymentIndex] instanceof CMSForeignCash)
        paymentType = ((CMSForeignCash)payments[paymentIndex]).getEODTenderType();
      boolean found = false;
      //to find if this payment type is already in the database
      for (int summaryIndex = 0; summaryIndex < currentPaymentSummaries.length; summaryIndex++) {
        if (currentPaymentSummaries[summaryIndex].getEmployeeId().equals(operatorId)
            && currentPaymentSummaries[summaryIndex].getRegisterId().equals(registerId)
            && currentPaymentSummaries[summaryIndex].getPaymentType().equals(paymentType)) {
          found = true;
          try {
            currentPaymentSummaries[summaryIndex].doSetPaymentTotal(paymentAmount.add(
                currentPaymentSummaries[summaryIndex].getPaymentTotal()));
            int mediaCount = ((CMSPaymentSummary)currentPaymentSummaries[summaryIndex]).
                getMediaCount();
            ((CMSPaymentSummary)currentPaymentSummaries[summaryIndex]).doSetMediaCount(mediaCount
                + 1);
          } catch (Exception ex) {
            // ignore??
          }
          // work-around for cloudscape date truncation problem.
          currentPaymentSummaries[summaryIndex].doSetDate(date);
          List list = fromObjectToBean(currentPaymentSummaries[summaryIndex]).toList();
          list.add(currentPaymentSummaries[summaryIndex].getId());
          parametricStatements.add(new ParametricStatement(updateSql, list));
          break;
        }
      }
      //to find if it is already in the newPaymentSummaries List
      if (!found) {
        PaymentSummary[] newPaymentSummaryArray = (PaymentSummary[])newPaymentSummaries.toArray(new
            PaymentSummary[0]);
        for (int summaryIndex = 0; summaryIndex < newPaymentSummaryArray.length; summaryIndex++) {
          if (newPaymentSummaryArray[summaryIndex].getEmployeeId().equals(operatorId)
              && newPaymentSummaryArray[summaryIndex].getRegisterId().equals(registerId)
              && newPaymentSummaryArray[summaryIndex].getPaymentType().equals(paymentType)) {
            found = true;
            try {
              ArmCurrency newTotal = paymentAmount.add(newPaymentSummaryArray[summaryIndex].
                  getPaymentTotal());
              int mediaCount = ((CMSPaymentSummary)newPaymentSummaryArray[summaryIndex]).
                  getMediaCount();
              newPaymentSummaryArray[summaryIndex].doSetPaymentTotal(newTotal);
              ((CMSPaymentSummary)newPaymentSummaryArray[summaryIndex]).doSetMediaCount(mediaCount
                  + 1);
            } catch (Exception ex) {
              // ignore??
            }
          }
        }
      }
      if (!found) {
        CMSPaymentSummary paymentSummary = new CMSPaymentSummary();
        paymentSummary.doSetDate(date);
        paymentSummary.doSetEmployeeId(operatorId);
        paymentSummary.doSetStoreId(storeId);
        paymentSummary.doSetRegisterId(registerId);
        paymentSummary.doSetPaymentTotal(paymentAmount);
        if (payments[paymentIndex] instanceof CMSForeignCash)
          paymentType = ((CMSForeignCash)payments[paymentIndex]).getEODTenderType();
        paymentSummary.doSetPaymentType(paymentType);
        paymentSummary.doSetId(this.getNextChelseaId());
        paymentSummary.doSetMediaCount(paymentSummary.getMediaCount() + 1);
        newPaymentSummaries.add(paymentSummary);
      }
    }
    for (int i = 0; i < newPaymentSummaries.size(); i++) {
      PaymentSummary paymentSummary = (PaymentSummary)newPaymentSummaries.get(i);
      parametricStatements.add(new ParametricStatement(insertSql
          , fromObjectToBean(paymentSummary).toList()));
    }
    return (ParametricStatement[])parametricStatements.toArray(new ParametricStatement[
        parametricStatements.size()]);
  }

  /**
   * put your documentation comment here
   * @return
   */
  protected BaseOracleBean getDatabeanInstance() {
    return new RkPaySumOracleBean();
  }

  /**
   * put your documentation comment here
   * @param beans
   * @return
   * @exception SQLException
   */
  private PaymentSummary[] fromBeansToObjects(BaseOracleBean[] beans)
      throws SQLException {
    PaymentSummary[] array = new PaymentSummary[beans.length];
    for (int i = 0; i < array.length; i++)
      array[i] = fromBeanToObject(beans[i]);
    return array;
  }

  /**
   * put your documentation comment here
   * @param baseBean
   * @return
   * @exception SQLException
   */
  private PaymentSummary fromBeanToObject(BaseOracleBean baseBean)
      throws SQLException {
    RkPaySumOracleBean bean = (RkPaySumOracleBean)baseBean;
    CMSPaymentSummary object = new CMSPaymentSummary();
    object.doSetId(bean.getId());
    object.doSetDate(bean.getPaymentDate());
    object.doSetEmployeeId(bean.getIdEm());
    object.doSetStoreId(bean.getIdStrRt());
    object.doSetRegisterId(bean.getRegisterId());
    object.doSetPaymentType(bean.getPaymentType());
    object.doSetPaymentTotal(bean.getPaymentTotal());
    object.doSetMediaCount(bean.getMediaCount().intValue());
    return object;
  }

  /**
   * put your documentation comment here
   * @param object
   * @return
   */
  private RkPaySumOracleBean fromObjectToBean(PaymentSummary object) {
    RkPaySumOracleBean bean = new RkPaySumOracleBean();
    bean.setId(object.getId());
    bean.setPaymentDate(object.getDate());
    bean.setIdEm(object.getEmployeeId());
    bean.setIdStrRt(object.getStoreId());
    bean.setRegisterId(object.getRegisterId());
    bean.setPaymentType(object.getPaymentType());
    bean.setPaymentTotal(object.getPaymentTotal());
    bean.setMediaCount(((CMSPaymentSummary)object).getMediaCount());
    return bean;
  }
}


