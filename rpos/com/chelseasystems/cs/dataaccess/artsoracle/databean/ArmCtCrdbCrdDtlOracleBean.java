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
 * This class is an object representation of the Arts database table ARM_CT_CRDB_CRD_DTL<BR>
 * Followings are the column of the table: <BR>
 *     ID_CT -- VARCHAR2(20)<BR>
 *     ID_STR_RT -- VARCHAR2(20)<BR>
 *     ID_ACNT_DB_CR_CRD -- VARCHAR2(40)<BR>
 *     EXPIRATION_DATE -- DATE(7)<BR>
 *     ZIP_CODE -- VARCHAR2(10)<BR>
 *     TY_CRD -- VARCHAR2(20)<BR>
 *     ID_EN_ACNT_DB_CR_CRD -- VARCHAR2(500)<BR> 
 *     STATUS -- CHAR(1)       
 *     KY_ID_EN_ACNT_DB_CR_CRD -- NUMBER(9)     
 *     CARD_TOKEN -- VARCHAR2(20)  
 *     MSK_CR_CRD -- VARCHAR2(20)  
 *     CUST_SIGNATURE -- BLOB 
 */
public class ArmCtCrdbCrdDtlOracleBean extends BaseOracleBean {

  public ArmCtCrdbCrdDtlOracleBean() {}

  public static String selectSql = "select ID_CT, ID_STR_RT, ID_ACNT_DB_CR_CRD, EXPIRATION_DATE, ZIP_CODE, TY_CRD,CARD_TOKEN,MSK_CR_CRD, CUST_SIGNATURE from ARM_CT_CRDB_CRD_DTL ";
  public static String insertSql = "insert into ARM_CT_CRDB_CRD_DTL (ID_CT, ID_STR_RT, ID_ACNT_DB_CR_CRD, EXPIRATION_DATE, ZIP_CODE, TY_CRD,CARD_TOKEN,MSK_CR_CRD,CUST_SIGNATURE) values (?, ?, ?, ?, ?, ?, ?, ?, ?)";
  public static String updateSql = "update ARM_CT_CRDB_CRD_DTL set ID_CT = ?, ID_STR_RT = ?, ID_ACNT_DB_CR_CRD = ?, EXPIRATION_DATE = ?, ZIP_CODE = ?, TY_CRD = ?,CARD_TOKEN=?,MSK_CR_CRD=?, CUST_SIGNATURE=?";
  public static String deleteSql = "delete from ARM_CT_CRDB_CRD_DTL ";

  public static String TABLE_NAME = "ARM_CT_CRDB_CRD_DTL";
  public static String COL_ID_CT = "ARM_CT_CRDB_CRD_DTL.ID_CT";
  public static String COL_ID_STR_RT = "ARM_CT_CRDB_CRD_DTL.ID_STR_RT";
  //sonali:AJB variables added for token number and masked credit card num
  //public static String COL_ID_ACNT_DB_CR_CRD = "ARM_CT_CRDB_CRD_DTL.ID_ACNT_DB_CR_CRD";
  public static String COL_ID_MSK_CR_CRD = "ARM_CT_CRDB_CRD_DTL.MSK_CR_CRD";
  public static String COL_ID_CARD_TOKEN= "ARM_CT_CRDB_CRD_DTL.CARD_TOKEN";
 
  public static String COL_EXPIRATION_DATE = "ARM_CT_CRDB_CRD_DTL.EXPIRATION_DATE";
  public static String COL_ZIP_CODE = "ARM_CT_CRDB_CRD_DTL.ZIP_CODE";
  public static String COL_TY_CRD = "ARM_CT_CRDB_CRD_DTL.TY_CRD";

  public String getSelectSql() { return selectSql; }
  public String getInsertSql() { return insertSql; }
  public String getUpdateSql() { return updateSql; }
  public String getDeleteSql() { return deleteSql; }

  private String idCt;
  private String idStrRt;
  private String idAcntDbCrCrd;
  private Date expirationDate;
  private String zipCode;
  private String tyCrd;
  private String crdToken;
  private String maskedCardNum;
  //Vivek Mishra : Added to capture customer signature.
  private Object custSignature;

  public String getIdCt() { return this.idCt; }
  public void setIdCt(String idCt) { this.idCt = idCt; }

  public String getIdStrRt() { return this.idStrRt; }
  public void setIdStrRt(String idStrRt) { this.idStrRt = idStrRt; }

  public String getIdAcntDbCrCrd() { return this.idAcntDbCrCrd; }
  public void setIdAcntDbCrCrd(String idAcntDbCrCrd) { this.idAcntDbCrCrd = idAcntDbCrCrd; }

  public Date getExpirationDate() { return this.expirationDate; }
  public void setExpirationDate(Date expirationDate) { this.expirationDate = expirationDate; }

  public String getZipCode() { return this.zipCode; }
  public void setZipCode(String zipCode) { this.zipCode = zipCode; }

  public String getTyCrd() { return this.tyCrd; }
  public void setTyCrd(String tyCrd) { this.tyCrd = tyCrd; }
  //modified by Sonali for AJB to add CARD_TOKEN
  public void setCrdToken(String crdToken) {
  	this.crdToken = crdToken;
  }
  public String getCrdToken() {
  	return crdToken;
  }
  public String getMaskedCardNum() {
		return maskedCardNum;
	}
	public void setMaskedCardNum(String maskedCardNum) {
		this.maskedCardNum = maskedCardNum;
	}

	//Vivek Mishra : Added to capture customer signature.
    public Object getCustSignature() {
		return custSignature;
	}
	public void setCustSignature(Object custSignature) {
		this.custSignature = custSignature;
	}
	//Ends
	
public BaseOracleBean[] getDatabeans(ResultSet rs) throws SQLException {
    ArrayList list = new ArrayList();
    while(rs.next()) {
      ArmCtCrdbCrdDtlOracleBean bean = new ArmCtCrdbCrdDtlOracleBean();
      bean.idCt = getStringFromResultSet(rs, "ID_CT");
      bean.idStrRt = getStringFromResultSet(rs, "ID_STR_RT");
      bean.idAcntDbCrCrd = getStringFromResultSet(rs, "ID_ACNT_DB_CR_CRD");
      bean.expirationDate = getDateFromResultSet(rs, "EXPIRATION_DATE");
      bean.zipCode = getStringFromResultSet(rs, "ZIP_CODE");
      bean.tyCrd = getStringFromResultSet(rs, "TY_CRD");
      bean.crdToken=getStringFromResultSet(rs, "CARD_TOKEN");
      bean.maskedCardNum=getStringFromResultSet(rs, "MSK_CR_CRD");
      bean.custSignature=getObjectFromResultSet(rs, "CUST_SIGNATURE");
      list.add(bean);
    }
    return (ArmCtCrdbCrdDtlOracleBean[]) list.toArray(new ArmCtCrdbCrdDtlOracleBean[0]);
  }

  //Vivek Mishra : Added method for getting Object from result set
  private Object getObjectFromResultSet(ResultSet rs, String column)
		throws SQLException {
	Object object = rs.getBlob(column);
	if (rs.wasNull())
		return null;
	return object;
   }
   //Ends
  
  public List toList() {
    List list = new ArrayList();
    addToList(list, this.getIdCt(), Types.VARCHAR);
    addToList(list, this.getIdStrRt(), Types.VARCHAR);
    addToList(list, this.getIdAcntDbCrCrd(), Types.VARCHAR);
    addToList(list, this.getExpirationDate(), Types.TIMESTAMP);
    addToList(list, this.getZipCode(), Types.VARCHAR);
    addToList(list, this.getTyCrd(), Types.VARCHAR);
    addToList(list, this.getCrdToken(), Types.VARCHAR);
    addToList(list, this.getMaskedCardNum(), Types.VARCHAR);
    addToList(list, this.getCustSignature(), Types.BLOB);
    return list;
  }

}
