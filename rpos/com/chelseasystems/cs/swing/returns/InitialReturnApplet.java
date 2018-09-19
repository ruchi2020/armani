/*
 * @copyright (c) 2001 Chelsea Market Systems LLC
 *
 * formatted with JxBeauty (c) johann.langhofer@nextra.at
 */
/*
 History:
 +------+------------+-----------+-----------+----------------------------------------------+
 | Ver# | Date       | By        | Defect #  | Description                                  |
 +------+------------+-----------+-----------+----------------------------------------------+
 | 4    | 06-08-2005 | Vikram    | 123       |Item Return Screen flow issue                 |
 +------+------------+-----------+-----------+----------------------------------------------+
 | 3    | 05-02-2005 | Khyati    | N/A       |Removed the comment length < 4                |
 +------+------------+-----------+-----------+----------------------------------------------+
 | 2    | 04-11-2005 | Khyati    | N/A       |1.Return Specification                        |
 ---------------------------------------------------------------------------------------------
 | 1    | 04-09-2005 | Khyati    | N/A       | Base                                         |
 +------+------------+-----------+-----------+----------------------------------------------+
 */


package com.chelseasystems.cs.swing.returns;

import java.awt.*;
import java.awt.event.*;
import java.util.*;
import javax.swing.*;
import javax.swing.table.*;
import com.chelseasystems.cr.appmgr.*;
import com.chelseasystems.cr.currency.*;
import com.chelseasystems.cs.store.CMSStore;
import com.chelseasystems.cs.swing.panel.TxnHeaderPanel;
import com.chelseasystems.cr.swing.event.CMSActionEvent;
import com.chelseasystems.cs.swing.menu.MenuConst;
import com.chelseasystems.cr.swing.ScrollableTableModel;
import com.chelseasystems.cs.pos.*;
import com.chelseasystems.cr.rules.*;
import com.chelseasystems.cs.customer.CMSCustomer;
import com.chelseasystems.cs.employee.CMSEmployee;
import com.chelseasystems.cs.item.CMSItem;
import com.chelseasystems.cr.swing.bean.*;
import javax.swing.border.Border;
import com.chelseasystems.cr.swing.*;
import com.chelseasystems.cs.swing.model.ReasonReturnModel;
import com.chelseasystems.cr.payment.CreditCard;
import com.chelseasystems.cr.payment.Payment;
import com.chelseasystems.cr.pos.*;
import com.chelseasystems.cr.pricing.PriceEngine;
import com.chelseasystems.cs.pos.*;
import com.chelseasystems.cs.swing.*;
import com.chelseasystems.cs.util.Version;
import com.ga.fs.fsbridge.ARMFSBridge;


/**
 */
public class InitialReturnApplet extends CMSApplet {
  private static final long serialVersionUID = 1L;
//public class InitialReturnApplet extends JApplet {
  //these are temp hardcoded until this becomes config driven
  /*
   public static final ReturnReasonStruct STOCK = new ReturnReasonStruct("RETURNING TO STOCK", false);
   public static final ReturnReasonStruct DAMAGED = new ReturnReasonStruct("DAMAGED MERCHANDISE", true);
   public static final ReturnReasonStruct PRICE new ReturnReasonStruct("ADJUST TO SALES PRICE", false);
   public static final ReturnReasonStruct AUDIT = new ReturnReasonStruct("REQUESTED BY SALES AUDIT",false);
   public static final ReturnReasonStruct OTHER = new ReturnReasonStruct("OTHER",false);
   */
  public ReturnReasonStruct[] returnReasons;
  private CMSCompositePOSTransaction saleTxn;
  private CMSCompositePOSTransaction returnTxn;
  private CMSCustomer customer;
  private boolean skippedCust = false;
  private boolean isGoHomeAllowed = false;
  static public Hashtable hReturnTxn = null;
  TxnHeaderPanel pnlTxnHeader = new TxnHeaderPanel();
  JCMSTextArea txtComments = new JCMSTextArea();
  ReasonReturnModel model;
  public String ReturnedFiscNum ;
  JCMSTable tblReason = new JCMSTable(model, JCMSTable.SELECT_ROW);
  //Removed as this boolean is no longer in use.
  //public static boolean isOrgPresale = false; 

  //Initialize the applet
  public void init() {
    try {
      returnReasons = ReturnReasonCfgMgr.getReturnReasons();
      model = new ReasonReturnModel(returnReasons);
      tblReason = new JCMSTable(model, JCMSTable.SELECT_ROW);
      model.setTblReason(tblReason);
      jbInit();
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  //Component initialization
  private void jbInit()
      throws Exception {
    JScrollPane pane = new JScrollPane(txtComments);
    JPanel pnlCenter = new JPanel();
    JPanel pnlComments = new JPanel();
    this.getContentPane().add(pnlTxnHeader, BorderLayout.NORTH);
    this.getContentPane().add(pnlCenter, BorderLayout.CENTER);
    pnlTxnHeader.setAppMgr(theAppMgr);
    pnlCenter.setLayout(new BorderLayout());
    pnlCenter.setBorder(BorderFactory.createEmptyBorder(0, 5, 5, 5));
    pnlComments.setBackground(theAppMgr.getEditAreaColor());
    pnlComments.setOpaque(false);
    pnlComments.setBorder(BorderFactory.createTitledBorder(res.getString("COMMENTS")));
    tblReason.setNextFocusableComponent(txtComments);
    txtComments.setNextFocusableComponent(tblReason);
    txtComments.setAppMgr(theAppMgr);
    pnlCenter.setBackground(theAppMgr.getBackgroundColor());
    pnlComments.setPreferredSize(new Dimension((int)(680 * r), (int)(133 * r)));
    pane.setPreferredSize(new Dimension((int)(810 * r), (int)(90 * r)));
    pnlCenter.add(pnlComments, BorderLayout.SOUTH);
    // pnlComments.add(txtComments, null);
    pnlComments.add(pane, null);
    pnlCenter.add(tblReason, BorderLayout.CENTER);
    pnlCenter.add(tblReason.getTableHeader(), BorderLayout.NORTH);
    tblReason.setAppMgr(theAppMgr);
    pane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
    pane.setVerticalScrollBar(new JColorScrollBar(theAppMgr.getTheme().getBackground()));
    tblReason.registerKeyboardAction(new ActionListener() {

      /**
       * put your documentation comment here
       * @param e
       */
      public void actionPerformed(ActionEvent e) {
        SwingUtilities.invokeLater(new Runnable() {

          /**
           * put your documentation comment here
           */
          public void run() {
            clickEvent(null);
          }
        });
      }
    }, KeyStroke.getKeyStroke(KeyEvent.VK_SPACE, 0), JComponent.WHEN_IN_FOCUSED_WINDOW);
    tblReason.getColumnModel().getColumn(0).setCellRenderer(new RadioRenderer());
    tblReason.addMouseListener(new MouseAdapter() {

      /**
       * put your documentation comment here
       * @param e
       */
      public void mouseClicked(MouseEvent e) {
        clickEvent(e);
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

		Action upArrow = new AbstractAction() {
			public void actionPerformed(ActionEvent e) {
				int row = tblReason.getSelectedRow();
				int totalRows = tblReason.getRowCount();
				if( totalRows > 0 && row > 0 ) {
					tblReason.changeSelection(row-1,1,false,false);
				}
			}
		};
		
		Action downArrow = new AbstractAction() {
			public void actionPerformed(ActionEvent e) {
				int row = tblReason.getSelectedRow();
				int totalRows = tblReason.getRowCount();
				if( totalRows > 0 && row < totalRows-1 ) {
					tblReason.changeSelection(row+1,1,false,false);
				}
			}
		};

		Action ctrlTab = new AbstractAction() {
			public void actionPerformed(ActionEvent e) {
			}
		};
		//For UP ARROW
		KeyStroke upKey = KeyStroke.getKeyStroke (KeyEvent.VK_UP,0);
		tblReason.getActionMap().put(tblReason.getInputMap().get(upKey), upArrow);
		//For DOWN ARROW
		KeyStroke downKey = KeyStroke.getKeyStroke (KeyEvent.VK_DOWN,0);
		tblReason.getActionMap().put(tblReason.getInputMap().get(downKey), downArrow);

		//For Shift+Tab / Ctrl + Shift + Tab
		KeyStroke ctrLTabKey = KeyStroke.getKeyStroke (KeyEvent.VK_TAB,2);
		tblReason.getActionMap().put(tblReason.getInputMap().get(ctrLTabKey), ctrlTab);
		
  }

  /**
   * This method clears out all the text fields that might have had text before.
   */
  public void clearTextFields() {
    pnlTxnHeader.clear();
    txtComments.setText("");
    enableComments(false);
    while (model.getCurrentPageNumber() != 0)
      model.prevPage();
  }

  //Start the applet
  public void start() {
    tblReason.clearSelection();
    clearTextFields();
    theAppMgr.setTransitionColor(theAppMgr.getBackgroundColor());
    theOpr = (com.chelseasystems.cs.employee.CMSEmployee)theAppMgr.getStateObject("OPERATOR");
    if (theAppMgr.getStateObject("CUSTOMER_SCREEN_CANCELLED") != null) {
      // returning from ding to enter customer, let state remain
      activateTable(true);
      theAppMgr.showMenu(MenuConst.ENTER_COMMENT, "OK", theOpr, theAppMgr.PREV_BUTTON);
      theAppMgr.removeStateObject("CUSTOMER_SCREEN_CANCELLED");
      selectReason();
      return;
    }
    saleTxn = (CMSCompositePOSTransaction)theAppMgr.getStateObject("TXN_POS");
    isGoHomeAllowed = (saleTxn == null);
    if (saleTxn == null)
      allocTransaction();
    if (theAppMgr.getStateObject("ARM_RETURN_TXN_OBJECTS") != null)
      hReturnTxn = (Hashtable)theAppMgr.getStateObject("ARM_RETURN_TXN_OBJECTS");
    if (theAppMgr.getStateObject("RETURN_COMMENTS") != null)
      txtComments.setText((String)theAppMgr.getStateObject("RETURN_COMMENTS"));
    theAppMgr.removeStateObject("RETURN_COMMENTS");
    if (theAppMgr.getStateObject("RETURN_REASON") != null) {
      int index = getReasonStructIndex((String)theAppMgr.getStateObject("RETURN_REASON"));
      tblReason.getSelectionModel().setSelectionInterval(index, index);
    }
    theAppMgr.removeStateObject("RETURN_REASON");
    if (theAppMgr.getStateObject("RETURN_TXN_POS") != null) {
      if (hReturnTxn == null) {
        hReturnTxn = new Hashtable(60);
      }
      CMSCompositePOSTransaction cacheTxn = (CMSCompositePOSTransaction)hReturnTxn.get(((
          CMSCompositePOSTransaction)theAppMgr.getStateObject("RETURN_TXN_POS")).getId());
      if (cacheTxn != null)
        returnTxn = cacheTxn;
      else {
        System.out.println("Putting in state object !!!");
        returnTxn = (CMSCompositePOSTransaction)theAppMgr.getStateObject("RETURN_TXN_POS");
        hReturnTxn.put(returnTxn.getId(), returnTxn);
        theAppMgr.addStateObject("ARM_RETURN_TXN_OBJECTS", hReturnTxn);
      }
      initHeader();
      selectReason();
      activateTable(true);
      theAppMgr.showMenu(MenuConst.ENTER_COMMENT, "OK", theOpr, theAppMgr.PREV_BUTTON);
    } else if (theAppMgr.getStateObject("RETURN_TXN") != null) { // txn from txn hist
      if (hReturnTxn == null) {
        hReturnTxn = new Hashtable(60);
      }
      CMSCompositePOSTransaction cacheTxn = (CMSCompositePOSTransaction)hReturnTxn.get(((
          CMSCompositePOSTransaction)theAppMgr.getStateObject("RETURN_TXN")).getId());
      if (cacheTxn != null)
        returnTxn = cacheTxn;
      else {
        System.out.println("Putting in state object !!!");
        returnTxn = (CMSCompositePOSTransaction)theAppMgr.getStateObject("RETURN_TXN");
        hReturnTxn.put(returnTxn.getId(), returnTxn);
        theAppMgr.addStateObject("ARM_RETURN_TXN_OBJECTS", hReturnTxn);
      }
      initHeader();
      selectReason();
      activateTable(true);
      theAppMgr.showMenu(MenuConst.ENTER_COMMENT, "OK", theOpr, theAppMgr.PREV_BUTTON);
    } else if (theAppMgr.getStateObject("RETURN_SALE") != null
        && ((String)theAppMgr.getStateObject("RETURN_SALE")).trim().equals("RETURN_SALE")) { // txn from txn hist
      selectReason();
      activateTable(true);
      theAppMgr.showMenu(MenuConst.ENTER_COMMENT, "OK", theOpr, theAppMgr.PREV_BUTTON);
    } else { // no return present, so prompt Eu for txn number
      activateTable(false);
      enterTxnId();
      theAppMgr.showMenu(MenuConst.RETURN_HIST, "RETURN", theOpr, theAppMgr.PREV_BUTTON);
    }
    theAppMgr.removeStateObject("RETURN_TXN_POS");
    theAppMgr.removeStateObject("RETURN_SALE");
  }

  //Stop the applet
  public void stop() {
    theAppMgr.removeStateObject("RETURN_POS");
    theAppMgr.removeStateObject("RETURN_TXN");
  }

  //
  public String getScreenName() {
    return (res.getString("Return Sale"));
  }

  //
  public String getVersion() {
    return ("$Revision: 1.2 $");
  }

  /**
   * @return
   */
  public boolean isHomeAllowed() {
   if (isGoHomeAllowed) {
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






			ARMFSBridge.postARMSignOffTransaction((CMSEmployee) theAppMgr
					.getStateObject("OPERATOR"));
		}





		return goToHomeView;
		/*
		 * --END
		 */





  }

  /**
   */
  public void appButtonEvent(String header, CMSActionEvent anEvent) {
    try {
      String sAction = anEvent.getActionCommand();
      System.out.println("sAction " + sAction);
      if (sAction.equals("TXN_HIST")) { //case looking for an old Txn
      } else if (sAction.equals("MERCH_RETURN")) {
    	theAppMgr.addStateObject("ARM_COMMAND", sAction);
        returnTxn = null;
        pnlTxnHeader.clear();
        theAppMgr.showMenu(MenuConst.ENTER_COMMENT, "MISC", theOpr, theAppMgr.PREV_BUTTON);
        selectReason();
        activateTable(true);
      } else if (sAction.equals("OK")) { //case getting the exact Txn.
    	String command = (String)theAppMgr.getStateObject("ARM_COMMAND");
    	if (command != null && command.length() > 0 && command.equalsIgnoreCase("TXN_ID")) {
    		if (getReturnReasonStruct() != null && getReturnReasonStruct().key != null && 
    				getReturnReasonStruct().key.equalsIgnoreCase("ONLINE_RETURN")) {
    			  		theAppMgr.showErrorDlg(res.getString(
    			  			"This reason code should be selected only for returns with no receipt."));            
    			  		anEvent.consume();
    			  		return;
    		}
    	}
        if (header.equals("COMMENTS")) {
          if (txtComments.getText() != null && txtComments.getText().trim().length() == 0) {
            theAppMgr.showErrorDlg(res.getString(
                "You must enter valid comments for the return reason."));
            enableComments(true);
            anEvent.consume();
            return;
          }
		//set return item comment limit to 100
          else  if ("JP".equalsIgnoreCase(Version.CURRENT_REGION) && txtComments.getText() != null && txtComments.getText().trim().length() > 100) {
              theAppMgr.showErrorDlg(res.getString(
                  "This field is limited to maximum 100 characters."));
              enableComments(true);
              anEvent.consume();
              return;
            }
          //ends here
          enableComments(false);
        } else if (testReason()) {
          if (returnTxn != null) {
            theAppMgr.addStateObject("RETURN_TXN_POS", returnTxn);
          } else {
            theAppMgr.addStateObject("RETURN_SALE", "RETURN_SALE");
          }
          theAppMgr.addStateObject("RETURN_COMMENTS", txtComments.getText());
          theAppMgr.addStateObject("RETURN_REASON", getReturnReasonStruct().key);
          return; // _State will goto next applet
        }
        anEvent.consume();
      } else if (sAction.equals("ENTER_COMMENT")) {
        enableComments(true);
      } else if (sAction.equals("CANCEL")) {} else if (sAction.equals("PREV")) {
        if (header.equals("MISC")) {
          start();
          anEvent.consume();
        }
        else if (header.equals("RETURN")) {
          // Set the SALE_MODE
          theAppMgr.addStateObject("TXN_MODE", new Integer(0));
        }
      }
    } catch (Exception ex) {
      theAppMgr.showExceptionDlg(ex);
    }
  }

  /**
   */
  public void selectReason() {
    theAppMgr.setSingleEditArea(res.getString("Select the reason for the return."));
  }

  /** Method used to get information from remote services */
  private void initHeader() {
    pnlTxnHeader.clear();
    PaymentTransactionAppModel appModel = returnTxn.getAppModel(CMSAppModelFactory.getInstance()
        , theAppMgr);
    pnlTxnHeader.showTransaction(appModel);
	//Vivek Mishra : Merged updated code from source provided by Sergio 19-MAY-16
	ReturnedFiscNum = returnTxn.getFiscalReceiptNumber();
	//Ends here
  }

  /**
   * @param command
   * @param value
   */
  public void editAreaEvent(String command, String value) {
    try {
      if (command.equals("TXN_ID")) {
    	theAppMgr.addStateObject("ARM_COMMAND", command);
        if (!theAppMgr.isOnLine()) {
          theAppMgr.showErrorDlg(res.getString(
              "Cannot retrieve transaction in offline mode.  Please select 'Misc Return' instead."));
          return;
        }
        if (value == null) {
          theAppMgr.showErrorDlg(res.getString("Cannot find transaction."));
          return;
        }
        //Khyati: Maintain List of already selected Return txns to avoid reseleciton of the same item on ReturnSaleApplet
        Hashtable hReturntable = (Hashtable)theAppMgr.getStateObject("ARM_RETURN_TXN_OBJECTS");
        if (hReturntable != null && hReturntable.containsKey(value)) {
          System.out.println("Should be Here !!!!!!!!!!!!!!!!!");
          returnTxn = (CMSCompositePOSTransaction)hReturntable.get(value);
        } else {
          System.out.println("And Not Here !!!!!!!!!!!!!!!!!");
          returnTxn = (CMSCompositePOSTransaction)CMSTransactionPOSHelper.findById(theAppMgr, value);
  //Anjana:Restrict the cashier to perform returns with receipt if there was no token in original sale
          String storeCountry = ((CMSStore)theAppMgr.getGlobalObject("STORE")).getCountry();
 //Mahesh Nandure : 08 MAY 2017 : Return transaction message for Europe
//Mahesh-start-bugno 28576-added the jp check also because it does not require in japan
          if(!storeCountry.equals("CAN") && !"EUR".equalsIgnoreCase(Version.CURRENT_REGION) && !"JP".equalsIgnoreCase(Version.CURRENT_REGION)){

          if(returnTxn!=null){
          Payment[] payments = returnTxn.getPaymentsArray();
          for (int i=0; i< payments.length; i++) {
        	  if(payments[i] instanceof CreditCard){
        		  CreditCard cc= (CreditCard)payments[i];
        		  if(cc.getTokenNo()==null || cc.getTokenNo().equalsIgnoreCase("")||cc.getTokenNo().length()==0){
        			  theAppMgr.showErrorDlg(res.getString(
        			          "The original credit card transaction was not processed on the pinpad.Please process this refund using a “No Receipt Return”."));
        			  enterTxnId();
        			  return;
        		  }
        	  }
							}
						}
					}
          if (returnTxn == null) {
            theAppMgr.showErrorDlg(res.getString("Cannot find transaction."));
            clearTextFields();
            enterTxnId();
            return;
          }
			//Anjana added to show a pop up to cashier if org txn had items on item threshold or multiunit promotions
			//Mahesh-start-bugno 28576-added the jp check also because it does not require in japan
		if(!"JP".equalsIgnoreCase(Version.CURRENT_REGION)){
          String promCode = null;
          POSLineItem[] lineItems = returnTxn.getLineItemsArray();
          for (int i = 0; i < lineItems.length; i++) {
        	   promCode = CMSTransactionPOSHelper.findPromCodeByTxnIdandBarcode(theAppMgr, returnTxn.getId(),lineItems[i].getExtendedBarCode());
                if(promCode!=null){
        	  theAppMgr.showErrorDlg(res.getString("The item being returned was part of a promotion. If only one item is being returned, follow the directions in the “Return with Receipt” section of the POS manual"));
        	  break;
        	  }
             } 
			}
        //Vivek Mishra : Added changes for fetching the original PRESALE Open txn.
          if("US".equalsIgnoreCase(Version.CURRENT_REGION) && returnTxn.getOrgPRSOTxn()!=null)
          {
        	  //isOrgPresale = true;
        	  System.out.println(returnTxn.getReturnTotalQuantityOfItems());
    		  System.out.println(returnTxn.getLineItemsArray().length);
        	  //returnTxn.addEntirePresaleTransactionAsReturn(((CMSCompositePOSTransaction)returnTxn.getOrgPRSOTxn()).getPresaleTransaction());
        	  System.out.println(returnTxn.getReturnTotalQuantityOfItems());
        	  System.out.println(returnTxn.getLineItemsArray().length);
        	  POSLineItem[] array1 = ((CMSCompositePOSTransaction)returnTxn.getOrgPRSOTxn()).getPresaleTransaction().getLineItemsArray();
        	  POSLineItem[] array2 = returnTxn.getSaleLineItemsArray();
        	  ArmCurrency miscAmount = new ArmCurrency(0.0d);
        	  for(int i =0; i<array1.length; i++)
        	  {
        		  array2[i].doSetItemRetailPrice(array1[i].getExtendedRetailAmount());
        		  array2[i].doSetItemSellingPrice(array1[i].getExtendedNetAmount());
        		  array2[i].doSetUnitTax(array1[i].getExtendedTaxAmount());
        		 ((POSLineItemDetail)array2[i].getLineItemDetails().nextElement()).doSetNetAmount(array1[i].getExtendedNetAmount());
        		 ((POSLineItemDetail)array2[i].getLineItemDetails().nextElement()).doSetDealMarkdownAmount(array1[i].getExtendedReductionAmount());
        		 //Vivek Mishra : Added to calculate and subtract the amount tendered for misc items as that is not refundable. 
        		 if(array1[i].getMiscItemId()!=null && (array1[i].getMiscItemId().equalsIgnoreCase("SHIP") || array1[i].getMiscItemId().equalsIgnoreCase("ALTERATION")))
        		 {
        			 miscAmount = miscAmount.add(array1[i].getExtendedNetAmount().add(array1[i].getExtendedTaxAmount()));
        		 }
        	  }
        	  Payment[] prsoPayments = (returnTxn.getOrgPRSOTxn()).getPaymentsArray();
        	  for(int j=0;j<prsoPayments.length;j++)
        	  {
        	  if(prsoPayments[j].getAmount().greaterThanOrEqualTo(miscAmount))
        	  {
        		  prsoPayments[j].setAmount(prsoPayments[j].getAmount().subtract(miscAmount));
        	  }
        	  if(prsoPayments[j].getAmount().greaterThan(new ArmCurrency(0.0d)));
        	  returnTxn.addPayment(prsoPayments[j]);
        	  }
        	  //returnTxn = (CMSCompositePOSTransaction)CMSTransactionPOSHelper.findById(theAppMgr, returnTxn.getOrgPRSOTxn().getId());
        	  //returnTxn.convertPresaleToSaleTransaction();
          }
          //Ends
          if (hReturnTxn == null && returnTxn != null) {
            hReturnTxn = new Hashtable(60);
          }
          if (returnTxn != null && value != null) {
            hReturnTxn.put(value, returnTxn);
            theAppMgr.addStateObject("ARM_RETURN_TXN_OBJECTS", hReturnTxn);
          }
        }
        //Vivek Mishra : Added to set tax exempt id from sale transaction into return transaction for Europe region 28_JUN_2016
        CMSCompositePOSTransaction theTxn = (CMSCompositePOSTransaction)theAppMgr.getStateObject("TXN_POS");
        if("EUR".equalsIgnoreCase(Version.CURRENT_REGION) && returnTxn!=null && returnTxn.getTaxExemptId()!=null && returnTxn.getTaxExemptId()!="")
        {
        	theTxn.setTaxExemptId(returnTxn.getTaxExemptId());
        }
        //Ends here
        initHeader();
        returnTxn.testIsReturnable(); // throws bre
        //RulesInfo result = returnTxn.isReturnable();
        if (returnTxn.isVoided()) {
          returnTxn = null;
          theAppMgr.showErrorDlg(res.getString(
              "This tranasction has been voided and is not returnable."));
          enterTxnId();
          return;
        }
        selectReason();
        activateTable(true);
        theAppMgr.showMenu(MenuConst.ENTER_COMMENT, "OK", theOpr, theAppMgr.PREV_BUTTON);
      }
    } catch (BusinessRuleException bre) {
      returnTxn = null;
      theAppMgr.showErrorDlg(res.getString(bre.getMessage()));
      enterTxnId();
    } catch (ClassCastException cce) {
      returnTxn = null;
      theAppMgr.showErrorDlg(res.getString(
          "This tranasction has no line items and is not returnable."));
      enterTxnId();
    } catch (Exception ex) {
      theAppMgr.showExceptionDlg(ex);
    }
  }

  /**
   */
  private void enterTxnId() {
    if (theAppMgr.isOnLine())
      theAppMgr.setSingleEditArea(res.getString("Enter transaction ID."), "TXN_ID"
          , theAppMgr.TRANS_ID_MASK);
    else
      theAppMgr.setSingleEditArea(res.getString("Select option."));
  }

  /**
   */
  private void allocTransaction() {
    try {
      saleTxn = CMSTransactionPOSHelper.allocate(theAppMgr);
      theAppMgr.addStateObject("TXN_POS", saleTxn);
    } catch (BusinessRuleException ex) {
      theAppMgr.showExceptionDlg(ex);
      theAppMgr.showMenu(MenuConst.NO_BUTTONS, theOpr);
      theAppMgr.setSingleEditArea(res.getString("Press 'Home' to return home."));
    }
  }

  /**
   * Method to make sure that a reason is selected.
   * @return
   */
  private boolean testReason() {
    ReturnReasonStruct returnReasonStruct = this.getReturnReasonStruct();
    if (returnReasonStruct == null) {
      theAppMgr.showErrorDlg(res.getString("You must select a reason for the return."));
      return (false);
    }
    if (returnReasonStruct.commentsRequired && txtComments.getText().length() == 0) {
      enableComments(true);
      theAppMgr.showErrorDlg(res.getString("You must enter valid comments for the return reason."));
      return (false);
    }
    return (true);
  }

  /**
   * @return
   */
  private String getReturnReason() {
    int row = tblReason.getSelectedRow();
    if (row > -1)
      return (String)model.getValueAt(row, 1);
    else
      return (null);
  }

  /**
   * put your documentation comment here
   * @return
   */
  private ReturnReasonStruct getReturnReasonStruct() {
    int row = tblReason.getSelectedRow();
    if (row > -1)
      return (model.getReasonStructAt(row));
    else
      return (null);
  }

  /**
   */
  private void activateTable(boolean enable) {
    tblReason.setEnabled(enable);
    tblReason.repaint();
  }

  /**
   */
  private void enableComments(boolean b) {
    if (b) {
      theAppMgr.setSingleEditArea(res.getString("Enter comments and select 'OK' when done."));
      theAppMgr.showMenu(MenuConst.OK_ONLY, "COMMENTS", theOpr);
      txtComments.setEnabled(true);
      SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                  txtComments.requestFocus();
            }
      });
    } else {
      selectReason();
      theAppMgr.showMenu(MenuConst.ENTER_COMMENT, "OK", theOpr, theAppMgr.PREV_BUTTON);
      txtComments.setEnabled(false);
      SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                  tblReason.requestFocusInWindow();
            }
      });
      //TD
      //theAppMgr.setEditAreaFocus();
    }
  }

  /**
   * @param e
   */
  public void clickEvent(MouseEvent me) {
    if (tblReason.isEnabled() && tblReason.hasFocus() && tblReason.getSelectedRow() > -1) {
      if (model.getReasonStructAt(tblReason.getSelectedRow()).commentsRequired) {
        enableComments(true);
      } else {
        enableComments(false);
      }
    }
  }

  /**
   * callback when <b>Page Down</b> icon is clicked
   */
  public void pageDown(MouseEvent e) {
    if (model.getCurrentPageNumber() < model.getPageCount() - 1) {
      model.nextPage();
      tblReason.removeRowSelectionInterval(0, tblReason.getRowCount() - 1);
      tblReason.addRowSelectionInterval(0, 0);
    }
    theAppMgr.showPageNumber(e, model.getCurrentPageNumber() + 1, model.getPageCount());
    SwingUtilities.invokeLater(new Runnable() {
        public void run() {
            tblReason.requestFocusInWindow();
        }
    });
  }

  /**
   * callback when <b>Page Up</b> icon is clicked
   */
  public void pageUp(MouseEvent e) {
    if (model.getCurrentPageNumber() > 0) {
      model.prevPage();
      tblReason.removeRowSelectionInterval(0, tblReason.getRowCount() - 1);
      tblReason.addRowSelectionInterval(model.getRowsShown() - 1, model.getRowsShown() - 1);
    }
    theAppMgr.showPageNumber(e, model.getCurrentPageNumber() + 1, model.getPageCount());
    SwingUtilities.invokeLater(new Runnable() {
        public void run() {
            tblReason.requestFocusInWindow();
        }
    });
  }

  /**
   */
  public void tblReason_componentResized(ComponentEvent e) {
    model.setRowsShown(tblReason.getHeight() / tblReason.getRowHeight());
    tblReason.getColumnModel().getColumn(1).setPreferredWidth((int)(750 * r));
    tblReason.repaint();
  }

  /**
   * put your documentation comment here
   * @param returnReasonKey
   * @return
   */
  private int getReasonStructIndex(String returnReasonKey) {
    Vector reasonStructVec = model.getAllRows();
    for (int i = 0; i < reasonStructVec.size(); i++) {
      if (returnReasonKey.trim().equals(((ReturnReasonStruct)reasonStructVec.elementAt(i)).key.trim()))
        return i;
    }
    return -1;
  }

  /********************************************************************/
  private class RadioRenderer extends JRadioButton implements TableCellRenderer {

    /**
     * put your documentation comment here
     */
    public RadioRenderer() {
      this.setOpaque(false);
      this.setHorizontalAlignment(SwingConstants.CENTER);
    }

    /**
     * put your documentation comment here
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
} // end of class

