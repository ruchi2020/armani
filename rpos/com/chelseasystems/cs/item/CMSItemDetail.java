/**
 * History (tab separated):-
 * Vers	Date		By	Spec		                Description
 * 1	3/10/05	        KS	POS_IS_ItemDownload_Rev1	CMSItem
 *
 *
 * formatted with JxBeauty (c) johann.langhofer@nextra.at
 */


package com.chelseasystems.cs.item;

import com.chelseasystems.cr.item.*;
import com.chelseasystems.cr.currency.*;
import com.chelseasystems.cr.business.BusinessObject;


/**
 *
 * <p>Title: CMSItemDetail</p>
 * <p>Description: This class stores the details of item</p>
 * <p>Copyright: Copyright (c) 2005</p>
 * <p>Company: </p>
 * @author not attributable
 * Created By:Khyati Shah
 * @version 1.0
 */
public class CMSItemDetail extends BusinessObject implements com.chelseasystems.cr.rules.
    IRuleEngine {
  static final long serialVersionUID = 8350808060163712091L;
  private String seasonDesc = null;
  private String brandDesc = null;
  private String labelDesc = null;
  private String sublineDesc = null;
  private String genderDesc = null;
  private String categoryDesc = null;
  private String dropDesc = null;
  private String sizeDesc = null;
  private String sizeIndx = null;
  private String extSizeIndx = null;
  private String kidsSizeDesc = null;
  private String colorDesc = null;
  private String productDesc = null;
  private String posDeptDesc = null;
  private String subClassDesc = null;
  private String classDesc = null;
  private String supplierName = null;
  private String itemId = null;

  /**
   * Constructor
   * @param id String
   */
  public CMSItemDetail(String itemId) {
    itemId = itemId;
  }

  /**
   * This method is used to get item season description
   * @return String
   */
  public String getSeasonDesc() {
    return this.seasonDesc;
  }

  /**
   * This method is used to set item season description
   * @param seasonDesc String
   */
  public void setSeasonDesc(String seasonDesc) {
    doSetSeasonDesc(seasonDesc);
  }

  /**
   * This method is used to set item season description
   * @param seasonDesc String
   */
  public void doSetSeasonDesc(String seasonDesc) {
    this.seasonDesc = seasonDesc;
  }

  /**
   * This method is used to get item's brand description
   * @return String
   */
  public String getBrandDesc() {
    return this.brandDesc;
  }

  /**
   * This method is used to set item's brand description
   * @param brandDesc String
   */
  public void setBrandDesc(String brandDesc) {
    doSetBrandDesc(brandDesc);
  }

  /**
   * This method is used to set item's brand description
   * @param brandDesc String
   */
  public void doSetBrandDesc(String brandDesc) {
    this.brandDesc = brandDesc;
  }

  /**
   * This method is used to get item's label description
   * @return String
   */
  public String getLabelDesc() {
    return this.labelDesc;
  }

  /**
   * This method is used to set item's label description
   * @param labelDesc String
   */
  public void setLabelDesc(String labelDesc) {
    doSetLabelDesc(labelDesc);
  }

  /**
   * This method is used to set item's label description
   * @param labelDesc String
   */
  public void doSetLabelDesc(String labelDesc) {
    this.labelDesc = labelDesc;
  }

  /**
   *
   * @return String
   */
  public String getSubLineDesc() {
    return this.sublineDesc;
  }

  /**
   *
   * @param sublineDesc String
   */
  public void setSublineDesc(String sublineDesc) {
    doSetSublineDesc(sublineDesc);
  }

  /**
   *
   * @param sublineDesc String
   */
  public void doSetSublineDesc(String sublineDesc) {
    this.sublineDesc = sublineDesc;
  }

  /**
   * This method is used to get item's gender description
   * @return String
   */
  public String getGenderDesc() {
    return this.genderDesc;
  }

  /**
   * This method is used to set item's gender description
   * @param genderDesc String
   */
  public void setGenderDesc(String genderDesc) {
    doSetGenderDesc(genderDesc);
  }

  /**
   * This method is used to set item's gender description
   * @param genderDesc String
   */
  public void doSetGenderDesc(String genderDesc) {
    this.genderDesc = genderDesc;
  }

  /**
   * This method is used to get item's category description
   * @return String
   */
  public String getCategoryDesc() {
    return this.categoryDesc;
  }

  /**
   * This method is used to set item's category description
   * @param categoryDesc String
   */
  public void setCategoryDesc(String categoryDesc) {
    doSetGenderDesc(categoryDesc);
  }

  /**
   * This method is used to set item's category description
   * @param categoryDesc String
   */
  public void doSetCategoryDesc(String categoryDesc) {
    this.categoryDesc = categoryDesc;
  }

  /**
   *
   * @return String
   */
  public String getDropDesc() {
    return this.dropDesc;
  }

  /**
   *
   * @param dropDesc String
   */
  public void setDropDesc(String dropDesc) {
    doSetDropDesc(dropDesc);
  }

  /**
   *
   * @param dropDesc String
   */
  public void doSetDropDesc(String dropDesc) {
    this.dropDesc = dropDesc;
  }

  /**
   * This method is used to get item's size description
   * @return String
   */
  public String getSizeDesc() {
    return this.sizeDesc;
  }

  /**
   * This method is used to set item's size description
   * @param sizeDesc String
   */
  public void setSizeDesc(String sizeDesc) {
    doSetSizeDesc(sizeDesc);
  }

  /**
   * This method is used to set item's size description
   * @param sizeDesc String
   */
  public void doSetSizeDesc(String sizeDesc) {
    this.sizeDesc = sizeDesc;
  }

  /**
   * This method is used to get item's size index
   * @return String
   */
  public String getSizeIndx() {
    return this.sizeIndx;
  }

  /**
   * This method is used to set item's size index
   * @param sizeIndx String
   */
  public void setSizeIndx(String sizeIndx) {
    doSetSizeIndx(sizeIndx);
  }

  /**
   * This method is used to set item's size index
   * @param sizeIndx String
   */
  public void doSetSizeIndx(String sizeIndx) {
    this.sizeIndx = sizeIndx;
  }

  /**
   * This method is used to get item's external size index
   * @return String
   */
  public String getExtSizeIndx() {
    return this.extSizeIndx;
  }

  /**
   * This method is used to set item's external size index
   * @param extSizeIndx String
   */
  public void setExtSizeIndx(String extSizeIndx) {
    doSetExtSizeIndx(extSizeIndx);
  }

  /**
   * This method is used to set item's external size index
   * @param extSizeIndx String
   */
  public void doSetExtSizeIndx(String extSizeIndx) {
    this.extSizeIndx = extSizeIndx;
  }

  /**
   * This method is used to get item's kid size description
   * @return String
   */
  public String getKidsSizeDesc() {
    return this.kidsSizeDesc;
  }

  /**
   * This method is used to set item's kid size description
   * @param kidsSizeDesc String
   */
  public void setKidsSizeDesc(String kidsSizeDesc) {
    doSetKidsSizeDesc(kidsSizeDesc);
  }

  /**
   * This method is used to set item's kid size description
   * @param kidsSizeDesc String
   */
  public void doSetKidsSizeDesc(String kidsSizeDesc) {
    this.kidsSizeDesc = kidsSizeDesc;
  }

  /**
   * This method is used to get item color description
   * @return String
   */
  public String getColorDesc() {
    return this.colorDesc;
  }

  /**
   * This method is used to set item color description
   * @param colorDesc String
   */
  public void setColorDesc(String colorDesc) {
    doSetKidsSizeDesc(colorDesc);
  }

  /**
   * This method is used to set item color description
   * @param colorDesc String
   */
  public void doSetColorDesc(String colorDesc) {
    this.colorDesc = colorDesc;
  }

  /**
   * This method is used to get product description of an item
   * @return String
   */
  public String getProductDesc() {
    return this.productDesc;
  }

  /**
   * This method is used to set product description of an item
   * @param productDesc String
   */
  public void setProductDesc(String productDesc) {
    doSetKidsSizeDesc(productDesc);
  }

  /**
   * This method is used to set product description of an item
   * @param productDesc String
   */
  public void doSetProductDesc(String productDesc) {
    this.productDesc = productDesc;
  }

  /**
   *
   * @return String
   */
  public String getPosDeptDesc() {
    return this.posDeptDesc;
  }

  /**
   *
   * @param posDeptDesc String
   */
  public void setPosDeptDesc(String posDeptDesc) {
    doSetPosDeptDesc(posDeptDesc);
  }

  /**
   *
   * @param posDeptDesc String
   */
  public void doSetPosDeptDesc(String posDeptDesc) {
    this.posDeptDesc = posDeptDesc;
  }

  /**
   * This method is used to get item sub class desc
   * @return String
   */
  public String getSubClassDesc() {
    return this.subClassDesc;
  }

  /**
   * This method is used to set item sub class desc
   * @param subClassDesc String
   */
  public void setSubClassDesc(String subClassDesc) {
    doSetSubClassDesc(subClassDesc);
  }

  /**
   * This method is used to set item sub class desc
   * @param subClassDesc String
   */
  public void doSetSubClassDesc(String subClassDesc) {
    this.subClassDesc = subClassDesc;
  }

  /**
   * This method is used to get item class desc
   * @return String
   */
  public String getClassDesc() {
    return this.classDesc;
  }

  /**
   * This method is used to set item class desc
   * @param classDesc String
   */
  public void setClassDesc(String classDesc) {
    doSetSubClassDesc(classDesc);
  }

  /**
   * This method is used to set item class desc
   * @param classDesc String
   */
  public void doSetClassDesc(String classDesc) {
    this.classDesc = classDesc;
  }

  /**
   * This method is used to get item supplier name
   * @return String
   */
  public String getSupplierName() {
    return this.supplierName;
  }

  /**
   * This method is used to set item supplier name
   * @param supplierName String
   */
  public void setSupplierName(String supplierName) {
    doSetSupplierName(supplierName);
  }

  /**
   * This method is used to set item supplier name
   * @param supplierName String
   */
  public void doSetSupplierName(String supplierName) {
    this.supplierName = supplierName;
  }
}

