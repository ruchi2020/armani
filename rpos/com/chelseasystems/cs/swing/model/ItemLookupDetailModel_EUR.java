/*
 History:
 +------+------------+-----------+-----------+----------------------------------------------------+
 | Ver# | Date       | By        | Defect #  | Description                                        |
 +------+------------+-----------+-----------+----------------------------------------------------+
 | 1    | 01/29/2007 | Tushar    | N/A       | Mearge with Global Version                         |
 +------+------------+-----------+-----------+----------------------------------------------------+
 |      |            |           |           |                                                    |
 --------------------------------------------------------------------------------------------------
 *
 */


package com.chelseasystems.cs.swing.model;

import com.chelseasystems.cs.item.*;
import java.util.TreeSet;
import java.util.Hashtable;

/**
* <p>Title:ItemLookupDetailModel </p>
* <p>Description: Model Object for ItemLookupDetailPanel</p>
* <p>Copyright: Copyright (c) 2005</p>
* <p>Company: SkillnetInc</p>
* @author Vikram Mundhra
* @version 1.0
*/
public class ItemLookupDetailModel_EUR extends ItemLookupDetailModel {
	private static String[] FIXED_COLUMN_NAMES = {"HDR_Store", "HDR_Color", "HDR_Product"};
	public static final int PRODUCT = 2;

	/**
	 * put your documentation comment here
	 * @param   CMSItem[] cmsItems
	 * @param   String homeStoreId
	 */
	public ItemLookupDetailModel_EUR(CMSItem[] cmsItems, String homeStoreId,String sColorId) {
		super(cmsItems, homeStoreId,sColorId);
	}

	  public String[] getFixedColumnNames() {
		    return FIXED_COLUMN_NAMES;
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
	        , cmsItem.getColorId(), cmsItem.getItemDetail().getColorDesc(), cmsItem.getProductNum());
	    itemRowKeySet.add(itemRowKey);
	    Hashtable itemRowKeyHash = null;
	    if (!itemRowSizeHash.containsKey(itemRowKey)) {
	    	itemRowKeyHash = new Hashtable();
	    	itemRowSizeHash.put(itemRowKey, itemRowKeyHash);
	    } else {
	    	itemRowKeyHash = ((Hashtable)itemRowSizeHash.get(itemRowKey));
	    }
	    itemRowKeyHash.put(getSizeKeyString(cmsItem), cmsItem);
	}
  
	/**
	* put your documentation comment here
	* @param row
	* @param column
	* @return
	*/
	public Object getValueAt(int row, int column) {
		if(column == PRODUCT) {
			ItemRowKey itemRowKey = (ItemRowKey)itemRowKeyVec.elementAt(row);
			return itemRowKey.productNum;
		}else{
			return super.getValueAt(row, column);
		}
	}
	
	public class ItemRowKey extends ItemLookupDetailModel.ItemRowKey {
		public String productNum;
		ItemRowKey(String storeId, String storeName, String colorId, String colorDesc) {
			super(storeId,storeName,colorId, colorDesc);
		}
		ItemRowKey(String sStoreId, String sStoreName, String sColorId, String sColorDesc, String prodNo) {
	    	this(sStoreId,sStoreName,sColorId, sColorDesc);
	        java.util.ResourceBundle rBundle = com.chelseasystems.cr.util.ResourceManager.getResourceBundle();
	        storeId = sStoreId;
	        storeName = sStoreName;
	        if(sColorId != null && sColorId.trim().length() > 0)
	        	colorId = sColorId;
	        else
	        	colorId = rBundle.getString("N/A");
	        if(sColorDesc != null)
	        	colorDesc = sColorDesc;
	        else
	        	colorDesc = "";
	        if(prodNo != null && prodNo.trim().length() > 0) {
	        	productNum = prodNo;
		    }else {
	        	productNum = rBundle.getString("N/A");
	        }
			System.out.println("EUROPE ItemRowKey " + toString() );
	    }

	    public boolean equals(Object obj) {
	    	if((storeId != null) && (colorId != null) && (productNum != null)) {
	    			if (storeId.equals(((ItemRowKey) obj).storeId)
	    						&& colorId.equals(((ItemRowKey) obj).colorId)
	    							&& productNum.equals(((ItemRowKey) obj).productNum)) {
	    							return true;
	    			}
	    	}
	    	return false;
	    }
		  //Fix for 1842: Null check for color id 
	    //Assumption: StoreID and productNum will never be null.
    	public int hashCode() {
    		if(colorId != null) {
    					return storeId.hashCode() & colorId.hashCode() & productNum.hashCode();
    		}
    		return storeId.hashCode()& productNum.hashCode();
    	}	
    	
    	
	    public int compareTo(Object obj) {
	    	if (storeId.compareTo(((ItemRowKey)obj).storeId) == 0) {
	    		if (colorId.compareTo(((ItemRowKey) obj).colorId) == 0) {
	    			return productNum.compareTo(((ItemRowKey) obj).productNum);
	    		} else {
	    			return colorId.compareTo(((ItemRowKey) obj).colorId);
	    		}
	    	}else{
	    		return super.compareTo(obj);
	    	}
	    }

	    public String toString() {
	      	return super.toString()+ " productNo = "+ productNum;
	    }
	}
	
}

