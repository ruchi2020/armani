/*
 * put your module comment here
 * formatted with JxBeauty (c) johann.langhofer@nextra.at
 */


package com.chelseasystems.cs.address;

import com.chelseasystems.cr.config.ConfigMgr;
import java.util.Vector;
import java.util.StringTokenizer;
import com.chelseasystems.cs.util.ArmConfigLoader;


/**
 * <p>Title: AddressMgr</p>
 *
 * <p>Description: Manages Address Panel configurations</p>
 *
 * <p>Copyright: Copyright (c) 2005</p>
 *
 * <p>Company:SkillnetInc </p>
 *
 * @author Manpreet S Bawa
 * @version 1.0
 */
/*
 History:
 +------+------------+-----------+-----------+----------------------------------------------------+
 | Ver# | Date       | By        | Defect #  | Description                                        |
 +------+------------+-----------+-----------+----------------------------------------------------+
 | 1    | 03-22-2005 | Manpreet  | N/A       | POS_104665_FS_CustomerManagement_Rev2              |
 +------+------------+-----------+-----------+----------------------------------------------------+
 | 2    | 09-01-2005 | Manpreet  |894,895,812| Read AddressType, PhoneType and Country from       |
 |      |            |           |           | ArmaniCommon.cfg also changed naming conventions of|
 |      |            |           |           | methods.                                           |
 +------+------------+-----------+-----------+----------------------------------------------------+
 | 3    | 09-06-2005 | Manpreet  | N/A       | Try/Catch blocks for populating addressType, phone |
 +------+------------+-----------+-----------+----------------------------------------------------+
 */
public class AddressMgr {
  private ConfigMgr configAddress;
  private ArmConfigLoader configArmGlobal;
  private Vector vecAddressFormats;
  private Vector vecAddressPanels;
  private Vector vecAddressTypeLabels;
  private Vector vecAddressTypeKeys;
  private Vector vecTelephoneTypeLabels;
  private Vector vecTelephoneTypeKeys;
  private Vector vecCountryKeys;
  private Vector vecCountryLabels;
  private String sDefaultAddressFormat;
  private String sDefaultAddressPanel;
  private final String CONFIGURATION_FILE = "address.cfg";
  private final String COUNTRY_LIST_KEY = "COUNTRY_LIST";
  private final String PANEL_KEY = ".ADDRESS_PANEL";
  private final String DEFAULT_COUNTRY_KEY = "DEFAULT_COUNTRY";
  private final String SEPERATOR = ",";
  private final String ADDRESS_TYPE_KEY = "ADDRESS_TYPE";
  private final String LABEL_KEY = ".LABEL";
  private final String PHONE_TYPE_KEY = "PHONE_TYPE";
  private final String COUNTRY = "COUNTRY";


  /**
   * Default Constructor
   */
  public AddressMgr() {
    vecAddressPanels = new Vector();
    vecAddressFormats = new Vector();
    vecAddressTypeKeys = new Vector();
    vecAddressTypeLabels = new Vector();
    vecTelephoneTypeLabels = new Vector();
    vecTelephoneTypeKeys = new Vector();
    vecCountryKeys = new Vector();
    vecCountryLabels = new Vector();
    configAddress = new ConfigMgr(CONFIGURATION_FILE);
//    configArmGlobal = new ConfigMgr("ArmaniCommon.cfg");
    configArmGlobal = new ArmConfigLoader();
    sDefaultAddressFormat = configAddress.getString(DEFAULT_COUNTRY_KEY);
    makeAddressPanels(configAddress.getString(COUNTRY_LIST_KEY));
    makeAddressTypes(configArmGlobal.getString(ADDRESS_TYPE_KEY));
    makePhoneTypes(configArmGlobal.getString(PHONE_TYPE_KEY));
    makeCountries(configArmGlobal.getString(COUNTRY));
  }


  /**
   *
   * @return Vector
   */
  public Vector getAddressFormats() {
    return vecAddressFormats;
  }


  /**
   *
   * @return Vector
   */
  public Vector getAddressPanels() {
    return vecAddressPanels;
  }


  /**
   *
   * @return String
   */
  public String getDefaultAddressFormat() {
    return sDefaultAddressFormat;
  }


  /**
   *
   * @return String
   */
  public String getDefaultAddressPanel() {
    return sDefaultAddressPanel;
  }


  /**
   *
   * @return Vector
   */
  public Vector getAddressTypeKeys() {
    return vecAddressTypeKeys;
  }


  /**
   *
   * @return Vector
   */
  public Vector getAddressTypes() {
    return vecAddressTypeLabels;
  }


  /**
   *
   * @return Vector
   */
  public Vector getPhoneTypes() {
    return vecTelephoneTypeLabels;
  }


  /**
   *
   * @return Vector
   */
  public Vector getPhoneTypeKeys() {
    return vecTelephoneTypeKeys;
  }


  /**
   * put your documentation comment here
   * @return
   */
  public Vector getCountryLabels() {
    return vecCountryLabels;
  }


  /**
   * put your documentation comment here
   * @return
   */
  public Vector getCountryKeys() {
    return vecCountryKeys;
  }


  /**
   * put your documentation comment here
   * @param sValues
   */
  private void makeCountries(String sValues) {
    try {
      String sTmp;
      StringTokenizer sTokens = new StringTokenizer(sValues, SEPERATOR);
      while (sTokens.hasMoreTokens()) {
        sTmp = sTokens.nextToken();
        if (configArmGlobal.getString(sTmp + LABEL_KEY) != null) {
          vecCountryKeys.addElement(configArmGlobal.getString(sTmp + ".CODE"));
          vecCountryLabels.addElement(configArmGlobal.getString(sTmp + LABEL_KEY));
        }
      }
    } catch (Exception e) {
      System.out.println("Exception loading countries " + e.getMessage());
    }
  }


  /**
   *
   * @param sValues String
   */
  private void makeAddressPanels(String sValues) {
    String sTmp;
    StringTokenizer sTokens = new StringTokenizer(sValues, SEPERATOR);
    while (sTokens.hasMoreTokens()) {
      sTmp = sTokens.nextToken();
      //Check if country maching panel definition exists.
      if (configAddress.getString(sTmp + PANEL_KEY) != null) {
        if (sDefaultAddressFormat.equals(sTmp)) {
          sDefaultAddressPanel = configAddress.getString(sTmp + PANEL_KEY);
        }
        vecAddressFormats.addElement(sTmp);
        vecAddressPanels.addElement(configAddress.getString(sTmp + PANEL_KEY));
      }
    }
  }


  /**
   *
   * @param sValues String
   */
  private void makeAddressTypes(String sValues) {
    try {
      String sTmp;
      StringTokenizer sTokens = new StringTokenizer(sValues, SEPERATOR);
      while (sTokens.hasMoreTokens()) {
        sTmp = sTokens.nextToken();
        if (configArmGlobal.getString(sTmp + LABEL_KEY) != null) {
          vecAddressTypeKeys.addElement(configArmGlobal.getString(sTmp + ".CODE"));
          vecAddressTypeLabels.addElement(configArmGlobal.getString(sTmp + LABEL_KEY));
        }
      }
    } catch (Exception e) {
      e.printStackTrace();
      System.out.println("Exception loading Address Types");
    }
  }


  /**
   *
   * @param sValues String
   */
  private void makePhoneTypes(String sValues) {
    try {
      String sTmp;
      StringTokenizer sTokens = new StringTokenizer(sValues, SEPERATOR);
      while (sTokens.hasMoreTokens()) {
        sTmp = sTokens.nextToken();
        if (configArmGlobal.getString(sTmp + LABEL_KEY) != null) {
          vecTelephoneTypeKeys.addElement(configArmGlobal.getString(sTmp + ".CODE"));
          vecTelephoneTypeLabels.addElement(configArmGlobal.getString(sTmp + LABEL_KEY));
        }
      }
    } catch (Exception e) {
      e.printStackTrace();
      System.out.println("Exception loading phone types");
    }
  }
}
