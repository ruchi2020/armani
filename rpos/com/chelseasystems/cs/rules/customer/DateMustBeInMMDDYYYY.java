/*
 * put your module comment here
 * formatted with JxBeauty (c) johann.langhofer@nextra.at
 */


package com.chelseasystems.cs.rules.customer;

import java.util.*;
import java.lang.*;
import java.text.SimpleDateFormat;
import java.text.ParseException;
import com.chelseasystems.cr.rules.*;
import com.chelseasystems.cr.logging.*;
import com.chelseasystems.cs.customer.*;
import com.chelseasystems.cr.util.ResourceManager;


/**
 * <p>Title: DateMustBeInMMDDYYYY</p>
 * <p>Description: This business rule checks date format, it should be MMDDYYY.
 * If some different format then throw the 'Invalid Date Format' business rule</p>
 * <p>Copyright: Copyright (c) 2005</p>
 * <p>Company:SkillNet Inc. </p>
 * @author
 * @version 1.0
 */
public class DateMustBeInMMDDYYYY extends Rule {

  /**
   * Default Constructor
   */
  public DateMustBeInMMDDYYYY() {
  }

  /**
   * Execute the Rule
   * @param theParent - instance of CMSCustomer
   * @param args - instance of String
   */
  public RulesInfo execute(Object theParent, Object args[]) {
    return (execute((CMSCustomer)theParent, (String)args[0]));
  }

  /**
   * Execute the Rule
   * @param theParent - instance of CMSCustomer
   * @param string - instance of String
   */
  private RulesInfo execute(CMSCustomer cmscustomer, String string) {
    if (string != null && string.length() > 0) {
      ResourceBundle res = ResourceManager.getResourceBundle();
      try {
        if (string.length() == 0)
          return new RulesInfo("This field is required.");
        SimpleDateFormat df = com.chelseasystems.cs.util.DateFormatUtil.getLocalDateFormat();
        df.setLenient(false);
        // try
        //{
        df.parse(string);
        //}
        //catch(ParseException pe)
        //{
        // df.applyPattern(res.getString("MM dd"));
        // df.parse(string);
        // }
      } catch (ParseException pe) {
        return (new RulesInfo("Invalid Date Format."));
      } catch (Exception ex) {
        LoggingServices.getCurrent().logMsg(getClass().getName(), "execute"
            , "Rule Failed, see exception.", "N/A", LoggingServices.MAJOR, ex);
      }
    }
    return (new RulesInfo());
  }

  /**
   * Returns the name of the Rule.
   * @return the name of the rule
   */
  public String getName() {
    return ("DateMustBeInMM/DD/YYYY");
  }

  /**
   * Returns the description of the business rule.
   * @returns description of the business rule.
   */
  public String getDesc() {
    StringBuffer buf = new StringBuffer();
    buf.append("Date must by in  MM/DD/YYYY format.");
    return (buf.toString());
  }
}

