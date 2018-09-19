package com.chelseasystems.cs.rules.airport;

import com.chelseasystems.cr.appmgr.menu.CMSMenuOption;
import com.chelseasystems.cr.employee.Employee;
import com.chelseasystems.cr.logging.LoggingServices;
import com.chelseasystems.cr.rules.Rule;
import com.chelseasystems.cr.rules.RulesInfo;
import com.chelseasystems.cr.store.Store;
import com.chelseasystems.cr.swing.CMSApplet;

public class HideIfBoardingPass extends Rule{

	@Override
	public RulesInfo execute(Object theParent, Object[] args) {
		// TODO Auto-generated method stub
		return execute((CMSMenuOption)theParent, (Employee)args[0], (Store)args[1]);
	}

	private RulesInfo execute(CMSMenuOption theParent, Employee employee,
			Store store) {
		// TODO Auto-generated method stub
		try {	    	   	
	    	if(CMSApplet.theAppMgr.getStateObject("ARM_BOARDING_PASS")!=null 
	    			&& CMSApplet.theAppMgr.getStateObject("ARM_BOARDING_PASS").equals("TRUE"))	    	
	    		return new RulesInfo("Hide as BoardingPassEntered");
	    } catch (Exception ex) {
	      LoggingServices.getCurrent().logMsg(getClass().getName(), "execute"
	          , "Rule Failed, see exception.", "N/A", LoggingServices.MAJOR, ex);
	    }
	    return new RulesInfo();
	  }
	

	@Override
	public String getDesc() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getName() {
		// TODO Auto-generated method stub
		return null;
	}

}
