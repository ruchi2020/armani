/*
 * put your module comment here
 * formatted with JxBeauty (c) johann.langhofer@nextra.at
 */

package com.chelseasystems.cs.fiscaldocument;

/*
History:
+------+------------+-----------+-----------+----------------------------------------------------+
| Ver# | Date       | By        | Defect #  | Description                                        |
+------+------------+-----------+-----------+----------------------------------------------------+
| 2    | 06-24-2005 | Megha     | N/A       | TaxFree                                            |
+------+------------+-----------+-----------+----------------------------------------------------+
*/
import  java.util.*;
import  com.chelseasystems.cr.appmgr.IRepositoryManager;


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
public class TaxFreeHelper {

	/**
	 * put your documentation comment here
	 * @param theMgr
	 * @return
	 * @exception Exception
	 */
	public static TaxFree[] findAllTaxFree(IRepositoryManager theMgr) throws Exception {
		TaxFreeClientServices cs = (TaxFreeClientServices) theMgr.getGlobalObject("TAX_FREE_SRVC");
		return cs.findAllTaxFree();
	}

	/**
	 * put your documentation comment here
	 * @param theMgr
	 * @return
	 * @exception Exception
	 */
	public static TaxFree[] findTaxFreeForStore(IRepositoryManager theMgr, String storeID) throws Exception {
		TaxFreeClientServices cs = (TaxFreeClientServices) theMgr.getGlobalObject("TAX_FREE_SRVC");
		return cs.findTaxFreeForStore(storeID);
	}
}
