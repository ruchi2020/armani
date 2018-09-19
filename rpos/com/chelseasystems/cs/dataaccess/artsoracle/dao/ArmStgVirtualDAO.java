package com.chelseasystems.cs.dataaccess.artsoracle.dao;

import java.sql.SQLException;
import java.util.Date;

import com.chelseasystems.cs.v12basket.CMSV12Basket;

public interface ArmStgVirtualDAO {
	public CMSV12Basket[] getBasketDetails(Date date, String storeId)
			throws SQLException;

	public boolean setBasketStatus(CMSV12Basket cmsV12Basket, String setStatus)
			throws SQLException;
}
