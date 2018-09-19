/*
 * CMSBoardingPassBldr.java
 * created by shushma for Duty free management PCR
 * for the purpose of scanning the boarding pass details via a MSR reader
 */

package com.chelseasystems.cs.swing.builder;

import java.util.Calendar;
import java.util.Date;

import com.chelseasystems.cr.appmgr.IApplicationManager;
import com.chelseasystems.cr.appmgr.IObjectBuilder;
import com.chelseasystems.cr.appmgr.IObjectBuilderManager;
import com.chelseasystems.cr.currency.ArmCurrency;
import com.chelseasystems.cr.payment.Amex;
import com.chelseasystems.cr.payment.CreditCard;
import com.chelseasystems.cr.pos.PaymentTransaction;
import com.chelseasystems.cr.swing.CMSApplet;
import com.chelseasystems.cs.boardingpass.CMSAirportDetails;
import com.chelseasystems.cs.msr.CMSMSR;
import com.chelseasystems.cs.msr.NonJavaPOSMSR;
import com.chelseasystems.cs.payment.CMSRedeemable;
import com.chelseasystems.cs.payment.CMSStoreValueCard;
import com.chelseasystems.cs.pos.CMSCompositePOSTransaction;
import com.chelseasystems.cs.register.CMSRegister;
import com.chelseasystems.cs.store.CMSStore;
import com.chelseasystems.cs.util.CreditAuthUtil;
import com.chelseasystems.cs.util.IsDigit;
import com.chelseasystems.cs.util.TransactionUtil;

public class CMSBoardingPassBldr implements IObjectBuilder{
	
	private IObjectBuilderManager theBldrMgr;
	private IApplicationManager theAppMgr;
	private CMSMSR cmsMSR = null;
	private CMSApplet applet;
	private CMSAirportDetails airportDetails;
	private boolean invokeBuilder;
	private boolean isOffline;
	private String accountNum = null;
	private String flightNo;
	private String checkIn;
	private String comp;
	private String seatNo;
	private CMSCompositePOSTransaction aTxn;
	private boolean zipTraversed;
	private boolean manual;
	private String destination;
	
	public CMSBoardingPassBldr() {
	  }

	public void EditAreaEvent(String theCommand, Object theEvent) {
		// TODO Auto-generated method stub
		if (theCommand.equals("BOARDING_PASS")) {
		      processSwipe((String)theEvent);
		      return;
			}
	}
	
	public void processSwipe(String input) {
		 if ((input == null || input.trim().length() == 0) ||(getBoardingPassInfo(input)  && processBoardingPassDetails(input))) {
			      completeAttributes();
			      return;
			    }
		  theAppMgr.setSingleEditArea(applet.res.getString("Scan or Enter Manually Boarding Pass Details and press 'Enter'"), "BOARDING_PASS");
	    theAppMgr.setEditAreaFocus();
	    invokeBuilder = false;
	}

	private boolean processBoardingPassDetails(String inputStr) {
		if(!(inputStr == null || inputStr.trim().length() == 0)){
		try{
		//Vivek Mishra : Merged updated code from source provided by Sergio 19-MAY-16
			 /*if(((inputStr.substring(0, 15)).trim()!=null)||((inputStr.substring(0, 15)).trim().length())!= 0)
			 airportDetails.setDestination((inputStr.substring(0, 15)).trim());
			 if(((inputStr.substring(15, 25)).trim()!=null)||((inputStr.substring(15, 25)).trim().length())!= 0)
				airportDetails.setFlightNo(inputStr.substring(15,25));
			 if(((inputStr.substring(25,35)).trim()!=null)||((inputStr.substring(25,35)).trim().length())!= 0)
				airportDetails.setCheckIn(inputStr.substring(25,35));
			 if(((inputStr.substring(35,37)).trim()!=null)||((inputStr.substring(35,37)).trim().length())!= 0)
				airportDetails.setComp(inputStr.substring(35,37));
			 if(((inputStr.substring(35,inputStr.length())).trim()!=null)||((inputStr.substring(35,inputStr.length())).trim().length())!= 0)
				airportDetails.setSeatNo(inputStr.substring(37,inputStr.length()));*/
				if(((inputStr.substring(33,36)).trim()!=null)||((inputStr.substring(33,36)).trim().length())!= 0)
			 airportDetails.setDestination((inputStr.substring(33,36)).trim());
			 if(((inputStr.substring(38,43)).trim()!=null)||((inputStr.substring(38,43)).trim().length())!= 0)
				airportDetails.setFlightNo(inputStr.substring(38,43));
			 if(((inputStr.substring(52,57)).trim()!=null)||((inputStr.substring(52,57)).trim().length())!= 0)
				airportDetails.setCheckIn(inputStr.substring(52,57));
			 if(((inputStr.substring(36,38)).trim()!=null)||((inputStr.substring(36,38)).trim().length())!= 0)
				airportDetails.setComp(inputStr.substring(36,38));
			 if(((inputStr.substring(48,52)).trim()!=null)||((inputStr.substring(48,52)).trim().length())!= 0)
				airportDetails.setSeatNo(inputStr.substring(48,52));
			 
			 // RICCARDO MODIFICA
			 String checkin =  airportDetails.getCheckIn();
			 
			 if (checkin.equals("DIPEN"))
			 {
				 airportDetails.setCheckIn("DIPENDENTE");
				 airportDetails.setFlightNo("ADR0001");
				 airportDetails.setSeatNo("ZZZ");
				 airportDetails.setEntryType("M");
			 } 
			 if (checkin.equals("CREWX"))
			 {
				 airportDetails.setCheckIn("CREW"); 
				 airportDetails.setFlightNo("ADR0001");
				 airportDetails.setSeatNo("ZZZ");
				 airportDetails.setEntryType("M");
			 } 
		
		}
			 catch(Exception e){}
			   return true;
		}
		else
			  return false;
		}

	private boolean completeAttributes() {
		 if (invokeBuilder) {
			 theAppMgr.setSingleEditArea(applet.res.getString("Scan or Enter Manually Boarding Pass Details and press 'Enter'"), "BOARDING_PASS");
			 invokeBuilder = false;
	          return false;
	        }
		 if((airportDetails.getDestination()==null||airportDetails.getDestination().length()==0)&&
			 (airportDetails.getFlightNo()==null||airportDetails.getFlightNo().length()==0)&&
			 (airportDetails.getCheckIn()==null||airportDetails.getCheckIn().length()==0)&&
			 (airportDetails.getComp()==null||airportDetails.getComp().length()==0)&&
			 (airportDetails.getSeatNo()==null||airportDetails.getSeatNo().length()==0)){
			 theAppMgr.setSingleEditArea(applet.res.getString("Scan or Enter Manually Boarding Pass Details and press 'Enter'"), "BOARDING_PASS");
			 return false;
		 }
	      theBldrMgr.processObject(applet, "BOARDING_PASS", airportDetails, this);
	     return (true);
	}

	public boolean getBoardingPassInfo(String inputStr) {
		// TODO Auto-generated method stub
		if (this.cmsMSR instanceof NonJavaPOSMSR)
		      if (!(((NonJavaPOSMSR)cmsMSR).extractDataToBuilder(inputStr))) {
		        theAppMgr.showErrorDlg("Failure reading Boarding Pass data.");
		        return (false);
		      }
		   return (true);
		}

	public void build(String Command, CMSApplet applet, Object initValue) {
		    this.applet = applet;
		    airportDetails = new CMSAirportDetails();
		    zipTraversed = false;
		    theAppMgr.setSingleEditArea(applet.res.getString("Scan or Enter Manually Boarding Pass Details and press 'Enter'"), "BOARDING_PASS");
		    theAppMgr.setEditAreaFocus();
		    System.out.println("Boarding Pass builder getting instance of CMSMSR...");
		    CMSMSR cmsMSR = null;
		    if(cmsMSR != null){
		    cmsMSR = CMSMSR.getInstance();
		    cmsMSR.registerCreditCardBuilder(this);
		    cmsMSR.activate();
		    }
		    }

	public void cleanup() {
		// TODO Auto-generated method stub
		if(cmsMSR != null)
		    cmsMSR.release();
	}

	public void init(IObjectBuilderManager theBldrMgr, IApplicationManager theAppMgr) {
		// TODO Auto-generated method stub
		this.theBldrMgr = theBldrMgr;
	    this.theAppMgr = theAppMgr;
	}
	
	public void setCMSMSR(CMSMSR cmsMSR) {
		// TODO Auto-generated method stub
		 this.cmsMSR = cmsMSR;
	}

	public void setAccountNum(String accountNum) {
	    this.accountNum  = new IsDigit().filterToGetDigits(accountNum);
	  }
	
}
