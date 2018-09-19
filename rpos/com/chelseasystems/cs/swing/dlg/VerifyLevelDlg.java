package com.chelseasystems.cs.swing.dlg;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Frame;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.util.ResourceBundle;

import javax.swing.BorderFactory;
import javax.swing.DefaultFocusManager;
import javax.swing.FocusManager;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import com.chelseasystems.cr.appmgr.IApplicationManager;
import com.chelseasystems.cr.swing.CMSApplet;
import com.chelseasystems.cr.swing.CMSFocusManager;
import com.chelseasystems.cr.util.ResourceManager;

/*
 History:
 +------+------------+-----------+-----------+----------------------------------------------------+
 | Ver# | Date       | By        | Defect #  | Description                                        |
 +------+------------+-----------+-----------+----------------------------------------------------+ 
 | 1    | 04-04-2006 |David Fung | PCR 67    | QAS                                                |
 +------+------------+-----------+-----------+----------------------------------------------------+
 */

/**
 * <p>Title:VerifyLevelDlg.java </p>
 *
 * <p>Description: VerifyLevelDlg </p>
 *
 * <p>Copyright: Copyright (c) 2005</p>
 *
 * <p>Company: Skillnet inc.</p>
 *
 * @author Sandhya Ajit
 * @version 1.0
 */

public class VerifyLevelDlg extends JDialog {

	private IApplicationManager theAppMgr;

	JButton btnOK;

	JButton btnCancel;

	JLabel jLabel1 = new JLabel();

	JTextField jTextField = new JTextField();

	JPanel pnlInfo = new JPanel();

	JPanel pnlToolBar = new JPanel();

	JPanel pnlMain = new JPanel();

	boolean ok = false;

	String addressText;

	String label;

	ResourceBundle res = ResourceManager.getResourceBundle();

	public VerifyLevelDlg(Frame frame, IApplicationManager theAppMgr, String title, String label,
			String addressText) {
		super(frame, title, true);
		try {
			this.addressText = addressText;
			this.label = label;
			setAppMgr(theAppMgr);
			jbInit();
			pack();
			double r = CMSApplet.r;
			this.setSize((int) (r * 800D), (int) (r * 250D));
			this.setResizable(false);
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	void jbInit() throws Exception {
		btnOK = theAppMgr.getTheme().getDefaultBtn();
		btnOK.setText(res.getString("OK"));
		btnOK.setMnemonic(res.getString("Mnemonic_OK").charAt(0));
		btnOK.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(ActionEvent e) {
				btnOK_actionPerformed(e);
			}
		});

		btnCancel = theAppMgr.getTheme().getDefaultBtn();
		btnCancel.setText(res.getString("Cancel"));
		btnCancel.setMnemonic(res.getString("Mnemonic_Cancel").charAt(0));
		btnCancel.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(ActionEvent e) {
				btnCancel_actionPerformed(e);
			}
		});

		jLabel1.setFont(theAppMgr.getTheme().getDialogFont());
		jLabel1.setText(res.getString(label));

		jTextField.setFont(theAppMgr.getTheme().getEditAreaFont());
		jTextField.setText(this.addressText);

		if (this.addressText.length() > 0) {
			jTextField.setEditable(false);
		}

		this.getContentPane().setBackground(theAppMgr.getBackgroundColor());
		this.getContentPane().setLayout(new BorderLayout());
		this.getContentPane().add(pnlInfo, BorderLayout.CENTER);
		this.getContentPane().add(pnlToolBar, BorderLayout.SOUTH);

		pnlInfo.setOpaque(false);
		pnlInfo.setBackground(theAppMgr.getBackgroundColor());
		pnlInfo.setLayout(new BorderLayout());
		pnlInfo.add(jLabel1, BorderLayout.NORTH);
		pnlInfo.add(this.jTextField, BorderLayout.CENTER);

		pnlToolBar.add(btnOK, null);
		pnlToolBar.add(btnCancel, null);

		pnlToolBar.setBackground(theAppMgr.getBackgroundColor());
		pnlToolBar.setBorder(BorderFactory.createEtchedBorder());

	}

	void btnOK_actionPerformed(ActionEvent e) {
		try {
			ok = true;
			this.addressText = this.jTextField.getText();
			FocusManager.setCurrentManager(new DefaultFocusManager());
			dispose();
		} catch (Exception ex) {
			System.out.println("Button OK in VerifyLevel Dialog failed.");
		}
	}

	void btnCancel_actionPerformed(ActionEvent e) {
		try {
			ok = false;
			FocusManager.setCurrentManager(new DefaultFocusManager());
			dispose();
		} catch (Exception ex) {
			System.out.println("Button Cancel in VerifyLevel Dialog failed.");
		}
	}

	public void setVisible(boolean visible) {
		if (visible) {
			Dimension d = Toolkit.getDefaultToolkit().getScreenSize();
			setLocation((d.width - getSize().width) / 2, (d.height - getSize().height) / 2);
			FocusManager.setCurrentManager(new CMSFocusManager());
		}
		super.setVisible(visible);
	}

	public void setAppMgr(IApplicationManager theAppMgr) {
		this.theAppMgr = theAppMgr;
	}

	public void actionPerformed(ActionEvent e) {
		dispose();
	}

	public boolean isOK() {
		return this.ok;
	}

	public String getAddressText() {
		return this.jTextField.getText();
	}

}