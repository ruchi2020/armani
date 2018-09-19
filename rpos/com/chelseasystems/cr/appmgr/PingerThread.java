/*
 * @copyright (c) 1998-2002 Retek Inc
 */

package com.chelseasystems.cr.appmgr;

import java.rmi.Remote;
import java.util.ArrayList;
import java.util.Iterator;

import com.chelseasystems.cr.appmgr.peer.IPeerManager;
import com.chelseasystems.cr.appmgr.peer.IPingerRMIPeer;
import com.chelseasystems.cr.config.ConfigMgr;
import com.chelseasystems.cr.logging.LoggingServices;
import com.chelseasystems.cr.node.IRemoteServerClient;
import com.chelseasystems.cr.util.Trace;

/**
 */
public class PingerThread extends Thread {

	private static int instances;

	private IPeerManager peerMgr;
	private DownTimeManager dtMgr;
	private boolean bOnline = false;
	private boolean isActive = false;

	/**
	 * @param    IPeerManager peerMgr
	 * @param    DownTimeManager dtMgr
	 * @param    String[] CompNames
	 */
	public PingerThread(IPeerManager peerMgr, DownTimeManager dtMgr) {
		this.setName("PingerThread-" + ++instances);
		this.peerMgr = peerMgr;
		this.dtMgr = dtMgr;
	}

	/**
	 * @return
	 */
	public boolean isActive() {
		return isActive;
	}

	/**
	 */
	public void run() {
		while(true) {
			isActive = false;
			System.out.println("&&& the pinger thread is going into suspend mode...");
			suspend();
			System.out.println("&&& the pinger thread is out of suspend mode...");
			// determine if another peer is alreay pinging
			checkForActivePingers();
			// at this point, no other peers are pinging, check to see if another
			// peer turned us online
			if (dtMgr.isOnLine()) {
				System.out.println("&&& This application is already online.. continue");
				continue;
			}
			isActive = true;
			System.out.println("&&& PingerThread started...");
			boolean offLine = true;
			boolean isComponentOnline = true;
			while(offLine) {
				isComponentOnline = true;
				try {
					sleep(60);
					String[] keys = DownTimeManager.getClientServiceKeys();
					for (int i = 0; i < keys.length; i++) {
						try {
							ClientServices clientService = (ClientServices) dtMgr.getRepositoryManager().getGlobalObject(keys[i]);
							Class onlineService = clientService.getOnlineService();
							if (onlineService != null && IRemoteServerClient.class.isAssignableFrom(onlineService)) {
								IRemoteServerClient client = (IRemoteServerClient) onlineService.newInstance();
								if (!client.isRemoteServerAvailable()) {
									//TD
									//System.out.println("PingerThread Service Not Available: " + client.getClass().getName());
									isComponentOnline = false;
									break;
								}
							}
						} catch (Exception ex1) {
							Trace.ex(ex1);
							System.out.println("PingerThread Failed " + keys[i]);
							isComponentOnline = false;
							break;
						}
					}
				} catch (Exception ex2) {
					System.err.println("PingerThread().run()->" + ex2);
				}
				if (isComponentOnline) {
					offLine = false;
					dtMgr.setOnLine(true);
					System.out.println("PingerThread- Online - multicasting");
					multicastOnline();
				}
			} // end inner while
		}
	}

	/**
	 *
	 */
	private void multicastOnline() {
		try {
			System.out.println("&&& Mulicasting to all peers that the system is online...");
			boolean isPingerActive = false;
			Remote[] rmts = peerMgr.getPeerStubs("pinger");
			if (rmts == null)
				return;
			for (int x = 0; x < rmts.length; x++) {
				IPingerRMIPeer pinger = (IPingerRMIPeer) rmts[x];
				try {
					pinger.onlineMode();
				} catch (Exception ex) {
					peerMgr.removeRemotePeerStub("pinger", rmts[x]);
				}
			}
			System.out.println("&&& Mulicasting to all peers that the system is online...Complete");
		} catch (Exception ex) {
			System.err.println("PingerThread.multicastOnline()->" + ex);
		}
	}

	/**
	 */
	private void checkForActivePingers() {
		System.out.println("&&& Starting the loop to check to peers that are alreading pinging...");
		while(true) {
			try {
				boolean isPingerActive = false;
				Remote[] rmts = peerMgr.getPeerStubs("pinger");
				if (rmts == null)
					return;
				for (int x = 0; x < rmts.length; x++) {
					IPingerRMIPeer pinger = (IPingerRMIPeer) rmts[x];
					try {
						if (pinger.isPingingActive()) {
							System.out.println("&&& A Peer is actively pinging");
							isPingerActive = true;
						}
					} catch (Exception ex) {
						peerMgr.removeRemotePeerStub("pinger", rmts[x]);
					}
				}
				// if not pingers are active;
				if (!isPingerActive) {
					System.out.println("&&& A Peer is not pinging, exiting loop");
					return;
				}
				sleep(60000);
			} catch (Exception ex) {
				System.err.println("PingerThread.checkForActivePingers()->" + ex);
			}
			System.out.println("&&& Completed the loop to check to peers that are alreading pinging...");
		} //end while
	}
}
