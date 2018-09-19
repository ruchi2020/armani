/*
 History:
 +------+------------+-----------+-----------+----------------------------------------------------+
 | Ver# | Date       | By        | Defect #  | Description                                        |
 +------+------------+-----------+-----------+----------------------------------------------------+
 | 7    | 08/08/2006 | Sandhya   | 1637      | A search by model/fabric is bringing back no result|
 +------+------------+-----------+-----------+----------------------------------------------------+
 | 6    | 04/04/2006 | Sandhya   | PCR1256   | Style locator enhancement                          |
 |      |            |           | PCR1167   | Sku lookup and visibility                          |
 +------+------------+-----------+-----------+----------------------------------------------------+
 | 5    | 07-27-2005 | Vikram    | 739       |Query changed to use Color ID instead of Description|
 +------+------------+-----------+-----------+----------------------------------------------------+
 | 4    | 07-11-2005 | Vikram    | 514       |Changes in Item Lookup advanced query for "Locate"  |
 +------+------------+-----------+-----------+----------------------------------------------------+
 | 3    | 04-29-2005 | Vikram    | N/A       |added Desc attributes for clr, season & supplier|
 +------+------------+-----------+-----------+----------------------------------------------------+
 | 2    | 04-21-2005 | Rajesh    | N/A       |modify to implement Serializable                    |
 +------+------------+-----------+-----------+----------------------------------------------------+
 *
 * formatted with JxBeauty (c) johann.langhofer@nextra.at
 */


package com.chelseasystems.cs.item;

import com.chelseasystems.cr.config.ConfigMgr;
import com.chelseasystems.cr.currency.ArmCurrency;
import com.chelseasystems.cs.dataaccess.artsoracle.databean.RkItemVwOracleBean;
import com.chelseasystems.cs.util.Version;

import java.io.Serializable;


/**
 *
 * <p>Title: ItemSearchString</p>
 *
 * <p>Description: This class define the search parameter for an item</p>
 *
 * <p>Copyright: Copyright (c) 2005</p>
 *
 * <p>Company: </p>
 *
 * @author Rajesh Pradhan
 * @version 1.0
 */
public class ItemSearchString implements Serializable {
   ConfigMgr configMgr = new ConfigMgr("item.cfg");
   String sku, style, brand, model, fabric, color, supplier, season, year, size, desc, price, store
      , colorDesc, seasonDesc, supplierName, productNum;
   ArmCurrency baseCurrency;
  
   int maxRecords = (configMgr.getInt("MAX_RECORDS_TO_RETRIEVE")<=0)?100:configMgr.getInt("MAX_RECORDS_TO_RETRIEVE");  
   boolean isAdvancedSearch;
   private boolean bPerformSearch = false;
   private boolean bItemSearchInMultipleStore = true;
   
  /**
   * Default Constructor
   */
  public ItemSearchString() {
	  setItemSearchInMultipleStore();
	  baseCurrency = new ArmCurrency(0.0d);
  }

  /**
   * This method is used to set sku of an item
   * @param val String
   */
  public void setSKU(String val) {
    this.sku = val;
  }

  /**
   * This method is used to get sku of an item
   * @return String
   */
  public String getSKU() {
    return this.sku;
  }

  /**
   * This method is used to set style of an item
   * @param val String
   */
  public void setStyle(String val) {
    this.style = val;
  }

  /**
   * This method is used to get style of an item
   * @return String
   */
  public String getStyle() {
    return this.style;
  }

  /**
   * This method is used to set brand of an item
   * @param val String
   */
  public void setBrand(String val) {
    this.brand = val;
    ;
  }

  /**
   * This method is used to get brand of an item
   * @return String
   */
  public String getBrand() {
    return this.brand;
  }

  /**
   * This method is used to set fabric of an item
   * @param val String
   */
  public void setModel(String val) {
    this.model = val;
    ;
  }

  /**
   * This method is used to get model of an item
   * @return String
   */
  public String getModel() {
    return this.model;
  }

  /**
   * This method is used to set fabric of an item
   * @param val String
   */
  public void setFabric(String val) {
    this.fabric = val;
  }

  /**
   * This method is used to get fabric of an item
   * @return String
   */
  public String getFabric() {
    return this.fabric;
  }

  /**
   * This method is used to set color of an item
   * @param val String
   */
  public void setColor(String val) {
    this.color = val;
    ;
  }

  /**
   * This method is used to get color of an item
   * @return String
   */
  public String getColor() {
    return this.color;
  }

  /**
   * This method is used to set supplier of an item
   * @param val String
   */
  public void setSupplier(String val) {
    this.supplier = val;
  }

  /**
   * This method is used to get supplier of an item
   * @return String
   */
  public String getSupplier() {
    return this.supplier;
  }

  /**
   * This method is used to set season of item
   * @param val String
   */
  public void setSeason(String val) {
    this.season = val;
  }

  /**
   *
   * @return String
   */
  public String getSeason() {
    return this.season;
  }

  /**
   * This method is used to set year
   * @param val String
   */
  public void setYear(String val) {
    this.year = val;
  }

  /**
   * This method is used to get year
   * @return String
   */
  public String getYear() {
    return this.year;
  }

  /**
   * This method is used to set size of an item
   * @param val String
   */
  public void setSize(String val) {
    this.size = val;
  }

  /**
   * This method is used to get size of an item
   * @return String
   */
  public String getSize() {
    return this.size;
  }

  /**
   * This method is used to set store
   * @param val String
   */
  public void setStore(String val) {
    this.store = val;
  }

  /**
   * This method is used to get store
   * @return String
   */
  public String getStore() {
    return this.store;
  }

  /**
   * This method is used to set item color description
   * @param colorDesc String
   */
  public void setColorDesc(String colorDesc) {
    this.colorDesc = colorDesc;
  }

  /**
   * This method is used to get item color description
   * @return String
   */
  public String getColorDesc() {
    return this.colorDesc;
  }

  /**
   * This method is used to set item's season description
   * @param seasonDesc String
   */
  public void setSeasonDesc(String seasonDesc) {
    this.seasonDesc = seasonDesc;
  }

  /**
   * This method is used to get item's season description
   * @return String
   */
  public String getSeasonDesc() {
    return this.seasonDesc;
  }

  /**
   * This method is used to set item's supplier name
   * @param supplierName String
   */
  public void setSupplierName(String supplierName) {
    this.supplierName = supplierName;
  }

  /**
   * This method is used to get item's supplier name
   * @return String
   */
  public String getSupplierName() {
    return this.supplierName;
  }

  /**
   * This method is used to set item's Product Number
   * @param productNum String
   */
  public void setProductNum(String productNum) {
    this.productNum = productNum;
  }

  /**
   * This method is used to get item's Product Number
   * @return String
   */
  public String getProductNum() {
    return this.productNum;
  }
  
  /**
   * This method is used to set for advance search
   * @param val boolean
   */
  public void setIsAdvancedSearch(boolean val) {
    this.isAdvancedSearch = val;
  }

  /**
   * This method is used to check for advance search
   * @return boolean
   */
  public boolean isAdvancedSearch() {
    return this.isAdvancedSearch;
  }
  public void setSearchRequired(boolean bSearch) {
    bPerformSearch = bSearch;
  }

  public boolean isSearchRequired() {
    return bPerformSearch;
  }

  public String toString(){
	  StringBuffer buf = new StringBuffer();
	  buf.append(" SKU = ").append(this.getSKU())
	  	.append(" | Style = ").append(this.getStyle())
	  	.append(" | Supplier = ").append(this.getSupplier())
	  	.append(" | Fabric = ").append(this.getFabric())
	  	.append(" | Model = ").append(this.getModel())
	  	.append(" | Color = ").append(this.getColor())
	  	.append(" | Year = ").append(this.getYear())
	  	.append(" | Season = ").append(this.getSeason())
	    .append(" | Store = ").append(this.getStore())
	    .append(" | Brand = ").append(this.getBrand());

	  return buf.toString();
  }
  /**
   * This method is used to build item search query
   * @return String
   */
  public String buildQuery() {
	  // Adding order by clauses for pcr 1859
    String query = "";
    String storeCondition = getStoreCondition();
    if (this.isAdvancedSearch) {
    	  /*
       query = " where "
       + "lower (" + RkItemVwOracleBean.COL_STYLE_NUM + ") = '"+ getStyle().trim().toLowerCase() + "' and "
       + "lower (" +  RkItemVwOracleBean.COL_MODEL + ") = '"+ getModel().trim().toLowerCase() + "' and "
       + RkItemVwOracleBean.COL_ID_SPR + " = '"+ getSupplier() + "' and "
       + "lower (" + RkItemVwOracleBean.COL_FABRIC + ") = '"+ getFabric().trim().toLowerCase() + "' and "
       + RkItemVwOracleBean.COL_SEASON + " = '"+ getSeason() + "' and "
       + RkItemVwOracleBean.COL_FY + " = '"+ getYear() + "'";
       */
      query = " where " + "lower (" + RkItemVwOracleBean.COL_STYLE_NUM
          + (getStyle() == null ? ") is null" : (") = '" + getStyle().trim().toLowerCase() + "'"))
          + " and lower (" + RkItemVwOracleBean.COL_MODEL
          + (getModel() == null ? ") is null" : (") = '" + getModel().trim().toLowerCase() + "'"))
          + " and " + RkItemVwOracleBean.COL_ID_SPR
          + (getSupplier() == null ? " is null" : (" = '" + getSupplier().trim() + "'"))
          + " and lower (" + RkItemVwOracleBean.COL_FABRIC
          + (getFabric() == null ? ") is null" : (") = '" + getFabric().trim().toLowerCase() + "'"))
          + " and " + RkItemVwOracleBean.COL_SEASON
          + (getSeason() == null ? " is null" : (" = '" + getSeason().trim() + "'")) + " and "
          + RkItemVwOracleBean.COL_FY
          + (getYear() == null ? " is null" : (" = '" + getYear().trim() + "'"));
      
          if( !isItemSearchInMultipleStore() ) {
        		System.out.println("buildQuery: GOING TO ADD COLOR & STORE CONDITION");
        	  query = query + " and ( " + RkItemVwOracleBean.COL_ID_COLOR 
              + (getColor() == null ? " is null" : (" = '" + getColor().trim().toLowerCase() + "'"))
              + ( (!storeCondition.equals("")) ? " or " + storeCondition : "" )
              + " )";
          }
          //+ ((getStore() == null || getStore().trim().length() <= 0) ? ""
          //: (" or " + RkItemVwOracleBean.COL_ID_STR_RT + " = '" + getStore().trim() + "'")) + " )";
    } else {
    	query = "select * from " + RkItemVwOracleBean.TABLE_NAME;
      if (getSKU() != null && getSKU().length() > 0) {
     // Changes for issue 1858
	  String retrieveRelatedItems = configMgr.getString("ITEM_SEARCH_RETRIEVE_RELATED_ITEMS");
	  if (retrieveRelatedItems != null && retrieveRelatedItems.equalsIgnoreCase("true")) {
		  query = "select distinct b.* from " + RkItemVwOracleBean.TABLE_NAME + " a, " +
		  RkItemVwOracleBean.TABLE_NAME + " b" + " where (a." + RkItemVwOracleBean.COL_ID_ITM_ +
		  " like '" + getSKU().trim() + "%'" + " or " + "a." + RkItemVwOracleBean.COL_BARCODE_ + " like '" + 
		  getSKU().trim() + "%')" + ( (!storeCondition.equals("")) ? " and " + storeCondition : "" )
          + " and a." + RkItemVwOracleBean.COL_CURRENCY_CODE_ + " = '" 
      	  + this.baseCurrency.getCurrencyType().getCode() + "'" + " and a." + RkItemVwOracleBean.COL_CURRENCY_CODE_ +
      	  " = b." + RkItemVwOracleBean.COL_CURRENCY_CODE_ + " and a." + RkItemVwOracleBean.COL_MODEL_ +
      	  " = b." + RkItemVwOracleBean.COL_MODEL_ + " and a." + RkItemVwOracleBean.COL_FABRIC_ + " = b." +
      	  RkItemVwOracleBean.COL_FABRIC_;
		  query = query + " and rownum <= " +  maxRecords;
		  query = query + " union";
		  query = query + " select * from " + RkItemVwOracleBean.TABLE_NAME;
        query = query + " where (" + RkItemVwOracleBean.COL_ID_ITM + " like '" + getSKU().trim()+"%'"
            + " or " + RkItemVwOracleBean.COL_BARCODE + " like '" + getSKU().trim() + "%')"
            + ( (!storeCondition.equals("")) ? " and " + storeCondition : "" )
            + " and " + RkItemVwOracleBean.COL_CURRENCY_CODE + " = '" 
        	+ this.baseCurrency.getCurrencyType().getCode() + "'";
        query = query + " and rownum <= " +  maxRecords;
	  } else {
        query = query + " where (" + RkItemVwOracleBean.COL_ID_ITM + " like '" + getSKU().trim()+"%'"
            + " or " + RkItemVwOracleBean.COL_BARCODE + " like '" + getSKU().trim() + "%')"
            + ( (!storeCondition.equals("")) ? " and " + storeCondition : "" )
            + " and " + RkItemVwOracleBean.COL_CURRENCY_CODE + " = '" 
        	+ this.baseCurrency.getCurrencyType().getCode() + "'";
        query = query + " and rownum <= " +  maxRecords;
	  }
        return query;
      }
      if (getStyle() != null && getStyle().length() > 0) {
        query = query + " where " + RkItemVwOracleBean.COL_STYLE_NUM + " = '" + getStyle().trim() +"'"
            + ( (!storeCondition.equals("")) ? " and " + storeCondition : "" )
            + " and " + RkItemVwOracleBean.COL_CURRENCY_CODE + " = '" 
            + this.baseCurrency.getCurrencyType().getCode() + "'"
            + " and rownum <= " +  maxRecords;
        return query;
      }
      if (getModel() != null && getModel().length() > 0) {
        query = query + " where lower(" + RkItemVwOracleBean.COL_MODEL + ") = '" + getModel().trim().toLowerCase() + "' ";
        //EUROPE
        if (getProductNum() != null && getProductNum().length() > 0) {
            query = query + " and lower(" + RkItemVwOracleBean.COL_PRODUCT_NUM + ") = '" + getProductNum().trim().toLowerCase() + "' ";
        }
		if (getFabric() != null && getFabric().length() > 0) {
			query = query + " and lower(" + RkItemVwOracleBean.COL_FABRIC + ") = '" + getFabric().trim().toLowerCase() + "' ";
		}
		if (getColor() != null && getColor().length() > 0) {
			query = query + " and lower(" + RkItemVwOracleBean.COL_ID_COLOR + ") = '" + getColor().trim().toLowerCase() + "' ";
		}
		if (getSupplier() != null && getSupplier().length() > 0) {
			query = query + " and " + RkItemVwOracleBean.COL_ID_SPR + " = '" + getSupplier() + "' ";
		}
		if (getSeason() != null && getSeason().length() > 0) {
			query = query + " and " + RkItemVwOracleBean.COL_SEASON + " = '" + getSeason() + "' ";
		}
		if (getYear() != null && getYear().length() > 0) {
			query = query + " and " + RkItemVwOracleBean.COL_FY + " = '" + getYear() + "' ";
		}
        if(!storeCondition.equals("")) {
        	query = query + " and " + storeCondition;
        }
        query = query + " and rownum <= " +  maxRecords;
      }
    }
    query += " and " + RkItemVwOracleBean.COL_CURRENCY_CODE + " = '" 
    	+ this.baseCurrency.getCurrencyType().getCode() + "'";
    return query;
  }
  

  	private String getStoreCondition() {
  		String sCondition="";
  		if(! isItemSearchInMultipleStore()) {
            if( getStore() != null && getStore().trim().length() > 0 ) {
            	sCondition = RkItemVwOracleBean.COL_ID_STR_RT + " = '" + getStore().trim() + "'";
            }
  		}
  		//System.out.println("StoreCondition: >" + sCondition +"<");
  		return sCondition;
  	}
  	
  	private boolean isItemSearchInMultipleStore() {
  		return bItemSearchInMultipleStore;
  	}

	private void setItemSearchInMultipleStore() {
  		String itemSearchStatus = configMgr.getString("ITEM_SEARCH_IN_MULTIPLE_STORE");
  		if( itemSearchStatus != null && itemSearchStatus.trim().equalsIgnoreCase("true") ){
  			bItemSearchInMultipleStore = true;
  		}else{
  			bItemSearchInMultipleStore = false;
  		}
	}
}

