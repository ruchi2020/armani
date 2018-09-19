/*
 * @copyright 2000 Chelsea Market Systems, LLC.
 History:
 +------+------------+-----------+-----------+-------------------------------------------------+
 | Ver# | Date       | By        | Defect #  | Description                                     |
 +------+------------+-----------+-----------+-------------------------------------------------+
 | 2    | 04-28-2005 |Megha      |           |   Validating UserID & Password First.
 |      |            |           |           |   Validating StoreID & RegisterID               |
 |      |            |           |           |                                                 |
 |      |            |           |           |                                                 |
 -----------------------------------------------------------------------------------------------
 | 3    | 05-24-05   | Manpreet  |20         | Changed size to accomodate 800 X 600 resolution |
 +------+------------+-----------+-----------+-------------------------------------------------+
 | 4    | 06-13-2005 |Vikram     |  188      | SOD: Password is not verified during SOD        |
 +------+------------+-----------+-----------+--------------------------------------------------
 | 5    | 07-11-2005 |Vikram     |  550      | Cash Drawer to open at store opening            |
 +------+------------+-----------+-----------+-------------------------------------------------+
 *
 * formatted with JxBeauty (c) johann.langhofer@nextra.at
 */
package com.chelseasystems.cs.swing.session;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import com.chelseasystems.cr.appmgr.*;
import com.chelseasystems.cr.swing.TextFilter;
import com.chelseasystems.cs.sos.SOSBootStrap_EUR;
import com.chelseasystems.cr.appmgr.bootstrap.BootStrapInfo;
import com.chelseasystems.cr.currency.ArmCurrency;
import com.chelseasystems.cr.swing.bean.*;
import com.chelseasystems.cr.swing.JColorScrollBar;
import com.chelseasystems.cs.store.CMSStore;
import com.chelseasystems.cs.register.CMSRegister;
import com.chelseasystems.cr.config.*;
import com.chelseasystems.cs.employee.CMSEmployee;
import java.util.Date;
import com.chelseasystems.cr.util.DateUtil;
import java.text.SimpleDateFormat;
import com.chelseasystems.cs.register.*;
import com.chelseasystems.cs.store.CMSStoreHelper;
import com.chelseasystems.cs.fiscaldocument.FiscalInterface;
import com.chelseasystems.cr.appmgr.mask.MaskManager;

import com.zelator.winterface.WInterface;

/**
 */
public class StartofSessionDialog_EUR extends JDialog {
	public boolean sodComplete = false;
	private boolean inProgress = false;
	private EditFocusAdapter focusAdapter = new EditFocusAdapter();
	private SOSActionListener actionListener = new SOSActionListener();
	private IBrowserManager theMgr;
	private Theme theme;
	SOSBootStrap_EUR bootStrap;
	boolean isUseInitialDrawerFund;
	boolean focusGot = false;
	JPanel pnlMain = new JPanel();
	double dResolution = com.chelseasystems.cr.swing.CMSApplet.r;
	CMSEmployee oper;
	JTextArea areaStatus = new JTextArea();
	/**
	 * All Text Fields
	 */
	JCMSTextField edtStoreID = new JCMSTextField();
	JCMSTextField edtRegID = new JCMSTextField();
	JCMSTextField edtRegType = new JCMSTextField();
	JCMSTextField edtRegDesc = new JCMSTextField();
	JCMSTextField edtBusinessdate = new JCMSTextField();
	JCMSTextField edtDrawerFund = new JCMSTextField();
	JPasswordField edtUserID = new JPasswordField();
	JPasswordField edtPassword = new JPasswordField();
	/**
	 * Labels
	 */
	JCMSLabel lblStoreID = new JCMSLabel();
	JCMSLabel lblRegID = new JCMSLabel();
	JCMSLabel lblRegType = new JCMSLabel();
	JCMSLabel lblRegDesc = new JCMSLabel();
	JCMSLabel lblBusinessdate = new JCMSLabel();
	JCMSLabel lblDrawerFund = new JCMSLabel();
	JCMSLabel lblUserID = new JCMSLabel();
	JCMSLabel lblPassword = new JCMSLabel();
	JButton btnCancel;
	JButton btnOK;
	JButton btnSysTimeDate;
	java.util.ResourceBundle res = com.chelseasystems.cr.util.ResourceManager.getResourceBundle();
	SimpleDateFormat df = com.chelseasystems.cs.util.DateFormatUtil.getLocalDateFormat();
	private FiscalInterface fiscalInterface = null;

  /**
   * Default Constructor
   * @param frame the owner of this dialog
   * @param title of the dialog box
   * @param modal application modal flag
   * @param bootStrap reference to Start of Session bootstrap
   * @param theAppMgr reference to the Application Manager
   */
	public StartofSessionDialog_EUR(Frame frame, String title, boolean modal, SOSBootStrap_EUR bootStrap, 
		IBrowserManager theMgr) {
		super(frame, title, modal);
		// this.setResizable(false);
		this.bootStrap = bootStrap;
		this.theMgr = theMgr;
		this.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		ConfigMgr config = new ConfigMgr("client_master.cfg");
		isUseInitialDrawerFund = config.getString("USE_INITIAL_DRAWER_FUND").equalsIgnoreCase("YES");
		try {
			theme = new ThemeManager().getDefaultTheme();
			jbInit();
			// Eur: Populate the initial values from register.cfg
			// In case the rpository objects aren't present.
			populateInitialValues();
			if (!isUseInitialDrawerFund) {
				lblDrawerFund.setVisible(false);
				edtDrawerFund.setVisible(false);
				edtDrawerFund.setText("0.00");
			} else {
				CMSStore store = (CMSStore) theMgr.getGlobalObject("STORE");
				CMSRegister register = (CMSRegister) theMgr.getGlobalObject("REGISTER");
				if (store != null && register != null) {
					edtDrawerFund.setEnabled(true);
					edtDrawerFund.setBackground(Color.white);
				}
			}
			pack();
			this.setResizable(false);
			setSize(590, 590);
			// setSize(700,700);
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	/**
	 * @exception Exception
	 */
	private void jbInit() throws Exception {
		/**
		 * Labels
		 */
		lblDrawerFund = new JCMSLabel();
		JPanel pnlEast = new JPanel();
		JPanel pnlSouth = new JPanel();
		JPanel pnlWest = new JPanel();
		JPanel pnlClient = new JPanel();
		JPanel pnlRegister = new JPanel();
		JPanel pnlUser = new JPanel();
		JPanel pnlholdFields = new JPanel();
		JCMSLabel lblTitle = new JCMSLabel();
		String val = null;
		pnlMain.setBackground(theme.getBackground());
		/**
		 * Store ID
		 */
		lblStoreID.setText(res.getString("Enter Store ID"));
		lblStoreID.setLabelFor(edtStoreID);
		edtStoreID.setDocument(new TextFilter(TextFilter.ALPHA_NUMERIC));
		//TD
		//edtStoreID.setNextFocusableComponent(edtRegID);
		edtStoreID.addActionListener(actionListener);
		edtStoreID.addFocusListener(focusAdapter);
		edtStoreID.setEnabled(false);
		edtStoreID.setBackground(theme.getEditArea());
		/**
		 * Reg ID
		 */
		lblRegID.setText(res.getString("Enter Register ID"));
		lblRegID.setLabelFor(edtRegID);
		edtRegID.setDocument(new TextFilter(TextFilter.NUMERIC));
		//TD
		//edtRegID.setNextFocusableComponent(edtDrawerFund);
		edtRegID.addActionListener(actionListener);
		edtRegID.addFocusListener(focusAdapter);
		edtRegID.setEnabled(false);
		edtRegID.setBackground(theme.getEditArea());
		/**
		 * Reg Type
		 */
		lblRegType.setText(res.getString("Enter Register Type"));
		lblRegType.setLabelFor(edtRegType);
		edtRegType.setEnabled(false);
		edtRegType.setBackground(theme.getEditArea());
		/**
		 * Reg Desc
		 */
		lblRegDesc.setText(res.getString("Register Description"));
		lblRegDesc.setLabelFor(edtRegDesc);
		edtRegDesc.setEnabled(false);
		edtRegDesc.setBackground(theme.getEditArea());
		/**
		 * Business Date
		 */
		lblBusinessdate.setText(res.getString("Business Date"));
		lblBusinessdate.setLabelFor(edtBusinessdate);
		/**
		 * Drawer Fund
		 */
		lblDrawerFund.setLabelFor(edtDrawerFund);
		lblDrawerFund.setText(res.getString("Enter Drawer Fund"));
		edtDrawerFund.addFocusListener(focusAdapter);
		edtDrawerFund.addActionListener(actionListener);
		//TD
		//edtDrawerFund.setNextFocusableComponent(edtUserID);
		edtDrawerFund.setDocument(new TextFilter(TextFilter.NUMERIC + ".,"));
		edtDrawerFund.setEnabled(false);
		edtDrawerFund.setBackground(theme.getEditArea());
		/**
		 * User ID
		 */
		lblUserID.setText(res.getString("Enter User ID"));
		lblUserID.setLabelFor(edtUserID);
		//TD
		//edtUserID.setNextFocusableComponent(edtPassword);
		edtUserID.addActionListener(actionListener);
		edtUserID.addFocusListener(focusAdapter);
		edtUserID.setDocument(new TextFilter(TextFilter.ALPHA_NUMERIC, 20));
		edtUserID.setBorder(edtDrawerFund.getBorder());
		/**
		 * Password
		 */
		lblPassword.setText(res.getString("Enter Password"));
		lblPassword.setLabelFor(edtPassword);
		// edtPassword.setNextFocusableComponent(isUseInitialDrawerFund ? edtDrawerFund : edtOperatorID);
		//TD
		/*
		if (isUseInitialDrawerFund) {
			edtPassword.setNextFocusableComponent(edtDrawerFund);
		} else {
			// edtPassword.setNextFocusableComponent(edtOperatorID);
			edtPassword.setNextFocusableComponent(edtStoreID);
		}
		*/
		edtPassword.addActionListener(actionListener);
		edtPassword.addFocusListener(focusAdapter);
		edtPassword.setBorder(edtDrawerFund.getBorder());    
		btnOK = theme.getDefaultBtn();
		btnOK.setText(res.getString("Start Processing"));
		//TD
		//btnOK.setNextFocusableComponent(btnCancel);
		btnOK.addActionListener(actionListener);
		btnOK.setMnemonic(res.getString("Mnemonic_Start").charAt(0));
		btnCancel = theme.getDefaultBtn();
		btnCancel.setText(res.getString("Reset"));
		//TD
		//btnCancel.setNextFocusableComponent(edtStoreID);
		btnCancel.addActionListener(actionListener);
		btnCancel.setMnemonic(res.getString("Mnemonic_Cancel").charAt(0));
		// EUR: Add Sys Time & Date
		btnSysTimeDate = theme.getDefaultBtn();
		btnSysTimeDate.setText(res.getString("System Date/Time"));
		// System.out.println("******** Label =" + res.getString("System Date/Time"));
		//TD
		//btnSysTimeDate.setNextFocusableComponent(btnOK);
		btnSysTimeDate.addActionListener(actionListener);
		btnSysTimeDate.setMnemonic(res.getString("Mnemonic_System_Date_Time").charAt(0));
		pnlRegister.setLayout(new GridBagLayout());
		pnlRegister.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(), "Initial Settings"));
		pnlUser.setLayout(new GridBagLayout());
		pnlholdFields.setLayout(new BorderLayout());
		pnlClient.setLayout(new BorderLayout());
		pnlSouth.setLayout(new GridBagLayout());
		lblTitle.setBorder(BorderFactory.createEmptyBorder(10, 0, 10, 0));
		val = "                                             ";
		lblTitle.setText(val + res.getString("RPOS Start of Session"));
		lblTitle.setLabelFor(edtStoreID);
		lblTitle.setHorizontalAlignment(SwingConstants.CENTER);
		areaStatus.setEditable(false);
		areaStatus.setText("");
		areaStatus.setRequestFocusEnabled(false);
		JScrollPane scrStatus = new JScrollPane(areaStatus);
		scrStatus.getViewport().setBackground(Color.white);
		scrStatus.setHorizontalScrollBar(new JColorScrollBar(theme.getBackground(), JScrollBar.HORIZONTAL));
		scrStatus.setVerticalScrollBar(new JColorScrollBar(theme.getBackground()));
		pnlMain.setLayout(new BorderLayout());
		pnlMain.setBorder(BorderFactory.createEtchedBorder());
		pnlEast.setOpaque(false);
		pnlSouth.setOpaque(false);
		pnlWest.setOpaque(false);
		pnlClient.setOpaque(false);
		pnlRegister.setOpaque(false);
		pnlUser.setOpaque(false);
		pnlholdFields.setOpaque(false);
		getContentPane().add(pnlMain);
		pnlMain.add(pnlEast, BorderLayout.EAST);
		pnlMain.add(pnlSouth, BorderLayout.SOUTH);
		pnlMain.add(lblTitle, BorderLayout.NORTH);
		pnlMain.add(pnlWest, BorderLayout.WEST);
		pnlMain.add(pnlClient, BorderLayout.CENTER);
		pnlholdFields.add(pnlRegister, BorderLayout.NORTH);
		pnlholdFields.add(pnlUser, BorderLayout.SOUTH);
		pnlClient.add(scrStatus, BorderLayout.CENTER);
		pnlClient.add(pnlholdFields, BorderLayout.WEST);
		pnlUser.add(lblBusinessdate, new GridBagConstraints(0, 9, 1, 1, 0.0, 0.0, GridBagConstraints.NORTHWEST, GridBagConstraints.NONE, new Insets(-4, 6, 9, 9), 130, 0));
		pnlUser.add(edtBusinessdate, new GridBagConstraints(0, 10, 1, 1, 1.0, 0.0, GridBagConstraints.NORTHWEST, GridBagConstraints.HORIZONTAL, new Insets(-4, 6, 9, 9), 0, 5));
		pnlUser.add(edtDrawerFund, new GridBagConstraints(0, 12, 1, 1, 1.0, 0.0, GridBagConstraints.NORTHWEST, GridBagConstraints.HORIZONTAL, new Insets(-4, 6, 9, 9), 0, 5));
		pnlUser.add(edtUserID, new GridBagConstraints(0, 14, 1, 1, 1.0, 0.0, GridBagConstraints.NORTHWEST, GridBagConstraints.HORIZONTAL, new Insets(-4, 6, 9, 9), 0, 5));
		pnlUser.add(edtPassword, new GridBagConstraints(0, 16, 1, 1, 1.0, 0.1, GridBagConstraints.NORTHWEST, GridBagConstraints.HORIZONTAL, new Insets(-4, 6, 9, 9), 0, 5));
		pnlUser.add(lblDrawerFund, new GridBagConstraints(0, 11, 1, 1, 0.0, 0.0, GridBagConstraints.NORTHWEST, GridBagConstraints.NONE, new Insets(-4, 6, 9, 9), 0, 0));
		pnlUser.add(lblUserID, new GridBagConstraints(0, 13, 1, 1, 0.0, 0.0, GridBagConstraints.NORTHWEST, GridBagConstraints.NONE, new Insets(-4, 6, 9, 9), 0, 0));
		pnlUser.add(lblPassword, new GridBagConstraints(0, 15, 1, 1, 0.0, 0.0, GridBagConstraints.NORTHWEST, GridBagConstraints.NONE, new Insets(-4, 6, 9, 9), 0, 0));
		pnlSouth.add(btnSysTimeDate, new GridBagConstraints(1, 1, 1, 1, 0.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(0, 5, 0, 122), 11, 0));
		pnlSouth.add(btnOK, new GridBagConstraints(2, 1, 1, 1, 0.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(0, -10, 0, 39), 29, 0));
		pnlSouth.add(btnCancel, new GridBagConstraints(3, 1, 2, 1, 0.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(0, -75, 0, 0), 30, 0));
		pnlRegister.add(lblStoreID, new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0, GridBagConstraints.NORTHWEST, GridBagConstraints.NONE, new Insets(-4, 6, 9, 9), 0, 0));
		pnlRegister.add(lblRegID, new GridBagConstraints(0, 2, 1, 1, 0.0, 0.0, GridBagConstraints.NORTHWEST, GridBagConstraints.NONE, new Insets(-4, 6, 9, 9), 0, 0));
		pnlRegister.add(lblRegType, new GridBagConstraints(0, 4, 1, 1, 0.0, 0.0, GridBagConstraints.NORTHWEST, GridBagConstraints.NONE, new Insets(-4, 6, 9, 9), 0, 0));
		pnlRegister.add(lblRegDesc, new GridBagConstraints(0, 6, 1, 1, 0.0, 0.0, GridBagConstraints.NORTHWEST, GridBagConstraints.NONE, new Insets(-4, 6, 9, 9), 0, 0));
		pnlRegister.add(edtRegID, new GridBagConstraints(0, 3, 1, 1, 1.0, 0.0, GridBagConstraints.NORTHWEST, GridBagConstraints.HORIZONTAL, new Insets(-4, 6, 9, 9), 0, 5));
		pnlRegister.add(edtRegType, new GridBagConstraints(0, 5, 1, 1, 1.0, 0.0, GridBagConstraints.NORTHWEST, GridBagConstraints.HORIZONTAL, new Insets(-4, 6, 9, 9), 0, 5));
		pnlRegister.add(edtRegDesc, new GridBagConstraints(0, 7, 1, 1, 1.0, 0.1, GridBagConstraints.NORTHWEST, GridBagConstraints.HORIZONTAL, new Insets(-4, 6, 9, 9), 0, 5));
		pnlRegister.add(edtStoreID, new GridBagConstraints(0, 1, 1, 1, 1.0, 0.0, GridBagConstraints.NORTHWEST, GridBagConstraints.HORIZONTAL, new Insets(-4, 6, 9, 9), 0, 5));
		lblTitle.setFont(theme.getTextFieldFont());
		lblPassword.setFont(theme.getLabelFont());
		edtPassword.setFont(theme.getEditAreaFont());
		lblUserID.setFont(theme.getLabelFont());
		edtUserID.setFont(theme.getEditAreaFont());
		lblStoreID.setFont(theme.getLabelFont());
		edtStoreID.setFont(theme.getEditAreaFont());
		lblDrawerFund.setFont(theme.getLabelFont());
		edtDrawerFund.setFont(theme.getEditAreaFont());
		lblRegID.setFont(theme.getLabelFont());
		edtRegID.setFont(theme.getEditAreaFont());
		lblRegType.setFont(theme.getLabelFont());
		edtRegType.setFont(theme.getEditAreaFont());
		lblRegDesc.setFont(theme.getLabelFont());
		edtRegDesc.setFont(theme.getEditAreaFont());
		lblBusinessdate.setFont(theme.getLabelFont());
		edtBusinessdate.setFont(theme.getEditAreaFont());
		edtBusinessdate.setEnabled(false);
		edtBusinessdate.setFont(theme.getEditAreaFont());
		areaStatus.setFont(theme.getEditAreaFont());
		this.getRootPane().setDefaultButton(btnOK);
		this.addWindowListener(new WindowAdapter() {
			/**
			 * put your documentation comment here
			 * 
			 * @param e
			 */
			public void windowOpened(WindowEvent e) {
				// requestFocusStoreID();
				requestFocusUserID();
			}
		});

		FocusTraversalPolicy ftPolicy = new FocusTraversalPolicy() {
			public Component getComponentAfter(Container focusCycleRoot, Component aComponent) {
				if (aComponent.equals(edtStoreID)) {
	                return edtRegID;
	            } else if (aComponent.equals(edtRegID)) {
	                return edtDrawerFund;
	            } else if (aComponent.equals(edtDrawerFund)) {
	                return edtUserID;
	            } else if (aComponent.equals(edtUserID)) {
	                return edtPassword;
	            } else if (aComponent.equals(edtPassword)) {
	        		if (isUseInitialDrawerFund) {
	        			return edtDrawerFund;
	        		} else {
	        			return edtStoreID;
	        		}	            	
	            } else if (aComponent.equals(btnSysTimeDate)) {
	                return btnOK;
	            } else if (aComponent.equals(btnOK)) {
	                return btnCancel;
	            } else if (aComponent.equals(btnCancel)) {
	                return edtStoreID;
	            }
	            return edtStoreID;
			}
			public Component getComponentBefore(Container focusCycleRoot, Component aComponent) {
				//Reverse the cycle.
				if (aComponent.equals(edtStoreID)) {
	                return btnCancel;
	            } else if (aComponent.equals(edtRegID)) {
	                return edtStoreID;
	            } else if (aComponent.equals(edtDrawerFund)) {
	                return edtRegID;
	            } else if (aComponent.equals(edtUserID)) {
	                return edtDrawerFund;
	            } else if (aComponent.equals(edtPassword)) {
	            	return edtUserID;
	            } else if (aComponent.equals(btnOK)) {
	                return edtStoreID;
	            } else if (aComponent.equals(btnCancel)) {
	                return btnOK;
	            }
	            return edtStoreID;
			}
			public Component getDefaultComponent(Container focusCycleRoot) {
			    return edtStoreID;
			}
			public Component getLastComponent(Container focusCycleRoot) {
			    return btnCancel;
			}
			public Component getFirstComponent(Container focusCycleRoot) {
			    return edtStoreID;
			}
		};
		setFocusTraversalPolicy(ftPolicy);		
	}

	/**
	 */
	public void paintImmediately() {
		Component[] components = getContentPane().getComponents();
		for (int i = 0; i < components.length; i++) {
			Dimension d = components[i].getSize();
			((JComponent) components[i]).paintImmediately(0, 0, d.width, d.height);
		}
	}

	/**
	 * @param visible
	 */
	public void setVisible(boolean visible) {
		if (visible) {
			//System.out.println("___Tim: " + " Calling WInterface in SOSDialog_EUR....");
	    	//WInterface.setOnTop(this);
	    	setAlwaysOnTop(true);
			Dimension d = Toolkit.getDefaultToolkit().getScreenSize();
			setLocation((d.width - getSize().width) / 2, (d.height - getSize().height) / 2);
		}
		super.setVisible(visible);
	}

	/**
	 * Method to retrieve the Store ID
	 * @return String text from Store ID text area
	 */
	public String getStoreID() {
		return edtStoreID.getText();
	}

	/**
	 */
	public ArmCurrency getDrawerFund() throws NumberFormatException {
	ArmCurrency amt = new ArmCurrency(edtDrawerFund.getText());
		edtDrawerFund.setText(amt.stringValue()); // bug 4553
		return amt;
	}

	/**
	 * Focus on Store ID
	 */
	public void requestFocusStoreID() {
		SwingUtilities.invokeLater(new Runnable() {
			/**
			 */
			public void run() {
				edtStoreID.requestFocus();
				focusGot = true;
			}
		});
	}

	/**
	 * On UserID
	 */
	public void requestFocusUserID() {
		SwingUtilities.invokeLater(new Runnable() {
			/**
			 */
			public void run() {
				edtUserID.requestFocus();
			}
		});
	}

	/**
	 * On RegID
	 */
	public void requestFocusRegID() {
		SwingUtilities.invokeLater(new Runnable() {
			/**
			 */
			public void run() {
				edtRegID.requestFocus();
			}
		});
	}

	/**
	 * Method to retrieve the Password
	 * @return String text from Password text area
	 */
	public String getPassword() {
		String passwd;
		if (edtPassword.getPassword() == null) {
			passwd = "";
		} else {
			passwd = String.copyValueOf(edtPassword.getPassword());
		}
		return passwd;
	}

	/**
	 */
	public void enableFields(boolean enabled) {
		edtUserID.setEnabled(enabled);
		edtPassword.setEnabled(enabled);
		edtStoreID.setEnabled(enabled);
		edtDrawerFund.setEnabled(enabled);
		/**
		 * New Fields
		 */
		edtRegID.setEnabled(enabled);
		edtRegType.setEnabled(enabled);
		edtRegDesc.setEnabled(enabled);
		edtBusinessdate.setEnabled(enabled);
		edtUserID.setBackground((enabled) ? Color.white : theme.getEditArea());
		edtPassword.setBackground((enabled) ? Color.white : theme.getEditArea());
		edtStoreID.setBackground((enabled) ? Color.white : theme.getEditArea());
		edtDrawerFund.setBackground((enabled) ? Color.white : theme.getEditArea());
		/**
		 * New Fields
		 */
		edtRegID.setBackground((enabled) ? Color.white : theme.getEditArea());
		edtRegType.setBackground((enabled) ? Color.white : theme.getEditArea());
		edtRegDesc.setBackground((enabled) ? Color.white : theme.getEditArea());
		edtBusinessdate.setBackground((enabled) ? Color.white : theme.getEditArea());
	}

	/**
	 * Method to retrieve the Operator ID
	 * @return String text from Operator ID text area
	 */
	public String getUserID() {
		return String.copyValueOf(edtUserID.getPassword());
	}

	/**
	 * Method to return UserID
	 * @param status
	 *            String
	 */
	public String getRegId() {
		return edtRegID.getText();
	}

	/**
	 * Method to update the SOS status
	 * @return String text additional status to add to Status area
	 */
	public void setStatus(String status) {
		setStatus(status, null);
	}

	/**
	 * Method to update the SOS status
	 * @param value
	 *            a non-resourceable value to describe the status
	 * @return String text additional status to add to Status area
	 */
	public void setStatus(String status, String value) {
		StringBuffer buf = new StringBuffer();
		buf.append(res.getString(status));
		if (value != null) {
			buf.append(value);
		}
		buf.append("\n==========================================\n");
		areaStatus.append(buf.toString());
		areaStatus.setCaretPosition(areaStatus.getText().length());
		areaStatus.repaint();
	}

	/**
	 * Method set the dialog once SOD is confirmed
	 */
	public void updateGUIForDone() {
		sodComplete = true;
		enableFields(false); // bug 4554
		// Validate the userId & PasswdId
		CMSRegister register = (CMSRegister) theMgr.getGlobalObject("REGISTER");
		if (register != null) {
			edtRegType.setText(register.getRegisterType());
			edtRegDesc.setText(register.getRegisterDesc());
		}
		setStatus(res.getString("Start of Session complete."));
		btnCancel.setVisible(false);
		this.btnSysTimeDate.setVisible(false);
		btnOK.setText(res.getString("Done"));
		btnOK.setMnemonic(res.getString("Mnemonic_Done").charAt(0));
		btnOK.requestFocus();
	}

	/**
	 */
	private void populateInitialValues() {
		CMSStore store = (CMSStore) theMgr.getGlobalObject("STORE");
		CMSRegister register = (CMSRegister) theMgr.getGlobalObject("REGISTER");
		ConfigMgr config = new ConfigMgr("register.cfg");
		String regIdFromConfig = config.getString("REGISTER_ID");
		String storeIdFromConfig = config.getString("STORE_ID");
		String dateString = null;
		try {
			Date d = DateUtil.getBeginingOfDay();
			dateString = df.format(d);
		} catch (Exception e) {
			System.out.println("Error in time format");
		}
		edtBusinessdate.setText(dateString);
		edtBusinessdate.setBackground(theme.getEditArea());
		if (store != null) {
			edtStoreID.setText(store.getId());
			edtStoreID.setEditable(false);
		} else if (store == null && storeIdFromConfig != null) {
			// Eur: Confirm that the storeId in the config file is not corrupt.
			try {
				CMSStore storeConfig = CMSStoreHelper.findById(theMgr, storeIdFromConfig);
				if (storeConfig != null) {
					edtStoreID.setText(storeConfig.getId());
					edtStoreID.setEditable(false);
				}
			} catch (Exception e) {
			}
		}
		if (register != null) {
			edtDrawerFund.setText(register.getDrawerFund().stringValue());
			edtRegID.setText(register.getId());
			edtRegType.setText(register.getRegisterType());
			edtRegDesc.setText(register.getRegisterDesc());
		}
		// Eur: Get reg Id from config file.
		else if (register == null && regIdFromConfig != null && storeIdFromConfig != null) {
			try {
				CMSRegister regConfig = CMSRegisterHelper.selectByStoreAndRegID(theMgr, storeIdFromConfig, regIdFromConfig);
				if (regConfig != null) {
					edtDrawerFund.setText(regConfig.getDrawerFund().stringValue());
					edtRegID.setText(regConfig.getId());
					edtRegType.setText(regConfig.getRegisterType());
					edtRegDesc.setText(regConfig.getRegisterDesc());
				}
			} catch (Exception e) {
			}
		}
		/**
		 * Reg Type & Reg Desc are read-only
		 */
	}

	/**
	 * Method to attempt processing of Start of Day
	 */
	private void beginStartOfDay() {
		btnOK.setVisible(false);
		if (!sodComplete) {
			boolean rc = processStartOfDay();
			if (rc) {
				// pop drawer
				try {
					System.out.println("->Pop Drawer...");
					// Fiscal interface
					ConfigMgr configMgr = new ConfigMgr("fiscal_document.cfg");
					String sClassName = configMgr.getString("FISCAL_DOCUMENT_PRINTER");
					Class cls = Class.forName(sClassName);
					fiscalInterface = (FiscalInterface) cls.newInstance();
					// end fiscal
//				 Riccardo
					// fiscalInterface.openDrawer();
					// end 

				} catch (Exception ex) {
					// CashDrawer not found!
					System.out.println("Cash Drawer Not Found");
				}
				updateGUIForDone();
			}
			btnOK.setVisible(true);
		} else {
			setVisible(false);
		}
	}

	/**
	 * Method to validate and complete all SOD parameters
	 */
	private boolean processStartOfDay() {
		try {
			pnlMain.setCursor(java.awt.Cursor.getPredefinedCursor(java.awt.Cursor.WAIT_CURSOR));
			if (!bootStrap.validateProcessDate()) {
				return true; // needs to continue on to app for processing eod
			}
			if (!bootStrap.validateUserID()) {
				requestFocusUserID();
				return false;
			}
			if (!bootStrap.validatePassword()) {
				edtPassword.requestFocus();
				return false;
			}
			if (!bootStrap.validateStoreID()) {
				this.edtStoreID.setBackground(Color.white);
				requestFocusStoreID();
				return false;
			}
			if (!bootStrap.validateRegisterID()) {
				edtRegID.setEnabled(true);
				// edtUserID.setBackground(Color.white);
				edtRegID.requestFocus();
				// requestFocusRegID();
				return false;
			}
			BootStrapInfo info = bootStrap.finishIt(); // validate drawer fund within this method
			if (info.isError()) {
				if (isUseInitialDrawerFund) {
					edtDrawerFund.requestFocus();
				} else {
					edtUserID.requestFocus();
				}
				this.setStatus(info.getMsg());
				return false;
			}
			return true;
		} finally {
			pnlMain.setCursor(java.awt.Cursor.getPredefinedCursor(java.awt.Cursor.DEFAULT_CURSOR));
		}
	}

	/**
	 * Private inner class to respond to text area focus changes
	 */
	private class EditFocusAdapter extends FocusAdapter {
		/**
		 * @param e
		 */
		public void focusGained(FocusEvent e) {
			((JTextField) e.getSource()).selectAll();
		}
	}

	/**
	 * Make sure that the operator performing this function has the authority to do so.
	 * @return boolean <code>true</code> if the user is authorized to run start-of-day or <code>false</code> otherwise.
	 */
	/**
	 * Private inner class to respond to Actions performed on text areas or dialog buttons.
	 */
	private class SOSActionListener implements ActionListener {
		/**
		 * @param e
		 */
		public void actionPerformed(ActionEvent e) {
			Object source = e.getSource();
			CMSRegister register = (CMSRegister) theMgr.getGlobalObject("REGISTER");
			CMSStore store = (CMSStore) theMgr.getGlobalObject("STORE");
			boolean validUser = false;
			if (source == btnCancel) {
				bootStrap.cancelButtonPressed();
			} else if (source == btnSysTimeDate) {
				// MP: Launch the script from here.
				ConfigMgr config = new ConfigMgr("register.cfg");
				String launchScriptPath = config.getFileName("LAUNCH_CALENDAR");
				try {
					// Sergio
					// Class cls = Class.forName(launchScriptPath).newInstance();
					// Class cls = Class.forName(launchScriptPath);
					// cls.newInstance();
					// System.out.println("clsNEWINstance " + cls.getClass());
					Class cls = Class.forName(launchScriptPath);
					fiscalInterface = (FiscalInterface) cls.newInstance();
					System.out.println("NEW INSTNACE " + fiscalInterface.getClass());
					fiscalInterface.setSystemAndFiscalDate(null);
					Date d = DateUtil.getBeginingOfDay();
					String dateString = df.format(d);
					edtBusinessdate.setText(dateString);
					edtBusinessdate.setBackground(theme.getEditArea());
				} catch (Exception ex) {
					ex.printStackTrace();
				}
			} else if (source == btnOK && btnOK.getText().equals("Start Processing") && edtStoreID.getText().equals("")) {
				/**
				 * Get Store ID & Reg ID if they don't exist as repository object
				 */
				boolean validpasswd = false, validuserID = false;
				// if ( (store == null) || (register == null)) {
				validuserID = bootStrap.validateUserID();
				if (validuserID)
					validpasswd = bootStrap.validatePassword();
				if (!(validpasswd) || !(validuserID)) {
					// reset
					edtUserID.setText("");
					edtPassword.setText("");
					requestFocusUserID();
				} else if (validpasswd && validuserID) {
					validUser = true;
					edtStoreID.setEnabled(true);
					edtRegID.setEnabled(true);
					edtStoreID.setBackground(Color.white);
					edtRegID.setBackground(Color.white);
					edtUserID.setEnabled(false);
					edtPassword.setEnabled(false);
					edtUserID.setBackground(theme.getEditArea());
					edtPassword.setBackground(theme.getEditArea());
					if (isUseInitialDrawerFund) {
						edtDrawerFund.setEnabled(true);
						edtDrawerFund.setBackground(Color.white);
					}
					requestFocusStoreID();
				}
				// }
			} else if (source == btnOK && !edtStoreID.getText().equals("")) {
				beginStartOfDay();
			} else if (source == edtStoreID || source == edtDrawerFund || source == edtPassword) {
				if (source == edtDrawerFund) {
					if (!verifyDrawerAmount())
						return;
				}
				((JComponent) source).transferFocus();
			}
		}
	}

	// MP: Resets the fields.
	public void reset() {
		edtUserID.setText("");
		edtPassword.setText("");
		CMSStore store = (CMSStore) theMgr.getGlobalObject("STORE");
		CMSRegister register = (CMSRegister) theMgr.getGlobalObject("REGISTER");
		ConfigMgr config = new ConfigMgr("register.cfg");
		String regIdFromConfig = config.getString("REGISTER_ID");
		String storeIdFromConfig = config.getString("STORE_ID");
		if (store == null && storeIdFromConfig == null)
			edtStoreID.setText("");
		else if (store != null)
			edtStoreID.setText(storeIdFromConfig);
		if (register == null && regIdFromConfig == null) {
			edtRegID.setText("");
			edtRegDesc.setText("");
			edtRegType.setText("");
		} else if (register != null || regIdFromConfig != null)
			edtRegID.setText(regIdFromConfig);
		if (((Date) theMgr.getGlobalObject("PROCESS_DATE")) == null && store == null && register == null) {
			edtDrawerFund.setText("");
		} else if (register != null) {
			String cash = register.getDrawerFund().stringValue();
			edtDrawerFund.setText(cash.toString());
		}
	}

	// MP: Brings the Dialog state back to initial state.
	public void setEnabledForReset() {
		edtStoreID.setEnabled(false);
		edtRegID.setEnabled(false);
		edtUserID.setEnabled(true);
		edtPassword.setEnabled(true);
		edtUserID.setBackground(Color.white);
		edtPassword.setBackground(Color.white);
		edtStoreID.setBackground(theme.getEditArea());
		edtRegID.setBackground(theme.getEditArea());
		if (((Date) theMgr.getGlobalObject("PROCESS_DATE")) == null) {
			edtDrawerFund.setEnabled(true);
			edtDrawerFund.setBackground(Color.white);
		} else {
			edtDrawerFund.setEnabled(false);
			edtDrawerFund.setBackground(theme.getEditArea());
		}
	}

	public Date getBusinessDate() {
		try {
			return df.parse(edtBusinessdate.getText());
		} catch (Exception e) {
			return null;
		}
	}

	/**
	 * Methods that validates the entry for 'Amount' text field.
	 */
	private boolean verifyDrawerAmount() {
		IApplicationManager theAppMgr = (IApplicationManager) theMgr;
		Object amount = MaskManager.getInstance(theAppMgr).getMaskVerify(edtDrawerFund.getText(), theAppMgr.CURRENCY_MASK);
		if (amount == null) {
			theAppMgr.showErrorDlg(res.getString("Please enter the drawer fund in correct format"));
			edtDrawerFund.setText("0");
			return false;
		}
		return true;
	}
}
