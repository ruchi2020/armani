/*
 * put your module comment here
 * formatted with JxBeauty (c) johann.langhofer@nextra.at
 */
// Copyright 2001,chelseamarketsystem


package com.chelseasystems.cs.store;

import java.rmi.*;
import java.util.Date;

import com.igray.naming.*;
import com.chelseasystems.cr.store.*;

import java.lang.*;


/**
 *
 * <p>Title: ICMSStoreRMIServer</p>
 *
 * <p>Description: Defines the customer services that are available remotely via RMI.</p>
 *
 * <p>Copyright: Copyright (c) 2005</p>
 *
 * <p>Company: </p>
 *
 * @author not attributable
 * @version 1.0
 */
public interface ICMSStoreRMIServer extends Remote, IPing {

  /**search a store by its id
   *@parm storeId storeId
   **/
  public CMSStore findById(String storeId)
      throws RemoteException;
  
  /**Added for Expiry_DATE issue on Nov 22,2016 
   *@parm storeId storeId
   **/
  public CMSStore findByStoreId(String storeId,java.sql.Date process_dt)
      throws RemoteException;
  //ends here

  /**search store for a city
   *@parm city city
   **/
  public CMSStore[] findByCity(String city)
      throws RemoteException;


  /**search store form a given state
   *@parm state state
   **/
  public CMSStore[] findByState(String state)
      throws RemoteException;


  /**search stores for the given state and city
   *@parm city city
   *@parm state state
   **/
  public CMSStore[] findByCityAndState(String city, String state)
      throws RemoteException;


  /**
   *
   * @return CMSStore[]
   * @throws RemoteException
   */
  public CMSStore[] findAll()
      throws RemoteException;


  /**
   * put your documentation comment here
   * @return
   * @exception RemoteException
   */
  public CMSStore[] findAllStores()
      throws RemoteException;


  /**find stroes ids for the city
   *@parm city city
   **/
  public String[] findIdsByCity(String city)
      throws RemoteException;


  /**find stores ids for the state
   *@parm state state
   **/
  public String[] findIdsByState(String state)
      throws RemoteException;


  /**find store ids in the city and state.
   *@parm city city
   *@parm state state
   **/
  public String[] findIdsByCityAndState(String city, String state)
      throws RemoteException;


  /**
   *
   * @return String[]
   * @throws RemoteException
   */
  public String[] findAllIds()
      throws RemoteException;
}

