/*
 * This unpublished work is protected by trade secret, copyright and other laws.
 * In the event of publication, the following notice shall apply:
 * Copyright &copy; 2005 Retek Inc.  All Rights Reserved.
 */
package com.chelseasystems.cr.swing;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import java.util.Locale;
import java.util.ResourceBundle;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTextArea;

import com.chelseasystems.cr.appmgr.AppManager;
import com.chelseasystems.cr.appmgr.BrowserManager;
import com.chelseasystems.cr.appmgr.Theme;
import com.chelseasystems.cr.config.FileMgr;
import com.chelseasystems.cr.util.INIFile;
import com.chelseasystems.cr.util.INIFileException;
import com.chelseasystems.cr.util.ResourceManager;

public class MainFrame extends JFrame implements IMainFrame {

	//~ Instance fields ------------------------------------------------------------------------------

	JPanel pnlClient;
	JPanel pnlMain;
	JTextArea txtStatus;
	ResourceBundle res;
	private IAppToolbar appToolBar;
	private IGlobalBar globalBar;
	private boolean minimizeEnabled;
	private int anchor;
	private int mode;

	//~ Constructors ---------------------------------------------------------------------------------

	public MainFrame() {
		pnlClient = new JPanel();
		pnlMain = new JPanel();
		txtStatus = new JTextArea();
		res = ResourceManager.getResourceBundle();
		mode = 0;
		try {
			loadUserPreferences();
			jbInit();
			installListeners();
			//setTitle(res.getString("RPOS"));
			//Sergio
			setTitle(res.getString(" ARM - Armani Retail Management"));
			setAnchor(anchor);
			pack();
			setResizable(false);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	//~ Methods --------------------------------------------------------------------------------------

	public void setAnchor(int anchor) throws Exception {
		if ((anchor >= 0) && (anchor <= 3)) {
			appToolBar.setAnchor(anchor);
			globalBar.setAnchor(anchor);
			switch (anchor) {
				case 1: // '\001'
					getContentPane().add((Container) appToolBar, "West");
					pnlMain.add((Container) globalBar, "South");
					break;
				case 2: // '\002'
					getContentPane().add((Container) appToolBar, "East");
					pnlMain.add((Container) globalBar, "North");
					break;
				case 3: // '\003'
					getContentPane().add((Container) appToolBar, "West");
					pnlMain.add((Container) globalBar, "North");
					break;
				default:
					getContentPane().add((Container) appToolBar, "East");
					pnlMain.add((Container) globalBar, "South");
					break;
			}
			getContentPane().validate();
			getContentPane().repaint();
			if (this.anchor != anchor) {
				this.anchor = anchor;
				INIFile iniConfig = new INIFile(FileMgr.getLocalFile("config", System.getProperty("USER_CONFIG")), false);
				iniConfig.writeEntry("USER_PREFERENCES", "ANCHOR", Integer.toString(anchor));
			}
		}
	}

	public int getAnchor() {
		return anchor;
	}

	public IAppToolbar getAppToolBar() {
		return appToolBar;
	}

	public int getCurrentPageNumber() {
		return 0;
	}

	public IGlobalBar getGlobalBar() {
		return globalBar;
	}

	public void setLocale(Locale aLocale) {
		res = ResourceManager.getResourceBundle();
		if (globalBar != null) {
			globalBar.setLocale(aLocale);
		}
		super.setLocale(aLocale);
	}

	public int getPageCount() {
		return 0;
	}

	public double getRatio() {
		return (double) (getSize().width / 1024);
	}

	public void setTheme(Theme aTheme) throws Exception {
		globalBar.setTheme(aTheme);
		appToolBar.setTheme(aTheme);
		getContentPane().removeAll();
		jbInit();
		setAnchor(anchor);
		setResizable(true);
		pack();
		setResizable(false);
	}

	public void setTransitionColor(Color newColor) {
		globalBar.setTransitionColor(newColor);
		appToolBar.setTransitionColor(newColor);
	}

	public void applicationKeyPressed(ActionEvent anEvent) {
		String command = anEvent.getActionCommand();
		if (command.equals("FONT_CMD")) {
			toggleFontMode();
		} else if (command.equals("BUILD_HELP_CMD")) {
			toggleBuildHelpMode();
		}
	}

	public void nextPage() {
		getGlobalBar().doClickPageDown();
	}

	public void prevPage() {
		getGlobalBar().doClickPageUp();
	}

	public void selectF2() {
		appToolBar.fireApplicationKeyEvent(new ActionEvent(this, 0, "1"));
	}

	public void show() {
		Insets inset = getInsets();
		int h = inset.top + inset.bottom + appToolBar.getPreferredSize().height;
		int w = inset.left + inset.right + appToolBar.getPreferredSize().width + globalBar.getPreferredSize().width;
		setSize(w, h);
		super.show();
	}

	public void showCMSApplet(CMSApplet applet) {
		pnlClient.removeAll();
		pnlClient.add(applet, "Center");
		pnlClient.revalidate();
	}

	public void showHelpPane(boolean show) {
		mode = show ? 2 : 0;
	}

	public void toggleBuildHelpMode() {
		if (mode != 0) {
			resetMode();
		} else {
			try {
				INIFile iniConfig = new INIFile(FileMgr.getLocalFile("config", System.getProperty("USER_CONFIG")), false);
				if (iniConfig.getValue("ENABLE_BUILD_HELP_MODE").equalsIgnoreCase("true")) {
					mode = 3;
					globalBar.toggleBuildHelpAnimation(true);
					((AppManager) BrowserManager.getInstance()).actionPerformed(new ActionEvent(this, 0, "HELP_CMD"));
				}
			} catch (INIFileException ex) {
				System.err.println("MainFrame.toggleFontMode" + ex);
			}
		}
	}

	public void toggleFontMode() {
		if (mode != 0) {
			resetMode();
		} else {
			try {
				INIFile iniConfig = new INIFile(FileMgr.getLocalFile("config", System.getProperty("USER_CONFIG")), false);
				if (iniConfig.getValue("ENABLE_FONT_MODE").equalsIgnoreCase("true")) {
					mode = 1;
					globalBar.toggleFontAnimation(true);
					FontGlassPane gpane = new FontGlassPane();
					getRootPane().setGlassPane(gpane);
					gpane.setVisible(true);
				}
			} catch (INIFileException ex) {
				System.err.println("MainFrame.toggleFontMode" + ex);
			}
		}
	}

	private void installListeners() {
		addWindowListener(new WindowAdapter() {
			public void windowIconified(WindowEvent e) {
				if (!minimizeEnabled) {
					System.out.println("WindowEvent.WINDOW_ICONIFIED");
					setState(0);
					toFront();
				}
			}

			public void windowActivated(WindowEvent e) {
				if (mode != 0) {
					getRootPane().requestFocus();
				} else if (globalBar.isTextAreaEnabled()) {
					globalBar.setTextAreaFocus();
				}
			}
		});
	}

	private void jbInit() throws Exception {
		getContentPane().add(pnlMain, "Center");
		setIconImage(CMSImageIcons.getInstance().getTitlebar().getImage());
		setDefaultCloseOperation(0);
		pnlMain.setOpaque(false);
		pnlMain.setLayout(new BorderLayout());
		pnlClient.setLayout(new BorderLayout());
		pnlMain.add(pnlClient, "Center");
	}

	private void loadUserPreferences() {
		String strConfigFile = System.getProperty("USER_CONFIG");
		if (strConfigFile == null) {
			System.err.println("FATAL ERROR - USER_CONFIG property not set. Use -DUSER_CONFIG");
			System.exit(-1);
		}
		try {
			INIFile iniConfig = new INIFile(FileMgr.getLocalFile("config", strConfigFile), false);
			globalBar = (IGlobalBar) Class.forName(iniConfig.getValue("GUI", "GLOBALBAR", (com.chelseasystems.cr.swing.GlobalBar.class).getName())).newInstance();
			appToolBar = (IAppToolbar) Class.forName(iniConfig.getValue("GUI", "APPTOOLBAR", (com.chelseasystems.cr.swing.ScrollableToolBarPanel.class).getName())).newInstance();
			appToolBar.addApplicationKeyListener(this);
			appToolBar.addApplicationKeyListener(globalBar);
			anchor = Integer.parseInt(iniConfig.getValue("USER_PREFERENCES", "ANCHOR", "0"));
			minimizeEnabled = iniConfig.getValue("USER_PREFERENCES", "MINIMIZE_ENABLED", "false").equalsIgnoreCase("true");
		} catch (Exception ex) {
			System.err.println("FATAL ERROR - Error opening USER_CONFIG->" + strConfigFile);
			ex.printStackTrace();
			System.exit(-1);
		}
	}

	private void resetMode() {
		mode = 0;
		globalBar.toggleFontAnimation(false);
		globalBar.toggleBuildHelpAnimation(false);
		getRootPane().getGlassPane().setVisible(false);
	}
}
