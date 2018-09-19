package com.chelseasystems.cs.v12basket;

import java.util.Date;

import com.chelseasystems.cr.appmgr.IRepositoryManager;

public class CMSV12BasketHelper {
	public static CMSV12Basket[] getBasketDetails(IRepositoryManager theMgr,
			Date date, String storeId) throws Exception {
		CMSV12BasketClientServices cs = (CMSV12BasketClientServices) theMgr
				.getGlobalObject("V12_BASKET_SRVC");
		return (CMSV12Basket[]) cs.getBasketDetails(date, storeId);
	}

	public static boolean setBasketStatus(IRepositoryManager theMgr,
			CMSV12Basket cmsV12Basket, String setStatus) throws Exception {
		CMSV12BasketClientServices cs = (CMSV12BasketClientServices) theMgr
				.getGlobalObject("V12_BASKET_SRVC");
		return (boolean) cs.setBasketStatus(cmsV12Basket, setStatus);
	}
}
