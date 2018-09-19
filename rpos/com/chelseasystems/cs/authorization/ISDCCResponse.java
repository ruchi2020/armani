package com.chelseasystems.cs.authorization;

import java.util.Date;

public class ISDCCResponse {

	/*
	 * Header Fields
	 */
	String messageLength;
	String messageId;
    String journalKey;
    String messageType;
    String tenderType;
    String respAccountno;
    String amount;
    String storeNumber;
    String terminalNumber;
    String messageSequence;
    String authDate;
    String authTime;
    String hostActionCode;
    String statusCode;
    String responseMsg;
    String authCode;
    String authResponseCode;
    String merchId;
    /*
     * Ends Header fields
     */
	
    String merchantCategoryCode;
    String respPOSEntryMode;
    String retreivalReferenceNumber;
    String respAuthorizationSource;
    String cardType;
    String currencyCode;
    String respPaymentServiceIndicator;
    String respTransactionIdentifier;
    String respValidationCode;
    String localTransactionDate;
    String associationReturnCode;
    String localTransactionTime;
    String systemTraceAuditNumber;
    String transmissionDateandTime;
    String respExpiryDate;
    String hostTransactionReferenceNumber;
    String trackNumber;
    String authorizerBankNumber;
    String posPinEntry;
    String marketSpecificEntry;
    String token;
    String partialApprovalIndicator;
    String traceNumber;
    String resCode;
	Date approvalDate;
	String merchantID;
	String respID;
	String respAddressVerification;
	String cardIdentifier;
    String trackData;
    
   
	
    public String getMessageLength(){
    	return messageLength;
    }
    public void setMessageLength(String mesLength) {
    	this.messageLength = mesLength;
    }
    
	public String getAuthCode() {
		return authCode;
	}
	public void setAuthCode(String authCode) {
		this.authCode = authCode;
	}
	public String getResCode() {
		return resCode;
	}
	public void setResCode(String resCode) {
		this.resCode = resCode;
	}
	public String getResponseMsg() {
		return responseMsg;
	}
	public void setResponseMsg(String responseMsg) {
		this.responseMsg = responseMsg;
	}
	public String getStatusCode() {
		return statusCode;
	}
	public void setStatusCode(String statusCode) {
		this.statusCode = statusCode;
	}
	public Date getApprovalDate() {
		return approvalDate;
	}
	public void setApprovalDate(Date approvalDate) {
		this.approvalDate = approvalDate;
	}
	public String getAmount() {
		return amount;
	}
	public void setAmount(String amount) {
		this.amount = amount;
	}
	public String getCardIdentifier() {
		return cardIdentifier;
	}
	public void setCardIdentifier(String cardIdentifier) {
		this.cardIdentifier = cardIdentifier;
	}
	public String getMerchantID() {
		return merchantID;
	}
	public void setMerchantID(String merchantID) {
		this.merchantID = merchantID;
	}
	public String getRespAddressVerification() {
		return respAddressVerification;
	}
	public void setRespAddressVerification(String respAddressVerification) {
		this.respAddressVerification = respAddressVerification;
	}
	public String getRespAuthorizationSource() {
		return respAuthorizationSource;
	}
	public void setRespAuthorizationSource(String respAuthorizationSource) {
		this.respAuthorizationSource = respAuthorizationSource;
	}
	public String getRespID() {
		return respID;
	}
	public void setRespID(String respID) {
		this.respID = respID;
	}
	public String getRespPaymentServiceIndicator() {
		return respPaymentServiceIndicator;
	}
	public void setRespPaymentServiceIndicator(String respPaymentServiceIndicator) {
		this.respPaymentServiceIndicator = respPaymentServiceIndicator;
	}
	public String getRespPOSEntryMode() {
		return respPOSEntryMode;
	}
	public void setRespPOSEntryMode(String respPOSEntryMode) {
		this.respPOSEntryMode = respPOSEntryMode;
	}
	public String getRespTransactionIdentifier() {
		return respTransactionIdentifier;
	}
	public void setRespTransactionIdentifier(String respTransactionIdentifier) {
		this.respTransactionIdentifier = respTransactionIdentifier;
	}
	public String getRespValidationCode() {
		return respValidationCode;
	}
	public void setRespValidationCode(String respValidationCode) {
		this.respValidationCode = respValidationCode;
	}
	public String getRespAccountno() {
		return respAccountno;
	}
	public void setRespAccountno(String respAccountno) {
		this.respAccountno = respAccountno;
	}
	public String getRespExpiryDate() {
		return respExpiryDate;
	}
	public void setRespExpiryDate(String respExpiryDate) {
		this.respExpiryDate = respExpiryDate;
	}
	public String getTrackData() {
		return trackData;
	}
	public void setTrackData(String trackData) {
		this.trackData = trackData;
	}
	public String getAssociationReturnCode() {
		return associationReturnCode;
	}
	public void setAssociationReturnCode(String associationReturnCode) {
		this.associationReturnCode = associationReturnCode;
	}
	public String getAuthDate() {
		return authDate;
	}
	public void setAuthDate(String authDate) {
		this.authDate = authDate;
	}
	public String getAuthorizerBankNumber() {
		return authorizerBankNumber;
	}
	public void setAuthorizerBankNumber(String authorizerBankNumber) {
		this.authorizerBankNumber = authorizerBankNumber;
	}
	public String getAuthResponseCode() {
		return authResponseCode;
	}
	public void setAuthResponseCode(String authResponseCode) {
		this.authResponseCode = authResponseCode;
	}
	public String getAuthTime() {
		return authTime;
	}
	public void setAuthTime(String authTime) {
		this.authTime = authTime;
	}
	public String getCardType() {
		return cardType;
	}
	public void setCardType(String cardType) {
		this.cardType = cardType;
	}
	public String getCurrencyCode() {
		return currencyCode;
	}
	public void setCurrencyCode(String currencyCode) {
		this.currencyCode = currencyCode;
	}
	public String getHostActionCode() {
		return hostActionCode;
	}
	public void setHostActionCode(String hostActionCode) {
		this.hostActionCode = hostActionCode;
	}
	public String getHostTransactionReferenceNumber() {
		return hostTransactionReferenceNumber;
	}
	public void setHostTransactionReferenceNumber(
			String hostTransactionReferenceNumber) {
		this.hostTransactionReferenceNumber = hostTransactionReferenceNumber;
	}
	public String getJournalKey() {
		return journalKey;
	}
	public void setJournalKey(String journalKey) {
		this.journalKey = journalKey;
	}
	public String getLocalTransactionDate() {
		return localTransactionDate;
	}
	public void setLocalTransactionDate(String localTransactionDate) {
		this.localTransactionDate = localTransactionDate;
	}
	public String getLocalTransactionTime() {
		return localTransactionTime;
	}
	public void setLocalTransactionTime(String localTransactionTime) {
		this.localTransactionTime = localTransactionTime;
	}
	public String getMarketSpecificEntry() {
		return marketSpecificEntry;
	}
	public void setMarketSpecificEntry(String marketSpecificEntry) {
		this.marketSpecificEntry = marketSpecificEntry;
	}
	public String getMerchantCategoryCode() {
		return merchantCategoryCode;
	}
	public void setMerchantCategoryCode(String merchantCategoryCode) {
		this.merchantCategoryCode = merchantCategoryCode;
	}
	public String getMerchId() {
		return merchId;
	}
	public void setMerchId(String merchId) {
		this.merchId = merchId;
	}
	public String getMessageSequence() {
		return messageSequence;
	}
	public void setMessageSequence(String messageSequence) {
		this.messageSequence = messageSequence;
	}
	public String getMessageType() {
		return messageType;
	}
	public void setMessageType(String messageType) {
		this.messageType = messageType;
	}
	public String getMessageId() {
		return messageId;
	}
	public void setMessageId(String msmessageId) {
		this.messageId = msmessageId;
	}
	public String getPartialApprovalIndicator() {
		return partialApprovalIndicator;
	}
	public void setPartialApprovalIndicator(String partialApprovalIndicator) {
		this.partialApprovalIndicator = partialApprovalIndicator;
	}
	public String getPosPinEntry() {
		return posPinEntry;
	}
	public void setPosPinEntry(String posPinEntry) {
		this.posPinEntry = posPinEntry;
	}
	public String getRetreivalReferenceNumber() {
		return retreivalReferenceNumber;
	}
	public void setRetreivalReferenceNumber(String retreivalReferenceNumber) {
		this.retreivalReferenceNumber = retreivalReferenceNumber;
	}
	public String getStoreNumber() {
		return storeNumber;
	}
	public void setStoreNumber(String storeNumber) {
		this.storeNumber = storeNumber;
	}
	public String getSystemTraceAuditNumber() {
		return systemTraceAuditNumber;
	}
	public void setSystemTraceAuditNumber(String systemTraceAuditNumber) {
		this.systemTraceAuditNumber = systemTraceAuditNumber;
	}
	public String getTenderType() {
		return tenderType;
	}
	public void setTenderType(String tenderType) {
		this.tenderType = tenderType;
	}
	public String getTerminalNumber() {
		return terminalNumber;
	}
	public void setTerminalNumber(String terminalNumber) {
		this.terminalNumber = terminalNumber;
	}
	public String getToken() {
		return token;
	}
	public void setToken(String token) {
		this.token = token;
	}
	public String getTraceNumber() {
		return traceNumber;
	}
	public void setTraceNumber(String traceNumber) {
		this.traceNumber = traceNumber;
	}
	public String getTrackNumber() {
		return trackNumber;
	}
	public void setTrackNumber(String trackNumber) {
		this.trackNumber = trackNumber;
	}
	public String getTransmissionDateandTime() {
		return transmissionDateandTime;
	}
	public void setTransmissionDateandTime(String transmissionDateandTime) {
		this.transmissionDateandTime = transmissionDateandTime;
	}
}
