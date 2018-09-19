//
// Copyright 2002, Retek Inc. All Rights Reserved.
//
package com.chelseasystems.cs.dataaccess.artsoracle.dao;

/////
import com.chelseasystems.cs.dataaccess.*;
import com.chelseasystems.cs.dataaccess.artsoracle.databean.*;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.sql.*;
import java.util.*;

import com.chelseasystems.cs.payment.CMSCoupon;
import com.chelseasystems.cs.payment.CMSPremioDiscount;
import com.chelseasystems.cs.payment.Coupon;
import com.chelseasystems.cs.payment.ICMSPaymentConstants;
import com.chelseasystems.cr.currency.ArmCurrency;
import com.chelseasystems.cr.currency.CurrencyType;
import com.chelseasystems.cr.database.ParametricStatement;
import com.chelseasystems.cr.payment.ManufacturerCoupon;
import com.chelseasystems.cs.payment.MallCert;
import com.chelseasystems.cr.payment.Payment;

public class CouponOracleDAO extends BaseOracleDAO implements CouponDAO{

  private static String selectSql = TrLtmCpnTndOracleBean.selectSql;
  private static String insertSql = TrLtmCpnTndOracleBean.insertSql;
  private static String updateSql = TrLtmCpnTndOracleBean.updateSql
      + where(TrLtmChkTndOracleBean.COL_AI_TRN, TrLtmChkTndOracleBean.COL_AI_LN_ITM);
  private static String deleteSql = TrLtmCpnTndOracleBean.deleteSql
      + where(TrLtmChkTndOracleBean.COL_AI_TRN);

  // Added by Satin for new coupon management PCR.
  private static String wheresqlForCouponManagement = "where ID_CPN = ?";
  private static String updateSqlForCouponManagement = CoCfgCpnOracleBean.updateSqlForCoupon + wheresqlForCouponManagement;
	
	
	// Added by Satin for new coupon management PCR.
	// Selects coupon from CO_CFG_CPN using couponId and storeId and returns CMSCoupon.
	public CMSCoupon findByBarcodeAndStoreId(String barcode, String storeId) throws SQLException {
	  String whereSql = "where ID_CPN = ? and ID_STR_RT = ?";  	
	  List params = new ArrayList();
	  params.add(barcode);
	  params.add(storeId);
	
	// Added by Satin for CMSCoupon.
	// Here a select query is created. beans is a select query: 'Select * from CO_CFG_CPN where ID_CPN = ?(couponId) and ID_STR_RT = ?(StoreId)'   
	  BaseOracleBean[] beans = this.query(new CoCfgCpnOracleBean(), 
			  CoCfgCpnOracleBean.selectSql + whereSql, params);
	  if (beans != null && beans.length > 0) {
	      return this.fromBeanToObject(beans[0]);
	  }
	  return null;  
	}

	
	
	public ParametricStatement[] getUpdateSQL (String couponUsedFlag, String couponId) throws SQLException {
	    
		List<String> params = new ArrayList();
		params.add(couponUsedFlag);
		params.add(couponId);
	    ArrayList statements = new ArrayList();
	    statements.add(new ParametricStatement(updateSqlForCouponManagement, params));

	    return  (ParametricStatement[])statements.toArray(new ParametricStatement[0]);
	  }
	
	
	
	public void updateCouponUsedFlag(String couponUsedFlag, String couponId) throws SQLException {
	    execute(getUpdateSQL(couponUsedFlag, couponId));
	  }
	
	// Added by Satin.
	// This will return a CMSCoupon object.
	private CMSCoupon fromBeanToObject(BaseOracleBean baseBean) {
	    CoCfgCpnOracleBean bean = (CoCfgCpnOracleBean)baseBean;
	    CMSCoupon object = new CMSCoupon(ICMSPaymentConstants.COUPON);
	    object.setCouponCode(bean.getIdCpn());
	    
	    /*Added by Vivek Mishra for retrieving the currency code from the Store record
	     *to use store specific currency not the base currency for coupons.
	     *Changes are done for Europe region as they required to use currency type RMB
	     *instead of EUR for HK stores.
	    **/
	    
	    object.setAmount(bean.getMoAmt());
	 	try{
	 	String queryState = "SELECT " + PaStrRtlOracleBean.COL_TY_CNY + " FROM " + PaStrRtlOracleBean.TABLE_NAME + " WHERE " + PaStrRtlOracleBean.COL_ID_STR_RT + " = ? ";
	 	String[] tyCny = queryForIds(queryState, Collections.singletonList(bean.getIdStrRt()));
	    ArmCurrency cur = new ArmCurrency(CurrencyType.getCurrencyType(tyCny[0]),bean.getMoAmt().doubleValue());
	    object.setAmount(cur);
	    }catch(Exception e){}
	    
	    //End
	    
	    object.setEffectiveDate(bean.getDtEf());
	    object.setExpirateDate(bean.getDtEx());
	    object.setCouponUsedFlag(bean.getCdSts());
	    object.setStoreId(bean.getIdStrRt());
	    /*try
	    {
	    (object.getAmount()).doSetCurrencyType(CurrencyType.getCurrencyType("EUR"));
	    }
	    catch(Exception e)
	    {
	    	
	    }*/
	    return object;
	}
  
  
   /**
   * put your documentation comment here
   * @param transactionId
   * @param sequenceNumber
   * @param txnPaymentName
   * @return
   * @exception SQLException
   */
  Payment getByTransactionIdAndSequenceNumber (String transactionId, int sequenceNumber, String txnPaymentName, String type) throws SQLException {
    String whereSql = where(TrLtmCpnTndOracleBean.COL_AI_TRN, TrLtmCpnTndOracleBean.COL_AI_LN_ITM);
    ArrayList list = new ArrayList();
    list.add(transactionId);
    list.add(new Integer(sequenceNumber));
    BaseOracleBean[] beans = query(new TrLtmCpnTndOracleBean(), whereSql, list);
    if (beans == null || beans.length == 0)
      return  null;
    return  fromBeanToObject(beans[0], txnPaymentName, type);
  }



  protected BaseOracleBean getDatabeanInstance() {
    return new TrLtmCpnTndOracleBean();
  }

  /////this method needs to customized
  private Payment fromBeanToObject(BaseOracleBean baseBean, String txnPaymentName, String type) {
    TrLtmCpnTndOracleBean bean = (TrLtmCpnTndOracleBean)baseBean;
    if (txnPaymentName.equals(PAYMENT_TYPE_COUPON)) {
      Coupon coupon = new Coupon(txnPaymentName);
      coupon.setType(bean.getTyCpn());
      coupon.setPromotionCode(bean.getLuCpnPrm());
      coupon.setRegisterId(bean.getIdWs());
      coupon.setStoreId(bean.getIdStrRt());
      coupon.setScanCode(bean.getUcCpnSc());
      return coupon;
    }
    else if (txnPaymentName.equals(PAYMENT_TYPE_MALLCERT)) {
      MallCert mallCert = new MallCert(txnPaymentName);
      mallCert.setRegisterId(bean.getIdWs());
      mallCert.setStoreId(bean.getIdStrRt());
      mallCert.setType(bean.getTyCpn());
      return mallCert;
    }
    else if (txnPaymentName.equals(PAYMENT_TYPE_PREMIO_DISCOUNT)) {
        CMSPremioDiscount premioDiscount = new CMSPremioDiscount(txnPaymentName);
        premioDiscount.setRegisterId(bean.getIdWs());
        premioDiscount.setStoreId(bean.getIdStrRt());
        premioDiscount.setLoyaltyNumber(bean.getUcCpnSc());
        return premioDiscount;
      }
    return null;
  }

}
