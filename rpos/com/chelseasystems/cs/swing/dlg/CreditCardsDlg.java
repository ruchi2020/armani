/*
 * put your module comment here
 * formatted with JxBeauty (c) johann.langhofer@nextra.at
 */


package com.chelseasystems.cs.swing.dlg;

import com.chelseasystems.cr.util.*;
import com.chelseasystems.cs.swing.dlg.GenericChooserRow;
import java.util.*;
import com.chelseasystems.cr.config.ConfigMgr;


/**
 * put your documentation comment here
 */
public class CreditCardsDlg {
  private static String configFilename = "creditcard.cfg";
  //  private boolean SolicitReasons = false;
  private GenericChooserRow tableData[] = null;
  private String reasons[] = null;
  private ResourceBundle res = null;
  private static ConfigMgr config = new ConfigMgr(configFilename);
  private static String cardCodes[] = null;
  private String cardTypeKey = null;

  /**
   * put your documentation comment here
   * @param   String cardTypeKey
   */
  public CreditCardsDlg(String cardTypeKey) {
    res = ResourceManager.getResourceBundle();
    this.cardTypeKey = cardTypeKey;
    System.out.println("In CreditCardsDlg()");
    init();
  }

  /**
   * put your documentation comment here
   */
  private void init() {
    try {
      Hashtable htCardCodes = getCreditCardCodes();
      String overrides[] = (String[])htCardCodes.keySet().toArray(new String[0]);
      int count = overrides.length;
      GenericChooserRow rows[] = new GenericChooserRow[count];
      reasons = new String[count];
      for (int i = 0; i < count; i++) {
        reasons[i] = res.getString((String)htCardCodes.get(overrides[i]));
        String display[] = {reasons[i]
        };
        rows[i] = new GenericChooserRow(display, overrides[i]);
      }
      tableData = rows;
    } catch (Exception e) {
      e.printStackTrace();
      System.out.println("Exception has occurred in loading CreditCardsDlg.");
    }
  }

  /**
   * Reads creditcard.cfg and builds the exempt ids
   * @return Hashtable
   */
  private Hashtable getCreditCardCodes() {
    Hashtable htCardDesc = new Hashtable(13);
    String cardTypes = config.getString(cardTypeKey);
    StringTokenizer st = new StringTokenizer(cardTypes, ",");
    cardCodes = new String[st.countTokens()];
    int i = 0;
    for (; st.hasMoreTokens(); ) {
      cardCodes[i] = st.nextToken();
      String cardCode = res.getString(config.getString(cardCodes[i] + ".CODE"));
      String cardDesc = res.getString(config.getString(cardCodes[i] + ".DESC"));
      htCardDesc.put(cardCode, cardDesc);
      i++;
    }
    return htCardDesc;
  }

  /**
   * put your documentation comment here
   * @return
   */
  public GenericChooserRow[] getTabelData() {
    return tableData;
  }

  /**
   * put your documentation comment here
   * @return
   */
  public String[] getReasons() {
    return reasons;
  }

  /**
   * put your documentation comment here
   * @return
   */
  public static String getConfigFilename() {
    return configFilename;
  }
}

