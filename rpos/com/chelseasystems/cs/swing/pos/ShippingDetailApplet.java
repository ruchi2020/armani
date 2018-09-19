/*
 * @copyright (c) 2001 Chelsea Market Systems LLC
 *
 * formatted with JxBeauty (c) johann.langhofer@nextra.at
 */
/*
 History:
 +------+------------+-----------+-----------+----------------------------------------------+
 | Ver# | Date       | By        | Defect #  | Description                                  |
 +------+------------+-----------+-----------+----------------------------------------------+
 | 4    | 07-18-2005 | Vikram    | 602       |Added dialog prompt to be displayed for "Home"|
 +------+------------+-----------+-----------+----------------------------------------------+
 | 3    | 06-08-2005 | Vikram    | 124       |Corrected code for unselected items           |
 --------------------------------------------------------------------------------------------
 | 2    | 04-12-2005 | Khyati    | N/A       |1.Send Sale specification.                    |
 --------------------------------------------------------------------------------------------
 | 1    | 04-12-2005 | Base      | N/A       |                                              |
 --------------------------------------------------------------------------------------------
 */

package com.chelseasystems.cs.swing.pos;

import com.chelseasystems.cr.swing.*;
import com.chelseasystems.cr.swing.event.CMSActionEvent;
import com.chelseasystems.cr.rules.BusinessRuleException;
import com.chelseasystems.cs.swing.builder.CMSItemWrapper;

//added by Shushma for Shipping Fed-Ex
import com.chelseasystems.cs.swing.dlg.GenericChooseFromTableDlg;
import com.chelseasystems.cs.swing.dlg.GenericChooserRow;
//Fed-Ex close
import com.chelseasystems.cs.swing.menu.MenuConst;
import com.chelseasystems.cs.util.Version;
import com.chelseasystems.cs.employee.CMSEmployee;
import com.chelseasystems.cs.employee.CMSEmployeeHelper;
import com.chelseasystems.cs.pos.*;
import com.chelseasystems.cr.pos.*;
import com.chelseasystems.cr.rules.BusinessRuleException;
import com.chelseasystems.cr.swing.bean.*;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.sql.Date;
import java.util.StringTokenizer;

import com.chelseasystems.cr.business.BusinessObject;
import com.chelseasystems.cr.currency.ArmCurrency;
import com.chelseasystems.cr.item.*;
import com.chelseasystems.cr.config.*;
import com.chelseasystems.cr.item.MiscItemManager;
import com.chelseasystems.cs.item.*;

/**
 * put your documentation comment here
 */
public class ShippingDetailApplet extends CMSApplet {
	public static final String NAME_SPEC = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ.-' ";
	CMSCompositePOSTransaction theTxn;
	CMSShippingRequest shippingRequest;
	ItemsToShipPanel lineItemPanel = new ItemsToShipPanel();
	JCMSTextField txtFirstName = new JCMSTextField();
	JCMSTextField txtLastName = new JCMSTextField();
	ConfigMgr config = new ConfigMgr("item.cfg");
//added by Shushma for Shipping Fed-Ex
	private CMSItem item;
	public CMSItem getItem() {
		return item;
	}
	public void setItem(CMSItem item) {
		this.item = item;
	}
//Fed-Ex close
	
	private CMSMiscItem miscItem = null;
	private ArmCurrency shippingFee = null;
	private CMSMiscItem mItem = null;
	private MiscItemTemplate shipMiscItem = null;
	private EditAreaListener key;
	private String sEditAreaValue = null;
	private boolean isFromInquiry = false;
	
	//added by Shushma for Shipping Fed-Ex
	private String shipdesc;
	private ArmCurrency armCurrency;
	private MiscItemTemplate MisItemTemplate1;
	private CMSEmployee cmsEmployee;
	private long privilege;
	private MiscItemTemplate miscItemTemplate;
	private String itemBuilder;

	public long getPrivilege() {
		return privilege;
	}

	public void setPrivilege(long privilege) {
		this.privilege = privilege;
	}

	public CMSEmployee getCmsEmployee() {
		return cmsEmployee;
	}

	public void setCmsEmployee(CMSEmployee cmsEmployee) {
		this.cmsEmployee = cmsEmployee;
	}
//Fed-Ex close
	/**
	 */
	public void init() {
		try {
			jbInit();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * @exception Exception
	 */
	private void jbInit() throws Exception {
		JPanel mainPanel = new JPanel();
		JPanel jPanel1 = new JPanel();
		JCMSLabel lblFirstName = new JCMSLabel();
		JCMSLabel lblLastName = new JCMSLabel();
		key = new EditAreaListener();
		this.getContentPane().add(mainPanel, BorderLayout.CENTER);
		mainPanel.setLayout(new BorderLayout());
		mainPanel.add(jPanel1, BorderLayout.NORTH);
		mainPanel.add(lineItemPanel, BorderLayout.CENTER);
		jPanel1.setLayout(new GridBagLayout());
		lblFirstName.setText("First Name");
		lblLastName.setText("Last Name");
		jPanel1.add(lblFirstName, new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(30, 20, 0, 0), 0, 0));
		jPanel1.add(lblLastName, new GridBagConstraints(1, 0, 1, 1, 0.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(30, 10, 0, 0), 0, 0));
		jPanel1.add(txtFirstName, new GridBagConstraints(0, 1, 1, 1, 1.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(0, 20, 10, 10), 0, 5));
		jPanel1.add(txtLastName, new GridBagConstraints(1, 1, 1, 1, 1.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(0, 10, 10, 20), 0, 5));
		jPanel1.setBackground(theAppMgr.getBackgroundColor());
		mainPanel.setBackground(theAppMgr.getBackgroundColor());
		lineItemPanel.setAppMgr(theAppMgr);
		lblFirstName.setAppMgr(theAppMgr);
		lblLastName.setAppMgr(theAppMgr);
		txtFirstName.setAppMgr(theAppMgr);
		txtLastName.setAppMgr(theAppMgr);
		txtFirstName.setEnabled(false);
		txtLastName.setEnabled(false);
		txtLastName.setDocument(new TextFilter(NAME_SPEC, 30));
		txtFirstName.setDocument(new TextFilter(NAME_SPEC, 30));

	}

	/**
	 */
	public void stop() {
		getContentPane().getToolkit().removeAWTEventListener(key);
	}

	//
	public String getScreenName() {
		return (res.getString("Shipping Detail"));
	}

	//
	public String getVersion() {
		return ("$Revision: 1.2 $");
	}

	/**
	 * Start the applet.
	 */
	public void start() {
		theAppMgr.setTransitionColor(theAppMgr.getBackgroundColor());
		clear();
		sEditAreaValue = null;
		theOpr = (CMSEmployee) theAppMgr.getStateObject("OPERATOR");
		theTxn = (CMSCompositePOSTransaction) theAppMgr.getStateObject("TXN");
		if (theTxn == null && theAppMgr.getStateObject("ADD_FROM_INQUIRY") != null) {
			theTxn = (CMSCompositePOSTransaction) theAppMgr.getStateObject("THE_TXN");
			isFromInquiry = true;
		}
		if (theAppMgr.getStateObject("ADD_FROM_INQUIRY") != null)
			isFromInquiry = true;
		shippingRequest = (CMSShippingRequest) theAppMgr.getStateObject("SHIPPING_REQUEST");
		displayShippingRequest();
		lineItemPanel.setInquireMode((theAppMgr.getStateObject("SHIPPING_INQUIRY_ONLY") != null));
		pleaseContinue();
		getContentPane().getToolkit().addAWTEventListener(key, AWTEvent.KEY_EVENT_MASK);

	}
	
	//added by shushma for Fed-Ex
	public void editAreaEvent(String Command, String sEdit) {
			try {
				if (Command.equals("OPERATOR")) {
				//String s = sEdit.toUpperCase();
				setCmsEmployee(CMSEmployeeHelper.findByShortName(theAppMgr, sEdit));
				cmsEmployee=getCmsEmployee();
				if(cmsEmployee==null)
				{
					theAppMgr.showErrorDlg(res.getString("You have entered wrong Manager's id."));
					enterManagerID();
					return;
				}
				else{
				enterPassword();
				return;
				}
			}
			else if(Command.equals("PASSWORD")){
				String password=cmsEmployee.getPassword();
				if (password.equalsIgnoreCase(sEdit))
				enterShippingOverride(cmsEmployee);
				else
				{
					theAppMgr.showErrorDlg(res.getString("You have entered wrong password."));
					enterPassword();				}
				return;
			}
		}
		catch(Exception e){
			e.printStackTrace();
		}
	}
	//fed-ex close
	//added by shushma for Fed-Ex
	private void enterShippingOverride(CMSEmployee cmEmployee) {
		try {
		// TODO Auto-generated method stub
		setCmsEmployee(cmEmployee);
		setPrivilege(this.cmsEmployee.doGetPrivileges());
		miscItemTemplate=getMisItemTemplate1();
		if(privilege==11||privilege==1||privilege==3||privilege==9){
			String str[]={miscItemTemplate.getMiscItemDescription()};
			miscItemTemplate.setDescription(str);
			miscItemTemplate.setCanOverrideAmount(true);
			item=getItem();
			item.setDescription(str[0]);
			miscItem = new CMSMiscItem(shipMiscItem.getKey(), item);
			applyMiscItemTemplate();
		}
		else{
			theAppMgr.showErrorDlg(res.getString("You are not authorized for Shipping cost override."));
			enterManagerID();
			return;
			}
	} 
		catch (Exception e) {
	}
			setShippingFee(miscItemTemplate.getAmount());
			return;
			}
		

	// return to the builders the txn so they may do intermediate validations
	public BusinessObject getDestinationObjectForCurrentBuilder() {
		return (theTxn);
	}

	private void enterPassword() {
		// TODO Auto-generated method stub
		theAppMgr.setSingleEditArea(res.getString("Please enter your secret password.")
		        , "PASSWORD", theAppMgr.PASSWORD_MASK);
			theAppMgr.setEditAreaFocus();
			return;
	}
	//fed-ex close
	/**
	 * ks: added this to set the shipping fee entered by the user
	 * @param Command String
	 * @param currency ArmCurrency
	 */
	public void editAreaEvent(String Command, ArmCurrency currency) {
		try {
			if (Command.startsWith("SHIP_FEE")) {
			//changed by shushma for Fed-Ex 
			//whole statements are encaplsulated into below method
				buildShip(currency);
				/*
				if (mItem == null) {
					miscItem.setUnitPrice(currency);
					if (theAppMgr.getStateObject("TXN_MODE") != null && ((Integer) theAppMgr.getStateObject("TXN_MODE")).intValue() == InitialSaleApplet.PRE_SALE_OPEN) {
						CMSPresaleLineItem presaleLineItem = theTxn.addPresaleMiscItem(miscItem);
						presaleLineItem.setShippingRequest(shippingRequest);
						if (presaleLineItem.isItemTaxable())
							presaleLineItem.setTaxableMiscLineItem(miscItem);
						//            this.shippingRequest.doAddPresaleLineItem(presaleLineItem);

					} else {
						CMSSaleLineItem miscSalelineItem = (CMSSaleLineItem) theTxn.addSaleMiscItem(miscItem);
						miscSalelineItem.setShippingRequest(shippingRequest);
						if (miscSalelineItem.isItemTaxable())
							miscSalelineItem.setTaxableMiscLineItem(miscItem);
						//            this.shippingRequest.doAddLineItem(miscSalelineItem);
					}
					shippingFee = currency;
					//          theAppMgr.addStateObject("ARM_SHIPPING_FEE", miscItem);
				}
				applyItemsToRequest();
				this.shippingRequest.setShippingFee(shippingFee);
				theAppMgr.removeStateObject("SHIPPING_REQUEST");
				theAppMgr.removeStateObject("TXN");
				theAppMgr.removeStateObject("SHIPPING_INQUIRY_ONLY");
				theAppMgr.fireButtonEvent("OK");
			*/}
			
		} catch (Exception ex) {
			theAppMgr.showExceptionDlg(ex);
			try {
				if (miscItem.getUnitPrice().equals(currency) == false) {
					setShippingFee(shippingFee);
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
	////added by shushma for Fed-Ex
	public void buildShip(ArmCurrency currency) throws Exception{

		if (mItem == null) {
			miscItem.setUnitPrice(currency);
			if (theAppMgr.getStateObject("TXN_MODE") != null && ((Integer) theAppMgr.getStateObject("TXN_MODE")).intValue() == InitialSaleApplet.PRE_SALE_OPEN) {
				CMSPresaleLineItem presaleLineItem = theTxn.addPresaleMiscItem(miscItem);
				presaleLineItem.setShippingRequest(shippingRequest);
				if (presaleLineItem.isItemTaxable())
					presaleLineItem.setTaxableMiscLineItem(miscItem);
				//            this.shippingRequest.doAddPresaleLineItem(presaleLineItem);

			} else {
				CMSSaleLineItem miscSalelineItem = (CMSSaleLineItem) theTxn.addSaleMiscItem(miscItem);
				miscSalelineItem.setShippingRequest(shippingRequest);
				if (miscSalelineItem.isItemTaxable())
					miscSalelineItem.setTaxableMiscLineItem(miscItem);
				//            this.shippingRequest.doAddLineItem(miscSalelineItem);
			}
			shippingFee = currency;
			//          theAppMgr.addStateObject("ARM_SHIPPING_FEE", miscItem);
		}
		applyItemsToRequest();
		this.shippingRequest.setShippingFee(shippingFee);
		theAppMgr.removeStateObject("SHIPPING_REQUEST");
		theAppMgr.removeStateObject("TXN");
		theAppMgr.removeStateObject("SHIPPING_INQUIRY_ONLY");
		theAppMgr.fireButtonEvent("OK");
	
	}

	/**
	 *
	 */
	private void pleaseContinue() {
		if (theAppMgr.getStateObject("SHIPPING_INQUIRY_ONLY") != null) {
			theAppMgr.showMenu(MenuConst.PREV_ONLY, theOpr);
			theAppMgr.setSingleEditArea(res.getString("Select option."));
			theAppMgr.setEditAreaFocus();
		} else {
			theAppMgr.showMenu(MenuConst.SHIPPING_DETAIL_SELECT_ALL, theOpr);
			theAppMgr.setSingleEditArea(res.getString("Select items to ship."));
			theAppMgr.setEditAreaFocus();
		}
	}

	/**
	 *
	 */
	private void clear() {
		txtFirstName.setText("");
		txtLastName.setText("");
		lineItemPanel.clear();
	}

	/**
	 *
	 */
	private void displayShippingRequest() {
		txtFirstName.setText(shippingRequest.getFirstName());
		txtLastName.setText(shippingRequest.getLastName());
		POSLineItem[] posLineItems = theTxn.getLineItemsArray();
		//        SaleLineItem[] saleLineItems = new SaleLineItem[posLineItems.length];
		//        System.arraycopy(posLineItems, 0, saleLineItems, 0, posLineItems.length);
		lineItemPanel.loadModel(posLineItems, shippingRequest);
	}

	/**
	 *
	 * @param Command String
	 * @param obj Object
	 */
	public void objectEvent(String Command, Object obj) {
		if (Command.equals("MISC_ITEM")) {
			if (obj != null) {
				try {
					if (theTxn != null) {
						theTxn.setPostAndPack(false, new ArmCurrency(0.0));
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
				try {
					//___Tim: 1864
					CMSItemWrapper itemWrapper = (CMSItemWrapper)obj;
					CMSMiscItem item = itemWrapper.getMiscItem();
//					CMSMiscItem item = (CMSMiscItem) obj;
					if (theAppMgr.getStateObject("TXN_MODE") != null && ((Integer) theAppMgr.getStateObject("TXN_MODE")).intValue() == InitialSaleApplet.PRE_SALE_OPEN) {
						CMSPresaleLineItem presaleLineItem = theTxn.addPresaleMiscItem(miscItem);
						presaleLineItem.setExtendedBarCode(itemWrapper.getExtendedBarCode());
						presaleLineItem.setShippingRequest(shippingRequest);
						if (presaleLineItem.isItemTaxable())
							presaleLineItem.setTaxableMiscLineItem(item);
						this.shippingRequest.doAddPresaleLineItem(presaleLineItem);
					} else {
						CMSSaleLineItem miscSalelineItem = (CMSSaleLineItem) theTxn.addSaleMiscItem(item);
						miscSalelineItem.setExtendedBarCode(itemWrapper.getExtendedBarCode());
						miscSalelineItem.setShippingRequest(shippingRequest);
						if (miscSalelineItem.isItemTaxable())
							miscSalelineItem.setTaxableMiscLineItem(item);
						this.shippingRequest.doRemoveLineItem(miscSalelineItem);
					}
				} catch (BusinessRuleException bx) {
					theAppMgr.showErrorDlg(res.getString(bx.getMessage()));
				}
			}
		}
	}

	/**
	 * ks: Reads the item.cfg file and build miscItemTemplate object for the Item bldr
	 */
	private boolean buildShippingFeeItem() {
		
	//changed by shushma for Fed-Ex
		try {
			shippingFee = null;
			String itemId = config.getString("SHIP.BASE_ITEM");
			itemBuilder = config.getString("ITEM.BUILDER");
			MiscItemTemplate[] miscItemTemplate = MiscItemManager.getInstance().getMiscItemsArray(itemId);
			if (miscItemTemplate != null && miscItemTemplate.length > 0) {
				for (int i = 0; i < miscItemTemplate.length; i++) {
					if (miscItemTemplate[i].getBaseItemId().trim().equals(itemId.trim())) {
						String str[]={"Shipping Charge"};
						miscItemTemplate[i].setDescription(str);
						miscItemTemplate[i].setMiscItemDescription("Shipping Charge");
						miscItemTemplate[i].setCanOverrideAmount(true);
						shipMiscItem = miscItemTemplate[i];
						break;
					} else {
						continue;
					}
				}
				if("US".equalsIgnoreCase(Version.CURRENT_REGION)){
				setItem(CMSItemHelper.findByBarCode(theAppMgr, shipMiscItem.getBaseItemId(), theStore.getId()));
				if(item == null){
					item = new CMSItem(shipMiscItem.getBaseItemId());
					item.setBarCode(item.getBarCode());
					item.setDescription(shipMiscItem.getMiscItemDescription());
				}
				////added by shushma for Shipping Fed-Ex
				String shipping = config.getString("SHIPPING_OPTIONS");
				StringTokenizer strTok = new StringTokenizer(shipping, ",");
				int n=strTok.countTokens();
				GenericChooserRow[] availMiscItemTemplates = new GenericChooserRow[strTok.countTokens()];
				for(int k=0;k<n;k++){
					String misckey = (String) strTok.nextToken();
					shipdesc = config.getString(misckey + ".DESC");
					double amt = Double.parseDouble(config.getString(misckey + ".AMOUNT"));
					armCurrency=new ArmCurrency(amt);
					availMiscItemTemplates[k] = new GenericChooserRow(new String[]{shipdesc},amt);
				}
				GenericChooseFromTableDlg dlg = new GenericChooseFromTableDlg(theAppMgr.getParentFrame(), theAppMgr, availMiscItemTemplates, new String[] { (res.getString("FedEx OPTIONS")) });
				dlg.getTable().getColumnModel().getColumn(0).setCellRenderer(GenericChooseFromTableDlg.getCenterRenderer());
				dlg.setVisible(true);

				if (dlg.isOK()) {
					Object curr=dlg.getSelectedRow().getRowKeyData();
					Object[] desc=dlg.getSelectedRow().getDisplayRow();
					armCurrency=new ArmCurrency((Double) curr);					
					shipMiscItem.setAmount(armCurrency);
					shipMiscItem.setMiscItemDescription((String) desc[0]);
					if(((String) desc[0]).equals("Shipping Cost Override")){
						setMisItemTemplate1(shipMiscItem);
						enterManagerID();
						return false;
					}
					else{
						int i=((String) desc[0]).indexOf(":");
						String str1[]={((String) desc[0]).substring(0,i)};
						shipMiscItem.setDescription(str1);
						shipMiscItem.setCanOverrideAmount(false);
						item=getItem();
							item.setDescription(str1[0]);
					}
			//change Fed-Ex closed
			
					miscItem = new CMSMiscItem(shipMiscItem.getKey(), item);
					try {
						applyMiscItemTemplate();
					} catch (Exception e) {
					}
					buildShip(shipMiscItem.getAmount());
					//theAppMgr.buildObject(this, "MISC_ITEM", itemBuilder, shipMiscItem);
					return false;
					}
					//added by shushma for Shipping Fed-Ex
				else {
					theAppMgr.showErrorDlg(res.getString("You did not select any shipping options, item was not added."));
						shippingRequest.delete();
						theAppMgr.removeStateObject("TXN");
						theAppMgr.removeStateObject("SHIPPING_INQUIRY_ONLY");
						theAppMgr.fireButtonEvent("CANCEL");
						return true;
					}
				} 
			else{
				try {
					item = null;
					try {
						item = CMSItemHelper.findByBarCode(theAppMgr, shipMiscItem.getBaseItemId(), theStore.getId());
					} catch (Exception e) {
						//e.printStackTrace();
					}
					if (item == null) {
						item = new CMSItem(shipMiscItem.getBaseItemId());
						item.setBarCode(item.getBarCode());
						item.setDescription(shipMiscItem.getMiscItemDescription());
					}
				} catch (Exception ex) {
					theAppMgr.showErrorDlg(ex.getMessage());
				}
				if (item != null) {
					miscItem = new CMSMiscItem(shipMiscItem.getKey(), item);
					try {
						applyMiscItemTemplate();
					} catch (Exception e) {
					}
					setShippingFee(shipMiscItem.getAmount());
					return false;
				} else {
					theAppMgr.showErrorDlg(res.getString("Cannot find item") + " " + shipMiscItem.getBaseItemId());
				}
			}
			}	
		}
		catch (Exception ex) {
			theAppMgr.showErrorDlg(ex.getMessage());
			return false;
		}
		

		
		
		return false;
	}
	
	//added by shushma for Shipping Fed-Ex
	private void enterManagerID() {
		// TODO Auto-generated method stub
		theAppMgr.setSingleEditArea(res.getString("Enter Manager's ID."), "OPERATOR", theAppMgr.SSN_MASK);
		theAppMgr.setEditAreaFocus();
		return;
		}
//enterManagerID close
	/**
	 * Set the EditArea to prompt to enter shipping fee
	 * @param shippingFee ArmCurrency
	 */
	private void setShippingFee(ArmCurrency shippingFee) {
		if (theAppMgr.getStateObject("ADD_FROM_INQUIRY") == null) {
			sEditAreaValue = "" + shippingFee.getDoubleValue();
			theAppMgr.setSingleEditArea(res.getString("Enter Shipping Fee."), "SHIP_FEE", shippingFee, theAppMgr.CURRENCY_MASK);
		}
	}

	/**
	 *
	 * @param anEvent
	 */
	public void appButtonEvent(CMSActionEvent anEvent) {
		String sAction = anEvent.getActionCommand();
		try {
			if (sAction.equals("OK")) {
				//Check if any items are selected to the shipping request
				if (lineItemPanel.getSelectedLineItems() != null && lineItemPanel.getSelectedLineItems().length == 0) {
					theAppMgr.showErrorDlg(res.getString("Atleast one item have to be selected to continue."));
					anEvent.consume();
					return;
				}
				if (sEditAreaValue != null && theAppMgr.getStateObject("ARM_SHIPPING_FEE") == null) {
				editAreaEvent("SHIP_FEE", new ArmCurrency(this.sEditAreaValue));
				}
				//ks: added this to set the shipping fee entered by the user. Build shipping fee misc fee
				mItem = (CMSMiscItem) theAppMgr.getStateObject("ARM_SHIPPING_FEE");
				if (mItem == null && isFromInquiry == false) {
					anEvent.consume();
			//added by shushma for Shipping Fed-Ex
					boolean ship=buildShippingFeeItem();
					if(ship){
						
						return;
					}
					//changed for Fed-Ex close
				}
				applyItemsToRequest();
				if (isFromInquiry)
					theAppMgr.fireButtonEvent("OK");
			} else if (sAction.equals("SELECT_ALL")) {
				lineItemPanel.selectAllRows(true);
				theAppMgr.showMenu(MenuConst.SHIPPING_DETAIL_DESELECT_ALL, theOpr);
				anEvent.consume();
			} else if (sAction.equals("DE_SELECT_ALL")) {
				lineItemPanel.selectAllRows(false);
				theAppMgr.showMenu(MenuConst.SHIPPING_DETAIL_SELECT_ALL, theOpr);
				anEvent.consume();
			} else if (sAction.equals("CANCEL")) {
				anEvent.consume();
				if (theAppMgr.showOptionDlg(res.getString("Cancel Shipping Request"), res.getString("Are you sure you want to cancel this Shipping Request?"))) {
					shippingRequest.delete();
					theAppMgr.removeStateObject("TXN");
					theAppMgr.removeStateObject("SHIPPING_INQUIRY_ONLY");
					theAppMgr.fireButtonEvent("CANCEL");
				}
			}
		} catch (BusinessRuleException r) {
			anEvent.consume();
			theAppMgr.showErrorDlg(res.getString(r.getMessage()));
		} catch (Exception e) {
			theAppMgr.showExceptionDlg(e);
		}
	}

	/**
	 *
	 * @exception BusinessRuleException
	 */
	private void applyItemsToRequest() throws BusinessRuleException {
		POSLineItem[] selected = lineItemPanel.getSelectedLineItems();
		if (selected != null)
			for (int i = 0; i < selected.length; i++) {
				if (selected[i] instanceof SaleLineItem)
					((SaleLineItem) selected[i]).setShippingRequest(shippingRequest);
				else if (selected[i] instanceof CMSConsignmentLineItem)
					((CMSConsignmentLineItem) selected[i]).setShippingRequest(shippingRequest);
				else if (selected[i] instanceof CMSPresaleLineItem)
					((CMSPresaleLineItem) selected[i]).setShippingRequest(shippingRequest);
			}
		POSLineItem[] unSelected = lineItemPanel.getUnSelectedLineItems();
		if (unSelected != null)
			for (int i = 0; i < unSelected.length; i++) {
				if (unSelected[i] instanceof SaleLineItem)
					((SaleLineItem) unSelected[i]).clearShippingRequest();
				else if (unSelected[i] instanceof CMSConsignmentLineItem)
					((CMSConsignmentLineItem) unSelected[i]).clearShippingRequest();
				else if (unSelected[i] instanceof CMSPresaleLineItem)
					((CMSPresaleLineItem) unSelected[i]).clearShippingRequest();
			}
	}

	/**
	 * callback when <b>Page Down</b> icon is clicked
	 */
	public void pageDown(MouseEvent e) {
		lineItemPanel.nextPage();
		theAppMgr.showPageNumber(e, lineItemPanel.getCurrentPageNumber() + 1, lineItemPanel.getTotalPages());
	}

	/**
	 * callback when <b>Page Up</b> icon is clicked
	 */
	public void pageUp(MouseEvent e) {
		lineItemPanel.prevPage();
		theAppMgr.showPageNumber(e, lineItemPanel.getCurrentPageNumber() + 1, lineItemPanel.getTotalPages());
	}

	/**
	 * @return
	 */
	public boolean isHomeAllowed() {
		return (theAppMgr.showOptionDlg(res.getString("Cancel Transaction"), res.getString("Are you sure you want to cancel this transaction?")));
	}

	/**
	 *
	 * @exception BusinessRuleException
	 */
	private void applyMiscItemTemplate() throws BusinessRuleException {
		if (!shipMiscItem.getCanOverrideAmount())
			miscItem.setUnitPrice(shipMiscItem.getAmount());
		if (!shipMiscItem.getCanOverrideTaxable())
			miscItem.setTaxable(new Boolean(shipMiscItem.getTaxable()));
		if (!shipMiscItem.getCanOverrideDescription()) {
			String[] descriptions = shipMiscItem.getDescription();
			if (descriptions != null && descriptions.length > 0 && shipMiscItem.getDescIdx() > -1)
				miscItem.setDescription(descriptions[shipMiscItem.getDescIdx()]);
		}
		if (!shipMiscItem.getCanOverrideComment())
			miscItem.setComment(shipMiscItem.getComment());
		miscItem.setGLAccount(shipMiscItem.getGLaccount());
		miscItem.setDefaultQuantity(new Integer(shipMiscItem.getDefaultQty()));
	}

////added by shushma for Shipping Fed-Ex
	public void setMisItemTemplate1(MiscItemTemplate misItemTemplate1) {
		MisItemTemplate1 = misItemTemplate1;
	}

	public MiscItemTemplate getMisItemTemplate1() {
		return MisItemTemplate1;
	}
//setters-getters closed
	private class EditAreaListener implements AWTEventListener {

		/**
		 * put your documentation comment here
		 * @param awtEvent
		 */
		public void eventDispatched(AWTEvent awtEvent) {
			KeyEvent keyEvent;
			try {
				// Typecast to KeyEvent
				keyEvent = (KeyEvent) awtEvent;
			} catch (ClassCastException ce) {
				return;
			}
			if (keyEvent.getComponent() instanceof javax.swing.JPasswordField) {
				JPasswordField jField = (JPasswordField) keyEvent.getComponent();
				sEditAreaValue = jField.getText();
			}
		}
	}

} // end Applet

