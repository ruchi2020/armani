/*
 * put your module comment here
 * formatted with JxBeauty (c) johann.langhofer@nextra.at
 */


package com.chelseasystems.cs.tax;

import java.util.HashMap;
import java.util.Map;

import com.chelseasystems.cr.tax.SaleTaxDetail;
import com.chelseasystems.cr.currency.ArmCurrency;
import com.chelseasystems.cs.config.ArmTaxRateConfig;


/**
 * <p>Title: CMSSaleTaxDetail</p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2004</p>
 * <p>Company: </p>
 * @author unascribed
 * @version 1.0
 */
public class CMSSaleTaxDetail extends SaleTaxDetail {
  private String state = null;
  private String taxJurisdiction = null;
  private ArmCurrency stateTax = null;
  private Map taxMap;
  private Map taxExcMap;
  private ArmTaxRateConfig[]  armCATaxConfig ;
  private HashMap<String, Object[]> taxDetailMap;

 

/**
   * Constructor
   * @param taxType String
   * @param taxAmount Currency
   * @param taxRate double
   * @param state String
   */
  public CMSSaleTaxDetail(String taxType, ArmCurrency taxAmount, double taxRate, String state
      , String taxJurisdiction) {
    super(taxType, taxAmount, taxRate);
    this.state = state;
    this.taxJurisdiction = taxJurisdiction;
  }
  
  public CMSSaleTaxDetail(String taxType, ArmCurrency taxAmount, double taxRate, String state
	      , String taxJurisdiction, ArmCurrency stateTax, Map taxMap , Map taxExcMap , ArmTaxRateConfig[] armCATaxConfig) {
	    super(taxType, taxAmount, taxRate);
	    this.state = state;
	    this.taxJurisdiction = taxJurisdiction;
	    this.stateTax = stateTax;
	    this.taxMap = taxMap;
	    this.taxExcMap = taxExcMap;
	    this.armCATaxConfig = armCATaxConfig;

	  }
  
  public CMSSaleTaxDetail(String taxType, ArmCurrency taxAmount, double taxRate, String state
	      , String taxJurisdiction, ArmCurrency stateTax, Map taxMap , Map taxExcMap , ArmTaxRateConfig[] armCATaxConfig, HashMap<String, Object[]> taxDetailMap) {
	    super(taxType, taxAmount, taxRate);
	    this.state = state;
	    this.taxJurisdiction = taxJurisdiction;
	    this.stateTax = stateTax;
	    this.taxMap = taxMap;
	    this.taxExcMap = taxExcMap;
	    this.armCATaxConfig = armCATaxConfig;
	    this.taxDetailMap = taxDetailMap;
	  
	  }

  /**
   * Method used to get state
   * @return String
   */
  public String getState() {
    return this.state;
  }

  /**
   * Method used to set state
   * @param state String
   */
  public void setState(String state) {
    this.state = state;
  }

  /**
   * Method used to get taxJurisdiction
   * @return String
   */
  public String getTaxJurisdiction() {
    return this.taxJurisdiction;
  }

  /**
   * Method used to set state
   * @param state String
   */
  public void setTaxJurisdiction(String taxJurisdiction) {
    this.taxJurisdiction = taxJurisdiction;
  }


public HashMap<String, Object[]> getTaxDetailMap() {
	return taxDetailMap;
}

public void setTaxDetailMap(HashMap<String, Object[]> taxDetailMap) {
	this.taxDetailMap = taxDetailMap;
}
	
  public ArmCurrency getStateTax() {
		return stateTax;
	}

	public void setStateTax(ArmCurrency stateTax) {
		this.stateTax = stateTax;
	}
	
	
	  public Map getTaxMap() {
			return taxMap;
		}

		public void setTaxMap(Map taxMap) {
			this.taxMap = taxMap;
		}
		
		  public Map getTaxExcMap() {
				return taxExcMap;
			}

			public void setTaxExcMap(Map taxExcMap) {
				this.taxExcMap = taxExcMap;
			}

			public ArmTaxRateConfig[] getArmCATaxConfig() {
				return armCATaxConfig;
			}

			public void setArmCATaxConfig(ArmTaxRateConfig[] armCATaxConfig) {
				this.armCATaxConfig = armCATaxConfig;
			}


}

