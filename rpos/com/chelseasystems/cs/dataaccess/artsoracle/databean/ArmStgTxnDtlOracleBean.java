package com.chelseasystems.cs.dataaccess.artsoracle.databean;

import java.util.Date;
import java.util.List;
import java.util.ArrayList;
import java.sql.SQLException;
import java.sql.ResultSet;
import java.sql.Types;

/**
 *
 * This class is an object representation of the Arts database table
 * ARM_STG_TXN_DTL<BR>
 * Followings are the column of the table: <BR>
 * RECORD_TYPE -- CHAR(2)<BR>
 * TRANSACTION_ID -- VARCHAR2(20)<BR>
 * LINE_ID -- NUMBER(22)<BR>
 * LINE_SEQ_NUM -- NUMBER(22)<BR>
 * LINE_ITEM_TYPE -- CHAR(1)<BR>
 * DOC_NUM -- VARCHAR2(20)<BR>
 * MISC_ITEM_ID -- VARCHAR2(20)<BR>
 * MISC_ITEM_TXBL -- CHAR(1)<BR>
 * ID_ITM -- VARCHAR2(50)<BR>
 * QUANTITY -- NUMBER(22)<BR>
 * ITM_RTL_PRICE -- NUMBER(10.2)<BR>
 * ITM_SEL_PRICE -- NUMBER(10.2)<BR>
 * MANUAL_MD_AMT -- NUMBER(10.2)<BR>
 * MANUAL_UNIT_PRICE -- NUMBER(10.2)<BR>
 * CONSULTANT_ID -- VARCHAR2(20)<BR>
 * ADD_CONSULTANT_ID -- VARCHAR2(20)<BR>
 * RETURN_REASON_ID -- VARCHAR2(20)<BR>
 * RETURN_COMMENTS -- VARCHAR2(200)<BR>
 * VAT_RATE -- NUMBER(9.2)<BR>
 * TAX_EXEMPT_ID -- VARCHAR2(20)<BR>
 * SHIP_STATE -- VARCHAR2(20)<BR>
 * SHIP_ZIP_CODE -- VARCHAR2(10)<BR>
 * NET_AMT -- NUMBER(10.2)<BR>
 * TAX_AMT -- NUMBER(10.2)<BR>
 * MANUAL_TAX_AMT -- NUMBER(9.2)<BR>
 * EXCEPTION_TAX_JUR -- VARCHAR2(20)<BR>
 * DEAL_MKDN_AMT -- NUMBER(10.2)<BR>
 * DEAL_ID -- VARCHAR2(20)<BR>
 * RTN_SALE_AI_TRN -- VARCHAR2(20)<BR>
 * RTN_ORIG_STORE_ID -- VARCHAR2(30)<BR>
 * RTN_ORIG_PROCESS_DT -- DATE(7)<BR>
 * RTN_ORIG_OPR_ID -- VARCHAR2(30)<BR>
 * RTN_ORIG_REGISTER_ID -- VARCHAR2(30)<BR>
 * ORIG_TRANSACTION_ID -- VARCHAR2(30)<BR>
 * ORIG_ADD_NO -- VARCHAR2(30)<BR>
 * REDUCTION_AMOUNT -- NUMBER(10.2)<BR>
 * REDUCTION_REASON -- VARCHAR2(50)<BR>
 * DSC_AMOUNT -- NUMBER(10.2)<BR>
 * DSC_PERCENT -- NUMBER(9.2)<BR>
 * DSC_REASON -- VARCHAR2(50)<BR>
 * DSC_EMPLOYEE_ID -- VARCHAR2(50)<BR>
 * DSC_ADVERTISING_CODE -- VARCHAR2(50)<BR>
 * DSC_IS_DSC_PERCENT -- CHAR(1)<BR>
 * TND_TYPE -- VARCHAR2(20)<BR>
 * TND_CODE -- VARCHAR2(20)<BR>
 * TND_AMOUNT -- NUMBER(10.2)<BR>
 * TND_ID_ACNT_NMB -- VARCHAR2(30)<BR>
 * TND_RESP_AUTH -- VARCHAR2(30)<BR>
 * TND_APPROVAL_DATE -- DATE(7)<BR>
 * TND_EXPIRATION_DT -- DATE(7)<BR>
 * TND_HOLDER_NAME -- VARCHAR2(50)<BR>
 * TND_MANUAL_OVERRIDE -- CHAR(1)<BR>
 * CHK_ABA_NUM -- VARCHAR2(20)<BR>
 * CHK_ROUTING_NUM -- VARCHAR2(20)<BR>
 * CHK_BANK -- VARCHAR2(20)<BR>
 * CHK_IS_SCANNED -- CHAR(1)<BR>
 * CC_CVV_CODE -- VARCHAR2(10)<BR>
 * CC_AMEX_CID_NUM -- VARCHAR2(10)<BR>
 * RDM_CONTROL_NUMBER -- VARCHAR2(20)<BR>
 * TND_SWIPE_IND -- CHAR(1)<BR>
 * TND_RESP_ADDRESS_VERIF -- VARCHAR2(20)<BR>
 * TND_RESP_JOURNAL_KEY -- VARCHAR2(50)<BR>
 * TND_RESP_MSG -- VARCHAR2(50)<BR>
 * TND_RESP_MSG_NUM -- VARCHAR2(20)<BR>
 * TND_MERCHANT_ID -- VARCHAR2(20)<BR>
 * TND_CARD_IDENTIFIER -- CHAR(2)<BR>
 * CC_CARD_PLAN -- VARCHAR2(20)<BR>
 * RDM_TND_ID_STR_RT -- VARCHAR2(20)<BR>
 * RDM_DC_CPN_SCAN_CODE -- VARCHAR2(20)<BR>
 * RDM_TND_PRM_CODE -- VARCHAR2(10)<BR>
 * RDM_EXPIRATION_DATE -- DATE(7)<BR>
 * FC_XCHANGE_RATE -- NUMBER(10.2)<BR>
 * FC_FROM_CURRENCY -- CHAR(3)<BR>
 * FC_TO_CURRENCY -- CHAR(3)<BR>
 * RDM_TYPE -- VARCHAR2(20)<BR>
 * RDM_ISSUING_STORE -- VARCHAR2(50)<BR>
 * RDM_CUST_NUM -- VARCHAR2(50)<BR>
 * RDM_ISSUANCE_DT -- DATE(7)<BR>
 * RDM_EXPIRATION_DT -- DATE(7)<BR>
 * RDM_FACE_VALUE_AMT -- NUMBER(10.2)<BR>
 * RDM_BALANCE_AMT -- NUMBER(10.2)<BR>
 * RDM_BUYBACK_AMT -- NUMBER(10.2)<BR>
 * RDM_BUYBACK_COMMENT -- VARCHAR2(50)<BR>
 * POST_VOIDED_TRANS_NO -- VARCHAR2(20)<BR>
 * POST_VOID_REASON -- VARCHAR2(20)<BR>
 * POST_VOID_REGISTER -- VARCHAR2(50)<BR>
 * CUSTOMER_ROLE -- CHAR(1)<BR>
 * CUST_ID -- VARCHAR2(30)<BR>
 * CUST_FIRST_NAME -- VARCHAR2(30)<BR>
 * CUST_LAST_NAME -- VARCHAR2(60)<BR>
 * CUST_ADDR_1 -- VARCHAR2(60)<BR>
 * CUST_ADDR_2 -- VARCHAR2(60)<BR>
 * CUST_CITY -- VARCHAR2(30)<BR>
 * CUST_STATE -- VARCHAR2(30)<BR>
 * CUST_COUNTRY -- VARCHAR2(30)<BR>
 * CUST_PCODE -- VARCHAR2(10)<BR>
 * CUST_PHONE_1 -- VARCHAR2(30)<BR>
 * CUST_PHONE_2 -- VARCHAR2(30)<BR>
 * CASHIER_TXN_COMMENT -- VARCHAR2(500)<BR>
 * CASHIER_TXN_REASON -- VARCHAR2(500)<BR>
 * OPENING_DRWR_FND -- NUMBER(10.2)<BR>
 * EOD_TND_TYPE -- VARCHAR2(20)<BR>
 * EOD_TND_TOTAL -- VARCHAR2(200)<BR>
 * NOT_IN_FILE_SKU_DTL -- VARCHAR2(100)<BR>
 * STATUS -- CHAR(1)<BR>
 * DATA_POPULATION_DT -- DATE(7)<BR>
 * EXTRACTED_DT -- DATE(7)<BR>
 * DOC_TYPE -- CHAR(2)<BR>
 * VAT_COMMENTS -- VARCHAR2(200)<BR>
 * ORIG_LINE_ITEM_TYPE -- CHAR(1)<BR>
 * EOD_REPORTED_TND_TOTAL -- VARCHAR2(200)<BR>
 * EXTENDED_BARCODE -- VARCHAR2(128)<BR>
 * APPROVER_ID -- VARCHAR2(128)<BR>
 *
 */
public class ArmStgTxnDtlOracleBean extends BaseOracleBean {

	public ArmStgTxnDtlOracleBean() {
	}

	// changed by shushma for promotion code
	public static String selectSql = "select RECORD_TYPE, TRANSACTION_ID, LINE_ID, LINE_SEQ_NUM, LINE_ITEM_TYPE, DOC_NUM, MISC_ITEM_ID, MISC_ITEM_TXBL, ID_ITM, QUANTITY, ITM_RTL_PRICE, ITM_SEL_PRICE, MANUAL_MD_AMT, MANUAL_UNIT_PRICE, CONSULTANT_ID, ADD_CONSULTANT_ID, RETURN_REASON_ID, RETURN_COMMENTS, VAT_RATE, TAX_EXEMPT_ID, SHIP_STATE, SHIP_ZIP_CODE, NET_AMT, TAX_AMT, MANUAL_TAX_AMT, EXCEPTION_TAX_JUR, DEAL_MKDN_AMT, DEAL_ID, RTN_SALE_AI_TRN, RTN_ORIG_STORE_ID, RTN_ORIG_PROCESS_DT, RTN_ORIG_OPR_ID, RTN_ORIG_REGISTER_ID, ORIG_TRANSACTION_ID, ORIG_ADD_NO, REDUCTION_AMOUNT, REDUCTION_REASON, DSC_AMOUNT, DSC_PERCENT, DSC_REASON, DSC_EMPLOYEE_ID, DSC_ADVERTISING_CODE, DSC_IS_DSC_PERCENT, TND_TYPE, TND_CODE, TND_AMOUNT, TND_ID_ACNT_NMB, TND_RESP_AUTH, TND_APPROVAL_DATE, TND_EXPIRATION_DT, TND_HOLDER_NAME, TND_MANUAL_OVERRIDE, CHK_ABA_NUM, CHK_ROUTING_NUM, CHK_BANK, CHK_IS_SCANNED, CC_CVV_CODE, CC_AMEX_CID_NUM, RDM_CONTROL_NUMBER, TND_SWIPE_IND, TND_RESP_ADDRESS_VERIF, TND_RESP_JOURNAL_KEY, TND_RESP_MSG, TND_RESP_MSG_NUM, TND_MERCHANT_ID, TND_CARD_IDENTIFIER, CC_CARD_PLAN, RDM_TND_ID_STR_RT, RDM_DC_CPN_SCAN_CODE, RDM_TND_PRM_CODE, RDM_EXPIRATION_DATE, FC_XCHANGE_RATE, FC_FROM_CURRENCY, FC_TO_CURRENCY, RDM_TYPE, RDM_ISSUING_STORE, RDM_CUST_NUM, RDM_ISSUANCE_DT, RDM_EXPIRATION_DT, RDM_FACE_VALUE_AMT, RDM_BALANCE_AMT, RDM_BUYBACK_AMT, RDM_BUYBACK_COMMENT, POST_VOIDED_TRANS_NO, POST_VOID_REASON, POST_VOID_REGISTER, CUSTOMER_ROLE, CUST_ID, CUST_FIRST_NAME, CUST_LAST_NAME, CUST_ADDR_1, CUST_ADDR_2, CUST_CITY, CUST_STATE, CUST_COUNTRY, CUST_PCODE, CUST_PHONE_1, CUST_PHONE_2, CASHIER_TXN_COMMENT, CASHIER_TXN_REASON, OPENING_DRWR_FND, EOD_TND_TYPE, EOD_TND_TOTAL, NOT_IN_FILE_SKU_DTL, STATUS, DATA_POPULATION_DT, EXTRACTED_DT, DOC_TYPE, VAT_COMMENTS, ORIG_LINE_ITEM_TYPE, EOD_REPORTED_TND_TOTAL, EXTENDED_BARCODE, PROMOTION_CODE, APPROVER_ID, CD_TKN_NUMBER, CURRENCY_CODE, TRN_RCPT_DATA, CURRENCY_CONV_RATE, CUST_SIGNATURE, MRK, MRK_PER, RED_AMT2, DSC_PER2, PRZ_RETAIL, MOBILE_REGISTER_ID from ARM_STG_TXN_DTL ";
	public static String insertSql = "insert into ARM_STG_TXN_DTL (RECORD_TYPE, TRANSACTION_ID, LINE_ID, LINE_SEQ_NUM, LINE_ITEM_TYPE, DOC_NUM, MISC_ITEM_ID, MISC_ITEM_TXBL, ID_ITM, QUANTITY, ITM_RTL_PRICE, ITM_SEL_PRICE, MANUAL_MD_AMT, MANUAL_UNIT_PRICE, CONSULTANT_ID, ADD_CONSULTANT_ID, RETURN_REASON_ID, RETURN_COMMENTS, VAT_RATE, TAX_EXEMPT_ID, SHIP_STATE, SHIP_ZIP_CODE, NET_AMT, TAX_AMT, MANUAL_TAX_AMT, EXCEPTION_TAX_JUR, DEAL_MKDN_AMT, DEAL_ID, RTN_SALE_AI_TRN, RTN_ORIG_STORE_ID, RTN_ORIG_PROCESS_DT, RTN_ORIG_OPR_ID, RTN_ORIG_REGISTER_ID, ORIG_TRANSACTION_ID, ORIG_ADD_NO, REDUCTION_AMOUNT, REDUCTION_REASON, DSC_AMOUNT, DSC_PERCENT, DSC_REASON, DSC_EMPLOYEE_ID, DSC_ADVERTISING_CODE, DSC_IS_DSC_PERCENT, TND_TYPE, TND_CODE, TND_AMOUNT, TND_ID_ACNT_NMB, TND_RESP_AUTH, TND_APPROVAL_DATE, TND_EXPIRATION_DT, TND_HOLDER_NAME, TND_MANUAL_OVERRIDE, CHK_ABA_NUM, CHK_ROUTING_NUM, CHK_BANK, CHK_IS_SCANNED, CC_CVV_CODE, CC_AMEX_CID_NUM, RDM_CONTROL_NUMBER, TND_SWIPE_IND, TND_RESP_ADDRESS_VERIF, TND_RESP_JOURNAL_KEY, TND_RESP_MSG, TND_RESP_MSG_NUM, TND_MERCHANT_ID, TND_CARD_IDENTIFIER, CC_CARD_PLAN, RDM_TND_ID_STR_RT, RDM_DC_CPN_SCAN_CODE, RDM_TND_PRM_CODE, RDM_EXPIRATION_DATE, FC_XCHANGE_RATE, FC_FROM_CURRENCY, FC_TO_CURRENCY, RDM_TYPE, RDM_ISSUING_STORE, RDM_CUST_NUM, RDM_ISSUANCE_DT, RDM_EXPIRATION_DT, RDM_FACE_VALUE_AMT, RDM_BALANCE_AMT, RDM_BUYBACK_AMT, RDM_BUYBACK_COMMENT, POST_VOIDED_TRANS_NO, POST_VOID_REASON, POST_VOID_REGISTER, CUSTOMER_ROLE, CUST_ID, CUST_FIRST_NAME, CUST_LAST_NAME, CUST_ADDR_1, CUST_ADDR_2, CUST_CITY, CUST_STATE, CUST_COUNTRY, CUST_PCODE, CUST_PHONE_1, CUST_PHONE_2, CASHIER_TXN_COMMENT, CASHIER_TXN_REASON, OPENING_DRWR_FND, EOD_TND_TYPE, EOD_TND_TOTAL, NOT_IN_FILE_SKU_DTL, STATUS, DATA_POPULATION_DT, EXTRACTED_DT, DOC_TYPE, VAT_COMMENTS, ORIG_LINE_ITEM_TYPE, EOD_REPORTED_TND_TOTAL, EXTENDED_BARCODE, PROMOTION_CODE, APPROVER_ID, CD_TKN_NUMBER, CURRENCY_CODE, TRN_RCPT_DATA, CURRENCY_CONV_RATE, CUST_SIGNATURE, MRK, MRK_PER, RED_AMT2, DSC_PER2, PRZ_RETAIL, MOBILE_REGISTER_ID) values (?,?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
	public static String updateSql = "update ARM_STG_TXN_DTL set RECORD_TYPE = ?, TRANSACTION_ID = ?, LINE_ID = ?, LINE_SEQ_NUM = ?, LINE_ITEM_TYPE = ?, DOC_NUM = ?, MISC_ITEM_ID = ?, MISC_ITEM_TXBL = ?, ID_ITM = ?, QUANTITY = ?, ITM_RTL_PRICE = ?, ITM_SEL_PRICE = ?, MANUAL_MD_AMT = ?, MANUAL_UNIT_PRICE = ?, CONSULTANT_ID = ?, ADD_CONSULTANT_ID = ?, RETURN_REASON_ID = ?, RETURN_COMMENTS = ?, VAT_RATE = ?, TAX_EXEMPT_ID = ?, SHIP_STATE = ?, SHIP_ZIP_CODE = ?, NET_AMT = ?, TAX_AMT = ?, MANUAL_TAX_AMT = ?, EXCEPTION_TAX_JUR = ?, DEAL_MKDN_AMT = ?, DEAL_ID = ?, RTN_SALE_AI_TRN = ?, RTN_ORIG_STORE_ID = ?, RTN_ORIG_PROCESS_DT = ?, RTN_ORIG_OPR_ID = ?, RTN_ORIG_REGISTER_ID = ?, ORIG_TRANSACTION_ID = ?, ORIG_ADD_NO = ?, REDUCTION_AMOUNT = ?, REDUCTION_REASON = ?, DSC_AMOUNT = ?, DSC_PERCENT = ?, DSC_REASON = ?, DSC_EMPLOYEE_ID = ?, DSC_ADVERTISING_CODE = ?, DSC_IS_DSC_PERCENT = ?, TND_TYPE = ?, TND_CODE = ?, TND_AMOUNT = ?, TND_ID_ACNT_NMB = ?, TND_RESP_AUTH = ?, TND_APPROVAL_DATE = ?, TND_EXPIRATION_DT = ?, TND_HOLDER_NAME = ?, TND_MANUAL_OVERRIDE = ?, CHK_ABA_NUM = ?, CHK_ROUTING_NUM = ?, CHK_BANK = ?, CHK_IS_SCANNED = ?, CC_CVV_CODE = ?, CC_AMEX_CID_NUM = ?, RDM_CONTROL_NUMBER = ?, TND_SWIPE_IND = ?, TND_RESP_ADDRESS_VERIF = ?, TND_RESP_JOURNAL_KEY = ?, TND_RESP_MSG = ?, TND_RESP_MSG_NUM = ?, TND_MERCHANT_ID = ?, TND_CARD_IDENTIFIER = ?, CC_CARD_PLAN = ?, RDM_TND_ID_STR_RT = ?, RDM_DC_CPN_SCAN_CODE = ?, RDM_TND_PRM_CODE = ?, RDM_EXPIRATION_DATE = ?, FC_XCHANGE_RATE = ?, FC_FROM_CURRENCY = ?, FC_TO_CURRENCY = ?, RDM_TYPE = ?, RDM_ISSUING_STORE = ?, RDM_CUST_NUM = ?, RDM_ISSUANCE_DT = ?, RDM_EXPIRATION_DT = ?, RDM_FACE_VALUE_AMT = ?, RDM_BALANCE_AMT = ?, RDM_BUYBACK_AMT = ?, RDM_BUYBACK_COMMENT = ?, POST_VOIDED_TRANS_NO = ?, POST_VOID_REASON = ?, POST_VOID_REGISTER = ?, CUSTOMER_ROLE = ?, CUST_ID = ?, CUST_FIRST_NAME = ?, CUST_LAST_NAME = ?, CUST_ADDR_1 = ?, CUST_ADDR_2 = ?, CUST_CITY = ?, CUST_STATE = ?, CUST_COUNTRY = ?, CUST_PCODE = ?, CUST_PHONE_1 = ?, CUST_PHONE_2 = ?, CASHIER_TXN_COMMENT = ?, CASHIER_TXN_REASON = ?, OPENING_DRWR_FND = ?, EOD_TND_TYPE = ?, EOD_TND_TOTAL = ?, NOT_IN_FILE_SKU_DTL = ?, STATUS = ?, DATA_POPULATION_DT = ?, EXTRACTED_DT = ?, DOC_TYPE = ?, VAT_COMMENTS = ?, ORIG_LINE_ITEM_TYPE = ?, EOD_REPORTED_TND_TOTAL = ?, EXTENDED_BARCODE = ?, PROMOTION_CODE=?, APPROVER_ID = ?, CD_TKN_NUMBER=?, CURRENCY_CODE=?, TRN_RCPT_DATA=?, CURRENCY_CONV_RATE=?, CUST_SIGNATURE=?, MRK=?, MRK_PER=?, RED_AMT2=?, DSC_PER2=?, PRZ_RETAIL=? MOBILE_REGISTER_ID=?";
	public static String deleteSql = "delete from ARM_STG_TXN_DTL ";

	public static String TABLE_NAME = "ARM_STG_TXN_DTL";
	public static String COL_RECORD_TYPE = "ARM_STG_TXN_DTL.RECORD_TYPE";
	public static String COL_TRANSACTION_ID = "ARM_STG_TXN_DTL.TRANSACTION_ID";
	public static String COL_LINE_ID = "ARM_STG_TXN_DTL.LINE_ID";
	public static String COL_LINE_SEQ_NUM = "ARM_STG_TXN_DTL.LINE_SEQ_NUM";
	public static String COL_LINE_ITEM_TYPE = "ARM_STG_TXN_DTL.LINE_ITEM_TYPE";
	public static String COL_DOC_NUM = "ARM_STG_TXN_DTL.DOC_NUM";
	public static String COL_MISC_ITEM_ID = "ARM_STG_TXN_DTL.MISC_ITEM_ID";
	public static String COL_MISC_ITEM_TXBL = "ARM_STG_TXN_DTL.MISC_ITEM_TXBL";
	public static String COL_ID_ITM = "ARM_STG_TXN_DTL.ID_ITM";
	public static String COL_QUANTITY = "ARM_STG_TXN_DTL.QUANTITY";
	public static String COL_ITM_RTL_PRICE = "ARM_STG_TXN_DTL.ITM_RTL_PRICE";
	public static String COL_ITM_SEL_PRICE = "ARM_STG_TXN_DTL.ITM_SEL_PRICE";
	public static String COL_MANUAL_MD_AMT = "ARM_STG_TXN_DTL.MANUAL_MD_AMT";
	public static String COL_MANUAL_UNIT_PRICE = "ARM_STG_TXN_DTL.MANUAL_UNIT_PRICE";
	public static String COL_CONSULTANT_ID = "ARM_STG_TXN_DTL.CONSULTANT_ID";
	public static String COL_ADD_CONSULTANT_ID = "ARM_STG_TXN_DTL.ADD_CONSULTANT_ID";
	public static String COL_RETURN_REASON_ID = "ARM_STG_TXN_DTL.RETURN_REASON_ID";
	public static String COL_RETURN_COMMENTS = "ARM_STG_TXN_DTL.RETURN_COMMENTS";
	public static String COL_VAT_RATE = "ARM_STG_TXN_DTL.VAT_RATE";
	public static String COL_TAX_EXEMPT_ID = "ARM_STG_TXN_DTL.TAX_EXEMPT_ID";
	public static String COL_SHIP_STATE = "ARM_STG_TXN_DTL.SHIP_STATE";
	public static String COL_SHIP_ZIP_CODE = "ARM_STG_TXN_DTL.SHIP_ZIP_CODE";
	public static String COL_NET_AMT = "ARM_STG_TXN_DTL.NET_AMT";
	public static String COL_TAX_AMT = "ARM_STG_TXN_DTL.TAX_AMT";
	public static String COL_MANUAL_TAX_AMT = "ARM_STG_TXN_DTL.MANUAL_TAX_AMT";
	public static String COL_EXCEPTION_TAX_JUR = "ARM_STG_TXN_DTL.EXCEPTION_TAX_JUR";
	public static String COL_DEAL_MKDN_AMT = "ARM_STG_TXN_DTL.DEAL_MKDN_AMT";
	public static String COL_DEAL_ID = "ARM_STG_TXN_DTL.DEAL_ID";
	public static String COL_RTN_SALE_AI_TRN = "ARM_STG_TXN_DTL.RTN_SALE_AI_TRN";
	public static String COL_RTN_ORIG_STORE_ID = "ARM_STG_TXN_DTL.RTN_ORIG_STORE_ID";
	public static String COL_RTN_ORIG_PROCESS_DT = "ARM_STG_TXN_DTL.RTN_ORIG_PROCESS_DT";
	public static String COL_RTN_ORIG_OPR_ID = "ARM_STG_TXN_DTL.RTN_ORIG_OPR_ID";
	public static String COL_RTN_ORIG_REGISTER_ID = "ARM_STG_TXN_DTL.RTN_ORIG_REGISTER_ID";
	public static String COL_ORIG_TRANSACTION_ID = "ARM_STG_TXN_DTL.ORIG_TRANSACTION_ID";
	public static String COL_ORIG_ADD_NO = "ARM_STG_TXN_DTL.ORIG_ADD_NO";
	public static String COL_REDUCTION_AMOUNT = "ARM_STG_TXN_DTL.REDUCTION_AMOUNT";
	public static String COL_REDUCTION_REASON = "ARM_STG_TXN_DTL.REDUCTION_REASON";
	public static String COL_DSC_AMOUNT = "ARM_STG_TXN_DTL.DSC_AMOUNT";
	public static String COL_DSC_PERCENT = "ARM_STG_TXN_DTL.DSC_PERCENT";
	public static String COL_DSC_REASON = "ARM_STG_TXN_DTL.DSC_REASON";
	public static String COL_DSC_EMPLOYEE_ID = "ARM_STG_TXN_DTL.DSC_EMPLOYEE_ID";
	public static String COL_DSC_ADVERTISING_CODE = "ARM_STG_TXN_DTL.DSC_ADVERTISING_CODE";
	public static String COL_DSC_IS_DSC_PERCENT = "ARM_STG_TXN_DTL.DSC_IS_DSC_PERCENT";
	public static String COL_TND_TYPE = "ARM_STG_TXN_DTL.TND_TYPE";
	public static String COL_TND_CODE = "ARM_STG_TXN_DTL.TND_CODE";
	public static String COL_TND_AMOUNT = "ARM_STG_TXN_DTL.TND_AMOUNT";
	public static String COL_TND_ID_ACNT_NMB = "ARM_STG_TXN_DTL.TND_ID_ACNT_NMB";
	public static String COL_TND_RESP_AUTH = "ARM_STG_TXN_DTL.TND_RESP_AUTH";
	public static String COL_TND_APPROVAL_DATE = "ARM_STG_TXN_DTL.TND_APPROVAL_DATE";
	public static String COL_TND_EXPIRATION_DT = "ARM_STG_TXN_DTL.TND_EXPIRATION_DT";
	public static String COL_TND_HOLDER_NAME = "ARM_STG_TXN_DTL.TND_HOLDER_NAME";
	public static String COL_TND_MANUAL_OVERRIDE = "ARM_STG_TXN_DTL.TND_MANUAL_OVERRIDE";
	public static String COL_CHK_ABA_NUM = "ARM_STG_TXN_DTL.CHK_ABA_NUM";
	public static String COL_CHK_ROUTING_NUM = "ARM_STG_TXN_DTL.CHK_ROUTING_NUM";
	public static String COL_CHK_BANK = "ARM_STG_TXN_DTL.CHK_BANK";
	public static String COL_CHK_IS_SCANNED = "ARM_STG_TXN_DTL.CHK_IS_SCANNED";
	public static String COL_CC_CVV_CODE = "ARM_STG_TXN_DTL.CC_CVV_CODE";
	public static String COL_CC_AMEX_CID_NUM = "ARM_STG_TXN_DTL.CC_AMEX_CID_NUM";
	public static String COL_RDM_CONTROL_NUMBER = "ARM_STG_TXN_DTL.RDM_CONTROL_NUMBER";
	public static String COL_TND_SWIPE_IND = "ARM_STG_TXN_DTL.TND_SWIPE_IND";
	public static String COL_TND_RESP_ADDRESS_VERIF = "ARM_STG_TXN_DTL.TND_RESP_ADDRESS_VERIF";
	public static String COL_TND_RESP_JOURNAL_KEY = "ARM_STG_TXN_DTL.TND_RESP_JOURNAL_KEY";
	public static String COL_TND_RESP_MSG = "ARM_STG_TXN_DTL.TND_RESP_MSG";
	public static String COL_TND_RESP_MSG_NUM = "ARM_STG_TXN_DTL.TND_RESP_MSG_NUM";
	public static String COL_TND_MERCHANT_ID = "ARM_STG_TXN_DTL.TND_MERCHANT_ID";
	public static String COL_TND_CARD_IDENTIFIER = "ARM_STG_TXN_DTL.TND_CARD_IDENTIFIER";
	public static String COL_CC_CARD_PLAN = "ARM_STG_TXN_DTL.CC_CARD_PLAN";
	public static String COL_RDM_TND_ID_STR_RT = "ARM_STG_TXN_DTL.RDM_TND_ID_STR_RT";
	public static String COL_RDM_DC_CPN_SCAN_CODE = "ARM_STG_TXN_DTL.RDM_DC_CPN_SCAN_CODE";
	public static String COL_RDM_TND_PRM_CODE = "ARM_STG_TXN_DTL.RDM_TND_PRM_CODE";
	public static String COL_RDM_EXPIRATION_DATE = "ARM_STG_TXN_DTL.RDM_EXPIRATION_DATE";
	public static String COL_FC_XCHANGE_RATE = "ARM_STG_TXN_DTL.FC_XCHANGE_RATE";
	public static String COL_FC_FROM_CURRENCY = "ARM_STG_TXN_DTL.FC_FROM_CURRENCY";
	public static String COL_FC_TO_CURRENCY = "ARM_STG_TXN_DTL.FC_TO_CURRENCY";
	public static String COL_RDM_TYPE = "ARM_STG_TXN_DTL.RDM_TYPE";
	public static String COL_RDM_ISSUING_STORE = "ARM_STG_TXN_DTL.RDM_ISSUING_STORE";
	public static String COL_RDM_CUST_NUM = "ARM_STG_TXN_DTL.RDM_CUST_NUM";
	public static String COL_RDM_ISSUANCE_DT = "ARM_STG_TXN_DTL.RDM_ISSUANCE_DT";
	public static String COL_RDM_EXPIRATION_DT = "ARM_STG_TXN_DTL.RDM_EXPIRATION_DT";
	public static String COL_RDM_FACE_VALUE_AMT = "ARM_STG_TXN_DTL.RDM_FACE_VALUE_AMT";
	public static String COL_RDM_BALANCE_AMT = "ARM_STG_TXN_DTL.RDM_BALANCE_AMT";
	public static String COL_RDM_BUYBACK_AMT = "ARM_STG_TXN_DTL.RDM_BUYBACK_AMT";
	public static String COL_RDM_BUYBACK_COMMENT = "ARM_STG_TXN_DTL.RDM_BUYBACK_COMMENT";
	public static String COL_POST_VOIDED_TRANS_NO = "ARM_STG_TXN_DTL.POST_VOIDED_TRANS_NO";
	public static String COL_POST_VOID_REASON = "ARM_STG_TXN_DTL.POST_VOID_REASON";
	public static String COL_POST_VOID_REGISTER = "ARM_STG_TXN_DTL.POST_VOID_REGISTER";
	public static String COL_CUSTOMER_ROLE = "ARM_STG_TXN_DTL.CUSTOMER_ROLE";
	public static String COL_CUST_ID = "ARM_STG_TXN_DTL.CUST_ID";
	public static String COL_CUST_FIRST_NAME = "ARM_STG_TXN_DTL.CUST_FIRST_NAME";
	public static String COL_CUST_LAST_NAME = "ARM_STG_TXN_DTL.CUST_LAST_NAME";
	public static String COL_CUST_ADDR_1 = "ARM_STG_TXN_DTL.CUST_ADDR_1";
	public static String COL_CUST_ADDR_2 = "ARM_STG_TXN_DTL.CUST_ADDR_2";
	public static String COL_CUST_CITY = "ARM_STG_TXN_DTL.CUST_CITY";
	public static String COL_CUST_STATE = "ARM_STG_TXN_DTL.CUST_STATE";
	public static String COL_CUST_COUNTRY = "ARM_STG_TXN_DTL.CUST_COUNTRY";
	public static String COL_CUST_PCODE = "ARM_STG_TXN_DTL.CUST_PCODE";
	public static String COL_CUST_PHONE_1 = "ARM_STG_TXN_DTL.CUST_PHONE_1";
	public static String COL_CUST_PHONE_2 = "ARM_STG_TXN_DTL.CUST_PHONE_2";
	public static String COL_CASHIER_TXN_COMMENT = "ARM_STG_TXN_DTL.CASHIER_TXN_COMMENT";
	public static String COL_CASHIER_TXN_REASON = "ARM_STG_TXN_DTL.CASHIER_TXN_REASON";
	public static String COL_OPENING_DRWR_FND = "ARM_STG_TXN_DTL.OPENING_DRWR_FND";
	public static String COL_EOD_TND_TYPE = "ARM_STG_TXN_DTL.EOD_TND_TYPE";
	public static String COL_EOD_TND_TOTAL = "ARM_STG_TXN_DTL.EOD_TND_TOTAL";
	public static String COL_NOT_IN_FILE_SKU_DTL = "ARM_STG_TXN_DTL.NOT_IN_FILE_SKU_DTL";
	public static String COL_STATUS = "ARM_STG_TXN_DTL.STATUS";
	public static String COL_DATA_POPULATION_DT = "ARM_STG_TXN_DTL.DATA_POPULATION_DT";
	public static String COL_EXTRACTED_DT = "ARM_STG_TXN_DTL.EXTRACTED_DT";
	public static String COL_DOC_TYPE = "ARM_STG_TXN_DTL.DOC_TYPE";
	public static String COL_VAT_COMMENTS = "ARM_STG_TXN_DTL.VAT_COMMENTS";
	public static String COL_ORIG_LINE_ITEM_TYPE = "ARM_STG_TXN_DTL.ORIG_LINE_ITEM_TYPE";
	public static String COL_EOD_REPORTED_TND_TOTAL = "ARM_STG_TXN_DTL.EOD_REPORTED_TND_TOTAL";
	public static String COL_EXTENDED_BARCODE = "ARM_STG_TXN_DTL.EXTENDED_BARCODE";
	// added by shushma for promotion code
	public static String COL_PROMOTION_CODE = "ARM_STG_TXN_DTL.PROMOTION_CODE";
	// added by Rachana for approval of return transaction
	public static String COL_APPROVER_ID = "ARM_STG_TXN_DTL.APPROVER_ID";

	// Anjana added below columns to save same data in staging table as core
	// table:- tr_ltm_crdb_crd_tn
	public static String COL_LU_ACNT_CRD_TKN = "ARM_STG_TXN_DTL.CD_TKN_NUMBER";
	public static String COL_CD_CNYN = "ARM_STG_TXN_DTL.CURRENCY_CODE";
	public static String COL_TRN_RCPT_DATA = "ARM_STG_TXN_DTL.TRN_RCPT_DATA";
	public static String COL_CO_CNY_CONV_RATE = "ARM_STG_TXN_DTL.CURRENCY_CONV_RATE";
	public static String CUST_SIGNATURE = "ARM_STG_TXN_DTL.CUST_SIGNATURE";
	public static String COL_MRK = "ARM_STG_TXN_DTL.MRK";
	public static String COL_MRK_PER = "ARM_STG_TXN_DTL.MRK_PER";
	public static String COL_RED_AMT2 = "ARM_STG_TXN_DTL.RED_AMT2";
	public static String COL_DSC_PER2 = "ARM_STG_TXN_DTL.DSC_PER2";
	public static String COL_PRZ_RETAIL = "ARM_STG_TXN_DTL.PRZ_RETAIL";

	public static String COL_MOBILE_REGISTER_ID = "ARM_STG_TXN_DTL.MOBILE_REGISTER_ID";

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

	private Double mrk;
	private Double mrk_per;
	private Double red_amt2;
	private Double dsc_per2;
	private Double prz_Retail;
	private String recordType;
	private String transactionId;
	private Long lineId;
	private Long lineSeqNum;
	private String lineItemType;
	private String docNum;
	private String miscItemId;
	private String miscItemTxbl;
	private String idItm;
	private Long quantity;
	private Double itmRtlPrice;
	private Double itmSelPrice;
	private Double manualMdAmt;
	private Double manualUnitPrice;
	private String consultantId;
	private String addConsultantId;
	private String returnReasonId;
	private String returnComments;
	private Double vatRate;
	private String taxExemptId;
	private String shipState;
	private String shipZipCode;
	private Double netAmt;
	private Double taxAmt;
	private Double manualTaxAmt;
	private String exceptionTaxJur;
	private Double dealMkdnAmt;
	private String dealId;
	private String rtnSaleAiTrn;
	private String rtnOrigStoreId;
	private Date rtnOrigProcessDt;
	private String rtnOrigOprId;
	private String rtnOrigRegisterId;
	private String origTransactionId;
	private String origAddNo;
	private Double reductionAmount;
	private String reductionReason;
	private Double dscAmount;
	private Double dscPercent;
	private String dscReason;
	private String dscEmployeeId;
	private String dscAdvertisingCode;
	private String dscIsDscPercent;
	private String tndType;
	private String tndCode;
	private Double tndAmount;
	private String tndIdAcntNmb;
	private String tndRespAuth;
	private Date tndApprovalDate;
	private Date tndExpirationDt;
	private String tndHolderName;
	private String tndManualOverride;
	private String chkAbaNum;
	private String chkRoutingNum;
	private String chkBank;
	private String chkIsScanned;
	private String ccCvvCode;
	private String ccAmexCidNum;
	private String rdmControlNumber;
	private String tndSwipeInd;
	private String tndRespAddressVerif;
	private String tndRespJournalKey;
	private String tndRespMsg;
	private String tndRespMsgNum;
	private String tndMerchantId;
	private String tndCardIdentifier;
	private String ccCardPlan;
	private String rdmTndIdStrRt;
	private String rdmDcCpnScanCode;
	private String rdmTndPrmCode;
	private Date rdmExpirationDate;
	private Double fcXchangeRate;
	private String fcFromCurrency;
	private String fcToCurrency;
	private String rdmType;
	private String rdmIssuingStore;
	private String rdmCustNum;
	private Date rdmIssuanceDt;
	private Date rdmExpirationDt;
	private Double rdmFaceValueAmt;
	private Double rdmBalanceAmt;
	private Double rdmBuybackAmt;
	private String rdmBuybackComment;
	private String postVoidedTransNo;
	private String postVoidReason;
	private String postVoidRegister;
	private String customerRole;
	private String custId;
	private String custFirstName;
	private String custLastName;
	private String custAddr1;
	private String custAddr2;
	private String custCity;
	private String custState;
	private String custCountry;
	private String custPcode;
	private String custPhone1;
	private String custPhone2;
	private String cashierTxnComment;
	private String cashierTxnReason;
	private Double openingDrwrFnd;
	private String eodTndType;
	private String eodTndTotal;
	private String notInFileSkuDtl;
	private String status;
	private Date dataPopulationDt;
	private Date extractedDt;
	private String docType;
	private String vatComments;
	private String origLineItemType;
	private String eodReportedTndTotal;
	private String extendedBarcode;
	// added by shushma for promotion code
	private String promotionCode;
	// added by Rachana for approval of return transaction
	private String approverId;
	// Anjana added below columns to save same data in staging table as core
	// table:- tr_ltm_crdb_crd_tn
	private String cardToken;
	private String currencyCode;
	private String recieptData;
	private String convRate;
	private Object custSignature;

	private String mobileRegisterId;

	public String getPromotionCode() {
		return promotionCode;
	}

	public void setPromotionCode(String promotionCode) {
		this.promotionCode = promotionCode;
	}

	// promotion code closed

	public String getRecordType() {
		return this.recordType;
	}

	public Double getMrk() {
		return mrk;
	}

	public void setMrk(Double mrk) {
		this.mrk = mrk;
	}

	public Double getMrk_per() {
		return mrk_per;
	}

	public void setMrk_per(Double mrk_per) {
		this.mrk_per = mrk_per;
	}

	public Double getRed_amt2() {
		return red_amt2;
	}

	public void setRed_amt2(Double red_amt2) {
		this.red_amt2 = red_amt2;
	}

	public Double getDsc_per2() {
		return dsc_per2;
	}

	public void setDsc_per2(Double dsc_per2) {
		this.dsc_per2 = dsc_per2;
	}

	public Double getPrz_Retail() {
		return prz_Retail;
	}

	public void setPrz_Retail(Double prz_Retail) {
		this.prz_Retail = prz_Retail;
	}

	public void setRecordType(String recordType) {
		this.recordType = recordType;
	}

	public String getTransactionId() {
		return this.transactionId;
	}

	public void setTransactionId(String transactionId) {
		this.transactionId = transactionId;
	}

	public Long getLineId() {
		return this.lineId;
	}

	public void setLineId(Long lineId) {
		this.lineId = lineId;
	}

	public void setLineId(long lineId) {
		this.lineId = new Long(lineId);
	}

	public void setLineId(int lineId) {
		this.lineId = new Long((long) lineId);
	}

	public Long getLineSeqNum() {
		return this.lineSeqNum;
	}

	public void setLineSeqNum(Long lineSeqNum) {
		this.lineSeqNum = lineSeqNum;
	}

	public void setLineSeqNum(long lineSeqNum) {
		this.lineSeqNum = new Long(lineSeqNum);
	}

	public void setLineSeqNum(int lineSeqNum) {
		this.lineSeqNum = new Long((long) lineSeqNum);
	}

	public String getLineItemType() {
		return this.lineItemType;
	}

	public void setLineItemType(String lineItemType) {
		this.lineItemType = lineItemType;
	}

	public String getDocNum() {
		return this.docNum;
	}

	public void setDocNum(String docNum) {
		this.docNum = docNum;
	}

	public String getMiscItemId() {
		return this.miscItemId;
	}

	public void setMiscItemId(String miscItemId) {
		this.miscItemId = miscItemId;
	}

	public String getMiscItemTxbl() {
		return this.miscItemTxbl;
	}

	public void setMiscItemTxbl(String miscItemTxbl) {
		this.miscItemTxbl = miscItemTxbl;
	}

	public String getIdItm() {
		return this.idItm;
	}

	public void setIdItm(String idItm) {
		this.idItm = idItm;
	}

	public Long getQuantity() {
		return this.quantity;
	}

	public void setQuantity(Long quantity) {
		this.quantity = quantity;
	}

	public void setQuantity(long quantity) {
		this.quantity = new Long(quantity);
	}

	public void setQuantity(int quantity) {
		this.quantity = new Long((long) quantity);
	}

	public Double getItmRtlPrice() {
		return this.itmRtlPrice;
	}

	public void setItmRtlPrice(Double itmRtlPrice) {
		this.itmRtlPrice = itmRtlPrice;
	}

	public void setItmRtlPrice(double itmRtlPrice) {
		this.itmRtlPrice = new Double(itmRtlPrice);
	}

	public Double getItmSelPrice() {
		return this.itmSelPrice;
	}

	public void setItmSelPrice(Double itmSelPrice) {
		this.itmSelPrice = itmSelPrice;
	}

	public void setItmSelPrice(double itmSelPrice) {
		this.itmSelPrice = new Double(itmSelPrice);
	}

	public Double getManualMdAmt() {
		return this.manualMdAmt;
	}

	public void setManualMdAmt(Double manualMdAmt) {
		this.manualMdAmt = manualMdAmt;
	}

	public void setManualMdAmt(double manualMdAmt) {
		this.manualMdAmt = new Double(manualMdAmt);
	}

	public Double getManualUnitPrice() {
		return this.manualUnitPrice;
	}

	public void setManualUnitPrice(Double manualUnitPrice) {
		this.manualUnitPrice = manualUnitPrice;
	}

	public void setManualUnitPrice(double manualUnitPrice) {
		this.manualUnitPrice = new Double(manualUnitPrice);
	}

	public String getConsultantId() {
		return this.consultantId;
	}

	public void setConsultantId(String consultantId) {
		this.consultantId = consultantId;
	}

	public String getAddConsultantId() {
		return this.addConsultantId;
	}

	public void setAddConsultantId(String addConsultantId) {
		this.addConsultantId = addConsultantId;
	}

	public String getReturnReasonId() {
		return this.returnReasonId;
	}

	public void setReturnReasonId(String returnReasonId) {
		this.returnReasonId = returnReasonId;
	}

	public String getReturnComments() {
		return this.returnComments;
	}

	public void setReturnComments(String returnComments) {
		this.returnComments = returnComments;
	}

	public Double getVatRate() {
		return this.vatRate;
	}

	public void setVatRate(Double vatRate) {
		this.vatRate = vatRate;
	}

	public void setVatRate(double vatRate) {
		this.vatRate = new Double(vatRate);
	}

	public String getTaxExemptId() {
		return this.taxExemptId;
	}

	public void setTaxExemptId(String taxExemptId) {
		this.taxExemptId = taxExemptId;
	}

	public String getShipState() {
		return this.shipState;
	}

	public void setShipState(String shipState) {
		this.shipState = shipState;
	}

	public String getShipZipCode() {
		return this.shipZipCode;
	}

	public void setShipZipCode(String shipZipCode) {
		this.shipZipCode = shipZipCode;
	}

	public Double getNetAmt() {
		return this.netAmt;
	}

	public void setNetAmt(Double netAmt) {
		this.netAmt = netAmt;
	}

	public void setNetAmt(double netAmt) {
		this.netAmt = new Double(netAmt);
	}

	public Double getTaxAmt() {
		return this.taxAmt;
	}

	public void setTaxAmt(Double taxAmt) {
		this.taxAmt = taxAmt;
	}

	public void setTaxAmt(double taxAmt) {
		this.taxAmt = new Double(taxAmt);
	}

	public Double getManualTaxAmt() {
		return this.manualTaxAmt;
	}

	public void setManualTaxAmt(Double manualTaxAmt) {
		this.manualTaxAmt = manualTaxAmt;
	}

	public void setManualTaxAmt(double manualTaxAmt) {
		this.manualTaxAmt = new Double(manualTaxAmt);
	}

	public String getExceptionTaxJur() {
		return this.exceptionTaxJur;
	}

	public void setExceptionTaxJur(String exceptionTaxJur) {
		this.exceptionTaxJur = exceptionTaxJur;
	}

	public Double getDealMkdnAmt() {
		return this.dealMkdnAmt;
	}

	public void setDealMkdnAmt(Double dealMkdnAmt) {
		this.dealMkdnAmt = dealMkdnAmt;
	}

	public void setDealMkdnAmt(double dealMkdnAmt) {
		this.dealMkdnAmt = new Double(dealMkdnAmt);
	}

	public String getDealId() {
		return this.dealId;
	}

	public void setDealId(String dealId) {
		this.dealId = dealId;
	}

	public String getRtnSaleAiTrn() {
		return this.rtnSaleAiTrn;
	}

	public void setRtnSaleAiTrn(String rtnSaleAiTrn) {
		this.rtnSaleAiTrn = rtnSaleAiTrn;
	}

	public String getRtnOrigStoreId() {
		return this.rtnOrigStoreId;
	}

	public void setRtnOrigStoreId(String rtnOrigStoreId) {
		this.rtnOrigStoreId = rtnOrigStoreId;
	}

	public Date getRtnOrigProcessDt() {
		return this.rtnOrigProcessDt;
	}

	public void setRtnOrigProcessDt(Date rtnOrigProcessDt) {
		this.rtnOrigProcessDt = rtnOrigProcessDt;
	}

	public String getRtnOrigOprId() {
		return this.rtnOrigOprId;
	}

	public void setRtnOrigOprId(String rtnOrigOprId) {
		this.rtnOrigOprId = rtnOrigOprId;
	}

	public String getRtnOrigRegisterId() {
		return this.rtnOrigRegisterId;
	}

	public void setRtnOrigRegisterId(String rtnOrigRegisterId) {
		this.rtnOrigRegisterId = rtnOrigRegisterId;
	}

	public String getOrigTransactionId() {
		return this.origTransactionId;
	}

	public void setOrigTransactionId(String origTransactionId) {
		this.origTransactionId = origTransactionId;
	}

	public String getOrigAddNo() {
		return this.origAddNo;
	}

	public void setOrigAddNo(String origAddNo) {
		this.origAddNo = origAddNo;
	}

	public Double getReductionAmount() {
		return this.reductionAmount;
	}

	public void setReductionAmount(Double reductionAmount) {
		this.reductionAmount = reductionAmount;
	}

	public void setReductionAmount(double reductionAmount) {
		this.reductionAmount = new Double(reductionAmount);
	}

	public String getReductionReason() {
		return this.reductionReason;
	}

	public void setReductionReason(String reductionReason) {
		this.reductionReason = reductionReason;
	}

	public Double getDscAmount() {
		return this.dscAmount;
	}

	public void setDscAmount(Double dscAmount) {
		this.dscAmount = dscAmount;
	}

	public void setDscAmount(double dscAmount) {
		this.dscAmount = new Double(dscAmount);
	}

	public Double getDscPercent() {
		return this.dscPercent;
	}

	public void setDscPercent(Double dscPercent) {
		this.dscPercent = dscPercent;
	}

	public void setDscPercent(double dscPercent) {
		this.dscPercent = new Double(dscPercent);
	}

	public String getDscReason() {
		return this.dscReason;
	}

	public void setDscReason(String dscReason) {
		this.dscReason = dscReason;
	}

	public String getDscEmployeeId() {
		return this.dscEmployeeId;
	}

	public void setDscEmployeeId(String dscEmployeeId) {
		this.dscEmployeeId = dscEmployeeId;
	}

	public String getDscAdvertisingCode() {
		return this.dscAdvertisingCode;
	}

	public void setDscAdvertisingCode(String dscAdvertisingCode) {
		this.dscAdvertisingCode = dscAdvertisingCode;
	}

	public String getDscIsDscPercent() {
		return this.dscIsDscPercent;
	}

	public void setDscIsDscPercent(String dscIsDscPercent) {
		this.dscIsDscPercent = dscIsDscPercent;
	}

	public String getTndType() {
		return this.tndType;
	}

	public void setTndType(String tndType) {
		this.tndType = tndType;
	}

	public String getTndCode() {
		return this.tndCode;
	}

	public void setTndCode(String tndCode) {
		this.tndCode = tndCode;
	}

	public Double getTndAmount() {
		return this.tndAmount;
	}

	public void setTndAmount(Double tndAmount) {
		this.tndAmount = tndAmount;
	}

	public void setTndAmount(double tndAmount) {
		this.tndAmount = new Double(tndAmount);
	}

	public String getTndIdAcntNmb() {
		return this.tndIdAcntNmb;
	}

	public void setTndIdAcntNmb(String tndIdAcntNmb) {
		this.tndIdAcntNmb = tndIdAcntNmb;
	}

	public String getTndRespAuth() {
		return this.tndRespAuth;
	}

	public void setTndRespAuth(String tndRespAuth) {
		this.tndRespAuth = tndRespAuth;
	}

	public Date getTndApprovalDate() {
		return this.tndApprovalDate;
	}

	public void setTndApprovalDate(Date tndApprovalDate) {
		this.tndApprovalDate = tndApprovalDate;
	}

	public Date getTndExpirationDt() {
		return this.tndExpirationDt;
	}

	public void setTndExpirationDt(Date tndExpirationDt) {
		this.tndExpirationDt = tndExpirationDt;
	}

	public String getTndHolderName() {
		return this.tndHolderName;
	}

	public void setTndHolderName(String tndHolderName) {
		this.tndHolderName = tndHolderName;
	}

	public String getTndManualOverride() {
		return this.tndManualOverride;
	}

	public void setTndManualOverride(String tndManualOverride) {
		this.tndManualOverride = tndManualOverride;
	}

	public String getChkAbaNum() {
		return this.chkAbaNum;
	}

	public void setChkAbaNum(String chkAbaNum) {
		this.chkAbaNum = chkAbaNum;
	}

	public String getChkRoutingNum() {
		return this.chkRoutingNum;
	}

	public void setChkRoutingNum(String chkRoutingNum) {
		this.chkRoutingNum = chkRoutingNum;
	}

	public String getChkBank() {
		return this.chkBank;
	}

	public void setChkBank(String chkBank) {
		this.chkBank = chkBank;
	}

	public String getChkIsScanned() {
		return this.chkIsScanned;
	}

	public void setChkIsScanned(String chkIsScanned) {
		this.chkIsScanned = chkIsScanned;
	}

	public String getCcCvvCode() {
		return this.ccCvvCode;
	}

	public void setCcCvvCode(String ccCvvCode) {
		this.ccCvvCode = ccCvvCode;
	}

	public String getCcAmexCidNum() {
		return this.ccAmexCidNum;
	}

	public void setCcAmexCidNum(String ccAmexCidNum) {
		this.ccAmexCidNum = ccAmexCidNum;
	}

	public String getRdmControlNumber() {
		return this.rdmControlNumber;
	}

	public void setRdmControlNumber(String rdmControlNumber) {
		this.rdmControlNumber = rdmControlNumber;
	}

	public String getTndSwipeInd() {
		return this.tndSwipeInd;
	}

	public void setTndSwipeInd(String tndSwipeInd) {
		this.tndSwipeInd = tndSwipeInd;
	}

	public String getTndRespAddressVerif() {
		return this.tndRespAddressVerif;
	}

	public void setTndRespAddressVerif(String tndRespAddressVerif) {
		this.tndRespAddressVerif = tndRespAddressVerif;
	}

	public String getTndRespJournalKey() {
		return this.tndRespJournalKey;
	}

	public void setTndRespJournalKey(String tndRespJournalKey) {
		this.tndRespJournalKey = tndRespJournalKey;
	}

	public String getTndRespMsg() {
		return this.tndRespMsg;
	}

	public void setTndRespMsg(String tndRespMsg) {
		this.tndRespMsg = tndRespMsg;
	}

	public String getTndRespMsgNum() {
		return this.tndRespMsgNum;
	}

	public void setTndRespMsgNum(String tndRespMsgNum) {
		this.tndRespMsgNum = tndRespMsgNum;
	}

	public String getTndMerchantId() {
		return this.tndMerchantId;
	}

	public void setTndMerchantId(String tndMerchantId) {
		this.tndMerchantId = tndMerchantId;
	}

	public String getTndCardIdentifier() {
		return this.tndCardIdentifier;
	}

	public void setTndCardIdentifier(String tndCardIdentifier) {
		this.tndCardIdentifier = tndCardIdentifier;
	}

	public String getCcCardPlan() {
		return this.ccCardPlan;
	}

	public void setCcCardPlan(String ccCardPlan) {
		this.ccCardPlan = ccCardPlan;
	}

	public String getRdmTndIdStrRt() {
		return this.rdmTndIdStrRt;
	}

	public void setRdmTndIdStrRt(String rdmTndIdStrRt) {
		this.rdmTndIdStrRt = rdmTndIdStrRt;
	}

	public String getRdmDcCpnScanCode() {
		return this.rdmDcCpnScanCode;
	}

	public void setRdmDcCpnScanCode(String rdmDcCpnScanCode) {
		this.rdmDcCpnScanCode = rdmDcCpnScanCode;
	}

	public String getRdmTndPrmCode() {
		return this.rdmTndPrmCode;
	}

	public void setRdmTndPrmCode(String rdmTndPrmCode) {
		this.rdmTndPrmCode = rdmTndPrmCode;
	}

	public Date getRdmExpirationDate() {
		return this.rdmExpirationDate;
	}

	public void setRdmExpirationDate(Date rdmExpirationDate) {
		this.rdmExpirationDate = rdmExpirationDate;
	}

	public Double getFcXchangeRate() {
		return this.fcXchangeRate;
	}

	public void setFcXchangeRate(Double fcXchangeRate) {
		this.fcXchangeRate = fcXchangeRate;
	}

	public void setFcXchangeRate(double fcXchangeRate) {
		this.fcXchangeRate = new Double(fcXchangeRate);
	}

	public String getFcFromCurrency() {
		return this.fcFromCurrency;
	}

	public void setFcFromCurrency(String fcFromCurrency) {
		this.fcFromCurrency = fcFromCurrency;
	}

	public String getFcToCurrency() {
		return this.fcToCurrency;
	}

	public void setFcToCurrency(String fcToCurrency) {
		this.fcToCurrency = fcToCurrency;
	}

	public String getRdmType() {
		return this.rdmType;
	}

	public void setRdmType(String rdmType) {
		this.rdmType = rdmType;
	}

	public String getRdmIssuingStore() {
		return this.rdmIssuingStore;
	}

	public void setRdmIssuingStore(String rdmIssuingStore) {
		this.rdmIssuingStore = rdmIssuingStore;
	}

	public String getRdmCustNum() {
		return this.rdmCustNum;
	}

	public void setRdmCustNum(String rdmCustNum) {
		this.rdmCustNum = rdmCustNum;
	}

	public Date getRdmIssuanceDt() {
		return this.rdmIssuanceDt;
	}

	public void setRdmIssuanceDt(Date rdmIssuanceDt) {
		this.rdmIssuanceDt = rdmIssuanceDt;
	}

	public Date getRdmExpirationDt() {
		return this.rdmExpirationDt;
	}

	public void setRdmExpirationDt(Date rdmExpirationDt) {
		this.rdmExpirationDt = rdmExpirationDt;
	}

	public Double getRdmFaceValueAmt() {
		return this.rdmFaceValueAmt;
	}

	public void setRdmFaceValueAmt(Double rdmFaceValueAmt) {
		this.rdmFaceValueAmt = rdmFaceValueAmt;
	}

	public void setRdmFaceValueAmt(double rdmFaceValueAmt) {
		this.rdmFaceValueAmt = new Double(rdmFaceValueAmt);
	}

	public Double getRdmBalanceAmt() {
		return this.rdmBalanceAmt;
	}

	public void setRdmBalanceAmt(Double rdmBalanceAmt) {
		this.rdmBalanceAmt = rdmBalanceAmt;
	}

	public void setRdmBalanceAmt(double rdmBalanceAmt) {
		this.rdmBalanceAmt = new Double(rdmBalanceAmt);
	}

	public Double getRdmBuybackAmt() {
		return this.rdmBuybackAmt;
	}

	public void setRdmBuybackAmt(Double rdmBuybackAmt) {
		this.rdmBuybackAmt = rdmBuybackAmt;
	}

	public void setRdmBuybackAmt(double rdmBuybackAmt) {
		this.rdmBuybackAmt = new Double(rdmBuybackAmt);
	}

	public String getRdmBuybackComment() {
		return this.rdmBuybackComment;
	}

	public void setRdmBuybackComment(String rdmBuybackComment) {
		this.rdmBuybackComment = rdmBuybackComment;
	}

	public String getPostVoidedTransNo() {
		return this.postVoidedTransNo;
	}

	public void setPostVoidedTransNo(String postVoidedTransNo) {
		this.postVoidedTransNo = postVoidedTransNo;
	}

	public String getPostVoidReason() {
		return this.postVoidReason;
	}

	public void setPostVoidReason(String postVoidReason) {
		this.postVoidReason = postVoidReason;
	}

	public String getPostVoidRegister() {
		return this.postVoidRegister;
	}

	public void setPostVoidRegister(String postVoidRegister) {
		this.postVoidRegister = postVoidRegister;
	}

	public String getCustomerRole() {
		return this.customerRole;
	}

	public void setCustomerRole(String customerRole) {
		this.customerRole = customerRole;
	}

	public String getCustId() {
		return this.custId;
	}

	public void setCustId(String custId) {
		this.custId = custId;
	}

	public String getCustFirstName() {
		return this.custFirstName;
	}

	public void setCustFirstName(String custFirstName) {
		this.custFirstName = custFirstName;
	}

	public String getCustLastName() {
		return this.custLastName;
	}

	public void setCustLastName(String custLastName) {
		this.custLastName = custLastName;
	}

	public String getCustAddr1() {
		return this.custAddr1;
	}

	public void setCustAddr1(String custAddr1) {
		this.custAddr1 = custAddr1;
	}

	public String getCustAddr2() {
		return this.custAddr2;
	}

	public void setCustAddr2(String custAddr2) {
		this.custAddr2 = custAddr2;
	}

	public String getCustCity() {
		return this.custCity;
	}

	public void setCustCity(String custCity) {
		this.custCity = custCity;
	}

	public String getCustState() {
		return this.custState;
	}

	public void setCustState(String custState) {
		this.custState = custState;
	}

	public String getCustCountry() {
		return this.custCountry;
	}

	public void setCustCountry(String custCountry) {
		this.custCountry = custCountry;
	}

	public String getCustPcode() {
		return this.custPcode;
	}

	public void setCustPcode(String custPcode) {
		this.custPcode = custPcode;
	}

	public String getCustPhone1() {
		return this.custPhone1;
	}

	public void setCustPhone1(String custPhone1) {
		this.custPhone1 = custPhone1;
	}

	public String getCustPhone2() {
		return this.custPhone2;
	}

	public void setCustPhone2(String custPhone2) {
		this.custPhone2 = custPhone2;
	}

	public String getCashierTxnComment() {
		return this.cashierTxnComment;
	}

	public void setCashierTxnComment(String cashierTxnComment) {
		this.cashierTxnComment = cashierTxnComment;
	}

	public String getCashierTxnReason() {
		return this.cashierTxnReason;
	}

	public void setCashierTxnReason(String cashierTxnReason) {
		this.cashierTxnReason = cashierTxnReason;
	}

	public Double getOpeningDrwrFnd() {
		return this.openingDrwrFnd;
	}

	public void setOpeningDrwrFnd(Double openingDrwrFnd) {
		this.openingDrwrFnd = openingDrwrFnd;
	}

	public void setOpeningDrwrFnd(double openingDrwrFnd) {
		this.openingDrwrFnd = new Double(openingDrwrFnd);
	}

	public String getEodTndType() {
		return this.eodTndType;
	}

	public void setEodTndType(String eodTndType) {
		this.eodTndType = eodTndType;
	}

	public String getEodTndTotal() {
		return this.eodTndTotal;
	}

	public void setEodTndTotal(String eodTndTotal) {
		this.eodTndTotal = eodTndTotal;
	}

	public String getNotInFileSkuDtl() {
		return this.notInFileSkuDtl;
	}

	public void setNotInFileSkuDtl(String notInFileSkuDtl) {
		this.notInFileSkuDtl = notInFileSkuDtl;
	}

	public String getStatus() {
		return this.status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public Date getDataPopulationDt() {
		return this.dataPopulationDt;
	}

	public void setDataPopulationDt(Date dataPopulationDt) {
		this.dataPopulationDt = dataPopulationDt;
	}

	public Date getExtractedDt() {
		return this.extractedDt;
	}

	public void setExtractedDt(Date extractedDt) {
		this.extractedDt = extractedDt;
	}

	public String getDocType() {
		return this.docType;
	}

	public void setDocType(String docType) {
		this.docType = docType;
	}

	public String getVatComments() {
		return this.vatComments;
	}

	public void setVatComments(String vatComments) {
		this.vatComments = vatComments;
	}

	public String getOrigLineItemType() {
		return this.origLineItemType;
	}

	public void setOrigLineItemType(String origLineItemType) {
		this.origLineItemType = origLineItemType;
	}

	public String getEodReportedTndTotal() {
		return this.eodReportedTndTotal;
	}

	public void setEodReportedTndTotal(String eodReportedTndTotal) {
		this.eodReportedTndTotal = eodReportedTndTotal;
	}

	public String getExtendedBarcode() {
		return this.extendedBarcode;
	}

	public void setExtendedBarcode(String extendedBarcode) {
		this.extendedBarcode = extendedBarcode;
	}

	// added by Rachana for approval of return transaction
	public String getApproverId() {
		return this.approverId;
	}

	public void setApproverId(String approverId) {
		this.approverId = approverId;
	}

	// Anjana added below columns to save same data in staging table as core
	// table:- tr_ltm_crdb_crd_tn
	public String getCardToken() {
		return cardToken;
	}

	public void setCardToken(String cardToken) {
		this.cardToken = cardToken;
	}

	public String getCurrencyCode() {
		return currencyCode;
	}

	public void setCurrencyCode(String currencyCode) {
		this.currencyCode = currencyCode;
	}

	public String getRecieptData() {
		return recieptData;
	}

	public void setRecieptData(String recieptData) {
		this.recieptData = recieptData;
	}

	public String getConvRate() {
		return convRate;
	}

	public void setConvRate(String convRate) {
		this.convRate = convRate;
	}

	public Object getCustSignature() {
		return custSignature;
	}

	public void setCustSignature(Object custSignature) {
		this.custSignature = custSignature;
	}

	public String getMobileRegisterId() {
		return mobileRegisterId;
	}

	public void setMobileRegisterId(String mobileRegisterId) {
		this.mobileRegisterId = mobileRegisterId;
	}

	public BaseOracleBean[] getDatabeans(ResultSet rs) throws SQLException {
		ArrayList list = new ArrayList();
		while (rs.next()) {
			ArmStgTxnDtlOracleBean bean = new ArmStgTxnDtlOracleBean();
			bean.recordType = getStringFromResultSet(rs, "RECORD_TYPE");
			bean.transactionId = getStringFromResultSet(rs, "TRANSACTION_ID");
			bean.lineId = getLongFromResultSet(rs, "LINE_ID");
			bean.lineSeqNum = getLongFromResultSet(rs, "LINE_SEQ_NUM");
			bean.lineItemType = getStringFromResultSet(rs, "LINE_ITEM_TYPE");
			bean.docNum = getStringFromResultSet(rs, "DOC_NUM");
			bean.miscItemId = getStringFromResultSet(rs, "MISC_ITEM_ID");
			bean.miscItemTxbl = getStringFromResultSet(rs, "MISC_ITEM_TXBL");
			bean.idItm = getStringFromResultSet(rs, "ID_ITM");
			bean.quantity = getLongFromResultSet(rs, "QUANTITY");
			bean.itmRtlPrice = getDoubleFromResultSet(rs, "ITM_RTL_PRICE");
			bean.itmSelPrice = getDoubleFromResultSet(rs, "ITM_SEL_PRICE");
			bean.manualMdAmt = getDoubleFromResultSet(rs, "MANUAL_MD_AMT");
			bean.manualUnitPrice = getDoubleFromResultSet(rs,
					"MANUAL_UNIT_PRICE");
			bean.consultantId = getStringFromResultSet(rs, "CONSULTANT_ID");
			bean.addConsultantId = getStringFromResultSet(rs,
					"ADD_CONSULTANT_ID");
			bean.returnReasonId = getStringFromResultSet(rs, "RETURN_REASON_ID");
			bean.returnComments = getStringFromResultSet(rs, "RETURN_COMMENTS");
			bean.vatRate = getDoubleFromResultSet(rs, "VAT_RATE");
			bean.taxExemptId = getStringFromResultSet(rs, "TAX_EXEMPT_ID");
			bean.shipState = getStringFromResultSet(rs, "SHIP_STATE");
			bean.shipZipCode = getStringFromResultSet(rs, "SHIP_ZIP_CODE");
			bean.netAmt = getDoubleFromResultSet(rs, "NET_AMT");
			bean.taxAmt = getDoubleFromResultSet(rs, "TAX_AMT");
			bean.manualTaxAmt = getDoubleFromResultSet(rs, "MANUAL_TAX_AMT");
			bean.exceptionTaxJur = getStringFromResultSet(rs,
					"EXCEPTION_TAX_JUR");
			bean.dealMkdnAmt = getDoubleFromResultSet(rs, "DEAL_MKDN_AMT");
			bean.dealId = getStringFromResultSet(rs, "DEAL_ID");
			bean.rtnSaleAiTrn = getStringFromResultSet(rs, "RTN_SALE_AI_TRN");
			bean.rtnOrigStoreId = getStringFromResultSet(rs,
					"RTN_ORIG_STORE_ID");
			bean.rtnOrigProcessDt = getDateFromResultSet(rs,
					"RTN_ORIG_PROCESS_DT");
			bean.rtnOrigOprId = getStringFromResultSet(rs, "RTN_ORIG_OPR_ID");
			bean.rtnOrigRegisterId = getStringFromResultSet(rs,
					"RTN_ORIG_REGISTER_ID");
			bean.origTransactionId = getStringFromResultSet(rs,
					"ORIG_TRANSACTION_ID");
			bean.origAddNo = getStringFromResultSet(rs, "ORIG_ADD_NO");
			bean.reductionAmount = getDoubleFromResultSet(rs,
					"REDUCTION_AMOUNT");
			bean.reductionReason = getStringFromResultSet(rs,
					"REDUCTION_REASON");
			bean.dscAmount = getDoubleFromResultSet(rs, "DSC_AMOUNT");
			bean.dscPercent = getDoubleFromResultSet(rs, "DSC_PERCENT");
			bean.dscReason = getStringFromResultSet(rs, "DSC_REASON");
			bean.dscEmployeeId = getStringFromResultSet(rs, "DSC_EMPLOYEE_ID");
			bean.dscAdvertisingCode = getStringFromResultSet(rs,
					"DSC_ADVERTISING_CODE");
			bean.dscIsDscPercent = getStringFromResultSet(rs,
					"DSC_IS_DSC_PERCENT");
			bean.tndType = getStringFromResultSet(rs, "TND_TYPE");
			bean.tndCode = getStringFromResultSet(rs, "TND_CODE");
			bean.tndAmount = getDoubleFromResultSet(rs, "TND_AMOUNT");
			bean.tndIdAcntNmb = getStringFromResultSet(rs, "TND_ID_ACNT_NMB");
			bean.tndRespAuth = getStringFromResultSet(rs, "TND_RESP_AUTH");
			bean.tndApprovalDate = getDateFromResultSet(rs, "TND_APPROVAL_DATE");
			bean.tndExpirationDt = getDateFromResultSet(rs, "TND_EXPIRATION_DT");
			bean.tndHolderName = getStringFromResultSet(rs, "TND_HOLDER_NAME");
			bean.tndManualOverride = getStringFromResultSet(rs,
					"TND_MANUAL_OVERRIDE");
			bean.chkAbaNum = getStringFromResultSet(rs, "CHK_ABA_NUM");
			bean.chkRoutingNum = getStringFromResultSet(rs, "CHK_ROUTING_NUM");
			bean.chkBank = getStringFromResultSet(rs, "CHK_BANK");
			bean.chkIsScanned = getStringFromResultSet(rs, "CHK_IS_SCANNED");
			bean.ccCvvCode = getStringFromResultSet(rs, "CC_CVV_CODE");
			bean.ccAmexCidNum = getStringFromResultSet(rs, "CC_AMEX_CID_NUM");
			bean.rdmControlNumber = getStringFromResultSet(rs,
					"RDM_CONTROL_NUMBER");
			bean.tndSwipeInd = getStringFromResultSet(rs, "TND_SWIPE_IND");
			bean.tndRespAddressVerif = getStringFromResultSet(rs,
					"TND_RESP_ADDRESS_VERIF");
			bean.tndRespJournalKey = getStringFromResultSet(rs,
					"TND_RESP_JOURNAL_KEY");
			bean.tndRespMsg = getStringFromResultSet(rs, "TND_RESP_MSG");
			bean.tndRespMsgNum = getStringFromResultSet(rs, "TND_RESP_MSG_NUM");
			bean.tndMerchantId = getStringFromResultSet(rs, "TND_MERCHANT_ID");
			bean.tndCardIdentifier = getStringFromResultSet(rs,
					"TND_CARD_IDENTIFIER");
			bean.ccCardPlan = getStringFromResultSet(rs, "CC_CARD_PLAN");
			bean.rdmTndIdStrRt = getStringFromResultSet(rs, "RDM_TND_ID_STR_RT");
			bean.rdmDcCpnScanCode = getStringFromResultSet(rs,
					"RDM_DC_CPN_SCAN_CODE");
			bean.rdmTndPrmCode = getStringFromResultSet(rs, "RDM_TND_PRM_CODE");
			bean.rdmExpirationDate = getDateFromResultSet(rs,
					"RDM_EXPIRATION_DATE");
			bean.fcXchangeRate = getDoubleFromResultSet(rs, "FC_XCHANGE_RATE");
			bean.fcFromCurrency = getStringFromResultSet(rs, "FC_FROM_CURRENCY");
			bean.fcToCurrency = getStringFromResultSet(rs, "FC_TO_CURRENCY");
			bean.rdmType = getStringFromResultSet(rs, "RDM_TYPE");
			bean.rdmIssuingStore = getStringFromResultSet(rs,
					"RDM_ISSUING_STORE");
			bean.rdmCustNum = getStringFromResultSet(rs, "RDM_CUST_NUM");
			bean.rdmIssuanceDt = getDateFromResultSet(rs, "RDM_ISSUANCE_DT");
			bean.rdmExpirationDt = getDateFromResultSet(rs, "RDM_EXPIRATION_DT");
			bean.rdmFaceValueAmt = getDoubleFromResultSet(rs,
					"RDM_FACE_VALUE_AMT");
			bean.rdmBalanceAmt = getDoubleFromResultSet(rs, "RDM_BALANCE_AMT");
			bean.rdmBuybackAmt = getDoubleFromResultSet(rs, "RDM_BUYBACK_AMT");
			bean.rdmBuybackComment = getStringFromResultSet(rs,
					"RDM_BUYBACK_COMMENT");
			bean.postVoidedTransNo = getStringFromResultSet(rs,
					"POST_VOIDED_TRANS_NO");
			bean.postVoidReason = getStringFromResultSet(rs, "POST_VOID_REASON");
			bean.postVoidRegister = getStringFromResultSet(rs,
					"POST_VOID_REGISTER");
			bean.customerRole = getStringFromResultSet(rs, "CUSTOMER_ROLE");
			bean.custId = getStringFromResultSet(rs, "CUST_ID");
			bean.custFirstName = getStringFromResultSet(rs, "CUST_FIRST_NAME");
			bean.custLastName = getStringFromResultSet(rs, "CUST_LAST_NAME");
			bean.custAddr1 = getStringFromResultSet(rs, "CUST_ADDR_1");
			bean.custAddr2 = getStringFromResultSet(rs, "CUST_ADDR_2");
			bean.custCity = getStringFromResultSet(rs, "CUST_CITY");
			bean.custState = getStringFromResultSet(rs, "CUST_STATE");
			bean.custCountry = getStringFromResultSet(rs, "CUST_COUNTRY");
			bean.custPcode = getStringFromResultSet(rs, "CUST_PCODE");
			bean.custPhone1 = getStringFromResultSet(rs, "CUST_PHONE_1");
			bean.custPhone2 = getStringFromResultSet(rs, "CUST_PHONE_2");
			bean.cashierTxnComment = getStringFromResultSet(rs,
					"CASHIER_TXN_COMMENT");
			bean.cashierTxnReason = getStringFromResultSet(rs,
					"CASHIER_TXN_REASON");
			bean.openingDrwrFnd = getDoubleFromResultSet(rs, "OPENING_DRWR_FND");
			bean.eodTndType = getStringFromResultSet(rs, "EOD_TND_TYPE");
			bean.eodTndTotal = getStringFromResultSet(rs, "EOD_TND_TOTAL");
			bean.notInFileSkuDtl = getStringFromResultSet(rs,
					"NOT_IN_FILE_SKU_DTL");
			bean.status = getStringFromResultSet(rs, "STATUS");
			bean.dataPopulationDt = getDateFromResultSet(rs,
					"DATA_POPULATION_DT");
			bean.extractedDt = getDateFromResultSet(rs, "EXTRACTED_DT");
			bean.docType = getStringFromResultSet(rs, "DOC_TYPE");
			bean.vatComments = getStringFromResultSet(rs, "VAT_COMMENTS");
			bean.origLineItemType = getStringFromResultSet(rs,
					"ORIG_LINE_ITEM_TYPE");
			bean.eodReportedTndTotal = getStringFromResultSet(rs,
					"EOD_REPORTED_TND_TOTAL");
			bean.extendedBarcode = getStringFromResultSet(rs,
					"EXTENDED_BARCODE");
			// added by shushma for promotion code
			bean.promotionCode = getStringFromResultSet(rs, "PROMOTION_CODE");
			// added by Rachana for approval of return transaction
			bean.approverId = getStringFromResultSet(rs, "APPROVER_ID");
			// Anjana added below columns to save same data in staging table as
			// core table:- tr_ltm_crdb_crd_tn
			bean.cardToken = getStringFromResultSet(rs, "CD_TKN_NUMBER");
			bean.currencyCode = getStringFromResultSet(rs, "CURRENCY_CODE");
			bean.recieptData = getStringFromResultSet(rs, "TRN_RCPT_DATA");
			bean.convRate = getStringFromResultSet(rs, "CURRENCY_CONV_RATE");
			bean.custSignature = getObjectFromResultSet(rs, "CUST_SIGNATURE");
			bean.mrk = getDoubleFromResultSet(rs, "MRK");
			bean.mrk_per = getDoubleFromResultSet(rs, "MRK_PER");
			bean.red_amt2 = getDoubleFromResultSet(rs, "RED_AMT2");
			bean.dsc_per2 = getDoubleFromResultSet(rs, "DSC_PER2");
			bean.prz_Retail = getDoubleFromResultSet(rs, "PRZ_RETAIL");

			bean.mobileRegisterId = getStringFromResultSet(rs,
					"MOBILE_REGISTER_ID");

			list.add(bean);
		}
		return (ArmStgTxnDtlOracleBean[]) list
				.toArray(new ArmStgTxnDtlOracleBean[0]);
	}

	// Vivek Mishra : Added method for getting Object from result set
	private Object getObjectFromResultSet(ResultSet rs, String column)
			throws SQLException {
		Object object = rs.getBlob(column);
		if (rs.wasNull())
			return null;
		return object;
	}

	// Ends

	public List toList() {
		List list = new ArrayList();
		addToList(list, this.getRecordType(), Types.VARCHAR);
		addToList(list, this.getTransactionId(), Types.VARCHAR);
		addToList(list, this.getLineId(), Types.DECIMAL);
		addToList(list, this.getLineSeqNum(), Types.DECIMAL);
		addToList(list, this.getLineItemType(), Types.VARCHAR);
		addToList(list, this.getDocNum(), Types.VARCHAR);
		addToList(list, this.getMiscItemId(), Types.VARCHAR);
		addToList(list, this.getMiscItemTxbl(), Types.VARCHAR);
		addToList(list, this.getIdItm(), Types.VARCHAR);
		addToList(list, this.getQuantity(), Types.DECIMAL);
		addToList(list, this.getItmRtlPrice(), Types.DECIMAL);
		addToList(list, this.getItmSelPrice(), Types.DECIMAL);
		addToList(list, this.getManualMdAmt(), Types.DECIMAL);
		addToList(list, this.getManualUnitPrice(), Types.DECIMAL);
		addToList(list, this.getConsultantId(), Types.VARCHAR);
		addToList(list, this.getAddConsultantId(), Types.VARCHAR);
		addToList(list, this.getReturnReasonId(), Types.VARCHAR);
		addToList(list, this.getReturnComments(), Types.VARCHAR);
		addToList(list, this.getVatRate(), Types.DECIMAL);
		addToList(list, this.getTaxExemptId(), Types.VARCHAR);
		addToList(list, this.getShipState(), Types.VARCHAR);
		addToList(list, this.getShipZipCode(), Types.VARCHAR);
		addToList(list, this.getNetAmt(), Types.DECIMAL);
		addToList(list, this.getTaxAmt(), Types.DECIMAL);
		addToList(list, this.getManualTaxAmt(), Types.DECIMAL);
		addToList(list, this.getExceptionTaxJur(), Types.VARCHAR);
		addToList(list, this.getDealMkdnAmt(), Types.DECIMAL);
		addToList(list, this.getDealId(), Types.VARCHAR);
		addToList(list, this.getRtnSaleAiTrn(), Types.VARCHAR);
		addToList(list, this.getRtnOrigStoreId(), Types.VARCHAR);
		addToList(list, this.getRtnOrigProcessDt(), Types.TIMESTAMP);
		addToList(list, this.getRtnOrigOprId(), Types.VARCHAR);
		addToList(list, this.getRtnOrigRegisterId(), Types.VARCHAR);
		addToList(list, this.getOrigTransactionId(), Types.VARCHAR);
		addToList(list, this.getOrigAddNo(), Types.VARCHAR);
		addToList(list, this.getReductionAmount(), Types.DECIMAL);
		addToList(list, this.getReductionReason(), Types.VARCHAR);
		addToList(list, this.getDscAmount(), Types.DECIMAL);
		addToList(list, this.getDscPercent(), Types.DECIMAL);
		addToList(list, this.getDscReason(), Types.VARCHAR);
		addToList(list, this.getDscEmployeeId(), Types.VARCHAR);
		addToList(list, this.getDscAdvertisingCode(), Types.VARCHAR);
		addToList(list, this.getDscIsDscPercent(), Types.VARCHAR);
		addToList(list, this.getTndType(), Types.VARCHAR);
		addToList(list, this.getTndCode(), Types.VARCHAR);
		addToList(list, this.getTndAmount(), Types.DECIMAL);
		addToList(list, this.getTndIdAcntNmb(), Types.VARCHAR);
		addToList(list, this.getTndRespAuth(), Types.VARCHAR);
		addToList(list, this.getTndApprovalDate(), Types.TIMESTAMP);
		addToList(list, this.getTndExpirationDt(), Types.TIMESTAMP);
		addToList(list, this.getTndHolderName(), Types.VARCHAR);
		addToList(list, this.getTndManualOverride(), Types.VARCHAR);
		addToList(list, this.getChkAbaNum(), Types.VARCHAR);
		addToList(list, this.getChkRoutingNum(), Types.VARCHAR);
		addToList(list, this.getChkBank(), Types.VARCHAR);
		addToList(list, this.getChkIsScanned(), Types.VARCHAR);
		addToList(list, this.getCcCvvCode(), Types.VARCHAR);
		addToList(list, this.getCcAmexCidNum(), Types.VARCHAR);
		addToList(list, this.getRdmControlNumber(), Types.VARCHAR);
		addToList(list, this.getTndSwipeInd(), Types.VARCHAR);
		addToList(list, this.getTndRespAddressVerif(), Types.VARCHAR);
		addToList(list, this.getTndRespJournalKey(), Types.VARCHAR);
		addToList(list, this.getTndRespMsg(), Types.VARCHAR);
		addToList(list, this.getTndRespMsgNum(), Types.VARCHAR);
		addToList(list, this.getTndMerchantId(), Types.VARCHAR);
		addToList(list, this.getTndCardIdentifier(), Types.VARCHAR);
		addToList(list, this.getCcCardPlan(), Types.VARCHAR);
		addToList(list, this.getRdmTndIdStrRt(), Types.VARCHAR);
		addToList(list, this.getRdmDcCpnScanCode(), Types.VARCHAR);
		addToList(list, this.getRdmTndPrmCode(), Types.VARCHAR);
		addToList(list, this.getRdmExpirationDate(), Types.TIMESTAMP);
		addToList(list, this.getFcXchangeRate(), Types.DECIMAL);
		addToList(list, this.getFcFromCurrency(), Types.VARCHAR);
		addToList(list, this.getFcToCurrency(), Types.VARCHAR);
		addToList(list, this.getRdmType(), Types.VARCHAR);
		addToList(list, this.getRdmIssuingStore(), Types.VARCHAR);
		addToList(list, this.getRdmCustNum(), Types.VARCHAR);
		addToList(list, this.getRdmIssuanceDt(), Types.TIMESTAMP);
		addToList(list, this.getRdmExpirationDt(), Types.TIMESTAMP);
		addToList(list, this.getRdmFaceValueAmt(), Types.DECIMAL);
		addToList(list, this.getRdmBalanceAmt(), Types.DECIMAL);
		addToList(list, this.getRdmBuybackAmt(), Types.DECIMAL);
		addToList(list, this.getRdmBuybackComment(), Types.VARCHAR);
		addToList(list, this.getPostVoidedTransNo(), Types.VARCHAR);
		addToList(list, this.getPostVoidReason(), Types.VARCHAR);
		addToList(list, this.getPostVoidRegister(), Types.VARCHAR);
		addToList(list, this.getCustomerRole(), Types.VARCHAR);
		addToList(list, this.getCustId(), Types.VARCHAR);
		addToList(list, this.getCustFirstName(), Types.VARCHAR);
		addToList(list, this.getCustLastName(), Types.VARCHAR);
		addToList(list, this.getCustAddr1(), Types.VARCHAR);
		addToList(list, this.getCustAddr2(), Types.VARCHAR);
		addToList(list, this.getCustCity(), Types.VARCHAR);
		addToList(list, this.getCustState(), Types.VARCHAR);
		addToList(list, this.getCustCountry(), Types.VARCHAR);
		addToList(list, this.getCustPcode(), Types.VARCHAR);
		addToList(list, this.getCustPhone1(), Types.VARCHAR);
		addToList(list, this.getCustPhone2(), Types.VARCHAR);
		addToList(list, this.getCashierTxnComment(), Types.VARCHAR);
		addToList(list, this.getCashierTxnReason(), Types.VARCHAR);
		addToList(list, this.getOpeningDrwrFnd(), Types.DECIMAL);
		addToList(list, this.getEodTndType(), Types.VARCHAR);
		addToList(list, this.getEodTndTotal(), Types.VARCHAR);
		addToList(list, this.getNotInFileSkuDtl(), Types.VARCHAR);
		addToList(list, this.getStatus(), Types.VARCHAR);
		addToList(list, this.getDataPopulationDt(), Types.TIMESTAMP);
		addToList(list, this.getExtractedDt(), Types.TIMESTAMP);
		addToList(list, this.getDocType(), Types.VARCHAR);
		addToList(list, this.getVatComments(), Types.VARCHAR);
		addToList(list, this.getOrigLineItemType(), Types.VARCHAR);
		addToList(list, this.getEodReportedTndTotal(), Types.VARCHAR);
		addToList(list, this.getExtendedBarcode(), Types.VARCHAR);
		// added by shushma for promotion code
		addToList(list, this.getPromotionCode(), Types.VARCHAR);
		// added by Rachana for approval of return transaction
		addToList(list, this.getApproverId(), Types.VARCHAR);
		// Anjana added below columns to save same data in staging table as core
		// table:- tr_ltm_crdb_crd_tn
		addToList(list, this.getCardToken(), Types.VARCHAR);
		addToList(list, this.getCurrencyCode(), Types.VARCHAR);
		addToList(list, this.getRecieptData(), Types.BLOB);
		addToList(list, this.getConvRate(), Types.DECIMAL);
		addToList(list, this.getCustSignature(), Types.BLOB);
		addToList(list, this.mrk, Types.DECIMAL);
		addToList(list, this.mrk_per, Types.DECIMAL);
		addToList(list, this.red_amt2, Types.DECIMAL);
		addToList(list, this.dsc_per2, Types.DECIMAL);
		addToList(list, this.prz_Retail, Types.DECIMAL);

		addToList(list, this.mobileRegisterId, Types.VARCHAR);

		return list;
	}
}
