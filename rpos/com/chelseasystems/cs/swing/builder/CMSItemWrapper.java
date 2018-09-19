/**
 * 
 */
package com.chelseasystems.cs.swing.builder;

import com.chelseasystems.cs.item.CMSItem;
import com.chelseasystems.cs.pos.CMSMiscItem;
import com.chelseasystems.cs.pos.CMSSpecificItem;

/**
 * Wrapper object to hold item and extendedBarCode.
 * 
 * @author Tim
 * @version 1.0
 */
public class CMSItemWrapper {
	
	private CMSItem item;
	private CMSMiscItem miscItem;
	private CMSSpecificItem specificItem;
	private String extendedBarCode;
	//Vivek Mishra : Added for Extended Barcode CR Europe 06-OCT-2016
	private String extendedStagingBarCode = null;
	//Ends here 06-OCT-2016
	
	public static int EXTENDED_BARCODE_LENGTH = 34;
	
	public CMSItemWrapper(){
		
	}

	/**
	 * @return the item
	 */
	public CMSItem getItem() {
		return item;
	}

	/**
	 * @param item the item to set
	 */
	protected void setItem(CMSItem item) {
		this.item = item;
	}

	/**
	 * @return the miscItem
	 */
	public CMSMiscItem getMiscItem() {
		return miscItem;
	}

	/**
	 * @param miscItem the miscItem to set
	 */
	protected void setMiscItem(CMSMiscItem miscItem) {
		this.miscItem = miscItem;
	}

	/**
	 * @return the extendedBarCode
	 */
	public String getExtendedBarCode() {
		return extendedBarCode;
	}

	/**
	 * @param extendedBarCode the extendedBarCode to set
	 */
	protected void setExtendedBarCode(String extendedBarCode) {
		this.doSetExtendedBarCode(extendedBarCode);
	}
	
	/**
	 * @param extendedBarCode the extendedBarCode to set
	 */
	protected void doSetExtendedBarCode(String extendedBarCode) {
//		if(extendedBarCode.length() == EXTENDED_BARCODE_LENGTH){
//			this.extendedBarCode = extendedBarCode;
//		}
		this.extendedBarCode = extendedBarCode;
	}

	/**
	 * @return the specificItem
	 */
	public CMSSpecificItem getSpecificItem() {
		return specificItem;
	}

	/**
	 * @param specificItem the specificItem to set
	 */
	protected void setSpecificItem(CMSSpecificItem specificItem) {
		this.specificItem = specificItem;
	}
	//Vivek Mishra : Added for Extended Barcode CR Europe 06-OCT-2016
	public String getExtendedStagingBarCode() {
		return extendedStagingBarCode;
	}

	public void setExtendedStagingBarCode(String extendedStagingBarCode) {
		this.extendedStagingBarCode = extendedStagingBarCode;
	}
    //Ends here 06-OCT-2016	
}
