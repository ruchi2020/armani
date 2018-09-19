/*
 * @copyright (c) 2001 Chelsea Market Systems LLC
 *
 * formatted with JxBeauty (c) johann.langhofer@nextra.at
 */


package com.chelseasystems.cs.swing.model;

import java.util.Vector;
import java.util.Hashtable;
import java.util.Enumeration;
import javax.swing.table.TableColumn;

import com.chelseasystems.cr.config.ConfigMgr;
import com.chelseasystems.cr.item.Item;
import com.chelseasystems.cr.pos.POSLineItem;
import com.chelseasystems.cr.pos.SaleLineItem;
import com.chelseasystems.cr.pos.LayawayLineItem;
import com.chelseasystems.cr.pos.PaymentTransactionAppModel;
import com.chelseasystems.cr.swing.ScrollableTableModel;
import com.chelseasystems.cr.swing.bean.JCMSTable;
import com.chelseasystems.cs.item.CMSItem;
import com.chelseasystems.cs.util.LineItemPOSUtil;


/**
 * Used to render gift certificate entries.   Will allow operator to select specific
 * lineItems for gift certificate report.
 * @author      Moises G. Solis
 */
public class GiftReceiptModel extends ScrollableTableModel {
  public static final int SELECT = 0;
  public static final int CODE = 1;
  public static final int DESC = 2;
  //ISSUE #1887
  public static final int ITEMNAME = 3;
  private PaymentTransactionAppModel txn;
  private java.util.Vector entries;

  private static ConfigMgr itemCfg = new ConfigMgr("item.cfg");

  /**
   * Thie table model is used to render GiftReceipts.
   */
  public GiftReceiptModel(PaymentTransactionAppModel txn) {
    java.util.ResourceBundle res = com.chelseasystems.cr.util.ResourceManager.getResourceBundle();
    this.setColumnIdentifiers(new String[] {res.getString("Select"), res.getString("Item Code")
        , res.getString("Item Class")
    });
    this.txn = txn;
    extractUniqueItems();
    // Mark all items selected
    setAllItemSelection(new Boolean(true));
  }

  /**
   */
  private void extractUniqueItems() {
    int count = 0;
    POSLineItem[] lineItems = txn.getLineItemsArray();
    Hashtable ht = new Hashtable();
    entries = new java.util.Vector();
    for (int i = 0; i < lineItems.length; i++) {
      if (lineItems[i] instanceof SaleLineItem) {
        Item item = lineItems[i].getItem();
        //String id = (String)ht.get(item.getId());
        // Display all the Items
        // Remove the Unique check
        //if (id == null) {
          //ht.put(((CMSItem)item).getBarCode(), getItemClassDesc(lineItems[i]));
        //Gift Receipt should not contain Shipping charge 
        if (!lineItems[i].isMiscItem() || 
        	(LineItemPOSUtil.isNotOnFileItem(lineItems[i].getItem().getId()))){
//        		((lineItems[i].getItem().getId()).equals(itemCfg.getString("NOTONFILE.BASE_ITEM")))) {
        	//MB: 11/29/2007 
        	//Issue:1887 (Set Item Name)--- Begin----
        	//Commented and replaced to create GiftReceiptEntry object with extra arguement (Item name)
        		//addNewItem(new GiftReceiptEntry(((CMSItem)item).getBarCode(), getItemClassDesc(lineItems[i]))));
        		addNewItem(new GiftReceiptEntry(((CMSItem)item).getBarCode(), getItemClassDesc(lineItems[i]),getItemDesc(lineItems[i])));
          //Issue:1887 (Set Item Name)--- Begin----        	
        }
      }
    }
    // Display all the Items
    // Remove the Unique check
    //convertToVector(ht);
  }

  /**
   * @param ht
   */
  private void convertToVector(Hashtable ht) {
    entries = new java.util.Vector();
    Enumeration enm = ht.keys();
    while (enm.hasMoreElements()) {
      String itemId = (String)enm.nextElement();
      addNewItem(new GiftReceiptEntry(itemId, (String)ht.get(itemId)));
    }
  }

  /**
   * @param entry
   */
  public void addNewItem(GiftReceiptEntry entry) {
    entries.addElement(entry);
    super.addRow(entry);
  }

  /**
   * Inner class that represents a gift line item entry.
   */
  /*public class GiftReceiptEntry {
   private Boolean selected;
   private String itemId;
   private String description;
   /**
    * @param SaleLineItem line
    */
   /*       public GiftReceiptEntry (String itemId, String description) {
    this.selected = new Boolean(false);
    this.itemId = itemId;
    this.description = description;
    }
    public String toString() {
    return (itemId + " : " + description + " : " + selected);
    }
    public void setSelected(Boolean selected) {
    this.selected = selected;
    }
    /**
     * @return boolean
     */
    /*      public boolean isSelected() {
     return selected.booleanValue();
     }
     /**
      * @return Boolean selected
      */
     /*
      public Boolean getSelected() {
      return selected;
      }
      /**
       * @return String
       */
      /*  public String getItemId() {
       return itemId;
       }
       /**
        * @return String
        */
       /*   public String getDescription() {
        return description;
        }
        }
        /**
         * @param int
         * @return String itemId
         */
        public String getItemId(int row) {
          GiftReceiptEntry entry = (GiftReceiptEntry)getRowInPage(row);
          return entry.getItemId();
        }

  /**
   * @param int
   * @return String Description
   */
  public String getDescription(int row) {
    GiftReceiptEntry entry = (GiftReceiptEntry)getRowInPage(row);
    return entry.getDescription();
  }

  /**Issue #1887
   * @param int
   * @return String Description
   */
  public String getItemName(int row) {
    GiftReceiptEntry entry = (GiftReceiptEntry)getRowInPage(row);
    return entry.getItemName();
  }

    
  
  
  
  /**
   * Determines if table row was selected
   * @param int
   * @return boolean
   */
  public boolean isSelected(int row) {
    if(row < 0)
      return false;
    GiftReceiptEntry entry = (GiftReceiptEntry)getRowInPage(row);
    return entry.isSelected();
  }

  /**
   * Allows for selecting checkbox.
   */
  public void setSelected(int row, Boolean select) {
    if(row < 0)
      return;
    GiftReceiptEntry rowEntry = (GiftReceiptEntry)getRowInPage(row);
    rowEntry.setSelected(select);
  }

  /**
   * Allows for selecting checkbox for all lines.
   */
  public void setAllItemSelection(Boolean select) {
    Vector vRows = this.getAllRows();
    for (int i = 0; i < vRows.size(); i++) {
      GiftReceiptEntry rowEntry = (GiftReceiptEntry)vRows.elementAt(i);
      rowEntry.setSelected(select);
    }
  }

  /**
   * @param row
   * @param column
   * @return Object
   */
  public Object getValueAt(int row, int column) {
    Vector vTemp = getCurrentPage();
    GiftReceiptEntry entry = (GiftReceiptEntry)vTemp.elementAt(row);
    switch (column) {
      case SELECT:
        return entry.getSelected();
      case CODE:
        return entry.getItemId();
      case DESC:
        return entry.getDescription();
        //ISSUE # 1887
      case ITEMNAME:
      	return entry.getItemName();
      default:
        return " ";
    }
  }

  /**
   * @param int
   * @return GiftReceiptEntry
   */
  public GiftReceiptEntry getGiftEntry(int row) {
    GiftReceiptEntry entry = (GiftReceiptEntry)getRowInPage(row);
    return entry;
  }

  /**
   * @return boolean line items in model contain any selected entries;
   */
  public boolean containsSelectedItems() {
    Enumeration enm = entries.elements();
    while (enm.hasMoreElements()) {
      GiftReceiptEntry entry = (GiftReceiptEntry)enm.nextElement();
      if (entry.isSelected()) {
        return true;
      }
    }
    return false;
  }

  /**
   * @return
   */
  public GiftReceiptEntry[] getSelectedEntries() {
    Vector v = new Vector();
    if (containsSelectedItems()) {
      Enumeration enm = entries.elements();
      while (enm.hasMoreElements()) {
        GiftReceiptEntry entry = (GiftReceiptEntry)enm.nextElement();
        if (entry.isSelected()) {
          v.addElement(entry);
        }
      }
    }
    return (GiftReceiptEntry[])v.toArray(new GiftReceiptEntry[v.size()]);
  }

  /**
   * @return int
   */
  public int getColumnCount() {
    return 3;
  }

  /**
   * @param row
   * @param column
   * @return boolean
   */
  public boolean isCellEditable(int row, int column) {
    return false;
  }

  /**
   * put your documentation comment here
   * @param lineItem
   * @return
   */
  public String getItemClassDesc(POSLineItem lineItem) {
    String itemDesc = lineItem.getItemDescription();
    String classDesc = ((CMSItem)lineItem.getItem()).getItemDetail().getClassDesc();
    String itemId = ((CMSItem)lineItem.getItem()).getId();
    try {
      if (lineItem.isMiscItem())
        return itemDesc;
      if (lineItem.getLineItemDetailsArray()[0].getGiftCertificateId() != null)
        return itemDesc;
      if (classDesc != null && classDesc.trim().length() > 0)
        return classDesc;
      else
        return itemDesc;
    } catch (Exception exp) {
      exp.printStackTrace();
      return itemId;
    }   
  
  }
  
  
  public String getItemDesc(POSLineItem lineItem) {
		String itemDesc = lineItem.getItemDescription();
		String itemId = ((CMSItem) lineItem.getItem()).getId();
		try {
			if (lineItem.isMiscItem()) {
				return itemDesc;
			}
			if (lineItem.getLineItemDetailsArray()[0].getGiftCertificateId() != null) {
				return itemDesc;
			}
			if (itemId != null && itemDesc != null) {
				String barCode = ((CMSItem) lineItem.getItem()).getBarCode();
				if (barCode == null || barCode.length() == 0) {
					barCode = itemId;
				}
				return barCode + " " + itemDesc;
			} else if (itemId != null) {
				return itemId;
			} else {
				return itemDesc;
			}
		} catch (Exception exp) {
			exp.printStackTrace();
			return itemId;
		}
	}
  

  
  
  
  
}

