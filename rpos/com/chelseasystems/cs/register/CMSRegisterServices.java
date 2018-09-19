/**
 * Title:        <p>CMSRegisterServices
 * Description:  <p>Abstract implementation of RegisterServices for Chelsea Store.
 * Copyright:    Copyright (c) <p>2001
 * Company:      <p>Chelsea Store
 * @author David Fung
 * @version 1.0
 *  History:
 +------+------------+-----------+-----------+-------------------------------------------------+
 | Ver# | Date       | By        | Defect #  | Description                                     |
 +------+------------+-----------+-----------+-------------------------------------------------+
 | 2    | 04-28-2005 |Megha      |           |  Added new method selectByStoreAndRegID
 |      |            |           |           |   selectByStoreID                               |
 |      |            |           |           |                                                 |
 |      |            |           |           |                                                 |
 -----------------------------------------------------------------------------------------------
 *
 * formatted with JxBeauty (c) johann.langhofer@nextra.at
 */


package com.chelseasystems.cs.register;

import java.util.HashMap;

import com.chelseasystems.cr.register.Register;
import com.chelseasystems.cr.register.RegisterServices;


/**
 *
 * <p>Title: CMSRegisterServices</p>
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
public abstract class CMSRegisterServices extends RegisterServices {

  /**
   * This method is used to select by a RegisterID & StoreID
   * @param register String
   * @param store String
   * @return CMSRegister
   * @throws Exception
   */
  public abstract CMSRegister selectByStoreAndRegID(String register, String store)
      throws Exception;

  /**
   * This method is used to get all   Registers by  StoreID
   * @param store String
   * @return CMSRegister[]
   * @throws Exception
   */
  public abstract CMSRegister[] selectByStoreID(String store)
      throws Exception;
  
  
  
  /**
   * Vishal Yevale : Dec 2017 : POS_TS_NF25 Implementation PCR (FRANCE)
   * @param txnObject (CMSTransactionSOS OR CMSTransactionEOD)
   * @return MAP (Calculated  netAmount of Month and Year (if end of year)
   * @throws Exception
   */
  public abstract HashMap submitAndReturnNetAmount(Object txnObject) throws Exception;

}

