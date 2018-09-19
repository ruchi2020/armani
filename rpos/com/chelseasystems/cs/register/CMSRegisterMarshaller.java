/*
 * put your module comment here
 * formatted with JxBeauty (c) johann.langhofer@nextra.at
 */


package com.chelseasystems.cs.register;

import java.util.*;
import com.chelseasystems.cr.register.Register;
import com.chelseasystems.cr.register.RegisterMarshaller;
import com.chelseasystems.cs.register.CMSRegister;


/**
 *
 * <p>Title: CMSRegisterMarshaller</p>
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
public class CMSRegisterMarshaller extends RegisterMarshaller {

  /**
   *
   * @param cMSRegister CMSRegister
   * @return Map
   */
  public Map toValueObject(CMSRegister cMSRegister) {
    Map valueObject = new HashMap(1);
    valueObject = super.toValueObject(valueObject, cMSRegister);
    return valueObject;
  }

  /**
   *
   * @param valueObject Map
   * @return Register
   */
  public Register toBusinessObject(Map valueObject) {
    Register cMSRegister = this.createBusinessObjectInstance(valueObject);
    return cMSRegister;
  }

  /**
   *
   * @param valueObject Map
   * @return Register
   */
  protected Register createBusinessObjectInstance(Map valueObject) {
    return new CMSRegister("", "");
  }
}

