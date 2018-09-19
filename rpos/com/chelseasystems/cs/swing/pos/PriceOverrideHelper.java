/*
 * put your module comment here
 * formatted with JxBeauty (c) johann.langhofer@nextra.at
 */


package com.chelseasystems.cs.swing.pos;


/*
 History:
 +------+------------+-----------+-----------+----------------------------------------------+
 | Ver# | Date       | By        | Defect #  | Description                                  |
 +------+------------+-----------+-----------+----------------------------------------------+
 | 2    | 09-07-2005 | Manpreet  | 903       | Reading reasons frm ArmaniCommon.cfg         |
 +------+------------+-----------+-----------+----------------------------------------------+
 | 1    | N/A        | N/A       | N/A       | Initial version                              |
 +------+------------+-----------+-----------+----------------------------------------------+
 */
import java.util.*;
import com.chelseasystems.cr.util.INIFile;
import com.chelseasystems.cr.util.INIFileException;
import com.chelseasystems.cr.config.FileMgr;
import com.chelseasystems.cs.swing.dlg.GenericChooserRow;
import com.chelseasystems.cr.config.ConfigMgr;
import com.chelseasystems.cr.util.*;
import com.chelseasystems.cs.util.ArmConfigLoader;


/**
 * This is the helper class for price overrides.   It reads the configuration
 * file when the object is created.   When required, it provides the [] of GenericChooserRows
 * which the GenericChooseFromTableDialog constructor requires.  It also
 * provides the applet with activate/deactivate property.
 *
 * Creation date: (8/2/2001 3:28:31 PM)
 * @author: Moises G. Solis
 */
public class PriceOverrideHelper extends java.lang.Object {
  private static java.lang.String configFilename;
  private boolean SolicitReasons;
  private GenericChooserRow[] tableData;
  private java.lang.String[] reasons;
  private ResourceBundle res = ResourceManager.getResourceBundle();
  private Vector keysThatSolicitReasonVector;
  static {
    configFilename = "priceoverride.cfg";
  }


  /**
   * PriceOverrideHelper constructor comment.
   */
  public PriceOverrideHelper() {
    super();
    System.out.println("In PriceOverrideHelper()");
    init();
  }


  /**
   * Initialize the object.
   * Creation date: (8/2/2001 4:15:48 PM)
   */
  private void init() {
    try {
      INIFile file = new INIFile(FileMgr.getLocalFile("config", configFilename), false);
      setSolicitReasons(file.getValue("SOLICIT_REASONS"));
      if (SolicitReasons) {
        //MSB - 09/07/05
        //Read reasons from ArmaniCommon.cfg
        //        			String[] overrides = getActiveKeys(file.getValue("ACTIVE_OVERRIDES"));
        //        			int count = overrides.length;
        //        			GenericChooserRow[] rows = new GenericChooserRow[count];
        //                                reasons = new java.lang.String[count];
        //
        //        			for (int i = 0; i < count; i++ ) {
        //                                        reasons[i] = res.getString((String) file.getValue(overrides[i]));
        //        				String[] display = { reasons[i] };
        //        				rows[i] = new GenericChooserRow( display, overrides[i]);
        //        			}
        //
        //        			tableData = rows;
        //        		}
//        ConfigMgr armConfig = new ConfigMgr("ArmaniCommon.cfg");
        ArmConfigLoader armConfig = new ArmConfigLoader();
        String reasonTypes = armConfig.getString("PRICE_OVERRIDE_REASON_CD");
        ArrayList list = new ArrayList();
        String nextToken;
        if ((reasonTypes != null) && (reasonTypes.trim().length() > 0)) {
          StringTokenizer stk = new StringTokenizer(reasonTypes, ",");
          String types[] = new String[stk.countTokens()];
          String desc[] = new String[stk.countTokens()];
          int i = 0;
          while (stk.hasMoreTokens()) {
            nextToken = stk.nextToken();
            types[i] = armConfig.getString(nextToken + ".CODE");
            desc[i] = armConfig.getString(nextToken + ".LABEL");
            if ((desc[i] != null) && (desc[i].trim().length() > 0)) {
              list.add(new GenericChooserRow(new String[] {desc[i]
              }
                  , types[i]));
            }
            i++;
          }
          tableData = (GenericChooserRow[])list.toArray(new GenericChooserRow[0]);
        }
      }
    } catch (INIFileException e) {
      SolicitReasons = false;
      System.out.println("Exception has occurred in loading PriceOverrideHelper.");
      switch (e.getCode()) {
        case INIFileException.KEYNOTDEFINED:
          System.out.println("Key was not defined");
          break;
        case INIFileException.HEADERNOTDEFINED:
          System.out.println("Header was not defined");
          break;
        case INIFileException.FILENOTFOUND:
          System.out.println(configFilename + " was not found.");
          break;
        case INIFileException.IOEXCEPTION:
          System.out.println("Could not read " + configFilename);
          break;
        default:
          System.out.println("Unknown error occurred...");
      }
    } catch (Exception e) {}
  }


  /**
   * Set the overall behavior.
   * Creation date: (8/2/2001 4:22:06 PM)
   * @param newSolicitReasons boolean SolicitReasons
   */
  private void setSolicitReasons(String activate) {
    SolicitReasons = (activate.equalsIgnoreCase("true")) ? true : false;
  }


  /**
   * Parses the comma delimited key for active discounts.
   * Creation date: (8/2/2001 4:33:10 PM)
   * @return java.lang.String[]
   * @param keys java.lang.String overrides
   */
  private String[] getActiveKeys(String keys) {
    StringTokenizer tokenizer = new StringTokenizer(keys, ",");
    int count = tokenizer.countTokens();
    String[] overrides = new String[count];
    for (int i = 0; i < count; i++) {
      overrides[i] = tokenizer.nextToken();
    }
    return overrides;
  }


  /**
   * Returns the data for the table.
   * Creation date: (8/2/2001 5:50:12 PM)
   * @return com.chelseasystems.cr.swing.dlg.GenericChooserRow tableData
   */
  public GenericChooserRow[] getTabelData() {
    return tableData;
  }


  /**
   * This method is used by the PDA.   It will display the array
   * of strings for selection purposes.
   * Creation date: (8/2/2001 5:50:12 PM)
   * @return java.lang.String[] reasons
   */
  public java.lang.String[] getReasons() {
    return reasons;
  }


  /**
   * put your documentation comment here
   * @param keysThatSolicitReason
   */
  private void loadSolicitReasonKeys(String keysThatSolicitReason) {
    keysThatSolicitReasonVector = new Vector();
    StringTokenizer st = new StringTokenizer(keysThatSolicitReason, ",");
    while (st.hasMoreElements())
      keysThatSolicitReasonVector.add(st.nextElement());
  }


  /**
   * Allows for enabling or disabling of price overrides.   This is set by the
   * configuration file.
   * Creation date: (8/2/2001 3:52:57 PM)
   * @return boolean isSolicitReasons
   */
  public boolean isSolicitReasons() {
    return SolicitReasons;
  }


  /**
   * Returns the name of the configuration file.
   * Creation date: (8/2/2001 3:29:23 PM)
   * @return java.lang.String filename
   */
  public static java.lang.String getConfigFilename() {
    return configFilename;
  }
}
