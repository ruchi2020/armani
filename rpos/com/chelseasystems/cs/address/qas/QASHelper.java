package com.chelseasystems.cs.address.qas;

import java.util.ResourceBundle;
import com.chelseasystems.cr.appmgr.IApplicationManager;
import com.chelseasystems.cr.appmgr.IRepositoryManager;
import com.chelseasystems.cr.config.ConfigMgr;
import com.chelseasystems.cr.swing.CMSApplet;
import com.chelseasystems.cr.util.ResourceManager;
import com.chelseasystems.cs.address.Address;
import com.chelseasystems.cs.address.CMSAddressVerifyHelper;
import com.chelseasystems.cs.swing.dlg.GenericChooserRow;
import com.chelseasystems.cs.swing.dlg.QASAddressDlgBox;
import com.chelseasystems.cs.swing.dlg.RefineAddressDlg;
import com.chelseasystems.cs.swing.dlg.VerifyLevelDlg;
import com.qas.proweb.FormattedAddress;
import com.qas.proweb.PicklistItem;
import com.qas.proweb.SearchResult;


/*
History:
+------+------------+-----------+-----------+----------------------------------------------------+
| Ver# | Date       | By        | Defect #  | Description                                        |
+------+------------+-----------+-----------+----------------------------------------------------+
| 1    | 04-04-2006 |David Fung| PCR 67     | QAS                                                |
+------+------------+-----------+-----------+----------------------------------------------------+
*/

/**
* <p>Title:QASHelper.java </p>
*
* <p>Description: QASHelper </p>
*
* <p>Copyright: Copyright (c) 2005</p>
*
* <p>Company: Skillnet inc.</p>
*
* @author David Fung
* @version 1.0
*/

public class QASHelper {
  public static boolean useQAS = false;
  static String countryList = "USA";
  static ResourceBundle res = ResourceManager.getResourceBundle();
  static {
    ConfigMgr config = new ConfigMgr("addressverify.cfg");
    useQAS = config.getString("USE_QAS").equals("true");
    countryList = config.getString("QAS_COUNTRY");
  }

  // Call this method from Applet.
  public static Address verifyAddress(IRepositoryManager theMgr, Address address) {
    try {
      // Fix for 1568: QAS: For an existing customer, RPOS is not making a
      // call-out
      // to QAS and therefore addresses are not checked.
      if (!QASHelper.useQAS || QASHelper.countryList.indexOf(address.getCountry()) == -1) {
        return address;
      }
      // System.out.println("QASHelper.verifyAddress-->IN\n" + address);
      String dataId = address.getCountry();
      String[] userInput = QASUtil.getInputForSearch(address);
      Address oldAddress = new Address();
      oldAddress.setAddressLine1(address.getAddressLine1());
      oldAddress.setAddressLine2(address.getAddressLine2());
      oldAddress.setCity(address.getCity());
      oldAddress.setState(address.getState());
      oldAddress.setZipCode(address.getZipCode());
      oldAddress.setZipCodeExtension(address.getZipCodeExtension());
      String verifyLevel = "";
      String textEntered = "";
      String addressText = "";
      QASRequest request = new QASRequest(dataId, userInput);
      QASResponse response = (QASResponse)CMSAddressVerifyHelper.search(theMgr, request);
      if (response == null || response.isNoMatchFound()) {
        System.out.println("QASHelper.verifyAddress-->NoMatchFound!");
        verifyLevel = "None";
      } else {
        verifyLevel = response.getSearchResultVerifyLevel();
        System.out.println("VERIFY LEVEL: " + verifyLevel);
        if (verifyLevel.equals(SearchResult.Verified)) {
          if (response.getVerifiedAddress() != null) {
            System.out.println("QASHelper.verifyAddress-->Verified!");
            address = QASUtil.fixAddress(address, response.getVerifiedAddress(), verifyLevel);
          }
        } else if (verifyLevel.equals(SearchResult.InteractionRequired)) {
          if (response.getVerifiedAddress() != null) {
            address = QASUtil.fixAddress(address, response.getVerifiedAddress(), verifyLevel);
            StringBuffer sbAddrText = new StringBuffer();
            sbAddrText.append(address.getAddressLine1());
            sbAddrText.append(" ");
            if (address.getAddressLine2() != null) {
              sbAddrText.append(address.getAddressLine2());
              sbAddrText.append(" ");
            }
            sbAddrText.append(address.getCity());
            sbAddrText.append(" ");
            sbAddrText.append(address.getState());
            addressText = sbAddrText.toString();
          }
          textEntered = loadVerifyLevelDlg(verifyLevel
              , res.getString("The following address is recommended:"), addressText);
          if (textEntered != null && textEntered.length() == 0) {
            // When CANCEL is pressed in the dialog box
            if (theMgr instanceof IApplicationManager) {
              ((IApplicationManager)theMgr).showErrorDlg("QAS: Please rectify address");
            }
            return oldAddress;
          }
          // Construct user input
          userInput = QASUtil.getInputForSearch(address);
          // Call search
          address = search(theMgr, address, dataId, userInput);
        } else if (verifyLevel.equals(SearchResult.StreetPartial)) {
          textEntered = loadVerifyLevelDlg(verifyLevel, res.getString("Enter street number")
              , addressText);
          // Construct user input
          address.setAddressLine1(textEntered + " " + address.getAddressLine1());
          userInput = QASUtil.getInputForSearch(address);
          // Call search
          address = search(theMgr, address, dataId, userInput);
        } else if (verifyLevel.equals(SearchResult.PremisesPartial)) {
          textEntered = loadVerifyLevelDlg(verifyLevel
              , res.getString("Enter apartment/suite/unit number"), addressText);
          // Construct user input
          address.setAddressLine2(textEntered + " " + address.getAddressLine2());
          userInput = QASUtil.getInputForSearch(address);
          // Call search
          address = search(theMgr, address, dataId, userInput);
        } else if (verifyLevel.equals(SearchResult.Multiple)) {
          PicklistItem pickedItem = QASHelper.loadPicklistDlg(response);
          if (pickedItem != null) {
            // System.out.println("QASHelper.Picked ~~~>>> " +
            // pickedItem.getText());
            FormattedAddress formattedAddress = response.getFormattedAddress(pickedItem.getMoniker());
            if (formattedAddress != null) {
              address = QASUtil.fixAddress(address, formattedAddress, verifyLevel);
            } else { // to refine
              String refinementText = loadRefineAddressDlg(pickedItem.getText());
              if (refinementText != null) {
                QASRequest refinementRequest = new QASRequest(response, pickedItem, refinementText);
                QASResponse refinementResponse = (QASResponse)CMSAddressVerifyHelper.search(theMgr
                    , refinementRequest);
                if (refinementResponse != null && refinementResponse.getRefinementAddress() != null) {
                  FormattedAddress formattedAddressForRefinement = refinementResponse.
                      getRefinementAddress();
                  if (formattedAddressForRefinement != null) {
                    address = QASUtil.fixAddress(address, formattedAddressForRefinement
                        , verifyLevel);
                  }
                }
              } // else not found return original address
            }
          }
        }
      }
    } catch (Exception exception) {
      if (theMgr instanceof IApplicationManager) {
        ((IApplicationManager)theMgr).showErrorDlg("QAS Error:" + exception.getMessage());
      }
      // System.out.println("Exception --> " + exception);
      exception.printStackTrace();
      return address;
    }
    // System.out.println("QASHelper.verifyAddress-->OUT\n" + address);
    return address;
  }

  /**
   * @param response
   * @return
   */
  private static PicklistItem loadPicklistDlg(QASResponse response) {
    PicklistItem[] items = response.getPicklistItems();
    GenericChooserRow[] availMiscItemTemplates = new GenericChooserRow[items.length];
    for (int i = 0; i < availMiscItemTemplates.length; i++) {
      String[] displayRow = new String[] {
          items[i].getText()
      };
      Object rowKeyData = items[i];
      availMiscItemTemplates[i] = new GenericChooserRow(displayRow, rowKeyData);
    }
    String[] columnTitles = new String[] {
        "Address List"
    }; // res.getString("Address
    // List") };
    QASAddressDlgBox dlg = new QASAddressDlgBox(CMSApplet.theAppMgr.getParentFrame()
        , CMSApplet.theAppMgr, availMiscItemTemplates, columnTitles);
    dlg.getTable().getColumnModel().getColumn(0).setCellRenderer(QASAddressDlgBox.getCenterRenderer());
    dlg.setVisible(true);
    if (dlg.isOK()) {
      return (PicklistItem)dlg.getSelectedRow().getRowKeyData();
    } else {
      return null;
    }
  }

  /**
   * @param addressToRefine
   * @return
   */
  private static String loadRefineAddressDlg(String addressToRefine) {
    RefineAddressDlg dlg = new RefineAddressDlg(CMSApplet.theAppMgr.getParentFrame()
        , CMSApplet.theAppMgr, addressToRefine);
    dlg.setVisible(true);
    if (dlg.isOK()) {
      String text = dlg.getAddressText().trim();
      if (text.endsWith("[odd]")) {
        return text.substring(0, text.length() - 5);
      } else if (text.endsWith("[even]")) {
        return text.substring(0, text.length() - 6);
      } else {
        return text.trim();
      }
    } else {
      return null;
    }
  }

  /**
   * @param title
   * @param label
   * @param addressText
   * @return
   */
  private static String loadVerifyLevelDlg(String title, String label, String addressText) {
    VerifyLevelDlg dlg = new VerifyLevelDlg(CMSApplet.theAppMgr.getParentFrame()
        , CMSApplet.theAppMgr, title, label, addressText);
    dlg.setVisible(true);
    if (dlg.isOK()) {
      String text = dlg.getAddressText().trim();
      return text.trim();
    } else {
      return "";
    }
  }

  /**
   * @param theMgr
   * @param address
   * @param dataId
   * @param userInput
   * @return
   */
  private static Address search(IRepositoryManager theMgr, Address address, String dataId
      , String[] userInput) {
    try {
      QASRequest request = new QASRequest(dataId, userInput);
      QASResponse response = (QASResponse)CMSAddressVerifyHelper.search(theMgr, request);
      String verifyLevel = response.getSearchResultVerifyLevel();
      if (response != null) {
        if (response.getVerifiedAddress() != null) {
          address = QASUtil.fixAddress(address, response.getVerifiedAddress(), verifyLevel);
          address.setQasVerifyLevel(response.getSearchResultVerifyLevel());
        } else if (response.getPicklistItems() != null) {
          PicklistItem pickedItem = QASHelper.loadPicklistDlg(response);
          if (pickedItem != null) {
            System.out.println("QASHelper.Picked ~~~>>> " + pickedItem.getText());
            FormattedAddress formattedAddress = response.getFormattedAddress(pickedItem.getMoniker());
            if (formattedAddress != null) {
              verifyLevel = SearchResult.Verified;
              address = QASUtil.fixAddress(address, formattedAddress, verifyLevel);
            } else { // to refine
              String refinementText = loadRefineAddressDlg(pickedItem.getText());
              System.out.println("QASHelper.RefinementText ~~~>>> " + refinementText);
              if (refinementText != null) {
                QASRequest refinementRequest = new QASRequest(response, pickedItem, refinementText);
                QASResponse refinementResponse = (QASResponse)CMSAddressVerifyHelper.search(theMgr
                    , refinementRequest);
                if (refinementResponse != null && refinementResponse.getRefinementAddress() != null) {
                  FormattedAddress formattedAddressForRefinement = refinementResponse.
                      getRefinementAddress();
                  System.out.println("QASHelper.RefinementText Verify Level~~~>>> "
                      + refinementResponse.getSearchResultVerifyLevel());
                  if (formattedAddressForRefinement != null) {
                    if (refinementResponse.getSearchResultVerifyLevel() == null)
                      verifyLevel = SearchResult.None;
                    else
                      verifyLevel = refinementResponse.getSearchResultVerifyLevel();
                    address = QASUtil.fixAddress(address, formattedAddressForRefinement
                        , verifyLevel);
                  }
                }
              } // else not found return original address
            }
          }
        }
      }
    } catch (Exception exception) {
      if (theMgr instanceof IApplicationManager) {
        ((IApplicationManager)theMgr).showErrorDlg("QAS Error:" + exception.getMessage());
      }
      exception.printStackTrace();
      return address;
    }
    return address;
  }
}