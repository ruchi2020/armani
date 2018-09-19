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
 | 3    | 07-19-2007 | Ruchi     | PCR       | init()created a arrayList to hold priority and
 |      |            |           |			 | code.                                        +
 +------+------------+-----------+-----------+----------------------------------------------+
 | 2    | 09-07-2005 | Manpreet  | 898       | init()Dont have to read reasons from Resource|
 +------+------------+-----------+-----------+----------------------------------------------+
 | 1    | N/A        | Khyati    | N/A       | Initial version                              |
 +------+------------+-----------+-----------+----------------------------------------------+
 
 */
import com.chelseasystems.cr.config.ConfigMgr;
import com.chelseasystems.cr.config.FileMgr;
import com.chelseasystems.cr.util.*;
import com.chelseasystems.cs.swing.dlg.GenericChooserRow;
import java.io.PrintStream;
import java.util.*;
import com.chelseasystems.cs.config.ArmConfigDetail;
import com.chelseasystems.cs.discount.*;


/**
 * put your documentation comment here
 */
public class DiscountReasonHelper {
  //private static String configFilename = "discount.cfg";
  private static String configFilename = "ArmaniCommon.cfg";
  private boolean SolicitReasons = false;
  private GenericChooserRow tableData[] = null;
  private String reasons[] = null;
  private ResourceBundle res = null;
  private Vector keysThatSolicitReasonVector = null;

  /**
   * put your documentation comment here
   */
  public DiscountReasonHelper() {
    //        res = ResourceManager.getResourceBundle();
    //        System.out.println("In DiscountReasonHelper()");
    init();
  }

  /**
   * put your documentation comment here
   */
  private void init() {
    try {
     ConfigMgr config = new ConfigMgr("discount.cfg");//Ruchi
     boolean discountSortByCode = true;
      SolicitReasons = CMSDiscountMgr.getSolicitReasons();
      if (SolicitReasons) {
       Hashtable htDiscountReason = CMSDiscountMgr.getReasonHashtable(); 
       // Introduced new variable discountList for PCR:1800 to hold the priority and code .
       //this getCodePriorityHashtable has code and prioritycode
       ArrayList discountList = CMSDiscountMgr.getConfigDetailList();
       Collections.sort(discountList, new PriorityOrCodeTypeComparator());
        String overrides[] = (String[])htDiscountReason.keySet().toArray(new String[0]);
        int count = overrides.length;
        GenericChooserRow rows[] = new GenericChooserRow[count];
        reasons = new String[count];
        int j = 0;
        String display[];
		//itererating over the discountList  
		for (int i =0;  i < discountList.size(); i++) {
			ArmConfigDetail acd = (ArmConfigDetail) discountList.get(i);
			reasons[j] = acd.getDescription();
			display = new String[]{ reasons[j] };
			rows[j] = new GenericChooserRow(display, acd.getCode());
			j++;
		}
       tableData = rows;
      }
    } catch (Exception e) {
      e.printStackTrace();
      SolicitReasons = false;
      System.out.println("Exception has occurred in loading DiscountReasonHelper.");
    }
  }

  /**
   * put your documentation comment here
   * @param keys
   * @return
   */
  private String[] getActiveKeys(String keys) {
    StringTokenizer tokenizer = new StringTokenizer(keys, ",");
    int count = tokenizer.countTokens();
    String overrides[] = new String[count];
    for (int i = 0; i < count; i++)
      overrides[i] = tokenizer.nextToken();
    return overrides;
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
   * @param keysThatSolicitReason
   */
  private void loadSolicitReasonKeys(String keysThatSolicitReason) {
    keysThatSolicitReasonVector = new Vector();
    for (StringTokenizer st = new StringTokenizer(keysThatSolicitReason, ","); st.hasMoreElements();
        keysThatSolicitReasonVector.add(st.nextElement()));
  }

  /**
   * put your documentation comment here
   * @return
   */
  public boolean isSolicitReasons() {
    return SolicitReasons;
  }

  /**
   * put your documentation comment here
   * @return
   */
  public static String getConfigFilename() {
    return configFilename;
  }
  //Created class for PCR:1800 to sort on the basis of priority or code
  private class PriorityOrCodeTypeComparator implements Comparator {
	  ConfigMgr config = new ConfigMgr("discount.cfg");
	  String discountSortOrder = config.getString("DISCOUNT_SORT_ORDER");
	  public int compare(Object obj1, Object obj2) {
	    	String value1 = null;
			String value2 = null;
			if ("PRIORITY".equalsIgnoreCase(discountSortOrder)) {
				value1 = ((ArmConfigDetail) obj1).getPriority();
				value2 = ((ArmConfigDetail) obj2).getPriority();
			} else {
				value1 = ((ArmConfigDetail) obj1).getCode();
				value2 = ((ArmConfigDetail) obj2).getCode();
			}
			return value1.compareTo(value2);			
		}
  }// closed PriorityOrCodeTypeComparator
  
}

