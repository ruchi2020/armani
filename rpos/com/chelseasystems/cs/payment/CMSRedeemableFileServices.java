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

import com.chelseasystems.cr.logging.*;
import com.chelseasystems.cr.util.*;
import com.chelseasystems.cr.appmgr.*;
import com.chelseasystems.cr.payment.*;
import com.chelseasystems.cs.payment.*;
import com.chelseasystems.cr.util.*;
import com.chelseasystems.cr.config.*;
import com.chelseasystems.cs.pos.*;
import com.chelseasystems.cr.pos.*;
import com.chelseasystems.cs.xml.*;
import java.util.*;
import java.text.*;
import java.io.*;


/**
 *
 * <p>Title: CMSRedeemableFileServices</p>
 *
 * <p>Description: A simple implementation of EmployeeServices that manages a
 * non-persistent memory-based database of Employee information</p>
 *
 * <p>Copyright: Copyright (c) 2005</p>
 *
 * <p>Company: </p>
 *
 * @author not attributable
 * @version 1.0
 */
public class CMSRedeemableFileServices extends CMSRedeemableServices {
  static private Vector allRedeemables = null;
  static private Vector deletedRedeemables = new Vector();
  static private String fileName = FileMgr.getLocalFile("xml", "redeemable.xml");

  /**
   *
   */
  public CMSRedeemableFileServices() {
    if (allRedeemables == null)
      loadAllRedeemablesFromFile();
  }

  /**
   * Method find redeemables on the basis of control number
   * @param controlNumber String
   * @return Redeemable
   * @throws Exception
   */
  public Redeemable findRedeemable(String controlNumber)
      throws Exception {
    if (allRedeemables == null || allRedeemables.size() == 0)
      return null;
    for (int i = 0; i < allRedeemables.size(); i++) {
      Redeemable redeemable = (Redeemable)allRedeemables.get(i);
      if (redeemable instanceof GiftCert
          && ((GiftCert)redeemable).getControlNum().equalsIgnoreCase(controlNumber))
        return (Redeemable)redeemable.clone();
      else if (redeemable instanceof StoreValueCard
          && ((StoreValueCard)redeemable).getControlNum().equalsIgnoreCase(controlNumber))
        return (Redeemable)redeemable.clone();
      else if (redeemable instanceof HouseAccount
          && ((HouseAccount)redeemable).getControlNum().equalsIgnoreCase(controlNumber))
        return (Redeemable)redeemable.clone();
    }
    return null;
  }

  /**
   * Method finds store value card by its id
   * @param id String
   * @return StoreValueCard
   * @throws Exception
   */
  public StoreValueCard findStoreValueCard(String id)
      throws Exception {
    if (allRedeemables == null || allRedeemables.size() == 0)
      return null;
    for (int i = 0; i < allRedeemables.size(); i++)
      if (((Redeemable)allRedeemables.get(i)).getId().equalsIgnoreCase(id)
          && allRedeemables.get(i) instanceof StoreValueCard)
        return (StoreValueCard)((StoreValueCard)allRedeemables.get(i)).clone();
    return null;
  }

 /**
   * Method finds gift certificate by its id
   * @param id String
   * @return GiftCert
   * @throws Exception
   */
  public GiftCert findGiftCert(String id)
      throws Exception {
    if (allRedeemables == null || allRedeemables.size() == 0)
      return null;
    for (int i = 0; i < allRedeemables.size(); i++)
      if (((Redeemable)allRedeemables.get(i)).getId().equalsIgnoreCase(id)
          && allRedeemables.get(i) instanceof GiftCert)
        return (GiftCert)((GiftCert)allRedeemables.get(i)).clone();
    return null;
  }

  /**
   * Method Finds a due bill based on an id
   * @param id String
   * @return DueBill
   * @throws Exception
   */
  public DueBill findDueBill(String id)
      throws Exception {
    if (allRedeemables == null || allRedeemables.size() == 0)
      return null;
    for (int i = 0; i < allRedeemables.size(); i++)
      if (((Redeemable)allRedeemables.get(i)).getId().equalsIgnoreCase(id)
          && allRedeemables.get(i) instanceof DueBill)
        return (DueBill)((DueBill)allRedeemables.get(i)).clone();
    return null;
  }

  /**
   * Method used to find house account by its id
   * @param id String
   * @return HouseAccount
   * @throws Exception
   */
  public HouseAccount findHouseAccount(String id)
      throws Exception {
    if (allRedeemables == null || allRedeemables.size() == 0)
      return null;
    for (int i = 0; i < allRedeemables.size(); i++)
      if (((Redeemable)allRedeemables.get(i)).getId().equalsIgnoreCase(id)
          && allRedeemables.get(i) instanceof HouseAccount)
        return (HouseAccount)((HouseAccount)allRedeemables.get(i)).clone();
    return null;
  }

  /**
   * Method finds redeemables on the basis of customer id
   * @param customerId String
   * @throws Exception
   * @return Redeemable
   */
  public Redeemable[] findByCustomerId(String customerId)
      throws Exception {
    return null;
  }

  /**
   * Method finds house account on the basis of customer id
   * @param customerid String
   * @throws Exception
   * @return Redeemable[]
   */
  public Redeemable[] findHouseAccountByCustomerId(String customerid)
      throws Exception {
    return null;
  }

  /**
   * Method finds redeemables on the basis of type and id
   * @param type String
   * @param id String
   * @throws Exception
   * @return Redeemable
   */
  public Redeemable findRedeemableById(String type, String id)
      throws Exception {
    return null;
  }

  /**
   * Method adds redeemables
   * @param redeemable Redeemable
   * @throws Exception
   */
  public void addRedeemable(Redeemable redeemable)
      throws Exception {
    allRedeemables.add(redeemable);
    writeRedeemablesToFile();
  }

  /**
   * Method Adds a new due bill.  This method should be called when a due bill
   * is issued
   * @param bill DueBill
   * @throws Exception
   */
  public void addDueBill(DueBill bill)
      throws Exception {
    allRedeemables.add(bill);
    writeRedeemablesToFile();
  }

  /**
   * This method add a new gift certificate. This method should be called each
   * time a gift cert is purchased
   * @param gift GiftCert
   * @throws Exception
   */
  public void addGiftCert(GiftCert gift)
      throws Exception {
    allRedeemables.add(gift);
    writeRedeemablesToFile();
  }

  /**
   * Method checks if the new Gift Cert control number is valid
   * @param newControlNumber String
   * @return boolean
   */
  public boolean isNewGiftCertControlNumberValid(String newControlNumber) {
    if (allRedeemables == null || allRedeemables.size() == 0)
      return true;
    for (int i = 0; i < allRedeemables.size(); i++)
      if (allRedeemables.get(i) instanceof GiftCert
          && ((GiftCert)allRedeemables.get(i)).getControlNum().equals(newControlNumber))
        return false;
    return true;
  }

  /**
   * This methods updated an exiting gift certificate
   * @param aTxn CompositePOSTransaction
   * @throws Exception
   */
  public void updateGiftCert(CompositePOSTransaction aTxn)
      throws Exception {
    updateRedeemable(aTxn);
  }

  /**
   * Method Updates an existing due bill
   * @param aTxn CompositePOSTransaction
   * @throws Exception
   */
  public void updateDueBill(CompositePOSTransaction aTxn)
      throws Exception {
    updateRedeemable(aTxn);
  }

  /**
   * Method used to update redeemable
   * @param aTxn CompositePOSTransaction
   * @throws Exception
   */
  public void updateRedeemable(CompositePOSTransaction aTxn)
      throws Exception {
    Payment[] pays = aTxn.getPaymentsArray();
    Redeemable redeemablePayment = null;
    Redeemable original = null;
    for (int i = 0; i < pays.length; i++) {
      if ((pays[i] instanceof Redeemable) && !(pays[i] instanceof DueBillIssue)) {
        String type = pays[i].getClass().getName();
        redeemablePayment = (Redeemable)pays[i];
        if (redeemablePayment == null)
          throw new Exception("There is no " + type + " in the transaction.");
        // if payment is a credit memo, reverse the sign of the payment
        if (redeemablePayment instanceof DueBill)
          redeemablePayment.setAmount(redeemablePayment.getAmount().absoluteValue());
        RedeemableHist hist = new RedeemableHist();
        hist.setDateUsed(new Date());
        hist.setTransactionsIdUsed(aTxn.getId());
        hist.setAmountUsed(redeemablePayment.getAmount());
        original = null;
        for (int j = 0; j < allRedeemables.size(); j++)
          if (redeemablePayment.getId().equals(((Redeemable)allRedeemables.get(j)).getId())) {
            original = (Redeemable)allRedeemables.get(j);
            original.addRedemption(hist);
            break;
          }
        if (original == null)
          throw new Exception("Original " + type + " is not found: " + redeemablePayment.getId());
      }
    }
    writeRedeemablesToFile();
  }

  /**
   * Method will keep reference to deleted redeemable.
   * @param redeemable Redeemable
   * @throws Exception
   */
  public void deleteRedeemable(Redeemable redeemable)
      throws Exception {
    if (redeemable == null)
      throw new NullPointerException();
    for (int i = 0; i < allRedeemables.size(); i++) {
      Redeemable re = (Redeemable)allRedeemables.get(i);
      if (re.getId().equals(redeemable.getId())
          && re.getClass().getName().equals(redeemable.getClass().getName())) {
        deletedRedeemables.add(redeemable);
        allRedeemables.removeElementAt(i);
        writeRedeemablesToFile();
        return;
      }
    }
  }

  /**
   * Method write redeemables to file
   * @throws Exception
   */
  private void writeRedeemablesToFile()
      throws Exception {
    String xml = new RedeemableXML().toXML(allRedeemables);
    FileWriter fileWriter = new FileWriter(fileName);
    fileWriter.write(xml);
    fileWriter.close();
  }

  /**
   * Method loads redeemables from the file
   */
  private void loadAllRedeemablesFromFile() {
    long begin = new java.util.Date().getTime();
    if (fileName == null || fileName.equals("")) {
      LoggingServices.getCurrent().logMsg(getClass().getName(), "loadAllRedeemablesFromFile()"
          , "Missing data file name.", "Make sure the data file is there."
          , LoggingServices.CRITICAL);
      System.exit(1);
    }
    try {
      allRedeemables = (new RedeemableXML()).toObjects(fileName);
    } catch (org.xml.sax.SAXException saxException) {
      LoggingServices.getCurrent().logMsg(getClass().getName(), "loadAllRedeemablesFromFile()"
          , "Cannot parse the redeemable data file.", "Verify the integrity of the store data file"
          , LoggingServices.CRITICAL);
      System.exit(1);
    } catch (IOException e) {
      LoggingServices.getCurrent().logMsg(getClass().getName(), "loadAllRedeemablesFromFile()"
          , "Cannot process the redeemable data file."
          , "Verify the integrity of the store data file", LoggingServices.CRITICAL);
      System.exit(1);
    } catch (Exception e) {
      LoggingServices.getCurrent().logMsg(getClass().getName(), "loadAllRedeemablesFromFile()"
          , "Cannot process the redeemable data file.", "Unknow exception: " + e
          , LoggingServices.CRITICAL);
      System.exit(1);
    }
    long end = new java.util.Date().getTime();
    System.out.println("Number of Redeemables loaded: " + allRedeemables.size() + " ("
        + (end - begin) + "ms)");
  }

  /**
   * Changes made by Satin.
   * This method finds coupon from the database based on barcode scanned and store id.
   * @param barcode String, StoreId String
   * @throws Exception
   * @return CMSCoupon
   */
@Override
public CMSCoupon findByBarcodeAndStoreId(String barcode, String StoreId)
		throws Exception {
	
	return null;
}


}
