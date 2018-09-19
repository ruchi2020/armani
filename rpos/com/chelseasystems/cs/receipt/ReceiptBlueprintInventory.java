/*
 * put your module comment here
 * formatted with JxBeauty (c) johann.langhofer@nextra.at
 */


package com.chelseasystems.cs.receipt;

import com.chelseasystems.cr.config.*;


public interface ReceiptBlueprintInventory {
  ConfigMgr cfg = new ConfigMgr("JPOS_peripherals.cfg");
  String CMSCompositePOSTransaction_Sale = cfg.getString("CMSCompositePOSTransaction_Sale");
  String CMSCompositePOSTransaction_EMVDecline = cfg.getString("CMSCompositePOSTransaction_EMVDecline"); // Added by Himani for Decline Receipt changes
  String RewardTransaction = cfg.getString("RewardTransaction");
  String CMSCompositePOSTransaction_Sale_Dup = cfg.getString("CMSCompositePOSTransaction_Sale_Dup");
  String CMSCompositePOSTransaction_Sale_Cancel = cfg.getString(
      "CMSCompositePOSTransaction_Sale_Cancel");
  String CMSCompositePOSTransaction_Post_Failed = cfg.getString(
      "CMSCompositePOSTransaction_Post_Failed");
  //  the cancel blueprint has been implemented for sales
  String CMSCompositePOSTransaction_Sale_Suspend = cfg.getString(
      "CMSCompositePOSTransaction_Sale_Suspend");
  String CMSCompositePOSTransaction_Sale_Sigs = cfg.getString(
      "CMSCompositePOSTransaction_Sale_Sigs");
  String CMSCollection_Sigs = cfg.getString("CMSCollection_Sigs");
  String CMSCollection = cfg.getString("CMSCollection");
  String CMSCollection_Cancel = cfg.getString("CMSCollection_Cancel");
  String CMSCollection_Suspend = cfg.getString("CMSCollection_Suspend");
  String CMSPaidOut_Sigs = cfg.getString("CMSPaidOut_Sigs");
  String CMSPaidOut = cfg.getString("CMSPaidOut");
  String CMSPaidOut_Cancel = cfg.getString("CMSPaidOut_Cancel");
  String CMSPaidOut_Suspend = cfg.getString("CMSPaidOut_Suspend");
  String CMSLayaway_Sigs = cfg.getString("CMSLayaway_Sigs");
  String CMSLayaway = cfg.getString("CMSLayaway");
  String CMSLayaway_Cancel = cfg.getString("CMSLayaway_Cancel");
  String CMSLayaway_Suspend = cfg.getString("CMSLayaway_Suspend");
  String CMSLayawayOutstanding = cfg.getString("CMSLayawayOutstanding");
  String CMSLayawayOverdue = cfg.getString("CMSLayawayOverdue");
  String CMSLayawayPayment = cfg.getString("CMSLayawayPayment");
  String CMSLayawayPayment_Cancel = cfg.getString("CMSLayawayPayment_Cancel");
  String CMSLayawayPayment_Suspend = cfg.getString("CMSLayawayPayment_Suspend");
  String CMSLayawayPayment_Sigs = cfg.getString("CMSLayawayPayment_Sigs");
  String CMSEODTotals = cfg.getString("CMSEODTotals");
  String CMSEODStoreTotals = cfg.getString("CMSEODStoreTotals");
  String CMSEODTrial = cfg.getString("CMSEODTrial");
  String CMSDueBillIssue = cfg.getString("CMSDueBillIssue");
  String CMSLayawayRTS = cfg.getString("CMSLayawayRTS");
  String CMSLayawayRTS_Cancel = cfg.getString("CMSLayawayRTS_Cancel");
  String CMSLayawayRTS_Suspend = cfg.getString("CMSLayawayRTS_Suspend");
  String CMSVoidTransaction = cfg.getString("CMSVoidTransaction");
  String CMSTransferOut = cfg.getString("CMSTransferOut");
  String CMSTransferInPartial = cfg.getString("CMSTransferInPartial");
  String CMSTransferInComplete = cfg.getString("CMSTransferInComplete");
  String CMSEmployeeSchedule = cfg.getString("CMSEmployeeSchedule");
  String CMSGiftReceipt = cfg.getString("CMSGiftReceipt");
  String CMSCompositePOSTransaction_MerchTag = cfg.getString("CMSCompositePOSTransaction_MerchTag");
  String CMSClockIn = cfg.getString("CMSClockIn");
  String CMSClockOut = cfg.getString("CMSClockOut");
  String CMSStoreInfo = cfg.getString("CMSStoreInfo");
  String CMSNoSaleTransaction = cfg.getString("CMSNoSaleTransaction");
  String CMSBuyBack = cfg.getString("CMSBuyBack");
  String CMSBuyBack_Sigs = cfg.getString("CMSBuyBack_Sigs");
  String CMSBuyBack_Cancel = cfg.getString("CMSBuyBack_Cancel");
  String CMSBuyBack_Suspend = cfg.getString("CMSBuyBack_Suspend");
  String CMSCompositePOSTransaction_Shipping = cfg.getString("CMSCompositePOSTransaction_Shipping");
  String CMSSessionStart = cfg.getString("CMSSessionStart");
  String CMSSessionEnd = cfg.getString("CMSSessionEnd");
  String CMSStartOfDay = cfg.getString("CMSStartOfDay");
  String CMSTimecardModInOut = cfg.getString("CMSTimecardModInOut");
  String CMSTimecardInsInOut = cfg.getString("CMSTimecardInsInOut");
  String CMSTimecardAddInOut = cfg.getString("CMSTimecardAddInOut");
  String CMSTimecardDelInOut = cfg.getString("CMSTimecardDelInOut");
  String CMSInvalidLogonAttempt = cfg.getString("CMSInvalidLogonAttempt");
  String CMSEmployeeResource = cfg.getString("CMSEmployeeResource");
  String CMSResendBrokenTxn = cfg.getString("CMSResendBrokenTxn");
  String CMSUpdateEmployeeFile = cfg.getString("CMSUpdateEmployeeFile");
  String CMSUpdateItemFile = cfg.getString("CMSUpdateItemFile");
  String CMSRecallParkedTxn = cfg.getString("CMSRecallParkedTxn");
  String CMSEmployeeAccessNew = cfg.getString("CMSEmployeeAccessNew");
  String CMSEmployeeAccessMod = cfg.getString("CMSEmployeeAccessMod");
  String CMSEmployeeAccessTerm = cfg.getString("CMSEmployeeAccessTerm");
  String CMSEmployeeAccessFinger = cfg.getString("CMSEmployeeAccessFinger");
  String CMSCashierSessionEvent = cfg.getString("CMSCashierSessionEvent");
  String CMSRedeemableBalance = cfg.getString("CMSRedeemableBalance");
  String CMSOperatorLogOn = cfg.getString("CMSOperatorLogOn");
  String CMSOperatorLogOff = cfg.getString("CMSOperatorLogOff");
  String CMSVATInvoice = cfg.getString("CMSVATInvoice");
  String CMSNotIssuedVATInvoice = cfg.getString("CMSNotIssuedVATInvoice");
  String CMSRetailExportReceipt = cfg.getString("CMSRetailExportReceipt");
  String CMSAlterationReceipt = cfg.getString("CMSAlterationReceipt");


  // etc.
}

