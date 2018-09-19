/*
* CMSAirportListRMIClient.java
* 09/20/2013
* @copyright (c) 2013 SkillnetInc.
* Created by Shushma Priya
*/
package com.chelseasystems.cs.boardingpass;

import java.rmi.ConnectException;
import java.rmi.RMISecurityManager;

import com.chelseasystems.cr.appmgr.DowntimeException;
import com.chelseasystems.cr.config.ConfigMgr;
import com.chelseasystems.cr.config.NetworkMgr;
import com.chelseasystems.cr.logging.LoggingServices;
import com.chelseasystems.cr.node.ICMSComponent;
import com.chelseasystems.cr.node.IRemoteServerClient;
import com.chelseasystems.cs.boardingpass.CMSAirportDetails;
import com.chelseasystems.cs.boardingpass.CMSAirportListServices;
import com.chelseasystems.cs.boardingpass.ICMSAirportListRMIServer;
import com.chelseasystems.cs.pos.CMSCompositePOSTransaction;
import com.igray.naming.NamingService;

/*
 * This class is for invoking the RMI mapping from server to client 
 */
public class CMSAirportListRMIClient extends CMSAirportListServices implements
IRemoteServerClient{

	private ConfigMgr config = null;
	private ICMSAirportListRMIServer cmsAirportListServer = null;
	
	private int maxTries = 1;
	
	public CMSAirportListRMIClient()
    throws DowntimeException {
  config = new ConfigMgr("airport.cfg");
  if (System.getSecurityManager() == null) {
    System.setSecurityManager(new RMISecurityManager());
  }
  init();
}
	
	private void init()
    throws DowntimeException {
  try {
    this.lookup();
  } catch (Exception e) {
    LoggingServices.getCurrent().logMsg(getClass().getName(), "init()"
        , "Cannot establish connection to RMI server."
        , "Make sure that the server is registered on the remote server"
        + " and that the name of the remote server and remote service are"
        + " correct in the airport.cfg file.", LoggingServices.MAJOR, e);
    throw new DowntimeException(e.getMessage());
  }
}
	
	
	public CMSAirportDetails[] getAirportDetails() throws Exception {
		// TODO Auto-generated method stub
		for (int x = 0; x < maxTries; x++) {
		      if (cmsAirportListServer == null) {
		        init();
		      }
		      try {
		        return cmsAirportListServer.getAirportDetails();
		      } catch (ConnectException ce) {
		    	  cmsAirportListServer = null;
		      } catch (Exception ex) {
		        throw new DowntimeException(ex.getMessage());
		      }
	}
		 throw new DowntimeException("Unable to establish connection to CMSAirportListServices");
	}

	public boolean isRemoteServerAvailable() {
		// TODO Auto-generated method stub
		try {
		      return ((ICMSComponent)this.cmsAirportListServer).isAvailable();
		    } catch (Exception ex) {
		      return false;
		    }
	}

	public void lookup() throws Exception {
		// TODO Auto-generated method stub
		NetworkMgr mgr = new NetworkMgr("network.cfg");
	    maxTries = mgr.getRetryAttempts();
	    String connect = mgr.getRMIMasterNode() + config.getString("REMOTE_NAME") + mgr.getQuery();
	    cmsAirportListServer = (ICMSAirportListRMIServer)NamingService.lookup(connect);
	}

	@Override
	public boolean submit(CMSAirportDetails airportDetails) throws Exception {
		// TODO Auto-generated method stub
		 for (int x = 0; x < maxTries; x++) {
		      if (cmsAirportListServer == null) {
		        init();
		      }
		      try {
		        return  cmsAirportListServer.submit(airportDetails);
		      } catch (ConnectException ce) {
		    	  cmsAirportListServer = null;
		      } catch (Exception ex) {
		        throw  new DowntimeException(ex.getMessage());
		      }
		    }
		    throw  new DowntimeException("Unable to establish connection to cmsAirportListServices");
		    }

	}
