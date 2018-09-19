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
 * This class is an object representation of the Arts database table RK_POS_LN_ITM_DTL<BR>
 * Followings are the column of the table: <BR>
 *     SEQUENCE_NUMBER(PK) -- NUMBER(3)<BR>
 *     NET_AMT -- VARCHAR2(75)<BR>
 *     TAX_AMT -- VARCHAR2(75)<BR>
 *     MANUAL_TAX_AMT -- VARCHAR2(75)<BR>
 *     REGIONAL_TAX_AMT -- VARCHAR2(75)<BR>
 *     DEAL_MKDN_AMT -- VARCHAR2(75)<BR>
 *     USER_ENTERED_FL -- NUMBER(1)<BR>
 *     MAN_REG_TAX_AMT -- VARCHAR2(75)<BR>
 *     GIFT_CERT_ID -- VARCHAR2(100)<BR>
 *     SALE_RETURNED -- NUMBER(1)<BR>
 *     AI_TRN(PK) -- VARCHAR2(128)<BR>
 *     AI_LN_ITM(PK) -- NUMBER(22)<BR>
 *     DEAL_ID -- VARCHAR2(50)<BR>
 *     VAT_AMT -- VARCHAR2(75)<BR>
 *     FL_PROCESSED -- CHAR(1)<BR>
 *     POS_LN_ITM_TY_ID -- NUMBER(3)<BR>
 *     LOYALTY_PT -- NUMBER(15.2)<BR>
 *
 */
public class RkPosLnItmDtlOracleBean extends BaseOracleBean {

  public RkPosLnItmDtlOracleBean() {}

  public static String selectSql = "select SEQUENCE_NUMBER, NET_AMT, TAX_AMT, MANUAL_TAX_AMT, REGIONAL_TAX_AMT, DEAL_MKDN_AMT, USER_ENTERED_FL, MAN_REG_TAX_AMT, GIFT_CERT_ID, SALE_RETURNED, AI_TRN, AI_LN_ITM, DEAL_ID, VAT_AMT, FL_PROCESSED, POS_LN_ITM_TY_ID, LOYALTY_PT, STATE_TAX_AMT, CITY_TAX_AMT, GST_TAX_AMT, PST_TAX_AMT, QST_TAX_AMT, GSTHST_TAX_AMT from RK_POS_LN_ITM_DTL ";
  public static String insertSql = "insert into RK_POS_LN_ITM_DTL (SEQUENCE_NUMBER, NET_AMT, TAX_AMT, MANUAL_TAX_AMT, REGIONAL_TAX_AMT, DEAL_MKDN_AMT, USER_ENTERED_FL, MAN_REG_TAX_AMT, GIFT_CERT_ID, SALE_RETURNED, AI_TRN, AI_LN_ITM, DEAL_ID, VAT_AMT, FL_PROCESSED, POS_LN_ITM_TY_ID, LOYALTY_PT, STATE_TAX_AMT, CITY_TAX_AMT, GST_TAX_AMT, PST_TAX_AMT, QST_TAX_AMT, GSTHST_TAX_AMT) values (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
  public static String updateSql = "update RK_POS_LN_ITM_DTL set SEQUENCE_NUMBER = ?, NET_AMT = ?, TAX_AMT = ?, MANUAL_TAX_AMT = ?, REGIONAL_TAX_AMT = ?, DEAL_MKDN_AMT = ?, USER_ENTERED_FL = ?, MAN_REG_TAX_AMT = ?, GIFT_CERT_ID = ?, SALE_RETURNED = ?, AI_TRN = ?, AI_LN_ITM = ?, DEAL_ID = ?, VAT_AMT = ?, FL_PROCESSED = ?, POS_LN_ITM_TY_ID = ?, LOYALTY_PT = ?, STATE_TAX_AMT = ?, CITY_TAX_AMT = ?, GST_TAX_AMT = ?, PST_TAX_AMT = ?, QST_TAX_AMT = ?, GSTHST_TAX_AMT = ?";
  public static String deleteSql = "delete from RK_POS_LN_ITM_DTL ";

  public static String TABLE_NAME = "RK_POS_LN_ITM_DTL";
  public static String COL_SEQUENCE_NUMBER = "RK_POS_LN_ITM_DTL.SEQUENCE_NUMBER";
  public static String COL_NET_AMT = "RK_POS_LN_ITM_DTL.NET_AMT";
  public static String COL_TAX_AMT = "RK_POS_LN_ITM_DTL.TAX_AMT";
  public static String COL_MANUAL_TAX_AMT = "RK_POS_LN_ITM_DTL.MANUAL_TAX_AMT";
  public static String COL_REGIONAL_TAX_AMT = "RK_POS_LN_ITM_DTL.REGIONAL_TAX_AMT";
  public static String COL_DEAL_MKDN_AMT = "RK_POS_LN_ITM_DTL.DEAL_MKDN_AMT";
  public static String COL_USER_ENTERED_FL = "RK_POS_LN_ITM_DTL.USER_ENTERED_FL";
  public static String COL_MAN_REG_TAX_AMT = "RK_POS_LN_ITM_DTL.MAN_REG_TAX_AMT";
  public static String COL_GIFT_CERT_ID = "RK_POS_LN_ITM_DTL.GIFT_CERT_ID";
  public static String COL_SALE_RETURNED = "RK_POS_LN_ITM_DTL.SALE_RETURNED";
  public static String COL_AI_TRN = "RK_POS_LN_ITM_DTL.AI_TRN";
  public static String COL_AI_LN_ITM = "RK_POS_LN_ITM_DTL.AI_LN_ITM";
  public static String COL_DEAL_ID = "RK_POS_LN_ITM_DTL.DEAL_ID";
  public static String COL_VAT_AMT = "RK_POS_LN_ITM_DTL.VAT_AMT";
  public static String COL_FL_PROCESSED = "RK_POS_LN_ITM_DTL.FL_PROCESSED";
  public static String COL_POS_LN_ITM_TY_ID = "RK_POS_LN_ITM_DTL.POS_LN_ITM_TY_ID";
  public static String COL_LOYALTY_PT = "RK_POS_LN_ITM_DTL.LOYALTY_PT";
  public static String COL_STATE_TAX_AMT = "RK_POS_LN_ITM_DTL.STATE_TAX_AMT";
  public static String COL_CITY_TAX_AMT = "RK_POS_LN_ITM_DTL.CITY_TAX_AMT";
  public static String COL_GST_TAX_AMT = "RK_POS_LN_ITM_DTL.GST_TAX_AMT";
  public static String COL_PST_TAX_AMT = "RK_POS_LN_ITM_DTL.PST_TAX_AMT";
  public static String COL_QST_TAX_AMT = "RK_POS_LN_ITM_DTL.QST_TAX_AMT";
  public static String COL_GSTHST_TAX_AMT = "RK_POS_LN_ITM_DTL.GSTHST_TAX_AMT";
  
  public String getSelectSql() { return selectSql; }
  public String getInsertSql() { return insertSql; }
  public String getUpdateSql() { return updateSql; }
  public String getDeleteSql() { return deleteSql; }

  private Long sequenceNumber;
  private ArmCurrency netAmt;
  private ArmCurrency taxAmt;
  private ArmCurrency manualTaxAmt;
  private ArmCurrency regionalTaxAmt;
  private ArmCurrency dealMkdnAmt;
  private Boolean userEnteredFl;
  private ArmCurrency manRegTaxAmt;
  private String giftCertId;
  private Boolean saleReturned;
  private String aiTrn;
  private Long aiLnItm;
  private String dealId;
  private ArmCurrency vatAmt;
  private Boolean flProcessed;
  private Long posLnItmTyId;
  private Double loyaltyPt;
  private ArmCurrency stateTaxAmt;
  private ArmCurrency cityTaxAmt;
  private ArmCurrency gstTaxAmt;
  private ArmCurrency pstTaxAmt;
  private ArmCurrency qstTaxAmt;
  private ArmCurrency gsthstTaxAmt;
  
  public Long getSequenceNumber() { return this.sequenceNumber; }
  public void setSequenceNumber(Long sequenceNumber) { this.sequenceNumber = sequenceNumber; }
  public void setSequenceNumber(long sequenceNumber) { this.sequenceNumber = new Long(sequenceNumber); }
  public void setSequenceNumber(int sequenceNumber) { this.sequenceNumber = new Long((long)sequenceNumber); }

  public ArmCurrency getNetAmt() { return this.netAmt; }
  public void setNetAmt(ArmCurrency netAmt) { this.netAmt = netAmt; }

  public ArmCurrency getTaxAmt() { return this.taxAmt; }
  public void setTaxAmt(ArmCurrency taxAmt) { this.taxAmt = taxAmt; }

  public ArmCurrency getManualTaxAmt() { return this.manualTaxAmt; }
  public void setManualTaxAmt(ArmCurrency manualTaxAmt) { this.manualTaxAmt = manualTaxAmt; }

  public ArmCurrency getRegionalTaxAmt() { return this.regionalTaxAmt; }
  public void setRegionalTaxAmt(ArmCurrency regionalTaxAmt) { this.regionalTaxAmt = regionalTaxAmt; }

  public ArmCurrency getDealMkdnAmt() { return this.dealMkdnAmt; }
  public void setDealMkdnAmt(ArmCurrency dealMkdnAmt) { this.dealMkdnAmt = dealMkdnAmt; }

  public Boolean getUserEnteredFl() { return this.userEnteredFl; }
  public void setUserEnteredFl(Boolean userEnteredFl) { this.userEnteredFl = userEnteredFl; }
  public void setUserEnteredFl(boolean userEnteredFl) { this.userEnteredFl = new Boolean(userEnteredFl); }

  public ArmCurrency getManRegTaxAmt() { return this.manRegTaxAmt; }
  public void setManRegTaxAmt(ArmCurrency manRegTaxAmt) { this.manRegTaxAmt = manRegTaxAmt; }

  public String getGiftCertId() { return this.giftCertId; }
  public void setGiftCertId(String giftCertId) { this.giftCertId = giftCertId; }

  public Boolean getSaleReturned() { return this.saleReturned; }
  public void setSaleReturned(Boolean saleReturned) { this.saleReturned = saleReturned; }
  public void setSaleReturned(boolean saleReturned) { this.saleReturned = new Boolean(saleReturned); }

  public String getAiTrn() { return this.aiTrn; }
  public void setAiTrn(String aiTrn) { this.aiTrn = aiTrn; }

  public Long getAiLnItm() { return this.aiLnItm; }
  public void setAiLnItm(Long aiLnItm) { this.aiLnItm = aiLnItm; }
  public void setAiLnItm(long aiLnItm) { this.aiLnItm = new Long(aiLnItm); }
  public void setAiLnItm(int aiLnItm) { this.aiLnItm = new Long((long)aiLnItm); }

  public String getDealId() { return this.dealId; }
  public void setDealId(String dealId) { this.dealId = dealId; }

  public ArmCurrency getVatAmt() { return this.vatAmt; }
  public void setVatAmt(ArmCurrency vatAmt) { this.vatAmt = vatAmt; }

  public Boolean getFlProcessed() { return this.flProcessed; }
  public void setFlProcessed(Boolean flProcessed) { this.flProcessed = flProcessed; }
  public void setFlProcessed(boolean flProcessed) { this.flProcessed = new Boolean(flProcessed); }

  public Long getPosLnItmTyId() { return this.posLnItmTyId; }
  public void setPosLnItmTyId(Long posLnItmTyId) { this.posLnItmTyId = posLnItmTyId; }
  public void setPosLnItmTyId(long posLnItmTyId) { this.posLnItmTyId = new Long(posLnItmTyId); }
  public void setPosLnItmTyId(int posLnItmTyId) { this.posLnItmTyId = new Long((long)posLnItmTyId); }

  public Double getLoyaltyPt() { return this.loyaltyPt; }
  public void setLoyaltyPt(Double loyaltyPt) { this.loyaltyPt = loyaltyPt; }
  public void setLoyaltyPt(double loyaltyPt) { this.loyaltyPt = new Double(loyaltyPt); }
  
  

  public ArmCurrency getStateTaxAmt() {
	return stateTaxAmt;
}
public void setStateTaxAmt(ArmCurrency stateTaxAmt) {
	this.stateTaxAmt = stateTaxAmt;
}
public ArmCurrency getCityTaxAmt() {
	return cityTaxAmt;
}
public void setCityTaxAmt(ArmCurrency cityTaxAmt) {
	this.cityTaxAmt = cityTaxAmt;
  }
//Test
  public ArmCurrency getGstTaxAmt() {
	return gstTaxAmt;
  }
  public void setGstTaxAmt(ArmCurrency gstTaxAmt) {
	this.gstTaxAmt = gstTaxAmt;
  }
  public ArmCurrency getPstTaxAmt() {
	return pstTaxAmt;
  }
  public void setPstTaxAmt(ArmCurrency pstTaxAmt) {
	this.pstTaxAmt = pstTaxAmt;
  }
  public ArmCurrency getQstTaxAmt() {
	return qstTaxAmt;
  }
  public void setQstTaxAmt(ArmCurrency qstTaxAmt) {
	this.qstTaxAmt = qstTaxAmt;
  }
  public ArmCurrency getGsthstTaxAmt() {
	return gsthstTaxAmt;
  }
  public void setGsthstTaxAmt(ArmCurrency gsthstTaxAmt) {
	this.gsthstTaxAmt = gsthstTaxAmt;
  }  
//Ends  
public BaseOracleBean[] getDatabeans(ResultSet rs) throws SQLException {
    ArrayList list = new ArrayList();
    while(rs.next()) {
      RkPosLnItmDtlOracleBean bean = new RkPosLnItmDtlOracleBean();
      bean.sequenceNumber = getLongFromResultSet(rs, "SEQUENCE_NUMBER");
      bean.netAmt = getCurrencyFromResultSet(rs, "NET_AMT");
      bean.taxAmt = getCurrencyFromResultSet(rs, "TAX_AMT");
      bean.manualTaxAmt = getCurrencyFromResultSet(rs, "MANUAL_TAX_AMT");
      bean.regionalTaxAmt = getCurrencyFromResultSet(rs, "REGIONAL_TAX_AMT");
      bean.dealMkdnAmt = getCurrencyFromResultSet(rs, "DEAL_MKDN_AMT");
      bean.userEnteredFl = getBooleanFromResultSet(rs, "USER_ENTERED_FL");
      bean.manRegTaxAmt = getCurrencyFromResultSet(rs, "MAN_REG_TAX_AMT");
      bean.giftCertId = getStringFromResultSet(rs, "GIFT_CERT_ID");
      bean.saleReturned = getBooleanFromResultSet(rs, "SALE_RETURNED");
      bean.aiTrn = getStringFromResultSet(rs, "AI_TRN");
      bean.aiLnItm = getLongFromResultSet(rs, "AI_LN_ITM");
      bean.dealId = getStringFromResultSet(rs, "DEAL_ID");
      bean.vatAmt = getCurrencyFromResultSet(rs, "VAT_AMT");
      bean.flProcessed = getBooleanFromResultSet(rs, "FL_PROCESSED");
      bean.posLnItmTyId = getLongFromResultSet(rs, "POS_LN_ITM_TY_ID");
      bean.loyaltyPt = getDoubleFromResultSet(rs, "LOYALTY_PT");
      bean.stateTaxAmt = getCurrencyFromResultSet(rs, "STATE_TAX_AMT");
      bean.cityTaxAmt = getCurrencyFromResultSet(rs, "CITY_TAX_AMT");
      bean.gsthstTaxAmt = getCurrencyFromResultSet(rs, "GSTHST_TAX_AMT");
      bean.gstTaxAmt = getCurrencyFromResultSet(rs, "GST_TAX_AMT");
      bean.pstTaxAmt = getCurrencyFromResultSet(rs, "PST_TAX_AMT");
      bean.qstTaxAmt = getCurrencyFromResultSet(rs, "QST_TAX_AMT");
      list.add(bean);
    }
    return (RkPosLnItmDtlOracleBean[]) list.toArray(new RkPosLnItmDtlOracleBean[0]);
  }

  public List toList() {
    List list = new ArrayList();
    addToList(list, this.getSequenceNumber(), Types.DECIMAL);
    addToList(list, this.getNetAmt(), Types.VARCHAR);
    addToList(list, this.getTaxAmt(), Types.VARCHAR);
    addToList(list, this.getManualTaxAmt(), Types.VARCHAR);
    addToList(list, this.getRegionalTaxAmt(), Types.VARCHAR);
    addToList(list, this.getDealMkdnAmt(), Types.VARCHAR);
    addToList(list, this.getUserEnteredFl(), Types.DECIMAL);
    addToList(list, this.getManRegTaxAmt(), Types.VARCHAR);
    addToList(list, this.getGiftCertId(), Types.VARCHAR);
    addToList(list, this.getSaleReturned(), Types.DECIMAL);
    addToList(list, this.getAiTrn(), Types.VARCHAR);
    addToList(list, this.getAiLnItm(), Types.DECIMAL);
    addToList(list, this.getDealId(), Types.VARCHAR);
    addToList(list, this.getVatAmt(), Types.VARCHAR);
    addToList(list, this.getFlProcessed(), Types.DECIMAL);
    addToList(list, this.getPosLnItmTyId(), Types.DECIMAL);
    addToList(list, this.getLoyaltyPt(), Types.DECIMAL);
    addToList(list, this.getStateTaxAmt(), Types.VARCHAR);
    addToList(list, this.getCityTaxAmt(), Types.VARCHAR);
    addToList(list, this.getGstTaxAmt(), Types.VARCHAR);
    addToList(list, this.getPstTaxAmt(), Types.VARCHAR);
    addToList(list, this.getQstTaxAmt(), Types.VARCHAR);
    addToList(list, this.getGsthstTaxAmt(), Types.VARCHAR);
    return list;
  }

}
