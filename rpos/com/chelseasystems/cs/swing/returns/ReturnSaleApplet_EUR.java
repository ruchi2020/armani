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
import java.util.Vector;
import java.util.Enumeration;

import javax.swing.*;
import javax.swing.table.*;

import com.chelseasystems.cr.swing.CMSApplet;
import com.chelseasystems.cs.swing.menu.MenuConst;
import com.chelseasystems.cs.customer.CMSCustomer;
import com.chelseasystems.cs.employee.CMSEmployee;
import com.chelseasystems.cr.swing.bean.JCMSTable;
import com.chelseasystems.cr.swing.event.CMSActionEvent;
import com.chelseasystems.cr.pos.*;
import com.chelseasystems.cs.pos.ASISTxnData;
import com.chelseasystems.cs.pos.CMSCompositePOSTransaction;
import com.chelseasystems.cs.pos.CMSTransactionPOSHelper;
import com.chelseasystems.cs.payment.CMSRedeemableHelper;
import com.chelseasystems.cr.payment.GiftCert;
import com.chelseasystems.cr.payment.Redeemable;
import com.chelseasystems.cr.rules.BusinessRuleException;
import com.chelseasystems.cs.swing.model.ReturnSaleModel_EUR;
import com.chelseasystems.cs.swing.panel.TxnHeaderPanel;
import com.chelseasystems.cs.swing.CMSAppModelFactory;
import com.chelseasystems.cr.pos.*;
import com.chelseasystems.cr.item.*;
import com.chelseasystems.cr.swing.MultiLineCellRenderer;

import java.util.Hashtable;

import com.chelseasystems.cs.pos.CMSSaleLineItem;
import com.chelseasystems.cs.swing.bean.ArmJCMSTable;
import com.chelseasystems.cr.config.ConfigMgr;
import com.chelseasystems.cs.util.LineItemPOSUtil;
import com.ga.fs.fsbridge.ARMFSBridge;



/**
 */
public class ReturnSaleApplet_EUR extends CMSApplet {
  //public class ReturnSaleApplet extends JApplet {

  /** the building txn (case return with other purchase) */
  private CMSCompositePOSTransaction theTxn;

  /** the returning txn */
  private CMSCompositePOSTransaction returnTxn;
  private CMSCustomer cust;
  //private boolean selectAll = false;
  ReturnSaleModel_EUR model;
  JCMSTable tblItem;
  TxnHeaderPanel pnlHeader;
  ASISTxnData  asisData;
  public static Hashtable hReturnTxns = null;
  //private int lastSelectedItemsForExchange = 0;
  //private int lastSelectedItemsForReturn = 0;

  //Initialize the applet
  public void init() {
    model = new ReturnSaleModel_EUR();
    tblItem = new ArmJCMSTable(model, JCMSTable.SELECT_ROW);
    pnlHeader = new TxnHeaderPanel(true);
    asisData = new ASISTxnData();
    try {
      jbInit();
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  //Component initialization
  private void jbInit()
      throws Exception {
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
    modelColumn.getColumn(3).setCellRenderer(new ReturnMultiLineCellRenderer());
  }

  //Stop the applet
  public void stop() {}

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
  }

  //Start the applet
  public void start() {
    theAppMgr.setTransitionColor(theAppMgr.getBackgroundColor());
    theOpr = (CMSEmployee)theAppMgr.getStateObject("OPERATOR");
    model.clear();
    //lastSelectedItemsForExchange = 0;
    //lastSelectedItemsForReturn = 0;
    //selectAll = false;
    //ks: Get the Hashtable of all return txn
    hReturnTxns = (Hashtable)theAppMgr.getStateObject("ARM_RETURN_TXN_OBJECTS");
    //theAppMgr.removeStateObject("ARM_RETURN_TXN_OBJECTS");
    //check if there exists a building SaleTxn
    theTxn = (CMSCompositePOSTransaction)theAppMgr.getStateObject("TXN_POS");
   
    //now check if there exists an exact TxnPOS needed returning
    returnTxn = (CMSCompositePOSTransaction)theAppMgr.getStateObject("RETURN_TXN_POS");
    
   
    /* try {
	
    	//asisData.setTxnNo("FT 201485401/"+returnTxn.getFiscalReceiptNumber());
	
    } catch (BusinessRuleException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	} 
    
    try {
		theTxn.setASISTxnData(asisData);
		returnTxn.setASISTxnData(asisData);
		theTxn.setApprover("FT 201485401/"+returnTxn.getFiscalReceiptNumber());
		returnTxn.setApprover("FT 201485401/"+returnTxn.getFiscalReceiptNumber());
	} catch (BusinessRuleException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}*/
    
    
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
    theAppMgr.showMenu(MenuConst.RET_EUROPE, theOpr, theAppMgr.OK_CANCEL_BUTTON);
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

  // Per CR4220 and discussion w/ Ruben, Gift Certs are no longer returnable at all.
  //   /**
   //    * Check to make sure that gift cert has not already been redeemed
   //    */
  //   private boolean isGiftCertRedeemed (POSLineItem line) {
  //     try {
  //       if (!line.getItem().isRedeemable())
  //         return false;
  //
  //       for (Enumeration enm = line.getLineItemDetails() ; enm.hasMoreElements() ;) {
  //         String controlNumber = ((POSLineItemDetail)enm.nextElement()).getGiftCertificateId();
  //         if (controlNumber != null && controlNumber.trim().length() > 0) {
  //           Redeemable redeemable = CMSRedeemableHelper.findRedeemable(theAppMgr, controlNumber);
  //           if (redeemable.isRedeemed()) {
  //             return true;
  //           }
  //         }
  //       }
  //     }
  //     catch (Exception ex) {
  //       theAppMgr.showExceptionDlg(ex);
  //       return true;
  //     }
  //     return false;
  //   }
  /**
   * getSelectedItems
   */
  private ReturnSaleModel_EUR.ReturnItem[] getSelectedReturnItems() {
    ReturnSaleModel_EUR.ReturnItem[] items = null;
    Vector vTemp = new Vector();
    for (Enumeration enm = model.getAllRows().elements(); enm.hasMoreElements(); ) {
      ReturnSaleModel_EUR.ReturnItem returnItem = (ReturnSaleModel_EUR.ReturnItem)enm.nextElement();
      if (returnItem.isReturn.booleanValue()) {
        vTemp.addElement(returnItem);
      }
    }
    items = new ReturnSaleModel_EUR.ReturnItem[vTemp.size()];
    vTemp.copyInto(items);
    return items;
  }

  /**
   * Method that selects the items to return. Only the returnable items
   * will be displayed on the list.
   */
  private void returnLineItems(ReturnSaleModel_EUR.ReturnItem[] items)
      throws BusinessRuleException {
    if (items == null) {
      System.out.println("ReturnSaleApplet.returnLineItems->items == null");
      return;
    }
    ReturnLineItem lineItem = null;
    CMSSaleLineItem saleLineItem = null;
    for (int x = 0; x < items.length; x++) {
      items[x].line.testIsReturnable();
    }
    for (int x = 0; x < items.length; x++) {
      saleLineItem = (CMSSaleLineItem)items[x].line;
      String txnId = saleLineItem.getTransaction().getCompositeTransaction().getId();
      if (hReturnTxns != null) {
        if (hReturnTxns.containsKey(txnId)) {
          if (saleLineItem.isSelectedForReturn) {
            continue;
          } else {
            saleLineItem.setIsSelectedForReturn(true);
          }
        }
      }
      if (items[x].isForExchange)
        lineItem = theTxn.addEntireSaleLineItemAsExchange(saleLineItem);
      else
        lineItem = theTxn.addEntireSaleLineItemAsReturn(saleLineItem);
      lineItem.setAdditionalConsultant(saleLineItem.getAdditionalConsultant());
      //___Tim:1864
      if(saleLineItem.getExtendedBarCode() != null){
    	  lineItem.setExtendedBarCode(saleLineItem.getExtendedBarCode());
      }
      //Vivek Mishra : Added to save promotion code while return transaction 19-JUL-2016
      if(returnTxn!=null)
	  {
    try{
		String promCode = CMSTransactionPOSHelper.findPromCodeByTxnIdandBarcode(theAppMgr, returnTxn.getId(),saleLineItem.getExtendedBarCode());
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
      //Ends here
      
      
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
      Vector returnItemVec = model.getAllRows();
      for (int i = 0; i < returnItemVec.size(); i++) {
        ReturnSaleModel_EUR.ReturnItem returnItem = (ReturnSaleModel_EUR.ReturnItem)returnItemVec.
            elementAt(i);
        try {
          checkIfValidForReturn(returnItem);
          returnItem.isSelected = true;
          returnItem.returnedAmount = returnItem.line.getQuantityAvailableForReturn();
        } catch (BusinessRuleException ex) {
          //theAppMgr.showErrorDlg(ex.getMessage());
        }
      }
      //selectAll = true;
      theAppMgr.showMenu(MenuConst.RET_DESELECT_ALL_EUROPE, theOpr, theAppMgr.OK_CANCEL_BUTTON);
      tblItem.repaint();
    } else if (sAction.equals("DE_SELECT_ALL")) {
      Vector returnItemVec = model.getAllRows();
      for (int i = 0; i < returnItemVec.size(); i++) {
        ReturnSaleModel_EUR.ReturnItem returnItem = (ReturnSaleModel_EUR.ReturnItem)returnItemVec.
            elementAt(i);
        returnItem.isSelected = false;
        returnItem.returnedAmount = 0;
      }
      //selectAll = false;
      theAppMgr.showMenu(MenuConst.RET_EUROPE, theOpr, theAppMgr.OK_CANCEL_BUTTON);
      tblItem.repaint();
    } else if (sAction.equals("OK")) {
      ReturnSaleModel_EUR.ReturnItem[] returnItems = getSelectedReturnItems();
      if (returnItems.length == 0) {
        theAppMgr.showErrorDlg(res.getString("You have not selected any line items to return."));
        ((CMSActionEvent)anEvent).consume();
        return;
      }
      try {
        returnLineItems(returnItems);
        // Set the Mode to Sale after Return Sale Applet
        theAppMgr.addStateObject("TXN_MODE", new Integer(0));
      } catch (BusinessRuleException ex) {
        theAppMgr.showErrorDlg(res.getString(ex.getMessage()));
        anEvent.consume();
        return;
      }
    } else if (sAction.equals("RETURN") || sAction.equals("EXCHANGE")) {
      //int row = tblItem.getSelectedRow();
      Vector rows = model.getAllRows();
      int selectedItems = 0;
      int selectedItemsForReturn = 0;
      int selectedItemsForExchange = 0;
      for (int i = 0; i < rows.size(); i++) {
        ReturnSaleModel_EUR.ReturnItem returnLine = (ReturnSaleModel_EUR.ReturnItem)rows.elementAt(
            i);
        if (!returnLine.isSelected)
          continue;
        selectedItems++;
        returnLine.isSelected = false;
        try {
          checkIfValidForReturn(returnLine);
        } catch (BusinessRuleException ex) {
          if (ex.getMessage().trim().length() > 0)
            theAppMgr.showErrorDlg(ex.getMessage());
          continue;
        }
        if (sAction.equals("RETURN")) {
          if (returnLine.isReturn.booleanValue() && !returnLine.isForExchange) {
            //&& (!selectAll || lastSelectedItemsForReturn == rows.size())) {
            returnLine.isReturn = new Boolean(false);
          } else {
            returnLine.isReturn = new Boolean(true);
            returnLine.isForExchange = false;
            selectedItemsForReturn++;
          }
        } else { //if(sAction.equals("EXCHANGE"))
          if (returnLine.isReturn.booleanValue() && returnLine.isForExchange) {
            //&& (!selectAll || lastSelectedItemsForExchange == rows.size())) {
            returnLine.isReturn = new Boolean(false);
          } else {
            returnLine.isReturn = new Boolean(true);
            returnLine.isForExchange = true;
            selectedItemsForExchange++;
          }
        }
      }
      theAppMgr.showMenu(MenuConst.RET_EUROPE, theOpr, theAppMgr.OK_CANCEL_BUTTON);
      //lastSelectedItemsForReturn = selectedItemsForReturn;
      //lastSelectedItemsForExchange = selectedItemsForExchange;
      if (selectedItems <= 0) {
        theAppMgr.showErrorDlg(res.getString("You must first select a line item."));
        return;
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
   * 
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
    TableColumn selectCol = tblItem.getColumnModel().getColumn(model.SELECT);
    selectCol.setPreferredWidth(50);
    TableColumn reCol = tblItem.getColumnModel().getColumn(model.RE);
    reCol.setPreferredWidth(30);
    TableColumn ItemCol = tblItem.getColumnModel().getColumn(model.CODE);
    ItemCol.setPreferredWidth((int)(140 * r));
    TableColumn assocCol = tblItem.getColumnModel().getColumn(model.ASSOCIATE);
    assocCol.setPreferredWidth(60);
    TableColumn QtyCol = tblItem.getColumnModel().getColumn(model.RET);
    QtyCol.setPreferredWidth((int)(40 * r));
    TableColumn RetCol = tblItem.getColumnModel().getColumn(model.QTY);
    RetCol.setPreferredWidth((int)(40 * r));
    TableColumn UnitPriceCol = tblItem.getColumnModel().getColumn(model.PRICE);
    UnitPriceCol.setPreferredWidth((int)(105 * r));
    TableColumn MarkdownCol = tblItem.getColumnModel().getColumn(model.MARKDOWN);
    MarkdownCol.setPreferredWidth((int)(105 * r));
    TableColumn AmtDueCol = tblItem.getColumnModel().getColumn(model.AMT);
    AmtDueCol.setPreferredWidth((int)(105 * r));
    TableColumn DescriptionCol = tblItem.getColumnModel().getColumn(model.DESC);
    DescriptionCol.setPreferredWidth(tblItem.getWidth()
        - (selectCol.getPreferredWidth() + reCol.getPreferredWidth() + ItemCol.getPreferredWidth()
        + assocCol.getPreferredWidth() + QtyCol.getPreferredWidth()
        + RetCol.getPreferredWidth() + UnitPriceCol.getPreferredWidth()
        + AmtDueCol.getPreferredWidth() + MarkdownCol.getPreferredWidth()));
  }

  /**
   * @param e
   */
  public void clickEvent(MouseEvent e) {
    int row = tblItem.getSelectedRow();
    System.out.println("row " + row);
    if (row < 0)
      return;
    ReturnSaleModel_EUR.ReturnItem line = model.getReturnItem(row);
    try {
      checkIfValidForReturn(line);
    } catch (BusinessRuleException ex) {
      if (ex.getMessage().trim().length() > 0)
        theAppMgr.showErrorDlg(ex.getMessage());
      else
        return;
    }
    line.isSelected = !line.isSelected;
    model.fireTableDataChanged();
  }

  /**
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
  private void checkIfValidForReturn(ReturnSaleModel_EUR.ReturnItem line)
      throws BusinessRuleException {
    if ((line.line.getItem().isRedeemable())) {
      throw new BusinessRuleException(res.getString(
          "Redeemable items cannot be returned. Use the buy-back transaction."));
    }
    if ((MiscItemManager.getInstance().isMiscItem(line.line.getItem().getId()))
        && !LineItemPOSUtil.isNotOnFileItem(line.line.getItem().getId())) {
    	//Sergio	
    	if(isGiftCard(line.line.getItem().getId())) /* The gift is permitted*/	
        	throw new BusinessRuleException(res.getString(
        	"Non merchandise item should be returned using the no receipt option."));
    }
    if (line.line.getQuantityAvailableForReturn() == 0)
      throw new BusinessRuleException("");
  }
  
  private boolean isGiftCard(String itemId) {
      ConfigMgr itmCfg = new ConfigMgr("item.cfg");
      String giftId = itmCfg.getString("GIFT.BASE_ITEM");
	  //Vivek Mishra : Merged updated code from source provided by Sergio 19-MAY-16
	  String merchItem = itmCfg.getString("ALLOW_MERCHANDISE_ITEM");
      if ((itemId.compareTo(giftId) != 0)&&(merchItem == "FALSE"))//Ends here
	    return true;
	  return false;
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
    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected
        , boolean hasFocus, int row, int column) {
      Boolean b = (Boolean)value;
      if (b != null)
        setSelected(b.booleanValue());
      ReturnSaleModel_EUR itemModel = (ReturnSaleModel_EUR)table.getModel();
      ReturnSaleModel_EUR.ReturnItem returnLine = itemModel.getReturnItem(row);
      setBackground(DefaultBackground);
      // Check to see if it is a Gift Cert
      // CR4220
      //         if (isGiftCertRedeemed(returnLine.line)) {
      //ks: Added else if, if item has already been selected previously)
//      if (returnLine.line.getItem().isRedeemable()) {
//        this.setEnabled(false);
//        return this;
//      } else if (((CMSSaleLineItem)returnLine.line).isSelectedForReturn) {
//        setForeground(Color.lightGray);
//        this.setSelected(true);
//        this.setEnabled(false);
//        return this;
//      } else
//        this.setEnabled(true);
      this.setSelected(returnLine.isSelected);

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
    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected
        , boolean hasFocus, int row, int col) {
      super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, col);
      if (value != null)
        setText(value.toString());
      else
        setText("");
      // determine if line item is return
      switch (col) {
        case 1:
        case 2: {
          setHorizontalAlignment(JLabel.CENTER);
          break;
        }
        case 3: {
          setHorizontalAlignment(JLabel.LEFT);
          break;
        }
        case 4:
        case 5:
        case 6: {
          setHorizontalAlignment(JLabel.CENTER);
          break;
        }
        case 7:
        case 8:
        case 9: {
          setHorizontalAlignment(JLabel.RIGHT);
          break;
        }
      }
      ReturnSaleModel_EUR itemModel = (ReturnSaleModel_EUR)table.getModel();
      ReturnSaleModel_EUR.ReturnItem returnLine = itemModel.getReturnItem(row);
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
        else
          setForeground(Color.red);
      }
      return this;
    }
  }


  /***********************************************************************/
  private class ReturnMultiLineCellRenderer extends MultiLineCellRenderer {

    /**
     */
    public ReturnMultiLineCellRenderer() {
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
    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected
        , boolean hasFocus, int row, int col) {
      super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, col);
      if (value != null)
        setText(value.toString());
      else
        setText("");
      //setHorizontalAlignment(JLabel.LEFT);
      ReturnSaleModel_EUR itemModel = (ReturnSaleModel_EUR)table.getModel();
      ReturnSaleModel_EUR.ReturnItem returnLine = itemModel.getReturnItem(row);
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
        else
          setForeground(Color.red);
      }
      return this;
    }
  }
}

