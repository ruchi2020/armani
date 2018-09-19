/**
 * Title:        <p>
 * Description:  <p>
 * Copyright:    Copyright (c) <p>
 * Company:      <p>
 * @author
 * @version 1.0
 *
 * formatted with JxBeauty (c) johann.langhofer@nextra.at
 */


package com.chelseasystems.cs.tax;

import com.chelseasystems.cr.tax.*;
import com.chelseasystems.cr.pos.*;
import com.chelseasystems.cr.store.Store;
import java.util.Date;
import java.util.HashMap;


/**
 *
 * <p>Title: CMSTaxServices</p>
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
public abstract class CMSTaxServices extends TaxServices {

  /**
   * Default Constructor
   */
  public CMSTaxServices() {
  }

  /**
   * put your documentation comment here
   * @param zipCode
   * @return
   * @exception Exception
   */
  public abstract ArmTaxRate[] findByZipCode(String zipCode)
      throws Exception;
      
  
 
    public static void setCurrent(CMSTaxServices aTaxService)
	    {
	        current = aTaxService;
	    }

	    public static CMSTaxServices getCurrent()
	    {
	        return current;
	    }

	    public abstract SaleTax getTaxAmounts(CompositePOSTransaction compositepostransaction, Store store, Store store1, Date date)
	        throws Exception;
	    
	    public abstract SaleTax getTaxAmounts(CompositePOSTransaction compositepostransaction, Store store, Store store1, Date date, HashMap<String, Object[]> taxDetailMap)
		        throws Exception;

	    private static CMSTaxServices current;
	}

