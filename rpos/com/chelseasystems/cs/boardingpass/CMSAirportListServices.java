/*
* CMSAirportListServices.java
* 09/20/2013
* @copyright (c) 2013 SkillnetInc.
* Created by Shushma Priya
*/

package com.chelseasystems.cs.boardingpass;

import com.chelseasystems.cr.config.ConfigMgr;
import com.chelseasystems.cs.boardingpass.CMSAirportDetails;
import com.chelseasystems.cs.boardingpass.CMSAirportListServices;
import com.chelseasystems.cs.pos.CMSCompositePOSTransaction;
/*
 * This class is for mapping the duty free shop related data from database to client 
 */
public abstract class CMSAirportListServices {
	private static CMSAirportListServices current; 

	public static CMSAirportListServices getCurrent()
    {
      return current;
    }

    public static void setCurrent(CMSAirportListServices aService)
    {
        System.out.println("Setting current implementation of CMSAirportListServices.");
        current = aService;
    }
    
	public abstract CMSAirportDetails[] getAirportDetails() throws Exception;
	public abstract boolean submit(CMSAirportDetails airportDetails) throws Exception ;

}
