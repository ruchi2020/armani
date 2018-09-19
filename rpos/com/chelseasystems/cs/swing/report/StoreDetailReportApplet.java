/*
 * @copyright (c) 2001 Chelsea Market Systems LLC
 *
 * formatted with JxBeauty (c) johann.langhofer@nextra.at
 */


package com.chelseasystems.cs.swing.report;

import java.awt.*;
import java.awt.event.*;
import java.util.*;
import javax.swing.*;
import javax.swing.border.*;
import com.chelseasystems.cr.swing.CMSApplet;
import com.chelseasystems.cs.swing.menu.MenuConst;
import com.chelseasystems.cs.store.*;
import com.chelseasystems.cs.employee.*;
import com.chelseasystems.cr.swing.bean.*;
import com.chelseasystems.cr.swing.event.CMSActionEvent;
import com.chelseasystems.cr.util.ObjectStore;
import com.chelseasystems.cr.receipt.ReceiptLocaleSetter;
import com.chelseasystems.cs.receipt.ReceiptBlueprintInventory;
import com.chelseasystems.rb.ReceiptFactory;
import com.chelseasystems.cr.util.ObjectStore;
import com.chelseasystems.cr.receipt.ReceiptConfigInfo;
import com.chelseasystems.cs.pos.CMSCompositePOSTransaction;
import com.ga.fs.fsbridge.ARMFSBridge;


/**
 */
public class StoreDetailReportApplet extends CMSApplet {
  private CMSStore aStore;
  JCMSComboBox cbxStore = new CustomComboBox();
  JCMSTextField fldAddress = new JCMSTextField();
  JCMSTextField fldCity = new JCMSTextField();
  JCMSTextField fldState = new JCMSTextField();
  JCMSTextField fldZip = new JCMSTextField();
  JCMSTextField fldCountry = new JCMSTextField();
  JCMSTextField fldPriPhone = new JCMSTextField();
  JCMSTextField fldSecPhone = new JCMSTextField();
  JCMSTextField fldFaxPhone = new JCMSTextField();
  JCMSTextField fldArea = new JCMSTextField();
  JCMSTextField fldAreaShortName = new JCMSTextField();
  JCMSTextField fldDistrictShortName = new JCMSTextField();
  JCMSTextField fldDistrict = new JCMSTextField();
  JCMSTextField fldRegionalShortName = new JCMSTextField();
  JCMSTextField fldRegional = new JCMSTextField();
  JCMSTextField fldManager = new JCMSTextField();
  JCMSTextField fldManagerShortName = new JCMSTextField();
  JCMSTextField fldAssistant = new JCMSTextField();
  JCMSTextField fldAssistantShortName = new JCMSTextField();
  JCMSTextField fldFOT = new JCMSTextField();
  JCMSTextField fldFOTShortName = new JCMSTextField();
  JCMSTextField fldTraineeShortName = new JCMSTextField();
  JCMSTextField fldTrainee = new JCMSTextField();
  //private CMSStore cmsStores[];
  Vector storeComboBoxModelVec;
  Map storeIdMap;
  String SELECT_STORE_STR = res.getString("Select Store");

  //Initialize the applet
  public void init() {
    try {
      jbInit();
      populateStoreIdsAndTypes();
      cbxStore.addItemListener(new ItemListener() {

        /**
         * put your documentation comment here
         * @param e
         */
        public void itemStateChanged(ItemEvent e) {
          if (e.getStateChange() != ItemEvent.SELECTED)
            return;
          String storeId = null;
          if (cbxStore.getSelectedIndex() > -1
              && storeComboBoxModelVec.elementAt(cbxStore.getSelectedIndex()) instanceof CMSStore) {
            CMSStore tmpStore = (CMSStore)storeComboBoxModelVec.elementAt(cbxStore.getSelectedIndex());
            if (tmpStore == null || tmpStore.getId().trim().length() == 0) {
              theAppMgr.showMenu(MenuConst.PREV_ONLY, theOpr);
              aStore = null;
            } else {
              try {
                storeId = tmpStore.getId();
                aStore = CMSStoreHelper.findById(theAppMgr, storeId);
                if (aStore != null)
                  theAppMgr.showMenu(MenuConst.PRINT_ONLY, theOpr, theAppMgr.PREV_BUTTON);
              } catch (Exception ex) {
                ex.printStackTrace();
              }
            }
          } else {
            theAppMgr.showMenu(MenuConst.PREV_ONLY, theOpr);
            aStore = null;
          }
          clearFields();
          populateScreen(aStore);
//          enterStore();
        }
      });
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  /**
   * Start the applet
   */
  public void start() {
    theAppMgr.setTransitionColor(theAppMgr.getBackgroundColor());
    theOpr = (CMSEmployee)theAppMgr.getStateObject("OPERATOR");
    theAppMgr.showMenu(MenuConst.PREV_ONLY, theOpr);
    enableFields(false);
    clearFields();
    enterStore();
  }

  /**
   * Stop the applet
   */
  public void stop() {}

  /**
   * callback when an application tool bar button is pressed
   */
  public void editAreaEvent(String Command, String sEdit) {
    try {
      if (Command.equals("NUMBER")) {
        aStore = CMSStoreHelper.findById(theAppMgr, sEdit);
        if (aStore == null) {
          theAppMgr.showErrorDlg(res.getString("Store Id") + ": " + sEdit
              + res.getString(" is not a valid store Id."));
        } else {
          clearFields();
          populateScreen(aStore);
          theAppMgr.showMenu(MenuConst.PRINT_ONLY, theOpr, theAppMgr.PREV_BUTTON);
        }
        enterStore();
      }
    } catch (Exception e) {
      System.out.println("Exception in EDITAREA EVENT" + e);
    }
  }

  /**
   */
  public void appButtonEvent(CMSActionEvent anEvent) {
    String command = anEvent.getActionCommand();
    if (command.equals("PRINT")) {
      anEvent.consume();
      Object[] arguments = {aStore
      };
      if (ReceiptConfigInfo.getInstance().isProducingRDO()) {
        String fileName = ReceiptConfigInfo.getInstance().getPathForRDO() + "StoreDetailReport.rdo";
        try {
          ObjectStore objectStore = new ObjectStore(fileName);
          objectStore.write(aStore);
        } catch (Exception e) {
          System.out.println("exception on writing object to blueprint folder: " + e);
        }
      }
      ReceiptFactory receiptFactory = new ReceiptFactory(arguments
          , ReceiptBlueprintInventory.CMSStoreInfo);
      // print the report in the employee's preferred language
      ReceiptLocaleSetter localeSetter = new ReceiptLocaleSetter((CMSStore)theStore
          , (CMSEmployee)theOpr);
      localeSetter.setLocale(receiptFactory);
      receiptFactory.print(theAppMgr);
    }
  }

  //Get Applet information
  public String getScreenName() {
    return (res.getString("Store Information"));
  }

  /**
   * @return
   */
  public String getVersion() {
    return ("$Revision: 1.1 $");
  }

  /**
   */
  private void enterStore() {
    theAppMgr.setSingleEditArea(res.getString("Enter the store number to view details."), "NUMBER");
  }

  /**
   */
  private void populateScreen(CMSStore store) {
    if (store == null) {
      cbxStore.setSelectedItem(SELECT_STORE_STR);
      return;
    }
    //else
    cbxStore.setSelectedIndex(storeComboBoxModelVec.indexOf(storeIdMap.get(new Integer(aStore.getId()))));
    fldAddress.setText(store.getAddress());
    fldCity.setText(store.getCity());
    fldState.setText(store.getState());
    fldZip.setText(store.getZipCode());
    fldCountry.setText(store.getCountry());
    fldPriPhone.setText(store.getTelephone() == null ? "" : store.getTelephone().getFormattedNumber());
    fldSecPhone.setText(store.getAlternateTelephone() == null ? ""
        : store.getAlternateTelephone().getFormattedNumber());
    fldFaxPhone.setText(store.getFacsimile() == null ? "" : store.getFacsimile().getFormattedNumber());
    CMSEmployee emp = null;
    /*try
     {
     emp = CMSEmployeeHelper.findById(theAppMgr, store.getAreaManagerId());
     if(emp != null)
     {
     fldArea.setText(emp.getFirstName() + " " + emp.getLastName());
     fldAreaShortName.setText(theAppMgr, emp.getShortName());
     }
     }
     catch(Exception ex)
     {
     System.err.println("StoreDetailApplet.populateScreen()1->" + ex);
     }*/
    try {
      emp = CMSEmployeeHelper.findById(theAppMgr, store.getDistrictManagerId());
      if (emp != null) {
        fldDistrictShortName.setText(emp.getFirstName() + " " + emp.getLastName());
        fldDistrict.setText(emp.getShortName());
      }
    } catch (Exception ex) {
      System.err.println("StoreDetailApplet.populateScreen()2->" + ex);
    }
    try {
      emp = CMSEmployeeHelper.findById(theAppMgr, store.getRegionalManagerId());
      if (emp != null) {
        fldRegionalShortName.setText(emp.getFirstName() + " " + emp.getLastName());
        fldRegional.setText(emp.getShortName());
      }
    } catch (Exception ex) {
      System.err.println("StoreDetailApplet.populateScreen()3->" + ex);
    }
    try {
      emp = CMSEmployeeHelper.findById(theAppMgr, store.getManagerId());
      if (emp != null) {
        fldManager.setText(emp.getFirstName() + " " + emp.getLastName());
        fldManagerShortName.setText(emp.getShortName());
      }
    } catch (Exception ex) {
      System.err.println("StoreDetailApplet.populateScreen()4->" + ex);
    }
    try {
      emp = CMSEmployeeHelper.findById(theAppMgr, store.getAsstManagerId());
      if (emp != null) {
        fldAssistant.setText(emp.getFirstName() + " " + emp.getLastName());
        fldAssistantShortName.setText(emp.getShortName());
      }
    } catch (Exception ex) {
      System.err.println("StoreDetailApplet.populateScreen()5->" + ex);
    }
    /*try {
         emp = CMSEmployeeHelper.findById(theAppMgr, store.getFieldOperationsTrainerId());
         if (emp != null) {
         fldFOT.setText(emp.getFirstName() + " " + emp.getLastName());
         fldFOTShortName.setText(emp.getShortName());
         }
         } catch (Exception ex) {
         System.err.println("StoreDetailApplet.populateScreen()6->" + ex);
         }
         try {
         emp = CMSEmployeeHelper.findById(theAppMgr, store.getManagerTraineeId());
         if (emp != null) {
         fldTraineeShortName.setText(emp.getFirstName() + " " + emp.getLastName());
         fldTrainee.setText(emp.getShortName());
         }
         } catch (Exception ex) {
         System.err.println("StoreDetailApplet.populateScreen()7->" + ex);
         }*/

  }

  /**
   */
  private void clearFields() {
    if (aStore != null)
      cbxStore.setSelectedIndex(storeComboBoxModelVec.indexOf(storeIdMap.get(new Integer(aStore.
          getId()))));
    else
      cbxStore.setSelectedItem(SELECT_STORE_STR);
    fldAddress.setText("");
    fldCity.setText("");
    fldState.setText("");
    fldZip.setText("");
    fldCountry.setText("");
    fldPriPhone.setText("");
    fldSecPhone.setText("");
    fldFaxPhone.setText("");
    fldArea.setText("");
    fldAreaShortName.setText("");
    fldDistrictShortName.setText("");
    fldDistrict.setText("");
    fldRegionalShortName.setText("");
    fldRegional.setText("");
    fldManager.setText("");
    fldManagerShortName.setText("");
    fldAssistant.setText("");
    fldAssistantShortName.setText("");
    fldFOT.setText("");
    fldFOTShortName.setText("");
    fldTraineeShortName.setText("");
    fldTrainee.setText("");
  }

  /**
   */
  private void enableFields(boolean enabled) {
    //cbxStore.setEnabled(enabled);
    fldAddress.setEnabled(enabled);
    fldCity.setEnabled(enabled);
    fldState.setEnabled(enabled);
    fldZip.setEnabled(enabled);
    fldCountry.setEnabled(enabled);
    fldPriPhone.setEnabled(enabled);
    fldSecPhone.setEnabled(enabled);
    fldFaxPhone.setEnabled(enabled);
    fldArea.setEnabled(enabled);
    fldAreaShortName.setEnabled(enabled);
    fldDistrictShortName.setEnabled(enabled);
    fldDistrict.setEnabled(enabled);
    fldRegionalShortName.setEnabled(enabled);
    fldRegional.setEnabled(enabled);
    fldManager.setEnabled(enabled);
    fldManagerShortName.setEnabled(enabled);
    fldAssistant.setEnabled(enabled);
    fldAssistantShortName.setEnabled(enabled);
    fldFOT.setEnabled(enabled);
    fldFOTShortName.setEnabled(enabled);
    fldTraineeShortName.setEnabled(enabled);
    fldTrainee.setEnabled(enabled);
  }

  /**
   * put your documentation comment here
   * @return
   */
  private JPanel acquireStorePanel() {
    JPanel rtnVal = new JPanel();
    rtnVal.setBorder(new TitledBorder(BorderFactory.createLineBorder(Color.gray, 1)
        , res.getString("Store Information")));
    rtnVal.setLayout(new GridBagLayout());
    rtnVal.setOpaque(false);
    JCMSLabel lblStore = new JCMSLabel();
    JCMSLabel lblAddress = new JCMSLabel();
    JCMSLabel lblCity = new JCMSLabel();
    JCMSLabel lblCountry = new JCMSLabel();
    JCMSLabel lblPriPhone = new JCMSLabel();
    JCMSLabel lblSecPhone = new JCMSLabel();
    JCMSLabel lblFaxPhone = new JCMSLabel();
    JCMSLabel lblState = new JCMSLabel();
    JCMSLabel lblZip = new JCMSLabel();
    lblStore.setText(res.getString("Store Name") + ":");
    lblAddress.setText(res.getString("Address") + ":");
    lblCity.setText(res.getString("City") + ":");
    lblCountry.setText(res.getString("Country") + ":");
    lblPriPhone.setText(res.getString("Primary Phone Number") + ":");
    lblSecPhone.setText(res.getString("Secondary Phone Number") + ":");
    lblFaxPhone.setText(res.getString("Fax Phone Number") + ":");
    lblState.setText(res.getString("State") + ":");
    lblZip.setText(res.getString("Zip Code") + ":");
    rtnVal.add(lblStore
        , new GridBagConstraints(0, 0, 3, 1, 0.0, 0.0, GridBagConstraints.WEST
        , GridBagConstraints.HORIZONTAL, new Insets(5, 10, 0, 0), 0, 0));
    rtnVal.add(lblPriPhone
        , new GridBagConstraints(3, 0, 3, 1, 0.1, 0.0, GridBagConstraints.WEST
        , GridBagConstraints.HORIZONTAL, new Insets(5, 40, 0, 0), 200, 0));
    rtnVal.add(cbxStore
        , new GridBagConstraints(0, 1, 3, 1, 0.0, 0.0, GridBagConstraints.WEST
        , GridBagConstraints.HORIZONTAL, new Insets(0, 10, 0, 0), 0, 5));
    rtnVal.add(fldPriPhone
        , new GridBagConstraints(3, 1, 3, 1, 0.1, 0.0, GridBagConstraints.WEST
        , GridBagConstraints.HORIZONTAL, new Insets(0, 40, 0, 10), 0, 5));
    rtnVal.add(lblAddress
        , new GridBagConstraints(0, 2, 3, 1, 0.0, 0.0, GridBagConstraints.WEST
        , GridBagConstraints.HORIZONTAL, new Insets(9, 10, 0, 0), 0, 0));
    rtnVal.add(lblSecPhone
        , new GridBagConstraints(3, 2, 3, 1, 0.1, 0.0, GridBagConstraints.WEST
        , GridBagConstraints.HORIZONTAL, new Insets(9, 40, 0, 0), 0, 0));
    rtnVal.add(fldAddress
        , new GridBagConstraints(0, 3, 3, 1, 0.0, 0.0, GridBagConstraints.WEST
        , GridBagConstraints.HORIZONTAL, new Insets(5, 10, 0, 0), 0, 5));
    rtnVal.add(fldSecPhone
        , new GridBagConstraints(3, 3, 3, 1, 0.1, 0.0, GridBagConstraints.WEST
        , GridBagConstraints.HORIZONTAL, new Insets(5, 40, 0, 10), 0, 5));
    rtnVal.add(lblCity
        , new GridBagConstraints(0, 4, 1, 1, 0.0, 0.0, GridBagConstraints.WEST
        , GridBagConstraints.HORIZONTAL, new Insets(9, 10, 0, 0), 200, 0));
    rtnVal.add(lblState
        , new GridBagConstraints(1, 4, 1, 1, 0.0, 0.0, GridBagConstraints.WEST
        , GridBagConstraints.HORIZONTAL, new Insets(9, 10, 0, 0), 50, 0));
    rtnVal.add(lblZip
        , new GridBagConstraints(2, 4, 1, 1, 0.0, 0.0, GridBagConstraints.WEST
        , GridBagConstraints.HORIZONTAL, new Insets(9, 10, 0, 0), 0, 0));
    rtnVal.add(lblFaxPhone
        , new GridBagConstraints(3, 4, 3, 1, 0.1, 0.0, GridBagConstraints.WEST
        , GridBagConstraints.HORIZONTAL, new Insets(9, 40, 0, 0), 0, 0));
    rtnVal.add(fldCity
        , new GridBagConstraints(0, 5, 1, 1, 0.0, 0.0, GridBagConstraints.WEST
        , GridBagConstraints.HORIZONTAL, new Insets(5, 10, 0, 0), 0, 5));
    rtnVal.add(fldState
        , new GridBagConstraints(1, 5, 1, 1, 0.0, 0.0, GridBagConstraints.WEST
        , GridBagConstraints.HORIZONTAL, new Insets(5, 10, 0, 0), 0, 5));
    rtnVal.add(fldZip
        , new GridBagConstraints(2, 5, 1, 1, 0.0, 0.0, GridBagConstraints.WEST
        , GridBagConstraints.HORIZONTAL, new Insets(5, 10, 0, 0), 0, 5));
    rtnVal.add(fldFaxPhone
        , new GridBagConstraints(3, 5, 3, 1, 0.1, 0.0, GridBagConstraints.WEST
        , GridBagConstraints.HORIZONTAL, new Insets(5, 40, 0, 10), 0, 5));
    rtnVal.add(lblCountry
        , new GridBagConstraints(0, 6, 2, 1, 0.0, 0.0, GridBagConstraints.WEST
        , GridBagConstraints.HORIZONTAL, new Insets(9, 10, 0, 0), 0, 0));
    rtnVal.add(fldCountry
        , new GridBagConstraints(0, 7, 2, 1, 0.0, 0.0, GridBagConstraints.WEST
        , GridBagConstraints.HORIZONTAL, new Insets(5, 10, 10, 0), 0, 5));
    return (rtnVal);
  }

  /**
   * put your documentation comment here
   * @return
   */
  private JPanel acquireManagementPanel() {
    JPanel rtnVal = new JPanel();
    rtnVal.setBorder(new TitledBorder(BorderFactory.createLineBorder(Color.gray, 1)
        , res.getString("Management Team")));
    rtnVal.setLayout(new GridBagLayout());
    rtnVal.setOpaque(false);
    JCMSLabel lblArea = new JCMSLabel();
    JCMSLabel lblAreaShortName = new JCMSLabel();
    JCMSLabel lblDistrictShortName = new JCMSLabel();
    JCMSLabel lblDistrict = new JCMSLabel();
    JCMSLabel lblRegionalShortName = new JCMSLabel();
    JCMSLabel lblRegional = new JCMSLabel();
    JCMSLabel lblManager = new JCMSLabel();
    JCMSLabel lblManagerShortName = new JCMSLabel();
    JCMSLabel lblAssistant = new JCMSLabel();
    JCMSLabel lblAssistantShortName = new JCMSLabel();
    JCMSLabel lblFOT = new JCMSLabel();
    JCMSLabel lblFOTShortName = new JCMSLabel();
    JCMSLabel lblTraineeShortName = new JCMSLabel();
    JCMSLabel lblTrainee = new JCMSLabel();
    lblArea.setText(res.getString("Area Manager") + ":");
    lblAreaShortName.setText(res.getString("User Name") + ":");
    lblDistrictShortName.setText(res.getString("User Name") + ":");
    lblDistrict.setText(res.getString("District Manager") + ":");
    lblRegionalShortName.setText(res.getString("User Name") + ":");
    lblRegional.setText(res.getString("Regional Manager") + ":");
    lblManager.setText(res.getString("Store Manager") + ":");
    lblManagerShortName.setText(res.getString("User Name") + ":");
    lblAssistant.setText(res.getString("Assistant Manager") + ":");
    lblAssistantShortName.setText(res.getString("User Name") + ":");
    lblFOT.setText(res.getString("Field Operations Trainer") + ":");
    lblFOTShortName.setText(res.getString("User Name") + ":");
    lblTraineeShortName.setText(res.getString("User Name") + ":");
    lblTrainee.setText(res.getString("Manager Trainee") + ":");
    rtnVal.add(fldRegionalShortName
        , new GridBagConstraints(3, 1, 1, 1, 0.0, 0.0, GridBagConstraints.WEST
        , GridBagConstraints.HORIZONTAL, new Insets(5, 5, 0, 5), 0, 5));
    rtnVal.add(fldArea
        , new GridBagConstraints(2, 5, 1, 1, 0.0, 0.0, GridBagConstraints.WEST
        , GridBagConstraints.HORIZONTAL, new Insets(5, 20, 0, 0), 0, 5));
    rtnVal.add(lblAreaShortName
        , new GridBagConstraints(3, 4, 1, 1, 0.0, 0.0, GridBagConstraints.WEST
        , GridBagConstraints.NONE, new Insets(9, 5, 0, 0), 0, 0));
    rtnVal.add(fldAreaShortName
        , new GridBagConstraints(3, 5, 1, 1, 0.0, 0.0, GridBagConstraints.WEST
        , GridBagConstraints.HORIZONTAL, new Insets(5, 5, 0, 5), 0, 5));
    rtnVal.add(lblArea
        , new GridBagConstraints(2, 4, 1, 1, 0.0, 0.0, GridBagConstraints.WEST
        , GridBagConstraints.NONE, new Insets(9, 20, 0, 0), 0, 0));
    rtnVal.add(fldDistrict
        , new GridBagConstraints(2, 3, 1, 1, 0.0, 0.0, GridBagConstraints.WEST
        , GridBagConstraints.HORIZONTAL, new Insets(5, 20, 0, 0), 0, 5));
    rtnVal.add(fldDistrictShortName
        , new GridBagConstraints(3, 3, 1, 1, 0.0, 0.0, GridBagConstraints.WEST
        , GridBagConstraints.HORIZONTAL, new Insets(5, 5, 0, 5), 0, 5));
    rtnVal.add(lblDistrict
        , new GridBagConstraints(2, 2, 1, 1, 0.0, 0.0, GridBagConstraints.WEST
        , GridBagConstraints.NONE, new Insets(9, 20, 0, 0), 0, 0));
    rtnVal.add(lblDistrictShortName
        , new GridBagConstraints(3, 2, 1, 1, 0.0, 0.0, GridBagConstraints.WEST
        , GridBagConstraints.NONE, new Insets(9, 5, 0, 0), 0, 0));
    rtnVal.add(lblRegionalShortName
        , new GridBagConstraints(3, 0, 1, 1, 0.0, 0.0, GridBagConstraints.WEST
        , GridBagConstraints.NONE, new Insets(0, 5, 0, 0), 30, 0));
    rtnVal.add(lblRegional
        , new GridBagConstraints(2, 0, 1, 1, 0.0, 0.0, GridBagConstraints.WEST
        , GridBagConstraints.NONE, new Insets(0, 20, 0, 0), 0, 0));
    rtnVal.add(fldRegional
        , new GridBagConstraints(2, 1, 1, 1, 0.1, 0.0, GridBagConstraints.WEST
        , GridBagConstraints.HORIZONTAL, new Insets(5, 20, 0, 0), 0, 5));
    rtnVal.add(fldFOTShortName
        , new GridBagConstraints(1, 7, 1, 1, 0.0, 0.0, GridBagConstraints.WEST
        , GridBagConstraints.HORIZONTAL, new Insets(5, 5, 0, 0), 0, 5));
    rtnVal.add(fldFOT
        , new GridBagConstraints(0, 7, 1, 1, 0.0, 0.0, GridBagConstraints.WEST
        , GridBagConstraints.HORIZONTAL, new Insets(5, 5, 0, 0), 0, 5));
    rtnVal.add(lblFOT
        , new GridBagConstraints(0, 6, 1, 1, 0.0, 0.0, GridBagConstraints.WEST
        , GridBagConstraints.NONE, new Insets(9, 5, 0, 0), 0, 0));
    rtnVal.add(lblFOTShortName
        , new GridBagConstraints(1, 6, 1, 1, 0.0, 0.0, GridBagConstraints.WEST
        , GridBagConstraints.NONE, new Insets(9, 5, 0, 0), 0, 0));
    rtnVal.add(fldTrainee
        , new GridBagConstraints(0, 5, 1, 1, 0.0, 0.0, GridBagConstraints.WEST
        , GridBagConstraints.HORIZONTAL, new Insets(5, 5, 0, 0), 0, 5));
    rtnVal.add(fldTraineeShortName
        , new GridBagConstraints(1, 5, 1, 1, 0.0, 0.0, GridBagConstraints.WEST
        , GridBagConstraints.HORIZONTAL, new Insets(5, 5, 0, 0), 0, 5));
    rtnVal.add(lblTrainee
        , new GridBagConstraints(0, 4, 1, 1, 0.0, 0.0, GridBagConstraints.WEST
        , GridBagConstraints.NONE, new Insets(9, 5, 0, 0), 0, 0));
    rtnVal.add(lblTraineeShortName
        , new GridBagConstraints(1, 4, 1, 1, 0.0, 0.0, GridBagConstraints.WEST
        , GridBagConstraints.NONE, new Insets(9, 5, 0, 0), 0, 0));
    rtnVal.add(fldAssistant
        , new GridBagConstraints(0, 3, 1, 1, 0.0, 0.0, GridBagConstraints.WEST
        , GridBagConstraints.HORIZONTAL, new Insets(5, 5, 0, 0), 0, 5));
    rtnVal.add(fldAssistantShortName
        , new GridBagConstraints(1, 3, 1, 1, 0.0, 0.0, GridBagConstraints.WEST
        , GridBagConstraints.HORIZONTAL, new Insets(5, 5, 0, 0), 0, 5));
    rtnVal.add(lblAssistant
        , new GridBagConstraints(0, 2, 1, 1, 0.0, 0.0, GridBagConstraints.WEST
        , GridBagConstraints.NONE, new Insets(9, 5, 0, 0), 0, 0));
    rtnVal.add(lblAssistantShortName
        , new GridBagConstraints(1, 2, 1, 1, 0.0, 0.0, GridBagConstraints.WEST
        , GridBagConstraints.NONE, new Insets(9, 5, 0, 0), 0, 0));
    rtnVal.add(fldManager
        , new GridBagConstraints(0, 1, 1, 1, 0.0, 0.0, GridBagConstraints.WEST
        , GridBagConstraints.HORIZONTAL, new Insets(5, 5, 0, 0), 0, 5));
    rtnVal.add(fldManagerShortName
        , new GridBagConstraints(1, 1, 1, 1, 0.0, 0.0, GridBagConstraints.WEST
        , GridBagConstraints.HORIZONTAL, new Insets(5, 5, 0, 0), 0, 5));
    rtnVal.add(lblManager
        , new GridBagConstraints(0, 0, 1, 1, 0.1, 0.0, GridBagConstraints.WEST
        , GridBagConstraints.NONE, new Insets(0, 5, 0, 0), 0, 0));
    rtnVal.add(lblManagerShortName
        , new GridBagConstraints(1, 0, 1, 1, 0.0, 0.0, GridBagConstraints.WEST
        , GridBagConstraints.NONE, new Insets(0, 5, 0, 0), 30, 0));
    return (rtnVal);
  }

  //Component initialization
  private void jbInit()
      throws Exception {
    JPanel panel = new JPanel();
    panel.setLayout(new GridLayout(2, 1));
    panel.setBackground(theAppMgr.getBackgroundColor());
    panel.add(acquireStorePanel(), null);
    panel.add(acquireManagementPanel(), null);
    this.setBorder(BorderFactory.createEmptyBorder(6, 6, 6, 6));
    this.setBackground(theAppMgr.getBackgroundColor());
    this.add(panel);
    cbxStore.setAppMgr(theAppMgr);
    fldAddress.setAppMgr(theAppMgr);
    fldCity.setAppMgr(theAppMgr);
    fldState.setAppMgr(theAppMgr);
    fldZip.setAppMgr(theAppMgr);
    fldCountry.setAppMgr(theAppMgr);
    fldPriPhone.setAppMgr(theAppMgr);
    fldSecPhone.setAppMgr(theAppMgr);
    fldFaxPhone.setAppMgr(theAppMgr);
    fldArea.setAppMgr(theAppMgr);
    fldAreaShortName.setAppMgr(theAppMgr);
    fldDistrictShortName.setAppMgr(theAppMgr);
    fldDistrict.setAppMgr(theAppMgr);
    fldRegionalShortName.setAppMgr(theAppMgr);
    fldRegional.setAppMgr(theAppMgr);
    fldManager.setAppMgr(theAppMgr);
    fldManagerShortName.setAppMgr(theAppMgr);
    fldAssistant.setAppMgr(theAppMgr);
    fldAssistantShortName.setAppMgr(theAppMgr);
    fldFOT.setAppMgr(theAppMgr);
    fldFOTShortName.setAppMgr(theAppMgr);
    fldTraineeShortName.setAppMgr(theAppMgr);
    fldTrainee.setAppMgr(theAppMgr);
  }

  /**
   * MP: Home pressed at customer display exits transaction with no message
   * @return
   */
  public boolean isHomeAllowed() 
	{
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
   */
  private void populateStoreIdsAndTypes() {
    try {
      CMSStore[] cmsStores = CMSStoreHelper.findAllStores(CMSApplet.theAppMgr);
      if (cmsStores == null)
        return;
      storeIdMap = new TreeMap();
      for (int i = 0; i < cmsStores.length; i++) {
        if (cmsStores[i].getBrandID() != null && cmsStores[i].getBrandID().trim().length() > 0)
          storeIdMap.put(new Integer(cmsStores[i].getId()), cmsStores[i]);
      }
      storeComboBoxModelVec = new Vector(storeIdMap.values());
      storeComboBoxModelVec.insertElementAt(SELECT_STORE_STR, 0);
      cbxStore.removeAllItems();
      cbxStore.addItem(SELECT_STORE_STR);
      for (int iCtr = 1; iCtr < storeComboBoxModelVec.size(); iCtr++) {
        //            cbxStore.addItem(storeComboBoxModelVec.get(iCtr));
        CMSStore cmsStore = (CMSStore)storeComboBoxModelVec.get(iCtr);
        if (cmsStore.getShopDescription() != null
            && cmsStore.getShopDescription().trim().length() > 0)
          cbxStore.addItem(cmsStore.getId() + ", " + cmsStore.getShopDescription());
        else
          cbxStore.addItem(cmsStore.getId());
      }
      //        cbxStore.setModel(new DefaultComboBoxModel(storeComboBoxModelVec));
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  private class CustomComboBox extends JCMSComboBox {

    /**
     * put your documentation comment here
     * @return
     */
    public Dimension getPreferredSize() {
      return new Dimension((int)(95 * r), (int)(25 * r));
    }

    /**
     * put your documentation comment here
     * @return
     */
    public Dimension getMaximumSize() {
      return new Dimension((int)(95 * r), (int)(25 * r));
    }

    /**
     * put your documentation comment here
     * @return
     */
    public Dimension getMinimumSize() {
      return new Dimension((int)(95 * r), (int)(25 * r));
    }
  }
} //end class

