/*
 * @copyright (c) 2001 Chelsea Market Systems LLC
 *
 * formatted with JxBeauty (c) johann.langhofer@nextra.at
 */
/*
 History:
 +------+------------+-----------+-----------+---------------------------------------------+
 | Ver# | Date       | By        | Defect #  | Description                                 |
 +------+------------+-----------+-----------+---------------------------------------------+
 | 45   | 11-06-2006 | Tim       | 1699      | Modified ItemBldr to enable misc item entry |
 |      |            |           |           | from edit area                              |
 +------+------------+-----------+-----------+---------------------------------------------+
 | 4    | 08-18-2006 | Tim       | N/A       | Modified ItemLookup to search for items     |
 |      |            |           |           | on various params and from same brand stores|
 +------+------------+-----------+-----------+---------------------------------------------+
 | 3    | 04-12-2005 | Anand     | N/A       | Incorporated PCR to search by retail barcode|
 |      |            |           |           | instead of the entire bar code as was case  |
 +------+------------+-----------+-----------+---------------------------------------------+
 | 2    | 02-08-2005 | Manpreet  | N/A       | Modified to findByBarcode()                 |
 +------+------------+-----------+-----------+---------------------------------------------+
 */

package com.chelseasystems.cs.swing.builder;

import com.chelseasystems.cr.appmgr.*;
import com.chelseasystems.cr.appmgr.mask.MaskConstants;
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
import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import java.util.Enumeration;
import java.util.Iterator;

import com.chelseasystems.cs.store.CMSStore;
import java.util.Vector;
import com.chelseasystems.cr.config.ConfigMgr;
import java.util.StringTokenizer;
import com.chelseasystems.cs.customer.CMSCustomer;

/**
 */
public class ItemBldr_JP implements IObjectBuilder {
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

	private ItemSearchString itemSearchString;

	private String extendedBarCode = "";
	
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
	public void init(IObjectBuilderManager theBldrMgr,
			IApplicationManager theAppMgr) {
		this.theBldrMgr = theBldrMgr;
		this.theAppMgr = theAppMgr;
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
				theAppMgr
						.showErrorDlg(CMSApplet.res.getString(ex.getMessage()));
			}
		}
		if (theCommand.equals("MISC_COMMENT")) {
			try {
				miscItem.setComment((String) theEvent);
				miscItemTemplate.setCanOverrideComment(false);
			} catch (BusinessRuleException ex) {
				theAppMgr
						.showErrorDlg(CMSApplet.res.getString(ex.getMessage()));
			}
		}
		if (theCommand.equals("MISC_ITEM_TAXABLE")) {
			String answer = ((String) theEvent).toUpperCase();
			if (answer.equals(CMSApplet.res.getString("Y"))
					|| answer.equals(CMSApplet.res.getString("N"))) {
				try {
					miscItem.setTaxable(new Boolean(answer.equals(CMSApplet.res
							.getString("Y"))));
					miscItemTemplate.setCanOverrideTaxable(false);
				} catch (BusinessRuleException ex) {
					theAppMgr.showErrorDlg(CMSApplet.res.getString(ex
							.getMessage()));
				}
			} else {
				theAppMgr.showErrorDlg(CMSApplet.res
						.getString("Please respond \"Y\" or \"N\"."));
				theAppMgr.setSingleEditArea(CMSApplet.res
						.getString("Is the item taxable (\"Y\" or \"N\")"),
						"MISC_ITEM_TAXABLE",
						miscItemTemplate.getTaxable() ? CMSApplet.res
								.getString("Y") : CMSApplet.res.getString("N"));
			}
		}
		if (theCommand.equals("PRICE")) {
			try {
				ArmCurrency price = (ArmCurrency) theEvent;
				spcItem.setUnitPrice(price);
			} catch (BusinessRuleException ex) {
				theAppMgr
						.showErrorDlg(CMSApplet.res.getString(ex.getMessage()));
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
				if (item.getRedeemableType().equals(
						Redeemable.STORE_VALUE_CARD_ADD_TYPE)) {
					if (CMSRedeemableHelper.findStoreValueCard(theAppMgr,
							sCtrlNo) == null) {
						theAppMgr
								.showErrorDlg(CMSApplet.res
										.getString("There is not a stored value card on file with that control number."));
						completeAttributes();
						return;
					}
				} else if (!CMSRedeemableHelper
						.isNewGiftCertControlNumberValid(theAppMgr, sCtrlNo)) {
					theAppMgr
							.showErrorDlg(CMSApplet.res
									.getString("That is not a valid control number or it may already be in use."));
					completeAttributes();
					return;
				} else if (applet.getDestinationObjectForCurrentBuilder() != null) {
					CompositePOSTransaction txn = (CompositePOSTransaction) applet
							.getDestinationObjectForCurrentBuilder();
					Enumeration lineItems = txn.getSaleLineItems();
					for (; lineItems.hasMoreElements();) {
						POSLineItem lineItem = (POSLineItem) lineItems
								.nextElement();
						Enumeration details = lineItem.getLineItemDetails();
						for (; details.hasMoreElements();) {
							POSLineItemDetail lineItemDetail = (POSLineItemDetail) details
									.nextElement();
							if (lineItemDetail.getGiftCertificateId() != null
									&& lineItemDetail.getGiftCertificateId()
											.length() > 0
									&& lineItemDetail.getGiftCertificateId()
											.equals(sCtrlNo)) {
								theAppMgr
										.showErrorDlg(CMSApplet.res
												.getString("This transaction already has an item with that control ID."));
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
		}
		if (completeAttributes())
			if (miscItem == null)
				theBldrMgr.processObject(applet, "SPECIFIC", itemWrapper, this);
			else {
				if (closeDepositTypes.contains(miscItemTemplate.getKey()))
					theAppMgr.addStateObject("DEPOSIT_TYPE", "CLOSE_DEPOSIT");
				theBldrMgr
						.processObject(applet, "MISC_ITEM", itemWrapper, this);
			}
	}

	/**
	 * 
	 * @param Command
	 * @param applet
	 * @param initValue
	 */
	private void build(String Command, CMSApplet applet,
			MiscItemTemplate initValue) {
		miscItemTemplate = initValue;
		try {
			// 1864
			extendedBarCode = miscItemTemplate.getBaseItemId();
			String sItemValue = getBarcode(extendedBarCode);
			item = CMSItemHelper.findByBarCode(theAppMgr, sItemValue, storeId);
			if (item == null) {
				theAppMgr.showErrorDlg(CMSApplet.res
						.getString("Item not found"));
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
					theAppMgr.addStateObject("MISC_ITEM_TEMPLATE",
							miscItemTemplate);
					theAppMgr.addStateObject("ARM_CUST_REQUIRED", "TRUE");
					theAppMgr.fireButtonEvent("ADD_CUSTOMER");
					return;
				}
				custDepositBalance = customer.getCustomerBalance();
				item.setIsDeposit(true);
				if (closeDepositTypes.contains(miscItemTemplate.getKey())) {
					miscItemTemplate.setAmount(customer.getCustomerBalance());
				}
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
		if (MiscItemManager.getInstance().isMiscItem(initValue.toString())) {
			// ___Tim: 1699: Changed to allow miscItem entry from editArea.
			// However, deposits still have to be added from the "Services"
			// dialog
			initValue = this.getMiscItemTemplate(initValue.toString());
			if (initValue == null) {
				theBldrMgr.processObject(this.applet, "MISC_ITEM", null, this);
				return;
			}
		}
		if (initValue instanceof MiscItemTemplate) {
			if (((MiscItemTemplate) initValue).getKey().equals("NOTONFILE")) {
				String builderStr = new ConfigMgr("item.cfg")
						.getString("NOTONFILE.BUILDER");
				theAppMgr.buildObject(this.applet, "MISC_ITEM", builderStr,
						(MiscItemTemplate) initValue);
			} else {
				build(Command, this.applet, (MiscItemTemplate) initValue);
			}
			return;
		}
		if (((String) initValue).equalsIgnoreCase("S")) {
			searchMode = true;
		} else {
			try {
				String sItemValue = (String) initValue;
				extendedBarCode = sItemValue;
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
				String retailBarcode = itmParser.getRetailBarcode(applet,
						theAppMgr, sItemValue, new Integer(sItemValue.length())
								.toString());
				if (retailBarcode != null) {
					item = CMSItemHelper.findByBarCode(theAppMgr,
							retailBarcode, storeId);
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
		for (int i = 0; i < descriptions.length; i++) {
			availDescriptions[i] = new GenericChooserRow(
					new String[] { descriptions[i] }, descriptions[i]);
		}
		GenericChooseFromTableDlg dlg = new GenericChooseFromTableDlg(theAppMgr
				.getParentFrame(), theAppMgr, availDescriptions,
				new String[] { (CMSApplet.res.getString("Item Description")) });
		dlg.getTable().getColumnModel().getColumn(0).setCellRenderer(
				centerRenderer);
		dlg.setVisible(true, miscItemTemplate.getDescIdx());
		if (dlg.isOK()) {
			String desc = (String) dlg.getSelectedRow().getRowKeyData();
			try {
				miscItemTemplate.setCanOverrideDescription(false);
				miscItem.setDescription(desc);
				return (true);
			} catch (BusinessRuleException e) {
				theAppMgr.showErrorDlg(CMSApplet.res.getString(e.getMessage()));
				return (false);
			}
		} else {
			theAppMgr
					.showErrorDlg(CMSApplet.res
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
							.getMiscItemDescription() }, miscItemTemplates[i]);
		}
		GenericChooseFromTableDlg dlg = new GenericChooseFromTableDlg(
				theAppMgr.getParentFrame(),
				theAppMgr,
				availMiscItemTemplates,
				new String[] { (CMSApplet.res.getString("Miscellaneous Item")) });
		dlg.getTable().getColumnModel().getColumn(0).setCellRenderer(
				centerRenderer);
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
				theAppMgr.showErrorDlg(CMSApplet.res.getString(e.getMessage()));
				return (false);
			}
		} else {
			theAppMgr
					.showErrorDlg(CMSApplet.res
							.getString("You did not select a miscellaneous item, item was not added."));
			return (false);
		}
	}

	/**
	 * Build the MiscItemTemplate using the given baseItemId. To be used when
	 * the miscItemID is entered/scanned in the editArea
	 * 
	 * @param baseItemId
	 * @return MiscItemTemplate
	 */
	private MiscItemTemplate getMiscItemTemplate(String baseItemId) {
		MiscItemTemplate[] miscItemTemplates = MiscItemManager.getInstance()
				.getMiscItemsArray(baseItemId);
		if ((miscItemTemplates != null) && (miscItemTemplates.length == 1)
				&& !isDepositItemKey(miscItemTemplates[0].getKey())) {
			return (MiscItemTemplate) miscItemTemplates[0].clone();
		}
		theAppMgr.showErrorDlg(CMSApplet.res.getString("Cannot find item."));
		return null;
	}

	private boolean isDepositItemKey(String miscItemKey) {
		Iterator iter = null;
		if (openDepositTypes != null) {
			iter = openDepositTypes.iterator();
			while (iter.hasNext()) {
				if (((String) iter.next()).equalsIgnoreCase(miscItemKey))
					return true;
			}
		}
		if (closeDepositTypes != null) {
			iter = closeDepositTypes.iterator();
			while (iter.hasNext()) {
				if (((String) iter.next()).equalsIgnoreCase(miscItemKey))
					return true;
			}
		}
		return false;
	}

	/**
	 * 
	 * @exception BusinessRuleException
	 */
	private void applyMiscItemTemplate() throws BusinessRuleException {
		if (!miscItemTemplate.getCanOverrideAmount()) {
			miscItem.setUnitPrice(miscItemTemplate.getAmount());
		}
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
			if (!theAppMgr.isOnLine()) {
				theAppMgr.showErrorDlg(CMSApplet.res
						.getString("Search not available in off-line mode."));
				theBldrMgr.processObject(applet, "ITEM", null, this);
				return false;
			}
			itemSearchString = new ItemSearchString();
			ItemLookupDlg itmLookupDLg = new ItemLookupDlg(theAppMgr
					.getParentFrame(), theAppMgr, itemSearchString);
			itmLookupDLg.setVisible(true);
			if (itemSearchString.isSearchRequired()) {
				try {
					Item[] itemArray = CMSItemHelper.findItems(theAppMgr,
							itemSearchString);
					
					// ___Tim: Bug 1704: do not restrict search by brand or
					// storeID.
					/*
					 * if (itemArray == null || itemArray.length == 0) {
					 * itemSearchString.setBrand(((CMSStore)theAppMgr.getGlobalObject("STORE")).getBrandID());
					 * System.out.println(" Unable to find item in current store .. " +
					 * "looking in other same brand stores... itemSearchString = " +
					 * itemSearchString.toString()); itemArray =
					 * CMSItemHelper.findItems(theAppMgr, itemSearchString); }
					 */
					searchMode = false;
					if (itemArray == null || itemArray.length == 0) {
						theAppMgr
								.showErrorDlg(CMSApplet.res
										.getString("No items found matching your description."));
						theBldrMgr.processObject(applet, "ITEM", null, this);
						return false;
					} else {
						ItemDlg dlg = new ItemDlg(theAppMgr.getParentFrame(),
								theAppMgr, itemArray);
						dlg.setVisible(true);
						if (dlg.isOK()) {
							// 1864
							// before item=dlg.getItem();
							extendedBarCode = dlg.getItemBarCode();
							String sItemValue = getBarcode(extendedBarCode);
							// this will return store specific item 
							item = CMSItemHelper.findByBarCode(theAppMgr,
									sItemValue, storeId);
							//1925 if item is not present in current store
							if(item == null){
								item=dlg.getItem();
							}
							itemWrapper = new CMSItemWrapper();
							itemWrapper.setItem(item);
							itemWrapper.setExtendedBarCode(extendedBarCode);
							if (completeAttributes()) {
								theBldrMgr.processObject(applet, "ITEM",
										itemWrapper, this);
							}
						} else {
							theBldrMgr
									.processObject(applet, "ITEM", null, this);
							return false;
						}
					}
				} catch (Exception ex) {
					theAppMgr.showExceptionDlg(ex);
					theBldrMgr
							.processObject(applet, command, itemWrapper, this);
					return false;
				}

			} else {
				theBldrMgr.processObject(applet, "ITEM", null, this);
				return false;
			}
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
			String pricePrompt = CMSApplet.res
					.getString("Enter price to charge for item.");
			if (openDepositTypes.contains(miscItemTemplate.getKey())
					|| closeDepositTypes.contains(miscItemTemplate.getKey())) {
				if (custDepositBalance == null)
					custDepositBalance = new ArmCurrency(0.0d);
				if (openDepositTypes.contains(miscItemTemplate.getKey()))
					pricePrompt = CMSApplet.res.getString("Enter Amount")
							+ " ( "
							+ CMSApplet.res.getString("Previous Balance")
							+ custDepositBalance.formattedStringValue() + " )";
				else
					pricePrompt = CMSApplet.res.getString("Enter Amount")
							+ " ( "
							+ CMSApplet.res.getString("Available Balance")
							+ custDepositBalance.formattedStringValue() + " )";
			}
			if (miscItemTemplate.getAmount() == null)
				theAppMgr.setSingleEditArea(pricePrompt, "MISC_PRICE",
						MaskConstants.CURRENCY_MASK);
			else
				theAppMgr.setSingleEditArea(pricePrompt, "MISC_PRICE",
						miscItemTemplate.getAmount(),
						MaskConstants.CURRENCY_MASK);
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
			String initValue = miscItemTemplate.getTaxable() ? CMSApplet.res
					.getString("Y") : CMSApplet.res.getString("N");
			theAppMgr
					.setSingleEditArea(CMSApplet.res
							.getString("Is the item taxable (\"Y\" or \"N\")"),
							"MISC_ITEM_TAXABLE", initValue,
							MaskConstants.REQUIRED_MASK);
			return (false);
		}
		// misc item override comment
		if (miscItemTemplate != null
				&& miscItemTemplate.getCanOverrideComment()) {
			if (miscItemTemplate.getComment() == null) {
				theAppMgr.setSingleEditArea(CMSApplet.res
						.getString("Enter comment for item."), "MISC_COMMENT",
						MaskConstants.INPUT_IS_OPTIONAL_MASK);
			} else {
				// System.out.println("comment default: " +
				// miscItemTemplate.getComment());
				theAppMgr.setSingleEditArea(CMSApplet.res
						.getString("Enter comment for item."), "MISC_COMMENT",
						miscItemTemplate.getComment(),
						MaskConstants.INPUT_IS_OPTIONAL_MASK);
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
				return (true); // there are no attributes to set on a null
								// item.
			}
			if (applet instanceof InitialSaleApplet
					&& ((InitialSaleApplet) applet).getMode() == InitialSaleApplet.RETURN_MODE) {
				theAppMgr
						.showErrorDlg("Redeemable items cannot be returned.  Use the 'Buy Back' option in the management menu.");
				item = null;
				return (true); // there are no attributes to set on a null
								// item.
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
										CMSApplet.res
												.getString("Enter gift certificate ID or control number."),
										"GC_CONTROL_ID");
					} else { // if(item.getRedeemableType().equals(Redeemable.STORE_VALUE_CARD_TYPE))
						theAppMgr
								.setSingleEditArea(
										CMSApplet.res
												.getString("Enter store value card ID or control number."),
										"GC_CONTROL_ID");
					}
					return (false);
				}
				if (spcItem.getUnitPrice() == null
						|| spcItem.getUnitPrice().lessThan(
								new ArmCurrency(0.01))) {
					if (item.getRedeemableType().equals("G")) {
						theAppMgr.setSingleEditArea(CMSApplet.res
								.getString("Enter gift certificate amount."),
								"GC_PRICE", MaskConstants.CURRENCY_MASK);
					} else { // if(item.getRedeemableType().equals("S"))
						theAppMgr.setSingleEditArea(CMSApplet.res
								.getString("Enter store value card amount."),
								"GC_PRICE", MaskConstants.CURRENCY_MASK);
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
				theAppMgr.setSingleEditArea(CMSApplet.res
						.getString("Enter manual price amount."), "PRICE",
						MaskConstants.CURRENCY_MASK);
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
	 * Parse the entered barcode - if it is 34 digits long, then the lookup
	 * should use only the first 12 digits - :1864
	 * 
	 * @param inputBarCode
	 * @return
	 */
	private String getBarcode(String inputBarCode) {
		// return (inputBarCode.length() == 34)?inputBarCode.substring(0,
		// 12):inputBarCode;
		return inputBarCode;
	}
} // ItemBldr
