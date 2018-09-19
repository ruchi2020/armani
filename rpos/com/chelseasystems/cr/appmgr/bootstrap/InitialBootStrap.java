/*
 * @copyright (c) 2002 Retek Inc.
 */

package com.chelseasystems.cr.appmgr.bootstrap;

import java.awt.Window;
import java.awt.Frame;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import com.chelseasystems.cr.appmgr.IBrowserManager;
import com.chelseasystems.cr.appmgr.BrowserManager;
import com.chelseasystems.cr.config.ConfigMgr;
import com.zelator.winterface.WInterface;

/**
 * This is the parent class for BrowserInitialBootStrap.   Unlike this class,
 * BrowserInitialBootStrap does not have a GUI.  You will find that
 * BrowserInitialBootStrap overwrites methods whose only purpose is to write
 * to the GUI.  Therefore, any changes to this class must also be made to
 * BrowserInitialBootStrap.
 */
public class InitialBootStrap implements IBootStrap {

	/**
	 * Key to config file entry that specifies whether this bootstrap should
	 * primpt Eu for training or production option.  Valid values are:
	 * true, false or prompt.
	 */
	public static final String TRAINING_MODE = "TRAINING_MODE";

	ProgressDlg progressDlg = null;
	TrainingDlg trainingDlg = null;
	StartUpModeDlg modeDlg = null;
	IBrowserManager theMgr;
	Window parentframe;

	/**
	 */
	public InitialBootStrap() {
	}

	/**
	 * @return
	 */
	public String getName() {
		return "Initial BootStrap";
	}

	/**
	 * @return
	 */
	public String getDesc() {
		return "desc";
	}

	/**
	 * @param theMgr
	 * @param parentframe
	 * @param bootMgr
	 * @return BootStrapInfo
	 */
	public BootStrapInfo start(IBrowserManager theMgr, Window parentframe, BootStrapManager bootMgr) {
		this.theMgr = theMgr;

		trainingDlg = new TrainingDlg(parentframe);
		progressDlg = new ProgressDlg(parentframe);
		modeDlg = new StartUpModeDlg(parentframe);

		ConfigMgr configMgr = new ConfigMgr(System.getProperty("USER_CONFIG"));
		String strMode = configMgr.getString(TRAINING_MODE);

		if (strMode != null && strMode.equalsIgnoreCase("prompt")) {
			trainingDlg.setVisible(true);
		} else if (strMode != null) {
			System.setProperty("TRAINING", strMode);
		}
		//System.out.println("___Tim: " + " Calling WInterface in InitialBootStrap 1 ....");
		progressDlg.setAlwaysOnTop(true);
		//WInterface.setOnTop(progressDlg);
		//WInterface.setOnTop(parentframe);
		progressDlg.setVisible(true);
		showModeDlg();

		return new BootStrapInfo(BootStrapInfo.class.getName());
	}

	/**
	 */
	private void showModeDlg() {
		boolean bTraining = false;
		String sTrain = System.getProperty("TRAINING");
		if (sTrain != null) {
			Boolean bTrain = new Boolean(sTrain);
			if (bTrain.booleanValue()) {
				bTraining = true;
				theMgr.addGlobalObject("TRAINING", new Boolean(true));
				if (parentframe instanceof Frame)
					((Frame) parentframe).setTitle("Training Mode");
			}
		} else {
			// To get to this point requires that the training dlg be closed.
			// Under this condition, the operator aborted...
			System.out.println("Operator aborted application...");
			System.exit(-1);
		}
		if (bTraining)
			modeDlg.setMode("Training Mode");
		else
			modeDlg.setMode("Production Mode");
		//System.out.println("___Tim: " + " Calling WInterface in InitialBootStrap 2 ....");
		
		//TD
		modeDlg.setAlwaysOnTop(true);
		//WInterface.setOnTop(modeDlg);
		//WInterface.setOnTop(modeDlg.getOwner());
		modeDlg.setVisible(true);
	}

	/**
	 * @param status a resourceable string that is the status
	 */
	void setBootStrapStatus(String status) {
		setBootStrapStatus(status, null);
	}

	/**
	 * @param status a resourceable string that is the status
	 * @param value a non-resourceable string that describes the status
	 */
	void setBootStrapStatus(String status, String value) {
		if (!theMgr.isOnLine())
			progressDlg.setOffLine();
		progressDlg.setStatus(status, value);
	}

	/**
	 * @param value
	 */
	void setProgress(int value) {
		progressDlg.setProgress(value);
	}

	/**
	 *
	 */
	void closeDlgs() {
		setBootStrapStatus("Generating window...");
		Window window = ((BrowserManager) theMgr).getActiveWindow();
		if (window != null)
			window.addWindowListener(new WindowAdapter() {
				public void windowOpened(WindowEvent evt) {
					progressDlg.dispose();
					modeDlg.dispose();
				}
			});
	}
}
