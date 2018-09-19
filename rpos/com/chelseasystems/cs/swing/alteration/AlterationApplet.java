/*
 * put your module comment here
 * formatted with JxBeauty (c) johann.langhofer@nextra.at
 */


package com.chelseasystems.cs.swing.alteration;

import com.chelseasystems.cr.item.*;
import com.chelseasystems.cr.config.*;
import com.chelseasystems.cr.swing.*;
import com.chelseasystems.cr.swing.bean.*;
import com.chelseasystems.cs.swing.panel.AlterationListPanel;
import com.chelseasystems.cs.pos.*;
import com.chelseasystems.cr.pos.*;
import com.chelseasystems.cs.item.CMSItem;
import com.chelseasystems.cs.employee.*;
import com.chelseasystems.cs.store.CMSStore;
import com.chelseasystems.cs.swing.panel.*;
import com.chelseasystems.cr.swing.event.CMSActionEvent;
import com.chelseasystems.cs.swing.menu.MenuConst;
import com.chelseasystems.cs.util.AlterationLookUpUtil;
import com.chelseasystems.cs.dataaccess.artsoracle.dao.ArtsConstants;
import com.chelseasystems.cr.swing.layout.*;
import com.chelseasystems.cr.currency.ArmCurrency;
import javax.swing.*;
import java.awt.event.*;
import java.awt.*;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeEvent;
import java.util.ArrayList;
import java.util.Date;
import java.text.SimpleDateFormat;
import com.chelseasystems.cs.swing.mask.CMSMaskConstants;
import java.util.Calendar;
import com.chelseasystems.cs.pos.CMSCompositePOSTransaction;
import com.chelseasystems.cr.rules.BusinessRuleException;
import com.chelseasystems.cs.swing.bean.ArmJCMSTable;
import com.chelseasystems.cs.item.CMSItemHelper;
import com.ga.fs.fsbridge.ARMFSBridge;
import java.text.*;


/**
 * <p>Title: AlterationApplet</p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2005</p>
 * <p>Company: SkillNet Inc</p>
 * @author Manpreet S Bawa
 * @version 1.0
 */
/*
 History:
 +------+------------+-----------+-----------+----------------------------------------------------+
 | Ver# | Date       | By        | Defect #  | Description                                        |
 +------+------------+-----------+-----------+----------------------------------------------------+
 | 1    | 04-21-2005 | Manpreet  | N/A       | POS_104665_TS_Alterations_Rev2                     |
 +------+------------+-----------+-----------+----------------------------------------------------+
 | 2    | 05-02-2005 | Manpreet  | N/A       | Changed mapping of MiscItem to Key rather than Id  |
 +------+------------+-----------+-----------+----------------------------------------------------+
 | 3    | 05-14-2005 | Manpreet  | N/A       | Implemented JobCodes for filtering Fitter & Tailor |
 +------+------------+-----------+-----------+----------------------------------------------------+
 | 4    | 07-12-2005 | Vikram    | 519       | System allowed bogus dates for try and promise date|
 +------+------------+-----------+-----------+----------------------------------------------------+
 | 5    | 07-21-2005 | Megha     | 642       | Implemented new flow for the new alteration request|
 +------+------------+-----------+-----------+----------------------------------------------------+
 | 6    | 08/23/2005 | Vikram    | 626       | 'Space' and 'Enter' handling for Panels with Tables|
 |      |            |           |           | with Checkbox.                                     |
 +------+------------+-----------+-----------+----------------------------------------------------+
 | 7    | 09/13/2005 | Manpreet  | 508       | Implementing Alterations on Presale Line items.    |
 +------+------------+-----------+-----------+----------------------------------------------------+
 */
public class AlterationApplet extends CMSApplet implements PropertyChangeListener {
  /**
   * AlterationList
   */
  private AlterationListPanel alterationListPanel;
  /**
   * Header Panel
   */
  private AlterationHeaderPanel pnlHeader;
  /**
   * LineItem
   */
  private POSLineItem posLineItem;
  /**
   * CompositPosTransaction
   */
  private CMSCompositePOSTransaction theTxn;
  /**
   * Comments
   */
  private JCMSTextArea txtComments;
  /**
   * Comments label
   */
  private JCMSLabel lblComments;
  /**
   * Subtotal
   */
  private JCMSLabel lblSubTotal;
  /**
   * Subtotal value
   */
  private JCMSLabel lblSubTotalValue;
  /**
   * AlterationLookup
   */
  private AlterationLookUpUtil alterationLookUp;
  /**
   * Panel to hold AlterationList, HeaderPanel and Footer panel
   */
  private JPanel pnlAlteration;
  /**
   * Cardlayout
   */
  private RolodexLayout cardLayout;
  /**
   * Fitters list
   */
  private EmpListPanel pnlEmpFitters;
  /**
   * Tailors list
   */
  private EmpListPanel pnlEmpTailors;
  /**
   * Selected AlterationLineItemDetail
   */
  private AlterationLineItemDetail selectedAlterationLineItmDetail;
  /**
   * If modifying alteration.
   */
  private boolean bEdittingAlteration = false;
  /**
   * Date format.
   */
  private final SimpleDateFormat dateFormat = com.chelseasystems.cs.util.DateFormatUtil.getLocalDateFormat();
  /**
   * For storing Try date so that it can be compared with Promise Date.
   */
  private String tryDate = new String();

  /**
   * Init
   */
  public void init() {
    try {
      jbInit();
      dateFormat.setLenient(false);
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  /**
   * Start
   */
  public void start() {
    bEdittingAlteration = false;
    selectedAlterationLineItmDetail = null;
    alterationLookUp = new AlterationLookUpUtil();
    theTxn = (CMSCompositePOSTransaction)theAppMgr.getStateObject("TXN_POS");
    // If PosLineItem was passed from
    // initial sale applet
    if (theAppMgr.getStateObject("POS_LINE_ITEM") != null) {
      posLineItem = (POSLineItem)theAppMgr.getStateObject("POS_LINE_ITEM");
      theAppMgr.removeStateObject("POS_LINE_ITEM");
      if (!(posLineItem instanceof SaleLineItem || posLineItem instanceof CMSPresaleLineItem)) { //MSB - 09/13/05 -- Preasale Alteration
        returnBackToApplet();
        return;
      }
      reset();
      initHeaderPanel((CMSItem)posLineItem.getItem());
//      String classId = ((CMSItem)posLineItem.getItem()).getClassId();
//      if (posLineItem.isMiscItem()
//          && posLineItem.getItem().getId().equals(new ConfigMgr("item.cfg").
//          getString("NOTONFILE.BASE_ITEM"))) //make sure item is not not-on-file
//        classId = LineItemPOSUtil.getNotOnFileItemClass(posLineItem);
      if (!populateAlterationDetails()) {
        returnBackToApplet();
        return;
      }
      // If an existing alteration has to be loaded
      // for modification.
      if (theAppMgr.getStateObject("ARM_SELECT_ALTERATION_ID") != null) {
        SwingUtilities.invokeLater(new Runnable() {

          /**
           * put your documentation comment here
           */
          public void run() {
            // -MSB 01/26/2006
            // Casting would throw exception in case of PreSaleLine items.
            // amd lineItem wasnt being used in the method,so directly called
            // the other method.
//            findAlterationLineDetailByID((CMSSaleLineItem)posLineItem
//                , (String)theAppMgr.getStateObject("ARM_SELECT_ALTERATION_ID"));
            findAlterationLineDetailByID((String)theAppMgr.getStateObject("ARM_SELECT_ALTERATION_ID"));
            theAppMgr.removeStateObject("ARM_SELECT_ALTERATION_ID");
            if (selectedAlterationLineItmDetail != null) {
              pnlHeader.setTotalPrice(selectedAlterationLineItmDetail.getTotalPrice().
                  formattedStringValue());
              initMainBtns();
            }
          }
        });
      }
      // If a new alteration
      // Retrieve the alterationId passed from
      // previous screen.
      else if (theAppMgr.getStateObject("ARM_ALTERATION_ID") != null) {
        SwingUtilities.invokeLater(new Runnable() {

          /**
           * put your documentation comment here
           */
          public void run() {
            pnlHeader.setAlterationID((String)theAppMgr.getStateObject("ARM_ALTERATION_ID"));
            theAppMgr.removeStateObject("ARM_ALTERATION_ID");
            populateFitterScreen();
          }
        });
      }
    } else {
      returnBackToApplet();
      return;
    }
    //initMainBtns();
    // First populate the Fitter Screen.
  }

  /**
   * Return back to previous applet
   */
  private void returnBackToApplet() {
    // Swing utilities is needed
    // since applet is not fully loaded
    // and goBack would take you 2 steps back.
    SwingUtilities.invokeLater(new Runnable() {

      /**
       * put your documentation comment here
       */
      public void run() {
        SwingUtilities.invokeLater(new Runnable() {

          /**
           * put your documentation comment here
           */
          public void run() {
            theAppMgr.showErrorDlg(res.getString("Alteration not allowed on this item"));
            theAppMgr.goBack();
          }
        });
      }
    });
  }

  /**
   * put your documentation comment here
   */
  void populateFitterScreen() {
    if (pnlEmpFitters == null) {
      if (populateAllFitters()) {
        theAppMgr.showMenu(MenuConst.CANCEL_ONLY, "FITTER_ID", theOpr);
        cardLayout.show(this.getContentPane(), "FITTERS_LIST");
        enterFitter();
      } else {
        initNewFlow();
      }
    } else {
      theAppMgr.showMenu(MenuConst.CANCEL_ONLY, "FITTER_ID", theOpr);
      cardLayout.show(this, "FITTERS_LIST");
      enterFitter();
    }
  }

  /**
   * AppButton Event.
   * @param anEvent CMSActionEvent
   */
  public void appButtonEvent(CMSActionEvent anEvent) {
    String sCommand = anEvent.getActionCommand();
    if (sCommand.equals("FITTER_ID")) {
      if (pnlEmpFitters == null) {
        if (populateAllFitters()) {
          theAppMgr.showMenu(MenuConst.CANCEL_ONLY, "FITTER_ID", theOpr);
          cardLayout.show(this.getContentPane(), "FITTERS_LIST");
          enterFitter();
        }
      } else {
        theAppMgr.showMenu(MenuConst.CANCEL_ONLY, "FITTER_ID", theOpr);
        cardLayout.show(this, "FITTERS_LIST");
        enterFitter();
      }
    } else if (sCommand.equals("TAILOR_ID")) {
      if (pnlEmpTailors == null) {
        if (populateAllTailors()) {
          theAppMgr.showMenu(MenuConst.CANCEL_ONLY, "TAILOR_ID", theOpr);
          cardLayout.show(this.getContentPane(), "TAILORS_LIST");
          enterTailor();
        }
      } else {
        theAppMgr.showMenu(MenuConst.CANCEL_ONLY, "TAILOR_ID", theOpr);
        cardLayout.show(this.getContentPane(), "TAILORS_LIST");
        enterTailor();
      }
    } else if (sCommand.equals("COMMENTS")) {
      enterComments();
    } else if (sCommand.equals("CLEAR_COMMENTS")) {
      clearComments();
    } else if (sCommand.equals("OK")) {
      if (completeAttributes()) {
        applyAlterations();
        theAppMgr.goBack();
      }
    } else if (sCommand.equals("CANCEL")) {
      theAppMgr.goBack();
    }
  }

  /**
   * Apply alterations to lineItemDetails.
   */
  private void applyAlterations() {
    try {
      AlterationDetail[] alterations = alterationListPanel.getSelectedAlterationDetails();
      if (alterations == null) {
        return;
      }
      if (!(posLineItem instanceof CMSSaleLineItem || posLineItem instanceof CMSPresaleLineItem)) { //MSB - 09/13/05 -- Preasale Alteration
        return;
      }
      //MSB - 09/13/05 -- Preasale Alteration
      //      CMSSaleLineItemDetail cmsSaleLineItmDetail = (CMSSaleLineItemDetail)
      //          posLineItem.getLineItemDetailsArray()[0];
      // If modifying an alteration item.
      // Clean all the details and
      // make a new one.
      if (selectedAlterationLineItmDetail != null) {
        selectedAlterationLineItmDetail.clearLineItemDetails();
      } else {
        selectedAlterationLineItmDetail = new AlterationLineItemDetail();
      }
      selectedAlterationLineItmDetail.setPriceOverride(!pnlHeader.isAutoPriceUpdateAllowed());
      selectedAlterationLineItmDetail.setFitterID(pnlHeader.getFitterID());
      selectedAlterationLineItmDetail.setAlterationTicketID(pnlHeader.getAlterationID());
      selectedAlterationLineItmDetail.setTailorID(pnlHeader.getTailorID());
      selectedAlterationLineItmDetail.setTotalPrice(getCurrencyFromString(pnlHeader.getTotalPrice()));
      selectedAlterationLineItmDetail.setPromiseDate(getDateFromString(pnlHeader.getPromiseDate()));
      selectedAlterationLineItmDetail.setTryDate(getDateFromString(pnlHeader.getTryDate()));
      selectedAlterationLineItmDetail.setComments(txtComments.getText());
      for (int iCtr = 0; iCtr < alterations.length; iCtr++) {
        if (alterations[iCtr] == null) {
          continue;
        }
        selectedAlterationLineItmDetail.addAlterationDetail(alterations[iCtr]);
      }
      if (!bEdittingAlteration) {
        //MSB - 09/13/05 -- Preasale Alteration
        if (posLineItem.getLineItemDetailsArray()[0] instanceof CMSPresaleLineItemDetail) {
          ((CMSPresaleLineItemDetail)posLineItem.getLineItemDetailsArray()[0]).
              addAlterationLineItemDetail(selectedAlterationLineItmDetail);
        } else if (posLineItem.getLineItemDetailsArray()[0] instanceof CMSSaleLineItemDetail) {
          ((CMSSaleLineItemDetail)posLineItem.getLineItemDetailsArray()[0]).
              addAlterationLineItemDetail(selectedAlterationLineItmDetail);
        }
        //        cmsSaleLineItmDetail.addAlterationLineItemDetail(
        //            selectedAlterationLineItmDetail);
        applyAlterationToTxn(getCurrencyFromString(pnlHeader.getTotalPrice())
            , posLineItem.getItemDescription());
      }
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  /**
   * Apply Alteration item to transaction
   *  -- add it as Misc. Item.
   * @param itemPrice Currency
   * @param sItemDesc String
   */
  private void applyAlterationToTxn(ArmCurrency itemPrice, String sItemDesc) {
    CMSItem item = null;
    CMSMiscItem miscItem = null;
    MiscItemTemplate alterationMiscItemTemplate = null;
    ConfigMgr config = new ConfigMgr("item.cfg");
    String itemId = config.getString("ALTERATION.BASE_ITEM");
    MiscItemTemplate[] miscItemTemplate = MiscItemManager.getInstance().getMiscItemsArray(itemId);
    if (miscItemTemplate != null && miscItemTemplate.length > 0) {
      for (int i = 0; i < miscItemTemplate.length; i++) {
        if (miscItemTemplate[i].getKey().trim().equals("ALTERATION")) {
          alterationMiscItemTemplate = miscItemTemplate[i];
          break;
        } else {
          continue;
        }
      }
      try {
        try {
          item = CMSItemHelper.findByBarCode(theAppMgr, alterationMiscItemTemplate.getBaseItemId()
              , theStore.getId());
        } catch (Exception e) {
          //e.printStackTrace();
        }
        if (item == null) {
          item = new CMSItem(alterationMiscItemTemplate.getBaseItemId());
          item.setBarCode(item.getBarCode());
          item.setDescription(alterationMiscItemTemplate.getMiscItemDescription());
        }
      } catch (Exception ex) {
        theAppMgr.showErrorDlg(ex.getMessage());
      }
      if (item != null) {
        try {
          sItemDesc += " Alteration";
          miscItem = new CMSMiscItem(alterationMiscItemTemplate.getKey(), item);
          try {
            applyMiscItemTemplate(alterationMiscItemTemplate, miscItem);
          } catch (Exception e) {
            e.printStackTrace();
          }
          //miscItem.setDefaultQuantity(new Integer(1));
          miscItem.setDescription(sItemDesc);
          if (itemPrice == null) {
            itemPrice = new ArmCurrency(0.0d);
          }
          miscItem.setUnitPrice(itemPrice);
          // MSB 09/13/05 -- PresaleLineItem alteration
          if (posLineItem instanceof CMSPresaleLineItem) {
            CMSPresaleLineItem miscSaleLineItem = (CMSPresaleLineItem) theTxn.addPresaleMiscItem(miscItem);
            miscSaleLineItem.setAlterationLineItem( (CMSPresaleLineItem) posLineItem);
          }
          else if(posLineItem instanceof CMSSaleLineItem){
            CMSSaleLineItem miscSaleLineItem =  (CMSSaleLineItem)theTxn.addSaleMiscItem(miscItem);
            miscSaleLineItem.setAlterationLineItem((CMSSaleLineItem)posLineItem);
          }
        } catch (Exception e) {
          e.printStackTrace();
        }
      } else {
        theAppMgr.showErrorDlg(res.getString("Cannot find item") + " "
            + alterationMiscItemTemplate.getBaseItemId());
      }
    }
  }

  /**
   * Lookup AlterationLineDetail by AlterationTicketId
   * @param cmsSaleLineItem CMSSaleLineItem
   * @param sAlterationID String
   */
  private void findAlterationLineDetailByID(CMSSaleLineItem cmsSaleLineItem, String sAlterationID) {
    //    POSLineItemDetail details[] = cmsSaleLineItem.getLineItemDetailsArray();
    //
    //    for (int iCtr = 0; iCtr < details.length; iCtr++) {
    //      CMSSaleLineItemDetail cmsSaleLineItemDetail = (CMSSaleLineItemDetail)
    //          details[iCtr];
    //      AlterationLineItemDetail alterationLineDetails[] = cmsSaleLineItemDetail.
    //          getAlterationLineItemDetailArray();
    //      if (alterationLineDetails == null) {
    //        continue;
    //      }
    //
    //      for (int altCtr = 0; altCtr < alterationLineDetails.length; altCtr++) {
    //        if (alterationLineDetails[altCtr] == null) {
    //          continue;
    //        }
    //
    //        if (alterationLineDetails[altCtr].getAlterationTicketID().
    //            equalsIgnoreCase(sAlterationID)) {
    //          selectedAlterationLineItmDetail = alterationLineDetails[altCtr];
    //          loadSelectedAlteration(alterationLineDetails[altCtr]);
    //          return;
    //        }
    //      }
    //    }
    //MSB - 09/13/05 -- To implement Alteration on Presale line items.
    findAlterationLineDetailByID(sAlterationID);
  }

  /**
   * put your documentation comment here
   * @param sAlterationID
   */
  private void findAlterationLineDetailByID(String sAlterationID) {
    POSLineItemDetail details[] = posLineItem.getLineItemDetailsArray();
    for (int iCtr = 0; iCtr < details.length; iCtr++) {
      AlterationLineItemDetail alterationLineDetails[] = null;
      if (details[iCtr] instanceof CMSPresaleLineItemDetail) {
        alterationLineDetails = ((CMSPresaleLineItemDetail)details[iCtr]).
            getAlterationLineItemDetailArray();
      } else if (details[iCtr] instanceof CMSSaleLineItemDetail) {
        alterationLineDetails = ((CMSSaleLineItemDetail)details[iCtr]).
            getAlterationLineItemDetailArray();
      }
      if (alterationLineDetails == null) {
        continue;
      }
      for (int altCtr = 0; altCtr < alterationLineDetails.length; altCtr++) {
        if (alterationLineDetails[altCtr] == null) {
          continue;
        }
        if (alterationLineDetails[altCtr].getAlterationTicketID().equalsIgnoreCase(sAlterationID)) {
          selectedAlterationLineItmDetail = alterationLineDetails[altCtr];
          loadSelectedAlteration(alterationLineDetails[altCtr]);
          return;
        }
      }
    }
  }

  /**
   * Load values for selected alteration
   * @param alterationLineItemDetail AlterationLineItemDetail
   */
  private void loadSelectedAlteration(AlterationLineItemDetail alterationLineItemDetail) {
    AlterationDetail alterationDetails[] = alterationLineItemDetail.getAlterationDetailsArray();
    if (alterationDetails == null) {
      return;
    }
    pnlHeader.setAlterationID(alterationLineItemDetail.getAlterationTicketID());
    pnlHeader.setTryDate(getFormattedDateString(alterationLineItemDetail.getTryDate()));
    pnlHeader.setPromiseDate(getFormattedDateString(alterationLineItemDetail.getPromiseDate()));
    pnlHeader.setFitterID(alterationLineItemDetail.getFitterID());
    pnlHeader.setTailorID(alterationLineItemDetail.getTailorID());
    for (int iAltDetail = 0; iAltDetail < alterationDetails.length; iAltDetail++) {
      if (alterationDetails[iAltDetail] == null) {
        continue;
      }
      if (alterationDetails[iAltDetail].getAlterationCode() == null) {
        continue;
      }
      alterationListPanel.selectByAlterationDetailInList(alterationDetails[iAltDetail]);
    }
    setComments(alterationLineItemDetail.getComments());
    setSubTotal(alterationListPanel.getSubTotal().formattedStringValue());
    bEdittingAlteration = true;
  }

  /**
   * Convert String to currency.
   * This is done to remove the $ sign
   * and using Double.doubleValue to get
   * accurate value.
   * @param sValue String
   * @return Currency
   */
  private ArmCurrency getCurrencyFromString(String sValue) {
    if (sValue == null || sValue.length() < 1) {
      return null;
    }
    return new ArmCurrency(sValue);
  }

  /**
   * Parsing date from string
   * @param sValue String
   * @return Date
   */
  private Date getDateFromString(String sValue) {
    Date dtTmp = null;
    if (sValue == null || sValue.length() < 1) {
      return null;
    }
    try {
      dtTmp = dateFormat.parse(sValue);
    } catch (Exception e) {
      e.printStackTrace();
    }
    return dtTmp;
  }

  /**
   *
   * @param date Date
   * @return String
   */
  private String getFormattedDateString(Date date) {
    return dateFormat.format(date);
  }

  /**
   * If all the mandatory fields are done.
   * @return boolean
   */
  private boolean completeAttributes() {
    int iTmp = -1;
    if (pnlHeader.getAlterationID().length() < 1) {
      theAppMgr.showErrorDlg(res.getString("Alteration ID can't be blank"));
      pnlHeader.requestFocusTo(pnlHeader.ALTERATION_ID);
      return false;
    }
    if (pnlHeader.getTryDate().length() < 1) {
      theAppMgr.showErrorDlg(res.getString("Try date needed to proceed"));
      pnlHeader.requestFocusTo(pnlHeader.TRY_DATE);
      return false;
    }
    if (pnlHeader.getTryDate().length() > 1) {
      iTmp = isValidTryDate(pnlHeader.getTryDate());
      if (iTmp != -1) {
        if (iTmp == 1) {
          theAppMgr.showErrorDlg(res.getString("Enter a valid date"));
        } else if (iTmp == 2) {
          theAppMgr.showErrorDlg(res.getString("Try Date can't be prior to Today's Date"));
        }
        pnlHeader.requestFocusTo(pnlHeader.TRY_DATE);
        return false;
      }
    }
    if (pnlHeader.getPromiseDate().length() < 1) {
      theAppMgr.showErrorDlg(res.getString("Promise date needed to proceed"));
      pnlHeader.requestFocusTo(pnlHeader.PROMISE_DATE);
      return false;
    }
    if (pnlHeader.getPromiseDate().length() > 1) {
      iTmp = isValidPromiseDate(pnlHeader.getTryDate(), pnlHeader.getPromiseDate());
      if (iTmp != -1) {
        if (iTmp == 1) {
          theAppMgr.showErrorDlg(res.getString("Enter a valid date"));
        } else if (iTmp == 2) {
          theAppMgr.showErrorDlg(res.getString("Promise Date can't be prior to Try Date"));
        }
        pnlHeader.requestFocusTo(pnlHeader.PROMISE_DATE);
        return false;
      }
    }
    return true;
  }

  /**
   * See if date entered is valid
   * @param sDate String
   * @return boolean
   */
  private int isValidTryDate(String sDate) {
    try {
      Date tryDate = dateFormat.parse(sDate);
      Date currDate = new Date();
      String tryDateStrVal = (dateFormat.format(tryDate)).trim();
      String currDateStrVal = (dateFormat.format(currDate)).trim();
      if (!(tryDateStrVal.equals(currDateStrVal))) {
        if (tryDate.before(new Date())) {
          return 2;
        }
      }
    } catch (Exception e) {
      return 1;
    }
    return -1;
  }

  /**
   * See if date entered is valid
   * @param sDate String
   * @return boolean
   */
  private int isValidPromiseDate(String sDate, String promiseDateVal) {
    try {
      Date tryDate = dateFormat.parse(sDate);
      Date promiseDate = dateFormat.parse(promiseDateVal);
      String tryDateStrVal = (dateFormat.format(tryDate)).trim();
      String promiseDateStrVal = (dateFormat.format(promiseDate)).trim();
      if (!(tryDateStrVal.equals(promiseDateStrVal))) {
        if (promiseDate.before(tryDate)) {
          return 2;
        }
      }
    } catch (Exception e) {
      return 1;
    }
    return -1;
  }

  /**
   * Edit Area Event
   * @param command
   * @param date
   */
  public void editAreaEvent(String command, Date date) {
    if (command.equals("TRY_DATE")) {
      String dateString = dateFormat.format(date);
      this.tryDate = dateString;
      if (alterationListPanel.getTable() instanceof ArmJCMSTable)
        ((ArmJCMSTable)alterationListPanel.getTable()).enableSpaceCapture(false);
      if (isCorrectTryDate(tryDate)) {
        pnlHeader.setTryDate(dateString);
        pnlHeader.clearPromiseDate();
        theAppMgr.setSingleEditArea(res.getString(
            "Enter Promise Date (MM/DD/YYYY)  or  \'C\' for Calendar."), "PROMISE_DATE"
            , getDefaultPromiseDate(), CMSMaskConstants.CALENDAR_MASK);
      } else {
        theAppMgr.setSingleEditArea(res.getString(
            "Enter Try Date(MM/DD/YYYY)  or  \'C\' for Calendar."), "TRY_DATE", new Date()
            , CMSMaskConstants.CALENDAR_MASK);
      }
    }
    if (command.equals("PROMISE_DATE")) {
      String dateString = dateFormat.format(date);
      if (isCorrectPromiseDateFormat(tryDate, dateString)) {
        pnlHeader.setPromiseDate(dateString);
        if (alterationListPanel.getTable() instanceof ArmJCMSTable)
          ((ArmJCMSTable)alterationListPanel.getTable()).enableSpaceCapture(true);
        enterSelect();
      } else {
        if (alterationListPanel.getTable() instanceof ArmJCMSTable)
          ((ArmJCMSTable)alterationListPanel.getTable()).enableSpaceCapture(false);
        pnlHeader.clearPromiseDate();
        theAppMgr.setSingleEditArea(res.getString(
            "Enter Promise Date (MM/DD/YYYY)  or  \'C\' for Calendar."), "PROMISE_DATE"
            , getDefaultPromiseDate(), CMSMaskConstants.CALENDAR_MASK);
      }
    }
  }

  /**
   * Edit area event.
   * @param sCommand String
   * @param sEdit String
   */
  public void editAreaEvent(String sCommand, String sEdit) {
    if (sCommand.equals("COMMENTS")) {
      setComments(sEdit);
      enterSelect();
    } else if (sCommand.equals("ALTERATION_CODE")) {
      if (sEdit == null || sEdit.trim().length() == 0)
        alterationListPanel.toggleRowSelection();
      else if (!alterationListPanel.selectByAlterationCodeInPage(sEdit.trim()))
        theAppMgr.showErrorDlg(res.getString("Alteration Code not found"));
      else
        setSubTotal(alterationListPanel.getSubTotal().formattedStringValue());
      enterSelect();
    } else if (sCommand.equals("FITTER")) {
      CMSEmployee empSelected = null;
      if (sEdit == null || sEdit.trim().length() == 0)
        empSelected = (CMSEmployee)pnlEmpFitters.getSelectedEmployee();
      else
        empSelected = findEmployeeByExternalID(sEdit.trim());
      if (isAFitter(empSelected))
        applyFitter(empSelected);
      else {
        theAppMgr.showErrorDlg(res.getString("Fitter not found"));
        enterFitter();
      }
    } else if (sCommand.equals("TAILOR")) {
      CMSEmployee empSelected = null;
      if (sEdit == null || sEdit.trim().length() == 0)
        empSelected = (CMSEmployee)pnlEmpTailors.getSelectedEmployee();
      else
        empSelected = findEmployeeByExternalID(sEdit.trim());
      if (isATailor(empSelected))
        applyTailor(empSelected);
      else {
        theAppMgr.showErrorDlg(res.getString("Tailor not found"));
        enterFitter();
      }
    }
  }

  /**
   * Lookup an employee by external id.
   * @param sExtID String
   * @return CMSEmployee
   */
  private CMSEmployee findEmployeeByExternalID(String sExtID) {
    try {
      return CMSEmployeeHelper.findByExternalId(theAppMgr, sExtID);
    } catch (Exception e) {}
    return null;
  }

  /**
   * Check if Employee is a Fitter
   * @param employee CMSEmployee
   * @return boolean
   */
  private boolean isAFitter(CMSEmployee employee) {
    if (employee == null) {
      return false;
    }
    return (employee.getJobCodeId().equals(ArtsConstants.JC_FITTER_ID)
        || employee.getJobCodeId().equals(ArtsConstants.JC_STORE_ASSOCIATE_ID)
        || employee.getJobCodeId().equals(ArtsConstants.JC_MANAGER_ID)
        || employee.getJobCodeId().equals(ArtsConstants.JC_CASHIER_ID))
        && employee.isEmploymentStatusActive();
  }

  /**
   * Check if Employee is a Tailor
   * @param employee CMSEmployee
   * @return boolean
   */
  private boolean isATailor(CMSEmployee employee) {
    if (employee == null) {
      return false;
    }
    return (employee.getJobCodeId().equals(ArtsConstants.JC_TAILOR_ID))
        && employee.isEmploymentStatusActive();
  }

  /**
   * AppButton event.
   * @param sHeader String
   * @param anEvent CMSActionEvent
   */
  public void appButtonEvent(String sHeader, CMSActionEvent anEvent) {
    String sCommand = anEvent.getActionCommand();
    //MP: If Tailor selection cancelled , go back to original flow.
    if (sCommand.equals("CANCEL") && (sHeader.equals("TAILOR_ID"))) {
      initMainBtns();
      cardLayout.show(this, "ALTERATION_LIST");
    }
    // If Fitter Selection cancelled, go back to new flow
    else if (sCommand.equals("CANCEL") && (sHeader.equals("FITTER_ID"))) {
      initNewFlow();
      cardLayout.show(this, "ALTERATION_LIST");
    }
  }

  /**
   * Reset the applet
   */
  private void reset() {
    pnlHeader.reset();
    alterationListPanel.clear();
    lblSubTotalValue.setText("");
    txtComments.setText("");
  }

  /**
   * Initialize header panel
   * @param cmsItem CMSItem
   */
  private void initHeaderPanel(CMSItem cmsItem) {
    alterationListPanel.clear();
    if (posLineItem.isMiscItem()) {
      return;
    }
    pnlHeader.setSku(cmsItem.getId());
    pnlHeader.setFabric(cmsItem.getFabric());
    pnlHeader.setStyle(cmsItem.getStyleNum());
    pnlHeader.setColor(cmsItem.getColorId() + "-" + cmsItem.getItemDetail().getColorDesc());
    pnlHeader.setModel(cmsItem.getModel());
    pnlHeader.setSupplier(cmsItem.getItemDetail().getSupplierName());
  }

  /**
   * Populate all the alteration details.
   * @param sClassID String
   * @return boolean
   */
  private boolean populateAlterationDetails() {
    String subGroupId = (String)theAppMgr.getStateObject("ALTERATION_SUB_GROUP_ID");
    theAppMgr.removeStateObject("ALTERATION_SUB_GROUP_ID");
    if (subGroupId == null || subGroupId.length() < 1) {
      return false;
    }
    try {
      alterationListPanel.clear();
      AlterationItemGroup alterationObj = null;
      alterationObj = alterationLookUp.findBySubGroupId(subGroupId);
      if (alterationObj == null) {
        return false;
      }
      AlterationDetail alterationDetails[] = alterationObj.getAlterationDetailsArray();
      if (alterationDetails == null) {
        return false;
      }
      for (int iCtr = 0; iCtr < alterationDetails.length; iCtr++) {
        if (alterationDetails[iCtr] == null) {
          continue;
        }
        alterationListPanel.addAlterationDetail(alterationDetails[iCtr]);
      }
      alterationListPanel.selectRow(0, false);
    } catch (Exception e) {
      e.printStackTrace();
      return false;
    }
    return true;
  }

  /**
   * Version
   * @return String
   */
  public String getVersion() {
    return ("$Revision: 1.1 $");
  }

  /**
   * ScreenName
   * @return String
   */
  public String getScreenName() {
    return (res.getString("Alterations"));
  }

  /**
   * Comments
   * @param sComments String
   */
  public void setComments(String sComments) {
    if (sComments == null || sComments.trim().length() < 1) {
      return;
    }
    txtComments.setText(sComments);
  }

  /**
   * Clear comments
   */
  public void clearComments() {
    txtComments.setText("");
  }

  /**
   * Set subtotal
   * @param sValue String
   */
  public void setSubTotal(String sValue) {
    if (sValue == null || sValue.trim().length() < 1) {
      return;
    }
    lblSubTotalValue.setText(sValue);
  }

  /**
   * PageDown
   * @param e MouseEvent
   */
  public void pageDown(MouseEvent e) {
    Component pnlCurrent = cardLayout.getCurrent(getContentPane());
    if (pnlCurrent.equals(pnlAlteration)) {
      alterationListPanel.nextPage();
      theAppMgr.showPageNumber(e
          , ((PageNumberGetter)alterationListPanel).getCurrentPageNumber() + 1
          , ((PageNumberGetter)alterationListPanel).getTotalPages());
    } else if (pnlCurrent.equals(pnlEmpFitters)) {
      pnlEmpFitters.nextPage();
      theAppMgr.showPageNumber(e, ((PageNumberGetter)pnlEmpFitters).getCurrentPageNumber() + 1
          , ((PageNumberGetter)pnlEmpFitters).getTotalPages());
    } else if (pnlCurrent.equals(pnlEmpTailors)) {
      pnlEmpTailors.nextPage();
      theAppMgr.showPageNumber(e, ((PageNumberGetter)pnlEmpTailors).getCurrentPageNumber() + 1
          , ((PageNumberGetter)pnlEmpTailors).getTotalPages());
    }
  }

  /**
   * PageUp
   * @param e MouseEvent
   */
  public void pageUp(MouseEvent e) {
    Component pnlCurrent = cardLayout.getCurrent(getContentPane());
    if (pnlCurrent.equals(pnlAlteration)) {
      alterationListPanel.prevPage();
      theAppMgr.showPageNumber(e
          , ((PageNumberGetter)alterationListPanel).getCurrentPageNumber() + 1
          , ((PageNumberGetter)alterationListPanel).getTotalPages());
    } else if (pnlCurrent.equals(pnlEmpFitters)) {
      pnlEmpFitters.prevPage();
      theAppMgr.showPageNumber(e, ((PageNumberGetter)pnlEmpFitters).getCurrentPageNumber() + 1
          , ((PageNumberGetter)pnlEmpFitters).getTotalPages());
    } else if (pnlCurrent.equals(pnlEmpTailors)) {
      pnlEmpTailors.prevPage();
      theAppMgr.showPageNumber(e, ((PageNumberGetter)pnlEmpTailors).getCurrentPageNumber() + 1
          , ((PageNumberGetter)pnlEmpTailors).getTotalPages());
    }
  }

  /**
   * Initialize components
   * @throws Exception
   */
  public void jbInit()
      throws Exception {
    pnlAlteration = new JPanel();
    JPanel pnlComments = new JPanel();
    JPanel pnlFooter = new JPanel();
    JPanel pnlTotal = new JPanel();
    alterationListPanel = new AlterationListPanel();
    pnlHeader = new AlterationHeaderPanel();
    txtComments = new JCMSTextArea();
    txtComments.setEnabled(false);
    lblComments = new JCMSLabel(res.getString("Comments"));
    lblComments.setHorizontalAlignment(SwingConstants.LEFT);
    lblSubTotal = new JCMSLabel(res.getString("Subtotal")+":");
    lblSubTotalValue = new JCMSLabel("");
    pnlTotal.setLayout(new BorderLayout());
    pnlFooter.setLayout(new BorderLayout());
    pnlComments.setLayout(new BorderLayout());
    pnlTotal.setLayout(new BorderLayout());
    lblSubTotal.setPreferredSize(new Dimension(580, 30));
    lblSubTotalValue.setPreferredSize(new Dimension(65, 20));
    pnlFooter.setPreferredSize(new Dimension(833, 70));
    pnlTotal.setPreferredSize(new Dimension(833, 20));
    pnlComments.setPreferredSize(new Dimension(833, 50));
    pnlHeader.setPreferredSize(new Dimension(833, 130));
    txtComments.setPreferredSize(new Dimension(833, 25));
    lblComments.setPreferredSize(new Dimension(833, 20));
    lblSubTotal.setHorizontalAlignment(SwingConstants.RIGHT);
    lblSubTotalValue.setHorizontalAlignment(SwingConstants.LEFT);
    pnlTotal.add(lblSubTotal, BorderLayout.WEST);
    pnlTotal.add(lblSubTotalValue, BorderLayout.EAST);
    pnlFooter.add(pnlComments, BorderLayout.CENTER);
    pnlFooter.add(pnlTotal, BorderLayout.SOUTH);
    pnlComments.add(lblComments, BorderLayout.NORTH);
    pnlComments.add(txtComments, BorderLayout.CENTER);
    pnlAlteration.setLayout(new BorderLayout());
    pnlAlteration.setName("ALTERATION_LIST");
    pnlAlteration.add(alterationListPanel, BorderLayout.CENTER);
    pnlAlteration.add(pnlHeader, BorderLayout.NORTH);
    pnlAlteration.add(pnlFooter, BorderLayout.SOUTH);
    cardLayout = new RolodexLayout();
    this.getContentPane().setLayout(cardLayout);
    this.getContentPane().add(pnlAlteration, "ALTERATION_LIST");
    lblSubTotalValue.setAppMgr(theAppMgr);
    lblSubTotal.setAppMgr(theAppMgr);
    lblComments.setAppMgr(theAppMgr);
    txtComments.setAppMgr(theAppMgr);
    alterationListPanel.setAppMgr(theAppMgr);
    pnlHeader.setAppMgr(theAppMgr);
    pnlComments.setBackground(theAppMgr.getBackgroundColor());
    pnlTotal.setBackground(theAppMgr.getBackgroundColor());
    pnlFooter.setBackground(theAppMgr.getBackgroundColor());
    lblComments.setFont(theAppMgr.getTheme().getTextFieldFont());
    lblSubTotal.setFont(theAppMgr.getTheme().getTextFieldFont());
    lblSubTotalValue.setFont(theAppMgr.getTheme().getTextFieldFont());
    alterationListPanel.getSubTotalLbl().addPropertyChangeListener(this);
    pnlHeader.getPromiseDateTxt().setVerifier(new Verifier());
    pnlHeader.getTryDateTxt().setVerifier(new Verifier());
  }

  /**
   * Intialize MainScreen buttons
   */
  private void initMainBtns() {
    theAppMgr.showMenu(MenuConst.ALTERATIONS, theOpr);
    enterSelect();
  }

  /**
   * Enter main screen select
   */
  private void enterSelect() {
    theAppMgr.setSingleEditArea(res.getString("Select or enter alteration code"), "ALTERATION_CODE");
  }

  /**
   * Enter comments
   */
  private void enterComments() {
    theAppMgr.setSingleEditArea(res.getString("Type comment and press enter"), "COMMENTS"
        , theAppMgr.NO_MASK);
  }

  /**
   * Enter fitter
   */
  private void enterFitter() {
    theAppMgr.setSingleEditArea(res.getString("Select or enter Fitter ID."), "FITTER");
  }

  /**
   * Enter tailor
   */
  private void enterTailor() {
    theAppMgr.setSingleEditArea(res.getString("Select or enter tailor ID."), "TAILOR");
  }

  /**
   * PropertyChange Event
   * This event captures subtotal value
   * A hidden label from AlterationListPanel
   * is used to trap the change.
   * On Selection/De-selection /Editing of a row
   * set text of hidden label is called
   * which fires this event and updates the price
   *
   * @param pce PropertyChangeEvent
   */
  public void propertyChange(PropertyChangeEvent pce) {
    // Get updated subtotal from the table.
    // and update the label
    if (pce.getSource() instanceof JLabel) {
      setSubTotal((String)pce.getNewValue());
      // If alteration isnt being edited
      // and total price hasnt been overriden yet.
      if (pnlHeader.isAutoPriceUpdateAllowed() && !bEdittingAlteration) {
        pnlHeader.setTotalPrice((String)pce.getNewValue());
        return;
      }
      // If alteration is being edited
      // and total price wasnt overriden last use.
      if (bEdittingAlteration && (!selectedAlterationLineItmDetail.isPriceOverriden())) {
        pnlHeader.setTotalPrice((String)pce.getNewValue());
      }
    }
  }

  /**
   * Load all the Fitters into panel
   * @return boolean
   */
  private boolean populateAllFitters() {
    String sColumnIdentifiers[] = {res.getString("ID"), res.getString("First Name")
        , res.getString("Last Name")
    };
    pnlEmpFitters = new EmpListPanel(sColumnIdentifiers);
    pnlEmpFitters.setAppMgr(theAppMgr);
    try {
      CMSEmployee[] empFitters = CMSEmployeeHelper.findByStore(theAppMgr, (CMSStore)theStore);
      empFitters = filterEmployees(empFitters, true);
      if (empFitters == null || empFitters.length < 1) {
        theAppMgr.showErrorDlg(res.getString("No Fitters found for this store"));
        pnlEmpFitters = null;
        return false;
      }
      pnlEmpFitters.setEmployees(empFitters);
      this.getContentPane().add(pnlEmpFitters, "FITTERS_LIST");
      pnlEmpFitters.addMouseListener(new MouseAdapter() {

        /**
         * put your documentation comment here
         * @param e
         */
        public void mouseClicked(MouseEvent e) {
          applyFitter((CMSEmployee)pnlEmpFitters.getSelectedEmployee());
        }
      });
      return true;
    } catch (Exception e) {
      e.printStackTrace();
    }
    pnlEmpFitters = null;
    return false;
  }

  /**
   * Load all the tailors
   * @return boolean
   */
  private boolean populateAllTailors() {
    String sColumnIdentifiers[] = {res.getString("ID"), res.getString("First Name")
        , res.getString("Last Name")
    };
    pnlEmpTailors = new EmpListPanel(sColumnIdentifiers);
    pnlEmpTailors.setAppMgr(theAppMgr);
    try {
      CMSEmployee[] empTailors = CMSEmployeeHelper.findByStore(theAppMgr, (CMSStore)theStore);
      empTailors = filterEmployees(empTailors, false);
      if (empTailors == null || empTailors.length < 1) {
        theAppMgr.showErrorDlg(res.getString("No Tailors found for this store"));
        pnlEmpTailors = null;
        return false;
      }
      pnlEmpTailors.setEmployees(empTailors);
      this.getContentPane().add(pnlEmpTailors, "TAILORS_LIST");
      pnlEmpTailors.addMouseListener(new MouseAdapter() {

        /**
         * put your documentation comment here
         * @param e
         */
        public void mouseClicked(MouseEvent e) {
          applyTailor((CMSEmployee)pnlEmpTailors.getSelectedEmployee());
        }
      });
      return true;
    } catch (Exception e) {
      e.printStackTrace();
    }
    pnlEmpTailors = null;
    return false;
  }

  /**
   * Filter employees
   * @param empStore CMSEmployee[]
   * @param bFilterFitters boolean
   * @return CMSEmployee[]
   */
  private CMSEmployee[] filterEmployees(CMSEmployee[] empStore, boolean bFilterFitters) {
    ArrayList empList = new ArrayList();
    try {
      for (int iCtr = 0; iCtr < empStore.length; iCtr++) {
        CMSEmployee cmsEmployee = empStore[iCtr];
        if (bFilterFitters) { // Filter Fitters
          if (isAFitter(cmsEmployee)) {
            empList.add(cmsEmployee);
          }
        } else if (!bFilterFitters) { // Filter Tailors
          if (isATailor(cmsEmployee)) {
            empList.add(cmsEmployee);
          }
        }
      }
    } catch (Exception e) {}
    return (CMSEmployee[])empList.toArray(new CMSEmployee[empList.size()]);
  }

  /**
   * Apply selected Fitter
   * @param empSelected CMSEmployee
   */
  private void applyFitter(CMSEmployee empSelected) {
    if (empSelected == null) {
      return;
    }
    pnlHeader.setFitterID(empSelected.getExternalID());
    cardLayout.show(getContentPane(), "ALTERATION_LIST");
    // Once the fitter is selected we need to prompt for Try Date.
    initNewFlow();
  }

  /**
   * Apply selected Tailor
   * @param empSelected CMSEmployee
   */
  private void applyTailor(CMSEmployee empSelected) {
    if (empSelected == null) {
      return;
    }
    pnlHeader.setTailorID(empSelected.getExternalID());
    cardLayout.show(getContentPane(), "ALTERATION_LIST");
    initMainBtns();
  }

  /**
   * Verifier for Date
   * <p>Title: Verifier</p>
   * <p>Description: Verifier for Date</p>
   * <p>Copyright: Copyright (c) 2005</p>
   * <p>Company: SkillNet Inc</p>
   * @author Manpreet S Bawa
   * @version 1.0
   */
  private class Verifier extends CMSInputVerifier {

    /**
     * Verify if input is valid
     * @param comp JComponent
     * @return boolean
     */
    public boolean verify(JComponent comp) {
      JCMSTextField txt = (JCMSTextField)comp;
      String sTxt = txt.getText().trim();
      if (sTxt.length() == 0 && txt.getName().equals("TryDate")) {
        pnlHeader.clearPromiseDate();
      }
      if (sTxt.length() > 0) {
        try {
          if (sTxt.length() != 10) {
            throw new Exception("not valid date");
          }
          Date dtTxt = dateFormat.parse(sTxt);
          // The Try Date isn't equal or greater than the Current Date = Today's date.
          if (txt.getName().equals("TryDate")) {
            ConfigMgr config = new ConfigMgr("alteration.cfg");
            String dateRemaining = config.getString("PROMISE_DATE_AFTER_TRY_DATE_DAYS");
            Integer dateAdded = new Integer(dateRemaining);
            Date tryDate = dateFormat.parse(pnlHeader.getTryDate());
            Calendar rightNow = Calendar.getInstance();
            rightNow.setTime(tryDate);
            rightNow.add(Calendar.DAY_OF_YEAR, dateAdded.intValue());
            pnlHeader.setPromiseDate((dateFormat.format(rightNow.getTime())).trim());
            String tryDateStrVal = sTxt;
            String currDateStrVal = (dateFormat.format(new Date())).trim();
            if (!tryDateStrVal.equals(currDateStrVal)) {
              if (dtTxt.before(new Date())) {
                theAppMgr.showErrorDlg(res.getString("Enter a future Date"));
                pnlHeader.requestFocusTo(pnlHeader.TRY_DATE);
                return false;
              }
            }
          }
          // The Promise Date isn't equal or greater than the Try Date .
          else if (txt.getName().equals("PromiseDate")) {
            if (!isCorrectPromiseDateFormat(pnlHeader.getTryDate(), pnlHeader.getPromiseDate())) {
              pnlHeader.requestFocusTo(pnlHeader.PROMISE_DATE);
              return false;
            }
            //            else {
            //              pnlHeader.requestFocusTo(pnlHeader.TOTAL_PRICE);
            //              return true;
            //            }
          }
          return true;
        } catch (Exception e) {
          theAppMgr.showErrorDlg(res.getString("Enter a valid date"));
          if (txt.getName().equals("PromiseDate")) {
            pnlHeader.requestFocusTo(pnlHeader.PROMISE_DATE);
          } else if (txt.getName().equals("TryDate")) {
            pnlHeader.requestFocusTo(pnlHeader.TRY_DATE);
          }
          return false;
        }
      }
      return true;
    }
  }


  /**
   * Stop
   */
  public void stop() {}

  /**
   * put your documentation comment here
   */
  private void enterDate() {
    if (alterationListPanel.getTable() instanceof ArmJCMSTable)
      ((ArmJCMSTable)alterationListPanel.getTable()).enableSpaceCapture(false);
    theAppMgr.setSingleEditArea(res.getString(
        "Enter Try Date (MM/DD/YYYY)  or  \'C\' for Calendar."), "TRY_DATE", new Date()
        , CMSMaskConstants.CALENDAR_MASK);
    String dateString = dateFormat.format(new Date());
    theAppMgr.setEditAreaFocus();
  }

  /**
   * If no fitter present, the editAreaEvent asks to enter Try Date.
   */
  public void initNewFlow() {
    theAppMgr.showMenu(MenuConst.ALTERATIONS, theOpr);
    enterDate();
    theAppMgr.setEditAreaFocus();
  }

  /**
   * If the Promise Date is correct, used for EditAreaEvent
   * @param tryDateStr String
   * @param promiseDateStr String
   * @return boolean
   */
  public boolean isCorrectPromiseDateFormat(String tryDateStr, String promiseDateStr) {
    if (tryDateStr.trim().length() > 0) {
      Date tryDate = null;
      Date promiseDate = null;
      try {
        tryDate = dateFormat.parse(tryDateStr);
        promiseDate = dateFormat.parse(promiseDateStr);
      } catch (ParseException ex) {
        theAppMgr.showErrorDlg(res.getString("Invalid date. Please provide valid date in mm/dd/yyyy format."));
        return false;
      }
      String tryDateStrVal = (dateFormat.format(tryDate)).trim();
      String promiseDateStrVal = (dateFormat.format(promiseDate)).trim();
      if (!tryDateStrVal.equals(promiseDateStrVal)) {
        if (tryDate.after(promiseDate)) {
          CMSApplet.theAppMgr.showErrorDlg(res.getString("Promise Date can't be prior to Try Date"));
          return false;
        }
      }
    }
    return true;
  }

  /**
   * If the Try Date is correct , used for editAreaEvent
   * @param tryDateStr String
   * @return boolean
   */
  public boolean isCorrectTryDate(String tryDateStr) {
    Date tryDate = null;
    try {
      tryDate = dateFormat.parse(tryDateStr);
    } catch (ParseException ex) {
      theAppMgr.showErrorDlg(res.getString("Invalid date. Please provide valid date in mm/dd/yyyy format."));
      return false;
    }
    Date currDate = new Date();
    String tryDateStrVal = (dateFormat.format(tryDate)).trim();
    String currDateStrVal = (dateFormat.format(currDate)).trim();
    if (!(tryDateStrVal.equals(currDateStrVal))) {
      if (tryDate.before(currDate)) {
        CMSApplet.theAppMgr.showErrorDlg(res.getString("Enter a Future Try Date"));
        return false;
      }
    }
    return true;
  }

  /**
   * Gets the default Promise Date
   * @return Date
   */
  public Date getDefaultPromiseDate() {
    ConfigMgr config = new ConfigMgr("alteration.cfg");
    String dateRemaining = config.getString("PROMISE_DATE_AFTER_TRY_DATE_DAYS");
    Integer dateAdded = new Integer(dateRemaining);
    Date tryDate = null;
    try {
      tryDate = dateFormat.parse(pnlHeader.getTryDate());
    } catch (ParseException ex) {
      theAppMgr.showErrorDlg(res.getString("Invalid date. Please provide valid date in mm/dd/yyyy format."));
      return null;
    }
    Calendar rightNow = Calendar.getInstance();
    rightNow.setTime(tryDate);
    rightNow.add(Calendar.DAY_OF_YEAR, dateAdded.intValue());
    return rightNow.getTime();
  }

  /**
   * MP: Home pressed at customer display exits transaction with no message
   * @return
   */
  public boolean isHomeAllowed() {
    CMSCompositePOSTransaction cmsTxn = (CMSCompositePOSTransaction)theAppMgr.getStateObject(
        "TXN_POS");
    if (cmsTxn == null) {
      return (true);
    }
  
    /*Added by Yves Agbessi (05-Dec-2017)
	 * Handles the posting of the Sign Off event for Fiscal Solutions Service
	 * START--
	 * */
	boolean goToHomeView = (theAppMgr.showOptionDlg(res.getString("Cancel Transaction")
	        , res.getString("Are you sure you want to cancel this transaction?")));
	if(goToHomeView){
		
		ARMFSBridge.postARMSignOffTransaction((CMSEmployee)theOpr);
	}
	
	return goToHomeView;
	
	/*
	 * --END
	 * */
  }

  /**
   *
   * @exception BusinessRuleException
   */
  private void applyMiscItemTemplate(MiscItemTemplate alterationMiscItemTemplate
      , CMSMiscItem miscItem)
      throws BusinessRuleException {
    if (!alterationMiscItemTemplate.getCanOverrideAmount())
      miscItem.setUnitPrice(alterationMiscItemTemplate.getAmount());
    if (!alterationMiscItemTemplate.getCanOverrideTaxable())
      miscItem.setTaxable(new Boolean(alterationMiscItemTemplate.getTaxable()));
    if (!alterationMiscItemTemplate.getCanOverrideDescription()) {
      String[] descriptions = alterationMiscItemTemplate.getDescription();
      if (descriptions != null && descriptions.length > 0
          && alterationMiscItemTemplate.getDescIdx() > -1)
        miscItem.setDescription(descriptions[alterationMiscItemTemplate.getDescIdx()]);
    }
    if (!alterationMiscItemTemplate.getCanOverrideComment())
      miscItem.setComment(alterationMiscItemTemplate.getComment());
    miscItem.setGLAccount(alterationMiscItemTemplate.getGLaccount());
    miscItem.setDefaultQuantity(new Integer(alterationMiscItemTemplate.getDefaultQty()));
  }

  /**
   * put your documentation comment here
   * @param mouseevent
   */
  public void clickEvent(MouseEvent mouseevent) {
    if (mouseevent == null)
      mouseevent = new MouseEvent(this, MouseEvent.MOUSE_CLICKED, System.currentTimeMillis(), 0, 0
          , 0, 1, false);
    Component pnlCurrent = cardLayout.getCurrent(getContentPane());
    if (pnlCurrent == pnlAlteration) {
      JCMSTable table = alterationListPanel.getTable();
      table.dispatchEvent(mouseevent);
      table.setRowSelectionInterval(table.getSelectionModel().getAnchorSelectionIndex()
          , table.getSelectionModel().getLeadSelectionIndex());
    } else if (pnlCurrent == pnlEmpFitters) {
      pnlEmpFitters.getTable().dispatchEvent(mouseevent);
    } else if (pnlCurrent == pnlEmpTailors) {
      pnlEmpTailors.getTable().dispatchEvent(mouseevent);
    }
  }
}

