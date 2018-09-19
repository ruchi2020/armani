package com.chelseasystems.cs.dataaccess;

import java.sql.SQLException;

import com.chelseasystems.cr.database.ParametricStatement;
import com.chelseasystems.cs.boardingpass.CMSAirportDetails;
import com.chelseasystems.cs.currency.CurrencyRate;

public interface ArmAirportListDAO extends BaseDAO {

	public CMSAirportDetails[] getAirportDetails () throws SQLException;

	public void insert(CMSAirportDetails airportDetails)throws SQLException;
	
	public ParametricStatement[] getInsertSQL(CMSAirportDetails airportDetails);

}
