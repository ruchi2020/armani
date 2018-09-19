/*
* ICMSAirportListRMIServer.java
* 09/20/2013
* @copyright (c) 2013 SkillnetInc.
* Created by Shushma Priya
*/

package com.chelseasystems.cs.boardingpass;

import java.rmi.Remote;
import java.rmi.RemoteException;

import com.chelseasystems.cs.boardingpass.CMSAirportDetails;
import com.chelseasystems.cs.pos.CMSCompositePOSTransaction;
import com.igray.naming.IPing;
/*
 * This interface is for invoking the RMI mapping from server to client 
 */
public interface ICMSAirportListRMIServer extends Remote, IPing {

	public CMSAirportDetails[] getAirportDetails()throws RemoteException;

	public boolean submit(CMSAirportDetails airportDetails)throws RemoteException;

}
