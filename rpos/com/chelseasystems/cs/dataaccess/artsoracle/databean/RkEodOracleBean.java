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
 * This class is an object representation of the Arts database table RK_EOD<BR>
 * Followings are the column of the table: <BR>
 *     MGR_SAYS_CASH_LIST -- VARCHAR2(200)<BR>
 *     MGR_SAYS_CHEK_LIST -- VARCHAR2(200)<BR>
 *     MGR_SAYS_BCRD -- VARCHAR2(75)<BR>
 *     MGR_SAYS_AMEX -- VARCHAR2(75)<BR>
 *     MGR_SAYS_DISC -- VARCHAR2(75)<BR>
 *     MGR_SAYS_TR_CHKS -- VARCHAR2(75)<BR>
 *     MGR_SAYS_MNY_ODR -- VARCHAR2(75)<BR>
 *     REG_DRWR_FUND -- VARCHAR2(75)<BR>
 *     AI_TRN(PK) -- VARCHAR2(128)<BR>
 *     MGR_SAYS_MALLCERTIFICATE -- VARCHAR2(75)<BR>
 *
 */
public class RkEodOracleBean extends BaseOracleBean {

  public RkEodOracleBean() {}

  public static String selectSql = "select MGR_SAYS_CASH_LIST, MGR_SAYS_CHEK_LIST, MGR_SAYS_BCRD, MGR_SAYS_AMEX, MGR_SAYS_DISC, MGR_SAYS_TR_CHKS, MGR_SAYS_MNY_ODR, REG_DRWR_FUND, AI_TRN, MGR_SAYS_MALLCERTIFICATE from RK_EOD ";
  public static String insertSql = "insert into RK_EOD (MGR_SAYS_CASH_LIST, MGR_SAYS_CHEK_LIST, MGR_SAYS_BCRD, MGR_SAYS_AMEX, MGR_SAYS_DISC, MGR_SAYS_TR_CHKS, MGR_SAYS_MNY_ODR, REG_DRWR_FUND, AI_TRN, MGR_SAYS_MALLCERTIFICATE) values (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
  public static String updateSql = "update RK_EOD set MGR_SAYS_CASH_LIST = ?, MGR_SAYS_CHEK_LIST = ?, MGR_SAYS_BCRD = ?, MGR_SAYS_AMEX = ?, MGR_SAYS_DISC = ?, MGR_SAYS_TR_CHKS = ?, MGR_SAYS_MNY_ODR = ?, REG_DRWR_FUND = ?, AI_TRN = ?, MGR_SAYS_MALLCERTIFICATE = ? ";
  public static String deleteSql = "delete from RK_EOD ";

  public static String TABLE_NAME = "RK_EOD";
  public static String COL_MGR_SAYS_CASH_LIST = "RK_EOD.MGR_SAYS_CASH_LIST";
  public static String COL_MGR_SAYS_CHEK_LIST = "RK_EOD.MGR_SAYS_CHEK_LIST";
  public static String COL_MGR_SAYS_BCRD = "RK_EOD.MGR_SAYS_BCRD";
  public static String COL_MGR_SAYS_AMEX = "RK_EOD.MGR_SAYS_AMEX";
  public static String COL_MGR_SAYS_DISC = "RK_EOD.MGR_SAYS_DISC";
  public static String COL_MGR_SAYS_TR_CHKS = "RK_EOD.MGR_SAYS_TR_CHKS";
  public static String COL_MGR_SAYS_MNY_ODR = "RK_EOD.MGR_SAYS_MNY_ODR";
  public static String COL_REG_DRWR_FUND = "RK_EOD.REG_DRWR_FUND";
  public static String COL_AI_TRN = "RK_EOD.AI_TRN";
  public static String COL_MGR_SAYS_MALLCERTIFICATE = "RK_EOD.MGR_SAYS_MALLCERTIFICATE";

  public String getSelectSql() { return selectSql; }
  public String getInsertSql() { return insertSql; }
  public String getUpdateSql() { return updateSql; }
  public String getDeleteSql() { return deleteSql; }

  private String mgrSaysCashList;
  private String mgrSaysChekList;
  private ArmCurrency mgrSaysBcrd;
  private ArmCurrency mgrSaysAmex;
  private ArmCurrency mgrSaysDisc;
  private ArmCurrency mgrSaysTrChks;
  private ArmCurrency mgrSaysMnyOdr;
  private ArmCurrency regDrwrFund;
  private String aiTrn;
  private ArmCurrency mgrSaysMallcertificate;

  public String getMgrSaysCashList() { return this.mgrSaysCashList; }
  public void setMgrSaysCashList(String mgrSaysCashList) { this.mgrSaysCashList = mgrSaysCashList; }

  public String getMgrSaysChekList() { return this.mgrSaysChekList; }
  public void setMgrSaysChekList(String mgrSaysChekList) { this.mgrSaysChekList = mgrSaysChekList; }

  public ArmCurrency getMgrSaysBcrd() { return this.mgrSaysBcrd; }
  public void setMgrSaysBcrd(ArmCurrency mgrSaysBcrd) { this.mgrSaysBcrd = mgrSaysBcrd; }

  public ArmCurrency getMgrSaysAmex() { return this.mgrSaysAmex; }
  public void setMgrSaysAmex(ArmCurrency mgrSaysAmex) { this.mgrSaysAmex = mgrSaysAmex; }

  public ArmCurrency getMgrSaysDisc() { return this.mgrSaysDisc; }
  public void setMgrSaysDisc(ArmCurrency mgrSaysDisc) { this.mgrSaysDisc = mgrSaysDisc; }

  public ArmCurrency getMgrSaysTrChks() { return this.mgrSaysTrChks; }
  public void setMgrSaysTrChks(ArmCurrency mgrSaysTrChks) { this.mgrSaysTrChks = mgrSaysTrChks; }

  public ArmCurrency getMgrSaysMnyOdr() { return this.mgrSaysMnyOdr; }
  public void setMgrSaysMnyOdr(ArmCurrency mgrSaysMnyOdr) { this.mgrSaysMnyOdr = mgrSaysMnyOdr; }

  public ArmCurrency getRegDrwrFund() { return this.regDrwrFund; }
  public void setRegDrwrFund(ArmCurrency regDrwrFund) { this.regDrwrFund = regDrwrFund; }

  public String getAiTrn() { return this.aiTrn; }
  public void setAiTrn(String aiTrn) { this.aiTrn = aiTrn; }

  public ArmCurrency getMgrSaysMallcertificate() { return this.mgrSaysMallcertificate; }
  public void setMgrSaysMallcertificate(ArmCurrency mgrSaysMallcertificate) { this.mgrSaysMallcertificate = mgrSaysMallcertificate; }

  public BaseOracleBean[] getDatabeans(ResultSet rs) throws SQLException {
    ArrayList list = new ArrayList();
    while(rs.next()) {
      RkEodOracleBean bean = new RkEodOracleBean();
      bean.mgrSaysCashList = getStringFromResultSet(rs, "MGR_SAYS_CASH_LIST");
      bean.mgrSaysChekList = getStringFromResultSet(rs, "MGR_SAYS_CHEK_LIST");
      bean.mgrSaysBcrd = getCurrencyFromResultSet(rs, "MGR_SAYS_BCRD");
      bean.mgrSaysAmex = getCurrencyFromResultSet(rs, "MGR_SAYS_AMEX");
      bean.mgrSaysDisc = getCurrencyFromResultSet(rs, "MGR_SAYS_DISC");
      bean.mgrSaysTrChks = getCurrencyFromResultSet(rs, "MGR_SAYS_TR_CHKS");
      bean.mgrSaysMnyOdr = getCurrencyFromResultSet(rs, "MGR_SAYS_MNY_ODR");
      bean.regDrwrFund = getCurrencyFromResultSet(rs, "REG_DRWR_FUND");
      bean.aiTrn = getStringFromResultSet(rs, "AI_TRN");
      bean.mgrSaysMallcertificate = getCurrencyFromResultSet(rs, "MGR_SAYS_MALLCERTIFICATE");
      list.add(bean);
    }
    return (RkEodOracleBean[]) list.toArray(new RkEodOracleBean[0]);
  }

  public List toList() {
    List list = new ArrayList();
    addToList(list, this.getMgrSaysCashList(), Types.VARCHAR);
    addToList(list, this.getMgrSaysChekList(), Types.VARCHAR);
    addToList(list, this.getMgrSaysBcrd(), Types.VARCHAR);
    addToList(list, this.getMgrSaysAmex(), Types.VARCHAR);
    addToList(list, this.getMgrSaysDisc(), Types.VARCHAR);
    addToList(list, this.getMgrSaysTrChks(), Types.VARCHAR);
    addToList(list, this.getMgrSaysMnyOdr(), Types.VARCHAR);
    addToList(list, this.getRegDrwrFund(), Types.VARCHAR);
    addToList(list, this.getAiTrn(), Types.VARCHAR);
    addToList(list, this.getMgrSaysMallcertificate(), Types.VARCHAR);
    return list;
  }

}
