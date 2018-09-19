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
 * This class is an object representation of the Arts database table TR_LTM_DSC<BR>
 * Followings are the column of the table: <BR>
 *     AI_TRN(PK) -- VARCHAR2(128)<BR>
 *     AI_LN_ITM(PK) -- NUMBER(22)<BR>
 *     TY_DSC -- VARCHAR2(20)<BR>
 *     MO_DSC -- VARCHAR2(75)<BR>
 *     CNVT_TO_MKDN -- NUMBER(1)<BR>
 *     IS_DSC_PERCENT -- NUMBER(1)<BR>
 *     IS_ADDITION_TO_MD -- NUMBER(1)<BR>
 *     IS_SIG_REQUIRED -- NUMBER(1)<BR>
 *     ADVERTISING_CODE -- VARCHAR2(200)<BR>
 *     CORPORATE_ID -- VARCHAR2(128)<BR>
 *     EMPLOYEE_ID -- VARCHAR2(128)<BR>
 *     REASON -- VARCHAR2(200)<BR>
 *     PERCENT -- NUMBER(7.4)<BR>
 *     GUI_LABEL -- VARCHAR2(200)<BR>
 *     TYPE -- VARCHAR2(200)<BR>
 *     DISCOUNT_CODE -- VARCHAR2(20)<BR>
 *     AUTH_CODE -- VARCHAR2(50)<BR>
 *
 */
public class TrLtmDscOracleBean extends BaseOracleBean {

  public TrLtmDscOracleBean() {}

  public static String selectSql = "select AI_TRN, AI_LN_ITM, TY_DSC, MO_DSC, CNVT_TO_MKDN, IS_DSC_PERCENT, IS_ADDITION_TO_MD, IS_SIG_REQUIRED, ADVERTISING_CODE, CORPORATE_ID, EMPLOYEE_ID, REASON, PERCENT, GUI_LABEL, TYPE, DISCOUNT_CODE, AUTH_CODE from TR_LTM_DSC ";
  public static String insertSql = "insert into TR_LTM_DSC (AI_TRN, AI_LN_ITM, TY_DSC, MO_DSC, CNVT_TO_MKDN, IS_DSC_PERCENT, IS_ADDITION_TO_MD, IS_SIG_REQUIRED, ADVERTISING_CODE, CORPORATE_ID, EMPLOYEE_ID, REASON, PERCENT, GUI_LABEL, TYPE, DISCOUNT_CODE, AUTH_CODE) values (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
  public static String updateSql = "update TR_LTM_DSC set AI_TRN = ?, AI_LN_ITM = ?, TY_DSC = ?, MO_DSC = ?, CNVT_TO_MKDN = ?, IS_DSC_PERCENT = ?, IS_ADDITION_TO_MD = ?, IS_SIG_REQUIRED = ?, ADVERTISING_CODE = ?, CORPORATE_ID = ?, EMPLOYEE_ID = ?, REASON = ?, PERCENT = ?, GUI_LABEL = ?, TYPE = ?, DISCOUNT_CODE = ?, AUTH_CODE = ? ";
  public static String deleteSql = "delete from TR_LTM_DSC ";

  public static String TABLE_NAME = "TR_LTM_DSC";
  public static String COL_AI_TRN = "TR_LTM_DSC.AI_TRN";
  public static String COL_AI_LN_ITM = "TR_LTM_DSC.AI_LN_ITM";
  public static String COL_TY_DSC = "TR_LTM_DSC.TY_DSC";
  public static String COL_MO_DSC = "TR_LTM_DSC.MO_DSC";
  public static String COL_CNVT_TO_MKDN = "TR_LTM_DSC.CNVT_TO_MKDN";
  public static String COL_IS_DSC_PERCENT = "TR_LTM_DSC.IS_DSC_PERCENT";
  public static String COL_IS_ADDITION_TO_MD = "TR_LTM_DSC.IS_ADDITION_TO_MD";
  public static String COL_IS_SIG_REQUIRED = "TR_LTM_DSC.IS_SIG_REQUIRED";
  public static String COL_ADVERTISING_CODE = "TR_LTM_DSC.ADVERTISING_CODE";
  public static String COL_CORPORATE_ID = "TR_LTM_DSC.CORPORATE_ID";
  public static String COL_EMPLOYEE_ID = "TR_LTM_DSC.EMPLOYEE_ID";
  public static String COL_REASON = "TR_LTM_DSC.REASON";
  public static String COL_PERCENT = "TR_LTM_DSC.PERCENT";
  public static String COL_GUI_LABEL = "TR_LTM_DSC.GUI_LABEL";
  public static String COL_TYPE = "TR_LTM_DSC.TYPE";
  public static String COL_DISCOUNT_CODE = "TR_LTM_DSC.DISCOUNT_CODE";
  public static String COL_AUTH_CODE = "TR_LTM_DSC.AUTH_CODE";

  public String getSelectSql() { return selectSql; }
  public String getInsertSql() { return insertSql; }
  public String getUpdateSql() { return updateSql; }
  public String getDeleteSql() { return deleteSql; }

  private String aiTrn;
  private Long aiLnItm;
  private String tyDsc;
  private ArmCurrency moDsc;
  private Boolean cnvtToMkdn;
  private Boolean isDscPercent;
  private Boolean isAdditionToMd;
  private Boolean isSigRequired;
  private String advertisingCode;
  private String corporateId;
  private String employeeId;
  private String reason;
  private Double percent;
  private String guiLabel;
  private String type;
  private String discountCode;
  private String authCode;

  public String getAiTrn() { return this.aiTrn; }
  public void setAiTrn(String aiTrn) { this.aiTrn = aiTrn; }

  public Long getAiLnItm() { return this.aiLnItm; }
  public void setAiLnItm(Long aiLnItm) { this.aiLnItm = aiLnItm; }
  public void setAiLnItm(long aiLnItm) { this.aiLnItm = new Long(aiLnItm); }
  public void setAiLnItm(int aiLnItm) { this.aiLnItm = new Long((long)aiLnItm); }

  public String getTyDsc() { return this.tyDsc; }
  public void setTyDsc(String tyDsc) { this.tyDsc = tyDsc; }

  public ArmCurrency getMoDsc() { return this.moDsc; }
  public void setMoDsc(ArmCurrency moDsc) { this.moDsc = moDsc; }

  public Boolean getCnvtToMkdn() { return this.cnvtToMkdn; }
  public void setCnvtToMkdn(Boolean cnvtToMkdn) { this.cnvtToMkdn = cnvtToMkdn; }
  public void setCnvtToMkdn(boolean cnvtToMkdn) { this.cnvtToMkdn = new Boolean(cnvtToMkdn); }

  public Boolean getIsDscPercent() { return this.isDscPercent; }
  public void setIsDscPercent(Boolean isDscPercent) { this.isDscPercent = isDscPercent; }
  public void setIsDscPercent(boolean isDscPercent) { this.isDscPercent = new Boolean(isDscPercent); }

  public Boolean getIsAdditionToMd() { return this.isAdditionToMd; }
  public void setIsAdditionToMd(Boolean isAdditionToMd) { this.isAdditionToMd = isAdditionToMd; }
  public void setIsAdditionToMd(boolean isAdditionToMd) { this.isAdditionToMd = new Boolean(isAdditionToMd); }

  public Boolean getIsSigRequired() { return this.isSigRequired; }
  public void setIsSigRequired(Boolean isSigRequired) { this.isSigRequired = isSigRequired; }
  public void setIsSigRequired(boolean isSigRequired) { this.isSigRequired = new Boolean(isSigRequired); }

  public String getAdvertisingCode() { return this.advertisingCode; }
  public void setAdvertisingCode(String advertisingCode) { this.advertisingCode = advertisingCode; }

  public String getCorporateId() { return this.corporateId; }
  public void setCorporateId(String corporateId) { this.corporateId = corporateId; }

  public String getEmployeeId() { return this.employeeId; }
  public void setEmployeeId(String employeeId) { this.employeeId = employeeId; }

  public String getReason() { return this.reason; }
  public void setReason(String reason) { this.reason = reason; }

  public Double getPercent() { return this.percent; }
  public void setPercent(Double percent) { this.percent = percent; }
  public void setPercent(double percent) { this.percent = new Double(percent); }

  public String getGuiLabel() { return this.guiLabel; }
  public void setGuiLabel(String guiLabel) { this.guiLabel = guiLabel; }

  public String getType() { return this.type; }
  public void setType(String type) { this.type = type; }

  public String getDiscountCode() { return this.discountCode; }
  public void setDiscountCode(String discountCode) { this.discountCode = discountCode; }

  public String getAuthCode() { return this.authCode; }
  public void setAuthCode(String authCode) { this.authCode = authCode; }

  public BaseOracleBean[] getDatabeans(ResultSet rs) throws SQLException {
    ArrayList list = new ArrayList();
    while(rs.next()) {
      TrLtmDscOracleBean bean = new TrLtmDscOracleBean();
      bean.aiTrn = getStringFromResultSet(rs, "AI_TRN");
      bean.aiLnItm = getLongFromResultSet(rs, "AI_LN_ITM");
      bean.tyDsc = getStringFromResultSet(rs, "TY_DSC");
      bean.moDsc = getCurrencyFromResultSet(rs, "MO_DSC");
      bean.cnvtToMkdn = getBooleanFromResultSet(rs, "CNVT_TO_MKDN");
      bean.isDscPercent = getBooleanFromResultSet(rs, "IS_DSC_PERCENT");
      bean.isAdditionToMd = getBooleanFromResultSet(rs, "IS_ADDITION_TO_MD");
      bean.isSigRequired = getBooleanFromResultSet(rs, "IS_SIG_REQUIRED");
      bean.advertisingCode = getStringFromResultSet(rs, "ADVERTISING_CODE");
      bean.corporateId = getStringFromResultSet(rs, "CORPORATE_ID");
      bean.employeeId = getStringFromResultSet(rs, "EMPLOYEE_ID");
      bean.reason = getStringFromResultSet(rs, "REASON");
      bean.percent = getDoubleFromResultSet(rs, "PERCENT");
      bean.guiLabel = getStringFromResultSet(rs, "GUI_LABEL");
      bean.type = getStringFromResultSet(rs, "TYPE");
      bean.discountCode = getStringFromResultSet(rs, "DISCOUNT_CODE");
      bean.authCode = getStringFromResultSet(rs, "AUTH_CODE");
      list.add(bean);
    }
    return (TrLtmDscOracleBean[]) list.toArray(new TrLtmDscOracleBean[0]);
  }

  public List toList() {
    List list = new ArrayList();
    addToList(list, this.getAiTrn(), Types.VARCHAR);
    addToList(list, this.getAiLnItm(), Types.DECIMAL);
    addToList(list, this.getTyDsc(), Types.VARCHAR);
    addToList(list, this.getMoDsc(), Types.VARCHAR);
    addToList(list, this.getCnvtToMkdn(), Types.DECIMAL);
    addToList(list, this.getIsDscPercent(), Types.DECIMAL);
    addToList(list, this.getIsAdditionToMd(), Types.DECIMAL);
    addToList(list, this.getIsSigRequired(), Types.DECIMAL);
    addToList(list, this.getAdvertisingCode(), Types.VARCHAR);
    addToList(list, this.getCorporateId(), Types.VARCHAR);
    addToList(list, this.getEmployeeId(), Types.VARCHAR);
    addToList(list, this.getReason(), Types.VARCHAR);
    addToList(list, this.getPercent(), Types.DECIMAL);
    addToList(list, this.getGuiLabel(), Types.VARCHAR);
    addToList(list, this.getType(), Types.VARCHAR);
    addToList(list, this.getDiscountCode(), Types.VARCHAR);
    addToList(list, this.getAuthCode(), Types.VARCHAR);
    return list;
  }

}
