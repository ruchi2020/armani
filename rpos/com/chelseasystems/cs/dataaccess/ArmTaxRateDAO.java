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
 | 1    | 02-17-2005 | Anand     | N/A       | Modified to add method to facilitate the     |
 |      |            |           |           | select location from table based on ZIP.     |
 --------------------------------------------------------------------------------------------
 */


package  com.chelseasystems.cs.dataaccess;

import  java.sql.SQLException;
import java.util.Date;

import  com.chelseasystems.cs.tax.ArmTaxRate;


public interface ArmTaxRateDAO extends BaseDAO
{

  /**
   * @param zipCode
   * @return
   * @exception SQLException
   */
  public ArmTaxRate selectByZipCode (String zipCode) throws SQLException;



  /**
   * @param zip
   * @param city
   * @param state
   * @return
   * @exception SQLException
   */
  public ArmTaxRate selectByZipCityState (String zip, String city, String state) throws SQLException;


  /**
   * @param zipCode
   * @return
   * @exception SQLException
   */
  public ArmTaxRate[] multipleSelectByZipCode (String zipCode) throws SQLException;
  
  public ArmTaxRate  selectByStateAndNullZip (String state) throws SQLException;
  
  //Vishal Yevale : 9 jan 2017
  public ArmTaxRate[] selectTaxTypeAndRate(String zip, String nation, String state, java.sql.Date process_dt) throws SQLException;
  
  public ArmTaxRate  selectByStateAndNullZip (String state, java.sql.Date process_dt) throws SQLException;
 
  public ArmTaxRate selectByZipCityState (String zip, String city, String state,java.sql.Date process_dt) throws SQLException;

  //end vishal yevale : 9 jan  2017

  public ArmTaxRate[] selectTaxTypeAndRate(String zip, String nation, String state) throws SQLException;
  
  

}



