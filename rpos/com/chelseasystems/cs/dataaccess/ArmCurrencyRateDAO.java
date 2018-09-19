/**
 * Description:ArmCurrencyRateDAO
 * Created By:Khyati Shah
 * Created Date:1/24/2005
 *
 * formatted with JxBeauty (c) johann.langhofer@nextra.at
 */
/**
 * History (tab separated):-
 * Vers	Date		By	Spec		                Description
 * 1	1/24/05	        KS	POS_IS_CurrencyRate_Rev1	ArmCurrencyRateDAO
 *
 */


package  com.chelseasystems.cs.dataaccess;

import  com.chelseasystems.cs.currency.CurrencyRate;
import  java.sql.SQLException;


public interface ArmCurrencyRateDAO extends BaseDAO
{

  /**
   *
   * @throws SQLException
   * @return CurrencyRate[]
   */
  public CurrencyRate[] selectAll () throws SQLException;



  /**
   *
   * @throws SQLException
   * @return CurrencyRate[]
   */
  public CurrencyRate[] selectAll (String sCountry, String sLanguage) throws SQLException;
}



