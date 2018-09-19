/*
 * @copyright (c) 2002 Retek
 *
 * formatted with JxBeauty (c) johann.langhofer@nextra.at
 */
package com.chelseasystems.cs.customer;

import java.util.Date;
/**
 *
 * <p>Title: CMSCustomerDate</p>
 *
 * <p>Description: This file will return current Customer Creation date and modified date
 if Cashier creates new or modify the customer info</p>
 *
 * <p>Copyright: Copyright (c) 2009</p>
 *
 * <p>Company:Skillnet Solutions India </p>
 *
 * @author Vivek Sawant
 * @version 1.0
 */

public class CMSCustomerDate {

	
	public Date creationDate;

	public Date getCreationDate() {
		return creationDate;
	}

	public void setCreationDate(Date creationDate) {
		this.creationDate = creationDate;
	}
	
}
