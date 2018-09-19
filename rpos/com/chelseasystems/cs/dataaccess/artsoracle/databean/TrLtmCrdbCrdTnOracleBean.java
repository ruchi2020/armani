//
// Copyright 2002, Retek Inc. All Rights Reserved.
//
package com.chelseasystems.cs.dataaccess.artsoracle.databean;

import java.util.Date;
import java.util.Calendar;
import java.util.List;
import java.util.ArrayList;
import java.awt.Image;
import java.sql.SQLException;
import java.sql.ResultSet;
import java.sql.Types;

import javax.swing.ImageIcon;

import com.chelseasystems.cr.currency.ArmCurrency;
import com.chelseasystems.cs.util.CreditAuthUtil;
import com.chelseasystems.cs.util.EncryptionUtils;

/**
 * 
 * This class is an object representation of the Arts database table
 * TR_LTM_CRDB_CRD_TN<BR>
 * Followings are the column of the table: <BR>
 * AI_TRN(PK) -- VARCHAR2(128)<BR>
 * AI_LN_ITM(PK) -- SMALLINT(5)<BR>
 * ID_ISSR_TND_MD -- VARCHAR2(128)<BR>
 * ID_ACNT_DB_CR_CRD -- VARCHAR2(40)<BR>
 * TY_ID_PRSL_RQ -- VARCHAR2(20)<BR>
 * ID_RFC_PRSL_ID -- VARCHAR2(128)<BR>
 * LU_MTH_AZN -- VARCHAR2(40)<BR>
 * DE_TRCK_2_BT_MP -- VARCHAR2(200)<BR>
 * LU_ADJN_CR_DB -- VARCHAR2(60)<BR>
 * LU_NMB_CRD_SWP_KY -- VARCHAR2(20)<BR>
 * CH_IM_TND_AZN_CT -- VARCHAR2(20)<BR>
 * LU_CLS_TND -- VARCHAR2(40)<BR>
 * APPROVAL_DATE -- DATE(7)<BR>
 * RESP_ID -- VARCHAR2(100)<BR>
 * TRACK_DATA -- VARCHAR2(200)<BR>
 * TRACK_NUMBER -- VARCHAR2(100)<BR>
 * EXPIRATION_DATE -- DATE(7)<BR>
 * TENDER_TYPE -- VARCHAR2(100)<BR>
 * MESSAGE_TYPE -- VARCHAR2(100)<BR>
 * MESSAGE_IDENTIFIER -- VARCHAR2(100)<BR>
 * RESP_STATUS_DESC -- VARCHAR2(100)<BR>
 * RESP_STATUS_CODE -- VARCHAR2(100)<BR>
 * RESP_HOST_ACTION -- VARCHAR2(100)<BR>
 * AMEX_CID_NUMBER -- VARCHAR2(50)<BR>
 * MANUAL_OVERRIDE -- NUMBER(1)<BR>
 * AUTH_REQUIRED -- NUMBER(1)<BR>
 * COMPANY_NAME -- VARCHAR2(100)<BR>
 * HOLDER_NAME -- VARCHAR2(100)<BR>
 * RESP_VALID_CODE -- VARCHAR2(100)<BR>
 * RESP_TRANS_IDFR -- VARCHAR2(100)<BR>
 * RESP_PAY_SERVICE -- VARCHAR2(100)<BR>
 * RESP_ADDRESS_VERIF -- VARCHAR2(100)<BR>
 * RESP_AUTH_SOURCE -- VARCHAR2(100)<BR>
 * RESP_POS_ENTRY_MOD -- VARCHAR2(100)<BR>
 * RESP_AUTH_RESPONSE -- VARCHAR2(100)<BR>
 * RESP_AUTHORIZATION -- VARCHAR2(100)<BR>
 * CARD_IDENTIFIER -- VARCHAR2(2)<BR>
 * PLAN_CODE -- VARCHAR2(50)<BR>
 * ID_EN_ACNT_DB_CR_CRD -- VARCHAR2(500) 
 * KY_ID_EN_ACNT_DB_CR_CRD -- NUMBER(9)     
 * LU_ACNT_CRD_TKN -- VARCHAR2(100) 
 * ID_MSK_ACNT_CRD -- VARCHAR2(20)  
 * CD_CNY -- VARCHAR2(3)   
 * TRN_RCPT_DATA -- BLOB          
 * CH_IM_TND_AZN_CT -- BLOB
 * CH_IMG_TND_AZN_CT -- BLOB
 */
public class TrLtmCrdbCrdTnOracleBean extends BaseOracleBean {

	public TrLtmCrdbCrdTnOracleBean() {
	}

	public static String selectSql = "select AI_TRN, AI_LN_ITM, ID_ISSR_TND_MD, ID_ACNT_DB_CR_CRD, TY_ID_PRSL_RQ, ID_RFC_PRSL_ID, LU_MTH_AZN, DE_TRCK_2_BT_MP, LU_ADJN_CR_DB, LU_NMB_CRD_SWP_KY, LU_CLS_TND, APPROVAL_DATE, TRACK_DATA, TRACK_NUMBER, EXPIRATION_DATE, TENDER_TYPE, MESSAGE_TYPE, MESSAGE_IDENTIFIER, RESP_STATUS_DESC, RESP_STATUS_CODE, RESP_HOST_ACTION, AMEX_CID_NUMBER, MANUAL_OVERRIDE, AUTH_REQUIRED, COMPANY_NAME, HOLDER_NAME, RESP_VALID_CODE, RESP_TRANS_IDFR, RESP_PAY_SERVICE, RESP_ADDRESS_VERIF, RESP_AUTH_SOURCE, RESP_POS_ENTRY_MOD, RESP_AUTH_RESPONSE, RESP_AUTHORIZATION, CARD_IDENTIFIER, PLAN_CODE, CO_CNY_CONV_RATE, ID_MSK_ACNT_CRD, LU_ACNT_CRD_TKN, CD_CNY, TRN_RCPT_DATA, CH_IM_TND_AZN_CT, RESP_ID, CH_IMG_TND_AZN_CT , EMV_TAG_AID, EMV_TAG_TVR, EMV_TAG_IAD, EMV_TAG_TSI, EMV_TAG_ARC, EMV_TAG_CVM from TR_LTM_CRDB_CRD_TN ";
	public static String insertSql = "insert into TR_LTM_CRDB_CRD_TN (AI_TRN, AI_LN_ITM, ID_ISSR_TND_MD, ID_ACNT_DB_CR_CRD, TY_ID_PRSL_RQ, ID_RFC_PRSL_ID, LU_MTH_AZN, DE_TRCK_2_BT_MP, LU_ADJN_CR_DB, LU_NMB_CRD_SWP_KY, LU_CLS_TND, APPROVAL_DATE, TRACK_DATA, TRACK_NUMBER, EXPIRATION_DATE, TENDER_TYPE, MESSAGE_TYPE, MESSAGE_IDENTIFIER, RESP_STATUS_DESC, RESP_STATUS_CODE, RESP_HOST_ACTION, AMEX_CID_NUMBER, MANUAL_OVERRIDE, AUTH_REQUIRED, COMPANY_NAME, HOLDER_NAME, RESP_VALID_CODE, RESP_TRANS_IDFR, RESP_PAY_SERVICE, RESP_ADDRESS_VERIF, RESP_AUTH_SOURCE, RESP_POS_ENTRY_MOD, RESP_AUTH_RESPONSE, RESP_AUTHORIZATION, CARD_IDENTIFIER, PLAN_CODE, CO_CNY_CONV_RATE, ID_MSK_ACNT_CRD, LU_ACNT_CRD_TKN, CD_CNY, TRN_RCPT_DATA, CH_IM_TND_AZN_CT, RESP_ID, CH_IMG_TND_AZN_CT, EMV_TAG_AID, EMV_TAG_TVR, EMV_TAG_IAD, EMV_TAG_TSI, EMV_TAG_ARC, EMV_TAG_CVM) values (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
	public static String updateSql = "update TR_LTM_CRDB_CRD_TN set AI_TRN = ?, AI_LN_ITM = ?, ID_ISSR_TND_MD = ?, ID_ACNT_DB_CR_CRD = ?, TY_ID_PRSL_RQ = ?, ID_RFC_PRSL_ID = ?, LU_MTH_AZN = ?, DE_TRCK_2_BT_MP = ?, LU_ADJN_CR_DB = ?, LU_NMB_CRD_SWP_KY = ?, LU_CLS_TND = ?, APPROVAL_DATE = ?, TRACK_DATA = ?, TRACK_NUMBER = ?, EXPIRATION_DATE = ?, TENDER_TYPE = ?, MESSAGE_TYPE = ?, MESSAGE_IDENTIFIER = ?, RESP_STATUS_DESC = ?, RESP_STATUS_CODE = ?, RESP_HOST_ACTION = ?, AMEX_CID_NUMBER = ?, MANUAL_OVERRIDE = ?, AUTH_REQUIRED = ?, COMPANY_NAME = ?, HOLDER_NAME = ?, RESP_VALID_CODE = ?, RESP_TRANS_IDFR = ?, RESP_PAY_SERVICE = ?, RESP_ADDRESS_VERIF = ?, RESP_AUTH_SOURCE = ?, RESP_POS_ENTRY_MOD = ?, RESP_AUTH_RESPONSE = ?, RESP_AUTHORIZATION = ?, CARD_IDENTIFIER = ?, PLAN_CODE = ? CO_CNY_CONV_RATE = ? ID_MSK_ACNT_CRD = ? LU_ACNT_CRD_TKN = ? CD_CNY = ? TRN_RCPT_DATA = ? CH_IM_TND_AZN_CT = ? RESP_ID = ?, CH_IMG_TND_AZN_CT = ?,EMV_TAG_AID = ?, EMV_TAG_TVR = ?, EMV_TAG_IAD = ?, EMV_TAG_TSI = ?, EMV_TAG_ARC = ?, EMV_TAG_CVM = ?";
	public static String deleteSql = "delete from TR_LTM_CRDB_CRD_TN ";
																	
    /*public static String selectSql = "select AI_TRN, AI_LN_ITM, ID_ISSR_TND_MD, ID_ACNT_DB_CR_CRD, TY_ID_PRSL_RQ, ID_RFC_PRSL_ID, LU_MTH_AZN, DE_TRCK_2_BT_MP, LU_ADJN_CR_DB, LU_NMB_CRD_SWP_KY, CH_IM_TND_AZN_CT, LU_CLS_TND, APPROVAL_DATE, RESP_ID, TRACK_DATA, TRACK_NUMBER, EXPIRATION_DATE, TENDER_TYPE, MESSAGE_TYPE, MESSAGE_IDENTIFIER, RESP_STATUS_DESC, RESP_STATUS_CODE, RESP_HOST_ACTION, AMEX_CID_NUMBER, MANUAL_OVERRIDE, AUTH_REQUIRED, COMPANY_NAME, HOLDER_NAME, RESP_VALID_CODE, RESP_TRANS_IDFR, RESP_PAY_SERVICE, RESP_ADDRESS_VERIF, RESP_AUTH_SOURCE, RESP_POS_ENTRY_MOD, RESP_AUTH_RESPONSE, RESP_AUTHORIZATION, CARD_IDENTIFIER, PLAN_CODE, CO_CNY_CONV_RATE, ID_MSK_ACNT_CRD, LU_ACNT_CRD_TKN, CD_CNY, TRN_RCPT_DATA from TR_LTM_CRDB_CRD_TN ";
	public static String insertSql = "insert into TR_LTM_CRDB_CRD_TN (AI_TRN, AI_LN_ITM, ID_ISSR_TND_MD, ID_ACNT_DB_CR_CRD, TY_ID_PRSL_RQ, ID_RFC_PRSL_ID, LU_MTH_AZN, DE_TRCK_2_BT_MP, LU_ADJN_CR_DB, LU_NMB_CRD_SWP_KY, CH_IM_TND_AZN_CT, LU_CLS_TND, APPROVAL_DATE, RESP_ID, TRACK_DATA, TRACK_NUMBER, EXPIRATION_DATE, TENDER_TYPE, MESSAGE_TYPE, MESSAGE_IDENTIFIER, RESP_STATUS_DESC, RESP_STATUS_CODE, RESP_HOST_ACTION, AMEX_CID_NUMBER, MANUAL_OVERRIDE, AUTH_REQUIRED, COMPANY_NAME, HOLDER_NAME, RESP_VALID_CODE, RESP_TRANS_IDFR, RESP_PAY_SERVICE, RESP_ADDRESS_VERIF, RESP_AUTH_SOURCE, RESP_POS_ENTRY_MOD, RESP_AUTH_RESPONSE, RESP_AUTHORIZATION, CARD_IDENTIFIER, PLAN_CODE, CO_CNY_CONV_RATE, ID_MSK_ACNT_CRD, LU_ACNT_CRD_TKN, CD_CNY, TRN_RCPT_DATA) values (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
	public static String updateSql = "update TR_LTM_CRDB_CRD_TN set AI_TRN = ?, AI_LN_ITM = ?, ID_ISSR_TND_MD = ?, ID_ACNT_DB_CR_CRD = ?, TY_ID_PRSL_RQ = ?, ID_RFC_PRSL_ID = ?, LU_MTH_AZN = ?, DE_TRCK_2_BT_MP = ?, LU_ADJN_CR_DB = ?, LU_NMB_CRD_SWP_KY = ?, CH_IM_TND_AZN_CT = ?, LU_CLS_TND = ?, APPROVAL_DATE = ?, RESP_ID = ?, TRACK_DATA = ?, TRACK_NUMBER = ?, EXPIRATION_DATE = ?, TENDER_TYPE = ?, MESSAGE_TYPE = ?, MESSAGE_IDENTIFIER = ?, RESP_STATUS_DESC = ?, RESP_STATUS_CODE = ?, RESP_HOST_ACTION = ?, AMEX_CID_NUMBER = ?, MANUAL_OVERRIDE = ?, AUTH_REQUIRED = ?, COMPANY_NAME = ?, HOLDER_NAME = ?, RESP_VALID_CODE = ?, RESP_TRANS_IDFR = ?, RESP_PAY_SERVICE = ?, RESP_ADDRESS_VERIF = ?, RESP_AUTH_SOURCE = ?, RESP_POS_ENTRY_MOD = ?, RESP_AUTH_RESPONSE = ?, RESP_AUTHORIZATION = ?, CARD_IDENTIFIER = ?, PLAN_CODE = ? CO_CNY_CONV_RATE = ? ID_MSK_ACNT_CRD = ? LU_ACNT_CRD_TKN = ? CD_CNY = ? TRN_RCPT_DATA = ?";
	public static String deleteSql = "delete from TR_LTM_CRDB_CRD_TN ";
*/
	public static String TABLE_NAME = "TR_LTM_CRDB_CRD_TN";
	public static String COL_AI_TRN = "TR_LTM_CRDB_CRD_TN.AI_TRN";
	public static String COL_AI_LN_ITM = "TR_LTM_CRDB_CRD_TN.AI_LN_ITM";
	public static String COL_ID_ISSR_TND_MD = "TR_LTM_CRDB_CRD_TN.ID_ISSR_TND_MD";
	public static String COL_ID_ACNT_DB_CR_CRD = "TR_LTM_CRDB_CRD_TN.ID_ACNT_DB_CR_CRD";
	public static String COL_TY_ID_PRSL_RQ = "TR_LTM_CRDB_CRD_TN.TY_ID_PRSL_RQ";
	public static String COL_ID_RFC_PRSL_ID = "TR_LTM_CRDB_CRD_TN.ID_RFC_PRSL_ID";
	public static String COL_LU_MTH_AZN = "TR_LTM_CRDB_CRD_TN.LU_MTH_AZN";
	public static String COL_DE_TRCK_2_BT_MP = "TR_LTM_CRDB_CRD_TN.DE_TRCK_2_BT_MP";
	public static String COL_LU_ADJN_CR_DB = "TR_LTM_CRDB_CRD_TN.LU_ADJN_CR_DB";
	public static String COL_LU_NMB_CRD_SWP_KY = "TR_LTM_CRDB_CRD_TN.LU_NMB_CRD_SWP_KY";
	public static String COL_LU_CLS_TND = "TR_LTM_CRDB_CRD_TN.LU_CLS_TND";
	public static String COL_APPROVAL_DATE = "TR_LTM_CRDB_CRD_TN.APPROVAL_DATE";
	public static String COL_RESP_ID = "TR_LTM_CRDB_CRD_TN.RESP_ID";
	public static String COL_TRACK_DATA = "TR_LTM_CRDB_CRD_TN.TRACK_DATA";
	public static String COL_TRACK_NUMBER = "TR_LTM_CRDB_CRD_TN.TRACK_NUMBER";
	public static String COL_EXPIRATION_DATE = "TR_LTM_CRDB_CRD_TN.EXPIRATION_DATE";
	public static String COL_TENDER_TYPE = "TR_LTM_CRDB_CRD_TN.TENDER_TYPE";
	public static String COL_MESSAGE_TYPE = "TR_LTM_CRDB_CRD_TN.MESSAGE_TYPE";
	public static String COL_MESSAGE_IDENTIFIER = "TR_LTM_CRDB_CRD_TN.MESSAGE_IDENTIFIER";
	public static String COL_RESP_STATUS_DESC = "TR_LTM_CRDB_CRD_TN.RESP_STATUS_DESC";
	public static String COL_RESP_STATUS_CODE = "TR_LTM_CRDB_CRD_TN.RESP_STATUS_CODE";
	public static String COL_RESP_HOST_ACTION = "TR_LTM_CRDB_CRD_TN.RESP_HOST_ACTION";
	public static String COL_AMEX_CID_NUMBER = "TR_LTM_CRDB_CRD_TN.AMEX_CID_NUMBER";
	public static String COL_MANUAL_OVERRIDE = "TR_LTM_CRDB_CRD_TN.MANUAL_OVERRIDE";
	public static String COL_AUTH_REQUIRED = "TR_LTM_CRDB_CRD_TN.AUTH_REQUIRED";
	public static String COL_COMPANY_NAME = "TR_LTM_CRDB_CRD_TN.COMPANY_NAME";
	public static String COL_HOLDER_NAME = "TR_LTM_CRDB_CRD_TN.HOLDER_NAME";
	public static String COL_RESP_VALID_CODE = "TR_LTM_CRDB_CRD_TN.RESP_VALID_CODE";
	public static String COL_RESP_TRANS_IDFR = "TR_LTM_CRDB_CRD_TN.RESP_TRANS_IDFR";
	public static String COL_RESP_PAY_SERVICE = "TR_LTM_CRDB_CRD_TN.RESP_PAY_SERVICE";
	public static String COL_RESP_ADDRESS_VERIF = "TR_LTM_CRDB_CRD_TN.RESP_ADDRESS_VERIF";
	public static String COL_RESP_AUTH_SOURCE = "TR_LTM_CRDB_CRD_TN.RESP_AUTH_SOURCE";
	public static String COL_RESP_POS_ENTRY_MOD = "TR_LTM_CRDB_CRD_TN.RESP_POS_ENTRY_MOD";
	public static String COL_RESP_AUTH_RESPONSE = "TR_LTM_CRDB_CRD_TN.RESP_AUTH_RESPONSE";
	public static String COL_RESP_AUTHORIZATION = "TR_LTM_CRDB_CRD_TN.RESP_AUTHORIZATION";
	public static String COL_CARD_IDENTIFIER = "TR_LTM_CRDB_CRD_TN.CARD_IDENTIFIER";
	public static String COL_PLAN_CODE = "TR_LTM_CRDB_CRD_TN.PLAN_CODE";
	public static String COL_CO_CNY_CONV_RATE = "TR_LTM_CRDB_CRD_TN.CO_CNY_CONV_RATE";
	public static String COL_ID_MSK_ACNT_CRD = "TR_LTM_CRDB_CRD_TN.ID_MSK_ACNT_CRD";
	public static String COL_LU_ACNT_CRD_TKN = "TR_LTM_CRDB_CRD_TN.LU_ACNT_CRD_TKN";
	public static String COL_CD_CNY = "TR_LTM_CRDB_CRD_TN.CD_CNY";
	public static String COL_TRN_RCPT_DATA = "TR_LTM_CRDB_CRD_TN.TRN_RCPT_DATA";
	public static String COL_CH_IM_TND_AZN_CT = "TR_LTM_CRDB_CRD_TN.CH_IM_TND_AZN_CT";
	//Vivek Mishra : Added this new column for storing customer signature.
	public static String COL_CH_IMG_TND_AZN_CT = "TR_LTM_CRDB_CRD_TN.CH_IMG_TND_AZN_CT";

	// VISHAL YEVALE - 7 SEPT 2016 : ADDED TO PRINT AND SAVE EMV TAG
	public static String COL_EMV_TAG_AID="TR_LTM_CRDB_CRD_TN.EMV_TAG_AID";
	public static String COL_EMV_TAG_TVR="TR_LTM_CRDB_CRD_TN.EMV_TAG_TVR";
	public static String COL_EMV_TAG_IAD="TR_LTM_CRDB_CRD_TN.EMV_TAG_IAD";
	public static String COL_EMV_TAG_TSI="TR_LTM_CRDB_CRD_TN.EMV_TAG_TSI";
	public static String COL_EMV_TAG_ARC="TR_LTM_CRDB_CRD_TN.EMV_TAG_ARC";
	public static String COL_EMV_TAG_CVM="TR_LTM_CRDB_CRD_TN.EMV_TAG_CVM";
	// end VISHAL YEVALE - 7 SEPT 2016 : ADDED TO PRINT AND SAVE EMV TAG

	
	public String getSelectSql() {
		return selectSql;
	}

	public String getInsertSql() {
		return insertSql;
	}

	public String getUpdateSql() {
		return updateSql;
	}

	public String getDeleteSql() {
		return deleteSql;
	}

	private String aiTrn;
	private Long aiLnItm;
	private String idIssrTndMd;
	private String idAcntDbCrCrd;
	private String tyIdPrslRq;
	private String idRfcPrslId;
	private String luMthAzn;
	private String deTrck2BtMp;
	private String luAdjnCrDb;
	private String luNmbCrdSwpKy;
	private String chImTndAznCt;
    private String luClsTnd;
	private Date approvalDate;
	private String trackData;
	private String trackNumber;
	private Date expirationDate;
	private String tenderType;
	private String messageType;
	private String messageIdentifier;
	private String respStatusDesc;
	private String respStatusCode;
	private String respHostAction;
	private String amexCidNumber;
	private Boolean manualOverride;
	private Boolean authRequired;
	private String companyName;
	private String holderName;
	private String respValidCode;
	private String respTransIdfr;
	private String respPayService;
	private String respAddressVerif;
	private String respAuthSource;
	private String respPosEntryMod;
	private String respAuthResponse;
	private String respAuthorization;
	private String cardIdentifier;
	private String planCode;
	private String convRate;
	private String maskCardNum;
	private String accntCardToken;
	private String currencyCode;
	private String recieptData;
	//Vivek Mishra : Added for AJB signature persistence and retrieval 
	//private Object signature;
	//Vivek Mishra : Changed the signature column to chImgTndAznCt
	private Object chImgTndAznCt;
	//private String signature;
	//Ends
	//Vivek Mishra : Added for AJB unique sequence persistence and retrieval
	//private String ajbSeq;
	//Vivek Mishra : Changed the ajbSeq column to respId
	private String respId;
	//Ends
	// VISHAL YEVALE - 7 SEPT 2016 : ADDED TO PRINT AND SAVE EMV TAG
	private String emvTagAid;
	private String emvTagTvr;
	private String emvTagIad;
	private String emvTagTsi;
	private String emvTagArc;
	private String emvTagCvm;
	public String getEmvTagAid() {
		return emvTagAid;
	}

	public void setEmvTagAid(String emvTagAid) {
		this.emvTagAid = emvTagAid;
	}

	public String getEmvTagTvr() {
		return emvTagTvr;
	}

	public void setEmvTagTvr(String emvTagTvr) {
		this.emvTagTvr = emvTagTvr;
	}

	public String getEmvTagIad() {
		return emvTagIad;
	}

	public void setEmvTagIad(String emvTagIad) {
		this.emvTagIad = emvTagIad;
	}

	public String getEmvTagTsi() {
		return emvTagTsi;
	}

	public void setEmvTagTsi(String emvTagTsi) {
		this.emvTagTsi = emvTagTsi;
	}

	public String getEmvTagArc() {
		return emvTagArc;
	}

	public void setEmvTagArc(String emvTagArc) {
		this.emvTagArc = emvTagArc;
	}

	public String getEmvTagCvm() {
		return emvTagCvm;
	}

	public void setEmvTagCvm(String emvTagCvm) {
		this.emvTagCvm = emvTagCvm;
	}
	//end  VISHAL YEVALE - 7 SEPT 2016 : ADDED TO PRINT AND SAVE EMV TAG

	public String getAiTrn() {
		return this.aiTrn;
	}

	public void setAiTrn(String aiTrn) {
		this.aiTrn = aiTrn;
	}

	public Long getAiLnItm() {
		return this.aiLnItm;
	}

	public void setAiLnItm(Long aiLnItm) {
		this.aiLnItm = aiLnItm;
	}

	public void setAiLnItm(long aiLnItm) {
		this.aiLnItm = new Long(aiLnItm);
	}

	public void setAiLnItm(int aiLnItm) {
		this.aiLnItm = new Long((long) aiLnItm);
	}

	public String getIdIssrTndMd() {
		return this.idIssrTndMd;
	}

	public void setIdIssrTndMd(String idIssrTndMd) {
		this.idIssrTndMd = idIssrTndMd;
	}

	// public String getIdAcntDbCrCrd() { return this.idAcntDbCrCrd; }
	public String getIdAcntDbCrCrd() {
		return this.idAcntDbCrCrd;
	}

	public void setIdAcntDbCrCrd(String idAcntDbCrCrd) {
		// Mask the Credit Card Num
		// System.out.println ("Masking the Account No for DateBase ");
		// this.idAcntDbCrCrd = CreditAuthUtil.maskCreditCardNo(idAcntDbCrCrd);
		this.idAcntDbCrCrd = idAcntDbCrCrd;
	}

	public String getTyIdPrslRq() {
		return this.tyIdPrslRq;
	}

	public void setTyIdPrslRq(String tyIdPrslRq) {
		this.tyIdPrslRq = tyIdPrslRq;
	}

	public String getIdRfcPrslId() {
		return this.idRfcPrslId;
	}

	public void setIdRfcPrslId(String idRfcPrslId) {
		this.idRfcPrslId = idRfcPrslId;
	}

	public String getLuMthAzn() {
		return this.luMthAzn;
	}

	public void setLuMthAzn(String luMthAzn) {
		this.luMthAzn = luMthAzn;
	}

	public String getDeTrck2BtMp() {
		return this.deTrck2BtMp;
	}

	public void setDeTrck2BtMp(String deTrck2BtMp) {
		this.deTrck2BtMp = deTrck2BtMp;
	}

	public String getLuAdjnCrDb() {
		return this.luAdjnCrDb;
	}

	public void setLuAdjnCrDb(String luAdjnCrDb) {
		this.luAdjnCrDb = luAdjnCrDb;
	}

	public String getLuNmbCrdSwpKy() {
		return this.luNmbCrdSwpKy;
	}

	public void setLuNmbCrdSwpKy(String luNmbCrdSwpKy) {
		this.luNmbCrdSwpKy = luNmbCrdSwpKy;
	}

	public String getLuClsTnd() {
		return this.luClsTnd;
	}

	public void setLuClsTnd(String luClsTnd) {
		this.luClsTnd = luClsTnd;
	}

	public Date getApprovalDate() {
		return this.approvalDate;
	}

	public void setApprovalDate(Date approvalDate) {
		this.approvalDate = approvalDate;
	}

	public String getTrackData() {
		return this.trackData;
	}

	public void setTrackData(String trackData) {
		// Issue # 993
		// Mask the Track Data
		// System.out.println ("Masking the Track Data for DateBase");
		// this.trackData = CreditAuthUtil.maskTrackData(trackData);
		this.trackData = trackData;
	}

	public String getTrackNumber() {
		return this.trackNumber;
	}

	public void setTrackNumber(String trackNumber) {
		this.trackNumber = trackNumber;
	}

	public Date getExpirationDate() {
		return this.expirationDate;
	}

	public void setExpirationDate(Date expirationDate) {
		this.expirationDate = expirationDate;
	}

	public String getTenderType() {
		return this.tenderType;
	}

	public void setTenderType(String tenderType) {
		this.tenderType = tenderType;
	}

	public String getMessageType() {
		return this.messageType;
	}

	public void setMessageType(String messageType) {
		this.messageType = messageType;
	}

	public String getMessageIdentifier() {
		return this.messageIdentifier;
	}

	public void setMessageIdentifier(String messageIdentifier) {
		this.messageIdentifier = messageIdentifier;
	}

	public String getRespStatusDesc() {
		return this.respStatusDesc;
	}

	public void setRespStatusDesc(String respStatusDesc) {
		this.respStatusDesc = respStatusDesc;
	}

	public String getRespStatusCode() {
		return this.respStatusCode;
	}

	public void setRespStatusCode(String respStatusCode) {
		this.respStatusCode = respStatusCode;
	}

	public String getRespHostAction() {
		return this.respHostAction;
	}

	public void setRespHostAction(String respHostAction) {
		this.respHostAction = respHostAction;
	}

	public String getAmexCidNumber() {
		return this.amexCidNumber;
	}

	public void setAmexCidNumber(String amexCidNumber) {
		this.amexCidNumber = amexCidNumber;
	}

	public Boolean getManualOverride() {
		return this.manualOverride;
	}

	public void setManualOverride(Boolean manualOverride) {
		this.manualOverride = manualOverride;
	}

	public void setManualOverride(boolean manualOverride) {
		this.manualOverride = new Boolean(manualOverride);
	}

	public Boolean getAuthRequired() {
		return this.authRequired;
	}

	public void setAuthRequired(Boolean authRequired) {
		this.authRequired = authRequired;
	}

	public void setAuthRequired(boolean authRequired) {
		this.authRequired = new Boolean(authRequired);
	}

	public String getCompanyName() {
		return this.companyName;
	}

	public void setCompanyName(String companyName) {
		this.companyName = companyName;
	}

	public String getHolderName() {
		return this.holderName;
	}

	public void setHolderName(String holderName) {
		this.holderName = holderName;
	}

	public String getRespValidCode() {
		return this.respValidCode;
	}

	public void setRespValidCode(String respValidCode) {
		this.respValidCode = respValidCode;
	}

	public String getRespTransIdfr() {
		return this.respTransIdfr;
	}

	public void setRespTransIdfr(String respTransIdfr) {
		this.respTransIdfr = respTransIdfr;
	}

	public String getRespPayService() {
		return this.respPayService;
	}

	public void setRespPayService(String respPayService) {
		this.respPayService = respPayService;
	}

	public String getRespAddressVerif() {
		return this.respAddressVerif;
	}

	public void setRespAddressVerif(String respAddressVerif) {
		this.respAddressVerif = respAddressVerif;
	}

	public String getRespAuthSource() {
		return this.respAuthSource;
	}

	public void setRespAuthSource(String respAuthSource) {
		this.respAuthSource = respAuthSource;
	}

	public String getRespPosEntryMod() {
		return this.respPosEntryMod;
	}

	public void setRespPosEntryMod(String respPosEntryMod) {
		this.respPosEntryMod = respPosEntryMod;
	}

	public String getRespAuthResponse() {
		return this.respAuthResponse;
	}

	public void setRespAuthResponse(String respAuthResponse) {
		this.respAuthResponse = respAuthResponse;
	}

	public String getRespAuthorization() {
		return this.respAuthorization;
	}

	public void setRespAuthorization(String respAuthorization) {
		this.respAuthorization = respAuthorization;
	}

	public String getCardIdentifier() {
		return this.cardIdentifier;
	}

	public void setCardIdentifier(String cardIdentifier) {
		this.cardIdentifier = cardIdentifier;
	}

	public String getPlanCode() {
		return this.planCode;
	}

	public void setPlanCode(String planCode) {
		this.planCode = planCode;
	}

	public String getConvRate() {
		return convRate;
	}

	public void setConvRate(String convRate) {
		this.convRate = convRate;
	}

	public String getMaskCardNum() {
		return maskCardNum;
	}

	public void setMaskCardNum(String maskCardNum) {
		this.maskCardNum = maskCardNum;
	}

	public String getAccntCardToken() {
		return accntCardToken;
	}

	public void setAccntCardToken(String accntCardToken) {
		this.accntCardToken = accntCardToken;
	}

	public String getCurrencyCode() {
		return currencyCode;
	}

	public void setCdCny(String currencyCode) {
		this.currencyCode = currencyCode;
	}

	public String getRecieptData() {
		return recieptData;
	}

	public void setRecieptData(String recieptData) {
		this.recieptData = recieptData;
	}
	
	public String getChImTndAznCt() {
		return chImTndAznCt;
	}

	public void setChImTndAznCt(String chImTndAznCt) {
		this.chImTndAznCt = chImTndAznCt;
	}

	//Vivek Mishra : Added for AJB signature persistence and retrieval 
	public Object getChImgTndAznCt() {
		return chImgTndAznCt;
	}

	public void setChImgTndAznCt(Object chImgTndAznCt) {
		this.chImgTndAznCt = chImgTndAznCt;
	}
    //Ends
	
	//Vivek Mishra : Added for AJB unique sequence persistence and retrieval
	public String getRespId() {
		return this.respId;
	}

	public void setRespId(String respId) {
		this.respId = respId;
	}
	//Ends
	
	public BaseOracleBean[] getDatabeans(ResultSet rs) throws SQLException {
		ArrayList list = new ArrayList();
		// TD
		EncryptionUtils encryptionUtils = new EncryptionUtils();
		while (rs.next()) {
			TrLtmCrdbCrdTnOracleBean bean = new TrLtmCrdbCrdTnOracleBean();
			bean.aiTrn = getStringFromResultSet(rs, "AI_TRN");
			bean.aiLnItm = getLongFromResultSet(rs, "AI_LN_ITM");
			bean.idIssrTndMd = getStringFromResultSet(rs, "ID_ISSR_TND_MD");
			// TD
			// bean.idAcntDbCrCrd = getStringFromResultSet(rs,
			// "ID_ACNT_DB_CR_CRD");
			// ----------------------------------------------------------------------
			String ccNum = getStringFromResultSet(rs, "ID_ACNT_DB_CR_CRD");
			String decryptCCNum = null;
			if (ccNum != null && ccNum.trim().length() > 0) {
				decryptCCNum = encryptionUtils.decrypt(ccNum);
			}
			bean.idAcntDbCrCrd = decryptCCNum;
			// bean.setIdAcntDbCrCrd(decryptCCNum);
			// ----------------------------------------------------------------------
			bean.tyIdPrslRq = getStringFromResultSet(rs, "TY_ID_PRSL_RQ");
			bean.idRfcPrslId = getStringFromResultSet(rs, "ID_RFC_PRSL_ID");
			bean.luMthAzn = getStringFromResultSet(rs, "LU_MTH_AZN");
			bean.deTrck2BtMp = getStringFromResultSet(rs, "DE_TRCK_2_BT_MP");
			bean.luAdjnCrDb = getStringFromResultSet(rs, "LU_ADJN_CR_DB");
			bean.luNmbCrdSwpKy = getStringFromResultSet(rs, "LU_NMB_CRD_SWP_KY");
			bean.luClsTnd = getStringFromResultSet(rs, "LU_CLS_TND");
			bean.approvalDate = getDateFromResultSet(rs, "APPROVAL_DATE");
			// TD
			// bean.trackData = getStringFromResultSet(rs, "TRACK_DATA");
			// ----------------------------------------------------------------------
			String trkData = getStringFromResultSet(rs, "TRACK_DATA");
			String decryptTrackData = null;
			if (trkData != null && trkData.trim().length() > 0) {
				decryptTrackData = encryptionUtils.decrypt(trkData);
			}
			bean.trackData = decryptTrackData;
			// ----------------------------------------------------------------------
			bean.trackNumber = getStringFromResultSet(rs, "TRACK_NUMBER");
			bean.expirationDate = getDateFromResultSet(rs, "EXPIRATION_DATE");
			bean.tenderType = getStringFromResultSet(rs, "TENDER_TYPE");
			bean.messageType = getStringFromResultSet(rs, "MESSAGE_TYPE");
			bean.messageIdentifier = getStringFromResultSet(rs,
					"MESSAGE_IDENTIFIER");
			bean.respStatusDesc = getStringFromResultSet(rs, "RESP_STATUS_DESC");
			bean.respStatusCode = getStringFromResultSet(rs, "RESP_STATUS_CODE");
			bean.respHostAction = getStringFromResultSet(rs, "RESP_HOST_ACTION");
			bean.amexCidNumber = getStringFromResultSet(rs, "AMEX_CID_NUMBER");
			bean.manualOverride = getBooleanFromResultSet(rs, "MANUAL_OVERRIDE");
			bean.authRequired = getBooleanFromResultSet(rs, "AUTH_REQUIRED");
			bean.companyName = getStringFromResultSet(rs, "COMPANY_NAME");
			bean.holderName = getStringFromResultSet(rs, "HOLDER_NAME");
			bean.respValidCode = getStringFromResultSet(rs, "RESP_VALID_CODE");
			bean.respTransIdfr = getStringFromResultSet(rs, "RESP_TRANS_IDFR");
			bean.respPayService = getStringFromResultSet(rs, "RESP_PAY_SERVICE");
			bean.respAddressVerif = getStringFromResultSet(rs,
					"RESP_ADDRESS_VERIF");
			bean.respAuthSource = getStringFromResultSet(rs, "RESP_AUTH_SOURCE");
			bean.respPosEntryMod = getStringFromResultSet(rs,
					"RESP_POS_ENTRY_MOD");
			bean.respAuthResponse = getStringFromResultSet(rs,
					"RESP_AUTH_RESPONSE");
			bean.respAuthorization = getStringFromResultSet(rs,
					"RESP_AUTHORIZATION");
			bean.cardIdentifier = getStringFromResultSet(rs, "CARD_IDENTIFIER");
			bean.planCode = getStringFromResultSet(rs, "PLAN_CODE");
			bean.convRate = getStringFromResultSet(rs, "CO_CNY_CONV_RATE");
			bean.maskCardNum = getStringFromResultSet(rs, "ID_MSK_ACNT_CRD");
			bean.accntCardToken = getStringFromResultSet(rs, "LU_ACNT_CRD_TKN");
			bean.currencyCode = getStringFromResultSet(rs, "CD_CNY");
			bean.recieptData = getStringFromResultSet(rs, "TRN_RCPT_DATA");
			bean.chImTndAznCt = getStringFromResultSet(rs, "CH_IM_TND_AZN_CT");
			//Vivek Mishra : Added for AJB unique sequence persistence and retrieval
			bean.respId = getStringFromResultSet(rs, "RESP_ID");
			//Ends
			//Vivek Mishra : Added for AJB signature persistence and retrieval 
			bean.chImgTndAznCt = getObjectFromResultSet(rs, "CH_IMG_TND_AZN_CT");
			//Ends
			//bean.signature = getStringFromResultSet(rs, "SIGNATURE");

			// VISHAL YEVALE - 7 SEPT 2016 : ADDED TO PRINT AND SAVE EMV TAG
			bean.emvTagAid=getStringFromResultSet(rs, "EMV_TAG_AID");
			bean.emvTagArc=getStringFromResultSet(rs, "EMV_TAG_ARC");
			bean.emvTagCvm=getStringFromResultSet(rs, "EMV_TAG_CVM");
			bean.emvTagIad=getStringFromResultSet(rs, "EMV_TAG_IAD");
			bean.emvTagTsi=getStringFromResultSet(rs, "EMV_TAG_TSI");
			bean.emvTagTvr=getStringFromResultSet(rs, "EMV_TAG_TVR");
			// end VISHAL YEVALE - 7 SEPT 2016 : ADDED TO PRINT AND SAVE EMV TAG

			list.add(bean);
		}
		return (TrLtmCrdbCrdTnOracleBean[]) list
				.toArray(new TrLtmCrdbCrdTnOracleBean[0]);
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
		addToList(list, this.getAiTrn(), Types.VARCHAR);
		addToList(list, this.getAiLnItm(), Types.DECIMAL);
		addToList(list, this.getIdIssrTndMd(), Types.VARCHAR);
		// TD
		// addToList(list, this.getIdAcntDbCrCrd(), Types.VARCHAR);
		// -------------------------------------------------------
		EncryptionUtils encryptionUtils = new EncryptionUtils();
		String ccNum = this.getIdAcntDbCrCrd();
		String decryptCCNum = null;
		if (ccNum != null && ccNum.trim().length() > 0) {
			decryptCCNum = encryptionUtils.encrypt(ccNum);
		}
		addToList(list, decryptCCNum, Types.VARCHAR);
		// -------------------------------------------------------
		addToList(list, this.getTyIdPrslRq(), Types.VARCHAR);
		addToList(list, this.getIdRfcPrslId(), Types.VARCHAR);
		addToList(list, this.getLuMthAzn(), Types.VARCHAR);
		addToList(list, this.getDeTrck2BtMp(), Types.VARCHAR);
		addToList(list, this.getLuAdjnCrDb(), Types.VARCHAR);
		addToList(list, this.getLuNmbCrdSwpKy(), Types.VARCHAR);
		addToList(list, this.getLuClsTnd(), Types.VARCHAR);
		addToList(list, this.getApprovalDate(), Types.TIMESTAMP);
		// TD
		// addToList(list, this.getTrackData(), Types.VARCHAR);
		// -------------------------------------------------------
		String trkData = this.getTrackData();
		String decryptTrkData = null;
		if (trkData != null && trkData.trim().length() > 0) {
			decryptTrkData = encryptionUtils.encrypt(trkData);
		}
		addToList(list, decryptTrkData, Types.VARCHAR);
		// -------------------------------------------------------

		addToList(list, this.getTrackNumber(), Types.VARCHAR);
		addToList(list, this.getExpirationDate(), Types.TIMESTAMP);
		addToList(list, this.getTenderType(), Types.VARCHAR);
		addToList(list, this.getMessageType(), Types.VARCHAR);
		addToList(list, this.getMessageIdentifier(), Types.VARCHAR);
		addToList(list, this.getRespStatusDesc(), Types.VARCHAR);
		addToList(list, this.getRespStatusCode(), Types.VARCHAR);
		addToList(list, this.getRespHostAction(), Types.VARCHAR);
		addToList(list, this.getAmexCidNumber(), Types.VARCHAR);
		addToList(list, this.getManualOverride(), Types.DECIMAL);
		addToList(list, this.getAuthRequired(), Types.DECIMAL);
		addToList(list, this.getCompanyName(), Types.VARCHAR);
		addToList(list, this.getHolderName(), Types.VARCHAR);
		addToList(list, this.getRespValidCode(), Types.VARCHAR);
		addToList(list, this.getRespTransIdfr(), Types.VARCHAR);
		addToList(list, this.getRespPayService(), Types.VARCHAR);
		addToList(list, this.getRespAddressVerif(), Types.VARCHAR);
		addToList(list, this.getRespAuthSource(), Types.VARCHAR);
		addToList(list, this.getRespPosEntryMod(), Types.VARCHAR);
		addToList(list, this.getRespAuthResponse(), Types.VARCHAR);
		addToList(list, this.getRespAuthorization(), Types.VARCHAR);
		addToList(list, this.getCardIdentifier(), Types.VARCHAR);
		addToList(list, this.getPlanCode(), Types.DECIMAL);
		addToList(list, this.getConvRate(), Types.DECIMAL);
		addToList(list, this.getMaskCardNum(), Types.VARCHAR);
		addToList(list, this.getAccntCardToken(), Types.VARCHAR);
		addToList(list, this.getCurrencyCode(), Types.VARCHAR);
		addToList(list, this.getRecieptData(), Types.BLOB);
		addToList(list, this.getChImTndAznCt(), Types.VARCHAR);
		addToList(list, this.getRespId(), Types.VARCHAR);
		//addToList(list, this.getAjbSeq(), Types.VARCHAR);
		//addToList(list, this.getSignature(), Types.VARCHAR);
		addToList(list, this.getChImgTndAznCt(), Types.BLOB);
		
		// VISHAL YEVALE - 7 SEPT 2016 : ADDED TO PRINT AND SAVE EMV TAG

		addToList(list, this.getEmvTagAid(),Types.VARCHAR);
		addToList(list, this.getEmvTagTvr(),Types.VARCHAR);
		addToList(list, this.getEmvTagIad(),Types.VARCHAR);
		addToList(list, this.getEmvTagTsi(),Types.VARCHAR);
		addToList(list, this.getEmvTagArc(),Types.VARCHAR);
		addToList(list, this.getEmvTagCvm(),Types.VARCHAR);
		
		// end VISHAL YEVALE - 7 SEPT 2016 : ADDED TO PRINT AND SAVE EMV TAG

		return list;
	}
}
