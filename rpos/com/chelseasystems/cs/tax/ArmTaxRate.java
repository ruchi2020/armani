/*
 * @copyright (c) 2001 Chelsea Market Systems LLC
 *
 * formatted with JxBeauty (c) johann.langhofer@nextra.at
 */
/*
 History:
 +------+------------+-----------+-----------+----------------------------------------------+
 | Ver# | Date       | By        | Defect #  | Description                                  |
 +------+------------+-----------+-----------+----------------------------------------------+
 | 2    | 06-21-2005 | Rajesh    | N/A       | Add new field TAX_JUR                        |
 --------------------------------------------------------------------------------------------
 | 1    | 02-16-2005 | Anand     | N/A       | Created to map object to ARM_TAX_RATE object |
 --------------------------------------------------------------------------------------------
 */


package com.chelseasystems.cs.tax;

import java.util.Date;
import com.chelseasystems.cr.business.BusinessObject;


/**
 *
 * <p>Title: ArmTaxRate</p>
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
public class ArmTaxRate extends BusinessObject {
  private String zipCode;
  private String city;
  private String county;
  private String state;
  private Double taxRate;
  private Date effectiveDt;
  private String taxJurisdiction;
  private String nation;

private String taxType;

	//field added for expiration date
	private Date expirationDt;
  
  /**
   * Method is used to get country
   * @return String
   */
  public String getCounty() {
    return this.county;
  }

  /**
   * Method is used to set country
   * @param county String
   */
  public void doSetCounty(String county) {
    this.county = county;
  }

  /**
   * Method is used to get city
   * @return String
   */
  public String getCity() {
    return this.city;
  }

  /**
   * Method is used to set city
   * @param city String
   */
  public void doSetCity(String city) {
    this.city = city;
  }

  /**
   * Method is used to get state
   * @return String
   */
  public String getState() {
    return this.state;
  }

  /**
   * Method is used to set state
   * @param state String
   */
  public void doSetState(String state) {
    this.state = state;
  }

  /**
   * Method is used to get tax rate
   * @return Double
   */
  public Double getTaxRate() {
    return this.taxRate;
  }

  /**
   * Method is used to set tax rate
   * @param taxRate Double
   */
  public void doSetTaxRate(Double taxRate) {
    this.taxRate = taxRate;
  }

  /**
   * Method is used to get effective date
   * @return Date
   */
  public Date getEffectiveDt() {
    return this.effectiveDt;
  }

  /**
   * Method is used to set effective date
   * @param effectiveDt Date
   */
  public void doSetEffectiveDt(Date effectiveDt) {
    this.effectiveDt = effectiveDt;
  }

  /**
   * Method is used to get zip code
   * @return String
   */
  public String getZipCode() {
    return this.zipCode;
  }

  /**
   * Method is used to set zip code
   * @param zipCode String
   */
  public void doSetZipCode(String zipCode) {
    this.zipCode = zipCode;
  }

  /**
   *
   * @param taxJurisdiction String
   */
  public void doSetTaxJurisdiction(String taxJurisdiction) {
    this.taxJurisdiction = taxJurisdiction;
  }

  /**
   *
   * @return String
   */
  public String getTaxJurisdiction() {
    return this.taxJurisdiction;
  }
  
	public String getTaxType() {
		return taxType;
	}
		
	public void setTaxType(String taxType) {
		this.taxType = taxType;
	}

	public void setTaxRate(Double taxRate) {
		this.taxRate = taxRate;
	}
	
	public String getNation() {
		return nation;
	}
	
	public void setNation(String nation) {
		this.nation = nation;
	}

	/**Added for expiration date issue on Nov 15,2016
	 * @return the expirationDt
	 */
	public Date getExpirationDt() {
		return this.expirationDt;
	}

	/**Added for expiration date issue on Nov 15,2016
	 * @param expirationDt the expirationDt to set
	 */
	public void setExpirationDt(Date expirationDt) {
		this.expirationDt = expirationDt;
	}

	//ends here

}

