/*
* CMSAirportListRMIServerImpl.java
* 09/20/2013
* @copyright (c) 2013 SkillnetInc.
* Created by Shushma Priya
*/

package com.chelseasystems.cs.boardingpass;

import java.rmi.ConnectException;
import java.rmi.RemoteException;
import java.util.Properties;

import com.chelseasystems.cr.logging.LoggingServices;
import com.chelseasystems.cr.node.CMSComponent;
import com.chelseasystems.cs.boardingpass.CMSAirportDetails;
import com.chelseasystems.cs.boardingpass.CMSAirportListServices;
import com.chelseasystems.cs.boardingpass.ICMSAirportListRMIServer;
import com.chelseasystems.cs.customer.CMSCustomer;
import com.chelseasystems.cs.customer.CMSCustomerServices;
import com.chelseasystems.cs.pos.CMSCompositePOSTransaction;

/*
 * This class is for invoking the RMI mapping from server to client 
 */
public class CMSAirportListRMIServerImpl extends CMSComponent implements ICMSAirportListRMIServer{

	public CMSAirportListRMIServerImpl(Properties props)
			throws RemoteException {
		 super(props);
		    setImpl();
		    init();
		// TODO Auto-generated constructor stub
	}

	public boolean ping() throws RemoteException {
		// TODO Auto-generated method stub
		return true;
	}

	public CMSAirportDetails[] getAirportDetails() throws RemoteException {
		// TODO Auto-generated method stub
		long start = getStartTime();
	    try {
	      if (!isAvailable()) {
	        throw new ConnectException("Service is not available");
	      }
	      incConnection();
	      return (CMSAirportDetails[])((CMSAirportListServices)CMSAirportListServices.getCurrent()).
	      getAirportDetails();
	    } catch (Exception e) {
	      throw new RemoteException(e.getMessage(), e);
	    } finally {
	      addPerformance("getAirportDetails", start);
	      decConnection();
	    }
	}

	@Override
	protected void configEvent(String[] arg0) {
		// TODO Auto-generated method stub
		
	}
	 private void setImpl() {
		    Object obj = getConfigManager().getObject("SERVER_IMPL");
		    if (null == obj) {
		      LoggingServices.getCurrent().logMsg(getClass().getName(), "setImpl()"
		          , "Could not instantiate SERVER_IMPL.", "Make sure airport.cfg contains SERVER_IMPL"
		          , LoggingServices.MAJOR);
		    }
		    CMSAirportListServices.setCurrent((CMSAirportListServices)obj);
		  }
	 
	 private void init() {
		    System.out.println("Binding to RMIRegistry...");
		    String theName = getConfigManager().getString("REMOTE_NAME");
		    if (null != theName) {
		      bind(theName, this);
		    } else {
		      LoggingServices.getCurrent().logMsg(getClass().getName(), "init()"
		          , "Could not find name to bind to in registry."
		          , "Make sure airport.cfg contains a REMOTE_NAME entry.", LoggingServices.MAJOR);
		    }
		  }

	public boolean submit(CMSAirportDetails airportDetails)
			throws RemoteException {
		long start = getStartTime();
	    try {
	      if (!isAvailable()) {
	        throw  new ConnectException("Service is not available");
	      }
	      incConnection();
	      return  (CMSAirportListServices.getCurrent()).submit(airportDetails);
	    } catch (Exception e) {
	      throw  new RemoteException(e.getMessage(), e);
	    } finally {
	      addPerformance("submit", start);
	      decConnection();
	    }
	}

}
