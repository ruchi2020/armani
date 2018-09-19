package com.chelseasystems.cs.config;

import com.chelseasystems.cr.business.BusinessObject;

public class ArmTaxRateConfigDetail extends BusinessObject{

	private String sState; 
	private String sThresholdamt;
	private String sZipcode;
	private String sTaxjur; 
	private String sTaxrate;
	private String sCategory;
	private String sThresholdrule;
	private String sProduct;
	private String sBrand;

	  /**
	   * put your documentation comment here
	   */
	/*sState = new String(); 
	sThresholdamt = new String();
	sZipcode = new String();
	sTaxjur = new String(); 
	sTaxrate = new String();
	sItemclass = new String();
	sThresholdrule = new String();*/
	  

	  /**
	   * put your documentation comment here
	   * @param sState
	   */
	  public void setState(String sState) {
	    if (sState == null) {
	      return;
	    }
	    doSetState(sState);
	  }

	  /**
	   * put your documentation comment here
	   * @param sState
	   */
	  public void doSetState(String sState) {
	    this.sState = sState;
	  }

	  /**
	   * put your documentation comment here
	   * @return
	   */
	  public String getState() {
	    return sState;
	  }

	  /**
	   * put your documentation comment here
	   * @param sThresholdamt
	   */
	  public void setThresholdamt(String sThresholdamt) {
	    if (sThresholdamt == null) {
	      return;
	    }
	    doSetThresholdamt(sThresholdamt);
	  }

	  /**
	   * put your documentation comment here
	   * @param sThresholdamt
	   */
	  public void doSetThresholdamt(String sThresholdamt) {
	    this.sThresholdamt = sThresholdamt;
	  }

	  /**
	   * put your documentation comment here
	   * @return
	   */
	  public String getThresholdamt() {
	    return sThresholdamt;
	  }
	  
	  /**
	   * put your documentation comment here
	   * @param sZipcode
	   */
	  public void setZipcode(String sZipcode) {
	    if (sZipcode == null) {
	      return;
	    }
	    doSetZipcode(sZipcode);
	  }

	  /**
	   * put your documentation comment here
	   * @param sZipcode
	   */
	  public void doSetZipcode(String sZipcode) {
	    this.sZipcode = sZipcode;
	  }

	  /**
	   * put your documentation comment here
	   * @return
	   */
	  public String getZipcode() {
	    return sZipcode;
	  }
	  
	  /**
	   * put your documentation comment here
	   * @param sTaxjur
	   */
	  public void setTaxjur(String sTaxjur) {
	    if (sTaxjur == null) {
	      return;
	    }
	    doSetZipcode(sTaxjur);
	  }

	  /**
	   * put your documentation comment here
	   * @param sTaxjur
	   */
	  public void doSetTaxjur(String sTaxjur) {
	    this.sTaxjur = sTaxjur;
	  }

	  /**
	   * put your documentation comment here
	   * @return
	   */
	  public String getTaxjur() {
	    return sTaxjur;
	  }
	  
	  /**
	   * put your documentation comment here
	   * @param sTaxrate
	   */
	  public void setTaxrate(String sTaxrate) {
	    if (sTaxrate == null) {
	      return;
	    }
	    doSetZipcode(sTaxrate);
	  }

	  /**
	   * put your documentation comment here
	   * @param sTaxrate
	   */
	  public void doSetTaxrate(String sTaxrate) {
	    this.sTaxrate = sTaxrate;
	  }

	  /**
	   * put your documentation comment here
	   * @return
	   */
	  public String getTaxrate() {
	    return sTaxrate;
	  }
	  
	  /**
	   * put your documentation comment here
	   * @param sCatagory
	   */
	  public void setCategory(String sCategory) {
	    if (sCategory == null) {
	      return;
	    }
	    doSetCategory(sCategory);
	  }

	  /**
	   * put your documentation comment here
	   * @param sCatagory
	   */
	  public void doSetCategory(String sCategory) {
	    this.sCategory = sCategory;
	  }

	  /**
	   * put your documentation comment here
	   * @return
	   */
	  public String getCategory() {
	    return sCategory;
	  }
	  
	  /**
	   * put your documentation comment here
	   * @param sThresholdrule
	   */
	  public void setThresholdrule(String sThresholdrule) {
	    if (sThresholdrule == null) {
	      return;
	    }
	    doSetThresholdrule(sThresholdrule);
	  }

	  /**
	   * put your documentation comment here
	   * @param sThresholdrule
	   */
	  public void doSetThresholdrule(String sThresholdrule) {
	    this.sThresholdrule = sThresholdrule;
	  }

	  /**
	   * put your documentation comment here
	   * @return
	   */
	  public String getThresholdrule() {
	    return sThresholdrule;
	  }
	  
	  /**
	   * put your documentation comment here
	   * @param sProduct
	   */
	  public void setProduct(String sProduct) {
	    if (sProduct == null) {
	      return;
	    }
	    doSetProduct(sProduct);
	  }

	  /**
	   * put your documentation comment here
	   * @param sProduct
	   */
	  public void doSetProduct(String sProduct) {
	    this.sProduct = sProduct;
	  }

	  /**
	   * put your documentation comment here
	   * @return
	   */
	  public String getProduct() {
	    return sProduct;
	  }
	  
	  /**
	   * put your documentation comment here
	   * @param sBrand
	   */
	  public void setBrand(String sBrand) {
	    if (sBrand == null) {
	      return;
	    }
	    doSetBrand(sBrand);
	  }

	  /**
	   * put your documentation comment here
	   * @param sBrand
	   */
	  public void doSetBrand(String sBrand) {
	    this.sBrand = sBrand;
	  }

	  /**
	   * put your documentation comment here
	   * @return
	   */
	  public String getBrand() {
	    return sBrand;
	  }
}
