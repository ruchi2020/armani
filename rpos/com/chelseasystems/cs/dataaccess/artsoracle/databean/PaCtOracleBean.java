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
 * This class is an object representation of the Arts database table PA_CT<BR>
 * Followings are the column of the table: <BR>
 *     ID_CT(PK) -- VARCHAR2(128)<BR>
 *     TY_RO_PRTY -- VARCHAR2(60)<BR>
 *     TY_CT_LFC -- VARCHAR2(40)<BR>
 *     MO_CT_INCM_ANN -- VARCHAR2(75)<BR>
 *     SC_MRTL -- VARCHAR2(20)<BR>
 *     NM_RLG_AFLN -- VARCHAR2(40)<BR>
 *     NM_HGH_EDC_LV -- VARCHAR2(40)<BR>
 *     ID_PRTY -- VARCHAR2(128)<BR>
 *     FL_RC_ML -- NUMBER(1)<BR>
 *     DE_COMMENTS -- VARCHAR2(255)<BR>
 *     TY_CT -- VARCHAR2(10)<BR>
 *     FL_LOYALTY -- VARCHAR2(1)<BR>
 *     PE_VIP_DISC -- NUMBER(4.2)<BR>
 *     CD_PRIVACY -- VARCHAR2(1)<BR>
 *     FL_MAIL -- VARCHAR2(1)<BR>
 *     FL_CALL -- VARCHAR2(1)<BR>
 *     FL_EML -- VARCHAR2(1)<BR>
 *     FL_SMS -- VARCHAR2(1)<BR>
 *     CUST_BARCODE -- VARCHAR2(20)<BR>
 *     SC_CT -- VARCHAR2(6)<BR>
 *     CUST_BALANCE -- VARCHAR2(75)<BR>
 *     ADDED BY VIVEK FOR CUSTOMER PRIVACY MANAGEMENT
 *     FL_MASTER -- VARCHAR2(1)<BR>
 *     FL_MARKETING -- VARCHAR2(1)<BR>
 *     Added by vivek sawant to stored Customer's created date.
 *     ISSUE_DATE -- DATE<BR>
 *
 */
public class PaCtOracleBean extends BaseOracleBean {

  public PaCtOracleBean() {}

  public static String selectSql = "select ID_CT, TY_RO_PRTY, TY_CT_LFC, MO_CT_INCM_ANN, SC_MRTL, NM_RLG_AFLN, NM_HGH_EDC_LV, ID_PRTY, FL_RC_ML, DE_COMMENTS, TY_CT, FL_LOYALTY, PE_VIP_DISC, CD_PRIVACY, FL_MAIL, FL_CALL, FL_EML, FL_SMS, CUST_BARCODE, SC_CT, CUST_BALANCE, FL_MASTER, FL_MARKETING, ISSUE_DATE from PA_CT "; 
  public static String insertSql = "insert into PA_CT (ID_CT, TY_RO_PRTY, TY_CT_LFC, MO_CT_INCM_ANN, SC_MRTL, NM_RLG_AFLN, NM_HGH_EDC_LV, ID_PRTY, FL_RC_ML, DE_COMMENTS, TY_CT, FL_LOYALTY, PE_VIP_DISC, CD_PRIVACY, FL_MAIL, FL_CALL, FL_EML, FL_SMS, CUST_BARCODE, SC_CT, CUST_BALANCE, FL_MASTER, FL_MARKETING, ISSUE_DATE) values (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
  public static String updateSql = "update PA_CT set ID_CT = ?, TY_RO_PRTY = ?, TY_CT_LFC = ?, MO_CT_INCM_ANN = ?, SC_MRTL = ?, NM_RLG_AFLN = ?, NM_HGH_EDC_LV = ?, ID_PRTY = ?, FL_RC_ML = ?, DE_COMMENTS = ?, TY_CT = ?, FL_LOYALTY = ?, PE_VIP_DISC = ?, CD_PRIVACY = ?, FL_MAIL = ?, FL_CALL = ?, FL_EML = ?, FL_SMS = ?, CUST_BARCODE = ?, SC_CT = ?, CUST_BALANCE = ?, FL_MASTER = ?, FL_MARKETING = ?, ISSUE_DATE = ? ";  
  public static String deleteSql = "delete from PA_CT ";

  public static String TABLE_NAME = "PA_CT";
  public static String COL_ID_CT = "PA_CT.ID_CT";
  public static String COL_TY_RO_PRTY = "PA_CT.TY_RO_PRTY";
  public static String COL_TY_CT_LFC = "PA_CT.TY_CT_LFC";
  public static String COL_MO_CT_INCM_ANN = "PA_CT.MO_CT_INCM_ANN";
  public static String COL_SC_MRTL = "PA_CT.SC_MRTL";
  public static String COL_NM_RLG_AFLN = "PA_CT.NM_RLG_AFLN";
  public static String COL_NM_HGH_EDC_LV = "PA_CT.NM_HGH_EDC_LV";
  public static String COL_ID_PRTY = "PA_CT.ID_PRTY";
  public static String COL_FL_RC_ML = "PA_CT.FL_RC_ML";
  public static String COL_DE_COMMENTS = "PA_CT.DE_COMMENTS";
  public static String COL_TY_CT = "PA_CT.TY_CT";
  public static String COL_FL_LOYALTY = "PA_CT.FL_LOYALTY";
  public static String COL_PE_VIP_DISC = "PA_CT.PE_VIP_DISC";
  public static String COL_CD_PRIVACY = "PA_CT.CD_PRIVACY";
  public static String COL_FL_MAIL = "PA_CT.FL_MAIL";
  public static String COL_FL_CALL = "PA_CT.FL_CALL";
  public static String COL_FL_EML = "PA_CT.FL_EML";
  public static String COL_FL_SMS = "PA_CT.FL_SMS";
  public static String COL_CUST_BARCODE = "PA_CT.CUST_BARCODE";
  public static String COL_SC_CT = "PA_CT.SC_CT";
  public static String COL_CUST_BALANCE = "PA_CT.CUST_BALANCE";
  //Added for Privacy Mgmt Marketing and Master
  public static String COL_FL_MASTER = "PA_CT.FL_MASTER";
  public static String COL_FL_MARKETING = "PA_CT.FL_MARKETING";
  //Added by vivek sawant to stored Customer's created date. 
  public static String COL_ISSUE_DATE  = "PA_CT.ISSUE_DATE";

  public String getSelectSql() { return selectSql; }
  public String getInsertSql() { return insertSql; }
  public String getUpdateSql() { return updateSql; }
  public String getDeleteSql() { return deleteSql; }

  private String idCt;
  private String tyRoPrty;
  private String tyCtLfc;
  private ArmCurrency moCtIncmAnn;
  private String scMrtl;
  private String nmRlgAfln;
  private String nmHghEdcLv;
  private String idPrty;
  private Boolean flRcMl;
  private String deComments;
  private String tyCt;
  private Boolean flLoyalty;
  private Double peVipDisc;
  private String cdPrivacy;
  private Boolean flMail;
  private Boolean flCall;
  private Boolean flEml;
  private Boolean flSms;
  private String custBarcode;
  private String scCt;
  private ArmCurrency custBalance;
  //Added for Privacy Mgmt Marketing and Master
  private Boolean flMaster;
  private Boolean flMarketing;
  //Added by vivek sawant to stored Customer's created date.
  private Date issueDate;
  

  public String getIdCt() { return this.idCt; }
  public void setIdCt(String idCt) { this.idCt = idCt; }

  public String getTyRoPrty() { return this.tyRoPrty; }
  public void setTyRoPrty(String tyRoPrty) { this.tyRoPrty = tyRoPrty; }

  public String getTyCtLfc() { return this.tyCtLfc; }
  public void setTyCtLfc(String tyCtLfc) { this.tyCtLfc = tyCtLfc; }

  public ArmCurrency getMoCtIncmAnn() { return this.moCtIncmAnn; }
  public void setMoCtIncmAnn(ArmCurrency moCtIncmAnn) { this.moCtIncmAnn = moCtIncmAnn; }

  public String getScMrtl() { return this.scMrtl; }
  public void setScMrtl(String scMrtl) { this.scMrtl = scMrtl; }

  public String getNmRlgAfln() { return this.nmRlgAfln; }
  public void setNmRlgAfln(String nmRlgAfln) { this.nmRlgAfln = nmRlgAfln; }

  public String getNmHghEdcLv() { return this.nmHghEdcLv; }
  public void setNmHghEdcLv(String nmHghEdcLv) { this.nmHghEdcLv = nmHghEdcLv; }

  public String getIdPrty() { return this.idPrty; }
  public void setIdPrty(String idPrty) { this.idPrty = idPrty; }

  public Boolean getFlRcMl() { return this.flRcMl; }
  public void setFlRcMl(Boolean flRcMl) { this.flRcMl = flRcMl; }
  public void setFlRcMl(boolean flRcMl) { this.flRcMl = new Boolean(flRcMl); }

  public String getDeComments() { return this.deComments; }
  public void setDeComments(String deComments) { this.deComments = deComments; }

  public String getTyCt() { return this.tyCt; }
  public void setTyCt(String tyCt) { this.tyCt = tyCt; }

  public Boolean getFlLoyalty() { return this.flLoyalty; }
  public void setFlLoyalty(Boolean flLoyalty) { this.flLoyalty = flLoyalty; }
  public void setFlLoyalty(boolean flLoyalty) { this.flLoyalty = new Boolean(flLoyalty); }

  public Double getPeVipDisc() { return this.peVipDisc; }
  public void setPeVipDisc(Double peVipDisc) { this.peVipDisc = peVipDisc; }
  public void setPeVipDisc(double peVipDisc) { this.peVipDisc = new Double(peVipDisc); }

  public String getCdPrivacy() { return this.cdPrivacy; }
  public void setCdPrivacy(String cdPrivacy) { this.cdPrivacy = cdPrivacy; }

  public Boolean getFlMail() { return this.flMail; }
  public void setFlMail(Boolean flMail) { this.flMail = flMail; }
  public void setFlMail(boolean flMail) { this.flMail = new Boolean(flMail); }

  public Boolean getFlCall() { return this.flCall; }
  public void setFlCall(Boolean flCall) { this.flCall = flCall; }
  public void setFlCall(boolean flCall) { this.flCall = new Boolean(flCall); }

  public Boolean getFlEml() { return this.flEml; }
  public void setFlEml(Boolean flEml) { this.flEml = flEml; }
  public void setFlEml(boolean flEml) { this.flEml = new Boolean(flEml); }

  public Boolean getFlSms() { return this.flSms; }
  public void setFlSms(Boolean flSms) { this.flSms = flSms; }
  public void setFlSms(boolean flSms) { this.flSms = new Boolean(flSms); }

  public String getCustBarcode() { return this.custBarcode; }
  public void setCustBarcode(String custBarcode) { this.custBarcode = custBarcode; }

  public String getScCt() { return this.scCt; }
  public void setScCt(String scCt) { this.scCt = scCt; }

  public ArmCurrency getCustBalance() { return this.custBalance; }
  public void setCustBalance(ArmCurrency custBalance) { this.custBalance = custBalance; }

  //Added for Privacy Mgmt Marketing and Master
  public Boolean getFlMaster() { return this.flMaster; }
  public void setFlMaster(Boolean flMaster) { this.flMaster = flMaster; }
  public void setFlMaster(boolean flMaster) { this.flMaster = new Boolean(flMaster); }

  //Added for Privacy Mgmt Marketing and Master
  public Boolean getFlMarketing() { return this.flMarketing; }
  public void setFlMarketing(Boolean flMarketing) { this.flMarketing = flMarketing; }
  public void setFlMarketing(boolean flMarketing) { this.flMarketing = new Boolean(flMarketing); }

  //Added by vivek sawant to stored Customer's created date.
  public Date getIssueDate() {	return this.issueDate;}
  public void setIssueDate(Date issueDate) {this.issueDate = issueDate;}
  
  public BaseOracleBean[] getDatabeans(ResultSet rs) throws SQLException {
    ArrayList list = new ArrayList();
    while(rs.next()) {
      PaCtOracleBean bean = new PaCtOracleBean();
      bean.idCt = getStringFromResultSet(rs, "ID_CT");
      bean.tyRoPrty = getStringFromResultSet(rs, "TY_RO_PRTY");
      bean.tyCtLfc = getStringFromResultSet(rs, "TY_CT_LFC");
      bean.moCtIncmAnn = getCurrencyFromResultSet(rs, "MO_CT_INCM_ANN");
      bean.scMrtl = getStringFromResultSet(rs, "SC_MRTL");
      bean.nmRlgAfln = getStringFromResultSet(rs, "NM_RLG_AFLN");
      bean.nmHghEdcLv = getStringFromResultSet(rs, "NM_HGH_EDC_LV");
      bean.idPrty = getStringFromResultSet(rs, "ID_PRTY");
      bean.flRcMl = getBooleanFromResultSet(rs, "FL_RC_ML");
      bean.deComments = getStringFromResultSet(rs, "DE_COMMENTS");
      bean.tyCt = getStringFromResultSet(rs, "TY_CT");
      bean.flLoyalty = getBooleanFromResultSet(rs, "FL_LOYALTY");
      bean.peVipDisc = getDoubleFromResultSet(rs, "PE_VIP_DISC");
      bean.cdPrivacy = getStringFromResultSet(rs, "CD_PRIVACY");
      bean.flMail = getBooleanFromResultSet(rs, "FL_MAIL");
      bean.flCall = getBooleanFromResultSet(rs, "FL_CALL");
      bean.flEml = getBooleanFromResultSet(rs, "FL_EML");
      bean.flSms = getBooleanFromResultSet(rs, "FL_SMS");
      bean.custBarcode = getStringFromResultSet(rs, "CUST_BARCODE");
      bean.scCt = getStringFromResultSet(rs, "SC_CT");
      bean.custBalance = getCurrencyFromResultSet(rs, "CUST_BALANCE");
      //Added for Privacy Mgmt Marketing and Master
      bean.flMaster = getBooleanFromResultSet(rs,"FL_MASTER");
      bean.flMarketing = getBooleanFromResultSet(rs,"FL_MARKETING");
      //Added by vivek sawant to stored Customer's created date.
      bean.issueDate = getDateFromResultSet(rs, "ISSUE_DATE");
     list.add(bean);
    }
    return (PaCtOracleBean[]) list.toArray(new PaCtOracleBean[0]);
  }

  public List toList() {
    List list = new ArrayList();
    addToList(list, this.getIdCt(), Types.VARCHAR);
    addToList(list, this.getTyRoPrty(), Types.VARCHAR);
    addToList(list, this.getTyCtLfc(), Types.VARCHAR);
    addToList(list, this.getMoCtIncmAnn(), Types.VARCHAR);
    addToList(list, this.getScMrtl(), Types.VARCHAR);
    addToList(list, this.getNmRlgAfln(), Types.VARCHAR);
    addToList(list, this.getNmHghEdcLv(), Types.VARCHAR);
    addToList(list, this.getIdPrty(), Types.VARCHAR);
    addToList(list, this.getFlRcMl(), Types.DECIMAL);
    addToList(list, this.getDeComments(), Types.VARCHAR);
    addToList(list, this.getTyCt(), Types.VARCHAR);
    addToList(list, this.getFlLoyalty(), Types.DECIMAL);
    addToList(list, this.getPeVipDisc(), Types.DECIMAL);
    addToList(list, this.getCdPrivacy(), Types.VARCHAR);
    addToList(list, this.getFlMail(), Types.DECIMAL);
    addToList(list, this.getFlCall(), Types.DECIMAL);
    addToList(list, this.getFlEml(), Types.DECIMAL);
    addToList(list, this.getFlSms(), Types.DECIMAL);
    addToList(list, this.getCustBarcode(), Types.VARCHAR);
    addToList(list, this.getScCt(), Types.VARCHAR);
    addToList(list, this.getCustBalance(), Types.VARCHAR);
    //Added for Privacy Mgmt Marketing and Master
    addToList(list, this.getFlMaster(), Types.DECIMAL);
    addToList(list, this.getFlMarketing(), Types.DECIMAL); 
    //Added by vivek sawant to stored Customer's created date.
    addToList(list, this.getIssueDate(), Types.TIMESTAMP);
    return list;
  }

}
