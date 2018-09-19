/*
 * put your module comment here
 * formatted with JxBeauty (c) johann.langhofer@nextra.at
 */


package com.chelseasystems.cs.config;

import java.util.Enumeration;
import java.util.Hashtable;
import java.util.List;
import com.chelseasystems.cr.business.BusinessObject;


/**
 * put your documentation comment here
 */
public class ArmConfig extends BusinessObject {
  private Hashtable htConfigAttribs;

  /**
   * put your documentation comment here
   */
  public ArmConfig() {
    htConfigAttribs = new Hashtable();
  }

  /**
   * put your documentation comment here
   * @param sConfigKey
   * @param lstArmConfigDet
   */
  public void addConfigDetail(String sConfigKey, List lstArmConfigDet) {
    if (sConfigKey == null || lstArmConfigDet == null) {
      return;
    }
    doAddConfigDetail(sConfigKey, lstArmConfigDet);
  }

  /**
   * put your documentation comment here
   * @param sConfigKey
   * @param lstArmConfigDet
   */
  public void doAddConfigDetail(String sConfigKey, List lstArmConfigDet) {
    htConfigAttribs.put(sConfigKey, lstArmConfigDet);
  }

  /**
   * put your documentation comment here
   * @param sConfigKey
   * @return
   */
  public List getConfigDetailsFor(String sConfigKey) {
    return (List)htConfigAttribs.get(sConfigKey);
  }

  /**
   * put your documentation comment here
   * @return
   */
  public Enumeration getConfigurationTypes() {
    return htConfigAttribs.keys();
  }
}
