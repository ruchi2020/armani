/*
* CMSAirportListClientServices.java
* 09/20/2013
* @copyright (c) 2013 SkillnetInc.
* Created by Shushma Priya
*/

package com.chelseasystems.cs.boardingpass;

import com.chelseasystems.cr.appmgr.ClientServices;
import com.chelseasystems.cr.appmgr.DowntimeException;
import com.chelseasystems.cr.config.ConfigMgr;
import com.chelseasystems.cr.logging.LoggingServices;
import com.chelseasystems.cr.swing.event.IWorkInProgressListener;
import com.chelseasystems.cs.boardingpass.CMSAirportDetails;
import com.chelseasystems.cs.boardingpass.CMSAirportListServices;
import com.chelseasystems.cs.customer.CMSCustomer;
import com.chelseasystems.cs.customer.CMSCustomerServices;
import com.chelseasystems.cs.pos.CMSCompositePOSTransaction;

public class CMSAirportListClientServices extends ClientServices{
	
	/*
	 * This class is for mapping the duty free shop related data from database to client 
	 */

	public CMSAirportListClientServices() {
	    // Set up the configuration manager.
	    config = new ConfigMgr("airport.cfg");
	  }
	public CMSAirportDetails[] getAirportDetails() throws Exception{
		// TODO Auto-generated method stub
		 try {
		      this.fireWorkInProgressEvent(true);
		      return (CMSAirportDetails[])(CMSAirportListServices.getCurrent()).
		      getAirportDetails();
		    } 
		 catch (DowntimeException ex) {
		      LoggingServices.getCurrent().logMsg(getClass().getName(), "getAirportDetails"
		          , "Primary Implementation for CMSCustomerServices failed, going Off-Line..."
		          , "See Exception", LoggingServices.MAJOR, ex);
		      offLineMode();
		      setOffLineMode();
		      return (CMSAirportDetails[])((CMSAirportListServices)CMSAirportListServices.getCurrent()).
		      getAirportDetails();
		    } 
		 catch (Exception e) {
				e.printStackTrace();
				return null;
			}finally {
				this.fireWorkInProgressEvent(false);
			}
	}
	
	 public void offLineMode() {
		    LoggingServices.getCurrent().logMsg("Off-Line Mode for CMSAirportListClientServices");
		    String CLIENT_LOCAL_IMPL = config.getString("");
		    CMSAirportListServices serviceImpl = (CMSAirportListServices)config.getObject(
		        "CLIENT_LOCAL_IMPL");
		    if (null == serviceImpl) {
		      LoggingServices.getCurrent().logMsg("CMSAirportListClientServices", "offLineMode()"
		          , "Cannot instantiate the class that provides the"
		          + " implementation of CMSAirportListClientServices in airport.cfg."
		          , "Make sure that airport.cfg contains an entry with "
		          + "a key of CLIENT_DOWNTIME and a value"
		          + " that is the name of a class that provides a concrete"
		          + " implementation of CMSAirportListServices.", LoggingServices.CRITICAL);
		    }
		    CMSAirportListServices.setCurrent(serviceImpl);
		  }

	@Override
	public void init(boolean online) throws Exception {
		// TODO Auto-generated method stub
		 // Set up the proper implementation of the service.
	    if (online) {
	      onLineMode();
	    } else {
	      offLineMode();
	    }
	}

	@Override
	public void onLineMode() {
		// TODO Auto-generated method stub
		LoggingServices.getCurrent().logMsg("On-Line Mode for CMSAirportListClientServices");
		String CLIENT_IMPL = config.getObject("CLIENT_IMPL").toString();
		CMSAirportListServices serviceImpl = (CMSAirportListServices)config.getObject("CLIENT_IMPL");
	    if (null == serviceImpl) {
	      LoggingServices.getCurrent().logMsg("CMSAirportListClientServices", "onLineMode()"
	          , "Cannot instantiate the class that provides the"
	          + "implementation of CMSAirportListServices in airport.cfg."
	          , "Make sure that exchangerate.cfg contains an entry with "
	          + "a key of CLIENT_IMPL and a value that is the name of a class "
	          + "that provides a concrete implementation of CMSAirportListServices."
	          , LoggingServices.MAJOR);
	      setOffLineMode();
	      return;
	    }
	    CMSAirportListServices.setCurrent(serviceImpl);
	}
	protected Class getOnlineService () throws ClassNotFoundException {
	    String className = config.getString("CLIENT_IMPL");
	    Class serviceClass = Class.forName(className);
	    return  serviceClass;
	  }
	
	public Object getCurrentService() {
	    return CMSAirportListServices.getCurrent();
	  }
	public boolean submit(CMSAirportDetails airportDetails) throws Exception{
		// TODO Auto-generated method stub
		try {
		      this.fireWorkInProgressEvent(true);
		      return (CMSAirportListServices.getCurrent()).submit(
		    		  airportDetails);
		    } catch (Exception ex) {
		      LoggingServices.getCurrent().logMsg(getClass().getName(), "submit"
		          , "Primary Implementation for CMSAirportListServices failed, going Off-Line..."
		          , "See Exception", LoggingServices.MAJOR, ex);
		      offLineMode();
		      setOffLineMode();
		      return CMSAirportListServices.getCurrent().submit(
		    		  airportDetails);
		    } finally {
		      this.fireWorkInProgressEvent(false);
		    }
	}
	
}
