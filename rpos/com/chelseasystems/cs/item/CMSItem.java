/**
 * History (tab separated):-
 * Vers	Date		By	Spec		                Description
 * 1    2/4/05	        KS		                        Base
 * 2    2/4/05	        KS	POS_IS_ItemDownload_Rev1	CMSItem
 * 4    4/20/05          RP      Item Lookup implementation
 * 5    4/30/05          VM      Item Lookup implementation      Removed trim() from getStoreName
 * 6    4/4/2006         SA		 Style locator enhancement and sku lookup and visibility
 * formatted with JxBeauty (c) johann.langhofer@nextra.at
 */


package com.chelseasystems.cs.item;

import com.chelseasystems.cr.item.*;
import com.chelseasystems.cr.currency.*;
import java.util.Date;


/**
 *
 * <p>Title: CMSItem</p>
 * <p>Description: This class stores the attributes of an item</p>
 * <p>Copyright: Copyright (c) 2005</p>
 * <p>Company: </p>
 * @author Khyati Shah
 * @version 1.0
 */
public class CMSItem extends Item implements com.chelseasystems.cr.rules.IRuleEngine {
  static final long serialVersionUID = 8350808060163710291L;
  private String label = null;
  private String forTheYear = null;
  private String barCode = null;
  private String subline = null;
  private String gender = null;
  private String category = null;
  private String itemDrop = null;
  private String variant = null;
  private String model = null;
  private String fabric = null;
  private String styleNum = null;
  private String productNum = null;
  private String scItemSls = null;
  private String brandId = null;
  private String classId = null;
  private String colorId = null;
  private String season = null;
  private String sizeId = null;
  private String kidsSize = null;
  private String supplierId = null;
  private String subClassId = null;
  private String storeId = null;
  private String currencyCode = null;
  private String posDept = null;
  private Date updateDate = null;
  private CMSItemDetail cmsItemDetail = null;
  private ItemStock itemStock = null;
  private String storeName = null;
  private boolean isDeposit = false;
  //added by deepika for itemId
  private String id;




//By default, item is from the current store
  //The flag is set to false when an item from a different store is
  //added to the system.
  private boolean isItemFromCurrentStore = true;

  /**
   * Constructor
   * @param id String
   */
  public CMSItem(String id) {
    super(id);
    cmsItemDetail = new CMSItemDetail(id);
    itemStock = new ItemStock();
  }

  /**
   * This method is used to get item details
   * @return CMSItemDetail
   */
  public CMSItemDetail getItemDetail() {
    return this.cmsItemDetail;
  }

  /**
   * This method is used to get item stock
   * @return ItemStock
   */
  public ItemStock getItemStock() {
    return this.itemStock;
  }

  /**
   * This method is used to get item label
   * @return String
   */
  public String getLabel() {
    return this.label;
  }

  /**
   * This method is used to set item label
   * @param label String
   */
  public void setLabel(String label) {
    doSetLabel(label);
  }

  /**
   * This method is used to set item label
   * @param label String
   */
  public void doSetLabel(String label) {
    this.label = label;
  }

  /**
   *
   * @return String
   */
  public String getForTheYear() {
    return this.forTheYear;
  }

  /**
   *
   * @param forTheYear String
   */
  public void setForTheYear(String forTheYear) {
    doSetForTheYear(forTheYear);
  }

  /**
   *
   * @param forTheYear String
   */
  public void doSetForTheYear(String forTheYear) {
    this.forTheYear = forTheYear;
  }

  /**
   * This method is used to set item bar code
   * @return String
   */
  public String getBarCode() {
    return this.barCode;
  }

  /**
   * This method is used to get item bar code
   * @param barCode String
   */
  public void setBarCode(String barCode) {
    doSetBarCode(barCode);
  }

  /**
   * This method is used to set item bar code
   * @param barCode String
   */
  public void doSetBarCode(String barCode) {
    this.barCode = barCode;
  }

  /**
   *
   * @return String
   */
  public String getSubline() {
    return this.subline;
  }

  /**
   *
   * @param subline String
   */
  public void setSubLine(String subline) {
    doSetSubline(subline);
  }

  /**
   *
   * @param subline String
   */
  public void doSetSubline(String subline) {
    this.subline = subline;
  }

  /**
   * This method is used to get the gender for which item for
   * @return String
   */
  public String getGender() {
    return this.gender;
  }

  /**
   * This method is used to set the gender for which item for
   * @param gender String
   */
  public void setGender(String gender) {
    doSetGender(gender);
  }

  /**
   * This method is used to set the gender for which item for
   * @param gender String
   */
  public void doSetGender(String gender) {
    this.gender = gender;
  }

  /**
   * This method is used to get item category
   * @return String
   */
  public String getCategory() {
    return this.category;
  }

  /**
   * This method is used to set item category
   * @param category String
   */
  public void setCategory(String category) {
    doSetCategory(category);
  }

  /**
   * This method is used to set item category
   * @param category String
   */
  public void doSetCategory(String category) {
    this.category = category;
  }

  /**
   *
   * @return String
   */
  public String getItemDrop() {
    return this.itemDrop;
  }

  /**
   *
   * @param itemDrop String
   */
  public void setItemDrop(String itemDrop) {
    doSetItemDrop(itemDrop);
  }

  /**
   *
   * @param itemDrop String
   */
  public void doSetItemDrop(String itemDrop) {
    this.itemDrop = itemDrop;
  }

  /**
   *
   * @return String
   */
  public String getVariant() {
    return this.variant;
  }

  /**
   *
   * @param variant String
   */
  public void setVariant(String variant) {
    doSetVariant(variant);
  }

  /**
   *
   * @param variant String
   */
  public void doSetVariant(String variant) {
    this.variant = variant;
  }

  /**
   * This method is used to get item model
   * @return String
   */
  public String getModel() {
    return this.model;
  }

  /**
   * This method is used to set item model
   * @param model String
   */
  public void setModel(String model) {
    doSetModel(model);
  }

  /**
   * This method is used to set item model
   * @param model String
   */
  public void doSetModel(String model) {
    this.model = model;
  }

  /**
   * This method is used to get item fabric
   * @return String
   */
  public String getFabric() {
    return this.fabric;
  }

  /**
   * This method is used to set item fabric
   * @param fabric String
   */
  public void setFabric(String fabric) {
    doSetFabric(fabric);
  }

  /**
   * This method is used to set item fabric
   * @param fabric String
   */
  public void doSetFabric(String fabric) {
    this.fabric = fabric;
  }

  /**
   * This method is used to set item style number
   * @return String
   */
  public String getStyleNum() {
    return this.styleNum;
  }

  /**
   * This method is used to set item style number
   * @param styleNum String
   */
  public void setStyleNum(String styleNum) {
    doSetStyleNum(styleNum);
  }

  /**
   * This method is used to set item style number
   * @param styleNum String
   */
  public void doSetStyleNum(String styleNum) {
    this.styleNum = styleNum;
  }

  /**
   * This method is used to get item product number
   * @return String
   */
  public String getProductNum() {
    return this.productNum;
  }

  /**
   * This method is used to set item product number
   * @param productNum String
   */
  public void setProductNum(String productNum) {
    doSetProductNum(productNum);
  }

  /**
   * This method is used to set item product number
   * @param productNum String
   */
  public void doSetProductNum(String productNum) {
    this.productNum = productNum;
  }

  /**
   *
   * @return String
   */
  public String getScItemSls() {
    return this.scItemSls;
  }

  /**
   *
   * @param scItemSls String
   */
  public void setScItemSls(String scItemSls) {
    doSetScItemSls(scItemSls);
  }

  /**
   *
   * @param scItemSls String
   */
  public void doSetScItemSls(String scItemSls) {
    this.scItemSls = scItemSls;
  }

  /**
   * This method is used to get item brand id
   * @return String
   */
  public String getBrand() {
    return this.brandId;
  }

  /**
   * This method is used to set item brand id
   * @param nmBrn String
   */
  public void setBrand(String brandId) {
    doSetBrand(brandId);
  }

  /**
   * This method is used to set item brand id
   * @param nmBrn String
   */
  public void doSetBrand(String brandId) {
    this.brandId = brandId;
  }

  /**
   * This method is used to get item class id
   * @return String
   */
  public String getClassId() {
    return this.classId;
  }

  /**
   * This method is used to set item class id
   * @param nmClass String
   */
  public void setClassId(String classId) {
    doSetClassId(classId);
  }

  /**
   * This method is used to set item class id
   * @param nmClass String
   */
  public void doSetClassId(String classId) {
    this.classId = classId;
  }

  /**
   * This method is used to get item color id
   * @return String
   */
  public String getColorId() {
    return this.colorId;
  }

  /**
   * This method is used to set item color id
   * @param colorId String
   */
  public void setColorId(String colorId) {
    doSetColorId(colorId);
  }

  /**
   * This method is used to set item color id
   * @param colorId String
   */
  public void doSetColorId(String colorId) {
    this.colorId = colorId;
  }

  /**
   * This method is used to get item season
   * @return String
   */
  public String getSeason() {
    return this.season;
  }

  /**
   * This method is used to set item season
   * @param season String
   */
  public void setSeason(String season) {
    doSetSeason(season);
  }

  /**
   * This method is used to set item season
   * @param season String
   */
  public void doSetSeason(String season) {
    this.season = season;
  }

  /**
   * This method is used to get item size id
   * @return String
   */
  public String getSizeId() {
    return this.sizeId;
  }

  /**
   * This method is used to set item size id
   * @param sizeId String
   */
  public void setSizeId(String sizeId) {
    doSetSizeId(sizeId);
  }

  /**
   * This method is used to set item size id
   * @param sizeId String
   */
  public void doSetSizeId(String sizeId) {
    this.sizeId = sizeId;
  }

  /**
   * This method is used to get item's kid size
   * @return String
   */
  public String getKidsSize() {
    return this.kidsSize;
  }

  /**
   * This method is used to set item's kid size
   * @param kidsSize String
   */
  public void setKidsSize(String kidsSize) {
    doSetKidsSize(kidsSize);
  }

  /**
   * This method is used to set item's kid size
   * @param kidsSize String
   */
  public void doSetKidsSize(String kidsSize) {
    this.kidsSize = kidsSize;
  }

  /**
   * This method is used to get item's supplier id
   * @return String
   */
  public String getSupplierId() {
    return this.supplierId;
  }

  /**
   * This method is used to set item's supplier id
   * @param supplierName String
   */
  public void setSupplierId(String supplierId) {
    doSetSupplierId(supplierId);
  }

  /**
   * This method is used to set item's supplier id
   * @param supplierName String
   */
  public void doSetSupplierId(String supplierId) {
    this.supplierId = supplierId;
  }

  /**
   * This method is used to get item's sub class id
   * @return String
   */
  public String getSubClassId() {
    return this.subClassId;
  }

  /**
   * This method is used to set item's sub class id
   * @param subClassName String
   */
  public void setSubClassId(String subClassId) {
    doSetSubClassId(subClassId);
  }

  /**
   * This method is used to set item's sub class id
   * @param subClassId String
   */
  public void doSetSubClassId(String subClassId) {
    this.subClassId = subClassId;
  }

  /**
   * This method is used to get item's store id
   * @return String
   */
  public String getStoreId() {
    return this.storeId;
  }

  /**
   * This method is used to set item's store id
   * @param storeId String
   */
  public void setStoreId(String storeId) {
    doSetStoreId(storeId);
  }

  /**
   * This method is used to set item's store id
   * @param storeId String
   */
  public void doSetStoreId(String storeId) {
    this.storeId = storeId;
  }

  /**
   * This method is used to get item's currency code
   * @return String
   */
  public String getCurrencyCode() {
    return this.currencyCode;
  }

  /**
   * This method is used to set item's currency code
   * @param currencyCode String
   */
  public void setCurrencyCode(String currencyCode) {
    doSetCurrencyCode(currencyCode);
  }

  /**
   * This method is used to set item's currency code
   * @param currencyCode String
   */
  public void doSetCurrencyCode(String currencyCode) {
    this.currencyCode = currencyCode;
  }

  /**
   *
   * @return String
   */
  public String getPosDept() {
    return this.posDept;
  }

  /**
   *
   * @param posDept String
   */
  public void setPosDept(String posDept) {
    doSetPosDept(posDept);
  }

  /**
   *
   * @param posDept String
   */
  public void doSetPosDept(String posDept) {
    this.posDept = posDept;
  }

  /**
   * This method is used to get promotion on item
   * @return String
   */
  public String getPromotion() {
    return "";
  }

  /**
   * This method is used to get item's updated date
   * @return Date
   */
  public Date getUpdateDate() {
    return this.updateDate;
  }

  /**
   * This method is used to set item's updated date
   * @param updateDate Date
   */
  public void SetUpdateDate(Date updateDate) {
    doSetUpdateDate(updateDate);
  }

  /**
   * This method is used to set item's updated date
   * @param updateDate Date
   */
  public void doSetUpdateDate(Date updateDate) {
    this.updateDate = updateDate;
  }

  /**
   * This method is used to set item's store name
   * @param val String
   */
  public void doSetStoreName(String val) {
    this.storeName = val;
  }

  /**
   * This method is used to set item's store name
   * @param val String
   */
  public void setStoreName(String val) {
    this.doSetStoreName(val);
  }

  /**
   * This method is used to get item's store name
   * @return String
   */
  public String getStoreName() {
    return this.storeName;
  }

  /**
   *
   * @param isdeposit boolean
   */
  public void setIsDeposit(boolean isdeposit) {
    doSetIsDeposit(isdeposit);
  }

  /**
   *
   * @param isdeposit boolean
   */
  public void doSetIsDeposit(boolean isdeposit) {
    this.isDeposit = isdeposit;
  }

  /**
   *
   * @return boolean
   */
  public boolean isDeposit() {
    return this.isDeposit;
  }
  
  
  /**
  *
  * @param isItemFromCurrentStore boolean
  */
 public void setIsItemFromCurrentStore(boolean isItemFromCurrentStore) {
   doSetIsItemFromCurrentStore(isItemFromCurrentStore);
 }

 /**
  *
  * @param isItemFromCurrentStore boolean
  */
 public void doSetIsItemFromCurrentStore(boolean isItemFromCurrentStore) {
   this.isItemFromCurrentStore = isItemFromCurrentStore;
 }

 /**
  *
  * @return boolean
  */
 public boolean isItemFromCurrentStore() {
   return this.isItemFromCurrentStore;
 }
 /**
 *fix for 1865
 * @return void
 */
 //Added setter-getter for itemId by deepika
 public String getId() {
		if(id == null )
		return super.getId();
		else 
			return id;
	}

	public void setId(String id) {
		this.id = id;
	}
	
	//ended code by deepika
	
 public void updateItemId(){
	 if(getId().length()<this.getBarCode().length())
 	   updateItemId(getBarCode());
 }
 
 public void updateItemId(String barcode){
	 System.out.println("Ruchi adding barcode :"+barcode);
	 this.barCode = barCode;
 }

}

