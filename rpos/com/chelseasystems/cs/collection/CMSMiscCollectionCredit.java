package com.chelseasystems.cs.collection;

import com.chelseasystems.cr.store.Store;

public class CMSMiscCollectionCredit extends CMSMiscCollection {
	/**
	   * Constructor
	   * @param store Store
	   */
	  public CMSMiscCollectionCredit(Store store) {
	    super("misc", store);
	  }

	  /**
	   * Constructor
	   * @param type String
	   * @param store Store
	   */
	  public CMSMiscCollectionCredit(String type, Store store) {
	    super(type, store);
	  }
}
