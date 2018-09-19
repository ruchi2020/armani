/*
 * put your module comment here
 * formatted with JxBeauty (c) johann.langhofer@nextra.at
 */


 package com.chelseasystems.cs.swing.customer;

import java.util.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

import com.chelseasystems.cr.swing.*;
import com.chelseasystems.cr.swing.bean.*;
import com.chelseasystems.cr.swing.event.*;
import com.chelseasystems.cr.swing.layout.*;
import com.chelseasystems.cs.customer.*;
import com.chelseasystems.cs.employee.*;
import com.chelseasystems.cr.pos.*;
import com.chelseasystems.cs.store.*;
import com.chelseasystems.cs.swing.menu.*;
import com.chelseasystems.cs.swing.panel.*;
import com.chelseasystems.cs.util.CustomerUtil;
import com.chelseasystems.cs.util.Version;
import com.chelseasystems.cr.rules.BusinessRuleException;
import com.chelseasystems.cs.pos.CMSCompositePOSTransaction;
import com.chelseasystems.cr.config.ConfigMgr;
import com.chelseasystems.cr.swing.NonFocusableButton;
//import com.armani.crm.crmCustomer;
import com.chelseasystems.cs.address.Address;
import com.chelseasystems.cs.util.DateFormatUtil;
/*
History:
+------+------------+-----------+-----------+----------------------------------------------------+
| Ver# | Date       | By        | Defect #  | Description                                        |
+------+------------+-----------+-----------+----------------------------------------------------+
| 1    | 18-11-2011 | Deepika   | Bug#7120  |  Modified the flow to restrict DefaultCustomer     |
|      |            |           |           |  creation from POS.                                |
+------+------------+-----------+-----------+----------------------------------------------------+  
*/
/**
 * <p>Title: CustomerDefaultSearchApplet</p>
 *
 * <p>Description: Search Customer Details </p>
 *
 * <p>Copyright: Copyright (c) 2005</p>
 *
 * <p>Company: </p>
 *
 * @author Paresh
 * @version 1.0
 */

public class CustomerDefaultSearchApplet extends CMSApplet {
  private JPanel viewPortPanel;
  private JPanel customerBasicPanel;
  private AddressPanel pnlAddress;
  private CardDetailsPanel pnlCardDetails;
  private CustomerMiscSearchInfoPanel pnlMiscSearch;
  private CustomerBasicPanel pnlCustBasic;
  private ViewAddressPanel pnlViewAddress;
  private ViewCreditCardPanel pnlViewCreditCard;
  private ViewCommentsPanel pnlViewComments;
  private ViewPurchaseHistoryPanel pnlViewPurchHistory;
  private RolodexLayout cardLayout;
  private PaymentTransaction theTxn;
  private JPanel pnlLastVisited;
  private CMSCustomer cmsCustomer;
//  private CustomerCreditCard creditCard;
//  private boolean bShowPrevApplet = false;
//  private boolean bUpdateMode = false;
//  private boolean bViewsEnabled = false;
//  private boolean bNewCustBasicDone = false;
//  private String sAssignedAssociate = "";
  double dResolution = com.chelseasystems.cr.swing.CMSApplet.r;
  private CMSStore cmsStore;
//  private CMSCustomer customerInitial;
  GridBagLayout gridBagLayout1 = new GridBagLayout();
  private boolean bConsumeEvent = false;
  private Component compTabEvent;
  private CustomerMgmtEvtListener mgmtEvtListener;
  private AddressTypeListener addTypeListener;
  private static final String CONFIGURATION_FILE = "customer.cfg";
  private static final ConfigMgr configMgr = new ConfigMgr(CONFIGURATION_FILE);
  private String custBasicPanelClassName =
      configMgr.getString("CUSTOMER.BASIC_PANEL");
//  private boolean updateAllStgTbl=false;

  /**
   * Get ScreeName
   * @return ScreenName
   */
  public String getScreenName() {
    return "Customer Search";
  }


  /**
   * getVersioni
   * @return Version
   */
  public String getVersion() {
    return ("$Revision: 1.1 $");
  }


  /**
   * init
   */
  public void init() {
    try {
//      cmsCustomer = new CMSCustomer();
//      creditCard = new CustomerCreditCard();
      jbInit();
    } catch (Exception ex) {
      ex.printStackTrace();
    }
  }


  /**
   * start
   */
  public void start() {
    reset();
    theAppMgr.showMenu(MenuConst.CUSTOMER_DEFAULT_SEARCH, theOpr);

    setEnabled(false);
    
    pnlMiscSearch.cmbxAge.setEnabled(true);
    pnlMiscSearch.cmboxGender.setEnabled(true);
    pnlAddress.cbCountry.setEnabled(false);
    pnlAddress.pnlBaseAddress.getTxtCity().setEnabled(true);
    pnlAddress.pnlBaseAddress.getTxtZipCode().setEnabled(true);
    Component[] comp = pnlAddress.pnlBottom.getComponents();
    for (int i = 0; i < comp.length; i++) {
      if (comp[i] instanceof EURAddressPanel) {
        ((EURAddressPanel)comp[i]).enableDefaultSearch();
        break;
      }
    }
  }

  /**
   * Initialize all view panels.
   */
  private void initViews() {
    try {
      pnlViewComments = new ViewCommentsPanel();
      pnlViewCreditCard = new ViewCreditCardPanel();
      pnlViewAddress = new ViewAddressPanel(pnlAddress);
      pnlAddress.setPrimary(true);
      pnlViewAddress.setCustomer(cmsCustomer);
//      addTypeListener = new AddressTypeListener();
//      pnlAddress.addItemListener(addTypeListener);
      pnlCardDetails = new CardDetailsPanel();
      pnlViewPurchHistory = new ViewPurchaseHistoryPanel();
      pnlCustBasic.setAppMgr(theAppMgr);
      pnlViewComments.setAppMgr(theAppMgr);
      pnlViewCreditCard.setAppMgr(theAppMgr);
      pnlViewAddress.setAppMgr(theAppMgr);
      pnlCardDetails.setAppMgr(theAppMgr);
      pnlViewComments.setAppMgr(theAppMgr);
      pnlViewPurchHistory.setAppMgr(theAppMgr);
      viewPortPanel.add(pnlViewComments, pnlViewComments);
      viewPortPanel.add(pnlViewCreditCard, pnlViewCreditCard);
      viewPortPanel.add(pnlViewAddress, pnlViewAddress);
      viewPortPanel.add(pnlCardDetails, pnlCardDetails);
      viewPortPanel.add(pnlViewPurchHistory, pnlViewPurchHistory);
    } catch (Exception e) {
      e.printStackTrace();
    }
//    bViewsEnabled = true;
  }

  /**
   * stop
   */
  public void stop() {
    removeAWTListeners();
  }


  /**
   * Intialize and Layout components
   * @throws Exception
   */
  private void jbInit()
      throws Exception {

    viewPortPanel = new JPanel();
    customerBasicPanel = new JPanel();
    pnlAddress = new AddressPanel();
    pnlMiscSearch = new CustomerMiscSearchInfoPanel();
    //PCR1325 Customer Details fix for Armani Japan
    if (custBasicPanelClassName == null) {
      pnlCustBasic = new CustomerBasicPanel();
    } else {
      pnlCustBasic = (CustomerBasicPanel)Class.forName(custBasicPanelClassName).newInstance();
    }
    cardLayout = new RolodexLayout();
    viewPortPanel.setLayout(cardLayout);
    pnlCustBasic.setPreferredSize(new Dimension((int)(800 * dResolution), (int)(135 * dResolution)));
    pnlAddress.setPreferredSize(new Dimension((int)(350 * dResolution), (int)(400 * dResolution)));
//    pnlMiscSearch.setPreferredSize(new Dimension((int)(250 * dResolution), (int)(400 * dResolution)));
    pnlAddress.setAppMgr(theAppMgr);
    pnlCustBasic.setAppMgr(theAppMgr);
    pnlMiscSearch.setAppMgr(theAppMgr);
    JPanel lowerPnl = new JPanel();
    JPanel outerPnl = new JPanel();
    outerPnl.setFont(theAppMgr.getTheme().getTextFieldFont());
    outerPnl.setBackground(theAppMgr.getBackgroundColor());
    lowerPnl.setFont(theAppMgr.getTheme().getTextFieldFont());
    lowerPnl.setBackground(theAppMgr.getBackgroundColor());
    outerPnl.setLayout(new GridBagLayout());
    outerPnl.add(pnlMiscSearch, new GridBagConstraints(0, 0, 1, 1, 1.0, 0.0
        , GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(5, 5, 5, 5), 0, 0));
    outerPnl.add(lowerPnl, new GridBagConstraints(0, 1, 1, 1, 1.0, 1.0
        , GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(5, 5, 5, 5), 0, 0));
    customerBasicPanel.setLayout(new BorderLayout());
    customerBasicPanel.add(pnlCustBasic, BorderLayout.NORTH);
    customerBasicPanel.add(pnlAddress, BorderLayout.CENTER);
    customerBasicPanel.add(outerPnl, BorderLayout.EAST);
    viewPortPanel.add(customerBasicPanel, customerBasicPanel);
    showTitle(true);
    this.setBackground(theAppMgr.getBackgroundColor());
    this.getContentPane().add(viewPortPanel, BorderLayout.CENTER);
//    pnlAddress.setEnabled(false);
    pnlLastVisited = customerBasicPanel;
//    bShowPrevApplet = true;
    Verifiers verifiers = new Verifiers();

    // FirstName is optional for Japan
    if (!("JP".equalsIgnoreCase(Version.CURRENT_REGION))) {
      pnlCustBasic.getFirstNameJTXT().setVerifier(verifiers.
          getFirstNameVerifier());
    }
    pnlCustBasic.getLastNameJTXT().setVerifier(verifiers.getLastNameVerifier());
  }

  /**
   * Register AWT Listnerers for Applet.
   */
  private void addAWTListners() {
    java.awt.Toolkit.getDefaultToolkit().addAWTEventListener(mgmtEvtListener,
        AWTEvent.KEY_EVENT_MASK);
    java.awt.Toolkit.getDefaultToolkit().addAWTEventListener(mgmtEvtListener,
        AWTEvent.MOUSE_EVENT_MASK);
    pnlAddress.addItemListener(addTypeListener);
  }

  /**
   * Remove AWT Listeners.
   */
  private void removeAWTListeners() {
    java.awt.Toolkit.getDefaultToolkit().removeAWTEventListener(mgmtEvtListener);
    pnlAddress.removeItemListener(addTypeListener);
  }

  /**
   * PageDownEvent
   * @param e MouseEvent
   */
  public void pageDown(MouseEvent e) {
    pnlLastVisited = (JPanel)cardLayout.getCurrent(viewPortPanel);
    //bShowPrevApplet = false;
    if (pnlLastVisited.equals(pnlViewComments)) {
      pnlViewComments.pageDown();
      return;
    }
    if (pnlLastVisited.equals(pnlViewCreditCard)) {
      pnlViewCreditCard.pageDown();
      return;
    }
    if (pnlLastVisited.equals(pnlViewAddress)) {
      pnlViewAddress.pageDown();
      return;
    }
    cardLayout.next(viewPortPanel);
    while (cardLayout.getCurrent(viewPortPanel).equals(pnlViewComments)
        || cardLayout.getCurrent(viewPortPanel).equals(pnlViewCreditCard)
        || cardLayout.getCurrent(viewPortPanel).equals(pnlViewAddress)
        || cardLayout.getCurrent(viewPortPanel).equals(pnlCardDetails)
        || cardLayout.getCurrent(viewPortPanel).equals(pnlViewPurchHistory))
      cardLayout.next(viewPortPanel);
    pnlLastVisited = (JPanel)cardLayout.getCurrent(viewPortPanel);
    if (pnlLastVisited.equals(customerBasicPanel)) {
      showTitle(true);
      if (pnlCustBasic.getFirstNameJTXT().isEnabled())
        pnlCustBasic.requestFocusOnLastName();
      return;
    }
    showTitle(false);
  }


  /**
   * PageUpEvent
   * @param e MouseEvent
   */
  public void pageUp(MouseEvent e) {
    pnlLastVisited = (JPanel)cardLayout.getCurrent(viewPortPanel);
    if (pnlLastVisited.equals(customerBasicPanel))
      return;
    if (pnlLastVisited.equals(pnlViewComments)) {
      pnlViewComments.pageUp();
      return;
    }
    if (pnlLastVisited.equals(pnlViewCreditCard)) {
      pnlViewCreditCard.pageUp();
      return;
    }
    if (pnlLastVisited.equals(pnlViewAddress)) {
      pnlViewAddress.pageUp();
      return;
    }
    cardLayout.previous(viewPortPanel);
    while (cardLayout.getCurrent(viewPortPanel).equals(pnlViewComments)
        || cardLayout.getCurrent(viewPortPanel).equals(pnlViewCreditCard)
        || cardLayout.getCurrent(viewPortPanel).equals(pnlViewAddress)
        || cardLayout.getCurrent(viewPortPanel).equals(pnlCardDetails)
        || cardLayout.getCurrent(viewPortPanel).equals(pnlViewPurchHistory))
      cardLayout.previous(viewPortPanel);
    if (cardLayout.getCurrent(viewPortPanel).equals(customerBasicPanel)) {
      showTitle(true);
      if (pnlCustBasic.getFirstNameJTXT().isEnabled())
        pnlCustBasic.requestFocusOnLastName();
      return;
    }
    showTitle(false);
  }


  /**
   * Edit Area event
   * @param command Command
   * @param input Input
   */
  public void editAreaEvent(String sCommand, String sInput) {
    if (sCommand.equals("ADD_COMMENTS")) {
      CustomerComment comment = new CustomerComment();
      comment = new CustomerComment();
      comment.setDateCommented(new Date());
      comment.setComment(sInput);
      if (theTxn != null && theTxn instanceof CompositePOSTransaction
          && ((CompositePOSTransaction)theTxn).getConsultant() != null) {
        comment.setAssociateId(((CompositePOSTransaction)theTxn).getConsultant().getExternalID());
      } else
        comment.setAssociateId(((CMSEmployee)theOpr).getExternalID());
      comment.setBrandId(cmsStore.getBrandID());
      comment.setStoreId(cmsStore.getId());
      // Set the modified flag to set it in new comments array
      comment.setIsModified(true);
      pnlViewComments.addCustomerComment(comment);
      setEditArea("Select option", null, -1);
    }
  }


  /**
   * Show/Hide Title
   * @param bShowTitle true/false
   */
  private void showTitle(boolean bShowTitle) {
    if (bShowTitle)
      this.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder()
          , res.getString("Basic Information")));
    else
      this.setBorder(null);
  }


  /**
   * put your documentation comment here
   * @param Command
   * @param obj
   */
  public void objectEvent(String Command, Object obj) {
  }


  /**
   * put your documentation comment here
   */
  public void requestFocusOnAddress() {
    SwingUtilities.invokeLater(new Runnable() {

      /**
       * put your documentation comment here
       */
      public void run() {
        pnlAddress.cbCountry.requestFocus();
      }
    });
  }


  /**
   * SUB MENU EVENTS
   * @param sHeader SUB_MENU Name
   * @param anEvent CMSActionEvent
   */
  public void appButtonEvent(String sHeader, CMSActionEvent anEvent) {
  }


  /**
   * Button Event
   * @param anEvent CMSActionEvent
   */
  public void appButtonEvent(CMSActionEvent anEvent) {
    String sAction = anEvent.getActionCommand();
    if (sAction.equals("SEARCH")) {

      String customerCode = null;
      Address address = pnlAddress.getAddress();

      String city = address.getCity();
      String zip = address.getZipCode();
      String gender = pnlMiscSearch.getSelectedGender();
//      if (!(gender.equalsIgnoreCase("M") || gender.equalsIgnoreCase("F")))
//        gender = "";

      CustomerSearchString searchString = new CustomerSearchString();
      searchString.setCity(city);
      searchString.setCountry(address.getCountry());
      searchString.setZipCode(zip);
//      if (!((gender != null)
//          && ((gender.trim().equalsIgnoreCase("2")) || (gender.trim().equalsIgnoreCase("3"))))) {
//        gender = "";
//      }
      searchString.setGender(gender);
      //code added by deepika
      searchString.setAge(pnlMiscSearch.getAgeRangeCode());
      searchString.setLowerBirthDateRange(CustomerUtil.getLowerBirthDate(pnlMiscSearch.
          getAgeRangeCode()));
      searchString.setUpperBirthDateRange(CustomerUtil.getUpperBirthDate(pnlMiscSearch.
          getAgeRangeCode()));
      searchString.setIsDefaultCustomer(true);
      try {
		CMSCustomer cmsCustomers[] = (CMSCustomer[])CMSCustomerHelper.findBySearchQuery(theAppMgr
            , searchString);
        
         /*Started code changes for Bug#7120 DefaultCustomer Issue by Deepika 
          * to restrict default customer creation from POS.
          */
        if (cmsCustomers == null || cmsCustomers.length < 1) {
        	theAppMgr.showErrorDlg(res.getString(
            "No records found for default customer"));
        	 }
        
        else if(cmsCustomers != null)
        {
        	  theAppMgr.addStateObject("TXN_CUSTOMER", cmsCustomers);
        }
        theAppMgr.addStateObject("DEFAULT_SEARCH_LIST", cmsCustomers);
        }
      /*Ended code changes for Bug#7120 DefaultCustomer Issue by Deepika 
       * to restrict default customer creation from POS.
       */
     //commented by Deepika for Bug#7120 DefaultCustomer Issue.
      /*  if (cmsCustomers == null || cmsCustomers.length < 1) {
        
          CMSCustomer cmsCust = null;
          cmsCust = new CMSCustomer();
          cmsCust.setAge(pnlMiscSearch.getAgeRangeCode());
          cmsCust.setDateOfBirth(CustomerUtil.getEstimatedBirthDate(pnlMiscSearch.getAgeRangeCode()));
          address.setAddressLine1(".");

          if(zip != null && zip.trim().length() > 0)
            address.setZipCode(zip);
          else
            address.setZipCode(".");

          if(city != null && city.trim().length() > 0)
            address.setCity(city);
          else
            address.setCity(".");
          address.setUseAsPrimary(true);
          cmsCust.addAddress(address);
          cmsCust.setGender(gender);
          cmsCust.setFirstName("Customer-"+address.getCountry()+"-"+gender+"-"+cmsCust.getAge());
          cmsCust.setLastName(".");
          cmsCust.setIsDefaultCustomer(true);
          cmsCustomers = new CMSCustomer[1];
          cmsCustomers[0] = CMSCustomerHelper.submit(theAppMgr, cmsCust);
          if(cmsCustomers[0] != null) {
            cmsCust = cmsCustomers[0];
          } else {
            cmsCust.setNew();
			cmsCustomers[0] = cmsCust;
		  }
          theAppMgr.addStateObject("TXN_CUSTOMER", cmsCust);
        }
        theAppMgr.addStateObject("DEFAULT_SEARCH_LIST", cmsCustomers);
      }*/
        
	
     
        catch (TooManySearchResultsException e) {
        theAppMgr.showErrorDlg(res.getString("More than 100 query results"));
        anEvent.consume();
        pnlMiscSearch.cmbxAge.requestFocus();
        return;
      } catch (Exception e) {
        e.printStackTrace();
        showNoCustFound();
        anEvent.consume();
        pnlMiscSearch.cmbxAge.requestFocus();
        return;
      }

    }
  }

  /**
   * put your documentation comment here
   */
  private void showNoCustFound() {
    theAppMgr.showErrorDlg(res.getString("Sorry, no customer found"));
  }


  /**
   * Set Edit area message
   * @param sMessage Message
   * @param sCommand Command
   * @param iMask Mask
   */
  public void setEditArea(String sMessage, String sCommand, int iMask) {
    if (sCommand != null)
      theAppMgr.setSingleEditArea(res.getString(sMessage), sCommand, iMask);
    else
      theAppMgr.setSingleEditArea(res.getString(sMessage));
  }


  /**
   * Reset the fields.
   */
  public void reset() {
    pnlCustBasic.reset();
    //-MSB 01/31/06
    // ViewAddress list should be cleared
    // before reseting address panel.
    if (pnlViewAddress != null)
      pnlViewAddress.clear();
    pnlAddress.reset();
    pnlMiscSearch.reset();
    if (pnlViewComments != null)
      pnlViewComments.clear();
    if (pnlViewCreditCard != null)
      pnlViewCreditCard.clear();
    if (pnlCardDetails != null)
      pnlCardDetails.reset();
    if (pnlViewPurchHistory != null)
      pnlViewPurchHistory.clear();
    if (pnlMiscSearch != null)
      pnlMiscSearch.reset();
  }


  /**
   * Enable pages.
   * @param bEnabled True/False
   */
  public void setEnabled(boolean bEnabled) {
    pnlCustBasic.setEnabled(bEnabled);
    pnlAddress.setEnabled(bEnabled);
    pnlMiscSearch.setEnabled(bEnabled);
  }

  /**
   * Enable modify.
   * @param bEnabled True/False
   */
  public void setModifyEnabled(boolean bEnabled) {
    pnlCustBasic.setEnabled(bEnabled);
    pnlCustBasic.setEnabled(bEnabled);
    pnlAddress.setModifyEnabled(bEnabled);
    pnlMiscSearch.setEnabled(bEnabled);
  }

  private class AddressTypeListener implements ItemListener {

    /**
     * put your documentation comment here
     * @param itmEvt
     */
    public void itemStateChanged(ItemEvent itmEvt) {
      JCMSComboBox comp = (JCMSComboBox)itmEvt.getSource();
      if (bConsumeEvent)
        return;
      if (comp.getName().equals("ADDRESS_TYPE")) {
        if (pnlViewAddress != null && pnlViewAddress.getNumberAddresses() > 1
            && pnlViewAddress.addressTypeExists(pnlAddress.getAddressType()) != -1) {
          bConsumeEvent = true;
          comp.removeItemListener(this);
          theAppMgr.showErrorDlg(res.getString(
              "Address type already exists, please select a different type"));
          pnlAddress.setAddressType(pnlViewAddress.getPrimaryAddress().getAddressType());
          comp.addItemListener(this);
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
                  bConsumeEvent = false;
                }
              });
            }
          });
        }
      }
    }
  }


  /**
   *
   * <p>Title: CustomerMgmtEvtListener</p>
   * <p>Description: Traps Mouse and Tab events. </p>
   * <p>Copyright: Copyright (c) 2005</p>
   * <p>Company: SkillNet Inc</p>
   * @author Manpreet S Bawa
   * @version 1.0
   */
  private class CustomerMgmtEvtListener implements AWTEventListener {

    /**
     * put your documentation comment here
     * @param awtEvent
     */
    public void eventDispatched(AWTEvent awtEvent) {

      //- MSB 01/26/2006
      // Focus lost event for text fields is fired before
      // appButton event if button is pressed using
      // mouse. So in case of CANCEL event when everything has to be ignored
      // and no validation has to be done, focusEvent would still take
      // precedence and validate the data. To stop this event had to be captured
      // at event que level and checked if its CANCEL button event, focus Event validation
      // should be ignored.
      if (awtEvent instanceof MouseEvent) {
        MouseEvent me = (MouseEvent)awtEvent;
        if (me.getClickCount() == 1) {
          if (me.getComponent() instanceof NonFocusableButton) {
            NonFocusableButton button = (NonFocusableButton)me.getComponent();
            if (button.getMenuOption() == null)return;
            if (button.getMenuOption().getCommand().equals("CANCEL")
                &&
                cardLayout.getCurrent(viewPortPanel) instanceof CustomerDetailPanel
                ) {
              //State object to tell focuse event has to be consumed.
              theAppMgr.addStateObject("ARM_CONSUME_FOCUS_EVT", "TRUE");
            }
          }
        }
        return;
      }

      KeyEvent keyEvent;
      try {
        // Typecast to KeyEvent
        keyEvent = (KeyEvent)awtEvent;
      } catch (ClassCastException ce) {
        ce.printStackTrace();
        return;
      }
      if (keyEvent.getKeyCode() == KeyEvent.VK_ENTER) {
        if (awtEvent.getSource() instanceof JButton) {
          JButton btnEvent = (JButton)awtEvent.getSource();
          if (btnEvent.getActionCommand().equals("Lookup")) {
            if (!theAppMgr.isOnLine()) {
              theAppMgr.showErrorDlg(res.getString("Search not available this time"));
              btnEvent.setEnabled(false);
              return;
            }
            theAppMgr.fireButtonEvent("CUST_LOOKUP");
          }
        }
      }
      // Check If its TAB event.
      if (keyEvent.getKeyCode() == KeyEvent.VK_TAB) {
        // JCMSTextField is source
        if (awtEvent.getSource() instanceof JCMSTextField) {
          compTabEvent = (Component)awtEvent.getSource();
          /* if (compTabEvent.getName().trim().equals("AssociateID")) {
             pnlCustBasic.setFocusOnTitle();
             return;
           } else */if (compTabEvent.getName().trim().equals("AssociateID")
              || compTabEvent.getName().trim().equals("ChildNames")
              /*||compTabEvent.getName().trim().equals("DateOfIssue")*/) {
            invokeFocusThread();
            return;
          }
        }
        if (awtEvent.getSource() instanceof JCMSComboBox) {
          compTabEvent = (Component)awtEvent.getSource();
//          System.out.println("Event Name : " + compTabEvent.getName());
          if (compTabEvent.getName().indexOf("PHONE") != -1) {
            int iIndexDuplicatePhone = pnlAddress.containsDuplicatePhone();
            if (iIndexDuplicatePhone != -1) {
              /*if(iIndexDuplicatePhone==1)
               pnlAddress.requestFocusToPrimaryPhone();
               else if (iIndexDuplicatePhone == 2)
               pnlAddress.requestFocusToSecondaryPhone();
               else if (iIndexDuplicatePhone == 3)
               pnlAddress.requestFocusToTernaryPhone();
               */
              theAppMgr.showErrorDlg(res.getString(
                  "Duplicate phone type, please select a different type"));
              ((Component)awtEvent.getSource()).requestFocus();
              return;
            }
          }
        }
      }
    }
  }


  /**
   * Invoke thread to switch panels.
   */
  private void invokeFocusThread() {
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
            switchPanel();
          }
        });
      }
    });
  }


  /**
   * Switch panels
   */
  private void switchPanel() {
    // Check if TAB event was result of
    // focus lost not focus gain.
    if (!compTabEvent.hasFocus()) {
      if (compTabEvent.getName().trim().equals("ChildNames"))
        cardLayout.first(viewPortPanel);
      else
        cardLayout.next(viewPortPanel);
      if (cardLayout.getCurrent(viewPortPanel).equals(customerBasicPanel)) {
        showTitle(true);
        //PCR1325 Customer Details fix for Armani Japan
        if (custBasicPanelClassName == null) {
          pnlCustBasic.requestFocusOnFirstName();
        } else {
          pnlCustBasic.requestFocusOnLastName();
        }
      }
    }
  }


  //------------------------------------------------------------------
  //*********************   Verifier Classes   ***********************
   //------------------------------------------------------------------
   private class Verifiers {

     /**
      * @return
      */
     public CMSInputVerifier getFirstNameVerifier() {
       return (new CMSInputVerifier() {

         /**
          * @param c
          * @return
          */
         public boolean verify(JComponent c) {
           try {
             CMSCustomer cms = new CMSCustomer();
             if (cms != null)

               // Verify if its valid first name.
               cms.verifyFirstName(pnlCustBasic.getFirstName());
             return (true);
           } catch (BusinessRuleException bex) {
             theAppMgr.showErrorDlg(res.getString(bex.getMessage()));
             return (false);
           }
         }
       });
     } // getLastName


     /**
      * put your documentation comment here
      * @return
      */
     public CMSInputVerifier getLastNameVerifier() {
       return (new CMSInputVerifier() {

         /**
          * @param c
          * @return
          */
         public boolean verify(JComponent c) {
           try {
             CMSCustomer cms = new CMSCustomer();
             cms.setLastName(pnlCustBasic.getLastName());
             return (true);
           } catch (BusinessRuleException bex) {
             theAppMgr.showErrorDlg(res.getString(bex.getMessage()));
             return (false);
           }
         }
       });
     } // getLastName
   }


  /**
   * MP: Home pressed at customer display exits transaction with no message
   * @return
   */
  public boolean isHomeAllowed() {
    CMSCompositePOSTransaction cmsTxn = (CMSCompositePOSTransaction)theAppMgr.getStateObject(
        "TXN_POS");
    if (cmsTxn == null) {
      return (true);
    }
    return (theAppMgr.showOptionDlg(res.getString("Cancel Transaction")
        , res.getString("Are you sure you want to cancel this transaction?")));
  }
}
