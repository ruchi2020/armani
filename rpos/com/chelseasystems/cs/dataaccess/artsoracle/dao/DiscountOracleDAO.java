/*
 * put your module comment here
 * formatted with JxBeauty (c) johann.langhofer@nextra.at
 */
//
// Copyright 1999-2001, Chelsea Market Systems
//


package  com.chelseasystems.cs.dataaccess.artsoracle.dao;

import  com.chelseasystems.cr.database.ParametricStatement;
import  com.chelseasystems.cr.discount.*;
import  com.chelseasystems.cr.currency.*;
import  com.chelseasystems.cs.dataaccess.*;
import  com.chelseasystems.cs.dataaccess.artsoracle.databean.*;
import  com.chelseasystems.cs.discount.*;
import  java.util.Date;
import  java.sql.*;
import  java.util.*;
import  com.chelseasystems.cs.payment.CMSRedeemable;


/**
 *
 *  Discount Data Access Object.<br>
 *  This object encapsulates all database access for Discount.<p>
 *  Below table shows the mapping between the object and the database tables and columns:<p>
 *  <table border=1>
 *    <tr><td><b>Object Attribute</b></td><td><b>Table Name</b></td><td><b>Column Name</b></td></tr>
 *    <tr><td>AdvertisingCode</td><td>TR_LTM_DSC</td><td>ADVERTISING_CODE</td></tr>
 *    <tr><td>Amount</td><td>TR_LTM_DSC</td><td>MO_DSC</td></tr>
 *    <tr><td>ConvertToMarkdown</td><td>TR_LTM_DSC</td><td>CONVERT_TO_MARKDOWN</td></tr>
 *    <tr><td>CorporateId</td><td>TR_LTM_DSC</td><td>CORPORATE_ID</td></tr>
 *    <tr><td>Employee</td><td>TR_LTM_DSC</td><td>EMPLOYEE_ID</td></tr>
 *    <tr><td>GuiLabel</td><td>TR_LTM_DSC</td><td>GUI_LABEL</td></tr>
 *    <tr><td>IsDiscountPercent</td><td>TR_LTM_DSC</td><td>IS_DISCOUNT_PERCENT</td></tr>
 *    <tr><td>IsInAdditionToMarkdown</td><td>TR_LTM_DSC</td><td>IS_IN_ADDITION_TO_MD</td></tr>
 *    <tr><td>IsSignatureRequired</td><td>TR_LTM_DSC</td><td>IS_SIGNATURE_REQUIRED</td></tr>
 *    <tr><td>Percent</td><td>TR_LTM_DSC</td><td>PERCENT</td></tr>
 *    <tr><td>Reason</td><td>TR_LTM_DSC</td><td>REASON</td></tr>
 *    <tr><td>Type</td><td>TR_LTM_DSC</td><td>TYPE</td></tr>
 *    <tr><td>DiscoutCode</td><td>TR_LTM_DSC</td><td>DISCOUNT_CODE</td></tr>
 *  </table>
 *
 *  @see Discount
 *  @see com.chelseasystems.cs.dataaccess.artsoracle.databean.BaseOracleBean
 *  @see com.chelseasystems.cs.dataaccess.artsoracle.databean.TrLtmDscOracleBean
 *  @see com.chelseasystems.cs.dataaccess.artsoracle.databean.TrLtmEmDscOracleBean
 *
 */
public class DiscountOracleDAO extends BaseOracleDAO
    implements DiscountDAO {
  private static EmployeeOracleDAO employeeDAO = new EmployeeOracleDAO();
  private static String selectSql = TrLtmDscOracleBean.selectSql;
  private static String insertSql = TrLtmDscOracleBean.insertSql;
  private static String updateSql = TrLtmDscOracleBean.updateSql + where(TrLtmDscOracleBean.COL_AI_TRN, TrLtmDscOracleBean.COL_AI_LN_ITM);
  private static String deleteSql = TrLtmDscOracleBean.deleteSql + where(TrLtmDscOracleBean.COL_AI_TRN);

  /**
   * put your documentation comment here
   * @param transactionId
   * @param sequenceNumber
   * @param object
   * @return 
   * @exception SQLException
   */
  ParametricStatement[] getInsertSQL (String transactionId, int sequenceNumber, CMSDiscount object) throws SQLException {
    ArrayList statements = new ArrayList();
    statements.add(new ParametricStatement(insertSql, toTrLtmDscBean(transactionId, sequenceNumber, object).toList()));
    if (object instanceof CMSEmployeeDiscount)
      statements.add(new ParametricStatement(TrLtmEmDscOracleBean.insertSql, toTrLtmEmDscBean(transactionId, sequenceNumber, (CMSEmployeeDiscount)object).toList()));
    return  (ParametricStatement[])statements.toArray(new ParametricStatement[0]);
  }

  /**
   * put your documentation comment here
   * @param compositeId
   * @return 
   * @exception SQLException
   */
  public Discount[] selectByCompositeId (String compositeId) throws SQLException {
    String whereSql = where(TrLtmDscOracleBean.COL_AI_TRN);
    return  (fromBeansToObjects(query(new TrLtmDscOracleBean(), whereSql, compositeId)));
  }

  /**
   * put your documentation comment here
   * @param beans
   * @return 
   * @exception SQLException
   */
  private Discount[] fromBeansToObjects (BaseOracleBean[] beans) throws SQLException {
    Discount[] array = new Discount[beans.length];
    for (int i = 0; i < array.length; i++)
      array[i] = fromBeanToObject(beans[i]);
    return  (array);
  }

  /**
   * put your documentation comment here
   * @param baseBean
   * @return 
   * @exception SQLException
   */
  private CMSDiscount fromBeanToObject (BaseOracleBean baseBean) throws SQLException {
    TrLtmDscOracleBean bean = (TrLtmDscOracleBean)baseBean;
    //All reference to CMSDiscount instead of Discount and Added RewardDiscount
    CMSDiscount object = null;
    if (bean.getTyDsc().equals(this.DISCOUNT_TYPE_BASIC))
      object = (CMSDiscount)new CMSBasicDiscount(); 
    else if (bean.getTyDsc().equals(this.DISCOUNT_TYPE_EMPLOYEE))
      object = (CMSDiscount)new CMSEmployeeDiscount(); 
    else if (bean.getTyDsc().equals(this.DISCOUNT_TYPE_REWARD))
      object = (CMSDiscount)new RewardDiscount(); 
    else 
      object = new CMSBasicDiscount();
    object.doSetType(bean.getType());
    object.doSetGuiLabel(bean.getGuiLabel());
    object.doSetPercent(bean.getPercent().doubleValue());
    if (object instanceof CMSEmployeeDiscount)
    ((CMSEmployeeDiscount)object).doSetNormalDiscountPercent(bean.getPercent().doubleValue());
    object.doSetAmount(bean.getMoDsc());
    object.doSetReason(bean.getReason());
    //Added Discount Code
    object.doSetDiscountCode(bean.getDiscountCode());
    if (bean.getEmployeeId() != null && bean.getEmployeeId().length() > 0)
      object.doSetEmployee(employeeDAO.selectById(bean.getEmployeeId()));
    object.doSetCorporateId(bean.getCorporateId());
    object.doSetAdvertisingCode(bean.getAdvertisingCode());
    object.doSetIsSignatureRequired(bean.getIsSigRequired().booleanValue());
    object.doSetIsInAdditionToMarkdown(bean.getIsAdditionToMd().booleanValue());
    object.doSetIsDiscountPercent(bean.getIsDscPercent().booleanValue());
    object.doSetConvertToMarkdown(bean.getCnvtToMkdn().booleanValue());
    object.doSetSequenceNumber(bean.getAiLnItm().intValue());
    return  (object);
  }

  /**
   * put your documentation comment here
   * @param transactionId
   * @param sequenceNumber
   * @param object
   * @return 
   */
  private TrLtmDscOracleBean toTrLtmDscBean (String transactionId, int sequenceNumber, CMSDiscount object) {
    TrLtmDscOracleBean bean = new TrLtmDscOracleBean();
    if (object instanceof CMSBasicDiscount)
      bean.setTyDsc(ArtsConstants.DISCOUNT_TYPE_BASIC); 
    else if (object instanceof CMSEmployeeDiscount)
      bean.setTyDsc(ArtsConstants.DISCOUNT_TYPE_EMPLOYEE); 
    else if (object instanceof RewardDiscount)
      bean.setTyDsc(ArtsConstants.DISCOUNT_TYPE_REWARD); 
    else 
      bean.setTyDsc(ArtsConstants.DISCOUNT_TYPE_BASIC);
    bean.setAiTrn(transactionId);
    bean.setAiLnItm(sequenceNumber);
    bean.setType(object.getType());
    bean.setGuiLabel(object.getGuiLabel());
    bean.setPercent(object.getPercent());
    bean.setMoDsc(object.getAmount());
    bean.setReason(object.getReason());
    //Added Discount Code
    bean.setDiscountCode(object.getDiscountCode());
    if (object.getEmployee() != null)
      bean.setEmployeeId(object.getEmployee().getId());
    bean.setCorporateId(object.getCorporateId());
    bean.setAdvertisingCode(object.getAdvertisingCode());
    bean.setIsSigRequired(object.isSignatureRequired());
    bean.setIsAdditionToMd(object.isInAdditionToMarkdown());
    bean.setIsDscPercent(object.isDiscountPercent());
    bean.setCnvtToMkdn(object.getConvertToMarkdown());
    if (object instanceof RewardDiscount && ((RewardDiscount)object).getRewardCard() != null)
      bean.setAuthCode(((RewardDiscount)object).getRewardCard().getManualAuthCode());
    return  (bean);
  }

  /**
   * put your documentation comment here
   * @param transactionId
   * @param sequenceNumber
   * @param object
   * @return 
   */
  private TrLtmEmDscOracleBean toTrLtmEmDscBean (String transactionId, int sequenceNumber, CMSEmployeeDiscount object) {
    TrLtmEmDscOracleBean bean = new TrLtmEmDscOracleBean();
    bean.setAiLnItm(sequenceNumber);
    bean.setAiTrn(transactionId);
    bean.setIdEm(object.getEmployee().getId());
    bean.setIdGpEmDsc(null);
    bean.setMoItmLnEmDsc(object.getAmount());
    return  bean;
  }
}



