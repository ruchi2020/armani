package com.chelseasystems.cs.dataaccess.artsoracle.databean;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class TrLtmChkTndOracleBean extends BaseOracleBean{

	public static String selectSql = "select AI_TRN, AI_LN_ITM, ID_BK_CHK, ID_ACNT_CHK, AI_CHK, TY_ID_PRSL_RQ, ID_PRSL_AZN, ID_ADJN_CHK, DC_BRT_CHK, LU_CLS_TND, TRANSIT_NUMBER, MICR_DATA, TENDER_TYPE, MSG_TYPE, MSG_IDENTIFIER, EMPLOYEE_ID, MANUAL_OVERRIDE, AUTH_REQUIRED, RSP_AUTH_RESP_CODE, RSP_AUTH_CODE, RSP_STAT_CODE_DESC, RSP_STAT_CODE, RSP_HOST_ACT_CODE, APPROVAL_DATE, IS_ID_SCANNED_IN, DRVR_LICENSE_NUM, STATE_ID_CODE, ACTION_DATE, CHECK_SCANNED_IN, RESP_ID, LU_NMB_CHK_SWP_KY from TR_LTM_CHK_TND ";
	  public static String insertSql = "insert into TR_LTM_CHK_TND (AI_TRN, AI_LN_ITM, ID_BK_CHK, ID_ACNT_CHK, AI_CHK, TY_ID_PRSL_RQ, ID_PRSL_AZN, ID_ADJN_CHK, DC_BRT_CHK, LU_CLS_TND, TRANSIT_NUMBER, MICR_DATA, TENDER_TYPE, MSG_TYPE, MSG_IDENTIFIER, EMPLOYEE_ID, MANUAL_OVERRIDE, AUTH_REQUIRED, RSP_AUTH_RESP_CODE, RSP_AUTH_CODE, RSP_STAT_CODE_DESC, RSP_STAT_CODE, RSP_HOST_ACT_CODE, APPROVAL_DATE, IS_ID_SCANNED_IN, DRVR_LICENSE_NUM, STATE_ID_CODE, ACTION_DATE, CHECK_SCANNED_IN, RESP_ID, LU_NMB_CHK_SWP_KY) values (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
	  public static String updateSql = "update TR_LTM_CHK_TND set AI_TRN = ?, AI_LN_ITM = ?, ID_BK_CHK = ?, ID_ACNT_CHK = ?, AI_CHK = ?, TY_ID_PRSL_RQ = ?, ID_PRSL_AZN = ?, ID_ADJN_CHK = ?, DC_BRT_CHK = ?, LU_CLS_TND = ?, TRANSIT_NUMBER = ?, MICR_DATA = ?, TENDER_TYPE = ?, MSG_TYPE = ?, MSG_IDENTIFIER = ?, EMPLOYEE_ID = ?, MANUAL_OVERRIDE = ?, AUTH_REQUIRED = ?, RSP_AUTH_RESP_CODE = ?, RSP_AUTH_CODE = ?, RSP_STAT_CODE_DESC = ?, RSP_STAT_CODE = ?, RSP_HOST_ACT_CODE = ?, APPROVAL_DATE = ?, IS_ID_SCANNED_IN = ?, DRVR_LICENSE_NUM = ?, STATE_ID_CODE = ?, ACTION_DATE = ?, CHECK_SCANNED_IN = ?, RESP_ID = ?, LU_NMB_CHK_SWP_KY = ?";
	  public static String deleteSql = "delete from TR_LTM_CHK_TND ";

	  public static String TABLE_NAME = "TR_LTM_CHK_TND";
	  public static String COL_AI_TRN = "TR_LTM_CHK_TND.AI_TRN";
	  public static String COL_AI_LN_ITM = "TR_LTM_CHK_TND.AI_LN_ITM";
	  public static String COL_ID_BK_CHK = "TR_LTM_CHK_TND.ID_BK_CHK";
	  public static String COL_ID_ACNT_CHK = "TR_LTM_CHK_TND.ID_ACNT_CHK";
	  public static String COL_AI_CHK = "TR_LTM_CHK_TND.AI_CHK";
	  public static String COL_TY_ID_PRSL_RQ = "TR_LTM_CHK_TND.TY_ID_PRSL_RQ";
	  public static String COL_ID_PRSL_AZN = "TR_LTM_CHK_TND.ID_PRSL_AZN";
	  public static String COL_ID_ADJN_CHK = "TR_LTM_CHK_TND.ID_ADJN_CHK";
	  public static String COL_DC_BRT_CHK = "TR_LTM_CHK_TND.DC_BRT_CHK";
	  public static String COL_LU_CLS_TND = "TR_LTM_CHK_TND.LU_CLS_TND";
	  public static String COL_TRANSIT_NUMBER = "TR_LTM_CHK_TND.TRANSIT_NUMBER";
	  public static String COL_MICR_DATA = "TR_LTM_CHK_TND.MICR_DATA";
	  public static String COL_TENDER_TYPE = "TR_LTM_CHK_TND.TENDER_TYPE";
	  public static String COL_MSG_TYPE = "TR_LTM_CHK_TND.MSG_TYPE";
	  public static String COL_MSG_IDENTIFIER = "TR_LTM_CHK_TND.MSG_IDENTIFIER";
	  public static String COL_EMPLOYEE_ID = "TR_LTM_CHK_TND.EMPLOYEE_ID";
	  public static String COL_MANUAL_OVERRIDE = "TR_LTM_CHK_TND.MANUAL_OVERRIDE";
	  public static String COL_AUTH_REQUIRED = "TR_LTM_CHK_TND.AUTH_REQUIRED";
	  public static String COL_RSP_AUTH_RESP_CODE = "TR_LTM_CHK_TND.RSP_AUTH_RESP_CODE";
	  public static String COL_RSP_AUTH_CODE = "TR_LTM_CHK_TND.RSP_AUTH_CODE";
	  public static String COL_RSP_STAT_CODE_DESC = "TR_LTM_CHK_TND.RSP_STAT_CODE_DESC";
	  public static String COL_RSP_STAT_CODE = "TR_LTM_CHK_TND.RSP_STAT_CODE";
	  public static String COL_RSP_HOST_ACT_CODE = "TR_LTM_CHK_TND.RSP_HOST_ACT_CODE";
	  public static String COL_APPROVAL_DATE = "TR_LTM_CHK_TND.APPROVAL_DATE";
	  public static String COL_IS_ID_SCANNED_IN = "TR_LTM_CHK_TND.IS_ID_SCANNED_IN";
	  public static String COL_DRVR_LICENSE_NUM = "TR_LTM_CHK_TND.DRVR_LICENSE_NUM";
	  public static String COL_STATE_ID_CODE = "TR_LTM_CHK_TND.STATE_ID_CODE";
	  public static String COL_ACTION_DATE = "TR_LTM_CHK_TND.ACTION_DATE";
	  public static String COL_CHECK_SCANNED_IN = "TR_LTM_CHK_TND.CHECK_SCANNED_IN";
	  //public static String COL_AJB_SEQ = "TR_LTM_CHK_TND.AJB_SEQ";
	  //Vivek Mishra : Changed AJB_SEQ to RESP_ID
	  public static String COL_RESP_ID = "TR_LTM_CHK_TND.RESP_ID";
	  //Vivek Mishra : Added to capture check entry mode.
	  public static String COL_LU_NMB_CHK_SWP_KY = "TR_LTM_CHK_TND.LU_NMB_CHK_SWP_KY";
	  private String aiTrn;
	  private Long aiLnItm;
	  private String idBkChk;
	  private String idAcntChk;
	  private String aiChk;
	  private String tyIdPrslRq;
	  private String idPrslAzn;
	  private String idAdjnChk;
	  private String dcBrtChk;
	  private String luClsTnd;
	  private String transitNumber;
	  private String micrData;
	  private String tenderType;
	  private String msgType;
	  private String msgIdentifier;
	  private String employeeId;
	  private Boolean manualOverride;
	  private Boolean authRequired;
	  private String rspAuthRespCode;
	  private String rspAuthCode;
	  private String rspStatCodeDesc;
	  private String rspStatCode;
	  private String rspHostActCode;
	  private Date approvalDate;
	  private Boolean isIdScannedIn;
	  private String drvrLicenseNum;
	  private String stateIdCode;
	  private String actionDate;
	  private Boolean checkScannedIn;
	//Vivek Mishra : Added for AJB unique sequence persistence and retrieval
		//private String ajbSeq;
	  private String respId;
		//Ends
	  private String luNmbChkSwpKy;

	  public String getSelectSql()
	  {
	    return selectSql; } 
	  public String getInsertSql() { return insertSql; } 
	  public String getUpdateSql() { return updateSql; } 
	  public String getDeleteSql() { return deleteSql;
	  }

	  public String getAiTrn()
	  {
	    return this.aiTrn; } 
	  public void setAiTrn(String aiTrn) { this.aiTrn = aiTrn; } 
	  public Long getAiLnItm() {
	    return this.aiLnItm; } 
	  public void setAiLnItm(Long aiLnItm) { this.aiLnItm = aiLnItm; } 
	  public void setAiLnItm(long aiLnItm) { this.aiLnItm = new Long(aiLnItm); } 
	  public void setAiLnItm(int aiLnItm) { this.aiLnItm = new Long(aiLnItm); } 
	  public String getIdBkChk() {
	    return this.idBkChk; } 
	  public void setIdBkChk(String idBkChk) { this.idBkChk = idBkChk; } 
	  public String getIdAcntChk() {
	    return this.idAcntChk; } 
	  public void setIdAcntChk(String idAcntChk) { this.idAcntChk = idAcntChk; } 
	  public String getAiChk() {
	    return this.aiChk; } 
	  public void setAiChk(String aiChk) { this.aiChk = aiChk; } 
	  public String getTyIdPrslRq() {
	    return this.tyIdPrslRq; } 
	  public void setTyIdPrslRq(String tyIdPrslRq) { this.tyIdPrslRq = tyIdPrslRq; } 
	  public String getIdPrslAzn() {
	    return this.idPrslAzn; } 
	  public void setIdPrslAzn(String idPrslAzn) { this.idPrslAzn = idPrslAzn; } 
	  public String getIdAdjnChk() {
	    return this.idAdjnChk; } 
	  public void setIdAdjnChk(String idAdjnChk) { this.idAdjnChk = idAdjnChk; } 
	  public String getDcBrtChk() {
	    return this.dcBrtChk; } 
	  public void setDcBrtChk(String dcBrtChk) { this.dcBrtChk = dcBrtChk; } 
	  public String getLuClsTnd() {
	    return this.luClsTnd; } 
	  public void setLuClsTnd(String luClsTnd) { this.luClsTnd = luClsTnd; } 
	  public String getTransitNumber() {
	    return this.transitNumber; } 
	  public void setTransitNumber(String transitNumber) { this.transitNumber = transitNumber; } 
	  public String getMicrData() {
	    return this.micrData; } 
	  public void setMicrData(String micrData) { this.micrData = micrData; } 
	  public String getTenderType() {
	    return this.tenderType; } 
	  public void setTenderType(String tenderType) { this.tenderType = tenderType; } 
	  public String getMsgType() {
	    return this.msgType; } 
	  public void setMsgType(String msgType) { this.msgType = msgType; } 
	  public String getMsgIdentifier() {
	    return this.msgIdentifier; } 
	  public void setMsgIdentifier(String msgIdentifier) { this.msgIdentifier = msgIdentifier; } 
	  public String getEmployeeId() {
	    return this.employeeId; } 
	  public void setEmployeeId(String employeeId) { this.employeeId = employeeId; } 
	  public Boolean getManualOverride() {
	    return this.manualOverride; } 
	  public void setManualOverride(Boolean manualOverride) { this.manualOverride = manualOverride; } 
	  public void setManualOverride(boolean manualOverride) { this.manualOverride = new Boolean(manualOverride); } 
	  public Boolean getAuthRequired() {
	    return this.authRequired; } 
	  public void setAuthRequired(Boolean authRequired) { this.authRequired = authRequired; } 
	  public void setAuthRequired(boolean authRequired) { this.authRequired = new Boolean(authRequired); } 
	  public String getRspAuthRespCode() {
	    return this.rspAuthRespCode; } 
	  public void setRspAuthRespCode(String rspAuthRespCode) { this.rspAuthRespCode = rspAuthRespCode; } 
	  public String getRspAuthCode() {
	    return this.rspAuthCode; } 
	  public void setRspAuthCode(String rspAuthCode) { this.rspAuthCode = rspAuthCode; } 
	  public String getRspStatCodeDesc() {
	    return this.rspStatCodeDesc; } 
	  public void setRspStatCodeDesc(String rspStatCodeDesc) { this.rspStatCodeDesc = rspStatCodeDesc; } 
	  public String getRspStatCode() {
	    return this.rspStatCode; } 
	  public void setRspStatCode(String rspStatCode) { this.rspStatCode = rspStatCode; } 
	  public String getRspHostActCode() {
	    return this.rspHostActCode; } 
	  public void setRspHostActCode(String rspHostActCode) { this.rspHostActCode = rspHostActCode; } 
	  public Date getApprovalDate() {
	    return this.approvalDate; } 
	  public void setApprovalDate(Date approvalDate) { this.approvalDate = approvalDate; } 
	  public Boolean getIsIdScannedIn() {
	    return this.isIdScannedIn; } 
	  public void setIsIdScannedIn(Boolean isIdScannedIn) { this.isIdScannedIn = isIdScannedIn; } 
	  public void setIsIdScannedIn(boolean isIdScannedIn) { this.isIdScannedIn = new Boolean(isIdScannedIn); } 
	  public String getDrvrLicenseNum() {
	    return this.drvrLicenseNum; } 
	  public void setDrvrLicenseNum(String drvrLicenseNum) { this.drvrLicenseNum = drvrLicenseNum; } 
	  public String getStateIdCode() {
	    return this.stateIdCode; } 
	  public void setStateIdCode(String stateIdCode) { this.stateIdCode = stateIdCode; } 
	  public String getActionDate() {
	    return this.actionDate; } 
	  public void setActionDate(String actionDate) { this.actionDate = actionDate; } 
	  public Boolean getCheckScannedIn() {
	    return this.checkScannedIn; } 
	  public void setCheckScannedIn(Boolean checkScannedIn) { this.checkScannedIn = checkScannedIn; } 
	  public void setCheckScannedIn(boolean checkScannedIn) { this.checkScannedIn = new Boolean(checkScannedIn); }
	//Vivek Mishra : Added for AJB unique sequence persistence and retrieval
	  public String getRespId() {
		    return this.respId; } 
		  public void setRespId(String respId) { this.respId = respId; } 
		//Ends
	  public String getLuNmbChkSwpKy() {
				return luNmbChkSwpKy;
			}
	  public void setLuNmbChkSwpKy(String luNmbChkSwpKy) {
				this.luNmbChkSwpKy = luNmbChkSwpKy;
			}
	  
	  public BaseOracleBean[] getDatabeans(ResultSet rs) throws SQLException {
	    ArrayList list = new ArrayList();
	    while (rs.next()) {
	      TrLtmChkTndOracleBean bean = new TrLtmChkTndOracleBean();
	      bean.aiTrn = getStringFromResultSet(rs, "AI_TRN");
	      bean.aiLnItm = getLongFromResultSet(rs, "AI_LN_ITM");
	      bean.idBkChk = getStringFromResultSet(rs, "ID_BK_CHK");
	      bean.idAcntChk = getStringFromResultSet(rs, "ID_ACNT_CHK");
	      bean.aiChk = getStringFromResultSet(rs, "AI_CHK");
	      bean.tyIdPrslRq = getStringFromResultSet(rs, "TY_ID_PRSL_RQ");
	      bean.idPrslAzn = getStringFromResultSet(rs, "ID_PRSL_AZN");
	      bean.idAdjnChk = getStringFromResultSet(rs, "ID_ADJN_CHK");
	      bean.dcBrtChk = getStringFromResultSet(rs, "DC_BRT_CHK");
	      bean.luClsTnd = getStringFromResultSet(rs, "LU_CLS_TND");
	      bean.transitNumber = getStringFromResultSet(rs, "TRANSIT_NUMBER");
	      bean.micrData = getStringFromResultSet(rs, "MICR_DATA");
	      bean.tenderType = getStringFromResultSet(rs, "TENDER_TYPE");
	      bean.msgType = getStringFromResultSet(rs, "MSG_TYPE");
	      bean.msgIdentifier = getStringFromResultSet(rs, "MSG_IDENTIFIER");
	      bean.employeeId = getStringFromResultSet(rs, "EMPLOYEE_ID");
	      bean.manualOverride = getBooleanFromResultSet(rs, "MANUAL_OVERRIDE");
	      bean.authRequired = getBooleanFromResultSet(rs, "AUTH_REQUIRED");
	      bean.rspAuthRespCode = getStringFromResultSet(rs, "RSP_AUTH_RESP_CODE");
	      bean.rspAuthCode = getStringFromResultSet(rs, "RSP_AUTH_CODE");
	      bean.rspStatCodeDesc = getStringFromResultSet(rs, "RSP_STAT_CODE_DESC");
	      bean.rspStatCode = getStringFromResultSet(rs, "RSP_STAT_CODE");
	      bean.rspHostActCode = getStringFromResultSet(rs, "RSP_HOST_ACT_CODE");
	      bean.approvalDate = getDateFromResultSet(rs, "APPROVAL_DATE");
	      bean.isIdScannedIn = getBooleanFromResultSet(rs, "IS_ID_SCANNED_IN");
	      bean.drvrLicenseNum = getStringFromResultSet(rs, "DRVR_LICENSE_NUM");
	      bean.stateIdCode = getStringFromResultSet(rs, "STATE_ID_CODE");
	      bean.actionDate = getStringFromResultSet(rs, "ACTION_DATE");
	      bean.checkScannedIn = getBooleanFromResultSet(rs, "CHECK_SCANNED_IN");
	      //Vivek Mishra : Added for AJB unique sequence persistence and retrieval
		  //bean.ajbSeq = getStringFromResultSet(rs, "AJB_SEQ");
	      bean.respId = getStringFromResultSet(rs, "RESP_ID");
		  //Ends
	      bean.luNmbChkSwpKy = getStringFromResultSet(rs, "LU_NMB_CHK_SWP_KY");
	      list.add(bean);
	    }
	    return (TrLtmChkTndOracleBean[])(TrLtmChkTndOracleBean[])list.toArray(new TrLtmChkTndOracleBean[0]);
	  }

	public List toList() {
	    List list = new ArrayList();
	    addToList(list, getAiTrn(), Types.VARCHAR);
	    addToList(list, getAiLnItm(), Types.DECIMAL);
	    addToList(list, getIdBkChk(), Types.VARCHAR);
	    addToList(list, getIdAcntChk(), Types.VARCHAR);
	    addToList(list, getAiChk(), Types.VARCHAR);
	    addToList(list, getTyIdPrslRq(), Types.VARCHAR);
	    addToList(list, getIdPrslAzn(), Types.VARCHAR);
	    addToList(list, getIdAdjnChk(), Types.VARCHAR);
	    addToList(list, getDcBrtChk(), Types.VARCHAR);
	    addToList(list, getLuClsTnd(), Types.VARCHAR);
	    addToList(list, getTransitNumber(), Types.VARCHAR);
	    addToList(list, getMicrData(), Types.VARCHAR);
	    addToList(list, getTenderType(), Types.VARCHAR);
	    addToList(list, getMsgType(), Types.VARCHAR);
	    addToList(list, getMsgIdentifier(), Types.VARCHAR);
	    addToList(list, getEmployeeId(), Types.VARCHAR);
	    addToList(list, getManualOverride(), Types.DECIMAL);
	    addToList(list, getAuthRequired(), Types.DECIMAL);
	    addToList(list, getRspAuthRespCode(), Types.VARCHAR);
	    addToList(list, getRspAuthCode(), Types.VARCHAR);
	    addToList(list, getRspStatCodeDesc(), Types.VARCHAR);
	    addToList(list, getRspStatCode(), Types.VARCHAR);
	    addToList(list, getRspHostActCode(), Types.VARCHAR);
	    addToList(list, getApprovalDate(), Types.TIMESTAMP);
	    addToList(list, getIsIdScannedIn(), Types.DECIMAL);
	    addToList(list, getDrvrLicenseNum(), Types.VARCHAR);
	    addToList(list, getStateIdCode(), Types.VARCHAR);
	    addToList(list, getActionDate(), Types.VARCHAR);
	    addToList(list, getCheckScannedIn(), Types.DECIMAL);
	    //addToList(list, this.getAjbSeq(), Types.VARCHAR);
	    addToList(list, getRespId(), Types.VARCHAR);
	    addToList(list, getLuNmbChkSwpKy(), Types.VARCHAR);
	    return list;
	  }
}
