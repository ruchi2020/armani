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
 * This class is an object representation of the Arts database table PA_STR_RTL<BR>
 * Followings are the column of the table: <BR>
 *     ID_STR_RT(PK) -- VARCHAR2(128)<BR>
 *     DC_OPN_RT_STR -- DATE(7)<BR>
 *     ID_PRTY -- VARCHAR2(128)<BR>
 *     DC_CL_RT_STR -- DATE(7)<BR>
 *     TY_RO_PRTY -- VARCHAR2(60)<BR>
 *     QU_SZ_STR -- NUMBER(9.2)<BR>
 *     QU_SZ_AR_SL -- NUMBER(9.2)<BR>
 *     LU_ZN_PRC_RT_STR -- VARCHAR2(40)<BR>
 *     FL_TX_RGNL -- NUMBER(1)<BR>
 *     DE_TX_RGNL -- VARCHAR2(255)<BR>
 *     DE_TX -- VARCHAR2(255)<BR>
 *     PE_TX_RGNL -- NUMBER(9.4)<BR>
 *     PE_TX -- NUMBER(9.4)<BR>
 *     PW_ACS_STR -- VARCHAR2(20)<BR>
 *     TY_CNY -- VARCHAR2(40)<BR>
 *     ID_BRAND -- VARCHAR2(30)<BR>
 *     FISCAL_CODE -- VARCHAR2(30)<BR>
 *     DE_STR_RT -- VARCHAR2(50)<BR>
 *     DE_CNY -- VARCHAR2(50)<BR>
 *     DE_CMY -- VARCHAR2(50)<BR>
 *     ID_CMY -- VARCHAR2(128)<BR>
 *     MESSAGE -- VARCHAR2(256)<BR>
 *     
 *     ** Added new column REGISTRATION_ID VARCHAR2(50) 
 *
 */
public class PaStrRtlOracleBean extends BaseOracleBean {

  public PaStrRtlOracleBean() {}
//Mahesh Nandure: 12-1-2017: markdown markup cr by sergio for EUROPE (added STORE TYPE and TIME ZONE ) columns
  public static String selectSql = "select ID_STR_RT, DC_OPN_RT_STR, ID_PRTY, DC_CL_RT_STR, TY_RO_PRTY, QU_SZ_STR, QU_SZ_AR_SL, LU_ZN_PRC_RT_STR, FL_TX_RGNL, DE_TX_RGNL, DE_TX, PE_TX_RGNL, PE_TX, PW_ACS_STR, TY_CNY, ID_BRAND, FISCAL_CODE, DE_STR_RT, DE_CNY, DE_CMY, ID_CMY, MESSAGE, REGISTRATION_ID , STORE_TYPE, TIME_ZONE from PA_STR_RTL ";
  public static String insertSql = "insert into PA_STR_RTL (ID_STR_RT, DC_OPN_RT_STR, ID_PRTY, DC_CL_RT_STR, TY_RO_PRTY, QU_SZ_STR, QU_SZ_AR_SL, LU_ZN_PRC_RT_STR, FL_TX_RGNL, DE_TX_RGNL, DE_TX, PE_TX_RGNL, PE_TX, PW_ACS_STR, TY_CNY, ID_BRAND, FISCAL_CODE, DE_STR_RT, DE_CNY, DE_CMY, ID_CMY, MESSAGE, REGISTRATION_ID, STORE_TYPE, TIME_ZONE) values (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
  public static String updateSql = "update PA_STR_RTL set ID_STR_RT = ?, DC_OPN_RT_STR = ?, ID_PRTY = ?, DC_CL_RT_STR = ?, TY_RO_PRTY = ?, QU_SZ_STR = ?, QU_SZ_AR_SL = ?, LU_ZN_PRC_RT_STR = ?, FL_TX_RGNL = ?, DE_TX_RGNL = ?, DE_TX = ?, PE_TX_RGNL = ?, PE_TX = ?, PW_ACS_STR = ?, TY_CNY = ?, ID_BRAND = ?, FISCAL_CODE = ?, DE_STR_RT = ?, DE_CNY = ?, DE_CMY = ?, ID_CMY = ?, MESSAGE = ?, REGISTRATION_ID = ?, STORE_TYPE = ?, TIME_ZONE = ? ";
  public static String deleteSql = "delete from PA_STR_RTL ";

  public static String TABLE_NAME = "PA_STR_RTL";
  public static String COL_ID_STR_RT = "PA_STR_RTL.ID_STR_RT";
  public static String COL_DC_OPN_RT_STR = "PA_STR_RTL.DC_OPN_RT_STR";
  public static String COL_ID_PRTY = "PA_STR_RTL.ID_PRTY";
  public static String COL_DC_CL_RT_STR = "PA_STR_RTL.DC_CL_RT_STR";
  public static String COL_TY_RO_PRTY = "PA_STR_RTL.TY_RO_PRTY";
  public static String COL_QU_SZ_STR = "PA_STR_RTL.QU_SZ_STR";
  public static String COL_QU_SZ_AR_SL = "PA_STR_RTL.QU_SZ_AR_SL";
  public static String COL_LU_ZN_PRC_RT_STR = "PA_STR_RTL.LU_ZN_PRC_RT_STR";
  public static String COL_FL_TX_RGNL = "PA_STR_RTL.FL_TX_RGNL";
  public static String COL_DE_TX_RGNL = "PA_STR_RTL.DE_TX_RGNL";
  public static String COL_DE_TX = "PA_STR_RTL.DE_TX";
  public static String COL_PE_TX_RGNL = "PA_STR_RTL.PE_TX_RGNL";
  public static String COL_PE_TX = "PA_STR_RTL.PE_TX";
  public static String COL_PW_ACS_STR = "PA_STR_RTL.PW_ACS_STR";
  public static String COL_TY_CNY = "PA_STR_RTL.TY_CNY";
  public static String COL_ID_BRAND = "PA_STR_RTL.ID_BRAND";
  public static String COL_FISCAL_CODE = "PA_STR_RTL.FISCAL_CODE";
  public static String COL_SHOP_DESC = "PA_STR_RTL.SHOP_DESC";
  public static String COL_COMPANY_DESC = "PA_STR_RTL.COMPANY_DESC";
  public static String COL_DE_STR_RT = "PA_STR_RTL.DE_STR_RT";
  public static String COL_DE_CNY = "PA_STR_RTL.DE_CNY";
  public static String COL_DE_CMY = "PA_STR_RTL.DE_CMY";
  public static String COL_ID_CMY = "PA_STR_RTL.ID_CMY";
  public static String COL_MESSAGE = "PA_STR_RTL.MESSAGE";
  public static String COL_REGISTRATION_ID = "PA_STR_RTL.REGISTRATION_ID";
  public static String COL_STORE_TYPE = "PA_STR_RTL.STORE_TYPE";
  public static String COL_TIME_ZONE = "PA_STR_RTL.TIME_ZONE";

  public String getSelectSql() { return selectSql; }
  public String getInsertSql() { return insertSql; }
  public String getUpdateSql() { return updateSql; }
  public String getDeleteSql() { return deleteSql; }

  private String idStrRt;
  private Date dcOpnRtStr;
  private String idPrty;
  private Date dcClRtStr;
  private String tyRoPrty;
  private Double quSzStr;
  private Double quSzArSl;
  private String luZnPrcRtStr;
  private Boolean flTxRgnl;
  private String deTxRgnl;
  private String deTx;
  private Double peTxRgnl;
  private Double peTx;
  private String pwAcsStr;
  private String tyCny;
  private String idBrand;
  private String fiscalCode;
  private String shopDesc;
  private String companyDesc;
  private String deStrRt;
  private String deCny;
  private String deCmy;
  private String idCmy;
  private String message;
  private String registrationId;
  private String storeType;
  private String timeZone;
  
  

  public String getStoreType() {
	return storeType;
}
public void setStoreType(String storeType) {
	this.storeType = storeType;
}
public String getTimeZone() {
	return timeZone;
}
public void setTimeZone(String timeZone) {
	this.timeZone = timeZone;
}
public String getIdStrRt() { return this.idStrRt; }
  public void setIdStrRt(String idStrRt) { this.idStrRt = idStrRt; }

  public Date getDcOpnRtStr() { return this.dcOpnRtStr; }
  public void setDcOpnRtStr(Date dcOpnRtStr) { this.dcOpnRtStr = dcOpnRtStr; }

  public String getIdPrty() { return this.idPrty; }
  public void setIdPrty(String idPrty) { this.idPrty = idPrty; }

  public Date getDcClRtStr() { return this.dcClRtStr; }
  public void setDcClRtStr(Date dcClRtStr) { this.dcClRtStr = dcClRtStr; }

  public String getTyRoPrty() { return this.tyRoPrty; }
  public void setTyRoPrty(String tyRoPrty) { this.tyRoPrty = tyRoPrty; }

  public Double getQuSzStr() { return this.quSzStr; }
  public void setQuSzStr(Double quSzStr) { this.quSzStr = quSzStr; }
  public void setQuSzStr(double quSzStr) { this.quSzStr = new Double(quSzStr); }

  public Double getQuSzArSl() { return this.quSzArSl; }
  public void setQuSzArSl(Double quSzArSl) { this.quSzArSl = quSzArSl; }
  public void setQuSzArSl(double quSzArSl) { this.quSzArSl = new Double(quSzArSl); }

  public String getLuZnPrcRtStr() { return this.luZnPrcRtStr; }
  public void setLuZnPrcRtStr(String luZnPrcRtStr) { this.luZnPrcRtStr = luZnPrcRtStr; }

  public Boolean getFlTxRgnl() { return this.flTxRgnl; }
  public void setFlTxRgnl(Boolean flTxRgnl) { this.flTxRgnl = flTxRgnl; }
  public void setFlTxRgnl(boolean flTxRgnl) { this.flTxRgnl = new Boolean(flTxRgnl); }

  public String getDeTxRgnl() { return this.deTxRgnl; }
  public void setDeTxRgnl(String deTxRgnl) { this.deTxRgnl = deTxRgnl; }

  public String getDeTx() { return this.deTx; }
  public void setDeTx(String deTx) { this.deTx = deTx; }

  public Double getPeTxRgnl() { return this.peTxRgnl; }
  public void setPeTxRgnl(Double peTxRgnl) { this.peTxRgnl = peTxRgnl; }
  public void setPeTxRgnl(double peTxRgnl) { this.peTxRgnl = new Double(peTxRgnl); }

  public Double getPeTx() { return this.peTx; }
  public void setPeTx(Double peTx) { this.peTx = peTx; }
  public void setPeTx(double peTx) { this.peTx = new Double(peTx); }

  public String getPwAcsStr() { return this.pwAcsStr; }
  public void setPwAcsStr(String pwAcsStr) { this.pwAcsStr = pwAcsStr; }

  public String getTyCny() { return this.tyCny; }
  public void setTyCny(String tyCny) { this.tyCny = tyCny; }

  public String getIdBrand() { return this.idBrand; }
  public void setIdBrand(String idBrand) { this.idBrand = idBrand; }

  public String getFiscalCode() { return this.fiscalCode; }
  public void setFiscalCode(String fiscalCode) { this.fiscalCode = fiscalCode; }

  public String getShopDesc() { return this.shopDesc; }
  public void setShopDesc(String shopDesc) { this.shopDesc = shopDesc; }

  public String getCompanyDesc() { return this.companyDesc; }
  public void setCompanyDesc(String companyDesc) { this.companyDesc = companyDesc; }

  public String getDeStrRt() { return this.deStrRt; }
  public void setDeStrRt(String deStrRt) { this.deStrRt = deStrRt; }

  public String getDeCny() { return this.deCny; }
  public void setDeCny(String deCny) { this.deCny = deCny; }

  public String getDeCmy() { return this.deCmy; }
  public void setDeCmy(String deCmy) { this.deCmy = deCmy; }

  public String getIdCmy() { return this.idCmy; }
  public void setIdCmy(String idCmy) { this.idCmy = idCmy; }

  public String getMessage() { return this.message; }
  public void setMessage(String message) { this.message = message; }
  
  public String getRegistrationId() { return registrationId; }
  public void setRegistrationId(String registrationId) { this.registrationId = registrationId; }

  public BaseOracleBean[] getDatabeans(ResultSet rs) throws SQLException {
    ArrayList list = new ArrayList();
    while(rs.next()) {
      PaStrRtlOracleBean bean = new PaStrRtlOracleBean();
      bean.idStrRt = getStringFromResultSet(rs, "ID_STR_RT");
      bean.dcOpnRtStr = getDateFromResultSet(rs, "DC_OPN_RT_STR");
      bean.idPrty = getStringFromResultSet(rs, "ID_PRTY");
      bean.dcClRtStr = getDateFromResultSet(rs, "DC_CL_RT_STR");
      bean.tyRoPrty = getStringFromResultSet(rs, "TY_RO_PRTY");
      bean.quSzStr = getDoubleFromResultSet(rs, "QU_SZ_STR");
      bean.quSzArSl = getDoubleFromResultSet(rs, "QU_SZ_AR_SL");
      bean.luZnPrcRtStr = getStringFromResultSet(rs, "LU_ZN_PRC_RT_STR");
      bean.flTxRgnl = getBooleanFromResultSet(rs, "FL_TX_RGNL");
      bean.deTxRgnl = getStringFromResultSet(rs, "DE_TX_RGNL");
      bean.deTx = getStringFromResultSet(rs, "DE_TX");
      bean.peTxRgnl = getDoubleFromResultSet(rs, "PE_TX_RGNL");
      bean.peTx = getDoubleFromResultSet(rs, "PE_TX");
      bean.pwAcsStr = getStringFromResultSet(rs, "PW_ACS_STR");
      bean.tyCny = getStringFromResultSet(rs, "TY_CNY");
      bean.idBrand = getStringFromResultSet(rs, "ID_BRAND");
      bean.fiscalCode = getStringFromResultSet(rs, "FISCAL_CODE");
      bean.deStrRt = getStringFromResultSet(rs, "DE_STR_RT");
      bean.deCny = getStringFromResultSet(rs, "DE_CNY");
      bean.deCmy = getStringFromResultSet(rs, "DE_CMY");
      bean.idCmy = getStringFromResultSet(rs, "ID_CMY");
      bean.message = getStringFromResultSet(rs, "MESSAGE");
      bean.registrationId = getStringFromResultSet(rs, "REGISTRATION_ID");
      bean.storeType = getStringFromResultSet(rs, "STORE_TYPE");
      bean.timeZone = getStringFromResultSet(rs, "TIME_ZONE");
      list.add(bean);
    }
    return (PaStrRtlOracleBean[]) list.toArray(new PaStrRtlOracleBean[0]);
  }

  public List toList() {
    List list = new ArrayList();
    addToList(list, this.getIdStrRt(), Types.VARCHAR);
    addToList(list, this.getDcOpnRtStr(), Types.TIMESTAMP);
    addToList(list, this.getIdPrty(), Types.VARCHAR);
    addToList(list, this.getDcClRtStr(), Types.TIMESTAMP);
    addToList(list, this.getTyRoPrty(), Types.VARCHAR);
    addToList(list, this.getQuSzStr(), Types.DECIMAL);
    addToList(list, this.getQuSzArSl(), Types.DECIMAL);
    addToList(list, this.getLuZnPrcRtStr(), Types.VARCHAR);
    addToList(list, this.getFlTxRgnl(), Types.DECIMAL);
    addToList(list, this.getDeTxRgnl(), Types.VARCHAR);
    addToList(list, this.getDeTx(), Types.VARCHAR);
    addToList(list, this.getPeTxRgnl(), Types.DECIMAL);
    addToList(list, this.getPeTx(), Types.DECIMAL);
    addToList(list, this.getPwAcsStr(), Types.VARCHAR);
    addToList(list, this.getTyCny(), Types.VARCHAR);
    addToList(list, this.getIdBrand(), Types.VARCHAR);
    addToList(list, this.getFiscalCode(), Types.VARCHAR);
    addToList(list, this.getDeStrRt(), Types.VARCHAR);
    addToList(list, this.getDeCny(), Types.VARCHAR);
    addToList(list, this.getDeCmy(), Types.VARCHAR);
    addToList(list, this.getIdCmy(), Types.VARCHAR);
    addToList(list, this.getMessage(), Types.VARCHAR);
    addToList(list, this.getRegistrationId(), Types.VARCHAR);
    addToList(list, this.getStoreType(), Types.VARCHAR);
    addToList(list, this.getTimeZone(), Types.VARCHAR);

    return list;
  }

}
