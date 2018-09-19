/*
 * put your module comment here
 * formatted with JxBeauty (c) johann.langhofer@nextra.at
 */


package com.ga.cs.dataaccess.artsoracle.databean;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import com.chelseasystems.cr.currency.ArmCurrency;
import com.chelseasystems.cs.dataaccess.artsoracle.databean.ArmPosCsgOracleBean;
import com.chelseasystems.cs.dataaccess.artsoracle.databean.BaseOracleBean;
import com.chelseasystems.cs.dataaccess.artsoracle.databean.PaCtOracleBean;
import com.chelseasystems.cs.dataaccess.artsoracle.databean.PaPrsOracleBean;
import com.chelseasystems.cs.dataaccess.artsoracle.databean.RkPosLnItmDtlOracleBean;
import com.chelseasystems.cs.dataaccess.artsoracle.databean.TrLtmRtlTrnOracleBean;
import com.chelseasystems.cs.dataaccess.artsoracle.databean.TrRtlOracleBean;
import com.chelseasystems.cs.dataaccess.artsoracle.databean.TrTrnOracleBean;
import com.chelseasystems.cs.dataaccess.artsoracle.dao.ArtsConstants;


//this class is hand-coded; not created by DAOGenerator.
public class ConsignmentSummaryReportBean extends BaseOracleBean {

  /**
   * put your documentation comment here
   */
  public ConsignmentSummaryReportBean() {
  }

  public static String COL_AI_TRN = TrTrnOracleBean.COL_AI_TRN;
  public static String COL_TS_TRN_CRT = TrTrnOracleBean.COL_TS_TRN_CRT;
  public static String COL_ID_CONSIGNMENT = ArmPosCsgOracleBean.COL_ID_CONSIGNMENT;
  public static String COL_ID_CT = TrRtlOracleBean.COL_ID_CT;
  public static String COL_CONSULTANT_ID = TrRtlOracleBean.COL_CONSULTANT_ID;
  public static String COL_ITM_RETAIL_PRICE = TrLtmRtlTrnOracleBean.COL_ITM_RETAIL_PRICE;
  public static String COL_QUANTITY = TrLtmRtlTrnOracleBean.COL_QUANTITY;
  public static String COL_LN_PRS = PaPrsOracleBean.COL_LN_PRS;
  public static String COL_FN_PRS = PaPrsOracleBean.COL_FN_PRS;
  public static String selectSql = "select " + ConsignmentSummaryReportBean.COL_AI_TRN + ", "
      + ConsignmentSummaryReportBean.COL_TS_TRN_CRT + ", "
      + ConsignmentSummaryReportBean.COL_ID_CONSIGNMENT + ", "
      + ConsignmentSummaryReportBean.COL_ID_CT + ", "
      + ConsignmentSummaryReportBean.COL_CONSULTANT_ID + ", "
      + ConsignmentSummaryReportBean.COL_ITM_RETAIL_PRICE + ", "
      + ConsignmentSummaryReportBean.COL_QUANTITY + ", " + ConsignmentSummaryReportBean.COL_LN_PRS
      + ", " + ConsignmentSummaryReportBean.COL_FN_PRS + " " + " from "
      + ArmPosCsgOracleBean.TABLE_NAME + "," + TrTrnOracleBean.TABLE_NAME + ","
      + TrRtlOracleBean.TABLE_NAME + "," + TrLtmRtlTrnOracleBean.TABLE_NAME + ","
      + RkPosLnItmDtlOracleBean.TABLE_NAME + "," + PaCtOracleBean.TABLE_NAME + ","
      + PaPrsOracleBean.TABLE_NAME + " where " + ArmPosCsgOracleBean.COL_AI_TRN + " = "
      + TrTrnOracleBean.COL_AI_TRN + " and " + TrTrnOracleBean.COL_AI_TRN + " = "
      + TrRtlOracleBean.COL_AI_TRN + " and " + TrRtlOracleBean.COL_AI_TRN + " = "
      + TrLtmRtlTrnOracleBean.COL_AI_TRN + " and " + TrLtmRtlTrnOracleBean.COL_AI_TRN + " = "
      + RkPosLnItmDtlOracleBean.COL_AI_TRN + " and " + TrLtmRtlTrnOracleBean.COL_AI_LN_ITM + " = "
      + RkPosLnItmDtlOracleBean.COL_AI_LN_ITM + " and "
      + TrLtmRtlTrnOracleBean.COL_POS_LN_ITM_TY_ID + " = "
      + ArtsConstants.POS_LINE_ITEM_TYPE_CONSIGNMENT + " and " + TrRtlOracleBean.COL_ID_CT + " = "
      + PaCtOracleBean.COL_ID_CT + " and " + PaCtOracleBean.COL_ID_PRTY + " = "
      + PaPrsOracleBean.COL_ID_PRTY_PRS + " and " + TrTrnOracleBean.COL_ID_VOID + " is null "
      + " and (" + RkPosLnItmDtlOracleBean.COL_FL_PROCESSED + " = '0' or "
      + RkPosLnItmDtlOracleBean.COL_FL_PROCESSED + " is null) " + " and "
      + TrTrnOracleBean.COL_ID_STR_RT + " = ? ";
  public static String TABLE_NAME = null;

  /**
   * put your documentation comment here
   * @return
   */
  public String getSelectSql() {
    return selectSql;
  }

  /**
   * put your documentation comment here
   * @return
   */
  public String getInsertSql() {
    return null;
  }

  /**
   * put your documentation comment here
   * @return
   */
  public String getUpdateSql() {
    return null;
  }

  /**
   * put your documentation comment here
   * @return
   */
  public String getDeleteSql() {
    return null;
  }

  private String aiTrn;
  private Date tsTrnCrt;
  private String idConsignment;
  private String idCt;
  private String consultantId;
  private ArmCurrency itmRetailPrice;
  private Long quantity;
  private String lnPrs;
  private String fnPrs;

  /**
   * put your documentation comment here
   * @param rs
   * @return
   * @exception SQLException
   */
  public BaseOracleBean[] getDatabeans(ResultSet rs)
      throws SQLException {
    ArrayList list = new ArrayList();
    while (rs.next()) {
      ConsignmentSummaryReportBean bean = new ConsignmentSummaryReportBean();
      bean.aiTrn = getStringFromResultSet(rs, "AI_TRN");
      bean.tsTrnCrt = getDateFromResultSet(rs, "TS_TRN_CRT");
      bean.idConsignment = getStringFromResultSet(rs, "ID_CONSIGNMENT");
      bean.idCt = getStringFromResultSet(rs, "ID_CT");
      bean.consultantId = getStringFromResultSet(rs, "CONSULTANT_ID");
      bean.itmRetailPrice = getCurrencyFromResultSet(rs, "ITM_RETAIL_PRICE");
      bean.quantity = getLongFromResultSet(rs, "QUANTITY");
      bean.lnPrs = getStringFromResultSet(rs, "LN_PRS");
      bean.fnPrs = getStringFromResultSet(rs, "FN_PRS");
      list.add(bean);
    }
    return (ConsignmentSummaryReportBean[])list.toArray(new ConsignmentSummaryReportBean[0]);
  }

  /**
   * put your documentation comment here
   * @return
   */
  public List toList() {
    List list = new ArrayList();
    addToList(list, this.getAiTrn(), Types.VARCHAR);
    addToList(list, this.getTsTrnCrt(), Types.TIMESTAMP);
    addToList(list, this.getIdConsignment(), Types.VARCHAR);
    addToList(list, this.getIdCt(), Types.VARCHAR);
    addToList(list, this.getConsultantId(), Types.VARCHAR);
    addToList(list, this.getItmRetailPrice(), Types.VARCHAR);
    addToList(list, this.getQuantity(), Types.DECIMAL);
    addToList(list, this.getLnPrs(), Types.VARCHAR);
    addToList(list, this.getFnPrs(), Types.VARCHAR);
    return list;
  }

  /**
   * put your documentation comment here
   * @return
   */
  public String getIdConsignment() {
    return idConsignment;
  }

  /**
   * put your documentation comment here
   * @param idConsignment
   */
  public void setIdConsignment(String idConsignment) {
    this.idConsignment = idConsignment;
  }

  /**
   * put your documentation comment here
   * @return
   */
  public String getAiTrn() {
    return aiTrn;
  }

  /**
   * put your documentation comment here
   * @param aiTrn
   */
  public void setAiTrn(String aiTrn) {
    this.aiTrn = aiTrn;
  }

  /**
   * put your documentation comment here
   * @return
   */
  public Date getTsTrnCrt() {
    return tsTrnCrt;
  }

  /**
   * put your documentation comment here
   * @param tsTrnCrt
   */
  public void setTsTrnCrt(Date tsTrnCrt) {
    this.tsTrnCrt = tsTrnCrt;
  }

  /**
   * put your documentation comment here
   * @return
   */
  public String getConsultantId() {
    return consultantId;
  }

  /**
   * put your documentation comment here
   * @param consultantId
   */
  public void setConsultantId(String consultantId) {
    this.consultantId = consultantId;
  }

  /**
   * put your documentation comment here
   * @return
   */
  public String getFnPrs() {
    return fnPrs;
  }

  /**
   * put your documentation comment here
   * @param fnPrs
   */
  public void setFnPrs(String fnPrs) {
    this.fnPrs = fnPrs;
  }

  /**
   * put your documentation comment here
   * @return
   */
  public String getIdCt() {
    return idCt;
  }

  /**
   * put your documentation comment here
   * @param idCt
   */
  public void setIdCt(String idCt) {
    this.idCt = idCt;
  }

  /**
   * put your documentation comment here
   * @return
   */
  public ArmCurrency getItmRetailPrice() {
    return itmRetailPrice;
  }

  /**
   * put your documentation comment here
   * @param itmRetailPrice
   */
  public void setItmRetailPrice(ArmCurrency itmRetailPrice) {
    this.itmRetailPrice = itmRetailPrice;
  }

  /**
   * put your documentation comment here
   * @return
   */
  public String getLnPrs() {
    return lnPrs;
  }

  /**
   * put your documentation comment here
   * @param lnPrs
   */
  public void setLnPrs(String lnPrs) {
    this.lnPrs = lnPrs;
  }

  /**
   * put your documentation comment here
   * @return
   */
  public Long getQuantity() {
    return quantity;
  }

  /**
   * put your documentation comment here
   * @param quantity
   */
  public void setQuantity(Long quantity) {
    this.quantity = quantity;
  }

  /**
   * put your documentation comment here
   * @return
   */
  public String toString() {
    List list = this.toList();
    StringBuffer sb = new StringBuffer();
    for (Iterator it = list.iterator(); it.hasNext(); ) {
      sb.append("[" + it.next().toString() + "]");
    }
    return sb.toString();
  }
}

