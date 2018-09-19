
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

import java.text.SimpleDateFormat;
import java.util.Date;

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
import com.chelseasystems.cs.item.CMSItemHelper;
/*

 <p>Title: ExceptionItemBldr </p>
 <p>Description: Used for Adding  Non Service Items into transaction</p>
  <p>Company: SkillNet Inc</p>
  @author Deepika Auti


+------+------------+-----------+---------------------------+-
| 1    | 20-03-2012 | Deepika   | Adding NonServiceItem PCR |       
+------+------------+-----------+---------------------------+-
 */


/**
 */
public class ExceptionItemBldr implements IObjectBuilder {
	private CMSItem item;
	private CMSMiscItem miscItem;
	private CMSItemWrapper itemWrapper;
	private MiscItemTemplate exceptionItemTemplate;
	private IObjectBuilderManager theBldrMgr;
	private CMSApplet applet;
	private IApplicationManager theAppMgr;
	private String buildCommand;
	private String itemId;
	private String itemDept;
	private String itemClass;
	private String itemDesc;
	// Added by Satin to look for itemId in AS_ITM corresponding to barcode entered.
	private String barcode;
	private String correspondingItemId;
	private CMSStore cmsStore;
	private String storeId;
	
	// Added by Satin for Exception item.
	private ArmCurrency unitPrice;
	private String taxable;
	private String itemId1;
	private int flag = 0;

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
		exceptionItemTemplate = null;
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
				{//itemId = (String)theEvent;
				
				// Added by Satin to look for itemId in AS_ITM corresponding to barcode entered. 
				cmsStore = (CMSStore) theAppMgr.getGlobalObject("STORE");
			    storeId = cmsStore.getId();
				barcode = (String)theEvent;
				itemId1 = (String)theEvent;
				
				if((barcode.length()) == 8){
					itemId = barcode;
					// Add code to update AS_ITM_RTL_STR table based on 8 digit barcode

					CMSItem testItem1 = null;

				
					try {
					    
					testItem1 = (CMSItem) CMSItemHelper.findById(theAppMgr, itemId);
						
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
				
				else{
				 try {
					itemId = (String)CMSItemHelper.selectItemIdFromAsItm(theAppMgr, barcode);
					// Add code to insert item for the store in AS_ITM_RTL_STR based on ItemId received from AS_ITM
					
					if (itemId == null){
						theAppMgr.showErrorDlg(applet.res.getString("Barcode lookup failed."));						
					}
										 
				} catch (Exception e) {
					e.printStackTrace();
				};
				}
				}
			//Added by Satin to check if item is present in AS_ITM
			try {
				String itemPresentInAsItm = (String)CMSItemHelper.selectItemFromAsItm(theAppMgr, itemId);
				if(itemPresentInAsItm==null){
					//theAppMgr.showErrorDlg(applet.res.getString("Original item not on file."));
					theAppMgr.showErrorDlg(applet.res.getString("Item not present in Master. " +
							"Press Esc key and use 'Item Not on File' under Services."));
					
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
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
		} 
		else if (theCommand.equals("DESC")) {
			if(((String)theEvent).length() > 20)
				theAppMgr.showErrorDlg(applet.res.getString("Item Class is too long"));
			else
				itemDesc = (String)theEvent;
		}else if (theCommand.equals("PRICE")) {
			   try {
			   //Anjana added 05-22-2017 to prevent preciosn issue causing broken txns
			    	  if(((ArmCurrency)theEvent).greaterThan(new ArmCurrency(999999.99))){
			    		  theAppMgr.showErrorDlg(applet.res.getString("Please check the Item Price"));
			    	  }
			    	  else
				miscItem.setUnitPrice((ArmCurrency)theEvent);
				 unitPrice = ((ArmCurrency)theEvent);
			} catch (BusinessRuleException ex) {
				ex.printStackTrace();
			} catch (CurrencyException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		else if (theCommand.equals("TAXABLE")) {
			String answer = ((String)theEvent).toUpperCase();
			taxable = ((String)theEvent).toUpperCase();
			if (answer.equals(applet.res.getString("Y")) || answer.equals(applet.res.getString("N"))) {
				try {
					miscItem.setTaxable(new Boolean(answer.equals(applet.res.getString("Y"))));
					exceptionItemTemplate.setCanOverrideTaxable(false);
					processedTaxable = true;
				} catch (BusinessRuleException ex) {
					theAppMgr.showErrorDlg(applet.res.getString(ex.getMessage()));
				}
			} else {
				theAppMgr.showErrorDlg(applet.res.getString("Please respond \"Y\" or \"N\"."));
				theAppMgr.setSingleEditArea(applet.res.getString("Is the item taxable (\"Y\" or \"N\")")
						, "TAXABLE"
						, exceptionItemTemplate.getTaxable() ? applet.res.getString("Y")
								: applet.res.getString("N"));
			}
		}
		

		// Added by Satin to insert a record into AS_ITM_RTL_STR.
//starts here		
		CMSItem itemToInsert = new CMSItem(itemId);
		Date date = new Date();
		cmsStore = (CMSStore) theAppMgr.getGlobalObject("STORE");
	    storeId = cmsStore.getId();
	    String miscItemDesc = itemDesc;

	    try{
	    	if (completeAttributes()) {
	    	itemToInsert.setId(itemId);
			itemToInsert.setStoreId(storeId);
			try {

				itemToInsert.setDescription(itemDesc);
				itemToInsert.setRetailPrice(miscItem.getUnitPrice());
			} catch (BusinessRuleException e1) {
				e1.printStackTrace();
			}
			itemToInsert.SetUpdateDate(date);
			itemToInsert.doSetTaxable(miscItem.getTaxable());
			
			String retailPrice1 = (itemToInsert.getRetailPrice().toString());
			String[] token1= retailPrice1.split(", |->");
			String currencyCode = token1[0];

			itemToInsert.setCurrencyCode(currencyCode);
			Double vatRate= itemToInsert.getVatRate();
			
			String taxable;
			
			ArmCurrency retailPrice = miscItem.getUnitPrice();
			
			if (miscItem.getTaxable()){
				taxable = "Y";
			}
			else
			{
				taxable= "N";
			}
			
			
			try {
				CMSItemHelper.insertIntoAsItmRtlStr(theAppMgr, storeId, itemId, retailPrice, currencyCode, itemDesc, vatRate, taxable);
			}
			 catch (Exception e) {
				e.printStackTrace();
			}
	    }
	    	}
	    catch(Exception e){
	    	e.printStackTrace();
	    }
//ends here
		
	    
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
			exceptionItemTemplate = (MiscItemTemplate)initValue;
			//            return;
		} 
		processBuild();
	}

	/**
	 * @return whether item was found and is complete.
	 */
	private boolean completeAttributes()
	throws Exception {
		if (miscItem == null) {
			miscItem = this.createExceptionItem();
			if (miscItem == null) return true;
			//       throw new BusinessRuleException("ERROR: while creating Misc Item.");
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
		if (itemDesc == null || itemDesc.trim().length() == 0) {
			theAppMgr.setSingleEditArea(applet.res.getString("Enter Item Desc"), "DESC"
					, theAppMgr.REQUIRED_MASK);
			return false;
		}
		if (miscItem.getUnitPrice() == null) {
			theAppMgr.setSingleEditArea(applet.res.getString("Enter Item Unit Price"), "PRICE"
					, theAppMgr.CURRENCY_MASK);
			return false;
		}
		if (!processedTaxable) {
			String initValue = exceptionItemTemplate.getTaxable() ? applet.res.getString("Y")
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
	private CMSMiscItem createExceptionItem()
	throws Exception {
		miscItem = null;
		item= new CMSItem(null);
		if (exceptionItemTemplate != null) {
			String storeId = ((CMSStore)theAppMgr.getGlobalObject("STORE")).getId();
			item.setStoreId(storeId);
			if (item == null)
			{
				theBldrMgr.processObject(applet, buildCommand, miscItem, this);
				return null;    	
			}
			miscItem = new CMSMiscItem("Exception_Item", item);
			//___Tim
			itemWrapper = new CMSItemWrapper();
			itemWrapper.setMiscItem(miscItem);
			//            miscItem = new CMSMiscItem(exceptionItemTemplate.getBaseItemId(), null);
			//Added by Satin for test.
			

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
					((CMSItem) miscItem.getItem()).setId(itemId);
					((CMSItem) miscItem.getItem()).setBarCode(itemId);
					miscItem.setDescription(itemDesc);
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

