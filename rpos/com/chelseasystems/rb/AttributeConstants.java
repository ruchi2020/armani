/**
 * Title:        TMW POS<p>
 * Description:  TMW Point of Sale<p>
 * Copyright:    Copyright (c) 1998<p>
 * Company:      Chelsea Market Systems<p>
 * @author Dan Reading
 * @version
 *
 * formatted with JxBeauty (c) johann.langhofer@nextra.at
 */
//   This interface simply defines the list of valid receipt blueprints
//   and the code is maintained by ReceiptArchitect

package com.chelseasystems.rb;

public interface AttributeConstants {
	public static final String ATTR_NAME = "Name";
	public static final String ATTR_STORE_COUNT = "Store Count";
	public static final String ATTR_CUSTOMER_COUNT = "Customer Count";
	public static final String ATTR_EJ_COUNT = "Electronic Journal";
	public static final String ATTR_STORE_FOOTER = "Store Footer";
	public static final String ATTR_STORE_LOGO = "Print Store Logo";
	public static final String ATTR_DEPENDS_ON = "Depends on Presence of";
	public static final String ATTR_VALUE_TO_PRINT = "Value to Print";
	public static final String ATTR_FIXED_LENGTH = "Fixed Length";
	public static final String ATTR_LINE_FEED = "Is Line Feed";
	public static final String ATTR_JUSTIFIED = "Justified";
	public static final String ATTR_DOUBLE_WIDTH = "Is Double Width Font";
	public static final String ATTR_BARCODE = "Is Printed as Barcode";
	public static final String ATTR_PRECEDED_BY_SPACE = "Is Preceded By Space";

	public static final String ATTR_REVERSE_SIGN = "Is Sign Reversed";
	public static final String ATTR_LEADING_FORMAT_POSITIVE = "Lead Format";
	public static final String ATTR_TRAILING_FORMAT_POSITIVE = "Trail Format";
	public static final String ATTR_LEADING_FORMAT_NEGATIVE = "Lead Format if Neg";
	public static final String ATTR_TRAILING_FORMAT_NEGATIVE = "Trail Format if Neg";
	public static final String ATTR_PRECISION = "Decimal Places Shown";
	public static final String ATTR_DATE_FORMAT = "Date Format";
	public static final String ATTR_CLASS_METHODS = "Object Navigation Path";
	public static final String ATTR_PRINT_IF_TRUE = "Value Printed if True";
	public static final String ATTR_PRINT_IF_FALSE = "Value Printed if False";
	public static final String ATTR_PRINT_IF_ZERO = "Is Printed When Zero";
	public static final String ATTR_PRINT_IF_LENGTH_ZERO = "Is Printed When Length is Zero";
	public static final String ATTR_MULTIPLY_BY = "Multiply By";

	public static final String DATE_FORMAT_MMDDYYYY = "MM/DD/YYYY";
	public static final String DATE_FORMAT_DDMMYYYY = "DD/MM/YYYY";
	public static final String DATE_FORMAT_YYYYMMDD = "YYYY/MM/DD";
	public static final String DATE_FORMAT_MONTHDDYYYY = "Month DD, YYYY";
	public static final String DATE_FORMAT_MMDD = "MM/DD";
	public static final String DATE_FORMAT_DDMM = "DD/MM";
	public static final String DATE_FORMAT_MMYY = "MM/YY";
	public static final String DATE_FORMAT_HH_MM_A = "HH:MM AM/PM";
	public static final String DATE_FORMAT_HH_MM = "HH:MM (military)";
	public static final String DATE_FORMAT_DAY_OF_WEEK_A = "Day of Week (abbreviated)";
	public static final String DATE_FORMAT_DAY_OF_WEEK = "Day of Week";

	public static final String FOOTER_NONE = "None";
	public static final String FOOTER_REGULAR = "Regular";
	public static final String FOOTER_EXPANDED = "Expanded";

	public static final String JUST_LEFT = "Left";
	public static final String JUST_CENTER = "Center";
	public static final String JUST_RIGHT = "Right";

	// etc.
}
