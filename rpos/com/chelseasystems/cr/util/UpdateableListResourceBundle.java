/*
 * This unpublished work is protected by trade secret, copyright and other laws.
 * In the event of publication, the following notice shall apply:
 * Copyright © 2004 Retek Inc.  All Rights Reserved.
 *
 * formatted with JxBeauty (c) johann.langhofer@nextra.at
 */


package com.chelseasystems.cr.util;

import java.util.HashMap;


/**
 * @author Christian Greene
 */
public abstract class UpdateableListResourceBundle extends UpdateableResourceBundle {

  /**
   */
  public UpdateableListResourceBundle() {
    this.loadLookup();
  }

  /**
   * See class description.
   */
  protected abstract Object[][] getContents();

  /**
   * This function does the loading of the map.
   */
  private synchronized void loadLookup() {
    Object[][] contents = this.getContents();
    hmRes = new HashMap((int)(contents.length * 1.3));
    updateMap(contents);
  }
}

