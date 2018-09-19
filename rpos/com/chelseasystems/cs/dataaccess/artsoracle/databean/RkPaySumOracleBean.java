//
// Copyright 2002, Retek Inc. All Rights Reserved.
//
package com.chelseasystems.cs.dataaccess.artsoracle.databean;

import java.util.Date;
import java.util.Calendar;
import java.util.List;
import java.util.ArrayList;
import java.sql.SQLException;
import java.sql.ResultSet;
import java.sql.Types;
import com.chelseasystems.cr.currency.ArmCurrency;

/**
 *
 * This class is an object representation of the Arts database table RK_PAY_SUM<BR>
 * Followings are the column of the table: <BR>
 *     ID(PK) -- VARCHAR2(50)<BR>
 *     PAYMENT_DATE -- DATE(7)<BR>
 *     REGISTER_ID -- VARCHAR2(50)<BR>
 *     PAYMENT_TYPE -- VARCHAR2(50)<BR>
 *     PAYMENT_TOTAL -- VARCHAR2(75)<BR>
 *     ID_STR_RT -- VARCHAR2(128)<BR>
 *     ID_EM -- VARCHAR2(128)<BR>
 *     MEDIA_COUNT -- NUMBER(22)<BR>
 *
 */
public class RkPaySumOracleBean extends BaseOracleBean {

  public RkPaySumOracleBean() {}

  public static String selectSql = "select ID, PAYMENT_DATE, REGISTER_ID, PAYMENT_TYPE, PAYMENT_TOTAL, ID_STR_RT, ID_EM, MEDIA_COUNT from RK_PAY_SUM ";
  public static String insertSql = "insert into RK_PAY_SUM (ID, PAYMENT_DATE, REGISTER_ID, PAYMENT_TYPE, PAYMENT_TOTAL, ID_STR_RT, ID_EM, MEDIA_COUNT) values (?, ?, ?, ?, ?, ?, ?, ?)";
  public static String updateSql = "update RK_PAY_SUM set ID = ?, PAYMENT_DATE = ?, REGISTER_ID = ?, PAYMENT_TYPE = ?, PAYMENT_TOTAL = ?, ID_STR_RT = ?, ID_EM = ?, MEDIA_COUNT = ? ";
  public static String deleteSql = "delete from RK_PAY_SUM ";

  public static String TABLE_NAME = "RK_PAY_SUM";
  public static String COL_ID = "RK_PAY_SUM.ID";
  public static String COL_PAYMENT_DATE = "RK_PAY_SUM.PAYMENT_DATE";
  public static String COL_REGISTER_ID = "RK_PAY_SUM.REGISTER_ID";
  public static String COL_PAYMENT_TYPE = "RK_PAY_SUM.PAYMENT_TYPE";
  public static String COL_PAYMENT_TOTAL = "RK_PAY_SUM.PAYMENT_TOTAL";
  public static String COL_ID_STR_RT = "RK_PAY_SUM.ID_STR_RT";
  public static String COL_ID_EM = "RK_PAY_SUM.ID_EM";
  public static String COL_MEDIA_COUNT = "RK_PAY_SUM.MEDIA_COUNT";

  public String getSelectSql() { return selectSql; }
  public String getInsertSql() { return insertSql; }
  public String getUpdateSql() { return updateSql; }
  public String getDeleteSql() { return deleteSql; }

  private String id;
  private Date paymentDate;
  private String registerId;
  private String paymentType;
  private ArmCurrency paymentTotal;
  private String idStrRt;
  private String idEm;
  private Long mediaCount;

  public String getId() { return this.id; }
  public void setId(String id) { this.id = id; }

  public Date getPaymentDate() { return this.paymentDate; }
  public void setPaymentDate(Date paymentDate) { this.paymentDate = paymentDate; }

  public String getRegisterId() { return this.registerId; }
  public void setRegisterId(String registerId) { this.registerId = registerId; }

  public String getPaymentType() { return this.paymentType; }
  public void setPaymentType(String paymentType) { this.paymentType = paymentType; }

  public ArmCurrency getPaymentTotal() { return this.paymentTotal; }
  public void setPaymentTotal(ArmCurrency paymentTotal) { this.paymentTotal = paymentTotal; }

  public String getIdStrRt() { return this.idStrRt; }
  public void setIdStrRt(String idStrRt) { this.idStrRt = idStrRt; }

  public String getIdEm() { return this.idEm; }
  public void setIdEm(String idEm) { this.idEm = idEm; }

  public Long getMediaCount() { return this.mediaCount; }
  public void setMediaCount(Long mediaCount) { this.mediaCount = mediaCount; }
  public void setMediaCount(long mediaCount) { this.mediaCount = new Long(mediaCount); }
  public void setMediaCount(int mediaCount) { this.mediaCount = new Long((long)mediaCount); }

  public BaseOracleBean[] getDatabeans(ResultSet rs) throws SQLException {
    ArrayList list = new ArrayList();
    while(rs.next()) {
      RkPaySumOracleBean bean = new RkPaySumOracleBean();
      bean.id = getStringFromResultSet(rs, "ID");
      bean.paymentDate = getDateFromResultSet(rs, "PAYMENT_DATE");
      bean.registerId = getStringFromResultSet(rs, "REGISTER_ID");
      bean.paymentType = getStringFromResultSet(rs, "PAYMENT_TYPE");
      bean.paymentTotal = getCurrencyFromResultSet(rs, "PAYMENT_TOTAL");
      bean.idStrRt = getStringFromResultSet(rs, "ID_STR_RT");
      bean.idEm = getStringFromResultSet(rs, "ID_EM");
      bean.mediaCount = getLongFromResultSet(rs, "MEDIA_COUNT");
      list.add(bean);
    }
    return (RkPaySumOracleBean[]) list.toArray(new RkPaySumOracleBean[0]);
  }

  public List toList() {
    List list = new ArrayList();
    addToList(list, this.getId(), Types.VARCHAR);
    addToList(list, this.getPaymentDate(), Types.TIMESTAMP);
    addToList(list, this.getRegisterId(), Types.VARCHAR);
    addToList(list, this.getPaymentType(), Types.VARCHAR);
    addToList(list, this.getPaymentTotal(), Types.VARCHAR);
    addToList(list, this.getIdStrRt(), Types.VARCHAR);
    addToList(list, this.getIdEm(), Types.VARCHAR);
    addToList(list, this.getMediaCount(), Types.DECIMAL);
    return list;
  }

}
