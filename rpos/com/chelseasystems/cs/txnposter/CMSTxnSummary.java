/*
 * put your module comment here
 * formatted with JxBeauty (c) johann.langhofer@nextra.at
 */
//
// Copyright 1999-2001, Chelsea Market Systems
//
/*
 History:
 +------+------------+-----------+-----------+----------------------------------------------------+
 | Ver# | Date       | By        | Defect #  | Description                                        |
 +------+------------+-----------+-----------+----------------------------------------------------+
 | 1    | 04-29-2005 | Pankaja   | N/A       |To compute the media count                          |
 +------+------------+-----------+-----------+----------------------------------------------------+
 */


package com.chelseasystems.cs.txnposter;

import com.chelseasystems.cr.txnposter.*;
import com.chelseasystems.cr.config.ConfigMgr;
import com.chelseasystems.cr.config.FileMgr;
import com.chelseasystems.cr.currency.*;
import com.chelseasystems.cr.employee.Employee;
import com.chelseasystems.cr.item.Item;
import com.chelseasystems.cs.item.CMSItem;
import com.chelseasystems.cr.paidout.PaidOutTransaction;
import com.chelseasystems.cr.payment.Cash;
import com.chelseasystems.cr.payment.Payment;
import com.chelseasystems.cr.payment.GiftCert;
import com.chelseasystems.cs.payment.*;
import com.chelseasystems.cr.pos.*;
import com.chelseasystems.cs.pos.*;
import com.chelseasystems.cr.transaction.CommonTransaction;
import com.chelseasystems.cr.util.ObjectStore;
import javax.swing.Timer;
import java.io.*;
import java.util.*;

import com.chelseasystems.cs.collection.CMSMiscCollection;


/**
 * <p>Title: </p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2004</p>
 * <p>Company: </p>
 * @author unascribed
 * @version 1.0
 */
public class CMSTxnSummary extends TxnSummary implements TxnPostedListener {
  private static int timeIncrement;
  private static final String TOTAL = "TOTAL";
  private static transient Timer salesTimer;

  /**
   * put your documentation comment here
   */
  public CMSTxnSummary() {
    super();
  }

  /**
   * put your documentation comment here
   * @param event
   */
  public void transactionPosted(TxnPostedEvent event) {
    if (event.getTransaction() instanceof PaymentTransaction) {
      addTransaction((PaymentTransaction)event.getTransaction());
    }
  }

  /**
   * put your documentation comment here
   * @param aTxn
   */
  public static void addTransaction(PaymentTransaction aTxn) {
    try {
      updateTxnPaySummary(aTxn);
      updateTxnTypeSummary(aTxn);
      updateSalesPeriodSummary(aTxn);
      updateTxnReductionSummary(aTxn);
      updateTxnSalesTaxSummary(aTxn);
      updateItemSalesSummary(aTxn);
    } catch (Exception ex) {
      System.out.println("Exception addTransaction()->" + ex);
    }
  }

  /**
   * put your documentation comment here
   * @param aTxn
   */
  private static void updateTxnPaySummary(PaymentTransaction aTxn) {
    try {
      Hashtable htPayment = readTxnPaySum();
      Vector vPayment = null;
      if (!htPayment.containsKey(aTxn.getTheOperator().getId())) {
        vPayment = new Vector();
        htPayment.put(aTxn.getTheOperator().getId(), vPayment);
      }
      vPayment = (Vector)htPayment.get(aTxn.getTheOperator().getId());
      if (aTxn instanceof VoidTransaction) {
        com.chelseasystems.cr.transaction.ITransaction origTxn = ((VoidTransaction)aTxn).
            getOriginalTransaction();
        if (origTxn instanceof PaymentTransaction) {
          PaymentTransaction payTxn = (PaymentTransaction)origTxn;
          Payment pays[] = payTxn.getPaymentsArray();
          int x = 0;
          do {
            if (x >= pays.length)
              break;
            boolean isFound = false;
            Enumeration enm = vPayment.elements();
            do {
              if (!enm.hasMoreElements())
                break;
              CMSMediaEntry entry = (CMSMediaEntry)enm.nextElement();
              System.out.println("Ruchi inside updateTxnPaySummary   :"+pays[x].getTransactionPaymentName());
              System.out.println("Ruchi inside updateTxnPaySummary   :"+entry.getPaymentID());
              if (!entry.getPaymentID().equals(pays[x].getTransactionPaymentName()))
                continue;
              isFound = true;
              ArmCurrency amountToAdd = pays[x].getAmount();
              if (pays[x].isForeign())
                amountToAdd = amountToAdd.getConvertedFrom();
              entry.addAmount(amountToAdd.multiply( -1));
              entry.setMediaCount(entry.getMediaCount() - 1);
              break;
            } while (true); if (!isFound) {
              CMSMediaEntry entry = new CMSMediaEntry(pays[x]);
              ArmCurrency amountToAdd = pays[x].getAmount();
              if (pays[x].isForeign())
                amountToAdd = amountToAdd.getConvertedFrom().multiply( -1);
              entry.addAmount(amountToAdd.multiply( -1));
              entry.setMediaCount(entry.getMediaCount() - 1);
              vPayment.addElement(entry);
            }
            x++;
          } while (true);
        }
      } else {
        Payment pays[] = aTxn.getPaymentsArray();
        for (int x = 0; x < pays.length; x++) {
          System.out.println("Payment Object : " + pays[x].getClass().getName());
          boolean isFound = false;
          Enumeration enm = vPayment.elements();
          do {
            if (!enm.hasMoreElements())
              break;
            CMSMediaEntry entry = (CMSMediaEntry)enm.nextElement();
            System.out.println("Ruchi CMSMediaEntry   :"+pays[x].getTransactionPaymentName());
            System.out.println("entry.getPaymentID()   :"+entry.getPaymentID());
            if (!entry.getPaymentID().equals(pays[x].getTransactionPaymentName()))
              continue;
            isFound = true;
            System.out.println("Ruchi CMSMediaEntry   :"+pays[x].getTransactionPaymentName());
            ArmCurrency amountToAdd = pays[x].getAmount();
            if (pays[x].isForeign())
              amountToAdd = amountToAdd.getConvertedFrom();
            entry.addAmount(amountToAdd);
            if (!aTxn.getTransactionType().equals("RETN"))
              entry.setMediaCount(entry.getMediaCount() + 1);
            break;
          } while (true); if (!isFound) {
            CMSMediaEntry entry = new CMSMediaEntry(pays[x]);
            ArmCurrency amountToAdd = pays[x].getAmount();
            if (pays[x].isForeign())
              amountToAdd = amountToAdd.getConvertedFrom();
            entry.addAmount(amountToAdd);
            if (!aTxn.getTransactionType().equals("RETN"))
              entry.setMediaCount(entry.getMediaCount() + 1);
            vPayment.addElement(entry);
          }
        }
      }
      writeTxnPaySum(htPayment);
      displayPayments(htPayment);
    } catch (Exception ex) {
      System.out.println("Exception updateTxnPaySummary()->".concat(String.valueOf(String.valueOf(
          ex))));
      ex.printStackTrace();
    }
  }

  /**
   * put your documentation comment here
   * @return
   */
  private static Hashtable readTxnPaySum() {
    try {
      ObjectStore store = new ObjectStore(FileMgr.getLocalFile("tmp", "txn_pay_sum.ser"));
      Hashtable ht = (Hashtable)store.read();
      if (ht == null) {
        return new Hashtable();
      } else {
        return ht;
      }
    } catch (Exception ex) {
      System.out.println("Exception readTxnPaySum()->" + ex);
    }
    return new Hashtable();
  }

  /**
   * put your documentation comment here
   * @param ht
   */
  private static void writeTxnPaySum(Hashtable ht) {
    try {
    	System.out.println("Ruchi inside writeTxnPaySum  :"+ht);
      ObjectStore store = new ObjectStore(FileMgr.getLocalFile("tmp", "txn_pay_sum.ser"));
      store.write(ht);
    } catch (Exception ex) {
      System.out.println("Exception writeTxnPaySum()->" + ex);
    }
  }

  /**
   * put your documentation comment here
   * @param htPayment
   */
  private static void displayPayments(Hashtable htPayment) { /*
        for(Enumeration enm = htPayment.keys(); enm.hasMoreElements();)
        {
        String oprID = (String)enm.nextElement();
        System.out.println("Operator: " + oprID);
        Hashtable hPaymentByOpr = (Hashtable)htPayment.get(oprID);
        for(Enumeration enumMedia = hPaymentByOpr.keys(); enumMedia.hasMoreElements(); )
        {
        CMSMediaEntry mediaEntry = (CMSMediaEntry)hPaymentByOpr.get(enumMedia.nextElement());
        System.out.println("CMSTxnSummary:Payment: " + mediaEntry.getPaymentDescription() + " CMSTxnSummary:Amount: " + mediaEntry.getTotalAmount());
        System.out.println("CMSTxnSummary:getMediaCount: " + mediaEntry.getMediaCount());
        }
        }
     */
   }

  /**
   * put your documentation comment here
   * @param emp
   * @param type
   * @return
   */
  public static int getMediaCountByOperator(Employee emp, String type) {
    Hashtable htPayment = readTxnPaySum();
    Vector vPayment = (Vector)htPayment.get(emp.getId());
    if (vPayment == null)
      return 0;
    for (Enumeration enm = vPayment.elements(); enm.hasMoreElements(); ) {
      CMSMediaEntry media = (CMSMediaEntry)enm.nextElement();
      if (media.getPaymentID().equals(type))
        return media.getMediaCount();
    }
    return 0;
  }

  /**
   * put your documentation comment here
   * @param type
   * @return
   */
  public static int getMediaCountByRegister(String type) {
    int result = 0;
    try {
      Hashtable htPayment = readTxnPaySum();
      for (Enumeration enm = htPayment.elements(); enm.hasMoreElements(); ) {
        Vector vMedia = (Vector)enm.nextElement();
        for (Enumeration enumMedia = vMedia.elements(); enumMedia.hasMoreElements(); ) {
          CMSMediaEntry media = (CMSMediaEntry)enumMedia.nextElement();
          if (media.getPaymentID().equals(type))
            result = (result == 0) ? media.getMediaCount() : result + media.getMediaCount();
        }
      }
      if (result > 0) {
        return result;
      }
    } catch (Exception ex) {
      System.out.println("Exception getMediaCountByRegister()->".concat(String.valueOf(String.
          valueOf(ex))));
    }
    return result;
  }

  /**
   * put your documentation comment here
   * @param emp
   * @return
   */
  public static String[] getPaymentsByOperator(Employee emp) {
    Hashtable htPayment = readTxnPaySum();
    Vector vPayment = (Vector)htPayment.get(emp.getId());
    if (vPayment == null)
      return new String[0];
    String result[] = new String[vPayment.size()];
    int x = 0;
    for (Enumeration enm = vPayment.elements(); enm.hasMoreElements(); ) {
      result[x] = (String)enm.nextElement();
      x++;
    }
    return result;
  }

  /**
   * put your documentation comment here
   * @return
   */
  public static String[] getPaymentsByRegister() {
    Vector vAllPay = new Vector();
    Hashtable htPayment = readTxnPaySum();
    System.out.println("Ruchi inside CMSTxnSummary table   :"+htPayment);
    for (Enumeration enm = htPayment.elements(); enm.hasMoreElements(); ) {
      Vector vMedia = (Vector)enm.nextElement();
      for (Enumeration enumMedia = vMedia.elements(); enumMedia.hasMoreElements(); ) {
        MediaEntry media = (MediaEntry)enumMedia.nextElement();
        System.out.println("Ruchi printing payment ID  :"+media.getPaymentID());
        if (!vAllPay.contains(media.getPaymentID()))
          vAllPay.add(media.getPaymentID());
      }
    }
    String result[] = new String[vAllPay.size()];
    vAllPay.copyInto(result);
    return result;
  }

  /**
   * put your documentation comment here
   */
  public static void clear() {
    try {
      TxnSummary.clear();
    } catch (Exception ex) {
      System.out.println("Exception clear()->" + ex);
    }
  }

  /**
   * put your documentation comment here
   * @param aTxn
   */
  private static void updateTxnTypeSummary(PaymentTransaction aTxn) {
    try {
      Hashtable htType = readTxnTypeSum();
      Vector vType = null;
      if (!htType.containsKey(aTxn.getTheOperator().getId())) {
        vType = new Vector();
        htType.put(aTxn.getTheOperator().getId(), vType);
      }
      vType = (Vector)htType.get(aTxn.getTheOperator().getId());
      String txnType = aTxn.getTransactionType();
      if (txnType.equalsIgnoreCase("PDOT")) {
        txnType = txnType + "|" + ((PaidOutTransaction)aTxn).getType();
      }
      System.out.println("THE TXN TYPE IS: " + txnType);
      if (txnType.equalsIgnoreCase("SALE/RETN")) {
        //addToOperatorsVector(htType, vType, "SALE", ((CompositePOSTransaction)aTxn).getSaleNetAmount());
        //addToOperatorsVector(htType, vType, "RETN", ((CompositePOSTransaction)aTxn).getReturnNetAmount());
        addToOperatorsVector(htType, vType, "SALE/RETN"
            , ((CompositePOSTransaction)aTxn).getCompositeNetAmount());
      } else if (txnType.equalsIgnoreCase("SALE")) {
        addToOperatorsVector(htType, vType, txnType
            , ((CompositePOSTransaction)aTxn).getSaleNetAmount());
      } else if (txnType.equalsIgnoreCase("RETN")) {
        addToOperatorsVector(htType, vType, txnType
            , ((CompositePOSTransaction)aTxn).getReturnNetAmount());
      } else if (aTxn instanceof VoidTransaction) {
        addToOperatorsVector(htType, vType, txnType
            , ((VoidTransaction)aTxn).getVoidedAmount().multiply( -1));
        // adjust txn type amounts
        com.chelseasystems.cr.transaction.ITransaction origTxn = ((VoidTransaction)aTxn).
            getOriginalTransaction();
        if (origTxn instanceof CompositePOSTransaction) {
          CompositePOSTransaction cPOSTxn = (CompositePOSTransaction)origTxn;
          if (cPOSTxn.getTransactionType().equals("SALE")) {
            addToOperatorsVector(htType, vType, cPOSTxn.getTransactionType()
                , cPOSTxn.getSaleNetAmount().multiply( -1));
          }
          if (cPOSTxn.getTransactionType().equals("RETN")) {
            addToOperatorsVector(htType, vType, cPOSTxn.getTransactionType()
                , cPOSTxn.getReturnNetAmount().multiply( -1));
          }
          if (cPOSTxn.getTransactionType().equals("SALE/RETN")) {
            addToOperatorsVector(htType, vType, cPOSTxn.getTransactionType()
                , cPOSTxn.getCompositeNetAmount().multiply( -1));
          }
        }
      } else if (aTxn instanceof CMSMiscCollection) {
        addToOperatorsVector(htType, vType, txnType + "|" + ((CMSMiscCollection)aTxn).getType()
            , aTxn.getTotalPaymentAmount());
      } else {
        addToOperatorsVector(htType, vType, txnType, aTxn.getTotalPaymentAmount());
      }
    } catch (Exception ex) {
      System.out.println("Exception updateTxnPaySummary()->" + ex);
    }
  }

  /**
   * put your documentation comment here
   * @param htType
   * @param vType
   * @param txnType
   * @param amount
   */
  private static void addToOperatorsVector(Hashtable htType, Vector vType, String txnType
      , ArmCurrency amount) {
    boolean isFound = false;
    for (Enumeration enm = vType.elements(); enm.hasMoreElements(); ) {
      TxnTypeEntry entry = (TxnTypeEntry)enm.nextElement();
      if (entry.getTxnType().equals(txnType)) {
        isFound = true;
        entry.addAmount(amount);
        break;
      }
    }
    if (!isFound) {
      TxnTypeEntry entry = new TxnTypeEntry(txnType);
      entry.addAmount(amount);
      vType.addElement(entry);
    }
    writeTxnTypeSum(htType);
    displayTypes(htType);
  }

  /**
   * put your documentation comment here
   * @param ht
   */
  private static void writeTxnTypeSum(Hashtable ht) {
    try {
      ObjectStore store = new ObjectStore(FileMgr.getLocalFile("tmp", "txn_type_sum.ser"));
      System.out.println("Ruchi writing in the object :"+ht);
      store.write(ht);
    } catch (Exception ex) {
      System.out.println("Exception writeTxnTypeSum()->" + ex);
    }
  }

  /**
   * put your documentation comment here
   * @param htTypes
   */
  private static void displayTypes(Hashtable htTypes) {
    for (Enumeration enm = htTypes.keys(); enm.hasMoreElements(); ) {
      String oprID = (String)enm.nextElement();
      System.out.println("Operator: " + oprID);
      Vector vTypes = (Vector)htTypes.get(oprID);
      TxnTypeEntry entry;
      for (Enumeration enumType = vTypes.elements(); enumType.hasMoreElements();
          System.out.println("Types: " + entry.getTxnType() + " Amount: "
          + entry.getTotalAmount().formattedStringValue())) {
        entry = (TxnTypeEntry)enumType.nextElement();
      }
    }
  }

  /**
   * put your documentation comment here
   * @return
   */
  private static Hashtable readTxnTypeSum() {
    try {
      ObjectStore store = new ObjectStore(FileMgr.getLocalFile("tmp", "txn_type_sum.ser"));
      Hashtable ht = (Hashtable)store.read();
      if (ht == null) {
        return new Hashtable();
      } else {
        return ht;
      }
    } catch (Exception ex) {
      System.out.println("Exception readTxnTypeSum()->" + ex);
    }
    return new Hashtable();
  }

  /**
   * put your documentation comment here
   * @param aTxn
   */
  private static void updateSalesPeriodSummary(PaymentTransaction aTxn) {
    try {
      TreeMap tmSalesPeriodSummary = readSalesPeriodSum();
      ArmCurrency periodTotal = new ArmCurrency(0.0D);
      Date period = getCurrentDatePeriod();
      if (!tmSalesPeriodSummary.containsKey(period)) {
        tmSalesPeriodSummary.put(period, periodTotal);
      }
      periodTotal = (ArmCurrency)tmSalesPeriodSummary.get(period);
      if (aTxn != null) {
        periodTotal = periodTotal.add(aTxn.getTotalPaymentAmount());
        tmSalesPeriodSummary.put(period, periodTotal);
      }
      writeSalesPeriodSum(tmSalesPeriodSummary);
    } catch (Exception ex) {
      System.out.println("Exception updateSalesPeriodSummary()->" + ex);
    }
  }

  /**
   * put your documentation comment here
   * @return
   */
  private static TreeMap readSalesPeriodSum() {
    try {
      ObjectStore store = new ObjectStore(FileMgr.getLocalFile("tmp", "txn_sales_per.ser"));
      TreeMap tm = (TreeMap)store.read();
      if (tm == null) {
        return new TreeMap();
      } else {
        return tm;
      }
    } catch (Exception ex) {
      System.out.println("Exception readSalesPeriodSum()->" + ex);
    }
    return new TreeMap();
  }

  /**
   * put your documentation comment here
   * @param tm
   */
  private static void writeSalesPeriodSum(TreeMap tm) {
    try {
      ObjectStore store = new ObjectStore(FileMgr.getLocalFile("tmp", "txn_sales_per.ser"));
      store.write(tm);
    } catch (Exception ex) {
      System.out.println("Exception writeTxnPaySum()->" + ex);
    }
  }

  /**
   * put your documentation comment here
   * @param aTxn
   */
  private static void updateTxnReductionSummary(PaymentTransaction aTxn) {
    try {
      Hashtable htTxnReductionSummary = readTxnReductionSum();
      Hashtable reductionTable = new Hashtable();
      if (!htTxnReductionSummary.containsKey(aTxn.getTheOperator().getId())) {
        reductionTable = new Hashtable();
        htTxnReductionSummary.put(aTxn.getTheOperator().getId(), reductionTable);
      } else {
        reductionTable = (Hashtable)htTxnReductionSummary.get(aTxn.getTheOperator().getId());
      }
      Hashtable newReductionTable = null;
      if (aTxn instanceof VoidTransaction) {
        com.chelseasystems.cr.transaction.ITransaction origTransaction = ((VoidTransaction)aTxn).
            getOriginalTransaction();
        if (origTransaction instanceof CompositePOSTransaction) {
          newReductionTable = getReductions((PaymentTransaction)origTransaction, true);
        }
      } else if (aTxn instanceof CompositePOSTransaction) {
        newReductionTable = getReductions(aTxn, false);
      }
      if (newReductionTable != null) {
        for (Enumeration enm = newReductionTable.keys(); enm.hasMoreElements(); ) {
          Object key = enm.nextElement();
          if (reductionTable.containsKey(key)) {
            ArmCurrency oldTotal = (ArmCurrency)reductionTable.get(key);
            ArmCurrency newReduction = (ArmCurrency)newReductionTable.get(key);
            ArmCurrency total = oldTotal.add(newReduction);
            reductionTable.put(key, total);
          } else {
            reductionTable.put(key, newReductionTable.get(key));
          }
        }
        htTxnReductionSummary.put(aTxn.getTheOperator().getId(), reductionTable);
        writeTxnReductionSum(htTxnReductionSummary);
      }
    } catch (Exception ex) {
      System.out.println("Exception updateTxnReductionSummary()->" + ex);
    }
  }

  /**
   * put your documentation comment here
   * @param aTxn
   */
  private static void updateTxnSalesTaxSummary(PaymentTransaction aTxn) {
    try {
      Hashtable htTxnSalesTaxSummary = readTxnSalesTaxSum();
      ArmCurrency taxes[];
      if (!htTxnSalesTaxSummary.containsKey(aTxn.getTheOperator().getId())) {
        taxes = (new ArmCurrency[] {new ArmCurrency(0.0D), new ArmCurrency(0.0D)
        });
        htTxnSalesTaxSummary.put(aTxn.getTheOperator().getId(), taxes);
      } else {
        taxes = (ArmCurrency[])htTxnSalesTaxSummary.get(aTxn.getTheOperator().getId());
      }
      if (aTxn instanceof VoidTransaction) {
        com.chelseasystems.cr.transaction.ITransaction origTxn = ((VoidTransaction)aTxn).
            getOriginalTransaction();
        if (origTxn instanceof CompositePOSTransaction) {
          CompositePOSTransaction compPosTxn = (CompositePOSTransaction)origTxn;
          for (Enumeration enm = compPosTxn.getSaleLineItems(); enm.hasMoreElements(); ) {
            POSLineItem line = (POSLineItem)enm.nextElement();
            taxes[0] = taxes[0].subtract(line.getExtendedTaxAmount());
            taxes[1] = taxes[1].subtract(line.getExtendedRegionalTaxAmount());
          }
          for (Enumeration enm = compPosTxn.getReturnLineItems(); enm.hasMoreElements(); ) {
            POSLineItem line = (POSLineItem)enm.nextElement();
            taxes[0] = taxes[0].add(line.getExtendedTaxAmount());
            taxes[1] = taxes[1].add(line.getExtendedRegionalTaxAmount());
          }
        }
      } else if (aTxn instanceof CompositePOSTransaction) {
        CompositePOSTransaction compPosTxn = (CompositePOSTransaction)aTxn;
        for (Enumeration enm = compPosTxn.getSaleLineItems(); enm.hasMoreElements(); ) {
          POSLineItem line = (POSLineItem)enm.nextElement();
          taxes[0] = taxes[0].add(line.getExtendedTaxAmount());
          taxes[1] = taxes[1].add(line.getExtendedRegionalTaxAmount());
        }
        for (Enumeration enm = compPosTxn.getReturnLineItems(); enm.hasMoreElements(); ) {
          POSLineItem line = (POSLineItem)enm.nextElement();
          taxes[0] = taxes[0].subtract(line.getExtendedTaxAmount());
          taxes[1] = taxes[1].subtract(line.getExtendedRegionalTaxAmount());
        }
      }
      htTxnSalesTaxSummary.put(aTxn.getTheOperator().getId(), taxes);
      writeTxnSalesTaxSum(htTxnSalesTaxSummary);
    } catch (Exception ex) {
      System.out.println("Exception updateTxnSalesTaxSummary()->" + ex);
    }
  }

  /**
   * put your documentation comment here
   * @param aTxn
   */
  private static void updateItemSalesSummary(PaymentTransaction aTxn) {
    System.out.println("start TxnSummary.updateItemSalesSummary()");
    try {
      Hashtable htPayment = readItemSalesSum();
      Vector vPayment = null;
      if (aTxn instanceof VoidTransaction) {
        com.chelseasystems.cr.transaction.ITransaction origTxn = ((VoidTransaction)aTxn).
            getOriginalTransaction();
        if (origTxn instanceof CompositePOSTransaction) {
          CompositePOSTransaction compPosTxn = (CompositePOSTransaction)origTxn;
          POSLineItem lines[] = compPosTxn.getLineItemsArray();
          for (int x = 0; x < lines.length; x++) {
            if (!htPayment.containsKey(lines[x].getItem().getId())) {
              vPayment = new Vector();
              htPayment.put(lines[x].getItem().getId(), vPayment);
            }
            vPayment = (Vector)htPayment.get(lines[x].getItem().getId());
            boolean isFound = false;
            for (Enumeration enm = vPayment.elements(); enm.hasMoreElements(); ) {
              ItemEntry entry = (ItemEntry)enm.nextElement();
              if (entry.getItemID().equals(lines[x].getItem().getId())) {
                isFound = true;
                entry.addItemQty(lines[x].getQuantity().intValue() * -1);
                if (lines[x].getItem().isRedeemable()) {
                  entry.addItemAmount(lines[x].getItemRetailPrice().multiply( -1));
                } else {
                  entry.addItemAmount(lines[x].getExtendedNetAmount().multiply( -1));
                }
                break;
              }
            }
            if (!isFound) {
              ItemEntry entry = new ItemEntry(lines[x].getItem());
              entry.addItemQty(lines[x].getQuantity().intValue() * -1);
              if (lines[x].getItem().isRedeemable()) {
                entry.addItemAmount(lines[x].getItemRetailPrice().multiply( -1));
              } else {
                entry.addItemAmount(lines[x].getExtendedNetAmount().multiply( -1));
              }
              vPayment.addElement(entry);
            }
          }
        }
      } else if (aTxn instanceof CompositePOSTransaction) {
        CompositePOSTransaction compPosTxn = (CompositePOSTransaction)aTxn;
        POSLineItem lines[] = compPosTxn.getLineItemsArray();
        for (int x = 0; x < lines.length; x++) {
          if (!(lines[x] instanceof LayawayLineItem)) {
            if (!htPayment.containsKey(lines[x].getItem().getId())) {
              vPayment = new Vector();
              htPayment.put(lines[x].getItem().getId(), vPayment);
            }
            vPayment = (Vector)htPayment.get(lines[x].getItem().getId());
            boolean isFound = false;
            for (Enumeration enm = vPayment.elements(); enm.hasMoreElements(); ) {
              ItemEntry entry = (ItemEntry)enm.nextElement();
              if (entry.getItemID().equals(lines[x].getItem().getId())) {
                isFound = true;
                entry.addItemQty(lines[x].getQuantity().intValue());
                if (lines[x].getItem().isRedeemable()) {
                  entry.addItemAmount(lines[x].getItemRetailPrice());
                } else {
                  entry.addItemAmount(lines[x].getExtendedNetAmount());
                }
                break;
              }
            }
            if (!isFound) {
              ItemEntry entry = new ItemEntry(lines[x].getItem());
              entry.addItemQty(lines[x].getQuantity().intValue());
              if (lines[x].getItem().isRedeemable()) {
                entry.addItemAmount(lines[x].getItemRetailPrice());
              } else {
                entry.addItemAmount(lines[x].getExtendedNetAmount());
              }
              vPayment.addElement(entry);
            }
          }
        }
      }
      writeItemSalesSum(htPayment);
    } catch (Exception ex) {
      System.out.println("Exception updateItemSalesSummary()->" + ex);
    }
  }

  /**
   * put your documentation comment here
   * @param ht
   */
  private static void writeItemSalesSum(Hashtable ht) {
    try {
      ObjectStore store = new ObjectStore(FileMgr.getLocalFile("tmp", "txn_item_sum.ser"));
      store.write(ht);
    } catch (Exception ex) {
      System.out.println("Exception writeItemTypeSum()->" + ex);
    }
  }

  /**
   * put your documentation comment here
   * @return
   */
  private static Hashtable readTxnReductionSum() {
    try {
      ObjectStore store = new ObjectStore(FileMgr.getLocalFile("tmp", "txn_reduction_sum.ser"));
      Hashtable ht = (Hashtable)store.read();
      if (ht == null) {
        return new Hashtable();
      } else {
        return ht;
      }
    } catch (Exception ex) {
      System.out.println("Exception readTxnReductionSum()->" + ex);
    }
    return new Hashtable();
  }

  /**
   * put your documentation comment here
   * @param ht
   */
  private static void writeTxnReductionSum(Hashtable ht) {
    try {
      ObjectStore store = new ObjectStore(FileMgr.getLocalFile("tmp", "txn_reduction_sum.ser"));
      store.write(ht);
    } catch (Exception ex) {
      System.out.println("Exception writeTxnReductionSum()->" + ex);
    }
  }

  /**
   * put your documentation comment here
   * @return
   */
  private static Hashtable readTxnSalesTaxSum() {
    try {
      ObjectStore store = new ObjectStore(FileMgr.getLocalFile("tmp", "txn_salestax_sum.ser"));
      Hashtable ht = (Hashtable)store.read();
      if (ht == null) {
        return new Hashtable();
      } else {
        return ht;
      }
    } catch (Exception ex) {
      System.out.println("Exception readTxnSalesTaxSum()->" + ex);
    }
    return new Hashtable();
  }

  /**
   * put your documentation comment here
   * @param ht
   */
  private static void writeTxnSalesTaxSum(Hashtable ht) {
    try {
      ObjectStore store = new ObjectStore(FileMgr.getLocalFile("tmp", "txn_salestax_sum.ser"));
      store.write(ht);
    } catch (Exception ex) {
      System.out.println("Exception writeTxnSalesTaxSum()->" + ex);
    }
  }

  /**
   * put your documentation comment here
   * @return
   */
  private static Hashtable readItemSalesSum() {
    try {
      ObjectStore store = new ObjectStore(FileMgr.getLocalFile("tmp", "txn_item_sum.ser"));
      Hashtable ht = (Hashtable)store.read();
      if (ht == null) {
        return new Hashtable();
      } else {
        return ht;
      }
    } catch (Exception ex) {
      System.out.println("Exception readItemSalesSum()->" + ex);
    }
    return new Hashtable();
  }

  /**
   * put your documentation comment here
   * @return
   */
  private static Date getCurrentDatePeriod() {
    Calendar cal = Calendar.getInstance();
    cal.set(14, 0);
    cal.set(13, 0);
    int dif = cal.get(12) % timeIncrement;
    cal.add(12, -dif);
    return cal.getTime();
  }

  /**
   * put your documentation comment here
   * @param transaction
   * @param negate
   * @return
   */
  private static Hashtable getReductions(PaymentTransaction transaction, boolean negate) {
    Hashtable retVal = new Hashtable(10);
    CurrencyType baseCurrencyType = transaction.getBaseCurrencyType();
    if (transaction instanceof CompositePOSTransaction) {
      CompositePOSTransaction compositeTransaction = (CompositePOSTransaction)transaction;
      try {
        Enumeration saleEnum = compositeTransaction.getSaleLineItems();
        ArmCurrency saleLineItemReductions[] = getLineItemReductions(saleEnum, baseCurrencyType);
        Enumeration returnEnum = compositeTransaction.getReturnLineItems();
        ArmCurrency returnLineItemReductions[] = getLineItemReductions(returnEnum, baseCurrencyType);
        ArmCurrency total = saleLineItemReductions[0].subtract(returnLineItemReductions[0]);
        ArmCurrency deals = saleLineItemReductions[1].subtract(returnLineItemReductions[1]);
        ArmCurrency markdowns = saleLineItemReductions[2].subtract(returnLineItemReductions[2]);
        ArmCurrency discounts = saleLineItemReductions[3].subtract(returnLineItemReductions[3]);
        if (negate) {
          total = total.multiply( -1);
          deals = deals.multiply( -1);
          markdowns = markdowns.multiply( -1);
          discounts = discounts.multiply( -1);
        }
        retVal.put("TOTAL", total);
        retVal.put("Deals", deals);
        retVal.put("Markdowns", markdowns);
        retVal.put("Discounts", discounts);
      } catch (CurrencyException currencyexception) {}
    } else {
      retVal.put("TOTAL", new ArmCurrency(baseCurrencyType, 0.0D));
      retVal.put("Deals", new ArmCurrency(baseCurrencyType, 0.0D));
      retVal.put("Markdowns", new ArmCurrency(baseCurrencyType, 0.0D));
      retVal.put("Discounts", new ArmCurrency(baseCurrencyType, 0.0D));
    }
    return retVal;
  }
  
  public static ArmCurrency getPaymentAmountByRegister(String type) {
		ArmCurrency result = getPaymentAmountByCodeForRegister(type);
			if (result != null)
				return result;
			// else
			return TxnSummary.getPaymentAmountByRegister(type);
		}
  public static ArmCurrency getPaymentAmountByCodeForRegister(String code) {
		try {
		ArmCurrency result = null;
			Hashtable htPayment = readTxnPayCodeSum();
			for (Enumeration enm = htPayment.elements(); enm.hasMoreElements();) {
				Vector vMedia = (Vector) enm.nextElement();
				for (Enumeration enumMedia = vMedia.elements(); enumMedia.hasMoreElements();) {
					CMSMediaEntry media = (CMSMediaEntry) enumMedia.nextElement();
					// System.out.println("~~~~~~~~~~~~~~ "+media.getPaymentCode()+":"+media.getPaymentID()+":"+media.getPaymentDescription());
					if (media != null && media.getPaymentCode() != null && media.getPaymentCode().equals(code))
						result = result == null ? media.getTotalAmount() : result.add(media.getTotalAmount());
				}
			}
			return result;
		} catch (Exception ex) {
			ex.printStackTrace();
			System.out.println("Exception getPaymentAmountByRegister()->".concat(String.valueOf(String.valueOf(ex))));
		}
		return null;
	}
  private static Hashtable readTxnPayCodeSum() {
		try {
			ObjectStore store = new ObjectStore(FileMgr.getLocalFile("tmp", "txn_pay_code_sum.ser"));
			Hashtable ht = (Hashtable) store.read();
			if (ht == null) {
				return new Hashtable();
			} else {
				return ht;
			}
		} catch (Exception ex) {
			System.out.println("Exception readTxnPayCodeSum()->" + ex);
		}
		return new Hashtable();
	}

  /**
   * put your documentation comment here
   * @param enum
   * @param baseCurrencyType
   * @return
   * @exception CurrencyException
   */
  private static ArmCurrency[] getLineItemReductions(Enumeration enm, CurrencyType baseCurrencyType)
      throws CurrencyException {
    ArmCurrency retVal[] = {new ArmCurrency(baseCurrencyType, 0.0D), new ArmCurrency(baseCurrencyType, 0.0D)
        , new ArmCurrency(baseCurrencyType, 0.0D), new ArmCurrency(baseCurrencyType, 0.0D)
    };
    while (enm.hasMoreElements()) {
      POSLineItem lineItem = (POSLineItem)enm.nextElement();
      for (Enumeration detailEnum = lineItem.getLineItemDetails(); detailEnum.hasMoreElements(); ) {
        POSLineItemDetail lineItemDetail = (POSLineItemDetail)detailEnum.nextElement();
        for (Enumeration reductionsEnum = lineItemDetail.getReductions();
            reductionsEnum.hasMoreElements(); ) {
          Reduction reduction = (Reduction)reductionsEnum.nextElement();
          String reason = reduction.getReason();
          ArmCurrency reductionAmount = reduction.getAmount();
          retVal[0] = retVal[0].add(reductionAmount);
          if (reason.endsWith("Deal Markdown")) {
            retVal[1] = retVal[1].add(reductionAmount);
          } else if (reason.endsWith("Markdown")) {
            retVal[2] = retVal[2].add(reductionAmount);
          } else if (reason.endsWith("Discount")) {
            retVal[3] = retVal[3].add(reductionAmount);
          }
        }
      }
    }
    return retVal;
  }

  static {
    timeIncrement = 15;
    salesTimer = new Timer(timeIncrement * 1000 * 60, new SalesPeriodAdapter());
    ConfigMgr config = new ConfigMgr("txnposter.cfg");
    int tempIncrement = config.getInt("SALES_COLLECTION_INCREMENT");
    if (tempIncrement > 0 && tempIncrement <= 60) {
      timeIncrement = tempIncrement;
    }
    salesTimer.setInitialDelay(0);
    salesTimer.setDelay(timeIncrement * 60 * 1000);
    salesTimer.start();
  }
}

