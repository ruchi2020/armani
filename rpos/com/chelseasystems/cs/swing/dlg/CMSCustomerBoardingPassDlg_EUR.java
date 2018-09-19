package com.chelseasystems.cs.swing.dlg;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Frame;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.rmi.Remote;
import java.text.SimpleDateFormat;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JPanel;

import jpos.JposConst;

import com.chelseasystems.cr.appmgr.IApplicationManager;
import com.chelseasystems.cr.config.ConfigMgr;
import com.chelseasystems.cr.employee.IEmployeeRMIPeer;
import com.chelseasystems.cr.swing.CMSApplet;
import com.chelseasystems.cs.boardingpass.CMSAirportDetails;
import com.chelseasystems.cs.boardingpass.CMSAirportListHelper;
import com.chelseasystems.cs.pos.CMSCompositePOSTransaction;
import com.chelseasystems.cs.pos.TransactionSearchString;
import com.chelseasystems.cs.store.CMSStore;
import com.chelseasystems.cs.swing.panel.CMSCustomerBoardingPassPanel_EUR;
import com.chelseasystems.cs.swing.panel.FiscalTxnLookupPanel;
import com.chelseasystems.cs.swing.pos.IdentifyConsultantApplet;

public class CMSCustomerBoardingPassDlg_EUR extends JDialog implements ActionListener, JposConst{
	  private JButton btnOk;
	  private JButton btnCancel;
	  private JPanel pnlButtons;
	  private CMSCustomerBoardingPassPanel_EUR pnlLookup;
	  private IApplicationManager theAppMgr;
	 // private TransactionSearchString txnSrchStr;
	 // private SimpleDateFormat dateFormat;
	  private CMSStore theStore;
	  private boolean ok;
	  private CMSAirportDetails airportDetails;
	  private String destinations;
	  private String flightNo;
	  private String checkIn;
	  private String comp;
	  private String seatNo;
	  private String entry;
	  public boolean flag = false;
	  
	  public CMSCustomerBoardingPassDlg_EUR(IApplicationManager theAppMgr, CMSAirportDetails airportDetails) {
		    super(theAppMgr.getParentFrame(), "Boarding Pass Information", true);
		    this.theAppMgr = theAppMgr;
		    theStore = ((CMSStore)theAppMgr.getGlobalObject("STORE"));
		    this.airportDetails = airportDetails;
		    init();
		    pnlLookup.setAppMgr(theAppMgr);
		   }
	private void init() {
		// TODO Auto-generated method stub
	    pnlLookup = new CMSCustomerBoardingPassPanel_EUR(airportDetails);
	    pnlButtons = new JPanel(new BorderLayout());
	    btnOk = theAppMgr.getTheme().getDefaultBtn();
	    btnOk.setText("Ok");
	    btnOk.setMnemonic('O');
	    btnOk.addActionListener(this);
	    pnlButtons.add(btnOk, BorderLayout.WEST);
	    btnCancel = theAppMgr.getTheme().getDefaultBtn();
	    btnCancel.setText("Cancel");
	    btnCancel.setMnemonic('C');
	    btnCancel.addActionListener(this);
	    pnlButtons.add(btnCancel, BorderLayout.EAST);
	    pnlButtons.setBackground(theAppMgr.getBackgroundColor());
	    this.getContentPane().setLayout(new BorderLayout());
	    this.getContentPane().add(pnlLookup, BorderLayout.CENTER);
	    this.getContentPane().add(pnlButtons, BorderLayout.SOUTH);
	    this.setResizable(false);
	    setSize(400, 250);
	}
	
	public void setVisible(boolean visible) {
	    if (visible) {
	      Dimension d = Toolkit.getDefaultToolkit().getScreenSize();
	      setLocation((d.width - getSize().width) / 2, (d.height - getSize().height) / 2);
	    }
	    super.setVisible(visible);
	  }
	public void actionPerformed(ActionEvent ae) {
		// TODO Auto-generated method stub
		airportDetails = new CMSAirportDetails();
		String sCommand = ae.getActionCommand();
		//added by shushma for DUTY FREE MANAGEMENT PCR
		try{
	if (sCommand.equals("Ok")) {
		destinations = pnlLookup.getDestination();
		flightNo = pnlLookup.getFlightNumber();
		checkIn = pnlLookup.getCheckInNumber();
		comp = pnlLookup.getCompNumber();
		seatNo = pnlLookup.getSeatNumber();
		entry = pnlLookup.getEntry();
		if(!checkLength()){
		      if(!completeAttributes())
		      {	
		    	   theAppMgr.showErrorDlg("Please enter all the information." +
			        		"\n -Flight No.: 01 – 10 characters only" +
			        		"\n -Check-In No.: 01 – 10 characters only" +
			        		"\n -Comp.:  01 – 02 characters only" +
			        		"\n -Seat No.: 01 – 05 characters only");
		    	   return;
		      }
		      else{
		    	  airportDetails.setDestination(destinations);
		    	  airportDetails.setFlightNo(flightNo);
		    	  airportDetails.setCheckIn(checkIn);
		    	  airportDetails.setComp(comp);
		    	  airportDetails.setSeatNo(seatNo);
		    	  airportDetails.setEntryType(entry);
		    	  setAirportDetails(airportDetails);
		      }
		    } 
		else
		return;
	}
		else if (sCommand.equals("Cancel")) {
			flag = true;
		    	}
		    }
		catch(Exception e){
			e.printStackTrace();
		}
		dispose();
		return;
}  
	
		private boolean completeAttributes()
	  {
	    if((flightNo.length() > 0) && (checkIn.length() > 0) && (comp.length() > 0) && (seatNo.length() > 0))
	    	return true;
	        return false;
	  }
	private boolean checkLength()
	  {
	    if(flightNo.length() > 10){
	    	theAppMgr.showErrorDlg("The maximum Length of Flight No. must be 10");
	    	return true;
	    }
	    else if(checkIn.length() > 10){
	    	theAppMgr.showErrorDlg("The maximum Length of Check in No. must be 10");
	    	return true;
	    }
	    else if(seatNo.length() > 5){
	    	theAppMgr.showErrorDlg("The maximum Length of Seat No. must be 5");
	    	return true;
	    }
	    else if (comp.length() > 2){
	    	theAppMgr.showErrorDlg("The maximum Length of Comp must be 2");
	    	return true;
	    }
	    //Added by SonaliRaina to produce error dialog on not changing destination field
	    else if(destinations.equalsIgnoreCase("Not-Coded")){
	    	theAppMgr.showErrorDlg("Please Select Airport Destination");
	    	return true;
	    }
	    return false;
	  }
	
	//added  by shushma for duty free management PCR
	 public CMSAirportDetails getAirportDetails() {
			return airportDetails;
		}

		public void setAirportDetails(CMSAirportDetails airportDetails) {
			this.airportDetails = airportDetails;
		}
		
		/**
	   * put your documentation comment here
	   * @return
	   */
	  }

