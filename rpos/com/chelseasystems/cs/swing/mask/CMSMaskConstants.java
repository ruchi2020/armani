package com.chelseasystems.cs.swing.mask;

import com.chelseasystems.cr.appmgr.mask.MaskConstants;

/**
 * <p>Title: </p>
 * <p>Description: Customer specific masks</p>
 * <p>Copyright: Copyright (c) 2002</p>
 * <p>Company: </p>
 * @author Dan Reading
 * @version 1.0
 */

public interface CMSMaskConstants extends MaskConstants {
	// mask ids < 1000 are reserved for Retek use.
	// customer specific masks ids should be >= 1000 so as never
	// to conflict with the masks we provide.

	// a sample custom mask - enforces user entry as an integer in the range of 0-999.
	public static final int THREE_DIGIT_INTEGER_MASK = 500;
	public static final int CALENDAR_MASK = 501;
	public static final int QUANTIFIED_CURRENCY_MASK = 502;
}