/**
 * History (tab separated):-
 * Vers	Date		By	Spec		                Description
 * 1	2/4/05	        KS		                        Base
 * 2	2/4/05	        KS	POS_IS_ItemDownload_Rev1	CMSItem
 *
 *
 * formatted with JxBeauty (c) johann.langhofer@nextra.at
 */


package com.chelseasystems.cs.item;

import java.util.*;

import com.chelseasystems.cr.currency.ArmCurrency;
import com.chelseasystems.cr.item.*;
import com.chelseasystems.cr.store.*;
import com.chelseasystems.cs.util.*;

import java.sql.SQLException;
import java.text.SimpleDateFormat;



/**
 *
 * <p>Title: CMSItemDetailsServices</p>
 *
 * <p>Description: </p>
 *
 * <p>Copyright: Copyright (c) 2005</p>
 *
 * <p>Company: </p>
 *
 * @author Khyati Shah
 * @version 1.0
 */
public class CMSItemDetailsServices extends CMSItemServices {
	public static SimpleDateFormat yyyyMMddHHmmss = new SimpleDateFormat("yyyyMMddHHmmss");

	/**
	 * Method will return an Item for a given Item ID
	 * @param id String
	 * @throws Exception
	 * @return Item
	 */
	public Item findById(String id) throws java.lang.Exception {
		String itmString = ItemDetailsMap.getItemDetailsString(id);
		return parseItemString(itmString);
	}

	/**
	 * Method will return an Item for a given Item ID
	 * @param aStore the store requesting the item
	 */
	public CMSItem findByBarCode(String barCode, String storeId) throws java.lang.Exception {
		String itmString = ItemDetailsMap.getItemDetailsString(barCode);
		return parseItemString(itmString);
	}
	
	/**
	 * Added by Anjana to return the SAP item
	 * @param barCode String
	 * @param storeId String
	 * @param itemIdLength String
 	 * @throws Exception
	 * @return CMSItem
	 */
	public CMSItem findSAPBarCode(String barCode, String storeId, String itemIdLength) throws java.lang.Exception {
		String itmString = ItemDetailsMap.getItemDetailsString(barCode);
		return parseItemString(itmString);
	}
	

	/**
	 * This method download items dat file at client site
	 * @param fileName String
	 * @param storeId String
 	 * @throws Exception
	 * @return byte[]
	 */
	public byte[] getItemFile(String fileName, String storeId, Date d) throws java.lang.Exception {
		return new byte[0];
	}

	/**
	 * @param itemStrin String
	 * @return CMSItem
	 */
	private CMSItem parseItemString(String itmString) throws Exception {
		try {
			char eol = '\n';
			String newString = "";
			if (itmString.indexOf(eol) > -1)
				newString = itmString.substring(0, itmString.indexOf(eol) - 1);
			itmString = newString;
			CMSItem item = null;
			CMSItemDetail itemDetail = null;
			StringTokenizer st = new StringTokenizer(itmString, "|", true);
			ArrayList prmIds = new ArrayList();
			String PREV = "";
			ArrayList values = new ArrayList();
			while(st.hasMoreTokens()) {
				String s = st.nextToken().trim();
				if (!PREV.equals("|") && !s.equals("|")) {
					values.add(s);
				} else if (PREV.equals("|") && s.equals("|")) {
					values.add("");
				} else if (PREV.equals("|") && !s.equals("|")) {
					values.add(s);
				}
				PREV = s;
			}
			int j = values.size();
			for (int i = 0; i < j; i++) {
				if (i == 0) {
					item = new CMSItem((String) values.get(i));
					itemDetail = item.getItemDetail();
					continue;
				}
				if (i == 1) {
					if (values.get(i) == null)
						values.set(i, "");
					item.doSetBarCode((String) values.get(i));
					continue;
				}
				if (i == 2) {
					if (values.get(i) == null)
						values.set(i, "");
					item.doSetStoreId((String) values.get(i));
					continue;
				}
				if (i == 3) {
					if (values.get(i) == null)
						values.set(i, "0.00");
					item.doSetRetailPrice(new ArmCurrency((String) values.get(i)));
					continue;
				}
				if (i == 4) {
					if (values.get(i) == null)
						values.set(i, "0.00");
					item.doSetMarkdownAmount(new ArmCurrency((String) values.get(i)));
					continue;
				}
				if (i == 5) {
					if (values.get(i) != null)
						if (((String) values.get(i)).equalsIgnoreCase("N"))
							item.doSetTaxable(false);
						else
							item.doSetTaxable(true);
					continue;
				}
				if (i == 6) {
					if (values.get(i) == null)
						values.set(i, "0.00");
					item.doSetCurrencyCode((String) values.get(i));
					continue;
				}
				if (i == 7) {
					if (values.get(i) == null)
						values.set(i, "0.00");
					if (((String) values.get(i)).trim().length() == 0)
						values.set(i, "0.00");
					item.doSetVatRate(new Double((String) values.get(i)));
					continue;
				}
				if (i == 8) {
					if (values.get(i) == null)
						values.set(i, "");
					item.doSetForTheYear((String) values.get(i));
					continue;
				}
				if (i == 9) {
					if (values.get(i) == null)
						values.set(i, "");
					item.doSetSeason((String) values.get(i));
					continue;
				}
				if (i == 10) {
					if (values.get(i) == null)
						values.set(i, "");
					itemDetail.doSetSeasonDesc((String) values.get(i));
					continue;
				}
				if (i == 11) {
					if (values.get(i) == null)
						values.set(i, "");
					item.doSetBrand((String) values.get(i));
					continue;
				}
				if (i == 12) {
					if (values.get(i) == null)
						values.set(i, "");
					itemDetail.doSetBrandDesc((String) values.get(i));
					continue;
				}
				if (i == 13) {
					if (values.get(i) == null)
						values.set(i, "");
					item.doSetLabel((String) values.get(i));
					continue;
				}
				if (i == 14) {
					if (values.get(i) == null)
						values.set(i, "");
					itemDetail.doSetLabelDesc((String) values.get(i));
					continue;
				}
				if (i == 15) {
					if (values.get(i) == null)
						values.set(i, "");
					item.doSetSubline((String) values.get(i));
					continue;
				}
				if (i == 16) {
					if (values.get(i) == null)
						values.set(i, "");
					itemDetail.doSetSublineDesc((String) values.get(i));
					continue;
				}
				if (i == 17) {
					if (values.get(i) == null)
						values.set(i, "");
					item.doSetGender((String) values.get(i));
					continue;
				}
				if (i == 18) {
					if (values.get(i) == null)
						values.set(i, "");
					itemDetail.doSetGenderDesc((String) values.get(i));
					continue;
				}
				if (i == 19) {
					if (values.get(i) == null)
						values.set(i, "");
					item.doSetCategory((String) values.get(i));
					continue;
				}
				if (i == 20) {
					if (values.get(i) == null)
						values.set(i, "");
					itemDetail.doSetCategoryDesc((String) values.get(i));
					continue;
				}
				if (i == 21) {
					if (values.get(i) == null)
						values.set(i, "");
					item.doSetItemDrop((String) values.get(i));
					continue;
				}
				if (i == 22) {
					if (values.get(i) == null)
						values.set(i, "");
					itemDetail.doSetDropDesc((String) values.get(i));
					continue;
				}
				if (i == 23) {
					if (values.get(i) == null)
						values.set(i, "");
					item.doSetVariant((String) values.get(i));
					continue;
				}
				if (i == 24) {
					if (values.get(i) == null)
						values.set(i, "");
					item.doSetSizeId((String) values.get(i));
					continue;
				}
				if (i == 25) {
					if (values.get(i) == null)
						values.set(i, "");
					itemDetail.doSetSizeDesc((String) values.get(i));
					continue;
				}
				if (i == 26) {
					if (values.get(i) == null)
						values.set(i, "");
					itemDetail.doSetSizeIndx((String) values.get(i));
					continue;
				}
				if (i == 27) {
					if (values.get(i) == null)
						values.set(i, "");
					itemDetail.doSetExtSizeIndx((String) values.get(i));
					continue;
				}
				if (i == 28) {
					if (values.get(i) == null)
						values.set(i, "");
					item.doSetKidsSize((String) values.get(i));
					continue;
				}
				if (i == 29) {
					if (values.get(i) == null)
						values.set(i, "");
					itemDetail.doSetKidsSizeDesc((String) values.get(i));
					continue;
				}
				if (i == 30) {
					if (values.get(i) == null)
						values.set(i, "");
					item.doSetSupplierId((String) values.get(i));
					continue;
				}
				if (i == 31) {
					if (values.get(i) == null)
						values.set(i, "");
					itemDetail.doSetSupplierName((String) values.get(i));
					continue;
				}
				if (i == 32) {
					if (values.get(i) == null)
						values.set(i, "");
					item.doSetModel((String) values.get(i));
					continue;
				}
				if (i == 33) {
					if (values.get(i) == null)
						values.set(i, "");
					item.doSetFabric((String) values.get(i));
					continue;
				}
				if (i == 34) {
					if (values.get(i) == null)
						values.set(i, "");
					item.doSetColorId((String) values.get(i));
					if (values.get(i) == null)
						values.set(i, "");
					continue;
				}
				if (i == 35) {
					if (values.get(i) == null)
						values.set(i, "");
					itemDetail.doSetColorDesc((String) values.get(i));
					continue;
				}
				if (i == 36) {
					if (values.get(i) == null)
						values.set(i, "");
					item.doSetProductNum((String) values.get(i));
					continue;
				}
				if (i == 37) {
					if (values.get(i) == null)
						values.set(i, "");
					itemDetail.doSetProductDesc((String) values.get(i));
					continue;
				}
				if (i == 38) {
					if (values.get(i) == null)
						values.set(i, "");
					item.doSetDescription((String) values.get(i));
					continue;
				}
				if (i == 39) {
					if (values.get(i) == null)
						values.set(i, "");
					item.doSetDepartment((String) values.get(i));
					continue;
				}
				if (i == 40) {
					if (values.get(i) == null)
						values.set(i, "");
					itemDetail.doSetPosDeptDesc((String) values.get(i));
					continue;
				}
				if (i == 41) {
					if (values.get(i) == null)
						values.set(i, "");
					item.doSetClassId((String) values.get(i));
					continue;
				}
				if (i == 42) {
					if (values.get(i) == null)
						values.set(i, "");
					itemDetail.doSetClassDesc((String) values.get(i));
					continue;
				}
				if (i == 43) {
					if (values.get(i) == null)
						values.set(i, "");
					item.doSetSubClassId((String) values.get(i));
					continue;
				}
				if (i == 44) {
					if (values.get(i) == null)
						values.set(i, "");
					itemDetail.doSetSubClassDesc((String) values.get(i));
					continue;
				}
				if (i == 45) {
					if (values.get(i) == null)
						values.set(i, "");
					item.doSetStyleNum((String) values.get(i));
					continue;
				}
				if (i == 46) {
					if (values.get(i) == null)
						values.set(i, "");
					item.doSetScItemSls((String) values.get(i));
					continue;
				}
				if (i == 47) {
					if (values.get(i) == null)
						values.set(i, "");
					item.doSetRequiresManualUnitPrice("R".equalsIgnoreCase((String) values.get(i)));
					item.doSetManualPriceEntryProhibited("P".equalsIgnoreCase((String) values.get(i)));
					continue;
				}
				if (i == 48) {
					Date updateDate = null;
					if ((values.get(i)) != null) {
						SimpleDateFormat sdf = new SimpleDateFormat("dd-MMM-yyyy");
						try {
							updateDate = sdf.parse((String) values.get(i));
						} catch (Exception ex) {
						}
					}
					item.doSetUpdateDate(updateDate);
					continue;
				}
				if (i >= 49) { // start of tokens with promotion info
					String promotionString = (String) values.get(i);
					if (promotionString != null && promotionString.length() > 0) {
						int delimPosition = promotionString.indexOf(',', 0);
						String promotionId = promotionString.substring(0, delimPosition);
						String expirationDt = promotionString.substring(delimPosition + 1);
						if (promotionId == null || promotionId.equals(""))
							continue;
						if (expirationDt != null && !expirationDt.equals("")) {
							Date dtExp = yyyyMMddHHmmss.parse(expirationDt);
							Calendar calExpDt = Calendar.getInstance();
							calExpDt.setTime(dtExp);
							Calendar calCurrentDt = Calendar.getInstance();
							calCurrentDt.setTime(new Date());
							if (calExpDt.after(calCurrentDt)) {
								prmIds.add(promotionId);
							}
						} else
							prmIds.add(promotionId);
					}
					continue;
				}
			}
			if (item != null) {
				item.doSetPromotionIds(prmIds);
			}
			if (item != null)
				System.out.println("after " + item.toString());
			return item;
		} catch (Exception ex) {
			throw ex;
		}
	}

	/**
	 * This method returns all items
	 * @throws Exception
	 * @return Item[]
	 */
	public Item[] findAllItems() throws java.lang.Exception {
		return null; // this is a server method!
	}

	/**
	 * This method returns all items of a store
	 * @param aStore Store
	 * @throws Exception
	 * @return Item[]
	 */
	public Item[] findAllItemsForStore(Store aStore) throws java.lang.Exception {
		return null; // this is a server method!
	}

	/**
	 * Method will return an array of item ids whose description at least partially matches 
     * the submitted description
	 * @param description  String
	 * @return String[]
	 * @throws Exception
	 */
	public String[] findIDListByDescription(String description) throws java.lang.Exception {
		return null;
	}

	/**
	 * This method is used to search for Items by description for a store.
	 * @param desc substring of the description
	 * @return an array of items
	 * @throws Exception
	 */
	public String[] findIDListByDescription(String description, String storeId) throws Exception {
		return null;
	}

	/**
	 * Search for Items by description.
	 * @param desc substring of the description
	 * @return an array of items
	 * @throws Exception
	 */
	public Item[] findByDescription(String description) throws Exception {
		return null;
	}

	/**
	 * This method is used to update an Item, specified by its Id, with a new price, 
     * which is specified by the amount. The change will be persisted
	 * after the vector of items is updated.
	 * @param itemId String
	 * @param amount Currency
	 * @return boolean
	 * @throws Exception
	 */
	public boolean updateItemPrice(String itemId, ArmCurrency amount) throws Exception {
		return false; // not being used!!
	}

	/**
	 * @param store Store
	 * @throws Exception
	 * @return Map
	 */
	public Map getSupplierSeasonYear(Store store) throws Exception {
		return null;
	}

	/**
	 * This method is used to search item on the basis of specified search parameter
	 * @param searchString ItemSearchString
	 * @throws Exception
	 * @return Item[]
	 */
	public Item[] findItems(ItemSearchString searchString) throws Exception {
		return null;
	}

	/**
	 * This method is used to find items with zero unit price
	 * 
	 * @return CMSItem[]
	 * @throws Exception
	 */
	public CMSItem[] findItemsWithNoUnitPrice() throws java.lang.Exception {
		CMSItem item = null;
		Map barcodeMap = new HashMap();
		List noUnitPriceItemsList = new ArrayList();
		String itemId = null;
		String itmString = null;

		barcodeMap = ItemDetailsMap.getBarcodeMap();
		if (barcodeMap.size() > 0) {
			// Read data from .dat
			for (Iterator it = barcodeMap.keySet().iterator(); it.hasNext();) {
				itemId = (String) it.next();
				itmString = ItemDetailsMap.getItemDetailsString(itemId);
				item = parseItemString(itmString);
				if (item != null && item.getRetailPrice() != null) {
					if (item.getRetailPrice().doubleValue() == 0.00) {
						noUnitPriceItemsList.add(item);
					}
				}
			}
		}

		if (noUnitPriceItemsList != null && noUnitPriceItemsList.size() > 0) {
			return (CMSItem[]) noUnitPriceItemsList.toArray(new CMSItem[0]);
		} else {
			return null;
		}
	}

	
	/**
	 * Added by Satin to search an itemId corresponding to entered barcode for exception item.
	 * @param barcode String
	 * @throws Exception
	 * @return String
	 */
	public String selectItemIdFromAsItm(String barcode)
			throws Exception {
		return null;
	}
	
	
	/**
	 * Added by Satin to select an item corresponding to entered itemId for exception item.
	 * @param itemId String
	 * @throws Exception
	 * @return String
	 */
	public String selectItemFromAsItm(String itemId)
			throws Exception {
		return null;
	}

	 /**
	 * Added by Satin.
	 * This method is used to insert a record into AS_ITM_RTL_STR for exception item.
	 * 
	 * @param theAppMgr IRepositoryManager
	 * @param storeId String
	 * @param itemId String
	 * @param retailPrice ArmCurrency
	 * @param currencyCode String
	 * @param vatRate Double
	 * @param taxable String
	 * @return void
	 * @throws Exception
	 */
	public void insertIntoAsItmRtlStr (String storeId, String itemId, ArmCurrency retailPrice, String currencyCode, String itemDesc, Double vatRate, String taxable)
			throws Exception {}



	
}
