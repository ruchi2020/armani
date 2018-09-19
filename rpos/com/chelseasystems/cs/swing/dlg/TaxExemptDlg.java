/*
 * put your module comment here
 * formatted with JxBeauty (c) johann.langhofer@nextra.at
 */
// Decompiled by Jad v1.5.8e2. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://kpdus.tripod.com/jad.html
// Decompiler options: packimports(3) definits noctor radix(10) lradix(10)
// Source File Name:   PriceOverrideHelper.java


package com.chelseasystems.cs.swing.dlg;

import com.chelseasystems.cr.util.*;
import com.chelseasystems.cs.swing.dlg.GenericChooserRow;
import java.util.*;
import com.chelseasystems.cr.config.ConfigMgr;
import com.chelseasystems.cs.swing.dlg.GenericDisplayTableOptionDlg;
import com.chelseasystems.cs.util.ArmConfigLoader;


/**
 * put your documentation comment here
 */
public class TaxExemptDlg {
  private static String configFilename = "ArmaniCommon.cfg";


  //  private boolean SolicitReasons = false;
  private GenericChooserRow tableData[] = null;
  private String reasons[] = null;
  private ResourceBundle res = null;
  private Vector keysThatSolicitReasonVector = null;


//  private static ConfigMgr config = new ConfigMgr(configFilename);
  private static ArmConfigLoader config = new ArmConfigLoader();
  private static String reasonCodes[] = null;


  /**
   * put your documentation comment here
   */
  public TaxExemptDlg() {
    res = ResourceManager.getResourceBundle();
    System.out.println("In TaxExemptDlg()");
    init();
  }


  /**
   * put your documentation comment here
   */
  private void init() {
    try {
      Hashtable htTaxExemptIds = getTaxExemptIds();
      String overrides[] = (String[])htTaxExemptIds.keySet().toArray(new String[0]);
      Arrays.sort(overrides);
      int count = overrides.length;
      GenericChooserRow rows[] = new GenericChooserRow[count];
      reasons = new String[count];
      for (int i = 0; i < count; i++) {
        reasons[i] = res.getString((String)htTaxExemptIds.get(overrides[i]));
        String display[] = {reasons[i]
        };
        rows[i] = new GenericChooserRow(display, overrides[i]);
      }
      tableData = rows;
    } catch (Exception e) {
      e.printStackTrace();
      System.out.println("Exception has occurred in loading TaxExemptDlg.");
    }
  }


  /**
   * Reads vatExempt.cfg and builds the exempt ids
   * @return Hashtable
   */
  private Hashtable getTaxExemptIds() {
    Hashtable htVatExemptDesc = new Hashtable();
    String discReasonCodes = config.getString("VAT_EXEMPT_CD");
    StringTokenizer st = new StringTokenizer(discReasonCodes, ",");
    reasonCodes = new String[st.countTokens()];
    int i = 0;
    for (; st.hasMoreTokens(); ) {
      reasonCodes[i] = st.nextToken();
      String taxExemptCode = res.getString(config.getString(reasonCodes[i] + ".CODE"));
      String taxExemptDesc = res.getString(config.getString(reasonCodes[i] + ".LABEL"));
      htVatExemptDesc.put(taxExemptCode, taxExemptDesc);
      i++;
    }
    return htVatExemptDesc;
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
