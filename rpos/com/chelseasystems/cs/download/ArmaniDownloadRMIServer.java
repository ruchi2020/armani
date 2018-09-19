/*
 * put your module comment here
 * formatted with JxBeauty (c) johann.langhofer@nextra.at
 */


package com.chelseasystems.cs.download;

/*
 History:
 +------+------------+-----------+-----------+----------------------------------------------------+
 | Ver# | Date       | By        | Defect #  | Description                                        |
 +------+------------+-----------+-----------+----------------------------------------------------+
 | 1    | 08-24-2005 | Manpreet  | N/A       |Download services                                   |
 +------+------------+-----------+-----------+----------------------------------------------------+
 */
import java.rmi.Remote;
import com.igray.naming.IPing;
import java.rmi.RemoteException;
import com.chelseasystems.cs.pos.AlterationItemGroup;
import com.chelseasystems.cs.config.ArmConfig;
import com.chelseasystems.cs.config.ArmDiscountRule;
import com.chelseasystems.cs.config.ArmPayConfigDetail;
import com.chelseasystems.cs.config.ArmPayPlanConfigDetail;
import com.chelseasystems.cs.config.ArmTaxRateConfig;


/**
 * <p>Title: </p>
 *
 * <p>Description: </p>
 *
 * <p>Copyright: Copyright (c) 2005</p>
 *
 * <p>Company: </p>
 *
 * @author not attributable
 * @version 1.0
 */
public interface ArmaniDownloadRMIServer extends Remote, IPing {

  /**
   * put your documentation comment here
   * @return
   * @exception RemoteException
   */
  public AlterationItemGroup[] getAllAlterationItemGroups()
      throws RemoteException;


  /**
   * put your documentation comment here
   * @param sCountry
   * @param sLanguage
   * @return
   * @exception RemoteException
   */
  public AlterationItemGroup[] getAlterationItemGroupsByCountryAndLanguage(String sCountry
      , String sLanguage)
      throws RemoteException;


  /**
   * put your documentation comment here
   * @param sCountry
   * @param sLanguage
   * @return
   * @exception RemoteException
   */
  public ArmConfig getConfigByCountryAndLanguage(String sCountry, String sLanguage)
      throws RemoteException;


  /**
   * put your documentation comment here
   * @param sCountry
   * @param sLanguage
   * @return
   * @exception RemoteException
   */
  public ArmPayConfigDetail[] getPayConfigByCountryAndLanguage(String sCountry, String sLanguage)
      throws RemoteException;


  public ArmPayPlanConfigDetail[] getPayPlanConfigByCountryAndLanguage(String sCountry, String sLanguage)
      throws RemoteException;
  
  /**
   * put your documentation comment here
   * @param sCountry
   * @param sLanguage
   * @return
   * @exception RemoteException
   */
  public ArmDiscountRule[] getDiscountRuleByCountryAndLanguage(String sCountry, String sLanguage)
    throws RemoteException;
  
  //Added by Vivek Mishra for PCR_TaxExceptionRulesImplementationChanges_US
  //start
  
  /**
   * put your documentation comment here
   * @param sState
   * @param sZipcode
   * @return
   * @exception Exception
   */
  public abstract ArmTaxRateConfig[] getExceptionTaxDetailByStateAndZipcode(String sState, String sZipcode)
      throws RemoteException;
  
  //end
}

