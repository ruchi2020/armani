package com.chelseasystems.cs.swing.dlg;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Frame;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JPanel;

import com.chelseasystems.cr.swing.CMSApplet;
import com.chelseasystems.cr.appmgr.IApplicationManager;
import com.chelseasystems.cs.item.ItemSearchString;
import com.chelseasystems.cs.store.CMSStore;
import com.chelseasystems.cs.swing.panel.ItemDetailsTxnLookupPanel;
import com.chelseasystems.cs.swing.panel.ItemLookupPanel;

public class ItemLookupDlg extends JDialog implements ActionListener {

	private JButton btnOk;
	private JButton btnCancel;
	private JPanel pnlButtons;
	private ItemLookupPanel pnlLookup;
	private IApplicationManager theAppMgr;
	private ItemSearchString itemSearchString;

	/**
	 * put your documentation comment here
	 * @param   Frame frame
	 * @param   IApplicationManager theAppMgr
	 * @param   TransactionSearchString itemSearchString
	 */
	public ItemLookupDlg(Frame frame, IApplicationManager theAppMgr, ItemSearchString itemSearchString) {
		super(frame, "Item Lookup", true);
		this.theAppMgr = theAppMgr;
		this.itemSearchString = itemSearchString;
		init();
		//		pnlLookup.setAppMgr(theAppMgr);
	}

	/**
	 * put your documentation comment here
	 */
	private void init() {
		pnlLookup = new ItemLookupPanel(theAppMgr);
		pnlButtons = new JPanel(new BorderLayout());
		btnOk = theAppMgr.getTheme().getDefaultBtn();
		btnOk.setText("Search");
		btnOk.setMnemonic('S');
		btnOk.addActionListener(this);
		pnlButtons.add(btnOk, BorderLayout.WEST);
		btnCancel = theAppMgr.getTheme().getDefaultBtn();
		btnCancel.setText("Cancel");
		btnCancel.setMnemonic('C');
		btnCancel.addActionListener(this);
		pnlButtons.add(btnCancel, BorderLayout.EAST);
		pnlButtons.setBackground(theAppMgr.getBackgroundColor());
		this.getContentPane().setLayout(new BorderLayout());
		this.getContentPane().add(pnlLookup, BorderLayout.CENTER);
		this.getContentPane().add(pnlButtons, BorderLayout.SOUTH);
		this.setResizable(false);
		setSize(520, 250);
	}

	/**
	 * put your documentation comment here
	 * @param visible
	 */
	public void setVisible(boolean visible) {
		if (visible) {
			Dimension d = Toolkit.getDefaultToolkit().getScreenSize();
			setLocation((d.width - getSize().width) / 2, (d.height - getSize().height) / 2);
		}
		super.setVisible(visible);
	}

	/**
	 * put your documentation comment here
	 * @param ae
	 */
	public void actionPerformed(ActionEvent ae) {
		String sCommand = ae.getActionCommand();
		boolean additionalFieldFlag = false;
		if (sCommand.equals("Search")) {
			if (pnlLookup.getSku() != null && !pnlLookup.getSku().trim().equals("")) {
				itemSearchString.setSKU(pnlLookup.getSku().trim());
				itemSearchString.setSearchRequired(true);
			} else if (pnlLookup.getStyle() != null && !pnlLookup.getStyle().trim().equals("")) {
				itemSearchString.setStyle(pnlLookup.getStyle().trim());
				itemSearchString.setSearchRequired(true);
			} else if (pnlLookup.getModel() != null && !pnlLookup.getModel().trim().equals("")) {
				if (pnlLookup.getFabric() != null && !pnlLookup.getFabric().trim().equals("")) {
					itemSearchString.setFabric(pnlLookup.getFabric().trim());
					additionalFieldFlag = true;
				}
				if (pnlLookup.getColor() != null && !pnlLookup.getColor().trim().equals("")) {
					itemSearchString.setColor(pnlLookup.getColor().trim());
					additionalFieldFlag = true;
				}
				if (pnlLookup.getSupplier() != null && !pnlLookup.getSupplier().trim().equals("")) {
					itemSearchString.setSupplier(pnlLookup.getSupplier());
					additionalFieldFlag = true;
				}
				if (pnlLookup.getYear() != null && !pnlLookup.getYear().trim().equals("")) {
					itemSearchString.setYear(pnlLookup.getYear().trim());
					additionalFieldFlag = true;
				}
				if (pnlLookup.getSeason() != null && !pnlLookup.getSeason().toString().trim().equals("")) {
					itemSearchString.setSeason(pnlLookup.getSeason());
					additionalFieldFlag = true;
				}
				if (!additionalFieldFlag) {
					theAppMgr.showErrorDlg(CMSApplet.res.getString("Enter SKU or Style or (Model + additional field) to search"));
					return;
				}
				itemSearchString.setModel(pnlLookup.getModel().trim());
				itemSearchString.setSearchRequired(true);
			} else {
				theAppMgr.showErrorDlg(CMSApplet.res.getString("Enter SKU or Style or (Model + additional field) to search"));
				return;
			}
			itemSearchString.setStore(((CMSStore) theAppMgr.getGlobalObject("STORE")).getId());
		} else if (sCommand.equals("Cancel")) {
			itemSearchString.setSearchRequired(false);
		}
		this.dispose();
	}
}