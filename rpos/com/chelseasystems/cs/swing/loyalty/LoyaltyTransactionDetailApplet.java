/*
 * put your module comment here
 * formatted with JxBeauty (c) johann.langhofer@nextra.at
 */


package com.chelseasystems.cs.swing.loyalty;

import com.chelseasystems.cr.appmgr.*;
import com.chelseasystems.cr.swing.*;
import com.chelseasystems.cr.swing.bean.*;
import com.chelseasystems.cs.swing.menu.MenuConst;
import java.awt.event.*;
import com.chelseasystems.cr.swing.event.CMSActionEvent;
import javax.swing.*;
import java.awt.*;
import com.chelseasystems.cs.loyalty.*;
import java.util.Date;
import com.chelseasystems.cs.customer.CMSCustomer;
import com.chelseasystems.cs.employee.CMSEmployee;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import com.chelseasystems.cs.pos.CMSCompositePOSTransaction;
import com.chelseasystems.cr.pos.PaymentTransaction;
import com.chelseasystems.cs.pos.CMSTransactionPOSHelper;
import com.ga.fs.fsbridge.ARMFSBridge;


/**
 * <p>Title:ItemLookupListApplet </p>
 * <p>Description: Searches items on provided search criteria</p>
 * <p>Copyright: Copyright (c) 2005</p>
 * <p>Company: SkillnetInc</p>
 * @author Megha
 * @version 1.0
 */
public class LoyaltyTransactionDetailApplet extends CMSApplet {
  private LoyaltyTransactionDetailPanel pnlLoyaltyTransactionDetail;
  private JPanel pnlHeader;
  private JCMSLabel lblCustomerName;
  private JCMSLabel lblLifeTimeBal;
  private JCMSLabel lblLoyaltyNo;
  private JCMSLabel lblIssueDate;
  private JCMSLabel lblCurrentBal;
  private JCMSLabel lblLoyaltyType;
  private JCMSLabel lblDateRange;
  private JCMSTextField txtCustomerName;
  private JCMSTextField txtLifeTimeBal;
  private JCMSTextField txtLoyaltyNo;
  private JCMSTextField txtIssueDate;
  private JCMSTextField txtCurrentBal;
  private JCMSTextField txtLoyaltyType;
  private JCMSTextField txtDateRange;
  //  private ScrollableToolBarPanel screen;
  private String start_date = "";
  private String end_date = "";
  //private LoyaltyHistory loyaltyHistory;
  IRepositoryManager theMgr;
  double dResolution = com.chelseasystems.cr.swing.CMSApplet.r;
  Loyalty loyaltyCard;
  LoyaltyHistory[] loadCardHistory;

  /**
   * put your documentation comment here
   */
  public void stop() {
    // reset();
  }

  /**
   * put your documentation comment here
   */
  public void init() {
    try {
      jbInit();
      pnlLoyaltyTransactionDetail.addMouseListener(new MouseAdapter() {
        public void mouseClicked(MouseEvent e) {
          if (e.getClickCount() != 2)
            return;
          clickEvent(e);
        }
      });
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  /**
   * put your documentation comment here
   */
  public void start() {
    //loyaltyHistory = null;
    reset();
    theAppMgr.showMenu(MenuConst.LOYALTY_DETAILS, theOpr);
    Date currDate = new Date();
    /**
     * Loading the Txn History for a given Loyalty Card
     */
    loyaltyCard = (Loyalty)theAppMgr.getStateObject("LOYALTY");
    CMSCustomer cmsCust = new CMSCustomer();
    cmsCust = (CMSCustomer)theAppMgr.getStateObject("CUSTOMER_LOOKUP");
    try {
      SimpleDateFormat df = com.chelseasystems.cs.util.DateFormatUtil.getLocalDateFormat();
      Calendar startDate = Calendar.getInstance();
      startDate.setTime(df.parse(df.format(currDate))); //parse and format required to set time 00:00
      // Set the date 50 years back to get all the history as requested in issue#880
      startDate.add(Calendar.YEAR, -50);
      String selectedTransactionID = ((String)theAppMgr.getStateObject("TXN_HIST_SELECTEDTXNID"));
      theAppMgr.removeStateObject("TXN_HIST_SELECTEDTXNID");
      if (theAppMgr.getStateObject("LOYALTY_HIST_LIST") != null) {
        loadCardHistory = (LoyaltyHistory[])theAppMgr.getStateObject("LOYALTY_HIST_LIST");
        theAppMgr.removeStateObject("LOYALTY_HIST_LIST");
      } else {
        loadCardHistory = LoyaltyHelper.getLoyaltyHistory(theAppMgr, loyaltyCard.getLoyaltyNumber()
            , startDate.getTime(), currDate);
      }
      loadBySearchedLoyaltyHistory(new Integer(Integer.MIN_VALUE), selectedTransactionID);
      /**
       * Setting the panel fields
       */
      String datePrev = "";
      String dateCurrent = "";
      Date issueDate = new Date();
      Date currentDate = new Date();
      try {
        df.setLenient(false);
        datePrev = df.format(startDate.getTime());
        issueDate = loyaltyCard.getIssueDate();
        dateCurrent = df.format(currentDate);
        currentDate = df.parse(dateCurrent);
      } catch (Exception e) {
        e.printStackTrace();
      }
      txtCustomerName.setText(cmsCust.getFirstName() + " " + cmsCust.getLastName());
      txtLoyaltyNo.setText(loyaltyCard.getLoyaltyNumber());
      txtLoyaltyType.setText(loyaltyCard.getStoreType());
      //issueDate.getF
      txtIssueDate.setText(df.format(issueDate));
      txtLifeTimeBal.setText((new Float(loyaltyCard.getLifeTimeBalance())).toString());
      txtCurrentBal.setText((new Float(loyaltyCard.getCurrBalance())).toString());
      if (theAppMgr.getStateObject("DATE_STRING") != null) {
        txtDateRange.setText(theAppMgr.getStateObject("DATE_STRING").toString());
        theAppMgr.removeStateObject("DATE_STRING");
      } else
        txtDateRange.setText(datePrev + " - " + dateCurrent);
      for (int i = 0; i < loadCardHistory.length; i++) {
        //loyaltyHistory = pnlLoyaltyTransactionDetail.getItemAt(i);
        SwingUtilities.invokeLater(new Runnable() {
          public void run() {
            pnlLoyaltyTransactionDetail.update();
          }
        });
      }
      enter();
      this.addComponentListener(new java.awt.event.ComponentAdapter() {
        public void componentResized(ComponentEvent e) {
          //resize();
        }
      });
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  /**
   * put your documentation comment here
   */
  private void reset() {
    pnlLoyaltyTransactionDetail.clear();
    txtCustomerName.setText("");
    txtCustomerName.setText("");
    txtLifeTimeBal.setText("");
    txtLoyaltyNo.setText("");
    txtIssueDate.setText("");
    txtCurrentBal.setText("");
    txtLoyaltyType.setText("");
    txtDateRange.setText("");
    loadCardHistory = null;
  }

  /**
   * For setting the default edit area
   */
  private void enter() {
    theAppMgr.setSingleEditArea(res.getString("Select a transaction to view details"));
  }

  /**
   * For Handling EditArea Events
   * @param sCommand String
   * @param sInput String
   */
  public void editAreaEvent(String sCommand, String sInput) {
    LoyaltyHistory loyaltyHistory[];
    if (sCommand.equals("START_DATE")) {
      theAppMgr.unRegisterSingleEditArea();
      if (sInput == null || sInput.trim().length() < 1) {
        theAppMgr.showErrorDlg(res.getString("Enter the Start Date"));
        return;
      } else {
        start_date = sInput;
        theAppMgr.setSingleEditArea(res.getString("End Date"), "END_DATE", theAppMgr.REQUIRED_MASK);
        theAppMgr.setEditAreaFocus();
      }
    }
    if (sCommand.equals("END_DATE")) {
      theAppMgr.unRegisterSingleEditArea();
      if (sInput == null || sInput.trim().length() < 1) {
        theAppMgr.showErrorDlg(res.getString("Enter the End Date"));
        return;
      } else {
        end_date = sInput;
        theAppMgr.setSingleEditArea(res.getString("Select a transaction to view details"));
        /**
         * Call the Helper Class
         */
        loyaltyHistory = null;
        // pnlLoyaltyTransactionDetail.clear();
        /**
         * Loading the Txn History for a given Loyalty Card
         */
        Date startDate = new Date();
        Date endDate = new Date();
        try {
          SimpleDateFormat df = com.chelseasystems.cs.util.DateFormatUtil.getLocalDateFormat();
          df.setLenient(false);
          startDate = (Date)df.parse(start_date.trim());
          endDate = (Date)df.parse(end_date.trim());
          loyaltyCard = (Loyalty)theAppMgr.getStateObject("LOYALTY");
          try {
            loadCardHistory = LoyaltyHelper.getLoyaltyHistory(theAppMgr
                , loyaltyCard.getLoyaltyNumber(), startDate, endDate);
            if (loadCardHistory == null) {
              System.out.println("******No history");
            }
          } catch (Exception e) {
            e.printStackTrace();
          }
          pnlLoyaltyTransactionDetail.clear();
          loadBySearchedLoyaltyHistory(new Integer(Integer.MIN_VALUE), null);
          txtDateRange.setText(start_date + "-" + end_date);
          for (int i = 0; i < loadCardHistory.length; i++) {
        	  
        	// remove the loyalty with 0 points
        	 if(loadCardHistory[i]!=null && loadCardHistory[i].getPointEarned()==0.0){
        		 continue;
        	 }
        	  
        	  
            pnlLoyaltyTransactionDetail.getItemAt(i);
            SwingUtilities.invokeLater(new Runnable() {

              /**
               */
              public void run() {
                pnlLoyaltyTransactionDetail.update();
              }
            });
          }
        } catch (Exception e) {
          e.printStackTrace();
        }
      }
    }
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
   * For Handling Button Events
   * @param anEvent CMSActionEvent
   */
  public void appButtonEvent(CMSActionEvent anEvent) {
    String sAction = anEvent.getActionCommand();
    boolean additionalFieldFlag = false;
    String input_date = null;
    String sQuery = "";
    if (sAction.equals("DATE")) {
      String DATE_START = null;
      theAppMgr.setSingleEditArea(res.getString("Start Date"), "START_DATE", input_date
          , theAppMgr.REQUIRED_MASK);
      theAppMgr.setEditAreaFocus();
    } else if (sAction.equals("PREV")) {
      theAppMgr.addStateObject("PREV_DETAILS", "DETAILS");
      theAppMgr.removeStateObject("LOYALTY");
      theAppMgr.showMenu(MenuConst.LOYALTY, theOpr);
      //theAppMgr.goBack();
    } else if (sAction.equals("CANCEL")) {
      theAppMgr.showMenu(MenuConst.LOYALTY, theOpr);
      theAppMgr.goBack();
    }
  }

  /**
   * put your documentation comment here
   * @param loyaltyHistory[]
   * @return
   */
  private int loadLoyaltyItemsHistory(LoyaltyHistory loyaltyHistory[]) {
    pnlLoyaltyTransactionDetail.clear();
    pnlLoyaltyTransactionDetail.setItems(loyaltyHistory);
    return loyaltyHistory.length;
  }

  /**
   * put your documentation comment here
   * @return
   */
  public String getVersion() {
    return ("$Revision: 1.1 $");
  }

  /**
   * put your documentation comment here
   * @return
   */
  public String getScreenName() {
    return "Loyalty Details";
  }

  /**
   * Initializes the parameters
   * @throws Exception
   */
  private void jbInit()
      throws Exception {
    pnlLoyaltyTransactionDetail = new LoyaltyTransactionDetailPanel(theAppMgr);
    pnlHeader = new JPanel();
    lblCustomerName = new JCMSLabel(); 
    lblLifeTimeBal = new JCMSLabel(); 
    lblLoyaltyNo = new JCMSLabel(); 
    lblIssueDate = new JCMSLabel(); 
    lblCurrentBal = new JCMSLabel();
    lblLoyaltyType = new JCMSLabel();
    lblDateRange = new JCMSLabel();
    txtCustomerName = new JCMSTextField();
    txtLifeTimeBal = new JCMSTextField();
    txtLoyaltyNo = new JCMSTextField();
    txtIssueDate = new JCMSTextField();
    txtCurrentBal = new JCMSTextField();
    txtLoyaltyType = new JCMSTextField();
    txtDateRange = new JCMSTextField();
    this.getContentPane().setLayout(new BorderLayout());
    this.getContentPane().add(pnlHeader, BorderLayout.NORTH);
    this.getContentPane().add(pnlLoyaltyTransactionDetail, BorderLayout.SOUTH);
    this.setLayout(new BorderLayout());
    //  pnlHeader.setPreferredSize(new Dimension(833, 130));
    pnlHeader.setLayout(new GridBagLayout());
    lblCustomerName.setLabelFor(txtCustomerName);
    lblCustomerName.setText(res.getString("Customer Name"));
    txtCustomerName.setText("");
    txtCustomerName.setEnabled(false);
    lblLifeTimeBal.setLabelFor(txtLifeTimeBal);
    lblLifeTimeBal.setText(res.getString("Lifetime Bal."));
    txtLifeTimeBal.setText("");
    txtLifeTimeBal.setEnabled(false);
    lblLoyaltyNo.setLabelFor(txtLoyaltyNo);
    lblLoyaltyNo.setText(res.getString("Loyalty No"));
    txtLoyaltyNo.setText("");
    txtLoyaltyNo.setEnabled(false);
    lblIssueDate.setLabelFor(txtIssueDate);
    lblIssueDate.setText(res.getString("Issue Date"));
    txtIssueDate.setText("");
    txtIssueDate.setEnabled(false);
    lblCurrentBal.setLabelFor(txtCurrentBal);
    lblCurrentBal.setText(res.getString("Current Bal."));
    txtCurrentBal.setText("");
    txtCurrentBal.setEnabled(false);
    lblLoyaltyType.setLabelFor(txtLoyaltyType);
    lblLoyaltyType.setText(res.getString("Loyalty Type."));
    txtLoyaltyType.setText("");
    txtLoyaltyType.setEnabled(false);
    lblDateRange.setLabelFor(txtDateRange);
    lblDateRange.setText(res.getString("Date Range"));
    txtDateRange.setText("");
    txtDateRange.setEnabled(false);
    pnlHeader.setPreferredSize(new Dimension((int)(800 * dResolution), (int)(135 * dResolution)));
    pnlLoyaltyTransactionDetail.setPreferredSize(new Dimension((int)(400 * dResolution)
        , (int)(465 * dResolution)));
    this.add(pnlHeader, BorderLayout.NORTH);
    this.add(pnlLoyaltyTransactionDetail, BorderLayout.SOUTH);
    //  this.setBackground(theAppMgr.getBackgroundColor());
    pnlHeader.setBackground(theAppMgr.getBackgroundColor());
    pnlLoyaltyTransactionDetail.setAppMgr(theAppMgr);
    lblCustomerName.setAppMgr(theAppMgr);
    lblCustomerName.setFont(theAppMgr.getTheme().getLabelFont());
    txtCustomerName.setAppMgr(theAppMgr);
    txtCustomerName.setFont(theAppMgr.getTheme().getTextFieldFont());
    lblLifeTimeBal.setAppMgr(theAppMgr);
    lblLifeTimeBal.setFont(theAppMgr.getTheme().getLabelFont());
    txtLifeTimeBal.setAppMgr(theAppMgr);
    txtLifeTimeBal.setFont(theAppMgr.getTheme().getTextFieldFont());
    lblLoyaltyNo.setAppMgr(theAppMgr);
    lblLoyaltyNo.setFont(theAppMgr.getTheme().getLabelFont());
    txtLoyaltyNo.setAppMgr(theAppMgr);
    txtLoyaltyNo.setFont(theAppMgr.getTheme().getTextFieldFont());
    lblIssueDate.setAppMgr(theAppMgr);
    lblIssueDate.setFont(theAppMgr.getTheme().getLabelFont());
    txtIssueDate.setAppMgr(theAppMgr);
    txtIssueDate.setFont(theAppMgr.getTheme().getTextFieldFont());
    lblCurrentBal.setAppMgr(theAppMgr);
    lblCurrentBal.setFont(theAppMgr.getTheme().getLabelFont());
    txtCurrentBal.setAppMgr(theAppMgr);
    txtCurrentBal.setFont(theAppMgr.getTheme().getTextFieldFont());
    lblLoyaltyType.setAppMgr(theAppMgr);
    lblLoyaltyType.setFont(theAppMgr.getTheme().getLabelFont());
    txtLoyaltyType.setAppMgr(theAppMgr);
    txtLoyaltyType.setFont(theAppMgr.getTheme().getTextFieldFont());
    lblDateRange.setAppMgr(theAppMgr);
    lblDateRange.setFont(theAppMgr.getTheme().getLabelFont());
    txtDateRange.setAppMgr(theAppMgr);
    txtDateRange.setFont(theAppMgr.getTheme().getTextFieldFont());
    pnlHeader.add(lblDateRange
        , new GridBagConstraints(5, 2, 1, 1, 0.0, 0.0, GridBagConstraints.WEST
        , GridBagConstraints.NONE, new Insets(3, -10, 1, 30), 4, 0));
    pnlHeader.add(txtDateRange
        , new GridBagConstraints(7, 2, 5, 1, 1.0, 0.0, GridBagConstraints.WEST
        , GridBagConstraints.NONE, new Insets(3, -7, 1, 70), 360, 4));
    pnlHeader.add(lblIssueDate
        , new GridBagConstraints(4, 1, 2, 1, 0.0, 0.0, GridBagConstraints.WEST
        , GridBagConstraints.NONE, new Insets(3, -8, 1, 0), 4, 0));
    pnlHeader.add(txtIssueDate
        , new GridBagConstraints(7, 1, 1, 1, 1.0, 0.0, GridBagConstraints.WEST
        , GridBagConstraints.NONE, new Insets(3, -9, 1, 0), 80, 4));
    pnlHeader.add(lblCurrentBal
        , new GridBagConstraints(8, 1, 2, 1, 0.0, 0.0, GridBagConstraints.WEST
        , GridBagConstraints.NONE, new Insets(3, -38, 1, 38), 4, 0));
    pnlHeader.add(txtLifeTimeBal
        , new GridBagConstraints(9, 0, 4, 1, 1.0, 0.0, GridBagConstraints.WEST
        , GridBagConstraints.NONE, new Insets(4, 55, 0, 80), 95, 4));
    pnlHeader.add(lblCustomerName
        , new GridBagConstraints(0, 0, 3, 1, 0.0, 0.0, GridBagConstraints.WEST
        , GridBagConstraints.NONE, new Insets(0, 1, 0, 15), 4, 4));
    pnlHeader.add(lblLoyaltyNo
        , new GridBagConstraints(0, 1, 1, 1, 0.0, 0.0, GridBagConstraints.SOUTHWEST
        , GridBagConstraints.NONE, new Insets(0, -1, 4, 13), 4, 0));
    pnlHeader.add(lblLoyaltyType
        , new GridBagConstraints(0, 2, 2, 1, 0.0, 0.0, GridBagConstraints.SOUTHWEST
        , GridBagConstraints.NONE, new Insets(4, -1, 0, 13), 4, 0));
    pnlHeader.add(txtCustomerName
        , new GridBagConstraints(3, 0, 4, 1, 1.0, 0.0, GridBagConstraints.WEST
        , GridBagConstraints.NONE, new Insets( -9, -12, 0, 35), 230, 4));
    pnlHeader.add(txtCurrentBal
        , new GridBagConstraints(10, 1, 1, 1, 1.0, 0.0, GridBagConstraints.WEST
        , GridBagConstraints.NONE, new Insets(4, -15, 0, 44), 95, 4));
    pnlHeader.add(txtLoyaltyType
        , new GridBagConstraints(2, 2, 3, 3, 1.0, 0.0, GridBagConstraints.WEST
        , GridBagConstraints.NONE, new Insets(4, 0, 0, 0), 113, 5));
    pnlHeader.add(txtLoyaltyNo
        , new GridBagConstraints(2, 1, 2, 1, 1.0, 0.0, GridBagConstraints.WEST
        , GridBagConstraints.NONE, new Insets(4, 0, 0, 0), 113, 5));
    pnlHeader.add(lblLifeTimeBal
        , new GridBagConstraints(7, 0, 2, 1, 0.0, 0.0, GridBagConstraints.WEST
        , GridBagConstraints.NONE, new Insets(3, 80, 1, -20), 4, 0));
  }

  /**
   * put your documentation comment here
   */
  public void resize() {
    double r = com.chelseasystems.cr.swing.CMSApplet.r;
    lblCustomerName.setPreferredSize(new Dimension((int)(55 * r), (int)(30 * r)));
    txtCustomerName.setPreferredSize(new Dimension((int)(75 * r), (int)(30 * r)));
    lblLifeTimeBal.setPreferredSize(new Dimension((int)(55 * r), (int)(30 * r)));
    txtLifeTimeBal.setPreferredSize(new Dimension((int)(75 * r), (int)(30 * r)));
    lblLoyaltyNo.setPreferredSize(new Dimension((int)(55 * r), (int)(30 * r)));
    txtLoyaltyNo.setPreferredSize(new Dimension((int)(75 * r), (int)(30 * r)));
    lblIssueDate.setPreferredSize(new Dimension((int)(55 * r), (int)(30 * r)));
    txtIssueDate.setPreferredSize(new Dimension((int)(75 * r), (int)(30 * r)));
    lblCurrentBal.setPreferredSize(new Dimension((int)(55 * r), (int)(30 * r)));
    txtCurrentBal.setPreferredSize(new Dimension((int)(75 * r), (int)(30 * r)));
    lblLoyaltyType.setPreferredSize(new Dimension((int)(55 * r), (int)(30 * r)));
    txtLoyaltyType.setPreferredSize(new Dimension((int)(75 * r), (int)(30 * r)));
    lblDateRange.setPreferredSize(new Dimension((int)(55 * r), (int)(30 * r)));
    txtDateRange.setPreferredSize(new Dimension((int)(75 * r), (int)(30 * r)));
  }

  /**
   * put your documentation comment here
   * @param e
   */
  public void pageDown(MouseEvent e) {
    //    if ( pnlLoyaltyTransactionDetail.getSelectedItem() != null) {
    pnlLoyaltyTransactionDetail.nextPage();
    theAppMgr.showPageNumber(e, pnlLoyaltyTransactionDetail.getCurrentPageNumber() + 1
        , pnlLoyaltyTransactionDetail.getTotalPages());
    // }
  }

  /**
   * put your documentation comment here
   * @param e
   */
  public void pageUp(MouseEvent e) {
    //if (pnlLoyaltyTransactionDetail.getSelectedItem() != null) {
    pnlLoyaltyTransactionDetail.prevPage();
    theAppMgr.showPageNumber(e, pnlLoyaltyTransactionDetail.getCurrentPageNumber() + 1
        , pnlLoyaltyTransactionDetail.getTotalPages());
    // }
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
  public void setEditArea(String sMessage, String sCommand, int iMask) {
    if (sCommand != null) {
      theAppMgr.setSingleEditArea(sMessage, sCommand, iMask);
    } else {
      theAppMgr.setSingleEditArea(sMessage);
    }
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
   * Handles click event
   * @param me MouseEvent
   */
  public void clickEvent(MouseEvent me) {
    LoyaltyHistory loyaltyHist = pnlLoyaltyTransactionDetail.getAddressModel().getItemAt(
        pnlLoyaltyTransactionDetail.getTable().getSelectedRow());
    if (loyaltyHist != null)
      try {
        System.out.println("**** txn Id = " + loyaltyHist.getTransactionId());
        PaymentTransaction theTxn = (PaymentTransaction)CMSTransactionPOSHelper.findById(theAppMgr
            , loyaltyHist.getTransactionId());
        if (theTxn != null) {
          theAppMgr.addStateObject("THE_TXN", theTxn);
          theAppMgr.addStateObject("TXN_HIST_SELECTEDTXNID", theTxn.getId());
          theAppMgr.addStateObject("LOYALTY_HIST_LIST"
              , pnlLoyaltyTransactionDetail.getAddressModel().getLoyaltyHistoryArray());
          theAppMgr.addStateObject("TXN_HEADER_ROW"
              , new Integer(pnlLoyaltyTransactionDetail.getSelectedRowIndex()));
          //theAppMgr.addStateObject("TXN_HIST_SORTCOLUMN", theTxn);
          theAppMgr.removeStateObject("ARM_TXN_HEADERS");
          theAppMgr.addStateObject("DATE_STRING", txtDateRange.getText());
          theAppMgr.fireButtonEvent("OK");
        } else {
          theAppMgr.showErrorDlg(res.getString("Cannot find transaction."));
        }
      } catch (Exception ex) {
        theAppMgr.showExceptionDlg(ex);
      }
  }

  /**
   * put your documentation comment here
   * @param sortColumn
   * @param selectedTransactionID
   */
  private void loadBySearchedLoyaltyHistory(Integer sortColumn, String selectedTransactionID) {
    //pnlLoyaltyTransactionDetail.clear();
    if (loadCardHistory == null)
      return;
    pnlLoyaltyTransactionDetail.setItems(loadCardHistory);
    int sortColumnIntValue = Integer.MAX_VALUE;
    if (sortColumn != null)
      sortColumnIntValue = sortColumn.intValue();
    int selectedItemRowNew = pnlLoyaltyTransactionDetail.getAddressModel().sortByColumnType(
        sortColumnIntValue, selectedTransactionID);
    if (selectedItemRowNew > -1)
      pnlLoyaltyTransactionDetail.selectRow(selectedItemRowNew);
  }
}

