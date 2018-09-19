package com.chelseasystems.cs.ajbauthorization;

/**
 * @author Vivek M
 *
 */

import java.util.ArrayList;
import java.util.Iterator;

/**
 * Enum which contains all the constant values required in any of the AJB
 * request message.
 * 
 */

public enum AJBMessageCodes {
	IX_CMD_REQUEST("100"), IX_CMD_RESPONSE("101"), IX_CMD_PROMPT_REQUEST("150"), IX_CMD_PROMPT_RESPONSE(
			"151"), IX_CMD_SAF_REQUEST("111"), IX_CMD_SAF_RESPONSE("111"), IX_CMD_GIFTCARD_SWIPE("107"), IX_CMD_ACK_REQUEST("901"),

	IX_ACTION_CODE_RESPONSE_AUTHORIZED("0"), IX_ACTION_CODE_RESPONSE_DECLINED(
			"1"), IX_ACTION_CODE_RESPONSE_CALL_REFERRAL("2"), IX_ACTION_CODE_RESPONSE_BANK_DOWN(
			"3"), IX_ACTION_CODE_RESPONSE_COMM_ISSUE("5"), IX_ACTION_CODE_RESPONSE_REPORT_ERROR(
			"6"), IX_ACTION_CODE_RESPONSE_TRY_LATER("8"), IX_ACTION_CODE_RESPONSE_TIME_OUT(
			"10"), IX_ACTION_CODE_RESPONSE_APPROVED_ADMIN("12"), IX_ACTION_CODE_RESPONSE_MAC_FAILURE(
			"14"),

	IX_DEBIT_CREDIT_CHECK("check"), IX_DEBIT_CREDIT_ECHECK("echeck"), IX_DEBIT_CREDIT_CREDIT(
			"credit"), IX_DEBIT_CREDIT_VOID("Void"), IX_DEBIT_CREDIT_DEBIT(
			"debit"), IX_DEBIT_CREDIT_GIFTCARD("giftcard"), IX_DEBIT_CREDIT_VOIDSALE(
			"VoidSale"), IX_DEBIT_CREDIT_FORCE("Force"), IX_DEBIT_CREDIT_GETCARD(
			"Getcard"), IX_DEBIT_CREDIT_GETEFT("GetEFT"), IX_OPTIONS_TOKENIZATION(
			"*Tokenization"), IX_TRAN_TYPE_DEBIT_BIN_LOOKUP("Binlookup"), IX_TRAN_TYPE_CARD_GET_TOKEN(
			"GetToken"), IX_TRAN_TYPE_CARD_SALE("Sale"), IX_TRAN_TYPE_GIFT_CARD_SALE("Redeem"), IX_GIFT_CARD_VOIDSALE(
					"voidSale"), IX_GIFT_CARD_VOIDACTIVATE(
							"voidActivate"), IX_GIFT_CARD_VOIDRELOAD(
									"voidReload"), IX_GIFT_CARD_VOIDREFUND(
											"voidRefund"), IX_GIFT_CARD_VOIDCASHOUT(
													"voidCashout"),IX_TRAN_TYPE_CHECK_SALE(
			"Sale"), IX_TRAN_TYPE_REFUND("Refund"),
				//Added by Himani for redeemable fipay integration
			IX_TRAN_TYPE_CASHOUT("cashout"), 
			IX_TRAN_TYPE_DEBIT_INIT(
			"InitDebit"), IX_OPTIONS_BINLOOKUP("*Binlookup"), IX_VERIFONE_PROMPT(
			"Prompt"), IX_VERIFONE_POS_ITEMS("*POSITEMS"),IX_VERIFONE_RESET("*POSITEMS-Reset"), IX_VERIFONE_POS_ITEMS_CLEAR(
			"*POSITEMS-CLEAR"),IX_VERIFONE_POS_ITEMS_REMOVE(
					"*POSITEMS-REMOVE"), IX_VERIFONE_POS_ITEMS_CHANGE("*POSITEM-CHANGE"), IX_VERIFONE_POS_ITEMS_REFRESH(
			"*POSITEMS-REFRESH"), IX_VERIFONE_SIGNATURE("*SIGNATURE"),IX_OPTIONS_TELEAUTH("TELAUTH"), 
			IX_TRAN_TYPE_BALANCE_INQUIRY("balanceInquiry"), IX_TRAN_TYPE_ACTIVATE("Activate"), IX_TRAN_TYPE_RELOAD("Reload"),
			//Added by Himani for GC Transaction History
			IX_TRAN_TYPE_TRANSACTION_HISTORY("ministatement"),

	IX_ENTRY_METHOD_MANUAL("*cem_manual"), IX_ENTRY_METHOD_MAGSWIPE(
			"*CEM_Swiped"), IX_ENTRY_METHOD_ICC("*CEM_Insert"), IX_ENTRY_METHOD_ICC_FALLBACK(
			"*FALLBACK_EMV"), IX_CHECK_VOID("Void"),
	//Vivek Mishra : Added to send verious track request with Gift Card 107 swipe request		
	IX_SWIPE_MANUAL_ENTRY("0"), IX_SWIPE_TRACK1("1"), IX_SWIPE_TRACK2("2"), IX_SWIPE_TRACK1AND2("4"),
	IX_OPTIONS_VERIFY_CARD("VerifyCard");

	private String value;

	private AJBMessageCodes(String value) {
		this.value = value;
	}

	public String getValue() {
		return this.value;
	}
}
