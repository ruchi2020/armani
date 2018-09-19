/*
 * put your module comment here
 * formatted with JxBeauty (c) johann.langhofer@nextra.at
 */
//
// Copyright 1999-2001, Chelsea Market Systems
//
/*
 History:
 +------+------------+-----------+-----------+----------------------------------------------------+
 | Ver# | Date       | By        | Defect #  | Description                                        |
 +------+------------+-----------+-----------+----------------------------------------------------+
 | 1    | 04-29-2005 | Pankaja   | N/A       |To compute the media count                          |
 +------+------------+-----------+-----------+----------------------------------------------------+
 */


package com.chelseasystems.cs.txnposter;

import com.chelseasystems.cr.txnposter.PaymentSummary;


/**
 * <p>Title: </p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2005</p>
 * <p>Company: </p>
 * @author unascribed
 * @version 1.0
 */
public class CMSPaymentSummary extends PaymentSummary {
  int mediaCount = 0;

  /**
   * put your documentation comment here
   */
  public CMSPaymentSummary() {
    super();
  }

  /**
   * put your documentation comment here
   * @return
   */
  public int getMediaCount() {
    return mediaCount;
  }

  /**
   * put your documentation comment here
   * @param mediaCount
   */
  public void setMediaCount(int mediaCount) {
    doSetMediaCount(mediaCount);
  }

  /**
   * put your documentation comment here
   * @param mediaCount
   */
  public void doSetMediaCount(int mediaCount) {
    this.mediaCount = mediaCount;
  }
}

