/*
 * put your module comment here
 * formatted with JxBeauty (c) johann.langhofer@nextra.at
 */

package com.chelseasystems.cs.swing.panel;

import javax.swing.*;
import com.chelseasystems.cr.swing.bean.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ResourceBundle;
import com.chelseasystems.cr.swing.layout.RolodexLayout;
import com.chelseasystems.cr.appmgr.IApplicationManager;
import com.chelseasystems.cs.address.*;
import java.util.Vector;
import com.chelseasystems.cs.pos.CMSShippingRequest;
import com.chelseasystems.cr.rules.*;

/*
 * History:
 * +------+------------+-----------+-----------+----------------------------------------------------+ |
 * Ver# | Date | By | Defect # | Description |
 * +------+------------+-----------+-----------+----------------------------------------------------+ |
 * 2 | 09-01-2005 | Manpreet | N/A | addMgr.getCountryList() changed to | | | | | |
 * addMgr.getAddressFormats() |
 * +------+------------+-----------+-----------+----------------------------------------------------+ |
 * 1 | 03-05-2005 | Manpreet | N/A | POS_104665_FS_CustomerManagement_Rev2 |
 * +------+------------+-----------+-----------+----------------------------------------------------+
 */
/**
 * <p>
 * Title:AddressPanel.java
 * </p>
 * 
 * <p>
 * Description: Add/View Address
 * </p>
 * 
 * <p>
 * Copyright: Copyright (c) 2005
 * </p>
 * 
 * <p>
 * Company: Skillnet inc.
 * </p>
 * 
 * @author Manpreet S Bawa
 * @version 1.0
 */
public class AddressPanel extends JPanel implements ItemListener, ActionListener {
	private JCMSLabel lblCountry;

	public JCMSComboBox cbCountry;

	private JCMSCheckBox chkboxUsePrimary;

	public JPanel pnlBottom;

	private RolodexLayout cardLayout;

	private JPanel pnlTop = new JPanel();

	private final ResourceBundle RESOURCE;

	private AddressMgr addMgr;

	private Address addressCurrent;

	private IApplicationManager theAppMgr;

	public BaseAddressPanel pnlBaseAddress;

	private String addressPanelType;

	private String addressFormat;

	private boolean lastIsShowing = false;

	/**
	 * put your documentation comment here
	 */
	public AddressPanel() {
		this(BaseAddressPanel.FULL_ADDRESS_PANEL);
	}

	/**
	 * put your documentation comment here
	 * 
	 * @param String
	 *          addressPanelType
	 */
	public AddressPanel(String addressPanelType) {
		this.addressPanelType = addressPanelType;
		RESOURCE = com.chelseasystems.cr.util.ResourceManager.getResourceBundle();
		try {
			addMgr = new AddressMgr();
			jbInit();
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
							// MSB - 09/01/2005 -- Changed naming convention in AddressMgr
							// Class
							// cardLayout.show(pnlBottom, addMgr.getDefaultCountry());
							// cbCountry.setSelectedItem(addMgr.getDefaultCountry());
							addressFormat = addMgr.getDefaultAddressFormat();
							cardLayout.show(pnlBottom, addressFormat);
							cbCountry.setSelectedItem(addressFormat);
							if (addressFormat.indexOf("EUR") != -1)
								lblCountry.setText(RESOURCE.getString("Address Format"));
							else
								lblCountry.setText(RESOURCE.getString("Country"));
						}
					});
				}
			});

		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	/**
	 * put your documentation comment here
	 * 
	 * @param theAppMgr
	 */
	public void setAppMgr(IApplicationManager theAppMgr) {
		this.theAppMgr = theAppMgr;
		this.setBackground(theAppMgr.getBackgroundColor());
		pnlTop.setBackground(theAppMgr.getBackgroundColor());
		pnlBottom.setBackground(theAppMgr.getBackgroundColor());
		for (int iCtr = 0; iCtr < pnlBottom.getComponentCount(); iCtr++) {
			BaseAddressPanel baseAddress = (BaseAddressPanel) pnlBottom.getComponent(iCtr);
			baseAddress.setAppMgr(theAppMgr);
		}
		chkboxUsePrimary.setAppMgr(theAppMgr);
		// ks: Set the country font
		lblCountry.setFont(theAppMgr.getTheme().getLabelFont());
		cbCountry.setAppMgr(theAppMgr);
	}

	/**
	 * put your documentation comment here
	 * 
	 * @exception Exception
	 */
	private void jbInit() throws Exception {
		Vector vecPanels;
		Vector vecCountries;
		String sTmp;
		Class cls;
		cardLayout = new RolodexLayout();
		setBorder(BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(), RESOURCE
				.getString("Address")));
		this.setLayout(new BorderLayout());
		// TD
		this.setPreferredSize(new Dimension(370, 400));
		lblCountry = new JCMSLabel();
		cbCountry = new JCMSComboBox();
		chkboxUsePrimary = new JCMSCheckBox();
		pnlBottom = new JPanel();
		lblCountry.setText(RESOURCE.getString("Country"));
		chkboxUsePrimary.setHorizontalTextPosition(SwingConstants.LEADING);
		chkboxUsePrimary.setText(RESOURCE.getString("Use As Primary"));
		pnlBottom.setLayout(cardLayout);
		// Changed naming convention MSB - 09/01/05
		// vecPanels = addMgr.getCountryPanels();
		// vecCountries = addMgr.getCountryList();
		vecPanels = addMgr.getAddressPanels();
		vecCountries = addMgr.getAddressFormats();
		cbCountry.setModel(new DefaultComboBoxModel(vecCountries));
		for (int iCtr = 0; iCtr < vecPanels.size(); iCtr++) {
			sTmp = (String) vecPanels.elementAt(iCtr);
			if (sTmp == null)
				continue;
			cls = Class.forName(sTmp);
			pnlBaseAddress = (BaseAddressPanel) cls.newInstance();
			pnlBaseAddress.setAddressFormat((String) vecCountries.elementAt(iCtr));
			pnlBaseAddress.setAddressTypes(addMgr.getAddressTypes(), addMgr.getAddressTypeKeys());
			pnlBaseAddress.setPhoneTypes(addMgr.getPhoneTypes(), addMgr.getPhoneTypeKeys());
			if (!addressPanelType.trim().equalsIgnoreCase(BaseAddressPanel.FULL_ADDRESS_PANEL))
				pnlBaseAddress.resetAddressPanelType(addressPanelType);
			pnlBottom.add(pnlBaseAddress, vecCountries.elementAt(iCtr));
		}

		pnlTop.setLayout(new GridBagLayout());
		pnlTop.setPreferredSize(new Dimension(144, 20));
		// pnlBottom.setPreferredSize(new Dimension(144, 400));
		pnlTop.add(lblCountry, new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0, GridBagConstraints.WEST,
				GridBagConstraints.NONE, new Insets(2, 2, 3, 0), 0, 0));// 12, 0));
		pnlTop.add(cbCountry, new GridBagConstraints(1, 0, 1, 1, 1.0, 0.0, GridBagConstraints.WEST,
				GridBagConstraints.NONE, new Insets(2, 2, 3, 0), 0, 0));
		if (this.addressPanelType.trim().equalsIgnoreCase(BaseAddressPanel.FULL_ADDRESS_PANEL)) {
			pnlTop.add(chkboxUsePrimary, new GridBagConstraints(2, 0, 1, 1, 0.0, 0.0,
					GridBagConstraints.EAST, GridBagConstraints.NONE, new Insets(2, 3, 3, 1), 1, 1));
		}
		chkboxUsePrimary.addActionListener(this);
		cbCountry.addItemListener(this);
		this.add(pnlTop, java.awt.BorderLayout.NORTH);
		this.add(pnlBottom, java.awt.BorderLayout.CENTER);
	}

	/**
	 * put your documentation comment here
	 * 
	 * @return
	 */
	public String getCountry() {
		return (String) cbCountry.getSelectedItem();
	}

	/**
	 * put your documentation comment here
	 * 
	 * @param sCountry
	 */
	public void setCountry(String sCountry) {
		cbCountry.setSelectedItem(sCountry);
		addressFormat = sCountry;
	}

	/**
	 * put your documentation comment here
	 * 
	 * @param bIsPrimary
	 */
	public void setPrimary(boolean bIsPrimary) {
		chkboxUsePrimary.setSelected(bIsPrimary);
		BaseAddressPanel pnlPrimaryAddress = (BaseAddressPanel) cardLayout.getCurrent(pnlBottom);
		pnlPrimaryAddress.setPrimary(bIsPrimary);
		for (int iCtr = 0; iCtr < pnlBottom.getComponentCount(); iCtr++) {
			BaseAddressPanel pnlOthers = (BaseAddressPanel) pnlBottom.getComponent(iCtr);
			if (!pnlOthers.equals(pnlPrimaryAddress))
				pnlOthers.setPrimary(false);
		}
	}

	/**
	 * put your documentation comment here
	 * 
	 * @return
	 */
	public boolean isPrimary() {
		return chkboxUsePrimary.isSelected();
	}

	/**
	 * put your documentation comment here
	 * 
	 * @return
	 * @exception BusinessRuleException
	 */
	public boolean isValidAddress() throws BusinessRuleException {
		BaseAddressPanel pnlPrimaryAddress = (BaseAddressPanel) cardLayout.getCurrent(pnlBottom);
		return pnlPrimaryAddress.isAllScreenInputValid();
	}

	/**
	 * put your documentation comment here
	 * 
	 * @return
	 * @exception BusinessRuleException
	 */
	private boolean isValidState() throws BusinessRuleException {
		// Only do state validation for USAAddressPanel
		BaseAddressPanel pnlPrimaryAddress = (BaseAddressPanel) cardLayout.getCurrent(pnlBottom);
		return pnlPrimaryAddress.isStateValid();
	}

	/**
	 * put your documentation comment here
	 * 
	 * @return
	 */
	public int containsDuplicatePhone() {
		BaseAddressPanel addressPanel = (BaseAddressPanel) cardLayout.getCurrent(pnlBottom);
		if (addressPanel == null)
			return -1;
		if (addressPanel.getPhone1().length() > 0
				&& addressPanel.getPhone2().length() > 0
				&& addressPanel.getSelectedPhoneType1().equalsIgnoreCase(
						addressPanel.getSelectedPhoneType2()))
			return 2;
		else if ((addressPanel.getPhone1().length() > 0 && addressPanel.getPhone3().length() > 0 && addressPanel
				.getSelectedPhoneType1().equalsIgnoreCase(addressPanel.getSelectedPhoneType3()))
				|| (addressPanel.getPhone2().length() > 0 && addressPanel.getPhone3().length() > 0 && addressPanel
						.getSelectedPhoneType2().equalsIgnoreCase(addressPanel.getSelectedPhoneType3())))
			return 3;
		return -1;
	}

	/**
	 * put your documentation comment here
	 * 
	 * @param listener
	 */
	public void addItemListener(ItemListener listener) {
		for (int iCtr = 0; iCtr < pnlBottom.getComponentCount(); iCtr++) {
			if (pnlBottom.getComponent(iCtr) instanceof BaseAddressPanel) {
				((BaseAddressPanel) pnlBottom.getComponent(iCtr)).addItemListener(listener);
			}
		}
	}

	public void removeItemListener(ItemListener listener) {
		for (int iCtr = 0; iCtr < pnlBottom.getComponentCount(); iCtr++) {
			if (pnlBottom.getComponent(iCtr) != null
					&& pnlBottom.getComponent(iCtr) instanceof BaseAddressPanel) {
				((BaseAddressPanel) pnlBottom.getComponent(iCtr)).removeItemListener(listener);
			}
		}
	}

	/**
	 * put your documentation comment here
	 */
	public void requestFocusToPrimaryPhone() {
		BaseAddressPanel addressPanel = (BaseAddressPanel) cardLayout.getCurrent(pnlBottom);
		addressPanel.requestFocusToPrimaryPhone();
	}

	/**
	 * put your documentation comment here
	 */
	public void requestFocusToSecondaryPhone() {
		BaseAddressPanel addressPanel = (BaseAddressPanel) cardLayout.getCurrent(pnlBottom);
		addressPanel.requestFocusToSecondaryPhone();
	}

	/**
	 * put your documentation comment here
	 */
	public void requestFocusToTernaryPhone() {
		BaseAddressPanel addressPanel = (BaseAddressPanel) cardLayout.getCurrent(pnlBottom);
		addressPanel.requestFocusToTernaryPhone();
	}

	/**
	 * put your documentation comment here
	 * 
	 * @param address
	 */
	public void setAddress(Address address) {
		// addressCurrent = address;
		cbCountry.removeItemListener(this);
		// setCountry(address.getCountry());
		// setCountry(address.getAddressFormat());
		// cbCountry.addItemListener(this);
		setPrimary(address.isUseAsPrimary());
		// ---- addMgr.getCountryList() changed to addMgr.getAddressFormats() -- MSB
		// 09/01/05
		if (address.getAddressFormat() != null && !address.getAddressFormat().trim().equals("")
				&& addMgr.getAddressFormats().contains(address.getAddressFormat())) {
			// cardLayout.show(pnlBottom, address.getAddressFormat());
		} else if (address.getCountry() != null && !address.getCountry().trim().equals("")
				&& addMgr.getAddressFormats().contains(address.getCountry())) {
				address.setAddressFormat(address.getCountry());
			// cardLayout.show(pnlBottom, address.getCountry());
		} else if (address.getCountry() != null && !address.getCountry().trim().equals("")
				&& addMgr.getAddressFormats().contains("EUROPE")) {
			// no matching address format exists, the country data exists but
			// does not match in the country list, so it may be Europe
			address.setAddressFormat("EUROPE");
			// cardLayout.show(pnlBottom, "EUROPE");
		} else {
			address.setAddressFormat(addMgr.getDefaultAddressFormat());
			// cardLayout.show(pnlBottom, addMgr.getDefaultCountry());
		}
		setCountry(address.getAddressFormat());
		cbCountry.addItemListener(this);
		cardLayout.show(pnlBottom, address.getAddressFormat());
		BaseAddressPanel addressPanel = (BaseAddressPanel) cardLayout.getCurrent(pnlBottom);
		addressPanel.setCustomerAddress(address);
	}

	/**
	 * put your documentation comment here
	 * 
	 * @param shippingRequest
	 */
	public void setShippingRequest(CMSShippingRequest shippingRequest) {
		// addressCurrent = address;
		cbCountry.removeItemListener(this);
		// setCountry(address.getCountry());
		// setCountry(address.getAddressFormat());
		// cbCountry.addItemListener(this);
		if (shippingRequest.getAddressFormat() != null
				&& !shippingRequest.getAddressFormat().trim().equals("")
				&& addMgr.getAddressFormats().contains(shippingRequest.getAddressFormat())) {
			cardLayout.show(pnlBottom, shippingRequest.getAddressFormat());
		} else if (shippingRequest.getCountry() != null
				&& !shippingRequest.getCountry().trim().equals("")
				&& addMgr.getAddressFormats().contains(shippingRequest.getCountry())) {
			shippingRequest.setAddressFormat(shippingRequest.getCountry());
			cardLayout.show(pnlBottom, shippingRequest.getCountry());
		} else if (shippingRequest.getCountry() != null
				&& !shippingRequest.getCountry().trim().equals("")
				&& addMgr.getAddressFormats().contains("EUROPE")) {
			// no matching address format exists, the country data exists but
			// does not match in the country list, so it may be Europe
			shippingRequest.setAddressFormat("EUROPE");
			// cardLayout.show(pnlBottom, "EUROPE");
		} else {
			shippingRequest.setAddressFormat(addMgr.getDefaultAddressFormat());
			// cardLayout.show(pnlBottom, addMgr.getDefaultCountry());
		}
		setCountry(shippingRequest.getAddressFormat());
		cbCountry.addItemListener(this);
		if (pnlBottom != null) {
			cardLayout.show(pnlBottom, shippingRequest.getAddressFormat());
			BaseAddressPanel addressPanel = (BaseAddressPanel) cardLayout.getCurrent(pnlBottom);
			addressPanel.setShippingRequest(shippingRequest);
		}
	}

	/**
	 * put your documentation comment here
	 */
	public void reset() {
		for (int iCtr = 0; iCtr < pnlBottom.getComponentCount(); iCtr++) {
			if (pnlBottom.getComponent(iCtr) instanceof BaseAddressPanel)
				((BaseAddressPanel) pnlBottom.getComponent(iCtr)).reset();
		}
		// - MSB 11/03/05 Reset Address Panel to Default address panel
		// not first panel in list.
		cbCountry.removeItemListener(this);
		addressFormat = addMgr.getDefaultAddressFormat();
		if (addressFormat != null) {
			cardLayout.show(pnlBottom, addressFormat);
			cbCountry.setSelectedItem(addressFormat);
		} else if (cbCountry.getItemCount() > 0)
			cbCountry.setSelectedIndex(0);
		cbCountry.addItemListener(this);
		chkboxUsePrimary.setSelected(false);
		}

	/**
	 * put your documentation comment here
	 * 
	 * @param bEnabled
	 */
	public void setPrimaryEnabled(boolean bEnabled) {
	//Mayuri Edhara :: assigned an object instead of calling the method twice.
		Address getAddress = getAddress();
		if (getAddress() != null && !getAddress().isUseAsPrimary())
			chkboxUsePrimary.setEnabled(bEnabled);
		else
			chkboxUsePrimary.setEnabled(false);
	}

	/**
	 * put your documentation comment here
	 * 
	 * @param bEnabled
	 */
	public void setEnabled(boolean bEnabled) {
		cbCountry.setEnabled(bEnabled);
		setPrimaryEnabled(bEnabled);
		BaseAddressPanel addressPanel = (BaseAddressPanel) cardLayout.getCurrent(pnlBottom);
		for (int iCtr = 0; iCtr < addressPanel.getComponentCount(); iCtr++) {
			if (!(addressPanel.getComponent(iCtr) instanceof JCMSLabel))
				addressPanel.getComponent(iCtr).setEnabled(bEnabled);
		}
	}

	public void setModifyEnabled(boolean bEnabled) {
		this.setEnabled(bEnabled);
		//Fix for 1908 dated 2/13/2008 Customer Management - If you add address and select OTHER as country, you cannot go back and modify it later.
		// cbCountry.setEnabled(false);
		BaseAddressPanel addressPanel = (BaseAddressPanel) cardLayout.getCurrent(pnlBottom);
		addressPanel.setModifyEnabled(bEnabled);
	}

	/**
	 * put your documentation comment here
	 * 
	 * @return
	 */
	public String getAddressType() {
		BaseAddressPanel addressPanel = (BaseAddressPanel) cardLayout.getCurrent(pnlBottom);
		return addressPanel.getSelectedAddressType();
	}

	/**
	 * put your documentation comment here
	 * 
	 * @param address
	 * @return
	 */
	public boolean isAddressEqualTo(Address address) {
		if (address == null)
			return false;
		if (addressCurrent.getAddressLine1().equals(address.getAddressLine1())
				&& addressCurrent.getAddressLine2().equals(address.getAddressLine2())
				&& addressCurrent.getCity().equals(address.getCity())
				&& addressCurrent.getCountry().equals(address.getCountry())
				&& addressCurrent.getState().equals(address.getState())
				&& addressCurrent.getZipCode().equals(address.getZipCode())
				&& addressCurrent.getPrimaryPhone().equals(address.getPrimaryPhone())
				&& addressCurrent.getSecondaryPhone().equals(address.getSecondaryPhone())
				&& addressCurrent.getTernaryPhone().equals(address.getTernaryPhone())
				&& addressCurrent.getAddressType().equals(address.getAddressType())
		// &&
		// this.getDirectional().equals(address.getDirectional())
		// &&
		// this.getEuropeanCountry().equals(address.getEuropeanCountry())
		)
			return true;
		return false;
	}

	/**
	 * put your documentation comment here
	 * 
	 * @param sAddressType
	 */
	public void setAddressType(String sAddressType) {
		BaseAddressPanel addressPanel = (BaseAddressPanel) cardLayout.getCurrent(pnlBottom);
		addressPanel.setSelectedAddressType(sAddressType);
	}

	/**
	 * put your documentation comment here
	 * 
	 * @return
	 */
	public Address getAddress() {
		BaseAddressPanel addressPanel = (BaseAddressPanel) cardLayout.getCurrent(pnlBottom);
		try {
			addressCurrent = addressPanel.getCustomerAddress();
			if (addressCurrent != null) {
				if (addressCurrent.getCountry() == null || addressCurrent.getCountry().length() < 1) {
					addressCurrent.setCountry(getCountry());
				}
				addressCurrent.setAddressFormat(getCountry());
				addressCurrent.setUseAsPrimary(isPrimary());
				if (addressCurrent.getAddressId() != null
						&& addressCurrent.getAddressId().trim().length() > 1)
					addressCurrent.setIsModified(true);
				return addressCurrent;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * put your documentation comment here
	 * 
	 * @param evt
	 */
	public void itemStateChanged(ItemEvent evt) {
		String sCommand = (String) evt.getItem();
		if (evt.getSource() instanceof JCMSComboBox) {
			sCommand = (String) cbCountry.getSelectedItem();
			if ((this.isShowing() && this.lastIsShowing) || sCommand.equals(addressFormat)) {
				if (sCommand.indexOf("EUR") != -1)
					lblCountry.setText(RESOURCE.getString("Address Format"));
				else
					lblCountry.setText(RESOURCE.getString("Country"));
				BaseAddressPanel addressPanel = (BaseAddressPanel) cardLayout.getCurrent(pnlBottom);
				addressCurrent = addressPanel.getCustomerAddress();
				addressFormat = sCommand;
				cardLayout.show(pnlBottom, sCommand);
				if (addressCurrent != null) {
					((BaseAddressPanel) cardLayout.getCurrent(pnlBottom)).setCustomerAddress(addressCurrent);
				}
				setEnabled(true);
			} else {
				cbCountry.setSelectedItem(addressFormat);
			}
			lastIsShowing = this.isShowing();
		}
	}

	/**
	 * put your documentation comment here
	 * 
	 * @param ae
	 */
	public void actionPerformed(ActionEvent ae) {
		setPrimary(chkboxUsePrimary.isSelected());
	}

	/**
	 * put your documentation comment here
	 * 
	 * @return
	 */
	public BaseAddressPanel getCurrentAddressPanel() {
		return (BaseAddressPanel) cardLayout.getCurrent(pnlBottom);
	}
}
