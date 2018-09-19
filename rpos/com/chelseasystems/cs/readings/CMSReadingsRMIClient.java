/*
 * @copyright (c) 2001 Chelsea Market Systems LLC
 *
 * formatted with JxBeauty (c) johann.langhofer@nextra.at
 */


package com.chelseasystems.cs.readings;

import com.chelseasystems.cr.appmgr.*;
import com.chelseasystems.cr.config.*;
import com.chelseasystems.cr.logging.*;
import com.chelseasystems.cr.node.IRemoteServerClient;
import com.chelseasystems.cr.node.ICMSComponent;
import com.igray.naming.*;
import java.rmi.*;
import com.chelseasystems.cr.readings.*;
import java.lang.*;
import com.chelseasystems.cr.employee.*;
import java.util.*;
import com.chelseasystems.cr.store.*;
import com.chelseasystems.cs.store.*;
import com.chelseasystems.cr.currency.*;
import com.chelseasystems.cr.txnposter.*;
import com.chelseasystems.cs.txnposter.*;
import com.chelseasystems.cr.item.MiscItemManager;


/**
 *
 * <p>Title: CMSReadingsRMIClient</p>
 *
 * <p>Description: The client-side of an RMI connection for fetching/submitting.</p>
 *
 * <p>Copyright: Copyright (c) 2005</p>
 *
 * <p>Company: </p>
 *
 * @author not attributable
 * @version 1.0
 */
public class CMSReadingsRMIClient extends CMSReadingsServices implements IRemoteServerClient {

  /** The configuration manager */
  private ConfigMgr config = null;

  /** The reference to the remote implementation of the service. */
  private ICMSReadingsRMIServer cmsreadingsServer = null;

  /** The maximum number of times to try to establish a connection to the RMIServerImpl */
  private int maxTries = 1;

  /**
   * Set the configuration manager and make sure that the system has a
   * security manager set.
   * @throws DowntimeException
   */
  public CMSReadingsRMIClient()
      throws DowntimeException {
    config = new ConfigMgr("readings.cfg");
    if (System.getSecurityManager() == null)
      System.setSecurityManager(new RMISecurityManager());
    init();
  }

  /**
   * Get the remote interface from the registry.
   * @throws DowntimeException
   */
  private void init()
      throws DowntimeException {
    try {
      this.lookup();
      System.out.println("CMSReadingsRMIClient Lookup: Complete");
    } catch (Exception e) {
      LoggingServices.getCurrent().logMsg(getClass().getName(), "init()"
          , "Cannot establish connection to RMI server."
          , "Make sure that the server is registered on the remote server"
          + " and that the name of the remote server and remote service are"
          + " correct in the update.cfg file.", LoggingServices.MAJOR, e);
      throw new DowntimeException(e.getMessage());
    }
  }

  /**
   * Perform lookup of remote server.
   * @exception Exception
   */
  public void lookup()
      throws Exception {
    NetworkMgr mgr = new NetworkMgr("network.cfg");
    maxTries = mgr.getRetryAttempts();
    String connect = mgr.getRMIMasterNode() + config.getString("REMOTE_NAME") + mgr.getQuery();
    cmsreadingsServer = (ICMSReadingsRMIServer)NamingService.lookup(connect);
  }

  /**
   * @see com.chelseasystems.cr.node.ICMSComponent
   * @return  <true> is component is available
   */
  public boolean isRemoteServerAvailable() {
    try {
      return ((ICMSComponent)this.cmsreadingsServer).isAvailable();
    } catch (Exception ex) {
      return false;
    }
  }

  /**
   * default-list
   * @param emp emp
   */
  public EmployeeSales[] getSales(Store store, Employee emp)
      throws Exception {
    for (int x = 0; x < maxTries; x++) {
      if (cmsreadingsServer == null)
        init();
      try {
        return cmsreadingsServer.getSales((Store)store, (Employee)emp);
      } catch (ConnectException ce) {
        cmsreadingsServer = null;
      } catch (Exception ex) {
        throw new DowntimeException(ex.getMessage());
      }
    }
    throw new DowntimeException("Unable to establish connection to CMSReadingsServices");
  }

  /**
   * default-list
   * @param emp emp
   * @param date date
   */
  public EmployeeSales[] getSales(Store store, Employee emp, Date date)
      throws Exception {
    for (int x = 0; x < maxTries; x++) {
      if (cmsreadingsServer == null)
        init();
      try {
        return cmsreadingsServer.getSales((Store)store, (Employee)emp, (Date)date);
      } catch (ConnectException ce) {
        cmsreadingsServer = null;
      } catch (Exception ex) {
        throw new DowntimeException(ex.getMessage());
      }
    }
    throw new DowntimeException("Unable to establish connection to CMSReadingsServices");
  }

  /**
   * default-list
   * @param store store
   */
  public MerchandiseSales[] getSales(Store store)
      throws Exception {
    for (int x = 0; x < maxTries; x++) {
      if (cmsreadingsServer == null)
        init();
      try {
        return cmsreadingsServer.getSales((Store)store);
      } catch (ConnectException ce) {
        cmsreadingsServer = null;
      } catch (Exception ex) {
        throw new DowntimeException(ex.getMessage());
      }
    }
    throw new DowntimeException("Unable to establish connection to CMSReadingsServices");
  }

  /**
   * default-list
   * @param store store
   * @param date date
   */
  public MerchandiseSales[] getSales(Store store, Date date)
      throws Exception {
    for (int x = 0; x < maxTries; x++) {
      if (cmsreadingsServer == null)
        init();
      try {
        return cmsreadingsServer.getSales((Store)store, (Date)date);
      } catch (ConnectException ce) {
        cmsreadingsServer = null;
      } catch (Exception ex) {
        throw new DowntimeException(ex.getMessage());
      }
    }
    throw new DowntimeException("Unable to establish connection to CMSReadingsServices");
  }

  /**
   * default-list
   * @param store store
   * @param date date
   */
  public Employee[] findEmployeesForReadings(Store store, Date date)
      throws Exception {
    for (int x = 0; x < maxTries; x++) {
      if (cmsreadingsServer == null)
        init();
      try {
        return cmsreadingsServer.findEmployeesForReadings((Store)store, (Date)date);
      } catch (ConnectException ce) {
        cmsreadingsServer = null;
      } catch (Exception ex) {
        throw new DowntimeException(ex.getMessage());
      }
    }
    throw new DowntimeException("Unable to establish connection to CMSReadingsServices");
  }

  /**
   * default-list
   * @param emp emp
   */
  public Commissions[] getCommissions(Employee emp)
      throws Exception {
    for (int x = 0; x < maxTries; x++) {
      if (cmsreadingsServer == null)
        init();
      try {
        return cmsreadingsServer.getCommissions((Employee)emp);
      } catch (ConnectException ce) {
        cmsreadingsServer = null;
      } catch (Exception ex) {
        throw new DowntimeException(ex.getMessage());
      }
    }
    throw new DowntimeException("Unable to establish connection to CMSReadingsServices");
  }

  /**
   * default-list
   * @param store store
   * @param date date
   */
  public TrialBalance getTrialBalance(Store store, Date date)
      throws Exception {
    try {
      return getLocalTrialBalance(store, date);
    } catch (Exception ex) {
      throw new DowntimeException(ex.getMessage());
    }
  }

  /**
   * default-list
   * @param store store
   * @param date date
   * @param oper operator
   */
  public TrialBalance getOperatorTrialBalance(Store store, Date date, Employee oper)
      throws Exception {
    try {
      return getLocalOperatorTrialBalance(store, date, oper);
    } catch (Exception ex) {
      throw new DowntimeException(ex.getMessage());
    }
  }

  // /////////////////  private methods //
  /**
   *
   * @param store Store
   * @param date Date
   * @return TrialBalance
   * @throws Exception
   */
  static public TrialBalance getLocalTrialBalance(Store store, Date date)
      throws Exception {
    CMSTrialBalance tb = new CMSTrialBalance();
    try {
      ConfigMgr config = new ConfigMgr("item.cfg");
      String storeValueCardId = config.getString("STORE_VALUE_CARD.BASE_ITEM");
      //      MiscItemManager.getInstance().
      // cash
      ArmCurrency amount = TxnSummary.getPaymentAmountByRegister("CASH");
      tb.doAddCashTendered(amount);
      // check
      amount = TxnSummary.getPaymentAmountByRegister("CHECK");
      tb.doAddCheckTendered(amount);
      // foreign types
      CurrencyType[] types = CurrencyType.getValidCurrencyTypes();
      //___Tim: Changed to read currency types 
      // directly from config file to avoid loading default currencies
      ConfigMgr currencyConfig = new ConfigMgr("currency.cfg");
      String currencyTypesString = currencyConfig.getString("ADDITIONAL_CURRENCY_TYPES");
      if(currencyTypesString != null){
    	  String[] currencyTypes = currencyTypesString.split(",");
    	  for (int i = 0; i < currencyTypes.length; i++) {
	          amount = TxnSummary.getPaymentAmountByRegister(currencyTypes[i] + "_CASH");
	          if (amount.doubleValue() > 0) {
	            tb.doAddCashTendered(amount);
	          }
	          amount = TxnSummary.getPaymentAmountByRegister(currencyTypes[i] + "_CHECK");
	          if (amount.doubleValue() > 0) {
	            tb.doAddCheckTendered(amount);
	          }
    	  }
      }
      /*for (int i = 0; i < types.length; i++) {
        if (!types[i].equals(store.getCurrencyType())) {
          amount = TxnSummary.getPaymentAmountByRegister(types[i].getCode() + "_CASH");
          if (amount.doubleValue() > 0) {
//            if (!amount.getCurrencyType().equals(types[i]))
//              amount.convertTo(types[i]);
            tb.doAddCashTendered(amount);
          }
          amount = TxnSummary.getPaymentAmountByRegister(types[i].getCode() + "_CHECK");
          if (amount.doubleValue() > 0) {
//            if (!amount.getCurrencyType().equals(types[i]))
//              amount.convertTo(types[i]);
            tb.doAddCheckTendered(amount);
          }
        }
      }*/
      tb.doSetBankcardTendered(TxnSummary.getPaymentAmountByRegister("BCRD"));
      tb.doSetBankcardTendered(TxnSummary.getPaymentAmountByRegister("MASTER_CARD"));
      tb.doSetBankcardTendered(TxnSummary.getPaymentAmountByRegister("VISA"));
      tb.doSetAmexTendered(TxnSummary.getPaymentAmountByRegister("AMEX"));
      tb.doSetDiscoverTendered(TxnSummary.getPaymentAmountByRegister("DISC"));
      tb.doSetCreditMemoIssued(TxnSummary.getPaymentAmountByRegister("CREDIT_MEMO_ISSUE"));
      tb.doSetCreditMemoTendered(TxnSummary.getPaymentAmountByRegister("CREDIT_MEMO"));
      //ks: how do we handle Gift cert item id??
      tb.doSetGiftCertificateIssued(TxnSummary.getSaleAmountByItemId("0042"));
      tb.doSetGiftCertificateTendered(TxnSummary.getPaymentAmountByRegister("GIFT_CERT"));
      tb.doSetCMSRedeemableTotal(TxnSummary.getPaymentAmountByRegister("REDEEMABLE"));
      //ks: replaced 0043 = storeValueCardId. Commented from add because store value card value cannot be modifed for aramni
      //      ArmCurrency totalStoreValueCardAmount = TxnSummary.getSaleAmountByItemId(storeValueCardId).add(TxnSummary.getSaleAmountByItemId("0044"));
      ArmCurrency totalStoreValueCardAmount = TxnSummary.getSaleAmountByItemId(storeValueCardId);
      tb.doSetStoreValueCardIssued(totalStoreValueCardAmount);
      tb.doSetStoreValueCardTendered(TxnSummary.getPaymentAmountByRegister("STORE_VALUE_CARD"));
      tb.doSetMfrCouponTendered(TxnSummary.getPaymentAmountByRegister("MFR_COUPON"));
      tb.doSetMoneyOrderTendered(TxnSummary.getPaymentAmountByRegister("MONEY_ORDER"));
      tb.doSetTravelersCheckTendered(TxnSummary.getPaymentAmountByRegister("TRAVERLS_CHECK"));
      //transactions
      tb.doSetSaleTotal(TxnSummary.getTypeAmountByRegister("SALE"));
      tb.doSetCollectionTotal(TxnSummary.getTypeAmountByRegister("COLL"));
      tb.doSetLayawaySaleTotal(TxnSummary.getTypeAmountByRegister("LAWI"));
      tb.doSetLayawayPaymentTotal(TxnSummary.getTypeAmountByRegister("LAWP"));
      tb.doSetPaidoutTotal(TxnSummary.getTypeAmountByRegister("PDOT|MISC_PAID_OUT"));
      tb.doSetReturnTotal(TxnSummary.getTypeAmountByRegister("RETN"));
      tb.doSetLayawayRTSTotal(TxnSummary.getTypeAmountByRegister("LAWR"));
      tb.doSetVoidTotal(TxnSummary.getTypeAmountByRegister("VOID"));
      tb.doSetRedeemableBuyBackTotal(TxnSummary.getTypeAmountByRegister("RBBT"));
      //fianacial totals
      tb.doSetReductionTotal(TxnSummary.getReductionAmountByRegister());
      tb.doSetReductions(TxnSummary.getReductionsByRegister());
      tb.doSetSalesTaxTotal(TxnSummary.getSalesTaxAmountByRegister());
      tb.doSetRegionalTaxTotal(TxnSummary.getRegionalTaxAmountByRegister());
      tb.setSaleReturnTotal(TxnSummary.getTypeAmountByRegister("SALE/RETN"));
      // Additional Tender Totals
      tb.setHouseAccountTotal(TxnSummary.getPaymentAmountByRegister("HOUSE_ACCOUNT"));
      tb.setMailCheckTotal(TxnSummary.getPaymentAmountByRegister("MAIL_CHECK"));
      tb.setMallCertTotal(TxnSummary.getPaymentAmountByRegister("GIFT_CERT"));
      //tb.doSetDiscountTotal();
      //tb.doSetMarkdownTotal();
      // Set Paid In totals by reason
      Hashtable paidIns = new Hashtable();
      String[] TxnTypes = TxnSummary.getTypesByRegister();
      if (TxnTypes != null && TxnTypes.length > 0) {
        for (int i = 0; i < TxnTypes.length; i++) {
          System.out.println("##############################Type 11111: " + TxnTypes[i]);
          if (TxnTypes[i].startsWith("COLL|")) {
            String txnType = TxnTypes[i].substring(TxnTypes[i].indexOf("|") + 1);
            paidIns.put("RA_" + txnType, TxnSummary.getTypeAmountByRegister(TxnTypes[i]));
          }
        }
      }
      tb.setPaidInsTotal(paidIns);

      Hashtable paidOuts = new Hashtable();
      TxnTypes = TxnSummary.getTypesByRegister();
      if (TxnTypes != null && TxnTypes.length > 0) {
        for (int i = 0; i < TxnTypes.length; i++) {
          System.out.println("##############################Type 2222: " + TxnTypes[i]);
          if (TxnTypes[i].startsWith("PDOT|")) {
            String txnType = TxnTypes[i].substring(TxnTypes[i].indexOf("|") + 1);
            paidOuts.put(txnType, TxnSummary.getTypeAmountByRegister(TxnTypes[i]));
          }
        }
      }
      tb.setPaidOutsTotal(paidOuts);
      if (((CMSStore)store).isVATEnabled()) {
        System.out.println("test " + CMSTxnSummary_EUR.getVatTotalWithAllVatRate());
        tb.setVATTotalByRates(CMSTxnSummary_EUR.getVatTotalWithAllVatRate());
        tb.setVATInvoiceTotal(CMSTxnSummary_EUR.getTotalVatInvoiceAmt());
        tb.setTaxExemptIdTotal(CMSTxnSummary_EUR.getTaxExemptTotal());
        tb.setVATInvoiceTotalByVatRate(CMSTxnSummary_EUR.getVatInvoiceTotalByVatRate());
        tb.setTaxFreeTotalAmt(CMSTxnSummary_EUR.getTotalTaxFreeAmt());
        tb.setTotReductionAmt(CMSTxnSummary_EUR.getTotalReductionAmt());
        tb.setTotalVATAmount(CMSTxnSummary_EUR.getTotalVATAmount());
      }
      String[] paymentTypes = CMSTxnSummary_EUR.getPaymentsByRegister();
      Hashtable paymentTypetable = new Hashtable();
      Hashtable paymentMediaCountTable = new Hashtable();
      if (paymentTypes != null){
        for (int i = 0; i < paymentTypes.length; i++){
        	System.out.println("Ruchi when paymentTyes are not null:"+paymentTypes[i]);
          ArmCurrency paymentTotalAmt = CMSTxnSummary_EUR.getPaymentAmountByRegister(paymentTypes[i]);
          paymentTypetable.put(paymentTypes[i], paymentTotalAmt);
          Integer mediaCountInt = new Integer(CMSTxnSummary_EUR.getMediaCountByRegister(paymentTypes[i]));
          paymentMediaCountTable.put(paymentTypes[i], mediaCountInt);
        }
      }
      tb.setPaymentTotalsAllType(paymentTypetable);
      tb.setMediaCountAllPaymentType(paymentMediaCountTable);

      //Total itemCount
      Hashtable itemSalestable = TxnSummary.getItemSales();
      Hashtable itemCount = new Hashtable();
      if (itemSalestable != null){
        Enumeration enm = itemSalestable.elements();
        while(enm.hasMoreElements()){
          Vector itemVec = (Vector)enm.nextElement();
          for (int i = 0; itemVec != null && i < itemVec.size(); i++){
            ItemEntry itemEntry = (ItemEntry) itemVec.get(0);
            System.out.println("Item id: " + itemEntry.getItemID() + " qty: " +
                               itemEntry.getItemQty());
            itemCount.put(itemEntry.getItemID(), new Integer(itemEntry.getItemQty()));
          }
        }
      }
      tb.setTotalItemCount(itemCount);
    } catch (Exception ee) {
      System.out.println("Ex " + ee);
      ee.printStackTrace();
      throw ee;
    }
    return tb;
  }

  /**
   * Method returns the operator trial balance
   * @param store Store
   * @param date Date
   * @param oper Employee
   * @return TrialBalance
   * @throws Exception
   */
  private TrialBalance getLocalOperatorTrialBalance(Store store, Date date, Employee oper)
      throws Exception {
    CMSTrialBalance tb = new CMSTrialBalance();
    //payments
    tb.doAddCashTendered(TxnSummary.getPaymentAmountByOperator(oper, "CASH"));
    tb.doAddCashTendered(TxnSummary.getPaymentAmountByOperator(oper, "CAD_CASH"));
    tb.doAddCheckTendered(TxnSummary.getPaymentAmountByOperator(oper, "CHECK"));
    //tb.doAddCheckTendered(TxnSummary.getPaymentAmountByOperator(oper,"CAD_CHECK"));
    tb.doSetBankcardTendered(TxnSummary.getPaymentAmountByOperator(oper, "BCRD"));
    tb.doSetBankcardTendered(TxnSummary.getPaymentAmountByOperator(oper, "MASTER_CARD"));
    tb.doSetBankcardTendered(TxnSummary.getPaymentAmountByOperator(oper, "VISA"));
    tb.doSetAmexTendered(TxnSummary.getPaymentAmountByOperator(oper, "AMEX"));
    tb.doSetDiscoverTendered(TxnSummary.getPaymentAmountByOperator(oper, "DISC"));
    tb.doSetCreditMemoIssued(TxnSummary.getPaymentAmountByOperator(oper, "CREDIT_MEMO_ISSUE"));
    tb.doSetCreditMemoTendered(TxnSummary.getPaymentAmountByOperator(oper, "CREDIT_MEMO"));
    //tb.doSetGiftCertificateIssued(TxnSummary.getPaymentAmountByOperator(oper,""));
    tb.doSetGiftCertificateTendered(TxnSummary.getPaymentAmountByOperator(oper, "GIFT_CERT"));
    tb.doSetMfrCouponTendered(TxnSummary.getPaymentAmountByOperator(oper, "MFR_COUPON"));
    tb.doSetMoneyOrderTendered(TxnSummary.getPaymentAmountByOperator(oper, "MONEY_ORDER"));
    tb.doSetTravelersCheckTendered(TxnSummary.getPaymentAmountByOperator(oper, "TRAVERLS_CHECK"));
    //transactions
    tb.doSetSaleTotal(TxnSummary.getTypeAmountByOperator(oper, "SALE"));
    tb.doSetCollectionTotal(TxnSummary.getTypeAmountByOperator(oper, "COLL"));
    tb.doSetLayawaySaleTotal(TxnSummary.getTypeAmountByOperator(oper, "LAWI"));
    tb.doSetLayawayPaymentTotal(TxnSummary.getTypeAmountByOperator(oper, "LAWP"));
    tb.doSetPaidoutTotal(TxnSummary.getTypeAmountByOperator(oper, "PDOT|MISC_PAID_OUT"));
    tb.doSetReturnTotal(TxnSummary.getTypeAmountByOperator(oper, "RETN"));
    tb.doSetLayawayRTSTotal(TxnSummary.getTypeAmountByOperator(oper, "LAWR"));
    tb.doSetVoidTotal(TxnSummary.getTypeAmountByOperator(oper, "VOID"));
    tb.doSetRedeemableBuyBackTotal(TxnSummary.getTypeAmountByOperator(oper, "RBBT"));
    //fianacial totals
    tb.doSetReductionTotal(TxnSummary.getReductionAmountByOperator(oper));
    tb.doSetReductions(TxnSummary.getReductionsByOperator(oper));
    tb.doSetSalesTaxTotal(TxnSummary.getSalesTaxAmountByOperator(oper));
    tb.doSetRegionalTaxTotal(TxnSummary.getRegionalTaxAmountByOperator(oper));
    tb.setSaleReturnTotal(TxnSummary.getTypeAmountByOperator(oper, "SALE/RETN"));
    //tb.doSetDiscountTotal();
    //tb.doSetMarkdownTotal();
    // Additional Tender Totals
    tb.setHouseAccountTotal(TxnSummary.getPaymentAmountByOperator(oper, "HOUSE_ACCOUNT"));
    tb.setMailCheckTotal(TxnSummary.getPaymentAmountByOperator(oper, "MAIL_CHECK"));
    tb.setMallCertTotal(TxnSummary.getPaymentAmountByOperator(oper, "GIFT_CERT"));
    // Set Paid In totals by reason
    Hashtable paidIns = new Hashtable();
    String[] TxnTypes = TxnSummary.getTypesByRegister();
    if (TxnTypes != null && TxnTypes.length > 0) {
      for (int i = 0; i < TxnTypes.length; i++) {
        System.out.println("##############################Type : " + TxnTypes[i]);
        if (TxnTypes[i].startsWith("COLL|")) {
          String txnType = TxnTypes[i].substring(TxnTypes[i].indexOf("|") + 1);
          paidIns.put("RA_" + txnType, TxnSummary.getTypeAmountByOperator(oper, TxnTypes[i]));
        }
      }
    }
    tb.setPaidInsTotal(paidIns);
    if (((CMSStore)store).isVATEnabled()) {
      tb.setVATTotalByRates(CMSTxnSummary_EUR.getVatTotalWithAllVatRatesByOperator(oper));
      tb.setVATInvoiceTotal(CMSTxnSummary_EUR.getTotalVatInvoiceAmtByOperator(oper));
      tb.setTaxExemptIdTotal(CMSTxnSummary_EUR.getTaxExemptTotalByOperator(oper));
      tb.setVATInvoiceTotalByVatRate(CMSTxnSummary_EUR.getVatInvoiceTotalByVatRateAndOperator(oper));
      tb.setTaxFreeTotalAmt(CMSTxnSummary_EUR.getTotalTaxFreeAmtByOperator(oper));
      tb.setTotReductionAmt(CMSTxnSummary_EUR.getTotalReductionAmtByOper(oper));
    }
    String[] paymentTypes = CMSTxnSummary_EUR.getPaymentsByRegister();
    Hashtable paymentTypetable = new Hashtable();
    Hashtable paymentMediaCountTable = new Hashtable();
    if (paymentTypes != null){
      for (int i = 0; i < paymentTypes.length; i++){
        //ArmCurrency paymentTotalAmt = CMSTxnSummary_EUR.getPaymentAmountByRegister(paymentTypes[i]);
    	  System.out.println("Ruchi printing paymentTypes CMSReadingRMIClient :"+paymentTypes[i]);
    	 ArmCurrency paymentTotalAmt = CMSTxnSummary.getPaymentAmountByRegister(paymentTypes[i]);
        paymentTypetable.put(paymentTypes[i], paymentTotalAmt);
       // Integer mediaCountInt = new Integer(CMSTxnSummary_EUR.getMediaCountByRegister(paymentTypes[i]));
        Integer mediaCountInt = new Integer(CMSTxnSummary.getMediaCountByRegister(paymentTypes[i]));
        paymentMediaCountTable.put(paymentTypes[i], mediaCountInt);
      }
    }
    tb.setPaymentTotalsAllType(paymentTypetable);
    tb.setMediaCountAllPaymentType(paymentMediaCountTable);

    //Total itemCount
    Hashtable itemSalestable = TxnSummary.getItemSales();
    Hashtable itemCount = new Hashtable();
    if (itemSalestable != null){
      Enumeration enm = itemSalestable.elements();
      while(enm.hasMoreElements()){
        Vector itemVec = (Vector)enm.nextElement();
        for (int i = 0; itemVec != null && i < itemVec.size(); i++){
          ItemEntry itemEntry = (ItemEntry) itemVec.get(0);
          System.out.println("Item id: " + itemEntry.getItemID() + " qty: " +
                             itemEntry.getItemQty());
          itemCount.put(itemEntry.getItemID(), new Integer(itemEntry.getItemQty()));
        }
      }
    }
    tb.setTotalItemCount(itemCount);

    return tb;
  }
}

