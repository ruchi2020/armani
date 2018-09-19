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
 * This class is an object representation of the Arts database table V_ARM_TXN_HDR<BR>
 * Followings are the column of the table: <BR>
 *     CUST_ID -- VARCHAR2(50)<BR>
 *     TY_TRN -- VARCHAR2(60)<BR>
 *     PAY_TRN_TYPE -- VARCHAR2(60)<BR>
 *     ID_STR_RT -- VARCHAR2(128)<BR>
 *     ID_OPR -- VARCHAR2(128)<BR>
 *     TS_TRN_PRC -- DATE(7)<BR>
 *     TS_TRN_SBM -- DATE(7)<BR>
 *     TY_GUI_TRN -- VARCHAR2(128)<BR>
 *     PAY_TYPES -- VARCHAR2(1024)<BR>
 *     TOTAL_AMT -- VARCHAR2(75)<BR>
 *     AI_TRN -- VARCHAR2(128)<BR>
 *     CONSULTANT_ID -- VARCHAR2(30)<BR>
 *     REDUCTION_AMOUNT -- VARCHAR2(75)<BR>
 *     NET_AMOUNT -- VARCHAR2(75)<BR>
 *     DISCOUNT_TYPES -- VARCHAR2(200)<BR>
 *     ITEMS_IDS -- VARCHAR2(4000)<BR>
 *     REGISTER_ID -- VARCHAR2(128)<BR>
 *     FN_PRS -- VARCHAR2(40)<BR>
 *     LN_PRS -- VARCHAR2(40)<BR>
 *     EXP_DT -- DATE(7)<BR>
 *     REF_ID -- VARCHAR2(128)<BR>
 *     VOID_ID -- VARCHAR2(128)<BR>
 *     CUST_FIRST_NAME -- VARCHAR2(40)<BR>
 *     CUST_LAST_NAME -- VARCHAR2(40)<BR>
 *     STORE_DESC -- VARCHAR2(50)<BR>
 *
 */
public class VArmTxnHdrOracleBean extends BaseOracleBean {

  public VArmTxnHdrOracleBean() {}

  public static String selectSql = "select CUST_ID, TY_TRN, PAY_TRN_TYPE, ID_STR_RT, ID_OPR, TS_TRN_PRC, TS_TRN_SBM, TY_GUI_TRN, PAY_TYPES, TOTAL_AMT, AI_TRN, CONSULTANT_ID, REDUCTION_AMOUNT, NET_AMOUNT, DISCOUNT_TYPES, ITEMS_IDS, REGISTER_ID, FN_PRS, LN_PRS, EXP_DT, REF_ID, VOID_ID, CUST_FIRST_NAME, CUST_LAST_NAME, STORE_DESC from V_ARM_TXN_HDR ";
  public static String insertSql = "insert into V_ARM_TXN_HDR (CUST_ID, TY_TRN, PAY_TRN_TYPE, ID_STR_RT, ID_OPR, TS_TRN_PRC, TS_TRN_SBM, TY_GUI_TRN, PAY_TYPES, TOTAL_AMT, AI_TRN, CONSULTANT_ID, REDUCTION_AMOUNT, NET_AMOUNT, DISCOUNT_TYPES, ITEMS_IDS, REGISTER_ID, FN_PRS, LN_PRS, EXP_DT, REF_ID, VOID_ID, CUST_FIRST_NAME, CUST_LAST_NAME, STORE_DESC) values (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
  public static String updateSql = "update V_ARM_TXN_HDR set CUST_ID = ?, TY_TRN = ?, PAY_TRN_TYPE = ?, ID_STR_RT = ?, ID_OPR = ?, TS_TRN_PRC = ?, TS_TRN_SBM = ?, TY_GUI_TRN = ?, PAY_TYPES = ?, TOTAL_AMT = ?, AI_TRN = ?, CONSULTANT_ID = ?, REDUCTION_AMOUNT = ?, NET_AMOUNT = ?, DISCOUNT_TYPES = ?, ITEMS_IDS = ?, REGISTER_ID = ?, FN_PRS = ?, LN_PRS = ?, EXP_DT = ?, REF_ID = ?, VOID_ID = ?, CUST_FIRST_NAME = ?, CUST_LAST_NAME = ?, STORE_DESC = ? ";
  public static String deleteSql = "delete from V_ARM_TXN_HDR ";

  public static String TABLE_NAME = "V_ARM_TXN_HDR";
  public static String COL_CUST_ID = "V_ARM_TXN_HDR.CUST_ID";
  public static String COL_TY_TRN = "V_ARM_TXN_HDR.TY_TRN";
  public static String COL_PAY_TRN_TYPE = "V_ARM_TXN_HDR.PAY_TRN_TYPE";
  public static String COL_ID_STR_RT = "V_ARM_TXN_HDR.ID_STR_RT";
  public static String COL_ID_OPR = "V_ARM_TXN_HDR.ID_OPR";
  public static String COL_TS_TRN_PRC = "V_ARM_TXN_HDR.TS_TRN_PRC";
  public static String COL_TS_TRN_SBM = "V_ARM_TXN_HDR.TS_TRN_SBM";
  public static String COL_TY_GUI_TRN = "V_ARM_TXN_HDR.TY_GUI_TRN";
  public static String COL_PAY_TYPES = "V_ARM_TXN_HDR.PAY_TYPES";
  public static String COL_TOTAL_AMT = "V_ARM_TXN_HDR.TOTAL_AMT";
  public static String COL_AI_TRN = "V_ARM_TXN_HDR.AI_TRN";
  public static String COL_CONSULTANT_ID = "V_ARM_TXN_HDR.CONSULTANT_ID";
  public static String COL_REDUCTION_AMOUNT = "V_ARM_TXN_HDR.REDUCTION_AMOUNT";
  public static String COL_NET_AMOUNT = "V_ARM_TXN_HDR.NET_AMOUNT";
  public static String COL_DISCOUNT_TYPES = "V_ARM_TXN_HDR.DISCOUNT_TYPES";
  public static String COL_ITEMS_IDS = "V_ARM_TXN_HDR.ITEMS_IDS";
  public static String COL_REGISTER_ID = "V_ARM_TXN_HDR.REGISTER_ID";
  public static String COL_FN_PRS = "V_ARM_TXN_HDR.FN_PRS";
  public static String COL_LN_PRS = "V_ARM_TXN_HDR.LN_PRS";
  public static String COL_EXP_DT = "V_ARM_TXN_HDR.EXP_DT";
  public static String COL_REF_ID = "V_ARM_TXN_HDR.REF_ID";
  public static String COL_VOID_ID = "V_ARM_TXN_HDR.VOID_ID";
  public static String COL_CUST_FIRST_NAME = "V_ARM_TXN_HDR.CUST_FIRST_NAME";
  public static String COL_CUST_LAST_NAME = "V_ARM_TXN_HDR.CUST_LAST_NAME";
  public static String COL_STORE_DESC = "V_ARM_TXN_HDR.STORE_DESC";

  public String getSelectSql() { return selectSql; }
  public String getInsertSql() { return insertSql; }
  public String getUpdateSql() { return updateSql; }
  public String getDeleteSql() { return deleteSql; }

  private String custId;
  private String tyTrn;
  private String payTrnType;
  private String idStrRt;
  private String idOpr;
  private Date tsTrnPrc;
  private Date tsTrnSbm;
  private String tyGuiTrn;
  private String payTypes;
  private ArmCurrency totalAmt;
  private String aiTrn;
  private String consultantId;
  private ArmCurrency reductionAmount;
  private ArmCurrency netAmount;
  private String discountTypes;
  private String itemsIds;
  private String registerId;
  private String fnPrs;
  private String lnPrs;
  private Date expDt;
  private String refId;
  private String voidId;
  private String custFirstName;
  private String custLastName;
  private String storeDesc;

  public String getCustId() { return this.custId; }
  public void setCustId(String custId) { this.custId = custId; }

  public String getTyTrn() { return this.tyTrn; }
  public void setTyTrn(String tyTrn) { this.tyTrn = tyTrn; }

  public String getPayTrnType() { return this.payTrnType; }
  public void setPayTrnType(String payTrnType) { this.payTrnType = payTrnType; }

  public String getIdStrRt() { return this.idStrRt; }
  public void setIdStrRt(String idStrRt) { this.idStrRt = idStrRt; }

  public String getIdOpr() { return this.idOpr; }
  public void setIdOpr(String idOpr) { this.idOpr = idOpr; }

  public Date getTsTrnPrc() { return this.tsTrnPrc; }
  public void setTsTrnPrc(Date tsTrnPrc) { this.tsTrnPrc = tsTrnPrc; }

  public Date getTsTrnSbm() { return this.tsTrnSbm; }
  public void setTsTrnSbm(Date tsTrnSbm) { this.tsTrnSbm = tsTrnSbm; }

  public String getTyGuiTrn() { return this.tyGuiTrn; }
  public void setTyGuiTrn(String tyGuiTrn) { this.tyGuiTrn = tyGuiTrn; }

  public String getPayTypes() { return this.payTypes; }
  public void setPayTypes(String payTypes) { this.payTypes = payTypes; }

  public ArmCurrency getTotalAmt() { return this.totalAmt; }
  public void setTotalAmt(ArmCurrency totalAmt) { this.totalAmt = totalAmt; }

  public String getAiTrn() { return this.aiTrn; }
  public void setAiTrn(String aiTrn) { this.aiTrn = aiTrn; }

  public String getConsultantId() { return this.consultantId; }
  public void setConsultantId(String consultantId) { this.consultantId = consultantId; }

  public ArmCurrency getReductionAmount() { return this.reductionAmount; }
  public void setReductionAmount(ArmCurrency reductionAmount) { this.reductionAmount = reductionAmount; }

  public ArmCurrency getNetAmount() { return this.netAmount; }
  public void setNetAmount(ArmCurrency netAmount) { this.netAmount = netAmount; }

  public String getDiscountTypes() { return this.discountTypes; }
  public void setDiscountTypes(String discountTypes) { this.discountTypes = discountTypes; }

  public String getItemsIds() { return this.itemsIds; }
  public void setItemsIds(String itemsIds) { this.itemsIds = itemsIds; }

  public String getRegisterId() { return this.registerId; }
  public void setRegisterId(String registerId) { this.registerId = registerId; }

  public String getFnPrs() { return this.fnPrs; }
  public void setFnPrs(String fnPrs) { this.fnPrs = fnPrs; }

  public String getLnPrs() { return this.lnPrs; }
  public void setLnPrs(String lnPrs) { this.lnPrs = lnPrs; }

  public Date getExpDt() { return this.expDt; }
  public void setExpDt(Date expDt) { this.expDt = expDt; }

  public String getRefId() { return this.refId; }
  public void setRefId(String refId) { this.refId = refId; }

  public String getVoidId() { return this.voidId; }
  public void setVoidId(String voidId) { this.voidId = voidId; }

  public String getCustFirstName() { return this.custFirstName; }
  public void setCustFirstName(String custFirstName) { this.custFirstName = custFirstName; }

  public String getCustLastName() { return this.custLastName; }
  public void setCustLastName(String custLastName) { this.custLastName = custLastName; }

  public String getStoreDesc() { return this.storeDesc; }
  public void setStoreDesc(String storeDesc) { this.storeDesc = storeDesc; }

  public BaseOracleBean[] getDatabeans(ResultSet rs) throws SQLException {
    ArrayList list = new ArrayList();
    while(rs.next()) {
      VArmTxnHdrOracleBean bean = new VArmTxnHdrOracleBean();
      bean.custId = getStringFromResultSet(rs, "CUST_ID");
      bean.tyTrn = getStringFromResultSet(rs, "TY_TRN");
      bean.payTrnType = getStringFromResultSet(rs, "PAY_TRN_TYPE");
      bean.idStrRt = getStringFromResultSet(rs, "ID_STR_RT");
      bean.idOpr = getStringFromResultSet(rs, "ID_OPR");
      bean.tsTrnPrc = getDateFromResultSet(rs, "TS_TRN_PRC");
      bean.tsTrnSbm = getDateFromResultSet(rs, "TS_TRN_SBM");
      bean.tyGuiTrn = getStringFromResultSet(rs, "TY_GUI_TRN");
      bean.payTypes = getStringFromResultSet(rs, "PAY_TYPES");
      bean.totalAmt = getCurrencyFromResultSet(rs, "TOTAL_AMT");
      bean.aiTrn = getStringFromResultSet(rs, "AI_TRN");
      bean.consultantId = getStringFromResultSet(rs, "CONSULTANT_ID");
      bean.reductionAmount = getCurrencyFromResultSet(rs, "REDUCTION_AMOUNT");
      bean.netAmount = getCurrencyFromResultSet(rs, "NET_AMOUNT");
      bean.discountTypes = getStringFromResultSet(rs, "DISCOUNT_TYPES");
      bean.itemsIds = getStringFromResultSet(rs, "ITEMS_IDS");
      bean.registerId = getStringFromResultSet(rs, "REGISTER_ID");
      bean.fnPrs = getStringFromResultSet(rs, "FN_PRS");
      bean.lnPrs = getStringFromResultSet(rs, "LN_PRS");
      bean.expDt = getDateFromResultSet(rs, "EXP_DT");
      bean.refId = getStringFromResultSet(rs, "REF_ID");
      bean.voidId = getStringFromResultSet(rs, "VOID_ID");
      bean.custFirstName = getStringFromResultSet(rs, "CUST_FIRST_NAME");
      bean.custLastName = getStringFromResultSet(rs, "CUST_LAST_NAME");
      bean.storeDesc = getStringFromResultSet(rs, "STORE_DESC");
      list.add(bean);
    }
    return (VArmTxnHdrOracleBean[]) list.toArray(new VArmTxnHdrOracleBean[0]);
  }

  public List toList() {
    List list = new ArrayList();
    addToList(list, this.getCustId(), Types.VARCHAR);
    addToList(list, this.getTyTrn(), Types.VARCHAR);
    addToList(list, this.getPayTrnType(), Types.VARCHAR);
    addToList(list, this.getIdStrRt(), Types.VARCHAR);
    addToList(list, this.getIdOpr(), Types.VARCHAR);
    addToList(list, this.getTsTrnPrc(), Types.TIMESTAMP);
    addToList(list, this.getTsTrnSbm(), Types.TIMESTAMP);
    addToList(list, this.getTyGuiTrn(), Types.VARCHAR);
    addToList(list, this.getPayTypes(), Types.VARCHAR);
    addToList(list, this.getTotalAmt(), Types.VARCHAR);
    addToList(list, this.getAiTrn(), Types.VARCHAR);
    addToList(list, this.getConsultantId(), Types.VARCHAR);
    addToList(list, this.getReductionAmount(), Types.VARCHAR);
    addToList(list, this.getNetAmount(), Types.VARCHAR);
    addToList(list, this.getDiscountTypes(), Types.VARCHAR);
    addToList(list, this.getItemsIds(), Types.VARCHAR);
    addToList(list, this.getRegisterId(), Types.VARCHAR);
    addToList(list, this.getFnPrs(), Types.VARCHAR);
    addToList(list, this.getLnPrs(), Types.VARCHAR);
    addToList(list, this.getExpDt(), Types.TIMESTAMP);
    addToList(list, this.getRefId(), Types.VARCHAR);
    addToList(list, this.getVoidId(), Types.VARCHAR);
    addToList(list, this.getCustFirstName(), Types.VARCHAR);
    addToList(list, this.getCustLastName(), Types.VARCHAR);
    addToList(list, this.getStoreDesc(), Types.VARCHAR);
    return list;
  }

}
