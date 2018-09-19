/*
 * put your module comment here
 * formatted with JxBeauty (c) johann.langhofer@nextra.at
 */
// Copyright 2001, Chelsea Market Systems


package com.chelseasystems.cs.tax;

import com.chelseasystems.cr.currency.*;
import com.chelseasystems.cr.pos.*;
import com.chelseasystems.cr.store.*;
import com.chelseasystems.cr.tax.*;
import com.igray.naming.*;
import java.rmi.*;
import java.lang.*;
import java.util.*;


/**
 *
 * <p>Title: ICMSTaxRMIServer</p>
 *
 * <p>Description: Defines the tax services that are available remotely via RMI.</p>
 *
 * <p>Copyright: Copyright (c) 2005</p>
 *
 * <p>Company: </p>
 *
 * @author not attributable
 * @version 1.0
 */
public interface ICMSTaxRMIServer extends Remote, IPing {

  /**
   * Return an array of SaleTax objects that represent the tax on each of the
   * taxable line item details.
   * @param aTxn the transaction for which to calculate taxes
   * @param fromStore the store that serves as the point-of-origin of the sale
   * @param toStore the store that serves as the point-of-acceptance of the sale
   * @param aDate the processing date for which to calculate tax
   */
  public SaleTax getTaxAmounts(CompositePOSTransaction aTxn, Store fromStore, Store toStore
      , Date aDate)
      throws Exception;

  public SaleTax getTaxAmounts(CompositePOSTransaction aTxn, Store fromStore, Store toStore
	      , Date aDate, HashMap<String, Object[]> taxDetailMap)
	      throws Exception;

  /**
   * put your documentation comment here
   * @param zipCode
   * @return
   * @exception Exception
   */
  public ArmTaxRate[] findByZipCode(String zipCode)
      throws Exception;
}

