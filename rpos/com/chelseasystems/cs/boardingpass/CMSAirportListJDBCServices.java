/*
* CMSAirportListJDBCServices.java
* 09/20/2013
* @copyright (c) 2013 SkillnetInc.
* Created by Shushma Priya
*/

package com.chelseasystems.cs.boardingpass;

import com.chelseasystems.cr.config.ConfigMgr;
import com.chelseasystems.cr.employee.Employee;
import com.chelseasystems.cr.logging.LoggingServices;
import com.chelseasystems.cs.boardingpass.CMSAirportDetails;
import com.chelseasystems.cs.boardingpass.CMSAirportListServices;
import com.chelseasystems.cs.dataaccess.ArmAirportListDAO;
import com.chelseasystems.cs.pos.CMSCompositePOSTransaction;

public class CMSAirportListJDBCServices extends CMSAirportListServices{
	
	/*
	 * This class is for mapping the duty free shop related data from database to client 
	 */
	
	private ArmAirportListDAO airportListDAO;

	public CMSAirportListJDBCServices() {
	    ConfigMgr configMgr = new ConfigMgr("jdbc.cfg");
	    airportListDAO = (ArmAirportListDAO)configMgr.getObject("AIRPORT_DAO");
	  }
	
	public CMSAirportDetails[] getAirportDetails() throws Exception {
		// TODO Auto-generated method stub
		try {
		      return airportListDAO.getAirportDetails();
		    } catch (Exception exception) {
		      LoggingServices.getCurrent().logMsg(this.getClass().getName(), "getAirportDetails", "Exception"
		          , "See Exception", LoggingServices.MAJOR, exception);
		      throw exception;
		    }
	}
	
	public boolean submit(CMSAirportDetails airportDetails) throws Exception {
		try {
       	airportListDAO.insert(airportDetails);
       return true;
		}
  catch (Exception exception) {
    LoggingServices.getCurrent().logMsg(this.getClass().getName(), "submit", "Exception"
        , "See Exception", LoggingServices.MAJOR, exception);
    return false;
  }
		
}

}
