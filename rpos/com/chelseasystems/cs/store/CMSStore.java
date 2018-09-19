/*
 * put your module comment here
 * formatted with JxBeauty (c) johann.langhofer@nextra.at
 */
//
// Copyright 1999-2001, Chelsea Market Systems
//
/*
 History:
 +------+------------+-----------+-----------+----------------------------------------------------+
 | Ver# | Date       | By        | Defect #  | Description                                        |
 +------+------------+-----------+-----------+----------------------------------------------------+
 | 5   | 03-03-2005 | Pankaja  | N/A        |Modified  to add Company_Code                       |
 +------+------------+-----------+-----------+----------------------------------------------------+
 | 4    | 02-09-2005 | Manpreet  | N/A       |Modified  to add Company_Code,   Comp_city          |
 |      |            |           |           | CompanyPhone, CompanyAddress, CompanyPostCode      |
 +------+------------+-----------+-----------+----------------------------------------------------+
 | 3    | 01-27-2005 | Manpreet  | N/A       | Modified  to add FiscalCode,ShopDescription,       |
 |      |            |           |           | CompanyDescription                                 |
 +------+------------+-----------+-----------+----------------------------------------------------+
 | 2    | 01-24-2005 | Manpreet  | N/A       | Modified  to add BRAND_ID                          |
 +------+------------+-----------+-----------+----------------------------------------------------+
 */


package com.chelseasystems.cs.store;

import com.chelseasystems.cr.store.Store;
import com.chelseasystems.cr.config.ConfigMgr;


/**
 *
 * <p>Title: CMSStore</p>
 *
 * <p>Description: </p>
 *
 * <p>Copyright: Copyright (c) 2005</p>
 *
 * <p>Company: </p>
 *
 * @author not attributable
 * @version 1.0
 */
public class CMSStore extends Store implements com.chelseasystems.cr.rules.IRuleEngine {
  static final long serialVersionUID = 5003440766527108639L;
  private String sBrandID;
  private String sAddressLine2;
  private String sFiscalCode;
  private String sShopDescription;
  private String sCompanyDescription;
  private String compOfficePhoneArea;
  private String compOfficePhoneNumber;
  private String compAddressLine1;
  private String compAddressLine2;
  private String compCity;
  private String compState;
  private String compPostalCode;
  private String compCountry;
  private String sCompCode;
  private String sTaxJur;
  private String message;
  private Double stateTax;
  private Double gst_hstTAX ;
  private Double gstTAX ;
  private Double pstTAX ;
  private Double qstTAX ;
  private String registrationId;
  //Mahesh Nandure: 12-1-2017: markdown markup cr by sergio for EUROPE 
  private String storeType;
  private String timeZone;
  //Vishal yevale :CR from Riccardo Start of day Posting 21th March 2017
  private String digitalSignatureSOS;
  
	 //Vishal Yevale : Dec 2017 : POS_TS_NF25 Implementation PCR (FRANCE)
  private boolean isFranceStore;
  
  public boolean isFranceStore() {
	return isFranceStore;
}

public void checkForFranceStore() {
	ConfigMgr cfg = new ConfigMgr("store.cfg");
    if (cfg.getString("IS_FRANCE_STORE") != null && cfg.getString("IS_FRANCE_STORE").equalsIgnoreCase("TRUE")){
	this.isFranceStore = true;
    }else{
    	this.isFranceStore = false;
    }
}
// END Vishal Yevale : Dec 2017 : POS_TS_NF25 Implementation PCR (FRANCE)

public String getDigitalSignatureSOS() {
	return digitalSignatureSOS;
}

public void setDigitalSignatureSOS(String digitalSignatureSOS) {
	this.digitalSignatureSOS = digitalSignatureSOS;
}
//END VISHAL 21th March 2017
public String getTimeZone() {
	return timeZone;
}

public void setTimeZone(String timeZone) {
	this.timeZone = timeZone;
}

public String getStoreType() {
	return storeType;
}

public void setStoreType(String storeType) {
	this.storeType = storeType;
}

//end Mahesh Nandure

/**
   * Constructor
   * @param anId String
   */
  public CMSStore(String anId) {
    super(anId);
  }

  /**
   * Method return brand id
   * @return String
   */
  public String getBrandID() {
    return this.sBrandID;
  }

  /**
   * Method sets brand id
   * @param sBrandID String
   */
  public void setBrandID(String sBrandID) {
    doSetBrandID(sBrandID);
  }

  /**
   * Method sets brand id
   * @param sBrandID String
   */
  public void doSetBrandID(String sBrandID) {
    this.sBrandID = sBrandID;
  }

  /**
   * Method returns address line 2
   * @return String
   */
  public String getAddressLine2() {
    return this.sAddressLine2;
  }

  /**
   * Method set address line 2
   * @param sAddressLine2 String
   */
  public void setAddressLine2(String sAddressLine2) {
    doSetAddressLine2(sAddressLine2);
  }

  /**
   * Method set address line 2
   * @param sAddressLine2 String
   */
  public void doSetAddressLine2(String sAddressLine2) {
    this.sAddressLine2 = sAddressLine2;
  }

  /**
   * Method returns fiscal code
   * @return String
   */
  public String getFiscalCode() {
    return this.sFiscalCode;
  }

  /**
   * Method used to set fiscal code
   * @param sFiscalCode String
   */
  public void setFiscalCode(String sFiscalCode) {
    doSetFiscalCode(sFiscalCode);
  }

  /**
   * Method used to set fiscal code
   * @param sFiscalCode String
   */
  public void doSetFiscalCode(String sFiscalCode) {
    this.sFiscalCode = sFiscalCode;
  }

  /**
   * Method returns shop description
   * @return String
   */
  public String getShopDescription() {
    return this.sShopDescription;
  }

  /**
   * Method used to set shop description
   * @param sShopDescription String
   */
  public void setShopDescription(String sShopDescription) {
    doSetShopDescription(sShopDescription);
  }

  /**
   * Method used to set shop description
   * @param sShopDescription String
   */
  public void doSetShopDescription(String sShopDescription) {
    this.sShopDescription = sShopDescription;
  }

  /**
   * Method returns company discription
   * @return String
   */
  public String getCompanyDescription() {
    return this.sCompanyDescription;
  }

  /**
   * Method used to set company discription
   * @param sShopDesc String
   */
  public void setCompanyDescription(String sShopDesc) {
    doSetCompanyDescription(sShopDesc);
  }

  /**
   * Method used to set company discription
   * @param sShopDesc String
   */
  public void doSetCompanyDescription(String sShopDesc) {
    this.sCompanyDescription = sShopDesc;
  }

  /**
   * Method returns company office phone area
   * @return String
   */
  public String getCompOfficePhoneArea() {
    return this.compOfficePhoneArea;
  }

  /**
   * Method used to set company office phone area
   * @param compOfficePhoneArea String
   */
  public void setCompOfficePhoneArea(String compOfficePhoneArea) {
    doSetCompOfficePhoneArea(compOfficePhoneArea);
  }

  /**
   * Method used to set company office phone area
   * @param compOfficePhoneArea String
   */
  public void doSetCompOfficePhoneArea(String compOfficePhoneArea) {
    this.compOfficePhoneArea = compOfficePhoneArea;
  }

  /**
   * Method returns company office phone number
   * @return String
   */
  public String getCompOfficePhoneNumber() {
    return this.compOfficePhoneNumber;
  }

  /**
   * Method used to set company office phone number
   * @param compOfficePhoneNumber String
   */
  public void setCompOfficePhoneNumber(String compOfficePhoneNumber) {
    doSetCompOfficePhoneNumber(compOfficePhoneNumber);
  }

  /**
   * Method used to set company office phone number
   * @param compOfficePhoneNumber String
   */
  public void doSetCompOfficePhoneNumber(String compOfficePhoneNumber) {
    this.compOfficePhoneNumber = compOfficePhoneNumber;
  }

  /**
   * Method returns company address line 1
   * @return String
   */
  public String getCompAddressLine1() {
    return this.compAddressLine1;
  }

  /**
   * Method used to set company address line 1
   * @param compAddressLine1 String
   */
  public void setCompAddressLine1(String compAddressLine1) {
    doSetCompAddressLine1(compAddressLine1);
  }

  /**
   * Method used to set company address line 1
   * @param compAddressLine1 String
   */
  public void doSetCompAddressLine1(String compAddressLine1) {
    this.compAddressLine1 = compAddressLine1;
  }

  /**
   * Method returns company address line 2
   * @return String
   */
  public String getCompAddressLine2() {
    return this.compAddressLine2;
  }

  /**
   * Method used to set company address line 2
   * @param compAddressLine2 String
   */
  public void setCompAddressLine2(String compAddressLine2) {
    doSetCompAddressLine2(compAddressLine2);
  }

  /**
   * Method used to set company address line 2
   * @param compAddressLine2 String
   */
  public void doSetCompAddressLine2(String compAddressLine2) {
    this.compAddressLine2 = compAddressLine2;
  }

  /**
   * Method returns company city
   * @return String
   */
  public String getCompCity() {
    return this.compCity;
  }

  /**
   * Method used to set company city
   * @param compCity String
   */
  public void setCompCity(String compCity) {
    doSetCompCity(compCity);
  }

  /**
   * Method used to set company city
   * @param compCity String
   */
  public void doSetCompCity(String compCity) {
    this.compCity = compCity;
  }

  /**
   * Method used to get the company state
   * @return String
   */
  public String getCompState() {
    return this.compState;
  }

  /**
   * Method used to set the company state
   * @param compState String
   */
  public void setCompState(String compState) {
    doSetCompState(compState);
  }

  /**
   * Method used to set the company state
   * @param compState String
   */
  public void doSetCompState(String compState) {
    this.compState = compState;
  }

  /**
   * Method used to get the company postal code
   * @return String
   */
  public String getCompPostalCode() {
    return this.compPostalCode;
  }

  /**
   * Method used to set the company postal code
   * @param compPostalCode String
   */
  public void setCompPostalCode(String compPostalCode) {
    doSetCompPostalCode(compPostalCode);
  }

  /**
   * Method used to set the company postal code
   * @param compPostalCode String
   */
  public void doSetCompPostalCode(String compPostalCode) {
    this.compPostalCode = compPostalCode;
  }

  /**
   * Method used to get the company country
   * @return String
   */
  public String getCompCountry() {
    return this.compCountry;
  }

  /**
   * Method used to set the company country
   * @param compCountry String
   */
  public void setCompCountry(String compCountry) {
    doSetCompCountry(compCountry);
  }

  /**
   * Method used to set the company country
   * @param compCountry String
   */
  public void doSetCompCountry(String compCountry) {
    this.compCountry = compCountry;
  }

  /**
   * Method used to get the company code
   * @return String
   */
  public String getCompanyCode() {
    return sCompCode;
  }

  /**
   * Method used to set the company code
   * @param sCompCode String
   */
  public void setCompanyCode(String sCompCode) {
    doSetCompanyCode(sCompCode);
  }

  /**
   * Method used to set the company code
   * @param sCompCode String
   */
  public void doSetCompanyCode(String sCompCode) {
    this.sCompCode = sCompCode;
  }

  /**
   * Method used to get the tax jurisdiction
   * @return String
   */
  public String getTaxJurisdiction() {
    return this.sTaxJur;
  }

  /**
   * Method used to set the tax jurisdiction
   * @param sTaxJur String
   */
  public void setTaxJurisdiction(String sTaxJur) {
    doSetTaxJurisdiction(sTaxJur);
  }

  /**
   * Method used to set the tax jurisdiction
   * @param sTaxJur String
   */
  public void doSetTaxJurisdiction(String sTaxJur) {
    this.sTaxJur = sTaxJur;
  }

  /**
   * put your documentation comment here
   * @return
   */
  public boolean isOutlet() {
    ConfigMgr cfg = new ConfigMgr("store.cfg");
    if (cfg.getString("IS_OUTLET_STORE") != null && cfg.getString("IS_OUTLET_STORE").equals("true"))
      return true;
    return false;
  }

  /**
   * put your documentation comment here
   * @return
   */
  public String getShopCode() {
    String sId = super.getId();
    ConfigMgr config = new ConfigMgr("store.cfg");
    if (config.getString("EXTRACT_COMPANY_CODE") != null
        && config.getString("EXTRACT_COMPANY_CODE").equalsIgnoreCase("TRUE")) {
      return sId.substring(this.getCompanyCode().length());
    }
    return sId;
  }

  /**
   * put your documentation comment here
   * @return
   */
  public String getWebAddress() {    
    ConfigMgr config = new ConfigMgr("store.cfg");
    return config.getString("WEB_ADDRESS");
  }

  /**
   * put your documentation comment here
   * @return
   */
  public boolean isVATEnabled() {
    ConfigMgr config = new ConfigMgr("store.cfg");
    String vatEnabled = config.getString("VAT_ENABLED");
    if (vatEnabled != null && vatEnabled.equalsIgnoreCase("TRUE")) {
      return true;
    }
    return false;
  }

  public void doSetMessage(String message) {
    this.message = message;
  }

  public void setMessage(String message) {
    this.doSetMessage(message);
  }

  public String getMessage() {
    return message;
  }
  
  public Double getStateTax() {
	return stateTax;
}

public void setStateTax(Double stateTax) {
	this.doSetStateTax(stateTax);
}

public void doSetStateTax(Double stateTax) {
	this.stateTax = stateTax;
}

// FOR CANADIAN TAX CALCULATION
public Double getGst_hstTAX() {
	return gst_hstTAX;
}

public void setGst_hstTAX(Double gst_hstTAX) {
	this.doSetGst_hstTAX(gst_hstTAX);
}

public void doSetGst_hstTAX(Double gst_hstTAX){
	this.gst_hstTAX= gst_hstTAX;
}

public Double getGstTAX() {
	return gstTAX;
}

public void setGstTAX(Double gstTAX) {
	this.doSetGstTAX(gstTAX);
}

public void doSetGstTAX(Double gstTAX){
	this.gstTAX = gstTAX;
}

public Double getPstTAX() {
	return pstTAX;
}

public void setPstTAX(Double pstTAX) {
	this.doSetPstTAX(pstTAX);
}

public void doSetPstTAX(Double pstTAX){
	this.pstTAX = pstTAX;
}

public Double getQstTAX() {
	return qstTAX;
}

public void setQstTAX(Double qstTAX) {
	this.doSetQstTAX(qstTAX);
}

public void doSetQstTAX(Double qstTAX){
	this.qstTAX = qstTAX;
	
}
public String getRegistrationId() {
	return registrationId;
}

public void setRegistrationId(String registrationId) {
	this.doSetRegistrationId(registrationId);
}

public void doSetRegistrationId(String registrationId) {
	this.registrationId = registrationId;
}

}
