/*
 * @copyright (c) 2001, Chelsea Market Systems LLC
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
 | 3    | 04-28-2005 | Pankaja   | N/A       |1.Rectified the problem related to quantity   |
 ---------------------------------------------------------------------------------------------
 | 2    | 04-11-2005 | Khyati    | N/A       |1.Return Specification                        |
 ---------------------------------------------------------------------------------------------
 | 1    | 04-11-2005 | Khyati    | N/A       | Base                                         |
 +------+------------+-----------+-----------+----------------------------------------------+
 */


package com.chelseasystems.cs.swing.returns;

import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.ComponentEvent;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.MissingResourceException;
import java.util.Set;
import java.util.StringTokenizer;
import java.util.Vector;
import java.util.Enumeration;
import javax.swing.*;
import javax.swing.table.*;
import com.chelseasystems.cr.swing.CMSApplet;
import com.chelseasystems.cs.store.CMSStore;
import com.chelseasystems.cs.swing.menu.MenuConst;
import com.chelseasystems.cs.customer.CMSCustomer;
import com.chelseasystems.cs.employee.CMSEmployee;
import com.chelseasystems.cr.swing.bean.JCMSTable;
import com.chelseasystems.cr.swing.event.CMSActionEvent;
import com.chelseasystems.cr.pos.*;
import com.chelseasystems.cs.pos.CMSCompositePOSTransaction;
import com.chelseasystems.cs.pos.CMSTransactionPOSHelper;
import com.chelseasystems.cs.payment.CMSRedeemableHelper;
import com.chelseasystems.cr.payment.GiftCert;
import com.chelseasystems.cr.payment.Redeemable;
import com.chelseasystems.cr.register.Register;
import com.chelseasystems.cr.rules.BusinessRuleException;
import com.chelseasystems.cs.swing.model.ReturnSaleModel;
import com.chelseasystems.cs.swing.panel.TxnHeaderPanel;
import com.chelseasystems.cs.swing.CMSAppModelFactory;
import com.chelseasystems.cr.pos.*;
import com.chelseasystems.cr.item.*;
import com.chelseasystems.cr.swing.MultiLineCellRenderer;
import java.util.Hashtable;
import com.chelseasystems.cs.pos.CMSSaleLineItem;
import com.chelseasystems.cs.swing.bean.ArmJCMSTable;
import com.chelseasystems.cr.config.ConfigMgr;
import com.chelseasystems.cs.item.*;
import com.chelseasystems.cs.util.*;
import com.chelseasystems.cr.payment.Payment;
import com.chelseasystems.cs.payment.CMSPremioDiscount;

/**
 */
public class ReturnSaleApplet extends CMSApplet {
  //public class ReturnSaleApplet extends JApplet {

  /** the building txn (case return with other purchase) */
  private CMSCompositePOSTransaction theTxn;

  /** the returning txn */
  private CMSCompositePOSTransaction returnTxn;
  private CMSCustomer cust;
  private boolean selectAll = false;
  ReturnSaleModel model;
  JCMSTable tblItem;
  TxnHeaderPanel pnlHeader;
  public static Hashtable hReturnTxns = null;
//Vivek Mishra : Added for not showing AJB servers down dialogue multiple times.
  private int count = 0;
  private int lineCount = 0; 
  //End	
	private static ConfigMgr config;
	private String fipay_flag;
  //Initialize the applet
  public void init() {
    model = new ReturnSaleModel();
    tblItem = new ArmJCMSTable(model, JCMSTable.SELECT_ROW);
    pnlHeader = new TxnHeaderPanel();
    try {
      jbInit();
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  //Component initialization
	private void jbInit() throws Exception {
    JPanel pnlSummary = new JPanel();
    JPanel pnlTable = new JPanel();
    pnlSummary.setPreferredSize(new Dimension((int)(670 * r), (int)(20 * r)));
    pnlTable.setLayout(new BorderLayout());
    pnlTable.add(tblItem.getTableHeader(), BorderLayout.NORTH);
    this.getContentPane().add(pnlHeader, BorderLayout.NORTH);
    this.getContentPane().add(pnlSummary, BorderLayout.SOUTH);
    this.getContentPane().add(pnlTable, BorderLayout.CENTER);
    pnlTable.add(tblItem, BorderLayout.CENTER);
    pnlSummary.setBackground(theAppMgr.getBackgroundColor());
    pnlHeader.setAppMgr(theAppMgr);
    tblItem.setAppMgr(theAppMgr);
    TableColumnModel model = tblItem.getColumnModel();
    TableColumnModel modelColumn = tblItem.getColumnModel();
    tblItem.addMouseListener(new java.awt.event.MouseAdapter() {

      /**
       * put your documentation comment here
       * @param e
       */
      public void mouseClicked(MouseEvent e) {
        clickEvent(e);
      }
    });
    tblItem.addComponentListener(new java.awt.event.ComponentAdapter() {

      /**
       * put your documentation comment here
       * @param e
       */
      public void componentResized(ComponentEvent e) {
        tblItem_componentResized(e);
      }
    });
    // set cell renderer
    TextRenderer renderer = new TextRenderer();
    for (int i = 1; i < modelColumn.getColumnCount(); i++)
      modelColumn.getColumn(i).setCellRenderer(renderer);
    CheckBoxRenderer boxRenderer = new CheckBoxRenderer();
    modelColumn.getColumn(0).setCellRenderer(boxRenderer);
    modelColumn.getColumn(2).setCellRenderer(new MultiLineCellRenderer());
  }

  //Stop the applet
	public void stop() {
	}

  /** Returns screen name */
  public String getScreenName() {
    return res.getString("Return Transaction");
  }

  /** Returns version */
  public String getVersion() {
    return "$Revision: 1.2 $";
  }

  /**
   * initHeader
   * initializes the OperatorName, ConsultantName
   * and CustomerName and Phone
   */
  private void populateScreen() {
    PaymentTransactionAppModel appModel = returnTxn.getAppModel(CMSAppModelFactory.getInstance()
        , theAppMgr);
    pnlHeader.showTransaction(appModel);
    for (Enumeration enm = returnTxn.getSaleLineItems(); enm.hasMoreElements(); ) {
      SaleLineItem line = (SaleLineItem)enm.nextElement();
      model.addNewItem(line);
      
    }
    if (!model.containsReturnableItems()) {
      theAppMgr.showErrorDlgLater(res.getString(
          "There are no returnable items found in this transaction."));
      theAppMgr.showMenu(MenuConst.CANCEL_ONLY, theOpr);
    }
    //Vivek Mishra : Added for sending 150 prompt AJB request. 
    else
    {
    	lineCount = returnTxn.getLineItemsArray().length;
    	//Vivek Mishra : Changed to send a single 150 request with all the items
    	/*for(POSLineItem item : returnTxn.getLineItemsArray())
    	{
    	 sendItemMessageData(item ,lineItemArray,  false, false);
    	}*/
    	//Vivek Mishra : Changed for sending only the refresh message.
    	//sendItemMessageData(returnTxn.getLineItemsArray()[0] ,returnTxn.getLineItemsArray(),false,false,false);
    	
    	
		if(fipay_flag!=null && fipay_flag.equalsIgnoreCase("Y")){

    	sendItemMessageData(returnTxn.getLineItemsArray()[0] ,returnTxn.getLineItemsArray(),true,false,false,false);
		}
    }
    //End
  }

  //Start the applet
  public void start() {
	  
	 	String fileName = "store_custom.cfg";
		config = new ConfigMgr(fileName);
		fipay_flag = config.getString("FIPAY_Integration");
		
		if (fipay_flag == null) {
			fipay_flag = "Y";
		}
		
    theAppMgr.setTransitionColor(theAppMgr.getBackgroundColor());
    theOpr = (CMSEmployee)theAppMgr.getStateObject("OPERATOR");
    model.clear();
    selectAll = false;
    //ks: Get the Hashtable of all return txn
    hReturnTxns = (Hashtable)theAppMgr.getStateObject("ARM_RETURN_TXN_OBJECTS");
    //theAppMgr.removeStateObject("ARM_RETURN_TXN_OBJECTS");
    //check if there exists a building SaleTxn
    theTxn = (CMSCompositePOSTransaction)theAppMgr.getStateObject("TXN_POS");
    //now check if there exists an exact TxnPOS needed returning
    returnTxn = (CMSCompositePOSTransaction)theAppMgr.getStateObject("RETURN_TXN_POS");
    if (returnTxn == null) {
      //then this Txn should be found from txn history list
      returnTxn = (CMSCompositePOSTransaction)theAppMgr.getStateObject("THE_TXN");
      populateTxn(returnTxn);
    }
    // check for customer coming from customer applet.  this happens
    // if the original txn does not have a customer assigned
    cust = (CMSCustomer)theAppMgr.getStateObject("CUSTOMER_FOUND");
    CMSCustomer returnCust = (CMSCustomer)returnTxn.getCustomer();
    //Khyati: Commented the line to not avoid getTelephone
    //      if ((returnCust == null || returnCust.getTelephone().equals(CMSCustomer.UNKNOWN_CUSTOMER_PHONE)) && cust != null) {
    if ((returnCust == null) && cust != null) {
      try {
        returnTxn.setCustomer(cust);
      } catch (BusinessRuleException ex) {
        theAppMgr.showErrorDlg(res.getString(ex.getMessage()));
      }
    }
    theAppMgr.showMenu(MenuConst.SELECT_ALLONLY, theOpr, theAppMgr.OK_CANCEL_BUTTON);
    populateScreen();
    theAppMgr.setSingleEditArea(res.getString("Select item(s) to return."));
  }

  /**
   * @return
   */
  private boolean isReturnOnly() {
    if (theAppMgr.getStateObject("TXN_POS") != null)
      return false;
    else
      return true;
  }

  /**
   * getSelectedItems
   */
  private SaleLineItem[] getSelectedLineItems() {
    SaleLineItem lines[] = null;
    Vector vTemp = new Vector();
    for (Enumeration enm = model.getAllRows().elements(); enm.hasMoreElements(); ) {
      ReturnSaleModel.ReturnItem returnItem = (ReturnSaleModel.ReturnItem)enm.nextElement();
      if (returnItem.isReturn.booleanValue()) {
        vTemp.addElement(returnItem.line);
      }
    }
    lines = new SaleLineItem[vTemp.size()];
    vTemp.copyInto(lines);
    return lines;
  }

  /**
   * Method that selects the items to return. Only the returnable items
   * will be displayed on the list.
   */
  private void returnLineItems(SaleLineItem[] lines) throws BusinessRuleException {
    if (lines == null) {
       return;
    }
    ReturnLineItem lineItem = null;
    // see bug 4764, 4765.  added this test before actually adding any lineitems.
    for (int x = 0; x < lines.length; x++) {
      lines[x].testIsReturnable();
    }
    for (int x = 0; x < lines.length; x++) {
      //Khyati: Add the selected line items to the ARM_RETURN_TXN_OBJECTS to disable reselection of previoulsy selected item in the same transaction
      String txnId = lines[x].getTransaction().getCompositeTransaction().getId();
      if (hReturnTxns != null) {
        if (hReturnTxns.containsKey(txnId)) {
          //            CMSCompositePOSTransaction returnTxn = (CMSCompositePOSTransaction)hReturnTxns.get(txnId);
          if (((CMSSaleLineItem)lines[x]).isSelectedForReturn) {
            continue;
          } else {
            ((CMSSaleLineItem)lines[x]).setIsSelectedForReturn(true);
            //lines[x].setAdditionalConsultant(theTxn.getConsultant());
            //              CMSReturnLineItem cmsReturnItem[] = (CMSSaleLineItem[])theTxn.getReturnLineItemsArray();
            //              for (int i = 0; i < cmsReturnItem.length; i++){
            //                if (cmsReturnItem
            //              }
          }
        }
      }
      //         if (selectAll)
      lineItem = theTxn.addEntireSaleLineItemAsReturn(lines[x]);
      //         else
      // returns qty 1 (can adjust quantity later in sale screen)
      //            lineItem = theTxn.addSaleLineItemAsReturn(lines[x]);
      lineItem.setAdditionalConsultant(lines[x].getAdditionalConsultant());
      //added for dolci
      	if("US".equalsIgnoreCase(Version.CURRENT_REGION)){
		String itemId = lineItem.getItem().getId();
		//this will check whether item is dolci or not
		boolean flag = isDolciCandy(itemId);
		if(flag){
			HashMap dolciSkuValue =(HashMap)theAppMgr.getGlobalObject("DOLCI_ITEM_PRESENT");
			String key = getKeyFromValue(dolciSkuValue,itemId);
			int index = key.indexOf(".");
			String keyIndex = key.substring(index+1);
			 String itemDesc = key.replace('.', '-');	
			lineItem.getItem().setDescription(itemDesc);	
		  }
		}
		//ended for dolci     
      //___Tim:1864
      if(lines[x].getExtendedBarCode() != null){
    	  lineItem.setExtendedBarCode(lines[x].getExtendedBarCode());
      }
	  //added by Anjana for Promotion changes
      if(returnTxn!=null){
      		try{
		String promCode = CMSTransactionPOSHelper.findPromCodeByTxnIdandBarcode(theAppMgr, returnTxn.getId(),lines[x].getExtendedBarCode());
		if(promCode!=null){
			lineItem.setPromotionCode(promCode);
			
			}}
			 catch (Exception e) {
				// TODO Auto-generated catch block
				  theAppMgr.showErrorDlg(res.getString("Error in loading the promotions for item"));
				
			}
		
      }
  	
		else{
			lineItem.setPromotionCode(null);
		}
      try {
        if (theAppMgr.getStateObject("RETURN_COMMENTS") != null)
          ((ReturnLineItem)lineItem).setComments(theAppMgr.getStateObject("RETURN_COMMENTS").
              toString());
        if (theAppMgr.getStateObject("RETURN_REASON") != null)
          ((ReturnLineItem)lineItem).setReasonId(theAppMgr.getStateObject("RETURN_REASON").toString());
      } catch (BusinessRuleException bre) {
        lineItem.deleteWithoutAuditTrail();
        throw bre;
      }
    }
  }

  /**
   * @param theTxn
   */
  private void populateTxn(CMSCompositePOSTransaction theTxn) {
    try {
      theTxn.setCustomer(returnTxn.getCustomer());
    } catch (BusinessRuleException ex) {
      theAppMgr.showErrorDlg(res.getString(ex.getMessage()));
    }
  }

  /**
   * @param anEvent
   */
  public void appButtonEvent(CMSActionEvent anEvent) {
    String sAction = anEvent.getActionCommand();
    if (sAction.equals("SELECT_ALL")) {
		for (Enumeration enu = model.getAllRows().elements(); enu.hasMoreElements();) {
			ReturnSaleModel.ReturnItem returnItem = (ReturnSaleModel.ReturnItem) enu.nextElement();
        // check for gift cert
        //Khyati: Added MiscItemManager to disable selection of Misc items
			if ((returnItem.line.getItem().isRedeemable())
				|| ("US".equalsIgnoreCase(Version.CURRENT_REGION)
					&&	MiscItemManager.getInstance().isMiscItem(returnItem.line.getItem().getId())
					&& !LineItemPOSUtil.isNotOnFileItem(returnItem.line.getItem().getId()))) { //make sure item is not not-on-file
				try { // Merge changes from Japan -  allow return of all misc Items except Deposit
					if(theTxn.validateIsReturnAllowed(returnItem.line)){
						returnItem.isReturn = new Boolean(false);
					}
				} catch (BusinessRuleException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
          // CR4220
          //               if (returnItem.line.getQuantityAvailableForReturn() != 0 && !isGiftCertRedeemed(returnItem.line))
          //                  returnItem.isReturn = new Boolean(true);
        } else if (returnItem.line.getQuantityAvailableForReturn() != 0) {
          returnItem.isReturn = new Boolean(true);
          returnItem.returnedAmount = returnItem.line.getQuantityAvailableForReturn();
        //Vivek Mishra : Added for sending the 150 AJB refresh request after every select item. 
          //sendItemMessageData(returnItem.line ,returnTxn.getLineItemsArray(), false, true);
          //End
        }
      }
      selectAll = true;
      theAppMgr.showMenu(MenuConst.DESELECT_ALLONLY, theOpr, theAppMgr.OK_CANCEL_BUTTON);
      tblItem.repaint();
    } else if (sAction.equals("DE_SELECT_ALL")) {
			for (Enumeration enu = model.getAllRows().elements(); enu.hasMoreElements();) {
				ReturnSaleModel.ReturnItem returnItem = (ReturnSaleModel.ReturnItem) enu.nextElement();
        returnItem.isReturn = new Boolean(false);
        returnItem.returnedAmount = 0;
      //Vivek Mishra : Added for sending the 150 AJB refresh request after every select item. 
     //   sendItemMessageData(returnItem.line ,null, false, true);
        //End
      }
      selectAll = false;
      theAppMgr.showMenu(MenuConst.SELECT_ALLONLY, theOpr, theAppMgr.OK_CANCEL_BUTTON);
      tblItem.repaint();
    } else if (sAction.equals("OK")) {
      SaleLineItem[] returnLines = getSelectedLineItems();
      //To refresh the item buckets  for  150 message on pinpad for return
      //Vivek Mishra : Commented the call as call is happening from InitalSaleApplet's updateLable method. 
      //sendItemMessageData(returnLines[0],returnLines,true,false,false);
      
     if (returnLines.length == 0) {
        theAppMgr.showErrorDlg(res.getString("You have not selected any line items to return."));
        ((CMSActionEvent)anEvent).consume();
        return;
      }
            // Check to make sure that all items are selected before proceeding
            // in case of return of txn that had premio payment
            Payment[] payments = returnTxn.getPaymentsArray();
            for (int i=0; i< payments.length; i++) {
                if (payments[i] instanceof CMSPremioDiscount) {
                    if (returnLines.length != model.getTotalRowCount()) {
                        theAppMgr.showErrorDlg(res.getString("This transaction contains Premio Discount payment. Must return in full."));
                        ((CMSActionEvent) anEvent).consume();
                        SwingUtilities.invokeLater(new Runnable() {
                            /**
                             * put your documentation comment here
                             */
                            public void run() {
                                appButtonEvent(new CMSActionEvent(this, 0, "SELECT_ALL", 0));
                            }
                          });
                        return;
                    }
                }
            }
            
      try {
        returnLineItems(returnLines);
        theAppMgr.addStateObject("TXN_MODE", new Integer(0));    
       } catch (BusinessRuleException ex) {
        theAppMgr.showErrorDlg(res.getString(ex.getMessage()));
        anEvent.consume();
        return;
      }
    } else if (sAction.equals("ONE")) {
      int row = tblItem.getSelectedRow();
      if (row < 0) {
        theAppMgr.showErrorDlg(res.getString("You must first select a line item."));
        return;
      }
      ReturnSaleModel.ReturnItem returnLine = model.getReturnItem(row);
      if (returnLine.line.getQuantityAvailableForReturn() == 0) {
        theAppMgr.showErrorDlg(res.getString("This line item is not available for return."));
        return;
      }
      // CR4220
      //         if (isGiftCertRedeemed(returnLine.line)) {
      if (returnLine.line.getItem().isRedeemable()) {
        theAppMgr.showErrorDlg(res.getString(
            "Redeemable items cannot be returned. Use the buy-back transaction."));
        return;
      }
      if (returnLine.isReturn.booleanValue()){
        returnLine.isReturn = new Boolean(false);
    //Vivek Mishra : Added for sending the 150 AJB refresh request after every select item. 
   //   sendItemMessageData(returnLine.line,returnTxn.getLineItemsArray(), false, true);
      //End
      }
      else{
        returnLine.isReturn = new Boolean(true);
      //Vivek Mishra : Added for sending the 150 AJB refresh request after every select item. 
    //    sendItemMessageData(returnLine.line,returnTxn.getLineItemsArray(), false, true);
        //End
      }
      tblItem.repaint();
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
   * @param e
   */
  void tblItem_componentResized(ComponentEvent e) {
    model.setRowsShown(tblItem.getHeight() / tblItem.getRowHeight());
    TableColumn returnCol = tblItem.getColumnModel().getColumn(model.RETURN);
    returnCol.setPreferredWidth(50);
    TableColumn ItemCol = tblItem.getColumnModel().getColumn(model.CODE);
    ItemCol.setPreferredWidth((int)(150 * r));
    TableColumn QtyCol = tblItem.getColumnModel().getColumn(model.RET);
    QtyCol.setPreferredWidth((int)(50 * r));
    TableColumn RetCol = tblItem.getColumnModel().getColumn(model.QTY);
    RetCol.setPreferredWidth((int)(50 * r));
    TableColumn UnitPriceCol = tblItem.getColumnModel().getColumn(model.PRICE);
    UnitPriceCol.setPreferredWidth((int)(105 * r));
    TableColumn MarkdownCol = tblItem.getColumnModel().getColumn(model.MARKDOWN);
    MarkdownCol.setPreferredWidth((int)(105 * r));
    TableColumn AmtDueCol = tblItem.getColumnModel().getColumn(model.AMT);
    AmtDueCol.setPreferredWidth((int)(105 * r));
    TableColumn DescriptionCol = tblItem.getColumnModel().getColumn(model.DESC);
    DescriptionCol.setPreferredWidth(tblItem.getWidth()
        - (returnCol.getPreferredWidth() + ItemCol.getPreferredWidth() + QtyCol.getPreferredWidth()
        + RetCol.getPreferredWidth() + UnitPriceCol.getPreferredWidth()
        + AmtDueCol.getPreferredWidth() + MarkdownCol.getPreferredWidth()));
  }

  /**
   * @param e
   */
  public void clickEvent(MouseEvent e) {
    int row = tblItem.getSelectedRow();
//    System.out.println("row " + row);
    if (row < 0){
      return;
    }
    //ItemListTable.getSelectionModel().addSelectionInterval(row, row);
    ReturnSaleModel.ReturnItem returnItem = model.getReturnItem(row);
    // Check to see if it is a Gift Cert
    // CR4220
    //      if (isGiftCertRedeemed(line.line)) {
    //Khyati: Added MiscItemManager to disable selection of Misc items
    //      if (line.line.getItem().isRedeemable()) {
    if ((returnItem.line.getItem().isRedeemable())) {
      theAppMgr.showErrorDlg(res.getString(
          "Redeemable items cannot be returned. Use the buy-back transaction."));
      return;
    }
   if ((MiscItemManager.getInstance().isMiscItem(returnItem.line.getItem().getId()))
        && !LineItemPOSUtil.isNotOnFileItem(returnItem.line.getItem().getId())) { //and if not not-on-file item.
    	try { // Merge changes from Japan -  allow return of all misc Items except Deposit
				if(!theTxn.validateIsReturnAllowed(returnItem.line)){
					theAppMgr.showErrorDlg(res.getString(
				  "Non merchandise items should be returned using the no receipt option."));
					return;
				}
			} catch (MissingResourceException e1) {
				e1.printStackTrace();
			} catch (BusinessRuleException e1) {
				e1.printStackTrace();
			}
   }
    
    if (returnItem.line.getQuantityAvailableForReturn() == 0){
      return;
    }
    if (returnItem.isReturn.booleanValue()) {
      returnItem.isReturn = new Boolean(false);
      returnItem.returnedAmount = 0;
    //Vivek Mishra : Added for sending the 150 AJB refresh request after every select item. 
   //   sendItemMessageData(returnItem.line ,returnTxn.getLineItemsArray(), false, true);
      //End
    } else {
      returnItem.isReturn = new Boolean(true);
      returnItem.returnedAmount = 1;
    //Vivek Mishra : Added for sending the 150 AJB refresh request after every select item. 
  //    sendItemMessageData(returnItem.line ,returnTxn.getLineItemsArray(), false, true);
      //End
    }
    model.fireTableDataChanged();
  }

  /**
   * @return
   */
  public boolean isHomeAllowed() {
    return theAppMgr.showOptionDlg(res.getString("Cancel Transaction")
        , res.getString("Selecting 'Home' cancels the transaction.  Do you want to cancel this transaction?"));
  }

  /***********************************************************************/
  private class CheckBoxRenderer extends JCheckBox implements TableCellRenderer {
    private Color DefaultBackground;
    private Color DefaultForeground;

    /**
     */
    public CheckBoxRenderer() {
      super();
      setHorizontalAlignment(SwingConstants.CENTER);
      setForeground(new Color(0, 0, 175));
      setBackground(Color.white);
      DefaultBackground = getBackground();
      DefaultForeground = getForeground();
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
		public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected,
				boolean hasFocus, int row, int column) {
      Boolean b = (Boolean)value;
      if (b != null){
        setSelected(b.booleanValue());
      }
      ReturnSaleModel itemModel = (ReturnSaleModel)table.getModel();
      ReturnSaleModel.ReturnItem returnLine = itemModel.getReturnItem(row);
      setBackground(DefaultBackground);
      // Check to see if it is a Gift Cert
      // CR4220
      //         if (isGiftCertRedeemed(returnLine.line)) {
      //ks: Added else if, if item has already been selected previously)
      if (returnLine.line.getItem().isRedeemable()) {
        this.setEnabled(false);
        return this;
      } else if (((CMSSaleLineItem)returnLine.line).isSelectedForReturn) {
        setForeground(Color.lightGray);
        this.setSelected(true);
        this.setEnabled(false);
        return this;
      } else{
        this.setEnabled(true);
      }
      // determine if checked to become a return when ok is hit
      if (returnLine.isReturn.booleanValue()) {
        setForeground(Color.red);
      } else {
        if (itemModel.isSale(row)) {
          setForeground(DefaultForeground);
          POSLineItem line = itemModel.getLineItem(row);
          if (false) { //line.getQuantityAvailToReturn() == 0) {
            setForeground(Color.lightGray);
          }
        } else if (itemModel.isLayaway(row)) {
          setForeground(new Color(0, 132, 121));
        } else {
          setForeground(Color.red);
        }
      }
      return this;
    }
  }


  /***********************************************************************/
  private class TextRenderer extends DefaultTableCellRenderer {

    /**
     */
    public TextRenderer() {
      super();
      setFont(theAppMgr.getTheme().getTextFieldFont());
      //setForeground(new Color(0, 0, 175));
    }

    /**
     * @param table
     * @param value
     * @param isSelected
     * @param hasFocus
     * @param row
     * @param col
     * @return
     */
		public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected,
				boolean hasFocus, int row, int col) {
      super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, col);
      if (value != null)
        setText(value.toString());
      else{
        setText("");
      }
      // determine if line item is return
      switch (col) {
        case 1:
        case 2: {
          setHorizontalAlignment(JLabel.LEFT);
          break;
        }
        case 3:
        case 4: {
          setHorizontalAlignment(JLabel.CENTER);
          break;
        }
        case 5:
        case 6:
        case 7: {
          setHorizontalAlignment(JLabel.RIGHT);
          break;
        }
      }
      ReturnSaleModel itemModel = (ReturnSaleModel)table.getModel();
      ReturnSaleModel.ReturnItem returnLine = itemModel.getReturnItem(row);
      // determine if checked to become a return when ok is hit
      if (returnLine.isReturn.booleanValue()) {
        setForeground(Color.red);
      } else {
        // determine if line item is return
        if (itemModel.isSale(row)) {
          setForeground(isSelected ? Color.white : tblItem.getForeground());
          POSLineItem line = model.getLineItem(row);
          if (false) { //line.getQuantityAvailToReturn() == 0) {
            setForeground(Color.lightGray);
          }
        } else if (itemModel.isLayaway(row))
          setForeground(new Color(0, 132, 121));
        else{
          setForeground(Color.red);
        }
      }
      return this;
    }
  }
  /**
   * This is method is add the skues of dolci in HashMap and also set in Golbalobject
   * @return boolean
   * @param ItemID
   * 
   * */
  private boolean isDolciCandy(String itemID){
      java.util.Map dolciSkuValue =(java.util.Map)theAppMgr.getGlobalObject("DOLCI_ITEM_PRESENT");
  	  java.util.List dolciSkuList = null;
  	  String  dolciValue = null;
  	  if(dolciSkuValue == null) {
            dolciSkuList = new ArrayList();
            dolciSkuValue = new HashMap();
            ConfigMgr mgr = new ConfigMgr("item.cfg");
        	  String dolciKeys = mgr.getString("DOLCI_CANDY_KEYS");
            StringTokenizer strTok = new StringTokenizer(dolciKeys, ",");
  	          while(strTok.hasMoreTokens()) {
  	                dolciSkuList.add(strTok.nextToken());
  	          }
            int length = dolciSkuList.size();
            if(length!=0){
            	for(int i=0;i<length;i++){
            		String key = dolciSkuList.get(i).toString();
            		String boxesKeys = mgr.getString(key+"_BOXES_KEYS");
            		if(boxesKeys!=null){
            		     StringTokenizer strToken = new StringTokenizer(boxesKeys, ",");
  	              	   while(strToken.hasMoreTokens()) {
  	              		String token = strToken.nextToken();
  	              		String newKey = key+"."+token;
  	              		dolciValue = mgr.getString(newKey);
  	              		dolciValue = dolciValue.trim();
  	              		//this is added all skues in hashmap.  	              	
  	              		dolciSkuValue.put(newKey,dolciValue);    
  	                }
            		}
            	 }
            }
            theAppMgr.addGlobalObject("DOLCI_ITEM_PRESENT",dolciSkuValue );       
           }
  	  return dolciSkuValue.containsValue(itemID);
  }
  /**
   * This is method which retrived the key from value in Dolci Candy.
   * HashMap is set in Application for Dolci candy 
   * Value is ItemId from EditArea Event.
   * @author vivek sawant
   * */
  	 public String getKeyFromValue(HashMap hashmap,String value){
  		Set ref = hashmap.keySet();
  		Iterator it = ref.iterator();
  		
  		while (it.hasNext()) {
  			 String key = (String)it.next();
  			 String values =(String)hashmap.get(key);
  			  if(values.equals(value)) {
  		        return key;
  		    }
  		  }
  		 return null;
  	 }
  	 
  	//Vivek Mishra : Added to send AJB 150 massage for each item.  
  	//Vivek Mishra : Added to send AJB 150 massage for each item.  
 	private boolean sendItemMessageData(POSLineItem line ,POSLineItem[] lineItemArray, boolean Refresh,boolean idleMessage,boolean clearMessage,boolean fromPayment ) {
 		try {
 			String result = "";
 			//Vivek Mishra : Added for not showing AJB servers down dialogue multiple times.
 			//int qty = theTxn.getLineItemsArray().length;
 			//End
 			String responseArray[] = null;
 			String ajbResponse[] = null;
 			Register register = (Register) theAppMgr.getGlobalObject("REGISTER");
 			
 				CMSCompositePOSTransaction txn = new CMSCompositePOSTransaction((CMSStore)theAppMgr.getGlobalObject("STORE"));
 				//Changes for Canada validate method needs to pass
 				String storeCountry = ((CMSStore)theAppMgr.getGlobalObject("STORE")).getCountry();
 				if(!storeCountry.equals("CAN")){
 					//String tt = orgTxn.getTransactionType();
 					responseArray = CMSItemHelper.validate(theAppMgr, txn, register.getId(),line,lineItemArray,Refresh,false,false,"",fromPayment);
 					if(responseArray!=null){
 						ajbResponse = responseArray;
 						//End
 					int length = responseArray.length;
 					for (int i=0; i<length ;i++){	
 						if(responseArray[i]!=null)
 					if(responseArray[i] != null && responseArray[i].toString().contains("All the Ajb Servers")){
 						//Vivek Mishra : Added for not showing AJB servers down dialogue multiple times.
 							count++;
 						if(count==lineCount)
 						{
 						theAppMgr.showErrorDlg("All the AJB servers are down");
 						count=0;
 						}//End
 						return false;
 					}
 					}
 					}
 				}else
 				
 				if (responseArray == null) {
 					return true;
 				}
 				
 			count=0;	
 			return (true);
 		} catch (Exception ex) {
 			ex.printStackTrace();
 			theAppMgr.showExceptionDlg(ex);
 			return (false);
 		}
 	
   }
   //End
}

