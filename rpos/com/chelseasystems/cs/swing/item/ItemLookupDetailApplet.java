/*
 History:
 +------+------------+-----------+-----------+----------------------------------------------------+
 | Ver# | Date       | By        | Defect #  | Description                                        |
 +------+------------+-----------+-----------+----------------------------------------------------+
 | 5    | 07/06/2005 | Vikram    | 353       | Item locator - Edit area prompt is incorrect       |
 +------+------------+-----------+-----------+----------------------------------------------------+
 | 4    | 05/25/2005 | Vikram    | N/A       | Updated for POS_104665_FS_ItemLookup_Rev1          |
 +------+------------+-----------+-----------+----------------------------------------------------+
 | 1    | 04/27/2005 | Vikram    | N/A       | POS_104665_FS_ItemLookup_Rev1                      |
 --------------------------------------------------------------------------------------------------
 *
 * formatted with JxBeauty (c) johann.langhofer@nextra.at
 */


package com.chelseasystems.cs.swing.item;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ComponentEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.StringReader;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.StringTokenizer;

import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;

import com.chelseasystems.cr.appmgr.IApplicationManager;
import com.chelseasystems.cr.config.ConfigMgr;
import com.chelseasystems.cr.config.FileMgr;
import com.chelseasystems.cr.item.MiscItemManager;
import com.chelseasystems.cr.item.MiscItemTemplate;
import com.chelseasystems.cr.pos.POSLineItem;
import com.chelseasystems.cr.rules.BusinessRuleException;
import com.chelseasystems.cr.swing.CMSApplet;
import com.chelseasystems.cr.swing.bean.JCMSLabel;
import com.chelseasystems.cr.swing.bean.JCMSTextField;
import com.chelseasystems.cr.swing.event.CMSActionEvent;
import com.chelseasystems.cr.swing.report.Reportable;
import com.chelseasystems.cr.util.INIFile;
import com.chelseasystems.cr.util.INIFileException;
import com.chelseasystems.cs.discount.CMSEmployeeDiscount;
import com.chelseasystems.cs.employee.CMSEmployee;
import com.chelseasystems.cs.employee.CMSEmployeeHelper;
import com.chelseasystems.cs.item.CMSItem;
import com.chelseasystems.cs.item.CMSItemHelper;
import com.chelseasystems.cs.item.ItemSearchString;
import com.chelseasystems.cs.pos.CMSCompositePOSTransaction;
import com.chelseasystems.cs.store.CMSStore;
import com.chelseasystems.cs.swing.dlg.GenericChooseFromTableDlg;
import com.chelseasystems.cs.swing.dlg.GenericChooserRow;
import com.chelseasystems.cs.swing.menu.MenuConst;
import com.chelseasystems.cs.swing.model.ItemLookupDetailModel;
import com.chelseasystems.cs.swing.panel.ItemLookupDetailPanel;
import com.chelseasystems.cs.swing.report.ReceiptLogViewerApplet;
import com.chelseasystems.cs.util.Version;
import com.ga.cs.swing.model.BaseReportModel;
import com.ga.cs.swing.report.ArmaniReportPrinter;
import com.ga.cs.swing.report.BaseReportApplet;
import com.ga.fs.fsbridge.ARMFSBridge;
import com.klg.jclass.page.EndOfFrameException;
import com.klg.jclass.page.JCFrame;
import com.klg.jclass.page.JCPage;
import com.klg.jclass.page.JCTextStyle;



/**
 * <p>Title:ItemLookupDetailApplet </p>
 * <p>Description: Searches displays items provided search criteria in state object</p>
 * <p>Copyright: Copyright (c) 2005</p>
 * <p>Company: SkillnetInc</p>
 * @author Vikram Mundhra
 * @version 1.0
 */
public class ItemLookupDetailApplet extends CMSApplet{
  private ItemLookupDetailPanel pnlItemLookupDetail;
  private ItemLookupDetailModel itemLookupDetailModel;
  private JScrollPane scrollPane;
  private JPanel pnlSearch;
  private JCMSLabel lblStyle;
  private JCMSTextField txtStyle;
  private JCMSLabel lblSupplier;
  private JCMSTextField txtSupplier;
  private JCMSLabel lblModel;
  private JCMSTextField txtModel;
  private JCMSLabel lblProduct;
  private JCMSTextField txtProduct;
  private JCMSLabel lblFabric;
  private JCMSTextField txtFabric;
  private JCMSLabel lblColor;
  private JCMSTextField txtColor;
  private JCMSLabel lblYear;
  private JCMSTextField txtYear;
  private JCMSLabel lblSeason;
  private JCMSTextField txtSeason;
  private CMSItem cmsItem;
  private ItemSearchString itemSearchString;
  private CMSItem[] cmsItems;
  private String homeStoreId;
  private boolean previousFlag = false;
  public static final int SALE_MODE = 0;
  public static final int RETURN_MODE = 1;
  public static final int LAYAWAY_MODE = 2;
  public static final int EMPLOYEE_SALE = 3;
  //mb: added for pre-sale
  public static final int PRE_SALE_OPEN = 4;
  //ks: Added for Consignment
  public static final int CONSIGNMENT_OPEN = 6;
  public static final int NO_SALE_MODE = 8;
  public static final int NO_RETURN_MODE = 13;
  public static final int RESERVATIONS_OPEN = 9;
  public static final int NO_OPEN_RESERVATIONS_CLOSE_SALE = 11;
  public static final int NO_OPEN_RESERVATIONS_CLOSE_RETURN = 12;
  private int iTxnMode = SALE_MODE;
  private boolean showItemAvailableQty = true;
  private static boolean searchItemInMultipleStore = false;
  private boolean needNextColor = false;
  private boolean needPrevColor = false; 
  
  //added for stock locator by neeti
  CMSEmployee emp;
  
  
  /**
   *
   */
  public void stop() {
    // reset();
    if (!previousFlag) {
      theAppMgr.removeStateObject("ITEM_LOOOKUP_SEARCHSTRING");
      theAppMgr.removeStateObject("ITEM_LOOOKUP_SEARCHRESULT");
      theAppMgr.removeStateObject("ITEM_LOOOKUP_SELECTEDSKU");
      theAppMgr.removeStateObject("ITEM_LOOOKUP_SORTCOLUMN");
      //theAppMgr.removeStateObject("COLOR_VECTOR");
    }
  }

  /**
   *
   */
  public void init() {
    try {
        // TD 
        ConfigMgr configMgr = new ConfigMgr("item.cfg");
    	String itemSearchStatus = configMgr.getString("ITEM_SEARCH_IN_MULTIPLE_STORE");
    	if( itemSearchStatus != null && itemSearchStatus.trim().equalsIgnoreCase("true") ){
    		searchItemInMultipleStore = true;
    	}else{
    		searchItemInMultipleStore = false;
    	}
        showItemAvailableQty = true;
   	
      cmsItems = new CMSItem[0];
      CMSStore store = (CMSStore)theAppMgr.getGlobalObject("STORE");
      if (store != null)
        homeStoreId = store.getId();
      jbInit();
      pnlItemLookupDetail.addMouseListener(new MouseAdapter() {

        /**
         * put your documentation comment here
         * @param e
         */
        public void mouseClicked(MouseEvent e) {
          if (e.getClickCount() < 2)
            return;
          cmsItem = pnlItemLookupDetail.getSelectedItem();
          if (cmsItem == null && theAppMgr.getStateObject("SELECTED_ITEM") == null)
            itemSelected(cmsItem);
          else {
            if (theAppMgr.getStateObject("SELECTED_ITEM") != null) {
              theAppMgr.addStateObject("SELECTED_ITEM", cmsItem.getId());
              theAppMgr.fireButtonEvent("LOOOKUP");
            }
            if (cmsItem != null) {
              itemSelected(cmsItem);
            }
          }
        }
      });
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  /**
   *
   * @param cmsItem CMSItem
   */
  public void itemSelected(CMSItem cmsItem) {
    if (cmsItem == null)
      return;
    if (theAppMgr.getStateObject("OPERATOR") == null
        || ((CMSEmployee)theAppMgr.getStateObject("OPERATOR")).getExternalID().length() < 1)
      return;
    theAppMgr.addStateObject("ITEM_LOCATE", cmsItem);
    theAppMgr.fireButtonEvent("LOAD_MGMT");
  }

  /**
   *
   */
  public void start() {
    cmsItems = new CMSItem[0];
    previousFlag = false;
    //firstRunCompleted = false;
    theOpr = (CMSEmployee) theAppMgr.getStateObject("OPERATOR");
    try {
      if (theAppMgr.getStateObject("ITEM_LOCATE") != null) {
        theAppMgr.removeStateObject("ITEM_LOCATE");
      } else {
        reset();
      }
      
      itemSearchString = (ItemSearchString) theAppMgr.getStateObject("ITEM_LOCATE_SEARCHSTRING");
      cmsItems = this.processQuery(itemSearchString);
      if (cmsItems == null) {
        doPrevious();
        theAppMgr.removeStateObject("ITEM_LOCATE_SEARCHSTRING");
        return;
      }
      this.getContentPane().remove(pnlItemLookupDetail);
      //pnlItemLookupDetail = new ItemLookupDetailPanel(theAppMgr, cmsItems,itemSearchString.getColor());
      pnlItemLookupDetail = getItemLookupDetailPanel(theAppMgr, cmsItems,itemSearchString.getColor());
      scrollPane.setViewportView(pnlItemLookupDetail);
      
      enter();
     
     
     //TD
      if(searchItemInMultipleStore) {
          showItemAvailableQty = true;
          boolean bNextColorAvailable = pnlItemLookupDetail.getAddressModel().hasNextColor();
          if (theAppMgr.getStateObject("OPERATOR") == null) {
        	  if(bNextColorAvailable) {
        	  	if(showItemAvailableQty){
        	  		theAppMgr.showMenu(MenuConst.NEXT_COLOR_AND_PREV_AND_ITEM_UNAVAILABLE, theOpr);
        	  	}else{
        	  		theAppMgr.showMenu(MenuConst.NEXT_COLOR_AND_PREV_AND_ITEM_AVAILABLE, theOpr);
        	  	}
        	  } else {
        		  //___Tim: 1709:Update menus to add "toggle" button. 
        		  // TOGGLE button also added to all the LOCATE_* menus in menu.xml
        		  if(showItemAvailableQty){
        			  theAppMgr.showMenu(MenuConst.LOCATE_WITH_PREV_AND_ITEM_UNAVAILABLE, theOpr);
        		  }else{
        			  theAppMgr.showMenu(MenuConst.LOCATE_WITH_PREV_AND_ITEM_AVAILABLE, theOpr);
        		  }
        		  //theAppMgr.showMenu(MenuConst.PREV_ONLY, theOpr);
        	  }
          } else {
        	  if(bNextColorAvailable) {
        	  	if(showItemAvailableQty){
        	  		theAppMgr.showMenu(MenuConst.LOCATE_WITH_NEXT_COLOR_AND_ITEM_UNAVAILABLE, theOpr);
        	  	}else{
          		  theAppMgr.showMenu(MenuConst.LOCATE_WITH_NEXT_COLOR_AND_ITEM_AVAILABLE, theOpr);
        	  	}
        	  } else {
        	  	if(showItemAvailableQty){
        	  		theAppMgr.showMenu(MenuConst.LOCATE_AND_ITEM_UNAVAILABLE, theOpr);
        	  	}else{
        	  		theAppMgr.showMenu(MenuConst.LOCATE_AND_ITEM_AVAILABLE, theOpr);
        	  	}
        	  }
          }
      }else{
    	  //If NO Next and Previous Buttons 
    	  //TD
	      if (theAppMgr.getStateObject("OPERATOR") == null) {
	        theAppMgr.showMenu(MenuConst.PREV_ONLY, theOpr);
	      } else {
	        theAppMgr.showMenu(MenuConst.LOCATE, theOpr);
	      }
      }

       this.addComponentListener(new java.awt.event.ComponentAdapter() {

        /**
         * put your documentation comment here
         * @param e
         */
        public void componentResized(ComponentEvent e) {
          resize();
        }
      }); SwingUtilities.invokeLater(new Runnable() {

        /**
         * put your documentation comment here
         */
        public void run() {
          SwingUtilities.invokeLater(new Runnable() {

            /**
             * put your documentation comment here
             */
            public void run() {
              //txtStyle.requestFocus();
            }
          });
        }
      });
    } catch (Exception e) {
      e.printStackTrace();
    }
    theAppMgr.removeStateObject("ITEM_LOCATE_SEARCHSTRING");
  }

	protected ItemLookupDetailPanel getItemLookupDetailPanel(IApplicationManager appMgr, CMSItem[] cmsItms, String colorId) {
		return new ItemLookupDetailPanel(appMgr, cmsItms, colorId );
	}
  
  /**
   *
   */
  private void reset() {
    pnlItemLookupDetail.clear();
    txtStyle.setText("");
    txtModel.setText("");
    txtFabric.setText("");
    txtColor.setText("");
    txtSupplier.setText("");
    txtYear.setText("");
    txtSeason.setText("");
   
   
  }

  /**
   *
   */
  private void enter() {
    theAppMgr.setSingleEditArea(res.getString("Select a cell and then menu option"));
  }

  
  
  /**
   *
   */
  private void doAddItem() {
	POSLineItem lineItem = null;
    CMSItem cmsItem = this.pnlItemLookupDetail.getSelectedItem();
    if (cmsItem != null) {
    	if (!cmsItem.getStoreId().equals(homeStoreId)) {
    		return;
    	}
    	//TODO - TD Does following requires - confirm
        //Fix for PCR1256 and PCR1167
        //----------------------------------------------------------
        /*
    	if (!cmsItem.getStoreId().equals(homeStoreId)) {
      	  cmsItem.doSetIsItemFromCurrentStore(false);
        }
        */
        //----------------------------------------------------------
      CMSCompositePOSTransaction posTxn = (CMSCompositePOSTransaction)theAppMgr.getStateObject(
          "TXN_POS");
      if (posTxn == null)
        return;
      if (theAppMgr.getStateObject("TXN_MODE") != null) {
        iTxnMode = ((Integer)theAppMgr.getStateObject("TXN_MODE")).intValue();
      }
      try {
        switch (iTxnMode) {
          case RETURN_MODE:
            lineItem = posTxn.addReturnItem(cmsItem);
            break;
          case LAYAWAY_MODE:
            lineItem = posTxn.addLayawayItem(cmsItem);
            break;
          case PRE_SALE_OPEN:
            lineItem = posTxn.addPresaleItem(cmsItem);
            break;
          case CONSIGNMENT_OPEN:
            lineItem = posTxn.addConsignmentItem(cmsItem);
            break;
            //-- From Europe --
          case RESERVATIONS_OPEN:
            lineItem = posTxn.addReservationItem(cmsItem);
            break;
          case NO_OPEN_RESERVATIONS_CLOSE_SALE:
            lineItem = posTxn.addNoReservationLineItemAsSale(cmsItem);
            break;
          case NO_OPEN_RESERVATIONS_CLOSE_RETURN:
              lineItem = posTxn.addNoReservationLineItemAsReturn(cmsItem);
              break;
          case NO_SALE_MODE:
              lineItem = posTxn.addNoSaleItem(cmsItem);
              break;
          case NO_RETURN_MODE:
              lineItem = posTxn.addNoReturnItem(cmsItem);
              //-- End From Europe --
          default:
            lineItem = posTxn.addSaleItem(cmsItem);
        }
		lineItem.setExtendedBarCode(cmsItem.getBarCode());

        //TODO - TD Confirm the following changes
        //Employee Discount for Armani
        if (theAppMgr.getStateObject("ARM_EMPLOYEE_DISCOUNT") != null && (theAppMgr.getStateObject("ARM_EMPLOYEE_DISCOUNT") instanceof CMSEmployeeDiscount)) {
        	lineItem.addDiscount((CMSEmployeeDiscount)theAppMgr.getStateObject("ARM_EMPLOYEE_DISCOUNT"));
        }
      } catch (BusinessRuleException ex) {
        ex.printStackTrace();
      }
    }
  }

  /**
   *
   */
  private void doPrevious() {
    reset();
    previousFlag = true;
  }

  /**
   * put your documentation comment here
   * @param anEvent
   */

	public void appButtonEvent(CMSActionEvent anEvent) {
		String sAction = anEvent.getActionCommand();
		String sQuery = "";
		if (sAction.equals("ADD_ITEM")) {
			if(pnlItemLookupDetail.getSelectedItem() != null) {
				doAddItem();
			}else{
				anEvent.consume();
				this.theAppMgr.showErrorDlg(res.getString("Select Item to Add"));
			}
		}
		if (sAction.equals("PREV")) {
			doPrevious();
		}
	    if (sAction.equals("NEXT_COLOR")) {
	    	doNextColor();
	    }
	    if (sAction.equals("PREVIOUS_COLOR")) {
	    	doPreviousColor();
	    }
	    // Tim: 1709:Update menus to add "toggle" button. 
	    if("TOGGLE_ITEM_AVAILABILITY".equals(sAction) 
		    || "TOGGLE_ITEM_UNAVAILABILITY".equals(sAction)){
		    String appMenu = theAppMgr.getMenu();
		    if(appMenu != null){ 
		    	theAppMgr.showMenu(getToggleMenu(appMenu),theOpr);
		    }
		    showItemAvailableQty = !showItemAvailableQty;
		    pnlItemLookupDetail.toggleShowAvailableQty(showItemAvailableQty);
		    anEvent.consume();
	    }
	    //Started issue #1941 for Stock Locator by neeti
	    
	 		if ("STOCK_REQUEST".equals(sAction)) {
			if ("US".equalsIgnoreCase(Version.CURRENT_REGION)) {
				if (pnlItemLookupDetail.getSelectedItem() != null) {					
						doShowStockItem();									
				} else {
					theAppMgr.showErrorDlg("Select item for stock");
					anEvent.consume();
				}
			}
		}
		if ("SEND_REQUEST".equals(sAction)) {
			if ("US".equalsIgnoreCase(Version.CURRENT_REGION)) {				
				if (pnlItemLookupDetail.getSelectedItem() != null) {					
					enterEmployeeId();
				} else {
					theAppMgr.showErrorDlg("Select Item for Stock");
					anEvent.consume();
				}
			}
		}
	 }

	
	 	// Ended issue # 1941 for Stock Locator by neeti

	
	private String getToggleMenu(String currMenu){
		
		if(MenuConst.LOCATE_AND_ITEM_AVAILABLE.equalsIgnoreCase(currMenu)){
	  		return MenuConst.LOCATE_AND_ITEM_UNAVAILABLE;
	  	}else if(MenuConst.LOCATE_AND_ITEM_UNAVAILABLE.equalsIgnoreCase(currMenu)){
	  		return MenuConst.LOCATE_AND_ITEM_AVAILABLE;
	  	}else if(MenuConst.LOCATE_WITH_NEXT_AND_PREVIOUS_COLOR_AND_ITEM_AVAILABLE.equalsIgnoreCase(currMenu)){
	  		return MenuConst.LOCATE_WITH_NEXT_AND_PREVIOUS_COLOR_AND_ITEM_UNAVAILABLE;
	  	}else if(MenuConst.LOCATE_WITH_NEXT_AND_PREVIOUS_COLOR_AND_ITEM_UNAVAILABLE.equalsIgnoreCase(currMenu)){
	  		return MenuConst.LOCATE_WITH_NEXT_AND_PREVIOUS_COLOR_AND_ITEM_AVAILABLE;
	  	}if(MenuConst.LOCATE_WITH_NEXT_COLOR_AND_ITEM_AVAILABLE.equalsIgnoreCase(currMenu)){
	  		return MenuConst.LOCATE_WITH_NEXT_COLOR_AND_ITEM_UNAVAILABLE;
	  	}else if(MenuConst.LOCATE_WITH_NEXT_COLOR_AND_ITEM_UNAVAILABLE.equalsIgnoreCase(currMenu)){
	  		return MenuConst.LOCATE_WITH_NEXT_COLOR_AND_ITEM_AVAILABLE;
	  	}if(MenuConst.LOCATE_WITH_PREV_AND_ITEM_AVAILABLE.equalsIgnoreCase(currMenu)){
	  		return MenuConst.LOCATE_WITH_PREV_AND_ITEM_UNAVAILABLE;
	  	}else if(MenuConst.LOCATE_WITH_PREV_AND_ITEM_UNAVAILABLE.equalsIgnoreCase(currMenu)){
	  		return MenuConst.LOCATE_WITH_PREV_AND_ITEM_AVAILABLE;
	  	}if(MenuConst.LOCATE_WITH_PREVIOUS_COLOR_AND_ITEM_AVAILABLE.equalsIgnoreCase(currMenu)){
	  		return MenuConst.LOCATE_WITH_PREVIOUS_COLOR_AND_ITEM_UNAVAILABLE;
	  	}else if(MenuConst.LOCATE_WITH_PREVIOUS_COLOR_AND_ITEM_UNAVAILABLE.equalsIgnoreCase(currMenu)){
	  		return MenuConst.LOCATE_WITH_PREVIOUS_COLOR_AND_ITEM_AVAILABLE;
	  	}else if(MenuConst.NEXT_COLOR_AND_PREV_AND_ITEM_AVAILABLE.equalsIgnoreCase(currMenu)){
	  		return MenuConst.NEXT_COLOR_AND_PREV_AND_ITEM_UNAVAILABLE;
	  	}else if(MenuConst.NEXT_COLOR_AND_PREV_AND_ITEM_UNAVAILABLE.equalsIgnoreCase(currMenu)){
	  		return MenuConst.NEXT_COLOR_AND_PREV_AND_ITEM_AVAILABLE;
	  	}else if(MenuConst.NEXT_AND_PREVIOUS_COLOR_AND_PREV_AND_ITEM_AVAILABLE.equalsIgnoreCase(currMenu)){
	  		return MenuConst.NEXT_AND_PREVIOUS_COLOR_AND_PREV_AND_ITEM_UNAVAILABLE;
	  	}else if(MenuConst.NEXT_AND_PREVIOUS_COLOR_AND_PREV_AND_ITEM_UNAVAILABLE.equalsIgnoreCase(currMenu)){
	  		return MenuConst.NEXT_AND_PREVIOUS_COLOR_AND_PREV_AND_ITEM_AVAILABLE;
	  	}else if(MenuConst.PREVIOUS_COLOR_AND_PREV_AND_ITEM_AVAILABLE.equalsIgnoreCase(currMenu)){
	  		return MenuConst.PREVIOUS_COLOR_AND_PREV_AND_ITEM_UNAVAILABLE;
	  	}else if(MenuConst.PREVIOUS_COLOR_AND_PREV_AND_ITEM_UNAVAILABLE.equalsIgnoreCase(currMenu)){
	  		return MenuConst.PREVIOUS_COLOR_AND_PREV_AND_ITEM_AVAILABLE;
	  	}
	  	if(MenuConst.SEND_REQUEST_AND_ITEM_AVAILABLE.equalsIgnoreCase(currMenu)){
	  		return MenuConst.SEND_REQUEST_AND_ITEM_UNAVAILABLE;	  		
	  	}else if(MenuConst.SEND_REQUEST_AND_ITEM_UNAVAILABLE.equalsIgnoreCase(currMenu)){
	  		return MenuConst.SEND_REQUEST_AND_ITEM_AVAILABLE;
	  	}
	  	return currMenu;
	}
	
	  /**
	   * Displays the next color in all locations
	   */
	  private void doNextColor() {
		  //itemSearchString = (ItemSearchString) theAppMgr.getStateObject("ITEM_LOCATE_SEARCHSTRING");
		  itemLookupDetailModel = pnlItemLookupDetail.getAddressModel();
		  String colorDetail[] = itemLookupDetailModel.getNextColorDetail();
		  if(colorDetail != null) {
			  itemSearchString.setColor(colorDetail[0]);
			  itemSearchString.setColorDesc(colorDetail[1]);
		  }
		  refreshItemLookupHeaderPanel(itemSearchString);
		  if (itemLookupDetailModel.getCurrentColorIndex() > -1) {
			  itemLookupDetailModel.showNextColor();
			  needNextColor = itemLookupDetailModel.hasNextColor();
			  if (needNextColor) {
			  	  needPrevColor = itemLookupDetailModel.hasPreviousColor();
		  		  if (theAppMgr.getStateObject("OPERATOR") == null) {
		  			  if (needPrevColor) {
		  			  	if(showItemAvailableQty){
		  			  		theAppMgr.showMenu(MenuConst.NEXT_AND_PREVIOUS_COLOR_AND_PREV_AND_ITEM_UNAVAILABLE, theOpr);
		  			  	}else{
		  			  		theAppMgr.showMenu(MenuConst.NEXT_AND_PREVIOUS_COLOR_AND_PREV_AND_ITEM_AVAILABLE, theOpr);
		  			  	}
		  			  } else {
		  			  	if(showItemAvailableQty){
		  			  		theAppMgr.showMenu(MenuConst.NEXT_COLOR_AND_PREV_AND_ITEM_UNAVAILABLE, theOpr);
		  			  	}else{
		  			  		theAppMgr.showMenu(MenuConst.NEXT_COLOR_AND_PREV_AND_ITEM_AVAILABLE, theOpr);
		  			  	}
		  			  }
		  		  } else {
		  			  if (needPrevColor) {
		  			  	if(showItemAvailableQty){
		  			  		theAppMgr.showMenu(MenuConst.LOCATE_WITH_NEXT_AND_PREVIOUS_COLOR_AND_ITEM_UNAVAILABLE, theOpr);
		  			  	}else{
		  			  		theAppMgr.showMenu(MenuConst.LOCATE_WITH_NEXT_AND_PREVIOUS_COLOR_AND_ITEM_AVAILABLE, theOpr);
		  			  	}
		  			  } else {
		  			  	if(showItemAvailableQty){
		  			  		theAppMgr.showMenu(MenuConst.LOCATE_WITH_NEXT_COLOR_AND_ITEM_UNAVAILABLE, theOpr);
		  			  	}else{
		  			  		theAppMgr.showMenu(MenuConst.LOCATE_WITH_NEXT_COLOR_AND_ITEM_AVAILABLE, theOpr);
		  			  	}
		  			  }
		  		  }
			  } else {
				  if (theAppMgr.getStateObject("OPERATOR") == null) {
				  	if(showItemAvailableQty){
				  		theAppMgr.showMenu(MenuConst.PREVIOUS_COLOR_AND_PREV_AND_ITEM_UNAVAILABLE, theOpr);
				  	}else{
				  		theAppMgr.showMenu(MenuConst.PREVIOUS_COLOR_AND_PREV_AND_ITEM_AVAILABLE, theOpr);
				  	}
			    } else {
	  			  	if(showItemAvailableQty){
	  		    	  theAppMgr.showMenu(MenuConst.LOCATE_WITH_PREVIOUS_COLOR_AND_ITEM_UNAVAILABLE, theOpr);
	  			  	}else{
	  		    	  theAppMgr.showMenu(MenuConst.LOCATE_WITH_PREVIOUS_COLOR_AND_ITEM_AVAILABLE, theOpr);
	  			  	}
			      }
			  }
		  }
	  }

	  /**
	   * Displays the previous color in all locations
	   */
	  private void doPreviousColor() {
		  //itemSearchString = (ItemSearchString) theAppMgr.getStateObject("ITEM_LOCATE_SEARCHSTRING");
		  itemLookupDetailModel = pnlItemLookupDetail.getAddressModel();
		  String colorDetail[] = itemLookupDetailModel.getPreviousColorDetail();
		  if(colorDetail != null) {
			  itemSearchString.setColor(colorDetail[0]);
			  itemSearchString.setColorDesc(colorDetail[1]);
		  }
		  if (itemLookupDetailModel.getCurrentColorIndex() > 0) {
			  //theAppMgr.addStateObject("ITEM_LOCATE_SEARCHSTRING", itemSearchString);
			  //firstRunCompleted = true;
			  refreshItemLookupHeaderPanel(itemSearchString);
			  //refreshItemLookupDetailPanel(firstRunCompleted, itemRowKeyVecColor);
			  itemLookupDetailModel.showPreviousColor();
			  needPrevColor = itemLookupDetailModel.hasPreviousColor();
			  if (needPrevColor) {
				  needNextColor = itemLookupDetailModel.hasNextColor();
				  if (theAppMgr.getStateObject("OPERATOR") == null) {
					  if (needNextColor) {
					  	if(showItemAvailableQty){
	  			  		theAppMgr.showMenu(MenuConst.NEXT_AND_PREVIOUS_COLOR_AND_PREV_AND_ITEM_UNAVAILABLE, theOpr);
	  			  	}else{
	  			  		theAppMgr.showMenu(MenuConst.NEXT_AND_PREVIOUS_COLOR_AND_PREV_AND_ITEM_AVAILABLE, theOpr);
	  			  	}
					  } else {
					  	if(showItemAvailableQty){
		    	  		theAppMgr.showMenu(MenuConst.NEXT_COLOR_AND_PREV_AND_ITEM_UNAVAILABLE, theOpr);
		    	  	}else{
		    	  		theAppMgr.showMenu(MenuConst.NEXT_COLOR_AND_PREV_AND_ITEM_AVAILABLE, theOpr);
		    	  	}
					  }
				  } else {
		  			  if (needNextColor) {
		  			  	if(showItemAvailableQty){
		  			  		theAppMgr.showMenu(MenuConst.LOCATE_WITH_NEXT_AND_PREVIOUS_COLOR_AND_ITEM_UNAVAILABLE, theOpr);
		  			  	}else{
		  			  		theAppMgr.showMenu(MenuConst.LOCATE_WITH_NEXT_AND_PREVIOUS_COLOR_AND_ITEM_AVAILABLE, theOpr);
		  			  	}
		  			  } else {
		  			  	if(showItemAvailableQty){
		  			  		theAppMgr.showMenu(MenuConst.LOCATE_WITH_NEXT_COLOR_AND_ITEM_UNAVAILABLE, theOpr);
		  			  	}else{
		  			  		theAppMgr.showMenu(MenuConst.LOCATE_WITH_NEXT_COLOR_AND_ITEM_AVAILABLE, theOpr);
		  			  	}
		  			  }
				  }
			  } else {
				  if (theAppMgr.getStateObject("OPERATOR") == null) {
				  	if(showItemAvailableQty){
				  		theAppMgr.showMenu(MenuConst.NEXT_COLOR_AND_PREV_AND_ITEM_UNAVAILABLE, theOpr);
				  	}else{
				  		theAppMgr.showMenu(MenuConst.NEXT_COLOR_AND_PREV_AND_ITEM_AVAILABLE, theOpr);
				  	}
				  } else {
				  	if(showItemAvailableQty){
			    	  theAppMgr.showMenu(MenuConst.LOCATE_WITH_NEXT_COLOR_AND_ITEM_UNAVAILABLE, theOpr);
				  	}else{
			    	  theAppMgr.showMenu(MenuConst.LOCATE_WITH_NEXT_COLOR_AND_ITEM_AVAILABLE, theOpr);
				  	}
				  }
			  }
		  } else {
			  if (theAppMgr.getStateObject("OPERATOR") == null) {
			  	if(showItemAvailableQty){
	  	  		theAppMgr.showMenu(MenuConst.NEXT_COLOR_AND_PREV_AND_ITEM_UNAVAILABLE, theOpr);
	  	  	}else{
	  	  		theAppMgr.showMenu(MenuConst.NEXT_COLOR_AND_PREV_AND_ITEM_AVAILABLE, theOpr);
	  	  	}
			  } else {
			  	if(showItemAvailableQty){
		    	  theAppMgr.showMenu(MenuConst.LOCATE_WITH_NEXT_COLOR_AND_ITEM_UNAVAILABLE, theOpr);
			  	}else{
		    	  theAppMgr.showMenu(MenuConst.LOCATE_WITH_NEXT_COLOR_AND_ITEM_AVAILABLE, theOpr);
			  	}
				  //theAppMgr.showMenu(MenuConst.LOCATE_WITH_NEXT_COLOR, theOpr);
			  }
		  }
	  }

	  /**
	   * Refresh ItemLookupHeaderPanel with the new values
	   * @param itemSearchString
	   */
	  private void refreshItemLookupHeaderPanel(ItemSearchString itemSearchStr) {
		  if( txtStyle != null && itemSearchStr != null ) {
		  	  this.txtStyle.setText(itemSearchStr.getStyle());
		  	  this.txtModel.setText(itemSearchStr.getModel());
		  	  this.txtSupplier.setText(itemSearchStr.getSupplierName());
		  	  this.txtFabric.setText(itemSearchStr.getFabric());
		  	  this.txtColor.setText(itemSearchStr.getColor() + "-" + itemSearchStr.getColorDesc());
		  	  this.txtYear.setText(itemSearchStr.getYear());
		  	  this.txtSeason.setText(itemSearchStr.getSeasonDesc());
		  	  if(this.getContentPane() != null ) {
		  		  this.getContentPane().invalidate();
		  		  this.getContentPane().repaint();
		  	  }
		  }
	  	  //this.getContentPane().remove(pnlItemLookupDetail);
	  }

	  /**
	   *  Refresh ItemLookupDetailPanel with the new values
	   */
	  
	  /*
	  private void refreshItemLookupDetailPanel(boolean firstRunCompleted, Vector itemRowKeyVecColor) {
	  	  //pnlItemLookupDetail = new ItemLookupDetailPanel(theAppMgr, cmsItems, firstRunCompleted, itemRowKeyVecColor);
	  	  //scrollPane.setViewportView(pnlItemLookupDetail);
	      this.addComponentListener(new java.awt.event.ComponentAdapter() {
	      	public void componentResized(ComponentEvent e) {
	      		resize();
	      	}
	      });
	      SwingUtilities.invokeLater(new Runnable() {
	      	public void run() {
	      		SwingUtilities.invokeLater(new Runnable() {
	      			public void run() {
	      			}
	      		});
	      	}
	      });
	  }
	  */
	  
  /*
   private void showNoItemFound()
   {
   theAppMgr.showErrorDlg(res.getString("Sorry, no Item found"));
   }
   */
  /**
   *
   * @param itemSearchString ItemSearchString
   * @return CMSItem[]
   */
  private CMSItem[] processQuery(ItemSearchString itmSearchStr ) {
    CMSItem[] cmsItems = null;
    //itemSearchString = (ItemSearchString)theAppMgr.getStateObject("ITEM_LOCATE_SEARCHSTRING");
    if (itmSearchStr == null) {
      doPrevious();
      return null;
    }
    //theAppMgr.removeStateObject("ITEM_LOCATE_SEARCHSTRING");
    this.txtStyle.setText(itmSearchStr.getStyle());
    this.txtModel.setText(itmSearchStr.getModel());
    //EUROPE
    this.txtProduct.setText(itmSearchStr.getProductNum());
    this.txtSupplier.setText(itmSearchStr.getSupplierName());
    this.txtFabric.setText(itmSearchStr.getFabric());

    String colorStr = itmSearchStr.getColor();
    if( colorStr != null ) {
    	colorStr = colorStr.trim();
    }
    if(colorStr != null) {
    	if(itmSearchStr.getColorDesc() != null && itmSearchStr.getColorDesc().trim().length() > 0 ) {
    		colorStr = colorStr + "-" + itmSearchStr.getColorDesc().trim();
    	}
    }else{
    	colorStr="";
    }
    this.txtColor.setText(colorStr);
    //this.txtColor.setText( itmSearchStr.getColor() + "-" + itmSearchStr.getColorDesc());
    //this.txtYear.setText(itmSearchStr.getYearDesc());
    this.txtYear.setText(itmSearchStr.getYear());
    this.txtSeason.setText(itmSearchStr.getSeasonDesc());
    //      itmSearchStr.setColor("");
    try {
        if (cmsItems == null || cmsItems.length <= 0) {
            cmsItems = (CMSItem[])CMSItemHelper.findItems(theAppMgr, itmSearchStr);
        }
    } catch (Exception ex) {
        ex.printStackTrace();
    }
    return cmsItems;
  }

  /**
   *
   * @return String
   */
  public String getVersion() {
    return ("$Revision: 1.1 $");
  }

  /**
   *
   * @return String
   */
  public String getScreenName() {
    return "Item Locate";
  }

  /**
   *
   * @throws Exception
   */
  protected void jbInit()
      throws Exception {
	//pnlItemLookupDetail = new ItemLookupDetailPanel(theAppMgr, cmsItems);
	pnlItemLookupDetail = getItemLookupDetailPanel(theAppMgr, cmsItems, null);
    scrollPane = new JScrollPane(pnlItemLookupDetail, JScrollPane.VERTICAL_SCROLLBAR_NEVER
        , JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
    pnlSearch = new JPanel();
    lblStyle = new JCMSLabel();
    txtStyle = new JCMSTextField();
    lblModel = new JCMSLabel();
    txtModel = new JCMSTextField();
    /*JPanel pnlNorth = new JPanel();
    this.getContentPane().add(pnlNorth, BorderLayout.NORTH);
    pnlNorth.add(lblTitle, null);
    lblTitle.setHorizontalAlignment(SwingConstants.CENTER);
    lblTitle.setPreferredSize(new Dimension((int)(450 * r), (int)(25 * r)));
    pnlNorth.setBackground(theAppMgr.getBackgroundColor());
    pnlNorth.setPreferredSize(new Dimension((int)(10 * r), (int)(35 * r)));*/
    
    //EUROPE
    lblProduct = new JCMSLabel();
    txtProduct = new JCMSTextField();
    lblFabric = new JCMSLabel();
    txtFabric = new JCMSTextField();
    lblColor = new JCMSLabel();
    txtColor = new JCMSTextField();
    lblSupplier = new JCMSLabel();
    txtSupplier = new JCMSTextField();
    lblYear = new JCMSLabel();
    txtYear = new JCMSTextField();
    lblSeason = new JCMSLabel();
    txtSeason = new JCMSTextField();
    GridBagLayout gridBagLayout1 = new GridBagLayout();
    BorderLayout borderLayout1 = new BorderLayout();
    this.getContentPane().setLayout(borderLayout1);
    this.getContentPane().add(pnlSearch, BorderLayout.NORTH);
    //this.getContentPane().add(pnlItemLookupDetail, BorderLayout.CENTER);
    this.getContentPane().add(scrollPane, BorderLayout.CENTER);
    this.setLayout(borderLayout1);
    pnlSearch.setPreferredSize(new Dimension(833, 105));
    pnlSearch.setLayout(gridBagLayout1);
    lblStyle.setLabelFor(txtStyle);
    lblStyle.setText("Style");
    txtStyle.setText("");
    lblModel.setLabelFor(txtModel);
    lblModel.setText("Model");
    txtModel.setText("");
    //EUROPE
    lblProduct.setLabelFor(txtProduct);
    lblProduct.setText("Product Number");
    txtProduct.setText("");
    lblFabric.setLabelFor(txtFabric);
    lblFabric.setText("Fabric");
    txtFabric.setText("");
    lblColor.setLabelFor(txtColor);
    lblColor.setText("Color");
    txtColor.setText("");
    lblSupplier.setText("Supplier");
    lblYear.setText("Year");
    lblSeason.setText("Season");
    txtStyle.setEnabled(false);
    //EUROPE
    txtProduct.setEnabled(false);
    txtModel.setEnabled(false);
    txtSupplier.setEnabled(false);
    txtFabric.setEnabled(false);
    txtColor.setEnabled(false);
    txtYear.setEnabled(false);
    txtSeason.setEnabled(false);
    this.add(pnlSearch, BorderLayout.NORTH);
    this.setBackground(theAppMgr.getBackgroundColor());
    pnlSearch.setBackground(theAppMgr.getBackgroundColor());
    //pnlItemLookupDetail.setAppMgr(theAppMgr);
    lblStyle.setAppMgr(theAppMgr);
    lblStyle.setFont(theAppMgr.getTheme().getLabelFont());
    txtStyle.setAppMgr(theAppMgr);
    txtStyle.setFont(theAppMgr.getTheme().getTextFieldFont());
    lblModel.setAppMgr(theAppMgr);
    lblModel.setFont(theAppMgr.getTheme().getLabelFont());
    txtModel.setAppMgr(theAppMgr);
    txtModel.setFont(theAppMgr.getTheme().getTextFieldFont());
    //EUROPE
    lblProduct.setAppMgr(theAppMgr);
    lblProduct.setFont(theAppMgr.getTheme().getLabelFont());
    txtProduct.setAppMgr(theAppMgr);
    txtProduct.setFont(theAppMgr.getTheme().getTextFieldFont());
    lblFabric.setAppMgr(theAppMgr);
    lblFabric.setFont(theAppMgr.getTheme().getLabelFont());
    txtFabric.setAppMgr(theAppMgr);
    txtFabric.setFont(theAppMgr.getTheme().getTextFieldFont());
    lblColor.setAppMgr(theAppMgr);
    lblColor.setFont(theAppMgr.getTheme().getLabelFont());
    txtColor.setAppMgr(theAppMgr);
    txtColor.setFont(theAppMgr.getTheme().getTextFieldFont());
    lblSupplier.setAppMgr(theAppMgr);
    lblSupplier.setFont(theAppMgr.getTheme().getLabelFont());
    txtSupplier.setAppMgr(theAppMgr);
    txtSupplier.setFont(theAppMgr.getTheme().getTextFieldFont());
    lblYear.setAppMgr(theAppMgr);
    lblYear.setFont(theAppMgr.getTheme().getLabelFont());
    txtYear.setAppMgr(theAppMgr);
    txtYear.setFont(theAppMgr.getTheme().getTextFieldFont());
    lblSeason.setAppMgr(theAppMgr);
    lblSeason.setFont(theAppMgr.getTheme().getLabelFont());
    txtSeason.setAppMgr(theAppMgr);
    txtSeason.setFont(theAppMgr.getTheme().getTextFieldFont());
    pnlSearch.add(lblStyle
        , new GridBagConstraints(0, 0, 2, 1, 0.0, 0.0, GridBagConstraints.WEST
        , GridBagConstraints.NONE, new Insets(0, 12, 0, 4), 4, 4));
    pnlSearch.add(txtStyle
        , new GridBagConstraints(0, 1, 2, 1, 1.0, 0.0, GridBagConstraints.WEST
        , GridBagConstraints.HORIZONTAL, new Insets(0, 8, 4, 4), 4, 0));
    pnlSearch.add(lblModel
        , new GridBagConstraints(2, 0, 2, 1, 0.0, 0.0, GridBagConstraints.SOUTHWEST
        , GridBagConstraints.NONE, new Insets(0, 8, 0, 4), 4, 4));
    pnlSearch.add(txtModel
        , new GridBagConstraints(2, 1, 2, 1, 1.0, 0.0, GridBagConstraints.NORTHWEST
        , GridBagConstraints.HORIZONTAL, new Insets(0, 4, 4, 4), 4, 0));
    //EUROPE
    pnlSearch.add(lblProduct
            , new GridBagConstraints(4, 0, 2, 1, 0.0, 0.0, GridBagConstraints.SOUTHWEST
            , GridBagConstraints.NONE, new Insets(0, 8, 0, 4), 4, 4));
    pnlSearch.add(txtProduct
            , new GridBagConstraints(4, 1, 2, 1, 1.0, 0.0, GridBagConstraints.NORTHWEST
            , GridBagConstraints.HORIZONTAL, new Insets(0, 4, 4, 4), 4, 0));
    pnlSearch.add(lblSupplier
        , new GridBagConstraints(/*4*/6, 0, /*3*/2, 1, 0.0, 0.0, GridBagConstraints.SOUTHWEST
        , GridBagConstraints.NONE, new Insets(0, 8, 0, 4), 4, 4));
    pnlSearch.add(txtSupplier
        , new GridBagConstraints(6, 1, 2, 1, 1.0, 0.0, GridBagConstraints.NORTHWEST
        , GridBagConstraints.HORIZONTAL, new Insets(0, 4, 4, 4), 4, 0));
    pnlSearch.add(lblFabric
        , new GridBagConstraints(0, 2, 2, 1, 0.0, 0.0, GridBagConstraints.SOUTHWEST
        , GridBagConstraints.NONE, new Insets(4, 12, 0, 4), 4, 0));
    pnlSearch.add(txtFabric
        , new GridBagConstraints(0, 3, 2, 1, 1.0, 0.0, GridBagConstraints.NORTHWEST
        , GridBagConstraints.HORIZONTAL, new Insets(0, 8, 4, 4), 4, 4));
    pnlSearch.add(lblColor
        , new GridBagConstraints(2, 2, 2, 1, 0.0, 0.0, GridBagConstraints.SOUTHWEST
        , GridBagConstraints.NONE, new Insets(4, 8, 0, 4), 4, 0));
    pnlSearch.add(txtColor
        , new GridBagConstraints(2, 3, 2, 1, 1.0, 0.0, GridBagConstraints.NORTHWEST
        , GridBagConstraints.HORIZONTAL, new Insets(0, 4, 4, 4), 4, 4));
    pnlSearch.add(lblYear
        , new GridBagConstraints(4, 2, /*1*/2, 1, 0.0, 0.0, GridBagConstraints.SOUTHWEST
        , GridBagConstraints.NONE, new Insets(4, 8, 0, 4), 4, 0));
    pnlSearch.add(txtYear
        , new GridBagConstraints(4, 3, 2, 1, 1.0, 0.0, GridBagConstraints.NORTHWEST
        , GridBagConstraints.HORIZONTAL, new Insets(0, 4, 4, 4), 4, 4));
    pnlSearch.add(lblSeason
        , new GridBagConstraints(/*5*/6, 2, 2, 1, 0.0, 0.0, GridBagConstraints.SOUTHWEST
        , GridBagConstraints.NONE, new Insets(4, 8, 0, 4), 4, 0));
    pnlSearch.add(txtSeason
        , new GridBagConstraints(6, 3, 2, 1, 1.0, 0.0, GridBagConstraints.NORTHWEST
        , GridBagConstraints.HORIZONTAL, new Insets(0, 4, 4, 4), 4, 4));
  }

  /**
   *
   */
  public void resize() {
    double r = com.chelseasystems.cr.swing.CMSApplet.r;
    lblStyle.setPreferredSize(new Dimension((int)(55 * r), (int)(30 * r)));
    txtStyle.setPreferredSize(new Dimension((int)(75 * r), (int)(30 * r)));
    lblModel.setPreferredSize(new Dimension((int)(55 * r), (int)(30 * r)));
    txtModel.setPreferredSize(new Dimension((int)(95 * r), (int)(30 * r)));
    //EUROPE
    lblProduct.setPreferredSize(new Dimension((int)(55 * r), (int)(30 * r)));
    txtProduct.setPreferredSize(new Dimension((int)(95 * r), (int)(30 * r)));
    lblFabric.setPreferredSize(new Dimension((int)(55 * r), (int)(30 * r)));
    txtFabric.setPreferredSize(new Dimension((int)(95 * r), (int)(30 * r)));
    lblColor.setPreferredSize(new Dimension((int)(55 * r), (int)(30 * r)));
    txtColor.setPreferredSize(new Dimension((int)(95 * r), (int)(30 * r)));
    lblSupplier.setPreferredSize(new Dimension((int)(55 * r), (int)(30 * r)));
    txtSupplier.setPreferredSize(new Dimension((int)(95 * r), (int)(30 * r)));
    lblYear.setPreferredSize(new Dimension((int)(55 * r), (int)(30 * r)));
    txtYear.setPreferredSize(new Dimension((int)(95 * r), (int)(30 * r)));
    lblSeason.setPreferredSize(new Dimension((int)(55 * r), (int)(30 * r)));
    txtSeason.setPreferredSize(new Dimension((int)(95 * r), (int)(30 * r)));
  }

  /**
   *
   * @param e MouseEvent
   */
  public void pageDown(MouseEvent e) {
    pnlItemLookupDetail.nextPage();
    theAppMgr.showPageNumber(e, pnlItemLookupDetail.getCurrentPageNumber() + 1
        , pnlItemLookupDetail.getTotalPages());
  }

  /**
   *
   * @param e MouseEvent
   */
  public void pageUp(MouseEvent e) {
    pnlItemLookupDetail.prevPage();
    theAppMgr.showPageNumber(e, pnlItemLookupDetail.getCurrentPageNumber() + 1
        , pnlItemLookupDetail.getTotalPages());
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
  //Started for issue # 1941 stock locator by neeti
  
  private void doShowStockItem() {

		cmsItem = pnlItemLookupDetail.getSelectedItem();
		if (cmsItem == null)
			return;		
		if (cmsItem.getItemStock().getAvailableQty() >= 1) {
			this.getContentPane().remove(pnlItemLookupDetail);
			pnlItemLookupDetail = getItemLookupDetailPanelForStoreLocator(
					cmsItems, theAppMgr, null);
			pnlItemLookupDetail.repaint();
			this.getContentPane().add(pnlItemLookupDetail);
			scrollPane.setViewportView(pnlItemLookupDetail);
			scrollPane.repaint();
		}
		else{
			theAppMgr.showErrorDlg("No Stock Available");
		}
		 if(showItemAvailableQty){
		  		theAppMgr.showMenu(MenuConst.SEND_REQUEST_AND_ITEM_UNAVAILABLE, theOpr);
		  	}else{
		  		theAppMgr.showMenu(MenuConst.SEND_REQUEST_AND_ITEM_AVAILABLE, theOpr);
		  	}
	}  
  
  protected ItemLookupDetailPanel getItemLookupDetailPanelForStoreLocator(
			CMSItem[] cmsItms, IApplicationManager appMgr, String colorId) {
		return new ItemLookupDetailPanel(cmsItms, appMgr, colorId);
	}

	private void enterEmployeeId() {
		theAppMgr.setSingleEditArea(res.getString("Enter employee ID"),
				"EMPLOYEE");
	}

	public void editAreaEvent(String sCommand, String sInput) {
		if ("US".equalsIgnoreCase(Version.CURRENT_REGION)) {
			if (sCommand.equals("EMPLOYEE")) {
				try {
					emp = CMSEmployeeHelper.findByExternalId(theAppMgr, sInput);
					if (emp != null && emp.isEmploymentStatusActive()) {
						MiscItemTemplate miscItemTemplate = null;
						if (miscItemTemplate == null) {
							miscItemTemplate = solicitMiscItemTemplateChoice();
							enterEmployeeId();
						}
					} else {
						theAppMgr.showErrorDlg(res
								.getString("Cannot find Employee."));
						enterEmployeeId();
					}
				} catch (Exception ex) {
					theAppMgr.showExceptionDlg(ex);
					enterEmployeeId();
				}
			}
		}
	}
	// For Dialog
	private MiscItemTemplate solicitMiscItemTemplateChoice() {
		MiscItemTemplate[] oldmiscItemTemplates = this.getMiscItemsArray();
		MiscItemTemplate[] miscItemTemplates = new MiscItemTemplate[oldmiscItemTemplates.length];
		ConfigMgr mgr = new ConfigMgr("item.cfg");
		String misckeys = mgr.getString("MISC_STOCK_LOCATOR");
		StringTokenizer strTok = new StringTokenizer(misckeys, ",");
		int k = 0;
		while (strTok.hasMoreTokens()) {
			String misckey = strTok.nextToken();
			for (int j = 0; j < oldmiscItemTemplates.length; j++) {
				if (oldmiscItemTemplates[j].getKey().equals(misckey))
					miscItemTemplates[k] = oldmiscItemTemplates[j];
			}
			k++;
		}
		GenericChooserRow[] availMiscItemTemplates = new GenericChooserRow[miscItemTemplates.length];
		for (int i = 0; i < availMiscItemTemplates.length; i++) {
			availMiscItemTemplates[i] = new GenericChooserRow(
					new String[] { miscItemTemplates[i]
							.getMiscItemDescription() }, miscItemTemplates[i]);
		}
		GenericChooseFromTableDlg dlg = new GenericChooseFromTableDlg(theAppMgr
				.getParentFrame(), theAppMgr, availMiscItemTemplates,
				new String[] { (res.getString("Location")) });
		dlg.getTable().getColumnModel().getColumn(0).setCellRenderer(
				GenericChooseFromTableDlg.getCenterRenderer());
		dlg.setVisible(true);
		if (dlg.isOK()) {
			cmsItem = pnlItemLookupDetail.getSelectedItem();
			String empId = emp.getFirstName() + " " + emp.getLastName() + "  "
					+ emp.getExternalID();
			String miscItemDesc = ((MiscItemTemplate) dlg.getSelectedRow()
					.getRowKeyData()).getMiscItemDescription();
			ArmaniStockLocatorPrinter stockPrinter = new ArmaniStockLocatorPrinter(
					cmsItem, empId, miscItemDesc);
			stockPrinter.setPortrait();
			stockPrinter.print();
			return (MiscItemTemplate) ((MiscItemTemplate) dlg.getSelectedRow()
					.getRowKeyData()).clone();
		}
		return null;
	}

	private MiscItemTemplate[] getMiscItemsArray() {
		INIFile file = null;
		String miscItemKeys = null;
		HashMap miscItems = new HashMap();
		try {
			file = new INIFile(FileMgr.getLocalFile("config", "item.cfg"), true);
			miscItemKeys = file.getValue("MISC_STOCK_LOCATOR");
		} catch (INIFileException e) {
			e.printStackTrace();
		}
		StringTokenizer st = new StringTokenizer(miscItemKeys, ",");
		while (st.hasMoreElements()) {
			String key = (String) st.nextElement();
			String miscItemDesc = res.getString(file.getValue(key,
					"MISC_ITEM_DESC", ""));
			miscItems.put(key, new MiscItemTemplate(null, 0, false, null,
					false, false, false, null, null, false, miscItemDesc, 0,
					key, null));
		}
		return ((MiscItemTemplate[]) miscItems.values().toArray(
				new MiscItemTemplate[miscItems.size()]));
	}

	//Ended for isse #1941 stock locator by neeti
}

