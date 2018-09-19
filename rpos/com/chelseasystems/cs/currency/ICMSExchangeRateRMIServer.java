/**
 * History (tab separated):-
 * Vers	Date		By	Spec		                Description
 * 1	1/24/05	        KS	POS_IS_CurrencyRate_Rev1	ICMSExchangeRateRMIServer
 *
 *
 * formatted with JxBeauty (c) johann.langhofer@nextra.at
 */


package com.chelseasystems.cs.currency;

import java.rmi.*;
import com.igray.naming.*;
import com.chelseasystems.cr.currency.*;
import com.chelseasystems.cs.currency.*;


/**
 *
 * <p>Title: ICMSExchangeRateRMIServer</p>
 *
 * <p>Description: This interface defines the customer services that are
 * available remotely via RMI</p>
 *
 * <p>Copyright: Copyright (c) 2005</p>
 *
 * <p>Company: </p>
 *
 * @author not attributable
 * @version 1.0
 */
public interface ICMSExchangeRateRMIServer extends Remote, IPing {

  /**
   * default-list
   * @param FromCurrencyType From ArmCurrency Type
   * @param ToCurrencyType To ArmCurrency type
   */
  //   public ArmCurrency getExchangeRate (CurrencyType FromCurrencyType, CurrencyType ToCurrencyType) throws RemoteException;
  /**
   * This method is used to find all currency rates
   */
  public CurrencyRate[] findAllCurrencyRates()
      throws RemoteException;


  /**
   * This method is used to find all currency rates
   */
  public CurrencyRate[] findAllCurrencyRates(String sCountry, String sLanguage)
      throws RemoteException;


  /**
   * This method is used to a currency rate
   */
  public ArmCurrency getExchangeRate(CurrencyType from, CurrencyType to)
      throws MissingExchangeRateException, RemoteException;
}

