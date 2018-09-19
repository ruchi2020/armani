/*
 * put your module comment here
 * formatted with JxBeauty (c) johann.langhofer@nextra.at
 */


package com.chelseasystems.cs.currency;

/*
 History:
 +------+------------+-----------+-----------+----------------------------------------------+
 | Ver# | Date       | By        | Defect #  | Description                                  |
 +------+------------+-----------+-----------+----------------------------------------------+
 | 1    | 06-16-2005 | Khyati    | N/A       | ArmCurrency Exchange rate                       |
 +------+------------+-----------+-----------+----------------------------------------------+
 */
import com.chelseasystems.cr.appmgr.*;


/**
 * <p>Title: </p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2005</p>
 * <p>Company: </p>
 * @author not attributable
 * @version 1.0
 */
public class CMSExchangeRateHelper {

  /**
   * put your documentation comment here
   * @param theAppMgr
   * @param sCountry
   * @param sLanguage
   * @return
   * @exception Exception
   */
  public static CurrencyRate[] findAllCurrencyRates(IRepositoryManager theAppMgr, String sCountry
      , String sLanguage)
      throws Exception {
    CMSExchangeRateClientServices cs = (CMSExchangeRateClientServices)theAppMgr.getGlobalObject(
        "CURRENCY_EXCHANGE_SRVC");
    return cs.findAllCurrencyRates(sCountry, sLanguage);
  }
}

