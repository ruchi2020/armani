package com.chelseasystems.cs.swing.panel;

import java.awt.Container;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.KeyEvent;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.StringTokenizer;
import java.util.Vector;

import javax.swing.JPanel;

import com.chelseasystems.cr.appmgr.IApplicationManager;
import com.chelseasystems.cr.config.ConfigMgr;
import com.chelseasystems.cr.config.FileMgr;
import com.chelseasystems.cr.swing.bean.JCMSComboBox;
import com.chelseasystems.cr.swing.bean.JCMSLabel;
import com.chelseasystems.cr.swing.bean.JCMSTextField;
import com.chelseasystems.cs.boardingpass.CMSAirportDetails;
import com.chelseasystems.cs.customer.CMSCustomerHelper;

public class CMSCustomerBoardingPassPanel_EUR extends JPanel{
	
	private JCMSLabel lblDestination;
	private JCMSLabel lblFlightNumber;
	private JCMSLabel lblCheckInNumber;
	private JCMSLabel lblCompNumber;
	private JCMSLabel lblSeatNumber;
	private JCMSTextField txtFlightNumber;
	private JCMSTextField txtCheckInNumber;
	private JCMSTextField txtCompNumber;
	private JCMSTextField txtSeatNumber;
	private JCMSComboBox cbxDestination;
	private Vector vecDestination;
	private Container container;
	private GridBagLayout gbLayout;
	private GridBagConstraints gbConstraints; 
	private String flightNo;
	private String checkIn;
	private String comp;
	private String seatNo;
	private String entry;
	private String destination;
	private CMSAirportDetails airportDetails;
	private int fieldsModified = 0;
	public CMSCustomerBoardingPassPanel_EUR(CMSAirportDetails airportDetails) {
	    try {
	    	this.airportDetails = airportDetails;
	    	vecDestination = new Vector();
	    	jbInit();
	    	populateDestinations();
	    } catch (Exception e) {
	      e.printStackTrace();
	    }
	  }

	private void jbInit() {
		// TODO Auto-generated method stub
		lblDestination = new JCMSLabel("Destination");
		lblFlightNumber = new JCMSLabel("Flight No.");
		lblCheckInNumber = new JCMSLabel("Check-In No.");
		lblCompNumber = new JCMSLabel("Comp.");
		lblSeatNumber = new JCMSLabel("Seat No.");
		txtFlightNumber = new JCMSTextField();
		txtCheckInNumber = new JCMSTextField();
		txtCompNumber = new JCMSTextField();
		txtSeatNumber = new JCMSTextField();
		cbxDestination = new JCMSComboBox();
		 this.setLayout(new GridBagLayout());
		 txtFlightNumber.setText(airportDetails.getFlightNo());
		 flightNo = txtFlightNumber.getText();
		 txtCheckInNumber.setText(airportDetails.getCheckIn());
		 checkIn = txtCheckInNumber.getText();
		 txtCompNumber.setText(airportDetails.getComp());
		 comp = txtCompNumber.getText();
		 txtSeatNumber.setText(airportDetails.getSeatNo());
		 seatNo = txtSeatNumber.getText();
		
 	    this.setPreferredSize(new Dimension(400, 200));
				this.add(lblDestination
				 , new GridBagConstraints(0, 0, 2, 1, 0.0, 0.0, GridBagConstraints.SOUTHWEST
					        , GridBagConstraints.NONE, new Insets(4, 12, 0, 4), 4, 0));
			    this.add(cbxDestination
			    		, new GridBagConstraints(0, 1, 8, 1, 1.0, 0.0, GridBagConstraints.NORTHWEST
			    		        , GridBagConstraints.HORIZONTAL, new Insets(0, 8, 4, 4), 4, 4));
			    this.add(lblFlightNumber
			    		, new GridBagConstraints(0, 2, 2, 1, 0.0, 0.0, GridBagConstraints.SOUTHWEST
			    		        , GridBagConstraints.NONE, new Insets(4, 12, 0, 4), 4, 0));
			    this.add(txtFlightNumber
			    		, new GridBagConstraints(0, 3, 2, 1, 1.0, 0.0, GridBagConstraints.NORTHWEST
			    		        , GridBagConstraints.HORIZONTAL, new Insets(0, 8, 4, 4), 4, 0));
			    this.add(lblCheckInNumber
			    		, new GridBagConstraints(3, 2, 2, 1, 0.0, 0.0, GridBagConstraints.SOUTHWEST
			    		        , GridBagConstraints.NONE, new Insets(4, 12, 0, 4), 4, 0));
			    this.add(txtCheckInNumber
			    		 , new GridBagConstraints(3, 3, 2, 1, 1.0, 0.0, GridBagConstraints.NORTHWEST
				    		        , GridBagConstraints.HORIZONTAL, new Insets(0, 8, 4, 4), 4, 0));
			    this.add(lblCompNumber
			    		, new GridBagConstraints(6, 2, 2, 1, 0.0, 0.0, GridBagConstraints.SOUTHWEST
			    		        , GridBagConstraints.NONE, new Insets(4, 12, 0, 4), 4, 0));
			    this.add(txtCompNumber
			    		 , new GridBagConstraints(6, 3, 2, 1, 1.0, 0.0, GridBagConstraints.NORTHWEST
				    		        , GridBagConstraints.HORIZONTAL, new Insets(0, 8, 4, 4), 4, 0));
			    this.add(lblSeatNumber
			    		, new GridBagConstraints(9, 2, 2, 1, 0.0, 0.0, GridBagConstraints.SOUTHWEST
			    		        , GridBagConstraints.NONE, new Insets(4, 12, 0, 4), 4, 0));
			    this.add(txtSeatNumber
			    		, new GridBagConstraints(9, 3, 2, 1, 1.0, 0.0, GridBagConstraints.NORTHWEST
			    		        , GridBagConstraints.HORIZONTAL, new Insets(0, 8, 4, 4), 4, 0));
			  
	}
	public String getFlightNumber()
	  {
		if(isModified(txtFlightNumber,flightNo)){
			fieldsModified++;
		return txtFlightNumber.getText().trim();
		}
		return flightNo;
	  }

	  public void setFlightNumber(String sValue)
	  {
	    if (sValue == null || sValue.length() < 1)
	      return;
	    txtFlightNumber.setText(sValue);
	  }
	  
	  public String getCheckInNumber()
	  {
		  if(isModified(txtCheckInNumber,checkIn)){
			  fieldsModified++;
				return txtCheckInNumber.getText().trim();
		  }
				return checkIn;
	  }

	  public void setCheckInNumber(String sValue)
	  {
	    if (sValue == null || sValue.length() < 1)
	      return;
	    txtCheckInNumber.setText(sValue);
	  }
	  
	  public String getCompNumber()
	  {
		  if(isModified(txtCompNumber,comp)){
			  fieldsModified++;
				return txtCompNumber.getText().trim();
		  }
				return comp;
	  }

	  public void setCompNumber(String sValue)
	  {
	    if (sValue == null || sValue.length() < 1)
	      return;
	    txtCompNumber.setText(sValue);
	  }
	  
	  public String getSeatNumber()
	  {
		  if(isModified(txtSeatNumber,seatNo)){
			  fieldsModified++;
				return txtSeatNumber.getText().trim();
		  }
				return seatNo;
	  }

	  public void setSeatNumber(String sValue)
	  {
	    if (sValue == null || sValue.length() < 1)
	      return;
	    txtSeatNumber.setText(sValue);
	  }
	  public void setDestination(String sValue) {
		    if (sValue == null || sValue.length() < 1)
		      return;
		    cbxDestination.setSelectedItem(sValue);
		  }
	  public String getDestination() {
		    if( ((String) cbxDestination.getSelectedItem()).equalsIgnoreCase("")) return "";
		    if(isModified(cbxDestination,destination)){
				  fieldsModified++;
				  destination = ((String)cbxDestination.getSelectedItem()).trim();
				  }
		    destination = destination.replaceAll("\\s+", " ");
		    int i=destination.lastIndexOf(" ");
		    String dest = destination.substring(0, i)+ "-"+destination.substring(i+1,destination.length());
		     return (dest);
		    }
	
	private void populateDestinations() {
     try{
    	 boolean destFound=false;
			File airportlistfile = new File(FileMgr.getLocalFile("airportlist", "airportlist.dat"));
			 if(airportlistfile.exists())
			 {
	        	Scanner sc=new Scanner(airportlistfile);
	        	List destinations = new ArrayList();
	        	List destinationCode = new ArrayList();
	        	int i=0;
	        	while(sc.hasNextLine())
	        	{
	        		String dest=(String) sc.nextLine();
	                String[] datatokens = dest.split("[|]");
	                destinations.add(datatokens[0]);
	                destinationCode.add(datatokens[1]);
	                i++;
	                cbxDestination.addItem(String.format(datatokens[0]+"%40s",datatokens[1]));
	               }
	        	  String destination = airportDetails.getDestination();
	        	 if(destination!=null || destination.length()!=0){
	        	 for(int j=0;j<destinations.size();j++){
	        		 if(destination.equalsIgnoreCase((String)destinations.get(j))){
	        			 cbxDestination.setSelectedItem(String.format((String)destinations.get(j)+"%40s",(String)destinationCode.get(j)));
	        			 this.destination=((String)cbxDestination.getSelectedItem()).trim();
	        			 destFound=true;
	        			 break;
	        		 }
	        	 }
	        	 //Added by SonaliRaina to add Not Coded item on wrong destination
	        	 if(destFound==false){
	        		 String notCoded="Not"+" "+"Coded";
	        	      cbxDestination.addItem(notCoded);
	       			  cbxDestination.setSelectedItem(notCoded);
	       			}
	        	 }
	       		 
	       		 
		            
			 }
		}
		 catch(NullPointerException ne)
		 {
			 System.out.println("Unable to download AirPort List file");
		 }
		catch(Exception e){      
			e.printStackTrace();
		}
	  	}
	
	 public void setAppMgr(IApplicationManager theAppMgr) {
		    setBackground(theAppMgr.getBackgroundColor());
		    lblDestination.setAppMgr(theAppMgr);
		    cbxDestination.setAppMgr(theAppMgr);
		    lblFlightNumber.setAppMgr(theAppMgr);
		    txtFlightNumber.setAppMgr(theAppMgr);
		    lblCheckInNumber.setAppMgr(theAppMgr);
		    txtCheckInNumber.setAppMgr(theAppMgr);
		    lblCompNumber.setAppMgr(theAppMgr);
		    txtCompNumber.setAppMgr(theAppMgr);
		    lblSeatNumber.setAppMgr(theAppMgr);
		    txtSeatNumber.setAppMgr(theAppMgr);

		  }
	 public boolean isModified(JCMSTextField jTextField, String s){
         if(jTextField.getText().equalsIgnoreCase(s))
         return false;
		return true;
     }
	 public boolean isModified(JCMSComboBox jComboBox, String s){
         if(((String)jComboBox.getSelectedItem()).equalsIgnoreCase(s))
         return false;
		return true;
     }
	 public void keyTyped(KeyEvent ke) {
		 
		 txtFlightNumber = (JCMSTextField)ke.getComponent();
	      if (txtFlightNumber.getText().trim().length()>0 && txtFlightNumber.getText().equalsIgnoreCase(flightNo)) {
	        ke.consume();
	        return;
	      }
	      
	    }
	 public String getEntry() {
		 if(fieldsModified==0)
			 entry="A";
		 else if(fieldsModified>0)
			 entry="M";
		 return entry;
		}

		public void setEntry(String entry) {
			this.entry = entry;
		}
	 
	}
