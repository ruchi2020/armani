/*
 * put your module comment here
 * formatted with JxBeauty (c) johann.langhofer@nextra.at
 */


package com.chelseasystems.cs.swing.model;

import com.chelseasystems.cr.swing.ScrollableTableModel;
import com.chelseasystems.cs.customer.CMSCustomer;
import com.chelseasystems.cs.customer.CustomerSalesAssociate;
import com.chelseasystems.cs.address.Address;
import com.chelseasystems.cr.swing.CMSApplet;
import com.chelseasystems.cs.store.CMSStore;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.TreeMap;
import java.util.Vector;


/**
 * <p>Title:CustomerListModel.java </p>
 *
 * <p>Description: CustomerListModel</p>
 *
 * <p>Copyright: Copyright (c) 2005</p>
 *
 * <p>Company: SkillNet Inc. </p>
 *
 * @author Manpreet S Bawa
 * @version 1.0
 */
/*
 History:
 +------+------------+-----------+-----------+----------------------------------------------------+
 | Ver# | Date       | By        | Defect #  | Description                                        |
 +------+------------+-----------+-----------+----------------------------------------------------+
 | 1    | 03-25-2005 | Manpreet  | N/A       | POS_104665_FS_CustomerManagement_Rev2              |
 +------+------------+-----------+-----------+----------------------------------------------------+
 */
public class CustomerListModel extends ScrollableTableModel {
  private final String COLUMN_NAMES[] = {"ID", "Last/First Name", "Address"
  };
  private final String COLUMN_NAMES_JP[] = {"ID", "Last/First Name (JP)","Last/First Name","Address"
  };

  public final static int ID = 0;
  public final static int LAST_FIRST_NAME = 1;
  public final static int LAST_FIRST_NAME_JP=2;
  public final static int ADDRESS_JP=3;
  public final static int ADDRESS = 2;
  protected Vector customerVec;
  protected int lastSelectedCustomerRow = -1;
  protected java.util.ResourceBundle res = com.chelseasystems.cr.util.ResourceManager.getResourceBundle();

  public static final String ISO_COUNTRY_JAPAN = "JP";
  public static final String ISO_COUNTRY_US = "US";
  private String sStoreISOCountry="US";
  private String sPrefectureSeperator;
  private String sAddressSeperator;
  private String sBuildingSeperator;
  private String sNameSeperator;

  private CustomerSalesAssociate custSalesAssociate;
  private CMSCustomer customer = null;
  private List salesAssocList = new ArrayList();
  private String storeId;

  /**
   * put your documentation comment here
   */
  public CustomerListModel() {
    initSeperators();
      customerVec = new Vector();
      String sIdentiFiers[] = new String[COLUMN_NAMES.length];
      for (int iCtr = 0; iCtr < COLUMN_NAMES.length; iCtr++) {
        sIdentiFiers[iCtr] = res.getString(COLUMN_NAMES[iCtr]);
      }
      this.setColumnIdentifiers(sIdentiFiers);
  }

  private void initSeperators()
  {
    sPrefectureSeperator = res.getString("PREFECTURE_SEPERATOR");
    sAddressSeperator = res.getString("ADDRESS_SEPERATOR");
    sBuildingSeperator = res.getString("BUILDING_SEPERATOR");
    sNameSeperator = res.getString("NAME_SEPERATOR");
    if(sNameSeperator.equals("NAME_SEPERATOR")) sNameSeperator = ",";

    if(sPrefectureSeperator.equals("PREFECTURE_SEPERATOR"))
      sPrefectureSeperator = ",";

    if(sAddressSeperator.equals("ADDRESS_SEPERATOR"))
      sAddressSeperator = ",";

    if(sBuildingSeperator.equals("BUILDING_SEPERATOR"))
      sBuildingSeperator = ",";
  }

  public CustomerListModel(String sISOCountry)
  {
    if(sISOCountry == null || sISOCountry.length()<1) return;
    sStoreISOCountry = sISOCountry;
    initSeperators();
    customerVec = new Vector();
    if(isModelForJapan())
    {
      String sIdentiFiers[] = new String[COLUMN_NAMES_JP.length];
      for (int iCtr = 0; iCtr < COLUMN_NAMES_JP.length; iCtr++) {
        sIdentiFiers[iCtr] = res.getString(COLUMN_NAMES_JP[iCtr]);
      }
      this.setColumnIdentifiers(sIdentiFiers);
    }
    else
    {
      String sIdentiFiers[] = new String[COLUMN_NAMES.length];
      for (int iCtr = 0; iCtr < COLUMN_NAMES.length; iCtr++) {
        sIdentiFiers[iCtr] = res.getString(COLUMN_NAMES[iCtr]);
      }
      this.setColumnIdentifiers(sIdentiFiers);

    }
  }

  /**
   * put your documentation comment here
   * @param cmsCustomer
   */
  public void addCustomer(CMSCustomer cmsCustomer) {
    if (cmsCustomer == null)
      return;
    addRow(cmsCustomer);
    customerVec.add(cmsCustomer);
    this.fireTableDataChanged();
  }

  //  public void deleteCustomerAt(int row)
  //  {
  //      removeRowInPage(row);
  //      this.fireTableRowsDeleted(row, row);
  //  }
  public CMSCustomer getCustomerAt(int row) {
    return (CMSCustomer)this.getRowInPage(row);
  }

  public String getPreferredISOCountry()
  {
    return this.sStoreISOCountry;
  }

  public boolean isModelForJapan()
  {
     return sStoreISOCountry.equals(ISO_COUNTRY_JAPAN);
  }

  public boolean isModelForUS()
  {
    return sStoreISOCountry.equals(ISO_COUNTRY_US);
  }

  private Object getValue_JP(CMSCustomer cmsCustomer, int iColumn)
  {
    switch (iColumn) {
      case ID:
        return cmsCustomer.getId();
      case LAST_FIRST_NAME:
        // Issue 1324 required to switch columns.
        return getNameString_JP(cmsCustomer);
      case LAST_FIRST_NAME_JP:
        // Issue 1324 required to swich columns.
        return getNameString(cmsCustomer);
      case ADDRESS_JP:
        CMSStore store = (CMSStore)CMSApplet.theAppMgr.getGlobalObject("STORE");
        if(cmsCustomer.testIfAddressViewable(store.getId()))
          return getAddressString_JP(cmsCustomer.getPrimaryAddress());
        return res.getString("VIP_CUSTOMER_ADDRESS");
    }
    return "";
  }

  /**
   * put your documentation comment here
   * @param row
   * @param column
   * @return
   */
  public Object getValueAt(int row, int column) {
    CMSCustomer cmsCustomer = (CMSCustomer)this.getCurrentPage().elementAt(row);

    if(isModelForJapan()) return getValue_JP(cmsCustomer, column);
     switch (column) {
      case ID:
        return cmsCustomer.getId();
      case LAST_FIRST_NAME:
        return getNameString(cmsCustomer);
      case ADDRESS:
        return getAddressString(cmsCustomer.getPrimaryAddress());
    }
    return "";
  }

  /**
   * put your documentation comment here
   * @param RowsShown
   */
  public void setRowsShown(int RowsShown) {
    super.setRowsShown(RowsShown);
  }

  /**
   * put your documentation comment here
   * @return
   */
  public int getColumnCount() {

    if(isModelForJapan()) return this.COLUMN_NAMES_JP.length;
    return COLUMN_NAMES.length;
  }

  /**
   * put your documentation comment here
   * @param address
   * @return
   */
  private String getAddressString(Address address) {
    if (address == null)
      return "";
    String sTmp = "";
    if (address.getAddressLine1() != null && address.getAddressLine1().indexOf("null") == -1)
      sTmp = address.getAddressLine1();
    if (address.getCity() != null && address.getCity().indexOf("null") == -1) {
      if (sTmp.length() > 1)
        sTmp += sAddressSeperator;
      sTmp += address.getCity();
    }
    if (address.getState() != null && address.getState().indexOf("null") == -1) {
      if (sTmp.length() > 1)
        sTmp += sPrefectureSeperator;
      sTmp += address.getState();
    }
    if (address.getZipCode() != null && address.getZipCode().indexOf("null") == -1) {
      if (sTmp.length() > 1)
        sTmp += ",";
      sTmp += address.getZipCode();
    }
    return sTmp.toUpperCase();
  }

  private String getNameString_JP(CMSCustomer cmsCustomer)
  {
    String sTmp="";
    if (cmsCustomer.getDBLastName() != null && cmsCustomer.getDBLastName().indexOf("null") == -1) {
      sTmp += cmsCustomer.getDBLastName();
    }
    if (cmsCustomer.getDBFirstName() != null && cmsCustomer.getDBFirstName().indexOf("null") == -1) {
      if (sTmp.length() > 1)
        sTmp += sNameSeperator;
      sTmp += cmsCustomer.getDBFirstName();
    }
    return sTmp;
  }

  private String getNameString(CMSCustomer cmsCustomer)
  {
    String sTmp="";
    if (cmsCustomer.getLastName() != null && cmsCustomer.getLastName().indexOf("null") == -1) {
      sTmp += cmsCustomer.getLastName();
    }
    if (cmsCustomer.getFirstName() != null && cmsCustomer.getFirstName().indexOf("null"
        ) == -1
        &&
        cmsCustomer.getFirstName().trim().length() >0
        ) {
      if (sTmp.length() > 1)
        sTmp += sNameSeperator;
      sTmp += cmsCustomer.getFirstName();
    }
    return sTmp;
  }

  private String getAddressString_JP(Address address) {
    if (address == null)
      return "";
    String sTmp = "";
//  MSB - Issue 1324 (02/03/06)
//  Zip Code not required for Japan.
//    if (address.getZipCode() != null && address.getZipCode().indexOf("null") == -1) {
//      sTmp += address.getZipCode();
//    }
    if (address.getState() != null && address.getState().indexOf("null") == -1) {
      if (sTmp.length() > 1)
        sTmp += sPrefectureSeperator;
      sTmp += address.getState();
    }
    
    //Added by Vivek Mishra to include city with address in JAPAN region
    if (address.getCity() != null && address.getCity().indexOf("null") == -1) {
        if (sTmp.length() > 1)
          sTmp += sPrefectureSeperator;
        sTmp += address.getCity();
      }
    //End
    
    if (address.getAddressLine1() != null && address.getAddressLine1().indexOf("null") == -1)
    {
      if (sTmp.length() > 1)
        sTmp += sAddressSeperator;
      sTmp += address.getAddressLine1();
    }
    if (address.getAddressLine2() != null && address.getAddressLine2().indexOf("null") == -1)
    {
      if (sTmp.length() > 1)
        sTmp += sBuildingSeperator;
      sTmp += address.getAddressLine2();
    }
    return sTmp.toUpperCase();
  }

  /**
   * put your documentation comment here
   * @param selectedCustomerID
   * @return
   */
  public int sortCustomers(String selectedCustomerID) {
    TreeMap sortColumnMap = new TreeMap();
    CMSCustomer tmpCust = null;
    int selectedCustomerRow = -1;
    for (int i = 0; i < customerVec.size(); i++) {
      tmpCust = (CMSCustomer)customerVec.elementAt(i);
      sortColumnMap.put(tmpCust.getLastName().toLowerCase().trim() + ","
          + tmpCust.getFirstName().toLowerCase().trim() + "," + tmpCust.getId().trim(), tmpCust);
    }
    if (sortColumnMap.size() > 0) {
      for (int i = 0; i < customerVec.size(); i++) {
        removeRowInModel(customerVec.elementAt(i));
      }
      Vector sortedCustVec = new Vector(sortColumnMap.values());
      for (int i = 0; i < sortedCustVec.size(); i++) {
        tmpCust = (CMSCustomer)sortedCustVec.elementAt(i);
        addRow(tmpCust);
        if (selectedCustomerID != null && tmpCust != null
            && selectedCustomerID.trim().equalsIgnoreCase(tmpCust.getId().trim()))
          selectedCustomerRow = i;
      }
      fireTableDataChanged();
    }
    this.lastSelectedCustomerRow = selectedCustomerRow;
    return selectedCustomerRow;
  }

  public int orderCustomerByHomeStore(String selectedCustomerID) {
	  Vector vecOriginalData = new Vector();
	  Vector vecHomeStoreCusts = new Vector();
	  Vector vecOtherStoreCusts = new Vector();
	  int selectedCustomerRow = -1;

	  storeId = ((CMSStore)CMSApplet.theAppMgr.getGlobalObject("STORE")).getId();
	  //System.out.println("storeId: " + storeId);
	  for (int i = 0; i < customerVec.size(); i++) {
		  vecOriginalData.addElement((CMSCustomer)customerVec.elementAt(i));
	  }
	  if (vecOriginalData.size() > 0) {
	  	for (int i = 0; i < customerVec.size(); i++) {
	  		removeRowInModel(customerVec.elementAt(i));
	  	}
	  	for (int i = 0; i < vecOriginalData.size(); i++) {
	  		customer = (CMSCustomer)vecOriginalData.elementAt(i);
	  		if (isCustomerFromCurrentStore(customer)) {
	  			vecHomeStoreCusts.addElement(customer);
	  		} else {
	  			vecOtherStoreCusts.addElement(customer);
	  		}
	 	}
	  	//System.out.println("vecHomeStoreCusts size: " + vecHomeStoreCusts.size());
	  	//System.out.println("before vecHomeStoreCusts: " + vecHomeStoreCusts);
	  	Collections.sort(vecHomeStoreCusts, new LastNameComparator());
	  	//System.out.println("after vecHomeStoreCusts: " + vecHomeStoreCusts);

	  	//System.out.println("vecOtherStoreCusts size: " + vecOtherStoreCusts.size());
	  	//System.out.println("before vecOtherStoreCusts: " + vecOtherStoreCusts);
	  	Collections.sort(vecOtherStoreCusts, new LastNameComparator());
	  	//System.out.println("after vecOtherStoreCusts: " + vecOtherStoreCusts);

	  	//Add the vectors to the model, home first and then the other
                int sortIndex = 0;
	  	for (int i = 0; i < vecHomeStoreCusts.size(); i++, sortIndex++) {
	  		customer = (CMSCustomer)vecHomeStoreCusts.elementAt(i);
	  		addRow(customer);
	  		if (selectedCustomerID != null && customer != null &&
	  	  			selectedCustomerID.trim().equalsIgnoreCase(customer.getId().trim()))
	  	  		selectedCustomerRow = sortIndex;
	  	}
	  	for (int i = 0; i < vecOtherStoreCusts.size(); i++, sortIndex++)	{
	  		customer = (CMSCustomer)vecOtherStoreCusts.elementAt(i);
	  		addRow(customer);
	  		if (selectedCustomerID != null && customer != null &&
	  				selectedCustomerID.trim().equalsIgnoreCase(customer.getId().trim()))
	  			selectedCustomerRow = sortIndex;
	  	}
	  	fireTableDataChanged();
	  }
	  this.lastSelectedCustomerRow = selectedCustomerRow;
	  return selectedCustomerRow;
	}

  /**
   * put your documentation comment here
   * @return
   */
  public int getLastSelectedCustomerRow() {
    return lastSelectedCustomerRow;
  }

  /**
   * put your documentation comment here
   * @param lastSelectedCustomerRow
   */
  public void setLastSelectedCustomerRow(int lastSelectedCustomerRow) {
    this.lastSelectedCustomerRow = lastSelectedCustomerRow;
  }

  /**
   * put your documentation comment here
   */
  public void clear() {
    super.clear();
    customerVec = new Vector();
  }

  /**
   * Returns true if customer is from the current store
   * @param customer
   * @return
   */
  public boolean isCustomerFromCurrentStore(CMSCustomer customer) {
    salesAssocList = customer.getSalesAssociates();
    for (int i = 0; i < salesAssocList.size(); i++) {
      custSalesAssociate = (CustomerSalesAssociate)salesAssocList.get(i);
      if (custSalesAssociate.getStoreId().equals(storeId))
        return true;
    }
    return false;
  }

  public class LastNameComparator implements Comparator {
    public int compare(Object obj1, Object obj2) {
      String lastName1 = ((CMSCustomer)obj1).getLastName().toUpperCase();
      String firstName1 = ((CMSCustomer)obj1).getFirstName().toUpperCase();
      String lastName2 = ((CMSCustomer)obj2).getLastName().toUpperCase();
      String firstName2 = ((CMSCustomer)obj2).getFirstName().toUpperCase();

      if (!(lastName1.equals(lastName2)))
        return lastName1.compareTo(lastName2);
      else
        return firstName1.compareTo(firstName2);
    }
  }
}

