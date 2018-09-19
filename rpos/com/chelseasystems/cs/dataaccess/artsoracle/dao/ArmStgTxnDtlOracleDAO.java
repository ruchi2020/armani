/*
 * put your module comment here
 * formatted with JxBeauty (c) johann.langhofer@nextra.at
 */
//
// Copyright 2002, Retek Inc. All Rights Reserved.
//
package com.chelseasystems.cs.dataaccess.artsoracle.dao;

import java.io.ByteArrayOutputStream;
import java.sql.SQLException;
import java.sql.Types;
import java.util.*;
import java.util.zip.GZIPOutputStream;
import java.lang.Math;

import com.chelseasystems.cs.dataaccess.*;
import com.chelseasystems.cs.dataaccess.artsoracle.databean.*;
import com.chelseasystems.cr.pos.*;
import com.chelseasystems.cr.discount.Discount;
import com.chelseasystems.cs.eod.*;
import com.chelseasystems.cs.sos.CMSTransactionSOS;
import com.chelseasystems.cs.database.CMSParametricStatement;
import com.chelseasystems.cs.store.CMSStore;
import com.chelseasystems.cs.pos.*;
import com.chelseasystems.cs.payment.*;
import com.chelseasystems.cr.payment.*;
import com.chelseasystems.cs.collection.CMSMiscCollection;
import com.chelseasystems.cs.paidout.CMSMiscPaidOut;
import com.chelseasystems.cs.paidout.CMSCashDropPaidOut;
import com.chelseasystems.cr.pos.NoSaleTransaction;
import com.chelseasystems.cr.store.Store;
import com.chelseasystems.cr.transaction.CommonTransaction;
import com.chelseasystems.cr.currency.ArmCurrency;
import com.chelseasystems.cr.customer.Customer;
import com.chelseasystems.cr.database.ParametricStatement;
import com.chelseasystems.cr.config.ConfigMgr;
import com.chelseasystems.cs.customer.CMSCustomer;
import com.chelseasystems.cs.address.Address;
import com.chelseasystems.cs.employee.CMSEmployee;
import com.chelseasystems.cs.util.TransactionUtil;
import com.chelseasystems.cs.util.LineItemPOSUtil;
import com.chelseasystems.cs.fiscaldocument.FiscalDocument;
import com.chelseasystems.cs.item.CMSItem;
import com.chelseasystems.cs.discount.CMSDiscount;
import com.chelseasystems.cs.discount.CMSEmployeeDiscount;

/**
 * put your documentation comment here
 */
public class ArmStgTxnDtlOracleDAO extends BaseOracleDAO implements ArmStgTxnDtlDAO {
	public static final String RECORD_TYPE_M = "M"; // M - merchandise/item detail
	public static final String RECORD_TYPE_A = "A"; // A - tender detail
	public static final String RECORD_TYPE_V = "V";
	public static final String RECORD_TYPE_C = "C"; // C - customer details
	public static final String RECORD_TYPE_P = "P";
	public static final String RECORD_TYPE_CA = "CA";
	public static final String RECORD_TYPE_RB = "RB";
	public static final String RECORD_TYPE_FD = "FD";
	public static final String RECORD_TYPE_UA = "UA";
	public static final String RECORD_TYPE_UC = "UC";
	public static final String RECORD_TYPE_US = "US";
	public static final String POS_LINE_ITEM_TYPE_SALE = "1";
	public static final String POS_LINE_ITEM_TYPE_RETURN = "2";
	public static final String POS_LINE_ITEM_TYPE_LAYAWAY = "3";
	public static final String POS_LINE_ITEM_TYPE_PRESALE = "6";
	public static final String POS_LINE_ITEM_TYPE_CONSIGNMENT = "5";
	public static final String POS_LINE_ITEM_TYPE_RESERVATION = "4";
	public static final String POS_LINE_ITEM_TYPE_VOID = "7";
	public static final String POS_LINE_ITEM_TYPE_NOSALE = "8";
	public static final String POS_LINE_ITEM_TYPE_EXCHANGE = "9";
	public static final String POS_LINE_ITEM_TYPE_NORETURN = "10";
//	Added by Rachana for approval of return transaction
	// Vishal Yevale : addded this fields for Eur MARKUP AND MARKDOWN CR 15 JAN 2017
	public String approverId = null;
	public Double priceDMS = null;
	public Double priceOUT = null;
	public Double coeff = null;
	public boolean calcMkdwDisc =false;
	public String storeType = null;
	public String country = null;
	public boolean isOnFile = true;

//END VISHAL YEVALE
	/**
	 * put your documentation comment here
	 * @param object
	 * @return
	 * @exception SQLException
	 */
	public ParametricStatement[] getEODTxnDtlInsertSQL(CMSTransactionEOD object) throws SQLException {
		try {
			ConfigMgr config = new ConfigMgr("payment.cfg");
			List statements = new ArrayList();
			ArmStgTxnDtlOracleBean bean = null;
			List eodPymts = new ArrayList();
			String strEODPymts = config.getString("EOD_PAYMENTS");
			StringTokenizer st = new StringTokenizer(strEODPymts, ",");
			while(st.hasMoreTokens()) {
				eodPymts.add(st.nextToken());
			}
			Hashtable ht = object.getEODTenderTotals();
			String tndType = "";
			for (Enumeration enm = ht.keys(); enm.hasMoreElements();) {
				tndType = (String) enm.nextElement();
				// if (!eodPymts.contains(tndType)) {
				bean = new ArmStgTxnDtlOracleBean();
				bean.setDataPopulationDt(new Date());
				bean.setRecordType(RECORD_TYPE_CA);
				bean.setTransactionId(object.getId());
				bean.setEodTndType(tndType);
				if (tndType.equals("CASH")) {
					bean.setEodReportedTndTotal(fromCurrenciesToString(object.getMgrSaysCash()));
				} else if (tndType.equals("CHECK")) {
					bean.setEodReportedTndTotal(fromCurrenciesToString(object.getMgrSaysCheck()));
				} else
					bean.setEodReportedTndTotal(((CMSEODNonDepositTotal) ht.get(tndType)).getReportedEODTotal().toDelimitedString());
				bean.setEodTndTotal(((CMSEODNonDepositTotal) ht.get(tndType)).getEODTotal().toDelimitedString());
				statements.add(new ParametricStatement(ArmStgTxnDtlOracleBean.insertSql, bean.toList()));
				// }
			}
			return (ParametricStatement[]) statements.toArray(new ParametricStatement[0]);
		} catch (Exception e) {
			e.printStackTrace();
			throw new SQLException();
		}
	}

	/**
	 * put your documentation comment here
	 * @param object
	 * @return
	 * @exception SQLException
	 */
	public ParametricStatement[] getSOSTxnDtlInsertSQL(CMSTransactionSOS object) throws SQLException {
		try {
			List statements = new ArrayList();
			//Vishal Yevale :CR from Riccardo Start of day Posting 21th March 2017
			CMSStore store=(CMSStore) object.getStore();
			if(store!=null && store.getDigitalSignatureSOS()!=null && store.getDigitalSignatureSOS().length()>=1)
			statements.addAll(Arrays.asList(addDigitalSignature(object)));
			//end Vishal Yevale 21 March 2017
			ArmStgTxnDtlOracleBean bean = new ArmStgTxnDtlOracleBean();
			bean.setDataPopulationDt(new Date());
			bean.setRecordType(RECORD_TYPE_CA);
			bean.setTransactionId(object.getId());
			bean.setOpeningDrwrFnd(currencyVal(object.getRegister().getDrawerFund()));
			statements.add(new ParametricStatement(ArmStgTxnDtlOracleBean.insertSql, bean.toList()));
			return (ParametricStatement[]) statements.toArray(new ParametricStatement[0]);
		} catch (Exception e) {
			e.printStackTrace();
			throw (SQLException) e;
		}
	}

	//Vishal Yevale : added Method for CR from Riccardo Start of day Posting 21th March 2017
	private ParametricStatement[] addDigitalSignature(CommonTransaction object) {
		List statements = new ArrayList();
		RkPayTrnOracleBean bean = new RkPayTrnOracleBean();
		bean.setAiTrn(object.getId());
		bean.setTotalAmt(new ArmCurrency(0.0));
		bean.setTypeId("TRNPOS");		
		bean.setPayTypes("*SOS*");
		statements.add(new ParametricStatement(RkPayTrnOracleBean.insertSql, bean.toList()));
		CMSStore store=(CMSStore)object.getStore();
		 TrRtlOracleBean trRtlOracleBean = new TrRtlOracleBean();   
		 trRtlOracleBean.setAiTrn(object.getId());
		 trRtlOracleBean.setIdCt("59999999");
		 trRtlOracleBean.setConsultantId(object.getTheOperator().getId());
		 trRtlOracleBean.setRegisterId(object.getRegisterId());
		 trRtlOracleBean.setDigitalSignature(store.getDigitalSignatureSOS());
		 trRtlOracleBean.setNetAmount(new ArmCurrency(0.0));
		 trRtlOracleBean.setReductionAmount(new ArmCurrency(0.0));
		 trRtlOracleBean.setDiscountTypes("*");
		 trRtlOracleBean.setItemsIds("*");
		 trRtlOracleBean.setFiscalDocNumbers("0");
		 statements.add(new CMSParametricStatement(TrRtlOracleBean.insertSql, trRtlOracleBean.toList(),true));
		return (ParametricStatement[]) statements.toArray(new ParametricStatement[0]);
	}
	//end Vishal Yevale 21 March 2017
	/**
	 * put your documentation comment here
	 * @param object
	 * @return
	 * @exception SQLException
	 */
	public ParametricStatement[] processPaymentTxnDtl(PaymentTransaction object) throws SQLException {
		try {
			List statements = new ArrayList();
			statements.addAll(Arrays.asList(getCOLPOTNOSTxnDtlInsertSql(object)));
			statements.addAll(Arrays.asList(getVODTxnDtlInsertSql(object)));
			statements.addAll(Arrays.asList(getBBKTxnDtlInsertSql(object)));
			statements.addAll(Arrays.asList(getPOSTxnDtlInsertSql(object)));
			return (ParametricStatement[]) statements.toArray(new ParametricStatement[0]);
		} catch (Exception e) {
			e.printStackTrace();
			throw new SQLException();
		}
	}

	/**
	 * put your documentation comment here
	 * @param object
	 * @return
	 * @exception SQLException
	 */
	public ParametricStatement[] getPAYTxnDtlInsertSql(PaymentTransaction object) throws SQLException {
		try {
			//Vivek Mishra : Added countryid parameter to restrict use of global variable countryId in order to resolve CAD_CASH issue : 14-JUL-2016
			String countryId = object.getStore().getCountry();
			//Ends here
			List statements = new ArrayList();
			ArmStgTxnDtlOracleBean bean = null;		
			int lineId = 0;
			Payment[] pymtArr = object.getPaymentsArray();
			for (int i = 0; i < pymtArr.length; i++) {
				bean = new ArmStgTxnDtlOracleBean();
				bean.setDataPopulationDt(new Date());
				bean.setRecordType("A");
				bean.setTransactionId(object.getId());
				//Vivek Mishra : Added fix for wrong Credit Card tender type issue in case of Mobile terminal US 02-DEC-2015
				if(pymtArr[i] instanceof CreditCard && ((CreditCard)pymtArr[i]).getCreditCardHolderName().equalsIgnoreCase("Mobile"))
				{
					bean.setTndType(((CreditCard)pymtArr[i]).getTenderType());
				}
				else
				{
				//Vivek Mishra : Added countryid argument in order to resolve CAD_CASH issue : 14-JUL-2016	
				//	bean.setTndType(TransactionUtil.getPaymentTypeId(pymtArr[i]);
					//Vivek Mishra : Added to save TND_TYPE as CREDIT_CARD for Japan region 18-OCT-2016
				    if(object.getStore().getCountry()!=null && object.getStore().getCountry().equalsIgnoreCase("JAPAN"))
				    {
				    	if(pymtArr[i].getTransactionPaymentName()!=null && pymtArr[i].getTransactionPaymentName().equals(PAYMENT_TYPE_CREDIT_CARD))
				            bean.setTndType(PAYMENT_TYPE_CREDIT_CARD);	
				    	else
				bean.setTndType(TransactionUtil.getPaymentTypeId(pymtArr[i],countryId));
				    }
				    else{
				    	bean.setTndType(TransactionUtil.getPaymentTypeId(pymtArr[i],countryId));
				    }
					//Ends here 18-OCT-2016
				//Ends here
				}
				lineId = lineId + 1;
				bean.setLineId(lineId);
				bean.setTndCode(pymtArr[i].getPaymentCode()); // for Europe
				bean.setTndAmount(currencyVal(pymtArr[i].getAmount()));
				
				//Added by Rachana for approval of return transaction
				if (object instanceof CompositePOSTransaction) {
					CMSCompositePOSTransaction cmpObj = (CMSCompositePOSTransaction) object;
					if(cmpObj.getTotalPaymentAmount().stringValue().startsWith("-")){
						bean.setApproverId(cmpObj.getApprover());
						bean.setTndCode(pymtArr[i].getPaymentCode()); 
					}
				}
				bean.setTndRespJournalKey(pymtArr[i].getJournalKey());
				//Vivek Mishra : Added countryid check to restrict invocation of this AJB specific code for region other than US : 06-SEP-2016
			    countryId = object.getStore().getCountry();
				if(countryId.equalsIgnoreCase("USA")||countryId.equalsIgnoreCase("CAN")){
				//Anjana added below to set tender journal key when AJb is offline with POS
				if(pymtArr[i].getJournalKey()==null){
					 String journalKey = null; 
			    	 long lastJournalKey = 0;
			    	  String str = object.getId();
			    	  String[] temp;
			    	 
			    	  /* delimiter */
			    	  String delimiter = "\\*";
			    	  /* given string will be split by the argument delimiter provided. */
			    	  temp = str.split(delimiter);
			    	  /* print substrings */
			    
			    	 String storeNum = temp[0];
			    	 String registerId = temp[1];
			  
			    	    String timeStampStr = "" + new Date().getTime();
			    	    //Store ID of 4 digits
			    	    journalKey = getRightJustifiedNumber(storeNum.trim(), 4);
			    	    //Register ID of 3 digits
			    	    journalKey += getRightJustifiedNumber(registerId.trim(), 3);
			    	    //TimeStamp of 9 least significant digits with max precision=10 seconds
			    	    journalKey += getRightJustifiedNumber(timeStampStr.substring(0, timeStampStr.length() - 4), 9);
			    	    if (Long.parseLong(journalKey) <= lastJournalKey)
			    	      journalKey = "" + (lastJournalKey + 1);
			    	    lastJournalKey = Long.parseLong(journalKey);
			    	    bean.setTndRespJournalKey(journalKey);
				}
					}
				bean.setTndRespMsg(pymtArr[i].getRespMessage());
				bean.setTndRespMsgNum(pymtArr[i].getMessageNumber());
				bean.setTndMerchantId(pymtArr[i].getMerchantID());
				// BankCheck
				if (pymtArr[i] instanceof BankCheck) {
					bean.setTndIdAcntNmb(((BankCheck) pymtArr[i]).getAccountNumber());
					bean.setTndRespAuth(((BankCheck) pymtArr[i]).getRespAuthorizationCode());
					bean.setTndApprovalDate(((BankCheck) pymtArr[i]).getApprovalDate());
					if (((BankCheck) pymtArr[i]).getManualOverride())
						bean.setTndManualOverride("1");
					else
						bean.setTndManualOverride("0");
					bean.setChkAbaNum(((BankCheck) pymtArr[i]).getTransitNumber());
					// bean.setChkRoutingNum(); // for Europe
					bean.setChkBank(((BankCheck) pymtArr[i]).getBankNumber());
					if (((BankCheck) pymtArr[i]).getIsCheckScannedIn())
						bean.setChkIsScanned("1");
					else
						bean.setChkIsScanned("0");
				}
				// CreditCard
				if (pymtArr[i] instanceof CreditCard) {
					bean.setTndIdAcntNmb(((CreditCard) pymtArr[i]).getAccountNumber());
					bean.setTndRespAuth(((CreditCard) pymtArr[i]).getRespAuthorizationCode());
					bean.setTndHolderName(((CreditCard) pymtArr[i]).getCreditCardHolderName());
					bean.setTndExpirationDt(((CreditCard) pymtArr[i]).getExpirationDate());
					bean.setTndCardIdentifier(((CreditCard) pymtArr[i]).getCardIdentifier());
					bean.setCcCvvCode(((CreditCard) pymtArr[i]).getCv2SecurityCode());
					bean.setCcCardPlan(((CreditCard) pymtArr[i]).getCardPlanCode());
					  //Anjana added below columns to save same data in staging table as core table:- tr_ltm_crdb_crd_tn
					if(((CreditCard) pymtArr[i]).getTokenNo()!=null)
					bean.setCardToken(((CreditCard) pymtArr[i]).getTokenNo());
					 //Anjana : Added for setting the Signature response from AJB in Byte Code format
				    if(((CreditCard) pymtArr[i]).isSignatureValid()){   
				        //byte[] sign = Base64.decodeBase64((object.getSignatureAscii()).getBytes());
				    	byte[] sign = ((CreditCard) pymtArr[i]).getSignByteCode();
				         //Vivek Mishra : Added logic for compressing the Byte Array before saving it to the BOLB as there is limitation of BLOB that it can't store byte code greater than 4k(4000 Bytes).
				        try
				        {
				          ByteArrayOutputStream byteStream =
				            new ByteArrayOutputStream(sign.length);
				          try
				          {
				            GZIPOutputStream zipStream =
				              new GZIPOutputStream(byteStream);
				            try
				            {
				              zipStream.write(sign);
				            }
				            finally
				            {
				              zipStream.close();
				            }
				          }
				          finally
				          {
				            byteStream.close();
				          }

				          byte[] compressedData = byteStream.toByteArray();
				         //Setting the compressed byte array here.
				          bean.setCustSignature(compressedData);
				        
				        }catch(Exception e)
				        {
				        	e.printStackTrace();
				        }
				    }
					//Ends
				        
					
				    
					//ruchi for Canada
					if(object.getStore().getCountry().equals("CAN"))
					if(((CreditCard) pymtArr[i]).getTenderType().equalsIgnoreCase("Debit")&& pymtArr[i].getGUIPaymentNameForMobileTerminal().equalsIgnoreCase("Debit")){
						bean.setTndType("DINERS");
					}else
						//Vivek Mishra : Added to save TND_TYPE as CREDIT_CARD for Japan region 18-OCT-2016
					    if(object.getId()!=null && object.getId().contains("*"))
					    {
					    	String tempTransactionId = object.getId();
					    	tempTransactionId = tempTransactionId.replace("*", ",");
					    	String[] txnIdBkd = tempTransactionId.split(",");
					    	if(pymtArr[i].getTransactionPaymentName()!=null && pymtArr[i].getTransactionPaymentName().equals(PAYMENT_TYPE_CREDIT_CARD) && txnIdBkd[0].length()>5)
					        bean.setTndType(PAYMENT_TYPE_CREDIT_CARD);	
					    }
					    else{
					    	bean.setTndType(TransactionUtil.getPaymentTypeId(pymtArr[i],countryId));
					    }
					    //Ends here 18-OCT-2016
					//ISD Changes
					//Commenting ISD Changes

//					if ((((CreditCard) pymtArr[i]).getEncryptedData()) != null){
//						bean.setEncryptedString(((CreditCard) pymtArr[i]).getEncryptedData());
//						bean.setKeyId(((CreditCard) pymtArr[i]).getKey_id());
//					}
					//ended
					if (((CreditCard) pymtArr[i]).isManuallyKeyed())
						bean.setTndSwipeInd("0");
					else
						bean.setTndSwipeInd("1");
					bean.setTndRespAddressVerif(((CreditCard) pymtArr[i]).getRespAddressVerification());
					if (pymtArr[i] instanceof Amex)
						bean.setCcAmexCidNum(((Amex) pymtArr[i]).getCidNumber());
				}
				if (pymtArr[i] instanceof Coupon) {
					bean.setRdmTndIdStrRt(((Coupon) pymtArr[i]).getStoreId()); 
					bean.setRdmDcCpnScanCode(((Coupon) pymtArr[i]).getScanCode());
					bean.setRdmTndPrmCode(((Coupon) pymtArr[i]).getPromotionCode());
					bean.setRdmExpirationDate(((Coupon) pymtArr[i]).getExpirateDate());
					bean.setTndCode(((Coupon) pymtArr[i]).getType());
				}
				if (pymtArr[i] instanceof Cash) {
				ArmCurrency tndAmount = pymtArr[i].getAmount();
					if (tndAmount.isConverted()) {
						bean.setFcXchangeRate(ArmCurrency.getConversionRate(tndAmount, tndAmount.getConvertedFrom()));
						bean.setFcFromCurrency(tndAmount.getConvertedFrom().getCurrencyType().getCode());
						bean.setFcToCurrency(tndAmount.getCurrencyType().getCode());
					}
					//if(!pymtArr[i].getPaymentCode().equals(null))
					/*System.out.println("Ruchi printing the code before setting :"+pymtArr[i].getPaymentCode());
					String tenderCode = null;
					if(pymtArr[i].getPaymentCode() !=  null){
					 tenderCode = pymtArr[i].getPaymentCode(); 
					 System.out.println("ruchi tender code   :"+tenderCode);
					 }
					//ruchi printing tender code
					System.out.println("Ruchi printing the code :"+pymtArr[i].getPaymentCode());
					System.out.println("Ruchi printing transactionpaymentname  :"+pymtArr[i].getTransactionPaymentName());
					System.out.println("Ruchi printing tndAmoutn  :"+tndAmount);
					System.out.println("Ruchi printing tendercode  :"+tenderCode);
					if(tndAmount.stringValue().startsWith("-") && (pymtArr[i].getPaymentCode().equals(null))){
					 bean.setTndCode(tenderCode);	
					 System.out.println("Ruchi printing tenderCode  "+tenderCode);	
					}*/
				}
				if (pymtArr[i] instanceof Redeemable) {
					bean.setRdmControlNumber(((Redeemable) pymtArr[i]).getId());
					bean.setRdmFaceValueAmt(currencyVal(((Redeemable) pymtArr[i]).getIssueAmount().getAbsoluteValue()));
					bean.setRdmBalanceAmt(currencyVal(((Redeemable) pymtArr[i]).getRemainingBalance()));
					if (pymtArr[i] instanceof CMSDueBillIssue) {
						bean.setRdmControlNumber(((CMSDueBillIssue) pymtArr[i]).getControlNum());
						bean.setRdmType("DUE_BILL");
						bean.setRdmIssuingStore(((CMSDueBillIssue) pymtArr[i]).getStoreId());
						bean.setRdmCustNum(((CMSDueBillIssue) pymtArr[i]).getCustomerId());
						bean.setRdmIssuanceDt(((CMSDueBillIssue) pymtArr[i]).getIssuanceDate());
						bean.setRdmExpirationDate(((CMSDueBillIssue) pymtArr[i]).getExpirationDate());
					}
					if (pymtArr[i] instanceof CMSStoreValueCard) {
						bean.setRdmControlNumber(((CMSStoreValueCard) pymtArr[i]).getControlNum());
						bean.setRdmType("GIFT_CARD");
						bean.setRdmIssuingStore(((CMSStoreValueCard) pymtArr[i]).getStoreId());
						bean.setRdmCustNum(((CMSStoreValueCard) pymtArr[i]).getCustomerId());
						bean.setRdmIssuanceDt(((CMSStoreValueCard) pymtArr[i]).getIssuanceDate());
						bean.setRdmExpirationDate(((CMSStoreValueCard) pymtArr[i]).getExpirationDate());
					}
					if (pymtArr[i] instanceof HouseAccount) {
						bean.setRdmControlNumber(((HouseAccount) pymtArr[i]).getControlNum());
						bean.setRdmType("HOUSE_ACCOUNT");
						bean.setRdmCustNum(((HouseAccount) pymtArr[i]).getCustomerId());
					}
					if (pymtArr[i] instanceof CMSRedeemable) {
						bean.setRdmControlNumber(((CMSRedeemable) pymtArr[i]).getControlNum());
						if (pymtArr[i].getTransactionPaymentName().equals("STORE_VALUE_CARD")) {
							bean.setRdmType("GIFT_CARD");
						}
						if (pymtArr[i].getTransactionPaymentName().equals("CREDIT_MEMO")) {
							bean.setRdmType("DUE_BILL");
						}
						bean.setRdmFaceValueAmt(currencyVal(((CMSRedeemable) pymtArr[i]).getIssueAmount().getAbsoluteValue()));
						bean.setRdmBalanceAmt(currencyVal(((CMSRedeemable) pymtArr[i]).getBalance()));
						bean.setRdmCustNum(((CMSRedeemable) pymtArr[i]).getCustomerId());
					}
				}
				statements.add(new ParametricStatement(ArmStgTxnDtlOracleBean.insertSql, bean.toList()));
			}		
			return (ParametricStatement[]) statements.toArray(new ParametricStatement[0]);
		} catch (Exception e) {
			e.printStackTrace();
			throw new SQLException();
		}
	}

	
	//Anjana added below to save RESP_ID when POS is offline with AJB
	  private static String getRightJustifiedNumber(String orgString, int leng) {
		    String inStr = orgString.trim();
		    int diff = leng - inStr.length();
		    String result = null;
		    if (diff > 0) {
		      String temp = createZeroString(diff);
		      result = temp.concat(inStr);
		    } else if (diff < 0) {
		      //System.err.println("ERROR -- input too large.  Input: >" + inStr + "<   Max. length: " + leng);
		      result = inStr.substring(0 - diff, inStr.length());
		    } else {
		      result = new String(inStr);
		    }
		    return result;
		  }
	  private static String createZeroString(int size) {
		    char array[] = new char[size];
		    for (int i = 0; i < size; i++)
		      array[i] = '0';
		    String str = new String(array);
		    array = null;
		    return str;
		  }
	  //Ends
	/**
	 * put your documentation comment here
	 * @param object
	 * @return
	 * @exception SQLException
	 */
	public ParametricStatement[] getCOLPOTNOSTxnDtlInsertSql(PaymentTransaction object) throws SQLException {
			try {
			List statements = new ArrayList();
			ArmStgTxnDtlOracleBean bean = null;
			
			if (object instanceof CMSMiscCollection) {
				bean = new ArmStgTxnDtlOracleBean();
				bean.setDataPopulationDt(new Date());
				bean.setRecordType(RECORD_TYPE_P);
				bean.setTransactionId(object.getId());
				bean.setCashierTxnComment(((CMSMiscCollection) object).getComment());
				bean.setCashierTxnReason(((CMSMiscCollection) object).getType());
				bean.setNetAmt(((CMSMiscCollection) object).getAmount().doubleValue());
				if (((CMSMiscCollection) object).getRedeemable() instanceof HouseAccount)
					bean.setRdmControlNumber(((CMSMiscCollection) object).getRedeemable().getId());
				statements.add(new ParametricStatement(ArmStgTxnDtlOracleBean.insertSql, bean.toList()));
				// payments
				statements.addAll(Arrays.asList(getPAYTxnDtlInsertSql(object)));
				// Customer record type "C"
				CMSCustomer cust = ((CMSMiscCollection) object).getCustomer();
				if (cust != null) {
					bean = new ArmStgTxnDtlOracleBean();
					bean.setDataPopulationDt(new Date());
					bean.setRecordType(RECORD_TYPE_C);
					bean.setTransactionId(object.getId());
					bean.setCustomerRole("1");
					bean.setCustId(cust.getId());
					bean.setCustFirstName(cust.getFirstName());
					bean.setCustLastName(cust.getLastName());

					Address addr = cust.getPrimaryAddress();
					if (addr != null) {
						bean.setCustAddr1(addr.getAddressLine1());
						bean.setCustAddr2(addr.getAddressLine2());
						bean.setCustCity(addr.getCity());
						bean.setCustState(addr.getState());
						bean.setCustPcode(addr.getZipCode());
						bean.setCustCountry(addr.getCountry());
						if (addr.getPrimaryPhone() != null)
							bean.setCustPhone1(addr.getPrimaryPhone().getTelephoneNumber());
						if (addr.getSecondaryPhone() != null)
							bean.setCustPhone2(addr.getSecondaryPhone().getTelephoneNumber());
					}
					statements.add(new ParametricStatement(ArmStgTxnDtlOracleBean.insertSql, bean.toList()));
				}
			}//end of CMSMiscCollection
			if (object instanceof CMSMiscPaidOut) {
				bean = new ArmStgTxnDtlOracleBean();
				bean.setDataPopulationDt(new Date());
				bean.setRecordType(RECORD_TYPE_P);
				bean.setTransactionId(object.getId());
				bean.setCashierTxnComment(((CMSMiscPaidOut) object).getComment());
				bean.setCashierTxnReason(((CMSMiscPaidOut) object).getType());
				bean.setNetAmt(((CMSMiscPaidOut) object).getAmount().doubleValue());
				statements.add(new ParametricStatement(ArmStgTxnDtlOracleBean.insertSql, bean.toList()));
				// payments
				statements.addAll(Arrays.asList(getPAYTxnDtlInsertSql(object)));
				// Customer record type "C"
				CMSCustomer cust = ((CMSMiscPaidOut) object).getCustomer();
				if (cust != null) {
					bean = new ArmStgTxnDtlOracleBean();
					bean.setDataPopulationDt(new Date());
					bean.setRecordType(RECORD_TYPE_C);
					bean.setTransactionId(object.getId());
					bean.setCustomerRole("1");
					bean.setCustId(cust.getId());
					bean.setCustFirstName(cust.getFirstName());
					bean.setCustLastName(cust.getLastName());
					Address addr = cust.getPrimaryAddress();
					if (addr != null) {
						bean.setCustAddr1(addr.getAddressLine1());
						bean.setCustAddr2(addr.getAddressLine2());
						bean.setCustCity(addr.getCity());
						bean.setCustState(addr.getState());
						bean.setCustPcode(addr.getZipCode());
						bean.setCustCountry(addr.getCountry());
						if (addr.getPrimaryPhone() != null)
							bean.setCustPhone1(addr.getPrimaryPhone().getTelephoneNumber());
						if (addr.getSecondaryPhone() != null)
							bean.setCustPhone2(addr.getSecondaryPhone().getTelephoneNumber());
					}
					statements.add(new ParametricStatement(ArmStgTxnDtlOracleBean.insertSql, bean.toList()));
				}
			}// end of CMSMiscPaidOut
			if (object instanceof CMSCashDropPaidOut) {
				bean = new ArmStgTxnDtlOracleBean();
				bean.setDataPopulationDt(new Date());
				bean.setRecordType(RECORD_TYPE_P);
				bean.setTransactionId(object.getId());
				bean.setCashierTxnComment(((CMSCashDropPaidOut) object).getComment());
				bean.setCashierTxnReason(((CMSCashDropPaidOut) object).getType());
				bean.setNetAmt(((CMSCashDropPaidOut) object).getAmount().doubleValue());
				statements.add(new ParametricStatement(ArmStgTxnDtlOracleBean.insertSql, bean.toList()));
				// payments
				statements.addAll(Arrays.asList(getPAYTxnDtlInsertSql(object)));
			}
			if (object instanceof PointManagement) {
				bean = new ArmStgTxnDtlOracleBean();
				bean.setRecordType(RECORD_TYPE_P);
				bean.setTransactionId(object.getId());
				bean.setRdmControlNumber(((PointManagement) object).getLoyalty().getLoyaltyNumber());
				bean.setCustId(((PointManagement) object).getLoyalty().getCustomer().getId());
				statements.add(new ParametricStatement(ArmStgTxnDtlOracleBean.insertSql, bean.toList()));
			}
			if (object instanceof RewardTransaction) {
				bean = new ArmStgTxnDtlOracleBean();
				bean.setRecordType(RECORD_TYPE_P);
				bean.setTransactionId(object.getId());
				bean.setRdmControlNumber(((RewardTransaction) object).getRewardCard().getControlNum());
				bean.setCustId(((RewardTransaction) object).getRewardCard().getLoyalty().getCustomer().getId());
				statements.add(new ParametricStatement(ArmStgTxnDtlOracleBean.insertSql, bean.toList()));
			}
			if (object instanceof NoSaleTransaction) {
				bean = new ArmStgTxnDtlOracleBean();
				bean.setDataPopulationDt(new Date());
				bean.setRecordType(RECORD_TYPE_P);
				bean.setTransactionId(object.getId());
				bean.setCashierTxnReason(((CMSNoSaleTransaction) object).getComment());
				statements.add(new ParametricStatement(ArmStgTxnDtlOracleBean.insertSql, bean.toList()));
			}
			return (ParametricStatement[]) statements.toArray(new ParametricStatement[0]);
		} catch (Exception e) {
			e.printStackTrace();
			throw new SQLException();
		}
	}

	/**
	 * put your documentation comment here
	 * @param object
	 * @return
	 * @exception SQLException
	 */
	public ParametricStatement[] getVODTxnDtlInsertSql(PaymentTransaction object) throws SQLException {
		try {
			List statements = new ArrayList();
			ArmStgTxnDtlOracleBean bean = new ArmStgTxnDtlOracleBean();
			bean.setDataPopulationDt(new Date());
			if (object instanceof VoidTransaction) {
				bean.setRecordType(RECORD_TYPE_V);
				bean.setTransactionId(object.getId());
				bean.setPostVoidedTransNo(((VoidTransaction) object).getOriginalTransaction().getId());
				bean.setPostVoidReason(((VoidTransaction) object).getReason());
				bean.setPostVoidRegister(((Transaction) ((VoidTransaction) object).getOriginalTransaction()).getRegisterId());
				statements.add(new ParametricStatement(ArmStgTxnDtlOracleBean.insertSql, bean.toList()));
			}
			return (ParametricStatement[]) statements.toArray(new ParametricStatement[0]);
		} catch (Exception e) {
			e.printStackTrace();
			throw new SQLException();
		}
	}

	/**
	 * put your documentation comment here
	 * @param object
	 * @return
	 * @exception SQLException
	 */
	public ParametricStatement[] getBBKTxnDtlInsertSql(PaymentTransaction object) throws SQLException {
		try {
			List statements = new ArrayList();
			ArmStgTxnDtlOracleBean bean = null;
			if (object instanceof RedeemableBuyBackTransaction) {
				bean = new ArmStgTxnDtlOracleBean();
				bean.setDataPopulationDt(new Date());
				Redeemable rdmObj = ((RedeemableBuyBackTransaction) object).getRedeemable();
				bean.setRecordType(RECORD_TYPE_RB);
				bean.setTransactionId(object.getId());
				bean.setRdmControlNumber(((RedeemableBuyBackTransaction) object).getRedeemable().getId());
				if (rdmObj instanceof CMSDueBillIssue)
					bean.setRdmType("DUE_BILL");
				if (rdmObj instanceof CMSStoreValueCard)
					bean.setRdmType("GIFT_CARD");
				if (rdmObj instanceof HouseAccount)
					bean.setRdmType("HOUSE_ACCOUNT");
				bean.setRdmBuybackAmt(currencyVal(((RedeemableBuyBackTransaction) object).getAmount()));
				bean.setRdmBuybackComment(((RedeemableBuyBackTransaction) object).getComment());
				CMSCustomer cust = ((CMSRedeemableBuyBackTransaction) object).getCustomer();
				if (cust != null) {
					bean.setCustId(cust.getId());
				}
				statements.add(new ParametricStatement(ArmStgTxnDtlOracleBean.insertSql, bean.toList()));
				statements.addAll(Arrays.asList(getPAYTxnDtlInsertSql(object)));
				// Redeemable Buyback Txn
				// Customer record type "C"
				if (cust != null) {
					bean = new ArmStgTxnDtlOracleBean();
					bean.setDataPopulationDt(new Date());
					bean.setRecordType(RECORD_TYPE_C);
					bean.setTransactionId(object.getId());
					bean.setCustomerRole("1");
					bean.setCustId(cust.getId());
					bean.setCustFirstName(cust.getFirstName());
					bean.setCustLastName(cust.getLastName());
					Address addr = cust.getPrimaryAddress();
					if (addr != null) {
						bean.setCustAddr1(addr.getAddressLine1());
						bean.setCustAddr2(addr.getAddressLine2());
						bean.setCustCity(addr.getCity());
						bean.setCustState(addr.getState());
						bean.setCustPcode(addr.getZipCode());
						bean.setCustCountry(addr.getCountry());
						if (addr.getPrimaryPhone() != null)
							bean.setCustPhone1(addr.getPrimaryPhone().getTelephoneNumber());
						if (addr.getSecondaryPhone() != null)
							bean.setCustPhone2(addr.getSecondaryPhone().getTelephoneNumber());
					}
					statements.add(new ParametricStatement(ArmStgTxnDtlOracleBean.insertSql, bean.toList()));
				}
			}
			return (ParametricStatement[]) statements.toArray(new ParametricStatement[0]);
		} catch (Exception e) {
			e.printStackTrace();
			throw new SQLException();
		}
	}

	/**
	 * put your documentation comment here
	 * @param object
	 * @return
	 * @exception SQLException
	 */
	public ParametricStatement[] getPOSTxnDtlInsertSql(PaymentTransaction object) throws SQLException {
		try {
			List statements = new ArrayList();
			ArmStgTxnDtlOracleBean bean = null;
			POSLineItem[] lnItmArr = null;
					
			// check for CompositePOSTransaction
			if (object instanceof CompositePOSTransaction) {
				CMSCompositePOSTransaction posTxn = (CMSCompositePOSTransaction) object;
				lnItmArr = posTxn.getLineItemsArray();
				// Vishal Yevale : 13 Jan 2017 : added code to define storeType (markdown markup cr by sergio for EUROPE)
				if(lnItmArr[0]!=null){
					CMSStore store = (CMSStore) lnItmArr[0].getTransaction().getCompositeTransaction().getStore();
					if(store!=null){
						 storeType = store.getStoreType();
						if(storeType!=null && (storeType.equalsIgnoreCase("1") || storeType.equalsIgnoreCase("2"))){
							country = ((CMSStore)store).getCompanyCode();
							calcMkdwDisc = true;
						}
					}
				}
				//end Vishal : 13 jan 2017
				if (lnItmArr != null && lnItmArr.length > 0) {
					// iterate through the line items
					for (int i = 0; i < lnItmArr.length; i++) {
						// Sale line item
						if (lnItmArr[i] instanceof SaleLineItem) {
							SaleLineItem saleLnItm = (SaleLineItem) lnItmArr[i];
							statements.addAll(Arrays.asList(processSaleLineItems(saleLnItm)));
						} // end Sale line item
						// Return line item
						if (lnItmArr[i] instanceof ReturnLineItem) {
							ReturnLineItem rtnLnItm = (ReturnLineItem) lnItmArr[i];
							statements.addAll(Arrays.asList(processReturnLineItems(rtnLnItm)));
						} // end Return line item
						// Presale line item
						if (lnItmArr[i] instanceof CMSPresaleLineItem) {
							CMSPresaleLineItem prsLnItm = (CMSPresaleLineItem) lnItmArr[i];
							statements.addAll(Arrays.asList(processPresaleLineItems(prsLnItm)));
						} // end Presale line item
						// Consignment line item
						if (lnItmArr[i] instanceof CMSConsignmentLineItem) {
							CMSConsignmentLineItem csgLnItm = (CMSConsignmentLineItem) lnItmArr[i];
							statements.addAll(Arrays.asList(processConsignmentLineItems(csgLnItm)));
						} // end Consignment line item
						// Reservation line item
						if (lnItmArr[i] instanceof CMSReservationLineItem) {
							CMSReservationLineItem rsvLnItm = (CMSReservationLineItem) lnItmArr[i];
							statements.addAll(Arrays.asList(processReservationLineItems(rsvLnItm)));
						} // end Presale line item
						
						
					} // end iterate through the line items
					// Payments record type 'A'
					statements.addAll(Arrays.asList(getPAYTxnDtlInsertSql(object)));
				} // end if there are any line items
				// Customer record type 'C'
				statements.addAll(Arrays.asList(processCustomerDetails(bean, posTxn)));
			} // end check for CompositePOSTransaction
			return (ParametricStatement[]) statements.toArray(new ParametricStatement[0]);
		} catch (Exception e) {
			e.printStackTrace();
			throw new SQLException();
		}
	}

	/**
	 * put your documentation comment here
	 * @param currency
	 * @return
	 */
	private double currencyVal(ArmCurrency currency) {
		if (currency != null)
			return currency.getDoubleValue();
		else
			return 0.0d;
	}

	/**
	 * put your documentation comment here
	 * @param currencies[]
	 * @return
	 */
	private String fromCurrenciesToString(ArmCurrency currencies[]) {
		if (currencies == null || currencies.length == 0)
			return "";
		StringBuffer sb = new StringBuffer();
		for (int i = 0; i < currencies.length; i++)
			sb.append(currencies[i].toDelimitedString() + "*");
		return sb.toString();
	}

	/**
	 * @param posTxn
	 * @return
	 */
	private ParametricStatement[] processLineItems(CMSCompositePOSTransaction posTxn) {
		List statements = new ArrayList();
		return (ParametricStatement[]) statements.toArray(new ParametricStatement[0]);
	}

	/**
	 * @param posTxn
	 * @return
	 */
	private ParametricStatement[] processSaleLineItems(SaleLineItem lineItem) {
		List statements = new ArrayList();
		ArmStgTxnDtlOracleBean bean = initMRecordType(lineItem);
		if (lineItem instanceof CMSNoSaleLineItem) {
			bean.setLineItemType(POS_LINE_ITEM_TYPE_NOSALE);
			statements.add(new ParametricStatement(ArmStgTxnDtlOracleBean.insertSql, bean.toList()));
			// statements.addAll(Arrays.asList(this.processFiscalDocument(lineItem)));
		} else if (lineItem instanceof CMSSaleLineItem) {
			CMSSaleLineItemDetail saleLnItmDtl = (CMSSaleLineItemDetail) lineItem.getLineItemDetailsArray()[0];
			bean.setLineItemType(POS_LINE_ITEM_TYPE_SALE);
			bean.setExceptionTaxJur(((CMSSaleLineItem) lineItem).getTaxJurisdiction());
			String giftCertId = lineItem.getLineItemDetailsArray()[0].getGiftCertificateId();
			if (giftCertId != null && giftCertId.length() > 0) {
				if((giftCertId.charAt(giftCertId.length()-1))=='R'){
					giftCertId = giftCertId.replaceAll("R", "");
				}
				bean.setRdmControlNumber(giftCertId);
				bean.setMiscItemId("GIFT_CARD");
			}

			// check if this Sale originates from Presale
			CMSPresaleLineItemDetail prsLnItmDtl = (CMSPresaleLineItemDetail) saleLnItmDtl.getPresaleLineItemDetail();
			if (prsLnItmDtl != null) {
				bean.setOrigTransactionId(prsLnItmDtl.getLineItem().getTransaction().getCompositeTransaction().getId());
				bean.setOrigAddNo(((PresaleTransaction) prsLnItmDtl.getLineItem().getTransaction()).getPresaleId());
				bean.setOrigLineItemType(POS_LINE_ITEM_TYPE_PRESALE);
			}
			// check if this Sale originates from Consignment
			CMSConsignmentLineItemDetail csgLnItmDtl = (CMSConsignmentLineItemDetail) saleLnItmDtl.getConsignmentLineItemDetail();
			if (csgLnItmDtl != null) {
				bean.setOrigTransactionId(csgLnItmDtl.getLineItem().getTransaction().getCompositeTransaction().getId());
				bean.setOrigAddNo(((ConsignmentTransaction) csgLnItmDtl.getLineItem().getTransaction()).getConsignmentId());
				bean.setOrigLineItemType(this.POS_LINE_ITEM_TYPE_CONSIGNMENT);
			}
			// check if this Sale originates from Reservation
			CMSReservationLineItemDetail rsvLnItmDtl = (CMSReservationLineItemDetail) saleLnItmDtl.getReservationLineItemDetail();
			if (rsvLnItmDtl != null) {
				bean.setOrigTransactionId(rsvLnItmDtl.getLineItem().getTransaction().getCompositeTransaction().getId());
				bean.setOrigAddNo(((ReservationTransaction) rsvLnItmDtl.getLineItem().getTransaction()).getReservationId());
				bean.setOrigLineItemType(this.POS_LINE_ITEM_TYPE_RESERVATION);
			}
			ShippingRequest shipReq = lineItem.getShippingRequest();
			if (shipReq != null) {
				bean.setShipState(shipReq.getState());
				bean.setShipZipCode(shipReq.getZipCode());
				bean.setCustomerRole("2");
				bean.setCustFirstName(shipReq.getFirstName());
				bean.setCustLastName(shipReq.getLastName());
				bean.setCustAddr1(shipReq.getAddress());
				bean.setCustCity(shipReq.getCity());
				bean.setCustState(shipReq.getState());
				bean.setCustPcode(shipReq.getZipCode());
				bean.setCustCountry(shipReq.getCountry());
				bean.setCustPhone1(shipReq.getPhone());
			}
			statements.addAll(Arrays.asList(processReductionDetails(bean, lineItem)));
			// statements.addAll(Arrays.asList(this.processFiscalDocument(lineItem)));

		}

		return (ParametricStatement[]) statements.toArray(new ParametricStatement[0]);
	}

	/**
	 * @param posTxn
	 * @return
	 */
	private ParametricStatement[] processReturnLineItems(POSLineItem lineItem) {
		List statements = new ArrayList();
		ArmStgTxnDtlOracleBean bean = initMRecordType(lineItem);
		if (lineItem instanceof CMSNoReturnLineItem) {
			bean.setLineItemType(POS_LINE_ITEM_TYPE_NORETURN);
			statements.add(new ParametricStatement(ArmStgTxnDtlOracleBean.insertSql, bean.toList()));
			// statements.addAll(Arrays.asList(this.processFiscalDocument(lineItem)));
		} else if (lineItem instanceof CMSReturnLineItem) {
			CMSReturnLineItem rtnLnItm = (CMSReturnLineItem) lineItem;
			CMSReturnLineItemDetail rtnLnItmDtl = (CMSReturnLineItemDetail) rtnLnItm.getLineItemDetailsArray()[0];
			if (rtnLnItm.getIsForExchange() != null && rtnLnItm.getIsForExchange().booleanValue())
				bean.setLineItemType(POS_LINE_ITEM_TYPE_EXCHANGE);
			else if (rtnLnItm instanceof CMSVoidLineItem)
				bean.setLineItemType(POS_LINE_ITEM_TYPE_VOID);
			else
				bean.setLineItemType(POS_LINE_ITEM_TYPE_RETURN);
			bean.setExceptionTaxJur(rtnLnItm.getTaxJurisdiction());
			bean.setReturnReasonId(rtnLnItm.getReasonId());
			bean.setReturnComments(rtnLnItm.getComments());
			
			//Added by Rachana for approval of return transaction
			bean.setApproverId(lineItem.getApprover());
			
			// check if this Return originates from Sale
			CMSSaleLineItemDetail saleLnItmDtl = (CMSSaleLineItemDetail) rtnLnItmDtl.getSaleLineItemDetail();

			if (saleLnItmDtl != null) {
				bean.setRtnSaleAiTrn(saleLnItmDtl.getLineItem().getTransaction().getCompositeTransaction().getId());
				bean.setRtnOrigStoreId(saleLnItmDtl.getLineItem().getTransaction().getCompositeTransaction().getStore().getId());
				bean.setRtnOrigOprId(((CMSEmployee) saleLnItmDtl.getLineItem().getTransaction().getCompositeTransaction().getTheOperator()).getExternalID());
				bean.setRtnOrigRegisterId(saleLnItmDtl.getLineItem().getTransaction().getCompositeTransaction().getRegisterId());
				bean.setRtnOrigProcessDt(saleLnItmDtl.getLineItem().getTransaction().getCompositeTransaction().getProcessDate());
			}
			// check if this Return originates from Presale
			CMSPresaleLineItemDetail prsLnItmDtl = (CMSPresaleLineItemDetail) rtnLnItmDtl.getPresaleLineItemDetail();
			if (prsLnItmDtl != null) {
				bean.setOrigTransactionId(prsLnItmDtl.getLineItem().getTransaction().getCompositeTransaction().getId());
				bean.setOrigAddNo(((PresaleTransaction) prsLnItmDtl.getLineItem().getTransaction()).getPresaleId());
				bean.setOrigLineItemType(this.POS_LINE_ITEM_TYPE_PRESALE);
			}
			// check if this Return originates from Consignment
			CMSConsignmentLineItemDetail csgLnItmDtl = (CMSConsignmentLineItemDetail) rtnLnItmDtl.getConsignmentLineItemDetail();
			if (csgLnItmDtl != null) {
				bean.setOrigTransactionId(csgLnItmDtl.getLineItem().getTransaction().getCompositeTransaction().getId());
				bean.setOrigAddNo(((ConsignmentTransaction) csgLnItmDtl.getLineItem().getTransaction()).getConsignmentId());
				bean.setOrigLineItemType(this.POS_LINE_ITEM_TYPE_CONSIGNMENT);
			}
			// check if this Return originates from Reservation
			CMSReservationLineItemDetail rsvLnItmDtl = (CMSReservationLineItemDetail) rtnLnItmDtl.getReservationLineItemDetail();
			if (rsvLnItmDtl != null) {
				bean.setOrigTransactionId(rsvLnItmDtl.getLineItem().getTransaction().getCompositeTransaction().getId());
				bean.setOrigAddNo(((ReservationTransaction) rsvLnItmDtl.getLineItem().getTransaction()).getReservationId());
				bean.setOrigLineItemType(this.POS_LINE_ITEM_TYPE_RESERVATION);
			}
			statements.addAll(Arrays.asList(processReductionDetails(bean, lineItem)));
		}
		return (ParametricStatement[]) statements.toArray(new ParametricStatement[0]);
	}

	/**
	 * @param posTxn
	 * @return
	 */
	private ParametricStatement[] processPresaleLineItems(POSLineItem lineItem) {
		List statements = new ArrayList();
		ArmStgTxnDtlOracleBean bean = initMRecordType(lineItem);
		CMSPresaleLineItem prsLnItm = (CMSPresaleLineItem) lineItem;
		bean.setLineItemType(POS_LINE_ITEM_TYPE_PRESALE);
		statements.add(new ParametricStatement(ArmStgTxnDtlOracleBean.insertSql, bean.toList()));
		return (ParametricStatement[]) statements.toArray(new ParametricStatement[0]);
	}

	/**
	 * @param posTxn
	 * @return
	 */
	private ParametricStatement[] processConsignmentLineItems(POSLineItem lineItem) {
		List statements = new ArrayList();
		ArmStgTxnDtlOracleBean bean = initMRecordType(lineItem);
		CMSConsignmentLineItem csgLnItm = (CMSConsignmentLineItem) lineItem;
		bean.setLineItemType(POS_LINE_ITEM_TYPE_CONSIGNMENT);
		ShippingRequest shipReq = ((CMSConsignmentLineItem) lineItem).getShippingRequest();
		if (shipReq != null) {
			bean.setShipState(shipReq.getState());
			bean.setShipZipCode(shipReq.getZipCode());
			bean.setCustomerRole("2");
			bean.setCustFirstName(shipReq.getFirstName());
			bean.setCustLastName(shipReq.getLastName());
			bean.setCustAddr1(shipReq.getAddress());
			bean.setCustCity(shipReq.getCity());
			bean.setCustState(shipReq.getState());
			bean.setCustPcode(shipReq.getZipCode());
			bean.setCustCountry(shipReq.getCountry());
			bean.setCustPhone1(shipReq.getPhone());
		}
		statements.add(new ParametricStatement(ArmStgTxnDtlOracleBean.insertSql, bean.toList()));
		return (ParametricStatement[]) statements.toArray(new ParametricStatement[0]);
	}

	/**
	 * @param posTxn
	 * @return
	 */
	private ParametricStatement[] processReservationLineItems(POSLineItem lineItem) {
		List statements = new ArrayList();
		ArmStgTxnDtlOracleBean bean = initMRecordType(lineItem);
		bean.setLineItemType(POS_LINE_ITEM_TYPE_RESERVATION);
		statements.add(new ParametricStatement(ArmStgTxnDtlOracleBean.insertSql, bean.toList()));
		return (ParametricStatement[]) statements.toArray(new ParametricStatement[0]);
	}

	/**
	 * @param lineItem
	 * @return
	 */
	private ArmStgTxnDtlOracleBean initMRecordType(POSLineItem lineItem) {
		ArmStgTxnDtlOracleBean bean = new ArmStgTxnDtlOracleBean();
		bean.setDataPopulationDt(new Date());
		bean.setRecordType(RECORD_TYPE_M);
		bean.setTransactionId(lineItem.getTransaction().getCompositeTransaction().getId());
		bean.setLineId(lineItem.getSequenceNumber());
		CMSCompositePOSTransaction theTxn = (CMSCompositePOSTransaction) 
				lineItem.getTransaction().getCompositeTransaction();
		if (theTxn instanceof CMSCompositePOSTransaction) {
			if(theTxn.getCmsV12Basket()!=null) {
				bean.setMobileRegisterId(theTxn.getCmsV12Basket().getMobileRegisterId());
			}
		}
		// bean.setDocNum(object.getDocNum()); //for Europe
		if (lineItem.isMiscItem()) {
			
			//Added by Satin to replace 'EXCEPTION_ITEM' with 'NOTONFILE' in MISC_ITEM_ID
			if ((lineItem.getMiscItemId()).equalsIgnoreCase("Exception_Item"))
			{
				bean.setMiscItemId("NOTONFILE");
			}
			else{
				bean.setMiscItemId(lineItem.getMiscItemId());
			}
			
			//End of change to replace 'EXCEPTION_ITEM' with 'NOTONFILE' in MISC_ITEM_ID
			//bean.setMiscItemId(lineItem.getMiscItemId());
			bean.setNotInFileSkuDtl(lineItem.getMiscItemComment());
			if (lineItem.doGetMiscItemTaxable() != null) {
				if (lineItem.doGetMiscItemTaxable().booleanValue())
					bean.setMiscItemTxbl("1");
				else
					bean.setMiscItemTxbl("0");
			}
		}
		String giftCertId = lineItem.getLineItemDetailsArray()[0].getGiftCertificateId();
		if (giftCertId != null && giftCertId.length() > 0) {
			bean.setRdmControlNumber(giftCertId);
			bean.setMiscItemId("GIFT_CARD");
		}
		bean.setIdItm(lineItem.getItem().getId());
		bean.setQuantity(lineItem.getQuantity().intValue());
		bean.setItmRtlPrice(currencyVal(lineItem.getItemRetailPrice()));
		bean.setItmSelPrice(currencyVal(lineItem.getItemSellingPrice()));
		bean.setManualUnitPrice(currencyVal(lineItem.getManualUnitPrice()));
		bean.setConsultantId(((CMSEmployee) lineItem.getTransaction().getCompositeTransaction().getConsultant()).getExternalID());
		if (lineItem.getAdditionalConsultant() != null)
			bean.setAddConsultantId(((CMSEmployee) lineItem.getAdditionalConsultant()).getExternalID());
//		vivek added this code for japan needs the consultant must be there in Reserve reservation transaction
		if (lineItem instanceof CMSReservationLineItem){
			if(lineItem.getAdditionalConsultant() == null)
			bean.setAddConsultantId(((CMSEmployee) lineItem.getTransaction().getCompositeTransaction().getConsultant()).getExternalID());
		}		
		//End of Vivek 
		bean.setVatRate(lineItem.getItem().getVatRate());
		bean.setTaxExemptId(lineItem.getTransaction().getCompositeTransaction().getTaxExemptId());
		bean.setNetAmt(currencyVal(lineItem.getExtendedNetAmount()));
		ConfigMgr config = new ConfigMgr("vat.cfg");
		String isVatEnabled = config.getString("VAT_ENABLED");
		if (isVatEnabled.equalsIgnoreCase("true")) {
			bean.setTaxAmt(currencyVal(lineItem.getExtendedVatAmount()));
		} else {
			bean.setTaxAmt(currencyVal(lineItem.getExtendedTaxAmount()));
		}
		//Vivek Mishra : Added for Extended Barcode CR Europe 06-OCT-2016
		if(lineItem.getExtendedStagingBarCode() != null){
			bean.setExtendedBarcode(lineItem.getExtendedStagingBarCode());
		}
		//Ends here 06-OCT-2016
		else if(lineItem.getExtendedBarCode() != null){
			bean.setExtendedBarcode(lineItem.getExtendedBarCode());
		}//needs the setExtendedBarcode must be there in Reserve reservation transaction -japan 2010 koji.
		else{
			bean.setExtendedBarcode(lineItem.getItem().getId());
		}
//		bean.setEncryptedString(null);
//		bean.setKeyId(null);
		return bean;
	}

	private ParametricStatement[] processReductionDetails(ArmStgTxnDtlOracleBean bean, POSLineItem lineItem) {
		// Reductions/discounts
		List statements = new ArrayList();
		LineItemPOSUtil util = new LineItemPOSUtil(lineItem);
		Map reductionAmtMap = util.getReductionAmountByReason();
		int seqNum = 0;
		//Vishal Yevale : markdown CR 15 JAN 2017
		Double currAmount = null;
		boolean calculateOnce = true;
		boolean isDiscountAvailable = false;
		boolean firstReduIsLess = false;
		boolean NetAmountIsMore = false;
		double priceOUT2 = 0.0d;
		double mrkAmount = 0.0;
		double totalReduction = 0.0;
		int count = 0;
		if(calcMkdwDisc){
			try{
				if(bean.getItmSelPrice()!=null){
					currAmount = bean.getItmSelPrice();
				}
				isOnFile = true;
				//Vishal Yevale : changed CMSSaleLineItem to POSLineItem  : to solve precision issue : 8 June 2017
				POSLineItem posLineItem = lineItem;
				if((posLineItem.getMiscItemId()!=null && posLineItem.getMiscItemId().toUpperCase().contains("NOT")) || (posLineItem.doGetMiscItemDescription()!=null && posLineItem.doGetMiscItemDescription().toUpperCase().contains("NOT"))){
					isOnFile = false;
				}
				CMSItem item = (CMSItem) lineItem.getItem();					
				String barcode = item.getBarCode();
				priceDMS = null;
				priceOUT = null;
				selectByCountryAndBarcode(country, barcode, currAmount);
				if(storeType.equalsIgnoreCase("2") && priceOUT!=null){
					 priceOUT2 = priceOUT;
					 if(bean.getNetAmt()!=null && bean.getNetAmt()>=priceOUT){
						 NetAmountIsMore = true;
					 }
					}
			}catch(Exception e){
				System.out.println(e);
			}
		}
		//END VISHAL YEVALE 15 JAN 2017
		for (Iterator itr = reductionAmtMap.keySet().iterator(); itr.hasNext();) {
			String reason = (String) itr.next();
			seqNum = seqNum + 1;
			bean.setLineSeqNum(seqNum);
			bean.setReductionReason(reason);
			bean.setReductionAmount(currencyVal(((ArmCurrency) reductionAmtMap.get(reason))));
			
			//added by shushma for promotion code
			if(lineItem.getPromotionCode()!=null){
				bean.setPromotionCode(lineItem.getPromotionCode());
			}
			else{
				bean.setPromotionCode(null);
			}
			//promotion code close
			if (reason.equals("DEAL MARKDOWN")) {
				bean.setDealMkdnAmt(currencyVal(((ArmCurrency) reductionAmtMap.get(reason))));
				bean.setDealId(lineItem.getLineItemDetailsArray()[0].getDealId());
				//statements.add(new ParametricStatement(ArmStgTxnDtlOracleBean.insertSql, bean.toList()));
			} else if (reason.equals("MANUAL MARKDOWN")) {
				bean.setManualMdAmt(currencyVal(((ArmCurrency) reductionAmtMap.get(reason))));
				//statements.add(new ParametricStatement(ArmStgTxnDtlOracleBean.insertSql, bean.toList()));
			} else if (reason.equals("ITEM MARKDOWN")) {
				bean.setManualMdAmt(currencyVal(((ArmCurrency) reductionAmtMap.get(reason))));
				//statements.add(new ParametricStatement(ArmStgTxnDtlOracleBean.insertSql, bean.toList()));
			} else if (reason.toLowerCase().indexOf(" discount") > 0) {
				//Vivek Mishra : Added to make promotion code null for other discounts 05-AUG-2016
				bean.setPromotionCode(null);
				//Ends here
				String dscType = reason.substring(0, reason.toLowerCase().indexOf(" discount"));
				Discount dsc = util.getDiscountByType(dscType);
				// Fix for 1481: Return of employee sales - Employee_ID field is NULL in staging table
				if (dsc == null) {
					Discount[] arrayDiscounts = null;
					if (lineItem instanceof ReturnLineItem) {
						SaleLineItem saleLineItem = ((ReturnLineItem) lineItem).getSaleLineItem();
						if (saleLineItem != null) {
							arrayDiscounts = saleLineItem.getDiscountsArray();
							if (arrayDiscounts != null && arrayDiscounts.length > 0) {
								for (int j = 0; j < arrayDiscounts.length; j++) {
								
								//Anjana removed the employee code check as the code needs to be saved for all discounts 
								//Anjana: 05/17 added to set discount code for returns and exchanges
										if (j == count) {
											dsc = arrayDiscounts[count];
											count++;
										break;
									}
									//}
								}
							}
						}
					}
				}
				if (dsc != null) {
					bean.setDscAmount(currencyVal(dsc.getAmount()));
					if (((CMSDiscount) dsc).getDiscountCodeNote() != null && ((CMSDiscount) dsc).getDiscountCodeNote().length() > 0) {
						bean.setDscReason(((CMSDiscount) dsc).getDiscountCodeNote());
					} else {
						bean.setDscReason(((CMSDiscount) dsc).getDiscountCode());
						//Anjana:05/17 setting default code 02 for older transactions where discount code is missing from Sale and  to populate it while return/exchange
						if(bean.getDscReason()==null && (reason.toLowerCase().indexOf(" discount") > 0)){
						((CMSDiscount) dsc).setDiscountCode("02");
						bean.setDscReason(((CMSDiscount) dsc).getDiscountCode());
						}
					}
					bean.setDscPercent(dsc.getPercent());
					bean.setDscAdvertisingCode(dsc.getAdvertisingCode());
					if (dsc.getEmployee() != null)
						bean.setDscEmployeeId(((CMSEmployee) dsc.getEmployee()).getExternalID());
					if (dsc.isDiscountPercent())
						bean.setDscIsDscPercent("1");
					else
						bean.setDscIsDscPercent("0");
					//statements.add(new ParametricStatement(ArmStgTxnDtlOracleBean.insertSql, bean.toList()));
				} else {
					bean.setDscAmount(0.0d);
					bean.setDscReason("");
					bean.setDscPercent(0.0d);
					bean.setDscAdvertisingCode("");
					bean.setDscEmployeeId("");
					bean.setDscIsDscPercent("");
					//statements.add(new ParametricStatement(ArmStgTxnDtlOracleBean.insertSql, bean.toList()));
				}
			} else {
				bean.setDscAmount(0.0d);
				bean.setDscReason("");
				bean.setDscPercent(0.0d);
				bean.setDscAdvertisingCode("");
				bean.setDscEmployeeId("");
				bean.setDscIsDscPercent("");
				//statements.add(new ParametricStatement(ArmStgTxnDtlOracleBean.insertSql, bean.toList()));
			}
			// VISHAL YEVALE 16 JAN 2017 : MARKDOWN CR FOR OUTLET STORE
			if(calcMkdwDisc){
				isDiscountAvailable = true;
				try{
				if(storeType.equalsIgnoreCase("1")){
					if(priceDMS!=null && bean.getItmSelPrice()!=null && bean.getReductionAmount()!=null){
						if(calculateOnce){
						double mrk = bean.getItmSelPrice()-priceDMS;
						bean.setMrk(mrk);
						double mrk_Per = round(((mrk*100)/priceDMS),2);
						bean.setMrk_per(mrk_Per/100);
						calculateOnce = false;
						}else{
							bean.setMrk(null);
							bean.setMrk_per(null);
						}
						bean.setRed_amt2(bean.getReductionAmount());
						double currAmount2 = currAmount;
						currAmount = currAmount-bean.getReductionAmount();
						double dscPercent = 0.0d;
						if(bean.getDscPercent()==null || bean.getDscPercent()-0.0==0.0){
							dscPercent = round(((bean.getReductionAmount()*100)/currAmount2),2);
							bean.setDsc_per2(dscPercent/100);
						}else{
							bean.setDsc_per2(bean.getDscPercent());
						}
						bean.setPrz_Retail(priceDMS);
					}
					statements.add(new ParametricStatement(ArmStgTxnDtlOracleBean.insertSql, bean.toList()));
				}else if(storeType.equalsIgnoreCase("2")){
					if(priceOUT!=null && bean.getItmSelPrice()!=null && bean.getReductionAmount()!=null){
						double dscPercent;
						double mrk = 0.0;
						double dscPercent2 = 0.0d;
						double currAmount2 = currAmount;
						currAmount = currAmount-bean.getReductionAmount();
					if(calculateOnce){
						 mrk = round(bean.getItmSelPrice()-priceOUT,2);
						double mrkPer =  round((mrk*100)/bean.getItmSelPrice(),2);
						bean.setMrk(mrk);
						bean.setMrk_per(mrkPer/100);
						double firstReductionAmount = bean.getReductionAmount();
						mrkAmount = mrk;
						if(firstReductionAmount <= mrk){
							totalReduction = bean.getReductionAmount();
							firstReduIsLess = true;
							calculateOnce = false;
							if(NetAmountIsMore){
							double red_amt = firstReductionAmount - mrk;
							if(priceOUT - 0.0 != 0.0){
								double dsc_per = red_amt/priceOUT;
								bean.setDsc_per2(round(dsc_per,2));
							}else{
								bean.setDsc_per2(0.0);
							}
							bean.setRed_amt2(red_amt);
							}else{
							bean.setDsc_per2(0.0);
								bean.setRed_amt2(0.0);
							}
						}else{
						double redAmt = firstReductionAmount - mrkAmount;
						bean.setRed_amt2(redAmt);
						if(!(redAmt-0.0==0.0)){
						dscPercent2 = round((redAmt * 100)/priceOUT2,2);
						}
						bean.setDsc_per2(dscPercent2/100);
						calculateOnce = false;
						priceOUT2 = priceOUT2 - redAmt;
						}
						}else{
								bean.setMrk(null);
								bean.setMrk_per(null);		
								if(firstReduIsLess){
									totalReduction = totalReduction + bean.getReductionAmount();
									if(NetAmountIsMore){
										//mrkAmount = mrkAmount -bean.getReductionAmount();
										double mrkAm = totalReduction - mrkAmount;
										double reduction = bean.getReductionAmount();
										 	mrkAm = reduction/(bean.getItmSelPrice() - mrkAm);
										bean.setRed_amt2(reduction);
										bean.setDsc_per2(round(mrkAm,2));
									}else{
										double redAmt = totalReduction - mrkAmount;
										bean.setRed_amt2(redAmt);
										if(!(redAmt-0.0==0.0)){
										dscPercent2 = round((redAmt * 100)/priceOUT2,2);
										}
										bean.setDsc_per2(dscPercent2/100);
										priceOUT2 = priceOUT2 - redAmt ;
										mrkAmount = mrkAmount + redAmt;
									}
								}else{
								double redAmt = bean.getReductionAmount();
					bean.setRed_amt2(redAmt);
								double dsc_per2 = round((redAmt * 100)/priceOUT2,2);
								bean.setDsc_per2(dsc_per2/100);
					priceOUT2 = priceOUT2 - redAmt ;
								}
						}
					bean.setPrz_Retail(priceOUT);

					}
					statements.add(new ParametricStatement(ArmStgTxnDtlOracleBean.insertSql, bean.toList()));
				}else{
					statements.add(new ParametricStatement(ArmStgTxnDtlOracleBean.insertSql, bean.toList()));
				}
				}catch(Exception e){
					e.printStackTrace();
					System.out.println(e);
				}
			}else{
			statements.add(new ParametricStatement(ArmStgTxnDtlOracleBean.insertSql, bean.toList()));
			}
		} // end reduction reason iteration 

		if(calcMkdwDisc && (!isDiscountAvailable)){
			if(storeType.equalsIgnoreCase("1")){
				if(priceDMS!=null && priceDMS -0.0!=0.0 && bean.getItmSelPrice()!=null){
					double mrk=bean.getItmSelPrice()-priceDMS;
					bean.setMrk(mrk);
					double mrk_per = round(((mrk*100)/priceDMS),2);
					bean.setMrk_per(mrk_per/100);
					bean.setPrz_Retail(priceDMS);
				}
			}
			
			if(storeType.equalsIgnoreCase("2")){
				if(priceOUT!=null && priceOUT -0.0!=0.0 && bean.getItmSelPrice()!=null){
					double mrk=bean.getItmSelPrice()-priceOUT;
					bean.setMrk(mrk);
					double mrk_per = round(((mrk*100)/bean.getItmSelPrice()),2);
					bean.setMrk_per(mrk_per/100);
					bean.setRed_amt2(mrk*-1);
					double dsc_per = mrk/priceOUT*-1;
					bean.setDsc_per2(round(dsc_per,2));
					bean.setPrz_Retail(priceOUT);
				}
			}
		}
		if (reductionAmtMap.isEmpty())
			statements.add(new ParametricStatement(ArmStgTxnDtlOracleBean.insertSql, bean.toList()));
		// end vishal Yeavale 17 JAN 2017
		return (ParametricStatement[]) statements.toArray(new ParametricStatement[0]);
	}

	private ParametricStatement[] processCustomerDetails(ArmStgTxnDtlOracleBean bean, CompositePOSTransaction posTxn) {		
		// Customer record type 'C'
		List statements = new ArrayList();
		CMSCustomer cust = (CMSCustomer) posTxn.getCustomer();
		if (cust != null) {
			bean = new ArmStgTxnDtlOracleBean();
			bean.setDataPopulationDt(new Date());
			bean.setRecordType(RECORD_TYPE_C);
			bean.setTransactionId(posTxn.getId());
			bean.setCustomerRole("1");
			bean.setCustId(cust.getId());
			bean.setCustFirstName(cust.getFirstName());
			bean.setCustLastName(cust.getLastName());
			
			//Added by Rachana for approval of return transaction
			if(posTxn instanceof CompositePOSTransaction){
				CMSCompositePOSTransaction cmpObj = (CMSCompositePOSTransaction) posTxn;
				if(cmpObj.getTotalPaymentAmount().stringValue().startsWith("-")){
					bean.setApproverId(cmpObj.getApprover());
				}
			}
			
			Address addr = cust.getPrimaryAddress();
			if (addr != null) {
				bean.setCustAddr1(addr.getAddressLine1());
				bean.setCustAddr2(addr.getAddressLine2());
				bean.setCustCity(addr.getCity());
				bean.setCustState(addr.getState());
				bean.setCustPcode(addr.getZipCode());
				bean.setCustCountry(addr.getCountry());
				if (addr.getPrimaryPhone() != null)
					bean.setCustPhone1(addr.getPrimaryPhone().getTelephoneNumber());
				if (addr.getSecondaryPhone() != null)
					bean.setCustPhone2(addr.getSecondaryPhone().getTelephoneNumber());
			}
			// vishal : for customer_id prefix in europe for FRANCHISING store
				CMSCompositePOSTransaction theTxn=(CMSCompositePOSTransaction) posTxn;
				//vishal : fixed null pointer exception issue 30 sept 2016
				if(theTxn.getFranchising_Store()!=null && theTxn.getFranchising_Store() && (theTxn.getStaging_cust_id()!=null && (!theTxn.getStaging_cust_id()))){
				 ConfigMgr configMgr = new ConfigMgr("customer.cfg");
				 String custId=configMgr.getString("DUMMY_CUST_ID");
				 String custFirstName=configMgr.getString("DUMMY_CUST_FIRST_NAME");
			     String custLastName=configMgr.getString("DUMMY_CUST_LAST_NAME");
			     if(custId!=null){
		               bean.setCustId(custId);
			     }else{
			    	 bean.setCustId("P59999999");
			     }
			     if(custFirstName!=null){
			    	 bean.setCustFirstName(custFirstName);
			     }else{
			    	 bean.setCustFirstName("CUSTOMER TO BE ASSOCIATED");
			     }
			     if(custLastName!=null){
			    	 bean.setCustLastName(custLastName);
			     }else{
			    	bean.setCustLastName("CUSTOMER TO BE ASSOCIATED"); 
			     }
               bean.setCustFirstName(configMgr.getString("DUMMY_CUST_FIRST_NAME"));
               bean.setCustLastName(configMgr.getString("DUMMY_CUST_LAST_NAME"));
               bean.setCustAddr1(null);
				bean.setCustAddr2(null);
				bean.setCustCity(null);
				bean.setCustState(null);
				bean.setCustPcode(null);
				bean.setCustCountry(null);
				bean.setCustPhone1(null);
				bean.setCustPhone2(null);
			}
			// END VISHAL 15 SEPT 2016
			statements.add(new ParametricStatement(ArmStgTxnDtlOracleBean.insertSql, bean.toList()));
		}
		return (ParametricStatement[]) statements.toArray(new ParametricStatement[0]);
	}

	public ParametricStatement[] getFiscalDocumentInsertSQL(FiscalDocument document) {
		List statements = new ArrayList();
		ArmStgTxnDtlOracleBean bean = new ArmStgTxnDtlOracleBean();
		String vatComments = null;
		POSLineItem[] lineItems = document.getLineItemsArray();
		for (int index = 0; index < lineItems.length; index++) {
			POSLineItem lineItem = lineItems[index];
			if (lineItem instanceof CMSNoSaleLineItem) {
				bean.setVatComments(((CMSNoSaleLineItem) lineItem).getFiscalDocComment(document.getDocumentType()));
			} else if (lineItem instanceof CMSNoReturnLineItem) {
				bean.setVatComments(((CMSNoReturnLineItem) lineItem).getFiscalDocComment(document.getDocumentType()));
			} else if (lineItem instanceof CMSReturnLineItem) {
				bean.setVatComments(((CMSReturnLineItem) lineItem).getFiscalDocComment(document.getDocumentType()));
			} else if (lineItem instanceof CMSPresaleLineItem) {
				bean.setVatComments(((CMSPresaleLineItem) lineItem).getFiscalDocComment(document.getDocumentType()));
			} else if (lineItem instanceof CMSConsignmentLineItem) {
				bean.setVatComments(((CMSConsignmentLineItem) lineItem).getFiscalDocComment(document.getDocumentType()));
			} else if (lineItem instanceof CMSReservationLineItem) {
				bean.setVatComments(((CMSReservationLineItem) lineItem).getFiscalDocComment(document.getDocumentType()));
			} else if (lineItem instanceof CMSReturnLineItem) {
				bean.setVatComments(((CMSReturnLineItem) lineItem).getFiscalDocComment(document.getDocumentType()));
			}
			bean.setDataPopulationDt(new Date());
			bean.setRecordType(RECORD_TYPE_FD);
			bean.setTransactionId(lineItem.getTransaction().getCompositeTransaction().getId());
			bean.setLineId(lineItem.getSequenceNumber());
			bean.setDocNum(document.getDocumentNumber());
			bean.setDocType(document.getDocumentType());
			statements.add(new ParametricStatement(ArmStgTxnDtlOracleBean.insertSql, bean.toList()));
		}
		return (ParametricStatement[]) statements.toArray(new ParametricStatement[0]);
	}

	public ParametricStatement[] getUpdateConsultantInsertSQL(POSLineItem[] lineItems) {
		List statements = new ArrayList();
		ArmStgTxnDtlOracleBean bean = new ArmStgTxnDtlOracleBean();
		String vatComments = null;
		for (int index = 0; index < lineItems.length; index++) {
			POSLineItem lineItem = lineItems[index];
			bean.setDataPopulationDt(new Date());
			bean.setRecordType(RECORD_TYPE_UA);
			bean.setTransactionId(lineItem.getTransaction().getCompositeTransaction().getId());
			bean.setLineId(lineItem.getSequenceNumber());
			bean.setLineSeqNum(0);
			bean.setIdItm(lineItem.getItem().getId());
			bean.setConsultantId(((CMSEmployee) lineItem.getTransaction().getCompositeTransaction().getConsultant()).getExternalID());
			if (lineItem.getAdditionalConsultant() != null)
				bean.setAddConsultantId(((CMSEmployee) lineItem.getAdditionalConsultant()).getExternalID());
			statements.add(new ParametricStatement(ArmStgTxnDtlOracleBean.insertSql, bean.toList()));
		}
		return (ParametricStatement[]) statements.toArray(new ParametricStatement[0]);
	}

	public ParametricStatement[] getUpdateCustomerInsertSQL(CMSCompositePOSTransaction posTransaction) {
		List statements = new ArrayList();
		ArmStgTxnDtlOracleBean bean = new ArmStgTxnDtlOracleBean();
		bean.setDataPopulationDt(new Date());
		bean.setRecordType(RECORD_TYPE_UC);
		bean.setTransactionId(posTransaction.getId());
		bean.setCustomerRole("1");
		Customer cust = posTransaction.getCustomer();
		if (cust != null) {

			bean.setCustId(cust.getId());
			bean.setCustFirstName(cust.getFirstName());
			bean.setCustLastName(cust.getLastName());
			Address addr = ((CMSCustomer) cust).getPrimaryAddress();
			if (addr != null) {
				bean.setCustAddr1(addr.getAddressLine1());
				bean.setCustAddr2(addr.getAddressLine2());
				bean.setCustCity(addr.getCity());
				bean.setCustState(addr.getState());
				bean.setCustPcode(addr.getZipCode());
				bean.setCustCountry(addr.getCountry());
				if (addr.getPrimaryPhone() != null)
					bean.setCustPhone1(addr.getPrimaryPhone().getTelephoneNumber());
				if (addr.getSecondaryPhone() != null)
					bean.setCustPhone2(addr.getSecondaryPhone().getTelephoneNumber());
			}
		}
		statements.add(new ParametricStatement(ArmStgTxnDtlOracleBean.insertSql, bean.toList()));
		return (ParametricStatement[]) statements.toArray(new ParametricStatement[0]);
	}

	public ParametricStatement[] getUpdateShipRequestInsertSQL(CMSCompositePOSTransaction posTransaction) {
		// return getUpdateTxnInsertSQL(lineItems,RECORD_TYPE_UC);
		List statements = new ArrayList();
		POSLineItem[] posLineItems = posTransaction.getLineItemsArray();
		for (int j = 0; posLineItems != null && j < posLineItems.length; j++) {
			ArmStgTxnDtlOracleBean bean = new ArmStgTxnDtlOracleBean();
			bean.setRecordType(RECORD_TYPE_US);
			bean.setTransactionId(posTransaction.getId());
			bean.setLineId(j);
			bean.setDataPopulationDt(new Date());
			bean.setIdItm(posLineItems[j].getItem().getId());
			bean.setCustomerRole("1");
			bean.setItmRtlPrice(currencyVal(posLineItems[j].getItemRetailPrice()));
			bean.setItmSelPrice(currencyVal(posLineItems[j].getItemSellingPrice()));
			bean.setManualUnitPrice(currencyVal(posLineItems[j].getManualUnitPrice()));
			bean.setConsultantId(((CMSEmployee) posLineItems[j].getTransaction().getCompositeTransaction().getConsultant()).getExternalID());
			if (posLineItems[j].getAdditionalConsultant() != null)
				bean.setAddConsultantId(((CMSEmployee) posLineItems[j].getAdditionalConsultant()).getExternalID());
			bean.setVatRate(posLineItems[j].getItem().getVatRate());
			bean.setTaxExemptId(posLineItems[j].getTransaction().getCompositeTransaction().getTaxExemptId());
			bean.setNetAmt(currencyVal(posLineItems[j].getExtendedNetAmount()));
			ConfigMgr config = new ConfigMgr("vat.cfg");
			String isVatEnabled = config.getString("VAT_ENABLED");
			if (isVatEnabled.equalsIgnoreCase("true")) {
				bean.setTaxAmt(currencyVal(posLineItems[j].getExtendedVatAmount()));
			} else {
				bean.setTaxAmt(currencyVal(posLineItems[j].getExtendedTaxAmount()));
			}
			ShippingRequest shippingRequest = null;
			if (posLineItems[j] instanceof CMSConsignmentLineItem) {
				shippingRequest = ((CMSConsignmentLineItem) posLineItems[j]).getShippingRequest();
				bean.setLineItemType(this.POS_LINE_ITEM_TYPE_CONSIGNMENT);
			} else if (posLineItems[j] instanceof CMSPresaleLineItem) {
				shippingRequest = ((CMSPresaleLineItem) posLineItems[j]).getShippingRequest();
				bean.setLineItemType(this.POS_LINE_ITEM_TYPE_PRESALE);
			} else if (posLineItems[j] instanceof SaleLineItem) {
				shippingRequest = ((SaleLineItem) posLineItems[j]).getShippingRequest();
				bean.setLineItemType(this.POS_LINE_ITEM_TYPE_SALE);
			}
			if (shippingRequest != null) {
				bean.setShipState(shippingRequest.getState());
				bean.setShipZipCode(shippingRequest.getZipCode());
				Customer cust = posTransaction.getCustomer();
				if (cust != null) {
					
					bean.setCustId(cust.getId());
					bean.setCustFirstName(cust.getFirstName());
					bean.setCustLastName(cust.getLastName());
					Address addr = ((CMSCustomer) cust).getPrimaryAddress();
					if (addr != null) {
						bean.setCustAddr1(addr.getAddressLine1());
						bean.setCustAddr2(addr.getAddressLine2());
						bean.setCustCity(addr.getCity());
						bean.setCustState(addr.getState());
						bean.setCustPcode(addr.getZipCode());
						bean.setCustCountry(addr.getCountry());
						if (addr.getPrimaryPhone() != null)
							bean.setCustPhone1(addr.getPrimaryPhone().getTelephoneNumber());
						if (addr.getSecondaryPhone() != null)
							bean.setCustPhone2(addr.getSecondaryPhone().getTelephoneNumber());
					}
				}
			}
			statements.add(new ParametricStatement(ArmStgTxnDtlOracleBean.insertSql, bean.toList()));
		}
		return (ParametricStatement[]) statements.toArray(new ParametricStatement[0]);
	}
	
	
	//added by shushma
	  public String[] selectByTxnId(String id)throws SQLException{
		String sql="select "+ArmStgTxnDtlOracleBean.COL_PROMOTION_CODE+" from "+ArmStgTxnDtlOracleBean.TABLE_NAME+" where "+ArmStgTxnDtlOracleBean.COL_TRANSACTION_ID+"=? and "+ArmStgTxnDtlOracleBean.COL_EXTENDED_BARCODE+" is not null";
		List params=new ArrayList();
		params.add(0,id);
		String[] ids = queryForIds(sql, params);
	    if (ids == null || ids.length == 0) {
	      return null;
	    } else {
	      return ids;
	    }
		  
	  }
	  
	  public String selectByTxnIdandBarcode(String id , String barcode )throws SQLException{  
			String sql="select "+ArmStgTxnDtlOracleBean.COL_PROMOTION_CODE+" from "+ArmStgTxnDtlOracleBean.TABLE_NAME+" where "+ArmStgTxnDtlOracleBean.COL_TRANSACTION_ID+"=? and "+ArmStgTxnDtlOracleBean.COL_EXTENDED_BARCODE+" is not null and "+ArmStgTxnDtlOracleBean.COL_EXTENDED_BARCODE+"=?";
			List params=new ArrayList();
			params.add(0,id);
			params.add(1,barcode);
			String[] ids = queryForIds(sql, params);
			if (ids == null || ids.length == 0) {
		      return null;
		    } else {
		      return ids[0];
		    }
	  }
	  /**
	   * vishal Yevale 13 Jan 2017
	   * @param country
	   * @param barcode
	   * @throws SQLException
	   */
	  public void selectByCountryAndBarcode (String country, String barcode, Double currAmount) throws SQLException {
		  if(isOnFile){
		  List params = null;
		    BaseOracleBean[] beans = null;
		    // party
		    params = new ArrayList();
		    if(barcode !=null){
		    params.add(country);
		    params.add(barcode);
		    beans = this.query(new ArmStgMrkprzOracleBean(), where(new String[] {
		    		ArmStgMrkprzOracleBean.COL_COUNTRY, ArmStgMrkprzOracleBean.COL_BARCODE
		    }), params);
		    if(beans!=null && beans.length>=1){
		    	ArmStgMrkprzOracleBean bean = (ArmStgMrkprzOracleBean) beans[0];
		    	priceDMS = bean.getPriceDMS();
		    	priceOUT = bean.getPriceOUT();
		    	if(storeType.equalsIgnoreCase("1") && priceDMS==null){
		    		isOnFile = false;
		    	}
		    	if(storeType.equalsIgnoreCase("2") && priceOUT==null){
		    		isOnFile = false;
		    	}
		    	
		    }else{
		    	isOnFile = false;
		    }
		    }else{
		    	isOnFile = false;
		    }
	  }
		   if((!isOnFile) && priceDMS==null && priceOUT==null){
			   List params = null;
			    BaseOracleBean[] beans = null;
			    // party
			    params = new ArrayList();
			    ConfigMgr config = new ConfigMgr("item.cfg");
				 barcode = config.getString("NOF_ITEM_BARCODE");
				 params.add(country);
				 params.add(barcode);
				 beans = this.query(new ArmStgMrkprzOracleBean(), where(new String[] {
				    		ArmStgMrkprzOracleBean.COL_COUNTRY, ArmStgMrkprzOracleBean.COL_BARCODE
				    }), params);
				    if(beans!=null && beans.length>=1){
				    	ArmStgMrkprzOracleBean bean = (ArmStgMrkprzOracleBean) beans[0];
				    	coeff = bean.getCoeff();
				    }
				    if(coeff!=null && currAmount!=null){
				    	if(storeType.equalsIgnoreCase("1")){
				    		if(coeff==0){
				    			priceDMS = currAmount;
				    		}else{
				    			priceDMS = (currAmount * 100)/(100-coeff);
				    		}
				    	}
				    	if(storeType.equalsIgnoreCase("2")){
				    		if(coeff==0){
				    			priceOUT = currAmount;
				    		}
				    		else{
				    			priceOUT = currAmount - (currAmount * coeff/100);
				    		}
				    	}
				    }
		   }
	  }
	  public static double round(double value, int places) {
		    if (places < 0) throw new IllegalArgumentException();
		    long factor = (long) Math.pow(10, places);
		    value = value * factor;
		    long tmp = Math.round(value);
		    return (double) tmp / factor;
		}
}
