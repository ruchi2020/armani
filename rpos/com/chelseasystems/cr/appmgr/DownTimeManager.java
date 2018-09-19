/*
 * This unpublished work is protected by trade secret, copyright and other laws.
 * In the event of publication, the following notice shall apply:
 * Copyright &copy; 2004 Retek Inc.  All Rights Reserved.
 */
package com.chelseasystems.cr.appmgr;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.StringTokenizer;

import com.chelseasystems.cr.appmgr.peer.IPeerManager;
import com.chelseasystems.cr.config.ConfigMgr;

/**
 * The DownTimeManager allows the system to know when it has a valid connection
 * to back-end services. This is partly accompished through the ability for any
 * object in the system that implements <CODE>DownTimeListener</CODE> to be
 * notified when the system changes from on-line mode (services are all
 * available from the back-end) to off-line mode (services are not available on
 * the back end) by registering with the <CODE>DownTimeManager</CODE> instance
 * in the <CODE>AppManager</CODE>.
 *
 * @see com.chelseasystems.cr.appmgr.DownTimeListener
 * @see com.chelseasystems.cr.appmgr.AppManager
 */
public class DownTimeManager implements IDownTimeManager {

	private static String[] clientServiceKeys;

	private IRepositoryManager repMgr;
	private boolean online = true;
	private PingerThread pinger = null;
	private List listeners = null;

	/**
	 *
	 * @param bsrMgr
	 */
	public DownTimeManager(IBrowserManager bsrMgr) {
		this(bsrMgr, bsrMgr);
	}

	/**
	 *
	 * @param repMgr
	 * @param peerMgr
	 */
	public DownTimeManager(IRepositoryManager repMgr, IPeerManager peerMgr) {
		this.repMgr = repMgr;
		this.listeners = Collections.synchronizedList(new ArrayList(10));
		pinger = new PingerThread(peerMgr, this);
		pinger.start();
	}

	/**
	 * @return isOnline
	 */
	public boolean isOnLine() {
		return online;
	}

	/**
	 * @return is pinger thread active
	 */
	public boolean isPingerActive() {
		return pinger.isActive();
	}

	/**
	 *
	 * @param isOnline
	 */
	public void setOnLine(boolean isOnline) {
		if (isOnline) {
			System.out.println("OnLine mode in DT-Mgr");
			online = true;
			String[] serviceNames = getClientServiceKeys();
			for (int x = 0; x < serviceNames.length; x++) {
				ClientServices service = (ClientServices) repMgr.getGlobalObject(serviceNames[x]);
				if (service != null)
					service.onLineMode();
			}
			this.fireDownTimeEvent(true);
		} else {
			if (!isOnLine()) {
				System.out.println("returning due to offline already being set");
				return;
			}
			online = false;
			System.out.println("Offline mode in DT-Mgr");
			String[] keys = getClientServiceKeys();
			for (int i = 0; i < keys.length; i++) {
				ClientServices cs = (ClientServices) repMgr.getGlobalObject(keys[i]);
				if (cs != null) {
					// turn client service to offline mode
					cs.offLineMode();
				}
			}
			this.fireDownTimeEvent(false);
			pinger.resume(); // start pinger thread checking for online mode
		}
	}

	/**
	 * @param listener
	 */
	public void addDownTimeListener(DownTimeListener listener) {
		this.listeners.add(listener);
	}

	/**
	 * @param listener
	 */
	public void removeDownTimeListener(DownTimeListener listener) {
		this.listeners.remove(listener);
	}

	/**
	 */
	public void clearDownTimeListeners() {
		this.listeners.clear();
	}

	////////////////////////////////////////
	//
	// Protected methods
	//
	////////////////////////////////////////

	/**
	 *
	 * @param isOnLine
	 */
	protected void fireDownTimeEvent(boolean isOnLine) {
		synchronized (this.listeners) {
			int size = this.listeners.size();
			for (int i = 0; i < size; i++) {
				((DownTimeListener) this.listeners.get(i)).downTimeEvent(isOnLine);
			}
		}
	}

	/**
	 * @return  reference to the repository mgr
	 */
	protected IRepositoryManager getRepositoryManager() {
		return this.repMgr;
	}

	/**
	 * @return list of service keys
	 */
	protected static String[] getClientServiceKeys() {
		if (clientServiceKeys == null) {
			try {
				List list = new ArrayList();
				String sConfigFile = System.getProperty("USER_CONFIG");
				ConfigMgr mgr = new ConfigMgr(sConfigFile);
				String services = mgr.getString("SERVICES_LIST");
				StringTokenizer tokens = new StringTokenizer(services, ",");
				while(tokens.hasMoreElements()) {
					list.add(tokens.nextToken());
				} // end while
				clientServiceKeys = (String[]) list.toArray(new String[list.size()]);
			} catch (Exception ex) {
				ex.printStackTrace();
			}
		}
		return clientServiceKeys;
	}

}
