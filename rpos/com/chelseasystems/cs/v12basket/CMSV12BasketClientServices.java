package com.chelseasystems.cs.v12basket;

import java.util.Date;

import com.chelseasystems.cr.appmgr.ClientServices;
import com.chelseasystems.cr.appmgr.DowntimeException;
import com.chelseasystems.cr.config.ConfigMgr;
import com.chelseasystems.cr.logging.LoggingServices;

public class CMSV12BasketClientServices extends ClientServices {

	/**
	 * Set the current implementation
	 */
	public CMSV12BasketClientServices() {
		// Set up the configuration manager.
		config = new ConfigMgr("v12basket.cfg");
	}

	/**
	 * initialize primary implementation
	 */
	public void init(boolean online) throws Exception {
		// Set up the proper implementation of the service.
		if (online) {
			onLineMode();
		} else {
			offLineMode();
		}
	}

	/**
	 * Reads "CLIENT_IMPL" from config file. Returns the class that defines what
	 * object is providing the service to objects using this client service in
	 * "on-line" mode, i.e. connected to an app server. If null, this
	 * clientservice is not considered when determining app online status.
	 * 
	 * @return a class of the online service.
	 */
	protected Class getOnlineService() throws ClassNotFoundException {
		String className = config.getString("CLIENT_IMPL");
		Class serviceClass = Class.forName(className);
		return serviceClass;
	}

	public void offLineMode() {
		LoggingServices.getCurrent().logMsg(
				"Off-Line Mode for CMSV12BasketClientServices");
		CMSV12BasketServices serviceImpl = (CMSV12BasketServices) config
				.getObject("CLIENT_DOWNTIME");
		if (null == serviceImpl) {
			LoggingServices
					.getCurrent()
					.logMsg("CMSV12BasketClientServices",
							"offLineMode()",
							"Cannot instantiate the class that provides the"
									+ " implementation of CMSV12BasketServices in v12Basket.",
							"Make sure that v12Basket contains an entry with "
									+ "a key of CLIENT_DOWNTIME and a value"
									+ " that is the name of a class that provides a concrete"
									+ " implementation of CMSV12BasketServices.",
							LoggingServices.CRITICAL);
		}
		CMSV12BasketServices.setCurrent(serviceImpl);
	}

	public void onLineMode() {
		LoggingServices.getCurrent().logMsg(
				"On-Line Mode for CMSV12BasketClientServices");
		CMSV12BasketServices serviceImpl = (CMSV12BasketServices) config
				.getObject("CLIENT_IMPL");
		if (null == serviceImpl) {
			LoggingServices
					.getCurrent()
					.logMsg("CMSV12BasketClientServices",
							"onLineMode()",
							"Cannot instantiate the class that provides the"
									+ "implementation of CMSV12BasketServices in v12Basket.",
							"Make sure that v12Basket contains an entry with "
									+ "a key of CLIENT_IMPL and a value that is the name of a class "
									+ "that provides a concrete implementation of CMSV12BasketServices.",
							LoggingServices.MAJOR);
			setOffLineMode();
			return;
		}
		CMSV12BasketServices.setCurrent(serviceImpl);

	}

	/**
	 *
	 * @return Object
	 */
	public Object getCurrentService() {
		return CMSV12BasketServices.getCurrent();
	}

	public CMSV12Basket[] getBasketDetails(Date date, String storeId)
			throws Exception {

		try {
			this.fireWorkInProgressEvent(true);
			CMSV12Basket[] cmsV12Basket = (CMSV12Basket[]) ((CMSV12BasketServices) CMSV12BasketServices
					.getCurrent()).getBasketDetails(date, storeId);
			if (cmsV12Basket == null) {
				CMSV12BasketServices serverImpl = (CMSV12BasketServices) config
						.getObject("CLIENT_IMPL");
				if (serverImpl != null) {
					CMSV12BasketServices.setCurrent(serverImpl);
					cmsV12Basket = (CMSV12Basket[]) ((CMSV12BasketServices) CMSV12BasketServices
							.getCurrent()).getBasketDetails(date, storeId);
				}
				CMSV12BasketServices.setCurrent((CMSV12BasketServices) config
						.getObject("CLIENT_IMPL"));
			}
			return cmsV12Basket;
		} catch (DowntimeException ex) {
			CMSV12BasketServices.setCurrent((CMSV12BasketServices) config
					.getObject("CLIENT_IMPL"));
			LoggingServices
					.getCurrent()
					.logMsg(getClass().getName(),
							"findById",
							"Primary Implementation for CMSV12BasketServices failed, going Off-Line...",
							"See Exception", LoggingServices.MAJOR, ex);
			offLineMode();
			setOffLineMode();
			return (CMSV12Basket[]) ((CMSV12BasketServices) CMSV12BasketServices
					.getCurrent()).getBasketDetails(date, storeId);
		} finally {
			CMSV12BasketServices.setCurrent((CMSV12BasketServices) config
					.getObject("CLIENT_IMPL"));
			this.fireWorkInProgressEvent(false);
		}
	}

	public boolean setBasketStatus(CMSV12Basket cmsV12Basket, String setStatus)
			throws Exception {
		try {
			this.fireWorkInProgressEvent(true);
			boolean status = CMSV12BasketServices.getCurrent().setBasketStatus(
					cmsV12Basket, setStatus);
			return status;
		} finally {
			this.fireWorkInProgressEvent(false);
		}
	}
}
