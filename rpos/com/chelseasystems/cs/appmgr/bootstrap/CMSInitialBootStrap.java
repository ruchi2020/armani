/**
 * 
 */
package com.chelseasystems.cs.appmgr.bootstrap;

import java.awt.Window;

import com.chelseasystems.cr.appmgr.IBrowserManager;
import com.chelseasystems.cr.appmgr.bootstrap.BootStrapInfo;
import com.chelseasystems.cr.appmgr.bootstrap.BootStrapManager;
import com.chelseasystems.cr.appmgr.bootstrap.InitialBootStrap;
import com.chelseasystems.cs.appmgr.daemon.SingleInstanceDaemon;

/**
 * @author vikram
 *
 */
public class CMSInitialBootStrap extends InitialBootStrap {

	/**
	 * @param theMgr
	 * @param parentframe
	 * @param bootMgr
	 * @return BootStrapInfo
	 */
	public BootStrapInfo start(IBrowserManager theMgr, Window parentframe, BootStrapManager bootMgr) {
		BootStrapInfo bootStrapInfo = super.start(theMgr, parentframe, bootMgr);
		loadAndStartSingleInstanceDaemon(theMgr);
		return bootStrapInfo;
	}

	private void loadAndStartSingleInstanceDaemon(IBrowserManager theMgr) {
		SingleInstanceDaemon daemon = SingleInstanceDaemon.getInstance(theMgr);
		if (!daemon.isAlive())
			daemon.start();
	}

}
