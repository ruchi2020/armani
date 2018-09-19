//
// Copyright 2002, Retek Inc. All Rights Reserved.
//
/*
History:
+------+------------+-----------+-----------+----------------------------------------------+
| Ver# | Date       | By        | Defect #  | Description                                  |
+------+------------+-----------+-----------+----------------------------------------------+
| 4    | 04-10-2006 | Khyati    | N/A       | New columns for Credit card information      |
--------------------------------------------------------------------------------------------
| 3    | 04-29-2005 | Pankaja   | N/A       | New sql for updation of the expiration dt    |
--------------------------------------------------------------------------------------------
| 2    | 04-12-2005 | Rajesh    | N/A       | Specs Presale impl                           |
+------+------------+-----------+-----------+----------------------------------------------+
*/
package com.chelseasystems.cs.dataaccess.artsoracle.databean;

import java.util.Date;
import java.util.Calendar;
import java.util.List;
import java.util.ArrayList;
import java.sql.SQLException;
import java.sql.ResultSet;
import java.sql.Types;
import com.chelseasystems.cr.currency.ArmCurrency;
import com.chelseasystems.cs.util.EncryptionUtils;

/**
 *
 * This class is an object representation of the Arts database table ARM_POS_PRS<BR>
 * Followings are the column of the table: <BR>
 *     AI_TRN(PK) -- VARCHAR2(128)<BR>
 *     ID_PRESALE -- VARCHAR2(128)<BR>
 *     EXP_DT -- DATE(7)<BR>
 *
 */
public class ArmPosPrsOracleBean extends BaseOracleBean {

  public ArmPosPrsOracleBean() {}

  public static String selectSql = "select AI_TRN, ID_PRESALE, EXP_DT, CREDIT_CARD_NUMBER, CREDIT_CARD_TYPE, CREDIT_CARD_EXPIRATION_DATE, BILLING_ZIP_CODE from ARM_POS_PRS ";
  public static String insertSql = "insert into ARM_POS_PRS (AI_TRN, ID_PRESALE, EXP_DT, CREDIT_CARD_NUMBER, CREDIT_CARD_TYPE, CREDIT_CARD_EXPIRATION_DATE, BILLING_ZIP_CODE) values (?, ?, ?, ?, ?, ?, ?)";
  public static String updateSql = "update ARM_POS_PRS set AI_TRN = ?, ID_PRESALE = ?, EXP_DT = ?, CREDIT_CARD_NUMBER = ?, CREDIT_CARD_TYPE = ?, CREDIT_CARD_EXPIRATION_DATE = ?, BILLING_ZIP_CODE = ? ";
  public static String updateExpirationDtSql = "update ARM_POS_PRS set EXP_DT = ? ";
  public static String deleteSql = "delete from ARM_POS_PRS ";

  public static String TABLE_NAME = "ARM_POS_PRS";
  public static String COL_AI_TRN = "ARM_POS_PRS.AI_TRN";
  public static String COL_ID_PRESALE = "ARM_POS_PRS.ID_PRESALE";
  public static String COL_EXP_DT = "ARM_POS_PRS.EXP_DT";
  public static String COL_CREDIT_CARD_NUMBER = "ARM_POS_PRS.CREDIT_CARD_NUMBER";
  public static String COL_CREDIT_CARD_TYPE = "ARM_POS_PRS.CREDIT_CARD_TYPE";
  public static String COL_CREDIT_CARD_EXPIRATION_DATE = "ARM_POS_PRS.CREDIT_CARD_EXPIRATION_DATE";
  public static String COL_BILLING_ZIP_CODE = "ARM_POS_PRS.BILLING_ZIP_CODE";


  public String getSelectSql() { return selectSql; }
  public String getInsertSql() { return insertSql; }
  public String getUpdateSql() { return updateSql; }
  public String getDeleteSql() { return deleteSql; }

  private String aiTrn;
  private String idPresale;
  private Date expDt;
  private String creditCardNumber;
  private String creditCardType;
  private Date creditCardExpirationDate;
  private String billingZipCode;


  public String getAiTrn() { return this.aiTrn; }
  public void setAiTrn(String aiTrn) { this.aiTrn = aiTrn; }

  public String getIdPresale() { return this.idPresale; }
  public void setIdPresale(String idPresale) { this.idPresale = idPresale; }

  public Date getExpDt() { return this.expDt; }
  public void setExpDt(Date expDt) { this.expDt = expDt; }

  public String getCreditCardNumber() { return this.creditCardNumber; }
  public void setCreditCardNumber(String creditCardNumber) { this.creditCardNumber = creditCardNumber; }

  public String getCreditCardType() { return this.creditCardType; }
  public void setCreditCardType(String creditCardType) { this.creditCardType = creditCardType; }

  public Date getCreditCardExpirationDate() { return this.creditCardExpirationDate; }
  public void setCreditCardExpirationDate(Date creditCardExpirationDate) { this.creditCardExpirationDate = creditCardExpirationDate; }

  public String getBillingZipCode() { return this.billingZipCode; }
  public void setBillingZipCode(String billingZipCode) { this.billingZipCode = billingZipCode; }

  public BaseOracleBean[] getDatabeans(ResultSet rs) throws SQLException {
    ArrayList list = new ArrayList();
    
    //TD
    EncryptionUtils encryptionUtils = new EncryptionUtils();
    while(rs.next()) {
      ArmPosPrsOracleBean bean = new ArmPosPrsOracleBean();
      bean.aiTrn = getStringFromResultSet(rs, "AI_TRN");
      bean.idPresale = getStringFromResultSet(rs, "ID_PRESALE");
      bean.expDt = getDateFromResultSet(rs, "EXP_DT");
      //TD
      String ccNum = getStringFromResultSet(rs, "CREDIT_CARD_NUMBER");
      String decryptCCNum = null;
      if(ccNum != null && ccNum.trim().length() > 0 ) {
    	  decryptCCNum = encryptionUtils.decrypt(ccNum);///encrypt
      }
      bean.creditCardNumber = decryptCCNum;
      //bean.creditCardNumber = getStringFromResultSet(rs, "CREDIT_CARD_NUMBER");
      bean.creditCardType = getStringFromResultSet(rs, "CREDIT_CARD_TYPE");
      bean.creditCardExpirationDate = getDateFromResultSet(rs, "CREDIT_CARD_EXPIRATION_DATE");
      bean.billingZipCode = getStringFromResultSet(rs, "BILLING_ZIP_CODE");
      list.add(bean);
    }
    return (ArmPosPrsOracleBean[]) list.toArray(new ArmPosPrsOracleBean[0]);
  }

  public List toList() {
    List list = new ArrayList();
    //TD
    EncryptionUtils encryptionUtils = new EncryptionUtils();
    String ccNum = this.getCreditCardNumber();
    String decryptCCNum = null;
    if(ccNum != null && ccNum.trim().length() > 0 ) {
  	  decryptCCNum = encryptionUtils.encrypt(ccNum);
    }
    addToList(list, this.getAiTrn(), Types.VARCHAR);
    addToList(list, this.getIdPresale(), Types.VARCHAR);
    addToList(list, this.getExpDt(), Types.TIMESTAMP);
    addToList(list, decryptCCNum, Types.VARCHAR);
    //addToList(list, this.getCreditCardNumber(), Types.VARCHAR);
    addToList(list, this.getCreditCardType(), Types.VARCHAR);
    addToList(list, this.getCreditCardExpirationDate(), Types.TIMESTAMP);
    addToList(list, this.getBillingZipCode(), Types.VARCHAR);
    return list;
  }

}
