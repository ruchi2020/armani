/*
 * put your module comment here
 * formatted with JxBeauty (c) johann.langhofer@nextra.at
 */


package com.chelseasystems.cs.address;

import com.chelseasystems.cr.telephone.Telephone;
import java.io.Serializable;

/**
 * <p>Title: Address.java</p>
 *
 * <p>Description:Business object for storing Customer address </p>
 *
 * <p>Copyright: Copyright (c) 2005</p>
 *
 * <p>Company: Skillnet Inc.</p>
 *
 * @author Manpreet S Bawa
 * @version 1.0
 */
/*
 History:
 +------+------------+-----------+-----------+----------------------------------------------------+
 | Ver# | Date       | By        | Defect #  | Description                                        |
 +------+------------+-----------+-----------+----------------------------------------------------+
 | 4    | 04-04-2006 |David Fung | PCR67     | QAS                                                |
 +------+------------+-----------+-----------+----------------------------------------------------+
 | 3    | 03-29-2009 | Khyati    |           | Test after branching                               |
 +------+------------+-----------+-----------+----------------------------------------------------+
 | 2    | 06-22-2005 | Vikram    | 262       | Introduced AddressFormat, removed EuropeanCountry  |
 +------+------------+-----------+-----------+----------------------------------------------------+
 | 1    | 03-21-2005 | Manpreet  | N/A       | POS_104665_FS_CustomerManagement_Rev2              |
 +------+------------+-----------+-----------+----------------------------------------------------+
 */
public class Address implements Serializable {

  private boolean qasModified = false;
  /**
   * Customer Id
   */
  private String sAddressID;
  /**
   * Customer Id
   */
  private String sCustomerID;
  /**
   * Address Type
   */
  private String sAddressType;
  /**
   * AddressLine 1
   */
  private String sAddressLine1;
  /**
   * AddressLine 2
   */
  private String sAddressLine2;
  /**
   * City
   */
  private String sCity;
  /**
   * State
   */
  private String sState;
  /**
   * Zip code
   */
  private String sZipCode;
  /**
   * Zip code extension
   */
  private String sZipCodeExt;
  /**
   * Primary Telephone
   */
  private Telephone telePrimary;
  /**
   * Secondary Telephone
   */
  private Telephone teleSecondary;
  /**
   * Ternary Telephone
   */
  private Telephone teleTernary;
  /**
   * Use As Primary
   */
  private boolean bUseAsPrimary;
  /**
   * Country
   */
  private String sCountry;
  /**
   * Municipality
   */
  private String sMunicipality;
  /**
   * Directional
   */
  private String sDirectional;
  /**
   *
   */
  private boolean isModified;
  /**
   *
   */
  private boolean isRemove;
  /**
   * Address Panel Type
   */
  private String addressFormat;
  private String oldZipCode;
  private String verifyLevel;
  private boolean validateZipCode;

  /**
   * Default Constructor
   */
  public Address() {
	validateZipCode = true;
  }

  /**
   * Set Directional
   * @param sValue
   */
  public void doSetDirectional(String sValue) {
    this.sDirectional = sValue;
  }

  /**
   * Set Directional
   * @param sValue
   */
  public void setDirectional(String sValue) {
	this.doSetDirectional(sValue);
  }

  /**
   * Get Directional
   * @return
   */
  public String getDirectional() {
    return this.sDirectional;
  }

  /**
   * Set Country
   * @param sValue
   */
  public void doSetCountry(String sValue) {
	this.sCountry = sValue;
  }
 
  /**
   * Set Country
   * @param sValue
   */
  public void setCountry(String sValue) {
	if (!sValue.equalsIgnoreCase(this.sCountry)) {
		this.doSetCountry(sValue);
		this.qasModified = false;
	} else return;
  }

  /**
   * Get Country
   * @return Country
   */
  public String getCountry() {
    return this.sCountry;
  }

  /**
   * Set Customer ID
   * @param sValue
   */
  public void doSetCustomerId(String sValue) {
    if (sValue == null || sValue.length() < 1) {
      return;
    }
    this.sCustomerID = sValue;
  }

  /**
   * Set Customer ID
   * @param sValue
   */
  public void setCustomerId(String sValue) {
	this.doSetCustomerId(sValue);
}

  /**
   * Get Customer ID
   * @return CustomerId
   */
  public String getCustomerId() {
    return this.sCustomerID;
  }

  /**
   * Set Address ID
   * @param sValue
   */
  public void doSetAddressId(String sValue) {
	this.sAddressID = sValue;
  }

  /**
   * Set Address ID
   * @param sValue AddressID
   */
  public void setAddressId(String sValue) {
	this.doSetAddressId(sValue);
  }

  /**
   * Get Address ID
   * @return Address Id
   */
  public String getAddressId() {
    return this.sAddressID;
  }

  /**
   * Set Address Line 1
   * @param sValue AddressLine1
   */
  public void doSetAddressLine1(String sValue) {
    this.sAddressLine1 = sValue;
  }

  /**
   * Set Address Line 1
   * @param sValue
   */
  public void setAddressLine1(String sValue) {
	if (!sValue.equalsIgnoreCase(this.sAddressLine1)) {
		this.doSetAddressLine1(sValue);
		this.qasModified = false;
	} else return;
  }

  /**
   * Get Address Line 1
   * @return AddressLine1
   */
  public String getAddressLine1() {
    return this.sAddressLine1;
  }

  /**
   * Set Address Line 2
   * @param sValue AddressLine2
   */
  public void doSetAddressLine2(String sValue) {
    this.sAddressLine2 = sValue;
  }

  /**
   * Set Address Line 2
   * @param sValue
   */
  public void setAddressLine2(String sValue) {
	// To handle Address Line 2 if empty
	if (!((sValue == null || sValue.equals("")) && this.sAddressLine2 == null)
			&& !sValue.equalsIgnoreCase(this.sAddressLine2)) {
			this.doSetAddressLine2(sValue);
			this.qasModified = false;
	} else return;
}

  /**
   * Get Address Line 2
   * @return AddressLine2
   */
  public String getAddressLine2() {
    return this.sAddressLine2;
  }

  /**
   * Set City
   * @param sValue City
   */
  public void doSetCity(String sValue) {
	this.sCity = sValue;
	this.validateZipCode = true;
  }

  /**
   * Set City
   * @param sValue
   */
  public void setCity(String sValue) {
	if (!sValue.equalsIgnoreCase(this.sCity)) {
		this.doSetCity(sValue);
		this.qasModified = false;
	} else return;
  }

  /**
   * Get City
   * @return City
   */
  public String getCity() {
    return this.sCity;
  }

  /**
   * Set State
   * @param sValue State
   */
  public void doSetState(String sValue) {
	this.sState = sValue;
	this.validateZipCode = true;
  }

  /**
   * Set State
   * @param sValue
   */
  public void setState(String sValue) {
	if (!sValue.equalsIgnoreCase(this.sState)) {
		this.doSetState(sValue);
		this.qasModified = false;
	} else return;
  }

  /**
   * Get State
   * @return State
   */
  public String getState() {
    return this.sState;
  }

  /**
   * Set ZipCode
   * @param sValue ZipCode
   */
  public void doSetZipCode(String sValue) {
	this.sZipCode = sValue;
	this.validateZipCode = true;
  }

  /**
   * Set ZipCode
   * @param sValue
   */
  public void setZipCode(String sValue) {
	if (!sValue.equalsIgnoreCase(this.sZipCode)) {
		this.doSetZipCode(sValue);
		this.qasModified = false;
	} else return;
  }

  /**
   * Get Zipcode
   * @return ZipCode
   */
  public String getZipCode() {
    return this.sZipCode;
  }

  /**
   * Set PrimaryPhone
   * @param telePrimary PrimaryPhone
   */
  public void doSetPrimaryPhone(Telephone telePrimary) {
	if (telePrimary == null) {
		return;
	}
	this.telePrimary = telePrimary;
  }

  /**
   * Set PrimaryPhone
   * @param telePrimary
   */
  public void setPrimaryPhone(Telephone telePrimary) {
	this.doSetPrimaryPhone(telePrimary);
  }

  /**
   * Get Primary Phone.
   * @return PrimaryPhone
   */
  public Telephone getPrimaryPhone() {
    return this.telePrimary;
  }

  /**
   * Set SecondaryPhone.
   * @param telePrimary SecondaryPhone
   */
  public void doSetSecondaryPhone(Telephone teleSecondary) {
	if (teleSecondary == null) {
      return;
	}
	this.teleSecondary = teleSecondary;
  }

  /**
   * Set SecondaryPhone.
   * @param telePrimary
   */
  public void setSecondaryPhone(Telephone telePrimary) {
	this.doSetSecondaryPhone(telePrimary);
  }

  /**
   * Get SecondaryPhone.
   * @return SecondaryPhone
   */
  public Telephone getSecondaryPhone() {
    return this.teleSecondary;
  }

  /**
   * Set Ternaryphone.
   * @param telePrimary Ternaryphone
   */
  public void doSetTernaryPhone(Telephone teleTernary) {
	if (teleTernary == null) {
		  return;
    }
	this.teleTernary = teleTernary;
	}

  /**
   * Set Ternaryphone.
   * @param telePrimary
   */
  public void setTernaryPhone(Telephone telePrimary) {
	this.doSetTernaryPhone(telePrimary);
  }

  /**
   * Get TernaryPhone.
   * @return TernaryPhone
   */
  public Telephone getTernaryPhone() {
    return this.teleTernary;
  }

  /**
   * Set Address Type
   * @param sValue AddressType
   */
  public void doSetAddressType(String sValue) {
	if (sValue == null || sValue.length() < 1) {
      return;
    }
	this.sAddressType = sValue;
  }

  /**
   * Set Address Type
   * @param sValue
   */
  public void setAddressType(String sValue) {
	this.doSetAddressType(sValue);
  }

  /**
   * Get Address Type
   * @return AddressType
   */
  public String getAddressType() {
    return this.sAddressType;
  }

  /**
   * Set Use as Primary
   * @param bUsePrimary true/false
   */
  public void doSetUseAsPrimary(boolean bUsePrimary) {
	this.bUseAsPrimary = bUsePrimary;
  }

  /**
   * Set Use as Primary
   * @param bUsePrimary true/false
   */
  public void setUseAsPrimary(boolean bUsePrimary) {
	this.doSetUseAsPrimary(bUsePrimary);
  }

  /**
   * Use As Primary
   * @return True/False
   */
  public boolean isUseAsPrimary() {
    return this.bUseAsPrimary;
  }

  /**
   * Method used to set address is modified
   * @param val boolean
   */
  public void doSetIsModified(boolean val) {
	this.isModified = val;
  }
  
  /**
   * Method used to set address is modified
   * @param val
   */
  public void setIsModified(boolean val) {
	this.doSetIsModified(val);
  }

  /**
   * Method check is address modified
   * @return boolean
   */
  public boolean isModified() {
    return this.isModified;
  }

  /**
   * Method used to set address is removed
   * @param val
   */
  public void doSetIsRemove(boolean val) {
	this.isRemove = val;
  }

  /**
   * Method used to set address is removed
   * @param val boolean
   */
  public void setIsRemove(boolean val) {
	this.doSetIsRemove(val);
  }

  /**
   * Method check is address removed
   * @return boolean
   */
  public boolean isRemove() {
    return this.isRemove;
  }

  /**
   * Set Address Format
   * @param sValue
   */
  public void doSetAddressFormat(String sValue) {
	this.addressFormat = sValue;
  }

  /**
   * Set Address Format
   * @param sValue
   */
  public void setAddressFormat(String addressFormat) {
	this.doSetAddressFormat(addressFormat);
  }

  /**
   * Get Address Format
   * @return String
   */
  public String getAddressFormat() {
    return addressFormat;
  }

  /**
   * Set zipcode extension
   * @param val String
   */
  public void doSetZipCodeExtension(String val) {
	this.sZipCodeExt = val;
  }

  /**
   * Set zipcode extension
   * @param val String
   */
  public void setZipCodeExtension(String val) {
	this.doSetZipCodeExtension(val);
  }

  /**
   * Get zipcode extension
   * @return String
   */
  public String getZipCodeExtension() {
    return this.sZipCodeExt;
  }

  /**
   * Set old zipcode
   * @param val
   */
  public void doSetOldZipCode(String val) {
	this.oldZipCode = val;
  }

  /**
   * Set old zipcode
   * @param val
   */
  public void setOldZipCode(String val) {
	  this.doSetOldZipCode(val);
  }

  /**
   * Get old zipcode
   * @return
   */
  public String getOldZipCode() {
	return this.oldZipCode;
  }

  /**
   * Set QAS modified
   * @param qasModified
   */
  public void doSetQasModified(boolean qasModified) {
	this.qasModified = qasModified;
  }

  /**
   * Set QAS modified
   * @param qasModified
   */
  public void setQasModified(boolean qasModified) {
	this.doSetQasModified(qasModified);
  }

  /**
   * @return Returns the qasModified.
   */
  public boolean isQasModified() {
	return qasModified;
  }

  /**
   * Set QAS Verify Level
   * @param level
   */
  public void doSetQasVerifyLevel(String level) {
	  this.verifyLevel = level;
  }

  /**
   * Set QAS Verify Level
   * @param level
   */
  public void setQasVerifyLevel(String level) {
	this.doSetQasVerifyLevel(level);
  }

  /**
   * Get QAS Verify Level
   * @return
   */
  public String getQasVerifyLevel() {
	return this.verifyLevel;
}

  /**
   * Set validate zipcode
   * @param validate
   */
  public void doSetValidateZipCode(boolean validate) {
	this.validateZipCode = validate;
  }

  /**
   * Set validate zipcode
   * @param validate
   */
  public void setValidateZipCode(boolean validate) {
	  this.doSetValidateZipCode(validate);
  }

  /**
   * Should validate zipcode
   * @param newZipCode
   * @return
   */
  public boolean shouldValidateZipCode(String newZipCode) {
	return (!(newZipCode.equals(this.sZipCode)));
  }

  public String toString() {
	StringBuffer sb = new StringBuffer();
	sb.append("com.chelseasystems.cs.address.Address:");
	sb.append("\nUseAsPrimary " + this.bUseAsPrimary);
	sb.append("\nModified " + this.isModified);
	sb.append("\nRemove " + this.isRemove);
	sb.append("\nAddressFormat " + this.addressFormat);
	sb.append("\nOldZipCode " + this.oldZipCode);
	sb.append("\nAddressLine1 " + this.sAddressLine1);
	sb.append("\nAddressLine2 " + this.sAddressLine2);
	sb.append("\nAddressType " + this.sAddressType);
	sb.append("\nCity " + this.sCity);
	sb.append("\nCountry " + this.sCountry);
	sb.append("\nCustomerID " + this.sCustomerID);
	sb.append("\nDirectional " + this.sDirectional);
	sb.append("\nMunicipality " + this.sMunicipality);
	sb.append("\nState " + this.sState);
	sb.append("\nZipCode " + this.sZipCode);
	sb.append("\nZipCodeExt " + this.sZipCodeExt);
	sb.append("\nTelePrimary " + this.telePrimary);
	sb.append("\nTeleSecondary " + this.teleSecondary);
	sb.append("\nTeleTernary " + this.teleTernary);
	sb.append("\nAddressID " + this.sAddressID);
	sb.append("\nQAS Modified " + this.qasModified);
	return sb.toString();
  }
}
