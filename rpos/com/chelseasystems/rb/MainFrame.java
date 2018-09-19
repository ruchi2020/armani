/**
 * @copyright (c) 2001 Chelsea Market Systems LLC
 */

package com.chelseasystems.rb;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.border.*;
import javax.swing.event.*;
import java.util.*;
import java.io.*;
import com.chelseasystems.cr.swing.dlg.AboutDlg;
import com.chelseasystems.oi.*;
import com.chelseasystems.ab.*;
import com.chelseasystems.cr.appmgr.*;
import com.chelseasystems.cr.util.*;
import com.chelseasystems.cr.swing.bean.*;
import com.chelseasystems.cr.swing.*;
import com.chelseasystems.cr.config.*;
import com.chelseasystems.cr.swing.layout.RolodexLayout;
import com.chelseasystems.cr.swing.CMSJFileChooserHelper;
import java.beans.*;

/**
 * The main frame that controls the GUI Architect for receipt building
 * @author       Dan Reading
 */
public class MainFrame extends JFrame implements AttributeConstants {
	/** Flag whether screen is displaying a blueprint. */
	private boolean isGUIFullyConstructed = false;
	private boolean isFtrGUIFullyConstructed = false;
	private boolean isFootersViewEdittingFooter = false;
	private boolean isBlueprintModified = false;
	private boolean isFooterModified = false;
	/** The blueprint object. */
	private ReceiptBlueprint receiptBlueprint;
	private ReceiptBlueprint receiptFooter;
	/** Currently selected receiptLine. */
	private LineGroupPanel lineGroupPanel;
	private LineGroupPanel ftrLineGroupPanel;
	/** Currently selected receiptElement. */
	private ReceiptElementJTextField receiptElementJTextField;
	private ReceiptElementJTextField ftrReceiptElementJTextField;
	private Object[][] elementAttributes;
	private Object[][] ftrElementAttributes;
	private Object[][] methodElementAttributes;
	private Object[][] booleanElementAttributes;
	private Object[][] dateElementAttributes;
	private Object[][] numberElementAttributes;
	private Object[][] currencyElementAttributes;
	private Object[][] attributeGUIRepresentations;
	private Object[][] attributeFtrGUIRepresentations;
	private String currentLookAndFeel;
	private String[] currentLookAndFeels = { new String(), new String() };
	private String footerTitle = "Receipt Builder";
	private String receiptTitle = "Receipt Builder";
	Locale locale;
	JMenu menuView;
	ButtonGroup languageChoices;

	JPanel pnlMain = new JPanel();
	JPanel pnlMainFooters = new JPanel();
	JPanel pnlFootersWorkArea = new JPanel();
	JPanel pnlMainReceipts = new JPanel();
	JPanel pnlReceiptsWorkArea = new JPanel();
	RolodexLayout cardLayout = new RolodexLayout();

	JLabel statusBar = new JLabel();
	TitledBorder titledBorder3;
	TitledBorder titledBorder4;
	JPanel pnlReceiptReports = new JPanel();
	ReceiptReportsTab receiptReportsTab = new ReceiptReportsTab();
	private AttributesPanel pnlElementAttributes;
	private AttributesPanel pnlReceiptAttributes;
	ObjectInspectorPnl pnlObjectInspector = new ObjectInspectorPnl(this, true);
	JSplitPane leftSplitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, true, pnlObjectInspector, pnlReceiptReports);
	JSplitPane rightSplitPane;
	JSplitPane mainSplitPane;
	JCMSToolBar toolBar = new JCMSToolBar();
	JCMSToolBar toolBar2 = new JCMSToolBar();
	MenuFileSaveBpt menuFileSave = new MenuFileSaveBpt();
	MenuFilePrintBbt menuFilePrintBbt = new MenuFilePrintBbt();
	MenuFileOpenRdo menuFileOpenRdo = new MenuFileOpenRdo();
	MenuFileDetachRDO menuFileDetachRDO = new MenuFileDetachRDO();
	MenuFileCloseBpt menuFileClose = new MenuFileCloseBpt();
	MenuEditMoveUpReceiptLine menuEditMoveUpReceiptLine = new MenuEditMoveUpReceiptLine();
	MenuEditAddReceiptReport menuEditAddReceiptReport = new MenuEditAddReceiptReport();
	MenuEditDeleteReceiptReport menuEditDeleteReceiptReport = new MenuEditDeleteReceiptReport();
	MenuEditAddReceiptLine menuEditAddReceiptLine = new MenuEditAddReceiptLine();
	MenuEditDeleteReceiptLine menuEditDeleteReceiptLine = new MenuEditDeleteReceiptLine();
	MenuEditMoveDownReceiptLine menuEditMoveDownReceiptLine = new MenuEditMoveDownReceiptLine();
	MenuEditAddReceiptElement menuEditAddReceiptElement = new MenuEditAddReceiptElement();
	MenuEditDeleteReceiptElement menuEditDeleteReceiptElement = new MenuEditDeleteReceiptElement();
	MenuViewFtr menuViewFooters = new MenuViewFtr();

	JPanel pnlFooter = new JPanel();
	private AttributesPanel pnlFtrElementAttributes;
	JPanel pnlDummyObjectInspector = new JPanel();
	JSplitPane leftFtrSplitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, true, pnlDummyObjectInspector, pnlFooter);
	JPanel rightFtrSplitPane;
	JSplitPane mainFtrSplitPane;

	Menu2ViewReceipts menu2ViewReceipts = new Menu2ViewReceipts();
	Menu2FileNewFtr menu2FileNewFtr = new Menu2FileNewFtr();
	Menu2FileOpenFtr menu2FileOpenFtr = new Menu2FileOpenFtr();
	Menu2FileSaveFtr menu2FileSaveFtr = new Menu2FileSaveFtr();
	Menu2FileCloseFtr menu2FileCloseFtr = new Menu2FileCloseFtr();
	Menu2EditAddFtrElement menu2EditAddFtrElement = new Menu2EditAddFtrElement();
	Menu2EditDeleteFtrElement menu2EditDeleteFtrElement = new Menu2EditDeleteFtrElement();

	JMenuBar menuBar1;
	JMenuBar menuBar2;
	JMenu menuOptions;
	JMenu menuHelp;
	JMenuItem viewReceipts;
	JMenuItem viewFooters;
	ReceiptFooterPane receiptFooterTab;
	ComboBoxCell rcptStoreFooter;

	//Construct the frame
	public MainFrame() {
		enableEvents(AWTEvent.WINDOW_EVENT_MASK);
		System.setProperty("USER_CONFIG", "client_master.cfg");
		try {
			this.setTitle("Receipt Builder");
			this.setSize(Toolkit.getDefaultToolkit().getScreenSize());
			jbInit();
			updateMenus();
			setMessageBundles();
			this.pack();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	//Component initialization
	private void jbInit() throws Exception {
		TitledBorder titledBorderFtr = new TitledBorder(new EtchedBorder(EtchedBorder.RAISED, Color.white, new Color(148, 145, 140)), "Receipt Footer");
		TitledBorder titledBorderFtrElAttrib = new TitledBorder(BorderFactory.createEtchedBorder(Color.white, new Color(148, 145, 140)), "Footer Data Element Attributes");
		pnlFooter.setLayout(new BorderLayout());
		pnlFooter.setBorder(titledBorderFtr);
		setUpPnlFtrElementAttributes();
		pnlFtrElementAttributes.setVisible(false);
		pnlFtrElementAttributes.setBorder(titledBorderFtrElAttrib);
		rightFtrSplitPane = new JPanel();
		rightFtrSplitPane.setLayout(new BorderLayout());
		rightFtrSplitPane.add(pnlFtrElementAttributes, BorderLayout.CENTER);
		mainFtrSplitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, true, leftFtrSplitPane, rightFtrSplitPane);
		receiptFooterTab = new ReceiptFooterPane();
		receiptFooterTab.setLayout(new BorderLayout());
		pnlFooter.add(receiptFooterTab, BorderLayout.CENTER);

		Border lowerBorder = BorderFactory.createLoweredBevelBorder();
		Border border1 = BorderFactory.createBevelBorder(BevelBorder.RAISED, Color.white, Color.white, new java.awt.Color(134, 134, 134), new java.awt.Color(93, 93, 93));
		Border border2 = BorderFactory.createCompoundBorder(border1, lowerBorder);
		Border border3 = BorderFactory.createBevelBorder(BevelBorder.RAISED, Color.white, Color.white, new java.awt.Color(134, 134, 134), new java.awt.Color(93, 93, 93));
		Border border4 = BorderFactory.createCompoundBorder(border3, lowerBorder);
		TitledBorder titledBorder1 = new TitledBorder(BorderFactory.createEtchedBorder(Color.white, new Color(148, 145, 140)), "Receipt Sample Data Objects");
		TitledBorder titledBorder2 = new TitledBorder(new EtchedBorder(EtchedBorder.RAISED, Color.white, new Color(148, 145, 140)), "Receipt Report");
		titledBorder3 = new TitledBorder(BorderFactory.createEtchedBorder(Color.white, new Color(148, 145, 140)), "Receipt Data Element Attributes");
		titledBorder4 = new TitledBorder(BorderFactory.createEtchedBorder(Color.white, new Color(148, 145, 140)), "Receipt Attributes");
		statusBar.setBorder(border2);
		statusBar.setText(" ");
		pnlReceiptReports.setLayout(new BorderLayout());
		setUpPnlElementAttributes();
		pnlElementAttributes.setVisible(false);
		pnlElementAttributes.setBorder(titledBorder3);
		setUpPnlReceiptAttributes();
		pnlReceiptAttributes.setBorder(titledBorder4);
		rightSplitPane = new JSplitPane(JSplitPane.VERTICAL_SPLIT, true, pnlReceiptAttributes, pnlElementAttributes);
		mainSplitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, true, leftSplitPane, rightSplitPane);
		pnlObjectInspector.setBorder(titledBorder1);
		pnlReceiptReports.setBorder(titledBorder2);
		receiptReportsTab.setTabPlacement(JTabbedPane.BOTTOM);
		//receiptReportsTab.addTab("Receipt Report One", new JPanel());
		pnlReceiptReports.add(receiptReportsTab, BorderLayout.CENTER);
		menuBar1 = new JMenuBar();
		toolBar.setFloatable(false);
		toolBar.setBorder(BorderFactory.createEtchedBorder(Color.white, new Color(142, 142, 142)));
		JMenu menuFile = new JMenu("File");
		menuFile.setMnemonic('F');
		MenuFileNewBpt menuFileNewBpt = new MenuFileNewBpt();
		JMenuItem mi = menuFile.add(menuFileNewBpt);
		mi.setMnemonic('N');
		mi.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_N, KeyEvent.CTRL_MASK));
		toolBar.add(menuFileNewBpt);
		MenuFileOpenBpt menuFileOpenBpt = new MenuFileOpenBpt();
		mi = menuFile.add(menuFileOpenBpt);
		mi.setMnemonic('O');
		mi.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_O, KeyEvent.CTRL_MASK));
		toolBar.add(menuFileOpenBpt, false);
		mi = menuFile.add(menuFileSave);
		mi.setMnemonic('S');
		mi.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_S, KeyEvent.CTRL_MASK));
		toolBar.add(menuFileSave, false);
		mi = menuFile.add(menuFilePrintBbt);
		mi.setMnemonic('P');
		mi.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_P, KeyEvent.CTRL_MASK));
		toolBar.add(menuFilePrintBbt, false);
		menuFile.addSeparator();
		toolBar.addSeparator();
		mi = menuFile.add(menuFileOpenRdo);
		mi.setMnemonic('R');
		mi.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_R, KeyEvent.CTRL_MASK));
		toolBar.add(menuFileOpenRdo, false);
		mi = menuFile.add(menuFileDetachRDO);
		mi.setMnemonic('D');
		mi.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_D, KeyEvent.CTRL_MASK));
		toolBar.add(menuFileDetachRDO, false);
		menuFile.addSeparator();
		toolBar.addSeparator();
		mi = menuFile.add(menuFileClose);
		mi.setMnemonic('C');
		MenuFileExit menuFileExit = new MenuFileExit();
		mi = menuFile.add(menuFileExit);
		mi.setMnemonic('X');
		menuBar1.add(menuFile);
		JMenu menuEdit = new JMenu("Edit");
		menuEdit.setMnemonic('E');
		mi = menuEdit.add(menuEditAddReceiptReport);
		mi.setMnemonic('R');
		toolBar.add(menuEditAddReceiptReport, false);
		mi = menuEdit.add(menuEditDeleteReceiptReport);
		mi.setMnemonic('C');
		toolBar.add(menuEditDeleteReceiptReport, false);
		menuEdit.addSeparator();
		toolBar.addSeparator();
		mi = menuEdit.add(menuEditAddReceiptLine);
		mi.setMnemonic('L');
		toolBar.add(menuEditAddReceiptLine, false);
		mi = menuEdit.add(menuEditDeleteReceiptLine);
		mi.setMnemonic('N');
		toolBar.add(menuEditDeleteReceiptLine, false);
		mi = menuEdit.add(menuEditMoveUpReceiptLine);
		mi.setMnemonic('U');
		mi.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_UP, KeyEvent.CTRL_MASK));
		toolBar.add(menuEditMoveUpReceiptLine, false);
		mi = menuEdit.add(menuEditMoveDownReceiptLine);
		mi.setMnemonic('W');
		mi.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_DOWN, KeyEvent.CTRL_MASK));
		toolBar.add(menuEditMoveDownReceiptLine, false);
		menuEdit.addSeparator();
		toolBar.addSeparator();
		mi = menuEdit.add(menuEditAddReceiptElement);
		mi.setMnemonic('A');
		mi.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_A, KeyEvent.CTRL_MASK));
		toolBar.add(menuEditAddReceiptElement, false);
		mi = menuEdit.add(menuEditDeleteReceiptElement);
		mi.setMnemonic('D');
		mi.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_DELETE, 0));
		toolBar.add(menuEditDeleteReceiptElement, false);
		menuBar1.add(menuEdit);
		menuView = new JMenu("View");
		menuView.setMnemonic('V');
		viewFooters = menuView.add(menuViewFooters);
		viewFooters.setMnemonic('F');
		viewFooters.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_F, KeyEvent.CTRL_MASK));
		toolBar.add(menuViewFooters, true);
		viewReceipts = menuView.add(menu2ViewReceipts);
		viewReceipts.setMnemonic('R');
		viewReceipts.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_R, KeyEvent.CTRL_MASK));
		viewReceipts.setVisible(false);
		// add supported locales
		menuView.addSeparator();
		languageChoices = new ButtonGroup();
		final Locale[] locales = LocaleManager.getInstance().getSupportedLocales();
		for (int i = 0; i < locales.length; i++) {
			final Locale locale = locales[i];
			JRadioButtonMenuItem radioButton = new JRadioButtonMenuItem(locale.getDisplayName(), LocaleManager.getInstance().getFlagForLocale(locale));
			radioButton.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					setCMSLocale(locale);
					statusBar.setText(locale.getDisplayName());
				}
			});
			if (locale.equals(LocaleManager.getInstance().getDefaultLocale())) {
				radioButton.setSelected(true);
				statusBar.setText(locale.getDisplayName());
				MainFrame.this.locale = LocaleManager.getInstance().getDefaultLocale();
			}
			languageChoices.add(radioButton);
			menuView.add(radioButton);
		}
		menuBar1.add(menuView);

		menuOptions = new JMenu("Options");
		menuOptions.setMnemonic('O');
		final JCheckBoxMenuItem showToolBar = new JCheckBoxMenuItem("Show Tool Bar", CMSImageIcons.getInstance().getHammer(), true);
		showToolBar.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				toolBar.setVisible(showToolBar.getState());
				toolBar2.setVisible(showToolBar.getState());
			}
		});
		mi = menuOptions.add(showToolBar);
		mi.setMnemonic('T');
		menuOptions.addSeparator();
		ButtonGroup lookAndFeelChoices = new ButtonGroup();
		UIManager.LookAndFeelInfo[] availableLookAndFeels = UIManager.getInstalledLookAndFeels();
		// Allow the user to specify the look-and-feel, a nice bell
		if (availableLookAndFeels == null) {
			System.out.println("look and feels are null for this system...");
		} else {
			for (int i = 0; i < availableLookAndFeels.length; i++) {
				JRadioButtonMenuItem lookAndFeelRadioButton = new JRadioButtonMenuItem(availableLookAndFeels[i].getName(), CMSImageIcons.getInstance().getLookAndFeel());
				final String lookAndFeelClassName = availableLookAndFeels[i].getClassName();
				lookAndFeelRadioButton.addActionListener(new ActionListener() {

					public void actionPerformed(ActionEvent e) {
						setLookAndFeel(lookAndFeelClassName);
					}
				});
				if (availableLookAndFeels[i].getName().equals("Metal")) {
					lookAndFeelRadioButton.setSelected(true);
					setLookAndFeel(lookAndFeelClassName);
				}
				lookAndFeelChoices.add(lookAndFeelRadioButton);
				menuOptions.add(lookAndFeelRadioButton);
			}
		}
		menuBar1.add(menuOptions);

		menuHelp = new JMenu("Help");
		menuHelp.setMnemonic('H');
		MenuHelpAbout menuHelpAbout = new MenuHelpAbout();
		mi = menuHelp.add(menuHelpAbout);
		mi.setMnemonic('A');
		menuBar1.add(menuHelp);
		this.setJMenuBar(menuBar1);
		receiptReportsTab.addContainerListener(new ContainerAdapter() {
			public void componentAdded(ContainerEvent e) {

				updateMenus();
			}

			public void componentRemoved(ContainerEvent e) {
				updateMenus();
			}
		});
		receiptFooterTab.addContainerListener(new ContainerAdapter() {
			public void componentAdded(ContainerEvent e) {

				updateMenus();
			}

			public void componentRemoved(ContainerEvent e) {
				updateMenus();
			}
		});

		/*   JPanel pnlMainFooters = new JPanel();
		 JPanel pnlFootersWorkArea = new JPanel();
		 JPanel pnlMainReceipts = new JPanel();
		 JPanel pnlReceiptsWorkArea = new JPanel();
		 */
		this.getContentPane().add(pnlMain, BorderLayout.NORTH);
		this.getContentPane().add(statusBar, BorderLayout.SOUTH);

		pnlMain.setLayout(cardLayout);
		pnlMain.add(pnlMainReceipts, "RECEIPTS");
		pnlMain.add(pnlMainFooters, "FOOTERS");
		cardLayout.show(pnlMain, "RECEIPTS");

		pnlMainFooters.setLayout(new BorderLayout());
		pnlMainFooters.add(toolBar2, BorderLayout.NORTH);
		pnlMainFooters.add(pnlFootersWorkArea, BorderLayout.CENTER);

		pnlFootersWorkArea.setBackground(new java.awt.Color(142, 158, 192));
		pnlFootersWorkArea.setLayout(new BorderLayout());
		double r = Toolkit.getDefaultToolkit().getScreenSize().getWidth() / 1024;
		pnlFootersWorkArea.setPreferredSize(new Dimension((int) (1016 * r), (int) (666 * r)));
		mainFtrSplitPane.setDividerLocation((int) (700 * r));
		leftFtrSplitPane.setDividerLocation((int) (300 * r));

		pnlMainReceipts.setLayout(new BorderLayout());
		pnlMainReceipts.add(toolBar, BorderLayout.NORTH);
		pnlMainReceipts.add(pnlReceiptsWorkArea, BorderLayout.CENTER);
		pnlReceiptsWorkArea.setBackground(new java.awt.Color(142, 158, 192));
		pnlReceiptsWorkArea.setLayout(new BorderLayout());

		pnlReceiptsWorkArea.setPreferredSize(new Dimension((int) (1016 * r), (int) (666 * r)));
		mainSplitPane.setDividerLocation((int) (700 * r));
		leftSplitPane.setDividerLocation((int) (300 * r));
		this.setIconImage(CMSImageIcons.getInstance().getTitlebar().getImage());

		menuBar2 = new JMenuBar();
		toolBar2.setFloatable(false);
		toolBar2.setBorder(BorderFactory.createEtchedBorder(Color.white, new Color(142, 142, 142)));
		JMenu menu2File = new JMenu("File");
		menu2File.setMnemonic('F');
		mi = menu2File.add(menu2FileNewFtr);
		mi.setMnemonic('N');
		mi.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_N, KeyEvent.CTRL_MASK));
		toolBar2.add(menu2FileNewFtr);
		mi = menu2File.add(menu2FileOpenFtr);
		mi.setMnemonic('O');
		mi.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_O, KeyEvent.CTRL_MASK));
		toolBar2.add(menu2FileOpenFtr);

		mi = menu2File.add(menu2FileSaveFtr);
		mi.setMnemonic('S');
		mi.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_S, KeyEvent.CTRL_MASK));
		toolBar2.add(menu2FileSaveFtr);

		menu2File.addSeparator();
		toolBar2.addSeparator();

		mi = menu2File.add(menu2FileCloseFtr);
		mi.setMnemonic('C');
		mi.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_C, KeyEvent.CTRL_MASK));
		toolBar2.add(menu2FileCloseFtr);

		mi = menu2File.add(menuFileExit);
		mi.setMnemonic('X');
		toolBar2.add(menuFileExit);
		menuBar2.add(menu2File);

		JMenu menu2Edit = new JMenu("Edit");
		menu2Edit.setMnemonic('E');

		mi = menu2Edit.add(menu2EditAddFtrElement);
		mi.setMnemonic('A');
		mi.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_A, KeyEvent.CTRL_MASK));
		toolBar2.add(menu2EditAddFtrElement);
		mi = menu2Edit.add(menu2EditDeleteFtrElement);
		mi.setMnemonic('D');
		mi.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_D, KeyEvent.CTRL_MASK));
		toolBar2.add(menu2EditDeleteFtrElement);
		menuBar2.add(menu2Edit);

		//  add this to the one view menu instead, we'll make only the appropriate one appear
		//mi = menu2View.add(menu2ViewReceipts);
		//mi.setMnemonic('R');
		//mi.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_R, KeyEvent.CTRL_MASK));
		toolBar2.add(menu2ViewReceipts, true);
	}

	private void setMessageBundles() {
		ConfigMgr configMgr = new ConfigMgr(System.getProperty("USER_CONFIG"));
		String messageBundles;
		try {
			messageBundles = configMgr.getString("MESSAGE_BUNDLE");
		} catch (Exception e) {
			messageBundles = "";
		}
		if (messageBundles.length() < 2)
			System.out.println("Unable to set the message bundle concat list from the user config file (eg client_master).  Using default list instead");
		else
			ResourceManager.setResourceBundle(messageBundles);
	}

	/**
	 */
	private boolean isBlueprintModified() {
		return isBlueprintModified;
	}

	/**
	 */
	private void setBlueprintModified(boolean isModified) {
		isBlueprintModified = isModified;
	}

	public void setModified(boolean isModified) {
		if (cardLayout.getCurrent(pnlMain) == pnlMainReceipts)
			setBlueprintModified(isModified);
		else
			setFooterModified(isModified);
	}

	private boolean isFooterModified() {
		return isFooterModified;
	}

	/**
	 */
	private void setFooterModified(boolean isModified) {
		isFooterModified = isModified;
	}

	//File | OpenReceiptBlueprint... action performed
	void fileNewBpt_actionPerformed(ActionEvent e) {
		//  to do:
		//  enable menu options that apply to open blueprint
		if (receiptBlueprint != null) {
			int ok = JOptionPane.showOptionDialog(this, "The new blueprint will overlay the one you are currently editing,\nselect cancel if you want to save the current one first.",
					"Overlay current blueprint?", JOptionPane.OK_CANCEL_OPTION, JOptionPane.QUESTION_MESSAGE, null, null, null);
			if (ok != JOptionPane.OK_OPTION)
				return;
		}
		String bptName = JOptionPane.showInputDialog(this, "Please enter the name for the new Receipt Blueprint.", "Enter Blueprint name", JOptionPane.QUESTION_MESSAGE);
		if (bptName == null)
			return;
		if (bptName.endsWith(".bpt"))
			bptName = bptName.substring(0, bptName.length() - 4);
		receiptBlueprint = new ReceiptBlueprint(bptName);
		setBlueprintModified(false);
		ReceiptReport[] receiptReports = { new ReceiptReport() };
		receiptBlueprint.setReceiptReports(receiptReports);
		receiptReports[0].setName("New Receipt Report");
		loadBlueprint();
		statusBar.setText(" ");
		setBlueprintModified(false);
		Properties props = System.getProperties();
		receiptTitle = "Receipt Builder  " + props.get("default.bpt_folder") + File.separatorChar + bptName + ".bpt";
		this.setTitle(receiptTitle);
		char[] separatorArray = { File.separatorChar };
		String separatorString = new String(separatorArray);
		String pathName = new String(props.get("default.bpt_folder") + separatorString + bptName + ".bpt");
		File bptFile = new File(pathName);
		props.put("default.bpt_file", bptFile);
	}

	//File | OpenReceiptBlueprint... action performed
	void fileOpenBpt_actionPerformed(ActionEvent e) {
		if (!close())
			return;
		Properties props = System.getProperties();
		JFileChooser chooser = CMSJFileChooserHelper.fixFileChooser((new JFileChooser((String) props.get("user.dir"))));
		String previousDir = (String) props.get("default.bpt_folder");
		if (previousDir != null && previousDir.length() > 0)
			chooser.setCurrentDirectory(new File(previousDir));
		ExampleFileFilter filter = new ExampleFileFilter();
		filter.addExtension("bpt");
		filter.setDescription("Receipt Blueprints");
		chooser.setFileFilter(filter);
		int returnVal = chooser.showOpenDialog(this);
		if (returnVal == JFileChooser.APPROVE_OPTION) {
			String fileName = chooser.getSelectedFile().getName();
			props.put("default.bpt_file", chooser.getSelectedFile());
			props.put("default.bpt_folder", chooser.getCurrentDirectory().toString());
			receiptTitle = "Receipt Builder  " + props.get("default.bpt_folder") + File.separatorChar + fileName;
			this.setTitle(receiptTitle);
			System.setProperties(props);
			StringBuffer fnBuf = new StringBuffer();
			fnBuf.append(chooser.getCurrentDirectory());
			fnBuf.append(File.separatorChar);
			fnBuf.append(fileName);
			try {
				ObjectStore objectStore = new ObjectStore(fnBuf.toString());
				Object temp = objectStore.read();
				if (!(temp instanceof ReceiptBlueprint)) {
					JOptionPane.showMessageDialog(this, "The file does not appear to be a valid blueprint object", "Invalid Blueprint", JOptionPane.ERROR_MESSAGE);
					return;
				}

				receiptBlueprint = ((ReceiptBlueprint) temp).cloneAsCurrentClassVersion();
				setBlueprintModified(false);

			} catch (Exception ex) {
				System.out.println(ex.getClass());
				if (ex.getMessage().endsWith("does not contain a serialized object"))
					JOptionPane.showMessageDialog(this, "The file does not appear to contain a valid blueprint object", "Not a Serialized Object", JOptionPane.ERROR_MESSAGE);
				else
					JOptionPane.showMessageDialog(this, "Unable to unserialize object.\n" + ex.getMessage(), "Open File", JOptionPane.ERROR_MESSAGE);
				return;
			}
			loadBlueprint();
			statusBar.setText(" ");
			setBlueprintModified(false);
		}
	}

	//File | OpenReceiptDataObject... action performed
	void fileOpenRdo_actionPerformed(ActionEvent e) {
		Properties props = System.getProperties();
		JFileChooser chooser = CMSJFileChooserHelper.fixFileChooser(new JFileChooser((String) props.get("user.dir")));
		String previousDir = (String) props.get("default.rdo_folder");
		if (previousDir != null && previousDir.length() > 0)
			chooser.setCurrentDirectory(new File(previousDir));
		ExampleFileFilter filter = new ExampleFileFilter();
		filter.addExtension("rdo");
		filter.setDescription("Receipt Sample Data Objects");
		chooser.setFileFilter(filter);
		int returnVal = chooser.showOpenDialog(this);
		if (returnVal == JFileChooser.APPROVE_OPTION) {
			String fileName = chooser.getSelectedFile().getName();
			props.put("default.file", chooser.getSelectedFile());
			props.put("default.rdo_folder", chooser.getCurrentDirectory().toString());
			System.setProperties(props);
			StringBuffer fnBuf = new StringBuffer();
			fnBuf.append(chooser.getCurrentDirectory());
			fnBuf.append(File.separatorChar);
			fnBuf.append(fileName);
			try {
				ObjectStore objectStore = new ObjectStore(fnBuf.toString());
				Object temp = objectStore.read();
				// Initially, I envisioned a receipt being generated from a list of business
				// objects.  As things evolved, it became clear that it was easier to wrap
				// the set of objects necessary for a receipt into an AppModel.  Therefore,
				// it is not necessary to have multiple RDOs attached to a receipt blueprint.  djr
				//if(receiptBlueprint.getReceiptObjects() == null)
				receiptBlueprint.setReceiptObjects(new Object[] { temp });
				//else
				//   receiptBlueprint.setReceiptObjects(ReceiptBuilderUtility.addArrayItem(receiptBlueprint.getReceiptObjects(),
				//         temp));
				pnlObjectInspector.loadUpMultipleObjects("Receipt Data Object", receiptBlueprint.getReceiptObjects());
				validateRDO();
			} catch (Exception ex) {
				System.out.println("error unserializing RDO: " + ex.getMessage());
				ex.printStackTrace();
			}
		}
	}

	//File | Save... action performed
	public boolean saveFile() {
		if (!(receiptBlueprint.getReceiptObjects() == null || receiptBlueprint.getReceiptObjects().length == 0)) {
			int selected = JOptionPane.showOptionDialog(this, "You should unplug the RDO samples before saving.  "
					+ "If these objects become invalid later, you will not be able to un-serialize this blueprint.", "RDO objects should be unplugged", JOptionPane.OK_CANCEL_OPTION,
					JOptionPane.QUESTION_MESSAGE, null, null, null);
			if (selected != JOptionPane.OK_OPTION)
				return false;
		}
		Properties props = System.getProperties();
		JFileChooser chooser;
		if (props.get("default.bpt_folder") == null) {
			chooser = new JFileChooser((String) props.get("user.dir"));
		} else {
			chooser = new JFileChooser((String) props.get("default.bpt_folder"));
		}
		CMSJFileChooserHelper.fixFileChooser(chooser);
		ExampleFileFilter filter = new ExampleFileFilter();
		filter.addExtension("bpt");
		filter.setDescription("Receipt Blueprints");
		chooser.setFileFilter(filter);
		if (props.get("default.bpt_file") != null) {
			chooser.setSelectedFile((File) props.get("default.bpt_file"));
		}
		int returnVal = chooser.showSaveDialog(this);
		if (returnVal == JFileChooser.APPROVE_OPTION) {
			if (!(chooser.getSelectedFile().getName().endsWith(".bpt"))) {
				JOptionPane.showMessageDialog(this, "That is not a valid blueprint file name.\nA blueprint must have an extension of \"bpt\")", "Not a .bpt file", JOptionPane.ERROR_MESSAGE);
				return false;
			}
			if (chooser.getSelectedFile().canRead()) {
				if (!chooser.getSelectedFile().canWrite()) {
					JOptionPane.showMessageDialog(this, "The selected file is locked against update access.", "File Locked", JOptionPane.ERROR_MESSAGE);
					return false;
				}
				int ok = JOptionPane.showOptionDialog(this, "file " + chooser.getSelectedFile().getName() + " already exists.  Do you want to replace it?", "Replace existing file?",
						JOptionPane.OK_CANCEL_OPTION, JOptionPane.QUESTION_MESSAGE, null, null, null);
				if (ok != JOptionPane.OK_OPTION)
					return false;
			}
			setWaitCursor(true);
			//receiptBlueprint.setReceiptObjects(receiptDataObjects.toArray());
			String fileName = chooser.getSelectedFile().getName();
			props.put("default.bpt_file", chooser.getSelectedFile());
			props.put("default.bpt_folder", chooser.getCurrentDirectory().toString());
			receiptTitle = "Receipt Builder  " + props.get("default.bpt_folder") + File.separatorChar + fileName;
			this.setTitle(receiptTitle);
			System.setProperties(props);
			StringBuffer fnBuf = new StringBuffer();
			fnBuf.append(chooser.getCurrentDirectory());
			fnBuf.append(File.separatorChar);
			fnBuf.append(fileName);
			receiptBlueprint.setName(fileName.substring(0, fileName.lastIndexOf(".")));
			//System.out.println("set receipt name: " + receiptBlueprint.getName());
			try {
				ObjectStore objectStore = new ObjectStore(fnBuf.toString());
				cleanBlueprint();// uncomment this...
				objectStore.write(receiptBlueprint);
				//loadBlueprint();// comment this...
				setBlueprintModified(false);
				statusBar.setText("Saved File: " + fnBuf.toString());
				return true;
			} catch (Exception ex) {
				ex.printStackTrace();
				JOptionPane.showMessageDialog(this, "Fatal Error serializing blueprint - " + ex.getMessage(), "Save File", JOptionPane.ERROR_MESSAGE);
			} finally {
				setWaitCursor(false);
			}
		}
		return false;
	}

	public boolean saveFooter() {
		Properties props = System.getProperties();
		JFileChooser chooser;
		if (props.get("default.ftr_folder") == null) {
			System.out.println("create default chooser");
			chooser = new JFileChooser();
		} else {
			System.out.println("create chooser" + props.get("default.ftr_folder"));
			chooser = new JFileChooser((String) props.get("default.ftr_folder"));
		}
		ExampleFileFilter filter = new ExampleFileFilter();
		filter.addExtension("ftr");
		filter.setDescription("Receipt Footers");
		chooser.setFileFilter(filter);
		if (props.get("default.ftr_file") != null) {
			chooser.setSelectedFile((File) props.get("default.ftr_file"));
		}
		System.out.println("showing file chooser fixed late...");
		CMSJFileChooserHelper.fixFileChooser(chooser);
		int returnVal = chooser.showSaveDialog(this);
		System.out.println("back from showing file chooser...");
		if (returnVal == JFileChooser.APPROVE_OPTION) {
			System.out.println("chooser selected file: " + chooser.getSelectedFile().getName());
			//chooser.approveSelection();
			if (!(chooser.getSelectedFile().getName().endsWith(".ftr"))) {
				JOptionPane.showMessageDialog(this, "That is not a valid footer file name.\nA footer must have an extension of \"ftr\")", "Not a .ftr file", JOptionPane.ERROR_MESSAGE);
				return false;
			}
			if (chooser.getSelectedFile().canRead()) {
				if (!chooser.getSelectedFile().canWrite()) {
					JOptionPane.showMessageDialog(this, "The selected file is locked against update access.", "File Locked", JOptionPane.ERROR_MESSAGE);
					return false;
				}
				int ok = JOptionPane.showOptionDialog(this, "file " + chooser.getSelectedFile().getName() + " already exists.  Do you want to replace it?", "Replace existing file?",
						JOptionPane.OK_CANCEL_OPTION, JOptionPane.QUESTION_MESSAGE, null, null, null);
				if (ok != JOptionPane.OK_OPTION)
					return false;
			}
			setWaitCursor(true);
			//receiptBlueprint.setReceiptObjects(receiptDataObjects.toArray());
			String fileName = chooser.getSelectedFile().getName();
			props.put("default.ftr_file", chooser.getSelectedFile());
			props.put("default.ftr_folder", chooser.getCurrentDirectory().toString());
			System.out.println("saving defualt directory: " + props.get("default.ftr_folder"));
			footerTitle = "Receipt Builder  " + props.get("default.ftr_folder") + File.separatorChar + fileName;
			this.setTitle(footerTitle);
			System.setProperties(props);
			StringBuffer fnBuf = new StringBuffer();
			fnBuf.append(chooser.getCurrentDirectory());
			fnBuf.append(File.separatorChar);
			fnBuf.append(fileName);
			receiptFooter.setName(fileName.substring(0, fileName.lastIndexOf(".")));
			try {
				ObjectStore objectStore = new ObjectStore(fnBuf.toString());
				objectStore.write(receiptFooter);
				setFooterModified(false);
				statusBar.setText("Saved File: " + fnBuf.toString());
				return true;
			} catch (Exception ex) {
				ex.printStackTrace();
				JOptionPane.showMessageDialog(this, "Fatal Error serializing footer - " + ex.getMessage(), "Save File", JOptionPane.ERROR_MESSAGE);
			} finally {
				reloadCboFooters();
				setWaitCursor(false);
			}
		}
		return false;
	}

	private void reloadCboFooters() {
		JComboBox cboFooter = ((JComboBox) rcptStoreFooter.getMyJComponent());
		cboFooter.removeAllItems();
		String[] footers = this.getFooterNames();
		for (int i = 0; i < footers.length; i++) {
			System.out.println("loading: " + footers[i]);
			cboFooter.addItem(footers[i]);
		}
	}

	/**
	 */
	protected void updateMenus() {
		boolean receiptIsLoaded = receiptReportsTab != null && receiptReportsTab.getComponentCount() > 0;

		menuFileSave.setEnabled(receiptIsLoaded);
		menuFilePrintBbt.setEnabled(receiptIsLoaded);
		menuFileOpenRdo.setEnabled(receiptIsLoaded);
		menuFileDetachRDO.setEnabled(receiptIsLoaded);
		menuFileClose.setEnabled(receiptIsLoaded);
		menuEditMoveUpReceiptLine.setEnabled(receiptIsLoaded);
		menuEditAddReceiptReport.setEnabled(receiptIsLoaded);
		menuEditDeleteReceiptReport.setEnabled(receiptIsLoaded);
		menuEditAddReceiptLine.setEnabled(receiptIsLoaded);
		menuEditDeleteReceiptLine.setEnabled(receiptIsLoaded);
		menuEditMoveDownReceiptLine.setEnabled(receiptIsLoaded);
		menuEditAddReceiptElement.setEnabled(receiptIsLoaded);
		menuEditDeleteReceiptElement.setEnabled(receiptIsLoaded);
		for (int i = 2; i < menuView.getItemCount(); i++)
			if (menuView.getItem(i) != null)
				menuView.getItem(i).setEnabled(receiptIsLoaded || receiptFooterTab.getReceiptBlueprint() != null);
		menu2FileSaveFtr.setEnabled(receiptFooterTab.getReceiptBlueprint() != null);
		menu2FileCloseFtr.setEnabled(receiptFooterTab.getReceiptBlueprint() != null);
		menu2EditAddFtrElement.setEnabled(receiptFooterTab.getReceiptBlueprint() != null);
		menu2EditDeleteFtrElement.setEnabled(receiptFooterTab.getReceiptBlueprint() != null);
	}

	/**
	 * @param e
	 */
	void filePrintBpt_actionPerformed(ActionEvent e) {
		if (receiptBlueprint == null) {
			JOptionPane.showMessageDialog(this, "Nothing to print, open a Receipt Blueprint and then attach a Receipt Data Object", "Print Error", JOptionPane.ERROR_MESSAGE);
			return;
		}
		if (receiptBlueprint.getReceiptObjects() == null || receiptBlueprint.getReceiptObjects().length == 0) {
			JOptionPane.showMessageDialog(this, "You must attach a Receipt Data Object to this blueprint so that it has something to print", "Print Error", JOptionPane.ERROR_MESSAGE);
			return;
		}
		boolean atLeastOneCopy = false;
		ReceiptReport[] receiptReports = receiptBlueprint.getReceiptReports();
		for (int i = 0; i < receiptReports.length; i++) {
			if (receiptReports[i].getCustCopyCount() > 0 || receiptReports[i].getStoreCopyCount() > 0) {
				atLeastOneCopy = true;
				break;
			}
		}
		if (!atLeastOneCopy) {
			JOptionPane.showMessageDialog(this, "There is nothing to print - all of the receipt reports specify zero copies for both store and customer", "Print Error", JOptionPane.ERROR_MESSAGE);
			return;
		}
		String restoreName = receiptBlueprint.getName();
		if (!storeTempBlueprint())
			return;
		//      StringBuffer fnBuf = new StringBuffer();
		//      Properties props = System.getProperties();
		//      if (props.get("default.bpt_folder") == null) {
		//         fnBuf.append("C:");
		//      }
		//      else {
		//         fnBuf.append((String)props.get("default.bpt_folder"));
		//      }
		//      fnBuf.append(File.separatorChar);
		//      fnBuf.append("temp.bpt");
		//      String restoreName = receiptBlueprint.getName();
		//      receiptBlueprint.setName("temp");
		//      try {
		//         ObjectStore objectStore = new ObjectStore(fnBuf.toString());
		//         cleanBlueprint();
		//         objectStore.write(receiptBlueprint);
		//         //loadBlueprint();
		ReceiptFactory.invalidateBlueprint(receiptBlueprint.getName());
		ReceiptReport[] reports = receiptBlueprint.getReceiptReports();
		for (int i = 0; i < reports.length; i++) {
			ReceiptFactory.invalidateBlueprint(reports[i].getStoreFooterFileName() + ".ftr");
		}
		ReceiptFactory.invalidateBlueprint(receiptBlueprint.getName() + ".bpt");
		ReceiptFactory receiptFactory = new ReceiptFactory(receiptBlueprint.getReceiptObjects(), receiptBlueprint.getName());
		ReceiptAppManager theAppMgr = new ReceiptAppManager(this);
		receiptFactory.setLocale(this.locale);
		receiptFactory.print(theAppMgr);
		receiptBlueprint.setName(restoreName);
		//      } catch (Exception ex) {
		//         System.out.println("error serializing object " + ex.getMessage());
		//         ex.printStackTrace();
		//         JOptionPane.showMessageDialog(this, "Fatal Error serializing blueprint - call support",
		//               "Fatal Error", JOptionPane.ERROR_MESSAGE);
		//         return;
		//      } finally {
		receiptBlueprint.setName(restoreName);
		setWaitCursor(false);
		//      }
	}

	private boolean storeTempBlueprint() {
		boolean success = false;
		StringBuffer fnBuf = new StringBuffer();
		Properties props = System.getProperties();
		//      if (props.get("default.bpt_folder") == null) {
		//         fnBuf.append("C:");
		//      }
		//      else {
		//         fnBuf.append((String)props.get("default.bpt_folder"));
		//      }
		//      fnBuf.append(File.separatorChar);
		//      fnBuf.append("temp.bpt");
		fnBuf.append(FileMgr.getLocalFile("receiptblueprints", "temp.bpt"));
		String restoreName = receiptBlueprint.getName();
		receiptBlueprint.setName("temp");
		try {
			ObjectStore objectStore = new ObjectStore(fnBuf.toString());
			cleanBlueprint();
			objectStore.write(receiptBlueprint);
			success = true;
		} catch (Exception ex) {
			receiptBlueprint.setName(restoreName);
			System.out.println("error serializing object " + ex.getMessage());
			ex.printStackTrace();
			JOptionPane.showMessageDialog(this, "Fatal Error serializing blueprint - call support", "Fatal Error", JOptionPane.ERROR_MESSAGE);
		} finally {
			setWaitCursor(false);
			return success;
		}
	}

	private void validateRDO() {
		String restoreName = receiptBlueprint.getName();
		if (!storeTempBlueprint())
			return;
		try {
			ReceiptFactory receiptFactory = new ReceiptFactory(receiptBlueprint.getReceiptObjects(), receiptBlueprint.getName());
			ReceiptAppManager theAppMgr = new ReceiptAppManager(this);
			receiptFactory.validateRDO(theAppMgr);
		} catch (InvalidRDOException e) {
			JOptionPane.showMessageDialog(this, e.getMessage(), "RDO Warning", JOptionPane.ERROR_MESSAGE);
		} catch (Exception ex) {
			System.out.println("error validating RDO " + ex.getMessage());
			ex.printStackTrace();
			JOptionPane.showMessageDialog(this, "Fatal Error serializing blueprint - call support", "Fatal Error", JOptionPane.ERROR_MESSAGE);
		} finally {
			receiptBlueprint.setName(restoreName);
			setWaitCursor(false);
		}
	}

	/**
	 * @param e
	 */
	void menuEditAddReceiptReport_actionPerformed(ActionEvent e) {
		String[] possibleValues = new String[receiptReportsTab.getTabCount() + 1];
		possibleValues[0] = "New Blank Report";
		ReceiptReport[] receiptReports = receiptBlueprint.getReceiptReports();
		for (int i = 1; i < receiptReports.length + 1; i++) {
			possibleValues[i] = "Copy Report " + receiptReports[i - 1].getName();
		}
		Object selectedValue = JOptionPane.showInputDialog(null, "Please select a report to copy, or indicate that you would like the new report to be blank", "Select Report to Clone",
				JOptionPane.INFORMATION_MESSAGE, null, possibleValues, possibleValues[0]);
		if (selectedValue == null)
			return;
		int selectedIndex = 0;
		for (; !((String) selectedValue).equals(possibleValues[selectedIndex]); selectedIndex++) {
		}
		selectedIndex--;
		if (selectedIndex == -1) {
			receiptBlueprint.setReceiptReports((ReceiptReport[]) ReceiptBuilderUtility.addArrayItem(receiptBlueprint.getReceiptReports(), new ReceiptReport("Receipt Report"
					+ (receiptBlueprint.getReceiptReports().length + 1))));
		} else {
			// create a deep clone by serializing the blueprint, then steal the requesteded report
			StringBuffer fnBuf = new StringBuffer();
			Properties props = System.getProperties();
			if (props.get("default.bpt_folder") == null) {
				fnBuf.append("C:");
			} else {
				fnBuf.append((String) props.get("default.bpt_folder"));
			}
			fnBuf.append(File.separatorChar);
			fnBuf.append("temp.bpt");
			try {
				ObjectStore objectStore = new ObjectStore(fnBuf.toString());
				cleanBlueprint();
				objectStore.write(receiptBlueprint);
				ReceiptBlueprint clonedReceiptBlueprint = (ReceiptBlueprint) objectStore.read();
				ReceiptReport[] clonedReceiptReports = clonedReceiptBlueprint.getReceiptReports();
				clonedReceiptReports[selectedIndex].setName("Receipt Report " + (receiptBlueprint.getReceiptReports().length + 1));
				receiptBlueprint.setReceiptReports((ReceiptReport[]) ReceiptBuilderUtility.addArrayItem(receiptBlueprint.getReceiptReports(), clonedReceiptReports[selectedIndex]));
			} catch (Exception ex) {
				System.out.println("error serializing object" + ex.getMessage());
				ex.printStackTrace();
				JOptionPane.showMessageDialog(this, "Fatal Error serializing blueprint - call support", "Fatal Error", JOptionPane.ERROR_MESSAGE);
				return;
			}
		}
		loadBlueprint();
		receiptReportsTab.setSelectedIndex(receiptReportsTab.getTabCount() - 1);
		setBlueprintModified(true);
	}

	/**
	 * @param e
	 */
	void menuEditDeleteReceiptReport_actionPerformed(ActionEvent e) {
		if (receiptBlueprint.getReceiptReports().length <= 1) {
			JOptionPane.showMessageDialog(this, "A receipt blueprint must have at least one receipt report", "can't delete all reports", JOptionPane.ERROR_MESSAGE);
			return;
		}
		int selected = JOptionPane.showOptionDialog(this, "Are you sure you want to delete the currently selected receipt report?", "Delete Report", JOptionPane.OK_CANCEL_OPTION,
				JOptionPane.QUESTION_MESSAGE, null, null, null); //  buttons,okButton);
		if (selected != JOptionPane.OK_OPTION)
			return;
		ReceiptReport[] receiptReports = receiptBlueprint.getReceiptReports();
		receiptBlueprint.setReceiptReports((ReceiptReport[]) ReceiptBuilderUtility.removeArrayItem(receiptReports, receiptReports[receiptReportsTab.getSelectedIndex()]));
		cleanBlueprint();
		loadBlueprint();
		setBlueprintModified(true);
	}

	/**
	 * Add line group to report.
	 * @param e
	 */
	void menuEditAddReceiptLine_actionPerformed(ActionEvent e) {
		//JScrollPane receiptReportTabPane = (JScrollPane)receiptReportsTab.getSelectedComponent();
		//Box receiptPanel = (Box)receiptReportTabPane.getViewport().getView();
		Box receiptPanel = getReceiptPanel();
		// first remove glue
		if (receiptPanel.getComponentCount() > 0)
			receiptPanel.remove(receiptPanel.getComponentCount() - 1);
		// create a receipt element and line
		ReceiptElement[] receiptElements = { new ReceiptElement("<---Text Element--->") };
		ReceiptLine receiptLine = new ReceiptLine(receiptElements);
		// determine selected report
		ReceiptReport[] receiptReports = receiptBlueprint.getReceiptReports();
		int reportX = receiptReportsTab.getSelectedIndex();
		receiptReports[reportX].addReceiptLine(receiptLine);
		// shadow this or it will think it is already the current and focused group
		LineGroupPanel lineGroupPanel = new LineGroupPanel(this, receiptLine);
		receiptPanel.add(lineGroupPanel);
		// create a physical gui panel
		JPanel physicalLinePanel = new JPanel();
		physicalLinePanel.setLayout(new BoxLayout(physicalLinePanel, BoxLayout.X_AXIS));
		//physicalLinePanel.setMaximumSize(new Dimension(600, 35));   why was I setting the max size on a line?  There was probably a damn good reason...
		lineGroupPanel.add(physicalLinePanel);
		// create and add gui representation
		ReceiptElementJTextField initialElement = new ReceiptElementJTextField(this, receiptElements[0], pnlElementAttributes);
		physicalLinePanel.add(initialElement);
		// try this.....
		physicalLinePanel.add(Box.createHorizontalGlue());
		receiptPanel.add(Box.createVerticalGlue());
		receiptPanel.validate();
		getReceiptScrollPane().repaint();

		initialElement.requestFocus();
		setBlueprintModified(true);
		pack();
		physicalLinePanel.scrollRectToVisible(physicalLinePanel.getBounds());
	}

	/**
	 * @param e
	 */
	void menuEditDeleteReceiptLine_actionPerformed(ActionEvent e) {
		if (getLineGroupPanel() == null) {
			JOptionPane.showMessageDialog(this, "Please first click on the line group you would like to delete", "Indicate Line Group", JOptionPane.ERROR_MESSAGE);
			return;
		}
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				getLineGroupPanel().requestFocus();
			}
		});
		ReceiptReport[] receiptReports = receiptBlueprint.getReceiptReports();
		int reportX = receiptReportsTab.getSelectedIndex();
		if (receiptReports[reportX].getReceiptLines().length == 1) {
			JOptionPane.showMessageDialog(this, "A receipt report must have at least one line group", "can't delete all line groups", JOptionPane.ERROR_MESSAGE);
			return;
		}
		int selected = JOptionPane.showOptionDialog(this, "Are you sure you want to delete this line group?", "Delete Line Group", JOptionPane.OK_CANCEL_OPTION, JOptionPane.QUESTION_MESSAGE, null,
				null, null); //  buttons,okButton);
		if (selected != JOptionPane.OK_OPTION)
			return;
		// set new receipt lines on reports
		receiptReports[reportX].setReceiptLines((ReceiptLine[]) ReceiptBuilderUtility.removeArrayItem(receiptReports[reportX].getReceiptLines(), getLineGroupPanel().getReceiptLine()));
		// update gui
		//JScrollPane receiptReportTabPane = (JScrollPane)receiptReportsTab.getSelectedComponent();
		Box receiptPanel = getReceiptPanel();
		if (receiptPanel.getComponentCount() > 0)
			receiptPanel.remove(receiptPanel.getComponentCount() - 1);
		receiptPanel.remove(getLineGroupPanel());
		// refresh borders
		Component[] remainingLineGroupPanels = receiptPanel.getComponents();
		for (int i = 0; i < remainingLineGroupPanels.length; i++)
			if (remainingLineGroupPanels[i] instanceof LineGroupPanel)
				((LineGroupPanel) remainingLineGroupPanels[i]).setBorder();
			else {
				//System.out.println("ERROR: There is a non-LineGroupPanel component in receiptPanel, I am removing it: " + remainingLineGroupPanels[i].getClass().getName());
				receiptPanel.remove(remainingLineGroupPanels[i]);
			}
		receiptPanel.add(Box.createVerticalGlue());
		receiptPanel.validate();
		receiptPanel.repaint();
		setLineGroupPanel(null);
		setBlueprintModified(true);
		pack();
	}

	/**
	 * @param e
	 */
	void menuEditMoveUpReceiptLine_actionPerformed(ActionEvent e) {
		//JScrollPane receiptReportTabPane = (JScrollPane)receiptReportsTab.getSelectedComponent();
		Box receiptPanel = getReceiptPanel();
		ReceiptReport[] receiptReports = receiptBlueprint.getReceiptReports();
		int rr = receiptReportsTab.getSelectedIndex();
		ReceiptLine[] lines = receiptReports[rr].getReceiptLines();
		if (lines == null)
			return;
		java.util.List list = Arrays.asList(lines);
		for (int i = 0; i < list.size(); i++)
			if (list.get(i) == getLineGroupPanel().getReceiptLine() && i > 0) {
				ReceiptLine tempLn = (ReceiptLine) list.get(i - 1);
				list.set(i - 1, list.get(i));
				list.set(i, tempLn);
				Component tempPnl = receiptPanel.getComponent(i - 1);
				receiptPanel.add(getLineGroupPanel(), i - 1);
				receiptPanel.add(tempPnl, i);
				break;
			}
		receiptReports[rr].setReceiptLines((ReceiptLine[]) list.toArray(lines));
		// refresh borders
		Component[] remainingLineGroupPanels = receiptPanel.getComponents();
		for (int i = 0; i < remainingLineGroupPanels.length; i++)
			if (remainingLineGroupPanels[i] instanceof LineGroupPanel)
				((LineGroupPanel) remainingLineGroupPanels[i]).setBorder();
		receiptPanel.add(Box.createVerticalGlue());
		receiptPanel.validate();
		receiptPanel.repaint();
		setBlueprintModified(true);
	}

	/**
	 * @param e
	 */
	void menuEditMoveDownReceiptLine_actionPerformed(ActionEvent e) {
		//JScrollPane receiptReportTabPane = (JScrollPane)receiptReportsTab.getSelectedComponent();
		Box receiptPanel = getReceiptPanel();
		ReceiptReport[] receiptReports = receiptBlueprint.getReceiptReports();
		int rr = receiptReportsTab.getSelectedIndex();
		ReceiptLine[] lines = receiptReports[rr].getReceiptLines();
		if (lines == null)
			return;
		java.util.List list = Arrays.asList(lines);
		for (int i = list.size() - 1; i >= 0; i--)
			if (list.get(i) == getLineGroupPanel().getReceiptLine() && i < list.size() - 1) {
				ReceiptLine tempLn = (ReceiptLine) list.get(i + 1);
				list.set(i + 1, list.get(i));
				list.set(i, tempLn);
				Component tempPnl = receiptPanel.getComponent(i + 1);
				receiptPanel.add(getLineGroupPanel(), i + 1);
				receiptPanel.add(tempPnl, i);
				break;
			}
		receiptReports[rr].setReceiptLines((ReceiptLine[]) list.toArray(lines));
		// refresh borders
		Component[] remainingLineGroupPanels = receiptPanel.getComponents();
		for (int i = 0; i < remainingLineGroupPanels.length; i++)
			if (remainingLineGroupPanels[i] instanceof LineGroupPanel)
				((LineGroupPanel) remainingLineGroupPanels[i]).setBorder();
		receiptPanel.add(Box.createVerticalGlue());
		receiptPanel.validate();
		receiptPanel.repaint();
		setBlueprintModified(true);
	}

	/**
	 * @param e
	 */
	private void menuEditAddReceiptElement_actionPerformed(ActionEvent e) {
		ReceiptElement newReceiptElement = new ReceiptElement();
		newReceiptElement.setStringToPrint("<---Text Element--->");
		addReceiptElement(newReceiptElement, -1, -1); // -1 indicates to add to last physical line panel
	}

	public void addReceiptElement(ReceiptElement newReceiptElement, int insertionIndexComponent, int insertionIndexElementArray) {
		addReceiptElement(getLineGroupPanel(), newReceiptElement, insertionIndexComponent, insertionIndexElementArray);
	}

	/**
	 * @param newReceiptElement
	 * @param insertionIndexComponent
	 * @param insertionIndexElementArray
	 */
	public void addReceiptElement(LineGroupPanel lineGroupPanel, ReceiptElement newReceiptElement, int insertionIndexComponent, int insertionIndexElementArray) {
		newReceiptElement.setLocale(locale);
		if (lineGroupPanel == null) {
			JOptionPane.showMessageDialog(this, "Please first click on the line group you would like to add an element to", "Indicate Line Group", JOptionPane.ERROR_MESSAGE);
			return;
		}
		ReceiptElement[] receiptElements = lineGroupPanel.getReceiptLine().getReceiptElements();
		//testPrint(receiptElements);
		lineGroupPanel.getReceiptLine().setReceiptElements((ReceiptElement[]) ReceiptBuilderUtility.addArrayItem(receiptElements, newReceiptElement, insertionIndexElementArray));
		//testPrint(lineGroupPanel.getReceiptLine().getReceiptElements());
		ReceiptElementJTextField newReceiptElementJTextField = new ReceiptElementJTextField(this, newReceiptElement, this.getElementAttributesPanel());
		JPanel physicalLinePanel;
		//if (lineGroupPanel.getComponentCount() > 0 && receiptElementJTextField != null) {
		if (lineGroupPanel.getComponentCount() > 0) {
			if (!(lineGroupPanel.getComponent(lineGroupPanel.getComponentCount() - 1) instanceof JPanel)) {
				System.out.println("ERROR: group panel has components but last one is not a physical line JPanel...");
				System.out.println("the class of the last component is: " + lineGroupPanel.getComponent(lineGroupPanel.getComponentCount() - 1).getClass().getName());
			}
			//physicalLinePanel = (JPanel)receiptElementJTextField.getParent();
			if (insertionIndexComponent >= 0 && getReceiptElementJTextField() != null)
				physicalLinePanel = (JPanel) getReceiptElementJTextField().getParent();
			else
				physicalLinePanel = (JPanel) lineGroupPanel.getComponent(lineGroupPanel.getComponentCount() - 1);
			if (physicalLinePanel.getComponentCount() > 0) {
				physicalLinePanel.remove(physicalLinePanel.getComponentCount() - 1);
			}
		} else {
			physicalLinePanel = new JPanel();
			physicalLinePanel.setLayout(new BoxLayout(physicalLinePanel, BoxLayout.X_AXIS));
			//physicalLinePanel.setMaximumSize(new Dimension(600, 35));  why do this?
			lineGroupPanel.add(physicalLinePanel);
		}
		if (insertionIndexComponent < 0)
			physicalLinePanel.add(newReceiptElementJTextField);
		else
			physicalLinePanel.add(newReceiptElementJTextField, insertionIndexComponent);
		if (newReceiptElementJTextField.getReceiptElement().isLineFeed())
			newReceiptElementJTextField.getReceiptElement().isLineFeed(false);
		physicalLinePanel.add(Box.createHorizontalGlue());
		physicalLinePanel.validate();
		physicalLinePanel.repaint();
	}

	/**
	 * The listeners weren't working so I put them in ComboBoxCell
	 * @return JComboBox with listeners already set to notify blueprint changed.
	 */
	private JComboBox createComboBox(Object[] initialValues) {
		JComboBox combo = new JComboBox();
		if (initialValues != null)
			combo.setModel(new DefaultComboBoxModel(initialValues));
		/*combo.addActionListener(new ActionListener() {
		 public void actionPerformed (ActionEvent e) {
		 System.out.println("mainfrom listener combo action..." + e.getID());
		 setBlueprintModified(true);
		 }
		 });
		 combo.addItemListener(new ItemListener() {
		 public void itemStateChanged (ItemEvent e) {
		 System.out.println("mainfrom listener combo state...");
		 setBlueprintModified(true);
		 }
		 });*/
		return combo;
	}

	/**
	 * @param receiptElements
	 */
	private void testPrint(ReceiptElement[] receiptElements) {
		for (int i = 0; i < receiptElements.length; i++) {
			System.out.println(receiptElements[i].getArchitectString());
		}
	}

	/**
	 * @param e
	 */
	void menuEditDeleteReceiptElement_actionPerformed(ActionEvent e) {
		if (getReceiptElementJTextField() == null) {
			JOptionPane.showMessageDialog(this, "Please first click on the receipt Element you would like to delete.", "Indicate Receipt Element", JOptionPane.ERROR_MESSAGE);
			return;
		}
		SwingUtilities.invokeLater(new Runnable() {

			/**
			 */
			public void run() {
				getReceiptElementJTextField().requestFocus();
			}
		});
		if (getLineGroupPanel().getReceiptLine().getReceiptElements().length == 1) {
			JOptionPane.showMessageDialog(this, "A receipt line group must have at least one element", "can't delete all elements", JOptionPane.ERROR_MESSAGE);
			return;
		}
		int selected = JOptionPane.showOptionDialog(this, "Are you sure you want to delete this receipt element?", "Delete Element", JOptionPane.OK_CANCEL_OPTION, JOptionPane.QUESTION_MESSAGE, null,
				null, null); //  buttons,okButton);
		if (selected != JOptionPane.OK_OPTION)
			return;
		deleteReceiptElement(getReceiptElementJTextField());
		setReceiptElementJTextField(null);
		setModified(true);
	}

	/**
	 * @param deleteReceiptElementJTextField
	 */
	public void deleteReceiptElement(ReceiptElementJTextField deleteReceiptElementJTextField) {
		ReceiptElement[] receiptElements = getLineGroupPanel().getReceiptLine().getReceiptElements();
		getLineGroupPanel().getReceiptLine().setReceiptElements((ReceiptElement[]) ReceiptBuilderUtility.removeArrayItem(receiptElements, deleteReceiptElementJTextField.getReceiptElement()));
		Component[] physicalLinePanels = getLineGroupPanel().getComponents();
		JPanel physicalLinePanel = (JPanel) deleteReceiptElementJTextField.getParent();
		if (physicalLinePanel.getComponentCount() > 1)
			physicalLinePanel.remove(physicalLinePanel.getComponentCount() - 1);
		physicalLinePanel.remove(deleteReceiptElementJTextField);
		if ((physicalLinePanel.getComponentCount() > 0)) {
			physicalLinePanel.add(Box.createHorizontalGlue());
			physicalLinePanel.validate();
			physicalLinePanel.repaint();
		} else
			physicalLinePanel.getParent().remove(physicalLinePanel);
		System.out.println("PACKING AFTER DELETE...");
		this.pack();
	}

	//File | detachReceiptSampleDataObject... action performed
	void fileDetachRDO_actionPerformed(ActionEvent e) {
		if (receiptBlueprint.getReceiptObjects() == null || receiptBlueprint.getReceiptObjects().length == 0) {
			JOptionPane.showMessageDialog(this, "There are no Sample Data Objects associated with the current Blueprint", "Nothing to Detach", JOptionPane.ERROR_MESSAGE);
			return;
		}
		int selected = JOptionPane.showOptionDialog(this, "Are you sure you want to detach this Sample Data Object from the blueprint?", "Detach Data Object", JOptionPane.OK_CANCEL_OPTION,
				JOptionPane.QUESTION_MESSAGE, null, null, null); //  buttons,okButton);
		if (selected != JOptionPane.OK_OPTION)
			return;
		receiptBlueprint.setReceiptObjects(ReceiptBuilderUtility.removeArrayItem(receiptBlueprint.getReceiptObjects(), pnlObjectInspector.getCurrentlySelectedObject()));
		pnlObjectInspector.loadUpMultipleObjects("Receipt Data Objects", receiptBlueprint.getReceiptObjects());
	}

	//Help | About action performed
	public void helpAbout_actionPerformed(ActionEvent e) {
		setWaitCursor(true);
		AboutDlg dlg = new AboutDlg(this, "Receipt Builder");
		dlg.getAboutPanel().setProduct("Receipt Builder");
		dlg.getAboutPanel().setVersion(Version.VERSION);
		dlg.show();
		setWaitCursor(false);
	}

	public void menuViewReceipts_actionPerformed(ActionEvent e) {
		this.setJMenuBar(menuBar1);
		menuBar2.remove(menuView);
		menuBar2.remove(menuOptions);
		menuBar2.remove(menuHelp);
		menuBar1.add(menuView);
		menuBar1.add(menuOptions);
		menuBar1.add(menuHelp);
		viewFooters.setVisible(true);
		viewReceipts.setVisible(false);
		cardLayout.show(pnlMain, "RECEIPTS");
		this.setTitle(receiptTitle);
		this.pack();
		if (!currentLookAndFeels[0].equals(currentLookAndFeels)) {
			setLookAndFeel(currentLookAndFeel);
			currentLookAndFeels[0] = currentLookAndFeel;
		}

	}

	public void menuViewFooters_actionPerformed(ActionEvent e) {
		this.setJMenuBar(menuBar2);
		menuBar1.remove(menuView);
		menuBar1.remove(menuOptions);
		menuBar1.remove(menuHelp);
		menuBar2.add(menuView);
		menuBar2.add(menuOptions);
		menuBar2.add(menuHelp);
		viewFooters.setVisible(false);
		viewReceipts.setVisible(true);
		cardLayout.show(pnlMain, "FOOTERS");
		this.setTitle(footerTitle);
		this.pack();
		if (!currentLookAndFeels[1].equals(currentLookAndFeels)) {
			setLookAndFeel(currentLookAndFeel);
			currentLookAndFeels[1] = currentLookAndFeel;
		}

	}

	//Overridden so we can exit on System Close
	protected void processWindowEvent(WindowEvent e) {
		//super.processWindowEvent(e); //without this, window listeners dont work...
		if (e.getID() == WindowEvent.WINDOW_CLOSING) {
			exit();
		}
		if (e.getID() == WindowEvent.WINDOW_OPENED) {
			//splashscreen.dispose();
		}
	}

	/**
	 */
	private void exit() {
		if (close()) {
			try {
				INIFile file = new INIFile("receiptarchitect.properties", true);
				if (!file.isFileWritable()) {
					System.out.println("Informational Message: Property file was not be saved, it is write protected.");
					return;
				}
				String ftr = System.getProperty("default.ftr_folder");
				String bpt = System.getProperty("default.bpt_folder");
				String rdo = System.getProperty("default.rdo_folder");
				file.writeEntry("default.bpt_folder", bpt == null ? "" : bpt);
				file.writeEntry("default.rdo_folder", rdo == null ? "" : rdo);
				file.writeEntry("default.ftr_folder", ftr == null ? "" : ftr);
			} catch (Exception ex) {
				ex.printStackTrace();
			} finally {
				System.exit(0);
			}
		}
	}

	/**
	 */
	private boolean close() {
		if (pnlReceiptsWorkArea.getComponentCount() == 0)
			return true;
		boolean shouldClose = true;
		if (isBlueprintModified()) {
			int ans = JOptionPane.showConfirmDialog(this, "Blueprint changed.  Do you want to save changes?", receiptBlueprint.getName(), JOptionPane.YES_NO_CANCEL_OPTION);
			if (ans == JOptionPane.YES_OPTION)
				shouldClose = saveFile();
			else if (ans == JOptionPane.NO_OPTION)
				shouldClose = true;
			else if (ans == JOptionPane.CANCEL_OPTION)
				shouldClose = false;
		}
		return shouldClose;
	}

	/**
	 */
	private boolean closeFtr() {
		if (pnlFootersWorkArea.getComponentCount() == 0)
			return true;
		boolean shouldClose = true;
		if (isFooterModified()) {
			int ans = JOptionPane.showConfirmDialog(this, "Footer changed.  Do you want to save changes?", receiptFooter.getName(), JOptionPane.YES_NO_CANCEL_OPTION);
			if (ans == JOptionPane.YES_OPTION)
				shouldClose = saveFile();
			else if (ans == JOptionPane.NO_OPTION)
				shouldClose = true;
			else if (ans == JOptionPane.CANCEL_OPTION)
				shouldClose = false;
		}
		return shouldClose;
	}

	/**
	 * @param busy
	 */
	public void setWaitCursor(boolean busy) {
		if (busy)
			setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
		else
			setCursor(Cursor.getDefaultCursor());
	}

	/**
	 */
	private void loadGui() {
		isGUIFullyConstructed = true;
		pnlReceiptsWorkArea.removeAll();
		pnlReceiptsWorkArea.add(mainSplitPane, BorderLayout.CENTER);
		pnlReceiptsWorkArea.revalidate();
		pnlReceiptsWorkArea.repaint();
	}

	/**
	 */
	private void loadFtrGui() {
		isFtrGUIFullyConstructed = true;
		pnlFootersWorkArea.removeAll();
		pnlFootersWorkArea.add(mainFtrSplitPane, BorderLayout.CENTER);
		pnlFootersWorkArea.revalidate();
		pnlFootersWorkArea.repaint();
	}

	/**
	 */
	private void setUpPnlElementAttributes() {
		defineAttributeGUIRepresentations();
		loadElementAttributes();
		loadMethodElementAttributes();
		loadDateElementAttributes();
		loadBooleanElementAttributes();
		loadNumberElementAttributes();
		loadCurrencyElementAttributes();
		pnlElementAttributes = new AttributesPanel(this, elementAttributes);
	}

	private void setUpPnlFtrElementAttributes() {
		defineAttributeFtrGUIRepresentations();
		loadFtrElementAttributes();
		pnlFtrElementAttributes = new AttributesPanel(this, ftrElementAttributes);
	}

	/**
	 * @return
	 */
	public Object[][] getBooleanElementAttributes() {
		return booleanElementAttributes;
	}

	/**
	 * @return
	 */
	public Object[][] getDateElementAttributes() {
		return dateElementAttributes;
	}

	/**
	 * @return
	 */
	public Object[][] getNumberElementAttributes() {
		return numberElementAttributes;
	}

	public Object[][] getCurrencyElementAttributes() {
		return currencyElementAttributes;
	}

	/**
	 * @return
	 */
	public Object[][] getElementAttributes() {
		if (cardLayout.getCurrent(pnlMain) == pnlMainReceipts)
			return elementAttributes;
		else
			return ftrElementAttributes;
	}

	/**
	 * @return
	 */
	public Object[][] getMethodElementAttributes() {
		return methodElementAttributes;
	}

	/**
	 */
	private void loadDateElementAttributes() {
		dateElementAttributes = new Object[10][2];
		dateElementAttributes[0][0] = ATTR_DEPENDS_ON;
		dateElementAttributes[1][0] = ATTR_JUSTIFIED;
		dateElementAttributes[2][0] = ATTR_VALUE_TO_PRINT;
		dateElementAttributes[3][0] = ATTR_FIXED_LENGTH;
		dateElementAttributes[4][0] = ATTR_LINE_FEED;
		dateElementAttributes[5][0] = ATTR_DOUBLE_WIDTH;
		dateElementAttributes[6][0] = ATTR_BARCODE;
		dateElementAttributes[7][0] = ATTR_PRECEDED_BY_SPACE;
		dateElementAttributes[8][0] = ATTR_DATE_FORMAT;
		dateElementAttributes[9][0] = ATTR_CLASS_METHODS;
		for (int i = 0; i < dateElementAttributes.length; i++) {
			for (int y = 0; y < attributeGUIRepresentations.length; y++) {
				if (dateElementAttributes[i][0].equals(attributeGUIRepresentations[y][0])) {
					dateElementAttributes[i][1] = attributeGUIRepresentations[y][1];
					break;
				}
			}
		}
	}

	/**
	 */
	private void loadNumberElementAttributes() {
		numberElementAttributes = new Object[18][2];
		numberElementAttributes[0][0] = ATTR_DEPENDS_ON;
		numberElementAttributes[1][0] = ATTR_JUSTIFIED;
		numberElementAttributes[2][0] = ATTR_VALUE_TO_PRINT;
		numberElementAttributes[3][0] = ATTR_FIXED_LENGTH;
		numberElementAttributes[4][0] = ATTR_LINE_FEED;
		numberElementAttributes[5][0] = ATTR_DOUBLE_WIDTH;
		numberElementAttributes[6][0] = ATTR_BARCODE;
		numberElementAttributes[7][0] = ATTR_PRECEDED_BY_SPACE;
		numberElementAttributes[8][0] = ATTR_REVERSE_SIGN;
		numberElementAttributes[9][0] = ATTR_LEADING_FORMAT_POSITIVE;
		numberElementAttributes[10][0] = ATTR_TRAILING_FORMAT_POSITIVE;
		numberElementAttributes[11][0] = ATTR_LEADING_FORMAT_NEGATIVE;
		numberElementAttributes[12][0] = ATTR_TRAILING_FORMAT_NEGATIVE;
		numberElementAttributes[13][0] = ATTR_PRECISION;
		numberElementAttributes[14][0] = ATTR_PRINT_IF_ZERO;
		numberElementAttributes[15][0] = ATTR_PRINT_IF_LENGTH_ZERO;
		numberElementAttributes[16][0] = ATTR_MULTIPLY_BY;
		numberElementAttributes[17][0] = ATTR_CLASS_METHODS;
		for (int i = 0; i < numberElementAttributes.length; i++) {
			for (int y = 0; y < attributeGUIRepresentations.length; y++) {
				if (numberElementAttributes[i][0].equals(attributeGUIRepresentations[y][0])) {
					numberElementAttributes[i][1] = attributeGUIRepresentations[y][1];
					break;
				}
			}
		}
	}

	/**
	 */
	private void loadBooleanElementAttributes() {
		booleanElementAttributes = new Object[11][2];
		booleanElementAttributes[0][0] = ATTR_DEPENDS_ON;
		booleanElementAttributes[1][0] = ATTR_JUSTIFIED;
		booleanElementAttributes[2][0] = ATTR_VALUE_TO_PRINT;
		booleanElementAttributes[3][0] = ATTR_FIXED_LENGTH;
		booleanElementAttributes[4][0] = ATTR_LINE_FEED;
		booleanElementAttributes[5][0] = ATTR_DOUBLE_WIDTH;
		booleanElementAttributes[6][0] = ATTR_BARCODE;
		booleanElementAttributes[7][0] = ATTR_PRECEDED_BY_SPACE;
		booleanElementAttributes[8][0] = ATTR_PRINT_IF_TRUE;
		booleanElementAttributes[9][0] = ATTR_PRINT_IF_FALSE;
		booleanElementAttributes[10][0] = ATTR_CLASS_METHODS;
		for (int i = 0; i < booleanElementAttributes.length; i++) {
			for (int j = 0; j < attributeGUIRepresentations.length; j++) {
				if (booleanElementAttributes[i][0].equals(attributeGUIRepresentations[j][0])) {
					booleanElementAttributes[i][1] = attributeGUIRepresentations[j][1];
					break;
				}
			}
		}
	}

	/**
	 */
	private void loadElementAttributes() {
		elementAttributes = new Object[6][2];
		elementAttributes[0][0] = ATTR_DEPENDS_ON;
		elementAttributes[1][0] = ATTR_VALUE_TO_PRINT;
		elementAttributes[2][0] = ATTR_FIXED_LENGTH;
		elementAttributes[3][0] = ATTR_LINE_FEED;
		elementAttributes[4][0] = ATTR_DOUBLE_WIDTH;
		elementAttributes[5][0] = ATTR_BARCODE;
		//elementAttributes[6][0] = ATTR_PRECEDED_BY_SPACE;
		for (int i = 0; i < elementAttributes.length; i++) {
			for (int y = 0; y < attributeGUIRepresentations.length; y++) {
				if (elementAttributes[i][0].equals(attributeGUIRepresentations[y][0])) {
					elementAttributes[i][1] = attributeGUIRepresentations[y][1];
					break;
				}
			}
		}
	}

	/**
	 */
	private void loadFtrElementAttributes() {
		ftrElementAttributes = new Object[4][2];
		ftrElementAttributes[0][0] = ATTR_VALUE_TO_PRINT;
		ftrElementAttributes[1][0] = ATTR_FIXED_LENGTH;
		ftrElementAttributes[2][0] = ATTR_LINE_FEED;
		ftrElementAttributes[3][0] = ATTR_DOUBLE_WIDTH;
		//elementAttributes[6][0] = ATTR_PRECEDED_BY_SPACE;
		for (int i = 0; i < ftrElementAttributes.length; i++) {
			for (int y = 0; y < attributeFtrGUIRepresentations.length; y++) {
				if (ftrElementAttributes[i][0].equals(attributeFtrGUIRepresentations[y][0])) {
					ftrElementAttributes[i][1] = attributeFtrGUIRepresentations[y][1];
					break;
				}
			}
		}
	}

	/**
	 */
	private void loadMethodElementAttributes() {
		methodElementAttributes = new Object[10][2];
		methodElementAttributes[0][0] = ATTR_DEPENDS_ON;
		methodElementAttributes[1][0] = ATTR_JUSTIFIED;
		methodElementAttributes[2][0] = ATTR_VALUE_TO_PRINT;
		methodElementAttributes[3][0] = ATTR_FIXED_LENGTH;
		methodElementAttributes[4][0] = ATTR_LINE_FEED;
		methodElementAttributes[5][0] = ATTR_DOUBLE_WIDTH;
		methodElementAttributes[6][0] = ATTR_BARCODE;
		methodElementAttributes[7][0] = ATTR_PRECEDED_BY_SPACE;
		methodElementAttributes[8][0] = ATTR_PRINT_IF_LENGTH_ZERO;
		methodElementAttributes[9][0] = ATTR_CLASS_METHODS;
		for (int i = 0; i < methodElementAttributes.length; i++) {
			for (int y = 0; y < attributeGUIRepresentations.length; y++) {
				if (methodElementAttributes[i][0].equals(attributeGUIRepresentations[y][0])) {
					methodElementAttributes[i][1] = attributeGUIRepresentations[y][1];
					break;
				}
			}
		}
	}

	private void loadCurrencyElementAttributes() {
		currencyElementAttributes = new Object[11][2];
		currencyElementAttributes[0][0] = ATTR_DEPENDS_ON;
		currencyElementAttributes[1][0] = ATTR_JUSTIFIED;
		currencyElementAttributes[2][0] = ATTR_VALUE_TO_PRINT;
		currencyElementAttributes[3][0] = ATTR_FIXED_LENGTH;
		currencyElementAttributes[4][0] = ATTR_LINE_FEED;
		currencyElementAttributes[5][0] = ATTR_DOUBLE_WIDTH;
		currencyElementAttributes[6][0] = ATTR_BARCODE;
		currencyElementAttributes[7][0] = ATTR_PRECEDED_BY_SPACE;
		currencyElementAttributes[8][0] = ATTR_PRINT_IF_ZERO;
		currencyElementAttributes[9][0] = ATTR_REVERSE_SIGN;
		currencyElementAttributes[10][0] = ATTR_CLASS_METHODS;
		for (int i = 0; i < currencyElementAttributes.length; i++) {
			for (int y = 0; y < attributeGUIRepresentations.length; y++) {
				if (currencyElementAttributes[i][0].equals(attributeGUIRepresentations[y][0])) {
					currencyElementAttributes[i][1] = attributeGUIRepresentations[y][1];
					break;
				}
			}
		}
	}

	private void defineValueToPrint(Object[] attributeGUIRepresentations) {
		attributeGUIRepresentations[0] = ATTR_VALUE_TO_PRINT;
		JComboBox cboValue = createComboBox(getPredefinedValues());
		cboValue.setEditable(true);
		TableReadyComponent elmtValueToPrint = new ComboBoxCell(ATTR_VALUE_TO_PRINT, cboValue);
		elmtValueToPrint.addUneditableObjectType("ReceiptMethodElement");
		attributeGUIRepresentations[1] = elmtValueToPrint;
	}

	private void defineFixedLength(Object[] attributeGUIRepresentations) {
		attributeGUIRepresentations[0] = ATTR_FIXED_LENGTH;
		TableReadyComponent elmtFixedLength = new TableReadyComponent(ATTR_FIXED_LENGTH, "");
		elmtFixedLength.addUneditableObjectType("ReceiptElement");
		((JTextField) elmtFixedLength.getMyJComponent()).setDocument(new TextFilter(TextFilter.NUMERIC, 3));
		attributeGUIRepresentations[1] = elmtFixedLength;
	}

	private void defineLineFeed(Object[] attributeGUIRepresentations) {
		attributeGUIRepresentations[0] = ATTR_LINE_FEED;
		CheckBoxCell elmtIsLineFeed = new CheckBoxCell(ATTR_LINE_FEED);
		/* add listener so we can split/join the lines as its value changes */
		((JCheckBox) elmtIsLineFeed.getMyJComponent()).addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (((JCheckBox) e.getSource()).isSelected())
					breakLine();
				else
					joinLine();
			}
		});
		attributeGUIRepresentations[1] = elmtIsLineFeed;
	}

	private void defineJustified(Object[] attributeGUIRepresentations) {
		attributeGUIRepresentations[0] = ATTR_JUSTIFIED;
		JComboBox cboJust = createComboBox(new String[] { "Left", "Center", "Right" });
		TableReadyComponent elmtJustified = new ComboBoxCell(ATTR_JUSTIFIED, cboJust);
		elmtJustified.addUneditableObjectType("ReceiptElement");
		attributeGUIRepresentations[1] = elmtJustified;
	}

	/**
	 * Build GUI attributes.
	 */
	private void defineAttributeFtrGUIRepresentations() {
		attributeFtrGUIRepresentations = new Object[5][2];
		// Value to Print
		defineValueToPrint(attributeFtrGUIRepresentations[0]);
		// Fixed Length
		defineFixedLength(attributeFtrGUIRepresentations[1]);
		// Is Line Feed
		defineLineFeed(attributeFtrGUIRepresentations[2]);
		// Justified
		defineJustified(attributeFtrGUIRepresentations[3]);
		// Is Double Width Font
		attributeFtrGUIRepresentations[4][0] = ATTR_DOUBLE_WIDTH;
		attributeFtrGUIRepresentations[4][1] = new CheckBoxCell(ATTR_DOUBLE_WIDTH);
	}

	private void defineAttributeGUIRepresentations() {
		attributeGUIRepresentations = new Object[21][2];
		// Depends On
		attributeGUIRepresentations[0][0] = ATTR_DEPENDS_ON;
		attributeGUIRepresentations[0][1] = new ComboBoxCellDependsOn(ATTR_DEPENDS_ON, createComboBox(null));
		// Value to Print
		defineValueToPrint(attributeGUIRepresentations[1]);
		// Fixed Length
		defineFixedLength(attributeGUIRepresentations[2]);
		// Is Line Feed
		defineLineFeed(attributeGUIRepresentations[3]);
		// Justified
		defineJustified(attributeGUIRepresentations[4]);
		// Is Double Width Font
		attributeGUIRepresentations[5][0] = ATTR_DOUBLE_WIDTH;
		attributeGUIRepresentations[5][1] = new CheckBoxCell(ATTR_DOUBLE_WIDTH);
		// Is Printed as Barcode
		attributeGUIRepresentations[6][0] = ATTR_BARCODE;
		attributeGUIRepresentations[6][1] = new CheckBoxCell(ATTR_BARCODE);
		// Is Preceded by space
		attributeGUIRepresentations[7][0] = ATTR_PRECEDED_BY_SPACE;
		attributeGUIRepresentations[7][1] = new CheckBoxCell(ATTR_PRECEDED_BY_SPACE);
		// Date Format
		String[] dateFormatBoxChoices = { DATE_FORMAT_MONTHDDYYYY, DATE_FORMAT_MMDDYYYY, DATE_FORMAT_DDMMYYYY, DATE_FORMAT_YYYYMMDD, DATE_FORMAT_MMDD, DATE_FORMAT_DDMM, DATE_FORMAT_MMYY,
				DATE_FORMAT_HH_MM_A, DATE_FORMAT_HH_MM, DATE_FORMAT_DAY_OF_WEEK_A, DATE_FORMAT_DAY_OF_WEEK };
		attributeGUIRepresentations[8][0] = ATTR_DATE_FORMAT;
		JComboBox cboDate = createComboBox(dateFormatBoxChoices);
		attributeGUIRepresentations[8][1] = new ComboBoxCell(ATTR_DATE_FORMAT, cboDate);
		// Value Printed if True
		attributeGUIRepresentations[9][0] = ATTR_PRINT_IF_TRUE;
		attributeGUIRepresentations[9][1] = new TableReadyComponent(ATTR_PRINT_IF_TRUE, "true");
		// Value Printed if False
		attributeGUIRepresentations[10][0] = ATTR_PRINT_IF_FALSE;
		attributeGUIRepresentations[10][1] = new TableReadyComponent(ATTR_PRINT_IF_FALSE, "false");
		// Is Sign Reversed
		attributeGUIRepresentations[11][0] = ATTR_REVERSE_SIGN;
		attributeGUIRepresentations[11][1] = new CheckBoxCell(ATTR_REVERSE_SIGN);
		// Lead Format
		attributeGUIRepresentations[12][0] = ATTR_LEADING_FORMAT_POSITIVE;
		attributeGUIRepresentations[12][1] = new TableReadyComponent(ATTR_LEADING_FORMAT_POSITIVE, "");
		// Trail Format
		attributeGUIRepresentations[13][0] = ATTR_TRAILING_FORMAT_POSITIVE;
		attributeGUIRepresentations[13][1] = new TableReadyComponent(ATTR_TRAILING_FORMAT_POSITIVE, "");
		// Lead Format if Negative
		attributeGUIRepresentations[14][0] = ATTR_LEADING_FORMAT_NEGATIVE;
		attributeGUIRepresentations[14][1] = new TableReadyComponent(ATTR_LEADING_FORMAT_NEGATIVE, "");
		// Trail Format if Negative
		attributeGUIRepresentations[15][0] = ATTR_TRAILING_FORMAT_NEGATIVE;
		attributeGUIRepresentations[15][1] = new TableReadyComponent(ATTR_TRAILING_FORMAT_NEGATIVE, "");
		// Decimal Places Shown
		attributeGUIRepresentations[16][0] = ATTR_PRECISION;
		attributeGUIRepresentations[16][1] = new TableReadyComponent(ATTR_PRECISION, "2");
		((JTextField) ((TableReadyComponent) attributeGUIRepresentations[16][1]).getMyJComponent()).setDocument(new TextFilter(TextFilter.NUMERIC, 1));
		// Object Navigation Path
		attributeGUIRepresentations[17][0] = ATTR_CLASS_METHODS;
		attributeGUIRepresentations[17][1] = new TextAreaCell(ATTR_CLASS_METHODS);
		// Is printed when zero
		attributeGUIRepresentations[18][0] = ATTR_PRINT_IF_ZERO;
		attributeGUIRepresentations[18][1] = new CheckBoxCell(this.ATTR_PRINT_IF_ZERO);
		// Is printed when length is zero
		attributeGUIRepresentations[19][0] = ATTR_PRINT_IF_LENGTH_ZERO;
		attributeGUIRepresentations[19][1] = new CheckBoxCell(this.ATTR_PRINT_IF_LENGTH_ZERO);
		// Multiply By
		attributeGUIRepresentations[20][0] = ATTR_MULTIPLY_BY;
		attributeGUIRepresentations[20][1] = new TableReadyComponent(ATTR_MULTIPLY_BY, "");
		((JTextField) ((TableReadyComponent) attributeGUIRepresentations[20][1]).getMyJComponent()).setDocument(new TextFilter(TextFilter.NUMERIC));
	}

	/**
	 * Get the set of predefined strings from the JPOS_peripherals.cfg file that can be
	 * conveniently and consistently printed on a receipt by choosing from a combobox.
	 * Then append the available functions.
	 */
	private String[] getPredefinedValues() {
		INIFile file;
		Vector predefinedValues = new Vector();
		try {
			file = new INIFile(FileMgr.getLocalFile("config", "JPOS_peripherals.cfg"), false);
			// load the predefined strings from config
			StringTokenizer st;
			String predefinedList = file.getValue("RA_PREDEFINED_LIST");
			st = new StringTokenizer(predefinedList, ",");
			while(st.hasMoreElements()) {
				String key = (String) st.nextElement();
				String raPredefinedField = file.getValue(key);
				predefinedValues.add(raPredefinedField);
			}
		} catch (Exception e) {
			JOptionPane.showMessageDialog(this, "Error accessing JPOS_peripherals.cfg file for predefined fields info: " + e.getMessage(), "recipts.cfg error", JOptionPane.ERROR_MESSAGE);
			return new String[0];
		}
		predefinedValues.addAll(Arrays.asList(ReceiptElement.getFunctions()));
		return (String[]) predefinedValues.toArray(new String[predefinedValues.size()]);
	}

	/**
	 */
	private void setUpPnlReceiptAttributes() {
		TableReadyComponent rcptName;
		TableReadyComponent rcptStoreCount;
		TableReadyComponent rcptCustomerCount;
		TableReadyComponent rcptEJCount;
		//TableReadyComponent rcptStoreFooter;
		TableReadyComponent rcptPrintStoreLogo;
		Object[][] receiptAttributes = new Object[6][2];
		// Receipt Report Name
		receiptAttributes[0][0] = ATTR_NAME;
		rcptName = new TableReadyComponent(ATTR_NAME, "initial name");
		rcptName.setCallBackObject(receiptReportsTab);
		receiptAttributes[0][1] = rcptName;
		// Store Count
		receiptAttributes[1][0] = ATTR_STORE_COUNT;
		rcptStoreCount = new TableReadyComponent(ATTR_STORE_COUNT, "");
		rcptStoreCount.setCallBackObject(receiptReportsTab);
		((JTextField) rcptStoreCount.getMyJComponent()).setDocument(new TextFilter(TextFilter.NUMERIC, 3));
		receiptAttributes[1][1] = rcptStoreCount;
		// Customer Count
		receiptAttributes[2][0] = ATTR_CUSTOMER_COUNT;
		rcptCustomerCount = new TableReadyComponent(ATTR_CUSTOMER_COUNT, "");
		rcptCustomerCount.setCallBackObject(receiptReportsTab);
		((JTextField) rcptCustomerCount.getMyJComponent()).setDocument(new TextFilter(TextFilter.NUMERIC, 3));
		receiptAttributes[2][1] = rcptCustomerCount;
		// EJ Count
		receiptAttributes[3][0] = ATTR_EJ_COUNT;
		rcptEJCount = new CheckBoxCell(ATTR_EJ_COUNT);
		rcptEJCount.setCallBackObject(receiptReportsTab);
		receiptAttributes[3][1] = rcptEJCount;
		// Store Footer
		receiptAttributes[4][0] = ATTR_STORE_FOOTER;
		JComboBox cboFooter = createComboBox(getFooterNames());
		rcptStoreFooter = new ComboBoxCell(ATTR_STORE_FOOTER, cboFooter);
		rcptStoreFooter.setCallBackObject(receiptReportsTab);
		receiptAttributes[4][1] = rcptStoreFooter;
		// Store Logo
		receiptAttributes[5][0] = ATTR_STORE_LOGO;
		rcptPrintStoreLogo = new CheckBoxCell(ATTR_STORE_LOGO);
		rcptPrintStoreLogo.setCallBackObject(receiptReportsTab);
		receiptAttributes[5][1] = rcptPrintStoreLogo;
		pnlReceiptAttributes = new AttributesPanel(this, receiptAttributes);
		receiptReportsTab.setAttributesPanel(pnlReceiptAttributes);
		receiptReportsTab.addChangeListener(new ChangeListener() {
			public void stateChanged(ChangeEvent e) {
				receiptReportsTabChanged();
			}
		});
	}

	private String[] getFooterNames() {
		File blueprintFile = new File(FileMgr.getLocalFile("receiptblueprints"));
		String[] footers = new String[0];
		if (blueprintFile != null)
			footers = blueprintFile.list(new FilenameFilter() {
				public boolean accept(File dir, String name) {
					if (name.endsWith(".ftr"))
						return true;
					return false;
				}
			});
		for (int i = 0; i < footers.length; i++)
			footers[i] = footers[i].substring(0, footers[i].indexOf(".ftr"));
		footers = (String[]) ReceiptBuilderUtility.addArrayItem(footers, "NO FOOTER", 0);
		return footers;
	}

	// configure the screen to display a particular blueprint and load him up
	private void loadBlueprint() {
		if (!isGUIFullyConstructed)
			loadGui();
		pnlObjectInspector.loadUpMultipleObjects("Receipt Data Objects", receiptBlueprint.getReceiptObjects());
		receiptReportsTab.removeAll();
		receiptReportsTab.setReceiptBlueprint(receiptBlueprint);
		ReceiptReport[] receiptReports = receiptBlueprint.getReceiptReports();
		for (int i = 0; i < receiptReports.length; i++) {
			Box view = new Box(BoxLayout.Y_AXIS);
			JScrollPane receiptReportTabPane = new JScrollPane(view);
			receiptReportTabPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
			receiptReportTabPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
			receiptReportsTab.addTab(receiptReports[i].getName(), receiptReportTabPane);
			receiptReportsTab.setSelectedIndex(i);
			loadReceiptReport(receiptReports[i], view);
		}
		receiptReportsTab.setSelectedIndex(0);
		pnlElementAttributes.setVisible(false);
		((AbstractButton) (languageChoices.getElements().nextElement())).setSelected(true);
	}

	// configure the screen to display a particular footer and load him up
	private void loadFooter() {
		if (!isFtrGUIFullyConstructed)
			loadFtrGui();
		receiptFooterTab.removeAll();
		receiptFooterTab.setReceiptBlueprint(receiptFooter);
		ReceiptReport[] receiptReports = receiptFooter.getReceiptReports();
		System.out.println("receipt reports lenght: " + receiptReports.length);
		for (int i = 0; i < receiptReports.length; i++) {
			Box view = new Box(BoxLayout.Y_AXIS);
			JScrollPane receiptReportTabPane = new JScrollPane(view);
			receiptReportTabPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
			receiptReportTabPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
			System.out.println("adding receipt report tab pane(scrollpnae) to receipt footer tab...");
			receiptFooterTab.add(receiptReportTabPane, BorderLayout.CENTER);
			loadReceiptReport(receiptReports[i], view);
		}
		pnlFtrElementAttributes.setVisible(false);
		((AbstractButton) (languageChoices.getElements().nextElement())).setSelected(true);
	}

	/**
	 * @param receiptReport
	 * @param receiptPanel
	 */
	private void loadReceiptReport(ReceiptReport receiptReport, Box receiptPanel) {
		ReceiptLine[] receiptLines = receiptReport.getReceiptLines();
		if (receiptLines != null) {
			for (int i = 0; i < receiptLines.length; i++) {
				loadReceiptElements(receiptLines[i], receiptPanel);
			}
		}
		receiptPanel.add(Box.createVerticalGlue());
		receiptPanel.validate();
		receiptPanel.repaint();
	}

	/**
	 * @param receiptLine
	 * @param receiptPanel
	 */
	private void loadReceiptElements(ReceiptLine receiptLine, Box receiptPanel) {
		setLineGroupPanel(new LineGroupPanel(this));
		getLineGroupPanel().setReceiptLine(receiptLine);
		getLineGroupPanel().setBorder(); // sets the border number to the index of line group
		getLineGroupPanel().setLayout(new BoxLayout(getLineGroupPanel(), BoxLayout.Y_AXIS));
		getReceiptPanel().add(getLineGroupPanel());
		JPanel physicalLinePanel = new JPanel();
		physicalLinePanel.setLayout(new BoxLayout(physicalLinePanel, BoxLayout.X_AXIS));
		//physicalLinePanel.setMaximumSize(new Dimension(600, 35));   why do this?
		getLineGroupPanel().add(physicalLinePanel);
		ReceiptElement[] receiptElements = receiptLine.getReceiptElements();
		if (receiptElements != null) {
			for (int i = 0; i < receiptElements.length; i++) {
				ReceiptElementJTextField receiptElementJTextField = new ReceiptElementJTextField(this, receiptElements[i], this.getElementAttributesPanel());
				if (receiptElements[i].isLineFeed() && physicalLinePanel.getComponentCount() > 0) {
					physicalLinePanel.add(Box.createHorizontalGlue());
					physicalLinePanel = new JPanel();
					physicalLinePanel.setLayout(new BoxLayout(physicalLinePanel, BoxLayout.X_AXIS));
					//physicalLinePanel.setMaximumSize(new Dimension(600, 35));   why do this?
					getLineGroupPanel().add(physicalLinePanel);
				}
				physicalLinePanel.add(receiptElementJTextField);
			}
			physicalLinePanel.add(Box.createHorizontalGlue());
		}
	}

	/**
	 * @return
	 */
	public ReceiptBlueprint getReceiptBlueprint() {
		if (cardLayout.getCurrent(pnlMain) == pnlMainReceipts)
			return receiptBlueprint;
		else
			return receiptFooter;
	}

	/**
	 * @return
	 */
	public LineGroupPanel getLineGroupPanel() {
		if (cardLayout.getCurrent(pnlMain) == pnlMainReceipts)
			return lineGroupPanel;
		else
			return ftrLineGroupPanel;
	}

	/**
	 * @param lineGroupPanel
	 */
	public void setLineGroupPanel(LineGroupPanel lineGroupPanel) {
		if (cardLayout.getCurrent(pnlMain) == pnlMainReceipts)
			this.lineGroupPanel = lineGroupPanel;
		else
			ftrLineGroupPanel = lineGroupPanel;
	}

	/**
	 * @return
	 */
	public ReceiptElementJTextField getReceiptElementJTextField() {
		if (cardLayout.getCurrent(pnlMain) == pnlMainReceipts)
			return receiptElementJTextField;
		else
			return ftrReceiptElementJTextField;
	}

	/**
	 * @param receiptElementJTextField
	 */
	public void setReceiptElementJTextField(ReceiptElementJTextField receiptElementJTextField) {
		if (cardLayout.getCurrent(pnlMain) == pnlMainReceipts)
			this.receiptElementJTextField = receiptElementJTextField;
		else
			this.ftrReceiptElementJTextField = receiptElementJTextField;
	}

	/**
	 * @return
	 */
	public ReceiptReport getReceiptReport() {
		if (cardLayout.getCurrent(pnlMain) == pnlMainReceipts)
			return receiptReportsTab.getReceiptReport();
		else
			return receiptFooterTab.getReceiptReport();
	}

	private Box getReceiptPanel() {
		return (Box) getReceiptScrollPane().getViewport().getView();
	}

	public AttributesPanel getElementAttributesPanel() {
		if (cardLayout.getCurrent(pnlMain) == pnlMainReceipts) {
			return pnlElementAttributes;
		} else {
			return pnlFtrElementAttributes;
		}
	}

	private JScrollPane getReceiptScrollPane() {
		if (cardLayout.getCurrent(pnlMain) == pnlMainReceipts)
			return (JScrollPane) receiptReportsTab.getSelectedComponent();
		else
			return (JScrollPane) receiptFooterTab.getComponent(0);
	}

	/**
	 */
	private void breakLine() {
		if (getReceiptElementJTextField().getReceiptElement().isLineFeed()) {
			return;
		} else {
		}
		Box receiptPanel = getReceiptPanel();
		// first remove glue
		if (receiptPanel.getComponentCount() > 0)
			receiptPanel.remove(receiptPanel.getComponentCount() - 1);
		JPanel physicalLinePanel = (JPanel) getReceiptElementJTextField().getParent();
		if (physicalLinePanel.getComponentCount() > 0)
			physicalLinePanel.remove(physicalLinePanel.getComponentCount() - 1);
		JPanel newLinePanel = new JPanel();
		newLinePanel.setLayout(new BoxLayout(newLinePanel, BoxLayout.X_AXIS));
		//newLinePanel.setMaximumSize(new Dimension(600, 35));  why do this?
		getLineGroupPanel().add(newLinePanel, ReceiptBuilderUtility.findIndexOfComponent(getLineGroupPanel(), physicalLinePanel) + 1);
		Component[] linePanelComponents = physicalLinePanel.getComponents();
		// move all components to the new physical line panel beginning with current receiptElementJTextField
		for (int i = ReceiptBuilderUtility.findIndexOfComponent(physicalLinePanel, getReceiptElementJTextField()); i < linePanelComponents.length; i++) {
			newLinePanel.add(linePanelComponents[i]);
			physicalLinePanel.remove(linePanelComponents[i]);
		}
		newLinePanel.add(Box.createHorizontalGlue());
		physicalLinePanel.add(Box.createHorizontalGlue());
		receiptPanel.add(Box.createVerticalGlue());
		newLinePanel.requestFocus();
		receiptPanel.validate();
		newLinePanel.scrollRectToVisible(newLinePanel.getBounds());

		receiptPanel.repaint();
	}

	/**
	 */
	private void joinLine() {
		if (!getReceiptElementJTextField().getReceiptElement().isLineFeed())
			return;
		JScrollPane receiptReportTabPane = this.getReceiptScrollPane();
		Box receiptPanel = (Box) receiptReportTabPane.getViewport().getView();
		JPanel physicalLinePanel = (JPanel) getReceiptElementJTextField().getParent();
		// see if we can find a physicalLinePanel above us in the component array,
		// that is our target.  If none found, then we must be in the first line already.
		int currentLocation = ReceiptBuilderUtility.findIndexOfComponent(getLineGroupPanel(), physicalLinePanel);
		if (currentLocation < 0) {
			System.out.println("ERROR: findIndexOfComponent() not found");
			return;
		}
		Component[] lineGroupPanelLines = getLineGroupPanel().getComponents();
		JPanel targetLinePanel = null;
		for (int i = currentLocation - 1; i >= 0; i--) {
			if (lineGroupPanelLines[i] instanceof JPanel) {
				targetLinePanel = (JPanel) lineGroupPanelLines[i];
				break;
			}
		}
		if (targetLinePanel == null) {
			return;
		}
		// first remove glue
		if (receiptPanel.getComponentCount() > 0)
			receiptPanel.remove(receiptPanel.getComponentCount() - 1);
		if (physicalLinePanel.getComponentCount() > 0)
			physicalLinePanel.remove(physicalLinePanel.getComponentCount() - 1);
		if (targetLinePanel.getComponentCount() > 0)
			targetLinePanel.remove(targetLinePanel.getComponentCount() - 1);
		Component[] linePanelComponents = physicalLinePanel.getComponents();
		// move all components to the new physical line panel beginning with current receiptElementJTextField
		for (int i = 0; i < linePanelComponents.length; i++) {
			targetLinePanel.add(linePanelComponents[i]);
			physicalLinePanel.remove(linePanelComponents[i]);
		}
		physicalLinePanel.getParent().remove(physicalLinePanel);
		targetLinePanel.add(Box.createHorizontalGlue());
		receiptPanel.add(Box.createVerticalGlue());
		targetLinePanel.requestFocus();
		receiptPanel.validate();
		receiptPanel.repaint();
	}

	/**
	 */
	public void reSetRightSplitPaneDivider() {
		if (cardLayout.getCurrent(pnlMain) == pnlMainReceipts)
			rightSplitPane.setDividerLocation(.3);
	}

	/**
	 */
	private void receiptReportsTabChanged() {
		//System.out.println("receipt reports tab change...");
		if (receiptElementJTextField != null) {
			receiptElementJTextField.setBackground(Color.white);
			receiptElementJTextField = null;
		}
		if (getLineGroupPanel() != null) {
			getLineGroupPanel().removeFocus();
			setLineGroupPanel(null);
		}
		if (pnlElementAttributes != null) {
			pnlElementAttributes.setVisible(false);
		}
		if (receiptBlueprint != null) {
			ReceiptReport[] receiptReports = receiptBlueprint.getReceiptReports();
			if (receiptReports != null && receiptReportsTab.getSelectedIndex() >= 0) {
				//System.out.println("receipt reports count: " + receiptReports.length);
				//System.out.println("tab index: " + receiptReportsTab.getSelectedIndex());
				if (receiptReportsTab.getSelectedIndex() < receiptReports.length)
					pnlReceiptAttributes.refreshAttributeValues(receiptReports[receiptReportsTab.getSelectedIndex()].getNameValuePairs());
			}
		}
	}

	/**
	 */
	private void cleanBlueprint() {
		ReceiptReport[] receiptReports = receiptBlueprint.getReceiptReports();
		for (int i = 0; i < receiptReports.length; i++) {
			ReceiptLine[] receiptLines = receiptReports[i].getReceiptLines();
			if (receiptLines == null || receiptLines.length == 0) {
				// remove a report that has no line groups
				receiptBlueprint.setReceiptReports((ReceiptReport[]) ReceiptBuilderUtility.removeArrayItem(receiptBlueprint.getReceiptReports(), receiptReports[i]));
			} else {
				for (int j = 0; j < receiptLines.length; j++) {
					ReceiptElement[] receiptElements = receiptLines[j].getReceiptElements();
					// remove a line group that has no elements
					if (receiptElements == null || receiptElements.length == 0)
						receiptReports[i].setReceiptLines((ReceiptLine[]) ReceiptBuilderUtility.removeArrayItem(receiptReports[i].getReceiptLines(), receiptLines[j]));
				}
				if (receiptReports[i].getReceiptLines() == null || receiptReports[i].getReceiptLines().length == 0)
					// now check again that the report still has line groups else remove
					receiptBlueprint.setReceiptReports((ReceiptReport[]) ReceiptBuilderUtility.removeArrayItem(receiptBlueprint.getReceiptReports(), receiptReports[i]));
			}
		}
		// make sure every line group starts with a line feed
		receiptReports = receiptBlueprint.getReceiptReports();
		for (int i = 0; i < receiptReports.length; i++) {
			ReceiptLine[] receiptLines = receiptReports[i].getReceiptLines();
			for (int j = 0; j < receiptLines.length; j++) {
				ReceiptElement[] receiptElements = receiptLines[j].getReceiptElements();
				receiptElements[0].isLineFeed(true);
			}
		}
	}

	/**
	 * @param lookAndFeelClassName
	 */
	private void setLookAndFeel(String lookAndFeelClassName) {
		currentLookAndFeel = lookAndFeelClassName;
		try {
			UIManager.setLookAndFeel(lookAndFeelClassName);
		} catch (Exception ex) {
			System.out.println("MainFrame.setLookAndFeel()->" + ex);
		}
		if (lookAndFeelClassName.endsWith("MetalLookAndFeel")) {
			UIManager.put("TextField.background", Color.white);
		}
		SwingUtilities.updateComponentTreeUI(MainFrame.this);
		pack();
		repaint();
	}

	private void setCMSLocale(Locale locale) {
		setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
		this.locale = locale;

		if (receiptBlueprint != null)
			receiptBlueprint.setLocale(locale);
		if (receiptFooter != null)
			receiptFooter.setLocale(locale);
		if (getReceiptElementJTextField() != null)
			getReceiptElementJTextField().elementFocusGained();
		reSetAllReceiptElementsInContainer(receiptReportsTab);
		reSetAllReceiptElementsInContainer(receiptFooterTab);
		setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
		pack();
		repaint();
	}

	private void reSetAllReceiptElementsInContainer(Container container) {
		if (container == null)
			return;
		Component[] comp = container.getComponents();
		for (int i = 0; i < comp.length; i++) {
			if (comp[i] instanceof ReceiptElementJTextField) {
				((ReceiptElementJTextField) comp[i]).reSetText();
			} else if (comp[i] instanceof Container) {
				reSetAllReceiptElementsInContainer((Container) comp[i]);
				((Container) comp[i]).doLayout();
			}
		}
	}

	/**************************************************************************/
	private class MenuFileNewBpt extends AbstractAction implements JCMSToolBar.NamedAction {

		public MenuFileNewBpt() {
			super("New Receipt Blueprint...", CMSImageIcons.getInstance().getNewBlueprint());
		}

		public String getActionName() {
			return "New Receipt Blueprint...";
		}

		public void actionPerformed(ActionEvent e) {
			fileNewBpt_actionPerformed(e);
		}
	}

	private class MenuFileOpenBpt extends AbstractAction implements JCMSToolBar.NamedAction {

		public MenuFileOpenBpt() {
			super("Open Receipt Blueprint...", CMSImageIcons.getInstance().getOpen());
		}

		public String getActionName() {
			return "Open Receipt Blueprint...";
		}

		public void actionPerformed(ActionEvent e) {
			fileOpenBpt_actionPerformed(e);
		}
	}

	private class MenuFileOpenRdo extends AbstractAction implements JCMSToolBar.NamedAction {

		public MenuFileOpenRdo() {
			super("Attach Receipt Sample Data Object..", CMSImageIcons.getInstance().getPlugIn());
		}

		public String getActionName() {
			return "Attach Receipt Sample Data Object...";
		}

		public void actionPerformed(ActionEvent e) {
			fileOpenRdo_actionPerformed(e);
		}
	}

	private class MenuFileSaveBpt extends AbstractAction implements JCMSToolBar.NamedAction {

		public MenuFileSaveBpt() {
			super("Save Receipt Blueprint...", CMSImageIcons.getInstance().getSave());
		}

		public String getActionName() {
			return "Save Receipt Blueprint...";
		}

		public void actionPerformed(ActionEvent e) {
			saveFile();
		}
	}

	private class MenuFileExit extends AbstractAction implements JCMSToolBar.NamedAction {

		public MenuFileExit() {
			super("Exit", CMSImageIcons.getInstance().getExit());
		}

		public String getActionName() {
			return "Exit";
		}

		public void actionPerformed(ActionEvent e) {
			exit();
		}
	}

	private class MenuFileCloseBpt extends AbstractAction implements JCMSToolBar.NamedAction {

		public MenuFileCloseBpt() {
			super("Close", CMSImageIcons.getInstance().getClose());
		}

		public String getActionName() {
			return "Close";
		}

		public void actionPerformed(ActionEvent e) {
			if (!close())
				return;
			setBlueprintModified(false);
			isGUIFullyConstructed = false;
			receiptTitle = "Receipt Builder";
			setTitle(receiptTitle);
			receiptBlueprint = null;
			MainFrame.this.pnlReceiptsWorkArea.removeAll();
			MainFrame.this.pnlReceiptsWorkArea.repaint();
		}
	}

	private class MenuEditAddReceiptReport extends AbstractAction implements JCMSToolBar.NamedAction {

		public MenuEditAddReceiptReport() {
			super("Add Receipt Report", CMSImageIcons.getInstance().getAddReport());
		}

		public String getActionName() {
			return "Add Receipt Report";
		}

		public void actionPerformed(ActionEvent e) {
			menuEditAddReceiptReport_actionPerformed(e);
		}
	}

	private class MenuEditDeleteReceiptReport extends AbstractAction implements JCMSToolBar.NamedAction {

		public MenuEditDeleteReceiptReport() {
			super("Delete Receipt Report", CMSImageIcons.getInstance().getDeleteReport());
		}

		public String getActionName() {
			return "Delete Receipt Report";
		}

		public void actionPerformed(ActionEvent e) {
			menuEditDeleteReceiptReport_actionPerformed(e);
		}
	}

	private class MenuEditAddReceiptLine extends AbstractAction implements JCMSToolBar.NamedAction {

		public MenuEditAddReceiptLine() {
			super("Add Receipt Line Group", CMSImageIcons.getInstance().getAddGroup());
		}

		public String getActionName() {
			return "Add Receipt Line Group";
		}

		public void actionPerformed(ActionEvent e) {
			menuEditAddReceiptLine_actionPerformed(e);
		}
	}

	private class MenuEditDeleteReceiptLine extends AbstractAction implements JCMSToolBar.NamedAction {

		public MenuEditDeleteReceiptLine() {
			super("Delete Receipt Line Group", CMSImageIcons.getInstance().getDeleteGroup());
		}

		public String getActionName() {
			return "Delete Receipt Line Group";
		}

		public void actionPerformed(ActionEvent e) {
			menuEditDeleteReceiptLine_actionPerformed(e);
		}
	}

	private class MenuEditMoveUpReceiptLine extends AbstractAction implements JCMSToolBar.NamedAction {

		public MenuEditMoveUpReceiptLine() {
			super("Move Line Group Up", CMSImageIcons.getInstance().getUp());
		}

		public String getActionName() {
			return "Move Receipt Line Group Up";
		}

		public void actionPerformed(ActionEvent e) {
			menuEditMoveUpReceiptLine_actionPerformed(e);
		}
	}

	private class MenuEditMoveDownReceiptLine extends AbstractAction implements JCMSToolBar.NamedAction {

		public MenuEditMoveDownReceiptLine() {
			super("Move Line Group Down", CMSImageIcons.getInstance().getDown());
		}

		public String getActionName() {
			return "Move Receipt Line Group Down";
		}

		public void actionPerformed(ActionEvent e) {
			menuEditMoveDownReceiptLine_actionPerformed(e);
		}
	}

	private class MenuEditAddReceiptElement extends AbstractAction implements JCMSToolBar.NamedAction {

		public MenuEditAddReceiptElement() {
			super("Add Receipt Element", CMSImageIcons.getInstance().getAddElement());
		}

		public String getActionName() {
			return "Add Receipt Element";
		}

		public void actionPerformed(ActionEvent e) {
			menuEditAddReceiptElement_actionPerformed(e);
		}
	}

	private class MenuEditDeleteReceiptElement extends AbstractAction implements JCMSToolBar.NamedAction {

		public MenuEditDeleteReceiptElement() {
			super("Delete Receipt Element", CMSImageIcons.getInstance().getDeleteElement());
		}

		public String getActionName() {
			return "Delete Receipt Element";
		}

		public void actionPerformed(ActionEvent e) {
			menuEditDeleteReceiptElement_actionPerformed(e);
		}
	}

	private class MenuHelpAbout extends AbstractAction implements JCMSToolBar.NamedAction {

		public MenuHelpAbout() {
			super("About", CMSImageIcons.getInstance().getAbout());
		}

		public String getActionName() {
			return "About";
		}

		public void actionPerformed(ActionEvent e) {
			helpAbout_actionPerformed(e);
		}
	}

	private class MenuFilePrintBbt extends AbstractAction implements JCMSToolBar.NamedAction {

		public MenuFilePrintBbt() {
			super("Print Receipt Blueprint Sample", CMSImageIcons.getInstance().getPrint());
		}

		public String getActionName() {
			return "Print Receipt Blueprint Sample";
		}

		public void actionPerformed(ActionEvent e) {
			filePrintBpt_actionPerformed(e);
		}
	}

	private class MenuFileDetachRDO extends AbstractAction implements JCMSToolBar.NamedAction {

		public MenuFileDetachRDO() {
			super("Detach Receipt Sample Data Object", CMSImageIcons.getInstance().getPlugOut());
		}

		public String getActionName() {
			return "Detach Receipt Sample Data Object";
		}

		public void actionPerformed(ActionEvent e) {
			fileDetachRDO_actionPerformed(e);
		}
	}

	private class MenuViewFtr extends AbstractAction implements JCMSToolBar.NamedAction {

		public MenuViewFtr() {
			super("Footers", CMSImageIcons.getInstance().getFooters());
		}

		public String getActionName() {
			return "Footers";
		}

		public void actionPerformed(ActionEvent e) {
			menuViewFooters_actionPerformed(e);
		}
	}

	private class Menu2ViewReceipts extends AbstractAction implements JCMSToolBar.NamedAction {

		public Menu2ViewReceipts() {
			super("Receipts", CMSImageIcons.getInstance().getReceipts());
		}

		public String getActionName() {
			return "Receipts";
		}

		public void actionPerformed(ActionEvent e) {
			menuViewReceipts_actionPerformed(e);
		}
	}

	private class Menu2FileNewFtr extends AbstractAction implements JCMSToolBar.NamedAction {

		public Menu2FileNewFtr() {
			super("New Footer", CMSImageIcons.getInstance().getNewFooter());
		}

		public String getActionName() {
			return "New Footer";
		}

		public void actionPerformed(ActionEvent e) {
			menu2NewFooter_actionPerformed(e);
		}
	}

	private class Menu2FileOpenFtr extends AbstractAction implements JCMSToolBar.NamedAction {

		public Menu2FileOpenFtr() {
			super("Open Footer", CMSImageIcons.getInstance().getOpen());
		}

		public String getActionName() {
			return "Open Footer";
		}

		public void actionPerformed(ActionEvent e) {
			menu2OpenFooter_actionPerformed(e);
		}
	}

	private class Menu2FileSaveFtr extends AbstractAction implements JCMSToolBar.NamedAction {

		public Menu2FileSaveFtr() {
			super("Save Footer", CMSImageIcons.getInstance().getSave());
		}

		public String getActionName() {
			return "Save Footer";
		}

		public void actionPerformed(ActionEvent e) {
			menu2SaveFooter_actionPerformed(e);
		}
	}

	private class Menu2FileCloseFtr extends AbstractAction implements JCMSToolBar.NamedAction {

		public Menu2FileCloseFtr() {
			super("Close Footer", CMSImageIcons.getInstance().getClose());
		}

		public String getActionName() {
			return "Close Footer";
		}

		public void actionPerformed(ActionEvent e) {
			menu2CloseFooter_actionPerformed(e);
		}
	}

	private class Menu2EditAddFtrElement extends AbstractAction implements JCMSToolBar.NamedAction {

		public Menu2EditAddFtrElement() {
			super("Add Footer Element", CMSImageIcons.getInstance().getAddElement());
		}

		public String getActionName() {
			return "Add Footer Element";
		}

		public void actionPerformed(ActionEvent e) {
			menu2AddFooterElement_actionPerformed(e);
		}
	}

	private class Menu2EditDeleteFtrElement extends AbstractAction implements JCMSToolBar.NamedAction {

		public Menu2EditDeleteFtrElement() {
			super("Delete Footer Element", CMSImageIcons.getInstance().getDeleteElement());
		}

		public String getActionName() {
			return "Delete Footer Element";
		}

		public void actionPerformed(ActionEvent e) {
			menu2DeleteFooterElement_actionPerformed(e);
		}
	}

	public void menu2NewFooter_actionPerformed(ActionEvent e) {
		if (receiptFooter != null) {
			int ok = JOptionPane.showOptionDialog(this, "The new footer will overlay the one you are currently editing,\nselect cancel if you want to save the current one first.",
					"Overlay current blueprint?", JOptionPane.OK_CANCEL_OPTION, JOptionPane.QUESTION_MESSAGE, null, null, null);
			if (ok != JOptionPane.OK_OPTION)
				return;
		}
		String ftrName = JOptionPane.showInputDialog(this, "Please enter the name for the new Receipt Footer.", "Enter Footer name", JOptionPane.QUESTION_MESSAGE);
		if (ftrName == null)
			return;
		if (ftrName.endsWith(".ftr"))
			ftrName = ftrName.substring(0, ftrName.length() - 4);
		receiptFooter = new ReceiptBlueprint(ftrName);
		setFooterModified(false);
		ReceiptReport[] receiptReports = { new ReceiptReport() };
		receiptFooter.setReceiptReports(receiptReports);
		loadFooter();
		statusBar.setText(" ");
		Properties props = System.getProperties();
		if (props.get("default.ftr_folder") == null)
			props.put("default.ftr_folder", "");
		footerTitle = "Receipt Builder  " + props.get("default.ftr_folder") + File.separatorChar + ftrName + ".ftr";
		this.setTitle(footerTitle);
		char[] separatorArray = { File.separatorChar };
		String separatorString = new String(separatorArray);
		String pathName = new String(props.get("default.ftr_folder") + separatorString + ftrName + ".ftr");
		File ftrFile = new File(pathName);
		props.put("default.ftr_file", ftrFile);
	}

	public void menu2OpenFooter_actionPerformed(ActionEvent e) {
		if (!closeFtr())
			return;
		Properties props = System.getProperties();
		JFileChooser chooser = CMSJFileChooserHelper.fixFileChooser(new JFileChooser((String) props.get("user.dir")));
		String previousDir = (String) props.get("default.ftr_folder");
		System.out.println("default footer folder: " + previousDir);
		if (previousDir != null && previousDir.length() > 0)
			chooser.setCurrentDirectory(new File(previousDir));
		ExampleFileFilter filter = new ExampleFileFilter();
		filter.addExtension("ftr");
		filter.setDescription("Receipt Footers");
		chooser.setFileFilter(filter);
		int returnVal = chooser.showOpenDialog(this);
		if (returnVal == JFileChooser.APPROVE_OPTION) {
			String fileName = chooser.getSelectedFile().getName();
			props.put("default.ftr_file", chooser.getSelectedFile());
			props.put("default.ftr_folder", chooser.getCurrentDirectory().toString());
			footerTitle = "Receipt Builder  " + props.get("default.ftr_folder") + File.separatorChar + fileName;
			this.setTitle(footerTitle);
			System.setProperties(props);
			StringBuffer fnBuf = new StringBuffer();
			fnBuf.append(chooser.getCurrentDirectory());
			fnBuf.append(File.separatorChar);
			fnBuf.append(fileName);
			try {
				ObjectStore objectStore = new ObjectStore(fnBuf.toString());
				System.out.println("created object store using fnbuf: " + fnBuf.toString());
				Object temp = objectStore.read();
				if (temp == null)
					System.out.println("null upon read????");
				if (!(temp instanceof ReceiptBlueprint)) {
					JOptionPane.showMessageDialog(this, "The file does not appear to be a valid footer object", "Invalid Footer", JOptionPane.ERROR_MESSAGE);
					return;
				}
				receiptFooter = ((ReceiptBlueprint) temp).cloneAsCurrentClassVersion();
				System.out.println("receipt footer read in name: " + receiptFooter.getName());
			} catch (Exception ex) {
				System.out.println("exception on open ftr...");
				System.out.println(ex.getClass());
				ex.printStackTrace();
				System.out.println("checking message");
				if (ex.getMessage().endsWith("does not contain a serialized object"))
					JOptionPane.showMessageDialog(this, "The file does not appear to contain a valid footer object", "Not a Serialized Object", JOptionPane.ERROR_MESSAGE);
				else
					JOptionPane.showMessageDialog(this, "Unable to unserialize object.\n" + ex.getMessage(), "Open File", JOptionPane.ERROR_MESSAGE);
				return;
			}
			loadFooter();
			statusBar.setText(" ");
			setFooterModified(false);
		}
	}

	public void menu2SaveFooter_actionPerformed(ActionEvent e) {
		saveFooter();
	}

	public void menu2CloseFooter_actionPerformed(ActionEvent e) {
		if (!closeFtr())
			return;
		setFooterModified(false);
		isFtrGUIFullyConstructed = false;
		footerTitle = "Receipt Builder";
		setTitle(footerTitle);
		receiptFooter = null;
		pnlFootersWorkArea.removeAll();
		pnlFootersWorkArea.repaint();
	}

	//  dummy, this is not a menu option
	public void menu2AddFooterLine_actionPerformed(ActionEvent e) {

		//JScrollPane receiptReportTabPane = (JScrollPane)receiptFooterTab.getComponent(0);
		//Box receiptPanel = (Box)receiptReportTabPane.getViewport().getView();
		Box receiptPanel = getReceiptPanel();
		// first remove glue
		if (receiptPanel.getComponentCount() > 0)
			receiptPanel.remove(receiptPanel.getComponentCount() - 1);
		// create a receipt element and line
		ReceiptElement[] receiptElements = { new ReceiptElement("<---Text Element--->") };
		ReceiptLine receiptLine = new ReceiptLine(receiptElements);
		// determine selected report
		ReceiptReport[] receiptReports = receiptFooter.getReceiptReports();
		int reportX = 0;
		receiptReports[reportX].addReceiptLine(receiptLine);
		// shadow this or it will think it is already the current and focused group
		ftrLineGroupPanel = new LineGroupPanel(this, receiptLine);
		receiptPanel.add(ftrLineGroupPanel);
		// create a physical gui panel
		JPanel physicalLinePanel = new JPanel();
		physicalLinePanel.setLayout(new BoxLayout(physicalLinePanel, BoxLayout.X_AXIS));
		ftrLineGroupPanel.add(physicalLinePanel);
		// create and add gui representation

		ReceiptElementJTextField initialElement = new ReceiptElementJTextField(this, receiptElements[0], pnlFtrElementAttributes);
		physicalLinePanel.add(initialElement);
		// try this.....
		physicalLinePanel.add(Box.createHorizontalGlue());
		receiptPanel.add(Box.createVerticalGlue());
		receiptPanel.validate();
		getReceiptScrollPane().repaint();

		initialElement.requestFocus();
		setFooterModified(true);
		pack();
		physicalLinePanel.scrollRectToVisible(physicalLinePanel.getBounds());
	}

	public void menu2AddFooterElement_actionPerformed(ActionEvent e) {
		JScrollPane receiptReportTabPane = (JScrollPane) receiptFooterTab.getComponent(0);
		Box receiptPanel = (Box) receiptReportTabPane.getViewport().getView();
		Component[] components = receiptPanel.getComponents();
		if (ftrLineGroupPanel == null) {
			menu2AddFooterLine_actionPerformed(e);
			return;
		}
		ReceiptElement newReceiptElement = new ReceiptElement();
		newReceiptElement.setStringToPrint("<---Text Element--->");
		addReceiptElement(ftrLineGroupPanel, newReceiptElement, -1, -1);
	}

	public void menu2DeleteFooterElement_actionPerformed(ActionEvent e) {
		menuEditDeleteReceiptElement_actionPerformed(e);
	}
} // end of MainFrame

