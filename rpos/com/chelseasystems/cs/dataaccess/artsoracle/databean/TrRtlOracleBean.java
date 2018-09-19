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
 * This class is an object representation of the Arts database table TR_RTL<BR>
 * Followings are the column of the table: <BR>
 * IN_RNG_ELPSD -- NUMBER(5)<BR>
 * IN_TND_ELPSD -- NUMBER(5)<BR>
 * IN_ELPSD_IDL -- NUMBER(5)<BR>
 * IN_LCK_ELPSD -- NUMBER(5)<BR>
 * QU_ITM_LN_SC -- NUMBER(7)<BR>
 * QU_ITM_LN_KY -- NUMBER(7)<BR>
 * PE_ITM_LN_SC -- NUMBER(7.4)<BR>
 * PE_ITM_LN_KY -- NUMBER(7.4)<BR>
 * ID_CT -- VARCHAR2(128)<BR>
 * QU_DPT_KY -- NUMBER(7)<BR>
 * PE_DPT_KY -- NUMBER(7.4)<BR>
 * AI_TRN(PK) -- VARCHAR2(128)<BR>
 * CONSULTANT_ID -- VARCHAR2(128)<BR>
 * TAX_EXEMPT_ID -- VARCHAR2(128)<BR>
 * REG_TAX_EXMP_ID -- VARCHAR2(128)<BR>
 * NOT_RTN_REASON -- VARCHAR2(200)<BR>
 * REDUCTION_AMOUNT -- VARCHAR2(75)<BR>
 * NET_AMOUNT -- VARCHAR2(75)<BR>
 * DISCOUNT_TYPES -- VARCHAR2(200)<BR>
 * REGISTER_ID -- VARCHAR2(128)<BR>
 * ITEMS_IDS -- VARCHAR2(4000)<BR>
 * LOYALTY_TRUNCATED -- NUMBER(1)<BR>
 * LOYALTY_CARD_NUM -- VARCHAR2(20)<BR>
 * FISCAL_DOC_NUMBERS -- VARCHAR2(2000)<BR>
 * LOYALTY_POINTS_EARNED -- NUMBER(7)<BR>
 * APPROVER_ID -- VARCHAR2(128)<BR>
 */
public class TrRtlOracleBean extends BaseOracleBean {

	public TrRtlOracleBean() {
	}

	public static String selectSql = "select IN_RNG_ELPSD, IN_TND_ELPSD, IN_ELPSD_IDL, IN_LCK_ELPSD, QU_ITM_LN_SC, QU_ITM_LN_KY, PE_ITM_LN_SC, PE_ITM_LN_KY, ID_CT, QU_DPT_KY, PE_DPT_KY, AI_TRN, CONSULTANT_ID, TAX_EXEMPT_ID, REG_TAX_EXMP_ID, NOT_RTN_REASON, REDUCTION_AMOUNT, NET_AMOUNT, DISCOUNT_TYPES, REGISTER_ID, ITEMS_IDS, LOYALTY_TRUNCATED, LOYALTY_CARD_NUM, FISCAL_DOC_NUMBERS, LOYALTY_POINTS_EARNED, DIGITAL_SIGNATURE, APPROVER_ID, MOBILE_REGISTER_ID, TRN_COMMENT from TR_RTL ";
	public static String insertSql = "insert into TR_RTL (IN_RNG_ELPSD, IN_TND_ELPSD, IN_ELPSD_IDL, IN_LCK_ELPSD, QU_ITM_LN_SC, QU_ITM_LN_KY, PE_ITM_LN_SC, PE_ITM_LN_KY, ID_CT, QU_DPT_KY, PE_DPT_KY, AI_TRN, CONSULTANT_ID, TAX_EXEMPT_ID, REG_TAX_EXMP_ID, NOT_RTN_REASON, REDUCTION_AMOUNT, NET_AMOUNT, DISCOUNT_TYPES, REGISTER_ID, ITEMS_IDS, LOYALTY_TRUNCATED, LOYALTY_CARD_NUM, FISCAL_DOC_NUMBERS, LOYALTY_POINTS_EARNED, DIGITAL_SIGNATURE, APPROVER_ID, MOBILE_REGISTER_ID, TRN_COMMENT) values (?,?,?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
	public static String updateSql = "update TR_RTL set IN_RNG_ELPSD = ?, IN_TND_ELPSD = ?, IN_ELPSD_IDL = ?, IN_LCK_ELPSD = ?, QU_ITM_LN_SC = ?, QU_ITM_LN_KY = ?, PE_ITM_LN_SC = ?, PE_ITM_LN_KY = ?, ID_CT = ?, QU_DPT_KY = ?, PE_DPT_KY = ?, AI_TRN = ?, CONSULTANT_ID = ?, TAX_EXEMPT_ID = ?, REG_TAX_EXMP_ID = ?, NOT_RTN_REASON = ?, REDUCTION_AMOUNT = ?, NET_AMOUNT = ?, DISCOUNT_TYPES = ?, REGISTER_ID = ?, ITEMS_IDS = ?, LOYALTY_TRUNCATED = ?, LOYALTY_CARD_NUM = ?, FISCAL_DOC_NUMBERS = ?, LOYALTY_POINTS_EARNED = ?, DIGITAL_SIGNATURE = ?, APPROVER_ID =?, MOBILE_REGISTER_ID=?, TRN_COMMENT=? ";
	public static String deleteSql = "delete from TR_RTL ";

	public static String TABLE_NAME = "TR_RTL";
	public static String COL_IN_RNG_ELPSD = "TR_RTL.IN_RNG_ELPSD";
	public static String COL_IN_TND_ELPSD = "TR_RTL.IN_TND_ELPSD";
	public static String COL_IN_ELPSD_IDL = "TR_RTL.IN_ELPSD_IDL";
	public static String COL_IN_LCK_ELPSD = "TR_RTL.IN_LCK_ELPSD";
	public static String COL_QU_ITM_LN_SC = "TR_RTL.QU_ITM_LN_SC";
	public static String COL_QU_ITM_LN_KY = "TR_RTL.QU_ITM_LN_KY";
	public static String COL_PE_ITM_LN_SC = "TR_RTL.PE_ITM_LN_SC";
	public static String COL_PE_ITM_LN_KY = "TR_RTL.PE_ITM_LN_KY";
	public static String COL_ID_CT = "TR_RTL.ID_CT";
	public static String COL_QU_DPT_KY = "TR_RTL.QU_DPT_KY";
	public static String COL_PE_DPT_KY = "TR_RTL.PE_DPT_KY";
	public static String COL_AI_TRN = "TR_RTL.AI_TRN";
	public static String COL_CONSULTANT_ID = "TR_RTL.CONSULTANT_ID";
	public static String COL_TAX_EXEMPT_ID = "TR_RTL.TAX_EXEMPT_ID";
	public static String COL_REG_TAX_EXMP_ID = "TR_RTL.REG_TAX_EXMP_ID";
	public static String COL_NOT_RTN_REASON = "TR_RTL.NOT_RTN_REASON";
	public static String COL_REDUCTION_AMOUNT = "TR_RTL.REDUCTION_AMOUNT";
	public static String COL_NET_AMOUNT = "TR_RTL.NET_AMOUNT";
	public static String COL_DISCOUNT_TYPES = "TR_RTL.DISCOUNT_TYPES";
	public static String COL_REGISTER_ID = "TR_RTL.REGISTER_ID";
	public static String COL_ITEMS_IDS = "TR_RTL.ITEMS_IDS";
	public static String COL_LOYALTY_TRUNCATED = "TR_RTL.LOYALTY_TRUNCATED";
	public static String COL_LOYALTY_CARD_NUM = "TR_RTL.LOYALTY_CARD_NUM";
	public static String COL_FISCAL_DOC_NUMBERS = "TR_RTL.FISCAL_DOC_NUMBERS";
	public static String COL_LOYALTY_POINTS_EARNED = "TR_RTL.LOYALTY_POINTS_EARNED";
	// Added by Satin for digital_signature
	public static String COL_DIGITAL_SIGNATURE = "TR_RTL.DIGITAL_SIGNATURE";
	// Added by Rachana for approval of return transaction
	public static String COL_APPROVER_ID = "TR_RTL.APPROVER_ID";

	public static String COL_MOBILE_REGISTER_ID = "TR_RTL.MOBILE_REGISTER_ID";
	public static String COL_TRN_COMMENT = "TR_RTL.TRN_COMMENT";

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

	private Long inRngElpsd;
	private Long inTndElpsd;
	private Long inElpsdIdl;
	private Long inLckElpsd;
	private Long quItmLnSc;
	private Long quItmLnKy;
	private Double peItmLnSc;
	private Double peItmLnKy;
	private String idCt;
	private Long quDptKy;
	private Double peDptKy;
	private String aiTrn;
	private String consultantId;
	private String taxExemptId;
	private String regTaxExmpId;
	private String notRtnReason;
	private ArmCurrency reductionAmount;
	private ArmCurrency netAmount;
	private String discountTypes;
	private String registerId;
	private String itemsIds;
	private Boolean loyaltyTruncated;
	private String loyaltyCardNum;
	private String fiscalDocNumbers;
	private Long loyaltyPointsEarned;
	// Added by Satin for digital signature
	private String digitalSignature;
	// Added by Rachana for approval of return transaction
	private String approverId;

	private String mobileRegisterId;
	private String trnComment;

	public Long getInRngElpsd() {
		return this.inRngElpsd;
	}

	public void setInRngElpsd(Long inRngElpsd) {
		this.inRngElpsd = inRngElpsd;
	}

	public void setInRngElpsd(long inRngElpsd) {
		this.inRngElpsd = new Long(inRngElpsd);
	}

	public void setInRngElpsd(int inRngElpsd) {
		this.inRngElpsd = new Long((long) inRngElpsd);
	}

	public Long getInTndElpsd() {
		return this.inTndElpsd;
	}

	public void setInTndElpsd(Long inTndElpsd) {
		this.inTndElpsd = inTndElpsd;
	}

	public void setInTndElpsd(long inTndElpsd) {
		this.inTndElpsd = new Long(inTndElpsd);
	}

	public void setInTndElpsd(int inTndElpsd) {
		this.inTndElpsd = new Long((long) inTndElpsd);
	}

	public Long getInElpsdIdl() {
		return this.inElpsdIdl;
	}

	public void setInElpsdIdl(Long inElpsdIdl) {
		this.inElpsdIdl = inElpsdIdl;
	}

	public void setInElpsdIdl(long inElpsdIdl) {
		this.inElpsdIdl = new Long(inElpsdIdl);
	}

	public void setInElpsdIdl(int inElpsdIdl) {
		this.inElpsdIdl = new Long((long) inElpsdIdl);
	}

	public Long getInLckElpsd() {
		return this.inLckElpsd;
	}

	public void setInLckElpsd(Long inLckElpsd) {
		this.inLckElpsd = inLckElpsd;
	}

	public void setInLckElpsd(long inLckElpsd) {
		this.inLckElpsd = new Long(inLckElpsd);
	}

	public void setInLckElpsd(int inLckElpsd) {
		this.inLckElpsd = new Long((long) inLckElpsd);
	}

	public Long getQuItmLnSc() {
		return this.quItmLnSc;
	}

	public void setQuItmLnSc(Long quItmLnSc) {
		this.quItmLnSc = quItmLnSc;
	}

	public void setQuItmLnSc(long quItmLnSc) {
		this.quItmLnSc = new Long(quItmLnSc);
	}

	public void setQuItmLnSc(int quItmLnSc) {
		this.quItmLnSc = new Long((long) quItmLnSc);
	}

	public Long getQuItmLnKy() {
		return this.quItmLnKy;
	}

	public void setQuItmLnKy(Long quItmLnKy) {
		this.quItmLnKy = quItmLnKy;
	}

	public void setQuItmLnKy(long quItmLnKy) {
		this.quItmLnKy = new Long(quItmLnKy);
	}

	public void setQuItmLnKy(int quItmLnKy) {
		this.quItmLnKy = new Long((long) quItmLnKy);
	}

	public Double getPeItmLnSc() {
		return this.peItmLnSc;
	}

	public void setPeItmLnSc(Double peItmLnSc) {
		this.peItmLnSc = peItmLnSc;
	}

	public void setPeItmLnSc(double peItmLnSc) {
		this.peItmLnSc = new Double(peItmLnSc);
	}

	public Double getPeItmLnKy() {
		return this.peItmLnKy;
	}

	public void setPeItmLnKy(Double peItmLnKy) {
		this.peItmLnKy = peItmLnKy;
	}

	public void setPeItmLnKy(double peItmLnKy) {
		this.peItmLnKy = new Double(peItmLnKy);
	}

	public String getIdCt() {
		return this.idCt;
	}

	public void setIdCt(String idCt) {
		this.idCt = idCt;
	}

	public Long getQuDptKy() {
		return this.quDptKy;
	}

	public void setQuDptKy(Long quDptKy) {
		this.quDptKy = quDptKy;
	}

	public void setQuDptKy(long quDptKy) {
		this.quDptKy = new Long(quDptKy);
	}

	public void setQuDptKy(int quDptKy) {
		this.quDptKy = new Long((long) quDptKy);
	}

	public Double getPeDptKy() {
		return this.peDptKy;
	}

	public void setPeDptKy(Double peDptKy) {
		this.peDptKy = peDptKy;
	}

	public void setPeDptKy(double peDptKy) {
		this.peDptKy = new Double(peDptKy);
	}

	public String getAiTrn() {
		return this.aiTrn;
	}

	public void setAiTrn(String aiTrn) {
		this.aiTrn = aiTrn;
	}

	public String getConsultantId() {
		return this.consultantId;
	}

	public void setConsultantId(String consultantId) {
		this.consultantId = consultantId;
	}

	public String getTaxExemptId() {
		return this.taxExemptId;
	}

	public void setTaxExemptId(String taxExemptId) {
		this.taxExemptId = taxExemptId;
	}

	public String getRegTaxExmpId() {
		return this.regTaxExmpId;
	}

	public void setRegTaxExmpId(String regTaxExmpId) {
		this.regTaxExmpId = regTaxExmpId;
	}

	public String getNotRtnReason() {
		return this.notRtnReason;
	}

	public void setNotRtnReason(String notRtnReason) {
		this.notRtnReason = notRtnReason;
	}

	public ArmCurrency getReductionAmount() {
		return this.reductionAmount;
	}

	public void setReductionAmount(ArmCurrency reductionAmount) {
		this.reductionAmount = reductionAmount;
	}

	public ArmCurrency getNetAmount() {
		return this.netAmount;
	}

	public void setNetAmount(ArmCurrency netAmount) {
		this.netAmount = netAmount;
	}

	public String getDiscountTypes() {
		return this.discountTypes;
	}

	public void setDiscountTypes(String discountTypes) {
		this.discountTypes = discountTypes;
	}

	public String getRegisterId() {
		return this.registerId;
	}

	public void setRegisterId(String registerId) {
		this.registerId = registerId;
	}

	public String getItemsIds() {
		return this.itemsIds;
	}

	public void setItemsIds(String itemsIds) {
		this.itemsIds = itemsIds;
	}

	public Boolean getLoyaltyTruncated() {
		return this.loyaltyTruncated;
	}

	public void setLoyaltyTruncated(Boolean loyaltyTruncated) {
		this.loyaltyTruncated = loyaltyTruncated;
	}

	public void setLoyaltyTruncated(boolean loyaltyTruncated) {
		this.loyaltyTruncated = new Boolean(loyaltyTruncated);
	}

	public String getLoyaltyCardNum() {
		return this.loyaltyCardNum;
	}

	public void setLoyaltyCardNum(String loyaltyCardNum) {
		this.loyaltyCardNum = loyaltyCardNum;
	}

	public String getFiscalDocNumbers() {
		return this.fiscalDocNumbers;
	}

	public void setFiscalDocNumbers(String fiscalDocNumbers) {
		this.fiscalDocNumbers = fiscalDocNumbers;
	}

	public Long getLoyaltyPointsEarned() {
		return this.loyaltyPointsEarned;
	}

	public void setLoyaltyPointsEarned(Long loyaltyPointsEarned) {
		this.loyaltyPointsEarned = loyaltyPointsEarned;
	}

	public void setLoyaltyPointsEarned(long loyaltyPointsEarned) {
		this.loyaltyPointsEarned = new Long(loyaltyPointsEarned);
	}

	public void setLoyaltyPointsEarned(int loyaltyPointsEarned) {
		this.loyaltyPointsEarned = new Long((long) loyaltyPointsEarned);
	}

	// Added by Satin for digital signature.
	public String getDigitalSignature() {
		return this.digitalSignature;
	}

	public void setDigitalSignature(String digitalSignature) {
		this.digitalSignature = digitalSignature;
	}

	// Added by Rachana for approval of return transaction
	public String getApproverId() {
		return this.approverId;
	}

	public void setApproverId(String approverId) {
		this.approverId = approverId;
	}

	public String getMobileRegisterId() {
		return mobileRegisterId;
	}

	public void setMobileRegisterId(String mobileRegisterId) {
		this.mobileRegisterId = mobileRegisterId;
	}

	public String getTrnComment() {
		return trnComment;
	}

	public void setTrnComment(String trnComment) {
		this.trnComment = trnComment;
	}

	public BaseOracleBean[] getDatabeans(ResultSet rs) throws SQLException {
		ArrayList list = new ArrayList();
		while (rs.next()) {
			TrRtlOracleBean bean = new TrRtlOracleBean();
			bean.inRngElpsd = getLongFromResultSet(rs, "IN_RNG_ELPSD");
			bean.inTndElpsd = getLongFromResultSet(rs, "IN_TND_ELPSD");
			bean.inElpsdIdl = getLongFromResultSet(rs, "IN_ELPSD_IDL");
			bean.inLckElpsd = getLongFromResultSet(rs, "IN_LCK_ELPSD");
			bean.quItmLnSc = getLongFromResultSet(rs, "QU_ITM_LN_SC");
			bean.quItmLnKy = getLongFromResultSet(rs, "QU_ITM_LN_KY");
			bean.peItmLnSc = getDoubleFromResultSet(rs, "PE_ITM_LN_SC");
			bean.peItmLnKy = getDoubleFromResultSet(rs, "PE_ITM_LN_KY");
			bean.idCt = getStringFromResultSet(rs, "ID_CT");
			bean.quDptKy = getLongFromResultSet(rs, "QU_DPT_KY");
			bean.peDptKy = getDoubleFromResultSet(rs, "PE_DPT_KY");
			bean.aiTrn = getStringFromResultSet(rs, "AI_TRN");
			bean.consultantId = getStringFromResultSet(rs, "CONSULTANT_ID");
			bean.taxExemptId = getStringFromResultSet(rs, "TAX_EXEMPT_ID");
			bean.regTaxExmpId = getStringFromResultSet(rs, "REG_TAX_EXMP_ID");
			bean.notRtnReason = getStringFromResultSet(rs, "NOT_RTN_REASON");
			bean.reductionAmount = getCurrencyFromResultSet(rs,
					"REDUCTION_AMOUNT");
			bean.netAmount = getCurrencyFromResultSet(rs, "NET_AMOUNT");
			bean.discountTypes = getStringFromResultSet(rs, "DISCOUNT_TYPES");
			bean.registerId = getStringFromResultSet(rs, "REGISTER_ID");
			bean.itemsIds = getStringFromResultSet(rs, "ITEMS_IDS");
			bean.loyaltyTruncated = getBooleanFromResultSet(rs,
					"LOYALTY_TRUNCATED");
			bean.loyaltyCardNum = getStringFromResultSet(rs, "LOYALTY_CARD_NUM");
			bean.fiscalDocNumbers = getStringFromResultSet(rs,
					"FISCAL_DOC_NUMBERS");
			bean.loyaltyPointsEarned = getLongFromResultSet(rs,
					"LOYALTY_POINTS_EARNED");
			// Added by Satin for digital signature
			bean.digitalSignature = getStringFromResultSet(rs,
					"DIGITAL_SIGNATURE");
			// Added by Rachana for approval of return transaction
			bean.approverId = getStringFromResultSet(rs, "APPROVER_ID");

			bean.mobileRegisterId = getStringFromResultSet(rs,
					"MOBILE_REGISTER_ID");
			bean.trnComment = getStringFromResultSet(rs, "TRN_COMMENT");
			list.add(bean);
		}
		return (TrRtlOracleBean[]) list.toArray(new TrRtlOracleBean[0]);
	}

	public List toList() {
		List list = new ArrayList();
		addToList(list, this.getInRngElpsd(), Types.DECIMAL);
		addToList(list, this.getInTndElpsd(), Types.DECIMAL);
		addToList(list, this.getInElpsdIdl(), Types.DECIMAL);
		addToList(list, this.getInLckElpsd(), Types.DECIMAL);
		addToList(list, this.getQuItmLnSc(), Types.DECIMAL);
		addToList(list, this.getQuItmLnKy(), Types.DECIMAL);
		addToList(list, this.getPeItmLnSc(), Types.DECIMAL);
		addToList(list, this.getPeItmLnKy(), Types.DECIMAL);
		addToList(list, this.getIdCt(), Types.VARCHAR);
		addToList(list, this.getQuDptKy(), Types.DECIMAL);
		addToList(list, this.getPeDptKy(), Types.DECIMAL);
		addToList(list, this.getAiTrn(), Types.VARCHAR);
		addToList(list, this.getConsultantId(), Types.VARCHAR);
		addToList(list, this.getTaxExemptId(), Types.VARCHAR);
		addToList(list, this.getRegTaxExmpId(), Types.VARCHAR);
		addToList(list, this.getNotRtnReason(), Types.VARCHAR);
		addToList(list, this.getReductionAmount(), Types.VARCHAR);
		addToList(list, this.getNetAmount(), Types.VARCHAR);
		addToList(list, this.getDiscountTypes(), Types.VARCHAR);
		addToList(list, this.getRegisterId(), Types.VARCHAR);
		addToList(list, this.getItemsIds(), Types.VARCHAR);
		addToList(list, this.getLoyaltyTruncated(), Types.DECIMAL);
		addToList(list, this.getLoyaltyCardNum(), Types.VARCHAR);
		addToList(list, this.getFiscalDocNumbers(), Types.VARCHAR);
		addToList(list, this.getLoyaltyPointsEarned(), Types.DECIMAL);
		// Added by Satin for digital Signature
		addToList(list, this.getDigitalSignature(), Types.VARCHAR);
		// Added by Rachana for approval of return transaction
		addToList(list, this.getApproverId(), Types.VARCHAR);

		addToList(list, this.getMobileRegisterId(), Types.VARCHAR);
		addToList(list, this.getTrnComment(), Types.VARCHAR);

		return list;
	}

}
