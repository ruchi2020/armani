/*
 * put your module comment here
 * formatted with JxBeauty (c) johann.langhofer@nextra.at
 */


package com.chelseasystems.cs.employee;

import com.chelseasystems.cr.employee.*;
import java.util.*;
import com.chelseasystems.cr.telephone.*;


/**
 *
 * <p>Title: CMSEmployeeMarshaller</p>
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
public class CMSEmployeeMarshaller extends EmployeeMarshaller {

  /**
   * Default Constructor
   */
  public CMSEmployeeMarshaller() {
  }

  /**
   *
   * @return Employee
   */
  protected Employee createBusinessObjectInstance() {
    return new CMSEmployee();
  }

  /**
   *
   * @param valueObject Map
   * @return Telephone
   */
  protected Telephone toTelephoneBusinessObject(Map valueObject) {
    TelephoneMarshaller telephoneMarshaller = new TelephoneMarshaller();
    return telephoneMarshaller.toBusinessObject(valueObject);
  }

  /**
   *
   * @param telephone Telephone
   * @return Map
   */
  protected Map toTelephoneValueObject(Telephone telephone) {
    TelephoneMarshaller telephoneMarshaller = new TelephoneMarshaller();
    return telephoneMarshaller.toValueObject(telephone);
  }
}

