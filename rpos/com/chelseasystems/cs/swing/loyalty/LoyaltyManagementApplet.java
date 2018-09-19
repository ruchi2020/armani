/*
 * put your module comment here
 * formatted with JxBeauty (c) johann.langhofer@nextra.at
 */


package com.chelseasystems.cs.swing.loyalty;

import com.chelseasystems.cr.swing.*;
import com.chelseasystems.cr.swing.bean.*;
import com.chelseasystems.cs.swing.menu.MenuConst;
import com.chelseasystems.cs.swing.panel.CMSPremioHistoryPanel;

import java.awt.event.*;
import com.chelseasystems.cs.employee.CMSEmployee;
import com.chelseasystems.cr.swing.event.CMSActionEvent;
import javax.swing.*;
import java.awt.*;
import com.chelseasystems.cs.store.CMSStore;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

import com.chelseasystems.cr.config.ConfigMgr;
import com.chelseasystems.cs.loyalty.*;
import com.chelseasystems.cs.loyalty.RewardCard;
import com.chelseasystems.cs.customer.CMSCustomer;
import com.chelseasystems.cs.pos.RewardTransaction;
import com.chelseasystems.cs.pos.PointManagement;
import com.chelseasystems.cs.txnnumber.CMSTransactionNumberHelper;
import com.chelseasystems.cs.register.CMSRegister;
import com.chelseasystems.cs.pos.CMSCompositePOSTransaction;
import com.chelseasystems.cr.pos.PaymentTransactionAppModel;
import com.chelseasystems.cs.swing.CMSAppModelFactory;
import com.chelseasystems.cr.receipt.ReceiptConfigInfo;
import com.chelseasystems.cr.receipt.ReceiptLocaleSetter;
import com.chelseasystems.cr.util.ObjectStore;
import com.chelseasystems.cs.payment.CMSLoyaltyAppModel;
import com.chelseasystems.rb.ReceiptFactory;
import com.ga.fs.fsbridge.ARMFSBridge;

/**
 * <p>Title:ItemLookupListApplet </p>
 * <p>Description: Searches items on provided search criteria</p>
 * <p>Copyright: Copyright (c) 2005</p>
 * <p>Company: SkillnetInc</p>
 * @author Vikram Mundhra
 * @version 1.0
 */
public class LoyaltyManagementApplet extends CMSApplet {
  public static final String NAME_SPEC = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ.-' ";
  private LoyaltyManagementPanel pnlLoyaltyManagement;
  private RewardDisplayPanel pnlRewardDisplay;
  private CMSPremioHistoryPanel pnlPremioHistory;
  private JPanel pnlHeader;
  private JCMSLabel lblLastName;
  private JCMSLabel lblFirstName;
  private JCMSLabel lblCustomerId;
  private JCMSTextField txtLastName;
  private JCMSTextField txtFirstName;
  private JCMSTextField txtCustomerId;
  //  private ScrollableToolBarPanel screen;
  private Loyalty loyalty;
  private ArrayList loyaltyList = null;
  private ArrayList premioHistoryList = null;
  private HashMap premioHistoryMap = null;
  //IRepositoryManager theMgr ;
  CMSCustomer cmsCust;
  String pointsUpdated = "";
  String PointsAdded = "";
  String RewardId = "";
  String PointDeleted = "";
  String reason = "";
  String reasonDeduct = "";
  double dResolution = com.chelseasystems.cr.swing.CMSApplet.r;

  private static ConfigMgr loyaltyConfigMgr = new ConfigMgr("loyalty.cfg");
  private boolean displayPremioHistory = false;
  /**
   * put your documentation comment here
   */
  public void stop() {
    // reset();
  }

  /**
   * Initializes all the values
   */
  public void init() {
    try {
      jbInit();
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  /**
   * put your documentation comment here
   */
  public void start() {
    reset();
    RewardCard loadRwdCard[] = null;
    theAppMgr.showMenu(MenuConst.LOYALTY, theOpr);
    cmsCust = (CMSCustomer)theAppMgr.getStateObject("CUSTOMER_LOOKUP");
    try {
      if (cmsCust != null) {
        loadRwdCard = LoyaltyHelper.getCustomerRewards(theAppMgr, cmsCust.getId());
        loyaltyList = LoyaltyHelper.getCustomerLoyaltiesAsList(theAppMgr, cmsCust.getId());
        if (loyaltyList != null) {
          pnlLoyaltyManagement.setItems((Loyalty[])loyaltyList.toArray(new Loyalty[0]));
          pnlLoyaltyManagement.selectFirstRow();
          if(displayPremioHistory){
          	premioHistoryList = LoyaltyHelper.getCustomerPremioDiscountHistoryAsList(theAppMgr, ((Loyalty)loyaltyList.get(0)).getLoyaltyNumber());          
          	premioHistoryMap.put(((Loyalty)loyaltyList.get(0)).getLoyaltyNumber(),premioHistoryList);
          }
        }
        if (loadRwdCard != null) {
          pnlRewardDisplay.setItems(loadRwdCard);
          pnlRewardDisplay.update();
        }
        if(premioHistoryList != null){
        	pnlPremioHistory.setItems((CMSPremioHistory[])premioHistoryList.toArray(new CMSPremioHistory[0]));
        	pnlPremioHistory.update();
        }
          /**
           * Loading the text fields
           */
          txtCustomerId.setText(cmsCust.getId());
          txtLastName.setText(cmsCust.getLastName());
          txtFirstName.setText(cmsCust.getFirstName());
      }
      enter();
      this.addComponentListener(new java.awt.event.ComponentAdapter() {
        public void componentResized(ComponentEvent e) {
          resize();
        }
      });
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  /**
   * Resets the fields.
   */
  private void reset() {
    pnlLoyaltyManagement.clear();
    pnlRewardDisplay.clear();
    pnlPremioHistory.clear();
    txtCustomerId.setText("");
    txtFirstName.setText("");
    txtLastName.setText("");
    
    loyalty = null;
    loyaltyList = null;
    premioHistoryList = null;
    premioHistoryMap = new HashMap();
    
    enter();
  }

  /**
   * put your documentation comment here
   */
  private void enter() {
    theAppMgr.setSingleEditArea(res.getString("Select Option"));
  }

  /**
   * Handle for Edit Area Events
   * @param sCommand String
   * @param sInput String
   */
  public void editAreaEvent(String sCommand, String sInput) {
    if (sCommand.equals("ADD_PO")) {
      if (sInput == null || sInput.trim().length() < 1) {
        theAppMgr.showErrorDlg(res.getString("Enter the Points to add to account"));
        return;
      } else {
        pointsUpdated = sInput;
        theAppMgr.setSingleEditArea(res.getString("Enter Reason"), "REASON_ADD"
            , theAppMgr.REQUIRED_MASK);
        theAppMgr.setEditAreaFocus();
      }
    }
    if (sCommand.equals("REASON_ADD")) {
//      System.out.println("In REASON ADD");
      if (sInput == null || sInput.trim().length() < 1) {
        theAppMgr.showErrorDlg(res.getString("Enter the Reason"));
        theAppMgr.setEditAreaFocus();
        //return;
      } else {
        updateLoyaltyPoints(sInput, true);
      }
    }
    if (sCommand.equals("REMOVE_PO")) {
      if (sInput == null || sInput.trim().length() < 1) {
        theAppMgr.showErrorDlg(res.getString("Enter points to deduct from account"));
        return;
      } else {
        pointsUpdated = sInput;
        theAppMgr.setSingleEditArea(res.getString("Enter Reason"), "REASON_DEDUCT"
            , theAppMgr.REQUIRED_MASK);
        theAppMgr.setEditAreaFocus();
      }
    }
    if (sCommand.equals("REASON_DEDUCT")) {
      if (sInput == null || sInput.trim().length() < 1) {
        theAppMgr.showErrorDlg(res.getString("Enter the Reason"));
        theAppMgr.setEditAreaFocus();
      } else {
        System.out.println("In here trying to post ...");
        updateLoyaltyPoints(sInput, false);
       }
    }
  }

/*
 * boolean update : true = addPoints, false = deductPoints
 */
private void updateLoyaltyPoints(String reasonForUpdate, boolean update) {
	//reason = sInput;
	long pointsToUpdate;
	if(update == true) pointsToUpdate= new Long(pointsUpdated).longValue();
	else pointsToUpdate= -(new Long(pointsUpdated).longValue());
	Loyalty loyaltyObj = (Loyalty)theAppMgr.getStateObject("LOYALTYFORUPDATEPOINTS");
	if(loyaltyObj == null) return;
	//Loyalty loyaltyRemove = loyaltyObj;
	try {
      //Update the local copy
      Loyalty loyaltyCard = getLoyaltyFromList(loyaltyList, loyaltyObj.getLoyaltyNumber());
	  loyaltyCard.updateCurrBalance(pointsToUpdate);
	  theAppMgr.removeStateObject("LOYALTYFORUPDATEPOINTS");
	  CMSStore cmsStore = (CMSStore)theAppMgr.getGlobalObject("STORE");
	  CMSEmployee cmsEmployee = (CMSEmployee)theAppMgr.getStateObject("OPERATOR");
	  PointManagement PointTxn = new PointManagement(cmsStore, loyaltyObj);
	  PointTxn.setComment(reasonForUpdate);
	  PointTxn.setPoints(pointsToUpdate);
	  PointTxn.setRegisterId(((CMSRegister)theAppMgr.getGlobalObject("REGISTER")).getId());
	  PointTxn.setId(CMSTransactionNumberHelper.getNextTransactionNumber(theAppMgr
	      , (CMSStore)theStore, (CMSRegister)theAppMgr.getGlobalObject("REGISTER")));
	  PointTxn.setSubmitDate(new Date());
	  PointTxn.setProcessDate((Date)theAppMgr.getGlobalObject("PROCESS_DATE"));
	  PointTxn.setTheOperator(cmsEmployee);
	  PointTxn.post();
	} catch (Exception e) {
	  e.printStackTrace();
	  System.out.println(e.getLocalizedMessage());
	}
	// Setting the panel
	try {
	  if (loyaltyList != null) {
	    pnlLoyaltyManagement.setItems((Loyalty[])loyaltyList.toArray(new Loyalty[0]));

	      SwingUtilities.invokeLater(new Runnable() {
	        public void run() {
	          pnlLoyaltyManagement.update();
	        }
	      });
	  }
	  theAppMgr.setSingleEditArea(res.getString("Select Option"));
	} catch (Exception e) {
	  e.printStackTrace();
	}
	updatePointsForTxnLoyaltyCard(getLoyaltyFromList(loyaltyList, loyaltyObj.getLoyaltyNumber()));
}

  private void updatePointsForTxnLoyaltyCard(Loyalty updatedLoyalty){
	  CMSCompositePOSTransaction theTxn = (CMSCompositePOSTransaction)theAppMgr.getStateObject("TXN_POS");
	  if(theTxn == null) return;

	  Loyalty txnLoyalty = theTxn.getLoyaltyCard();
	  if(txnLoyalty == null ) return;
	// if points are added during a reissue, then the update is not reflected if the following stmt is used
	  else if( !(txnLoyalty.getLoyaltyNumber().trim().equalsIgnoreCase(updatedLoyalty.getLoyaltyNumber().trim()))) return;
	  else theTxn.setLoyaltyCard(updatedLoyalty);
  }

  /**
   * put your documentation comment here
   */
  private void doPrevious() {
    reset();
    theAppMgr.goBack();
  }

  //  private void mustCallAfterTheAppMgrGoBack() {
  //
  //    screen = (ScrollableToolBarPanel) AppManager.getCurrent().getMainFrame().
  //        getAppToolBar();
  //    screen.unregisterKeyboardAction(KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE,
  //        0));
  //  }
  /**
   * For Event Handling
   * @param anEvent CMSActionEvent
   */
  public void appButtonEvent(String sHeader, CMSActionEvent anEvent) {
    theAppMgr.unRegisterSingleEditArea(); // cancel any builders in progress
    String sAction = anEvent.getActionCommand();
    if (sAction.equals("PREV")) {
      anEvent.consume();
      if (sHeader.equals("ACCOUNT_MAIN_HEADER")) {
        theAppMgr.showMenu(MenuConst.LOYALTY, theOpr);
      }
    } else if (sAction.equals("ADD_POINTS")) {
      // See if a loyalty card is selected.
      if (pnlLoyaltyManagement.getSelectedItem() == null) {
        anEvent.consume();
        theAppMgr.showErrorDlg(res.getString("Select a loyalty card to proceed"));
        theAppMgr.setSingleEditArea(res.getString("Select Option"));
      } else {
        theAppMgr.addStateObject("LOYALTYFORUPDATEPOINTS", pnlLoyaltyManagement.getSelectedItem());
        Loyalty loyaltyObj = (Loyalty)theAppMgr.getStateObject("LOYALTYFORUPDATEPOINTS");
        if (loyaltyObj.getStatus() == false) {
          theAppMgr.showErrorDlg(res.getString("Sorry the Loyalty Card chosen is inactive"));
          theAppMgr.setSingleEditArea(res.getString("Select Option"));
          return;
        } else {
          theAppMgr.setSingleEditArea(res.getString("Enter points to add to account"), "ADD_PO"
              , theAppMgr.REQUIRED_MASK);
          theAppMgr.setEditAreaFocus();
        }
      }
    } else if (sAction.equals("REMOVE_POINTS")) {
      // See if a loyalty card is selected.
      if (pnlLoyaltyManagement.getSelectedItem() == null) {
        anEvent.consume();
        theAppMgr.showErrorDlg(res.getString("Select a loyalty card to proceed"));
        theAppMgr.setSingleEditArea(res.getString("Select Option"));
      } else {
        theAppMgr.addStateObject("LOYALTYFORUPDATEPOINTS", pnlLoyaltyManagement.getSelectedItem());
        Loyalty loyaltyObj = (Loyalty)theAppMgr.getStateObject("LOYALTYFORUPDATEPOINTS");
        if (loyaltyObj.getStatus() == false) {
          theAppMgr.showErrorDlg(res.getString("Sorry the Loyalty Card chosen is inactive"));
          theAppMgr.setSingleEditArea(res.getString("Select Option"));
        } else {
          theAppMgr.setSingleEditArea(res.getString("Enter points to deduct from account")
              , "REMOVE_PO", theAppMgr.REQUIRED_MASK);
          theAppMgr.setEditAreaFocus();
        }
      }
    } else if (sAction.equals("REISSUE_LOYALTY_CARD")) {
      if (pnlLoyaltyManagement.getSelectedItem() == null) {
        theAppMgr.showErrorDlg(res.getString("Select a loyalty card"));
        theAppMgr.setSingleEditArea(res.getString("Select Option"));
      } else if (theAppMgr.showOptionDlg(res.getString("Void Loyalty Card")
          , res.getString("Are you sure you want to void this Loyalty card and issue a new Loyalty card?"))) {
        //Check if the loyalty Card is inactive
        if (pnlLoyaltyManagement.getSelectedItem().getStatus() == false) {
          theAppMgr.showErrorDlg(res.getString("Sorry the Loyalty Card chosen is inactive"));
          theAppMgr.setSingleEditArea(res.getString("Select Option"));
        } else {
          theAppMgr.unRegisterSingleEditArea();
          String Builder = loyaltyConfigMgr.getString("LOYALTY_BUILDER");
          System.out.println("the builder is: " + Builder);
          theAppMgr.buildObject("ENROLL_LOYALTY", Builder, "");
          theAppMgr.addStateObject("LOYALTYFORREISSUE", pnlLoyaltyManagement.getSelectedItem());
        }
      } else {
        anEvent.consume();
        theAppMgr.setSingleEditArea(res.getString("Select Option"));
      }
    } else if (sAction.equals("PRINT_POINT_DETAILS")) {
      printPremioDetails();
    }
  }

  /**
   * For Event Handling
   * @param anEvent CMSActionEvent
   */
  public void appButtonEvent(CMSActionEvent anEvent) {
    String sAction = anEvent.getActionCommand();
    boolean additionalFieldFlag = false;
    String sQuery = "";
    if (sAction.equals("LOYALTY_DETAILS")) {
      if (pnlLoyaltyManagement.getSelectedItem() != null) {
        theAppMgr.showMenu(MenuConst.LOYALTY_DETAILS, theOpr);
        theAppMgr.addStateObject("LOYALTY", pnlLoyaltyManagement.getSelectedItem());
      } else if (pnlRewardDisplay.getSelectedItem() != null) {
        theAppMgr.showMenu(MenuConst.PREV_PRINT, theOpr);
        theAppMgr.addStateObject("REWARDVAL", pnlRewardDisplay.getSelectedItem());
      } else {
        anEvent.consume();
        theAppMgr.showErrorDlg(res.getString("Select a loyalty or reward card to proceed"));
        theAppMgr.setSingleEditArea(res.getString("Select Option"));
      }
    } else if (sAction.equals("LOYALTY_ENROLL")) {
      theAppMgr.removeStateObject("LOYALTYFORREISSUE");
      theAppMgr.unRegisterSingleEditArea();
      ConfigMgr config = new ConfigMgr("loyalty.cfg");
      String Builder = config.getString("LOYALTY_BUILDER");
      System.out.println("the builder is: " + Builder);
      theAppMgr.buildObject("ENROLL_LOYALTY", Builder, "");
    } else if (sAction.equals("ISSUE_REWARD_NORULE")) {
      // If a LoyaltyCard is chosen
      Loyalty OldObj = new Loyalty();
      if (pnlLoyaltyManagement.getSelectedItem() == null) {
        anEvent.consume();
        theAppMgr.showErrorDlg(res.getString("Select a loyalty card to proceed"));
        theAppMgr.setSingleEditArea(res.getString("Select Option"));
        return;
      }
      //else
      {
        theAppMgr.addStateObject("LOYALTYOBJ", pnlLoyaltyManagement.getSelectedItem());
        OldObj = pnlLoyaltyManagement.getSelectedItem();
      }
      if (!isValidInStore(OldObj)) {
        theAppMgr.showErrorDlg(res.getString(
            "Cannot issue reward for this loyalty card type in this store."));
        return;
      }
      // If eligible for LoyaltyCard
      if (!eligibleForRewardCard(OldObj)) {
        theAppMgr.showErrorDlg(res.getString("Not Eligible for Reward Card"));
        theAppMgr.setSingleEditArea(res.getString("Select Option"));
      } else {
        theAppMgr.unRegisterSingleEditArea();
        ConfigMgr config = new ConfigMgr("loyalty.cfg");
        String Builder = config.getString("ISSUE_REWARD_CARD_BUILDER");
        System.out.println("the builder is: " + Builder);
        theAppMgr.buildObject("ISSUERWDCARD", Builder, "");
      }
    } else if (sAction.equals("ACCOUNT_MAINTENANCE")) {
      theAppMgr.showMenu(MenuConst.ACCOUNT_MAINTENANCE, "ACCOUNT_MAIN_HEADER", theOpr);
      theAppMgr.setSingleEditArea(res.getString("Select Option"), "ACCOUNT_MAINTAIN"
          , theAppMgr.REQUIRED_MASK);
    } else if (sAction.equals("LOYALTY_CARD_ACTIVE")) {
		changeLoyaltyCardStatus(anEvent, true);
	} else if (sAction.equals("LOYALTY_CARD_INACTIVE")) {
		changeLoyaltyCardStatus(anEvent, false);
    } else if (sAction.equals("PREV")) {
      theAppMgr.addStateObject("PREV_LOYALTY", "LOYALTYMGM");
    }
  }

  /**
   * Handles Object Event
   * @param Command String
   * @param obj Object
   */
  public void objectEvent(String Command, Object obj) {
    Loyalty loyaltyCardObj = (Loyalty)theAppMgr.getStateObject("LOYALTYFORREISSUE");
    if (Command.equals("ENROLL_LOYALTY") && loyaltyCardObj == null) {
      if (obj != null) {
        loyalty = (Loyalty)obj;
        txtCustomerId.setText(loyalty.getCustomer().getId());
        /**
         * For adding loyalty info to loyalty customer
         */
        try {
          LoyaltyHelper.addLoyalty(theAppMgr, loyalty);
          loyalty.getCustomer().setLoyaltyMember();

          // in case the customer is being enrolled into Loyalty now
          if(loyaltyList == null){
        	  loyaltyList = new ArrayList();
          }

          loyaltyList.add(loyalty);
        } catch (Exception e) {
          e.printStackTrace();
        }
        pnlLoyaltyManagement.addItem(loyalty);
        SwingUtilities.invokeLater(new Runnable() {
          public void run() {
            pnlLoyaltyManagement.update();
          }
        });
        theAppMgr.setSingleEditArea(res.getString("Select Option"));
        // add loyalty to panel and update the panel
      }
    }
    // For Issuing reward card
    if (Command.equals("ISSUERWDCARD")) {
      loyaltyCardObj = (Loyalty)theAppMgr.getStateObject("LOYALTYOBJ");
      theAppMgr.removeStateObject("LOYALTYOBJ");
      Loyalty OldObj = loyaltyCardObj;
      // For Reward Txn
      CMSStore cmsStore = (CMSStore)theAppMgr.getGlobalObject("STORE");
      CMSEmployee cmsEmployee = (CMSEmployee)theAppMgr.getStateObject("OPERATOR");
      RewardTransaction rewardTxn = new RewardTransaction(cmsStore);
      try {
        rewardTxn.setId(CMSTransactionNumberHelper.getNextTransactionNumber(theAppMgr
            , (CMSStore)theStore, (CMSRegister)theAppMgr.getGlobalObject("REGISTER")));
        rewardTxn.setSubmitDate(new Date());
        rewardTxn.setProcessDate((Date)theAppMgr.getGlobalObject("PROCESS_DATE"));
        rewardTxn.setTheOperator(cmsEmployee);
      } catch (Exception e) {
        e.printStackTrace();
      }
      ConfigMgr config = new ConfigMgr("loyalty.cfg");
      String loyaltyRewardRatio = config.getString("LOYALTY_REWARD_REDEMPTION_RATIO");
      String loyaltyAmount = config.getString("LOYALTY_REWARD_AMOUNT");
      double points = (new Double(loyaltyRewardRatio)).doubleValue()
          * (new Double(loyaltyAmount)).doubleValue();
      RewardCard rewardCard = (RewardCard)obj;
      if (rewardCard != null) {
        rewardCard.setLoyalty(OldObj);
        rewardCard.getLoyalty().setLoyaltyNumber(OldObj.getLoyaltyNumber());
        rewardTxn.setRewardCard(rewardCard);
        try {
          rewardTxn.setRegisterId(((CMSRegister)theAppMgr.getGlobalObject("REGISTER")).getId());
        } catch (Exception e) {
          e.printStackTrace();
        }
        //loyaltyCardObj.setCurrBalance(loyaltyCardObj.getCurrBalance()-points);
        try {
          if (cmsCust != null)
            rewardTxn.setCustomer(cmsCust);
        } catch (Exception e) {
          e.printStackTrace();
        }
        // Points that need to be added
        rewardTxn.setPoints((long)points);
        try {
          rewardTxn.post();

          /* This code added by usha for producing the Reward Transaction RDO */
          PaymentTransactionAppModel appModel = rewardTxn.getAppModel(CMSAppModelFactory.
              getInstance(), theAppMgr);
          if (ReceiptConfigInfo.getInstance().isProducingRDO()) {
            String fileName = ReceiptConfigInfo.getInstance().getPathForRDO()
                + "RewardTransaction.rdo";
            try {
              ObjectStore objectStore = new ObjectStore(fileName);
              objectStore.write(appModel);
            } catch (Exception e) {
              System.out.println("exception on writing object to blueprint folder: " + e);
            }
          }
          appModel.printReceipt(theAppMgr);
          /* end code addition */

        } catch (Exception e) {
          e.printStackTrace();
        }
        //Need to enter the issued reward Card.
        try {
          Loyalty loyaltyCard = this.getLoyaltyFromList(loyaltyList, OldObj.getLoyaltyNumber());
          loyaltyCard.setCurrBalance(loyaltyCard.getCurrBalance() - points);
          updatePointsForTxnLoyaltyCard(loyaltyCard);
          if (loyaltyList != null) {
            pnlLoyaltyManagement.setItems((Loyalty[])loyaltyList.toArray(new Loyalty[0]));

              SwingUtilities.invokeLater(new Runnable() {
                public void run() {
                  pnlLoyaltyManagement.update();
                }
              });
            }
        } catch (Exception e) {}
        pnlRewardDisplay.addItem(rewardCard);
        SwingUtilities.invokeLater(new Runnable() {
          public void run() {
            //              pnlLoyaltyManagement.update();
            pnlRewardDisplay.update();
            pnlLoyaltyManagement.update();
          }
        });
        theAppMgr.setSingleEditArea(res.getString("Select Option"));
      }
    }
    // For ReIssue LoyaltyCard
    if (Command.equals("ENROLL_LOYALTY") && loyaltyCardObj != null) {
      if (obj != null) {
        loyalty = (Loyalty)obj;
        loyaltyCardObj = (Loyalty)theAppMgr.getStateObject("LOYALTYFORREISSUE");
        theAppMgr.removeStateObject("LOYALTYFORREISSUE");
        Loyalty oldLoyalty = loyaltyCardObj;
        /**
         * For transfering points from old loyalty to new loyalty
         */
        loyalty.setCurrBalance(oldLoyalty.getCurrBalance());
        loyalty.setLifeTimeBalance(oldLoyalty.getLifeTimeBalance());
        if(loyalty.isYearlyComputed()){
        	loyalty.setLastYearBalance(oldLoyalty.getLastYearBalance());
        	loyalty.setCurrYearBalance(oldLoyalty.getCurrYearBalance());
        }
        txtCustomerId.setText(loyalty.getCustomer().getId());
        try {
          LoyaltyHelper.reissueLoyalty(theAppMgr, loyaltyCardObj.getLoyaltyNumber(), loyalty);
          //pnlLoyaltyManagement.deleteItem(oldLoyalty);
        } catch (Exception e) {
          e.printStackTrace();
        }
        try {
          loyaltyList = LoyaltyHelper.getCustomerLoyaltiesAsList(theAppMgr, cmsCust.getId());
          pnlLoyaltyManagement.clear();
          if (loyaltyList != null) {
            pnlLoyaltyManagement.setItems((Loyalty[])loyaltyList.toArray(new Loyalty[0]));
              SwingUtilities.invokeLater(new Runnable() {
                public void run() {
                  pnlLoyaltyManagement.update();
                }
              });
            }
          setStoreLoyaltyCardForTxnCustomer(loyaltyList);
        } catch (Exception e) {}
        // pnlLoyaltyManagement.addItem(loyalty);
        theAppMgr.setSingleEditArea(res.getString("Select Option"));
      }
    }
  }

  /**
   * put your documentation comment here
   * @param loyalty[]
   * @return
   */
  private int loadLoyaltyItems(Loyalty loyalty[]) {
    pnlLoyaltyManagement.clear();
    pnlLoyaltyManagement.setItems(loyalty);
    return loyalty.length;
  }

  /**
   * put your documentation comment here
   * @param rewardCard[]
   * @return
   */
  private int loadRewardCard(RewardCard rewardCard[]) {
    pnlRewardDisplay.clear();
    pnlRewardDisplay.setItems(rewardCard);
    return rewardCard.length;
  }

  /**
   * Get the version
   * @return String
   */
  public String getVersion() {
    return ("$Revision: 1.1 $");
  }

  /**
   * Gets the screen Name
   * @return String
   */
  public String getScreenName() {
    return "Customer Loyalty";
  }
  
  public void updateTableSelection(JComponent comp)
  {
	  if (comp == null) return;
	  if (comp == pnlLoyaltyManagement){
			  if(displayPremioHistory){
				  pnlPremioHistory.clear();
				  try{
					  premioHistoryList = (ArrayList)premioHistoryMap.get(pnlLoyaltyManagement.getSelectedItem().getLoyaltyNumber());
					  if(premioHistoryList == null 
					  	&& !(premioHistoryMap.containsKey(pnlLoyaltyManagement.getSelectedItem().getLoyaltyNumber()))){
						  premioHistoryList = LoyaltyHelper.getCustomerPremioDiscountHistoryAsList(theAppMgr,
								  pnlLoyaltyManagement.getSelectedItem().getLoyaltyNumber());
						  premioHistoryMap.put(pnlLoyaltyManagement.getSelectedItem().getLoyaltyNumber(),premioHistoryList);
					  }
					  pnlPremioHistory.setItems((CMSPremioHistory[])premioHistoryList.toArray(new CMSPremioHistory[0]));
		
					  SwingUtilities.invokeLater(new Runnable() {
				          public void run() {
				        	  pnlPremioHistory.update();
		//		        	  pnlPremioHistory.repaint();
				          }
				        });
				  }catch(Exception ex){
					  System.out.println(" Unable to retrieve tansaction history for loyalty : " + pnlLoyaltyManagement.getSelectedItem().getLoyaltyNumber());
				  }
			  }else{
				  pnlRewardDisplay.getTable().clearSelection();
			  }
	  }else if (comp == pnlRewardDisplay){
		  pnlLoyaltyManagement.getTable().clearSelection();
	  }else if(comp == pnlPremioHistory){
		  pnlLoyaltyManagement.getTable().clearSelection();
	  }
  }

  /**
   * put your documentation comment here
   * @exception Exception
   */
  private void jbInit()
      throws Exception {
	if("TRUE".equalsIgnoreCase(loyaltyConfigMgr.getString("DISPLAY_PREMIO_HISTORY")))
	   displayPremioHistory = true;
	else displayPremioHistory = false;
    pnlLoyaltyManagement = new LoyaltyManagementPanel(theAppMgr);
    pnlRewardDisplay = new RewardDisplayPanel(theAppMgr);
    pnlPremioHistory = new CMSPremioHistoryPanel(theAppMgr);
    //setParentComponent method makes sure only one row is selected from any panel at any
    //point of time
    pnlLoyaltyManagement.setParentComponent(this);
    pnlRewardDisplay.setParentComponent(this);
    pnlPremioHistory.setParentComponent(this);
    pnlHeader = new JPanel();
    lblFirstName = new JCMSLabel();
    txtFirstName = new JCMSTextField();
    lblCustomerId = new JCMSLabel();
    txtCustomerId = new JCMSTextField();
    lblLastName = new JCMSLabel();
    txtLastName = new JCMSTextField();
    this.getContentPane().setLayout(new BorderLayout());
    this.getContentPane().add(pnlHeader, BorderLayout.NORTH);
    this.getContentPane().add(pnlLoyaltyManagement, BorderLayout.CENTER);
    if(displayPremioHistory){
    	this.getContentPane().add(pnlPremioHistory, BorderLayout.SOUTH);
    }
    else this.getContentPane().add(pnlRewardDisplay, BorderLayout.SOUTH);
    this.setLayout(new BorderLayout());
    //  pnlHeader.setPreferredSize(new Dimension(833, 130));
    pnlHeader.setLayout(new GridBagLayout());
    lblFirstName.setLabelFor(txtFirstName);
    lblFirstName.setText(res.getString("First Name"));
    txtFirstName.setText("");
    txtFirstName.setEnabled(false);
    lblLastName.setLabelFor(txtLastName);
    lblLastName.setText(res.getString("Last Name"));
    txtLastName.setText("");
    txtLastName.setEnabled(false);
    lblCustomerId.setLabelFor(txtCustomerId);
    lblCustomerId.setText(res.getString("Customer No"));
    txtCustomerId.setText("");
    txtCustomerId.setEnabled(false);
    pnlHeader.setPreferredSize(new Dimension((int)(800 * dResolution), (int)(135 * dResolution)));
    pnlLoyaltyManagement.setPreferredSize(new Dimension((int)(400 * dResolution)
        , (int)(400 * dResolution)));
    pnlRewardDisplay.setPreferredSize(new Dimension((int)(300 * dResolution)
        , (int)(230 * dResolution)));
    if(displayPremioHistory){
    	pnlPremioHistory.setPreferredSize(new Dimension((int)(300 * dResolution)
            , (int)(230 * dResolution)));
    }
    this.add(pnlHeader, BorderLayout.NORTH);
    this.add(pnlLoyaltyManagement, BorderLayout.CENTER);
    if(displayPremioHistory){
    	this.add(pnlPremioHistory, BorderLayout.SOUTH);
      pnlPremioHistory.setAppMgr(theAppMgr);
    }
    else this.add(pnlRewardDisplay, BorderLayout.SOUTH);
    //  this.setBackground(theAppMgr.getBackgroundColor());
    pnlHeader.setBackground(theAppMgr.getBackgroundColor());
    pnlLoyaltyManagement.setAppMgr(theAppMgr);
    pnlRewardDisplay.setAppMgr(theAppMgr);

    lblFirstName.setAppMgr(theAppMgr);
    lblFirstName.setFont(theAppMgr.getTheme().getLabelFont());
    txtFirstName.setAppMgr(theAppMgr);
    txtFirstName.setFont(theAppMgr.getTheme().getTextFieldFont());
    lblLastName.setAppMgr(theAppMgr);
    lblLastName.setFont(theAppMgr.getTheme().getLabelFont());
    txtLastName.setAppMgr(theAppMgr);
    txtLastName.setFont(theAppMgr.getTheme().getTextFieldFont());
    lblCustomerId.setAppMgr(theAppMgr);
    lblCustomerId.setFont(theAppMgr.getTheme().getLabelFont());
    txtCustomerId.setAppMgr(theAppMgr);
    txtCustomerId.setFont(theAppMgr.getTheme().getTextFieldFont());
    pnlHeader.add(txtFirstName
        , new GridBagConstraints(3, 0, 1, 1, 1.0, 0.0, GridBagConstraints.WEST
        , GridBagConstraints.HORIZONTAL, new Insets(0, 4, 0, 8), 4, 4));
    pnlHeader.add(txtLastName
        , new GridBagConstraints(1, 0, 1, 1, 1.0, 0.0, GridBagConstraints.WEST
        , GridBagConstraints.HORIZONTAL, new Insets(0, 4, 0, 4), 4, 4));
    pnlHeader.add(txtCustomerId
        , new GridBagConstraints(1, 1, 1, 1, 1.0, 0.0, GridBagConstraints.SOUTHWEST
        , GridBagConstraints.HORIZONTAL, new Insets(4, 5, 2, 3), 4, 4));
    pnlHeader.add(lblFirstName
        , new GridBagConstraints(2, 0, 1, 1, 0.0, 0.0, GridBagConstraints.WEST
        , GridBagConstraints.NONE, new Insets(0, 8, 0, 1), 12, 4));
    pnlHeader.add(lblLastName
        , new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0, GridBagConstraints.WEST
        , GridBagConstraints.NONE, new Insets(0, 12, 0, 4), 22, 4));
    pnlHeader.add(lblCustomerId
        , new GridBagConstraints(0, 1, 1, 2, 0.0, 0.0, GridBagConstraints.SOUTHWEST
        , GridBagConstraints.NONE, new Insets(4, 9, 0, 1), 26, 0));
    txtLastName.setDocument(new TextFilter(NAME_SPEC, 30));
    txtFirstName.setDocument(new TextFilter(NAME_SPEC, 30));
  }

  /**
   * Resizes the components
   */
  public void resize() {
    double r = com.chelseasystems.cr.swing.CMSApplet.r;
    lblFirstName.setPreferredSize(new Dimension((int)(55 * r), (int)(30 * r)));
    txtFirstName.setPreferredSize(new Dimension((int)(75 * r), (int)(30 * r)));
    lblLastName.setPreferredSize(new Dimension((int)(55 * r), (int)(30 * r)));
    txtLastName.setPreferredSize(new Dimension((int)(75 * r), (int)(30 * r)));
    lblCustomerId.setPreferredSize(new Dimension((int)(55 * r), (int)(30 * r)));
    txtCustomerId.setPreferredSize(new Dimension((int)(75 * r), (int)(30 * r)));
  }

  /**
   * put your documentation comment here
   * @param e
   */
  public void pageDown(MouseEvent e) {	  
	  //pnlLoyaltyManagement.getTable().clearSelection();
	  //pnlRewardDisplay.getTable().clearSelection();	  
	  if((e.getComponent() == pnlLoyaltyManagement) || (pnlLoyaltyManagement.getSelectedRowIndex() != -1)){
	  pnlLoyaltyManagement.nextPage();
		  this.updateTableSelection(pnlLoyaltyManagement);
      theAppMgr.showPageNumber(e, pnlLoyaltyManagement.getCurrentPageNumber() + 1
          , pnlLoyaltyManagement.getTotalPages());
	  }else if((e.getComponent() == pnlRewardDisplay) || (pnlRewardDisplay.getSelectedRowIndex() != -1)){
      pnlRewardDisplay.nextPage();
	      this.updateTableSelection(pnlRewardDisplay);
      theAppMgr.showPageNumber(e, pnlRewardDisplay.getCurrentPageNumber() + 1
          , pnlRewardDisplay.getTotalPages());
      }else if((e.getComponent() == pnlPremioHistory) || (pnlPremioHistory.getSelectedRowIndex() != -1)){
	      pnlPremioHistory.nextPage();
	      this.updateTableSelection(pnlPremioHistory);
	      theAppMgr.showPageNumber(e, pnlPremioHistory.getCurrentPageNumber() + 1
	          , pnlPremioHistory.getTotalPages());
      }
  }

  /**
   * put your documentation comment here
   * @param e
   */
  public void pageUp(MouseEvent e) {	  
	  //pnlLoyaltyManagement.getTable().clearSelection();
	  //pnlRewardDisplay.getTable().clearSelection();	  	  
	  if((e.getComponent() == pnlLoyaltyManagement) || (pnlLoyaltyManagement.getSelectedRowIndex() != -1)){
      pnlLoyaltyManagement.prevPage();
	      this.updateTableSelection(pnlLoyaltyManagement);
      theAppMgr.showPageNumber(e, pnlLoyaltyManagement.getCurrentPageNumber() + 1
          , pnlLoyaltyManagement.getTotalPages());
	  }else if((e.getComponent() == pnlRewardDisplay) || (pnlRewardDisplay.getSelectedRowIndex() != -1)){
      pnlRewardDisplay.prevPage();
	      this.updateTableSelection(pnlRewardDisplay);
      theAppMgr.showPageNumber(e, pnlRewardDisplay.getCurrentPageNumber() + 1
          , pnlRewardDisplay.getTotalPages());    
	  }else if((e.getComponent() == pnlPremioHistory) || (pnlPremioHistory.getSelectedRowIndex() != -1)){
	      pnlPremioHistory.prevPage();
	      this.updateTableSelection(pnlPremioHistory);
	      theAppMgr.showPageNumber(e, pnlPremioHistory.getCurrentPageNumber() + 1
	          , pnlPremioHistory.getTotalPages());
	  }
  }

  // private int processQuery(ItemSearchString itemSearchString)
  //   {
  //     try
  //     {
  //
  //       cmsItems = (CMSItem []) CMSItemHelper.findItems(theAppMgr, itemSearchString);
  //
  //       if(cmsItems ==null || cmsItems.length <1)
  //       {
  //         showNoItemFound();
  //         txtSKU.requestFocus();
  //         return 0;
  //       }
  //       return loadItems(cmsItems);
  //     }
  //     catch(Exception e)
  //     {
  //       e.printStackTrace();
  //       showNoItemFound();
  //       txtSKU.requestFocus();
  //     }
  //     return -1;
  //   }
  /**
   * Sets the editArea
   * @param sMessage String
   * @param sCommand String
   * @param iMask int
   */
  public void setEditArea(String sMessage, String sCommand, int iMask) {
    theAppMgr.setEditAreaFocus();
    if (sCommand != null) {
      theAppMgr.setSingleEditArea(sMessage, sCommand, iMask);
    } else {
      theAppMgr.setSingleEditArea(sMessage);
    }
  }

  /**
   * Loads the Loyalty Card
   * @return Loyalty[]
   */
  Loyalty[] loadLoyaltyCard() {
    Loyalty card = new Loyalty();
    Loyalty loadCard[] = new Loyalty[1];
    Date d = new Date("10/03/2004");
    try {
      card.setLoyaltyNumber("AO123456789");
      card.setStoreType("Armani");
      card.setIssuedBy("44332211");
      card.setIssueDate(d);
      card.setCurrBalance(431);
      card.setLifeTimeBalance(10431);
      card.setStatus(true);
    } catch (Exception e) {
      e.printStackTrace();
    }
    loadCard[0] = card;
    return loadCard;
  }

  //  RewardCard[] loadRewardCard() {
  //    RewardCard card = new RewardCard();
  //    RewardCard loadCard[] = new RewardCard[4];
  //    Date d = new Date("10/03/2004");
  //    ArmCurrency c = new ArmCurrency(100);
  //    try {
  //      card.setId("987654321");
  //
  //      card.setCreateDate(d);
  //      card.setExpDate(d);
  //      card.setAmount(c);
  //      card.setStatus(false);
  //    }
  //    catch (Exception e) {
  //
  //    }
  //    loadCard[0] = card;
  //    loadCard[1] = card;
  //    loadCard[2] = card;
  //    loadCard[3] = card;
  //    return loadCard;
  //  }
  /**
   * If a loyalty card is eligible for reward card
   * @param loyaltyCardVal Loyalty
   * @return boolean
   */
  public boolean eligibleForRewardCard(Loyalty loyaltyCardVal) {
    try {
      ConfigMgr config = new ConfigMgr("loyalty.cfg");
      String loyaltyRewardRatio = config.getString("LOYALTY_REWARD_REDEMPTION_RATIO");
      String loyaltyAmount = config.getString("LOYALTY_REWARD_AMOUNT");
      double points = (new Double(loyaltyRewardRatio)).doubleValue()
          * (new Double(loyaltyAmount)).doubleValue();
      if (loyaltyCardVal.getStatus() == false)
        return false;
      if (loyaltyCardVal.getCurrBalance() >= points) {
        return true;
      }
    } catch (Exception e) {
      e.printStackTrace();
    }
    return false;
  }

  /**
   * MP: Home pressed at customer display exits transaction with no message
   * @return
   */
 public boolean isHomeAllowed() {
		CMSCompositePOSTransaction cmsTxn = (CMSCompositePOSTransaction) theAppMgr
				.getStateObject("TXN_POS");
		if (cmsTxn == null) {
			return (true);
		}
		/*
		 * Added by Yves Agbessi (05-Dec-2017) Handles the posting of the Sign
		 * Off event for Fiscal Solutions Service START--
		 */
		boolean goToHomeView = (theAppMgr
				.showOptionDlg(
						res.getString("Cancel Transaction"),
						res.getString("Are you sure you want to cancel this transaction?")));
		if (goToHomeView) {
			ARMFSBridge.postARMSignOffTransaction((CMSEmployee) theOpr);
		}

	return goToHomeView;
		/*
		 * --END
		*/

		}


  /**
   * put your documentation comment here
   * @param loyalty
   * @return
   */
  private boolean isValidInStore(Loyalty loyalty) {
    ConfigMgr loyaltyConfigMgr = new ConfigMgr("loyalty.cfg");
    String brandID = ((CMSStore)theAppMgr.getGlobalObject("STORE")).getBrandID();
    String loyaltyStrBrandID = loyaltyConfigMgr.getString(loyalty.getStoreType() + ".TYPE");
    if (loyaltyStrBrandID != null && brandID.trim().equalsIgnoreCase(loyaltyStrBrandID.trim()))
      return true;
    //else
    return false;
  }

  private void changeLoyaltyCardStatus(CMSActionEvent anEvent, boolean status){
	boolean activate = true;
  	if (pnlLoyaltyManagement.getSelectedItem() != null) {
  		  Loyalty updateLoyaltyCard = pnlLoyaltyManagement.getSelectedItem();
  		  // if the card is already active, simply return
  		  if(updateLoyaltyCard.getStatus() == status){
  			  anEvent.consume();
  			  theAppMgr.setSingleEditArea(res.getString("Select Option"));
  			  return;
  		  }else{
    		  // if another LoyaltyCard of same store type is active, then display error message
			  if(status == activate && loyaltyList != null){
	    		  for(int i=0; i<loyaltyList.size(); i++){
	    			  if(((Loyalty)loyaltyList.get(i)).getStoreType().trim().equalsIgnoreCase(updateLoyaltyCard.getStoreType().trim())
	    					  && ((Loyalty)loyaltyList.get(i)).getLoyaltyNumber() != updateLoyaltyCard.getLoyaltyNumber()
	    					  && ((Loyalty)loyaltyList.get(i)).getStatus() == true){
	    				  anEvent.consume();
	    				  theAppMgr.showErrorDlg(res.getString("Loyalty Card of same Type is already active"));
	    				  theAppMgr.setSingleEditArea(res.getString("Select Option"));
	    				  return;
	    			  }
	    		  }
			  }
    		  //updateLoyaltyCard.setStatus(status);
    		  try{
    			  LoyaltyHelper.setStatus(theAppMgr, updateLoyaltyCard.getLoyaltyNumber(), status);
    		  }catch(Exception ex){
    			System.out.println("!!!Unable to update status of Loyalty card : " + updateLoyaltyCard.getLoyaltyNumber());
    			ex.printStackTrace();
    		  }

    	      Loyalty loyaltyCard = this.getLoyaltyFromList(loyaltyList, updateLoyaltyCard.getLoyaltyNumber());
    	      loyaltyCard.setStatus(status);

    		  loadLoyaltyItems((Loyalty[])loyaltyList.toArray(new Loyalty[0]));
    		  setStoreLoyaltyCardForTxnCustomer(loyaltyList);
    		  anEvent.consume();
    		  theAppMgr.setSingleEditArea(res.getString("Select Option"));
  		  }
        } else {
          anEvent.consume();
          theAppMgr.showErrorDlg(res.getString("Select a loyalty card to proceed"));
          theAppMgr.setSingleEditArea(res.getString("Select Option"));
        }
  }

  private void setStoreLoyaltyCardForTxnCustomer(ArrayList loyaltyList){
	  if(loyaltyList == null || loyaltyList.size() < 1) return;

	  CMSCompositePOSTransaction theTxn = (CMSCompositePOSTransaction)theAppMgr.getStateObject("TXN_POS");
	  if(theTxn == null
			|| theTxn.getCustomer() == null
			|| !(theTxn.getCustomer().getId().trim().equalsIgnoreCase(((Loyalty)loyaltyList.get(0)).getCustomer().getId().trim())))
		  return;  // if the txn customer is different from the customer being modified, simply return

	  ConfigMgr loyaltyConfigMgr = new ConfigMgr("loyalty.cfg");
	  String brandID = ((CMSStore)theTxn.getStore()).getBrandID();

	  for(int i=0; i<loyaltyList.size(); i++){
		  String loyaltyStrBrandID = loyaltyConfigMgr.getString(((Loyalty)loyaltyList.get(i)).getStoreType() + ".TYPE");
	      if (loyaltyStrBrandID != null
	    	  && brandID.trim().equalsIgnoreCase(loyaltyStrBrandID.trim())
	          && ((Loyalty)loyaltyList.get(i)).getStatus()== true) {
	    	  // since only one loyalty card for this store type can be active, set this card for the txn.
			        theTxn.setLoyaltyCard((Loyalty)loyaltyList.get(i));
			        return;
	      }
	  }

	  theTxn.setLoyaltyCard(null);
}

  private Loyalty getLoyaltyFromList(ArrayList loyaltyList, String loyaltyNumber){
	  if(loyaltyList == null || loyaltyNumber == null)
		  return null;
	  else{
		  Loyalty loyaltyCard = null;
		  for(int i=0; i<loyaltyList.size(); i++){
			  loyaltyCard = (Loyalty)loyaltyList.get(i);
			  if(loyaltyCard.getLoyaltyNumber().trim().equalsIgnoreCase(loyaltyNumber.trim()))
				  return loyaltyCard;
		  }
		  return null;
	  }
  }

  private void printPremioDetails() {
      // Must make sure that at least one row is selected.
      Loyalty loyalty = pnlLoyaltyManagement.getSelectedItem();
      if (loyalty == null) {
          theAppMgr.showErrorDlg(res.getString("Select a loyalty card to proceed"));
          return;
      }
      // Create app model
      CMSLoyaltyAppModel clam = new CMSLoyaltyAppModel(loyalty);
      // Create RDO
      // Print receipt
      if (ReceiptConfigInfo.getInstance().isProducingRDO()) {
          String fileName = ReceiptConfigInfo.getInstance().getPathForRDO() +
              "CMSPremioPointDetails" + ".rdo";
          try {
            ObjectStore objectStore = new ObjectStore(fileName);
            objectStore.write(clam);
          } catch (Exception e) {
            System.out.println("exception on writing object to blueprint folder: "
                + e);
          }
        }
        Object[] arguments = {
            clam
        };
        CMSEmployee nullOperator = null;
        ReceiptLocaleSetter localeSetter = new ReceiptLocaleSetter((CMSStore)CMSApplet.theStore,
            (CMSEmployee)nullOperator);
        ReceiptFactory receiptFactory = new ReceiptFactory(arguments, "CMSPremioPointDetails");
        localeSetter.setLocale(receiptFactory);
        receiptFactory.print(theAppMgr);
  }
}
