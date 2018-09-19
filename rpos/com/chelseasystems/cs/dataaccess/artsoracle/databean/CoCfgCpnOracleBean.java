package com.chelseasystems.cs.dataaccess.artsoracle.databean;
 
import com.chelseasystems.cr.currency.ArmCurrency;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class CoCfgCpnOracleBean extends BaseOracleBean
 {
  public static String selectSql = "select ID_CPN, DE_CPN, CD_CNY, MO_MX_DSC_AMT, MO_AMT, ID_STR_RT, FL_PCT, ID_PFT_CTR, CD_TX_CLS, CD_XPT, DT_EF, DT_EX, FL_PRMPT, FL_DPLY, CD_STS, ID_CFG from CO_CFG_CPN ";
  public static String insertSql = "insert into CO_CFG_CPN (ID_CPN, DE_CPN, CD_CNY, MO_MX_DSC_AMT, MO_AMT, ID_STR_RT, FL_PCT, ID_PFT_CTR, CD_TX_CLS, CD_XPT, DT_EF, DT_EX, FL_PRMPT, FL_DPLY, CD_STS, ID_CFG) values (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
  public static String updateSql = "update CO_CFG_CPN set ID_CPN = ?, DE_CPN = ?, CD_CNY = ?, MO_MX_DSC_AMT = ?, MO_AMT = ?, ID_STR_RT = ?, FL_PCT = ?, ID_PFT_CTR = ?, CD_TX_CLS = ?, CD_XPT = ?, DT_EF = ?, DT_EX = ?, FL_PRMPT = ?, FL_DPLY = ?, CD_STS = ?, ID_CFG = ? ";
  // Added by Satin.
  //added for updating coupon used flag 
  public static String updateSqlForCoupon = "update CO_CFG_CPN set CD_STS = ?";
  public static String deleteSql = "delete from CO_CFG_CPN ";
 
  public static String TABLE_NAME = "CO_CFG_CPN";
  public static String COL_ID_CPN = "CO_CFG_CPN.ID_CPN";
  public static String COL_DE_CPN = "CO_CFG_CPN.DE_CPN";
  public static String COL_CD_CNY = "CO_CFG_CPN.CD_CNY";
  public static String COL_MO_MX_DSC_AMT = "CO_CFG_CPN.MO_MX_DSC_AMT";
  public static String COL_MO_AMT = "CO_CFG_CPN.MO_AMT";
  // Added by Satin.
  //ADDED FOR STORE ID
  public static String ID_STR_RT = "CO_CFG_CPN.ID_STR_RT";
  public static String COL_FL_PCT = "CO_CFG_CPN.FL_PCT";
  public static String COL_ID_PFT_CTR = "CO_CFG_CPN.ID_PFT_CTR";
  public static String COL_CD_TX_CLS = "CO_CFG_CPN.CD_TX_CLS";
  public static String COL_CD_XPT = "CO_CFG_CPN.CD_XPT";
  public static String COL_DT_EF = "CO_CFG_CPN.DT_EF";
  public static String COL_DT_EX = "CO_CFG_CPN.DT_EX";
  public static String COL_FL_PRMPT = "CO_CFG_CPN.FL_PRMPT";
  public static String COL_FL_DPLY = "CO_CFG_CPN.FL_DPLY";
  public static String COL_CD_STS = "CO_CFG_CPN.CD_STS";
  public static String COL_ID_CFG = "CO_CFG_CPN.ID_CFG";
  private String idCpn;
  private String deCpn;
  private String cdCny;
  private ArmCurrency moMxDscAmt;
  private ArmCurrency moAmt;
  //Added by Satin.
  // Added for store
  private String idStrRt;
  private Boolean flPct;
  private String idPftCtr;
  private String cdTxCls;
  private String cdXpt;
  private Date dtEf;
  private Date dtEx;
  private Boolean flPrmpt;
  private Boolean flDply;
  private String cdSts;
  private String idCfg;
 
  public String getSelectSql()
   {
    return selectSql; } 
  public String getInsertSql() { return insertSql; } 
  public String getUpdateSql() { return updateSql; } 
  public String getDeleteSql() { return deleteSql; }
  // Added by Satin.
  //added for updating coupon used flag
  public String getUpadteSqlForCoupon(){ return updateSqlForCoupon; }
 
  public String getIdCpn()
   {
    return this.idCpn; } 
  public void setIdCpn(String idCpn) { this.idCpn = idCpn; }
 
  public String getDeCpn() { return this.deCpn; } 
  public void setDeCpn(String deCpn) { this.deCpn = deCpn; }
 
  public String getCdCny() { return this.cdCny; } 
  public void setCdCny(String cdCny) { this.cdCny = cdCny; }
 
  public ArmCurrency getMoMxDscAmt() { return this.moMxDscAmt; } 
  public void setMoMxDscAmt(ArmCurrency moMxDscAmt) { this.moMxDscAmt = moMxDscAmt; }
 
  public ArmCurrency getMoAmt() { return this.moAmt; } 
  public void setMoAmt(ArmCurrency moAmt) { this.moAmt = moAmt; }
 
  //Added by Satin.
  // getter-setter for store id
  public String getIdStrRt() { return this.idStrRt; } 
  public void setIdStrRt(String idStrRt) { this.idStrRt = idStrRt; }
  
  public Boolean getFlPct() { return this.flPct; } 
  public void setFlPct(Boolean flPct) { this.flPct = flPct; } 
  public void setFlPct(boolean flPct) { this.flPct = new Boolean(flPct); }
 
  public String getIdPftCtr() { return this.idPftCtr; } 
  public void setIdPftCtr(String idPftCtr) { this.idPftCtr = idPftCtr; }
 
  public String getCdTxCls() { return this.cdTxCls; } 
  public void setCdTxCls(String cdTxCls) { this.cdTxCls = cdTxCls; }
 
  public String getCdXpt() { return this.cdXpt; } 
  public void setCdXpt(String cdXpt) { this.cdXpt = cdXpt; }
 
  public Date getDtEf() { return this.dtEf; } 
  public void setDtEf(Date dtEf) { this.dtEf = dtEf; }
 
  public Date getDtEx() { return this.dtEx; } 
  public void setDtEx(Date dtEx) { this.dtEx = dtEx; }
 
  public Boolean getFlPrmpt() { return this.flPrmpt; } 
  public void setFlPrmpt(Boolean flPrmpt) { this.flPrmpt = flPrmpt; } 
  public void setFlPrmpt(boolean flPrmpt) { this.flPrmpt = new Boolean(flPrmpt); }
 
  public Boolean getFlDply() { return this.flDply; } 
  public void setFlDply(Boolean flDply) { this.flDply = flDply; } 
  public void setFlDply(boolean flDply) { this.flDply = new Boolean(flDply); }
 
  public String getCdSts() { return this.cdSts; } 
  public void setCdSts(String cdSts) { this.cdSts = cdSts; }
 
  public String getIdCfg() { return this.idCfg; } 
  public void setIdCfg(String idCfg) { this.idCfg = idCfg; }
 
  public BaseOracleBean[] getDatabeans(ResultSet rs) throws SQLException {
    ArrayList list = new ArrayList();
    while (rs.next()) {
      CoCfgCpnOracleBean bean = new CoCfgCpnOracleBean();
      bean.idCpn = getStringFromResultSet(rs, "ID_CPN");
      bean.deCpn = getStringFromResultSet(rs, "DE_CPN");
      bean.cdCny = getStringFromResultSet(rs, "CD_CNY");
      bean.moMxDscAmt = getCurrencyFromResultSet(rs, "MO_MX_DSC_AMT");
      bean.moAmt = getCurrencyFromResultSet(rs, "MO_AMT");
      //Added by Satin.
      // ADDED FOR STORE ID
      bean.idStrRt = getStringFromResultSet(rs, "ID_STR_RT");
      bean.flPct = getBooleanFromResultSet(rs, "FL_PCT");
      bean.idPftCtr = getStringFromResultSet(rs, "ID_PFT_CTR");
      bean.cdTxCls = getStringFromResultSet(rs, "CD_TX_CLS");
      bean.cdXpt = getStringFromResultSet(rs, "CD_XPT");
      bean.dtEf = getDateFromResultSet(rs, "DT_EF");
      bean.dtEx = getDateFromResultSet(rs, "DT_EX");
      bean.flPrmpt = getBooleanFromResultSet(rs, "FL_PRMPT");
      bean.flDply = getBooleanFromResultSet(rs, "FL_DPLY");
      bean.cdSts = getStringFromResultSet(rs, "CD_STS");
      bean.idCfg = getStringFromResultSet(rs, "ID_CFG");
      list.add(bean);
     }
    return ((CoCfgCpnOracleBean[])(CoCfgCpnOracleBean[])list.toArray(new CoCfgCpnOracleBean[0]));
   }
 
   public List toList() {
    List list = new ArrayList();
    addToList(list, getIdCpn(), 12);
    addToList(list, getDeCpn(), 12);
    addToList(list, getCdCny(), 12);
    addToList(list, getMoMxDscAmt(), 12);
    addToList(list, getMoAmt(), 12);
    
    //Added by Satin.
    // added for storeId
    addToList(list, getIdStrRt(), 12);
    
    addToList(list, getFlPct(), 3);
    addToList(list, getIdPftCtr(), 12);
    addToList(list, getCdTxCls(), 12);
    addToList(list, getCdXpt(), 12);
    addToList(list, getDtEf(), 93);
    addToList(list, getDtEx(), 93);
    addToList(list, getFlPrmpt(), 3);
    addToList(list, getFlDply(), 3);
    addToList(list, getCdSts(), 12);
    addToList(list, getIdCfg(), 12);
    return list;
  }
 }

/* Location:           C:\armani\armani\retek\library\retek_shared_common.jar
 * Qualified Name:     com.chelseasystems.cs.dataaccess.artsoracle.databean.CoCfgCpnOracleBean
 * Java Class Version: 5 (49.0)
 * JD-Core Version:    0.5.3
 */