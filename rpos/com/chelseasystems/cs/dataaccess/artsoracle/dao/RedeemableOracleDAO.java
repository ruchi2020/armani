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
import  com.chelseasystems.cr.payment.DueBill;
import  com.chelseasystems.cr.payment.DueBillIssue;
import  com.chelseasystems.cr.payment.GiftCert;
import  com.chelseasystems.cr.payment.Redeemable;
import  com.chelseasystems.cr.payment.StoreValueCard;
import  com.chelseasystems.cs.dataaccess.artsoracle.databean.BaseOracleBean;
import  com.chelseasystems.cs.dataaccess.artsoracle.databean.TrLtmGfCfTndOracleBean;
import  com.chelseasystems.cs.payment.CMSStoreValueCard;
import  com.chelseasystems.cs.payment.CMSDueBill;
import  com.chelseasystems.cs.payment.CMSDueBillIssue;
import  com.chelseasystems.cs.payment.HouseAccount;


/**
 *
 *  Redeemable Data Access Object.<br>
 *  This object encapsulates all database access for Redeemable.<p>
 *  Below table shows the mapping between the object and the database tables and columns:<p>
 *  <table border=1>
 *    <tr><td><b>Object Attribute</b></td><td><b>Table Name</b></td><td><b>Column Name</b></td></tr>
 *    <tr><td>AuditNote</td><td>TR_LTM_GF_CF_TND</td><td>AUDIT_NOTE</td></tr>
 *    <tr><td>ControlNum</td><td>TR_LTM_GF_CF_TND</td><td>CONTROL_NUMBER</td></tr>
 *    <tr><td>CreateDate</td><td>TR_LTM_GF_CF_TND</td><td>CREATE_DATE</td></tr>
 *    <tr><td>FirstName</td><td>TR_LTM_GF_CF_TND</td><td>FIRST_NAME</td></tr>
 *    <tr><td>Id</td><td>TR_LTM_GF_CF_TND</td><td>ID_NMB_SRZ_GF_CF</td></tr>
 *    <tr><td>IssueAmount</td><td>TR_LTM_GF_CF_TND</td><td>ISSUE_AMOUNT</td></tr>
 *    <tr><td>LastName</td><td>TR_LTM_GF_CF_TND</td><td>LAST_NAME</td></tr>
 *    <tr><td>PhoneNumber</td><td>TR_LTM_GF_CF_TND</td><td>PHONE_NUMBER</td></tr>
 *    <tr><td>Type</td><td>TR_LTM_GF_CF_TND</td><td>TYPE</td></tr>
 *  </table>
 *
 *  @see Redeemable
 *  @see com.chelseasystems.cs.dataaccess.artsoracle.databean.BaseOracleBean
 *  @see com.chelseasystems.cs.dataaccess.artsoracle.databean.TrLtmGfCfTndOracleBean
 *
 */
public class RedeemableOracleDAO extends BaseOracleDAO {
  private static String selectSql = TrLtmGfCfTndOracleBean.selectSql;
  private static String insertSql = TrLtmGfCfTndOracleBean.insertSql;
  private static String updateSql = TrLtmGfCfTndOracleBean.updateSql + where(TrLtmGfCfTndOracleBean.COL_AI_TRN, TrLtmGfCfTndOracleBean.COL_AI_LN_ITM);
  private static String deleteSql = TrLtmGfCfTndOracleBean.deleteSql + where(TrLtmGfCfTndOracleBean.COL_AI_TRN);

  /**
   * put your documentation comment here
   * @param transactionId
   * @param sequenceNumber
   * @param paymentTypeId
   * @param object
   * @return
   * @exception SQLException
   */
  ParametricStatement[] getInsertSQL (String transactionId, int sequenceNumber, String paymentTypeId, Redeemable object) throws SQLException {
    ArrayList statements = new ArrayList();
    statements.add(new ParametricStatement(insertSql, fromObjectToBean(transactionId, sequenceNumber, paymentTypeId, object).toList()));
    return  (ParametricStatement[])statements.toArray(new ParametricStatement[0]);
  }

  /**
   * put your documentation comment here
   * @param transactionId
   * @param sequenceNumber
   * @param paymentTypeId
   * @param object
   * @return
   * @exception SQLException
   */
  ParametricStatement[] getUpdateSQL (String transactionId, int sequenceNumber, String paymentTypeId, Redeemable object) throws SQLException {
    ArrayList statements = new ArrayList();
    List params = fromObjectToBean(transactionId, sequenceNumber, paymentTypeId, object).toList();
    params.add(transactionId);
    params.add(new Integer(sequenceNumber));
    statements.add(new ParametricStatement(updateSql, params));
    return  (ParametricStatement[])statements.toArray(new ParametricStatement[0]);
  }

  /**
   * put your documentation comment here
   * @param transactionId
   * @return
   * @exception SQLException
   */
  ParametricStatement[] getDeleteSQL (String transactionId) throws SQLException {
    ArrayList statements = new ArrayList();
    List params = new ArrayList();
    params.add(transactionId);
    statements.add(new ParametricStatement(deleteSql, params));
    return  (ParametricStatement[])statements.toArray(new ParametricStatement[0]);
  }

  /**
   * put your documentation comment here
   * @param transactionId
   * @param sequenceNumber
   * @param txnPaymentName
   * @return
   * @exception SQLException
   */
  Redeemable getByTransactionIdAndSequenceNumber (String transactionId, int sequenceNumber, String txnPaymentName) throws SQLException {
    String whereSql = where(TrLtmGfCfTndOracleBean.COL_AI_TRN, TrLtmGfCfTndOracleBean.COL_AI_LN_ITM);
    ArrayList list = new ArrayList();
    list.add(transactionId);
    list.add(new Integer(sequenceNumber));
    Redeemable[] redeemables = fromBeansToObjects(query(new TrLtmGfCfTndOracleBean(), whereSql, list), txnPaymentName);
    if (redeemables == null || redeemables.length == 0)
      return  null;
    return  redeemables[0];
  }

  /**
   * put your documentation comment here
   * @return
   */
  protected BaseOracleBean getDatabeanInstance () {
    return  new TrLtmGfCfTndOracleBean();
  }

  /**
   * put your documentation comment here
   * @param beans
   * @param txnPaymentName
   * @return
   * @exception SQLException
   */
  private Redeemable[] fromBeansToObjects (BaseOracleBean[] beans, String txnPaymentName) throws SQLException {
    Redeemable[] array = new Redeemable[beans.length];
    for (int i = 0; i < array.length; i++)
      array[i] = fromBeanToObject(beans[i], txnPaymentName);
    return  array;
  }

  /**
   * put your documentation comment here
   * @param baseBean
   * @param txnPaymentName
   * @return
   * @exception SQLException
   */
  private Redeemable fromBeanToObject (BaseOracleBean baseBean, String txnPaymentName) throws SQLException {
    TrLtmGfCfTndOracleBean bean = (TrLtmGfCfTndOracleBean)baseBean;
    String type = bean.getLuClsTnd();
    Redeemable object = null;
    if (type.equals(PAYMENT_TYPE_GIFT_CERTIFICATE))
      object = new GiftCert(txnPaymentName);
    else if (type.equals(PAYMENT_TYPE_DUE_BILL))
      object = new CMSDueBill(txnPaymentName);
    else if (type.equals(PAYMENT_TYPE_DUE_BILL_ISSUE))
      object = new CMSDueBillIssue(txnPaymentName);
    else if (type.equals(PAYMENT_TYPE_STORE_VALUE_CARD))
      object = new CMSStoreValueCard(txnPaymentName);
    object.setId(bean.getIdNmbSrzGfCf());
    object.setPhoneNumber(bean.getPhoneNumber());
    object.setLastName(bean.getLastName());
    object.setFirstName(bean.getFirstName());
    object.setCreateDate(bean.getCreateDate());
    object.setIssueAmount(bean.getIssueAmount());
    object.setType(bean.getType());
    object.setAuditNote(bean.getAuditNote());
    if (object instanceof GiftCert) {
      ((GiftCert)object).setControlNum(bean.getControlNumber());
    }
    else if (object instanceof StoreValueCard) {
      ((StoreValueCard)object).doSetControlNum(bean.getControlNumber());
      ((CMSStoreValueCard)object).doSetManualAuthCode(bean.getManualAuthCode());
      //Vivek Mishra : Added to fetch AJB sequence 02-NOV-2016
      ((CMSStoreValueCard)object).setAjbSequence(bean.getAjbSeq());
      //Ends here 02-NOV-2016
    }
    else if (object instanceof CMSDueBill) {
      ((CMSDueBill)object).doSetManualAuthCode(bean.getManualAuthCode());
      //Vivek Mishra : Added to fetch AJB sequence 02-NOV-2016
      ((CMSDueBill)object).setAjbSequence(bean.getAjbSeq());
      //Ends here 02-NOV-2016
    }
    else if (object instanceof CMSDueBillIssue) {
      ((CMSDueBillIssue)object).doSetControlNum(bean.getControlNumber());
      //Vivek Mishra : Added to fetch AJB sequence 02-NOV-2016
      ((CMSDueBillIssue)object).setAjbSequence(bean.getAjbSeq());
      //Ends here 02-NOV-2016
    }
    return  object;
  }

  /**
   * put your documentation comment here
   * @param transactionId
   * @param sequenceNumber
   * @param paymentTypeId
   * @param object
   * @return
   */
  private TrLtmGfCfTndOracleBean fromObjectToBean (String transactionId, int sequenceNumber, String paymentTypeId, Redeemable object) {
    TrLtmGfCfTndOracleBean bean = new TrLtmGfCfTndOracleBean();
    bean.setAiTrn(transactionId);
    bean.setAiLnItm(sequenceNumber);
    bean.setIdNmbSrzGfCf(object.getId());
    bean.setLuClsTnd(paymentTypeId);
    bean.setPhoneNumber(object.getPhoneNumber());
    bean.setLastName(object.getLastName());
    bean.setFirstName(object.getFirstName());
    bean.setCreateDate(object.getCreateDate());
    bean.setIssueAmount(object.getIssueAmount());
    bean.setType(object.getType());
    bean.setAuditNote(object.getAuditNote());
    if (object instanceof GiftCert) {
      bean.setControlNumber(((GiftCert)object).getControlNum());
    }
    if (object instanceof StoreValueCard) {
      bean.setControlNumber(((StoreValueCard)object).getControlNum());
      bean.setFlManual(((CMSStoreValueCard)object).getIsManual() ? "1" : "0");
      bean.setManualAuthCode(((CMSStoreValueCard)object).getManualAuthCode());
      //Vivek Mishra : Added to save AJB sequence 02-NOV-2016
      bean.setAjbSeq(((CMSStoreValueCard)object).getAjbSequence());
      //Ends here 02-NOV-2016
    }
    if (object instanceof DueBill) {
      if (object instanceof CMSDueBillIssue) {
        bean.setFlManual(((CMSDueBillIssue)object).getIsManual() ? "1" : "0");
        bean.setControlNumber(((CMSDueBillIssue)object).getControlNum());
        bean.setIdNmbSrzGfCf(((CMSDueBillIssue)object).getControlNum());
        //Vivek Mishra : Added to save AJB sequence 02-NOV-2016
        bean.setAjbSeq(((CMSDueBillIssue)object).getAjbSequence());
        //Ends here 02-NOV-2016
      }
      else if (object instanceof CMSDueBill) {
        bean.setFlManual(((CMSDueBill)object).getIsManual() ? "1" : "0");
        bean.setManualAuthCode(((CMSDueBill)object).getManualAuthCode());
        //Vivek Mishra : Added to save AJB sequence 02-NOV-2016
        bean.setAjbSeq(((CMSDueBill)object).getAjbSequence());
        //Ends here 02-NOV-2016
      }
    }
    if (object instanceof HouseAccount) {
      bean.setControlNumber(((HouseAccount)object).getControlNum());
    }
    return  bean;
  }
}



