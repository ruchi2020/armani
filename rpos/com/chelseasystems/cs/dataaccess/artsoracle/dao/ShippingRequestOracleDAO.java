/*
 * put your module comment here
 * formatted with JxBeauty (c) johann.langhofer@nextra.at
 */
//
// Copyright 1999-2001, Chelsea Market Systems
//
/*
 History:
 +------+------------+-----------+-----------+----------------------------------------------+
 | Ver# | Date       | By        | Defect #  | Description                                  |
 +------+------------+-----------+-----------+----------------------------------------------+
 | 2    | 04-12-2005 | Khyati    | N/A       |1.Send Sale specification.Added Address2      |
 --------------------------------------------------------------------------------------------
 | 1    | 04-12-2005 | Base      | N/A       |                                              |
 --------------------------------------------------------------------------------------------
 */


package  com.chelseasystems.cs.dataaccess.artsoracle.dao;

import  java.sql.SQLException;
import  com.chelseasystems.cr.database.ParametricStatement;
import  com.chelseasystems.cr.pos.CompositePOSTransaction;
import  com.chelseasystems.cr.pos.ShippingRequest;
import  com.chelseasystems.cs.dataaccess.ShippingRequestDAO;
import  com.chelseasystems.cs.dataaccess.artsoracle.databean.BaseOracleBean;
import  com.chelseasystems.cs.dataaccess.artsoracle.databean.RkShipReqOracleBean;
import  com.chelseasystems.cs.pos.CMSShippingRequest;


/**
 *
 *  ShippingRequest Data Access Object.<br>
 *  This object encapsulates all database access for ShippingRequest.<p>
 *  Below table shows the mapping between the object and the database tables and columns:<p>
 *  <table border=1>
 *    <tr><td><b>Object Attribute</b></td><td><b>Table Name</b></td><td><b>Column Name</b></td></tr>
 *    <tr><td>Address</td><td>RK_SHIPPING_REQUEST</td><td>ADDRESS</td></tr>
 *    <tr><td>Apartment</td><td>RK_SHIPPING_REQUEST</td><td>APARTMENT</td></tr>
 *    <tr><td>City</td><td>RK_SHIPPING_REQUEST</td><td>CITY</td></tr>
 *    <tr><td>Country</td><td>RK_SHIPPING_REQUEST</td><td>COUNTRY</td></tr>
 *    <tr><td>FirstName</td><td>RK_SHIPPING_REQUEST</td><td>FIRST_NAME</td></tr>
 *    <tr><td>GiftMessage</td><td>RK_SHIPPING_REQUEST</td><td>GIFT_MESSAGE</td></tr>
 *    <tr><td>LastName</td><td>RK_SHIPPING_REQUEST</td><td>LAST_NAME</td></tr>
 *    <tr><td>Phone</td><td>RK_SHIPPING_REQUEST</td><td>PHONE</td></tr>
 *    <tr><td>SpecialInstructions</td><td>RK_SHIPPING_REQUEST</td><td>SPECIAL_INSTR</td></tr>
 *    <tr><td>State</td><td>RK_SHIPPING_REQUEST</td><td>STATE</td></tr>
 *    <tr><td>ZipCode</td><td>RK_SHIPPING_REQUEST</td><td>ZIP_CODE</td></tr>
 *    <tr><td>Address2</td><td>RK_SHIPPING_REQUEST</td><td>ADDRESS2</td></tr>
 *  </table>
 *
 *  @see ShippingRequest
 *  @see com.chelseasystems.cs.dataaccess.artsoracle.databean.BaseOracleBean
 *  @see com.chelseasystems.cs.dataaccess.artsoracle.databean.RkShipReqOracleBean
 *
 */
public class ShippingRequestOracleDAO extends BaseOracleDAO
    implements ShippingRequestDAO {
  private static String selectSql = RkShipReqOracleBean.selectSql;
  private static String insertSql = RkShipReqOracleBean.insertSql;
  private static String updateSql = RkShipReqOracleBean.updateSql + where(RkShipReqOracleBean.COL_AI_TRN);
  private static String deleteSql = RkShipReqOracleBean.deleteSql + where(RkShipReqOracleBean.COL_AI_TRN);

  /**
   * put your documentation comment here
   * @param object
   * @param seqNum
   * @return 
   * @exception SQLException
   */
  ParametricStatement getInsertSQL(ShippingRequest object, int seqNum)
      throws SQLException {
    return  new ParametricStatement(insertSql, fromObjectToBean(object, seqNum).toList());
  }

  /**
   * put your documentation comment here
   * @param transaction
   * @exception SQLException
   */
  protected void addShippingRequestIntoCompositePOSTransaction(CompositePOSTransaction transaction)
      throws SQLException {
    String whereSql = where(RkShipReqOracleBean.COL_AI_TRN);
    fromBeansToObjects(query(new RkShipReqOracleBean(), whereSql, transaction.getId()), transaction);
  }

  /**
   * put your documentation comment here
   * @param beans
   * @param transaction
   * @return 
   */
  private ShippingRequest[] fromBeansToObjects (BaseOracleBean[] beans, CompositePOSTransaction transaction) {
    RkShipReqOracleBean[] sortedBeans = sortShippingRequestBeansBySeqNum(beans);
    ShippingRequest[] array = new CMSShippingRequest[beans.length];
    for (int i = 0; i < array.length; i++)
      array[i] = fromBeanToObject(sortedBeans[i], transaction);
    return  array;
  }

  /**
   * put your documentation comment here
   * @param baseBean
   * @param tranaction
   * @return 
   */
  private ShippingRequest fromBeanToObject (BaseOracleBean baseBean, CompositePOSTransaction tranaction) {
    RkShipReqOracleBean bean = (RkShipReqOracleBean)baseBean;
    ShippingRequest object = new CMSShippingRequest(tranaction);
    ((CMSShippingRequest)object).doSetSeqNum(bean.getSeqNum().toString());
    object.doSetFirstName(bean.getFirstName());
    object.doSetLastName(bean.getLastName());
    object.doSetAddress(bean.getAddress());
    //ks: Added address 2
    ((CMSShippingRequest)object).doSetAddress2(bean.getAddress2());
    object.doSetApartment(bean.getApartment());
    object.doSetCity(bean.getCity());
    object.doSetState(bean.getState());
    object.doSetZipCode(bean.getZipCode());
    object.doSetCountry(bean.getCountry());
    object.doSetPhone(bean.getPhone());
    object.doSetSpecialInstructions(bean.getSpecialInstr());
    object.doSetGiftMessage(bean.getGiftMessage());
    ((CMSShippingRequest)object).doSetAddressFormat(bean.getAdsFormat());
    return  object;
  }

  /**
   * put your documentation comment here
   * @param object
   * @param seqNum
   * @return 
   */
  private RkShipReqOracleBean fromObjectToBean (ShippingRequest object, int seqNum) {
    RkShipReqOracleBean bean = new RkShipReqOracleBean();
    bean.setAiTrn(object.getCompositeTransaction().getId());
    bean.setSeqNum(seqNum);
    bean.setFirstName(object.getFirstName());
    bean.setLastName(object.getLastName());
    bean.setAddress(object.getAddress());
    bean.setApartment(object.getApartment());
    bean.setCity(object.getCity());
    bean.setState(object.getState());
    bean.setZipCode(object.getZipCode());
    bean.setCountry(object.getCountry());
    bean.setPhone(object.getPhone());
    bean.setSpecialInstr(object.getSpecialInstructions());
    bean.setGiftMessage(object.getGiftMessage());
    //ks: Added address 2
    bean.setAddress2(((CMSShippingRequest)object).getAddress2());
    bean.setAdsFormat(((CMSShippingRequest)object).getAddressFormat());
    return  bean;
  }

  /**
   * put your documentation comment here
   * @param beans
   * @return 
   */
  private RkShipReqOracleBean[] sortShippingRequestBeansBySeqNum (BaseOracleBean[] beans) {
    RkShipReqOracleBean[] sortedShippingRequestBean = new RkShipReqOracleBean[beans.length];
    for (int i = 0; i < beans.length; i++) {
      RkShipReqOracleBean bean = (RkShipReqOracleBean)beans[i];
      Long seqNum = bean.getSeqNum();
      sortedShippingRequestBean[seqNum.intValue()] = bean;
    }
    return  sortedShippingRequestBean;
  }
}



