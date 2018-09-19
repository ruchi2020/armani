/*
 * put your module comment here
 * formatted with JxBeauty (c) johann.langhofer@nextra.at
 */


package com.chelseasystems.cs.store;

import java.util.Date;

import com.chelseasystems.cr.store.Store;
import com.chelseasystems.cr.store.StoreServices;


/**
 *
 * <p>Title: CMSStoreServices</p>
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
public abstract class CMSStoreServices extends StoreServices {

  /**
   * Returns all store objects
   * @return CMSStore[]
   * @throws Exception
   */
  public abstract CMSStore[] findAllStores()
      throws Exception;
  
  //Poonam: Added for Expiry_DATE issue on Nov 22,2016 
  public abstract Store findByStoreId(String s,java.sql.Date process_dt)
	        throws Exception;
  //ends here
}

