/*
 * put your module comment here
 * formatted with JxBeauty (c) johann.langhofer@nextra.at
 */
//
// Copyright 2002, Retek Inc. All Rights Reserved.
//

package com.chelseasystems.cs.dataaccess.artsoracle.dao;

import com.chelseasystems.cs.dataaccess.*;
import com.chelseasystems.cs.dataaccess.artsoracle.databean.*;
import com.chelseasystems.cs.discount.CMSEmployeeDiscount;
import com.chelseasystems.cr.transaction.CommonTransaction;
import com.chelseasystems.cs.eod.CMSTransactionEOD;
import com.chelseasystems.cs.sos.CMSTransactionSOS;
import com.chelseasystems.cr.pos.POSLineItem;
import com.chelseasystems.cr.pos.PaymentTransaction;
import com.chelseasystems.cr.pos.ReturnLineItem;
import com.chelseasystems.cr.pos.SaleLineItem;
import com.chelseasystems.cr.pos.VoidTransaction;
import com.chelseasystems.cs.pos.CMSCompositePOSTransaction;
import com.chelseasystems.cs.pos.CMSRedeemableBuyBackTransaction;
import com.chelseasystems.cs.pos.PresaleTransaction;
import com.chelseasystems.cs.pos.ConsignmentTransaction;
import com.chelseasystems.cs.pos.ReservationTransaction;
import com.chelseasystems.cr.database.ParametricStatement;
import com.chelseasystems.cr.discount.Discount;
import com.chelseasystems.cs.util.TransactionUtil;
import com.chelseasystems.cs.employee.CMSEmployee;
import com.chelseasystems.cr.currency.ArmCurrency;

import java.sql.SQLException;

import com.chelseasystems.cr.config.*;

import java.util.*;

import com.chelseasystems.cs.collection.CMSMiscCollection;
import com.chelseasystems.cs.collection.CMSCollectionTransaction;
import com.chelseasystems.cs.customer.CMSCustomer;
import com.chelseasystems.cs.paidout.CMSPaidOutTransaction;
import com.chelseasystems.cs.pos.ASISTxnData;
import com.chelseasystems.cs.paidout.CMSMiscPaidOut;

/**
 * put your documentation comment here
 */
public class ArmStgTxnHdrOracleDAO extends BaseOracleDAO implements ArmStgTxnHdrDAO {
	static String txnDelim = new ConfigMgr("txnnumber.cfg").getString("DELIM");

	/**
	 * put your documentation comment here
	 * @param object
	 * @return
	 * @exception SQLException
	 */
	public ParametricStatement[] getStgTxnHeaderInsertSQL(CommonTransaction object) throws SQLException {
		List statements = new ArrayList();
		ArmStgTxnDtlOracleDAO armStgTxnDtlOracleDAO = new ArmStgTxnDtlOracleDAO();
		ArmStgFiscalDocOracleDAO armStgFiscalDocOracleDAO = new ArmStgFiscalDocOracleDAO();
		ArmStgTxnHdrOracleBean bean = new ArmStgTxnHdrOracleBean();
		bean.setDataPopulationDt(new Date());
		String txnId = object.getId();
		String storeId = object.getStore().getId();
		String regId = object.getRegisterId();
		// vishal : added for europe for FRANCHISING store requirement
		CMSCompositePOSTransaction cmpObj =null;
		//end vishal  2016 sept 15
		if (object instanceof CMSTransactionEOD)
			regId = ((CMSTransactionEOD) object).getRegister().getId();
		if (object instanceof CMSTransactionSOS)
			regId = ((CMSTransactionSOS) object).getRegister().getId();
		String txnNum = (txnId.substring(storeId.length())).substring(regId.length());
		if (txnDelim != null && txnNum.indexOf(txnDelim) != -1) {
			txnNum = txnNum.substring(2 * txnDelim.length());
		}
		bean.setTransactionId(object.getId());
		bean.setTransactionType(TransactionUtil.getTransactionType(object));
		bean.setTransactionNum(new Long(txnNum));
		bean.setTxnCategory("2");
		bean.setEntryDate(object.getSubmitDate());
		bean.setTrBeginDate(object.getCreateDate());
		bean.setTrEndDate(object.getPostDate());
		bean.setSubmitDate(object.getSubmitDate());
		bean.setCreateDate(object.getCreateDate());
		bean.setPostDate(object.getPostDate());
		bean.setProcessDate(object.getProcessDate());
		bean.setVoidFlag("0");
		if (object.isTrainingFlagOn())
			bean.setTrainingFlag("1");
		else
			bean.setTrainingFlag("0");
		bean.setCashier(((CMSEmployee) object.getTheOperator()).getExternalID());
		bean.setRegisterId(regId);
		bean.setStoreId(storeId);
		bean.setCurrencyCd(object.getStore().getCurrencyType().getCode());
		if (object instanceof CMSTransactionEOD) {
			CMSTransactionEOD obj = (CMSTransactionEOD) object;
			bean.setCloseoutFlag("1");
			statements.add(new ParametricStatement(ArmStgTxnHdrOracleBean.insertSql, bean.toList()));
			statements.addAll(Arrays.asList(armStgTxnDtlOracleDAO.getEODTxnDtlInsertSQL(obj)));
		}
		if (object instanceof CMSTransactionSOS) {
			CMSTransactionSOS obj = (CMSTransactionSOS) object;
			bean.setCloseoutFlag("0");
			statements.add(new ParametricStatement(ArmStgTxnHdrOracleBean.insertSql, bean.toList()));
			statements.addAll(Arrays.asList(armStgTxnDtlOracleDAO.getSOSTxnDtlInsertSQL(obj)));
		}
		if (object instanceof PaymentTransaction) {
			ASISTxnData asisTxn = null;
			PaymentTransaction obj = (PaymentTransaction) object;
			bean.setPayTxnType(obj.getTransactionType());
			if (obj instanceof VoidTransaction)
				bean.setVoidFlag("1");
			else
				bean.setVoidFlag("0");
			bean.setCloseoutFlag("0");
			if (obj instanceof CMSCompositePOSTransaction) {
				bean.setTxnCategory("1");
				// vishal 16 sept 2016 just make this cmpObj variable global
				 cmpObj = (CMSCompositePOSTransaction) obj;
				bean.setFiscalReceiptDate(cmpObj.getFiscalReceiptDate());
				bean.setFiscalNo(cmpObj.getFiscalReceiptNumber());
				if (cmpObj.getPresaleLineItemsArray() != null && cmpObj.getPresaleLineItemsArray().length > 0) {
					PresaleTransaction prsTxn = cmpObj.getPresaleTransaction();
					if (prsTxn != null)
						bean.setAddNo(prsTxn.getPresaleId());
				}
				if (cmpObj.getConsignmentLineItemsArray() != null && cmpObj.getConsignmentLineItemsArray().length > 0) {
					ConsignmentTransaction csgTxn = cmpObj.getConsignmentTransaction();
					if (csgTxn != null)
						bean.setAddNo(csgTxn.getConsignmentId());
				}
				if (cmpObj.getReservationLineItemsArray() != null && cmpObj.getReservationLineItemsArray().length > 0) {
					ReservationTransaction rsvTxn = cmpObj.getReservationTransaction();
					if (rsvTxn != null)
						bean.setAddNo(rsvTxn.getReservationId());
				}
				if (cmpObj.getEmployeeSale())
					bean.setEmployeeId(cmpObj.getEmployeeId());
				
				//Added by Rachana for apporval of return transaction
				if(cmpObj.getTotalPaymentAmount().stringValue().startsWith("-")){
					bean.setApproverId(cmpObj.getApprover());
				}
        
				//Fix for 1481: Return of employee sales - Employee_ID field is NULL in staging table
				Discount[] arrayDiscounts = null;
				POSLineItem[] posLineItem = cmpObj.getLineItemsArray();
				if (posLineItem != null && posLineItem.length > 0) {
					for (int i = 0;  i < posLineItem.length; i++) {
						if (posLineItem[i] instanceof ReturnLineItem) {
							SaleLineItem saleLineItem = ((ReturnLineItem)posLineItem[i]).getSaleLineItem();
							if (saleLineItem != null) {
								arrayDiscounts = saleLineItem.getDiscountsArray();
								if (arrayDiscounts != null && arrayDiscounts.length > 0) {
									for (int j = 0; j < arrayDiscounts.length; j++) {
										if (arrayDiscounts[j] instanceof CMSEmployeeDiscount) {
											bean.setEmployeeId(arrayDiscounts[j].getEmployee().getExternalID());
											System.out.println(" Ruchi insdie ArmStgTxnHDROracleDAO :"+arrayDiscounts[j].getReason());
										}
									}
								}
								//Added by Rachana for apporval of return transaction
								bean.setApproverId(cmpObj.getApprover());
							}
							
						}
					}
				}
				if (cmpObj.getCustomer() != null)
					bean.setCustomerId(cmpObj.getCustomer().getId());
				bean.setConsultantId(((CMSEmployee) cmpObj.getConsultant()).getExternalID());
				bean.setTaxJurisdiction(cmpObj.getStore().getState());
				bean.setTaxExemptId(cmpObj.getTaxExemptId());
				bean.setReductionAmount(currencyVal(cmpObj.getCompositeReductionAmount()));
				bean.setNetAmount(currencyVal(cmpObj.getCompositeNetAmount()));
				if (cmpObj.getASISTxnData() != null) {
					asisTxn = cmpObj.getASISTxnData();
				}
			}
			if (obj instanceof CMSCollectionTransaction) {
				bean.setFiscalReceiptDate(((CMSCollectionTransaction) obj).getFiscalReceiptDate());
				bean.setFiscalNo(((CMSCollectionTransaction) obj).getFiscalReceiptNumber());
				if (((CMSCollectionTransaction) obj).getASISTxnData() != null) {
					asisTxn = ((CMSCollectionTransaction) obj).getASISTxnData();
				}
				if (obj instanceof CMSMiscCollection) {
					if (((CMSMiscCollection) obj).getCustomer() != null)
						bean.setCustomerId(((CMSMiscCollection) obj).getCustomer().getId());
				}
			}
			// Redeemable Buyback Txn
			if (obj instanceof CMSRedeemableBuyBackTransaction) {
				if (((CMSRedeemableBuyBackTransaction) obj).getCustomer() != null)
					bean.setCustomerId(((CMSRedeemableBuyBackTransaction) obj).getCustomer().getId());
			}
			if (obj instanceof CMSPaidOutTransaction) {
				if (obj instanceof CMSMiscPaidOut) {
					if (((CMSMiscPaidOut) obj).getCustomer() != null) {
						bean.setCustomerId(((CMSMiscPaidOut) obj).getCustomer().getId());
					}
				}
				bean.setFiscalReceiptDate(((CMSPaidOutTransaction) obj).getFiscalReceiptDate());
				bean.setFiscalNo(((CMSPaidOutTransaction) obj).getFiscalReceiptNumber());
				if (((CMSPaidOutTransaction) obj).getASISTxnData() != null) {
					asisTxn = ((CMSPaidOutTransaction) obj).getASISTxnData();
				}
			}
			if (asisTxn != null) {
				bean.setAsisComments(asisTxn.getComments());
				bean.setAsisCompanyCd(asisTxn.getCompanyCode());
				bean.setAsisCustomerId(asisTxn.getCustNo());
				bean.setAsisCustomerName(asisTxn.getCustName());
				bean.setAsisFiscalDate(asisTxn.getFiscalReceiptDate());
				bean.setAsisFiscalDocDate(asisTxn.getFiscalDocDate());
				bean.setAsisFiscalDocNum(asisTxn.getFiscalDocNo());
				bean.setAsisFiscalDocType(asisTxn.getDocType());
				bean.setAsisFiscalReceiptNo(asisTxn.getFiscalReceiptNo());
				bean.setAsisRegisterId(asisTxn.getRegId());
				bean.setAsisStoreId(asisTxn.getStoreId());
				bean.setAsisTxnDate(asisTxn.getTxnDate());
				bean.setAsisTxnNum(asisTxn.getTxnNo());
				bean.setAsisOrderNo(asisTxn.getOrderNo());
				bean.setAsisOrderDate(asisTxn.getOrderDate());
				bean.setAsisSupplierNo(asisTxn.getSupplierNo());
				bean.setAsisSupplierDate(asisTxn.getSupplierDate());
				bean.setAsisNotes(asisTxn.getNotes());
			}
			// vishal : added for europe for FRANCHISING store requirement
			if(cmpObj!=null){
			if(cmpObj.getFranchising_Store()!=null && cmpObj.getFranchising_Store() && (cmpObj.getStaging_cust_id()!=null) && (!cmpObj.getStaging_cust_id())){
				ConfigMgr configMgr = new ConfigMgr("customer.cfg");
	               String custId=configMgr.getString("DUMMY_CUST_ID");
	               if(custId!=null){
		               bean.setCustomerId(custId);
			     }else{
			    	 bean.setCustomerId("P59999999");
			     }
			}
			}
			//END VISHAL 15 SEPT 2016
			statements.add(new ParametricStatement(ArmStgTxnHdrOracleBean.insertSql, bean.toList()));
			statements.addAll(Arrays.asList(armStgTxnDtlOracleDAO.processPaymentTxnDtl(obj)));
		}
		return (ParametricStatement[]) statements.toArray(new ParametricStatement[0]);
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
}
