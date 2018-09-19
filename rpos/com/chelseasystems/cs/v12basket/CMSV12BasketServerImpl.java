package com.chelseasystems.cs.v12basket;

import java.rmi.ConnectException;
import java.rmi.RemoteException;
import java.util.Date;
import java.util.Properties;

import com.chelseasystems.cr.logging.LoggingServices;
import com.chelseasystems.cr.node.CMSComponent;

public class CMSV12BasketServerImpl extends CMSComponent implements
		ICMSV12BasketRMIServer {
	private static final long serialVersionUID = 1L;

	/**
	 *
	 * @param props
	 *            Properties
	 * @throws RemoteException
	 */
	public CMSV12BasketServerImpl(Properties props) throws RemoteException {
		super(props);
		setImpl();
		init();
	}

	/**
	 * sets the current implementation
	 **/
	private void setImpl() {
		Object obj = getConfigManager().getObject("SERVER_IMPL");
		if (null == obj) {
			LoggingServices.getCurrent().logMsg(getClass().getName(),
					"setImpl()", "Could not instantiate SERVER_IMPL.",
					"Make sure register contains SERVER_IMPL",
					LoggingServices.MAJOR);
		}
		CMSV12BasketServices.setCurrent((CMSV12BasketServices) obj);
	}

	/**
	   *
	   */
	private void init() {
		System.out.println("Binding to RMIRegistry...");
		String theName = getConfigManager().getString("REMOTE_NAME");
		if (null != theName) {
			bind(theName, this);
		} else {
			LoggingServices.getCurrent().logMsg(getClass().getName(), "init()",
					"Could not find name to bind to in registry.",
					"Make sure register contains a RMIREGISTRY entry.",
					LoggingServices.MAJOR);
		}
	}

	/**
	 * receives callback when the config file changes
	 * 
	 * @param aKey
	 *            an array of keys that have changed
	 */
	protected void configEvent(String[] aKey) {
	}

	/**
	 * Used by the DowntimeManager to determine when this object is available.
	 * Just because this process is up doesn't mean that the clients can come
	 * up. Make sure that the database is available.
	 * 
	 * @return boolean <code>true</code> indicates that this class is available.
	 **/
	public boolean ping() throws RemoteException {
		return true;
	}

	public CMSV12Basket[] getBasketDetails(Date date, String storeId)
			throws RemoteException {
		long start = getStartTime();
		try {
			if (!isAvailable())
				throw new ConnectException("Service is not available");
			incConnection();
			return (CMSV12Basket[]) CMSV12BasketServices.getCurrent()
					.getBasketDetails(date, storeId);
		} catch (Exception e) {
			throw new RemoteException(e.getMessage(), e);
		} finally {
			addPerformance("getBasketDetails", start);
			decConnection();
		}
	}

	
	public boolean setBasketStatus(CMSV12Basket cmsV12Basket, String setStatus)
			throws RemoteException {
		long start = getStartTime();
		try {
			if (!isAvailable())
				throw new ConnectException("Service is not available");
			incConnection();
			return (boolean) CMSV12BasketServices.getCurrent().setBasketStatus(
					cmsV12Basket, setStatus);
		} catch (Exception e) {
			throw new RemoteException(e.getMessage(), e);
		} finally {
			addPerformance("getBasketDetails", start);
			decConnection();
		}
	}
}
