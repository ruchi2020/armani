/*
 History:
 +------+------------+-----------+-----------+----------------------------------------------------+
 | Ver# | Date       | By        | Defect #  | Description                                        |
 +------+------------+-----------+-----------+----------------------------------------------------+
 | 2    | 05-05-2005|  Khyati   | N/A        |Added methods for store ID, scan code, promotion    |
 |                                           |code, expiration                                    |
 --------------------------------------------------------------------------------------------------
 *
 * formatted with JxBeauty (c) johann.langhofer@nextra.at
 */


package com.chelseasystems.cs.payment;

import com.chelseasystems.cr.rules.IRuleEngine;
import com.chelseasystems.cr.payment.*;
import java.util.Date;


/**
 *
 * <p>Title: Coupon</p>
 *
 * <p>Description: This class store the information of coupon</p>
 *
 * <p>Copyright: Copyright (c) 2005</p>
 *
 * <p>Company: </p>
 *
 * @author not attributable
 * @version 1.0
 */
public class Coupon extends ManufacturerCoupon implements IRuleEngine {
  private String code;
  private String desc;
  //ks : added store ID, scan code, promotion code, expiration
  private String storeId;
  private String regId;
  private String scanCode;
  private String promotionCode;
  private Date expirationDate;

  /**
   * Default Constructor
   */
  public Coupon() {
    this(null);
  }

  /**
   * Constructor
   * @param transactionPaymentName String
   */
  public Coupon(String transactionPaymentName) {
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

  //ks : added store ID, scan code, promotion code, expiration
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
    doSetExpirationDate(expDate);
    return;
  }

  /**
   * This method is used to set expiry date of coupon
   * @param expDate Date
   */
  public void doSetExpirationDate(Date expDate) {
    this.expirationDate = expDate;
  }
}
