/*
 History:
 +------+------------+-----------+-----------+----------------------------------------------------+
 | Ver# | Date       | By        | Defect #  | Description                                        |
 +------+------------+-----------+-----------+----------------------------------------------------+
 | 4    | 05/14/2005 | Vikram    | N/A       | Updated for POS_104665_FS_ItemLookup_Rev1          |
 +------+------------+-----------+-----------+----------------------------------------------------+
 | 1    | 04/27/2005 | Vikram    | N/A       | POS_104665_FS_ItemLookup_Rev1                      |
 --------------------------------------------------------------------------------------------------
 *
 * formatted with JxBeauty (c) johann.langhofer@nextra.at
 */


package com.chelseasystems.cs.swing.model;

import com.chelseasystems.cr.config.ConfigMgr;
import com.chelseasystems.cr.swing.*;
import com.chelseasystems.cs.dataaccess.artsoracle.databean.MaPrcItmOracleBean;
import com.chelseasystems.cs.item.*;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.TreeMap;
import java.util.TreeSet;
import java.util.Hashtable;
import java.util.Vector;


/**
 * <p>Title:ItemLookupDetailModel </p>
 * <p>Description: Model Object for ItemLookupDetailPanel</p>
 * <p>Copyright: Copyright (c) 2005</p>
 * <p>Company: SkillnetInc</p>
 * @author Vikram Mundhra
 * @version 1.0
 */
public class ItemLookupDetailModel extends ScrollableTableModel {
  private String[] COLUMN_NAMES = null;
  private String[] FIXED_COLUMN_NAMES = {"HDR_Store", "HDR_Color"};
  public static final int STORE = 0;
  public static final int COLOR = 1;
  protected Vector itemRowKeyVec;
  protected Vector sizeVec;
  protected Hashtable itemRowSizeHash;
  private Hashtable sizeDescHash;
  private CMSItem[][] itemMatrix;
  private String homeStoreId;
  private Hashtable itemRowItemsArrayHash;
  private Vector itemRowKeyVecMain;
  private Vector itemColor;
  int iCurrentColorIndex = -1;
  private Vector otherColors = null;
  private int iNoOfFixedColumns = 0;
  private boolean bUniqueColorAvailable = false;    
  private static ConfigMgr configMgr = new ConfigMgr("item.cfg");  
  
  /**
   * put your documentation comment here
   * @param   CMSItem[] cmsItems
   * @param   String homeStoreId
   */
  public ItemLookupDetailModel(CMSItem[] cmsItems, String homeStoreId, String sColorId) {
    COLUMN_NAMES =  getFixedColumnNames();
    iNoOfFixedColumns = getColumnNames().length;
    java.util.ResourceBundle res = com.chelseasystems.cr.util.ResourceManager.getResourceBundle();
    this.homeStoreId = homeStoreId;
    itemRowSizeHash = new Hashtable();
    TreeSet sizeSet = new TreeSet();
    sizeDescHash = new Hashtable();
    TreeSet itemRowKeySet = new TreeSet();
    itemRowKeyVec = new Vector();
    itemRowKeyVecMain = new Vector();
    itemColor = new Vector();
    sizeVec = new Vector();
	  otherColors = new Vector();
    for (int i = 0; i < cmsItems.length; i++) {
      addItem(cmsItems[i], itemRowKeySet, sizeSet);
      //System.out.println("---> CMSItem: Store: " + cmsItems[i].getStoreId() +" ColorID: " + cmsItems[i].getColorId() + " Desc: " + cmsItems[i].getItemDetail().getColorDesc() +" Name: "+cmsItems[i].getStoreName() );
      sizeDescHash.put(getSizeKeyString(cmsItems[i]), getSizeKeyString(cmsItems[i]));
    }
    itemRowKeyVec.addAll(itemRowKeySet);
    //Keep the whole list of found items
    itemRowKeyVecMain.addAll(itemRowKeySet);    

    sizeVec.addAll(sizeSet);
    
    if(isItemSearchInMultipleStore()) {
    	//ItemSearchString itmSearchStr = (ItemSearchString)theAppMgr.getStateObject("ITEM_LOCATE_SEARCHSTRING");        
    	//sColorId = itmSearchStr.getColor();
    	if(sColorId == null && ( cmsItems != null && cmsItems.length > 0) ) {
    		sColorId = cmsItems[0].getColorId();
    	}
    	generateItemMatrix(sColorId);
    }else{
    	generateItemMatrix();
    }
    resetColumnNames();
    //String sIdentiFiers[] = new String[COLUMN_NAMES.length];
    String colNames[] = getColumnNames();
    String sIdentiFiers[] = new String[colNames.length];
    for (int iCtr = 0; iCtr < colNames.length; iCtr++) {
      if (iCtr < iNoOfFixedColumns) {
    	  sIdentiFiers[iCtr] = res.getString(colNames[iCtr]);
      } else {
    	  sIdentiFiers[iCtr] = colNames[iCtr];
      }
    }
    //for(int i=0; i<sIdentiFiers.length; i++){
    //	System.out.println("Columns being set to ScrollableTableModel: ["+i+"] - " + sIdentiFiers[i]);
    //}
    this.setColumnIdentifiers(sIdentiFiers);
  }

	

	/**
	 * put your documentation comment here
	 */
	protected void resetColumnNames() {
		String colNames[] = getColumnNames();
		if (colNames == null || sizeVec == null)
			return;
		int iFixedColSize = getNoOfFixedColumns();
		String[] new_col_names = new String[iFixedColSize + sizeVec.size()];		
		int i = 0;
		for (i = 0; i < iFixedColSize; i++) {
			new_col_names[i] = colNames[i];			
		}
		for (i = 0; i < sizeVec.size(); i++) {
			new_col_names[i + iFixedColSize] = (String)sizeDescHash.get(sizeVec.elementAt(i));			
		}
		COLUMN_NAMES = new_col_names;
		//setColumnNames(new_col_names);
	}

  /**
   * put your documentation comment here
   * @param cmsItem
   * @param itemRowKeySet
   * @param sizeSet
   */
  public void addItem(CMSItem cmsItem, TreeSet itemRowKeySet, TreeSet sizeSet) {
	  
    if (cmsItem == null)
      return;
    sizeSet.add(getSizeKeyString(cmsItem));    
    ItemRowKey itemRowKey = new ItemRowKey(cmsItem.getStoreId(), cmsItem.getStoreName()
        , cmsItem.getColorId(), cmsItem.getItemDetail().getColorDesc());
    itemRowKeySet.add(itemRowKey);
    Hashtable itemRowKeyHash = null;
    if (!itemRowSizeHash.containsKey(itemRowKey)) {
      itemRowKeyHash = new Hashtable();
      itemRowSizeHash.put(itemRowKey, itemRowKeyHash);
    } else
      itemRowKeyHash = ((Hashtable)itemRowSizeHash.get(itemRowKey));
    itemRowKeyHash.put(getSizeKeyString(cmsItem), cmsItem);
    //this.fireTableDataChanged();
  }
  
	public CMSItem[][] generateItemMatrix() {
		return generateItemMatrix(null);
	} 
  /**
   * put your documentation comment here
   * @return
   */
	public CMSItem[][] generateItemMatrix(String colorId) {
		clear();
		if( colorId != null ) {
			String mainColorId = "";
			itemRowKeyVec = new Vector();
			TreeMap uniqueColorId = new TreeMap();
			for (int i = 0; i < itemRowKeyVecMain.size(); i++) {
				ItemLookupDetailModel.ItemRowKey irk = (ItemLookupDetailModel.ItemRowKey) itemRowKeyVecMain.elementAt(i);      
				if((irk.colorId.equals(colorId)) || irk.storeId.equals(homeStoreId)) {
					if (irk.colorId.equals(colorId)) {
						if (irk.storeId.equals(homeStoreId)) {
							itemRowKeyVec.insertElementAt(irk, 0);
							uniqueColorId.put(irk.colorId, new String[]{irk.colorId, irk.colorDesc});
							mainColorId = irk.colorId;
						} else {
							itemRowKeyVec.addElement(irk);
							uniqueColorId.put(irk.colorId, new String[]{irk.colorId, irk.colorDesc});
						}
					} else {
						itemRowKeyVec.addElement(irk);
						uniqueColorId.put(irk.colorId, new String[]{irk.colorId, irk.colorDesc});
					}
				}

				if(!bUniqueColorAvailable) {
					if ((!(irk.colorId.equals(colorId))) && (!(irk.storeId.equals(homeStoreId)))) {
						otherColors.addElement(irk);
					}
				}
			}
			if( !bUniqueColorAvailable && uniqueColorId.size() > 0) {
				bUniqueColorAvailable=true;
				Iterator ite = uniqueColorId.keySet().iterator();
				while(ite.hasNext()) {
					String strId = (String)ite.next();
					if(strId.equals(mainColorId)){
						itemColor.insertElementAt(uniqueColorId.get(strId),0);
					}else{
						itemColor.addElement(uniqueColorId.get(strId));
					}
				}
			}
			//Set other colors to check if NEXT_COLOR button has to be 
			//displayed in the applet
			setOtherColors(otherColors);
			if(iCurrentColorIndex == -1 && itemColor.size() > 0 ) {
				iCurrentColorIndex=0;
			}
		}
		
		itemRowItemsArrayHash = new Hashtable();
		itemMatrix = new CMSItem[sizeVec.size()][itemRowKeyVec.size()];
		Hashtable itemRowHash = null;
		//Hashtable itemRowSizeHash = null;
		int x = 0;
		for (int y = 0; y < itemRowKeyVec.size(); y++) {
			//itemRowSizeHash = new Hashtable();
			CMSItem[] itemRowItems = new CMSItem[sizeVec.size()];
			itemRowHash = (Hashtable)itemRowSizeHash.get(itemRowKeyVec.elementAt(y));
			for (x = 0; x < sizeVec.size(); x++) {
				itemMatrix[x][y] = (CMSItem)itemRowHash.get(sizeVec.elementAt(x));
				//itemRowSizeHash.put(new Integer(sizeVec.size()), (CMSItem)itemRowHash.get(sizeVec.elementAt(x)));
				itemRowItems[x] = (CMSItem)itemRowHash.get(sizeVec.elementAt(x));
			}
			itemRowItemsArrayHash.put(itemRowKeyVec.elementAt(y), itemRowItems);
		}
		//this.fireTableStructureChanged();
		for (int y = 0; y < itemRowKeyVec.size(); y++) {
			itemRowHash = (Hashtable)itemRowSizeHash.get(itemRowKeyVec.elementAt(y));
			this.addRow(itemRowHash);
		}
		this.fireTableDataChanged();
		//System.out.println("itemRowKeyVec: " + itemRowKeyVec );
		return itemMatrix;
  }
/*
	 public int getRowCount() {
		 if(itemRowKeyVec != null) {
			 return itemRowKeyVec.size();
		 }
		 return 0;
	 }
*/
	public void showPreviousColor() {
		iCurrentColorIndex--;
		//System.out.println("showPreviousColor.... for Index " + iCurrentColorIndex + itemColor );
		if( iCurrentColorIndex >= 0 && iCurrentColorIndex < itemColor.size() ) {
			String colorId = ((String[])itemColor.elementAt(iCurrentColorIndex))[0];
			generateItemMatrix(colorId);		
		}else{
			iCurrentColorIndex++;
		}
	}

	public void showNextColor() {
		iCurrentColorIndex++;
		//System.out.println("showNextColor.... for Index " + iCurrentColorIndex + itemColor );
		if( iCurrentColorIndex < itemColor.size() ) {
			String colorId = ((String[])itemColor.elementAt(iCurrentColorIndex))[0];
			generateItemMatrix(colorId);
		}else{
			iCurrentColorIndex--;
		}
	}

	public String[] getPreviousColorDetail(){
		String aStrColor[] = null;
		int iColIndex = iCurrentColorIndex -1;
		if( iColIndex >= 0 && iColIndex < itemColor.size() ) {
			aStrColor = (String[])itemColor.elementAt(iColIndex);
			//System.out.println("----- PreviousColor::" + aStrColor[0]+"-"+aStrColor[1] );
		}
		return aStrColor;
	}
	public String[] getNextColorDetail(){
		String aStrColor[] = null;
		int iColIndex = iCurrentColorIndex +1;
		if( iColIndex < itemColor.size() ) {
			aStrColor = (String[])itemColor.elementAt(iColIndex);
			//System.out.println("NextColor::" + aStrColor[0]+"-"+aStrColor[1] );
		}
		return aStrColor;
	}

	  /**
	   * Determines if the next_color button has to be displayed
	   * @param colorVec
	   */
	  public void setOtherColors(Vector colorVec) {
		  this.otherColors = colorVec;
	  }
	  
	  /**
	   *  Returns the other colors set
	   * @return
	   */
	  public Vector getOtherColors() {
		  return this.otherColors;
	  }
		
  public boolean hasNextColor() {
	  int colorVecSize = itemColor.size();
	  int iCurrIndex = iCurrentColorIndex +1;
	  return (iCurrIndex < colorVecSize);
	  //return (iCurrIndex != -1 && iCurrIndex < colorVecSize-1 && colorVecSize > 1);
  }
  public boolean hasPreviousColor() {
	  int colorVecSize = itemColor.size();
	  int iCurrIndex = iCurrentColorIndex - 1;
	  return (iCurrIndex >= 0 && colorVecSize > 0);
	  //return (iCurrIndex != -1 && iCurrIndex > 0 && colorVecSize > 1);
  }

  public int getCurrentColorIndex() {
	  return iCurrentColorIndex;	  
  } 

  
  /**
   * put your documentation comment here
   * @param itemRowMap
   */
  public void addItemRow(Map itemRowMap) {
    if (itemRowMap == null)
      return;
    addRow(itemRowMap);
    this.fireTableDataChanged();
  }

  /**
   * put your documentation comment here
   * @param row
   */
  public void deleteItemRowAt(int row) {
    removeRowInPage(row);
    this.fireTableRowsDeleted(row, row);
  }

  /**
   * put your documentation comment here
   * @param row
   * @return
   */
  public Map getItemRowAt(int row) {	 
   return (Map)this.getRowInPage(row);
	  
  }

  /**
   * put your documentation comment here
   * @param row
   * @param column
   * @return
   */
  public CMSItem getItemAt(int row, int column) {
    int iFixedColSize = getNoOfFixedColumns();
    if (column < iFixedColSize) {
    	return null;
    }
    return this.itemMatrix[column - iFixedColSize][row];
  }

	/**Fix for 1842 
	 * put your documentation comment here
	 * @param row
	 * @param column
	 * @return
	 */
  
	public Object getValueAt(int row, int column) {
		int iFixedColSize = getNoOfFixedColumns();
		row = ( getCurrentPageNumber() * getRowsShown() ) + row;
		ItemRowKey itemRowKey = (ItemRowKey)itemRowKeyVec.elementAt(row);
		switch (column) {
			case COLOR:
				return (itemRowKey.colorId == null ? "" : itemRowKey.colorId )+ "\n"
				+ (itemRowKey.colorDesc == null ? "" : itemRowKey.colorDesc);
			case STORE:
				return itemRowKey.storeId + "\n"
				+ (itemRowKey.storeName == null ? "" : itemRowKey.storeName);
		}
		if (column < sizeVec.size() + iFixedColSize) {
			return itemRowKey;
		}
		return "";
	}

  /**
   * put your documentation comment here
   * @param RowsShown
   *
  public void setRowsShown(int RowsShown) {
    super.setRowsShown(RowsShown);
  }*/

  /**
   * put your documentation comment here
   * @return
   */
  public int getColumnCount() {	  
    return COLUMN_NAMES.length;
  }

  /**
   * put your documentation comment here
   * @return
   */
  public String[] getColumnNames() {
    return COLUMN_NAMES;
  }

  public String[] getFixedColumnNames() {
	    return FIXED_COLUMN_NAMES;
  }
  
  public int getNoOfFixedColumns() {
	  return iNoOfFixedColumns;
  }
  /**
   * put your documentation comment here
   * @return
   */
  public Hashtable getItemRowItemsArrayHash() {
    return itemRowItemsArrayHash;
  }

  /**
   * put your documentation comment here
   * @return
   */
  public Hashtable getItemRowSizeHash() {
    return itemRowSizeHash;
  }

  /**
   * put your documentation comment here
   * @param cmsItem
   * @return
   */
  protected String getSizeKeyString(CMSItem cmsItem) {
    String sizeKeyString = "";
    if (cmsItem.getItemDetail().getSizeIndx() != null)
      sizeKeyString = sizeKeyString + cmsItem.getItemDetail().getSizeIndx().trim();
    if (cmsItem.getItemDetail().getExtSizeIndx() != null
        && !cmsItem.getItemDetail().getExtSizeIndx().trim().equals("")) {
      if (sizeKeyString.trim().length() > 0)
        sizeKeyString = sizeKeyString + ":";
      sizeKeyString = sizeKeyString + cmsItem.getItemDetail().getExtSizeIndx().trim();
    }
    return sizeKeyString;
  }

  public class ItemRowKey implements Comparable {
    public String storeId;
    public String storeName;
    public String colorId;
    public String colorDesc;
    //EUROPE VERSION
    public String productNum;

    /**
     * put your documentation comment here
     * @param     String storeId
     * @param     String storeName
     * @param     String colorId
     * @param     String colorDesc
     */
    ItemRowKey(String storeId, String storeName, String colorId, String colorDesc) {    	
      this.storeId = storeId;
      this.storeName = storeName;
      this.colorId = colorId;
      this.colorDesc = colorDesc;
    }

    /**
     * put your documentation comment here
     * @param obj
     * @return
     */
    public boolean equals(Object obj) {
      if (((ItemRowKey)obj).colorId != null ) {
      if (storeId.equals(((ItemRowKey)obj).storeId) && colorId.equals(((ItemRowKey)obj).colorId))
        return true;
      } else {
      if (storeId.equals(((ItemRowKey)obj).storeId))
        return true;
      }
      return false;
    }

    /**
     * put your documentation comment here
     * @return
     * Ruchi Issue :1842In itemlookup for some Item if i push LOCATE i have a gray window. Look at the attachment. 
     */
    public int hashCode() {
    	if (colorId != null) {
    		return storeId.hashCode() & colorId.hashCode();
    	}else {
    		return storeId.hashCode();
    	}
    }

    /**
     * put your documentation comment here
     * @param obj
     * @return
     * Ruchi Issue :1842 check for null Color id
     */
    public int compareTo(Object obj) {
      if (storeId.compareTo(((ItemRowKey)obj).storeId) == 0)
      	if (colorId == null) {
      		return 0;
      	} else {
      		return colorId.compareTo(((ItemRowKey)obj).colorId);
      	}
      else if (storeId.equals(homeStoreId))
        return -1;
      else if (((ItemRowKey)obj).storeId.equals(homeStoreId))
        return 1;
      else
        return storeId.compareTo(((ItemRowKey)obj).storeId);
    }

    public String toString() {
    	
      	return "storeId ="+ storeId+
          " storeName ="+ storeName+
          " colorId ="+ colorId+
          " colorDesc ="+ colorDesc;    	
      }
 }

  private boolean isItemSearchInMultipleStore() {
	  	  String itemSearchStatus = configMgr.getString("ITEM_SEARCH_IN_MULTIPLE_STORE");
		  //System.out.println("isItemSearchInMultipleStore::itemSearchStatus:: " +itemSearchStatus );
	  	  if( itemSearchStatus != null && itemSearchStatus.trim().equalsIgnoreCase("true") ){
	  		  return true;
	  	  }else{
	  		  return false;
	  	  }
	}  
	//-------------------------------------------------
	private int currentPage = 0;
	private int rowsShown = 3;

	public Vector getCurrentPage() {
		return null;
		/*
		Vector vResult = new Vector();
		for (int x = currentPage * rowsShown; x < ((currentPage + 1) * rowsShown); x++) {
			if (x < vRows.size()) {
				vResult.addElement(vRows.elementAt(x));
			}
		}
		return vResult;
		*/
	}

	public int getCurrentPageNumber() {
		return currentPage;
	}

	public int getPageCount() {
		int page = itemRowKeyVec.size() / rowsShown;
		if ((itemRowKeyVec.size() % rowsShown) > 0) {
			page++;
		}
		return page;
	}

	public int getRowCount() {
		int rowCount = 0;
		int totalRows = itemRowKeyVec.size();
		if (totalRows > 0) {
			int pages = totalRows / rowsShown;
			if (currentPage < pages) {
				rowCount = rowsShown;
			} else {
				rowCount = totalRows % rowsShown;
			}
		}
		return rowCount;
	}

	public Object getRowInPage(int row) {
		int actualRowNo = (getCurrentPageNumber() * getRowsShown()) + row;
		if( actualRowNo < itemRowKeyVec.size() ) {
			return itemRowKeyVec.elementAt(actualRowNo);
		}
		return null;
	}

	public void setRowsShown(int noOfRows) {
		rowsShown = noOfRows;
	}

	public int getRowsShown() {
		return rowsShown;
	}

	public int getTotalRowCount() {
		return itemRowKeyVec.size();
	}

	public void firstPage() {
		currentPage = 0;
	}

	public void lastPage() {
		if (getPageCount() > 0) {
			currentPage = getPageCount() - 1;
			fireTableDataChanged();
		}
	}

	public void nextPage() {
		if (currentPage < (getPageCount() - 1)) {
			currentPage++;
			fireTableDataChanged();
		}
	}

	public void prevPage() {
		if (currentPage > 0) {
			currentPage--;
			fireTableDataChanged();
		}
	}
	
	/*Started #issue 1941 stock locator by neeti
	 * creating new constructor of model with different arguments for repaint new model and
	 * get item row with different sizes for the perticular store in the same applet 
	*/
	public ItemLookupDetailModel(String homeStoreId, CMSItem[] cmsItems,
			String sColorId) {

		COLUMN_NAMES = getFixedColumnNames();
		iNoOfFixedColumns = getColumnNames().length;
		java.util.ResourceBundle res = com.chelseasystems.cr.util.ResourceManager
				.getResourceBundle();
		this.homeStoreId = homeStoreId;
		itemRowSizeHash = new Hashtable();
		TreeSet sizeSet = new TreeSet();
		sizeDescHash = new Hashtable();
		TreeSet itemRowKeySet = new TreeSet();
		itemRowKeyVec = new Vector();
		itemRowKeyVecMain = new Vector();
		itemColor = new Vector();
		sizeVec = new Vector();
		otherColors = new Vector();
		for (int i = 0; i < cmsItems.length; i++) {
			addItem(cmsItems[i], itemRowKeySet, sizeSet);
			sizeDescHash.put(getSizeKeyString(cmsItems[i]),
					getSizeKeyString(cmsItems[i]));
		}
		itemRowKeyVec.addAll(itemRowKeySet);
		itemRowKeyVecMain.addAll(itemRowKeySet);
		sizeVec.addAll(sizeSet);
		if (isItemSearchInMultipleStore()) {
			if (sColorId == null && (cmsItems != null && cmsItems.length > 0)) {
				sColorId = cmsItems[0].getColorId();
			}
			generateItemMatrixLocator(sColorId);
		} else {
			generateItemMatrixLocator();
		}
		resetColumnNames();
		String colNames[] = getColumnNames();
		String sIdentiFiers[] = new String[colNames.length];
		for (int iCtr = 0; iCtr < colNames.length; iCtr++) {
			if (iCtr < iNoOfFixedColumns) {
				sIdentiFiers[iCtr] = res.getString(colNames[iCtr]);
			} else {
				sIdentiFiers[iCtr] = colNames[iCtr];
			}
		}
		this.setColumnIdentifiers(sIdentiFiers);
	}

	public CMSItem[][] generateItemMatrixLocator() {
		return generateItemMatrixLocator(null);
	} 	 

	 public CMSItem[][] generateItemMatrixLocator(String colorId) {
		clear();
		if (colorId != null) {
			String mainColorId = "";
			itemRowKeyVec = new Vector();
			TreeMap uniqueColorId = new TreeMap();
			for (int i = 0; i < itemRowKeyVecMain.size(); i++) {
				ItemLookupDetailModel.ItemRowKey irk = (ItemLookupDetailModel.ItemRowKey) itemRowKeyVecMain
						.elementAt(i);
				if (irk.storeId.equals(homeStoreId)) {
					if (irk.colorId.equals(colorId)) {
						if (irk.storeId.equals(homeStoreId)) {
							itemRowKeyVec.insertElementAt(irk, 0);
							uniqueColorId.put(irk.colorId, new String[] {
									irk.colorId, irk.colorDesc });
							mainColorId = irk.colorId;
						} else {
							itemRowKeyVec.addElement(irk);
							uniqueColorId.put(irk.colorId, new String[] {
									irk.colorId, irk.colorDesc });
						}
					} else {
						itemRowKeyVec.addElement(irk);
						uniqueColorId.put(irk.colorId, new String[] {
								irk.colorId, irk.colorDesc });
					}
				}

				if (!bUniqueColorAvailable) {
					if ((!(irk.colorId.equals(colorId)))
							&& (!(irk.storeId.equals(homeStoreId)))) {
						otherColors.addElement(irk);
					}
				}
			}
			if (!bUniqueColorAvailable && uniqueColorId.size() > 0) {
				bUniqueColorAvailable = true;
				Iterator ite = uniqueColorId.keySet().iterator();
				while (ite.hasNext()) {
					String strId = (String) ite.next();
					if (strId.equals(mainColorId)) {
						itemColor.insertElementAt(uniqueColorId.get(strId), 0);
					} else {
						itemColor.addElement(uniqueColorId.get(strId));
					}
				}
			}

			setOtherColors(otherColors);
			if (iCurrentColorIndex == -1 && itemColor.size() > 0) {
				iCurrentColorIndex = 0;
			}
		}
		itemRowItemsArrayHash = new Hashtable();
		itemMatrix = new CMSItem[sizeVec.size()][itemRowKeyVec.size()];
		Hashtable itemRowHash = null;
		int x = 0;
		for (int y = 0; y < itemRowKeyVec.size(); y++) {
			CMSItem[] itemRowItems = new CMSItem[sizeVec.size()];
			itemRowHash = (Hashtable) itemRowSizeHash.get(itemRowKeyVec
					.elementAt(y));
			for (x = 0; x < sizeVec.size(); x++) {
				itemMatrix[x][y] = (CMSItem) itemRowHash.get(sizeVec
						.elementAt(x));
				itemRowItems[x] = (CMSItem) itemRowHash.get(sizeVec
						.elementAt(x));
			}
			itemRowItemsArrayHash.put(itemRowKeyVec.elementAt(y), itemRowItems);
		}
		for (int y = 0; y < itemRowKeyVec.size(); y++) {
			itemRowHash = (Hashtable) itemRowSizeHash.get(itemRowKeyVec
					.elementAt(y));
			this.addRow(itemRowHash);
		}
		this.fireTableDataChanged();
		return itemMatrix;
	}

	
	 //Ended #issue 1941 stock locator by neeti
		  
	//-------------------------------------------------  
}

