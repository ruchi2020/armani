/*
 * put your module comment here
 * formatted with JxBeauty (c) johann.langhofer@nextra.at
 */


package com.chelseasystems.cs.swing.dlg;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import com.chelseasystems.cr.swing.CMSApplet;
import com.chelseasystems.cs.address.Address;
import com.chelseasystems.cr.appmgr.IApplicationManager;
import com.chelseasystems.cs.swing.panel.AddressPanel;
import com.chelseasystems.cs.swing.panel.QASAddressPanel;
import com.chelseasystems.cs.swing.panel.ViewAddressPanel;
import com.chelseasystems.cr.rules.*;


/**
 * <p>
 * Title: AddressDlg
 * </p>
 *
 * Description: Displays address in dialog
 *
 * Copyright: Copyright (c) 2005
 *
 * <p>
 * Company:Skill Net inc
 * </p>
 *
 * @author Manpreet S Bawa
 * @version 1.0
 */
/*
 History:
 +------+------------+-----------+-----------+----------------------------------------------------+
 | Ver# | Date       | By        | Defect #  | Description                                        |
 +------+------------+-----------+-----------+----------------------------------------------------+
 | 1    | 03-21-2005 | Manpreet  | N/A       | POS_104665_FS_CustomerManagement_Rev2              |
 +------+------------+-----------+-----------+----------------------------------------------------+
 */
public class AddressDlg extends JDialog implements ActionListener {
  private Address address;
  private AddressPanel pnlAddress;
  private JButton btnOk;
  private JButton btnCancel;
  private JPanel pnlButtons;
  private IApplicationManager theAppMgr;
  private ViewAddressPanel pnlViewAddress;
  private int iRowToModify;
  private boolean bNewAddress;
  private int iAddressIndexInList;
  java.util.ResourceBundle res = com.chelseasystems.cr.util.ResourceManager.getResourceBundle();

  /**
   * put your documentation comment here
   * @param   Frame frame
   * @param   IApplicationManager theAppMgr
   * @param   ViewAddressPanel pnlViewAddress
   * @param   boolean bNewAddress
   */
  public AddressDlg(Frame frame, IApplicationManager theAppMgr, ViewAddressPanel pnlViewAddress
      , boolean bNewAddress) {
    super(frame, "Add/Modify Address", true);
    this.theAppMgr = theAppMgr;
    this.pnlViewAddress = pnlViewAddress;
    this.bNewAddress = bNewAddress;
    iAddressIndexInList = -1;
    try {
      init();
    } catch (Exception ex) {
      ex.printStackTrace();
    }
    if (bNewAddress) {
      address = new Address();
      if (pnlViewAddress.getNumberAddresses() < 1)
        address.setUseAsPrimary(true);
      pnlAddress.setEnabled(true);
    } else {
      this.address = pnlViewAddress.getSelectedAddress();
      iRowToModify = pnlViewAddress.getSelectedRowIndex();
      SwingUtilities.invokeLater(new Runnable() {

        /**
         * put your documentation comment here
         */
        public void run() {
          SwingUtilities.invokeLater(new Runnable() {

            /**
             * put your documentation comment here
             */
            public void run() {
              pnlAddress.setAddress(address);
              pnlAddress.setModifyEnabled(true);
            }
          });
        }
      });
    }
  }

  /**
   * put your documentation comment here
   * @param theAppMgr
   */
  public void setAppMgr(IApplicationManager theAppMgr) {
    pnlAddress.setAppMgr(theAppMgr);
  }

  /**
   * put your documentation comment here
   */
  private void init()
      throws Exception {
    pnlAddress = new AddressPanel();
    pnlButtons = new JPanel(new BorderLayout());
    btnOk = theAppMgr.getTheme().getDefaultBtn();
    btnOk.setText("Ok");
    btnOk.setMnemonic('O');
    btnOk.addActionListener(this);
    pnlButtons.add(btnOk, BorderLayout.WEST);
    btnCancel = theAppMgr.getTheme().getDefaultBtn();
    btnCancel.setText("Cancel");
    btnCancel.setMnemonic('C');
    btnCancel.addActionListener(this);
    pnlButtons.add(btnCancel, BorderLayout.EAST);
    pnlButtons.setBackground(theAppMgr.getBackgroundColor());
    this.getContentPane().setLayout(new BorderLayout());
    this.getContentPane().add(pnlAddress, BorderLayout.CENTER);
    this.getContentPane().add(pnlButtons, BorderLayout.SOUTH);
    this.setResizable(false);
    setSize(500, 450);
  }

  /**
   * put your documentation comment here
   * @param visible
   */
  public void setVisible(boolean visible) {
    if (visible) {
      Dimension d = Toolkit.getDefaultToolkit().getScreenSize();
      setLocation((d.width - getSize().width) / 2, (d.height - getSize().height) / 2);
    }
    super.setVisible(visible);
  }

  /**
   * put your documentation comment here
   * @param ae
   */
  public void actionPerformed(ActionEvent ae) {
    String sCommand = ae.getActionCommand();
    Address qasVerifiedAddress = null;
    if (sCommand.equals("Ok")) {
      if (bNewAddress) {
        if (verifyAddressType()) {
          showAddressTypeErrDlg();
          return;
        }
        if (!verifyPhoneNumbers())
          return;
        try {
          if (!pnlAddress.isValidAddress()) {
            theAppMgr.showErrorDlg(res.getString("Address is not valid."));
            return;
          }
          // PCR67 QAS for US
          if (pnlAddress.getCurrentAddressPanel() instanceof QASAddressPanel) {
            qasVerifiedAddress = ((QASAddressPanel)pnlAddress.getCurrentAddressPanel()).
                qasVerifyAddress();
            // YES/NO dialog
            if (qasVerifiedAddress != null
                && ((qasVerifiedAddress.getQasVerifyLevel() != null
                && !qasVerifiedAddress.getQasVerifyLevel().equals("Verified"))
                || qasVerifiedAddress.getQasVerifyLevel() == null)) {
              if (!theAppMgr.showOptionDlg("", res.getString("Do you want to continue?"))) {
                return;
              }
            }
          }
          address = pnlAddress.getAddress();
          pnlViewAddress.addAddress(address);
          pnlViewAddress.setPersistRequired(true);
        } catch (BusinessRuleException ex) {
          theAppMgr.showErrorDlg(ex.getMessage());
          return;
        }
      } else if (!bNewAddress) {
        if (verifyAddressType() && iRowToModify != iAddressIndexInList) {
          showAddressTypeErrDlg();
          return;
        }
        if (!verifyPhoneNumbers())
          return;
        try {
          if (pnlAddress.isValidAddress()) {
            // PCR67 QAS for US
            if (pnlAddress.getCurrentAddressPanel() instanceof QASAddressPanel) {
              qasVerifiedAddress = ((QASAddressPanel)pnlAddress.getCurrentAddressPanel()).
                  qasVerifyAddress();
              // YES/NO dialog
              if (qasVerifiedAddress != null
                  && ((qasVerifiedAddress.getQasVerifyLevel() != null
                  && !qasVerifiedAddress.getQasVerifyLevel().equals("Verified"))
                  || qasVerifiedAddress.getQasVerifyLevel() == null)) {
                if (!theAppMgr.showOptionDlg("", res.getString("Do you want to continue?"))) {
                  return;
                }
              }
            }
            address = pnlAddress.getAddress();
            pnlViewAddress.setAddress(address, iRowToModify);
            pnlViewAddress.setPersistRequired(true);
          } else {
            theAppMgr.showErrorDlg(res.getString("Address is not valid."));
            return;
          }
        } catch (BusinessRuleException ex1) {
          theAppMgr.showErrorDlg(ex1.getMessage());
          return;
        }
      }
    }
    this.dispose();
  }

  /**
   * put your documentation comment here
   */
  private void showAddressTypeErrDlg() {
    theAppMgr.showErrorDlg(CMSApplet.res.getString(
        "Address type already exists, please select a different type"));
  }

  /**
   * put your documentation comment here
   * @return
   */
  private boolean verifyPhoneNumbers() {
    int iDuplicatePhoneIdx = pnlAddress.containsDuplicatePhone();
    if (iDuplicatePhoneIdx != -1) {
      theAppMgr.showErrorDlg(res.getString("Duplicate phone type, please select a different type"));
      if (iDuplicatePhoneIdx == 1) {
        pnlAddress.requestFocusToPrimaryPhone();
      } else if (iDuplicatePhoneIdx == 2) {
        pnlAddress.requestFocusToSecondaryPhone();
      } else if (iDuplicatePhoneIdx == 3) {
        pnlAddress.requestFocusToTernaryPhone();
      }
      return false;
    }
    return true;
  }

  /**
   * put your documentation comment here
   * @return
   */
  private boolean verifyAddressType() {
    iAddressIndexInList = pnlViewAddress.addressTypeExists(pnlAddress.getAddressType());
    if (iAddressIndexInList != -1) {
      return true;
    }
    return false;
  }
}

