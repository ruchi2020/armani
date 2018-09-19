/*
 History:
 +------+------------+-----------+-----------+---------------------------------------------+
 | Ver# | Date       | By        | Defect #  | Description                                 |
 +------+------------+-----------+-----------+---------------------------------------------+
 |  1   | 09-12-2005 |  Vikram   |   986     | New builder to handle Not-On-File Items.    |
 +------+------------+-----------+-----------+---------------------------------------------+
 *
 * formatted with JxBeauty (c) johann.langhofer@nextra.at
 */


package com.chelseasystems.cs.swing.builder;

import com.chelseasystems.cr.appmgr.*;
import com.chelseasystems.cr.currency.ArmCurrency;
import com.chelseasystems.cr.currency.CurrencyException;
import com.chelseasystems.cr.item.*;
import com.chelseasystems.cr.rules.BusinessRuleException;
import com.chelseasystems.cr.swing.CMSApplet;
import com.chelseasystems.cs.item.*;
import com.chelseasystems.cs.pos.*;
import com.chelseasystems.cs.store.CMSStore;
import com.chelseasystems.cs.util.LineItemPOSUtil;


/**
 */
public class NotOnFileItemBldr implements IObjectBuilder {
  private CMSItem item;
  private CMSMiscItem miscItem;
  private CMSItemWrapper itemWrapper;
  private MiscItemTemplate notOnFileMiscItemTemplate;
  private IObjectBuilderManager theBldrMgr;
  private CMSApplet applet;
  private IApplicationManager theAppMgr;
  private String buildCommand;
  private String itemId;
  private String itemDept;
  private String itemClass;
  private boolean processedTaxable = false;

  /**
   * @param theBldrMgr
   * @param theAppMgr
   */
  public void init(IObjectBuilderManager theBldrMgr, IApplicationManager theAppMgr) {
    this.theBldrMgr = theBldrMgr;
    this.theAppMgr = theAppMgr;
  }

  /**
   */
  public void cleanup() {
    notOnFileMiscItemTemplate = null;
    miscItem = null;
    processedTaxable = false;
    itemId = null;
    itemDept = null;
    itemClass = null;
  }

  /**
   * @param theCommand
   * @param theEvent
   */
  public void EditAreaEvent(String theCommand, Object theEvent) {
    if (theCommand.equals("ITEM")) {
      if(((String)theEvent).length() > 34)
        theAppMgr.showErrorDlg(applet.res.getString("Misc Item Barcode/SKU is too long"));
      else
      itemId = (String)theEvent;
    } else if (theCommand.equals("DEPT")) {
      if(((String)theEvent).length() > 20)
        theAppMgr.showErrorDlg(applet.res.getString("Item Department is too long"));
      else
      itemDept = (String)theEvent;
    } else if (theCommand.equals("CLASS")) {
      if(((String)theEvent).length() > 20)
        theAppMgr.showErrorDlg(applet.res.getString("Item Class is too long"));
      else
      itemClass = (String)theEvent;
    } else if (theCommand.equals("PRICE")) {
	//Anjana added 05-22-2017 to prevent precision issue causing broken txns
      try {
    	  if(((ArmCurrency)theEvent).greaterThanOrEqualTo(new ArmCurrency(999999.99))){
    		  theAppMgr.showErrorDlg(applet.res.getString("Item Price is too long"));
    	  }
    	  else
        miscItem.setUnitPrice((ArmCurrency)theEvent);
      } catch (BusinessRuleException ex) {
        ex.printStackTrace();
      } catch (CurrencyException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
    } else if (theCommand.equals("TAXABLE")) {
      String answer = ((String)theEvent).toUpperCase();
      if (answer.equals(applet.res.getString("Y")) || answer.equals(applet.res.getString("N"))) {
        try {
          miscItem.setTaxable(new Boolean(answer.equals(applet.res.getString("Y"))));
          notOnFileMiscItemTemplate.setCanOverrideTaxable(false);
          processedTaxable = true;
        } catch (BusinessRuleException ex) {
          theAppMgr.showErrorDlg(applet.res.getString(ex.getMessage()));
        }
      } else {
        theAppMgr.showErrorDlg(applet.res.getString("Please respond \"Y\" or \"N\"."));
        theAppMgr.setSingleEditArea(applet.res.getString("Is the item taxable (\"Y\" or \"N\")")
            , "TAXABLE"
            , notOnFileMiscItemTemplate.getTaxable() ? applet.res.getString("Y")
            : applet.res.getString("N"));
      }
    }
    processBuild();
  }

  /**
   * Look up the CMSItem by item id and pass it back to applet when all
   * attributes are complete.
   * @param Command
   * @param applet
   * @param initValue
   */
  public void build(String command, CMSApplet applet, Object initValue) {
    this.buildCommand = command;
    this.applet = applet;
    if (initValue instanceof MiscItemTemplate) {
      //            build(Command, applet, (MiscItemTemplate)initValue);
      notOnFileMiscItemTemplate = (MiscItemTemplate)initValue;
      //            return;
    } else {
      MiscItemTemplate[] miscItemTemplates = MiscItemManager.getInstance().getMiscItemsArray();
      for (int i = 0; i < miscItemTemplates.length; i++) {
        if (LineItemPOSUtil.isNotOnFileItem(miscItemTemplates[i].getBaseItemId())) {
          notOnFileMiscItemTemplate = miscItemTemplates[i];
          break;
        }
      }
    }
    processBuild();
  }

  /**
   * @return whether item was found and is complete.
   */
  private boolean completeAttributes()
      throws Exception {
    if (miscItem == null) {
      miscItem = this.createNotOnFileItem();
      if (miscItem == null) return true;
//        throw new BusinessRuleException("ERROR: while creating Misc Item.");
    }
    if (itemId == null || itemId.trim().length() == 0) {
      theAppMgr.setSingleEditArea(applet.res.getString("Scan/Enter Barcode/SKU"), "ITEM"
          , theAppMgr.REQUIRED_MASK);
      return false;
    }
    if (itemDept == null || itemDept.trim().length() == 0) {
      theAppMgr.setSingleEditArea(applet.res.getString("Enter Item Department"), "DEPT"
          , theAppMgr.REQUIRED_MASK);
      return false;
    }
    if (itemClass == null || itemClass.trim().length() == 0) {
      theAppMgr.setSingleEditArea(applet.res.getString("Enter Item Class"), "CLASS"
          , theAppMgr.REQUIRED_MASK);
      return false;
    }
    if (miscItem.getUnitPrice() == null) {
      theAppMgr.setSingleEditArea(applet.res.getString("Enter Item Unit Price"), "PRICE"
          , theAppMgr.CURRENCY_MASK);
      return false;
    }
    if (!processedTaxable) {
      String initValue = notOnFileMiscItemTemplate.getTaxable() ? applet.res.getString("Y")
          : applet.res.getString("N");
      theAppMgr.setSingleEditArea(applet.res.getString("Is the item taxable (\"Y\" or \"N\")")
          , "TAXABLE", initValue, theAppMgr.REQUIRED_MASK);
      return false;
    }
    return true;
  } // completeAttributes

  /**
   * put your documentation comment here
   * @return
   * @exception Exception
   */
  private CMSMiscItem createNotOnFileItem()
      throws Exception {
    miscItem = null;
    if (notOnFileMiscItemTemplate != null) {
      String storeId = ((CMSStore)theAppMgr.getGlobalObject("STORE")).getId();
      item = CMSItemHelper.findByBarCode(theAppMgr, notOnFileMiscItemTemplate.getBaseItemId()
          , storeId);
      if (item == null)
      {
          theAppMgr.showErrorDlg(applet.res.getString("Item not found"));
          theBldrMgr.processObject(applet, buildCommand, miscItem, this);
          return null;
      }
      miscItem = new CMSMiscItem(notOnFileMiscItemTemplate.getKey(), item);
      //___Tim
      itemWrapper = new CMSItemWrapper();
      itemWrapper.setMiscItem(miscItem);
      //            miscItem = new CMSMiscItem(notOnFileMiscItemTemplate.getBaseItemId(), null);
      if (!notOnFileMiscItemTemplate.getCanOverrideAmount())
        miscItem.setUnitPrice(notOnFileMiscItemTemplate.getAmount());
      if (!notOnFileMiscItemTemplate.getCanOverrideTaxable())
        miscItem.setTaxable(new Boolean(notOnFileMiscItemTemplate.getTaxable()));
      if (!notOnFileMiscItemTemplate.getCanOverrideDescription()) {
        String[] descriptions = notOnFileMiscItemTemplate.getDescription();
        if (descriptions != null && descriptions.length > 0
            && notOnFileMiscItemTemplate.getDescIdx() > -1)
          miscItem.setDescription(descriptions[notOnFileMiscItemTemplate.getDescIdx()]);
      }
      if (!notOnFileMiscItemTemplate.getCanOverrideComment())
        miscItem.setComment(notOnFileMiscItemTemplate.getComment());
      miscItem.setGLAccount(notOnFileMiscItemTemplate.getGLaccount());
      miscItem.setDefaultQuantity(new Integer(notOnFileMiscItemTemplate.getDefaultQty()));
    }
    return miscItem;
  }

  /**
   * put your documentation comment here
   */
  private void processBuild() {
    try {
      if (completeAttributes()) {
        if (miscItem != null)
        {
          miscItem.setComment(itemId + "|" + itemClass + "|" + itemDept);
          theBldrMgr.processObject(applet, buildCommand, itemWrapper, this);
        }
      }
    } catch (Exception ex1) {
      ex1.printStackTrace();
      theBldrMgr.processObject(applet, buildCommand, null, this);
    }
  }
} // ItemBldr

