/*
 History:
 +------+------------+-----------+-----------+----------------------------------------------------+
 | Ver# | Date       | By        | Defect #  | Description                                        |
 +------+------------+-----------+-----------+----------------------------------------------------+
 | 8    | 04-04-2006 | Sandhya   | PCR1167   | Sku lookup and visibility                          |
 +------+------------+-----------+-----------+----------------------------------------------------+
 | 7    | 05-25-2005 | Megha     | N/A       | Customer Messages                                  |
 +------+------------+-----------+-----------+----------------------------------------------------+
 | 19   | 05-13-2005 | Rajesh    | N/A       | Sales audit implementation                         |
 +------+------------+-----------+-----------+----------------------------------------------------+
 | 6    | 05-12-2005 | Vikram    | N/A       | Reward Discount Specification                      |
 +------+------------+-----------+-----------+----------------------------------------------------+
 | 5    | 04-29-2005 | Pankaja   | N/A       | Resolved the issue related to returns              |
 --------------------------------------------------------------------------------------------------
 | 4    | 04-27-2005 | Pankaja   | N/A       | New method for updation of the expiration dt       |
 --------------------------------------------------------------------------------------------------
 | 3    | 04-17-2005 | Pankaja   | N/A       | Modify persistTransaction method to handle PRS/CSG |
 +------+------------+-----------+-----------+----------------------------------------------------+
 | 2    | 04-15-2005 | Rajesh    | N/A       | modify findByPresaleId/findByConsignmentId methods |
 +------+------------+-----------+-----------+----------------------------------------------------+
 *
 * formatted with JxBeauty (c) johann.langhofer@nextra.at
 */


package com.chelseasystems.cs.pos;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Vector;

import com.chelseasystems.cr.collection.CollectionTransaction;
import com.chelseasystems.cr.config.ConfigMgr;
import com.chelseasystems.cr.config.FileMgr;
import com.chelseasystems.cr.currency.ArmCurrency;
import com.chelseasystems.cr.currency.CurrencyException;
import com.chelseasystems.cr.customer.Customer;
import com.chelseasystems.cr.database.ParametricStatement;
import com.chelseasystems.cr.discount.Discount;
import com.chelseasystems.cr.item.Item;
import com.chelseasystems.cr.layaway.Layaway;
import com.chelseasystems.cr.layaway.LayawayPaymentInfo;
import com.chelseasystems.cr.layaway.LayawayPaymentTransaction;
import com.chelseasystems.cr.layaway.LayawayRTSTransaction;
import com.chelseasystems.cr.logging.LoggingServices;
import com.chelseasystems.cr.payment.DueBill;
import com.chelseasystems.cr.payment.DueBillIssue;
import com.chelseasystems.cr.payment.GiftCert;
import com.chelseasystems.cr.payment.IPaymentConstants;
import com.chelseasystems.cr.payment.Payment;
import com.chelseasystems.cr.payment.Redeemable;
import com.chelseasystems.cr.payment.RedeemableHist;
import com.chelseasystems.cr.payment.StoreValueCard;
import com.chelseasystems.cr.pos.AdHocQueryConstraints;
import com.chelseasystems.cr.pos.CompositePOSTransaction;
import com.chelseasystems.cr.pos.POSLineItem;
import com.chelseasystems.cr.pos.POSLineItemDetail;
import com.chelseasystems.cr.pos.PaymentTransaction;
import com.chelseasystems.cr.pos.RedeemableBuyBackTransaction;
import com.chelseasystems.cr.pos.ReturnLineItem;
import com.chelseasystems.cr.pos.ReturnLineItemDetail;
import com.chelseasystems.cr.pos.SaleLineItemDetail;
import com.chelseasystems.cr.pos.TransactionHeader;
import com.chelseasystems.cr.pos.VoidTransaction;
import com.chelseasystems.cr.transaction.ITransaction;
import com.chelseasystems.cr.transfer.Transfer;
import com.chelseasystems.cr.transfer.TransferIn;
import com.chelseasystems.cr.txnposter.PaymentSummary;
import com.chelseasystems.cr.txnposter.SalesSummary;
import com.chelseasystems.cr.txnposter.TxnTypeSummary;
import com.chelseasystems.cr.util.DateUtil;
import com.chelseasystems.cs.boardingpass.CMSAirportDetails;
import com.chelseasystems.cs.collection.CMSCollectionTransaction;
import com.chelseasystems.cs.collection.CMSMiscCollection;
import com.chelseasystems.cs.collection.CMSMiscCollectionCredit;
import com.chelseasystems.cs.customer.CMSCustomer;
import com.chelseasystems.cs.customer.CMSCustomerMessage;
import com.chelseasystems.cs.customer.CreditHistory;
import com.chelseasystems.cs.dataaccess.ArmAirportListDAO;
import com.chelseasystems.cs.dataaccess.ArmCustSaleSummaryDAO;
import com.chelseasystems.cs.dataaccess.ArmPosCsgDAO;
import com.chelseasystems.cs.dataaccess.ArmPosPrsDAO;
import com.chelseasystems.cs.dataaccess.ArmPosRsvDAO;
import com.chelseasystems.cs.dataaccess.ArmStgTxnHdrDAO;
import com.chelseasystems.cs.dataaccess.CollectionDAO;
import com.chelseasystems.cs.dataaccess.CustomerDAO;
import com.chelseasystems.cs.dataaccess.EmpSaleDAO;
import com.chelseasystems.cs.dataaccess.LayawayDAO;
import com.chelseasystems.cs.dataaccess.LayawayPaymentInfoDAO;
import com.chelseasystems.cs.dataaccess.PaidOutDAO;
import com.chelseasystems.cs.dataaccess.PaymentSummaryDAO;
import com.chelseasystems.cs.dataaccess.PaymentTransactionDAO;
import com.chelseasystems.cs.dataaccess.PosLineItemDetailDAO;
import com.chelseasystems.cs.dataaccess.RedeemableHistDAO;
import com.chelseasystems.cs.dataaccess.RedeemableIssueDAO;
import com.chelseasystems.cs.dataaccess.SalesSummaryDAO;
import com.chelseasystems.cs.dataaccess.TransactionDAO;
import com.chelseasystems.cs.dataaccess.TransactionHeaderDAO;
import com.chelseasystems.cs.dataaccess.TransferOutDAO;
import com.chelseasystems.cs.dataaccess.TxnTypeSummaryDAO;
import com.chelseasystems.cs.dataaccess.VArmTxnHdrDAO;
import com.chelseasystems.cs.dataaccess.VoidTransactionDAO;
import com.chelseasystems.cs.dataaccess.ArmStgFiscalDocDAO;
import com.chelseasystems.cs.dataaccess.ArmStgTxnDtlDAO;
import com.chelseasystems.cs.dataaccess.LoyaltyDAO;
// Added by Satin for Digital Signature
import com.chelseasystems.cs.dataaccess.artsoracle.dao.CompositeOracleDAO;
import com.chelseasystems.cs.dataaccess.CompositeDAO;

//added to update coupon used flag
import com.chelseasystems.cs.dataaccess.CouponDAO;
import com.chelseasystems.cs.discount.RewardDiscount;
import com.chelseasystems.cs.item.CMSItem;
import com.chelseasystems.cs.layaway.CMSLayaway;

//added to update coupon used flag
import com.chelseasystems.cs.payment.CMSCoupon;
import com.chelseasystems.cs.payment.CMSDueBill;
import com.chelseasystems.cs.payment.CMSDueBillIssue;
import com.chelseasystems.cs.payment.CMSRedeemable;
import com.chelseasystems.cs.payment.CMSStoreValueCard;
import com.chelseasystems.cs.payment.Credit;
import com.chelseasystems.cs.xml.PaymentTransactionXML;
import com.chelseasystems.cs.loyalty.*;
//added for update coupon flag
//added for update boarding pass table in case of pending txns
import com.chelseasystems.cs.dataaccess.artsoracle.dao.ArmAirportListOracleDAO;
import com.chelseasystems.cs.dataaccess.artsoracle.dao.CouponOracleDAO;
import com.chelseasystems.cs.dataaccess.artsoracle.dao.ArmCustSaleSummaryOracleDAO;
import com.chelseasystems.cs.dataaccess.artsoracle.dao.ArmStgTxnDtlOracleDAO;
import com.chelseasystems.cs.dataaccess.artsoracle.dao.ArmStgTxnHdrOracleDAO;
import com.chelseasystems.cs.dataaccess.artsoracle.dao.CompositeOracleDAO;
import com.chelseasystems.cs.dataaccess.artsoracle.dao.CustomerOracleDAO;
import com.chelseasystems.cs.dataaccess.artsoracle.dao.PaymentTransactionOracleDAO;
import com.chelseasystems.cs.dataaccess.artsoracle.dao.PosLineItemOracleDAO;
import com.chelseasystems.cs.dataaccess.artsoracle.databean.ArmCustDepositHistOracleBean;
import com.chelseasystems.cs.dataaccess.ItemDAO;
import com.chelseasystems.cs.customer.DepositHistory;
import com.chelseasystems.cs.fiscaldocument.FiscalDocumentNumber;
import com.chelseasystems.cs.dataaccess.ArmFiscalDocNoDAO;
import com.chelseasystems.cs.dataaccess.ArmFiscalDocumentDAO;
import com.chelseasystems.cs.fiscaldocument.FiscalDocument;
import com.chelseasystems.cs.paidout.CMSMiscPaidOut;
import com.chelseasystems.cs.dataaccess.artsoracle.databean.ArmStgTxnHdrOracleBean;
import com.chelseasystems.cs.dataaccess.ArmItmSohDAO;
import com.chelseasystems.cs.dataaccess.artsoracle.databean.TrTrnOracleBean;
import com.chelseasystems.cs.dataaccess.artsoracle.dao.EmpSaleOracleDAO;


/**
 * put your documentation comment here
 */
public class CMSTransactionPOSJDBCServices extends CMSTransactionPOSServices {
  private CustomerDAO customerDAO;
  //added by sonali for duty free management PCR for Europe
  private ArmAirportListDAO airportDAO;
  private TransactionDAO transactionDAO;
  // Added by Satin for getting digital Signature from TR_RTL
  private CompositeDAO compositeDAO;
  private PaymentTransactionDAO paymentTransactionDAO;
  private PaidOutDAO paidOutDAO;
  private CollectionDAO collectionDAO;
  private VoidTransactionDAO voidTransactionDAO;
  private PosLineItemDetailDAO posLineItemDetailDAO;
  private LayawayDAO layawayDAO;
  private RedeemableIssueDAO redeemableIssueDAO;
  // Added by Satin.
  // Added for coupon management
  private CouponDAO couponDAO;
  private RedeemableHistDAO redeemableHistDAO;
  private LayawayPaymentInfoDAO layawayPaymentInfoDAO;
  private TransactionHeaderDAO transactionHeaderDAO;
  private EmpSaleDAO empSaleDAO;
  private PaymentSummaryDAO paymentSummaryDAO;
  private TxnTypeSummaryDAO txnTypeSummaryDAO;
  private SalesSummaryDAO salesSummaryDAO;
  private TransferOutDAO transferOutDAO;
  private VArmTxnHdrDAO vArmTxnHdrDAO;
  private ArmPosCsgDAO armPosCsgDAO;
  private ArmPosPrsDAO armPosPrsDAO;
  private ArmPosRsvDAO armPosRsvDAO;
  private ArmStgTxnHdrDAO armStgTxnHdrDAO;
  private ArmStgTxnDtlDAO armStgTxnDtlDAO;
  private ArmStgFiscalDocDAO armStgFiscalDocDAO;
  private LoyaltyDAO loyaltyDAO;
  private ArmFiscalDocumentDAO armFiscalDocumentDAO;
  private ArmFiscalDocNoDAO armFiscalDocNoDAO;
  private ArmItmSohDAO armItmSohDAO;
  private ItemDAO itemDAO;
  private ArmCustSaleSummaryDAO custSaleSummaryDAO;
  private final String GUI_TRN_TYP_RESERVATION_OPEN = "RSVO";
 

  /**
   * Default Constructor
   */
  public CMSTransactionPOSJDBCServices() {
	ConfigMgr configMgr = new ConfigMgr("jdbc.cfg");
	customerDAO = (CustomerDAO)configMgr.getObject("CUSTOMER_DAO");
    transactionDAO = (TransactionDAO)configMgr.getObject("TRANSACTION_DAO");
    // Added by Satin for Digital Signature.
    compositeDAO = (CompositeDAO)configMgr.getObject("COMPOSITE_DAO");
    paymentTransactionDAO = (PaymentTransactionDAO)configMgr.getObject("PAYMENTTRANSACTION_DAO");
    paidOutDAO = (PaidOutDAO)configMgr.getObject("PAIDOUT_DAO");
    collectionDAO = (CollectionDAO)configMgr.getObject("COLLECTION_DAO");
    voidTransactionDAO = (VoidTransactionDAO)configMgr.getObject("VOIDTRANSACTION_DAO");
    posLineItemDetailDAO = (PosLineItemDetailDAO)configMgr.getObject("POSLINEITEMDETAIL_DAO");
    layawayDAO = (LayawayDAO)configMgr.getObject("LAYAWAY_DAO");
    redeemableIssueDAO = (RedeemableIssueDAO)configMgr.getObject("REDEEMABLEISSUE_DAO");
    redeemableHistDAO = (RedeemableHistDAO)configMgr.getObject("REDEEMABLEHIST_DAO");
    layawayPaymentInfoDAO = (LayawayPaymentInfoDAO)configMgr.getObject("LAYAWAYPAYMENTINFO_DAO");
    transactionHeaderDAO = (TransactionHeaderDAO)configMgr.getObject("TRANSACTIONHEADERINFO_DAO");
    empSaleDAO = (EmpSaleDAO)configMgr.getObject("EMPSALE_DAO");
    paymentSummaryDAO = (PaymentSummaryDAO)configMgr.getObject("PAYMENTSUMMARY_DAO");
    txnTypeSummaryDAO = (TxnTypeSummaryDAO)configMgr.getObject("TXNTYPESUMMARY_DAO");
    salesSummaryDAO = (SalesSummaryDAO)configMgr.getObject("SALESSUMMARYDAO_DAO");
    transferOutDAO = (TransferOutDAO)configMgr.getObject("TRANSFEROUT_DAO");
    vArmTxnHdrDAO = (VArmTxnHdrDAO)configMgr.getObject("ARMTRANSACTIONHEADERINFO_DAO");
    armPosCsgDAO = (ArmPosCsgDAO)configMgr.getObject("ARMPOSCSG_DAO");
    armPosPrsDAO = (ArmPosPrsDAO)configMgr.getObject("ARMPOSPRS_DAO");
    armPosRsvDAO = (ArmPosRsvDAO)configMgr.getObject("ARMPOSRSV_DAO");
    armStgTxnHdrDAO = (ArmStgTxnHdrDAO)configMgr.getObject("ARMSTGTXNHDR_DAO");
    loyaltyDAO = (LoyaltyDAO)configMgr.getObject("LOYALTY_DAO");
    armFiscalDocumentDAO = (ArmFiscalDocumentDAO)configMgr.getObject("ARMFISCALDOCUMENT_DAO");
    armFiscalDocNoDAO = (ArmFiscalDocNoDAO)configMgr.getObject("ARMFISCALDOCUMENTNUMBER_DAO");
    armStgFiscalDocDAO = (ArmStgFiscalDocDAO)configMgr.getObject("ARMSTGFISCALDOC_DAO");
    armStgTxnDtlDAO = (ArmStgTxnDtlDAO)configMgr.getObject("ARMSTGTXNDTL_DAO");
    //System.out.println("armStgTxnDtlDAO: "+armStgTxnDtlDAO);
    armItmSohDAO = (ArmItmSohDAO)configMgr.getObject("ITEM_SOH_DAO");
    itemDAO = (ItemDAO)configMgr.getObject("ITEM_DAO");
    custSaleSummaryDAO = (ArmCustSaleSummaryDAO)configMgr.getObject("ARMCUSTSALESUMMARY_DAO");
    // Added by Satin.
    //added to update CMSCoupon
    couponDAO = (CouponDAO) configMgr.getObject("COUPON_DAO");
    //added by sonali for duty free management PCR for Europe
	airportDAO=(ArmAirportListDAO)configMgr.getObject("AIRPORT_DAO");
    
  }

  /**
   * This method is used to search a PaymentTransaction by its id
   * @param id String
   * @return PaymentTransaction
   * @throws Exception
   */
  public PaymentTransaction findById(String id)
      throws java.lang.Exception {
    try {
      return paymentTransactionDAO.selectById(id);
    } catch (Exception exception) {
      LoggingServices.getCurrent().logMsg(this.getClass().getName(), "findById", "Exception"
          , "See Exception", LoggingServices.MAJOR, exception);
      exception.printStackTrace();
      throw exception;
    }
  }
  
  //added by shushma for promotion
 public String[] findPromCodeByTxnId(String txnId) throws Exception{
 try {
	ArmStgTxnDtlOracleDAO armOracleDAO=(ArmStgTxnDtlOracleDAO)armStgTxnDtlDAO;
	return armOracleDAO.selectByTxnId(txnId);
} catch (Exception exception) {
  LoggingServices.getCurrent().logMsg(this.getClass().getName(), "findById", "Exception"
      , "See Exception", LoggingServices.MAJOR, exception);
  exception.printStackTrace();
  throw exception;
}
}
 
 
 //Added by Anjana to persist promotion code in DB for returns
public String findPromCodeByTxnIdandBarcode(String txnId , String barcode) throws Exception{
try {
	 ArmStgTxnDtlOracleDAO armOracleDAO = new ArmStgTxnDtlOracleDAO();
		return armOracleDAO.selectByTxnIdandBarcode(txnId,barcode);
} catch (Exception exception) {
 LoggingServices.getCurrent().logMsg(this.getClass().getName(), "findById", "Exception"
     , "See Exception", LoggingServices.MAJOR, exception);
 exception.printStackTrace();
 throw exception;
}
}

  /**
   * This method is used to search a PaymentTransaction by its id
   * @param customerId String
   * @return PaymentTransaction[]
   * @throws Exception
   */
  public PaymentTransaction[] findByCustomerId(String customerId)
      throws java.lang.Exception {
    try {
      TransactionHeader[] headers = transactionHeaderDAO.selectByCustomerId(customerId);
      List list = new ArrayList();
      for (int i = 0; i < headers.length; i++) {
        PaymentTransaction transaction = paymentTransactionDAO.selectById(headers[i].getId());
        if (transaction != null)
          list.add(transaction);
      }
      return (PaymentTransaction[])list.toArray(new PaymentTransaction[0]);
    } catch (Exception exception) {
      LoggingServices.getCurrent().logMsg(this.getClass().getName(), "findByCustomerId"
          , "Exception", "See Exception", LoggingServices.MAJOR, exception);
      exception.printStackTrace();
      throw exception;
    }
  }

  /**
   *
   * @param sCutomerId String
   * @param dtStart Date
   * @param dtEnd Date
   * @return CMSTransactionHeader[]
   * @throws Exception
   */
  public CMSTransactionHeader[] findByCustomerIdAndDates(String sCutomerId, Date dtStart
      , Date dtEnd)
      throws Exception {
    try {
      return (CMSTransactionHeader[])transactionHeaderDAO.selectByCustomerIdAndDates(sCutomerId
          , dtStart, dtEnd);
    } catch (Exception exception) {
      LoggingServices.getCurrent().logMsg(this.getClass().getName(), "findByCustomerIdAndDate"
          , "Exception", "See Exception", LoggingServices.MAJOR, exception);
      exception.printStackTrace();
      throw exception;
    }
  }

  /**
   *
   * @param sCutomerId String
   * @param sPaymentType String
   * @return CMSTransactionHeader[]
   * @throws Exception
   */
  public CMSTransactionHeader[] findByCustomerIdAndPaymentType(String sCutomerId
      , String sPaymentType)
      throws Exception {
    try {
      return (CMSTransactionHeader[])transactionHeaderDAO.selectByCustomerIdAndPaymentType(
          sCutomerId, sPaymentType);
    } catch (Exception exception) {
      LoggingServices.getCurrent().logMsg(this.getClass().getName()
          , "findByCustomerIdAndPaymentType", "Exception", "See Exception", LoggingServices.MAJOR
          , exception);
      exception.printStackTrace();
      throw exception;
    }
  }

  /**
   *
   * @param sCutomerId String
   * @return CMSTransactionHeader[]
   * @throws Exception
   */
  public CMSTransactionHeader[] findByCustomerIdAndShippingRequested(String sCutomerId)
      throws Exception {
    try {
      return (CMSTransactionHeader[])transactionHeaderDAO.selectByCustomerIdAndShippingRequested(
          sCutomerId);
    } catch (Exception exception) {
      LoggingServices.getCurrent().logMsg(this.getClass().getName()
          , "findByCustomerIdAndShippingRequested", "Exception", "See Exception"
          , LoggingServices.MAJOR, exception);
      exception.printStackTrace();
      throw exception;
    }
  }

  /**
   *
   * @param txnSrchStr TransactionSearchString
   * @return CMSTransactionHeader[]
   * @throws Exception
   */
  public CMSTransactionHeader[] findBySearchCriteria(TransactionSearchString txnSrchStr)
      throws Exception {
    try {
      return (CMSTransactionHeader[])transactionHeaderDAO.selectBySearchCriteria(txnSrchStr);
    } catch (Exception exception) {
      LoggingServices.getCurrent().logMsg(this.getClass().getName(), "findBySearchCriteria"
          , "Exception", "See Exception", LoggingServices.MAJOR, exception);
      exception.printStackTrace();
      throw exception;
    }
  }

  /**
   * This method is used to search a Transaction on the basis of store id,
   * consultant id and for a specific date range
   * @param storeId String
   * @param beginDate Date
   * @param endDate Date
   * @param consultantId String
   * @return TransactionHeader[]
   * @throws Exception
   */
  public TransactionHeader[] findByConsultantID(String storeId, Date beginDate, Date endDate
      , String consultantId)
      throws Exception {
    try {
      //Date begin = DateUtil.getBeginingOfDay(beginDate);
      //Date end = DateUtil.getEndOfDay(endDate);
      return transactionHeaderDAO.selectByStoreIdAndDatesAndConsultantId(storeId, beginDate
          , endDate, consultantId);
    } catch (Exception exception) {
      LoggingServices.getCurrent().logMsg(this.getClass().getName(), "findByConsultantID"
          , "Exception", "See Exception", LoggingServices.MAJOR, exception);
      exception.printStackTrace();
      throw exception;
    }
  }

  /**
   * This method is used to search a Transaction on the basis of store id,
   * discount type and for a specific date range
   * @param storeId String
   * @param beginDate Date
   * @param endDate Date
   * @param discountType String
   * @return TransactionHeader[]
   * @throws Exception
   */
  public TransactionHeader[] findByDiscountType(String storeId, Date beginDate, Date endDate
      , String discountType)
      throws Exception {
    try {
      //Date begin = DateUtil.getBeginingOfDay(beginDate);
      //Date end = DateUtil.getEndOfDay(endDate);
      return transactionHeaderDAO.selectByStoreIdAndDatesAndDiscountType(storeId, beginDate
          , endDate, discountType);
    } catch (Exception exception) {
      LoggingServices.getCurrent().logMsg(this.getClass().getName(), "findByDiscountType"
          , "Exception", "See Exception", LoggingServices.MAJOR, exception);
      exception.printStackTrace();
      throw exception;
    }
  }

  /**
   * This method is used to search a Transaction on the basis of store id,
   * exact amount and for a specific date range
   * @param storeId String
   * @param beginDate Date
   * @param endDate Date
   * @param amount Currency
   * @return TransactionHeader[]
   * @throws Exception
   */
  public TransactionHeader[] findByExactAmount(String storeId, Date beginDate, Date endDate
      , ArmCurrency amount)
      throws Exception {
    try {
      //Date begin = DateUtil.getBeginingOfDay(beginDate);
      //Date end = DateUtil.getEndOfDay(endDate);
      return transactionHeaderDAO.selectByStoreIdAndDatesAndAmount(storeId, beginDate, endDate
          , amount);
    } catch (Exception exception) {
      LoggingServices.getCurrent().logMsg(this.getClass().getName(), "findByExactAmount"
          , "Exception", "See Exception", LoggingServices.MAJOR, exception);
      exception.printStackTrace();
      throw exception;
    }
  }

  /**
   * This method is used to search a Transaction on the basis of store id,
   * operator id and for a specific date range
   * @param storeId String
   * @param beginDate Date
   * @param endDate Date
   * @param operatorId String
   * @return TransactionHeader[]
   * @throws Exception
   */
  public TransactionHeader[] findByOperatorID(String storeId, Date beginDate, Date endDate
      , String operatorId)
      throws Exception {
    try {
      // Date begin = DateUtil.getBeginingOfDay(beginDate);
      //Date end = DateUtil.getEndOfDay(endDate);
      return transactionHeaderDAO.selectByStoreIdAndDatesAndOperatorId(storeId, beginDate, endDate
          , operatorId);
    } catch (Exception exception) {
      LoggingServices.getCurrent().logMsg(this.getClass().getName(), "findByOperatorID"
          , "Exception", "See Exception", LoggingServices.MAJOR, exception);
      exception.printStackTrace();
      throw exception;
    }
  }

  /**
   * This method is used to search a Transaction on the basis of store id,
   * payment type and for a specific date range
   * @param storeId String
   * @param beginDate Date
   * @param endDate Date
   * @param paymentType String
   * @return TransactionHeader[]
   * @throws Exception
   */
  public TransactionHeader[] findByPaymentType(String storeId, Date beginDate, Date endDate
      , String paymentType)
      throws Exception {
    try {
      //Date begin = DateUtil.getBeginingOfDay(beginDate);
      // Date end = DateUtil.getEndOfDay(endDate);
      return transactionHeaderDAO.selectByStoreIdAndDatesAndPaymentType(storeId, beginDate, endDate
          , paymentType);
    } catch (Exception exception) {
      LoggingServices.getCurrent().logMsg(this.getClass().getName(), "findByPaymentType"
          , "Exception", "See Exception", LoggingServices.MAJOR, exception);
      exception.printStackTrace();
      throw exception;
    }
  }

  /**
   * This method is used to search a Transaction on the basis of store id,
   * credit payment type and for a specific date range
   * @param storeId String
   * @param beginDate Date
   * @param endDate Date
   * @param creditPaymentTypes String[]
   * @return TransactionHeader[]
   * @throws Exception
   */
  public TransactionHeader[] findByCreditPaymentType(String storeId, Date beginDate, Date endDate
      , String[] creditPaymentTypes)
      throws Exception {
    try {
      //Date begin = DateUtil.getBeginingOfDay(/);
      //Date end = DateUtil.getEndOfDay(endDate);
      return transactionHeaderDAO.selectByStoreIdAndDatesAndCreditPaymentTypes(storeId, beginDate
          , endDate, creditPaymentTypes);
    } catch (Exception exception) {
      LoggingServices.getCurrent().logMsg(this.getClass().getName(), "findByCreditPaymentType"
          , "Exception", "See Exception", LoggingServices.MAJOR, exception);
      exception.printStackTrace();
      throw exception;
    }
  }

  /**
   * This method is used to search a Transaction on the basis of store id,
   * transaction type and for a specific date range
   * @param storeId String
   * @param beginDate Date
   * @param endDate Date
   * @param transactionType String
   * @return TransactionHeader[]
   * @throws Exception
   */
  public TransactionHeader[] findByTransactionType(String storeId, Date beginDate, Date endDate
      , String transactionType)
      throws Exception {
    try {
      //Date begin = DateUtil.getBeginingOfDay(beginDate);
      //Date end = DateUtil.getEndOfDay(endDate);
      return transactionHeaderDAO.selectByStoreIdAndDatesAndTransactionType(storeId, beginDate
          , endDate, transactionType);
    } catch (Exception exception) {
      LoggingServices.getCurrent().logMsg(this.getClass().getName(), "findByTransactionType"
          , "Exception", "See Exception", LoggingServices.MAJOR, exception);
      exception.printStackTrace();
      throw exception;
    }
  }

  /**
   *
   * @param customerId String
   * @return TransactionHeader[]
   * @throws Exception
   */
  public TransactionHeader[] findByCustomerIdHeader(String customerId)
      throws java.lang.Exception {
    try {
      return transactionHeaderDAO.selectByCustomerId(customerId);
    } catch (Exception exception) {
      LoggingServices.getCurrent().logMsg(this.getClass().getName(), "findByCustomerIdHeader"
          , "Exception", "See Exception", LoggingServices.MAJOR, exception);
      exception.printStackTrace();
      throw exception;
    }
  }

  /**
   * This method is used to search a Transaction on the basis of store id
   * and for a specific date range
   * @param storeId String
   * @param beginDate Date
   * @param endDate Date
   * @return TransactionHeader[]
   * @throws Exception
   */
  public TransactionHeader[] findByDate(String storeId, Date beginDate, Date endDate)
      throws java.lang.Exception {
    try {
      //Date begin = DateUtil.getBeginingOfDay(beginDate);
      //Date end = DateUtil.getEndOfDay(endDate);
      return transactionHeaderDAO.selectByStoreIdAndDates(storeId, beginDate, endDate);
    } catch (Exception exception) {
      LoggingServices.getCurrent().logMsg(this.getClass().getName(), "findByDate", "Exception"
          , "See Exception", LoggingServices.MAJOR, exception);
      exception.printStackTrace();
      throw exception;
    }
  }

  /**
   * This method is used to search a Transaction on the basis of store id
   * and for a specific date range
   * @param storeId String
   * @param beginDate Date
   * @param endDate Date
   * @return TransactionHeader[]
   * @throws Exception
   */
  public TransactionHeader[] findByShippingRequested(String storeId, Date beginDate, Date endDate)
      throws Exception {
    try {
      //Date begin = DateUtil.getBeginingOfDay(aBeginDate);
      //Date end = DateUtil.getEndOfDay(anEndDate);
      return transactionHeaderDAO.selectByStoreIdAndDatesAndShippingRequested(storeId, beginDate
          , endDate);
    } catch (Exception exception) {
      LoggingServices.getCurrent().logMsg(this.getClass().getName(), "findByShippingRequested"
          , "Exception", "See Exception", LoggingServices.MAJOR, exception);
      exception.printStackTrace();
      throw exception;
    }
  }

  /**
   *
   * @param adHocQueryConstraints AdHocQueryConstraints
   * @return TransactionHeader[]
   * @throws Exception
   */
  public TransactionHeader[] findByAdHocQueryConstraints(AdHocQueryConstraints
      adHocQueryConstraints)
      throws Exception {
    try {
      if ((adHocQueryConstraints.getTxnBeginDate() == null || adHocQueryConstraints.getTxnEndDate() == null)
          && (adHocQueryConstraints.getTxnBeginTime() == null
          || adHocQueryConstraints.getTxnEndTime() == null)
          && (adHocQueryConstraints.getAtLeastAmount() == null
          || adHocQueryConstraints.getNotMoreThanAmount() == null)
          && (adHocQueryConstraints.getPaymentTypes() == null
          || adHocQueryConstraints.getPaymentTypes().length == 0)
          && (adHocQueryConstraints.getOperators() == null
          || adHocQueryConstraints.getOperators().length == 0)
          && (adHocQueryConstraints.getTxnTypes() == null
          || adHocQueryConstraints.getTxnTypes().length == 0)
          && (adHocQueryConstraints.getDiscounts() == null
          || adHocQueryConstraints.getDiscounts().length == 0)
          && (adHocQueryConstraints.getItems() == null
          || adHocQueryConstraints.getItems().length == 0)
          && (adHocQueryConstraints.getRegisters() == null
          || adHocQueryConstraints.getRegisters().length == 0)
          && (adHocQueryConstraints.getCustomers() == null
          || adHocQueryConstraints.getCustomers().length == 0)
          && (adHocQueryConstraints.getStores() == null
          || adHocQueryConstraints.getStores().length == 0)
          && (adHocQueryConstraints.getConsultants() == null
          || adHocQueryConstraints.getConsultants().length == 0)
          && (adHocQueryConstraints.getSearchForHavingShippingRequests() == null))
        throw new Exception("AdHocQueryConstraints do not contain any constraint.");
      TransactionHeader[] transactionHeaders = transactionHeaderDAO.selectByAdHocQueryConstraints(
          adHocQueryConstraints);
      //test for time
      if (adHocQueryConstraints.getTxnBeginTime() != null && adHocQueryConstraints.getTxnEndTime() != null) {
        List headers = new ArrayList();
        //time passing with hr and min only, need to set the seconds and millis
        Calendar calendarBegin = Calendar.getInstance();
        calendarBegin.setTime(adHocQueryConstraints.getTxnBeginTime());
        calendarBegin.set(Calendar.SECOND, 0);
        calendarBegin.set(Calendar.MILLISECOND, 0);
        Calendar calendarEnd = Calendar.getInstance();
        calendarEnd.setTime(adHocQueryConstraints.getTxnEndTime());
        calendarEnd.set(Calendar.SECOND, 59);
        calendarEnd.set(Calendar.MILLISECOND, 999);
        for (int i = 0; i < transactionHeaders.length; i++)
          if (DateUtil.areTimesInOrder(calendarBegin.getTime()
              , transactionHeaders[i].getProcessDate(), calendarEnd.getTime()))
            headers.add(transactionHeaders[i]);
        return (TransactionHeader[])headers.toArray(new CMSTransactionHeader[0]);
      }
      return transactionHeaders;
    } catch (Exception exception) {
      LoggingServices.getCurrent().logMsg(this.getClass().getName(), "findByAdHocQueryConstraints"
          , "Exception", "See Exception", LoggingServices.MAJOR, exception);
      exception.printStackTrace();
      throw exception;
    }
  }

  /**
   * This method is used to return the payment summaries for the specified
   * store for on specified date.
   *
   * @param storeId The store to search for.
   * @param date The date on which to search.
   * @return An array of the payment summaries.
   * @throws Exception
   */
  public PaymentSummary[] getStorePaymentSummary(String storeId, Date date)
      throws Exception {
    try {
      return this.paymentSummaryDAO.selectByDateStoreId(date, storeId);
    } catch (Exception exception) {
      LoggingServices.getCurrent().logMsg(this.getClass().getName(), "getStorePaymentSummary"
          , "Exception", "See Exception", LoggingServices.MAJOR, exception);
      exception.printStackTrace();
      throw exception;
    }
  }

  /**
   * This method is used to return the transaction type summaries for the
   * specified store on the specified date.
   *
   * @param storeId The store to search for.
   * @param date The date on which to search.
   * @return An array of the transaction type summaries.
   * @throws Exception
   */
  public TxnTypeSummary[] getStoreTxnTypeSummary(String storeId, Date date)
      throws Exception {
    try {
      return this.txnTypeSummaryDAO.selectByDateStoreId(date, storeId);
    } catch (Exception exception) {
      LoggingServices.getCurrent().logMsg(this.getClass().getName(), "getStoreTxnTypeSummary[1]"
          , "Exception", "See Exception", LoggingServices.MAJOR, exception);
      exception.printStackTrace();
      throw exception;
    }
  }

  /**
   * This method is used to return the transaction type summaries for the
   * specified store on the specified date by the specified operator.
   *
   * @param storeId The store to search for.
   * @param date The date on which to search.
   * @return An array of the transaction type summaries.
   * @throw Exception
   */
  public TxnTypeSummary[] getStoreTxnTypeSummary(String storeId, Date date, String operatorId)
      throws Exception {
    try {
      return this.txnTypeSummaryDAO.selectByDateStoreIdEmployeeId(date, storeId, operatorId);
    } catch (Exception exception) {
      LoggingServices.getCurrent().logMsg(this.getClass().getName(), "getStoreTxnTypeSummary[2]"
          , "Exception", "See Exception", LoggingServices.MAJOR, exception);
      exception.printStackTrace();
      throw exception;
    }
  }

  /**
   * This method is used to return the sales summaries for the specified
   * store for on specified date.
   *
   * @param storeId The store to search for.
   * @param date The date on which to search.
   * @return An array of the sales summaries.
   * @throw Exception
   */
  public SalesSummary[] getStoreSalesSummary(String storeId, Date date)
      throws Exception {
    try {
      return this.salesSummaryDAO.selectByDateStoreId(date, storeId);
    } catch (Exception exception) {
      LoggingServices.getCurrent().logMsg(this.getClass().getName(), "getStoreSalesSummary[1]"
          , "Exception", "See Exception", LoggingServices.MAJOR, exception);
      exception.printStackTrace();
      throw exception;
    }
  }

  /**
   * This method is used to return the sales summaries for the specified
   * store for between the specified dates (inclusive).
   * @param storeId The store to search for.
   * @param from The date from which to search.
   * @param to The date up to which to search.
   * @return An array of the sales summaries.
   * @throw Exception
   */
  public SalesSummary[] getStoreSalesSummary(String storeId, Date from, Date to)
      throws Exception {
    try {
      return this.salesSummaryDAO.selectByDateStoreId(from, to, storeId);
    } catch (Exception exception) {
      LoggingServices.getCurrent().logMsg(this.getClass().getName(), "getStoreSalesSummary[2]"
          , "Exception", "See Exception", LoggingServices.MAJOR, exception);
      exception.printStackTrace();
      throw exception;
    }
  }

  /**
   * This method is used to persist whole transaction
   * @param paymentTransaction PaymentTransaction
   * @return boolean
   * @throws Exception
   */
  public boolean submit(PaymentTransaction paymentTransaction)
      throws java.lang.Exception {
	    paymentTransaction.doSetPostDate(new java.util.Date());
    if (paymentTransaction instanceof CMSCompositePOSTransaction) {
      FiscalDocument fiscalDocument = ((CMSCompositePOSTransaction)paymentTransaction).
          getCurrFiscalDocument();
      if (fiscalDocument != null)return submitFiscalDocument(fiscalDocument);
    }

    writeXMLToFile(paymentTransaction);
    try {
      //long startTime = System.currentTimeMillis();
      updateOfflinePayment(paymentTransaction);
      List statements = new ArrayList();
      statements.addAll(Arrays.asList(persistCustomer(paymentTransaction)));
      statements.addAll(Arrays.asList(persistTransaction(paymentTransaction)));
      
//      statements.addAll(Arrays.asList(persistNewReservation(paymentTransaction)));
      statements.addAll(Arrays.asList(updateRedeemableIssued(paymentTransaction)));
      statements.addAll(Arrays.asList(persistNewLayaway(paymentTransaction)));
      statements.addAll(Arrays.asList(persistRedeemable(paymentTransaction)));
      statements.addAll(Arrays.asList(persistNewDueBillIssue(paymentTransaction)));
      statements.addAll(Arrays.asList(addLayawayPaymentTransaction(paymentTransaction)));
      statements.addAll(Arrays.asList(updateLayawayForRTS(paymentTransaction)));
      statements.addAll(Arrays.asList(undoForVoidTransaciton(paymentTransaction)));
      statements.addAll(Arrays.asList(addToEmployeeSale(paymentTransaction)));
      statements.addAll(Arrays.asList(updateRewardCards(paymentTransaction)));
      statements.addAll(Arrays.asList(persistCustDepositHistory(paymentTransaction)));
      statements.addAll(Arrays.asList(persistCustCreditHistory(paymentTransaction)));
      
      //
      // Added by Satin for new Coupon Management PCR.
      // added to update coupon used flag
      statements.addAll(Arrays.asList(updateCouponUsedFlag(paymentTransaction)));
      System.out.println("Ruchi inside the CMSTransactionPOSJDBCServices  just before :");
      statements.addAll(Arrays.asList(armStgTxnHdrDAO.getStgTxnHeaderInsertSQL(paymentTransaction)));
      //Fix for 1605: Customer Purchase History - This is not being updated. in customer management      
      statements.addAll(Arrays.asList(addToPurchaseHistory(paymentTransaction)));
      if (paymentTransaction instanceof CMSCompositePOSTransaction) {
        CMSCompositePOSTransaction newRsvOpenTxn = ((CMSCompositePOSTransaction)paymentTransaction).
            getNewReservationOpenTxn();
        if (newRsvOpenTxn != null) {
          statements.addAll(Arrays.asList(armStgTxnHdrDAO.getStgTxnHeaderInsertSQL((newRsvOpenTxn))));
        }
      }
      transactionDAO.execute((ParametricStatement[])statements.toArray(new ParametricStatement[0]));
      //System.out.println("Time to presist transaction " + paymentTransaction.getId() + ": " + (System.currentTimeMillis() - startTime) + "ms");
      return true;
    } catch (Exception exception) {
      LoggingServices.getCurrent().logMsg(this.getClass().getName(), "submit", "Exception"
          , "See Exception", LoggingServices.MAJOR, exception);
      exception.printStackTrace();
    //Vivek Mishra : Added to fix broken transaction issue due to corrupted NOF data 30-NOV-2016
      boolean isRepostable = false;
      if((paymentTransaction.getStore().getCountry().equals("USA") || paymentTransaction.getStore().getCountry().equals("CAN")) && paymentTransaction instanceof CMSCompositePOSTransaction){
		POSLineItem[] items = ((CMSCompositePOSTransaction)paymentTransaction).getLineItemsArray();
		ConfigMgr mgr = new ConfigMgr("item.cfg");
		String nofId = mgr.getString("NOTONFILE.BASE_ITEM");
		CMSItem cmsItem = null;
		for(POSLineItem item : items)
		{
			if(nofId.equals(null))
			{
				break;
			}
			//Vivek Mishra : Added null check for item.doGetMiscItemDescription() to overcome null pointer issue for Misc items other than NOF 16-DEC-2016
			if(item.getItem() instanceof CMSItem && item.isMiscItem() && item.doGetMiscItemDescription()!=null && item.doGetMiscItemDescription().equalsIgnoreCase("item not on file"))
			{
				cmsItem = (CMSItem)(item.getItem());
				if(!(cmsItem.getId().equals(nofId)))
				{
					cmsItem.setId(nofId);
					cmsItem.setBarCode(nofId);
					cmsItem.setStoreId(paymentTransaction.getStore().getId());
					isRepostable = true;
				}
			}
			else 
			{
				continue;
			}
		}
      }
      if(isRepostable)
      {
    	  isRepostable = false;
    	  return submit(paymentTransaction);
      }
//Ends here 29-NOV-2016
      return false;
    } catch (Throwable throwable) {
      LoggingServices.getCurrent().logMsg(this.getClass().getName(), "submit", "Throwable"
          , "See Exception", LoggingServices.MAJOR, new Exception(throwable.getMessage()));
      return false;
    }
  }
  
/**
   * This method is used to persist FiscalDocument
   * @param fiscalDocument FiscalDocument
   * @throws Exception
   * @return boolean
   */
  public boolean submitFiscalDocument(FiscalDocument fiscalDocument)
      throws java.lang.Exception {
    try {
      List statements = new ArrayList();
      statements.addAll(Arrays.asList(armFiscalDocumentDAO.getInsertSQL(fiscalDocument)));
      statements.addAll(Arrays.asList(armStgFiscalDocDAO.getStgFiscalDocInsertSQL(
          fiscalDocument)));
      statements.addAll(Arrays.asList(armStgTxnDtlDAO.getFiscalDocumentInsertSQL(fiscalDocument)));
      armFiscalDocumentDAO.execute((ParametricStatement[])statements.toArray(new
          ParametricStatement[0]));
      return true;
    } catch (Exception exception) {
      LoggingServices.getCurrent().logMsg(this.getClass().getName(), "submitFiscalDocument"
          , "Exception", "See Exception", LoggingServices.MAJOR, exception);
      exception.printStackTrace();
      return false;
    }
  }

  // Determine the type of the offline CMSRedeemable payment
  void updateOfflinePayment(PaymentTransaction txn)
      throws Exception {
    Payment[] pays = txn.getPaymentsArray();
    if (pays != null && pays.length > 0) {
      for (int i = 0; i < pays.length; i++) {
        if (pays[i] instanceof CMSRedeemable) {
          // Off-line Gift Card/Credit Memo redemption
          String redeemId = ((CMSRedeemable)pays[i]).getControlNum();
          Redeemable redeemable = redeemableIssueDAO.selectRedeemableById(redeemId);
          if (redeemable != null) {
            if (redeemable instanceof CMSStoreValueCard) {
              pays[i].setTransactionPaymentName("STORE_VALUE_CARD");
              ((CMSRedeemable)pays[i]).doSetCustomerId(((CMSStoreValueCard)redeemable).
                  getCustomerId());
            } else if (redeemable instanceof CMSDueBill) {
              pays[i].setTransactionPaymentName("CREDIT_MEMO");
              ((CMSRedeemable)pays[i]).doSetCustomerId(((CMSDueBill)redeemable).getCustomerId());
            }
            ((CMSRedeemable)pays[i]).setIssueAmount(redeemable.getIssueAmount());
            ((CMSRedeemable)pays[i]).setBalance(redeemable.getRemainingBalance());
          }
        }
      }
    }
  }

  /**
   * This method is used to persist customer
   * @param paymentTransaction PaymentTransaction
   * @return ParametricStatement[]
   * @throws SQLException
   */
  private ParametricStatement[] persistCustomer(PaymentTransaction paymentTransaction)
      throws SQLException {
    List statements = new ArrayList();
    Customer customer = null;
    if (paymentTransaction instanceof CompositePOSTransaction)
      customer = ((CompositePOSTransaction)paymentTransaction).getCustomer();
    else if (paymentTransaction instanceof RedeemableBuyBackTransaction)
      customer = ((CMSRedeemableBuyBackTransaction)paymentTransaction).getCustomer();
    else if (paymentTransaction instanceof CollectionTransaction)
      customer = ((CMSMiscCollection)paymentTransaction).getCustomer();
     
      CMSCustomerMessage custMsg = new CMSCustomerMessage();
      boolean isSearchedById;
      if (customer == null)
        return new ParametricStatement[0];
    else {
      customer.doSetLastName(customer.getLastName().toUpperCase());
      customer.doSetFirstName(customer.getFirstName().toUpperCase());
      // Commented by Deepak as customer is saved from the Customer Management screen directly.
      //if (customer.isNew()) statements.addAll(Arrays.asList(customerDAO.getInsertSQL(customer)));
      //else if (customer.isModified()) statements.addAll(Arrays.asList(customerDAO.getUpdateSQL(customer)));
      // For taking care of off-line scenarios
      if (customer.getId() == null || customer.getId().equals(""))
        statements.addAll(Arrays.asList(customerDAO.getInsertSQL(customer)));
      // For Customer Messages
      custMsg = ((CMSCustomer)(customer)).getCustomerMessage();
      if (custMsg != null && custMsg.getMessageType() != null
          && custMsg.getMessageType().equals("Q")) {
        if (((CMSCustomer)(customer)).getCustomerMessage() != null) {
          custMsg = ((CMSCustomer)customer).getCustomerMessage();
          isSearchedById = custMsg.getisSearchedById();
          if (isSearchedById) {
            statements.addAll(Arrays.asList(customerDAO.getUpdateSQLForCustomerMessage(custMsg)));
          } else if (!isSearchedById) {
            statements.addAll(Arrays.asList(customerDAO.getInsertSQLForMessages(custMsg, customer)));
          }
        }
      }
    }
    return (ParametricStatement[])statements.toArray(new ParametricStatement[0]);
  }

  /**
   * This method is used to persist transaction
   * @param paymentTransaction PaymentTransaction
   * @return ParametricStatement[]
   * @throws SQLException
   */
  private ParametricStatement[] persistTransaction(PaymentTransaction paymentTransaction)
      throws SQLException {
    List statements = new ArrayList();
    CMSItem cmsItem = null;
    statements.addAll(Arrays.asList(transactionDAO.getInsertSQL(paymentTransaction)));
    if (paymentTransaction instanceof CompositePOSTransaction) {
      CompositePOSTransaction compositePOSTransaction = (CompositePOSTransaction)paymentTransaction;
      //___Tim: Issue 1767: PCR1167 Sku lookup and visibility - logic has been moved into method processNotStoreItems()
      statements.addAll(Arrays.asList(processNotStoreItems(compositePOSTransaction)));
      POSLineItem[] saleLineItems = compositePOSTransaction.getSaleLineItemsArray();
      ArrayList itemIdList = new ArrayList(); 
      String homeStoreId = paymentTransaction.getStore().getId();
      //System.out.println("IN PERSIST TRANSACTION");
      //System.out.println("saleLineItems length: " + saleLineItems.length);
      for (int i = 0; i < saleLineItems.length; i++) {
    	Item saleLineItem = saleLineItems[i].getItem();    	
    	//PCR1167 Sku lookup and visibility
/*    	if (saleLineItem instanceof CMSItem) {
    		//System.out.println(saleLineItem.getId() + " is an instance of CMSItem");
    		cmsItem = (CMSItem) saleLineItem;    		
    		if (!(cmsItem.getStoreId().equals(homeStoreId))) {
    			if ((itemDAO.selectByItemIdAndStoreId(cmsItem.getId(), homeStoreId)) == null) {
    				//System.out.println("INSERT INTO AS_ITM_RTL_STR TABLE");
    				if(!checkIfItemAlreadyExists(itemIdList, cmsItem)){
	    				itemIdList.add(cmsItem.getId());
	    				statements.addAll(Arrays.asList(itemDAO.getInsertSQLForItemRetailStore(cmsItem, homeStoreId)));
    				}
    			}
    		}
    	}*/
    	
        for (int j = 0; j < saleLineItems[i].getLineItemDetailsArray().length; j++) {
          POSLineItemDetail saleDetail = saleLineItems[i].getLineItemDetailsArray()[j];
          CMSPresaleLineItemDetail preSaleDetail = ((CMSSaleLineItemDetail)saleDetail).
              getPresaleLineItemDetail();
          if (preSaleDetail != null)
            statements.addAll(Arrays.asList(posLineItemDetailDAO.
                getUpdateProcessedPreSaleLineItemDetailSQL(preSaleDetail, saleDetail)));
          CMSConsignmentLineItemDetail csgnLineDetail = ((CMSSaleLineItemDetail)saleDetail).
              getConsignmentLineItemDetail();
          if (csgnLineDetail != null)
            statements.addAll(Arrays.asList(posLineItemDetailDAO.
                getUpdateProcessedConsignmentLineItemDetailSQL(csgnLineDetail, saleDetail)));
          CMSReservationLineItemDetail rsvLineDetail = ((CMSSaleLineItemDetail)saleDetail).
              getReservationLineItemDetail();
          if (rsvLineDetail != null)
            statements.addAll(Arrays.asList(posLineItemDetailDAO.
                getUpdateProcessedReservationLineItemDetailSQL(rsvLineDetail, saleDetail)));
        }
      }
      POSLineItem[] returnLineItems = compositePOSTransaction.getReturnLineItemsArray();
      for (int i = 0; i < returnLineItems.length; i++) {
    	Item returnLineItem = returnLineItems[i].getItem();    	
    	//PCR1167 Sku lookup and visibility
    	/*if (returnLineItem instanceof CMSItem) {
    		//System.out.println(returnLineItem.getId() + " is an instance of CMSItem");
    		cmsItem = (CMSItem) returnLineItem;
    		if (!(cmsItem.getStoreId().equals(homeStoreId))) {
    			if ((itemDAO.selectByItemIdAndStoreId(cmsItem.getId(), homeStoreId)) == null) {
    				//System.out.println("INSERT INTO AS_ITM_RTL_STR TABLE");
    				if(!checkIfItemAlreadyExists(itemIdList, cmsItem)){
	    				itemIdList.add(cmsItem.getId());
	    				statements.addAll(Arrays.asList(itemDAO.getInsertSQLForItemRetailStore(cmsItem, homeStoreId)));
    				}
    			}
    			
    			//insert into ARM_ITM_HIST
    			//statements.addAll(Arrays.asList(itemDAO.getInsertSQLForItemHistory(cmsItem, homeStoreId)));
    		}
    	}*/
    	  
        for (int j = 0; j < returnLineItems[i].getLineItemDetailsArray().length; j++) {
          POSLineItemDetail returnDetail = returnLineItems[i].getLineItemDetailsArray()[j];
          CMSPresaleLineItemDetail preSaleDetail = ((CMSReturnLineItemDetail)returnDetail).
              getPresaleLineItemDetail();
          if (preSaleDetail != null)
            statements.addAll(Arrays.asList(posLineItemDetailDAO.
                getUpdateProcessedPreSaleLineItemDetailSQL(preSaleDetail, returnDetail)));
          CMSConsignmentLineItemDetail csgnLineDetail = ((CMSReturnLineItemDetail)returnDetail).
              getConsignmentLineItemDetail();
          if (csgnLineDetail != null)
            statements.addAll(Arrays.asList(posLineItemDetailDAO.
                getUpdateProcessedConsignmentLineItemDetailSQL(csgnLineDetail, returnDetail)));
          CMSReservationLineItemDetail rsvLineDetail = ((CMSReturnLineItemDetail)returnDetail).
              getReservationLineItemDetail();
          if (rsvLineDetail != null)
            statements.addAll(Arrays.asList(posLineItemDetailDAO.
                getUpdateProcessedReservationLineItemDetailSQL(rsvLineDetail, returnDetail)));
          SaleLineItemDetail saleDetail = ((ReturnLineItemDetail)returnDetail).
              getSaleLineItemDetail();
          if (((ReturnLineItem)returnLineItems[i]).isMiscReturn())
            continue;
          statements.addAll(Arrays.asList(posLineItemDetailDAO.
              getUpdateReturningSaleLineItemDetailSQL(saleDetail
              , (ReturnLineItemDetail)returnDetail)));
        }
      }
      
      //insert or update into ARM_ITM_SOH for sale or return
      //System.out.println("INSERT/UPDATE ARM_ITM_SOH TABLE");
      statements.addAll(Arrays.asList(armItmSohDAO.createForPaymentTransaction(paymentTransaction)));
    //added by SonaliRaina for duty free management PCR of Europe
      try{
      statements.addAll(Arrays.asList(persistBoardingDetails(paymentTransaction)));
      }
      catch(Exception e){}

    }
    return (ParametricStatement[])statements.toArray(new ParametricStatement[0]);
  }

  private boolean checkIfItemAlreadyExists(ArrayList itemIdList, CMSItem cmsItem) {
	Iterator iterator = itemIdList.iterator();
	while(iterator.hasNext()){
		if(((String)iterator.next()).equals(cmsItem.getId())){
			return true;
		}
	}
	return false;
}

  /**
   *
   * @param paymentTransaction PaymentTransaction
   * @return ParametricStatement[]
   * @throws SQLException
   * @throws CurrencyException
   */
  private ParametricStatement[] persistNewLayaway(PaymentTransaction paymentTransaction)
      throws SQLException, CurrencyException {
    List statements = new ArrayList();
    if (!(paymentTransaction instanceof CompositePOSTransaction))
      return new ParametricStatement[0];
    Layaway layaway = createLayaway((CompositePOSTransaction)paymentTransaction);
    if (layaway != null)
      statements.addAll(Arrays.asList(layawayDAO.getInsertSQL(layaway)));
    return (ParametricStatement[])statements.toArray(new ParametricStatement[0]);
  }

  /**
   *
   * @param paymentTransaction PaymentTransaction
   * @return ParametricStatement[]
   * @throws SQLException
   * @throws CurrencyException
   */
//  private ParametricStatement[] persistNewReservation(PaymentTransaction paymentTransaction)
//      throws SQLException, CurrencyException {
//    List statements = new ArrayList();
//    if (!(paymentTransaction instanceof CompositePOSTransaction))
//      return new ParametricStatement[0];
//    CMSCompositePOSTransaction compositeTxn = ((CMSCompositePOSTransaction)paymentTransaction).
//        getNewReservationOpenTxn();
//    if (compositeTxn != null) {
//      statements.addAll(Arrays.asList(transactionDAO.getInsertSQL(compositeTxn)));
//    }
//    return (ParametricStatement[])statements.toArray(new ParametricStatement[0]);
//  }

  /**
   * put your documentation comment here
   * @param paymentTransaction
   * @return
   * @exception SQLException, CurrencyException
   */
  private ParametricStatement[] persistCustDepositHistory(PaymentTransaction paymentTransaction)
      throws SQLException, CurrencyException {
    List statements = new ArrayList();
    if (paymentTransaction instanceof CompositePOSTransaction) {
      POSLineItem[] lineItems = ((CMSCompositePOSTransaction)paymentTransaction).
          getDepositLineItems();
      //Currency depositAmount = new ArmCurrency(0.0);
      for (int index = 0; index < lineItems.length; index++) {
        DepositHistory hist = null;
        POSLineItem lineItem = lineItems[index];
        hist = new DepositHistory();
        hist.setassoc(((CMSCompositePOSTransaction)paymentTransaction).getConsultant().getId());
        hist.setCustomer((CMSCustomer)((CMSCompositePOSTransaction)paymentTransaction).getCustomer());
        hist.setStoreID(((CMSCompositePOSTransaction)paymentTransaction).getStore().getId());
        hist.setTransactionDate(paymentTransaction.getProcessDate());
        hist.setTransactionId(paymentTransaction.getId());
        if (lineItem instanceof CMSSaleLineItem) {
          hist.setTransactionType(DepositHistory.OPEN_DEPOSIT_TYPE);
          hist.setamount(lineItem.getExtendedRetailAmount());
        } else if (lineItem instanceof CMSReturnLineItem) {
          hist.setTransactionType(DepositHistory.CLOSE_DEPOSIT_TYPE);
          hist.setamount(lineItem.getExtendedRetailAmount().multiply( -1));
        } else
          continue;
        //depositAmount = depositAmount.add(hist.getamount());
        statements.add(customerDAO.getInsertDepositHistorySQL(hist));
      }
//      if (((CMSCompositePOSTransaction)paymentTransaction).getCustomer() != null
//          && depositAmount.getDoubleValue() != 0.0)
//        statements.add(customerDAO.getUpdateDepositSQL((CMSCustomer)((CMSCompositePOSTransaction)
//            paymentTransaction).getCustomer(), depositAmount));
      return (ParametricStatement[])statements.toArray(new ParametricStatement[0]);
    } else if (paymentTransaction instanceof CMSMiscCollection
        && ("OPEN_DEPOSIT").equals(((CMSMiscCollection)paymentTransaction).getType())) {
      DepositHistory hist = null;
      hist = new DepositHistory();
      hist.setassoc(((CMSMiscCollection)paymentTransaction).getTheOperator().getId());
      hist.setCustomer((CMSCustomer)((CMSMiscCollection)paymentTransaction).getCustomer());
      hist.setStoreID(((CMSMiscCollection)paymentTransaction).getStore().getId());
      hist.setTransactionDate(paymentTransaction.getProcessDate());
      hist.setTransactionId(paymentTransaction.getId());
      hist.setTransactionType(DepositHistory.OPEN_DEPOSIT_TYPE);
      hist.setamount(((CMSMiscCollection)paymentTransaction).getAmount());
      statements.add(customerDAO.getInsertDepositHistorySQL(hist));
//      if (((CMSMiscCollection)paymentTransaction).getCustomer() != null
//          && hist.getamount().doubleValue() != 0.0)
//        statements.add(customerDAO.getUpdateDepositSQL((CMSCustomer)((CMSMiscCollection)
//            paymentTransaction).getCustomer(), hist.getamount()));
      return (ParametricStatement[])statements.toArray(new ParametricStatement[0]);
    } else if (paymentTransaction instanceof CMSMiscPaidOut
        && ("CLOSE_DEPOSIT").equals(((CMSMiscPaidOut)paymentTransaction).getType())) {
      DepositHistory hist = null;
      hist = new DepositHistory();
      hist.setassoc(((CMSMiscPaidOut)paymentTransaction).getTheOperator().getId());
      hist.setCustomer((CMSCustomer)((CMSMiscPaidOut)paymentTransaction).getCustomer());
      hist.setStoreID(((CMSMiscPaidOut)paymentTransaction).getStore().getId());
      hist.setTransactionDate(paymentTransaction.getProcessDate());
      hist.setTransactionId(paymentTransaction.getId());
      hist.setTransactionType(DepositHistory.CLOSE_DEPOSIT_TYPE);
      hist.setamount(((CMSMiscPaidOut)paymentTransaction).getAmount());
      statements.add(customerDAO.getInsertDepositHistorySQL(hist));
//      if (((CMSMiscPaidOut)paymentTransaction).getCustomer() != null
//          && hist.getamount().doubleValue() != 0.0)
//        statements.add(customerDAO.getUpdateDepositSQL((CMSCustomer)((CMSMiscPaidOut)
//            paymentTransaction).getCustomer(), hist.getamount()));
      return (ParametricStatement[])statements.toArray(new ParametricStatement[0]);
    }
    return new ParametricStatement[0];
  }

  /**
   *
   * @param paymentTransaction PaymentTransaction
   * @return ParametricStatement[]
   * @throws Exception
   */
  private ParametricStatement[] addLayawayPaymentTransaction(PaymentTransaction paymentTransaction)
      throws java.lang.Exception {
    List statements = new ArrayList();
    if (!(paymentTransaction instanceof LayawayPaymentTransaction))
      return new ParametricStatement[0];
    LayawayPaymentTransaction layawayPaymentTransaction = (LayawayPaymentTransaction)
        paymentTransaction;
    Layaway layaway = layawayDAO.selectById(layawayPaymentTransaction.getOriginalLayawayId());
    ArmCurrency updatedAmountDue = layaway.getCurrentAmountDue().subtract(layawayPaymentTransaction.
        getAmount());
    layaway.doSetCurrentAmountDue(updatedAmountDue);
    Date date = layawayPaymentTransaction.getPostDate();
    ArmCurrency amount = layawayPaymentTransaction.getAmount();
    String storeId = layawayPaymentTransaction.getStore().getId();
    String txnId = layawayPaymentTransaction.getId();
    LayawayPaymentInfo layawayPaymentInfo = new LayawayPaymentInfo(date, amount, storeId, txnId);
    layaway.doAddPayment(layawayPaymentInfo);
    statements.addAll(Arrays.asList(layawayDAO.getUpdateSQL(layaway)));
    return (ParametricStatement[])statements.toArray(new ParametricStatement[0]);
  }

  /**
   *
   * @param paymentTransaction PaymentTransaction
   * @return ParametricStatement[]
   * @throws Exception
   */
  private ParametricStatement[] updateLayawayForRTS(PaymentTransaction paymentTransaction)
      throws java.lang.Exception {
    List statements = new ArrayList();
    if (!(paymentTransaction instanceof LayawayRTSTransaction))
      return new ParametricStatement[0];
    LayawayRTSTransaction layawayRTSTransaction = (LayawayRTSTransaction)paymentTransaction;
    Layaway originalLayaway = layawayDAO.selectById(layawayRTSTransaction.getOriginalLayaway().
        getId());
    originalLayaway.setRTS();
    statements.addAll(Arrays.asList(layawayDAO.getUpdateSQL(originalLayaway)));
    return (ParametricStatement[])statements.toArray(new ParametricStatement[0]);
  }

  /**
   * This method is used to update redeemable issue
   * @param paymentTransaction PaymentTransaction
   * @return ParametricStatement[]
   * @throws Exception
   */
  private ParametricStatement[] updateRedeemableIssued(PaymentTransaction paymentTransaction)
      throws Exception {
    List statements = new ArrayList();
    Payment[] pays = paymentTransaction.getPaymentsArray();
    Payment payment = null;
    for (int i = 0; i < pays.length; i++) {
      payment = (Payment)pays[i];
      if ((payment != null) && (payment instanceof Redeemable) && (payment instanceof DueBillIssue == false)) {
        Redeemable redeemablePayment = (Redeemable)payment;
        if (redeemablePayment instanceof DueBill)
          redeemablePayment.setAmount(redeemablePayment.getAmount().absoluteValue());
        RedeemableHist hist = new RedeemableHist();
        hist.setDateUsed(new Date());
        hist.setTransactionsIdUsed(paymentTransaction.getId());
        hist.setAmountUsed(redeemablePayment.getAmount());
        Redeemable originalRedeemableIssue = redeemableIssueDAO.selectRedeemableById(
            redeemablePayment.getId());
        //          Redeemable originalRedeemableIssue = redeemableIssueDAO.
        //              selectRedeemableById(redeemablePayment.getType(), redeemablePayment.getId());
        originalRedeemableIssue.addRedemption(hist);
        statements.addAll(Arrays.asList(redeemableIssueDAO.getUpdateSQL(originalRedeemableIssue)));
      }
    }
    //ks: Persist House account for Paid In
    if (paymentTransaction instanceof CMSMiscCollection) {
      if (((CMSMiscCollection)paymentTransaction).getRedeemable() != null) {
        Redeemable houseAccount = (Redeemable)((CMSMiscCollection)paymentTransaction).getRedeemable();
        statements.addAll(Arrays.asList(redeemableIssueDAO.getUpdateSQL(houseAccount)));
      }
    }
    if (paymentTransaction instanceof RedeemableBuyBackTransaction) {
      RedeemableHist buyBackHist = new RedeemableHist();
      buyBackHist.setDateUsed(new Date());
      buyBackHist.setTransactionsIdUsed(paymentTransaction.getId());
      RedeemableBuyBackTransaction redeemableBuyBackTransaction = (RedeemableBuyBackTransaction)
          paymentTransaction;
      buyBackHist.setAmountUsed(redeemableBuyBackTransaction.getAmount().multiply( -1));
      Redeemable redeemable = redeemableBuyBackTransaction.getRedeemable();
      redeemable.addRedemption(buyBackHist);
      statements.addAll(Arrays.asList(redeemableIssueDAO.getUpdateSQL(redeemable)));
    }
    //ks: Check for existing houseaccount. If new create one or else update existing one
    return (ParametricStatement[])statements.toArray(new ParametricStatement[0]);
  }

 
  /**
   * Added by Satin for New Coupon Management PCR
   * This method updates coupon based on paymentTransaction.
   * @param PaymentTransaction paymentTransaction
   * @return ParametricStatement
   * @throws Exception
   */
  private ParametricStatement[] updateCouponUsedFlag(PaymentTransaction paymentTransaction)
	      throws Exception{

	    List statements = new ArrayList();
	    Payment[] pays = paymentTransaction.getPaymentsArray();
	    Payment payment = null;
		      for (int i = 0; i < pays.length; i++){
		        if ((pays[i] instanceof CMSCoupon)) {
		          
		          CMSCoupon couponPayment = (CMSCoupon) pays[i];
		          statements.addAll(Arrays.asList(couponDAO.getUpdateSQL("1", couponPayment.getCouponCode())));
		         
		        }
		      }
	      
	      return (ParametricStatement[])statements.toArray(new ParametricStatement[0]);
	    }
  
  
  
  
  
  /**
   * This method is used to persist redeemable payment
   * @param paymentTransaction PaymentTransaction
   * @return ParametricStatement[]
   * @throws SQLException
   * @throws CurrencyException
   */
  private ParametricStatement[] persistRedeemable(PaymentTransaction paymentTransaction)
      throws SQLException, CurrencyException {
    List statements = new ArrayList();
    if (paymentTransaction instanceof CompositePOSTransaction) {
      Redeemable[] redeemables = createRedeemablesSold((CompositePOSTransaction)paymentTransaction);
      for (int i = 0; i < redeemables.length; i++) {
        if (redeemables[i].getType().equals(Redeemable.STORE_VALUE_CARD_ADD_TYPE)) {
          String controlNumber = ((StoreValueCard)redeemables[i]).getControlNum();
          Redeemable oldSVC = (StoreValueCard)redeemableIssueDAO.
              selectStoreValueCardByControlNumber(controlNumber);
          ArmCurrency newValue = redeemables[i].getIssueAmount().add(oldSVC.getIssueAmount());
          oldSVC.setIssueAmount(newValue);
          statements.addAll(Arrays.asList(redeemableIssueDAO.getUpdateSQL(oldSVC)));
        } else {
          statements.addAll(Arrays.asList(redeemableIssueDAO.getInsertSQL(redeemables[i])));
        }
      }
    }
    return (ParametricStatement[])statements.toArray(new ParametricStatement[0]);
  }

  /**
   *
   * @param paymentTransaction PaymentTransaction
   * @return ParametricStatement[]
   * @throws SQLException
   */
  private ParametricStatement[] persistNewDueBillIssue(PaymentTransaction paymentTransaction)
      throws SQLException {
    List statements = new ArrayList();
    DueBillIssue dueBillIssue = getNewDueBillIssue(paymentTransaction);
    if (dueBillIssue != null)
      statements.addAll(Arrays.asList(redeemableIssueDAO.getInsertSQL(dueBillIssue)));
    return (ParametricStatement[])statements.toArray(new ParametricStatement[0]);
  }

  /**
   *
   * @param paymentTransaction PaymentTransaction
   * @return ParametricStatement[]
   * @throws SQLException
   * @throws CurrencyException
   */
  private ParametricStatement[] undoForVoidTransaciton(PaymentTransaction paymentTransaction)
      throws SQLException, CurrencyException {
    if (!(paymentTransaction instanceof VoidTransaction))
      return new ParametricStatement[0];
    ITransaction originalTransaction = ((VoidTransaction)paymentTransaction).getOriginalTransaction();
    if (originalTransaction instanceof PaymentTransaction) {
      return undoForVoidTransacitonWithOriginalPaymentTransaction((PaymentTransaction)
          originalTransaction, paymentTransaction);
    }
    /**
     *
     * Implement this!
     *
     else if (originalTransaction instanceof Transfer)
     return undoForVoidTransacitonWithOriginalTransferTransaction((Transfer)originalTransaction);
     */
    else
      return new ParametricStatement[0];
  }

  /**
   *
   * @param originalTransaction PaymentTransaction
   * @return ParametricStatement[]
   * @throws SQLException
   * @throws CurrencyException
   */
  private ParametricStatement[] undoForVoidTransacitonWithOriginalPaymentTransaction(
      PaymentTransaction originalTransaction, PaymentTransaction voidTransaction)
      throws SQLException, CurrencyException {
    List statements = new ArrayList();
    if (originalTransaction instanceof CompositePOSTransaction) {
      //mark layaway as deleted
      Layaway layaway = createLayaway((CompositePOSTransaction)originalTransaction);
      if (layaway != null)
        statements.add(layawayDAO.getUpdateSQLForLogicalDelete(layaway.getId()));
      //mark GiftCert sold as deleted
      Redeemable[] redeemables = createRedeemablesSold((CompositePOSTransaction)originalTransaction);
      for (int i = 0; i < redeemables.length; i++) {
        if (redeemables[i] instanceof GiftCert) {
          statements.add(redeemableIssueDAO.getUpdateGiftCertSQLForLogicalDelete(redeemables[i]));
        } else if (redeemables[i] instanceof StoreValueCard) {
          if (redeemables[i].getType().equals(Redeemable.STORE_VALUE_CARD_TYPE)) {
            //statements.add(redeemableIssueDAO.getUpdateStoreValueCardSQLForLogicalDelete(redeemables[i]));
            statements.addAll(Arrays.asList(redeemableIssueDAO.getDeleteSQL(redeemables[i])));
          } else if (redeemables[i].getType().equals(Redeemable.STORE_VALUE_CARD_ADD_TYPE)) {
            String controlNumber = ((StoreValueCard)redeemables[i]).getControlNum();
            Redeemable oldSVC = (StoreValueCard)redeemableIssueDAO.
                selectStoreValueCardByControlNumber(controlNumber);
            ArmCurrency newValue = oldSVC.getIssueAmount().subtract(redeemables[i].getIssueAmount());
            oldSVC.setIssueAmount(newValue);
            statements.addAll(Arrays.asList(redeemableIssueDAO.getUpdateSQL(oldSVC)));
          }
        }
      }
      //change original-sale line item detail to be un-returned
      POSLineItem[] returnLineItems = ((CompositePOSTransaction)originalTransaction).
          getReturnLineItemsArray();
      String returnTransactionId = originalTransaction.getId();
      for (int i = 0; i < returnLineItems.length; i++) {
        int returnLineItemSeqNum = returnLineItems[i].getSequenceNumber();
        POSLineItemDetail[] returnLineItemDetails = returnLineItems[i].getLineItemDetailsArray();
        for (int j = 0; j < returnLineItemDetails.length; j++) {
          CMSReturnLineItemDetail returnLineItemDetail = (CMSReturnLineItemDetail)
              returnLineItemDetails[j];
          if (returnLineItemDetail.getPresaleLineItemDetail() != null)
            statements.addAll(Arrays.asList(posLineItemDetailDAO.getUndoPresale(
                returnLineItemDetail
                , returnLineItemDetail.getPresaleLineItemDetail())));
          else if (returnLineItemDetail.getConsignmentLineItemDetail() != null)
            statements.addAll(Arrays.asList(posLineItemDetailDAO.getUndoConsignment(
                returnLineItemDetail, returnLineItemDetail.getConsignmentLineItemDetail())));
          else if (returnLineItemDetail.getReservationLineItemDetail() != null)
            statements.addAll(Arrays.asList(posLineItemDetailDAO.getUndoReservation(
                returnLineItemDetail, returnLineItemDetail.getReservationLineItemDetail())));
          else {
            int returnLineItemDetailSeqNum = returnLineItemDetails[j].getSequenceNumber();
            statements.addAll(Arrays.asList(posLineItemDetailDAO.getUndoReturn(returnTransactionId
                , returnLineItemSeqNum, returnLineItemDetailSeqNum)));
          }
        }
      }
      //change original-presale line item detail to be un-returned
      POSLineItem[] saleLineItems = ((CompositePOSTransaction)originalTransaction).
          getSaleLineItemsArray();
      String saleTransactionId = originalTransaction.getId();
      for (int i = 0; i < saleLineItems.length; i++) {
        POSLineItemDetail[] saleLineItemDetails = saleLineItems[i].getLineItemDetailsArray();
        for (int j = 0; j < saleLineItemDetails.length; j++) {
          CMSSaleLineItemDetail saleLineItemDetail = (CMSSaleLineItemDetail)saleLineItemDetails[j];
          if (saleLineItemDetail.getPresaleLineItemDetail() != null)
            statements.addAll(Arrays.asList(posLineItemDetailDAO.getUndoPresale(saleLineItemDetail
                , saleLineItemDetail.getPresaleLineItemDetail())));
          else if (saleLineItemDetail.getConsignmentLineItemDetail() != null)
            statements.addAll(Arrays.asList(posLineItemDetailDAO.getUndoConsignment(
                saleLineItemDetail, saleLineItemDetail.getConsignmentLineItemDetail())));
          else if (saleLineItemDetail.getReservationLineItemDetail() != null)
            statements.addAll(Arrays.asList(posLineItemDetailDAO.getUndoReservation(
                saleLineItemDetail, saleLineItemDetail.getReservationLineItemDetail())));
          else {}
        }
      }

      if (((CompositePOSTransaction)originalTransaction).getCustomer() != null){
        String oriTxnId = originalTransaction.getId();
        String customerId = ((CompositePOSTransaction)originalTransaction).getCustomer().getId();
        DepositHistory[] depositHistory = customerDAO.getDepositHistory(customerId, oriTxnId);
        if (depositHistory != null && depositHistory.length >0){
          statements.add(customerDAO.getUpdateDepositHistorySQLForDelete(depositHistory[0]));
//          if (depositHistory[0].getTransactionType().equalsIgnoreCase("OPEN"))
            //statements.add(customerDAO.getUpdateDepositSQL((CMSCustomer)((CompositePOSTransaction)originalTransaction).getCustomer(), depositHistory[0].getamount().multiply(-1)));
//          else if (depositHistory[0].getTransactionType().equalsIgnoreCase("CLOSE") )
//            statements.add(customerDAO.getUpdateDepositSQL((CMSCustomer)((CompositePOSTransaction)originalTransaction).getCustomer(), depositHistory[0].getamount()));
        }
      }
    } //endif CompositePOSTransaction
    if (originalTransaction instanceof RewardTransaction) {
      RewardCard rewardCard = ((RewardTransaction)originalTransaction).getRewardCard();
      if (rewardCard != null)
        statements.addAll(Arrays.asList(redeemableIssueDAO.getDeleteSQL(rewardCard)));
      //statements.addAll(Arrays.asList(redeemableIssueDAO.getUpdateRewardCardSQLForLogicalDelete(rewardCard)));
      else
        throw new SQLException(
            "ERROR: Could not find original Reward card from original transaction.");
    }
    // Adjust loyalty points !!
    LoyaltyHistory[] loyaltyHists = loyaltyDAO.selectHistoryByTransactionId(originalTransaction.
        getId());
    if (loyaltyHists != null && loyaltyHists.length > 0) {
      // Void the points by updating the loyalty card and inserting a reverse record in the Loyalty history
      for (int i = 0; i < loyaltyHists.length; i++) {
        LoyaltyHistory orighist = loyaltyHists[i];
        Loyalty loyaltyCard = loyaltyDAO.selectById(orighist.getLoyaltyNumber());
        double pointsEarned = orighist.getPointEarned() * ( -1);
        if (originalTransaction instanceof RewardTransaction && pointsEarned < 0) { //to take care of existing data inconsitancy
          pointsEarned = pointsEarned * ( -1);
        }
        double lifeTimeBalance = loyaltyCard.getLifeTimeBalance();
        if (pointsEarned < 0)
          lifeTimeBalance = lifeTimeBalance + pointsEarned;
        if(loyaltyCard.isYearlyComputed()){
	        statements.add(loyaltyDAO.getUpdatePointsSQL(loyaltyCard.getLoyaltyNumber()
	            , loyaltyCard.getCurrBalance() + pointsEarned, lifeTimeBalance, loyaltyCard.getCurrYearBalance() + pointsEarned,loyaltyCard.getLastYearBalance()));
        }else{
        	statements.add(loyaltyDAO.getUpdatePointsSQL(loyaltyCard.getLoyaltyNumber()
    	            , loyaltyCard.getCurrBalance() + pointsEarned, lifeTimeBalance, 0,0));
        }
        LoyaltyHistory hist = new LoyaltyHistory();
        hist.setLoyaltyNumber(loyaltyCard.getLoyaltyNumber());
        hist.setTransactionDate(voidTransaction.getSubmitDate());
        hist.setStoreID(voidTransaction.getStore().getId());
        hist.setTransactionId(voidTransaction.getId());
        hist.setPointEarned(orighist.getPointEarned() * ( -1));
        hist.setTransactionType(voidTransaction.getTransactionType());

        double pointsVoided = orighist.getPointEarned() * -1;
        if (pointsVoided > 0) {
            hist.setReasonCode((int)pointsVoided+ " " + "POINTS ADDED FOR LOYALTY CARD"+" : " + loyaltyCard.getLoyaltyNumber());
        } else if (pointsVoided < 0) {
            hist.setReasonCode((int)pointsVoided+ " " + "POINTS SUBTRACTED FOR LOYALTY CARD"+" : " + loyaltyCard.getLoyaltyNumber());
        } else {
        hist.setReasonCode("");
        /*if (originalTransaction instanceof RewardTransaction) {
          hist.setReasonCode("VOIDED - " + ((RewardTransaction)originalTransaction).getComment());
         */
        }
        statements.add(loyaltyDAO.getInsertHistorySQL(hist));
      }
      
      if (originalTransaction instanceof CompositePOSTransaction) {
    	  CMSCompositePOSTransaction  cmsCompositePosTxn = (CMSCompositePOSTransaction)originalTransaction;
    	  Loyalty loyalty = cmsCompositePosTxn.getLoyaltyCard();
  	    double points = cmsCompositePosTxn.getLoyaltyPoints();
  	    double loyaltyUsed = cmsCompositePosTxn.getUsedLoyaltyPoints();
  	    if (loyalty != null) {
	  	    double currBal = loyalty.getCurrBalance();
	  	    double currYearBal = loyalty.getCurrYearBalance();
	  	    double lastYearBal = loyalty.getLastYearBalance();
	  	    if(loyalty.isYearlyComputed()){
	  	    	statements.add(loyaltyDAO.getUpdatePointsSQL(loyalty.getLoyaltyNumber(), 
	  	  	    		currBal - points + loyaltyUsed, loyalty.getLifeTimeBalance() - points + loyaltyUsed,
	  	  	    		currYearBal - points+loyaltyUsed,lastYearBal ));
	  	    } else {
	  	    	statements.add(loyaltyDAO.getUpdatePointsSQL(loyalty.getLoyaltyNumber(), 
	  	  	    		currBal - points + loyaltyUsed, loyalty.getLifeTimeBalance() - points + loyaltyUsed,
	  	  	    		0,0 ));
	  	    }
  	    }
  	    
  	    
      }
      
    }
    
    Payment[] payments = originalTransaction.getPaymentsArray();
    for (int i = 0; i < payments.length; i++)
      if (payments[i] instanceof DueBillIssue) { //mark Due Bill Issue as deleted
//        statements.add(redeemableIssueDAO.getUpdateDueBillSQLForLogicalDelete((Redeemable)payments[
//            i]));
        //Fix for defect# 1247
        RedeemableHist redhist = new RedeemableHist();
        ArmCurrency amount = ((DueBillIssue) payments[i]).getAmount().multiply(0);
        redhist.setAmountUsed(amount);
        redhist.setTransactionsIdUsed(voidTransaction.getId());
        redhist.setDateUsed(voidTransaction.getSubmitDate());
		((DueBillIssue)payments[i]).addRedemption(redhist);
        statements.addAll(Arrays.asList(redeemableIssueDAO.getUpdateSQL(((DueBillIssue)payments[i]))));
        statements.add(redeemableIssueDAO.getUpdateDueBillSQLForLogicalDelete((Redeemable)payments[i]));        
      } else if (payments[i] instanceof DueBill || payments[i] instanceof GiftCert
          || payments[i] instanceof StoreValueCard) //undo Redeemable (hist) used
        statements.add(redeemableHistDAO.getDeleteSQL(originalTransaction.getId()));
    if (originalTransaction instanceof LayawayPaymentTransaction) { //undo add layaway payment info
      Layaway originalLayaway = layawayDAO.selectById(((LayawayPaymentTransaction)
          originalTransaction).getOriginalLayawayId());
      ArmCurrency updatedAmountDue = originalLayaway.getCurrentAmountDue().add(((
          LayawayPaymentTransaction)originalTransaction).getAmount());
      originalLayaway.doSetCurrentAmountDue(updatedAmountDue);
      statements.addAll(Arrays.asList(layawayDAO.getUpdateSQL(originalLayaway)));
      statements.add(layawayPaymentInfoDAO.getDeleteSQLByLayawayPaymentTransactionId(
          originalTransaction.getId()));
    }
    if (originalTransaction instanceof LayawayRTSTransaction) { //undo RTS
      Layaway originalLayaway = ((LayawayRTSTransaction)originalTransaction).getOriginalLayaway();
      originalLayaway.doSetAuditFlag("");
      statements.addAll(Arrays.asList(layawayDAO.getUpdateSQL(originalLayaway)));
    }
    if (originalTransaction instanceof RedeemableBuyBackTransaction) {
      // delete the redeemable history object that was created in the original transaction.
      // that should undo the damage.
      statements.add(redeemableHistDAO.getDeleteSQL(originalTransaction.getId()));
    }
    return (ParametricStatement[])statements.toArray(new ParametricStatement[0]);
  }
//  return (ParametricStatement[])statements.toArray(new ParametricStatement[0]);
//      } else if (paymentTransaction instanceof CMSMiscPaidOut
//          && ("CLOSE_DEPOSIT").equals(((CMSMiscPaidOut)paymentTransaction).getType())) {
//        DepositHistory hist = null;
//        hist = new DepositHistory();
//        hist.setassoc(((CMSMiscPaidOut)paymentTransaction).getTheOperator().getId());
//        hist.setCustomer((CMSCustomer)((CMSMiscPaidOut)paymentTransaction).getCustomer());
//        hist.setStoreID(((CMSMiscPaidOut)paymentTransaction).getStore().getId());
//        hist.setTransactionDate(paymentTransaction.getProcessDate());
//        hist.setTransactionId(paymentTransaction.getId());
//        hist.setTransactionType(DepositHistory.CLOSE_DEPOSIT_TYPE);
//        hist.setamount(((CMSMiscPaidOut)paymentTransaction).getAmount());
//        statements.add(customerDAO.getInsertDepositHistorySQL(hist));
//        if (((CMSMiscPaidOut)paymentTransaction).getCustomer() != null
//            && hist.getamount().doubleValue() != 0.0)
//          statements.add(customerDAO.getUpdateDepositSQL((CMSCustomer)((CMSMiscPaidOut)
//              paymentTransaction).getCustomer(), hist.getamount()));
//        return (ParametricStatement[])statements.toArray(new ParametricStatement[0]);

  /**
   *
   * @param originalTransaction Transfer
   * @return ParametricStatement[]
   * @throws SQLException
   */
  private ParametricStatement[] undoForVoidTransacitonWithOriginalTransferTransaction(Transfer
      originalTransaction)
      throws SQLException {
    if (originalTransaction instanceof TransferIn)
      return transferOutDAO.getUpdateForCompletionSQL(((TransferIn)originalTransaction).
          getTransferOut(), false);
    else
      return new ParametricStatement[0];
  }

  /**
   *
   * @param paymentTransaction PaymentTransaction
   * @return ParametricStatement[]
   * @throws SQLException
   */
  private ParametricStatement[] addToEmployeeSale(PaymentTransaction paymentTransaction)
      throws SQLException {
    List statements = new ArrayList();
    if (paymentTransaction instanceof VoidTransaction)
      statements.addAll(Arrays.asList(empSaleDAO.getInsertSQL((VoidTransaction)paymentTransaction)));
    else if (paymentTransaction instanceof CompositePOSTransaction)
      statements.addAll(Arrays.asList(empSaleDAO.getInsertSQL((CompositePOSTransaction)
          paymentTransaction)));
    return (ParametricStatement[])statements.toArray(new ParametricStatement[0]);
  }

  /**
   *
   * @param paymentTransaction PaymentTransaction
   * @return ParametricStatement[]
   * @throws SQLException
   */
  private ParametricStatement[] updateRewardCards(PaymentTransaction paymentTransaction)
      throws SQLException {
    ArrayList statements = new ArrayList();
    if (paymentTransaction instanceof VoidTransaction)
      return new ParametricStatement[0];
    else if (paymentTransaction instanceof CompositePOSTransaction) {
      Discount[] discounts = ((CompositePOSTransaction)paymentTransaction).getSettlementDiscountsArray();
      for (int i = 0; i < discounts.length; i++) {
        if (discounts[i] instanceof RewardDiscount && ((RewardDiscount)discounts[i]).getRewardCard() != null) {
          RedeemableHist hist = new RedeemableHist();
          hist.setDateUsed(new Date());
          hist.setTransactionsIdUsed(paymentTransaction.getId());
          hist.setAmountUsed(((RewardDiscount)discounts[i]).getAmount());
          ((RewardDiscount)discounts[i]).getRewardCard().addRedemption(hist);
          statements.addAll(Arrays.asList(redeemableIssueDAO.getUpdateRewardCardSQLForLogicalDelete(((
              RewardDiscount)discounts[i]).getRewardCard())));
        }
      }
    }
    return (ParametricStatement[])statements.toArray(new ParametricStatement[statements.size()]);
  }

  /**
   *
   * @param compositePOSTransaction CompositePOSTransaction
   * @return CMSLayaway
   * @throws CurrencyException
   */
  private CMSLayaway createLayaway(CompositePOSTransaction compositePOSTransaction)
      throws CurrencyException {
    POSLineItem[] layawayLineItems = compositePOSTransaction.getLayawayLineItemsArray();
    if (layawayLineItems == null || layawayLineItems.length == 0)
      return null;
    CMSLayaway layaway = new CMSLayaway();
    layaway.doSetAuditFlag("");
    layaway.doSetConsultant(compositePOSTransaction.getConsultant());
    layaway.doSetCustomer(compositePOSTransaction.getCustomer());
    layaway.doSetId(compositePOSTransaction.getId()); //use the same id as txn
    layaway.doSetOriginalTransactionDate(compositePOSTransaction.getCreateDate());
    layaway.doSetOriginalTransactionId(compositePOSTransaction.getId());
    layaway.doSetStore(compositePOSTransaction.getStore());
    layaway.doSetRestockingFeePercent(compositePOSTransaction.getLayawayTransaction().
        getRestockingPercent());
    layaway.doSetIsRestockingFree(compositePOSTransaction.getLayawayTransaction().isRestockingFree());
    layaway.doSetOriginalNetAmount(compositePOSTransaction.getLayawayOriginalNetAmount());
    layaway.doSetOriginalTransactionAmount(compositePOSTransaction.getLayawayOriginalTotalAmountDue());
    ArmCurrency diff = compositePOSTransaction.getLayawayOriginalTotalAmountDue().subtract(
        compositePOSTransaction.getLayawayTotalAmountDue());
    layaway.doSetCurrentAmountDue(diff);
    Date date = compositePOSTransaction.getCreateDate();
    ArmCurrency amount = compositePOSTransaction.getLayawayTotalAmountDue();
    String storeId = compositePOSTransaction.getStore().getId();
    String txnId = compositePOSTransaction.getId();
    LayawayPaymentInfo layawayPaymentInfo = new LayawayPaymentInfo(date, amount, storeId, txnId);
    layaway.doAddPayment(layawayPaymentInfo);
    return layaway;
  }

  /**
   *
   * @param compositePOSTransaction CompositePOSTransaction
   * @return Redeemable[]
   */
  private Redeemable[] createRedeemablesSold(CompositePOSTransaction compositePOSTransaction) {
    ArrayList redeemables = new ArrayList();
    POSLineItem[] saleLineItems = compositePOSTransaction.getSaleLineItemsArray();
    if (saleLineItems == null || saleLineItems.length == 0)
      return new Redeemable[0];
    for (int i = 0; i < saleLineItems.length; i++) {
      POSLineItemDetail[] lineItemDetails = saleLineItems[i].getLineItemDetailsArray();
      for (int j = 0; j < lineItemDetails.length; j++) {
        lineItemDetails[j].getLineItem();
        String giftCertId = lineItemDetails[j].getGiftCertificateId();
        //Vivek Mishra : Added to identify reload transaction
        if (giftCertId != null && giftCertId.length() > 0 && !giftCertId.contains("R")) {
          String redeemableType = lineItemDetails[j].getLineItem().getItem().getRedeemableType();
          Redeemable redeemable = null;
          if (Redeemable.GIFT_CERTIFICATE_TYPE.equals(redeemableType)) {
            redeemable = new GiftCert(IPaymentConstants.GIFT_CERTIFICATE);
            ((GiftCert)redeemable).setControlNum(giftCertId);
          } else if (Redeemable.STORE_VALUE_CARD_TYPE.equals(redeemableType)) {
            //ks: To set customer ID
            redeemable = new CMSStoreValueCard(IPaymentConstants.STORE_VALUE_CARD);
            ((CMSStoreValueCard)redeemable).doSetCustomerId(compositePOSTransaction.getCustomer().
                getId());
            ((StoreValueCard)redeemable).doSetControlNum(giftCertId);
            ((CMSStoreValueCard)redeemable).doSetStoreId(compositePOSTransaction.getStore().getId());
            if (lineItemDetails[j] instanceof CMSSaleLineItemDetail) {
              ((CMSStoreValueCard)redeemable).setManualAuthCode(((CMSSaleLineItemDetail)
                  lineItemDetails[j]).getManualAuthCode());
            }
          } else if (Redeemable.STORE_VALUE_CARD_ADD_TYPE.equals(redeemableType)) {
            redeemable = new StoreValueCard(IPaymentConstants.STORE_VALUE_CARD);
            ((StoreValueCard)redeemable).doSetControlNum(giftCertId);
          }
          if (redeemable != null) {
            redeemable.setAuditNote("");
            redeemable.setCreateDate(compositePOSTransaction.getCreateDate());
            redeemable.setId(giftCertId);
            redeemable.setIssueAmount(lineItemDetails[j].getLineItem().getItemRetailPrice());
            redeemable.setType(redeemableType);
            if (compositePOSTransaction.getCustomer() != null) {
              redeemable.setFirstName(compositePOSTransaction.getCustomer().getFirstName());
              redeemable.setLastName(compositePOSTransaction.getCustomer().getLastName());
              //ks: Phone number no longer a required field
              //              redeemable.setPhoneNumber(compositePOSTransaction.getCustomer().getTelephone().getFormattedNumber());
            }
            redeemables.add(redeemable);
          }
        }
      }
    }
    if (redeemables == null || redeemables.size() == 0)
      return new Redeemable[0];
    else
      return (Redeemable[])redeemables.toArray(new Redeemable[0]);
  }

  /**
   *
   * @param paymentTransaction PaymentTransaction
   * @return DueBillIssue
   */
  private DueBillIssue getNewDueBillIssue(PaymentTransaction paymentTransaction) {
    Payment[] payments = paymentTransaction.getPaymentsArray();
    for (int i = 0; i < payments.length; i++)
      if (payments[i] instanceof CMSDueBillIssue) {
        CMSDueBillIssue dueBillIssue = (CMSDueBillIssue)payments[i];
        //ks: sets control number in the bldr as per the new requirement
        //        dueBillIssue.setId(paymentTransaction.getId());
        dueBillIssue.setCreateDate(paymentTransaction.getCreateDate());
        dueBillIssue.setIssueAmount(dueBillIssue.getAmount().absoluteValue());
        if (paymentTransaction instanceof CompositePOSTransaction) {
          CompositePOSTransaction compositePOSTransaction = (CompositePOSTransaction)
              paymentTransaction;
          if (compositePOSTransaction.getCustomer() != null) {
            dueBillIssue.setFirstName(compositePOSTransaction.getCustomer().getFirstName());
            dueBillIssue.setLastName(compositePOSTransaction.getCustomer().getLastName());
            //            ks: Phone Number no longer a key for customer lookup
            //            dueBillIssue.setPhoneNumber(compositePOSTransaction.getCustomer().getTelephone().getFormattedNumber());
            ((CMSDueBillIssue)dueBillIssue).doSetCustomerId(compositePOSTransaction.getCustomer().
                getId());
            ((CMSDueBillIssue)dueBillIssue).doSetStoreId(compositePOSTransaction.getStore().getId());
          }
        } else { //a non-compositePOSTransaction has no Customer
          dueBillIssue.setFirstName("");
          dueBillIssue.setLastName("");
          dueBillIssue.setPhoneNumber("");
        }
        return dueBillIssue;
      }
    return null;
  }

  private Boolean WRITE_TXN_TO_XML = null;

  /**
   *
   * @param paymentTransaction PaymentTransaction
   */
  private void writeXMLToFile(PaymentTransaction paymentTransaction) {
    if (!(paymentTransaction instanceof CompositePOSTransaction))
      return;
    try {
      if (WRITE_TXN_TO_XML == null)
        WRITE_TXN_TO_XML = new Boolean(new ConfigMgr("pos.cfg").getString("WRITE_TXN_TO_XML"));
    } catch (Exception exp) {
      System.out.println("Exception --> " + exp);
      exp.printStackTrace();
      WRITE_TXN_TO_XML = Boolean.FALSE;
    }
    if (!WRITE_TXN_TO_XML.booleanValue())
      return;
    try {
      char[] array = paymentTransaction.getId().toCharArray();
      StringBuffer sb = new StringBuffer(10);
      for (int i = 0; i < array.length; i++)
        if (Character.isDigit(array[i]))
          sb.append(array[i]);
      String fileName = FileMgr.getLocalFile("tmp", "TXN" + sb + ".xml");
      new PaymentTransactionXML().writToFile(fileName, paymentTransaction);
    } catch (Exception exception) {
      LoggingServices.getCurrent().logMsg(this.getClass().getName(), "writeXMLToFile", "Exception"
          , "See Exception", LoggingServices.MAJOR, exception);
      exception.printStackTrace();
    }
  }

  /**
   * This method is used to find the open Presale on the basis of transaction id
   * @param txnId String
   * @return CMSTransactionHeader
   * @throws Exception
   */
  public CMSTransactionHeader findOpenPresaleById(String txnId)
      throws java.lang.Exception {
    try {
      return vArmTxnHdrDAO.selectByTxnId(txnId, "PRSO");
    } catch (Exception exception) {
      LoggingServices.getCurrent().logMsg(this.getClass().getName(), "findOpenPresaleById"
          , "Exception", "See Exception", LoggingServices.MAJOR, exception);
      exception.printStackTrace();
      throw exception;
    }
  }

  /**
   * This method is used to find the open Presale on the basis of store id
   * @param storeId String
   * @return CMSTransactionHeader[]
   * @throws Exception
   */
  public CMSTransactionHeader[] findOpenPresaleByStore(String storeId)
      throws java.lang.Exception {
    try {
      return vArmTxnHdrDAO.selectByStoreId(storeId, "PRSO");
    } catch (Exception exception) {
      LoggingServices.getCurrent().logMsg(this.getClass().getName(), "findOpenPresaleByStore"
          , "Exception", "See Exception", LoggingServices.MAJOR, exception);
      exception.printStackTrace();
      throw exception;
    }
  }

  /**
   * This method is used to find the open Presale on the basis of customer id
   *
   * @param custId String
   * @return CMSTransactionHeader[]
   * @throws Exception
   */
  public CMSTransactionHeader[] findOpenPresaleByCustomer(String custId)
      throws java.lang.Exception {
    try {
      return vArmTxnHdrDAO.selectByCustId(custId, "PRSO");
    } catch (Exception exception) {
      LoggingServices.getCurrent().logMsg(this.getClass().getName(), "findOpenPresaleByCustomer"
          , "Exception", "See Exception", LoggingServices.MAJOR, exception);
      exception.printStackTrace();
      throw exception;
    }
  }

  /**
   * This method is used to find the open Presale on the basis of store
   * id and for specific date range
   * @param storeId String
   * @param startDate Date
   * @param endDate Date
   * @return CMSTransactionHeader[]
   * @throws Exception
   */
  public CMSTransactionHeader[] findOpenPresaleByDate(String storeId, Date startDate, Date endDate)
      throws java.lang.Exception {
    try {
      return vArmTxnHdrDAO.selectByDate(storeId, "PRSO", startDate, endDate);
    } catch (Exception exception) {
      LoggingServices.getCurrent().logMsg(this.getClass().getName(), "findOpenPresaleByCustomer"
          , "Exception", "See Exception", LoggingServices.MAJOR, exception);
      exception.printStackTrace();
      throw exception;
    }
  }

  /**
   * This method is used to find the open consignments on the basis of
   * transaction id
   * @param txnId String
   * @return CMSTransactionHeader
   * @throws Exception
   */
  public CMSTransactionHeader findOpenConsignmentById(String txnId)
      throws java.lang.Exception {
    try {
      return vArmTxnHdrDAO.selectByTxnId(txnId, "CSGO");
    } catch (Exception exception) {
      LoggingServices.getCurrent().logMsg(this.getClass().getName(), "findOpenConsignmentById"
          , "Exception", "See Exception", LoggingServices.MAJOR, exception);
      exception.printStackTrace();
      throw exception;
    }
  }

  /**
   * This method is used to find the open consignments on the basis of store
   * id
   * @param storeId String
   * @return CMSTransactionHeader[]
   * @throws Exception
   */
  public CMSTransactionHeader[] findOpenConsignmentByStore(String storeId)
      throws java.lang.Exception {
    try {
      return vArmTxnHdrDAO.selectByStoreId(storeId, "CSGO");
    } catch (Exception exception) {
      LoggingServices.getCurrent().logMsg(this.getClass().getName(), "findOpenConsignmentByStore"
          , "Exception", "See Exception", LoggingServices.MAJOR, exception);
      exception.printStackTrace();
      throw exception;
    }
  }

  /**
   * This method is used to find the open consignments on the basis of
   * customer id
   * @param custId String
   * @return CMSTransactionHeader[]
   * @throws Exception
   */
  public CMSTransactionHeader[] findOpenConsignmentByCustomer(String custId)
      throws java.lang.Exception {
    try {
      return vArmTxnHdrDAO.selectByCustId(custId, "CSGO");
    } catch (Exception exception) {
      LoggingServices.getCurrent().logMsg(this.getClass().getName()
          , "findOpenConsignmentByCustomer", "Exception", "See Exception", LoggingServices.MAJOR
          , exception);
      exception.printStackTrace();
      throw exception;
    }
  }

  /**
   * This method is used to find the open consignments on the basis of store
   * id and for specific date range
   * @param storeId String
   * @param startDate Date
   * @param endDate Date
   * @return CMSTransactionHeader[]
   * @throws Exception
   */
  public CMSTransactionHeader[] findOpenConsignmentByDate(String storeId, Date startDate
      , Date endDate)
      throws java.lang.Exception {
    try {
      return vArmTxnHdrDAO.selectByDate(storeId, "CSGO", startDate, endDate);
    } catch (Exception exception) {
      LoggingServices.getCurrent().logMsg(this.getClass().getName()
          , "findOpenConsignmentByCustomer", "Exception", "See Exception", LoggingServices.MAJOR
          , exception);
      exception.printStackTrace();
      throw exception;
    }
  }

  /**
   * This method is used to update of the Expiration Date for consignment Transaction
   * @param txn
   * @return
   * @throws Exception
   */
  public ConsignmentTransaction updateConsignmentExpirationDate(ConsignmentTransaction txn)
      throws Exception {
    try {
      return armPosCsgDAO.updateExpirationDt(txn);
    } catch (Exception exception) {
      LoggingServices.getCurrent().logMsg(this.getClass().getName()
          , "updateConsignmentExpirationDate", "Exception", "See Exception", LoggingServices.MAJOR
          , exception);
      exception.printStackTrace();
      throw exception;
    }
  }

  /**
   * This method is used to update of the Expiration Date for Presale Transaction
   * @param txn
   * @return
   * @throws Exception
   */
  public PresaleTransaction updatePresaleExpirationDate(PresaleTransaction txn)
      throws Exception {
    try {
      return armPosPrsDAO.updateExpirationDt(txn);
    } catch (Exception exception) {
      LoggingServices.getCurrent().logMsg(this.getClass().getName(), "updatePresaleExpirationDate"
          , "Exception", "See Exception", LoggingServices.MAJOR, exception);
      exception.printStackTrace();
      throw exception;
    }
  }

  public ReservationTransaction updateReservationExpirationDate(ReservationTransaction txn)
      throws Exception {
    try {
      return armPosRsvDAO.updateExpirationDt(txn);
    } catch (Exception exception) {
      LoggingServices.getCurrent().logMsg(this.getClass().getName()
          , "updateReservationExpirationDate"
          , "Exception", "See Exception", LoggingServices.MAJOR, exception);
      exception.printStackTrace();
      throw exception;
    }
  }

  /**
   * This method is used to find the transaction ids on the basis of store id
   * and register id
   * @param storeId String
   * @param registerId String
   * @return String[]
   * @throws Exception
   */
  public String[] findTxnIdsByStoreIdAndRegisterId(String storeId, String registerId)
      throws java.lang.Exception {
    try {
      return transactionDAO.selectMaxTxnId(storeId, registerId);
    } catch (Exception exception) {
      LoggingServices.getCurrent().logMsg(this.getClass().getName(), "findById", "Exception"
          , "See Exception", LoggingServices.MAJOR, exception);
      exception.printStackTrace();
      throw exception;
    }
  }

  /**
   * Find FiscalDocumentNumber by Store and Register ID
   * @param sStoreId String
   * @param sRegisterId String
   * @throws Exception
   * @return FiscalDocumentNumber
   */
  public FiscalDocumentNumber findFiscalDocNumByStoreAndRegister(String sStoreId
      , String sRegisterId)
      throws Exception {
    try {
      FiscalDocumentNumber fiscalDocumentNumber = armFiscalDocNoDAO.getByStoreAndRegister(sStoreId
          , sRegisterId);
      return fiscalDocumentNumber;
    } catch (Exception exception) {
      LoggingServices.getCurrent().logMsg(this.getClass().getName()
          , "findFiscalDocNumByStoreAndRegister", "Exception", "See Exception"
          , LoggingServices.MAJOR, exception);
      exception.printStackTrace();
      throw exception;
    }
  }

  /**
   * This method is used to find the open Reservation on the basis of
   * transaction id
   * @param txnId String
   * @return CMSTransactionHeader
   * @throws Exception
   */
  public CMSTransactionHeader findOpenReservationById(String txnId)
      throws java.lang.Exception {
    try {
      return vArmTxnHdrDAO.selectByTxnId(txnId, GUI_TRN_TYP_RESERVATION_OPEN);
    } catch (Exception exception) {
      LoggingServices.getCurrent().logMsg(this.getClass().getName(), "findOpenReservationById"
          , "Exception", "See Exception", LoggingServices.MAJOR, exception);
      exception.printStackTrace();
      throw exception;
    }
  }

  /**
   * This method is used to find the open Reservation on the basis of store
   * id
   * @param storeId String
   * @return CMSTransactionHeader[]
   * @throws Exception
   */
  public CMSTransactionHeader[] findOpenReservationByStore(String storeId)
      throws java.lang.Exception {
    try {
      return vArmTxnHdrDAO.selectByStoreId(storeId, GUI_TRN_TYP_RESERVATION_OPEN);
    } catch (Exception exception) {
      LoggingServices.getCurrent().logMsg(this.getClass().getName(), "findOpenReservationByStore"
          , "Exception", "See Exception", LoggingServices.MAJOR, exception);
      exception.printStackTrace();
      throw exception;
    }
  }

  /**
   * This method is used to find the open Reservation on the basis of
   * customer id
   * @param custId String
   * @return CMSTransactionHeader[]
   * @throws Exception
   */
  public CMSTransactionHeader[] findOpenReservationByCustomer(String custId)
      throws java.lang.Exception {
    try {
      return vArmTxnHdrDAO.selectByCustId(custId, GUI_TRN_TYP_RESERVATION_OPEN);
    } catch (Exception exception) {
      LoggingServices.getCurrent().logMsg(this.getClass().getName()
          , "findOpenReservationByCustomer", "Exception", "See Exception", LoggingServices.MAJOR
          , exception);
      exception.printStackTrace();
      throw exception;
    }
  }

  /**
   * This method is used to find the open Reservation on the basis of store
   * id and for specific date range
   * @param storeId String
   * @param startDate Date
   * @param endDate Date
   * @return CMSTransactionHeader[]
   * @throws Exception
   */
  public CMSTransactionHeader[] findOpenReservationByDate(String storeId, Date startDate
      , Date endDate)
      throws java.lang.Exception {
    try {
      return vArmTxnHdrDAO.selectByDate(storeId, GUI_TRN_TYP_RESERVATION_OPEN, startDate, endDate);
    } catch (Exception exception) {
      LoggingServices.getCurrent().logMsg(this.getClass().getName(), "findOpenReservationByDate"
          , "Exception", "See Exception", LoggingServices.MAJOR, exception);
      exception.printStackTrace();
      throw exception;
    }
  }
  
  /**
   * put your documentation comment here
   * @param composite
   * @return
   * @exception Exception
   */
  public CMSCompositePOSTransaction addShippingRequestToTransaction(CMSCompositePOSTransaction
      composite)
      throws Exception {
    List statements = new ArrayList();
    try {
      //System.out.println("JDBC");
      PosLineItemOracleDAO lineItemDAO = new PosLineItemOracleDAO();
      statements.addAll(Arrays.asList(lineItemDAO.getUpdateShippingRequestSQL(composite)));
      CompositeOracleDAO compositeOracleDAO = new CompositeOracleDAO();
      ArmStgTxnDtlOracleDAO stgOracleDAO = new ArmStgTxnDtlOracleDAO();
      statements.addAll(Arrays.asList(stgOracleDAO.getUpdateShipRequestInsertSQL(composite)));
      compositeOracleDAO.execute((ParametricStatement[])statements.toArray(new ParametricStatement[
          0]));
      return composite;
    } catch (Exception exception) {
      LoggingServices.getCurrent().logMsg(this.getClass().getName()
          , "addShippingRequestToTransaction", "Exception", "See Exception", LoggingServices.MAJOR
          , exception);
      exception.printStackTrace();
      throw exception;
    }
  }

  /**
   * This method is used to update customer for a transaction
   * @param paymentTransaction CompositePOSTransaction
   * @return ParametricStatement[]
   * @throws SQLException
   */
  public CMSCompositePOSTransaction updateCustomer(CMSCompositePOSTransaction posTransaction)
      throws Exception {
    List statements = new ArrayList();
    try {
    	
      //System.out.println("--- UPDATING CUSTOMER ---");
    	
      Customer customer = posTransaction.getCustomer();
      if (customer == null)
        return posTransaction;
      CompositeOracleDAO compositeOracleDAO = new CompositeOracleDAO();
      statements.add(((PaymentTransactionOracleDAO)paymentTransactionDAO).getUpdateCustomerSQL(
          posTransaction));
      statements.add(compositeOracleDAO.getUpdateCustomerSQL(posTransaction));
      ArmCustSaleSummaryOracleDAO dao = new ArmCustSaleSummaryOracleDAO();
      statements.addAll(Arrays.asList(dao.getUpdateCustomerSQL(posTransaction)));
      ArmStgTxnDtlOracleDAO stgOracleDAO = new ArmStgTxnDtlOracleDAO();
      statements.addAll(Arrays.asList(stgOracleDAO.getUpdateCustomerInsertSQL(posTransaction)));
      
      
      //If required, update Customer in CRM Staging and set
      //status as processed.
      //This will work for only customers that are added to transaction using
      //REFRESH button on Customer Lookup applet.
      CMSCustomer cmsCustomer = (CMSCustomer) customer;
      ConfigMgr customerCFG = new ConfigMgr("customer.cfg");
      String customerPrefix = "S";
      CustomerOracleDAO customerOracleDAO = new CustomerOracleDAO();
      if(customerCFG.getString("SAP_CUSTOMER_ID_PREFIX")!=null){
      	customerPrefix  = customerCFG.getString("SAP_CUSTOMER_ID_PREFIX");
      }
      if(cmsCustomer.getId().startsWith(customerPrefix) && cmsCustomer.getStoreId()!=null){
    	System.out.println("UPDATING CRM CUSTOMER "+customer.getId());
      	statements.addAll(Arrays.asList(customerOracleDAO.getUpdateSQLForCRMStagingCustomer(cmsCustomer)));
      }       
           
      //--End 
      
      compositeOracleDAO.execute((ParametricStatement[])statements.toArray(new ParametricStatement[
          0]));
    } catch (Exception exception) {
      LoggingServices.getCurrent().logMsg(this.getClass().getName(), "updateCustomer", "Exception"
          , "See Exception", LoggingServices.MAJOR, exception);
      exception.printStackTrace();
      return null;
    }
    return posTransaction;
  }

  /**
   * put your documentation comment here
   * @param posTransaction
   * @param lineItems
   * @return
   * @exception Exception
   */
  public CMSCompositePOSTransaction updateConsultant(CMSCompositePOSTransaction posTransaction
      , POSLineItem[] lineItems)
      throws Exception {
    List statements = new ArrayList();
    System.out.println("CMSTransactionPOSJdbcServices : updateConsultant : " + lineItems.length);
    try {
      PosLineItemOracleDAO lineItemDAO = new PosLineItemOracleDAO();
      CompositeOracleDAO compositeOracleDAO = new CompositeOracleDAO();
      statements.add(compositeOracleDAO.getUpdateConsultantSQL(posTransaction));
      statements.addAll(Arrays.asList(lineItemDAO.getUpdateConsutantSQL(lineItems)));
      ArmStgTxnDtlOracleDAO stgOracleDAO = new ArmStgTxnDtlOracleDAO();
      statements.addAll(Arrays.asList(stgOracleDAO.getUpdateConsultantInsertSQL(lineItems)));
      EmpSaleOracleDAO empSaledao = new EmpSaleOracleDAO();
      statements.addAll(Arrays.asList(empSaledao.getUpdateSql(posTransaction, lineItems)));
      System.out.println("total statments " + statements.size());
      lineItemDAO.execute((ParametricStatement[])statements.toArray(new ParametricStatement[0]));
    } catch (Exception exception) {
      LoggingServices.getCurrent().logMsg(this.getClass().getName(), "updateConsultant"
          , "Exception", "See Exception", LoggingServices.MAJOR, exception);
      exception.printStackTrace();
      return null;
    }
    return posTransaction;
  }
  
  
  
  /**
	 * Added by Satin to search digital signature based on transaction Id.
	 * @param txnId String
	 * @throws Exception
	 * @return String
	 */
  public String selectDigitalSignature(String txnId)
      throws java.lang.Exception {
    try {
      return compositeDAO.selectDigitalSignature(txnId);
    } catch (Exception exception) {
      LoggingServices.getCurrent().logMsg(this.getClass().getName(), "findById", "Exception"
          , "See Exception", LoggingServices.MAJOR, exception);
      exception.printStackTrace();
      throw exception;
    }
  }
  
  
  /**
   * put your documentation comment here
   * @param paymentTransaction
   * @return
   * @exception SQLException, CurrencyException
   */
  protected ParametricStatement[] persistCustCreditHistory(PaymentTransaction paymentTransaction)
      throws SQLException, CurrencyException {
	  //System.out.println("persistCustCreditHistory "+paymentTransaction.getTransactionType());
    List statements = new ArrayList();
    if (paymentTransaction instanceof CompositePOSTransaction) {
    	//System.out.println(" CompositePOSTransaction ");

      ArmCurrency creditAmount = new ArmCurrency(0.0);
      CreditHistory hist = null;
      hist = new CreditHistory();
      hist.setassoc(((CMSCompositePOSTransaction)paymentTransaction).getConsultant().getId());
      hist.setCustomer((CMSCustomer)((CMSCompositePOSTransaction)paymentTransaction).getCustomer());
      hist.setStoreID(((CMSCompositePOSTransaction)paymentTransaction).getStore().getId());
      hist.setTransactionDate(paymentTransaction.getProcessDate());
      hist.setTransactionId(paymentTransaction.getId());
      hist.setTransactionType(CreditHistory.OPEN_CREDIT_TYPE);

      
      Payment[] payments = ((CMSCompositePOSTransaction)paymentTransaction).getPaymentsArray();
      Credit creditPayment = null;
      for (int index = 0; index < payments.length; index++) {
    	  Payment payment = payments[index];
    	  if(payment instanceof Credit){
    		//  System.out.println("Credit Payment ");
    		  creditPayment = (Credit)payment;
    		  break;
    	  }
      }
      
      if(creditPayment!=null ){
    	  hist.setamount(creditPayment.getAmount());
          ParametricStatement statement  = customerDAO.getInsertCreditHistorySQL(hist);
          //System.out.println("addting getInser " + statement.toString());
          if(hist.getamount().getDoubleValue()>0){
        	  statements.add(statement);
          }
      }else{
    	  System.out.println("no credit payment ");
      }


      return (ParametricStatement[])statements.toArray(new ParametricStatement[0]);
    } else if (paymentTransaction instanceof CMSMiscCollectionCredit
        && ("MISC_PAID_IN").equals(((CMSMiscCollectionCredit)paymentTransaction).getType())) {
    	//System.out.println(" CMSMiscCollection ");
      CreditHistory hist = null;
      hist = new CreditHistory();
      hist.setassoc(((CMSMiscCollectionCredit)paymentTransaction).getTheOperator().getId());
      hist.setCustomer((CMSCustomer)((CMSMiscCollectionCredit)paymentTransaction).getCustomer());
      hist.setStoreID(((CMSMiscCollectionCredit)paymentTransaction).getStore().getId());
      hist.setTransactionDate(paymentTransaction.getProcessDate());
      hist.setTransactionId(paymentTransaction.getId());
      hist.setTransactionType(CreditHistory.CLOSE_CREDIT_TYPE);
      hist.setamount(((CMSMiscCollectionCredit)paymentTransaction).getAmount().multiply(-1));
      ParametricStatement statement  = customerDAO.getInsertCreditHistorySQL(hist);
      //System.out.println("addting getInser " + statement.toString());
      if(hist.getamount().getDoubleValue()!=0){
    	  statements.add(statement);
      }
    //  statements.add(customerDAO.getInsertCreditHistorySQL(hist));
      return (ParametricStatement[])statements.toArray(new ParametricStatement[0]);
    }

    return new ParametricStatement[0];
  }

  /**
   * @param paymentTransaction PaymentTransaction
   * @return ParametricStatement[]
   * @throws SQLException
   */
  private ParametricStatement[] addToPurchaseHistory(PaymentTransaction paymentTransaction)
      throws SQLException {
	  List statements = new ArrayList();
	  if (paymentTransaction instanceof VoidTransaction) {
		  ITransaction originalTransaction = ((VoidTransaction)paymentTransaction).getOriginalTransaction();
		  if (originalTransaction instanceof CompositePOSTransaction) {
			  if ((((CompositePOSTransaction)originalTransaction).getCustomer()) != null) {
				  //Add void to purchase history if customer is not null
				  statements.addAll(Arrays.asList(custSaleSummaryDAO.getInsertSQL((VoidTransaction)paymentTransaction)));  		  
			  }
		  }
	  }
	  return (ParametricStatement[])statements.toArray(new ParametricStatement[0]);
  }

  private ParametricStatement[] processNotStoreItems(CompositePOSTransaction compositePOSTransaction)
      throws SQLException {
    List statements = new ArrayList();
    ArrayList itemIdList = new ArrayList();
    POSLineItem[] lineItems = compositePOSTransaction.getLineItemsArray();
    String homeStoreId = compositePOSTransaction.getStore().getId();
    for (int i = 0; i < lineItems.length; i++) {
      CMSItem item = (CMSItem)lineItems[i].getItem();
//PCR1167 Sku lookup and visibility
//System.out.println(saleLineItem.getId() + " is an instance of CMSItem");
      if (!(item.getStoreId().equals(homeStoreId))) {
        if ((itemDAO.selectByItemIdAndStoreId(item.getId(), homeStoreId)) == null) {
//System.out.println("INSERT INTO AS_ITM_RTL_STR TABLE");
          if (!checkIfItemAlreadyExists(itemIdList, item)) {
            itemIdList.add(item.getId());
            statements.addAll(Arrays.asList(itemDAO.getInsertSQLForItemRetailStore(item
                , homeStoreId)));
          }
        }
      }
    }
    return (ParametricStatement[])statements.toArray(new ParametricStatement[0]);
  }
  
//added by SonaliRaina for duty free management PCR for Europe
  //modified by shushma for posting the boarding pass details in pending txn
  private ParametricStatement[] persistBoardingDetails(PaymentTransaction paymentTransaction) throws SQLException {
	// TODO Auto-generated method stub
	  List statements = new ArrayList();
	    CMSAirportDetails airportDetails = ((CMSCompositePOSTransaction)paymentTransaction).getAirportDetails();
	    airportDetails.setTransID(((CMSCompositePOSTransaction)paymentTransaction).getId());
		airportDetails.setCustName(((CMSCompositePOSTransaction)paymentTransaction).getCustomer().getFirstName()+" "+((CMSCompositePOSTransaction)paymentTransaction).getCustomer().getLastName());
		if(airportDetails.getCustName().length()>40)
			airportDetails.setCustName(airportDetails.getCustName().substring(0,40));
	    statements.addAll(Arrays.asList(airportDAO.getInsertSQL(airportDetails)));
	   return (ParametricStatement[])statements.toArray(new ParametricStatement[0]);
  }
  
}

