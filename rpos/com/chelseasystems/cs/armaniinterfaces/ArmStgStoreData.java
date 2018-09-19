/*
 History:
 +------+------------+-----------+-----------+----------------------------------------------------+
 | Ver# | Date       | By        | Defect #  | Description                                        |
 +------+------------+-----------+-----------+----------------------------------------------------+
 | 4    | 02-09-2005 | Manpreet  | N/A       |Modified  to add Company_Code,Company_desc,Comp_city|
 |      |            |           |           | CompanyPhone, CompanyAddress, CompanyPostCode      |
 +------+------------+-----------+-----------+----------------------------------------------------+
 | 3    | 01-27-2005 | Manpreet  | N/A       | Modified  to add FiscalCode, StoreDesc, CompanyDesc|
 +------+------------+-----------+-----------+----------------------------------------------------+
 | 2    | 01-24-2005 | Manpreet  | N/A       | Modified  to add BrandId, FrankingCompany          |
 +------+------------+-----------+-----------+----------------------------------------------------+
 | 1    | 01-20-2005 | Manpreet  | N/A       | Original Version                                   |
 +------+------------+-----------+-----------+----------------------------------------------------+
 *
 * formatted with JxBeauty (c) johann.langhofer@nextra.at
 */


package com.chelseasystems.cs.armaniinterfaces;

import java.util.Date;
import java.io.Serializable;


/**
 * <p>Title: ArmStgStoreData.java </p>
 *
 * <p>Description: Stores Armani Staging store data</p>
 *
 * <p>Copyright: Copyright (c) 2005</p>
 *
 * <p>Company: SkillNetInc </p>
 *
 * @author Bawa Manpreet Singh
 * @version 1.0
 * Date Created : 01/20/2005
 */
public class ArmStgStoreData implements Serializable {
  private Long lTranType;
  private String sRowID;
  private String sCompanyCode = "";
  private String sShopCode = "";
  private String sOfficePhoneArea;
  private String sOfficePhoneNumber;
  private String sAddressLine1;
  private String sAddressLine2;
  private String sCity;
  private String sState;
  private String sPostalCode;
  private String sCountry;
  private String sCurrency;
  private Double dTaxRate;
  private String sCountryCode;
  private String sLanguageCode;
  private Long lStgEventId;
  private Long lStgStatus;
  private String sStgErrorMessage;
  private String sBrandID;
  private String sFrankingCompanyName;
  private String sFrankingCompanyAcNum;
  private String sFiscalCode;
  private String sShopDescription;
  private String sCompanyDescription;
  private Date dtStgLoadDate;
  private Date dtStgProcessDate;
  // Manpreet S Bawa (02/09/05)
  private String compOfficePhoneArea;
  private String compOfficePhoneNumber;
  private String compAddressLine1;
  private String compAddressLine2;
  private String compCity;
  private String compState;
  private String compPostalCode;
  private String compCountry;

  /**
   * Default Constructor
   */
  public ArmStgStoreData() {
    System.out.println("creating data");
  }

  /**
   *
   * @return Long
   */
  public Long getTransactionType() {
    return this.lTranType;
  }

  /**
   *
   * @param lTranType Long
   */
  public void setTransactionType(Long lTranType) {
    this.lTranType = lTranType;
  }

  /**
   *
   * @return String
   */
  public String getRowID() {
    return this.sRowID;
  }

  /**
   *
   * @param sRowID String
   */
  public void setRowID(String sRowID) {
    this.sRowID = sRowID;
  }

  /**
   *
   * @return String
   */
  public String getShopCode() {
    return this.sShopCode;
  }

  /**
   *
   * @param sShopCode String
   */
  public void setShopCode(String sShopCode) {
    this.sShopCode = sShopCode;
  }

  /**
   *
   * @return String
   */
  public String getCompanyCode() {
    return this.sCompanyCode;
  }

  /**
   *
   * @param sCompanyCode String
   */
  public void setCompanyCode(String sCompanyCode) {
    this.sCompanyCode = sCompanyCode;
  }

  /**
   *
   * @return String
   */
  public String getOfficePhoneArea() {
    return this.sOfficePhoneArea;
  }

  /**
   *
   * @param sOfficePhoneArea String
   */
  public void setOfficePhoneArea(String sOfficePhoneArea) {
    this.sOfficePhoneArea = sOfficePhoneArea;
  }

  /**
   *
   * @return String
   */
  public String getOfficePhoneNumber() {
    return this.sOfficePhoneNumber;
  }

  /**
   *
   * @param sOfficePhoneNumber String
   */
  public void setOfficePhoneNumber(String sOfficePhoneNumber) {
    this.sOfficePhoneNumber = sOfficePhoneNumber;
  }

  /**
   *
   * @return String
   */
  public String getAddressLine1() {
    return this.sAddressLine1;
  }

  /**
   *
   * @param sAddressLine1 String
   */
  public void setAddressLine1(String sAddressLine1) {
    this.sAddressLine1 = sAddressLine1;
  }

  /**
   *
   * @return String
   */
  public String getAddressLine2() {
    return this.sAddressLine2;
  }

  /**
   *
   * @param sAddressLine2 String
   */
  public void setAddressLine2(String sAddressLine2) {
    this.sAddressLine2 = sAddressLine2;
  }

  /**
   *
   * @return String
   */
  public String getCity() {
    return this.sCity;
  }

  /**
   *
   * @param sCity String
   */
  public void setCity(String sCity) {
    this.sCity = sCity;
  }

  /**
   *
   * @return String
   */
  public String getState() {
    return this.sState;
  }

  /**
   *
   * @param sState String
   */
  public void setState(String sState) {
    this.sState = sState;
  }

  /**
   *
   * @return String
   */
  public String getPostalCode() {
    return this.sPostalCode;
  }

  /**
   *
   * @param sPostalCode String
   */
  public void setPostalCode(String sPostalCode) {
    this.sPostalCode = sPostalCode;
  }

  /**
   *
   * @return String
   */
  public String getCountry() {
    return this.sCountry;
  }

  /**
   *
   * @param sCountry String
   */
  public void setCountry(String sCountry) {
    this.sCountry = sCountry;
  }

  /**
   *
   * @return String
   */
  public String getCountryCode() {
    return this.sCountryCode;
  }

  /**
   *
   * @param sCountryCode String
   */
  public void setCountryCode(String sCountryCode) {
    this.sCountryCode = sCountryCode;
  }

  /**
   *
   * @return String
   */
  public String getLanguageCode() {
    return this.sLanguageCode;
  }

  /**
   *
   * @param sLanguageCode String
   */
  public void setLanguageCode(String sLanguageCode) {
    this.sLanguageCode = sLanguageCode;
  }

  /**
   *
   * @return String
   */
  public String getCurrency() {
    return this.sCurrency;
  }

  /**
   * put your documentation comment here
   * @param sCurrency
   */
  public void setCurrency(String sCurrency) {
    this.sCurrency = sCurrency;
  }

  /**
   * put your documentation comment here
   * @return
   */
  public Double getTaxRate() {
    return this.dTaxRate;
  }

  /**
   * put your documentation comment here
   * @param dTaxRate
   */
  public void setTaxRate(Double dTaxRate) {
    this.dTaxRate = dTaxRate;
  }

  /**
   public void setTaxRate(double dTaxRate) {this.dTaxRate = new Double(dTaxRate);}
   /**
    *
    * @return String
    */
   public String getBrandID() {
     return this.sBrandID;
   }

  /**
   *
   * @param sBrandID String
   */
  public void setBrandID(String sBrandID) {
    this.sBrandID = sBrandID;
  }

  /**
   *
   * @return String
   */
  public String getFrankingCompanyName() {
    return this.sFrankingCompanyName;
  }

  /**
   *
   * @param sFrankingCompanyName String
   */
  public void setFrankingCompanyName(String sFrankingCompanyName) {
    this.sFrankingCompanyName = sFrankingCompanyName;
  }

  /**
   *
   * @return String
   */
  public String getFrankingCompanyAcNum() {
    return this.sFrankingCompanyAcNum;
  }

  /**
   *
   * @param sFrankingCompanyAcNum String
   */
  public void setFrankingCompanyAcNum(String sFrankingCompanyAcNum) {
    this.sFrankingCompanyAcNum = sFrankingCompanyAcNum;
  }

  /**
   *
   * @return String
   */
  public String getFiscalCode() {
    return this.sFiscalCode;
  }

  /**
   *
   * @param sFiscalCode String
   */
  public void setFiscalCode(String sFiscalCode) {
    this.sFiscalCode = sFiscalCode;
  }

  /**
   *
   * @return String
   */
  public String getShopDescription() {
    return this.sShopDescription;
  }

  /**
   *
   * @param sShopDescription String
   */
  public void setShopDescription(String sShopDescription) {
    this.sShopDescription = sShopDescription;
  }

  /**
   *
   * @return String
   */
  public String getCompanyDescription() {
    return this.sCompanyDescription;
  }

  /**
   *
   * @param sShopDesc String
   */
  public void setCompanyDescription(String sShopDesc) {
    this.sCompanyDescription = sShopDesc;
  }

  /**
   *
   * @return String
   */
  public String getStgErrorMessage() {
    return this.sStgErrorMessage;
  }

  /**
   *
   * @param sStgErrorMessage String
   */
  public void setStgErrorMessage(String sStgErrorMessage) {
    this.sStgErrorMessage = sStgErrorMessage;
  }

  /**
   *
   * @return Long
   */
  public Long getStgEventID() {
    return this.lStgEventId;
  }

  /**
   *
   * @param lStgEventId Long
   */
  public void setStgEventID(Long lStgEventId) {
    this.lStgEventId = lStgEventId;
  }

  /**
   *
   * @return Long
   */
  public Long getStgStatus() {
    return this.lStgStatus;
  }

  /**
   *
   * @param lStgStatus Long
   */
  public void setStgStatus(Long lStgStatus) {
    this.lStgStatus = lStgStatus;
  }

  /**
   *
   * @return Date
   */
  public Date getLoadDate() {
    return this.dtStgLoadDate;
  }

  /**
   *
   * @param dtStgLoadDate Date
   */
  public void setLoadDate(Date dtStgLoadDate) {
    this.dtStgLoadDate = dtStgLoadDate;
  }

  /**
   *
   * @return Date
   */
  public Date getStgProcessDate() {
    return this.dtStgProcessDate;
  }

  /**
   *
   * @param dtStgProcessDate Date
   */
  public void setStgProcessDate(Date dtStgProcessDate) {
    this.dtStgProcessDate = dtStgProcessDate;
  }

  /**
   *
   * @return String
   */
  public String getCompOfficePhoneArea() {
    return this.compOfficePhoneArea;
  }

  /**
   *
   * @param compOfficePhoneArea String
   */
  public void setCompOfficePhoneArea(String compOfficePhoneArea) {
    this.compOfficePhoneArea = compOfficePhoneArea;
  }

  /**
   *
   * @return String
   */
  public String getCompOfficePhoneNumber() {
    return this.compOfficePhoneNumber;
  }

  /**
   *
   * @param compOfficePhoneNumber String
   */
  public void setCompOfficePhoneNumber(String compOfficePhoneNumber) {
    this.compOfficePhoneNumber = compOfficePhoneNumber;
  }

  /**
   *
   * @return String
   */
  public String getCompAddressLine1() {
    return this.compAddressLine1;
  }

  /**
   *
   * @param compAddressLine1 String
   */
  public void setCompAddressLine1(String compAddressLine1) {
    this.compAddressLine1 = compAddressLine1;
  }

  /**
   *
   * @return String
   */
  public String getCompAddressLine2() {
    return this.compAddressLine2;
  }

  /**
   *
   * @param compAddressLine2 String
   */
  public void setCompAddressLine2(String compAddressLine2) {
    this.compAddressLine2 = compAddressLine2;
  }

  /**
   *
   * @return String
   */
  public String getCompCity() {
    return this.compCity;
  }

  /**
   *
   * @param compCity String
   */
  public void setCompCity(String compCity) {
    this.compCity = compCity;
  }

  /**
   *
   * @return String
   */
  public String getCompState() {
    return this.compState;
  }

  /**
   *
   * @param compState String
   */
  public void setCompState(String compState) {
    this.compState = compState;
  }

  /**
   *
   * @return String
   */
  public String getCompPostalCode() {
    return this.compPostalCode;
  }

  /**
   *
   * @param compPostalCode String
   */
  public void setCompPostalCode(String compPostalCode) {
    this.compPostalCode = compPostalCode;
  }

  /**
   *
   * @return String
   */
  public String getCompCountry() {
    return this.compCountry;
  }

  /**
   *
   * @param compCountry String
   */
  public void setCompCountry(String compCountry) {
    this.compCountry = compCountry;
  }

  /**
   *
   * @return String
   */
  public String toString() {
    String sArmaniStgData;
    sArmaniStgData = "------ ArmaniStagingData-------\n" + "RowID =" + sRowID + "\n" + "TranType ="
        + lTranType + "\n" + "CompanyCode =" + sCompanyCode + "\n" + "ShopCode =" + sShopCode
        + "\n" + "PhoneArea =" + sOfficePhoneArea + "\n" + "PhoneNumber =" + sOfficePhoneNumber
        + "\n" + "AddressLine1 =" + sAddressLine1 + "\n" + "AddressLine2 =" + sAddressLine2 + "\n"
        + "City =" + sCity + "\n" + "State =" + sState + "\n" + "PostalCode =" + sPostalCode + "\n"
        + "Country =" + sCountry + "\n" + "Currency =" + sCurrency + "\n" + "TaxRate =" + dTaxRate
        + "\n" + "CountryCode =" + sCountryCode + "\n" + "LanguageCode=" + sLanguageCode + "\n"
        + "BrandId=" + sBrandID + "\n" + "FrankingCompanyName = " + sFrankingCompanyName + "\n"
        + "FiscalCode= " + sFiscalCode + "\n" + "ShopDescription= " + sShopDescription + "\n"
        + "CompanyDescription= " + sCompanyDescription + "\n" + "FrankingCompanyAccountNumber= "
        + sFrankingCompanyAcNum + "\n" + "CompOfficePhoneArea= " + compOfficePhoneArea + "\n"
        + "CompOfficePhoneNumber= " + compOfficePhoneNumber + "\n" + "CompAddressLine1= "
        + compAddressLine1 + "\n" + "CompAddressLine2= " + compAddressLine2 + "\n" + "CompCity= "
        + compCity + "\n" + "CompState= " + compState + "\n" + "CompPostalCode= " + compPostalCode
        + "\n" + "CompCountry= " + compCountry + "\n" + "LoadDate =" + dtStgLoadDate + "\n"
        + "ProcessDate =" + dtStgProcessDate + "\n" + "EventID =" + lStgEventId + "\n" + "Status ="
        + lStgStatus + "\n" + "ErrorMessage =" + sStgErrorMessage + "\n";
    System.out.println(sArmaniStgData);
    return sArmaniStgData;
  }
}
