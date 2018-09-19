package com.chelseasystems.cs.v12basket;

import java.util.Date;

public abstract class CMSV12BasketServices extends V12BasketServices {

	public abstract CMSV12Basket[] getBasketDetails(Date date, String storeId)
			throws Exception;

	public abstract boolean setBasketStatus(CMSV12Basket cmsV12Basket,
			String setStatus) throws Exception;

}
