/*
 * @copyright (c) 2001 Chelsea Market Systems LLC
 *
 * formatted with JxBeauty (c) johann.langhofer@nextra.at
 */
/*
 History:
 +------+------------+-----------+-----------+----------------------------------------------+
 | Ver# | Date       | By        | Defect #  | Description                                  |
 +------+------------+-----------+-----------+----------------------------------------------+
 | 4    | 07-22-2005 | Vikram    | 286       | Changes for shipping to foreign countries.   |
 +------+------------+-----------+-----------+----------------------------------------------+
 | 3    | 06-02-2005 | Sameena   | 98        |Introduced null-check before setting every    |
 |      |            |           |           |part of address in method "setAddress"        |
 +------+------------+-----------+-----------+----------------------------------------------+
 | 2    | 04-12-2005 | Khyati    | N/A       |1.Send Sale specification.                    |
 --------------------------------------------------------------------------------------------
 | 1    | 04-12-2005 | Base      | N/A       |                                              |
 --------------------------------------------------------------------------------------------
 */


package com.chelseasystems.cs.pos;

import com.chelseasystems.cr.pos.ShippingRequest;
import com.chelseasystems.cr.pos.CompositePOSTransaction;
import com.chelseasystems.cs.address.Address;
import com.chelseasystems.cs.pos.CMSConsignmentLineItem;
import com.chelseasystems.cr.rules.BusinessRuleException;
import com.chelseasystems.cr.pos.POSLineItem;
import java.util.Vector;
import java.util.Enumeration;
import com.chelseasystems.cr.currency.ArmCurrency;


/**
 *
 * <p>Title: CMSShippingRequest</p>
 *
 * <p>Description: This class store the details of shipping request</p>
 *
 * <p>Copyright: Copyright (c) 2005</p>
 *
 * <p>Company: </p>
 *
 * @author Khyati
 * @version 1.0
 */
public class CMSShippingRequest extends ShippingRequest {

/**
	 * 
	 */
	private static final long serialVersionUID = -2100534216471338158L;
/**
	 * 
	 */
private String address2 = null;
  private Vector consignmentLineItems = null;
  private Vector presaleLineItems = null;
  private String addressFormat = null;
  private String seqNum = null;
  private CMSMiscItem miscItem = null;
  private ArmCurrency shippingFee = null;
  private Double offlineShipTax = null;
  private Double offlineStateTax = null;
  private Double offlineCityTax = null;
  private Double stateTax = null;
  
  private Double offlineGstTax = null;
  private Double offlineQstTax = null;
  private Double offlinePstTax = null; 
  private Double offlineHstTax = null; 


  /**
   * Constructor
   * @param    CompositePOSTranaction aCompositePOSTranaction
   */
  public CMSShippingRequest(CompositePOSTransaction aCompositePOSTranaction) {
    super(aCompositePOSTranaction);
    address2 = new String();
    consignmentLineItems = new Vector();
    presaleLineItems = new Vector();
  }

  /**
   * This method used to set the shipping address2
   * @param address2 String
   */
  public void setAddress2(String address2) {
    doSetAddress2(address2);
  }

  /**
   * This method used to set the shipping address2
   * @param address2 String
   */
  public void doSetAddress2(String address2) {
    this.address2 = address2;
  }

  /**
   * This method used to get the shipping address2
   * @return String
   */
  public String getAddress2() {
    return this.address2;
  }

  /**
   * This method used to set the shipping address
   * @param address Address
   */
  public void setAddress(Address address) {
    try {
      //SP - Introduced null-check before setting every part of address in method "setAddress"
      if (address != null) {
        if (address.getAddressLine1() != null)
          this.setAddress(address.getAddressLine1());
        this.setAddress2(address.getAddressLine2());
        if (address.getCity() != null)
          this.setCity(address.getCity());
        this.setCountry(address.getCountry());
        if (address.getState() != null)
          this.setState(address.getState());
        if (address.getZipCode() != null)
          this.setZipCode(address.getZipCode());
        if (address.getPrimaryPhone() != null)
          this.setPhone(address.getPrimaryPhone().getTelephoneNumber());
        if (address.getAddressFormat() != null)
          this.setAddressFormat(address.getAddressFormat());
      }
    } catch (Exception ex) {
      ex.printStackTrace();
    }
  }

  /**
   * This method is used to add the consignment line item
   * @param aLineItem CMSConsignmentLineItem
   */
  public void doAddConsignmentLineItem(CMSConsignmentLineItem aLineItem) {
    consignmentLineItems.addElement(aLineItem);
  }

  /**
   * This method is used to remove the consignment line item
   * @param aLineItem CMSConsignmentLineItem
   */
  public void doRemoveConsignmentLineItem(CMSConsignmentLineItem aLineItem) {
    consignmentLineItems.removeElement(aLineItem);
  }

  /**
   * This method is used to get the consignment line items
   * @return Enumeration
   */
  public Enumeration getConsignmentLineItems() {
    return consignmentLineItems.elements();
  }

  /**
   * This method is used to add the consignment line items
   * @return POSLineItem[]
   */
  public POSLineItem[] getConsignmentLineItemsArray() {
    return (POSLineItem[])consignmentLineItems.toArray(new POSLineItem[0]);
  }

  /**
   * This method is used to add the presale line item
   * @param aLineItem CMSPresaleLineItem
   */
  public void doAddPresaleLineItem(CMSPresaleLineItem aLineItem) {
    presaleLineItems.addElement(aLineItem);
  }

  /**
   * This method is used to remove the presale line item
   * @param aLineItem CMSPresaleLineItem
   */
  public void doRemovePresaleLineItem(CMSPresaleLineItem aLineItem) {
    presaleLineItems.removeElement(aLineItem);
  }

  /**
   * This method is used to get the presale line items
   * @return Enumeration
   */
  public Enumeration getPresaleLineItems() {
    return presaleLineItems.elements();
  }

  /**
   * This method is used to add the presale line items
   * @return POSLineItem[]
   */
  public POSLineItem[] getPresaleLineItemsArray() {
    return (POSLineItem[])presaleLineItems.toArray(new POSLineItem[0]);
  }

  /**
   * This method is used to delete the line item
   */
  public void doDelete() {
    super.doDelete();
    for (Enumeration aLineItemList = getConsignmentLineItems(); aLineItemList.hasMoreElements();
        ((CMSConsignmentLineItem)aLineItemList.nextElement()).doClearShippingRequest());
    consignmentLineItems = new Vector();
    for (Enumeration aLineItemList = getPresaleLineItems(); aLineItemList.hasMoreElements();
        ((CMSPresaleLineItem)aLineItemList.nextElement()).doClearShippingRequest());
    presaleLineItems = new Vector();
    //    getCompositeTransaction().doRemoveShippingRequest(this);
  }

  /**
   * This check whether the passing object is equal to ShippingRequest object or not
   * @param anObject Object
   * @return boolean
   */
  public boolean equals(Object anObject) {
    if (anObject instanceof ShippingRequest) {
      ShippingRequest other = (ShippingRequest)anObject;
      if (!(this == other))
        return false;
      return true;
    } else {
      return false;
    }
  }

  /**
   * This check whether the passing object is equal to ShippingRequest object or not
   * @param anObject Object
   * @return boolean
   */
  public boolean compare (Object shippingRequest) {
    if (shippingRequest == null || shippingRequest instanceof ShippingRequest == false)
      return false;
    CMSShippingRequest shipReq = (CMSShippingRequest) shippingRequest;
    if (this.getFirstName()!=null && !this.getFirstName().equals(shipReq.getFirstName()))
      return false;
    if (this.getLastName()!=null && !this.getLastName().equals(shipReq.getLastName()))
      return false;
    if (this.getAddress()!=null && !this.getAddress().equals(shipReq.getAddress()))
      return false;
    if (this.getAddress2()!=null && !this.getAddress2().equals(shipReq.getAddress2()))
      return false;
    if (this.getApartment()!=null && !this.getApartment().equals(shipReq.getApartment()))
      return false;
    if (this.getCity()!=null && !this.getCity().equals(shipReq.getCity()))
      return false;
    if (this.getState()!=null && !this.getState().equals(shipReq.getState()))
      return false;
    if (this.getZipCode()!=null && !this.getZipCode().equals(shipReq.getZipCode()))
      return false;
    if (this.getCountry()!=null && !this.getCountry().equals(shipReq.getCountry()))
      return false;
    if (this.getPhone()!=null && !this.getPhone().equals(shipReq.getPhone()))
      return false;
    if (this.getSpecialInstructions()!=null && !this.getSpecialInstructions().equals(shipReq.getSpecialInstructions()))
      return false;
    if (this.getGiftMessage()!=null && !this.getGiftMessage().equals(shipReq.getGiftMessage()))
      return false;
    return true;
  }

  /**
   * put your documentation comment here
   * @param shippingRequest
   * @exception BusinessRuleException
   */
  public void copy(CMSShippingRequest shippingRequest)
      throws BusinessRuleException {
    this.setFirstName(shippingRequest.getFirstName());
    this.setLastName(shippingRequest.getLastName());
    this.setAddress(shippingRequest.getAddress());
    //ks: Added address 2
    this.setAddress2(shippingRequest.getAddress2());
    this.setApartment(shippingRequest.getApartment());
    this.setCity(shippingRequest.getCity());
    this.setState(shippingRequest.getState());
    this.setZipCode(shippingRequest.getZipCode());
    this.setCountry(shippingRequest.getCountry());
    this.setPhone(shippingRequest.getPhone());
    this.setSpecialInstructions(shippingRequest.getSpecialInstructions());
    this.setGiftMessage(shippingRequest.getGiftMessage());
  }

  /**
   * This method used to set the shipping addressFormat
   * @param addressFormat String
   */
  public void setAddressFormat(String addressFormat) {
    doSetAddressFormat(addressFormat);
  }

  /**
   * This method used to set the shipping addressFormat
   * @param addressFormat String
   */
  public void doSetAddressFormat(String addressFormat) {
    this.addressFormat = addressFormat;
  }

  /**
   * This method used to get the shipping addressFormat
   * @return String
   */
  public String getAddressFormat() {
    return this.addressFormat;
  }

  public void setFirstName(String sName) {
    String aFirstName = sName.trim();
    if (!getFirstName().equals(aFirstName)) {
      // First Name is optional in case of Japan
      // but at DB end FirstName cant be NULL
      // so explicitly make it blank - MSB(11/17/05)
      if (aFirstName == null || aFirstName.length() < 1) aFirstName = " ";
      doSetFirstName(aFirstName);
    }
  }

  public void verifyFirstName(String sName) throws BusinessRuleException {
    checkForNullParameter("setFirstName", sName);
    String aFirstName = sName.trim();
    executeRule("setFirstName", aFirstName);
  }

  public void setSeqNum(String seqNum){
    return;
  }

  public void doSetSeqNum(String seqNum){
    this.seqNum = seqNum;
  }

  public String getSeqNum(){
    return this.seqNum;
  }

  /**
   * put your documentation comment here
   * @param miscItem
   */
  public void setMiscItem (CMSMiscItem miscItem) {
    this.miscItem = miscItem;
  }

  /**
   * put your documentation comment here
   * @return
   */
  public CMSMiscItem getMiscItem () {
    return  this.miscItem;
  }

  /**
   * put your documentation comment here
   * @return
   */
  public ArmCurrency getShippingFee () {
    return  this.shippingFee;
  }

  /**
   * put your documentation comment here
   * @param shippingFee
   */
  public void setShippingFee (ArmCurrency shippingFee) {
    this.shippingFee = shippingFee;
  }
  
  public Double getOfflineShipTax() {
		return offlineShipTax;
	}

	public void setOfflineShipTax(Double offlineShipTax) {
		this.offlineShipTax = offlineShipTax;
	}
	
	public Double getOfflineStateTax() {
		return offlineStateTax;
	}

	public void setOfflineStateTax(Double offlineStateTax) {
		this.offlineStateTax = offlineStateTax;
	}

	public Double getOfflineCityTax() {
		return offlineCityTax;
	}

	public void setOfflineCityTax(Double offlineCityTax) {
		this.offlineCityTax = offlineCityTax;
	}

	public Double getStateTax() {
		return stateTax;
	}
	
	public void setStateTax(Double stateTax) {
		this.stateTax = stateTax;
	}

	public Double getOfflineGstTax() {
		return offlineGstTax;
	}

	public void setOfflineGstTax(Double offlineGstTax) {
		this.offlineGstTax = offlineGstTax;
	}

	public Double getOfflineQstTax() {
		return offlineQstTax;
	}

	public void setOfflineQstTax(Double offlineQstTax) {
		this.offlineQstTax = offlineQstTax;
	}
	
	public Double getOfflinePstTax() {
		return offlinePstTax;
	}

	public void setOfflinePstTax(Double offlinePstTax) {
		this.offlinePstTax = offlinePstTax;
	}
	
	public Double getOfflineHstTax() {
		return offlineHstTax;
	}

	public void setOfflineHstTax(Double offlineHstTax) {
		this.offlineHstTax = offlineHstTax;
	}

}

