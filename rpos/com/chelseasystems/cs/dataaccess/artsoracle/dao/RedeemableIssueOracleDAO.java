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
import  com.chelseasystems.cr.payment.DueBillIssue;
import  com.chelseasystems.cr.payment.GiftCert;
import  com.chelseasystems.cr.payment.IPaymentConstants;
import  com.chelseasystems.cr.payment.Redeemable;
import  com.chelseasystems.cr.payment.RedeemableHist;
import  com.chelseasystems.cr.payment.StoreValueCard;
import  com.chelseasystems.cs.payment.HouseAccount;
import  com.chelseasystems.cs.payment.CMSDueBillIssue;
import  com.chelseasystems.cs.payment.ICMSPaymentConstants;
import  com.chelseasystems.cs.payment.CMSStoreValueCard;
import  com.chelseasystems.cs.dataaccess.RedeemableIssueDAO;
import  com.chelseasystems.cs.dataaccess.artsoracle.databean.BaseOracleBean;
import  com.chelseasystems.cs.dataaccess.artsoracle.databean.DoCfGfOracleBean;
import  com.chelseasystems.cs.loyalty.RewardCard;
import  com.chelseasystems.cr.currency.CurrencyException;
import  com.chelseasystems.cs.payment.CMSRedeemable;
import  com.chelseasystems.cr.customer.Customer;


/**
 *
 *  Redeemable Data Access Object.<br>
 *  This object encapsulates all database access for Redeemable.<p>
 *  Below table shows the mapping between the object and the database tables and columns:<p>
 *  <table border=1>
 *    <tr><td><b>Object Attribute</b></td><td><b>Table Name</b></td><td><b>Column Name</b></td></tr>
 *    <tr><td>ControlNum</td><td>DO_CF_GF</td><td>GIFT_CONTROL</td></tr>
 *    <tr><td>Id</td><td>DO_CF_GF</td><td>ID_NMB_SRZ_GF_CF</td></tr>
 *    <tr><td>PhoneNumber</td><td>DO_CF_GF</td><td>TL_DNR_GF_CF</td></tr>
 *    <tr><td>LastName</td><td>DO_CF_GF</td><td>NM_DNR_GF_CF</td></tr>
 *    <tr><td>FirstName</td><td>DO_CF_GF</td><td>FN_DNR_GF_CF</td></tr>
 *    <tr><td>CreateDate</td><td>DO_CF_GF</td><td>TS_ISS_GF_CF</td></tr>
 *    <tr><td>IssueAmount</td><td>DO_CF_GF</td><td>MO_VL_FC_GF_CF</td></tr>
 *    <tr><td>Type</td><td>DO_CF_GF</td><td>TY_ISS_GF_CF</td></tr>
 *    <tr><td>AuditNote</td><td>DO_CF_GF</td><td>AUDIT_NOTE</td></tr>
 *    <tr><td>CustomerId</td><td>DO_CF_GF</td><td>ID_CT</td></tr>
 *    <tr><td>ExpirationDate</td><td>DO_CF_GF</td><td>DC_EXPIRATION</td></tr>
 *  </table>
 *
 *  @see Redeemable
 *  @see com.chelseasystems.cs.dataaccess.artsoracle.databean.BaseOracleBean
 *  @see com.chelseasystems.cs.dataaccess.artsoracle.databean.DoCfGfOracleBean
 *
 */
public class RedeemableIssueOracleDAO extends BaseOracleDAO
    implements RedeemableIssueDAO {
  private static RedeemableHistOracleDAO redeemableHistDAO = new RedeemableHistOracleDAO();
  private static CustomerOracleDAO customerDAO = new CustomerOracleDAO();
  private static String REDEEMABLE_ISSUE_ID = DoCfGfOracleBean.COL_ID_NMB_SRZ_GF_CF;
  private static String REDEEMABLE_ISSUE_TYPE_ID = DoCfGfOracleBean.COL_TY_GF_CF;
  private static String REDEEMABLE_ISSUE_DELETED = DoCfGfOracleBean.COL_DELETED;
  private static String REDEEMABLE_ISSUE_GIFT_CONTROL = DoCfGfOracleBean.COL_GIFT_CONTROL;
  private static String selectSql = DoCfGfOracleBean.selectSql;
  private static String insertSql = DoCfGfOracleBean.insertSql;
  private static String updateSql = DoCfGfOracleBean.updateSql + where(REDEEMABLE_ISSUE_ID, REDEEMABLE_ISSUE_TYPE_ID);
  private static String deleteSql = DoCfGfOracleBean.deleteSql + where(REDEEMABLE_ISSUE_ID, REDEEMABLE_ISSUE_TYPE_ID);
  private static String updateForDeleteSql = "update " + DoCfGfOracleBean.TABLE_NAME + " set " + REDEEMABLE_ISSUE_DELETED + " = 1 where " + REDEEMABLE_ISSUE_ID + " = ? and " + REDEEMABLE_ISSUE_TYPE_ID + " = ?";

  /**
   * put your documentation comment here
   * @param object
   * @return
   * @exception SQLException
   */
  public ParametricStatement[] getInsertSQL (Redeemable object) throws SQLException {
    List statements = new ArrayList();
    statements.add(new ParametricStatement(insertSql, fromObjectToBean(object).toList()));
    if (object.getRedemptionHistory() != null) {
      RedeemableHist[] redemptionHist = (RedeemableHist[])object.getRedemptionHistory().toArray(new RedeemableHist[0]);
      String redeemableType = getRedeemableIssueType(object);
      for (int i = 0; i < redemptionHist.length; i++)
        statements.add(redeemableHistDAO.getInsertSQL(object.getId(), redeemableType, i, redemptionHist[i]));
    }
    return  (ParametricStatement[])statements.toArray(new ParametricStatement[0]);
  }

  /**
   * put your documentation comment here
   * @param object
   * @return
   * @exception SQLException
   */
  public ParametricStatement[] getUpdateSQL (Redeemable object) throws SQLException {
    String redeemableId = object.getId();
    String redeemableTypeId = getRedeemableIssueType(object);
    ArrayList statements = new ArrayList();
    List params = fromObjectToBean(object).toList();
    params.add(redeemableId);
    params.add(redeemableTypeId);
    statements.add(new ParametricStatement(updateSql, params));
    statements.add(redeemableHistDAO.getDeleteSQL(redeemableId, redeemableTypeId));
    if (object.getRedemptionHistory() != null) {
      RedeemableHist[] redemptionHist = (RedeemableHist[])object.getRedemptionHistory().toArray(new RedeemableHist[0]);
      for (int i = 0; i < redemptionHist.length; i++)
        statements.add(redeemableHistDAO.getInsertSQL(redeemableId, redeemableTypeId, i, redemptionHist[i]));
    }
    return  (ParametricStatement[])statements.toArray(new ParametricStatement[0]);
  }

  /**
   * put your documentation comment here
   * @param object
   * @return
   * @exception SQLException
   */
  public ParametricStatement[] getDeleteSQL (Redeemable object) throws SQLException {
    String redeemableId = object.getId();
    String redeemableTypeId = getRedeemableIssueType(object);
    ArrayList statements = new ArrayList();
    statements.add(redeemableHistDAO.getDeleteSQL(redeemableId, redeemableTypeId));
    List params = new ArrayList();
    params.add(redeemableId);
    params.add(redeemableTypeId);
    statements.add(new ParametricStatement(deleteSql, params));
    return  (ParametricStatement[])statements.toArray(new ParametricStatement[0]);
  }

  /**
   * put your documentation comment here
   * @param object
   * @exception SQLException
   */
  public void insert (Redeemable object) throws SQLException {
    execute(getInsertSQL(object));
  }

  /**
   * put your documentation comment here
   * @param object
   * @exception SQLException
   */
  public void update (Redeemable object) throws SQLException {
    execute(getUpdateSQL(object));
  }

  /**
   * put your documentation comment here
   * @param object
   * @exception SQLException
   */
  public void delete (Redeemable object) throws SQLException {
    execute(getDeleteSQL(object));
  }

  /**
   * put your documentation comment here
   * @param object
   * @return
   * @exception SQLException
   */
  public ParametricStatement getUpdateGiftCertSQLForLogicalDelete (Redeemable object) throws SQLException {
    List params = new ArrayList();
    params.add(object.getId());
    params.add(PAYMENT_TYPE_GIFT_CERTIFICATE);
    return  new ParametricStatement(updateForDeleteSql, params);
  }

  /**
   * put your documentation comment here
   * @param object
   * @return
   * @exception SQLException
   */
  public ParametricStatement getUpdateStoreValueCardSQLForLogicalDelete (Redeemable object) throws SQLException {
    List params = new ArrayList();
    params.add(object.getId());
    params.add(PAYMENT_TYPE_STORE_VALUE_CARD);
    return  new ParametricStatement(updateForDeleteSql, params);
  }

  /**
   * put your documentation comment here
   * @param object
   * @return
   * @exception SQLException
   */
  public ParametricStatement getUpdateHouseAccountSQLForLogicalDelete (Redeemable object) throws SQLException {
    List params = new ArrayList();
    params.add(object.getId());
    params.add(PAYMENT_TYPE_HOUSE_ACCOUNT);
    return  new ParametricStatement(updateForDeleteSql, params);
  }

  /**
   * put your documentation comment here
   * @param object
   * @return
   * @exception SQLException
   */
  public ParametricStatement getUpdateDueBillSQLForLogicalDelete (Redeemable object) throws SQLException {
    List params = new ArrayList();
    params.add(object.getId());
    params.add(PAYMENT_TYPE_DUE_BILL_ISSUE);
    return  new ParametricStatement(updateForDeleteSql, params);
  }

  /**
   * put your documentation comment here
   * @param object
   * @return
   * @exception SQLException
   */
  public ParametricStatement[] getUpdateRewardCardSQLForLogicalDelete (Redeemable object) throws SQLException {
    ArrayList statements = new ArrayList();
    List params = new ArrayList();
    params.add(object.getId());
    params.add(DISCOUNT_TYPE_REWARD_CARD);
    statements.add(new ParametricStatement(updateForDeleteSql, params));
    statements.add(redeemableHistDAO.getDeleteSQL(object.getId(), getRedeemableIssueType(object)));
    if (object.getRedemptionHistory() != null) {
      RedeemableHist[] redemptionHist = (RedeemableHist[])object.getRedemptionHistory().toArray(new RedeemableHist[0]);
      for (int i = 0; i < redemptionHist.length; i++)
        statements.add(redeemableHistDAO.getInsertSQL(object.getId(), getRedeemableIssueType(object), i, redemptionHist[i]));
    }
    return  (ParametricStatement[])statements.toArray(new ParametricStatement[0]);
  }

  /**
   * put your documentation comment here
   * @param id
   * @return
   * @exception SQLException
   */
  public Redeemable selectRedeemableById (String id) throws SQLException {
    String whereSql = "where " + REDEEMABLE_ISSUE_ID + " = ?";
    List params = new ArrayList();
    params.add(id);
    Redeemable[] redeemables = fromBeansToObjects(query(new DoCfGfOracleBean(), whereSql, params));
    if (redeemables == null || redeemables.length == 0)
      return  null;
    return  redeemables[0];
  }

  /**
   * put your documentation comment here
   * @param id
   * @return
   * @exception SQLException
   */
  public Redeemable selectStoreValueCardById (String id) throws SQLException {
    String whereSql = "where (" + REDEEMABLE_ISSUE_DELETED + " = 0 or " + REDEEMABLE_ISSUE_DELETED + " is null) and " + REDEEMABLE_ISSUE_ID + " = ? and " + REDEEMABLE_ISSUE_TYPE_ID + " = ?";
    List params = new ArrayList();
    params.add(id);
    params.add(PAYMENT_TYPE_STORE_VALUE_CARD);
    Redeemable[] redeemables = fromBeansToObjects(query(new DoCfGfOracleBean(), whereSql, params));
    if (redeemables == null || redeemables.length == 0)
      return  null;
    return  redeemables[0];
  }

  /**
   * put your documentation comment here
   * @param id
   * @return
   * @exception SQLException
   */
  public Redeemable selectHouseAccountById (String id) throws SQLException {
    String whereSql = "where (" + REDEEMABLE_ISSUE_DELETED + " = 0 or " + REDEEMABLE_ISSUE_DELETED + " is null) and " + REDEEMABLE_ISSUE_ID + " = ? and " + REDEEMABLE_ISSUE_TYPE_ID + " = ?";
    List params = new ArrayList();
    params.add(id);
    params.add(PAYMENT_TYPE_HOUSE_ACCOUNT);
    Redeemable[] redeemables = fromBeansToObjects(query(new DoCfGfOracleBean(), whereSql, params));
    if (redeemables == null || redeemables.length == 0)
      return  null;
    return  redeemables[0];
  }

  /**
   * put your documentation comment here
   * @param customerId
   * @return
   * @exception SQLException
   */
  public Redeemable[] selectByCustomerId (String customerId) throws SQLException {
    String whereSql = where(DoCfGfOracleBean.COL_ID_CT);
    List params = new ArrayList();
    params.add(customerId);
    Redeemable[] redeemables = fromBeansToObjects(query(new DoCfGfOracleBean(), whereSql, params));
    if (redeemables == null || redeemables.length == 0)
      return  null;
    return  redeemables;
  }

  /**
   * put your documentation comment here
   * @param type
   * @param customerId
   * @return
   * @exception SQLException
   */
  public Redeemable[] selectByCustomerId (String type, String customerId) throws SQLException {
    String whereSql = where(DoCfGfOracleBean.COL_TY_GF_CF, DoCfGfOracleBean.COL_ID_CT);
    List params = new ArrayList();
    params.add(type);
    params.add(customerId);
    Redeemable[] redeemables = fromBeansToObjects(query(new DoCfGfOracleBean(), whereSql, params));
    if (redeemables == null || redeemables.length == 0)
      return  null;
    return  redeemables;
  }

  /**
   *
   * @param type String
   * @param id String
   * @throws SQLException
   * @return Redeemable[]
   */
  public Redeemable selectRedeemableById (String type, String id) throws SQLException {
    if (type.equals("HOUSE_ACCOUNT")) {
      Redeemable redeem = selectRedeemableById(id);
      return  redeem;
    }
    String whereSql = "where (" + REDEEMABLE_ISSUE_DELETED + " = 0 or " + REDEEMABLE_ISSUE_DELETED + " is null) and " + REDEEMABLE_ISSUE_TYPE_ID + " = ? and " + REDEEMABLE_ISSUE_GIFT_CONTROL + " = ?";
    List params = new ArrayList();
    params.add(type);
    params.add(id);
    Redeemable[] redeemables = fromBeansToObjects(query(new DoCfGfOracleBean(), whereSql, params));
    if (redeemables == null || redeemables.length == 0)
      return  null;
    return  redeemables[0];
  }

  /**
   * put your documentation comment here
   * @param id
   * @return
   * @exception SQLException
   */
  public Redeemable selectGiftCertById (String id) throws SQLException {
    String whereSql = "where (" + REDEEMABLE_ISSUE_DELETED + " = 0 or " + REDEEMABLE_ISSUE_DELETED + " is null) and " + REDEEMABLE_ISSUE_ID + " = ? and " + REDEEMABLE_ISSUE_TYPE_ID + " = ?";
    List params = new ArrayList();
    params.add(id);
    params.add(PAYMENT_TYPE_GIFT_CERTIFICATE);
    Redeemable[] redeemables = fromBeansToObjects(query(new DoCfGfOracleBean(), whereSql, params));
    if (redeemables == null || redeemables.length == 0)
      return  null;
    return  redeemables[0];
  }

  /**
   * put your documentation comment here
   * @param id
   * @return
   * @exception SQLException
   */
  public Redeemable selectDueBillById (String id) throws SQLException {
    String whereSql = "where (" + REDEEMABLE_ISSUE_DELETED + " = 0 or " + REDEEMABLE_ISSUE_DELETED + " is null) and " + REDEEMABLE_ISSUE_ID + " = ? and " + REDEEMABLE_ISSUE_TYPE_ID + " = ?";
    List params = new ArrayList();
    params.add(id);
    params.add(PAYMENT_TYPE_DUE_BILL_ISSUE);
    Redeemable[] redeemables = fromBeansToObjects(query(new DoCfGfOracleBean(), whereSql, params));
    if (redeemables == null || redeemables.length == 0)
      return  null;
    return  redeemables[0];
  }

  /**
   * put your documentation comment here
   * @param id
   * @return
   * @exception SQLException
   */
  public Redeemable selectRewardCardById (String id) throws SQLException {
    String whereSql = "where (" + REDEEMABLE_ISSUE_DELETED + " = 0 or " + REDEEMABLE_ISSUE_DELETED + " is null) and " + REDEEMABLE_ISSUE_ID + " = ? and " + REDEEMABLE_ISSUE_TYPE_ID + " = ?";
    List params = new ArrayList();
    params.add(id);
    params.add(DISCOUNT_TYPE_REWARD_CARD);
    Redeemable[] redeemables = fromBeansToObjects(query(new DoCfGfOracleBean(), whereSql, params));
    if (redeemables == null || redeemables.length == 0)
      return  null;
    return  redeemables[0];
  }

  /*ks: Method diverted in JDBS services
   //new method added by Anand for searching by control number (Armani specific)
   public Redeemable selectDueBillByControlNumber(String id) throws SQLException
   {
   String whereSql = "where (" + REDEEMABLE_ISSUE_DELETED + " = 0 or " + REDEEMABLE_ISSUE_DELETED + " is null) and " + REDEEMABLE_ISSUE_GIFT_CONTROL + " = ? and " + REDEEMABLE_ISSUE_TYPE_ID + " = ?";
   List params = new ArrayList();
   params.add(id);
   params.add(PAYMENT_TYPE_DUE_BILL_ISSUE);
   Redeemable[] redeemables = fromBeansToObjects(query(new DoCfGfOracleBean(), whereSql, params));
   if (redeemables == null || redeemables.length == 0)
   return null;
   return redeemables[0];
   }
   */
  public Redeemable selectRedeemableByControlNumber (String controlNumber) throws SQLException {
    String whereSql = "where (" + REDEEMABLE_ISSUE_DELETED + " = 0 or " + REDEEMABLE_ISSUE_DELETED + " is null) and " + REDEEMABLE_ISSUE_GIFT_CONTROL + " = ?";
    List params = new ArrayList();
    params.add(controlNumber);
    Redeemable[] redeemables = fromBeansToObjects(query(new DoCfGfOracleBean(), whereSql, params));
    if (redeemables == null || redeemables.length == 0)
      return  null;
    return  redeemables[0];
  }

  /**
   * put your documentation comment here
   * @param controlNumber
   * @return
   * @exception SQLException
   */
  public Redeemable selectRewardCardByControlNumber (String controlNumber) throws SQLException {
    String whereSql = "where (" + REDEEMABLE_ISSUE_DELETED + " = 0 or " + REDEEMABLE_ISSUE_DELETED + " is null) and " + REDEEMABLE_ISSUE_GIFT_CONTROL + " = ? and " + REDEEMABLE_ISSUE_TYPE_ID + " = ?";
    List params = new ArrayList();
    params.add(controlNumber);
    params.add(DISCOUNT_TYPE_REWARD_CARD);
    Redeemable[] redeemables = fromBeansToObjects(query(new DoCfGfOracleBean(), whereSql, params));
    if (redeemables == null || redeemables.length == 0)
      return  null;
    return  redeemables[0];
  }

  /**
   * put your documentation comment here
   * @param controlNumber
   * @return
   * @exception SQLException
   */
  public Redeemable selectStoreValueCardByControlNumber (String controlNumber) throws SQLException {
    String whereSql = "where (" + REDEEMABLE_ISSUE_DELETED + " = 0 or " + REDEEMABLE_ISSUE_DELETED + " is null) and " + REDEEMABLE_ISSUE_GIFT_CONTROL + " = ? and " + REDEEMABLE_ISSUE_TYPE_ID + " = ?";
    List params = new ArrayList();
    params.add(controlNumber);
    params.add(PAYMENT_TYPE_STORE_VALUE_CARD);
    Redeemable[] redeemables = fromBeansToObjects(query(new DoCfGfOracleBean(), whereSql, params));
    if (redeemables == null || redeemables.length == 0)
      return  null;
    return  redeemables[0];
  }

  /**
   * put your documentation comment here
   * @param controlNumber
   * @return
   * @exception SQLException
   */
  public Redeemable selectGiftCertByControlNumber (String controlNumber) throws SQLException {
    String whereSql = "where (" + REDEEMABLE_ISSUE_DELETED + " = 0 or " + REDEEMABLE_ISSUE_DELETED + " is null) and " + REDEEMABLE_ISSUE_GIFT_CONTROL + " = ? and " + REDEEMABLE_ISSUE_TYPE_ID + " = ?";
    List params = new ArrayList();
    params.add(controlNumber);
    params.add(PAYMENT_TYPE_GIFT_CERTIFICATE);
    Redeemable[] redeemables = fromBeansToObjects(query(new DoCfGfOracleBean(), whereSql, params));
    if (redeemables == null || redeemables.length == 0)
      return  null;
    return  redeemables[0];
  }

  /**
   * put your documentation comment here
   * @param controlNumber
   * @return
   * @exception SQLException
   */
  public boolean isGiftCertControlNumberUsed (String controlNumber) throws SQLException {
    //    String whereSql = "where REDEEMABLE_ISSUE_GIFT_CONTROL = ? and REDEEMABLE_ISSUE_TYPE_ID = ?";
    String whereSql = where(REDEEMABLE_ISSUE_GIFT_CONTROL);
    List params = new ArrayList();
    params.add(controlNumber);
    //    params.add(PAYMENT_TYPE_GIFT_CERTIFICATE);
    BaseOracleBean[] beans = query(new DoCfGfOracleBean(), whereSql, params);
    return  (beans == null || beans.length == 0);
  }

  /**
   * put your documentation comment here
   * @return
   */
  protected BaseOracleBean getDatabeanInstance () {
    return  new DoCfGfOracleBean();
  }

  /**
   * put your documentation comment here
   * @param beans
   * @return
   * @exception SQLException
   */
  private Redeemable[] fromBeansToObjects (BaseOracleBean[] beans) throws SQLException {
    Redeemable[] array = new Redeemable[beans.length];
    for (int i = 0; i < array.length; i++)
      array[i] = fromBeanToObject(beans[i]);
    return  array;
  }

  /**
   * put your documentation comment here
   * @param baseBean
   * @return
   * @exception SQLException
   */
  private Redeemable fromBeanToObject (BaseOracleBean baseBean) throws SQLException {
    DoCfGfOracleBean bean = (DoCfGfOracleBean)baseBean;
    Redeemable object = null;
    String redeemableIssueTypeId = bean.getTyGfCf();
    if (redeemableIssueTypeId.equals(PAYMENT_TYPE_GIFT_CERTIFICATE)) {
      object = new GiftCert(IPaymentConstants.GIFT_CERTIFICATE);
      GiftCert giftCert = (GiftCert)object;
      giftCert.setControlNum(bean.getGiftControl());
    }
    else if (redeemableIssueTypeId.equals(PAYMENT_TYPE_DUE_BILL_ISSUE)) {
      object = new CMSDueBillIssue(IPaymentConstants.CREDIT_MEMO_ISSUE);
      ((CMSDueBillIssue)object).doSetControlNum(bean.getGiftControl());
      ((CMSDueBillIssue)object).doSetCustomerId(bean.getIdCt());
      ((CMSDueBillIssue)object).setStoreId(bean.getIdStrIssg());
      if (bean.getDeleted() != null)
        ((CMSDueBillIssue)object).setStatus(!bean.getDeleted().booleanValue());
    }
    else if (redeemableIssueTypeId.equals(PAYMENT_TYPE_STORE_VALUE_CARD)) {
      object = new CMSStoreValueCard(IPaymentConstants.STORE_VALUE_CARD);
      ((StoreValueCard)object).doSetControlNum(bean.getGiftControl());
      ((CMSStoreValueCard)object).doSetCustomerId(bean.getIdCt());
      ((CMSStoreValueCard)object).setStoreId(bean.getIdStrIssg());
      if (bean.getDeleted() != null)
        ((CMSStoreValueCard)object).setStatus(!bean.getDeleted().booleanValue());
    }
    else if (redeemableIssueTypeId.equals(PAYMENT_TYPE_HOUSE_ACCOUNT)) {
      object = new HouseAccount(ICMSPaymentConstants.HOUSE_ACCOUNT);
      ((HouseAccount)object).doSetControlNum(bean.getGiftControl());
      ((HouseAccount)object).doSetCustomerId(bean.getIdCt());
      if (bean.getDeleted() != null)
        ((HouseAccount)object).setStatus(!bean.getDeleted().booleanValue());
    }
    else if (redeemableIssueTypeId.equals(DISCOUNT_TYPE_REWARD_CARD)) {
      object = new RewardCard(ICMSPaymentConstants.REWARD_CARD);
      ((RewardCard)object).doSetControlNum(bean.getGiftControl());
      ((RewardCard)object).doSetCustomerId(bean.getIdCt());
      ((RewardCard)object).doSetExpDate(bean.getDcEpGfCf());
      ((RewardCard)object).doSetLoyalty(new LoyaltyOracleDAO().selectById(bean.getLoyaltyNum()));
      ((RewardCard)object).doSetIssuingStoreId(bean.getIdStrIssg());
      if (bean.getDeleted() != null)
        ((RewardCard)object).setStatus(!bean.getDeleted().booleanValue());
    }
    object.setId(bean.getIdNmbSrzGfCf());
    object.setPhoneNumber(bean.getTlDnrGfCf());
    object.setLastName(bean.getNmDnrGfCf());
    object.setFirstName(bean.getFnDnrGfCf());
    object.setCreateDate(bean.getTsIssGfCf());
    object.setIssueAmount(bean.getMoVlFcGfCf());
    object.setType(bean.getTyIssGfCf());
    object.setAuditNote(bean.getAuditNote());
    if (bean.getIdCt() != null && !bean.getIdCt().trim().equals("") && (bean.getFnDnrGfCf() == null || bean.getFnDnrGfCf().trim().equals("") || bean.getNmDnrGfCf() == null || bean.getNmDnrGfCf().trim().equals(""))) {
      Customer customer = customerDAO.selectById(bean.getIdCt());
      if (customer != null) {
        object.setFirstName(customer.getFirstName());
        object.setLastName(customer.getLastName());
        if (customer.getTelephone() != null)
          object.setPhoneNumber(customer.getTelephone().getFormattedNumber());
      }
    }
    RedeemableHist[] redeemableHist = redeemableHistDAO.getByRedeemableIssueIdAndType(bean.getIdNmbSrzGfCf(), redeemableIssueTypeId);
    for (int i = 0; i < redeemableHist.length; i++)
      object.addRedemption(redeemableHist[i]);
    return  object;
  }

  /**
   * put your documentation comment here
   * @param object
   * @return
   */
  private DoCfGfOracleBean fromObjectToBean (Redeemable object) {
    DoCfGfOracleBean bean = new DoCfGfOracleBean();
    bean.setTyGfCf(getRedeemableIssueType(object));
    bean.setIdNmbSrzGfCf(object.getId());
    bean.setTlDnrGfCf(object.getPhoneNumber());
    bean.setNmDnrGfCf(object.getLastName());
    bean.setFnDnrGfCf(object.getFirstName());
    bean.setTsIssGfCf(object.getCreateDate());
    bean.setMoVlFcGfCf(object.getIssueAmount());
    bean.setTyIssGfCf(object.getType());
    //((CMSRedeemable)object).getManualAuthCode()
    //    if((object.getAuditNote()!=null && !object.getAuditNote().trim().equals(""))
    //            || !((object instanceof CMSRedeemable) && ((CMSRedeemable)object).getManualAuthCode()!=null))
    //VM
    if ((object instanceof CMSStoreValueCard) && ((CMSStoreValueCard)object).getManualAuthCode() != null && (object.getAuditNote() == null || object.getAuditNote().trim().equals(""))) {
      bean.setAuditNote(((CMSStoreValueCard)object).getManualAuthCode());
    }
    else
      bean.setAuditNote(object.getAuditNote());
    bean.setDeleted(false);
    if (bean.getTyGfCf().equals(PAYMENT_TYPE_GIFT_CERTIFICATE)) {
      bean.setGiftControl(((GiftCert)object).getControlNum());
      bean.setIdNmbSrzGfCf(((GiftCert)object).getControlNum());
    }
    else if (bean.getTyGfCf().equals(PAYMENT_TYPE_STORE_VALUE_CARD)) {
      bean.setGiftControl(((StoreValueCard)object).getControlNum());
      bean.setIdNmbSrzGfCf(((StoreValueCard)object).getControlNum());
      bean.setIdCt(((CMSStoreValueCard)object).getCustomerId());
      bean.setIdStrIssg(((CMSStoreValueCard)object).getStoreId());
    }
    else if (bean.getTyGfCf().equals(PAYMENT_TYPE_HOUSE_ACCOUNT)) {
      bean.setGiftControl(((HouseAccount)object).getControlNum());
      bean.setIdNmbSrzGfCf(((HouseAccount)object).getId());
      bean.setIdCt(((HouseAccount)object).getCustomerId());
    }
    else if (bean.getTyGfCf().equals(PAYMENT_TYPE_DUE_BILL_ISSUE)) {
      bean.setGiftControl(((CMSDueBillIssue)object).getControlNum());
      bean.setIdNmbSrzGfCf(((CMSDueBillIssue)object).getControlNum());
      bean.setIdCt(((CMSDueBillIssue)object).getCustomerId());
      bean.setIdStrIssg(((CMSDueBillIssue)object).getStoreId());
    }
    else if (bean.getTyGfCf().equals(DISCOUNT_TYPE_REWARD_CARD)) {
      bean.setGiftControl(((RewardCard)object).getControlNum());
      bean.setIdNmbSrzGfCf(((RewardCard)object).getControlNum());
      bean.setIdCt(((RewardCard)object).getCustomerId());
      bean.setDcEpGfCf(((RewardCard)object).getExpDate());
      bean.setLoyaltyNum(((RewardCard)object).getLoyalty().getLoyaltyNumber());
      bean.setIdStrIssg(((RewardCard)object).getIssuingStoreId());
      //bean.setMoBlncUnspGfCf(( (RewardCard) object).getRemainingBalance());
    }
    return  bean;
  }

  /**
   * put your documentation comment here
   * @param redeemable
   * @return
   */
  String getRedeemableIssueType (Redeemable redeemable) {
    if (redeemable instanceof GiftCert)
      return  PAYMENT_TYPE_GIFT_CERTIFICATE;
    else if (redeemable instanceof DueBillIssue)
      return  PAYMENT_TYPE_DUE_BILL_ISSUE;
    else if (redeemable instanceof StoreValueCard)
      return  PAYMENT_TYPE_STORE_VALUE_CARD;
    else if (redeemable instanceof HouseAccount)
      return  PAYMENT_TYPE_HOUSE_ACCOUNT;
    else if (redeemable instanceof RewardCard)
      return  DISCOUNT_TYPE_REWARD_CARD;
    return  null;
  }
}



