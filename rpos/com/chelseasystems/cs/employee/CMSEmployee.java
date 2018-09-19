/*
 * put your module comment here
 * formatted with JxBeauty (c) johann.langhofer@nextra.at
 */
//
// Copyright 1999-2001, Chelsea Market Systems
//
/*
 History:
 +------+------------+-----------+-----------+----------------------------------------------+
 | Ver# | Date       | By        | Defect #  | Description                                  |
 +------+------------+-----------+-----------+----------------------------------------------+
 | 4    | 06-03-2005 | Vikram    | 114       | Unable to reset employee password            |
 |      |            |           |           | Overridden setPassword method                |
 --------------------------------------------------------------------------------------------
 | 3    | 02-14-2005 | Anand     | N/A       | Modified to add External ID                  |
 --------------------------------------------------------------------------------------------
 | 2    | 01-26-2005 | Manpreet  | N/A       | Modified  to add AddressLine2, Country, Email|
 +------+------------+-----------+-----------+----------------------------------------------+
 */


package com.chelseasystems.cs.employee;

import com.chelseasystems.cr.employee.Employee;
import com.chelseasystems.cr.appmgr.IRepositoryManager;
import com.chelseasystems.cr.telephone.*;
import com.chelseasystems.cr.rules.BusinessRuleException;


/**
 *
 * <p>Title: CMSEmployee</p>
 *
 * <p>Description: This store the employee information like address, email id,
 * customer id</p>
 *
 * <p>Copyright: Copyright (c) 2005</p>
 *
 * <p>Company: </p>
 *
 * @author Manpreet
 * @version 1.0
 */
public class CMSEmployee extends Employee implements com.chelseasystems.cr.rules.IRuleEngine {
  static final long serialVersionUID = 940707150178031335L;
  public static String emptyEmployeeID = "000-00-0000";
  /**
   * Store the address of employee
   */
  private String sAddressLine2;
  /**
   * Store the country of employee
   */
  private String sCountry;
  /**
   * Store the email of employee
   */
  private String sEmail;
  /**
   * Store the customer id of employee
   */
  private String sCustomerID;

  //  private String externalID;
  /**
   * Constructor
   * @param anId String
   */
  public CMSEmployee(String anId) {
    super(anId);
  }

  /**
   * Default Constructor
   */
  public CMSEmployee() {
    super(emptyEmployeeID);
  }

  // Added by Manpreet S Bawa - 01/26/2005
  /**
   * This method is used to get address line 2 of employee
   * @return String
   */
  public String getAddressLine2() {
    return this.sAddressLine2;
  }

  /**
   * This method is used to set address line 2 of employee
   * @param sAddressLine2 String
   */
  public void setAddressLine2(String sAddressLine2) {
    this.sAddressLine2 = sAddressLine2;
  }

  /**
   * This method is used to get country of employee
   * @return String
   */
  public String getCountry() {
    return this.sCountry;
  }

  /**
   * This method is used to set country employee
   * @param sCountry String
   */
    public void setCountry(String aString) throws BusinessRuleException {
        checkForNullParameter("setCountry", aString);
        String sCountry = aString.trim();
        executeRule("setCountry", sCountry);
        doSetCountry(sCountry);
    }

    /**
     * This method is used to set country employee
     * @param sCountry String
     */
    public void doSetCountry(String sCountry) {
    this.sCountry = sCountry;
  }

  /**
   * This method is used to get email id of employee
   * @return String
   */
  public String getEmail() {
    return this.sEmail;
  }

  /**
   * This method is used to set email id of employee
   * @param sEmail String
   */
  public void setEmail(String sEmail) {
    this.sEmail = sEmail;
  }

  /**
   * This method is used to get customer id of employee
   * @return String
   */
  public String getCustomerID() {
    return this.sCustomerID;
  }

  /**
   * This method is used to set customer id of employee
   * @param sCustomerID String
   */
  public void setCustomerID(String sCustomerID) {
    this.sCustomerID = sCustomerID;
  }

  //added by Anand on 02/14/2005 for external ID change
  //  public String getExternalId() {return this.externalID;}
  //  public void setExternalId(String externalID) {this.externalID = externalID;}
  /**
   * This method is used to create a unique short name of employee on the
   * basis of first name, middle name and last name.
   * @param theAppMgr IRepositoryManager
   * @param employee CMSEmployee
   * @return String
   * @throws Exception
   */
  public static String findUniqueShortName(IRepositoryManager theAppMgr, CMSEmployee employee)
      throws Exception {
    StringBuffer shortName = new StringBuffer();
    String makeUnique = "";
    if (employee.getFirstName().length() > 0)
      shortName.append(employee.getFirstName().substring(0, 1).toUpperCase());
    if (employee.getMiddleName().length() > 0)
      shortName.append(employee.getMiddleName().substring(0, 1).toUpperCase());
    else
      shortName.append("X");
    StringBuffer createLastName = new StringBuffer();
    char[] lastNameLetters = employee.getLastName().trim().toCharArray();
    for (int i = 0; i < lastNameLetters.length; i++)
      if (Character.isLetterOrDigit(lastNameLetters[i]))
        createLastName.append(lastNameLetters[i]);
    String lastName = createLastName.toString();
    if (lastName.length() > 11)
      shortName.append(lastName.substring(0, 11).toUpperCase());
    else
      shortName.append(lastName.toUpperCase());
    CMSEmployee testIfExists = CMSEmployeeHelper.findByShortName(theAppMgr, shortName.toString());
    int makeUniqueInt = 1;
    while (testIfExists != null) {
      makeUnique = Integer.toString(makeUniqueInt++);
      testIfExists = CMSEmployeeHelper.findByShortName(theAppMgr, shortName.toString() + makeUnique);
    }
    return shortName.toString() + makeUnique;
  }

  /**
   * This method is used to get the telephone no of customer
   * @return Telephone
   */
  public Telephone getTelephone() {
    //     System.out.println("tele: "+super.getTelephone()+" emp: "+ super.getId());
	  if (super.getTelephone() != null) {
			return new Telephone(super.getTelephone()) {
				public String toString() {
					return getFormattedNumber();
				}
			};
	  } 
	  return null;
  }

  /**
   *
   * @param aString String
   * @throws BusinessRuleException
   */
  //VM: Overridden to allow setting of blank Password irrespective of Nickname
  public void setPassword(String aString)
      throws BusinessRuleException {
    checkForNullParameter("setPassword", aString);
    String aPassword = aString.trim();
    executeRule("setPassword", aPassword);
    if (aPassword.equals("") || !aPassword.equals(getNickName())) {
      doSetPassword(aPassword);
      setModified();
    }
  }
// Issue # 1235
  public String getMaskPassword()
  {

      String password = getPassword();
      String maskPwd="";
      if(password!=null){
        for (int i=0;i<password.length();i++){
          maskPwd=maskPwd+"X";
        }

        return maskPwd;
      }else {

        return password;
      }
    }

}

