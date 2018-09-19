/**
 * Title:        <p>
 * Description:  <p>
 * Copyright:    Copyright (c) <p>
 * Company:      <p>
 * @author
 * @version 1.0
 *
 * formatted with JxBeauty (c) johann.langhofer@nextra.at
 */


package com.chelseasystems.cs.payment;

import com.chelseasystems.cr.payment.*;
import com.chelseasystems.cs.payment.HouseAccount;


/**
 *
 * <p>Title: CMSRedeemableServices</p>
 *
 * <p>Description: </p>
 *
 * <p>Copyright: Copyright (c) 2005</p>
 *
 * <p>Company: </p>
 *
 * @author not attributable
 * @version 1.0
 */
public abstract class CMSRedeemableServices extends RedeemableServices {

  //  public abstract HouseAccount findHouseAccount(String id) throws Exception;
  /**
   * Method used to find redeemables on the basis of customer id
   * @param customerid String
   * @return Redeemable[]
   * @throws Exception
   */
  public abstract Redeemable[] findByCustomerId(String customerid)
      throws Exception;

  /**
   * Method used to find house accounts on the basis of customer id
   * @param customerid String
   * @return Redeemable[]
   * @throws Exception
   */
  public abstract Redeemable[] findHouseAccountByCustomerId(String customerid)
	      throws Exception;
  
  
  /**
   * Added by Satin.
   * This method finds coupon from the database based on barcode scanned and store id.
   * @param barCode String, StoreId String 
   * @throws Exception
   * @return CMSCoupon
   */
  public abstract CMSCoupon findByBarcodeAndStoreId(String barCode, String StoreId)
      throws Exception;

  /**
   * Method used to find redeemables on the basis of id
   * @param type String
   * @param id String
   * @return Redeemable
   * @throws Exception
   */
  public abstract Redeemable findRedeemableById(String type, String id)
      throws Exception;
  //  public abstract Redeemable[] findHouseAccountById(String controlId) throws Exception;
}
