package com.chelseasystems.cs.v12basket;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.Date;

import com.igray.naming.IPing;

public interface ICMSV12BasketRMIServer extends Remote, IPing {

	CMSV12Basket[] getBasketDetails(Date date, String storeId)
			throws RemoteException;

	public boolean setBasketStatus(CMSV12Basket cmsV12Basket, String setStatus)
			throws RemoteException;

}
