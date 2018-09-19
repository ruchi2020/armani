/*
 * put your module comment here
 * formatted with JxBeauty (c) johann.langhofer@nextra.at
 */

package com.chelseasystems.cs.customer;

import java.util.Date;

import com.chelseasystems.cr.business.BusinessObject;


/**
 * <p>Title: </p>
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
public class CMSCustomerAlertRule extends BusinessObject {
  private static final long serialVersionUID = 0;
  private String countryCode;
  private String recordType;
  private Date startDate;
  private Date endDate;
  private String productCode;
  private String value;
  private Integer priority;
	// The variable added by Vivek on 24 Nov 08
	private String idBrand;
	private String dsc_level;

  /**
   * put your documentation comment here
   */
  public CMSCustomerAlertRule() {
  }

  /**
   * This method is used to set countryCode of the customer alert rule
   * @param val String
   */
  public void setCountryCode(String val) {
	doSetCountryCode(val);
  }

  /**
   * This method is used to set countryCode of the customer alert rule
   * @param val String
   */
  public void doSetCountryCode(String val) {
    this.countryCode = val;
  }

  /**
   * This method is used to get countryCode of the customer alert rule
   * @return String
   */
  public String getCountryCode() {
    return this.countryCode;
  }

  /**
   * This method is used to set recordType of the customer alert rule
   * @param val String
   */
  public void setRecordType(String val) {
    doSetRecordType(val);
  }

  /**
   * This method is used to set recordType of the customer alert rule
   * @param val String
   */
  public void doSetRecordType(String val) {
    this.recordType = val;
  }

  /**
   * This method is used to get recordType of the customer alert rule
   * @return String
   */
  public String getRecordType() {
    return this.recordType;
  }

  /**
   * This method is used to set startDate of the customer alert rule
   * @param val String
   */
  public void setStartDate(Date val) {
	doSetStartDate(val);
  }

  /**
   * This method is used to set startDate of the customer alert rule
   * @param val String
   */
  public void doSetStartDate(Date val) {
    this.startDate = val;
  }

  /**
   * This method is used to get startDate of the customer alert rule
   * @return String
   */
  public Date getStartDate() {
    return this.startDate;
  }

  /**
   * This method is used to set endDate of the customer alert rule
   * @param val String
   */
  public void setEndDate(Date val) {
	doSetEndDate(val);
  }

  /**
   * This method is used to set endDate of the customer alert rule
   * @param val String
   */
  public void doSetEndDate(Date val) {
    this.endDate = val;
  }

  /**
   * This method is used to get endDate of the customer alert rule
   * @return String
   */
  public Date getEndDate() {
    return this.endDate;
  }

  /**
   * This method is used to set productCode of the customer alert rule
   * @param val String
   */
  public void setProductCode(String val) {
	doSetProductCode(val);
  }

  /**
   * This method is used to set productCode of the customer alert rule
   * @param val String
   */
  public void doSetProductCode(String val) {
    this.productCode = val;
  }

  /**
   * This method is used to get productCode of the customer alert rule
   * @return String
   */
  public String getProductCode() {
    return this.productCode;
  }

  /**
   * This method is used to set value of the customer alert rule
   * @param val String
   */
  public void setValue(String val) {
	doSetValue(val);
  }

  /**
   * This method is used to set value of the customer alert rule
   * @param val String
   */
  public void doSetValue(String val) {
    this.value = val;
  }

  /**
   * This method is used to get value of the customer alert rule
   * @return String
   */
  public String getValue() {
    return this.value;
  }
  
  /**
   * This method is used to set priority of the customer alert rule
   * @param priority
   */
  public void setPriority(Integer priority) {
	this.priority = priority;
  }
  
   /**
    * This method is used to set priority of the customer alert rule
    * @param priority
    */
  public void doSetPriority(Integer priority) {
	this.priority = priority;
  }
  
  /**
   * This method is used to get priority of the customer alert rule
   * @return
   */
  public Integer getPriority() {
	return priority;
  }

	/**
	 * This method is used to get Discount percent level of the customer alert
	 * rule Auther:Vivek Sawant 24 Nov 08
	 * 
	 * @return
	 */
	public String getDsc_level() {
		return dsc_level;
}

	/**
	 * This method is used to set Discount percent level of the customer alert
	 * rule Auther:Vivek Sawant 24 Nov 08
	 * 
	 * @return
	 */
	public void setDsc_level(String dsc_level) {
		this.dsc_level = dsc_level;
	}

	/**
	 * This method is used to get Brand ID of the customer alert rule
	 * Auther:Vivek Sawant 24 Nov 08
	 * 
	 * @return
	 */
	public String getIdBrand() {
		return idBrand;
	}

	/**
	 * This method is used to set Brand ID of the customer alert rule
	 * Auther:Vivek Sawant 24 Nov 08
	 * 
	 * @return
	 */
	public void setIdBrand(String idBrand) {
		this.idBrand = idBrand;
	}

}
