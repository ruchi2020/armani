package com.chelseasystems.cs.v12basket;

import java.util.Date;

import com.chelseasystems.cr.config.ConfigMgr;
import com.chelseasystems.cr.logging.LoggingServices;
import com.chelseasystems.cs.dataaccess.artsoracle.dao.ArmStgVirtualSaleDAO;

public class CMSV12BasketJDBCServices extends CMSV12BasketServices {

	private ArmStgVirtualSaleDAO v12BasketDAO;

	/**
	 * Default Constructor
	 */
	public CMSV12BasketJDBCServices() {
		ConfigMgr configMgr = new ConfigMgr("jdbc.cfg");
		v12BasketDAO = (ArmStgVirtualSaleDAO) configMgr
				.getObject("V12_BASKET_DAO");
	}

	@Override
	public CMSV12Basket[] getBasketDetails(Date date, String storeId)
			throws Exception {
		try {
			return v12BasketDAO.getBasketDetails(date, storeId);
		} catch (Exception exception) {
			LoggingServices.getCurrent().logMsg(this.getClass().getName(),
					"getBasketDetails", "Exception", "See Exception",
					LoggingServices.MAJOR, exception);
			throw exception;
		}
	}

	public boolean setBasketStatus(CMSV12Basket cmsV12Basket, String setStatus)
			throws Exception {
		try {
			return v12BasketDAO.setBasketStatus(cmsV12Basket, setStatus);
		} catch (Exception exception) {
			LoggingServices.getCurrent().logMsg(this.getClass().getName(),
					"setBasketStatus", "Exception", "See Exception",
					LoggingServices.MAJOR, exception);
			throw exception;
		}

	}

}
