package com.chelseasystems.cs.payment;

import com.chelseasystems.cr.payment.*;
import com.chelseasystems.cr.rules.IRuleEngine;
import com.chelseasystems.cs.swing.builder.CMSPremioDiscountBldr;

public class CMSPremioDiscount extends ManufacturerCoupon implements IRuleEngine {

	private String desc; 
	private String loyaltyNumber;
	private String custId;
	private String storeId;
	private String regId;
	private String redeemPoints;
	
	private double loyaltyRewardRatio;

	/**
	   * Default Constructor
	   */
	  public CMSPremioDiscount() {
	    this(null);
	  }

	  /**
	   * Constructor
	   * @param transactionPaymentName String
	   */
	  public CMSPremioDiscount(String transactionPaymentName) {
	    super(transactionPaymentName);
	    if(CMSPremioDiscountBldr.loyaltyRewardRatio!=null)
	    loyaltyRewardRatio = Double.parseDouble(CMSPremioDiscountBldr.loyaltyRewardRatio);
	  }
	
	  /**
	   * This method is used to set description of coupon
	   * @param desc String
	   */
	  public void setDesc(String desc) {
	    this.desc = desc;
	  }

	  /**
	   * This method is used to get description of coupon
	   * @return String
	   */
	  public String getDesc() {
	    return desc;
	  }
	
	  
	  public String getCustId() {
			return custId;
		}

		public void setCustId(String custId) {
			this.custId = custId;
		}

		public String getLoyaltyNumber() {
			return loyaltyNumber;
		}

		public void setLoyaltyNumber(String loyaltyNumber) {
			this.loyaltyNumber = loyaltyNumber;
		}
		
		
	  
	public boolean isAuthRequired() {
		// TODO Auto-generated method stub
		return false;
	}

	public boolean isSignatureRequired() {
		// TODO Auto-generated method stub
		return false;
	}

	public boolean isOverPaymentAllowed() {
		// TODO Auto-generated method stub
		return false;
	}

	public boolean isFrankingRequired() {
		// TODO Auto-generated method stub
		return false;
	}

	public String getStoreId() {
	    return this.storeId;
	  }

	  /**
	   * This method is used to set store id of coupon
	   * @param storeId String
	   */
	  public void setStoreId(String storeId) {
	    doSetStoreId(storeId);
	    return;
	  }

	  /**
	   * This method is used to set store id of coupon
	   * @param storeId String
	   */
	  public void doSetStoreId(String storeId) {
	    this.storeId = storeId;
	    return;
	  }

	  /**
	   * put your documentation comment here
	   * @return
	   */
	  public String getRegisterId() {
	    return this.regId;
	  }

	  /**
	   * This method is used to set reg id of coupon
	   * @param regId String
	   */
	  public void setRegisterId(String regId) {
	    doSetRegisterId(regId);
	    return;
	  }

	  /**
	   * This method is used to set reg id of coupon
	   * @param regId String
	   */
	  public void doSetRegisterId(String regId) {
	    this.regId = regId;
	    return;
	  }
	  /**
	   * put your documentation comment here
	   * @return
	   */
	  public String getRedeemPoints() {
	    return this.redeemPoints;
	  }

	  /**
	   * This method is used to set reg id of coupon
	   * @param regId String
	   */
	  public void setRedeemPoints(String points) {
	    doSetRedeemPoints(points);
	    return;
	  }

	  /**
	   * This method is used to set reg id of coupon
	   * @param regId String
	   */
	  public void doSetRedeemPoints(String points) {
	    this.redeemPoints= points;
	    return;
	  }

	public double getLoyaltyRewardRatio() {
		return loyaltyRewardRatio;
	}

	public void setLoyaltyRewardRatio(double loyaltyRewardRatio) {
		this.loyaltyRewardRatio = loyaltyRewardRatio;
	}
}
