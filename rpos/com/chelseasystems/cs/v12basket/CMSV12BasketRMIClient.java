package com.chelseasystems.cs.v12basket;

import java.rmi.ConnectException;
import java.rmi.RMISecurityManager;
import java.util.Date;

import com.chelseasystems.cr.appmgr.DowntimeException;
import com.chelseasystems.cr.config.ConfigMgr;
import com.chelseasystems.cr.config.NetworkMgr;
import com.chelseasystems.cr.logging.LoggingServices;
import com.chelseasystems.cr.node.ICMSComponent;
import com.chelseasystems.cr.node.IRemoteServerClient;
import com.igray.naming.NamingService;

public class CMSV12BasketRMIClient extends CMSV12BasketServices implements
		IRemoteServerClient {

	/** The configuration manager */
	private ConfigMgr config = null;

	/** The reference to the remote implementation of the service. */
	private ICMSV12BasketRMIServer icmsv12BasketServer = null;

	private int maxTries = 1;

	/**
	 * Set the configuration manager and make sure that the system has a
	 * security manager set.
	 **/
	public CMSV12BasketRMIClient() throws DowntimeException {
		config = new ConfigMgr("v12basket.cfg");
		if (System.getSecurityManager() == null)
			System.setSecurityManager(new RMISecurityManager());
		init();
	}

	/**
	 * Get the remote interface from the registry.
	 */
	private void init() throws DowntimeException {
		try {
			this.lookup();
			System.out.println("CMSV12BasketRMIClient Lookup: Complete");
		} catch (Exception e) {
			LoggingServices
					.getCurrent()
					.logMsg(getClass().getName(),
							"init()",
							"Cannot establish connection to RMI server.",
							"Make sure that the server is registered on the remote server"
									+ " and that the name of the remote server and remote service are"
									+ " correct in the update.cfg file.",
							LoggingServices.MAJOR, e);
			throw new DowntimeException(e.getMessage());
		}
	}

	/**
	 * Perform lookup of remote server.
	 * 
	 * @exception Exception
	 */
	public void lookup() throws Exception {
		NetworkMgr mgr = new NetworkMgr("network.cfg");
		mgr.getRetryAttempts();
		String connect = mgr.getRMIMasterNode()
				+ config.getString("REMOTE_NAME") + mgr.getQuery();
		icmsv12BasketServer = (ICMSV12BasketRMIServer) NamingService
				.lookup(connect);
	}

	/**
	 * @see com.chelseasystems.cr.node.ICMSComponent
	 * @return <true> is component is available
	 */
	public boolean isRemoteServerAvailable() {
		try {
			return ((ICMSComponent) this.icmsv12BasketServer).isAvailable();
		} catch (Exception ex) {
			return false;
		}
	}

	public CMSV12Basket[] getBasketDetails(Date date, String storeId)
			throws Exception {
		for (int x = 0; x < maxTries; x++) {
			if (icmsv12BasketServer == null)
				init();
			try {
				return icmsv12BasketServer.getBasketDetails(date, storeId);
			} catch (ConnectException ce) {
				icmsv12BasketServer = null;
			} catch (Exception ex) {
				throw new DowntimeException(ex.getMessage());
			}
		}
		throw new DowntimeException(
				"Unable to establish connection to CMSV12BasketServices");
	}

	@Override
	public boolean setBasketStatus(CMSV12Basket cmsV12Basket, String setStatus)
			throws Exception {
		for (int x = 0; x < maxTries; x++) {
			if (icmsv12BasketServer == null)
				init();
			try {
				return icmsv12BasketServer.setBasketStatus(cmsV12Basket,
						setStatus);
			} catch (ConnectException ce) {
				icmsv12BasketServer = null;
			} catch (Exception ex) {
				throw new DowntimeException(ex.getMessage());
			}
		}
		throw new DowntimeException(
				"Unable to establish connection to CMSV12BasketServices");

	}
}
