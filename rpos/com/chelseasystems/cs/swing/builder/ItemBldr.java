package com.chelseasystems.cs.swing.builder;

import com.chelseasystems.cr.appmgr.*;
import com.chelseasystems.cr.currency.ArmCurrency;
import com.chelseasystems.cr.item.*;
import com.chelseasystems.cr.payment.Redeemable;
import com.chelseasystems.cr.pos.*;
import com.chelseasystems.cr.rules.BusinessRuleException;
import com.chelseasystems.cr.swing.CMSApplet;
import com.chelseasystems.cs.item.*;
import com.chelseasystems.cs.payment.CMSRedeemableHelper;
import com.chelseasystems.cs.pos.*;
import com.chelseasystems.cs.swing.dlg.*;
import com.chelseasystems.cs.swing.pos.InitialSaleApplet;
import com.chelseasystems.cs.swing.menu.MenuConst;
import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import java.util.Enumeration;
import com.chelseasystems.cs.store.CMSStore;
import java.util.Vector;
import com.chelseasystems.cr.config.ConfigMgr;
import java.util.StringTokenizer;
import com.chelseasystems.cs.customer.CMSCustomer;
import com.chelseasystems.cs.util.Version;
import java.util.HashMap;

/**
 */
public class ItemBldr implements IObjectBuilder {
	private CMSItem item;
	private CMSSpecificItem spcItem;
	private CMSMiscItem miscItem;
	private CMSItemWrapper itemWrapper;
	private MiscItemTemplate miscItemTemplate;
	private IObjectBuilderManager theBldrMgr;
	private CMSApplet applet;
	private String storeId;
	private IApplicationManager theAppMgr;
	private boolean searchMode;
	private String command;
	private static DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
	private CMSStore cmsStore;
	private static Vector openDepositTypes;
	private static Vector closeDepositTypes;
	private ArmCurrency custDepositBalance;
	private String extendedBarCode = "";
  //Vivek Mishra : Added for Extended Barcode CR Europe 06-OCT-2016
private String isExtendedStagingBarcode = "N";
  //Ends here 06-OCT-2016
	private boolean isBasketItem = false;

	// init
	static {
		centerRenderer.setHorizontalAlignment(SwingConstants.CENTER);
		initDepositTypes();
	}

	/**
	 * @param theBldrMgr
	 * @param theAppMgr
	 */
	public void init(IObjectBuilderManager theBldrMgr,IApplicationManager theAppMgr) {
		this.theBldrMgr = theBldrMgr;
		this.theAppMgr = theAppMgr;
    //Vivek Mishra : Added for Extended Barcode CR Europe 06-OCT-2016
    if("EUR".equalsIgnoreCase(Version.CURRENT_REGION))
    {
    	ConfigMgr mgr = new ConfigMgr("item.cfg");
    	isExtendedStagingBarcode = mgr.getString("EXTENDED_STAGING_BARCODE");
    }
    //Ends here 06-OCT-2106
	}

	/**
   */
	public void cleanup() {
		miscItemTemplate = null;
		miscItem = null;
	}

	/**
	 * @param theCommand
	 * @param theEvent
	 */
	public void EditAreaEvent(String theCommand, Object theEvent) {
		if (theCommand.equals("MISC_PRICE")) {
			try {
				ArmCurrency price = (ArmCurrency) theEvent;
				miscItem.setUnitPrice(price);
				miscItemTemplate.setCanOverrideAmount(false);
			} catch (BusinessRuleException ex) {
				theAppMgr.showErrorDlg(applet.res.getString(ex.getMessage()));
			}
		}
		if (theCommand.equals("MISC_COMMENT")) {
			try {
				miscItem.setComment((String) theEvent);
				miscItemTemplate.setCanOverrideComment(false);
			} catch (BusinessRuleException ex) {
				theAppMgr.showErrorDlg(applet.res.getString(ex.getMessage()));
			}
		}
		if (theCommand.equals("MISC_ITEM_TAXABLE")) {
			String answer = ((String) theEvent).toUpperCase();
			if (answer.equals(applet.res.getString("Y"))|| answer.equals(applet.res.getString("N"))) {
				try {
					miscItem.setTaxable(new Boolean(answer.equals(applet.res.getString("Y"))));
					miscItemTemplate.setCanOverrideTaxable(false);
				} catch (BusinessRuleException ex) {
					theAppMgr.showErrorDlg(applet.res.getString(ex.getMessage()));
				}
			} else {
				theAppMgr.showErrorDlg(applet.res.getString("Please respond \"Y\" or \"N\"."));
				theAppMgr.setSingleEditArea(applet.res.getString("Is the item taxable (\"Y\" or \"N\")"),
								"MISC_ITEM_TAXABLE",
								miscItemTemplate.getTaxable() ? applet.res.getString("Y") : applet.res.getString("N"));
			}
		}
		if (theCommand.equals("PRICE")) {
			try {
				ArmCurrency price = (ArmCurrency) theEvent;
				spcItem.setUnitPrice(price);
			} catch (BusinessRuleException ex) {
				theAppMgr.showErrorDlg(applet.res.getString(ex.getMessage()));
			}
		} else if (theCommand.equals("GC_PRICE")) {
			try {
				ArmCurrency price = (ArmCurrency) theEvent;
				spcItem.setUnitPrice(price);
			} catch (BusinessRuleException ex) {
				theAppMgr.showExceptionDlg(ex);
				theBldrMgr.processObject(applet, "SPECIFIC", null, this);
				return;
			}
		} else if (theCommand.equals("GC_CONTROL_ID")) {
			String sCtrlNo = ((String) theEvent).toUpperCase();
			try {
				if (item.getRedeemableType().equals(Redeemable.STORE_VALUE_CARD_ADD_TYPE)) {
					if (CMSRedeemableHelper.findStoreValueCard(theAppMgr,sCtrlNo) == null) {
						theAppMgr.showErrorDlg(applet.res.getString("There is not a stored value card on file with that control number."));
						completeAttributes();
						return;
					}
				} else if (!CMSRedeemableHelper.isNewGiftCertControlNumberValid(theAppMgr, sCtrlNo)) {
					theAppMgr.showErrorDlg(applet.res.getString("That is not a valid control number or it may already be in use."));
					completeAttributes();
					return;
				} else if (applet.getDestinationObjectForCurrentBuilder() != null) {
					CompositePOSTransaction txn = (CompositePOSTransaction) applet
							.getDestinationObjectForCurrentBuilder();
					Enumeration lineItems = txn.getSaleLineItems();
					for (; lineItems.hasMoreElements();) {
						POSLineItem lineItem = (POSLineItem) lineItems.nextElement();
						Enumeration details = lineItem.getLineItemDetails();
						for (; details.hasMoreElements();) {
							POSLineItemDetail lineItemDetail = (POSLineItemDetail) details.nextElement();
							if (lineItemDetail.getGiftCertificateId() != null
									&& lineItemDetail.getGiftCertificateId().length() > 0 && lineItemDetail.getGiftCertificateId().equals(sCtrlNo)) {
								theAppMgr.showErrorDlg(applet.res.getString("This transaction already has an item with that control ID."));
								completeAttributes();
								return;
							}
						}
					}
				}
				spcItem.setGiftCertificateId(sCtrlNo);
			} catch (BusinessRuleException bre) {
				theAppMgr.showErrorDlg(bre.getMessage());
				theBldrMgr.processObject(applet, "SPECIFIC", null, this);
				return;
			} catch (Exception ex) {
				theAppMgr.showExceptionDlg(ex);
				theBldrMgr.processObject(applet, "SPECIFIC", null, this);
				return;
			}
		} else if (theCommand.equals("SEARCH")) {
			if (!theAppMgr.isOnLine()) {
				theAppMgr.showErrorDlg(applet.res.getString("Search not available in off-line mode."));
				theBldrMgr.processObject(applet, "ITEM", null, this);
				return;
			}
			try {
				String key = ((String) theEvent).toUpperCase();
				if (key.length() > 2) {
					String[] itemIdArray = CMSItemHelper.findIDListByDescription(theAppMgr, key, storeId);
					searchMode = false;
					if (itemIdArray == null || itemIdArray.length == 0) {
						theAppMgr.showErrorDlg(applet.res
										.getString("No items found matching your description."));
						theBldrMgr.processObject(applet, "ITEM", null, this);
						return;
					} else {
						ItemDlg dlg = new ItemDlg(theAppMgr.getParentFrame(),theAppMgr, itemIdArray);
						dlg.setVisible(true);
						if (dlg.isOK()) {
							// String sItemValue = dlg.getItemId();
							// 1864
							extendedBarCode = dlg.getItemBarCode();
							String sItemValue = getBarcode(extendedBarCode);
							item = CMSItemHelper.findByBarCode(theAppMgr,sItemValue, storeId);
							itemWrapper = new CMSItemWrapper();
							itemWrapper.setItem(item);
							itemWrapper.setExtendedBarCode(extendedBarCode);
							if (completeAttributes()) {
								theBldrMgr.processObject(applet, "ITEM",itemWrapper, this);
								return;
							}
						} else {
							theBldrMgr.processObject(applet, "ITEM", null, this);
							return;
						}
					}
				} else if (key.length() > 0) {
					theAppMgr.showErrorDlg(applet.res.getString("The systems requires at least three letters to search for."));
				} else {
					theBldrMgr.processObject(applet, "ITEM", null, this);
					return;
				}
			} catch (Exception ex) {
				theAppMgr.showExceptionDlg(ex);
				theBldrMgr.processObject(applet, command, itemWrapper, this);
				return;
			}
		}
		if (completeAttributes())
			if (miscItem == null)
				theBldrMgr.processObject(applet, "SPECIFIC", itemWrapper, this);
			else {
				if (closeDepositTypes.contains(miscItemTemplate.getKey()))
					theAppMgr.addStateObject("DEPOSIT_TYPE", "CLOSE_DEPOSIT");
				theBldrMgr.processObject(applet, "MISC_ITEM", itemWrapper, this);
			}
	}

	/**
	 *
	 * @param Command
	 * @param applet
	 * @param initValue
	 */
	private void build(String Command, CMSApplet applet,MiscItemTemplate initValue) {
		miscItemTemplate = initValue;
		try {
			// 1864
			extendedBarCode = miscItemTemplate.getBaseItemId();
			String sItemValue = getBarcode(extendedBarCode);
			item = CMSItemHelper.findByBarCode(theAppMgr, sItemValue, storeId);
			if (item == null) {
				theAppMgr.showErrorDlg(applet.res.getString("Item not found"));
				return;
			}
			itemWrapper = new CMSItemWrapper();
			itemWrapper.setItem(item);
			itemWrapper.setExtendedBarCode(extendedBarCode);
			if (openDepositTypes.contains(miscItemTemplate.getKey())
					|| closeDepositTypes.contains(miscItemTemplate.getKey())) {
				CMSCustomer customer = (CMSCustomer) ((CMSCompositePOSTransaction) theAppMgr
						.getStateObject("TXN_POS")).getCustomer();
				if (customer == null) {
					theAppMgr.addStateObject("MISC_ITEM_TEMPLATE",miscItemTemplate);
					theAppMgr.addStateObject("ARM_CUST_REQUIRED", "TRUE");
					theAppMgr.fireButtonEvent("ADD_CUSTOMER");
					return;
				}
				custDepositBalance = customer.getCustomerBalance();
				item.setIsDeposit(true);
				if (closeDepositTypes.contains(miscItemTemplate.getKey()))
					miscItemTemplate.setAmount(customer.getCustomerBalance());
			}
			miscItem = new CMSMiscItem(miscItemTemplate.getKey(), item);
			itemWrapper = new CMSItemWrapper();
			itemWrapper.setMiscItem(miscItem);
			itemWrapper.setExtendedBarCode(extendedBarCode);
			applyMiscItemTemplate();
		} catch (Exception ex) {
			theAppMgr.showExceptionDlg(ex);
			ex.printStackTrace();
		}
		if (completeAttributes()) {
			if (closeDepositTypes.contains(miscItemTemplate.getKey()))
				theAppMgr.addStateObject("DEPOSIT_TYPE", "CLOSE_DEPOSIT");
			theBldrMgr.processObject(applet, "MISC_ITEM", itemWrapper, this);
		}
	}

	/**
	 * Look up the CMSItem by item id and pass it back to applet when all
	 * attributes are complete.
	 * 
	 * @param Command
	 * @param applet
	 * @param initValue
	 */
	public void build(String Command, CMSApplet applet, Object initValue) {
		if (Command.equals("V12BASKET")) {
			Command = "ITEM";
			isBasketItem = true;
		}
		item = null;
		this.applet = applet;
		cmsStore = (CMSStore) theAppMgr.getGlobalObject("STORE");
		storeId = cmsStore.getId();
		// Do not allow misc items from here
		if (MiscItemManager.getInstance().isMiscItem(initValue.toString())) {
			theAppMgr
					.showErrorDlg(applet.res
							.getString("Non-merchandise items must be entered using the Services Menu"));
			theBldrMgr.processObject(applet, "MISC_ITEM", null, this);
			return;
		}
		if (initValue instanceof MiscItemTemplate) {
			build(Command, applet, (MiscItemTemplate) initValue);
			return;
		}
		// Added code for Dolci PCR by vivek
		if ("US".equalsIgnoreCase(Version.CURRENT_REGION)) {
			if (initValue instanceof HashMap) {
				HashMap map = (HashMap) initValue;
				String itmcode = (String) map.get("ItemCode");
				Double amount = (Double) map.get("Amount");
				ArmCurrency dolciAmt = new ArmCurrency(amount.doubleValue());
				build(Command, applet, itmcode, dolciAmt);
				return;
			}
		}

		// Ended code for Dolci PCR by vivek
		if (((String) initValue).equalsIgnoreCase("S")) {
			searchMode = true;
		} else {
			try {
				// 1864
				extendedBarCode = (String) initValue;
				String sItemValue = getBarcode(extendedBarCode);
				// To achieve FindByBarcode for Armani -- Bawa Manpreet Singh
				// TO - DO : Remove Hard Coded Store No.
				// if(sItemValue.length() <=8)
				// item = CMSItemHelper.findById(theAppMgr, sItemValue);
				// else
				// theAppMgr.showErrorDlg("sItemValue: " + sItemValue);
				// code added by Anand on 04/08/2005 to enable search based on
				// retail barcode(which is returned by
				// the getRetailBarcode() of ItemEntryParser, rather than the
				// entire barode that is scanned/keyed in
				ItemEntryParser itmParser = new ItemEntryParser();
				String longerBarCode = null;
				boolean flag = true;
				String retailBarcode = itmParser.getRetailBarcode(applet,
						theAppMgr, sItemValue,
						new Integer(sItemValue.length()).toString());

				// Modified by Anjana to fetch the proper barcode based on SAP
				// or Retek Store
				ConfigMgr mgr = new ConfigMgr("item.cfg");
				String hasSapLookup = mgr.getString("SAP_LOOKUP");
				if (retailBarcode != null) {
					if (hasSapLookup != null
							&& hasSapLookup.equalsIgnoreCase("Y")) {
						if (retailBarcode.length() > 8
								&& retailBarcode.length() <= 18) {
							theAppMgr.unRegisterSingleEditArea();
							theAppMgr.getMainFrame().getGlobalBar()
									.setTextAreaEnabled(false);
							item = CMSItemHelper.findSAPItem(theAppMgr,
									retailBarcode, storeId);
						}
					} else {
						theAppMgr.unRegisterSingleEditArea();
						theAppMgr.getMainFrame().getGlobalBar()
								.setTextAreaEnabled(false);
						item = CMSItemHelper.findByBarCode(theAppMgr,
								retailBarcode, storeId);
					}// End Anjana's changes to fetch the proper barcode based
						// on SAP or Retek Store
					// 1864
					itemWrapper = new CMSItemWrapper();
					itemWrapper.setItem(item);
					itemWrapper.setExtendedBarCode(extendedBarCode);
				}
			} catch (Exception ex) {
				theAppMgr.showExceptionDlg(ex);
				ex.printStackTrace();
			}
		}
		if (completeAttributes()) {
			if (miscItem == null) {
				theBldrMgr.processObject(applet, Command, itemWrapper, this);
			} else {
				if (closeDepositTypes.contains(miscItemTemplate.getKey()))
					theAppMgr.addStateObject("DEPOSIT_TYPE", "CLOSE_DEPOSIT");
				theBldrMgr
						.processObject(applet, "MISC_ITEM", itemWrapper, this);
			}
		}
	}

	/**
	 *
	 * @return
	 */
	private boolean solicitItemDescription() {
		String[] descriptions = miscItemTemplate.getDescription();
		GenericChooserRow[] availDescriptions = new GenericChooserRow[descriptions.length];
		int defaultSelectionRow = -1;
		for (int i = 0; i < descriptions.length; i++) {
			availDescriptions[i] = new GenericChooserRow(
					new String[] { descriptions[i] }, descriptions[i]);
		}
		GenericChooseFromTableDlg dlg = new GenericChooseFromTableDlg(
				theAppMgr.getParentFrame(), theAppMgr, availDescriptions,
				new String[] { (applet.res.getString("Item Description")) });
		dlg.getTable().getColumnModel().getColumn(0)
				.setCellRenderer(centerRenderer);
		dlg.setVisible(true, miscItemTemplate.getDescIdx());
		if (dlg.isOK()) {
			String desc = (String) dlg.getSelectedRow().getRowKeyData();
			try {
				miscItemTemplate.setCanOverrideDescription(false);
				miscItem.setDescription(desc);
				return (true);
			} catch (BusinessRuleException e) {
				theAppMgr.showErrorDlg(applet.res.getString(e.getMessage()));
				return (false);
			}
		} else {
			theAppMgr
					.showErrorDlg(applet.res
							.getString("You did not select a description, you must indicate a description."));
			return (false);
		}
	}

	/**
	 *
	 * @return
	 */
	private boolean solicitMiscItemTemplateChoice() {
		MiscItemTemplate[] miscItemTemplates = MiscItemManager.getInstance()
				.getMiscItemsArray(item.getId());
		GenericChooserRow[] availMiscItemTemplates = new GenericChooserRow[miscItemTemplates.length];
		for (int i = 0; i < availMiscItemTemplates.length; i++) {
			availMiscItemTemplates[i] = new GenericChooserRow(
					new String[] { miscItemTemplates[i]
							.getMiscItemDescription() },
					miscItemTemplates[i]);
		}
		GenericChooseFromTableDlg dlg = new GenericChooseFromTableDlg(
				theAppMgr.getParentFrame(), theAppMgr, availMiscItemTemplates,
				new String[] { (applet.res.getString("Miscellaneous Item")) });
		dlg.getTable().getColumnModel().getColumn(0)
				.setCellRenderer(centerRenderer);
		dlg.setVisible(true);
		if (dlg.isOK()) {
			miscItemTemplate = (MiscItemTemplate) ((MiscItemTemplate) dlg
					.getSelectedRow().getRowKeyData()).clone();
			try {
				miscItem = new CMSMiscItem(miscItemTemplate.getKey(), item);
				itemWrapper = new CMSItemWrapper();
				itemWrapper.setMiscItem(miscItem);
				itemWrapper.setExtendedBarCode(extendedBarCode);
				applyMiscItemTemplate();
				return (true);
			} catch (BusinessRuleException e) {
				theAppMgr.showErrorDlg(applet.res.getString(e.getMessage()));
				return (false);
			}
		} else {
			theAppMgr
					.showErrorDlg(applet.res
							.getString("You did not select a miscellaneous item, item was not added."));
			return (false);
		}
	}

	/**
	 *
	 * @exception BusinessRuleException
	 */
	private void applyMiscItemTemplate() throws BusinessRuleException {
		if (!miscItemTemplate.getCanOverrideAmount())
			miscItem.setUnitPrice(miscItemTemplate.getAmount());
		if (!miscItemTemplate.getCanOverrideTaxable())
			miscItem.setTaxable(new Boolean(miscItemTemplate.getTaxable()));
		if (!miscItemTemplate.getCanOverrideDescription()) {
			String[] descriptions = miscItemTemplate.getDescription();
			if (descriptions != null && descriptions.length > 0
					&& miscItemTemplate.getDescIdx() > -1)
				miscItem.setDescription(descriptions[miscItemTemplate
						.getDescIdx()]);
		}
		if (!miscItemTemplate.getCanOverrideComment())
			miscItem.setComment(miscItemTemplate.getComment());
		miscItem.setGLAccount(miscItemTemplate.getGLaccount());
		miscItem.setDefaultQuantity(new Integer(miscItemTemplate
				.getDefaultQty()));
	}

	/**
	 * @return whether item was found and is complete.
	 */
	private boolean completeAttributes() {
		// searching for item based on description
		if (searchMode) {
			theAppMgr.showMenu(MenuConst.CANCEL_ONLY, "ITEMBUILDER",
					applet.theOpr);
			theAppMgr.setSingleEditArea(applet.res
					.getString("Enter key words from the item description."),
					"SEARCH");
			return (false);
		}
		// no item was found
		if (item == null) {
			if (isBasketItem) {
				theAppMgr
						.showErrorDlg("Transaction can not be completed, Item not present in DB");
			} else {
				theAppMgr.showErrorDlg(applet.res
						.getString("Cannot find item."));
			}
			return (true); // there are no attributes to set on a null item.
		}
		// misc item
		if (MiscItemManager.getInstance().isMiscItem(item.getId())) {
			if (miscItemTemplate == null) {
				if (!solicitMiscItemTemplateChoice()) {
					item = null;
					return (true); // there are no attributes to set on a null
									// item.
				}
				// else continue, we now have a valid template reference
			}
		}
		// misc item override amount
		if (miscItemTemplate != null && miscItemTemplate.getCanOverrideAmount()) {
			String pricePrompt = applet.res
					.getString("Enter price to charge for item.");
			if (openDepositTypes.contains(miscItemTemplate.getKey())
					|| closeDepositTypes.contains(miscItemTemplate.getKey())) {
				if (custDepositBalance == null)
					custDepositBalance = new ArmCurrency(0.0d);
				if (openDepositTypes.contains(miscItemTemplate.getKey()))
					pricePrompt = applet.res.getString("Enter Amount") + " ( "
							+ applet.res.getString("Previous Balance")
							+ custDepositBalance.formattedStringValue() + " )";
				else
					pricePrompt = applet.res.getString("Enter Amount") + " ( "
							+ applet.res.getString("Available Balance")
							+ custDepositBalance.formattedStringValue() + " )";
			}
			if (miscItemTemplate.getAmount() == null)
				theAppMgr.setSingleEditArea(pricePrompt, "MISC_PRICE",
						theAppMgr.CURRENCY_MASK);
			else
				theAppMgr.setSingleEditArea(pricePrompt, "MISC_PRICE",
						miscItemTemplate.getAmount(), theAppMgr.CURRENCY_MASK);
			return (false);
		}
		// misc item override description
		if (miscItemTemplate != null
				&& miscItemTemplate.getCanOverrideDescription()) {
			if (solicitItemDescription()) {
			} else
				// and continue
				return (false);
		}
		// misc item override tax
		if (miscItemTemplate != null
				&& miscItemTemplate.getCanOverrideTaxable()) {
			String initValue = miscItemTemplate.getTaxable() ? applet.res
					.getString("Y") : applet.res.getString("N");
			theAppMgr.setSingleEditArea(applet.res
					.getString("Is the item taxable (\"Y\" or \"N\")"),
					"MISC_ITEM_TAXABLE", initValue, theAppMgr.REQUIRED_MASK);
			return (false);
		}
		// misc item override comment
		if (miscItemTemplate != null
				&& miscItemTemplate.getCanOverrideComment()) {
			if (miscItemTemplate.getComment() == null) {
				theAppMgr.setSingleEditArea(
						applet.res.getString("Enter comment for item."),
						"MISC_COMMENT", theAppMgr.INPUT_IS_OPTIONAL_MASK);
			} else {
				// System.out.println("comment default: " +
				// miscItemTemplate.getComment());
				theAppMgr.setSingleEditArea(
						applet.res.getString("Enter comment for item."),
						"MISC_COMMENT", miscItemTemplate.getComment(),
						theAppMgr.INPUT_IS_OPTIONAL_MASK);
			}
			return (false);
		}
		// gift certificates *OR* store values cards.
		if (item.isRedeemable()) {
			if (!theAppMgr.isOnLine()) {
				theAppMgr
						.showErrorDlg("Redeemable items cannot be purchased or redeemed while in offline mode.");
				item = null;
				theBldrMgr.processObject(applet, "SPECIFIC", null, this);
				return (true); // there are no attributes to set on a null item.
			}
			if (applet instanceof InitialSaleApplet
					&& ((InitialSaleApplet) applet).getMode() == InitialSaleApplet.RETURN_MODE) {
				theAppMgr
						.showErrorDlg("Redeemable items cannot be returned.  Use the 'Buy Back' option in the management menu.");
				item = null;
				return (true); // there are no attributes to set on a null item.
			}
			if (spcItem == null) {
				spcItem = new CMSSpecificItem(item);
				itemWrapper = new CMSItemWrapper();
				itemWrapper.setSpecificItem(spcItem);
				itemWrapper.setExtendedBarCode(extendedBarCode);
			}
			try {
				if (spcItem.getGiftCertificateId() == null
						|| spcItem.getGiftCertificateId().length() == 0) {
					if (item.getRedeemableType().equals(
							Redeemable.GIFT_CERTIFICATE_TYPE)) {
						theAppMgr
								.setSingleEditArea(
										applet.res
												.getString("Enter gift certificate ID or control number."),
										"GC_CONTROL_ID");
					} else { // if(item.getRedeemableType().equals(Redeemable.STORE_VALUE_CARD_TYPE))
						theAppMgr
								.setSingleEditArea(
										applet.res
												.getString("Enter store value card ID or control number."),
										"GC_CONTROL_ID");
					}
					return (false);
				}
				if (spcItem.getUnitPrice() == null
						|| spcItem.getUnitPrice().lessThan(
								new ArmCurrency(0.01))) {
					if (item.getRedeemableType().equals("G")) {
						theAppMgr.setSingleEditArea(applet.res
								.getString("Enter gift certificate amount."),
								"GC_PRICE", theAppMgr.CURRENCY_MASK);
					} else { // if(item.getRedeemableType().equals("S"))
						theAppMgr.setSingleEditArea(applet.res
								.getString("Enter store value card amount."),
								"GC_PRICE", theAppMgr.CURRENCY_MASK);
					}
					return (false);
				}
				return (true);
			} catch (Exception ex) {
				theAppMgr.showExceptionDlg(ex);
			}
		}
		// manual unit price
		if (miscItemTemplate == null && item.requiresManualUnitPrice()) { // ||
																			// item.isRedeemable())
																			// Quick
																			// fix,
																			// these
																			// switches
																			// are
																			// not
																			// independent
																			// but
																			// should
																			// be
			if (spcItem == null) {
				spcItem = new CMSSpecificItem(item);
				itemWrapper = new CMSItemWrapper();
				itemWrapper.setSpecificItem(spcItem);
				itemWrapper.setExtendedBarCode(extendedBarCode);
			}
			if (spcItem.getUnitPrice() == null
					|| spcItem.getUnitPrice().doubleValue() == 0) {
				theAppMgr.setSingleEditArea(
						applet.res.getString("Enter manual price amount."),
						"PRICE", theAppMgr.CURRENCY_MASK);
				return (false);
			}
		}
		return (true);
	} // completeAttributes

	/**
	 * put your documentation comment here
	 */
	private static void initDepositTypes() {
		openDepositTypes = new Vector();
		closeDepositTypes = new Vector();
		ConfigMgr mgr = new ConfigMgr("item.cfg");
		String depositeTypesStr = mgr.getString("OPEN_DEPOSIT_TYPES");
		if (depositeTypesStr == null || depositeTypesStr.trim().length() == 0)
			return;
		StringTokenizer strTok = new StringTokenizer(depositeTypesStr, ",");
		while (strTok.hasMoreTokens()) {
			openDepositTypes.addElement(strTok.nextToken());
		}
		depositeTypesStr = mgr.getString("CLOSE_DEPOSIT_TYPES");
		strTok = new StringTokenizer(depositeTypesStr, ",");
		while (strTok.hasMoreTokens()) {
			closeDepositTypes.addElement(strTok.nextToken());
		}
	}

	/**
	 * Parse the entered barcode - :1864
	 * 
	 * @param inputBarCode
	 * @return
	 */
	private String getBarcode(String inputBarCode) {
		// return (inputBarCode.length() == 34)?inputBarCode.substring(0,
		// 11):inputBarCode;
		return inputBarCode;
	}

	// Added for Dolci PCR vivek
	public void build(String Command, CMSApplet applet, String initValue,
			ArmCurrency amount) {
		try {

			// 1864
			extendedBarCode = (String) initValue;
			String sItemValue = getBarcode(extendedBarCode);
			// To achieve FindByBarcode for Armani -- Bawa Manpreet Singh
			// TO - DO : Remove Hard Coded Store No.
			// if(sItemValue.length() <=8)
			// item = CMSItemHelper.findById(theAppMgr, sItemValue);
			// else
			// theAppMgr.showErrorDlg("sItemValue: " + sItemValue);
			// code added by Anand on 04/08/2005 to enable search based on
			// retail barcode(which is returned by
			// the getRetailBarcode() of ItemEntryParser, rather than the entire
			// barode that is scanned/keyed in
			ItemEntryParser itmParser = new ItemEntryParser();
			String retailBarcode = itmParser.getRetailBarcode(applet,
					theAppMgr, sItemValue,
					new Integer(sItemValue.length()).toString());
			if (retailBarcode != null) {
				theAppMgr.unRegisterSingleEditArea();
				theAppMgr.getMainFrame().getGlobalBar()
						.setTextAreaEnabled(false);
				item = CMSItemHelper.findByBarCode(theAppMgr, retailBarcode,
						storeId);
				// 1864
				if (item == null) {
					theAppMgr.showErrorDlg("Can not find item.");
					item = null;
					return;
				}
				itemWrapper = new CMSItemWrapper();
				itemWrapper.setItem(item);
				item.setRetailPrice(amount);
				itemWrapper.setExtendedBarCode(extendedBarCode);

			}
		} catch (Exception ex) {
			theAppMgr.showExceptionDlg(ex);
			ex.printStackTrace();
		}
		if (completeAttributes()) {
			if (miscItem == null) {
				theBldrMgr.processObject(applet, Command, itemWrapper, this);
			} else {
				if (closeDepositTypes.contains(miscItemTemplate.getKey()))
					theAppMgr.addStateObject("DEPOSIT_TYPE", "CLOSE_DEPOSIT");
				theBldrMgr
						.processObject(applet, "MISC_ITEM", itemWrapper, this);
			}
		}
	}

	// Ended code for Dolci PCR vivek
}
