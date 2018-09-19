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
 | 2    | 04-12-2005 | Rajesh    | N/A       | Specs Consignment impl                       |
 +------+------------+-----------+-----------+----------------------------------------------+
 */


package  com.chelseasystems.cs.dataaccess.artsoracle.dao;

import  com.chelseasystems.cs.dataaccess.*;
import  com.chelseasystems.cs.dataaccess.artsoracle.databean.*;
import  com.chelseasystems.cs.pos.ConsignmentTransaction;
import  com.chelseasystems.cr.database.ParametricStatement;
import  java.util.Date;
import  java.sql.*;
import  java.util.*;


/**
 * put your documentation comment here
 */
public class ArmPosCsgOracleDAO extends BaseOracleDAO
    implements ArmPosCsgDAO {
  private static String selectSql = ArmPosCsgOracleBean.selectSql;
  private static String insertSql = ArmPosCsgOracleBean.insertSql;
  private static String updateSql = ArmPosCsgOracleBean.updateSql + "where AI_TRN = ?";
  private static String updateExpirateDtSql = ArmPosCsgOracleBean.updateExpirationDtSql + "where AI_TRN = ?";
  private static String deleteSql = ArmPosCsgOracleBean.deleteSql + "where AI_TRN = ?";

  /**
   * put your documentation comment here
   * @param object
   * @exception SQLException
   */
  public void insert (ConsignmentTransaction object) throws SQLException {
    this.execute(this.getInsertSQL(object));
  }

  /**
   * put your documentation comment here
   * @param object
   * @return
   * @exception SQLException
   */
  public ParametricStatement[] getInsertSQL (ConsignmentTransaction object) throws SQLException {
    List statements = new ArrayList();
    ArmPosCsgOracleBean armPosCsgBean = null;
    armPosCsgBean = fromObjectToBean(object);
    statements.add(new ParametricStatement(armPosCsgBean.getInsertSql(), armPosCsgBean.toList()));
    return  (ParametricStatement[])statements.toArray(new ParametricStatement[0]);
  }

  /**
   * put your documentation comment here
   * @param object
   * @return
   * @exception SQLException
   */
  public ParametricStatement getUpdateExpirationDtSQL (ConsignmentTransaction object) throws SQLException {
    List params = new ArrayList();
    params.add(object.getExpirationDate());
    params.add(object.getCompositeTransaction().getId());
    return  new ParametricStatement(updateExpirateDtSql, params);
  }

  /**
   * put your documentation comment here
   * @param object
   * @return
   * @exception SQLException
   */
  public ConsignmentTransaction updateExpirationDt (ConsignmentTransaction object) throws SQLException {
    this.execute(this.getUpdateExpirationDtSQL(object));
    return  object;
  }

  /**
   * put your documentation comment here
   * @param txnId
   * @return
   * @exception SQLException
   */
  public ArmPosCsgOracleBean getConsignmentTransaction (String txnId) throws SQLException {
    BaseOracleBean[] beans;
    List params = new ArrayList();
    params.add(txnId);
    try {
      beans = this.query(new ArmPosCsgOracleBean(), BaseOracleDAO.where(ArmPosCsgOracleBean.COL_AI_TRN), params);
      if (beans != null && beans.length > 0)
        return  (ArmPosCsgOracleBean)beans[0];
    } catch (Exception e) {
      System.out.println("***Exception in ArmPosCsgOracleDAO.getConsignmenttransaction***");
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
  private ArmPosCsgOracleBean fromObjectToBean (ConsignmentTransaction object) throws SQLException {
    ArmPosCsgOracleBean bean = new ArmPosCsgOracleBean();
    bean.setAiTrn(object.getCompositeTransaction().getId());
    object.doSetConsignmentId(getNextConsignmentId());
    bean.setIdConsignment(object.getConsignmentId());
    bean.setExpDt(object.getExpirationDate());
    return  bean;
  }

  /**
   * put your documentation comment here
   * @return
   * @exception SQLException
   */
  private String getNextConsignmentId () throws SQLException {
    String sql = "select ARM_SEQ_CSG_PRS_ID.nextVal from dual";
    String[] ids = this.queryForIds(sql, null);
    if (ids == null || ids.length != 1)
      return  null;
    return  ids[0];
  }
}



