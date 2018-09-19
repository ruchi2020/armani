/*
* CMSAirportListHelper.java
* 09/20/2013
* @copyright (c) 2013 SkillnetInc.
* Created by Shushma Priya
*/


package com.chelseasystems.cs.boardingpass;

import com.chelseasystems.cr.appmgr.*;
import com.chelseasystems.cs.boardingpass.CMSAirportDetails;
import com.chelseasystems.cs.boardingpass.CMSAirportListClientServices;
/*
 * This class is for mapping the duty free shop related data from database to client 
 */

public class CMSAirportListHelper {

public CMSAirportDetails[] getAirportDetails(IRepositoryManager theAppMgr) throws Exception {
		CMSAirportListClientServices cs = (CMSAirportListClientServices)theAppMgr.getGlobalObject(
        "AIRPORT_SRVC");
    return cs.getAirportDetails();
  }

public static boolean submit(IApplicationManager theAppMgr,
		CMSAirportDetails airportDetails) throws Exception {
	// TODO Auto-generated method stub
	CMSAirportListClientServices cs = (CMSAirportListClientServices)theAppMgr.getGlobalObject(
     "AIRPORT_SRVC");
 return cs.submit(airportDetails);
}

}



