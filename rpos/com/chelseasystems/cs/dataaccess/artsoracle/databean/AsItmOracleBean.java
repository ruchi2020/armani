package com.chelseasystems.cs.dataaccess.artsoracle.databean;

 import java.sql.ResultSet;
 import java.sql.SQLException;
 import java.util.ArrayList;
 import java.util.List;
 
 public class AsItmOracleBean extends BaseOracleBean
 {
   public static String selectSql = "select ID_ITM, BARCODE, ID_LN_PRC, ID_STRC_MR, LU_HRC_MR_LV, FL_ITM_DSC, LU_SBSN, LU_SN, FY, FL_ADT_ITM_PRC, NM_BRN, FL_AZN_FR_SLS, LU_ITM_USG, NM_ITM, DE_ITM, TY_ITM, LU_KT_ST, FL_ITM_SBST_IDN, LU_CLN_ORD, ID_DPT_POS, CD_CLB_MKTG, ID_CLSS, ID_SBCL, LU_EXM_TX, ID_PRT_ITM from AS_ITM ";
   public static String insertSql = "insert into AS_ITM (ID_ITM, BARCODE, ID_LN_PRC, ID_STRC_MR, LU_HRC_MR_LV, FL_ITM_DSC, LU_SBSN, LU_SN, FY, FL_ADT_ITM_PRC, NM_BRN, FL_AZN_FR_SLS, LU_ITM_USG, NM_ITM, DE_ITM, TY_ITM, LU_KT_ST, FL_ITM_SBST_IDN, LU_CLN_ORD, ID_DPT_POS, CD_CLB_MKTG, ID_CLSS, ID_SBCL, LU_EXM_TX, ID_PRT_ITM) values (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
   public static String updateSql = "update AS_ITM set ID_ITM = ?, BARCODE = ?, ID_LN_PRC = ?, ID_STRC_MR = ?, LU_HRC_MR_LV = ?, FL_ITM_DSC = ?, LU_SBSN = ?, LU_SN = ?, FY = ?, FL_ADT_ITM_PRC = ?, NM_BRN = ?, FL_AZN_FR_SLS = ?, LU_ITM_USG = ?, NM_ITM = ?, DE_ITM = ?, TY_ITM = ?, LU_KT_ST = ?, FL_ITM_SBST_IDN = ?, LU_CLN_ORD = ?, ID_DPT_POS = ?, CD_CLB_MKTG = ?, ID_CLSS = ?, ID_SBCL = ?, LU_EXM_TX = ?, ID_PRT_ITM = ? ";
   public static String deleteSql = "delete from AS_ITM ";
   // changes made by Satin for Exception Item.
   public static String selectSql1 = "select ID_ITM from AS_ITM ";
   
   public static String TABLE_NAME = "AS_ITM";
   public static String COL_ID_ITM = "AS_ITM.ID_ITM";
   public static String COL_ID_LN_PRC = "AS_ITM.ID_LN_PRC";
   public static String COL_ID_STRC_MR = "AS_ITM.ID_STRC_MR";
   public static String COL_LU_HRC_MR_LV = "AS_ITM.LU_HRC_MR_LV";
   public static String COL_FL_ITM_DSC = "AS_ITM.FL_ITM_DSC";
   public static String COL_LU_SBSN = "AS_ITM.LU_SBSN";
   public static String COL_LU_SN = "AS_ITM.LU_SN";
   public static String COL_FY = "AS_ITM.FY";
   public static String COL_FL_ADT_ITM_PRC = "AS_ITM.FL_ADT_ITM_PRC";
   public static String COL_NM_BRN = "AS_ITM.NM_BRN";
   public static String COL_FL_AZN_FR_SLS = "AS_ITM.FL_AZN_FR_SLS";
   public static String COL_LU_ITM_USG = "AS_ITM.LU_ITM_USG";
   public static String COL_NM_ITM = "AS_ITM.NM_ITM";
   public static String COL_DE_ITM = "AS_ITM.DE_ITM";
   public static String COL_TY_ITM = "AS_ITM.TY_ITM";
   public static String COL_LU_KT_ST = "AS_ITM.LU_KT_ST";
   public static String COL_FL_ITM_SBST_IDN = "AS_ITM.FL_ITM_SBST_IDN";
   public static String COL_LU_CLN_ORD = "AS_ITM.LU_CLN_ORD";
   public static String COL_ID_DPT_POS = "AS_ITM.ID_DPT_POS";
   public static String COL_CD_CLB_MKTG = "AS_ITM.CD_CLB_MKTG";
   public static String COL_ID_CLSS = "AS_ITM.ID_CLSS";
   public static String COL_ID_SBCL = "AS_ITM.ID_SBCL";
   public static String COL_LU_EXM_TX = "AS_ITM.LU_EXM_TX";
   public static String COL_ID_PRT_ITM = "AS_ITM.ID_PRT_ITM";
   // Added for barcode
   public static String COL_BARCODE = "AS_ITM.BARCODE";
   
   private String idItm;
   private String idLnPrc;
   private String idStrcMr;
   private String luHrcMrLv;
   private Boolean flItmDsc;
   private String luSbsn;
   private String luSn;
   private String fy;
   private Boolean flAdtItmPrc;
   private String nmBrn;
   private Boolean flAznFrSls;
   private String luItmUsg;
   private String nmItm;
   private String deItm;
   private String tyItm;
   private String luKtSt;
   private Boolean flItmSbstIdn;
   private String luClnOrd;
   private String idDptPos;
   private String cdClbMktg;
   private String idClss;
   private String idSbcl;
   private String luExmTx;
   private String idPrtItm;
   private String barcode;
 
   public String getSelectSql()
   {
     return selectSql; } 
   public String getInsertSql() { return insertSql; } 
   public String getUpdateSql() { return updateSql; } 
   public String getDeleteSql() { return deleteSql;
   }
 
   public String getIdItm()
   {
     return this.idItm; } 
   public void setIdItm(String idItm) { this.idItm = idItm; }
 

   // Added by Satin for exception Item.
   public String getBarcode() { return this.barcode; } 
   public void setBarcode(String barcode) { this.barcode = barcode; }
   
   
   
   public String getIdLnPrc() { return this.idLnPrc; } 
   public void setIdLnPrc(String idLnPrc) { this.idLnPrc = idLnPrc; }
 
   public String getIdStrcMr() { return this.idStrcMr; } 
   public void setIdStrcMr(String idStrcMr) { this.idStrcMr = idStrcMr; }
 
   public String getLuHrcMrLv() { return this.luHrcMrLv; } 
   public void setLuHrcMrLv(String luHrcMrLv) { this.luHrcMrLv = luHrcMrLv; }
 
   public Boolean getFlItmDsc() { return this.flItmDsc; } 
   public void setFlItmDsc(Boolean flItmDsc) { this.flItmDsc = flItmDsc; } 
   public void setFlItmDsc(boolean flItmDsc) { this.flItmDsc = new Boolean(flItmDsc); }
 
   public String getLuSbsn() { return this.luSbsn; } 
   public void setLuSbsn(String luSbsn) { this.luSbsn = luSbsn; }
 
   public String getLuSn() { return this.luSn; } 
   public void setLuSn(String luSn) { this.luSn = luSn; }
 
   public String getFy() { return this.fy; } 
   public void setFy(String fy) { this.fy = fy; }
 
   public Boolean getFlAdtItmPrc() { return this.flAdtItmPrc; } 
   public void setFlAdtItmPrc(Boolean flAdtItmPrc) { this.flAdtItmPrc = flAdtItmPrc; } 
   public void setFlAdtItmPrc(boolean flAdtItmPrc) { this.flAdtItmPrc = new Boolean(flAdtItmPrc); }
 
   public String getNmBrn() { return this.nmBrn; } 
   public void setNmBrn(String nmBrn) { this.nmBrn = nmBrn; }
 
   public Boolean getFlAznFrSls() { return this.flAznFrSls; } 
   public void setFlAznFrSls(Boolean flAznFrSls) { this.flAznFrSls = flAznFrSls; } 
   public void setFlAznFrSls(boolean flAznFrSls) { this.flAznFrSls = new Boolean(flAznFrSls); }
 
   public String getLuItmUsg() { return this.luItmUsg; } 
   public void setLuItmUsg(String luItmUsg) { this.luItmUsg = luItmUsg; }
 
   public String getNmItm() { return this.nmItm; } 
   public void setNmItm(String nmItm) { this.nmItm = nmItm; }
 
   public String getDeItm() { return this.deItm; } 
   public void setDeItm(String deItm) { this.deItm = deItm; }
 
   public String getTyItm() { return this.tyItm; } 
   public void setTyItm(String tyItm) { this.tyItm = tyItm; }
 
   public String getLuKtSt() { return this.luKtSt; } 
   public void setLuKtSt(String luKtSt) { this.luKtSt = luKtSt; }
 
   public Boolean getFlItmSbstIdn() { return this.flItmSbstIdn; } 
   public void setFlItmSbstIdn(Boolean flItmSbstIdn) { this.flItmSbstIdn = flItmSbstIdn; } 
   public void setFlItmSbstIdn(boolean flItmSbstIdn) { this.flItmSbstIdn = new Boolean(flItmSbstIdn); }
 
   public String getLuClnOrd() { return this.luClnOrd; } 
   public void setLuClnOrd(String luClnOrd) { this.luClnOrd = luClnOrd; }
 
   public String getIdDptPos() { return this.idDptPos; } 
   public void setIdDptPos(String idDptPos) { this.idDptPos = idDptPos; }
 
   public String getCdClbMktg() { return this.cdClbMktg; } 
   public void setCdClbMktg(String cdClbMktg) { this.cdClbMktg = cdClbMktg; }
 
   public String getIdClss() { return this.idClss; } 
   public void setIdClss(String idClss) { this.idClss = idClss; }
 
   public String getIdSbcl() { return this.idSbcl; } 
   public void setIdSbcl(String idSbcl) { this.idSbcl = idSbcl; }
 
   public String getLuExmTx() { return this.luExmTx; } 
   public void setLuExmTx(String luExmTx) { this.luExmTx = luExmTx; }
 
   public String getIdPrtItm() { return this.idPrtItm; } 
   public void setIdPrtItm(String idPrtItm) { this.idPrtItm = idPrtItm; }
 
   public BaseOracleBean[] getDatabeans(ResultSet rs) throws SQLException {
     ArrayList list = new ArrayList();
     while (rs.next()) {
       AsItmOracleBean bean = new AsItmOracleBean();
       bean.idItm = getStringFromResultSet(rs, "ID_ITM");
       
       // Added by Satin for exception item.
       bean.barcode = getStringFromResultSet(rs, "BARCODE");
       
       bean.idLnPrc = getStringFromResultSet(rs, "ID_LN_PRC");
       bean.idStrcMr = getStringFromResultSet(rs, "ID_STRC_MR");
       bean.luHrcMrLv = getStringFromResultSet(rs, "LU_HRC_MR_LV");
       bean.flItmDsc = getBooleanFromResultSet(rs, "FL_ITM_DSC");
       bean.luSbsn = getStringFromResultSet(rs, "LU_SBSN");
       bean.luSn = getStringFromResultSet(rs, "LU_SN");
       bean.fy = getStringFromResultSet(rs, "FY");
       bean.flAdtItmPrc = getBooleanFromResultSet(rs, "FL_ADT_ITM_PRC");
       bean.nmBrn = getStringFromResultSet(rs, "NM_BRN");
       bean.flAznFrSls = getBooleanFromResultSet(rs, "FL_AZN_FR_SLS");
       bean.luItmUsg = getStringFromResultSet(rs, "LU_ITM_USG");
       bean.nmItm = getStringFromResultSet(rs, "NM_ITM");
       bean.deItm = getStringFromResultSet(rs, "DE_ITM");
       bean.tyItm = getStringFromResultSet(rs, "TY_ITM");
       bean.luKtSt = getStringFromResultSet(rs, "LU_KT_ST");
       bean.flItmSbstIdn = getBooleanFromResultSet(rs, "FL_ITM_SBST_IDN");
       bean.luClnOrd = getStringFromResultSet(rs, "LU_CLN_ORD");
       bean.idDptPos = getStringFromResultSet(rs, "ID_DPT_POS");
       bean.cdClbMktg = getStringFromResultSet(rs, "CD_CLB_MKTG");
       bean.idClss = getStringFromResultSet(rs, "ID_CLSS");
       bean.idSbcl = getStringFromResultSet(rs, "ID_SBCL");
       bean.luExmTx = getStringFromResultSet(rs, "LU_EXM_TX");
       bean.idPrtItm = getStringFromResultSet(rs, "ID_PRT_ITM");
       list.add(bean);
     }
     return ((AsItmOracleBean[])(AsItmOracleBean[])list.toArray(new AsItmOracleBean[0]));
   }
 
   public List toList() {
     List list = new ArrayList();
     addToList(list, getIdItm(), 12);
     
     // Added by Satin for exception item.
     addToList(list, getBarcode(), 12);
     
     addToList(list, getIdLnPrc(), 12);
     addToList(list, getIdStrcMr(), 12);
     addToList(list, getLuHrcMrLv(), 12);
     addToList(list, getFlItmDsc(), 3);
     addToList(list, getLuSbsn(), 12);
     addToList(list, getLuSn(), 12);
     addToList(list, getFy(), 12);
     addToList(list, getFlAdtItmPrc(), 3);
     addToList(list, getNmBrn(), 12);
     addToList(list, getFlAznFrSls(), 3);
     addToList(list, getLuItmUsg(), 12);
     addToList(list, getNmItm(), 12);
     addToList(list, getDeItm(), 12);
     addToList(list, getTyItm(), 12);
     addToList(list, getLuKtSt(), 12);
     addToList(list, getFlItmSbstIdn(), 3);
     addToList(list, getLuClnOrd(), 12);
     addToList(list, getIdDptPos(), 12);
     addToList(list, getCdClbMktg(), 12);
     addToList(list, getIdClss(), 12);
     addToList(list, getIdSbcl(), 12);
     addToList(list, getLuExmTx(), 12);
     addToList(list, getIdPrtItm(), 12);
     return list;
   }
 }

/* Location:           C:\armani\armani\retek\library\retek_shared_common.jar
 * Qualified Name:     com.chelseasystems.cs.dataaccess.artsoracle.databean.AsItmOracleBean
 * Java Class Version: 5 (49.0)
 * JD-Core Version:    0.5.3
 */