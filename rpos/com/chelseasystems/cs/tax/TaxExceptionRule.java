/*
 * put your module comment here
 * formatted with JxBeauty (c) johann.langhofer@nextra.at
 */


package com.chelseasystems.cs.tax;

import com.chelseasystems.cr.business.BusinessObject;
import com.chelseasystems.cr.currency.ArmCurrency;
import java.util.*;


/**
 * <p>Title: TaxExceptionRule</p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2005</p>
 * <p>Company: </p>
 * @author unascribed
 * @version 1.0
 */
public class TaxExceptionRule extends BusinessObject {
  String exceptionState = null;
  ArmCurrency thresholdAmount = null;
  Vector applicableClass = null;
  double exceptionTaxRate = 0.0d;
  Vector exceptionZipCodes = null;
  Vector exceptionTaxJur = null;
  String aboveThresholdRule = null;
  Vector applicableProduct = null;
  Vector applicableBrand = null;
  /**
   * Constructor
   */
  TaxExceptionRule() {
    exceptionState = null;
    thresholdAmount = new ArmCurrency(0.0d);
    applicableClass = new Vector();
    exceptionTaxRate = 0.0d;
    exceptionZipCodes = new Vector();
    exceptionTaxJur = new Vector();
    aboveThresholdRule = "P";
    applicableProduct = new Vector();
    applicableBrand = new Vector();
  }

  /**
   * Method is used to get exception tax rate
   * @return double
   */
  public double getExceptionTaxRate() {
    return exceptionTaxRate;
  }

  /**
   * Method is used to set exception tax rate
   * @param exceptionTaxRate double
   */
  public void setExceptionTaxRate(double exceptionTaxRate) {
    this.exceptionTaxRate = exceptionTaxRate;
  }

  /**
   * Method is used to get item class
   * @return String[]
   */
  public String[] getItemClass() {
    return (String[])applicableClass.toArray(new String[0]);
  }

  /**
   * Method is used to get item class list
   * @return Vector
   */
  public Vector getItemClassList() {
    return applicableClass;
  }

  /**
   * Method is used to add item class
   * @param itemClass String
   */
  public void addItemClass(String itemClass) {
    applicableClass.add(itemClass);
  }

  /**
   * Method is used to get exception zip code
   * @return String[]
   */
  public String[] getExceptionZipCodes() {
    return (String[])this.exceptionZipCodes.toArray(new String[0]);
  }

  /**
   * Method is used to get exception zip code
   * @return Vector
   */
  public Vector getExceptionZipCodeList() {
    return this.exceptionZipCodes;
  }

  /**
   * Method is used to add exception zip code
   * @param zipcode String
   */
  public void addExceptionZipCode(String zipcode) {
    exceptionZipCodes.add(zipcode);
  }

  /**
   * Method is used to get exception tax jurisdiction list
   * @return String[]
   */
  public String[] getExceptionTaxJur() {
    return (String[])this.exceptionTaxJur.toArray(new String[0]);
  }

  /**
   * Method is used to get exception tax jur.
   * @return Vector
   */
  public Vector getExceptionTaxJurList() {
    return this.exceptionTaxJur;
  }

  /**
   * Method is used to add exception tax jur.
   * @param zipcode String
   */
  public void addExceptionTaxJur(String taxJur) {
    exceptionTaxJur.add(taxJur);
  }

  /**
   * Method is used to get threshold amount
   * @return Currency
   */
  public ArmCurrency getThresholdAmount() {
    return thresholdAmount;
  }

  /**
   * Method is used to set threshold amount
   * @param thresholdAmount Currency
   */
  public void setThresholdAmount(ArmCurrency thresholdAmount) {
    this.thresholdAmount = thresholdAmount;
  }

  /**
   * Method is used to get exception state
   * @return String
   */
  public String getExceptionState() {
    return exceptionState;
  }

  /**
   * Method is used to set exception state
   * @param state String
   */
  public void setExceptionState(String state) {
    this.exceptionState = state;
  }

  /**
   * Method is used to get above threshold rule
   * @return String
   */
  public String getAboveThresholdRule() {
    return this.aboveThresholdRule;
  }

  /**
   * Method is used to set above threshold rule
   * @param state String
   */
  public void setAboveThresholdRule(String rule) {
    this.aboveThresholdRule = rule;
  }
  
  //Added by Vivek Mishra for PCR_TaxExceptionRulesImplementationChanges_US
  //start
  
  /**
   * Method is used to get item class
   * @return String[]
   */
  public String[] getItemProduct() {
    return (String[])applicableProduct.toArray(new String[0]);
  }

  /**
   * Method is used to get item class list
   * @return Vector
   */
  // Fixed the product brand not set correctly
  public Vector getItemProductList() {
    return applicableProduct;
  }

  /**
   * Method is used to add item class
   * @param itemProduct String
   */
  public void addItemProduct(String itemProduct) {
	applicableProduct.add(itemProduct);
  }

  /**
   * Method is used to get item class
   * @return String[]
   */
  public String[] getItemBrand() {
    return (String[])applicableBrand.toArray(new String[0]);
  }

  /**
   * Method is used to get item class list
   * @return Vector
   */
  public Vector getItemBrandList() {
    return applicableBrand;
  }

  /**
   * Method is used to add item class
   * @param itemBrand String
   */
  public void addItemBrand(String itemBrand) {
    applicableBrand.add(itemBrand);
  }

  //end
}

