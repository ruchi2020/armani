/*
 * put your module comment here
 * formatted with JxBeauty (c) johann.langhofer@nextra.at
 */
//
// Copyright 1999-2001, Chelsea Market Systems
//


package com.chelseasystems.cs.store;

import com.chelseasystems.cr.store.*;
import com.chelseasystems.cr.employee.*;
import com.chelseasystems.cs.dataaccess.*;
import java.util.*;


/**
 *
 * <p>Title: CMSStoreNullServices</p>
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
public class CMSStoreNullServices extends CMSStoreServices {

  /**
   * Default Constructor
   */
  public CMSStoreNullServices() {
  }

  /**
   * search a store by its id
   * @param storeId String
   * @return Store
   * @throws Exception
   */
  public Store findById(String storeId)
      throws Exception {
    return null;
  }
  
  /**Poonam S.
   * Added for Expiry_DATE issue on Nov 22,2016 
   * @param storeId String
   * @return Store
   * @throws Exception
   */
  public Store findByStoreId(String storeId,java.sql.Date process_dt)
      throws Exception {
    return null;
  }
  //ends here

  /**
   * search store for a city
   * @param city String
   * @return Store[]
   * @throws Exception
   */
  public Store[] findByCity(String city)
      throws Exception {
    return new CMSStore[0];
  }

  /**
   * search store form a given state
   * @param state String
   * @return Store[]
   * @throws Exception
   */
  public Store[] findByState(String state)
      throws Exception {
    return new CMSStore[0];
  }

  /**
   * search stores for the given state and city
   * @param city String
   * @param state String
   * @return Store[]
   * @throws Exception
   */
  public Store[] findByCityAndState(String city, String state)
      throws Exception {
    return new CMSStore[0];
  }

  /**
   *
   * @return CMSStore[]
   * @throws Exception
   */
  public CMSStore[] findAllStores()
      throws Exception {
    return new CMSStore[0];
  }

  /**
   *
   * @return Store[]
   * @throws Exception
   */
  public Store[] findAll()
      throws Exception {
    return new CMSStore[0];
  }

  /**
   * find stroes ids for the city
   * @param city String
   * @return String[]
   * @throws Exception
   */
  public String[] findIdsByCity(String city)
      throws Exception {
    return new String[0];
  }

  /**
   * find stores ids for the state
   * @param state String
   * @return String[]
   * @throws Exception
   */
  public String[] findIdsByState(String state)
      throws Exception {
    return new String[0];
  }

  /**
   * find store ids in the city and state.
   * @param city String
   * @param state String
   * @return String[]
   * @throws Exception
   */
  public String[] findIdsByCityAndState(String city, String state)
      throws Exception {
    return new String[0];
  }

  /**
   *
   * @return String[]
   * @throws Exception
   */
  public String[] findAllIds()
      throws Exception {
    return new String[0];
  }
}

