/*
 * @copyright (c) 2001 Chelsea Market Systems LLC
 *
 * formatted with JxBeauty (c) johann.langhofer@nextra.at
 */


package com.chelseasystems.cs.swing.paidouts;

import java.awt.*;
import java.awt.event.*;
import java.util.*;
import java.lang.reflect.*;
import javax.swing.*;
import javax.swing.border.*;
import javax.swing.table.*;
import javax.swing.event.*;
import com.chelseasystems.cr.appmgr.IApplicationManager;
import com.chelseasystems.cr.swing.CMSApplet;
import com.chelseasystems.cs.swing.menu.MenuConst;
import com.chelseasystems.cr.swing.bean.JCMSTable;
import com.chelseasystems.cr.swing.ScrollableTableModel;
import com.chelseasystems.cr.swing.event.CMSActionEvent;
import com.chelseasystems.cr.paidout.PaidOutTransaction;
import com.chelseasystems.cr.rules.BusinessRuleException;
import com.chelseasystems.cr.config.FileMgr;
import com.chelseasystems.cr.util.INIFile;
import com.chelseasystems.cs.employee.CMSEmployee;
import com.chelseasystems.cs.customer.CMSCustomer;
import com.chelseasystems.cr.swing.CMSFocusManager;
import com.chelseasystems.cs.paidout.CMSPaidOutTransaction;
import com.chelseasystems.cs.pos.ASISTxnData;
import com.chelseasystems.cs.paidout.CMSMiscPaidOut;
import com.chelseasystems.cs.swing.collections.CollectionsPanelNFSCheckPayment;
import com.chelseasystems.cr.currency.ArmCurrency;
import com.chelseasystems.cr.pos.PaymentTransaction;
import com.chelseasystems.cs.store.CMSStore;
import com.chelseasystems.cs.util.CustomerUtil;


/**
 * @author Ruben Rodriguez
 */
public class PaidoutApplet extends CMSApplet {
  //public class PaidoutApplet extends JApplet implements IPaidOutGUIConst, ActionListener{
  private int paidOutType = -1;
  private CMSCustomer theCustomer = null;
  private PaidOutTransaction currentTxn = null;
  private final int MISC_PAID_OUT = 0;
  private final int CASH_TRANSFER = 1;
  private final int CLOSE_OUT = 2;
  JPanel jBottomPanel = new JPanel();
  PaidOutPanel jDetailPanel = null;
  ReasonModel model = new ReasonModel();
  JCMSTable tblReason = new JCMSTable(model, JCMSTable.SELECT_ROW);

  //Initialize the applet
  public void init() {
    try {
      jbInit();
      initPaidOutTypes(); // load from config file only the first time this app is used
    } catch (Exception e) {
      theAppMgr.showExceptionDlg(e);
      e.printStackTrace();
    }
  }

  //Component initialization
  private void jbInit()
      throws Exception {
    JPanel jMainPanel = new JPanel();
    JPanel jTopPanel = new JPanel();
    Border border1 = new EtchedBorder(EtchedBorder.RAISED, Color.white
        , new java.awt.Color(148, 145, 140));
    TitledBorder titledBorder1 = new TitledBorder(border1
        , res.getString("SELECT REASON FOR PAIDOUT"));
    Border border2 = BorderFactory.createCompoundBorder(titledBorder1
        , BorderFactory.createEmptyBorder(15, 25, 15, 5));
    jMainPanel.setLayout(new BorderLayout());
    jMainPanel.add(jTopPanel, BorderLayout.NORTH);
    jTopPanel.setPreferredSize(new Dimension((int)(555 * r), (int)(300 * r)));
    jTopPanel.setBackground(theAppMgr.getBackgroundColor());
    jTopPanel.setBorder(border2);
    jTopPanel.setLayout(new BorderLayout());
    jTopPanel.add(tblReason, BorderLayout.CENTER);
    jBottomPanel.setBackground(theAppMgr.getBackgroundColor());
    jBottomPanel.setLayout(new BorderLayout());
    jBottomPanel.setBorder(BorderFactory.createTitledBorder(res.getString("PAIDOUT DETAILS")));
    jMainPanel.add(jBottomPanel, BorderLayout.CENTER);
    this.getContentPane().add(jMainPanel, BorderLayout.CENTER);
    tblReason.setAppMgr(theAppMgr);
    tblReason.getColumnModel().getColumn(0).setCellRenderer(new RadioRenderer());
    tblReason.getSelectionModel().addListSelectionListener(new ListSelectionListener() {

      /**
       * put your documentation comment here
       * @param e
       */
      public void valueChanged(ListSelectionEvent e) {
        clickEvent(null);
      }
    });
    tblReason.addComponentListener(new java.awt.event.ComponentAdapter() {

      /**
       * put your documentation comment here
       * @param e
       */
      public void componentResized(ComponentEvent e) {
        tblReason_componentResized(e);
      }
    });
  }

  /**
   * @exception Exception
   */
  private void initPaidOutTypes()
      throws Exception {
    StringTokenizer st;
    INIFile file;
    file = new INIFile(FileMgr.getLocalFile("config", "paidouts.cfg"), true);
    //file = new INIFile(FileMgr.getAbsoluteFile("../files/prod/config/paidouts.cfg"),true);
    String paidOutTypes = file.getValue("PAIDOUT_TYPES");
    Class[] parameterTypes = {this.getClass(), Class.forName("java.lang.String")
    };
    st = new StringTokenizer(paidOutTypes, ",");
    while (st.hasMoreElements()) {
      String key = (String)st.nextElement();
      String paidOutPanelClassName = file.getValue(key, "PANEL_CLASS", "");
      //String paidOutDisplay = file.getValue(key, "DISPLAY", "");
      String paidOutDisplay = res.getString("paidouts.cfg." + key + ".DISPLAY");
      Constructor panelConstructor = Class.forName(paidOutPanelClassName).getConstructor(
          parameterTypes);
      Object[] parameters = {this, paidOutDisplay
      };
      PaidOutPanel paidOutPanel = (PaidOutPanel)panelConstructor.newInstance(parameters);
      model.addRow(paidOutPanel);
    }
  }

  //Start the applet
  public void start() {
    try {
      theAppMgr.setTransitionColor(theAppMgr.getBackgroundColor());
      theAppMgr.setSingleEditArea(res.getString(
          "Select reason for paidout, and enter required information.  Select 'OK' when complete."));
      theOpr = (CMSEmployee)theAppMgr.getStateObject("OPERATOR");
      theAppMgr.showMenu(MenuConst.PAID_IN, theOpr);
      // check to see if txn exist in the case of a "Previous" from the payment applet
      PaymentTransaction paymentTransaction= (PaymentTransaction)theAppMgr.getStateObject("TXN_POS");
      if (paymentTransaction instanceof PaidOutTransaction){
        currentTxn = (PaidOutTransaction)theAppMgr.getStateObject("TXN_POS");
      }
      else if (paymentTransaction != null){
        theAppMgr.addStateObject("PREV_TXN_POS", paymentTransaction);
      }
      if (jDetailPanel instanceof CloseOutPanel) {
        ((CloseOutPanel)jDetailPanel).setBackgroundColorToGrey();
        ((CloseOutPanel)jDetailPanel).setFocusOnAmount();
        if (theAppMgr.getStateObject("ASIS_TXN_DATA") != null
            || theAppMgr.getStateObject("TXN_CUSTOMER") != null) {
          // Don't reset.
        } else
           ((CloseOutPanel)jDetailPanel).reset();
        if (theAppMgr.getStateObject("TXN_CUSTOMER") != null) {
          CMSCustomer cmsCustomer = (CMSCustomer)theAppMgr.getStateObject("TXN_CUSTOMER");
          ((CloseOutPanel)jDetailPanel).setCustId(cmsCustomer.getId());
          ((CloseOutPanel)jDetailPanel).setFamilyName(cmsCustomer.getLastName());
          ((CloseOutPanel)jDetailPanel).setNameCust(cmsCustomer.getFirstName());
          //Merged code with Europe
          CMSCustomer txnCust = cmsCustomer;
          String thisStoreId  = ((CMSStore)theStore).getId();


          try {
                                if (thisStoreId != null && txnCust != null) {
                                        ArmCurrency custCurr = CustomerUtil.getDepositHistoryBalance(CMSApplet.theAppMgr, txnCust.getId(), thisStoreId);
                                        if (custCurr != null) {
                                                txnCust.setCustomerBalance(custCurr);
                                        }

                                }
          } catch (Exception e) {
                                // TODO Auto-generated catch block
                                e.printStackTrace();
          }
          if (cmsCustomer.getCustomerBalance()==null)
            ((CloseOutPanel)jDetailPanel).setPrevBalance(new ArmCurrency(0.00));
          if (cmsCustomer.getCustomerBalance() != null)
            ((CloseOutPanel)jDetailPanel).setPrevBalance(cmsCustomer.getCustomerBalance());
          ((CloseOutPanel)jDetailPanel).setFocusOnAmount();
        }
      }
      String sReasonCode = (String)theAppMgr.getStateObject("REASON_CODE");
      if (sReasonCode == null && currentTxn == null) {
        tblReason.setRowSelectionInterval(0, 0);
      }
      clickEvent(null);
    } catch (Exception e) {
      e.printStackTrace();
      showErrMsg("Error starting applet!\n" + e, true);
      return;
    }
    theAppMgr.removeStateObject("FROM_ASIS");
    theAppMgr.removeStateObject("FROM_ASIS_CANCEL");
    theAppMgr.removeStateObject("CUSTOMER_LOOKUP");
  }

  //Stop the applet
  public void stop() {}

  //Get Applet information
  public String getVersion() {
    return ("$Revision: 1.9.2.3 $");
  }

  /**
   * @return
   */
  public String getScreenName() {
    return (res.getString("Store Paidouts"));
  }

  /**
   * @return
   */
  public IApplicationManager getAppMgr() {
    return (theAppMgr);
  }

  /**
   * This method shows the error message and sets up only <CANCEL> button
   * to exit if 'true' is passed to 'resetButtons'(case of severe errors).
   */
  protected void showErrMsg(String errMsg, boolean resetButtons) {
    theAppMgr.showErrorDlg(errMsg);
    if (resetButtons) { //severe error case
      theAppMgr.goBack();
    }
  }

  /**
   * Tool bar button handler.
   */
  public void appButtonEvent(CMSActionEvent anEvent) {
    try {
      String sAction = anEvent.getActionCommand();
      if (sAction.equals("CANCEL")) {
        theAppMgr.removeStateObject("TXN_POS");
        //theAppMgr.goHome();
      } else if (sAction.equals("OK")) {
        if (!processInputData())
          anEvent.consume();
//      } else if (sAction.equals("OK_COLLECTIONS")) {
//        if (!processInputData())
//          anEvent.consume();
      } else if (sAction.equals("PREV")) {
        theAppMgr.removeStateObject("TXN_POS");
        PaymentTransaction paymentTransaction= (PaymentTransaction)theAppMgr.getStateObject("PREV_TXN_POS");
        if (paymentTransaction!=null){
          theAppMgr.addStateObject("TXN_POS",paymentTransaction);
        }
      } else if (sAction.equals("ADD_ASIS")) {
        theAppMgr.addStateObject("FROM_PAID_OUT", "FROM_PAID_OUT");
      } else if (sAction.equals("CUST_LOOKUP")) {
        theAppMgr.addStateObject("FROM_PAID_OUT_CUST_LOOKUP", "FROM_PAID_OUT_CUST_LOOKUP");
        theAppMgr.addStateObject("PAID_OUT_TXN_PRESENT", "PAID_OUT_TXN_PRESENT");
      }
    } catch (BusinessRuleException e) {
      anEvent.consume();
      theAppMgr.showErrorDlg(res.getString(e.getMessage()));
      CMSFocusManager.requestFocusFieldSupplyingInfo();
    } catch (Exception ex) {
      System.err.println(ex);
      showErrMsg(res.getString(
          "Error initializing the next screen.  Select 'Cancel' to start over."), true);
      return;
    }
  }

  /**
   * This method creates a new Txn and prepares for the next processing stage.
   */
  private boolean processInputData()
      throws BusinessRuleException {
    currentTxn = jDetailPanel.exportData();
    if (currentTxn instanceof CMSMiscPaidOut) {
      //if (((CMSMiscPaidOut)currentTxn).getType().equals("CLOSE_DEPOSIT")) {
        ((CMSMiscPaidOut)currentTxn).setCustomer((CMSCustomer)theAppMgr.getStateObject(
            "TXN_CUSTOMER"));
      //}
    }
    if (currentTxn == null)
      return (false); // error in input data
    if (theAppMgr.getGlobalObject("PROCESS_DATE") != null)
      currentTxn.setProcessDate((Date)theAppMgr.getGlobalObject("PROCESS_DATE"));
    else
      currentTxn.setProcessDate(new Date());
    currentTxn.setTheOperator((CMSEmployee)theAppMgr.getStateObject("OPERATOR"));
    if (theAppMgr.getStateObject("ASIS_TXN_DATA") != null) {
      if (currentTxn instanceof CMSPaidOutTransaction) {
        ((CMSPaidOutTransaction)currentTxn).setASISTxnData((ASISTxnData)theAppMgr.getStateObject(
            "ASIS_TXN_DATA"));
        theAppMgr.removeStateObject("ASIS_TXN_DATA");
      }
    }
    theAppMgr.addStateObject("TXN_POS", currentTxn);
    return (true);
  }

  /**
   * This function updates the content of the bottom panel according to the
   * radio button selection.
   */
  private void updateBottomPanel(final PaidOutPanel newPanel) {
    if (!(jBottomPanel.getComponentCount() > 0 && jBottomPanel.getComponent(0).equals(newPanel))) {
      jBottomPanel.removeAll();
      newPanel.setBackground(theAppMgr.getBackgroundColor());
      jBottomPanel.add(newPanel, BorderLayout.CENTER);
      newPanel.setVisible(true);
      jBottomPanel.validate();
      jDetailPanel = newPanel; //reset reference
      jDetailPanel.repaint();
      SwingUtilities.invokeLater(new Runnable() {

        /**
         * put your documentation comment here
         */
        public void run() {
          newPanel.initialFocus();
        }
      });
    }
    if (jDetailPanel instanceof CloseOutPanel) {
      if (theAppMgr.getStateObject("TXN_CUSTOMER") != null) {
        ((CloseOutPanel)jDetailPanel).setEnableAfter();
        ((CloseOutPanel)jDetailPanel).setBackgroundColorTowhite();
        ((CloseOutPanel)jDetailPanel).setFocusOnAmount();
      } else {
        ((CloseOutPanel)jDetailPanel).setEnabled();
      }
      theAppMgr.setSingleEditArea(res.getString("Select Customer"));
    }
  }

  /**
   */
  public void clickEvent(MouseEvent me) {
    int row = tblReason.getSelectedRow();
    if (row > -1) {
      updateBottomPanel((PaidOutPanel)model.getUnderlyingObject(row));
      populateButtons(row);
      if (jDetailPanel instanceof CloseOutPanel)
        theAppMgr.showMenu(MenuConst.PAID_OUT_CUST, theOpr, theAppMgr.PREV_CANCEL_BUTTON);
      else
        theAppMgr.showMenu(MenuConst.PAID_OUT, theOpr);
    }
  }

  /**
   * callback when <b>Page Down</b> icon is clicked
   */
  public void pageDown(MouseEvent e) {
    model.nextPage();
    theAppMgr.showPageNumber(e, model.getCurrentPageNumber() + 1, model.getPageCount());
  }

  /**
   * callback when <b>Page Up</b> icon is clicked
   */
  public void pageUp(MouseEvent e) {
    model.prevPage();
    theAppMgr.showPageNumber(e, model.getCurrentPageNumber() + 1, model.getPageCount());
  }

  /**
   */
  public void tblReason_componentResized(ComponentEvent e) {
    model.setRowsShown(tblReason.getHeight() / tblReason.getRowHeight());
    tblReason.getColumnModel().getColumn(1).setPreferredWidth((int)(750 * r));
    tblReason.repaint();
  }

  /********************************************************************/
  private class RadioRenderer extends JRadioButton implements TableCellRenderer {

    /**
     */
    public RadioRenderer() {
      this.setOpaque(false);
      this.setHorizontalAlignment(SwingConstants.CENTER);
    }

    /**
     * @param table
     * @param value
     * @param isSelected
     * @param hasFocus
     * @param row
     * @param column
     * @return
     */
    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected
        , boolean hasFocus, int row, int column) {
      this.setEnabled(table.isEnabled());
      this.setSelected(isSelected);
      return (this);
    }
  }


  private class ReasonModel extends ScrollableTableModel {

    /**
     */
    public ReasonModel() {
      super(new String[] {" ", res.getString("Select Reason for PaidOut")
      });
    }

    /**
     * @param row
     * @return
     */
    public Object getUnderlyingObject(int row) {
      Vector vPage = getCurrentPage();
      return (vPage.elementAt(row));
    }

    /**
     * @param row
     * @param column
     * @return
     */
    public Object getValueAt(int row, int column) {
      Vector vPage = getCurrentPage();
      if (vPage.size() == 0)
        return (null);
      if (column == 0)
        return (new Boolean(((JRadioButton)tblReason.getCellRenderer(row, column)).isSelected()));
      else
        return ((PaidOutPanel)vPage.elementAt(row)).getDisplayName();
    }
  }


  /**
   * put your documentation comment here
   * @param menu
   */
  public void populateButtons(int menu) {
    switch (menu) {
      case MISC_PAID_OUT: {
        theAppMgr.addStateObject("REASON_CODE", "MISC_PAID_OUT");
        if (theAppMgr.getStateObject("TXN_CUSTOMER") == null
            && theAppMgr.getStateObject("ASIS_TXN_DATA") == null) {
          ((PaidOutPanelCMSMiscPaidOut)jDetailPanel).clearFields();
        }
        break;
      }
      case CASH_TRANSFER: {
        theAppMgr.addStateObject("REASON_CODE", "CASH_TRANSFER");
        if (theAppMgr.getStateObject("TXN_CUSTOMER") == null
            && theAppMgr.getStateObject("ASIS_TXN_DATA") == null) {
          ((PaidOutPanelCMSCashDrop)jDetailPanel).clearFields();
        }
        break;
      }
      case CLOSE_OUT: {
        theAppMgr.addStateObject("REASON_CODE", "CLOSE_DEPOSIT");
        if (theAppMgr.getStateObject("TXN_CUSTOMER") == null
            && theAppMgr.getStateObject("ASIS_TXN_DATA") == null) {
          ((CloseOutPanel)jDetailPanel).clearFields();
        }
        break;
      }
      default:
        break;
    }
  }
}

