/*
 * put your module comment here
 * formatted with JxBeauty (c) johann.langhofer@nextra.at
 */


package  com.chelseasystems.cs.dataaccess.artsoracle.dao;

import  com.chelseasystems.cs.dataaccess.*;
import  com.chelseasystems.cs.dataaccess.artsoracle.databean.*;
import  com.chelseasystems.cs.pos.ReservationTransaction;
import  com.chelseasystems.cr.database.ParametricStatement;
import  java.util.Date;
import  java.sql.*;
import  java.util.*;


/*History:
 +------+------------+-----------+-----------+----------------------------------------------+
 | Ver# | Date       | By        | Defect #  | Description                                  |
 +------+------------+-----------+-----------+----------------------------------------------+
 | 1    | 07-21-2005 | Manpreet  | N/A       | Specs Reservation impl                       |
 +------+------------+-----------+-----------+----------------------------------------------+
 */
/**
 *
 * <p>Title:ArmPosRsvOracleDAO </p>
 * <p>Description: To persist Reservation Transaction</p>
 * <p>Copyright: Copyright (c) 2005</p>
 * <p>Company: SkillNet Inc</p>
 * @author Manpreet S Bawa
 * @version 1.0
 */
public class ArmPosRsvOracleDAO extends BaseOracleDAO
    implements ArmPosRsvDAO {
  private static String selectSql = ArmPosRsvOracleBean.selectSql;
  private static String insertSql = ArmPosRsvOracleBean.insertSql;
  private static String updateSql = ArmPosRsvOracleBean.updateSql + "where AI_TRN = ?";
  private static String updateExpirateDtSql = ArmPosRsvOracleBean.updateExpirationDtSql + "where AI_TRN = ?";
  private static String deleteSql = ArmPosRsvOracleBean.deleteSql + "where AI_TRN = ?";

  /**
   * put your documentation comment here
   * @param object
   * @exception SQLException
   */
  public void insert (ReservationTransaction object) throws SQLException {
    this.execute(this.getInsertSQL(object));
  }

  /**
   * put your documentation comment here
   * @param object
   * @return
   * @exception SQLException
   */
  public ParametricStatement[] getInsertSQL (ReservationTransaction object) throws SQLException {
    List statements = new ArrayList();
    ArmPosRsvOracleBean armPosRsvBean = null;
    armPosRsvBean = fromObjectToBean(object);
    statements.add(new ParametricStatement(armPosRsvBean.getInsertSql(), armPosRsvBean.toList()));
    return  (ParametricStatement[])statements.toArray(new ParametricStatement[0]);
  }

  /**
   * put your documentation comment here
   * @param object
   * @return
   * @exception SQLException
   */
  public ParametricStatement getUpdateExpirationDtSQL (ReservationTransaction object) throws SQLException {
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
  public ReservationTransaction updateExpirationDt (ReservationTransaction object) throws SQLException {
    this.execute(getUpdateExpirationDtSQL(object));
    return  object;
  }

  /**
   * put your documentation comment here
   * @param txnId
   * @return
   * @exception SQLException
   */
  public ArmPosRsvOracleBean getReservationTransaction (String txnId) throws SQLException {
    BaseOracleBean[] beans;
    List params = new ArrayList();
    params.add(txnId);
    try {
      beans = this.query(new ArmPosRsvOracleBean(), BaseOracleDAO.where(ArmPosRsvOracleBean.COL_AI_TRN), params);
      if (beans != null && beans.length > 0)
        return  (ArmPosRsvOracleBean)beans[0];
    } catch (Exception e) {
      System.out.println("***Exception in ArmPosRSVOracleDAO.getReservationtransaction***");
      e.printStackTrace();
      throw  (SQLException)e;
    }
    return  null;
  }

  /**
   * put your documentation comment here
   * @param object
   * @return
   */
  private ArmPosRsvOracleBean fromObjectToBean (ReservationTransaction object) throws SQLException {
    ArmPosRsvOracleBean bean = new ArmPosRsvOracleBean();
    bean.setAiTrn(object.getCompositeTransaction().getId());
    object.doSetReservationId(getNextReservationId());
    bean.setIdReservation(object.getReservationId());
    bean.setExpDt(object.getExpirationDate());
    bean.setDepositAmt(object.getDepositAmount());
    bean.setReasonCode(object.getReservationReason());
    if(object.getOriginalRSVOTxn()!= null)
      bean.setOrigRsvId(object.getOriginalRSVOTxn().getReservationId());
    return  bean;
  }
  
  /**
   * put your documentation comment here
   * @return
   * @exception SQLException
   */
  private String getNextReservationId () throws SQLException {
    String sql = "select ARM_SEQ_CSG_PRS_ID.nextVal from dual";
    String[] ids = this.queryForIds(sql, null);
    if (ids == null || ids.length != 1)
      return  null;
    return  ids[0];
  }
}



