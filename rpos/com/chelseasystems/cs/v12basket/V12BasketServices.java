package com.chelseasystems.cs.v12basket;

import java.util.Date;

public abstract class V12BasketServices {
	public static void setCurrent(V12BasketServices svcs) {
		current = svcs;
	}

	public static V12BasketServices getCurrent() {
		return current;
	}

	private static V12BasketServices current;

	public abstract CMSV12Basket[] getBasketDetails(Date date, String storeId)
			throws Exception;

	public abstract boolean setBasketStatus(CMSV12Basket cmsV12Basket,
			String setStatus) throws Exception;
}
