package com.chelseasystems.cs.loyalty;

import java.util.Date;
import com.chelseasystems.cr.business.BusinessObject;
import com.chelseasystems.cr.currency.ArmCurrency;

public class CMSPremioHistory extends BusinessObject{
	
	private Date txnDate = new Date();
	private String storeName = ""; 
	private String txnID = "";
	private String loyaltyNumber = "";
	private Double redeemedPoints = new Double(0.0d);
	private ArmCurrency txnPremioDiscountAmt = new ArmCurrency(0.0d);
	
	public CMSPremioHistory(){
	}
	
	public String getTransactionID(){
		return this.txnID; 
	}
	
	public String getLoyaltyNumber(){
		return this.loyaltyNumber;
	}
	
	public Date getTransactionDate(){
		return this.txnDate;
	}
	
	public ArmCurrency getTransactionPremioDiscountAmt(){
		return this.txnPremioDiscountAmt;
	}
	
	public String getStoreName(){
		return this.storeName; 
	}
	
	public Double getRedeemedPoints(){
		return this.redeemedPoints; 
	}
	
	public void doSetTransactionID(String txnID){
		this.txnID = txnID; 
	}
	
	public void doSetLoyaltyNumber(String loyaltyNum){
		this.loyaltyNumber = loyaltyNum;
	}
	
	public void doSetTransactionDate(Date txnDate){
		this.txnDate = txnDate;
	}
	
	public void doSetTransactionPremioDiscountAmt(ArmCurrency tenderAmt){
		this.txnPremioDiscountAmt = tenderAmt;
	}
	
	public void doSetStoreName(String storeName){
		this.storeName = storeName; 
	}
	
	public void doSetRedeemedPoints(Double redeemedPts){
		this.redeemedPoints = redeemedPts; 
	}
	
	public void setTransactionID(String txnID){
		doSetTransactionID(txnID); 
	}
	
	public void setLoyaltyNumber(String loyaltyNum){
		doSetLoyaltyNumber(loyaltyNum);
	}
	
	public void setTransactionDate(Date txnDate){
		doSetTransactionDate(txnDate);
	}
	
	public void setTransactionPremioDiscountAmt(ArmCurrency tenderAmt){
		doSetTransactionPremioDiscountAmt(tenderAmt);
	}
	
	public void setStoreName(String storeName){
		doSetStoreName(storeName); 
	}
	
	public void setRedeemedPoints(Double redeemedPts){
		doSetRedeemedPoints(redeemedPts); 
	}
}
