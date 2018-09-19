/*
 * put your module comment here
 * formatted with JxBeauty (c) johann.langhofer@nextra.at
 */
//
// Copyright 2002, Retek Inc. All Rights Reserved.
//
/*
 History:
 +------+------------+-----------+-----------+----------------------------------------------+
 | Ver# | Date       | By        | Defect #  | Description                                  |
 +------+------------+-----------+-----------+----------------------------------------------+
 | 4    | 08-10-2005 | Vikram    | 638       | Consignment/Pre-Sale ID is set using new     |
 |      |            |           |           | sequence ARM_SEQ_CSG_PRS_ID                  |
 +------+------------+-----------+-----------+----------------------------------------------+
 | 3    | 04-29-2005 | Pankaja   | N/A       | New method for updation of the expiration dt |
 --------------------------------------------------------------------------------------------
 | 2    | 04-12-2005 | Rajesh    | N/A       | Specs Presale impl                           |
 +------+------------+-----------+-----------+----------------------------------------------+
 */


package  com.chelseasystems.cs.dataaccess.artsoracle.dao;

import  com.chelseasystems.cs.dataaccess.*;
import  com.chelseasystems.cs.dataaccess.artsoracle.databean.*;
import  com.chelseasystems.cs.pos.PresaleTransaction;
import  com.chelseasystems.cr.database.ParametricStatement;
import  com.chelseasystems.cs.database.CMSParametricStatement;
import  java.util.Date;
import  java.sql.*;
import  java.util.*;


/**
 * put your documentation comment here
 */
public class ArmPosPrsOracleDAO extends BaseOracleDAO
    implements ArmPosPrsDAO {
  private static String selectSql = ArmPosPrsOracleBean.selectSql;
  private static String insertSql = ArmPosPrsOracleBean.insertSql;
  private static String updateSql = ArmPosPrsOracleBean.updateSql + "where AI_TRN = ?";
  private static String updateExpirateDtSql = ArmPosPrsOracleBean.updateExpirationDtSql + "where AI_TRN = ?";
  private static String deleteSql = ArmPosPrsOracleBean.deleteSql + "where AI_TRN = ?";

  /**
   * put your documentation comment here
   * @param object
   * @exception SQLException
   */
  public void insert (PresaleTransaction object) throws SQLException {
    this.execute(this.getInsertSQL(object));
  	//executeCaseSensitive(this.getInsertSQL(object));
  }

  /**
   * put your documentation comment here
   * @param object
   * @return
   * @exception SQLException
   */
  public ParametricStatement[] getInsertSQL (PresaleTransaction object) throws SQLException {
    List statements = new ArrayList();
    ArmPosPrsOracleBean armPosPrsBean = null;
    armPosPrsBean = fromObjectToBean(object);
    //Added for presale issue US START
    //Now it is inserting all presale credit card number in mixed case(encrypted form) before it was saving that in Upper case
    statements.add(new CMSParametricStatement(armPosPrsBean.getInsertSql(), armPosPrsBean.toList(), true));
    return  (ParametricStatement[])statements.toArray(new ParametricStatement[0]);
  }

  /**
   * put your documentation comment here
   * @param object
   * @return
   * @exception SQLException
   */
  public ParametricStatement getUpdateExpirationDtSQL (PresaleTransaction object) throws SQLException {
    List params = new ArrayList();
    params.add(object.getExpirationDate());
    params.add(object.getCompositeTransaction().getId());
    return  new ParametricStatement(this.updateExpirateDtSql, params);
  }

  /**
   * put your documentation comment here
   * @param object
   * @return
   * @exception SQLException
   */
  public PresaleTransaction updateExpirationDt (PresaleTransaction object) throws SQLException {
    this.execute(this.getUpdateExpirationDtSQL(object));
    return  object;
  }

  /**
   * put your documentation comment here
   * @param txnId
   * @return
   * @exception SQLException
   */
  public ArmPosPrsOracleBean getPresaleTransaction (String txnId) throws SQLException {
    BaseOracleBean[] beans;
    List params = new ArrayList();
    params.add(txnId);
    try {
    	beans = this.query(new ArmPosPrsOracleBean(), BaseOracleDAO.where(ArmPosPrsOracleBean.COL_AI_TRN), params);
      if (beans != null && beans.length > 0)
        return  (ArmPosPrsOracleBean)beans[0];
    } catch (Exception e) {
      System.out.println("***Exception in ArmPosPrsOracleDAO.getPresaletransaction***");
      e.printStackTrace();
      throw  (SQLException)e;
    }
    return  null;
  }

  /**
   * put your documentation comment here
   * @param object
   * @return
   * @exception SQLException
   */
  private ArmPosPrsOracleBean fromObjectToBean (PresaleTransaction object) throws SQLException {
    ArmPosPrsOracleBean bean = new ArmPosPrsOracleBean();
    bean.setAiTrn(object.getCompositeTransaction().getId());
    object.doSetPresaleId(getNextPresaleId());
    bean.setIdPresale(object.getPresaleId());
    bean.setExpDt(object.getExpirationDate());
    bean.setCreditCardNumber(object.getCreditCardNumber());
    bean.setCreditCardType(object.getCardType());
    bean.setBillingZipCode(object.getCardZipCode());
    bean.setCreditCardExpirationDate(object.getCardExpirationDate());
    return  bean;
  }

  /**
   * put your documentation comment here
   * @return
   * @exception SQLException
   */
  private String getNextPresaleId () throws SQLException {
    String sql = "select ARM_SEQ_CSG_PRS_ID.nextVal from dual";
    String[] ids = this.queryForIds(sql, null);
    if (ids == null || ids.length != 1)
      return  null;
    return  ids[0];
  }
}



