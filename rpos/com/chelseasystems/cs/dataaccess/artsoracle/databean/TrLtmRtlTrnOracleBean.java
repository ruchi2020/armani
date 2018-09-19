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
 * This class is an object representation of the Arts database table TR_LTM_RTL_TRN<BR>
 * Followings are the column of the table: <BR>
 *     AI_LN_ITM(PK) -- NUMBER(22)<BR>
 *     TY_LN_ITM -- VARCHAR2(20)<BR>
 *     TS_LN_ITM_BGN -- DATE(7)<BR>
 *     TS_LN_ITM_END -- DATE(7)<BR>
 *     FL_VD_LN_ITM -- NUMBER(1)<BR>
 *     AI_TRN(PK) -- VARCHAR2(128)<BR>
 *     ID_ITM -- VARCHAR2(128)<BR>
 *     POS_LN_ITM_TY_ID -- NUMBER(3)<BR>
 *     ADD_CONSULTANT_ID -- VARCHAR2(128)<BR>
 *     MANUAL_UNIT_PRICE -- VARCHAR2(75)<BR>
 *     MANUAL_MD_AMT -- VARCHAR2(75)<BR>
 *     VALID_NET_AMT_FL -- NUMBER(1)<BR>
 *     ITM_SEL_PRICE -- VARCHAR2(75)<BR>
 *     ITM_RETAIL_PRICE -- VARCHAR2(75)<BR>
 *     MISC_ITEM_RG_TXBL -- NUMBER(1)<BR>
 *     MISC_ITEM_TXBL -- NUMBER(1)<BR>
 *     MISC_ITEM_DESC -- VARCHAR2(200)<BR>
 *     MISC_ITEM_ID -- VARCHAR2(128)<BR>
 *     RETURN_COMMENTS -- VARCHAR2(200)<BR>
 *     RETURN_REASON_ID -- VARCHAR2(200)<BR>
 *     MANUAL_MD_REASON -- VARCHAR2(200)<BR>
 *     REG_TAX_EXEMPT_ID -- VARCHAR2(128)<BR>
 *     TAX_EXEMPT_ID -- VARCHAR2(128)<BR>
 *     SHIP_REQ_SEQ_NUM -- NUMBER(3)<BR>
 *     MISC_ITEM_COMMENT -- VARCHAR2(200)<BR>
 *     MISC_ITEM_GL_ACC -- VARCHAR2(200)<BR>
 *     QUANTITY -- NUMBER(22)<BR>
 *     DOC_NUM -- VARCHAR2(20)<BR>
 *     VAT_COMMENTS -- VARCHAR2(200)<BR>
 *     TY_DOC -- VARCHAR2(10)<BR>
 *     FL_EXCHANGE -- NUMBER(1)<BR>
 *     EXT_BARCODE -- VARCHAR2(128)<BR>
 *     APPROVER_ID -- VARCHAR2(128)<BR>
 *
 */
public class TrLtmRtlTrnOracleBean extends BaseOracleBean {

  public TrLtmRtlTrnOracleBean() {}

  public static String selectSql = "select AI_LN_ITM, TY_LN_ITM, TS_LN_ITM_BGN, TS_LN_ITM_END, FL_VD_LN_ITM, AI_TRN, ID_ITM, POS_LN_ITM_TY_ID, ADD_CONSULTANT_ID, MANUAL_UNIT_PRICE, MANUAL_MD_AMT, VALID_NET_AMT_FL, ITM_SEL_PRICE, ITM_RETAIL_PRICE, MISC_ITEM_RG_TXBL, MISC_ITEM_TXBL, MISC_ITEM_DESC, MISC_ITEM_ID, RETURN_COMMENTS, RETURN_REASON_ID, MANUAL_MD_REASON, REG_TAX_EXEMPT_ID, TAX_EXEMPT_ID, SHIP_REQ_SEQ_NUM, MISC_ITEM_COMMENT, MISC_ITEM_GL_ACC, QUANTITY, DOC_NUM, VAT_COMMENTS, TY_DOC, FL_EXCHANGE, EXT_BARCODE, APPROVER_ID, AJB_SEQ from TR_LTM_RTL_TRN ";
  public static String insertSql = "insert into TR_LTM_RTL_TRN (AI_LN_ITM, TY_LN_ITM, TS_LN_ITM_BGN, TS_LN_ITM_END, FL_VD_LN_ITM, AI_TRN, ID_ITM, POS_LN_ITM_TY_ID, ADD_CONSULTANT_ID, MANUAL_UNIT_PRICE, MANUAL_MD_AMT, VALID_NET_AMT_FL, ITM_SEL_PRICE, ITM_RETAIL_PRICE, MISC_ITEM_RG_TXBL, MISC_ITEM_TXBL, MISC_ITEM_DESC, MISC_ITEM_ID, RETURN_COMMENTS, RETURN_REASON_ID, MANUAL_MD_REASON, REG_TAX_EXEMPT_ID, TAX_EXEMPT_ID, SHIP_REQ_SEQ_NUM, MISC_ITEM_COMMENT, MISC_ITEM_GL_ACC, QUANTITY, DOC_NUM, VAT_COMMENTS, TY_DOC, FL_EXCHANGE, EXT_BARCODE, APPROVER_ID, AJB_SEQ) values (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
  public static String updateSql = "update TR_LTM_RTL_TRN set AI_LN_ITM = ?, TY_LN_ITM = ?, TS_LN_ITM_BGN = ?, TS_LN_ITM_END = ?, FL_VD_LN_ITM = ?, AI_TRN = ?, ID_ITM = ?, POS_LN_ITM_TY_ID = ?, ADD_CONSULTANT_ID = ?, MANUAL_UNIT_PRICE = ?, MANUAL_MD_AMT = ?, VALID_NET_AMT_FL = ?, ITM_SEL_PRICE = ?, ITM_RETAIL_PRICE = ?, MISC_ITEM_RG_TXBL = ?, MISC_ITEM_TXBL = ?, MISC_ITEM_DESC = ?, MISC_ITEM_ID = ?, RETURN_COMMENTS = ?, RETURN_REASON_ID = ?, MANUAL_MD_REASON = ?, REG_TAX_EXEMPT_ID = ?, TAX_EXEMPT_ID = ?, SHIP_REQ_SEQ_NUM = ?, MISC_ITEM_COMMENT = ?, MISC_ITEM_GL_ACC = ?, QUANTITY = ?, DOC_NUM = ?, VAT_COMMENTS = ?, TY_DOC = ?, FL_EXCHANGE = ?, , EXT_BARCODE = ?, APPROVER_ID =?, AJB_SEQ =? ";
  public static String deleteSql = "delete from TR_LTM_RTL_TRN ";

  public static String TABLE_NAME = "TR_LTM_RTL_TRN";
  public static String COL_AI_LN_ITM = "TR_LTM_RTL_TRN.AI_LN_ITM";
  public static String COL_TY_LN_ITM = "TR_LTM_RTL_TRN.TY_LN_ITM";
  public static String COL_TS_LN_ITM_BGN = "TR_LTM_RTL_TRN.TS_LN_ITM_BGN";
  public static String COL_TS_LN_ITM_END = "TR_LTM_RTL_TRN.TS_LN_ITM_END";
  public static String COL_FL_VD_LN_ITM = "TR_LTM_RTL_TRN.FL_VD_LN_ITM";
  public static String COL_AI_TRN = "TR_LTM_RTL_TRN.AI_TRN";
  public static String COL_ID_ITM = "TR_LTM_RTL_TRN.ID_ITM";
  public static String COL_POS_LN_ITM_TY_ID = "TR_LTM_RTL_TRN.POS_LN_ITM_TY_ID";
  public static String COL_ADD_CONSULTANT_ID = "TR_LTM_RTL_TRN.ADD_CONSULTANT_ID";
  public static String COL_MANUAL_UNIT_PRICE = "TR_LTM_RTL_TRN.MANUAL_UNIT_PRICE";
  public static String COL_MANUAL_MD_AMT = "TR_LTM_RTL_TRN.MANUAL_MD_AMT";
  public static String COL_VALID_NET_AMT_FL = "TR_LTM_RTL_TRN.VALID_NET_AMT_FL";
  public static String COL_ITM_SEL_PRICE = "TR_LTM_RTL_TRN.ITM_SEL_PRICE";
  public static String COL_ITM_RETAIL_PRICE = "TR_LTM_RTL_TRN.ITM_RETAIL_PRICE";
  public static String COL_MISC_ITEM_RG_TXBL = "TR_LTM_RTL_TRN.MISC_ITEM_RG_TXBL";
  public static String COL_MISC_ITEM_TXBL = "TR_LTM_RTL_TRN.MISC_ITEM_TXBL";
  public static String COL_MISC_ITEM_DESC = "TR_LTM_RTL_TRN.MISC_ITEM_DESC";
  public static String COL_MISC_ITEM_ID = "TR_LTM_RTL_TRN.MISC_ITEM_ID";
  public static String COL_RETURN_COMMENTS = "TR_LTM_RTL_TRN.RETURN_COMMENTS";
  public static String COL_RETURN_REASON_ID = "TR_LTM_RTL_TRN.RETURN_REASON_ID";
  public static String COL_MANUAL_MD_REASON = "TR_LTM_RTL_TRN.MANUAL_MD_REASON";
  public static String COL_REG_TAX_EXEMPT_ID = "TR_LTM_RTL_TRN.REG_TAX_EXEMPT_ID";
  public static String COL_TAX_EXEMPT_ID = "TR_LTM_RTL_TRN.TAX_EXEMPT_ID";
  public static String COL_SHIP_REQ_SEQ_NUM = "TR_LTM_RTL_TRN.SHIP_REQ_SEQ_NUM";
  public static String COL_MISC_ITEM_COMMENT = "TR_LTM_RTL_TRN.MISC_ITEM_COMMENT";
  public static String COL_MISC_ITEM_GL_ACC = "TR_LTM_RTL_TRN.MISC_ITEM_GL_ACC";
  public static String COL_QUANTITY = "TR_LTM_RTL_TRN.QUANTITY";
  public static String COL_DOC_NUM = "TR_LTM_RTL_TRN.DOC_NUM";
  public static String COL_VAT_COMMENTS = "TR_LTM_RTL_TRN.VAT_COMMENTS";
  public static String COL_TY_DOC = "TR_LTM_RTL_TRN.TY_DOC";
  public static String COL_FL_EXCHANGE = "TR_LTM_RTL_TRN.FL_EXCHANGE";
  public static String COL_EXT_BARCODE = "TR_LTM_RTL_TRN.EXT_BARCODE";
  // Added by Rachana for approval of return transaction
  public static String COL_APPROVER_ID = "TR_LTM_RTL_TRN.APPROVER_ID";
  //Vivek Mishra : Added to save AJB sequence number in case of gift card activation and reload
  public static String COL_AJB_SEQ = "TR_LTM_RTL_TRN.AJB_SEQ";

  public String getSelectSql() { return selectSql; }
  public String getInsertSql() { return insertSql; }
  public String getUpdateSql() { return updateSql; }
  public String getDeleteSql() { return deleteSql; }

  private Long aiLnItm;
  private String tyLnItm;
  private Date tsLnItmBgn;
  private Date tsLnItmEnd;
  private Boolean flVdLnItm;
  private String aiTrn;
  private String idItm;
  private Long posLnItmTyId;
  private String addConsultantId;
  private ArmCurrency manualUnitPrice;
  private ArmCurrency manualMdAmt;
  private Boolean validNetAmtFl;
  private ArmCurrency itmSelPrice;
  private ArmCurrency itmRetailPrice;
  private Boolean miscItemRgTxbl;
  private Boolean miscItemTxbl;
  private String miscItemDesc;
  private String miscItemId;
  private String returnComments;
  private String returnReasonId;
  private String manualMdReason;
  private String regTaxExemptId;
  private String taxExemptId;
  private Long shipReqSeqNum;
  private String miscItemComment;
  private String miscItemGlAcc;
  private Long quantity;
  private String docNum;
  private String vatComments;
  private String tyDoc;
  private Boolean flExchange;
  private String extBarcode;
//Added by Rachana for approval of return transaction
  private String approverId;
//Vivek Mishra : Added to save AJB sequence number in case of gift card activation and reload
  private String ajbSeq;

  public Long getAiLnItm() { return this.aiLnItm; }
  public void setAiLnItm(Long aiLnItm) { this.aiLnItm = aiLnItm; }
  public void setAiLnItm(long aiLnItm) { this.aiLnItm = new Long(aiLnItm); }
  public void setAiLnItm(int aiLnItm) { this.aiLnItm = new Long((long)aiLnItm); }

  public String getTyLnItm() { return this.tyLnItm; }
  public void setTyLnItm(String tyLnItm) { this.tyLnItm = tyLnItm; }

  public Date getTsLnItmBgn() { return this.tsLnItmBgn; }
  public void setTsLnItmBgn(Date tsLnItmBgn) { this.tsLnItmBgn = tsLnItmBgn; }

  public Date getTsLnItmEnd() { return this.tsLnItmEnd; }
  public void setTsLnItmEnd(Date tsLnItmEnd) { this.tsLnItmEnd = tsLnItmEnd; }

  public Boolean getFlVdLnItm() { return this.flVdLnItm; }
  public void setFlVdLnItm(Boolean flVdLnItm) { this.flVdLnItm = flVdLnItm; }
  public void setFlVdLnItm(boolean flVdLnItm) { this.flVdLnItm = new Boolean(flVdLnItm); }

  public String getAiTrn() { return this.aiTrn; }
  public void setAiTrn(String aiTrn) { this.aiTrn = aiTrn; }

  public String getIdItm() { return this.idItm; }
  public void setIdItm(String idItm) { this.idItm = idItm; }

  public Long getPosLnItmTyId() { return this.posLnItmTyId; }
  public void setPosLnItmTyId(Long posLnItmTyId) { this.posLnItmTyId = posLnItmTyId; }
  public void setPosLnItmTyId(long posLnItmTyId) { this.posLnItmTyId = new Long(posLnItmTyId); }
  public void setPosLnItmTyId(int posLnItmTyId) { this.posLnItmTyId = new Long((long)posLnItmTyId); }

  public String getAddConsultantId() { return this.addConsultantId; }
  public void setAddConsultantId(String addConsultantId) { this.addConsultantId = addConsultantId; }

  public ArmCurrency getManualUnitPrice() { return this.manualUnitPrice; }
  public void setManualUnitPrice(ArmCurrency manualUnitPrice) { this.manualUnitPrice = manualUnitPrice; }

  public ArmCurrency getManualMdAmt() { return this.manualMdAmt; }
  public void setManualMdAmt(ArmCurrency manualMdAmt) { this.manualMdAmt = manualMdAmt; }

  public Boolean getValidNetAmtFl() { return this.validNetAmtFl; }
  public void setValidNetAmtFl(Boolean validNetAmtFl) { this.validNetAmtFl = validNetAmtFl; }
  public void setValidNetAmtFl(boolean validNetAmtFl) { this.validNetAmtFl = new Boolean(validNetAmtFl); }

  public ArmCurrency getItmSelPrice() { return this.itmSelPrice; }
  public void setItmSelPrice(ArmCurrency itmSelPrice) { this.itmSelPrice = itmSelPrice; }

  public ArmCurrency getItmRetailPrice() { return this.itmRetailPrice; }
  public void setItmRetailPrice(ArmCurrency itmRetailPrice) { this.itmRetailPrice = itmRetailPrice; }

  public Boolean getMiscItemRgTxbl() { return this.miscItemRgTxbl; }
  public void setMiscItemRgTxbl(Boolean miscItemRgTxbl) { this.miscItemRgTxbl = miscItemRgTxbl; }
  public void setMiscItemRgTxbl(boolean miscItemRgTxbl) { this.miscItemRgTxbl = new Boolean(miscItemRgTxbl); }

  public Boolean getMiscItemTxbl() { return this.miscItemTxbl; }
  public void setMiscItemTxbl(Boolean miscItemTxbl) { this.miscItemTxbl = miscItemTxbl; }
  public void setMiscItemTxbl(boolean miscItemTxbl) { this.miscItemTxbl = new Boolean(miscItemTxbl); }

  public String getMiscItemDesc() { return this.miscItemDesc; }
  public void setMiscItemDesc(String miscItemDesc) { this.miscItemDesc = miscItemDesc; }

  public String getMiscItemId() { return this.miscItemId; }
  public void setMiscItemId(String miscItemId) { this.miscItemId = miscItemId; }

  public String getReturnComments() { return this.returnComments; }
  public void setReturnComments(String returnComments) { this.returnComments = returnComments; }

  public String getReturnReasonId() { return this.returnReasonId; }
  public void setReturnReasonId(String returnReasonId) { this.returnReasonId = returnReasonId; }

  public String getManualMdReason() { return this.manualMdReason; }
  public void setManualMdReason(String manualMdReason) { this.manualMdReason = manualMdReason; }

  public String getRegTaxExemptId() { return this.regTaxExemptId; }
  public void setRegTaxExemptId(String regTaxExemptId) { this.regTaxExemptId = regTaxExemptId; }

  public String getTaxExemptId() { return this.taxExemptId; }
  public void setTaxExemptId(String taxExemptId) { this.taxExemptId = taxExemptId; }

  public Long getShipReqSeqNum() { return this.shipReqSeqNum; }
  public void setShipReqSeqNum(Long shipReqSeqNum) { this.shipReqSeqNum = shipReqSeqNum; }
  public void setShipReqSeqNum(long shipReqSeqNum) { this.shipReqSeqNum = new Long(shipReqSeqNum); }
  public void setShipReqSeqNum(int shipReqSeqNum) { this.shipReqSeqNum = new Long((long)shipReqSeqNum); }

  public String getMiscItemComment() { return this.miscItemComment; }
  public void setMiscItemComment(String miscItemComment) { this.miscItemComment = miscItemComment; }

  public String getMiscItemGlAcc() { return this.miscItemGlAcc; }
  public void setMiscItemGlAcc(String miscItemGlAcc) { this.miscItemGlAcc = miscItemGlAcc; }

  public Long getQuantity() { return this.quantity; }
  public void setQuantity(Long quantity) { this.quantity = quantity; }
  public void setQuantity(long quantity) { this.quantity = new Long(quantity); }
  public void setQuantity(int quantity) { this.quantity = new Long((long)quantity); }

  public String getDocNum() { return this.docNum; }
  public void setDocNum(String docNum) { this.docNum = docNum; }

  public String getVatComments() { return this.vatComments; }
  public void setVatComments(String vatComments) { this.vatComments = vatComments; }

  public String getTyDoc() { return this.tyDoc; }
  public void setTyDoc(String tyDoc) { this.tyDoc = tyDoc; }

  public Boolean getFlExchange() { return this.flExchange; }
  public void setFlExchange(Boolean flExchange) { this.flExchange = flExchange; }
  public void setFlExchange(boolean flExchange) { this.flExchange = new Boolean(flExchange); }
  
  public String getExtBarcode() { return this.extBarcode; }
  public void setExtBarcode(String extBarcode) { this.extBarcode = extBarcode; }
  
  //Added by Rachana for approval of return transaction
  public String getApproverId() { return this.approverId; }
  public void setApproverId(String approverId) { this.approverId = approverId; }
  
  //Vivek Mishra : Added to save AJB sequence number in case of gift card activation and reload
  public String getAjbSeq() { return this.ajbSeq; }
  public void setAjbSeq(String ajbSeq) { this.ajbSeq = ajbSeq; }
  
public BaseOracleBean[] getDatabeans(ResultSet rs) throws SQLException {
    ArrayList list = new ArrayList();
    while(rs.next()) {
      TrLtmRtlTrnOracleBean bean = new TrLtmRtlTrnOracleBean();
      bean.aiLnItm = getLongFromResultSet(rs, "AI_LN_ITM");
      bean.tyLnItm = getStringFromResultSet(rs, "TY_LN_ITM");
      bean.tsLnItmBgn = getDateFromResultSet(rs, "TS_LN_ITM_BGN");
      bean.tsLnItmEnd = getDateFromResultSet(rs, "TS_LN_ITM_END");
      bean.flVdLnItm = getBooleanFromResultSet(rs, "FL_VD_LN_ITM");
      bean.aiTrn = getStringFromResultSet(rs, "AI_TRN");
      bean.idItm = getStringFromResultSet(rs, "ID_ITM");
      bean.posLnItmTyId = getLongFromResultSet(rs, "POS_LN_ITM_TY_ID");
      bean.addConsultantId = getStringFromResultSet(rs, "ADD_CONSULTANT_ID");
      bean.manualUnitPrice = getCurrencyFromResultSet(rs, "MANUAL_UNIT_PRICE");
      bean.manualMdAmt = getCurrencyFromResultSet(rs, "MANUAL_MD_AMT");
      bean.validNetAmtFl = getBooleanFromResultSet(rs, "VALID_NET_AMT_FL");
      bean.itmSelPrice = getCurrencyFromResultSet(rs, "ITM_SEL_PRICE");
      bean.itmRetailPrice = getCurrencyFromResultSet(rs, "ITM_RETAIL_PRICE");
      bean.miscItemRgTxbl = getBooleanFromResultSet(rs, "MISC_ITEM_RG_TXBL");
      bean.miscItemTxbl = getBooleanFromResultSet(rs, "MISC_ITEM_TXBL");
      bean.miscItemDesc = getStringFromResultSet(rs, "MISC_ITEM_DESC");
      bean.miscItemId = getStringFromResultSet(rs, "MISC_ITEM_ID");
      bean.returnComments = getStringFromResultSet(rs, "RETURN_COMMENTS");
      bean.returnReasonId = getStringFromResultSet(rs, "RETURN_REASON_ID");
      bean.manualMdReason = getStringFromResultSet(rs, "MANUAL_MD_REASON");
      bean.regTaxExemptId = getStringFromResultSet(rs, "REG_TAX_EXEMPT_ID");
      bean.taxExemptId = getStringFromResultSet(rs, "TAX_EXEMPT_ID");
      bean.shipReqSeqNum = getLongFromResultSet(rs, "SHIP_REQ_SEQ_NUM");
      bean.miscItemComment = getStringFromResultSet(rs, "MISC_ITEM_COMMENT");
      bean.miscItemGlAcc = getStringFromResultSet(rs, "MISC_ITEM_GL_ACC");
      bean.quantity = getLongFromResultSet(rs, "QUANTITY");
      bean.docNum = getStringFromResultSet(rs, "DOC_NUM");
      bean.vatComments = getStringFromResultSet(rs, "VAT_COMMENTS");
      bean.tyDoc = getStringFromResultSet(rs, "TY_DOC");
      bean.flExchange = getBooleanFromResultSet(rs, "FL_EXCHANGE");
      bean.extBarcode = getStringFromResultSet(rs, "EXT_BARCODE");
      //Added by Rachana for approval of return transaction
      bean.approverId = getStringFromResultSet(rs, "APPROVER_ID");
    //Vivek Mishra : Added to fatch AJB sequence number in case of gift card activation and reload
      bean.ajbSeq = getStringFromResultSet(rs, "AJB_SEQ");
      list.add(bean);
    }
    return (TrLtmRtlTrnOracleBean[]) list.toArray(new TrLtmRtlTrnOracleBean[0]);
  }

  public List toList() {
    List list = new ArrayList();
    addToList(list, this.getAiLnItm(), Types.DECIMAL);
    addToList(list, this.getTyLnItm(), Types.VARCHAR);
    addToList(list, this.getTsLnItmBgn(), Types.TIMESTAMP);
    addToList(list, this.getTsLnItmEnd(), Types.TIMESTAMP);
    addToList(list, this.getFlVdLnItm(), Types.DECIMAL);
    addToList(list, this.getAiTrn(), Types.VARCHAR);
    addToList(list, this.getIdItm(), Types.VARCHAR);
    addToList(list, this.getPosLnItmTyId(), Types.DECIMAL);
    addToList(list, this.getAddConsultantId(), Types.VARCHAR);
    addToList(list, this.getManualUnitPrice(), Types.VARCHAR);
    addToList(list, this.getManualMdAmt(), Types.VARCHAR);
    addToList(list, this.getValidNetAmtFl(), Types.DECIMAL);
    addToList(list, this.getItmSelPrice(), Types.VARCHAR);
    addToList(list, this.getItmRetailPrice(), Types.VARCHAR);
    addToList(list, this.getMiscItemRgTxbl(), Types.DECIMAL);
    addToList(list, this.getMiscItemTxbl(), Types.DECIMAL);
    addToList(list, this.getMiscItemDesc(), Types.VARCHAR);
    addToList(list, this.getMiscItemId(), Types.VARCHAR);
    addToList(list, this.getReturnComments(), Types.VARCHAR);
    addToList(list, this.getReturnReasonId(), Types.VARCHAR);
    addToList(list, this.getManualMdReason(), Types.VARCHAR);
    addToList(list, this.getRegTaxExemptId(), Types.VARCHAR);
    addToList(list, this.getTaxExemptId(), Types.VARCHAR);
    addToList(list, this.getShipReqSeqNum(), Types.DECIMAL);
    addToList(list, this.getMiscItemComment(), Types.VARCHAR);
    addToList(list, this.getMiscItemGlAcc(), Types.VARCHAR);
    addToList(list, this.getQuantity(), Types.DECIMAL);
    addToList(list, this.getDocNum(), Types.VARCHAR);
    addToList(list, this.getVatComments(), Types.VARCHAR);
    addToList(list, this.getTyDoc(), Types.VARCHAR);
    addToList(list, this.getFlExchange(), Types.DECIMAL);
    addToList(list, this.getExtBarcode(), Types.VARCHAR);
    //Added by Rachana for approval of return transaction
    addToList(list, this.getApproverId(), Types.VARCHAR);
    //Vivek Mishra : Added to save AJB sequence number in case of gift card activation and reload
    addToList(list, this.getAjbSeq(), Types.VARCHAR);
    return list;
  }

}
