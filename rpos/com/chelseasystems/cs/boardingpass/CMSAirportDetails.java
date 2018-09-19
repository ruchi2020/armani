/*
* CMSAirportDetails.java
* 09/20/2013
* @copyright (c) 2013 SkillnetInc.
* Created by Shushma Priya
*/

package com.chelseasystems.cs.boardingpass;

import com.chelseasystems.cr.business.BusinessObject;

public class CMSAirportDetails extends BusinessObject{
	
    /**
	 * This class contains the getters and setters for all the variables regarding the airport list and boarding pass information
	 */
	private static final long serialVersionUID = 1L;
	private String airportDesc;
    private String airportCode;
    private String areaCode;
    private String flag;
    private String destination;
    private String flightNo;
    private String checkIn;
    private String comp;
    private String seatNo;
    private String entryType;
    private String transID;
    private String custName;
    private boolean isManual = false;
    
   	public String getAirportDesc() { return this.airportDesc; }
    public void setAirportDesc(String airportDesc) { this.airportDesc = airportDesc; }
    
    public String getAirportCode() { return this.airportCode; }
    public void setAirportCode(String airportCode) { this.airportCode = airportCode; }
    
    public String getAreaCode() { return this.areaCode; }
    public void setAreaCode(String areaCode) { this.areaCode = areaCode; }
    
    public String getFlag() { return this.flag; }
    public void setFlag(String flag) { this.flag = flag; }
    
    public String getDestination() { return this.destination; }
    public void setDestination(String destination) { this.destination = destination; }
    
    public String getFlightNo() { return this.flightNo; }
    public void setFlightNo(String flightNo) { this.flightNo = flightNo; }
    
    public String getCheckIn() { return this.checkIn; }
    public void setCheckIn(String checkIn) { this.checkIn = checkIn; }
    
    public String getComp() { return this.comp; }
    public void setComp(String comp) { this.comp = comp; }
    
    public String getSeatNo() { return this.seatNo; }
    public void setSeatNo(String seatNo) { this.seatNo = seatNo; }
    
    public String getEntryType() { return this.entryType; }
    public void setEntryType(String entryType) { this.entryType = entryType; }
    
    public String getTransID() { return this.transID; }
    public void setTransID(String transID) { this.transID = transID; }
    
    public String getCustName() { return this.custName; }
    public void setCustName(String custName) { this.custName = custName; }

   	public void setManuallyKeyed(boolean isManual) {
		// TODO Auto-generated method stub
		this.isManual=isManual;
	}
   	public boolean getManuallyKeyed() {
		// TODO Auto-generated method stub
		return this.isManual;
	}
    
   }
