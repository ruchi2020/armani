/*
 History:
 +------+------------+-----------+-----------+----------------------------------------------------+
 | Ver# | Date       | By        | Defect #  | Description                                        |
 +------+------------+-----------+-----------+----------------------------------------------------+
 | 8    | 08/15/2005 | Vikram    | 879       | Allowed entry of special characters in search.     |
 +------+------------+-----------+-----------+----------------------------------------------------+
 | 7    | 07/12/2005 | Vikram    | N/A       | Corrections to sorting when returning from Lookup. |
 +------+------------+-----------+-----------+----------------------------------------------------+
 | 6    | 07/09/2005 | Vikram    | 437       | New menu option "Item Details" for Item Lookup     |
 +------+------------+-----------+-----------+----------------------------------------------------+
 | 5    | 07/05/2005 | Vikram    | 353       | Item locator - Edit area prompt is incorrect       |
 +------+------------+-----------+-----------+----------------------------------------------------+
 | 4    | 05/17/2005 | Vikram    | N/A       | Updated for POS_104665_FS_ItemLookup_Rev1          |
 +------+------------+-----------+-----------+----------------------------------------------------+
 | 1    | 04/27/2005 | Vikram    | N/A       | POS_104665_FS_ItemLookup_Rev1                      |
 --------------------------------------------------------------------------------------------------
 *
 * formatted with JxBeauty (c) johann.langhofer@nextra.at
 */


package com.chelseasystems.cs.swing.item;

import com.chelseasystems.cr.swing.*;
import com.chelseasystems.cr.swing.bean.*;
import com.chelseasystems.cs.swing.menu.MenuConst;
//import java.awt.event.MouseAdapter;
import java.awt.event.*;
import com.chelseasystems.cr.swing.event.CMSActionEvent;
import javax.swing.*;
import java.awt.*;
import com.chelseasystems.cs.store.CMSStore;
import com.chelseasystems.cs.swing.panel.ItemLookupListPanel;
import com.chelseasystems.cs.employee.CMSEmployee;
import com.chelseasystems.cs.item.CMSItem;
import com.chelseasystems.cs.item.CMSItemHelper;
import com.chelseasystems.cs.item.ItemSearchString;
import com.ga.fs.fsbridge.ARMFSBridge;
import java.util.Collections;
import java.util.Comparator;
import java.util.Map;
import java.util.Iterator;
import java.util.Vector;
import com.chelseasystems.cr.config.ConfigMgr;
import java.io.*;
import com.chelseasystems.cr.pos.POSLineItem;
import com.chelseasystems.cs.pos.CMSCompositePOSTransaction;
import com.chelseasystems.cr.rules.*;
import com.chelseasystems.cr.appmgr.mask.*;
import com.chelseasystems.cs.item.ItemEntryParser;


/**
 * <p>Title:ItemLookupListApplet </p>
 * <p>Description: Searches items on provided search criteria</p>
 * <p>Copyright: Copyright (c) 2005</p>
 * <p>Company: SkillnetInc</p>
 * @author Vikram Mundhra
 * @version 1.0
 */
public class ItemLookupListApplet extends CMSApplet {
	//Fix for 1821 removed single quote and double quote from the string. 
	public static final String ALPHA_NUMERIC_SPEC =
	      "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789.@_!` #$%&*()_-+=[]{}/;\\? ";
  private ItemLookupListPanel pnlItemLookupList;
  private JPanel pnlSearch;
  private JCMSLabel lblSKU;
  private JCMSTextField txtSKU;
  private JCMSLabel lblOR;
  private JCMSLabel lblStyle;
  private JCMSTextField txtStyle;
  private JCMSLabel lblModel;
  private JCMSTextField txtModel;
  private JCMSLabel lblFabric;
  private JCMSTextField txtFabric;
  private JCMSLabel lblColor;
  private JCMSTextField txtColor;
  private JCMSLabel lblSupplier;
  private JCMSComboBox cbxSupplier;
  private JCMSLabel lblYear;
  //private JCMSComboBox cbxYear;
  private JCMSTextField txtYear;
  private JCMSLabel lblSeason;
  private JCMSComboBox cbxSeason;
  //private ScrollableToolBarPanel screen;
  private Map supplierSeasonYearMap;
  private ItemSearchString itemSearchString;
  private CMSItem[] cmsItems;

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
    //Map tempMap = null;
    try {
      jbInit();
      initSupplierSeasonYearMap();
      //      pnlItemLookupList.addMouseListener(new MouseAdapter()
      //      {
      //          public void mouseClicked (MouseEvent e)
      //          {
      //            if (e.getClickCount() < 2)
      //                return;
      //            itemSelected(pnlItemLookupList.getSelectedItem());
      //          }
      //
      //      });
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  /**
   * put your documentation comment here
   */
  public void initSupplierSeasonYearMap() {
    try {
      CMSStore cmsStore = (CMSStore)theAppMgr.getGlobalObject("STORE");
      if (cmsStore != null)
        supplierSeasonYearMap = CMSItemHelper.getSupplierSeasonYear(theAppMgr
            , (CMSStore)theAppMgr.getGlobalObject("STORE"));
      else
        return;
    } catch (Exception ex) {
      ex.printStackTrace();
      return;
    }
    Vector supplierVec = new Vector();
    Vector seasonVec = new Vector();
    if (supplierSeasonYearMap != null) {
      Map supplierMap = (Map)(supplierSeasonYearMap.get("supplier"));
      supplierVec.addElement(new KeyValuePairObject("", ""));
      for (Iterator i = supplierMap.keySet().iterator(); i.hasNext(); ) {
        Object keyObj = i.next();
        supplierVec.addElement(new KeyValuePairObject(keyObj, supplierMap.get(keyObj)));
      }
      //Fix for Defect # 1257
      Collections.sort(supplierVec, new StoreNameComparator());
      
      Map seasonMap = (Map)(supplierSeasonYearMap.get("season"));
      seasonVec.addElement(new KeyValuePairObject("", ""));
      for (Iterator i = seasonMap.keySet().iterator(); i.hasNext(); ) {
        Object keyObj = i.next();
        // Issue # 580
        seasonVec.addElement(new KeyValuePairObject(keyObj, keyObj + ": " + seasonMap.get(keyObj)));
      }
    }
    cbxSupplier.setModel(new DefaultComboBoxModel(supplierVec));
    cbxSeason.setModel(new DefaultComboBoxModel(seasonVec)); /*
         Map yearMap = (Map)(supplierSeasonYearMap.get("year"));
         Vector yearVec = new Vector();
         yearVec.addElement(new KeyValuePairObject(null, ""));
         for (Iterator i = yearMap.keySet().iterator(); i.hasNext();)
         {
         Object keyObj = i.next();
         yearVec.addElement(new KeyValuePairObject(keyObj, yearMap.get(keyObj).toString()));
         }
         cbxYear.setModel(new DefaultComboBoxModel(yearVec));
    */

  }

  //  public void itemSelected(CMSItem cmsItem)
  //  {
  //    if(cmsItem == null) return;
  //    if(theAppMgr.getStateObject("OPERATOR")==null
  //       ||
  //       ((CMSEmployee)theAppMgr.getStateObject("OPERATOR")).getExternalID().length()<1)
  //    return;
  //    theAppMgr.addStateObject("ITEM_LOOKUP", cmsItem);
  //    theAppMgr.fireButtonEvent("LOAD_MGMT");
  //  }
  public void start() {
    cmsItems = null;
    itemSearchString = null;
    if (supplierSeasonYearMap == null) {
      initSupplierSeasonYearMap();
      if (supplierSeasonYearMap == null) {
        theAppMgr.fireButtonEvent("PREV");
        return;
      }
    }
    try {
      //      if(theAppMgr.getStateObject("ITEM_LOOKUP")!=null)
      //      {
      //        theAppMgr.removeStateObject("ITEM_LOOKUP");
      //      }
      //      else
      {
        txtSKU.setText("");
        txtStyle.setText("");
        txtModel.setText("");
        txtFabric.setText("");
        txtColor.setText("");
        cbxSupplier.setSelectedIndex(0);
        //cbxYear.setSelectedIndex(0);
        txtYear.setText("");
        cbxSeason.setSelectedIndex(0);
        reset();
      }
      if (theAppMgr.getStateObject("OPERATOR") == null) {
        theAppMgr.showMenu(MenuConst.ITEM_LOOKUP, theOpr);
      } else {
        theAppMgr.showMenu(MenuConst.ITEM_LOOKUP, theOpr);
      }
      enterItem();
      restoreSearchResultState();
      this.addComponentListener(new java.awt.event.ComponentAdapter() {

        /**
         * put your documentation comment here
         * @param e
         */
        public void componentResized(ComponentEvent e) {
          resize();
        }
      });
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
              if (cmsItems == null || cmsItems.length < 1)
              {
                txtSKU.requestFocus();
                enterItem();
              }
              else
                pnlItemLookupList.requestFocus();
            }
          });
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
    pnlItemLookupList.clear();
    txtSKU.setEnabled(true);
    txtStyle.setEnabled(true);
    txtModel.setEnabled(true);
    txtFabric.setEnabled(true);
    txtColor.setEnabled(true);
    cbxSupplier.setEnabled(true);
    //cbxYear.setEnabled(true);
    txtYear.setEnabled(true);
    cbxSeason.setEnabled(true);
    enterItem();
  }

  /**
   * put your documentation comment here
   */
  private void enter() {
    theAppMgr.setSingleEditArea(res.getString(
        "Scan/Enter SKU, Style or (Model + additional field) and select 'Search'"));
  }

  public void enterItem() {
    //MP: Changed the string for loyalty card swipe
    theAppMgr.setSingleEditArea(res.getString(
        "Scan/Enter SKU, Style or (Model + additional field) and select 'Search'"), "ITEM"
        );
    theAppMgr.setEditAreaFocus();
  }

  /**
   * put your documentation comment here
   * @param sCommand
   * @param sInput
   */
  public void editAreaEvent(String sCommand, String sInput) {
    try {
      if (sCommand.equals("ITEM")) {
        ItemEntryParser itmParser = new ItemEntryParser();
        String retailBarcode = itmParser.getRetailBarcode(this, theAppMgr, sInput
            , new Integer(sInput.length()).toString());

        txtSKU.setText(retailBarcode);
        enter();
        }
    } catch (Exception ex) {
      theAppMgr.showExceptionDlg(ex);
      enterItem();
    }

  }

  /**
   * put your documentation comment here
   * @param anEvent
   */
  private void doPrevious(CMSActionEvent anEvent) {
    reset();
    if (cmsItems != null && cmsItems.length > 0) {
      cmsItems = null;
      theAppMgr.showMenu(MenuConst.ITEM_LOOKUP, theOpr);
      txtSKU.requestFocus();
      enterItem();
      anEvent.consume();
    }
  }

  /**
   * put your documentation comment here
   * @param anEvent
   */
  public void appButtonEvent(CMSActionEvent anEvent) {
    String sAction = anEvent.getActionCommand();
    boolean additionalFieldFlag = false;
    //String sQuery= "";
    if (sAction.equals("SEARCH")) {
      itemSearchString = new ItemSearchString();
      if (txtSKU.getText() != null && !txtSKU.getText().trim().equals("")) {
        itemSearchString.setSKU(txtSKU.getText().trim());
      } else if (txtStyle.getText() != null && !txtStyle.getText().trim().equals("")) {
        itemSearchString.setStyle(txtStyle.getText().trim());
      } else if (txtModel.getText() != null && !txtModel.getText().trim().equals("")) {
        if (txtFabric.getText() != null && !txtFabric.getText().trim().equals("")) {
          itemSearchString.setFabric(txtFabric.getText().trim());
          additionalFieldFlag = true;
        }
        if (txtColor.getText() != null && !txtColor.getText().trim().equals("")) {
          itemSearchString.setColor(txtColor.getText().trim());
          additionalFieldFlag = true;
        }
        if (this.cbxSupplier.getSelectedItem() != null
            && !cbxSupplier.getSelectedItem().toString().trim().equals("")) {
          itemSearchString.setSupplier(((KeyValuePairObject)cbxSupplier.getSelectedItem()).key.
              toString());
          additionalFieldFlag = true;
        }
        /*
         if(this.cbxYear.getSelectedItem()!=null && !cbxYear.getSelectedItem().toString().trim().equals(""))
         {
         itemSearchString.setYear(((KeyValuePairObject)cbxYear.getSelectedItem()).key.toString());
         additionalFieldFlag = true;
         }
         */
        if (txtYear.getText() != null && !txtYear.getText().trim().equals("")) {
          itemSearchString.setYear(txtYear.getText().trim());
          additionalFieldFlag = true;
        }
        if (this.cbxSeason.getSelectedItem() != null
            && !cbxSeason.getSelectedItem().toString().trim().equals("")) {
          itemSearchString.setSeason(((KeyValuePairObject)cbxSeason.getSelectedItem()).key.toString());
          additionalFieldFlag = true;
        }
        if (!additionalFieldFlag) {
          theAppMgr.showErrorDlg(res.getString("Enter SKU or Style or (Model + additional field) to search"));
          txtFabric.requestFocus();
          return;
        }
        itemSearchString.setModel(txtModel.getText().trim());
      } else {
        theAppMgr.showErrorDlg(res.getString("Enter SKU or Style or (Model + additional field) to search"));
        txtSKU.requestFocus();
        enterItem();
        return;
      }
      itemSearchString.setStore(((CMSStore)theAppMgr.getGlobalObject("STORE")).getId());
      itemSearchString.setBrand(((CMSStore)theAppMgr.getGlobalObject("STORE")).getBrandID());
      if (processQuery(itemSearchString) > 0) {
        theAppMgr.showMenu(MenuConst.ITEM_LOOKUP_FOUND, theOpr);
        theAppMgr.setSingleEditArea(res.getString("Select Item and then Menu Option"));
        txtSKU.setEnabled(false);
        txtStyle.setEnabled(false);
        txtModel.setEnabled(false);
        txtFabric.setEnabled(false);
        txtColor.setEnabled(false);
        cbxSupplier.setEnabled(false);
        //cbxYear.setEnabled(false);
        txtYear.setEnabled(false);
        cbxSeason.setEnabled(false);
      }
    } else if (sAction.equals("PREV")) {
      doPrevious(anEvent);
    } else if (sAction.equals("LOCATE")) {
      doItemLocate();
    } else if (sAction.equals("LAUNCH_BACKOFFICE")) {
      doLaunchBackOffice();
    } else if (sAction.equals("ITEM_DETAILS")) {
      doItemDetails();
    }
  }

  /**
   * put your documentation comment here
   */
  private void showNoItemFound() {
    theAppMgr.showErrorDlg(res.getString("Sorry, no Item found"));
  }

  /**
   * put your documentation comment here
   * @param cmsItems[]
   * @param doSort
   * @return
   */
  private int loadItems(CMSItem cmsItems[], boolean doSort) {
    pnlItemLookupList.clear();
    pnlItemLookupList.setItems(cmsItems, doSort);
    pnlItemLookupList.selectFirstRow();
    return cmsItems.length;
  }

  /**
   * put your documentation comment here
   * @param itemSearchString
   * @return
   */
  private int processQuery(ItemSearchString itemSearchString) {
    try {
      cmsItems = (CMSItem[])CMSItemHelper.findItems(theAppMgr, itemSearchString);
      if (cmsItems == null || cmsItems.length < 1) {
        showNoItemFound();
        txtSKU.requestFocus();
        enterItem();
        return 0;
      }
      return loadItems(cmsItems, true);
    } catch (Exception e) {
      e.printStackTrace();
      showNoItemFound();
      txtSKU.requestFocus();
    }
    return -1;
  }

  /**
   * put your documentation comment here
   * @return
   */
  public String getVersion() {
    return ("$Revision: 1.2 $");
  }

  /**
   * put your documentation comment here
   * @return
   */
  public String getScreenName() {
    return "Item Lookup";
  }

  /**
   * put your documentation comment here
   * @exception Exception
   */
  private void jbInit()
      throws Exception {
    pnlItemLookupList = new ItemLookupListPanel(theAppMgr);
    pnlSearch = new JPanel();
    lblSKU = new JCMSLabel();
    txtSKU = new JCMSTextField();
    lblOR = new JCMSLabel();
    lblStyle = new JCMSLabel();
    txtStyle = new JCMSTextField();
    lblModel = new JCMSLabel();
    txtModel = new JCMSTextField();
    lblFabric = new JCMSLabel();
    txtFabric = new JCMSTextField();
    lblColor = new JCMSLabel();
    txtColor = new JCMSTextField();
    lblSupplier = new JCMSLabel();
    //cbxSupplier = new JCMSComboBox();
    cbxSupplier = new CustomComboBox();
    lblYear = new JCMSLabel();
    //cbxYear = new JCMSComboBox();
    txtYear = new JCMSTextField();
    lblSeason = new JCMSLabel();
    //cbxSeason = new JCMSComboBox();
    cbxSeason = new CustomComboBox();
    GridBagLayout gridBagLayout1 = new GridBagLayout();
    BorderLayout borderLayout1 = new BorderLayout();
    this.getContentPane().setLayout(borderLayout1);
    this.getContentPane().add(pnlSearch, BorderLayout.NORTH);
    this.getContentPane().add(pnlItemLookupList, BorderLayout.CENTER);
    this.setLayout(borderLayout1);
    pnlSearch.setPreferredSize(new Dimension(833, 130));
    pnlSearch.setLayout(gridBagLayout1);
    lblSKU.setLabelFor(txtSKU);
    lblSKU.setText(res.getString("SKU/Ref. No."));
    txtSKU.setText("");
    lblOR.setLabelFor(null);
    lblOR.setText(res.getString("OR"));
    lblStyle.setLabelFor(txtStyle);
    lblStyle.setText(res.getString("Style"));
    txtStyle.setText("");
    lblModel.setLabelFor(txtModel);
    lblModel.setText(res.getString("Model"));
    txtModel.setText("");
    lblFabric.setLabelFor(txtFabric);
    lblFabric.setText(res.getString("Fabric"));
    txtFabric.setText("");
    lblColor.setLabelFor(txtColor);
    lblColor.setText(res.getString("Color"));
    txtColor.setText("");
    lblSupplier.setLabelFor(cbxSupplier);
    lblSupplier.setText(res.getString("Supplier"));
    //lblYear.setLabelFor(cbxYear);
    lblYear.setLabelFor(txtYear);
    lblYear.setText(res.getString("Year"));
    lblSeason.setLabelFor(cbxSeason);
    lblSeason.setText(res.getString("Season"));
    this.add(pnlSearch, BorderLayout.NORTH);
    this.setBackground(theAppMgr.getBackgroundColor());
    pnlSearch.setBackground(theAppMgr.getBackgroundColor());
    pnlItemLookupList.setAppMgr(theAppMgr);
    lblSKU.setAppMgr(theAppMgr);
    lblSKU.setFont(theAppMgr.getTheme().getLabelFont());
    txtSKU.setAppMgr(theAppMgr);
    txtSKU.setFont(theAppMgr.getTheme().getTextFieldFont());
    lblOR.setAppMgr(theAppMgr);
    lblOR.setFont(theAppMgr.getTheme().getLabelFont());
    lblStyle.setAppMgr(theAppMgr);
    lblStyle.setFont(theAppMgr.getTheme().getLabelFont());
    txtStyle.setAppMgr(theAppMgr);
    txtStyle.setFont(theAppMgr.getTheme().getTextFieldFont());
    lblModel.setAppMgr(theAppMgr);
    lblModel.setFont(theAppMgr.getTheme().getLabelFont());
    txtModel.setAppMgr(theAppMgr);
    txtModel.setFont(theAppMgr.getTheme().getTextFieldFont());
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
    cbxSupplier.setAppMgr(theAppMgr);
    lblYear.setAppMgr(theAppMgr);
    lblYear.setFont(theAppMgr.getTheme().getLabelFont());
    //cbxYear.setAppMgr(theAppMgr);
    txtYear.setAppMgr(theAppMgr);
    txtYear.setFont(theAppMgr.getTheme().getTextFieldFont());
    txtYear.setDocument(new TextFilter(TextFilter.NUMERIC, 4));
    lblSeason.setAppMgr(theAppMgr);
    lblSeason.setFont(theAppMgr.getTheme().getLabelFont());
    cbxSeason.setAppMgr(theAppMgr);
    pnlSearch.add(lblSKU
        , new GridBagConstraints(0, 0, 2, 1, 0.0, 0.0, GridBagConstraints.WEST
        , GridBagConstraints.NONE, new Insets(0, 12, 0, 4), 4, 4));
    pnlSearch.add(txtSKU
        , new GridBagConstraints(2, 0, 2, 1, 1.0, 0.0, GridBagConstraints.WEST
        , GridBagConstraints.HORIZONTAL, new Insets(0, 4, 0, 4), 4, 4));
    pnlSearch.add(lblOR
        , new GridBagConstraints(4, 0, 1, 1, 0.5, 0.0, GridBagConstraints.CENTER
        , GridBagConstraints.NONE, new Insets(0, 8, 0, 8), 4, 4));
    pnlSearch.add(lblStyle
        , new GridBagConstraints(5, 0, 2, 1, 0.0, 0.0, GridBagConstraints.WEST
        , GridBagConstraints.NONE, new Insets(0, 8, 0, 4), 4, 4));
    pnlSearch.add(txtStyle
        , new GridBagConstraints(7, 0, 2, 1, 1.0, 0.0, GridBagConstraints.WEST
        , GridBagConstraints.HORIZONTAL, new Insets(0, 4, 0, 8), 4, 4));
    pnlSearch.add(lblModel
        , new GridBagConstraints(0, 1, 3, 1, 0.0, 0.0, GridBagConstraints.SOUTHWEST
        , GridBagConstraints.NONE, new Insets(4, 12, 0, 4), 4, 0));
    pnlSearch.add(txtModel
        , new GridBagConstraints(0, 2, 3, 1, 1.0, 0.0, GridBagConstraints.NORTHWEST
        , GridBagConstraints.HORIZONTAL, new Insets(0, 8, 4, 4), 4, 4));
    pnlSearch.add(lblFabric
        , new GridBagConstraints(3, 1, 3, 1, 0.0, 0.0, GridBagConstraints.SOUTHWEST
        , GridBagConstraints.NONE, new Insets(4, 8, 0, 4), 4, 0));
    pnlSearch.add(txtFabric
        , new GridBagConstraints(3, 2, 3, 1, 1.0, 0.0, GridBagConstraints.NORTHWEST
        , GridBagConstraints.HORIZONTAL, new Insets(0, 4, 4, 4), 4, 4));
    pnlSearch.add(lblColor
        , new GridBagConstraints(6, 1, 3, 1, 0.0, 0.0, GridBagConstraints.SOUTHWEST
        , GridBagConstraints.NONE, new Insets(4, 8, 0, 8), 4, 0));
    pnlSearch.add(txtColor
        , new GridBagConstraints(6, 2, 3, 1, 1.0, 0.0, GridBagConstraints.NORTHWEST
        , GridBagConstraints.HORIZONTAL, new Insets(0, 4, 4, 8), 4, 4));
    pnlSearch.add(lblSupplier
        , new GridBagConstraints(0, 3, 3, 1, 0.0, 0.0, GridBagConstraints.SOUTHWEST
        , GridBagConstraints.NONE, new Insets(4, 12, 0, 4), 4, 0));
    pnlSearch.add(cbxSupplier
        , new GridBagConstraints(0, 4, 3, 1, 1.0, 0.0, GridBagConstraints.NORTHWEST
        , GridBagConstraints.HORIZONTAL, new Insets(0, 8, 4, 4), 4, 0));
    pnlSearch.add(lblYear
        , new GridBagConstraints(3, 3, 3, 1, 0.0, 0.0, GridBagConstraints.SOUTHWEST
        , GridBagConstraints.NONE, new Insets(4, 8, 0, 4), 4, 0));
    //    pnlSearch.add(cbxYear,  new GridBagConstraints(3, 4, 3, 1, 1.0, 0.0
    //            ,GridBagConstraints.NORTHWEST, GridBagConstraints.HORIZONTAL, new Insets(0, 4, 4, 4), 4, 0));
    pnlSearch.add(txtYear
        , new GridBagConstraints(3, 4, 3, 1, 1.0, 0.0, GridBagConstraints.NORTHWEST
        , GridBagConstraints.HORIZONTAL, new Insets(0, 4, 4, 4), 4, 4));
    pnlSearch.add(lblSeason
        , new GridBagConstraints(6, 3, 3, 1, 0.0, 0.0, GridBagConstraints.SOUTHWEST
        , GridBagConstraints.NONE, new Insets(4, 8, 0, 8), 4, 0));
    pnlSearch.add(cbxSeason
        , new GridBagConstraints(6, 4, 3, 1, 1.0, 0.0, GridBagConstraints.NORTHWEST
        , GridBagConstraints.HORIZONTAL, new Insets(0, 4, 4, 8), 4, 0));
    /*
     seasonComboPanel = new CustomComboPanel();
     //CardLayout cardLayout = new CardLayout();
     seasonComboPanel.setLayout(null);
     seasonComboPanel.add(cbxSeason);
     cbxSeason.setBounds(0, 0, (int)(95*r), (int)(30*r));
     pnlSearch.add(seasonComboPanel,  new GridBagConstraints(6, 4, 3, 1, 1.0, 0.0
     ,GridBagConstraints.NORTHWEST, GridBagConstraints.HORIZONTAL, new Insets(0, 4, 4, 8), 4, 0));
     //cardLayout.first(seasonComboPanel);
     */
    txtSKU.setDocument(new TextFilter(ALPHA_NUMERIC_SPEC, 128));
    txtStyle.setDocument(new TextFilter(ALPHA_NUMERIC_SPEC, 10));
    txtModel.setDocument(new TextFilter(ALPHA_NUMERIC_SPEC, 50));
    txtFabric.setDocument(new TextFilter(ALPHA_NUMERIC_SPEC, 50));
    txtColor.setDocument(new TextFilter(ALPHA_NUMERIC_SPEC, 6));
  }

  /**
   * put your documentation comment here
   */
  public void resize() {
    double r = com.chelseasystems.cr.swing.CMSApplet.r;
    lblSKU.setPreferredSize(new Dimension((int)(55 * r), (int)(30 * r)));
    txtSKU.setPreferredSize(new Dimension((int)(75 * r), (int)(30 * r)));
    lblOR.setPreferredSize(new Dimension((int)(55 * r), (int)(30 * r)));
    lblStyle.setPreferredSize(new Dimension((int)(55 * r), (int)(30 * r)));
    txtStyle.setPreferredSize(new Dimension((int)(75 * r), (int)(30 * r)));
    lblModel.setPreferredSize(new Dimension((int)(55 * r), (int)(30 * r)));
    txtModel.setPreferredSize(new Dimension((int)(95 * r), (int)(30 * r)));
    lblFabric.setPreferredSize(new Dimension((int)(55 * r), (int)(30 * r)));
    txtFabric.setPreferredSize(new Dimension((int)(95 * r), (int)(30 * r)));
    lblColor.setPreferredSize(new Dimension((int)(55 * r), (int)(30 * r)));
    txtColor.setPreferredSize(new Dimension((int)(95 * r), (int)(30 * r)));
    lblSupplier.setPreferredSize(new Dimension((int)(55 * r), (int)(30 * r)));
    //cbxSupplier.setPreferredSize(new Dimension((int)(95*r), (int)(30*r)));
    lblYear.setPreferredSize(new Dimension((int)(55 * r), (int)(30 * r)));
    //cbxYear.setPreferredSize(new Dimension((int)(95*r), (int)(30*r)));
    txtYear.setPreferredSize(new Dimension((int)(95 * r), (int)(30 * r)));
    lblSeason.setPreferredSize(new Dimension((int)(55 * r), (int)(30 * r)));
    //cbxSeason.setPreferredSize(new Dimension((int)(95*r), (int)(30*r)));
  }

  /**
   * put your documentation comment here
   * @param e
   */
  public void pageDown(MouseEvent e) {
    int selectedRow = pnlItemLookupList.getAddressModel().getAllRows().indexOf(pnlItemLookupList.
        getSelectedItem());
    if (selectedRow < 0)
      selectedRow = pnlItemLookupList.getAddressModel().getLastSelectedItemRow();
    pnlItemLookupList.nextPage();
    theAppMgr.showPageNumber(e, pnlItemLookupList.getCurrentPageNumber() + 1
        , pnlItemLookupList.getTotalPages());
    pnlItemLookupList.selectRowIfInCurrentPage(selectedRow);
  }

  /**
   * put your documentation comment here
   * @param e
   */
  public void pageUp(MouseEvent e) {
    int selectedRow = pnlItemLookupList.getAddressModel().getAllRows().indexOf(pnlItemLookupList.
        getSelectedItem());
    if (selectedRow < 0)
      selectedRow = pnlItemLookupList.getAddressModel().getLastSelectedItemRow();
    pnlItemLookupList.prevPage();
    theAppMgr.showPageNumber(e, pnlItemLookupList.getCurrentPageNumber() + 1
        , pnlItemLookupList.getTotalPages());
    pnlItemLookupList.selectRowIfInCurrentPage(selectedRow);
  }

  /**
   * put your documentation comment here
   */
  public void doItemLocate() {
    CMSItem cmsItem = pnlItemLookupList.getSelectedItem();
    if (cmsItem == null)
      return;
    ItemSearchString itemSearchString = new ItemSearchString();
    itemSearchString.setStyle(cmsItem.getStyleNum());
    itemSearchString.setModel(cmsItem.getModel());
    itemSearchString.setProductNum(cmsItem.getProductNum());
    itemSearchString.setSupplier(cmsItem.getSupplierId());
    itemSearchString.setFabric(cmsItem.getFabric());
    itemSearchString.setColor(cmsItem.getColorId());
    itemSearchString.setYear(cmsItem.getForTheYear());
    itemSearchString.setSeason(cmsItem.getSeason());
    itemSearchString.setIsAdvancedSearch(true);
    itemSearchString.setColorDesc(cmsItem.getItemDetail().getColorDesc());
    itemSearchString.setSeasonDesc(cmsItem.getItemDetail().getSeasonDesc());
    itemSearchString.setSupplierName(cmsItem.getItemDetail().getSupplierName());
    itemSearchString.setStore(((CMSStore)theAppMgr.getGlobalObject("STORE")).getId());
    //itemSearchString.setYearDesc((String)(((Map)supplierSeasonYearMap.get("year")).get(cmsItem.getForTheYear().trim())));
    //itemSearchString.setYearDesc(cmsItem.getForTheYear());
    theAppMgr.addStateObject("ITEM_LOCATE_SEARCHSTRING", itemSearchString);
    //Store the state of the applet
    theAppMgr.addStateObject("ITEM_LOOOKUP_SEARCHSTRING", this.itemSearchString); //"this" is used to get original searchstring
    theAppMgr.addStateObject("ITEM_LOOOKUP_SEARCHRESULT", cmsItems);
    theAppMgr.addStateObject("ITEM_LOOOKUP_SELECTEDSKU", pnlItemLookupList.getSelectedItem().getId());
    theAppMgr.addStateObject("ITEM_LOOOKUP_SORTCOLUMN"
        , new Integer(pnlItemLookupList.getAddressModel().getCurrentSortColumnAndType()));
  }

  /**
   * put your documentation comment here
   */
  public void doLaunchBackOffice() {
    CMSItem cmsItem = pnlItemLookupList.getSelectedItem();
    if (cmsItem == null)
      return;
    String[] cmdarray = {new ConfigMgr("item.cfg").getString("BACKOFFICE_SCRIPT"), cmsItem.getId()
        , cmsItem.getStoreId()
    };
    try {
      Runtime.getRuntime().exec(cmdarray);
    } catch (IOException ex) {
      ex.printStackTrace();
    }
  }

  /**
   * put your documentation comment here
   */
  public void doItemDetails() {
    CMSItem cmsItem = pnlItemLookupList.getSelectedItem();
    if (cmsItem == null)
      return;
    CMSCompositePOSTransaction dummyCompositePOSTransaction = new CMSCompositePOSTransaction((
        CMSStore)theAppMgr.getGlobalObject("STORE"));
    POSLineItem dummyPOSLineItem = null;
    try {
      dummyPOSLineItem = dummyCompositePOSTransaction.addSaleItem(cmsItem);
    } catch (BusinessRuleException ex) {
      System.out.println("!!! ------------------ BusinessRuleException is: " + ex.getMessage());
    }
    theAppMgr.addStateObject("ItemLookUp_POSLineItem", dummyPOSLineItem);
    theAppMgr.addStateObject("ITEM_LOOOKUP_SEARCHSTRING", this.itemSearchString); //"this" is used to get original searchstring
    theAppMgr.addStateObject("ITEM_LOOOKUP_SEARCHRESULT", cmsItems);
    theAppMgr.addStateObject("ITEM_LOOOKUP_SELECTEDSKU", pnlItemLookupList.getSelectedItem().getId());
    theAppMgr.addStateObject("ITEM_LOOOKUP_SORTCOLUMN"
        , new Integer(pnlItemLookupList.getAddressModel().getCurrentSortColumnAndType()));
  }

  /**
   * put your documentation comment here
   */
  private void restoreSearchResultState() {
    itemSearchString = (ItemSearchString)theAppMgr.getStateObject("ITEM_LOOOKUP_SEARCHSTRING");
    cmsItems = (CMSItem[])theAppMgr.getStateObject("ITEM_LOOOKUP_SEARCHRESULT");
    String selectedSKU = ((String)theAppMgr.getStateObject("ITEM_LOOOKUP_SELECTEDSKU"));
    Integer sortColumn = ((Integer)theAppMgr.getStateObject("ITEM_LOOOKUP_SORTCOLUMN"));
    theAppMgr.removeStateObject("ITEM_LOOOKUP_SEARCHSTRING");
    theAppMgr.removeStateObject("ITEM_LOOOKUP_SEARCHRESULT");
    theAppMgr.removeStateObject("ITEM_LOOOKUP_SELECTEDSKU");
    theAppMgr.removeStateObject("ITEM_LOOOKUP_SORTCOLUMN");
    if (itemSearchString == null)
      return;
    txtSKU.setText(itemSearchString.getSKU());
    txtStyle.setText(itemSearchString.getStyle());
    txtModel.setText(itemSearchString.getModel());
    txtFabric.setText(itemSearchString.getFabric());
    txtColor.setText(itemSearchString.getColor());
    if (itemSearchString.getSupplier() != null)
      cbxSupplier.setSelectedItem(new KeyValuePairObject(itemSearchString.getSupplier()
          , ((Map)supplierSeasonYearMap.get("supplier")).get(itemSearchString.getSupplier())));
    //cbxYear.setSelectedItem(((Map)supplierSeasonYearMap.get("year")).get(itemSearchString.getYear()));
    txtYear.setText(itemSearchString.getYear());
    if (itemSearchString.getSeason() != null)
      cbxSeason.setSelectedItem(new KeyValuePairObject(itemSearchString.getSeason()
          , itemSearchString.getSeason() + ": "
          + ((Map)supplierSeasonYearMap.get("season")).get(itemSearchString.getSeason())));
    if (cmsItems == null || cmsItems.length < 1)
      return;
    loadItems(cmsItems, false);
    theAppMgr.showMenu(MenuConst.ITEM_LOOKUP_FOUND, theOpr);
    txtSKU.setEnabled(false);
    txtStyle.setEnabled(false);
    txtModel.setEnabled(false);
    txtFabric.setEnabled(false);
    txtColor.setEnabled(false);
    cbxSupplier.setEnabled(false);
    //cbxYear.setEnabled(false);
    txtYear.setEnabled(false);
    cbxSeason.setEnabled(false);
    int sortColumnIntValue = Integer.MAX_VALUE;
    if (sortColumn != null)
      sortColumnIntValue = sortColumn.intValue();
    int selectedItemRowNew = pnlItemLookupList.getAddressModel().sortItems(sortColumnIntValue
        , selectedSKU);
    if (selectedItemRowNew > -1)
      pnlItemLookupList.selectRow(selectedItemRowNew);
    theAppMgr.setSingleEditArea(res.getString("Select Item and then Menu Option"));
    //       if(selectedRowIndex==null || selectedRowIndex.intValue()<0)
    //           return;
    //
    //       pnlItemLookupList.selectRow(selectedRowIndex.intValue());
  }

  private class KeyValuePairObject {
    Object key;
    Object value;

    /**
     * put your documentation comment here
     * @param     Object key
     * @param     Object value
     */
    KeyValuePairObject(Object key, Object value) {
      this.key = key;
      this.value = value;
    }

    /**
     * put your documentation comment here
     * @return
     */
    public String toString() {
      return value.toString();
    }

    /**
     * put your documentation comment here
     * @param obj
     * @return
     */
    public boolean equals(Object obj) {
      if (obj == null || !(obj instanceof KeyValuePairObject) || key == null || value == null)
        return false;
      else
        return key.equals(((KeyValuePairObject)obj).key)
            && value.equals(((KeyValuePairObject)obj).value);
    }
  }


  private class CustomComboBox extends JCMSComboBox {

    /**
     * put your documentation comment here
     * @return
     */
    public Dimension getPreferredSize() {
      return new Dimension((int)(95 * r), (int)(30 * r));
    }

    /**
     * put your documentation comment here
     * @return
     */
    public Dimension getMaximumSize() {
      return new Dimension((int)(95 * r), (int)(30 * r));
    }

    /**
     * put your documentation comment here
     * @return
     */
    public Dimension getMinimumSize() {
      return new Dimension((int)(95 * r), (int)(30 * r));
    }
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

  
  private class StoreNameComparator implements Comparator {
	  public int compare(Object obj1, Object obj2) {
	        String value1 = ((KeyValuePairObject) obj1).value.toString().toUpperCase();
	        String value2 = ((KeyValuePairObject) obj2).value.toString().toUpperCase();        
	        return value1.compareTo(value2);
	  }
  }
}

