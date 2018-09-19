/*
 * put your module comment here
 * formatted with JxBeauty (c) johann.langhofer@nextra.at
 */


package com.chelseasystems.cs.customer;

import com.chelseasystems.cr.business.BusinessObject;
import com.chelseasystems.cs.dataaccess.artsoracle.databean.PaPrtyOracleBean;
import com.chelseasystems.cs.dataaccess.artsoracle.databean.LoAdsPrtyOracleBean;
import com.chelseasystems.cs.dataaccess.artsoracle.databean.LoAdsNstdOracleBean;
import com.chelseasystems.cs.dataaccess.artsoracle.databean.PaPrsOracleBean;
import com.chelseasystems.cs.dataaccess.artsoracle.databean.ArmAdsPhOracleBean;
import com.chelseasystems.cs.dataaccess.artsoracle.databean.ArmCtDtlOracleBean;
import com.chelseasystems.cs.dataaccess.artsoracle.dao.ArtsConstants;
import java.io.Serializable;
import java.sql.Date;
import java.util.List;
import java.util.StringTokenizer;
import com.chelseasystems.cr.config.ConfigMgr;
import com.chelseasystems.cs.dataaccess.artsoracle.databean.PaCtOracleBean;

/**
 * <p>Title: CustomerSearchString </p>
 * <p>Description: Used for Advanced Search - Customer Lookup</p>
 * <p>Copyright: Copyright (c) 2005</p>
 * <p>Company: SkillNet Inc</p>
 * @author Manpreet S Bawa
 * @version 1.0
 */
/*
 History:
 +------+------------+-----------+-----------+----------------------------------------------------+
 | Ver# | Date       | By        | Defect #  | Description                                        |
 +------+------------+-----------+-----------+----------------------------------------------------+
 | 1    | 06-30-2005 | Manpreet  | N/A       | POS_104665_TS_CustomerLookup_Rev1                  |
 +------+------------+-----------+-----------+----------------------------------------------------+
 | 2    | 11-07-2005 | Manpreet  | N/A       | Modified QueryString to work in all conditions     |
 +------+------------+-----------+-----------+----------------------------------------------------+
 | 3    | 10-06-2011 | Deepika   | PCR       |sAge variable added to support default customer search.
 +------+------------+-----------+-----------+----------------------------------------------------+
 | 4    | 18-11-2011 | Deepika   | Bug#7120  |  Modified the QueryString for DefaultCustomer Issue.      |
 +------+------------+-----------+-----------+----------------------------------------------------+  
 */
public class CustomerSearchString extends BusinessObject implements Serializable {
  public static String CUSTOMER_TYPE_EMPLOYEE_CODE;
  private String sFirstName;
  private String sLastName;
  private String sAddressLine1;
  private String sCity;
  private String sState;
  private String sPostalCode;
  private String sCountry;
  private String sPhoneNumber;
  private String sFiscalCode;
  private String sVATNumber;
  private String sDocumentNumber;
  private String sGender;
  private String sLowerBirthDateRange;
  private String sUpperBirthDateRange;
  private boolean isDefaultCustomer;
  private ConfigMgr config;
  private String strStartRange;
  private String strEndRange;
  private boolean isEmployeeSearch;
  private String sCustomerID;
 //Default customer serach
  private String sAge;


  static {
    ConfigMgr config = new ConfigMgr("customer.cfg");
    CUSTOMER_TYPE_EMPLOYEE_CODE = config.getString("CUSTOMER_TYPE_EMPLOYEE_CODE");
  }

  /**
   * put your documentation comment here
   */
  public CustomerSearchString() {
    sFirstName = new String("");
    sLastName = new String("");
    sAddressLine1 = new String("");
    sCity = new String("");
    sState = new String("");
    sPostalCode = new String("");
    sCountry = new String("");
    sPhoneNumber = new String("");
    sFiscalCode = new String("");
    sVATNumber = new String("");
    sDocumentNumber = new String("");
    sLowerBirthDateRange = new String("");
    sUpperBirthDateRange = new String("");
    isDefaultCustomer = false;
    config = new ConfigMgr("customer.cfg");
    strStartRange = config.getString("DEFAULT_CUST_START_RANGE");
    strEndRange = config.getString("DEFAULT_CUST_END_RANGE");
    isEmployeeSearch = false;
    sCustomerID = new String("");
    sGender = new String("");
	sAge = new String("");
    }

  public void setAge(String sAge) {
	this.sAge = sAge;
}

/**
   * put your documentation comment here
   * @param sValue
   */
  public void setLastName(String sValue) {
    sLastName = sValue;
  }

  /**
   * put your documentation comment here
   * @param sValue
   */
  public void setFirstName(String sValue) {
    sFirstName = sValue;
  }

  /**
   * put your documentation comment here
   * @param sValue
   */
  public void setAddressLine1(String sValue) {
    sAddressLine1 = sValue;
  }

  /**
   * put your documentation comment here
   * @param sValue
   */
  public void setCity(String sValue) {
    sCity = sValue;
  }

  /**
   * put your documentation comment here
   * @param sValue
   */
  public void setState(String sValue) {
    sState = sValue;
  }

  /**
   * put your documentation comment here
   * @param sValue
   */
  public void setZipCode(String sValue) {
    sPostalCode = sValue;
  }

  /**
   * put your documentation comment here
   * @param sValue
   */
  public void setCountry(String sValue) {
    sCountry = sValue;
  }

  /**
   * put your documentation comment here
   * @param sValue
   */
  public void setPhoneNumber(String sValue) {
    sPhoneNumber = sValue;
  }

  /**
   * put your documentation comment here
   * @param sValue
   */
  public void setFiscalReceiptNumber(String sValue) {
    sFiscalCode = sValue;
  }

  /**
   * put your documentation comment here
   * @param sValue
   */
  public void setVATNumber(String sValue) {
    sVATNumber = sValue;
  }

  /**
   * put your documentation comment here
   * @param sValue
   */
  public void setDocumentNumber(String sValue) {
    sDocumentNumber = sValue;
  }

  public void setIsDefaultCustomer(boolean sValue) {
    this.isDefaultCustomer = sValue;
  }

  public void setIsEmployeeSearch(boolean sValue) {
    this.isEmployeeSearch = sValue;
  }

  public void setCustomerID(String sValue) {
    sCustomerID = sValue;
  }
  
  /**
   * put your documentation comment here
   * @param params
   * @return
   */
  public String buildQuery(List params) {
    if(isEmployeeSearch)
      return this.buildEmployeeSearchQuery(params);
    String sQueryString = "";
    String sSelectString ="";
    String sJoinString = "";

    sQueryString = "SELECT UNIQUE "+PaPrtyOracleBean.COL_ID_PRTY+" FROM ";
    sQueryString += PaPrtyOracleBean.TABLE_NAME ;
    //exclude employees
    sSelectString += ", " + PaCtOracleBean.TABLE_NAME;
    sJoinString += PaPrtyOracleBean.COL_ID_PRTY + " = " + PaCtOracleBean.COL_ID_PRTY;
    sJoinString += " AND nvl(" + PaCtOracleBean.COL_TY_CT + ",'0') != ? ";
    params.add(CUSTOMER_TYPE_EMPLOYEE_CODE);
    sJoinString += " AND " + PaPrtyOracleBean.COL_TY_PRTY + " = ? ";
    params.add(ArtsConstants.PARTY_ROLE_TYPE_CUSTOMER);
    if (this.isDefaultCustomer) {
      sJoinString += " AND " + PaPrtyOracleBean.COL_ID_PRTY + " BETWEEN '" + strStartRange + "' AND '" + strEndRange + "' ";
    }
    else {
      sJoinString += " AND " + PaPrtyOracleBean.COL_ID_PRTY + " NOT BETWEEN '" + strStartRange + "' AND '" + strEndRange + "' ";
    }

    // Check if PaPrsOracleBean.TABLE_NAME needs to be added to Select
    if ((sFirstName.length() > 0) || (sLastName.length() > 0) || (sLowerBirthDateRange.length() > 0)
        || (sUpperBirthDateRange.length() > 0) || (sGender.length() > 0)) {
      sSelectString += ", " + PaPrsOracleBean.TABLE_NAME;
      sJoinString += " AND " + PaPrsOracleBean.COL_ID_PRTY_PRS + " = "
          + PaPrtyOracleBean.COL_ID_PRTY;
    }

    // Check if LoAdsNstdOracleBean.TABLE_NAME needs to be added to Select
    if(sAddressLine1.length()>0
       ||
       sCity.length() > 0
       ||
       sState.length() > 0
       ||
       sPostalCode.length() > 0
       ||
       sCountry.length() > 0
       ||
       sPhoneNumber.length() > 0
       )
    {
      sSelectString += ", "+LoAdsPrtyOracleBean.TABLE_NAME+", "+LoAdsNstdOracleBean.TABLE_NAME;
      sJoinString += " AND "+LoAdsPrtyOracleBean.COL_ID_PRTY + " = " + PaPrtyOracleBean.COL_ID_PRTY;
      sJoinString += " AND "+LoAdsNstdOracleBean.COL_ID_ADS + " = " + LoAdsPrtyOracleBean.COL_ID_ADS;

      // Check if ArmAdsPhOracleBean.TABLE_NAME needs to be added to Select
      if(sPhoneNumber.length() >0)
      {
        sSelectString += ", " + ArmAdsPhOracleBean.TABLE_NAME;
        sJoinString += " AND " +ArmAdsPhOracleBean.COL_ID_ADS + " = " + LoAdsPrtyOracleBean.COL_ID_ADS;
      }
    }

   
    // Check if ArmCtDtlOracleBean.TABLE_NAME needs to be added to Select 
		if(sFiscalCode.length()>0
        ||
        sVATNumber.length()>0
        ||
        sDocumentNumber.length()>0)
     {
       sSelectString += ", " +ArmCtDtlOracleBean.TABLE_NAME;
       sJoinString += " AND " +ArmCtDtlOracleBean.COL_ID_CT + " = " + PaPrtyOracleBean.COL_ID_PRTY;
    
     }
       //Started code changes for Bug#7120 DefaultCustomer Issue by Deepika.
       else
       {
    	   sJoinString += " AND " +PaCtOracleBean.COL_ID_CT + " = " + ArmCtDtlOracleBean.COL_ID_CT;
    	    
       }
       //Ended code changes for Bug#7120 DefaultCustomer Issue by Deepika.

       //Added for the defaultCustomersearch to include the age criteria in search query.
   if(sAge.length()>0)
   {
	sSelectString += ", " +ArmCtDtlOracleBean.TABLE_NAME;
   	sJoinString += " AND UPPER(" + ArmCtDtlOracleBean.COL_AGE + ") = ? ";
       params.add(sAge.toUpperCase());
       
       
   }
    sQueryString += sSelectString + " WHERE ";
    sQueryString += sJoinString;

    if (sFirstName.length() > 0) {
      sQueryString += " AND UPPER(" + PaPrsOracleBean.COL_FN_PRS + ") LIKE ?";
      params.add(sFirstName.toUpperCase() + "%");
    }
    if (sLastName.length() > 0) {
      sQueryString += " AND UPPER(" + PaPrsOracleBean.COL_LN_PRS + ") LIKE ?";
      params.add(sLastName.toUpperCase() + "%");
    }

    if (sAddressLine1 != null && sAddressLine1.length() > 0) {
      sQueryString += " AND UPPER(" + LoAdsNstdOracleBean.COL_A1_ADS + ") = ? ";
      params.add(sAddressLine1.toUpperCase());
    }
    if (sCity != null && sCity.length() > 0) {
      sQueryString += " AND UPPER(" + LoAdsNstdOracleBean.COL_NM_UN + ") = ? ";
      params.add(sCity.toUpperCase());
    }
    if (sState != null && sState.length() > 0) {
      sQueryString += " AND UPPER(" + LoAdsNstdOracleBean.COL_TE_NM + ") = ? ";
      params.add(sState.toUpperCase());
    }
    if (sPostalCode != null && sPostalCode.length() > 0) {
      sQueryString += " AND UPPER(" + LoAdsNstdOracleBean.COL_PC_NM + ") like ? ";
      params.add(sPostalCode.toUpperCase()+"%");
    }
    if (sCountry != null && sCountry.length() > 0) {
      sQueryString += " AND UPPER(" + LoAdsNstdOracleBean.COL_CO_NM + ") like ? ";
      params.add(sCountry.toUpperCase());
    }
    if (sPhoneNumber != null && sPhoneNumber.length() > 0) {
      sQueryString += " AND UPPER(" + ArmAdsPhOracleBean.COL_TL_PH + ") = ? ";
      params.add(sPhoneNumber.toUpperCase());
    }

    if (sFiscalCode != null && sFiscalCode.length() > 0) {
      sQueryString += " AND " + ArmCtDtlOracleBean.COL_FISCAL_CODE + " = ? ";
      params.add(sFiscalCode);
    }
    if (sVATNumber != null && sVATNumber.length() > 0) {
      sQueryString += " AND " + ArmCtDtlOracleBean.COL_VAT_NUM + " LIKE ? ";
      params.add(sVATNumber);
    }

    if (sDocumentNumber.length() > 0) {
      sQueryString += " AND " + ArmCtDtlOracleBean.COL_DOC_NUM + " LIKE ? ";
      params.add(sDocumentNumber);
    }

// Commented  the code to discard  the birthdaterange criteria for the bug#7120 Default Customer Issue by Deepika.
 
    /*if (sLowerBirthDateRange.length() > 0) {
      sQueryString += " AND " + PaPrsOracleBean.COL_DC_PRS_BRT + " >= ? ";
      params.add(getDateFromString(sLowerBirthDateRange));
    }

    if (sUpperBirthDateRange.length() > 0) {
      sQueryString += " AND " + PaPrsOracleBean.COL_DC_PRS_BRT + " <= ? ";
      params.add(getDateFromString(sUpperBirthDateRange));
    }*/

    if (sGender.length() > 0) {
      sQueryString += " AND " + PaPrsOracleBean.COL_TY_GND_PRS + " = ? ";
      params.add(sGender);
    }
    
    

    return sQueryString;
  }


  protected String buildEmployeeSearchQuery(List params) {
    String sQueryString = "";
    String sSelectString = "";
    String sJoinString = "";

    sQueryString = "SELECT UNIQUE " + PaCtOracleBean.COL_ID_PRTY + " FROM ";
    sQueryString += PaCtOracleBean.TABLE_NAME;

    sJoinString += PaCtOracleBean.COL_TY_CT + " = ? ";
    params.add(CUSTOMER_TYPE_EMPLOYEE_CODE);

    // Check if PaPrsOracleBean.TABLE_NAME needs to be added to Select
    if (sLastName.length() > 0) {
      sSelectString += ", " + PaPrsOracleBean.TABLE_NAME;
      sJoinString += " AND " + PaPrsOracleBean.COL_ID_PRTY_PRS + " = "
          + PaCtOracleBean.COL_ID_PRTY;
    }

    sQueryString += sSelectString + " WHERE ";
    sQueryString += sJoinString;

    if (sLastName.length() > 0) {
//      sQueryString += " AND UPPER(" + PaPrsOracleBean.COL_LN_PRS + ") LIKE ?";
//      params.add(sLastName.toUpperCase() + "%");
      sQueryString += " AND UPPER(" + PaPrsOracleBean.COL_LN_PRS + ") = ?";
      params.add(sLastName.toUpperCase());
    }

    if (sCustomerID.length() > 0) {
      sQueryString += " AND UPPER(" + PaCtOracleBean.COL_ID_CT + ") = ?";
      params.add(sCustomerID.toUpperCase());
    }

    return sQueryString;
  }


  public void setLowerBirthDateRange(String lowerBirthDateRange) {
    sLowerBirthDateRange = lowerBirthDateRange;
  }

  public void setUpperBirthDateRange(String upperBirthDateRange) {
    sUpperBirthDateRange = upperBirthDateRange;
  }

  private java.sql.Date getDateFromString(String aDate) {
    if (aDate == null)
      return null;
    StringTokenizer st = new StringTokenizer(aDate, "/-\\");
    int day, month, year;
    try {
      day = Integer.parseInt(st.nextToken());
      month = Integer.parseInt(st.nextToken());
      year = Integer.parseInt(st.nextToken());
      // Date date = new Date(year-1900,month-1,day);

      Date date = Date.valueOf(year + "-" + month + "-" + day);

      return date;
    } catch (Exception e) {
      System.out.println("invalid date format");
      return null;
    }
  }

  public void setGender(String gender) {
    if (gender == null) {
      gender = "";
    }
    sGender = gender;
  }
}

