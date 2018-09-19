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
 * This class is an object representation of the Arts database table ARM_CT_DTL<BR>
 * Followings are the column of the table: <BR>
 *     ID_CT -- VARCHAR2(20)<BR>
 *     VAT_NUM -- VARCHAR2(25)<BR>
 *     FISCAL_CODE -- VARCHAR2(25)<BR>
 *     ID_TYPE -- VARCHAR2(25)<BR>
 *     DOC_NUM -- VARCHAR2(25)<BR>
 *     PLACE_OF_ISSUE -- VARCHAR2(25)<BR>
 *     ISSUE_DT -- DATE(7)<BR>
 *     TY_PAY -- VARCHAR2(25)<BR>
 *     ID_CMY -- VARCHAR2(11)<BR>
 *     CD_INTER_CMY -- VARCHAR2(11)<BR>
 *     ACCT_NUM -- VARCHAR2(11)<BR>
 *     AGE -- VARCHAR2(2)<BR>
 *     REFERRED_BY -- VARCHAR2(11)<BR>
 *     PROFESSION -- VARCHAR2(25)<BR>
 *     EDUCATION -- VARCHAR2(25)<BR>
 *     NOTES1 -- VARCHAR2(100)<BR>
 *     NOTES2 -- VARCHAR2(100)<BR>
 *     NM_PTNR_FAM -- VARCHAR2(30)<BR>
 *     NM_PTNR -- VARCHAR2(30)<BR>
 *     BIRTH_PLACE -- VARCHAR2(30)<BR>
 *     TY_SPL_EVT -- VARCHAR2(20)<BR>
 *     DC_SPL_EVT -- DATE(7)<BR>
 *     NM_CHLD -- VARCHAR2(50)<BR>
 *     CHLD_NUM -- VARCHAR2(2)<BR>
 *     CREATE_OFFLINE -- VARCHAR2(1)<BR>
 *     SUPPLIER_PAYMENT -- VARCHAR2(20)<BR>
 *     BANK -- VARCHAR2(30)<BR>
 *     CRDT_CRD_NUM_1 -- VARCHAR2(30)<BR>
 *     CRDT_CRD_TYP_1 -- VARCHAR2(30)<BR>
 *     CRDT_CRD_NUM_2 -- VARCHAR2(30)<BR>
 *     CRDT_CRD_TYP_2 -- VARCHAR2(30)<BR>
 *
 */
public class ArmCtDtlOracleBean extends BaseOracleBean {

  public ArmCtDtlOracleBean() {}

  public static String selectSql = "select ID_CT, VAT_NUM, FISCAL_CODE, ID_TYPE, DOC_NUM, PLACE_OF_ISSUE, ISSUE_DT, TY_PAY, ID_CMY, CD_INTER_CMY, ACCT_NUM, AGE, REFERRED_BY, PROFESSION, EDUCATION, NOTES1, NOTES2, NM_PTNR_FAM, NM_PTNR, BIRTH_PLACE, TY_SPL_EVT, DC_SPL_EVT, NM_CHLD, CHLD_NUM, CREATE_OFFLINE, SUPPLIER_PAYMENT, BANK, CRDT_CRD_NUM_1, CRDT_CRD_TYP_1, CRDT_CRD_NUM_2, CRDT_CRD_TYP_2 from ARM_CT_DTL ";
  public static String insertSql = "insert into ARM_CT_DTL (ID_CT, VAT_NUM, FISCAL_CODE, ID_TYPE, DOC_NUM, PLACE_OF_ISSUE, ISSUE_DT, TY_PAY, ID_CMY, CD_INTER_CMY, ACCT_NUM, AGE, REFERRED_BY, PROFESSION, EDUCATION, NOTES1, NOTES2, NM_PTNR_FAM, NM_PTNR, BIRTH_PLACE, TY_SPL_EVT, DC_SPL_EVT, NM_CHLD, CHLD_NUM, CREATE_OFFLINE, SUPPLIER_PAYMENT, BANK, CRDT_CRD_NUM_1, CRDT_CRD_TYP_1, CRDT_CRD_NUM_2, CRDT_CRD_TYP_2) values (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
  public static String updateSql = "update ARM_CT_DTL set ID_CT = ?, VAT_NUM = ?, FISCAL_CODE = ?, ID_TYPE = ?, DOC_NUM = ?, PLACE_OF_ISSUE = ?, ISSUE_DT = ?, TY_PAY = ?, ID_CMY = ?, CD_INTER_CMY = ?, ACCT_NUM = ?, AGE = ?, REFERRED_BY = ?, PROFESSION = ?, EDUCATION = ?, NOTES1 = ?, NOTES2 = ?, NM_PTNR_FAM = ?, NM_PTNR = ?, BIRTH_PLACE = ?, TY_SPL_EVT = ?, DC_SPL_EVT = ?, NM_CHLD = ?, CHLD_NUM = ?, CREATE_OFFLINE = ?, SUPPLIER_PAYMENT = ?, BANK = ?, CRDT_CRD_NUM_1 = ?, CRDT_CRD_TYP_1 = ?, CRDT_CRD_NUM_2 = ?, CRDT_CRD_TYP_2 = ? ";
  public static String deleteSql = "delete from ARM_CT_DTL ";

  public static String TABLE_NAME = "ARM_CT_DTL";
  public static String COL_ID_CT = "ARM_CT_DTL.ID_CT";
  public static String COL_VAT_NUM = "ARM_CT_DTL.VAT_NUM";
  public static String COL_FISCAL_CODE = "ARM_CT_DTL.FISCAL_CODE";
  public static String COL_ID_TYPE = "ARM_CT_DTL.ID_TYPE";
  public static String COL_DOC_NUM = "ARM_CT_DTL.DOC_NUM";
  public static String COL_PLACE_OF_ISSUE = "ARM_CT_DTL.PLACE_OF_ISSUE";
  public static String COL_ISSUE_DT = "ARM_CT_DTL.ISSUE_DT";
  public static String COL_TY_PAY = "ARM_CT_DTL.TY_PAY";
  public static String COL_ID_CMY = "ARM_CT_DTL.ID_CMY";
  public static String COL_CD_INTER_CMY = "ARM_CT_DTL.CD_INTER_CMY";
  public static String COL_ACCT_NUM = "ARM_CT_DTL.ACCT_NUM";
  public static String COL_AGE = "ARM_CT_DTL.AGE";
  public static String COL_REFERRED_BY = "ARM_CT_DTL.REFERRED_BY";
  public static String COL_PROFESSION = "ARM_CT_DTL.PROFESSION";
  public static String COL_EDUCATION = "ARM_CT_DTL.EDUCATION";
  public static String COL_NOTES1 = "ARM_CT_DTL.NOTES1";
  public static String COL_NOTES2 = "ARM_CT_DTL.NOTES2";
  public static String COL_NM_PTNR_FAM = "ARM_CT_DTL.NM_PTNR_FAM";
  public static String COL_NM_PTNR = "ARM_CT_DTL.NM_PTNR";
  public static String COL_BIRTH_PLACE = "ARM_CT_DTL.BIRTH_PLACE";
  public static String COL_TY_SPL_EVT = "ARM_CT_DTL.TY_SPL_EVT";
  public static String COL_DC_SPL_EVT = "ARM_CT_DTL.DC_SPL_EVT";
  public static String COL_NM_CHLD = "ARM_CT_DTL.NM_CHLD";
  public static String COL_CHLD_NUM = "ARM_CT_DTL.CHLD_NUM";
  public static String COL_CREATE_OFFLINE = "ARM_CT_DTL.CREATE_OFFLINE";
  public static String COL_SUPPLIER_PAYMENT = "ARM_CT_DTL.SUPPLIER_PAYMENT";
  public static String COL_BANK = "ARM_CT_DTL.BANK";
  public static String COL_CRDT_CRD_NUM_1 = "ARM_CT_DTL.CRDT_CRD_NUM_1";
  public static String COL_CRDT_CRD_TYP_1 = "ARM_CT_DTL.CRDT_CRD_TYP_1";
  public static String COL_CRDT_CRD_NUM_2 = "ARM_CT_DTL.CRDT_CRD_NUM_2";
  public static String COL_CRDT_CRD_TYP_2 = "ARM_CT_DTL.CRDT_CRD_TYP_2";

  public String getSelectSql() { return selectSql; }
  public String getInsertSql() { return insertSql; }
  public String getUpdateSql() { return updateSql; }
  public String getDeleteSql() { return deleteSql; }

  private String idCt;
  private String vatNum;
  private String fiscalCode;
  private String idType;
  private String docNum;
  private String placeOfIssue;
  private Date issueDt;
  private String tyPay;
  private String idCmy;
  private String cdInterCmy;
  private String acctNum;
  private String age;
  private String referredBy;
  private String profession;
  private String education;
  private String notes1;
  private String notes2;
  private String nmPtnrFam;
  private String nmPtnr;
  private String birthPlace;
  private String tySplEvt;
  private Date dcSplEvt;
  private String nmChld;
  private String chldNum;
  private String createOffline;
  private String supplierPayment;
  private String bank;
  private String crdtCrdNum1;
  private String crdtCrdTyp1;
  private String crdtCrdNum2;
  private String crdtCrdTyp2;

  public String getIdCt() { return this.idCt; }
  public void setIdCt(String idCt) { this.idCt = idCt; }

  public String getVatNum() { return this.vatNum; }
  public void setVatNum(String vatNum) { this.vatNum = vatNum; }

  public String getFiscalCode() { return this.fiscalCode; }
  public void setFiscalCode(String fiscalCode) { this.fiscalCode = fiscalCode; }

  public String getIdType() { return this.idType; }
  public void setIdType(String idType) { this.idType = idType; }

  public String getDocNum() { return this.docNum; }
  public void setDocNum(String docNum) { this.docNum = docNum; }

  public String getPlaceOfIssue() { return this.placeOfIssue; }
  public void setPlaceOfIssue(String placeOfIssue) { this.placeOfIssue = placeOfIssue; }

  public Date getIssueDt() { return this.issueDt; }
  public void setIssueDt(Date issueDt) { this.issueDt = issueDt; }

  public String getTyPay() { return this.tyPay; }
  public void setTyPay(String tyPay) { this.tyPay = tyPay; }

  public String getIdCmy() { return this.idCmy; }
  public void setIdCmy(String idCmy) { this.idCmy = idCmy; }

  public String getCdInterCmy() { return this.cdInterCmy; }
  public void setCdInterCmy(String cdInterCmy) { this.cdInterCmy = cdInterCmy; }

  public String getAcctNum() { return this.acctNum; }
  public void setAcctNum(String acctNum) { this.acctNum = acctNum; }

  public String getAge() { return this.age; }
  public void setAge(String age) { this.age = age; }

  public String getReferredBy() { return this.referredBy; }
  public void setReferredBy(String referredBy) { this.referredBy = referredBy; }

  public String getProfession() { return this.profession; }
  public void setProfession(String profession) { this.profession = profession; }

  public String getEducation() { return this.education; }
  public void setEducation(String education) { this.education = education; }

  public String getNotes1() { return this.notes1; }
  public void setNotes1(String notes1) { this.notes1 = notes1; }

  public String getNotes2() { return this.notes2; }
  public void setNotes2(String notes2) { this.notes2 = notes2; }

  public String getNmPtnrFam() { return this.nmPtnrFam; }
  public void setNmPtnrFam(String nmPtnrFam) { this.nmPtnrFam = nmPtnrFam; }

  public String getNmPtnr() { return this.nmPtnr; }
  public void setNmPtnr(String nmPtnr) { this.nmPtnr = nmPtnr; }

  public String getBirthPlace() { return this.birthPlace; }
  public void setBirthPlace(String birthPlace) { this.birthPlace = birthPlace; }

  public String getTySplEvt() { return this.tySplEvt; }
  public void setTySplEvt(String tySplEvt) { this.tySplEvt = tySplEvt; }

  public Date getDcSplEvt() { return this.dcSplEvt; }
  public void setDcSplEvt(Date dcSplEvt) { this.dcSplEvt = dcSplEvt; }

  public String getNmChld() { return this.nmChld; }
  public void setNmChld(String nmChld) { this.nmChld = nmChld; }

  public String getChldNum() { return this.chldNum; }
  public void setChldNum(String chldNum) { this.chldNum = chldNum; }

  public String getCreateOffline() { return this.createOffline; }
  public void setCreateOffline(String createOffline) { this.createOffline = createOffline; }

  public String getSupplierPayment() { return this.supplierPayment; }
  public void setSupplierPayment(String supplierPayment) { this.supplierPayment = supplierPayment; }

  public String getBank() { return this.bank; }
  public void setBank(String bank) { this.bank = bank; }

  public String getCrdtCrdNum1() { return this.crdtCrdNum1; }
  public void setCrdtCrdNum1(String crdtCrdNum1) { this.crdtCrdNum1 = crdtCrdNum1; }

  public String getCrdtCrdTyp1() { return this.crdtCrdTyp1; }
  public void setCrdtCrdTyp1(String crdtCrdTyp1) { this.crdtCrdTyp1 = crdtCrdTyp1; }

  public String getCrdtCrdNum2() { return this.crdtCrdNum2; }
  public void setCrdtCrdNum2(String crdtCrdNum2) { this.crdtCrdNum2 = crdtCrdNum2; }

  public String getCrdtCrdTyp2() { return this.crdtCrdTyp2; }
  public void setCrdtCrdTyp2(String crdtCrdTyp2) { this.crdtCrdTyp2 = crdtCrdTyp2; }

  public BaseOracleBean[] getDatabeans(ResultSet rs) throws SQLException {
    ArrayList list = new ArrayList();
    while(rs.next()) {
      ArmCtDtlOracleBean bean = new ArmCtDtlOracleBean();
      bean.idCt = getStringFromResultSet(rs, "ID_CT");
      bean.vatNum = getStringFromResultSet(rs, "VAT_NUM");
      bean.fiscalCode = getStringFromResultSet(rs, "FISCAL_CODE");
      bean.idType = getStringFromResultSet(rs, "ID_TYPE");
      bean.docNum = getStringFromResultSet(rs, "DOC_NUM");
      bean.placeOfIssue = getStringFromResultSet(rs, "PLACE_OF_ISSUE");
      bean.issueDt = getDateFromResultSet(rs, "ISSUE_DT");
      bean.tyPay = getStringFromResultSet(rs, "TY_PAY");
      bean.idCmy = getStringFromResultSet(rs, "ID_CMY");
      bean.cdInterCmy = getStringFromResultSet(rs, "CD_INTER_CMY");
      bean.acctNum = getStringFromResultSet(rs, "ACCT_NUM");
      bean.age = getStringFromResultSet(rs, "AGE");
      bean.referredBy = getStringFromResultSet(rs, "REFERRED_BY");
      bean.profession = getStringFromResultSet(rs, "PROFESSION");
      bean.education = getStringFromResultSet(rs, "EDUCATION");
      bean.notes1 = getStringFromResultSet(rs, "NOTES1");
      bean.notes2 = getStringFromResultSet(rs, "NOTES2");
      bean.nmPtnrFam = getStringFromResultSet(rs, "NM_PTNR_FAM");
      bean.nmPtnr = getStringFromResultSet(rs, "NM_PTNR");
      bean.birthPlace = getStringFromResultSet(rs, "BIRTH_PLACE");
      bean.tySplEvt = getStringFromResultSet(rs, "TY_SPL_EVT");
      bean.dcSplEvt = getDateFromResultSet(rs, "DC_SPL_EVT");
      bean.nmChld = getStringFromResultSet(rs, "NM_CHLD");
      bean.chldNum = getStringFromResultSet(rs, "CHLD_NUM");
      bean.createOffline = getStringFromResultSet(rs, "CREATE_OFFLINE");
      bean.supplierPayment = getStringFromResultSet(rs, "SUPPLIER_PAYMENT");
      bean.bank = getStringFromResultSet(rs, "BANK");
      bean.crdtCrdNum1 = getStringFromResultSet(rs, "CRDT_CRD_NUM_1");
      bean.crdtCrdTyp1 = getStringFromResultSet(rs, "CRDT_CRD_TYP_1");
      bean.crdtCrdNum2 = getStringFromResultSet(rs, "CRDT_CRD_NUM_2");
      bean.crdtCrdTyp2 = getStringFromResultSet(rs, "CRDT_CRD_TYP_2");
      list.add(bean);
    }
    return (ArmCtDtlOracleBean[]) list.toArray(new ArmCtDtlOracleBean[0]);
  }

  public List toList() {
    List list = new ArrayList();
    addToList(list, this.getIdCt(), Types.VARCHAR);
    addToList(list, this.getVatNum(), Types.VARCHAR);
    addToList(list, this.getFiscalCode(), Types.VARCHAR);
    addToList(list, this.getIdType(), Types.VARCHAR);
    addToList(list, this.getDocNum(), Types.VARCHAR);
    addToList(list, this.getPlaceOfIssue(), Types.VARCHAR);
    addToList(list, this.getIssueDt(), Types.TIMESTAMP);
    addToList(list, this.getTyPay(), Types.VARCHAR);
    addToList(list, this.getIdCmy(), Types.VARCHAR);
    addToList(list, this.getCdInterCmy(), Types.VARCHAR);
    addToList(list, this.getAcctNum(), Types.VARCHAR);
    addToList(list, this.getAge(), Types.VARCHAR);
    addToList(list, this.getReferredBy(), Types.VARCHAR);
    addToList(list, this.getProfession(), Types.VARCHAR);
    addToList(list, this.getEducation(), Types.VARCHAR);
    addToList(list, this.getNotes1(), Types.VARCHAR);
    addToList(list, this.getNotes2(), Types.VARCHAR);
    addToList(list, this.getNmPtnrFam(), Types.VARCHAR);
    addToList(list, this.getNmPtnr(), Types.VARCHAR);
    addToList(list, this.getBirthPlace(), Types.VARCHAR);
    addToList(list, this.getTySplEvt(), Types.VARCHAR);
    addToList(list, this.getDcSplEvt(), Types.TIMESTAMP);
    addToList(list, this.getNmChld(), Types.VARCHAR);
    addToList(list, this.getChldNum(), Types.VARCHAR);
    addToList(list, this.getCreateOffline(), Types.VARCHAR);
    addToList(list, this.getSupplierPayment(), Types.VARCHAR);
    addToList(list, this.getBank(), Types.VARCHAR);
    addToList(list, this.getCrdtCrdNum1(), Types.VARCHAR);
    addToList(list, this.getCrdtCrdTyp1(), Types.VARCHAR);
    addToList(list, this.getCrdtCrdNum2(), Types.VARCHAR);
    addToList(list, this.getCrdtCrdTyp2(), Types.VARCHAR);
    return list;
  }

}
