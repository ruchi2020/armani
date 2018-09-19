package com.chelseasystems.cs.payment;

import com.chelseasystems.cr.rules.IRuleEngine;
import java.io.Serializable;
import com.chelseasystems.cr.payment.*;
import java.util.Date;


/**
*
* <p>Title: CMSCoupon</p>
*
* <p>Description: This class was created to extend Coupon object for Armani Europe. This class is responsible to set attributes of Coupon Object.</p>
*
* <p>Copyright: Copyright (c) 2012</p>
*
* <p>Company: </p>
*
* @author Satin
* @version 1.0
* 
*/

public class CMSCoupon extends ManufacturerCoupon implements IRuleEngine, Serializable {
 private String code;
 private String desc;
 private String storeId;
 private String regId;
 private String scanCode;
 private String promotionCode;
 private Date expirationDate;
 private Date effectiveDate;
 private String couponUsedFlag;
 private String couponId;
 private String cmsCouponAmount;
 

 /**
  * Default Constructor
  */
 public CMSCoupon() {
   this(null);
 }

 /**
  * Constructor
  * @param transactionPaymentName String
  */
 public CMSCoupon(String transactionPaymentName) {
   super(transactionPaymentName);
 }
 /**
  * This method is used to set type of coupon
  * @param code String
  */
 public void setType(String code) {
   this.code = code;
 }

 /**
  * This method is used to get type of coupon
  * @return String
  */
 public String getType() {
   return code;
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

 /**
  * This method is used to get store id of coupon
  * @return String
  */
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
  * This method is used to get registerId of Coupon object.
  * @return RegisterId String
  */
 public String getRegisterId() {
   return this.regId;
 }

 /**
  * This method is used to set reg id of coupon object.
  * @param regId String
  */
 public void setRegisterId(String regId) {
   doSetRegisterId(regId);
   return;
 }

 /**
  * This method is used to set reg id of coupon.
  * @param regId String
  */
 public void doSetRegisterId(String regId) {
   this.regId = regId;
   return;
 }

 /**
  * This method is used to get scan code of coupon
  * @return String
  */
 public String getScanCode() {
   return this.scanCode;
 }

 /**
  * This method is used to set scan code of coupon
  * @param scanCode String
  */
 public void setScanCode(String scanCode) {
   doSetScanCode(scanCode);
   return;
 }

 /**
  * This method is used to set scan code of coupon
  * @param scanCode String
  */
 public void doSetScanCode(String scanCode) {
   this.scanCode = scanCode;
   return;
 }

 /**
  * This method is used to get promotion code of coupon
  * @return String
  */
 public String getPromotionCode() {
   return this.promotionCode;
 }

 /**
  * This method is used to set promotion code of coupon
  * @param promotionCode String
  */
 public void setPromotionCode(String promotionCode) {
   doSetPromotionCode(promotionCode);
   return;
 }

 /**
  * This method is used to set promotion code of coupon
  * @param promotionCode String
  */
 public void doSetPromotionCode(String promotionCode) {
   this.promotionCode = promotionCode;
   return;
 }

 /**
  * This method is used to get expiry date of coupon
  * @return Date
  */
 public Date getExpirateDate() {
   return this.expirationDate;
 }

 /**
  * This method is used to set expiry date of coupon
  * @param expDate Date
  */
 public void setExpirateDate(Date expDate) {
   doSetExpirateDate(expDate);
   return;
 }

 /**
  * This method is used to set expiry date of coupon
  * @param expDate Date
  */
 public void doSetExpirateDate(Date expDate) {
   this.expirationDate = expDate;
 }
 
 /**
  * This method is used to get effective date of coupon
  * @return Date
  */
 public Date getEffectiveDate() {
   return this.effectiveDate;
 }

 /**
  * This method is used to set effective date of coupon
  * @param expDate Date
  */
 public void setEffectiveDate(Date effcDate) {
   doSetEffectiveDate(effcDate);
   return;
 }

 /**
  * This method is used to set effective date of coupon
  * @param expDate Date
  */
 public void doSetEffectiveDate(Date effcDate) {
   this.effectiveDate = effcDate;
 }
 
 /**
  * This method is used to get coupon used flag of coupon.
  * @return String
  */
 public String getCouponUsedFlag() {
	    return this.couponUsedFlag;
	  }

	  
 /**
  * This method is used to set coupon used flag
  * @param  cpnUsedFlag String
  */
 public void setCouponUsedFlag(String cpnUsedFlag) {
    doSetCouponUsedFlag(cpnUsedFlag);
    return;
  }

	  
 /**
  * This method is used to set coupon used flag
  * @param cpnUsedFlag String
  */
	  public void doSetCouponUsedFlag(String cpnUsedFlag) {
	    this.couponUsedFlag = cpnUsedFlag;
	  }
 

 /**
  * This method is used to get coupon id of coupon.
  * @return String
  */
	  public String getCouponCode() {
	    return this.couponId;
	  }

 /**
  * This method is used to set coupon id of coupon
  * @param couponId String
  */
  public void setCouponCode(String couponId) {
    doSetCouponCode(couponId);
    return;
  }

	  
  /**
   * This method is used to set coupon id of coupon
   * @param couponId String
   */
	public void doSetCouponCode(String couponId) {
	  this.couponId = couponId;
	  return;
	}

	  
  /**
   * This method is used to get coupon amount.
   * @return String
   */
  public String getCMSCouponAmount() {
    return this.cmsCouponAmount;
  }


  /**
   * This method is used to set coupon amount.
   * @param cmsCouponAmount String
   */
	public void setCMSCouponAmount(String cmsCouponAmount) {
	   doSetCMSCouponAmount(cmsCouponAmount);
	   return;
	 }

  /**
   * This method is used to set coupon amount.
   * @param cmsCouponAmount String
   */
  public void doSetCMSCouponAmount(String cmsCouponAmount) {
    this.cmsCouponAmount = cmsCouponAmount;
    return;
  }


}
