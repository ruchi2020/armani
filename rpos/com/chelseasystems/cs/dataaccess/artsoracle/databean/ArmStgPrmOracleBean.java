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
 * This class is an object representation of the Arts database table ARM_STG_PRM<BR>
 * Followings are the column of the table: <BR>
 *     START_DATE -- DATE(7)<BR>
 *     END_DATE -- DATE(7)<BR>
 *     STORE_ID -- VARCHAR2(10)<BR>
 *     PROMOTION_NUM -- VARCHAR2(10)<BR>
 *     APPLY_TO -- CHAR(1)<BR>
 *     DISCOUNT_AMOUNT -- NUMBER(20)<BR>
 *     DISCOUNT_PERCENT -- NUMBER(20)<BR>
 *     DISCOUNT_TYPE -- CHAR(1)<BR>
 *     THRESHOLD_AMT -- NUMBER(20)<BR>
 *     IS_DISCOUNT -- CHAR(1)<BR>
 *     APPLICABLE_ITM -- CHAR(1)<BR>
 *     STG_EVENT_ID -- NUMBER(22)<BR>
 *     STG_STATUS -- NUMBER(22)<BR>
 *     STG_ERROR_MESSAGE -- VARCHAR2(255)<BR>
 *     STG_LOAD_DATE -- DATE(7)<BR>
 *     STG_PROCESS_DATE -- DATE(7)<BR>
 *	   PROMOTION_NAME -- VARCHAR2(50)<BR>
 */
public class ArmStgPrmOracleBean extends BaseOracleBean {

  public ArmStgPrmOracleBean() {}

  public static String selectSql = "select START_DATE, END_DATE, STORE_ID, PROMOTION_NUM, APPLY_TO, DISCOUNT_AMOUNT, DISCOUNT_PERCENT, DISCOUNT_TYPE, THRESHOLD_AMT, DECODE(IS_DISCOUNT,'Y', '1', 'N', '0') IS_DISCOUNT, APPLICABLE_ITM, STG_EVENT_ID, STG_STATUS, STG_ERROR_MESSAGE, STG_LOAD_DATE, STG_PROCESS_DATE, PROMOTION_NAME from ARM_STG_PRM ";
  public static String insertSql = "insert into ARM_STG_PRM (START_DATE, END_DATE, STORE_ID, PROMOTION_NUM, APPLY_TO, DISCOUNT_AMOUNT, DISCOUNT_PERCENT, DISCOUNT_TYPE, THRESHOLD_AMT, IS_DISCOUNT, APPLICABLE_ITM, STG_EVENT_ID, STG_STATUS, STG_ERROR_MESSAGE, STG_LOAD_DATE, STG_PROCESS_DATE, PROMOTION_NAME) values (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
//  public static String updateSql = "update ARM_STG_PRM set START_DATE = ?, END_DATE = ?, STORE_ID = ?, PROMOTION_NUM = ?, APPLY_TO = ?, DISCOUNT_AMOUNT = ?, DISCOUNT_PERCENT = ?, DISCOUNT_TYPE = ?, THRESHOLD_AMT = ?, IS_DISCOUNT = ?, APPLICABLE_ITM = ?, STG_EVENT_ID = ?, STG_STATUS = ?, STG_ERROR_MESSAGE = ?, STG_LOAD_DATE = ?, STG_PROCESS_DATE = ?, PROMOTION_NAME = ? ";
  public static String updateSql ="update ARM_STG_PRM set STG_PROCESS_DATE = SYSDATE, STG_STATUS = ?, STG_ERROR_MESSAGE = ? where STG_EVENT_ID = ?";
  public static String deleteSql = "delete from ARM_STG_PRM ";

  public static String TABLE_NAME = "ARM_STG_PRM";
  public static String COL_START_DATE = "ARM_STG_PRM.START_DATE";
  public static String COL_END_DATE = "ARM_STG_PRM.END_DATE";
  public static String COL_STORE_ID = "ARM_STG_PRM.STORE_ID";
  public static String COL_PROMOTION_NUM = "ARM_STG_PRM.PROMOTION_NUM";
  public static String COL_APPLY_TO = "ARM_STG_PRM.APPLY_TO";
  public static String COL_DISCOUNT_AMOUNT = "ARM_STG_PRM.DISCOUNT_AMOUNT";
  public static String COL_DISCOUNT_PERCENT = "ARM_STG_PRM.DISCOUNT_PERCENT";
  public static String COL_DISCOUNT_TYPE = "ARM_STG_PRM.DISCOUNT_TYPE";
  public static String COL_THRESHOLD_AMT = "ARM_STG_PRM.THRESHOLD_AMT";
  public static String COL_IS_DISCOUNT = "ARM_STG_PRM.IS_DISCOUNT";
  public static String COL_APPLICABLE_ITM = "ARM_STG_PRM.APPLICABLE_ITM";
  public static String COL_STG_EVENT_ID = "ARM_STG_PRM.STG_EVENT_ID";
  public static String COL_STG_STATUS = "ARM_STG_PRM.STG_STATUS";
  public static String COL_STG_ERROR_MESSAGE = "ARM_STG_PRM.STG_ERROR_MESSAGE";
  public static String COL_STG_LOAD_DATE = "ARM_STG_PRM.STG_LOAD_DATE";
  public static String COL_STG_PROCESS_DATE = "ARM_STG_PRM.STG_PROCESS_DATE";
  public static String COL_PROMOTION_NAME = "ARM_STG_PRM.PROMOTION_NAME";

  public String getSelectSql() { return selectSql; }
  public String getInsertSql() { return insertSql; }
  public String getUpdateSql() { return updateSql; }
  public String getDeleteSql() { return deleteSql; }

  private Date startDate;
  private Date endDate;
  private String storeId;
  private String promotionNum;
  private String applyTo;
  private Double discountAmount;
  private Double discountPercent;
  private String discountType;
  private Double thresholdAmt;
  private Boolean isDiscount;
  private String applicableItm;
  private Long stgEventId;
  private Long stgStatus;
  private String stgErrorMessage;
  private Date stgLoadDate;
  private Date stgProcessDate;
  private String promotionName;

  public Date getStartDate() { return this.startDate; }
  public void setStartDate(Date startDate) { this.startDate = startDate; }

  public Date getEndDate() { return this.endDate; }
  public void setEndDate(Date endDate) { this.endDate = endDate; }

  public String getStoreId() { return this.storeId; }
  public void setStoreId(String storeId) { this.storeId = storeId; }

  public String getPromotionNum() { return this.promotionNum; }
  public void setPromotionNum(String promotionNum) { this.promotionNum = promotionNum; }

  public String getApplyTo() { return this.applyTo; }
  public void setApplyTo(String applyTo) { this.applyTo = applyTo; }

  public Double getDiscountAmount() { return this.discountAmount; }
  public void setDiscountAmount(Double discountAmount) { this.discountAmount = discountAmount; }
  public void setDiscountAmount(double discountAmount) { this.discountAmount = new Double(discountAmount); }
  public void setDiscountAmount(int discountAmount) { this.discountAmount = new Double((double)discountAmount); }

  public Double getDiscountPercent() { return this.discountPercent; }
  public void setDiscountPercent(Double discountPercent) { this.discountPercent = discountPercent; }
  public void setDiscountPercent(double discountPercent) { this.discountPercent = new Double(discountPercent); }
  public void setDiscountPercent(int discountPercent) { this.discountPercent = new Double((double)discountPercent); }

  public String getDiscountType() { return this.discountType; }
  public void setDiscountType(String discountType) { this.discountType = discountType; }

  public Double getThresholdAmt() { return this.thresholdAmt; }
  public void setThresholdAmt(Double thresholdAmt) { this.thresholdAmt = thresholdAmt; }
  public void setThresholdAmt(double thresholdAmt) { this.thresholdAmt = new Double(thresholdAmt); }
  public void setThresholdAmt(int thresholdAmt) { this.thresholdAmt = new Double((double)thresholdAmt); }

  public Boolean getIsDiscount() { return this.isDiscount; }
  public void setIsDiscount(Boolean isDiscount) { this.isDiscount = isDiscount; }

  public String getApplicableItm() { return this.applicableItm; }
  public void setApplicableItm(String applicableItm) { this.applicableItm = applicableItm; }

  public Long getStgEventId() { return this.stgEventId; }
  public void setStgEventId(Long stgEventId) { this.stgEventId = stgEventId; }
  public void setStgEventId(long stgEventId) { this.stgEventId = new Long(stgEventId); }
  public void setStgEventId(int stgEventId) { this.stgEventId = new Long((long)stgEventId); }

  public Long getStgStatus() { return this.stgStatus; }
  public void setStgStatus(Long stgStatus) { this.stgStatus = stgStatus; }
  public void setStgStatus(long stgStatus) { this.stgStatus = new Long(stgStatus); }
//  public void setStgStatus(int stgStatus) { this.stgStatus = new Boolean((boolean)stgStatus); }

  public String getStgErrorMessage() { return this.stgErrorMessage; }
  public void setStgErrorMessage(String stgErrorMessage) { this.stgErrorMessage = stgErrorMessage; }

  public Date getStgLoadDate() { return this.stgLoadDate; }
  public void setStgLoadDate(Date stgLoadDate) { this.stgLoadDate = stgLoadDate; }

  public Date getStgProcessDate() { return this.stgProcessDate; }
  public void setStgProcessDate(Date stgProcessDate) { this.stgProcessDate = stgProcessDate; }

  public String getPromotionName() { return this.promotionName; }
  public void setPromotionName(String promotionName) { this.promotionName = promotionName; }

  public BaseOracleBean[] getDatabeans(ResultSet rs) throws SQLException {
    ArrayList list = new ArrayList();
    while(rs.next()) {
      ArmStgPrmOracleBean bean = new ArmStgPrmOracleBean();
      bean.startDate = getDateFromResultSet(rs, "START_DATE");
      bean.endDate = getDateFromResultSet(rs, "END_DATE");
      bean.storeId = getStringFromResultSet(rs, "STORE_ID");
      bean.promotionNum = getStringFromResultSet(rs, "PROMOTION_NUM");
      bean.applyTo = getStringFromResultSet(rs, "APPLY_TO");
      bean.discountAmount = getDoubleFromResultSet(rs, "DISCOUNT_AMOUNT");
      bean.discountPercent = getDoubleFromResultSet(rs, "DISCOUNT_PERCENT");
      bean.discountType = getStringFromResultSet(rs, "DISCOUNT_TYPE");
      bean.thresholdAmt = getDoubleFromResultSet(rs, "THRESHOLD_AMT");
      bean.isDiscount = getBooleanFromResultSet(rs, "IS_DISCOUNT");
      bean.applicableItm = getStringFromResultSet(rs, "APPLICABLE_ITM");
      bean.stgEventId = getLongFromResultSet(rs, "STG_EVENT_ID");
      bean.stgStatus = getLongFromResultSet(rs, "STG_STATUS");
      bean.stgErrorMessage = getStringFromResultSet(rs, "STG_ERROR_MESSAGE");
      bean.stgLoadDate = getDateFromResultSet(rs, "STG_LOAD_DATE");
      bean.stgProcessDate = getDateFromResultSet(rs, "STG_PROCESS_DATE");
      bean.promotionName = getStringFromResultSet(rs, "PROMOTION_NAME");
      list.add(bean);
    }
    return (ArmStgPrmOracleBean[]) list.toArray(new ArmStgPrmOracleBean[0]);
  }

  public List toList() {
    List list = new ArrayList();
    addToList(list, this.getStartDate(), Types.TIMESTAMP);
    addToList(list, this.getEndDate(), Types.TIMESTAMP);
    addToList(list, this.getStoreId(), Types.VARCHAR);
    addToList(list, this.getPromotionNum(), Types.VARCHAR);
    addToList(list, this.getApplyTo(), Types.VARCHAR);
    addToList(list, this.getDiscountAmount(), Types.DECIMAL);
    addToList(list, this.getDiscountPercent(), Types.DECIMAL);
    addToList(list, this.getDiscountType(), Types.VARCHAR);
    addToList(list, this.getThresholdAmt(), Types.DECIMAL);
    addToList(list, this.getIsDiscount(), Types.VARCHAR);
    addToList(list, this.getApplicableItm(), Types.VARCHAR);
    addToList(list, this.getStgEventId(), Types.DECIMAL);
    addToList(list, this.getStgStatus(), Types.DECIMAL);
    addToList(list, this.getStgErrorMessage(), Types.VARCHAR);
    addToList(list, this.getStgLoadDate(), Types.TIMESTAMP);
    addToList(list, this.getStgProcessDate(), Types.TIMESTAMP);
    addToList(list, this.getPromotionName(), Types.VARCHAR);
    return list;
  }

}
