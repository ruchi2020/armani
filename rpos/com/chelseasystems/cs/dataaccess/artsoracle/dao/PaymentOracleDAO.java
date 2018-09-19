/*
 * put your module comment here
 * formatted with JxBeauty (c) johann.langhofer@nextra.at
 */
//
// Copyright 1999-2001, Chelsea Market Systems
//


package  com.chelseasystems.cs.dataaccess.artsoracle.dao;

import  java.sql.SQLException;
import  java.util.ArrayList;
import  java.util.Arrays;
import  java.util.List;

import com.chelseasystems.cr.appmgr.IApplicationManager;
import  com.chelseasystems.cr.database.ParametricStatement;
import  com.chelseasystems.cr.payment.Amex;
import  com.chelseasystems.cr.payment.BankCheck;
import  com.chelseasystems.cr.payment.BusinessCheck;
import  com.chelseasystems.cr.payment.Cash;
import  com.chelseasystems.cr.payment.Check;
import  com.chelseasystems.cr.payment.CreditCard;
import  com.chelseasystems.cr.payment.DebitCard;
import  com.chelseasystems.cr.payment.Discover;
import  com.chelseasystems.cr.payment.DueBill;
import  com.chelseasystems.cr.payment.DueBillIssue;
import  com.chelseasystems.cr.payment.EmployeeCheck;
import  com.chelseasystems.cr.payment.GiftCert;
import  com.chelseasystems.cr.payment.ManufacturerCoupon;
import  com.chelseasystems.cs.payment.CMSCoupon;
import  com.chelseasystems.cr.payment.MasterCard;
import  com.chelseasystems.cr.payment.MoneyOrder;
import  com.chelseasystems.cr.payment.Payment;
import  com.chelseasystems.cr.payment.Redeemable;
import  com.chelseasystems.cr.payment.StoreValueCard;
import  com.chelseasystems.cr.payment.TravellersCheck;
import  com.chelseasystems.cr.payment.Visa;
import com.chelseasystems.cr.pos.PaymentTransaction;
import  com.chelseasystems.cs.dataaccess.PaymentDAO;
import  com.chelseasystems.cs.dataaccess.artsoracle.databean.BaseOracleBean;
import  com.chelseasystems.cs.dataaccess.artsoracle.databean.TrLtmTndOracleBean;
import  com.chelseasystems.cs.payment.JCBCreditCard;
import  com.chelseasystems.cs.payment.Diners;
import  com.chelseasystems.cs.payment.CMSVisa;
import  com.chelseasystems.cs.payment.MallCert;
import  com.chelseasystems.cs.dataaccess.artsoracle.databean.TrLtmCpnTndOracleBean;
import  java.util.Date;
import com.armani.CMSCreditCard;

import com.chelseasystems.cs.payment.CMSPremioDiscount;
import  com.chelseasystems.cs.payment.CMSStoreValueCard;
import  com.chelseasystems.cs.payment.CMSRedeemable;
import  com.chelseasystems.cs.payment.CMSDueBill;
import  com.chelseasystems.cs.payment.HouseAccount;
import  com.chelseasystems.cs.payment.Coupon;
import  com.chelseasystems.cs.payment.MailCheck;
import  com.chelseasystems.cs.payment.CMSDueBillIssue;
import  com.chelseasystems.cs.payment.OutOfAreaCheck;
import  com.chelseasystems.cs.payment.LocalCheck;
import  com.chelseasystems.cs.payment.RoundPayment;
import  com.chelseasystems.cs.payment.Credit;




/**
 *
 *  Payment Data Access Object.<br>
 *  This object encapsulates all database access for Payment.<p>
 *  Below table shows the mapping between the object and the database tables and columns:<p>
 *  <table border=1>
 *    <tr><td><b>Object Attribute</b></td><td><b>Table Name</b></td><td><b>Column Name</b></td></tr>
 *    <tr><td>Amount</td><td>TR_LTM_TND</td><td>MO_ITM_LN_TND</td></tr>
 *    <tr><td>TransactionPaymentName</td><td>TR_LTM_TND</td><td>TY_TND</td></tr>
 *    <tr><td>journalKey</td><td>TR_LTM_TND</td><td>JOURNAL_KEY</td></tr>
 *    <tr><td>messageNumber</td><td>TR_LTM_TND</td>MSG_NUM</td></tr>
 *    <tr><td>respMessage</td><td>TR_LTM_TND</td>RES_MSG</td></tr>
 *    <tr><td>MerchantID</td><td>TR_LTM_TND</td>MERCHANT_ID</td></tr>
 *  </table>
 *
 *  @see Payment
 *  @see com.chelseasystems.cs.dataaccess.artsoracle.databean.BaseOracleBean
 *  @see com.chelseasystems.cs.dataaccess.artsoracle.databean.TrLtmTndOracleBean
 *
 */
/*
 History:
 +------+------------+-----------+-----------+----------------------------------------------------+
 | Ver# | Date       | By        | Defect #  | Description                                        |
 +------+------------+-----------+-----------+----------------------------------------------------+
 | 1    |            |           |           | Initial Version                                    |
 +------+------------+-----------+-----------+----------------------------------------------------+
 | 2    | 06-03-2005 | Megha     | 92        | Changed PAYMENT_TYPE_diners to PAYMENT_TYPE_DINERS |
 +------+------------+-----------+-----------+----------------------------------------------------+
 | 3    | 06-29-2005 | Vikram    | 235       | Transaction tendered with Gift card / credit note  |
 |      |            |           |           | generated error during View Transaction details    |
 +------+------------+-----------+-----------+----------------------------------------------------+
 */
public class PaymentOracleDAO extends BaseOracleDAO
    implements PaymentDAO {
  private static CheckOracleDAO checkDAO = new CheckOracleDAO();
  private static CouponOracleDAO couponDAO = new CouponOracleDAO();
  private static CreditCardOracleDAO creditCardDAO = new CreditCardOracleDAO();
  private static RedeemableOracleDAO redeemableDAO = new RedeemableOracleDAO();
  private static RedeemableIssueOracleDAO redeemableIssueDAO = new RedeemableIssueOracleDAO();
  private static String selectSql = TrLtmTndOracleBean.selectSql;
  private static String insertSql = TrLtmTndOracleBean.insertSql;
  private static String updateSql = TrLtmTndOracleBean.updateSql + where(TrLtmTndOracleBean.COL_AI_TRN, TrLtmTndOracleBean.COL_AI_LN_ITM);
  private static String deleteSql = TrLtmTndOracleBean.deleteSql + where(TrLtmTndOracleBean.COL_AI_TRN);
  //ruchi for Canada
  //private CMSPaymentTransactionAppModel theTxn = null;
  //public static IApplicationManager theAppMgr;

  /**
   * put your documentation comment here
   * @param transactionId
   * @param sequenceNumber
   * @param object
   * @return
   * @exception SQLException
   */
  ParametricStatement[] getInsertSQL (String transactionId, int sequenceNumber, Payment object) throws SQLException {
    try {
      ArrayList statements = new ArrayList();
      if (object != null && object instanceof Redeemable) {
        if (object instanceof CMSRedeemable) {
          CMSRedeemable redeemablePayment = (CMSRedeemable)object;
          Redeemable existingRedeemableObject = redeemableIssueDAO.selectRedeemableById(redeemablePayment.getId());
          if (existingRedeemableObject != null) {
            if (existingRedeemableObject instanceof CMSStoreValueCard) {
              CMSStoreValueCard storeValueCard = new CMSStoreValueCard(object.getTransactionPaymentName());
              storeValueCard.setId(existingRedeemableObject.getId());
              storeValueCard.setIssueAmount(redeemablePayment.getAmount());
              storeValueCard.setAmount(redeemablePayment.getAmount());
              storeValueCard.setGUIPaymentName(PAYMENT_TYPE_STORE_VALUE_CARD);
              storeValueCard.setIsManual(redeemablePayment.getIsManual());
              storeValueCard.setManualAuthCode(redeemablePayment.getManualAuthCode());
              storeValueCard.setCustomerId(redeemablePayment.getCustomerId());
              object = (CMSStoreValueCard)storeValueCard;
            }
            //            else if (existingRedeemableObject instanceof CMSDueBill) {
            else if (existingRedeemableObject instanceof CMSDueBillIssue || existingRedeemableObject instanceof CMSDueBill) {
              CMSDueBill dueBill = new CMSDueBill(object.getTransactionPaymentName());
              dueBill.setId(existingRedeemableObject.getId());
              dueBill.setIssueAmount(redeemablePayment.getAmount());
              dueBill.setAmount(redeemablePayment.getAmount());
              dueBill.setGUIPaymentName(PAYMENT_TYPE_DUE_BILL);
              dueBill.setIsManual(redeemablePayment.getIsManual());
              dueBill.setManualAuthCode(redeemablePayment.getManualAuthCode());
              dueBill.setCustomerId(redeemablePayment.getCustomerId());
              object = (CMSDueBill)dueBill;
            }
          }
          else {
            throw  new SQLException();
          }
        }       // object instance of cmsredeemable
      }
      ParametricStatement insertStatement = new ParametricStatement(insertSql, fromObjectToBean(transactionId, sequenceNumber, object).toList());
      statements.add(insertStatement);
      if (object instanceof Redeemable) {
        statements.addAll(Arrays.asList(redeemableDAO.getInsertSQL(transactionId, sequenceNumber, getPaymentTypeId(object), (Redeemable)object)));
      }
      if (object instanceof MallCert) {
        ParametricStatement pStmt = new ParametricStatement(TrLtmCpnTndOracleBean.insertSql, fromObjectToBean(transactionId, sequenceNumber, getPaymentTypeId(object), (MallCert)object).toList());
        statements.add(pStmt);
      }
      // For Coupon
      if (object instanceof Coupon) {
        ParametricStatement pStmt = new ParametricStatement(TrLtmCpnTndOracleBean.insertSql, fromObjectToBean(transactionId, sequenceNumber, getPaymentTypeId(object), (Coupon)object).toList());
        statements.add(pStmt);
      }
      
      // Changes made by Satin for new coupon management PCR. This is to insert coupon informantion into tr_ltm_cpn_tnd table.
      if (object instanceof CMSCoupon) {
    	  ParametricStatement pStmt = new ParametricStatement(TrLtmCpnTndOracleBean.insertSql, fromObjectToBean(transactionId, sequenceNumber, getPaymentTypeId(object), (CMSCoupon)object).toList());
          statements.add(pStmt);
        }
      if (object instanceof CMSPremioDiscount) {
          ParametricStatement pStmt = new ParametricStatement(TrLtmCpnTndOracleBean.insertSql, fromObjectToBean(transactionId, sequenceNumber, getPaymentTypeId(object), (CMSPremioDiscount)object).toList());
          statements.add(pStmt);
        }
      if (object instanceof BankCheck || object instanceof EmployeeCheck)
        statements.add(checkDAO.getInsertSQL(transactionId, sequenceNumber, getPaymentTypeId(object), (Check)object));
      else if (object instanceof CreditCard) {
          statements.add(creditCardDAO.getInsertSQL(transactionId, sequenceNumber, getPaymentTypeId(object), (CreditCard)object));
      }
      return  (ParametricStatement[])statements.toArray(new ParametricStatement[0]);
    } catch (Exception ex) {
      ex.printStackTrace();
      throw  new SQLException();
    }
  }

  /**
   * put your documentation comment here
   * @param transactionId
   * @param sequenceNumber
   * @param object
   * @return
   * @exception SQLException
   */
  ParametricStatement[] getUpdateSQL (String transactionId, int sequenceNumber, Payment object) throws SQLException {
    List list = fromObjectToBean(transactionId, sequenceNumber, object).toList();
    list.add(transactionId);
    list.add(new Integer(sequenceNumber));
    ParametricStatement updateStatement = new ParametricStatement(updateSql, list);
    ArrayList statements = new ArrayList();
    statements.add(updateStatement);
    if (object instanceof BankCheck || object instanceof EmployeeCheck)
      statements.add(checkDAO.getUpdateSQL(transactionId, sequenceNumber, getPaymentTypeId(object), (Check)object));
    else if (object instanceof CreditCard)
      statements.add(creditCardDAO.getUpdateSQL(transactionId, sequenceNumber, getPaymentTypeId(object), (CreditCard)object));
    else if (object instanceof Redeemable)
      statements.addAll(Arrays.asList(redeemableDAO.getUpdateSQL(transactionId, sequenceNumber, getPaymentTypeId(object), (Redeemable)object)));
    return  (ParametricStatement[])statements.toArray(new ParametricStatement[0]);
  }

  /**
   * put your documentation comment here
   * @param transactionId
   * @param object
   * @return
   * @exception SQLException
   */
  ParametricStatement[] getDeleteSQL (String transactionId, Payment object) throws SQLException {
    ArrayList statements = new ArrayList();
    if (object instanceof BankCheck || object instanceof EmployeeCheck)
      statements.add(checkDAO.getDeleteSQL(transactionId));
    else if (object instanceof CreditCard)
      statements.add(creditCardDAO.getDeleteSQL(transactionId));
    else if (object instanceof Redeemable)
      statements.addAll(Arrays.asList(redeemableDAO.getDeleteSQL(transactionId)));
    ArrayList list = new ArrayList();
    list.add(transactionId);
    statements.add(new ParametricStatement(deleteSql, list));
    return  (ParametricStatement[])statements.toArray(new ParametricStatement[0]);
  }

  /**
   * put your documentation comment here
   * @param transactionId
   * @return
   * @exception SQLException
   */
  Payment[] getByTransactionId (String transactionId) throws SQLException {
    String whereSql = where(TrLtmTndOracleBean.COL_AI_TRN);
    return  fromBeansToObjects(query(new TrLtmTndOracleBean(), whereSql, transactionId));
  }

  /**
   * put your documentation comment here
   * @return
   */
  protected BaseOracleBean getDatabeanInstance () {
    return  new TrLtmTndOracleBean();
  }

  /**
   * put your documentation comment here
   * @param beans
   * @return
   * @exception SQLException
   */
  private Payment[] fromBeansToObjects (BaseOracleBean[] beans) throws SQLException {
    Payment[] array = new Payment[beans.length];
    for (int i = 0; i < array.length; i++)
      array[i] = fromBeanToObject(beans[i]);
    return  array;
  }

  /**
   * put your documentation comment here
   * @param baseBean
   * @return
   * @exception SQLException
   */
  private Payment fromBeanToObject (BaseOracleBean baseBean) throws SQLException {
    TrLtmTndOracleBean bean = (TrLtmTndOracleBean)baseBean;
    //Vishal Yeavle : to consider Maestro Card as Master Card : fix 1 feb 2017
    if(bean.getLuClsTnd()!=null && bean.getLuClsTnd().equalsIgnoreCase("MAESTRO")){
    	bean.setLuClsTnd("MASTER_CARD");
    } //end Vishal Yevale 1 feb 2017
    Payment object = getNewPaymentObject(bean);
    if (bean.getMoItmLnTnd() != null)
      object.setAmount(bean.getMoItmLnTnd());
    String txnPaymentName = bean.getTyTnd();
    // To check for transaction data coming from interface
    if (txnPaymentName.equals(ArtsConstants.PAYMENT_TYPE_MASTER_CARD) || txnPaymentName.equals(ArtsConstants.PAYMENT_TYPE_VISA)) {
      txnPaymentName = "BCRD";
    }
    object.setTransactionPaymentName(txnPaymentName);
    // Added Journal Key, Message Number, Response Msg, Merchant ID.
    object.setJournalKey(bean.getJournalKey());
    object.setMessageNumber(bean.getMsgNum());
    object.setRespMessage(bean.getResMsg());
    object.setMerchantID(bean.getMerchantId());
    String paymentCode = bean.getCode();
    if (paymentCode != null)
      object.doSetPaymentCode(paymentCode);
    return  object;
  }

  /**
   * put your documentation comment here
   * @param transactionId
   * @param sequenceNumber
   * @param object
   * @return
   */
  private TrLtmTndOracleBean fromObjectToBean (String transactionId, int sequenceNumber, Payment object) {
    TrLtmTndOracleBean bean = new TrLtmTndOracleBean();
    bean.setAiTrn(transactionId);
    bean.setAiLnItm(sequenceNumber);
    bean.setMoItmLnTnd(object.getAmount());
    bean.setLuClsTnd(getPaymentTypeId(object));
    bean.setTyTnd(object.getTransactionPaymentName());
    // Added methods for Journal Key, Message Number, RespMessage, Merchant ID.
    bean.setJournalKey(object.getJournalKey());
    bean.setMsgNum(object.getMessageNumber());
    bean.setResMsg(object.getRespMessage());
    bean.setMerchantId(object.getMerchantID());
    String paymentCode = object.getPaymentCode();
    if (paymentCode != null)
      bean.setCode(paymentCode);
    return  bean;
  }

  /**
   * put your documentation comment here
   * @param transactionId
   * @param sequenceNumber
   * @param type
   * @param object
   * @return
   */
  private TrLtmCpnTndOracleBean fromObjectToBean (String transactionId, int sequenceNumber, String type, Payment object) {
    TrLtmCpnTndOracleBean bean = new TrLtmCpnTndOracleBean();
    bean.setAiTrn(transactionId);
    bean.setAiLnItm(sequenceNumber);
    if (object instanceof MallCert) {
      bean.setIdStrRt(((MallCert)object).getStoreId());
      bean.setIdWs(((MallCert)object).getRegisterId());
      bean.setTyCpn(((MallCert)object).getType());
    }
    // Changes made by Satin for coupon management PCR.
    else if (object instanceof CMSCoupon) {
      bean.setIdStrRt(((CMSCoupon)object).getStoreId());
      bean.setIdWs(((CMSCoupon)object).getRegisterId());
      bean.setUcCpnSc(((CMSCoupon)object).getCouponCode());
      
//    below code is to set coupon reasonCode and couponType.
//    bean.setTyCpn(((CMSCoupon)object).getType());
//    bean.setLuCpnPrm(((CMSCoupon)object).getPromotionCode());
    }
    
    else if (object instanceof Coupon) {
          bean.setIdStrRt(((Coupon)object).getStoreId());
          bean.setIdWs(((Coupon)object).getRegisterId());
          bean.setTyCpn(((Coupon)object).getType());
          bean.setUcCpnSc(((Coupon)object).getScanCode());
          bean.setLuCpnPrm(((Coupon)object).getPromotionCode());
    }
    else if (object instanceof CMSPremioDiscount) {
        bean.setIdStrRt(((CMSPremioDiscount)object).getStoreId());
        bean.setIdWs(((CMSPremioDiscount)object).getRegisterId());
        bean.setUcCpnSc(((CMSPremioDiscount)object).getLoyaltyNumber());
        bean.setIdWs(((CMSPremioDiscount)object).getRedeemPoints());
    }
    bean.setDcDyBsn(new Date());
    return  bean;
  }

  /**
   * put your documentation comment here
   * @param object
   * @return
   */
  private String getPaymentTypeId (Payment object) {
	  //ruchi
	String type = PAYMENT_TYPE_UNKNOWN;
    if (object instanceof Cash) {  
      type = PAYMENT_TYPE_CASH;
    }
    else if (object instanceof MallCert) {
      type = PAYMENT_TYPE_MALLCERT;
    }
    else if (object instanceof CMSPremioDiscount) {
        type = PAYMENT_TYPE_PREMIO_DISCOUNT;
      }
    else if (object instanceof Check) {
      if (object instanceof EmployeeCheck)
        type = PAYMENT_TYPE_EMPLOYEE_CHECK;
      else if (object instanceof BankCheck) {
        if (object instanceof BusinessCheck) {
          type = PAYMENT_TYPE_BUSINESS_CHECK;
        }
        else
          type = PAYMENT_TYPE_BANK_CHECK;
      }
      //ks: Local and out of area checks for europe
      else if (object instanceof LocalCheck) {
        type = PAYMENT_TYPE_LOCAL_CHECK;
      }
      else if (object instanceof OutOfAreaCheck) {
        type = PAYMENT_TYPE_OUT_OF_AREA_CHECK;
      }
      else if (object instanceof MoneyOrder)
        type = PAYMENT_TYPE_MONEY_ORDER;
      else if (object instanceof TravellersCheck)
        type = PAYMENT_TYPE_TRAVELLERS_CHECK;
      else
        type = PAYMENT_TYPE_CHECK;
    }
    //Ruchi Canada changes
    else if ((object instanceof CreditCard)&& (!((CreditCard)object).getAccountNumber().equalsIgnoreCase("4444333322221111"))) {
    if (object instanceof Amex)
        type = PAYMENT_TYPE_AMEX;
      else if (object instanceof Discover)
        type = PAYMENT_TYPE_DISCOVER;
      else if (object instanceof MasterCard)
        type = PAYMENT_TYPE_MASTER_CARD;
      // Added for CMSVisa
      else if (object instanceof Visa){
        type = PAYMENT_TYPE_VISA;
        }
      else if (object instanceof DebitCard)
        type = PAYMENT_TYPE_DEBIT_CARD;
      // Added for JCB & Diners.
      else if (object instanceof JCBCreditCard){
        type = PAYMENT_TYPE_JCB;
       }
      //Changed PAYMENT_TYPE_diners to PAYMENT_TYPE_DINERS
      else if (object instanceof Diners)
        type = PAYMENT_TYPE_DINERS;
    //sonali:Added for saving payment type in TR_LTM_CRDB_CRD_TN
      else
      {
if(object.getGUIPaymentName()!=null && (!object.getGUIPaymentName().equals(""))){ 
    	  type=object.getGUIPaymentName();
    	  //Vishal Yevale : Added for saving CREDIT_CARD rather than it's Merchant name
    	  if(!object.isUSRegion() && type.toUpperCase().contains("CREDIT")){
    		  return type=PAYMENT_TYPE_CREDIT_CARD;
    	  }
    	  //end Vishal yevale
    	 if((object.getGUIPaymentName().contains("MASTER"))){
        	  type = PAYMENT_TYPE_MASTER_CARD;
          }
    	  //Added the below as CREDIT CARD entry is not present in CO_CLS_TND
         else if((object.getGUIPaymentName().contains("Credit"))){
            type = PAYMENT_TYPE_CREDIT_CARD;
          }
         else if((object.getGUIPaymentName().contains("Debit"))){
             type = PAYMENT_TYPE_DEBIT_CARD;
           }
 
    	  }
    
     else {  
    	 type = PAYMENT_TYPE_CREDIT_CARD;
     }
     }
    	  
    }else if ((object instanceof CreditCard)){
    	if((object.getGUIPaymentName().equalsIgnoreCase("MASTER_CARD"))&&((CreditCard)object).getAccountNumber().equalsIgnoreCase("4444333322221111")){
    		type = PAYMENT_TYPE_MASTER_CARD;
    	}else if((object.getGUIPaymentName().equalsIgnoreCase("VISA"))&&((CreditCard)object).getAccountNumber().equalsIgnoreCase("4444333322221111")){
    		type = PAYMENT_TYPE_VISA;
    	}else if((object.getGUIPaymentName().equalsIgnoreCase("AMEX"))&&((CreditCard)object).getAccountNumber().equalsIgnoreCase("4444333322221111")){
    		type = PAYMENT_TYPE_AMEX;
    	}else if((object.getGUIPaymentName().equalsIgnoreCase("JCB"))&&((CreditCard)object).getAccountNumber().equalsIgnoreCase("4444333322221111")){    		
    		type = PAYMENT_TYPE_JCB;
    	}else if((object.getGUIPaymentName().equalsIgnoreCase("Debit"))&&((CreditCard)object).getAccountNumber().equalsIgnoreCase("4444333322221111")){
    		type = PAYMENT_TYPE_DINERS;
    	}
    }
    else if (object instanceof Redeemable) {
      if (object instanceof GiftCert)
        type = PAYMENT_TYPE_GIFT_CERTIFICATE;
      else if (object instanceof StoreValueCard || object instanceof CMSStoreValueCard) {
        type = PAYMENT_TYPE_STORE_VALUE_CARD;
      }
      else if (object instanceof DueBill || object instanceof CMSDueBill) {
        if (object instanceof DueBillIssue)
          type = PAYMENT_TYPE_DUE_BILL_ISSUE;
        else
          type = PAYMENT_TYPE_DUE_BILL;
      }
      else if (object instanceof HouseAccount) {
        type = PAYMENT_TYPE_HOUSE_ACCOUNT;
      }
    }
    else if (object instanceof Coupon) {
      type = PAYMENT_TYPE_COUPON;
    }
    else if (object instanceof ManufacturerCoupon) {
      type = PAYMENT_TYPE_MANUFACTURE_COUPON;
    }
    else if (object instanceof MailCheck) {
      type = PAYMENT_TYPE_MAIL_CHECK;
    }
    //ks: Round Payment
    else if (object instanceof RoundPayment) {
      type = PAYMENT_TYPE_ROUND_PAYMENT;
    }
    else if (object instanceof Credit) {
      type = PAYMENT_TYPE_CREDIT;
    }
    else {
      type = PAYMENT_TYPE_UNKNOWN;
    }
    return  type;
  }

  /**
   * put your documentation comment here
   * @param bean
   * @return
   * @exception SQLException
   */
  private Payment getNewPaymentObject (TrLtmTndOracleBean bean) throws SQLException {
    String type = bean.getLuClsTnd();
    Payment payment = null;
    if (type.equals(PAYMENT_TYPE_UNKNOWN))
      payment = null;
    else if (type.equals(PAYMENT_TYPE_CASH))
      payment = getCashObject(bean);
    else if (type.equals(PAYMENT_TYPE_CAD_CASH))
      payment = getCashObject(bean);
    else if (type.equals(PAYMENT_TYPE_CHECK))
      payment = getCheckObject(bean);
    else if (type.equals(PAYMENT_TYPE_EMPLOYEE_CHECK))
      payment = getCheckObject(bean);
    else if (type.equals(PAYMENT_TYPE_BANK_CHECK))
      payment = getCheckObject(bean);
    else if (type.equals(PAYMENT_TYPE_CAD_BANK_CHECK))
      payment = getCheckObject(bean);
    else if (type.equals(PAYMENT_TYPE_BUSINESS_CHECK))
      payment = getCheckObject(bean);
    else if (type.equals(PAYMENT_TYPE_CAD_BUSINESS_CHECK))
      payment = getCheckObject(bean);
    else if (type.equals(PAYMENT_TYPE_MONEY_ORDER))
      payment = getCheckObject(bean);
    else if (type.equals(PAYMENT_TYPE_TRAVELLERS_CHECK))
      payment = getCheckObject(bean);
    else if (type.equals(PAYMENT_TYPE_CREDIT_CARD))
      payment = getCreditCardObject(bean);
    else if (type.equals(PAYMENT_TYPE_AMEX))
      payment = getCreditCardObject(bean);
    else if (type.equals(PAYMENT_TYPE_DISCOVER))
      payment = getCreditCardObject(bean);
    else if (type.equals(PAYMENT_TYPE_MASTER_CARD))
      payment = getCreditCardObject(bean);
    else if (type.equals(PAYMENT_TYPE_VISA))
      payment = getCreditCardObject(bean);
    //ks: added for Europe
    else if (type.equals(PAYMENT_TYPE_LOCAL_CHECK))
      payment = getCheckObject(bean);
    else if (type.equals(PAYMENT_TYPE_OUT_OF_AREA_CHECK))
      payment = getCheckObject(bean);
    else if (type.equals(PAYMENT_TYPE_ROUND_PAYMENT))
      payment = getRoundPaymentObject(bean);
    else if (type.equals(PAYMENT_TYPE_CREDIT))
      payment = getCreditObject(bean);
    // Added methods for JCB & Diners
    else if (type.equals(PAYMENT_TYPE_JCB))
      payment = getCreditCardObject(bean);
    else if (type.equals(PAYMENT_TYPE_DINERS))
      payment = getCreditCardObject(bean);
    else if (type.equals(PAYMENT_TYPE_DEBIT_CARD))
      payment = getCreditCardObject(bean);
    else if (type.equals(PAYMENT_TYPE_REDEEMABLE))
      payment = getRedeemableObject(bean);
    else if (type.equals(PAYMENT_TYPE_GIFT_CERTIFICATE))
      payment = getRedeemableObject(bean);
    else if (type.equals(PAYMENT_TYPE_STORE_VALUE_CARD))
      payment = getRedeemableObject(bean);
    else if (type.equals(PAYMENT_TYPE_DUE_BILL))
      payment = getRedeemableObject(bean);
    else if (type.equals(PAYMENT_TYPE_DUE_BILL_ISSUE))
      payment = getRedeemableObject(bean);
    else if (type.equals(PAYMENT_TYPE_COUPON))
      payment = getManufacturerCouponObject(bean);
    else if (type.equals(PAYMENT_TYPE_MANUFACTURE_COUPON))
      payment = getManufacturerCouponObject(bean);
    else if (type.equals(PAYMENT_TYPE_MALLCERT))
      payment = getMallCertObject(bean);
    else if (type.equals(PAYMENT_TYPE_PREMIO_DISCOUNT))
        payment = getPremioDiscountObject(bean);
    else if (type.equals(PAYMENT_TYPE_HOUSE_ACCOUNT))
      payment = getHouseAccountObject(bean);
    else if (type.equals(PAYMENT_TYPE_MAIL_CHECK)) {
      payment = new MailCheck(bean.getTyTnd());
    }
    return  payment;
  }

  private Payment getPremioDiscountObject (TrLtmTndOracleBean bean) throws SQLException{
	    String txnPaymentName = bean.getLuClsTnd();
	    return couponDAO.getByTransactionIdAndSequenceNumber(bean.getAiTrn(), bean.getAiLnItm().intValue(), txnPaymentName, bean.getTyTnd());
  }


  /**
   * put your documentation comment here
   * @param bean
   * @return
   */
  private Payment getMallCertObject (TrLtmTndOracleBean bean) throws SQLException{
    String txnPaymentName = bean.getLuClsTnd();
    return couponDAO.getByTransactionIdAndSequenceNumber(bean.getAiTrn(), bean.getAiLnItm().intValue(), txnPaymentName, bean.getTyTnd());
  }

  /**
   * put your documentation comment here
   * @param bean
   * @return
   */
  private Payment getCashObject (TrLtmTndOracleBean bean) {
    return  new Cash(bean.getTyTnd());
  }

  /**
   * put your documentation comment here
   * @param bean
   * @return
   * @exception SQLException
   */
  private Payment getCheckObject (TrLtmTndOracleBean bean) throws SQLException {
    String type = bean.getLuClsTnd();
    String txnPaymentName = bean.getTyTnd();
    if (type.equals(PAYMENT_TYPE_CHECK))
      return  new Check(txnPaymentName);
    else if (type.equals(PAYMENT_TYPE_MONEY_ORDER))
      return  new MoneyOrder(txnPaymentName);
    else if (type.equals(PAYMENT_TYPE_TRAVELLERS_CHECK))
      return  new TravellersCheck(txnPaymentName);
    else if (type.equals(PAYMENT_TYPE_LOCAL_CHECK))
      return  new LocalCheck(txnPaymentName);
    else if (type.equals(PAYMENT_TYPE_OUT_OF_AREA_CHECK))
      return  new OutOfAreaCheck(txnPaymentName);
    return  checkDAO.getByTransactionIdAndSequenceNumber(bean.getAiTrn(), bean.getAiLnItm().intValue(), txnPaymentName);
  }

  /**
   * put your documentation comment here
   * @param bean
   * @return
   * @exception SQLException
   */
  private Payment getCreditCardObject (TrLtmTndOracleBean bean) throws SQLException {
    return  creditCardDAO.getByTransactionIdAndSequenceNumber(bean.getAiTrn(), bean.getAiLnItm().intValue(), bean.getTyTnd());
  }

  /**
   * put your documentation comment here
   * @param bean
   * @return
   * @exception SQLException
   */
  private Payment getRedeemableObject (TrLtmTndOracleBean bean) throws SQLException {
    return  redeemableDAO.getByTransactionIdAndSequenceNumber(bean.getAiTrn(), bean.getAiLnItm().intValue(), bean.getTyTnd());
  }

  /**
   * put your documentation comment here
   * @param bean
   * @return
   */
  private Payment getManufacturerCouponObject (TrLtmTndOracleBean bean) throws SQLException {
    String type = bean.getLuClsTnd();
    String txnPaymentName = bean.getTyTnd();
    if (type.equals(PAYMENT_TYPE_COUPON))
      return  couponDAO.getByTransactionIdAndSequenceNumber(bean.getAiTrn(), bean.getAiLnItm().intValue(), txnPaymentName, type);
    return new ManufacturerCoupon(bean.getTyTnd());
  }

  /**
   * put your documentation comment here
   * @param bean
   * @return
   */
  private Payment getHouseAccountObject (TrLtmTndOracleBean bean) {
    return  new HouseAccount(bean.getTyTnd());
  }

  /**
   * put your documentation comment here
   * @param bean
   * @return
   */
  private Payment getRoundPaymentObject (TrLtmTndOracleBean bean) {
    return  new RoundPayment(bean.getTyTnd());
  }

  /**
   * put your documentation comment here
   * @param bean
   * @return
   */
  private Payment getCreditObject (TrLtmTndOracleBean bean) {
    return  new Credit(bean.getTyTnd());
  }

}



