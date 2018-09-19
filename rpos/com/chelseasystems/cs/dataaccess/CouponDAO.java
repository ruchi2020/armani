package com.chelseasystems.cs.dataaccess;


import  com.chelseasystems.cr.item.*;
import  java.sql.*;
import  java.util.Map;
import  com.chelseasystems.cs.payment.*;
import  com.chelseasystems.cr.store.Store;
import  com.chelseasystems.cr.database.ParametricStatement;


public interface CouponDAO extends BaseDAO
{

   /**
   * Added by Satin.
   * This method finds coupon from the database based on barcode scanned and store id.
   * @param barcode String
   * @param storeId String
   * @throws SQLException
   * @return CMSCoupon
   */
	public CMSCoupon findByBarcodeAndStoreId (String barcode, String storeId) throws SQLException;
	
	
	/**
	   * Added by Satin.
	   * This method returns ParametricStatement which is used to update coupon used flag in the database.
	   * @param couponUsedFlag String
	   * @param couponId String
	   * @throws SQLException
	   * @return ParametricStatement
	   */
	public ParametricStatement[] getUpdateSQL (String couponUsedFlag, String couponId)throws SQLException;
	  
}
