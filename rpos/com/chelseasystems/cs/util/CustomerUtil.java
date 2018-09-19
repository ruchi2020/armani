/*
 History:
 +------+------------+-----------+-----------+----------------------------------------------------+
 | Ver# | Date       | By        | Defect #  | Description                                        |
 +------+------------+-----------+-----------+----------------------------------------------------+
 | 1    | 07/15/2005 | Vikram    | 328       | Created - to manage birth age-range                |
 --------------------------------------------------------------------------------------------------
 *
 * formatted with JxBeauty (c) johann.langhofer@nextra.at
 */


package com.chelseasystems.cs.util;

import java.util.Date;
import java.util.Calendar;

import com.chelseasystems.cr.appmgr.IRepositoryManager;
import com.chelseasystems.cr.currency.ArmCurrency;
import com.chelseasystems.cr.currency.CurrencyType;
import com.chelseasystems.cs.customer.CMSCustomer;
import com.chelseasystems.cs.customer.CMSCustomerHelper;
import com.chelseasystems.cs.customer.DepositHistory;
import com.chelseasystems.cs.customer.CreditHistory;

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
public class CustomerUtil {
 public static final String AGE_RANGE[] = {"Under 18", "18-24", "25-34", "35-44", "45-54", "55-65"
      , "Above 65", "Not Defined"
  };
	
  public static final int AGE_RANGE_NOT_DEFINED_INDEX = AGE_RANGE.length - 1;
  
  //Added variables AGE_RANGE_DEFAULT & AGE_RANGE_NOT_DEFINED_INDEX_DEFAULT for deault customer.
  public static final String AGE_RANGE_DEFAULT[] = {"Under 18", "18-24", "25-34", "35-44", "45-54", "55-64"
      , "Above 65"
  };

  public static final int AGE_RANGE_NOT_DEFINED_INDEX_DEFAULT = AGE_RANGE_DEFAULT.length - 1;
  
  private static final String AGE_RANGE_LOWER_LIMIT[] = {"0", "18", "25", "35", "45", "55", "65"
  };
  private static final String AGE_ESTIMATE[] = {"12", "21", "29", "39", "49", "59", "69"
  };

  /**
   * put your documentation comment here
   * @param date
   * @return
   * @exception Exception
   */
  public static int getAgeRangeIndex(Date date)
      throws Exception {
    Calendar cal = Calendar.getInstance();
    cal.setTime(date);
    Calendar tempCal = null;
    Calendar today = Calendar.getInstance();
    for (int i = 0; i < AGE_RANGE_LOWER_LIMIT.length && i < AGE_RANGE.length; i++) {
      tempCal = (Calendar)cal.clone();
      tempCal.add(Calendar.YEAR, Integer.parseInt(AGE_RANGE_LOWER_LIMIT[i]));
      if (tempCal.after(today))
        return i - 1;
    }
    return AGE_RANGE_LOWER_LIMIT.length - 1;
  }

  /**
   * put your documentation comment here
   * @param dateString
   * @return
   * @exception Exception
   */
  public static int getAgeRangeIndex(String dateString)
      throws Exception {
    return getAgeRangeIndex(new Date(dateString));
  }

  /**
   * put your documentation comment here
   * @param dateString
   * @return
   */
  public static String getAgeRangeString(String dateString) {
    try {
      return AGE_RANGE[getAgeRangeIndex(new Date(dateString))];
    } catch (Exception ex) {
      return AGE_RANGE[AGE_RANGE_NOT_DEFINED_INDEX];
    }
  }

  /**
   * put your documentation comment here
   * @param index
   * @return
   * @exception Exception
   */
  public static int getAgeEstimateForAgeRangeIndex(int index)
      throws Exception {
    return Integer.parseInt(AGE_ESTIMATE[index]);
  }

  /**
   * put your documentation comment here
   */
  public static String getLowerBirthDate(String anAgeRange) {
    anAgeRange = (anAgeRange == null) ? "" + AGE_RANGE_NOT_DEFINED_INDEX : anAgeRange.trim();
    Calendar date = Calendar.getInstance();
    int range = Integer.parseInt(anAgeRange);
    if ((range < 0) || (range == AGE_RANGE_NOT_DEFINED_INDEX)
        || (range > AGE_RANGE_LOWER_LIMIT.length - 2)) {
      return "";
    } else {
      date.roll(Calendar.YEAR, -1 * Integer.parseInt(AGE_RANGE_LOWER_LIMIT[range + 1]));
    }
    date.roll(Calendar.DATE, -1);
    return "" + (date.get(Calendar.MONTH) + 1) + "/" + date.get(Calendar.DATE) + "/"
        + date.get(Calendar.YEAR);
  }

  /**
   * put your documentation comment here
   */
  public static String getUpperBirthDate(String anAgeRange) {
    anAgeRange = (anAgeRange == null) ? "" + AGE_RANGE_NOT_DEFINED_INDEX : anAgeRange.trim();
    Calendar date = Calendar.getInstance();
    int range = Integer.parseInt(anAgeRange);
    if ((range <= 0) || (range == AGE_RANGE_NOT_DEFINED_INDEX)
        || (range > AGE_RANGE_LOWER_LIMIT.length - 1)) {
      return "";
    } else {
      date.roll(Calendar.YEAR, -1 * Integer.parseInt(AGE_RANGE_LOWER_LIMIT[range]));
    }
    return "" + (date.get(Calendar.MONTH) + 1) + "/" + date.get(Calendar.DATE) + "/"
        + date.get(Calendar.YEAR);
  }
  /**
   * Return Customer Name string using NameSeperator.
   * bLastNameFirst = true  -> LastName + NAME_SEPERATOR + FirstName
   * bLastNameFirst = false -> FirstName + NAME_SEPERATOR + LastName
   * @param cmsCustomer CMSCustomer
   * @param bLastNameFirst boolean
   * @return String
   */
  public static String getCustomerNameString(CMSCustomer cmsCustomer, boolean bLastNameFirst) {
    java.util.ResourceBundle res = com.chelseasystems.cr.util.ResourceManager.
        getResourceBundle();
    String sNameSeperator = res.getString("NAME_SEPERATOR");
    if (sNameSeperator.equals("NAME_SEPERATOR")) sNameSeperator = ",";

    String sTmp = "";
    // Last name has to be shown first.
    if(bLastNameFirst)
    {
      if (cmsCustomer.getLastName() != null &&
          cmsCustomer.getLastName().indexOf("null") == -1) {
        sTmp += cmsCustomer.getLastName();
      }
      if (cmsCustomer.getFirstName() != null &&
          cmsCustomer.getFirstName().indexOf("null"
                                             ) == -1
          &&
          cmsCustomer.getFirstName().trim().length() > 0
          ) {
        if (sTmp.length() > 1)
          sTmp += sNameSeperator;
        sTmp += cmsCustomer.getFirstName();
      }
    }
    // First name is to be shown first.
    else
    {
      if (cmsCustomer.getFirstName() != null &&
          cmsCustomer.getFirstName().indexOf("null") == -1) {
        sTmp += cmsCustomer.getFirstName();
      }
      if (cmsCustomer.getLastName() != null &&
          cmsCustomer.getLastName().indexOf("null"
                                             ) == -1
          &&
          cmsCustomer.getLastName().trim().length() > 0
          ) {
        if (sTmp.length() > 1)
          sTmp += sNameSeperator;
        sTmp += cmsCustomer.getLastName();
      }
    }
    return sTmp;
  }


//merged code with Europe 1.4.4.1
  public static ArmCurrency getDepositHistoryBalance(IRepositoryManager theMgr, String customerId, String storeId) throws Exception {
          DepositHistory[] histories = CMSCustomerHelper.getDepositHistory(theMgr,customerId,storeId);
          ArmCurrency thisCurrency = new ArmCurrency(0.0);
          for(int i=0;i<histories.length;i++){
                  DepositHistory hist = histories[i];
                  if(hist==null) continue;
                  ArmCurrency curr = hist.getamount();
                  if(curr!=null){
                          thisCurrency = thisCurrency.add(curr);
                  }
          }
          return thisCurrency;
      }
  public static ArmCurrency getCreditHistoryBalance(IRepositoryManager theMgr, String customerId
      , String storeId)
      throws Exception {
    CreditHistory[] creditHistory = CMSCustomerHelper.getCreditTenderHistory(theMgr, customerId
        , storeId);
    ArmCurrency totalBalance = new ArmCurrency(0.0);
    for (int i = 0; i < creditHistory.length; i++) {
      totalBalance = totalBalance.add(creditHistory[i].getamount());
    }
    return totalBalance;
  }


  public static Date getEstimatedBirthDate(String anAgeRange) throws Exception {
    anAgeRange = (anAgeRange == null) ? "" + AGE_RANGE_NOT_DEFINED_INDEX : anAgeRange.trim();
    Calendar date = Calendar.getInstance();
    int range = Integer.parseInt(anAgeRange);
    if ((range < 0) || (range == AGE_RANGE_NOT_DEFINED_INDEX)
        || (range > AGE_RANGE_LOWER_LIMIT.length - 2)) {
      return null;
    } else {
      date.roll(Calendar.YEAR, -1 * getAgeEstimateForAgeRangeIndex(range));
    }
    date.roll(Calendar.DATE, -1);
    return date.getTime();
  }
}

