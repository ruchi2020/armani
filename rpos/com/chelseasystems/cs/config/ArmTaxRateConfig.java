package com.chelseasystems.cs.config;


import java.util.Date;

import com.chelseasystems.cr.business.BusinessObject;


public class ArmTaxRateConfig extends BusinessObject{
	private static final long serialVersionUID = 1L;
	
	  private Double idexc;
	  private String state;
	  private Double amountThr;
	  private String zipCode;
	  private String taxJur;
	  private Double taxRate;
	  private String thrRule;
	  private Date effectiveDt;
	  private Double idRate;
	  private String nation;
	  private String brand;
	  private String category;
	  private String product;
	  private Date datains;
	  private Date dataMod;
	  private String taxType;
	  
	  //Poonam: field added for expiry date
	  private Date expirationDt;
	
	  public ArmTaxRateConfig() {
		  idexc = new Double(0.00);
		  state = new String();
		  amountThr = new Double(0.00);
		  zipCode = new String();
		  taxJur = new String();
		  taxRate = new Double(0.00);
		  thrRule = new String();
		  effectiveDt = new Date();
		  idRate = new Double(0.00);
		  nation = new String();
		  brand = new String();
		  category = new String();
		  product = new String();
		  datains = new Date();
		  dataMod = new Date();
		  taxType = new String();
		  
		  //Poonam: Added for expiration date issue on Nov 15,2016
		  expirationDt = new Date();
		  //ends here
	  }
	  
	  public Double getIdexc() {
		return idexc;
	}

	public void setIdexc(Double idexc) {
		this.idexc = idexc;
	}

	public Double getAmountThr() {
		return amountThr;
	}

	public void setAmountThr(Double amountThr) {
		this.amountThr = amountThr;
	}

	public String getThrRule() {
		return thrRule;
	}

	public void setThrRule(String thrRule) {
		this.thrRule = thrRule;
	}

	public Double getIdRate() {
		return idRate;
	}

	public void setIdRate(Double idRate) {
		this.idRate = idRate;
	}

	public String getNation() {
		return nation;
	}

	public void setNation(String nation) {
		this.nation = nation;
	}

	public String getBrand() {
		return brand;
	}

	public void setBrand(String brand) {
		this.brand = brand;
	}

	public String getCategory() {
		return category;
	}

	public void setCategory(String category) {
		this.category = category;
	}

	public String getProduct() {
		return product;
	}

	public void setProduct(String product) {
		this.product = product;
	}

	public Date getDatains() {
		return datains;
	}

	public void setDatains(Date datains) {
		this.datains = datains;
	}

	public Date getDataMod() {
		return dataMod;
	}

	public void setDataMod(Date dataMod) {
		this.dataMod = dataMod;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	/**
	   * put your documentation comment here
	   */  
	  public String getZipCode() {
		return zipCode;
	}

	public void setZipCode(String zipCode) {
		this.zipCode = zipCode;
	}

	

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}

	public Double getTaxRate() {
		return taxRate;
	}

	public void setTaxRate(Double taxRate) {
		this.taxRate = taxRate;
	}

	public Date getEffectiveDt() {
		return effectiveDt;
	}

	public void setEffectiveDt(Date effectiveDt) {
		this.effectiveDt = effectiveDt;
	}

	public String getTaxJur() {
		return taxJur;
	}

	public void setTaxJur(String taxJur) {
		this.taxJur = taxJur;
	}

	
	public String getTaxType() {
		return taxType;
	}

	public void setTaxType(String taxType) {
		this.taxType = taxType;
	}

	/**Added for expiration date issue on Nov 15,2016
	 * @return the expirationDt
	 */
	public Date getExpirationDt() {
		return expirationDt;
	}

	/**Added for expiration date issue on Nov 15,2016
	 * @param expirationDt the expirationDt to set
	 */
	public void setExpirationDt(Date expirationDt) {
		this.expirationDt = expirationDt;
	}
	//ends here
}
